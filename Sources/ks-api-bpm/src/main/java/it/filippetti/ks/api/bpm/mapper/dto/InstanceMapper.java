/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.InstanceDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.InstanceContext;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class InstanceMapper extends ContextMapper<Instance, InstanceDTO> {

    @Autowired
    private InstanceVariablesMapper variablesMapper;
    
    @Autowired
    private InstanceVariableValuesMapper variableValuesMapper;
    
    @Autowired
    private InstanceNodeMapper nodeMapper;

    @Autowired
    private TaskMapper taskMapper;
    
    @Autowired
    private ConfigurationMapper configurationMapper;
    
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired    
    private InstanceCommandMapper commandMapper;
    
    @Autowired
    private HistoryMapper historyMapper;    
    
    @Autowired
    private ChatMapper chatMapper;
    
    @Override
    protected InstanceDTO doMapping(Instance instance, MappingContext ctx) throws Exception {
        
        InstanceDTO dto = new InstanceDTO();

        dto.setId(instance.getId());
        dto.setName(instance.getName());
        dto.setVersion(instance.getVersion());
        dto.setProcessId(instance.getProcessId());
        dto.setBusinessName(instance.getBusinessName());
        dto.setBusinessState(instance.getBusinessState());
        dto.setCreatorUserId(instance.getCreatorUserId());
        dto.setInitiatorUserId(instance.getInitiatorUserId());
        dto.setStartTs(instance.getStartTs());
        dto.setLastActivityTs(instance.getLastActivityTs());
        dto.setEndTs(instance.getEndTs());
        dto.setArchiveTs(instance.getArchiveTs());
        dto.setStatus(instance.getStatus());
        dto.setRoot(instance.isRoot());
        if (ctx.fetch("rootInstance")) {
            dto.setRootInstance(map(
                instance.getRootInstance().orElse(null), 
                ctx.of("rootInstance")));
        }
        if (ctx.fetch("parentInstance")) {
            dto.setRootInstance(map(
                instance.getParentInstance().orElse(null), 
                ctx.of("parentInstance")));
        }
        if (ctx.fetch("treeInstances")) {
            dto.setTreeInstances(map(
                instance.getTreeInstances(false), 
                ctx.of("treeInstances")));
        }
        if (ctx.fetch("input")) {
            dto.setInput(
                instance.getIO().getInput().asMap());
        }         
        if (ctx.fetch("output")) {
            dto.setOutput(
                instance.getIO().getOutput().asMap());
        }          
        if (ctx.fetch("variables?publicOnly")) {
            dto.setVariables(variablesMapper.map(
                instance.getVariables(InstanceContext.MAIN, true), 
                ctx.of("variables?publicOnly")));
        }
        if (ctx.fetch("variables")) {
            dto.setVariables(variablesMapper.map(
                instance.getVariables(InstanceContext.MAIN, false), 
                ctx.of("variables")));
        }
        if (ctx.fetch("variableValues?publicOnly")) {
            dto.setVariableValues(variableValuesMapper.map(
                instance.getVariables(InstanceContext.MAIN, true), 
                ctx.of("variableValues?publicOnly")));
        }
        if (ctx.fetch("variableValues")) {
            dto.setVariableValues(variableValuesMapper.map(
                instance.getVariables(InstanceContext.MAIN, false), 
                ctx.of("variableValues")));
        }               
        if (ctx.fetch("nodes?activeOnly")) {
            dto.setNodes(nodeMapper.map(
                instance.getNodes(true), 
                ctx.of("nodes?activeOnly")));
        }
        if (ctx.fetch("nodes")) {
            dto.setNodes(nodeMapper.map(
                instance.getNodes(false), 
                ctx.of("nodes")));
        }
        if (ctx.fetch("tasks?activeOnly")) {
            dto.setTasks(taskMapper.map(
                instance.getTasks(true), 
                ctx.of("tasks?activeOnly")));
        }
        if (ctx.fetch("tasks")) {
            dto.setTasks(taskMapper.map(
                instance.getTasks(false), 
                ctx.of("tasks")));
        }
        if (ctx.fetch("treeTasks?activeOnly")) {
            dto.setTreeTasks(taskMapper.map(
                instance.getTreeTasks(true), 
                ctx.of("treeTasks?activeOnly")));
        }
        if (ctx.fetch("treeTasks")) {
            dto.setTreeTasks(taskMapper.map(
                instance.getTreeTasks(false), 
                ctx.of("treeTasks")));
        }
        if (ctx.fetch("configuration")) {
            dto.setConfiguration(configurationMapper.map(
                instance.getConfiguration().orElse(null), 
                ctx.of("configuration")));
        }
        if (ctx.fetch("notifications?activeOnly")) {
            dto.setNotifications(notificationMapper.map(
                instance.getActiveNotifications(ctx.authentication()),
                ctx.of("notifications?activeOnly")));
        }
        if (ctx.fetch("notifications")) {
            dto.setNotifications(notificationMapper.map(
                instance.getNotifications(),
                ctx.of("notifications")));
        }
        if (ctx.fetch("commands?failedOnly")) {
            dto.setCommands(commandMapper.map(
                instance.getCommands(true),
                ctx.of("commands?failedOnly")));
        }
        if (ctx.fetch("commands")) {
            dto.setCommands(commandMapper.map(
                instance.getCommands(false),
                ctx.of("commands")));
        }
        if (ctx.fetch("history")) {
            dto.setHistory(historyMapper.map(
                instance.getHistory(),
                ctx.of("history")));
        }
        if (ctx.fetch("authorizations")) {
            dto.setAuthorizations(instance.getAuthorizations(ctx.authentication())
                .stream()
                .map(e -> e.name())
                .collect(Collectors.toList()));
        }        
        if (ctx.fetch("chat")) {
            dto.setChat(chatMapper.map(
                instance.getChatMembership().orElse(null), 
                ctx.of("chat")));
        }
        if (ctx.fetch("nextTasks")) {
            dto.setNextTasks(taskMapper.map(
                instance.getNextTasks(ctx.authentication()), 
                ctx.of("nextTasks")));
        }
        return dto;
    }
}
