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

public class AggiornaRelated extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.AggiornaRelated.class);
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		log.info("init method execute");

        Map<String, Object> result = new HashMap<String, Object>();


		Documento documento = new Documento();

        HashMap<String, String> documentoMap = inputs.containsKey("document")?(HashMap<String, String>)inputs.get("document"):null;
		if (documentoMap == null)
            throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");

        documento.copyFrom(documentoMap);

        List<HashMap<String, String>> relatedDocs = inputs.containsKey("relatedDocs")?(List<HashMap<String, String>>)inputs.get("relatedDocs"):null;
        if (relatedDocs == null)
            throw new ActionRuntimeException("PIN 'relatedDocs' non trovato nei parametri di input!");


        try {

            List<String> listaDocNum = new ArrayList<String>();

            for (HashMap<String, String> relDoc : relatedDocs) {
                Documento doc = new Documento();
                doc.copyFrom(relDoc);
                listaDocNum.add(doc.getDocNum());
            }

            String token = getToken(inputs);
            DocerService.correlaDocumenti(token,documento.getDocNum(),listaDocNum);

            documento = DocerService.recuperaProfiloDocumento(token,documento.getDocNum());
            documento.setProperty("@structure", "document");

            List<HashMap<String, Object>> listaRelatedDoc = new ArrayList<HashMap<String, Object>>();
            for (String docNum : listaDocNum) {
                Documento doc = DocerService.recuperaProfiloDocumento(token,docNum);
                listaRelatedDoc.add(doc.toFlowObject());
            }

            //prepara la lista con tutti i documenti per il PIN results
            List<HashMap<String, Object>> allDocs = new ArrayList<HashMap<String, Object>>(listaRelatedDoc);
            allDocs.add(documento.toFlowObject());

            listaRelatedDoc.remove(documento.toFlowObject());

            result.put("first", documento.toFlowObject());
            result.put("delta", listaRelatedDoc);
            result.put("results", allDocs);
            result.put("userToken", token);

        } catch (Exception e) {
			log.error("method execute error: {}",e.getMessage());
			throw new ActionRuntimeException(e);
		}

        log.info("end method execute");

        return result;
	}

}
