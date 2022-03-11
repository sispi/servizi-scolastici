/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks6.exception;

/**
 *
 * @author marco.mazzocchetti
 */
public class OperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OperationException(Throwable cause) {
		super(cause);
	}
}
