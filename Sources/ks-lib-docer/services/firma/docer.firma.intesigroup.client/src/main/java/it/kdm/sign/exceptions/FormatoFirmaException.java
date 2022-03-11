package it.kdm.sign.exceptions;

public class FormatoFirmaException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public FormatoFirmaException() {
		super();
	}
	public FormatoFirmaException( String message ) {
		super(message);
	}

}
