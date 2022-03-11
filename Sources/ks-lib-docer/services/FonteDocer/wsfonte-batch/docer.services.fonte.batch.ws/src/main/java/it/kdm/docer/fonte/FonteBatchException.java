/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.fonte;

public class FonteBatchException extends Exception {
    
	
	public FonteBatchException(Throwable t) {
		super(t);
		
	}
    public FonteBatchException(String message) {
		super(message);		
	}
}
