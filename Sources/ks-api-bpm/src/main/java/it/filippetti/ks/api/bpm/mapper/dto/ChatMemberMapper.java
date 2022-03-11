/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.ChatMemberDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.ChatMember;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class ChatMemberMapper extends ContextMapper<ChatMember, ChatMemberDTO> {

    @Override
    protected ChatMemberDTO doMapping(ChatMember chatMember, MappingContext ctx) throws Exception {
        
        ChatMemberDTO dto = new ChatMemberDTO();

        dto.setInstanceId(chatMember.getInstance().getId());
        dto.setRead(chatMember.isRead());
        dto.setLastReadTs(chatMember.getLastReadTs());
        return dto;
    }
}
