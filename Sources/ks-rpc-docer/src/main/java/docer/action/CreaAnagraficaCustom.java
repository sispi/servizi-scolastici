package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.AnagraficaCustom;
import it.kdm.doctoolkit.services.DocerService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreaAnagraficaCustom extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.CreaAnagraficaCustom.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)  throws ActionRuntimeException {

		log.info("init method execute");
		String token = null;
		Map<String, Object> result = new HashMap<String, Object>();
		String anagType = inputs.containsKey("anag_type")?(String) inputs.get("anag_type"):null;
		Map<String, String> outputAnag=null;
		try {
			token = getToken(inputs);
			if(anagType == null) throw new ActionRuntimeException("PIN 'anagType' non trovato nei parametri di input!");
			AnagraficaCustom anagrafica = new AnagraficaCustom();
			anagrafica.properties.clear();
			inputs.remove("anag_type");
			for (Map.Entry<String, Object> entry : inputs.entrySet()) {
				String key = entry.getKey();
				if(!key.toLowerCase().contains("token") && !key.toLowerCase().contains("groupid") & !key.toLowerCase().contains("taskname") && !key.toLowerCase().contains("operation")) {
					String value = (String) entry.getValue();
					anagrafica.properties.put(key, value);
				}
			}
			anagrafica = DocerService.creaAnagraficaCustom(token, anagType, anagrafica);
			for (Map.Entry<String, String> entry : anagrafica.properties.entrySet()) {
				String key = entry.getKey();
				String value = (String) entry.getValue();
				result.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("method execute error:{}",e.getMessage());
			throw new ActionRuntimeException(e);
		}

		log.info("end method execute");
		return result;
	}

}
