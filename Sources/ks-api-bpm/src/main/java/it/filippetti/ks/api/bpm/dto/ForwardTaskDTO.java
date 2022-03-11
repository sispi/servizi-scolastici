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
@ApiModel("ForwardTask")
public class ForwardTaskDTO extends OperationDTO {
    
    @ApiModelProperty(position = 1, required = true, 
        example = "Administrators", 
        notes = "Identity to forward activity to, expressed as userId or groupId")       
    @NotNull
    @NotBlank
    private String targetIdentity;

    @ApiModelProperty(position = 2, required = false,
        example = "My forward message",            
        notes = "Forward message")       
    @Length(min = 1, max = 255)    
    private String message;
    
    public ForwardTaskDTO() {
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
}
