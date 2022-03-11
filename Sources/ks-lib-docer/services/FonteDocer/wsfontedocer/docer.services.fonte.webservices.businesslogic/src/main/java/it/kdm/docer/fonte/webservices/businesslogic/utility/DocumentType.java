package it.kdm.docer.fonte.webservices.businesslogic.utility;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DocumentType {
	
	private String typeId = "";	
	private String description = "";	
	
	// public solo per i test
	public Map<String,FieldDescriptor> baseFields = new HashMap<String,FieldDescriptor>();
	
	// public solo per i test
	public Map<String,Map<String,FieldDescriptor>> customProfiles = new HashMap<String,Map<String,FieldDescriptor>>();			
	
	public DocumentType(String typeId, String description){	
		
		this.typeId = String.valueOf(typeId).toUpperCase();		
		this.description = String.valueOf(description);		
	}
	
	public void addBaseField(FieldDescriptor field){
		
		if(baseFields.containsKey(field.getPropName())){
			return;
		}
		
		baseFields.put(field.getPropName(), field);		
	}	
	
	public String getTypeId(){
		return this.typeId;
	}
	public String getDescription(){
		return this.description;
	}
	
	private static String keytemplate = "%s/%s";
	private String composeKey(String codEnte,String codAOO){
		
		return String.format(keytemplate, codEnte.toUpperCase(), codAOO.toUpperCase());		
	}
	
	public void setDefinedForAOO(String codEnte, String codAOO){
	
		if(codEnte==null || codAOO == null)
			return;
				
		String enteAookey = composeKey(codEnte,codAOO);
		
		Map<String,FieldDescriptor> customProfile = customProfiles.get(enteAookey);
						
		if(customProfile==null){
			customProfile = new HashMap<String, FieldDescriptor>();
			customProfiles.put(enteAookey, customProfile);
			customProfile.putAll(baseFields);			
		}				
	}
	
	public boolean isDefinedForAOO(String codEnte, String codAOO){
		
		if(codEnte==null || codAOO == null)
			return false;
		
		String enteAookey = composeKey(codEnte,codAOO);
		
		return customProfiles.containsKey(enteAookey);
	}
	
	public void addCustomField(String codEnte, String codAOO, FieldDescriptor field) throws Exception{
		
		if(codEnte==null || codAOO == null)
			return;
		
		String enteAookey = composeKey(codEnte,codAOO);
		
		Map<String,FieldDescriptor> customProfile = customProfiles.get(enteAookey);
		
		if(customProfile==null){
			throw new Exception("Errore configurazione: " +this.typeId +" non definito per COD_ENTE/COD_AOO " +enteAookey );
		}
		
		field.setInheritedFromBase(false);
		// sovrascrivo la definizione del campo su base aoo (ogni aoo ha la propria regex)
		customProfile.put(field.getPropName(), field);		
				
	}
	
	
	public FieldDescriptor getFieldDescriptor(String codEnte, String codAOO, String fieldName){

		fieldName = String.valueOf(fieldName); 
				
		String enteAookey = composeKey(codEnte, codAOO);
		
		Map<String,FieldDescriptor> customProfile = customProfiles.get(enteAookey);
				
		if(customProfile!=null){
			
			return customProfile.get(fieldName);
		}
		
		return null;
	}

	
	public List<String> getFieldsNames(String codEnte, String codAOO){

		
		String enteAookey = composeKey(codEnte, codAOO);
		
		Map<String,FieldDescriptor> customProfile = customProfiles.get(enteAookey);
		if(customProfile!=null){
			return new ArrayList<String>(customProfile.keySet());			
		}
		
		return new ArrayList<String>(baseFields.keySet());
	}

}
