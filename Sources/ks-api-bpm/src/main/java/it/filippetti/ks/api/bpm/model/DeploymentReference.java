/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Objects;

/**
 *
 * @author marco.mazzocchetti
 */
public class DeploymentReference {
    
    private Long id;
    private String unitId;
    private String tenant;
    private String organization;
    
    public DeploymentReference(Long id, String unitId, String tenant, String organization) {
        this.id = id;
        this.unitId = unitId;
    }

    public Long getId() {
        return id;
    }

    public String getUnitId() {
        return unitId;
    }

    public String getTenant() {
        return tenant;
    }

    public String getOrganization() {
        return organization;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final DeploymentReference other = (DeploymentReference) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
