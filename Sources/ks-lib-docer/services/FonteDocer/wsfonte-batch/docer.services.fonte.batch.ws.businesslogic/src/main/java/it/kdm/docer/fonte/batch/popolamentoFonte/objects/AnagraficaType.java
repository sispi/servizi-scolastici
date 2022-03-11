package it.kdm.docer.fonte.batch.popolamentoFonte.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnagraficaType {

    private String typeId = "";
    
	private Map<String,FieldDescriptor> baseFields = new HashMap<String,FieldDescriptor>();
	
	private Map<String,Map<String,FieldDescriptor>> customProfiles = new HashMap<String,Map<String,FieldDescriptor>>();			

    private String codicePropName = "";
    private String descrizionePropName = "";

    public AnagraficaType(String typeId) {

        this.typeId = String.valueOf(typeId).toUpperCase();
    }


    public void addBaseField(FieldDescriptor field) {

        // i propName sono impostati uppercase nei costruttori dei
        // FieldDescriptor        
        if(!baseFields.containsKey(field.getPropName())){
        	baseFields.put(field.getPropName(),field);
        	
        	for(String chiave : customProfiles.keySet()){
            	Map<String,FieldDescriptor> customProfile = customProfiles.get(chiave);        	
            	// aggiungo ai campi base deicustom
                customProfile.put(field.getPropName(), field);              
            }
        }             
        
    }
    
    private static String keytemplate = "%s/%s";
	private String composeKey(String codEnte,String codAOO){
		
		return String.format(keytemplate, codEnte.toUpperCase(), codAOO.toUpperCase());		
	}
	
    public void addCustomField(String codEnte, String codAoo, FieldDescriptor field) {

        if(codEnte==null){
            codEnte = "";
        }
        
        if(codAoo==null){
            codAoo = "";
        }        
        
        codEnte = String.valueOf(codEnte).toUpperCase();
        codAoo = String.valueOf(codAoo).toUpperCase();
        
        String chiave = composeKey(codEnte, codAoo);
        
        Map<String,FieldDescriptor> customProfile = customProfiles.get(chiave);
        
        if(customProfile==null){
        	customProfile = new HashMap<String,FieldDescriptor>();
            
        	customProfile.putAll(baseFields);
            
        	customProfiles.put(chiave, customProfile);
        }
        
        // sovrascrivo la definizione con quella custom
        customProfile.put(field.getPropName(), field);        
    }
    
    public String getTypeId() {
        return this.typeId;
    }       


    private Map<String,FieldDescriptor> getAnagraficaFields(String codEnte, String codAoo) {
        
        if(codEnte==null || codEnte.equals("") || codAoo==null || codAoo.equals("")){
            return baseFields;
        }
        
        String chiave = composeKey(codEnte, codAoo);
        
        Map<String,FieldDescriptor> customProfile = customProfiles.get(chiave);
        
        if (customProfile == null) {
            return baseFields;
        }
        
        return customProfile;
    }
    
    public FieldDescriptor getFieldDescriptor(String codEnte, String codAoo, String propName){
        
        if(propName==null || propName.equals("")){
            return null; 
        }
        
        Map<String,FieldDescriptor> fields = getAnagraficaFields(codEnte, codAoo);
        
        return fields.get(propName.toUpperCase());
    }
    
    public List<String> getFieldsNames(String codEnte, String codAoo){       
        
        if(codEnte==null || codEnte.equals("") || codAoo==null || codAoo.equals("")){
        	return new ArrayList<String>(baseFields.keySet());
        }
        
        String chiave = composeKey(codEnte, codAoo);
        
        Map<String,FieldDescriptor> customProfile = customProfiles.get(chiave);
        if(customProfile!=null){
        	return new ArrayList<String>(customProfile.keySet());
        }
        return new ArrayList<String>(baseFields.keySet());
    }

    public String getCodicePropName() {
        return codicePropName;
    }

    public void setCodicePropName(String codicePropName) {
        this.codicePropName = String.valueOf(codicePropName).toUpperCase();
    }

    public String getDescrizionePropName() {
        return descrizionePropName;
    }

    public void setDescrizionePropName(String descrizionePropName) {
        this.descrizionePropName = String.valueOf(descrizionePropName).toUpperCase();
    }

    public boolean isAnagraficaCustom() {

        if (this.codicePropName == null || this.codicePropName.equals(""))
            return false;

        if (this.descrizionePropName == null || this.descrizionePropName.equals(""))
            return false;

        return true;
    }

}
