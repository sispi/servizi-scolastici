package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Acl;
import it.kdm.doctoolkit.model.FascicoloPrimario;
import it.kdm.doctoolkit.services.DocerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class settaAclFascicolo extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.settaAclFascicolo.class);
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

        HashMap<String, String> letturaMap = inputs.containsKey("read")?(HashMap<String, String>)inputs.get("read"):null;
        HashMap<String, String> modificaMap = inputs.containsKey("edit")?(HashMap<String, String>)inputs.get("edit"):null;
        HashMap<String, String> fullMap = inputs.containsKey("full")?(HashMap<String, String>)inputs.get("full"):null;

        String modeStr = inputs.containsKey("mode")?(String)inputs.get("mode"):null;



        try {

            String token = getToken(inputs);

            Acl aclRead = null;
            Acl aclEdit = null;
            Acl aclFull = null;

            //crea l'acl read
            if (letturaMap!=null && ((String)letturaMap.get("@structure")).equalsIgnoreCase("role")){
                if (letturaMap.containsKey("identity")) {
                    aclRead = new Acl();
                    aclRead.setUtenteGruppo((String)letturaMap.get("identity"));
                    aclRead.setDiritti("2");
                }
            }

            //crea l'acl edit
            if (modificaMap!=null && ((String)modificaMap.get("@structure")).equalsIgnoreCase("role")){
                if (modificaMap.containsKey("identity")) {
                    aclEdit = new Acl();
                    aclEdit.setUtenteGruppo((String) modificaMap.get("identity"));
                    aclEdit.setDiritti("1");
                }
            }

            //crea l'acl full
            if (fullMap!=null && ((String)fullMap.get("@structure")).equalsIgnoreCase("role")){
                if (fullMap.containsKey("identity")) {
                    aclFull = new Acl();
                    aclFull.setUtenteGruppo((String) fullMap.get("identity"));
                    aclFull.setDiritti("0");
                }
            }

            //lista dei diritti da settare
            ArrayList<Acl> outDiritti = new ArrayList<Acl>();

            if (modeStr.equalsIgnoreCase("remove") || modeStr.equalsIgnoreCase("replace")) {
                //rimuove gli utenti/gruppi passati in input dalla lista delle scl del documento
                List<Acl> aclFasc = DocerService.recuperaACLFascicolo(token,fascicolo.getEnte(),fascicolo.getAoo(),fascicolo.getProgressivo(),fascicolo.getAnno(),fascicolo.getClassifica());
                for (Acl a : aclFasc) {
                    if ((aclRead!=null && a.getUtenteGruppo().equalsIgnoreCase(aclRead.getUtenteGruppo())) ||
                           (aclEdit!=null && a.getUtenteGruppo().equalsIgnoreCase(aclEdit.getUtenteGruppo())) ||
                            (aclFull !=null && a.getUtenteGruppo().equalsIgnoreCase(aclFull.getUtenteGruppo()))) {
                        continue;
                    }
                    outDiritti.add(a);
                }
            }

            if (modeStr.equalsIgnoreCase("replace")) {
                if (aclRead!=null)
                    outDiritti.add(aclRead);
                if (aclEdit!=null)
                    outDiritti.add(aclEdit);
                if (aclFull!=null)
                    outDiritti.add(aclFull);
            }

            if (modeStr.equalsIgnoreCase("add")) {
                List<Acl> aclFasc = DocerService.recuperaACLFascicolo(token,fascicolo.getEnte(),fascicolo.getAoo(),fascicolo.getProgressivo(),fascicolo.getAnno(),fascicolo.getClassifica());
                for (Acl a : aclFasc) {
                    if (aclRead!=null && a.getUtenteGruppo().equalsIgnoreCase(aclRead.getUtenteGruppo())) {
                        if (a.getDiritti()>2)
                            outDiritti.add(aclRead);
                    }
                    if (aclEdit!=null && a.getUtenteGruppo().equalsIgnoreCase(aclEdit.getUtenteGruppo())) {
                        if (a.getDiritti()>1)
                            outDiritti.add(aclEdit);
                    }
                    if (aclFull!=null && a.getUtenteGruppo().equalsIgnoreCase(aclFull.getUtenteGruppo())) {
                        if (a.getDiritti()>0)
                            outDiritti.add(aclFull);
                    }

                    outDiritti.add(a);
                }
                
                if(!aclFasc.contains(aclRead) && aclRead != null ){
                	outDiritti.add(aclRead);
                }

                if(!aclFasc.contains(aclEdit) && aclEdit != null  ){
                	outDiritti.add(aclEdit);
                }

                if(!aclFasc.contains(aclFull) && aclFull != null  ){
                	outDiritti.add(aclFull);
                }
            }

            DocerService.impostaDirittiFascicolo(token, fascicolo, outDiritti);

            HashMap<String, String> aclList = new HashMap<String, String>();
            for (Acl a : outDiritti) {
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
