package it.kdm.docer.fonte.batch.popolamentoRaccoglitore;

import com.google.common.base.Strings;
import it.kdm.docer.clients.AuthenticationServiceStub;
import it.kdm.docer.clients.AuthenticationServiceStub.Login;
import it.kdm.docer.clients.AuthenticationServiceStub.LoginResponse;
import it.kdm.docer.clients.AuthenticationServiceStub.Logout;
import it.kdm.docer.clients.DocerExceptionException;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.clients.DocerServicesStub.CreateGroup;
import it.kdm.docer.clients.DocerServicesStub.CreateUser;
import it.kdm.docer.clients.DocerServicesStub.GetAOO;
import it.kdm.docer.clients.DocerServicesStub.GetAOOResponse;
import it.kdm.docer.clients.DocerServicesStub.GetEnte;
import it.kdm.docer.clients.DocerServicesStub.GetEnteResponse;
import it.kdm.docer.clients.DocerServicesStub.GetFascicolo;
import it.kdm.docer.clients.DocerServicesStub.GetFascicoloResponse;
import it.kdm.docer.clients.DocerServicesStub.GetProfileDocument;
import it.kdm.docer.clients.DocerServicesStub.GetProfileDocumentResponse;
import it.kdm.docer.clients.DocerServicesStub.GetRelatedDocuments;
import it.kdm.docer.clients.DocerServicesStub.GetRelatedDocumentsResponse;
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
import it.kdm.docer.clients.DocerServicesStub.SetACLDocument;
import it.kdm.docer.clients.DocerServicesStub.SetACLFascicolo;
import it.kdm.docer.clients.DocerServicesStub.SetGroupsOfUser;
import it.kdm.docer.clients.DocerServicesStub.UpdateFascicolo;
import it.kdm.docer.clients.DocerServicesStub.UpdateProfileDocument;
import it.kdm.docer.clients.DocerServicesStub.UpdateUser;
import it.kdm.docer.clients.ExceptionException;
import it.kdm.docer.clients.RerServiziPerLeFontiServiceStub;
import it.kdm.docer.clients.RerServiziPerLeFontiServiceStub.SaveOrUpdateInstanceRequestType;
import it.kdm.docer.commons.Config;
import it.kdm.docer.fonte.batch.BatchLog;
import it.kdm.docer.fonte.batch.DirectoryUtils;
import it.kdm.docer.fonte.batch.Log4jUtils;
import it.kdm.docer.fonte.batch.email.EmailFactory;
import it.kdm.docer.fonte.batch.popolamentoFonte.objects.OrderedList;
import it.kdm.docer.fonte.batch.popolamentoRaccoglitore.objects.DocumentoFlusso;
import it.kdm.docer.fonte.batch.popolamentoRaccoglitore.objects.FascicoloFlusso;
import it.kdm.docer.fonte.batch.popolamentoRaccoglitore.objects.SFTPModeConfiguration;
import it.kdm.docer.fonte.batch.popolamentoRaccoglitore.objects.SOAPModeConfiguration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.llom.OMDocumentImpl;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.apache.xml.utils.XML11Char;
import org.jaxen.JaxenException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class BatchPopolamentoRaccoglitore implements org.quartz.Job {

	private static String lastMessage = "";
	private BatchLog fonteLog = null;

	private OMFactory factory = OMAbstractFactory.getOMFactory();
	private OMElement keywordsFascicoloNode = null;
	private boolean severe_error = false;

	final static String UTF8 = "UTF-8";

	private org.slf4j.Logger logger = org.slf4j.org.slf4j.LoggerFactory.getLogger(BatchPopolamentoRaccoglitore.class);

	private static QName qnameDefault = new QName("default");
	private static QName qnameLabel = new QName("label");

	private static QName qnameAutorizzazione = new QName("autorizzazione");
	private static QName qnameTipoId = new QName("tipoId");
	private static QName qnameTipoRelazione = new QName("tipoRelazione");

	boolean delete_xml_after_upload = false;
	private String xml_transfer_mode = "none";
	private String notification_date_format = "yyyy-MM-dd";
	private String notification_time_format = "HH:mm:ss";
	private String today_format = "yyyyMMdd";
	private String ticks_format = "yyyyMMddHHmmssmS";

	private String ws_fonte_url;
	private String ws_fonte_auth_url;
	private String ws_fonte_login_userid;
	private String ws_fonte_login_pwd;
	private String ws_fonte_login_cod_ente;
	private long timeout;
	private Boolean cacheAttachments;
	private File cacheDir;
	private String fileSizeThreshold;
	private Integer primarysearch_max_results;

	private java.text.SimpleDateFormat date_formatter;
	private java.text.SimpleDateFormat time_formatter;
	private java.text.SimpleDateFormat dummy_formatter;
	private java.text.SimpleDateFormat flow_formatter;

	private int cntFascicoliFound = 0;
	private int cntDocumentiFascicolatiFound = 0;
	private int cntDocumentiNonfascicolatiFound = 0;
	private int cntXmlUploaded = 0;
	private int cntErrors = 0;
	private String logLocation = "";

	private String ticket_ws_fonte;

	Map<String, String> descriptionCache = new HashMap<String, String>();

	private static Config config;
	private static Properties configurationProperties;

	@SuppressWarnings("unchecked")
	private Map<String, String> getLogFile() {
		Collection<Logger> allLoggers = new ArrayList<Logger>();

		Logger rootLogger = Logger.getRootLogger();

		allLoggers.add(rootLogger);

		for (Enumeration<Logger> loggers =

		rootorg.slf4j.LoggerFactory.getLoggerRepository().getCurrentLoggers();

		loggers.hasMoreElements();) {

			allLoggers.add(loggers.nextElement());

		}

		Set<FileAppender> fileAppenders =

		new LinkedHashSet<FileAppender>();

		for (Logger logger : allLoggers) {

			for (Enumeration<Appender> appenders =

			logger.getAllAppenders();

			appenders.hasMoreElements();) {

				Appender appender = appenders.nextElement();

				if (appender instanceof FileAppender) {

					fileAppenders.add((FileAppender) appender);

				}

			}

		}

		Map<String, String> locations =

		new LinkedHashMap<String, String>();

		for (FileAppender appender : fileAppenders) {

			if (appender.getName().equalsIgnoreCase("R")) {
				locations.put(appender.getName(), appender.getFile());
			}

			locations.put(appender.getName(), appender.getFile());

		}

		return locations;
	}

	public static void setConfig(Config configuration, Properties configProperties) {

		config = configuration;
		configurationProperties = configProperties;
	}

	public BatchPopolamentoRaccoglitore() throws Exception {

		if (config == null) {
			throw new Exception("file config non impostato: invocare im metodo statico setConfig prima di una nuova istanza");
		}
		if (configurationProperties == null) {
			throw new Exception("file configurationProperties non impostato: invocare im metodo statico setConfig prima di una nuova istanza");
		}

		if (configurationProperties.getProperty("cacheAttachments") == null) {
			writeError("variabile cacheAttachments non trovata nel file /configuration.properties");
			throw new Exception("variabile cacheAttachments non trovata nel file /configuration.properties");
		}
		cacheAttachments = Boolean.valueOf(configurationProperties.getProperty("cacheAttachments"));

		if (configurationProperties.getProperty("cacheDir") == null) {
			writeError("variabile cacheDir non trovata nel file /configuration.properties");
			throw new Exception("variabile cacheDir non trovata nel file /configuration.properties");
		}

		String str = configurationProperties.getProperty("cacheDir");

		cacheDir = new File(str);
		if (!cacheDir.exists()) {
			writeError("Cache Directory " + cacheDir.getAbsolutePath() + " non trovata");
			throw new Exception("Cache Directory " + cacheDir.getAbsolutePath() + " non trovata");
		}

		cacheDir = DirectoryUtils.createTempDirectory("BatchPopolamentoRaccoglitore", cacheDir);

		fileSizeThreshold = configurationProperties.getProperty("fileSizeThreshold");

		fileSizeThreshold = configurationProperties.getProperty("fileSizeThreshold");

		if (fileSizeThreshold == null) {
			writeError("variabile fileSizeThreshold non trovata nel file /configuration.properties");
			throw new Exception("variabile fileSizeThreshold non trovata nel file /configuration.properties");
		}

		try {
			Integer.parseInt(fileSizeThreshold);
		}
		catch (NumberFormatException nfe) {
			writeError("chiave fileSizeThreshold ha formato errato: " + nfe.getMessage());
			throw new Exception("chiave fileSizeThreshold ha formato errato: " + nfe.getMessage());
		}

		if (configurationProperties.getProperty("primarysearch_max_results") == null) {
			writeError("variabile fileSizeThreshold non trovata nel file /configuration.properties");
			throw new Exception("variabile fileSizeThreshold non trovata nel file /configuration.properties");
		}

		try {
			primarysearch_max_results = Integer.parseInt(configurationProperties.getProperty("primarysearch_max_results"));
		}
		catch (NumberFormatException nfe) {
			writeError("chiave ws_primarysearch_max_results ha formato errato: " + nfe.getMessage());
			throw new Exception("chiave  ws_primarysearch_max_results ha formato errato: " + nfe.getMessage());
		}

		if (configurationProperties.getProperty("timeout") == null) {
			writeError("variabile timeout non trovata nel file /configuration.properties");
			throw new Exception("variabile timeout non trovata nel file /configuration.properties");
		}

		try {
			timeout = Integer.parseInt(configurationProperties.getProperty("timeout"));
		}
		catch (NumberFormatException nfe) {
			writeError("chiave timeout ha formato errato: " + nfe.getMessage());
			throw new Exception("chiave timeout ha formato errato: " + nfe.getMessage());
		}

		String str_delete_xml_after_upload = readConfigKey("//group[@name='batch-popolamento-raccoglitore']/section[@name='config']/delete_xml_after_upload");
		if (str_delete_xml_after_upload == null)
			throw new Exception("Parametri non completi per la configurazione del batch: chiave //group[@name='batch-popolamento-raccoglitore']/section[@name='config']/delete_xml_after_upload non trovata");

		delete_xml_after_upload = Boolean.valueOf(str_delete_xml_after_upload);

		xml_transfer_mode = readConfigKey("//group[@name='batch-popolamento-raccoglitore']/section[@name='config']/xml_transfer_mode");

		if (xml_transfer_mode == null)
			throw new Exception("Parametri non completi per la configurazione del batch: chiave //group[@name='batch-popolamento-raccoglitore']/section[@name='config']/xml_transfer_mode non trovata");

		date_formatter = new SimpleDateFormat(notification_date_format);
		time_formatter = new SimpleDateFormat(notification_time_format);
		flow_formatter = new SimpleDateFormat(ticks_format);

		ws_fonte_login_userid = readConfigKey("//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_login_userid");
		ws_fonte_login_pwd = readConfigKey("//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_login_pwd");
		ws_fonte_login_cod_ente = readConfigKey("//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_login_cod_ente");
		ws_fonte_url = readConfigKey("//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_url");
		ws_fonte_auth_url = readConfigKey("//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_auth_url");

	}

	private void writeInfo(String s) {
		logger.info(s);

		if (fonteLog != null) {
			fonteLog.info(s);
		}
	}

	private void writeDebug(String s) {
		logger.debug(s);
	}

	private void writeWarning(String s) {
		logger.warn(s);
	}

	private void writeError(String s) {
		cntErrors++;
		logger.error(s);

		if (fonteLog != null) {
			fonteLog.error(s);
		}
	}

	private DocerServicesStub fonteDocerWSClient = null;
	private AuthenticationServiceStub fonteAuthWSClient;
	private RerServiziPerLeFontiServiceStub rerServiziPerLeFontiServiceClient = null;

	private boolean initWSClientsFonte() {

		try {
			fonteAuthWSClient = getAuthenticationClient(ws_fonte_auth_url);
		}
		catch (AxisFault e) {
			writeError("errore inizializzazione fonteAuthWSClient: " + e.getMessage());
			return false;
		}

		try {
			fonteDocerWSClient = getDocerServicesClient(ws_fonte_url);
		}
		catch (AxisFault e) {
			writeError("errore inizializzazione fonteDocerWSClient: " + e.getMessage());
			return false;
		}

		writeDebug("inizializzati WSClients Fonte");
		return true;
	}

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

	private String loginWSFonte() throws Exception {

		if (!ws_fonte_auth_url.equals("")) {

			Login login = new Login();
			login.setUsername(ws_fonte_login_userid);
			login.setPassword(ws_fonte_login_pwd);
			login.setCodiceEnte(ws_fonte_login_cod_ente);
			login.setApplication("");

			writeDebug("esecuzione login WS Fonte auth su " + fonteAuthWSClient._getServiceClient().getOptions().getTo());
			LoginResponse resp = fonteAuthWSClient.login(login);
			return resp.get_return();
		}
		else {
			DocerServicesStub.Login login = new DocerServicesStub.Login();

			login.setUserId(ws_fonte_login_userid);
			login.setPassword(ws_fonte_login_pwd);
			login.setCodiceEnte(ws_fonte_login_cod_ente);

			writeDebug("esecuzione login WS Fonte su " + fonteDocerWSClient._getServiceClient().getOptions().getTo());
			DocerServicesStub.LoginResponse loginRes = fonteDocerWSClient.login(login);
			return loginRes.get_return();
		}

	}

	private void logoutWSFonte() {

		if (ticket_ws_fonte == null) {
			return;
		}

		if (!ws_fonte_auth_url.equals("")) {

			Logout logout = new Logout();
			logout.setToken(ticket_ws_fonte);

			try {
				fonteAuthWSClient.logout(logout);
			}
			catch (Exception e) {
			}
		}
		else {
			DocerServicesStub.Logout logout = new DocerServicesStub.Logout();

			logout.setToken(ticket_ws_fonte);

			try {
				fonteDocerWSClient.logout(logout);
			}
			catch (Exception e) {

			}
		}

		writeDebug("logout WSFonte effettuato");

	}

	private boolean initBatch() {
		if (!initWSClientsFonte()) {
			return false;
		}

		try {
			ticket_ws_fonte = loginWSFonte();
			writeDebug("recuperato ticket_ws_fonte: " + ticket_ws_fonte);
		}
		catch (Exception e) {
			writeError("Errore recupero ticket_ws_fonte: " + e.getMessage());
			return false;
		}
		if (ticket_ws_fonte == null) {
			writeError("Errore recupero ticket_ws_fonte: ticket vuoto");
			return false;
		}

		return true;
	}

	private OrderedList<String> getFontiConfigurate() {

		OrderedList<String> fontiConfigurate = new OrderedList<String>();

		try {
			sftpConfig = new SFTPModeConfiguration(config);
			for (int i = 0; i < sftpConfig.getFonti().size(); i++) {
				if (fontiConfigurate.contains(sftpConfig.getFonti().get(i))) {
					continue;
				}
				fontiConfigurate.add(sftpConfig.getFonti().get(i));
			}
		}
		catch (Exception e) {
			writeError(e.getMessage());
		}

		try {
			soapConfig = new SOAPModeConfiguration(config);
			for (int i = 0; i < soapConfig.getFonti().size(); i++) {
				if (fontiConfigurate.contains(soapConfig.getFonti().get(i))) {
					continue;
				}
				fontiConfigurate.add(soapConfig.getFonti().get(i));
			}
		}
		catch (Exception e) {
			writeError(e.getMessage());
		}

		return fontiConfigurate;

	}

	private SFTPModeConfiguration sftpConfig = null;
	private SOAPModeConfiguration soapConfig = null;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		sftpConfig = null;
		soapConfig = null;

		writeInfo("-----------------------------------------");
		writeInfo("-----------------------------------------");
		writeInfo("Esecuzione Batch Popolamento Raccoglitore");

		if (!initBatch()) {
			severe_error = true;
		}
		else{
			//writeInfo("Esecuzione purgeMemberhips...");
		 	//writeInfo("purged " +purgeMemberhips() +" users");				
		}

		
		OrderedList<String> fontiConfigurate = getFontiConfigurate();

		writeInfo(fontiConfigurate.size() + " fonti configurate: " + fontiConfigurate.toString());

		for (int i = 0; i < fontiConfigurate.size(); i++) {

			cntFascicoliFound = 0;
			cntDocumentiFascicolatiFound = 0;
			cntDocumentiNonfascicolatiFound = 0;
			cntXmlUploaded = 0;
			cntErrors = 0;

			String id_fonte = fontiConfigurate.get(i);

			if (!severe_error) {
				try {
					writeInfo("");
					fonteLog = new BatchLog(cacheDir, id_fonte);
					writeInfo("Esecuzione Batch Popolamento Raccoglitore per fonte " + id_fonte + " del giorno " + new DateTime().toString("dd-MM-yyyy"));

					writeInfo("Elaborazione Fonte " + id_fonte);

					OrderedList<FascicoloFlusso> flussiFascicoli = buildFlussiFascicoli(id_fonte);

					if (flussiFascicoli.size() > 0) {
						if (xml_transfer_mode.equalsIgnoreCase("sftp") || xml_transfer_mode.equalsIgnoreCase("soap")) {
							uploadFlussiFascicoli(flussiFascicoli);
						}
						else {
							writeInfo("Fonte " + id_fonte + ": upload flussi XML Fascicoli non effettuata perche' xml_transfer_mode=" + xml_transfer_mode);
						}
					}

					OrderedList<DocumentoFlusso> flussiDocumentiNonFascicolati = buildFlussiDocumentiNonFascicolati(id_fonte);

					if (flussiDocumentiNonFascicolati.size() > 0) {
						if (xml_transfer_mode.equalsIgnoreCase("sftp") || xml_transfer_mode.equalsIgnoreCase("soap")) {
							uploadFlussiDocumenti(flussiDocumentiNonFascicolati);
						}
						else {
							writeInfo("Fonte " + id_fonte + ": upload flussi XML Documenti Non Fascicolati non effettuata perche' xml_transfer_mode=" + xml_transfer_mode);
						}
					}

				}
				catch (Exception e) {
					writeError(e.getMessage());
				}
			}
			else {
				cntErrors = 1;
			}

			sendMail(id_fonte);
			if (fonteLog != null) {
				fonteLog.Close();
				fonteLog.deleteLogFile();
				fonteLog = null;
			}

		}

		logoutWSFonte();

	}

	private void uploadFlussiFascicoli(OrderedList<FascicoloFlusso> flussiFascicoli) {

		for (int k = 0; k < flussiFascicoli.size(); k++) {
			FascicoloFlusso ff = flussiFascicoli.get(k);

			String id_fonte = ff.getIdFonte();

			writeInfo("trasmissione flusso " + ff.getIdFlusso() + " al Raccoglitore in modalita' " + xml_transfer_mode);

			try {
				if (xml_transfer_mode.equalsIgnoreCase("sftp")) {
					sendViaSFTP(ff.getFileXml(), sftpConfig.getSFTPFolderHost(id_fonte), sftpConfig.getSFTPFolderPort(id_fonte), sftpConfig.getSFTPFolderUserid(id_fonte), sftpConfig.getSFTPFolderPassword(id_fonte), sftpConfig.getSFTPFolderName(id_fonte));
				}
				else if (xml_transfer_mode.equalsIgnoreCase("soap")) {
					sendViaSOAP(ff.getFileXml(), soapConfig.getUrl(), soapConfig.getOntology(), id_fonte, soapConfig.getTokenByFonte(id_fonte));
				}

				writeInfo("trasmissione effettuata con successo");
				cntXmlUploaded++;

				if (delete_xml_after_upload) {
					ff.getFileXml().delete();
				}

				// aggiorno il flag sulla fonte
				try {
					updateFascicoloFlagUpdate(ff.getIdFascicolo());
				}
				catch (Exception e) {
					writeError("updateFascicoloFlagUpdate: id_flusso=" + ff.getIdFlusso() + "; fascicolo=" + composeIdFascicolo(ff.getIdFascicolo()) + ": " + e.getMessage());
				}

			}
			catch (Exception e) {
				writeError(e.getMessage());
			}

		}

	}

	private void uploadFlussiDocumenti(OrderedList<DocumentoFlusso> flussiDocumenti) {

		for (int k = 0; k < flussiDocumenti.size(); k++) {
			DocumentoFlusso df = flussiDocumenti.get(k);

			String id_fonte = df.getIdFonte();

			writeInfo("trasmissione flusso " + df.getIdFlusso() + " al Raccoglitore in modalita' " + xml_transfer_mode);

			try {
				if (xml_transfer_mode.equalsIgnoreCase("sftp")) {
					sendViaSFTP(df.getFileXml(), sftpConfig.getSFTPFolderHost(id_fonte), sftpConfig.getSFTPFolderPort(id_fonte), sftpConfig.getSFTPFolderUserid(id_fonte), sftpConfig.getSFTPFolderPassword(id_fonte), sftpConfig.getSFTPFolderName(id_fonte));
				}
				else if (xml_transfer_mode.equalsIgnoreCase("soap")) {
					sendViaSOAP(df.getFileXml(), soapConfig.getUrl(), soapConfig.getOntology(), id_fonte, soapConfig.getTokenByFonte(id_fonte));
				}

				writeInfo("trasmissione effettuata con successo");
				cntXmlUploaded++;

				if (delete_xml_after_upload) {
					df.getFileXml().delete();
				}

				// aggiorno il flag sulla fonte
				try {
					updateDocumentoFlagUpdate(df.getIdDoc());
				}
				catch (Exception e) {
					writeError("updateFascicoloFlagUpdate: id_flusso=" + df.getIdFlusso() + "; documento=" + df.getIdDoc() + ": " + e.getMessage());
				}

			}
			catch (Exception e) {
				writeError(e.getMessage());
			}

		}

	}

	//private void addDocumentiToXML(SearchItem[] docsPrimariFascicolo, OMElement body, OMElement relazioniFascicoloNode, String racc_uid_fascicolo, List<String> raccUids) throws Exception {
	private void addDocumentiToXML(String id_fonte, SearchItem[] docsPrimariFascicolo, OMElement body, OrderedList<OMElement> relazioniFascicolo, String racc_uid_fascicolo, List<String> raccUids) throws Exception {

		for (SearchItem docs1 : docsPrimariFascicolo) {

			String docId = null;

			for (KeyValuePair pair : docs1.getMetadata()) {

				if (pair.getKey().equalsIgnoreCase("DOCNUM")) {
					docId = pair.getValue();
					break;
				}
			}

			if (StringUtils.isEmpty(docId)) {
				throw new Exception("metadato DOCNUM non presente; type_id: documento; metodo: fonteDocerWSClient.searchDocuments()");
			}

			Map<String, String> profiloDocumento = getProfiloDocumento(docId);

			String statoArchivistico = profiloDocumento.get("STATO_ARCHIVISTICO");

			if (StringUtils.isEmpty(statoArchivistico) || statoArchivistico.equals("0") || statoArchivistico.equals("1")) {
				writeError("documento " + docId + " ha STATO_ARCHIVISTICO=" + statoArchivistico + " e sara' ignorato");
				continue;
			}

			String racc_uid = profiloDocumento.get("RACC_UID");
			if (StringUtils.isEmpty(racc_uid)) {
				throw new Exception("metadato RACC_UID non presente; type_id: documento; metodo: fonteDocerWSClient.getProfileDocument()");
			}

			if (raccUids.contains(racc_uid)) {
				writeInfo("documento " + docId + " ha RACC_UID che e' gia' stato incluso nell'XML: RACC_UID=" + racc_uid);
				continue;
			}

			raccUids.add(racc_uid);

			OMElement notificationDocumento = buildDocumentoFascicolatoNotification(profiloDocumento, racc_uid_fascicolo);

			// aggiungo le relazioni

			List<String> gruppi = new ArrayList<String>();
			String gruppoOperatoriPa = generateGroupIdOperatoriPa("operatore_pa", "fonte", id_fonte);
			gruppi.add(gruppoOperatoriPa);
			
			if (relazioniFascicolo.size() > 0) {
				OMElement relazioniDocumentoNode = xPathSelectSingleNode("//record/commonFields/relazioni", notificationDocumento);
				if (relazioniDocumentoNode == null) {
					relazioniDocumentoNode = factory.createOMElement("relazioni", null);
					OMElement descrizioneDocumentoNode = xPathSelectSingleNode("//record/commonFields/descrizione", notificationDocumento);
					descrizioneDocumentoNode.insertSiblingAfter(relazioniDocumentoNode);
				}

				if (relazioniFascicolo.size() > 0) {
					// aggiungo le relazioni del fascicolo al documento
					for (int i = 0; i < relazioniFascicolo.size(); i++) {
						OMElement relazione = relazioniFascicolo.get(i);
						relazioniDocumentoNode.addChild(relazione.cloneOMElement());
					}

				}

				OrderedList<OMElement> relazioniDocumento = xPathSelectNodes("//record/commonFields/relazioni/relazione", notificationDocumento);
				if (relazioniDocumento.size() > 0) {
					
					// leggo le relazioni finali del documento
					for (int i = 0; i < relazioniDocumento.size(); i++) {
						OMElement relazione = relazioniDocumento.get(i);
						String groupId = generateGroupIdFromRelazioneElement(relazione);
						gruppi.add(groupId);
					}
					
				}

			}
			
			if(gruppi.size()>0){
				setACLDocumento(racc_uid, gruppi);
				writeDebug("documento " + racc_uid + " assegnati diritti readOnly ai gruppi " + gruppi.toString());			    
			}

			body.addChild(notificationDocumento);

			writeInfo("aggiunto documento " + racc_uid);

		}
	}

	private static String templateGroupIdReazione = "RLZN_%s_%s_%s";

	private String generateGroupIdFromRelazioneElement(OMElement relazioneElement) {

		OMAttribute tipoRelazioneAttr = relazioneElement.getAttribute(new QName(null, "tipoRelazione"));
		OMAttribute tipoIdAttr = relazioneElement.getAttribute(new QName(null, "tipoId"));
		String tipoRelazione = null;
		String tipoId = null;
		if (tipoRelazioneAttr != null) {
			tipoRelazione = tipoRelazioneAttr.getAttributeValue();
		}
		if (tipoIdAttr != null) {
			tipoId = tipoIdAttr.getAttributeValue();
		}

		return String.format(templateGroupIdReazione, tipoRelazione.toUpperCase(), tipoId.toUpperCase(), relazioneElement.getText().toUpperCase());

	}
	
	private String generateGroupIdOperatoriPa(String tipoRelazione, String tipoId, String idFonte) {		

		return String.format(templateGroupIdReazione, tipoRelazione.toUpperCase(), tipoId.toUpperCase(), idFonte.toUpperCase());
	}

	private SearchItem[] getFascicoliToNotify(String id_fonte) throws Exception {
		SearchAnagrafiche arg = new SearchAnagrafiche();
		arg.setToken(ticket_ws_fonte);
		arg.setType("FASCICOLO");

		KeyValuePair kvpIdFonte = new KeyValuePair();
		kvpIdFonte.setKey("ID_FONTE");
		kvpIdFonte.setValue(id_fonte);
		arg.addSearchCriteria(kvpIdFonte);

		KeyValuePair kvpFlag = new KeyValuePair();
		kvpFlag.setKey("NEEDS_RACCOGLITORE_UPDATE");
		kvpFlag.setValue("true");
		arg.addSearchCriteria(kvpFlag);

		SearchAnagraficheResponse searchResponse;
		try {
			searchResponse = fonteDocerWSClient.searchAnagrafiche(arg);
		}
		catch (Exception e) {
			throw new Exception("Errore ricerca Fascicoli da aggiornare per fonte: " + id_fonte + ": " + e.getMessage());
		}

		return searchResponse.get_return();
	}

	private OrderedList<FascicoloFlusso> buildFlussiFascicoli(String id_fonte) throws Exception {

		OrderedList<FascicoloFlusso> flussiFascicoliList = new OrderedList<FascicoloFlusso>();

		List<String> raccUids = new ArrayList<String>();

		SearchItem[] results = getFascicoliToNotify(id_fonte);

		if (results == null || results.length == 0) {
			writeInfo("Trovati 0 fascicoli da inviare al raccoglitore per fonte " + id_fonte);
			return flussiFascicoliList;
		}

		int n = results.length;

		writeInfo("Trovati " + n + " fascicoli da inviare al raccoglitore per fonte " + id_fonte);

		cntFascicoliFound = n;

		for (SearchItem f : results) {

			Date time = Calendar.getInstance().getTime();
			String timestamp = String.format("%sT%s", date_formatter.format(time), time_formatter.format(time));

			String id_flusso = generateIdFlusso(time);
			writeInfo("ID_FLUSSO: " + id_flusso);

			try {

				raccUids.clear();

				keywordsFascicoloNode = null;

				// ID FASCICOLO
				KeyValuePair[] fmetadata = f.getMetadata();
				KeyValuePair[] fascicoloId = getFascicoloID(fmetadata);
				KeyValuePair[] fascicoloSecondarioCriteria = getFascicoliSecondariCriteria(fmetadata);

				String strIdFascicolo = composeIdFascicolo(fascicoloId);

				writeInfo("elaborazione fascicolo: " + strIdFascicolo);

				Map<String, String> profiloFascicolo = getProfiloFascicolo(fascicoloId);

				String fascicolo_id_fonte = profiloFascicolo.get("ID_FONTE");

				if (fascicolo_id_fonte == null || !fascicolo_id_fonte.equals(id_fonte)) {
					writeError("il fascicolo " + strIdFascicolo + " ha ID_FONTE non rispondente alla ricerca effettuata: " + fascicolo_id_fonte + " <> " + id_fonte);
					continue;
				}

				String racc_uid_fascicolo = profiloFascicolo.get("RACC_UID");
				if (racc_uid_fascicolo == null || racc_uid_fascicolo.equals("")) {
					writeError("il fascicolo " + profiloFascicolo.toString() + " non ha RACC_UID assegnato");
					continue;
				}

				profiloFascicolo.put("ID_FLUSSO", id_flusso);

				OMElement xmlfile = getXmlTemplate("/schema/mod-header.xml");

				buildNode(xmlfile, "//p:rer-import-docer/importHeader/id-fonte").setText(id_fonte);
				buildNode(xmlfile, "//p:rer-import-docer/importHeader/id-flusso").setText(id_flusso);
				buildNode(xmlfile, "//p:rer-import-docer/importHeader/timestamp-flusso").setText(timestamp);

				// BODY FASCICOLO
				String codEnte = profiloFascicolo.get("COD_ENTE");
				String codAoo = profiloFascicolo.get("COD_AOO");

				String desEnte = getDesEnte(codEnte);

				profiloFascicolo.put("DES_ENTE", desEnte);

				String desAoo = getDesAOO(codEnte, codAoo);

				profiloFascicolo.put("DES_AOO", desAoo);

				OMElement body = buildNode(xmlfile, "//importBody");

				OMElement notificationFascicolo = buildFascicoloNode(profiloFascicolo);

				body.addChild(notificationFascicolo);

				//OMElement relazioniFascicoloNode = xPathSelectSingleNode("//record/commonFields/relazioni", notificationFascicolo);
				OrderedList<OMElement> relazioniFascicolo = xPathSelectNodes("//record/commonFields/relazioni/relazione", notificationFascicolo);

				OMElement descrizioneFascicoloNode = xPathSelectSingleNode("//record/commonFields/descrizione", notificationFascicolo);

				// DOCUMENTI DEL FASCICOLO DA AGGIUNGERE ALL'XML
				SearchItem[] docsPrimariFascicolo = getDocumentiFascicolo(id_fonte, fascicoloId);

				if (docsPrimariFascicolo == null || docsPrimariFascicolo.length == 0) {
					writeInfo("Il fascicolo non ha documenti primari record");
				}
				else {
					// int dcnt = 0;
					writeInfo("Il fascicolo ha " + docsPrimariFascicolo.length + " documenti primari");

					cntDocumentiFascicolatiFound += docsPrimariFascicolo.length;

					addDocumentiToXML(id_fonte, docsPrimariFascicolo, body, relazioniFascicolo, racc_uid_fascicolo, raccUids);
				}

				SearchItem[] docsSecondariFascicolo = getDocumentiFascicolo(id_fonte, fascicoloSecondarioCriteria);
				if (docsSecondariFascicolo == null || docsSecondariFascicolo.length == 0) {
					writeInfo("Il fascicolo non ha documenti secondari record");
				}
				else {

					writeInfo("Il fascicolo ha " + docsSecondariFascicolo.length + " documenti secondari");
					cntDocumentiFascicolatiFound += docsSecondariFascicolo.length;
					addDocumentiToXML(id_fonte, docsSecondariFascicolo, body, relazioniFascicolo, racc_uid_fascicolo, raccUids);

				}

				if (keywordsFascicoloNode != null) {
					descrizioneFascicoloNode.insertSiblingAfter(keywordsFascicoloNode);
				}

				String outxml = xmlfile.toString();

				/* GESTIONE CACHE FILE XML SINGOLO FLUSSO */
				File fonteDir = DirectoryUtils.createSubDirectory(id_fonte, cacheDir);

				String fileName = id_fonte + "~" + id_flusso + ".xml";

				File xmlFile = toXML(fonteDir, fileName, outxml);
				writeInfo("creato XML " + xmlFile.getAbsolutePath() + "; id_flusso=" + id_flusso + "; id_fonte=" + id_fonte);

				try {
					validateXML(outxml);
					FascicoloFlusso ff = new FascicoloFlusso();
					ff.setIdFonte(id_fonte);
					ff.setIdFlusso(id_flusso);
					ff.setIdFascicolo(fascicoloId);
					ff.setFileXml(xmlFile);

					flussiFascicoliList.add(ff);
				}
				catch (Exception e) {
					File dest = new File(xmlFile.getAbsolutePath() + ".x");
					xmlFile.renameTo(dest);
					throw e;
				}

				/* FINE XML SINGOLO FLUSSO */
			}
			catch (Exception e) {
				writeError("errore flusso " + id_flusso + " per la fonte " + id_fonte + ": " + e.getMessage());
				cntErrors++;
				continue;
			}

		}

		return flussiFascicoliList;
	}

	private OrderedList<DocumentoFlusso> buildFlussiDocumentiNonFascicolati(String id_fonte) throws Exception {

		cntDocumentiNonfascicolatiFound = 0;

		OrderedList<DocumentoFlusso> flussiDocumentiNonFascicolati = new OrderedList<DocumentoFlusso>();

		SearchItem[] docsNonFascicolati = getDocumentiNonFascicolati(id_fonte);

		if (docsNonFascicolati == null || docsNonFascicolati.length == 0) {
			writeInfo("Trovati 0 Documenti non fascicolati da inviare al raccoglitore");
			return flussiDocumentiNonFascicolati;
		}

		List<String> raccUids = new ArrayList<String>();

		writeInfo("Trovati " + docsNonFascicolati.length + " documenti non fascicolati da inviare al raccoglitore");

		cntDocumentiNonfascicolatiFound = docsNonFascicolati.length;
		for (SearchItem dnf : docsNonFascicolati) {

			Date time = Calendar.getInstance().getTime();
			String timestamp = String.format("%sT%s", date_formatter.format(time), time_formatter.format(time));

			String id_flusso = generateIdFlusso(time);

			try {

				boolean b1 = false;
				boolean b2 = false;
				boolean b3 = false;

				String docId = null;
				String statoArchivistico = null;
				String racc_uid = null;

				for (KeyValuePair pair : dnf.getMetadata()) {

					if (b1 && b2 && b3) {
						break;
					}

					if (pair.getKey().equalsIgnoreCase("DOCNUM")) {
						docId = pair.getValue();
						b1 = true;
						continue;
					}
					if (pair.getKey().equalsIgnoreCase("STATO_ARCHIVISTICO")) {
						statoArchivistico = pair.getValue();
						b2 = true;
						continue;
					}
					if (pair.getKey().equalsIgnoreCase("RACC_UID")) {
						racc_uid = pair.getValue();
						b3 = true;
						continue;
					}
				}

				if (StringUtils.isEmpty(docId)) {
					throw new Exception("metadato DOCNUM non presente; type_id: documento non fascicolato; metodo: fonteDocerWSClient.searchDocuments()");
				}

				if (StringUtils.isEmpty(statoArchivistico) || statoArchivistico.equals("0") || statoArchivistico.equals("1")) {
					writeError("documento non fascicolato " + docId + " ha STATO_ARCHIVISTICO=" + statoArchivistico + " e sara' ignorato");
					continue;
				}

				if (StringUtils.isEmpty(docId)) {
					throw new Exception("metadato RACC_UID non presente; type_id: documento non fascicolato; metodo: fonteDocerWSClient.searchDocuments()");
				}

				if (raccUids.contains(racc_uid)) {
					writeInfo("documento non fascicolato " + docId + " ha RACC_UID che e' gia' stato incluso nell'XML: RACC_UID=" + racc_uid);
					continue;
				}

				writeInfo("elaborazione documento non fascicolato: " + docId);

				Map<String, String> profiloDocumento = getProfiloDocumento(docId);

				profiloDocumento.put("ID_FLUSSO", id_flusso);

				OMElement xmlfile = getXmlTemplate("/schema/mod-header.xml");

				buildNode(xmlfile, "//p:rer-import-docer/importHeader/id-fonte").setText(id_fonte);
				buildNode(xmlfile, "//p:rer-import-docer/importHeader/id-flusso").setText(id_flusso);
				buildNode(xmlfile, "//p:rer-import-docer/importHeader/timestamp-flusso").setText(timestamp);

				OMElement body = buildNode(xmlfile, "//importBody");

				// Aggiorno le variabili di flusso
				String documento_id_fonte = profiloDocumento.get("ID_FONTE");
				if (!documento_id_fonte.equals(id_fonte)) {
					writeError("il documento " + docId + " ha ID_FONTE non rispondente alla ricerca effettuata: " + documento_id_fonte + " <> " + id_fonte);
					continue;
				}

				String racc_uid_documento = profiloDocumento.get("RACC_UID");
				if (racc_uid_documento == null || racc_uid_documento.equals("")) {
					writeError("il documento " + docId + " non ha RACC_UID assegnato");
					continue;
				}

				OMElement notificationDocumento = buildDocumentoNonFascicolatoNotification(profiloDocumento);

				body.addChild(notificationDocumento);

				String outxml = xmlfile.toString();

				/* GESTIONE CACHE FILE XML SINGOLO FLUSSO */
				File fonteDir = DirectoryUtils.createSubDirectory(id_fonte, cacheDir);

				String fileName = id_fonte + "~" + id_flusso + ".xml";

				File xmlFile = toXML(fonteDir, fileName, outxml);
				writeInfo("creato XML: id_flusso=" + id_flusso + "; id_fonte=" + id_fonte);

				try {
					validateXML(outxml);

					DocumentoFlusso df = new DocumentoFlusso();
					df.setIdDoc(docId);
					df.setIdFlusso(id_flusso);
					df.setIdFonte(id_fonte);
					df.setFileXml(xmlFile);

					flussiDocumentiNonFascicolati.add(df);
				}
				catch (Exception e) {
					File dest = new File(xmlFile.getAbsolutePath() + ".x");
					xmlFile.renameTo(dest);
					throw e;
				}

				/* FINE XML SINGOLO FLUSSO */
			}
			catch (Exception e) {
				writeError("errore flusso " + id_flusso + " per la fonte " + id_fonte + ": " + e.getMessage());
				cntErrors++;
				continue;
			}

		} // FINE CICLO FOR documenti non fascicolati

		return flussiDocumentiNonFascicolati;
	}

	private void sendMail(String id_fonte) {

		File fileToSend = null;

		String message = "Resoconto Batch Popolamento Raccoglitore\n\t\tFonte: " + id_fonte + "\n\t\tFascicoli trovati: " + cntFascicoliFound + "\n\t\tDocumenti Fascicolati trovati: " + cntDocumentiFascicolatiFound
				+ "\n\t\tDocumenti Non fascicolati trovati: " + cntDocumentiNonfascicolatiFound + "\n\t\tXML trasmessi: " + cntXmlUploaded + "\n\t\tErrori: " + cntErrors;

		if (severe_error) {
			message = "Resoconto Batch Popolamento Raccoglitore: fonte " + id_fonte + ": errore grave nel setup del batch o nei login ai WS";
			fileToSend = Log4jUtils.getLogFile(logger, "F");
		}
		else {
			fileToSend = fonteLog.getLog();
		}

		writeInfo(message);
		lastMessage = message;
		try {
			EmailFactory emf = new EmailFactory(config);
			String priority = "Low priority";
			if (cntErrors > 0) {
				priority = "High priority";
			}

			boolean sent = emf.sendMultiPartEmailMail(id_fonte, message, fileToSend, "Batch Popolamento Raccoglitore", priority);
			if (sent) {
				writeInfo("e-mail resoconto inviata");
			}
			else {
				writeInfo("e-mail resoconto NON inviata");
			}
		}
		catch (Exception e) {
			writeError("errore invio email resoconto: " + e.getMessage());
		}
	}

	private void updateFascicoloFlagUpdate(KeyValuePair[] fascicoloId) throws RemoteException, DocerExceptionException {

		UpdateFascicolo updateFascicolo = new UpdateFascicolo();
		updateFascicolo.setToken(ticket_ws_fonte);
		updateFascicolo.setFascicoloId(fascicoloId);

		KeyValuePair kvp = new KeyValuePair();
		kvp.setKey("NEEDS_RACCOGLITORE_UPDATE");
		kvp.setValue("false");
		KeyValuePair[] fascicoloInfo = new KeyValuePair[] { kvp };

		updateFascicolo.setFascicoloInfo(fascicoloInfo);

		fonteDocerWSClient.updateFascicolo(updateFascicolo);

	}

	private void updateDocumentoFlagUpdate(String docId) throws RemoteException, DocerExceptionException {

		UpdateProfileDocument updateProfileDocument = new UpdateProfileDocument();
		updateProfileDocument.setToken(ticket_ws_fonte);
		updateProfileDocument.setDocId(docId);

		KeyValuePair kvp = new KeyValuePair();
		kvp.setKey("NEEDS_RACCOGLITORE_UPDATE");
		kvp.setValue("false");
		KeyValuePair[] metadata = new KeyValuePair[] { kvp };

		updateProfileDocument.setMetadata(metadata);

		fonteDocerWSClient.updateProfileDocument(updateProfileDocument);

	}

	//    private String extractValueFromKey(KeyValuePair[] metadata, String key, String type_id, String methodName, boolean exceptionIfMissing) throws Exception {
	//
	//        for (KeyValuePair pair : metadata) {
	//            if (pair.getKey().equalsIgnoreCase(key)) {
	//                return pair.getValue();
	//            }
	//        }
	//
	//        if (exceptionIfMissing) {
	//            throw new Exception("metadato " + key + " non presente; type_id: " + type_id + "; metodo: " + methodName);
	//        }
	//        else {
	//            writeError("metadato " + key + " non presente; type_id: " + type_id + "; metodo: " + methodName);
	//        }
	//        return null;
	//    }

	public static String stripInvalidXmlCharacters(String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (XML11Char.isXML11Valid(c)) {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	private String generateIdFlusso(Date time) {

		dummy_formatter = new SimpleDateFormat(today_format);

		StringBuilder id_flusso = new StringBuilder();
		id_flusso = new StringBuilder();
		id_flusso.append("F");
		id_flusso.append(flow_formatter.format(time));

		return id_flusso.toString();
	}

	private SearchItem[] getDocumentiFascicolo(String id_fonte, KeyValuePair[] fascicoloId) throws RemoteException, DocerExceptionException {

		List<KeyValuePair> docCriteria = new ArrayList<KeyValuePair>();
		for (KeyValuePair kvp : fascicoloId) {
			docCriteria.add(kvp);
		}

		KeyValuePair kvpIdFonte = new KeyValuePair();
		kvpIdFonte.setKey("ID_FONTE");
		kvpIdFonte.setValue(id_fonte);
		docCriteria.add(kvpIdFonte);

		// devo cercare solo i records
		KeyValuePair kvpSA2 = new KeyValuePair();
		kvpSA2.setKey("STATO_ARCHIVISTICO");
		kvpSA2.setValue("2");
		docCriteria.add(kvpSA2);

		KeyValuePair kvpSA3 = new KeyValuePair();
		kvpSA3.setKey("STATO_ARCHIVISTICO");
		kvpSA3.setValue("3");
		docCriteria.add(kvpSA3);

		KeyValuePair kvpSA4 = new KeyValuePair();
		kvpSA4.setKey("STATO_ARCHIVISTICO");
		kvpSA4.setValue("4");
		docCriteria.add(kvpSA4);

		KeyValuePair kvpSA5 = new KeyValuePair();
		kvpSA5.setKey("STATO_ARCHIVISTICO");
		kvpSA5.setValue("5");
		docCriteria.add(kvpSA5);

		KeyValuePair kvpSA6 = new KeyValuePair();
		kvpSA6.setKey("STATO_ARCHIVISTICO");
		kvpSA6.setValue("6");
		docCriteria.add(kvpSA6);

		KeyValuePair kvpPrincipale = new KeyValuePair();
		kvpPrincipale.setKey("TIPO_COMPONENTE");
		kvpPrincipale.setValue("PRINCIPALE");
		docCriteria.add(kvpPrincipale);

		SearchDocuments darg = new SearchDocuments();
		darg.setToken(ticket_ws_fonte);
		darg.setMaxRows(primarysearch_max_results);
		darg.setKeywords(new String[] { "" });
		darg.setSearchCriteria(docCriteria.toArray(new KeyValuePair[0]));

		SearchDocumentsResponse docsResponse = fonteDocerWSClient.searchDocuments(darg);
		SearchItem[] results = docsResponse.get_return();

		return results;

	}

	private SearchItem[] getDocumentiNonFascicolati(String id_fonte) throws RemoteException, DocerExceptionException {

		List<KeyValuePair> docCriteria = new ArrayList<KeyValuePair>();

		KeyValuePair kvpIdFonte = new KeyValuePair();
		kvpIdFonte.setKey("ID_FONTE");
		kvpIdFonte.setValue(id_fonte);
		docCriteria.add(kvpIdFonte);

		KeyValuePair kvpFlag = new KeyValuePair();
		kvpFlag.setKey("NEEDS_RACCOGLITORE_UPDATE");
		kvpFlag.setValue("true");
		docCriteria.add(kvpFlag);

		KeyValuePair kvpNoFascicolo = new KeyValuePair();
		kvpNoFascicolo.setKey("PROGR_FASCICOLO");
		kvpNoFascicolo.setValue("");
		docCriteria.add(kvpNoFascicolo);

		KeyValuePair kvpNoFascicoliSecondari = new KeyValuePair();
		kvpNoFascicoliSecondari.setKey("FASC_SECONDARI");
		kvpNoFascicoliSecondari.setValue("");
		docCriteria.add(kvpNoFascicoliSecondari);

		// devo cercare solo i records
		KeyValuePair kvpSA2 = new KeyValuePair();
		kvpSA2.setKey("STATO_ARCHIVISTICO");
		kvpSA2.setValue("2");
		docCriteria.add(kvpSA2);

		KeyValuePair kvpSA3 = new KeyValuePair();
		kvpSA3.setKey("STATO_ARCHIVISTICO");
		kvpSA3.setValue("3");
		docCriteria.add(kvpSA3);

		KeyValuePair kvpSA4 = new KeyValuePair();
		kvpSA4.setKey("STATO_ARCHIVISTICO");
		kvpSA4.setValue("4");
		docCriteria.add(kvpSA4);

		KeyValuePair kvpSA5 = new KeyValuePair();
		kvpSA5.setKey("STATO_ARCHIVISTICO");
		kvpSA5.setValue("5");
		docCriteria.add(kvpSA5);

		KeyValuePair kvpSA6 = new KeyValuePair();
		kvpSA6.setKey("STATO_ARCHIVISTICO");
		kvpSA6.setValue("6");
		docCriteria.add(kvpSA6);

		KeyValuePair kvpPrincipale = new KeyValuePair();
		kvpPrincipale.setKey("TIPO_COMPONENTE");
		kvpPrincipale.setValue("PRINCIPALE");
		docCriteria.add(kvpPrincipale);

		SearchDocuments darg = new SearchDocuments();
		darg.setToken(ticket_ws_fonte);
		darg.setMaxRows(primarysearch_max_results);
		darg.setKeywords(new String[] { "" });
		darg.setSearchCriteria(docCriteria.toArray(new KeyValuePair[0]));

		SearchDocumentsResponse docsResponse = fonteDocerWSClient.searchDocuments(darg);
		SearchItem[] results = docsResponse.get_return();

		return results;

	}

	// private SearchItem[] getDocuments(KeyValuePair[] criteria, String token)
	// throws RemoteException, DocerExceptionException {
	//
	// SearchItem[] results = getDocuments(criteria, token);
	//
	// // racc uid dei documenti primari
	// List<String> raccUidsDocPrimari = new ArrayList<String>();
	//
	// for (SearchItem item : basket) {
	// for (KeyValuePair pair : item.getMetadata()) {
	// if (pair.getKey().equalsIgnoreCase("RACC_UID")) {
	// raccUidsDocPrimari.add(pair.getValue());
	// break;
	// }
	// }
	// }
	//
	// List<SearchItem> raccUidsDocSecondari = new ArrayList<SearchItem>();
	//
	// for (SearchItem item : results) {
	// for (KeyValuePair pair : item.getMetadata()) {
	// if (pair.getKey().equalsIgnoreCase("RACC_UID")) {
	// if (!raccUidsDocPrimari.contains(pair.getValue())) {
	// raccUidsDocSecondari.add(item);
	// }
	// break;
	// }
	// }
	// }
	//
	// return raccUidsDocSecondari.toArray(new SearchItem[0]);
	//
	// }

	//
	// public SearchItem[] trimSearchItems(SearchItem[] in, SearchItem[] basket)
	// {
	//
	// List<String> out = new ArrayList<String>();
	// List<String> UIDs = new ArrayList<String>();
	//
	// for (SearchItem item : basket) {
	// for (KeyValuePair pair : item.getMetadata()) {
	// if ("RACC_UID".equalsIgnoreCase(pair.getKey())) {
	// UIDs.add(pair.getValue());
	// }
	// }
	// }
	//
	// for (SearchItem newitem : in) {
	// for (KeyValuePair pair : newitem.getMetadata()) {
	// if ("RACC_UID".equalsIgnoreCase(pair.getKey())) {
	// if (!UIDs.contains(pair.getValue())) {
	// out.add(newitem);
	// }
	// }
	// }
	// }
	//
	// return (SearchItem[])out.toArray(new SearchItem[out.size()]);
	// }

	private String kvpArrayToString(KeyValuePair[] kvpArray) {

		String str = "";

		for (KeyValuePair kvp : kvpArray) {
			str += kvp.getKey() + "=" + kvp.getValue() + ", ";
		}

		str.replaceAll(", $", "");

		return "[ " + str + " ]";
	}

	private Map<String, String> getProfiloFascicolo(KeyValuePair[] kvpID) throws Exception {

		GetFascicolo getFascicolo = new GetFascicolo();
		getFascicolo.setToken(ticket_ws_fonte);
		getFascicolo.setFascicoloId(kvpID);

		GetFascicoloResponse getFascicoloResponse = fonteDocerWSClient.getFascicolo(getFascicolo);

		KeyValuePair[] fprofilo = getFascicoloResponse.get_return();

		if (fprofilo == null || fprofilo.length == 0) {
			throw new Exception("fascicolo " + kvpArrayToString(kvpID) + " non trovato nel repository Fonte");
		}

		Map<String, String> metadata = new HashMap<String, String>();
		putIntoMap(metadata, fprofilo);

		return metadata;
	}

	private Map<String, String> getProfiloDocumento(String racc_uid) throws Exception {

		GetProfileDocument getProfileDocument = new GetProfileDocument();
		getProfileDocument.setToken(ticket_ws_fonte);
		getProfileDocument.setDocId(racc_uid);

		GetProfileDocumentResponse getProfileDocumentResponse = fonteDocerWSClient.getProfileDocument(getProfileDocument);

		KeyValuePair[] profilo = getProfileDocumentResponse.get_return();

		if (profilo == null || profilo.length == 0) {
			throw new Exception("documento " + racc_uid + " non trovato nel repository Fonte");
		}

		Map<String, String> metadata = new HashMap<String, String>();
		putIntoMap(metadata, profilo);

		return metadata;
	}

	private String getDesAOO(String codEnte, String codAoo) throws RemoteException, DocerExceptionException {

		if (descriptionCache.containsKey(codEnte + ">>" + codAoo)) {
			return descriptionCache.get(codEnte + ">>" + codAoo);
		}

		GetAOO getaoo = new GetAOO();
		getaoo.setToken(ticket_ws_fonte);

		List<KeyValuePair> aooID = new ArrayList<KeyValuePair>();

		KeyValuePair pair = new KeyValuePair();
		pair.setKey("COD_ENTE");
		pair.setValue(codEnte);
		aooID.add(pair);

		pair = new KeyValuePair();
		pair.setKey("COD_AOO");
		pair.setValue(codAoo);
		aooID.add(pair);

		getaoo.setAooId(aooID.toArray(new KeyValuePair[0]));

		GetAOOResponse getAooResponse = fonteDocerWSClient.getAOO(getaoo);

		KeyValuePair[] aooprof = getAooResponse.get_return();

		for (KeyValuePair kvp : aooprof) {
			if (kvp.getKey().equalsIgnoreCase("DES_AOO")) {
				descriptionCache.put(codEnte + ">>" + codAoo, kvp.getValue());
				return kvp.getValue();
			}
		}

		return null;
	}

	private String getDesEnte(String codEnte) throws RemoteException, DocerExceptionException {

		if (descriptionCache.containsKey(codEnte)) {
			return descriptionCache.get(codEnte);
		}

		GetEnte get = new GetEnte();
		get.setToken(ticket_ws_fonte);
		get.setCodiceEnte(codEnte);

		GetEnteResponse resp = fonteDocerWSClient.getEnte(get);

		KeyValuePair[] prof = resp.get_return();

		for (KeyValuePair kvp : prof) {
			if (kvp.getKey().equalsIgnoreCase("DES_ENTE")) {
				descriptionCache.put(codEnte, kvp.getValue());
				return kvp.getValue();
			}
		}

		return null;
	}

//	private long purgeMemberhips() {
//
//		SearchUsers searchUsers = new SearchUsers();
//		searchUsers.setToken(ticket_ws_fonte);
//		
//		UpdateUser updateUser = new UpdateUser();		
//		updateUser.setToken(ticket_ws_fonte);
//
//		
//		KeyValuePair kvpU = new KeyValuePair();
//		kvpU.setKey("EMAIL_ADDRESS");
//		kvpU.setValue("log in da Raccoglitore");
//		searchUsers.addSearchCriteria(kvpU);
//
//		long totalPurged = 0;
//		boolean finish = false;
//		int count = 0; // 1000 cicli di purge ovvero ricerchiamo al massimo 1.000.000 di utenti loggati  nel raccoglitore nel periodo di run
//		while(!finish && count < 1000){
//			SearchUsersResponse searchUsersResponse = null;
//			try {
//				searchUsersResponse = fonteDocerWSClient.searchUsers(searchUsers);
//				count++;
//			}
//			catch (Exception e) {
//				writeWarning("purgeMembership: errore ricerca utenti per reset delle memberships: " + e.getMessage());
//				return totalPurged;
//			}
//
//			if (searchUsersResponse == null || searchUsersResponse.get_return() == null || searchUsersResponse.get_return().length == 0) {
//				return totalPurged;
//			}
//			else {
//				for (SearchItem si : searchUsersResponse.get_return()) {
//					String toPurgeUserId = "";
//					for (KeyValuePair kvp : si.getMetadata()) {
//						if (kvp.getKey().equalsIgnoreCase("USER_ID")) {
//							toPurgeUserId = kvp.getValue();
//							break;
//						}
//					}
//
//					if (StringUtils.isEmpty(toPurgeUserId)) {
//						continue;
//					}
//					
//					if(toPurgeUserId.equals("admin")){
//						continue;
//					}
//					if(toPurgeUserId.equals("sysadmin")){
//						continue;
//					}
//
//					// imposto l'Ente per individuare gli utenti loggati, necessario al batch di purge delle memberships
//					List<String> gruppiDaAssociare = new ArrayList<String>();
//					SetGroupsOfUser setGroupsOfUser = new SetGroupsOfUser();
//					setGroupsOfUser.setToken(ticket_ws_fonte);
//					setGroupsOfUser.setUserId(toPurgeUserId);
//
//					setGroupsOfUser.setGroups(gruppiDaAssociare.toArray(new String[0]));
//
//					try {
//						fonteDocerWSClient.setGroupsOfUser(setGroupsOfUser);
//					}
//					catch (Exception e) {
//						writeWarning("purgeMembership: l'utente " + toPurgeUserId + " non e' stato disassociato dalle memberships: " + e.getMessage());
//					}
//					
//					// annullo email per individuare gli utenti loggati					
//					KeyValuePair kvpMail = new KeyValuePair();
//					kvpMail.setKey("EMAIL_ADDRESS");
//					kvpMail.setValue("");
//					
//					updateUser.setUserInfo(new KeyValuePair[] { kvpMail });
//					updateUser.setUserId(toPurgeUserId);
//					try {
//						fonteDocerWSClient.updateUser(updateUser);
//						totalPurged++;
//					}
//					catch (Exception e) {
//						writeWarning("purgeMembership: l'utente " + toPurgeUserId + " non e' stato resettato: " + e.getMessage());
//					}
//					
//
//				}
//
//			}
//	
//		}
//						
//		return totalPurged;
//	}

	private OMElement buildDocumentoFascicolatoNotification(Map<String, String> metadata, String racc_uid_fasc) throws Exception {
		return buildDocumentoNotification(metadata, racc_uid_fasc);
	}

	private OMElement buildDocumentoNonFascicolatoNotification(Map<String, String> metadata) throws Exception {
		return buildDocumentoNotification(metadata, null);
	}

	private OMElement buildDocumentoNotification(Map<String, String> metadata, String racc_uid_fasc) throws Exception {

		OMElement notification = getXmlTemplate("/schema/notification-documento.xml");
		OMElement entita = buildNode(notification, "//record/EntitaDocumentale/Documento");

		// ATTRIBUTO RECORD
		OMElement record = buildNode(notification, "//record");
		record.addAttribute("uid", metadata.get("RACC_UID"), record.getNamespace());

		/* DATI DAL DATABASE */
		java.util.Date cts = Calendar.getInstance().getTime();
		java.util.Date uts = Calendar.getInstance().getTime();

		OMElement field = buildNode(notification, "//record/commonFields/creation-timestamp");
		field.setText(String.format("%sT%s", date_formatter.format(cts), time_formatter.format(cts)));

		field = buildNode(notification, "//record/commonFields/last-update-timestamp");
		field.setText(String.format("%sT%s", date_formatter.format(uts), time_formatter.format(uts)));

		String fine = metadata.get("DATA_CHIUSURA");
		if (fine != null && !"null".equals(fine) && !"".equals(fine))
			buildNode(notification, "//record/commonFields/dataFineValidita").setText(fine);

		insertNode(notification, "//record/commonFields/codice-ente", metadata.get("COD_ENTE"));

		String descrizione = getDescrizioneText(metadata, true);

		insertNode(notification, "//record/commonFields/descrizione", descrizione);

		insertNode(entita, "//Documento/numero", metadata.get("DOCNUM"));
		insertNode(entita, "//Documento/classifica", metadata.get("CLASSIFICA"));

		insertNode(entita, "//Documento/tipologiaDocumentaria/codice", metadata.get("TYPE_ID"));
		insertNode(entita, "//Documento/tipologiaDocumentaria/denominazione", metadata.get("TYPE_ID_DES"));

		OMElement tipologiaDocumentariaNode = xPathSelectSingleNode("//Documento/tipologiaDocumentaria", entita);

		if (racc_uid_fasc != null) {

			OMElement idFascicoloNode = factory.createOMElement("idFascicolo", null);
			idFascicoloNode.setText(racc_uid_fasc);

			tipologiaDocumentariaNode.insertSiblingAfter(idFascicoloNode);
		}

		field = insertNode(entita, "//Documento/stato", "valido");
		field.addAttribute("statoAttivo", "true", record.getNamespace());

		insertRegistrazioniNode(entita, metadata);

		insertAllegatiNode(entita, metadata);

		insertCustomFieldsNode(entita, metadata, true);

		insertRelazioniNode(entita, metadata, true);

		incrementKeywordsNode(metadata);

		return notification;
	}

	private void insertAllegatiNode(OMElement entita, Map<String, String> profiloDocPrincipale) throws JaxenException, Exception, RemoteException, DocerExceptionException, XMLStreamException {

		OMElement allegati = buildNode(entita, "//Documento/allegati");

		OMElement allegato_doc = getXmlTemplate("/schema/mod-allegato.xml");
		buildNode(allegato_doc, "//nomeFile").setText(profiloDocPrincipale.get("DOCNAME"));

		String docnamePrincipale = profiloDocPrincipale.get("DOCNAME");
		String descrizionePrincipale = profiloDocPrincipale.get("ABSTRACT");
		descrizionePrincipale = descrizionePrincipale == "" ? docnamePrincipale : descrizionePrincipale;
		buildNode(allegato_doc, "//descrizione").setText(descrizionePrincipale);

		String racc_uid_principale = profiloDocPrincipale.get("RACC_UID");
		buildNode(allegato_doc, "//url").setText(racc_uid_principale);

		allegato_doc.addAttribute("documentoPrincipale", "true", allegato_doc.getNamespace());

		allegati.addChild(allegato_doc);

		GetRelatedDocuments darg = new GetRelatedDocuments();
		darg.setToken(ticket_ws_fonte);
		darg.setDocId(profiloDocPrincipale.get("DOCNUM"));
		GetRelatedDocumentsResponse docsResponse = fonteDocerWSClient.getRelatedDocuments(darg);
		String[] ids = docsResponse.get_return();

		if (ids != null && ids.length > 0)
			for (String id : ids) {
				GetProfileDocument getProfileDocument = new GetProfileDocument();
				getProfileDocument.setToken(ticket_ws_fonte);
				getProfileDocument.setDocId(id);

				GetProfileDocumentResponse getProfielDocumentResponse = fonteDocerWSClient.getProfileDocument(getProfileDocument);

				KeyValuePair[] kvpArrRelated = getProfielDocumentResponse.get_return();

				HashMap<String, String> profiloRelated = new HashMap<String, String>();
				putIntoMap(profiloRelated, kvpArrRelated);

				String docnameRelated = profiloRelated.get("DOCNAME");
				String descrizioneRelated = profiloRelated.get("ABSTRACT");
				if (descrizioneRelated == null) {
					descrizioneRelated = "";
				}
				descrizioneRelated = descrizioneRelated == "" ? docnameRelated : descrizioneRelated;

				String racc_uid_related = profiloRelated.get("RACC_UID");

				if (racc_uid_related.equals(racc_uid_principale)) {
					continue;
				}

				OMElement allegato = getXmlTemplate("/schema/mod-allegato.xml");

				buildNode(allegato, "//nomeFile").setText(docnameRelated);
				buildNode(allegato, "//descrizione").setText(descrizioneRelated);
				buildNode(allegato, "//url").setText(racc_uid_related);
				allegato.addAttribute("documentoPrincipale", "false", allegato.getNamespace());

				allegati.addChild(allegato);

			}
		
	}

	//    private void insertRelazioniNodes(OMElement notification, Map<String, String> metadata, boolean isDocument) throws JaxenException, Exception {
	//
	//        String cf_persona = metadata.get("CF_PERSONA");
	//        String cf_azienda = metadata.get("CF_AZIENDA");
	//        // String immobile = getValueOf(metadata, "ID_IMMOBILE");
	//
	//        OMElement relazioni;
	//        if (!"".equals(cf_persona) || !"".equals(cf_azienda)) {
	//            relazioni = buildNode(notification, "//commonFields/relazioni");
	//
	//            if (!"".equals(cf_persona)) {
	//                OMElement persona = factory.createOMElement("relazione", null);
	//                persona.setText(cf_persona);
	//                persona.addAttribute("autorizzazione", "true", persona.getNamespace());
	//                persona.addAttribute("tipoId", "codiceFiscale", persona.getNamespace());
	//                persona.addAttribute("tipoRelazione", "titolare", persona.getNamespace());
	//
	//                relazioni.addChild(persona);
	//            }
	//
	//            if (!"".equals(cf_azienda)) {
	//                OMElement persona = factory.createOMElement("relazione", null);
	//                persona.setText(cf_azienda);
	//                persona.addAttribute("autorizzazione", "true", persona.getNamespace());
	//                persona.addAttribute("tipoId", "codiceFiscale", persona.getNamespace());
	//                persona.addAttribute("tipoRelazione", "titolare", persona.getNamespace());
	//
	//                relazioni.addChild(persona);
	//            }
	//        }
	//
	//        return;
	//
	//    }

	private OMElement buildFascicoloNode(Map<String, String> metadata) throws Exception {

		OMElement notification = getXmlTemplate("/schema/notification-fascicolo.xml");
		OMElement entita = buildNode(notification, "//record/EntitaDocumentale/Fascicolo");

		// ATTRIBUTO RECORD
		OMElement record = buildNode(notification, "//record");
		record.addAttribute("uid", metadata.get("RACC_UID"), record.getNamespace());

		/* DATI DAL DATABASE */
		java.util.Date cts = Calendar.getInstance().getTime();
		java.util.Date uts = Calendar.getInstance().getTime();

		OMElement field = buildNode(notification, "//record/commonFields/creation-timestamp");
		field.setText(String.format("%sT%s", date_formatter.format(cts), time_formatter.format(cts)));

		field = buildNode(notification, "//record/commonFields/last-update-timestamp");
		field.setText(String.format("%sT%s", date_formatter.format(uts), time_formatter.format(uts)));

		String fine = metadata.get("DATA_CHIUSURA");
		if (fine != null && !"null".equals(fine) && !"".equals(fine))
			buildNode(notification, "//record/commonFields/dataFineValidita").setText(fine);

		insertNode(notification, "//record/commonFields/codice-ente", metadata.get("COD_ENTE"));

		String descrizione = getDescrizioneText(metadata, false);

		// descrizione fascicolo
		insertNode(notification, "//record/commonFields/descrizione", descrizione);

		insertNode(entita, "//Fascicolo/numero", metadata.get("NUM_FASCICOLO"));
		insertNode(entita, "//Fascicolo/classifica", metadata.get("CLASSIFICA"));
		insertNode(entita, "//Fascicolo/anno", metadata.get("ANNO_FASCICOLO"));
		insertNode(entita, "//Fascicolo/progressivo", metadata.get("PROGR_FASCICOLO"));

		insertNode(entita, "//Fascicolo/areaOrganizzativaOmogenea/codice", metadata.get("COD_AOO"));
		insertNode(entita, "//Fascicolo/areaOrganizzativaOmogenea/denominazione", metadata.get("DES_AOO"));

		boolean enabled = Boolean.valueOf(metadata.get("ENABLED"));

		metadata.put("ENABLED", "valido");

		field = insertNode(entita, "//Fascicolo/stato", metadata.get("ENABLED"));
		field.addAttribute("statoAttivo", enabled ? "true" : "false", record.getNamespace());

		String data_apertura = metadata.get("DATA_APERTURA");
		if (data_apertura != null && !data_apertura.equals("")) {
			data_apertura = data_apertura.replaceAll("T.+$", "");
			OMElement fieldDataApertura = buildNode(entita, "//Fascicolo/dataApertura");
			fieldDataApertura.setText(data_apertura);
		}

		String data_chiusura = metadata.get("DATA_CHIUSURA");
		if (data_chiusura != null && !data_chiusura.equals("")) {
			data_chiusura = data_chiusura.replaceAll("T.+$", "");
			OMElement fieldDataApertura = buildNode(entita, "//Fascicolo/dataChiusura");
			fieldDataApertura.setText(data_chiusura);
		}

		String id_proc = metadata.get("ID_PROC");
		if (id_proc != null && !id_proc.equals("")) {
			insertNode(entita, "//Fascicolo/identificativoProcedimento", id_proc);
		}

		// il fascicolo padre non ha senso inserirlo

		insertCustomFieldsNode(entita, metadata, false);

		// insertRelazioniNodes(notification, metadata);
		insertRelazioniNode(entita, metadata, false);
		return notification;
	}

	// private String getValueOf(Map<String, String> metadata, String fieldName)
	// {
	//
	// return getValueOf(metadata, fieldName, "null", "null");
	// }

	// private String getValueOf(Map<String, String> metadata, String fieldName,
	// String returnValueIfNull, String returnValueIfVoid) {
	//
	// String val = metadata.get(fieldName);
	//
	// if (val == null) {
	// return returnValueIfNull;
	// }
	//
	// if (val.equals("")) {
	// return returnValueIfVoid;
	// }
	//
	// return val;
	// }
	//
	// private String getValueOfForDate(Map<String, String> metadata, String
	// fieldName, Boolean forceDummyVal, String dummy_format) {
	//
	// String val;
	// String format = dummy_format;
	//
	// try {
	// val = metadata.get(fieldName);
	//
	// if ((val == null || val.equals("") || val.equals("null")) &&
	// forceDummyVal) {
	// ts = Calendar.getInstance().getTime();
	// dummy_formatter = new SimpleDateFormat(format);
	// val = "" + dummy_formatter.format(ts);
	// }
	// else
	// val = String.valueOf(val);
	//
	// }
	// catch (Exception exc) {
	// val = null;
	// }
	//
	// return val;
	// }

	// private void setAttribute(OMElement xnode, String string, HashMap<String,
	// String> metadata, String name) throws JaxenException {
	//
	// xnode.addAttribute("uid", metadata.get(name), xnode.getNamespace());
	//
	// }

	private void insertRegistrazioniNode(OMElement xnode, Map<String, String> metadata) throws XMLStreamException, JaxenException, Exception {

		OMElement registrazioni = buildNode(xnode, "//Documento/registrazioni");
		OMElement record = buildNode(xnode, "//record");

		String ID_REGISTRO = metadata.get("ID_REGISTRO");
		String REGISTRO_PG = metadata.get("REGISTRO_PG");

		if (ID_REGISTRO != null && !"".equals(ID_REGISTRO)) {
			// writeDebug("Tipo registrazione 1 ");

			OMElement registrazione = getXmlTemplate("/schema/mod-registrazione.xml");
			registrazione.addAttribute("protocollo", "false", record.getNamespace());

			insertNode(registrazione, "//numero", metadata.get("N_REGISTRAZ"));
			insertNode(registrazione, "//registro", metadata.get("ID_REGISTRO"));

			String anno = metadata.get("A_REGISTRAZ");

			String d_registraz = metadata.get("D_REGISTRAZ");

			if (d_registraz == null || d_registraz.equals("")) {
				d_registraz = anno + "-01-01";
			}
			else {
				d_registraz = d_registraz.replaceAll("T.+$", "");
			}
			buildNode(registrazione, "//data").setText(d_registraz);

			insertNode(registrazione, "//anno", metadata.get("A_REGISTRAZ"));

			// insertNode(registrazione,"//oggetto", metadata,"O_REGISTRAZ");
			String ogg = metadata.get("O_REGISTRAZ");
			if (ogg != null && !"null".equals(ogg) && !"".equals(ogg))
				buildNode(registrazione, "//registrazione/oggetto").setText(ogg);

			registrazioni.addChild(registrazione);
		}

		if (REGISTRO_PG != null && !"".equals(REGISTRO_PG)) {
			// writeDebug("Tipo registrazione 2 ");

			OMElement registrazione = getXmlTemplate("/schema/mod-registrazione.xml");
			registrazione.addAttribute("protocollo", "true", record.getNamespace());

			insertNode(registrazione, "//numero", metadata.get("NUM_PG"));
			insertNode(registrazione, "//registro", metadata.get("REGISTRO_PG"));

			// insertNode(registrazione,"//data", metadata,"DATA_PG");
			String anno = metadata.get("ANNO_PG");

			String data_pg = metadata.get("DATA_PG");
			if (data_pg == null || data_pg.equals("") || data_pg.equals("null")) {
				data_pg = anno + "-01-01";
			}
			else {
				data_pg = data_pg.replaceAll("T.+$", "");
			}

			buildNode(registrazione, "//data").setText(data_pg);

			insertNode(registrazione, "//anno", anno);

			// insertNode(registrazione,"//oggetto", metadata,"OGGETTO_PG");
			String ogg = metadata.get("OGGETTO_PG");
			if (ogg != null && !"null".equals(ogg) && !"".equals(ogg))
				buildNode(registrazione, "//registrazione/oggetto").setText(ogg);

			registrazioni.addChild(registrazione);
		}

	}

	// private OMElement insertCustomFieldsNode(OMElement xnode, HashMap<String,
	// String> metadata) throws Exception {
	//
	// return insertCustomFieldsNode(xnode, metadata, null);
	//
	// }

	private String getDescrizioneText(Map<String, String> metadata, boolean isDocument) throws Exception {

		StringBuilder descrizione = new StringBuilder();

		List<OMElement> elements;

		String cod_aoo, cod_ente, type_id, id_fonte;
		cod_ente = metadata.get("COD_ENTE");
		cod_aoo = metadata.get("COD_AOO");
		type_id = metadata.get("TYPE_ID");
		id_fonte = metadata.get("ID_FONTE");

		if (isDocument) {
			elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/descrizionefields[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @type_id='" + type_id + "']/*");
			if (elements == null || elements.size() == 0) {
				elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/descrizionefields[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @type_id='*']/*");
				if (elements == null || elements.size() == 0) {
					elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/descrizionefields[@cod_ente='" + cod_ente + "' and @cod_aoo='*' and @type_id='*']/*");
					if (elements == null || elements.size() == 0) {
						elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/descrizionefields[@cod_ente='*' and @cod_aoo='*' and @type_id='*']/*");
					}
				}
			}

			int c = elements == null ? 0 : elements.size();
			writeDebug("trovati " + c + " descrizionefields configurati per cod_ente=" + cod_ente + " and cod_aoo=" + cod_aoo + " and type_id=" + type_id);
		}
		else {
			elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/descrizionefields[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @id_fonte='" + id_fonte + "']/*");
			if (elements == null || elements.size() == 0) {
				elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/descrizionefields[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @id_fonte='*']/*");
				if (elements == null || elements.size() == 0) {
					elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/descrizionefields[@cod_ente='" + cod_ente + "' and @cod_aoo='*' and @id_fonte='*']/*");
					if (elements == null || elements.size() == 0) {
						elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/descrizionefields[@cod_ente='*' and @cod_aoo='*' and @id_fonte='*']/*");
					}
				}
			}

			int c = elements == null ? 0 : elements.size();
			writeDebug("trovati " + c + " descrizionefields configurati per cod_ente=" + cod_ente + " and cod_aoo=" + cod_aoo + " and id_fonte=" + id_fonte);
		}

		if (elements != null && elements.size() > 0) {

			String elementSep = ((OMElement) elements.get(0).getParent()).getAttributeValue(new QName("separatore"));
			if (elementSep == null) elementSep = " ";

			for (OMElement descrizioneFieldElement : elements) {

				String nodeName = descrizioneFieldElement.getLocalName().toUpperCase();

				String nodeValue = metadata.get(nodeName);
				if (nodeValue == null) {
					continue;
				}

				nodeValue = nodeValue.trim();

				if (nodeValue.equals("")) {
					continue;
				}

				String formatoDataIn = descrizioneFieldElement.getAttributeValue(new QName("formato-data-in"));
				String formatoDataOut = descrizioneFieldElement.getAttributeValue(new QName("formato-data-out"));
				if (!Strings.isNullOrEmpty(formatoDataIn) && !Strings.isNullOrEmpty(formatoDataOut)) {
					DateTime dt = DateTimeFormat.forPattern(formatoDataIn).parseDateTime(nodeValue);
					nodeValue = DateTimeFormat.forPattern(formatoDataOut).print(dt);
				}

				String label = descrizioneFieldElement.getAttributeValue(new QName("label"));
				String sep = descrizioneFieldElement.getAttributeValue(new QName("separatore"));
				if (label == null) {
					descrizione.append(nodeValue);
				} else {
					descrizione.append(label);
					descrizione.append(sep == null ? ' ' : sep);
					descrizione.append(nodeValue);
				}

				descrizione.append(elementSep);

			}

			descrizione.delete(descrizione.length() - elementSep.length(), descrizione.length());
		}

		if (descrizione.length() == 0) {
			return "n.a.";
		}

		return descrizione.toString();

	}

	private void insertCustomFieldsNode(OMElement xnode, Map<String, String> metadata, boolean isDocument) throws Exception {

		OMElement customfields = null;
		List<OMElement> elements;

		String cod_aoo, cod_ente, type_id, id_fonte;
		cod_ente = metadata.get("COD_ENTE");
		cod_aoo = metadata.get("COD_AOO");
		type_id = metadata.get("TYPE_ID");
		id_fonte = metadata.get("ID_FONTE");

		if (isDocument) {
			elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/customfields[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @type_id='" + type_id + "']/*");
			if (elements == null || elements.size() == 0) {
				elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/customfields[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @type_id='*']/*");
				if (elements == null || elements.size() == 0) {
					elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/customfields[@cod_ente='" + cod_ente + "' and @cod_aoo='*' and @type_id='*']/*");
					if (elements == null || elements.size() == 0) {
						elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/customfields[@cod_ente='*' and @cod_aoo='*' and @type_id='*']/*");
					}
				}
			}

			int c = elements == null ? 0 : elements.size();
			writeDebug("trovati " + c + " custom_field configurati per cod_ente=" + cod_ente + " and cod_aoo=" + cod_aoo + " and type_id=" + type_id);
		}
		else {
			elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/customfields[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @id_fonte='" + id_fonte + "']/*");
			if (elements == null || elements.size() == 0) {
				elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/customfields[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @id_fonte='*']/*");
				if (elements == null || elements.size() == 0) {
					elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/customfields[@cod_ente='" + cod_ente + "' and @cod_aoo='*' and @id_fonte='*']/*");
					if (elements == null || elements.size() == 0) {
						elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/customfields[@cod_ente='*' and @cod_aoo='*' and @id_fonte='*']/*");
					}
				}
			}

			int c = elements == null ? 0 : elements.size();
			writeDebug("trovati " + c + " custom_field configurati per cod_ente=" + cod_ente + " and cod_aoo=" + cod_aoo + " and id_fonte=" + id_fonte);
		}

		if (elements != null && elements.size() > 0) {

			for (OMElement customFieldElement : elements) {

				OMElement customfield = getXmlTemplate("/schema/mod-customfield.xml");

				String nodeName = customFieldElement.getLocalName().toUpperCase();
				OMAttribute omaLabel = customFieldElement.getAttribute(qnameLabel);

				if (omaLabel != null && !omaLabel.equals("")) {
					nodeName = omaLabel.getAttributeValue();
				}

				String nodeValue = metadata.get(nodeName);
				if (nodeValue == null || nodeValue.equals("")) {

					nodeValue = "non assegnato";
					OMAttribute omaDefault = customFieldElement.getAttribute(qnameDefault);

					if (omaDefault != null && !omaDefault.equals("")) {
						nodeValue = omaDefault.getAttributeValue();
					}
				}

				if (customfields == null) {
					if (isDocument) {
						customfields = buildNode(xnode, "//Documento/custom_fields");
					}
					else {
						customfields = buildNode(xnode, "//Fascicolo/custom_fields");
					}
				}

				buildNode(customfield, "//custom_field/key").setText(nodeName.toLowerCase());
				insertNode(customfield, "//custom_field/value", nodeValue);

				customfields.addChild(customfield);
			}

		}

	}

	private void insertRelazioniNode(OMElement notification, Map<String, String> metadata, boolean isDocument) throws Exception {

		List<OMElement> elements;

		String cod_aoo, cod_ente, type_id, id_fonte, racc_uid;
		cod_ente = metadata.get("COD_ENTE");
		cod_aoo = metadata.get("COD_AOO");
		type_id = metadata.get("TYPE_ID");
		id_fonte = metadata.get("ID_FONTE");
		racc_uid = metadata.get("RACC_UID");

		if (isDocument) {
			elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/relazioni[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @type_id='" + type_id + "']/*");
			if (elements == null || elements.size() == 0) {
				elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/relazioni[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @type_id='*']/*");
				if (elements == null || elements.size() == 0) {
					elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/relazioni[@cod_ente='" + cod_ente + "' and @cod_aoo='*' and @type_id='*']/*");
					if (elements == null || elements.size() == 0) {
						elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/relazioni[@cod_ente='*' and @cod_aoo='*' and @type_id='*']/*");
					}
				}
			}

			int c = elements == null ? 0 : elements.size();
			writeDebug("trovate " + c + " relazioni configurate per cod_ente=" + cod_ente + " and cod_aoo=" + cod_aoo + " and type_id=" + type_id);
		}
		else {
			elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/relazioni[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @id_fonte='" + id_fonte + "']/*");
			if (elements == null || elements.size() == 0) {
				elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/relazioni[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @id_fonte='*']/*");
				if (elements == null || elements.size() == 0) {
					elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/relazioni[@cod_ente='" + cod_ente + "' and @cod_aoo='*' and @id_fonte='*']/*");
					if (elements == null || elements.size() == 0) {
						elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_fascicolo']/relazioni[@cod_ente='*' and @cod_aoo='*' and @id_fonte='*']/*");
					}
				}
			}

			int c = elements == null ? 0 : elements.size();
			writeDebug("trovate " + c + " relazioni configurate per cod_ente=" + cod_ente + " and cod_aoo=" + cod_aoo + " and id_fonte=" + id_fonte);
		}

		//<delega tipoRelazione="operatore_pa" tipoId="fonte" id="EMRRICERCA1" descrizione="..." /> 
		List<String> gruppi = new ArrayList<String>();
		
		String gruppoOperatoriPa = generateGroupIdOperatoriPa("operatore_pa", "fonte", id_fonte);
		gruppi.add(gruppoOperatoriPa);
		
		if (elements != null && elements.size() > 0) {

			OMElement relazioni = null;
			
			for (OMElement relazioniElement : elements) {

				String nodeName = relazioniElement.getLocalName().toUpperCase();

				String relazioneText = metadata.get(nodeName);

				if (relazioneText == null || relazioneText.equals("")) {
					continue;
				}

				if (relazioni == null) {
					relazioni = buildNode(notification, "//commonFields/relazioni");
				}

				String autorizzazione = "true";
				String tipoId = "codiceFiscale";
				String tipoRelazione = "titolare";

				OMAttribute omaAutorizzazione = relazioniElement.getAttribute(qnameAutorizzazione);
				if (omaAutorizzazione != null && !omaAutorizzazione.equals("")) {
					autorizzazione = omaAutorizzazione.getAttributeValue();
				}

				OMAttribute omaTipoId = relazioniElement.getAttribute(qnameTipoId);
				if (omaTipoId != null && !omaTipoId.equals("")) {
					tipoId = omaTipoId.getAttributeValue();
				}

				OMAttribute omaTipoRelazione = relazioniElement.getAttribute(qnameTipoRelazione);
				if (omaTipoRelazione != null && !omaTipoRelazione.equals("")) {
					tipoRelazione = omaTipoRelazione.getAttributeValue();
				}

				OMElement relazioneElement = factory.createOMElement("relazione", null);
				relazioneElement.setText(relazioneText);

				relazioneElement.addAttribute("autorizzazione", autorizzazione, relazioneElement.getNamespace());
				relazioneElement.addAttribute("tipoId", tipoId, relazioneElement.getNamespace());
				relazioneElement.addAttribute("tipoRelazione", tipoRelazione, relazioneElement.getNamespace());

				relazioni.addChild(relazioneElement);

				String groupId = generateGroupIdFromRelazioneElement(relazioneElement);
				gruppi.add(groupId);
			}			

		}
		
		if (!isDocument && gruppi.size()>0) {

			setACLFascicolo(racc_uid, gruppi);

			writeDebug("fascicolo " + racc_uid + " assegnati diritti readOnly ai gruppi: " + gruppi.toString());
		}

	}

	private void incrementKeywordsNode(Map<String, String> metadata) throws Exception {

		String cod_aoo, cod_ente, type_id;

		cod_ente = metadata.get("COD_ENTE");
		cod_aoo = metadata.get("COD_AOO");
		type_id = metadata.get("TYPE_ID");

		List<OMElement> elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/keywords[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @type_id='" + type_id + "' ]/*");
		if (elements == null || elements.size() == 0) {
			elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/keywords[@cod_ente='" + cod_ente + "' and @cod_aoo='" + cod_aoo + "' and @type_id='*' ]/*");
			if (elements == null || elements.size() == 0) {
				elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/keywords[@cod_ente='" + cod_ente + "' and @cod_aoo='*' and @type_id='*' ]/*");
				if (elements == null || elements.size() == 0) {
					elements = readConfigNodes("//group[@name='batch-popolamento-raccoglitore']/section[@name='profilo_documento']/keywords[@cod_ente='*' and @cod_aoo='*' and @type_id='*' ]/*");
				}
			}
		}

		int c = elements == null ? 0 : elements.size();
		writeDebug("trovate " + c + " keywords configurate per cod_ente=" + cod_ente + " and cod_aoo=" + cod_aoo + " and type_id=" + type_id);

		if (elements != null && elements.size() > 0) {

			for (OMElement element : elements) {

				String nodeName = element.getLocalName().toUpperCase();

				String value = metadata.get(nodeName);

				if (value == null || value.equals("")) {
					continue;
				}

				if (keywordsFascicoloNode == null) {
					keywordsFascicoloNode = factory.createOMElement("keywords", null);
				}

				OMElement keyword = factory.createOMElement("keyword", null);
				keyword.setText(value);
				keywordsFascicoloNode.addChild(keyword);
			}
		}

	}

	// private String getValueFromKey(KeyValuePair[] kvpArray, String metaName)
	// {
	//
	// for (KeyValuePair kvp : kvpArray) {
	//
	// if (kvp.getKey().equalsIgnoreCase(metaName)) {
	// return kvp.getValue();
	// }
	// }
	//
	// return null;
	// }

	private void putIntoMap(Map<String, String> map, KeyValuePair[] pairsArray) {

		for (KeyValuePair pair : pairsArray)
			map.put(pair.getKey(), pair.getValue());

	}

	// private void putIntoMap(Map<String, String> map, Map<String, String>
	// data) {
	//
	// for (String key : data.keySet())
	// map.put(key, String.valueOf(data.get(key)));
	// }

	// private OMElement insertNode(OMElement xnode, String xpath, String value)
	// throws JaxenException, Exception {
	//
	// OMElement field = buildNode(xnode, xpath);
	// field.setText(value);
	//
	// return field;
	//
	// }

	// private OMElement insertStringNodeIfNotNull(OMElement xnode, String
	// xpath, Map<String, String> metadata, String metaName) throws
	// JaxenException, Exception {
	//
	// String val = getValueOf(metadata, metaName, null, null);
	//
	// if(val == null){
	// return null;
	// }
	//
	// OMElement field = buildNode(xnode, xpath);
	// field.setText(val);
	// return field;
	//
	// }

	// private OMElement insertDateNodeIfNotNull(OMElement xnode, String xpath,
	// Map<String, String> metadata, String metaName) throws JaxenException,
	// Exception {
	//
	// String val = getValueOf(metadata, metaName, null, null);
	//
	// if(val == null){
	// return null;
	// }
	//
	// val = val.replaceAll("T.+$", "");
	// OMElement field = buildNode(xnode, xpath);
	// field.setText(val);
	// return field;
	// }
	//
	private OMElement insertNode(OMElement xnode, String xpath, String metaValue) throws JaxenException, Exception {

		if (metaValue == null || metaValue.equals("")) {
			metaValue = "n.a.";
		}

		OMElement field = buildNode(xnode, xpath);
		field.setText(metaValue);

		return field;

	}

	private KeyValuePair[] getFascicoloID(KeyValuePair[] metadata) {

		List<KeyValuePair> kvpID = new ArrayList<KeyValuePair>();
		for (KeyValuePair pair : metadata) {
			if (pair.getKey().equalsIgnoreCase("COD_ENTE"))
				kvpID.add(pair);
			else if (pair.getKey().equalsIgnoreCase("COD_AOO"))
				kvpID.add(pair);
			else if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO"))
				kvpID.add(pair);
			else if (pair.getKey().equalsIgnoreCase("CLASSIFICA"))
				kvpID.add(pair);
			else if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO"))
				kvpID.add(pair);
		}
		return kvpID.toArray(new KeyValuePair[0]);
	}

	private KeyValuePair[] getFascicoliSecondariCriteria(KeyValuePair[] metadata) {

		String anno = "";
		String claz = "";
		String prog = "";

		List<KeyValuePair> kvpID = new ArrayList<KeyValuePair>();
		for (KeyValuePair pair : metadata) {
			if (pair.getKey().equalsIgnoreCase("COD_ENTE"))
				kvpID.add(pair);
			else if (pair.getKey().equalsIgnoreCase("COD_AOO"))
				kvpID.add(pair);
			else if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO"))
				anno = pair.getValue();
			else if (pair.getKey().equalsIgnoreCase("CLASSIFICA"))
				claz = pair.getValue();
			else if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO"))
				prog = pair.getValue();
		}

		KeyValuePair pair = new KeyValuePair();
		pair.setKey("FASC_SECONDARI");
		pair.setValue(claz + "/" + anno + "/" + prog);
		kvpID.add(pair);

		return kvpID.toArray(new KeyValuePair[0]);
	}

	private RerServiziPerLeFontiServiceStub getRerServiziPerLeFontiServiceClient(String racEpr) throws AxisFault {

		RerServiziPerLeFontiServiceStub rerServiziPerLeFontiServiceClient = new RerServiziPerLeFontiServiceStub();

		Options options = rerServiziPerLeFontiServiceClient._getServiceClient().getOptions();
		options.setTo(new EndpointReference(racEpr));

		// options.setProperty(Constants.Configuration.ENABLE_MTOM,
		// Constants.VALUE_TRUE);
		options.setTimeOutInMilliSeconds(timeout);

		options.setProperty(Constants.Configuration.CACHE_ATTACHMENTS, cacheAttachments ? Constants.VALUE_TRUE : Constants.VALUE_FALSE);

		options.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR, cacheDir);

		options.setProperty(Constants.Configuration.FILE_SIZE_THRESHOLD, fileSizeThreshold);

		return rerServiziPerLeFontiServiceClient;
	}

	// private boolean sessioneScaduta(Exception e) {
	//
	// String errorMessage = e.getMessage();
	//
	// if (errorMessage == null && (e instanceof DocerException)) {
	// errorMessage = ((DocerException)e).getErrDescription();
	// }
	//
	// if (errorMessage == null) {
	// errorMessage = "";
	// }
	// errorMessage = errorMessage.toUpperCase();
	//
	// if
	// (errorMessage.matches(".*TICKET +NON +VALIDO +O +UTENTE +NON +AUTORIZZATO.*")
	// || errorMessage.matches(".*TOKEN +NON +VALIDO.*")) {
	//
	// writeError(errorMessage);
	// return true;
	// }
	//
	// return false;
	// }

	// private String transformXML(String xslFilename, String xmlString) throws
	// Exception {
	// String formattedOutput = "";
	// try {
	//
	// InputStream in = this.getClass().getResourceAsStream(xslFilename);
	//
	// TransformerFactory tFactory = TransformerFactory.newInstance();
	// Transformer transformer = tFactory.newTransformer(new StreamSource(in));
	//
	// StreamSource xmlSource = new StreamSource(new StringReader(xmlString));
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	//
	// transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	// transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	// transformer.transform(xmlSource, new StreamResult(baos));
	//
	// formattedOutput = baos.toString();
	//
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// throw new Exception(e.getMessage());
	// }
	//
	// return formattedOutput;
	// }

	static String regextrim = "(?i)\\<[ /]*((mittenti)|(destinatari)|(firmatario))[ /]*\\>";

	// private String trimRootNode(String xml) {
	// String newstr = xml.replaceAll(regextrim, "");
	// return newstr;
	// }

	protected final OMElement xPathSelectSingleNode(String xpath, OMElement node) throws JaxenException {
		AXIOMXPath path = new AXIOMXPath(node, xpath);

		Object singleNode = path.selectSingleNode(node);

		if (!(singleNode instanceof OMElement)) {
			if (singleNode != null) {
				writeError("singleNode instanceof OMDocumentImpl: " + singleNode);
			}

		}

		return (OMElement) singleNode;
	}

	//    protected final OrderedList<OMElement> xPathSelectNodes(String xpath, OMElement node) throws JaxenException {
	//        OrderedList<OMElement> list = new OrderedList<OMElement>();
	//        
	//        if(node==null){
	//            return list;
	//        }
	//        AXIOMXPath path = new AXIOMXPath(node, xpath);
	//
	//        List<?> nodes = path.selectNodes(path);
	//        
	//        for (Object singleNode : nodes) {
	//            if (!(singleNode instanceof OMElement)) {
	//                if(singleNode!=null){
	//                    writeError("singleNode instanceof OMDocumentImpl: " + singleNode);    
	//                }                
	//            }
	//
	//            list.add((OMElement)singleNode);
	//        }
	//        return list;
	//    }

	protected final OrderedList<OMElement> xPathSelectNodes(String xpath, OMElement node) throws JaxenException {
		OrderedList<OMElement> list = new OrderedList<OMElement>();

		if (node == null) {
			return list;
		}

		AXIOMXPath path = new AXIOMXPath(xpath);

		List<?> nodes = path.selectNodes(node);

		for (Object singleNode : nodes) {
			if (!(singleNode instanceof OMElement)) {
				if (singleNode != null) {
					writeError("singleNode instanceof OMDocumentImpl: " + singleNode);
				}
			}

			list.add((OMElement) singleNode);
		}
		return list;
	}

	private OMElement buildNode(OMElement xml, String xPath) throws JaxenException, Exception {

		OMElement subNode = xPathSelectSingleNode(xPath, xml);

		if (subNode == null) {

			Pattern pattern = Pattern.compile("^(.+)/([^/]+)$");
			Matcher m = pattern.matcher(xPath);

			if (!m.matches())
				throw new Exception("Impossibile elaborare il modello xml interno");

			String parentPath = m.group(1);
			String subNodeName = m.group(2);

			OMElement nodeParent = buildNode(xml, parentPath);

			// if (nodeParent == null)
			// nodeParent = buildNode(xml, parentPath);

			subNode = factory.createOMElement(subNodeName, null);
			nodeParent.addChild(subNode);

		}

		return subNode;

	}

	private void validateXML(String xml) throws Exception {

		InputStream is = null;
		try {
			is = this.getClass().getResourceAsStream("/schema/rer-import-docer-0920.xsd");
			if (is == null) {
				throw new Exception("file di validazione XSD non trovato: getResourceAsStream: /schema/rer-import-docer-0920.xsd");
			}

			Source xsdSchemaSource = new StreamSource(is);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes(UTF8))));

			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(xsdSchemaSource);
			javax.xml.validation.Validator validator = schema.newValidator();

			validator.validate(new DOMSource(doc));
			logger.info("XML validato");

		}
		catch (SAXException e) {
			writeError("Errore validazione XML sorgente: " + e.getMessage());
			throw new Exception("Errore validazione XML sorgente: " + e.getMessage());
		}
		finally {
			try {

				is.close();
			}
			catch (Exception e) {
			}
		}
	}

	// private DateTime parseDateTime(String datetime) {
	// DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
	// String nowAsString = df.format(new Date());
	// }

	private File toXML(File root, String name, String xmlString) throws Exception {

		DateTime d = new DateTime();
		String strDate = d.toString("yyyy-MM-dd'T'HH.mm.ss.SSS");

		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.setOutputProperty(OutputKeys.METHOD, "xml");
		xformer.setOutputProperty(OutputKeys.INDENT, "yes");
		xformer.setOutputProperty(OutputKeys.ENCODING, UTF8);

		if (!root.exists()) {
			throw new Exception("xmlFileDirectory non trovata: " + root.getAbsolutePath());
		}

		File file = new File(root, name);

		java.io.FileOutputStream fw = new java.io.FileOutputStream(file.getAbsolutePath());
		StreamSource xmlSource = new StreamSource(new StringReader(xmlString));

		xformer.transform(xmlSource, new StreamResult(fw));

		// String strout = fw.toString();

		fw.flush();
		fw.close();

		// // INDENTO
		// try {
		// InputStream in = new FileInputStream(file);
		// DocumentBuilder builder =
		// DocumentBuilderFactory.newInstance().newDocumentBuilder();
		// Document doc = builder.parse(in);
		//
		// OutputFormat format = new OutputFormat(doc);
		// format.setIndenting(true);
		//
		// // to generate a file output use fileoutputstream instead of
		// system.out
		// XMLSerializer serializer = new XMLSerializer(new
		// FileOutputStream(file), format);
		// serializer.serialize(doc);
		// } catch (Exception e) {
		// }
		return file;

	}

	private OMElement getXmlTemplate(String path) throws XMLStreamException {

		InputStream in = this.getClass().getResourceAsStream(path);
		XMLInputFactory inFact = XMLInputFactory.newInstance();
		XMLStreamReader reader = inFact.createXMLStreamReader(in);
		StAXOMBuilder builder = new StAXOMBuilder(reader);

		OMElement xml = builder.getDocumentElement();

		return xml;
	}

	// private OMElement getXmlFile(File obj) throws XMLStreamException,
	// FileNotFoundException {
	//
	// InputStream in = new FileInputStream(obj);
	// XMLInputFactory inFact = XMLInputFactory.newInstance();
	// XMLStreamReader reader = inFact.createXMLStreamReader(in);
	// StAXOMBuilder builder = new StAXOMBuilder(reader);
	//
	// OMElement xml = builder.getDocumentElement();
	//
	// return xml;
	// }

	private List<OMElement> readConfigNodes(String xpath) throws Exception {
		List<OMElement> list = config.getNodes(xpath);
		if (list == null) {
			logger.log(Level.ERROR, "collezione di nodi " + xpath + " mancante in configurazione");
			throw new Exception("collezione di nodi " + xpath + " mancante in configurazione");
		}

		return list;
	}

	private String readConfigKey(String xpath) throws Exception {

		if (config == null) {
			config = new Config();
		}

		OMElement omelement = config.getNode(xpath);
		if (omelement == null) {
			logger.log(Level.ERROR, "chiave " + xpath + " mancante in configurazione");
			throw new Exception("chiave " + xpath + " mancante in configurazione");
		}

		return omelement.getText();
	}

	private OMElement readConfigElement(String xpath) throws Exception {
		OMElement omelement = config.getNode(xpath);
		if (omelement == null) {
			logger.log(Level.ERROR, "chiave " + xpath + " mancante in configurazione");
			throw new Exception("chiave " + xpath + " mancante in configurazione");
		}

		return omelement;
	}

	private void sendViaSFTP(File file, String host, Integer port, String userid, String password, String folder) throws Exception {

		JSch.setConfig("StrictHostKeyChecking", "no");

		JSch jsch = new JSch();

		Session session;
		if(port!=null){
		    session = jsch.getSession(userid, host, port);    
		}
		else{
		    session = jsch.getSession(userid, host);
		}
		
		session.setPassword(password);

		try {
			session.connect();

			ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");

			try {
				channel.connect();

				channel.put(file.getAbsolutePath(), channel.getHome() + "/" + folder);
			}
			catch (Exception e) {
				throw new Exception("channel put :" + e.getMessage());

			}
			finally {
				channel.exit();
			}
		}
		catch (Exception e) {
			throw new Exception("conncect sftp :" + e.getMessage());
		}
		finally {
			session.disconnect();
		}

	}

	public void sendViaSOAP(File xmlFile, String url, String ontology, String idFonte, String token) throws Exception {

		writeDebug("sendViaSOAP");

		// CAMPO PER IL PASSAGGIO DI ULTERIORI CAMPI, NON UTILIZZATO
		// String securityInfo = "";

		if (rerServiziPerLeFontiServiceClient == null) {
			try {
				rerServiziPerLeFontiServiceClient = getRerServiziPerLeFontiServiceClient(url);
			}
			catch (AxisFault e) {
				writeError("AxisFault: " + e.getMessage());
				throw e;
			}
		}

		// l'entita' informativa espressa in xml secondo il tracciato concordata
		// con la fonte
		String content = FileUtils.readFileToString(xmlFile, UTF8);
		;
		// url che rappresena l'ontologia dell'entita' informativa
		String ontologyURI = ontology;
		// id univoco che identifica la fonte
		String source = idFonte;
		// informazione di sicurezza fornita alla fonte e che deve esssere
		// passata per certificare il chiamante
		// String token = idFonte;

		SaveOrUpdateInstanceRequestType saveOrUpdateInstanceRequestType = new SaveOrUpdateInstanceRequestType();
		saveOrUpdateInstanceRequestType.setContent(content);
		saveOrUpdateInstanceRequestType.setOntologyURI(ontologyURI);
		saveOrUpdateInstanceRequestType.setSource(source);
		saveOrUpdateInstanceRequestType.setToken(token);

		try {
			rerServiziPerLeFontiServiceClient.saveOrUpdateInstance(saveOrUpdateInstanceRequestType);
		}
		catch (RemoteException e) {
			writeError("RemoteException: " + e.getMessage());
			throw e;
		}
	}

	private static String templateIdFascicolo = "%s,%s,%s,%s,%s";

	private String composeIdFascicolo(KeyValuePair[] arr) throws Exception {

		String cod_ente = null;
		String cod_aoo = null;
		String classifica = null;
		String anno_fascicolo = null;
		String progr_fascicolo = null;

		for (KeyValuePair pair : arr) {

			if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
				cod_ente = pair.getValue();
				continue;
			}
			if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
				cod_aoo = pair.getValue();
				continue;
			}
			if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
				classifica = pair.getValue();
				continue;
			}
			if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
				anno_fascicolo = pair.getValue();
				continue;
			}
			if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
				progr_fascicolo = pair.getValue();
				continue;
			}
		}

		String id = String.format(templateIdFascicolo, cod_ente, cod_aoo, classifica, anno_fascicolo, progr_fascicolo);

		if (StringUtils.isEmpty(cod_ente)) {
			throw new Exception("metadato COD_ENTE vuoto o non presente; type_id: fascicolo; " + id);
		}
		if (StringUtils.isEmpty(cod_aoo)) {
			throw new Exception("metadato COD_AOO vuoto o non presente; type_id: fascicolo; " + id);
		}
		if (StringUtils.isEmpty(classifica)) {
			throw new Exception("metadato CLASSIFICA vuoto o non presente; type_id: fascicolo; " + id);
		}
		if (StringUtils.isEmpty(anno_fascicolo)) {
			throw new Exception("metadato ANNO_FASCICOLO vuoto o non presente; type_id: fascicolo; " + id);
		}
		if (StringUtils.isEmpty(progr_fascicolo)) {
			throw new Exception("metadato PROGR_FASCICOLO vuoto o non presente; type_id: fascicolo; " + id);
		}

		return id;
	}

	private void setACLFascicolo(String racc_uid, List<String> groups) throws Exception {

		List<String> upperCaseGroups = new ArrayList<String>();

		for (String gid : groups) {
			upperCaseGroups.add(gid.toUpperCase());
		}

		manageGroups(groups);

		SetACLFascicolo setACLFascicolo = new SetACLFascicolo();
		setACLFascicolo.setToken(ticket_ws_fonte);

		KeyValuePair kvp = new KeyValuePair();
		kvp.setKey("RACC_UID");
		kvp.setValue(racc_uid);

		// il metodo e' stato modificato in WSFonteDocer per gestire i racc_uid
		setACLFascicolo.setFascicoloId(new KeyValuePair[] { kvp });

		for (String gid : groups) {
			KeyValuePair kvpAcl = new KeyValuePair();
			kvpAcl.setKey(gid);
			kvpAcl.setValue("2"); // read only
			setACLFascicolo.addAcls(kvpAcl);
		}

		try {
			fonteDocerWSClient.setACLFascicolo(setACLFascicolo);
		}
		catch (Exception e) {
			throw new Exception("Errore setACLFascicolo: RACC_UID=" + racc_uid + ": " + e.getMessage());
		}

	}

	private void setACLDocumento(String racc_uid, List<String> groups) throws Exception {

		List<String> upperCaseGroups = new ArrayList<String>();

		for (String gid : groups) {
			upperCaseGroups.add(gid.toUpperCase());
		}

		manageGroups(groups);

		SetACLDocument setACLDocument = new SetACLDocument();
		setACLDocument.setToken(ticket_ws_fonte);
		setACLDocument.setDocId(racc_uid);

		for (String gid : groups) {
			KeyValuePair kvpAcl = new KeyValuePair();
			kvpAcl.setKey(gid);
			kvpAcl.setValue("2"); // read only
			setACLDocument.addAcls(kvpAcl);
		}

		try {
			fonteDocerWSClient.setACLDocument(setACLDocument);
		}
		catch (Exception e) {
			throw new Exception("Errore setACLDocumento: RACC_UID=" + racc_uid + ": " + e.getMessage());
		}

	}

	private void manageGroups(List<String> groups) throws Exception {

		if (groups.size() == 0) {
			return;
		}

		List<String> groupsToCreate = new ArrayList<String>();

		SearchGroups searchGroups = new SearchGroups();

		for (String gid : groups) {
			KeyValuePair kvp = new KeyValuePair();
			kvp.setKey("GROUP_ID");
			kvp.setValue(gid);
			searchGroups.addSearchCriteria(kvp);
			groupsToCreate.add(gid);
		}

		searchGroups.setToken(ticket_ws_fonte);
		SearchGroupsResponse searchGroupsResponse = null;
		try {
			searchGroupsResponse = fonteDocerWSClient.searchGroups(searchGroups);
		}
		catch (Exception e) {
			throw new Exception("Errore fonteDocerWSClient.searchGroups: " + e.getMessage());
		}

		if (searchGroupsResponse != null && searchGroupsResponse.get_return() != null) {

			for (SearchItem si : searchGroupsResponse.get_return()) {

				for (KeyValuePair kvp : si.getMetadata()) {
					if (kvp.getKey().equalsIgnoreCase("GROUP_ID")) {
						String gidToRemove = kvp.getValue().toUpperCase();
						groupsToCreate.remove(gidToRemove);
						break;
					}
				}
			}
		}

		if (groupsToCreate.size() > 0) {
			CreateGroup createGroup = new CreateGroup();
			createGroup.setToken(ticket_ws_fonte);
			for (String gid : groupsToCreate) {
				KeyValuePair kvp1 = new KeyValuePair();
				kvp1.setKey("GROUP_ID");
				kvp1.setValue(gid);
				createGroup.addGroupInfo(kvp1);

				KeyValuePair kvp2 = new KeyValuePair();
				kvp2.setKey("GROUP_NAME");
				kvp2.setValue(gid);
				createGroup.addGroupInfo(kvp2);

				try {
					fonteDocerWSClient.createGroup(createGroup);
					writeDebug("creato gruppo " + gid);
				}
				catch (Exception e) {
					throw new Exception("Errore fonteDocerWSClient.createGroup: " + e.getMessage());

				}

			}

		}
	}

	//	private void manageUser(String userId) throws Exception {
	//
	//		if (StringUtils.isEmpty(userId)) {
	//			return;
	//		}
	//		
	//		SearchUsers searchUsers = new SearchUsers();
	//		searchUsers.setToken(ticket_ws_fonte);
	//
	//		KeyValuePair kvp = new KeyValuePair();
	//		kvp.setKey("USER_ID");
	//		kvp.setValue(userId);
	//		searchUsers.addSearchCriteria(kvp);
	//		
	//		SearchUsersResponse searchUsersResponse = null;
	//		try {
	//			searchUsersResponse = fonteDocerWSClient.searchUsers(searchUsers);
	//			if (searchUsersResponse != null && searchUsersResponse.get_return() != null) {
	//
	//				if (searchUsersResponse.get_return().length > 0) {
	//					// utente esistente
	//					return;
	//				}
	//			}
	//		}
	//		catch (Exception e) {
	//			throw new Exception("Errore fonteDocerWSClient.searchGroups: " + e.getMessage());
	//		}
	//
	//		CreateUser createUser = new CreateUser();
	//		createUser.setToken(ticket_ws_fonte);
	//
	//		KeyValuePair kvp1 = new KeyValuePair();
	//		kvp1.setKey("USER_ID");
	//		kvp1.setValue(userId);
	//		createUser.addUserInfo(kvp1);
	//
	//		KeyValuePair kvp2 = new KeyValuePair();
	//		kvp2.setKey("FULL_NAME");
	//		kvp2.setValue(userId);
	//		createUser.addUserInfo(kvp2);
	//
	//		try {
	//			fonteDocerWSClient.createUser(createUser);
	//			writeDebug("creato utente " + userId);
	//		}
	//		catch (Exception e) {
	//			throw new Exception("Errore fonteDocerWSClient.createUser: " + e.getMessage());
	//		}
	//
	//	}
	
	public static String getLastMessage(){
		return lastMessage;
	}

}
