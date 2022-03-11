/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dino
 */
@ApiModel("CreateInstance")
public class CreateInstanceDTO {
    
    @ApiModelProperty(position = 1, required = true, example = "1", notes = "The Proceeding id")
    @NotNull
    private Long proceedingId;
    
    @ApiModelProperty(position = 3, required = true, notes = "The form in json format")
    private String model;

    public CreateInstanceDTO() {
    }

    public Long getProceedingId() {
        return proceedingId;
    }

    public void setProceedingId(Long proceedingId) {
        this.proceedingId = proceedingId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "{"
                + "proceedingId='" + getProceedingId() + "'"
                + ", model='" + getModel() + "'"
                + "}";
    }
    
}
