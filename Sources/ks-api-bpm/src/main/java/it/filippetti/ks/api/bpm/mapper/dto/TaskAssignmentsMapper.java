/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.TaskAssignmentsDTO;
import it.filippetti.ks.api.bpm.mapper.SimpleMapper;
import it.filippetti.ks.api.bpm.model.TaskAssignments;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class TaskAssignmentsMapper extends SimpleMapper<TaskAssignments, TaskAssignmentsDTO> {

    @Override
    protected TaskAssignmentsDTO doMapping(TaskAssignments assignments) throws Exception {
        
        TaskAssignmentsDTO dto = new TaskAssignmentsDTO();
        
        dto.setSwimlaneActor(assignments.getSwimlaneActor());
        dto.setRefuseGroup(assignments.getRefuseGroup());
        dto.setActualOwner(assignments.getActualOwner());
        dto.setPotentialOwners(assignments
            .getPotentialOwners()
            .stream()
            .sorted()
            .collect(Collectors.toUnmodifiableList()));
        dto.setBusinessAdministrators(assignments
            .getBusinessAdministrators()
            .stream()
            .sorted()
            .collect(Collectors.toUnmodifiableList()));
        dto.setTaskStakeholders(assignments
            .getTaskStakeholders()
            .stream()
            .sorted()
            .collect(Collectors.toUnmodifiableList()));
        dto.setExcludedOwners(assignments
            .getExcludedOwners()
            .stream()
            .sorted()
            .collect(Collectors.toUnmodifiableList()));
        dto.setRecipients(assignments
            .getRecipients()
            .stream()
            .sorted()
            .collect(Collectors.toUnmodifiableList()));
        return dto;
    }
    
}
