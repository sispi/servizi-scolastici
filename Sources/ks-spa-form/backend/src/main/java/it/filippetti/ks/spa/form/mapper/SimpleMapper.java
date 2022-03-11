/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.mapper;


import it.filippetti.ks.spa.form.dto.PageDTO;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

/**
 * 
 * @author mazzocchetti
 * @param <I>
 * @param <O> 
 */
public abstract class SimpleMapper<I, O> extends Mapper<I, O, Void> {
    
    private static final Logger log = LoggerFactory.getLogger(SimpleMapper.class);
    
    /**
     * 
     * @param input
     * @return 
     */
    public O map(I input) {
        return map(input, null);
    }
    
    /**
     * 
     * @param input
     * @return 
     */
    public List<O> map(Collection<I> input) {
        return map(input, null);
    }
    
    /**
     * 
     * @param input
     * @return 
     */
    public PageDTO<O> map(Page<I> input) {
        return map(input, null);
    }
    /**
     * 
     * @param input
     * @return 
     */
    public Map<String, O> map(
        Map<? extends Object, ? extends I> input) {
        return map(input, null);
    }

    @Override
    protected final boolean include(I input, Void context) {
        return include(input);
    }

    protected boolean include(I input) {
        return true;
    }


    @Override
    protected final O doMapping(I input, Void context) throws Exception {
        return doMapping(input);
    }
    
    protected abstract O doMapping(I input) throws Exception;
}

