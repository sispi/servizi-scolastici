package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;

public class LoggedUserInfo implements ILoggedUserInfo {
	
	private String userId;
	private String ticket;
	private String codiceEnte;
	
	public LoggedUserInfo(){
	}
	
	public LoggedUserInfo(String userId, String codiceEnte, String ticket){
	
		this.userId = userId;
		this.codiceEnte = codiceEnte;
		this.ticket = ticket;
	}
	
	public String getUserId() {
    	return this.userId;
    }
	
	public void setUserId(String userid) {
    	this.userId = userid;
    }
    
	public String getTicket() {
    	return this.ticket;
    }
	
	public void setTicket(String ticket) {
    	this.ticket = ticket;
    }  
    
	public String getCodiceEnte() {
    	return this.codiceEnte;
    }
    
	public void setCodiceEnte(String codiceEnte) {
    	this.codiceEnte = codiceEnte;
    } 
	
}
