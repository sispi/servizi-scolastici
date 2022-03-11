package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.DocerFile;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecuperaProfiloDocumento extends DocerAction {
	private final static Logger log = LoggerFactory.getLogger(docer.action.RecuperaProfiloDocumento.class);
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		//String tempDir = System.getProperty("java.io.tmpdir");

		log.info("init method execute");
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listaAllegati = new ArrayList<Map<String, Object>>();
		String token=null;
		String docNum = null;
		
		
		HashMap<String, Object> documentoMap = null;

		Documento documento = new Documento();
		documentoMap = inputs.containsKey("document")?(HashMap<String, Object>)inputs.get("document"):null;
		if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
		 String forceDownloadDocument = inputs.containsKey("forceDownloadDocument")?(String)inputs.get("forceDownloadDocument"):null;
	        if (forceDownloadDocument == null)
	        	forceDownloadDocument = "FALSE"; //default
		try {
            //String tempDir = ServerProperties.getParamsBpm("mail.attachDirEmailPec");
            //UUID guid = UUID.randomUUID();
			token = getToken(inputs);
//			for(String key:documentoMap.keySet()){
//				documento.setProperty(key, (String)documentoMap.get(key));
//			}
			if(!documentoMap.containsKey("DOCNUM") || documentoMap.get("DOCNUM")==null){
				throw new ActionRuntimeException("DOCNUM not present or null");
			}else{
				docNum=  documentoMap.get("DOCNUM").toString();
			}
			Documento doc = DocerService.recuperaProfiloDocumento(token, docNum);
			List<Documento> related = DocerService.getRiferimenti(token, docNum);
            for(Documento temp : related){
                listaAllegati.add(temp.toFlowObject());
            }
            
            if (forceDownloadDocument.equalsIgnoreCase("true")) {
            	DocerFile fileDownload = DocerService.downloadDocument(token, docNum);

				result.put("filePath", getUrlFromStream(fileDownload.getContent().getInputStream()) );

    			/*File originalFile = new File(tempDir+ File.separator+guid,doc.getDocName());
    			originalFile.getParentFile().mkdirs();
    			FileWriter writer = new FileWriter(originalFile);
    			OutputStream out = new FileOutputStream(originalFile);
    			IOUtils.copy(fileDownload.getContent().getInputStream(), out);
    			out.close();

    			String linkFileUrl = ServerProperties.getParamsBpm("url.download.file");

    			result.put("filePath", linkFileUrl+originalFile.getAbsolutePath());*/
            }else{
            	result.put("filePath", "");
            }		
            result.put("document", doc.toFlowObject());
			//result.put("document", unitaDocumentaria.getDocumentoPrincipale().toFlowObject());
			result.put("userToken", token);
			result.put("allegati", listaAllegati);
		} catch (Exception e) {
			log.error("method execute error:{}",e.getMessage());
			throw new ActionRuntimeException(e);
		}
		log.info("end method execute");
		return result;
	}

	

}
