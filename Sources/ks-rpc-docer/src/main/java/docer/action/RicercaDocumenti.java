package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.*;
import it.kdm.doctoolkit.services.DocerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RicercaDocumenti extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.RicercaDocumenti.class);
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		log.info("init method execute");

        Map<String, Object> result = new HashMap<String, Object>();


        DocumentoCriteria criteria = new DocumentoCriteria();

        //setta ente e aoo dai settings
        criteria.setProperty("COD_ENTE",this.getEnte());
        criteria.setProperty("COD_AOO",this.getAoo());

        HashMap<String, String> documentoMap = inputs.containsKey("document")?(HashMap<String, String>)inputs.get("document"):null;
		if (documentoMap != null)
            criteria.copyFrom(documentoMap);

        criteria.properties.remove("@structure");
        criteria.properties.remove("id");
        criteria.properties.remove("name");

        HashMap<String, String> fascicoloMap = inputs.containsKey("parentFascicolo")?(HashMap<String, String>)inputs.get("parentFascicolo"):null;
        HashMap<String, String> folderMap = inputs.containsKey("parentFolder")?(HashMap<String, String>)inputs.get("parentFolder"):null;

        String maxResultStr = inputs.containsKey("maxResults")?(String)inputs.get("maxResults"):null;

        String forceGetProfile = inputs.containsKey("forceGetProfile")?(String)inputs.get("forceGetProfile"):null;
        if (forceGetProfile == null)
            forceGetProfile = "FALSE"; //default


        try {

            String token = getToken(inputs);

            FascicoloPrimario fascicolo = new FascicoloPrimario();
            if (fascicoloMap!=null) {
                fascicolo.copyFrom(fascicoloMap);

                criteria.setProperty("ANNO_FASCICOLO",fascicolo.getAnno());
                criteria.setProperty("CLASSIFICA",fascicolo.getClassifica());
                criteria.setProperty("PROGR_FASCICOLO",fascicolo.getProgressivo());
            }

            //Controllare se metadato gusto per il set del criterio
            Cartella folder = new Cartella();
            if (folderMap!=null) {
                folder.copyFrom(folderMap);

                criteria.setProperty("FOLDER_ID",folder.getID());
            }

            //default
            int maxResult = 1;

            try {
                maxResult = Integer.parseInt(maxResultStr);
            }
            catch(Exception e){}

            criteria.setMaxElementi(maxResult);
            criteria.setOrderBy("DOCNUM", Ordinamento.orderByEnum.ASC);

            List<Documento> docs = DocerService.ricercaUnitaDocumentarie(token,criteria);

            List<HashMap<String, Object>> outListDocs = new ArrayList<HashMap<String, Object>>();

            for (Documento doc : docs) {
                Documento d = doc;
                if (forceGetProfile.equalsIgnoreCase("true")) {
                    d = DocerService.recuperaProfiloDocumento(token, doc.getDocNum());
                }
                outListDocs.add(d.toFlowObject());
            }

            HashMap<String, Object> firstDoc = null;
            List<HashMap<String, Object>> deltaList = new ArrayList<HashMap<String, Object>>(outListDocs);

            if (outListDocs.size()>0) {
                firstDoc = outListDocs.get(0);
                deltaList.remove(firstDoc);
            }

            result.put("first", firstDoc);
            result.put("delta", deltaList);
            result.put("results", outListDocs);
            result.put("userToken", token);

        } catch (Exception e) {
			log.error("method execute error: {}",e.getMessage());
			throw new ActionRuntimeException(e);
		}

        log.info("end method execute");

        return result;
	}

}
