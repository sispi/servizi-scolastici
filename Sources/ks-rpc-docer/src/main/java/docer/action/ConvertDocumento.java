package docer.action;

import docer.exception.ActionRuntimeException;
import docer.utils.FileConverterHelper;
import it.kdm.doctoolkit.model.DocerFile;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertDocumento extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.ConvertDocumento.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		//String tempDir = System.getProperty("java.io.tmpdir");
		log.info("init method execute ConvertDocumento");
		Map<String, Object> result = new HashMap<String, Object>();
		HashMap<String, Object> documentoMap = null;
		//DocerFile template = new DocerFile();
		String conversionType ="";
        try{
            String tempDir = ServerProperties.getParamsBpm("mail.attachDirEmailPec");
            UUID guid = UUID.randomUUID();
	        String token = getToken(inputs);
			
			Iterator<Entry<String, Object>> it = inputs.entrySet().iterator();
			    while (it.hasNext()) {
			        @SuppressWarnings("rawtypes")
					Entry pairs = (Entry)it.next();
			        log.info("valori template"+pairs.getKey() + " = " + pairs.getValue());
			        if(pairs.getKey().equals("document")){
			        	documentoMap = (HashMap<String, Object>)inputs.get("document");
			        }else if(pairs.getKey().equals("conversionType")){
			        	conversionType = pairs.getValue().toString();
			        }
			    }
			
			if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
	        String docNumTemplate = (String)documentoMap.get("DOCNUM");
			
			DocerFile template = DocerService.downloadDocument(token, docNumTemplate);
			Documento documentoTemplateProfilo = DocerService.recuperaProfiloDocumento(token, docNumTemplate);

			File originalFile = new File(tempDir+ File.separator+guid, "template_"+documentoTemplateProfilo.getDocName());
			originalFile.getParentFile().mkdirs();
			//FileWriter writer = new FileWriter(originalFile);
			OutputStream out = new FileOutputStream(originalFile);
			IOUtils.copy(template.getContent().getInputStream(), out);
			out.close();

            //String linkFileUrl = ServerProperties.getParamsBpm("url.download.file");
			
			String fileConverter = FilenameUtils.getBaseName(documentoTemplateProfilo.getDocName());
			String docNameConverted = fileConverter+"."+conversionType;
			File fileConverted = new File(tempDir+ File.separator+guid, docNameConverted);
			//JODConverterHelper.convert(originalFile, fileConverted);
			FileConverterHelper.convert(originalFile,fileConverted);
			
			Documento doc = new Documento();
			doc.setProperty("filePath", getUrlFromFile(fileConverted) );
			doc.setDocType("DOCUMENTO");
			doc.setDocName(docNameConverted);
			
			doc.setEnte((String)(this.getSystemProperties()).get("ente"));
			doc.setAoo((String)(this.getSystemProperties()).get("aoo"));
										
			result.put("document", doc.toFlowObject());
			result.put("userToken", token);

        } catch (Exception e) {
			log.error("method execute error:{}",e.getMessage());
			throw new ActionRuntimeException(e);
		}
		log.info("end method execute");
		return result;
	}

}
