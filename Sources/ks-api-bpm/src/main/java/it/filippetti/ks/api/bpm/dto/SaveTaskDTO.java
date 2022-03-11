/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("SaveTask")
public class SaveTaskDTO extends OperationDTO {
    
    @ApiModelProperty(position = 1, required = false,
        example = "true", 
        notes = "Indicates if input has to be merged or replaced")     
    @NotNull
    private Boolean mergeInput;
    
    @ApiModelProperty(position = 2, required = false,
        notes = "Task input")     
    @NotNull
    private Map<String, Object> input;

    @ApiModelProperty(position = 3, required = false,
        example = "My save message",     
        notes = "Save message")     
    @Length(min = 1, max = 255)    
    private String message;
    
    public SaveTaskDTO() {
        input = new HashMap<>();
        mergeInput = true;
    }

    public Boolean getMergeInput() {
        return mergeInput;
    }

    public void setMergeInput(Boolean mergeInput) {
        this.mergeInput = mergeInput;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
