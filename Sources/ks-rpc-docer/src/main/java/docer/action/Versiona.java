package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.DocerFile;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Versiona extends DocerAction {

private final static Logger log = LoggerFactory.getLogger(docer.action.Versiona.class);
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {
		log.info("init method execute");
		
		Map<String, Object> result = new HashMap<String, Object>();
		HashMap<String, String> documentoMap = null;
		try {			
			Documento documento = new Documento();
			
			boolean replaceVersion = false;		
			documentoMap = inputs.containsKey("document")?(HashMap<String, String>) inputs.get("document"):null;
			
			if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
			if(!inputs.containsKey("replaceVersion"))throw new ActionRuntimeException("PIN 'replaceVersion' non trovato nei parametri di input!");
			
			replaceVersion = (boolean) inputs.get("replaceVersion");
			documento.properties = documentoMap;
			
			String token = getToken(inputs);
			DocerFile file = DocerService.downloadDocument(token, documentoMap.get("DOCNUM"));
			
			InputStream input = file.getContent().getInputStream();
			
			if(replaceVersion)
				DocerService.sovrascriviUltimaVersione(token, documento, input);
			else
				DocerService.aggiungiNuovaVersione(token, documento, input);
			
			documento = DocerService.recuperaProfiloDocumento(token, documento.getDocNum());
			result.put("document", documentToHashmap(documento));
            result.put("userToken", token);
			
		} catch (Exception e) {
			log.error("method execute error;{}",e.getMessage());
			throw new ActionRuntimeException(e);
		}
		
		
		return result;
	}
	

	
}
