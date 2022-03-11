/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.HistoryDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class HistoryMapper extends ContextMapper<History, HistoryDTO> {

    @Autowired
    private TaskMapper taskMapper;
    
    @Override
    protected HistoryDTO doMapping(History history, MappingContext ctx) throws Exception {
        
        HistoryDTO dto = new HistoryDTO();

        dto.setTimestamp(history.getTimestamp());
        dto.setAction(history.getAction());
        dto.setUserId(history.getUserId());
        dto.setTargetIdentity(history.getTargetIdentity());
        dto.setMessage(history.getMessage());
        if (ctx.fetch("task")) {
            dto.setTask(taskMapper.map(
                history.getTask(),
                ctx.of("task")));
        }            
        return dto;
    }
}
