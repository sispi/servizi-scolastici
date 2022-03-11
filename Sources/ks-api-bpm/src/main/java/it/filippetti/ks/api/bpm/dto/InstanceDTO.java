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
    value = "Instance", 
    description = ""
    + "Fetchable:"
    + "<ul><li>rootInstance"
    + "<li>parentInstance"
    + "<li>treeInstances"
    + "<li>input"
    + "<li>output"
    + "<li>variables[?publicOnly]"
    + "<li>variableValues[?publicOnly]"
    + "<li>nodes[?activeOnly]"
    + "<li>tasks[?activeOnly]"
    + "<li>treeTasks[?activeOnly]"
    + "<li>configuration"
    + "<li>notifications[?activeOnly]"
    + "<li>commands[?failedOnly]"
    + "<li>history"
    + "<li>authorizations"
    + "<li>chat"
    + "<li>nextTasks</ul>"
    + "Sortable:"
    + "<ul><li>startTs (default)"
    + "<li>lastActivityTs"
    + "<li>businessName"
    + "<li>status</ul>")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstanceDTO {
    
    @ApiModelProperty(position = 1, 
        example = "77")     
    private Long id;
    
    @ApiModelProperty(position = 2, 
        example = "MyProcess", 
        notes = "Process name")         
    private String name;
    
    @ApiModelProperty(position = 3, 
        example = "1.0", 
        notes = "Process version")         
    private String version;
    
    @ApiModelProperty(position = 4, 
        example = "MyProcess1.0")         
    private String processId;
    
    @ApiModelProperty(position = 5, 
        example = "Proceedings No.12345")     
    private String businessName;
    
    @ApiModelProperty(position = 6, 
        example = "Approved", 
        notes = "Instance business state, if any")         
    private String businessState;
    
    @ApiModelProperty(position = 7, 
        example = "mario.rossi")         
    private String creatorUserId;
    
    @ApiModelProperty(position = 8, 
        example = "luca.neri")         
    private String initiatorUserId;
    
    @ApiModelProperty(position = 9, 
        example = "2020-11-11T14:57:48.000Z")         
    private Date startTs;
    
    @ApiModelProperty(position = 10, 
        example = "2020-11-11T14:57:48.000Z", 
        notes = "End timestamp, valued only when instance terminates")         
    private Date endTs;
    
    @ApiModelProperty(position = 11, 
        example = "2020-11-11T14:57:48.000Z")         
    private Date lastActivityTs;
    
    @ApiModelProperty(position = 12, 
        example = "2020-11-11T14:57:48.000Z", 
        notes = "Archiviation timestamp, valued only when instance terminates")         
    private Date archiveTs;

    @ApiModelProperty(position = 13, 
        example = "1", 
        notes = ""
        + "Instance status.<br/>"
        + "Allowed values are:"
        + "<li>1 Active"
        + "<li>2 Completed"
        + "<li>3 Aborted")         
    private Integer status;
    
    @ApiModelProperty(position = 14, 
        example = "true", 
        notes = "Indicates if instance is root")             
    private Boolean root;
    
    @ApiModelProperty(position = 15,
        example = "{}",            
        notes = "Root instance, same as instance if root")         
    private InstanceDTO rootInstance;
    
    @ApiModelProperty(position = 16, 
        example = "{}",            
        notes = "Parent instance, valued only when not root")         
    private InstanceDTO parentInstance;

    @ApiModelProperty(position = 17, 
        notes = "Instance input")         
    private Map<String, Object> input;

    @ApiModelProperty(position = 18, 
        notes = "Instance output, valued only when instance terminates")         
    private Map<String, Object> output;
    
    @ApiModelProperty(position = 19, 
        example = "[]",            
        notes = "Tree instances, excluded the instance itself")         
    private List<InstanceDTO> treeInstances;    
    
    @ApiModelProperty(position = 20, 
        notes = "Instance variables map that associates variable name with variable metadata and value")         
    private Map<String, InstanceVariableDTO> variables;
 
    @ApiModelProperty(position = 21,
        notes = "Instance variables map that associates variable name with variable value only")         
    private Map<String, Object> variableValues;
    
    @ApiModelProperty(position = 22, 
        notes = "Instance nodes")         
    private List<InstanceNodeDTO> nodes;
    
    @ApiModelProperty(position = 23, 
        notes = "Instance tasks")         
    private List<TaskDTO> tasks;

    @ApiModelProperty(position = 24, 
        notes = "Instance tree tasks")         
    private List<TaskDTO> treeTasks;
    
    @ApiModelProperty(position = 25, 
        notes = "Configuration in use by instance")         
    private ConfigurationDTO configuration;
    
    @ApiModelProperty(position = 26, 
        notes = "Instance notifications")        
    private List<NotificationDTO> notifications;

    @ApiModelProperty(position = 27, 
        notes = "Instance commands")            
    private List<InstanceCommandDTO> commands;
 
    @ApiModelProperty(position = 28, 
        notes = "Instance history")            
    private List<HistoryDTO> history;
    
    @ApiModelProperty(position = 29, 
        example = "[\"Abort\", \"Chat\"]",    
        notes = ""
        + "User authorized operations on this instance.<br/>"
        + "Allowed values are:"
        + "<li>Abort"
        + "<li>Retry"
        + "<li>Clone"
        + "<li>Chat") 
    private List<String> authorizations;
    
    @ApiModelProperty(position = 30, 
        notes = "Instance chat")         
    private ChatDTO chat;

    @ApiModelProperty(position = 31,
        example = "{}",            
        notes = ""
        + "Next task(s) to execute into user swimlane, if any.<br/>"
        + "Valued when instance starts or has moved forward due to a lifecycle action performed by user")       
    private List<TaskDTO> nextTasks;
    
    public InstanceDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessState() {
        return businessState;
    }

    public void setBusinessState(String businessState) {
        this.businessState = businessState;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getInitiatorUserId() {
        return initiatorUserId;
    }

    public void setInitiatorUserId(String initiatorUserId) {
        this.initiatorUserId = initiatorUserId;
    }

    public Date getStartTs() {
        return startTs;
    }

    public void setStartTs(Date startTs) {
        this.startTs = startTs;
    }

    public Date getEndTs() {
        return endTs;
    }

    public void setEndTs(Date endTs) {
        this.endTs = endTs;
    }

    public Date getLastActivityTs() {
        return lastActivityTs;
    }

    public void setLastActivityTs(Date lastActivityTs) {
        this.lastActivityTs = lastActivityTs;
    }

    public Date getArchiveTs() {
        return archiveTs;
    }

    public void setArchiveTs(Date archiveTs) {
        this.archiveTs = archiveTs;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getRoot() {
        return root;
    }

    public void setRoot(Boolean root) {
        this.root = root;
    }

    public InstanceDTO getRootInstance() {
        return rootInstance;
    }

    public void setRootInstance(InstanceDTO rootInstance) {
        this.rootInstance = rootInstance;
    }

    public InstanceDTO getParentInstance() {
        return parentInstance;
    }

    public void setParentInstance(InstanceDTO parentInstance) {
        this.parentInstance = parentInstance;
    }

    public List<InstanceDTO> getTreeInstances() {
        return treeInstances;
    }

    public void setTreeInstances(List<InstanceDTO> treeInstances) {
        this.treeInstances = treeInstances;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }

    public Map<String, Object> getOutput() {
        return output;
    }

    public void setOutput(Map<String, Object> output) {
        this.output = output;
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

    public List<InstanceNodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<InstanceNodeDTO> nodes) {
        this.nodes = nodes;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public List<TaskDTO> getTreeTasks() {
        return treeTasks;
    }

    public void setTreeTasks(List<TaskDTO> treeTasks) {
        this.treeTasks = treeTasks;
    }

    public ConfigurationDTO getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ConfigurationDTO configuration) {
        this.configuration = configuration;
    }

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }

    public List<InstanceCommandDTO> getCommands() {
        return commands;
    }

    public void setCommands(List<InstanceCommandDTO> commands) {
        this.commands = commands;
    }

    public List<HistoryDTO> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryDTO> history) {
        this.history = history;
    }

    public List<String> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<String> authorizations) {
        this.authorizations = authorizations;
    }

    public ChatDTO getChat() {
        return chat;
    }

    public void setChat(ChatDTO chat) {
        this.chat = chat;
    }

    public List<TaskDTO> getNextTasks() {
        return nextTasks;
    }

    public void setNextTasks(List<TaskDTO> nextTasks) {
        this.nextTasks = nextTasks;
    }

    @ApiModel("Page<Instance>")
    public static class Page extends PageDTO<InstanceDTO> {}     
}
