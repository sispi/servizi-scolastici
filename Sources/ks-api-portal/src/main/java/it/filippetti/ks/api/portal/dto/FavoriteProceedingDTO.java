/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author dino
 */
@ApiModel(value = "FavoriteProceeding") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteProceedingDTO {
    
    @ApiModelProperty(position = 1, example = "0") 
    private Long id;
    
    @ApiModelProperty(position = 2, notes = "The Proceeding") 
    private ProceedingDTO proceeding;

    public FavoriteProceedingDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProceedingDTO getProceeding() {
        return proceeding;
    }

    public void setProceeding(ProceedingDTO proceeding) {
        this.proceeding = proceeding;
    }
    
    @Override
    public String toString() {
        return "{"
                + "id='" + getId()+ "'"
                + ", proceeding='" + getProceeding() + "'"
                + "}";
    }
    
}
