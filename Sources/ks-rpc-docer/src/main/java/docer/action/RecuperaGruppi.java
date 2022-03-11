package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Group;
import it.kdm.doctoolkit.services.DocerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecuperaGruppi extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.RecuperaGruppi.class);
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		log.info("init method execute");

        Map<String, Object> result = new HashMap<String, Object>();

        List<Map> listaGruppi=new ArrayList<>();

        String idGruppo = inputs.containsKey("IdGruppo")?(String) inputs.get("IdGruppo"):null;
        String idUtente = inputs.containsKey("IdUtente")?(String) inputs.get("IdUtente"):null;

        if(idGruppo!=null && idUtente!=null)throw new ActionRuntimeException("Attenzione i pin in ingresso sono mutuamente esclusivi. Valorizzare solo uno pin! ");
        if(idUtente!=null){throw new ActionRuntimeException("Attenzione, il blocco funziona solomanete con l'id del gruppo ");}

        if(idGruppo!=null) {

            String token = null;
            try {
                token = getToken(inputs);
            } catch (Exception e) {
                log.error("method execute error: {}", e.getMessage());
                e.printStackTrace();
                throw new ActionRuntimeException(e);
            }

            //verifico che il gruppo esiste e che sia enabled (se fosse enabled=false il metodo recuperaGruppo va in eccezione)
            Boolean enabled = true;
            try {
                DocerService.recuperaGruppo(token, idGruppo);
            }catch (Exception e) {
                log.info("RecuperaGruppi - Group not found: {}", e.getMessage());
                result.put("listaGruppi", listaGruppi);
                result.put("userToken", token);
                enabled=false;
            }

            if(enabled) {
                try {

                    boolean fineGerarchia = false;
                    int livellogerarchico = 0;

                    while (!fineGerarchia) {

                        Map mappa = new HashMap();
                        mappa.put("livello", livellogerarchico);
                        mappa.put("idGruppo", idGruppo);
                        listaGruppi.add(mappa);

                        Group GruppoCorrente = DocerService.recuperaGruppo(token, idGruppo);
                        String idGruppoNext = GruppoCorrente.properties.get("PARENT_GROUP_ID");
                        mappa.put("nomeGruppo", GruppoCorrente.properties.get("GROUP_NAME"));
                        if (idGruppoNext == null) {
                            fineGerarchia = true;
                        } else {
                            idGruppo = idGruppoNext;
                            livellogerarchico++;
                        }

                    }

                    //ordinamento gerarchico dalla raadice fino alla foglia (ufficio in input);
                    if (livellogerarchico > 0) {

                        for (int i = 0; i < listaGruppi.size(); i++) {
                            int livelloCorrente = Integer.parseInt(listaGruppi.get(i).get("livello").toString());
                            listaGruppi.get(i).put("livello", livellogerarchico - livelloCorrente);

                        }
                    }

                    result.put("listaGruppi", listaGruppi);
                    result.put("userToken", token);

                } catch (Exception e) {
                    log.error("method execute error: {}", e.getMessage());
                    throw new ActionRuntimeException(e);
                }
            }
        }

        log.info("end method execute");

        return result;
	}

}
