package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassificaDocumento extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.ClassificaDocumento.class);
	
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
        log.info("init method execute");
        String token=null;
        Map<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> documentoMap = null;

        Documento documento = null;
        String classifica = null;

        documentoMap = inputs.containsKey("document")?(HashMap<String, Object>)inputs.get("document"):null;
        classifica = inputs.containsKey("classifica")?(String) inputs.get("classifica"):null;

        if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");



        try {
            documento = new Documento();

            documento.properties.remove("@type");
            documento.properties.remove("@structure");
            if(documento.properties.containsKey("userToken")){
                documento.properties.remove("userToken");
            }



            //documento.setProperty("PROC_ID", String.valueOf(this.getProcessInstanceId()));
            documento.setEnte((String) documentoMap.get("COD_ENTE"));
            documento.setAoo((String) documentoMap.get("COD_AOO"));
            documento.setDocNum((String)documentoMap.get("DOCNUM"));


            if(documento.getAoo()==null && documento.getAoo().equals("")){
                documento.setAoo((String)((HashMap)this.getSystemProperties().get("configuration")).get("aoo"));
            }

            if(documento.getEnte()==null && documento.getEnte().equals("")){
                documento.setEnte((String)((HashMap)this.getSystemProperties().get("configuration")).get("ente"));
            }









            token = getToken(inputs);
            //documento = DocerService.aggiornaDocumento(token, documento);

            documento = DocerService.classificaDocumento(token, documento, classifica);

            documento.setProperty("@structure", "document");

            result.put("document", documentToHashmap(documento));
            result.put("userToken", token);
        } catch (Exception e) {
            log.error("method execute error: {}",e.getMessage());
            throw new ActionRuntimeException(e);
        }
        log.info("end method execute");
        return result;
	}

}
