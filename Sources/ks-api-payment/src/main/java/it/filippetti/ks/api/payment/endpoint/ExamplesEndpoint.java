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
import it.filippetti.ks.api.payment.dto.CreateExampleDTO;
import it.filippetti.ks.api.payment.dto.ErrorDTO;
import it.filippetti.ks.api.payment.dto.ExampleDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.service.ExampleService;
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
 * @author marco.mazzocchetti
 */
@Api(tags = {"/examples"}) 
@RestController
@RequestMapping("/examples")
public class ExamplesEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(ExamplesEndpoint.class);
    
    @Autowired
    private ExampleService exampleService;

    @ApiOperation(
        value = "Create example", 
        notes = ""
        + "Create an example.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = ExampleDTO.class, 
            message = "<b>Example successfully created</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
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
    public ResponseEntity<?> createExample(
        @RequestBody CreateExampleDTO dto) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(exampleService.createExample(AuthenticationContextHolder.get(), dto));
    }    

    @ApiOperation(
        value = "Get examples",
        notes = ""
            + "Get a paged list of examples.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details)")
    @ApiResponses({
        @ApiResponse(code = 200, response = ExampleDTO.Page.class, 
            message = "<b>Examples list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials")
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getExamples(
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(exampleService.getExamples(AuthenticationContextHolder.get(), 
                pageNumber, pageSize, orderBy,
                fetch));
    }    

    @ApiOperation(
        value = "Get example",
        notes = ""
            + "Get detail of example.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details)")
    @ApiResponses({
        @ApiResponse(code = 200, response = ExampleDTO.class, 
            message = "<b>Deployment detail</b>"),
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
        path = "/{id}",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getExample(
        @PathVariable("id") Long id,
        @RequestParam(name = "fetch", required = false) String fetch) 
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(exampleService.getExample(AuthenticationContextHolder.get(), id, fetch));
    }    
}
