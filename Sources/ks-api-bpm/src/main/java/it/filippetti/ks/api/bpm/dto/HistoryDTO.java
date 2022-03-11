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
    value = "History", 
    description = ""
    + "Fetchable:"
    + "<ul><li>task</ul>")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryDTO {
 
    @ApiModelProperty(position = 1, 
        example = "2020-11-11T14:57:48.000Z", 
        notes = "Operation timestamp")     
    private Date timestamp;
   
    @ApiModelProperty(position = 2, 
        example = "Forward", 
        notes = "Operation action")     
    private String action;

    @ApiModelProperty(position = 3, 
        example = "mario.rossi", 
        notes = "Operation user")     
    private String userId;

    @ApiModelProperty(position = 4, 
        example = "luca.neri", 
        notes = "Operation target user, if any")     
    private String targetIdentity;

    @ApiModelProperty(position = 5, 
        example = "My message", 
        notes = "Operation message, if any")     
    private String message;

    @ApiModelProperty(position = 6, 
        notes = "Operation related task, if any")     
    private TaskDTO task;    

    public HistoryDTO() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTargetIdentity() {
        return targetIdentity;
    }

    public void setTargetIdentity(String targetIdentity) {
        this.targetIdentity = targetIdentity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }
    
    @ApiModel("Page<History>")
    public static class Page extends PageDTO<HistoryDTO> {}       
}
