/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.mapper.dto;

import it.filippetti.ks.api.payment.dto.ExampleDTO;
import it.filippetti.ks.api.payment.mapper.ContextMapper;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.model.Example;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class ExampleMapper extends ContextMapper<Example, ExampleDTO> {

    @Override
    protected ExampleDTO doMapping(Example example, MappingContext ctx) throws Exception {
    
        ExampleDTO dto = new ExampleDTO();

        dto.setId(example.getId());
        dto.setName(example.getName());
        dto.setTimestamp(example.getLastModifiedTs());
        if (ctx.fetch("parent")) {
            dto.setParent(this.map(
                example.getParent(), 
                ctx.of("parent")));
        }
        if (ctx.fetch("children")) {
            dto.setChildren(this.map(
                example.getChildren(), 
                ctx.of("children")));
        }
        return dto;
    }    
}
