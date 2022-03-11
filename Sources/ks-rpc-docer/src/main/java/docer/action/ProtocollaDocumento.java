package docer.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.*;
import it.kdm.doctoolkit.services.DocerService;
import it.kdm.doctoolkit.services.ServizioProtocollazione;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProtocollaDocumento extends DocerAction {

    private final static Logger log = LoggerFactory.getLogger(ProtocollaDocumento.class);


//	"document":{"label":"Documento","type":"document","value":""},
//    "oggetto":{"label":"Oggetto","type":"text","value":""},
//    "mittenti":{"label":"Mittenti","type":"soggetto","isCollection":true,"value":""},
//    "destinatari":{"label":"Destinatari","type":"soggetto","isCollection":true,"value":""},
//    "tipofirma":{"label":"Tipo Firma","value":"","type":"textRef","hidden":false},
//    "firmatari":{"label":"Firmatari","type":"soggetto", "isCollection":true,"value":""},
//    "assegnazione":{"label":"Assegnazione","type":"object", "isCollection":true,"value":""},
//    "protocollomittente":{"label":"Protocollo mittente","type":"object","value":""},
//    "userToken":{"label":"Token","value":"systemToken","type":"userToken","hidden":false}

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {
        log.info("init method execute");
        String token=null;

        Map<String, Object> result = new HashMap<String, Object>();
        String oggetto = null;
        List<HashMap<String, Object>> mittentiInput = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> destinatariInput = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> firmatariInput = new ArrayList<HashMap<String, Object>>();
        List<Corrispondente> mittentiList = new ArrayList<Corrispondente>();
        List<Corrispondente> destinatariList = new ArrayList<Corrispondente>();
        List<PersonaFisica> firmatariList = new ArrayList<PersonaFisica>();
        String tipoFirma = null;
        String tipoRichiesta=null;
        HashMap<String, String> documentoMap = null;

        documentoMap = inputs.containsKey("document")?(HashMap<String, String>)inputs.get("document"):null;
        oggetto = inputs.containsKey("oggetto")?(String) inputs.get("oggetto"):null;
        tipoFirma = inputs.containsKey("tipofirma")?(String) inputs.get("tipofirma"):null;
        tipoRichiesta = inputs.containsKey("tipo_richiesta")?(String) inputs.get("tipo_richiesta"):null;
        if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
        if(oggetto == null)throw new ActionRuntimeException("PIN 'oggetto' non trovato nei parametri di input!");
        if(tipoFirma == null)throw new ActionRuntimeException("PIN 'tipofirma' non trovato nei parametri di input!");
        if(tipoRichiesta == null)throw new ActionRuntimeException("PIN 'tipoRichiesta' non trovato nei parametri di input!");
        ServizioProtocollazione.tipoRichestaEunm tipoRichiestaE = null;

        switch (tipoRichiesta) {
            case "entrata":
                tipoRichiestaE = ServizioProtocollazione.tipoRichestaEunm.Entrata;
                break;
            case "uscita":
                tipoRichiestaE = ServizioProtocollazione.tipoRichestaEunm.Uscita;
                break;
            case "interna":
                tipoRichiestaE = ServizioProtocollazione.tipoRichestaEunm.Interna;
                break;

            default:
                throw new ActionRuntimeException("pin tipo_richiesta non supportato valori ammessi: entrata, uscita, interna");
        }








        Documento documento = new Documento();
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
        documento.properties = documentoMap;

        mittentiInput = inputs.containsKey("mittenti")?(List<HashMap<String, Object>>)inputs.get("mittenti"):null;
        if(mittentiInput == null)throw new ActionRuntimeException("Mittenti not found in protocollazione");
        for(HashMap<String, Object> m : mittentiInput){
            String type = (String)m.get("@TYPE");

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
                    am.setBusinessType((String)m.get("@TYPE"));
                    if(m.get("DenominazioneEnte")!=null && !m.get("DenominazioneEnte").equals("")){
                        am.setDenominazione((String) m.get("DenominazioneEnte"));
                    }
                    if(m.get("CodiceAmministrazioneEnte")!=null && !m.get("CodiceAmministrazioneEnte").equals("")){
                        am.setCodiceAmministrazione((String)m.get("CodiceAmministrazioneEnte"));
                    }
                    if(m.get("IndirizzoTelematicoEnte")!=null && !m.get("IndirizzoTelematicoEnte").equals("")){
                        am.setIndirizzoTelematico((String)m.get("IndirizzoTelematicoEnte"));
                    }
                    AOO aoo = new AOO();
                    if(m.get("DenominazioneAoo")!=null && !m.get("DenominazioneAoo").equals("")){
                        aoo.setDenominazione((String)m.get("DenominazioneAoo"));
                    }
                    if(m.get("CodiceAmministrazioneAoo")!=null && !m.get("CodiceAmministrazioneAoo").equals("")){
                        aoo.setCodiceAOO((String)m.get("CodiceAmministrazioneAoo"));
                    }
                    if(m.get("IndirizzoTelematicoAoo")!=null && !m.get("IndirizzoTelematicoAoo").equals("")){
                        aoo.setIndirizzoTelematico((String)m.get("IndirizzoTelematicoAoo"));
                    }
                    am.setAOO(aoo);

                    UO uo = new UO();
                    if(m.get("DenominazioneUo")!=null && !m.get("DenominazioneUo").equals("")){
                        uo.setDenominazione((String)m.get("DenominazioneUo"));
                    }
                    if(m.get("CodiceAmministrazioneUo")!=null && !m.get("CodiceAmministrazioneUo").equals("")){
                        uo.setCodiceUnitaOrganizzativa((String)m.get("CodiceAmministrazioneUo"));
                    }
                    if(m.get("IndirizzoTelematicoUo")!=null && !m.get("IndirizzoTelematicoUo").equals("")){
                        uo.setIndirizzoTelematico((String)m.get("IndirizzoTelematicoUo"));
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

        destinatariInput = inputs.containsKey("destinatari")?(List<HashMap<String, Object>>)inputs.get("destinatari"):null;
        if(destinatariInput == null)throw new ActionRuntimeException("destinatari not found in protocollazione");
        for(HashMap<String, Object> m : destinatariInput){
            String type = (String)m.get("@TYPE");

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
                    am.setBusinessType((String)m.get("@TYPE"));
                    if(m.get("DenominazioneEnte")!=null && !m.get("DenominazioneEnte").equals("")){
                        am.setDenominazione((String) m.get("DenominazioneEnte"));
                    }
                    else{
                        if(m.get("DES_ENTE")!=null && !m.get("DES_ENTE").equals("")){
                            am.setDenominazione((String) m.get("DES_ENTE"));
                        }
                    }
                    if(m.get("CodiceAmministrazioneEnte")!=null && !m.get("CodiceAmministrazioneEnte").equals("")){
                        am.setCodiceAmministrazione((String)m.get("CodiceAmministrazioneEnte"));
                    }
                    else{
                        if(m.get("COD_ENTE")!=null && !m.get("COD_ENTE").equals("")){
                            am.setCodiceAmministrazione((String)m.get("COD_ENTE"));
                        }
                    }
                    if(m.get("IndirizzoTelematicoEnte")!=null && !m.get("IndirizzoTelematicoEnte").equals("")){
                        am.setIndirizzoTelematico((String)m.get("IndirizzoTelematicoEnte"));
                    }
                    AOO aoo = new AOO();
                    if(m.get("DenominazioneAoo")!=null && !m.get("DenominazioneAoo").equals("")){
                        aoo.setDenominazione((String)m.get("DenominazioneAoo"));
                    }
                    else{
                        if(m.get("DES_AOO")!=null && !m.get("DES_AOO").equals("")){
                            aoo.setDenominazione((String)m.get("DES_AOO"));
                        }
                    }
                    if(m.get("CodiceAmministrazioneAoo")!=null && !m.get("CodiceAmministrazioneAoo").equals("")){
                        aoo.setCodiceAOO((String)m.get("CodiceAmministrazioneAoo"));
                    }
                    else{
                        if(m.get("COD_AOO")!=null && !m.get("COD_AOO").equals("")){
                            aoo.setCodiceAOO((String)m.get("COD_AOO"));
                        }
                    }
                    if(m.get("IndirizzoTelematicoAoo")!=null && !m.get("IndirizzoTelematicoAoo").equals("")){
                        aoo.setIndirizzoTelematico((String)m.get("IndirizzoTelematicoAoo"));
                    }
                    am.setAOO(aoo);

                    UO uo = new UO();
                    if(m.get("DenominazioneUo")!=null && !m.get("DenominazioneUo").equals("")){
                        uo.setDenominazione((String)m.get("DenominazioneUo"));
                    }
                    else{
                        if(m.get("UO")!=null && !m.get("UO").equals("")){
                            ObjectMapper mapper = new ObjectMapper();
                            Map<String, Object> uODestinatari = mapper.convertValue(m.get("UO"), Map.class);
                            if (uODestinatari.get("DES_UO")!=null && uODestinatari.get("DES_UO")!=""){
                                uo.setDenominazione((String)uODestinatari.get("DES_UO"));
                            }
                            if (uODestinatari.get("COD_UO")!=null && uODestinatari.get("COD_UO")!=""){
                                uo.setCodiceUnitaOrganizzativa((String)uODestinatari.get("COD_UO"));
                            }
                        }
                    }
                    if(m.get("CodiceAmministrazioneUo")!=null && !m.get("CodiceAmministrazioneUo").equals("")){
                        uo.setCodiceUnitaOrganizzativa((String)m.get("CodiceAmministrazioneUo"));
                    }

                    if(m.get("IndirizzoTelematicoUo")!=null && !m.get("IndirizzoTelematicoUo").equals("")){
                        uo.setIndirizzoTelematico((String)m.get("IndirizzoTelematicoUo"));
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
//				}


        firmatariInput = inputs.containsKey("firmatari")?(List<HashMap<String, Object>>)inputs.get("firmatari"):null;
        if(firmatariInput != null){
            for(HashMap<String, Object> m : firmatariInput){
                String type = (String)m.get("@TYPE");

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


        try {

            token = getToken(inputs);
            ProtocolloMittente protocolloMittente = null;
            HashMap<String, String> protocollom = inputs.containsKey("protocollomittente")?(HashMap<String, String>)inputs.get("protocollomittente"):null;
            if(protocollom!=null && protocollom.containsKey("NUM_PG_MITTENTE") && !protocollom.get("NUM_PG_MITTENTE").equals("")){
                protocolloMittente = new ProtocolloMittente();
                protocolloMittente.setNumero(protocollom.get("NUM_PG_MITTENTE"));
                protocolloMittente.setBusinessType("businessType");
                protocolloMittente.setClassifica(protocollom.get("CLASSIFICA_MITTENTE"));
                protocolloMittente.setCodiceAmministrazione(protocollom.get("COD_ENTE_MITTENTE"));
                protocolloMittente.setCodiceAOO(protocollom.get("COD_AOO_MITTENTE"));
                protocolloMittente.setData(protocollom.get("DATA_PG_MITTENTE"));
                protocolloMittente.setFascicolo(protocollom.get("FASCICOLO_MITTENTE"));
            }

            /*
                if(documento.properties!=null && documento.properties.containsKey("TIPO_FIRMA") && !documento.properties.get("TIPO_FIRMA").equals("")){
                tipoFirma = documento.properties.get("TIPO_FIRMA");
            }*/


            ServizioProtocollazione.protocollaUnitaDocumentaria(token,
                    documento.getDocNum(),
                    oggetto,
                    tipoRichiestaE ,
                    true,
                    protocolloMittente,
                    firmatariList,
                    tipoFirma, null, null, mittentiList, destinatariList, null);
            //ServizioProtocollazione.protocollaUnitaDocumentaria(token, documento.getDocNum(), oggetto,tipoRichiesta , true, null, null, "NF", null, null, mittentiList, destinatariList, null);
            documento = DocerService.recuperaProfiloDocumento(token, documento.getDocNum());
            documento.setProperty("@structure", "document");
            result.put("document", documentToHashmap(documento));
            result.put("userToken", token);
        } catch (Exception e) {
            if(e.getMessage().contains("STATO_ARCHIVISTICO protocollato (3)")){//TODO
                try{
                    documento = DocerService.recuperaProfiloDocumento(token, documento.getDocNum());
                    documento.setProperty("@structure", "document");
                    result.put("document", documentToHashmap(documento));
                    result.put("userToken", token);
                    return result;
                }catch (Exception e1){
                    log.error("method execute error:{}",e1.getMessage());
                    e1.printStackTrace();
                    throw new ActionRuntimeException(e1);
                }
            }
            log.error("method execute error:{}",e.getMessage());
            e.printStackTrace();
            throw new ActionRuntimeException(e);
        }finally {

        }
        log.info("end method execute");
        return result;
    }

}
