/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.EventDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.Event;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class EventMapper extends ContextMapper<Event, EventDTO> {

    @Override
    protected EventDTO doMapping(Event event, MappingContext ctx) throws Exception {
        
        EventDTO dto = new EventDTO();
        
        dto.setId(event.getEventId());
        dto.setType(event.getType().name());
        dto.setStart(event.isStart());
        dto.setCorrelationSubscription(event.getCorrelationSubscription());
        dto.setRetrievalExpression(event.getRetrievalExpression());
        dto.setQueue(event.getQueue());
        dto.setSummary(event.getSummary());
        dto.setSchema(event.getSchema());
        if (ctx.fetch("properties")) {
            dto.setProperties(event.getProperties().asMap());
        }
        return dto;        
    }
    
}
