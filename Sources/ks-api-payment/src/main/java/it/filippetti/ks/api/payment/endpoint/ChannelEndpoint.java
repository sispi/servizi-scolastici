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
import it.filippetti.ks.api.payment.dto.ChannelDTO;
import it.filippetti.ks.api.payment.dto.ConfigurationDTO;
import it.filippetti.ks.api.payment.dto.CreateChannelDTO;
import it.filippetti.ks.api.payment.dto.CreateConfigurationDTO;
import it.filippetti.ks.api.payment.dto.ErrorDTO;
import it.filippetti.ks.api.payment.dto.UpdateChannelDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.service.ChannelService;
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
@Api(tags = {"/channels"}) 
@RestController
@RequestMapping("/channels")
public class ChannelEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(ChannelEndpoint.class);
    
    @Autowired
    private ChannelService channelService;
    
    @Autowired
    private ConfigurationService configurationService;
    
    @ApiOperation(value = "Create channel", notes = "Create a channel.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = ChannelDTO.class, message = "<b>Channel successfully created</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>PAYMENT_BUSINESS_ERROR</b>: Business error")       
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@RequestBody CreateChannelDTO createChannelDTO) throws ApplicationException {
        log.info("Rest request to create Channel");
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.create(AuthenticationContextHolder.get(), createChannelDTO));
    }
    
    @ApiOperation(value = "Create a new configuration for the payment channel", notes = "Create a configuration.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = ConfigurationDTO.class, message = "<b>Configuration successfully created</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>PAYMENT_BUSINESS_ERROR</b>: Business error")       
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(
        path = "/{id}/configurations",
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createConfiguration(@RequestBody CreateConfigurationDTO createConfigurationDTO, @PathVariable("id") Long channelId) throws ApplicationException {
        log.info("Rest request to create Configuration");
        return ResponseEntity.status(HttpStatus.CREATED).body(configurationService.create(AuthenticationContextHolder.get(), createConfigurationDTO, channelId));
    }
    
    @ApiOperation(value = "Get Channel", notes = "Get detail of Channel.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ChannelDTO.class, message = "<b>Deployment detail</b>"),
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
        log.info("Rest request to get Channel");
        return ResponseEntity.status(HttpStatus.OK).body(channelService.findOne(AuthenticationContextHolder.get(), id));
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
    @ApiOperation(value = "Delete Channel")
    @ResponseStatus(HttpStatus.NO_CONTENT)         
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws ApplicationException{
        log.info("Rest request to delete Channel");
        channelService.delete(AuthenticationContextHolder.get(), id);
        return ResponseEntity.ok().build();
    }
    
    @ApiOperation(value = "Update Channel", notes = "Update a Channel.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ChannelDTO.class, message = "<b>Channel successfully updated</b>"),
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
    public ResponseEntity<?> update(@RequestBody UpdateChannelDTO updateChannelDTO, @PathVariable("id") Long id) throws ApplicationException{
        log.info("Rest request to update Channel");
        return ResponseEntity.status(HttpStatus.OK).body(channelService.update(AuthenticationContextHolder.get(), updateChannelDTO, id));
    }
    
    @ApiOperation(value = "Get channels", notes = ""
            + "Get a list of channels.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ChannelDTO.Page.class, message = "<b>Channels list</b>"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation")
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() throws ApplicationException {
        log.info("Rest request to get all Channels");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channelService.findAll(AuthenticationContextHolder.get()));
    }
    
    @ApiOperation(value = "Get configurations", notes = ""
            + "Get a paged list of configurations.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details)")
    @ApiResponses({
        @ApiResponse(code = 200, response = ConfigurationDTO.Page.class, message = "<b>Configurations list</b>"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation")
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(path = "/{id}/configurations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllConfigurationByChannelPaged(
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy,
        @PathVariable("id") Long id
    ) throws ApplicationException {
        log.info("Rest request to get all Configurations of Channel " + id);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(configurationService.findAllByChannelPaged(AuthenticationContextHolder.get(), pageNumber, pageSize, orderBy, id));
    }
    
}
