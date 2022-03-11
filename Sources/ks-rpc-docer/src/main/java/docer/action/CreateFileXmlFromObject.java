package docer.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.DocerFile;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import it.kdm.doctoolkit.utils.XMLHelper;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CreateFileXmlFromObject extends DocerAction {
    private final static Logger log = LoggerFactory.getLogger(docer.action.CreateFileXmlFromObject.class);

    @Override
    public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {


        log.info("init method execute");
        Map<String, Object> result = new HashMap<String, Object>();
        String token = null;
        String docNumXsl = null;
        HashMap<Object, Object> mapFromObject = null;
        HashMap<String, Object> documentoXslMap = null;

        mapFromObject = inputs.containsKey("oggetto") ? (HashMap<Object, Object>) inputs.get("oggetto") : null;
        documentoXslMap = inputs.containsKey("documentoXslMap") ? (HashMap<String, Object>) inputs.get("documentoXslMap") : null;
        if (mapFromObject == null)
            throw new ActionRuntimeException("PIN oggetto non trovato");

        try {

            //String linkFileUrl = ServerProperties.getParamsBpm("url.download.file");
            token = getToken(inputs);

            if (!documentoXslMap.containsKey("DOCNUM") || documentoXslMap.get("DOCNUM") == null) {
                throw new ActionRuntimeException("DOCNUM not present on documentoXslMap or null");
            } else {
                docNumXsl = documentoXslMap.get("DOCNUM").toString();
            }

            DocerFile dfXsl = DocerService.downloadDocument(token, docNumXsl);
            mapFromObject.remove("@structure");

            // convert map to json
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String jsonFromMap = ow.writeValueAsString(mapFromObject);

            //conversione json in xml
            String xml = XML.toString(new JSONObject(jsonFromMap));
            StringBuilder builder = new StringBuilder();
            builder
                    .append("<?xml version=\"1.0\"?>")
                    .append("<Element>")
                    .append(xml)
                    .append("</Element>");
            xml = builder.toString();

            String xmlSelected = XMLHelper.transformXML(dfXsl.getContent().getInputStream(), xml);
            xmlSelected="<?xml version=\"1.0\"?>\n"+xmlSelected;

            String tempDir = ServerProperties.getParamsBpm("mail.attachDirEmailPec");

            // rinomino il file da creare con il nome del file xsl
            String fileName = null;
            if (documentoXslMap.get("DOCNAME") != null && !"".equals(documentoXslMap.get("DOCNAME"))) {
                fileName = FilenameUtils.getBaseName((String) documentoXslMap.get("DOCNAME"));
                fileName = fileName + ".xml";
            } else {
                fileName = docNumXsl + ".xml";
            }

            UUID guid = UUID.randomUUID();
            File originalFile = new File(tempDir + File.separator + guid, fileName);
            originalFile.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(originalFile);
            OutputStream out = new FileOutputStream(originalFile);
            IOUtils.copy(new ByteArrayInputStream(xmlSelected.getBytes()), out);
            out.close();

            Documento documentoXml = new Documento();
            documentoXml.properties.put("filePath", getUrlFromFile(originalFile));

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
