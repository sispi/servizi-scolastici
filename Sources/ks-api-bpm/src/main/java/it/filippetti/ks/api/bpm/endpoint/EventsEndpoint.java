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
import it.filippetti.ks.api.bpm.dto.ErrorDTO;
import it.filippetti.ks.api.bpm.dto.EventDTO;
import it.filippetti.ks.api.bpm.dto.PublishEventDTO;
import it.filippetti.ks.api.bpm.dto.PublishOutcomeDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.service.EventService;
import java.util.Map;
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
@Api(tags = {"/events"}) 
@RestController
@RequestMapping("/events")
public class EventsEndpoint {

    @Autowired
    private EventService eventService;
    
    public EventsEndpoint() {
    }
    
    @ApiOperation(
        value = "Get events",
        notes = ""
            + "Get a paged list of declared events.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details).<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = EventDTO.Page.class, 
            message = "<b>Events list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
    })
    @ResponseStatus(HttpStatus.OK)        
    @RequestMapping(
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getEvents(
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(eventService.getEvents(AuthenticationContextHolder.get(), pageNumber, pageSize, orderBy, fetch));
    }     

    @ApiOperation(
        value = "Publish event", 
        notes = ""
        + "Publish a declared event.<br/>"
        + "Only events of type <b>message</b> or <b>signal</b> can be published.<br/>"
        + "Parameter outcomeVariable is the variable name of eventually correlated instance "
        + "of which to return and applies only for synchronous <b>message</b> events.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = PublishOutcomeDTO.class, 
            message = "<b>Event successfully published</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>EVENT_PUBLISHING_NOT_SUPPORTED</b>: Publishing not supported for this type of event"
            + "<li><b>EVENT_CORRELATION_ERROR</b>: Publishing failed due to a correlation error")
    })
    @ResponseStatus(HttpStatus.CREATED)          
    @RequestMapping(
        path = "/{id}/actions/publish",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> publishEvent(
        @PathVariable("id") String id,
        @RequestParam(name = "async", required = false, defaultValue = "false") Boolean async, 
        @RequestParam(name = "outcomeVariable", required = false) String outcomeVariable, 
        @RequestBody Map<String, Object> payload)
        throws ApplicationException {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(eventService.publishEvent(
                AuthenticationContextHolder.get(), 
                id, 
                new PublishEventDTO(payload, async, outcomeVariable)));
    }        
}
