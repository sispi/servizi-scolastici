package it.kdm.docer.timbrodigitale.sdk;

public class BusinessLogicException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4874401154183825097L;

	public BusinessLogicException(String message) {
		super(message);
	}

	public BusinessLogicException(Throwable throwable) {
		super(throwable);
	}
}
