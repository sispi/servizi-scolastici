/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.api.bpm.validator.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("DelegateTask")
public class DelegateTaskDTO extends OperationDTO {
    
    @ApiModelProperty(position = 1, required = true, 
        example = "mario.rossi", 
        notes = "userId to forward activity to")              
    @NotNull
    @NotBlank
    private String targetUserId;

    @ApiModelProperty(position = 2, required = false, 
        example = "My delegate reason",    
        notes = "Delegate reason")     
    @Length(min = 1, max = 255)  
    private String message;
    
    public DelegateTaskDTO() {
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
