package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.model.ProtoDocerServicesObj;
import it.kdm.doctoolkit.services.DocerService;
import it.kdm.doctoolkit.services.ServizioProtocollazione;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProtocollaDocumentoDoc extends DocerAction {

    private final static Logger log = LoggerFactory.getLogger(docer.action.ProtocollaDocumentoDoc.class);


/*    "document":{"label":"Documento","type":"document","value":""},
            "oggetto":{"label":"Oggetto","type":"textRef","value":""},
            "tipofirma":{"label":"Tipo Firma","value":"","type":"textRef","hidden":false},
            "tipo_richiesta":{"label":"Tipo richiesta","value":"","type":"textRef","hidden":false},
            "numero_protocollazione":{"label":"Numero protocollazione","value":"","type":"textRef","hidden":false},
            "data_protocollazione":{"label":"Data protocollazione","value":"","type":"textRef","hidden":false},
            "oggetto_protocollazione":{"label":"Oggetto protocollazione","value":"","type":"textRef","hidden":false},
            "registro_protocollazione":{"label":"Registro protocollazione","value":"","type":"textRef","hidden":false},
            "tipo_firma":{"label":"Tipo firma","value":"","type":"textRef","hidden":false},
            "userToken":{"label":"Token","value":"systemToken","type":"userToken","hidden":false}*/

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {
        Map<String, Object> result = new HashMap<String, Object>();
        try{
        log.info("init method execute");
        String token=null;


        String oggetto = null;
        String numero_protocollazione=null;
        String data_protocollazione=null;
        String registro_protocollazione=null;
        String codEnte=null;
        String codAoo=null;
        String tipoFirma = null;
        String tipoRichiesta=null;
        HashMap<String, String> documentoMap = null;
        String docnum=null;

        documentoMap = inputs.containsKey("document")?(HashMap<String, String>)inputs.get("document"):null;
        oggetto = inputs.containsKey("oggetto")?(String) inputs.get("oggetto"):null;
        tipoFirma = inputs.containsKey("tipofirma")?(String) inputs.get("tipofirma"):null;
        numero_protocollazione = inputs.containsKey("numero_protocollazione")?(String) inputs.get("numero_protocollazione"):null;
        data_protocollazione = inputs.containsKey("data_protocollazione")?(String) inputs.get("data_protocollazione"):null;
        registro_protocollazione = inputs.containsKey("registro_protocollazione")?(String) inputs.get("registro_protocollazione"):null;
        tipoRichiesta = inputs.containsKey("tipo_richiesta")?(String) inputs.get("tipo_richiesta"):null;
        if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");

        codEnte = documentoMap.containsKey("COD_ENTE")?(String) documentoMap.get("COD_ENTE"):null;
        codAoo = documentoMap.containsKey("COD_AOO")?(String) documentoMap.get("COD_AOO"):null;
        docnum = documentoMap.containsKey("DOCNUM")?(String) documentoMap.get("DOCNUM"):null;
        if(codEnte == null)throw new ActionRuntimeException("PIN 'document --> COD_ENTE' non trovato nei parametri di input!");
        if(codAoo == null)throw new ActionRuntimeException("PIN 'document --> COD_AOO' non trovato nei parametri di input!");
        if(docnum == null)throw new ActionRuntimeException("PIN 'document --> DOCNUM' non trovato nei parametri di input!");

        if(oggetto == null)throw new ActionRuntimeException("PIN 'oggetto' non trovato nei parametri di input!");
        if(tipoFirma == null)throw new ActionRuntimeException("PIN 'tipofirma' non trovato nei parametri di input!");
        if(tipoRichiesta == null)throw new ActionRuntimeException("PIN 'tipoRichiesta' non trovato nei parametri di input!");
        ServizioProtocollazione.tipoRichestaEunm tipoRichiestaE = null;

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


            token = getToken(inputs);
            ProtoDocerServicesObj protoDocerServicesObj = new ProtoDocerServicesObj();
            protoDocerServicesObj.setDataPG(data_protocollazione);
                    protoDocerServicesObj.setNumPG(numero_protocollazione);
            protoDocerServicesObj.setOggettoPG(oggetto);
                    protoDocerServicesObj.setRegistroPG(registro_protocollazione);
            protoDocerServicesObj.setTipoFirma(tipoFirma);
            protoDocerServicesObj.setCodEnte(codEnte);
            protoDocerServicesObj.setCodAoo(codAoo);
            DocerService.protocollaDocumentoDocerServices(token,docnum,tipoRichiesta,protoDocerServicesObj);
            Documento doc = DocerService.recuperaProfiloDocumento(token, docnum);
            result.put("document", doc.toFlowObject());
            result.put("userToken", token);
        } catch (Exception e) {
            log.error("method execute error ProtocollaDocumentoDoc:{}",e.getMessage());
            e.printStackTrace();
            throw new ActionRuntimeException(e);
        }
        log.info("end method execute");
        return result;
    }






       /* mittentiInput = inputs.containsKey("mittenti")?(List<HashMap<String, Object>>)inputs.get("mittenti"):null;
        if(mittentiInput == null)throw new ActionRuntimeException("Mittenti not found in protocollazione");
        for(HashMap<String,Object> m : mittentiInput){
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
					*//*
					 * CodiceAmministrazioneEnte,IndirizzoTelematicoEnte,DenominazioneAoo,CodiceAmministrazioneAoo,IndirizzoTelematicoAoo
					 * DenominazioneUo,CodiceAmministrazioneUo,IndirizzoTelematicoUo
					 *//*
                    break;
                default:
                    throw new ActionRuntimeException("Mittenti @type mismatch");
            }
        }

        destinatariInput = inputs.containsKey("destinatari")?(List<HashMap<String, Object>>)inputs.get("destinatari"):null;
        if(destinatariInput == null)throw new ActionRuntimeException("destinatari not found in protocollazione");
        for(HashMap<String,Object> m : destinatariInput){
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
                    destinatariList.add(am);
							*//*
							 * CodiceAmministrazioneEnte,IndirizzoTelematicoEnte,DenominazioneAoo,CodiceAmministrazioneAoo,IndirizzoTelematicoAoo
							 * DenominazioneUo,CodiceAmministrazioneUo,IndirizzoTelematicoUo
							 *//*
                    break;
                default:
                    throw new ActionRuntimeException("destinatari @type mismatch");
            }
        }*/
//				}

/*

        firmatariInput = inputs.containsKey("firmatari")?(List<HashMap<String, Object>>)inputs.get("firmatari"):null;
        if(firmatariInput != null){
            for(HashMap<String,Object> m : firmatariInput){
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
*/


/*        try {

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

            *//*
                if(documento.properties!=null && documento.properties.containsKey("TIPO_FIRMA") && !documento.properties.get("TIPO_FIRMA").equals("")){
                tipoFirma = documento.properties.get("TIPO_FIRMA");
            }*//*


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
            result.put("userToken", token);*/
}


