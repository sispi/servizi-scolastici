/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.api.bpm.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.bpm.dto.BookmarkDTO;
import it.filippetti.ks.api.bpm.dto.VoidDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.service.BookmarkService;
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
@Api(tags = {"/bookmarks"}) 
@RestController
@RequestMapping("/bookmarks")
public class BookmarksEndpoint {
    
    @Autowired
    private BookmarkService bookmarkService;
    
    @ApiOperation(
        value = "Get bookmarks",
        notes = ""
            + "Get a paged list of user instance bookmarks.<br/><br/>"
            + "Operation supports paging.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = BookmarkDTO.Page.class, 
            message = "<b>Bookmarks list</b>"),
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
    public ResponseEntity<?> getBookmarks(
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy) 
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(bookmarkService.getBookmarks(AuthenticationContextHolder.get(),
                pageNumber, pageSize, orderBy));
    }      
    
    @ApiOperation(
        value = "Delete bookmark", 
        notes = ""
        + "Delete a user instance bookmark.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = VoidDTO.class,
            message = "<b>Bookmark successfully deleted</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")
    })      
    @RequestMapping(
        path = "/{id}",    
        method = RequestMethod.DELETE, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteBookmark(
        @PathVariable("id") Long id)
        throws ApplicationException {
        
        bookmarkService.deleteBookmark(AuthenticationContextHolder.get(), id);
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }       
}
