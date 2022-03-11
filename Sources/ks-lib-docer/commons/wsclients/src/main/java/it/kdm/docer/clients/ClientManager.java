package it.kdm.docer.clients;

//import it.gov.digitpa.www.protocollo.ProtocolloService;
//import it.gov.digitpa.www.protocollo.ProtocolloServiceStub;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.kdm.docer.commons.configuration.ConfigurationUtils;

public enum ClientManager {

    INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(ClientManager.class);

    private HttpClient httpClient = null;

    private DocerServicesStub docerServicesClient = null;
    private String docerEpr;
    private boolean docerRequiresChanges = false;

    private WSConservazioneStub conservazioneClient = null;
    private String conservazioneEpr;
    private boolean conservazioneRequiresChanges = false;

    private WSConservazioneBatchStub conservazioneBatchClient = null;
    private String conservazioneBatchEpr;
    private boolean conservazioneBatchRequiresChanges = false;

    private WSDemoneStub demoneClient = null;
    private String demoneEpr;
    private boolean demoneRequiresChanges = false;

    private AuthorizationServiceStub authorizationServicesClient = null;
    private String authorizationEpr;
    private boolean authorizationRequiresChanges = false;

    private AuthenticationServiceStub authenticationServicesClient = null;
    private String authenticationEpr;
    private boolean authenticationRequiresChanges = false;

    private ConfigurationServiceStub confServicesClient = null;
    private String confEpr;
    private boolean confRequiresChanges = false;

    // Clients Configuration
    private long timeout = 120000;
    private boolean cacheAttachments = false;
    private File cacheDir = null;
    private String fileSizeThreshold = Integer.toString(5 * 1024 * 1024);
	
    private WSVerificaDocumentiStub verificaDocumentiClient;
    private String verificaDocumentiEpr;
	private boolean verificaDocumentiRequiresChanges;
	
	private WSTimbroDigitaleStub timbroDigitaleClient;
	private String timbroDigiraleEpr;
	private boolean timbroDigiraleRequiresChanges;
	
	private WSFascicolazioneStub fascicolazioneClient;
	private String fascicolazioneEpr;
	private boolean fascicolazioneRequiresChanges;
	
	private WSRegistrazioneStub registrazioneClient;
	private String registrazioneEpr;
	private boolean registrazioneRequiresChanges;
	
	private WSProtocollazioneStub protocollazioneClient;
	private String protocollazioneEpr;
	private boolean protocollazioneRequiresChanges;

//	private ProtocolloServiceStub agidProtocolloServiceClient;
	private String agidProtocolloServiceEpr;
	private boolean agidProtocolloServiceRequiresChanges;
	
	private TracerServiceStub tracerServiceClient;
	private String tracerServiceEpr;
	private boolean tracerServiceRequiresChanges;
	
    private ClientManager() {
        try {
            Properties props = ConfigurationUtils.loadProperties("docer_frontend.properties");

            long timeout = Long.parseLong(props.getProperty("timeout"));
            this.setTimeout(timeout);

            String attachmentDir = props.getProperty("attachments.dir");
            File f = new File(attachmentDir);
            if (!f.exists()) {
                f.mkdirs();
            }

            this.setCacheAttachments(true);
            this.setCacheDir(f);

            this.setConservazioneEpr(props.getProperty("conservazione.addr"));
            this.setConservazioneBatchEpr(props.getProperty("conservazione.batch.addr"));
            this.setDemoneEpr(props.getProperty("demone.addr"));
            this.setDocerServicesEpr(props.getProperty("remote.addr"));
            this.setAuthorizationEpr(props.getProperty("authorization.addr"));
            this.setAuthenticationEpr(props.getProperty("core.addr"));
            this.setConfEpr(props.getProperty("configuration.addr"));
            
            this.setProtocollazioneEpr(props.getProperty("protocollazione.epr"));
            this.setRegistrazioneEpr(props.getProperty("registrazione.epr"));
            this.setFascicolazioneEpr(props.getProperty("fascicolazione.epr"));
            this.setTimbroDigiraleEpr(props.getProperty("timbrodigitale.epr"));
            this.setVerificaDocumentiEpr(props.getProperty("verificadocumenti.epr"));
            
            this.setTracerServiceEpr(props.getProperty("tracer.addr"));
            
            MultiThreadedHttpConnectionManager connectionManager =
                    new MultiThreadedHttpConnectionManager();
            connectionManager.getParams().setDefaultMaxConnectionsPerHost(400);
            //connectionManager.getParams().setDefaultMaxConnectionsPerHost(20);

            httpClient = new HttpClient();
            httpClient.setHttpConnectionManager(connectionManager);
            

        } catch (IOException ex) {
            logger.error("Creazione ClientManager fallita", ex);
        }
    }

    public String getDocerServicesEpr(){
        return this.docerEpr;
    }
    
    public ClientManager setDocerServicesEpr(String epr) {
        this.docerEpr = epr;
        docerRequiresChanges = true;
        return this;
    }

    public DocerServicesStub getDocerServicesClient() throws AxisFault {
        if (docerServicesClient == null) {
            docerServicesClient = new DocerServicesStub(this.docerEpr);
        }

        if (docerRequiresChanges) {
            configure(docerServicesClient._getServiceClient(), docerEpr);
            docerRequiresChanges = false;
        }
        docerServicesClient._getServiceClient().cleanupTransport();
        return docerServicesClient;
    }

    private void configure(ServiceClient serviceClient, String epr) {
    	configure(serviceClient, epr, true);
    }
    
    private void configure(ServiceClient serviceClient, String epr, 
    		boolean mtom) {

    	Options options = serviceClient.getOptions();

        options.setTo(new EndpointReference(epr));
        options.setTimeOutInMilliSeconds(this.timeout);
        
        if (mtom) {
	        options.setProperty(Constants.Configuration.ENABLE_MTOM,
	                Constants.VALUE_TRUE); 
	        options.setProperty(Constants.Configuration.CACHE_ATTACHMENTS,
	                this.cacheAttachments ? Constants.VALUE_TRUE : Constants.VALUE_FALSE);
	        options.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR,
	                this.cacheDir.getAbsolutePath());
	        options.setProperty(Constants.Configuration.FILE_SIZE_THRESHOLD,
	                this.fileSizeThreshold);
        }

//        options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, Constants.VALUE_TRUE);
//        options.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, this.httpClient);
//        options.setProperty(HTTPConstants.AUTO_RELEASE_CONNECTION, Constants.VALUE_TRUE);

        /* Questo blocco e' stato commentato in quanto il ConfigurationContext 
         * sovrascrive le impostazioni dei servizi che usano il ClientManager.
         * Ad esempio il docersystem lavora in MTOM indipendentemente dalle
         * impostazioni locali
         */
        /*ConfigurationContext context = serviceClient.getServiceContext().getConfigurationContext();
        context.setProperty(Constants.Configuration.ENABLE_MTOM,
                Constants.VALUE_TRUE);
        context.setProperty(Constants.Configuration.CACHE_ATTACHMENTS,
                this.cacheAttachments ? Constants.VALUE_TRUE : Constants.VALUE_FALSE);
        context.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR,
                this.cacheDir.getAbsolutePath());
        context.setProperty(Constants.Configuration.FILE_SIZE_THRESHOLD,
                this.fileSizeThreshold);


        context.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, Constants.VALUE_TRUE);
        context.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, this.httpClient);
        context.setProperty(HTTPConstants.AUTO_RELEASE_CONNECTION, Constants.VALUE_TRUE);*/
    }

    public ClientManager setConservazioneEpr(String epr) {
        this.conservazioneEpr = epr;
        conservazioneRequiresChanges = true;
        return this;
    }

    public WSConservazioneStub getConservazioneClient() throws AxisFault {
        if (conservazioneClient == null) {
            conservazioneClient = new WSConservazioneStub(conservazioneEpr);
        }
        if (conservazioneRequiresChanges) {
            configure(conservazioneClient._getServiceClient(), conservazioneEpr);

            conservazioneRequiresChanges = false;
        }
        conservazioneClient._getServiceClient().cleanupTransport();
        return conservazioneClient;
    }

    public ClientManager setConservazioneBatchEpr(String epr) {
        this.conservazioneBatchEpr = epr;
        conservazioneBatchRequiresChanges = true;
        return this;
    }

    public WSConservazioneBatchStub getConservazioneBatchClient() throws AxisFault {
        if (conservazioneBatchClient == null) {
            conservazioneBatchClient = new WSConservazioneBatchStub(conservazioneBatchEpr);
        }
        if (conservazioneBatchRequiresChanges) {
            configure(conservazioneBatchClient._getServiceClient(), conservazioneBatchEpr);

            conservazioneBatchRequiresChanges = false;
        }
        conservazioneBatchClient._getServiceClient().cleanupTransport();
        return conservazioneBatchClient;
    }

    public ClientManager setDemoneEpr(String epr) {
        this.demoneEpr = epr;
        demoneRequiresChanges = true;
        return this;
    }

    public String getDemoneEpr() {
        return this.demoneEpr;
    }

    public WSDemoneStub getDemoneClient() throws AxisFault {
        if (demoneClient == null) {
            demoneClient = new WSDemoneStub(demoneEpr);
        }
        if (demoneRequiresChanges) {
            configure(demoneClient._getServiceClient(), demoneEpr);

            demoneRequiresChanges = false;
        }

        demoneClient._getServiceClient().cleanupTransport();
        return demoneClient;
    }

    public ClientManager setAuthorizationEpr(String epr) {
        this.authorizationEpr = epr;
        authorizationRequiresChanges = true;
        return this;
    }

    public AuthorizationServiceStub getAuthorizationClient() throws AxisFault {
        if (authorizationServicesClient == null) {
        	authorizationServicesClient = new AuthorizationServiceStub(authorizationEpr);
        } else if (authorizationRequiresChanges) {
            configure(authorizationServicesClient._getServiceClient(), 
            		authorizationEpr, false);

            authorizationRequiresChanges = false;
        }

        authorizationServicesClient._getServiceClient().cleanupTransport();
        return authorizationServicesClient;
    }
    
    public ClientManager setAuthenticationEpr(String epr) {
        this.authenticationEpr = epr;
        authenticationRequiresChanges = true;
        return this;
    }

    public AuthenticationServiceStub getAuthenticationClient() throws AxisFault {
        if (authenticationServicesClient == null) {
        	authenticationServicesClient = new AuthenticationServiceStub(authenticationEpr);
        } else if (authenticationRequiresChanges) {
            configure(authenticationServicesClient._getServiceClient(), 
            		authenticationEpr, false);

            authenticationRequiresChanges = false;
        }

        authenticationServicesClient._getServiceClient().cleanupTransport();
        return authenticationServicesClient;
    }

    public ClientManager setConfEpr(String epr) {
        this.confEpr = epr;
        confRequiresChanges = true;
        return this;
    }

    public ConfigurationServiceStub getConfClient() throws AxisFault {
        if (confServicesClient == null) {
            confServicesClient = new ConfigurationServiceStub(confEpr);
        } else if (confRequiresChanges) {
            configure(confServicesClient._getServiceClient(), 
            		confEpr, false);

            confRequiresChanges = false;
        }

        confServicesClient._getServiceClient().cleanupTransport();
        return confServicesClient;
    }

    private ClientManager setTracerServiceEpr(String epr) {
    	this.tracerServiceEpr = epr;
        tracerServiceRequiresChanges = true;
        return this;
	}
    
    public TracerServiceStub getTracerServiceClient() throws AxisFault {
        if (tracerServiceClient == null) {
        	tracerServiceClient = new TracerServiceStub(tracerServiceEpr);
        } else if (tracerServiceRequiresChanges) {
            configure(tracerServiceClient._getServiceClient(), tracerServiceEpr);

            tracerServiceRequiresChanges = false;
        }

        tracerServiceClient._getServiceClient().cleanupTransport();
        return tracerServiceClient;
    }
    
    private ClientManager setVerificaDocumentiEpr(String epr) {
    	this.verificaDocumentiEpr = epr;
        verificaDocumentiRequiresChanges = true;
        return this;
	}
    
    public WSVerificaDocumentiStub getVerificaDocumentiClient() throws AxisFault {
        if (verificaDocumentiClient == null) {
        	verificaDocumentiClient = new WSVerificaDocumentiStub(verificaDocumentiEpr);
        } else if (verificaDocumentiRequiresChanges) {
            configure(verificaDocumentiClient._getServiceClient(), verificaDocumentiEpr);

            verificaDocumentiRequiresChanges = false;
        }

        verificaDocumentiClient._getServiceClient().cleanupTransport();
        return verificaDocumentiClient;
    }

	private ClientManager setTimbroDigiraleEpr(String epr) {
		this.timbroDigiraleEpr = epr;
		timbroDigiraleRequiresChanges = true;
        return this;
	}
	
	public WSTimbroDigitaleStub getTimbroDigitaleClient() throws AxisFault {
        if (timbroDigitaleClient == null) {
        	timbroDigitaleClient = new WSTimbroDigitaleStub(timbroDigiraleEpr);
        } else if (timbroDigiraleRequiresChanges) {
            configure(timbroDigitaleClient._getServiceClient(), timbroDigiraleEpr);

            timbroDigiraleRequiresChanges = false;
        }

        timbroDigitaleClient._getServiceClient().cleanupTransport();
        return timbroDigitaleClient;
    }

	private ClientManager setFascicolazioneEpr(String epr) {
		this.fascicolazioneEpr = epr;
		fascicolazioneRequiresChanges = true;
        return this;
	}
	
	public WSFascicolazioneStub getFascicolazioneClient() throws AxisFault {
        if (fascicolazioneClient == null) {
        	fascicolazioneClient = new WSFascicolazioneStub(fascicolazioneEpr);
        } else if (fascicolazioneRequiresChanges) {
            configure(fascicolazioneClient._getServiceClient(), fascicolazioneEpr);

            fascicolazioneRequiresChanges = false;
        }

        fascicolazioneClient._getServiceClient().cleanupTransport();
        return fascicolazioneClient;
    }

	public ClientManager setAGIDProtocolloServiceEpr(String epr) {
		this.agidProtocolloServiceEpr = epr;
		agidProtocolloServiceRequiresChanges = true;
        return this;
	}
	
//	public ProtocolloServiceStub getAGIDProtocolloServiceClient() throws AxisFault {
//        if (agidProtocolloServiceClient == null) {
//        	agidProtocolloServiceClient = new ProtocolloServiceStub(agidProtocolloServiceEpr);
//        } else if (agidProtocolloServiceRequiresChanges) {
//            configure(agidProtocolloServiceClient._getServiceClient(), agidProtocolloServiceEpr);
//
//            agidProtocolloServiceRequiresChanges = false;
//        }
//
//        agidProtocolloServiceClient. _getServiceClient().cleanupTransport();
//        return agidProtocolloServiceClient;
//    }
	
	private ClientManager setRegistrazioneEpr(String epr) {
		this.registrazioneEpr = epr;
        registrazioneRequiresChanges = true;
        return this;
	}
	
	public WSRegistrazioneStub getRegistrazioneClient() throws AxisFault {
        if (registrazioneClient == null) {
        	registrazioneClient = new WSRegistrazioneStub(registrazioneEpr);
        } else if (registrazioneRequiresChanges) {
            configure(registrazioneClient._getServiceClient(), registrazioneEpr);

            registrazioneRequiresChanges = false;
        }

        return registrazioneClient;
    }

	private ClientManager setProtocollazioneEpr(String epr) {
		this.protocollazioneEpr = epr;
        protocollazioneRequiresChanges = true;
        return this;
	}
	
	public WSProtocollazioneStub getProtocollazioneClient() throws AxisFault {
        if (protocollazioneClient == null) {
        	protocollazioneClient = new WSProtocollazioneStub(protocollazioneEpr);
        } else if (protocollazioneRequiresChanges) {
            configure(protocollazioneClient._getServiceClient(), protocollazioneEpr);

            protocollazioneRequiresChanges = false;
        }

        return protocollazioneClient;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        demoneRequiresChanges = true;
        conservazioneRequiresChanges = true;
        docerRequiresChanges = true;
        this.timeout = timeout;
    }

    public boolean isCacheAttachments() {
        return cacheAttachments;
    }

    public void setCacheAttachments(boolean cacheAttachments) {
        demoneRequiresChanges = true;
        conservazioneRequiresChanges = true;
        docerRequiresChanges = true;
        this.cacheAttachments = cacheAttachments;
    }

    public File getCacheDir() {
        return cacheDir;
    }

    public void setCacheDir(File cacheDir) {
        demoneRequiresChanges = true;
        conservazioneRequiresChanges = true;
        docerRequiresChanges = true;
        this.cacheDir = cacheDir;
    }

    public long getFileSizeThreshold() {
        return Long.parseLong(fileSizeThreshold);
    }

    public void setFileSizeThreshold(long fileSizeThreshold) {
        demoneRequiresChanges = true;
        conservazioneRequiresChanges = true;
        docerRequiresChanges = true;
        this.fileSizeThreshold = Long.toString(fileSizeThreshold);
    }
}
