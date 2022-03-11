package it.emilia_romagna.fonteraccoglitore.bl.docer.utils;

import it.emilia_romagna.fonteraccoglitore.bl.docer.objects.FileDescriptor;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.TipoAttributoType;
import it.kdm.docer.clients.*;
import it.kdm.docer.clients.AuthenticationServiceStub.Login;
import it.kdm.docer.clients.AuthenticationServiceStub.LoginResponse;
import it.kdm.docer.clients.AuthenticationServiceStub.Logout;
import it.kdm.docer.clients.AuthenticationServiceStub.VerifyToken;
import it.kdm.docer.clients.AuthenticationServiceStub.VerifyTokenResponse;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.clients.DocerServicesStub.CreateUser;
import it.kdm.docer.clients.DocerServicesStub.DownloadDocument;
import it.kdm.docer.clients.DocerServicesStub.DownloadDocumentResponse;
import it.kdm.docer.clients.DocerServicesStub.GetFascicolo;
import it.kdm.docer.clients.DocerServicesStub.GetFascicoloResponse;
import it.kdm.docer.clients.DocerServicesStub.GetGroupsOfUser;
import it.kdm.docer.clients.DocerServicesStub.GetGroupsOfUserResponse;
import it.kdm.docer.clients.DocerServicesStub.GetProfileDocument;
import it.kdm.docer.clients.DocerServicesStub.GetProfileDocumentResponse;
import it.kdm.docer.clients.DocerServicesStub.GetUser;
import it.kdm.docer.clients.DocerServicesStub.GetUserResponse;
import it.kdm.docer.clients.DocerServicesStub.KeyValuePair;
import it.kdm.docer.clients.DocerServicesStub.SearchAnagrafiche;
import it.kdm.docer.clients.DocerServicesStub.SearchAnagraficheResponse;
import it.kdm.docer.clients.DocerServicesStub.SearchDocuments;
import it.kdm.docer.clients.DocerServicesStub.SearchDocumentsResponse;
import it.kdm.docer.clients.DocerServicesStub.SearchGroups;
import it.kdm.docer.clients.DocerServicesStub.SearchGroupsResponse;
import it.kdm.docer.clients.DocerServicesStub.SearchItem;
import it.kdm.docer.clients.DocerServicesStub.SearchUsers;
import it.kdm.docer.clients.DocerServicesStub.SearchUsersResponse;
import it.kdm.docer.clients.DocerServicesStub.SetGroupsOfUser;
import it.kdm.docer.clients.DocerServicesStub.UpdateUser;
import it.kdm.docer.clients.DocerServicesStub.VerifyTicket;
import it.kdm.docer.clients.DocerServicesStub.VerifyTicketResponse;
import it.kdm.docer.commons.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jaxen.JaxenException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FonteDocerUtils {

    private static Integer user_token_expiring_minutes = 15; //
    private static boolean debug = false;
    private static String testUser = "";
    private static Boolean cacheAttachments = null;
    private static File cacheDir;
    private static String fileSizeThreshold = null;
    private static Integer primarysearch_max_results = null;
    private static Integer timeout = null;
    private static Long maxZipFile = null;

    // fonte
    private static String ws_fonte_login_userid = null;
    private static String ws_fonte_login_pwd = null;
    private static String ws_fonte_login_cod_ente = null;
    private static String ws_fonte_url = null;
    private static String ws_fonte_auth_url = null;

    private static DocerServicesStub fonteDocerWSClient;
    private static AuthenticationServiceStub fonteAuthWSClient;

    private static String RACC_UID_FAS_PREFIX = "FAS-";
    private static String RACC_UID_DOC_PREFIX = "DOC-";

    private static String ID_FONTE = "ID_FONTE";
    private static String RACC_UID = "RACC_UID";

    private static String xsltDocumentiPath;
    private static String xsltFascicoliPath;

    private static Source xslDocumento;
    private static Source xslFascicolo;

    // private static Map<String, String> TOKEN_CACHE = new HashMap<String,
    // String>();

    private static TokenCache TOKEN_CACHE = new TokenCache();

    private String readConfigKey(Config config, String xpath) throws Exception {
	OMElement omelement = config.getNode(xpath);
	if (omelement == null) {
	    throw new Exception("chiave " + xpath + " mancante in configurazione");
	}

	return omelement.getText();
    }

    private String readConfigKey(Config config, String xpath, boolean exceptionIfMissing) throws Exception {
	OMElement omelement = config.getNode(xpath);
	if (omelement == null) {
	    if (!exceptionIfMissing) {
		return null;
	    }
	    throw new Exception("chiave " + xpath + " mancante in configurazione");
	}

	return omelement.getText();
    }

    private static String fileConfigBatchDigest = "";

    private String getDigest(byte[] config) {
	return DigestUtils.md5Hex(config);
    }

    // forms configurate nel batch import fonte
    private static Map<String, List<AttributoType>> searchFormsRaccoglitore = new HashMap<String, List<AttributoType>>();

    public FonteDocerUtils() throws RerFonteError {

	try {

	    InputStream in = this.getClass().getResourceAsStream("/configuration.properties");
	    if (in == null) {
		throw new Exception("file /configuration.properties non trovato");
	    }

	    Properties commonprops = new Properties();
	    commonprops.load(in);

	    in.close();

	    if (commonprops.getProperty("cacheAttachments") == null) {
		throw new Exception("variabile cacheAttachments non trovata nel file /configuration.properties");
	    }
	    cacheAttachments = Boolean.valueOf(commonprops.getProperty("cacheAttachments"));

	    if (commonprops.getProperty("cacheDir") == null) {
		throw new Exception("variabile cacheDir non trovata nel file /configuration.properties");
	    }

	    String str = commonprops.getProperty("cacheDir");

	    cacheDir = new File(str);
	    if (!cacheDir.exists()) {
		throw new Exception("Cache Directory " + cacheDir.getAbsolutePath() + " non trovata");
	    }

	    fileSizeThreshold = commonprops.getProperty("fileSizeThreshold");

	    if (fileSizeThreshold == null) {
		throw new Exception("variabile fileSizeThreshold non trovata nel file /configuration.properties");
	    }

	    try {
		Integer.parseInt(fileSizeThreshold);
	    }
	    catch (NumberFormatException nfe) {
		throw new Exception("chiave fileSizeThreshold ha formato errato: " + nfe.getMessage());
	    }

	    if (commonprops.getProperty("primarysearch_max_results") == null) {
		throw new Exception("variabile fileSizeThreshold non trovata nel file /configuration.properties");
	    }

	    try {
		primarysearch_max_results = Integer.parseInt(commonprops.getProperty("primarysearch_max_results"));
	    }
	    catch (NumberFormatException nfe) {
		throw new Exception("chiave ws_primarysearch_max_results ha formato errato: " + nfe.getMessage());
	    }

	    if (commonprops.getProperty("timeout") == null) {
		throw new Exception("variabile timeout non trovata nel file /configuration.properties");
	    }

	    try {
		timeout = Integer.parseInt(commonprops.getProperty("timeout"));
	    }
	    catch (NumberFormatException nfe) {
		throw new Exception("chiave timeout ha formato errato: " + nfe.getMessage());
	    }

	    xslDocumento = new StreamSource(this.getClass().getResourceAsStream("/documenti.xslt"));
	    xslFascicolo = new StreamSource(this.getClass().getResourceAsStream("/fascicoli.xslt"));

	    // try {
	    // maxZipFile =
	    // Long.parseLong(commonprops.getProperty("maxZipFile"));
	    // }
	    // catch (NumberFormatException nfe) {
	    // throw new Exception("chiave maxZipFile ha formato errato: " +
	    // nfe.getMessage());
	    // }

	    String strfileConfigBatch = commonprops.getProperty("batchpopolamento.config.path");
	    if (StringUtils.isEmpty(strfileConfigBatch)) {
		throw new Exception("chiave batchpopolamento.config.path non trovata in configurazione");
	    }
	    // leggo la configurazione dei batch
	    File fileConfigBatch = new File(strfileConfigBatch);
	    if (!fileConfigBatch.exists()) {
		throw new Exception("file di configurazione batchpopolamento.config.path non trovato: " + fileConfigBatch.getAbsolutePath());
	    }

	    byte[] binaryConfig = FileUtils.readFileToByteArray(fileConfigBatch);

	    String newDigest = getDigest(binaryConfig);

	    if (!newDigest.equals(fileConfigBatchDigest)) {

		if (debug) {
		    System.out.println(new Date().toString() +" - "+ this.getClass().getSimpleName() + ": digest modificato -> configurazione inizializzata");
		}

		TOKEN_CACHE.clear();
		Config batchPopolamentoConfig = new Config(fileConfigBatch);

		ws_fonte_login_userid = readConfigKey(batchPopolamentoConfig, "//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_login_userid");
		ws_fonte_login_pwd = readConfigKey(batchPopolamentoConfig, "//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_login_pwd");
		ws_fonte_login_cod_ente = readConfigKey(batchPopolamentoConfig, "//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_login_cod_ente");
		ws_fonte_url = readConfigKey(batchPopolamentoConfig, "//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_url");
		ws_fonte_auth_url = readConfigKey(batchPopolamentoConfig, "//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_auth_url");

		String str_user_token_expiring_minutes = readConfigKey(batchPopolamentoConfig, "//configuration/group[@name='raccoglitore']/section[@name='variables']/user_token_expiring_minutes",
			false);

		try {
		    user_token_expiring_minutes = Integer.valueOf(str_user_token_expiring_minutes);
		    TOKEN_CACHE.setExpiring(user_token_expiring_minutes);
		}
		catch (NumberFormatException nfe) {

		}

		debug = Boolean.valueOf(readConfigKey(batchPopolamentoConfig, "//configuration/group[@name='raccoglitore']/section[@name='variables']/debug", false));

		testUser = readConfigKey(batchPopolamentoConfig, "//configuration/group[@name='raccoglitore']/section[@name='variables']/testUser", false);

		setSimpleSearchFieldsMapping(batchPopolamentoConfig);

		populateRaccoglitoreSearchForms(batchPopolamentoConfig);

		fonteAuthWSClient = getAuthenticationClient(ws_fonte_auth_url);

		fonteDocerWSClient = getDocerServicesClient(ws_fonte_url);

		fileConfigBatchDigest = newDigest;
	    }

	}
	catch (Exception e) {
	    throw new RerFonteError(e);
	}

    }

    private void populateRaccoglitoreSearchForms(Config batchPopolamentoConfig) throws JaxenException {

	searchFormsRaccoglitore.clear();
	DEFAULT_FORM = null;

	List<AttributoType> commonListaAttributi = new ArrayList<AttributoType>();

	List<OMElement> elements = batchPopolamentoConfig.getNodes("//configuration/group[@name='raccoglitore']/section[@name='search_forms_raccoglitore']/form[@id_fonte='*']/*");
	if (elements != null) {

	}
	for (OMElement propElement : elements) {

	    if (propElement == null)
		continue;

	    AttributoType commonAttributoType = getAttributo(propElement);

	    boolean add = true;
	    for (AttributoType commonAttributotype : commonListaAttributi) {
		if (commonAttributotype.getCodice().equalsIgnoreCase(commonAttributoType.getCodice())) {
		    add = false;
		    break;
		}
	    }

	    if (!add) {
		continue;
	    }

	    commonListaAttributi.add(commonAttributoType);
	}

	if (commonListaAttributi.size() > 0) {
	    searchFormsRaccoglitore.put("*", commonListaAttributi);
	    DEFAULT_FORM = commonListaAttributi;
	}

	elements = batchPopolamentoConfig.getNodes("//configuration/group[@name='raccoglitore']/section[@name='search_forms_raccoglitore']/form");
	for (OMElement formElem : elements) {

	    if (formElem == null)
		continue;

	    if (formElem.getAttribute(new QName("id_fonte")) == null) {
		continue;
	    }

	    String id_fonte = formElem.getAttribute(new QName("id_fonte")).getAttributeValue();

	    if (id_fonte.equals("*")) {
		continue;
	    }

	    Iterator<OMElement> propIterator = formElem.getChildElements();
	    if (propIterator == null) {
		continue;
	    }

	    commonListaAttributi = searchFormsRaccoglitore.get("*");

	    List<AttributoType> listaAttributi = new ArrayList<AttributoType>();

	    if (commonListaAttributi != null) {
		listaAttributi.addAll(commonListaAttributi);
	    }

	    while (propIterator.hasNext()) {

		OMElement propElem = propIterator.next();

		if (propElem == null) {
		    break;
		}

		AttributoType attributoType = new AttributoType();
		attributoType.setCodice(propElem.getLocalName());
		attributoType.setObbligatorio(false);
		attributoType.setTipoAttributo(TipoAttributoType.TESTO);

		OMAttribute omAtt = propElem.getAttribute(new QName("descrizione"));
		if (omAtt != null) {
		    attributoType.setDescrizione(omAtt.getAttributeValue());
		}

		omAtt = propElem.getAttribute(new QName("descrizioneLunga"));
		if (omAtt != null) {
		    attributoType.setDescrizioneLunga(omAtt.getAttributeValue());
		}

		omAtt = propElem.getAttribute(new QName("tipoAttributo"));
		if (omAtt != null) {

		    TipoAttributoType tipoAttributo = TipoAttributoType.TESTO;

		    String strAttr = omAtt.getAttributeValue();
		    if (strAttr.equalsIgnoreCase("DATA")) {
			tipoAttributo = TipoAttributoType.DATA;
		    }
		    else if (strAttr.equalsIgnoreCase("DATARANGE")) {
			tipoAttributo = TipoAttributoType.DATARANGE;
		    }
		    else if (strAttr.equalsIgnoreCase("NUMERO")) {
			tipoAttributo = TipoAttributoType.NUMERO;
		    }
		    else if (strAttr.equalsIgnoreCase("DOMINIO")) {
			tipoAttributo = TipoAttributoType.DOMINIO;
		    }
		    else if (strAttr.equalsIgnoreCase("BOOLEAN")) {
			tipoAttributo = TipoAttributoType.BOOLEAN;
		    }
		    attributoType.setTipoAttributo(tipoAttributo);
		}

		omAtt = propElem.getAttribute(new QName("infoAttributo"));
		if (omAtt != null) {
		    attributoType.setInfoAttributo(omAtt.getAttributeValue());
		}

		omAtt = propElem.getAttribute(new QName("obbligatorio"));
		if (omAtt != null) {
		    attributoType.setObbligatorio(Boolean.valueOf(omAtt.getAttributeValue()));
		}

		omAtt = propElem.getAttribute(new QName("visibilita"));
		if (omAtt != null) {
		    attributoType.setVisibilita(omAtt.getAttributeValue());
		}

		AttributoType atToRemove = null;
		for (AttributoType commonAttributotype : listaAttributi) {
		    if (commonAttributotype.getCodice().equalsIgnoreCase(attributoType.getCodice())) {
			atToRemove = commonAttributotype;
			break;
		    }
		}

		if (atToRemove != null) {
		    listaAttributi.remove(atToRemove);
		}

		listaAttributi.add(attributoType);
	    }

	    searchFormsRaccoglitore.put(id_fonte.toUpperCase(), listaAttributi);

	}

	if (debug) {
	    System.out.println(new Date().toString() +" - "+ "configurati " + searchFormsRaccoglitore.size() + " form: ");
	    for (String key : searchFormsRaccoglitore.keySet()) {
		System.out.println(new Date().toString() +" - "+ "<form id_fonte=\"" + key + "\">");
	    }
	}

    }

    private AttributoType getAttributo(OMElement propElem) {

	if (propElem == null) {
	    return null;
	}

	AttributoType attributoType = new AttributoType();
	attributoType.setCodice(propElem.getLocalName());
	attributoType.setObbligatorio(false);
	attributoType.setTipoAttributo(TipoAttributoType.TESTO);

	OMAttribute omAtt = propElem.getAttribute(new QName("descrizione"));
	if (omAtt != null) {
	    attributoType.setDescrizione(omAtt.getAttributeValue());
	}

	omAtt = propElem.getAttribute(new QName("descrizioneLunga"));
	if (omAtt != null) {
	    attributoType.setDescrizioneLunga(omAtt.getAttributeValue());
	}

	omAtt = propElem.getAttribute(new QName("tipoAttributo"));
	if (omAtt != null) {

	    TipoAttributoType tipoAttributo = TipoAttributoType.TESTO;

	    String strAttr = omAtt.getAttributeValue();
	    if (strAttr.equalsIgnoreCase("DATA")) {
		tipoAttributo = TipoAttributoType.DATA;
	    }
	    else if (strAttr.equalsIgnoreCase("DATARANGE")) {
		tipoAttributo = TipoAttributoType.DATARANGE;
	    }
	    else if (strAttr.equalsIgnoreCase("NUMERO")) {
		tipoAttributo = TipoAttributoType.NUMERO;
	    }
	    else if (strAttr.equalsIgnoreCase("DOMINIO")) {
		tipoAttributo = TipoAttributoType.DOMINIO;
	    }
	    else if (strAttr.equalsIgnoreCase("BOOLEAN")) {
		tipoAttributo = TipoAttributoType.BOOLEAN;
	    }
	    attributoType.setTipoAttributo(tipoAttributo);
	}

	omAtt = propElem.getAttribute(new QName("infoAttributo"));
	if (omAtt != null) {
	    attributoType.setInfoAttributo(omAtt.getAttributeValue());
	}

	omAtt = propElem.getAttribute(new QName("obbligatorio"));
	if (omAtt != null) {
	    attributoType.setObbligatorio(Boolean.valueOf(omAtt.getAttributeValue()));
	}

	omAtt = propElem.getAttribute(new QName("visibilita"));
	if (omAtt != null) {
	    attributoType.setVisibilita(omAtt.getAttributeValue());
	}

	return attributoType;

    }

    // public List<AttributoType> getSearchFormRaccoglitore(String id_fonte){
    //
    // if(id_fonte==null){
    // return null;
    // }
    // return searchFormsRaccoglitore.get(id_fonte.toUpperCase());
    // }

    public boolean getDebug() {
	return debug;
    }

    public String getTestUser() {
	return testUser;
    }

    public void loginWSFonte(String userId, List<String> gruppiDelega) throws RerFonteError {

	userId = userId.toLowerCase();

	String adminToken = TOKEN_CACHE.get(ws_fonte_login_userid);

	if (!verifyToken(adminToken)) {
	    adminToken = _loginWSFonte(ws_fonte_login_userid);

	    TOKEN_CACHE.put(ws_fonte_login_userid, adminToken);
	    if (debug) {
		System.out.println(new Date().toString() +" - "+ "aggiunto token alla cache: utente " + ws_fonte_login_userid + " -> " + user_token_expiring_minutes + " minuti");
	    }
	}

	// recupero token dell'utente chiamante
	String userToken = TOKEN_CACHE.get(userId);

	// se e' gia' autenticato non rifaccio login e associazione deleghe (si
	// ipotizza che le deleghe non cambino in una giornata o almeno fino alla scadenza della cache)
	if (!userId.equals(ws_fonte_login_userid)) {
	    if (verifyToken(userToken)) {
		return;
	    }
	}

	try {

	    // se non c'e' il token l'utente non si e' mai loggato (potrebbe
	    // anche non esistere)
	    GetUser getUser = new GetUser();
	    getUser.setToken(adminToken);
	    getUser.setUserId(userId);

	    GetUserResponse getUserResponse = fonteDocerWSClient.getUser(getUser);

	    if (getUserResponse == null || getUserResponse.get_return() == null || getUserResponse.get_return().length == 0) {

		CreateUser createUser = new CreateUser();
		createUser.setToken(adminToken);

		KeyValuePair kvp1 = new KeyValuePair();
		kvp1.setKey("USER_ID");
		kvp1.setValue(userId.toLowerCase());

		KeyValuePair kvp2 = new KeyValuePair();
		kvp2.setKey("FULL_NAME");
		kvp2.setValue(userId);

		KeyValuePair kvp3 = new KeyValuePair();
		kvp3.setKey("USER_PASSWORD");
		kvp3.setValue(userId);

		KeyValuePair kvp4 = new KeyValuePair();
		kvp4.setKey("FIRST_NAME");
		kvp4.setValue(userId);

		KeyValuePair kvp5 = new KeyValuePair();
		kvp5.setKey("LAST_NAME");
		kvp5.setValue(userId);

		createUser.setUserInfo(new KeyValuePair[] { kvp1, kvp2, kvp3, kvp4, kvp5 });

		fonteDocerWSClient.createUser(createUser);
	    }

	    GetGroupsOfUser getGroupsOfUser = new GetGroupsOfUser();
	    getGroupsOfUser.setToken(adminToken);
	    getGroupsOfUser.setUserId(userId);
	    GetGroupsOfUserResponse getGroupsOfUserResponse = fonteDocerWSClient.getGroupsOfUser(getGroupsOfUser);

	    String[] currentUserGroups = getGroupsOfUserResponse.get_return();

	    // String relazione_group_prefix = "RLZN_";

	    SearchGroups searchGroups = new SearchGroups();
	    searchGroups.setToken(adminToken);

	    for (String group_id : gruppiDelega) {
		KeyValuePair kvpG = new KeyValuePair();
		kvpG.setKey("GROUP_ID");
		kvpG.setValue(group_id);
		searchGroups.addSearchCriteria(kvpG);
	    }

	    // ricerco i gruppi delega
	    SearchGroupsResponse searchGroupsResponse = fonteDocerWSClient.searchGroups(searchGroups);

	    List<String> gruppiDaAssociare = new ArrayList<String>();
	    SetGroupsOfUser setGroupsOfUser = new SetGroupsOfUser();
	    setGroupsOfUser.setToken(adminToken);
	    setGroupsOfUser.setUserId(userId);

	    // associo l'utente ai soli gruppi delega esistenti
	    if (searchGroupsResponse != null && searchGroupsResponse.get_return() != null) {

		for (SearchItem si : searchGroupsResponse.get_return()) {

		    for (KeyValuePair kvpS : si.getMetadata()) {
			if (kvpS.getKey().equalsIgnoreCase("GROUP_ID")) {
			    gruppiDaAssociare.add(kvpS.getValue());
			    break;
			}
		    }

		}
	    }

	    // inserisco sempre l'utente tra i SYS_ADMINS di Docer cosi' non e
	    // obbligatorio specificare l'ente in fase di login
	    gruppiDaAssociare.add("SYS_ADMINS");

	    if (currentUserGroups != null) {
		for (String currentGid : currentUserGroups) {
		    if (currentGid.toUpperCase().startsWith(relazione_group_prefix)) {
			continue;
		    }

		    gruppiDaAssociare.add(currentGid);
		}
	    }

	    setGroupsOfUser.setGroups(gruppiDaAssociare.toArray(new String[0]));

	    fonteDocerWSClient.setGroupsOfUser(setGroupsOfUser);

	    if (debug) {
		System.out.println(new Date().toString() +" - "+ "utente " +setGroupsOfUser.getUserId() + " associato ai gruppi " +gruppiDaAssociare.toString());
	    }
	    
	    // ora eseguo il login dell'utente su Fonte Docer
	    userToken = _loginWSFonte(userId);

	    TOKEN_CACHE.put(userId, userToken);
	    if (debug) {
		System.out.println(new Date().toString() +" - "+ "aggiunto token alla cache: utente " + userId + " -> " + user_token_expiring_minutes + " minuti");
	    }

	}
	catch (RemoteException e) {
	    if (debug) {
		e.printStackTrace();
		saveStringToFile(e.getMessage(), "loginWSFonte.txt");
	    }
	    throw new RerFonteError("fonteDocerWSClient.searchUsers: " + e.getMessage(), e);
	}
	catch (DocerExceptionException e) {
	    if (debug) {
		e.printStackTrace();
		saveStringToFile(e.getMessage(), "loginWSFonte.txt");
	    }
	    throw new RerFonteError("fonteDocerWSClient.searchUsers: " + e.getMessage(), e);
	}

    }

    public List<AttributoType> getAttributiRicerca(String id_fonte) {

	if (id_fonte != null) {
	    List<AttributoType> lista = searchFormsRaccoglitore.get(id_fonte.toUpperCase());
	    if (lista != null) {
		return lista;
	    }
	}

	return getDEFAULTFORM();

    }

    public FileDescriptor getEntitaInformativa(String userId, String uid) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {

	userId = userId.toLowerCase();

	String userToken = TOKEN_CACHE.get(userId);

	FileDescriptor fd = new FileDescriptor();

	if (!uid.startsWith(RACC_UID_DOC_PREFIX)) {
	    if (debug) {
		saveStringToFile("!uid.startsWith(RACC_UID_DOC_PREFIX) -> uid=" + uid, "getEntitaInformativa.txt");
	    }
	    throw new ObjectNotFoundError();
	}

	try {

	    String filename = getFileName(userToken, uid);

	    fd.setFileName(filename);

	    DataHandler dh = downloadDocument(userToken, uid);

	    fd.setIn(dh.getDataSource().getInputStream());

	    return fd;

	}
	catch (IOException e) {
	    if (debug) {
		saveStringToFile(e.getMessage(), "getEntitaInformativa.txt");
	    }
	    throw new RerFonteError(e);
	}

    }

    public String getMetadatiEntitaInformativa(String userId, String uid) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {

	userId = userId.toLowerCase();

	if (!uid.startsWith(RACC_UID_DOC_PREFIX) && !uid.startsWith(RACC_UID_FAS_PREFIX)) {
	    if (debug) {
		saveStringToFile("!uid.startsWith(RACC_UID_DOC_PREFIX) && !uid.startsWith(RACC_UID_FAS_PREFIX) -> uid=" + uid, "getMetadatiEntitaInformativa.txt");
	    }

	    throw new ObjectNotFoundError();
	}

	String userToken = TOKEN_CACHE.get(userId);

	if (uid.startsWith(RACC_UID_FAS_PREFIX)) {
	    GetFascicolo getFascicolo = new GetFascicolo();
	    getFascicolo.setToken(userToken);

	    KeyValuePair[] params = new KeyValuePair[1];
	    KeyValuePair kvp = new KeyValuePair();
	    kvp.setKey("RACC_UID");
	    kvp.setValue(uid);
	    params[0] = kvp;

	    getFascicolo.setFascicoloId(params);

	    GetFascicoloResponse getFascicoloResponse;
	    try {
		getFascicoloResponse = fonteDocerWSClient.getFascicolo(getFascicolo);
	    }
	    catch (RemoteException e) {

		if (debug) {
		    saveStringToFile("fonteDocerWSClient.getFascicolo -> uid=" + uid + ": " + e.getMessage(), "getMetadatiEntitaInformativa.txt");
		}

		if (e.getMessage().matches(".*Fascicolo.*non trovato.*")) {
		    throw new ObjectNotFoundError("Fascicolo " + uid + " non trovato", e);
		}
		throw new RerFonteError("getFascicolo", e);
	    }
	    catch (DocerExceptionException e) {
		if (debug) {
		    saveStringToFile("fonteDocerWSClient.getFascicolo -> uid=" + uid + ": " + e.getMessage(), "getMetadatiEntitaInformativa.txt");
		}
		if (e.getMessage().matches(".*Fascicolo.*non trovato.*")) {
		    throw new ObjectNotFoundError("Fascicolo " + uid + " non trovato", e);
		}
		throw new RerFonteError("getFascicolo", e);
	    }

	    if (getFascicoloResponse == null || getFascicoloResponse.get_return() == null || getFascicoloResponse.get_return().length == 0) {
		if (debug) {
		    saveStringToFile("fonteDocerWSClient.getFascicolo -> uid=" + uid + ": getFascicoloResponse null or void", "getMetadatiEntitaInformativa.txt");
		}
		throw new ObjectNotFoundError();
	    }

	    Document dom = buildXmlMetadata(getFascicoloResponse.get_return(), false);

	    return getStringFromDocument(dom, xslFascicolo);

	}

	// qui e' uid.startsWith(RACC_UID_DOC_PREFIX)

	GetProfileDocument getProfileDocument = new GetProfileDocument();
	getProfileDocument.setToken(userToken);
	getProfileDocument.setDocId(uid);

	GetProfileDocumentResponse getProfileDocumentResponse;
	try {
	    getProfileDocumentResponse = fonteDocerWSClient.getProfileDocument(getProfileDocument);
	}
	catch (RemoteException e) {
	    if (debug) {
		saveStringToFile("fonteDocerWSClient.getProfileDocument -> uid=" + uid + ": " + e.getMessage(), "getMetadatiEntitaInformativa.txt");
	    }
	    if (e.getMessage().matches(".*documento.*non trovato.*")) {
		throw new ObjectNotFoundError("Documento " + uid + " non trovato", e);
	    }
	    throw new RerFonteError("getProfileDocument", e);
	}
	catch (DocerExceptionException e) {
	    if (debug) {
		saveStringToFile("fonteDocerWSClient.getProfileDocument -> uid=" + uid + ": " + e.getMessage(), "getMetadatiEntitaInformativa.txt");
	    }
	    if (e.getMessage().matches(".*documento.*non trovato.*")) {
		throw new ObjectNotFoundError("Documento " + uid + " non trovato", e);
	    }
	    throw new RerFonteError("getProfileDocument", e);
	}

	if (getProfileDocumentResponse == null || getProfileDocumentResponse.get_return() == null || getProfileDocumentResponse.get_return().length == 0) {
	    if (debug) {
		saveStringToFile("fonteDocerWSClient.getProfileDocument -> uid=" + uid + ": getProfileDocumentResponse null or void", "getMetadatiEntitaInformativa.txt");
	    }
	    throw new ObjectNotFoundError();
	}

	Document dom = buildXmlMetadata(getProfileDocumentResponse.get_return(), true);

	return getStringFromDocument(dom, xslDocumento);

    }

    private static String testo_fascicolo_fieldname = "DES_FASCICOLO";
    private static String data_fascicolo_fieldname = "DATA_APERTURA";
    private static String numeroRegistrazione_fascicolo_fieldname = "PROGR_FASCICOLO";

    private static String testo_documento_fieldname = "TEXT";
    private static String data_documento_fieldname = "DATA_PG";
    private static String numeroRegistrazione_documento_fieldname = "NUM_PG";

    private void setSimpleSearchFieldsMapping(Config batchPopolamentoConfig) {

	List<OMElement> elements;
	try {
	    elements = batchPopolamentoConfig.getNodes("//configuration/group[@name='raccoglitore']/section[@name='search_forms_raccoglitore']/simple_search/fascicolo/*");

	    if (debug) {
		if (elements == null) {
		    System.out.println(new Date().toString() +" - "+ "//configuration/group[@name='raccoglitore']/section[@name='search_forms_raccoglitore']/simple_search/fascicolo/* is null");
		}
		else {
		    System.out.println(new Date().toString() +" - "+ "//configuration/group[@name='raccoglitore']/section[@name='search_forms_raccoglitore']/simple_search/fascicolo/* trovati " + elements.size() + " elementi");
		}

	    }

	    for (OMElement formElem : elements) {

		if (formElem == null) {
		    continue;
		}

		String nodeName = formElem.getLocalName();

		if (StringUtils.isEmpty(nodeName)) {
		    continue;
		}

		if (nodeName.equalsIgnoreCase("data")) {
		    data_fascicolo_fieldname = formElem.getText();
		    continue;
		}
		if (nodeName.equalsIgnoreCase("testo")) {
		    testo_fascicolo_fieldname = formElem.getText();
		    continue;
		}
		if (nodeName.equalsIgnoreCase("numero")) {
		    numeroRegistrazione_fascicolo_fieldname = formElem.getText();
		    continue;
		}
	    }

	    if (debug) {
		System.out.println(new Date().toString() +" - "+ "setSimpleSearchFieldsMapping fascicolo: data_fascicolo_fieldname=" + data_fascicolo_fieldname + ", testo_fascicolo_fieldname=" + testo_fascicolo_fieldname
			+ ", numeroRegistrazione_fascicolo_fieldname=" + numeroRegistrazione_fascicolo_fieldname);
	    }

	    elements = batchPopolamentoConfig.getNodes("//configuration/group[@name='raccoglitore']/section[@name='search_forms_raccoglitore']/simple_search/documento/*");

	    if (debug) {
		if (elements == null) {
		    System.out.println(new Date().toString() +" - "+ "//configuration/group[@name='raccoglitore']/section[@name='search_forms_raccoglitore']/simple_search/documento/* is null");
		}
		else {
		    System.out.println(new Date().toString() +" - "+ "//configuration/group[@name='raccoglitore']/section[@name='search_forms_raccoglitore']/simple_search/documento/* trovati " + elements.size() + " elementi");
		}

	    }

	    for (OMElement formElem : elements) {

		if (formElem == null) {
		    continue;
		}

		String nodeName = formElem.getLocalName();

		if (StringUtils.isEmpty(nodeName)) {
		    continue;
		}

		if (nodeName.equalsIgnoreCase("data")) {
		    data_documento_fieldname = formElem.getText();
		    continue;
		}
		if (nodeName.equalsIgnoreCase("testo")) {
		    testo_documento_fieldname = formElem.getText();
		    continue;
		}
		if (nodeName.equalsIgnoreCase("numero")) {
		    numeroRegistrazione_documento_fieldname = formElem.getText();
		    continue;
		}
	    }

	    if (debug) {
		System.out.println(new Date().toString() +" - "+ "setSimpleSearchFieldsMapping documento: data_documento_fieldname=" + data_documento_fieldname + ", testo_documento_fieldname=" + testo_documento_fieldname
			+ ", numeroRegistrazione_documento_fieldname=" + numeroRegistrazione_documento_fieldname);
	    }
	}
	catch (JaxenException e) {
	    e.printStackTrace();
	}
    }


    public SearchEntitaInformativaResponse searchEntitaInformativa(String userId, String idFonte, String minDate, String maxDate, String testo, String numeroRegistrazione) throws RerFonteError {

	SearchEntitaInformativaResponse searchEntitaInformativaResponse = new SearchEntitaInformativaResponse();

	Map<String, String> documentoSearchCriteria = new HashMap<String, String>();
	Map<String, String> fascicoloSearchCriteria = new HashMap<String, String>();

	if (StringUtils.isNotEmpty(testo)) {

	    if (StringUtils.isNotEmpty(testo_fascicolo_fieldname)) {
		fascicoloSearchCriteria.put(testo_fascicolo_fieldname, testo);
	    }

	    if (StringUtils.isNotEmpty(testo_documento_fieldname)) {
		documentoSearchCriteria.put(testo_documento_fieldname, testo);
	    }
	}

	if (StringUtils.isNotEmpty(numeroRegistrazione)) {

	    if (StringUtils.isNotEmpty(numeroRegistrazione_fascicolo_fieldname)) {
		fascicoloSearchCriteria.put(numeroRegistrazione_fascicolo_fieldname, numeroRegistrazione);
	    }

	    if (StringUtils.isNotEmpty(numeroRegistrazione_documento_fieldname)) {
		documentoSearchCriteria.put(numeroRegistrazione_documento_fieldname, numeroRegistrazione);
	    }

	}

	boolean addDataCriteria = false;
	String min = "MIN";
	String max = "MAX";

	if (StringUtils.isNotEmpty(minDate)) {
	    min = minDate;
	    addDataCriteria = true;
	}

	if (StringUtils.isNotEmpty(maxDate)) {
	    max = maxDate;
	    addDataCriteria = true;
	}

	if (addDataCriteria) {

	    String val = "[" + min + " TO " + max + "]";
	    if (StringUtils.isNotEmpty(data_fascicolo_fieldname)) {
		fascicoloSearchCriteria.put(data_fascicolo_fieldname, val);
	    }

	    if (StringUtils.isNotEmpty(data_documento_fieldname)) {
		documentoSearchCriteria.put(data_documento_fieldname, val);
	    }
	}

	int totalCount = 0;
	float ranking = 0;

	// ricerco fascicoli
	long start1 = new Date().getTime();
	SearchEntitaInformativaAvanzataResponse searchEntitaInformativaAvanzataResponse1 = searchEntitaInformativaAvanzata(userId, idFonte, "Fascicolo", fascicoloSearchCriteria);
	long end1 = new Date().getTime();
	long queryTime1 = end1 - start1;
	totalCount += searchEntitaInformativaAvanzataResponse1.getNumFound();
	ranking = searchEntitaInformativaAvanzataResponse1.getMaxScore();

	// ricerco documenti
	long start2 = new Date().getTime();
	SearchEntitaInformativaAvanzataResponse searchEntitaInformativaAvanzataResponse2 = searchEntitaInformativaAvanzata(userId, idFonte, "Documento", documentoSearchCriteria);
	long end2 = new Date().getTime();
	long queryTime2 = end2 - start2;
	totalCount += searchEntitaInformativaAvanzataResponse2.getNumFound();
	if (searchEntitaInformativaAvanzataResponse2.getMaxScore() > ranking) {
	    ranking = searchEntitaInformativaAvanzataResponse2.getMaxScore();
	}

	searchEntitaInformativaResponse.setQueryTime((int) Math.ceil((queryTime1 + queryTime2) / 1000.0));
	searchEntitaInformativaResponse.setNumFound(totalCount);
	searchEntitaInformativaResponse.setMaxScore(ranking);
	searchEntitaInformativaResponse.setId((String[]) ArrayUtils.addAll(searchEntitaInformativaAvanzataResponse1.getId(), searchEntitaInformativaAvanzataResponse2.getId()));
	return searchEntitaInformativaResponse;

    }

    public SearchEntitaInformativaAvanzataResponse searchEntitaInformativaAvanzata(String userId, String idFonte, String tipo, Map<String, String> searchCriteria) throws RerFonteError {

	SearchEntitaInformativaAvanzataResponse searchEntitaInformativaAvanzataResponse = new SearchEntitaInformativaAvanzataResponse();

	userId = userId.toLowerCase();

	String userToken = TOKEN_CACHE.get(userId);

	List<DocerServicesStub.KeyValuePair> documentoCriteria = new ArrayList<DocerServicesStub.KeyValuePair>();
	KeyValuePair kvpTC = new KeyValuePair();
	kvpTC.setKey("TIPO_COMPONENTE");
	kvpTC.setValue("PRINCIPALE");
	documentoCriteria.add(kvpTC);

	List<DocerServicesStub.KeyValuePair> fascicoloCriteria = new ArrayList<DocerServicesStub.KeyValuePair>();

	if (StringUtils.isNotEmpty(idFonte)) {
	    KeyValuePair kvpIdFonte = new KeyValuePair();
	    kvpIdFonte.setKey("ID_FONTE");
	    kvpIdFonte.setValue(idFonte);

	    documentoCriteria.add(kvpIdFonte);
	    fascicoloCriteria.add(kvpIdFonte);
	}

	List<String> ids = new ArrayList<String>();

	try {

	    long queryTime1 = 0;
	    long queryTime2 = 0;
	    float ranking = 0;
	    int totalCount = 0;

	    if (tipo.equalsIgnoreCase("documento")) {

		SearchDocuments searchDocuments = new SearchDocuments();
		searchDocuments.setToken(userToken);
		searchDocuments.setMaxRows(primarysearch_max_results);

		for (String key : searchCriteria.keySet()) {

		    if (key.equalsIgnoreCase("TEXT")) {
			searchDocuments.addKeywords(searchCriteria.get(key));
			continue;
		    }

		    KeyValuePair kvp1 = new KeyValuePair();
		    kvp1.setKey(key.toUpperCase());
		    kvp1.setValue(searchCriteria.get(key));
		    documentoCriteria.add(kvp1);

		}

		searchDocuments.setSearchCriteria(documentoCriteria.toArray(new KeyValuePair[0]));

		if (searchDocuments.getKeywords() == null) {
		    searchDocuments.setKeywords(new String[] { "" });
		}

		if (debug) {
		    System.out.println(new Date().toString() +" - "+ "ricerca su Docer: tipo=" + tipo + ", keywords: " + Arrays.asList(searchDocuments.getKeywords()).toString() + ", searchCriteria: "
			    + toMap(searchDocuments.getSearchCriteria()).toString());
		}

		long start1 = new Date().getTime();
		SearchDocumentsResponse searchDocumentsResponse = fonteDocerWSClient.searchDocuments(searchDocuments);
		long end1 = new Date().getTime();
		queryTime1 = end1 - start1;

		if (searchDocumentsResponse != null && searchDocumentsResponse.get_return() != null) {
		    for (SearchItem si : searchDocumentsResponse.get_return()) {

			boolean found1 = false;
			boolean found2 = false;
			for (KeyValuePair kvp : si.getMetadata()) {
			    if (found1 && found2) {
				break;
			    }
			    if (kvp.getKey().equalsIgnoreCase("RANKING")) {
				found1 = true;
				try {
				    float thisRanking = Float.parseFloat(kvp.getValue());
				    if (thisRanking > ranking) {
					ranking = thisRanking;
				    }
				}
				catch (NumberFormatException nfe) {

				}
				continue;
			    }
			    if (kvp.getKey().equalsIgnoreCase(RACC_UID)) {
				found2 = true;
				if (!ids.contains(kvp.getValue())) {
				    ids.add(kvp.getValue());
				    totalCount++;
				}
				continue;
			    }
			}

		    }
		}

	    }

	    if (tipo.equalsIgnoreCase("fascicolo")) {

		SearchAnagrafiche searchAnagrafiche = new SearchAnagrafiche();
		searchAnagrafiche.setToken(userToken);
		searchAnagrafiche.setType("FASCICOLO");

		for (String key : searchCriteria.keySet()) {
		    if (key.equalsIgnoreCase("TEXT")) {
			//continue;
			KeyValuePair kvpx = new KeyValuePair();
			    kvpx.setKey("$KEYWORDS");
			    kvpx.setValue(searchCriteria.get(key));
			    fascicoloCriteria.add(kvpx);
			    continue;
		    }

		    KeyValuePair kvp1 = new KeyValuePair();
		    kvp1.setKey(key.toUpperCase());
		    kvp1.setValue(searchCriteria.get(key));
		    fascicoloCriteria.add(kvp1);
		}

		// if(searchAnagrafiche.getSearchCriteria()==null){
		// fascicoloCriteria.add(new KeyValuePair());
		// }

		searchAnagrafiche.setSearchCriteria(fascicoloCriteria.toArray(new KeyValuePair[0]));

		if (debug) {
		    System.out.println(new Date().toString() +" - "+ "ricerca su Docer: tipo=" + tipo + ", searchCriteria: " + toMap(searchAnagrafiche.getSearchCriteria()).toString());
		}

		long start2 = new Date().getTime();
		SearchAnagraficheResponse searchAnagraficheResponse = fonteDocerWSClient.searchAnagrafiche(searchAnagrafiche);
		long end2 = new Date().getTime();
		queryTime2 = end2 - start2;

		if (searchAnagraficheResponse != null && searchAnagraficheResponse.get_return() != null) {
		    for (SearchItem si : searchAnagraficheResponse.get_return()) {

			boolean found1 = false;
			boolean found2 = false;
			for (KeyValuePair kvp : si.getMetadata()) {
			    if (found1 && found2) {
				break;
			    }
			    if (kvp.getKey().equalsIgnoreCase("RANKING")) {
				found1 = true;
				try {
				    float thisRanking = Float.parseFloat(kvp.getValue());
				    if (thisRanking > ranking) {
					ranking = thisRanking;
				    }
				}
				catch (NumberFormatException nfe) {

				}
				continue;
			    }
			    if (kvp.getKey().equalsIgnoreCase(RACC_UID)) {
				found2 = true;
				if (!ids.contains(kvp.getValue())) {
				    ids.add(kvp.getValue());
				    totalCount++;
				}
				continue;
			    }
			}

		    }
		}

	    }

	    searchEntitaInformativaAvanzataResponse.setQueryTime((int) Math.ceil((queryTime1 + queryTime2) / 1000.0));
	    searchEntitaInformativaAvanzataResponse.setNumFound(totalCount);
	    searchEntitaInformativaAvanzataResponse.setMaxScore(ranking);
	    searchEntitaInformativaAvanzataResponse.setId(ids.toArray(new String[0]));
	    return searchEntitaInformativaAvanzataResponse;

	}
	catch (RemoteException e) {
	    if (debug) {
		saveStringToFile("fonteDocerWSClient.search -> userId="+ userId+", idFonte=" +idFonte +", tipo=" +tipo+", searchCriteria=" +searchCriteria.toString() +": errore=" +e.getMessage(), "searchEntitaInformativaAvanzata.txt");
	    }
	    throw new RerFonteError("fonteDocerWSClient.search", e);
	}
	catch (DocerExceptionException e) {
	    if (debug) {
		saveStringToFile("fonteDocerWSClient.search -> userId="+ userId+", idFonte=" +idFonte +", tipo=" +tipo+", searchCriteria=" +searchCriteria.toString() +": errore=" +e.getMessage(), "searchEntitaInformativaAvanzata.txt");
	    }
	    throw new RerFonteError("fonteDocerWSClient.search", e);
	}

    }

    private boolean verifyToken(String token) {
	try {
	    if (StringUtils.isEmpty(token)) {
		return false;
	    }

	    if (!ws_fonte_auth_url.equals("")) {

		VerifyToken verifyToken = new VerifyToken();
		verifyToken.setToken(token);

		VerifyTokenResponse verifyTokenResponse = fonteAuthWSClient.verifyToken(verifyToken);

		// ticket ancora buono ?
		return verifyTokenResponse.get_return();
	    }
	    else {
		VerifyTicket verifyTicket = new VerifyTicket();
		verifyTicket.setToken(token);

		VerifyTicketResponse verifyTicketResponse = fonteDocerWSClient.verifyTicket(verifyTicket);

		// ticket ancora buono ?
		return verifyTicketResponse.get_return();
	    }

	}
	catch (RemoteException e1) {
	    return false;
	}
	catch (AuthenticationServiceExceptionException e1) {
	    return false;
	}
    }

    private String _loginWSFonte(String userId) throws RerFonteError {

	// String token = TOKEN_CACHE.get(userId);
	//
	// if (verifyToken(token)) {
	// return token;
	// }

	String password = userId;

	if (userId.equalsIgnoreCase(ws_fonte_login_userid)) {
	    password = ws_fonte_login_pwd;
	}

	if (!ws_fonte_auth_url.equals("")) {

	    Login login = new Login();
	    login.setUsername(userId);
	    login.setPassword(password);
	    login.setCodiceEnte("");
	    login.setApplication("");

	    LoginResponse resp;
	    try {
		resp = fonteAuthWSClient.login(login);
		return resp.get_return();
	    }
	    catch (RemoteException e) {
		throw new RerFonteError("login error", e);
	    }
	    catch (AuthenticationServiceExceptionException e) {
		throw new RerFonteError("login error", e);
	    }
	}
	else {
	    DocerServicesStub.Login login = new DocerServicesStub.Login();

	    login.setUserId(userId);
	    login.setPassword(password);
	    login.setCodiceEnte("");

	    DocerServicesStub.LoginResponse loginRes;
	    try {
		loginRes = fonteDocerWSClient.login(login);
		return "calltype:internal|" + loginRes.get_return();
	    }
	    catch (RemoteException e) {
		throw new RerFonteError("login error", e);
	    }
	    catch (DocerExceptionException e) {
		throw new RerFonteError("login error", e);
	    }
	}

    }

    private void logoutWSFonte(String token) {

	if (token == null) {
	    return;
	}

	if (!ws_fonte_auth_url.equals("")) {

	    Logout logout = new Logout();
	    logout.setToken(token);

	    try {
		fonteAuthWSClient.logout(logout);
	    }
	    catch (Exception e) {
	    }
	}
	else {
	    DocerServicesStub.Logout logout = new DocerServicesStub.Logout();

	    logout.setToken(token);

	    try {
		fonteDocerWSClient.logout(logout);
	    }
	    catch (Exception e) {

	    }
	}

    }

    private Document buildXmlMetadata(KeyValuePair[] profile, boolean documento) throws RerFonteError {

	DocumentBuilder db;
	try {
	    db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}
	catch (ParserConfigurationException e) {
	    throw new RerFonteError("buildXmlMetadata", e);
	}
	Document document = db.newDocument();

	Element eiElement = document.createElement("entita_informativa");
	document.appendChild(eiElement);

	if (profile != null) {
	    Element firstNodeElement = null;
	    if (documento) {
		firstNodeElement = document.createElement("documento");
	    }
	    else {
		firstNodeElement = document.createElement("fascicolo");
	    }

	    eiElement.appendChild(firstNodeElement);

	    for (KeyValuePair kvp : profile) {
		Element metaElement = document.createElement(kvp.getKey());
		metaElement.setTextContent(kvp.getValue());
		firstNodeElement.appendChild(metaElement);
	    }
	}

	return document;
    }

    public String getStringFromDocument(Document doc, Source xsltSource) {
	try {

	    DOMSource domSource = new DOMSource(doc);
	    StringWriter writer = new StringWriter();
	    StreamResult result = new StreamResult(writer);
	    TransformerFactory tf = TransformerFactory.newInstance();

	    Transformer transformer = tf.newTransformer(xsltSource);

	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	    transformer.transform(domSource, result);
	    return writer.toString();
	}
	catch (TransformerException ex) {
	    ex.printStackTrace();
	    return "";
	}
    }

    public void transform(String dataXML, String inputXSL, String output) throws TransformerConfigurationException, TransformerException {

	TransformerFactory factory = TransformerFactory.newInstance();
	StreamSource xslStream = new StreamSource(inputXSL);
	Transformer transformer = factory.newTransformer(xslStream);
	StreamSource in = new StreamSource(dataXML);
	StreamResult out = new StreamResult(output);
	transformer.transform(in, out);

    }

    // public File getEntitaInformativa(String uid) throws RerFonteError,
    // ObjectNotFoundError, AuthorizationDeniedError {
    //
    // String ticket = null;
    //
    // if (!uid.startsWith(RACC_UID_DOC_PREFIX)) {
    // throw new ObjectNotFoundError();
    // }
    //
    // try {
    //
    // ticket = loginWSFonte();
    //
    // // Map<String, File> fileMap = new HashMap<String, File>();
    // Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
    //
    // String filename = getFileName(ticket, uid);
    // DataHandler dh = downloadDocument(ticket, uid);
    // // String guid1 = UUID.randomUUID().toString();
    // // File f1 = new File(cacheDir, guid1);
    // // saveToDisk(dh, f1);
    // // fileMap.put(filename, f1);
    // try {
    // fileMap.put(filename, dh.getDataSource().getInputStream());
    // }
    // catch (IOException e) {
    // throw new RerFonteError("dh.getDataSource().getInputStream()", e);
    // }
    //
    // String[] related;
    // try {
    // related = getRelatedDocuments(ticket, uid);
    // }
    // catch (Exception e) {
    // throw new RerFonteError("getRelatedDocuments", e);
    // }
    //
    // String docId = uid.replaceAll("^" + RACC_UID_DOC_PREFIX, "");
    //
    // for (String relId : related) {
    //
    // if (relId.equals(docId)) {
    // continue;
    // }
    //
    // String filenameRel = getFileName(ticket, relId);
    // DataHandler dhRel = downloadDocument(ticket, relId);
    // // String guidRel = UUID.randomUUID().toString();
    // // File fRel = new File(cacheDir, guidRel);
    // // saveToDisk(dhRel, fRel);
    // // fileMap.put(filenameRel, fRel);
    // try {
    // fileMap.put(filenameRel, dhRel.getDataSource().getInputStream());
    // }
    // catch (IOException e) {
    // throw new RerFonteError("dh.getDataSource().getInputStream()", e);
    // }
    //
    // }
    //
    // Date now = new Date();
    //
    // String mills = String.valueOf(now.getTime());
    //
    // File archive = new File(cacheDir, uid + "-" + mills + ".zip");
    // // zip(fileMap, archive);
    // toZip(fileMap, archive);
    //
    // if (archive.length() > maxZipFile) {
    // throw new RerFonteError(new Exception("l'entita informativa " + uid +
    // " supera i " + maxZipFile + " Bytes"));
    // }
    //
    // return archive;
    //
    // }
    // // catch (Exception e) {
    // // throw new RerFonteError(e);
    // // }
    // finally {
    // logoutWSFonte(ticket);
    // }
    //
    // }

    // private void saveToDisk(DataHandler dh, File destFile) throws Exception {
    //
    // FileOutputStream filesystemOutputStream = null;
    //
    // try {
    //
    // int onegiga = 1073741824;
    // int totalRead = 0;
    //
    // // stream per scrivere su filesystem
    // filesystemOutputStream = new
    // FileOutputStream(destFile.getAbsolutePath());
    //
    // InputStream is = dh.getDataSource().getInputStream();
    // // buffer
    // byte[] buffer = new byte[4096];
    // int bytesRead = 0;
    // while ((totalRead < onegiga) && (bytesRead = is.read(buffer)) != -1) {
    // filesystemOutputStream.write(buffer, 0, bytesRead);
    // totalRead += bytesRead;
    // }
    //
    // if (totalRead >= onegiga) {
    // throw new Exception("superato limite di 1 GB nel caching del file");
    // }
    // filesystemOutputStream.close();
    //
    // }
    // catch (IOException e) {
    // throw new Exception("saveToDisk: IOException: " + e.getMessage());
    //
    // }
    // finally {
    //
    // if (dh.getInputStream() != null) {
    // try {
    // dh.getInputStream().close();
    // }
    // catch (IOException e2) {
    //
    // }
    //
    // }
    //
    // if (filesystemOutputStream != null) {
    // try {
    // filesystemOutputStream.close();
    // }
    // catch (IOException e2) {
    //
    // }
    // }
    //
    // }
    // }

    private void toZip(Map<String, InputStream> fileMap, File archive) throws RerFonteError {

	try {

	    if (!archive.getParentFile().exists()) {
		archive.getParentFile().mkdir();
		archive.createNewFile();
	    }

	    // create byte buffer
	    byte[] buffer = new byte[1024];

	    /*
	     * To create a zip file, use ZipOutputStream(OutputStream out)
	     * constructor of ZipOutputStream class.
	     */

	    // create object of FileOutputStream
	    FileOutputStream fout = new FileOutputStream(archive.getAbsolutePath());

	    // create object of ZipOutputStream from FileOutputStream
	    ZipOutputStream zout = new ZipOutputStream(fout);

	    for (String filename : fileMap.keySet()) {

		// System.out.println("Adding " + filename);
		// create object of FileInputStream for source file
		InputStream fin = fileMap.get(filename);

		/*
		 * To begin writing ZipEntry in the zip file, use void
		 * putNextEntry(ZipEntry entry) method of ZipOutputStream class.
		 * This method begins writing a new Zip entry to the zip file
		 * and positions the stream to the start of the entry data.
		 */

		ZipEntry ze = new ZipEntry(filename);

		zout.putNextEntry(ze);

		/*
		 * After creating entry in the zip file, actually write the
		 * file.
		 */
		int length;

		while ((length = fin.read(buffer)) > 0) {
		    zout.write(buffer, 0, length);
		}

		/*
		 * After writing the file to ZipOutputStream, use void
		 * closeEntry() method of ZipOutputStream class to close the
		 * current entry and position the stream to write the next
		 * entry.
		 */

		zout.closeEntry();

		// close the InputStream
		fin.close();

	    }

	    // close the ZipOutputStream
	    zout.close();

	}
	catch (IOException ioe) {
	    throw new RerFonteError("toZip", ioe);
	}
    }

    // private void zip(Map<String, File> fileMap, File archive) throws
    // Exception {
    //
    // try {
    //
    // if (!archive.getParentFile().exists()) {
    // archive.getParentFile().mkdir();
    // archive.createNewFile();
    // }
    //
    // // create byte buffer
    // byte[] buffer = new byte[1024];
    //
    // /*
    // * To create a zip file, use ZipOutputStream(OutputStream out)
    // * constructor of ZipOutputStream class.
    // */
    //
    // // create object of FileOutputStream
    // FileOutputStream fout = new FileOutputStream(archive.getAbsolutePath());
    //
    // // create object of ZipOutputStream from FileOutputStream
    // ZipOutputStream zout = new ZipOutputStream(fout);
    //
    // for (String filename : fileMap.keySet()) {
    //
    // // System.out.println("Adding " + filename);
    // // create object of FileInputStream for source file
    // FileInputStream fin = new FileInputStream(fileMap.get(filename));
    //
    // /*
    // * To begin writing ZipEntry in the zip file, use void
    // * putNextEntry(ZipEntry entry) method of ZipOutputStream class.
    // * This method begins writing a new Zip entry to the zip file
    // * and positions the stream to the start of the entry data.
    // */
    //
    // ZipEntry ze = new ZipEntry(filename);
    //
    // zout.putNextEntry(ze);
    //
    // /*
    // * After creating entry in the zip file, actually write the
    // * file.
    // */
    // int length;
    //
    // while ((length = fin.read(buffer)) > 0) {
    // zout.write(buffer, 0, length);
    // }
    //
    // /*
    // * After writing the file to ZipOutputStream, use void
    // * closeEntry() method of ZipOutputStream class to close the
    // * current entry and position the stream to write the next
    // * entry.
    // */
    //
    // zout.closeEntry();
    //
    // // close the InputStream
    // fin.close();
    //
    // }
    //
    // // close the ZipOutputStream
    // zout.close();
    //
    // }
    // catch (IOException ioe) {
    // throw new Exception("IOException :" + ioe);
    // }
    // }

    private AuthenticationServiceStub getAuthenticationClient(String authenticationEpr) throws AxisFault {

	AuthenticationServiceStub authenticationServicesClient = new AuthenticationServiceStub(authenticationEpr);

	Options options = authenticationServicesClient._getServiceClient().getOptions();
	options.setTo(new EndpointReference(authenticationEpr));

	options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
	options.setTimeOutInMilliSeconds(timeout);
	options.setProperty(Constants.Configuration.CACHE_ATTACHMENTS, cacheAttachments ? Constants.VALUE_TRUE : Constants.VALUE_FALSE);
	options.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR, cacheDir.getAbsolutePath());
	options.setProperty(Constants.Configuration.FILE_SIZE_THRESHOLD, fileSizeThreshold);

	return authenticationServicesClient;
    }

    private DocerServicesStub getDocerServicesClient(String epr) throws AxisFault {

	DocerServicesStub docerServicesClient = new DocerServicesStub(epr);

	Options options = docerServicesClient._getServiceClient().getOptions();

	options.setTo(new EndpointReference(epr));

	options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
	options.setTimeOutInMilliSeconds(timeout);
	options.setProperty(Constants.Configuration.CACHE_ATTACHMENTS, cacheAttachments ? Constants.VALUE_TRUE : Constants.VALUE_FALSE);
	options.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR, cacheDir.getAbsolutePath());
	options.setProperty(Constants.Configuration.FILE_SIZE_THRESHOLD, fileSizeThreshold);

	return docerServicesClient;
    }

    private DataHandler downloadDocument(String ticket, String docId) throws RerFonteError, ObjectNotFoundError {
	DownloadDocumentResponse downloadDocumentResponse;
	try {

	    DownloadDocument downloadDocument = new DownloadDocument();
	    downloadDocument.setToken(ticket);
	    downloadDocument.setDocId(docId);

	    // lo scarico
	    downloadDocumentResponse = fonteDocerWSClient.downloadDocument(downloadDocument);
	    return downloadDocumentResponse.get_return().getHandler();
	}
	catch (RemoteException e) {
	    if (debug) {
		saveStringToFile("fonteDocerWSClient.downloadDocument -> docId="+ docId +": +errore=" +e.getMessage(), "downloadDocument.txt");
	    }
	    if (e.getMessage().matches(".*documento.*non trovato.*")) {
		throw new ObjectNotFoundError("documento " + docId + " non trovato", e);
	    }
	    throw new RerFonteError("downloadDocument: " + docId, e);
	}
	catch (DocerExceptionException e) {
	    if (debug) {
		saveStringToFile("fonteDocerWSClient.downloadDocument -> docId="+ docId +": +errore=" +e.getMessage(), "downloadDocument.txt");
	    }
	    if (e.getMessage().matches(".*documento.*non trovato.*")) {
		throw new ObjectNotFoundError("documento " + docId + " non trovato", e);
	    }
	    throw new RerFonteError("downloadDocument: " + docId, e);
	}

    }

    public String getFileName(String ticket, String docId) throws RerFonteError, ObjectNotFoundError {

	GetProfileDocument getProfileDocument = new GetProfileDocument();
	getProfileDocument.setToken(ticket);
	getProfileDocument.setDocId(docId);

	// lo scarico
	GetProfileDocumentResponse getProfileDocumentResponse;
	try {
	    getProfileDocumentResponse = fonteDocerWSClient.getProfileDocument(getProfileDocument);
	}
	catch (RemoteException e) {
	    if (debug) {
		saveStringToFile("fonteDocerWSClient.getFileName -> docId="+ docId +": +errore=" +e.getMessage(), "getFileName.txt");
	    }
	    if (e.getMessage().matches(".*documento.*non trovato.*")) {
		throw new ObjectNotFoundError("documento " + docId + " non trovato", e);
	    }
	    throw new RerFonteError("getFileName: " + docId, e);
	}
	catch (DocerExceptionException e) {
	    if (debug) {
		saveStringToFile("fonteDocerWSClient.getFileName -> docId="+ docId +": +errore=" +e.getMessage(), "getFileName.txt");
	    }
	    if (e.getMessage().matches(".*documento.*non trovato.*")) {
		throw new ObjectNotFoundError("documento " + docId + " non trovato", e);
	    }
	    throw new RerFonteError("getFileName: " + docId, e);
	}

	if (getProfileDocumentResponse == null || getProfileDocumentResponse.get_return() == null) {
	    if (debug) {
		saveStringToFile("fonteDocerWSClient.getProfileDocumentResponse -> docId="+ docId +": +errore=getProfileDocumentResponse=null" , "getFileName.txt");
	    }
	    throw new ObjectNotFoundError();
	}

	String docname = extractValueByName(getProfileDocumentResponse.get_return(), "DOCNAME");

	return docname;
	// docname = docId + " - " + docname;
	// String tipoComponente =
	// extractValueByName(getProfileDocumentResponse.get_return(),
	// "TIPO_COMPONENTE");

	// if (tipoComponente.equalsIgnoreCase("principale")) {
	// return "Documento Principale/" + docname;
	// }
	//
	// if (tipoComponente.equalsIgnoreCase("ALLEGATO")) {
	// return "Allegati/" + docname;
	// }
	//
	// if (tipoComponente.equalsIgnoreCase("ANNOTAZIONE")) {
	// return "Annotazioni/" + docname;
	// }
	//
	// if (tipoComponente.equalsIgnoreCase("ANNESSO")) {
	// return "Annessi/" + docname;
	// }
	//
	// return "altri related/" + docname;

    }

    // private String[] getRelatedDocuments(String ticket, String docId) throws
    // Exception {
    //
    // try {
    // GetRelatedDocuments getRelatedDocuments = new GetRelatedDocuments();
    // getRelatedDocuments.setToken(ticket);
    // getRelatedDocuments.setDocId(docId);
    //
    // // scarico allegati
    // GetRelatedDocumentsResponse getRelatedDocumentsResponse =
    // fonteDocerWSClient.getRelatedDocuments(getRelatedDocuments);
    //
    // if (getRelatedDocumentsResponse == null ||
    // getRelatedDocumentsResponse.get_return() == null) {
    // return new String[0];
    // }
    //
    // return getRelatedDocumentsResponse.get_return();
    //
    // }
    // catch (RemoteException e) {
    // throw new Exception("getRelatedDocuments: " + docId + ": " +
    // e.getMessage());
    // }
    // catch (DocerExceptionException e) {
    // throw new Exception("getRelatedDocuments: " + docId + ": " +
    // e.getMessage());
    // }
    //
    // }

    private String extractValueByName(KeyValuePair[] metadata, String propName) {

	for (KeyValuePair kvp : metadata) {
	    if (kvp.getKey().equalsIgnoreCase(propName)) {
		return kvp.getValue();
	    }
	}

	return null;
    }

    private static List<AttributoType> DEFAULT_FORM = null;

    private List<AttributoType> getDEFAULTFORM() {

	if (DEFAULT_FORM == null) {
	    DEFAULT_FORM = new ArrayList<AttributoType>();

	    AttributoType tipo_oggetto = new AttributoType("tipo_oggetto", "Documento o Fascicolo", "Tipo di oggetto da ricercare", TipoAttributoType.DOMINIO, "{\"Documento\", \"Fascicolo\"}", true,
		    "");
	    DEFAULT_FORM.add(tipo_oggetto);

	    // attributi condivisi
	    AttributoType progr_fascicolo = new AttributoType("progr_fascicolo", "Progressivo Fascicolo", "Progressivo Fascicolo", TipoAttributoType.TESTO, "", false, "");
	    DEFAULT_FORM.add(progr_fascicolo);
	    AttributoType anno_fascicolo = new AttributoType("anno_fascicolo", "Anno Fascicolo", "Anno di apertura del Fascicolo", TipoAttributoType.TESTO, "", false, "");
	    DEFAULT_FORM.add(anno_fascicolo);
	    // documenti
	    AttributoType text = new AttributoType("text", "Ricerca testuale", "Ricerca testuale su profilo e contenuto dei documenti (solo per i Documenti)", TipoAttributoType.TESTO, "", false,
		    "$tipo_oggetto$==\"Documento\"");
	    DEFAULT_FORM.add(text);
	    AttributoType d_abstract = new AttributoType("abstract", "Oggetto del documento", "Oggetto del documento", TipoAttributoType.TESTO, "", false, "$tipo_oggetto$==\"Documento\"");
	    DEFAULT_FORM.add(d_abstract);

	    AttributoType num_pg = new AttributoType("num_pg", "Numero di protocollo", "Numero di protocollo", TipoAttributoType.TESTO, "", false, "$tipo_oggetto$==\"Documento\"");
	    DEFAULT_FORM.add(num_pg);
	    AttributoType anno_pg = new AttributoType("anno_pg", "Anno di protocollo", "Anno di protocollo", TipoAttributoType.NUMERO, "", false, "$tipo_oggetto$==\"Documento\"");
	    DEFAULT_FORM.add(anno_pg);
	    AttributoType oggetto_pg = new AttributoType("oggetto_pg", "Oggetto del protocollo", "Oggetto del protocollo", TipoAttributoType.TESTO, "", false, "$tipo_oggetto$==\"Documento\"");
	    DEFAULT_FORM.add(oggetto_pg);

	    AttributoType n_registraz = new AttributoType("n_registraz", "Numero di registrazione particolare", "Numero di registrazione particolare", TipoAttributoType.TESTO, "", false,
		    "$tipo_oggetto$==\"Documento\"");
	    DEFAULT_FORM.add(n_registraz);
	    AttributoType a_registraz = new AttributoType("a_registraz", "Anno di registrazione particolare", "Anno di registrazione particolare", TipoAttributoType.NUMERO, "", false,
		    "$tipo_oggetto$==\"Documento\"");
	    DEFAULT_FORM.add(a_registraz);
	    AttributoType o_registraz = new AttributoType("o_registraz", "Oggetto di registrazione particolare", "Oggetto di registrazione particolare", TipoAttributoType.TESTO, "", false,
		    "$tipo_oggetto$==\"Documento\"");
	    DEFAULT_FORM.add(o_registraz);

	    // fascicoli
	    AttributoType data_apertura = new AttributoType("data_apertura", "Data apertura", "Data apertura del fascicolo", TipoAttributoType.DATARANGE, "", false, "$tipo_oggetto$==\"Fascicolo\"");
	    DEFAULT_FORM.add(data_apertura);
	    AttributoType data_chiusura = new AttributoType("data_chiusura", "Data chiusura", "Data chiusura del fascicolo", TipoAttributoType.DATARANGE, "", false, "$tipo_oggetto$==\"Fascicolo\"");
	    DEFAULT_FORM.add(data_chiusura);
	    AttributoType id_proc = new AttributoType("id_proc", "ID Procedimento", "Identificativo del procedimento", TipoAttributoType.TESTO, "", false, "$tipo_oggetto$==\"Fascicolo\"");
	    DEFAULT_FORM.add(id_proc);
	    AttributoType id_immobile = new AttributoType("id_immobile", "ID Immobile", "Identificativo dell'immobile", TipoAttributoType.TESTO, "", false, "$tipo_oggetto$==\"Fascicolo\"");
	    DEFAULT_FORM.add(id_immobile);

	}

	return DEFAULT_FORM;

    }

    private static String relazione_group_prefix = "RLZN_";
    private static String templateGroupIdReazione = relazione_group_prefix + "%s_%s_%s";

    public static String generateGroupIdFromDelega(String tipoRelazione, String tipoId, String id) {

	return String.format(templateGroupIdReazione, tipoRelazione.toUpperCase(), tipoId.toUpperCase(), id.toUpperCase());
    }

    private Map<String, String> toMap(KeyValuePair[] kvpArr) {
	Map<String, String> map = new HashMap<String, String>();

	for (KeyValuePair kvp : kvpArr) {
	    map.put(kvp.getKey(), kvp.getValue());
	}

	return map;
    }

    public String toDocerDatetime(Date raccDate) {

	DateTime d = new DateTime(raccDate.getTime());
	// String strDate = d.toString("yyyy-MM-dd'T'HH:mm:ss.SSS");
	String strDate = d.toString();

	try {
	    DateTime date = parseDateTime(strDate);
	    return date.toString();
	}
	catch (Exception e) {
	    return null;
	}

    }

    private DateTime parseDateTime(String datetime) {
	DateTimeFormatter fm = ISODateTimeFormat.dateTime();
	return fm.parseDateTime(datetime);
    }

    public void saveStringToFile(String str, String name) {
	try {
	    if (str == null) {
		str = "null";
	    }
	    DateTime d = new DateTime();
	    String strDate = d.toString("yyyy-MM-dd HH.mm.ss");

	    FileUtils.writeStringToFile(new File(cacheDir, strDate + "_" + name), str);
	}
	catch (IOException e) {
	    System.out.println(new Date().toString() +" - saveStringToFile: "+ e.getMessage());
	}
    }

}
