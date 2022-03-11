package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.*;
import it.kdm.doctoolkit.services.DocerService;
import it.kdm.doctoolkit.services.ServizioRegistrazione;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistraDocumento extends DocerAction {
    private final static Logger log = LoggerFactory.getLogger(docer.action.RegistraDocumento.class);
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {
        log.info("init method execute");
        Map<String, Object> result = new HashMap<String, Object>();
        String token=null;
        String oggetto = null;
        HashMap<String, ?> documentoMap = null;
        HashMap<String, String> registroMap = null;

        List<HashMap<String, Object>> mittentiInput = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> destinatariInput = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> firmatariInput = new ArrayList<HashMap<String, Object>>();

        List<Corrispondente> mittentiList = new ArrayList<Corrispondente>();
        List<Corrispondente> destinatariList = new ArrayList<Corrispondente>();
        List<PersonaFisica> firmatariList = new ArrayList<PersonaFisica>();
        String tipoFirma = null;

        documentoMap = inputs.containsKey("document")?(HashMap<String, ?>)inputs.get("document"):null;
        registroMap = inputs.containsKey("registro")?(HashMap<String, String>) inputs.get("registro"):null;
        oggetto = inputs.containsKey("oggetto")?(String) inputs.get("oggetto"):null;
        tipoFirma = inputs.containsKey("tipofirma")?(String) inputs.get("tipofirma"):null;






        if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
        if(registroMap == null)throw new ActionRuntimeException("PIN 'registro' non trovato nei parametri di input!");
        if(oggetto == null)throw new ActionRuntimeException("PIN 'oggetto' non trovato nei parametri di input!");
        if(tipoFirma == null)throw new ActionRuntimeException("PIN 'tipofirma' non trovato nei parametri di input!");




        mittentiInput = inputs.containsKey("mittenti")?(List<HashMap<String, Object>>)inputs.get("mittenti"):null;
        //if(mittentiInput == null)throw new ActionRuntimeException("Mittenti not found in protocollazione");
        if(mittentiInput != null) {
            for (HashMap<String, Object> m : mittentiInput) {
                String type = (String) m.get("@TYPE");

                if (type == null) {
                    type = (String) m.get("@type");
                }

                switch (type) {
                    case "PersonaFisica":
                        PersonaFisica pf = new PersonaFisica();
                        pf.importProperties(m);
                        mittentiList.add(pf);
                        break;
                    case "PersonaGiuridica":
                        PersonaGiuridica pg = new PersonaGiuridica();
                        pg.importProperties(m);
                        mittentiList.add(pg);
                        break;
                    case "Amministrazione":
                        Amministrazione am = new Amministrazione();
                        am.setBusinessType((String) m.get("@TYPE"));
                        if (m.get("CodiceAmministrazioneEnte") != null && !m.get("CodiceAmministrazioneEnte").equals("")) {
                            am.setCodiceAmministrazione((String) m.get("CodiceAmministrazioneEnte"));
                        }
                        if (m.get("CodiceAmministrazioneEnte") != null && !m.get("CodiceAmministrazioneEnte").equals("")) {
                            am.setCodiceAmministrazione((String) m.get("CodiceAmministrazioneEnte"));
                        }
                        if (m.get("IndirizzoTelematicoEnte") != null && !m.get("IndirizzoTelematicoEnte").equals("")) {

                        }
                        AOO aoo = new AOO();
                        if (m.get("DenominazioneAoo") != null && !m.get("DenominazioneAoo").equals("")) {
                            aoo.setDenominazione((String) m.get("DenominazioneAoo"));
                        }
                        if (m.get("CodiceAmministrazioneAoo") != null && !m.get("CodiceAmministrazioneAoo").equals("")) {
                            aoo.setCodiceAOO((String) m.get("CodiceAmministrazioneAoo"));
                        }
                        if (m.get("IndirizzoTelematicoAoo") != null && !m.get("IndirizzoTelematicoAoo").equals("")) {
                            aoo.setIndirizzoTelematico((String) m.get("IndirizzoTelematicoAoo"));
                        }
                        am.setAOO(aoo);

                        UO uo = new UO();
                        if (m.get("DenominazioneUo") != null && !m.get("DenominazioneUo").equals("")) {
                            uo.setDenominazione((String) m.get("DenominazioneUo"));
                        }
                        if (m.get("CodiceAmministrazioneUo") != null && !m.get("CodiceAmministrazioneUo").equals("")) {
                            uo.setCodiceUnitaOrganizzativa((String) m.get("CodiceAmministrazioneUo"));
                        }
                        if (m.get("IndirizzoTelematicoUo") != null && !m.get("IndirizzoTelematicoUo").equals("")) {
                            //TBD
                        }
                        am.setUnitaOrganizzativa(uo);
                        mittentiList.add(am);
				/*
				 * CodiceAmministrazioneEnte,IndirizzoTelematicoEnte,DenominazioneAoo,CodiceAmministrazioneAoo,IndirizzoTelematicoAoo
				 * DenominazioneUo,CodiceAmministrazioneUo,IndirizzoTelematicoUo
				 */
                        break;
                    default:
                        throw new ActionRuntimeException("Mittenti @type mismatch");
                }
            }
        }

        destinatariInput = inputs.containsKey("destinatari")?(List<HashMap<String, Object>>)inputs.get("destinatari"):null;
        //if(destinatariInput == null)throw new ActionRuntimeException("destinatari not found in protocollazione");
        if(destinatariInput != null) {
            for (HashMap<String, Object> m : destinatariInput) {
                String type = (String) m.get("@TYPE");
                if (type == null) {
                    type = (String) m.get("@type");
                }
                switch (type) {
                    case "PersonaFisica":
                        PersonaFisica pf = new PersonaFisica();
                        pf.importProperties(m);
                        destinatariList.add(pf);
                        break;
                    case "PersonaGiuridica":
                        PersonaGiuridica pg = new PersonaGiuridica();
                        pg.importProperties(m);
                        destinatariList.add(pg);
                        break;
                    case "Amministrazione":
                        Amministrazione am = new Amministrazione();
                        am.setBusinessType((String) m.get("@TYPE"));
                        if (m.get("CodiceAmministrazioneEnte") != null && !m.get("CodiceAmministrazioneEnte").equals("")) {
                            am.setCodiceAmministrazione((String) m.get("CodiceAmministrazioneEnte"));
                        }
                        if (m.get("CodiceAmministrazioneEnte") != null && !m.get("CodiceAmministrazioneEnte").equals("")) {
                            am.setCodiceAmministrazione((String) m.get("CodiceAmministrazioneEnte"));
                        }
                        if (m.get("IndirizzoTelematicoEnte") != null && !m.get("IndirizzoTelematicoEnte").equals("")) {

                        }
                        AOO aoo = new AOO();
                        if (m.get("DenominazioneAoo") != null && !m.get("DenominazioneAoo").equals("")) {
                            aoo.setDenominazione((String) m.get("DenominazioneAoo"));
                        }
                        if (m.get("CodiceAmministrazioneAoo") != null && !m.get("CodiceAmministrazioneAoo").equals("")) {
                            aoo.setCodiceAOO((String) m.get("CodiceAmministrazioneAoo"));
                        }
                        if (m.get("IndirizzoTelematicoAoo") != null && !m.get("IndirizzoTelematicoAoo").equals("")) {
                            aoo.setIndirizzoTelematico((String) m.get("IndirizzoTelematicoAoo"));
                        }
                        am.setAOO(aoo);

                        UO uo = new UO();
                        if (m.get("DenominazioneUo") != null && !m.get("DenominazioneUo").equals("")) {
                            uo.setDenominazione((String) m.get("DenominazioneUo"));
                        }
                        if (m.get("CodiceAmministrazioneUo") != null && !m.get("CodiceAmministrazioneUo").equals("")) {
                            uo.setCodiceUnitaOrganizzativa((String) m.get("CodiceAmministrazioneUo"));
                        }
                        if (m.get("IndirizzoTelematicoUo") != null && !m.get("IndirizzoTelematicoUo").equals("")) {
                            //TBD
                        }
                        am.setUnitaOrganizzativa(uo);
                        destinatariList.add(am);
						/*
						 * CodiceAmministrazioneEnte,IndirizzoTelematicoEnte,DenominazioneAoo,CodiceAmministrazioneAoo,IndirizzoTelematicoAoo
						 * DenominazioneUo,CodiceAmministrazioneUo,IndirizzoTelematicoUo
						 */
                        break;
                    default:
                        throw new ActionRuntimeException("destinatari @type mismatch");
                }
            }
        }


        firmatariInput = inputs.containsKey("firmatario")?(List<HashMap<String, Object>>)inputs.get("firmatario"):null;
        if(firmatariInput != null){
            for(HashMap<String, Object> m : firmatariInput){
                String type = (String)m.get("@TYPE");
                if(type==null){
                    type = (String)m.get("@type");
                }
                switch (type) {
                    case "PersonaFisica":
                        PersonaFisica pf = new PersonaFisica();
                        pf.importProperties(m);
                        firmatariList.add(pf);
                        break;
                    default:
                        throw new ActionRuntimeException("firmatari @type mismatch");
                }
            }
        }

        //nella registrazione il tipo richiesta è sempre "Interna"
        ServizioRegistrazione.tipoRichestaEnum tipoRichiestaE = null;
        tipoRichiestaE = ServizioRegistrazione.tipoRichestaEnum.Interna;


        Documento documento = new Documento();
        Registro registro = new Registro();
        documentoMap.remove("TaskName");
        documentoMap.remove("filePath");
        documentoMap.remove("GroupId");
        documentoMap.remove("comment");
        documentoMap.remove("outcome");
        documentoMap.remove("NodeName");
        documentoMap.remove("ActorId");
        documentoMap.remove("@structure");

        if(documentoMap.containsKey("userToken")){
            documentoMap.remove("userToken");
        }

        documento.properties = (HashMap<String, String>)documentoMap;
        registro.properties = registroMap;

        try {
            token = getToken(inputs);
            registro = ServizioRegistrazione.registraUnitaDocumentaria(token, documento.getDocNum(), oggetto, registro.getIDRegistro(), true, firmatariList, tipoFirma ,mittentiList , destinatariList, null, tipoRichiestaE);
            documento = DocerService.recuperaProfiloDocumento(token, documento.getDocNum());
            documento.setProperty("@structure", "document");
            result.put("document", documentToHashmap(documento));
            result.put("userToken", token);
        } catch (Exception e) {
            log.error("method execute error:{}",e.getMessage());
            throw new ActionRuntimeException(e);
        }

        log.info("end method execute");
        return result;
    }

}
