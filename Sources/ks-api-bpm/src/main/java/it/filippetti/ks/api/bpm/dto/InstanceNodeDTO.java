/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "InstanceNode", 
    description = ""
    + "Fetchable:"
    + "<ul><li>input"
    + "<li>output"
    + "<li>context"
    + "<li>event"
    + "<li>variables[?publicOnly]"
    + "<li>variableValues[?publicOnly]"
    + "<li>commands[?failedOnly]"
    + "<li>authorizations"
    + "<li>task</ul>"
    + "Sortable:"
    + "<ul><li>id (default)</ul>")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstanceNodeDTO {
    
    @ApiModelProperty(position = 1, 
        example = "0", 
        notes = "Instance node id")       
    private Long id;

    @ApiModelProperty(position = 2, 
        example = "block0",
        notes = "Definition node id")       
    private String nodeId;

    @ApiModelProperty(position = 3, 
        example = "Receive Task",
        notes = "Definition node name")       
    private String nodeName;

    @ApiModelProperty(position = 4, 
        example = "WorkItemNode",
        notes = "Definition node type")       
    private String nodeType;

    @ApiModelProperty(position = 5, 
        example = "con_123",
        notes = "Incoming connection")       
    private String connection;

    @ApiModelProperty(position = 6, 
        example = "2020-11-11T14:57:48.000Z",
        notes = "Instance node enter timestamp")       
    private Date enterTs;

    @ApiModelProperty(position = 7, 
        example = "2020-11-11T14:57:48.000Z",
        notes = "Instance node exit timestamp, not valued when node is active")       
    private Date exitTs;

    @ApiModelProperty(position = 8, 
        example = "19",
        notes = "Subprocess instance id started by this node, if any")       
    private Long subprocessInstanceId;
    
    @ApiModelProperty(position = 9, 
        notes = "Instance node input, in case of catch event node contains payload")       
    private Object input;

    @ApiModelProperty(position = 10, 
        notes = "Instance node output, in case of throw event node contains payload")       
    private Object output;

    @ApiModelProperty(position = 11, 
        notes = "Instance node context")       
    private InstanceContextDTO context;
 
    @ApiModelProperty(position = 12,
        notes = "Instance node event info, if any")       
    private InstanceEventDTO event;

    @ApiModelProperty(position = 13,
        notes = "Instance node variables map that associates variable name with variable metadata and value")           
    private Map<String, InstanceVariableDTO> variables;
    
    @ApiModelProperty(position = 14,
        notes = "Instance node variables map that associates variable name with variable value only")           
    private Map<String, Object> variableValues;
    
    @ApiModelProperty(position = 15,
        notes = "Instance node asyncronous command executions, if any")           
    private List<InstanceCommandDTO> commands;
    
    @ApiModelProperty(position = 16, 
        example = "[\"Complete\", \"Retry\"]",    
        notes = ""
        + "User authorized operations on this instance node.<br/>"
        + "Allowed values are:"
        + "<li>Abort"
        + "<li>Complete"
        + "<li>Retry")        
    private List<String> authorizations;
    
    @ApiModelProperty(position = 17,
        notes = "Instance node related task, if any")           
    private TaskDTO task;    

    public InstanceNodeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InstanceContextDTO getContext() {
        return context;
    }

    public void setContext(InstanceContextDTO context) {
        this.context = context;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public Date getEnterTs() {
        return enterTs;
    }

    public void setEnterTs(Date enterTs) {
        this.enterTs = enterTs;
    }

    public Date getExitTs() {
        return exitTs;
    }

    public void setExitTs(Date exitTs) {
        this.exitTs = exitTs;
    }

    public Long getSubprocessInstanceId() {
        return subprocessInstanceId;
    }

    public void setSubprocessInstanceId(Long subprocessInstanceId) {
        this.subprocessInstanceId = subprocessInstanceId;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public InstanceEventDTO getEvent() {
        return event;
    }

    public void setEvent(InstanceEventDTO event) {
        this.event = event;
    }

    public Map<String, InstanceVariableDTO> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, InstanceVariableDTO> variables) {
        this.variables = variables;
    }

    public Map<String, Object> getVariableValues() {
        return variableValues;
    }

    public void setVariableValues(Map<String, Object> variableValues) {
        this.variableValues = variableValues;
    }

    public List<InstanceCommandDTO> getCommands() {
        return commands;
    }

    public void setCommands(List<InstanceCommandDTO> commands) {
        this.commands = commands;
    }

    public List<String> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<String> authorizations) {
        this.authorizations = authorizations;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }
    
    @ApiModel("Page<InstanceNode>")
    public static class Page extends PageDTO<InstanceNodeDTO> {}     
}
