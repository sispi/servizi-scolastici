/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.api.bpm.validator.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("StartInstance")
public class StartInstanceDTO extends OperationDTO {
    
    @ApiModelProperty(position = 1, required = true,
        example = "MyProcess1.0")     
    @NotNull
    @NotBlank
    private String processId;

    @ApiModelProperty(position = 2, required = false,
        notes = "Configuration profile",    
        example = "default")     
    @NotNull
    @NotBlank
    private String profile;

    @ApiModelProperty(position = 3, required = false,    
        notes = "Instance input")        
    @NotNull
    private Map<String, Object> input;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    Map<String, List<String>> formDataInput;
    
    public StartInstanceDTO() {
        profile = "default";
        input = new HashMap<>();
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }

    public boolean isFormDataInput() {
        return formDataInput != null;
    }
    
    public Map<String, List<String>> getFormDataInput() {
        return formDataInput;
    }

    public void setFormDataInput(Map<String, List<String>> formDataInput) {
        this.formDataInput = formDataInput;
    }
}
