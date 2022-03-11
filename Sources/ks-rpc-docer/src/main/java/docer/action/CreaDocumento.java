package docer.action;

import com.google.common.base.Strings;
import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.*;
import it.kdm.doctoolkit.services.DocerService;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreaDocumento extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.CreaDocumento.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {

		log.info("init method execute");
		String token = null;
		Map<String, Object> result = new HashMap<String, Object>();
		HashMap<String, Object> documentoMap = null;
		String fileURL = null;
        String primaryFile = null;
        String fileName = null;

		Documento documento = new Documento();
		documentoMap = inputs.containsKey("document")?(HashMap<String, Object>)inputs.get("document"):null;
		fileURL = inputs.containsKey("document")?(String)documentoMap.get("filePath"):null;

        primaryFile = inputs.containsKey("file")?(String)inputs.get("file"):null;

		Boolean verificaFirma  = false;
		Boolean estraiFirmatari = false;
		String verificaFirmaInput = inputs.containsKey("VERIFICA_FIRMA")?(String)inputs.get("VERIFICA_FIRMA"):"FALSE";

		if(inputs.containsKey("ESTRAI_FIRMATARI") && !Strings.isNullOrEmpty((String)inputs.get("ESTRAI_FIRMATARI"))){
			estraiFirmatari = "TRUE".equalsIgnoreCase((String)inputs.get("ESTRAI_FIRMATARI"));
		}else{
			try {
				estraiFirmatari = Boolean.parseBoolean(System.getProperty("docer.create.firmatari","false"));
			}catch(Exception e){
				estraiFirmatari = false;
				log.warn("Error in read ESTRAI_FIRMATARI from  bpm-server.properties");
			}
		}

		if(!Strings.isNullOrEmpty(verificaFirmaInput) && "TRUE".equalsIgnoreCase(verificaFirmaInput)){
			verificaFirma = true;
		}

        if (primaryFile!=null)
            fileURL=primaryFile;

        if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
        
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
        
        try{
        	documento.fromFlowObject(documentoMap);
        }catch(Exception e){
        	throw new ActionRuntimeException(e);
        	
        }
        


//        if(fileURL == null && primaryFile==null)throw new ActionRuntimeException("Puntamento al file non trovato nei parametri di input!");

//        List <HashMap> mitt= (List<HashMap>)documentoMap.get("Mittenti");
//        List <Corrispondente> mittentiLista = new ArrayList<Corrispondente>();
//       if(mitt!=null && mitt.size()>0){
//    		documentoMap.remove("Mittenti");
//        for (HashMap temp:mitt){
//        	
//        	if(temp.get("@type").equals("PersonaFisica")){
//	        	PersonaFisica c = new PersonaFisica();
//	        	c.properties = (HashMap<String,String>)temp.clone();
//	        	mittentiLista.add(c);
//        	}else if(temp.get("@type").equals("Amministrazione")){
//            	Amministrazione c = new Amministrazione();
//            	c.properties = (HashMap<String,String>)temp.clone();
//            	mittentiLista.add(c);
//            }else if(temp.get("@type").equals("PersonaGiuridica")){
//            	PersonaGiuridica c = new PersonaGiuridica();
//            	c.properties = (HashMap<String,String>)temp.clone();
//            	mittentiLista.add(c);
//            }
//        }
//       }
//       List <HashMap> dest= (List<HashMap>)documentoMap.get("Destinatari");
//       List <Corrispondente> destinatariLista = new ArrayList<Corrispondente>();
//       if(dest!=null && dest.size()>0){
//   		documentoMap.remove("Destinatari");
//        for (HashMap temp:dest){
//        	
//        	if(temp.get("@type").equals("PersonaFisica")){
//	        	PersonaFisica c = new PersonaFisica();
//	        	c.properties = (HashMap<String,String>)temp.clone();
//	        	destinatariLista.add(c);
//        	}else if(temp.get("@type").equals("Amministrazione")){
//            	Amministrazione c = new Amministrazione();
//            	c.properties = (HashMap<String,String>)temp.clone();
//            	destinatariLista.add(c);
//            }else if(temp.get("@type").equals("PersonaGiuridica")){
//            	PersonaGiuridica c = new PersonaGiuridica();
//            	c.properties = (HashMap<String,String>)temp.clone();
//            	destinatariLista.add(c);
//            }
//        }
//       }
		
		
		String docName = "";
		if (documento.getDocName()==null || documento.getDocName().equals("")){
			if(fileURL!= null && !fileURL.equals("")) {
				try {
					docName = FilenameUtils.getName(URLDecoder.decode(fileURL,"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					throw new ActionRuntimeException(e.getMessage());
				}
				//docName = FilenameUtils.getBaseName(fileURL)+"."+FilenameUtils.getExtension(fileURL);
				documento.setDocName(docName);
			} else {
				if(documento.getDocName()==null ||documento.getDocName().equals("") )
					documento.setDocName("Nuovo documento.txt");
			}
		}
	
		/*
		for(String key:documentoMap.keySet()){
			documento.setProperty(key, (String)documentoMap.get(key));
		}
		*/

       
		
		try {
			fileName=documento.getDocName();
			DocumentoCriteria documentoCriteria = new DocumentoCriteria();
			documentoCriteria.cleanFields(true);
			documentoCriteria.setProperty("work_item_key", createWorkitemKey(this.getProcessInstanceId(), this.getWorkItemId()));
			token = getToken(inputs);


			//imposto diritti documento
			/*List<Acl> diritti = new ArrayList<Acl>();
			String respRole = (String)this.getSystemProperties().get("respRole");
			if(!Strings.isNullOrEmpty(respRole)) {
				diritti.add(new Acl(respRole, aclRights.FullAccess));
			}
			//TODO settare readonly su tutte le UO
			String respUo = (String)this.getSystemProperties().get("respUo");
			if(!Strings.isNullOrEmpty(respUo)) {
				diritti.add(new Acl(respUo, aclRights.ReadOnly));
			}*/

			documentoCriteria.properties.remove("ENABLED");
			List<Documento> documentos = DocerService.ricercaUnitaDocumentarie(token,documentoCriteria);
			if(documentos!=null && documentos.size()>0){
				String docNumRetry = documentos.get(0).getDocNum();
				//DocerService.impostaDirittiDocumento(token,docNumRetry,diritti);
				documento = DocerService.recuperaProfiloDocumento(token,docNumRetry);
				result.put("document", documento.toFlowObject());
				result.put("userToken", token);
				log.info("end method execute create documento in retry");
				return result;
			}


//			if(mittentiLista.size()>0){
//				documento.setMittenti(mittentiLista);
//			}
//			if(destinatariLista.size()>0){
//				documento.setDestinatari(destinatariLista);
//			}
			
			
			InputStream input = null;
			
			
			if(!Strings.isNullOrEmpty(fileURL)) {

				//String httpFilePath = FilenameUtils.getFullPathNoEndSeparator(URLDecoder.decode(fileURL, "UTF-8"));
				//fileName = FilenameUtils.getName(URLDecoder.decode(fileURL, "UTF-8"));

				//URLConnection cnx = new URL(httpFilePath+"?fileName="+ URLEncoder.encode(fileName, "UTF-8")).openConnection();

				//String username = ToolkitConnector.getSysUser();
				//cnx.addRequestProperty("SSO_USER", username);
				//cnx.addRequestProperty("SSO_USER_ENTE", getEnte());


				/*URLConnection cnx = new URL(fileURL).openConnection();
				cnx.addRequestProperty(DocerAction.AUTHORIZATION, getJWTToken(inputs));
				input = cnx.getInputStream();*/

				if (estraiFirmatari) {
					File file = getFileFromUrl(fileURL);
					if (DocerService.isFileSigned(file.getAbsolutePath())){
						//List<PersonaFisica> lista = new FirmatariHelper().getFirmatari(file.getAbsolutePath(), verificaFirma);
						List<PersonaFisica> lista = DocerService.getFirmatari(new FileInputStream(file));
						documento.setFirmatari((List<Corrispondente>) (List<? extends Corrispondente>) lista);
					}
					input = new FileInputStream(file);
				} else {
					input = getStreamFromUrl(fileURL);
				}
				
			} else {
				if (documento.getDescrizione()==null || documento.getDescrizione().equals("")) {
					documento.setDescrizione("Non impostato.");
				}
				input = new ByteArrayInputStream( documento.getDescrizione().getBytes());
			}
			
			if (documento.getDocType() == null || documento.getDocType().equals("")) {
				documento.setDocType("DOCUMENTO");	
			}

			documento.setFile(input, fileName);
			documento.setProperty("PROC_ID", String.valueOf(this.getProcessInstanceId()));
			documento.setDirection("");

	
			if(documento.getAoo()==null || documento.getAoo().equals("")){
				documento.setAoo(((HashMap)this.getSystemProperties()).get("aoo").toString());
			}
			
			if(documento.getEnte()==null || documento.getEnte().equals("")){
				documento.setEnte(((HashMap)this.getSystemProperties()).get("ente").toString());
			}
			documento.properties.remove("id");
			documento.properties.remove("name");
			documento.setProperty("work_item_key", createWorkitemKey(this.getProcessInstanceId(), this.getWorkItemId()));
			UnitaDocumentaria unitDoc = new UnitaDocumentaria();
			unitDoc.DocumentoPrincipale = documento;
			token = getToken(inputs);

			documento = DocerService.creaDocumento(token,documento,null);

			documento.setProperty("@structure", "document");

			result.put("document", documento.toFlowObject());
			result.put("userToken", token);
		}catch(Exception e){
			log.error("method execute error:{}",e.getMessage());
			throw new ActionRuntimeException(e);
		}

		log.info("end method execute");
		return result;
	}

}
