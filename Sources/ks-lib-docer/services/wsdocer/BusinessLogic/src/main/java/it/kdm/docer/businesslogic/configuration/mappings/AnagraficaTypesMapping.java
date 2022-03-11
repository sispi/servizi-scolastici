package it.kdm.docer.businesslogic.configuration.mappings;

import it.kdm.docer.businesslogic.configuration.types.AnagraficaType;
import it.kdm.docer.businesslogic.configuration.types.DocumentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

public class AnagraficaTypesMapping {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    // mappa chiave by aoo -> anagrafica type
    private Map<String, List<AnagraficaType>> mapping = new HashMap<String, List<AnagraficaType>>();

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

    public AnagraficaType addAnagraficaType(String typeId, String codEnte, String codAoo) {

	if (typeId == null) {
	    typeId = "";
	}

	typeId = typeId.toUpperCase();

	String keyByAoo = buildKey(codEnte, codAoo);

	List<AnagraficaType> anagraficaTypesByAoo = mapping.get(keyByAoo);

	if (anagraficaTypesByAoo == null) {
	    // se e' la prima volta che customizzo un'anagrafica per la aoo 
	    // copio tutte le anagrafiche di default
	    List<AnagraficaType> listDefault = mapping.get(keyDefault);
	    if (listDefault != null) {
		List<AnagraficaType> copy = new ArrayList<AnagraficaType>();
		for(AnagraficaType at : listDefault){
		    copy.add(at.doACopy());
		}		
		anagraficaTypesByAoo = copy;
	    }
	    else {
		// se non sono state definite anagrafiche di default 
		anagraficaTypesByAoo = new ArrayList<AnagraficaType>();
	    }

	    mapping.put(keyByAoo, anagraficaTypesByAoo);
	}

	AnagraficaType atByAoo = null;
	for (AnagraficaType at : anagraficaTypesByAoo) {
	    if (at.getTypeId().equals(typeId)) {
		atByAoo = at;
		break;
	    }
	}

	if (atByAoo == null) {
	    atByAoo = new AnagraficaType(typeId);
	    anagraficaTypesByAoo.add(atByAoo);
	}

	return atByAoo;
    }

    public AnagraficaType get(String typeId, String codEnte, String codAoo) {

	if (typeId == null) {
	    typeId = "";
	}

	typeId = typeId.toUpperCase();

	List<AnagraficaType> anagraficaTypesByAoo = getAnagraficaTypes(codEnte, codAoo);

	AnagraficaType atByAoo = null;

	if (anagraficaTypesByAoo != null) {
	    for (AnagraficaType at : anagraficaTypesByAoo) {
		if (at.getTypeId().equals(typeId)) {
		    atByAoo = at;
		    break;
		}
	    }
	}

	return atByAoo;
    }

    public List<AnagraficaType> getAnagraficaTypes(String codEnte, String codAoo) {

	String keyByAoo = buildKey(codEnte, codAoo);

	List<AnagraficaType> list = mapping.get(keyByAoo);
	if(list==null){
	    list = mapping.get(keyDefault);	    
	    mapping.put(keyByAoo, list);
	}
	
	return list;
    }

    public Map<String, List<AnagraficaType>> getMappingForDebug() {
	return mapping;
    }

}
