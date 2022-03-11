package docer.action;


import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Documento;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class parserDaticert extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.parserDaticert.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {

		log.info("init method execute");
		String token = null;
		Map<String, Object> result = new HashMap<String, Object>();
		HashMap<String, Object> documentoMap = null;
		String fileURL = null;
        String primaryFile = null;
        String fileName = null;
        String esito="negativo";
        String errore="";

        Documento documento = new Documento();
		documentoMap = inputs.containsKey("document")?(HashMap<String, Object>)inputs.get("document"):null;
		fileURL = inputs.containsKey("document")?(String)documentoMap.get("filePath"):null;



        if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
        
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

    /*controllo i parametri del file in input*/
		try {

			InputStream input = null;

			if(fileURL!= null && !fileURL.equals("")) {
				//String httpFilePath = FilenameUtils.getFullPathNoEndSeparator(URLDecoder.decode(fileURL, "UTF-8"));
				//fileName = FilenameUtils.getName(URLDecoder.decode(fileURL, "UTF-8"));
				//URLConnection cnx = new URL(httpFilePath+"?fileName="+ URLEncoder.encode(fileName, "UTF-8")).openConnection();

				//URLConnection cnx = new URL(fileURL).openConnection();

				//String username = ToolkitConnector.getSysUser();
				//cnx.addRequestProperty("SSO_USER", username);
				//cnx.addRequestProperty("SSO_USER_ENTE", getEnte());
				//cnx.addRequestProperty(DocerAction.AUTHORIZATION, getJWTToken(inputs));
				//input = cnx.getInputStream();

				input = getStreamFromUrl(fileURL);

                javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(input);
                doc.getDocumentElement().normalize();

                System.out.println("Controllo se il campo errore dentro il tag postacert");
                errore=doc.getDocumentElement().getAttributeNode("errore").getValue();
                //se nel file daticert non ci sono errori allora l'esito sarà positivo, altrimenti rimane esito negativo
                if(errore.equals("nessuno")){
                    esito="positivo";
                    System.out.println("Esito "+esito);
                    System.out.println("Errore: "+errore);
                }
                /********************************************************/
            } else { throw new ActionRuntimeException("Nel path del documento non e' impostato nessun daticert.xml");}

            token = getToken(inputs);
            result.put("esito",esito);
            result.put("errore",errore);
            result.put("userToken", token);

		}catch(Exception e){
			log.error("method execute error:{}",e.getMessage());
			throw new ActionRuntimeException(e);
		}

		log.info("end method execute");
		return result;
	}

}
