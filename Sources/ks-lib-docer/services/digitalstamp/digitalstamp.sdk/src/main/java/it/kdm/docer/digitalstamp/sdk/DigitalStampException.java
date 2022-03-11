package it.kdm.docer.digitalstamp.sdk;

public class DigitalStampException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 7548459432737237880L;

	public DigitalStampException(String message) {
		super(message);
	}

	public DigitalStampException(Throwable throwable) {
		super(throwable);
	}
}
