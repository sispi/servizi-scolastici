/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("ShareDeployment")
public class ShareDeploymentDTO {
    
    @ApiModelProperty(position = 1, required = true,
        example = "true",
        notes = "Share or unshare deployment")     
    @NotNull
    private Boolean share;
    
    public ShareDeploymentDTO() {
    }

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }
}
