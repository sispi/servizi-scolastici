/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.InstanceCommandErrorDTO;
import it.filippetti.ks.api.bpm.mapper.SimpleMapper;
import it.filippetti.ks.api.bpm.model.InstanceCommandError;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class InstanceCommandErrorMapper extends SimpleMapper<InstanceCommandError, InstanceCommandErrorDTO> {

    @Override
    protected InstanceCommandErrorDTO doMapping(InstanceCommandError error) throws Exception {
        
        InstanceCommandErrorDTO dto = new InstanceCommandErrorDTO();
        
        dto.setMessage(error.getMessage());
        dto.setTimestamp(error.getTimestamp());
        dto.setStacktrace(error.getStacktrace());
        return dto;        
    }
}
