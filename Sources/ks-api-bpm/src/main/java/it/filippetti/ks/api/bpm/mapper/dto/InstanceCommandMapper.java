/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.InstanceCommandDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.InstanceCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class InstanceCommandMapper extends ContextMapper<InstanceCommand, InstanceCommandDTO> {

    @Autowired
    private InstanceNodeMapper nodeMapper;
    
    @Autowired
    private InstanceCommandErrorMapper errorMapper;
    
    @Override
    protected InstanceCommandDTO doMapping(InstanceCommand command, MappingContext ctx) throws Exception {
        
        InstanceCommandDTO dto = new InstanceCommandDTO();
        
        dto.setId(command.getId());
        dto.setName(command.getName());
        dto.setExecutionTs(command.getExecutionTs());
        dto.setExecutions(command.getExecutions());
        dto.setStatus(command.getStatus().name());
        dto.setMessage(command.getMessage());
        dto.setRetries(command.getRetries());
        if (ctx.fetch("node")) {
            dto.setNode(nodeMapper.map(
                command.getNode(),
                ctx.of("node")));
        }        
        if (ctx.fetch("context")) {
            dto.setContext(command.getIo().getContext().getData());
        }
        if (ctx.fetch("results")) {
            dto.setResults(command.getIo().getResults().getData());
        }
        if (ctx.fetch("errors")) {
            dto.setErrors(errorMapper.map(command.getErrors()));
        }
        return dto;
    }
}
