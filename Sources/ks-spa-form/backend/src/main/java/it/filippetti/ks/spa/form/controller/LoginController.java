/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.controller;

import it.filippetti.ks.spa.form.configuration.ApplicationProperties;
import it.filippetti.ks.spa.form.exception.ApplicationException;
import it.filippetti.ks.spa.form.exception.NotFoundException;
import it.filippetti.ks.spa.form.service.IdentityService;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import keysuite.docer.client.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author marco.mazzocchetti
 */
@Profile({"local", "development"})
@Controller
public class LoginController {
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private IdentityService identityService;
    
    @RequestMapping(
        path = "/login",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(
        @RequestParam(name = "tenant", required = false) String tenant,
        @RequestParam(name = "organization", required = false) String organization,
        @RequestParam(name = "userId", required = false) String userId,
        HttpServletResponse response) 
        throws ApplicationException {
        
        User user;
        String jwtToken;
        Cookie cookie;
        
        // apply defaults
        tenant = tenant != null ? tenant : IdentityService.TEST_TENANT;
        organization = organization != null ? organization : IdentityService.TEST_ORGANIZATION;
        userId = userId != null ? userId : applicationProperties.adminUser();
        
        // get user
        user = identityService.getUser(tenant, organization, userId).orElse(null);
        if (user == null) {
            throw new NotFoundException();
        }
        
        // generate token
        jwtToken = identityService.generateJwtToken(
            IdentityService.TEST_TENANT, 
            IdentityService.TEST_ORGANIZATION, 
            userId);
        
        // set cookie
        cookie = new Cookie(applicationProperties.name(), jwtToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        
        // return authentication
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Map.of(
                "tenant", tenant,
                "organization", organization,
                "userId", user.getUserName(),
                "isAdmin", user.isAdmin(),
                "roles", user.getGroups()
            ));
    }
}
