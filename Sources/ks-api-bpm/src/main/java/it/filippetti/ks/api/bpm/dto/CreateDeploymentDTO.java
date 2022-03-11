/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.api.bpm.validator.NotBlank;
import it.filippetti.ks.api.bpm.validator.OneOf;
import java.util.ArrayList;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("CreateDeployment")
public class CreateDeploymentDTO {

    @ApiModelProperty(position = 1, required = false,  
        example = "false",         
        notes = "Force deployment override if already exists")     
    @NotNull
    private Boolean override;

    @ApiModelProperty(position = 2, required = false,  
        example = "true",
        notes = ""
        + "Activate or deactivate deployment.<br/>"
        + "If not valued default to true or, "
        + "if deployment already exists, current value is preserved")            
    private Boolean activate;

    @ApiModelProperty(position = 3, required = false,  
        example = "false",
        notes = ""
        + "Share or unshare deployment.<br/>"
        + "If not valued default to false or, "
        + "if deployment already exists, current value is preserved")                    
    private Boolean share;

    @ApiModelProperty(position = 4, required = false, 
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

    @ApiModelProperty(position = 5, required = false, 
        notes = ""
        + "Deployment configuration, to contestually configure deployment at deploy "
        + "time")             
    @Valid    
    private CreateConfigurationDTO configuration;

    public CreateDeploymentDTO() {
        override = false;
        activeInstancesPolicy = "check";
    }

    public Boolean getOverride() {
        return override;
    }

    public void setOverride(Boolean override) {
        this.override = override;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public String getActiveInstancesPolicy() {
        return activeInstancesPolicy;
    }

    public void setActiveInstancesPolicy(String activeInstancesPolicy) {
        this.activeInstancesPolicy = activeInstancesPolicy;
    }

    public CreateConfigurationDTO getConfiguration() {
        return configuration;
    }

    public void setConfiguration(CreateConfigurationDTO configuration) {
        this.configuration = configuration;
    }
    
    /**
     * 
     */
    public static class BatchItem {
       
        @NotNull
        @NotBlank
        private String entry;

        @NotNull
        @Valid
        private CreateDeploymentDTO params;
        
        public BatchItem() {
            super();
        }

        public String getEntry() {
            return entry;
        }

        public CreateDeploymentDTO getParams() {
            return params;
        }
    }    
    
    public static class Batch extends ArrayList<BatchItem> {}
}
