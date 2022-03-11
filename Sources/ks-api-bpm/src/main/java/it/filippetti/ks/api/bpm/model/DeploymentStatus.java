/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_DEPLOYMENT_STATUS",
    indexes = {
        @Index(columnList="updateTs")}
)
public class DeploymentStatus extends Identifiable {

    @Id
    private String unitId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date  deployTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date  updateTs;

    @Basic
    @Column(nullable = false)
    private Boolean deployed;

    @Basic
    @Column(nullable = false)
    private Boolean active;
    
    public DeploymentStatus() {
    }

    public DeploymentStatus(String id) {
        this.unitId = id;
        this.deployTs = this.updateTs = new Date();
        this.deployed = true;
        this.active = true;
    }

    @Override
    public String getId() {
        return unitId;
    }    

    public String getUnitId() {
        return unitId;
    }

    public String getTenant() {
        return getGroupId().split("\\.")[0];
    }

    public String getOrganization() {
        return getGroupId().split("\\.")[1];
    }
    
    public String getGroupId() {
        return unitId.split(":")[0];
    }

    public String getArtifactId() {
        return unitId.split(":")[1];
    }

    public String getVersion() {
        return unitId.split(":")[2];
    }
    
    public Date getDeployTs() {
        return deployTs;
    }

    public Date getUpdateTs() {
        return updateTs;
    }

    public boolean isDeployed() {
        return deployed;
    }

    public boolean isActive() {
        return active;
    }
    
    public DeploymentStatus update(Boolean deployed, Boolean active) {
        
        this.updateTs = new Date();
        
        if (deployed != null) {
            this.deployTs = this.updateTs;
            this.deployed = deployed;
        }
        
        if (active != null) {
            this.active = active;
        }
        return this;
    }
}