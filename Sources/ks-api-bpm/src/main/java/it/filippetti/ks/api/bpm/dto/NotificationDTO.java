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

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "Notification", 
    description = ""
    + "Fetchable:"
    + "<ul><li>body"
    + "<li>instance"
    + "<li>task"
    + "<li>tags"
    + "<li>recipients"
    + "<li>attachments</ul>"
    + "Sortable:"
    + "<ul><li>priority (default)"
    + "<li>notifyTs (default)"
    + "<li>expireTs</ul>") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO {
    
    @ApiModelProperty(position = 1, 
        example = "77")        
    private Long id;  

    @ApiModelProperty(position = 2, 
        example = "My subject")        
    private String subject;    

    @ApiModelProperty(position = 3,
        example = "Bla, bla, bla")        
    private String body; 

    @ApiModelProperty(position = 4, 
        example = "false")        
    private Boolean priority;

    @ApiModelProperty(position = 5, 
        example = "2020-11-11T14:57:48.000Z")        
    private Date notifyTs;

    @ApiModelProperty(position = 6, 
        example = "2020-11-11T14:57:48.000Z",
        notes = "Expiration timestamp, if any")        
    private Date expireTs;

    @ApiModelProperty(position = 7, 
        example = "[\"document\",\"docNum:123456789\"]",    
        notes = "A set of tags that classify notification")        
    private List<String> tags;

    @ApiModelProperty(position = 8, 
        notes = "Notification related instance, if any")          
    private InstanceDTO instance;

    @ApiModelProperty(position = 9, 
        notes = "Notification related task, if any")          
    private TaskDTO task;

    @ApiModelProperty(position = 10, 
        notes = "Notification recipients")        
    private List<NotificationRecipientDTO> recipients;

    @ApiModelProperty(position = 11, 
        notes = "Notification attachments")        
    private List<NotificationAttachmentDTO> attachments;
    
    public NotificationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }

    public Date getNotifyTs() {
        return notifyTs;
    }

    public void setNotifyTs(Date notifyTs) {
        this.notifyTs = notifyTs;
    }

    public Date getExpireTs() {
        return expireTs;
    }

    public void setExpireTs(Date expireTs) {
        this.expireTs = expireTs;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public InstanceDTO getInstance() {
        return instance;
    }

    public void setInstance(InstanceDTO instance) {
        this.instance = instance;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public List<NotificationRecipientDTO> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<NotificationRecipientDTO> recipients) {
        this.recipients = recipients;
    }

    public List<NotificationAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<NotificationAttachmentDTO> attachments) {
        this.attachments = attachments;
    }
    
    @ApiModel("Page<Notification>")
    public static class Page extends PageDTO<NotificationDTO> {}         
}
