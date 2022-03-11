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
import it.filippetti.ks.api.portal.dto.CreateMediaDTO;
import it.filippetti.ks.api.portal.dto.ErrorDTO;
import it.filippetti.ks.api.portal.dto.MediaDTO;
import it.filippetti.ks.api.portal.dto.UpdateMediaDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.service.MediaService;
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
@Api(tags = {"/media"}) 
@RestController
@RequestMapping("/media")
public class MediaEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(MediaEndpoint.class);
    
    @Autowired
    private MediaService mediaService;
    
    @ApiOperation(value = "Create media")
    @ApiResponses({
        @ApiResponse(code = 201, response = MediaDTO.class, message = "<b>Media successfully created</b>"),
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
    public ResponseEntity<?> create(@RequestBody CreateMediaDTO createMediaDTO) throws ApplicationException{
        log.info("Rest request to create Media");
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaService.create(AuthenticationContextHolder.get(), createMediaDTO));
    }
    
    @ApiOperation(value = "Get media", notes = "Get detail of media.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = MediaDTO.class, message = "<b>Deployment detail</b>"),
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
        log.info("Rest request to get Media");
        return ResponseEntity.status(HttpStatus.OK).body(mediaService.findOne(AuthenticationContextHolder.get(), id));
    }
    
    @ApiOperation(value = "Get medias", notes = "Get a paged list of medias.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = MediaDTO.Page.class, message = "<b>medias list</b>"),
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
        log.info("Rest request to get all Medias");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mediaService.findAllPaged(AuthenticationContextHolder.get(), pageNumber, pageSize, orderBy));
    }
    
    @ApiOperation(value = "Delete media")
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
        log.info("Rest request to delete Media " + id);
        mediaService.delete(AuthenticationContextHolder.get(), id);
        return ResponseEntity.ok().build();
    }
    
    @ApiOperation(value = "Update media", notes = "Update an media.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = MediaDTO.class, message = "<b>Media successfully updated</b>"),
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
    public ResponseEntity<?> update(@RequestBody UpdateMediaDTO updateMediaDTO) throws ApplicationException{
        log.info("Rest request to update Media");
        return ResponseEntity.status(HttpStatus.OK).body(mediaService.update(AuthenticationContextHolder.get(), updateMediaDTO));
    }
    
    @ApiOperation(value = "Get media", notes = "Get detail of media.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = MediaDTO.class, message = "<b>Deployment detail</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, message = "Not Found: resource not found")
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/key/{myKey}",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> findOneByKey(@PathVariable("myKey") String myKey) throws ApplicationException{
        log.info("Rest request to get Media");
        return ResponseEntity.status(HttpStatus.OK).body(mediaService.findOneByKey(AuthenticationContextHolder.get(), myKey));
    }
    
}
