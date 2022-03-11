/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.exception;

/**
 *
 * @author marco.mazzocchetti
 */
public enum BusinessError {  
    EXAMPLE_BUSINESS_ERROR,
    SERVICE_BUSINESS_ERROR;
    
    public String code() {
        return this.name();
    }
}
