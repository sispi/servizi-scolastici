package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.User;
import it.kdm.doctoolkit.services.DocerService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class recuperaInfoUtente extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.recuperaInfoUtente.class);
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		log.info("init method execute");

        Map<String, Object> result = new HashMap<String, Object>();

        String userId = inputs.containsKey("user")?(String)inputs.get("user"):null;
		if (userId == null)
            throw new ActionRuntimeException("PIN 'user' non trovato nei parametri di input!");



        try {

            String token = getToken(inputs);
            User u = DocerService.recuperaUtente(token,userId);

            HashMap<String, String> gen = new HashMap<String, String>();
            gen.put("@structure","role");
            gen.put("identity",userId);
            gen.put("IndirizzoTelematico",u.getEmail());
            if (u.getNomeCompleto() != null && !"".equalsIgnoreCase(u.getNomeCompleto()))
                gen.put("denominazione",u.getNomeCompleto());
            else
                gen.put("denominazione",userId);


            result.put("userInfo", gen);
            result.put("userToken", token);

        } catch (Exception e) {
			log.error("method execute error: {}",e.getMessage());
			throw new ActionRuntimeException(e);
		}

        log.info("end method execute");

        return result;
	}

}
