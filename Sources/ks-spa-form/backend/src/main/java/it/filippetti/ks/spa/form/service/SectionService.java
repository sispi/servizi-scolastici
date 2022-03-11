/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import it.filippetti.ks.spa.form.dto.PageDTO;
import it.filippetti.ks.spa.form.dto.SectionDTO;
import it.filippetti.ks.spa.form.exception.ApplicationException;
import it.filippetti.ks.spa.form.exception.AuthorizationException;
import it.filippetti.ks.spa.form.exception.NotFoundException;
import it.filippetti.ks.spa.form.mapper.MappingContext;
import it.filippetti.ks.spa.form.mapper.dto.SectionMapper;
import it.filippetti.ks.spa.form.model.AuthenticationContext;
import it.filippetti.ks.spa.form.model.Fetcher;
import it.filippetti.ks.spa.form.model.Pager;
import it.filippetti.ks.spa.form.model.Section;
import it.filippetti.ks.spa.form.repository.SectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class SectionService {
    
    private static final Logger log = LoggerFactory.getLogger(SectionService.class);
    
    @Autowired
    private SectionRepository sectionRepository;
    
    @Autowired
    private SectionMapper sectionMapper;
    
    /**
     * 
     * @param context
     * @param key
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */
    public PageDTO<SectionDTO> getSections(
        AuthenticationContext context, 
        String key, 
        Integer pageNumber, Integer pageSize, String orderBy, 
        String fetch) 
        throws ApplicationException {
        
        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        return sectionMapper.map(
            sectionRepository.findAll(
                context, 
                key,
                Pager.of(Section.class, pageNumber, pageSize, orderBy)), 
            MappingContext.of(context, Fetcher.of(Section.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param key
     * @return
     * @throws ApplicationException 
     */
    public ArrayNode getSectionValue(
        AuthenticationContext context, 
        String key) 
        throws ApplicationException {
        
        Section section;
        
        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        // get section
        section = sectionRepository.findByKey(context, key);
        if (section == null) {
            throw new NotFoundException();
        }
        
        // return definition
        return section.getValue();
    }
}
