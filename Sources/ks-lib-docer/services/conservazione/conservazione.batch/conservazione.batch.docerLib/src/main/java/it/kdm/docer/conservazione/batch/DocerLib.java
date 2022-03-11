package it.kdm.docer.conservazione.batch;

import it.kdm.docer.clients.AuthenticationServiceStub;
import it.kdm.docer.clients.AuthenticationServiceStub.Login;
import it.kdm.docer.clients.ExceptionException;
import it.kdm.docer.clients.WSConservazioneBatchStub;
import it.kdm.docer.clients.WSConservazioneBatchStub.GetDocuments;
import it.kdm.docer.clients.WSConservazioneBatchStub.GetDocumentsResponse;
import it.kdm.docer.clients.WSConservazioneBatchStub.Search;
import it.kdm.docer.clients.WSConservazioneBatchStub.SearchAllegati;
import it.kdm.docer.clients.WSConservazioneBatchStub.SearchAllegatiResponse;
import it.kdm.docer.clients.WSConservazioneBatchStub.SearchResponse;
import it.kdm.docer.clients.WSConservazioneBatchStub.UpdateDocumentProfile;
import it.kdm.docer.clients.WSConservazioneBatchStub.UpdateDocumentState;
import it.kdm.docer.clients.WSDemoneStub;
import it.kdm.docer.clients.WSDemoneStub.AddJob;
import it.kdm.docer.clients.WSDemoneStub.AddJobResponse;
import it.kdm.docer.clients.WSDemoneStub.GetJobByDocId;
import it.kdm.docer.clients.WSDemoneStub.GetJobByDocIdResponse;
import it.kdm.docer.clients.WSDemoneStub.Job;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;
import it.kdm.utils.json.StringDataTableDeserializer;
import it.kdm.utils.json.StringDataTableSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.URI;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.jaxp.OMSource;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.slf4j.Logger;
import org.jaxen.JaxenException;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DocerLib {

	private static final String COLUMN_TIPO_COMPONENTE = "TIPO_COMPONENTE";
	private static final String TIPO_COMPONENTE_PRINCIPALE = "PRINCIPALE";
	private static final String TIPO_COMPONENTE_ALLEGATO = "ALLEGATO";
	private static final String TIPO_COMPONENTE_ANNESSO = "ANNESSO";
	private static final String TIPO_COMPONENTE_ANNOTAZIONE = "ANNOTAZIONE";
	
	private static final String COLUMN_STATO_CONSERV = "STATO_CONSERV";
	private static final String STATO_CONSERV_DA_NON_CONSERVARE = "0";
    private static final String STATO_CONSERV_DA_CONSERVARE = "1";
	private static final String STATO_CONSERV_CONSERVATO = "3";
	private static final String STATO_CONSERV_IN_ERRORE = "4";
    private static final String STATO_CONSERV_DA_AGGIORNARE = "5";
    private static final String STATO_CONSERV_IN_ATTESA = "6";

	private static final String COLUMN_DOCID_PRINCIPALE = "DOCNUM_PRINC";
    private static final String COLUMN_COD_ENTE = "COD_ENTE";
    private static final String COLUMN_COD_AOO = "COD_AOO";

	Logger log = org.slf4j.LoggerFactory.getLogger(DocerLib.class);
    private final static long DEFAULT_TIMEOUT = 300;
    private final static TimeUnit DEFAULT_TIMEOUT_GRANULARITY = TimeUnit.SECONDS;

    private String description = "";

    public enum EnumStatiConservazione {

        da_non_conservare,
        da_conservare,
        inviato_a_conservazione,
        conservato,
        in_errore,
        da_aggiornare,
        in_attesa;
    }

    public enum ConservazioneWebServices {

        VSYNC, // Versamento Sync
        AASYNC, // AggiuntaAllegatiSync
        MMDSYNC, // ModificaMetaDatiSync
        VSSYNC          // VerificaStatoSync
    }
    private static WSConservazioneBatchStub conservazioneClient;
    private static WSDemoneStub demoneClient;
    private AuthenticationServiceStub docerClient;
    private Gson gson;
    private Transformer transformer;
    private Validator validator;
	private Map<String, String> defaultMappings;
	private OMElement callerApp;
	private boolean testMode;

    public DocerLib() throws Exception {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DataTable.class, new StringDataTableDeserializer());
        gsonBuilder.registerTypeAdapter(DataTable.class, new StringDataTableSerializer());
        gsonBuilder.create();

        this.gson = gsonBuilder.create();

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        File schemaFile = ConfigurationUtils.loadResourceFile("batchData.xsd");
        Schema schema = factory.newSchema(schemaFile);
        this.validator = schema.newValidator();

        this.transformer = TransformerFactory.newInstance().newTransformer();

        conservazioneClient = new WSConservazioneBatchStub();
        docerClient = new AuthenticationServiceStub();
        demoneClient = new WSDemoneStub();

        this.setTimeout(DEFAULT_TIMEOUT, DEFAULT_TIMEOUT_GRANULARITY);
    }

    public String getEprConservazione() {
        return conservazioneClient._getServiceClient().getTargetEPR().getAddress();
    }

    public void setEprConservazione(String epr) {
        log.info("Impostato EPR per conservazione: " + epr);
        EndpointReference targetEpr = new EndpointReference(epr);
        conservazioneClient._getServiceClient().setTargetEPR(targetEpr);
    }

    public String getEprDemone() {
        return demoneClient._getServiceClient().getTargetEPR().getAddress();
    }

    public void setEprDemone(String epr) {
        log.info("Impostato EPR per demone: " + epr);
        EndpointReference targetEpr = new EndpointReference(epr);
        demoneClient._getServiceClient().setTargetEPR(targetEpr);
    }

    public String getEprDocer() {
        return docerClient._getServiceClient().getTargetEPR().getAddress();   
    }

    public void setEprDocer(String epr) {
        log.info("Impostato EPR per docer: " + epr);
        EndpointReference targetEpr = new EndpointReference(epr);
        docerClient._getServiceClient().setTargetEPR(targetEpr);    
    }

    public void setProxy(HttpTransportProperties.ProxyProperties proxyProperties) {
        docerClient._getServiceClient().getOptions().setProperty(
                HTTPConstants.PROXY, proxyProperties);
        conservazioneClient._getServiceClient().getOptions().setProperty(
                HTTPConstants.PROXY, proxyProperties);
        demoneClient._getServiceClient().getOptions().setProperty(
                HTTPConstants.PROXY, proxyProperties);
    }

    public long getTimeout(TimeUnit granularity) {
        return granularity.convert(
                conservazioneClient._getServiceClient().getOptions().getTimeOutInMilliSeconds(),
                TimeUnit.MILLISECONDS);
    }

    public void setTimeout(long timeout, TimeUnit granularity) {
        long convertedTimeout = TimeUnit.MILLISECONDS.convert(timeout, granularity);

        Options options = conservazioneClient._getServiceClient().getOptions();
        options.setTimeOutInMilliSeconds(convertedTimeout);

        options = demoneClient._getServiceClient().getOptions();
        options.setTimeOutInMilliSeconds(convertedTimeout);
    }

    public String login(String username, String password, String codiceEnte) throws Exception {
    	Login req = new Login();
		req.setUsername(username);
		req.setPassword(password);
		req.setCodiceEnte(codiceEnte);
		//req.setApplication("conservazione");
		return docerClient.login(req).get_return();
    }

    protected DataTable<String> search(String token, Map<String, List<String>> searchCriteria, List<String> columnNames) throws Exception {

        Search req = new Search();
        req.setToken(token);

        Type mapType = new TypeToken<Map<String, List<String>>>() {
        }.getType();
        req.setSearchCriteria(this.gson.toJson(searchCriteria, mapType));

        req.setColumnNames(columnNames.toArray(new String[0]));

        SearchResponse resp = conservazioneClient.search(req);
        Type dataTableType = new TypeToken<DataTable<String>>() {
        }.getType();

        return this.gson.fromJson(resp.get_return(), dataTableType);
    }

    protected DataTable<String> searchAllegati(String token, String docId, List<String> columnNames) throws Exception {
        SearchAllegati req = new SearchAllegati();
        req.setToken(token);
        req.setDocId(docId);
        req.setColumnNames(columnNames.toArray(new String[0]));

        SearchAllegatiResponse resp = conservazioneClient.searchAllegati(req);
        Type dataTableType = new TypeToken<DataTable<String>>() {
        }.getType();
        return this.gson.fromJson(resp.get_return(), dataTableType);
    }

    public void updateDocumentState(String token, String docId, EnumStatiConservazione statoConservazione) throws Exception {
        UpdateDocumentState req = new UpdateDocumentState();
        req.setToken(token);
        req.setDocId(docId);
        req.setStatoConservazione(statoConservazione.toString());

        conservazioneClient.updateDocumentState(req);
    }

    public void updateDocumentProfile(String token, String docId, Map<String, String> metadata) throws Exception {
        UpdateDocumentProfile req = new UpdateDocumentProfile();
        req.setToken(token);
        req.setDocId(docId);

        //Type mapType = new TypeToken<Map<String, List<String>>>() {}.getType();
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        req.setMetadata(this.gson.toJson(metadata, mapType));

        conservazioneClient.updateDocumentProfile(req);
    }

    protected URI getDocuments(String token, String docId) throws Exception {
        GetDocuments req = new GetDocuments();
        req.setToken(token);
        req.setDocId(docId);

        GetDocumentsResponse resp = conservazioneClient.getDocuments(req);
        return new URI(resp.get_return());
    }
    
	private OMElement parseXml(URI xml) throws Exception,
			FileNotFoundException, XMLStreamException {
		FileInputStream input = null;
		
		try {
			File xmlFile = new File(xml);
			if (!xmlFile.exists()) {
				throw new Exception("L'xml di configurazione non esiste: "
						+ xml.toString());
			}
			input = new FileInputStream(xmlFile);
	
			StAXOMBuilder builder = new StAXOMBuilder(input);
			OMElement document = builder.getDocumentElement();
			try {
				this.validator.validate(new OMSource(document));
			} catch (Exception ex) {
				throw new Exception("L'xml di configurazione non e' valido: "
						+ xml.toString(), ex);
			}
			
			document.build();
			
			return document;
		} finally {
			if(input != null) {
				input.close();
			}
		}
	}

	public void preparaConservazione(String token, URI xml) throws Exception {
		preparaConservazione(token, xml, false);
	}
	
    public void preparaConservazione(String token, URI xml, boolean testMode) throws Exception {
        OMElement document = parseXml(xml);

        log.info("Batch DocerLib versione 1.2.3");

        OMElement mapping = this.getChild("mapping", document);
        defaultMappings = this.parseMappings(mapping);

        callerApp = this.getChild("callerApplication", document);
        
        this.testMode = testMode;

        List<?> nodes = this.xPathSelectNodes("search", document);

        for (Object nodeObj : nodes) {
            OMElement searchNode = (OMElement) nodeObj;

            OMElement description = xPathSelectSingleNode("description", searchNode);
            if (description == null || description.getText().isEmpty()) {
                this.description = "";
            } else {
                this.description = description.getText();
            }

            preparaDocumentiDaAggiornare(token, searchNode);
            preparaAllegatiDaAggiungere(token, searchNode);
            preparaDocumentiDaConservare(token, searchNode);
        }
    }

	
	private void preparaDocumentiDaConservare(String token, OMElement searchNode) throws Exception {
		preparaDocumenti(token, searchNode, false);
	}

	private void preparaDocumentiDaAggiornare(String token, OMElement searchNode) throws Exception {
		preparaDocumenti(token, searchNode, true);
	}

	private void preparaAllegatiDaAggiungere(String token,
			OMElement searchNode) throws Exception  {
		OMElement documentTypeElem = this.getChild("documentType", searchNode);
        OMElement tipoConservazioneElem = this.getChild("tipoConservazione", searchNode);

        Map<String, String> localMappings = this.parseMappings(this.getChildOrNull("mapping", searchNode));

        Map<String, List<String>> searchCriteria = parseCriterias(searchNode);
        
        addSearchCriteria(searchCriteria, COLUMN_STATO_CONSERV, STATO_CONSERV_DA_AGGIORNARE, STATO_CONSERV_IN_ATTESA);
        addSearchCriteria(searchCriteria, COLUMN_TIPO_COMPONENTE, 
        							TIPO_COMPONENTE_ALLEGATO,
        							TIPO_COMPONENTE_ANNESSO,
        							TIPO_COMPONENTE_ANNOTAZIONE);
        
        OMElement docColumnsElement = this.getChild("documentColumnNames", searchNode);
        List<String> docColumnNames = this.parseColumns(docColumnsElement);
        docColumnNames.add(COLUMN_COD_ENTE);
        docColumnNames.add(COLUMN_COD_AOO);
        docColumnNames.add(COLUMN_STATO_CONSERV);

        OMElement attachmentsColumnElement = this.getChild("attachmentColumnNames", searchNode);
        List<String> attachmentColumnNames = this.parseColumns(attachmentsColumnElement);
        attachmentColumnNames.add(COLUMN_DOCID_PRINCIPALE);
        attachmentColumnNames.add(COLUMN_TIPO_COMPONENTE);
        attachmentColumnNames.add(COLUMN_COD_ENTE);
        attachmentColumnNames.add(COLUMN_COD_AOO);

        DataTable<String> results = this.search(token, searchCriteria, attachmentColumnNames);

        // Sovrascrive il search criteria dopo la prima search per non riprocessare gli allegati in errore
        addSearchCriteria(searchCriteria, COLUMN_STATO_CONSERV, STATO_CONSERV_DA_AGGIORNARE);

		if (results.getRows().size() == 0) {
		    log.info(String.format("<%s> Non sono stati trovati allegati da aggiungere",
                    description));
		}

		while (results.getRows().size() > 0) {
		    for (DataRow<String> attachmentResult : results) {
		        
		    	String docId = attachmentResult.get("DOCNUM");
		        
		        if(testMode) {
		        	// Test mode only reports found documents
		        	continue;
		        }
		        
		        String docIdPrincipale = attachmentResult.get(COLUMN_DOCID_PRINCIPALE);
		        
		        Map<String, List<String>> singleDocCriterias =
		        		new HashMap<String, List<String>>();
		        addSearchCriteria(singleDocCriterias, "DOCNUM", docIdPrincipale);
		        
		        DataTable<String> docResults = this.search(token, singleDocCriterias, docColumnNames);
		        
		        if (docResults.getRows().size() == 0) {
		        	log.info(String.format("<%s> L'allegato %s non e' associato a nessun documento principale",
                            description, docId));
                    updateDocumentState(token, docId, EnumStatiConservazione.in_errore);
		        } else {
		        	DataRow<String> docResult = docResults.getRow(0);
                    String statoConservPrinc = docResult.get(COLUMN_STATO_CONSERV);
                    if (statoConservPrinc.equals(STATO_CONSERV_DA_NON_CONSERVARE)) {
                        log.info(String.format("<%s> Il documento princpale %s non e' conservato, pertanto l'allegato %s non e' da aggiungere.",
                                description, docIdPrincipale, docId));
                        updateDocumentState(token, docId, EnumStatiConservazione.in_errore);
		        	} else {
		        		log.info(String.format("<%s> Preparando il job per l'allegato %s con documento principale %s.",
                                description, docId, docIdPrincipale));
		        		
		        		StringWriter documentWriter = new StringWriter();
			            this.transformer.transform(new DOMSource(docResult.getXml("documento")),
			                    new StreamResult(documentWriter));
			            String documentXml = documentWriter.toString();
			            documentWriter.close();
			            
			            URI attachment = this.getDocuments(token, docId);
			            
			            documentWriter = new StringWriter();
			            
			            String rootElement;
			            if (attachmentResult.get(COLUMN_TIPO_COMPONENTE).equals(
			            		TIPO_COMPONENTE_ALLEGATO)) {
			            	rootElement = "allegato";
			            } else if (attachmentResult.get(COLUMN_TIPO_COMPONENTE).equals(
			            		TIPO_COMPONENTE_ANNOTAZIONE)) {
			            	rootElement = "annotazione";
			            } else {
			            	rootElement = "annesso";
			            }
			            			       
			            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			            doc.appendChild(doc.createElement("allegati"));
			            Element root = doc.getDocumentElement();
			            
			            root.appendChild(doc.adoptNode(attachmentResult.getXml(rootElement).getDocumentElement()));
			            
						this.transformer.transform(new DOMSource(doc),
			                    new StreamResult(documentWriter));
			            String attachmentXml = documentWriter.toString();
			            documentWriter.close();
			            
			            String documentType = this.getValue(docResult, "documentType",
			                    defaultMappings, localMappings, documentTypeElem.getText());

			            String appVersante = this.getValue(docResult, "callerApplication",
			                    defaultMappings, localMappings, callerApp.getText());

			            String tipoConservazione = this.getValue(docResult, "tipoConservazione",
			                    defaultMappings, localMappings, tipoConservazioneElem.getText());

			            boolean forzaCollegamento = this.getBooleanValue(docResult, "forzaCollegamento",
			                    defaultMappings, localMappings, false);
			            boolean forzaAccettazione = this.getBooleanValue(docResult, "forzaAccettazione",
			                    defaultMappings, localMappings, false);
			            boolean forzaConservazione = this.getBooleanValue(docResult, "forzaConservazione",
			                    defaultMappings, localMappings, false);

                        String codiceEnte = docResult.get(COLUMN_COD_ENTE);
                        String codiceAoo  = docResult.get(COLUMN_COD_AOO);

			            // TODO: Togliere                    
			            String dataRegistrazioneStr = this.getValue(docResult, "dataRegistrazione", 
			            		defaultMappings, localMappings, "1970-01-01T00:00:00.000Z"); 
			    		DateTime dataRegistrazione = ISODateTimeFormat.dateTimeParser().parseDateTime(dataRegistrazioneStr);
			            		
			            OMElement identifierElem = this.getChild("identifierColumnNames", searchNode);
			            StringBuilder identifier = new StringBuilder();
			            
			            Iterator<?> children = identifierElem.getChildElements();
			            while(children.hasNext()) {
			            	OMElement child = (OMElement)children.next();
			            	String nodeName = child.getText();
			            	identifier.append(docResult.get(nodeName));
			            	if(children.hasNext()) {
			            		identifier.append("/");
			            	}
			            }
			            
			            boolean addJobRes = false;
			            String compositeId = String.format("%s%c%s", docIdPrincipale,
			            		Constants.DOCID_SEPARATOR, docId);
			            try {
			                addJobRes = this.addJob(ConservazioneWebServices.VSYNC.name(), compositeId,
			                		documentXml, attachmentXml, attachment.toString(), tipoConservazione, documentType,
			                        appVersante, forzaCollegamento, forzaAccettazione, forzaConservazione, dataRegistrazione.toDate(),
			                        identifier.toString(), codiceEnte, codiceAoo, Azione.AGGIUNGI_DOCUMENTO);
			            } catch (Exception e) {
			                log.error(String.format("<%s> %s", description, e.getLocalizedMessage()), e);
			            }
			            
			            if (!addJobRes) {
			            	log.warn(String.format(
			            			"<%s> Job per l'allegato %s con documento principale %s gia' esistente",
			            			description, docId, docIdPrincipale));
			                
			                Stato state = this.getJobState(compositeId, Azione.AGGIUNGI_DOCUMENTO);
			                if (state == Stato.COMPLETATO || state == Stato.ERRORE_DOCER) {
			                	log.warn(String.format(
				            			"<%s> Job per l'allegato %s con documento principale %s risulta completato. Lo stato del documento e' da aggiornare",
				            			description, docId, docIdPrincipale));
			                    updateState(token, EnumStatiConservazione.conservato, docId);
			                    if(state == Stato.ERRORE_DOCER) {
			                        this.completeJob(compositeId, Azione.AGGIUNGI_DOCUMENTO);
			                    }
			                    log.info(String.format(
				            			"<%s> Stato dell'allegato %s con documento principale %s aggiornato",
				            			description, docId, docIdPrincipale));
			                } else if (state == Stato.ERRORE) {
			                    log.warn(String.format(
			                    		"<%s> Job per l'allegato %s con documento principale %s risulta in errore. Bisogna riavviare il job",
			                    		description, docId, docIdPrincipale));
			                    this.restartJob(compositeId, Azione.AGGIUNGI_DOCUMENTO);
			                    //this.updateDocumentState(token, docId, EnumStatiConservazione.in_errore);
			                    log.info(String.format(
			                    		"<%s> Job per l'allegato %s con documento principale %s riavviato.",
			                    		description, docId, docIdPrincipale));
                            } else if (state == Stato.IN_ATTESA) {
                                log.warn(String.format(
                                        "<%s> Job per l'allegato %s con documento principale %s gia' in attesa. Bisogna aggiornare lo stato doc/ER",
                                        description, docId, docIdPrincipale));
                                updateState(token, EnumStatiConservazione.inviato_a_conservazione, docId);
                                log.info(String.format("<%s> Stato del documento %s aggiornato.",
                                        description, docId));
                            } else {
                                log.error(String.format(
                                    "<%s> Job per il documento %s in stato inatteso: %s.", description,
                                        docId, state));
                                updateState(token, EnumStatiConservazione.in_errore, docId);
                                log.info(String.format("<%s> Stato del documento %s aggiornato.",
                                        description, docId));
                            }
			            } else {
			            	log.info(String.format(
		                    		"<%s> Job per l'allegato %s con documento principale %s aggiunto.",
		                    		description, docId, docIdPrincipale));
			            	updateState(token, EnumStatiConservazione.inviato_a_conservazione, docId);
			                log.info(String.format(
		                    		"<%s> Job per l'allegato %s con documento principale %s aggiornato.",
		                    		description, docId, docIdPrincipale));
			            }
		        	}
		        }
		    }
		    
		    if(testMode) {
		    	break;
		    }
		    
		    results = this.search(token, 
		    		searchCriteria, attachmentColumnNames);
		}
	}

	private void updateState(String token, EnumStatiConservazione stato, String... ids) throws Exception {
		for (String id : ids) {
			this.updateDocumentState(token, id, stato);
		}
	}

	private void preparaDocumenti(String token, OMElement searchNode,
			boolean aggiornamento) throws Exception {
		
		OMElement documentTypeElem = this.getChild("documentType", searchNode);
        OMElement tipoConservazioneElem = this.getChild("tipoConservazione", searchNode);

        Map<String, String> localMappings = this.parseMappings(this.getChildOrNull("mapping", searchNode));

        Map<String, List<String>> searchCriteria = parseCriterias(searchNode);
        
        if (aggiornamento) {
        	addSearchCriteria(searchCriteria, COLUMN_STATO_CONSERV, STATO_CONSERV_DA_AGGIORNARE);
        } else {
        	addSearchCriteria(searchCriteria, COLUMN_STATO_CONSERV, STATO_CONSERV_DA_CONSERVARE);
        }
        
        addSearchCriteria(searchCriteria, COLUMN_TIPO_COMPONENTE, TIPO_COMPONENTE_PRINCIPALE);

        OMElement docColumnsElement = this.getChild("documentColumnNames", searchNode);
        List<String> docColumnNames = this.parseColumns(docColumnsElement);
        docColumnNames.add(COLUMN_COD_ENTE);
        docColumnNames.add(COLUMN_COD_AOO);

        OMElement attachmentsColumnElement = this.getChild("attachmentColumnNames", searchNode);
        List<String> attachmentColumnNames = this.parseColumns(attachmentsColumnElement);
        attachmentColumnNames.add(COLUMN_STATO_CONSERV);
        attachmentColumnNames.add(COLUMN_TIPO_COMPONENTE);
        
		DataTable<String> results = this.search(token, searchCriteria, docColumnNames);

		if (results.getRows().size() == 0) {
		    log.info(String.format("<%s> Non sono stati trovati documenti da %s",
		    		description, aggiornamento ? "aggiornare" : "conservare"));
		}

		while (results.getRows().size() > 0) {
		    for (DataRow<String> result : results) {
		        
		    	String docId = result.get("DOCNUM");
		        log.info(String.format("<%s> Preparando il job per il documento %s",
                        description, docId));
		        
		        if(testMode) {
		        	// Test mode only reports found documents
		        	continue;
		        }
		        
		        try {
		            List<URI> files = new ArrayList<URI>();
		            files.add(this.getDocuments(token, docId));

		            StringWriter documentWriter = new StringWriter();
		            this.transformer.transform(new DOMSource(result.getXml("documento")),
		                    new StreamResult(documentWriter));
		            String documentXml = documentWriter.toString();
		            documentWriter.close();
		            
		            Set<String> idSet = new HashSet<String>();
	            	idSet.add(docId);
	            	
		            String attachmentsXml;
		            
		            if (aggiornamento) {
		            	attachmentsXml = "<allegati />";
		            } else {
		            	
		            	Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		            	doc.appendChild(doc.createElement("allegati"));
		            	Element root = doc.getDocumentElement();
		            		            	
		            	DataTable<String> attachments = this.searchAllegati(token, docId,
			                    attachmentColumnNames);
			            for (DataRow<String> attachment : attachments) {
			            	if (attachment.get(COLUMN_STATO_CONSERV).isEmpty() ||
                                    !attachment.get(COLUMN_STATO_CONSERV).equals(STATO_CONSERV_CONSERVATO)) {
				            	String attachmentId = attachment.get("DOCNUM");
				            	idSet.add(attachmentId);
				                files.add(this.getDocuments(token, attachmentId));
				                
				                String rootElement;
					            if (attachment.get(COLUMN_TIPO_COMPONENTE).equals(
					            		TIPO_COMPONENTE_ALLEGATO)) {
					            	rootElement = "allegato";
					            } else if (attachment.get(COLUMN_TIPO_COMPONENTE).equals(
					            		TIPO_COMPONENTE_ANNOTAZIONE)) {
					            	rootElement = "annotazione";
					            } else {
					            	rootElement = "annesso";
					            }
					            
					            Document attachmentXml = attachment.getXml(rootElement);
					            root.appendChild(
					            		doc.adoptNode(attachmentXml.getDocumentElement()));
			            	}
			            }
	
			            documentWriter = new StringWriter();
			            this.transformer.transform(new DOMSource(doc),
			                    new StreamResult(documentWriter));
			            attachmentsXml = documentWriter.toString();
			            documentWriter.close();
		            }
		            
		            String[] ids = idSet.toArray(new String[0]);

		            String documentType = this.getValue(result, "documentType",
		                    defaultMappings, localMappings, documentTypeElem.getText());

		            String appVersante = this.getValue(result, "callerApplication",
		                    defaultMappings, localMappings, callerApp.getText());

		            String tipoConservazione = this.getValue(result, "tipoConservazione",
		                    defaultMappings, localMappings, tipoConservazioneElem.getText());

		            boolean forzaCollegamento = this.getBooleanValue(result, "forzaCollegamento",
		                    defaultMappings, localMappings, false);
		            boolean forzaAccettazione = this.getBooleanValue(result, "forzaAccettazione",
		                    defaultMappings, localMappings, false);
		            boolean forzaConservazione = this.getBooleanValue(result, "forzaConservazione",
		                    defaultMappings, localMappings, false);

                    String codiceEnte = result.get(COLUMN_COD_ENTE);
                    String codiceAoo  = result.get(COLUMN_COD_AOO);

		            // TODO: Togliere
		            String dataRegistrazioneStr = this.getValue(result, "dataRegistrazione", 
		            		defaultMappings, localMappings, "1970-01-01T00:00:00.000Z"); 
		    		DateTime dataRegistrazione = ISODateTimeFormat.dateTimeParser().parseDateTime(dataRegistrazioneStr);
		            		
		            OMElement identifierElem = this.getChild("identifierColumnNames", searchNode);
		            StringBuilder identifier = new StringBuilder();
		            
		            Iterator<?> children = identifierElem.getChildElements();
		            while(children.hasNext()) {
		            	OMElement child = (OMElement)children.next();
		            	String nodeName = child.getText();
		            	identifier.append(result.get(nodeName));
		            	if(children.hasNext()) {
		            		identifier.append("/");
		            	}
		            }
		                                    
		            boolean addJobRes = false;

                    Azione azione;
                    if (aggiornamento) {
                        azione = Azione.MODIFICA_METADATI;
                    } else {
                        azione = Azione.CONSERVAZIONE;
                    }

		            try {
                        addJobRes = this.addJob(ConservazioneWebServices.VSYNC.name(), docId, documentXml, attachmentsXml, this.join(files),
		                        tipoConservazione, documentType,
		                        appVersante, forzaCollegamento, forzaAccettazione, forzaConservazione, dataRegistrazione.toDate(),
		                        identifier.toString(), codiceEnte, codiceAoo, azione);
		            } catch (Exception e) {
		                log.error(e.getMessage(), e);
		                e.printStackTrace();
		            }

		            if (!addJobRes) {
		                log.warn(String.format("<%s> Job per il documento %s gia' esistente.",
                                description, docId));
		                Stato state = this.getJobState(docId, azione);
		                if (state == Stato.COMPLETATO || state == Stato.ERRORE_DOCER) {
		                    log.warn(String.format(
                                    "<%s> Job per il documento %s risulta completato. Lo stato del documento e' da aggiornare",
                                    description, docId));
		                    updateState(token, EnumStatiConservazione.conservato, ids);
		                    if(state == Stato.ERRORE_DOCER) {
		                        this.completeJob(docId, azione);
		                    }
		                    log.info(String.format(
                                    "<%s> Stato del documento %s aggiornato.",
                                    description, docId));
		                } else if (state == Stato.ERRORE) {
		                    log.warn(String.format(
                                    "<%s> Job per il documento %s risulta in errore. Bisogna riavviare il job",
                                    description, docId));
		                    this.restartJob(docId, azione);
		                    //this.updateDocumentState(token, docId, EnumStatiConservazione.in_errore);
		                    log.info(String.format("<%s> Job %s riavviato.", description, docId));
		                } else if (state == Stato.IN_ATTESA) {
                            log.warn(String.format(
                                    "<%s> Job per il documento %s gia' in attesa. Bisogna aggiornare lo stato doc/ER",
                                    description, docId));
                            updateState(token, EnumStatiConservazione.inviato_a_conservazione, ids);
                            log.info(String.format("<%s> Stato del documento %s aggiornato.",
                                    description, docId));
                        } else {
                            log.error(String.format(
                                    "<%s> Job per il documento %s in stato inatteso: %s.", description,
                                    docId, state));
                            updateState(token, EnumStatiConservazione.in_errore, ids);
                            log.info(String.format("<%s> Stato del documento %s aggiornato.",
                                    description, docId));
                        }
		            } else {
		                log.info(String.format("<%s> Job per il documento %s aggiunto.",
                                description, docId));
		                updateState(token, EnumStatiConservazione.inviato_a_conservazione, ids);
		                log.info(String.format("<%s> Stato del documento %s aggiornato.",
                                description, docId));
		            }
		        } catch (Exception ex) {
		            log.error(String.format("<%s> Si e' verificato un errore per il documento %s",
                            description, docId), ex);
		        }
		    }

		    if(testMode) {
		    	break;
		    }
		    results = this.search(token,
		    		searchCriteria, docColumnNames);
		}
	}
	
	// Returns whether the collection has been modified
    private boolean addSearchCriteria(Map<String, List<String>> criterias,
    		String key, String... values) {
    	
		ArrayList<String> vals = new ArrayList<String>();
    	if (Collections.addAll(vals, values)) {
    		criterias.put(key, vals);
    		return true;
    	}
    	
    	return false;
	}

	private Stato getJobState(String docId, Azione action) throws Exception {

        GetJobByDocId req = new GetJobByDocId();
        req.setId_doc(docId);
        req.setAzione(action.toString());

        GetJobByDocIdResponse resp = demoneClient.getJobByDocId(req);
        Job job = resp.get_return();
        
        return Stato.fromString(job.getStato());
    }

    private List<String> parseColumns(OMElement docColumnsElement) throws JaxenException {
        List<String> columnNames = new ArrayList<String>();
        List<?> docColumnNamesElements = this.xPathSelectNodes("column", docColumnsElement);
        for (Object columnNameObj : docColumnNamesElements) {
            OMElement columnName = (OMElement) columnNameObj;
            columnNames.add(columnName.getText());
        }

        return columnNames;
    }

    private Map<String, List<String>> parseCriterias(OMElement searchNode)
            throws JaxenException {
        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
        List<?> criterias = this.xPathSelectNodes("criterias/criteria", searchNode);

        for (Object criteriaObj : criterias) {
            OMElement criteria = (OMElement) criteriaObj;
            String name = criteria.getAttributeValue(new QName("name"));
            List<String> values = new ArrayList<String>();
            List<?> valueChildren = this.xPathSelectNodes("value", criteria);
            for (Object valueObj : valueChildren) {
                OMElement value = (OMElement) valueObj;
                values.add(value.getText());
            }
            searchCriteria.put(name, values);
        }
        return searchCriteria;
    }

    private Map<String, String> parseMappings(OMElement mapping) {
        Map<String, String> retMap = new HashMap<String, String>();

        if (mapping != null) {
            Iterator<?> children = mapping.getChildElements();
            while (children.hasNext()) {
                OMElement child = (OMElement) children.next();
                retMap.put(child.getLocalName(), child.getText());
            }
        }

        return retMap;
    }

    private boolean addJob(String web_service, String docId, String documentXml,
            String attachmentsXml, String files, String tipoConservazione,
            String documentType, String appVersante, boolean forzaCollegamento,
            boolean forzaAccettazione, boolean forzaConservazione, Date dataRegistrazione,
            String chiaveDoc, String codEnte, String codAoo, Azione azione) throws Exception {

        AddJob request = new AddJob();
        request.setDocId(docId);
        request.setDocumentoPrincipale(documentXml);
        request.setAllegati(attachmentsXml);
        request.setFiles(files);
        request.setTipoConservazione(tipoConservazione);
        request.setTipoDocumento(documentType);
        request.setApplicazioneChiamante(appVersante);
        request.setForzaCollegamento(forzaCollegamento);
        request.setForzaAccettazione(forzaAccettazione);
        request.setForzaConservazione(forzaConservazione);
        Calendar c = Calendar.getInstance();
        c.setTime(dataRegistrazione);
        request.setDataRegistrazione(c);
        request.setWeb_service(web_service);
        request.setChiave_doc(chiaveDoc);
        request.setCodEnte(codEnte);
        request.setCodAoo(codAoo);
        request.setAzione(azione.toString());

        AddJobResponse response = demoneClient.addJob(request);

        return response.get_return();
    }
    
    private boolean restartJob(String docId, Azione azione) throws Exception {
        
        Job job = getJob(docId, azione);
        
        WSDemoneStub.UpdateJob request = new WSDemoneStub.UpdateJob();
        request.setId_job(job.getId());
        request.setForzaAccettazione(job.getForzaAccettazione());
        request.setForzaCollegamento(job.getForzaCollegamento());
        request.setForzaConservazione(job.getForzaConservazione());
        // RIAVVIA
        request.setStato("A");
        
        WSDemoneStub.UpdateJobResponse respone = demoneClient.updateJob(request);
        return respone.get_return();
    }
    
    private boolean completeJob(String docId, Azione azione) throws Exception {
        
        Job job = this.getJob(docId, azione);
                
        WSDemoneStub.UpdateJob request = new WSDemoneStub.UpdateJob();
        request.setId_job(job.getId());
        request.setForzaAccettazione(job.getForzaAccettazione());
        request.setForzaCollegamento(job.getForzaCollegamento());
        request.setForzaConservazione(job.getForzaConservazione());
        // RIAVVIA
        request.setStato("C");
        
        WSDemoneStub.UpdateJobResponse respone = demoneClient.updateJob(request);
        return respone.get_return();
    }
    
    private Job getJob(String docId, Azione azione) throws Exception {
        GetJobByDocId request = new GetJobByDocId();
        request.setId_doc(docId);
        request.setAzione(azione.toString());

        GetJobByDocIdResponse resp = demoneClient.getJobByDocId(request);
        
        return resp.get_return();
    }

    private String getValue(DataRow<String> row, String valueName,
            Map<String, String> defaultMappings, Map<String, String> localMappings,
            String defaultReturn) {

        String colName = localMappings.get(valueName);
        if (colName == null) {
            colName = defaultMappings.get(valueName);
        }

        String value = row.get(colName);
        if (value == null || value.equals("")) {
            value = defaultReturn;
        }

        return value;
    }

    private boolean getBooleanValue(DataRow<String> row, String valueName,
            Map<String, String> defaultMappings, Map<String, String> localMappings,
            boolean defaultReturn) {
        String value = this.getValue(row, valueName, defaultMappings, localMappings, null);
        if (value == null) {
            return defaultReturn;
        }

        return Boolean.parseBoolean(value);
    }

    private String join(List<URI> files) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < files.size(); i++) {
            builder.append(files.get(i).toString());
            if (i < files.size() - 1) {
                builder.append("|");
            }
        }

        return builder.toString();
    }

    protected final OMElement xPathSelectSingleNode(String xpath, OMElement node)
            throws JaxenException {
        AXIOMXPath path = new AXIOMXPath(node, xpath);
        return (OMElement) path.selectSingleNode(node);
    }

    protected final List<?> xPathSelectNodes(String xpath, OMElement node)
            throws JaxenException {
        AXIOMXPath path = new AXIOMXPath(node, xpath);
        return path.selectNodes(node);
    }

    protected final OMElement getChild(String nodeName, OMElement node)
            throws Exception {
        Iterator<?> nodes = node.getChildrenWithName(new QName(nodeName));
        if (!nodes.hasNext()) {
            throw new Exception(String.format(
                    "Node %s not found in node %s.", nodeName,
                    node.getLocalName()));
        }

        return (OMElement) nodes.next();
    }

    protected final OMElement getChildOrNull(String nodeName, OMElement node) {
        Iterator<?> nodes = node.getChildrenWithName(new QName(nodeName));
        if (!nodes.hasNext()) {
            return null;
        }

        return (OMElement) nodes.next();
    }
}
