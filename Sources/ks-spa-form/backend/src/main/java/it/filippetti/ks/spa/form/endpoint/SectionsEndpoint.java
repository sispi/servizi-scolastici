/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.endpoint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.spa.form.authentication.AuthenticationContextHolder;
import it.filippetti.ks.spa.form.dto.SectionDTO;
import it.filippetti.ks.spa.form.exception.ApplicationException;
import it.filippetti.ks.spa.form.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author marco.mazzocchetti
 */
@Api(tags = {"/sections"}) 
@RestController
@RequestMapping("/api/sections")
public class SectionsEndpoint {

    @Autowired
    private SectionService sectionService;
    
    @ApiOperation(
        value = "Get shared sections",
        notes = ""
            + "Get a paged list of available shared sections.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details) and these filters:"
            + "<ul><li><b>key</b>: search sections by key starting with, case insensitive</ul>")    
    @ApiResponses({
        @ApiResponse(code = 200, response = SectionDTO.Page.class, 
            message = "<b>Section list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation")
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getSections(
        @RequestParam(name = "key", required = false) String key, 
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(sectionService.getSections(AuthenticationContextHolder.get(), 
                key,    
                pageNumber, pageSize, orderBy,
                fetch));
    }    

    @ApiOperation(
        value = "Get shared section value",
        notes = ""
            + "Get a shared section value.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ArrayNode.class, 
            message = "<b>Section value</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/{key}/value",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getSectionValue(
        @PathVariable("key") String key,
        @RequestParam(name = "fetch", required = false) String fetch) 
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(sectionService.getSectionValue(AuthenticationContextHolder.get(), key));
    }      
}
