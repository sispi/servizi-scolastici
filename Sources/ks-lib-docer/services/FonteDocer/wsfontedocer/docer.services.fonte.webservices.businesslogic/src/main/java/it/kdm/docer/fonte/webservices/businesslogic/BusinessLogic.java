package it.kdm.docer.fonte.webservices.businesslogic;

import it.kdm.docer.commons.Config;
import it.kdm.docer.fonte.webservices.businesslogic.utility.AnagraficaType;
import it.kdm.docer.fonte.webservices.businesslogic.utility.DocumentType;
import it.kdm.docer.fonte.webservices.businesslogic.utility.EnumBLPropertyType;
import it.kdm.docer.fonte.webservices.businesslogic.utility.FieldDescriptor;
import it.kdm.docer.sdk.Constants;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.EnumStatiConservazione;
import it.kdm.docer.sdk.EnumStatiPantarei;
import it.kdm.docer.sdk.EnumStatoArchivistico;
import it.kdm.docer.sdk.EnumTipiComponente;
import it.kdm.docer.sdk.ProviderManager;
import it.kdm.docer.sdk.classes.*;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.*;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMElement;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class BusinessLogic {

    // static RecordManager recordManager = recordManager;

    private static String SLASH = "/";
    private static String VOID = "";

    private static String ERROR_MESSAGE_CREATE_AOO = "Verificare i campi obbligatori (COD_ENTE, COD_AOO, DES_AOO)";
    private static String ERROR_MESSAGE_CREATE_TITOLARIO = "Verificare i campi obbligatori (COD_ENTE, COD_AOO, CLASSIFICA, DES_TITOLARIO)";
    private static String ERROR_MESSAGE_CREATE_FASCICOLO = "Verificare i campi obbligatori (COD_ENTE, COD_AOO, CLASSIFICA, PROGR_FASCICOLO, ANNO_FASCICOLO, DES_FASCICOLO)";

    // // private static String USER = "USER";
    // // private static String GROUP = "GROUP";
    //
    private static boolean CONFIG_INITIALIZED = false;
    private static String CONFIG_ERROR_MESSAGE = "";

    private int PRIMARYSEARCH_MAX_ROWS = 1000;

    private it.kdm.docer.sdk.IProvider provider = null;

    protected static List<String> BASE_PROFILE = new ArrayList<String>();
    protected static Map<String, FieldDescriptor> FIELDS = new HashMap<String, FieldDescriptor>();
    protected static Map<String, DocumentType> DOCUMENT_TYPES = new HashMap<String, DocumentType>();
    protected static Map<String, AnagraficaType> ANAGRAFICHE_TYPES = new HashMap<String, AnagraficaType>();
    protected static Map<String, List<String>> HITLISTS = new HashMap<String, List<String>>();
    private static Map<String, String> enteAooDescriptionCaching = new HashMap<String, String>();

    private static String regexpFascicoliSec = "^([^/]+)/([12][0-9][0-9][0-9])/(.+)$";
    private static Pattern patternFascicoliSec = Pattern.compile(regexpFascicoliSec);
    private static int classificaPosition = 1;
    private static int annoFascicoloPosition = 2;
    private static int progrFascicoloPosition = 3;
    private static boolean MIGRATION_MODE = true;

    private static boolean allow_record_versioning_archive = false;
    private static boolean allow_record_add_allegato = false;
    private static boolean allow_pubblicato_add_related = false;

    TicketFormatter ticketFormatter;

    // private static String configPath = "confstub.xml";
    // private static Config c = null;

    private static String URL = "URL";
    private static String ARCHIVE = "ARCHIVE";
    private static String PAPER = "PAPER";

    private static String ID_FONTE = "ID_FONTE";
    private static String RACC_UID = "RACC_UID";
    private static String NEEDS_RACCOGLITORE_UPDATE = "NEEDS_RACCOGLITORE_UPDATE";

    private static FieldDescriptor fdIdFonte;
    private static FieldDescriptor fdRaccUid;
    private static FieldDescriptor fdNeedsRaccoglitoreUpdate;

    private static String RACC_UID_FAS_PREFIX = "FAS-";
    private static String RACC_UID_DOC_PREFIX = "DOC-";

    public BusinessLogic(String providerName, int pimarySearchMaxRows) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    initConfiguration();
	    // recordManager = recordManager;
	}

	// provider verso il quale si deve effettiare la login
	this.provider = ProviderManager.create(providerName);
	this.PRIMARYSEARCH_MAX_ROWS = pimarySearchMaxRows;

	this.ticketFormatter = new TicketFormatter();
    }

    // private static BusinessLogicConfiguration blc = new
    // BusinessLogicConfiguration();
    //
    // public static void setConfigPath(String path) {
    // blc.setConfigPath(path);
    // }

    private static String configPath = "confstub.xml";
    private static Config c = null;

    public static void setConfigPath(String path) {
	configPath = path;
    }

    // CONFIGURAZIONE SEMPLIFICATA PER LA FONTE DOCER
    private void initConfiguration() {

	try {

	    File configFile = new File(configPath);

	    if (configFile == null || !configFile.exists())
		throw new DocerException("Config file non trovato: " + configFile.getAbsolutePath());

	    c = new Config();

	    c.setConfigFile(configFile);

	    CONFIG_INITIALIZED = false;

	    if (fdIdFonte == null) {
		fdIdFonte = new FieldDescriptor(ID_FONTE, EnumBLPropertyType.STRING, ".*", false);
	    }
	    if (fdRaccUid == null) {
		fdRaccUid = new FieldDescriptor(RACC_UID, EnumBLPropertyType.STRING, ".*", false);
	    }
	    if (fdNeedsRaccoglitoreUpdate == null) {
		fdNeedsRaccoglitoreUpdate = new FieldDescriptor(NEEDS_RACCOGLITORE_UPDATE, EnumBLPropertyType.BOOLEAN, ".*", false);
	    }

	    BASE_PROFILE.clear();
	    FIELDS.clear();
	    DOCUMENT_TYPES.clear();
	    ANAGRAFICHE_TYPES.clear();
	    HITLISTS.clear();
	    enteAooDescriptionCaching.clear();

	    List<OMElement> elements = c.getNodes("//configuration/group[@name='business_logic_variables']/section[@name='props']/*");
	    for (OMElement variableElem : elements) {

		if (variableElem == null)
		    continue;

		String variableName = variableElem.getLocalName();

		MIGRATION_MODE = true; // per la FONTE e' sempre migrazione

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

	    // aggiungo i FIELDS necessari alla Fonte
	    FIELDS.put(RACC_UID, fdRaccUid);
	    FIELDS.put(ID_FONTE, fdIdFonte);
	    FIELDS.put(NEEDS_RACCOGLITORE_UPDATE, fdNeedsRaccoglitoreUpdate);

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

		// aggiungo RACC_UID nelle hitlist di documenti e fascicoli per
		// restituirlo con le ricerche
		if (hitlistName.equalsIgnoreCase("HITLIST") || hitlistName.equalsIgnoreCase("HITLIST_FASCICOLO")) {
		    list.add(RACC_UID);
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

	    // aggiungo i campi della FONTE al profilo base dei documenti
	    BASE_PROFILE.add(RACC_UID);
	    BASE_PROFILE.add(ID_FONTE);
	    BASE_PROFILE.add(NEEDS_RACCOGLITORE_UPDATE);

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

		// i campi elegibili servono solo al Front-end
		// Iterator<OMElement> propIterator = elem.getChildElements();
		// if (propIterator == null)
		// continue;
		//
		// while (propIterator.hasNext()) {
		//
		// OMElement propElem = propIterator.next();
		//
		// if (propElem == null)
		// break;
		//
		// String propName = propElem.getLocalName().toUpperCase();
		//
		// if (propName.equals(VOID))
		// continue;
		//
		// FieldDescriptor field = FIELDS.get(propName);
		//
		// // se non e' definito non lo aggiungo
		// if (field == null)
		// continue;
		//
		// documentType.addEligibleField(field);
		// }

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

		// aggiungo i campi del fascicolo
		if (anagraficaType.getTypeId().equalsIgnoreCase("FASCICOLO")) {
		    anagraficaType.addBaseField(fdRaccUid);
		    anagraficaType.addBaseField(fdIdFonte);
		    anagraficaType.addBaseField(fdNeedsRaccoglitoreUpdate);
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

	    CONFIG_INITIALIZED = true;
	}
	catch (Exception e) {

	    e.printStackTrace();
	    CONFIG_INITIALIZED = false;
	    CONFIG_ERROR_MESSAGE = e.getMessage();

	}
    }

    public boolean writeConfig(String token, String xml) throws DocerException {

	// if(!CONFIG_INITIALIZED){
	// throw new DocerException("Configurazione non inizializzata: "
	// +CONFIG_ERROR_MESSAGE);
	// }

	try {
	    // NEL WRITECONFIG NON DEVE ESSERE FATTA LA VALIDAZIONE DEL TOKEN
	    // if (token == null || token.equals(VOID))
	    // throw new DocerException("token null");
	    //
	    // LoggedUserInfo loginUserInfo = parseToken(token);
	    //
	    // if (!provider.verifyTicket(loginUserInfo.getUserId(),
	    // loginUserInfo.getCodiceEnte(), loginUserInfo.getTicket())) {
	    // throw new DocerException("token non valido");
	    // }

	    if (xml != null && !xml.equals(VOID)) {
		// il file puo' viaggiare criptato e non posso leggerlo come
		// fosse in chiaro
		// validateNextConfigurationXml(xml);
		c.writeConfig(xml);
	    }

	    initConfiguration();

	    return true;
	}
	catch (Exception err) {
	    throw new DocerException(500, "writeConfig: " + err.getMessage());
	}
    }

    public String readConfig(String token) throws DocerException {

	try {

	    // NEL READ CONFIG NON DEVE ESSERE VERIFICATO IL TOKEN
	    // if (token == null || token.equals(VOID))
	    // throw new DocerException("token null");
	    //
	    // LoggedUserInfo loginUserInfo = parseToken(token);
	    //
	    // if (!provider.verifyTicket(loginUserInfo.getUserId(),
	    // loginUserInfo.getCodiceEnte(), loginUserInfo.getTicket())) {
	    // throw new DocerException("token non valido");
	    // }

	    return c.readConfig();
	}
	// catch (DocerException docEx) {
	//
	// throw new DocerException(500, "readConfig :" + docEx.getMessage());
	//
	// }
	catch (Exception err) {
	    throw new DocerException(500, "readConfig :" + err.getMessage());
	}
    }

    private LoggedUserInfo parseToken(String token) throws DocerException {
	Ticket tokenObject = this.ticketFormatter.parseTicket(token);

	String codiceEnte = tokenObject.getCodiceEnte();
	String userid = tokenObject.getUserid();
	String dmsticket = tokenObject.getDmsTicket();

	LoggedUserInfo loginUserInfo = new LoggedUserInfo();
	loginUserInfo.setCodiceEnte(codiceEnte);
	loginUserInfo.setTicket(dmsticket);
	loginUserInfo.setUserId(userid);

	return loginUserInfo;
    }

    // ************* METODI PUBBLICI ***********/

    // 40
    public String login(String codiceEnte, String userid, String password) throws DocerException {

	try {

	    if (userid == null || userid.equals(VOID))
		throw new DocerException("userid non specificata");
	    if (password == null || password.equals(VOID))
		throw new DocerException("password non specificata");

	    // effettuo login
	    String dmsticket = provider.login(userid, password, codiceEnte);

	    return this.ticketFormatter.generateTicket(new Ticket(codiceEnte, userid, dmsticket));

	}
	catch (DocerException docEx) {

	    // if(MIGRATION_MODE)
	    throw new DocerException(440, docEx.getMessage());

	    // throw new DocerException(440, "Autenticazione fallita.");

	}
	catch (Exception err) {
	    // if(MIGRATION_MODE)
	    throw new DocerException(540, err.getMessage());

	    // throw new DocerException(440, "Autenticazione fallita.");
	}
    }

    // 41
    public void logout(String token) throws DocerException {

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto i parametri dell'utente
	    provider.setCurrentUser(loginUserInfo);

	    // eseguo logout
	    provider.logout();

	}
	catch (DocerException docEx) {

	    throw new DocerException(441, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(541, err.getMessage());
	}
    }

    // 42
    public String createDocument(String token, Map<String, String> metadata, InputStream file) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	metadata = toUCMap(metadata);

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");
	    if (metadata == null)
		throw new DocerException("metadata null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // pulisco il profilo del documento dai campi descrizione
	    // cleanDocumentProfile(metadata);

	    EnumTipiComponente tipoComponente = getEnumTipiComponente(metadata.get(Constants.doc_tipo_componente));

	    String docnum = metadata.get(Constants.doc_docnum);

	    String codEnte = metadata.get(Constants.doc_cod_ente);
	    String codAOO = metadata.get(Constants.doc_cod_aoo);

	    String docname = metadata.get(Constants.doc_docname);
	    String documentTypeId = metadata.get(Constants.doc_type_id);

	    if (docnum == null || docnum.equals(VOID) || documentTypeId == null || documentTypeId.equals(VOID) || docname == null || docname.equals(VOID) || codEnte == null || codEnte.equals(VOID)
		    || codAOO == null || codAOO.equals(VOID)) {
		throw new DocerException("DOCNUM, TYPE_ID, DOCNAME, COD_ENTE, COD_AOO obbligatori");
	    }

	    documentTypeId = documentTypeId.toUpperCase();

	    // sostituiamo il valore uppercase
	    metadata.put(Constants.doc_type_id, documentTypeId);

	    DocumentType docType = DOCUMENT_TYPES.get(documentTypeId);

	    if (docType == null) {
		throw new DocerException("type_id sconosciuto: " + documentTypeId);
	    }

	    if (!docType.isDefinedForAOO(codEnte, codAOO))
		throw new DocerException("Document Type " + documentTypeId + " non e' definito per Ente " + codEnte + " e AOO " + codAOO);

	    FieldDescriptor fd = null;
	    // controllo se i campi specificati appartengono al documenttype
	    for (String str : metadata.keySet()) {

		fd = docType.getFieldDescriptor(codEnte, codAOO, str);

		if (fd == null)
		    throw new DocerException("il campo " + str + " non appartiene al tipo " + documentTypeId);

		// controllo lunghezza e formato dei metadati
		// metadata.put(str, fd.checkValueFormat(metadata.get(str)));
	    }
	    //
	    // // controllo i metadati obbligatori e vietati
	    // checkPropertiesCreateDocument(metadata);

	    // controllo Ente
	    checkEnte(codEnte); // codice_ente e' obbligatorio
	    // controllo AOO
	    checkAOO(codAOO, codEnte); // codice_aoo e' obbligatorio

	    checkFascicoloPrincipale(metadata);

	    checkFascicoliSecondari(metadata);

	    // controllo di esistenza delle anagrafiche custom
	    checkAnagraficheCustom(codEnte, codAOO, metadata);

	    // controllo l'esistenza delle anagrafiche dei Multivalues
	    checkAnagraficheMultivalue(codEnte, codAOO, docType, metadata);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put(Constants.doc_docnum, Arrays.asList(docnum));
	    DataTable<String> dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("DOCNUM"), -1, null);
	    if (dt.getRows().size() == 0) {

		metadata.put(RACC_UID, RACC_UID_DOC_PREFIX + docnum);
		String docId = provider.createDocument(docType.getTypeId(), metadata, file);
		if (!docnum.equals(docId)) {
		    throw new DocerException("mancata corrispondenza tra docnum migrato e docnum del sistema documentale: " + docnum + "<>" + docId);
		}

	    }

	    return docnum;
	}
	catch (DocerException docEx) {

	    throw new DocerException(442, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(542, err.getMessage());
	}
    }

    // 43
    public void updateProfileDocument(String token, String docId, Map<String, String> metadata) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	metadata = toUCMap(metadata);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (metadata == null)
		throw new DocerException("metadata null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    docId = docId.replaceAll("^" + RACC_UID_DOC_PREFIX, "");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // pulisco il profilo del documento dai campi descrizione
	    // cleanDocumentProfile(metadata);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    // impostazione dell'unico criterio di ricerca
	    List<String> criteria = new ArrayList<String>();
	    criteria.add(docId);
	    searchCriteria.put(Constants.doc_docnum, criteria);

	    // devo confrontare tutti i metadati del protocollo
	    // List<String> returnProperties =
	    // SCHEMASDEFINITIONS.getSchemaDefinition(PROTOCOLLO_SCHEMAID);
	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(Constants.doc_type_id);
	    returnProperties.add(Constants.doc_cod_ente);
	    returnProperties.add(Constants.doc_cod_aoo);
	    returnProperties.add(Constants.doc_stato_archivistico);
	    returnProperties.add(Constants.doc_docname);
	    returnProperties.add(Constants.doc_tipo_componente);
	    returnProperties.add(Constants.doc_docnum);
	    // returnProperties.add(Constants.doc_stato_pantarei);
	    // returnProperties.add(Constants.doc_archive_type);
	    // returnProperties.add(Constants.doc_pubblicazione_pubblicato);
	    // returnProperties.add(Constants.doc_docnum_record);
	    // returnProperties.add(Constants.doc_app_id);
	    // returnProperties.add(Constants.doc_docnum_princ);
	    // returnProperties.add(Constants.doc_creation_date);
	    // returnProperties.add(Constants.doc_stato_conserv);

	    // RICERCA PRIMARIA
	    DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    // deve esserci un solo risultato perche' devo modificare un solo
	    // documento
	    if (searchResults.getRows().size() == 0)
		throw new DocerException("documento non trovato nel repository documentale del WS Fonte");
	    if (searchResults.getRows().size() != 1)
		throw new DocerException("docId non univoco nel repository documentale del WS Fonte");

	    // HO TROVATO IL DOCUMENTO -> un solo risultato
	    DataRow<String> oldProfileData = searchResults.getRow(0);

	    String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
	    String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);

	    if (oldCodEnte == null || oldCodEnte.equals(VOID))
		throw new DocerException("Errore: documento " + docId + " senza COD_ENTE assegnato");

	    if (oldCodAOO == null || oldCodAOO.equals(VOID))
		throw new DocerException("Errore: documento " + docId + " senza COD_AOO assegnato");

	    String oldClassifica = oldProfileData.get(Constants.doc_classifica);
	    String oldAnnoFascicolo = oldProfileData.get(Constants.doc_anno_fascicolo);
	    String oldProgrFascicolo = oldProfileData.get(Constants.doc_progr_fascicolo);

	    // documentType NON puo' essere null (e' dato obbligatorio di
	    // creazione)
	    String oldTypeId = oldProfileData.get(Constants.doc_type_id);

	    if (oldTypeId == null || oldTypeId.equals(VOID))
		throw new DocerException("TYPE_ID non assegnato al documento nel sistema: " + docId);

	    oldTypeId = oldTypeId.toUpperCase();

	    DocumentType oldDocType = DOCUMENT_TYPES.get(oldTypeId);

	    if (oldDocType == null) {
		throw new DocerException("documento " + docId + " con type_id sconosciuto: " + oldTypeId);
	    }

	    DocumentType docType = oldDocType;

	    // in Docer 1.2 e' permesso il cambio di TYPE_ID
	    String newTypeId = metadata.get(Constants.doc_type_id);
	    if (newTypeId != null) {
		newTypeId = newTypeId.toUpperCase();

		if (newTypeId.equals(VOID)) {
		    throw new DocerException("non e' possibile modificare TYPE_ID in stringa vuota");
		}

		// sostituiamo il valore uppercase
		metadata.put(Constants.doc_type_id, newTypeId);

		DocumentType newDocType = DOCUMENT_TYPES.get(newTypeId);

		if (newDocType == null) {
		    throw new DocerException("nuovo TYPE_ID del documento sconosciuto: " + newTypeId);
		}

		if (!newDocType.isDefinedForAOO(oldCodEnte, oldCodAOO))
		    throw new DocerException("TYPE_ID " + newTypeId + " non e' definito per Ente " + oldCodEnte + " e AOO " + oldCodAOO);

		docType = newDocType;
	    }

	    FieldDescriptor fd = null;
	    // controllo se i campi specificati appartengono al documenttype
	    for (String key : metadata.keySet()) {

		fd = docType.getFieldDescriptor(oldCodEnte, oldCodAOO, key);

		if (fd == null) {
		    throw new DocerException("il campo " + key + " non appartiene al tipo " + docType.getTypeId());
		}

		// controllo lunghezza e formato dei metadati
		// metadata.put(key, fd.checkValueFormat(metadata.get(key)));
	    }

	    // controllo dei campi
	    // checkPropertiesUpdateProfile(oldProfileData, metadata);

	    // manageStatoConservazione(oldProfileData, metadata);

	    // controlliamo il docname: spesso rimandano erroneamente anche
	    // l'estensione
	    String oldDocName = oldProfileData.get(Constants.doc_docname);
	    String oldExtension = getExtension(oldDocName);

	    if (metadata.containsKey(Constants.doc_docname)) {

		String newDocName = metadata.get(Constants.doc_docname);
		if (newDocName == null || newDocName.equals(VOID)) {
		    throw new DocerException("non e' possibile assegnare il campo " + Constants.doc_docname + " null o vuoto");
		}

		String newExtension = getExtension(newDocName);

		if (!newExtension.equalsIgnoreCase(oldExtension)) {
		    throw new DocerException("non e' possibile modificare la parte estensione nel campo " + Constants.doc_docname + ": utilizzare versionamento avanzato");
		}
	    }

	    checkFascicoloPrincipale(metadata);

	    checkFascicoliSecondari(metadata);

	    // controllo di esistenza delle anagrafiche custom
	    checkAnagraficheCustom(oldCodEnte, oldCodAOO, metadata);
	    // controllo l'esistenza delle anagrafiche dei Multivalues
	    checkAnagraficheMultivalue(oldCodEnte, oldCodAOO, docType, metadata);

	    EnumTipiComponente oldTipoComponente = getEnumTipiComponente(oldProfileData.get(Constants.doc_tipo_componente));
	    EnumTipiComponente tipoComponente = getEnumTipiComponente(metadata.get(Constants.doc_tipo_componente));

	    if (tipoComponente == null) {
		tipoComponente = oldTipoComponente;
	    }

	    // metadata.put(RACC_UID, RACC_UID_DOC_PREFIX + docId);
	    metadata.remove(RACC_UID);

	    // aggiorno il profilo del documento principale
	    provider.updateProfileDocument(docId, metadata);

	}
	catch (DocerException docEx) {

	    throw new DocerException(443, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(543, err.getMessage());
	}
    }

    // 46
    public void addRelated(String token, String docId, List<String> toAddRelated) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    if (toAddRelated == null)
		throw new DocerException("lista related null");

	    docId = docId.replaceAll("^" + RACC_UID_DOC_PREFIX, "");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // il profilo deve essere sempre presente
	    if (toAddRelated.size() < 1)
		return;

	    List<String> addingList = new ArrayList<String>();

	    for (String rel : toAddRelated) {
		// pulisco la collezione rimuovendo i vuoti, i doppioni e il
		// master
		if (rel == null || rel.equals(VOID) || addingList.contains(rel) || rel.equals(docId))
		    continue;

		addingList.add(rel);
	    }

	    // evitiamo controlli inutili...
	    if (addingList.size() == 0)
		return;

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    // impostazione dell'unico criterio di ricerca
	    List<String> criteria = new ArrayList<String>();
	    criteria.add(docId);
	    searchCriteria.put(Constants.doc_docnum, criteria);

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(Constants.doc_docnum);
	    // returnProperties.add(Constants.doc_docnum_princ);
	    // returnProperties.add(Constants.doc_cod_ente);
	    // returnProperties.add(Constants.doc_cod_aoo);
	    // returnProperties.add(Constants.doc_stato_archivistico);
	    // returnProperties.add(Constants.doc_stato_pantarei);
	    // returnProperties.add(Constants.doc_tipo_componente);
	    // returnProperties.add(Constants.doc_classifica);
	    // returnProperties.add(Constants.doc_cod_titolario);
	    // returnProperties.add(Constants.doc_progr_fascicolo);
	    // returnProperties.add(Constants.doc_num_fascicolo);
	    // returnProperties.add(Constants.doc_anno_fascicolo);
	    // returnProperties.add(Constants.doc_fascicoli_secondari);
	    // returnProperties.add(Constants.doc_archive_type);
	    // returnProperties.add(Constants.doc_pubblicazione_pubblicato);

	    // test della library

	    // RICERCA PRIMARIA
	    DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    // deve esserci un solo risultato perche' devo modificare un solo
	    // documento
	    if (searchResults.getRows().size() == 0)
		throw new DocerException("documento " + docId + " non trovato nel repository del WS Fonte ");
	    if (searchResults.getRows().size() != 1)
		throw new DocerException("docId " + docId + " non univoco nel repository del WS Fonte ");

	    // HO TROVATO IL DOCUMENTO -> un solo risultato
	    DataRow<String> oldProfileData = searchResults.getRow(0);

	    // aggiungo i related
	    provider.addRelatedDocuments(docId, addingList);

	}
	catch (DocerException docEx) {

	    throw new DocerException(446, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(546, err.getMessage());
	}

    }

    // 47
    public void removeRelated(String token, String docId, List<String> toRemoveRelated) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    if (toRemoveRelated == null)
		throw new DocerException("lista related null");

	    docId = docId.replaceAll("^" + RACC_UID_DOC_PREFIX, "");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    List<String> removingList = new ArrayList<String>();

	    for (String remElement : toRemoveRelated) {

		if (remElement == null || remElement.equals(VOID))
		    continue;

		if (remElement.equals(docId))
		    continue;

		if (removingList.contains(remElement))
		    continue;

		removingList.add(remElement);
	    }

	    if (removingList.size() < 1) {
		return;
	    }

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    // impostazione dell'unico criterio di ricerca
	    List<String> criteria = new ArrayList<String>();
	    criteria.add(docId);
	    searchCriteria.put(Constants.doc_docnum, criteria);

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(Constants.doc_docnum);
	    returnProperties.add(Constants.doc_tipo_componente);
	    // returnProperties.add(Constants.doc_stato_archivistico);
	    // returnProperties.add(Constants.doc_stato_pantarei);

	    // test della library

	    // RICERCA PRIMARIA
	    DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    // deve esserci un solo risultato perche' devo modificare un solo
	    // documento
	    if (searchResults.getRows().size() == 0)
		throw new DocerException("documento " + docId + " non trovato nel repository documentale del WS Fonte");
	    if (searchResults.getRows().size() != 1)
		throw new DocerException("docId " + docId + " non univoco nel repository documentale del WS Fonte");

	    // ILockStatus checkedOutInfo =
	    // provider.isCheckedOutDocument(docId);
	    // if (checkedOutInfo.getLocked()) {
	    // throw new
	    // DocerException("documento in stato di blocco esclusivo");
	    // }

	    // HO TROVATO IL DOCUMENTO -> un solo risultato
	    DataRow<String> oldProfileData = searchResults.getRow(0);

	    docId = oldProfileData.get(Constants.doc_docnum);

	    // rimuovo i related
	    provider.removeRelatedDocuments(docId, removingList);

	}
	catch (DocerException docEx) {

	    throw new DocerException(447, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(547, err.getMessage());
	}

    }

    // 48
    public Map<String, String> getProfileDocument(String token, String docId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    docId = docId.replaceAll("^" + RACC_UID_DOC_PREFIX, "");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    // impostazione dell'unico criterio di ricerca
	    List<String> criteria = new ArrayList<String>();
	    criteria.add(docId);
	    searchCriteria.put(Constants.doc_docnum, criteria);

	    // richiedo tutte le proprieta'
	    List<String> returnProperties = new ArrayList<String>(FIELDS.keySet());
	    // test

	    // RICERCA PRIMARIA
	    DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    // deve esserci un solo risultato perche' devo modificare un solo
	    // documento
	    if (searchResults.getRows().size() == 0)
		throw new DocerException("documento non trovato");
	    if (searchResults.getRows().size() != 1)
		throw new DocerException("docId non univoco");

	    // HO TROVATO IL DOCUMENTO -> un solo risultato
	    DataRow<String> profileData = searchResults.getRow(0);

	    String codEnte = profileData.get(Constants.doc_cod_ente);
	    String codAOO = profileData.get(Constants.doc_cod_aoo);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("ERRORE: documento " + docId + " senza COD_ENTE assegnato");

	    // if (codAOO == null || codAOO.equals(VOID))
	    // throw new DocerException("ERRORE: documento " + docId
	    // + " senza COD_AOO assegnato");
	    if (codAOO == null) {
		codAOO = VOID;
	    }

	    // String documentTypeId = profileData.get(Constants.doc_type_id);
	    //
	    // if (documentTypeId == null || documentTypeId.equals(VOID))
	    // throw new DocerException("ERRORE: documento " + docId +
	    // " senza TYPE_ID assegnato");
	    //
	    // documentTypeId = documentTypeId.toUpperCase();
	    //
	    // DocumentType docType = DOCUMENT_TYPES.get(documentTypeId);
	    //
	    // if (docType == null) {
	    //
	    // throw new DocerException("type_id assegnato al documento " +
	    // docId + " sconosciuto: " + documentTypeId);
	    // }

	    // Map<String, String> profile = new HashMap<String, String>();
	    //
	    // FieldDescriptor fd = null;
	    // // ciclo le proprieta' e restituisco solo quelle definite nel
	    // docType
	    // for(String fieldName : returnProperties){
	    // fd = docType.getFieldDescriptor(codEnte, codAOO, fieldName);
	    // if(fd==null)
	    // continue; // prop non presente nel docType
	    //
	    // profile.put(fieldName, profileData.get(fieldName));
	    // }

	    // inserisco le descrizioni delle anagrafiche
	    return formatProfile(profileData);

	}
	catch (DocerException docEx) {

	    throw new DocerException(448, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(548, err.getMessage());
	}
    }

    // 49
    public byte[] downloadDocument(String token, String docId, String outputFileUniquePath, long maxFileLength) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    // Ricerca documento con i criteri
	    if (docId == null || docId.equals(VOID)) {
		throw new DocerException("docId obbligatorio");
	    }

	    docId = docId.replaceAll("^" + RACC_UID_DOC_PREFIX, "");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    byte[] file = provider.downloadLastVersion(docId, outputFileUniquePath, maxFileLength);

	    return file;

	}
	catch (DocerException docEx) {

	    throw new DocerException(449, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(549, err.getMessage());
	}
    }

    // 51
    public void replaceLastVersion(String token, String docId, InputStream file) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    docId = docId.replaceAll("^" + RACC_UID_DOC_PREFIX, "");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // e' richiesto anche il file documento oltre al profilo
	    if (file == null)
		throw new DocerException("file null");

	    if (file.available() == 0)
		throw new DocerException("file vuoto"); // File Attachment
							// Documento vuoto o
							// errata codifica
							// Base64 (MIME)

	    provider.replaceLastVersion(docId, file);

	}
	catch (DocerException docEx) {

	    throw new DocerException(451, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(551, err.getMessage());
	}

    }

    // 56
    public List<String> getRelatedDocuments(String token, String docId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    docId = docId.replaceAll("^" + RACC_UID_DOC_PREFIX, "");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    List<String> related = provider.getRelatedDocuments(docId);
	    related.remove(docId);
	    return related;

	}
	catch (DocerException docEx) {

	    throw new DocerException(456, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(556, err.getMessage());
	}
    }

    // ************** ANAGRAFICHE ********************

    // 61
    public void createEnte(String token, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	clearCache();

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(Constants.ente_type_id);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.ente_type_id);

	    String codEnte = info.get(Constants.ente_cod_ente);
	    String desEnte = info.get(Constants.ente_des_ente);
	    String enabled = info.get(Constants.ente_enabled);
	    if (enabled == null)
		enabled = "true";

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("Verificare i campi obbligatori (COD_ENTE, DES_ENTE)");

	    if (desEnte == null || desEnte.equals(VOID))
		throw new DocerException("Verificare i campi obbligatori (COD_ENTE, DES_ENTE)");

	    // Map<String, String> p = getEnteProfile(codEnte);
	    Map<String, String> idCrit = new HashMap<String, String>();
	    idCrit.put(Constants.ente_cod_ente, codEnte);
	    Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCrit);
	    if (p != null)
		throw new DocerException("Ente " + codEnte + " esistente");

	    IEnteInfo enteInfo = new EnteInfo();
	    enteInfo.setEnabled(getEnumBoolean(enabled));
	    enteInfo.setCodiceEnte(codEnte.toUpperCase());
	    enteInfo.setDescrizione(desEnte);

	    FieldDescriptor fd = null;
	    for (String fieldName : info.keySet()) {

		if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
		    continue;
		}

		fd = anagraficaType.getFieldDescriptor(codEnte, null, fieldName);
		if (fd == null) {
		    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica ENTE");
		}

		String propValue = fd.checkValueFormat(info.get(fieldName));
		if (fieldName.equalsIgnoreCase(Constants.ente_cod_ente)) {
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.ente_des_ente)) {
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.ente_enabled)) {
		    continue;
		}

		enteInfo.getExtraInfo().put(fd.getPropName(), propValue);
	    }

	    provider.createEnte(enteInfo);

	}
	catch (DocerException docEx) {

	    throw new DocerException(461, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(561, err.getMessage());
	}
    }

    // 62
    public void updateEnte(String token, String codiceEnte, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	clearCache();

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    if (codiceEnte == null || codiceEnte.equals(VOID))
		throw new DocerException("specificare COD_ENTE da modificare");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(Constants.ente_type_id);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.ente_type_id);

	    // String newCodEnte = info.get(Constants.ente_cod_ente);

	    IEnteId enteid = new EnteId();
	    enteid.setCodiceEnte(codiceEnte);

	    Map<String, String> idCrit = new HashMap<String, String>();
	    idCrit.put(Constants.ente_cod_ente, codiceEnte);
	    Map<String, String> pente = getAnagraficaProfile(Constants.ente_type_id, idCrit);
	    if (pente == null)
		throw new DocerException("Ente " + codiceEnte + " non trovato");

	    IEnteInfo enteInfo = new EnteInfo();

	    FieldDescriptor fd = null;
	    for (String fieldName : info.keySet()) {

		if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
		    continue;
		}

		fd = anagraficaType.getFieldDescriptor(codiceEnte, null, fieldName);
		if (fd == null) {
		    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica ENTE");
		}

		String newPropValue = fd.checkValueFormat(info.get(fieldName));
		String oldPropValue = pente.get(fieldName);

		if (fieldName.equalsIgnoreCase(Constants.ente_cod_ente)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare " + Constants.ente_cod_ente);

		    continue;
		}

		if (fieldName.equalsIgnoreCase(Constants.ente_des_ente)) {
		    enteInfo.setDescrizione(newPropValue);
		}

		if (fieldName.equalsIgnoreCase(Constants.ente_enabled)) {
		    enteInfo.setEnabled(getEnumBoolean(newPropValue));
		    continue;
		}

		enteInfo.getExtraInfo().put(fieldName, newPropValue);
	    }

	    // modifica ente
	    provider.updateEnte(enteid, enteInfo);

	}
	catch (DocerException docEx) {

	    throw new DocerException(462, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(562, err.getMessage());
	}

    }

    // 63
    public Map<String, String> getEnte(String token, String codiceEnte) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (codiceEnte == null)
		throw new DocerException("anagrafica id null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    if (codiceEnte == null || codiceEnte.equals(VOID))
		throw new DocerException("specificare COD_ENTE");

	    Map<String, String> idCrit = new HashMap<String, String>();
	    idCrit.put(Constants.ente_cod_ente, codiceEnte);
	    Map<String, String> profile = getAnagraficaProfile(Constants.ente_type_id, idCrit);

	    if (profile == null) {
		throw new DocerException("Ente " + codiceEnte + " non trovato");
	    }

	    return profile;

	}
	catch (DocerException docEx) {

	    throw new DocerException(463, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(563, err.getMessage());
	}

    }

    // 64
    public void createAOO(String token, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	clearCache();

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(Constants.aoo_type_id);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.aoo_type_id);

	    String codEnte = info.get(Constants.aoo_cod_ente);
	    String codAoo = info.get(Constants.aoo_cod_aoo);
	    String desAoo = info.get(Constants.aoo_des_aoo);
	    String enabled = info.get(Constants.aoo_enabled);
	    if (enabled == null)
		enabled = "true";

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_AOO);

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_AOO);

	    if (desAoo == null || desAoo.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_AOO);

	    IEnteId enteid = new EnteId();
	    enteid.setCodiceEnte(codEnte);

	    Map<String, String> idCrit = new HashMap<String, String>();
	    idCrit.put(Constants.ente_cod_ente, codEnte);
	    Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCrit);
	    if (p == null)
		throw new DocerException("Ente " + codEnte + " non trovato");

	    idCrit.put(Constants.aoo_cod_aoo, codAoo);

	    Map<String, String> paoo = getAnagraficaProfile(Constants.aoo_type_id, idCrit);
	    if (paoo != null)
		throw new DocerException("AOO " + idCrit.toString() + " esistente");

	    IAOOInfo aooInfo = new AOOInfo();
	    aooInfo.setEnabled(getEnumBoolean(enabled));
	    aooInfo.setCodiceEnte(codEnte);
	    aooInfo.setCodiceAOO(codAoo);
	    aooInfo.setDescrizione(desAoo);

	    FieldDescriptor fd = null;
	    for (String fieldName : info.keySet()) {

		if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
		    continue;
		}

		fd = anagraficaType.getFieldDescriptor(codEnte, codAoo, fieldName);
		if (fd == null) {
		    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica AOO");
		}

		String propValue = fd.checkValueFormat(info.get(fieldName));

		if (fieldName.equalsIgnoreCase(Constants.aoo_cod_ente)) {
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.aoo_cod_aoo)) {
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.aoo_des_aoo)) {
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.aoo_enabled)) {
		    continue;
		}

		aooInfo.getExtraInfo().put(fd.getPropName(), propValue);
	    }

	    provider.createAOO(aooInfo);

	}
	catch (DocerException docEx) {

	    throw new DocerException(464, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(564, err.getMessage());
	}
    }

    // 65
    public void updateAOO(String token, Map<String, String> id, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	id = toUCMap(id);
	info = toUCMap(info);

	clearCache();

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    if (id == null)
		throw new DocerException("anagrafica id null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(Constants.aoo_type_id);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.aoo_type_id);

	    String codEnte = id.get(Constants.aoo_cod_ente);
	    String codAoo = id.get(Constants.aoo_cod_aoo);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("AOO non trovata: specificare COD_ENTE");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("AOO non trovata: specificare COD_AOO");

	    IAOOId aooId = new AOOId();
	    aooId.setCodiceEnte(codEnte);
	    aooId.setCodiceAOO(codAoo);

	    Map<String, String> idCrit = new HashMap<String, String>();
	    idCrit.put(Constants.aoo_cod_ente, codEnte);
	    idCrit.put(Constants.aoo_cod_aoo, codAoo);

	    Map<String, String> paoo = getAnagraficaProfile(Constants.aoo_type_id, idCrit);
	    if (paoo == null)
		throw new DocerException("AOO " + idCrit.toString() + " non trovata");

	    IAOOInfo aooInfo = new AOOInfo();

	    FieldDescriptor fd = null;
	    for (String fieldName : info.keySet()) {

		if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
		    continue;
		}

		fd = anagraficaType.getFieldDescriptor(codEnte, codAoo, fieldName);
		if (fd == null) {
		    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica AOO");
		}

		// info.put(fieldName,
		// fd.checkValueFormat(info.get(fieldName)));
		String newPropValue = fd.checkValueFormat(info.get(fieldName));
		String oldPropValue = paoo.get(fieldName);

		if (fieldName.equalsIgnoreCase(Constants.aoo_cod_ente)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare " + Constants.aoo_cod_ente);

		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.aoo_cod_aoo)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare " + Constants.aoo_cod_aoo);

		    continue;
		}

		if (fieldName.equalsIgnoreCase(Constants.aoo_des_aoo)) {
		    aooInfo.setDescrizione(newPropValue);
		    continue;
		}

		if (fieldName.equalsIgnoreCase(Constants.aoo_enabled)) {
		    aooInfo.setEnabled(getEnumBoolean(newPropValue));
		    continue;
		}

		aooInfo.getExtraInfo().put(fieldName, newPropValue);
	    }

	    provider.updateAOO(aooId, aooInfo);

	}
	catch (DocerException docEx) {

	    throw new DocerException(465, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(565, err.getMessage());
	}
    }

    // 66
    public Map<String, String> getAOO(String token, Map<String, String> id) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	id = toUCMap(id);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (id == null)
		throw new DocerException("anagrafica id null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    String codEnte = id.get(Constants.aoo_cod_ente);
	    String codAoo = id.get(Constants.aoo_cod_aoo);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("specificare COD_ENTE");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("specificare COD_AOO");

	    Map<String, String> idCrit = new HashMap<String, String>();
	    idCrit.put(Constants.aoo_cod_ente, codEnte);
	    idCrit.put(Constants.aoo_cod_aoo, codAoo);

	    Map<String, String> profile = getAnagraficaProfile(Constants.aoo_type_id, idCrit);

	    if (profile == null) {
		throw new DocerException("AOO " + idCrit.toString() + " non trovata");
	    }

	    return profile;

	}
	catch (DocerException docEx) {

	    throw new DocerException(466, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(566, err.getMessage());
	}
    }

    // 70
    public void createTitolario(String token, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(Constants.titolario_type_id);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.titolario_type_id);

	    String codEnte = info.get(Constants.titolario_cod_ente);
	    String codAoo = info.get(Constants.titolario_cod_aoo);
	    String codTitolario = info.get(Constants.titolario_cod_titolario);
	    String classifica = info.get(Constants.titolario_classifica);
	    if (codTitolario == null || codTitolario.equals(VOID)) {
		codTitolario = classifica;
	    }
	    String desTitolario = info.get(Constants.titolario_des_titolario);
	    String parentClassifica = info.get(Constants.titolario_parent_classifica);

	    // controllo credenziali di manager
	    // if(parentClassifica==null || parentClassifica.equals(VOID)){
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Voce di Titolario di primo livello: utente non autorizzato per le operazioni di management");
	    // }

	    String enabled = info.get(Constants.titolario_enabled);
	    if (enabled == null)
		enabled = "true";

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_TITOLARIO);

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_TITOLARIO);

	    // if (codTitolario == null || codTitolario.equals(VOID))
	    // throw new DocerException(ERROR_MESSAGE_CREATE_TITOLARIO);

	    if (desTitolario == null || desTitolario.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_TITOLARIO);

	    if (classifica == null || classifica.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_TITOLARIO);

	    Map<String, String> idCritAoo = new HashMap<String, String>();
	    idCritAoo.put(Constants.aoo_cod_ente, codEnte);
	    idCritAoo.put(Constants.aoo_cod_aoo, codAoo);

	    Map<String, String> paoo = getAnagraficaProfile(Constants.aoo_type_id, idCritAoo);

	    if (paoo == null) {

		Map<String, String> idCritEnte = new HashMap<String, String>();
		idCritEnte.put(Constants.aoo_cod_ente, codEnte);

		Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCritEnte);
		if (p == null)
		    throw new DocerException("Ente " + codEnte + " non trovato");

		throw new DocerException("AOO " + idCritAoo.toString() + " non trovata");
	    }

	    // verifico utilizzo della CLASSIFICA
	    Map<String, String> idCritTitoario = new HashMap<String, String>();
	    idCritTitoario.put(Constants.aoo_cod_ente, codEnte);
	    idCritTitoario.put(Constants.aoo_cod_aoo, codAoo);
	    idCritTitoario.put(Constants.titolario_classifica, classifica);
	    Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, idCritTitoario);
	    if (ptit != null)
		throw new DocerException("Voce di Titolario " + idCritTitoario.toString() + " esistente");

	    idCritTitoario.clear();
	    idCritTitoario.put(Constants.aoo_cod_ente, codEnte);
	    idCritTitoario.put(Constants.aoo_cod_aoo, codAoo);
	    idCritTitoario.put(Constants.titolario_cod_titolario, codTitolario);

	    // verifico utilizzo del COD_TITOLARIO
	    ptit = getAnagraficaProfile(Constants.titolario_type_id, idCritTitoario);
	    if (ptit != null) {
		throw new DocerException("Compatibilita' DOCAREA/DocER: il metadato COD_TITOLARIO " + codTitolario
			+ " (impostato come CLASSIFICA se non specificato) e' gia' utilizzato da un altra voce di Titolario");
	    }

	    // controllo di esistenza del padre
	    if (parentClassifica != null && !parentClassifica.equals(VOID)) {

		idCritTitoario.clear();
		idCritTitoario.put(Constants.aoo_cod_ente, codEnte);
		idCritTitoario.put(Constants.aoo_cod_aoo, codAoo);
		idCritTitoario.put(Constants.titolario_classifica, parentClassifica);

		Map<String, String> ptitPadre = getAnagraficaProfile(Constants.titolario_type_id, idCritTitoario);
		if (ptitPadre == null)
		    throw new DocerException("Voce di Titolario padre " + idCritTitoario.toString() + " non trovato");
	    }

	    ITitolarioInfo titInfo = new TitolarioInfo();
	    titInfo.setEnabled(getEnumBoolean(enabled));
	    titInfo.setCodiceEnte(codEnte);
	    titInfo.setCodiceAOO(codAoo);
	    titInfo.setClassifica(classifica);
	    titInfo.setDescrizione(desTitolario);
	    titInfo.getExtraInfo().put(Constants.titolario_cod_titolario, codTitolario);

	    if (parentClassifica == null)
		parentClassifica = VOID;
	    titInfo.setParentClassifica(parentClassifica);

	    FieldDescriptor fd = null;
	    // aggiungiamo leextrainfo
	    for (String fieldName : info.keySet()) {

		if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
		    continue;
		}

		fd = anagraficaType.getFieldDescriptor(codEnte, codAoo, fieldName);
		if (fd == null) {
		    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica TITOLARIO per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
		}

		// info.put(fieldName,fd.checkValueFormat(info.get(fieldName)));
		String propValue = fd.checkValueFormat(info.get(fieldName));

		if (fieldName.equalsIgnoreCase(Constants.titolario_cod_ente)) {
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.titolario_cod_aoo)) {
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.titolario_cod_titolario)) {
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.titolario_des_titolario)) {
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.titolario_enabled)) {
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.titolario_classifica)) {
		    continue;
		}

		if (fieldName.equalsIgnoreCase(Constants.titolario_parent_classifica)) {
		    continue;
		}

		titInfo.getExtraInfo().put(fd.getPropName(), propValue);
	    }

	    provider.createTitolario(titInfo);

	}
	catch (DocerException docEx) {

	    throw new DocerException(470, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(570, err.getMessage());
	}
    }

    // 71
    public void updateTitolario(String token, Map<String, String> id, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	id = toUCMap(id);
	info = toUCMap(info);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    if (id == null)
		throw new DocerException("anagrafica id null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(Constants.titolario_type_id);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.titolario_type_id);

	    String codEnte = id.get(Constants.titolario_cod_ente);
	    String codAoo = id.get(Constants.titolario_cod_aoo);
	    // String codTitolario = id.get(Constants.titolario_cod_titolario);
	    String classifica = id.get(Constants.titolario_classifica);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("Voce di Titolario non trovata: specificare COD_ENTE");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("Voce di Titolario non trovata: specificare COD_AOO");

	    // if ((classifica == null || classifica.equals(VOID))
	    // && (codTitolario == null || codTitolario.equals(VOID)))
	    // throw new DocerException(
	    // "Titolario non trovato: specificare COD_TITOLARIO e/o CLASSIFICA");

	    if (classifica == null || classifica.equals(VOID))
		throw new DocerException("Voce di Titolario non trovata: specificare CLASSIFICA");

	    ITitolarioId titId = new TitolarioId();
	    titId.setCodiceEnte(codEnte);
	    titId.setCodiceAOO(codAoo);
	    // titId.setCodiceTitolario(codTitolario);
	    titId.setClassifica(classifica);

	    Map<String, String> idCrit = new HashMap<String, String>();
	    idCrit.put(Constants.titolario_cod_ente, codEnte);
	    idCrit.put(Constants.titolario_cod_aoo, codAoo);
	    idCrit.put(Constants.titolario_classifica, classifica);

	    Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, idCrit);
	    if (ptit == null) {
		// if (codTitolario != null && classifica != null)
		// throw new DocerException("Titolario con COD_TITOLARIO "
		// + codTitolario + " e CLASSIFICA " + classifica
		// + " non trovato");
		// if (codTitolario != null)
		// throw new DocerException("Titolario con COD_TITOLARIO "
		// + codTitolario + " non trovato");

		throw new DocerException("Voce di Titolario " + idCrit.toString() + " non trovata");
	    }

	    // String oldCodEnte = ptit.get(Constants.titolario_cod_ente);
	    // String oldCodAOO = ptit.get(Constants.titolario_cod_aoo);
	    // //String oldCodTitolario =
	    // ptit.get(Constants.titolario_cod_titolario);
	    // String oldClassifica = ptit.get(Constants.titolario_classifica);
	    // String oldParentClassifica =
	    // ptit.get(Constants.titolario_parent_classifica);
	    //
	    // if (oldParentClassifica == null)
	    // oldParentClassifica = VOID;

	    // if(oldParentClassifica.equals(VOID)){
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Aggiornamento Voce di Titolario di primo livello: utente non autorizzato per le operazioni di management");
	    // }

	    ITitolarioInfo titInfo = new TitolarioInfo();

	    FieldDescriptor fd = null;
	    for (String fieldName : info.keySet()) {

		if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
		    continue;
		}

		fd = anagraficaType.getFieldDescriptor(codEnte, codAoo, fieldName);
		if (fd == null) {
		    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica TITOLARIO per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
		}

		String newPropValue = fd.checkValueFormat(info.get(fieldName));
		String oldPropValue = ptit.get(fieldName);

		if (fieldName.equalsIgnoreCase(Constants.titolario_cod_ente)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare COD_ENTE");

		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.titolario_cod_aoo)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare COD_AOO");

		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.titolario_cod_titolario)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare COD_TITOLARIO");

		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.titolario_classifica)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare CLASSIFICA");

		    continue;
		}

		if (fieldName.equalsIgnoreCase(Constants.titolario_parent_classifica)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare PARENT_CLASSIFICA");

		    continue;
		}

		if (fieldName.equalsIgnoreCase(Constants.titolario_des_titolario)) {
		    titInfo.setDescrizione(newPropValue);
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.titolario_enabled)) {
		    titInfo.setEnabled(getEnumBoolean(newPropValue));
		    continue;
		}

		titInfo.getExtraInfo().put(fieldName, newPropValue);
	    }

	    provider.updateTitolario(titId, titInfo);

	}
	catch (DocerException docEx) {

	    throw new DocerException(471, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(571, err.getMessage());
	}
    }

    // 72
    public Map<String, String> getTitolario(String token, Map<String, String> id) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	id = toUCMap(id);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (id == null)
		throw new DocerException("anagrafica id null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    String codEnte = id.get(Constants.titolario_cod_ente);
	    String codAoo = id.get(Constants.titolario_cod_aoo);
	    // String codTitolario = id.get(Constants.titolario_cod_titolario);
	    String classifica = id.get(Constants.titolario_classifica);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("specificare COD_ENTE");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("specificare COD_AOO");

	    // if ((codTitolario == null || codTitolario.equals(VOID))
	    // && (classifica == null || classifica.equals(VOID)))
	    // throw new DocerException(
	    // "specificare COD_TITOLARIO e/o CLASSIFICA");

	    if (classifica == null || classifica.equals(VOID))
		throw new DocerException("specificare CLASSIFICA");

	    Map<String, String> idCrit = new HashMap<String, String>();
	    idCrit.put(Constants.titolario_cod_ente, codEnte);
	    idCrit.put(Constants.titolario_cod_aoo, codAoo);
	    idCrit.put(Constants.titolario_classifica, classifica);

	    Map<String, String> profile = getAnagraficaProfile(Constants.titolario_type_id, idCrit);

	    if (profile == null) {
		throw new DocerException("Voce di Titolario " + idCrit.toString() + " non trovata");
	    }

	    return profile;

	}
	catch (DocerException docEx) {

	    throw new DocerException(472, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(572, err.getMessage());
	}
    }

    // 73
    public void createFascicolo(String token, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	info.remove("NEEDS_FONTE_UPDATE");

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(Constants.fascicolo_type_id);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.fascicolo_type_id);

	    String codEnte = info.get(Constants.fascicolo_cod_ente);
	    String codAoo = info.get(Constants.fascicolo_cod_aoo);
	    String classifica = info.get(Constants.fascicolo_classifica);
	    String codTitolario = "";

	    String progrFascicolo = info.get(Constants.fascicolo_progr_fascicolo);
	    String numFascicolo = info.get(Constants.fascicolo_num_fascicolo);
	    if (numFascicolo == null || numFascicolo.equals(VOID)) {
		numFascicolo = progrFascicolo;
	    }

	    String annoFascicolo = info.get(Constants.fascicolo_anno_fascicolo);
	    String desFascicolo = info.get(Constants.fascicolo_des_fascicolo);
	    String enabled = info.get(Constants.fascicolo_enabled);
	    if (enabled == null)
		enabled = "true";

	    String parentProgressivo = info.get(Constants.fascicolo_parent_progr_fascicolo);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

	    if (classifica == null || classifica.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

	    if (progrFascicolo == null || progrFascicolo.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

	    // if (numFascicolo == null || numFascicolo.equals(VOID))
	    // throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

	    if (annoFascicolo == null || annoFascicolo.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

	    Map<String, String> idCritFascicolo = new HashMap<String, String>();
	    idCritFascicolo.put(Constants.fascicolo_cod_ente, codEnte);
	    idCritFascicolo.put(Constants.fascicolo_cod_aoo, codAoo);
	    idCritFascicolo.put(Constants.fascicolo_classifica, classifica);
	    idCritFascicolo.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);
	    idCritFascicolo.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);

	    Map<String, String> pfasc = getAnagraficaProfile(Constants.fascicolo_type_id, idCritFascicolo);
	    if (pfasc != null) {
		String a = "Fascicolo " + idCritFascicolo.toString() + " esistente";
	    }
	    else {

		Map<String, String> idCritTitolario = new HashMap<String, String>();
		idCritTitolario.put(Constants.fascicolo_cod_ente, codEnte);
		idCritTitolario.put(Constants.fascicolo_cod_aoo, codAoo);
		idCritTitolario.put(Constants.fascicolo_classifica, classifica);
		// cerco il titolario
		Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, idCritTitolario);

		if (ptit == null) {

		    Map<String, String> idCritAoo = new HashMap<String, String>();
		    idCritAoo.put(Constants.fascicolo_cod_ente, codEnte);
		    idCritAoo.put(Constants.fascicolo_cod_aoo, codAoo);

		    Map<String, String> paoo = getAnagraficaProfile(Constants.aoo_type_id, idCritAoo);

		    if (paoo == null) {
			// verifico esistenza dell'ente
			Map<String, String> idCritEnte = new HashMap<String, String>();
			idCritEnte.put(Constants.fascicolo_cod_ente, codEnte);

			Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCritEnte);
			if (p == null)
			    throw new DocerException("Ente " + codEnte + " non trovato");

			throw new DocerException("AOO " + idCritAoo.toString() + " non trovata");
		    }

		    throw new DocerException("Titolario " + idCritTitolario.toString() + " non trovato");

		}

		codTitolario = ptit.get(Constants.titolario_cod_titolario);
		if (codTitolario == null || codTitolario.equals(VOID)) {
		    throw new DocerException("Compatibilita' DOCAREA/DocER: la voce di titolario con classifica " + classifica + " non ha COD_TITOLARIO assegnato");
		}

		// Map<String, String> idCritFascicolo = new HashMap<String,
		// String>();
		// idCritFascicolo.put(Constants.fascicolo_cod_ente, codEnte);
		// idCritFascicolo.put(Constants.fascicolo_cod_aoo, codAoo);
		// idCritFascicolo.put(Constants.fascicolo_classifica,
		// classifica);
		// idCritFascicolo.put(Constants.fascicolo_progr_fascicolo,
		// progrFascicolo);
		// idCritFascicolo.put(Constants.fascicolo_anno_fascicolo,
		// annoFascicolo);
		//
		// Map<String, String> pfasc =
		// getAnagraficaProfile(Constants.fascicolo_type_id,
		// idCritFascicolo);
		// if (pfasc != null)
		// throw new DocerException("Fascicolo " +
		// idCritFascicolo.toString() + " esistente");

		// controllo utilizzo del num_fascicolo
		idCritFascicolo.clear();
		idCritFascicolo.put(Constants.fascicolo_cod_ente, codEnte);
		idCritFascicolo.put(Constants.fascicolo_cod_aoo, codAoo);
		idCritFascicolo.put(Constants.fascicolo_classifica, classifica);
		idCritFascicolo.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);
		idCritFascicolo.put(Constants.fascicolo_num_fascicolo, numFascicolo);

		pfasc = getAnagraficaProfile(Constants.fascicolo_type_id, idCritFascicolo);
		if (pfasc != null)
		    throw new DocerException("Compatibilita' DOCAREA/DocER: il metadato NUM_FASCICOLO " + numFascicolo
			    + " (impostato come PROGR_FASCICOLO se non specificato) e' gia' utilizzato da un altro Fascicolo");

		// controllo di esistenza del padre
		if (parentProgressivo != null && !parentProgressivo.equals(VOID)) {

		    idCritFascicolo.clear();
		    idCritFascicolo.put(Constants.fascicolo_cod_ente, codEnte);
		    idCritFascicolo.put(Constants.fascicolo_cod_aoo, codAoo);
		    idCritFascicolo.put(Constants.fascicolo_classifica, classifica);
		    idCritFascicolo.put(Constants.fascicolo_progr_fascicolo, parentProgressivo);
		    idCritFascicolo.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);

		    Map<String, String> pfascPadre = getAnagraficaProfile(Constants.fascicolo_type_id, idCritFascicolo);
		    if (pfascPadre == null)
			throw new DocerException("Fascicolo padre " + idCritFascicolo.toString() + " non trovato");
		}

		IFascicoloInfo fascInfo = new FascicoloInfo();
		fascInfo.setEnabled(getEnumBoolean(enabled));
		fascInfo.setCodiceEnte(codEnte);
		fascInfo.setCodiceAOO(codAoo);
		fascInfo.setClassifica(classifica);
		fascInfo.setProgressivo(progrFascicolo);
		fascInfo.setNumeroFascicolo(numFascicolo);
		// lo metto anche nelle extrainfo e due i campi per sicurezza
		fascInfo.getExtraInfo().put(Constants.fascicolo_num_fascicolo, numFascicolo);
		fascInfo.getExtraInfo().put(Constants.titolario_cod_titolario, codTitolario);

		fascInfo.setAnnoFascicolo(annoFascicolo);
		fascInfo.setDescrizione(desFascicolo);
		if (parentProgressivo == null)
		    parentProgressivo = VOID;
		fascInfo.setParentProgressivo(parentProgressivo);

		FieldDescriptor fd = null;
		// aggiungiamo le altre extra info
		for (String fieldName : info.keySet()) {

		    if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
			continue;
		    }

		    fd = anagraficaType.getFieldDescriptor(codEnte, codAoo, fieldName);
		    if (fd == null) {
			throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica FASCICOLO per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
		    }

		    String propValue = fd.checkValueFormat(info.get(fieldName));

		    if (fieldName.equalsIgnoreCase(Constants.fascicolo_cod_ente)) {
			continue;
		    }
		    if (fieldName.equalsIgnoreCase(Constants.fascicolo_cod_aoo)) {
			continue;
		    }
		    if (fieldName.equalsIgnoreCase(Constants.fascicolo_classifica)) {
			continue;
		    }
		    if (fieldName.equalsIgnoreCase(Constants.titolario_cod_titolario)) {
			continue;
		    }
		    if (fieldName.equalsIgnoreCase(Constants.fascicolo_progr_fascicolo)) {
			continue;
		    }
		    if (fieldName.equalsIgnoreCase(Constants.fascicolo_anno_fascicolo)) {
			continue;
		    }
		    if (fieldName.equalsIgnoreCase(Constants.fascicolo_des_fascicolo)) {
			continue;
		    }
		    if (fieldName.equalsIgnoreCase(Constants.fascicolo_enabled)) {
			continue;
		    }
		    if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
			continue;
		    }
		    if (fieldName.equalsIgnoreCase(Constants.fascicolo_parent_progr_fascicolo)) {
			continue;
		    }

		    fascInfo.getExtraInfo().put(fd.getPropName(), propValue);
		}

		fascInfo.getExtraInfo().put("RACC_UID", RACC_UID_FAS_PREFIX + UUID.randomUUID());
		provider.createFascicolo(fascInfo);
	    }

	}
	catch (DocerException docEx) {

	    throw new DocerException(473, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(573, err.getMessage());
	}
    }

    // 74
    public void updateFascicolo(String token, Map<String, String> id, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	id = toUCMap(id);
	info = toUCMap(info);

	info.remove("NEEDS_FONTE_UPDATE");

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (id == null)
		throw new DocerException("anagrafica id null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager = provider.userIsManager(userid);
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(Constants.fascicolo_type_id);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.fascicolo_type_id);

	    String codEnte = id.get(Constants.fascicolo_cod_ente);
	    String codAoo = id.get(Constants.fascicolo_cod_aoo);
	    String classifica = id.get(Constants.fascicolo_classifica);
	    String progrFascicolo = id.get(Constants.fascicolo_progr_fascicolo);
	    // String numFascicolo = id.get(Constants.fascicolo_num_fascicolo);
	    String annoFascicolo = id.get(Constants.fascicolo_anno_fascicolo);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

	    if (classifica == null || classifica.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

	    // if ((numFascicolo == null || numFascicolo.equals(VOID))
	    // && (progressivo == null || progressivo.equals(VOID)))
	    // throw new DocerException(
	    // "Fascicolo non trovato: specificare NUM_FASCICOLO e/o PROGR_FASCICOLO");

	    if (progrFascicolo == null || progrFascicolo.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare PROGR_FASCICOLO");

	    if (annoFascicolo == null || annoFascicolo.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

	    IFascicoloId fascId = new FascicoloId();
	    fascId.setCodiceEnte(codEnte);
	    fascId.setCodiceAOO(codAoo);
	    fascId.setClassifica(classifica);
	    fascId.setProgressivo(progrFascicolo);
	    // fascId.setNumeroFascicolo(numFascicolo);
	    fascId.setAnnoFascicolo(annoFascicolo);

	    Map<String, String> pfasc = getAnagraficaProfile(Constants.fascicolo_type_id, id);
	    if (pfasc == null) {
		throw new DocerException("Fascicolo " + id.toString() + " non trovato nel repository documentale del WS Fonte");
	    }

	    IFascicoloInfo fascInfo = new FascicoloInfo();

	    FieldDescriptor fd = null;
	    String newPropValue = "";
	    String oldPropValue = "";

	    // assegno le extrainfo
	    for (String fieldName : info.keySet()) {

		if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
		    continue;
		}

		fd = anagraficaType.getFieldDescriptor(codEnte, codAoo, fieldName);
		if (fd == null) {
		    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica FASCICOLO per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
		}

		newPropValue = fd.checkValueFormat(info.get(fieldName));
		oldPropValue = pfasc.get(fieldName);

		if (fieldName.equalsIgnoreCase(Constants.fascicolo_cod_ente)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare COD_ENTE");

		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.fascicolo_cod_aoo)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare COD_AOO");

		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.fascicolo_classifica)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare CLASSIFICA");
		    continue;
		}
		// il cod_titolario //compatibilita' DOCAREA
		if (fieldName.equalsIgnoreCase(Constants.titolario_cod_titolario)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare COD_TITOLARIO");
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.fascicolo_progr_fascicolo)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare PROGR_FASCICOLO");
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.fascicolo_num_fascicolo)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare NUM_FASCICOLO");
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.fascicolo_parent_progr_fascicolo)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare " + Constants.fascicolo_parent_progr_fascicolo);
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.fascicolo_anno_fascicolo)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare ANNO_FASCICOLO");
		    continue;
		}

		if (fieldName.equalsIgnoreCase(Constants.fascicolo_des_fascicolo)) {
		    fascInfo.setDescrizione(newPropValue);
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.fascicolo_enabled)) {
		    fascInfo.setEnabled(getEnumBoolean(newPropValue));
		    continue;
		}

		if (fieldName.equalsIgnoreCase("RACC_UID")) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare UID");

		    continue;
		}

		fascInfo.getExtraInfo().put(fieldName, newPropValue);
	    }

	    fascInfo.getExtraInfo().remove("RACC_UID");
	    provider.updateFascicolo(fascId, fascInfo);

	}
	catch (DocerException docEx) {

	    throw new DocerException(474, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(574, err.getMessage());
	}
    }

    // 75
    public Map<String, String> getFascicolo(String token, Map<String, String> id) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	id = toUCMap(id);

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (id == null)
		throw new DocerException("anagrafica id null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    Map<String, String> idCrit = null;

	    // test della library

	    String racc_uid = id.get("RACC_UID");

	    if (racc_uid != null && !racc_uid.equals(VOID)) {

		idCrit = new HashMap<String, String>();
		idCrit.put("RACC_UID", racc_uid);
	    }
	    else {

		String codEnte = id.get(Constants.fascicolo_cod_ente);
		String codAoo = id.get(Constants.fascicolo_cod_aoo);
		String classifica = id.get(Constants.fascicolo_classifica);
		String progrFascicolo = id.get(Constants.fascicolo_progr_fascicolo);
		String annoFascicolo = id.get(Constants.fascicolo_anno_fascicolo);

		if (codEnte == null || codEnte.equals(VOID))
		    throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

		if (codAoo == null || codAoo.equals(VOID))
		    throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

		if (classifica == null || classifica.equals(VOID))
		    throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

		if (progrFascicolo == null || progrFascicolo.equals(VOID))
		    throw new DocerException("Fascicolo non trovato: specificare PROGR_FASCICOLO");

		if (annoFascicolo == null || annoFascicolo.equals(VOID))
		    throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

		idCrit = new HashMap<String, String>();
		idCrit.put(Constants.fascicolo_cod_ente, codEnte);
		idCrit.put(Constants.fascicolo_cod_aoo, codAoo);
		idCrit.put(Constants.fascicolo_classifica, classifica);
		idCrit.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);
		idCrit.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);

	    }

	    Map<String, String> profile = getAnagraficaProfile(Constants.fascicolo_type_id, idCrit);

	    if (profile == null) {
		throw new DocerException("Fascicolo " + idCrit.toString() + " non trovato");
	    }

	    return profile;

	}
	catch (DocerException docEx) {

	    throw new DocerException(475, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(575, err.getMessage());
	}
    }

    // 76
    public void createAnagraficaCustom(String token, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    String type_id = info.get(Constants.anagrafica_type_id);

	    if (type_id == null || type_id.equals(VOID))
		throw new DocerException("TYPE_ID obbligatorio");

	    type_id = type_id.toUpperCase();

	    String codEnte = info.get(Constants.fascicolo_cod_ente);
	    String codAoo = info.get(Constants.fascicolo_cod_aoo);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("COD_ENTE obbligatorio");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("COD_AOO obbligatorio");

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(type_id);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + type_id);

	    if (!anagraficaType.isAnagraficaCustom())
		throw new DocerException("Anagrafica " + type_id + " non e' configurata come anagrafica custom");

	    // if (!anagraficaType.getDefinedForEnte(codEnte))
	    // throw new DocerException(
	    // "Anagrafica Custom " +type_id +" non e' definita per Ente "
	    // +codEnte);

	    String codCustom = info.get(anagraficaType.getCodicePropName());
	    if (codCustom == null || codCustom.equals(VOID))
		throw new DocerException("errore creazione anagrafica custom: " + anagraficaType.getCodicePropName() + " obbligatorio");

	    String desCustom = info.get(anagraficaType.getDescrizionePropName());
	    if (desCustom == null || desCustom.equals(VOID))
		throw new DocerException("errore creazione anagrafica custom: " + anagraficaType.getDescrizionePropName() + " obbligatorio");

	    ICustomItemInfo customItemInfo = new CustomItemInfo();
	    customItemInfo.setType(type_id);
	    customItemInfo.setCodiceEnte(codEnte);
	    customItemInfo.setCodiceAOO(codAoo);
	    customItemInfo.setCodiceCustom(codCustom);
	    customItemInfo.setDescrizione(desCustom);

	    customItemInfo.setEnabled(EnumBoolean.TRUE);

	    FieldDescriptor fd = null;

	    for (String fieldName : info.keySet()) {

		if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
		    continue;
		}

		fd = anagraficaType.getFieldDescriptor(codEnte, codAoo, fieldName);
		if (fd == null) {
		    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica " + type_id + " per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
		}

		String propValue = fd.checkValueFormat(info.get(fieldName));

		if (fieldName.equalsIgnoreCase(Constants.custom_cod_ente)) {
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.custom_cod_aoo)) {
		    continue;
		}

		if (fieldName.equalsIgnoreCase(anagraficaType.getCodicePropName())) {
		    continue;
		}

		if (fieldName.equalsIgnoreCase(anagraficaType.getDescrizionePropName())) {
		    continue;
		}

		if (fieldName.equalsIgnoreCase(Constants.custom_enabled)) {
		    customItemInfo.setEnabled(getEnumBoolean(info.get(fieldName)));
		    continue;
		}

		customItemInfo.getExtraInfo().put(fd.getPropName(), propValue);
	    }

	    Map<String, String> idCritAoo = new HashMap<String, String>();
	    idCritAoo.put(Constants.custom_cod_ente, codEnte);
	    idCritAoo.put(Constants.custom_cod_aoo, codAoo);

	    Map<String, String> p = getAnagraficaProfile(Constants.aoo_type_id, idCritAoo);
	    if (p == null) {

		Map<String, String> idCritEnte = new HashMap<String, String>();
		idCritEnte.put(Constants.custom_cod_ente, codEnte);
		p = getAnagraficaProfile(Constants.ente_type_id, idCritEnte);

		if (p == null)
		    throw new DocerException("Ente " + codEnte + " non trovato");

		throw new DocerException("AOO " + idCritAoo.toString() + " non trovata");
	    }

	    Map<String, String> idCritCustom = new HashMap<String, String>();
	    idCritCustom.put(Constants.custom_cod_ente, codEnte);
	    idCritCustom.put(Constants.custom_cod_aoo, codAoo);
	    idCritCustom.put(anagraficaType.getCodicePropName(), customItemInfo.getCodiceCustom());

	    p = getAnagraficaProfile(type_id, idCritCustom);
	    if (p != null)
		throw new DocerException("Anagrafica " + type_id + " " + idCritCustom.toString() + " esistente");

	    // creazione custom item
	    provider.createCustomItem(customItemInfo);

	}
	catch (DocerException docEx) {

	    throw new DocerException(476, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(576, err.getMessage());
	}
    }

    // 77
    public void updateAnagraficaCustom(String token, Map<String, String> id, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	id = toUCMap(id);
	info = toUCMap(info);

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    if (id == null)
		throw new DocerException("anagrafica id null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    String type_id = id.get(Constants.anagrafica_type_id);
	    if (type_id == null || type_id.equals(VOID))
		throw new DocerException("TYPE_ID obbligatorio tra i metadati di ID");

	    type_id = type_id.toUpperCase();

	    String codEnte = id.get(Constants.fascicolo_cod_ente);
	    String codAoo = id.get(Constants.fascicolo_cod_aoo);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("COD_ENTE obbligatorio");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("COD_AOO obbligatorio");

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(type_id);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + type_id);

	    if (!anagraficaType.isAnagraficaCustom())
		throw new DocerException("Anagrafica " + type_id + " non e' configurata come anagrafica custom");

	    // if (!anagraficaType.getDefinedForEnte(codEnte))
	    // throw new DocerException(
	    // "Anagrafica Custom " +type_id +" non e' definita per Ente "
	    // +codEnte);

	    String codCustom = id.get(anagraficaType.getCodicePropName());

	    if (codCustom == null || codCustom.equals(VOID))
		throw new DocerException("errore update anagrafica custom: " + anagraficaType.getCodicePropName() + " obbligatorio");

	    ICustomItemId customId = new CustomItemId();
	    customId.setType(type_id);
	    customId.setCodiceEnte(codEnte);
	    customId.setCodiceAOO(codAoo);
	    customId.setCodiceCustom(codCustom);

	    // controllo la collezione di ID
	    FieldDescriptor fd = null;
	    for (String fieldName : id.keySet()) {

		if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
		    continue;
		}

		fd = anagraficaType.getFieldDescriptor(codEnte, codAoo, fieldName);
		if (fd == null) {
		    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica " + type_id + " per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
		}

	    }

	    Map<String, String> idCrit = new HashMap<String, String>();
	    idCrit.put(Constants.custom_cod_ente, codEnte);
	    idCrit.put(Constants.custom_cod_aoo, codAoo);
	    idCrit.put(anagraficaType.getCodicePropName(), codCustom);

	    Map<String, String> pcustom = getAnagraficaProfile(type_id, idCrit);
	    if (pcustom == null)
		throw new DocerException("Anagrafica " + type_id + " " + idCrit.toString() + " non trovata");

	    ICustomItemInfo customItemNewInfo = new CustomItemInfo();

	    for (String fieldName : info.keySet()) {

		if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
		    continue;
		}

		fd = anagraficaType.getFieldDescriptor(codEnte, codAoo, fieldName);
		if (fd == null) {
		    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica " + type_id + " per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
		}

		String newPropValue = fd.checkValueFormat(info.get(fieldName));
		String oldPropValue = pcustom.get(fieldName);

		if (fieldName.equalsIgnoreCase(Constants.custom_cod_ente)) {
		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare COD_ENTE");

		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.custom_cod_aoo)) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare COD_AOO");

		    continue;
		}
		if (fieldName.equalsIgnoreCase(anagraficaType.getCodicePropName())) {

		    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
			throw new DocerException("non e' permesso modificare " + anagraficaType.getCodicePropName());

		    continue;
		}
		if (fieldName.equalsIgnoreCase(anagraficaType.getDescrizionePropName())) {
		    customItemNewInfo.setDescrizione(newPropValue);
		    continue;
		}
		if (fieldName.equalsIgnoreCase(Constants.custom_enabled)) {
		    customItemNewInfo.setEnabled(getEnumBoolean(newPropValue));
		    continue;
		}

		customItemNewInfo.getExtraInfo().put(fieldName, newPropValue);

	    }

	    // creazione custom item
	    provider.updateCustomItem(customId, customItemNewInfo);

	}
	catch (DocerException docEx) {

	    throw new DocerException(477, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(577, err.getMessage());
	}
    }

    // 78
    public Map<String, String> getAnagraficaCustom(String token, Map<String, String> id) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	id = toUCMap(id);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (id == null)
		throw new DocerException("anagrafica id null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    String type_id = id.get(Constants.anagrafica_type_id);
	    if (type_id == null || type_id.equals(VOID))
		throw new DocerException("TYPE_ID obbligatorio tra i metadati di ID");

	    type_id = type_id.toUpperCase();

	    String codEnte = id.get(Constants.fascicolo_cod_ente);
	    String codAoo = id.get(Constants.fascicolo_cod_aoo);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("COD_ENTE obbligatorio");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("COD_AOO obbligatorio");

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(type_id);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + type_id);

	    if (!anagraficaType.isAnagraficaCustom())
		throw new DocerException("Anagrafica " + type_id + " non e' configurata come anagrafica custom");

	    // if (!anagraficaType.getDefinedForEnte(codEnte))
	    // throw new DocerException(
	    // "Anagrafica Custom " +type_id +" non e' definita per Ente "
	    // +codEnte);

	    String codCustom = id.get(anagraficaType.getCodicePropName());

	    if (codCustom == null || codCustom.equals(VOID))
		throw new DocerException(anagraficaType.getCodicePropName() + " obbligatorio");

	    Map<String, String> idCrit = new HashMap<String, String>();
	    idCrit.put(Constants.custom_cod_ente, codEnte);
	    idCrit.put(Constants.custom_cod_aoo, codAoo);
	    idCrit.put(anagraficaType.getCodicePropName(), codCustom);
	    // idCrit.put(Constants.anagrafica_type_id, type_id);

	    Map<String, String> profile = getAnagraficaProfile(type_id, idCrit);

	    return profile;

	}
	catch (DocerException docEx) {

	    throw new DocerException(478, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(578, err.getMessage());
	}
    }

    // 91
    public List<ISearchItem> searchDocuments(String token, Map<String, List<String>> searchCriteria, List<String> keywords, int maxRows, Map<String, EnumSearchOrder> orderby) throws DocerException {

	long start = new Date().getTime();

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	searchCriteria = toUCMapOfList(searchCriteria);

	List<String> outputKeywords = new ArrayList<String>();

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    // CheckFields.checkSearchCriteria(searchCriteria);

	    // if (outputKeywords == null)
	    // outputKeywords = new ArrayList<String>();

	    // le keywords non possono essere null
	    if (keywords != null)
		for (int i = 0; i < keywords.size(); i++) {
		    if (keywords.get(i) == null)
			continue;

		    outputKeywords.add(keywords.get(i));
		}

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    if (maxRows < 0)
		maxRows = PRIMARYSEARCH_MAX_ROWS;

	    List<String> returnProperties = HITLISTS.get("HITLIST");

	    DataTable<String> dtresults = provider.searchDocuments(searchCriteria, outputKeywords, returnProperties, maxRows, orderby);

	    long end1 = new Date().getTime();

	    List<ISearchItem> results = new ArrayList<ISearchItem>();

	    int columnCounter = 0;
	    for (int i = 0; i < dtresults.getRows().size(); i++) {
		DataRow<String> dr = dtresults.getRow(i);

		SearchItem searchItem = new SearchItem();
		// searchItem.putAll(m);.setMetadata(new
		// KeyValuePair[returnProperties.size()]);

		columnCounter = 0;
		for (String columnName : returnProperties) {

		    String val = dr.get(columnName);
		    if (val == null) {
			val = VOID;
		    }
		    searchItem.put(columnName, val);
		    columnCounter++;
		}

		results.add(searchItem);
	    }

	    long end2 = new Date().getTime();

	    // System.out.println("tempo search da provider: " +(end1-start)
	    // +" msec");
	    //
	    // System.out.println("tempo search da business logic: "
	    // +(end2-start) +" msec");

	    return results;

	}
	catch (DocerException docEx) {

	    throw new DocerException(491, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(591, err.getMessage());
	}
    }

    // 92
    public List<ISearchItem> searchAnagrafiche(String token, String type, Map<String, List<String>> searchCriteria) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	searchCriteria = toUCMapOfList(searchCriteria);

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (type == null)
		throw new DocerException("type null");

	    type = type.toUpperCase();

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(type);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + type);

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    List<String> hitlist = HITLISTS.get("HITLIST_" + type);

	    List<Map<String, String>> res = provider.searchAnagrafiche(type, searchCriteria, hitlist);

	    DataTable<String> dt = new DataTable<String>();
	    dt.importMaps(res);

	    return dt.getSearchItems();

	}
	catch (DocerException docEx) {

	    throw new DocerException(492, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(592, err.getMessage());
	}
    }

    // 93
    public List<ISearchItem> searchUsers(String token, Map<String, List<String>> searchCriteria) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	searchCriteria = toUCMapOfList(searchCriteria);

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    // CheckFields.checkSearchCriteria(searchCriteria);

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    /*
	     * // controllo credenziali di manager boolean isManager =
	     * provider.userIsManager(userid); if (!isManager) // l'utente non
	     * e' un manager throw new DocerException(
	     * "Utente non autorizzato per le operazioni di management");
	     */

	    List<IUserProfileInfo> usersFound = provider.searchUsers(searchCriteria);

	    List<ISearchItem> results = new ArrayList<ISearchItem>();

	    for (IUserProfileInfo profileInfo : usersFound) {
		ISearchItem user = new SearchItem();

		user.put(Constants.user_user_id, profileInfo.getUserId());
		user.put(Constants.user_full_name, profileInfo.getFullName());
		user.put(Constants.user_email_address, profileInfo.getEmailAddress());
		user.put(Constants.user_first_name, profileInfo.getFirstName());
		user.put(Constants.user_last_name, profileInfo.getLastName());
		user.put(Constants.user_network_alias, profileInfo.getNetworkAlias());
		user.put(Constants.user_enabled, profileInfo.getEnabled().toString().toLowerCase());

		for (String key : profileInfo.getExtraInfo().keySet()) {
		    user.put(key, profileInfo.getExtraInfo().get(key));
		}

		results.add(user);
	    }

	    return results;

	}
	catch (DocerException docEx) {

	    throw new DocerException(493, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(593, err.getMessage());
	}
    }

    // 94
    public List<ISearchItem> searchGroups(String token, Map<String, List<String>> searchCriteria) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	searchCriteria = toUCMapOfList(searchCriteria);

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    // CheckFields.checkSearchCriteria(searchCriteria);

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    /*
	     * // controllo credenziali di manager boolean isManager =
	     * provider.userIsManager(userid); if (!isManager) // l'utente non
	     * e' un manager throw new DocerException(
	     * "Utente non autorizzato per le operazioni di management");
	     */

	    List<IGroupProfileInfo> groupsFound = provider.searchGroups(searchCriteria);

	    List<ISearchItem> results = new ArrayList<ISearchItem>();

	    for (IGroupProfileInfo profileInfo : groupsFound) {
		ISearchItem group = new SearchItem();

		group.put(Constants.group_group_id, profileInfo.getGroupId());
		group.put(Constants.group_group_name, profileInfo.getGroupName());
		group.put(Constants.group_parent_group_id, profileInfo.getParentGroupId());
		group.put(Constants.group_enabled, VOID);

		if (profileInfo.getEnabled().equals(EnumBoolean.TRUE))
		    group.put(Constants.group_enabled, "true");
		else if (profileInfo.getEnabled().equals(EnumBoolean.FALSE))
		    group.put(Constants.group_enabled, "false");

		group.put(Constants.group_gruppo_struttura, String.valueOf(Boolean.valueOf(profileInfo.getExtraInfo().get(Constants.group_gruppo_struttura))));

		results.add(group);
	    }

	    return results;

	}
	catch (DocerException docEx) {

	    throw new DocerException(494, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(594, err.getMessage());
	}

    }

    
    // 95
    public List<IKeyValuePair> getDocumentTypes(String token) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    // if (token == null || token.equals(VOID))
	    // throw new DocerException("token null");
	    //
	    // Ticket tokenObject = this.ticketFormatter.parseTicket(token);
	    //
	    // String library = tokenObject.getLibrary();
	    // String userid = tokenObject.getUserid();
	    // String dmsticket = tokenObject.getDmsTicket();
	    //
	    // // setto le info utente
	    // LoggedUserInfo loginUserInfo = new LoggedUserInfo();
	    // loginUserInfo.setLibrary(library);
	    // loginUserInfo.setUserId(userid);
	    // loginUserInfo.setTicket(dmsticket);
	    //
	    // // setto l'utente nel provider
	    // provider.setCurrentUser(loginUserInfo);
	    //
	    // // test della library
	    //
	    //
	    // if (typeIdDescriptionCaching != null
	    // && typeIdDescriptionCaching.size() > 0)
	    // return mapToList(typeIdDescriptionCaching);
	    // else
	    // typeIdDescriptionCaching = provider.getDocumentTypes();
	    //
	    // return mapToList(typeIdDescriptionCaching);

	    List<IKeyValuePair> result = new ArrayList<IKeyValuePair>();

	    for (DocumentType docType : DOCUMENT_TYPES.values()) {

		result.add(new KeyValuePair(docType.getTypeId(), docType.getDescription()));
	    }

	    return result;
	}
	// catch (DocerException docEx) {
	//
	// throw new DocerException(495, docEx.getMessage());
	//
	// }
	catch (Exception err) {
	    throw new DocerException(595, err.getMessage());
	}
    }

    // 96
    public boolean verifyTicket(String token) {

	// if(!CONFIG_INITIALIZED){
	// throw new DocerException("Configurazione non inizializzata: "
	// +CONFIG_ERROR_MESSAGE);
	// }

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // non bisogna impostare il current user
	    return provider.verifyTicket(loginUserInfo.getUserId(), loginUserInfo.getCodiceEnte(), loginUserInfo.getTicket());

	}
	catch (Exception err) {
	    return false;
	}
    }

    // 19
    public List<IKeyValuePair> getDocumentTypes(String token, String codEnte, String codAOO) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	if (codEnte == null || codEnte.equals(VOID))
	    throw new DocerException(419, "COD_ENTE obbligatorio");

	if (codAOO == null || codAOO.equals(VOID))
	    throw new DocerException(419, "COD_AOO obbligatorio");

	try {

	    List<IKeyValuePair> result = new ArrayList<IKeyValuePair>();

	    for (DocumentType docType : DOCUMENT_TYPES.values()) {

		if (docType.isDefinedForAOO(codEnte, codAOO))
		    result.add(new KeyValuePair(docType.getTypeId(), docType.getDescription()));
	    }

	    return result;
	}
	catch (Exception e) {
	    throw new DocerException(519, e.getMessage());
	}
    }

    // ************ METODI PRIVATI *****************//
    // ************ METODI PRIVATI *****************//
    // ************ METODI PRIVATI *****************//
    // ************ METODI PRIVATI *****************//

    // private Map<String, Map<String, String>>
    // getRelatedProfilesToUpdate(DataRow<String> masterProfile, List<String>
    // originalRelatedChain, List<String> toAddRelated) throws DocerException {
    //
    // Map<String, Map<String, String>> relatedProfileToUpdate = new
    // HashMap<String, Map<String, String>>();
    //
    // String masterDocid = masterProfile.get(Constants.doc_docnum);
    //
    // // LockStatus checkedOutInfo = null;
    //
    // List<String> relatedToCheck = new ArrayList<String>();
    // for (String id : originalRelatedChain) {
    // if (id.equals(masterDocid)) {
    // continue;
    // }
    // if (relatedToCheck.contains(id)) {
    // continue;
    // }
    // relatedToCheck.add(id);
    // }
    // for (String id : toAddRelated) {
    // if (id.equals(masterDocid)) {
    // continue;
    // }
    // if (relatedToCheck.contains(id)) {
    // continue;
    // }
    // relatedToCheck.add(id);
    // }
    //
    // if (relatedToCheck.size() == 0) {
    // return relatedProfileToUpdate;
    // }
    //
    // // creo i criteri di ricerca
    // Map<String, List<String>> searchCriteria = new HashMap<String,
    // List<String>>();
    // searchCriteria.put(Constants.doc_docnum, relatedToCheck);
    //
    // // metadati che voglio ricercare
    // List<String> returnProperties = new ArrayList<String>();
    // returnProperties.add(Constants.doc_docnum);
    // returnProperties.add(Constants.doc_docnum_princ);
    // returnProperties.add(Constants.doc_cod_ente);
    // returnProperties.add(Constants.doc_cod_aoo);
    // returnProperties.add(Constants.doc_stato_archivistico);
    // returnProperties.add(Constants.doc_stato_pantarei);
    // returnProperties.add(Constants.doc_tipo_componente);
    // returnProperties.add(Constants.doc_classifica);
    // returnProperties.add(Constants.doc_progr_fascicolo);
    // returnProperties.add(Constants.doc_anno_fascicolo);
    // returnProperties.add(Constants.doc_fascicoli_secondari);
    // returnProperties.add(Constants.doc_num_fascicolo);
    // returnProperties.add(Constants.doc_cod_titolario);
    // returnProperties.add(Constants.doc_archive_type);
    // returnProperties.add(Constants.doc_stato_conserv);
    //
    // // recupero dei profili di tutti i related (I livello -> relatedChain e
    // // toAdd)
    // DataTable<String> searchResults =
    // provider.searchDocuments(searchCriteria, null, returnProperties,
    // PRIMARYSEARCH_MAX_ROWS, null);
    //
    // // errore ricerca: nessun related e' presente sul DMS (!!!)
    // if (searchResults == null || searchResults.getRows().size() == 0)
    // throw new
    // DocerException("Errore recupero profili catena dei related: nessuno dei related da aggiungere e' stato trovato");
    //
    // List<String> allRelatedOfRelatedList = new ArrayList<String>();
    //
    // List<String> yetChecked = new ArrayList<String>();
    // yetChecked.add(masterDocid);
    //
    // // controllo i related (attuali e da aggiungere)
    // for (DataRow<String> relatedProfile : searchResults.getRows()) {
    //
    // String relDocnum = relatedProfile.get(Constants.doc_docnum);
    //
    // relatedToCheck.removeAll(Arrays.asList(relDocnum));
    //
    // // se fa parte della chain originale e' yet checked
    // if (yetChecked.contains(relDocnum)) {
    // continue;
    // }
    //
    // // controllo del checkout
    // // checkedOutInfo =
    // // (LockStatus)provider.isCheckedOutDocument(relDocnum);
    // //
    // // if (checkedOutInfo.getLocked()) {
    // // if (originalRelatedChain.contains(relDocnum)) {
    // // throw new DocerException("il documento " + relDocnum +
    // // " della catena dei related e' in stato di blocco esclusivo");
    // // }
    // // throw new DocerException("il related da aggiungere " + relDocnum
    // // + " e' in stato di blocco esclusivo");
    // // }
    //
    // yetChecked.add(relDocnum);
    //
    // boolean belongsToOriginalRelatedChain =
    // originalRelatedChain.contains(relDocnum);
    //
    // // controllo il related
    // checkRelated(relatedProfile, belongsToOriginalRelatedChain,
    // masterProfile);
    //
    // checkIfNeedsProfileUpdate(relatedProfile, masterProfile,
    // relatedProfileToUpdate);
    //
    // if (belongsToOriginalRelatedChain) {
    // continue;
    // }
    //
    // // *** recupero i related del related da aggiungere ***
    // List<String> relOfRels = provider.getRelatedDocuments(relDocnum);
    // if (relOfRels == null)
    // throw new
    // DocerException("errore recupero related del documento related da aggiungere: "
    // + relDocnum);
    //
    // for (String elem : relOfRels) {
    // if (allRelatedOfRelatedList.contains(elem)) {
    // continue;
    // }
    // allRelatedOfRelatedList.add(elem);
    // }
    //
    // }
    //
    // if (relatedToCheck.size() > 0) {
    // throw new DocerException("alcuni related non sono stati trovati: " +
    // String.valueOf(relatedToCheck));
    // }
    //
    // if (allRelatedOfRelatedList.size() == 0) {
    // return relatedProfileToUpdate;
    // }
    //
    // relatedToCheck.clear();
    // relatedToCheck.addAll(allRelatedOfRelatedList);
    //
    // searchCriteria.clear();
    // searchCriteria.put(Constants.doc_docnum, allRelatedOfRelatedList);
    //
    // // recupero dei profili dei related dei related da aggiungere
    // searchResults = provider.searchDocuments(searchCriteria, null,
    // returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
    //
    // // errore ricerca: nessun related e' presente sul DMS (!!!)
    // if (searchResults == null || searchResults.getRows().size() == 0)
    // throw new
    // DocerException("Errore recupero profili related dei related: nessuno dei related dei related da aggiungere e' stato trovato");
    //
    // // controllo i related dei related da aggiungere
    // for (DataRow<String> relOfRelProfile : searchResults.getRows()) {
    //
    // String relOfRelDocnum = relOfRelProfile.get(Constants.doc_docnum);
    //
    // relatedToCheck.removeAll(Arrays.asList(relOfRelDocnum));
    //
    // // se fa parte della chain originale e' yet checked
    // if (yetChecked.contains(relOfRelDocnum)) {
    // continue;
    // }
    //
    // // controllo del checkout
    // // checkedOutInfo =
    // // (LockStatus)provider.isCheckedOutDocument(relOfRelDocnum);
    // //
    // // if (checkedOutInfo.getLocked()) {
    // // throw new DocerException("il related " + relOfRelDocnum +
    // // " di un related da aggiungere e' in stato di blocco esclusivo");
    // // }
    //
    // yetChecked.add(relOfRelDocnum);
    //
    // boolean belongsToOriginalRelatedChain =
    // originalRelatedChain.contains(relOfRelDocnum);
    //
    // // controllo il related da aggiungere
    // checkRelated(relOfRelProfile, belongsToOriginalRelatedChain,
    // masterProfile);
    //
    // checkIfNeedsProfileUpdate(relOfRelProfile, masterProfile,
    // relatedProfileToUpdate);
    //
    // // *** non serve recuperare i related del related perche' e
    // // ridondante***
    // }
    //
    // return relatedProfileToUpdate;
    // }

    // private void checkIfNeedsProfileUpdate(DataRow<String> relatedProfile,
    // DataRow<String> masterProfile, Map<String, Map<String, String>>
    // toUpdateCollection) {
    //
    // String relDocnum =
    // String.valueOf(relatedProfile.get(Constants.doc_docnum));
    // String relCodEnte =
    // String.valueOf(relatedProfile.get(Constants.doc_cod_ente));
    // String relCodAOO =
    // String.valueOf(relatedProfile.get(Constants.doc_cod_aoo));
    // EnumStatoArchivistico relStatoArchivistico =
    // getEnumStatoArchivistico(relatedProfile.get(Constants.doc_stato_archivistico));
    // EnumTipiComponente relTipoComponente =
    // getEnumTipiComponente(relatedProfile.get(Constants.doc_tipo_componente));
    // String relClassifica =
    // String.valueOf(relatedProfile.get(Constants.doc_classifica));
    // String relProgrFascicolo =
    // String.valueOf(relatedProfile.get(Constants.doc_progr_fascicolo));
    // String relAnnoFascicolo =
    // String.valueOf(relatedProfile.get(Constants.doc_anno_fascicolo));
    // String relFascSecondari =
    // String.valueOf(relatedProfile.get(Constants.doc_fascicoli_secondari));
    // String relNumFascicolo =
    // String.valueOf(relatedProfile.get(Constants.doc_num_fascicolo));
    // String relCodTitolario =
    // String.valueOf(relatedProfile.get(Constants.doc_cod_titolario));
    // String relArchiveType =
    // String.valueOf(relatedProfile.get(Constants.doc_archive_type));
    // EnumStatiPantarei relStatoPantarei =
    // getEnumStatoPantarei(relatedProfile.get(Constants.doc_stato_pantarei));
    // String relDocnumPrinc =
    // String.valueOf(relatedProfile.get(Constants.doc_docnum_princ));
    // EnumStatiConservazione relStatoConservazione =
    // getEnumStatiConservazione(relatedProfile.get(Constants.doc_stato_conserv));
    //
    // String masterDocnum =
    // String.valueOf(masterProfile.get(Constants.doc_docnum));
    // String masterCodEnte =
    // String.valueOf(masterProfile.get(Constants.doc_cod_ente));
    // String masterCodAOO =
    // String.valueOf(masterProfile.get(Constants.doc_cod_aoo));
    // EnumStatoArchivistico masterStatoArchivistico =
    // getEnumStatoArchivistico(masterProfile.get(Constants.doc_stato_archivistico));
    // EnumTipiComponente masterTipoComponente =
    // getEnumTipiComponente(masterProfile.get(Constants.doc_tipo_componente));
    // String masterClassifica =
    // String.valueOf(masterProfile.get(Constants.doc_classifica));
    // String masterProgrFascicolo =
    // String.valueOf(masterProfile.get(Constants.doc_progr_fascicolo));
    // String masterAnnoFascicolo =
    // String.valueOf(masterProfile.get(Constants.doc_anno_fascicolo));
    // String masterFascSecondari =
    // String.valueOf(masterProfile.get(Constants.doc_fascicoli_secondari));
    // String masterNumFascicolo =
    // String.valueOf(masterProfile.get(Constants.doc_num_fascicolo));
    // String masterCodTitolario =
    // String.valueOf(masterProfile.get(Constants.doc_cod_titolario));
    // String masterArchiveType =
    // String.valueOf(masterProfile.get(Constants.doc_archive_type));
    // EnumStatiPantarei masterStatoPantarei =
    // getEnumStatoPantarei(masterProfile.get(Constants.doc_stato_pantarei));
    // EnumStatiConservazione masterStatoConservazione =
    // getEnumStatiConservazione(masterProfile.get(Constants.doc_stato_conserv));
    //
    // boolean needsUpdate = false;
    // if (!masterClassifica.equals(relClassifica)) {
    // needsUpdate = true;
    // }
    // if (!masterCodTitolario.equals(relCodTitolario)) {
    // needsUpdate = true;
    // }
    // if (!masterProgrFascicolo.equals(relProgrFascicolo)) {
    // needsUpdate = true;
    // }
    // if (!masterNumFascicolo.equals(relNumFascicolo)) {
    // needsUpdate = true;
    // }
    // if (!masterAnnoFascicolo.equals(relAnnoFascicolo)) {
    // needsUpdate = true;
    // }
    // if (!masterFascSecondari.equals(relFascSecondari)) {
    // needsUpdate = true;
    // }
    //
    // // se sono arrivato qui ed e' un record allora e' di sicuro un
    // // PRINCIPALE
    // if (masterTipoComponente.equals(EnumTipiComponente.PRINCIPALE) &&
    // masterStatoArchivistico.getCode() >
    // EnumStatoArchivistico.generico_definitivo.getCode()) {
    //
    // if (!masterStatoArchivistico.equals(relStatoArchivistico)) {
    // needsUpdate = true;
    // }
    // if (!relDocnumPrinc.equals(masterDocnum)) {
    // needsUpdate = true;
    // }
    // if (relTipoComponente.equals(EnumTipiComponente.UNDEFINED)) {
    // needsUpdate = true;
    // }
    // }
    //
    // if (masterStatoPantarei.getCode() > 3) {
    // if (!relStatoPantarei.equals(EnumStatiPantarei.allegato)) {
    // needsUpdate = true;
    // }
    // }
    //
    // if (needsUpdate) {
    //
    // Map<String, String> relNewProperties = new HashMap<String, String>();
    // toUpdateCollection.put(relDocnum, relNewProperties);
    //
    // relNewProperties.put(Constants.doc_cod_ente, masterCodEnte);
    // relNewProperties.put(Constants.doc_cod_aoo, masterCodAOO);
    // relNewProperties.put(Constants.doc_classifica, masterClassifica);
    // relNewProperties.put(Constants.doc_progr_fascicolo,
    // masterProgrFascicolo);
    // relNewProperties.put(Constants.doc_anno_fascicolo, masterAnnoFascicolo);
    // relNewProperties.put(Constants.doc_fascicoli_secondari,
    // masterFascSecondari);
    // relNewProperties.put(Constants.doc_cod_titolario, masterCodTitolario);
    // relNewProperties.put(Constants.doc_num_fascicolo, masterNumFascicolo);
    //
    // if (masterTipoComponente.equals(EnumTipiComponente.PRINCIPALE) &&
    // masterStatoArchivistico.getCode() >
    // EnumStatoArchivistico.generico_definitivo.getCode()) {
    // relNewProperties.put(Constants.doc_stato_archivistico,
    // String.valueOf(masterStatoArchivistico.getCode()));
    //
    // relNewProperties.put(Constants.doc_docnum_princ, masterDocnum);
    //
    // if (relTipoComponente.equals(EnumTipiComponente.UNDEFINED)) {
    // relNewProperties.put(Constants.doc_tipo_componente,
    // EnumTipiComponente.ALLEGATO.toString());
    // }
    // }
    //
    // if (masterStatoPantarei.getCode() > 3) {
    // relNewProperties.put(Constants.doc_stato_pantarei,
    // Integer.toString(EnumStatiPantarei.allegato.getCode()));
    // }
    // }
    //
    // // gestisco lo stato conservazione dei related
    // if (relStatoConservazione.equals(EnumStatiConservazione.da_conservare) &&
    // masterTipoComponente.equals(EnumTipiComponente.PRINCIPALE) &&
    // masterStatoConservazione.equals(EnumStatiConservazione.conservato)) {
    //
    // Map<String, String> relNewProperties = toUpdateCollection.get(relDocnum);
    // if (relNewProperties == null) {
    //
    // // il profilo del related non era da aggiornare (non lo abbiamo
    // // inserito prima)
    // relNewProperties = new HashMap<String, String>();
    //
    // toUpdateCollection.put(relDocnum, relNewProperties);
    //
    // if (masterTipoComponente.equals(EnumTipiComponente.PRINCIPALE) &&
    // masterStatoArchivistico.getCode() >
    // EnumStatoArchivistico.generico_definitivo.getCode()) {
    // relNewProperties.put(Constants.doc_stato_archivistico,
    // String.valueOf(masterStatoArchivistico.getCode()));
    //
    // relNewProperties.put(Constants.doc_docnum_princ, masterDocnum);
    //
    // if (relTipoComponente.equals(EnumTipiComponente.UNDEFINED)) {
    // relNewProperties.put(Constants.doc_tipo_componente,
    // EnumTipiComponente.ALLEGATO.toString());
    // }
    // }
    //
    // if (masterStatoPantarei.getCode() > 3) {
    // relNewProperties.put(Constants.doc_stato_pantarei,
    // Integer.toString(EnumStatiPantarei.allegato.getCode()));
    // }
    //
    // }
    //
    // relNewProperties.put(Constants.doc_stato_conserv,
    // String.valueOf(EnumStatiConservazione.da_aggiornare.getCode()));
    //
    // }
    //
    // }
    //
    // private void checkRelated(DataRow<String> relatedProfile, boolean
    // belongsToOriginalRelatedChain, DataRow<String> masterProfile) throws
    // DocerException {
    //
    // String masterDocid = masterProfile.get(Constants.doc_docnum);
    // String masterCodEnte = masterProfile.get(Constants.doc_cod_ente);
    // String masterCodAOO = masterProfile.get(Constants.doc_cod_aoo);
    // EnumStatoArchivistico masterStatoArchivistico =
    // getEnumStatoArchivistico(masterProfile.get(Constants.doc_stato_archivistico));
    // String masterArchiveType = masterProfile.get(Constants.doc_archive_type);
    //
    // String relatedId = relatedProfile.get(Constants.doc_docnum);
    // EnumStatoArchivistico relStatoArchivistico =
    // getEnumStatoArchivistico(relatedProfile.get(Constants.doc_stato_archivistico));
    // EnumStatiPantarei relStatoPantarei =
    // getEnumStatoPantarei(relatedProfile.get(Constants.doc_stato_pantarei));
    // String relCodEnte = relatedProfile.get(Constants.doc_cod_ente);
    // String relCodAOO = relatedProfile.get(Constants.doc_cod_aoo);
    // String relDocnumPrinc = relatedProfile.get(Constants.doc_docnum_princ);
    // if (relCodEnte == null) {
    // relCodEnte = "";
    // }
    // if (relCodAOO == null) {
    // relCodAOO = "";
    // }
    // if (relDocnumPrinc == null) {
    // relDocnumPrinc = "";
    // }
    //
    // EnumTipiComponente relTipoComponente =
    // getEnumTipiComponente(relatedProfile.get(Constants.doc_tipo_componente));
    // // non possiamo invocare addRelated per il documento se c'e' un altro
    // // PRINCIPALE nella catena
    // if (relTipoComponente.equals(EnumTipiComponente.PRINCIPALE)) {
    // if (!belongsToOriginalRelatedChain) {
    // throw new DocerException("il related " + relatedId + " ha " +
    // Constants.doc_tipo_componente +
    // " PRINCIPALE e non e' ammesso aggiungerlo alla catena dei related");
    // }
    // throw new
    // DocerException("non e' ammesso aggiungere related al documento " +
    // masterDocid + " in quanto esiste un altro documento (" + relatedId +
    // ") della catena dei related con " + Constants.doc_tipo_componente +
    // " PRINCIPALE");
    // }
    //
    // // controllo stato archivistico del related solo se non fa parte gia'
    // // della catena dei related (add related parzialmente riuscito)
    // if (relStatoArchivistico.getCode() >
    // EnumStatoArchivistico.generico_definitivo.getCode()) {
    // if (!belongsToOriginalRelatedChain) {
    // throw new DocerException("il related da aggiungere " + relatedId +
    // " ha STATO_ARCHIVISTICO " + relStatoArchivistico + " (" +
    // relStatoArchivistico.getCode() + ")");
    // }
    // }
    //
    // // controlliamo DOCNUM_PRINC e che non faccia riferimento ad un
    // // altro documento principale
    // if (!relDocnumPrinc.equals(VOID) && !relDocnumPrinc.equals(masterDocid))
    // {
    // if (!belongsToOriginalRelatedChain) {
    // throw new DocerException("il related da aggiungere " + relatedId +
    // " fa riferimento ad un altro documento principale: DOCNUM_PRINC=" +
    // relDocnumPrinc);
    // }
    // }
    //
    // // dopo essere diventato un record possiamo solo aggiungere annotazioni
    // // e annessi
    // if ((masterStatoArchivistico.getCode() >
    // EnumStatoArchivistico.generico_definitivo.getCode()) &&
    // !relTipoComponente.equals(EnumTipiComponente.ANNESSO) &&
    // !relTipoComponente.equals(EnumTipiComponente.ANNOTAZIONE)) {
    //
    // // modifica del 14/12/2012
    // if (!belongsToOriginalRelatedChain && !allow_record_add_allegato) {
    //
    // if (!masterArchiveType.equals(PAPER)) {
    // throw new DocerException("per un documento in STATO_ARCHIVISTICO " +
    // masterStatoArchivistico + " e ARCHIVE_TYPE=" + masterArchiveType +
    // " e' ammessa solo l'aggiunta di related con " +
    // Constants.doc_tipo_componente +
    // " ANNESSO o ANNOTAZIONE: il related da aggiungere " + relatedId + " ha "
    // + Constants.doc_tipo_componente + " " + relTipoComponente);
    // }
    // }
    // }
    //
    // // controlliamo cod_ente e cod_aoo
    // if (relCodEnte.equals(VOID)) {
    // throw new DocerException("il related " + relatedId +
    // " non ha COD_ENTE assegnato");
    // }
    // // controlliamo cod_ente e cod_aoo
    // if (relCodAOO.equals(VOID)) {
    // throw new DocerException("il related " + relatedId +
    // " non ha COD_AOO assegnato");
    // }
    //
    // if (!relCodEnte.equals(masterCodEnte)) {
    // throw new DocerException("il related " + relatedId +
    // " ha COD_ENTE diverso da quello del documento " + masterDocid + ": " +
    // masterCodEnte);
    // }
    //
    // if (!relCodAOO.equals(masterCodAOO)) {
    // throw new DocerException("il related " + relatedId +
    // " ha COD_AOO diverso da quello del documento " + masterDocid + ": " +
    // masterCodAOO);
    // }
    //
    // // controllo stato pantarei del related
    // if (relStatoPantarei.getCode() > 3) {
    //
    // if (!belongsToOriginalRelatedChain) {
    // throw new
    // DocerException("compatibilita' DOCAREA: il related da aggiungere " +
    // relatedId + " ha STATO_PANTAREI: " + relStatoPantarei + " (" +
    // relStatoPantarei.getCode() + ")");
    // }
    //
    // // c'e' un related che e' un master per DOCAREA (non dovrebbe mai
    // // accadere ma lo controllo)
    // if (relStatoPantarei.getCode() != 6) {
    // throw new DocerException("compatibilita' DOCAREA: il related " +
    // relatedId + " ha STATO_PANTAREI: " + relStatoPantarei + " (" +
    // relStatoPantarei.getCode() + ")");
    // }
    // }
    // }

    public static EnumStatiConservazione getEnumStatiConservazione(String statoConservazione) {

	try {
	    EnumStatiConservazione value = EnumStatiConservazione.getCode(Integer.parseInt(statoConservazione));

	    if (value == null) {
		value = EnumStatiConservazione.da_non_conservare;
	    }
	    return value;
	}
	catch (Exception e) {
	    return EnumStatiConservazione.da_non_conservare;
	}
    }

    public static EnumTipiComponente getEnumTipiComponente(String tipoComponente) {

	if (tipoComponente == null)
	    return EnumTipiComponente.UNDEFINED;

	if (tipoComponente.equalsIgnoreCase("PRINCIPALE"))
	    return EnumTipiComponente.PRINCIPALE;

	if (tipoComponente.equalsIgnoreCase("ALLEGATO"))
	    return EnumTipiComponente.ALLEGATO;

	if (tipoComponente.equalsIgnoreCase("ANNOTAZIONE"))
	    return EnumTipiComponente.ANNOTAZIONE;

	if (tipoComponente.equalsIgnoreCase("ANNESSO"))
	    return EnumTipiComponente.ANNESSO;

	return EnumTipiComponente.UNDEFINED;
    }

    public static EnumStatiPantarei getEnumStatoPantarei(String statoPantarei) {

	try {
	    EnumStatiPantarei value = EnumStatiPantarei.getCode(Integer.parseInt(statoPantarei));

	    if (value == null) {
		value = EnumStatiPantarei.undefined;
	    }
	    return value;
	}
	catch (Exception e) {
	    return EnumStatiPantarei.undefined;
	}

    }

    public static EnumStatoArchivistico getEnumStatoArchivistico(String statoArchivistico) {

	try {
	    EnumStatoArchivistico value = EnumStatoArchivistico.getCode(Integer.parseInt(statoArchivistico));

	    if (value == null) {
		value = EnumStatoArchivistico.undefined;
	    }
	    return value;
	}
	catch (Exception e) {
	    return EnumStatoArchivistico.undefined;
	}

    }

    public static EnumBoolean getEnumBoolean(String value) {

	if (value == null)
	    return EnumBoolean.UNSPECIFIED;

	if (value.equals("1") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("YES"))
	    return EnumBoolean.TRUE;

	if (value.equals("0") || value.equalsIgnoreCase("FALSE") || value.equalsIgnoreCase("N") || value.equalsIgnoreCase("NO"))
	    return EnumBoolean.FALSE;

	return EnumBoolean.UNSPECIFIED;
    }

    public static EnumACLRights getEnumACLRights(String right) {

	try {
	    EnumACLRights value = EnumACLRights.getCode(Integer.parseInt(right));

	    if (value == null) {
		value = EnumACLRights.undefined;
	    }
	    return value;
	}
	catch (Exception e) {
	    return EnumACLRights.undefined;
	}

    }

    private void checkEnte(String codEnte) throws DocerException {

	if (codEnte == null)
	    throw new DocerException("Ente " + codEnte + " non trovato");

	Map<String, String> idCrit = new HashMap<String, String>();
	idCrit.put(Constants.aoo_cod_ente, codEnte);

	Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCrit);
	if (p == null)
	    throw new DocerException("Ente " + codEnte + " non trovato");

	// if
	// (p.get(Constants.ente_enabled).equalsIgnoreCase(EnumBoolean.FALSE.toString()))
	// throw new DocerException("Ente " + codEnte + " disabilitato");

    }

    private void checkAOO(String codAoo, String codEnte) throws DocerException {

	if (codAoo == null)
	    throw new DocerException("AOO " + codAoo + " non trovata. Ente: " + codEnte);

	Map<String, String> idCrit = new HashMap<String, String>();
	idCrit.put(Constants.aoo_cod_ente, codEnte);
	idCrit.put(Constants.aoo_cod_aoo, codAoo);

	Map<String, String> p = getAnagraficaProfile(Constants.aoo_type_id, idCrit);
	if (p == null)
	    throw new DocerException("AOO " + codAoo + " non trovata. Ente: " + codEnte);

	// if
	// (p.get(Constants.aoo_enabled).equalsIgnoreCase(EnumBoolean.FALSE.toString()))
	// throw new DocerException("AOO " + codAoo + " disabilitata. Ente: " +
	// codEnte);

    }

    private EnumStatoArchivistico checkStatoArchivisticoClassificaDocumento(EnumStatoArchivistico oldStatoArchivistico) throws DocerException {

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico)) {
	    return EnumStatoArchivistico.generico;
	}

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico_definitivo)) {
	    return EnumStatoArchivistico.generico_definitivo;
	}

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato) || oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato)
		|| oldStatoArchivistico.equals(EnumStatoArchivistico.classificato)) {
	    return EnumStatoArchivistico.classificato;
	}

	throw new DocerException("non e' permessa l'invocazione del metodo classificaDocumento per un documento con STATO_ARCHIVISTICO " + oldStatoArchivistico + " (" + oldStatoArchivistico.getCode()
		+ ")");

    }

    // private void checkPropertiesCreateDocument(Map<String, String> metadata)
    // throws DocerException {
    //
    // for (String propName : metadata.keySet()) {
    //
    // String propValue = metadata.get(propName);
    //
    // // if (propValue == null)
    // // continue;
    //
    // if (propName.equalsIgnoreCase(Constants.doc_stato_pantarei)) {
    //
    // EnumStatiPantarei statoPantarei = getEnumStatoPantarei(propValue);
    //
    // if (!statoPantarei.equals(EnumStatiPantarei.generico) &&
    // !statoPantarei.equals(EnumStatiPantarei.daProtocollare) &&
    // !statoPantarei.equals(EnumStatiPantarei.daFascicolare)) {
    // throw new
    // DocerException("compatibilita' DOCAREA: non e' permesso creare un documento con "
    // + Constants.doc_stato_pantarei + " " + statoPantarei + " (" +
    // statoPantarei.getCode() + ")");
    // }
    //
    // continue;
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_stato_archivistico)) {
    //
    // EnumStatoArchivistico statoArchivistico =
    // getEnumStatoArchivistico(propValue);
    //
    // if (statoArchivistico.equals(EnumStatoArchivistico.generico) ||
    // statoArchivistico.equals(EnumStatoArchivistico.generico_definitivo)) {
    // continue;
    // }
    //
    // throw new DocerException("il metadato " + propName +
    // " e' assegnabile dall'esterno solo per i valori " +
    // EnumStatoArchivistico.generico + " (" +
    // EnumStatoArchivistico.generico.getCode() + ") e " +
    // EnumStatoArchivistico.generico_definitivo + " (" +
    // EnumStatoArchivistico.generico_definitivo.getCode() + ")");
    // }
    // // metadati interni
    // if (propName.equalsIgnoreCase(Constants.doc_docnum_princ) ||
    // propName.equalsIgnoreCase(Constants.doc_app_id) ||
    // propName.equalsIgnoreCase(Constants.doc_cod_titolario) ||
    // propName.equalsIgnoreCase(Constants.doc_num_fascicolo) ||
    // propName.equalsIgnoreCase(Constants.doc_docnum_record)) {
    // throw new DocerException("il metadato " + propName +
    // " non e' assegnabile dall'esterno");
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_docnum)) {
    // if (!MIGRATION_MODE)
    // throw new DocerException("il metadato  " + Constants.doc_docnum +
    // " puo' essere assegnato dall'esterno solo se MIGRATION_MODE=true");
    //
    // if (propValue == null) {
    // throw new DocerException("MIGRATION MODE: il metadato " +
    // Constants.doc_docnum + " non puo' essere impostato a null");
    // }
    //
    // continue;
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_classifica)) {
    // throw new
    // DocerException("il metodo createDocument non permette di specificare il campo "
    // + propName.toUpperCase() +
    // " di un documento. Per la classificazione utilizzare il metodo classificaDocumento");
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_protocollo_registro) ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_oggetto) ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_data) ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_numero) ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_anno) ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_annullato_protocollo)
    // ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_data_annullamento_protocollo)
    // ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_motivo_annullamento_protocollo))
    // {
    // throw new
    // DocerException("il metodo createDocument non permette di specificare il campo "
    // + propName.toUpperCase() +
    // " di un documento. Per la protocollazione utilizzare il metodo protocollaDocumento");
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_progr_fascicolo) ||
    // propName.equalsIgnoreCase(Constants.doc_anno_fascicolo) ||
    // propName.equalsIgnoreCase(Constants.doc_fascicoli_secondari)) {
    // throw new
    // DocerException("il metodo createDocument non permette di specificare il campo "
    // + propName.toUpperCase() +
    // " di un documento. Per la fascicolazione utilizzare il metodo fascicolaDocumento");
    // }
    //
    // // metadati di registrazione
    // if (propName.equalsIgnoreCase(Constants.doc_registrazione_anno) ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_data) ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_id_registro) ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_numero) ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_oggetto) ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_annullata_registrazione)
    // ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_data_annullamento_registrazione)
    // ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_motivo_annullamento_registrazione))
    // {
    // throw new
    // DocerException("il metodo createDocument non permette di specificare il campo "
    // + propName.toUpperCase() +
    // " di un documento. Per la registrazione utilizzare il metodo registraDocumento");
    // }
    //
    // // metadati di pubblicazione
    // if (propName.equalsIgnoreCase(Constants.doc_pubblicazione_registro) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_anno) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_fine) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_inizio) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_numero) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_oggetto) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_pubblicato)) {
    // throw new
    // DocerException("il metodo createDocument non permette di specificare il campo "
    // + propName.toUpperCase() +
    // " di un documento. Per la pubblicazione utilizzare il metodo pubblicaDocumento");
    // }
    //
    // }
    //
    // }
    //
    // private void manageStatoConservazione(DataRow<String> oldProfileData,
    // Map<String, String> newMetadata) throws DocerException {
    //
    // if (!newMetadata.containsKey(Constants.doc_stato_conserv)) {
    // return;
    // }
    //
    // // se modificano pure il tipo_componente la leicita' della modifica e'
    // // controllata dal metodo prima
    // String docnumPrinc = oldProfileData.get(Constants.doc_docnum_princ);
    // EnumTipiComponente oldTipoComponente =
    // getEnumTipiComponente(oldProfileData.get(Constants.doc_tipo_componente));
    // EnumTipiComponente newTipoComponente =
    // getEnumTipiComponente(newMetadata.get(Constants.doc_tipo_componente));
    // if (newTipoComponente.equals(EnumTipiComponente.UNDEFINED)) {
    // newTipoComponente = oldTipoComponente;
    // }
    //
    // if (newTipoComponente.equals(EnumTipiComponente.PRINCIPALE) ||
    // newTipoComponente.equals(EnumTipiComponente.UNDEFINED)) {
    // return;
    // }
    //
    // EnumStatiConservazione oldStatoConserv =
    // getEnumStatiConservazione(oldProfileData.get(Constants.doc_stato_conserv));
    // EnumStatiConservazione newStatoConserv =
    // getEnumStatiConservazione(newMetadata.get(Constants.doc_stato_conserv));
    //
    // boolean conservazioneDaAggiornare = false;
    // if (oldStatoConserv.equals(EnumStatiConservazione.da_non_conservare) &&
    // newStatoConserv.equals(EnumStatiConservazione.da_conservare)) {
    // conservazioneDaAggiornare = true;
    // }
    //
    // if (!conservazioneDaAggiornare) {
    // return;
    // }
    //
    // // se sono qui e' un ALLEGATO, ANNESSO, ANNOTAZIONE
    // EnumStatiConservazione statoConservDocPrincipale =
    // EnumStatiConservazione.da_non_conservare;
    //
    // Map<String, List<String>> searchCriteria = new HashMap<String,
    // List<String>>();
    // List<String> returnProperties = new ArrayList<String>();
    // List<String> criteria = new ArrayList<String>();
    //
    // // creo i criteri di ricerca
    // searchCriteria.put(Constants.doc_docnum, criteria);
    //
    // // le proprieta' di ritorno
    // returnProperties.add(Constants.doc_stato_conserv);
    // returnProperties.add(Constants.doc_tipo_componente);
    // returnProperties.add(Constants.doc_docnum);
    // returnProperties.add(Constants.doc_docnum_princ);
    //
    // // se nel profilo del related non e' definito il docnumPrincipale
    // // ricerco il principale
    // if (docnumPrinc == null || docnumPrinc.equals(VOID)) {
    //
    // String docId = oldProfileData.get(Constants.doc_docnum);
    // List<String> relatedChain = provider.getRelatedDocuments(docId);
    //
    // if (relatedChain == null || relatedChain.size() == 0) {
    // return;
    // }
    //
    // for (String relId : relatedChain) {
    //
    // if (relId.equals(docId)) {
    // continue;
    // }
    //
    // if (!criteria.contains(relId))
    // criteria.add(relId);
    // }
    //
    // if (criteria.size() < 1) {
    // return;
    // }
    //
    // // cerco lo STATO_CONSERV del PRINCIPALE
    // DataTable<String> searchResultsRelated =
    // provider.searchDocuments(searchCriteria, null, returnProperties,
    // PRIMARYSEARCH_MAX_ROWS, null);
    // for (DataRow<String> dr : searchResultsRelated.getRows()) {
    // EnumTipiComponente tipoComponenteCandidate =
    // getEnumTipiComponente(dr.get(Constants.doc_tipo_componente));
    // EnumStatiConservazione statoConservCandidate =
    // getEnumStatiConservazione(dr.get(Constants.doc_stato_conserv));
    // String docnumCandidate = dr.get(Constants.doc_docnum);
    // if (tipoComponenteCandidate.equals(EnumTipiComponente.PRINCIPALE)) {
    // statoConservDocPrincipale = statoConservCandidate;
    // docnumPrinc = docnumCandidate;
    // break;
    // }
    // }
    //
    // }
    // else {
    // criteria.add(docnumPrinc);
    // DataTable<String> results = provider.searchDocuments(searchCriteria,
    // null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
    // if (results == null || results.getRows().size() == 0) {
    // return;
    // }
    //
    // EnumTipiComponente tipoComponenteCandidate =
    // getEnumTipiComponente(results.getRow(0).get(Constants.doc_tipo_componente));
    // EnumStatiConservazione statoConservCandidate =
    // getEnumStatiConservazione(results.getRow(0).get(Constants.doc_stato_conserv));
    //
    // if (tipoComponenteCandidate.equals(EnumTipiComponente.PRINCIPALE)) {
    // statoConservDocPrincipale = statoConservCandidate;
    // }
    //
    // }
    //
    // if (statoConservDocPrincipale.equals(EnumStatiConservazione.conservato))
    // {
    // newMetadata.put(Constants.doc_stato_conserv,
    // String.valueOf(EnumStatiConservazione.da_aggiornare.getCode()));
    // newMetadata.put(Constants.doc_docnum_princ, docnumPrinc);
    // }
    // }
    //
    // private void checkPropertiesUpdateProfile(DataRow<String> oldProfileData,
    // Map<String, String> newMetadata) throws DocerException {
    //
    // List<String> relatedChain = null;
    // DataTable<String> searchResultsRelated = null;
    //
    // EnumStatoArchivistico oldStatoArchivistico =
    // getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));
    // boolean oldPubblicato =
    // Boolean.parseBoolean(oldProfileData.get(Constants.doc_pubblicazione_pubblicato));
    //
    // for (String propName : newMetadata.keySet()) {
    //
    // if (propName.equalsIgnoreCase(Constants.doc_stato_pantarei)) {
    //
    // EnumStatiPantarei oldStatoPantarei =
    // getEnumStatoPantarei(oldProfileData.get(Constants.doc_stato_pantarei));
    // EnumStatiPantarei newStatoPantarei =
    // getEnumStatoPantarei(newMetadata.get(Constants.doc_stato_pantarei));
    //
    // if (!oldStatoPantarei.equals(newStatoPantarei)) {
    //
    // if (newStatoPantarei.equals(EnumStatiPantarei.undefined) ||
    // newStatoPantarei.equals(EnumStatiPantarei.protocollato) ||
    // newStatoPantarei.equals(EnumStatiPantarei.fascicolato) ||
    // newStatoPantarei.equals(EnumStatiPantarei.allegato)) {
    // throw new
    // DocerException("compatibilita' DOCAREA: non e' permesso modificare STATO_PANTAREI dal valore "
    // + oldStatoPantarei + " (" + oldStatoPantarei.getCode() + ")" +
    // " al valore " + newStatoPantarei + " (" + newStatoPantarei.getCode() +
    // ")");
    // }
    //
    // if (oldStatoPantarei.equals(EnumStatiPantarei.protocollato) ||
    // oldStatoPantarei.equals(EnumStatiPantarei.fascicolato) ||
    // oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {
    // throw new
    // DocerException("compatibilita' DOCAREA: non e' permesso modificare STATO_PANTAREI dal valore "
    // + oldStatoPantarei + " (" + oldStatoPantarei.getCode() + ")" +
    // " al valore " + newStatoPantarei + " (" + newStatoPantarei.getCode() +
    // ")");
    // }
    //
    // }
    //
    // continue;
    // }
    //
    // if (propName.equals(Constants.doc_tipo_componente)) {
    //
    // EnumTipiComponente oldTipoComponente =
    // getEnumTipiComponente(oldProfileData.get(Constants.doc_tipo_componente));
    // EnumTipiComponente newTipoComponente =
    // getEnumTipiComponente(newMetadata.get(propName));
    //
    // if (!newTipoComponente.equals(oldTipoComponente)) {
    //
    // if (oldStatoArchivistico.getCode() >
    // EnumStatoArchivistico.generico_definitivo.getCode()) {
    // throw new DocerException("non e' permesso modificare il campo " +
    // Constants.doc_tipo_componente +
    // " di un documento con STATO_ARCHIVISTICO " + oldStatoArchivistico + " ("
    // + oldStatoArchivistico.getCode() + ")");
    // }
    //
    // if (oldPubblicato) {
    // throw new DocerException("non e' permesso modificare il campo " +
    // Constants.doc_tipo_componente + " di un documento PUBBLICATO");
    // }
    // }
    //
    // if (!oldTipoComponente.equals(newTipoComponente) &&
    // newTipoComponente.equals(EnumTipiComponente.PRINCIPALE)) {
    //
    // String docId = oldProfileData.get(Constants.doc_docnum);
    //
    // if (relatedChain == null) {
    // relatedChain = provider.getRelatedDocuments(docId);
    // }
    //
    // if (relatedChain == null || relatedChain.size() == 0) {
    // continue;
    // }
    //
    // // controllo i metadati dei related da aggiungere
    // Map<String, List<String>> searchCriteria = new HashMap<String,
    // List<String>>();
    //
    // List<String> criteria = new ArrayList<String>();
    //
    // for (String relId : relatedChain) {
    //
    // if (relId.equals(docId)) {
    // continue;
    // }
    //
    // if (!criteria.contains(relId))
    // criteria.add(relId);
    // }
    //
    // if (criteria.size() < 1) {
    // continue;
    // }
    //
    // // creo i criteri di ricerca
    // searchCriteria.put(Constants.doc_docnum, criteria);
    //
    // // le proprieta' di ritorno
    // // metadati che voglio ricercare
    // List<String> returnProperties = new ArrayList<String>();
    // returnProperties.add(Constants.doc_tipo_componente);
    // returnProperties.add(Constants.doc_docnum);
    //
    // // recupero il TIPO_COMPONENTE dei related
    // searchResultsRelated = provider.searchDocuments(searchCriteria, null,
    // returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
    //
    // // errore ricerca: nessun related e' presente sul
    // // DMS (!!!)
    // if (searchResultsRelated == null || searchResultsRelated.getRows().size()
    // == 0)
    // throw new
    // DocerException("Errore recupero profili catena dei related: nessun documento della related chain trovato");
    //
    // for (DataRow<String> dr : searchResultsRelated.getRows()) {
    //
    // EnumTipiComponente relTipoComponente =
    // getEnumTipiComponente(dr.get(Constants.doc_tipo_componente));
    //
    // // non possiamo aggiungere altri PRINCIPALI
    // if (relTipoComponente.equals(EnumTipiComponente.PRINCIPALE)) {
    // throw new DocerException("non e' permesso modificare il campo " +
    // propName + " da " + oldTipoComponente + " a " + newTipoComponente +
    // " in quanto la catena dei Related contiene gia' un documento (" +
    // dr.get(Constants.doc_docnum) + ") con " + Constants.doc_tipo_componente +
    // " PRINCIPALE");
    // }
    //
    // }
    //
    // }
    //
    // continue;
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_classifica) ||
    // propName.equalsIgnoreCase(Constants.doc_cod_titolario)) {
    // throw new
    // DocerException("il metodo updateProfileDocument non permette di specificare il campo "
    // + propName +
    // " di un documento. Per la classificazione utilizzare il metodo classificaDocumento");
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_protocollo_registro) ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_oggetto) ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_data) ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_numero) ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_anno) ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_annullato_protocollo)
    // ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_data_annullamento_protocollo)
    // ||
    // propName.equalsIgnoreCase(Constants.doc_protocollo_motivo_annullamento_protocollo))
    // {
    // throw new
    // DocerException("il metodo updateProfileDocument non permette di specificare il campo "
    // + propName +
    // " di un documento. Per la protocollazione utilizzare il metodo protocollaDocumento");
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_progr_fascicolo) ||
    // propName.equalsIgnoreCase(Constants.doc_anno_fascicolo) ||
    // propName.equalsIgnoreCase(Constants.doc_fascicoli_secondari) ||
    // propName.equalsIgnoreCase(Constants.doc_num_fascicolo)) {
    // throw new
    // DocerException("il metodo updateProfileDocument non permette di specificare il campo "
    // + propName +
    // " di un documento. Per la fascicolazione utilizzare il metodo fascicolaDocumento");
    // }
    //
    // // metadati di registrazione
    // if (propName.equalsIgnoreCase(Constants.doc_registrazione_anno) ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_data) ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_id_registro) ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_numero) ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_oggetto)
    // // nuovi metadati 25-10-2012
    // ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_annullata_registrazione)
    // ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_data_annullamento_registrazione)
    // ||
    // propName.equalsIgnoreCase(Constants.doc_registrazione_motivo_annullamento_registrazione))
    // {
    // throw new
    // DocerException("il metodo updateProfileDocument non permette di specificare il campo "
    // + propName +
    // " di un documento. Per la registrazione utilizzare il metodo registraDocumento");
    // }
    //
    // // metadati di pubblicazione
    // if (propName.equalsIgnoreCase(Constants.doc_pubblicazione_registro) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_anno) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_fine) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_inizio) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_numero) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_oggetto) ||
    // propName.equalsIgnoreCase(Constants.doc_pubblicazione_pubblicato)) {
    // throw new
    // DocerException("il metodo updateProfileDocument non permette di specificare il campo "
    // + propName +
    // " di un documento. Per la pubblicazione utilizzare il metodo pubblicaDocumento");
    // }
    //
    // String newValue = newMetadata.get(propName);
    // String oldValue = oldProfileData.get(propName);
    //
    // if (newValue == null && oldValue == null)
    // continue; // non cambiano
    //
    // if (newValue != null && oldValue != null && newValue.equals(oldValue))
    // continue; // non cambiano
    //
    // // il value cambia
    // if (propName.equalsIgnoreCase(Constants.doc_archive_type)) {
    //
    // if (oldValue == null || oldValue.equals(VOID)) {
    // if (newValue != null && !newValue.equals(URL)) {
    // continue; // ammesso solo modifica da vuoto ad ARCHIVE o
    // // PAPER
    // }
    // }
    //
    // throw new DocerException("non e' possibile modificare il campo " +
    // propName + " di un documento da " + oldValue + " a " + newValue);
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_docnum_record)) {
    // throw new
    // DocerException("non e' possibile modificare esplicitamente il campo " +
    // propName + " di un documento");
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_stato_archivistico)) {
    //
    // EnumStatoArchivistico newStatoArch = getEnumStatoArchivistico(newValue);
    // EnumStatoArchivistico oldStatoArch = getEnumStatoArchivistico(oldValue);
    //
    // if (!oldStatoArch.equals(EnumStatoArchivistico.generico) ||
    // !newStatoArch.equals(EnumStatoArchivistico.generico_definitivo)) {
    //
    // throw new DocerException("e' possibile modificare il campo " + propName +
    // " di un documento limitatamente al passaggio dal valore " +
    // String.valueOf(EnumStatoArchivistico.generico.getCode()) + " (" +
    // EnumStatoArchivistico.generico + ") al valore " +
    // String.valueOf(EnumStatoArchivistico.generico_definitivo.getCode()) +
    // " (" + EnumStatoArchivistico.generico_definitivo + ")");
    // }
    // continue;
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_cod_ente) ||
    // propName.equalsIgnoreCase(Constants.doc_cod_aoo) ||
    // propName.equalsIgnoreCase(Constants.doc_docnum) ||
    // propName.equalsIgnoreCase(Constants.doc_app_id) ||
    // propName.equalsIgnoreCase(Constants.doc_docnum_princ)
    // // || propName.equalsIgnoreCase(Constants.doc_stato_archivistico)
    // || propName.equalsIgnoreCase(Constants.doc_stato_pantarei)) {
    // throw new DocerException("non e' possibile modificare il campo " +
    // propName + " di un documento");
    // }
    //
    // if (oldPubblicato) {
    //
    // if (propName.equalsIgnoreCase(Constants.doc_creation_date)) {
    // throw new DocerException("non e' possibile modificare " + propName +
    // " di un documento PUBBLICATO");
    // }
    // }
    //
    // // lista dei metadati non modificabili
    // if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato) ||
    // oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato) ||
    // oldStatoArchivistico.equals(EnumStatoArchivistico.fascicolato) ||
    // oldStatoArchivistico.equals(EnumStatoArchivistico.classificato)) {
    //
    // if (propName.equalsIgnoreCase(Constants.doc_creation_date)) {
    // throw new DocerException("non e' possibile modificare " + propName +
    // " di un documento con " + Constants.doc_stato_archivistico + " " +
    // oldStatoArchivistico + " (" + oldStatoArchivistico.getCode() + ")");
    // }
    // }
    //
    // } // fine ciclo for
    //
    // }

    private void checkIfPropertyChanges(String methodName, String fieldName, EnumStatoArchivistico oldStatoArchivistico, String oldValue, String newValue) throws DocerException {

	String operation = "";
	if (methodName.startsWith("registra"))
	    operation = "registrazione";
	else if (methodName.startsWith("classifica"))
	    operation = "classificazione";
	else if (methodName.startsWith("protocolla"))
	    operation = "protocollazione";
	else if (methodName.startsWith("pubblica"))
	    operation = "pubblicazione";
	else if (methodName.startsWith("archivia"))
	    operation = "archivio di deposito";

	String messageStart1 = methodName + ": non e' ammesso eseguire l'operazione di " + operation;
	String messageStart2 = methodName + ": non e' ammessa la modifica del campo " + fieldName;
	String messageEnd = " per un documento con " + Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " (" + oldStatoArchivistico.getCode() + ")";

	if (operation.equals("pubblicazione")) {
	    messageEnd = " per un documento PUBBLICATO";
	}

	if (oldValue == null || oldValue.equals(VOID)) {

	    if (newValue != null && !newValue.equals(VOID))
		throw new DocerException(messageStart1 + messageEnd);

	    return;
	}

	if (oldValue != null) {

	    if (newValue == null)
		newValue = VOID;

	    if (!oldValue.equals(newValue)) {
		throw new DocerException(messageStart2 + messageEnd);
	    }
	}
    }

    private void forbidRegistrationChanges(DataRow<String> oldProfile, Map<String, String> metadatiRegistrazione) throws DocerException {

	EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

	String newRegIdReg = metadatiRegistrazione.get(Constants.doc_registrazione_id_registro);
	String newRegNumero = metadatiRegistrazione.get(Constants.doc_registrazione_numero);

	String newRegData = metadatiRegistrazione.get(Constants.doc_registrazione_data);

	String oldRegIdReg = oldProfile.get(Constants.doc_registrazione_id_registro);
	String oldRegNumero = oldProfile.get(Constants.doc_registrazione_numero);

	String oldRegData = oldProfile.get(Constants.doc_registrazione_data);

	if (metadatiRegistrazione.containsKey(Constants.doc_registrazione_id_registro)) {
	    checkIfPropertyChanges("registraDocumento", Constants.doc_registrazione_id_registro, oldStatoArchivistico, oldRegIdReg, newRegIdReg);
	}

	if (metadatiRegistrazione.containsKey(Constants.doc_registrazione_numero)) {
	    checkIfPropertyChanges("registraDocumento", Constants.doc_registrazione_numero, oldStatoArchivistico, oldRegNumero, newRegNumero);
	}

	if (metadatiRegistrazione.containsKey(Constants.doc_registrazione_data)) {
	    checkIfPropertyChanges("registraDocumento", Constants.doc_registrazione_data, oldStatoArchivistico, oldRegData, newRegData);
	}

	if (metadatiRegistrazione.containsKey(Constants.doc_registrazione_anno)) {
	    String newRegAnno = metadatiRegistrazione.get(Constants.doc_registrazione_anno);
	    String oldRegAnno = oldProfile.get(Constants.doc_registrazione_anno);

	    checkIfPropertyChanges("registraDocumento", Constants.doc_registrazione_anno, oldStatoArchivistico, oldRegAnno, newRegAnno);
	}
    }

    private void forbidPubblicazioneChanges(DataRow<String> oldProfile, Map<String, String> metadatiPubblicazione) throws DocerException {

	EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

	String oldPubAnno = oldProfile.get(Constants.doc_pubblicazione_anno);
	String oldPubDataInizio = oldProfile.get(Constants.doc_pubblicazione_data_inizio);
	String oldPubDataFine = oldProfile.get(Constants.doc_pubblicazione_data_fine);
	String oldPubNumero = oldProfile.get(Constants.doc_pubblicazione_numero);
	String oldPubReg = oldProfile.get(Constants.doc_pubblicazione_registro);
	String oldPubblicato = oldProfile.get(Constants.doc_pubblicazione_pubblicato);

	String newPubAnno = metadatiPubblicazione.get(Constants.doc_pubblicazione_anno);
	String newPubDataInizio = metadatiPubblicazione.get(Constants.doc_pubblicazione_data_inizio);
	String newPubDataFine = metadatiPubblicazione.get(Constants.doc_pubblicazione_data_fine);
	String newPubNumero = metadatiPubblicazione.get(Constants.doc_pubblicazione_numero);
	String newPubReg = metadatiPubblicazione.get(Constants.doc_pubblicazione_registro);
	String newPubblicato = metadatiPubblicazione.get(Constants.doc_pubblicazione_pubblicato);

	if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_registro)) {
	    checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_registro, oldStatoArchivistico, oldPubReg, newPubReg);
	}

	if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_numero)) {
	    checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_numero, oldStatoArchivistico, oldPubNumero, newPubNumero);
	}

	if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_data_inizio)) {
	    checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_data_inizio, oldStatoArchivistico, oldPubDataInizio, newPubDataInizio);
	}

	if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_data_fine)) {
	    checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_data_fine, oldStatoArchivistico, oldPubDataFine, newPubDataFine);
	}

	if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_anno)) {
	    checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_anno, oldStatoArchivistico, oldPubAnno, newPubAnno);
	}

	if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_pubblicato)) {
	    checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_pubblicato, oldStatoArchivistico, oldPubblicato, newPubblicato);
	}
    }

    private void forbidProtocolChanges(DataRow<String> oldProfile, Map<String, String> metadatiProtocollazione) throws DocerException {

	EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

	String newProtoAnno = metadatiProtocollazione.get(Constants.doc_protocollo_anno);
	String newProtoReg = metadatiProtocollazione.get(Constants.doc_protocollo_registro);
	String newProtoNumero = metadatiProtocollazione.get(Constants.doc_protocollo_numero);
	String newProtoData = metadatiProtocollazione.get(Constants.doc_protocollo_data);
	String newTipoProtocollazione = metadatiProtocollazione.get(Constants.doc_protocollo_tipo_protocollazione);

	String oldProtoAnno = oldProfile.get(Constants.doc_protocollo_anno);
	String oldProtoReg = oldProfile.get(Constants.doc_protocollo_registro);
	String oldProtoNumero = oldProfile.get(Constants.doc_protocollo_numero);
	String oldProtoData = oldProfile.get(Constants.doc_protocollo_data);
	String oldTipoProtocollazione = oldProfile.get(Constants.doc_protocollo_tipo_protocollazione);

	if (metadatiProtocollazione.containsKey(Constants.doc_protocollo_registro)) {
	    checkIfPropertyChanges("protocollaDocumento", Constants.doc_protocollo_registro, oldStatoArchivistico, oldProtoReg, newProtoReg);
	}

	if (metadatiProtocollazione.containsKey(Constants.doc_protocollo_numero)) {
	    checkIfPropertyChanges("protocollaDocumento", Constants.doc_protocollo_numero, oldStatoArchivistico, oldProtoNumero, newProtoNumero);
	}

	if (metadatiProtocollazione.containsKey(Constants.doc_protocollo_data)) {
	    checkIfPropertyChanges("protocollaDocumento", Constants.doc_protocollo_data, oldStatoArchivistico, oldProtoData, newProtoData);
	}

	if (metadatiProtocollazione.containsKey(Constants.doc_protocollo_anno)) {
	    checkIfPropertyChanges("protocollaDocumento", Constants.doc_protocollo_anno, oldStatoArchivistico, oldProtoAnno, newProtoAnno);
	}

	if (metadatiProtocollazione.containsKey(Constants.doc_protocollo_tipo_protocollazione)) {
	    checkIfPropertyChanges("protocollaDocumento", Constants.doc_protocollo_tipo_protocollazione, oldStatoArchivistico, oldTipoProtocollazione, newTipoProtocollazione);
	}
    }

    private void forbidRegAndProtoCommonFieldsChanges(DataRow<String> oldProfile, Map<String, String> metadati) throws DocerException {

	// campi comuni registrazione e protocollazione
	String newDestinatari = metadati.get(Constants.doc_reg_e_proto_destinatari);
	String newFirmatario = metadati.get(Constants.doc_reg_e_proto_firmatario);
	String newMittenti = metadati.get(Constants.doc_reg_e_proto_mittenti);
	String newTipoFirma = metadati.get(Constants.doc_reg_e_proto_tipo_firma);

	EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

	String oldDestinatari = oldProfile.get(Constants.doc_reg_e_proto_destinatari);
	String oldFirmatario = oldProfile.get(Constants.doc_reg_e_proto_firmatario);
	String oldMittenti = oldProfile.get(Constants.doc_reg_e_proto_mittenti);
	String oldTipoFirma = oldProfile.get(Constants.doc_reg_e_proto_tipo_firma);

	// modifica del 14/12/2012
	// if(metadati.containsKey(Constants.doc_reg_e_proto_destinatari)){
	// checkIfPropertyChanges(Constants.doc_reg_e_proto_destinatari,
	// oldStatoArchivistico,oldDestinatari, newDestinatari);
	// }
	//
	// if(metadati.containsKey(Constants.doc_reg_e_proto_firmatario)){
	// checkIfPropertyChanges(Constants.doc_reg_e_proto_firmatario,
	// oldStatoArchivistico,oldFirmatario, newFirmatario);
	// }
	//
	// if(metadati.containsKey(Constants.doc_reg_e_proto_mittenti)){
	// checkIfPropertyChanges(Constants.doc_reg_e_proto_mittenti,
	// oldStatoArchivistico,oldMittenti, newMittenti);
	// }

	if (metadati.containsKey(Constants.doc_reg_e_proto_tipo_firma)) {
	    checkIfPropertyChanges(Constants.doc_reg_e_proto_tipo_firma, oldStatoArchivistico, oldTipoFirma, newTipoFirma);
	}
    }

    private void checkIfPropertyChanges(String fieldName, EnumStatoArchivistico oldStatoArchivistico, String oldValue, String newValue) throws DocerException {

	String messageStart = "non e' ammessa la modifica del campo " + fieldName;
	String messageEnd = " per un documento con " + Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " (" + oldStatoArchivistico.getCode() + ")";

	if (oldValue == null || oldValue.equals(VOID)) {

	    if (newValue != null && !newValue.equals(VOID))
		throw new DocerException(messageStart + messageEnd);

	    return;
	}

	if (oldValue != null) {

	    if (newValue == null)
		newValue = VOID;

	    if (!oldValue.equals(newValue)) {
		throw new DocerException(messageStart + messageEnd);
	    }
	}
    }

    private void validateRegistrazioneProperties(Map<String, String> metadatiRegistrazione) throws DocerException {

	String idRegistro = metadatiRegistrazione.get(Constants.doc_registrazione_id_registro);
	String numeroRegistro = metadatiRegistrazione.get(Constants.doc_registrazione_numero);
	String dataRegistro = metadatiRegistrazione.get(Constants.doc_registrazione_data);

	String tipoFirma = metadatiRegistrazione.get(Constants.doc_reg_e_proto_tipo_firma);

	// String destinatari =
	// metadatiRegistrazione.get(Constants.doc_reg_e_proto_destinatari);
	String firmatario = metadatiRegistrazione.get(Constants.doc_reg_e_proto_firmatario);
	// String mittenti =
	// metadatiRegistrazione.get(Constants.doc_reg_e_proto_mittenti);

	if (numeroRegistro == null || numeroRegistro.equals(VOID) || idRegistro == null || idRegistro.equals(VOID) || dataRegistro == null || dataRegistro.equals(VOID) || tipoFirma == null
		|| tipoFirma.equals(VOID)) {
	    throw new DocerException(Constants.doc_registrazione_numero + ", " + Constants.doc_registrazione_id_registro + ", " + Constants.doc_registrazione_data + ", "
		    + Constants.doc_reg_e_proto_tipo_firma + " obbligatori per la registrazione");
	}

	if (firmatario == null || firmatario.equals(VOID)) {
	    if (tipoFirma.equals("F")) {
		throw new DocerException(Constants.doc_reg_e_proto_firmatario + " obbligatorio per la registrazione se " + Constants.doc_reg_e_proto_tipo_firma + " ha valore F");
	    }
	}

	// FACOLTATIVI
	// if (mittenti == null || mittenti.equals(VOID)) {
	// if(tipoProtocollazione.equals("E")){
	// throw new DocerException(Constants.doc_reg_e_proto_mittenti
	// + " obbligatorio per la registrazione se "
	// +Constants.doc_protocollo_tipo_protocollazione +" ha valore E");
	// }
	// }
	//
	// if (destinatari== null || destinatari.equals(VOID)) {
	// if(tipoProtocollazione.equals("I") ||
	// tipoProtocollazione.equals("U")){
	// throw new DocerException(Constants.doc_reg_e_proto_destinatari
	// + " obbligatorio per la registrazione se "
	// +Constants.doc_protocollo_tipo_protocollazione +" ha valore I o U");
	// }
	// }

	DateTime dataRegistrazione = new DateTime();
	try {

	    dataRegistrazione = parseDateTime(dataRegistro);
	}
	catch (Exception e) {
	    throw new DocerException(Constants.doc_registrazione_data + " formato errato");
	}

	String annoRegistro = String.valueOf(dataRegistrazione.getYear());

	metadatiRegistrazione.put(Constants.doc_registrazione_anno, annoRegistro);

    }

    private void validateProtocolloProperties(Map<String, String> metadatiProtocollazione) throws DocerException {

	String registroProtocollo = metadatiProtocollazione.get(Constants.doc_protocollo_registro);
	String numeroProtocollo = metadatiProtocollazione.get(Constants.doc_protocollo_numero);
	String annoProtocollo = metadatiProtocollazione.get(Constants.doc_protocollo_anno);
	String dataProtocollo = metadatiProtocollazione.get(Constants.doc_protocollo_data);
	String tipoProtocollazione = metadatiProtocollazione.get(Constants.doc_protocollo_tipo_protocollazione);

	String tipoFirma = metadatiProtocollazione.get(Constants.doc_reg_e_proto_tipo_firma);

	String destinatari = metadatiProtocollazione.get(Constants.doc_reg_e_proto_destinatari);
	String firmatario = metadatiProtocollazione.get(Constants.doc_reg_e_proto_firmatario);
	String mittenti = metadatiProtocollazione.get(Constants.doc_reg_e_proto_mittenti);

	if (MIGRATION_MODE) {
	    if (numeroProtocollo == null || numeroProtocollo.equals(VOID) || registroProtocollo == null || registroProtocollo.equals(VOID) || annoProtocollo == null || annoProtocollo.equals(VOID)) {
		throw new DocerException("migration mode: " + Constants.doc_protocollo_numero + ", " + Constants.doc_protocollo_registro + ", " + Constants.doc_protocollo_anno
			+ " obbligatori per la protocollazione");
	    }
	}
	else {

	    if (numeroProtocollo == null || numeroProtocollo.equals(VOID) || registroProtocollo == null || registroProtocollo.equals(VOID) || dataProtocollo == null || dataProtocollo.equals(VOID)
		    || tipoProtocollazione == null || tipoProtocollazione.equals(VOID) || tipoFirma == null || tipoFirma.equals(VOID)) {
		throw new DocerException(Constants.doc_protocollo_numero + ", " + Constants.doc_protocollo_registro + ", " + Constants.doc_protocollo_data + ", "
			+ Constants.doc_protocollo_tipo_protocollazione + ", " + Constants.doc_reg_e_proto_tipo_firma + " obbligatori per la protocollazione");
	    }

	    if (firmatario == null || firmatario.equals(VOID)) {
		if (tipoFirma.equals("F")) {
		    throw new DocerException(Constants.doc_reg_e_proto_firmatario + " obbligatorio per la protocollazione se " + Constants.doc_reg_e_proto_tipo_firma + " ha valore F");
		}
	    }

	    if (mittenti == null || mittenti.equals(VOID)) {
		if (tipoProtocollazione.equals("E")) {
		    throw new DocerException(Constants.doc_reg_e_proto_mittenti + " obbligatorio per la protocollazione se " + Constants.doc_protocollo_tipo_protocollazione + " ha valore E");
		}
	    }

	    if (destinatari == null || destinatari.equals(VOID)) {
		if (tipoProtocollazione.equals("I") || tipoProtocollazione.equals("U")) {
		    throw new DocerException(Constants.doc_reg_e_proto_destinatari + " obbligatorio per la protocollazione se " + Constants.doc_protocollo_tipo_protocollazione + " ha valore I o U");
		}
	    }

	    DateTime dataProtocollazione = new DateTime();
	    try {

		dataProtocollazione = parseDateTime(dataProtocollo);
	    }
	    catch (Exception e) {
		throw new DocerException(Constants.doc_protocollo_data + " formato errato");
	    }

	    annoProtocollo = String.valueOf(dataProtocollazione.getYear());
	    metadatiProtocollazione.put(Constants.doc_protocollo_anno, annoProtocollo);
	}

    }

    private void validatePubblicazioneProperties(Map<String, String> metadatiPubblicazione) throws DocerException {

	String registroPubblicazione = metadatiPubblicazione.get(Constants.doc_pubblicazione_registro);
	String numeroPubblicazione = metadatiPubblicazione.get(Constants.doc_pubblicazione_numero);
	String dataInizioPub = metadatiPubblicazione.get(Constants.doc_pubblicazione_data_inizio);
	String dataFinePub = metadatiPubblicazione.get(Constants.doc_pubblicazione_data_fine);
	// String pubblicato =
	// metadatiPubblicazione.get(Constants.doc_pubblicazione_pubblicato);

	if (registroPubblicazione == null || registroPubblicazione.equals(VOID) || numeroPubblicazione == null || numeroPubblicazione.equals(VOID) || dataInizioPub == null
		|| dataInizioPub.equals(VOID) || dataFinePub == null || dataFinePub.equals(VOID)) {
	    // || pubblicato == null || pubblicato.equals(VOID)) {
	    throw new DocerException(Constants.doc_pubblicazione_registro + ", " + Constants.doc_pubblicazione_numero + ", " + Constants.doc_pubblicazione_data_inizio + ", "
		    + Constants.doc_pubblicazione_data_fine + ", " + Constants.doc_pubblicazione_pubblicato + " obbligatori per la pubblicazione");
	}

	DateTime dataPub = new DateTime();
	try {

	    dataPub = parseDateTime(dataInizioPub);
	}
	catch (Exception e) {
	    throw new DocerException(Constants.doc_pubblicazione_data_inizio + " formato errato");
	}

	String annoPub = String.valueOf(dataPub.getYear());

	metadatiPubblicazione.put(Constants.doc_pubblicazione_anno, annoPub);

	metadatiPubblicazione.put(Constants.doc_pubblicazione_pubblicato, "true");
    }

    private Map<String, String> checkAndGetMetadatiRegistrazione(DataRow<String> oldProfile, Map<String, String> metadata) throws DocerException {

	String oldCodEnte = oldProfile.get(Constants.doc_cod_ente);
	String oldCodAOO = oldProfile.get(Constants.doc_cod_aoo);
	String oldRegNumero = oldProfile.get(Constants.doc_registrazione_numero);

	EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));
	EnumStatoArchivistico newStatoArchivistico = EnumStatoArchivistico.undefined;

	Map<String, String> metadatiRegistrazione = new HashMap<String, String>();
	metadatiRegistrazione.put(Constants.doc_cod_ente, oldCodEnte);
	metadatiRegistrazione.put(Constants.doc_cod_aoo, oldCodAOO);

	EnumStatiPantarei oldStatoPantarei = getEnumStatoPantarei(oldProfile.get(Constants.doc_stato_pantarei));
	// la registrazione di un allegato dovrebbe essere bloccata prima ma la
	// controllo per sicurezza
	if (oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {
	    throw new DocerException("compatibilita' DOCAREA: non e' permesso invocare il metodo registraDocumento per un documento con STATO_PANTAREI allegato (6) ");
	}

	metadatiRegistrazione.put(Constants.doc_stato_pantarei, oldProfile.get(Constants.doc_stato_pantarei));

	if (metadata.containsKey(Constants.doc_registrazione_data)) {
	    metadatiRegistrazione.put(Constants.doc_registrazione_data, metadata.get(Constants.doc_registrazione_data));
	}

	if (metadata.containsKey(Constants.doc_registrazione_id_registro)) {
	    metadatiRegistrazione.put(Constants.doc_registrazione_id_registro, metadata.get(Constants.doc_registrazione_id_registro));
	}

	if (metadata.containsKey(Constants.doc_registrazione_numero)) {
	    metadatiRegistrazione.put(Constants.doc_registrazione_numero, metadata.get(Constants.doc_registrazione_numero));
	}

	if (metadata.containsKey(Constants.doc_registrazione_oggetto)) {
	    metadatiRegistrazione.put(Constants.doc_registrazione_oggetto, metadata.get(Constants.doc_registrazione_oggetto));
	}

	// if(metadata.containsKey(Constants.doc_protocollo_tipo_protocollazione)){
	// metadatiRegistrazione.put(Constants.doc_protocollo_tipo_protocollazione,
	// metadata.get(Constants.doc_protocollo_tipo_protocollazione));
	// }

	if (metadata.containsKey(Constants.doc_reg_e_proto_destinatari)) {
	    metadatiRegistrazione.put(Constants.doc_reg_e_proto_destinatari, metadata.get(Constants.doc_reg_e_proto_destinatari));
	}

	if (metadata.containsKey(Constants.doc_reg_e_proto_firmatario)) {
	    metadatiRegistrazione.put(Constants.doc_reg_e_proto_firmatario, metadata.get(Constants.doc_reg_e_proto_firmatario));
	}

	if (metadata.containsKey(Constants.doc_reg_e_proto_mittenti)) {
	    metadatiRegistrazione.put(Constants.doc_reg_e_proto_mittenti, metadata.get(Constants.doc_reg_e_proto_mittenti));
	}

	if (metadata.containsKey(Constants.doc_reg_e_proto_tipo_firma)) {
	    metadatiRegistrazione.put(Constants.doc_reg_e_proto_tipo_firma, metadata.get(Constants.doc_reg_e_proto_tipo_firma));
	}

	// nuovi metadati 25-10-2012
	if (metadata.containsKey(Constants.doc_registrazione_annullata_registrazione)) {
	    metadatiRegistrazione.put(Constants.doc_registrazione_annullata_registrazione, metadata.get(Constants.doc_registrazione_annullata_registrazione));
	}

	if (metadata.containsKey(Constants.doc_registrazione_data_annullamento_registrazione)) {
	    metadatiRegistrazione.put(Constants.doc_registrazione_data_annullamento_registrazione, metadata.get(Constants.doc_registrazione_data_annullamento_registrazione));
	}

	if (metadata.containsKey(Constants.doc_registrazione_motivo_annullamento_registrazione)) {
	    metadatiRegistrazione.put(Constants.doc_registrazione_motivo_annullamento_registrazione, metadata.get(Constants.doc_registrazione_motivo_annullamento_registrazione));
	}

	boolean hasClassifica = false;
	boolean hasFascicolo = false;
	boolean hasProtocollo = false;
	boolean hasRegistrazione = false;

	if (oldRegNumero != null && !oldRegNumero.equals(VOID)) {
	    hasRegistrazione = true;
	}

	String oldNumPg = oldProfile.get(Constants.doc_protocollo_numero);
	if (oldNumPg != null && !oldNumPg.equals(VOID)) {
	    hasProtocollo = true;
	}

	String oldClassifica = oldProfile.get(Constants.doc_classifica);
	if (oldClassifica != null && !oldClassifica.equals(VOID)) {
	    hasClassifica = true;
	}
	String oldProgrFascicolo = oldProfile.get(Constants.doc_progr_fascicolo);
	if (oldProgrFascicolo != null && !oldProgrFascicolo.equals(VOID)) {
	    hasFascicolo = true;
	}

	if (hasProtocollo || hasRegistrazione) {
	    forbidRegAndProtoCommonFieldsChanges(oldProfile, metadatiRegistrazione);
	}

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico) || oldStatoArchivistico.equals(EnumStatoArchivistico.generico_definitivo)) {

	    validateRegistrazioneProperties(metadatiRegistrazione);

	    newStatoArchivistico = EnumStatoArchivistico.registrato;

	    if (hasFascicolo) {
		newStatoArchivistico = EnumStatoArchivistico.fascicolato;
	    }
	    else if (hasClassifica) {
		newStatoArchivistico = EnumStatoArchivistico.classificato;
	    }

	    metadatiRegistrazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

	    return metadatiRegistrazione;
	}

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato) || oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato)) {

	    // ammessa solo modifica dell'oggetto
	    newStatoArchivistico = oldStatoArchivistico;

	    // proibisco modifiche di registro
	    forbidRegistrationChanges(oldProfile, metadatiRegistrazione);

	    metadatiRegistrazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

	    return metadatiRegistrazione;
	}

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.classificato) || oldStatoArchivistico.equals(EnumStatoArchivistico.fascicolato)) {

	    newStatoArchivistico = oldStatoArchivistico;

	    if (hasProtocollo) {

		try {
		    forbidRegistrationChanges(oldProfile, metadatiRegistrazione);
		}
		catch (DocerException e) {
		    throw new DocerException(e.getMessage() + " e metadati di protocollazione assegnati");
		}

		metadatiRegistrazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

		return metadatiRegistrazione;
	    }

	    // se non ha protocollo e se ha metadati di registro allora non li
	    // posso cambiare a parte l'oggetto
	    if (hasRegistrazione) {

		try {
		    forbidRegistrationChanges(oldProfile, metadatiRegistrazione);
		}
		catch (DocerException e) {
		    throw new DocerException(e.getMessage() + " e metadati di registrazione assegnati");
		}

		metadatiRegistrazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));
		return metadatiRegistrazione;
	    }

	    // se non ha protocollo e non ha registro allora lo posso modificare
	    // (assegnare)
	    validateRegistrazioneProperties(metadatiRegistrazione);

	    metadatiRegistrazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

	    return metadatiRegistrazione;

	}

	throw new DocerException("non e' ammesso invocare il metodo registraDocumento per un documento con " + Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " ("
		+ oldStatoArchivistico.getCode() + ")");
    }

    private Map<String, String> checkAndGetMetadatiProtocollazione(DataRow<String> oldProfile, Map<String, String> metadata) throws DocerException {

	String oldCodEnte = oldProfile.get(Constants.doc_cod_ente);
	String oldCodAOO = oldProfile.get(Constants.doc_cod_aoo);

	EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));
	EnumStatoArchivistico newStatoArchivistico = EnumStatoArchivistico.undefined;

	String oldProtoNumero = oldProfile.get(Constants.doc_protocollo_numero);

	Map<String, String> metadatiProtocollazione = new HashMap<String, String>();
	metadatiProtocollazione.put(Constants.doc_cod_ente, oldCodEnte);
	metadatiProtocollazione.put(Constants.doc_cod_aoo, oldCodAOO);

	// IMPOSTO LO STATO_PANTAREI A 4 (PROTOCOLLATO)
	EnumStatiPantarei oldStatoPantarei = getEnumStatoPantarei(oldProfile.get(Constants.doc_stato_pantarei));
	// la protocollazione di un allegato dovrebbe essere bloccata prima ma
	// la controllo per sicurezza
	if (oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {
	    throw new DocerException("compatibilita' DOCAREA: non e' permesso invocare il metodo protocollaDocumento per un documento con STATO_PANTAREI allegato (6) ");
	}
	metadatiProtocollazione.put(Constants.doc_stato_pantarei, Integer.toString(EnumStatiPantarei.protocollato.getCode()));

	// bug fix 18-09-2012
	if (metadata.containsKey(Constants.doc_protocollo_anno)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_anno, metadata.get(Constants.doc_protocollo_anno));
	}

	if (metadata.containsKey(Constants.doc_protocollo_data)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_data, metadata.get(Constants.doc_protocollo_data));
	}

	if (metadata.containsKey(Constants.doc_protocollo_numero)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_numero, metadata.get(Constants.doc_protocollo_numero));
	}

	if (metadata.containsKey(Constants.doc_protocollo_registro)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_registro, metadata.get(Constants.doc_protocollo_registro));
	}

	if (metadata.containsKey(Constants.doc_protocollo_oggetto)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_oggetto, metadata.get(Constants.doc_protocollo_oggetto));
	}

	if (metadata.containsKey(Constants.doc_protocollo_tipo_protocollazione)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_tipo_protocollazione, metadata.get(Constants.doc_protocollo_tipo_protocollazione));
	}

	if (metadata.containsKey(Constants.doc_reg_e_proto_destinatari)) {
	    metadatiProtocollazione.put(Constants.doc_reg_e_proto_destinatari, metadata.get(Constants.doc_reg_e_proto_destinatari));
	}

	if (metadata.containsKey(Constants.doc_reg_e_proto_firmatario)) {
	    metadatiProtocollazione.put(Constants.doc_reg_e_proto_firmatario, metadata.get(Constants.doc_reg_e_proto_firmatario));
	}

	if (metadata.containsKey(Constants.doc_reg_e_proto_mittenti)) {
	    metadatiProtocollazione.put(Constants.doc_reg_e_proto_mittenti, metadata.get(Constants.doc_reg_e_proto_mittenti));
	}

	if (metadata.containsKey(Constants.doc_reg_e_proto_tipo_firma)) {
	    metadatiProtocollazione.put(Constants.doc_reg_e_proto_tipo_firma, metadata.get(Constants.doc_reg_e_proto_tipo_firma));
	}

	// nuovi metadati 25-10-2012
	if (metadata.containsKey(Constants.doc_protocollo_num_pg_mittente)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_num_pg_mittente, metadata.get(Constants.doc_protocollo_num_pg_mittente));
	}

	if (metadata.containsKey(Constants.doc_protocollo_data_pg_mittente)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_data_pg_mittente, metadata.get(Constants.doc_protocollo_data_pg_mittente));
	}

	if (metadata.containsKey(Constants.doc_protocollo_cod_ente_mittente)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_cod_ente_mittente, metadata.get(Constants.doc_protocollo_cod_ente_mittente));
	}

	if (metadata.containsKey(Constants.doc_protocollo_cod_aoo_mittente)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_cod_aoo_mittente, metadata.get(Constants.doc_protocollo_cod_aoo_mittente));
	}

	if (metadata.containsKey(Constants.doc_protocollo_classifica_mittente)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_classifica_mittente, metadata.get(Constants.doc_protocollo_classifica_mittente));
	}

	if (metadata.containsKey(Constants.doc_protocollo_fascicolo_mittente)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_fascicolo_mittente, metadata.get(Constants.doc_protocollo_fascicolo_mittente));
	}

	if (metadata.containsKey(Constants.doc_protocollo_annullato_protocollo)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_annullato_protocollo, metadata.get(Constants.doc_protocollo_annullato_protocollo));
	}

	if (metadata.containsKey(Constants.doc_protocollo_data_annullamento_protocollo)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_data_annullamento_protocollo, metadata.get(Constants.doc_protocollo_data_annullamento_protocollo));
	}

	if (metadata.containsKey(Constants.doc_protocollo_motivo_annullamento_protocollo)) {
	    metadatiProtocollazione.put(Constants.doc_protocollo_motivo_annullamento_protocollo, metadata.get(Constants.doc_protocollo_motivo_annullamento_protocollo));
	}

	boolean hasRegistrazione = false;

	boolean hasClassifica = false;
	boolean hasFascicolo = false;
	boolean hasProtocollo = false;

	String oldRegistrazioneNumero = oldProfile.get(Constants.doc_registrazione_numero);
	if (oldRegistrazioneNumero != null && !oldRegistrazioneNumero.equals(VOID)) {
	    hasRegistrazione = true;
	}

	if (oldProtoNumero != null && !oldProtoNumero.equals(VOID)) {
	    hasProtocollo = true;
	}

	String oldClassifica = oldProfile.get(Constants.doc_classifica);
	if (oldClassifica != null && !oldClassifica.equals(VOID)) {
	    hasClassifica = true;
	}
	String oldProgrFascicolo = oldProfile.get(Constants.doc_progr_fascicolo);
	if (oldProgrFascicolo != null && !oldProgrFascicolo.equals(VOID)) {
	    hasFascicolo = true;
	}

	if (hasProtocollo || hasRegistrazione) {
	    forbidRegAndProtoCommonFieldsChanges(oldProfile, metadatiProtocollazione);
	}

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico) || oldStatoArchivistico.equals(EnumStatoArchivistico.generico_definitivo)) {

	    validateProtocolloProperties(metadatiProtocollazione);

	    newStatoArchivistico = EnumStatoArchivistico.protocollato;

	    if (hasFascicolo) {
		newStatoArchivistico = EnumStatoArchivistico.fascicolato;
	    }
	    else if (hasClassifica) {
		newStatoArchivistico = EnumStatoArchivistico.classificato;
	    }

	    metadatiProtocollazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

	    return metadatiProtocollazione;
	}

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato)) {

	    validateProtocolloProperties(metadatiProtocollazione);

	    newStatoArchivistico = EnumStatoArchivistico.protocollato;

	    metadatiProtocollazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

	    return metadatiProtocollazione;
	}

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato)) {

	    // ammessa solo modifica dell'oggetto
	    newStatoArchivistico = oldStatoArchivistico;

	    forbidProtocolChanges(oldProfile, metadatiProtocollazione);

	    metadatiProtocollazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

	    return metadatiProtocollazione;
	}

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.classificato) || oldStatoArchivistico.equals(EnumStatoArchivistico.fascicolato)) {

	    newStatoArchivistico = oldStatoArchivistico;

	    // se ha protocollo posso modificare solo oggetto
	    if (hasProtocollo) {

		try {
		    forbidProtocolChanges(oldProfile, metadatiProtocollazione);
		}
		catch (DocerException e) {
		    throw new DocerException(e.getMessage() + " e metadati di protocollazione assegnati");
		}

		metadatiProtocollazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

		return metadatiProtocollazione;
	    }

	    validateProtocolloProperties(metadatiProtocollazione);

	    metadatiProtocollazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

	    return metadatiProtocollazione;

	}

	throw new DocerException("non e' ammesso invocare il metodo protocollaDocumento per un documento con " + Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " ("
		+ oldStatoArchivistico.getCode() + ")");
    }

    private Map<String, String> checkAndGetMetadatiFascicolazione(DataRow<String> oldProfile, Map<String, String> metadata) throws DocerException {

	boolean oldPubblicato = Boolean.parseBoolean(oldProfile.get(Constants.doc_pubblicazione_pubblicato));

	String oldCodEnte = oldProfile.get(Constants.doc_cod_ente);
	String oldCodAOO = oldProfile.get(Constants.doc_cod_aoo);

	String newCodEnte = metadata.get(Constants.doc_cod_ente);
	String newCodAOO = metadata.get(Constants.doc_cod_aoo);

	EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

	String newClassifica = metadata.get(Constants.doc_classifica);
	String newFascicoloProgressivo = metadata.get(Constants.doc_progr_fascicolo);
	String newFascicoloAnno = metadata.get(Constants.doc_anno_fascicolo);
	String newFascicoliSecondari = metadata.get(Constants.doc_fascicoli_secondari);

	String oldClassifica = oldProfile.get(Constants.doc_classifica);
	String oldFascicoloProgressivo = oldProfile.get(Constants.doc_progr_fascicolo);
	String oldFascicoloAnno = oldProfile.get(Constants.doc_anno_fascicolo);
	String oldFascicoliSecondari = oldProfile.get(Constants.doc_fascicoli_secondari);

	Map<String, String> metadatiFascicolazione = new HashMap<String, String>();
	metadatiFascicolazione.put(Constants.doc_cod_ente, oldCodEnte);
	metadatiFascicolazione.put(Constants.doc_cod_aoo, oldCodAOO);

	// STATO_PANTAREI
	EnumStatiPantarei oldStatoPantarei = getEnumStatoPantarei(oldProfile.get(Constants.doc_stato_pantarei));

	if (newCodEnte != null && !newCodEnte.equalsIgnoreCase(oldCodEnte)) {
	    throw new DocerException("Non e' permesso modificare " + Constants.doc_cod_ente);
	}

	if (newCodAOO != null && !newCodAOO.equalsIgnoreCase(oldCodAOO)) {
	    throw new DocerException("Non e' permesso modificare " + Constants.doc_cod_aoo);
	}

	boolean hadFascicolo = oldFascicoloProgressivo != null && !oldFascicoloProgressivo.equals(VOID);

	boolean progrFascicoloSpecificato = metadata.containsKey(Constants.doc_progr_fascicolo);
	boolean annoFascicoloSpecificato = metadata.containsKey(Constants.doc_anno_fascicolo);
	boolean classificaSpecificata = metadata.containsKey(Constants.doc_classifica);
	boolean fascicoliSecondariSpecificati = metadata.containsKey(Constants.doc_fascicoli_secondari);

	boolean annullamentoFascicolazione = progrFascicoloSpecificato && (newFascicoloProgressivo == null || newFascicoloProgressivo.equals(VOID));

	if (progrFascicoloSpecificato) {

	    if (!annoFascicoloSpecificato) {
		throw new DocerException("fascicolaDocumento: per individuare il Fascicolo principale e' necessario specificare ANNO_FASCIOLO");
	    }

	    if (!annullamentoFascicolazione && (newFascicoloAnno == null || newFascicoloAnno.equals(VOID) || newFascicoloAnno.equals("0"))) {
		throw new DocerException("fascicolaDocumento: specificare un valore di ANNO_FASCICOLO diverso da null o stringa vuota o 0");
	    }

	    metadatiFascicolazione.put(Constants.doc_progr_fascicolo, newFascicoloProgressivo);
	    metadatiFascicolazione.put(Constants.doc_anno_fascicolo, newFascicoloAnno);

	    if (!oldStatoPantarei.equals(EnumStatiPantarei.protocollato)) {
		metadatiFascicolazione.put(Constants.doc_stato_pantarei, String.valueOf(EnumStatiPantarei.fascicolato.getCode()));
	    }

	}

	if (!progrFascicoloSpecificato) {

	    if (annoFascicoloSpecificato) {
		throw new DocerException("fascicolaDocumento: se non si specifica PROGR_FASCICOLO non va specificato il campo ANNO_FASCIOLO");
	    }
	    if (classificaSpecificata) {
		throw new DocerException("fascicolaDocumento: se non si specifica PROGR_FASCICLO non va specificato il campo CLASSIFICA");
	    }
	}

	// se specifico classifica
	if (classificaSpecificata) {

	    if (oldClassifica == null) {
		oldClassifica = VOID;
	    }

	    if (newClassifica == null) {
		newClassifica = VOID;
	    }

	    if (annullamentoFascicolazione && !newClassifica.equalsIgnoreCase(oldClassifica)) {
		throw new DocerException("fascicolaDocumento: annullamento della fascicolazione: non e' permesso modificare CLASSIFICA");
	    }

	    metadatiFascicolazione.put(Constants.doc_classifica, newClassifica);

	}

	if (fascicoliSecondariSpecificati) {

	    if (annullamentoFascicolazione && newFascicoliSecondari != null && !newFascicoliSecondari.equals(VOID)) {
		throw new DocerException("fascicolaDocumento: Annullamento della fascicolazione: il campo " + Constants.doc_fascicoli_secondari
			+ " puo' essere annullato oppure non deve essere specificato");
	    }

	    if (!progrFascicoloSpecificato && !hadFascicolo)
		throw new DocerException("fascicolaDocumento: il campo " + Constants.doc_fascicoli_secondari + " non puo' essere assegnato a documenti che non hanno Fascicolo principale");

	    metadatiFascicolazione.put(Constants.doc_fascicoli_secondari, newFascicoliSecondari);
	}

	// la fascicolazione di un allegato dovrebbe essere bloccata prima ma la
	// controllo per sicurezza
	if (oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {
	    throw new DocerException("compatibilita' DOCAREA: non e' permesso invocare il metodo fascicolaDocumento per un documento in STATO_PANTAREI allegato (6) ");
	}
	metadatiFascicolazione.put(Constants.doc_stato_pantarei, oldProfile.get(Constants.doc_stato_pantarei));

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico)) {

	    metadatiFascicolazione.put(Constants.doc_stato_archivistico, String.valueOf(EnumStatoArchivistico.generico.getCode()));
	    return metadatiFascicolazione;
	}

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico_definitivo)) {

	    metadatiFascicolazione.put(Constants.doc_stato_archivistico, String.valueOf(EnumStatoArchivistico.generico_definitivo.getCode()));
	    return metadatiFascicolazione;
	}

	if (oldPubblicato) {
	    // throw new
	    // DocerException("non e' ammesso invocare il metodo fascicolaDocumento per un documento PUBBLICATO");
	    return metadatiFascicolazione;
	}

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato) || oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato)
		|| oldStatoArchivistico.equals(EnumStatoArchivistico.classificato) || oldStatoArchivistico.equals(EnumStatoArchivistico.fascicolato)) {

	    metadatiFascicolazione.put(Constants.doc_stato_archivistico, String.valueOf(EnumStatoArchivistico.fascicolato.getCode()));

	    if (annullamentoFascicolazione) {
		metadatiFascicolazione.put(Constants.doc_stato_archivistico, String.valueOf(EnumStatoArchivistico.classificato.getCode()));
		metadatiFascicolazione.put(Constants.doc_num_fascicolo, "");
	    }

	    return metadatiFascicolazione;
	}

	throw new DocerException("non e' ammesso invocare il metodo fascicolaDocumento per un documento con " + Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " ("
		+ oldStatoArchivistico.getCode() + ")");
    }

    private Map<String, String> checkAndGetMetadatiPubblicazione(DataRow<String> oldProfile, Map<String, String> metadata) throws DocerException {

	boolean oldPubblicato = Boolean.parseBoolean(oldProfile.get(Constants.doc_pubblicazione_pubblicato));

	String oldCodEnte = oldProfile.get(Constants.doc_cod_ente);
	String oldCodAOO = oldProfile.get(Constants.doc_cod_aoo);

	EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

	// String oldProtoNumero =
	// oldProfile.get(Constants.doc_protocollo_numero);
	Map<String, String> metadatiPubblicazione = new HashMap<String, String>();
	metadatiPubblicazione.put(Constants.doc_cod_ente, oldCodEnte);
	metadatiPubblicazione.put(Constants.doc_cod_aoo, oldCodAOO);

	EnumStatiPantarei oldStatoPantarei = getEnumStatoPantarei(oldProfile.get(Constants.doc_stato_pantarei));
	// la pubblicazione di un allegato dovrebbe essere bloccata prima ma la
	// controllo per sicurezza
	if (oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {
	    throw new DocerException("compatibilita' DOCAREA: non e' permesso invocare il metodo pubblicaDocumento per un documento in STATO_PANTAREI allegato (6)");
	}

	metadatiPubblicazione.put(Constants.doc_stato_pantarei, oldProfile.get(Constants.doc_stato_pantarei));

	if (metadata.containsKey(Constants.doc_pubblicazione_data_fine)) {
	    metadatiPubblicazione.put(Constants.doc_pubblicazione_data_fine, metadata.get(Constants.doc_pubblicazione_data_fine));
	}

	if (metadata.containsKey(Constants.doc_pubblicazione_data_inizio)) {
	    metadatiPubblicazione.put(Constants.doc_pubblicazione_data_inizio, metadata.get(Constants.doc_pubblicazione_data_inizio));
	}

	if (metadata.containsKey(Constants.doc_pubblicazione_registro)) {
	    metadatiPubblicazione.put(Constants.doc_pubblicazione_registro, metadata.get(Constants.doc_pubblicazione_registro));
	}

	if (metadata.containsKey(Constants.doc_pubblicazione_numero)) {
	    metadatiPubblicazione.put(Constants.doc_pubblicazione_numero, metadata.get(Constants.doc_pubblicazione_numero));
	}

	if (metadata.containsKey(Constants.doc_pubblicazione_oggetto)) {
	    metadatiPubblicazione.put(Constants.doc_pubblicazione_oggetto, metadata.get(Constants.doc_pubblicazione_oggetto));
	}

	if (metadata.containsKey(Constants.doc_pubblicazione_pubblicato)) {
	    metadatiPubblicazione.put(Constants.doc_pubblicazione_pubblicato, metadata.get(Constants.doc_pubblicazione_pubblicato));
	}

	// metadatiPubblicazione.put(Constants.doc_stato_archivistico,
	// String.valueOf(newStatoArchivistico.getCode()));
	if (oldPubblicato) {
	    forbidPubblicazioneChanges(oldProfile, metadatiPubblicazione);
	    return metadatiPubblicazione;
	}

	// se non e' pubblicato
	if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato) || oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato)
		|| oldStatoArchivistico.equals(EnumStatoArchivistico.classificato) || oldStatoArchivistico.equals(EnumStatoArchivistico.fascicolato)) {

	    validatePubblicazioneProperties(metadatiPubblicazione);
	    return metadatiPubblicazione;
	}

	throw new DocerException("non e' ammesso invocare il metodo pubblicaDocumento per un documento con " + Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " ("
		+ oldStatoArchivistico.getCode() + ")");
    }

    // private List<String> checkRelatedChainWhenRemove(List<String>
    // relatedChain) throws DocerException {
    //
    // // sia arriva a questo controllo solo se master_stato_pantarei =
    // // generico, da fascicolare, da protocollare
    //
    // List<String> listToUpdate = new ArrayList<String>();
    //
    // if (relatedChain.size() == 0) {
    // return listToUpdate;
    // }
    //
    // // creo i criteri di ricerca
    //
    // Map<String, List<String>> searchCriteria = new HashMap<String,
    // List<String>>();
    // searchCriteria.put(Constants.doc_docnum, relatedChain);
    //
    // // creo le proprieta' di ritorno
    // List<String> returnProperties = new ArrayList<String>();
    // returnProperties.add(Constants.doc_docnum);
    // returnProperties.add(Constants.doc_stato_archivistico);
    // returnProperties.add(Constants.doc_stato_pantarei);
    // returnProperties.add(Constants.doc_docnum_princ);
    //
    // DataTable<String> searchResults = null;
    // // eseguo la ricerca ed ottengo il dt di ritorno
    // try {
    // searchResults = provider.searchDocuments(searchCriteria, null,
    // returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
    // }
    // catch (Exception e) {
    // throw new DocerException(e.getMessage());
    // }
    //
    // if (searchResults == null || searchResults.getRows().size() == 0)
    // throw new DocerException("nessun related e' stato trovato");
    //
    // Collection<DataRow<String>> rows = searchResults.getRows();
    // for (DataRow<String> dr : rows) {
    //
    // String relId = dr.get(Constants.doc_docnum);
    //
    // String relDocnumPrinc = dr.get(Constants.doc_docnum_princ);
    // if (!relDocnumPrinc.equals(VOID)) {
    // listToUpdate.add(relId);
    // }
    //
    // EnumStatoArchivistico relatedStatoArchivistico =
    // getEnumStatoArchivistico(dr.get(Constants.doc_stato_archivistico));
    // EnumStatiPantarei relatedStatoPantarei =
    // getEnumStatoPantarei(dr.get(Constants.doc_stato_pantarei));
    //
    // if (relatedStatoArchivistico.getCode() > 1) {
    // throw new DocerException("il related " + relId +
    // " e' in STATO_ARCHIVISTICO " + relatedStatoArchivistico + " (" +
    // relatedStatoArchivistico.getCode() + ")");
    // }
    //
    // // e' possibile rimuovere o allegare solo documenti in stato
    // // pantarei 1, 2, 3
    // if (relatedStatoPantarei.getCode() > 3)
    // throw new DocerException("compatibilita' DOCAREA: il related " + relId +
    // " in STATO_PANTAREI " + relatedStatoPantarei + " (" +
    // relatedStatoPantarei.getCode() + ")");
    //
    // // LockStatus checkedOutInfo;
    // // try {
    // // checkedOutInfo =
    // // (LockStatus)provider.isCheckedOutDocument(relId);
    // // }
    // // catch (Exception e) {
    // // throw new DocerException(e.getMessage());
    // // }
    // //
    // // if (checkedOutInfo.getLocked())
    // // throw new DocerException("il related " + relId +
    // // " e' in stato di blocco exclusivo");
    // }
    //
    // return listToUpdate;
    //
    // }

    private Map<String, EnumACLRights> getACLCompound(String docId) throws DocerException {

	// recupero le ACL del Master
	try {
	    return provider.getACLDocument(docId);
	}
	catch (Exception e) {
	    throw new DocerException(e.getMessage());
	}

    }

    private Map<String, EnumACLRights> getNewACLForSetACL(Map<String, EnumACLRights> accessrights) throws DocerException {

	Map<String, EnumACLRights> aclNew = new HashMap<String, EnumACLRights>();

	for (String userOrGroup : accessrights.keySet()) {

	    EnumACLRights rights = accessrights.get(userOrGroup);
	    if (rights.equals(EnumACLRights.deny) || rights.equals(EnumACLRights.remove) || rights.equals(EnumACLRights.undefined))
		continue;
	    aclNew.put(userOrGroup, rights);
	}

	return aclNew;
    }

    // modifica del 30-10-2012
    // private void updateRelatedACL(List<String> relatedList,
    // Map<String, EnumACLRights> newACLtoSet) throws DocerException {
    //
    // if (relatedList == null || relatedList.size() < 1)
    // return;
    //
    // // attuali ACL del RELATED
    // Map<String, EnumACLRights> oldACLRelated = null;
    //
    // // nuove ACL del RELATED
    // Map<String, EnumACLRights> newACLRelated = new HashMap<String,
    // EnumACLRights>();
    //
    // for (String idRelated : relatedList) {
    //
    // oldACLRelated = provider.getACLDocument(idRelated);
    //
    // // copio le vecchie ACL
    // for (String personOrGroup : oldACLRelated.keySet()) {
    // EnumACLRights oldRight = oldACLRelated.get(personOrGroup);
    // if (oldACLRelated.equals(EnumACLRights.deny)
    // || oldACLRelated.equals(EnumACLRights.remove)
    // || oldACLRelated.equals(EnumACLRights.undefined))
    // continue;
    //
    // newACLRelated.put(personOrGroup, oldRight);
    // }
    //
    // // aggiungo le nuove ereditate dal master
    // for (String newUserOrGroup : newACLtoSet.keySet()) {
    // newACLRelated.put(newUserOrGroup,
    // newACLtoSet.get(newUserOrGroup));
    // }
    //
    // // aggiorno i diritti del related
    // try {
    //
    // // le ACL le aggiorno solo se sono state specificate
    // if (newACLRelated.size() > 0)
    // provider.setACLDocument(idRelated, newACLRelated);
    // } catch (Exception e) {
    // throw new DocerException(e.getMessage());
    // }
    // }
    // }

    private void updateRelatedACL(List<String> relatedList, Map<String, EnumACLRights> masterACL) throws DocerException {

	if (relatedList == null || relatedList.size() < 1)
	    return;

	for (String idRelated : relatedList) {

	    try {
		// aggiorno i diritti del related
		provider.setACLDocument(idRelated, masterACL);
	    }
	    catch (Exception e) {
		throw new DocerException(e.getMessage());
	    }
	}
    }

    // completa i profili con le descrizioni delle anagrafiche
    private Map<String, String> formatProfile(DataRow<String> profile) {

	Map<String, String> completeProfile = new HashMap<String, String>();

	String typeId = profile.get(Constants.doc_type_id);
	typeId = typeId.toUpperCase();

	String codEnte = profile.get(Constants.doc_cod_ente);
	String codAOO = profile.get(Constants.doc_cod_aoo);
	String classifica = profile.get(Constants.doc_classifica);
	String progrFascicolo = profile.get(Constants.doc_progr_fascicolo);
	String annoFascicolo = profile.get(Constants.doc_anno_fascicolo);

	DocumentType docType = DOCUMENT_TYPES.get(typeId);
	if (docType == null)
	    return completeProfile;

	completeProfile.put(Constants.doc_type_id, typeId);
	completeProfile.put(Constants.doc_type_id_des, docType.getDescription());

	for (String propName : profile) {

	    if (propName.equals(Constants.doc_type_id))
		continue;

	    if (propName.equals(Constants.doc_type_id_des))
		continue;

	    FieldDescriptor fd = FIELDS.get(propName.toUpperCase());
	    if (fd == null) {
		continue;
	    }

	    String propValue = profile.get(propName);

	    if (propValue == null)
		propValue = VOID;

	    // e' definito per il type_id
	    // if (docType.getFieldDescriptor(codEnte, codAOO,
	    // propName.toUpperCase()) == null) {
	    // continue;
	    // }

	    completeProfile.put(propName, propValue);

	    if (propName.equalsIgnoreCase(Constants.doc_cod_ente)) {
		if (profile.get(Constants.ente_des_ente) == null)
		    completeProfile.put(Constants.ente_des_ente, VOID);

		continue;
	    }

	    if (propName.equalsIgnoreCase(Constants.doc_cod_aoo)) {
		if (profile.get(Constants.aoo_des_aoo) == null)
		    completeProfile.put(Constants.aoo_des_aoo, VOID);
		continue;
	    }

	    if (propName.equalsIgnoreCase(Constants.doc_classifica)) {

		if (profile.get(Constants.titolario_des_titolario) == null)
		    completeProfile.put(Constants.titolario_des_titolario, VOID);

		continue;
	    }

	    if (propName.equalsIgnoreCase(Constants.doc_progr_fascicolo)) {
		if (profile.get(Constants.fascicolo_des_fascicolo) == null)
		    completeProfile.put(Constants.fascicolo_des_fascicolo, VOID);
		continue;
	    }

	    if (propName.equalsIgnoreCase(Constants.doc_anno_fascicolo)) {
		if (profile.get(Constants.fascicolo_des_fascicolo) == null)
		    completeProfile.put(Constants.fascicolo_des_fascicolo, VOID);
		continue;
	    }

	    if (propName.equalsIgnoreCase(Constants.doc_type_id)) {
		continue;
	    }

	    if (fd.pointToAnagrafica() && codEnte != null && codAOO != null) {

		try {
		    AnagraficaType at = ANAGRAFICHE_TYPES.get(fd.getAnagraficaTypeId().toUpperCase());

		    String des = profile.get(at.getDescrizionePropName());

		    String codAnagr = profile.get(propName);

		    // se la descrizione non proviene dalla ricerca allora la
		    // ricavo
		    if ((des == null || des.equals(VOID)) && codAnagr != null && !codAnagr.equals(VOID)) {
			Map<String, String> cusId = new HashMap<String, String>();
			cusId.put(Constants.custom_cod_ente, codEnte);
			cusId.put(Constants.custom_cod_aoo, codAOO);
			cusId.put(at.getCodicePropName(), codAnagr);

			Map<String, String> p = getAnagraficaProfile(at.getTypeId(), cusId);
			if (p != null) {
			    des = p.get(at.getDescrizionePropName());
			}

			if (des == null) {
			    des = VOID;
			}

		    }

		    completeProfile.put(at.getDescrizionePropName(), des);

		}
		catch (Exception e) {
		    e.printStackTrace();
		}

		continue;
	    }

	} // fine ciclo

	// recupero descrizione ente solo se non viene gia' dal provider
	if (codEnte != null && !codEnte.equals(VOID)) {

	    String desEnte = profile.get(Constants.ente_des_ente);
	    completeProfile.put(Constants.ente_des_ente, desEnte);

	    if (desEnte == null || desEnte.equals(VOID)) {

		desEnte = VOID;
		String enteKey = codEnte.toUpperCase();
		if (enteAooDescriptionCaching.containsKey(enteKey)) {

		    desEnte = enteAooDescriptionCaching.get(enteKey);
		}
		else {
		    try {
			Map<String, String> idCrit = new HashMap<String, String>();
			idCrit.put(Constants.ente_cod_ente, codEnte);
			Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCrit);
			if (p != null) {
			    desEnte = p.get(Constants.ente_des_ente);
			    enteAooDescriptionCaching.put(enteKey, desEnte);
			}

		    }
		    catch (Exception e) {
			e.printStackTrace(); // NON SOLLEVO ECCEZIONE
		    }
		}

		if (desEnte == null)
		    desEnte = VOID;

		completeProfile.put(Constants.ente_des_ente, desEnte);
	    }
	}

	// recupero descrizione aoo solo se non viene gia' dal provider
	if (codEnte != null && !codEnte.equals(VOID) && codAOO != null && !codAOO.equals(VOID)) {

	    String desAOO = profile.get(Constants.aoo_des_aoo);

	    completeProfile.put(Constants.aoo_des_aoo, desAOO);

	    if (desAOO == null || desAOO.equals(VOID)) {

		desAOO = VOID;
		String aooKey = codEnte.toUpperCase() + SLASH + codAOO.toUpperCase();

		if (enteAooDescriptionCaching.containsKey(aooKey))
		    desAOO = enteAooDescriptionCaching.get(aooKey);
		else {
		    try {

			Map<String, String> idCrit = new HashMap<String, String>();
			idCrit.put(Constants.aoo_cod_ente, codEnte);
			idCrit.put(Constants.aoo_cod_aoo, codAOO);

			Map<String, String> p = getAnagraficaProfile(Constants.aoo_type_id, idCrit);
			if (p != null) {
			    desAOO = p.get(Constants.aoo_des_aoo);
			    enteAooDescriptionCaching.put(aooKey, desAOO);
			}

		    }
		    catch (Exception e) {
			e.printStackTrace(); // NON SOLLEVO ECCEZIONE
		    }
		}

		if (desAOO == null)
		    desAOO = VOID;

		completeProfile.put(Constants.aoo_des_aoo, desAOO);

	    }
	}

	// recupero descrizione titolario solo se non viene gia' dal provider
	if (codEnte != null && !codEnte.equals(VOID) && codAOO != null && !codAOO.equals(VOID) && classifica != null && !classifica.equals(VOID)) {
	    try {

		String desTitolario = profile.get(Constants.titolario_des_titolario);
		completeProfile.put(Constants.titolario_des_titolario, desTitolario);

		if (desTitolario == null || desTitolario.equals(VOID)) {

		    Map<String, String> idCrit = new HashMap<String, String>();
		    idCrit.put(Constants.titolario_cod_ente, codEnte);
		    idCrit.put(Constants.titolario_cod_aoo, codAOO);
		    idCrit.put(Constants.titolario_classifica, classifica);

		    Map<String, String> p = getAnagraficaProfile(Constants.titolario_type_id, idCrit);
		    if (p != null) {

			desTitolario = p.get(Constants.titolario_des_titolario);
		    }

		    if (desTitolario == null)
			desTitolario = VOID;

		    completeProfile.put(Constants.titolario_des_titolario, desTitolario);

		}

	    }
	    catch (Exception e) {
		e.printStackTrace(); // NON SOLLEVO ECCEZIONE
	    }
	}

	// recupero descrizione fascicolo solo se non viene gia' dal provider
	if (codEnte != null && !codEnte.equals(VOID) && codAOO != null && !codAOO.equals(VOID) && classifica != null && !classifica.equals(VOID) && annoFascicolo != null
		&& !annoFascicolo.equals(VOID) && !annoFascicolo.equals("0") && progrFascicolo != null && !progrFascicolo.equals(VOID)) {
	    try {

		String desFascicolo = profile.get(Constants.fascicolo_des_fascicolo);
		completeProfile.put(Constants.fascicolo_des_fascicolo, desFascicolo);

		if (desFascicolo == null || desFascicolo.equals(VOID)) {

		    Map<String, String> idCrit = new HashMap<String, String>();
		    idCrit.put(Constants.fascicolo_cod_ente, codEnte);
		    idCrit.put(Constants.fascicolo_cod_aoo, codAOO);
		    idCrit.put(Constants.fascicolo_classifica, classifica);
		    idCrit.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);
		    idCrit.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);

		    Map<String, String> p = getAnagraficaProfile(Constants.fascicolo_type_id, idCrit);

		    if (p != null) {
			desFascicolo = p.get(Constants.fascicolo_des_fascicolo);
		    }

		    if (desFascicolo == null)
			desFascicolo = VOID;

		    completeProfile.put(Constants.fascicolo_des_fascicolo, desFascicolo);
		}

	    }
	    catch (Exception e) {
		e.printStackTrace(); // NON SOLLEVO ECCEZIONE
	    }
	}

	return completeProfile;
    }

    private void clearCache() {
	enteAooDescriptionCaching.clear();
    }

    private void checkAnagraficheCustom(String codEnte, String codAOO, Map<String, String> metadata) throws DocerException {

	for (String propName : metadata.keySet()) {

	    String propValue = metadata.get(propName);

	    if (propValue == null || propValue.equals(""))
		continue;

	    FieldDescriptor fd = FIELDS.get(propName.toUpperCase());

	    if (fd.pointToAnagrafica()) {

		if (codEnte == null || codEnte.equals(VOID)) {
		    throw new DocerException("COD_ENTE richiesto per individuare Anagrafica custom " + fd.getAnagraficaTypeId() + " puntata dalla proprieta' " + propName);
		}

		if (codAOO == null || codAOO.equals(VOID)) {
		    throw new DocerException("COD_AOO richiesto per individuare Anagrafica custom " + fd.getAnagraficaTypeId() + " puntata dalla proprieta' " + propName);
		}

		AnagraficaType at = ANAGRAFICHE_TYPES.get(fd.getAnagraficaTypeId().toUpperCase());

		Map<String, String> cusId = new HashMap<String, String>();
		cusId.put(Constants.custom_cod_ente, codEnte);
		cusId.put(Constants.custom_cod_aoo, codAOO);
		cusId.put(at.getCodicePropName(), propValue);

		Map<String, String> p = getAnagraficaProfile(at.getTypeId(), cusId);

		if (p == null)
		    throw new DocerException("Anagrafica custom " + metadata.get(propName) + " di tipo " + fd.getAnagraficaTypeId() + " non trovata");

		continue;
	    }
	}

    }

    private void checkAnagraficheMultivalue(String codEnte, String codAOO, DocumentType docType, Map<String, String> metadata) throws DocerException {

	Map<String, List<String>> searchCriteria = null;

	List<String> criteria = null;

	String[] mv = null;

	List<String> found = null;

	String pointedPropName = VOID;
	String anagraficaTypeId = VOID;

	FieldDescriptor fd = null;
	List<String> returnProperties = null;
	// controllo gli altri MV
	for (String propName : metadata.keySet()) {

	    String propValue = metadata.get(propName);

	    if (propValue == null || propValue.equals(VOID))
		continue; // non devo controllare nulla

	    // i fascicoli secondari sono ammessi solo da fascicolaDocumento
	    if (propName.equalsIgnoreCase(Constants.doc_fascicoli_secondari))
		continue;

	    fd = docType.getFieldDescriptor(codEnte, codAOO, propName);

	    if (fd == null)
		throw new DocerException("il campo " + propName + " non appartiene al tipo " + docType.getTypeId());

	    // se non e' una proprieta' multivalue...non controllo
	    if (!fd.isMultivalue())
		continue;

	    // se il multivalue non punta ad una anagrafica...non controllo
	    // (fino a
	    // Docer 1.2 i mv puntano sempre ad anagrafiche)
	    if (!fd.pointToAnagrafica())
		continue;

	    // tipo di anagrafica puntata
	    anagraficaTypeId = fd.getAnagraficaTypeId();

	    AnagraficaType at = ANAGRAFICHE_TYPES.get(anagraficaTypeId);

	    if (at == null) { // non e' una anagrafica
		continue;
	    }

	    // proprieta' con ruolo di codice che e' puntata dal mv
	    pointedPropName = at.getCodicePropName();

	    if (pointedPropName == null)
		continue;

	    if (searchCriteria == null) {
		searchCriteria = new HashMap<String, List<String>>();
		criteria = new ArrayList<String>();
	    }
	    else {
		searchCriteria.clear();
		criteria.clear();
	    }

	    // propValue = propValue.toUpperCase();

	    mv = propValue.split(" *; *");

	    for (String singleValue : mv) {
		String idAnagr = singleValue.trim();
		if (idAnagr.equals(VOID) || criteria.contains(idAnagr))
		    continue;
		criteria.add(idAnagr);
	    }

	    searchCriteria.put(pointedPropName.toUpperCase(), criteria);

	    if (criteria.size() > 0) {

		if (codEnte == null || codEnte.equals(VOID))
		    throw new DocerException("COD_ENTE obbligatorio se si specifica il campo multivalue: " + propName);
		if (codAOO == null || codAOO.equals(VOID))
		    throw new DocerException("COD_AOO obbligatorio se si specifica il campo multivalue: " + propName);

		List<String> enteCriteria = Arrays.asList(new String[] { codEnte });
		List<String> aooCriteria = Arrays.asList(new String[] { codAOO });

		searchCriteria.put(Constants.doc_cod_ente, enteCriteria);
		searchCriteria.put(Constants.doc_cod_aoo, aooCriteria);

		if (returnProperties == null) {
		    returnProperties = new ArrayList<String>();
		}
		else {
		    returnProperties.clear();
		}

		returnProperties.add(pointedPropName);

		List<Map<String, String>> r = provider.searchAnagrafiche(anagraficaTypeId, searchCriteria, returnProperties);
		if (r == null || r.size() < 1)
		    throw new DocerException("Anagrafiche " + Arrays.toString(criteria.toArray()) + " specificate in " + propName + " (mv di tipo " + anagraficaTypeId + " verso " + pointedPropName
			    + ") non trovate");

		if (found == null) {
		    found = new ArrayList<String>();
		}
		else {
		    found.clear();
		}

		// popolo la lista dei trovati
		for (Map<String, String> searchItem : r) {

		    for (String key : searchItem.keySet()) {
			if (key.equalsIgnoreCase(pointedPropName)) {
			    found.add(searchItem.get(key));
			    break;
			}
		    }
		}

		if (criteria.size() > found.size()) { // ho trovato meno
						      // risultati
		    for (String f : found) {
			criteria.remove(f);
		    }
		    throw new DocerException("Anagrafiche " + Arrays.toString(criteria.toArray()) + " specificate in " + propName + " (mv di tipo " + anagraficaTypeId + " verso " + pointedPropName
			    + ") non trovate");
		}

		if (criteria.size() < found.size()) { // ho trovato piu'
						      // risultati
		    for (String c : criteria) {
			found.remove(c);
		    }
		    throw new DocerException("Anagrafiche non univoche: " + Arrays.toString(found.toArray()) + "; specificate in " + propName + " (mv di tipo " + anagraficaTypeId + " verso "
			    + pointedPropName + ")");
		}

	    }

	}

    }

    private void checkFascicoliSecondari(Map<String, String> metadata) throws DocerException {

	boolean fascicoliSecondariSpecificati = metadata.containsKey(Constants.doc_fascicoli_secondari);

	if (!fascicoliSecondariSpecificati)
	    return;

	// tratto i fascicoli secondari
	String fascicoliSecondari = metadata.get(Constants.doc_fascicoli_secondari);

	if (fascicoliSecondari == null || fascicoliSecondari.equals(VOID)) {
	    return; // annullamento
	}
	// tutti i multivalue sono verso anagrafiche e sotto le aoo
	String codEnte = metadata.get(Constants.ente_cod_ente);
	String codAOO = metadata.get(Constants.aoo_cod_aoo);

	if (codEnte == null || codEnte.equals(VOID))
	    throw new DocerException("Ricerca Fascicoli secondari: COD_ENTE obbligatorio");

	if (codAOO == null || codAOO.equals(VOID))
	    throw new DocerException("Ricerca Fascicoli secondari: COD_AOO obbligatorio");

	Map<String, List<String>> searchCriteria = null;

	List<String> enteCriteria = null;
	List<String> aooCriteria = null;
	List<String> classificaCriteria = null;
	List<String> annoFascicoloCriteria = null;
	List<String> progrFascicoloCriteria = null;

	String[] fascicoli = null;

	fascicoli = fascicoliSecondari.split(";");

	for (String fascicolo : fascicoli) {

	    fascicolo = fascicolo.trim();

	    if (fascicolo.equals(VOID))
		continue;

	    if (searchCriteria == null) {
		searchCriteria = new HashMap<String, List<String>>();
		enteCriteria = new ArrayList<String>();
		aooCriteria = new ArrayList<String>();

		enteCriteria.add(codEnte);
		aooCriteria.add(codAOO);

		classificaCriteria = new ArrayList<String>();
		annoFascicoloCriteria = new ArrayList<String>();
		progrFascicoloCriteria = new ArrayList<String>();
	    }
	    else {
		searchCriteria.clear();
		classificaCriteria.clear();
		annoFascicoloCriteria.clear();
		progrFascicoloCriteria.clear();
	    }

	    Matcher m = patternFascicoliSec.matcher(fascicolo);

	    if (m.matches()) {

		classificaCriteria.add(m.group(classificaPosition));
		annoFascicoloCriteria.add(m.group(annoFascicoloPosition));
		progrFascicoloCriteria.add(m.group(progrFascicoloPosition));

		searchCriteria.put(Constants.fascicolo_cod_ente, enteCriteria);
		searchCriteria.put(Constants.fascicolo_cod_aoo, aooCriteria);
		searchCriteria.put(Constants.fascicolo_classifica, classificaCriteria);
		searchCriteria.put(Constants.fascicolo_anno_fascicolo, annoFascicoloCriteria);
		searchCriteria.put(Constants.fascicolo_progr_fascicolo, progrFascicoloCriteria);

		List<Map<String, String>> r = provider.searchAnagrafiche(Constants.fascicolo_type_id, searchCriteria, HITLISTS.get("HITLIST_FASCICOLO"));
		if (r == null || r.size() < 1)
		    throw new DocerException("FASCICOLI_SEC: fascicolo " + fascicolo + " non trovato");
	    }

	}

    }

    private Map<String, String> getAnagraficaProfile(String typeid, Map<String, String> id) throws DocerException {

	typeid = String.valueOf(typeid).toUpperCase();

	AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(typeid);
	if (anagraficaType == null)
	    throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + typeid);

	String racc_uid = id.get(RACC_UID);

	List<Map<String, String>> result = null;

	String codEnte = null;
	String codAoo = null;

	if (typeid.equalsIgnoreCase(Constants.fascicolo_type_id) && StringUtils.isNotEmpty(racc_uid)) {
	    Map<String, List<String>> idCriteria = new HashMap<String, List<String>>();
	    idCriteria.put(RACC_UID, Arrays.asList(racc_uid));
	    result = provider.searchAnagrafiche(typeid, idCriteria, Arrays.asList("COD_ENTE", "COD_AOO"));
	    if (result.size() == 0) {
		return null;
	    }
	    if (result.size() != 1) {
		throw new DocerException("Trovati " + result.size() + " risultati");
	    }

	    codEnte = result.get(0).get("COD_ENTE");
	    codAoo = result.get(0).get("COD_AOO");

	    idCriteria.clear();
	    idCriteria.put(RACC_UID, Arrays.asList(racc_uid));

	    result = provider.searchAnagrafiche(typeid, idCriteria, anagraficaType.getFieldsNames(codEnte, codAoo));

	}
	else {
	    // codEnte e' sempre obbligatorio
	    codEnte = id.get(Constants.ente_cod_ente);

	    // se non e' un Ente allora codAoo e' sempre obbligatorio
	    codAoo = id.get(Constants.aoo_cod_aoo);

	    if (StringUtils.isEmpty(codEnte)) {
		throw new DocerException("COD_ENTE obbligatorio");
	    }

	    if (!typeid.equals(Constants.ente_type_id) && StringUtils.isEmpty(codAoo)) {
		throw new DocerException("COD_AOO obbligatorio");
	    }

	    FieldDescriptor fd = null;

	    Map<String, List<String>> idCriteria = new HashMap<String, List<String>>();

	    // assegno criteria di ricerca
	    for (String fieldName : id.keySet()) {

		fd = anagraficaType.getFieldDescriptor(codEnte, codAoo, fieldName);

		if (fd == null) {
		    if (typeid.equals(Constants.ente_type_id)) {
			throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica " + typeid + " per COD_ENTE " + codEnte);
		    }
		    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica " + typeid + " per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
		}

		boolean isIdCriteria = false;

		if (typeid.equalsIgnoreCase("ENTE")) {
		    if (fd.getPropName().equalsIgnoreCase(Constants.ente_cod_ente)) {
			isIdCriteria = true;
		    }
		}
		else if (typeid.equalsIgnoreCase("AOO")) {
		    if (fd.getPropName().equalsIgnoreCase(Constants.aoo_cod_ente)) {
			isIdCriteria = true;
		    }
		    else if (fd.getPropName().equalsIgnoreCase(Constants.aoo_cod_aoo)) {
			isIdCriteria = true;
		    }
		}
		else if (typeid.equalsIgnoreCase("TITOLARIO")) {
		    if (fd.getPropName().equalsIgnoreCase(Constants.titolario_cod_ente)) {
			isIdCriteria = true;
		    }
		    else if (fd.getPropName().equalsIgnoreCase(Constants.titolario_cod_aoo)) {
			isIdCriteria = true;
		    }
		    else if (fd.getPropName().equalsIgnoreCase(Constants.titolario_classifica)) {
			isIdCriteria = true;
		    }
		    else if (fd.getPropName().equalsIgnoreCase(Constants.titolario_cod_titolario)) {
			isIdCriteria = true;
		    }
		}
		else if (typeid.equalsIgnoreCase("FASCICOLO")) {
		    if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_cod_ente)) {
			isIdCriteria = true;
		    }
		    else if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_cod_aoo)) {
			isIdCriteria = true;
		    }
		    else if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_classifica)) {
			isIdCriteria = true;
		    }
		    else if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_progr_fascicolo)) {
			isIdCriteria = true;
		    }
		    else if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_num_fascicolo)) {
			isIdCriteria = true;
		    }
		    else if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_anno_fascicolo)) {
			isIdCriteria = true;
		    }
		}
		else {
		    if (fd.getPropName().equalsIgnoreCase(Constants.custom_cod_ente)) {
			isIdCriteria = true;
		    }
		    else if (fd.getPropName().equalsIgnoreCase(Constants.custom_cod_aoo)) {
			isIdCriteria = true;
		    }
		    else if (fd.getPropName().equalsIgnoreCase(anagraficaType.getCodicePropName())) {
			isIdCriteria = true;
		    }
		}

		if (isIdCriteria) {
		    List<String> crit = new ArrayList<String>();
		    crit.add(id.get(fieldName));
		    idCriteria.put(fd.getPropName(), crit);
		}

		result = provider.searchAnagrafiche(typeid, idCriteria, anagraficaType.getFieldsNames(codEnte, codAoo));
	    }

	}

	if (result.size() == 0)
	    return null;

	if (result.size() != 1)
	    throw new DocerException("Trovati " + result.size() + " risultati");

	Map<String, String> resultProfile = result.get(0);

	Map<String, String> profile = new HashMap<String, String>();

	// assegno le extrainfo
	for (String key : resultProfile.keySet()) {

	    String value = resultProfile.get(key);

	    key = key.toUpperCase();

	    profile.put(key, value);
	}

	return profile;
    }

    // private Map<String, String> getFascicoloProfileByUid(String racc_uid)
    // throws DocerException {
    //
    // // uid e' sempre obbligatorio
    // if (racc_uid == null || racc_uid.equals(VOID)) {
    // throw new
    // DocerException("getFascicoloProfileByUid: RACC_UID obbligatorio");
    // }
    //
    // AnagraficaType anagraficaType =
    // ANAGRAFICHE_TYPES.get(it.kdm.docer.sdk.Constants.fascicolo_type_id);
    // if (anagraficaType == null)
    // throw new
    // DocerException("Anagrafica non definita in configurazione Business Logic: "
    // + it.kdm.docer.sdk.Constants.fascicolo_type_id);
    //
    // Map<String, List<String>> idCriteria = new HashMap<String,
    // List<String>>();
    //
    // List<String> crit = new ArrayList<String>();
    // crit.add(racc_uid);
    // idCriteria.put("RACC_UID", crit);
    //
    // List<Map<String, String>> result =
    // provider.searchAnagrafiche(it.kdm.docer.sdk.Constants.fascicolo_type_id,
    // idCriteria, ALL_FIELDS);
    //
    // if (result.size() == 0)
    // return null;
    //
    // if (result.size() != 1)
    // throw new DocerException("Trovati " + result.size() + " risultati");
    //
    // Map<String, String> resultProfile = result.get(0);
    //
    // Map<String, String> profile = new HashMap<String, String>();
    //
    // String codEnte = resultProfile.get("COD_ENTE");
    // String codAoo = resultProfile.get("COD_AOO");
    //
    // FieldDescriptor fd = null;
    // // assegno le extrainfo
    // for (String fieldName : resultProfile.keySet()) {
    //
    // if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
    // continue;
    // }
    //
    // fd = anagraficaType.getFieldDescriptor(codEnte, codAoo, fieldName);
    //
    // if (fd == null) {
    // if (!anagraficaType.getFieldsNames(codEnte, codAoo).contains(fieldName))
    // {
    // continue;
    // }
    // }
    //
    // profile.put(fieldName, resultProfile.get(fieldName));
    // }
    //
    // return profile;
    // }

    // private Map<String, String> getDocumentProfileByUid(String docId) throws
    // DocerException {
    //
    // Map<String, List<String>> searchCriteria = new HashMap<String,
    // List<String>>();
    //
    // // impostazione dell'unico criterio di ricerca
    // List<String> criteria = new ArrayList<String>();
    // criteria.add(docId);
    // searchCriteria.put(Constants.doc_docnum, criteria);
    //
    // // richiedo tutte le proprieta'
    // List<String> returnProperties = new ArrayList<String>(FIELDS.keySet());
    // // test
    //
    // // RICERCA PRIMARIA
    // DataTable<String> searchResults =
    // provider.searchDocuments(searchCriteria, null, returnProperties,
    // PRIMARYSEARCH_MAX_ROWS, null);
    //
    // // deve esserci un solo risultato perche' devo modificare un solo
    // // documento
    // if (searchResults.getRows().size() == 0)
    // throw new DocerException("documento non trovato");
    // if (searchResults.getRows().size() != 1)
    // throw new DocerException("docId non univoco");
    //
    // // HO TROVATO IL DOCUMENTO -> un solo risultato
    // DataRow<String> profileData = searchResults.getRow(0);
    //
    // String codEnte = profileData.get(Constants.doc_cod_ente);
    // String codAOO = profileData.get(Constants.doc_cod_aoo);
    //
    // if (codEnte == null || codEnte.equals(VOID))
    // throw new DocerException("ERRORE: documento " + docId +
    // " senza COD_ENTE assegnato");
    //
    // // if (codAOO == null || codAOO.equals(VOID))
    // // throw new DocerException("ERRORE: documento " + docId
    // // + " senza COD_AOO assegnato");
    // if (codAOO == null) {
    // codAOO = VOID;
    // }
    //
    // String documentTypeId = profileData.get(Constants.doc_type_id);
    //
    // if (documentTypeId == null || documentTypeId.equals(VOID))
    // throw new DocerException("ERRORE: documento " + docId +
    // " senza TYPE_ID assegnato");
    //
    // documentTypeId = documentTypeId.toUpperCase();
    //
    // DocumentType docType = DOCUMENT_TYPES.get(documentTypeId);
    //
    // if (docType == null) {
    //
    // throw new DocerException("type_id assegnato al documento " + docId +
    // " sconosciuto: " + documentTypeId);
    // }
    //
    // // Map<String, String> profile = new HashMap<String, String>();
    // //
    // // FieldDescriptor fd = null;
    // // // ciclo le proprieta' e restituisco solo quelle definite nel
    // // docType
    // // for(String fieldName : returnProperties){
    // // fd = docType.getFieldDescriptor(codEnte, codAOO, fieldName);
    // // if(fd==null)
    // // continue; // prop non presente nel docType
    // //
    // // profile.put(fieldName, profileData.get(fieldName));
    // // }
    //
    // // inserisco le descrizioni delle anagrafiche
    // return formatProfile(profileData);
    // }

    private IFolderInfo getFolderProfile(Map<String, String> id) throws DocerException {

	Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	// assegno criteria di ricerca
	for (String fieldName : id.keySet()) {

	    if (fieldName == null)
		continue;
	    List<String> criteria = new ArrayList<String>();
	    criteria.add(id.get(fieldName));

	    searchCriteria.put(fieldName.toUpperCase(), criteria);
	}

	List<String> returnProperties = new ArrayList<String>();
	returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
	returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_ente);
	returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_aoo);
	returnProperties.add(it.kdm.docer.sdk.Constants.folder_des_folder);
	returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_name);
	returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
	returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);

	DataTable<String> dtResult = null;

	try {
	    dtResult = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
	}
	catch (Exception e) {
	    throw new DocerException(e.getMessage());
	}

	if (dtResult.getRows().size() == 0)
	    return null;

	if (dtResult.getRows().size() > 1)
	    throw new DocerException("Trovate " + dtResult.getRows().size() + " Folder");

	IFolderInfo folderProfile = new FolderInfo();
	DataRow<String> rowProfile = dtResult.getRow(0);

	for (String columnName : dtResult.getColumnNames()) {

	    if (columnName.equals(it.kdm.docer.sdk.Constants.folder_folder_id)) {
		folderProfile.setFolderId(rowProfile.get(columnName));
		continue;
	    }
	    if (columnName.equals(it.kdm.docer.sdk.Constants.folder_cod_ente)) {
		folderProfile.setCodiceEnte(rowProfile.get(columnName));
		continue;
	    }
	    if (columnName.equals(it.kdm.docer.sdk.Constants.folder_cod_aoo)) {
		folderProfile.setCodiceAOO(rowProfile.get(columnName));
		continue;
	    }
	    if (columnName.equals(it.kdm.docer.sdk.Constants.folder_parent_folder_id)) {
		folderProfile.setParentFolderId(rowProfile.get(columnName));
		continue;
	    }
	    if (columnName.equals(it.kdm.docer.sdk.Constants.folder_des_folder)) {
		folderProfile.setDescrizione(rowProfile.get(columnName));
		continue;
	    }
	    if (columnName.equals(it.kdm.docer.sdk.Constants.folder_folder_name)) {
		folderProfile.setFolderName(rowProfile.get(columnName));
		continue;
	    }
	    if (columnName.equals(it.kdm.docer.sdk.Constants.folder_owner)) {
		folderProfile.setFolderOwner(rowProfile.get(columnName));
		continue;
	    }

	}

	return folderProfile;

    }

    // private List<ISearchItem> listOfMapToList(List<Map<String, String>> list)
    // {
    //
    // List<ISearchItem> searchItems = new ArrayList<ISearchItem>();
    //
    // for (Map<String, String> singleRes : list) {
    // ISearchItem si = new SearchItem();
    // List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
    // for (String key : singleRes.keySet()) {
    // kvpList.add(new KeyValuePair(key, singleRes.get(key)));
    // }
    // si.setMetadata(kvpList.toArray(new KeyValuePair[0]));
    // searchItems.add(si);
    // }
    //
    // return searchItems;
    // }

    // private List<IKeyValuePair> mapToList(Map<String, String> map) {
    //
    // List<IKeyValuePair> list = new ArrayList<IKeyValuePair>();
    //
    // for (String key : map.keySet()) {
    //
    // list.add(new KeyValuePair(key, map.get(key)));
    // }
    //
    // return list;
    // }

    private Map<String, String> getTitolario(String codEnte, String codAOO, String classifica) throws DocerException {

	if (codEnte == null || codEnte.equals(VOID))
	    throw new DocerException("Ricerca voce di Titolario: COD_ENTE obbligatorio");
	if (codAOO == null || codAOO.equals(VOID))
	    throw new DocerException("Ricerca voce di Titolario: COD_AOO obbligatorio");

	Map<String, String> id = new HashMap<String, String>();
	id.put(Constants.titolario_cod_ente, codEnte);
	id.put(Constants.titolario_cod_aoo, codAOO);
	id.put(Constants.titolario_classifica, classifica);

	Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, id);

	if (ptit == null) {
	    throw new DocerException("Titolario con CLASSIFICA " + classifica + " non trovato");
	}

	// if (ptit.get(Constants.titolario_enabled).equalsIgnoreCase(
	// EnumBoolean.FALSE.toString()))
	// throw new DocerException("Titolario disabilitato: CLASSIFICA "
	// + classifica);

	String codTitolario = ptit.get(Constants.titolario_cod_titolario);
	if (codTitolario == null || codTitolario.equals(VOID)) {
	    throw new DocerException("Errore compatibilita' DOCAREA/DocER: la voce di Titolario con CLASSIFICA " + classifica + " non ha COD_TITOLARIO assegnato");
	}

	return ptit;

    }

    private void checkFascicoloPrincipale(Map<String, String> metadata) throws DocerException {

	boolean progrFascicoloSpecificato = metadata.containsKey(Constants.doc_progr_fascicolo);

	if (!progrFascicoloSpecificato) {
	    return;
	}

	String progressivo_fascicolo = metadata.get(Constants.doc_progr_fascicolo);

	if (progrFascicoloSpecificato && (progressivo_fascicolo == null || progressivo_fascicolo.equals(VOID))) {
	    return; // annullamento fascicolazione
	}

	String anno_fascicolo = metadata.get(Constants.doc_anno_fascicolo);

	String codice_ente = metadata.get(Constants.doc_cod_ente);
	String codice_aoo = metadata.get(Constants.doc_cod_aoo);
	String classifica = metadata.get(Constants.doc_classifica);

	if (codice_ente == null || codice_ente.equals(VOID))
	    throw new DocerException("Ricerca Fascicolo: COD_ENTE obbligatorio");
	if (codice_aoo == null || codice_aoo.equals(VOID))
	    throw new DocerException("Ricerca Fascicolo: COD_AOO obbligatorio");
	if (classifica == null || classifica.equals(VOID))
	    throw new DocerException("Ricerca Fascicolo: CLASSIFICA obbligatoria");

	Map<String, String> id = new HashMap<String, String>();
	id.put(Constants.fascicolo_cod_ente, codice_ente);
	id.put(Constants.fascicolo_cod_aoo, codice_aoo);
	id.put(Constants.fascicolo_classifica, classifica);
	id.put(Constants.fascicolo_anno_fascicolo, anno_fascicolo);
	id.put(Constants.fascicolo_progr_fascicolo, progressivo_fascicolo);

	Map<String, String> fascicolo = getAnagraficaProfile(Constants.fascicolo_type_id, id);

	if (fascicolo == null) {
	    throw new DocerException("Fascicolo con COD_ENTE: " + codice_ente + ", COD_AOO: " + codice_aoo + ", CLASSIFICA: " + classifica + ", ANNO_FASCICOLO: " + anno_fascicolo
		    + ", PROGR_FASCICOLO: " + progressivo_fascicolo + " non trovato");
	}

	// if (fascicolo.get(Constants.fascicolo_enabled).equalsIgnoreCase(
	// EnumBoolean.FALSE.toString()))
	// throw new DocerException("Fascicolo disabilitato; COD_ENTE: "
	// + codice_ente + ", COD_AOO: " + codice_aoo
	// + ", CLASSIFICA: " + classifica + ", ANNO_FASCICOLO: "
	// + anno_fascicolo + ", PROGR_FASCICOLO: "
	// + progressivo_fascicolo);

	String codTitolario = fascicolo.get(Constants.titolario_cod_titolario);
	String numFascicolo = fascicolo.get(Constants.fascicolo_num_fascicolo);

	if (numFascicolo == null || numFascicolo.equals(VOID)) {
	    throw new DocerException("Errore compatibilita' DOCAREA/DocER: il fascicolo con PROGR_FASCICOLO " + progressivo_fascicolo + " e CLASSIFICA " + classifica
		    + " non ha NUM_FASCICOLO assegnato");
	}
	if (codTitolario == null || codTitolario.equals(VOID)) {
	    throw new DocerException("Errore compatibilita' DOCAREA/DocER: il fascicolo con PROGR_FASCICOLO " + progressivo_fascicolo + " e CLASSIFICA " + classifica
		    + " non ha COD_TITOLARIO assegnato");
	}

	metadata.put("NUM_FASCICOLO", numFascicolo);
	metadata.put("COD_TITOLARIO", codTitolario);
    }

    private DateTime parseDateTime(String datetime) {
	DateTimeFormatter fm = ISODateTimeFormat.dateTime();
	return fm.parseDateTime(datetime);
    }

    // controllo della catena dei related quando il documento diventa un RECORD
    private Map<String, Map<String, String>> checkRecordRelatedChain(EnumStatoArchivistico newMasterStatoArchivistico, EnumStatiPantarei newMasterStatoPantarei, String codEnte, String codAOO,
	    String docId, String typistId, List<String> relatedChain) throws DocerException {

	Map<String, Map<String, String>> relProfiles = new HashMap<String, Map<String, String>>();

	if (relatedChain == null || relatedChain.size() < 1)
	    return relProfiles;

	// criteri di ricerca
	Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	List<String> criteria = new ArrayList<String>();

	// metadati che voglio ricercare
	List<String> returnProperties = new ArrayList<String>();
	returnProperties.add(Constants.doc_cod_ente);
	returnProperties.add(Constants.doc_cod_aoo);
	returnProperties.add(Constants.doc_docnum);
	returnProperties.add(Constants.doc_stato_archivistico);
	returnProperties.add(Constants.doc_tipo_componente);
	returnProperties.add(Constants.doc_stato_pantarei);
	returnProperties.add(Constants.doc_docnum_princ);

	DataTable<String> dtResults = null;

	criteria.clear();

	for (String id : relatedChain) {

	    if (id.equals(docId))
		continue;

	    if (!criteria.contains(id))
		criteria.add(id);
	}

	if (criteria.size() == 0) {
	    return relProfiles;
	}

	// criteri di ricerca
	searchCriteria.clear();
	searchCriteria.put(Constants.doc_docnum, criteria);

	// cerco tutti gli stati pantarei dei documenti related attualmente
	try {
	    dtResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
	}
	catch (Exception e) {
	    throw new DocerException(e.getMessage());
	}

	Collection<DataRow<String>> searchResults = dtResults.getRows();

	// verifico lo stato dei related attuali al documento
	for (DataRow<String> dr : searchResults) {

	    String idRel = dr.get(Constants.doc_docnum);

	    // criteria.remove(idRel);

	    String relCodEnte = dr.get(Constants.doc_cod_ente);
	    String relCodAOO = dr.get(Constants.doc_cod_aoo);
	    EnumTipiComponente tipoComponenteRelated = getEnumTipiComponente(dr.get(Constants.doc_tipo_componente));
	    String relDocnumPrinc = dr.get(Constants.doc_docnum_princ);

	    EnumStatiPantarei relStatoPantarei = getEnumStatoPantarei(dr.get(Constants.doc_stato_pantarei));

	    if (relCodEnte == null || relCodEnte.equals(VOID)) {
		throw new DocerException("il related " + idRel + " ha COD_ENTE non assegnato");
	    }

	    if (relCodAOO == null || relCodAOO.equals(VOID)) {
		throw new DocerException("il related " + idRel + " ha COD_AOO non assegnato");
	    }

	    if (!relCodEnte.equalsIgnoreCase(codEnte)) {
		throw new DocerException("il related " + idRel + " ha COD_ENTE " + relCodEnte + " che non coincide con COD_ENTE del documento " + docId + ": " + codEnte);
	    }

	    if (!relCodAOO.equalsIgnoreCase(codAOO)) {
		throw new DocerException("il related " + idRel + " ha COD_AOO " + relCodAOO + " che non coincide con COD_AOO del documento " + docId + ": " + codAOO);
	    }

	    if (tipoComponenteRelated.equals(EnumTipiComponente.PRINCIPALE)) {
		throw new DocerException("il related " + idRel + " ha " + Constants.doc_tipo_componente + " PRINCIPALE");
	    }

	    if (relDocnumPrinc != null && !relDocnumPrinc.equals(VOID) && !relDocnumPrinc.equals(docId)) {
		throw new DocerException("il related " + idRel + " ha un DOCNUM_PRINC " + relDocnumPrinc + " diverso dal DOCNUM " + docId);
	    }

	    if (relStatoPantarei.equals(EnumStatiPantarei.protocollato) || relStatoPantarei.equals(EnumStatiPantarei.fascicolato)) {
		throw new DocerException("compatibilita' DOCAREA: il related " + idRel + " ha STATO_PANTAREI " + relStatoPantarei + " (" + relStatoPantarei.getCode() + ")");
	    }

	    // LockStatus checkedOutInfo;
	    // try {
	    // checkedOutInfo =
	    // (LockStatus)provider.isCheckedOutDocument(idRel);
	    // }
	    // catch (DocerException e) {
	    // throw new DocerException("controllo dei related: " +
	    // e.getMessage());
	    // }
	    //
	    // // se l'allegato e' in check out --> eccezione
	    // if (checkedOutInfo.getLocked()) {
	    // throw new DocerException("il related " + idRel +
	    // " e' in stato di blocco esclusivo");
	    // }

	    // creo i profili solo se non ci sono errori
	    Map<String, String> relProperties = new HashMap<String, String>();

	    // se e' un RECORD
	    if (newMasterStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
		// relProperties.put(Constants.doc_typist_id, typistId);
		relProperties.put(Constants.doc_docnum_princ, docId);
		relProperties.put(Constants.doc_stato_archivistico, Integer.toString(newMasterStatoArchivistico.getCode()));

		if (newMasterStatoPantarei.equals(EnumStatiPantarei.protocollato) || newMasterStatoPantarei.equals(EnumStatiPantarei.fascicolato)) {
		    relProperties.put(Constants.doc_stato_pantarei, Integer.toString(EnumStatiPantarei.allegato.getCode()));
		}

		relProperties.put(Constants.doc_tipo_componente, tipoComponenteRelated.toString());
		if (tipoComponenteRelated.equals(EnumTipiComponente.UNDEFINED)) {
		    relProperties.put(Constants.doc_tipo_componente, EnumTipiComponente.ALLEGATO.toString());
		}
	    }

	    relProfiles.put(idRel, relProperties);

	} // fine ciclo for

	return relProfiles;
    }

    private List<String> getFilteredRelated(String docId, List<EnumTipiComponente> filterTipoComponenteRelated) throws DocerException {

	List<String> typedRelated = new ArrayList<String>();

	List<String> relatedChain = provider.getRelatedDocuments(docId);

	// criteri di ricerca
	Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	List<String> criteria = new ArrayList<String>();

	// metadati che voglio ricercare
	List<String> returnProperties = new ArrayList<String>();
	returnProperties.add(Constants.doc_docnum);

	DataTable<String> dtResults = null;

	criteria.clear();

	for (String id : relatedChain) {

	    if (id.equals(docId))
		continue;

	    if (!criteria.contains(id))
		criteria.add(id);
	}

	if (criteria.size() == 0) {
	    return typedRelated;
	}

	// criteri di ricerca
	searchCriteria.clear();
	searchCriteria.put(Constants.doc_docnum, criteria);

	// cerco tutti gli stati pantarei dei documenti related attualmente
	try {
	    dtResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
	}
	catch (Exception e) {
	    throw new DocerException(e.getMessage());
	}

	Collection<DataRow<String>> searchResults = dtResults.getRows();

	// verifico lo stato dei related attuali al documento
	for (DataRow<String> dr : searchResults) {

	    String idRel = dr.get(Constants.doc_docnum);

	    EnumTipiComponente tipoComponenteRelated = getEnumTipiComponente(dr.get(Constants.doc_tipo_componente));

	    if (filterTipoComponenteRelated.contains(tipoComponenteRelated)) {
		if (typedRelated.contains(idRel)) {
		    continue;
		}

		typedRelated.add(idRel);
	    }
	}

	return typedRelated;
    }

    private enum OPERAZIONE {
	registrazione, protocollazione, classificazione, fascicolazione, pubblicazione, archiviazione_in_deposito
    };

    private DataRow<String> getOldProfile(OPERAZIONE operazione, String docId) throws DocerException {

	Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	// impostazione dell'unico criterio di ricerca
	List<String> criteria = new ArrayList<String>();
	criteria.add(docId);
	searchCriteria.put(Constants.doc_docnum, criteria);

	// richiedo le proprieta' da confrontare
	List<String> returnProperties = new ArrayList<String>();
	returnProperties.add(Constants.doc_stato_pantarei);
	returnProperties.add(Constants.doc_cod_ente);
	returnProperties.add(Constants.doc_cod_aoo);
	returnProperties.add(Constants.doc_type_id);
	returnProperties.add(Constants.doc_stato_archivistico);
	returnProperties.add(Constants.doc_tipo_componente);
	returnProperties.add(Constants.doc_pubblicazione_pubblicato);
	returnProperties.add(Constants.doc_docnum_record);

	if (operazione.equals(OPERAZIONE.classificazione)) {
	    returnProperties.add(Constants.doc_classifica);
	    returnProperties.add(Constants.doc_progr_fascicolo);
	}
	else if (operazione.equals(OPERAZIONE.fascicolazione)) {

	    returnProperties.add(Constants.doc_classifica);
	    returnProperties.add(Constants.doc_anno_fascicolo);
	    returnProperties.add(Constants.doc_progr_fascicolo);
	    returnProperties.add(Constants.doc_fascicoli_secondari);
	}
	else if (operazione.equals(OPERAZIONE.registrazione)) {

	    returnProperties.add(Constants.doc_classifica);
	    returnProperties.add(Constants.doc_progr_fascicolo);

	    returnProperties.add(Constants.doc_anno_fascicolo);

	    returnProperties.add(Constants.doc_registrazione_anno);
	    returnProperties.add(Constants.doc_registrazione_data);
	    returnProperties.add(Constants.doc_registrazione_id_registro);
	    returnProperties.add(Constants.doc_registrazione_numero);
	    returnProperties.add(Constants.doc_registrazione_oggetto);

	    returnProperties.add(Constants.doc_protocollo_numero);
	    returnProperties.add(Constants.doc_protocollo_tipo_protocollazione);

	    returnProperties.add(Constants.doc_reg_e_proto_destinatari);
	    returnProperties.add(Constants.doc_reg_e_proto_firmatario);
	    returnProperties.add(Constants.doc_reg_e_proto_mittenti);
	    returnProperties.add(Constants.doc_reg_e_proto_tipo_firma);

	    // nuovi metadati 25-10-2012
	    returnProperties.add(Constants.doc_registrazione_annullata_registrazione);
	    returnProperties.add(Constants.doc_registrazione_data_annullamento_registrazione);
	    returnProperties.add(Constants.doc_registrazione_motivo_annullamento_registrazione);
	}
	else if (operazione.equals(OPERAZIONE.protocollazione)) {

	    returnProperties.add(Constants.doc_protocollo_numero);
	    returnProperties.add(Constants.doc_protocollo_anno);
	    returnProperties.add(Constants.doc_protocollo_oggetto);
	    returnProperties.add(Constants.doc_protocollo_registro);
	    returnProperties.add(Constants.doc_protocollo_data);
	    returnProperties.add(Constants.doc_protocollo_tipo_protocollazione);

	    returnProperties.add(Constants.doc_classifica);

	    returnProperties.add(Constants.doc_anno_fascicolo);
	    returnProperties.add(Constants.doc_progr_fascicolo);

	    returnProperties.add(Constants.doc_registrazione_numero);

	    returnProperties.add(Constants.doc_reg_e_proto_destinatari);
	    returnProperties.add(Constants.doc_reg_e_proto_firmatario);
	    returnProperties.add(Constants.doc_reg_e_proto_mittenti);
	    returnProperties.add(Constants.doc_reg_e_proto_tipo_firma);

	    // nuovi metadati 25-10-2012
	    returnProperties.add(Constants.doc_protocollo_num_pg_mittente);
	    returnProperties.add(Constants.doc_protocollo_data_pg_mittente);
	    returnProperties.add(Constants.doc_protocollo_cod_ente_mittente);
	    returnProperties.add(Constants.doc_protocollo_cod_aoo_mittente);
	    returnProperties.add(Constants.doc_protocollo_classifica_mittente);
	    returnProperties.add(Constants.doc_protocollo_fascicolo_mittente);
	    returnProperties.add(Constants.doc_protocollo_annullato_protocollo);
	    returnProperties.add(Constants.doc_protocollo_data_annullamento_protocollo);
	    returnProperties.add(Constants.doc_protocollo_motivo_annullamento_protocollo);
	}
	else if (operazione.equals(OPERAZIONE.pubblicazione)) {

	    returnProperties.add(Constants.doc_pubblicazione_registro);
	    returnProperties.add(Constants.doc_pubblicazione_numero);
	    returnProperties.add(Constants.doc_pubblicazione_anno);
	    returnProperties.add(Constants.doc_pubblicazione_oggetto);
	    returnProperties.add(Constants.doc_pubblicazione_data_inizio);
	    returnProperties.add(Constants.doc_pubblicazione_data_fine);
	}
	else if (operazione.equals(OPERAZIONE.archiviazione_in_deposito)) {

	}

	// test della library

	// RICERCA PRIMARIA
	DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	// deve esserci un solo risultato perche' devo modificare un solo
	// documento
	if (searchResults.getRows().size() == 0)
	    throw new DocerException("documento non trovato");
	if (searchResults.getRows().size() != 1)
	    throw new DocerException("docId non univoco");

	// HO TROVATO IL DOCUMENTO -> un solo risultato
	return searchResults.getRow(0);

    }

    private void doOperationCheck(OPERAZIONE operazione, String docId, String userid, DataRow<String> oldProfileData, Map<String, String> metadata) throws DocerException {

	String oldDocnumRecord = oldProfileData.get(Constants.doc_docnum_record);
	if (oldDocnumRecord != null && !oldDocnumRecord.equals(VOID)) {

	    if (operazione.equals(OPERAZIONE.registrazione) || operazione.equals(OPERAZIONE.protocollazione)) {
		throw new DocerException("non e' possibile eseguire l'operazione di " + operazione + " per un documento con DOCNUM_RECORD assegnato (" + oldDocnumRecord + ")");
	    }
	}

	ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);
	if (checkedOutInfo.getLocked() && !checkedOutInfo.getUserId().equalsIgnoreCase(userid))
	    throw new DocerException("documento bloccato esclusivamente da un altro utente");

	EnumTipiComponente oldTipoComponente = getEnumTipiComponente(oldProfileData.get(Constants.doc_tipo_componente));

	if (!oldTipoComponente.equals(EnumTipiComponente.UNDEFINED) && !oldTipoComponente.equals(EnumTipiComponente.PRINCIPALE))
	    throw new DocerException("non e' possibile eseguire esplicitamente l'operazione di " + operazione + " per un documento con " + Constants.doc_tipo_componente + " " + oldTipoComponente);

	EnumStatiPantarei oldStatoPantarei = getEnumStatoPantarei(oldProfileData.get(Constants.doc_stato_pantarei));

	if (oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {

	    throw new DocerException("compatibilita' DOCAREA: non e' possibile eseguire l'operazione di " + operazione + " per un documento con STATO_PANTAREI: " + oldStatoPantarei + " ("
		    + oldStatoPantarei.getCode() + ")");
	}

	EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));

	if (oldStatoArchivistico.equals(EnumStatoArchivistico.in_archivio_di_deposito)) {

	    throw new DocerException("non e' possibile modificare un documento con STATO_ARCHIVISTICO: " + EnumStatoArchivistico.in_archivio_di_deposito + " ("
		    + EnumStatoArchivistico.in_archivio_di_deposito.getCode() + ")");
	}

	String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
	if (oldCodEnte == null || oldCodEnte.equals(VOID))
	    throw new DocerException("ERRORE: documento " + docId + " senza COD_ENTE assegnato");

	String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);
	if (oldCodAOO == null || oldCodAOO.equals(VOID))
	    throw new DocerException("ERRORE: documento " + docId + " senza COD_AOO assegnato");

	if (!metadata.containsKey(Constants.doc_cod_ente)) {
	    // throw new DocerException("COD_ENTE obbligatorio");
	    metadata.put("COD_ENTE", oldCodEnte);
	}

	String newCodEnte = metadata.get(Constants.doc_cod_ente);
	if (!oldCodEnte.equalsIgnoreCase(newCodEnte))
	    throw new DocerException("COD_ENTE specificato (" + newCodEnte + ") e' diverso da COD_ENTE assegnato al documento (" + oldCodEnte + ")");

	if (!metadata.containsKey(Constants.doc_cod_aoo)) {
	    // throw new DocerException("COD_AOO obbligatorio");
	    metadata.put("COD_AOO", oldCodAOO);
	}

	String newCodAOO = metadata.get(Constants.doc_cod_aoo);
	if (!oldCodAOO.equalsIgnoreCase(newCodAOO))
	    throw new DocerException("COD_AOO specificato (" + newCodAOO + ") e' diverso da COD_AOO assegnato al documento (" + oldCodAOO + ")");

	// documentType NON puo' essere null (e' dato obbligatorio di
	// creazione)
	String oldDocumentTypeId = oldProfileData.get(Constants.doc_type_id);

	if (oldDocumentTypeId == null)
	    throw new DocerException("document type non assegnato al documento nel sistema: " + docId);

	oldDocumentTypeId = oldDocumentTypeId.toUpperCase();

	DocumentType docType = DOCUMENT_TYPES.get(oldDocumentTypeId);

	if (docType == null) {

	    throw new DocerException("type_id sconosciuto: " + oldDocumentTypeId);
	}

	if (!docType.isDefinedForAOO(oldCodEnte, oldCodAOO)) {
	    throw new DocerException("Document Type " + oldDocumentTypeId + " non e' definito per Ente " + oldCodEnte + " e AOO " + oldCodAOO);

	}

	FieldDescriptor fd = null;
	// controllo se i campi specifiati appartengono al documenttype
	for (String str : metadata.keySet()) {

	    fd = docType.getFieldDescriptor(oldCodEnte, oldCodAOO, str);

	    if (fd == null)
		throw new DocerException("il campo " + str + " non appartiene al tipo " + oldDocumentTypeId);

	    // controllo lunghezza e formato dei metadati e modifico il formato
	    metadata.put(str, fd.checkValueFormat(metadata.get(str)));

	}

	// CONTROLLO I METADATI DA NON SPECIFICARE
	for (String propName : metadata.keySet()) {

	    if (propName.equalsIgnoreCase(Constants.doc_cod_ente) || propName.equalsIgnoreCase(Constants.doc_cod_aoo)) {
		continue;
	    }

	    if (operazione.equals(OPERAZIONE.registrazione)) {

		// metadati di registrazione
		if (propName.equalsIgnoreCase(Constants.doc_registrazione_anno) || propName.equalsIgnoreCase(Constants.doc_registrazione_data)
			|| propName.equalsIgnoreCase(Constants.doc_registrazione_id_registro) || propName.equalsIgnoreCase(Constants.doc_registrazione_numero)
			|| propName.equalsIgnoreCase(Constants.doc_registrazione_oggetto) || propName.equalsIgnoreCase(Constants.doc_reg_e_proto_destinatari)
			|| propName.equalsIgnoreCase(Constants.doc_reg_e_proto_firmatario) || propName.equalsIgnoreCase(Constants.doc_reg_e_proto_mittenti)
			|| propName.equalsIgnoreCase(Constants.doc_reg_e_proto_tipo_firma)
			// nuovi metadati 25-10-2012
			|| propName.equalsIgnoreCase(Constants.doc_registrazione_annullata_registrazione) || propName.equalsIgnoreCase(Constants.doc_registrazione_data_annullamento_registrazione)
			|| propName.equalsIgnoreCase(Constants.doc_registrazione_motivo_annullamento_registrazione)) {
		    continue;
		}

		throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);

	    }

	    if (operazione.equals(OPERAZIONE.protocollazione)) {

		if (propName.equalsIgnoreCase(Constants.doc_protocollo_registro) || propName.equalsIgnoreCase(Constants.doc_protocollo_oggetto)
			|| propName.equalsIgnoreCase(Constants.doc_protocollo_data) || propName.equalsIgnoreCase(Constants.doc_protocollo_numero)
			|| propName.equalsIgnoreCase(Constants.doc_protocollo_anno) || propName.equalsIgnoreCase(Constants.doc_protocollo_tipo_protocollazione)
			|| propName.equalsIgnoreCase(Constants.doc_reg_e_proto_destinatari)
			|| propName.equalsIgnoreCase(Constants.doc_reg_e_proto_firmatario)
			|| propName.equalsIgnoreCase(Constants.doc_reg_e_proto_mittenti)
			|| propName.equalsIgnoreCase(Constants.doc_reg_e_proto_tipo_firma)
			|| propName.equalsIgnoreCase(Constants.doc_protocollo_num_pg_mittente)
			// nuovi metadati 25-10-2012
			|| propName.equalsIgnoreCase(Constants.doc_protocollo_data_pg_mittente) || propName.equalsIgnoreCase(Constants.doc_protocollo_cod_ente_mittente)
			|| propName.equalsIgnoreCase(Constants.doc_protocollo_cod_aoo_mittente) || propName.equalsIgnoreCase(Constants.doc_protocollo_classifica_mittente)
			|| propName.equalsIgnoreCase(Constants.doc_protocollo_fascicolo_mittente) || propName.equalsIgnoreCase(Constants.doc_protocollo_annullato_protocollo)
			|| propName.equalsIgnoreCase(Constants.doc_protocollo_data_annullamento_protocollo) || propName.equalsIgnoreCase(Constants.doc_protocollo_motivo_annullamento_protocollo)) {

		    continue;
		}

		throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);

	    }

	    if (operazione.equals(OPERAZIONE.classificazione)) {

		if (propName.equalsIgnoreCase(Constants.doc_classifica)) {
		    continue;
		}

		throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);
	    }

	    if (operazione.equals(OPERAZIONE.fascicolazione)) {

		if (propName.equalsIgnoreCase(Constants.doc_classifica) || propName.equalsIgnoreCase(Constants.doc_progr_fascicolo) || propName.equalsIgnoreCase(Constants.doc_anno_fascicolo)
			|| propName.equalsIgnoreCase(Constants.doc_fascicoli_secondari)) {
		    continue;
		}

		throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);
	    }

	    if (operazione.equals(OPERAZIONE.pubblicazione)) {

		// metadati di pubblicazione
		if (propName.equalsIgnoreCase(Constants.doc_pubblicazione_registro) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_anno)
			|| propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_fine) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_inizio)
			|| propName.equalsIgnoreCase(Constants.doc_pubblicazione_numero) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_oggetto)
			|| propName.equalsIgnoreCase(Constants.doc_pubblicazione_pubblicato)) {
		    continue;
		}

		throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);

	    }

	    if (operazione.equals(OPERAZIONE.archiviazione_in_deposito)) {
		// DA IMPLEMETARE
		// metadati di pubblicazione
		// if () {
		// continue;
		// }

		throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);

	    }

	}

    }

    private Map<String, String> toUCMap(Map<String, String> map) {

	Map<String, String> ucmap = new HashMap<String, String>();

	for (String key : map.keySet()) {

	    ucmap.put(key.toUpperCase(), map.get(key));
	}

	return ucmap;
    }

    private Map<String, List<String>> toUCMapOfList(Map<String, List<String>> mapOfList) {

	Map<String, List<String>> ucmapOfList = new HashMap<String, List<String>>();

	for (String key : mapOfList.keySet()) {

	    ucmapOfList.put(key.toUpperCase(), mapOfList.get(key));
	}

	return ucmapOfList;
    }

    private void setAdvancedVersionsDocnumRecord(String docnumRecord, EnumStatoArchivistico oldStatoArchivistico, EnumStatoArchivistico newStatoArchivistico) throws DocerException {

	// se non cambio stato non faccio modifiche
	if (newStatoArchivistico.equals(oldStatoArchivistico)) {
	    return;
	}

	if (newStatoArchivistico.getCode() <= EnumStatoArchivistico.generico_definitivo.getCode()) {
	    return; // non e' una trasformazione in record
	}

	if (oldStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
	    return; // e' gia' un record
	}

	List<String> advancedVersions = provider.getAdvancedVersions(docnumRecord);

	Map<String, String> metadata = new HashMap<String, String>();
	metadata.put(Constants.doc_docnum_record, "");

	// setto il master
	provider.updateProfileDocument(docnumRecord, metadata);

	metadata.put(Constants.doc_docnum_record, docnumRecord);

	advancedVersions.remove(docnumRecord);
	for (String docId : advancedVersions) {
	    provider.updateProfileDocument(docId, metadata);
	}

    }

    private String getExtension(String docname) {

	if (docname == null) {
	    return "";
	}

	// recupero estensione contenuta nel docname
	String ext = "";
	if (docname.indexOf(".") > -1) {
	    // estensione
	    ext = docname.replaceAll("^.*\\.", "");
	}

	return ext;
    }

    // private void validateNextConfigurationXml(String xml) throws
    // DocerException {
    //
    // try {
    //
    // StAXOMBuilder builder = new StAXOMBuilder(new
    // ByteArrayInputStream(xml.getBytes("UTF-8")));
    //
    // OMElement testConfig = builder.getDocumentElement();
    //
    // // variabili di Business Logic
    // AXIOMXPath path = new
    // AXIOMXPath("//configuration/group[@name='business_logic_variables']/section[@name='props']");
    // List<OMElement> elements = (List<OMElement>)path.selectNodes(testConfig);
    // if (elements == null || elements.size() == 0) {
    // throw new
    // Exception("//configuration/group[@name='business_logic_variables']/section[@name='props'] -> nodo mancante");
    // }
    //
    // // FIELDS
    // path = new
    // AXIOMXPath("//configuration/group[@name='impianto']/section[@name='fields']");
    // elements = (List<OMElement>)path.selectNodes(testConfig);
    // if (elements == null || elements.size() == 0) {
    // throw new
    // Exception("//configuration/group[@name='impianto']/section[@name='fields'] -> nodo mancante");
    // }
    //
    // // BASE PROFILE
    // path = new
    // AXIOMXPath("//configuration/group[@name='impianto']/section[@name='baseprofile']");
    // elements = (List<OMElement>)path.selectNodes(testConfig);
    // if (elements == null || elements.size() == 0) {
    // throw new
    // Exception("//configuration/group[@name='impianto']/section[@name='baseprofile'] -> nodo mancante");
    // }
    //
    // // DOCUMENT TYPES
    // path = new
    // AXIOMXPath("//configuration/group[@name='impianto']/section[@name='document_types']");
    // elements = (List<OMElement>)path.selectNodes(testConfig);
    // if (elements == null || elements.size() == 0) {
    // throw new
    // Exception("//configuration/group[@name='impianto']/section[@name='document_types'] -> nodo mancante");
    // }
    //
    // // ANAGRAFICHE_TYPES
    // path = new
    // AXIOMXPath("//configuration/group[@name='impianto']/section[@name='anagrafiche_types']");
    // elements = (List<OMElement>)path.selectNodes(testConfig);
    // if (elements == null || elements.size() == 0) {
    // throw new
    // Exception("//configuration/group[@name='impianto']/section[@name='anagrafiche_types'] -> nodo mancante");
    // }
    //
    // // HITLIST
    // path = new
    // AXIOMXPath("//configuration/group[@name='impianto']/section[@name='hitlists']");
    // elements = (List<OMElement>)path.selectNodes(testConfig);
    // if (elements == null || elements.size() == 0) {
    // throw new
    // Exception("//configuration/group[@name='impianto']/section[@name='hitlists'] -> nodo mancante");
    // }
    //
    // // FORM_DINAMICHE documenti
    // path = new
    // AXIOMXPath("//configuration/group[@name='form_dinamiche']/section[@name='documenti']");
    // elements = (List<OMElement>)path.selectNodes(testConfig);
    // if (elements == null || elements.size() == 0) {
    // throw new
    // Exception("//configuration/group[@name='form_dinamiche']/section[@name='documenti'] -> nodo mancante");
    // }
    //
    // // FORM_DINAMICHE anagrafiche
    // path = new
    // AXIOMXPath("//configuration/group[@name='form_dinamiche']/section[@name='anagrafiche']");
    // elements = (List<OMElement>)path.selectNodes(testConfig);
    // if (elements == null || elements.size() == 0) {
    // throw new
    // Exception("//configuration/group[@name='form_dinamiche']/section[@name='anagrafiche'] -> nodo mancante");
    // }
    //
    // }
    // catch (Exception e) {
    //
    // throw new DocerException("validateNextConfigurationXml: " +
    // e.getMessage());
    // }
    // }

    // ***************************************************///

    // 44
    public void setACLDocument(String token, String docId, Map<String, EnumACLRights> acls) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (acls == null)
		throw new DocerException("acls null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    docId = docId.replaceAll("^" + RACC_UID_DOC_PREFIX, "");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    // impostazione dell'unico criterio di ricerca
	    List<String> criteria = new ArrayList<String>();
	    criteria.add(docId);
	    searchCriteria.put(Constants.doc_docnum, criteria);

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(Constants.doc_docnum);
	    returnProperties.add(Constants.doc_stato_archivistico);
	    returnProperties.add(Constants.doc_tipo_componente);
	    returnProperties.add(Constants.doc_stato_pantarei);

	    // test della library

	    // RICERCA PRIMARIA
	    DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    // deve esserci un solo risultato perche' devo modificare un solo
	    // documento
	    if (searchResults.getRows().size() == 0)
		throw new DocerException("documento non trovato");
	    if (searchResults.getRows().size() != 1)
		throw new DocerException("documento non univoco");

	    // ILockStatus checkedOutInfo =
	    // provider.isCheckedOutDocument(docId);
	    // if (checkedOutInfo.getLocked()) // &&
	    // // !checkedOutInfo.getUserId().equalsIgnoreCase(userid))
	    // throw new
	    // DocerException("documento in stato di blocco esclusivo");

	    // HO TROVATO IL DOCUMENTO -> un solo risultato
	    DataRow<String> oldProfileData = searchResults.getRow(0);

	    // stato_pantarei
	    EnumStatiPantarei statoPantarei = getEnumStatoPantarei(oldProfileData.get(Constants.doc_stato_pantarei));

	    // stato_archivistico
	    EnumStatoArchivistico statoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));

	    // controlla lo stato pantarei del documento
	    // checkStatoPantarei(statoArchivistico, statoArchivistico);

	    if (statoArchivistico.equals(EnumStatoArchivistico.undefined))
		throw new DocerException("Errore STATO_ARCHIVISTICO del documento: undefined");

	    EnumTipiComponente tipoComponente = getEnumTipiComponente(oldProfileData.get(Constants.doc_tipo_componente));
	    if (statoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode() && tipoComponente.equals(EnumTipiComponente.ALLEGATO))
		throw new DocerException("non e' possibile impostare esplicitamente ACL a documenti con STATO_ARCHIVISTICO " + statoArchivistico + " (" + statoArchivistico.getCode() + ") e "
			+ Constants.doc_tipo_componente + " " + tipoComponente);

	    if (tipoComponente.equals(EnumTipiComponente.ALLEGATO) && statoPantarei.equals(EnumStatiPantarei.allegato))
		throw new DocerException("compatibilita' DOCAREA: non e' possibile impostare esplicitamente ACL a documenti con TIPO_COMPONENTE ALLEGATO e STATO_PANTAREI allegato (6)");

	    // rimuovo le deny e le remove dalle ACL
	    Map<String, EnumACLRights> newACL = getNewACLForSetACL(acls);
	    // newACL.put(userid.toLowerCase(), EnumACLRights.fullAccess);

	    // recupero lista dei related
	    if (statoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode() && tipoComponente.equals(EnumTipiComponente.PRINCIPALE)) {

		List<String> allegati = getFilteredRelated(docId, Arrays.asList(EnumTipiComponente.UNDEFINED, EnumTipiComponente.ALLEGATO));
		// rimuovo dalla lista il MASTER (nel caso il DMS lo
		// restituisca)
		allegati.remove(docId);

		// aggiorno ACL dei related solo se record
		if (statoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
		    updateRelatedACL(allegati, newACL);
		}
	    }

	    // aggiornamento ACL del documento principale
	    provider.setACLDocument(docId, newACL);

	}
	catch (DocerException docEx) {
	    throw new DocerException(444, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(544, err.getMessage());
	}

    }

    // 45
    public Map<String, EnumACLRights> getACLDocument(String token, String docId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    // impostazione dell'unico criterio di ricerca
	    List<String> criteria = new ArrayList<String>();
	    criteria.add(docId);
	    searchCriteria.put(Constants.doc_docnum, criteria);

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(Constants.doc_docnum);

	    // test della library

	    // // RICERCA PRIMARIA
	    // DataTable<String> searchResults = provider.searchDocuments(
	    // searchCriteria, null, returnProperties,
	    // PRIMARYSEARCH_MAX_ROWS, null);
	    //
	    // // deve esserci un solo risultato
	    // if (searchResults.getRows().size() == 0)
	    // throw new DocerException("documento non trovato");
	    // if (searchResults.getRows().size() != 1)
	    // throw new DocerException("docId non univoco");

	    acls = provider.getACLDocument(docId);

	    return acls;
	}
	catch (DocerException docEx) {

	    throw new DocerException(445, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(545, err.getMessage());
	}

    }

    // 50
    public byte[] downloadVersion(String token, String docId, String versionNumber, String outputFileUniquePath, long maxFileLength) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    if (versionNumber == null || versionNumber.equals(VOID))
		throw new DocerException("versionNumber null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // Ricerca documento con i criteri
	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId obbligatorio");

	    // Map<String, List<String>> searchCriteria = new HashMap<String,
	    // List<String>>();
	    //
	    // // impostazione dell'unico criterio di ricerca
	    // List<String> criteria = new ArrayList<String>();
	    // criteria.add(docId);
	    // searchCriteria.put(Constants.doc_docnum, criteria);
	    //
	    // List<String> returnProperties = new ArrayList<String>();
	    // returnProperties.add(Constants.doc_docnum);
	    // returnProperties.add(Constants.doc_type_id);
	    //
	    // // test
	    //
	    //
	    // // RICERCA PRIMARIA
	    // DataTable<String> searchResults = provider.searchDocuments(
	    // searchCriteria, null, returnProperties,
	    // PRIMARYSEARCH_MAX_ROWS, null);
	    //
	    // // deve esserci un solo risultato perche' devo modificare un solo
	    // // documento
	    // if (searchResults.getRows().size() == 0)
	    // throw new DocerException("documento non trovato");
	    // if (searchResults.getRows().size() != 1)
	    // throw new DocerException("docId non univoco");

	    // HO TROVATO IL DOCUMENTO -> un solo risultato
	    // DataRow<String> profileData = searchResults.getRow(0);

	    // String documentType =
	    // profileData.get(Constants.doc_type_id).toUpperCase();

	    // if (PAPER_DOCUMENTS.contains(documentType.toUpperCase()))
	    // throw new DocerException("I documenti di tipo " + documentType
	    // + " non ammettono file");

	    // List<String> versions = provider.getVersions(docId);
	    // boolean exist = false;
	    // for (String verId : versions) {
	    // if (verId.equals(versionNumber)) {
	    // exist = true;
	    // break;
	    // }
	    // }
	    //
	    // if (!exist)
	    // throw new DocerException("la versione non esiste");

	    byte[] file = provider.downloadVersion(docId, versionNumber, outputFileUniquePath, maxFileLength);

	    return file;

	}
	catch (DocerException docEx) {

	    throw new DocerException(450, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(550, err.getMessage());
	}
    }

    // 52
    public String addNewVersion(String token, String docId, InputStream file) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);

	    if (checkedOutInfo.getLocked() && !checkedOutInfo.getUserId().equalsIgnoreCase(loginUserInfo.getUserId()))
		throw new DocerException("documento bloccato esclusivamente da un altro utente");

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    // impostazione dell'unico criterio di ricerca
	    List<String> criteria = new ArrayList<String>();
	    criteria.add(docId);

	    searchCriteria.put(Constants.doc_docnum, criteria);

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(Constants.doc_docnum);
	    returnProperties.add(Constants.doc_stato_archivistico);
	    returnProperties.add(Constants.doc_stato_pantarei);
	    returnProperties.add(Constants.doc_type_id);
	    // aggiunto il 14/12/2012
	    returnProperties.add(Constants.doc_archive_type);

	    // RICERCA PRIMARIA
	    DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    // deve esserci un solo risultato perche' devo modificare un solo
	    // documento
	    if (searchResults.getRows().size() == 0)
		throw new DocerException("documento non trovato");
	    if (searchResults.getRows().size() != 1)
		throw new DocerException("docId non univoco");

	    // HO TROVATO IL DOCUMENTO -> un solo risultato
	    DataRow<String> profileData = searchResults.getRow(0);

	    EnumStatoArchivistico statoArchivistico = getEnumStatoArchivistico(profileData.get(Constants.doc_stato_archivistico));

	    EnumStatiPantarei statoPantarei = getEnumStatoPantarei(profileData.get(Constants.doc_stato_pantarei));

	    String archiveType = profileData.get(Constants.doc_archive_type);
	    if (archiveType == null || archiveType.equals(VOID)) {
		archiveType = ARCHIVE;
	    }

	    if (archiveType.equalsIgnoreCase(URL)) {
		throw new DocerException("DOCNUM " + docId + ", il documento ha ARCHIVE_TYPE = URL e non ammette versioning del file");
	    }

	    // ARCHIVE
	    // PAPER
	    // URL

	    if (statoArchivistico.getCode() > EnumStatoArchivistico.generico.getCode()) {

		// modifica del 28/02/2013 --> i PAPER sono sempre versionabili
		// modifica del 14/01/2013
		if (!archiveType.equals(PAPER)) {

		    if (!allow_record_versioning_archive) {
			throw new DocerException("DOCNUM " + docId + ", attenzione, il documento ha STATO_ARCHIVISTICO " + statoArchivistico + " (" + statoArchivistico.getCode()
				+ ") e ammette versioning solo se ARCHIVE_TYPE=PAPER");
		    }

		    // qui e' allow_record_versioning_archive = true
		    if (!archiveType.equalsIgnoreCase(ARCHIVE)) {
			throw new DocerException("DOCNUM " + docId + ", attenzione, il documento ha STATO_ARCHIVISTICO " + statoArchivistico + " (" + statoArchivistico.getCode()
				+ ") e ammette versioning solo se ARCHIVE_TYPE=PAPER oppure ARCHIVE_TYPE=ARCHIVE (o vuoto)");
		    }

		    // togliamo il controllo sullo stato pantarei perche' non
		    // piu' applicabile
		}

	    }

	    // e' richiesto anche il file documento oltre al profilo
	    if (file == null)
		throw new DocerException("file null");

	    if (file.available() == 0)
		throw new DocerException("file vuoto");

	    String versionNumber = provider.addNewVersion(docId, file);

	    return versionNumber;

	}
	catch (DocerException docEx) {

	    throw new DocerException(452, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(552, err.getMessage());
	}

    }

    // 53
    public void lockDocument(String token, String docId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);

	    if (checkedOutInfo.getLocked()) {
		if (!checkedOutInfo.getUserId().equalsIgnoreCase(loginUserInfo.getUserId())) {
		    throw new DocerException("documento bloccato esclusivamente da un altro utente");
		}
		// e' gia' bloccato
		return;
	    }

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    // impostazione dell'unico criterio di ricerca
	    List<String> criteria = new ArrayList<String>();
	    criteria.add(docId);
	    searchCriteria.put(Constants.doc_docnum, criteria);

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(Constants.doc_docnum);
	    returnProperties.add(Constants.doc_stato_archivistico);
	    returnProperties.add(Constants.doc_stato_pantarei);
	    returnProperties.add(Constants.doc_archive_type);
	    // test della library

	    // RICERCA PRIMARIA
	    DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    // deve esserci un solo risultato
	    if (searchResults.getRows().size() == 0)
		throw new DocerException("documento non trovato");
	    if (searchResults.getRows().size() != 1)
		throw new DocerException("docId non univoco");

	    // NON POSSIAMO PROIBIRE IL LOCK PERCHE' NON SAPPIAMO SE POI
	    // MODIFICHERANNO IL FILE O SOLO IL PROFILO!!!
	    // HO TROVATO IL DOCUMENTO -> un solo risultato
	    // DataRow<String> profileData = searchResults.getRow(0);
	    //
	    // // stato_pantarei
	    // EnumStatoArchivistico statoArchivistico =
	    // getEnumStatoArchivistico(profileData.get(Constants.doc_stato_archivistico));
	    // EnumStatiPantarei statoPantarei =
	    // getEnumStatoPantarei(profileData.get(Constants.doc_stato_pantarei));
	    //
	    // String archiveType = profileData.get(Constants.doc_archive_type);
	    // if (archiveType == null || archiveType.equals(VOID)) {
	    // archiveType = ARCHIVE;
	    // }
	    //
	    // if (statoArchivistico.getCode() >
	    // EnumStatoArchivistico.generico.getCode()) {
	    //
	    // // modifica del 28/03/2013 --> i PAPER sono sempre versionabili
	    // if (!archiveType.equals(PAPER)) {
	    //
	    // if (!allow_record_versioning_archive) {
	    // throw new DocerException("DOCNUM " + docId +
	    // ", attenzione, il documento ha STATO_ARCHIVISTICO " +
	    // statoArchivistico + " (" + statoArchivistico.getCode() +
	    // ") e ammette lock e modifica dell'ultima versione solo se ARCHIVE_TYPE=PAPER");
	    // }
	    //
	    // // qui e' allow_record_versioning_archive = true
	    // if (!archiveType.equalsIgnoreCase(ARCHIVE)) {
	    // throw new DocerException("DOCNUM " + docId +
	    // ", attenzione, il documento ha STATO_ARCHIVISTICO " +
	    // statoArchivistico + " (" + statoArchivistico.getCode() +
	    // ") e ammette lock e modifica dell'ultima versione solo se ARCHIVE_TYPE=PAPER oppure ARCHIVE_TYPE=ARCHIVE (o vuoto)");
	    // }
	    // }
	    //
	    // }

	    // if (statoArchivistico.getCode() >
	    // EnumStatoArchivistico.generico.getCode())
	    // throw new
	    // DocerException("non e' permesso eseguire il blocco esclusivo di un documento con STATO_ARCHIVISTICO "
	    // + statoArchivistico + " (" + statoArchivistico.getCode() + ")");
	    //
	    // if (statoPantarei.getCode() > 3) {
	    // throw new
	    // DocerException("compatibilita' DOCAREA: non e' permesso eseguire il blocco esclusivo di un documento con STATO_PANTAREI "
	    // + statoPantarei + " (" + statoPantarei.getCode() + ")");
	    // }

	    // eseguo il lock
	    provider.lockDocument(docId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(453, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(553, err.getMessage());
	}

    }

    // 54
    public void unlockDocument(String token, String docId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);

	    if (!checkedOutInfo.getLocked()) {
		// throw new
		// DocerException("documento non in stato di blocco esclusivo");
		return;
	    }

	    // if
	    // (!checkedOutInfo.getUserId().equalsIgnoreCase(loginUserInfo.getUserId())
	    // && !provider.userIsManager(loginUserInfo.getUserId())) //
	    // l'unlock lo
	    // // permettiamo comunque
	    // // agli amministratori
	    // throw new DocerException(
	    // "documento bloccato esclusamente da un altro utente");

	    // eseguo il lock
	    provider.unlockDocument(docId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(454, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(554, err.getMessage());
	}

    }

    // 55
    public ILockStatus getLockStatus(String token, String docId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    return provider.isCheckedOutDocument(docId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(455, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(555, err.getMessage());
	}
    }

    // 57
    public void deleteDocument(String token, String docId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    // impostazione dell'unico criterio di ricerca
	    List<String> criteria = new ArrayList<String>();
	    criteria.add(docId);
	    searchCriteria.put(Constants.doc_docnum, criteria);

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(Constants.doc_docnum);
	    returnProperties.add(Constants.doc_stato_archivistico);

	    // test della library

	    ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);

	    if (checkedOutInfo.getLocked())
		throw new DocerException("documento in stato di blocco esclusivo");

	    // RICERCA PRIMARIA
	    DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    // deve esserci un solo risultato
	    if (searchResults.getRows().size() == 0)
		throw new DocerException("documento non trovato");
	    if (searchResults.getRows().size() != 1)
		throw new DocerException("docId non univoco");

	    // HO TROVATO IL DOCUMENTO -> un solo risultato
	    DataRow<String> profileData = searchResults.getRow(0);

	    EnumStatiPantarei statoPantarei = getEnumStatoPantarei(profileData.get(Constants.doc_stato_pantarei));

	    // stato_archivistico
	    EnumStatoArchivistico statoArchivistico = getEnumStatoArchivistico(profileData.get(Constants.doc_stato_archivistico));

	    if (statoArchivistico.getCode() > EnumStatoArchivistico.generico.getCode()) {
		throw new DocerException("non e' permesso eliminare documenti con STATO_ARCHIVISTICO " + statoArchivistico + " (" + statoArchivistico.getCode() + ")");
	    }

	    if (statoPantarei.getCode() > 3)
		throw new DocerException("compatibilita' DOCAREA: non e' permesso eliminare documenti con STATO_PANTAREI " + statoPantarei + " (" + statoPantarei.getCode() + ")");

	    // esecuzione della cancellazione
	    provider.deleteDocument(docId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(457, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(557, err.getMessage());
	}

    }

    // 58
    public List<String> getVersions(String token, String docId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    return provider.getVersions(docId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(458, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(558, err.getMessage());
	}
    }

    // 59
    public EnumACLRights getUserRights(String token, String docId, String userId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    if (userId == null || userId.equals(VOID))
		throw new DocerException("userId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    return provider.getEffectiveRights(docId, userId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(459, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(559, err.getMessage());
	}

    }

    // 60
    public List<IHistoryItem> getHistory(String token, String docId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    return provider.getHistory(docId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(460, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(560, err.getMessage());
	}

    }

    // ************** ANAGRAFICHE ********************

    // 79
    public void createUser(String token, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    String codEnte = null;
	    String codAOO = null;

	    IUserProfileInfo userProfile = new UserProfileInfo();

	    for (String key : info.keySet()) {

		if (key == null)
		    continue;

		if (key.equalsIgnoreCase(Constants.user_user_id)) {
		    userProfile.setUserId(info.get(Constants.user_user_id));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.user_full_name)) {
		    userProfile.setFullName(info.get(Constants.user_full_name));
		    continue;
		}

		if (key.equalsIgnoreCase(Constants.user_email_address)) {
		    userProfile.setEmailAddress(info.get(Constants.user_email_address));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.user_first_name)) {
		    userProfile.setFirstName(info.get(Constants.user_first_name));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.user_last_name)) {
		    userProfile.setLastName(info.get(Constants.user_last_name));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.user_user_password)) {
		    userProfile.setUserPassword(info.get(Constants.user_user_password));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.user_network_alias)) {
		    userProfile.setNetworkAlias(info.get(Constants.user_network_alias));
		    continue;
		}

		if (key.equalsIgnoreCase(Constants.user_enabled)) {
		    userProfile.setEnabled(getEnumBoolean(info.get(Constants.user_enabled)));
		    continue;
		}

		if (key.equalsIgnoreCase(Constants.user_cod_ente)) {
		    codEnte = info.get(Constants.user_cod_ente);
		}
		else if (key.equalsIgnoreCase(Constants.user_cod_aoo)) {
		    codAOO = info.get(Constants.user_cod_aoo);
		}
		// le extrainfo
		userProfile.getExtraInfo().put(key.toUpperCase(), info.get(key));
	    }

	    if (userProfile.getUserId() == null || userProfile.getUserId().equals(VOID))
		throw new DocerException("specificare USER_ID");

	    if (userProfile.getFullName() == null || userProfile.getFullName().equals(VOID))
		throw new DocerException("specificare FULL_NAME");

	    // verifica esistenza utente
	    if (provider.getUser(userProfile.getUserId()) != null)
		throw new DocerException("Utente esistente: " + userProfile.getUserId());

	    Map<String, String> id = null;

	    // verifica esistenza dell'Ente
	    if (codEnte != null && !codEnte.equals(VOID)) {
		id = new HashMap<String, String>();
		id.put(Constants.ente_cod_ente, codEnte);
		Map<String, String> p = getAnagraficaProfile("ENTE", id);

		if (p == null) {
		    throw new DocerException("Ente non trovato: " + codEnte);
		}
	    }

	    // verifica esistenza della AOO
	    if (codAOO != null && !codAOO.equals(VOID)) {

		if (codEnte == null || codEnte.equals(VOID)) {
		    throw new DocerException("AOO non trovata, specificare COD_ENTE");
		}
		if (id != null) {
		    id.clear();
		}
		else {
		    id = new HashMap<String, String>();
		}

		id.put(Constants.aoo_cod_ente, codEnte);
		id.put(Constants.aoo_cod_aoo, codAOO);
		Map<String, String> p = getAnagraficaProfile("AOO", id);

		if (p == null) {
		    throw new DocerException("AOO non trovata: " + codAOO);
		}
	    }

	    List<String> criteria = new ArrayList<String>();
	    criteria.add(loginUserInfo.getUserId());

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put(Constants.group_group_id, criteria);

	    List<IGroupProfileInfo> r = provider.searchGroups(searchCriteria);
	    // verifica esistenza utente
	    if (r != null && r.size() > 0)
		throw new DocerException("non e' ammesso assegnare ad un utente lo stesso id di un gruppo: " + userProfile.getUserId());

	    provider.createUser(userProfile);

	}
	catch (DocerException docEx) {

	    throw new DocerException(479, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(579, err.getMessage());
	}
    }

    // 80
    public void updateUser(String token, String userId, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    if (userId == null)
		throw new DocerException("userId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    // verifica presenza del parametro id
	    if (userId == null || userId.equals(VOID))
		throw new DocerException("Utente non trovato: specificare userId");

	    IUserProfileInfo newUserProfile = new UserProfileInfo();
	    String newCodEnte = null;
	    String newCodAOO = null;
	    for (String key : info.keySet()) {

		if (key == null)
		    continue;

		if (key.equalsIgnoreCase(Constants.user_user_id)) {
		    String newUserId = info.get(Constants.user_user_id);

		    if (newUserId != null && !newUserId.equals(userId))
			throw new DocerException("non e' permesso modificare USER_ID");

		    continue;
		}
		if (key.equalsIgnoreCase(Constants.user_full_name)) {
		    newUserProfile.setFullName(info.get(Constants.user_full_name));
		    continue;
		}

		if (key.equalsIgnoreCase(Constants.user_email_address)) {
		    newUserProfile.setEmailAddress(info.get(Constants.user_email_address));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.user_first_name)) {
		    newUserProfile.setFirstName(info.get(Constants.user_first_name));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.user_last_name)) {
		    newUserProfile.setLastName(info.get(Constants.user_last_name));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.user_user_password)) {
		    newUserProfile.setUserPassword(info.get(Constants.user_user_password));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.user_network_alias)) {
		    newUserProfile.setNetworkAlias(info.get(Constants.user_network_alias));
		    continue;
		}

		if (key.equalsIgnoreCase(Constants.user_enabled)) {
		    newUserProfile.setEnabled(getEnumBoolean(info.get(Constants.user_enabled)));
		    continue;
		}

		if (key.equalsIgnoreCase(Constants.user_cod_ente)) {
		    newCodEnte = info.get(Constants.user_cod_ente);
		}
		else if (key.equalsIgnoreCase(Constants.user_cod_aoo)) {
		    newCodAOO = info.get(Constants.user_cod_aoo);
		}

		newUserProfile.getExtraInfo().put(key, info.get(key));
	    }

	    IUserInfo userInfo = provider.getUser(userId);
	    // verifica esistenza utente
	    if (userInfo == null)
		throw new DocerException("Utente non trovato: " + userId);

	    String oldCodEnte = userInfo.getProfileInfo().getExtraInfo().get(Constants.user_cod_ente);
	    String oldCodAOO = userInfo.getProfileInfo().getExtraInfo().get(Constants.user_cod_aoo);

	    Map<String, String> id = null;

	    // verifica esistenza dell'Ente
	    if (newCodEnte != null && !newCodEnte.equals(VOID) && !newCodEnte.equalsIgnoreCase(oldCodEnte)) {
		id = new HashMap<String, String>();
		id.put(Constants.ente_cod_ente, newCodEnte);
		Map<String, String> p = getAnagraficaProfile("ENTE", id);

		if (p == null) {
		    throw new DocerException("nuovo Ente non trovato: " + newCodEnte);
		}
	    }

	    // verifica esistenza della AOO
	    if (newCodAOO != null && !newCodAOO.equals(VOID) && !newCodAOO.equalsIgnoreCase(oldCodAOO)) {

		if (newCodEnte == null || newCodEnte.equals(VOID)) {
		    throw new DocerException("nuova AOO non trovata, specificare COD_ENTE");
		}
		if (id != null) {
		    id.clear();
		}
		else {
		    id = new HashMap<String, String>();
		}
		id.put(Constants.aoo_cod_ente, newCodEnte);
		id.put(Constants.aoo_cod_aoo, newCodAOO);
		Map<String, String> p = getAnagraficaProfile("AOO", id);

		if (p == null) {
		    throw new DocerException("nuova AOO non trovata: " + newCodAOO);
		}
	    }

	    provider.updateUser(userId, newUserProfile);

	}
	catch (DocerException docEx) {

	    throw new DocerException(480, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(580, err.getMessage());
	}
    }

    // 81
    public Map<String, String> getUser(String token, String userId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (userId == null)
		throw new DocerException("userId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // verifica presenza del parametro id
	    if (userId == null || userId.equals(VOID))
		throw new DocerException("Utente non trovato: specificare userId");

	    // boolean isManager = provider.userIsManager(currentUser);
	    // if (!isManager && !currentUser.equalsIgnoreCase(userId))
	    // // l'utente non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    // verifica esistenza utente
	    IUserInfo userInfo = provider.getUser(userId);

	    if (userInfo == null)
		return null;

	    Map<String, String> user = new HashMap<String, String>();

	    user.put(Constants.user_user_id, userInfo.getProfileInfo().getUserId());
	    user.put(Constants.user_full_name, userInfo.getProfileInfo().getFullName());
	    user.put(Constants.user_email_address, userInfo.getProfileInfo().getEmailAddress());
	    user.put(Constants.user_first_name, userInfo.getProfileInfo().getFirstName());
	    user.put(Constants.user_last_name, userInfo.getProfileInfo().getLastName());
	    user.put(Constants.user_network_alias, userInfo.getProfileInfo().getNetworkAlias());

	    user.put(Constants.user_enabled, userInfo.getProfileInfo().getEnabled().toString().toLowerCase());

	    // le extrainfo
	    for (String key : userInfo.getProfileInfo().getExtraInfo().keySet()) {
		user.put(key, userInfo.getProfileInfo().getExtraInfo().get(key));
	    }

	    // user.put("IS_ADMINISTRATOR",
	    // String.valueOf(provider.userIsManager(userId)));

	    return user;

	}
	catch (DocerException docEx) {

	    throw new DocerException(481, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(581, err.getMessage());
	}
    }

    // 82
    public void updateGroupsOfUser(String token, String userId, List<String> groupsToAdd, List<String> groupsToRemove) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (userId == null)
		throw new DocerException("userId null");

	    if (groupsToAdd == null)
		groupsToAdd = new ArrayList<String>();

	    if (groupsToRemove == null)
		groupsToRemove = new ArrayList<String>();

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    // verifica presenza del parametro id
	    if (userId == null || userId.equals(VOID))
		throw new DocerException("Utente non trovato: specificare userId");

	    // verifica esistenza utente
	    IUserInfo userInfo = provider.getUser(userId);

	    if (userInfo == null)
		throw new DocerException("Utente non trovato");

	    List<String> finalGroupsToRemove = new ArrayList<String>();
	    List<String> finalGroupsToAdd = new ArrayList<String>();

	    // Nella lista dei gruppi da rimuovere non ci devono essere elementi
	    // VOID
	    for (int i = 0; i < groupsToRemove.size(); i++) {
		if (!groupsToRemove.get(i).equals(VOID))
		    finalGroupsToRemove.add(groupsToRemove.get(i));
	    }

	    // Nella lista dei gruppi da rimuovere non ci devono essere elementi
	    // VOID
	    for (int i = 0; i < groupsToAdd.size(); i++) {
		if (!groupsToAdd.get(i).equals(VOID))
		    finalGroupsToAdd.add(groupsToAdd.get(i));
	    }

	    // controllo dei gruppi da aggiungere
	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    List<IGroupProfileInfo> groups = null;

	    // rimuovo eventuali gruppi
	    if (finalGroupsToRemove.size() > 0) {

		searchCriteria.clear();

		// controllo degli utenti da rimuovere
		searchCriteria.put(Constants.group_group_id, finalGroupsToRemove);

		groups = provider.searchGroups(searchCriteria);

		for (String id : finalGroupsToRemove) {

		    boolean found = false;

		    for (IGroupProfileInfo groupProfile : groups) {
			if (groupProfile.getGroupId().equals(id)) {
			    found = true;
			    break;
			}
		    }
		    if (!found)
			throw new DocerException("Gruppo da rimuovere non trovato: " + id);
		}

		// rimuovo gruppi
		provider.removeGroupsFromUser(userId, finalGroupsToRemove);
	    }

	    searchCriteria.clear();

	    // aggiungo eventuali gruppi
	    if (finalGroupsToAdd.size() > 0) {

		searchCriteria.put(Constants.group_group_id, finalGroupsToAdd);

		groups = provider.searchGroups(searchCriteria);

		for (String id : finalGroupsToAdd) {

		    boolean found = false;

		    for (IGroupProfileInfo groupProfile : groups) {
			if (groupProfile.getGroupId().equals(id)) {
			    found = true;
			    break;
			}
		    }
		    if (!found)
			throw new DocerException("Gruppo da aggiungere non trovato: " + id);
		}

		// aggiungo gruppi
		provider.addGroupsToUser(userId, finalGroupsToAdd);
	    }

	}
	catch (DocerException docEx) {

	    throw new DocerException(482, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(582, err.getMessage());
	}

    }

    // 83
    public void setGroupsOfUser(String token, String userId, List<String> groupsToSet) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (userId == null)
		throw new DocerException("userId null");

	    if (groupsToSet == null)
		throw new DocerException("groupsToSet null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    // verifica presenza del parametro id
	    if (userId == null || userId.equals(VOID))
		throw new DocerException("Utente non trovato: specificare userId");

	    // verifica esistenza gruppo
	    IUserInfo userInfo = provider.getUser(userId);

	    if (userInfo == null)
		throw new DocerException("Utente " + userId + " non trovato");

	    List<String> groupsToAssign = new ArrayList<String>();

	    // non posso e non devo riassegnare il primary group
	    for (int i = 0; i < groupsToSet.size(); i++) {
		if (!groupsToSet.get(i).equals(VOID)) {
		    groupsToAssign.add(groupsToSet.get(i));
		}
	    }

	    // controllo degli utenti da aggiungere
	    if (groupsToAssign.size() > 0) {
		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
		searchCriteria.put(Constants.group_group_id, groupsToAssign);

		List<IGroupProfileInfo> groups = provider.searchGroups(searchCriteria);

		for (String id : groupsToAssign) {

		    boolean found = false;

		    for (IGroupProfileInfo userProfile : groups) {
			if (userProfile.getGroupId().equals(id)) {
			    found = true;
			    break;
			}
		    }
		    if (!found)
			throw new DocerException("Gruppo da assegnare non trovato: " + id);
		}
	    }

	    // rimuovo tutti i gruppi dell'utente
	    provider.removeGroupsFromUser(userId, userInfo.getGroups());

	    // assegno i nuovi gruppi
	    if (groupsToAssign.size() > 0) {
		provider.addGroupsToUser(userId, groupsToAssign);
	    }

	}
	catch (DocerException docEx) {

	    throw new DocerException(483, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(583, err.getMessage());
	}

    }

    // 84
    public List<String> getGroupsOfUser(String token, String userId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (userId == null)
		throw new DocerException("userId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager = provider.userIsManager(userid);
	    // if (!isManager)
	    // // l'utente non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    // verifica presenza del parametro id
	    if (userId == null || userId.equals(VOID))
		throw new DocerException("Utente non trovato: specificare userId");

	    // verifica esistenza gruppo
	    IUserInfo userInfo = provider.getUser(userId);

	    if (userInfo == null)
		throw new DocerException("Utente " + userId + " non trovato");

	    return userInfo.getGroups();

	}
	catch (DocerException docEx) {

	    throw new DocerException(484, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(584, err.getMessage());
	}

    }

    // 85
    public void createGroup(String token, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    IGroupProfileInfo groupProfile = new GroupProfileInfo();

	    for (String key : info.keySet()) {

		if (key == null)
		    continue;

		if (key.equalsIgnoreCase(Constants.group_group_id)) {
		    groupProfile.setGroupId(info.get(key));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.group_group_name)) {
		    groupProfile.setGroupName(info.get(key));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.group_parent_group_id)) {
		    groupProfile.setParentGroupId(info.get(key));
		    continue;
		}

		groupProfile.getExtraInfo().put(key, info.get(key));
	    }

	    if (groupProfile.getGroupId() == null || groupProfile.getGroupId().equals(VOID))
		throw new DocerException("specificare GROUP_ID");

	    if (groupProfile.getGroupName() == null || groupProfile.getGroupName().equals(VOID))
		throw new DocerException("specificare GROUP_NAME");

	    // if (groupProfile.getParentGroupId() == null ||
	    // groupProfile.getParentGroupId().equals(VOID))
	    // throw new DocerException("specificare PARENT_GROUP_ID");

	    // verifica esistenza gruppo
	    if (provider.getGroup(groupProfile.getGroupId()) != null)
		throw new DocerException("Gruppo esistente: " + groupProfile.getGroupId());

	    // verifica esistenza utente
	    if (provider.getUser(groupProfile.getGroupId()) != null)
		throw new DocerException("non e' ammesso assegnare ad un gruppo lo stesso id di un utente: " + groupProfile.getGroupId());

	    // verifica esistenza gruppo parent se specificato
	    if (groupProfile.getParentGroupId() != null && !groupProfile.getParentGroupId().equals(VOID)) {
		if (provider.getGroup(groupProfile.getParentGroupId()) == null)
		    throw new DocerException("Parent group non trovato: " + groupProfile.getParentGroupId());
	    }

	    provider.createGroup(groupProfile);

	}
	catch (DocerException docEx) {

	    throw new DocerException(485, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(585, err.getMessage());
	}
    }

    // 86
    public void updateGroup(String token, String groupId, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (groupId == null)
		throw new DocerException("groupId null");

	    if (info == null)
		throw new DocerException("info null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    // verifica presenza del parametro id
	    if (groupId == null || groupId.equals(VOID))
		throw new DocerException("Gruppo non trovato: specificare groupId");

	    if (info.get(Constants.group_parent_group_id) != null && !info.get(Constants.group_parent_group_id).equals(VOID) && info.get(Constants.group_parent_group_id).equals(groupId))
		throw new DocerException("gruppo parent coincidente con il gruppo stesso");

	    IGroupProfileInfo newGroupProfile = new GroupProfileInfo();

	    for (String key : info.keySet()) {

		if (key == null)
		    continue;

		if (key.equalsIgnoreCase(Constants.group_group_id)) {
		    newGroupProfile.setGroupId(info.get(key));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.group_group_name)) {
		    newGroupProfile.setGroupName(info.get(key));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.group_parent_group_id)) {
		    newGroupProfile.setParentGroupId(info.get(key));
		    continue;
		}
		if (key.equalsIgnoreCase(Constants.group_enabled)) {
		    newGroupProfile.setEnabled(getEnumBoolean(info.get(Constants.group_enabled)));
		    continue;
		}

		newGroupProfile.getExtraInfo().put(key, info.get(key));
	    }

	    // -- DA FARE -- sollevare eccezione solo se cambia
	    if (newGroupProfile.getGroupId() != null)
		throw new DocerException("non e' permesso modificare GROUP_ID");

	    // verifica esistenza gruppo
	    if (provider.getGroup(groupId) == null)
		throw new DocerException("Gruppo non trovato: " + groupId);

	    // verifica esistenza del gruppo primario
	    if (newGroupProfile.getParentGroupId() != null && !newGroupProfile.getParentGroupId().equals(VOID) && provider.getGroup(newGroupProfile.getParentGroupId()) == null)
		throw new DocerException("Parent group non trovato: " + newGroupProfile.getParentGroupId());

	    provider.updateGroup(groupId, newGroupProfile);

	}
	catch (DocerException docEx) {

	    throw new DocerException(486, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(586, err.getMessage());
	}
    }

    // 87
    public Map<String, String> getGroup(String token, String groupId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (groupId == null)
		throw new DocerException("groupId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // boolean isManager = provider.userIsManager(userid);
	    // if (!isManager)
	    // // l'utente non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    // verifica presenza del parametro id
	    if (groupId == null || groupId.equals(VOID))
		throw new DocerException("Gruppo non trovato: specificare groupId");

	    // verifica esistenza utente
	    IGroupInfo groupInfo = provider.getGroup(groupId);

	    if (groupInfo == null)
		return null;

	    Map<String, String> group = new HashMap<String, String>();

	    group.put(Constants.group_group_id, groupInfo.getProfileInfo().getGroupId());
	    group.put(Constants.group_group_name, groupInfo.getProfileInfo().getGroupName());
	    group.put(Constants.group_parent_group_id, groupInfo.getProfileInfo().getParentGroupId());

	    if (groupInfo.getProfileInfo().getEnabled().equals(EnumBoolean.TRUE))
		group.put(Constants.group_enabled, "true");
	    else if (groupInfo.getProfileInfo().getEnabled().equals(EnumBoolean.FALSE))
		group.put(Constants.group_enabled, "false");

	    group.put(Constants.group_gruppo_struttura, String.valueOf(Boolean.valueOf(groupInfo.getProfileInfo().getExtraInfo().get(Constants.group_gruppo_struttura))));

	    return group;

	}
	catch (DocerException docEx) {

	    throw new DocerException(487, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(587, err.getMessage());
	}
    }

    // 88
    public void updateUsersOfGroup(String token, String groupId, List<String> usersToAdd, List<String> usersToRemove) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (groupId == null)
		throw new DocerException("groupId null");

	    if (usersToAdd == null)
		usersToAdd = new ArrayList<String>();

	    if (usersToRemove == null)
		usersToRemove = new ArrayList<String>();

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    // verifica presenza del parametro id
	    if (groupId == null || groupId.equals(VOID))
		throw new DocerException("Gruppo non trovato: specificare groupId");

	    // verifica esistenza gruppo
	    IGroupInfo groupInfo = provider.getGroup(groupId);

	    if (groupInfo == null)
		throw new DocerException("Gruppo " + groupId + " non trovato");

	    List<String> finalUsersToRemove = new ArrayList<String>();
	    List<String> finalUsersToAdd = new ArrayList<String>();

	    // Nella lista dei gruppi da rimuovere non ci devono essere elementi
	    // VOID
	    for (int i = 0; i < usersToRemove.size(); i++) {
		if (!usersToRemove.get(i).equals(VOID))
		    finalUsersToRemove.add(usersToRemove.get(i));
	    }

	    // Nella lista dei gruppi da aggiungere non ci devono essere
	    // elementi VOID
	    for (int i = 0; i < usersToAdd.size(); i++) {
		if (!usersToAdd.get(i).equals(VOID))
		    finalUsersToAdd.add(usersToAdd.get(i));
	    }

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    List<IUserProfileInfo> users = null;

	    // rimuovo gli utenti eventuali
	    if (finalUsersToRemove.size() > 0) {

		searchCriteria.clear();

		// controllo degli utenti da rimuovere
		searchCriteria.put(Constants.user_user_id, finalUsersToRemove);

		users = provider.searchUsers(searchCriteria);

		for (String id : finalUsersToRemove) {

		    boolean found = false;

		    for (IUserProfileInfo userProfile : users) {
			if (userProfile.getUserId().equals(id)) {
			    found = true;
			    break;
			}
		    }
		    if (!found)
			throw new DocerException("Utente da rimuovere non trovato: " + id);
		}

		// rimuovo utenti
		provider.removeUsersFromGroup(groupId, finalUsersToRemove);

	    }

	    searchCriteria.clear();

	    // aggiungo gli utenti eventuali
	    if (finalUsersToAdd.size() > 0) {

		// controllo degli utenti da aggiungere
		searchCriteria.put(Constants.user_user_id, finalUsersToAdd);

		users = provider.searchUsers(searchCriteria);

		for (String id : finalUsersToAdd) {

		    boolean found = false;

		    for (IUserProfileInfo userProfile : users) {
			if (userProfile.getUserId().equals(id)) {
			    found = true;
			    break;
			}
		    }
		    if (!found)
			throw new DocerException("Utente da aggiungere non trovato: " + id);
		}

		// aggiungo utenti
		provider.addUsersToGroup(groupId, finalUsersToAdd);
	    }

	}
	catch (DocerException docEx) {

	    throw new DocerException(488, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(588, err.getMessage());
	}

    }

    // 89
    public void setUsersOfGroup(String token, String groupId, List<String> usersToSet) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (groupId == null)
		throw new DocerException("groupId null");

	    if (usersToSet == null)
		throw new DocerException("usersToSet null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager =
	    // provider.userIsManager(loginUserInfo.getUserId());
	    // if (!isManager)
	    // // l'utente che sta tentando il login non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    // verifica presenza del parametro id
	    if (groupId == null || groupId.equals(VOID))
		throw new DocerException("Gruppo non trovato: specificare groupId");

	    // verifica esistenza gruppo
	    IGroupInfo groupInfo = provider.getGroup(groupId);

	    if (groupInfo == null)
		throw new DocerException("Gruppo " + groupId + " non trovato");

	    List<String> finalUsersToSet = new ArrayList<String>();

	    // gli utenti da aggiungere non devono essere VOID
	    for (int i = 0; i < usersToSet.size(); i++) {
		if (!usersToSet.get(i).equals(VOID))
		    finalUsersToSet.add(usersToSet.get(i));
	    }

	    // controllo degli utenti da aggiungere
	    if (finalUsersToSet.size() > 0) {

		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
		searchCriteria.put(Constants.user_user_id, finalUsersToSet);

		List<IUserProfileInfo> users = provider.searchUsers(searchCriteria);

		for (String id : finalUsersToSet) {

		    boolean found = false;

		    for (IUserProfileInfo userProfile : users) {
			if (userProfile.getUserId().equals(id)) {
			    found = true;
			    break;
			}
		    }
		    if (!found)
			throw new DocerException("Utente da assegnare non trovato: " + id);
		}
	    }

	    // rimuovo tutti gli utenti del gruppo
	    provider.removeUsersFromGroup(groupId, groupInfo.getUsers());

	    // aggiungo i nuovi utenti
	    if (finalUsersToSet.size() > 0) {
		provider.addUsersToGroup(groupId, finalUsersToSet);
	    }

	}
	catch (DocerException docEx) {

	    throw new DocerException(489, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(589, err.getMessage());
	}

    }

    // 90
    public List<String> getUsersOfGroup(String token, String groupId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (groupId == null)
		throw new DocerException("groupId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    // controllo credenziali di manager
	    // boolean isManager = provider.userIsManager(userid);
	    // if (!isManager)
	    // // l'utente non e' un manager
	    // throw new DocerException(
	    // "Utente non autorizzato per le operazioni di management");

	    // verifica presenza del parametro id
	    if (groupId == null || groupId.equals(VOID))
		throw new DocerException("Gruppo non trovato: specificare groupId");

	    // verifica esistenza gruppo
	    IGroupInfo groupInfo = provider.getGroup(groupId);

	    if (groupInfo == null)
		throw new DocerException("Gruppo " + groupId + " non trovato");

	    return groupInfo.getUsers();

	}
	catch (DocerException docEx) {

	    throw new DocerException(490, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(590, err.getMessage());
	}

    }

    // 97
    public void setACLTitolario(String token, Map<String, String> id, Map<String, EnumACLRights> acls) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (id == null)
		throw new DocerException("Titolario id null");

	    if (acls == null)
		throw new DocerException("acls is null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    String codEnte = id.get(Constants.titolario_cod_ente);
	    String codAoo = id.get(Constants.titolario_cod_aoo);
	    String codTitolario = id.get(Constants.titolario_cod_titolario);
	    String classifica = id.get(Constants.titolario_classifica);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("specificare COD_ENTE");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("specificare COD_AOO");

	    if (codTitolario == null || codTitolario.equals(VOID)) {
		if (classifica == null || classifica.equals(VOID))
		    throw new DocerException("specificare COD_TITOLARIO o CLASSIFICA");
	    }

	    ITitolarioId titolarioId = new TitolarioId();
	    titolarioId.setCodiceEnte(codEnte);
	    titolarioId.setCodiceAOO(codAoo);
	    titolarioId.setCodiceTitolario(codTitolario);
	    titolarioId.setClassifica(classifica);

	    provider.setACLTitolario(titolarioId, acls);

	}
	catch (DocerException docEx) {

	    throw new DocerException(497, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(597, err.getMessage());
	}

    }

    // 98
    public Map<String, EnumACLRights> getACLTitolario(String token, Map<String, String> id) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (id == null)
		throw new DocerException("Titolario id null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    String codEnte = id.get(Constants.titolario_cod_ente);
	    String codAoo = id.get(Constants.titolario_cod_aoo);
	    String codTitolario = id.get(Constants.titolario_cod_titolario);
	    String classifica = id.get(Constants.titolario_classifica);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("specificare COD_ENTE");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("specificare COD_AOO");

	    if (codTitolario == null || codTitolario.equals(VOID)) {
		if (classifica == null || classifica.equals(VOID))
		    throw new DocerException("specificare COD_TITOLARIO o CLASSIFICA");
	    }

	    ITitolarioId titolarioId = new TitolarioId();
	    titolarioId.setCodiceEnte(codEnte);
	    titolarioId.setCodiceAOO(codAoo);
	    titolarioId.setCodiceTitolario(codTitolario);
	    titolarioId.setClassifica(classifica);

	    return provider.getACLTitolario(titolarioId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(498, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(598, err.getMessage());
	}

    }

    // 99
    public void setACLFascicolo(String token, Map<String, String> id, Map<String, EnumACLRights> acls) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (id == null)
		throw new DocerException("Fascicolo id null");

	    if (acls == null)
		throw new DocerException("acls null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    String codEnte = null;
	    String codAoo = null;
	    String classifica = null;
	    String numFascicolo = null;
	    String annoFascicolo = null;
	    String progressivoFascicolo = null;

	    if (id.containsKey(RACC_UID)) {

		String type = "FASCICOLO";
		AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(type);
		if (anagraficaType == null)
		    throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + type);

		List<String> hitlist = HITLISTS.get("HITLIST_" + type);
		String raccuid = id.get(RACC_UID);
		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
		searchCriteria.put(RACC_UID, Arrays.asList(raccuid));
		List<Map<String, String>> res = provider.searchAnagrafiche(type, searchCriteria, hitlist);
		if (res == null || res.size() == 0) {
		    throw new DocerException("setACLFascicolo: Fascicolo con RACC_UID=" + raccuid + " non trovato");
		}
		if (res.size() > 1) {
		    throw new DocerException("setACLFascicolo: trovati " + res.size() + " fascicoli con RACC_UID=" + raccuid);
		}

		codEnte = res.get(0).get(Constants.fascicolo_cod_ente);
		codAoo = res.get(0).get(Constants.fascicolo_cod_aoo);
		classifica = res.get(0).get(Constants.fascicolo_classifica);
		numFascicolo = res.get(0).get(Constants.fascicolo_num_fascicolo);
		annoFascicolo = res.get(0).get(Constants.fascicolo_anno_fascicolo);
		progressivoFascicolo = res.get(0).get(Constants.fascicolo_progr_fascicolo);
	    }
	    else {
		codEnte = id.get(Constants.fascicolo_cod_ente);
		codAoo = id.get(Constants.fascicolo_cod_aoo);
		classifica = id.get(Constants.fascicolo_classifica);
		numFascicolo = id.get(Constants.fascicolo_num_fascicolo);
		annoFascicolo = id.get(Constants.fascicolo_anno_fascicolo);
		progressivoFascicolo = id.get(Constants.fascicolo_progr_fascicolo);
	    }

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

	    if (classifica == null || classifica.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

	    if (numFascicolo == null || numFascicolo.equals(VOID)) {
		if (progressivoFascicolo == null || progressivoFascicolo.equals(VOID))
		    throw new DocerException("Fascicolo non trovato: specificare NUM_FASCICOLO o PROGR_FASCICOLO");
	    }

	    if (annoFascicolo == null || annoFascicolo.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

	    IFascicoloId fascicoloId = new FascicoloId();
	    fascicoloId.setCodiceEnte(codEnte);
	    fascicoloId.setCodiceAOO(codAoo);
	    fascicoloId.setClassifica(classifica);
	    fascicoloId.setNumeroFascicolo(numFascicolo);
	    fascicoloId.setProgressivo(progressivoFascicolo);
	    fascicoloId.setAnnoFascicolo(annoFascicolo);

	    provider.setACLFascicolo(fascicoloId, acls);

	}
	catch (DocerException docEx) {

	    throw new DocerException(499, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(599, err.getMessage());
	}

    }

    // 10
    public Map<String, EnumACLRights> getACLFascicolo(String token, Map<String, String> id) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (id == null)
		throw new DocerException("anagrafica id null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    String codEnte = id.get(Constants.fascicolo_cod_ente);
	    String codAoo = id.get(Constants.fascicolo_cod_aoo);
	    String classifica = id.get(Constants.fascicolo_classifica);
	    String numFascicolo = id.get(Constants.fascicolo_num_fascicolo);
	    String progressivoFascicolo = id.get(Constants.fascicolo_progr_fascicolo);
	    String annoFascicolo = id.get(Constants.fascicolo_anno_fascicolo);

	    if (codEnte == null || codEnte.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

	    if (codAoo == null || codAoo.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

	    if (classifica == null || classifica.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

	    if (numFascicolo == null || numFascicolo.equals(VOID)) {
		if (progressivoFascicolo == null || progressivoFascicolo.equals(VOID))
		    throw new DocerException("Fascicolo non trovato: specificare NUM_FASCICOLO o PROGR_FASCICOLO");
	    }

	    if (annoFascicolo == null || annoFascicolo.equals(VOID))
		throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

	    IFascicoloId fascicoloId = new FascicoloId();
	    fascicoloId.setCodiceEnte(codEnte);
	    fascicoloId.setCodiceAOO(codAoo);
	    fascicoloId.setClassifica(classifica);
	    fascicoloId.setNumeroFascicolo(numFascicolo);
	    fascicoloId.setProgressivo(progressivoFascicolo);
	    fascicoloId.setAnnoFascicolo(annoFascicolo);

	    Map<String, EnumACLRights> acls = provider.getACLFascicolo(fascicoloId);

	    return acls;

	}
	catch (DocerException docEx) {

	    throw new DocerException(410, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(510, err.getMessage());
	}

    }

    // 11
    public void addRiferimentiDocuments(String token, String docId, List<String> toAddRiferimenti) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    if (toAddRiferimenti == null)
		throw new DocerException("lista Riferimenti null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    if (toAddRiferimenti == null || toAddRiferimenti.size() < 1)
		return;

	    List<String> addingList = new ArrayList<String>();

	    for (String rif : toAddRiferimenti) {
		// pulisco la collezione rimuovendo i vuoti, i doppioni e il
		// master
		if (rif == null || rif.equals(VOID) || addingList.contains(rif) || rif.equals(docId))
		    continue;

		addingList.add(rif);
	    }

	    // test della library

	    // ILockStatus checkedOutInfo =
	    // provider.isCheckedOutDocument(docId);
	    // if (checkedOutInfo.getLocked()) // &&
	    // // !checkedOutInfo.getUserId().equalsIgnoreCase(userid))
	    // throw new
	    // DocerException("documento in stato di blocco esclusivo"); //
	    // documento
	    // // bloccato
	    // // esclusivamente
	    // // da
	    // // un
	    // // altro
	    // // utente

	    // controllo checkout dei Riferimenti
	    // checkLockStatusRiferimentiToAdd(addingList);

	    if (addingList.size() > 0)
		provider.addRiferimentiDocuments(docId, addingList);

	}
	catch (DocerException docEx) {

	    throw new DocerException(411, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(511, err.getMessage());
	}

    }

    // 12
    public void removeRiferimentiDocuments(String token, String docId, List<String> toRemoveRiferimenti) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    if (toRemoveRiferimenti == null)
		throw new DocerException("lista Riferimenti null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    if (toRemoveRiferimenti == null || toRemoveRiferimenti.size() < 1)
		return;

	    List<String> removingList = new ArrayList<String>();

	    for (String rif : toRemoveRiferimenti) {
		// pulisco la collezione rimuovendo i vuoti, i doppioni e il
		// master
		if (rif == null || rif.equals(VOID) || removingList.contains(rif) || rif.equals(docId))
		    continue;

		removingList.add(rif);
	    }

	    // test della library

	    // ILockStatus checkedOutInfo =
	    // provider.isCheckedOutDocument(docId);
	    // if (checkedOutInfo.getLocked()) // &&
	    // // !checkedOutInfo.getUserId().equalsIgnoreCase(userid))
	    // throw new
	    // DocerException("documento in stato di blocco esclusivo"); //
	    // documento
	    // // bloccato
	    // // esclusivamente
	    // // da
	    // // un
	    // // altro
	    // // utente

	    // controllo checkout dei Riferimenti
	    // checkLockStatusRiferimentiToRemove(removingList);

	    if (removingList.size() > 0)
		provider.removeRiferimentiDocuments(docId, removingList);

	}
	catch (DocerException docEx) {

	    throw new DocerException(412, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(512, err.getMessage());
	}

    }

    // 13
    public List<String> getRiferimentiDocuments(String token, String docId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // Map<String, List<String>> searchCriteria = new HashMap<String,
	    // List<String>>();
	    // impostazione dell'unico criterio di ricerca
	    // List<String> criteria = new ArrayList<String>();
	    // criteria.add(docId);
	    // searchCriteria.put(Constants.doc_docnum, criteria);
	    //
	    // List<String> returnProperties = new ArrayList<String>();
	    // returnProperties.add(Constants.doc_docnum);

	    // test della library

	    // // RICERCA PRIMARIA
	    // DataTable<String> searchResults = provider.searchDocuments(
	    // searchCriteria, null, returnProperties,
	    // PRIMARYSEARCH_MAX_ROWS, null);

	    // // deve esserci un solo risultato
	    // if (searchResults.getRows().size() == 0)
	    // throw new DocerException("documento non trovato");
	    // if (searchResults.getRows().size() != 1)
	    // throw new DocerException("docId non univoco");

	    List<String> riferimenti = provider.getRiferimentiDocuments(docId);
	    riferimenti.remove(docId);
	    return riferimenti;

	}
	catch (DocerException docEx) {

	    throw new DocerException(413, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(513, err.getMessage());
	}
    }

    // 14
    public void protocollaDocumento(String token, String docId, Map<String, String> metadata) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	metadata = toUCMap(metadata);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (metadata == null)
		throw new DocerException("metadata null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    DataRow<String> oldProfileData = getOldProfile(OPERAZIONE.protocollazione, docId);
	    doOperationCheck(OPERAZIONE.protocollazione, docId, loginUserInfo.getUserId(), oldProfileData, metadata);

	    Map<String, String> metadatiProtocollazione = checkAndGetMetadatiProtocollazione(oldProfileData, metadata);

	    String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
	    String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);

	    EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));
	    EnumStatoArchivistico newStatoArchivistico = getEnumStatoArchivistico(metadatiProtocollazione.get(Constants.doc_stato_archivistico));

	    EnumStatiPantarei newStatoPantarei = getEnumStatoPantarei(metadatiProtocollazione.get(Constants.doc_stato_pantarei));

	    // recupero lista dei related
	    List<String> relatedChain = provider.getRelatedDocuments(docId);
	    // rimuovo dalla lista il MASTER se presente
	    relatedChain.remove(docId);

	    Map<String, EnumACLRights> newACL = null;
	    if (oldStatoArchivistico.getCode() < EnumStatoArchivistico.registrato.getCode() && newStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
		newACL = getACLCompound(docId);
	    }

	    Map<String, Map<String, String>> relNewProperties = checkRecordRelatedChain(newStatoArchivistico, newStatoPantarei, oldCodEnte, oldCodAOO, docId, loginUserInfo.getUserId(), relatedChain);

	    // aggiorno i profili dei related
	    for (String relatedId : relNewProperties.keySet()) {

		// aggiorno il profilo del related
		Map<String, String> newProperties = relNewProperties.get(relatedId);

		// aggiorno il profilo del related
		provider.updateProfileDocument(relatedId, newProperties);

		EnumTipiComponente relTipoComponente = getEnumTipiComponente(newProperties.get(Constants.doc_tipo_componente));

		if (relTipoComponente.equals(EnumTipiComponente.ALLEGATO) && newACL != null) {
		    provider.setACLDocument(relatedId, newACL);
		}
	    }

	    // advanced versions
	    setAdvancedVersionsDocnumRecord(docId, oldStatoArchivistico, newStatoArchivistico);

	    metadatiProtocollazione.put(Constants.doc_tipo_componente, EnumTipiComponente.PRINCIPALE.toString());
	    metadatiProtocollazione.put(Constants.doc_docnum_princ, "");
	    provider.updateProfileDocument(docId, metadatiProtocollazione);

	}
	catch (DocerException docEx) {

	    throw new DocerException(443, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(543, err.getMessage());
	}
    }

    // 15
    public void registraDocumento(String token, String docId, Map<String, String> metadata) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	metadata = toUCMap(metadata);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (metadata == null)
		throw new DocerException("metadata null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    DataRow<String> oldProfileData = getOldProfile(OPERAZIONE.registrazione, docId);
	    doOperationCheck(OPERAZIONE.registrazione, docId, loginUserInfo.getUserId(), oldProfileData, metadata);

	    // **** controllo e profilazione dei dati di registro ****
	    Map<String, String> metadatiRegistrazione = checkAndGetMetadatiRegistrazione(oldProfileData, metadata);

	    String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
	    String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);
	    EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));
	    EnumStatoArchivistico newStatoArchivistico = getEnumStatoArchivistico(metadatiRegistrazione.get(Constants.doc_stato_archivistico));

	    EnumStatiPantarei newStatoPantarei = getEnumStatoPantarei(metadatiRegistrazione.get(Constants.doc_stato_pantarei));

	    // recupero lista dei related
	    List<String> relatedChain = provider.getRelatedDocuments(docId);
	    // rimuovo dalla lista il MASTER se presente
	    relatedChain.remove(docId);

	    Map<String, EnumACLRights> newACL = null;
	    if (oldStatoArchivistico.getCode() < EnumStatoArchivistico.registrato.getCode() && newStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
		newACL = getACLCompound(docId);
	    }

	    Map<String, Map<String, String>> relNewProperties = checkRecordRelatedChain(newStatoArchivistico, newStatoPantarei, oldCodEnte, oldCodAOO, docId, loginUserInfo.getUserId(), relatedChain);

	    // aggiorno i profili se sono cambiati
	    for (String relatedId : relNewProperties.keySet()) {

		// aggiorno il profilo del related
		Map<String, String> newProperties = relNewProperties.get(relatedId);

		// il profilo lo aggiorno solo se e' stato specificato
		provider.updateProfileDocument(relatedId, newProperties);

		EnumTipiComponente relTipoComponente = getEnumTipiComponente(newProperties.get(Constants.doc_tipo_componente));

		if (relTipoComponente.equals(EnumTipiComponente.ALLEGATO) && newACL != null) {
		    provider.setACLDocument(relatedId, newACL);
		}
	    }

	    // advanced versions
	    setAdvancedVersionsDocnumRecord(docId, oldStatoArchivistico, newStatoArchivistico);

	    metadatiRegistrazione.put(Constants.doc_tipo_componente, EnumTipiComponente.PRINCIPALE.toString());
	    metadatiRegistrazione.put(Constants.doc_docnum_princ, "");
	    provider.updateProfileDocument(docId, metadatiRegistrazione);

	}
	catch (DocerException docEx) {

	    throw new DocerException(415, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(515, err.getMessage());
	}
    }

    // 16
    public void fascicolaDocumento(String token, String docId, Map<String, String> metadata) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	metadata = toUCMap(metadata);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (metadata == null)
		throw new DocerException("metadata null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    DataRow<String> oldProfileData = getOldProfile(OPERAZIONE.fascicolazione, docId);
	    doOperationCheck(OPERAZIONE.fascicolazione, docId, loginUserInfo.getUserId(), oldProfileData, metadata);

	    Map<String, String> metadatiFascicolazione = checkAndGetMetadatiFascicolazione(oldProfileData, metadata);

	    checkFascicoloPrincipale(metadatiFascicolazione);

	    checkFascicoliSecondari(metadatiFascicolazione);

	    EnumStatoArchivistico newStatoArchivistico = getEnumStatoArchivistico(metadatiFascicolazione.get(Constants.doc_stato_archivistico));

	    EnumStatiPantarei newStatoPantarei = getEnumStatoPantarei(metadatiFascicolazione.get(Constants.doc_stato_pantarei));

	    // modifico sempre il fascicolo dei related
	    // recupero lista dei related
	    List<String> relatedChain = provider.getRelatedDocuments(docId);

	    // rimuovo dalla lista il MASTER se presente
	    relatedChain.remove(docId);

	    String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
	    String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);

	    // controllo stato_pantarei, TIPO_COMPONENTE e checkoutStatus della
	    // related chain e ritorno le nuove proprieta' dei profili
	    Map<String, Map<String, String>> relNewProperties = checkRecordRelatedChain(newStatoArchivistico, newStatoPantarei, oldCodEnte, oldCodAOO, docId, loginUserInfo.getUserId(), relatedChain);

	    for (String relatedId : relNewProperties.keySet()) {

		// aggiorno il profilo del related
		Map<String, String> newProperties = relNewProperties.get(relatedId);

		// aggiungo i metadati di fascicolo del master
		for (String key : metadatiFascicolazione.keySet()) {

		    // non sovrascrivo lo STATO_PANTAREI
		    if (key.equals(Constants.doc_stato_pantarei)) {
			continue;
		    }

		    newProperties.put(key, metadatiFascicolazione.get(key));
		}

		// il profilo lo aggiorno solo se e' stato specificato
		if (newProperties.size() > 0)
		    provider.updateProfileDocument(relatedId, newProperties);
	    }

	    // advanced versions
	    // EnumStatoArchivistico oldStatoArchivistico =
	    // getEnumStatoArchivistico(
	    // oldProfileData.get(Constants.doc_stato_archivistico));
	    // setAdvancedVersionDocnumRecord(docId, oldStatoArchivistico,
	    // newStatoArchivistico);

	    // aggiorno il master
	    if (newStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
		metadatiFascicolazione.put(Constants.doc_tipo_componente, EnumTipiComponente.PRINCIPALE.toString());
		metadatiFascicolazione.put(Constants.doc_docnum_princ, "");
	    }

	    provider.updateProfileDocument(docId, metadatiFascicolazione);

	}
	catch (DocerException docEx) {

	    throw new DocerException(416, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(516, err.getMessage());
	}
    }

    // 17
    public EnumACLRights getUserRightsAnagrafiche(String token, String type, Map<String, String> id, String userId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	id = toUCMap(id);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (userId == null || userId.equals(VOID))
		throw new DocerException("userId null");

	    if (type == null || type.equals(VOID))
		throw new DocerException("TYPE_ID obbligatorio");

	    type = type.toUpperCase();

	    AnagraficaType anagraficaType = ANAGRAFICHE_TYPES.get(type);
	    if (anagraficaType == null)
		throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + type);

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // test della library

	    id.remove(Constants.anagrafica_type_id);

	    return provider.getEffectiveRightsAnagrafiche(type, id, userId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(417, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(517, err.getMessage());
	}

    }

    // 18
    public EnumACLRights getUserRightsFolder(String token, String folderId, String userId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (folderId == null || folderId.equals(VOID))
		throw new DocerException("folderId null");

	    if (userId == null || userId.equals(VOID))
		throw new DocerException("userId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    return provider.getEffectiveRightsFolder(folderId, userId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(418, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(518, err.getMessage());
	}

    }

    // 20
    public void classificaDocumento(String token, String docId, Map<String, String> metadata) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	metadata = toUCMap(metadata);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (metadata == null)
		throw new DocerException("metadata null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    if (!metadata.containsKey(Constants.doc_classifica)) {
		throw new DocerException("CLASSIFICA e' obbligatorio");
	    }

	    String newClassifica = metadata.get(Constants.doc_classifica);
	    if (newClassifica == null || newClassifica.equals(VOID)) {
		throw new DocerException("non e' possibile assegnare CLASSIFICA null o vuota");
	    }

	    String newCodTitolario = metadata.get(Constants.doc_cod_titolario);
	    if (newCodTitolario != null && !newCodTitolario.equals(VOID)) {
		throw new DocerException("non e' possibile specificare COD_TITOLARIO dall'esterno");
	    }

	    // *** CONTROLLI ***//

	    DataRow<String> oldProfileData = getOldProfile(OPERAZIONE.classificazione, docId);
	    doOperationCheck(OPERAZIONE.classificazione, docId, loginUserInfo.getUserId(), oldProfileData, metadata);

	    String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
	    String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);

	    EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));

	    // controllo e calcolo del nuovo stato archivistico
	    EnumStatoArchivistico newStatoArchivistico = checkStatoArchivisticoClassificaDocumento(oldStatoArchivistico);

	    // controllo del fascicolo
	    String oldProgrFascicolo = oldProfileData.get(Constants.doc_progr_fascicolo);

	    if (oldProgrFascicolo != null && !oldProgrFascicolo.equals(VOID))
		throw new DocerException("classificaDocumento: modifica della classifica: il documento " + docId + " possiede un Fascicolo assegnato");

	    Map<String, String> titolario = getTitolario(oldCodEnte, oldCodAOO, newClassifica);

	    newClassifica = titolario.get(Constants.doc_classifica);
	    newCodTitolario = titolario.get(Constants.doc_cod_titolario);

	    // inseriamo vecchio codEnte e codAOO nel caso non sia stato
	    // specificato
	    HashMap<String, String> metadatiClassificazione = new HashMap<String, String>();
	    metadatiClassificazione.put(Constants.doc_cod_ente, oldCodEnte);
	    metadatiClassificazione.put(Constants.doc_cod_aoo, oldCodAOO);
	    metadatiClassificazione.put(Constants.doc_classifica, newClassifica);
	    metadatiClassificazione.put(Constants.doc_cod_titolario, newCodTitolario);
	    metadatiClassificazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

	    // lo stato_pantarei non cambia nella classificazione
	    EnumStatiPantarei newStatoPantarei = getEnumStatoPantarei(oldProfileData.get(Constants.doc_stato_pantarei));

	    // modifica la classifica dei related
	    List<String> relatedChain = provider.getRelatedDocuments(docId);
	    // rimuovo dalla lista il MASTER se presente
	    relatedChain.remove(docId);

	    // controllo stato_pantarei, DOCNUM_PRINC, TIPO_COMPONENTE e
	    // checkoutStatus
	    // della related chain e ritorno le nuove proprieta' dei profili
	    Map<String, Map<String, String>> relNewProperties = checkRecordRelatedChain(newStatoArchivistico, newStatoPantarei, oldCodEnte, oldCodAOO, docId, loginUserInfo.getUserId(), relatedChain);

	    // aggiorno i profili se sono cambiati
	    for (String relatedId : relNewProperties.keySet()) {

		// aggiorno il profilo del related
		Map<String, String> newProperties = relNewProperties.get(relatedId);

		newProperties.put(Constants.doc_classifica, newClassifica);
		newProperties.put(Constants.doc_cod_titolario, newCodTitolario);

		// aggiorno il profilo
		provider.updateProfileDocument(relatedId, newProperties);
	    }

	    // //advanced versions
	    // setAdvancedVersionDocnumRecord(docId, oldStatoArchivistico,
	    // newStatoArchivistico);

	    // aggiorno il profilo del documento principale
	    if (newStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
		metadatiClassificazione.put(Constants.doc_tipo_componente, EnumTipiComponente.PRINCIPALE.toString());
		metadatiClassificazione.put(Constants.doc_docnum_princ, "");
	    }

	    provider.updateProfileDocument(docId, metadatiClassificazione);

	}
	catch (DocerException docEx) {

	    throw new DocerException(420, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(520, err.getMessage());
	}

    }

    // 21
    public void pubblicaDocumento(String token, String docId, Map<String, String> metadata) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	metadata = toUCMap(metadata);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (metadata == null)
		throw new DocerException("metadata null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    DataRow<String> oldProfileData = getOldProfile(OPERAZIONE.pubblicazione, docId);
	    doOperationCheck(OPERAZIONE.pubblicazione, docId, loginUserInfo.getUserId(), oldProfileData, metadata);

	    Map<String, String> metadatiPubblicazione = checkAndGetMetadatiPubblicazione(oldProfileData, metadata);

	    String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
	    String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);
	    EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));
	    EnumStatoArchivistico newStatoArchivistico = getEnumStatoArchivistico(metadatiPubblicazione.get(Constants.doc_stato_archivistico));

	    EnumStatiPantarei newStatoPantarei = getEnumStatoPantarei(metadatiPubblicazione.get(Constants.doc_stato_pantarei));

	    // modifico i related se ho cambiato stato
	    // recupero lista dei related
	    List<String> relatedChain = provider.getRelatedDocuments(docId);

	    // rimuovo dalla lista il MASTER se presente
	    relatedChain.remove(docId);

	    // controllo stato_pantarei, TIPO_COMPONENTE e checkoutStatus
	    // della related chain e ritorno le nuove proprieta' dei profili
	    Map<String, Map<String, String>> relNewProperties = checkRecordRelatedChain(newStatoArchivistico, newStatoPantarei, oldCodEnte, oldCodAOO, docId, loginUserInfo.getUserId(), relatedChain);

	    // aggiorno i profili se sono cambiati
	    for (String relatedId : relNewProperties.keySet()) {

		// aggiorno il profilo del related
		Map<String, String> newProperties = relNewProperties.get(relatedId);

		// // aggiorno i metadati di pubblicazione
		// for(String key : metadatiPubblicazione.keySet()){
		//
		// // non sovrascrivo lo STATO_PANTAREI
		// if(key.equals(Constants.doc_stato_pantarei)){
		// continue;
		// }
		//
		// newProperties.put(key, metadatiPubblicazione.get(key));
		// }

		// il profilo lo aggiorno solo se e' stato specificato
		provider.updateProfileDocument(relatedId, newProperties);
	    }

	    // //advanced versions
	    // setAdvancedVersionDocnumRecord(docId, oldStatoArchivistico,
	    // newStatoArchivistico);

	    // aggiorno il profilo del documento principale
	    // TIPO_COMPONENTE lo metto solo per sicurezza nel caso sia
	    // UNDEFINED

	    // l'operazione di pubblicazione e' ammessa solo per RECORD quindi i
	    // metadati tipo_componente e docnum_princ sono stati gia' assegnati
	    // ma lo faccio tanto non costa nulla
	    metadatiPubblicazione.put(Constants.doc_tipo_componente, EnumTipiComponente.PRINCIPALE.toString());
	    metadatiPubblicazione.put(Constants.doc_docnum_princ, "");
	    provider.updateProfileDocument(docId, metadatiPubblicazione);

	}
	catch (DocerException docEx) {

	    throw new DocerException(421, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(521, err.getMessage());
	}

    }

    // 22
    public void archiviaDocumento(String token, String docId, Map<String, String> metadata) throws DocerException {

	/*
	 * if(!CONFIG_INITIALIZED){ throw new
	 * DocerException("Configurazione non inizializzata: "
	 * +CONFIG_ERROR_MESSAGE); } metadata = toUCMap(metadata);
	 */

	throw new DocerException("not implemented");

    }

    // 23
    public void addNewAdvancedVersion(String token, String docIdLastVersion, String docIdNewVersion) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docIdLastVersion == null || docIdLastVersion.equals(VOID))
		throw new DocerException("docIdLastVersion void o null");

	    if (docIdNewVersion == null || docIdNewVersion.equals(VOID))
		throw new DocerException("docIdNewVersion void o null");

	    if (docIdLastVersion.equals(docIdNewVersion)) {
		throw new DocerException("docIdLastVersion e docIdNewVersion coincidenti");
	    }

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    List<String> lastAdvancedVersionChain = provider.getAdvancedVersions(docIdLastVersion);

	    // il documento fa parte gia' delle versioni avanzate
	    if (lastAdvancedVersionChain.contains(docIdLastVersion)) {
		return;
	    }

	    if (!lastAdvancedVersionChain.contains(docIdLastVersion)) {
		lastAdvancedVersionChain.add(docIdLastVersion);
	    }

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    // impostazione dei criteri di ricerca
	    List<String> criteria = new ArrayList<String>();
	    criteria.add(docIdLastVersion);
	    criteria.add(docIdNewVersion);

	    searchCriteria.put(Constants.doc_docnum, criteria);

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(Constants.doc_docnum);
	    returnProperties.add(Constants.doc_stato_archivistico);
	    returnProperties.add(Constants.doc_tipo_componente);
	    returnProperties.add(Constants.doc_docnum_record);
	    returnProperties.add(Constants.doc_archive_type);

	    // RICERCA PRIMARIA per verificare i metadati
	    DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    if (searchResults.getRows().size() == 0)
		throw new DocerException("nessun documento non trovato");

	    // verifico profili advanced version
	    // String lastDocnum = "";
	    EnumTipiComponente lastTipoComponente = EnumTipiComponente.UNDEFINED;
	    EnumStatoArchivistico lastStatoArchivistico = EnumStatoArchivistico.undefined;
	    String lastDocnumRecord = "";
	    String lastArchiveType = "";

	    // String newDocnum = "";
	    EnumTipiComponente newTipoComponente = EnumTipiComponente.UNDEFINED;
	    EnumStatoArchivistico newStatoArchivistico = EnumStatoArchivistico.undefined;
	    String newDocnumRecord = "";
	    String newArchiveType = "";

	    // leggo i metadati
	    for (DataRow<String> dataRow : searchResults.getRows()) {

		String docnum = dataRow.get(Constants.doc_docnum);

		criteria.remove(docnum);

		if (docnum.equals(docIdLastVersion)) {

		    // lastDocnum = docnum;
		    lastTipoComponente = getEnumTipiComponente(dataRow.get(Constants.doc_tipo_componente));
		    lastStatoArchivistico = getEnumStatoArchivistico(dataRow.get(Constants.doc_stato_archivistico));
		    lastDocnumRecord = dataRow.get(Constants.doc_docnum_record);
		    lastArchiveType = dataRow.get(Constants.doc_archive_type);
		    if (lastArchiveType == null || lastArchiveType.equals(VOID)) {
			lastArchiveType = ARCHIVE;
		    }

		    continue;
		}

		if (docnum.equals(docIdNewVersion)) {

		    // newDocnum = docnum;
		    newTipoComponente = getEnumTipiComponente(dataRow.get(Constants.doc_tipo_componente));
		    newStatoArchivistico = getEnumStatoArchivistico(dataRow.get(Constants.doc_stato_archivistico));
		    newDocnumRecord = dataRow.get(Constants.doc_docnum_record);
		    newArchiveType = dataRow.get(Constants.doc_archive_type);
		    if (newArchiveType == null || newArchiveType.equals(VOID)) {
			newArchiveType = ARCHIVE;
		    }
		    continue;
		}

	    }

	    if (criteria.size() > 0) {
		throw new DocerException("documento/i " + Arrays.toString(criteria.toArray()) + " non trovato");
	    }

	    if (!lastTipoComponente.equals(EnumTipiComponente.PRINCIPALE)) {
		throw new DocerException("il versioning avanzato e' ammesso solo per documenti con " + Constants.doc_tipo_componente + " PRINCIPALE: valore " + lastTipoComponente);
	    }

	    if (!newTipoComponente.equals(lastTipoComponente)) {

		throw new DocerException("non e' possibile modificare " + Constants.doc_tipo_componente + " nel versioning avanzato: " + lastTipoComponente + " del documento " + docIdLastVersion
			+ " diverso da " + newTipoComponente + " del documento " + docIdNewVersion);
	    }

	    // if (!lastStatoArchivistico.equals(newStatoArchivistico)) {
	    //
	    // throw new
	    // DocerException("le versioni avanzate di un documento non possono avere "
	    // +Constants.doc_stato_archivistico +" diverso: "
	    // +lastStatoArchivistico + " (" + lastStatoArchivistico.getCode() +
	    // ") del documento "+lastDocnum
	    // +" diverso da "
	    // +newStatoArchivistico + " (" + newStatoArchivistico.getCode() +
	    // ") del documento "+newDocnum);
	    // }

	    if (!lastArchiveType.equals(newArchiveType)) {

		throw new DocerException("le versioni avanzate di un documento non possono avere " + Constants.doc_archive_type + " diverso: " + lastArchiveType + " del documento " + docIdLastVersion
			+ " diverso da " + newArchiveType + " del documento " + docIdNewVersion);
	    }

	    if (lastStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {

		// modifica del 28/02/2013 --> i PAPER sono sempre versionabili
		// modifica del 14/01/2013
		if (!lastArchiveType.equals(PAPER)) {

		    if (!allow_record_versioning_archive) {
			throw new DocerException("DOCNUM " + docIdLastVersion + ", attenzione, il documento ha STATO_ARCHIVISTICO " + lastStatoArchivistico + " (" + lastStatoArchivistico.getCode()
				+ ") e ammette versioning avanzato solo se ARCHIVE_TYPE=PAPER");
		    }

		    // qui e' allow_record_versioning_archive = true
		    if (!lastArchiveType.equalsIgnoreCase(ARCHIVE)) {
			throw new DocerException("DOCNUM " + docIdLastVersion + ", attenzione, il documento ha STATO_ARCHIVISTICO " + lastStatoArchivistico + " (" + lastStatoArchivistico.getCode()
				+ ") e ammette versioning avanzato solo se ARCHIVE_TYPE=PAPER oppure ARCHIVE_TYPE=ARCHIVE (o vuoto)");
		    }

		}

	    }

	    if (newStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {

		// modifica del 28/02/2013 --> i PAPER sono sempre versionabili
		// modifica del 14/01/2013
		if (!newArchiveType.equals(PAPER)) {

		    if (!allow_record_versioning_archive) {
			throw new DocerException("DOCNUM " + docIdNewVersion + ", attenzione, il documento ha STATO_ARCHIVISTICO " + newStatoArchivistico + " (" + newStatoArchivistico.getCode()
				+ ") e puo' essere aggiunto alle versioni avanzate solo se ARCHIVE_TYPE=PAPER");
		    }

		    // qui e' allow_record_versioning_archive = true
		    if (!newArchiveType.equalsIgnoreCase(ARCHIVE)) {
			throw new DocerException("DOCNUM " + docIdNewVersion + ", attenzione, il documento ha STATO_ARCHIVISTICO " + newStatoArchivistico + " (" + newStatoArchivistico.getCode()
				+ ") e puo' essere aggiunto alle versioni avanzate solo se ARCHIVE_TYPE=PAPER oppure ARCHIVE_TYPE=ARCHIVE (o vuoto)");
		    }

		}

	    }

	    List<String> newAdvancedVersionChain = provider.getAdvancedVersions(docIdNewVersion);
	    if (!newAdvancedVersionChain.contains(docIdNewVersion)) {
		newAdvancedVersionChain.add(docIdNewVersion);
	    }

	    // ILockStatus checkedOutInfo = null;
	    //
	    // for (String docnum : lastAdvancedVersionChain) {
	    // checkedOutInfo = provider.isCheckedOutDocument(docnum);
	    // if (checkedOutInfo.getLocked()) {
	    //
	    // throw new DocerException("il documento " + docnum +
	    // " della catena delle versioni avanzate di " + docIdLastVersion +
	    // " e' in stato di blocco esclusivo");
	    // }
	    // }
	    //
	    // for (String docnum : newAdvancedVersionChain) {
	    // checkedOutInfo = provider.isCheckedOutDocument(docnum);
	    // if (checkedOutInfo.getLocked()) {
	    //
	    // throw new DocerException("il documento " + docnum +
	    // " della catena delle versioni avanzate di " + docIdNewVersion +
	    // " e' in stato di blocco esclusivo");
	    // }
	    // }

	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put(Constants.doc_docnum_record, docIdNewVersion);

	    // setto il docnum_record per la catena attuale
	    for (String docId : lastAdvancedVersionChain) {
		provider.updateProfileDocument(docId, metadata);
	    }

	    // setto il docnum_record per l'altra catena
	    for (String docId : newAdvancedVersionChain) {
		if (docId.equals(docIdNewVersion)) {
		    continue;
		}
		provider.updateProfileDocument(docId, metadata);
	    }

	    metadata.put(Constants.doc_docnum_record, "");
	    provider.updateProfileDocument(docIdNewVersion, metadata);

	    provider.addNewAdvancedVersion(docIdLastVersion, docIdNewVersion);

	}
	catch (DocerException docEx) {

	    throw new DocerException(423, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(523, err.getMessage());
	}
    }

    // 24
    public List<String> getAdvancedVersions(String token, String docId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (docId == null || docId.equals(VOID))
		throw new DocerException("docId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    List<String> avs = provider.getAdvancedVersions(docId);

	    if (!avs.contains(docId)) {
		avs.add(docId);
	    }

	    return avs;

	}
	catch (DocerException docEx) {

	    throw new DocerException(424, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(524, err.getMessage());
	}
    }

    // 25
    public String createFolder(String token, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    if (info.get(Constants.folder_folder_id) != null)
		throw new DocerException(Constants.folder_folder_id + " non e' specificabile dall'esterno");

	    String codEnte = info.get(Constants.folder_cod_ente);
	    String codAOO = info.get(Constants.folder_cod_aoo);
	    String folderName = info.get(Constants.folder_folder_name);
	    String parentFolderId = info.get(Constants.folder_parent_folder_id);

	    String desFolder = info.get(Constants.folder_des_folder);
	    String owner = info.get(Constants.folder_owner);

	    String ERROR_MESSAGE_CREATE_FOLDER = "Verificare i campi obbligatori (FOLDER_NAME, PARENT_FOLDER_ID)";

	    if (folderName == null || folderName.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_FOLDER);

	    if (parentFolderId == null || parentFolderId.equals(VOID))
		throw new DocerException(ERROR_MESSAGE_CREATE_FOLDER);

	    if (owner == null) {
		owner = VOID;
	    }
	    if (!owner.equals(VOID) && !owner.equals(loginUserInfo.getUserId())) {
		throw new DocerException("non e' ammesso creare una Folder privata impostando " + Constants.folder_owner + " diverso dall'utente autenticato");
	    }

	    Map<String, String> id = new HashMap<String, String>();
	    id.put(it.kdm.docer.sdk.Constants.folder_folder_id, parentFolderId);
	    // cerco la folder parent
	    boolean isRootFolder = false;
	    IFolderInfo parentFolderProfile = getFolderProfile(id);

	    if (parentFolderProfile == null) {
		throw new DocerException("Folder Parent " + parentFolderId + " non trovata");
	    }

	    String parentParentFolderId = parentFolderProfile.getParentFolderId();
	    if (parentParentFolderId == null) {
		parentParentFolderId = "";
	    }

	    String parentOwner = parentFolderProfile.getFolderOwner();
	    if (parentOwner == null) {
		parentOwner = "";
	    }
	    String parentCodEnte = parentFolderProfile.getCodiceEnte();
	    if (parentCodEnte == null) {
		parentCodEnte = "";
	    }
	    String parentCodAOO = parentFolderProfile.getCodiceAOO();
	    if (parentCodAOO == null) {
		parentCodAOO = "";
	    }

	    if (codEnte == null || codEnte.equals(VOID)) {
		codEnte = parentCodEnte;
	    }
	    if (codAOO == null || codAOO.equals(VOID)) {
		codAOO = parentCodAOO;
	    }

	    if (!codEnte.equals(parentCodEnte)) {
		throw new DocerException("non e' ammesso creare una Folder con COD_ENTE diverso dal COD_ENTE della Folder Parent");
	    }
	    if (!codAOO.equals(parentCodAOO)) {
		throw new DocerException("non e' ammesso creare una Folder con COD_AOO diverso dal COD_AOO della Folder Parent");
	    }

	    if (!owner.equals(parentOwner) && !parentParentFolderId.equals(VOID)) {

		if (owner.equals(VOID))
		    throw new DocerException("non e' ammesso creare una Folder privata in una Folder pubblica");

		if (parentOwner.equals(VOID))
		    throw new DocerException("non e' ammesso creare una Folder pubblica in una Folder privata");

		throw new DocerException("non e' ammesso creare Folder nelle Folder private di un altro utente");
	    }

	    IFolderInfo folderProfile = new FolderInfo();
	    folderProfile.setCodiceEnte(codEnte);
	    folderProfile.setCodiceAOO(codAOO);
	    folderProfile.setParentFolderId(parentFolderId);
	    folderProfile.setDescrizione(desFolder);
	    folderProfile.setFolderName(folderName);
	    folderProfile.setFolderOwner(owner);

	    return provider.createFolder(folderProfile);

	}
	catch (DocerException docEx) {

	    throw new DocerException(425, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(525, err.getMessage());
	}
    }

    // 26
    public void updateFolder(String token, String folderId, Map<String, String> info) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	info = toUCMap(info);

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (info == null)
		throw new DocerException("info null");

	    if (folderId == null || folderId.equals(VOID))
		throw new DocerException("folderId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    if (info.get(Constants.folder_folder_id) != null)
		throw new DocerException(Constants.folder_folder_id + " non e' specificabile dall'esterno");

	    String newCodEnte = info.get(Constants.folder_cod_ente);
	    String newCodAoo = info.get(Constants.folder_cod_aoo);
	    String newFolderName = info.get(Constants.folder_folder_name);
	    String newParentFolderId = info.get(Constants.folder_parent_folder_id);
	    String newDesFolder = info.get(Constants.folder_des_folder);
	    String newFolderOwner = info.get(Constants.folder_owner);

	    Map<String, String> idCrit = new HashMap<String, String>();
	    idCrit.put(Constants.folder_folder_id, folderId);
	    IFolderInfo folderInfo = getFolderProfile(idCrit);

	    if (folderInfo == null)
		throw new DocerException("Folder " + folderId + " non trovata");

	    if (newCodEnte != null && !newCodEnte.equalsIgnoreCase(folderInfo.getCodiceEnte())) {
		throw new DocerException("non e' permesso modificare COD_ENTE di una Folder");
	    }
	    if (newCodAoo != null && !newCodAoo.equalsIgnoreCase(folderInfo.getCodiceAOO())) {
		throw new DocerException("non e' permesso modificare COD_AOO di una Folder");
	    }

	    String oldParentFolderId = folderInfo.getParentFolderId();
	    if (oldParentFolderId == null) {
		oldParentFolderId = "";
	    }

	    if (newParentFolderId != null && newParentFolderId.equals(oldParentFolderId)) {
		throw new DocerException("non e' permesso modificare " + Constants.folder_parent_folder_id + " di una Folder");
	    }

	    String oldFolderOwner = folderInfo.getFolderOwner();
	    if (oldFolderOwner == null) {
		oldFolderOwner = "";
	    }

	    if (newFolderOwner != null && !oldFolderOwner.equals(newFolderOwner)) {
		throw new DocerException("non e' permesso modificare " + Constants.folder_owner + " di una Folder");
	    }

	    IFolderInfo folderNewInfo = new FolderInfo();
	    folderNewInfo.setDescrizione(newDesFolder);
	    folderNewInfo.setFolderName(newFolderName);

	    provider.updateFolder(folderId, folderNewInfo);

	}
	catch (DocerException docEx) {

	    throw new DocerException(426, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(526, err.getMessage());
	}

    }

    // 27
    public List<ISearchItem> searchFolders(String token, Map<String, List<String>> searchCriteria, int maxRows, Map<String, EnumSearchOrder> orderby) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	searchCriteria = toUCMapOfList(searchCriteria);

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    if (maxRows < 0)
		maxRows = PRIMARYSEARCH_MAX_ROWS;

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_ente);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_aoo);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_des_folder);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_name);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
	    returnProperties.add("PATH");

	    DataTable<String> dtresults = provider.searchFolders(searchCriteria, returnProperties, maxRows, orderby);

	    List<ISearchItem> results = new ArrayList<ISearchItem>();

	    for (int i = 0; i < dtresults.getRows().size(); i++) {
		DataRow<String> dr = dtresults.getRow(i);
		ISearchItem searchItem = new SearchItem();
		for (String columnName : returnProperties) {

		    String val = dr.get(columnName);
		    if (val == null) {
			val = VOID;
		    }
		    searchItem.put(columnName, val);
		}

		results.add(searchItem);
	    }
	    return results;

	}
	catch (DocerException docEx) {

	    throw new DocerException(427, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(527, err.getMessage());
	}
    }


    // 28
    public void deleteFolder(String token, String folderId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (folderId == null || folderId.equals(VOID))
		throw new DocerException("folderId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // controllo esistenza
	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);

	    DataTable<String> results = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    if (results == null || results.getRows().size() == 0) {
		throw new DocerException("Folder " + folderId + " non trovata");
	    }

	    String parentFolderId = results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
	    if (parentFolderId == null || parentFolderId.equals(VOID)) {
		throw new DocerException("non e' permesso eliminare la Folder root");
	    }

	    String folderOwner = results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_owner);
	    if (folderOwner == null) {
		folderOwner = VOID;
	    }

	    if (!folderOwner.equals(VOID) && !folderOwner.equals(loginUserInfo.getUserId())) {
		throw new DocerException("non e' ammesso eliminare Folder private di un altro utente");
	    }

	    searchCriteria.clear();
	    // impostazione dell'unico criterio di ricerca
	    searchCriteria.put(Constants.folder_parent_folder_id, Arrays.asList(folderId));

	    returnProperties.clear();
	    returnProperties.add(Constants.doc_docnum);

	    // RICERCA DOCUMENTI
	    DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    // deve esserci un solo risultato
	    if (searchResults.getRows().size() > 0)
		throw new DocerException("la Folder " + folderId + " contiene documenti e non e' ammesso eliminarla");

	    returnProperties.clear();
	    returnProperties.add(Constants.folder_folder_id);
	    // RICERCA FOLDERS
	    searchResults = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    // deve esserci un solo risultato
	    if (searchResults.getRows().size() > 0)
		throw new DocerException("la Folder " + folderId + " contiene Folder e non e' ammesso eliminarla");

	    // esecuzione della cancellazione
	    provider.deleteFolder(folderId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(428, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(528, err.getMessage());
	}

    }

    // 29
    public void setACLFolder(String token, String folderId, Map<String, EnumACLRights> acls) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (folderId == null)
		throw new DocerException("folderId null");

	    if (acls == null)
		throw new DocerException("acls null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // controllo esistenza
	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);

	    DataTable<String> results = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    if (results == null || results.getRows().size() == 0) {
		throw new DocerException("Folder " + folderId + " non trovata");
	    }

	    String parentFolderId = results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
	    if (parentFolderId == null || parentFolderId.equals(VOID)) {
		throw new DocerException("non e' permesso modificare le ACL alla Folder root");
	    }

	    boolean isPublic = false;
	    String folderOwner = results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_owner);
	    if (folderOwner == null) {
		folderOwner = VOID;
	    }

	    if (folderOwner.equals(VOID)) {
		isPublic = true;
	    }

	    if (!isPublic && !folderOwner.equals(loginUserInfo.getUserId())) {
		throw new DocerException("non e' ammesso modificare le ACL di una Folder privata di un altro Utente (" + folderOwner + ")");
	    }

	    // for(String key : acls.keySet()){
	    // if(acls.get(key).equals(EnumACLRights.fullAccess)){
	    // throw new DocerException("non e' ammesso assegnare il diritto "
	    // +EnumACLRights.fullAccess +" ("
	    // +EnumACLRights.fullAccess.getCode() +") ad una Folder");
	    // }
	    // }

	    provider.setACLFolder(folderId, acls);

	}
	catch (DocerException docEx) {

	    throw new DocerException(429, docEx.getMessage());

	}
	catch (Exception err) {

	    throw new DocerException(529, err.getMessage());
	}

    }

    // 30
    public Map<String, EnumACLRights> getACLFolder(String token, String folderId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {

	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (folderId == null)
		throw new DocerException("folderId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // // controllo esistenza
	    // Map<String, List<String>> searchCriteria = new HashMap<String,
	    // List<String>>();
	    // searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id,
	    // Arrays.asList(folderId));
	    //
	    // List<String> returnProperties = new ArrayList<String>();
	    // returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
	    // returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
	    // returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
	    //
	    // DataTable<String> results =
	    // provider.searchFolders(searchCriteria, returnProperties,
	    // PRIMARYSEARCH_MAX_ROWS, null);
	    //
	    // if(results==null || results.getRows().size()==0){
	    // throw new DocerException("Folder " +folderId +" non trovata");
	    // }

	    // test della library
	    return provider.getACLFolder(folderId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(430, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(530, err.getMessage());
	}

    }

    // 56
    public List<String> getFolderDocuments(String token, String folderId) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (folderId == null || folderId.equals(VOID))
		throw new DocerException("folderId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // // controllo esistenza
	    // Map<String, List<String>> searchCriteria = new HashMap<String,
	    // List<String>>();
	    // searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id,
	    // Arrays.asList(folderId));
	    //
	    // List<String> returnProperties = new ArrayList<String>();
	    // returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
	    // returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
	    // returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
	    //
	    // DataTable<String> results =
	    // provider.searchFolders(searchCriteria, returnProperties,
	    // PRIMARYSEARCH_MAX_ROWS, null);
	    //
	    // if(results==null || results.getRows().size()==0){
	    // throw new DocerException("Folder " +folderId +" non trovata");
	    // }

	    return provider.getFolderDocuments(folderId);

	}
	catch (DocerException docEx) {

	    throw new DocerException(431, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(531, err.getMessage());
	}
    }

    public void addToFolderDocuments(String token, String folderId, List<String> documents) throws DocerException {

	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (folderId == null || folderId.equals(VOID))
		throw new DocerException("folderId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // controllo esistenza
	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);

	    DataTable<String> results = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    if (results == null || results.getRows().size() == 0) {
		throw new DocerException("Folder " + folderId + " non trovata");
	    }

	    String parentFolderId = results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
	    if (parentFolderId == null || parentFolderId.equals(VOID)) {
		throw new DocerException("non e' permesso aggiungere documenti alla Folder root");
	    }

	    // String folderOwner =
	    // results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_owner);
	    // if(folderOwner==null){
	    // folderOwner = VOID;
	    // }
	    //
	    // if(!folderOwner.equals(VOID) &&
	    // !folderOwner.equals(loginUserInfo.getUserId())){
	    // throw new
	    // DocerException("non e' ammesso aggiungere documenti in Folder private di un altro utente");
	    // }

	    if (documents == null || documents.size() == 0) {
		return;
	    }

	    searchCriteria.clear();
	    // impostazione dell'unico criterio di ricerca
	    searchCriteria.put(Constants.doc_docnum, documents);

	    returnProperties.clear();
	    returnProperties.add(Constants.doc_docnum);
	    returnProperties.add(Constants.doc_stato_archivistico);
	    returnProperties.add(Constants.doc_progr_fascicolo);

	    // RICERCA DOCUMENTI
	    DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    String docnumsFascicolati = "";
	    for (int i = 0; i < searchResults.getRows().size(); i++) {
		DataRow<String> dr = searchResults.getRow(i);

		String docnum = dr.get(Constants.doc_docnum);
		EnumStatoArchivistico statoArchivistico = getEnumStatoArchivistico(dr.get(Constants.doc_stato_archivistico));
		String progrFascicolo = dr.get(Constants.doc_progr_fascicolo);

		// if(statoArchivistico.getCode()>EnumStatoArchivistico.classificato.getCode()){
		// docnumsFascicolati += docnum +", ";
		// }
		if (progrFascicolo != null && !progrFascicolo.equals(VOID)) {
		    docnumsFascicolati += docnum + ", ";
		}
	    }

	    docnumsFascicolati = docnumsFascicolati.replaceAll(", $", "");

	    if (!docnumsFascicolati.equals(VOID)) {
		throw new DocerException("i documenti " + docnumsFascicolati + " possiedono un Fascicolo e non e' ammesso spostarli nelle Folder");
	    }

	    provider.addToFolderDocuments(folderId, documents);

	}
	catch (DocerException docEx) {

	    throw new DocerException(431, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(531, err.getMessage());
	}
    }

    public void removeFromFolderDocuments(String token, String folderId, List<String> documents) throws DocerException {
	if (!CONFIG_INITIALIZED) {
	    throw new DocerException("Configurazione non inizializzata: " + CONFIG_ERROR_MESSAGE);
	}

	try {
	    if (token == null || token.equals(VOID))
		throw new DocerException("token null");

	    if (folderId == null || folderId.equals(VOID))
		throw new DocerException("folderId null");

	    LoggedUserInfo loginUserInfo = parseToken(token);

	    // setto l'utente nel provider
	    provider.setCurrentUser(loginUserInfo);

	    // controllo esistenza
	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
	    returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);

	    DataTable<String> results = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

	    if (results == null || results.getRows().size() == 0) {
		throw new DocerException("Folder " + folderId + " non trovata");
	    }

	    if (documents != null && documents.size() > 0) {
		provider.removeFromFolderDocuments(folderId, documents);
	    }

	}
	catch (DocerException docEx) {

	    throw new DocerException(431, docEx.getMessage());

	}
	catch (Exception err) {
	    throw new DocerException(531, err.getMessage());
	}
    }

}
