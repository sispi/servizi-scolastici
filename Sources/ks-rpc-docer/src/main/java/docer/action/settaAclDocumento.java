package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Acl;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class settaAclDocumento extends DocerAction {
    private String SPLIT_ID = ",";
	private final static Logger log = LoggerFactory.getLogger(docer.action.settaAclDocumento.class);

    private static Object syncObj= new Object();

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		log.info("init method execute");

        HashMap<String, String> documentoMap = inputs.containsKey("document")?(HashMap<String, String>)inputs.get("document"):null;
        if (documentoMap == null)
            throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");

        String docNum = documentoMap.get("DOCNUM");
        Object obj ;
      /*  synchronized (setDoc) {
            log.debug("1 presente in setDoc");
            obj=setDoc.get(docNum);
            if(obj==null){
                log.debug("2 presente in retrieve obj setDoc");
                obj=new Object();
                setDoc.put(docNum,obj);
            }
        }*/




        Map<String, Object> result = new HashMap<String, Object>();


        Documento documento = new Documento();


        documento.copyFrom(documentoMap);

        HashMap<String, String> letturaMap = inputs.containsKey("read")?(HashMap<String, String>)inputs.get("read"):null;
        HashMap<String, String> modificaMap = inputs.containsKey("edit")?(HashMap<String, String>)inputs.get("edit"):null;
        HashMap<String, String> fullMap = inputs.containsKey("full")?(HashMap<String, String>)inputs.get("full"):null;

        String modeStr = inputs.containsKey("mode")?(String)inputs.get("mode"):null;



        try {

            String token = getToken(inputs);

            List<Acl> aclRead = new ArrayList<>();
            List<Acl> aclEdit = new ArrayList<>();
            List<Acl> aclFull = new ArrayList<>();

            //crea l'acl read
            if (letturaMap!=null && ((String)letturaMap.get("@structure")).equalsIgnoreCase("role")){
                if (letturaMap.containsKey("identity")) {
                    aclRead = splitIdentity(letturaMap.get("identity"), "2");
                }
            }

            //crea l'acl edit
            if (modificaMap!=null && ((String)modificaMap.get("@structure")).equalsIgnoreCase("role")){
                if (modificaMap.containsKey("identity")) {
                    aclEdit = splitIdentity(modificaMap.get("identity"), "1");
                }
            }

            //crea l'acl full
            if (fullMap!=null && ((String)fullMap.get("@structure")).equalsIgnoreCase("role")){
                if (fullMap.containsKey("identity")) {
                    aclFull = splitIdentity(fullMap.get("identity"), "0");
                }
            }

            //lista dei diritti da settare
            ArrayList<Acl> outDiritti = new ArrayList<Acl>();
            ArrayList<String> listRemove = new ArrayList<String>();

            synchronized (syncObj) {
                log.debug("3 presente in synchronized (obj)");
            if (modeStr.equalsIgnoreCase("remove") || modeStr.equalsIgnoreCase("replace")) {
                //rimuove gli utenti/gruppi passati in input dalla lista delle scl del documento
                List<Acl> aclDoc = DocerService.recuperaACLDocumento(token,documento.getDocNum());

                for(Acl aclrs:aclRead) {
                       aclDoc.remove(aclrs);
                }
                for(Acl aclws:aclEdit) {
                    aclDoc.remove(aclws);
                }

                for(Acl aclfs:aclFull) {
                    aclDoc.remove(aclfs);
                }

                for(Acl tmp:aclDoc){
                    Acl acl = new Acl();
                    acl.setUtenteGruppo(tmp.getUtenteGruppo());
                    acl.setDiritti(tmp.getDiritti()+"");
                    outDiritti.add(acl);
                }
               /* for (Acl a : aclDoc) {
                    for(Acl aclrs:aclRead) {
                        if (aclrs != null && a.getUtenteGruppo().equalsIgnoreCase(aclrs.getUtenteGruppo()) && listRemove.contains(aclrs.getUtenteGruppo()))  {
                            listRemove.add(aclrs.getUtenteGruppo());
                            continue;
                        }
                        outDiritti.add(aclrs);

                    }

                    for(Acl aclws:aclEdit) {
                        if (aclws != null && a.getUtenteGruppo().equalsIgnoreCase(aclws.getUtenteGruppo()) && listRemove.contains(aclws.getUtenteGruppo())){
                            listRemove.add(aclws.getUtenteGruppo());
                            continue;
                        }
                        outDiritti.add(aclws);

                    }

                    for(Acl aclfs:aclFull) {
                        if (aclfs != null && a.getUtenteGruppo().equalsIgnoreCase(aclfs.getUtenteGruppo()) && listRemove.contains(aclfs.getUtenteGruppo())) {
                            listRemove.add(aclfs.getUtenteGruppo());
                            continue;
                        }
                        outDiritti.add(aclfs);recuperaACLDocumento

                    }


                }*/
            }

            if (modeStr.equalsIgnoreCase("replace")) {
                for(Acl tmp: aclRead){
                    outDiritti.add(tmp);
                }
                for(Acl tmp : aclEdit){
                    outDiritti.add(tmp);
                }
                for(Acl tmp:aclFull){
                    outDiritti.add(tmp);
                }
            }

            if (modeStr.equalsIgnoreCase("add")) {
                List<Acl> aclDoc = DocerService.recuperaACLDocumento(token,documento.getDocNum());
                for (Acl a : aclDoc) {
                    for(Acl tmp:aclRead) {
                        if (tmp != null && a.getUtenteGruppo().equalsIgnoreCase(tmp.getUtenteGruppo())) {
                            if (a.getDiritti() > 2)
                                outDiritti.add(tmp);
                        }
                    }
                    for(Acl tmp:aclEdit) {
                        if (tmp != null && a.getUtenteGruppo().equalsIgnoreCase(tmp.getUtenteGruppo())) {
                            if (a.getDiritti() > 1)
                                outDiritti.add(tmp);
                        }
                    }
                    for(Acl tmp:aclFull) {
                        if (tmp != null && a.getUtenteGruppo().equalsIgnoreCase(tmp.getUtenteGruppo())) {
                            if (a.getDiritti() > 0)
                                outDiritti.add(tmp);
                        }
                    }

                    outDiritti.add(a);
                }

                for(Acl tmp:aclRead) {
                    if (!aclDoc.contains(tmp) && tmp != null) {
                        outDiritti.add(tmp);
                    }
                }

                for(Acl tmp:aclEdit) {
                    if (!aclDoc.contains(tmp) && tmp != null) {
                        outDiritti.add(tmp);
                    }
                }

                for(Acl tmp:aclFull) {
                    if (!aclDoc.contains(tmp) && tmp != null) {
                        outDiritti.add(tmp);
                    }
                }

            }

/*
            ArrayList<Acl> outDiritti2 = new ArrayList<Acl>();
            for(Acl tmp:outDiritti){
                outDiritti2.add(tmp);
                if(tmp.getUtenteGruppo().contains(",")){
                    String [] identity = StringUtils.split(tmp.getUtenteGruppo(),",");
                    tmp.setUtenteGruppo(identity[0]);
                    for(int i = 1; i<identity.length;i++){
                        Acl newAcl = new Acl();
                        newAcl.setUtenteGruppo(identity[i]);
                        newAcl.setDiritti(tmp.getDiritti()+"");
                        outDiritti2.add(newAcl);
                    }
                }



            }*/


            DocerService.impostaDirittiDocumento(token, documento.getDocNum(), outDiritti);
            } //end syncObj


           /* synchronized (setDoc){
                log.debug("4 presente in remove(docNum)");
                setDoc.remove(docNum);
            }*/

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



    private List<Acl> splitIdentity(String identity, String aclValue){
        List<Acl> aclList = new ArrayList<>();
        if(StringUtils.contains(identity,SPLIT_ID)){
            String[] identityS = StringUtils.split(identity,SPLIT_ID);
            for(String tmp:identityS) {
                Acl aclReadS = new Acl();
                aclReadS.setUtenteGruppo(tmp);
                aclReadS.setDiritti(aclValue);
                aclList.add(aclReadS);
            }
        }else{
            Acl aclReadNoSplit = new Acl();
            aclReadNoSplit.setUtenteGruppo(identity);
            aclReadNoSplit.setDiritti(aclValue);
            aclList.add(aclReadNoSplit);
        }
    return aclList;
    }



}
