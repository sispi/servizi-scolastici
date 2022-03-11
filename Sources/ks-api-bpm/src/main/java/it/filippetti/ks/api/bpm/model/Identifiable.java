/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;

/**
 *
 * @author mazzocchetti
 */
public abstract class Identifiable implements Serializable {

    public abstract Object getId();

    public boolean isNew() {
        return getId() == null;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(getId());
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
        // jpa proxies needs instanceof class comparison
        if (!getClass().isInstance(obj)) {
            return false;
        }
        final Identifiable other = (Identifiable) obj;
        // jpa proxies needs method id access
        return Objects.equals(this.getId(), other.getId());
    }
    
    /**
     * Try to resolve and initialize a proxy.
     * Useful in the special case when we need to resolve a lazy to-one association 
     * that can be missing at runtime
     * 
     * @param <T>
     * @param proxy
     * @return 
     */
    protected static <T> Optional<T> optionalProxy(T proxy) {
        try {
            Hibernate.initialize(proxy);
            return Optional.ofNullable(proxy);      
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }    
}
