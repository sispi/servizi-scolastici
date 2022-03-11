/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.ccd.lib;

import it.eng.parer.ws.versamento.dto.FileBinario;
import it.eng.parer.ws.xml.versReq.UnitaDocAggAllegati;
import it.eng.parer.ws.xml.versReq.UnitaDocumentaria;
import it.eng.parer.ws.xml.versResp.EsitoGenerale;
import it.eng.parer.ws.xml.versResp.EsitoVersAggAllegati;
import it.eng.parer.ws.xml.versResp.EsitoVersamento;
import it.eng.parer.ws.xml.versResp.types.ECEsitoExtType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Vector;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

/**
 *
 * @author Francesco Fioravanti
 */
public class ParerLib implements IParerLib {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ParerLib.class);

    private String sacerHost;
    private String sacerVersion;
    private boolean deleteAfterSend;
    private boolean useHttps;

	private String urlTemplate;

    public String getSacerHost() {
		return sacerHost;
	}

	public void setSacerHost(String sacerHost) {
		this.sacerHost = sacerHost;
		urlTemplate = sacerHost;
		if (!sacerHost.endsWith("/")) {
			urlTemplate += "/";
		}
		urlTemplate += "%s";
	}

	public String getSacerVersion() {
		return sacerVersion;
	}

	public void setSacerVersion(String sacerVersion) {
		this.sacerVersion = sacerVersion;
	}

	public boolean isDeleteAfterSend() {
		return deleteAfterSend;
	}

	public void setDeleteAfterSend(boolean deleteAfterSend) {
		this.deleteAfterSend = deleteAfterSend;
	}

	public boolean isUseHttps() {
		return useHttps;
	}

	public void setUseHttps(boolean useHttps) {
		this.useHttps = useHttps;
	}

	public EsitoVersamento versamento(String loginname, String password, UnitaDocumentaria versamento, Vector<FileBinario> fileBinari) throws Exception {
        EsitoVersamento esito;

        //*******************************************************************************************
        // SWITCH MODALITA' MOCK PARER
        //*******************************************************************************************
        if (this.sacerHost.toUpperCase().contains("MOCK")) {
            esito = new EsitoVersamento();
            Reader reader = null;
            try {
                InputStream in = this.getClass().getResourceAsStream("/response.xml");
                reader = new InputStreamReader(in);
                esito = EsitoVersamento.unmarshal(reader);
            } catch (Exception exo) {
                log.error(exo.getMessage(), exo);
            } finally {
                IOUtils.closeQuietly(reader);
            }

//            esito.setEsitoGenerale(new EsitoGenerale());
//            esito.getEsitoGenerale().setCodiceErrore("777");
//            esito.getEsitoGenerale().setCodiceEsito(ECEsitoExtType.NEGATIVO);
//            esito.getEsitoGenerale().setMessaggioErrore(ex.getMessage());
            esito.setDataVersamento(new Date());
            esito.setVersione(sacerVersion);
            esito.setVersioneXMLChiamata(sacerVersion);

            return esito;
        }

        String response = chiamaWS("VersamentoSync", loginname, password, versamento, fileBinari);

        
        try {
        	esito = EsitoVersamento.unmarshal(new StringReader(response));
        	
        	if (esito.getEsitoGenerale().getCodiceEsito() != ECEsitoExtType.NEGATIVO) {
                if (deleteAfterSend) {
                    for (FileBinario tmpFileBinario : fileBinari) {
                        tmpFileBinario.getFileSuDisco().delete();
                    }
                }
        	}
        } catch (Exception ex) {
        	esito = new EsitoVersamento();
        	Reader reader = null;
         	try {
             //istanzia esito nuovo
             //crea un codice errore 777 e scrivi nel message l'errore interno
         		
 	            InputStream in = this.getClass().getResourceAsStream("/response.xml");
 	            reader = new InputStreamReader(in);
 	            esito = EsitoVersamento.unmarshal(reader);
 	            
         	} catch (Exception exo) {
         		log.error(exo.getMessage(), exo);
         	} finally {
         		IOUtils.closeQuietly(reader);
         	}
         	
         	esito.setEsitoGenerale(new EsitoGenerale());
 			esito.getEsitoGenerale().setCodiceErrore("777");
 			esito.getEsitoGenerale().setCodiceEsito(ECEsitoExtType.NEGATIVO);
 			esito.getEsitoGenerale().setMessaggioErrore(ex.getMessage());
 			esito.setDataVersamento(new Date());
 			esito.setVersione(sacerVersion);
 			esito.setVersioneXMLChiamata(sacerVersion);

        }
         
         return esito;
    }
	
	public EsitoVersAggAllegati modificaMetadati(String loginname, String password, UnitaDocAggAllegati versamento) throws Exception {
        String response = chiamaWS("AggiuntaAllegatiSync", loginname, password, versamento, new Vector<FileBinario>());
        return EsitoVersAggAllegati.unmarshal(new StringReader(response));
    }
	
	public EsitoVersAggAllegati aggiungiDocumento(String loginname, String password, UnitaDocAggAllegati versamento, FileBinario fileBinario) throws Exception {
		Vector<FileBinario> fileBinari = new Vector<FileBinario>();
		fileBinari.add(fileBinario);
        String response = chiamaWS("AggiuntaAllegatiSync", loginname, password, versamento, fileBinari);
        return EsitoVersAggAllegati.unmarshal(new StringReader(response));
    }

    private String chiamaWS(String method, String loginname, String password, Serializable payload, Vector<FileBinario> fileBinari) throws Exception {
        StringWriter tmpStringWriter = new StringWriter();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = null;
        FileBody tmpFileBody = null;
        MultipartEntity reqEntity = null;
        HttpResponse response = null;
        String risposta = null;

        //imposta la versione
    	if (payload instanceof UnitaDocumentaria) {
        		((UnitaDocumentaria)payload).getIntestazione().setVersione(sacerVersion);
    	} else if (payload instanceof UnitaDocAggAllegati) {
    		((UnitaDocAggAllegati)payload).getIntestazione().setVersione(sacerVersion);
    	} else {
    		throw new Exception("La struttura-dati versamento è errata, impossibile impostare la versione");
    	}

        if (useHttps) {
            //se devo usare HTTPS...
            // creo un array di TrustManager per considerare tutti i certificati server validi		
            X509TrustManager tm = new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            };

            try {
                // Creo il contesto SSL utilizzando i trust manager creati			
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[]{tm}, null);

                // Creo la connessione https
                SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                ClientConnectionManager ccm = httpclient.getConnectionManager();
                SchemeRegistry sr = ccm.getSchemeRegistry();
                sr.register(new Scheme("https", 443, ssf));
                httpclient = new DefaultHttpClient(ccm, httpclient.getParams());
            } catch (NoSuchAlgorithmException e) {
            	String message = "Errore interno nella preparazione della chiamata HTTPS";
                log.error(message, e);
                throw new Exception(message, e);
            } catch (KeyManagementException e) {
            	String message = "Errore interno nella preparazione della chiamata HTTPS";
                log.error(message, e);
                throw new Exception(message, e);
            }
        }

        httppost = new HttpPost(String.format(urlTemplate, method));
        reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,  Charset.forName("ISO-8859-1"));
        
        try {
            //serializzazione del versamento su una stringa temporanea
        	Marshaller.marshal(payload, tmpStringWriter);
            
            // stampo la richiesta
            log.info(tmpStringWriter.toString());

        } catch (MarshalException ex) {
        	String message = "Errore interno nella serializzazione dei metadati del versamento";
            log.error(message, ex);
            throw new Exception(message, ex);
        } catch (ValidationException ex) {
            String message = "Errore interno (validazione) nella serializzazione dei metadati del versamento";
            log.error(message, ex);
            throw new Exception(message, ex);
        }

        try {
            //creazione della chiamata: aggiunta di utente, password, versione, metadati
            reqEntity.addPart("VERSIONE", new StringBody(sacerVersion,
                           "text/xml", Charset.forName("ISO-8859-1")));
            reqEntity.addPart("LOGINNAME", new StringBody(loginname,
                           "text/xml", Charset.forName("ISO-8859-1")));
            reqEntity.addPart("PASSWORD", new StringBody(password,
                           "text/xml", Charset.forName("ISO-8859-1")));
            reqEntity.addPart("XMLSIP", new StringBody(tmpStringWriter.toString(),
                           "text/xml", Charset.forName("ISO-8859-1")));
            //ciclo sui file
            for (FileBinario tmpFileBinario : fileBinari) {
                tmpFileBody = new FileBody(tmpFileBinario.getFileSuDisco(), "binary/octet-stream");
                reqEntity.addPart(tmpFileBinario.getId(), tmpFileBody);
            }

            //preparazione request
            httppost.setEntity(reqEntity);
        } catch (UnsupportedEncodingException ex) {
            String message = "Errore interno nella preparazione della chiamata al WS di versamento.";
            log.error(message, ex);
            throw new Exception(message, ex);
        }

        //chiamata del WS e recupero del relativo esito
        try {
            response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                risposta = EntityUtils.toString(resEntity);
            } else {
            	String message = "Errore: il WS di versamento non ha restituito dati di esito.";
            	log.error(message);
            	throw new Exception(message);
            }
        } catch (UnsupportedEncodingException ex) {
        	String message = "Errore nella chiamata al WS di versamento";
        	log.error(message, ex);
        	throw new Exception(message, ex);
        } catch (ClientProtocolException ex) {
        	String message = "Errore nella chiamata al WS di versamento";
        	log.error(message, ex);
        	throw new Exception(message, ex);
        } catch (ParseException ex) {
        	String message = "Errore nella chiamata al WS di versamento";
        	log.error(message, ex);
        	throw new Exception(message, ex);
        } catch (IOException ex) {
        	String message = "Errore nella chiamata al WS di versamento";
        	log.error(message, ex);
        	throw new Exception(message, ex);
        }
         
        return risposta;
    }

}