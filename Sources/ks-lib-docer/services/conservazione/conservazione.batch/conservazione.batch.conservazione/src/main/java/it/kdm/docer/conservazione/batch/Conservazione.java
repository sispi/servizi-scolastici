package it.kdm.docer.conservazione.batch;

import it.kdm.docer.clients.AuthenticationServiceStub;
import it.kdm.docer.clients.AuthenticationServiceStub.Login;
import it.kdm.docer.clients.WSConservazioneBatchStub;
import it.kdm.docer.clients.WSConservazioneBatchStub.AggiungiDocumento;
import it.kdm.docer.clients.WSConservazioneBatchStub.AggiungiDocumentoResponse;
import it.kdm.docer.clients.WSConservazioneBatchStub.ModificaMetadati;
import it.kdm.docer.clients.WSConservazioneBatchStub.ModificaMetadatiResponse;
import it.kdm.docer.clients.WSConservazioneBatchStub.Versamento;
import it.kdm.docer.clients.WSConservazioneBatchStub.VersamentoResponse;
import it.kdm.docer.conservazione.Utils;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.builder.unknowncontent.InputStreamDataSource;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.slf4j.Logger;


public class Conservazione {
	
	Logger log = org.slf4j.LoggerFactory.getLogger(Conservazione.class);
	
	private final static long DEFAULT_TIMEOUT = 180;
	private final static TimeUnit DEFAULT_TIMEOUT_GRANULARITY = TimeUnit.SECONDS;
	
	public enum TipoConservazione {
		SOSTITUTIVA,
		FISCALE
	}
	
	private static WSConservazioneBatchStub conservazioneClient;
	private AuthenticationServiceStub docerClient;


	public Conservazione() throws Exception {
		if(conservazioneClient == null) {
			conservazioneClient = new WSConservazioneBatchStub();
		}
		this.docerClient = new AuthenticationServiceStub();
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
	}
	
	public long getTimeout(TimeUnit granularity) {
		return granularity.convert(
				conservazioneClient._getServiceClient().getOptions().getTimeOutInMilliSeconds(),
				TimeUnit.MILLISECONDS);
	}

	public void setTimeout(long timeout, TimeUnit granularity) {
		Options options = conservazioneClient._getServiceClient().getOptions();
		options.setTimeOutInMilliSeconds(
				TimeUnit.MILLISECONDS.convert(timeout, granularity));
	}
	
	public String login(String username, String password, String library) throws Exception {
		Login req = new Login();
		req.setUsername(username);
		req.setPassword(password);
		req.setCodiceEnte(library);
		req.setApplication("conservazione");
		return docerClient.login(req).get_return();
	}
	
	public ConservazioneResult versamento(String token, String docId, 
			String documentoPrincipale, String allegati, String files, 
			String tipoConservazione, String tipoDocumento, 
			String applicazioneChiamante, boolean forzaCollegamento, 
			boolean forzaAccettazione, boolean forzaConservazione,
										  String ente, String aoo)
					throws Exception {
		
		Versamento req = new Versamento();
		req.setToken(token);
		req.setXmlDocumentoPrincipale(this.getDataHandler(documentoPrincipale));
		req.setXmlAllegati(this.getDataHandler(allegati));
		
		String[] uriArray = files.split("\\|");
		
		req.setFilesURI(uriArray);
		req.setTipoDocumento(tipoDocumento);
		req.setApplicazioneChiamante(applicazioneChiamante);
		req.setEnte(ente);
		req.setAoo(aoo);
		req.setTipoConservazione(tipoConservazione);
		
		req.setForzaAccettazione(forzaAccettazione);
		req.setForzaCollegamento(forzaCollegamento);
		req.setForzaConservazione(forzaConservazione);
		
		VersamentoResponse resp = conservazioneClient.versamento(req);
		
		return new ConservazioneResult(resp.get_return());
	}
	
	public ConservazioneResult modificaMetadati(String token, String docId, 
			String documentoPrincipale,  
			String tipoConservazione, String tipoDocumento, 
												String applicazioneChiamante, String ente, String aoo)
					throws Exception {
		
		ModificaMetadati req = new ModificaMetadati();
		req.setToken(token);
		req.setXmlDocumentoPrincipale(this.getDataHandler(documentoPrincipale));
		
		req.setTipoDocumento(tipoDocumento);
		req.setApplicazioneChiamante(applicazioneChiamante);
		req.setEnte(ente);
		req.setAoo(aoo);
		req.setTipoConservazione(tipoConservazione);
	
		ModificaMetadatiResponse resp = conservazioneClient.modificaMetadati(req);
	
		return new ConservazioneResult(resp.get_return());
	}
	
	public ConservazioneResult aggiungiDocumento(String token, String docId, 
			String documentoPrincipale, String allegati, String file,
			String tipoConservazione, String tipoDocumento, 
			String applicazioneChiamante, boolean forzaCollegamento, 
			boolean forzaAccettazione, boolean forzaConservazione, String ente, String aoo)
					throws Exception {
		
		AggiungiDocumento req = new AggiungiDocumento();
		req.setToken(token);
		req.setXmlDocumentoPrincipale(this.getDataHandler(documentoPrincipale));
		req.setXmlAllegati(this.getDataHandler(allegati));
		req.setFileURI(file);
		
		req.setApplicazioneChiamante(applicazioneChiamante);
		req.setEnte(ente);
		req.setAoo(aoo);
		req.setTipoDocumento(tipoDocumento);
		req.setTipoConservazione(tipoConservazione);
		
		req.setForzaAccettazione(forzaAccettazione);
		req.setForzaCollegamento(forzaCollegamento);
		req.setForzaConservazione(forzaConservazione);
		
		AggiungiDocumentoResponse resp = conservazioneClient.aggiungiDocumento(req);
	
		return new ConservazioneResult(resp.get_return());
	}

	private DataHandler getDataHandler(String xml) throws Exception {
		
		String charsetName = Utils.findXmlCharset(xml);
		
		DataSource ds = new InputStreamDataSource(new ByteArrayInputStream(xml.getBytes(charsetName)));
		return new DataHandler(ds);
	}
	
}
