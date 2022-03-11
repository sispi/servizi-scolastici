/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.spa.form.validator.Definition;
import it.filippetti.ks.spa.form.validator.NotBlank;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("UpdateForm")
public class UpdateFormDTO extends OperationDTO {
  
    @ApiModelProperty(position = 1, required = false,
        example = "MyForm")
    @NotBlank
    private String name;    

    @ApiModelProperty(position = 2, required = false)
    private ObjectNode schema;
    
    @ApiModelProperty(position = 3, required = false)
    @Definition
    private ObjectNode definition;
    
    public UpdateFormDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectNode getSchema() {
        return schema;
    }

    public void setSchema(ObjectNode schema) {
        this.schema = schema;
    }

    public ObjectNode getDefinition() {
        return definition;
    }

    public void setDefinition(ObjectNode definition) {
        this.definition = definition;
    }
}
