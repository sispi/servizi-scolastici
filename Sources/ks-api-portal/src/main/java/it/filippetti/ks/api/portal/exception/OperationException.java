/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.exception;

/**
 *
 * @author marco.mazzocchetti
 */
public class OperationException extends RuntimeException {
    
    public OperationException(Throwable cause) {
        super(cause);
    }
}
