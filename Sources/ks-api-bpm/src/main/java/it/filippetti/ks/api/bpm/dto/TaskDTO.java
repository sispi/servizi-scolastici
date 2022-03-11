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
    value = "Task", 
    description = ""
    + "Fetchable:"
    + "<ul><li>instance"
    + "<li>rootInstance"
    + "<li>node"
    + "<li>input"
    + "<li>output"
    + "<li>assignments"
    + "<li>authorizations"
    + "<li>comments"
    + "<li>attachments"
    + "<li>notifications[?activeOnly]"
    + "<li>history"
    + "<li>nextTasks</ul>"
    + "Sortable:"
    + "<ul><li>startTs (default)"
    + "<li>expireTs"
    + "<li>instanceId"
    + "<li>name"
    + "<li>status</ul>")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDTO {
    
    @ApiModelProperty(position = 1,
        example = "19")  
    private Long id; 

    @ApiModelProperty(position = 2,
        example = "19", 
        notes = "Task business key")  
    private String businessKey; 
    
    @ApiModelProperty(position = 3,
        example = "Human Task", 
        notes = "Task name")       
    private String name;
    
    @ApiModelProperty(position = 4,
        example = "My Task subject", 
        notes = "Task subject")         
    private String subject;

    @ApiModelProperty(position = 5,
        example = "My task description", 
        notes = "Task description")    
    private String description;
    
    @ApiModelProperty(position = 6,
        example = "My task type", 
        notes = "Task type")         
    private String type;
    
    @ApiModelProperty(position = 7,
        example = "0", 
        notes = "Task priority")         
    private Integer priority;
    
    @ApiModelProperty(position = 8,
        example = "2020-11-11T14:57:48.000Z", 
        notes = "Task start timestamp")       
    private Date startTs;
    
    @ApiModelProperty(position = 9,
        example = "2020-11-11T14:57:48.000Z", 
        notes = "Task end timestamp, valued only after completion")       
    private Date endTs;    
    
    @ApiModelProperty(position = 10,
        example = "2020-11-11T14:57:48.000Z", 
        notes = "Task expiration timestamp, if any")           
    private Date expireTs;

    @ApiModelProperty(position = 11,
        example = "InProgress", 
        notes = ""
        + "Task status.<br/>"
        + "Allowed values are:"
        + "<li>Ready"
        + "<li>Reserved"
        + "<li>InProgress"
        + "<li>Completed"
        + "<li>Error"
        + "<li>Obsolete")           
    private String status;

    @ApiModelProperty(position = 12,
        example = "Reserved")   
    private String previousStatus;

    @ApiModelProperty(position = 13,
        notes = "Task instance")   
    private InstanceDTO instance;

    @ApiModelProperty(position = 14,
        notes = "Task root instance")   
    private InstanceDTO rootInstance;

    @ApiModelProperty(position = 15,
        notes = "Task instance node")   
    private InstanceNodeDTO node;

    @ApiModelProperty(position = 16, 
        notes = "Task input")   
    private Map<String, Object> input;

    @ApiModelProperty(position = 17,
        notes = "Task output, valued only after task completion")   
    private Map<String, Object> output;
    
    @ApiModelProperty(position = 18, 
        notes = "Task assignments")       
    private TaskAssignmentsDTO assignments;
    
    @ApiModelProperty(position = 19, 
        example = "[\"Save\", \"Complete\"]",    
        notes = ""
        + "User authorized operations on this task.<br/>"
        + "Allowed values are:"
        + "<li>Claim"
        + "<li>Save"
        + "<li>Complete"
        + "<li>Delegate"
        + "<li>Forward"
        + "<li>Release"
        + "<li>Skip"
        + "<li>Refuse") 
    private List<String> authorizations;
    
    @ApiModelProperty(position = 20, 
        notes = "Task comments")       
    private List<TaskCommentDTO> comments;
    
    @ApiModelProperty(position = 21,
        notes = "Task attachments")       
    private List<TaskAttachmentDTO> attachments;     
    
    @ApiModelProperty(position = 22,
        notes = "Task notifications")       
    private List<NotificationDTO> notifications; 
    
    @ApiModelProperty(position = 23,
        notes = "Task history")       
    private List<HistoryDTO> history;
    
    @ApiModelProperty(position = 24,
        example = "{}",            
        notes = ""
        + "Next task(s) to execute into user swimlane, if any.<br/>"
        + "Valued when task has moved forward due to a lifecycle action performed by user")       
    private List<TaskDTO> nextTasks;
    
    public TaskDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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

    public Date getExpireTs() {
        return expireTs;
    }

    public void setExpireTs(Date expireTs) {
        this.expireTs = expireTs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public InstanceDTO getInstance() {
        return instance;
    }

    public void setInstance(InstanceDTO instance) {
        this.instance = instance;
    }

    public InstanceDTO getRootInstance() {
        return rootInstance;
    }

    public void setRootInstance(InstanceDTO rootInstance) {
        this.rootInstance = rootInstance;
    }

    public InstanceNodeDTO getNode() {
        return node;
    }

    public void setNode(InstanceNodeDTO node) {
        this.node = node;
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

    public TaskAssignmentsDTO getAssignments() {
        return assignments;
    }

    public void setAssignments(TaskAssignmentsDTO assignments) {
        this.assignments = assignments;
    }

    public List<String> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<String> authorizations) {
        this.authorizations = authorizations;
    }

    public List<TaskCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<TaskCommentDTO> comments) {
        this.comments = comments;
    } 

    public List<TaskAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<TaskAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }

    public List<HistoryDTO> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryDTO> history) {
        this.history = history;
    }

    public List<TaskDTO> getNextTasks() {
        return nextTasks;
    }

    public void setNextTasks(List<TaskDTO> nextTasks) {
        this.nextTasks = nextTasks;
    }
    
    @ApiModel("Page<Task>")
    public static class Page extends PageDTO<TaskDTO> {}     
}
