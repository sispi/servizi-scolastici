/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@DiscriminatorValue("Deployment")
public class DeploymentAsset extends Asset {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional = true)
    private Deployment deployment;
    
    public DeploymentAsset() {
    }

    protected DeploymentAsset(Deployment deployment, String name, byte[] content) {
        super(name, content);
        this.deployment = deployment;
    }

    public Deployment getDeployment() {
        return deployment;
    }
}
