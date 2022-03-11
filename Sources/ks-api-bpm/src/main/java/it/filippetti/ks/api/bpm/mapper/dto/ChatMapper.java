/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.ChatDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.ChatMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class ChatMapper extends ContextMapper<ChatMember, ChatDTO> {

    @Autowired
    private ChatMemberMapper memberMapper;

    @Autowired
    private ChatMessageMapper messageMapper;
    
    @Override
    protected ChatDTO doMapping(ChatMember chatMember, MappingContext ctx) throws Exception {
        
        ChatDTO dto = new ChatDTO();
        
        dto.setId(chatMember.getChat().getId());
        dto.setRead(chatMember.isRead());
        dto.setLastReadTs(chatMember.getLastReadTs());
        dto.setLastSendTs(chatMember.getChat().getLastSendTs());
        if (ctx.fetch("members")) {
            dto.setMembers(memberMapper.map(
                chatMember.getMembers(),
                ctx.of("members")));
        }
        if (ctx.fetch("messages")) {
            dto.setMessages(messageMapper.map(
                chatMember.getMessages(),
                ctx.of("messages")));
        }        
        return dto;
    }    
}
