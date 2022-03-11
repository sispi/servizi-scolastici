/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.exception;

/**
 *
 * @author marco.mazzocchetti
 */
public class CommandException extends RuntimeException {

    public CommandException(Throwable cause) {
        super(cause);
    }    
}
