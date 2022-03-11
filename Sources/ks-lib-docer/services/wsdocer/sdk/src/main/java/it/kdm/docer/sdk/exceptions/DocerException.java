package it.kdm.docer.sdk.exceptions;

public class DocerException extends Exception {
 
	private static final long serialVersionUID = 4893269847651712489L;
	
	long errNumber = 0;
	String errDescription = "";
	
	public DocerException(String message) {
		super(message);
		this.errNumber = -1;
		errDescription = message;
	}
	
	public DocerException(Throwable t) {
		super(t);
		this.errNumber = -1;
		errDescription = t.getMessage();
	}
	
	public DocerException(String message, Throwable t) {
		super(message, t);
		this.errNumber = -1;
	}
	 
	public DocerException(long ErrNumber, String ErrDescription) 
	{
		super(ErrDescription);
	    this.errNumber = ErrNumber;
	    this.errDescription = ErrDescription;
	}
	 
	// getErrNumber
	public long getErrNumber() {
 		return this.errNumber;
 	}
 	
 	// setErrNumber
 	public void setErrNumber(long errNum) {
 		this.errNumber = errNum;
 	}
 	
	public String getErrDescription() {
 		return this.errDescription + " [" + errNumber + "]";
 	}
 	
 	// setErrNumber
 	public void setErrDescription(String description) {
 		this.errDescription = description;
 	}
 	
 	
	
}
