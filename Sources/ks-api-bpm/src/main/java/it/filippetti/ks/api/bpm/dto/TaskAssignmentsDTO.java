/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "TaskAssignments", 
    description = "All values are expressed as identities, i.e. groupId or userId")
public class TaskAssignmentsDTO {
    
    @ApiModelProperty(position = 3,
        example = "mario.rossi")
    private String swimlaneActor;    
    
    @ApiModelProperty(position = 4,
        example = "Administrators")    
    private String refuseGroup;    
    
    @ApiModelProperty(position = 5,
        example = "mario.rossi")    
    private String actualOwner;
    
    @ApiModelProperty(position = 6,
        example = "[\"Operators\"]")    
    private List<String> potentialOwners;
    
    @ApiModelProperty(position = 7,
        example = "[\"Administrators\"]")    
    private List<String> businessAdministrators;
    
    @ApiModelProperty(position = 8,
        example = "[\"Managers\"]")    
    private List<String> taskStakeholders;    
    
    @ApiModelProperty(position = 9,
        example = "[]")    
    private List<String> excludedOwners;
    
    @ApiModelProperty(position = 10,
        example = "[]")    
    private List<String> recipients;

    public TaskAssignmentsDTO() {
    }

    public String getSwimlaneActor() {
        return swimlaneActor;
    }

    public void setSwimlaneActor(String swimlaneActor) {
        this.swimlaneActor = swimlaneActor;
    }

    public String getRefuseGroup() {
        return refuseGroup;
    }

    public void setRefuseGroup(String refuseGroup) {
        this.refuseGroup = refuseGroup;
    }

    public String getActualOwner() {
        return actualOwner;
    }

    public void setActualOwner(String actualOwner) {
        this.actualOwner = actualOwner;
    }

    public List<String> getPotentialOwners() {
        return potentialOwners;
    }

    public void setPotentialOwners(List<String> potentialOwners) {
        this.potentialOwners = potentialOwners;
    }

    public List<String> getBusinessAdministrators() {
        return businessAdministrators;
    }

    public void setBusinessAdministrators(List<String> businessAdministrators) {
        this.businessAdministrators = businessAdministrators;
    }

    public List<String> getTaskStakeholders() {
        return taskStakeholders;
    }

    public void setTaskStakeholders(List<String> taskStakeholders) {
        this.taskStakeholders = taskStakeholders;
    }

    public List<String> getExcludedOwners() {
        return excludedOwners;
    }

    public void setExcludedOwners(List<String> excludedOwners) {
        this.excludedOwners = excludedOwners;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }
}
