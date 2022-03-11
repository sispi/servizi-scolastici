/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
public class TaskAttachmentsFilter {
    
    private Set<String> values;

    private TaskAttachmentsFilter(Set<String> values) {
        this.values = values;
    }

    public Set<String> getValues() {
        return values;
    }

    public boolean isAny() {
        return values != null && values.isEmpty();
    }
    
    public static TaskAttachmentsFilter of(String attachments) {
        if (StringUtils.isBlank(attachments)) {
            return new TaskAttachmentsFilter(null);
        }
        else if (attachments.equals("*")) {     
            return new TaskAttachmentsFilter(Collections.EMPTY_SET);
        } 
        else {
           return new TaskAttachmentsFilter(Arrays
                .stream(attachments.split(",", -1))
                .filter(t -> !StringUtils.isBlank(t))
                .map(t -> t.trim())
                .collect(Collectors.collectingAndThen(
                    Collectors.toUnmodifiableSet(), 
                    t -> t.isEmpty() ? null : t)));
        }
    }
}
