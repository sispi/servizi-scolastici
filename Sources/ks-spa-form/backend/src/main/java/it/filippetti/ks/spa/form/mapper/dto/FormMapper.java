/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.mapper.dto;

import it.filippetti.ks.spa.form.dto.FormDTO;
import it.filippetti.ks.spa.form.mapper.ContextMapper;
import it.filippetti.ks.spa.form.mapper.MappingContext;
import it.filippetti.ks.spa.form.model.Form;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class FormMapper extends ContextMapper<Form, FormDTO> {

    @Autowired
    private BackupsMapper backupsMapper;
    
    @Override
    protected FormDTO doMapping(Form form, MappingContext ctx) throws Exception {
    
        FormDTO dto = new FormDTO();

        dto.setId(form.getId().getId());
        dto.setName(form.getName());
        dto.setLastModifiedBy(form.getLastModifiedBy());
        dto.setLastModifiedTs(form.getLastModifiedTs());
        if (ctx.fetch("schema")) {
            dto.setSchema(form.getSchema());
        }
        if (ctx.fetch("definition")) {
            dto.setDefinition(form.getDefinition());
        }
        if (ctx.fetch("backups")) {
            dto.setBackups(backupsMapper.map(
                form.getBackups(),
                ctx.of("backups")));
        }
        return dto;
    }    
}
