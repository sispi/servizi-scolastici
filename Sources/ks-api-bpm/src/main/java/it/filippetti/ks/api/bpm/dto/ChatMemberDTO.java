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
@ApiModel("ChatMember")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMemberDTO {

    @ApiModelProperty(position = 1, 
        example = "1")              
    private Long instanceId;
    
    @ApiModelProperty(position = 2, 
        example = "true", 
        notes = "Read status of the instance chat for this member")              
    private Boolean read;
    
    @ApiModelProperty(position = 3, 
        example = "2020-11-11T14:57:48.000Z", 
        notes = "Last time instance chat was read by this member")              
    private Date lastReadTs;
    
    public ChatMemberDTO() {
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
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
}
