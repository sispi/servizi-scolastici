/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.controller;

import it.filippetti.ks.spa.form.authentication.AuthenticationContextHolder;
import it.filippetti.ks.spa.form.exception.ApplicationException;
import it.filippetti.ks.spa.form.exception.NotFoundException;
import it.filippetti.ks.spa.form.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author marco.mazzocchetti
 */
@Controller
public class SpaController {
    
    @Autowired
    private FormRepository formRepository;
    
    @RequestMapping(
        path = "/console",
        method = RequestMethod.GET, 
        produces = MediaType.TEXT_HTML_VALUE
    )
    public String console() 
        throws ApplicationException {
        return "forward:/";
    }        
    
    @RequestMapping(
        path = "/editor/{id}",
        method = RequestMethod.GET, 
        produces = MediaType.TEXT_HTML_VALUE
    )
    public String editor(@PathVariable("id") String id) 
        throws ApplicationException {
        
        if (formRepository.findById(AuthenticationContextHolder.get(), id) != null) {
            return "forward:/";
        } else {
            throw new NotFoundException();
        }
    }            
}
