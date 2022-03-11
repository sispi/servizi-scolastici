/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.NotificationRecipientDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.NotificationRecipient;
import it.filippetti.ks.api.bpm.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class NotificationRecipientMapper extends ContextMapper<NotificationRecipient, NotificationRecipientDTO> {

    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private NotificationMapper notificationMapper;
    
    @Override
    protected NotificationRecipientDTO doMapping(NotificationRecipient recipient, MappingContext ctx) throws Exception {
        
        NotificationRecipientDTO dto = new NotificationRecipientDTO();
        dto.setUserId(recipient.getUserId());
        dto.setUserDisplayName(identityService
            .getUser(
                recipient.getNotification().getTenant(), 
                recipient.getUserId())
            .map(u -> u.getFullName())
            .orElse(null));
        dto.setReadTs(recipient.getReadTs());

        return dto;
    }    
}
