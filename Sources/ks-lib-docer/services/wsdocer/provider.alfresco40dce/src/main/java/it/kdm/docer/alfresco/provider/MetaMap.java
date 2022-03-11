package it.kdm.docer.alfresco.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MetaMap {

	private Map<String, MetaProperty> businessLogicMetaProps = new HashMap<String,MetaProperty>();
//	private Map<String, MetaProperty> alfrescoFullnameMetaProps = new HashMap<String,MetaProperty>();
//	private Map<String, MetaProperty> alfrescoShortnameMetaProps = new HashMap<String,MetaProperty>();

	private Map<String, String> alfrescoFullnameMapping = new HashMap<String,String>();
	
	private Map<String, String> alfrescoShortNameMapping = new HashMap<String,String>();
	
	public Collection<MetaProperty> getAllMetaProperties(){
	    return businessLogicMetaProps.values();	    	
	}
	
	public void putMetaProperty(MetaProperty metaProp){
		String blName = metaProp.getBlPropName().toUpperCase();
	    businessLogicMetaProps.put(blName, metaProp);
//	    alfrescoFullnameMetaProps.put(metaProp.getAlfFullPropName(), metaProp);
//	    alfrescoShortnameMetaProps.put(metaProp.getAlfShortPropName(), metaProp);
	    alfrescoFullnameMapping.put(metaProp.getAlfFullPropName(), blName);
	    alfrescoShortNameMapping.put(metaProp.getAlfShortPropName(), blName);
	}
	
    public MetaProperty getMetaPropertyFromBusinessLogicName(String blName){ 
        if(blName==null){
            return null;
        }
        return businessLogicMetaProps.get(blName.toUpperCase());       
    }

	public MetaProperty getMetaPropertyFromAlfrescoFullName(String alfFullname){	
	    if(alfFullname==null){
            return null;
        }	    
	    return businessLogicMetaProps.get(alfrescoFullnameMapping.get(alfFullname));
	}
		
	public MetaProperty getMetaPropertyFromAlfrescoShortName(String alfShortname){        
	    if(alfShortname==null){
            return null;
        }

	    return businessLogicMetaProps.get(alfrescoShortNameMapping.get(alfShortname));
    }
	
	public void clear(){
		businessLogicMetaProps.clear();
		alfrescoFullnameMapping.clear();
		alfrescoShortNameMapping.clear();
	}
	
}

