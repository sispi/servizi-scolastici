/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Date;
import org.jbpm.kie.services.impl.KModuleDeploymentUnit;

/**
 *
 * @author marco.mazzocchetti
 */
public class DeploymentUnit extends KModuleDeploymentUnit {

    private Date deployTs;

    public DeploymentUnit(String groupId, String artifactId, String version) {
        this(groupId, artifactId, version, new Date());
    }

    public DeploymentUnit(String groupId, String artifactId, String version, Date deployTs) {
        super(groupId, artifactId, version);
        this.deployTs = deployTs;
    }

    public String getTenant() {
        return getGroupId().split("\\.")[0];
    }

    public String getOrganization() {
        return getGroupId().split("\\.")[1];
    }

    public Date getDeployTs() {
        return deployTs;
    }

    public void setDeployTs(Date deployTs) {
        this.deployTs = deployTs;
    }
}    