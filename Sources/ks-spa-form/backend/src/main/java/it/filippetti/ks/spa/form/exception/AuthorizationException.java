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
public class AuthorizationException extends ApplicationException {
    
    public static final String DEFAULT_MESSAGE = "Authorization failed";
    
    public AuthorizationException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthorizationException(String details) {
        super(details);
    }
}
