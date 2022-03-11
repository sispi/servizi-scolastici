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
import it.filippetti.ks.api.portal.dto.ErrorDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.service.UserService;
import java.util.Map;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dino
 */
@Api(tags = {"/user"}) 
@RestController
@RequestMapping("/user")
public class UserEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(UserEndpoint.class);
    
    @Autowired
    private UserService userService;
    
    @ApiOperation(value = "Get User", notes = ""
            + "Get detail of User.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details)")
    @ApiResponses({
        @ApiResponse(code = 200, message = "<b>Deployment detail</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, message = "Not Found: resource not found")
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/{userId}",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> findOne(@PathVariable("userId") String userId) throws ApplicationException{
        log.info("Rest request to get User");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserFromDocer(AuthenticationContextHolder.get(), userId));
    }
    
    @ApiOperation(value = "Update User", notes = "Update an user.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, message = "<b>User successfully updated</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, message = "Not Found: resource not found"),
        @ApiResponse(code = 422, response = ErrorDTO.class, message = "Operation failed<br><br>"
            + "<li><b>PORTAL_BUSINESS_ERROR</b>: Business error")
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
        path = "/{userId}",
        method = RequestMethod.PATCH, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> update(@PathVariable("userId") String userId, @RequestBody Map<String, Object> data) throws ApplicationException{
        log.info("Rest request to update User");
        Object object = userService.updateUserFromDocer(AuthenticationContextHolder.get(), userId, data);
        return ResponseEntity.status(HttpStatus.OK).body(object);
        /*
        if(object != null){
            
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } */
    }
    
}
