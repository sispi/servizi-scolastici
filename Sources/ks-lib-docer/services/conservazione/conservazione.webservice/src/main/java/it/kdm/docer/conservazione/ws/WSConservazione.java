package it.kdm.docer.conservazione.ws;

import it.eng.parer.ccd.lib.ParerLib;
import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.WSDemoneStub;
import it.kdm.docer.commons.Config;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.conservazione.ConservazioneException;
import it.kdm.docer.conservazione.ConservazioneResult;
import it.kdm.docer.conservazione.DocerLib;
import it.kdm.docer.conservazione.bl.Conservazione;
import it.kdm.docer.conservazione.bl.IConservazioneBL;
import it.kdm.docer.conservazione.bl.IConservazioneBL.TipoConservazione;
import it.kdm.docer.conservazione.converter.Converter;
import it.kdm.docer.conservazione.provider.parer.EntePasswordManager;
import it.kdm.docer.conservazione.provider.parer.ProviderParer;

import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;

import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.exceptions.DocerException;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

public class WSConservazione {
	
	Logger log = org.slf4j.LoggerFactory.getLogger(WSConservazione.class);
	
	private java.io.File tempDir;
	private IConservazioneBL businessLogic;
	private TicketCipher cipher = new TicketCipher();

	private Config config;
	private DocerLib docerLib;

	public WSConservazione() throws ConservazioneException {
		
		try {
			Properties props = ConfigurationUtils.loadProperties("conservazione.properties");
			
			java.io.File configFile = new java.io.File(props.getProperty("config.path"));
			if(!configFile.exists()) {
				throw new FileNotFoundException(configFile.getAbsolutePath());
			}
			
			config = new Config();
			config.setConfigFile(configFile);
			
			initConfiguration();
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}
	
	private void initConfiguration() throws ConservazioneException {
		try {
			tempDir = new java.io.File(config.getNode(makePropertyXpath("configuration", "tempDir")).getText());
			log.debug("tempDir: " + this.tempDir);
			
			java.io.File templatesDir = new java.io.File(config.getNode(makePropertyXpath("configuration", "templatesDir")).getText());
			
			Converter converter = new Converter(templatesDir);
			
			ParerLib parer = new ParerLib();
			parer.setSacerHost(config.getNode(makePropertyXpath("parer", "sacerHost")).getText());
			parer.setSacerVersion(config.getNode(makePropertyXpath("parer", "sacerVersion")).getText());
			parer.setDeleteAfterSend(Boolean.parseBoolean(config.getNode(makePropertyXpath("parer", "deleteAfterSend")).getText()));
			parer.setUseHttps(Boolean.parseBoolean(config.getNode(makePropertyXpath("parer", "useHttps")).getText()));
			
			java.io.File usersFile = new java.io.File(config.getNode(makePropertyXpath("configuration", "usersFile")).getText());
			
			EntePasswordManager passwordManager = new EntePasswordManager(usersFile);
			
			ProviderParer provider = new ProviderParer();
			provider.setParerLib(parer);
			provider.setEntePasswordManager(passwordManager);
			
			boolean simulation = Boolean.parseBoolean(config.getNode(makePropertyXpath("configuration", "simulation")).getText());
			provider.setSimulate(simulation);
			
			Conservazione businessLogic = new Conservazione();
			businessLogic.setConverter(converter);
			businessLogic.setProvider(provider);
			
			this.businessLogic = businessLogic;

			String docerProvider = config.getNode(makePropertyXpath("docer", "provider")).getText();
			int primarySearchMaxRows = Integer.parseInt(config.getNode(makePropertyXpath("docer", "primarySearchMaxRows")).getText());
			java.io.File diskBufferDirectory = new java.io.File(config.getNode(makePropertyXpath("docer", "diskBufferDirectory")).getText());
			int maxFileLength = Integer.parseInt(config.getNode(makePropertyXpath("docer", "maxFileLength")).getText());

			docerLib = new DocerLib(docerProvider,
					primarySearchMaxRows,
					diskBufferDirectory,
					maxFileLength,
					config.getConfigFile().getAbsolutePath());

		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}

	private String makePropertyXpath(String section, String property) {
		return String.format("//group[@name='conservazione']/section[@name='%s']/%s", section, property);
	}

	public String login(String userId, String password, String codiceEnte) throws ConservazioneException {
        try {
			return cipher.encryptTicket(this.docerLib.login(userId, password, codiceEnte));
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
    }

	public KeyValuePair[] loginSSO(String saml, String codiceEnte) throws ConservazioneException {

        try {
            return this.docerLib.loginSSO(saml, codiceEnte);
        } catch (DocerException e) {
			throw new ConservazioneException(e);
        }

    }

    public boolean logout(String token) throws ConservazioneException {
        return true;
    }
    
    public boolean writeConfig(String token, String xmlConfig) throws ConservazioneException{
        try {
			config.writeConfig(xmlConfig);
		} catch (Exception e) {
			return false;
		}
		        
    	initConfiguration();
    	return true;
    }
    
    public String readConfig(String token) throws ConservazioneException {
        try {
			return config.readConfig();
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
    }

	public boolean updateJob(String token, long id_job, Character stato,
							 boolean forzaCollegamento, boolean forzaAccettazione,
							 boolean forzaConservazione) throws ConservazioneException {

		try {
//			WSDemoneStub client = ClientManager.INSTANCE.getDemoneClient();
//			WSDemoneStub.UpdateJob req = new WSDemoneStub.UpdateJob();
//
//			req.setId_job(id_job);
//			req.setForzaAccettazione(forzaAccettazione);
//			req.setForzaCollegamento(forzaCollegamento);
//			req.setForzaConservazione(forzaConservazione);
//			req.setStato(stato);
//
//			WSDemoneStub.UpdateJobResponse resp = client.updateJob(req);
			String epr = ClientManager.INSTANCE.getDemoneEpr();

			HttpClient clientHttp = new HttpClient();
			PostMethod method = new PostMethod(getEpr(epr, "updateJob"));

			method.addParameter("id_job", String.valueOf(id_job));
			method.addParameter("stato", String.valueOf(stato));
			method.addParameter("forzaCollegamento", String.valueOf(forzaCollegamento));
			method.addParameter("forzaAccettazione", String.valueOf(forzaAccettazione));
			method.addParameter("forzaConservazione", String.valueOf(forzaConservazione));

			clientHttp.executeMethod(method);

		} catch (Exception err) {
			throw new ConservazioneException(err);
		}

		return true;

	}

	public final String getEpr(String serviceUrl, String method) {
		serviceUrl = cleanServiceUrl(serviceUrl);
		String epr;
		if (serviceUrl.endsWith("/")) {
			epr = serviceUrl + method;
		} else {
			epr = serviceUrl + "/" + method;
		}

		return epr;
	}

	public static Pattern serviceUrlPattern = Pattern.compile("(^.*/services/[^/.]+)(\\..+HttpSoap[0-9]+Endpoint)?/.*$");

	public String cleanServiceUrl(String serviceUrl) {
		Matcher m = serviceUrlPattern.matcher(serviceUrl);
		if (m.matches()) {
			return m.group(1);
		} else {
			return serviceUrl;
		}
	}

	public String searchJob(String token, String dataInizio, String dataFine, String esito, String docId, String docType, String errCode) throws ConservazioneException  {
		try {
			WSDemoneStub client = ClientManager.INSTANCE.getDemoneClient();
			WSDemoneStub.SearchJobEstesa req = new WSDemoneStub.SearchJobEstesa();

			WSDemoneStub.KeyValuePair pair = new WSDemoneStub.KeyValuePair();
			pair.setKey("DATA_INIZIO");
			pair.setValue(dataInizio);
			req.addSearchCriteria(pair);

			pair = new WSDemoneStub.KeyValuePair();
			pair.setKey("DATA_FINE");
			pair.setValue(dataFine);
			req.addSearchCriteria(pair);

			pair = new WSDemoneStub.KeyValuePair();
			pair.setKey("ESITO");
			pair.setValue(esito);
			req.addSearchCriteria(pair);

			pair = new WSDemoneStub.KeyValuePair();
			pair.setKey("DOC_NUM");
			pair.setValue(docId);
			req.addSearchCriteria(pair);

			pair = new WSDemoneStub.KeyValuePair();
			pair.setKey("DOC_TYPE");
			pair.setValue(docType);
			req.addSearchCriteria(pair);

			pair = new WSDemoneStub.KeyValuePair();
			pair.setKey("ERR_CODE");
			pair.setValue(errCode);
			req.addSearchCriteria(pair);


			WSDemoneStub.SearchJobEstesaResponse resp = client.searchJobEstesa(req);

			OMElement doc = resp.getOMElement(new QName(null, "jobs"), OMAbstractFactory.getOMFactory());

			return doc.toString();

		} catch (Exception e) {
			throw new ConservazioneException(e);
		}

	}

	public String searchJobEstesa(String token, KeyValuePair[] searchCriteria, Integer maxRows) throws ConservazioneException  {
		try {
			WSDemoneStub client = ClientManager.INSTANCE.getDemoneClient();
			WSDemoneStub.SearchJobEstesa req = new WSDemoneStub.SearchJobEstesa();

			WSDemoneStub.KeyValuePair pair = new WSDemoneStub.KeyValuePair();

			for(KeyValuePair pairInput : searchCriteria) {
				pair = new WSDemoneStub.KeyValuePair();
				pair.setKey(pairInput.getKey());
				pair.setValue(pairInput.getValue());
				req.addSearchCriteria(pair);
			}

			WSDemoneStub.SearchJobEstesaResponse resp = client.searchJobEstesa(req);

			OMElement doc = resp.getOMElement(new QName(null, "jobs"), OMAbstractFactory.getOMFactory());

			return doc.toString();

		} catch (Exception e) {
			throw new ConservazioneException(e);
		}

	}

	public ConservazioneResult versamento(String token, DataHandler xmlDocumento, String tipoDocumento, 
			String applicazioneChiamante, String tipoConservazione,
			boolean forzaAccettazione, boolean forzaConservazione,
			boolean forzaCollegamento, File[] files, String ente, String aoo) throws ConservazioneException {
	
		try {
			StAXOMBuilder builder = new StAXOMBuilder(xmlDocumento.getInputStream());
			OMElement doc = builder.getDocumentElement();
			
			if(!tempDir.exists()) {
				FileUtils.forceMkdir(tempDir);
			}
			
			it.kdm.docer.conservazione.File[] tempFiles = new it.kdm.docer.conservazione.File[files.length];
			
			for(int i=0; i<files.length; i++) {
				java.io.File tempFile = new java.io.File(tempDir, files[i].getName());
				FileUtils.copyInputStreamToFile(files[i].getHandler().getInputStream(),
						tempFile);
				it.kdm.docer.conservazione.File file = new it.kdm.docer.conservazione.File();
				file.setFile(tempFile);
				file.setId(files[i].getId());
				tempFiles[i] = file;
			}
			
			return businessLogic.versamento(doc, tempFiles, tipoDocumento, applicazioneChiamante,
					TipoConservazione.valueOf(tipoConservazione),
					forzaAccettazione, forzaConservazione, forzaCollegamento,
					ente, aoo);
			
			
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}
	
	public ConservazioneResult modificaMetadati(String token, DataHandler xmlDocumento, String tipoDocumento, 
			String applicazioneChiamante, String tipoConservazione, String ente, String aoo) throws ConservazioneException {
		try {
			StAXOMBuilder builder = new StAXOMBuilder(xmlDocumento.getInputStream());
			OMElement doc = builder.getDocumentElement();
			
			return businessLogic.modificaMetadati(doc, tipoDocumento, applicazioneChiamante, 
					TipoConservazione.valueOf(tipoConservazione), ente, aoo);
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}
	
	public ConservazioneResult aggiungiDocumento(String token, DataHandler xmlDocumento, String tipoDocumento, 
			String applicazioneChiamante, String tipoConservazione, boolean forzaAccettazione, boolean forzaConservazione,
			boolean forzaCollegamento, File file, String ente, String aoo) throws ConservazioneException {
		try {
			StAXOMBuilder builder = new StAXOMBuilder(xmlDocumento.getInputStream());
			OMElement doc = builder.getDocumentElement();
			
			java.io.File tempFile = new java.io.File(tempDir, file.getName());
			FileUtils.copyInputStreamToFile(file.getHandler().getInputStream(),
					tempFile);
			it.kdm.docer.conservazione.File newFile = new it.kdm.docer.conservazione.File();
			newFile.setFile(tempFile);
			newFile.setId(file.getId());
			
			return businessLogic.aggiungiDocumento(doc, tipoDocumento, applicazioneChiamante, 
					TipoConservazione.valueOf(tipoConservazione),
					forzaAccettazione, forzaConservazione, forzaCollegamento, newFile,
					ente, aoo);
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}
	
}
