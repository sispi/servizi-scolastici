/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;
import org.kie.internal.task.api.model.InternalPeopleAssignments;

/**
 *
 * @author marco.mazzocchetti
 */
public class TaskAssignments implements Serializable {

    private Task task;

    public TaskAssignments(Task task) {
        this.task = task;
    }

    public String getTaskCreator() {
        return task.getTaskData().getCreatedBy() != null ? 
            task.getTaskData().getCreatedBy().getId() : 
            null;
    }

    public String getTaskInitiator() {
        return getInternalPeopleAssignments().getTaskInitiator() != null ?  
            getInternalPeopleAssignments().getTaskInitiator().getId() : 
            null;
    }
    
    public String getSwimlaneActor() {
        return task.getSwimlaneActorId();
    }

    public String getRefuseGroup() {
        return task.getRefuseGroupId();
    }
    
    public String getActualOwner() {
        return task.getTaskData().getActualOwner() != null ? 
            task.getTaskData().getActualOwner().getId() : 
            null;
    }

    public Set<String> getPotentialOwners() {
        return getInternalPeopleAssignments()
            .getPotentialOwners()
            .stream()
            .map(e -> e.getId())
            .collect(Collectors.toSet());    
    }
    
    public Set<String> getExcludedOwners() {
        return getInternalPeopleAssignments()
            .getExcludedOwners()
            .stream()
            .map(e -> e.getId())
            .collect(Collectors.toUnmodifiableSet());
    }

    public Set<String> getTaskStakeholders() {
        return getInternalPeopleAssignments()
            .getTaskStakeholders()
            .stream()
            .map(e -> e.getId())
            .collect(Collectors.toUnmodifiableSet());    
    }

    public Set<String> getRecipients() {
        return getInternalPeopleAssignments()
            .getRecipients()
            .stream()
            .map(e -> e.getId())
            .collect(Collectors.toUnmodifiableSet());       }



    public Set<String> getBusinessAdministrators() {
        return getInternalPeopleAssignments()
            .getBusinessAdministrators()
            .stream()
            .map(e -> e.getId())
            .collect(Collectors.toUnmodifiableSet());    
    }

    private InternalPeopleAssignments getInternalPeopleAssignments() {
        return ((InternalPeopleAssignments) task.getPeopleAssignments());
    }
}
