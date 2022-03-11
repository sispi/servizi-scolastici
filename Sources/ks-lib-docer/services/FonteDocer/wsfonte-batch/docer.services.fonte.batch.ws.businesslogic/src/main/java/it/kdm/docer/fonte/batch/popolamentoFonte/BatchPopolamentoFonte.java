package it.kdm.docer.fonte.batch.popolamentoFonte;

import it.kdm.docer.clients.AuthenticationServiceStub;
import it.kdm.docer.clients.AuthenticationServiceStub.Login;
import it.kdm.docer.clients.AuthenticationServiceStub.LoginResponse;
import it.kdm.docer.clients.AuthenticationServiceStub.Logout;
import it.kdm.docer.clients.DocerExceptionException;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.clients.DocerServicesStub.AddRelated;
import it.kdm.docer.clients.DocerServicesStub.CreateAOO;
import it.kdm.docer.clients.DocerServicesStub.CreateAnagraficaCustom;
import it.kdm.docer.clients.DocerServicesStub.CreateDocument;
import it.kdm.docer.clients.DocerServicesStub.CreateEnte;
import it.kdm.docer.clients.DocerServicesStub.CreateFascicolo;
import it.kdm.docer.clients.DocerServicesStub.CreateTitolario;
import it.kdm.docer.clients.DocerServicesStub.DownloadDocument;
import it.kdm.docer.clients.DocerServicesStub.DownloadDocumentResponse;
import it.kdm.docer.clients.DocerServicesStub.GetAOO;
import it.kdm.docer.clients.DocerServicesStub.GetAOOResponse;
import it.kdm.docer.clients.DocerServicesStub.GetAnagraficaCustom;
import it.kdm.docer.clients.DocerServicesStub.GetAnagraficaCustomResponse;
import it.kdm.docer.clients.DocerServicesStub.GetEnte;
import it.kdm.docer.clients.DocerServicesStub.GetEnteResponse;
import it.kdm.docer.clients.DocerServicesStub.GetFascicolo;
import it.kdm.docer.clients.DocerServicesStub.GetFascicoloResponse;
import it.kdm.docer.clients.DocerServicesStub.GetProfileDocument;
import it.kdm.docer.clients.DocerServicesStub.GetProfileDocumentResponse;
import it.kdm.docer.clients.DocerServicesStub.GetRelatedDocuments;
import it.kdm.docer.clients.DocerServicesStub.GetRelatedDocumentsResponse;
import it.kdm.docer.clients.DocerServicesStub.GetTitolario;
import it.kdm.docer.clients.DocerServicesStub.GetTitolarioResponse;
import it.kdm.docer.clients.DocerServicesStub.KeyValuePair;
import it.kdm.docer.clients.DocerServicesStub.ReadConfig;
import it.kdm.docer.clients.DocerServicesStub.ReadConfigResponse;
import it.kdm.docer.clients.DocerServicesStub.SearchAnagrafiche;
import it.kdm.docer.clients.DocerServicesStub.SearchAnagraficheResponse;
import it.kdm.docer.clients.DocerServicesStub.SearchDocuments;
import it.kdm.docer.clients.DocerServicesStub.SearchDocumentsResponse;
import it.kdm.docer.clients.DocerServicesStub.SearchItem;
import it.kdm.docer.clients.DocerServicesStub.UpdateAOO;
import it.kdm.docer.clients.DocerServicesStub.UpdateAnagraficaCustom;
import it.kdm.docer.clients.DocerServicesStub.UpdateEnte;
import it.kdm.docer.clients.DocerServicesStub.UpdateFascicolo;
import it.kdm.docer.clients.DocerServicesStub.UpdateProfileDocument;
import it.kdm.docer.clients.DocerServicesStub.UpdateTitolario;
import it.kdm.docer.clients.DocerServicesStub.WriteConfig;
import it.kdm.docer.clients.ExceptionException;
import it.kdm.docer.commons.Config;
import it.kdm.docer.fonte.batch.BatchLog;
import it.kdm.docer.fonte.batch.DirectoryUtils;
import it.kdm.docer.fonte.batch.Log4jUtils;
import it.kdm.docer.fonte.batch.email.EmailFactory;
import it.kdm.docer.fonte.batch.popolamentoFonte.objects.AnagraficaType;
import it.kdm.docer.fonte.batch.popolamentoFonte.objects.BusinessLogicConfiguration;
import it.kdm.docer.fonte.batch.popolamentoFonte.objects.DocumentType;
import it.kdm.docer.fonte.batch.popolamentoFonte.objects.EnumBLPropertyType;
import it.kdm.docer.fonte.batch.popolamentoFonte.objects.FieldDescriptor;
import it.kdm.docer.fonte.batch.popolamentoFonte.objects.MigrationFilterCriteria;
import it.kdm.docer.fonte.batch.popolamentoFonte.objects.MigrationFilterCriteriaManager;
import it.kdm.docer.fonte.batch.popolamentoFonte.objects.OrderedList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.w3c.dom.Document;

public class BatchPopolamentoFonte implements org.quartz.Job {

	private static String lastMessage = "";
	private static String VOID = "";
	private BatchLog fonteLog = null;
	private boolean severe_error = false;

	private String NEEDS_FONTE_UPDATE = "NEEDS_FONTE_UPDATE";
	private String NEEDS_RACCOGLITORE_UPDATE = "NEEDS_RACCOGLITORE_UPDATE";
	private String ID_FONTE = "ID_FONTE";
	private String RACC_UID = "RACC_UID";

	private org.slf4j.Logger logger = org.slf4j.Logger
			.getLogger(BatchPopolamentoFonte.class);

	private String ticket_ws_ac = null;
	private String ticket_ws_fonte = null;

	private List<String> documenti_skip_fields = null;
	private List<String> fascicoli_skip_fields = null;

	// business logic archivio corrente
	// private BusinessLogic blAC = null;

	private Boolean cacheAttachments = null;
	private File cacheDir;
	private String fileSizeThreshold = null;
	private Integer primarysearch_max_results = null;
	private Integer timeout = null;

	// archivio corrente
	private boolean download_ac_config = true;
	private String ws_ac_login_userid = null;
	private String ws_ac_login_pwd = null;
	private String ws_ac_login_cod_ente = null;
	private String ws_ac_url = null;
	private String ws_ac_auth_url = null;

	// static private String download_wsauth_epr = "";
	// static private String download_wsdocer_epr = "";

	// static boolean download_businesslogic_configuration_file = false;
	// static private String businesslogic_configuration_file = "";

	private String ws_fonte_login_userid = null;
	private String ws_fonte_login_pwd = null;
	private String ws_fonte_login_cod_ente = null;
	private String ws_fonte_url = null;
	private String ws_fonte_auth_url = null;

	private DocerServicesStub fonteDocerWSClient;
	private AuthenticationServiceStub fonteAuthWSClient;

	private DocerServicesStub acDocerWSClient;
	private AuthenticationServiceStub acAuthWSClient;

	private OrderedList<MigrationFilterCriteria> migrationFilterCriteriaList = null;

	private static QName nameQName = new QName("name");
	private static QName customQName = new QName("custom");
	private static QName roleQName = new QName("role");

	// private List<AnagraficaCustomDescriptor> anagraficheCustomDescriptions =
	// new ArrayList<AnagraficaCustomDescriptor>();

	private List<String> anagraficheCache = new ArrayList<String>();

	private List<String> titolarioTaxonomy = new ArrayList<String>();
	private List<String> fascicoloTaxonomy = new ArrayList<String>();

	private List<String> docIdLavoratix = new ArrayList<String>();
	private List<String> fascicoliLavoratix = new ArrayList<String>();

	private int cntErrors = 0;
	private int cntFascicoliCreated = 0;
	private int cntFascicoliUpdated = 0;
	private int cntDocumentiCreated = 0;
	private int cntDocumentiUpdated = 0;

	private static Config config;
	private static Properties configurationProperties;
	private static File acConfigurationFileDownloaded;

	private EmailFactory emf = null;

	private static Map<String, FieldDescriptor> FIELDS = new HashMap<String, FieldDescriptor>();
	private static Map<String, DocumentType> DOCUMENT_TYPES = new HashMap<String, DocumentType>();
	private static Map<String, AnagraficaType> ANAGRAFICHE_TYPES = new HashMap<String, AnagraficaType>();

	private static Map<String, String> enteAooDescriptionCaching = new HashMap<String, String>();

	public static void setConfig(Config configuration,
			Properties configProperties) {

		config = configuration;
		configurationProperties = configProperties;
	}

	public BatchPopolamentoFonte() throws Exception {

		if (config == null) {
			throw new Exception(
					"file config non impostato: invocare im metodo statico setConfig prima di una nuova istanza");
		}
		if (configurationProperties == null) {
			throw new Exception(
					"file configurationProperties non impostato: invocare im metodo statico setConfig prima di una nuova istanza");
		}

		if (configurationProperties.getProperty("cacheDir") == null) {
			writeError("variabile cacheDir non trovata nel file /configuration.properties");
			throw new Exception(
					"variabile cacheDir non trovata nel file /configuration.properties");
		}

		//
		// acConfigurationFile = new File(cacheDir,
		// "archivio_corrente_configuration.xml");
		// if (!acConfigurationFile.exists()) {
		// writeWarning("file di configurazione Business Logic WSFonteDocer non trovato: "
		// + acConfigurationFile.getAbsolutePath());
		// //throw new
		// Exception("file di configurazione Business Logic WSFonteDocer non trovato: "
		// + acConfigurationFile.getAbsolutePath());
		// }

		if (configurationProperties.getProperty("cacheAttachments") == null) {
			writeError("variabile cacheAttachments non trovata nel file /configuration.properties");
			throw new Exception(
					"variabile cacheAttachments non trovata nel file /configuration.properties");
		}
		cacheAttachments = Boolean.valueOf(configurationProperties
				.getProperty("cacheAttachments"));

		if (configurationProperties.getProperty("cacheDir") == null) {
			writeError("variabile cacheDir non trovata nel file /configuration.properties");
			throw new Exception(
					"variabile cacheDir non trovata nel file /configuration.properties");
		}

		String str = configurationProperties.getProperty("cacheDir");

		cacheDir = new File(str);
		if (!cacheDir.exists()) {
			throw new Exception("Cache Directory " + cacheDir.getAbsolutePath()
					+ " non trovata");
		}

		cacheDir = DirectoryUtils.createTempDirectory("BatchPopolamentoFonte",
				cacheDir);

		acConfigurationFileDownloaded = new File(cacheDir,
				"wsdocer_configuration_downloaded.xml");

		fileSizeThreshold = configurationProperties
				.getProperty("fileSizeThreshold");

		if (fileSizeThreshold == null) {
			writeError("variabile fileSizeThreshold non trovata nel file /configuration.properties");
			throw new Exception(
					"variabile fileSizeThreshold non trovata nel file /configuration.properties");
		}

		try {
			Integer.parseInt(fileSizeThreshold);
		} catch (NumberFormatException nfe) {
			writeError("chiave fileSizeThreshold ha formato errato: "
					+ nfe.getMessage());
			throw new Exception("chiave fileSizeThreshold ha formato errato: "
					+ nfe.getMessage());
		}

		if (configurationProperties.getProperty("primarysearch_max_results") == null) {
			writeError("variabile fileSizeThreshold non trovata nel file /configuration.properties");
			throw new Exception(
					"variabile fileSizeThreshold non trovata nel file /configuration.properties");
		}

		try {
			primarysearch_max_results = Integer
					.parseInt(configurationProperties
							.getProperty("primarysearch_max_results"));
		} catch (NumberFormatException nfe) {
			writeError("chiave ws_primarysearch_max_results ha formato errato: "
					+ nfe.getMessage());
			throw new Exception(
					"chiave ws_primarysearch_max_results ha formato errato: "
							+ nfe.getMessage());
		}

		if (configurationProperties.getProperty("timeout") == null) {
			writeError("variabile timeout non trovata nel file /configuration.properties");
			throw new Exception(
					"variabile timeout non trovata nel file /configuration.properties");
		}

		try {
			timeout = Integer.parseInt(configurationProperties
					.getProperty("timeout"));
		} catch (NumberFormatException nfe) {
			writeError("chiave timeout ha formato errato: " + nfe.getMessage());
			throw new Exception("chiave timeout ha formato errato: "
					+ nfe.getMessage());
		}

		// FileAppender appender = (FileAppender)logger.getAppender("FILE");
		// System.out.println(appender.getFile());

		// download_ac_config =
		// Boolean.parseBoolean(readConfigKey("//group[@name='batch-popolamento-fonte']/section[@name='config']/download_ac_config"));

		ws_ac_login_userid = readConfigKey("//group[@name='web-services']/section[@name='ws_archvio_corrente']/ws_ac_login_userid");
		ws_ac_login_pwd = readConfigKey("//group[@name='web-services']/section[@name='ws_archvio_corrente']/ws_ac_login_pwd");
		ws_ac_login_cod_ente = readConfigKey("//group[@name='web-services']/section[@name='ws_archvio_corrente']/ws_ac_login_cod_ente");
		ws_ac_url = readConfigKey("//group[@name='web-services']/section[@name='ws_archvio_corrente']/ws_ac_url");
		ws_ac_auth_url = readConfigKey("//group[@name='web-services']/section[@name='ws_archvio_corrente']/ws_ac_auth_url");

		ws_fonte_login_userid = readConfigKey("//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_login_userid");
		ws_fonte_login_pwd = readConfigKey("//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_login_pwd");
		ws_fonte_login_cod_ente = readConfigKey("//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_login_cod_ente");
		ws_fonte_url = readConfigKey("//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_url");
		ws_fonte_auth_url = readConfigKey("//group[@name='web-services']/section[@name='ws_fonte']/ws_fonte_auth_url");

		str = readConfigKey("//group[@name='batch-popolamento-fonte']/section[@name='config']/documenti_skip_fields");
		str = str.toUpperCase();
		str.replaceAll("^ *;", "");
		str.replaceAll("; *$", "");
		documenti_skip_fields = Arrays.asList(str.split(" *[;,] *"));

		str = readConfigKey("//group[@name='batch-popolamento-fonte']/section[@name='config']/fascicoli_skip_fields");

		str = str.toUpperCase();
		str.replaceAll("^ *;", "");
		str.replaceAll("; *$", "");
		fascicoli_skip_fields = Arrays.asList(str.split(" *[;,] *"));

		List<OMElement> omeFascicoloMigrationCriteria = config
				.getNodes("//group[@name='batch-popolamento-fonte']/section[@name='migration_criteria']/*");

		try {
			migrationFilterCriteriaList = MigrationFilterCriteriaManager
					.buildMigrationFilterCriteria(omeFascicoloMigrationCriteria);
			writeDebug("letti " + migrationFilterCriteriaList.size()
					+ " filtri di ricerca fonti");
		} catch (Exception e) {
			writeError("Errore ConfigManager.buildSearchFilterCriteria: "
					+ e.getMessage());
			throw new Exception(
					"Errore ConfigManager.buildSearchFilterCriteria: "
							+ e.getMessage());
		}

	}

	private String readConfigKey(String xpath) throws Exception {
		OMElement omelement = config.getNode(xpath);
		if (omelement == null) {
			writeError("chiave " + xpath + " mancante in configurazione");
			throw new Exception("chiave " + xpath
					+ " mancante in configurazione");
		}

		return omelement.getText();
	}

	private void writeDebug(String s) {
		logger.debug(s);
	}

	private void writeInfo(String s) {
		logger.info(s);

		if (fonteLog != null) {
			fonteLog.info(s);
		}

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

	private boolean downloadACConfigurationFile() {

		ReadConfig readConfig = new ReadConfig();
		readConfig.setToken(ticket_ws_ac);

		String configurationAsString;
		ReadConfigResponse response;
		try {
			response = acDocerWSClient.readConfig(readConfig);
			writeDebug("scaricata configurazione Business Logic DocER da WS Archivio Corrente: "
					+ ws_ac_url);

			configurationAsString = response.get_return();
		} catch (RemoteException e) {
			writeError("downloadACConfigurationFile: acDocerWSClient.readConfig: "
					+ e.getMessage());
			return false;
		} catch (DocerExceptionException e) {
			writeError("downloadACConfigurationFile: acDocerWSClient.readConfig: "
					+ e.getMessage());
			return false;
		}

		try {
			FileUtils.writeStringToFile(acConfigurationFileDownloaded,
					configurationAsString, "UTF-8");
			writeDebug("salvata configurazione Business Logic WSDocER in locale: "
					+ acConfigurationFileDownloaded.getAbsolutePath());

			WriteConfig writeConfig = new WriteConfig();
			writeConfig.setToken(ticket_ws_fonte);
			writeConfig.setXmlConfig(configurationAsString);
			fonteDocerWSClient.writeConfig(writeConfig);

			writeDebug("sovrascritta configurazione del WSFonteDocer: "
					+ ws_fonte_url);
			return true;
		} catch (RemoteException e) {
			writeError("downloadACConfigurationFile: fonteDocerWSClient.writeConfig: "
					+ e.getMessage());
			return false;
		} catch (DocerExceptionException e) {
			writeError("downloadACConfigurationFile: fonteDocerWSClient.writeConfig: "
					+ e.getMessage());
			return false;
		} catch (IOException e) {
			writeError("downloadACConfigurationFile: errore salvataggio configuration.xml in locale: "
					+ e.getMessage());
			return false;
		}

	}

	private static String regexpFascicoliSec = "^([^/]+)/([12][0-9][0-9][0-9])/(.+)$";
	private static Pattern patternFascicoliSec = Pattern
			.compile(regexpFascicoliSec);
	private static int classificaPosition = 1;
	private static int annoFascicoloPosition = 2;
	private static int progrFascicoloPosition = 3;

	private boolean initBatch() {

		emf = new EmailFactory(config);

		severe_error = false;
		if (!initWSClientsAC()) {
			return false;
		}

		if (!initWSClientsFonte()) {
			return false;
		}

		try {
			ticket_ws_ac = loginWSAC();
			writeDebug("recuperato ticket_ws_ac: " + ticket_ws_ac);
		} catch (Exception e) {
			writeError("Errore recupero ticket_ws_ac: " + e.getMessage());
			return false;
		}

		if (ticket_ws_ac == null) {
			writeError("Errore recupero ticket_ws_ac: ticket vuoto");
			return false;
		}

		try {
			ticket_ws_fonte = loginWSFonte();
			writeDebug("recuperato ticket_ws_fonte: " + ticket_ws_fonte);
		} catch (Exception e) {
			writeError("Errore recupero ticket_ws_fonte: " + e.getMessage());
			return false;
		}

		if (ticket_ws_fonte == null) {
			writeError("Errore recupero ticket_ws_fonte: ticket vuoto");
			return false;
		}

		// if (download_ac_config || !acConfigurationFileDownloaded.exists()) {

		// la configurazione AC deve essere sempre scaricata per allineamento
		// ambienti
		if (!downloadACConfigurationFile()) {
			return false;
		}
		// }

		if (!acConfigurationFileDownloaded.exists()) {
			writeError("il file di configurazione Business Logic DocER locale non esiste: "
					+ acConfigurationFileDownloaded.getAbsolutePath());
			return false;
		}

		if (!initACBusinessLogicConfiguration()) {
			return false;
		}

		return true;
	}

	private static BusinessLogicConfiguration blc = new BusinessLogicConfiguration();

	private boolean initACBusinessLogicConfiguration() {

		try {
			if (acConfigurationFileDownloaded == null
					|| !acConfigurationFileDownloaded.exists()) {
				throw new Exception(
						"acConfigurationFileDownloaded non trovato: "
								+ acConfigurationFileDownloaded
										.getAbsolutePath());
			}

			blc.setConfigPath(acConfigurationFileDownloaded.getAbsolutePath());

			blc.initConfiguration();

			FIELDS = blc.getFIELDS();
			DOCUMENT_TYPES = blc.getDOCUMENT_TYPES();
			ANAGRAFICHE_TYPES = blc.getANAGRAFICHE_TYPES();
			enteAooDescriptionCaching.clear();
			// MIGRATION_MODE = blc.isMIGRATION_MODE();

			// private static String regexpFascicoliSec =
			// "^([^/]+)/([12][0-9][0-9][0-9])/(.+)$";
			patternFascicoliSec = blc.getPatternFascicoliSec();
			classificaPosition = blc.getClassificaPosition();
			annoFascicoloPosition = blc.getAnnoFascicoloPosition();
			progrFascicoloPosition = blc.getProgrFascicoloPosition();

			// allow_record_versioning_archive =
			// blc.isAllow_record_versioning_archive();
			// allow_record_add_allegato = blc.isAllow_record_add_allegato();
			// allow_pubblicato_add_related =
			// blc.isAllow_pubblicato_add_related();

			return true;
		} catch (Exception e) {
			writeError(e.getMessage());
			return false;
		}

	}

	private void finishBatch() {
		logoutWSAC();
		logoutWSFonte();
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		writeInfo("-----------------------------------------");
		writeInfo("-----------------------------------------");
		writeInfo("Inizio esecuzione Batch Popolamento Fonte");

		cntErrors = 0;
		cntFascicoliCreated = 0;
		cntFascicoliUpdated = 0;
		cntDocumentiCreated = 0;
		cntDocumentiUpdated = 0;

		anagraficheCache.clear();

		// fascicoliLavorati.clear();
		// docIdLavorati.clear();

		// inizializzo e faccio i login
		if (!initBatch()) {
			severe_error = true;
		}

		List<String> fontiConfigurate = getFontiConfigurate();
		// ciclo le fonti
		for (String id_fonte : fontiConfigurate) {

			OrderedList<MigrationFilterCriteria> migrationFilterCriteriaByFonte = getMigrationFilterCriteriaByFonteId(id_fonte);

			if (migrationFilterCriteriaByFonte.size() == 0) {
				continue;
			}

			cntErrors = 0;
			cntFascicoliCreated = 0;
			cntFascicoliUpdated = 0;
			cntDocumentiCreated = 0;
			cntDocumentiUpdated = 0;

			if (!severe_error) {

				try {
					fonteLog = new BatchLog(cacheDir, id_fonte);

					writeInfo("Esecuzione Batch Popolamento Fonte per fonte "
							+ id_fonte + " del giorno "
							+ new DateTime().toString("dd-MM-yyyy"));

					for (int position = 0; position < migrationFilterCriteriaByFonte
							.size(); position++) {

						MigrationFilterCriteria singleMigrationFilterCriteria = migrationFilterCriteriaByFonte
								.get(position);

						writeInfo("");
						writeInfo("migration filter criteria: id filtro "
								+ singleMigrationFilterCriteria.getId()
								+ "; id Fonte "
								+ singleMigrationFilterCriteria.getIdFonte()
								+ "; isFascicoloCriteria="
								+ singleMigrationFilterCriteria
										.isFascicoloCriteria());

						if (singleMigrationFilterCriteria.isFascicoloCriteria()) {
							manageFascicoloMigrationCriteria(singleMigrationFilterCriteria);
						} else {
							manageDocumentoMigrationCriteria(singleMigrationFilterCriteria);
						}
					}
				} catch (Exception e) {
					writeError(e.getMessage());
				}

			} else {
				cntErrors = 1;
			}

			sendMail(id_fonte);
			if (fonteLog != null) {
				fonteLog.Close();
				fonteLog.deleteLogFile();
				fonteLog = null;
			}

		}

		finishBatch();

		writeInfo("Fine esecuzione");

	}

	private void sendMail(String id_fonte) {

		File fileToSend = null;

		String message = "Resoconto Batch Popolamento Fonte\n\t\tFonte: "
				+ id_fonte + "\n\t\tErrori: " + cntErrors
				+ "\n\t\tFascicoli creati: " + cntFascicoliCreated
				+ "\n\t\tFascicoli aggiornati: " + cntFascicoliUpdated
				+ "\n\t\tDocumenti creati: " + cntDocumentiCreated
				+ "\n\t\tDocumenti aggiornati: " + cntDocumentiUpdated;

		if (severe_error) {
			message = "Resoconto Batch Popolamento Fonte: fonte " + id_fonte
					+ ": errore grave nel setup del batch o nei login ai WS";
			fileToSend = Log4jUtils.getLogFile(logger, "F");
		} else {
			fileToSend = fonteLog.getLog();
		}

		writeInfo(message);
		lastMessage = message;

		try {
			String priority = "Low priority";
			if (cntErrors > 0) {
				priority = "High priority";
			}

			boolean sent = emf.sendMultiPartEmailMail(id_fonte, message,
					fileToSend, "Batch Popolamento Fonte", priority);

			if (sent) {
				writeInfo("e-mail resoconto inviata");
			} else {
				writeInfo("e-mail resoconto NON inviata");
			}

			writeInfo("");
		} catch (Exception e) {
			writeError("errore invio email resoconto: " + e.getMessage());
		}
	}

	private void manageFascicoloMigrationCriteria(
			MigrationFilterCriteria singleMigrationFilterCriteria) {

		String id_fonte = singleMigrationFilterCriteria.getIdFonte();
		boolean force_update = singleMigrationFilterCriteria.getForceUpdate();

		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

		for (String key : singleMigrationFilterCriteria.getObjectCriteria()
				.keySet()) {
			searchCriteria.put(key, singleMigrationFilterCriteria
					.getObjectCriteria().get(key));
		}

		if (!force_update) {
			searchCriteria.put(NEEDS_FONTE_UPDATE, Arrays.asList("true"));
		}

		SearchAnagraficheResponse searchAnagraficheResponse = null;

		try {

			SearchAnagrafiche searchAnagrafiche = new SearchAnagrafiche();
			searchAnagrafiche.setType("FASCICOLO");
			searchAnagrafiche.setToken(ticket_ws_ac);
			searchAnagrafiche
					.setSearchCriteria(toKeyValuePairArray2(searchCriteria));

			writeInfo("");
			writeInfo("Ricerca Fascicoli su AC per id filtro: "
					+ singleMigrationFilterCriteria.getId() + " (force_update="
					+ force_update + ")");
			searchAnagraficheResponse = acDocerWSClient
					.searchAnagrafiche(searchAnagrafiche);

			if (searchAnagraficheResponse == null
					|| searchAnagraficheResponse.get_return() == null
					|| searchAnagraficheResponse.get_return().length == 0) {
				writeInfo("trovati 0 fascicoli per id filtro: "
						+ singleMigrationFilterCriteria.getId()
						+ " (force_update=" + force_update + ")");
				return;
			}

		} catch (Exception e) {
			writeError("acDocerWSClient: Errore acDocerWSClient.searchAnagrafiche: id filtro: "
					+ singleMigrationFilterCriteria.getId()
					+ ": "
					+ e.getMessage());
			return;
		}

		writeInfo("trovati " + searchAnagraficheResponse.get_return().length
				+ " fascicoli per id filtro: "
				+ singleMigrationFilterCriteria.getId() + " (force_update="
				+ force_update + ")");

		Map<String, List<String>> documentiFascicoloCriteria = new HashMap<String, List<String>>();
		Map<String, String> id = new HashMap<String, String>();

		for (SearchItem si : searchAnagraficheResponse.get_return()) {

			String cod_ente = extractValueByName("fascicolo", si.getMetadata(),
					"COD_ENTE", true, "acDocerWSClient.searchAnagrafiche()");
			String cod_aoo = extractValueByName("fascicolo", si.getMetadata(),
					"COD_AOO", true, "acDocerWSClient.searchAnagrafiche()");
			String classifica = extractValueByName("fascicolo",
					si.getMetadata(), "CLASSIFICA", true,
					"acDocerWSClient.searchAnagrafiche()");
			String anno_fascicolo = extractValueByName("fascicolo",
					si.getMetadata(), "ANNO_FASCICOLO", true,
					"acDocerWSClient.searchAnagrafiche()");
			String progr_fascicolo = extractValueByName("fascicolo",
					si.getMetadata(), "PROGR_FASCICOLO", true,
					"acDocerWSClient.searchAnagrafiche()");

			GetFascicoloResponse getFascicoloResponse = null;
			String strIdFascicolo = null;
			try {
				strIdFascicolo = generateId("FASCICOLO", new String[] {
						cod_ente, cod_aoo, classifica, anno_fascicolo,
						progr_fascicolo });

				writeInfo("> elaborazione Fascicolo: " + strIdFascicolo + "...");

				GetFascicolo getFascicolo = new GetFascicolo();
				getFascicolo.setToken(ticket_ws_ac);
				getFascicolo.setFascicoloId(getFascicoloId(si));
				getFascicoloResponse = acDocerWSClient
						.getFascicolo(getFascicolo);

				if (getFascicoloResponse == null
						|| getFascicoloResponse.get_return() == null) {
					writeInfo("impossibile recuperare profilo fascicolo "
							+ strIdFascicolo);
					return;
				}
			} catch (Exception e) {
				writeError("acDocerWSClient: Errore acDocerWSClient.getFascicolo(getFascicolo): "
						+ strIdFascicolo + ": " + e.getMessage());
				return;
			}

			boolean needsFonteUpdateFascicolo = Boolean
					.valueOf(extractValueByName("fascicolo",
							getFascicoloResponse.get_return(),
							NEEDS_FONTE_UPDATE, true,
							"acDocerWSClient.getFascicolo()"));

			writeInfo("NEEDS_FONTE_UPDATE=" + needsFonteUpdateFascicolo
					+ " per il Fascicolo " + strIdFascicolo);

			if (needsFonteUpdateFascicolo || force_update) {

				if (!needsFonteUpdateFascicolo && force_update) {
					writeInfo("forceUpdate=true per id filtro: "
							+ singleMigrationFilterCriteria.getId());
				}

				id.clear();

				try {
					checkEnte(cod_ente);
				} catch (Exception e) {
					writeError("Errore checkEnte: " + cod_ente + ": "
							+ e.getMessage());
					continue;
				}

				id.put("COD_ENTE", cod_ente);
				id.put("COD_AOO", cod_aoo);
				try {
					checkAOO(id);
				} catch (Exception e) {
					writeError("Errore checkAOO: " + id.toString() + ": "
							+ e.getMessage());
					continue;
				}

				id.put("CLASSIFICA", classifica);
				try {
					titolarioTaxonomy.clear();
					checkTitolario(id);
				} catch (Exception e) {
					writeError("Errore checkTitolario: " + id.toString() + ": "
							+ e.getMessage());
					continue;
				}
				id.put("ANNO_FASCICOLO", anno_fascicolo);
				id.put("PROGR_FASCICOLO", progr_fascicolo);

				try {
					// checkFascicolo(id, id_fonte,
					// singleMigrationFilterCriteria.getCfPersonaSimulata(),
					// singleMigrationFilterCriteria.getCfAziendaSimulata());
					fascicoloTaxonomy.clear();
					checkFascicolo(id, id_fonte,
							getFascicoloResponse.get_return());
				} catch (Exception e) {
					writeError("Errore checkFascicolo: " + id.toString() + ": "
							+ e.getMessage());
					continue;
				}

			} else {
				continue;
			}

			boolean errorsOnDocuments = false;
			if (singleMigrationFilterCriteria.doSearchDocumentiFascicolo()) {

				writeInfo("ricerca documenti Principali del fascicolo...");
				// docnumsFascicoloFonte.clear();
				documentiFascicoloCriteria.clear();

				documentiFascicoloCriteria.put("TIPO_COMPONENTE",
						Arrays.asList("PRINCIPALE"));
				documentiFascicoloCriteria.put("COD_ENTE",
						Arrays.asList(cod_ente));
				documentiFascicoloCriteria.put("COD_AOO",
						Arrays.asList(cod_aoo));
				documentiFascicoloCriteria.put("CLASSIFICA",
						Arrays.asList(classifica));
				documentiFascicoloCriteria.put("PROGR_FASCICOLO",
						Arrays.asList(progr_fascicolo));
				documentiFascicoloCriteria.put("ANNO_FASCICOLO",
						Arrays.asList(anno_fascicolo));

				// cerco i documenti nell'archivio corrente in base ai
				// filtri di ricerca
				for (String key : singleMigrationFilterCriteria
						.getChildrenObjectCriteria().keySet()) {

					if (key.equals("TIPO_COMPONENTE")) {
						continue;
					}

					List<String> singleCriteria = singleMigrationFilterCriteria
							.getChildrenObjectCriteria().get(key);

					if (documentiFascicoloCriteria.containsKey(key)) {
						documentiFascicoloCriteria.get(key).addAll(
								singleCriteria);
					} else {
						documentiFascicoloCriteria.put(key, singleCriteria);
					}

				}

				// ricerco tutti i documenti principali del fascicolo
				try {
					KeyValuePair[] fascicoloId = toKeyValuePairArray2(documentiFascicoloCriteria);
					SearchDocuments searchDocumentsAC = new SearchDocuments();
					searchDocumentsAC.setToken(ticket_ws_ac);
					searchDocumentsAC.setKeywords(new String[] { "" });
					// searchDocuments.setOrderby(null);
					searchDocumentsAC.setMaxRows(primarysearch_max_results);
					searchDocumentsAC.setSearchCriteria(fascicoloId);

					SearchDocumentsResponse searchDocumentsResponseAC = acDocerWSClient
							.searchDocuments(searchDocumentsAC);

					List<String> documentsOfFascicoloAC = new ArrayList<String>();

					if (searchDocumentsResponseAC == null
							|| searchDocumentsResponseAC.get_return() == null) {
						writeInfo("nessun documento Principale trovato nel Fascicolo");
					} else {
						writeInfo("trovati "
								+ searchDocumentsResponseAC.get_return().length
								+ " documenti Principali nel Fascicolo");

						for (SearchItem siDoc : searchDocumentsResponseAC
								.get_return()) {

							String docId = extractValueByName("documento",
									siDoc.getMetadata(), "DOCNUM", true,
									"acDocerWSClient.searchDocuments()");
							documentsOfFascicoloAC.add(docId);
							try {
								writeInfo(">> elaborazione documento " + docId
										+ " del fascicolo...");
								checkDocumento(docId, true, id_fonte, null);
							} catch (Exception e) {
								errorsOnDocuments = true;
								writeError("Errore checkDocumento: "
										+ e.getMessage());
								continue;
							}
						}
					}

					writeInfo("controllo documenti spuri del fascicolo sulla Fonte...");
					// ricerco documenti del fascicolo sulla fonte (per
					// verificare se qualcuno ha cambiato fascicolo o e' stato
					// sfascicolato)
					SearchDocuments searchDocumentsFonte = new SearchDocuments();
					searchDocumentsFonte.setToken(ticket_ws_fonte);
					searchDocumentsFonte.setKeywords(new String[] { "" });
					// searchDocuments.setOrderby(null);
					searchDocumentsFonte.setMaxRows(primarysearch_max_results);
					searchDocumentsFonte.setSearchCriteria(fascicoloId);

					SearchDocumentsResponse searchDocumentsResponseFonte = fonteDocerWSClient
							.searchDocuments(searchDocumentsFonte);
					if (searchDocumentsResponseFonte != null
							&& searchDocumentsResponseFonte.get_return() != null) {

						for (SearchItem siDoc : searchDocumentsResponseFonte
								.get_return()) {

							String docId = extractValueByName("documento",
									siDoc.getMetadata(), "DOCNUM", true,
									"fonteDocerWSClient.searchDocuments()");

							if (documentsOfFascicoloAC.contains(docId)) {
								// il fascicolo e' contenuto su AC e su Fonte
								continue;
							}
							try {
								writeInfo(">> elaborazione documento spurio "
										+ docId + "...");
								checkDocumentoSpurio(docId);
							} catch (Exception e) {
								errorsOnDocuments = true;
								writeError("Errore checkDocumento: "
										+ e.getMessage());
								continue;
							}
						}
					}

				} catch (Exception e) {
					errorsOnDocuments = true;
					writeError("AC: acDocerWSClient.searchDocuments: "
							+ e.getMessage());
				}

			} else {
				writeInfo("id filtro "
						+ singleMigrationFilterCriteria.getId()
						+ " e' impostato per non eseguire la ricerca dei documenti del Fascicolo");
			}

			try {
				setNeedsRaccoglitoreUpdateFascicolo(id, true);
			} catch (Exception e) {
				writeError(e.getMessage());
				continue;
			}

			try {
				// aggiorno il flag solo se e' andato tutto bene
				if (!errorsOnDocuments) {
					setNeedsFonteUpdateFascicolo(id, false);
				}
			} catch (Exception e) {
				writeError(e.getMessage());
				continue;
			}

		} // fine ciclo fascicoli
	}

	private void manageDocumentoMigrationCriteria(
			MigrationFilterCriteria singleMigrationFilterCriteria) {

		boolean force_update = singleMigrationFilterCriteria.getForceUpdate();

		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

		for (String key : singleMigrationFilterCriteria.getObjectCriteria()
				.keySet()) {
			searchCriteria.put(key, singleMigrationFilterCriteria
					.getObjectCriteria().get(key));
		}

		searchCriteria.put("TIPO_COMPONENTE", Arrays.asList("PRINCIPALE"));

		if (!force_update) {
			searchCriteria.put(NEEDS_FONTE_UPDATE, Arrays.asList("true"));
		}

		SearchDocumentsResponse searchDocumentsResponse = null;
		try {
			SearchDocuments searchDocuments = new SearchDocuments();
			searchDocuments.setToken(ticket_ws_ac);
			searchDocuments.setKeywords(new String[] { "" });
			searchDocuments.setMaxRows(primarysearch_max_results);
			searchDocuments
					.setSearchCriteria(toKeyValuePairArray2(searchCriteria));

			writeInfo("");
			writeInfo("Ricerca Documenti Principali non fascicolati su AC per id filtro: "
					+ singleMigrationFilterCriteria.getId()
					+ " (force_update="
					+ force_update + ")");
			searchDocumentsResponse = acDocerWSClient
					.searchDocuments(searchDocuments);
		} catch (Exception e) {
			writeError("AC: acDocerWSClient.searchDocuments: " + e.getMessage());
			return;
		}

		if (searchDocumentsResponse == null
				|| searchDocumentsResponse.get_return() == null) {
			writeInfo("Nessun Documento Principale trovato per id filtro="
					+ singleMigrationFilterCriteria.getId() + " (force_update="
					+ force_update + ")");
			return;
		}

		writeInfo("trovati " + searchDocumentsResponse.get_return().length
				+ " Documenti Principali per id filtro="
				+ singleMigrationFilterCriteria.getId() + " (force_update="
				+ force_update + ")");

		Map<String, String> id = new HashMap<String, String>();
		// ciclo i risultati
		for (SearchItem si : searchDocumentsResponse.get_return()) {

			String docId = extractValueByName("documento", si.getMetadata(),
					"DOCNUM", true, "acDocerWSClient.searchDocuments()");

			writeInfo("> elaborazione Documento: " + docId + "...");

			if (docId == null || docId.equals("")) {
				writeError("il Documento " + docId + " non ha DOCNUM assegnato");
				continue;
			}

			// recupero profilo documento su Archivio Corrente
			GetProfileDocument getProfileDocument = new GetProfileDocument();
			getProfileDocument.setToken(ticket_ws_ac);
			getProfileDocument.setDocId(docId);
			GetProfileDocumentResponse getProfileDocumentResponse = null;

			try {
				getProfileDocumentResponse = acDocerWSClient
						.getProfileDocument(getProfileDocument);
			} catch (Exception e) {
				writeError("AC: acDocerWSClient.getProfileDocument: " + docId
						+ ": " + e.getMessage());
				continue;
			}

			KeyValuePair[] documentProfileAC = getProfileDocumentResponse
					.get_return();

			String cod_ente = null;
			String cod_aoo = null;
			String classifica = null;
			String anno_fascicolo = null;
			String progr_fascicolo = null;
			String fasc_secondari = null;
			boolean needsFonteUpdateDocumento = false;

			for (KeyValuePair kvpAC : documentProfileAC) {
				if (kvpAC.getKey().equalsIgnoreCase("COD_ENTE")) {
					cod_ente = kvpAC.getValue();
					continue;
				}
				if (kvpAC.getKey().equalsIgnoreCase("COD_AOO")) {
					cod_aoo = kvpAC.getValue();
					continue;
				}
				if (kvpAC.getKey().equalsIgnoreCase("CLASSIFICA")) {
					classifica = kvpAC.getValue();
					continue;
				}
				if (kvpAC.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
					progr_fascicolo = kvpAC.getValue();
					continue;
				}
				if (kvpAC.getKey().equalsIgnoreCase("FASC_SECONDARI")) {
					fasc_secondari = kvpAC.getValue();
					continue;
				}
				if (kvpAC.getKey().equalsIgnoreCase(NEEDS_FONTE_UPDATE)) {
					needsFonteUpdateDocumento = Boolean.valueOf(kvpAC
							.getValue());
					continue;
				}
			}

			writeInfo("NEEDS_FONTE_UPDATE=" + needsFonteUpdateDocumento
					+ " per il Documento " + docId);

			if (needsFonteUpdateDocumento || force_update) {

				if (!needsFonteUpdateDocumento && force_update) {
					writeInfo("forceUpdate=true per id filtro "
							+ singleMigrationFilterCriteria.getId());
				}

				if (StringUtils.isNotEmpty(progr_fascicolo)) {
					writeInfo("il documento "
							+ docId
							+ " ha un fascicolo assegnato e non puo' essere migrato con un documento_criteria");
					return;
				}

				if (StringUtils.isNotEmpty(fasc_secondari)) {
					writeInfo("il documento "
							+ docId
							+ " ha fascicoli secondari assegnati e non puo' essere migrato con un documento_criteria");
					return;
				}

				id.clear();

				try {
					checkEnte(cod_ente);
				} catch (Exception e) {
					writeError("Errore checkEnte: " + cod_ente + ": "
							+ e.getMessage());
					continue;
				}

				id.put("COD_ENTE", cod_ente);
				id.put("COD_AOO", cod_aoo);
				try {
					checkAOO(id);
				} catch (Exception e) {
					writeError("Errore checkAOO: " + id.toString() + ": "
							+ e.getMessage());
					continue;
				}

				try {
					id.put("CLASSIFICA", classifica);
					checkTitolario(id);
				} catch (Exception e) {
					writeError("Errore checkTitolario: " + id.toString() + ": "
							+ e.getMessage());
					return;
				}

				try {
					checkDocumento(docId, true,
							singleMigrationFilterCriteria.getIdFonte(),
							documentProfileAC);
				} catch (Exception e) {
					writeError("Errore checkDocumento: " + e.getMessage());
					continue;
				}

				try {
					setNeedsRaccoglitoreUpdateDocumento(docId, true);
				} catch (Exception e) {
					writeError(e.getMessage());
					continue;
				}
				try {
					setNeedsFonteUpdateDocumento(docId, false);
				} catch (Exception e) {
					writeError(e.getMessage());
					continue;
				}
			} else {
				continue;
			}

		}
	}

	private KeyValuePair[] toKeyValuePairArray1(Map<String, String> map) {

		List<KeyValuePair> params = new ArrayList<KeyValuePair>();
		for (String key : map.keySet()) {
			KeyValuePair kvp = new KeyValuePair();
			kvp.setKey(key);
			kvp.setValue(map.get(key));
			params.add(kvp);
		}

		return params.toArray(new KeyValuePair[0]);

	}

	private KeyValuePair[] toKeyValuePairArray2(Map<String, List<String>> map) {

		List<KeyValuePair> params = new ArrayList<KeyValuePair>();
		for (String key : map.keySet()) {

			for (String val : map.get(key)) {
				KeyValuePair kvp = new KeyValuePair();
				kvp.setKey(key);
				kvp.setValue(val);
				params.add(kvp);
			}

		}

		return params.toArray(new KeyValuePair[0]);

	}

	private String extractValueByName(String type, KeyValuePair[] metadata,
			String propName, boolean writeErrorIfMissing, String methodName) {

		for (KeyValuePair kvp : metadata) {
			if (kvp.getKey().equalsIgnoreCase(propName)) {
				return kvp.getValue();
			}
		}

		if (writeErrorIfMissing) {
			writeError("metadato " + propName + " assente; tipo "
					+ type.toUpperCase() + "; metodo " + methodName);
		}

		return null;

	}

	private void checkEnte(String cod_ente) throws Exception {

		String strId = generateId("ENTE", new String[] { cod_ente });

		writeDebug("verifica anagrafica " + strId + "...");

		if (anagraficheCache.contains(cod_ente)) {
			writeDebug("anagrafica gia' elaborata");
			return;
		}

		// recupero Ente da AC
		GetEnte getEnte = new GetEnte();
		getEnte.setToken(ticket_ws_ac);
		getEnte.setCodiceEnte(cod_ente);

		GetEnteResponse getEnteResponse;
		try {
			getEnteResponse = acDocerWSClient.getEnte(getEnte);
		} catch (Exception e) {
			throw new Exception("acDocerWSClient.getEnte: " + strId + ": "
					+ e.getMessage());
		}

		KeyValuePair[] enteProfileAC = getEnteResponse.get_return();

		// ricerco sulla Fonte per verificare esistenza
		SearchAnagrafiche searchAnagrafiche = new SearchAnagrafiche();
		searchAnagrafiche.setToken(ticket_ws_fonte);
		searchAnagrafiche.setType("ENTE");

		KeyValuePair kvp = new KeyValuePair();
		kvp.setKey("COD_ENTE");
		kvp.setValue(cod_ente);

		searchAnagrafiche.addSearchCriteria(kvp);

		SearchAnagraficheResponse response;
		try {
			response = fonteDocerWSClient.searchAnagrafiche(searchAnagrafiche);
		} catch (Exception e) {
			throw new Exception("fonteDocerWSClient.searchAnagrafiche: "
					+ strId + ": " + e.getMessage());
		}

		// non esiste sulla Fonte
		if (response == null || response.get_return() == null
				|| response.get_return().length == 0) {
			CreateEnte createEnte = new CreateEnte();
			createEnte.setToken(ticket_ws_fonte);
			createEnte.setEnteInfo(enteProfileAC);
			try {
				fonteDocerWSClient.createEnte(createEnte);
			} catch (Exception e) {
				throw new Exception("fonteDocerWSClient.createEnte: " + strId
						+ ": " + e.getMessage());
			}

			writeDebug("Ente creato nel WS Fonte: " + strId);
			anagraficheCache.add(cod_ente);
		} else if (response.get_return().length != 1) {
			anagraficheCache.add(cod_ente);
			throw new Exception("trovati " + response.get_return().length + " "
					+ strId + " nel repository della Fonte");
		} else {

			String acValue = extractValueByName("ente", enteProfileAC,
					"DES_ENTE", true, "acDocerWSClient.getEnte()");
			String fonteValue = extractValueByName("ente",
					response.get_return()[0].getMetadata(), "DES_ENTE", true,
					"fonteDocerWSClient.searchAnagrafiche()");

			if (!acValue.equals(fonteValue)) {
				UpdateEnte updateEnte = new UpdateEnte();
				updateEnte.setToken(ticket_ws_fonte);
				updateEnte.setEnteInfo(enteProfileAC);
				updateEnte.setCodiceEnte(cod_ente);
				try {
					fonteDocerWSClient.updateEnte(updateEnte);
				} catch (Exception e) {
					throw new Exception("fonteDocerWSClient.updateEnte: "
							+ strId + ": " + e.getMessage());
				}

				writeDebug("Descrizione Ente aggiornata nel WS Fonte: " + strId);
			}
			anagraficheCache.add(cod_ente);
		}
	}

	private void checkAOO(Map<String, String> id) throws Exception {

		String strId = generateId("AOO",
				new String[] { id.get("COD_ENTE"), id.get("COD_AOO") });

		writeDebug("verifica anagrafica: " + strId + "...");

		if (anagraficheCache.contains(strId)) {
			writeDebug("anagrafica gia' elaborata");
			return;
		}

		// aoo non esiste nel WS Fonte
		GetAOO getAoo = new GetAOO();
		getAoo.setToken(ticket_ws_ac);
		getAoo.setAooId(toKeyValuePairArray1(id));

		// aoo
		GetAOOResponse getAOOResponse;
		try {
			getAOOResponse = acDocerWSClient.getAOO(getAoo);
		} catch (Exception e) {
			throw new Exception("acDocerWSClient.getAOO: " + strId + ": "
					+ e.getMessage());
		}

		KeyValuePair[] aooProfileAC = getAOOResponse.get_return();

		// ricerco
		SearchAnagrafiche searchAnagrafiche = new SearchAnagrafiche();
		searchAnagrafiche.setToken(ticket_ws_fonte);
		searchAnagrafiche.setType("AOO");
		searchAnagrafiche.setSearchCriteria(toKeyValuePairArray1(id));

		SearchAnagraficheResponse response;
		try {
			response = fonteDocerWSClient.searchAnagrafiche(searchAnagrafiche);
		} catch (Exception e) {
			throw new Exception("fonteDocerWSClient.searchAnagrafiche: "
					+ strId + ": " + e.getMessage());
		}

		if (response == null || response.get_return() == null
				|| response.get_return().length == 0) {

			// la creo
			CreateAOO createAOO = new CreateAOO();
			createAOO.setToken(ticket_ws_fonte);
			createAOO.setAooInfo(aooProfileAC);
			try {
				fonteDocerWSClient.createAOO(createAOO);
			} catch (Exception e) {
				throw new Exception("fonteDocerWSClient.createAOO: " + strId
						+ ": " + e.getMessage());
			}
			writeDebug("AOO creata nel WS Fonte: " + strId);
			anagraficheCache.add(strId);
		} else if (response.get_return().length != 1) {
			anagraficheCache.add(strId);
			throw new Exception("trovati " + response.get_return().length
					+ " anagrafiche AOO nel repository della Fonte: " + strId);
		} else {
			String acValue = extractValueByName("aoo", aooProfileAC, "DES_AOO",
					true, "acDocerWSClient.getAOO()");
			String fonteValue = extractValueByName("aoo",
					response.get_return()[0].getMetadata(), "DES_AOO", true,
					"fonteDocerWSClient.searchAnagrafiche()");

			if (!acValue.equals(fonteValue)) {
				UpdateAOO updateAOO = new UpdateAOO();
				updateAOO.setToken(ticket_ws_fonte);
				updateAOO.setAooId(toKeyValuePairArray1(id));
				updateAOO.setAooInfo(aooProfileAC);
				try {
					fonteDocerWSClient.updateAOO(updateAOO);
				} catch (Exception e) {
					throw new Exception("fonteDocerWSClient.updateAOO: "
							+ strId + ": " + e.getMessage());
				}
				writeDebug("Descrizione AOO aggiornata nel WS Fonte: " + strId);
			}

			anagraficheCache.add(strId);
		}
	}

	private void checkTitolario(Map<String, String> id) throws Exception {

		String strId = generateId(
				"TITOLARIO",
				new String[] { id.get("COD_ENTE"), id.get("COD_AOO"),
						id.get("CLASSIFICA") });

		writeDebug("verifica anagrafica: " + strId + "...");

		if (anagraficheCache.contains(strId)) {
			writeDebug("anagrafica gia' elaborata");
			return;
		}

		if (titolarioTaxonomy.contains(strId)) {
			writeDebug("voce di titolario gia' esplorata");
			return;
		}

		titolarioTaxonomy.add(strId);

		GetTitolario getTitolario = new GetTitolario();
		getTitolario.setToken(ticket_ws_ac);
		getTitolario.setTitolarioId(toKeyValuePairArray1(id));

		// ricerco titolario in archivio corrente
		GetTitolarioResponse getTitolarioResponse;
		try {
			getTitolarioResponse = acDocerWSClient.getTitolario(getTitolario);
		} catch (Exception e) {
			throw new Exception("acDocerWSClient.getTitolario: " + strId + ": "
					+ e.getMessage());
		}

		KeyValuePair[] titolarioProfileAC = getTitolarioResponse.get_return();

		SearchAnagrafiche searchAnagrafiche = new SearchAnagrafiche();
		searchAnagrafiche.setToken(ticket_ws_fonte);
		searchAnagrafiche.setType("TITOLARIO");
		searchAnagrafiche.setSearchCriteria(toKeyValuePairArray1(id));

		SearchAnagraficheResponse response;
		try {
			response = fonteDocerWSClient.searchAnagrafiche(searchAnagrafiche);
		} catch (Exception e) {
			throw new Exception("fonteDocerWSClient.searchAnagrafiche: "
					+ strId + ": " + e.getMessage());
		}

		if (response == null || response.get_return() == null
				|| response.get_return().length == 0) {
			// il titolario non esiste nel WS Fonte
			// prima di creare il Titolario controllo il Padre
			String parentClassifica = extractValueByName("titolario",
					titolarioProfileAC, "PARENT_CLASSIFICA", true,
					"fonteDocerWSClient.searchAnagrafiche()");
			String classifica = extractValueByName("titolario",
					titolarioProfileAC, "CLASSIFICA", true,
					"fonteDocerWSClient.searchAnagrafiche()");

			if (parentClassifica != null && !parentClassifica.equals("")
					&& !parentClassifica.equals(classifica)) {

				Map<String, String> idPadre = new HashMap<String, String>();
				for (String key : id.keySet()) {
					idPadre.put(key, id.get(key));
				}

				// sovrascrivo la classifica
				idPadre.put("CLASSIFICA", parentClassifica);

				// controllo il padre
				checkTitolario(idPadre);

			}

			// lo creo
			CreateTitolario createTitolario = new CreateTitolario();
			createTitolario.setToken(ticket_ws_fonte);
			createTitolario.setTitolarioInfo(titolarioProfileAC);
			try {
				fonteDocerWSClient.createTitolario(createTitolario);
			} catch (Exception e) {
				throw new Exception("fonteDocerWSClient.createTitolario: "
						+ strId + ": " + e.getMessage());
			}
			writeDebug("Titolario creato nel WS Fonte: " + strId);
			anagraficheCache.add(strId);
		} else if (response.get_return().length != 1) {
			anagraficheCache.add(strId);
			throw new Exception("trovati " + response.get_return().length
					+ " anagrafiche TITOLARIO nel repository della Fonte: "
					+ strId);
		} else {
			String acValue = extractValueByName("titolario",
					titolarioProfileAC, "DES_TITOLARIO", true,
					"acDocerWSClient.getTitolario()");
			String fonteValue = extractValueByName("titolario",
					response.get_return()[0].getMetadata(), "DES_TITOLARIO",
					true, "fonteDocerWSClient.searchAnagrafiche()");

			if (!acValue.equals(fonteValue)) {
				UpdateTitolario updateTitolario = new UpdateTitolario();
				updateTitolario.setToken(ticket_ws_fonte);
				updateTitolario.setTitolarioId(toKeyValuePairArray1(id));
				updateTitolario.setTitolarioInfo(titolarioProfileAC);
				try {
					fonteDocerWSClient.updateTitolario(updateTitolario);
				} catch (Exception e) {
					throw new Exception("fonteDocerWSClient.updateTitolario: "
							+ strId + ": " + e.getMessage());
				}
				writeDebug("Descrizione Titolario aggiornata nel WS Fonte: "
						+ strId);
			}

			anagraficheCache.add(strId);
		}

	}

	// private void checkFascicolo(Map<String, String> id, String id_fonte,
	// String cfPersonaSimulata, String cfAziendaSimulata) throws Exception {
	private void checkFascicolo(Map<String, String> id, String id_fonte,
			KeyValuePair[] fascicoloProfileAC) throws Exception {

		String strId = generateId(
				"FASCICOLO",
				new String[] { id.get("COD_ENTE"), id.get("COD_AOO"),
						id.get("CLASSIFICA"), id.get("ANNO_FASCICOLO"),
						id.get("PROGR_FASCICOLO") });

		writeDebug("verifica anagrafica: " + strId + "...");

		// if (fascicoliLavorati.contains(strId)) {
		// writeDebug("fascicolo gia' elaborato");
		// return;
		// }

		if (fascicoloTaxonomy.contains(strId)) {
			writeDebug("fascicolo gia' esplorato");
			return;
		}

		fascicoloTaxonomy.add(strId);

		KeyValuePair[] fascicoloId = toKeyValuePairArray1(id);

		if (fascicoloProfileAC == null) {
			GetFascicolo getFascicolo = new GetFascicolo();
			getFascicolo.setToken(ticket_ws_ac);
			getFascicolo.setFascicoloId(fascicoloId);

			// recupero fascicolo su archivio corrente
			GetFascicoloResponse getFascicoloResponse;
			try {
				getFascicoloResponse = acDocerWSClient
						.getFascicolo(getFascicolo);
				fascicoloProfileAC = getFascicoloResponse.get_return();
			} catch (Exception e) {
				throw new Exception("acDocerWSClient.getFascicolo: " + strId
						+ ": " + e.getMessage());
			}

		}

		List<KeyValuePair> fascicoloProfileCleaned = new ArrayList<KeyValuePair>();
		for (KeyValuePair kvp : fascicoloProfileAC) {

			if (fascicoli_skip_fields.contains(kvp.getKey())) {
				continue;
			}

			if (kvp.getKey().equalsIgnoreCase(ID_FONTE)) {
				continue;
			} else if (kvp.getKey().equalsIgnoreCase(NEEDS_FONTE_UPDATE)) {
				continue;
			} else if (kvp.getKey().equalsIgnoreCase(NEEDS_RACCOGLITORE_UPDATE)) {
				continue;
			} else if (kvp.getKey().equalsIgnoreCase(RACC_UID)) {
				continue;
			}
			// else if (kvp.getKey().equalsIgnoreCase(CF_PERSONA)) {
			// if (cfPersonaSimulata != null && !cfPersonaSimulata.equals("")) {
			// // sovrascrivo dopo
			// continue;
			// }
			// }
			// else if (kvp.getKey().equalsIgnoreCase(CF_AZIENDA)) {
			// if (cfAziendaSimulata != null && !cfAziendaSimulata.equals("")) {
			// // sovrascrivo dopo
			// continue;
			// }
			// }

			fascicoloProfileCleaned.add(kvp);
		}

		// if (cfPersonaSimulata != null && !cfPersonaSimulata.equals("")) {
		// System.out.println("cf_persona simulata: " + cfPersonaSimulata);
		// KeyValuePair kvpCfPersonaSimulata = new KeyValuePair();
		// kvpCfPersonaSimulata.setKey(CF_PERSONA);
		// kvpCfPersonaSimulata.setValue(cfPersonaSimulata);
		//
		// fascicoloProfileCleaned.add(kvpCfPersonaSimulata);
		// }
		//
		// if (cfAziendaSimulata != null && !cfAziendaSimulata.equals("")) {
		// System.out.println("cf_azienda simulata: " + cfAziendaSimulata);
		// KeyValuePair kvpCfAziendaSimulata = new KeyValuePair();
		// kvpCfAziendaSimulata.setKey(CF_AZIENDA);
		// kvpCfAziendaSimulata.setValue(cfAziendaSimulata);
		//
		// fascicoloProfileCleaned.add(kvpCfAziendaSimulata);
		// }

		if (StringUtils.isNotEmpty(id_fonte)) {

			KeyValuePair kvpIdFonte = new KeyValuePair();
			kvpIdFonte.setKey(ID_FONTE);
			kvpIdFonte.setValue(id_fonte);

			fascicoloProfileCleaned.add(kvpIdFonte);
		}

		// CONTROLLI SULLA FONTE
		SearchAnagraficheResponse searchAnagraficheResponse;
		try {

			SearchAnagrafiche searchAnagrafiche = new SearchAnagrafiche();
			searchAnagrafiche.setToken(ticket_ws_fonte);
			searchAnagrafiche.setType("FASCICOLO");
			searchAnagrafiche.setSearchCriteria(fascicoloId);

			// ricerco fascicolo sulla fonte
			searchAnagraficheResponse = fonteDocerWSClient
					.searchAnagrafiche(searchAnagrafiche);

		} catch (Exception e) {
			throw new Exception("fonteDocerWSClient.searchAnagrafiche: "
					+ strId + ": " + e.getMessage());
		}

		if (searchAnagraficheResponse == null
				|| searchAnagraficheResponse.get_return() == null
				|| searchAnagraficheResponse.get_return().length == 0) {
			// il fascicolo non esiste nel WS Fonte
			// prima di creare il Fascicolo controllo il Padre
			String parentProgressivo = extractValueByName("fascicolo",
					fascicoloProfileAC, "PARENT_PROGR_FASCICOLO", true,
					"fonteDocerWSClient.searchAnagrafiche()");
			String progressivo = extractValueByName("fascicolo",
					fascicoloProfileAC, "PROGR_FASCICOLO", true,
					"fonteDocerWSClient.searchAnagrafiche()");

			if (parentProgressivo != null && !parentProgressivo.equals("")) {
				Map<String, String> idPadre = new HashMap<String, String>();
				for (String key : id.keySet()) {
					idPadre.put(key, id.get(key));
				}

				idPadre.put("PROGR_FASCICOLO", parentProgressivo);

				// controllo il padre
				if (!parentProgressivo.equals(progressivo)) {
					// checkFascicolo(idPadre, null, null, null);
					checkFascicolo(idPadre, null, null);
				}

			}

			// creo fascicolo sulla FONTE
			CreateFascicolo createFascicolo = new CreateFascicolo();
			createFascicolo.setToken(ticket_ws_fonte);
			createFascicolo.setFascicoloInfo(fascicoloProfileCleaned
					.toArray(new KeyValuePair[0]));
			try {
				fonteDocerWSClient.createFascicolo(createFascicolo);
				writeInfo("Fascicolo creato nel WS Fonte: " + strId);
				cntFascicoliCreated++;
				// fascicoliLavorati.add(strId);
			} catch (Exception e) {
				throw new Exception("fonteDocerWSClient.createFascicolo: "
						+ strId + ": " + e.getMessage());
			}
		} else {

			// aggiorno sempre i Fascicoli che ho trovato dalla ricerca (o hanno
			// force_update oppure il NEEDS_FONTE_UPDATE=true
			// non aggiorno i fascicoli padre che devo creare obbligatoriamente
			// sulla Fonte per replicare la tassonomia
			if (StringUtils.isNotEmpty(id_fonte)) {

				UpdateFascicolo updateFascicolo = new UpdateFascicolo();
				updateFascicolo.setToken(ticket_ws_fonte);
				updateFascicolo.setFascicoloId(toKeyValuePairArray1(id));
				updateFascicolo.setFascicoloInfo(fascicoloProfileCleaned
						.toArray(new KeyValuePair[0]));
				try {
					fonteDocerWSClient.updateFascicolo(updateFascicolo);
					writeInfo("Fascicolo aggiornato nel WS Fonte: " + strId);
					cntFascicoliUpdated++;
					// fascicoliLavorati.add(strId);
				} catch (Exception e) {
					throw new Exception("fonteDocerWSClient.updateFascicolo: "
							+ strId + ": " + e.getMessage());
				}

			}

		}

	}

	private void checkFascicoloDocumentoSpurio(KeyValuePair[] documentProfile)
			throws Exception {

		String codEnte = null;
		String codAOO = null;
		String classifica = null;
		String annoFascicolo = null;
		String progrFascicolo = null;

		for (KeyValuePair kvp : documentProfile) {
			if (kvp.getKey().equalsIgnoreCase("COD_ENTE")) {
				codEnte = kvp.getValue();
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("COD_AOO")) {
				codAOO = kvp.getValue();
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("CLASSIFICA")) {
				classifica = kvp.getValue();
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
				annoFascicolo = kvp.getValue();
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
				progrFascicolo = kvp.getValue();
				continue;
			}
		}

		if (StringUtils.isEmpty(codEnte)) {
			return;
		}
		if (StringUtils.isEmpty(codAOO)) {
			return;
		}

		if (StringUtils.isEmpty(progrFascicolo)) {
			return;
		}

		Map<String, String> idFascicolo = new HashMap<String, String>();

		idFascicolo.put("COD_ENTE", codEnte);
		idFascicolo.put("COD_AOO", codAOO);
		idFascicolo.put("CLASSIFICA", classifica);
		idFascicolo.put("ANNO_FASCICOLO", annoFascicolo);
		idFascicolo.put("PROGR_FASCICOLO", progrFascicolo);

		checkFascicolo(idFascicolo, null, null);

	}

	private void checkFascicoliSecondari(KeyValuePair[] documentProfile)
			throws Exception {

		String codEnte = null;
		String codAOO = null;
		String fascicoliSecondari = null;

		for (KeyValuePair kvp : documentProfile) {
			if (kvp.getKey().equalsIgnoreCase("COD_ENTE")) {
				codEnte = kvp.getValue();
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("COD_AOO")) {
				codAOO = kvp.getValue();
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("FASC_SECONDARI")) {
				fascicoliSecondari = kvp.getValue();
				continue;
			}
		}

		if (StringUtils.isEmpty(codEnte)) {
			return;
		}
		if (StringUtils.isEmpty(codAOO)) {
			return;
		}

		if (StringUtils.isEmpty(fascicoliSecondari)) {
			return;
		}

		String[] fascicoli = null;

		fascicoli = fascicoliSecondari.split(" *; *");

		if (fascicoli.length > 0) {
			writeDebug("gestione Fascicoli Secondari...");
		}

		Map<String, String> idFascicolo = null;

		for (String fascicolo : fascicoli) {

			fascicolo = fascicolo.trim();

			if (fascicolo.equals(""))
				continue;

			if (idFascicolo == null) {
				idFascicolo = new HashMap<String, String>();
			} else {
				idFascicolo.clear();
			}

			Matcher m = patternFascicoliSec.matcher(fascicolo);

			if (m.matches()) {

				idFascicolo.put("COD_ENTE", codEnte);
				idFascicolo.put("COD_AOO", codAOO);
				idFascicolo.put("CLASSIFICA", m.group(classificaPosition));
				idFascicolo.put("ANNO_FASCICOLO",
						m.group(annoFascicoloPosition));
				idFascicolo.put("PROGR_FASCICOLO",
						m.group(progrFascicoloPosition));

				checkFascicolo(idFascicolo, null, null);
			}

		}

	}

	private void checkAnagraficheCustom(KeyValuePair[] documentProfile)
			throws Exception {

		String codEnte = null;
		String codAOO = null;
		String typeId = null;

		for (KeyValuePair kvp : documentProfile) {
			if (kvp.getKey().equalsIgnoreCase("COD_ENTE")) {
				codEnte = kvp.getValue();
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("COD_AOO")) {
				codAOO = kvp.getValue();
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("TYPE_ID")) {
				typeId = kvp.getValue();
				continue;
			}
		}

		if (StringUtils.isEmpty(codEnte)) {
			return;
		}
		if (StringUtils.isEmpty(codAOO)) {
			return;
		}
		if (StringUtils.isEmpty(typeId)) {
			return;
		}

		DocumentType docType = DOCUMENT_TYPES.get(typeId);

		if (docType == null) {
			throw new Exception(
					"checkAnagraficheCustom: type_id documento sconosciuto: "
							+ typeId);
		}

		if (!docType.isDefinedForAOO(codEnte, codAOO))
			throw new Exception("checkAnagraficheCustom: Document Type "
					+ typeId + " non e' definito per Ente " + codEnte
					+ " e AOO " + codAOO);

		// FieldDescriptor fd = null;
		// // controllo se i campi specifiati appartengono al documenttype
		// for (String str : documentProfile.keySet()) {
		//
		// fd = docType.getFieldDescriptor(codEnte, codAOO, str);
		//
		// if (fd == null)
		// throw new Exception("il campo " + str + " non appartiene al tipo " +
		// typeId);
		// }

		String[] mv = null;

		String pointedPropName = "";
		String anagraficaTypeId = "";

		FieldDescriptor fd = null;

		// controllo le anagrafiche custom anche se Multivalue
		for (KeyValuePair kvp : documentProfile) {

			String propName = kvp.getKey();
			String propValue = kvp.getValue();

			if (StringUtils.isEmpty(propValue)) {
				continue; // non devo controllare nulla
			}

			// i fascicoli secondari sono gestiti a parte
			if (propName.equalsIgnoreCase("FASC_SECONDARI")) {
				continue;
			}

			fd = FIELDS.get(propName);

			if (fd == null) {
				// throw new Exception("il campo " + propName +
				// " non e' definito tra i FIELDS");
				continue;
			}

			// se il campo non punta ad un'anagrafica continue...
			if (!fd.pointToAnagrafica()) {
				continue;
			}

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

			if (fd.isMultivalue()) {
				// se e' anagrafica multivalue ha il codice multivalue
				mv = propValue.split(" *; *");

				for (String singleValue : mv) {
					String codiceAnagrafica = singleValue.trim();
					if (codiceAnagrafica.equals(""))
						continue;
					checkAnagraficaCustom(at, codEnte, codAOO, codiceAnagrafica);
				}
			} else {
				checkAnagraficaCustom(at, codEnte, codAOO, propValue);
			}

		}

	}

	private void checkAnagraficaCustom(AnagraficaType anagraficaType,
			String codEnte, String codAoo, String cod) throws Exception {

		String strId = generateId(anagraficaType.getTypeId(), new String[] {
				codEnte, codAoo, cod });

		writeDebug("verifica anagrafica custom: " + strId + "...");

		if (anagraficheCache.contains(strId)) {
			writeDebug("anagrafica gia' elaborata");
			return;
		}

		KeyValuePair kvpTypeid = new KeyValuePair();
		kvpTypeid.setKey("TYPE_ID");
		kvpTypeid.setValue(anagraficaType.getTypeId());

		KeyValuePair kvpCodEnte = new KeyValuePair();
		kvpCodEnte.setKey("COD_ENTE");
		kvpCodEnte.setValue(codEnte);

		KeyValuePair kvpCodAoo = new KeyValuePair();
		kvpCodAoo.setKey("COD_AOO");
		kvpCodAoo.setValue(codAoo);

		KeyValuePair kvpCodice = new KeyValuePair();
		kvpCodice.setKey(anagraficaType.getCodicePropName().toUpperCase());
		kvpCodice.setValue(cod);

		// recupero profilo anagrafica custom da Archivio corrente
		GetAnagraficaCustom getAnagraficaCustom = new GetAnagraficaCustom();
		getAnagraficaCustom.setToken(ticket_ws_ac);
		getAnagraficaCustom.setCustomId(new KeyValuePair[] { kvpTypeid,
				kvpCodEnte, kvpCodAoo, kvpCodice });

		GetAnagraficaCustomResponse getAnagraficaCustomResponse = null;
		try {
			getAnagraficaCustomResponse = acDocerWSClient
					.getAnagraficaCustom(getAnagraficaCustom);
		} catch (Exception e) {
			throw new Exception("acDocerWSClient.getAnagraficaCustom: " + strId
					+ ": " + e.getMessage());
		}

		KeyValuePair[] anagraficaCustomProfileAC = getAnagraficaCustomResponse
				.get_return();

		// ricerco sulla Fonte
		SearchAnagrafiche searchAnagrafiche = new SearchAnagrafiche();
		searchAnagrafiche.setToken(ticket_ws_fonte);
		searchAnagrafiche.setType(anagraficaType.getTypeId());
		searchAnagrafiche.setSearchCriteria(new KeyValuePair[] { kvpCodEnte,
				kvpCodAoo, kvpCodice });

		SearchAnagraficheResponse searchAnagraficheResponse;
		try {
			searchAnagraficheResponse = fonteDocerWSClient
					.searchAnagrafiche(searchAnagrafiche);
		} catch (Exception e) {
			throw new Exception("fonteDocerWSClient.searchAnagrafiche: "
					+ strId + ": " + e.getMessage());
		}

		// se l'anagrafica non esiste nella Fonte
		if (searchAnagraficheResponse == null
				|| searchAnagraficheResponse.get_return() == null
				|| searchAnagraficheResponse.get_return().length == 0) {

			CreateAnagraficaCustom createAnagraficaCustom = new CreateAnagraficaCustom();
			createAnagraficaCustom.setToken(ticket_ws_fonte);
			createAnagraficaCustom.setCustomInfo(getAnagraficaCustomResponse
					.get_return());
			try {
				fonteDocerWSClient
						.createAnagraficaCustom(createAnagraficaCustom);
				writeDebug("creata Anagrafica Custom nel WS Fonte: " + strId);
				anagraficheCache.add(strId);
			} catch (Exception e) {
				throw new Exception(
						"fonteDocerWSClient.createAnagraficaCustom: " + strId
								+ ": " + e.getMessage());
			}

		} else if (getAnagraficaCustomResponse.get_return().length != 1) {
			anagraficheCache.add(strId);
			throw new Exception("trovate "
					+ getAnagraficaCustomResponse.get_return().length
					+ " Anagrafiche Custom " + strId);
		} else {

			String acValue = extractValueByName(anagraficaType.getTypeId(),
					anagraficaCustomProfileAC,
					anagraficaType.getDescrizionePropName(), true,
					"acDocerWSClient.getAnagraficaCustom()");
			String fonteValue = extractValueByName(anagraficaType.getTypeId(),
					searchAnagraficheResponse.get_return()[0].getMetadata(),
					anagraficaType.getDescrizionePropName().toUpperCase(),
					true, "fonteDocerWSClient.searchAnagrafiche()");

			if (!acValue.equals(fonteValue)) {

				KeyValuePair[] anagraficaCustomId = new KeyValuePair[] {
						kvpTypeid, kvpCodEnte, kvpCodAoo, kvpCodice };

				UpdateAnagraficaCustom updateAnagraficaCustom = new UpdateAnagraficaCustom();
				updateAnagraficaCustom.setToken(ticket_ws_fonte);
				updateAnagraficaCustom.setCustomId(anagraficaCustomId);
				updateAnagraficaCustom.setCustomInfo(anagraficaCustomProfileAC);

				try {
					fonteDocerWSClient
							.updateAnagraficaCustom(updateAnagraficaCustom);
					writeDebug("Descrizione aggiornata per Anagrafica Custom nel WS Fonte: "
							+ strId);
					anagraficheCache.add(strId);
				} catch (Exception e) {
					throw new Exception(
							"fonteDocerWSClient.updateAnagraficaCustom: "
									+ strId + ": " + e.getMessage());
				}

			}

		}
	}

	// private void checkFascicoloSecondario(Map<String, String> id) throws
	// Exception {
	//
	// String strId = generateId("FASCICOLO", new String[] {id.get("COD_ENTE"),
	// id.get("COD_AOO"), id.get("CLASSIFICA"), id.get("ANNO_FASCICOLO"),
	// id.get("PROGR_FASCICOLO")});
	//
	// writeDebug("verifica anagrafica FASCICOLO: " + strId);
	//
	// if (anagraficheCache.contains(strId)) {
	// writeDebug("anagrafica gia' elaborata");
	// return;
	// }
	//
	// if (fascicoloTaxonomy.contains(strId)) {
	// writeDebug("fascicolo gia' esplorato");
	// return;
	// }
	//
	// fascicoloTaxonomy.add(strId);
	//
	// GetFascicolo getFascicolo = new GetFascicolo();
	// getFascicolo.setToken(ticket_ws_ac);
	// getFascicolo.setFascicoloId(toKeyValuePairArray1(id));
	//
	// // aoo
	// GetFascicoloResponse getFascicoloResponse;
	// try {
	// getFascicoloResponse = acDocerWSClient.getFascicolo(getFascicolo);
	// }
	// catch (RemoteException e) {
	// throw new
	// Exception("checkFascicoloSecondario: acDocerWSClient.getFascicolo: " +
	// id.toString() + ": " + e.getMessage());
	// }
	// catch (DocerExceptionException e) {
	// throw new
	// Exception("checkFascicoloSecondario: acDocerWSClient.getFascicolo: " +
	// id.toString() + ": " + e.getMessage());
	// }
	// if (getFascicoloResponse == null || getFascicoloResponse.get_return() ==
	// null || getFascicoloResponse.get_return().length == 0) {
	// throw new Exception("checkFascicoloSecondario: AC: Fascicolo " +
	// id.toString() + " non trovato nel repositoy Archivio Corrente");
	// }
	//
	// KeyValuePair[] fascicoloProfile = getFascicoloResponse.get_return();
	//
	// List<KeyValuePair> fascicoloProfileCleaned = new
	// ArrayList<KeyValuePair>();
	// for (KeyValuePair kvp : fascicoloProfile) {
	//
	// if (fascicoli_skip_fields.contains(kvp.getKey())) {
	// continue;
	// }
	//
	// fascicoloProfileCleaned.add(kvp);
	// }
	//
	// SearchAnagrafiche searchAnagrafiche = new SearchAnagrafiche();
	// searchAnagrafiche.setToken(ticket_ws_fonte);
	// searchAnagrafiche.setType("FASCICOLO");
	// searchAnagrafiche.setSearchCriteria(toKeyValuePairArray1(id));
	//
	// SearchAnagraficheResponse response;
	//
	// try {
	// response = fonteDocerWSClient.searchAnagrafiche(searchAnagrafiche);
	// }
	// catch (RemoteException e) {
	// throw new
	// Exception("checkFascicoloSecondario: docerWSClient.searchAnagrafiche " +
	// id.toString() + ": " + e.getMessage());
	// }
	// catch (DocerExceptionException e) {
	// throw new
	// Exception("checkFascicoloSecondario: docerWSClient.searchAnagrafiche " +
	// id.toString() + ": " + e.getMessage());
	// }
	//
	// if (response == null || response.get_return() == null ||
	// response.get_return().length == 0) {
	// // il fascicolo non esiste nel WS Fonte
	// // prima di creare il Fascicolo controllo il Padre
	// String parentProgressivo = extractValueByName("fascicolo",
	// fascicoloProfile, "PARENT_PROGR_FASCICOLO", true,
	// "fonteDocerWSClient.searchAnagrafiche()");
	// String progressivo = extractValueByName("fascicolo", fascicoloProfile,
	// "PROGR_FASCICOLO", true, "fonteDocerWSClient.searchAnagrafiche()");
	//
	// if (parentProgressivo != null && !parentProgressivo.equals("")) {
	// Map<String, String> idPadre = new HashMap<String, String>();
	// for (String key : id.keySet()) {
	// idPadre.put(key, id.get(key));
	// }
	//
	// idPadre.put("PROGR_FASCICOLO", parentProgressivo);
	//
	// // controllo il padre
	// if (!parentProgressivo.equals(progressivo)) {
	// checkFascicoloSecondario(idPadre);
	// }
	//
	// }
	//
	// // lo creo
	// CreateFascicolo createFascicolo = new CreateFascicolo();
	// createFascicolo.setToken(ticket_ws_fonte);
	// createFascicolo.setFascicoloInfo(fascicoloProfileCleaned.toArray(new
	// KeyValuePair[0]));
	// try {
	// fonteDocerWSClient.createFascicolo(createFascicolo);
	// writeDebug("checkFascicoloSecondario: checkFascicolo Fascicolo creato nel WS Fonte: "
	// + id.toString());
	// anagraficheCache.add(strId);
	// }
	// catch (RemoteException e) {
	// throw new
	// Exception("checkFascicoloSecondario: fonteDocerWSClient.createFascicolo "
	// + id.toString() + ": " + e.getMessage());
	// }
	// catch (DocerExceptionException e) {
	// throw new
	// Exception("checkFascicoloSecondario: fonteDocerWSClient.createFascicolo "
	// + id.toString() + ": " + e.getMessage());
	// }
	// }
	//
	// }

	private void setNeedsFonteUpdateFascicolo(Map<String, String> id,
			boolean needsFonteUpdate) throws Exception {

		UpdateFascicolo updateFascicolo = new UpdateFascicolo();
		updateFascicolo.setToken(ticket_ws_ac);
		updateFascicolo.setFascicoloId(toKeyValuePairArray1(id));

		List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();

		KeyValuePair kvpFlag = new KeyValuePair();
		kvpFlag.setKey(NEEDS_FONTE_UPDATE);
		kvpFlag.setValue(String.valueOf(needsFonteUpdate));
		kvpList.add(kvpFlag);

		updateFascicolo.setFascicoloInfo(kvpList.toArray(new KeyValuePair[0]));

		// aggiorno il flag sul profilo del documento
		try {
			acDocerWSClient.updateFascicolo(updateFascicolo);
			writeDebug("aggiornato NEEDS_FONTE_UPDATE su WS Archivio Corrente per Fascicolo: "
					+ id.toString());
		} catch (RemoteException e) {
			throw new Exception(
					"acDocerWSClient.updateFascicolo: aggiornamento NEEDS_FONTE_UPDATE "
							+ id.toString() + ": " + e.getMessage());
		} catch (DocerExceptionException e) {
			throw new Exception(
					"acDocerWSClient.updateFascicolo: aggiornamento NEEDS_FONTE_UPDATE "
							+ id.toString() + ": " + e.getMessage());
		}
	}

	private void setNeedsRaccoglitoreUpdateFascicolo(Map<String, String> id,
			boolean needsRaccoglitoreUpdate) throws Exception {

		UpdateFascicolo updateFascicolo = new UpdateFascicolo();
		updateFascicolo.setToken(ticket_ws_fonte);
		updateFascicolo.setFascicoloId(toKeyValuePairArray1(id));

		List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();

		KeyValuePair kvpFlag = new KeyValuePair();
		kvpFlag.setKey(NEEDS_RACCOGLITORE_UPDATE);
		kvpFlag.setValue(String.valueOf(needsRaccoglitoreUpdate));
		kvpList.add(kvpFlag);

		updateFascicolo.setFascicoloInfo(kvpList.toArray(new KeyValuePair[0]));

		// aggiorno il flag sul profilo del documento
		try {
			fonteDocerWSClient.updateFascicolo(updateFascicolo);
			writeDebug("aggiornato NEEDS_RACCOGLITORE_UPDATE su WS Fonte per Fascicolo: "
					+ id.toString());
		} catch (RemoteException e) {
			throw new Exception(
					"fonteDocerWSClient.updateFascicolo: aggiornamento NEEDS_RACCOGLITORE_UPDATE "
							+ id.toString() + ": " + e.getMessage());
		} catch (DocerExceptionException e) {
			throw new Exception(
					"fonteDocerWSClient.updateFascicolo: aggiornamento NEEDS_RACCOGLITORE_UPDATE "
							+ id.toString() + ": " + e.getMessage());
		}
	}

	private void setNeedsFonteUpdateDocumento(String docId,
			boolean needsFonteUpdate) throws Exception {

		UpdateProfileDocument updateProfileDocument = new UpdateProfileDocument();
		updateProfileDocument.setToken(ticket_ws_ac);
		updateProfileDocument.setDocId(docId);

		List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();

		KeyValuePair kvpFlag = new KeyValuePair();
		kvpFlag.setKey(NEEDS_FONTE_UPDATE);
		kvpFlag.setValue(String.valueOf(needsFonteUpdate));
		kvpList.add(kvpFlag);

		updateProfileDocument.setMetadata(kvpList.toArray(new KeyValuePair[0]));

		// aggiorno il flag sul profilo del documento
		try {
			acDocerWSClient.updateProfileDocument(updateProfileDocument);
			writeDebug("aggiornato NEEDS_FONTE_UPDATE su WS Archivio Corrente per Documento Principale: "
					+ docId);
		} catch (RemoteException e) {
			throw new Exception(
					"acDocerWSClient.updateProfileDocument: aggiornamento NEEDS_FONTE_UPDATE "
							+ docId + ": " + e.getMessage());
		} catch (DocerExceptionException e) {
			throw new Exception(
					"acDocerWSClient.updateProfileDocument: aggiornamento NEEDS_FONTE_UPDATE "
							+ docId + ": " + e.getMessage());
		}
	}

	private void setNeedsRaccoglitoreUpdateDocumento(String docId,
			boolean needsRaccoglitoreUpdate) throws Exception {

		UpdateProfileDocument updateProfileDocument = new UpdateProfileDocument();
		updateProfileDocument.setToken(ticket_ws_fonte);
		updateProfileDocument.setDocId(docId);

		List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();

		KeyValuePair kvpFlag = new KeyValuePair();
		kvpFlag.setKey(NEEDS_RACCOGLITORE_UPDATE);
		kvpFlag.setValue(String.valueOf(needsRaccoglitoreUpdate));
		kvpList.add(kvpFlag);

		updateProfileDocument.setMetadata(kvpList.toArray(new KeyValuePair[0]));

		// aggiorno il flag sul profilo del documento
		try {
			fonteDocerWSClient.updateProfileDocument(updateProfileDocument);
			writeDebug("aggiornato NEEDS_RACCOGLITORE_UPDATE su WS Fonte Docer per Documento Principale: "
					+ docId);
		} catch (RemoteException e) {
			throw new Exception(
					"fonteDocerWSClient.updateProfileDocument: aggiornamento NEEDS_RACCOGLITORE_UPDATE "
							+ docId + ": " + e.getMessage());
		} catch (DocerExceptionException e) {
			throw new Exception(
					"fonteDocerWSClient.updateProfileDocument: aggiornamento NEEDS_RACCOGLITORE_UPDATE "
							+ docId + ": " + e.getMessage());
		}
	}

	private final static String regex_who_match = ".*\\[\\[[^\\[\\]]+\\]\\].*";

	private String toXMLString(Document document) throws Exception {

		DateTime d = new DateTime();
		String strDate = d.toString("yyyy-MM-dd_HH.mm.ss.SSS");

		String name = strDate + "_elements.xml";

		Transformer xformer = TransformerFactory.newInstance().newTransformer();

		xformer.setOutputProperty(OutputKeys.METHOD, "xml");
		xformer.setOutputProperty(OutputKeys.INDENT, "yes");
		xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		StringWriter writer = new StringWriter();

		ByteArrayOutputStream fw = new ByteArrayOutputStream();
		xformer.transform(new DOMSource(document), new StreamResult(writer));

		fw.flush();
		fw.close();

		return writer.toString();

	}

	private void checkDocumento(String docId, boolean isPrincipale,
			String id_fonte, KeyValuePair[] documentProfileAC) throws Exception {

		// if (docIdLavorati.contains(docId)) {
		// writeInfo("documento " + docId + " gia' elaborato");
		// return;
		// }

		writeDebug("checkDocumento: " + docId + "...");

		if (documentProfileAC == null) {
			// recupero profilo documento su Archivio Corrente
			GetProfileDocument getProfileDocument = new GetProfileDocument();
			getProfileDocument.setToken(ticket_ws_ac);
			getProfileDocument.setDocId(docId);
			GetProfileDocumentResponse getProfileDocumentResponse = null;

			try {
				getProfileDocumentResponse = acDocerWSClient
						.getProfileDocument(getProfileDocument);
				documentProfileAC = getProfileDocumentResponse.get_return();
			} catch (Exception e) {
				throw new Exception("acDocerWSClient.getProfileDocument: "
						+ docId + ": " + e.getMessage());
			}

		}

		checkFascicoliSecondari(documentProfileAC);

		checkAnagraficheCustom(documentProfileAC);

		// ricerco documento sulla Fonte
		SearchDocuments searchDocuments = new SearchDocuments();
		searchDocuments.setToken(ticket_ws_fonte);
		searchDocuments.setMaxRows(primarysearch_max_results);
		searchDocuments.setKeywords(new String[] { "" });
		// searchDocuments.setOrderby(new KeyValuePair[]{});
		KeyValuePair kvp = new KeyValuePair();
		kvp.setKey("DOCNUM");
		kvp.setValue(docId);
		searchDocuments.setSearchCriteria(new KeyValuePair[] { kvp });

		SearchDocumentsResponse response;
		try {
			response = fonteDocerWSClient.searchDocuments(searchDocuments);
		} catch (Exception e) {
			throw new Exception("fonteDocerWSClient.searchDocuments: " + docId
					+ ": " + e.getMessage());
		}

		String messaggio_tipo_documento = isPrincipale ? "principale"
				: "allegato";

		boolean created = false;
		// se non esiste nel WS Fonte lo creo sulla Fonte
		if (response == null || response.get_return() == null
				|| response.get_return().length == 0) {

			DownloadDocumentResponse downloadDocumentResponse;
			try {

				DownloadDocument downloadDocument = new DownloadDocument();
				downloadDocument.setToken(ticket_ws_ac);
				downloadDocument.setDocId(docId);

				// scarico il file fisico dall'Archivio Corrente
				downloadDocumentResponse = acDocerWSClient
						.downloadDocument(downloadDocument);
			} catch (Exception e) {
				throw new Exception("acDocerWSClient.downloadDocument: "
						+ docId + ": " + e.getMessage());
			}

			DataHandler dhFile = downloadDocumentResponse.get_return()
					.getHandler();
			try {

				// lo creo sulla Fonte
				CreateDocument createDocument = buildCreateDocument(
						documentProfileAC, dhFile, id_fonte);
				fonteDocerWSClient.createDocument(createDocument);
				writeInfo("documento " + messaggio_tipo_documento
						+ " creato nel WS Fonte: docId " + docId);
				created = true;
				cntDocumentiCreated++;

				// aggiorno sempre per la fascicolazione (spostamento fisico nel
				// fascicolo)
				UpdateProfileDocument updateProfileDocument = buildUpdateProfileDocument(
						docId, documentProfileAC, id_fonte);

				try {
					fonteDocerWSClient
							.updateProfileDocument(updateProfileDocument);
					// docIdLavorati.add(docId);
					writeInfo("documento " + messaggio_tipo_documento
							+ " aggiornato nel WS Fonte: docId " + docId);
				} catch (Exception e) {
					throw new Exception(
							"fonteDocerWSClient.updateProfileDocument: "
									+ docId + ": " + e.getMessage());
				}

			} catch (Exception e) {
				throw new Exception("fonteDocerWSClient.createDocument: "
						+ docId + ": " + e.getMessage());
			}

		} else {

			// // recupero profilo documento su Archivio Corrente
			// GetProfileDocument getProfileDocumentFonte = new
			// GetProfileDocument();
			// getProfileDocumentFonte.setToken(ticket_ws_fonte);
			// getProfileDocumentFonte.setDocId(docId);
			// GetProfileDocumentResponse getProfileDocumentFonteResponse =
			// null;
			//
			// try {
			// getProfileDocumentFonteResponse =
			// fonteDocerWSClient.getProfileDocument(getProfileDocumentFonte);
			// }
			// catch (Exception e) {
			// throw new Exception("fonteDocerWSClient.getProfileDocument: " +
			// docId + ": " + e.getMessage());
			// }
			//
			// KeyValuePair[] documentProfileFonte =
			// getProfileDocumentFonteResponse.get_return();

			// if (profileIsChanged(documentProfileAC, documentProfileFonte)) {
			// aggiorno solo se e' cambiata qualche properties
			UpdateProfileDocument updateProfileDocument = buildUpdateProfileDocument(
					docId, documentProfileAC, id_fonte);

			try {
				fonteDocerWSClient.updateProfileDocument(updateProfileDocument);
				// docIdLavorati.add(docId);
				writeInfo("documento " + messaggio_tipo_documento
						+ " aggiornato nel WS Fonte: docId " + docId);
				// if (!created) {
				cntDocumentiUpdated++;
				// }

			} catch (Exception e) {
				throw new Exception(
						"fonteDocerWSClient.updateProfileDocument: " + docId
								+ ": " + e.getMessage());
			}
			// }

		}

		// se e' PRINCIPALE gestisco i related
		if (isPrincipale) {

			// recupero i related dall'Archivio Corrente
			GetRelatedDocuments getRelatedDocuments = new GetRelatedDocuments();
			getRelatedDocuments.setToken(ticket_ws_ac);
			getRelatedDocuments.setDocId(docId);

			GetRelatedDocumentsResponse getRelatedDocumentsResponse = null;

			try {
				getRelatedDocumentsResponse = acDocerWSClient
						.getRelatedDocuments(getRelatedDocuments);
			} catch (Exception e) {
				throw new Exception("acDocerWSClient.getRelatedDocuments: "
						+ docId + ": " + e.getMessage());
			}

			if (getRelatedDocumentsResponse != null
					&& getRelatedDocumentsResponse.get_return() != null
					&& getRelatedDocumentsResponse.get_return().length > 0) {

				List<String> acRelated = Arrays
						.asList(getRelatedDocumentsResponse.get_return());

				writeDebug("il documento " + docId + " ha " + acRelated.size()
						+ " related in AC");

				try {
					// recupero la lista dei related sulla Fonte
					getRelatedDocuments.setToken(ticket_ws_fonte);
					getRelatedDocumentsResponse = fonteDocerWSClient
							.getRelatedDocuments(getRelatedDocuments);
				} catch (Exception e) {
					throw new Exception(
							"fonteDocerWSClient.getRelatedDocuments: " + docId
									+ ": " + e.getMessage());
				}

				// lista dei related sulla Fonte
				List<String> fonteRelated;
				if (getRelatedDocumentsResponse != null
						&& getRelatedDocumentsResponse.get_return() != null) {
					fonteRelated = Arrays.asList(getRelatedDocumentsResponse
							.get_return());
				} else {
					fonteRelated = new ArrayList<String>();
				}

				List<String> docToRelate = new ArrayList<String>();

				for (String relatedId : acRelated) {

					// controllo il related
					checkDocumento(relatedId, false, id_fonte, null);

					if (fonteRelated.contains(relatedId)) {
						continue; // e' gia' related sulla fonte
					}

					// lo aggiungo alla lista dei related da aggiungere
					docToRelate.add(relatedId);
				}

				if (docToRelate.size() > 0) {
					AddRelated addRelated = new AddRelated();
					addRelated.setToken(ticket_ws_fonte);
					addRelated.setDocId(docId);
					addRelated.setRelated(docToRelate.toArray(new String[0]));

					try {
						// aggiungo i nuovi/mancanti related
						fonteDocerWSClient.addRelated(addRelated);
						writeDebug("aggiunti related " + docToRelate
								+ " al documento " + docId
								+ " nella Fonte DocER");
					} catch (Exception e) {
						throw new Exception("fonteDocerWSClient.addRelated: "
								+ docId + ": " + e.getMessage());
					}
				}

			} else {
				writeDebug("il documento " + docId + " non ha related in AC");
			}
		}

	}

	private void checkDocumentoSpurio(String docId) throws Exception {

		KeyValuePair[] documentProfileAC = null;

		writeDebug("checkDocumentoSpurio: " + docId + "...");

		// recupero profilo documento su Archivio Corrente
		GetProfileDocument getProfileDocument = new GetProfileDocument();
		getProfileDocument.setToken(ticket_ws_ac);
		getProfileDocument.setDocId(docId);
		GetProfileDocumentResponse getProfileDocumentResponse = null;

		try {
			getProfileDocumentResponse = acDocerWSClient
					.getProfileDocument(getProfileDocument);
			documentProfileAC = getProfileDocumentResponse.get_return();
		} catch (Exception e) {
			throw new Exception("acDocerWSClient.getProfileDocument: " + docId
					+ ": " + e.getMessage());
		}

		// azzero la tassonomia per evitare controlli ciclici (nel caso in cui
		// uno inserisca il parent uguale proprio progressivo)
		fascicoloTaxonomy.clear();

		checkFascicoloDocumentoSpurio(documentProfileAC);

		checkFascicoliSecondari(documentProfileAC);

		checkAnagraficheCustom(documentProfileAC);

		UpdateProfileDocument updateProfileDocument = buildUpdateProfileDocument(
				docId, documentProfileAC, null);

		try {
			fonteDocerWSClient.updateProfileDocument(updateProfileDocument);
			writeInfo("documento spurio aggiornato nel WS Fonte: docId "
					+ docId);

		} catch (Exception e) {
			throw new Exception("fonteDocerWSClient.updateProfileDocument: "
					+ docId + ": " + e.getMessage());
		}

	}

	// private KeyValuePair[] getCreationProfile(KeyValuePair[]
	// documentProfileAC) {
	//
	// List<KeyValuePair> creationProfile = new ArrayList<KeyValuePair>();
	//
	// for (KeyValuePair kvpAC : documentProfileAC) {
	//
	// String keyAC = kvpAC.getKey();
	// String valueAC = kvpAC.getValue();
	//
	// if (keyAC.equalsIgnoreCase(NEEDS_FONTE_UPDATE)) {
	// continue;
	// }
	// // non dovrebbe essere presente...lo metto per sicurezza
	// if (keyAC.equalsIgnoreCase(NEEDS_RACCOGLITORE_UPDATE)) {
	// continue;
	// }
	// // non dovrebbe essere presente...lo metto per sicurezza
	// if (keyAC.equalsIgnoreCase(RACC_UID)) {
	// continue;
	// }
	// // non dovrebbe essere presente...lo metto per sicurezza
	// if (keyAC.equalsIgnoreCase(ID_FONTE)) {
	// continue;
	// }
	//
	// if (keyAC.equalsIgnoreCase("PROGR_FASCICOLO")) {
	// continue;
	// }
	// if (keyAC.equalsIgnoreCase("ANNO_FASCICOLO")) {
	// continue;
	// }
	// if (keyAC.equalsIgnoreCase("FASC_SECONDARI")) {
	// continue;
	// }
	//
	// creationProfile.add(kvpAC);
	//
	// }
	//
	// return creationProfile.toArray(new KeyValuePair[0]);
	//
	// }

	private boolean profileIsChanged(KeyValuePair[] documentProfileAC,
			KeyValuePair[] documentProfileFonte) {

		for (KeyValuePair kvpAC : documentProfileAC) {

			String keyAC = kvpAC.getKey();
			String valueAC = kvpAC.getValue();

			if (keyAC.equalsIgnoreCase(NEEDS_FONTE_UPDATE)) {
				continue;
			}
			// non dovrebbe essere presente...lo metto per sicurezza
			if (keyAC.equalsIgnoreCase(NEEDS_RACCOGLITORE_UPDATE)) {
				continue;
			}
			// non dovrebbe essere presente...lo metto per sicurezza
			if (keyAC.equalsIgnoreCase(RACC_UID)) {
				continue;
			}
			// non dovrebbe essere presente...lo metto per sicurezza
			if (keyAC.equalsIgnoreCase(ID_FONTE)) {
				continue;
			}

			for (KeyValuePair kvpFonte : documentProfileFonte) {

				String keyFonte = kvpFonte.getKey();
				String valueFonte = kvpFonte.getValue();

				if (keyFonte.equalsIgnoreCase(keyAC)) {
					if (!valueFonte.equals(valueAC)) {
						return true;
					}
				}
			}
		}

		return false;

	}

	// private void sfascicolaDocumento(String docId) throws Exception {
	//
	// UpdateProfileDocument updateProfileDocument = new
	// UpdateProfileDocument();
	// updateProfileDocument.setToken(ticket_ws_fonte);
	// updateProfileDocument.setDocId(docId);
	//
	// KeyValuePair kvpProgrFascicolo = new KeyValuePair();
	// kvpProgrFascicolo.setKey("PROGR_FASCICOLO");
	// kvpProgrFascicolo.setValue("");
	//
	// KeyValuePair kvpAnnoFascicolo = new KeyValuePair();
	// kvpAnnoFascicolo.setKey("ANNO_FASCICOLO");
	// kvpAnnoFascicolo.setValue("");
	//
	// updateProfileDocument.setMetadata(new KeyValuePair[] { kvpAnnoFascicolo,
	// kvpProgrFascicolo });
	//
	// // aggiorno il flag sul profilo del documento
	// try {
	// fonteDocerWSClient.updateProfileDocument(updateProfileDocument);
	// }
	// catch (RemoteException e) {
	// throw new
	// Exception("fonteDocerWSClient.updateProfileDocument: sfascicolazione documento "
	// + docId + ": " + e.getMessage());
	// }
	// catch (DocerExceptionException e) {
	// throw new
	// Exception("fonteDocerWSClient.updateProfileDocument: sfascicolazione documento "
	// + docId + ": " + e.getMessage());
	// }
	//
	// }

	private CreateDocument buildCreateDocument(
			KeyValuePair[] documentProfileAC, DataHandler handler,
			String id_fonte) {

		CreateDocument createDocument = new CreateDocument();

		for (KeyValuePair kvp : documentProfileAC) {

			String keyAC = kvp.getKey();
			String valueAC = kvp.getValue();

			if (documenti_skip_fields.contains(kvp.getKey().toUpperCase())) {
				continue;
			}

			if (keyAC.equalsIgnoreCase(NEEDS_FONTE_UPDATE)) {
				continue;
			}
			// non dovrebbe essere presente...lo metto per sicurezza
			if (keyAC.equalsIgnoreCase(NEEDS_RACCOGLITORE_UPDATE)) {
				continue;
			}
			// non dovrebbe essere presente...lo metto per sicurezza
			if (keyAC.equalsIgnoreCase(RACC_UID)) {
				continue;
			}
			// non dovrebbe essere presente...lo metto per sicurezza
			if (keyAC.equalsIgnoreCase(ID_FONTE)) {
				continue;
			}

			if (keyAC.equalsIgnoreCase("PROGR_FASCICOLO")) {
				continue;
			}
			if (keyAC.equalsIgnoreCase("ANNO_FASCICOLO")) {
				continue;
			}
			if (keyAC.equalsIgnoreCase("FASC_SECONDARI")) {
				continue;
			}

			createDocument.addMetadata(kvp);

		}

		if (StringUtils.isNotEmpty(id_fonte)) {
			KeyValuePair kvp = new KeyValuePair();
			kvp.setKey(ID_FONTE);
			kvp.setValue(id_fonte);
			createDocument.addMetadata(kvp);
		}

		createDocument.setFile(handler);
		createDocument.setToken(ticket_ws_fonte);
		return createDocument;

	}

	private UpdateProfileDocument buildUpdateProfileDocument(String docId,
			KeyValuePair[] profileDocument, String id_fonte) {

		UpdateProfileDocument updateProfileDocument = new UpdateProfileDocument();

		for (KeyValuePair kvp : profileDocument) {

			if (documenti_skip_fields.contains(kvp.getKey().toUpperCase())) {
				continue;
			}
			if (kvp.getKey().equals("DOCNUM")) {
				continue;
			}

			if (kvp.getKey().equalsIgnoreCase(ID_FONTE)) {
				continue;
			} else if (kvp.getKey().equalsIgnoreCase(NEEDS_FONTE_UPDATE)) {
				continue;
			} else if (kvp.getKey().equalsIgnoreCase(NEEDS_RACCOGLITORE_UPDATE)) {
				continue;
			} else if (kvp.getKey().equalsIgnoreCase(RACC_UID)) {
				continue;
			}

			updateProfileDocument.addMetadata(kvp);
		}

		if (StringUtils.isNotEmpty(id_fonte)) {
			KeyValuePair kvp = new KeyValuePair();
			kvp.setKey(ID_FONTE);
			kvp.setValue(id_fonte);
			updateProfileDocument.addMetadata(kvp);
		}

		updateProfileDocument.setDocId(docId);
		updateProfileDocument.setToken(ticket_ws_fonte);
		return updateProfileDocument;
	}

	// private boolean initBusinessLogicArchivioCorrente() {
	//
	// try {
	// BusinessLogic.setConfigPath(businesslogic_configuration_file);
	// blAC = new BusinessLogic(provider, primarysearch_max_results);
	// // verifico che non ci siano stati errori nell'inizializzazione
	// // della Business Logic
	// blAC.getDocumentTypes("");
	// return true;
	// }
	// catch (DocerException e) {
	// writeError("500 - errore istanza Business Logic Archivio Corrente: " +
	// e.getMessage());
	// return false;
	// }
	// }

	private boolean initWSClientsAC() {

		try {
			acAuthWSClient = getAuthenticationClient(ws_ac_auth_url);
		} catch (AxisFault e) {
			writeError("errore inizializzazione acAuthWSClient: "
					+ e.getMessage());
			return false;
		}

		try {
			acDocerWSClient = getDocerServicesClient(ws_ac_url);
		} catch (AxisFault e) {
			writeError("errore inizializzazione acDocerWSClient: "
					+ e.getMessage());
			return false;
		}

		writeDebug("inizializzati WSClients AC");
		return true;
	}

	private boolean initWSClientsFonte() {

		try {
			fonteAuthWSClient = getAuthenticationClient(ws_fonte_auth_url);
		} catch (AxisFault e) {
			writeError("errore inizializzazione fonteAuthWSClient: "
					+ e.getMessage());
			return false;
		}

		try {
			fonteDocerWSClient = getDocerServicesClient(ws_fonte_url);
		} catch (AxisFault e) {
			writeError("errore inizializzazione fonteDocerWSClient: "
					+ e.getMessage());
			return false;
		}

		writeDebug("inizializzati WSClients Fonte");
		return true;
	}

	private String loginWSFonte() throws Exception {

		if (!ws_fonte_auth_url.equals("")) {

			Login login = new Login();
			login.setUsername(ws_fonte_login_userid);
			login.setPassword(ws_fonte_login_pwd);
			login.setCodiceEnte(ws_fonte_login_cod_ente);
			login.setApplication("");

			writeDebug("esecuzione login WS Fonte auth su "
					+ fonteAuthWSClient._getServiceClient().getOptions()
							.getTo());
			LoginResponse resp = fonteAuthWSClient.login(login);
			return resp.get_return();
		} else {
			DocerServicesStub.Login login = new DocerServicesStub.Login();

			login.setUserId(ws_fonte_login_userid);
			login.setPassword(ws_fonte_login_pwd);
			login.setCodiceEnte(ws_fonte_login_cod_ente);

			writeDebug("esecuzione login WS Fonte su "
					+ fonteDocerWSClient._getServiceClient().getOptions()
							.getTo());
			DocerServicesStub.LoginResponse loginRes = fonteDocerWSClient
					.login(login);
			return "calltype:internal|" + loginRes.get_return();
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
			} catch (Exception e) {
			}
		} else {
			DocerServicesStub.Logout logout = new DocerServicesStub.Logout();

			logout.setToken(ticket_ws_fonte);

			try {
				fonteDocerWSClient.logout(logout);
			} catch (Exception e) {

			}
		}

	}

	private String loginWSAC() throws Exception {

		if (!ws_ac_auth_url.equals("")) {

			Login login = new Login();
			login.setUsername(ws_ac_login_userid);
			login.setPassword(ws_ac_login_pwd);
			login.setCodiceEnte(ws_ac_login_cod_ente);
			login.setApplication("");

			LoginResponse resp;
			writeDebug("esecuzione login WS AC auth su "
					+ acAuthWSClient._getServiceClient().getOptions().getTo());
			resp = acAuthWSClient.login(login);
			return resp.get_return();

		} else {
			DocerServicesStub.Login login = new DocerServicesStub.Login();

			login.setUserId(ws_ac_login_userid);
			login.setPassword(ws_ac_login_pwd);
			login.setCodiceEnte(ws_ac_login_cod_ente);

			DocerServicesStub.LoginResponse loginRes;
			writeDebug("esecuzione login WS AC su "
					+ acDocerWSClient._getServiceClient().getOptions().getTo());
			loginRes = acDocerWSClient.login(login);
			return "calltype:internal|" + loginRes.get_return();

		}

	}

	private void logoutWSAC() {

		if (ticket_ws_ac == null) {
			return;
		}

		if (!ws_ac_auth_url.equals("")) {

			Logout logout = new Logout();
			logout.setToken(ticket_ws_ac);

			try {
				acAuthWSClient.logout(logout);
			} catch (Exception e) {
			}
		} else {
			DocerServicesStub.Logout logout = new DocerServicesStub.Logout();

			logout.setToken(ticket_ws_ac);

			try {
				acDocerWSClient.logout(logout);
			} catch (Exception e) {

			}
		}

	}

	private AuthenticationServiceStub getAuthenticationClient(
			String authenticationEpr) throws AxisFault {

		AuthenticationServiceStub authenticationServicesClient = new AuthenticationServiceStub(
				authenticationEpr);

		Options options = authenticationServicesClient._getServiceClient()
				.getOptions();
		options.setTo(new EndpointReference(authenticationEpr));

		options.setProperty(Constants.Configuration.ENABLE_MTOM,
				Constants.VALUE_TRUE);
		options.setTimeOutInMilliSeconds(timeout);
		options.setProperty(Constants.Configuration.CACHE_ATTACHMENTS,
				cacheAttachments ? Constants.VALUE_TRUE : Constants.VALUE_FALSE);
		options.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR,
				cacheDir.getAbsolutePath());
		options.setProperty(Constants.Configuration.FILE_SIZE_THRESHOLD,
				fileSizeThreshold);

		return authenticationServicesClient;
	}

	private DocerServicesStub getDocerServicesClient(String epr)
			throws AxisFault {

		DocerServicesStub docerServicesClient = new DocerServicesStub(epr);

		Options options = docerServicesClient._getServiceClient().getOptions();

		options.setTo(new EndpointReference(epr));

		options.setProperty(Constants.Configuration.ENABLE_MTOM,
				Constants.VALUE_TRUE);
		options.setTimeOutInMilliSeconds(timeout);
		options.setProperty(Constants.Configuration.CACHE_ATTACHMENTS,
				cacheAttachments ? Constants.VALUE_TRUE : Constants.VALUE_FALSE);
		options.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR,
				cacheDir.getAbsolutePath());
		options.setProperty(Constants.Configuration.FILE_SIZE_THRESHOLD,
				fileSizeThreshold);

		return docerServicesClient;
	}

	private static String template2 = "%s{%s}";
	private static String template3 = "%s{%s,%s}";
	private static String template4 = "%s{%s,%s,%s}";
	private static String template6 = "%s{%s,%s,%s,%s,%s}";

	private String generateId(String type, String[] args) throws Exception {

		if (args.length == 1) {
			if (StringUtils.isEmpty(args[0])) {
				throw new Exception("ERROR generateId: type= " + type
						+ ", args[0]=" + args[0]);
			}
			return String.format(template2, type, args[0]);
		}

		if (args.length == 2) {
			if (StringUtils.isEmpty(args[0]) || StringUtils.isEmpty(args[1])) {
				throw new Exception("ERROR generateId: type= " + type
						+ ", args[0]=" + args[0] + ", args[1]=" + args[1]);
			}
			return String.format(template3, type, args[0], args[1]);
		}

		if (args.length == 3) {
			if (StringUtils.isEmpty(args[0]) || StringUtils.isEmpty(args[1])
					|| StringUtils.isEmpty(args[2])) {
				throw new Exception("ERROR generateId: type= " + type
						+ ", args[0]=" + args[0] + ", args[1]=" + args[1]
						+ ", args[2]=" + args[2]);
			}
			return String.format(template4, type, args[0], args[1], args[2]);
		}
		if (args.length == 5) {
			if (StringUtils.isEmpty(args[0]) || StringUtils.isEmpty(args[1])
					|| StringUtils.isEmpty(args[2])
					|| StringUtils.isEmpty(args[3])
					|| StringUtils.isEmpty(args[4])) {
				throw new Exception("ERROR generateId: type= " + type
						+ ", args[0]=" + args[0] + ", args[1]=" + args[1]
						+ ", args[2]=" + args[2] + ", args[3]=" + args[3]
						+ ", args[4]=" + args[4]);
			}
			return String.format(template6, type, args[0], args[1], args[2],
					args[3], args[4]);
		}

		throw new Exception("ERROR generateId: type= " + type
				+ ", args.length=" + args.length);
	}

	public DataHandler testDownloadDocumentFromFonte(String docId)
			throws Exception {
		DownloadDocumentResponse downloadDocumentResponse;
		try {

			if (!initWSClientsFonte()) {
				throw new Exception("errore initWSClientFonte");
			}

			String ticket_ws_fonte = loginWSFonte();

			DownloadDocument downloadDocument = new DownloadDocument();
			downloadDocument.setToken(ticket_ws_fonte);
			downloadDocument.setDocId(docId);

			// lo scarico
			downloadDocumentResponse = fonteDocerWSClient
					.downloadDocument(downloadDocument);
			return downloadDocumentResponse.get_return().getHandler();
		} catch (RemoteException e) {
			throw new Exception("testDownloadDocumentFromFonte: " + docId
					+ ": " + e.getMessage());
		} catch (DocerExceptionException e) {
			throw new Exception("testDownloadDocumentFromFonte: " + docId
					+ ": " + e.getMessage());
		} finally {
			logoutWSFonte();
		}
	}

	public String testGetFileNameFromFonte(String docId) throws Exception {

		try {
			if (!initWSClientsFonte()) {
				throw new Exception("errore initWSClientFonte");
			}

			String ticket_ws_fonte = loginWSFonte();

			GetProfileDocument getProfileDocument = new GetProfileDocument();
			getProfileDocument.setToken(ticket_ws_fonte);
			getProfileDocument.setDocId(docId);

			// lo scarico
			GetProfileDocumentResponse getProfileDocumentResponse = fonteDocerWSClient
					.getProfileDocument(getProfileDocument);

			if (getProfileDocumentResponse == null
					|| getProfileDocumentResponse.get_return() == null) {
				throw new Exception("Documento " + docId
						+ " ha fileName sconosciuto");
			}

			return extractValueByName("Documento",
					getProfileDocumentResponse.get_return(), "DOCNAME", true,
					"fonteDocerWSClient.getProfileDocument()");

		} catch (RemoteException e) {
			throw new Exception("testGetFileNameFromFonte: " + docId + ": "
					+ e.getMessage());
		} catch (DocerExceptionException e) {
			throw new Exception("testGetFileNameFromFonte: " + docId + ": "
					+ e.getMessage());
		} finally {
			logoutWSFonte();
		}
	}

	private OrderedList<MigrationFilterCriteria> getMigrationFilterCriteriaByFonteId(
			String id_fonte) {

		OrderedList<MigrationFilterCriteria> listByEnte = new OrderedList<MigrationFilterCriteria>();

		for (int position = 0; position < migrationFilterCriteriaList.size(); position++) {
			MigrationFilterCriteria mfc = migrationFilterCriteriaList
					.get(position);
			if (mfc.getIdFonte().equals(id_fonte)) {
				listByEnte.add(mfc);
			}
		}

		return listByEnte;
	}

	private List<String> getFontiConfigurate() {

		List<String> list = new ArrayList<String>();

		for (int position = 0; position < migrationFilterCriteriaList.size(); position++) {
			MigrationFilterCriteria mfc = migrationFilterCriteriaList
					.get(position);
			if (list.contains(mfc.getIdFonte())) {
				continue;
			}

			list.add(mfc.getIdFonte());
		}

		return list;

	}

	private KeyValuePair[] getFascicoloId(SearchItem si) {
		KeyValuePair[] kvpArr = new KeyValuePair[5];

		for (KeyValuePair kvp : si.getMetadata()) {
			if (kvp.getKey().equalsIgnoreCase("COD_ENTE")) {
				kvpArr[0] = kvp;
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("COD_AOO")) {
				kvpArr[1] = kvp;
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("CLASSIFICA")) {
				kvpArr[2] = kvp;
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
				kvpArr[3] = kvp;
				continue;
			}
			if (kvp.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
				kvpArr[4] = kvp;
				continue;
			}
		}

		return kvpArr;
	}
	
	public static String getLastMessage(){
		return lastMessage;
	}

}
