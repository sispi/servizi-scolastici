/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.endpoint;

import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.api.bpm.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.bpm.dto.ClaimTaskDTO;
import it.filippetti.ks.api.bpm.dto.CompleteTaskDTO;
import it.filippetti.ks.api.bpm.dto.CreateFormViewDTO;
import it.filippetti.ks.api.bpm.dto.CreateTaskCommentDTO;
import it.filippetti.ks.api.bpm.dto.DelegateTaskDTO;
import it.filippetti.ks.api.bpm.dto.ErrorDTO;
import it.filippetti.ks.api.bpm.dto.ForwardTaskDTO;
import it.filippetti.ks.api.bpm.dto.NotificationDTO;
import it.filippetti.ks.api.bpm.dto.RefuseTaskDTO;
import it.filippetti.ks.api.bpm.dto.ReleaseTaskDTO;
import it.filippetti.ks.api.bpm.dto.SaveTaskDTO;
import it.filippetti.ks.api.bpm.dto.SkipTaskDTO;
import it.filippetti.ks.api.bpm.dto.TaskCommentDTO;
import it.filippetti.ks.api.bpm.dto.TaskDTO;
import it.filippetti.ks.api.bpm.dto.VoidDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.service.TaskService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author marco.mazzocchetti
 */
@Api(tags = {"/tasks"}) 
@RestController
@RequestMapping("/tasks")
public class TasksEndpoint {
    
    @Autowired
    private TaskService taskService;
    
    @ApiOperation(
        value = "Get tasks",
        notes = ""
            + "Get a paged list of all user accessible tasks.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details) and these filters:"
            + "<ul><li><b>businessKey</b>: get a unique task by business key, if any"
            + "<li><b>processId</b>: get tasks by processId starting with, case insensitive"
            + "<li><b>subject</b>: get tasks by subject starting with, case insensitive"
            + "<li><b>assignedAs</b>: get tasks assigned to user as specified business role, allowed values are "
            + "<i>ActualOwner, PotentialOwner, TaskStakeholder, BusinessAdministrator</i>"
            + "<li><b>status</b>: get tasks by status&ast;, allowed values are "
            + "<i>Ready, Reserved, InProgress, Completed, Error, Obsolete</i>"
            + "<li><b>expiration</b>: get tasks by expiration relative to current time, allowed format is "
            + "<i>-?[0-9]+[smhd]?</i> where allowed time units are <i>s</i>econds, <i>m</i>inutes, <i>h</i>ours, <i>d</i>ays (default) "
            + "(e.g. <i>0</i> for task expiring today, <i>-3d</i> for tasks expired 3 days ago, <i>+3h</i> for task expiring within 3 hours)"
            + "<li><b>attachments</b>: get tasks by attachment name&ast;, supports special wildcard syntax '*' to get tasks with any attachment"
            + "<li><b>activeOnly</b>: get only active tasks</ul>"
            + "[&ast;] Multiple comma separated values filter is applied with logical OR policy")            
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.Page.class, 
            message = "<b>Tasks list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)            
    @RequestMapping(
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTasks(
        @RequestParam(name = "businessKey", required = false) String businessKey, 
        @RequestParam(name = "processId", required = false) String processId, 
        @RequestParam(name = "subject", required = false) String subject, 
        @RequestParam(name = "assignedAs", required = false) String assignedAs, 
        @RequestParam(name = "status", required = false) String status, 
        @RequestParam(name = "expiration", required = false) String expiration, 
        @RequestParam(name = "attachments", required = false) String attachments, 
        @RequestParam(name = "activeOnly", required = false, defaultValue = "false") Boolean activeOnly, 
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.getTasks(AuthenticationContextHolder.get(), 
                businessKey,
                processId,
                assignedAs,
                status, 
                expiration,
                attachments,
                subject,
                activeOnly, 
                pageNumber, pageSize, orderBy, 
                fetch));
    }    
    
    @ApiOperation(
        value = "Get task",
        notes = ""
            + "Get detail of task.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details).<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.class, 
            message = "<b>Task detail</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)            
    @RequestMapping(
        path = "/{id}",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTask(
        @PathVariable("id") Long id,
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.getTask(AuthenticationContextHolder.get(), id, fetch));
    }   
    
    @ApiOperation(
        value = "Get task form template",
        notes = ""
        + "Get form FTL template of the specified type.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200,
            message = "<b>Form template</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),    
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>FORM_TEMPLATE_UNDEFINED</b>: Form template undefined")         
    })    
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
        path = "/{id}/form/template",
        method = RequestMethod.GET, 
        produces = "text/plain"
    )
    public void getTaskFormTemplate(
        @PathVariable("id") Long id, 
        @RequestParam(value = "type", required = false, defaultValue = "vue") String type,
        HttpServletResponse response)
        throws ApplicationException, IOException{
        
        taskService.getTaskFormTemplate(AuthenticationContextHolder.get(), id, type, response);
    }       

    @ApiOperation(
        value = "Get task form script",
        notes = ""
        + "Get form script.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200,
            message = "<b>Form script</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")
    })    
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
        path = "/{id}/form/script",
        method = RequestMethod.GET, 
        produces = "text/javascript"
    )
    public void getTaskFormScript(
        @PathVariable("id") Long id, 
        HttpServletResponse response)
        throws ApplicationException, IOException{
        
        taskService.getTaskFormScript(AuthenticationContextHolder.get(), id, response);
    }       
    
    @ApiOperation(
        value = "Create task form view",
        notes = ""
        + "Generate a form HTML view for the specified template type.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, 
            message = "<b>Form view successfully created</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>FORM_TEMPLATE_UNDEFINED</b>: Form template undefined"         
            + "<li><b>FORM_VIEW_GENERATION_ERROR</b>: An error has occurred during view generation")  
    })    
    @ResponseStatus(HttpStatus.CREATED)    
    @RequestMapping(
        path = "/{id}/form/views",
        method = RequestMethod.POST, 
        produces = "text/html"
    )
    public void createTaskFormView(
        @PathVariable("id") Long id, 
        @RequestBody CreateFormViewDTO body,
        HttpServletResponse response)
        throws ApplicationException, IOException, TemplateException{
        
        taskService.createTaskFormView(AuthenticationContextHolder.get(), id, body, response);
    }      
    
    @ApiOperation(
        value = "Get task notifications",
        notes = ""
            + "Get a paged list of all task related notifications.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details) and these filters:"
            + "<ul><li><b>activeOnly</b>: get only active notifications, i.e. not expired, not read and with current user as recipient"
            + "<li><b>tags</b>: get notifications filtered by tags&ast;</ul>"
            + "[&ast;] Multiple comma separated tags filter is applied with logical AND policy")        
    @ApiResponses({
        @ApiResponse(code = 200, response = NotificationDTO.Page.class,
            message = "<b>Notifications list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)                            
    @RequestMapping(
        path = "/{id}/notifications",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTaskNotifications(
        @PathVariable("id") Long id,    
        @RequestParam(name = "activeOnly", required = false, defaultValue = "false") Boolean activeOnly,
        @RequestParam(name = "tags", required = false) String tags,
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.getTaskNotifications(AuthenticationContextHolder.get(),
                id,
                activeOnly,
                tags,
                pageNumber, pageSize, orderBy,
                fetch));
    }   

    @ApiOperation(
        value = "Get task comments",
        notes = ""
            + "Get a list of all task comments.<br/>")        
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskCommentDTO.class, responseContainer = "List",
            message = "<b>Comments list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)                            
    @RequestMapping(
        path = "/{id}/comments",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTaskComments(
        @PathVariable("id") Long id)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.getTaskComments(AuthenticationContextHolder.get(),
                id));
    }   
    
    @ApiOperation(
        value = "Create task comment",
        notes = ""
            + "Add a new comment to the task.<br/>")        
    @ApiResponses({
        @ApiResponse(code = 201, response = TaskCommentDTO.class,
            message = "<b>Comment successfully created</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.CREATED)      
    @RequestMapping(
        path = "/{id}/comments",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createTaskComment(
        @PathVariable("id") Long id,
        @RequestBody CreateTaskCommentDTO body)
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(taskService.createTaskComment(AuthenticationContextHolder.get(), id, body));
    } 
    
    @ApiOperation(
        value = "Delete task comment",
        notes = ""
            + "Delete a comment of the task.<br/>")        
    @ApiResponses({
        @ApiResponse(code = 200, response = VoidDTO.class,
            message = "<b>Comment successfully deleted</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),        
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)          
    @RequestMapping(
        path = "/{id}/comments/{commentId}",    
        method = RequestMethod.DELETE, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteTaskComment(
        @PathVariable("id") Long id,  
        @PathVariable("commentId") Long commentId)
        throws ApplicationException {
        
        taskService.deleteTaskComment(AuthenticationContextHolder.get(), id, commentId);
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }  
    
    @ApiOperation(
        value = "Claim task", 
        notes = ""
        + "Claim task.<br/>"
        + "Only specific preconditions dependings on current user and task assignments/status allows the operation to the user,<br/>"
        + "in absence of which a 403 (authorization check) or 422 (runtime check) code is returned.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.class, 
            message = "<b>Task successfully claimed</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
    })
    @ResponseStatus(HttpStatus.OK)             
    @RequestMapping(
        path = "/{id}/actions/claim",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> claimTask(
        @PathVariable("id") Long id,     
        @RequestBody ClaimTaskDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.claimTask(AuthenticationContextHolder.get(), id, body));
    }       

    @ApiOperation(
        value = "Save task", 
        notes = ""
        + "Update task input variables, this operation doesn't change the task status.<br/>"
        + "Only specific preconditions dependings on current user and task assignments/status allows the operation to the user,<br/>"
        + "in absence of which a 403 (authorization check) code is returned.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.class, 
            message = "<b>Task successfully saved</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")
    })
    @ResponseStatus(HttpStatus.OK)                 
    @RequestMapping(
        path = "/{id}/actions/save",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> saveTask(
        @PathVariable("id") Long id,     
        @RequestBody SaveTaskDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.saveTask(AuthenticationContextHolder.get(), id, body));
    }       

    @ApiOperation(
        value = "Complete task", 
        notes = ""
        + "Complete task.<br/>"
        + "Only specific preconditions dependings on current user and task assignments/status allows the operation to the user,<br/>"
        + "in absence of which a 403 (authorization check) or 422 (runtime check) code is returned.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.class, 
            message = "<b>Task successfully completed</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
    })
    @ResponseStatus(HttpStatus.OK)                 
    @RequestMapping(
        path = "/{id}/actions/complete",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> completeTask(
        @PathVariable("id") Long id,
        @RequestBody CompleteTaskDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.completeTask(AuthenticationContextHolder.get(), id, body));
    }         

    @ApiOperation(
        value = "Complete task (form)", 
        notes = ""
        + "Complete task.<br/>"
        + "Only specific preconditions dependings on current user and task assignments/status allows the operation to the user,<br/>"
        + "in absence of which a 403 (authorization check) or 422 (runtime check) code is returned.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.class, 
            message = "<b>Task successfully completed</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
    })
    @ResponseStatus(HttpStatus.OK)                 
    @RequestMapping(
        path = "/{id}/actions/complete/form",
        method = RequestMethod.POST, 
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE           
    )
    @ApiImplicitParams({
        @ApiImplicitParam(name="operationId", paramType = "query", required = false),
        @ApiImplicitParam(name="output", paramType = "body")
    })
    public ResponseEntity<?> completeTaskForm(
        @PathVariable("id") Long id,
        @RequestParam(name = "operationId", required = false) String operationId,             
        @RequestBody(required = false) MultiValueMap<String, String> output) 
        throws ApplicationException{
       
        CompleteTaskDTO dto;
        
        if (output == null) {
            output = new LinkedMultiValueMap<>();
        } else {
            output.remove("operationId");
        }
        dto = new CompleteTaskDTO();
        dto.setOperationId(operationId);
        dto.setFormDataOutput(output);
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.completeTask(AuthenticationContextHolder.get(), id, dto));
    }  
    
    @ApiOperation(
        value = "Forward task", 
        notes = ""
        + "Forward task.<br/>"
        + "Only specific preconditions dependings on current user and task assignments/status allows the operation to the user,<br/>"
        + "in absence of which a 403 (authorization check) or 422 (runtime check) code is returned.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.class, 
            message = "<b>Task successfully forwarded</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>TASK_ACTION_TARGET_NOT_VALID</b>: Invalid action target identity (user or group)")
    })    
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
        path = "/{id}/actions/forward",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> forwardTask(
        @PathVariable("id") Long id,
        @RequestBody ForwardTaskDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.forwardTask(AuthenticationContextHolder.get(), id, body));
    }  

    @ApiOperation(
        value = "Refuse task", 
        notes = ""
        + "Refuse task, the operation is equivalent to forwarding to the refuse group configured for the task.<br/>"
        + "Only specific preconditions dependings on current user and task assignments/status allows the operation to the user,<br/>"
        + "in absence of which a 403 (authorization check) or 422 (runtime check) code is returned.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.class, 
            message = "<b>Task successfully refused</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
    })    
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
        path = "/{id}/actions/refuse",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> refuseTask(
        @PathVariable("id") Long id,
        @RequestBody RefuseTaskDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.refuseTask(AuthenticationContextHolder.get(), id, body));
    }  
    
    @ApiOperation(
        value = "Delegate task", 
        notes = ""
        + "Delegate task.<br/>"
        + "Only specific preconditions dependings on current user and task assignments/status allows the operation to the user,<br/>"
        + "in absence of which a 403 (authorization check) or 422 (runtime check) code is returned.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.class, 
            message = "<b>Task successfully delegated</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>TASK_ACTION_TARGET_NOT_VALID</b>: Invalid action target user")
    })    
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
        path = "/{id}/actions/delegate",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> delegateTask(
        @PathVariable("id") Long id,
        @RequestBody DelegateTaskDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.delegateTask(AuthenticationContextHolder.get(), id, body));
    }      

    @ApiOperation(
        value = "Release task", 
        notes = ""
        + "Release task.<br/>"
        + "Only specific preconditions dependings on current user and task assignments/status allows the operation to the user,<br/>"
        + "in absence of which a 403 (authorization check) or 422 (runtime check) code is returned.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.class, 
            message = "<b>Task successfully released</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
    })    
    @ResponseStatus(HttpStatus.OK)       
    @RequestMapping(
        path = "/{id}/actions/release",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> releaseTask(
        @PathVariable("id") Long id,
        @RequestBody ReleaseTaskDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.releaseTask(AuthenticationContextHolder.get(), id, body));
    }  
    
    @ApiOperation(
        value = "Skip task", 
        notes = ""
        + "Skip task.<br/>"
        + "Only specific preconditions dependings on current user and task assignments/status allows the operation to the user,<br/>"
        + "in absence of which a 403 (authorization check) or 422 (runtime check) code is returned.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.class, 
            message = "<b>Task successfully skipped</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
    })    
    @ResponseStatus(HttpStatus.OK)           
    @RequestMapping(
        path = "/{id}/actions/skip",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> skipTask(
        @PathVariable("id") Long id,
        @RequestBody SkipTaskDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.skipTask(AuthenticationContextHolder.get(), id, body));
    }      
}
