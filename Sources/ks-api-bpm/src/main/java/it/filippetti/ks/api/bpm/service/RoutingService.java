/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.filippetti.ks.api.bpm.command.CommandContext;
import it.filippetti.ks.api.bpm.command.RoutingCommand;
import it.filippetti.ks.api.bpm.command.callback.NotifyOnErrorCommandCallback;
import it.filippetti.ks.api.bpm.command.callback.WorkItemCommandCallback;
import it.filippetti.ks.api.bpm.configuration.RoutingConfiguration;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.exception.CorrelationException;
import it.filippetti.ks.api.bpm.model.Correlation;
import it.filippetti.ks.api.bpm.model.CorrelationError;
import it.filippetti.ks.api.bpm.model.CorrelationKey;
import it.filippetti.ks.api.bpm.model.CorrelationType;
import it.filippetti.ks.api.bpm.model.DeploymentReference;
import it.filippetti.ks.api.bpm.model.Event;
import it.filippetti.ks.api.bpm.model.EventScope;
import static it.filippetti.ks.api.bpm.model.EventScopeType.*;
import it.filippetti.ks.api.bpm.model.EventType;
import it.filippetti.ks.api.bpm.model.InstanceContext;
import it.filippetti.ks.api.bpm.model.InstanceReference;
import it.filippetti.ks.api.bpm.model.InstanceVariable;
import it.filippetti.ks.api.bpm.repository.DeploymentRepository;
import it.filippetti.ks.api.bpm.repository.EventRepository;
import it.filippetti.ks.api.bpm.repository.InstanceCorrelationRepository;
import it.filippetti.ks.api.bpm.repository.InstanceRepository;
import it.filippetti.ks.api.bpm.repository.InstanceVariableRepository;
import java.util.HashMap;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.NonUniqueResultException;
import org.drools.core.util.MVELSafeHelper;
import org.jbpm.services.api.DeploymentNotFoundException;
import org.jbpm.services.api.ProcessInstanceNotFoundException;
import org.kie.api.executor.ExecutionResults;
import org.kie.api.executor.ExecutorService;
import org.kie.api.runtime.process.WorkItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class RoutingService implements MessageListener, MessageConverter {
    
    private static final Logger log = LoggerFactory.getLogger(RoutingService.class);

    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired  
    private RoutingConfiguration routingConfiguration;
    
    @Autowired
    @Qualifier("internalObjectMapper")
    private ObjectMapper objectMapper;
    
    @Autowired
    private InstanceService instanceService;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private DeploymentRepository deploymentRepository;
    
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private InstanceCorrelationRepository instanceCorrelationRepository;
    
    @Autowired
    private InstanceVariableRepository instanceVariableRepository;

    /**
     * 
     * @param tenant
     * @param organization
     * @param signalId
     * @param signalPayload
     * @param scope 
     * @param async 
     */
    public void routeSignal(
        String tenant, 
        String organization,
        String signalId,
        Map<String, Object> signalPayload,
        EventScope scope,
        boolean async) {
        
        InstanceReference instance;
        
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        // manage async
        if (async) {
            signalId = String.format("ASYNC-%s", signalId);
        }
        
        switch (scope.getType()) {
            case instance:
                instance = instanceRepository.findActiveReferenceById(scope.getId());
                if (instance != null && 
                    instance.getTenant().equals(tenant) && 
                    instance.getOrganization().equals(organization)) {
                    try {
                        instanceService.signalProcessInstance(
                            instance.getUnitId(), 
                            instance.getId(),
                            signalId,
                            signalPayload);
                    } catch (DeploymentNotFoundException | ProcessInstanceNotFoundException e) {
                        log.warn(e.getMessage());
                    }
                }
                break;
            case organization:
                for (DeploymentReference deployment : deploymentRepository.findAllReferences(tenant, organization)) {
                    try {
                        instanceService.signalEvent(
                            deployment.getUnitId(), 
                            signalId, 
                            signalPayload);
                    } catch (DeploymentNotFoundException | ProcessInstanceNotFoundException e) {
                        log.warn(e.getMessage());
                    }
                }
                break;
            default:
                throw new IncompatibleClassChangeError();
        }
    }

    /**
     * 
     * @param tenant
     * @param organization
     * @param messageId
     * @param messagePayload
     * @param scope
     * @param async
     * @param sender
     * @param outcomeVariable
     * @return
     * @throws CorrelationException 
     */
    public Object routeMessage(
        String tenant, 
        String organization, 
        String messageId, 
        Map<String, Object> messagePayload, 
        EventScope scope,
        boolean async,
        WorkItem sender,
        String outcomeVariable) 
        throws CorrelationException {

        Event event;
        String queueName;
        RoutingConfiguration.Queue queueConfiguration;
        Map<String, Object> message;
        
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }

        // get event
        event = eventRepository.findByEventId(
            tenant, 
            organization, 
            messageId);
        if (event == null || event.getType() != EventType.message) {
            throw new CorrelationException(
                CorrelationError.message_not_defined, 
                String.format(
                    "Message '%s' is not defined",
                    messageId));
        }
        
        // get queue name
        queueName = event.getQueue();
        
        // route message internally
        if (queueName == null) {
            return routeMessageInternal(
                tenant,
                organization,
                messageId,
                messagePayload,
                scope,
                async,
                sender,
                outcomeVariable);
        }
        // route message through queue        
        else {
            // get queue configuration
            queueConfiguration = routingConfiguration.queue(queueName);
            if (queueConfiguration == null) {
                throw new IllegalStateException(String.format(
                    "Invalid queue: %s", 
                    queueName));
            }
            // custom producer
            if (queueConfiguration.hasCustomProducer()) {
                try {
                    message = queueConfiguration.customProducer().produceMessage(
                        queueName,    
                        tenant, 
                        organization, 
                        messageId, 
                        messagePayload);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } 
            // standard producer (no processing)
            else {
                message = messagePayload;
            }
            
            // send to jms queue
            routingConfiguration.jmsTemplate().convertAndSend(queueName, message);
            
            // return null outcome
            return null;
        }
    }

    
    /**
     * 
     * @param tenant
     * @param organization
     * @param messageId
     * @param messagePayload
     * @param scope
     * @param async
     * @param sender
     * @param outcomeVariable
     * @return
     * @throws CorrelationException 
     */
    public Object routeMessageInternal(
        String tenant, 
        String organization, 
        String messageId, 
        Map<String, Object> messagePayload, 
        EventScope scope,
        boolean async,
        WorkItem sender, 
        String outcomeVariable) 
        throws CorrelationException {
        
        Event event;
        String queueName, retrievalExpression, correlationSubscription;
        Correlation correlation;
        CorrelationType correlationType;
        CorrelationKey conversationKey, contextKey, correlationKey;
        CorrelationException correlationException;
        Map<String, Object> parameters;
        CommandContext commandContext;
        ExecutionResults executionResults;
        InstanceVariable instanceVariable;
        Object outcome;
        
        
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        // get event
        event = eventRepository.findByEventId(
            tenant, 
            organization, 
            messageId);
        if (event == null || event.getType() != EventType.message) {
            throw new CorrelationException(
                CorrelationError.message_not_defined, 
                String.format(
                    "Message '%s' is not defined",
                    messageId));
        }
        
        // get retrievalExpression
        retrievalExpression = event.getRetrievalExpression();

        // get correlationSubscription
        correlationSubscription = event.getCorrelationSubscription();

        // get queue name (for log only)
        queueName = event.getQueue();

        // search correlation
        correlation = null;
        correlationType = null;
        correlationKey = null;
        try {
            if (retrievalExpression != null) {
                // search by conversation
                conversationKey = evaluateCorrelationKey(messageId, retrievalExpression, messagePayload);
                correlation = instanceCorrelationRepository.findCorrelation(
                    tenant, organization, messageId, CorrelationType.conversation, conversationKey, scope);
                // set correlation anyway (conversation has precedence)
                correlationType = CorrelationType.conversation;
                correlationKey = conversationKey;
            }
            if (correlation == null && correlationSubscription != null) {
                // search by context
                contextKey = evaluateCorrelationKey(messageId, correlationSubscription, messagePayload);
                correlation = instanceCorrelationRepository.findCorrelation(
                    tenant, organization, messageId, CorrelationType.context, contextKey, scope);
                // set correlation only if found
                if (correlation != null) {
                    correlationType = CorrelationType.context;
                    correlationKey = contextKey;
                }
            }
        } catch (NonUniqueResultException e) {
            throw new CorrelationException(
                CorrelationError.correlation_non_unique, 
                String.format(
                    "Non unique correlation " + 
                    "for messageId '%s', scope %s",
                    messageId, scope));

        }
        if (correlation == null && !event.isStart()) {
            throw new CorrelationException(
                CorrelationError.correlation_not_found, 
                String.format(
                    "No correlation " + 
                    "for messageId '%s', scope %s",
                    messageId, scope));
        }

        // build command parameters
        parameters = new HashMap<>();
        parameters.put("tenant", tenant);
        parameters.put("organization", organization);
        parameters.put("messageId", messageId);
        parameters.put("messagePayload", messagePayload);
        parameters.put("scope", scope);
        parameters.put("queueName", queueName);        
        parameters.put("correlation", correlation);
        parameters.put("correlationType", correlationType);
        parameters.put("correlationKey", correlationKey);

        // outcome
        outcome = null;
        
        // async
        if (async) {
            // build command context
            commandContext = sender != null ? 
                new CommandContext(sender, WorkItemCommandCallback.class) :
                new CommandContext(NotifyOnErrorCommandCallback.class);
            commandContext.putData(parameters);
            
            // schedule command
            executorService.scheduleRequest(RoutingCommand.class.getName(), commandContext);
        }
        // not async
        else {
            // build command context
            commandContext = new CommandContext();
            commandContext.putData(parameters);
            
            // execute command
            executionResults = new RoutingCommand().execute(commandContext);
            
            // check and throw exception, if any
            correlationException = (CorrelationException) executionResults.getData("correlationException");
            if (correlationException != null) {
                throw correlationException;
            }
            
            //  get correlation
            correlation = (Correlation) executionResults.getData("correlation");
            
            // get and return outcome variable value, if any 
            if (outcomeVariable != null) {
                instanceVariable = instanceVariableRepository.findById(
                    correlation.getInstanceId(), 
                    InstanceContext.MAIN, 
                    outcomeVariable);
                if (instanceVariable != null) {
                    outcome = instanceVariable.getValue().asObject();
                }
            }
        }
        
        // return outcome
        return outcome;
    }

    @Override
    public void onMessage(Message message) {

        Queue queue;
        RoutingConfiguration.Queue queueConfiguration;
        String tenant, organization, messageId;
        Map<String, Object> messagePayload;

        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        try {
            // get queue
            queue = (Queue) message.getJMSDestination();

            // get queue configuration
            queueConfiguration = routingConfiguration.queue(queue.getQueueName());
            if (queueConfiguration == null) {
                throw new IllegalStateException(String.format(
                    "Invalid queue: %s", 
                    queue.getQueueName()));
            }
            
            // get message payload
            messagePayload = fromMessage(message);
            
            // custom consumer
            if (queueConfiguration.hasCustomConsumer()) {
                queueConfiguration.customConsumer().consumeMessage(
                    queue.getQueueName(),    
                    messagePayload);
            // standard consumer (internal routing)
            } else {
                // get messageId
                tenant = evaluateMessageIdValue(queueConfiguration.tenant(), messagePayload);
                organization = evaluateMessageIdValue(queueConfiguration.organization(), messagePayload);
                messageId = evaluateMessageIdValue(queueConfiguration.messageId(), messagePayload);
                
                // route message internally
                routeMessageInternal(
                    tenant,
                    organization,
                    messageId, 
                    messagePayload, 
                    EventScope.organization(),
                    queueConfiguration.async(),
                    null,
                    null);
            }
        } catch (Throwable t) {
            // set rollback only
            transactionManager
                .getTransaction(new DefaultTransactionDefinition())
                .setRollbackOnly();
            // log
            if (t instanceof JMSException || 
                t instanceof CorrelationException) {
                log.error(t.getMessage());
            } else {
                log.error(t.getMessage(), t);
            }
        }
    }
    
    @Override
    public Message toMessage(Object messagePayload, Session session)
        throws JMSException {

        try {
            return session.createTextMessage(objectMapper.writeValueAsString((Map) messagePayload));
        } catch (ClassCastException | JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> fromMessage(Message message)
        throws JMSException {

        try {
            return (Map) objectMapper.readValue(((TextMessage) message).getText(), Object.class);
        } catch (ClassCastException | JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }
    }
    
    /**
     * 
     * @param value
     * @param payload
     * @return
     * @throws CorrelationException 
     */
    private String evaluateMessageIdValue(
        String value, Map<String, Object> payload) 
        throws CorrelationException {
        if (value.startsWith("$")) {
            try {
                value = (String) MVELSafeHelper
                    .getEvaluator()
                    .eval(value.substring(1), payload);
            } catch (RuntimeException e) {
                throw new CorrelationException(
                    CorrelationError.message_id_not_evaluable, 
                    String.format("Cannot evaluate message id " +
                        "for expression '%s', payload '%s': %s",
                        value, payload, e.getMessage()));
            }
            if (value == null) {
                throw new CorrelationException(
                    CorrelationError.message_id_not_evaluable, 
                    String.format("Message id evaluates to null " +
                        "for expression '%s', payload '%s'",
                        value, payload));
            }                
        } 
        return value;
    }
    
    /**
     * 
     * @param messageId
     * @param expression
     * @param payload
     * @return
     * @throws ApplicationException 
     */
    private CorrelationKey evaluateCorrelationKey(
        String messageId, String expression, Map<String, Object> payload) 
        throws CorrelationException {
        
        Object correlationKey;
        
        try {
            correlationKey = MVELSafeHelper
                .getEvaluator()
                .eval(expression, payload);
        } catch (RuntimeException e) {
            throw new CorrelationException(
                CorrelationError.correlation_key_not_evaluable, 
                String.format(
                    "Cannot evaluate message '%s' correlation key " +
                    "for expression '%s', payload '%s': %s",
                    messageId, expression, payload, e.getMessage()));
        }
        if (correlationKey == null) {
            throw new CorrelationException(
                CorrelationError.correlation_key_not_evaluable, 
                String.format(
                    "Message '%s' correlation key evaluates to null " +
                    "for expression '%s', payload '%s'",
                    messageId, expression, payload));
        }
        
        return new CorrelationKey(correlationKey);
    }    
    
    /**
     * 
     */
    public interface QueueCustomConsumer {

        public abstract void consumeMessage(
            String queue, Map<String, Object> message) 
            throws Exception;
    }      
    
    public interface QueueCustomProducer {

        public abstract Map<String, Object> produceMessage(
            String queue, String tenant, String organization, String messageId, Map<String, Object> messagePayload) 
            throws Exception;
    }      
}
