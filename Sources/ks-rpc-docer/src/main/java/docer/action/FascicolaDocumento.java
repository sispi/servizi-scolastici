package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.model.Fascicolo;
import it.kdm.doctoolkit.model.UnitaDocumentaria;
import it.kdm.doctoolkit.services.DocerService;
import it.kdm.doctoolkit.services.ServizioFascicolazione;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FascicolaDocumento extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.FascicolaDocumento.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {
		log.info("init method execute");
		String token = null;
		Map<String, Object> result = new HashMap<String, Object>();

		HashMap<String, String> documentoMap = null;
		HashMap<String, String> fascicoloMap = null;
		List<HashMap<String, String>> fascicoliSecondariMap = null;
		List<Fascicolo> fascicoliSecondari = null;
		Documento documento = new Documento();
		Fascicolo fascicolo = new Fascicolo();
		
		documentoMap = inputs.containsKey("document")?(HashMap<String, String>) inputs.get("document"):null;
		fascicoloMap = inputs.containsKey("fascicolo")?(HashMap<String, String>)  inputs.get("fascicolo"):null;
		fascicoliSecondariMap = inputs.containsKey("fascicoliSecondari")?(List<HashMap<String, String>>)  inputs.get("fascicoliSecondari"):null;
		
		if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
		if(fascicoloMap == null)throw new ActionRuntimeException("PIN 'fascicolo' non trovato nei parametri di input!");
		if(fascicoliSecondariMap==null){
			log.info("Fascicoli secondari non settati");
		}else{
			fascicoliSecondari = new ArrayList<Fascicolo>();
			for(HashMap<String, String> m : fascicoliSecondariMap){
				Fascicolo fascicoloSecondario = new Fascicolo();
				fascicoloSecondario.properties = m;
				fascicoliSecondari.add(fascicoloSecondario);
			}
		}
		
		documento.properties = documentoMap;
		fascicolo.properties = fascicoloMap;
		documento.properties.remove("@structure");
		
		if(documento.properties.containsKey("userToken")){
			documento.properties.remove("userToken");
		}
		
		if(fascicolo.properties.containsKey("userToken")){
			fascicolo.properties.remove("userToken");
		}
		try {
			UnitaDocumentaria unitDoc = new UnitaDocumentaria();
			unitDoc.DocumentoPrincipale = documento;
			//TODO Fascicoli secondari ??????
			token = getToken(inputs);
			ServizioFascicolazione.fascicolaUnitaDocumentaria(token, unitDoc, fascicolo,fascicoliSecondari);

			documento = DocerService.recuperaProfiloDocumento(token, documento.getDocNum());
			documento.setProperty("@structure", "document");
			result.put("document", documentToHashmap(documento));
			result.put("userToken", token);
		} catch (Exception e) {
			log.error("method execute error;{}",e.getMessage());
			throw new ActionRuntimeException(e);
		}
		log.info("end method execute");
		return result;
	}

}
