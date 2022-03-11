/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.registrazione;

/**
 *
 * @author lorenzo
 */
public class RegistrazioneException extends Exception {
    public RegistrazioneException(Throwable t) {
		super(t);
		
	}
    public RegistrazioneException(String message) {
		super(message);
		
	}
}
