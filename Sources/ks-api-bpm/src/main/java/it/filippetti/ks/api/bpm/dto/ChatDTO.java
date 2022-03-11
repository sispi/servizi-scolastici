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
    value = "Chat", 
    description = ""
    + "Fetchable:"
    + "<li>members"
    + "<li>messages") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatDTO {
    
    @ApiModelProperty(position = 1, 
        example = "19")    
    private String id;
    
    @ApiModelProperty(position = 2, 
        example = "false", 
        notes = "Read status of the instance chat")    
    private Boolean read;
    
    @ApiModelProperty(position = 3, 
        example = "2020-11-10T09:59:42.000Z", 
        notes = "Last time instance chat was read")      
    private Date lastReadTs;
    
    @ApiModelProperty(position = 4, 
        example = "2020-11-11T14:57:48.000Z", 
        notes = "Last time a message was published to the instance chat")          
    private Date lastSendTs;
    
    @ApiModelProperty(position = 5, 
        notes = "Chat local members")              
    private List<ChatMemberDTO> members;
    
    @ApiModelProperty(position = 5,
        notes = "Chat messages")              
    private List<ChatMessageDTO> messages;
    
    public ChatDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Date getLastReadTs() {
        return lastReadTs;
    }

    public void setLastReadTs(Date lastReadTs) {
        this.lastReadTs = lastReadTs;
    }

    public Date getLastSendTs() {
        return lastSendTs;
    }

    public void setLastSendTs(Date lastSendTs) {
        this.lastSendTs = lastSendTs;
    }



    public List<ChatMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<ChatMemberDTO> members) {
        this.members = members;
    }

    public List<ChatMessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessageDTO> messages) {
        this.messages = messages;
    }
}
