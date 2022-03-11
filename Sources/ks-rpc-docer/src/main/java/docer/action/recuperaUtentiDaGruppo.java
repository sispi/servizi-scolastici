package docer.action;

import docer.exception.ActionRuntimeException;
import java.util.*;
import keysuite.cache.ClientCache;
import keysuite.docer.client.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class recuperaUtentiDaGruppo extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.recuperaUtentiDaGruppo.class);
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		log.info("init method execute");

        Map<String, Object> result = new HashMap<String, Object>();

		HashMap<String, String> map = inputs.containsKey("group")?(HashMap<String, String>)inputs.get("group"):null;
		if (map == null)
            throw new ActionRuntimeException("PIN 'group' non trovato nei parametri di input!");

        String groupId = "";
        if (!map.containsKey("identity"))
            throw new ActionRuntimeException("Input PIN 'group' non valido. Impossibile recuperare la proprietà 'identity'.");

        groupId = map.get("identity");

        try {

            List<HashMap<String, String>> listaUser = new ArrayList<HashMap<String, String>>();


            String token = getToken(inputs);

            Group group = ClientCache.getInstance().getGroup(getAoo(), groupId);

            Collection<keysuite.docer.client.User> users = ClientCache.getInstance().getUsersInGroup(getAoo(),groupId);

            for (keysuite.docer.client.User user : users) {
                HashMap<String, String> gen = new HashMap<String, String>();
                gen.put("@structure","role");
                gen.put("identity",user.getUserName());
                gen.put("IndirizzoTelematico",user.getEmail());
                gen.put("denominazione",user.getFullName());

                listaUser.add(gen);
            }

            /*String[] userNames = DocerService.recuperaUtentiDaGruppo(token,groupId);

            for (String userName : userNames) {
                User u = DocerService.recuperaUtente(token,userName);
                HashMap<String, String> gen = new HashMap<String, String>();
                gen.put("@structure","role");
                gen.put("identity",userName);
                gen.put("IndirizzoTelematico",u.getEmail());
                if (u.getNomeCompleto() != null && !"".equalsIgnoreCase(u.getNomeCompleto()))
                    gen.put("denominazione",u.getNomeCompleto());
                else
                    gen.put("denominazione",userName);

                listaUser.add(gen);
            }*/

            List<HashMap<String, String>> delta = new ArrayList<HashMap<String, String>>(listaUser);

            HashMap<String, String> first = null;

            if (listaUser.size()>0) {
                first = listaUser.get(0);
                delta.remove(first);
            }

            result.put("first", first);
            result.put("delta", delta);
            result.put("results", listaUser);
            result.put("userToken", token);

        } catch (Exception e) {
			log.error("method execute error: {}",e.getMessage());
			throw new ActionRuntimeException(e);
		}

        log.info("end method execute");

        return result;
	}

}
