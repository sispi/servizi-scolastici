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
public class TagsFilter {
    
    private Set<String> values;

    private TagsFilter(Set<String> values) {
        this.values = values;
    }

    public Set<String> getValues() {
        return values;
    }
    
    public static TagsFilter of(String tags) {
        if (tags == null) {
            return new TagsFilter(Collections.EMPTY_SET);
        } else {
           return new TagsFilter(Arrays
                .stream(tags.split(",", -1))
                .filter(t -> !StringUtils.isBlank(t))
                .map(t -> t.trim())
                .collect(Collectors.toUnmodifiableSet()));
        }
    }
}
