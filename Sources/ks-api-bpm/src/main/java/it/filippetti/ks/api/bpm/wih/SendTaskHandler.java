/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.wih;

import bpmn2.SendException;
import it.filippetti.ks.api.bpm.exception.CorrelationException;
import it.filippetti.ks.api.bpm.model.EventScope;
import it.filippetti.ks.api.bpm.model.EventScopeType;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.repository.InstanceRepository;
import it.filippetti.ks.api.bpm.service.RoutingService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
@WorkItemHandlerName({"Send Task", "External Send Task"})
public class SendTaskHandler implements WorkItemHandler, WorkItemSupport {
    
    private static final Logger log = LoggerFactory.getLogger(SendTaskHandler.class);
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private RoutingService routingService;

    @Autowired
    private InstanceRepository instanceRepository;
    
    public SendTaskHandler() {
    }
    
    /**
     * 
     * @param workItem
     * @param manager 
     */
    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> executeWorkItem 'Send Task' (%d)", workItem.getId()));
        
        long workItemId, senderInstanceId;
        String signalId, messageId, customScopeType;
        Boolean isSendTask;
        EventScopeType scopeType;
        EventScope scope;
        Map<String, Object> signalPayload, messagePayload, results;
        Instance senderInstance;
        
        
        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get workitem info
            workItemId = workItem.getId();
            senderInstanceId = workItem.getProcessInstanceId();

            // get sender instance
            senderInstance = instanceRepository.findById(senderInstanceId).orElseThrow(() -> {
                throw new IllegalStateException(String.format(
                    "Instance %d not found", 
                    senderInstanceId));
            });
            
            // get scope 
            customScopeType = (String) getNodeInstance(workItem)
                .getNode()
                .getMetaData()
                .get("customScopeType");
            scopeType = StringUtils.isBlank(customScopeType) ? 
                EventScopeType.organization : 
                EventScopeType.valueOf(customScopeType);
            switch (scopeType) {
                case instance: 
                    scope = EventScope.instance(senderInstance.getId());
                    break;
                case organization: 
                    scope = EventScope.organization();
                    break;
                default:
                    throw new IncompatibleClassChangeError();
            }          

            // init results
            results = new HashMap<>();
            
            // get signal parameters
            signalId = getParameter(workItem, "Signal", String.class, null);
            signalPayload = getParameter(workItem, "Data", Map.class, Collections.EMPTY_MAP);
            
            // route a signal
            // We are in that case because BPMN scope is 'external' and engine 
            // delegate to registered External Send Task workitem handler.
            if (signalId != null) {
                // route signal
                routingService.routeSignal(
                    senderInstance.getTenant(), 
                    senderInstance.getOrganization(),
                    signalId, 
                    signalPayload, 
                    scope, 
                    false);
                
                // set results
                results.put("signalId", signalId);
                results.put("signalPayload", signalPayload);
                results.put("scope", scope);
            }        
            // route a message            
            else {
                // get message parameter (is not a signal, hence required)
                messageId = getRequiredParameter(workItem, "MessageType", String.class);
                messagePayload = getRequiredParameter(workItem, "Message", Map.class);
                isSendTask = getParameter(workItem, "isSendTask", Boolean.class, false);
                try {
                    // route message async
                    routingService.routeMessage(
                        senderInstance.getTenant(), 
                        senderInstance.getOrganization(), 
                        messageId, 
                        messagePayload,
                        scope, 
                        true, 
                        workItem,
                        null);

                    // set results
                    results.put("messageId", messageId);
                    results.put("messagePayload", messagePayload);
                    results.put("scope", scope);
                } catch (CorrelationException e) {
                    if (isSendTask && e.getCorrelationError().isWorkflowError()) {
                        throw new SendException(e);
                    } else {
                        throw e;
                    }
                }
            }
            
            // complete workitem
            manager.completeWorkItem(workItemId, results);
        }
        catch (SendException e) {
            throw e;
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            
            // throw exception
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t);
            }
        }
        // commit
        transactionManager.commit(tx);
    }

    /**
     * 
     * @param workItem
     * @param manager 
     */
    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> abortWorkItem 'Send Task' (%d)", workItem.getId()));
    }    
}
