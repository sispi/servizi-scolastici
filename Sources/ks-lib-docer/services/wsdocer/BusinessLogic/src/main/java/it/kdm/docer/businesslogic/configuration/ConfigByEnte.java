package it.kdm.docer.businesslogic.configuration;

import it.kdm.docer.businesslogic.configuration.enums.EnumBLPropertyType;
import it.kdm.docer.businesslogic.configuration.mappings.AnagraficaTypesMapping;
import it.kdm.docer.businesslogic.configuration.mappings.DocumentTypesMapping;
import it.kdm.docer.businesslogic.configuration.types.AnagraficaType;
import it.kdm.docer.businesslogic.configuration.types.DocumentType;
import it.kdm.docer.businesslogic.configuration.types.FieldDescriptor;
import it.kdm.docer.businesslogic.utility.FascicoloUtils;
import it.kdm.docer.commons.Config;

import java.util.*;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMElement;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

public class ConfigByEnte {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private List<String> ADMINS_GROUPS = new ArrayList<String>();
    private static String VOID = "";

    private boolean CONFIG_INITIALIZED = false;
    private String CONFIG_ERROR_MESSAGE = "";
    private List<String> BASE_PROFILEz = new ArrayList<String>();
    private Map<String, FieldDescriptor> FIELDS = new HashMap<String, FieldDescriptor>();
    // private Map<String, DocumentType> DOCUMENT_TYPES = new HashMap<String,
    // DocumentType>();
    // private Map<String, AnagraficaType> ANAGRAFICHE_TYPES = new
    // HashMap<String, AnagraficaType>();

    private DocumentTypesMapping documentTypesMapping = new DocumentTypesMapping();
    private AnagraficaTypesMapping anagraficaTypesMapping = new AnagraficaTypesMapping();

    private Map<String, List<String>> hitlists = new HashMap<String, List<String>>();
    private Map<String, String> enteAooDescriptionCaching = new HashMap<String, String>();

    private String fascicoliSecondariFormat = "classifica/anno_fascicolo/progr_fascicolo";
    private String regexpFascicoliSec = "^([^/]+)/([12][0-9][0-9][0-9])/(.+)$";
    private Pattern patternFascicoliSec = Pattern.compile(regexpFascicoliSec);
    private int classificaPosition = 1;
    private int annoFascicoloPosition = 2;
    private int progrFascicoloPosition = 3;
    private boolean MIGRATION_MODE = false;

    private FascicoloUtils fascicoloUtils = new FascicoloUtils(fascicoliSecondariFormat, regexpFascicoliSec, classificaPosition, annoFascicoloPosition, progrFascicoloPosition);

    private boolean allow_record_versioning_archive = false;
    private List<String> allow_record_add_allegato = new ArrayList<String>();
    private List<String> allow_propagate_acl = new ArrayList<String>();
    private boolean allow_pubblicato_add_related = false;
    private String default_no_file = "";

	private Map<String, Map<String, String>> mapPianoClass = new HashMap<String, Map<String, String>>();
    private Map<String, Map<String, Map<String, String>>> storicoPianoClass = new HashMap<String, Map<String, Map<String, String>>>();

    public void initConfiguration(Config c) {

	try {

	    logger.debug("> lettura file di configurazione " + c.getConfigFile().getAbsolutePath());

	    CONFIG_INITIALIZED = false;
	    ADMINS_GROUPS.clear();
	    FIELDS.clear();
	    //BASE_PROFILE.clear();
	    documentTypesMapping.clear();
	    anagraficaTypesMapping.clear();
	    hitlists.clear();
	    enteAooDescriptionCaching.clear();

		// DOCER-36
		mapPianoClass.clear();
        storicoPianoClass.clear();

	    logger.debug("> fatto clear dei mappings");

	    List<OMElement> elements = c.getNodes("//configuration/group[@name='business_logic_variables']/section[@name='props']/*");
	    for (OMElement variableElem : elements) {

		if (variableElem == null)
		    continue;

		String variableName = variableElem.getLocalName();

		if (variableName.equalsIgnoreCase("admins_groups")) {

		    String adminsGroups = variableElem.getText();
		    if (adminsGroups != null) {
			String[] adminsGroupArr = adminsGroups.split(" *[,;] *");
			for (String adminGroupId : adminsGroupArr) {
			    if (StringUtils.isEmpty(adminGroupId)) {
				continue;
			    }
			    ADMINS_GROUPS.add(adminGroupId);
			    logger.debug("> aggiunto " + adminGroupId + " tra gli ADMINS_GROUPS");
			}
		    }

		    continue;
		}

		if (variableName.equalsIgnoreCase("migration_mode")) {

		    OMAttribute omAtt = variableElem.getAttribute(new QName("value"));
		    if (omAtt != null) {
			MIGRATION_MODE = Boolean.parseBoolean(omAtt.getAttributeValue());
			logger.debug("> MIGRATION_MODE: " + MIGRATION_MODE);
		    }

		    continue;
		}

		if (variableName.equalsIgnoreCase("fascicoli_secondari_mapping")) {

		    OMAttribute omAtt = variableElem.getAttribute(new QName("regexp"));
		    if (omAtt != null) {

			String appoRegex = omAtt.getAttributeValue();
			patternFascicoliSec = Pattern.compile(appoRegex);
			regexpFascicoliSec = appoRegex;
		    }

		    omAtt = variableElem.getAttribute(new QName("format"));
		    if (omAtt != null) {
			fascicoliSecondariFormat = omAtt.getAttributeValue();
		    }

		    omAtt = variableElem.getAttribute(new QName("classifica_position"));
		    if (omAtt != null) {
			classificaPosition = Integer.parseInt(omAtt.getAttributeValue().replace("$", VOID));
		    }

		    omAtt = variableElem.getAttribute(new QName("anno_fascicolo_position"));
		    if (omAtt != null) {
			annoFascicoloPosition = Integer.parseInt(omAtt.getAttributeValue().replace("$", VOID));
		    }

		    omAtt = variableElem.getAttribute(new QName("progr_fascicolo_position"));
		    if (omAtt != null) {
			progrFascicoloPosition = Integer.parseInt(omAtt.getAttributeValue().replace("$", VOID));
		    }

		    fascicoloUtils = new FascicoloUtils(fascicoliSecondariFormat, regexpFascicoliSec, classificaPosition, annoFascicoloPosition, progrFascicoloPosition);

		    logger.debug("> fascicoloUtils inizializzato: fascicoliSecondariFormat: " + fascicoliSecondariFormat + ", regexpFascicoliSec: " + regexpFascicoliSec + ", classificaPosition: "
			    + classificaPosition + ", annoFascicoloPosition: " + annoFascicoloPosition + ", progrFascicoloPosition: " + progrFascicoloPosition);

		}

         // Modifica DOCER-36 Piano di Classificazione (2015-11-10)
        if (variableName.equalsIgnoreCase("piano_class_default")) {

			boolean pianoClassEnabled = false;
			OMAttribute omAtt = variableElem.getAttribute(new QName("active"));
            if (omAtt != null) {
				pianoClassEnabled = Boolean.parseBoolean(omAtt.getAttributeValue());
            }

			String pianoClassDefault = null;
            omAtt = variableElem.getAttribute(new QName("value"));
            if (omAtt != null) {
                pianoClassDefault = omAtt.getAttributeValue();
            }

			OMAttribute omEnte = variableElem.getAttribute(new QName("ente"));
			OMAttribute omAoo = variableElem.getAttribute(new QName("aoo"));
			if (pianoClassEnabled && omEnte != null && omAoo != null) {
				String ente = omEnte.getAttributeValue();
				String aoo = omAoo.getAttributeValue();

				Map<String, String> mapEnte = mapPianoClass.get(ente);
				if (mapEnte == null) {
					mapEnte = new HashMap<String, String>();
					mapPianoClass.put(ente, mapEnte);
				}

				mapPianoClass.get(ente).put(aoo, pianoClassDefault);
				logger.debug("> PIANO_CLASS " + ente + " - " + aoo + ": " + pianoClassDefault);
			}

            continue;

        }

        if (variableName.equalsIgnoreCase("storico_piano")) {

            String anno = null;
            OMAttribute omAtt = variableElem.getAttribute(new QName("anno"));
            if (omAtt != null) {
                anno = omAtt.getAttributeValue();
            }

            String value = null;
            omAtt = variableElem.getAttribute(new QName("value"));
            if (omAtt != null) {
                value = omAtt.getAttributeValue();
            }

            OMAttribute omEnte = variableElem.getAttribute(new QName("ente"));
            OMAttribute omAoo = variableElem.getAttribute(new QName("aoo"));

            if (value!=null && anno!=null && omEnte != null && omAoo != null) {
                String ente = omEnte.getAttributeValue();
                String aoo = omAoo.getAttributeValue();

                Map<String, Map<String, String>> mapEnte = storicoPianoClass.get(ente);
                if (mapEnte == null) {
                    mapEnte = new HashMap<String, Map<String, String>>();
                    storicoPianoClass.put(ente, mapEnte);
                }

                Map<String, String> mapAoo = storicoPianoClass.get(ente).get(aoo);
                if(mapAoo == null){
                    mapAoo = new HashMap<String, String>();
                    storicoPianoClass.get(ente).put(aoo,mapAoo);
                }

                storicoPianoClass.get(ente).get(aoo).put(anno,value);
                logger.debug("> STORICO_PIANO_CLASS " + ente + " - " + aoo + ": " + storicoPianoClass.toString());
            }

            continue;

        }

                // modifica del 28/02/2013 --> i PAPER sono sempre versionabili
		// // modifica del 14/12/2012
		// if(variableName.equalsIgnoreCase("allow_record_versioning_paper")){
		//
		// OMAttribute omAtt = variableElem.getAttribute(new
		// QName("value"));
		// if(omAtt!=null){
		// allow_record_versioning_paper =
		// Boolean.parseBoolean(omAtt.getAttributeValue());
		// }
		//
		// continue;
		// }
		// modifica del 14/12/2012
		if (variableName.equalsIgnoreCase("allow_record_versioning_archive")) {

		    OMAttribute omAtt = variableElem.getAttribute(new QName("value"));
		    if (omAtt != null) {
			allow_record_versioning_archive = Boolean.parseBoolean(omAtt.getAttributeValue());
		    }

		    logger.debug("> allow_record_versioning_archive: " + allow_record_versioning_archive);

		    continue;
		}
		// modifica del 14/12/2012
		if (variableName.equalsIgnoreCase("allow_record_add_allegato")) {

		    OMAttribute omAtt = variableElem.getAttribute(new QName("value"));
		    if (omAtt != null) {
				//allow_record_add_allegato = Boolean.parseBoolean(omAtt.getAttributeValue());
				allow_record_add_allegato = Arrays.asList(omAtt.getAttributeValue().split(","));
		    }

		    logger.debug("> allow_record_add_allegato: " + allow_record_add_allegato);

		    continue;
		}

		// default template file per NO_FILE
		if (variableName.equalsIgnoreCase("default_no_file")) {

			OMAttribute omAtt = variableElem.getAttribute(new QName("value"));
			if (omAtt != null) {
				default_no_file = omAtt.getAttributeValue();
			}

			logger.debug("> default_no_file: " + default_no_file);

			continue;
		}

		if (variableName.equalsIgnoreCase("allow_propagate_acl")) {

			OMAttribute omAtt = variableElem.getAttribute(new QName("value"));
			if (omAtt != null) {
				allow_propagate_acl = Arrays.asList(omAtt.getAttributeValue().split(","));
			}

			logger.debug("> allow_propagate_acl: " + allow_propagate_acl);

			continue;
		}

		// modifica del 14/12/2012
		if (variableName.equalsIgnoreCase("allow_pubblicato_add_related")) {

		    OMAttribute omAtt = variableElem.getAttribute(new QName("value"));
		    if (omAtt != null) {
			allow_pubblicato_add_related = Boolean.parseBoolean(omAtt.getAttributeValue());
		    }

		    logger.debug("> allow_pubblicato_add_related: " + allow_pubblicato_add_related);

		    continue;
		}

	    }

	    // leggo i FIELDS
	    elements = c.getNodes("//configuration/group[@name='impianto']/section[@name='fields']/*");
	    for (OMElement propElem : elements) {

		if (propElem == null)
		    continue;

		String propName = propElem.getLocalName().toUpperCase();

		if (propName.equals(VOID))
		    continue;

		if (FIELDS.containsKey(propName)) {
		    logger.debug("FIELDS contiene gia': " + propName);
		    continue;
		}

		String format = null;
		OMAttribute omAtt = propElem.getAttribute(new QName("format"));
		if (omAtt != null) {
		    format = omAtt.getAttributeValue();
		}

		boolean multivalue = false;
		omAtt = propElem.getAttribute(new QName("multivalue"));
		if (omAtt != null) {
		    String strMV = omAtt.getAttributeValue();
		    if (strMV.equalsIgnoreCase("true"))
			multivalue = true;
		    else if (strMV.equalsIgnoreCase("false"))
			multivalue = false;
		}

		EnumBLPropertyType propertyType = EnumBLPropertyType.UNDEFINED;
		omAtt = propElem.getAttribute(new QName("type"));
		if (omAtt != null) {
		    propertyType = EnumBLPropertyType.getEnum(omAtt.getAttributeValue());
		}

		FieldDescriptor propDescriptor = new FieldDescriptor(propName, propertyType, format, multivalue);

		FIELDS.put(propName, propDescriptor);

	    }	    

	    // leggo le HITLIST
	    elements = c.getNodes("//configuration/group[@name='impianto']/section[@name='hitlists']/type");
	    for (OMElement hitlistElem : elements) {

		if (hitlistElem == null)
		    continue;

		String hitlistName = VOID;
		OMAttribute omAtt = hitlistElem.getAttribute(new QName("name"));
		if (omAtt != null) {
		    hitlistName = omAtt.getAttributeValue();
		}

		if (hitlistName == null || hitlistName.equals(VOID))
		    continue;

		hitlistName = hitlistName.toUpperCase();

		Iterator<OMElement> propIterator = hitlistElem.getChildElements();
		if (propIterator == null)
		    continue;

		List<String> list = new ArrayList<String>();

		while (propIterator.hasNext()) {

		    OMElement propElem = propIterator.next();

		    if (propElem == null)
			break;

		    String propName = propElem.getLocalName().toUpperCase();

		    if (propName.equals(VOID))
			continue;

		    // lo aggiungo solo se e' un campo definito
		    if (!FIELDS.containsKey(propName)) {
			logger.warn("WARNING Docer configuration: il campo " + propName + " non e' definito tra i FIELDS e sara' ignorato per " + hitlistName);
			if (logger.isDebugEnabled()) {
			    logger.debug("WARNING Docer configuration: il campo " + propName + " non e' definito tra i FIELDS e sara' ignorato per " + hitlistName);
			}
			// System.out.println("WARNING Docer configuration: il campo "
			// + propName +
			// " non e' definito tra i FIELDS e sara' ignorato per "
			// + hitlistName);
			continue;
		    }

		    list.add(propName);

		}

		hitlists.put(hitlistName, list);
		
	    }
	    
	    // leggo BASE PROFILE
	    elements = c.getNodes("//configuration/group[@name='impianto']/section[@name='baseprofile']/*");
	    for (OMElement propElem : elements) {

		if (propElem == null)
		    continue;

		String propName = propElem.getLocalName().toUpperCase();

		// se e' gia' contenuto skip
//		if (BASE_PROFILE.contains(propName)) {
//		    continue;
//		}

		// lo aggiungo solo se e' un campo definito
		if (!FIELDS.containsKey(propName)) {
		    logger.warn("WARNING Docer configuration: il campo " + propName + " non e' definito tra i FIELDS e sara' ignorato in BaseProfile");
		    if (logger.isDebugEnabled()) {
			logger.debug("WARNING Docer configuration: il campo " + propName + " non e' definito tra i FIELDS e sara' ignorato in BaseProfile");
		    }
		    continue;
		}

		documentTypesMapping.addBaseField(FIELDS.get(propName));
		//BASE_PROFILE.add(propName);

	    }

	    // leggo anagrafiche_types di default
	    elements = c.getNodes("//configuration/group[@name='impianto']/section[@name='anagrafiche_types']/type");
	    for (OMElement elem : elements) {

		if (elem == null)
		    continue;

		String anagraficaTypeId = elem.getAttributeValue(new QName("name"));
		if (StringUtils.isEmpty(anagraficaTypeId)) {
		    continue;
		}

		anagraficaTypeId = anagraficaTypeId.toUpperCase();

		AnagraficaType anagraficaType = anagraficaTypesMapping.addAnagraficaType(anagraficaTypeId, null, null);

		boolean isAnagraficaCustom = (!anagraficaTypeId.equals("ENTE") && !anagraficaTypeId.equals("AOO") && !anagraficaTypeId.equals("TITOLARIO") && !anagraficaTypeId.equals("FASCICOLO"));

		Iterator<OMElement> propIterator = elem.getChildElements();
		if (propIterator == null)
		    continue;

		while (propIterator.hasNext()) {

		    OMElement propElem = propIterator.next();

		    if (propElem == null)
			break;

		    String propName = propElem.getLocalName().toUpperCase();

		    if (propName.equals(VOID))
			continue;

		    FieldDescriptor field = FIELDS.get(propName);

		    // se e' un campo non definito non lo tratto
		    // if (field == null){
		    // System.out.println("WARNING Docer configuration: il campo "
		    // +propName
		    // +" non e' definito tra i FIELDS e sara' ignorato per l'anagrafica "
		    // +typeId);
		    // continue;
		    // }

		    String role = VOID;
		    OMAttribute omAtt = propElem.getAttribute(new QName("role"));
		    if (omAtt != null) {
			role = omAtt.getAttributeValue();
			if (role.equalsIgnoreCase("codice")) {
			    if (field != null) {
				field.setAnagraficaTypeId(anagraficaTypeId);
			    }
			    anagraficaType.setCodicePropName(propName);
			}
			else if (role.equalsIgnoreCase("descrizione")) {
			    anagraficaType.setDescrizionePropName(propName);
			}
		    }

		    if (field == null) {
			logger.warn("WARNING Docer configuration: il campo " + propName + " non e' definito tra i FIELDS e sara' ignorato per l'anagrafica " + anagraficaTypeId);
			if (logger.isDebugEnabled()) {
			    logger.debug("WARNING Docer configuration: il campo " + propName + " non e' definito tra i FIELDS e sara' ignorato per l'anagrafica " + anagraficaTypeId);
			}
			continue;
		    }
		    
		    anagraficaType.addFieldDescriptor(field);
		}

	    }


	    // leggo i DOCUMENT TYPES
	    elements = c.getNodes("//configuration/group[@name='impianto']/section[@name='document_types']/type");
	    for (OMElement elem : elements) {

		if (elem == null)
		    continue;

		String typeId = elem.getAttributeValue(new QName("name"));
		if (StringUtils.isEmpty(typeId))
		    continue;

		String description = elem.getAttributeValue(new QName("description"));

		String componentTypes = elem.getAttributeValue(new QName("componentTypes"));

		DocumentType documentType = documentTypesMapping.addDocumentType(typeId, description, componentTypes, null, null);

//		// aggiungo prop di base comuni a tutti i documenttype
//		for (String key : BASE_PROFILE) {
//
//		    FieldDescriptor basefield = FIELDS.get(key);
//
//		    // se e' un campo non definito non lo aggiungo
//		    if (basefield == null) {
//			logger.warn("WARNING Docer configuration: il campo " + key + " non e' definito tra i FIELDS e sara' ignorato per tipo documento " + documentType.getTypeId());
//			if (logger.isDebugEnabled()) {
//			    logger.debug("WARNING Docer configuration: il campo " + key + " non e' definito tra i FIELDS e sara' ignorato per tipo documento " + documentType.getTypeId());
//			}
//			continue;
//		    }
//
//		    documentType.addFieldDescriptor(basefield);
//		}

	    }


	    // leggo i campi custom delle ANAGRAFICHE (per AOO)
	    elements = c.getNodes("//configuration/group[@name='form_dinamiche']/section[@name='anagrafiche']/ente");
	    for (OMElement elem : elements) {

		if (elem == null)
		    continue;

		String codEnte = elem.getAttributeValue(new QName("id"));
		String codAOO = null;

		if (StringUtils.isEmpty(codEnte)) {
		    continue;
		}

		// ciclo le anagrafiche per ente
		Iterator<OMElement> anagraficaIterator = elem.getChildElements();
		if (anagraficaIterator == null)
		    continue;

		while (anagraficaIterator.hasNext()) {

		    OMElement anagrElem = anagraficaIterator.next();

		    if (anagrElem == null)
			break;

		    String anagraficaTypeId = anagrElem.getLocalName().toUpperCase();

		    if (StringUtils.isEmpty(anagraficaTypeId))
			continue;		    

		    // recupero la aoo a cui si riferiscono le proprieta'
		    // specifiche dell'anagrafica
		    Iterator<OMElement> aooIterator = anagrElem.getChildElements();
		    if (aooIterator == null)
			continue;

		    while (aooIterator.hasNext()) {

			OMElement aooElem = aooIterator.next();

			if (aooElem == null)
			    break;

			codAOO = aooElem.getAttributeValue(new QName("id"));

			if (StringUtils.isEmpty(codAOO)) {
			    continue;
			}

			AnagraficaType anagraficaType = anagraficaTypesMapping.addAnagraficaType(anagraficaTypeId, codEnte, codAOO);
			
			Iterator<OMElement> propIterator = aooElem.getChildElements();
			if (propIterator == null)
			    continue;
			while (propIterator.hasNext()) {

			    OMElement propElem = propIterator.next();
			    if (propElem == null)
				break;

			    String propName = propElem.getLocalName().toUpperCase();

			    if (propName.equals(VOID))
				continue;

			    FieldDescriptor field = FIELDS.get(propName);

			    // se non e' definito non lo aggiungo
			    if (field == null) {
				logger.warn("WARNING Docer configuration: il campo " + propName + " specificato nell'anagrafica da customizzare " + anagraficaTypeId + " per ente " + codEnte
					+ " e aoo " + codAOO + " non e' definito tra i FIELDS e sara' ignorato");
				if (logger.isDebugEnabled()) {
				    logger.debug("WARNING Docer configuration: il campo " + propName + " specificato nell'anagrafica da customizzare " + anagraficaTypeId + " per ente " + codEnte
					    + " e aoo " + codAOO + " non e' definito tra i FIELDS e sara' ignorato");
				}

				continue;
			    }

			    String customformat = null;
			    OMAttribute omAtt = propElem.getAttribute(new QName("format"));
			    if (omAtt != null) {
				customformat = omAtt.getAttributeValue();
			    }

			    // se non e' definito custom prendo il format base
			    if (customformat == null) {
				customformat = field.getFormat();
			    }

			    FieldDescriptor customField = new FieldDescriptor(propName, field.getType(), customformat, field.isMultivalue());
			    if (field.pointToAnagrafica()) {
				customField.setAnagraficaTypeId(field.getAnagraficaTypeId());
			    }

			    customField.setInheritedFromDefault(false);
			    
			    anagraficaType.addFieldDescriptor(customField);
			}

		    }

		}// chiudo while delle prop custom delle anagrafiche

	    }
	    
	    // leggo i DOCUMENT TYPES definiti per aoo ed i campi custom
	    elements = c.getNodes("//configuration/group[@name='form_dinamiche']/section[@name='documenti']/ente/aoo/documento");
	    for (OMElement elem : elements) {

		if (elem == null) {
		    continue;
		}

		String documentTypeId = elem.getAttributeValue(new QName("type"));
		if (StringUtils.isEmpty(documentTypeId)) {
		    continue;
		}

		// recupero AOO padre
		OMContainer container = elem.getParent();
		if (container == null) {
		    continue;
		}

		OMElement aooNode = (OMElement) container;
		String codAOO = aooNode.getAttributeValue(new QName("id"));
		if (StringUtils.isEmpty(codAOO)) {
		    continue;
		}

		codAOO = codAOO.toUpperCase();

		// recupero Ente padre
		container = aooNode.getParent();
		if (container == null) {
		    continue;
		}

		OMElement enteNode = (OMElement) container;
		String codEnte = enteNode.getAttributeValue(new QName("id"));
		if (StringUtils.isEmpty(codEnte)) {
		    continue;
		}

		codEnte = codEnte.toUpperCase();

		String description = elem.getAttributeValue(new QName("description"));

		String componentTypes = elem.getAttributeValue(new QName("componentTypes"));

		DocumentType documentType = documentTypesMapping.addDocumentType(documentTypeId, description, componentTypes, codEnte, codAOO);

		Iterator<OMElement> propIterator = elem.getChildElements();
		if (propIterator == null)
		    continue;

		while (propIterator.hasNext()) {

		    OMElement propElem = propIterator.next();

		    if (propElem == null)
			break;

		    String propName = propElem.getLocalName().toUpperCase();

		    if (propName.equals(VOID))
			continue;

		    FieldDescriptor field = FIELDS.get(propName);

		    // se non e' definito non lo aggiungo
		    if (field == null) {
			logger.warn("WARNING Docer configuration: il campo " + propName + " specificato per il tipo documento " + documentTypeId + " per ente " + codEnte + " e aoo " + codAOO
				+ " non e' definito tra i FIELDS e sara' ignorato");
			if (logger.isDebugEnabled()) {
			    logger.debug("WARNING Docer configuration: il campo " + propName + " specificato per il tipo documento " + documentTypeId + " per ente " + codEnte + " e aoo " + codAOO
				    + " non e' definito tra i FIELDS e sara' ignorato");
			}

			continue;
		    }

		    String customformat = null;
		    OMAttribute om = propElem.getAttribute(new QName("format"));
		    if (om != null) {
			customformat = om.getAttributeValue();
		    }
		    // se non e' definito custom prendo il format base
		    if (customformat == null) {
			customformat = field.getFormat();
		    }

		    boolean isMultivalue = field.isMultivalue();
		    String customMultivalue = null;
		    om = propElem.getAttribute(new QName("multivalue"));
		    if (om != null) {
			customMultivalue = om.getAttributeValue();
		    }
		    // se non e' definito custom prendo il format base
		    if (customMultivalue == null) {
			customMultivalue = field.getFormat();
		    }
		    else {
			isMultivalue = Boolean.valueOf(customMultivalue);
		    }

		    FieldDescriptor customField = new FieldDescriptor(propName, field.getType(), customformat, isMultivalue);
		    if (field.pointToAnagrafica()) {
			customField.setAnagraficaTypeId(field.getAnagraficaTypeId());
		    }

		    customField.setInheritedFromDefault(false);
		    
		    documentType.addFieldDescriptor(customField);
		}

	    }

	    
	    if (logger.isDebugEnabled()) {
		logger.debug("> FIELDS: " + FIELDS.keySet());
		for (FieldDescriptor fd : FIELDS.values()) {
		    logger.debug("-> " +fd.toString());
		}
	    }
	    
	    for(String key : hitlists.keySet()){
		logger.debug("> Hitlist: " + key + ": " + hitlists.get(key));
	    }	  
	    	    
	    //logger.debug("> BASE_PROFILE: " + BASE_PROFILE);

//	    logger.debug("> Anagrafiche di default: " + anagraficaTypesMapping.getAnagraficaTypes(null, null).size());
//	    if (logger.isDebugEnabled()) {
//		List<String> list = new ArrayList<String>();
//		List<AnagraficaType> ats = anagraficaTypesMapping.getAnagraficaTypes(null, null);
//		for (AnagraficaType at : ats) {
//		    list.add(at.getTypeId());
//		}
//		logger.debug("-> " +list);
//
//		for (AnagraficaType at : ats) {
//		    logger.debug("--> " + at.getTypeId());
//		    for (FieldDescriptor fd : at.getProfile().values()) {
//			logger.debug("---> " +fd.toString());
//		    }
//		}
//
//	    }

	    List<String> list = new ArrayList<String>();
	    
	    if (logger.isDebugEnabled()) {
		for (String key : anagraficaTypesMapping.getMappingForDebug().keySet()) {
		    list.clear();
		    for (AnagraficaType at : anagraficaTypesMapping.getMappingForDebug().get(key)) {
			list.add(at.getTypeId());
		    }

		    logger.debug("> Anagrafiche types per " + key + ": " + list.toString());

		    for (AnagraficaType at : anagraficaTypesMapping.getMappingForDebug().get(key)) {
			logger.debug("-> " + at.getTypeId() +", " +at.getProfile().values().size() +" props");
			for (FieldDescriptor fd : at.getProfile().values()) {
			    if(!fd.isInheritedFromDefault()){
				logger.debug("--> " +fd.toString());
			    }
			    
			}
		    }
		}
	    }

//	    logger.debug("> Document types di default: " + documentTypesMapping.getDocumentTypes(null, null).size());
//	    if (logger.isDebugEnabled()) {
//		List<DocumentType> dts = documentTypesMapping.getDocumentTypes(null, null);
//		List<String> list = new ArrayList<String>();
//		for (DocumentType dt : dts) {
//		    list.add(dt.getTypeId() + " (" + dt.getDescription() + ")");
//		}
//		logger.debug("-> " +list);
//
//		for (DocumentType dt : dts) {
//		    logger.debug("--> " + dt.getTypeId());
//		    for (FieldDescriptor fd : dt.getProfile().values()) {
//			logger.debug("---> " +fd.toString());
//		    }
//		}
//	    }

	    if (logger.isDebugEnabled()) {
		for (String key : documentTypesMapping.getMappingsForDebug().keySet()) {
		    list.clear();
		    for (DocumentType dt : documentTypesMapping.getMappingsForDebug().get(key)) {
			list.add(dt.getTypeId() + " (" + dt.getDescription() + ")");
		    }

		    logger.debug("> Document types per " + key + ": " + list.toString());
		    
		    for (DocumentType dt : documentTypesMapping.getMappingsForDebug().get(key)) {
			logger.debug("-> " + dt.getTypeId() +", " +dt.getProfile().values().size() +" props");
			for (FieldDescriptor fd : dt.getProfile().values()) {
			    if(!fd.isInheritedFromDefault()){
				logger.debug("--> " +fd.toString());
			    }				
			}
		    }
		    
		}
	    }

	    CONFIG_INITIALIZED = true;

	    logger.debug("> Configurazione inizializzata");
	    logger.debug("");

	}
	catch (Exception e) {

	    //e.printStackTrace();
	    CONFIG_INITIALIZED = false;
	    CONFIG_ERROR_MESSAGE = e.getMessage();
	}
	finally {
	    if (ADMINS_GROUPS.size() == 0) {
		ADMINS_GROUPS.add("SYS_ADMINS");
	    }
	}
    }

    public List<String> getADMINS_GROUPS() {
	return ADMINS_GROUPS;
    }

    public boolean isCONFIG_INITIALIZED() {
	return CONFIG_INITIALIZED;
    }

    public String getCONFIG_ERROR_MESSAGE() {
	return CONFIG_ERROR_MESSAGE;
    }

//    public List<String> getBASE_PROFILE() {
//	return BASE_PROFILE;
//    }

    public Map<String, FieldDescriptor> getFIELDS() {
	return FIELDS;
    }

    public DocumentTypesMapping getDocumentTypesMapping() {
	return documentTypesMapping;
    }

    public AnagraficaTypesMapping getAnagraficaTypesMapping() {
	return anagraficaTypesMapping;
    }

    public Map<String, List<String>> getHitlists() {
	return hitlists;
    }

    public Map<String, String> getEnteAooDescriptionCaching() {
	return enteAooDescriptionCaching;
    }

    public boolean isMIGRATION_MODE() {
	return MIGRATION_MODE;
    }

    public FascicoloUtils getFascicoloUtils() {
	return fascicoloUtils;
    }

    public boolean isAllow_record_versioning_archive() {
	return allow_record_versioning_archive;
    }

    public List<String> isAllow_record_add_allegato() {
	return allow_record_add_allegato;
    }

	public List<String> isAllow_propagate_acl() {
		return allow_propagate_acl;
    }

	public String getDefault_no_file() {
		return default_no_file;
	}

    public boolean isAllow_pubblicato_add_related() {
	return allow_pubblicato_add_related;
    }

	/**
	 * Restutuisce il piano di classificazione di una aoo se attivo, altrimenti null
	 * @param ente
	 * @param aoo
	 * @return
	 */
	public String getPianoClass(String ente, String aoo) {
		Map<String, String> mapEnte = mapPianoClass.get(ente);
		if (mapEnte != null)
			return mapEnte.get(aoo);

		return null;
	}

    /**
     * Restutuisce il piano di classificazione di una aoo per un determinato anno (se presente in configurazione), altrimenti null
     * @param ente
     * @param aoo
     * @param anno
     * @return
     */
    public String getStoricoPianoClass(String ente, String aoo, String anno) {
        Map<String, Map<String, String>> mapEnte = storicoPianoClass.get(ente);
        if (mapEnte != null) {
            Map<String, String> mapAoo = mapEnte.get(aoo);
            if (mapAoo != null)
                return mapAoo.get(anno);
        }

        return null;
    }

}
