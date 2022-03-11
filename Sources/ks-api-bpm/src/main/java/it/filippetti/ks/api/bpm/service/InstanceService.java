/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import freemarker.template.TemplateException;
import it.filippetti.ks.api.bpm.configuration.ApplicationProperties;
import it.filippetti.ks.api.bpm.dto.AbortInstanceDTO;
import it.filippetti.ks.api.bpm.dto.BookmarkDTO;
import it.filippetti.ks.api.bpm.dto.ChatDTO;
import it.filippetti.ks.api.bpm.dto.ChatMessageDTO;
import it.filippetti.ks.api.bpm.dto.CompleteInstanceNodeDTO;
import it.filippetti.ks.api.bpm.dto.CreateBookmarkDTO;
import it.filippetti.ks.api.bpm.dto.CreateFormViewDTO;
import it.filippetti.ks.api.bpm.dto.InstanceCommandDTO;
import it.filippetti.ks.api.bpm.dto.InstanceDTO;
import it.filippetti.ks.api.bpm.dto.InstanceNodeDTO;
import it.filippetti.ks.api.bpm.dto.InstanceVariableDTO;
import it.filippetti.ks.api.bpm.dto.NotificationDTO;
import it.filippetti.ks.api.bpm.dto.OperationDTO;
import it.filippetti.ks.api.bpm.dto.PageDTO;
import it.filippetti.ks.api.bpm.dto.PublishChatMessageDTO;
import it.filippetti.ks.api.bpm.dto.RetryInstanceDTO;
import it.filippetti.ks.api.bpm.dto.RetryInstanceNodeDTO;
import it.filippetti.ks.api.bpm.dto.StartInstanceDTO;
import it.filippetti.ks.api.bpm.dto.TaskDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.exception.AuthorizationException;
import it.filippetti.ks.api.bpm.exception.BusinessError;
import it.filippetti.ks.api.bpm.exception.BusinessException;
import it.filippetti.ks.api.bpm.exception.NotFoundException;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.mapper.dto.BookmarkMapper;
import it.filippetti.ks.api.bpm.mapper.dto.ChatMapper;
import it.filippetti.ks.api.bpm.mapper.dto.ChatMessageMapper;
import it.filippetti.ks.api.bpm.mapper.dto.InstanceCommandMapper;
import it.filippetti.ks.api.bpm.mapper.dto.InstanceMapper;
import it.filippetti.ks.api.bpm.mapper.dto.InstanceNodeMapper;
import it.filippetti.ks.api.bpm.mapper.dto.InstanceVariableValuesMapper;
import it.filippetti.ks.api.bpm.mapper.dto.InstanceVariablesMapper;
import it.filippetti.ks.api.bpm.mapper.dto.NotificationMapper;
import it.filippetti.ks.api.bpm.mapper.dto.TaskMapper;
import it.filippetti.ks.api.bpm.model.ArchivedInstance;
import it.filippetti.ks.api.bpm.model.AssetType;
import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Bookmark;
import it.filippetti.ks.api.bpm.model.ChatMember;
import it.filippetti.ks.api.bpm.model.ChatMessage;
import it.filippetti.ks.api.bpm.model.Configuration;
import it.filippetti.ks.api.bpm.model.ConfigurationAuthorization;
import it.filippetti.ks.api.bpm.model.Deployment;
import it.filippetti.ks.api.bpm.model.DeploymentUnit;
import it.filippetti.ks.api.bpm.model.Fetcher;
import it.filippetti.ks.api.bpm.model.History;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.InstanceAuthorization;
import it.filippetti.ks.api.bpm.model.InstanceCommand;
import it.filippetti.ks.api.bpm.model.InstanceContext;
import it.filippetti.ks.api.bpm.model.InstanceNode;
import it.filippetti.ks.api.bpm.model.InstanceNodeAuthorization;
import it.filippetti.ks.api.bpm.model.InstanceStatusFilter;
import it.filippetti.ks.api.bpm.model.InstanceVariable;
import it.filippetti.ks.api.bpm.model.Notification;
import it.filippetti.ks.api.bpm.model.Pager;
import it.filippetti.ks.api.bpm.model.TagsFilter;
import it.filippetti.ks.api.bpm.model.Task;
import it.filippetti.ks.api.bpm.repository.ArchivedInstanceRepository;
import it.filippetti.ks.api.bpm.repository.BookmarkRepository;
import it.filippetti.ks.api.bpm.repository.ChatMemberRepository;
import it.filippetti.ks.api.bpm.repository.ChatMessageRepository;
import it.filippetti.ks.api.bpm.repository.ChatRepository;
import it.filippetti.ks.api.bpm.repository.DeploymentRepository;
import it.filippetti.ks.api.bpm.repository.HistoryRepository;
import it.filippetti.ks.api.bpm.repository.InstanceAccessRepository;
import it.filippetti.ks.api.bpm.repository.InstanceCommandRepository;
import it.filippetti.ks.api.bpm.repository.InstanceCorrelationRepository;
import it.filippetti.ks.api.bpm.repository.InstanceNodeRepository;
import it.filippetti.ks.api.bpm.repository.InstanceRepository;
import it.filippetti.ks.api.bpm.repository.InstanceVariableRepository;
import it.filippetti.ks.api.bpm.repository.NotificationRepository;
import it.filippetti.ks.api.bpm.repository.TaskRepository;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.time.DateUtils;
import org.drools.core.process.instance.impl.WorkItemImpl;
import org.jbpm.kie.services.impl.ProcessServiceImpl;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.services.api.DeploymentNotActiveException;
import org.jbpm.services.api.DeploymentNotFoundException;
import org.jbpm.services.api.ProcessInstanceNotFoundException;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * @author marco.mazzocchetti
 */
public class InstanceService extends ProcessServiceImpl 
    implements FormDataSupport {

    private static final Logger log = LoggerFactory.getLogger(InstanceService.class);

    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private ValidationService validationService;

    @Autowired    
    private OperationService operationService;
    
    @Autowired
    private DeploymentService deploymentService;

    @Autowired
    private ExecutorService executorService;
    
    @Autowired
    private ChatService chatService;

    @Autowired
    private FormAssetService formAssetService;
    
    @Autowired
    private DeploymentRepository deploymentRepository;
    
    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private InstanceCorrelationRepository instanceCorrelationRepository;

    @Autowired
    private InstanceAccessRepository instanceAccessRepository;
    
    @Autowired
    private InstanceNodeRepository instanceNodeRepository;

    @Autowired
    private InstanceVariableRepository instanceVariableRepository;

    @Autowired
    private InstanceCommandRepository instanceCommandRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private HistoryRepository historyRepository;
    
    @Autowired
    private ChatMemberRepository chatMemberRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    @Autowired
    private BookmarkRepository bookmarkRepository;
    
    @Autowired
    private ArchivedInstanceRepository archivedInstanceRepository;
    
    @Autowired
    private InstanceMapper instanceMapper;
    
    @Autowired
    private InstanceVariablesMapper variablesMapper;

    @Autowired
    private InstanceVariableValuesMapper variableValuesMapper;
    
    @Autowired
    private InstanceNodeMapper nodeMapper;

    @Autowired
    private InstanceCommandMapper commandMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private ChatMessageMapper chatMessageMapper;
    
    @Autowired
    private NotificationMapper notificationMapper;
    
    @Autowired
    private BookmarkMapper bookmarkMapper;
    
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    private ScheduledFuture archiver;
    
    public InstanceService() {
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startup() {
        if (applicationProperties.isMainNode()) {
            log.info("Starting instance archiver");
            archiver = taskScheduler.scheduleWithFixedDelay(() -> {
                    archiveInstances();
                }, 
                Duration.of(applicationProperties.instanceArchiverDelay(), ChronoUnit.MINUTES));        
        }
    }
    
    @PreDestroy
    public void shutdown() {
        if (applicationProperties.isMainNode()) {
            log.info("Shutting down operation cache cleaner");
            archiver.cancel(false);
        }
    }
    
    /**
     * 
     * @param context
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public InstanceDTO startInstance(
        AuthenticationContext context, StartInstanceDTO dto) 
        throws ApplicationException {

        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            long instanceId;
            Deployment deployment;
            Map<String, Object> input;
        
            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get deployment
                deployment = deploymentRepository.findByProcessId(
                    context.getTenant(),
                    context.getOrganization(), 
                    dto.getProcessId());
                if (deployment == null) {
                    throw new BusinessException(
                        BusinessError.DEPLOYMENT_NOT_FOUND, 
                        "Deployment not found");
                }
                // get input
                if (dto.isFormDataInput()) {
                    input = formDataToMap(dto.getFormDataInput(), new HashMap<>());
                } else {
                    input = dto.getInput();
                }
                // start instance
                instanceId = startInstance(context, deployment, dto.getProfile(), input);
            }
            catch (Throwable t) {
                // rollback
                transactionManager.rollback(tx);
                throw t;
            }
            // commit
            transactionManager.commit(tx);

            // map and return
            return instanceMapper.map(
                // we need to reload instance to get jbpm internal instance changes  
                instanceRepository.reload(Instance.class, instanceId), 
                MappingContext.of(context, Fetcher.of(Instance.class, "nextTasks")));
        },
        InstanceDTO.class);
    }
    
    /**
     * 
     * @param context
     * @param processId
     * @param rootOnly
     * @param activeOnly
     * @param searchableOnly
     * @param unreadChatOnly
     * @param errorOnly
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */    
    public PageDTO<InstanceDTO> getInstances(
        AuthenticationContext context, 
        String processId,
        String status,
        Boolean taskOwner,
        boolean rootOnly, boolean activeOnly, boolean searchableOnly, boolean unreadChatOnly, boolean errorOnly,
        Integer pageNumber, Integer pageSize, String orderBy, 
        String fetch) 
        throws ApplicationException {
        
        return instanceMapper.map(
            instanceRepository.findAll(
                context, 
                processId,
                InstanceStatusFilter.of(status),
                taskOwner,
                rootOnly, activeOnly, searchableOnly, unreadChatOnly, errorOnly,
                Pager.of(Instance.class, pageNumber, pageSize, orderBy)), 
            MappingContext.of(
                context, 
                Fetcher.of(Instance.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param instanceId
     * @param fetch
     * @return
     * @throws ApplicationException 
     */  
    public InstanceDTO getInstance(
        AuthenticationContext context, Long instanceId, String fetch) 
        throws ApplicationException {
        
        Instance instance;
        
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        return instanceMapper.map(
            instance, 
            MappingContext.of(context, Fetcher.of(Instance.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @return
     * @throws ApplicationException 
     */
    public InstanceDTO abortInstance(AuthenticationContext context, Long instanceId, AbortInstanceDTO dto) 
        throws ApplicationException {
        
        validationService.validate(dto);
        
        return operationService.execute(dto.getOperationId(), () -> {

            Instance instance;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get instance
                instance = instanceRepository.findById(context, instanceId);
                if (instance == null) {
                    throw new NotFoundException();
                }
                // check authorization
                if (!instance.hasAuthorization(context, InstanceAuthorization.Abort)) {
                    throw new AuthorizationException();
                }
                // check status
                if (instance.getStatus() != ProcessInstance.STATE_ACTIVE) {
                    throw new BusinessException(BusinessError.INSTANCE_NOT_ACTIVE);
                }
                
                // in some scenario race conditions can happen 
                // due to the jbpm thread unsafe implementation
                // see AbstractDeploymentService.commonDeploy
                synchronized (deploymentService) {
                    try {
                        // abort instance
                        abortProcessInstance(instance.getUnitId(), instanceId);
                    } catch (DeploymentNotFoundException e) {
                        throw new BusinessException(                    
                            BusinessError.DEPLOYMENT_NOT_FOUND, 
                            "Deployment not found");
                    } catch (ProcessInstanceNotFoundException e) {
                        throw new NotFoundException();
                    }
                }
                
                // log history
                historyRepository.save(new History(
                    instance, 
                    InstanceAuthorization.Abort.name(), 
                    context, 
                    dto.getMessage()));
            }
            catch (Throwable t) {
                // rollback
                transactionManager.rollback(tx);
                throw t;
            }
            // commit
            transactionManager.commit(tx);

            // map and return
            return instanceMapper.map(
                instance, 
                MappingContext.of(context));
        }, 
        InstanceDTO.class);
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param dto
     * @return
     * @throws ApplicationException
     * @throws IOException 
     */
    public InstanceDTO retryInstance(
        AuthenticationContext context, Long instanceId,
        RetryInstanceDTO dto) 
        throws ApplicationException, IOException{
        
        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            Instance instance;
            InstanceCommand command;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get instance
                instance = instanceRepository.findById(context, instanceId);
                if (instance == null) {
                    throw new NotFoundException();
                }
                // check authorization
                if (!instance.hasAuthorization(context, InstanceAuthorization.Retry)) {
                    throw new AuthorizationException();
                }
                // check if active
                if (instance.getStatus() != ProcessInstance.STATE_ACTIVE) {
                    throw new BusinessException(BusinessError.INSTANCE_NOT_ACTIVE);
                }
                // get command
                command = instance.getCommand(dto.getCommandId()).orElse(null);
                if (command == null) {
                    throw new BusinessException(
                        BusinessError.INSTANCE_COMMAND_NOT_FOUND);
                }
                // check if not executing
                if (command.isExecuting()) {
                    throw new BusinessException(
                        BusinessError.INSTANCE_COMMAND_EXECUTING, 
                        "Instance is currently executing command");
                }
                // schedule request
                executorService.scheduleRequest(
                    command.getName(), 
                    dto.getContext()!= null ? 
                        command.getIo().getContext().applyData(dto.getContext(), false) : 
                        command.getIo().getContext());
            }
            catch (Throwable t) {
                // rollback
                transactionManager.rollback(tx);
                throw t;
            }
            // commit
            transactionManager.commit(tx);

            // map and return
            return instanceMapper.map(
                // we need to reload node to get jbpm internal node changes     
                instanceRepository.reload(instance), 
                MappingContext.of(context));
        }, 
        InstanceDTO.class);                
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param publicOnly
     * @param fetch
     * @return
     * @throws ApplicationException 
     */   
    public Map<String, InstanceVariableDTO> getInstanceVariables(
        AuthenticationContext context, Long instanceId, 
        boolean publicOnly, 
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
            
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        
        return variablesMapper.map(
            instance.getVariables(publicOnly), 
            MappingContext.of(
                context, 
                Fetcher.of(InstanceVariable.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param instanceId
     * @param publicOnly
     * @return
     * @throws ApplicationException 
     */   
    public Map<String, Object> getInstanceVariableValues(
        AuthenticationContext context, Long instanceId, 
        boolean publicOnly) 
        throws ApplicationException {
        
        Instance instance;
            
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        
        return variableValuesMapper.map(
            instance.getVariables(publicOnly), 
            MappingContext.of(context));
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param name
     * @param value
     * @throws ApplicationException 
     */
    public void updateInstanceVariable(
        AuthenticationContext context, Long instanceId, 
        String name, Object value) 
        throws ApplicationException {
        
        Instance instance;
            
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get instance
            instance = instanceRepository.findById(context, instanceId);
            if (instance == null) {
                throw new NotFoundException();
            }
            // check variable
            if (!instance.hasVariable(InstanceContext.MAIN, name)) {
                throw new NotFoundException();
            }           
            // check authorization
            if (!context.isAdmin()) {
                throw new AuthorizationException();
            }
            // check status
            if (instance.getStatus() != ProcessInstance.STATE_ACTIVE) {
                throw new BusinessException(BusinessError.INSTANCE_NOT_ACTIVE);
            }
            // set variable
            try {
                getProcessInstance(instance).setVariable(name, value);
            } catch (DeploymentNotFoundException | ProcessInstanceNotFoundException e) {
                log.warn(e.getMessage());
            }
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
    }

    /**
     * 
     * @param context
     * @param instanceId
     * @param activeOnly
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */  
    public PageDTO<InstanceNodeDTO> getInstanceNodes(
        AuthenticationContext context, Long instanceId, 
        boolean activeOnly,
        Integer pageNumber, Integer pageSize, String orderBy,
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
            
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        
        return nodeMapper.map(
            instanceNodeRepository.findByInstance(
                instance,
                activeOnly, 
                Pager.of(InstanceNode.class, pageNumber, pageSize, orderBy)), 
            MappingContext.of(
                context, 
                Fetcher.of(InstanceNode.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param instanceId
     * @param nodeInstanceId
     * @param fetch
     * @return
     * @throws ApplicationException 
     */  
    public InstanceNodeDTO getInstanceNode(
        AuthenticationContext context, Long instanceId, Long nodeInstanceId, 
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
        InstanceNode node;
        
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }

        // get node
        node = instanceNodeRepository.findById(instance, nodeInstanceId);
        if (node == null) {
            throw new NotFoundException();
        }
        
        return nodeMapper.map(
            node, 
            MappingContext.of(
                context, 
                Fetcher.of(InstanceNode.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param nodeInstanceId
     * @param publicOnly
     * @param fetch
     * @return
     * @throws ApplicationException 
     */  
    public Map<String, InstanceVariableDTO> getInstanceNodeVariables(
        AuthenticationContext context, Long instanceId, Long nodeInstanceId, 
        boolean publicOnly,
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
        InstanceNode node;
        
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        // get node
        node = instanceNodeRepository.findById(instance, nodeInstanceId);
        if (node == null) {
            throw new NotFoundException();
        }        
        
        return variablesMapper.map(
            node.getVariables(publicOnly), 
            MappingContext.of(
                context, 
                Fetcher.of(InstanceVariable.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param nodeInstanceId
     * @param publicOnly
     * @return
     * @throws ApplicationException 
     */   
    public Map<String, Object> getInstanceNodeVariableValues(
        AuthenticationContext context, Long instanceId, Long nodeInstanceId, 
        boolean publicOnly) 
        throws ApplicationException {
        
        Instance instance;
        InstanceNode node;
        
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        // get node
        node = instanceNodeRepository.findById(instance, nodeInstanceId);
        if (node == null) {
            throw new NotFoundException();
        }        
        
        return variableValuesMapper.map(
            node.getVariables(publicOnly), 
            MappingContext.of(context));
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param nodeInstanceId
     * @param name
     * @param value
     * @throws ApplicationException 
     */
    public void updateInstanceNodeVariable(
        AuthenticationContext context, Long instanceId, Long nodeInstanceId,
        String name, Object value) 
        throws ApplicationException {
        
        Instance instance;
        InstanceNode node;
            
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get instance
            instance = instanceRepository.findById(context, instanceId);
            if (instance == null) {
                throw new NotFoundException();
            }
            // get node
            node = instanceNodeRepository.findById(instance, nodeInstanceId);
            if (node == null) {
                throw new NotFoundException();
            }           
            // check variable
            if (!node.hasVariable(name)) {
                throw new NotFoundException();
            }           
            // check authorization
            if (!context.isAdmin()) {
                throw new AuthorizationException();
            }
            // check status
            if (instance.getStatus() != ProcessInstance.STATE_ACTIVE) {
                throw new BusinessException(BusinessError.INSTANCE_NOT_ACTIVE);
            }
            if (!node.isActive()) {
                throw new BusinessException(BusinessError.INSTANCE_NODE_NOT_ACTIVE);
            }
            // set variable
            try {
                getProcessInstance(instance)
                    // search recursive ?
                    .getNodeInstance(nodeInstanceId, true)
                    .setVariable(name, value);
            } catch (DeploymentNotFoundException | ProcessInstanceNotFoundException e) {
                log.warn(e.getMessage());
            } catch (NullPointerException e) {
                log.warn(String.format(
                    "Node instance with id %d was not found", 
                    nodeInstanceId));
            }
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param nodeInstanceId
     * @param failedOnly
     * @param fetch
     * @return
     * @throws ApplicationException 
     */ 
    public List<InstanceCommandDTO> getInstanceNodeCommands(
        AuthenticationContext context, Long instanceId, Long nodeInstanceId, 
        boolean failedOnly,
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
        InstanceNode node;
        
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        // get node
        node = instanceNodeRepository.findById(instance, nodeInstanceId);
        if (node == null) {
            throw new NotFoundException();
        }        
        
        return commandMapper.map(node.getCommands(failedOnly), 
            MappingContext.of(context, 
                Fetcher.of(InstanceCommand.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param nodeInstanceId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public InstanceNodeDTO abortInstanceNode(
        AuthenticationContext context, Long instanceId, Long nodeInstanceId, 
        OperationDTO dto) 
        throws ApplicationException {
        
        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            Instance instance;
            InstanceNode node;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get instance
                instance = instanceRepository.findById(context, instanceId);
                if (instance == null) {
                    throw new NotFoundException();
                }
                // get node
                node = instanceNodeRepository.findById(instance, nodeInstanceId);
                if (node == null) {
                    throw new NotFoundException();
                }       
                // check authorization
                if (!node.hasAuthorization(context, InstanceNodeAuthorization.Abort)) {
                    throw new AuthorizationException();
                }
                // check if active
                if (instance.getStatus() != ProcessInstance.STATE_ACTIVE) {
                    throw new BusinessException(BusinessError.INSTANCE_NOT_ACTIVE);
                }
                if (!node.isActive()) {
                    throw new BusinessException(
                        BusinessError.INSTANCE_NODE_NOT_ACTIVE, 
                        "Node not active");
                }
                // abort node workItem
                abortWorkItem(
                    instance.getUnitId(), 
                    instance.getId(), 
                    node.getWorkItemId());                
            }
            catch (Throwable t) {
                // rollback
                transactionManager.rollback(tx);
                throw t;
            }
            // commit
            transactionManager.commit(tx);

            // map and return
            return nodeMapper.map(
                // we need to reload node to get jbpm internal node changes     
                instanceNodeRepository.reload(node), 
                MappingContext.of(context));
        }, 
        InstanceNodeDTO.class);                
    }

    /**
     * 
     * @param context
     * @param instanceId
     * @param nodeInstanceId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public InstanceNodeDTO completeInstanceNode(
        AuthenticationContext context, Long instanceId, Long nodeInstanceId, 
        CompleteInstanceNodeDTO dto) 
        throws ApplicationException {
        
        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            Instance instance;
            InstanceNode node;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get instance
                instance = instanceRepository.findById(context, instanceId);
                if (instance == null) {
                    throw new NotFoundException();
                }
                // get node
                node = instanceNodeRepository.findById(instance, nodeInstanceId);
                if (node == null) {
                    throw new NotFoundException();
                }       
                // check authorization
                if (!node.hasAuthorization(context, InstanceNodeAuthorization.Complete)) {
                    throw new AuthorizationException();
                }
                // check if active
                if (instance.getStatus() != ProcessInstance.STATE_ACTIVE) {
                    throw new BusinessException(BusinessError.INSTANCE_NOT_ACTIVE);
                }
                if (!node.isActive()) {
                    throw new BusinessException(
                        BusinessError.INSTANCE_NODE_NOT_ACTIVE, 
                        "Node not active");
                }
                // check if not executing
                if (node.isExecuting()) {
                    throw new BusinessException(
                        BusinessError.INSTANCE_COMMAND_EXECUTING, 
                        "Node is currently executing command");
                }                
                // complete node workItem
                completeWorkItem(
                    instance.getUnitId(), 
                    instance.getId(), 
                    node.getWorkItemId(), 
                    dto.getOutput());                
            }
            catch (Throwable t) {
                // rollback
                transactionManager.rollback(tx);
                throw t;
            }
            // commit
            transactionManager.commit(tx);

            // map and return
            return nodeMapper.map(
                // we need to reload node to get jbpm internal node changes     
                instanceNodeRepository.reload(node), 
                MappingContext.of(context));
        }, 
        InstanceNodeDTO.class);                
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param nodeInstanceId
     * @param dto
     * @return
     * @throws ApplicationException
     * @throws IOException 
     */
    public InstanceNodeDTO retryInstanceNode(
        AuthenticationContext context, Long instanceId, Long nodeInstanceId,
        RetryInstanceNodeDTO dto) 
        throws ApplicationException, IOException{
        
        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            Instance instance;
            InstanceNode node;
            InstanceCommand command;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get instance
                instance = instanceRepository.findById(context, instanceId);
                if (instance == null) {
                    throw new NotFoundException();
                }
                // get node
                node = instanceNodeRepository.findById(instance, nodeInstanceId);
                if (node == null) {
                    throw new NotFoundException();
                }       
                // check authorization
                if (!node.hasAuthorization(context, InstanceNodeAuthorization.Retry)) {
                    throw new AuthorizationException();
                }
                // check if active
                if (instance.getStatus() != ProcessInstance.STATE_ACTIVE) {
                    throw new BusinessException(BusinessError.INSTANCE_NOT_ACTIVE);
                }
                if (!node.isActive()) {
                    throw new BusinessException(
                        BusinessError.INSTANCE_NODE_NOT_ACTIVE, 
                        "Node not active");
                }
                // check if not executing
                if (node.isExecuting()) {
                    throw new BusinessException(
                        BusinessError.INSTANCE_COMMAND_EXECUTING, 
                        "Node is currently executing command");
                }
                // get command
                command = node.getCommand().get();
                // schedule request
                executorService.scheduleRequest(
                    command.getName(), 
                    dto.getInput()!= null ? 
                        command.getIo().getContext().applyWorkItemParameters(dto.getInput(), false) : 
                        command.getIo().getContext());
            }
            catch (Throwable t) {
                // rollback
                transactionManager.rollback(tx);
                throw t;
            }
            // commit
            transactionManager.commit(tx);

            // map and return
            return nodeMapper.map(
                // we need to reload node to get jbpm internal node changes     
                instanceNodeRepository.reload(node), 
                MappingContext.of(context));
        }, 
        InstanceNodeDTO.class);                
    }
    /**
     * 
     * @param context
     * @param instanceId
     * @param failedOnly
     * @param fetch
     * @return
     * @throws ApplicationException 
     */   
    public List<InstanceCommandDTO> getInstanceCommands(
        AuthenticationContext context, Long instanceId, 
        boolean failedOnly,
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
            
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        
        return commandMapper.map(instance.getCommands(failedOnly), 
            MappingContext.of(context, 
                Fetcher.of(InstanceCommand.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param instanceId
     * @param fetch
     * @return
     * @throws ApplicationException 
     */   
    public List<InstanceDTO> getTreeInstances(
        AuthenticationContext context, Long instanceId, 
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
            
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        
        return instanceMapper.map(
            instance.getTreeInstances(false), 
            MappingContext.of(
                context, 
                Fetcher.of(Instance.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param instanceId
     * @param activeOnly
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */    
    public PageDTO<TaskDTO> getInstanceTasks(
        AuthenticationContext context, Long instanceId, 
        boolean activeOnly,
        Integer pageNumber, Integer pageSize, String orderBy,
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
            
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        
        return taskMapper.map(
            taskRepository.findByInstance(
                instance, 
                activeOnly, 
                Pager.of(Task.class, pageNumber, pageSize, orderBy)), 
            MappingContext.of(
                context, 
                Fetcher.of(Task.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param instanceId
     * @param activeOnly
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */   
    public PageDTO<TaskDTO> getTreeTasks(
        AuthenticationContext context, Long instanceId, 
        boolean activeOnly,
        Integer pageNumber, Integer pageSize, String orderBy,
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
            
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        
        if (instance.getRootInstance().isPresent()) {
            return taskMapper.map(
                taskRepository.findByRootInstance(
                    instance.getRootInstance().get(), 
                    activeOnly, 
                    Pager.of(Task.class, pageNumber, pageSize, orderBy)), 
                MappingContext.of(
                    context, 
                    Fetcher.of(Task.class, fetch)));
        } else {
            return taskMapper.map(
                Page.empty(
                    Pager.of(Task.class, pageNumber, pageSize, orderBy)), 
                MappingContext.EMPTY);
        }
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param activeOnly
     * @param tags
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */   
    public PageDTO<NotificationDTO> getInstanceNotifications(
        AuthenticationContext context, Long instanceId, 
        boolean activeOnly,
        String tags,
        Integer pageNumber, Integer pageSize, String orderBy,
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
            
        // get instance
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }
        
        return notificationMapper.map(notificationRepository.findByInstance(context,
                instance, 
                activeOnly, 
                TagsFilter.of(tags),
                Pager.of(Notification.class, pageNumber, pageSize, orderBy)), 
            MappingContext.of(
                context, 
                Fetcher.of(Notification.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public BookmarkDTO createBookmark(
        AuthenticationContext context, Long instanceId,
        CreateBookmarkDTO dto) 
        throws ApplicationException {
        
        Instance instance;
        Bookmark bookmark;
        
        validationService.validate(dto);
        
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get instance
            instance = instanceRepository.findById(context, instanceId);
            if (instance == null) {
                throw new NotFoundException();
            }
            // store bookmark
            bookmark = bookmarkRepository.save(new Bookmark(
                instance, 
                context.getUserId(),
                dto.getDescription()));
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
        
        // map and return
        return bookmarkMapper.map(bookmark);
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param fetch
     * @return
     * @throws ApplicationException 
     */  
    public ChatDTO getChat(
        AuthenticationContext context, Long instanceId, 
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
        
        // get instance/chat
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null || instance.getChatMembership().isEmpty()) {
            throw new NotFoundException();
        }

        return chatMapper.map(
            instance.getChatMembership().get(), 
            MappingContext.of(context, Fetcher.of(ChatMember.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @throws ApplicationException 
     */
    public void markChatAsRead(
        AuthenticationContext context, Long instanceId) 
        throws ApplicationException {
        
        Instance instance;
            
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get instance/chat
            instance = instanceRepository.findById(context, instanceId);
            if (instance == null || instance.getChatMembership().isEmpty()) {
                throw new NotFoundException();
            }
            // check status
            if (instance.getStatus() != ProcessInstance.STATE_ACTIVE) {
                throw new BusinessException(BusinessError.INSTANCE_NOT_ACTIVE);
            }
            // mark as read
            instance.getChatMembership().get().markAsRead();
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */
    public PageDTO<ChatMessageDTO> getChatMessages(
        AuthenticationContext context, Long instanceId, 
        Integer pageNumber, Integer pageSize, String orderBy,
        String fetch) 
        throws ApplicationException {
        
        Instance instance;
            
        // get instance/chat
        instance = instanceRepository.findById(context, instanceId);
        if (instance == null || instance.getChatMembership().isEmpty()) {
            throw new NotFoundException();
        }
        
        return chatMessageMapper.map(
            chatMessageRepository.findByChat(
                instance.getChatMembership().get().getChat(),
                Pager.of(ChatMessage.class, pageNumber, pageSize, orderBy)), 
            MappingContext.of(
                context, 
                Fetcher.of(ChatMessage.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param dto
     * @throws ApplicationException 
     */
    public void publishChatMessage(
        AuthenticationContext context, Long instanceId, 
        PublishChatMessageDTO dto) 
        throws ApplicationException {
        
        Instance instance;
        
        validationService.validate(dto);
        
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get instance/chat
            instance = instanceRepository.findById(context, instanceId);
            if (instance == null || instance.getChatMembership().isEmpty()) {
                throw new NotFoundException();
            }
            // check authorization
            if (!instance.hasAuthorization(context, InstanceAuthorization.Chat)) {
                throw new AuthorizationException();
            }        
            // check status
            if (instance.getStatus() != ProcessInstance.STATE_ACTIVE) {
                throw new BusinessException(BusinessError.INSTANCE_NOT_ACTIVE);
            }
            // publish message
            chatService.publishMessage(
                instance, context.getUserId(), dto.getText());
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param chatMessageId
     * @throws ApplicationException 
     */
    public void deleteChatMessage(
        AuthenticationContext context, Long instanceId, Long chatMessageId) 
        throws ApplicationException {
        
        Instance instance;
        ChatMessage chatMessage;
        
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get instance/chat
            instance = instanceRepository.findById(context, instanceId);
            if (instance == null || instance.getChatMembership().isEmpty()) {
                throw new NotFoundException();
            }
            // get message
            chatMessage = chatMessageRepository.findById(chatMessageId).orElse(null);
            if (chatMessage == null || !chatMessage.getChat().isMember(instance)) {
                throw new NotFoundException();
            }
            // check authorization
            if (!chatMessage.isSender(context)) {
                throw new AuthorizationException();
            }        
            // check status
            if (instance.getStatus() != ProcessInstance.STATE_ACTIVE) {
                throw new BusinessException(BusinessError.INSTANCE_NOT_ACTIVE);
            }
            // delete message
            chatMessageRepository.delete(chatMessage);
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
    }
   
    /**
     * 
     * @param context
     * @param instanceId
     * @param name
     * @param type
     * @param response
     * @throws ApplicationException
     * @throws IOException 
     */  
    public void getInstanceFormTemplate(
        AuthenticationContext context, 
        Long instanceId,
        String name,
        String type,
        HttpServletResponse response)
        throws ApplicationException, IOException {
        
        Instance instance;
        Configuration configuration;
        AssetType assetType;

        // get instance/configuration/form asset type
        instance =  instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }        
        configuration = instance.getConfiguration().orElse(null);
        if (configuration == null) {
            throw new BusinessException(
                BusinessError.CONFIGURATION_NOT_FOUND,
                "Configuration not found");
        }
        assetType = AssetType.instanceForm(name);
        if (assetType == null || configuration.getAsset(assetType) == null) {
            throw new NotFoundException();
        }

        // get template
        formAssetService
            .get(configuration.getAsset(assetType))
            .getTemplate(context, type, response);       
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param name
     * @param response
     * @throws ApplicationException
     * @throws IOException 
     */
    public void getInstanceFormScript(
        AuthenticationContext context, 
        Long instanceId,
        String name,
        HttpServletResponse response)
        throws ApplicationException, IOException {
        
        Instance instance;
        Configuration configuration;
        AssetType assetType;

        // get instance/configuration/form asset type
        instance =  instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }        
        configuration = instance.getConfiguration().orElse(null);
        if (configuration == null) {
            throw new BusinessException(
                BusinessError.CONFIGURATION_NOT_FOUND,
                "Configuration not found");
        }
        assetType = AssetType.instanceForm(name);
        if (assetType == null || configuration.getAsset(assetType) == null) {
            throw new NotFoundException();
        }

        // get template
        formAssetService
            .get(configuration.getAsset(assetType))
            .getScript(context, response);       
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param name
     * @param dto
     * @param response
     * @throws ApplicationException
     * @throws IOException
     * @throws TemplateException 
     */  
    public void createInstanceFormView(
        AuthenticationContext context, 
        Long instanceId, 
        String name,
        CreateFormViewDTO dto,
        HttpServletResponse response)
        throws ApplicationException, IOException, TemplateException {
        
        Instance instance;
        Configuration configuration;
        AssetType assetType;
        Map<String, Object> model, cloneInput;

        // get instance/configuration/form asset type
        instance =  instanceRepository.findById(context, instanceId);
        if (instance == null) {
            throw new NotFoundException();
        }        
        configuration = instance.getConfiguration().orElse(null);
        if (configuration == null) {
            throw new BusinessException(
                BusinessError.CONFIGURATION_NOT_FOUND,
                "Configuration not found");
        }
        assetType = AssetType.instanceForm(name);
        if (assetType == null || configuration.getAsset(assetType) == null) {
            throw new NotFoundException();
        }

        
        // get clone input model
        cloneInput = instance.getIO().getInput().asMap();
        
        // get context model
        switch (assetType) {
            case form_info:
                model = instance
                    .getVariables(InstanceContext.MAIN, true)
                    .stream()
                    .filter(v -> v.isBusiness())
                    .collect(Collectors.toMap(
                        v -> v.getName(),
                        v -> v.getValue().asObject()));                        
                break;
            case form_clone:
                model = configuration
                    .getAsset(AssetType.project)
                    .getContent()
                    .asJsonAssetContent()
                    .getMap("main", "settings", "flowBuffer")
                    .entrySet()
                    .stream()
                    .filter(e -> Boolean.TRUE.equals(((Map) e.getValue()).getOrDefault("isInput", false)))
                    .filter(e -> cloneInput.get(e.getKey()) != null)                        
                    .collect(Collectors.toMap(e -> e.getKey(), e -> cloneInput.get(e.getKey())));                        
                break;
            default:
                throw new IncompatibleClassChangeError();
        }
        // apply model overrides
        model.putAll(dto.getModelOverrides());

        // create view        
        formAssetService
            .get(configuration.getAsset(assetType))
            .createView(
                context,                     
                dto.getType(),
                dto.getOptions(),
                model, 
                response);      
    }
    
    /**
     * 
     * @param instanceId 
     */
    public void cleanInstance(Long instanceId) {
        
        Instance rootInstance;
        Configuration configuration;
        
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            for (Instance instance : instanceRepository
                .findById(instanceId)
                .orElseThrow(() -> {
                    throw new IllegalStateException(String.format(
                        "Instance %d not found", 
                        instanceId));
                 })
                .getTreeInstances(true)) {
                rootInstance = instance.getRootInstance().orElse(null);
                configuration = instance.getConfiguration().orElse(null);
                if (instance.isCompleted() && 
                    (rootInstance == null || rootInstance.isCompleted())) {
                    // set archive timestamp
                    if (configuration != null) {
                        if (configuration.getRetentionPolicy().isEnabled()) {
                            instance.setArchiveTs(DateUtils.addDays(
                                rootInstance != null ? rootInstance.getEndTs() : instance.getEndTs() , 
                                configuration.getRetentionPolicy().days()));
                        }
                    } else {
                        instance.setArchiveTs(new Date());
                    }
                    instanceRepository.save(instance);
                    // clean
                    if (configuration == null || 
                        configuration.getRetentionPolicy().isCleanEnabled()) {
                        instanceCommandRepository.deleteByInstance(instance);
                        instanceCorrelationRepository.deleteByInstance(instance);
                        instanceNodeRepository.deleteByInstance(instance);
                        instanceVariableRepository.deleteNonPublicByInstance(instance);
                    }
                }
            }
        } catch(Throwable t) {
            transactionManager.rollback(tx);
            throw t;
        }
        transactionManager.commit(tx);
    }
    
    /**
     * 
     */
    public void archiveInstances() {
        
        List<Long> archiviables, archived;
        Set<Long> skipped;
        Instance instance;
        
        archived = new ArrayList<>();
        skipped = new HashSet<>();
        while ((archiviables = instanceRepository.findArchiviables(
            applicationProperties.instanceArchiverBatchSize())).size() > skipped.size()) {
            for (Long instanceId : archiviables) {
                if (skipped.contains(instanceId)) {
                    continue;
                }
                // start transaction
                TransactionStatus tx = transactionManager.getTransaction(
                     new DefaultTransactionDefinition());
                try {
                    instance = instanceRepository
                        .findById(instanceId)
                        .orElseThrow(() -> {
                            throw new IllegalStateException(
                                String.format("Instance %d not found", 
                                instanceId));
                         });
                    
                    archivedInstanceRepository.save(new ArchivedInstance(
                        instance, 
                        instanceMapper));
                   
                    instanceCommandRepository.deleteByInstance(instance);
                    instanceCorrelationRepository.deleteByInstance(instance);
                    instanceNodeRepository.deleteByInstance(instance);
                    instanceVariableRepository.deleteByInstance(instance);
                    instanceAccessRepository.deleteByInstance(instance);
                    notificationRepository.deleteByInstance(instance);
                    historyRepository.deleteByInstance(instance);
                    taskRepository.deleteByInstance(instance);
                    chatMemberRepository.deleteByInstance(instance);
                    chatMessageRepository.deleteWithNoMembers();
                    chatRepository.deleteWithNoMembers();                    
                    bookmarkRepository.deleteByInstance(instance);
                    instanceRepository.delete(instance);
                } catch (Throwable t) {
                    // rollback
                    transactionManager.rollback(tx);
                    skipped.add(instanceId);
                    log.error(String.format(
                        "Unexpected error archiving instance %d", 
                        instanceId), t);            
                }
                if (!tx.isCompleted()) {
                    // commit
                    transactionManager.commit(tx);
                    archived.add(instanceId);
                }
            }
        }
        if (archived.size() > 0) {
            log.info(String.format("Archived %d instance(s): %s", archived.size(), archived));      
        }
        if (skipped.size() > 0) {
            log.info(String.format("Skipped archiviation of %d instance(s): %s", skipped.size(), skipped));      
        }
    }
    
    /**
     * 
     * @param workItem
     * @param results 
     */
    public void completeWorkItem(WorkItem workItem, Map<String, Object> results) {
        completeWorkItem(
            ((WorkItemImpl) workItem).getDeploymentId(), 
            workItem.getProcessInstanceId(), 
            workItem.getId(), 
            results);
    }
    
    /**
     * 
     * @param workItem 
     */
    public void abortWorkItem(WorkItem workItem) {
        abortWorkItem(
            ((WorkItemImpl) workItem).getDeploymentId(), 
            workItem.getProcessInstanceId(), 
            workItem.getId());
    }
    
    /**
     * 
     * @param context
     * @param deployment
     * @param profile
     * @param input
     * @return
     * @throws ApplicationException 
     */
    protected long startInstance(AuthenticationContext context, Deployment deployment, String profile, Map<String, Object> input) 
        throws ApplicationException {

        DeploymentUnit unit;
        Configuration configuration;

        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }

        // check deployment active
        if (!deployment.getStatus().isActive()) {
            throw new BusinessException(                    
                BusinessError.DEPLOYMENT_NOT_ACTIVE, 
                "Deployment not active");
        }

        // check deployment sync
        unit = deploymentService.getDeploymentUnit(deployment.getUnitId());
        if (unit == null || !deployment.getStatus().getDeployTs().equals(unit.getDeployTs())) {
            throw new BusinessException(                    
                BusinessError.DEPLOYMENT_OUT_OF_SYNC, 
                "Deployment out of sync and currently scheduled for update, retry later");
        }
        
        // get configuration
        configuration = deployment.getConfiguration(context.getOrganization(), profile);
        if (configuration == null) {
            throw new BusinessException(                    
                BusinessError.CONFIGURATION_NOT_FOUND, 
                "Configuration not found");
        }

        // check authorization
        if (!configuration.hasAuthorization(context, ConfigurationAuthorization.Start)) {
            throw new AuthorizationException();
        }
        
        // check configuration active
        if (!configuration.isActive()) {
            throw new BusinessException(                    
                BusinessError.CONFIGURATION_NOT_ACTIVE, 
                "Configuration not active");
        }

        // check configuration runnable
        if (!configuration.isRunnable()) {
            throw new BusinessException(                    
                BusinessError.CONFIGURATION_NOT_RUNNABLE, 
                "Configuration not runnable");
        }
        
        // apply profile
        input.put(InstanceVariable.Name.PROFILE, profile);
        
        // in some scenario race conditions can happen 
        // due to the jbpm thread unsafe implementation
        // see AbstractDeploymentService.commonDeploy
        synchronized (deploymentService) {
            try {
                // start instance
                return startProcess(deployment.getUnitId(),
                    deployment.getProcessId(),
                    input);
            } catch (DeploymentNotActiveException e) {
                throw new BusinessException(                    
                    BusinessError.DEPLOYMENT_NOT_ACTIVE, 
                    "Deployment not active");
            } catch (DeploymentNotFoundException e) {
                throw new BusinessException(
                    BusinessError.DEPLOYMENT_NOT_FOUND, 
                    "Deployment not found");
            }
        }
    }    

    private RuleFlowProcessInstance getProcessInstance(Instance instance) {
        return (RuleFlowProcessInstance) super.getProcessInstance(
            instance.getUnitId(), instance.getId()); 
    }
}
