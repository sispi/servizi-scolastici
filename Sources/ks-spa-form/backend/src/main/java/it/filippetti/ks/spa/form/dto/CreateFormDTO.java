/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.dto;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.spa.form.validator.Definition;
import it.filippetti.ks.spa.form.validator.NotBlank;
import it.filippetti.ks.spa.form.validator.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("CreateForm")
public class CreateFormDTO extends UpdateFormDTO {
  
    @ApiModelProperty(position = 1, required = false,
        example = "123e4567-e89b-12d3-a456-556642440000")
    @Pattern(
        regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
        message = "Invalid id format (UUID)")
    private String id;
    
    @ApiModelProperty(position = 1, required = true,
        example = "MyForm")
    @NotNull
    @NotBlank
    private String name;    

    @ApiModelProperty(position = 2, required = false)
    @NotNull
    @Schema
    private ObjectNode schema;

    @ApiModelProperty(position = 3, required = false)
    @Definition
    private ObjectNode definition;
    
    public CreateFormDTO() {
        schema = new ObjectNode(JsonNodeFactory.instance);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
