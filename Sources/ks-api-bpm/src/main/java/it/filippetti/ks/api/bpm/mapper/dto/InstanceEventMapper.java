/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.InstanceEventDTO;
import it.filippetti.ks.api.bpm.mapper.SimpleMapper;
import it.filippetti.ks.api.bpm.model.InstanceEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class InstanceEventMapper extends SimpleMapper<InstanceEvent, InstanceEventDTO> {

    @Override
    protected InstanceEventDTO doMapping(InstanceEvent nodeEvent) throws Exception {
        
        InstanceEventDTO dto = new InstanceEventDTO();
        
        dto.setId(nodeEvent.geId());
        dto.setType(nodeEvent.getType());
        dto.setNodeType(nodeEvent.getNodeType());
        dto.setCorrelable(nodeEvent.isCorrelable());
        return dto;
    }
}
