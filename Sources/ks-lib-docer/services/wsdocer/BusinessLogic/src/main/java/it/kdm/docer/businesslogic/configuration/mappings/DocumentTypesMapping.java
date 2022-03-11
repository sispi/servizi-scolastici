package it.kdm.docer.businesslogic.configuration.mappings;

import it.kdm.docer.businesslogic.configuration.types.AnagraficaType;
import it.kdm.docer.businesslogic.configuration.types.DocumentType;
import it.kdm.docer.businesslogic.configuration.types.FieldDescriptor;
import it.kdm.docer.sdk.exceptions.DocerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

public class DocumentTypesMapping {
    
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    // mappa chiave by aoo -> document type
    private Map<String, List<DocumentType>> mapping = new HashMap<String, List<DocumentType>>();

    private List<FieldDescriptor> baseProfile = new ArrayList<FieldDescriptor>();

    public void addBaseField(FieldDescriptor field) throws DocerException {
        
		for(FieldDescriptor baseField : baseProfile){
			if(baseField.getPropName().equalsIgnoreCase(field.getPropName())){
				return;
			}
		}

		baseProfile.add(field);
    }

    
    private String buildKey(String ente, String aoo) {

		if (StringUtils.isEmpty(ente) || StringUtils.isEmpty(aoo)) {
			return "default";
		}

		return ente.toUpperCase() + "/" + aoo.toUpperCase();
    }

    private String keyDefault = buildKey("", "");

    public void clear() {
		mapping.clear();
    }

    public DocumentType addDocumentType(String typeId, String description, String componentTypes, String codEnte, String codAoo) throws DocerException {

		if (typeId == null) {
			typeId = "";
		}

		typeId = typeId.toUpperCase();

		String keyByAoo = buildKey(codEnte, codAoo);

		List<DocumentType> documentTypesByAoo = mapping.get(keyByAoo);

		if (documentTypesByAoo == null) {
			// se e' la prima volta che aggiungo un'document per la aoo copio la definizione di
			// default se esiste
			List<DocumentType> listDefault = mapping.get(keyDefault);
			if (listDefault != null) {
				List<DocumentType> copy = new ArrayList<DocumentType>();
				for(DocumentType dt : listDefault){
					copy.add(dt.doACopy());
				}
				documentTypesByAoo = copy;
			}
			else {
				documentTypesByAoo = new ArrayList<DocumentType>();
			}

			mapping.put(keyByAoo, documentTypesByAoo);
		}

		DocumentType dtByAoo = null;
		for (DocumentType dt : documentTypesByAoo) {
			if (dt.getTypeId().equals(typeId)) {
				dtByAoo = dt;
				break;
			}
		}

		// se non esiste una definizione per aoo e nemmeno un default
		if (dtByAoo == null) {
			// creo una nuova definizione
			dtByAoo = new DocumentType(typeId, description, componentTypes);

			// aggiungo i campi base
			// facciamo una copia perche' potrebbe essere fatto l'override
			for(FieldDescriptor baseField : baseProfile){
				dtByAoo.addFieldDescriptor(baseField.copy());
			}

			documentTypesByAoo.add(dtByAoo);
		}

		return dtByAoo;
    }

    public DocumentType get(String typeId, String codEnte, String codAoo) {

		if (typeId == null) {
			typeId = "";
		}

		typeId = typeId.toUpperCase();

		List<DocumentType> documentTypesByAoo = getDocumentTypes(codEnte, codAoo);

		DocumentType dtByAoo = null;

		if (documentTypesByAoo != null) {
			for (DocumentType dt : documentTypesByAoo) {
				if (dt.getTypeId().equalsIgnoreCase(typeId)) {
					dtByAoo = dt;
					break;
				}
			}
		}

		return dtByAoo;
    }

    public List<DocumentType> getDocumentTypes(String codEnte, String codAoo) {
	
		String keyByAoo = buildKey(codEnte, codAoo);

		List<DocumentType> list = mapping.get(keyByAoo);
		if (list==null){
			list = mapping.get(keyDefault);
			if(list==null){
				list = new ArrayList<DocumentType>();
			}
			mapping.put(keyByAoo, list);
		}

		return list;
    }

    public Map<String, List<DocumentType>> getMappingsForDebug() {

		return mapping;
    }

}
