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
public class Correlation implements Serializable {
    
    private Long instanceId;
    private Long nodeInstanceId;
    private String unitId;
    private Long workItemId;

    public Correlation(Long instanceId, String unitId) {
        this.instanceId = instanceId;
        this.unitId = unitId;
    }

    public Correlation(Long instanceId, Long nodeInstanceId, String unitId, Long workItemId) {
        this.instanceId = instanceId;
        this.nodeInstanceId = nodeInstanceId;
        this.unitId = unitId;
        this.workItemId = workItemId;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public Long getNodeInstanceId() {
        return nodeInstanceId;
    }

    public String getUnitId() {
        return unitId;
    }

    public Long getWorkItemId() {
        return workItemId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.instanceId);
        hash = 79 * hash + Objects.hashCode(this.nodeInstanceId);
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
        final Correlation other = (Correlation) obj;
        if (!Objects.equals(this.instanceId, other.instanceId)) {
            return false;
        }
        return Objects.equals(this.nodeInstanceId, other.nodeInstanceId);
    }
}
