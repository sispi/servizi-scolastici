/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.spa.form.validator.NotBlank;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("CreateFormView")
public class CreateFormViewDTO {
    
    @ApiModelProperty(position = 1, required = true,
        example = "vue",
        notes = ""
        + "Template type")
    @NotNull
    @NotBlank
    private String type;

    @ApiModelProperty(position = 2, required = false,
        notes = ""
        + "Template options.<br/>"
        + "Get template content for documentation of specific template type.")
    @NotNull
    private Map<String, Object> options;

    @ApiModelProperty(position = 3, required = false,
        notes = "Form model")      
    @NotNull
    private Map<String, Object> model;    
    
    public CreateFormViewDTO() {
        super();
        type = "vue";
        options = new HashMap<>();
        model = new HashMap<>();
    }

    public CreateFormViewDTO(String type, Map<String, Object> options) {
        this();
        this.type = type;
        this.options = options;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
