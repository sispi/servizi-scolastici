package it.kdm.docer.alfresco.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.webservice.types.Reference;
import org.w3c.dom.Element;

import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.classes.LockStatus;
import it.kdm.utils.DataTable;

public class GetDocumentsWebScriptResult {
      
    private int count = 0;
    private Map<String,Reference> docidToReferenceMap = new HashMap<String, Reference>();
    private Map<String,Reference> folderidToReferenceMap = new HashMap<String, Reference>();
    private List<Reference> referenceList = new ArrayList<Reference>();
    
    private DataTable<String> orderedRequestedPropertiesResults = new DataTable<String>();
    private Map<Integer,List<Element>> orderedAllPropertiesResults = new HashMap<Integer, List<Element>>();
    private Map<Reference,Map<String,EnumACLRights>> aclsMap = new HashMap<Reference, Map<String,EnumACLRights>>();
    private Map<Reference,EnumACLRights> effectiveRightsMap = new HashMap<Reference, EnumACLRights>();
    private Map<Reference,LockStatus> lockStatusMap = new HashMap<Reference, LockStatus>();    
    private Map<Reference,Map<String,Reference>> relatedMap = new HashMap<Reference, Map<String,Reference>>();
    private Map<Reference,Map<String,Reference>> advancedVersionMap = new HashMap<Reference, Map<String,Reference>>();
    private Map<Reference,Map<String,Reference>> riferimentiMap = new HashMap<Reference, Map<String,Reference>>();
      
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }    
    
    public Map<String, Reference> getDocidToReferenceMap() {
        return docidToReferenceMap;
    }     
    
    public Map<String, Reference> getFolderidToReferenceMap() {
        return folderidToReferenceMap;
    }
    
    public List<Reference> getResultReferenceList() {
        return referenceList;
    }
    
    public DataTable<String> getOrderedRequestedPropertiesResult() {
        return orderedRequestedPropertiesResults ;
    }
    public void setOrderedRequestedPropertiesResult(DataTable<String> orderedRequestedPropertiesResults) {
        this.orderedRequestedPropertiesResults  = orderedRequestedPropertiesResults;
    }
    
    public Map<Integer, List<Element>> getAllResultsOrderedMap() {
        return orderedAllPropertiesResults;
    }
    public void addToAllResultsOrderedMap(List<Element> allProperties) {
        this.orderedAllPropertiesResults.put(this.orderedAllPropertiesResults.size(), allProperties);
    }
    

    public Map<String, EnumACLRights> getACLS(Reference nodeRef) {
        Map<String, EnumACLRights> acls = aclsMap.get(nodeRef);
        if(acls==null){
            return new HashMap<String, EnumACLRights>();
        }        
        return acls;
    }    
    public void addACLS(Reference nodeRef, Map<String, EnumACLRights> acls) {
        aclsMap.put(nodeRef, acls);
    }
    
    public LockStatus getLockStatus(Reference nodeRef) {
        LockStatus lockStatus = lockStatusMap.get(nodeRef);
        if(lockStatus==null){
            return new LockStatus();
        }        
        return lockStatus;
    }
    
    public void addLockStatus(Reference nodeRef, LockStatus status) {
        lockStatusMap.put(nodeRef, status);
    }
    
    
    public EnumACLRights getEffectiveRights(Reference nodeRef) {
        EnumACLRights effectiveRights = effectiveRightsMap.get(nodeRef);
        if(effectiveRights==null){
            return EnumACLRights.undefined;
        }
        
        return effectiveRights;
    }    
    public void addEffectiveRights(Reference nodeRef, EnumACLRights effectiveRights) {
        effectiveRightsMap.put(nodeRef, effectiveRights);
    }
    
    
    public Map<String, Reference> getRelated(Reference nodeRef) {
        Map<String, Reference> related = relatedMap.get(nodeRef);
        if(related==null){
            return new HashMap<String, Reference>();
        }
        
        return related;
    }
    
    public void addRelated(Reference nodeRef, Map<String, Reference> related) {
        relatedMap.put(nodeRef, related);
    }
    
      
    
    
    public Map<String, Reference> getAdvancedVersions(Reference nodeRef) {
        Map<String, Reference> advancedVersions = advancedVersionMap.get(nodeRef);
        if(advancedVersions==null){
            return new HashMap<String, Reference>();
        }
        
        return advancedVersions;
    }
    
    public void addAdvancedVersions(Reference nodeRef, Map<String, Reference> advancedVersions) {
        advancedVersionMap.put(nodeRef, advancedVersions);
    }
        
    
    public Map<String, Reference> getRiferimenti(Reference nodeRef) {
        Map<String, Reference> riferimenti = riferimentiMap.get(nodeRef);
        if(riferimenti==null){
            return new HashMap<String, Reference>();
        }
        
        return riferimenti;
    }
    
    public void addRiferimenti(Reference nodeRef, Map<String, Reference> riferimenti) {
        riferimentiMap.put(nodeRef, riferimenti);
    }
    
}