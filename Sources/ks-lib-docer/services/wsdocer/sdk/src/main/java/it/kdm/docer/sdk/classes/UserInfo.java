package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.IUserInfo;
import it.kdm.docer.sdk.interfaces.IUserProfileInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfo implements IUserInfo {

    List<String> groups = new ArrayList<String>();
    IUserProfileInfo profileInfo = new UserProfileInfo();

    public List<String> getGroups() {		
		return this.groups;
	}
		
    public void setGroups(List<String> groups) {
		this.groups = groups;		
	}

	public IUserProfileInfo getProfileInfo() {
		// TODO Auto-generated method stub
		return profileInfo;
	}

	public void setProfileInfo(IUserProfileInfo profileInfo) {
		this.profileInfo = profileInfo;
		
	}	
 

	
    
}
