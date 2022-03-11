/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("RefuseTask")
public class RefuseTaskDTO extends OperationDTO {
    
    @ApiModelProperty(position = 1, 
        example = "My refuse reason",    
        notes = "Refuse reason")      
    @Length(min = 1, max = 255)    
    private String message;

    public RefuseTaskDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
