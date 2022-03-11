/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.firma;

/**
 * @author lorenzo
 */

public class InvioDocumentiException extends Exception {
    public InvioDocumentiException(Throwable t) {
        super(t);

    }

    public InvioDocumentiException(String message) {
        super(message);
    }

    public InvioDocumentiException() {
        super();
    }

    public InvioDocumentiException(String message, Throwable cause) {
        super(message, cause);
    }

}