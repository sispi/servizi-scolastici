/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "Deployment", 
    description = ""
    + "Fetchable:"
    + "<ul><li>configuration (default)"
    + "<li>assets"
    + "<li>dependencies"
    + "<li>dependants"
    + "<li>events</ul>"
    + "Sortable:"
    + "<ul><li>name (default)"
    + "<li>version (default)"
    + "<li>deployTs</ul>") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeploymentDTO {
    
    @ApiModelProperty(position = 1, 
        example = "77") 
    private Long id;
    
    @ApiModelProperty(position = 2, 
        example = "MyProcess", 
        notes = "Process name")
    private String name;
    
    @ApiModelProperty(position = 3, 
        example = "1.0", 
        notes = "Process version")    
    private String version;
    
    @ApiModelProperty(position = 4, 
        example = "MyProcess1.0")    
    private String processId;
    
    @ApiModelProperty(position = 5, 
        example = "true", 
        notes = "Indicates if this process belongs to current user organization")        
    private Boolean owned;

    @ApiModelProperty(position = 6, 
        example = "false", 
        notes = "Indicates if this process is shared with other organizations")            
    private Boolean shared;

    @ApiModelProperty(position = 7, 
        example = "true", 
        notes = "Indicates if this deployment is currently active") 
    private Boolean active;
    
    @ApiModelProperty(position = 8, 
        example = "2020-11-11T14:57:48.000Z", 
        notes = "Last deploy timestamp") 
    private Date deployTs;
    
    @ApiModelProperty(position = 9, 
        notes = "The configurations of this deployment, if any") 
    private List<ConfigurationDTO> configurations;
    
    @ApiModelProperty(position = 10, 
        notes = ""        
        + "Deployment assets map that associates asset name with asset metadata and content "
        + "expressed as json object or raw text, depending on asset type.")      
    private Map<String, AssetDTO> assets; 

    @ApiModelProperty(position = 11, 
        example = "[]",
        notes = "Deployments on which this deployment depends")     
    private List<DeploymentDTO> dependencies; 

    @ApiModelProperty(position = 12,
        example = "[]",            
        notes = "Deployments that depend on this deployment")     
    private List<DeploymentDTO> dependants; 

    @ApiModelProperty(position = 13, 
        notes = "Events declared by this deployment")  
    private List<EventDTO> events;

    public DeploymentDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Boolean getOwned() {
        return owned;
    }

    public void setOwned(Boolean owned) {
        this.owned = owned;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getDeployTs() {
        return deployTs;
    }

    public void setDeployTs(Date deployTs) {
        this.deployTs = deployTs;
    }
    
    public List<ConfigurationDTO> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<ConfigurationDTO> configurations) {
        this.configurations = configurations;
    }    

    public Map<String, AssetDTO> getAssets() {
        return assets;
    }

    public void setAssets(Map<String, AssetDTO> assets) {
        this.assets = assets;
    }

    public List<DeploymentDTO> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<DeploymentDTO> dependencies) {
        this.dependencies = dependencies;
    }

    public List<DeploymentDTO> getDependants() {
        return dependants;
    }

    public void setDependants(List<DeploymentDTO> dependants) {
        this.dependants = dependants;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }
    
    @ApiModel("Page<Deployment>")
    public static class Page extends PageDTO<DeploymentDTO> {}
}
