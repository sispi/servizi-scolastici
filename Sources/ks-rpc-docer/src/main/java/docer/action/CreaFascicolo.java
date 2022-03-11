package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Fascicolo;
import it.kdm.doctoolkit.model.FascicoloCriteria;
import it.kdm.doctoolkit.services.DocerService;
import it.kdm.doctoolkit.services.ServizioFascicolazione;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreaFascicolo extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.CreaFascicolo.class);
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		log.info("init method execute");
		String token=null;
		Map<String, Object> result = new HashMap<String, Object>();

		HashMap<String, String> fascicoloMap = null;
		HashMap<String, String> fascicoloPadreMap = null;
		HashMap<String, String> mittenteMap = null;
		
		
		fascicoloMap = inputs.containsKey("fascicolo")?(HashMap<String, String>) inputs.get("fascicolo"):null;
		fascicoloPadreMap = inputs.containsKey("fascicoloPadre")?(HashMap<String, String>) inputs.get("fascicoloPadre"):null;
		mittenteMap = inputs.containsKey("mittente")?(HashMap<String, String>) inputs.get("mittente"):null;


		if(fascicoloMap == null)throw new ActionRuntimeException("PIN 'fascicolo' non trovato nei parametri di input!");
		//if(fascicoloPadre == null)throw new ActionRuntimeException("PIN 'fascicoloPadre' non trovato nei parametri di input!");





		Fascicolo fascicolo = new Fascicolo();
		fascicoloMap.remove("TaskName");
		fascicoloMap.remove("filePath");
		fascicoloMap.remove("GroupId");
		fascicoloMap.remove("comment");
		fascicoloMap.remove("outcome");
		fascicoloMap.remove("NodeName");
		fascicoloMap.remove("ActorId");
		fascicoloMap.remove("@structure");
		fascicoloMap.remove("name");
		fascicoloMap.remove("id");
		
		if(fascicoloMap.containsKey("userToken")){
			fascicoloMap.remove("userToken");
		}
		
		
		
		fascicolo.properties = fascicoloMap;
	
		Fascicolo fascicoloPadre = new Fascicolo();
		fascicoloPadre.properties = fascicoloPadreMap;
	
		if(fascicoloPadreMap != null){
			fascicolo.setProgressivoPadre(fascicoloPadre.getProgressivo());
			fascicolo.setClassifica(fascicoloPadre.getClassifica());
			fascicolo.setAnno(fascicoloPadre.getAnno());
		}
		//else{
		//	fascicolo.setProgressivoPadre("");
		//}
		

		if(mittenteMap != null){
			if (mittenteMap.containsKey("@type") && mittenteMap.containsKey("id")) {
                String type = mittenteMap.get("@type");
                if (type.equalsIgnoreCase("PersonaFisica")) {
                    String id = mittenteMap.get("id");
                    fascicolo.setCodFiscPersona(id);
                }
                if (type.equalsIgnoreCase("PersonaGiuridica")) {
                    String id = mittenteMap.get("id");
                    fascicolo.setCodFiscAzienda(id);
                }
            }

		}
		 
		try {
			/*List<Acl> diritti = new ArrayList<Acl>();
			String respRole = (String)this.getSystemProperties().get("respRole");
			diritti.add(new Acl(respRole, aclRights.FullAccess));
			//TODO settare readonly su tutte le UO
			String respUo = (String)this.getSystemProperties().get("respUo");
			diritti.add(new Acl(respUo, aclRights.ReadOnly));**/
		
			fascicolo.setProperty("id_proc", String.valueOf(this.getProcessInstanceId()));
			fascicolo.setProperty("work_item_key", createWorkitemKey(this.getProcessInstanceId(), this.getWorkItemId()));
			

			if(fascicolo.getAoo()==null || fascicolo.getAoo().equals("")){
				fascicolo.setAoo(((HashMap)this.getSystemProperties()).get("aoo").toString());
			}
			
			if(fascicolo.getEnte()==null || fascicolo.getEnte().equals("")){
				fascicolo.setEnte(((HashMap)this.getSystemProperties()).get("ente").toString());
			}
			
			
			
			
			token = getToken(inputs);

			Fascicolo fascSearch = new Fascicolo();
			fascSearch.cleanFields(true);
			fascSearch.setProperty("work_item_key", createWorkitemKey(this.getProcessInstanceId(), this.getWorkItemId()));

			List<Fascicolo> fascicolos = ServizioFascicolazione.ricercaFascicoli(token,fascSearch);
			if(fascicolos!= null && fascicolos.size()>0){
				Fascicolo tmp = fascicolos.get(0);
				//DocerService.impostaDirittiFascicolo(token,tmp,diritti);
				FascicoloCriteria fascicoloCriteria = new FascicoloCriteria();
				fascicoloCriteria.setEnte(tmp.getEnte());
				fascicoloCriteria.setAoo(tmp.getAoo());
				fascicoloCriteria.setProperty("ANNO_FASCICOLO",tmp.getAnno());
				fascicoloCriteria.setProperty("PROGR_FASCICOLO",tmp.getProgressivo());
				fascicoloCriteria.setProperty("CLASSIFICA",tmp.getClassifica());
				Fascicolo fout=  DocerService.recuperaFascicolo(token, fascicoloCriteria);
				result.put("fascicolo", fout.properties);
				result.put("userToken", token);
				log.info("end method execute create fascicolo retry mode");
				return result;
			}


			Fascicolo doc = null;
			if(fascicolo.properties.containsKey("PROGR_FASCICOLO") && 
					fascicolo.properties.get("PROGR_FASCICOLO")!=null && 
					!fascicolo.properties.get("PROGR_FASCICOLO").equals("")){
				doc = ServizioFascicolazione.creaFascicoloForzaNuovo(token, fascicolo, null);
			}else{
				doc = ServizioFascicolazione.creaFascicolo(token, fascicolo, null);
			}

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
