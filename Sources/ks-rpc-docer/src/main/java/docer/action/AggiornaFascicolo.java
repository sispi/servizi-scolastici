package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Fascicolo;
import it.kdm.doctoolkit.services.ServizioFascicolazione;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AggiornaFascicolo extends DocerAction{

	private final static Logger log = LoggerFactory.getLogger(docer.action.AggiornaFascicolo.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		String token=null;
		log.info("init method execute");
		Map<String, Object> result = new HashMap<String, Object>();

		HashMap<String, String> fascicoloMap = null;
		//HashMap<String, String> fascicoloPadreMap = null;
		//HashMap<String, String> mittenteMap = null;
		
		fascicoloMap = inputs.containsKey("fascicolo")?(HashMap<String, String>) inputs.get("fascicolo"):null;
		//fascicoloPadreMap = inputs.containsKey("fascicoloPadre")?(HashMap<String, String>) inputs.get("fascicoloPadre"):null;
		
		if(fascicoloMap == null)throw new ActionRuntimeException("PIN 'fascicolo' non trovato nei parametri di input!");
		//if(fascicoloPadre == null)throw new ActionRuntimeException("PIN 'fascicoloPadre' non trovato nei parametri di input!");
		
		Fascicolo fascicolo = new Fascicolo();
		fascicolo.properties = fascicoloMap;
	
//		Fascicolo fascicoloPadre = new Fascicolo();
//		fascicoloPadre.properties = fascicoloPadreMap;
	
		fascicolo.properties.remove("@structure");
//		fascicoloPadre.properties.remove("@structure");
		
		if(fascicolo.properties.containsKey("userToken")){
			fascicolo.properties.remove("userToken");
		}
//		if(fascicoloPadre.properties.containsKey("userToken")){
//			fascicoloPadre.properties.remove("userToken");
//		}
		
//		if(fascicoloPadreMap != null){
//			fascicolo.setProgressivoPadre(fascicoloPadre.getProgressivo());
//		}
//		else{
//			fascicolo.setProgressivoPadre("");
//		}
		
		//TODO mittenti ?????
//		if(mittenteMap != null ){
//			fascicolo.setCFPersona(mittenteMap.get(""));
//		}
		
		try {
			/*List<Acl> diritti = new ArrayList<Acl>();
			String respRole = (String)this.getSystemProperties().get("respRole");
			diritti.add(new Acl(respRole, aclRights.FullAccess));*/
			//TODO settare readonly su tutte le UO
			//String respUo = (String)this.getSystemProperties().get("respUo");
			//diritti.add(new Acl(respUo, aclRights.ReadOnly));
			fascicolo.setProperty("id_proc", String.valueOf(this.getProcessInstanceId()));
			token = getToken(inputs);
			
			Fascicolo doc = ServizioFascicolazione.aggiornaFascicolo(token, fascicolo, null);
			doc.setProperty("@structure", "fascicolo");
			result.put("fascicolo", doc.properties);
			result.put("userToken", token);
		} catch (Exception e) {
			log.error("method execute error:{}",e.getMessage());
			e.printStackTrace();
			throw new ActionRuntimeException(e);
			
		}
		
		log.info("end method execute");
		
		return result;
	}
}
