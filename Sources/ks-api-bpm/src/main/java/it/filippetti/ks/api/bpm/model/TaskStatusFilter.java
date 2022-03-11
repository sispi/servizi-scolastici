/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import it.filippetti.ks.api.bpm.exception.ValidationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.task.model.Status;

/**
 *
 * @author marco.mazzocchetti
 */
public class TaskStatusFilter {
    
    private enum EnabledValue {
        Ready, Reserved, InProgress, Completed, Error, Obsolete
    }
    
    private Set<Status> values;

    private TaskStatusFilter(Set<Status> values) {
        this.values = values;
    }

    public Set<Status> getValues() {
        return values;
    }
    
    public static TaskStatusFilter of(String status) throws ValidationException {
        
        Set<Status> values;
        
        if (StringUtils.isBlank(status)) {
            return new TaskStatusFilter(null);
        }
        values = new HashSet<>();
        for (String value : status.split(",", -1)) {
            try {
                values.add(Status.valueOf(EnabledValue.valueOf(value).name()));
            } catch (IllegalArgumentException e) {
                throw new ValidationException(String.format(
                    "status: invalid value '%s', allowed values are %s",
                    value,
                    Arrays.stream(EnabledValue.values()).collect(Collectors.toList())));
            }
        }
        return new TaskStatusFilter(Collections.unmodifiableSet(values));
    }
}
