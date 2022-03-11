/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Embeddable
public class InstanceContext implements Serializable {

    public static final InstanceContext MAIN = new InstanceContext("0", "0");
    public static final String SEPARATOR = ":";

    @Basic
    @Column(name = "contextId", nullable = false)
    private String id;

    @Basic
    @Column(name = "contextInstanceId", nullable = false)
    private String instanceId;

    public InstanceContext() {
    }

    public InstanceContext(String id, String instanceId) {
        this.id = id;
        this.instanceId = instanceId;
    }

    public String id() {
        return id;
    }

    public String instanceId() {
        return instanceId;
    }
    
    public String key(Long instanceId) {
       return String.format("%s%s%s", instanceId, SEPARATOR, this.instanceId);
    }
    
    public boolean isMain() {
        return this.equals(MAIN);
    }

    public List<String> hierarchy() {
        
        String[] id;
        List<String> hierarchy;
        
        hierarchy = Lists.newArrayList(MAIN.instanceId);
        if (!instanceId.equals(MAIN.instanceId)) {
            id = instanceId.split(SEPARATOR);
            for (int i = 0; i < id.length; i++) {
                hierarchy.add(String.join(SEPARATOR, ArrayUtils.subarray(id, 0, i)));
            }
        
        }
        return Collections.unmodifiableList(hierarchy); 
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", id, instanceId);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.instanceId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InstanceContext other = (InstanceContext) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.instanceId, other.instanceId);
    }    
}
