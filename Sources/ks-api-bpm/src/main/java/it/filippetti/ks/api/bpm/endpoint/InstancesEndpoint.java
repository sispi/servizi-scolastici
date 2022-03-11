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
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.api.bpm.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.bpm.dto.AbortInstanceDTO;
import it.filippetti.ks.api.bpm.dto.BookmarkDTO;
import it.filippetti.ks.api.bpm.dto.ChatDTO;
import it.filippetti.ks.api.bpm.dto.ChatMessageDTO;
import it.filippetti.ks.api.bpm.dto.CompleteInstanceNodeDTO;
import it.filippetti.ks.api.bpm.dto.CreateBookmarkDTO;
import it.filippetti.ks.api.bpm.dto.CreateFormViewDTO;
import it.filippetti.ks.api.bpm.dto.ErrorDTO;
import it.filippetti.ks.api.bpm.dto.InstanceCommandDTO;
import it.filippetti.ks.api.bpm.dto.InstanceDTO;
import it.filippetti.ks.api.bpm.dto.InstanceNodeDTO;
import it.filippetti.ks.api.bpm.dto.InstanceVariableDTO;
import it.filippetti.ks.api.bpm.dto.NotificationDTO;
import it.filippetti.ks.api.bpm.dto.OperationDTO;
import it.filippetti.ks.api.bpm.dto.PublishChatMessageDTO;
import it.filippetti.ks.api.bpm.dto.RetryInstanceDTO;
import it.filippetti.ks.api.bpm.dto.RetryInstanceNodeDTO;
import it.filippetti.ks.api.bpm.dto.StartInstanceDTO;
import it.filippetti.ks.api.bpm.dto.TaskDTO;
import it.filippetti.ks.api.bpm.dto.VoidDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.service.FormDataSupport;
import it.filippetti.ks.api.bpm.service.InstanceService;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@Api(tags = {"/instances"}) 
@RestController
@RequestMapping("/instances")
public class InstancesEndpoint implements FormDataSupport {
    
    @Autowired
    private InstanceService instanceService;
    
    @ApiOperation(
        value = "Start instance", 
        notes = ""
        + "Start a new process instance.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = InstanceDTO.class, 
            message = "<b>Instance successfully created</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>DEPLOYMENT_NOT_FOUND</b>: Deployment not found"
            + "<li><b>DEPLOYMENT_NOT_ACTIVE</b>: Deployment not active"
            + "<li><b>DEPLOYMENT_OUT_OF_SYNC</b>: Deployment out of sync and currently scheduled for update, retry later"
            + "<li><b>CONFIGURATION_NOT_FOUND</b>: Configuration not found"
            + "<li><b>CONFIGURATION_NOT_ACTIVE</b>: Configuration not active"
            + "<li><b>CONFIGURATION_NOT_RUNNABLE</b>: Configuration not runnable"
            + "<li><b>INSTANCE_WORKFLOW_ERROR</b>: Instance workflow execution failed")       
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(
        method = RequestMethod.POST, 
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE            
    )
    public ResponseEntity<?> startInstance(
        @RequestBody StartInstanceDTO body) 
        throws ApplicationException{

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(instanceService.startInstance(AuthenticationContextHolder.get(), body));
    }
    
    @ApiOperation(
        value = "Start instance (form)", 
        notes = ""
        + "Start a new process instance.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = InstanceDTO.class, 
            message = "<b>Instance successfully created</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>DEPLOYMENT_NOT_FOUND</b>: Deployment not found"
            + "<li><b>DEPLOYMENT_NOT_ACTIVE</b>: Deployment not active"
            + "<li><b>DEPLOYMENT_OUT_OF_SYNC</b>: Deployment out of sync and currently scheduled for update, retry later"
            + "<li><b>CONFIGURATION_NOT_FOUND</b>: Configuration not found"
            + "<li><b>CONFIGURATION_NOT_ACTIVE</b>: Configuration not active"
            + "<li><b>CONFIGURATION_NOT_RUNNABLE</b>: Configuration not runnable"
            + "<li><b>INSTANCE_WORKFLOW_ERROR</b>: Instance workflow execution failed")       
    })
    @ResponseStatus(HttpStatus.CREATED)    
    @RequestMapping(
        path="/form",
        method = RequestMethod.POST, 
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE           
    )
    @ApiImplicitParams({
        @ApiImplicitParam(name="operationId", paramType = "query", required = false),
        @ApiImplicitParam(name="processId", paramType = "query", required = true),
        @ApiImplicitParam(name="input", paramType = "body")
    })
    public ResponseEntity<?> startInstanceForm(
        @RequestParam(name = "operationId", required = false) String operationId,             
        @RequestParam(name = "processId", required = true) String processId, 
        @RequestBody(required = false) MultiValueMap<String, String> input) 
        throws ApplicationException{
       
        StartInstanceDTO dto;
        
        input.remove("operationId");
        input.remove("processId");
        
        dto = new StartInstanceDTO();
        dto.setOperationId(operationId);
        dto.setProcessId(processId);
        if (input != null) {
            dto.setFormDataInput(input);
        }
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(instanceService.startInstance(AuthenticationContextHolder.get(), dto));
    }  

    @ApiOperation(
        value = "Get instances",
        notes = ""
            + "Get a paged list of all user accessible instances.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details) and these filters:"
            + "<ul><li><b>processId</b>: get instances by processId starting with, case insensitive"
            + "<li><b>status</b>: get instances by status&ast;, allowed values are "
            + "<i>1 (Active), 2 (Completed), 3 (Aborted)</i>"
            + "<li><b>taskOwner</b>: get only instances with or without Ready/InProgress task(s) assigned to user as potential/actual owner"
            + "<li><b>rootOnly</b>: get only root instances"
            + "<li><b>activeOnly</b>: get only active instances"
            + "<li><b>searchableOnly</b>: get only searchable instances"    
            + "<li><b>unreadChatOnly</b>: get only instances with unread chat messages"
            + "<li><b>errorOnly</b>: get only instances stalled due to an error</ul>"
            + "[&ast;] Multiple comma separated values filter is applied with logical OR policy")            
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceDTO.Page.class, 
            message = "<b>Instances list</b>"),
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
    public ResponseEntity<?> getInstances(
        @RequestParam(name = "processId", required = false) String processId, 
        @RequestParam(name = "status", required = false) String status,             
        @RequestParam(name = "taskOwner", required = false) Boolean taskOwner,             
        @RequestParam(name = "rootOnly", required = false, defaultValue = "false") Boolean rootOnly, 
        @RequestParam(name = "activeOnly", required = false, defaultValue = "false") Boolean activeOnly, 
        @RequestParam(name = "searchableOnly", required = false, defaultValue = "true") Boolean searchableOnly, 
        @RequestParam(name = "unreadChatOnly", required = false, defaultValue = "false") Boolean unreadChatOnly, 
        @RequestParam(name = "errorOnly", required = false, defaultValue = "false") Boolean errorOnly, 
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getInstances(AuthenticationContextHolder.get(), 
                processId,
                status,
                taskOwner,
                rootOnly, activeOnly, searchableOnly, unreadChatOnly, errorOnly,
                pageNumber, pageSize, orderBy, 
                fetch));
    }    
    
    @ApiOperation(
        value = "Get instance",
        notes = ""
            + "Get detail of instance.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details).<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceDTO.class, 
            message = "<b>Instance detail</b>"),
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
    public ResponseEntity<?> getInstance(
        @PathVariable("id") Long id,
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getInstance(AuthenticationContextHolder.get(), id, fetch));
    }   
    
    @ApiOperation(
        value = "Get instance variables",
        notes = ""
            + "Get a map of instance variables with metadata and value.<br/>"
            + "Returned variables are only those in main context (i.e. global scope).<br/><br/>"                
            + "Operation supports paging, dynamic properties fetch&ast; and sort (see model for details) and these filters:"
            + "<ul><li><b>publicOnly</b>: get only public variables (i.e. marked as business, input, output or configuration)</ul>"
            + "[&ast;] With special fetch syntax <i>@<variable-name></i> it's possible to get only listed variables")    
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceVariableDTO.Map.class, 
            message = "<b>Instance variables map</b>"),          
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)      
    @RequestMapping(
        path = "/{id}/variables",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstanceVariables(
        @PathVariable("id") Long id,    
        @RequestParam(name = "publicOnly", required = false, defaultValue = "false") Boolean publicOnly,
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getInstanceVariables(AuthenticationContextHolder.get(),
                id,
                publicOnly, 
                fetch));
    }    

    @ApiOperation(
        value = "Get instance variable values",
        notes = ""
            + "Get a map of instance variables values only.<br/>"
            + "Returned variables are only those in main context (i.e. global scope).<br/><br/>"
            + "Operation supports paging, dynamic properties fetch&ast; and sort (see model for details) and these filters:"
            + "<ul><li><b>publicOnly</b>: get only public variables (i.e. marked as business, in, out or config)</ul>"
            + "[&ast;] With special fetch syntax <i>@<variable-name></i> it's possible to get only listed variables")    
    @ApiResponses({
        @ApiResponse(code = 200, response = Map.class, 
            message = "<b>Instance variables map</b>"),          
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)          
    @RequestMapping(
        path = "/{id}/variable-values",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstanceVariableValues(
        @PathVariable("id") Long id,    
        @RequestParam(name = "publicOnly", required = false, defaultValue = "false") Boolean publicOnly)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getInstanceVariableValues(AuthenticationContextHolder.get(),
                id,
                publicOnly));
    }    
    
    @ApiOperation(
        value = "Update instance variable", 
        notes = ""
        + "Update instance variable value.<br/>"
        + "Only active instances allow variable updates.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = VoidDTO.class, 
            message = "<b>Variable successfully updated</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>INSTANCE_NOT_ACTIVE</b>: Instance not active")
    })
    @ResponseStatus(HttpStatus.OK)           
    @RequestMapping(
        path = "/{id}/variables/{name}/value",    
        method = RequestMethod.PUT, 
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateInstanceVariable(
        @PathVariable("id") Long id,
        @PathVariable("name") String name,    
        @RequestBody Object value)
        throws ApplicationException{

        instanceService.updateInstanceVariable(AuthenticationContextHolder.get(),
            id,
            name, 
            value);

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }    
    
    @ApiOperation(
        value = "Get instance nodes",
        notes = ""
            + "Get a paged list of all instance nodes.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details) and these filters:"
            + "<ul><li><b>activeOnly</b>: get only active nodes</ul>")    
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceNodeDTO.Page.class, 
            message = "<b>Instance nodes list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)            
    @RequestMapping(
        path = "/{id}/nodes",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstanceNodes(
        @PathVariable("id") Long id,    
        @RequestParam(name = "activeOnly", required = false, defaultValue = "false") Boolean activeOnly,
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getInstanceNodes(AuthenticationContextHolder.get(),
                id,
                activeOnly,
                pageNumber, pageSize, orderBy,
                fetch));
    }        


    @ApiOperation(
        value = "Get instance node",
        notes = ""
            + "Get detail of instance node.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details).<br/>")    
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceNodeDTO.class, 
            message = "<b>Instance node detail</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)            
    @RequestMapping(
        path = "/{id}/nodes/{nodeId}",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstanceNode(
        @PathVariable("id") Long id,    
        @PathVariable("nodeId") Long nodeInstanceId,    
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getInstanceNode(AuthenticationContextHolder.get(),
                id,
                nodeInstanceId,
                fetch));
    }        

    @ApiOperation(
        value = "Get instance node commands",
        notes = ""
            + "Get a list of all instance node commands in chronological execution order.<br/>"
            + "Commands are available only to those nodes that delegate their execution to an asynchronous command.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details) and these filters:"
            + "<ul><li><b>failedOnly</b>: get only commands whose execution failed</ul>")    
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceCommandDTO.class, responseContainer = "List",
            message = "<b>Instance node commands list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)                
    @RequestMapping(
        path = "/{id}/nodes/{nodeId}/commands",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstanceNodeCommands(
        @PathVariable("id") Long id,  
        @PathVariable("nodeId") Long nodeInstanceId,
        @RequestParam(name = "failedOnly", required = false, defaultValue = "false") Boolean failedOnly,
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getInstanceNodeCommands(AuthenticationContextHolder.get(),
                id,
                nodeInstanceId,
                failedOnly,
                fetch));
    }  

    @ApiOperation(
        value = "Get instance node variables",
        notes = ""
            + "Get a map of instance node variables with metadata and value.<br/>"
            + "Returned variables are only those in node context (i.e. local scope).<br/><br/>"                
            + "Operation supports paging, dynamic properties fetch&ast; and sort (see model for details) and these filters:"
            + "<ul><li><b>publicOnly</b>: get only public variables (i.e. marked as business, input, output or configuration)</ul>"
            + "[&ast;] With special fetch syntax <i>@<variable-name></i> it's possible to get only listed variables")    
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceVariableDTO.Map.class, 
            message = "<b>Instance node variables map</b>"),          
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)          
    @RequestMapping(
        path = "/{id}/nodes/{nodeId}/variables",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstanceNodeVariables(
        @PathVariable("id") Long id,    
        @PathVariable("nodeId") Long nodeInstanceId,            
        @RequestParam(name = "publicOnly", required = false, defaultValue = "false") Boolean publicOnly,
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getInstanceNodeVariables(AuthenticationContextHolder.get(),
                id,
                nodeInstanceId,
                publicOnly, 
                fetch));
    }    

    @ApiOperation(
        value = "Get instance node variable values",
        notes = ""
            + "Get a map of instance node variables values only.<br/>"
            + "Returned variables are only those in node context (i.e. local scope).<br/><br/>"
            + "Operation supports paging, dynamic properties fetch&ast; and sort (see model for details) and these filters:"
            + "<ul><li><b>publicOnly</b>: get only public variables (i.e. marked as business, in, out or config)</ul>"
            + "[&ast;] With special fetch syntax <i>@<variable-name></i> it's possible to get only listed variables")    
    @ApiResponses({
        @ApiResponse(code = 200, response = Map.class, 
            message = "<b>Instance variables map</b>"),          
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)              
    @RequestMapping(
        path = "/{id}/nodes/{nodeId}/variable-values",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstanceNodeVariableValues(
        @PathVariable("id") Long id,    
        @PathVariable("nodeId") Long nodeInstanceId,            
        @RequestParam(name = "publicOnly", required = false, defaultValue = "false") Boolean publicOnly)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getInstanceNodeVariableValues(AuthenticationContextHolder.get(),
                id,
                nodeInstanceId,
                publicOnly));
    }    

    @ApiOperation(
        value = "Update instance node variable", 
        notes = ""
        + "Update instance node variable value.<br/>"
        + "Only active nodes allow variable updates.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = VoidDTO.class, 
            message = "<b>Variable successfully updated</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>INSTANCE_NOT_ACTIVE</b>: Instance not active"                    
            + "<li><b>INSTANCE_NODE_NOT_ACTIVE</b>: Node not active")
    })
    @ResponseStatus(HttpStatus.OK)               
    @RequestMapping(
        path = "/{id}/nodes/{nodeId}/variables/{name}/value",    
        method = RequestMethod.PUT, 
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateInstanceNodeVariable(
        @PathVariable("id") Long id,    
        @PathVariable("nodeId") Long nodeInstanceId,            
        @PathVariable("name") String name,    
        @RequestBody Object value)
        throws ApplicationException{

        instanceService.updateInstanceNodeVariable(AuthenticationContextHolder.get(),
            id,
            nodeInstanceId,
            name, 
            value);

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }    
    
    @ApiOperation(
        value = "Abort instance node", 
        notes = ""
        + "Abort instance node.<br/>"
        + "Only some kinds of active nodes (i.e. work item based) supports abort.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceNodeDTO.class, 
            message = "<b>Node successfully aborted</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>INSTANCE_NOT_ACTIVE</b>: Instance not active"                    
            + "<li><b>INSTANCE_NODE_NOT_ACTIVE</b>: Node not active")
    })
    @ResponseStatus(HttpStatus.OK)     
    @RequestMapping(
        path = "/{id}/nodes/{nodeId}/actions/abort",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> abortInstanceNode(
        @PathVariable("id") Long id,    
        @PathVariable("nodeId") Long nodeInstanceId,            
        @RequestBody OperationDTO body)
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.abortInstanceNode(AuthenticationContextHolder.get(), id, nodeInstanceId, body));
    }       

    @ApiOperation(
        value = "Complete instance node", 
        notes = ""
        + "Force completion of instance node.<br/>"
        + "Only some kinds of active nodes (i.e. work item based) supports complete.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceNodeDTO.class, 
            message = "<b>Node successfully completed</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>INSTANCE_NOT_ACTIVE</b>: Instance not active"                    
            + "<li><b>INSTANCE_NODE_NOT_ACTIVE</b>: Node not active"
            + "<li><b>INSTANCE_COMMAND_EXECUTING</b>: Node is currently executing command")
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/{id}/nodes/{nodeId}/actions/complete",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> completeInstanceNode(
        @PathVariable("id") Long id,    
        @PathVariable("nodeId") Long nodeInstanceId,            
        @RequestBody CompleteInstanceNodeDTO body)
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.completeInstanceNode(AuthenticationContextHolder.get(), id, nodeInstanceId, body));
    }       
    
    @ApiOperation(
        value = "Retry instance node", 
        notes = ""
        + "Retry execution of instance node.<br/>"
        + "Only some kinds of active nodes (i.e. work item based backed by an asynchronous command) supports retry.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceNodeDTO.class, 
            message = "<b>Node command successfully scheduled for retry</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>INSTANCE_NOT_ACTIVE</b>: Instance not active"                    
            + "<li><b>INSTANCE_NODE_NOT_ACTIVE</b>: Node not active"
            + "<li><b>INSTANCE_COMMAND_EXECUTING</b>: Node is currently executing command")
    })    
    @RequestMapping(
        path = "/{id}/nodes/{nodeId}/actions/retry",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> retryInstanceNode(
        @PathVariable("id") Long id,    
        @PathVariable("nodeId") Long nodeInstanceId,            
        @RequestBody RetryInstanceNodeDTO body)
        throws ApplicationException, IOException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.retryInstanceNode(AuthenticationContextHolder.get(), id, nodeInstanceId, body));
    }       
    
    @ApiOperation(
        value = "Get instance commands",
        notes = ""
            + "Get a list of all instance commands for any node in chronological execution order.<br/>"
            + "Commands are available only to those nodes that delegate their execution to an asynchronous command.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details) and these filters:"
            + "<ul><li><b>failedOnly</b>: get only commands whose execution failed</ul>")    
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceCommandDTO.class, responseContainer = "List",
            message = "<b>Instance commands list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)                    
    @RequestMapping(
        path = "/{id}/commands",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstanceCommands(
        @PathVariable("id") Long id,    
        @RequestParam(name = "failedOnly", required = false, defaultValue = "false") Boolean failedOnly,
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getInstanceCommands(AuthenticationContextHolder.get(),
                id,
                failedOnly,
                fetch));
    }   

    @ApiOperation(
        value = "Get instance tasks",
        notes = ""
            + "Get a paged list of all instance related tasks.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details) and these filters:"
            + "<ul><li><b>activeOnly</b>: get only active tasks</ul>")    
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.Page.class,
            message = "<b>Tasks list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)                    
    @RequestMapping(
        path = "/{id}/tasks",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstanceTasks(
        @PathVariable("id") Long id,    
        @RequestParam(name = "activeOnly", required = false, defaultValue = "false") Boolean activeOnly,
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getInstanceTasks(AuthenticationContextHolder.get(),
                id,
                activeOnly,
                pageNumber, pageSize, orderBy,
                fetch));
    }      

    @ApiOperation(
        value = "Get tree tasks",
        notes = ""
            + "Get a paged list of all instance tree related tasks.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details) and these filters:"
            + "<ul><li><b>activeOnly</b>: get only active tasks</ul>")    
    @ApiResponses({
        @ApiResponse(code = 200, response = TaskDTO.Page.class,
            message = "<b>Tasks list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)        
    @RequestMapping(
        path = "/{id}/tree-tasks",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTreeTasks(
        @PathVariable("id") Long id,    
        @RequestParam(name = "activeOnly", required = false, defaultValue = "false") Boolean activeOnly,
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getTreeTasks(AuthenticationContextHolder.get(),
                id,
                activeOnly,
                pageNumber, pageSize, orderBy,
                fetch));
    }    

    @ApiOperation(
        value = "Get tree instances",
        notes = ""
            + "Get a list of all tree instances (of tree this instance belongs), excluding the instance itself.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details).<br/>")    
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceDTO.class, responseContainer = "List",
            message = "<b>Tasks list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)        
    @RequestMapping(
        path = "/{id}/tree-instances",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTreeInstances(
        @PathVariable("id") Long id,    
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getTreeInstances(AuthenticationContextHolder.get(),
                id,
                fetch));
    }  

    @ApiOperation(
        value = "Get instance notifications",
        notes = ""
            + "Get a paged list of all instance related notifications.<br/><br/>"
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
    public ResponseEntity<?> getInstanceNotifications(
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
            .body(instanceService.getInstanceNotifications(AuthenticationContextHolder.get(),
                id,
                activeOnly,
                tags,
                pageNumber, pageSize, orderBy,
                fetch));
    }   
    
    @ApiOperation(
        value = "Abort instance", 
        notes = ""
        + "Abort instance.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceNodeDTO.class, 
            message = "<b>Instance successfully aborted</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>INSTANCE_NOT_ACTIVE</b>: Instance not active")
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
        path = "/{id}/actions/abort",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> abortInstance(
        @PathVariable("id") Long id,
        @RequestBody AbortInstanceDTO body)
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.abortInstance(AuthenticationContextHolder.get(), id, body));
    }       

    @ApiOperation(
        value = "Retry instance", 
        notes = ""
        + "Retry execution of instance.<br/>"
        + "Only some kinds of instances (i.e. with asynchronous node execution backed by command) supports retry.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceDTO.class, 
            message = "<b>Instance command successfully scheduled for retry</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>INSTANCE_NOT_ACTIVE</b>: Instance not active"                    
            + "<li><b>INSTANCE_COMMAND_NOT_FOUND</b>: Instance Command not found"
            + "<li><b>INSTANCE_COMMAND_EXECUTING</b>: Instance is currently executing command")
    })    
    @RequestMapping(
        path = "/{id}/actions/retry",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> retryInstance(
        @PathVariable("id") Long id,    
        @RequestBody RetryInstanceDTO body)
        throws ApplicationException, IOException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.retryInstance(AuthenticationContextHolder.get(), id, body));
    }       
    
    @ApiOperation(
        value = "Create instance bookmark", 
        notes = ""
        + "Create a new bookmark for the instance.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = BookmarkDTO.class, 
            message = "<b>Bookmark successfully created</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")
    })
    @ResponseStatus(HttpStatus.CREATED)    
    @RequestMapping(
        path = "/{id}/bookmarks",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createInstanceBookmark(
        @PathVariable("id") Long id,
        @RequestBody CreateBookmarkDTO body)
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(instanceService.createBookmark(AuthenticationContextHolder.get(), id, body));
    }       

    @ApiOperation(
        value = "Get instance chat",
        notes = ""
            + "Get detail of instance chat status.<br/>"
            + "Instance chat is associated with the instance tree, so every instance of the tree share the same chat.<br>"
            + "In some rare workflow scenario, when the tree root instance terminates before its children, chat retrieving could fail and return a 404 code.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details).<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ChatDTO.class, 
            message = "<b>Instance chat</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })    
    @RequestMapping(
        path = "/{id}/chat",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstanceChat(
        @PathVariable("id") Long id,    
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getChat(AuthenticationContextHolder.get(),
                id,
                fetch));
    }    

    @ApiOperation(
        value = "Mark instance chat as read", 
        notes = ""
        + "Mark instance chat status as read.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = VoidDTO.class, 
            message = "<b>Chat successfully marked as read</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>INSTANCE_NOT_ACTIVE</b>: Instance not active")
    })
    @ResponseStatus(HttpStatus.OK)     
    @RequestMapping(
        path = "/{id}/chat/read",    
        method = RequestMethod.PUT, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> markInstanceChatAsRead(
        @PathVariable("id") Long id)
        throws ApplicationException {
        
        instanceService.markChatAsRead(AuthenticationContextHolder.get(), id);
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }    

    @ApiOperation(
        value = "Get instance chat messages",
        notes = ""
            + "Get a paged list of all instance chat messages.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details).<br/>")    
    @ApiResponses({
        @ApiResponse(code = 200, response = ChatMessageDTO.Page.class, 
            message = "<b>Instance chat messages list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)            
    @RequestMapping(
        path = "/{id}/chat/messages",    
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstanceChatMessages(
        @PathVariable("id") Long id,    
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.getChatMessages(AuthenticationContextHolder.get(),
                id,
                pageNumber, pageSize, orderBy,
                fetch));
    }     
    
    @ApiOperation(
        value = "Publish instance chat message", 
        notes = ""
        + "Publish a new instance chat message.<br/>"
        + "Publishing is an asynchronous operation, so nothing is returned as response and there's no guarantee of message reception.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = VoidDTO.class, 
            message = "<b>Message successfully published</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>INSTANCE_NOT_ACTIVE</b>: Instance not active")
    })    
    @ResponseStatus(HttpStatus.CREATED)    
    @RequestMapping(
        path = "/{id}/chat/messages",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> publishInstanceChatMessage(
        @PathVariable("id") Long id,
        @RequestBody PublishChatMessageDTO body)
        throws ApplicationException {
        
        instanceService.publishChatMessage(AuthenticationContextHolder.get(), id, body);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }       
    
    @ApiOperation(
        value = "Delete instance chat message", 
        notes = ""
        + "Delete an instance chat message.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = VoidDTO.class, 
            message = "<b>Message successfully deleted</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>INSTANCE_NOT_ACTIVE</b>: Instance not active")
    })    
    @ResponseStatus(HttpStatus.OK)      
    @RequestMapping(
        path = "/{id}/chat/messages/{messageId}",    
        method = RequestMethod.DELETE, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteInstanceChatMessage(
        @PathVariable("id") Long id,
        @PathVariable("messageId") Long messageId)
        throws ApplicationException {
        
        instanceService.deleteChatMessage(AuthenticationContextHolder.get(), id, messageId);
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }       
    
    @ApiOperation(
        value = "Get instance form template",
        notes = ""
        + "Get info/clone form FTL template of the specified type.<br/>")
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
        path = "/{id}/forms/{name}/template",
        method = RequestMethod.GET, 
        produces = "text/plain"
    )
    public void getInstanceFormTemplate(
        @PathVariable("id") Long id,
        @ApiParam(allowableValues = "info,clone", required = true)    
        @PathVariable("name") String name, 
        @RequestParam(value = "type", required = false, defaultValue = "vue") String type,
        HttpServletResponse response)
        throws ApplicationException, IOException{
        
        instanceService.getInstanceFormTemplate(
            AuthenticationContextHolder.get(), id, name, type, response);
    }       

    @ApiOperation(
        value = "Get instance form script",
        notes = ""
        + "Get info/clone form script.<br/>")
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
        path = "/{id}/forms/{name}/script",
        method = RequestMethod.GET, 
        produces = "text/javascript"
    )
    public void getInstanceFormScript(
        @PathVariable("id") Long id,
        @ApiParam(allowableValues = "info,clone", required = true)    
        @PathVariable("name") String name, 
        @RequestParam(value = "type", required = false, defaultValue = "vue") String type,
        HttpServletResponse response)
        throws ApplicationException, IOException{
        
        instanceService.getInstanceFormTemplate(
            AuthenticationContextHolder.get(), id, name, type, response);
    }       
    
    @ApiOperation(
        value = "Create instance form view",
        notes = ""
        + "Generate a info/clone form HTML view for the specified template type.<br/>")
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
        path = "/{id}/forms/{name}/views",
        method = RequestMethod.POST, 
        produces = "text/html"
    )
    public void createInstanceFormView(
        @PathVariable("id") Long id, 
        @ApiParam(allowableValues = "info,clone", required = true)    
        @PathVariable("name") String name, 
        @RequestBody CreateFormViewDTO body,
        HttpServletResponse response)
        throws ApplicationException, IOException, TemplateException{
        
        instanceService.createInstanceFormView(
            AuthenticationContextHolder.get(), id, name, body, response);
    }              
}
