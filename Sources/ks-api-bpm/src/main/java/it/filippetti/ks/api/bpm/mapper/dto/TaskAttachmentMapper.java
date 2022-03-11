/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.TaskAttachmentDTO;
import it.filippetti.ks.api.bpm.mapper.SimpleMapper;
import org.kie.api.task.model.Attachment;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class TaskAttachmentMapper extends SimpleMapper<Attachment, TaskAttachmentDTO> {

    @Override
    protected TaskAttachmentDTO doMapping(Attachment attachment) throws Exception {
        
        TaskAttachmentDTO dto = new TaskAttachmentDTO();
        
        dto.setName(attachment.getName());
        dto.setContentType(attachment.getContentType());
        return dto;
    }    
}
