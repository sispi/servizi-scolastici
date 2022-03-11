/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.fascicolazione;

/**
 *
 * @author lorenzo
 */
public class FascicolazioneException extends Exception {
    public FascicolazioneException(Throwable t) {
		super(t);
		
	}
    public FascicolazioneException(String message) {
		super(message);
		
	}
}
