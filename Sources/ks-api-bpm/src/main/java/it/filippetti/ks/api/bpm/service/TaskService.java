/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import freemarker.template.TemplateException;
import it.filippetti.ks.api.bpm.dto.ClaimTaskDTO;
import it.filippetti.ks.api.bpm.dto.CompleteTaskDTO;
import it.filippetti.ks.api.bpm.dto.CreateFormViewDTO;
import it.filippetti.ks.api.bpm.dto.CreateTaskCommentDTO;
import it.filippetti.ks.api.bpm.dto.DelegateTaskDTO;
import it.filippetti.ks.api.bpm.dto.ForwardTaskDTO;
import it.filippetti.ks.api.bpm.dto.NotificationDTO;
import it.filippetti.ks.api.bpm.dto.PageDTO;
import it.filippetti.ks.api.bpm.dto.RefuseTaskDTO;
import it.filippetti.ks.api.bpm.dto.ReleaseTaskDTO;
import it.filippetti.ks.api.bpm.dto.SaveTaskDTO;
import it.filippetti.ks.api.bpm.dto.SkipTaskDTO;
import it.filippetti.ks.api.bpm.dto.TaskCommentDTO;
import it.filippetti.ks.api.bpm.dto.TaskDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.exception.AuthorizationException;
import it.filippetti.ks.api.bpm.exception.BusinessError;
import it.filippetti.ks.api.bpm.exception.BusinessException;
import it.filippetti.ks.api.bpm.exception.NotFoundException;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.mapper.dto.NotificationMapper;
import it.filippetti.ks.api.bpm.mapper.dto.TaskCommentMapper;
import it.filippetti.ks.api.bpm.mapper.dto.TaskMapper;
import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Fetcher;
import it.filippetti.ks.api.bpm.model.History;
import it.filippetti.ks.api.bpm.model.Notification;
import it.filippetti.ks.api.bpm.model.Pager;
import it.filippetti.ks.api.bpm.model.TagsFilter;
import it.filippetti.ks.api.bpm.model.Task;
import it.filippetti.ks.api.bpm.model.TaskAssignedAsFilter;
import it.filippetti.ks.api.bpm.model.TaskAttachmentsFilter;
import it.filippetti.ks.api.bpm.model.TaskAuthorization;
import it.filippetti.ks.api.bpm.model.TaskExpirationFilter;
import it.filippetti.ks.api.bpm.model.TaskStatusFilter;
import it.filippetti.ks.api.bpm.repository.HistoryRepository;
import it.filippetti.ks.api.bpm.repository.NotificationRepository;
import it.filippetti.ks.api.bpm.repository.TaskRepository;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.jbpm.kie.services.impl.UserTaskServiceImpl;
import org.jbpm.services.task.exception.PermissionDeniedException;
import org.kie.api.task.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author marco.mazzocchetti
 */
public class TaskService extends UserTaskServiceImpl implements FormDataSupport {

    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private OperationService operationService;
    
    @Autowired
    private ValidationService validationService;

    @Autowired    
    private FormAssetService formAssetService;
    
    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private TaskMapper taskMapper;
    
    @Autowired
    private NotificationMapper notificationMapper;
    
    @Autowired
    private TaskCommentMapper taskCommentMapper;
    
    public TaskService() {
    }
    
    /**
     * 
     * @param context
     * @param activeOnly
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */  
    public PageDTO<TaskDTO> getTasks(
        AuthenticationContext context, 
        String businessKey,    
        String processId,   
        String assignedAs, 
        String status, 
        String expiration,       
        String attachments,
        String subject,
        boolean activeOnly, 
        Integer pageNumber, Integer pageSize, String orderBy, 
        String fetch) 
        throws ApplicationException {
        
        return taskMapper.map(taskRepository.findAll(context, 
                businessKey,
                processId,
                TaskAssignedAsFilter.of(assignedAs),
                TaskStatusFilter.of(status),
                TaskExpirationFilter.of(expiration),
                TaskAttachmentsFilter.of(attachments),
                subject,
                activeOnly, 
                Pager.of(Task.class, pageNumber, pageSize, orderBy)), 
            MappingContext.of(
                context, 
                Fetcher.of(Task.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param taskId
     * @param fetch
     * @return
     * @throws ApplicationException 
     */   
    public TaskDTO getTask(
        AuthenticationContext context, Long taskId, String fetch) 
        throws ApplicationException {
        
        Task task;
        
        task = taskRepository.findById(context, taskId);
        if (task == null) {
            throw new NotFoundException();
        }

        return taskMapper.map(
            task, 
            MappingContext.of(context, Fetcher.of(Task.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param taskId
     * @param type
     * @param response
     * @throws ApplicationException
     * @throws IOException 
     */  
    public void getTaskFormTemplate(
        AuthenticationContext context, 
        Long taskId,
        String type,
        HttpServletResponse response)
        throws ApplicationException, IOException {
        
        Task task;
        
        // get task
        task =  taskRepository.findById(context, taskId);
        if (task == null || !task.hasFormAsset()) {
            throw new NotFoundException();
        }

        // get template
        formAssetService
            .get(task.getFormAsset())
            .getTemplate(context, type, response);       
    }

    /**
     * 
     * @param context
     * @param taskId
     * @param dto
     * @param response
     * @throws ApplicationException
     * @throws IOException
     * @throws TemplateException 
     */
    public void createTaskFormView(
        AuthenticationContext context, 
        Long taskId, 
        CreateFormViewDTO dto,
        HttpServletResponse response)
        throws ApplicationException, IOException, TemplateException {
        
        Task task;
        Map<String, Object> model;
        
        // get task
        task =  taskRepository.findById(context, taskId);
        if (task == null || !task.hasFormAsset()) {
            throw new NotFoundException();
        }
        
        // get context model        
        model = task.getIO().getInput().asMap();
        // apply model overrides
        model.putAll(dto.getModelOverrides());

        // create view        
        formAssetService
            .get(task.getFormAsset())
            .createView(
                context, 
                dto.getType(),
                dto.getOptions(),
                model, 
                response);      
    }
    
    /**
     * 
     * @param context
     * @param taskId
     * @param response
     * @throws ApplicationException
     * @throws IOException 
     */
    public void getTaskFormScript(
        AuthenticationContext context, 
        Long taskId,
        HttpServletResponse response)
        throws ApplicationException, IOException {
        
        Task task;
        
        // get task
        task =  taskRepository.findById(context, taskId);
        if (task == null || !task.hasFormAsset()) {
            throw new NotFoundException();
        }

        // get script
        formAssetService
            .get(task.getFormAsset())
            .getScript(context, response);       
    }
    
    /**
     * 
     * @param context
     * @param taskId
     * @param activeOnly
     * @param tags
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */   
    public PageDTO<NotificationDTO> getTaskNotifications(
        AuthenticationContext context, Long taskId, 
        boolean activeOnly,
        String tags,
        Integer pageNumber, Integer pageSize, String orderBy,
        String fetch) 
        throws ApplicationException {
        
        Task task;
        
        // get task
        task =  taskRepository.findById(context, taskId);
        if (task == null) {
            throw new NotFoundException();
        }
        
        return notificationMapper.map(notificationRepository.findByTask(context,
                task, 
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
     * @param taskId
     * @return
     * @throws ApplicationException 
     */   
    public List<TaskCommentDTO> getTaskComments(
        AuthenticationContext context, Long taskId) 
        throws ApplicationException {
        
        Task task;
        
        // get task
        task =  taskRepository.findById(context, taskId);
        if (task == null) {
            throw new NotFoundException();
        }
        
        return taskCommentMapper.map(task.getTaskData().getComments());
    }
    
    /**
     * 
     * @param context
     * @param taskId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public TaskCommentDTO createTaskComment(
        AuthenticationContext context, Long taskId,
        CreateTaskCommentDTO dto) 
        throws ApplicationException {
        
        Task task;
        Long commentId;
        TaskCommentDTO comment;
        DefaultTransactionDefinition txDef;
        
        validationService.validate(dto);
        
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get task
            task = taskRepository.findById(context, taskId);
            if (task == null) {
                throw new NotFoundException();
            }
            // add comment
            commentId = addComment(
                task.getInstance().getUnitId(), 
                taskId, 
                dto.getText(), 
                context.getUserId(), 
                new Date());
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);

        // start read transaction (needed to retrieve lob in postgres)
        txDef = new DefaultTransactionDefinition();
        txDef.setReadOnly(true);
        tx = transactionManager.getTransaction(txDef);
        try {
            // get and map
            comment = taskCommentMapper.map(task
                .getTaskData()
                .getComments()
                .stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(IllegalStateException::new));
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
        
        // return
        return comment;
    }
    
    /**
     * 
     * @param context
     * @param taskId
     * @param commentId
     * @throws ApplicationException 
     */
    public void deleteTaskComment(
        AuthenticationContext context, Long taskId, final Long commentId) 
        throws ApplicationException {
        
        Task task;
        Comment comment;
        
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get task
            task = taskRepository.findById(context, taskId);
            if (task == null) {
                throw new NotFoundException();
            }
            // get comment
            comment = task
                .getTaskData()
                .getComments()
                .stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElse(null);
            if (comment == null) {
                throw new NotFoundException();
            }
            // check authorization
            if (!comment.getAddedBy().getId().equals(context.getUserId())) {
                throw new AuthorizationException();
            }
            // delete comment
            deleteComment(taskId, commentId);
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
     * @param taskId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public TaskDTO claimTask(AuthenticationContext context, Long taskId, ClaimTaskDTO dto) 
        throws ApplicationException {
        
        validationService.validate(dto);
        
        return operationService.execute(dto.getOperationId(), () -> {
            
            Task task;
            
            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get task
                task = taskRepository.findById(context, taskId);
                if (task == null) {
                    throw new NotFoundException();
                }
                // check authorization
                if (!task.hasAuthorization(context, TaskAuthorization.Claim)) {
                    throw new AuthorizationException();
                }
                // claim and start task
                try {
                    claim(taskId, context.getUserId());
                    start(taskId, context.getUserId());
                } catch (PermissionDeniedException e) {
                    throw new AuthorizationException();
                }
                // log history
                historyRepository.save(new History(
                    task, 
                    TaskAuthorization.Claim.name(), 
                    context, 
                    null,
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
            return taskMapper.map(
                // we need to reload task to get jbpm internal task changes    
                taskRepository.reload(task),
                MappingContext.of(context));
        }, 
        TaskDTO.class);
    }

    /**
     * 
     * @param context
     * @param taskId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public TaskDTO saveTask(
        AuthenticationContext context, Long taskId, 
        SaveTaskDTO dto)
        throws ApplicationException {

        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            Task task;
            Map<String, Object> input;
            
            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get task
                task = taskRepository.findById(context, taskId);
                if (task == null) {
                    throw new NotFoundException();
                }
                // check authorization
                if (!task.hasAuthorization(context, TaskAuthorization.Save)) {
                    throw new AuthorizationException();
                }
                // get input
                if (dto.getMergeInput()) {
                    input = task.getIO().getInput().asMap();
                    input.putAll(dto.getInput());
                } else {
                    input = dto.getInput();
                }                
                // set input
                task.setInput(input);
                // store task
                taskRepository.save(task);
                // log history
                historyRepository.save(new History(
                    task, 
                    TaskAuthorization.Save.name(), 
                    context, 
                    null,
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
            return taskMapper.map(
                task, 
                MappingContext.of(context));
        }, 
        TaskDTO.class);        
    }
    
    /**
     * 
     * @param context
     * @param taskId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public TaskDTO completeTask(
        AuthenticationContext context, Long taskId, 
        CompleteTaskDTO dto)
        throws ApplicationException {

        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            Task task;
            Map<String, Object> input, output;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get task
                task = taskRepository.findById(context, taskId);
                if (task == null) {
                    throw new NotFoundException();
                }
                // check authorization
                if (!(dto.getAutoProgress() ?
                    task.hasAnyAuthorization(context, TaskAuthorization.Claim, TaskAuthorization.Complete) :
                    task.hasAuthorization(context, TaskAuthorization.Complete))) {
                    throw new AuthorizationException();
                }
                // get input
                if (dto.getInput() != null) {
                    if (dto.getMergeInput()) {
                        input = task.getIO().getInput().asMap();
                        input.putAll(dto.getInput());
                    } else {
                        input = dto.getInput();
                    }                
                    // set input
                    task.setInput(input);
                    // store task
                    taskRepository.save(task);
                } else {
                    if (dto.getMergeInput()) {
                        input = task.getIO().getInput().asMap();
                    } else {
                        input = new HashMap<>();
                    }                
                }              
                // get output
                if (dto.isFormDataOutput()) {
                    output = formDataToMap(dto.getFormDataOutput(), input);
                } else {
                    output = input;
                    output.putAll(dto.getOutput());
                }
                // set built in output (result key is for backward compatibility)
                output.put("ActorId", context.getUserId());
                output.put("userToken", context.getJwtToken());
                if (!output.containsKey("Result")) {
                    output.put("Result", new HashMap<>(output));
                }
                // complete task
                try {
                    if (dto.getAutoProgress()) {
                        completeAutoProgress(taskId, context.getUserId(), output);
                    } else {
                        complete(taskId, context.getUserId(), output);
                    }
                } catch (PermissionDeniedException e) {
                    throw new AuthorizationException();
                }                
                // log history
                historyRepository.save(new History(
                    task, 
                    TaskAuthorization.Complete.name(), 
                    context, 
                    null,
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
            return taskMapper.map(
                // we need to reload task to get jbpm internal task changes     
                taskRepository.reload(task), 
                MappingContext.of(context, Fetcher.of(Task.class, "nextTasks")));
        }, 
        TaskDTO.class);        
    }
    
    /**
     * 
     * @param context
     * @param taskId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public TaskDTO forwardTask(
        AuthenticationContext context, Long taskId, 
        ForwardTaskDTO dto)
        throws ApplicationException {

        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            Task task;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get task
                task = taskRepository.findById(context, taskId);
                if (task == null) {
                    throw new NotFoundException();
                } 
                // check authorization
                if (!task.hasAuthorization(context, TaskAuthorization.Forward)) {
                    throw new AuthorizationException();
                }
                // check target
                if (identityService.getActor(context.getTenant(), dto.getTargetIdentity()).isEmpty()) {
                    throw new BusinessException(
                        BusinessError.TASK_ACTION_TARGET_NOT_VALID, 
                        String.format("Invalid action target identity '%s'", dto.getTargetIdentity()));
                }
                // forward task
                try {
                    forward(taskId, context.getUserId(), dto.getTargetIdentity());
                } catch (PermissionDeniedException e) {
                    throw new AuthorizationException();
                }                                
                // log history
                historyRepository.save(new History(
                    task, 
                    TaskAuthorization.Forward.name(), 
                    context, 
                    dto.getTargetIdentity(),
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
            return taskMapper.map(
                // we need to reload task to get jbpm internal task changes     
                taskRepository.reload(task), 
                MappingContext.of(context, Fetcher.of(Task.class, "nextTasks")));
        }, 
        TaskDTO.class);        
    }    

    /**
     * 
     * @param context
     * @param taskId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public TaskDTO refuseTask(
        AuthenticationContext context, Long taskId, 
        RefuseTaskDTO dto)
        throws ApplicationException {

        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            Task task;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get task
                task = taskRepository.findById(context, taskId);
                if (task == null) {
                    throw new NotFoundException();
                } 
                // check authorization
                if (!task.hasAuthorization(context, TaskAuthorization.Refuse)) {
                    throw new AuthorizationException();
                }
                // forward task
                try {
                    forward(taskId, context.getUserId(), task.getAssignments().getRefuseGroup());
                } catch (PermissionDeniedException e) {
                    throw new AuthorizationException();
                }                                
                // log history
                historyRepository.save(new History(
                    task, 
                    TaskAuthorization.Refuse.name(), 
                    context, 
                    null,
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
            return taskMapper.map(
                // we need to reload task to get jbpm internal task changes     
                taskRepository.reload(task), 
                MappingContext.of(context, Fetcher.of(Task.class, "nextTasks")));
        }, 
        TaskDTO.class);        
    }    
    
    /**
     * 
     * @param context
     * @param taskId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public TaskDTO delegateTask(
        AuthenticationContext context, Long taskId, 
        DelegateTaskDTO dto)
        throws ApplicationException {

        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            Task task;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get task
                task = taskRepository.findById(context, taskId);
                if (task == null) {
                    throw new NotFoundException();
                } 
                // check authorization
                if (!task.hasAuthorization(context, TaskAuthorization.Delegate)) {
                    throw new AuthorizationException();
                }
                // check target
                if (identityService.getActor(context.getTenant(), dto.getTargetUserId()).isEmpty()) {
                    throw new BusinessException(
                        BusinessError.TASK_ACTION_TARGET_NOT_VALID, 
                        String.format("Invalid action target user id '%s'", dto.getTargetUserId()));
                }
                // delegate and (re)start task
                try {
                    delegate(taskId, context.getUserId(), dto.getTargetUserId());
                    start(taskId, dto.getTargetUserId());
                } catch (PermissionDeniedException e) {
                    throw new AuthorizationException();
                }                
                // log history
                historyRepository.save(new History(
                    task, 
                    TaskAuthorization.Delegate.name(), 
                    context, 
                    dto.getTargetUserId(),
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
            return taskMapper.map(
                // we need to reload task to get jbpm internal task changes     
                taskRepository.reload(task), 
                MappingContext.of(context, Fetcher.of(Task.class, "nextTasks")));
        }, 
        TaskDTO.class);        
    }    
    
    /**
     * 
     * @param context
     * @param taskId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public TaskDTO releaseTask(
        AuthenticationContext context, Long taskId, 
        ReleaseTaskDTO dto)
        throws ApplicationException {

        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            Task task;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get task
                task = taskRepository.findById(context, taskId);
                if (task == null) {
                    throw new NotFoundException();
                } 
                // check authorization
                if (!task.hasAuthorization(context, TaskAuthorization.Release)) {
                    throw new AuthorizationException();
                }
                // release task
                try {
                    release(taskId, context.getUserId());
                } catch (PermissionDeniedException e) {
                    throw new AuthorizationException();
                }                                            
                // log history
                historyRepository.save(new History(
                    task, 
                    TaskAuthorization.Release.name(), 
                    context, 
                    null,
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
            return taskMapper.map(
                // we need to reload task to get jbpm internal task changes     
                taskRepository.reload(task), 
                MappingContext.of(context, Fetcher.of(Task.class, "nextTasks")));
        }, 
        TaskDTO.class);        
    }        

    /**
     * 
     * @param context
     * @param taskId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public TaskDTO skipTask(
        AuthenticationContext context, Long taskId, 
        SkipTaskDTO dto)
        throws ApplicationException {

        validationService.validate(dto);

        return operationService.execute(dto.getOperationId(), () -> {

            Task task;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                // get task
                task = taskRepository.findById(context, taskId);
                if (task == null) {
                    throw new NotFoundException();
                } 
                // check authorization
                if (!task.hasAuthorization(context, TaskAuthorization.Skip)) {
                    throw new AuthorizationException();
                }
                // skip task
                try {
                    skip(taskId, context.getUserId());
                } catch (PermissionDeniedException e) {
                    throw new AuthorizationException();
                }                                            
                // log history
                historyRepository.save(new History(
                    task, 
                    TaskAuthorization.Skip.name(), 
                    context, 
                    null,
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
            return taskMapper.map(
                // we need to reload task to get jbpm internal task changes     
                taskRepository.reload(task), 
                MappingContext.of(context, Fetcher.of(Task.class, "nextTasks")));
        }, 
        TaskDTO.class);        
    }        
}
