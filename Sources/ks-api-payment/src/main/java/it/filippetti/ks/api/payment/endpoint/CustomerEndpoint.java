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
import it.filippetti.ks.api.payment.dto.CreateCustomerDTO;
import it.filippetti.ks.api.payment.dto.CustomerDTO;
import it.filippetti.ks.api.payment.dto.ErrorDTO;
import it.filippetti.ks.api.payment.dto.UpdateCustomerDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.service.CustomerService;
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
@Api(tags = {"/customers"}) 
@RestController
@RequestMapping("/customers")
public class CustomerEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(CustomerEndpoint.class);
    
    @Autowired
    private CustomerService customerService;
    
    @ApiOperation(value = "Create customer", notes = "Create a customer.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = CustomerDTO.class, message = "<b>Customer successfully created</b>"),
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
    public ResponseEntity<?> create(@RequestBody CreateCustomerDTO createCustomerDTO) throws ApplicationException {
        log.info("Rest request to create Customer");
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(AuthenticationContextHolder.get(), createCustomerDTO));
    }
    
    @ApiOperation(value = "Get Customer", notes = "Get detail of Customer.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = CustomerDTO.class, message = "<b>Deployment detail</b>"),
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
        log.info("Rest request to get Customer");
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findOne(AuthenticationContextHolder.get(), id));
    }
    
    @ApiResponses({
        @ApiResponse(code = 204, message = "<b>Deleted</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
    })
    @ApiOperation(value = "Delete Customer")
    @ResponseStatus(HttpStatus.NO_CONTENT)         
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws ApplicationException{
        log.info("Rest request to delete Customer");
        customerService.delete(AuthenticationContextHolder.get(), id);
        return ResponseEntity.noContent().build();
    }
    
    @ApiOperation(value = "Update Customer", notes = "Update a Customer.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = CustomerDTO.class, message = "<b>Customer successfully updated</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, message = "Operation failed<br><br>"
            + "<li><b>EXAMPLE_BUSINESS_ERROR</b>: Business error description")       
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
        method = RequestMethod.PUT, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> update(@RequestBody UpdateCustomerDTO updateCustomerDTO) throws ApplicationException{
        log.info("Rest request to update Customer");
        return ResponseEntity.status(HttpStatus.OK).body(customerService.update(AuthenticationContextHolder.get(), updateCustomerDTO));
    }
    
    @ApiOperation(value = "Get Customer", notes = "Get a paged list of Customers.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = CustomerDTO.Page.class, message = "<b>Customer list</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllPaged(
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy
    ) throws ApplicationException {
        log.info("Rest request to get all Customers");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(customerService.findAllPaged(AuthenticationContextHolder.get(), pageNumber, pageSize, orderBy));
    }
    
}
