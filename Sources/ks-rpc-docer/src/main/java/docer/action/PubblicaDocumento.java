package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.model.PubblicaDocumentoSdk;
import it.kdm.doctoolkit.services.DocerService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by enrico on 27/06/17.
 */
public class PubblicaDocumento extends DocerAction {

    private final static Logger log = LoggerFactory.getLogger(docer.action.PubblicaDocumento.class);

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {

        log.info("init method execute");
        String token = null;
        HashMap<String, String> documento = null;
        String numero = null;
        HashMap<String, String> registro = null;
        String oggetto = null;
        String dataInizio = null;
        String dataFine = null;
        Map<String, Object> result = new HashMap<String, Object>();

        documento = inputs.containsKey("document")?(HashMap<String, String>)inputs.get("document"):null;
        numero = inputs.containsKey("numero")?(String) inputs.get("numero"):null;
        registro = inputs.containsKey("registro")?(HashMap<String, String>)inputs.get("registro"):null;
        oggetto = inputs.containsKey("oggetto")?(String) inputs.get("oggetto"):null;
        dataInizio = inputs.containsKey("dataInizio")?(String) inputs.get("dataInizio"):null;
        dataFine = inputs.containsKey("dataFine")?(String) inputs.get("dataFine"):null;


        if(documento == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
        if(numero == null)throw new ActionRuntimeException("PIN 'numero' non trovato nei parametri di input!");
        if(registro == null)throw new ActionRuntimeException("PIN 'registro' non trovato nei parametri di input!");
        if(oggetto == null)throw new ActionRuntimeException("PIN 'oggetto' non trovato nei parametri di input!");
        if(dataInizio == null)throw new ActionRuntimeException("PIN 'dataInizio' non trovato nei parametri di input!");
        if(dataFine == null)throw new ActionRuntimeException("PIN 'dataFine' non trovato nei parametri di input!");


        try {
            token = getToken(inputs);

            PubblicaDocumentoSdk pubblicaDocumentoSdk = new PubblicaDocumentoSdk();
            pubblicaDocumentoSdk.setNumero(numero);
            pubblicaDocumentoSdk.setOggetto(oggetto);
            pubblicaDocumentoSdk.setRegistro(registro.get("IDRegistro"));
            pubblicaDocumentoSdk.setDataFine(dataFine);
            pubblicaDocumentoSdk.setDataInizio(dataInizio);

            DocerService.pubblicaDocumento(token, documento.get("DOCNUM"),pubblicaDocumentoSdk);

            Documento doc = DocerService.recuperaProfiloDocumento(token,documento.get("DOCNUM"));

            result.put("document", documentToHashmap(doc));
            result.put("userToken", token);

        }catch (Exception e) {
            log.error("method execute error:{}",e.getMessage());
            e.printStackTrace();
            throw new ActionRuntimeException(e);
        }
        log.info("end method execute");

        return result;
    }

}
