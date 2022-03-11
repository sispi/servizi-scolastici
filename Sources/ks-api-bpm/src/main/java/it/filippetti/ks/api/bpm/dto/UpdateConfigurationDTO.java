/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.api.bpm.validator.OneOf;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("UpdateConfiguration")
public class UpdateConfigurationDTO {

    @ApiModelProperty(position = 1, required = false,
        example = "My category")     
    @Length(min = 1, max = 255)
    private String category;
    
    @ApiModelProperty(position = 2, required = false,
        example = "true", 
        notes = "Indicates if new instances can be directly started by api users")     
    private Boolean runnable;

    @ApiModelProperty(position = 3, required = false,
        example = "true", 
        notes = "Indicates if instances will appears by default in results")      
    private Boolean searchable;
    
    
    @ApiModelProperty(position = 4, required = false,
        example = "{\"Start\": [\"Operators\", \"mario.rossi\"]}", 
        notes = ""
        + "Permissions map that associates every permissions with a set of "
        + "identities, expressed as groupId or userId.<br/>"
        + "Organization admins have all permissions by default.<br/>"
        + "Managed permissions are:"
        + "<li>View"
        + "<li>Configure"
        + "<li>Start"
        + "<li>Retry"
        + "<li>Abort"
        + "<li>Clone"
        + "<li>Chat")     
    @NotNull
    private Map<
        @NotNull @OneOf({"View", "Configure", "Start", "Retry", "Abort", "Clone", "Chat"}) String, 
        @NotNull Set<String>
        > permissions;

    @ApiModelProperty(position = 5, 
        notes = "Retention policy applied to completed instances")     
    @Valid
    private RetentionPolicyDTO retentionPolicy;

    @ApiModelProperty(position = 6,
        notes = "Default input applied to new instances")      
    @Valid
    private Map<@NotNull String, Object> defaultInput;
    
    @ApiModelProperty(position = 7, 
        example = "{\"Human Task_block2.ftl\":\"...\"}", 
        notes = ""        
        + "Configuration asset overrides map that associates asset name with asset content "
        + "expressed as json object or raw text, depending on asset type.")     
    @NotNull
    private Map<@NotNull String, @NotNull Object> assets;
    
    public UpdateConfigurationDTO() {
        permissions = new HashMap<>();
        assets = new HashMap<>();
    }

    public Boolean getRunnable() {
        return runnable;
    }

    public void setRunnable(Boolean runnable) {
        this.runnable = runnable;
    }

    public Boolean getSearchable() {
        return searchable;
    }

    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Set<String>> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Set<String>> permissions) {
        this.permissions = permissions;
    }  

    public RetentionPolicyDTO getRetentionPolicy() {
        return retentionPolicy;
    }

    public void setRetentionPolicy(RetentionPolicyDTO retentionPolicy) {
        this.retentionPolicy = retentionPolicy;
    }

    public Map<String, Object> getDefaultInput() {
        return defaultInput;
    }

    public void setDefaultInput(Map<String, Object> defaultInput) {
        this.defaultInput = defaultInput;
    }

    public Map<String, Object> getAssets() {
        return assets;
    }

    public void setAssets(Map<String, Object> assets) {
        this.assets = assets;
    }
}
