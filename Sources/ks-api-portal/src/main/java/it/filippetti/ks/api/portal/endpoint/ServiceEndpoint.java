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
import it.filippetti.ks.api.portal.dto.CreateServiceDTO;
import it.filippetti.ks.api.portal.dto.ErrorDTO;
import it.filippetti.ks.api.portal.dto.ServiceDTO;
import it.filippetti.ks.api.portal.dto.UpdateServiceDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author dino
 */
@Api(tags = {"/service"}) 
@RestController
@RequestMapping("/service")
public class ServiceEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(ServiceEndpoint.class);
    
    @Autowired
    private ServiceService serviceService;
    
    @ApiOperation(value = "Get services", notes = "Get a paged list of services.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ServiceDTO.Page.class, message = "<b>Service list</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials")
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllPaged(
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy
    ) throws ApplicationException {
        log.info("Rest request to get all Services");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(serviceService.findAllPaged(AuthenticationContextHolder.get(), pageNumber, pageSize, orderBy));
    }
    
    @ApiOperation(value = "Create service", notes = "" + "Create an service.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = ServiceDTO.class, message = "<b>Service successfully created</b>"),
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
    public ResponseEntity<?> create(@RequestBody CreateServiceDTO createServiceDTO) throws ApplicationException{
        log.info("Rest request to create Service");
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceService.create(AuthenticationContextHolder.get(), createServiceDTO));
    }
    
    @ApiOperation(value = "Get service", notes = "Get detail of service.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ServiceDTO.class, message = "<b>Deployment detail</b>"),
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
        log.info("Rest request to get Service");
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.findOne(AuthenticationContextHolder.get(), id));
    }
    
    @ApiOperation(value = "Update service", notes = "Update an service.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ServiceDTO.class, message = "<b>Service successfully updated</b>"),
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
    public ResponseEntity<?> update(@RequestBody UpdateServiceDTO updateServiceDTO) throws ApplicationException{
        log.info("Rest request to update Service");
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.update(AuthenticationContextHolder.get(), updateServiceDTO));
    }
    
    @ApiOperation(value = "Delete service")
    @ApiResponses({
        @ApiResponse(code = 204, message = "<b>Deleted</b>"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, message = "Not Found: resource not found"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>PORTAL_BUSINESS_ERROR</b>: Business error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)         
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws ApplicationException{
        log.info("Rest request to delete Service");
        serviceService.delete(AuthenticationContextHolder.get(), id);
        return ResponseEntity.ok().build();
    }
    
    @ApiOperation(value = "Get services tree", notes = ""
            + "Get a services tree.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details)")
    @ApiResponses({
        @ApiResponse(code = 200, message = "<b>Service Tree</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials")
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(path = "/tree", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTreeByAoo() throws ApplicationException {
        log.info("Rest request to get Services tree");
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.serviceTree(AuthenticationContextHolder.get()));
    }
    
}
