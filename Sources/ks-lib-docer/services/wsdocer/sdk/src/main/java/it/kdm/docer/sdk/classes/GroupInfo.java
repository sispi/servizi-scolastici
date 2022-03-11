package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.IGroupInfo;
import it.kdm.docer.sdk.interfaces.IGroupProfileInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupInfo implements IGroupInfo {

    
    List<String> users = new ArrayList<String>();
    IGroupProfileInfo profileInfo = new GroupProfileInfo();
   
    
    public List<String> getUsers() {		
		return this.users;
	}
		
    public void setUsers(List<String> users) {
		this.users = users;		
	}

	public IGroupProfileInfo getProfileInfo() {
		// TODO Auto-generated method stub
		return profileInfo;
	}

	public void setProfileInfo(IGroupProfileInfo profileInfo) {
		this.profileInfo = profileInfo;
		
	}	
   
}
