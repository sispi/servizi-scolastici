/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import it.filippetti.ks.api.bpm.exception.ValidationException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.runtime.process.ProcessInstance;

/**
 *
 * @author marco.mazzocchetti
 */
public class InstanceStatusFilter {
   
    private static final Set<Integer> enabledValues = Set.of(
        ProcessInstance.STATE_ACTIVE, 
        ProcessInstance.STATE_COMPLETED, 
        ProcessInstance.STATE_ABORTED);
    
    private Set<Integer> values;

    private InstanceStatusFilter(Set<Integer> values) {
        this.values = values;
    }

    public Set<Integer> getValues() {
        return values;
    }
    
    public static InstanceStatusFilter of(String status) throws ValidationException {
        
        Set<Integer> values;
        Integer value;
        if (StringUtils.isBlank(status)) {
            return new InstanceStatusFilter(null);
        }
        values = new HashSet<>();
        for (String stringValue : status.split(",", -1)) {
            try {
                value = Integer.valueOf(stringValue);
                if (enabledValues.contains(value)) {
                    values.add(value);
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                throw new ValidationException(String.format(
                    "status: invalid value '%s', allowed values are %s",
                    stringValue,
                    enabledValues));
            }
        }
        return new InstanceStatusFilter(Collections.unmodifiableSet(values));
    }
}
