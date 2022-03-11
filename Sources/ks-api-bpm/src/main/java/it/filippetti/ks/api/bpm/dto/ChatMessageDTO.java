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

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "ChatMessage", 
    description = ""
    + "Fetchable:"
    + "<ul><li>text</ul>"
    + "Sortable:"
    + "<ul><li>sendTs (default)</ul>")    
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageDTO {
    
    @ApiModelProperty(position = 1, 
        example = "1")     
    private Long id;
    
    @ApiModelProperty(position = 2, 
        example = "Proceedings No. 12345", 
        notes = "Business name of the sender instance")     
    private String senderInstanceName;    
    
    @ApiModelProperty(position = 3, 
        example = "mario.rossi")     
    private String senderUserId;  
    
    @ApiModelProperty(position = 4, 
        example = "Mario Rossi")     
    private String senderDisplayName;  
    
    @ApiModelProperty(position = 5, 
        example = "2020-11-11T14:57:48.000Z")     
    private Date sendTs;    
    
    @ApiModelProperty(position = 6, 
        example = "Bla, bla, bla")    
    private String text;       

    public ChatMessageDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderInstanceName() {
        return senderInstanceName;
    }

    public void setSenderInstanceName(String senderInstanceName) {
        this.senderInstanceName = senderInstanceName;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getSenderDisplayName() {
        return senderDisplayName;
    }

    public void setSenderDisplayName(String senderDisplayName) {
        this.senderDisplayName = senderDisplayName;
    }

    public Date getSendTs() {
        return sendTs;
    }

    public void setSendTs(Date sendTs) {
        this.sendTs = sendTs;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    @ApiModel("Page<ChatMessage>")
    public static class Page extends PageDTO<ChatMessageDTO> {}    
}
