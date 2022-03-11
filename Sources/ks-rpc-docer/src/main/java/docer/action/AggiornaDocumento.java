package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.*;
import it.kdm.doctoolkit.services.DocerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AggiornaDocumento extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.AggiornaDocumento.class);
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		log.info("init method execute");
		String token=null;
		Map<String, Object> result = new HashMap<String, Object>();
		HashMap<String, Object> documentoMap = null;
		HashMap<String, String> mittenteMap = null;
		HashMap<String, String> destinatarioMap = null;

		Documento documento = null;
		documentoMap = inputs.containsKey("document")?(HashMap<String, Object>)inputs.get("document"):null;
		mittenteMap = inputs.containsKey("mittente")?(HashMap<String, String>)inputs.get("mittente"):null;
		destinatarioMap = inputs.containsKey("destinatario")?(HashMap<String, String>)inputs.get("destinatario"):null;
		if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
		//if(mittenteMap == null)throw new ActionRuntimeException("PIN 'mittente' non trovato nei parametri di input!");
		//if(destinatarioMap == null)throw new ActionRuntimeException("PIN 'destinatario' non trovato nei parametri di input!");

        try {
		documento = new Documento();
		
		//MITTENTE

        List<Corrispondente> mittentiLista = new ArrayList<Corrispondente>();
		if(documentoMap.containsKey("Mittenti")){
			List<HashMap> mitt= (List<HashMap>)documentoMap.get("Mittenti");
	        if(mitt!=null && mitt.size()>0){
				for (HashMap temp:mitt){
		        	if(temp.get("@type").equals("PersonaFisica")){
			        	PersonaFisica c = new PersonaFisica();
			        	c.properties = (HashMap<String, String>)temp.clone();
			        	mittentiLista.add(c);
		        	}else if(temp.get("@type").equals("Amministrazione")){
		            	Amministrazione c = new Amministrazione();
		            	c.properties = (HashMap<String, String>)temp.clone();
		            	mittentiLista.add(c);
		            }else if(temp.get("@type").equals("PersonaGiuridica")){
		            	PersonaGiuridica c = new PersonaGiuridica();
		            	c.properties = (HashMap<String, String>)temp.clone();
		            	mittentiLista.add(c);
		            }
		        	documentoMap.remove("Mittenti");	
		        }
		        
	        }
		}

		List<Vistatore> vistatoreLista = new ArrayList<Vistatore>();
		if(documentoMap.containsKey("VISTO")){
	        List<HashMap> vist= (List<HashMap>)documentoMap.get("VISTO");
	        for (HashMap temp:vist){
			        	Vistatore c = new Vistatore();
			        	c.properties = (HashMap<String, String>)temp.clone();
			        	vistatoreLista.add(c);
		    }

	        documentoMap.remove("VISTO");	
		}    
		
		List<Corrispondente> destinatariLista = new ArrayList<Corrispondente>();
		if(documentoMap.containsKey("Destinatari")){
	        List<HashMap> dest= (List<HashMap>)documentoMap.get("Destinatari");
	        if(dest!=null && dest.size()>0){
		        for (HashMap temp:dest){
		        	if(temp.get("@type").equals("PersonaFisica")){
			        	PersonaFisica c = new PersonaFisica();
			        	c.properties = (HashMap<String, String>)temp.clone();
			        	destinatariLista.add(c);
		        	}else if(temp.get("@type").equals("Amministrazione")){
		            	Amministrazione c = new Amministrazione();
		            	c.properties = (HashMap<String, String>)temp.clone();
		            	try {
							AOO aoo = new AOO();
							aoo.properties = (HashMap<String, String>) temp.clone();
							UO uo = new UO();
							uo.properties = (HashMap<String, String>) temp.clone();
							c.setAOO(aoo);
							c.setUnitaOrganizzativa(uo);
						}catch (Exception ex){
		            		ex.printStackTrace();
						}
		            	destinatariLista.add(c);
		            }else if(temp.get("@type").equals("PersonaGiuridica")){
		            	PersonaGiuridica c = new PersonaGiuridica();
		            	c.properties = (HashMap<String, String>)temp.clone();
		            	destinatariLista.add(c);
		            }
		        documentoMap.remove("Destinatari");	
		     }
		        
		    }
		}    
		
		
		
		
		
		
		for(String key:documentoMap.keySet()){
			documento.setProperty(key, (String)documentoMap.get(key));
		}
		
		if(vistatoreLista.size()>0){
			documento.setVistatore(vistatoreLista);
		}
		
		if(mittentiLista.size()>0){
			documento.setMittenti(mittentiLista);
		}
		if(destinatariLista.size()>0){
			documento.setDestinatari(destinatariLista);
		}
		
        if(mittenteMap != null) {
            List<Corrispondente> listMittenti = new ArrayList<Corrispondente>();
            if("Amministrazione".equals(mittenteMap.get("@type"))){
                Amministrazione mittente = new Amministrazione();
                mittente.properties = mittenteMap;
                listMittenti.add(mittente);
            }else if("PersonaFisica".equals(mittenteMap.get("@type"))){
                PersonaFisica mittente = new PersonaFisica();
                mittente.properties = mittenteMap;
                listMittenti.add(mittente);
            }else if("PersonaGiuridica".equals(mittenteMap.get("@type"))){
                PersonaGiuridica mittente = new PersonaGiuridica();
                mittente.properties = mittenteMap;
                listMittenti.add(mittente);
            }
                documento.setMittenti(listMittenti);
        }
		//DESTINATARIO
        if(destinatarioMap != null) {
            List<Corrispondente> listDestinatari = new ArrayList<Corrispondente>();
            if("Amministrazione".equals(destinatarioMap.get("@type"))){
                Amministrazione destinatario = new Amministrazione();
                destinatario.properties = destinatarioMap;
                listDestinatari.add(destinatario);
            }else if("PersonaFisica".equals(destinatarioMap.get("@type"))){
                PersonaFisica destinatario = new PersonaFisica();
                destinatario.properties = destinatarioMap;
                listDestinatari.add(destinatario);
            }else if("PersonaGiuridica".equals(destinatarioMap.get("@type"))){
                PersonaGiuridica destinatario = new PersonaGiuridica();
                destinatario.properties = destinatarioMap;
                listDestinatari.add(destinatario);
            }

            documento.setDestinatari(listDestinatari);
        }

        documento.properties.remove("@type");
		documento.properties.remove("@structure");
		if(documento.properties.containsKey("userToken")){
			documento.properties.remove("userToken");
		}
		/*List<Acl> diritti = new ArrayList<Acl>();
		String respRole = (String)this.getSystemProperties().get("respRole");
		diritti.add(new Acl(respRole, aclRights.FullAccess));*/
		//TODO settare readonly su tutte le UO
		//String respUo = (String)this.getSystemProperties().get("respUo");
		//diritti.add(new Acl(respUo, aclRights.ReadOnly));
		documento.setProperty("PROC_ID", String.valueOf(this.getProcessInstanceId()));

		if(documento.getAoo()==null && documento.getAoo().equals("")){
			documento.setAoo((String)((HashMap)this.getSystemProperties().get("configuration")).get("aoo"));
		}
		
		if(documento.getEnte()==null && documento.getEnte().equals("")){
			documento.setEnte((String)((HashMap)this.getSystemProperties().get("configuration")).get("ente"));
		}
		token = getToken(inputs);
		documento = DocerService.aggiornaDocumento(token, documento);
		documento.setProperty("@structure", "document");

        result.put("document", documentToHashmap(documento));
		result.put("userToken", token);
		} catch (Exception e) {
			log.error("method execute error: {}",e.getMessage());
			throw new ActionRuntimeException(e);
		}
		log.info("end method execute");
		return result;
	}

}
