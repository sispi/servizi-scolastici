/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.TaskCommentDTO;
import it.filippetti.ks.api.bpm.mapper.SimpleMapper;
import org.kie.api.task.model.Comment;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class TaskCommentMapper extends SimpleMapper<Comment, TaskCommentDTO> {

    @Override
    protected TaskCommentDTO doMapping(Comment comment) throws Exception {
        
        TaskCommentDTO dto = new TaskCommentDTO();
        
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setTimestamp(comment.getAddedAt());
        dto.setUserId(comment.getAddedBy().getId());
        return dto;
    }
    
}
