/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.api.bpm.validator.ListUnique;
import it.filippetti.ks.api.bpm.validator.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("CreateNotification")
public class CreateNotificationDTO extends OperationDTO {

    @ApiModelProperty(position = 1, required = false,  
        example = "true", 
        notes = "Send notification also via email")         
    @NotNull
    private Boolean email;

    @ApiModelProperty(position = 2, required = true, 
        example = "Hello ${name}", 
        notes = "Subject mvel template or plain text/html")             
    @NotNull
    @NotBlank
    private String subject;    

    @ApiModelProperty(position = 3, required = false,
        example = "Bla, bla, bla", 
        notes = "Body mvel template or plain text/html")                 
    @NotBlank
    private String body; 

    @ApiModelProperty(position = 4, required = false,  
        example = "false")                 
    @NotNull
    private Boolean priority;

    @ApiModelProperty(position = 5, required = false, 
        example = "2020-11-11T14:57:48.000Z",
        notes = "Expiration timestamp, if not valued never expires")                     
    private Date expireTs;

    @ApiModelProperty(position = 6, required = false,
        example = "[\"document\",\"docNum:123456789\"]",
        notes = "A set of tags to classify notification")                         
    @NotNull
    @ListUnique
    private List<@NotNull @Length(min = 1, max = 255) String> tags;

    @ApiModelProperty(position = 7, required = false,
        example = "admin@keysuite.it",
        notes = ""
            + "The notification email sender.<br/>"
            + "Only preconfigured system emails are allowed, if not valued a default is applied")                         
    @NotBlank
    private String from;
    
    @ApiModelProperty(position = 8, required = true, 
        example = "[\"Administrators\"]",
        notes = ""
        + "A set of recipients expressed as userId, groupId or email address.<br/>"
        + "Same applies to cc and bcc recipients")                             
    @NotNull
    @ListUnique
    @NotEmpty
    private List<@NotNull @NotBlank String> toRecipients;

    @ApiModelProperty(position = 9, required = false,
        example = "[]")    
    @NotNull
    @ListUnique
    private List<@NotNull @NotBlank String> ccRecipients;
    
    @ApiModelProperty(position = 10, required = false,
        example = "[]")    
    @NotNull
    @ListUnique
    private List<@NotNull @NotBlank String> bccRecipients;    
    
    @ApiModelProperty(position = 11, required = false,  
        example = "[\"file://storage/file.txt\"]", 
        notes = ""
        + "Attachments expressed as url (file or http(s) protocol) or "
        + "passing directly text content")        
    @NotNull
    private List<@NotNull @NotBlank String> attachments;    

    @ApiModelProperty(position = 12, required = false,  
        example = "{\"name\": \"Mario Rossi\"}",
        notes = "Context variables map for subject/body mvel templates interpolation")            
    @NotNull
    private Map<@NotNull @NotBlank String, @NotNull Object> contextVariables;

    public CreateNotificationDTO() {
        this.email = true;
        this.priority = false;
        this.tags = new ArrayList<>();
        this.toRecipients = new ArrayList<>();
        this.ccRecipients = new ArrayList<>();
        this.bccRecipients = new ArrayList<>();
        this.attachments = new ArrayList<>();
        this.contextVariables = new HashMap<>();
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
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

    public String getFrom() {
        return from;
    }

    public List<String> getToRecipients() {
        return toRecipients;
    }

    public void setToRecipients(List<String> toRecipients) {
        this.toRecipients = toRecipients;
    }

    public List<String> getCcRecipients() {
        return ccRecipients;
    }

    public void setCcRecipients(List<String> ccRecipients) {
        this.ccRecipients = ccRecipients;
    }

    public List<String> getBccRecipients() {
        return bccRecipients;
    }

    public void setBccRecipients(List<String> bccRecipients) {
        this.bccRecipients = bccRecipients;
    }



    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public Map<String, Object> getContextVariables() {
        return contextVariables;
    }

    public void setContextVariables(Map<String, Object> contextVariables) {
        this.contextVariables = contextVariables;
    }
}
