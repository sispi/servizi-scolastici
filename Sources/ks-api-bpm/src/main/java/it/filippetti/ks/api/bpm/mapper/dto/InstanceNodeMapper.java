/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.InstanceNodeDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.InstanceNode;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class InstanceNodeMapper extends ContextMapper<InstanceNode, InstanceNodeDTO> {

    @Autowired
    private InstanceContextMapper contextMapper;

    @Autowired
    private InstanceEventMapper eventMapper;

    @Autowired
    private InstanceVariablesMapper variablesMapper;

    @Autowired
    private InstanceVariableValuesMapper variableValuesMapper;
    
    @Autowired
    private InstanceCommandMapper commandMapper;

    @Autowired
    private TaskMapper taskMapper;
    
    @Override
    protected InstanceNodeDTO doMapping(InstanceNode node, MappingContext ctx) throws Exception {
        
        InstanceNodeDTO dto = new InstanceNodeDTO();
        
        dto.setId(node.getNodeInstanceId());
        dto.setNodeId(node.getNodeId());
        dto.setNodeName(node.getNodeName());
        dto.setNodeType(node.getNodeType());
        dto.setConnection(node.getConnection());
        dto.setEnterTs(node.getEnterTs());
        dto.setExitTs(node.getExitTs());
        dto.setSubprocessInstanceId(node.getSubprocessInstanceId());
        if (ctx.fetch("input")) {
            dto.setInput(node.getIO().getInput().asObject());
        }
        if (ctx.fetch("output")) {
            dto.setOutput(node.getIO().getOutput().asObject());
        }
        if (ctx.fetch("context")) {
            dto.setContext(contextMapper.map(node.getContext()));
        }
        if (ctx.fetch("event")) {
            dto.setEvent(eventMapper.map(node.getEvent()));
        }
        if (ctx.fetch("variables?publicOnly")) {
            dto.setVariables(variablesMapper.map(
                node.getVariables(true),
                ctx.of("variables?publicOnly")));
        }
        if (ctx.fetch("variables")) {
            dto.setVariables(variablesMapper.map(
                node.getVariables(false),
                ctx.of("variables")));
        }
        if (ctx.fetch("variableValues?publicOnly")) {
            dto.setVariableValues(variableValuesMapper.map(
                node.getVariables(true), 
                ctx.of("variableValues?publicOnly")));
        }
        if (ctx.fetch("variableValues")) {
            dto.setVariableValues(variableValuesMapper.map(
                node.getVariables(false), 
                ctx.of("variableValues")));
        }                
        if (ctx.fetch("commands?failedOnly")) {
            dto.setCommands(commandMapper.map(
                node.getCommands(true),
                ctx.of("commands?failedOnly")));
        }
        if (ctx.fetch("commands")) {
            dto.setCommands(commandMapper.map(
                node.getCommands(false),
                ctx.of("commands")));
        }
        if (ctx.fetch("authorizations")) {
            dto.setAuthorizations(node.getAuthorizations(ctx.authentication())
                .stream()
                .map(e -> e.name())
                .collect(Collectors.toList()));
        }         
        if (ctx.fetch("task")) {
            dto.setTask(taskMapper.map(
                node.getTask(),
                ctx.of("task")));
        }
        return dto;
    }    
}
