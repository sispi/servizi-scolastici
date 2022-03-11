/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.mapper.dto;

import it.filippetti.ks.api.portal.dto.InstanceDTO;
import it.filippetti.ks.api.portal.mapper.ContextMapper;
import it.filippetti.ks.api.portal.mapper.MappingContext;
import it.filippetti.ks.api.portal.model.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dino
 */
@Component
public class InstanceMapper extends ContextMapper<Instance, InstanceDTO>{
    
    @Autowired
    private ProceedingMapper proceedingMapper;

    @Override
    protected InstanceDTO doMapping(Instance input, MappingContext context) throws Exception {
        InstanceDTO instanceDTO = new InstanceDTO();
        instanceDTO.setId(input.getId());
        instanceDTO.setProceeding(proceedingMapper.map(input.getProceeding(), context));
        instanceDTO.setCreationDate(input.getCreationDate());
        instanceDTO.setDispatchDate(input.getDispatchDate());
        instanceDTO.setModel(input.getModel());
        instanceDTO.setSent(input.getSent());
        instanceDTO.setBpmInstanceId(input.getBpmInstanceId());
        return instanceDTO;
    }
    
}
