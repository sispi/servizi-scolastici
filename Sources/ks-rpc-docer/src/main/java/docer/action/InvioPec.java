package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.*;
import it.kdm.doctoolkit.services.ServizioPec;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class InvioPec extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.InvioPec.class);

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {

		log.info("init method execute");
		String token = null;
		String oggetto = null;
		String modalitaInvio = null;
		String tipoRichiesta = null;
		String forzaInvio = null;
		Map<String, Object> result = new HashMap<String, Object>();
		HashMap<String, Object> documentoMap = null;
		List<HashMap<String, Object>> destinatari = null;
		List<Corrispondente> destinatariList = new ArrayList<Corrispondente>();
		HashMap<String, String> esito = null;

		//recupero oggetto della mail
		oggetto = inputs.containsKey("oggetto")? (String) inputs.get("oggetto") :null;
		if(oggetto == null )throw new ActionRuntimeException("PIN 'oggetto' non trovato nei parametri di input!");

		//recupero modalita invio della mail
		modalitaInvio = inputs.containsKey("modalitaInvio")? (String) inputs.get("modalitaInvio") :null;
		if(modalitaInvio == null )throw new ActionRuntimeException("PIN 'modalitaInvio' non trovato nei parametri di input!");

		//recupero tipo richiesta della mail
		tipoRichiesta = inputs.containsKey("tipoRichiesta")? (String) inputs.get("tipoRichiesta") :null;
		if(tipoRichiesta == null )throw new ActionRuntimeException("PIN 'tipoRichiesta' non trovato nei parametri di input!");

		//recupero forza invio della mail
		forzaInvio = inputs.containsKey("forzaInvio")? (String) inputs.get("forzaInvio") :null;
		if(forzaInvio == null )throw new ActionRuntimeException("PIN 'forzaInvio' non trovato nei parametri di input!");


		//recupero documento della mail se presente
		documentoMap = inputs.containsKey("document")?(HashMap<String, Object>)inputs.get("document"):null;
		if(documentoMap == null )throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");

		//recupero destinatari della mail
		destinatari = inputs.containsKey("destinatari")? (List<HashMap<String, Object>>) inputs.get("destinatari") :null;
		if(destinatari == null )throw new ActionRuntimeException("PIN 'destinatari' non trovato nei parametri di input!");

		for(HashMap<String, Object> m : destinatari){
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
						am.setDenominazione((String)m.get("DenominazioneEnte"));
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
							/*
							 * CodiceAmministrazioneEnte,IndirizzoTelematicoEnte,DenominazioneAoo,CodiceAmministrazioneAoo,IndirizzoTelematicoAoo
							 * DenominazioneUo,CodiceAmministrazioneUo,IndirizzoTelematicoUo
							 */
					break;
				default:
					throw new ActionRuntimeException("destinatari @type mismatch");
			}
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

			try{
				documento.fromFlowObject(documentoMap);
			}catch(Exception e){
				throw new ActionRuntimeException(e);
			}

		    if(documento.getDocNum()==null || "".equals(documento.getDocNum()) ){
				throw new ActionRuntimeException("PIN 'documento'.'docnum' non valido!");
			}

			PecObject pecObject = new PecObject();
			pecObject.setDestinatari(destinatariList);
			pecObject.setDocumento(documento);
			pecObject.setForzaInvio(forzaInvio);
			pecObject.setModalitaInvio(modalitaInvio);
			pecObject.setOggetto(oggetto);
			pecObject.setTipoRichiesta(tipoRichiesta);

		try {
			token = getToken(inputs);

			String esito1 = ServizioPec.inviaPec(token, pecObject);
			String esitoJiride = "1";


			InputSource source = new InputSource(new StringReader(esito1));
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();



			esitoJiride = (String) xpath.evaluate("/Esito/Codice", source, XPathConstants.STRING);




			result.put("esito", esitoJiride);
			result.put("userToken", token);
		} catch (Exception e) {
			log.error("method execute error:{}",e.getMessage());
			throw new ActionRuntimeException(e);
		}
		finally{

		}
		log.info("end method execute");
		return result;
	}

}
