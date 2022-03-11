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
public class DatastoreException extends Exception {

    public DatastoreException(String message) {
        super(message);
    }

    public DatastoreException(String message, Throwable cause) {
        super(message, cause);
    }
}