package it.kdm.docer.conservazione;

public class ConservazioneException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4107948381203416358L;
	
	public ConservazioneException(String message) {
		super(message);
	}
	
	public ConservazioneException(String message, Throwable t) {
		super(message, t);
	}
	
	public ConservazioneException(Throwable t) {
		super(t);
	}

}
