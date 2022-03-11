package it.kdm.docer.alfresco.search;

import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.classes.LockStatus;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.webservice.types.Reference;

public class SearchResult {

    private String cmName = "";
	private DocId docId = new DocId("");
    private Reference reference = new Reference();       
    private Map<String,EnumACLRights> acl = new HashMap<String,EnumACLRights>();
    private EnumACLRights effectiveRights = EnumACLRights.undefined;
    private LockStatus lockStatus = new LockStatus();    
    private Map<String,Reference> relatedMap = new HashMap<String,Reference>();
    private Map<String,Reference> advancedVersions = new HashMap<String,Reference>();
    private Map<String,Reference> riferimentiMap = new HashMap<String,Reference>();
    private DataRow<String> dataRow = null;  
    
    public void setDataRow(DataRow<String> dataRow){
    	this.dataRow = dataRow;
    }
    
    public DataRow<String> getProfile() throws DocerException{
    	
    	if(dataRow==null){
    		throw new DocerException("Profilo (DataRow) non impostato nel SearchResult");
    	}
    	return this.dataRow;
    }
    
    public void setProfile(DataRow<String> dataRow){
    	
    	this.dataRow = dataRow;;
    }

    public String getCmName() {
		return cmName;
	}
	public void setCmName(String cmName) {
		this.cmName = cmName;
	}
	public String getDocId() {
		return docId.toString();
	}
	public void setDocId(DocId docId) {
		this.docId = docId;
	}	
	public Reference getReference() {
		return reference;
	}
	public void setReference(Reference reference) {
		this.reference = reference;
	}
	public Map<String, EnumACLRights> getAcl() {
		return acl;
	}
	public void setAcl(Map<String, EnumACLRights> acl) {
		this.acl = acl;
	}
	public EnumACLRights getEffectiveRights() {
		return effectiveRights;
	}
	public void setEffectiveRights(EnumACLRights effectiveRights) {
		this.effectiveRights = effectiveRights;
	}
	public LockStatus getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(LockStatus lockStatus) {
		this.lockStatus = lockStatus;
	}
	public Map<String, Reference> getRelatedMap() {
		return relatedMap;
	}
	public void setRelatedMap(Map<String, Reference> relatedMap) {
		this.relatedMap = relatedMap;
	}
	public Map<String, Reference> getAdvancedVersionsMap() {
		return advancedVersions;
	}
	public void setAdvancedVersionsMap(Map<String, Reference> advancedVersions) {
		this.advancedVersions = advancedVersions;
	}
	public Map<String, Reference> getRiferimentiMap() {
		return riferimentiMap;
	}
	public void setRiferimentiMap(Map<String, Reference> riferimentiMap) {
		this.riferimentiMap = riferimentiMap;
	}
//	public EnumAlfNodeType getNodeType() {
//		return nodeType;
//	}
//	public void setNodeType(EnumAlfNodeType nodeType) {
//		this.nodeType = nodeType;
//	}
}
