/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import it.filippetti.ks.api.bpm.exception.ValidationException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
public class TaskAssignedAsFilter {

    /**
     * 
     * !!! ORDER MATTERS !!!
     * 
     */    
    private enum Value {
        ActualOwner,
        PotentialOwner,
        TaskStakeholder,
        BusinessAdministrator
    }

    private Value value;

    private TaskAssignedAsFilter(Value value) {
        this.value = value;
    }

    public Integer getValue() {
        return value != null ? value.ordinal() : null;
    }

    public static TaskAssignedAsFilter of(String assignedAs) throws ValidationException {
        
        if (StringUtils.isBlank(assignedAs)) {
            return new TaskAssignedAsFilter(null);
        }
        try {
            return new TaskAssignedAsFilter(Value.valueOf(assignedAs));
        } catch (IllegalArgumentException e) {
            throw new ValidationException(String.format("assignedAs: invalid value '%s', allowed values are %s",
                assignedAs,
                Arrays.stream(Value.values()).collect(Collectors.toList())));
        }
    }
}
