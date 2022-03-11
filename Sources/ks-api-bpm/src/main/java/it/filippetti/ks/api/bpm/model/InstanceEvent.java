/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Embeddable
public class InstanceEvent implements Serializable {
    
    @Basic
    @Column(name = "eventId")
    private String id;
    
    @Basic
    @Column(name = "eventType")
    private String type; 
    
    @Basic
    @Column(name = "eventNodeType")    
    private String nodeType; 
    
    @Basic
    @Column(name = "eventCorrelable")
    private Boolean correlable;

    public InstanceEvent() {
    }

    public InstanceEvent(
        String id,
        String type,
        String nodeType,
        Boolean correlable) {
        this.id = StringUtils.abbreviate(id, 255);
        this.type = type;
        this.nodeType = nodeType;
        this.correlable = correlable;
    }

    public String geId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getNodeType() {
        return nodeType;
    }

    public boolean isCorrelable() {
        return correlable;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
        hash = 19 * hash + Objects.hashCode(this.type);
        hash = 19 * hash + Objects.hashCode(this.nodeType);
        hash = 19 * hash + Objects.hashCode(this.correlable);
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
        final InstanceEvent other = (InstanceEvent) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.nodeType, other.nodeType)) {
            return false;
        }
        return Objects.equals(this.correlable, other.correlable);
    }  
}
