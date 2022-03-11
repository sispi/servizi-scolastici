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
@ApiModel("NotificationRecipient")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationRecipientDTO {

    @ApiModelProperty(position = 1, 
        example = "mario.rossi")            
    private String userId;
    
    @ApiModelProperty(position = 1, 
        example = "Mario Rossi")            
    private String userDisplayName;
    
    @ApiModelProperty(position = 1, 
        example = "2020-11-11T14:57:48.000Z", 
        notes = "Indicates if (when not null) and when notification was read by recipient user")            
    private Date readTs;   

    public NotificationRecipientDTO() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public Date getReadTs() {
        return readTs;
    }

    public void setReadTs(Date readTs) {
        this.readTs = readTs;
    }
}
