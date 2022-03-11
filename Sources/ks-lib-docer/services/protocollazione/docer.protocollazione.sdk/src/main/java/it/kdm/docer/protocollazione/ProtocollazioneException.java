/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.protocollazione;

/**
 *
 * @author lorenzo
 */
public class ProtocollazioneException extends Exception {
    public ProtocollazioneException(Throwable t) {
		super(t);
		
	}
    public ProtocollazioneException(String message) {
		super(message);
		
	}
}
