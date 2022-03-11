/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.InstanceContextDTO;
import it.filippetti.ks.api.bpm.mapper.SimpleMapper;
import it.filippetti.ks.api.bpm.model.InstanceContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class InstanceContextMapper extends SimpleMapper<InstanceContext, InstanceContextDTO> {

    @Override
    protected InstanceContextDTO doMapping(InstanceContext context) throws Exception {
        
        InstanceContextDTO dto = new InstanceContextDTO();
        
        dto.setId(context.id());
        dto.setInstanceId(context.instanceId());
        return dto;
    }
}
