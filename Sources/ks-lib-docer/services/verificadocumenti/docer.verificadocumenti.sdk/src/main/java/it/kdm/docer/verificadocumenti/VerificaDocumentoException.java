/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.verificadocumenti;

/**
 *
 * @author lorenzo
 */
public class VerificaDocumentoException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5880021506635791585L;
	
	public VerificaDocumentoException(Throwable t) {
		super(t);
		
	}
    public VerificaDocumentoException(String message) {
		super(message);
		
	}
}
