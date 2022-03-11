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
@ApiModel("CreateBookmark")
public class CreateBookmarkDTO {
  
    @ApiModelProperty(position = 1, required = false,
        example = "My favourite instance", 
        notes = "Bookmark description, if not valued instance business name is applied") 
    @Length(min = 1, max = 255)
    private String description;

    public CreateBookmarkDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }    
}
