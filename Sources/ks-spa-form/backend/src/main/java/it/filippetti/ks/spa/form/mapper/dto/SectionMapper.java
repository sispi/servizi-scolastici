/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.mapper.dto;

import it.filippetti.ks.spa.form.dto.SectionDTO;
import it.filippetti.ks.spa.form.mapper.ContextMapper;
import it.filippetti.ks.spa.form.mapper.MappingContext;
import it.filippetti.ks.spa.form.model.Section;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class SectionMapper extends ContextMapper<Section, SectionDTO> {

    @Override
    protected SectionDTO doMapping(Section section, MappingContext ctx) throws Exception {
        
        SectionDTO dto = new SectionDTO();
        dto.setKey(section.getKey());
        dto.setLastModifiedBy(section.getLastModifiedBy());
        dto.setLastModifiedTs(section.getLastModifiedTs());
        if (ctx.fetch("value")) {
            dto.setValue(section.getValue());
        }
        return dto;
    }
}
