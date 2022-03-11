/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "Configuration", 
    description = ""
    + "Fetchable:"
    + "<ul><li>deployment (default)"
    + "<li>defaultInput"
    + "<li>assets"
    + "<li>permissions"
    + "<li>retentionPolicy"
    + "<li>authorizations</ul>"
    + "Sortable:"
    + "<ul><li>deployment.name (default)"
    + "<li>deployment.version (default)</ul>")   
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationDTO {

    @ApiModelProperty(position = 1, 
        example = "77") 
    private Long id;

    @ApiModelProperty(position = 2, 
        example = "default") 
    private String profile;

    @ApiModelProperty(position = 3, 
        example = "My category") 
    private String category;

    @ApiModelProperty(position = 4, 
        example = "true", 
        notes = "Indicates if this configuration is currently active") 
    private Boolean active;
    
    @ApiModelProperty(position = 5, 
        example = "true", 
        notes = "Indicates if new instances can be directly started by api users") 
    private Boolean runnable;
    
    @ApiModelProperty(position = 6, 
        example = "true", 
        notes = "Indicates if instances will appears by default in results") 
    private Boolean searchable;

    @ApiModelProperty(position = 7,
        notes = "The configured deployment")     
    private DeploymentDTO deployment;

    @ApiModelProperty(position = 8, 
        example = "{\"Start\":[\"Operators\", \"mario.rossi\"]}",    
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
    private Map<String, Set<String>> permissions;    
    
    @ApiModelProperty(position = 9, 
        notes = "Retention policy applied to completed instances")  
    private RetentionPolicyDTO retentionPolicy;

    @ApiModelProperty(position = 10,
        notes = "Default input applied to new instances")  
    private Map<String, Object> defaultInput;
    
    @ApiModelProperty(position = 11, 
        notes = ""        
        + "Configuration assets map that associates asset name with asset metadata and content "
        + "expressed as json object or raw text, depending on asset type.")      
    private Map<String, AssetDTO> assets;    

    @ApiModelProperty(position = 12, 
        example = "[\"View\", \"Start\"]",    
        notes = ""
        + "User authorized operations on this configuration.<br/>"
        + "Allowed values are:"
        + "<li>View"
        + "<li>Configure"
        + "<li>Start") 
    private List<String> authorizations;
    
    public ConfigurationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeploymentDTO getDeployment() {
        return deployment;
    }

    public void setDeployment(DeploymentDTO deployment) {
        this.deployment = deployment;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getRunnable() {
        return runnable;
    }

    public Boolean getSearchable() {
        return searchable;
    }

    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    public void setRunnable(Boolean runnable) {
        this.runnable = runnable;
    }

    public Map<String, Object> getDefaultInput() {
        return defaultInput;
    }

    public void setDefaultInput(Map<String, Object> defaultInput) {
        this.defaultInput = defaultInput;
    }

    public Map<String, AssetDTO> getAssets() {
        return assets;
    }

    public void setAssets(Map<String, AssetDTO> assets) {
        this.assets = assets;
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

    public List<String> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<String> authorizations) {
        this.authorizations = authorizations;
    }
    
    @ApiModel("Page<Configuration>")
    public static class Page extends PageDTO<ConfigurationDTO> {}     
}
