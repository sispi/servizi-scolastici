/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.NotificationAttachmentDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.NotificationAttachment;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class NotificationAttachmentMapper extends ContextMapper<NotificationAttachment, NotificationAttachmentDTO> {

    @Override
    protected NotificationAttachmentDTO doMapping(NotificationAttachment attachment, MappingContext ctx) throws Exception {
        
        NotificationAttachmentDTO dto = new NotificationAttachmentDTO();
        
        dto.setName(attachment.getName());
        dto.setContentType(attachment.getContentType());
        return dto;
    }
}
