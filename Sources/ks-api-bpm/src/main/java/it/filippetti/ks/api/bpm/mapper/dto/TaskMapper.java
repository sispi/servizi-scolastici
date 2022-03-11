/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.TaskDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.Task;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class TaskMapper extends ContextMapper<Task, TaskDTO> {

    @Autowired
    private InstanceMapper instanceMapper;

    @Autowired
    private InstanceNodeMapper nodeMapper;
    
    @Autowired
    private TaskAssignmentsMapper assignmentsMapper;

    @Autowired
    private TaskCommentMapper commentMapper;

    @Autowired
    private TaskAttachmentMapper attachmentMapper;
    
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private HistoryMapper historyMapper;
    
    @Override
    protected TaskDTO doMapping(Task task, MappingContext ctx) throws Exception {
        
        TaskDTO dto = new TaskDTO();
        
        dto.setId(task.getId());
        dto.setBusinessKey(task.getBusinessKey());
        dto.setName(task.getName());
        dto.setSubject(task.getSubject());
        dto.setDescription(task.getDescription());
        dto.setType(task.getTaskType());
        dto.setStartTs(task.getStartTs());
        dto.setEndTs(task.getEndTs());
        dto.setExpireTs(task.getTaskData().getExpirationTime());
        dto.setStatus(task.getTaskData().getStatus().name());
        dto.setPreviousStatus(task.getTaskData().getPreviousStatus() != null ? 
            task.getTaskData().getPreviousStatus().name() : 
            null);
        if (ctx.fetch("instance")) {
            dto.setInstance(instanceMapper.map(
                task.getInstance(),
                ctx.of("instance")));
        }
        if (ctx.fetch("rootInstance")) {
            dto.setInstance(instanceMapper.map(
                task.getRootInstance().orElse(null),
                ctx.of("rootInstance")));
        }        
        if (ctx.fetch("node")) {
            dto.setNode(nodeMapper.map(
                task.getNode(),
                ctx.of("node")));
        }
        if (ctx.fetch("input")) {
            dto.setInput(task.getIO().getInput().asMap());
        }
        if (ctx.fetch("output")) {
            dto.setOutput(task.getIO().getOutput().asMap());
        }
        if (ctx.fetch("assignments")) {
            dto.setAssignments(assignmentsMapper.map(
                task.getAssignments()));
        }
        if (ctx.fetch("authorizations")) {
            dto.setAuthorizations(task.getAuthorizations(ctx.authentication())
                .stream()
                .map(e -> e.name())
                .collect(Collectors.toList()));
        }
        if (ctx.fetch("comments")) {
            dto.setComments(commentMapper.map(
                task.getTaskData().getComments()));
        }
        if (ctx.fetch("attachments")) {
            dto.setAttachments(attachmentMapper.map(
                task.getTaskData().getAttachments()));
        }        
        if (ctx.fetch("notifications?activeOnly")) {
            dto.setNotifications(notificationMapper.map(
                task.getActiveNotifications(ctx.authentication()),
                ctx.of("notifications?activeOnly")));
        }
        if (ctx.fetch("notifications")) {
            dto.setNotifications(notificationMapper.map(
                task.getNotifications(),
                ctx.of("notifications")));
        }
        if (ctx.fetch("history")) {
            dto.setHistory(historyMapper.map(
                task.getHistory(),
                ctx.of("history")));
        }        
        if (ctx.fetch("nextTasks")) {
            dto.setNextTasks(map(
                task.getNextTasks(ctx.authentication()), 
                ctx.of("nextTasks")));
        }
        return dto;
    }
}
