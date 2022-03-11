/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.api.payment.validator.NotBlank;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("CreateExample")
public class CreateExampleDTO extends OperationDTO {
  
    @ApiModelProperty(position = 1, required = true,
        example = "foo", 
        notes = "The name")
    @NotNull
    @NotBlank
    private String name;    

    @ApiModelProperty(position = 2, required = false,
        example = "[\"bar\",\"baz\"]", 
        notes = "The child names")
    @NotNull
    private List<@NotNull @NotBlank String> children;

    public CreateExampleDTO() {
        children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }
}
