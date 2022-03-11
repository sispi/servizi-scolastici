package docer.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.DocerFile;
import it.kdm.doctoolkit.services.DocerService;
import it.kdm.doctoolkit.utils.XMLHelper;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateXmlFromObject extends DocerAction {
    private final static Logger log = LoggerFactory.getLogger(docer.action.CreateXmlFromObject.class);

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
            result.put("userToken", token);
            result.put("stringaXml", xmlSelected);

        } catch (Exception e) {
            log.error("method execute error:{}", e.getMessage());
            throw new ActionRuntimeException(e);
        }
        log.info("end method execute");
        return result;
    }


}
