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
@ApiModel("InstanceCommandError")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstanceCommandErrorDTO {
   
    @ApiModelProperty(position = 1,
        example = "2020-11-11T14:57:48.000Z")      
    private Date timestamp;
    
    @ApiModelProperty(position = 2,
        example = "Error message")      
    private String message;

    @ApiModelProperty(position = 3,
        example = "...")      
    private String stacktrace;

    public InstanceCommandErrorDTO() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }    
}
