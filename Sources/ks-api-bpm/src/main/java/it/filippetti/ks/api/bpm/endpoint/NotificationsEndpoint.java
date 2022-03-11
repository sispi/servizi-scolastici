/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.api.bpm.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.bpm.dto.CreateNotificationDTO;
import it.filippetti.ks.api.bpm.dto.NotificationDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.service.NotificationService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@Api(tags = {"/notifications"}) 
@RestController
@RequestMapping("/notifications")
public class NotificationsEndpoint {
    
    @Autowired
    private NotificationService notificationService;
    
    @ApiOperation(
        value = "Create notification", 
        notes = ""
        + "Create a new notification.<br/>"
        + "Notifications explicitly created through API doesn't have a specific context, i.e. are not related to any instance or task.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = NotificationDTO.class, 
            message = "<b>Notification successfully created</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")
    })
    @ResponseStatus(HttpStatus.CREATED)      
    @RequestMapping(
        method = RequestMethod.POST, 
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE            
    )
    public ResponseEntity<?> createNotification(
        @RequestBody CreateNotificationDTO body) 
        throws ApplicationException{

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(notificationService.createNotification(AuthenticationContextHolder.get(), body));
    }      
    
    @ApiOperation(
        value = "Get notifications",
        notes = ""
            + "Get a paged list of all notifications.<br/><br/>"
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
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getNotifications(
        @RequestParam(name = "activeOnly", required = false, defaultValue = "false") Boolean activeOnly,
        @RequestParam(name = "tags", required = false) String tags,
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(notificationService.getNotifications(AuthenticationContextHolder.get(),
                activeOnly,
                tags,
                pageNumber, pageSize, orderBy,
                fetch));
    }      
    
    @ApiOperation(
        value = "Get notification",
        notes = ""
            + "Get detail of notification.<br/>"
            + "Operation supports dynamic properties fetch (see model for details).<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = NotificationDTO.class, 
            message = "<b>Notification detail</b>"),
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
    public ResponseEntity<?> getNotification(
        @PathVariable("id") Long id,    
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(notificationService.getNotification(AuthenticationContextHolder.get(),
                id,
                fetch));
    }       
    
    @ApiOperation(
        value = "Download notification attachment",
        notes = ""
            + "Download a single notification attachment content.<br/><br/>"
            + "Operation requires this filter:"
            + "<ul><li><b>name</b>: get content by attachment name (e.g. attachment-1.txt)</ul>")
    @ApiResponses({
        @ApiResponse(code = 200,
            message = "<b>Notification attachment content</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/{id}/attachment-content",
        method = RequestMethod.GET, 
        produces = MediaType.ALL_VALUE
    )
    public void downloadNotificationAttachment(
        @PathVariable("id") Long id,
        @RequestParam(name = "name", required = true) String name,
        HttpServletResponse response) 
        throws ApplicationException, IOException {
 
        notificationService.downloadNotificationAttachment(AuthenticationContextHolder.get(), id, name, response);
    }    

    @ApiOperation(
        value = "Mark notification as read", 
        notes = ""
        + "Mark notification as read.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = NotificationDTO.class, 
            message = "<b>Notification successfully marked as read</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/{id}/read",    
        method = RequestMethod.PUT, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> markNotificationAsRead(
        @PathVariable("id") Long id)
        throws ApplicationException {
        
        notificationService.markNotificationAsRead(AuthenticationContextHolder.get(), id);
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }      
}
