/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@DiscriminatorValue("Configuration")
public class ConfigurationAsset extends Asset {

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Configuration configuration;
    
    public ConfigurationAsset() {
    }

    protected ConfigurationAsset(Configuration configuration, String name, byte[] content) {
        super(name, content);
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
    
    protected void merge(Asset asset) {
    }
}