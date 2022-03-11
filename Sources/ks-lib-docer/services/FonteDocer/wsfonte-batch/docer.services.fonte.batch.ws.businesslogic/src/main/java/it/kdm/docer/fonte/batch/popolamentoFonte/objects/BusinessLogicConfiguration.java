package it.kdm.docer.fonte.batch.popolamentoFonte.objects;

import it.kdm.docer.commons.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMElement;

public class BusinessLogicConfiguration {

	public boolean isMIGRATION_MODE() {
		return MIGRATION_MODE;
	}

	public List<String> getBASE_PROFILE() {
		return BASE_PROFILE;
	}

	public Map<String, FieldDescriptor> getFIELDS() {
		return FIELDS;
	}

	public Map<String, DocumentType> getDOCUMENT_TYPES() {
		return DOCUMENT_TYPES;
	}

	public Map<String, AnagraficaType> getANAGRAFICHE_TYPES() {
		return ANAGRAFICHE_TYPES;
	}

	public Map<String, List<String>> getHITLISTS() {
		return HITLISTS;
	}

	public String getRegexpFascicoliSec() {
		return regexpFascicoliSec;
	}

	public Pattern getPatternFascicoliSec() {
		return patternFascicoliSec;
	}

	public int getClassificaPosition() {
		return classificaPosition;
	}

	public int getAnnoFascicoloPosition() {
		return annoFascicoloPosition;
	}

	public int getProgrFascicoloPosition() {
		return progrFascicoloPosition;
	}

	public boolean isAllow_record_versioning_archive() {
		return allow_record_versioning_archive;
	}

	public boolean isAllow_record_add_allegato() {
		return allow_record_add_allegato;
	}

	public boolean isAllow_pubblicato_add_related() {
		return allow_pubblicato_add_related;
	}

	private static List<String> BASE_PROFILE = new ArrayList<String>();
	private static Map<String, FieldDescriptor> FIELDS = new HashMap<String, FieldDescriptor>();
	private static Map<String, DocumentType> DOCUMENT_TYPES = new HashMap<String, DocumentType>();
	private static Map<String, AnagraficaType> ANAGRAFICHE_TYPES = new HashMap<String, AnagraficaType>();
	private static Map<String, List<String>> HITLISTS = new HashMap<String, List<String>>();

	private static String regexpFascicoliSec = "^([^/]+)/([12][0-9][0-9][0-9])/(.+)$";
	private static Pattern patternFascicoliSec = Pattern.compile(regexpFascicoliSec);
	private static int classificaPosition = 1;
	private static int annoFascicoloPosition = 2;
	private static int progrFascicoloPosition = 3;
	private static boolean MIGRATION_MODE = true;

	private static boolean allow_record_versioning_archive = false;
	private static boolean allow_record_add_allegato = false;
	private static boolean allow_pubblicato_add_related = false;

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	private String configPath = "confstub.xml";

	private static String ID_FONTE = "ID_FONTE";
	private static String RACC_UID = "RACC_UID";
	private static String NEEDS_RACCOGLITORE_UPDATE = "NEEDS_RACCOGLITORE_UPDATE";

	private static String VOID = "";

	private static Config c = null;

	public void writeConfig(String xml) throws IOException, XMLStreamException {

		if (c == null) {
			c = new Config();
		}

		c.writeConfig(xml);
	}

	public String readConfig() throws XMLStreamException, IOException {

		if (c == null) {
			c = new Config();
		}

		return c.readConfig();
	}

	public void initConfiguration() throws Exception {

		try {

			if (c == null) {
				c = new Config();
			}

			File configFile = new File(configPath);

			if (configFile == null || !configFile.exists())
				throw new Exception("Config file non trovato: " + configFile.getAbsolutePath());

			c.setConfigFile(configFile);

			BASE_PROFILE.clear();
			FIELDS.clear();
			DOCUMENT_TYPES.clear();
			ANAGRAFICHE_TYPES.clear();
			HITLISTS.clear();

			List<OMElement> elements = c.getNodes("//configuration/group[@name='business_logic_variables']/section[@name='props']/*");
			for (OMElement variableElem : elements) {

				if (variableElem == null)
					continue;

				String variableName = variableElem.getLocalName();

				if (variableName.equalsIgnoreCase("migration_mode")) {

					OMAttribute omAtt = variableElem.getAttribute(new QName("value"));
					if (omAtt != null) {
						MIGRATION_MODE = Boolean.parseBoolean(omAtt.getAttributeValue());
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

					continue;
				}
				// modifica del 14/12/2012
				if (variableName.equalsIgnoreCase("allow_record_add_allegato")) {

					OMAttribute omAtt = variableElem.getAttribute(new QName("value"));
					if (omAtt != null) {
						allow_record_add_allegato = Boolean.parseBoolean(omAtt.getAttributeValue());
					}

					continue;
				}

				// modifica del 14/12/2012
				if (variableName.equalsIgnoreCase("allow_pubblicato_add_related")) {

					OMAttribute omAtt = variableElem.getAttribute(new QName("value"));
					if (omAtt != null) {
						allow_pubblicato_add_related = Boolean.parseBoolean(omAtt.getAttributeValue());
					}

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

			//			FIELDS.put(ID_FONTE, fdIdFonte);
			//			FIELDS.put(NEEDS_RACCOGLITORE_UPDATE, fdNeedsRaccoglitoreUpdate);
			//			FIELDS.put(RACC_UID, fdRaccUid);

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
						continue;
					}

					list.add(propName);

				}

				HITLISTS.put(hitlistName, list);
			}

			// leggo BASE PROFILE
			elements = c.getNodes("//configuration/group[@name='impianto']/section[@name='baseprofile']/*");
			for (OMElement propElem : elements) {

				if (propElem == null)
					continue;

				String propName = propElem.getLocalName().toUpperCase();

				// se e' gia' contenuto skip
				if (BASE_PROFILE.contains(propName)) {
					continue;
				}

				// lo aggiungo solo se e' un campo definito
				if (!FIELDS.containsKey(propName)) {
					continue;
				}

				BASE_PROFILE.add(propName);

			}

			// leggo i DOCUMENT TYPES
			elements = c.getNodes("//configuration/group[@name='impianto']/section[@name='document_types']/type");
			for (OMElement elem : elements) {

				if (elem == null)
					continue;

				String typeId = elem.getAttributeValue(new QName("name"));
				if (typeId == null || typeId.equals(VOID))
					continue;

				String description = elem.getAttributeValue(new QName("description"));

				DocumentType documentType = new DocumentType(typeId.toUpperCase(), description);

				// aggiungo prop di base comuni a tutti i documenttype
				for (String key : BASE_PROFILE) {

					FieldDescriptor basefield = FIELDS.get(key);

					// se e' un campo non e' definito non lo aggiungo
					if (basefield == null)
						continue;

					documentType.addBaseField(basefield);
				}

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
					if (field == null)
						continue;

					documentType.addEligibleField(field);
				}

				DOCUMENT_TYPES.put(documentType.getTypeId(), documentType);
			}

			// leggo i DOCUMENT TYPES definiti per aoo ed i campi custom
			elements = c.getNodes("//configuration/group[@name='form_dinamiche']/section[@name='documenti']/ente/aoo/documento");
			for (OMElement elem : elements) {

				if (elem == null)
					continue;

				String typeId = elem.getAttributeValue(new QName("type"));
				if (typeId == null || typeId.equals(VOID))
					continue;

				// recupero AOO padre
				OMContainer container = elem.getParent();
				if (container == null)
					continue;

				OMElement aooNode = (OMElement) container;
				String codAOO = aooNode.getAttributeValue(new QName("id"));
				if (codAOO == null || codAOO.equals(VOID))
					continue;

				codAOO = codAOO.toUpperCase();

				// recupero Ente padre
				container = aooNode.getParent();
				if (container == null)
					continue;

				OMElement enteNode = (OMElement) container;
				String codEnte = enteNode.getAttributeValue(new QName("id"));
				if (codEnte == null || codEnte.equals(VOID))
					continue;

				codEnte = codEnte.toUpperCase();

				DocumentType documentType = DOCUMENT_TYPES.get(typeId.toUpperCase());

				if (documentType == null) {
					// throw new DocerException("type_id: " + typeId +
					// " non definito in configurazione");
					continue;
				}

				documentType.setDefinedForAOO(codEnte, codAOO);

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
					if (field == null)
						continue;

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

					documentType.addCustomField(codEnte, codAOO, customField);
				}

				// DOCUMENT_TYPES.put(documentType.getTypeId(), documentType);
			}

			// leggo anagrafiche_types
			elements = c.getNodes("//configuration/group[@name='impianto']/section[@name='anagrafiche_types']/type");
			for (OMElement elem : elements) {

				if (elem == null)
					continue;

				String typeId = elem.getAttributeValue(new QName("name"));
				if (typeId == null || typeId.equals(VOID))
					continue;

				typeId = typeId.toUpperCase();

				AnagraficaType anagraficaType = new AnagraficaType(typeId);

				boolean isAnagraficaCustom = (!typeId.equals("ENTE") && !typeId.equals("AOO") && !typeId.equals("TITOLARIO") && !typeId.equals("FASCICOLO"));

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
					if (field == null)
						continue;

					String role = VOID;
					OMAttribute omAtt = propElem.getAttribute(new QName("role"));
					if (omAtt != null) {
						role = omAtt.getAttributeValue();
						if (role.equalsIgnoreCase("codice")) {
							field.setAnagraficaTypeId(typeId);
							anagraficaType.setCodicePropName(propName);
						}
						else if (role.equalsIgnoreCase("descrizione")) {
							anagraficaType.setDescrizionePropName(propName);
						}
					}

					anagraficaType.addBaseField(field);
				}

				if (isAnagraficaCustom) {
					// la aggiungiamo solo se vediamo che e' una definizione
					// completa
					if (anagraficaType.getCodicePropName() == null || anagraficaType.getCodicePropName().equals(VOID)) {
						continue;
					}
					if (anagraficaType.getDescrizionePropName() == null || anagraficaType.getDescrizionePropName().equals(VOID)) {
						continue;
					}
				}

				ANAGRAFICHE_TYPES.put(anagraficaType.getTypeId(), anagraficaType);
			}

			// leggo i campi custom delle ANAGRAFICHE (per AOO)
			elements = c.getNodes("//configuration/group[@name='form_dinamiche']/section[@name='anagrafiche']/ente");
			for (OMElement elem : elements) {

				if (elem == null)
					continue;

				String codEnte = elem.getAttributeValue(new QName("id"));

				if (codEnte == null || codEnte.equals(VOID)) {
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

					if (anagraficaTypeId.equals(VOID))
						continue;

					AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(anagraficaTypeId);

					if (anagraficaType == null)
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

						String codAoo = aooElem.getAttributeValue(new QName("id"));

						if (codAoo == null || codAoo.equals(VOID)) {
							continue;
						}

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
							if (field == null)
								continue;

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

							anagraficaType.addCustomField(codEnte, codAoo, customField);
						}

					}

				}// chiudo while delle prop custom delle anagrafiche

			}

		}
		catch (Exception e) {
			throw new Exception("BusinessLogicConfiguration.initConfiguration(): " + e.getMessage());
		}
	}

}
