package it.kdm.sign.exceptions;

public class ListaDocumentiException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public ListaDocumentiException(){
		super();
	}
	public ListaDocumentiException(String message){
		super(message);
	}
}

