package it.kdm.docer.businesslogic.configuration.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class DocumentType {

    private String typeId = "";
    private String description = "";

    private String componentTypes = "";
    
    private List<String> fieldsNames = new ArrayList<String>();

    private Map<String, FieldDescriptor> profile = new HashMap<String, FieldDescriptor>();

    public DocumentType(String typeId, String description) {
        this.typeId = String.valueOf(typeId).toUpperCase();
        this.description = String.valueOf(description);
    }

    public DocumentType(String typeId, String description, String componentTypes) {
        this.typeId = String.valueOf(typeId).toUpperCase();
        this.description = String.valueOf(description);
        this.componentTypes = componentTypes;
    }

    public String getDescription() {
        return this.description;
    }

    public String getComponentTypes() {
        return componentTypes;
    }

    public Map<String, FieldDescriptor> getProfile() {
	return profile;
    }

    public void copyFrom(DocumentType documentType) {

	if(StringUtils.isEmpty(this.description)){
	    this.typeId = documentType.getDescription();    
	}

	this.profile.putAll(documentType.getProfile());
    }

    public void addFieldDescriptor(FieldDescriptor fieldDescriptor) {	
	
	profile.put(fieldDescriptor.getPropName(), fieldDescriptor);
        if(!fieldsNames.contains(fieldDescriptor.getPropName())){
            fieldsNames.add(fieldDescriptor.getPropName());
        }
    }

    public FieldDescriptor getFieldDescriptor(String propName) {

	if (StringUtils.isEmpty(propName)) {
	    return null;
	}

	return profile.get(propName.toUpperCase());
    }

    public String getTypeId() {
	return this.typeId;
    }

    public List<String> getFieldsNames() {	
	return fieldsNames;
    }
    
    public DocumentType doACopy(){
        DocumentType copy = new DocumentType(this.typeId, this.description, this.componentTypes);
        copy.profile.putAll(this.profile);
        copy.fieldsNames.addAll(this.fieldsNames);
        return copy;
    }
}
