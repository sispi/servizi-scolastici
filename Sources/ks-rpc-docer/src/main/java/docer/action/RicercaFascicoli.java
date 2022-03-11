package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Fascicolo;
import it.kdm.doctoolkit.model.FascicoloCriteria;
import it.kdm.doctoolkit.model.FascicoloPrimario;
import it.kdm.doctoolkit.services.DocerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RicercaFascicoli extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.RicercaFascicoli.class);
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		log.info("init method execute");

        Map<String, Object> result = new HashMap<String, Object>();


        FascicoloCriteria criteria = new FascicoloCriteria();

        //setta ente e aoo dai settings
        criteria.setProperty("COD_ENTE",this.getEnte());
        criteria.setProperty("COD_AOO",this.getAoo());

        HashMap<String, String> fascicoloMap = inputs.containsKey("fascicolo")?(HashMap<String, String>)inputs.get("fascicolo"):null;
		if (fascicoloMap != null)
            criteria.copyFrom(fascicoloMap);

        criteria.properties.remove("@structure");
        criteria.properties.remove("id");
        criteria.properties.remove("name");

        HashMap<String, String> parentFascicoloMap = inputs.containsKey("parentFascicolo")?(HashMap<String, String>)inputs.get("parentFascicolo"):null;

        String maxResultStr = inputs.containsKey("maxResults")?(String)inputs.get("maxResults"):null;

        try {

            String token = getToken(inputs);

            FascicoloPrimario fascicolo = new FascicoloPrimario();
            if (parentFascicoloMap!=null) {
                fascicolo.copyFrom(parentFascicoloMap);

                criteria.setProperty("ANNO_FASCICOLO",fascicolo.getAnno());
                criteria.setProperty("CLASSIFICA",fascicolo.getClassifica());
                criteria.setProperty("PARENT_PROGR_FASCICOLO",fascicolo.getProgressivo());
            }

            //default
            int maxResult = 1;

            try {
                maxResult = Integer.parseInt(maxResultStr);
            }
            catch(Exception e){

                throw new ActionRuntimeException("pin maxResultStr value not is integer");

            }

            criteria.setMaxElementi(maxResult);
            //criteria.setOrderBy("DOCNUM", Ordinamento.orderByEnum.ASC);

            List<Fascicolo> fascicoli = DocerService.ricercaFascicoli(token,criteria);

            List<HashMap<String, Object>> outListFascicoli = new ArrayList<HashMap<String, Object>>();

            for (Fascicolo fas : fascicoli) {
                outListFascicoli.add(fas.toFlowObject());
            }

            HashMap<String, Object> firstFascicolo = null;
            List<HashMap<String, Object>> deltaList = new ArrayList<HashMap<String, Object>>(outListFascicoli);

            if (outListFascicoli.size()>0) {
                firstFascicolo = outListFascicoli.get(0);
                deltaList.remove(firstFascicolo);
            }

            result.put("first", firstFascicolo);
            result.put("delta", deltaList);
            result.put("results", outListFascicoli);
            result.put("userToken", token);

        } catch (Exception e) {
			log.error("method execute error: {}",e.getMessage());
			throw new ActionRuntimeException(e);
		}

        log.info("end method execute");

        return result;
	}

}
