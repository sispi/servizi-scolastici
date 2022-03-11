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
public enum PermissionType {
    
    // can view configured deployment
    View, 
    // can configure deployment
    Configure,
    // can start instances
    Start, 
    // can retry instances
    Retry, 
    // can abort instances
    Abort, 
    // can clone instances
    Clone, 
    // can chat on instances
    Chat
}
