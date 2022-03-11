package it.kdm.docer.sdk.classes;


import java.util.Date;

import it.kdm.docer.sdk.interfaces.IHistoryItem;

public class HistoryItem implements IHistoryItem{

	Date date = new Date();
	String user = "";
	String description = "";
	
	public Date getDate() {		
		return this.date;
	}
    
	public void setDate(Date date) {
		this.date = date;		
	}	

	
	public String getUser() {		
		return this.user;
	}
    
	public void setUser(String user) {
		this.user = user;		
	}	
	
	public String getDescription() {		
		return this.description;
	}
    
	public void setDescription(String description) {
		this.description = description;		
	}	
	
	
}
