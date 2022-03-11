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
@ApiModel("TaskComment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskCommentDTO {
 
    @ApiModelProperty(position = 1,
        example = "1")        
    private Long id;
    
    @ApiModelProperty(position = 2,
        example = "Bla, bla, bla")        
    private String text;

    @ApiModelProperty(position = 3,
        example = "2020-11-11T14:57:48.000Z")        
    private Date timestamp;

    @ApiModelProperty(position = 4,
        example = "mario.rossi")        
    private String userId;

    public TaskCommentDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }    
}
