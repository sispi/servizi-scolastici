package it.kdm.docer.timbrodigitale.sdk;

public class ProviderException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7548459432737237880L;

	public ProviderException(String message) {
		super(message);
	}
	
	public ProviderException(Throwable throwable) {
		super(throwable);
	}
}
