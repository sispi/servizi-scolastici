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
@ApiModel("ActivateConfiguration")
public class ActivateConfigurationDTO {

    @ApiModelProperty(position = 1, required = true,
        example = "true",
        notes = "Activate or deactivate configuration")     
    @NotNull
    private Boolean activate;

    @ApiModelProperty(position = 2, required = false,
        example = "false",
        notes = ""
            + "In case of activation, performs deactivation of all previous "
            + "versions of this configuration")
    @NotNull
    private Boolean deactivatePreviousVersions;

    public ActivateConfigurationDTO() {
        deactivatePreviousVersions = false;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public Boolean getDeactivatePreviousVersions() {
        return deactivatePreviousVersions;
    }

    public void setDeactivatePreviousVersions(Boolean deactivatePreviousVersions) {
        this.deactivatePreviousVersions = deactivatePreviousVersions;
    }
}
