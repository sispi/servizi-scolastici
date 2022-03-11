/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("CompleteTask")
public class CompleteTaskDTO extends OperationDTO {

    @ApiModelProperty(position = 1, required = false,  
        example = "false",
        notes = "Automatically claim the task if needed") 
    @NotNull
    private Boolean autoProgress;

    @ApiModelProperty(position = 2, required = false,
        example = "true", 
        notes = "Indicates if input has to be merged or replaced") 
    @NotNull
    private Boolean mergeInput;

    @ApiModelProperty(position = 3, required = false,
        notes = "Task input") 
    private Map<String, Object> input;

    @ApiModelProperty(position = 4, required = false,
        notes = "Task output") 
    @NotNull
    private Map<String, Object> output;
    
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    Map<String, List<String>> formDataOutput;
 
    @ApiModelProperty(position = 5, required = false,
        example = "My complete message",     
        notes = "Complete message")     
    @Length(min = 1, max = 255)    
    private String message;

    public CompleteTaskDTO() {
        mergeInput = true;
        output = new HashMap<>();
        autoProgress = false;
    }

    public Boolean getAutoProgress() {
        return autoProgress;
    }

    public void setAutoProgress(Boolean autoProgress) {
        this.autoProgress = autoProgress;
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

    public Map<String, Object> getOutput() {
        return output;
    }

    public void setOutput(Map<String, Object> output) {
        this.output = output;
    }
    
    public boolean isFormDataOutput() {
        return formDataOutput != null;
    }

    public Map<String, List<String>> getFormDataOutput() {
        return formDataOutput;
    }

    public void setFormDataOutput(Map<String, List<String>> formDataOutput) {
        this.formDataOutput = formDataOutput;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
