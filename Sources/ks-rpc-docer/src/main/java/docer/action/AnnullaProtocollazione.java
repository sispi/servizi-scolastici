package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.model.protocollazione.AnnullaProtocolloOBJ;
import it.kdm.doctoolkit.services.DocerService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by enrico on 19/06/17.
 */
public class AnnullaProtocollazione extends DocerAction  {

    private final static Logger log = LoggerFactory.getLogger(docer.action.AnnullaProtocollazione.class);

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {
        log.info("init method execute");
        String token = null;

        Map<String, Object> result = new HashMap<String, Object>();
        String docnumProtocollato = null;
        String motivoAnnullamento = null;
        String docnumAnnullamento = null;

        docnumProtocollato = inputs.containsKey("docnum")?(String)inputs.get("docnum"):null;
        motivoAnnullamento = inputs.containsKey("motivoAnnullamento")?(String)inputs.get("motivoAnnullamento"):null;
        docnumAnnullamento = inputs.containsKey("docnumAnnullamento")?(String)inputs.get("docnumAnnullamento"):null;

        if(docnumProtocollato == null)throw new ActionRuntimeException("PIN 'docnum' non trovato nei parametri di input!");
        if(motivoAnnullamento == null)throw new ActionRuntimeException("PIN 'motivoAnnullamento' non trovato nei parametri di input!");
        if(docnumAnnullamento == null)throw new ActionRuntimeException("PIN 'docnumAnnullamento' non trovato nei parametri di input!");

        try {
            token = getToken(inputs);
            AnnullaProtocolloOBJ annullaProtocolloOBJ = new AnnullaProtocolloOBJ();
            annullaProtocolloOBJ.setDocNum(docnumProtocollato);
            annullaProtocolloOBJ.setMotivoAnnullaMentoPG(motivoAnnullamento);
            annullaProtocolloOBJ.setProvvedimentoAnnullaPG(docnumAnnullamento);

            DocerService.annullamentoProtocollazione(token,annullaProtocolloOBJ);
            Documento documento = DocerService.recuperaProfiloDocumento(token, docnumProtocollato);

            result.put("document", documentToHashmap(documento));
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
