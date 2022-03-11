/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper;


import it.filippetti.ks.api.bpm.dto.PageDTO;
import it.filippetti.ks.api.bpm.model.Pager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author mazzocchetti
 * @param <I>
 * @param <O>
 * @param <C> 
 */
@Transactional(readOnly = true)
public abstract class Mapper<I, O, C> {
    
    private static final Logger log = LoggerFactory.getLogger(Mapper.class);
    
    /**
     * 
     * @param input
     * @param context
     * @return 
     */
    public O map(I input, C context) {
        try {
            return input == null ?
                nullMapping() :
                include(input, context) ?
                    doMapping(input, context) :
                    null;
        } catch (Throwable e) {
           throw new MappingException(e);
        }
    }
    
    /**
     * 
     * @param input
     * @param context
     * @return 
     */
    public List<O> map(Collection<I> input, C context) {
        
        List<O> output = new ArrayList<>();
        for (I i : input) {
            if (include(i, context)) {
                output.add(map(i, context));
            }
        }
        return output;
    }
    /**
     * 
     * @param input
     * @param context
     * @return 
     */
    public Map<String, O> map(
        Map<? extends Object, ? extends I> input, 
        C context) {

        Map<String, O> output = new HashMap<>();
        for (Map.Entry<? extends Object, ? extends I> i : input.entrySet()) {
            if (include(i.getValue(), context)) {
                output.put(
                    i.getKey().toString(),
                    map(i.getValue(), context));
            }
        }
        return output;
    }
    
    /**
     * 
     * @param input
     * @param context
     * @return 
     */
    public PageDTO<O> map(Page<I> input, C context) {
        
        return new PageDTO<>(
            input.getTotalElements(), 
            ((Pager.Request) input.getPageable()).getOneBasedPageNumber(),
            input.getPageable().getPageSize(),
            input.getPageable().getSort()
                .toList()
                .stream()
                .map(o -> new PageDTO.Order(
                    ((Pager.Order) o).getSortable().property(),
                    o.getDirection().name()))
                .collect(Collectors.toList()),
            map(input.getContent(), context));
    }
    
    
    /**
     * 
     * @return 
     */
    protected O nullMapping() {
        return null;
    }
    
    /**
     * 
     * @param input
     * @param context
     * @return 
     */
    protected boolean include(I input, C context) {
        return true;
    }
    
    /**
     * 
     * @param input
     * @param context
     * @return 
     * @throws java.lang.Exception 
     */
    protected abstract O doMapping(I input, C context) throws Exception;
}
