/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.PEC;

/**
 *
 * @author lorenzo
 */

public class PECException extends Exception {
    public PECException(Throwable t) {
		super(t);
		
	}
    public PECException(String message) {
		super(message);
		
	}
}