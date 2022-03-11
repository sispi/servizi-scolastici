package it.kdm.sign.exceptions;

public class TipoFirmaException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public TipoFirmaException() {
		super();
	}
	public TipoFirmaException( String message ) {
		super(message);
	}
}
