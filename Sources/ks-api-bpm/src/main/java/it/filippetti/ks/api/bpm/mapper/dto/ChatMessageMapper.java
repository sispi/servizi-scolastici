/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.ChatMessageDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.ChatMessage;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class ChatMessageMapper extends ContextMapper<ChatMessage, ChatMessageDTO> {

    @Override
    protected ChatMessageDTO doMapping(ChatMessage message, MappingContext ctx) throws Exception {
        
        ChatMessageDTO dto = new ChatMessageDTO();
        
        dto.setId(message.getId());
        dto.setSenderInstanceName(message.getSenderInstanceName());
        dto.setSenderUserId(message.getSenderUserId());
        dto.setSenderDisplayName(message.getSenderDisplayName());
        dto.setSendTs(message.getSendTs());
        if (ctx.fetch("text")) {
            dto.setText(message.getText());
        }
        return dto;
    }
}
