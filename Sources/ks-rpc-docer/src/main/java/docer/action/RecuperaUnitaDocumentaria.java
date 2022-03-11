package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecuperaUnitaDocumentaria extends DocerAction {
	private final static Logger log = LoggerFactory.getLogger(RecuperaProfiloDocumento.class);
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		log.info("init method execute");
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listaAllegati = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listAnnessi = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listAnnotazioni = new ArrayList<Map<String, Object>>();
		String token=null;
		String docNum = null;
		
		
		HashMap<String, Object> documentoMap = null;

		Documento documento = new Documento();
		documentoMap = inputs.containsKey("document")?(HashMap<String, Object>)inputs.get("document"):null;
		if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
		
		try {
			token = getToken(inputs);

			if(!documentoMap.containsKey("DOCNUM") || documentoMap.get("DOCNUM")==null){
				throw new ActionRuntimeException("DOCNUM not present or null");
			}else{
				docNum=  documentoMap.get("DOCNUM").toString();
			}
			Documento doc = DocerService.recuperaProfiloDocumento(token, docNum);
			List<Documento> related = DocerService.recuperaCorrelati(token, docNum);
            for(Documento temp : related){
            	
            	if(temp.getTipoComponente()!= null && temp.getTipoComponente().equals("ALLEGATO")){
            		listaAllegati.add(temp.toFlowObject());
            	}else if(temp.getTipoComponente()!= null && temp.getTipoComponente().equals("ANNESSO")){
            		listAnnessi.add(temp.toFlowObject());
            	}else if(temp.getTipoComponente()!= null && temp.getTipoComponente().equals("ANNOTAZIONE")){
            		listAnnotazioni.add(temp.toFlowObject());
            	}   
            }

            result.put("document", doc.toFlowObject());
			result.put("userToken", token);
			result.put("allegati", listaAllegati);
			result.put("annessi", listAnnessi);
			result.put("annotazioni", listAnnotazioni);
		} catch (Exception e) {
			log.error("method execute error:{}",e.getMessage());
			throw new ActionRuntimeException(e);
		}
		log.info("end method execute");
		return result;
	}

	

}
