package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EliminaDocumento extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(ApplicaTimbro.class);

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {

		log.info("init method execute");
		String token = null;
		Map<String, Object> result = new HashMap<String, Object>();
		HashMap<String, Object> documentoMap = null;
		Boolean esitoB = null;
		Documento documento = new Documento();
		documentoMap = inputs.containsKey("document")?(HashMap<String, Object>)inputs.get("document"):null;

		if(documentoMap == null )throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");

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
		if(documento.getDocNum()==null || "".equals(documento.getDocNum())){
			throw new ActionRuntimeException("identificativo del documento non valido");
		}
		try {
			token = getToken(inputs);
			esitoB = DocerService.rimuoviDocumentoDefinitivamente(token, documento);
			result.put("esito", esitoB.toString());
			result.put("userToken", token);
		} catch (Exception e) {
			log.error("method execute error:{}",e.getMessage());
			throw new ActionRuntimeException(e);
		}
		finally{

		}
		log.info("end method execute");
		return result;
	}

}
