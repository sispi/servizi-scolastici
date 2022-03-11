package it.kdm.docer.businesslogic.configuration.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class AnagraficaType {

    private String typeId = "";

    // public solo per test
    private List<String> fieldsNames = new ArrayList<String>();

    // public solo per test
    private Map<String,FieldDescriptor> profile = new HashMap<String,FieldDescriptor>();

    private String codicePropName = "";
    private String descrizionePropName = "";

    public AnagraficaType(String typeId) {

        this.typeId = String.valueOf(typeId).toUpperCase();
    }

    public Map<String,FieldDescriptor> getProfile(){
	return profile;
    }

    public void copyFrom(AnagraficaType anagraficaType){
	this.typeId = anagraficaType.getTypeId();	
	this.codicePropName = anagraficaType.getCodicePropName();
	this.descrizionePropName = anagraficaType.getDescrizionePropName();
	this.profile.putAll(anagraficaType.getProfile());
    }
    
    public void addFieldDescriptor(FieldDescriptor fieldDescriptor) {

        // i propName sono impostati uppercase nei costruttori dei
        // FieldDescriptor        
        profile.put(fieldDescriptor.getPropName(),fieldDescriptor);
        if(!fieldsNames.contains(fieldDescriptor.getPropName())){
	    fieldsNames.add(fieldDescriptor.getPropName());    
	}
    }

    public FieldDescriptor getFieldDescriptor(String propName) {

	if(StringUtils.isEmpty(propName)){
	    return null;
	}
	
        return profile.get(propName.toUpperCase());        	
    }
	
    public String getTypeId() {
        return this.typeId;
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

    public List<String> getFieldsNames(){
	return fieldsNames;
    }
    
    public AnagraficaType doACopy(){
	AnagraficaType copy = new AnagraficaType(this.typeId);
	copy.profile.putAll(this.profile);
	copy.fieldsNames.addAll(this.fieldsNames);
	copy.codicePropName = this.codicePropName;
	copy.descrizionePropName = this.descrizionePropName;
	    
	return copy;
    }
}
