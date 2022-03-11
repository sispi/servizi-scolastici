/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.api.payment.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.payment.dto.CreateProviderDTO;
import it.filippetti.ks.api.payment.dto.ErrorDTO;
import it.filippetti.ks.api.payment.dto.ProviderDTO;
import it.filippetti.ks.api.payment.dto.UpdateProviderDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.service.ProviderService;
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
@Api(tags = {"/providers"}) 
@RestController
@RequestMapping("/providers")
public class ProviderEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(ProviderEndpoint.class);
    
    @Autowired
    private ProviderService providerService;
    
    @ApiOperation(value = "Create provider", notes = "Create a provider.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = ProviderDTO.class, message = "<b>Provider successfully created</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>EXAMPLE_BUSINESS_ERROR</b>: Business error description")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@RequestBody CreateProviderDTO createProviderDTO) throws ApplicationException {
        log.info("Rest request to create Provider");
        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.create(AuthenticationContextHolder.get(), createProviderDTO));
    }
    
    @ApiOperation(value = "Get provider", notes = "Get detail of provider.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ProviderDTO.class, message = "<b>Deployment detail</b>"),
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
        log.info("Rest request to get Provider");
        return ResponseEntity.status(HttpStatus.OK).body(providerService.findOne(AuthenticationContextHolder.get(), id));
    }
    
    @ApiResponses({
        @ApiResponse(code = 200, message = "<b>Deleted</b>"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, message = "Not Found: resource not found"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>PAYMENT_BUSINESS_ERROR</b>: Business error")
    })
    @ApiOperation(value = "Delete Provider")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws ApplicationException{
        log.info("Rest request to delete Provider");
        providerService.delete(AuthenticationContextHolder.get(), id);
        return ResponseEntity.ok().build();
    }
    
    @ApiOperation(value = "Update Provider", notes = "Update a Provider.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ProviderDTO.class, message = "<b>Provider successfully updated</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, message = "Operation failed<br><br>"
            + "<li><b>EXAMPLE_BUSINESS_ERROR</b>: Business error description")       
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
        path = "/{id}",
        method = RequestMethod.PUT, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> update(@RequestBody UpdateProviderDTO updateProviderDTO, @PathVariable("id") Long id) throws ApplicationException{
        log.info("Rest request to update Provider");
        return ResponseEntity.status(HttpStatus.OK).body(providerService.update(AuthenticationContextHolder.get(), updateProviderDTO, id));
    }
    
    @ApiOperation(value = "Get providers", notes = ""
            + "Get a paged list of providers.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details)")
    @ApiResponses({
        @ApiResponse(code = 200, response = ProviderDTO.Page.class, message = "<b>Providers list</b>"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation")
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllPaged(
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy
    ) throws ApplicationException {
        log.info("Rest request to get all Providers");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(providerService.findAllPaged(AuthenticationContextHolder.get(), pageNumber, pageSize, orderBy));
    }
    
    @ApiOperation(value = "Get providers", notes = "Get a list of providers.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ProviderDTO.class, message = "<b>Providers list</b>"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation")
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(path = "/list",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() throws ApplicationException {
        log.info("Rest request to get all Providers");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(providerService.findAll(AuthenticationContextHolder.get()));
    }
}
