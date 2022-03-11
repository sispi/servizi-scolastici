/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

/**
 *
 * @author marco.mazzocchetti
 */
public enum EventType {
    signal, 
    message,
    timer,
    conditional,
    link,
    escalation,
    error,
    compensation,
    terminate, 
    none;
    
    public boolean isPublishable() {
        return 
            this == signal || 
            this == message;
    }
}