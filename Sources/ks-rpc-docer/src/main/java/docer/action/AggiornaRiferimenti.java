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


//aggiungiRiferimento
//getRiferimenti

public class AggiornaRiferimenti extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.AggiornaRiferimenti.class);
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

        List<HashMap<String, String>> referenceDocs = inputs.containsKey("referenceDocs")?(List<HashMap<String, String>>)inputs.get("referenceDocs"):null;
        if (referenceDocs == null)
            throw new ActionRuntimeException("PIN 'referenceDocs' non trovato nei parametri di input!");


        try {

            List<Documento> listaDocNum = new ArrayList<Documento>();

            for (HashMap<String, String> relDoc : referenceDocs) {
                Documento doc = new Documento();
                doc.copyFrom(relDoc);
                listaDocNum.add(doc);
            }

            String token = getToken(inputs);
            DocerService.aggiungiRiferimento(token,documento,listaDocNum);

            documento = DocerService.recuperaProfiloDocumento(token,documento.getDocNum());
            documento.setProperty("@structure", "document");

            List<HashMap<String, Object>> listReferenceDoc = new ArrayList<HashMap<String, Object>>();
            for (Documento tmpDoc : listaDocNum) {
                Documento doc = DocerService.recuperaProfiloDocumento(token,tmpDoc.getDocNum());
                listReferenceDoc.add(doc.toFlowObject());
            }

            //prepara la lista con tutti i documenti per il PIN results
            List<HashMap<String, Object>> allDocs = new ArrayList<HashMap<String, Object>>(listReferenceDoc);
            allDocs.add(documento.toFlowObject());

            listReferenceDoc.remove(documento.toFlowObject());

            result.put("first", documento.toFlowObject());
            result.put("delta", listReferenceDoc);
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
