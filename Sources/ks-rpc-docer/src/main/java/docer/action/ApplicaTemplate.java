package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.DocerFile;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicaTemplate extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.ApplicaTemplate.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		//String tempDir = System.getProperty("java.io.tmpdir");

		UUID guid = UUID.randomUUID();
		log.info("init method execute ApplicaTemplate");
		Map<String, Object> result = new HashMap<String, Object>();
		HashMap<String, Object> documentoMap = null;
		DocerFile template = new DocerFile();
        Map<String, String> templateParams = new HashMap<String, String>();

        try{
            //String tempDir = ServerProperties.getParamsBpm("mail.attachDirEmailPec");
	        String token = getToken(inputs);

            if (inputs.containsKey("variabili"))
                templateParams = (HashMap<String, String>)inputs.get("variabili");

            documentoMap = (HashMap<String, Object>)inputs.get("document");
			if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
	        String docNumTemplate = (String)documentoMap.get("DOCNUM");

			template = DocerService.downloadDocument(token, docNumTemplate);
			Documento documentoTemplateProfilo = DocerService.recuperaProfiloDocumento(token, docNumTemplate);

			/*File fileTemplate = new File(tempDir+ File.separator+guid, "template_"+documentoTemplateProfilo.getDocName());
			fileTemplate.getParentFile().mkdirs();
			FileWriter writer = new FileWriter(fileTemplate);
			OutputStream out = new FileOutputStream(fileTemplate);
			IOUtils.copy(template.getContent().getInputStream(), out);
			out.close();
			
			File fileTemplateApplied = new File(tempDir+ File.separator+guid, documentoTemplateProfilo.getDocName());
			DocumentService documentService = new DocumentService();
			documentService.applyTemplate(fileTemplate,fileTemplateApplied,templateParams);
            String linkFileUrl = ServerProperties.getParamsBpm("url.download.file");*/
			
			Documento doc = new Documento();
			//doc.setProperty("filePath", linkFileUrl+fileTemplateApplied.getAbsolutePath());
			doc.setProperty("filePath", getUrlFromStream(template.getContent().getInputStream()) );
            doc.setDocType(documentoTemplateProfilo.getDocType());
            doc.setDocName(documentoTemplateProfilo.getDocName());
            doc.setTipoComponente(documentoTemplateProfilo.getTipoComponente());
			
			doc.setEnte((String)(this.getSystemProperties()).get("ente"));
			doc.setAoo((String)(this.getSystemProperties()).get("aoo"));
			
//			Documento docCreated = DocerService.creaDocumento(token, doc, null);
							
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
