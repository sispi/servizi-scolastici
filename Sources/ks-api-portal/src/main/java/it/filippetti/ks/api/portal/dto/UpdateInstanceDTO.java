/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dino
 */
@ApiModel(value = "UpdateInstance") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateInstanceDTO {
    
    @NotNull
    @ApiModelProperty(position = 1, example = "1")
    private Long id;
    
    @ApiModelProperty(position = 2, required = true, notes = "The form in json format")
    private String model;
    
    @ApiModelProperty(position = 4, required = true, example = "true")
    private Boolean send;
    
    @ApiModelProperty(position = 5, example = "1")
    private Object input;
    
    public UpdateInstanceDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public Boolean getSend() {
        return send;
    }

    public void setSend(Boolean send) {
        this.send = send;
    }
    
    
    @Override
    public String toString() {
        return "{"
            + "id='" + getId() + "'"
            + ", createBPMProcess='" + getSend()+ "'"
            + ", model='" + getModel() + "'"
            + "}";
    }
}
