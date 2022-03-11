package docer.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.exception.DocerApiException;
import it.kdm.doctoolkit.model.*;
import it.kdm.doctoolkit.services.DocerService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StreamUtils;


public class Invia extends DocerAction {

	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {
		String token = null;
		
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> destinatario = null;
		Map<String, String> mittente = null;
		Map<String, ?> documentoMap = null;
		String channel = null;
        String subject = null;
		String messaggio ="";
	
		destinatario = inputs.containsKey("destinatario")?(Map<String, String>) inputs.get("destinatario"):null;
		mittente = inputs.containsKey("mittente")?(Map<String, String>) inputs.get("mittente"):null;
		documentoMap = inputs.containsKey("document")?(Map<String, ?>)inputs.get("document"):null;
		messaggio = (String)inputs.get("message");
		channel = (String)inputs.get("channel");
        subject = (String)inputs.get("subject");
        
        if (subject==null || subject.equals("")){
        	subject="(nessun oggetto)";
        }
        
        if(messaggio==null)
        	messaggio="";
        
        
		//messaggio = inputs.containsKey("message")?(String) inputs.get("message"):null;
		
//		if(mittente == null)throw new ActionRuntimeException("PIN 'mittente' non trovato nei parametri di input!");
		if(destinatario == null)throw new ActionRuntimeException("PIN 'destinatario' non trovato nei parametri di input!");
		
//		if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
		
//		if(channel == null)throw new ActionRuntimeException("PIN 'channel' non trovato nei parametri di input!");
		//if(messaggio == null)throw new ActionRuntimeException("PIN 'messaggio' non trovato nei parametri di input!");
		
		Documento documento = null;
		
		if(documentoMap != null){
			documento = new Documento();
			documentoMap.remove("TaskName");
			documentoMap.remove("filePath");
			documentoMap.remove("GroupId");
			documentoMap.remove("comment");
			documentoMap.remove("outcome");
			documentoMap.remove("NodeName");
			documentoMap.remove("ActorId");
			documentoMap.remove("@structure");
		
			if(documentoMap.containsKey("userToken")){
				documentoMap.remove("userToken");
			}
			
			documento.properties = (HashMap<String, String>)documentoMap;
			documento.properties = (HashMap<String, String>)documentoMap;
		}
		try {
			token = getToken(inputs);
	
				documento = viaMail(inputs,documento,destinatario,mittente,subject,channel, messaggio, token);
				if(documento!=null){
					result.put("document", documentToHashmap(documento));
				}
				result.put("userToken", token);
			

			
		} catch (Exception e) {
			throw new ActionRuntimeException(e);
		}
		
		return result;
	}
	
	
	@SuppressWarnings("rawtypes")
	private Documento viaMail(Map<String, Object> inputs, Documento documento, Map<String, String> destinatario, Map<String, String> mittente, String subject, String channel, String messaggio, String token) throws DocerApiException, FileNotFoundException,
            Exception, MessagingException{

		Documento principale = null;

		List<String> fileAllegati = new ArrayList<>();
		
		if(documento!=null){
			Documento doc =  DocerService.recuperaProfiloDocumento(token, documento.getDocNum());
			DocerFile docerfile = DocerService.downloadDocument(token, doc.getDocNum());
			
			File file = new File(doc.getDocName());
			OutputStream out = new FileOutputStream(file);
			IOUtils.copy(docerfile.getContent().getInputStream(), out);
			out.close();
			fileAllegati.add(getUrlFromFile(file));
			List<Documento> docAllegati = DocerService.getRiferimenti(token, doc.getDocNum());
			List<String> attachments = new ArrayList<String>();
			attachments.add(doc.getDocName());
			for (Documento allegato : docAllegati) {
				DocerFile docerfileAllegato = DocerService.downloadDocument(token, allegato.getDocNum());
				attachments.add(allegato.getDocName());
				File fileAllegato = new File(allegato.getDocName());
				out = new FileOutputStream(fileAllegato);
				IOUtils.copy(docerfileAllegato.getContent().getInputStream(), out);
				out.close();
				fileAllegati.add(fileAllegato.getAbsolutePath());
			}
			principale = doc;
		}

		String url = System.getProperty("api.bpm.baseUrl","http://localhost") + "/notifications";

		HttpPost method = new HttpPost(url);

		CloseableHttpClient httpClient = HttpClients.custom().build();

		String json = "{\n" +
				"  \"operationId\": \"%s\",\n" +
				"  \"email\": true,\n" +
				"%s" +
				"  \"subject\": \"%s\",\n" +
				"  \"body\": \"%s\",\n" +
				"  \"toRecipients\": [\"%s\"]\n" +
				"%s" +
				"}";

		String to = destinatario.get("IndirizzoTelematico");

		String from = null;

		if (mittente!=null)
			from = mittente.get("IndirizzoTelematico");

		String deploymentId = getDeplomentId();

		//ENTE_TETS.AOO_TEST:My_Process:1.0

		String processName_ = deploymentId.split(":")[1];

		String conf = System.getProperty("email."+getAoo()+"."+processName_,System.getProperty("email."+getAoo()));

		if (Strings.isNullOrEmpty(from) && !Strings.isNullOrEmpty(conf)){
			from = conf;
		}

		if (Strings.isNullOrEmpty(from))
			from = "";
		else
			from = "\"from\" : \"" + from + "\",";

		String operationId = createWorkitemKey(getProcessInstanceId(),getWorkItemId());

		String attachments = "";
		if (fileAllegati.size()>0)
			attachments = ",\"attachments\": [\""+StringUtils.join(fileAllegati,"\",\"")+"\"]\n";

		json = String.format(json,operationId,from,subject,messaggio,to,attachments);

		StringEntity entity = new StringEntity(json);

		method.setHeader("Authorization",getJWTToken(inputs));
		method.setHeader("Accept", "application/json");
		method.setHeader("Content-type", "application/json");
		method.setEntity(entity);

		HttpResponse response = httpClient.execute(method);

		if (response.getStatusLine().getStatusCode() != 201 && response.getStatusLine().getStatusCode() != 200) {

			String message = "Failed";
			try{
				String jsRsp = StreamUtils.copyToString(response.getEntity().getContent(), Charset.defaultCharset());


				Map map = new ObjectMapper().readValue(jsRsp,Map.class);
				message = (String) map.getOrDefault("message",message);

			} catch (Exception e) {
				e.printStackTrace();
			}

			throw new ActionRuntimeException(message);
		}

		return principale;
	}
	
	private List<Corrispondente> prepareList(HashMap<String, String> lista){
		
		List<Corrispondente> result = new ArrayList<Corrispondente>();
		
		if("uo".equals(lista.get("@structure"))){
			Amministrazione dest = new Amministrazione();
			dest.setIndirizzoTelematico(lista.get("IndirizzoTelematico"));
			dest.setCodiceAmministrazione("");
			dest.setDenominazione("");
			UO unitaOrganizzativa = new UO();
			unitaOrganizzativa.setCodiceUnitaOrganizzativa(lista.get("COD_UO"));
			dest.setUnitaOrganizzativa(unitaOrganizzativa);
			result.add(dest);
		}else if("role".equals(lista.get("@structure"))){
			PersonaFisica dest = new PersonaFisica();
			dest.setIndirizzoTelematico(lista.get("IndirizzoTelematico"));
			result.add(dest);
		}else if("line".equals(lista.get("@structure"))){
			String type = lista.get("@type");
			if("Amministrazione".equals(type)){
				Amministrazione dest = new Amministrazione();
				dest.setIndirizzoTelematico(lista.get("IndirizzoTelematico"));
				dest.setCodiceAmministrazione("");
				dest.setDenominazione("");
				UO unitaOrganizzativa = new UO();
				unitaOrganizzativa.setCodiceUnitaOrganizzativa(lista.get("COD_UO"));
				dest.setUnitaOrganizzativa(unitaOrganizzativa);
				result.add(dest);
			}else if("PersonaFisica".equals(type)){
				PersonaFisica dest = new PersonaFisica();
				dest.setIndirizzoTelematico(lista.get("IndirizzoTelematico"));
				result.add(dest);
			}else if("PersonaGiuridica".equals(type)){
				PersonaGiuridica dest = new PersonaGiuridica();
				dest.setIndirizzoTelematico(lista.get("IndirizzoTelematico"));
				result.add(dest);	
			}
		}
		return result;
	}

}
