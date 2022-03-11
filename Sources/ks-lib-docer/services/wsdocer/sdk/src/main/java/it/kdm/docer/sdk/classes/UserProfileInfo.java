package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.IUserProfileInfo;

import java.util.HashMap;
import java.util.Map;

public class UserProfileInfo implements IUserProfileInfo {

    String userId = null;
    String fullName = null;        
    String primaryGroupId = null;
  
    String firstName = null;
    String lastName = null;
    String networkAlias = null;
    String password = null;
    String emailAddress = null;
    
    EnumBoolean enabled = EnumBoolean.UNSPECIFIED;

    Map<String, String> extraInfo = new HashMap<String, String>();
    

	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getFullName() {
		return this.fullName ;
	}

	public void setFullName(String fullname) {
		this.fullName = fullname;
	}
	
//	public String getPrimaryGroupId() {
//		return primaryGroupId;
//	}
//	
//	public void setPrimaryGroupId(String primaryGroupId) {
//		this.primaryGroupId = primaryGroupId;		
//	}
	
	

	
	public String getEmailAddress() {
		return this.emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;	
	}
	
	public String getFirstName() {
		return this.firstName ;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getNetworkAlias() {
		return this.networkAlias;
	}
	
	public void setNetworkAlias(String networkAlias) {
		this.networkAlias = networkAlias;
	}
		
	public String getUserPassword() {
	return password;
	}
	public void setUserPassword(String userPassword) {
		this.password = userPassword;		
	}
  
    public EnumBoolean getEnabled() {
    	return this.enabled;
    }
    
    public void setEnabled(EnumBoolean enabled) {
    	this.enabled = enabled;
    }

 
    public Map<String, String> getExtraInfo() {
		return this.extraInfo;
	}

	public void setExtraInfo(Map<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}

	
    
}
