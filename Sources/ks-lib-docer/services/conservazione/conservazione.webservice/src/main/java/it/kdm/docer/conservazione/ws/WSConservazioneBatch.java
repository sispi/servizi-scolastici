package it.kdm.docer.conservazione.ws;

import it.eng.parer.ccd.lib.ParerLib;
import it.kdm.docer.commons.Config;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.conservazione.ConservazioneException;
import it.kdm.docer.conservazione.ConservazioneResult;
import it.kdm.docer.conservazione.DocerLib;
import it.kdm.docer.conservazione.IDocerLib;
import it.kdm.docer.conservazione.bl.Conservazione;
import it.kdm.docer.conservazione.bl.IConservazioneBL;
import it.kdm.docer.conservazione.bl.IConservazioneBL.TipoConservazione;
import it.kdm.docer.conservazione.converter.Converter;
import it.kdm.docer.conservazione.provider.parer.EntePasswordManager;
import it.kdm.docer.conservazione.provider.parer.ProviderParer;
import it.kdm.docer.sdk.EnumStatiConservazione;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.utils.DataTable;
import it.kdm.utils.json.StringDataTableDeserializer;
import it.kdm.utils.json.StringDataTableSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class WSConservazioneBatch {
	
	Logger log = org.slf4j.LoggerFactory.getLogger(WSConservazioneBatch.class);
	
	private Gson gson;
	
	//private boolean testMode;

	private TicketCipher cipher = new TicketCipher();

	private Config config;
	
	private IConservazioneBL businessLogic;
	private IDocerLib docerLib;
	
//	private String tempDir;

	public WSConservazioneBatch() throws ConservazioneException  {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(DataTable.class, new StringDataTableDeserializer());
			gsonBuilder.registerTypeAdapter(DataTable.class, new StringDataTableSerializer());
			gsonBuilder.create();
			
			this.gson = gsonBuilder.create();
			
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
			/*tempDir = config.getNode(makePropertyXpath("configuration", "tempDir")).getText();
			log.debug("tempDir: " + tempDir);*/
			
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
			
			/*String testMode = config.getNode(makePropertyXpath("configuration", "testMode")).getText();
			this.testMode = Boolean.parseBoolean(testMode);
			log.debug("testMode: " + testMode);*/
			
			String docerProvider = config.getNode(makePropertyXpath("docer", "provider")).getText();
			int primarySearchMaxRows = Integer.parseInt(config.getNode(makePropertyXpath("docer", "primarySearchMaxRows")).getText());
			File diskBufferDirectory = new File(config.getNode(makePropertyXpath("docer", "diskBufferDirectory")).getText());
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
	
	public ConservazioneResult versamento(String token, DataHandler xmlDocumentoPrincipale, 
			DataHandler xmlAllegati, String tipoDocumento, 
			String applicazioneChiamante, String tipoConservazione,
			boolean forzaAccettazione, boolean forzaConservazione,
			boolean forzaCollegamento, String[] filesURI, String ente, String aoo) throws ConservazioneException {
	
		try {
			OMElement rootDoc = buildXml(xmlDocumentoPrincipale, xmlAllegati);
			
			//File tempFolder = new File(tempDir);
			
			it.kdm.docer.conservazione.File[] fileArray = new it.kdm.docer.conservazione.File[filesURI.length];
			for(int i=0; i<filesURI.length; i++) {
				File file = new File(new URI(filesURI[i]));
				fileArray[i] = new it.kdm.docer.conservazione.File(); 
				fileArray[i].setFile(file);
				fileArray[i].setId(file.getName());
				/*if(this.testMode) {
					file = new File(tempFolder, file.getName());
					fileArray[i].setFile(file);
					FileUtils.write(file, "test_mode");
				}*/
			}
			
			return businessLogic.versamento(rootDoc, fileArray, tipoDocumento, applicazioneChiamante,
					TipoConservazione.valueOf(tipoConservazione),
					forzaAccettazione, forzaConservazione, forzaCollegamento,
					ente, aoo);
			
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}
	
	public ConservazioneResult modificaMetadati(String token, DataHandler xmlDocumentoPrincipale, 
			String tipoDocumento, String applicazioneChiamante,
			String tipoConservazione, String ente, String aoo) throws ConservazioneException {
	
		try {
			OMElement rootDoc = buildXml(xmlDocumentoPrincipale, null);
			
			return businessLogic.modificaMetadati(rootDoc, tipoDocumento, applicazioneChiamante,
					TipoConservazione.valueOf(tipoConservazione), ente, aoo);
			
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}
	
	public ConservazioneResult aggiungiDocumento(String token, DataHandler xmlDocumentoPrincipale, 
			DataHandler xmlAllegati, String tipoDocumento, 
			String applicazioneChiamante, String tipoConservazione, boolean forzaAccettazione, boolean forzaConservazione,
			boolean forzaCollegamento, String fileURI, String ente, String aoo) throws ConservazioneException {
	
		try {
			OMElement rootDoc = buildXml(xmlDocumentoPrincipale, xmlAllegati);
			
			//File tempFolder = new File(tempDir);
			
			File file = new File(new URI(fileURI));
			it.kdm.docer.conservazione.File container = new it.kdm.docer.conservazione.File(); 
			container.setFile(file);
			container.setId(file.getName());
			
			return businessLogic.aggiungiDocumento(rootDoc, tipoDocumento, applicazioneChiamante, 
					TipoConservazione.valueOf(tipoConservazione), 
					forzaAccettazione, forzaConservazione, forzaCollegamento, 
					container, ente, aoo);
			
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}

	private OMElement buildXml(DataHandler xmlDocumentoPrincipale,
			DataHandler xmlAllegati) throws XMLStreamException, IOException {
		StAXOMBuilder builder = new StAXOMBuilder(xmlDocumentoPrincipale.getInputStream());
		OMElement document = builder.getDocumentElement();
		
		OMElement rootDoc = document.getOMFactory().createOMElement("root", 
				document.getDefaultNamespace());
		rootDoc.addChild(document);
		
		if (xmlAllegati != null) {
			builder = new StAXOMBuilder(xmlAllegati.getInputStream());
			rootDoc.addChild(builder.getDocumentElement());
		} else {
			rootDoc.addChild(document.getOMFactory().createOMElement("allegati", 
				document.getDefaultNamespace()));
		}
		return rootDoc;
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
			docerLib.writeConfig(cipher.decryptTicket(token), xmlConfig);
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
	
	public String search(String token, String searchCriteria, String[] columnNames) throws ConservazioneException {
		try {
			Type mapType = new TypeToken<Map<String, List<String>>>() {}.getType();
			Map<String, List<String>> searchCriteriaMap = 
					this.gson.fromJson(searchCriteria, mapType);

			DataTable<String> result = docerLib.search(cipher.decryptTicket(token), searchCriteriaMap, this.makeSet(columnNames));
			
			return this.gson.toJson(result);
		} catch (DocerException e) {
			throw new ConservazioneException(e);
		}
	}
	
	private Set<String> makeSet(String[] columnNames) {
		Set<String> resultSet = new HashSet<String>();
		for(String columnName : columnNames) {
			resultSet.add(columnName);
		}
		
		return resultSet;
	}

	public String searchAllegati(String token, String docId, String[] columnNames) throws ConservazioneException {
		try {
			DataTable<String> result = docerLib.searchAllegati(cipher.decryptTicket(token), docId, this.makeSet(columnNames));
			return this.gson.toJson(result);
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}
	
	public boolean updateDocumentState(String token, String docId, String statoConservazione)throws ConservazioneException {
		try {
			docerLib.updateDocumentState(cipher.decryptTicket(token), docId, EnumStatiConservazione.valueOf(statoConservazione));
			return true;
		} catch (DocerException e) {
			throw new ConservazioneException(e);
		}
	}
	
	public boolean updateDocumentProfile(String token, String docId, String metadata)throws ConservazioneException {
		try {
			Type mapType = new TypeToken<Map<String, String>>() {}.getType();
			Map<String, String> metadataMap = this.gson.fromJson(metadata, mapType);
			
			docerLib.updateDocumentProfile(cipher.decryptTicket(token), docId, metadataMap);
			return true;
		} catch (DocerException e) {
			throw new ConservazioneException(e);
		}
	}
	
	public String getDocuments(String token, String docId)throws ConservazioneException {
		try {
			return docerLib.getDocuments(cipher.decryptTicket(token), docId).toString();
		} catch (DocerException e) {
			throw new ConservazioneException(e);
		}
	}
}
