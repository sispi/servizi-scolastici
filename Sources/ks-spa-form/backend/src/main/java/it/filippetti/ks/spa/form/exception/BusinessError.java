/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.exception;

/**
 *
 * @author marco.mazzocchetti
 */
public enum BusinessError {  
    FORM_ALREADY_EXISTS,
    FORM_TEMPLATE_UNDEFINED,
    FORM_VIEW_GENERATION_ERROR;
    
    public String code() {
        return this.name();
    }
}
