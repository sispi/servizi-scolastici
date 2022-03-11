package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Acl;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by enrico on 01/10/15.
 */
public class getAclDocumento extends DocerAction{

    private final static Logger log = LoggerFactory.getLogger(docer.action.getAclDocumento.class);
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


        try {

            String token = getToken(inputs);

            List<Acl> aclDoc = DocerService.recuperaACLDocumento(token, documento.getDocNum());


            HashMap<String, String> aclList = new HashMap<String, String>();

            for (Acl a : aclDoc) {
                aclList.put(a.getUtenteGruppo(), String.valueOf(a.getDiritti()));
            }

            result.put("aclList", aclList);
            result.put("userToken", token);

        } catch (Exception e) {
            log.error("method execute error: {}",e.getMessage());
            throw new ActionRuntimeException(e);
        }

        log.info("end method execute");

        return result;
    }


}
