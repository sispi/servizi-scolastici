package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Acl;
import it.kdm.doctoolkit.model.FascicoloPrimario;
import it.kdm.doctoolkit.services.DocerService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by enrico on 01/10/15.
 */

public class getAclFascicolo extends DocerAction {

    private final static Logger log = LoggerFactory.getLogger(docer.action.getAclFascicolo.class);
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
        log.info("init method execute");

        Map<String, Object> result = new HashMap<String, Object>();


        FascicoloPrimario fascicolo = new FascicoloPrimario();

        HashMap<String, String> fascicoloMap = inputs.containsKey("fascicolo")?(HashMap<String, String>)inputs.get("fascicolo"):null;
        if (fascicoloMap == null)
            throw new ActionRuntimeException("PIN 'fascicolo' non trovato nei parametri di input!");

        fascicolo.copyFrom(fascicoloMap);

        try {

            String token = getToken(inputs);

            List<Acl> aclFasc = DocerService.recuperaACLFascicolo(token, fascicolo.getEnte(), fascicolo.getAoo(), fascicolo.getProgressivo(), fascicolo.getAnno(), fascicolo.getClassifica());

            HashMap<String, String> aclList = new HashMap<String, String>();
            for (Acl a : aclFasc) {
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