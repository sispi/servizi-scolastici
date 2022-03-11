package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.IGroupProfileInfo;

import java.util.HashMap;
import java.util.Map;

public class GroupProfileInfo implements IGroupProfileInfo {

	String groupId = null;
    String groupName = null;        
    String parentGroupId = null;   
    EnumBoolean enabled = EnumBoolean.UNSPECIFIED;
    Map<String, String> extraInfo = new HashMap<String, String>();

    
    public String getGroupId() {
    	return this.groupId;
    }
    
    public void setGroupId(String groupId) {
    	this.groupId = groupId;
    }
    
    public String getGroupName() {
    	return this.groupName;
    }
    
    public void setGroupName(String groupname) {
    	this.groupName = groupname;
    }    
    
    public EnumBoolean getEnabled() {
    	return this.enabled;
    }
    
    public void setEnabled(EnumBoolean enabled) {
    	this.enabled = enabled;
    }
    
    public String getParentGroupId() {
    	return this.parentGroupId;
    }
    
    public void setParentGroupId(String parentGroupId) {
    	this.parentGroupId = parentGroupId;
    }  
    
	public Map<String, String> getExtraInfo() {
		return this.extraInfo;
	}

	public void setExtraInfo(Map<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}
   
}
