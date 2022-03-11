/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_DEPLOYMENT_DEPENDENCY",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"dependant_id", "dependency_id"})
    }
)
public class DeploymentDependency extends Identifiable {
    
    @EmbeddedId
    private Id id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("dependantId")
    private Deployment dependant;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional = false)
    @MapsId("dependencyId")
    private Deployment dependency;

    @Basic
    private String variableKeys;
    
    public DeploymentDependency() {
    }
    
    public DeploymentDependency(
        Deployment dependant, 
        Deployment dependency,
        Set<String> variableKeys) {
        this.id = new Id(dependant.getId(), dependency.getId());
        this.dependant = dependant;
        this.dependency = dependency;
        setVariableKeys(variableKeys);
    }

    @Override
    public Id getId() {
        return id;
    }

    public Deployment getDependant() {
        return dependant;
    }

    public Deployment getDependency() {
        return dependency;
    }

    public Set<String> getVariableKeys() {
        return variableKeys == null ? 
            Collections.EMPTY_SET :
            Set.of(variableKeys.split(","));
    }

    protected void setVariableKeys(Set<String> variableKeys) {
        this.variableKeys = variableKeys.isEmpty() ? 
            null : 
            variableKeys.stream().collect(Collectors.joining(","));
    }
    
    @Embeddable
    public static final class Id implements Serializable {
        
        private Long dependantId;
        private Long dependencyId;

        public Id() {
        }

        public Id(Long dependantId, Long dependencyId) {
            this.dependantId = dependantId;
            this.dependencyId = dependencyId;
        }

        public Long getDependantId() {
            return dependantId;
        }

        public Long getDependencyId() {
            return dependencyId;
        }

        @Override
        public int hashCode() {
            int hash = 7;
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
            final Id other = (Id) obj;
            if (!Objects.equals(this.dependantId, other.dependantId)) {
                return false;
            }
            return Objects.equals(this.dependencyId, other.dependencyId);
        }
    }
}
