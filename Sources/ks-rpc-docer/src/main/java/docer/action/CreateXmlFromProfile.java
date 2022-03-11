package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.DocerFile;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import it.kdm.doctoolkit.utils.XMLHelper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateXmlFromProfile extends DocerAction {
    private final static Logger log = LoggerFactory.getLogger(docer.action.CreateXmlFromProfile.class);

    @Override
    public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {
        //String tempDir = System.getProperty("java.io.tmpdir");

        log.info("init method execute");
        Map<String, Object> result = new HashMap<String, Object>();
        String token = null;
        String docNum = null;
        String docNumXsl = null;

        HashMap<String, Object> documentoMap = null;
        HashMap<String, Object> documentoXslMap = null;

        Documento documento = new Documento();
        documentoMap = inputs.containsKey("documento") ? (HashMap<String, Object>) inputs.get("documento") : null;
        documentoXslMap = inputs.containsKey("documentoXslMap") ? (HashMap<String, Object>) inputs.get("documentoXslMap") : null;
        if (documentoMap == null)
            throw new ActionRuntimeException("PIN 'documew" +
                    "s" +
                    "whanto' non trovato nei parametri di input!");

        try {
            //String linkFileUrl = ServerProperties.getParamsBpm("url.download.file");

            token = getToken(inputs);

            if (!documentoMap.containsKey("DOCNUM") || documentoMap.get("DOCNUM") == null) {
                throw new ActionRuntimeException("DOCNUM not present on document or null");
            } else {
                docNum = documentoMap.get("DOCNUM").toString();
            }
            if (!documentoXslMap.containsKey("DOCNUM") || documentoXslMap.get("DOCNUM") == null) {
                throw new ActionRuntimeException("DOCNUM not present on documentoXslMap or null");
            } else {
                docNumXsl = documentoXslMap.get("DOCNUM").toString();
            }

            String xmlMetadata = DocerService.recuperaXmlProfiloDocumento(token, docNum);
            //TODO da completare


            DocerFile dfXsl = DocerService.downloadDocument(token, docNumXsl);

            String xmlSelected = XMLHelper.transformXML(dfXsl.getContent().getInputStream(), xmlMetadata);

            String fileName = null;
            if (documentoMap.get("DOCNAME") != null && !"".equals(documentoMap.get("DOCNAME"))) {
                fileName = FilenameUtils.getBaseName((String) documentoMap.get("DOCNAME"));
                fileName = fileName + ".xml";
            } else {
                fileName = docNum + ".xml";
            }
            String tempDir = ServerProperties.getParamsBpm("mail.attachDirEmailPec");
            UUID guid = UUID.randomUUID();
            File originalFile = new File(tempDir + File.separator + guid, fileName);
            originalFile.getParentFile().mkdirs();
            //FileWriter writer = new FileWriter(originalFile);
            OutputStream out = new FileOutputStream(originalFile);
            IOUtils.copy(new ByteArrayInputStream(xmlSelected.getBytes()), out);
            out.close();
            result.put("filePath",  getUrlFromFile(originalFile) );

            Documento documentoXml = new Documento();
            documentoXml.properties.put("filePath", getUrlFromFile(originalFile) );
            //result.put("document", unitaDocumentaria.getDocumentoPrincipale().toFlowObject());
            result.put("userToken", token);
            result.put("documentoXml", documentoXml.toFlowObject());
        } catch (Exception e) {
            log.error("method execute error:{}", e.getMessage());
            throw new ActionRuntimeException(e);
        }
        log.info("end method execute");
        return result;
    }


}
