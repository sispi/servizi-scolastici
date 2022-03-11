/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import com.google.common.collect.Lists;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 
 * @author marco.mazzocchetti
 */
public interface FormDataSupport {
    
    public default Map<String, Object> formDataToMap(
        Map<String, List<String>> formData) {
        return formDataToMap(formData, new HashMap<>());
    }    
    
    public default Map<String, Object> formDataToMap(
        Map<String, List<String>> formData, Map<String, Object> map) {
        
        Pattern pattern;
        Matcher matcher;
        String accessors, string;
        List<Object> path;
        Object key, lookupKey, object, container, value;
        boolean parseValues, isInlineArray;
        String inlineArray;
        int i;

        parseValues = formData.remove("__parseValues__") != null;
        
        pattern = Pattern.compile("^(.+)\\[(\\d+)\\]$");
        for (Map.Entry<String, List<String>> entry : formData.entrySet()) {
            accessors = entry.getKey();
            // detect inline array special case
            if (accessors.endsWith("[]")) {
                isInlineArray = true;
                accessors = accessors.substring(0, accessors.length() - 2);
            } else {
                isInlineArray = false;
            }
            // compute path
            path = new ArrayList<>();
            for (String accessor : accessors.split("\\.", -1)) {
                if ((matcher = pattern.matcher(accessor)).matches()) {
                    path.add(matcher.group(1));
                    path.add(Integer.parseInt(matcher.group(2)));
                } else {
                    path.add(accessor);
                }
            }
            // compute value
            if (entry.getValue() == null || 
                entry.getValue().isEmpty() || 
                entry.getValue().get(0) == null) {
                value = null;
            }
            else {
                // inline array special case
                if (isInlineArray) {
                    inlineArray = entry.getValue().get(0);
                    if (inlineArray.startsWith("[") && 
                        inlineArray.endsWith("]")) {
                        inlineArray = inlineArray.substring(1, inlineArray.length() - 1);
                    }
                    value = inlineArray.length() > 0 ? 
                        Arrays.asList(inlineArray.split(",", -1)) : 
                        Lists.newArrayList();
                // standard case
                } else {
                    string = entry.getValue().get(0);
                    if (parseValues) {
                        if (string.startsWith("\"") && 
                            string.endsWith("\"")) {
                            value = string.substring(1, string.length() - 1);
                        } else {
                            value = string;
                            try {
                                value = Date.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(string)));
                            } catch (DateTimeParseException e1) {
                                try {
                                    value = NumberUtils.createNumber(string.replaceFirst("^0+(?!$)", ""));
                                } catch (NumberFormatException e2) {
                                    try {
                                        value = BooleanUtils.toBoolean(string.toLowerCase(), "true", "false");
                                    } catch (IllegalArgumentException e3) {
                                    }
                                }
                            }
                        }
                    } else {
                        value = string;
                    }
                }
            }
            // set value
            container = map;
            i = 0;
            while (i <  path.size() - 1) {
                key = path.get(i);
                lookupKey = path.get(i + 1);
                if (key instanceof Integer) {
                    object = ((List) container).get((Integer) key);
                    if (!(object instanceof Map)) {
                        object = new HashMap<>();
                        ((List) container).set((Integer) key, object);
                    }
                } else {
                    object = ((Map) container).get((String) key);
                    if (lookupKey instanceof Integer) {
                        if (!(object instanceof List)) {
                            object = new ArrayList<>();
                            ((Map) container).put(key, object);                        
                        }
                        for (int j = ((List) object).size(); j <= (Integer) lookupKey; j++) {
                            ((List) object).add(null);
                        }
                    } 
                    else if (!(object instanceof Map)) {
                        object = new HashMap<>();
                        ((Map) container).put(key, object);
                    }
                }
                container = object;
                i++;
            }
            key = path.get(i);
            if (key instanceof Integer) {
                ((List) container).set((Integer) key, value);
            } else {
                ((Map) container).put(key, value);
            }
        }        
        return map;
    }
}
