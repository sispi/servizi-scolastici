/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.api.bpm.validator.OneOf;
import javax.validation.constraints.NotNull;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("DeleteDeployment")
public class DeleteDeploymentDTO {
    
    @ApiModelProperty(position = 1, required = false, 
        example = "check",
        notes = ""
        + "Indicates how to manage any active instance of this deployment.<br/>"
        + "Allowed values are: "
        + "<li>check"
        + "<li>abort"
        + "<li>ignore")       
    @NotNull
    @OneOf({"check", "abort", "ignore"})    
    private String activeInstancesPolicy;

    public DeleteDeploymentDTO() {
        activeInstancesPolicy = "check";
    }

    public String getActiveInstancesPolicy() {
        return activeInstancesPolicy;
    }

    public void setActiveInstancesPolicy(String activeInstancesPolicy) {
        this.activeInstancesPolicy = activeInstancesPolicy;
    }
}
