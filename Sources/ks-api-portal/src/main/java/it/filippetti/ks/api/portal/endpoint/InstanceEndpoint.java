/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.api.portal.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.portal.dto.CreateInstanceDTO;
import it.filippetti.ks.api.portal.dto.ErrorDTO;
import it.filippetti.ks.api.portal.dto.InstanceDTO;
import it.filippetti.ks.api.portal.dto.UpdateInstanceDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.service.InstanceService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
 * @author dino
 */
@Api(tags = {"/instance"}) 
@RestController
@RequestMapping("/instance")
public class InstanceEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(InstanceEndpoint.class);
    
    @Autowired
    private InstanceService instanceService;
    
    @ApiOperation(value = "Create Instance", notes = "" + "Create an Instance.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = InstanceDTO.class, message = "<b>Instance successfully created</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, message = "Operation failed<br><br>"
            + "<li><b>PORTAL_BUSINESS_ERROR</b>: Business error description")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@RequestBody CreateInstanceDTO createInstanceDTO) throws ApplicationException, IOException{
        log.info("Rest request to create Instance");
        return ResponseEntity.status(HttpStatus.CREATED).body(instanceService.create(AuthenticationContextHolder.get(), createInstanceDTO));
    }
    
    @ApiOperation(value = "Update Instance", notes = "Update an Instance.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceDTO.class, message = "<b>Instance successfully updated</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, message = "Not Found: resource not found"),
        @ApiResponse(code = 422, response = ErrorDTO.class, message = "Operation failed<br><br>"
            + "<li><b>PORTAL_BUSINESS_ERROR</b>: Business error")
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
        method = RequestMethod.PUT, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> update(@RequestBody UpdateInstanceDTO updateInstanceDTO) throws ApplicationException, IOException{
        log.info("Rest request to update Instance");
        return ResponseEntity.status(HttpStatus.OK).body(instanceService.update(AuthenticationContextHolder.get(), updateInstanceDTO));
    }
    
    @ApiResponses({
        @ApiResponse(code = 200, message = "<b>Deleted</b>"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, message = "Not Found: resource not found"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>PORTAL_BUSINESS_ERROR</b>: Business error")
    })
    @ApiOperation(value = "Delete status")
    @ResponseStatus(HttpStatus.NO_CONTENT)         
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws ApplicationException{
        log.info("Rest request to delete Instance");
        instanceService.delete(AuthenticationContextHolder.get(), id);
        return ResponseEntity.ok().build();
    }
    
    @ApiOperation(value = "Get Instance", notes = ""
            + "Get detail of Instance.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceDTO.class, message = "<b>Deployment detail</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, message = "Not Found: resource not found")
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/{id}",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> findOne(@PathVariable("id") Long id) throws ApplicationException{
        log.info("Rest request to get Instance");
        return ResponseEntity.status(HttpStatus.OK).body(instanceService.findOne(AuthenticationContextHolder.get(), id));
    }
    
    @ApiOperation(value = "Get instances", notes = ""
            + "Get a paged list of instances.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceDTO.Page.class, message = "<b>instances list</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials")
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllPaged(
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy,
        @RequestParam(name = "sent", required = false, defaultValue = "false") Boolean sent
    ) throws ApplicationException {
        log.info("Rest request to get all instances");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.findAllPaged(AuthenticationContextHolder.get(), pageNumber, pageSize, orderBy, sent));
    }
    
    @ApiOperation(value = "Get Instance by BPM Instance id", notes = ""
            + "Get detail of Instance.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceDTO.class, message = "<b>Deployment detail</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, message = "Not Found: resource not found")
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/bpm/{bpmInstanceId}",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> findOneByBpmInstanceId(@PathVariable("bpmInstanceId") Long bpmInstanceId) throws ApplicationException{
        log.info("Rest request to get Instance by BPM Instance id");
        return ResponseEntity.status(HttpStatus.OK).body(instanceService.findOneByBpmInstanceId(AuthenticationContextHolder.get(), bpmInstanceId));
    }
    
    @ApiOperation(value = "Check if user has already filled out the practice", notes = "Return true if the user can fill the instance")
    @ApiResponses({
        @ApiResponse(code = 200, response = Boolean.class, message = "<b>Check for the multiple instance</b>"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation")
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/can-compile/{instanceType}/{proceedingId}",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> canCompileInstance(@PathVariable("instanceType") InstanceType instanceType, @PathVariable("proceedingId") Long proceedingId) throws ApplicationException{
        log.info("Rest request to check if the user can compile the instance");
        switch (instanceType) {
            case UNIQUE:
                return ResponseEntity.status(HttpStatus.OK).body(instanceService.checkForUniqueInstance(AuthenticationContextHolder.get(), proceedingId));
            case SINGLE:
                return ResponseEntity.status(HttpStatus.OK).body(instanceService.checkForSingleInstance(AuthenticationContextHolder.get(), proceedingId));
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }
    
    
    @ApiOperation(value = "Check if user has already filled out the practice", notes = "Return the practice ID or null if the user can fill the instance")
    @ApiResponses({
        @ApiResponse(code = 200, response = InstanceDTO.class, message = "<b>Check for the multiple instance</b>"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation")
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/find-can-compile/{instanceType}/{proceedingId}",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> findCanCompileInstance(@PathVariable("instanceType") InstanceType instanceType, @PathVariable("proceedingId") Long proceedingId) throws ApplicationException{
        log.info("Rest request to find and check if the user can compile the instance");
        switch (instanceType) {
            case UNIQUE:
                return ResponseEntity.status(HttpStatus.OK).body(instanceService.getForUniqueInstance(AuthenticationContextHolder.get(), proceedingId));
            case SINGLE:
                return ResponseEntity.status(HttpStatus.OK).body(instanceService.getForSingleInstance(AuthenticationContextHolder.get(), proceedingId));
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }
    
    public enum InstanceType {
        UNIQUE, SINGLE;
    }
    
   
    @ApiOperation(value = "Get number of instances", notes = "Get the total number of instances.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = Integer.class, message = "<b>Total number of instances</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
            path = "/count",
            method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> count() throws ApplicationException {
        log.info("Rest request to get the number of all instances");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(instanceService.count(AuthenticationContextHolder.get()));
    }
    
    @ApiOperation(value = "Get number of documents", notes = "Get the total number of documents.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = Integer.class, message = "<b>Total number of documents</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
            path = "/document/count",
            method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> countDocuments() throws ApplicationException {
        log.info("Rest request to get the number of all documents");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(  instanceService.countReceivedDocuments(AuthenticationContextHolder.get()) + 
                    instanceService.countSentDocuments(AuthenticationContextHolder.get()));
    }
    
    
    @ApiOperation(value = "Get number of instances by type", notes = "Get the number of instances grouped by type.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = List.class, message = "<b>Number of instances by type</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
            path = "/count/byType/{start}/{end}",
            method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Object[]>> countByType(
            @PathVariable("start") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date start, 
            @PathVariable("end") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date end) throws ApplicationException {
                
        log.info("Rest request to get number of instances by type");
        List<Object[]> totals = instanceService
                .countByType(AuthenticationContextHolder.get(), start, end);
        
        return ResponseEntity.status(HttpStatus.OK).body(totals);
    }
    
    
    
    @ApiOperation(value = "Get number of instances by status", notes = "Get the number of instances grouped by status.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = List.class, message = "<b>Number of instances by status</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
            path = "/count/byStatus",
            method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Integer>> countByStatus() throws ApplicationException {
                
        log.info("Rest request to get number of instances by status");
        Map<String, Integer> totals = instanceService
                .countByStatus(AuthenticationContextHolder.get());
        
        return ResponseEntity.status(HttpStatus.OK).body(totals);
    }
}
