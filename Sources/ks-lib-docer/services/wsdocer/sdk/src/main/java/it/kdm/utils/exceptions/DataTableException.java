package it.kdm.utils.exceptions;

public class DataTableException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5496664847986461615L;

	
	public DataTableException(String message) {
		super(message);
	}
	
	public DataTableException(Throwable t) {
		super(t);
	}
}
