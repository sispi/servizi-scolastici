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
import it.filippetti.ks.api.payment.dto.ConfigurationDTO;
import it.filippetti.ks.api.payment.dto.ErrorDTO;
import it.filippetti.ks.api.payment.dto.UpdateConfigurationDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.service.ConfigurationService;
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
@Api(tags = {"/configurations"}) 
@RestController
@RequestMapping("/configurations")
public class ConfigurationEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(ConfigurationEndpoint.class);
    
    @Autowired
    private ConfigurationService configurationService;
    
    @ApiOperation(value = "Get Configuration", notes = "Get detail of Configuration.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ConfigurationDTO.class, message = "<b>Deployment detail</b>"),
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
        log.info("Rest request to get Configuration");
        return ResponseEntity.status(HttpStatus.OK).body(configurationService.findOne(AuthenticationContextHolder.get(), id));
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
    @ApiOperation(value = "Delete Configuration")
    @ResponseStatus(HttpStatus.NO_CONTENT)         
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws ApplicationException{
        log.info("Rest request to delete Configuration");
        configurationService.delete(AuthenticationContextHolder.get(), id);
        return ResponseEntity.ok().build();
    }
    
    @ApiOperation(value = "Get configuration", notes = "Get a paged list of configuration.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ConfigurationDTO.Page.class, message = "<b>Configuration list</b>"),
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
        log.info("Rest request to get all Configurations");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(configurationService.findAllPaged(AuthenticationContextHolder.get(), pageNumber, pageSize, orderBy));
    }
    
    @ApiOperation(value = "Get configuration of a channel", notes = "Get a list of configuration.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ConfigurationDTO.class, message = "<b>Configuration list</b>"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation")
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/channels/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllByChannel(@PathVariable("id") Long id) throws ApplicationException {
        log.info("Rest request to get all Configurations of a Channel");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(configurationService.findAllByChannel(AuthenticationContextHolder.get(), id));
    }
    
    @ApiOperation(value = "Update Configuration", notes = "Update a Configuration.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ConfigurationDTO.class, message = "<b>Configuration successfully updated</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, message = "Not Found: resource not found"),
        @ApiResponse(code = 422, response = ErrorDTO.class, message = "Operation failed<br><br>"
            + "<li><b>PAYMENT_BUSINESS_ERROR</b>: Business error")   
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
        path = "/{id}",
        method = RequestMethod.PUT, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> update(@RequestBody UpdateConfigurationDTO updateConfigurationDTO, @PathVariable("id") Long id) throws ApplicationException{
        log.info("Rest request to update Configuration");
        return ResponseEntity.status(HttpStatus.OK).body(configurationService.update(AuthenticationContextHolder.get(), updateConfigurationDTO, id));
    }
    
    @ApiResponses({
        @ApiResponse(code = 204, message = "<b>Updated</b>")
    })
    @ApiOperation(value = "Update default Configuration")
    @ResponseStatus(HttpStatus.NO_CONTENT)         
    @RequestMapping(path = "/{configurationId}/channels/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateDefault(@PathVariable("configurationId") Long configurationId, @PathVariable("channelId") Long channelId) throws ApplicationException{
        log.info("Rest request to update Configuration");
        configurationService.setDefault(AuthenticationContextHolder.get(), configurationId, channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    
    
}
