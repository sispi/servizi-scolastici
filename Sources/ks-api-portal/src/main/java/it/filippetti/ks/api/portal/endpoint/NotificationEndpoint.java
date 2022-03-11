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
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.service.NotificationService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dino
 */
@Api(tags = {"/notification"}) 
@RestController
@RequestMapping("/notification")
public class NotificationEndpoint {
    private static final Logger log = LoggerFactory.getLogger(NotificationEndpoint.class);
    
    @Autowired
    private NotificationService notificationService;
    
    @ApiOperation(value = "Count notifications")
    @ApiResponses({
        @ApiResponse(code = 200, response = Map.class, message = "<b>Notifications</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials")
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> countNotifications() throws ApplicationException {
        log.info("Rest request to get all notification");
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.countNotification(AuthenticationContextHolder.get()));
    }
    
}
