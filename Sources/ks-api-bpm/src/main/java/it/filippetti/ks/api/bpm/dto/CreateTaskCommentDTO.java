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

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("CreateTaskComment")
public class CreateTaskCommentDTO {
    
    @ApiModelProperty(position = 1, required = true, 
        example = "Bla, bla, bla")             
    @NotNull
    @NotBlank
    private String text;

    public CreateTaskCommentDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
