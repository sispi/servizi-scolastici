/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.listener;

import com.google.common.base.Strings;
import it.filippetti.ks.api.bpm.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.bpm.configuration.ApplicationProperties;
import it.filippetti.ks.api.bpm.model.*;
import it.filippetti.ks.api.bpm.repository.*;
import it.filippetti.ks.api.bpm.service.DeploymentService;
import it.filippetti.ks.api.bpm.service.IdentityService;
import it.filippetti.ks.api.bpm.service.InstanceService;
import it.filippetti.ks.api.bpm.wih.WorkItemSupport;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.drools.core.util.MVELSafeHelper;
import org.jbpm.process.core.event.EventFilter;
import org.jbpm.process.core.event.EventTypeFilter;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.workflow.core.Node;
import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
import org.jbpm.workflow.core.node.*;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.jbpm.workflow.instance.impl.NodeInstanceImpl;
import org.jbpm.workflow.instance.impl.NodeInstanceResolverFactory;
import org.jbpm.workflow.instance.node.*;
import org.kie.api.event.process.*;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.EnvironmentName;
import org.kie.api.runtime.KieRuntime;
import org.kie.api.runtime.process.NodeInstanceContainer;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.kie.internal.task.api.model.InternalTask;
import org.mvel2.integration.VariableResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class InstanceListener implements ProcessEventListener, WorkItemSupport {
    
    private static final Logger log = LoggerFactory.getLogger(InstanceListener.class);
   
    @Value("${spring.jta.narayana.default-timeout:60}")
    private Integer transactionTimeout;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    ApplicationProperties applicationProperties;
    
    @Autowired
    private DeploymentService deploymentService;

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private DeploymentRepository deploymentRepository;
    
    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private InstanceNodeRepository instanceNodeRepository;

    @Autowired
    private InstanceVariableRepository instanceVariableRepository;

    @Autowired
    private InstanceCorrelationRepository instanceCorrelationRepository;

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private ChatRepository chatRepository;
    
    @Autowired
    private TaskScheduler taskScheduler;
    
    public InstanceListener() {
    }
    
    @Override
    public void beforeProcessStarted(ProcessStartedEvent event) {

        long m = System.currentTimeMillis();        
        log.info(String.format(">>> beforeProcessStarted (%d)", 
            event.getProcessInstance().getId()));

        long instanceId;
        Long parentInstanceId, rootInstanceId;
        RuleFlowProcessInstance processInstance;
        String unitId, processId, 
            creatorUserId, initiatorUserId, actorId,
            tenant, organization, profile, businessName,
            humanToken, systemToken, chatId;
        AuthenticationContext context;
        Deployment deployment;
        DeploymentUnit unit;
        Configuration configuration;
        Instance parentInstance;
        InstanceVariable variable;
        Chat chat;
        Map<String, Object> variables;
        
        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get event data
            instanceId = event.getProcessInstance().getId();
            parentInstanceId = event.getProcessInstance().getParentProcessInstanceId();        
            processInstance = (RuleFlowProcessInstance) event.getProcessInstance();

            // get unit id
            unitId = processInstance.getDeploymentId();

            // get process id
            processId = processInstance.getProcessId();

            // get authentication context
            context =  AuthenticationContextHolder.getOptional().orElse(null);

            // get deployment
            deployment = deploymentRepository.findByUnitId(unitId);
            if (deployment == null) {
                throw new IllegalStateException(String.format(
                    "Deployment %s not found", unitId));
            }

            // check deployment sync
            unit = deploymentService.getDeploymentUnit(unitId);
            if (unit == null || !deployment.getStatus().getDeployTs().equals(unit.getDeployTs())) {
                throw new IllegalStateException(String.format(
                    "Deployment %s out of sync", unitId));
            }

            // get process unit deployment
            if (!processId.equals(deployment.getProcessId())) {
                deployment = deployment.getUnitDeployments()
                    .stream()
                    .filter(d -> d.getProcessId().equals(processId))
                    .findFirst()
                    .orElseThrow(() -> {
                        throw new IllegalStateException(String.format(
                            "Deployment %s (%s) not found", unitId, processId));
                    });
            }

            // get parent instance
            parentInstance = parentInstanceId > 0 ?
                instanceRepository
                    .findById(parentInstanceId)
                    .orElseThrow(() -> {
                        throw new IllegalStateException(String.format(
                            "Deployment %s (%s) parent instance %d not found", 
                            unitId, processId, parentInstanceId));
                    }) : 
                null;
            
            // get root instance
            if (parentInstance != null) {
                if (parentInstance.getRootInstance().isPresent()) {
                    rootInstanceId = parentInstance.getRootInstance().get().getId();
                } else {
                    rootInstanceId = null;
                }
            } else {
                rootInstanceId = instanceId;
            }
            
            // get tenant
            tenant = deployment.getTenant();
            
            // get organization
            if (parentInstance != null) {
                organization = parentInstance.getOrganization();
            }
            else if (context != null) {
                organization = context.getOrganization();
            }
            else {
                organization = deployment.getOrganization();
            }

            // get business name
            businessName = deployment.getName();
            
            // get profile
            profile = (String) processInstance
                .getVariables()
                .getOrDefault(InstanceVariable.Name.PROFILE, "default");
            
            // get configuration
            configuration = deployment.getConfiguration(organization, profile);
            if (configuration == null) {
                throw new IllegalStateException(String.format(
                    "Deployment %s (%s) configuration for organization/profile '%s/%s' not found", 
                    unitId, processId, organization, profile));
            }

            // init variables
            variables = configuration.getSettings().copy().getMap(Settings.KEY_BUFFER_DEFAULTS);
            variables.putAll(processInstance.getVariables());
            variables.put(InstanceVariable.Name.PROFILE, profile);
            
            // get initiator/actor id
            if (parentInstance != null) {
                initiatorUserId = parentInstance.getInitiatorUserId();
                actorId = initiatorUserId;
            }
            else if (context != null) {
                initiatorUserId = context.getUserId();
                actorId = initiatorUserId;
            }
            else {
                initiatorUserId = applicationProperties.systemUser();
                actorId = null;
            }
            
            // get creator
            creatorUserId = (String) variables.get(InstanceVariable.Name.CREATOR_USER_ID);
            if (StringUtils.isBlank(creatorUserId)) {
                creatorUserId = initiatorUserId;
            }
            
            // get human token
            humanToken = (String) variables.get(InstanceVariable.Name.HUMAN_TOKEN);
            if (StringUtils.isBlank(humanToken)) {
                if (parentInstance != null) {
                    variable = instanceVariableRepository
                        .findById(
                            parentInstance, 
                            InstanceContext.MAIN, 
                            InstanceVariable.Name.HUMAN_TOKEN);
                    if (variable != null) {
                        humanToken = variable.getValue().asString();
                    }
                }
                else if (context != null) {
                    humanToken = context.getJwtToken();
                }
                else {
                    humanToken = null;
                }
            }

            // get system token
            systemToken = identityService.generateAdminJwtToken(tenant, organization);
            
            // get chat
            chatId = (String) variables.get(InstanceVariable.Name.CHAT_ID);
            if (StringUtils.isBlank(chatId)) {
                chatId = null;
            }
            if (parentInstance == null) {
                if (chatId == null) {
                    chatId = UUID.randomUUID().toString();
                }
                chat = chatRepository.findById(chatId).orElse(null);
                if (chat == null) {
                    // CHANGE00
                    chat = chatRepository.saveAndFlush(new Chat(chatId));
                }
            } else {
                chat = null;
            }

            // setup variables
            variables.put(InstanceVariable.Name.TENANT, tenant);
            variables.put(InstanceVariable.Name.ORGANIZATION, organization);
            variables.put(InstanceVariable.Name.INSTANCE_ID, instanceId);
            variables.put(InstanceVariable.Name.ROOT_INSTANCE_ID, rootInstanceId);
            variables.put(InstanceVariable.Name.ACTOR_ID, actorId);
            variables.put(InstanceVariable.Name.CREATOR_USER_ID, creatorUserId);
            variables.compute(InstanceVariable.Name.BUSINESS_NAME, (k, v) -> 
                StringUtils.isBlank((String) v) ? businessName : v
            );
            variables.compute(InstanceVariable.Name.BUSINESS_STATE, (k, v) -> 
                StringUtils.isBlank((String) v) ? null : v
            );            
            variables.compute(InstanceVariable.Name.SWIMLANES, (k, v) -> 
                v == null ? Collections.EMPTY_LIST : v
            );
            variables.compute(InstanceVariable.Name.CORRELATION_SUBSCRIPTION, (k, v) -> 
                StringUtils.isBlank((String) v) ? InstanceVariable.Name.INSTANCE_ID : v
            );
            variables.put(InstanceVariable.Name.CORRELATION_KEY, null);
            variables.put(InstanceVariable.Name.HUMAN_TOKEN, humanToken);
            variables.put(InstanceVariable.Name.SYSTEM_TOKEN, systemToken);
            variables.put(InstanceVariable.Name.CHAT_ID, chatId);

            // create and store instance
            // CHANGE00
            instanceRepository.saveAndFlush(new Instance(
                instanceId, 
                unit,
                deployment,
                parentInstance,
                configuration,
                creatorUserId,
                initiatorUserId,
                variables,
                chat));
            
            // set process variables (fire events)
            variables.forEach((name, value) -> {
                processInstance.setVariable(name, value);
            });
        }
        catch (Throwable t) {
            // log exception
            log.info(t.getMessage(), t);

            // rollback
            transactionManager.rollback(tx);
            
            //throw exception
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t);
            }
        }
        // commit
        transactionManager.commit(tx);
        
        log.info(String.format(">>> beforeProcessStarted (%d) completed in %d ms", 
            event.getProcessInstance().getId(), 
            Long.valueOf(System.currentTimeMillis() - m)));

    }

    @Override
    public void afterProcessStarted(ProcessStartedEvent event) {
    }

    @Override
    public void beforeProcessCompleted(ProcessCompletedEvent event) {
    }

    @Override
    public void afterProcessCompleted(ProcessCompletedEvent event) {
       
        long m = System.currentTimeMillis();                
        log.info(String.format(">>> afterProcessCompleted (%d)", 
            event.getProcessInstance().getId()));
        
        long instanceId;
        RuleFlowProcessInstance processInstance;
        int status;
        Instance instance;
        Map<String, Map> metadata;
        Map<String, Object> output;
        
        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get event data
            instanceId = event.getProcessInstance().getId();
            processInstance = (RuleFlowProcessInstance) event.getProcessInstance();

            // get status
            status = processInstance.getState();

            // get instance
            instance = instanceRepository
                .findById(instanceId)
                .orElseThrow(() -> {
                throw new IllegalStateException(String.format(
                    "Instance %d not found", 
                    instanceId));
                });
            
            // get output
            output = new HashMap<>();
            metadata = (Map) instance
                .getConfiguration()
                .orElseThrow(() -> {
                    throw new IllegalStateException(String.format(
                        "Instance %d configuration not found", 
                        instanceId));
                    })
                .getAsset(AssetType.project)
                .getContent()
                .asJsonAssetContent()
                .getMap("main", "settings", "flowBuffer");
            if (metadata != null) {
                metadata
                    .entrySet()
                    .stream()
                    .filter(e -> Boolean.TRUE.equals(e.getValue().getOrDefault("isOutput", false)))
                    .map(e -> e.getKey())
                    .forEach(k -> output.put(k, processInstance.getVariable(k)));
            }
            
            // complete instance
            instance.complete(status, output);
            
            // store instance
            // CHANGE00
            instance = instanceRepository.saveAndFlush(instance);
            
            // clean runtime data
            // we need to schedule clean outside and after current transaction
            // to avoid stale entity exceptions when deleting jbpm logs 
            if (instance.isRoot()) {
                taskScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        instanceService.cleanInstance(instanceId);
                    }
                }, Instant.now().plus(transactionTimeout, ChronoUnit.SECONDS));
            }
        }
        catch (Throwable t) {
            // log exception
            log.info(t.getMessage(), t);

            // rollback
            transactionManager.rollback(tx);
            
            //throw exception
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t);
            }
        }
        // commit
        transactionManager.commit(tx);
        
        log.info(String.format(">>> afterProcessCompleted (%d) completed in %d ms", 
            event.getProcessInstance().getId(),
            Long.valueOf(System.currentTimeMillis() - m)));        
    }

    @Override
    public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
 
        long m = System.currentTimeMillis();                
        log.info(String.format(">>> beforeNodeTriggered (%d, %d)", 
            event.getProcessInstance().getId(), 
            event.getNodeInstance().getId()));
        
        Long instanceId;
        RuleFlowProcessInstance processInstance;
        NodeInstance nodeInstance;
        String correlationSubscription;
        Object correlationKey;
        Instance instance;
        InstanceContext context;
        
        // skip end node instance
        if (event.getNodeInstance() instanceof EndNodeInstance) {
            return;
        }

        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get event data
            instanceId = event.getProcessInstance().getId();
            processInstance = (RuleFlowProcessInstance) event.getProcessInstance();
            nodeInstance = (NodeInstance) event.getNodeInstance();

            // get instance
            instance = instanceRepository
                .findById(instanceId)
                .orElseThrow(() -> {
                    throw new IllegalStateException(String.format(
                        "Instance %d not found", 
                        instanceId));
                });
            
            // get correlation subscription
            correlationSubscription = (String) processInstance
                .getVariable(InstanceVariable.Name.CORRELATION_SUBSCRIPTION);
            
            // update context correlation
            correlationKey = updateContextCorrelation(
                instance, correlationSubscription, new NodeInstanceResolverFactory(nodeInstance));

            // get context
            context = getInstanceContext(nodeInstance);
            
            // set process variables (fire events)
            processInstance.setVariable(InstanceVariable.Name.CONTEXT_ID, context.id());
            processInstance.setVariable(InstanceVariable.Name.CONTEXT_INSTANCE_ID, context.instanceId());
            processInstance.setVariable(InstanceVariable.Name.CONTEXT_KEY, context.key(instanceId));
            processInstance.setVariable(InstanceVariable.Name.CORRELATION_KEY, correlationKey);
            
            // flush all variables before node execution
            // CHANGE00
            // instanceVariableRepository.flush();
        }
        catch (Throwable t) {
            // log exception
            log.info(t.getMessage(), t);

            // rollback
            transactionManager.rollback(tx);
            
            //throw exception
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t);
            }
        }
        // commit
        transactionManager.commit(tx);
        
        log.info(String.format(">>> beforeNodeTriggered (%d, %d) completed in %d ms", 
            event.getProcessInstance().getId(), 
            event.getNodeInstance().getId(), 
            Long.valueOf(System.currentTimeMillis() - m)));        
    }

    @Override
    public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {

        long m = System.currentTimeMillis();        
        log.info(String.format(">>> afterNodeTriggered (%d, %d)", 
            event.getProcessInstance().getId(), 
            event.getNodeInstance().getId()));
        
        long instanceId;
        NodeInstanceImpl nodeInstance;
        NodeEventInfo nodeEventInfo;
        Instance instance;
        
        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get event data
            instanceId = event.getProcessInstance().getId();
            nodeInstance = (NodeInstanceImpl) event.getNodeInstance();
            
            // get instance
            instance = instanceRepository
                 .findById(instanceId)
                 .orElseThrow(() -> {
                    throw new IllegalStateException(String.format(
                         "Instance %d not found", 
                         instanceId));
                 });
            
            // set last activity time
            instance.setLastActivityTs();
            
            // store instance
            instance = instanceRepository.save(instance);

            // get node event info
            nodeEventInfo = getNodeEventInfo(nodeInstance);

            // create node
            createInstanceNode(instance, nodeInstance, nodeEventInfo);

            // update conversation correlation
            if (nodeEventInfo != null && nodeEventInfo.isMessageThrow()) {
                createConversationCorrelation(
                    instance, nodeEventInfo.id(), nodeEventInfo.payload());
            }
        }
        catch (Throwable t) {
            // log exception
            log.info(t.getMessage(), t);

            // rollback
            transactionManager.rollback(tx);
            
            //throw exception
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t);
            }
        }
        // commit
        transactionManager.commit(tx);

        log.info(String.format(">>> afterNodeTriggered (%d, %d) completed in %d ms", 
            event.getProcessInstance().getId(), 
            event.getNodeInstance().getId(), 
            Long.valueOf(System.currentTimeMillis() - m)));
        
    }

    @Override
    public void beforeNodeLeft(ProcessNodeLeftEvent event) {
        
        long m = System.currentTimeMillis();                
        log.info(String.format(">>> beforeNodeLeft (%d, %d)", 
            event.getProcessInstance().getId(), event.getNodeInstance().getId()));
        
        long instanceId;
        RuleFlowProcessInstance processInstance;
        NodeEventInfo nodeEventInfo; 
        NodeInstanceImpl nodeInstance;
        Instance instance;

        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get event data
            instanceId = event.getProcessInstance().getId();
            processInstance = (RuleFlowProcessInstance) event.getProcessInstance();
            nodeInstance = (NodeInstanceImpl) event.getNodeInstance();

            // get instance
            instance = instanceRepository
                .findById(instanceId)
                .orElseThrow(() -> {
                    throw new IllegalStateException(String.format(
                         "Instance %d not found", 
                         instanceId));
                });

            // get node event info
            nodeEventInfo = getNodeEventInfo(nodeInstance);
            
            // complete node
            completeInstanceNode(instance, nodeInstance, nodeEventInfo);

            // set process variables (fire events)
            if (nodeInstance instanceof WorkItemNodeInstance && ((WorkItemNodeInstance) nodeInstance).getWorkItem() != null) {
                processInstance.setVariable(
                    InstanceVariable.Name.LAST_WORKITEM_STATE, 
                    ((WorkItemNodeInstance) nodeInstance).getWorkItem().getState());
                processInstance.setVariable(
                     InstanceVariable.Name.LAST_WORKITEM_RESULTS,
                     ((WorkItemNodeInstance) nodeInstance).getWorkItem().getResults());
            }

            // update conversation correlation
            if (nodeEventInfo != null && nodeEventInfo.isMessageCatch()) {
                createConversationCorrelation(
                    instance, nodeEventInfo.id(), nodeEventInfo.payload());
            }            

            // update instance fact
            updateInstanceFact(processInstance);
        }
        catch (Throwable t) {
            // log exception
            log.info(t.getMessage(), t);

            // rollback
            transactionManager.rollback(tx);
            
            //throw exception
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t);
            }
        }
        // commit
        transactionManager.commit(tx);
        
        log.info(String.format(">>> beforeNodeLeft (%d, %d) completed in %d ms", 
            event.getProcessInstance().getId(), 
            event.getNodeInstance().getId(), 
            Long.valueOf(System.currentTimeMillis() - m)));        
    }

    @Override
    public void afterNodeLeft(ProcessNodeLeftEvent event) {
    }

    @Override
    public void beforeVariableChanged(ProcessVariableChangedEvent event) {
    }

    @Override
    public void afterVariableChanged(ProcessVariableChangedEvent event) {

        long m = System.currentTimeMillis();        
        log.info(String.format(">>> afterVariableChanged (%d, %s)", 
            event.getProcessInstance().getId(), 
            event.getVariableId()));
        
        long instanceId;
        String name;
        Object value;
        Boolean business, in, out, config;
        Instance instance;
        InstanceContext context;
        InstanceVariable variable;
        Map<String, Object> metadata;
        
        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get event data
            instanceId = event.getProcessInstance().getId();

            // get name
            name = event.getVariableId().contains(InstanceContext.SEPARATOR) ? 
                StringUtils.substringAfterLast(event.getVariableId(), InstanceContext.SEPARATOR) :
                event.getVariableId();

            // get value
            value = event.getNewValue();

            // get context
            context = getInstanceContext(event.getVariableId(), event.getVariableInstanceId());

            // get instance
            instance = instanceRepository.findById(instanceId).orElse(null);

            // In some scenario (subprocess), due to call stack mechanism of jbpm,
            // variable changed event fires before process start event.
            // This is not a problem because we'll store variable when process start 
            // event occurrs so we can skip event management
            if (instance != null) {
                // get variable
                variable = instanceVariableRepository.findById(instance, context, name);

                if (variable == null) {
                    // get flags
                    business = false;
                    in = false;
                    out = false;
                    config = false;
                    if (context.isMain()) {   
                        metadata = instance
                            .getConfiguration()
                            .orElseThrow(() -> {
                            throw new IllegalStateException(String.format(
                                "Instance %d configuration not found", 
                                instanceId));
                            })
                            .getAsset(AssetType.project)
                            .getContent()
                            .asJsonAssetContent()
                            .getMap("main", "settings", "flowBuffer", name);
                        if (metadata != null) {
                            business = (Boolean) metadata.getOrDefault("isInfo", false);
                            in = (Boolean) metadata.getOrDefault("isInput", false);
                            out = (Boolean) metadata.getOrDefault("isOutput", false);
                            config = (Boolean) metadata.getOrDefault("isConfig", false);
                        }
                    }
                    // create variable
                    variable = new InstanceVariable(
                        instance, context, name, business, in, out, config);
                }

                // update variable
                variable.update(value);

                // store variable
                instanceVariableRepository.save(variable);

                // update instance
                if (context.isMain()) {
                    // set business name
                    if (name.equals(InstanceVariable.Name.BUSINESS_NAME) && value != null) {
                        instance.setBusinessName((String) value);
                    }
                    // set business state
                    if (name.equals(InstanceVariable.Name.BUSINESS_STATE)) {
                        instance.setBusinessState((String) value);
                    }
                    // add swimlane accesses
                    if (name.equals(InstanceVariable.Name.SWIMLANES)) {
                        for (String identity : (List<String>) value) {
                            instance.addAccess(identity, InstanceAccessType.swimlane, null);
                        }
                    }
                    // store instance
                    // CHANGE00
                    instanceRepository.saveAndFlush(instance);
                }
            }
        }
        catch (Throwable t) {
            // log exception
            log.info(t.getMessage(), t);

            // rollback
            transactionManager.rollback(tx);
            
            //throw exception
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t);
            }
        }
        // commit
        transactionManager.commit(tx);
        
        log.info(String.format(">>> afterVariableChanged (%d, %s) completed in %d ms", 
            event.getProcessInstance().getId(), 
            event.getVariableId(), 
            Long.valueOf(System.currentTimeMillis() - m)));        
    }
    
    /**
     * 
     * @param instance
     * @param nodeInstance
     * @param nodeEventInfo
     * @return 
     */
    private InstanceNode createInstanceNode(
        Instance instance,
        NodeInstance nodeInstance,
        NodeEventInfo nodeEventInfo) {

        long nodeInstanceId;
        String nodeId, nodeName, nodeType, incomingConnection, businessKey, swimlaneActorId, refuseGroupId;
        Object input;
        WorkItem workItem;
        Long subprocessInstanceId;
        Set<String> identities;
        Map<String, Object> metadata;
        InstanceContext context;
        InstanceNode node;
        InstanceEvent nodeEvent;
        InternalTask internalTask;
        Task task;

        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        // get nodeInstanceId
        nodeInstanceId = nodeInstance.getId();
        
        // get node
        node = instanceNodeRepository.findById(instance, nodeInstanceId);
        if (node != null) {
           return node;
        }
        
        // get node info
        nodeInstanceId = nodeInstance.getId();
        if (nodeInstance.getNode() != null) {
           nodeId = (String) nodeInstance.getNode().getMetaData().get("UniqueId");
           nodeName = nodeInstance.getNode().getName();
           nodeType = nodeInstance.getNode().getClass().getSimpleName();
        } else {
           nodeId = Long.toString(nodeInstance.getNodeId());
           nodeName = nodeInstance.getNodeName();
           nodeType = (String) ((NodeInstanceImpl) nodeInstance).getMetaData("NodeType");
        }

        if (nodeName==null)
            nodeName = nodeType;

        // get incoming connection
        incomingConnection = (String) ((NodeInstanceImpl) nodeInstance)
            .getMetaData()
            .get("IncomingConnection");

        // get node event
        nodeEvent = null;
        if (nodeEventInfo != null) {
            nodeEvent = new InstanceEvent(
                nodeEventInfo.id(), 
                nodeEventInfo.type(),
                nodeEventInfo.nodeType(),
                nodeEventInfo.isCorrelable());
        }

        // get workItem
        workItem = null;
        if (nodeInstance instanceof WorkItemNodeInstance && ((WorkItemNodeInstance) nodeInstance).getWorkItem() != null) {
            workItem = ((WorkItemNodeInstance) nodeInstance).getWorkItem();
        }

        // get input
        input = null;
        if (workItem != null) {
            input = workItem.getParameters();
        }
        else if (nodeEventInfo != null && nodeEventInfo.isThrow()) {
            input = nodeEventInfo.payload();
        }

        // get subprocessInstanceId
        subprocessInstanceId = null;
        if (nodeInstance instanceof SubProcessNodeInstance) {
            subprocessInstanceId = ((SubProcessNodeInstance) nodeInstance).getProcessInstanceId();
        }        

        // get metadata
        metadata = new HashMap<>();
        metadata.put("node", nodeInstance.getNode().getMetaData());
        metadata.put("nodeInstance", ((NodeInstanceImpl) nodeInstance).getMetaData());

        // get context
        context = getInstanceContext(nodeInstance);

        // create node
        node = new InstanceNode(
            instance,
            nodeInstanceId,
            context, 
            nodeId, 
            nodeName,
            nodeType, 
            incomingConnection,
            nodeEvent,
            input,
            metadata, 
            workItem,
            subprocessInstanceId);

        // store node
        // CHANGE00
        node = instanceNodeRepository.saveAndFlush(node);

        // manage task
        if (nodeInstance instanceof HumanTaskNodeInstance) {
            
            // extract assignment identities
            identities = new HashSet<>();
            swimlaneActorId = null;
            refuseGroupId = null;
            for(Task.Assignment assignment: Task.Assignment.values()){
                String assignmentValue = (String) workItem.getParameter(assignment.name());
                if(!StringUtils.isBlank(assignmentValue)){
                    String[] assignmentIdentities = assignmentValue.split(Task.ASSIGNMENT_SEPARATOR, -1);
                    for(String identity: assignmentIdentities){
                        if(!StringUtils.isBlank(identity)){
                            identities.add(identity);
                            if (assignment == Task.Assignment.SwimlaneActorId) {
                                swimlaneActorId = identity;
                            }
                            if (assignment == Task.Assignment.RefuseGroupId) {
                                refuseGroupId = identity;
                            }
                        }
                    }
                }
            }

            // get internal task (needs to flush jbpm command scope em to see changes)
            getCommandScopedEntityManager(instance).flush();
            internalTask = taskRepository.findInternalByWorkItemId(workItem.getId());
            if (internalTask == null) {
                throw new IllegalStateException(String.format(
                    "Internal task for workitem %d not found", 
                    workItem.getId()));
            }

            // get business key
            businessKey = String.valueOf(getParameter(
                workItem, 
                Task.BUSINESS_KEY_PARAMETER, 
                Object.class, 
                internalTask.getId()));
            
            // create task
            task = new Task(
                node, 
                internalTask,
                workItem,
                businessKey,
                swimlaneActorId,
                refuseGroupId);                    

            // store task
            // CHANGE00
            task = taskRepository.saveAndFlush(task);

            if (!identities.isEmpty()) {
                // add task accesses
                for (String identity : identities) {
                    instance.addAccess(identity, InstanceAccessType.task, task);
                }
                // store instance
                // CHANGE00
                instanceRepository.saveAndFlush(instance);
            }
        }
        return node;
    }
    
    /**
     * 
     * @param instance
     * @param nodeInstance
     * @param nodeEventInfo
     * @return 
     */
    private InstanceNode completeInstanceNode(
        Instance instance,
        NodeInstance nodeInstance,
        NodeEventInfo nodeEventInfo) {
        
        Object output;
        WorkItem workItem;
        InstanceNode node;
        Task task;
        
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        // get output
        output = null;
        if (nodeInstance instanceof WorkItemNodeInstance && ((WorkItemNodeInstance) nodeInstance).getWorkItem() != null) {
            output = ((WorkItemNodeInstance) nodeInstance).getWorkItem().getResults();
        }
        else if (nodeEventInfo != null && nodeEventInfo.isCatch()) {
            output = nodeEventInfo.payload();
        }

        // get node
        node = createInstanceNode(instance, nodeInstance, nodeEventInfo);
        
        // complete node
        node.complete(output);

        // store node
        // CHANGE00
        instanceNodeRepository.saveAndFlush(node);

        // manage task
        if (nodeInstance instanceof HumanTaskNodeInstance) {

             // get workItem
            workItem = ((HumanTaskNodeInstance) nodeInstance).getWorkItem();

            // get task
            task = taskRepository.findByWorkItemId(workItem.getId());
            if (task == null) {
                throw new IllegalStateException(String.format(
                    "Task for workitem %d not found", 
                    workItem.getId()));
            }

            // complete task
            task.complete(workItem.getResults());

            // store task
            // CHANGE00
            taskRepository.saveAndFlush(task);
        }
        return node;
    }
    
    /**
     * 
     * @param instance
     * @param correlationSubscription
     * @param variableResolverFactory
     * @return 
     */
    private Object updateContextCorrelation(
        Instance instance, 
        String correlationSubscription,
        VariableResolverFactory variableResolverFactory) {
        
        Object correlationKeyValue;
        CorrelationKey correlationKey;
        InstanceCorrelation correlation;
        
        // evaluate context correlation key
        try {
            correlationKeyValue = MVELSafeHelper
                .getEvaluator()
                .eval(correlationSubscription, variableResolverFactory);
        } catch (RuntimeException e) {
            throw new RuntimeException(
                String.format(
                    "Cannot evaluate instance %d context correlation key for correlationSubscription '%s': %s", 
                    instance.getId(), correlationSubscription, e.getMessage()));
        }
        if (correlationKeyValue == null) {
            throw new RuntimeException(
                String.format(
                    "Instance %d context correlation key evaluates to null for correlationSubscription '%s'", 
                    instance.getId(), correlationSubscription));
        }

        // build new correlation key
        correlationKey = new CorrelationKey(correlationKeyValue);

        // get correlation
        correlation = instanceCorrelationRepository.findContextCorrelation(instance);
        if (correlation == null) {
            // create correlation
            correlation = InstanceCorrelation.newContextCorrelation(instance, correlationKey);
        } else if (!correlation.getCorrelationKey().equals(correlationKey)) {
            // update correlation
            correlation.setCorrelationKey(correlationKey);
        }
        
        // store correlation
        // CHANGE00
        instanceCorrelationRepository.saveAndFlush(correlation);    
        
        return correlationKeyValue;
    }

    /**
     * 
     * @param instance
     * @param messageId
     * @param payload 
     */
    private void createConversationCorrelation(
        Instance instance, 
        String messageId, 
        Object payload) {
        
        Event event;
        String retrievalExpression;
        Object correlationKeyValue;
        InstanceCorrelation correlation;

        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        // validate messageId and payload
        if (messageId == null) {
            throw new RuntimeException(
                String.format(
                    "Cannot process instance %d message: missing messageId",
                    instance.getId()));
        }
        if (payload == null || !(payload instanceof Map)) {
            throw new RuntimeException(
                String.format(
                    "Cannot process instance %d message '%s': missing or invalid payload",
                    instance.getId(), messageId));
        }

        // get event
        event = eventRepository.findByEventId(instance.getTenant(), instance.getOrganization(), messageId);
        if (event == null || event.getType() != EventType.message) {
            throw new RuntimeException(
                String.format(
                    "Cannot process instance %d message '%s': message is not defined",
                    instance.getId(), messageId));
        }
        
        // get retrieval expression
        retrievalExpression = event.getRetrievalExpression();
        if (retrievalExpression != null) {
            
            // evaluate conversation correlation key
            try {
                correlationKeyValue = MVELSafeHelper
                    .getEvaluator()
                    .eval(retrievalExpression, payload);
            } catch (RuntimeException e) {
                throw new RuntimeException(
                    String.format(
                        "Cannot evaluate instance %d conversation correlation key " +
                        "for messageId '%s', retrievalExpression '%s', payload '%s': %s",
                        instance.getId(), messageId, retrievalExpression, payload, e.getMessage()));
            }
            if (correlationKeyValue == null) {
                throw new RuntimeException(
                    String.format(
                        "Instance %d conversation correlation key evaluates to null " +
                        "for messageId '%s', retrievalExpression '%s', payload '%s'",
                        instance.getId(), messageId, retrievalExpression, payload));
            }
            
            // create correlation
            correlation = InstanceCorrelation.newConversationCorrelation(
                instance, new CorrelationKey(correlationKeyValue), messageId);
            
            // store correlation
            // CHANGE00
            instanceCorrelationRepository.saveAndFlush(correlation);
        }
        
        if (event.isStart() && (correlationKeyValue = ((Map) payload).get("$start")) != null) {
            // store correlation
            // CHANGE00
            instanceCorrelationRepository.saveAndFlush(InstanceCorrelation.newConversationCorrelation(
                instance, new CorrelationKey(correlationKeyValue), messageId));            
        }
    }
    
    /**
     * 
     * @param nodeInstance
     * @return 
     */
    private InstanceContext getInstanceContext(NodeInstance nodeInstance) {
        
        NodeInstanceContainer container;
        String contextId, contextInstanceId;
        
        container = nodeInstance.getNodeInstanceContainer();
        if (nodeInstance.getNodeInstanceContainer() instanceof CompositeContextNodeInstance) {
            contextId = ((Node) ((CompositeContextNodeInstance) container).getNode()).getUniqueId();
            contextInstanceId = ((CompositeContextNodeInstance) container).getUniqueId();
            return new InstanceContext(contextId, contextInstanceId);
        } else {
            return InstanceContext.MAIN;
        }      
    }
    
    /**
     * 
     * @param variableId
     * @param variableInstanceId
     * @return 
     */
    private InstanceContext getInstanceContext(String variableId, String variableInstanceId) {
        
        String contextId, contextInstanceId;

        contextId = variableId.contains(InstanceContext.SEPARATOR) ? 
            StringUtils.substringBeforeLast(variableId, InstanceContext.SEPARATOR) :
            InstanceContext.MAIN.id(); 
        contextInstanceId = variableInstanceId.contains(InstanceContext.SEPARATOR) ? 
            StringUtils.substringBeforeLast(variableInstanceId, InstanceContext.SEPARATOR) :
            InstanceContext.MAIN.instanceId();

        return new InstanceContext(contextId, contextInstanceId);
    }
    
    /**
     * 
     * @param processInstance 
     */
    private void updateInstanceFact(WorkflowProcessInstance processInstance){

        String[] events = processInstance.getEventTypes();
        KieRuntime kieSession;
        FactHandle factHandle;
        
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        try {
            events = processInstance.getEventTypes();
            for (int i = 0; i < events.length; i++){
                if (events[i].startsWith("RuleFlowStateNode-")){
                    kieSession = processInstance.getKnowledgeRuntime();
                    factHandle = kieSession.getFactHandle(processInstance);
                    if (factHandle != null)
                        kieSession.update(factHandle, processInstance);
                    else
                        kieSession.insert(processInstance);
                    break;
                }
            }
        } catch(RuntimeException e){
            log.error(String.format(
                "Cannot update instance %d fact: %s", 
                processInstance.getId(), e.getMessage()));
        }                    
    }    
    
    /**
     * 
     * @param nodeInstance
     * @return 
     */
    private NodeEventInfo getNodeEventInfo(NodeInstance nodeInstance) {
        
        boolean isCatch = false;
        Long workItemId = null;
        String eventNodeType = null; 
        String eventType = null; 
        String eventId = null;
        Object eventPayload = null;
        
        // WorkItem
        if (nodeInstance instanceof WorkItemNodeInstance && ((WorkItemNodeInstance) nodeInstance).getWorkItem() != null) {
            
            WorkItem _workItem;
            String _signalId;
            
            _workItem = ((WorkItemNodeInstance) nodeInstance).getWorkItem();          
            // Send Task
            // External Send Task
            if (_workItem.getName().equals("Send Task") || 
                _workItem.getName().equals("External Send Task")) {
                workItemId = _workItem.getId();
                if (Boolean.TRUE.equals(_workItem.getParameter("isSendTask"))) {
                    eventNodeType = "throw";
                } else {
                    if (nodeInstance.getNode().getOutgoingConnections().isEmpty()) {
                        eventNodeType = "end";
                    } else {
                        eventNodeType = "throw";
                    }
                }
                _signalId = (String) _workItem.getParameter("Signal");
                if (!Strings.isNullOrEmpty(_signalId)) {
                    eventType = "signal";
                    eventId = _signalId;
                    eventPayload = _workItem.getParameter("Data");
                } else {
                    eventType = "message";
                    eventId = (String) _workItem.getParameter("MessageType");
                    eventPayload = _workItem.getParameter("Message");
                }
            }
            // Receive Task
            else if (_workItem.getName().equals("Receive Task")) {
                isCatch = true;
                workItemId = _workItem.getId();
                eventNodeType = "catch";
                eventType = "message";
                eventId = (String) _workItem.getParameter("MessageType");
                eventPayload = _workItem.getResult("Message");
            } else {
                return null;
            }
        // Timer    
        } else if (nodeInstance.getNode() instanceof TimerNode) {
            
            TimerNode _timerNode;

            isCatch = true;
            eventNodeType = "catch";
            eventType = "timer";            
            _timerNode = (TimerNode) nodeInstance.getNode();
            if (_timerNode.getTimer() != null) {
                eventId = _timerNode.getTimer().toString();
            }
        // State
        } else if (nodeInstance.getNode() instanceof StateNode) {
            
            StateNode _stateNode;

            isCatch = true;
            eventNodeType = "catch";
            eventType = "conditional";            
            _stateNode = (StateNode) nodeInstance.getNode();
            eventId = (String) _stateNode.getMetaData("Condition");
        // CatchLink
        } else if (nodeInstance.getNode() instanceof CatchLinkNode) {
            
            CatchLinkNode _linkNode;

            isCatch = true;
            eventNodeType = "catch";
            eventType = "link";
            _linkNode = (CatchLinkNode) nodeInstance.getNode();
            eventId = (String) _linkNode.getMetaData("LinkName");
        // ThrowLink
        } else if (nodeInstance.getNode() instanceof ThrowLinkNode) {
            
            ThrowLinkNode _linkNode;

            eventNodeType = "throw";
            eventType = "link";
            _linkNode = (ThrowLinkNode) nodeInstance.getNode();
            eventId = (String) _linkNode.getMetaData("linkName");
        // Event
        } else if (nodeInstance.getNode() instanceof EventNode) {
            
            EventNode _eventNode;
            String _messageType;
            String _nodeType;
            String _payloadVariable;

            isCatch = true;
            _eventNode = (EventNode) nodeInstance.getNode();
            eventNodeType = _eventNode instanceof BoundaryEventNode ? "boundary" : "catch";
            _messageType = (String) _eventNode.getMetaData().get("MessageType");
            _nodeType = _eventNode.getType();
            if (_messageType != null) {
                eventType = "message";
                eventId = _messageType;
            } else if (_nodeType != null) {
                if (_nodeType.startsWith("Escalation-")) {
                    eventType = "escalation";
                    eventId = _nodeType.substring(eventType.length() + 1);
                } else if (_nodeType.startsWith("Error-")) {
                    eventType = "error";
                    eventId = _nodeType.substring(eventType.length() + 1);
                    if ("null".equals(eventId)) {
                        eventId = "anyException";
                    }
                } else if (_nodeType.startsWith("Timer-")) {
                    eventType = "timer";
                    eventId = _nodeType.substring(eventType.length() + 1);
                } else if (_nodeType.equals("Compensation")) {
                    eventType = "compensation";
                    eventId = (String) _eventNode.getMetaData("AttachedTo");
                } else if (_nodeType.startsWith("Conditional-")) {
                    eventType = "conditional";
                    eventId = _nodeType.substring(eventType.length() + 1);
                } else {
                    eventType = "signal";
                    eventId = _nodeType;
                }
            } else {
                eventType = "none";
            }
            _payloadVariable = _eventNode.getVariableName();
            if (_payloadVariable != null) {
                eventPayload = nodeInstance.getVariable(_payloadVariable);
            }
        // Start
        } else if (nodeInstance.getNode() instanceof StartNode) {
            
            StartNode _startNode;
            String _messageType;
            List<Trigger> _triggers;
            String _triggerType;
            String _payloadVariable;

            isCatch = true;
            eventNodeType = "start";            
            _startNode = (StartNode) nodeInstance.getNode();
            _messageType = (String) _startNode.getMetaData().get("MessageType");
            _triggerType = null;
            _triggers = _startNode.getTriggers();
            if (_triggers != null) {
                for (Trigger trigger : _triggers) {
                    if (trigger instanceof EventTrigger) {
                        for (EventFilter filter : ((EventTrigger) trigger).getEventFilters()) {
                            if (filter instanceof EventTypeFilter) {
                                _triggerType = ((EventTypeFilter) filter).getType();
                            }
                        }
                    }
                    if (trigger instanceof ConstraintTrigger) {
                        _triggerType = "Conditional";
                        eventId = ((ConstraintTrigger) trigger).getConstraint();
                    }
                }
            }
            if (_messageType != null) {
                eventType = "message";
                eventId = _messageType;
            } else if (_triggerType != null) {
                if (_triggerType.equals("Conditional")) {
                    eventType = "conditional";
                } else if (_triggerType.startsWith("Timer-")) {
                    eventType = "timer";
                    eventId = _triggerType.substring(eventType.length() + 1);
                } else if (_triggerType.startsWith("Error-")) {
                    eventType = "error";
                    eventId = _triggerType.substring(eventType.length() + 1);
                    if ("null".equals(eventId)) {
                        eventId = "anyException";
                    }
                } else if (_triggerType.startsWith("Escalation-")) {
                    eventType = "escalation";
                    eventId = _triggerType.substring(eventType.length() + 1);
                } else if (_triggerType.equals("Compensation")) {
                    eventType = "compensation";
                    eventId = null;
                    if (nodeInstance.getNodeInstanceContainer() instanceof CompositeContextNodeInstance) {
                        eventId = ((CompositeContextNodeInstance) nodeInstance.getNodeInstanceContainer()).getUniqueId();
                    }
                } else {
                    eventType = "signal";
                    eventId = _triggerType;
                }
            } else {
                eventType = "none";
            }
            _payloadVariable = (String) _startNode.getMetaData("TriggerMapping");
            if (_payloadVariable != null) {
                eventPayload = nodeInstance.getVariable(_payloadVariable);
            }
        // Action    
        } else if (nodeInstance.getNode() instanceof ActionNode) {

            ActionNode _actionNode;
            String _eventType;
            String _activityRef;
            String _payloadVariable;
            String _consequence;

            eventNodeType = "throw";
            _actionNode = (ActionNode) nodeInstance.getNode();
            _eventType = (String) _actionNode.getMetaData("EventType");
            _activityRef = (String) _actionNode.getMetaData("compensation-activityRef");
            _payloadVariable = (String) _actionNode.getMetaData("MappingVariable");
            if ("signal".equals(_eventType)) {
                eventType = "signal";
                eventId = (String) _actionNode.getMetaData("Ref");
            } else if (_activityRef != null) {
                eventType = "compensation";
                eventId = _activityRef;
            } else if (_payloadVariable != null) {
                eventType = "escalation";
                if (_actionNode.getAction() instanceof DroolsConsequenceAction) {
                    _consequence = (String) ((DroolsConsequenceAction) _actionNode.getAction()).getConsequence();
                    if (_consequence != null) {
                        int idx1 = _consequence.indexOf("scopeInstance.handleException(");
                        int idx2 = _consequence.indexOf("\"", idx1 + 1);
                        if (idx1 > 0 && idx2 > idx1) {
                            eventId = _consequence.substring(idx1 + 1, idx2 - 1);
                        }
                    }
                }
            } else {
                // not an event at all
                return null;
            }
            if (_payloadVariable != null) {
                eventPayload = nodeInstance.getVariable(_payloadVariable);
            }
        // Fault    
        } else if (nodeInstance.getNode() instanceof FaultNode) {
            
            FaultNode _faultNode;
            String _payloadVariable;

            eventNodeType = "end";
            _faultNode = (FaultNode) nodeInstance.getNode();
            eventType = _faultNode.isTerminateParent() ? "error" : "escalation";
            eventId = _faultNode.getFaultName();
            _payloadVariable = _faultNode.getFaultVariable();
            if (_payloadVariable != null) {
                eventPayload = nodeInstance.getVariable(_payloadVariable);
            }
        // End
        } else if (nodeInstance.getNode() instanceof EndNode) {
            
            EndNode _endNode;
            String _eventType;
            String _activityRef;
            String _payloadVariable;

            eventNodeType = "end";
            _endNode = (EndNode) nodeInstance.getNode();
            _eventType = (String) _endNode.getMetaData("EventType");
            _activityRef = (String) _endNode.getMetaData("compensation-activityRef");
            if ("signal".equals(_eventType)) {
                eventType = "signal";
                eventId = (String) _endNode.getMetaData("Ref");
            } else if (_activityRef != null) {
                eventType = "compensation";
                eventId = _activityRef;
            } else if (_endNode.isTerminate()) {
                eventType = "terminate";
                if (nodeInstance.getNodeInstanceContainer() instanceof CompositeContextNodeInstance) {
                    eventId = ((CompositeContextNodeInstance) nodeInstance.getNodeInstanceContainer()).getUniqueId();
                }
            } else {
                eventType = "none";
            }
            _payloadVariable = (String) _endNode.getMetaData("MappingVariable");
            if (_payloadVariable != null) {
                eventPayload = nodeInstance.getVariable(_payloadVariable);
            }
        } else {
            return null;
        }
        
        if (eventPayload == null) {
            eventPayload = new HashMap<>();
        }
        
        return new NodeEventInfo(
            eventNodeType,
            eventType,
            eventId,
            eventPayload,
            isCatch,
            workItemId);
    }
    
    /**
     * 
     */
    private static class NodeEventInfo {
        
        private String nodeType; 
        private String type; 
        private String id;
        private Object payload;    
        private boolean isCatch;
        private Long workItemId;

        public NodeEventInfo(
            String nodeType,
            String type,
            String id,
            Object payload,
            boolean isCatch,
            Long workItemId) {
            this.nodeType = nodeType;
            this.type = type;
            this.id = id;
            this.payload = payload;
            this.isCatch = isCatch;
            this.workItemId = workItemId;
        }

        public String nodeType() {
            return nodeType;
        }

        public String type() {
            return type;
        }

        public String id() {
            return id;
        }

        public Object payload() {
            return payload;
        }

        public boolean isThrow() {
            return !isCatch;
        }

        public boolean isCatch() {
            return isCatch;
        }

        public boolean isMessageThrow() {
            return !isCatch && type.equals("message");
        }
        
        public boolean isMessageCatch() {
            return isCatch && type.equals("message");
        }
        
        public boolean isCorrelable() {
            return isCatch && type.equals("message") && !nodeType.equals("start");
        }
        
        public Long workItemId() {
            return workItemId;
        }
    }
    
    private Environment getEnvironment(Instance instance) {
        return RuntimeManagerRegistry
            .get()
            .getManager(instance.getUnitId())
            .getRuntimeEngine(ProcessInstanceIdContext.get(instance.getId()))
            .getKieSession()
            .getEnvironment();
    }
    
    private EntityManager getCommandScopedEntityManager(Instance instance) {
        return (EntityManager) getEnvironment(instance)
            .get(EnvironmentName.CMD_SCOPED_ENTITY_MANAGER);
    }    
}
