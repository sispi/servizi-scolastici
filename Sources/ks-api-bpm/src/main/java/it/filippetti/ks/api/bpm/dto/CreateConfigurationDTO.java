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
@ApiModel("CreateConfiguration")
public class CreateConfigurationDTO extends UpdateConfigurationDTO {

    @ApiModelProperty(position = 1, required = false,  
        example = "default",         
        notes = "Configuration profile")     
    @NotNull
    @NotBlank
    private String profile;
    
    @ApiModelProperty(position = 2, required = false,  
        example = "false",         
        notes = "Force configuration override if already exists")     
    @NotNull
    private Boolean override;
    
    @ApiModelProperty(position = 3, required = false,  
        example = "false",         
        notes = "Merge configuration settings with previous version, if any")     
    @NotNull    
    private Boolean merge;
    
    @ApiModelProperty(position = 4, required = false,  
        example = "true",         
        notes = ""
        + "Activate or deactivate configuration.<br/>"
        + "If not valued default to true or, "
        + "if configuration already exists, current value is preserved")
    private Boolean activate;
 
    @ApiModelProperty(position = 5, required = false,  
        example = "false",
        notes = ""
            + "In case of activation, performs deactivation of all previous "
            + "versions of this configuration")        
    @NotNull        
    private Boolean deactivatePreviousVersions;

    public CreateConfigurationDTO() {
        super();
        profile = "default";
        override = false;
        merge = false;
        deactivatePreviousVersions = false;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Boolean getOverride() {
        return override;
    }

    public void setOverride(Boolean override) {
        this.override = override;
    }

    public Boolean getMerge() {
        return merge;
    }

    public void setMerge(Boolean merge) {
        this.merge = merge;
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
