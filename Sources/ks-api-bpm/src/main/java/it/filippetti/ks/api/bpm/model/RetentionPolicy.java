/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author marco.mazzocchetti
 */
@Embeddable
public class RetentionPolicy {
    
    @Basic
    @Column(nullable = false)
    private Integer retentionDays;

    @Basic
    @Column(nullable = false)
    private Boolean retentionClean;

    public RetentionPolicy() {
        retentionDays = -1;
        retentionClean = true;
    }
    
    public boolean isEnabled() {
        return retentionDays >= 0;
    }
    
    public int days() {
        return retentionDays;
    }

    public boolean isCleanEnabled() {
        return retentionClean;
    }

    public void update(int days, boolean cleanRuntimeData) {
        this.retentionDays = days;
        this.retentionClean = cleanRuntimeData;
    }
}
