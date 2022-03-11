/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.NotificationDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class NotificationMapper extends ContextMapper<Notification, NotificationDTO> {

    @Autowired
    private InstanceMapper instanceMapper;

    @Autowired
    private TaskMapper taskMapper;
    
    @Autowired
    private NotificationRecipientMapper recipientMapper;
    
    @Autowired
    private NotificationAttachmentMapper attachmentMapper;
    
    @Override
    protected NotificationDTO doMapping(Notification notification, MappingContext ctx) throws Exception {
        
        NotificationDTO dto = new NotificationDTO();
        
        dto.setId(notification.getId());
        dto.setSubject(notification.getSubject());
        dto.setPriority(notification.hasPriority());
        dto.setNotifyTs(notification.getNotifyTs());
        dto.setExpireTs(Notification.NEVER_EXPIRE_TS.equals(notification.getExpireTs()) ? 
            null : 
            notification.getExpireTs());
        if (ctx.fetch("tags")) {
            dto.setTags(notification.getTags());
        }
        if (ctx.fetch("body")) {
            dto.setBody(notification.getBody().getValue());
        }
        if (ctx.fetch("instance")) {
            dto.setInstance(instanceMapper.map(
                notification.getInstance(),
                ctx.of("instance")));
        }
        if (ctx.fetch("task")) {
            dto.setTask(taskMapper.map(
                notification.getTask(),
                ctx.of("task")));
        }
        if (ctx.fetch("recipients")) {
            dto.setRecipients(recipientMapper.map(
                notification.getRecipients(),
                ctx.of("recipients")));
        }
        if (ctx.fetch("attachments")) {
            dto.setAttachments(attachmentMapper.map(
                notification.getAttachments(),
                ctx.of("attachments")));
        }        
        return dto;
    }
}
