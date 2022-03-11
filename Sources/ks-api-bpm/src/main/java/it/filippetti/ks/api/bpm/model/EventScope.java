/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author marco.mazzocchetti
 */
public class EventScope implements Serializable {

    private EventScopeType type;
    private Long id;    
    
    public EventScope(EventScopeType type, Long id) {
        this.type = type;
        this.id = id;
    }

    public EventScopeType getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        if (type == EventScopeType.organization) {
            return type.name();
        } else {
            return String.format("s (%s)", type.name(), id.toString());
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.type);
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final EventScope other = (EventScope) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
        
    public static EventScope instance(Long instanceId) {
        return new EventScope(EventScopeType.instance, instanceId);
    }

    public static EventScope organization() {
        return new EventScope(EventScopeType.organization, null);
    }
}
