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
public enum CorrelationError {
    
    message_id_not_evaluable,
    message_not_defined,
    correlation_key_not_evaluable,
    correlation_not_found,
    correlation_non_unique;
    
    public boolean isWorkflowError() {
        return 
            this == correlation_not_found || 
            this == correlation_non_unique;
    }
}
