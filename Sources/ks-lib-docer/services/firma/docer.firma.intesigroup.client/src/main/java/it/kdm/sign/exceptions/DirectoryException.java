package it.kdm.sign.exceptions;

public class DirectoryException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public DirectoryException() {
		super();
	}
	public DirectoryException( String message ) {
		super(message);
	}

}
