/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.configuration;

/**
 *
 * @author marco.mazzocchetti
 */
public enum ApplicationProfile {
    test, local, development, staging, release;
    
    public boolean isAnyOf(ApplicationProfile... profiles) {
        for (ApplicationProfile profile : profiles) {
            if (this == profile) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isCluster() {
        return 
            this == development || 
            this == staging || 
            this == release;
    }
}
