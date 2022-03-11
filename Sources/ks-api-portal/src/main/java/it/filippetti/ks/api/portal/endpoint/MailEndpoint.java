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
import it.filippetti.ks.api.portal.dto.ContactUsDTO;
import it.filippetti.ks.api.portal.dto.ErrorDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dino
 */
@Api(tags = {"/mail"}) 
@RestController
@RequestMapping("/mail")
public class MailEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(MailEndpoint.class);
    
    @Autowired
    private MailService mailService;
    
    @ApiOperation(value = "Send email", notes = "" + "Create an email.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, message = "<b>Email successfully sended</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, message = "Operation failed<br><br>"
            + "<li><b>PORTAL_BUSINESS_ERROR</b>: Business error description")
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
        path = "/contactUs",
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@RequestBody ContactUsDTO contactUsDTO) throws ApplicationException, Exception{
        log.info("Rest request to send contactUs email");
        mailService.sendContactUsEmail(contactUsDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
}
