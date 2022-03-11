package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CambiaVersione extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(ApplicaTimbro.class);

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {

		log.info("init method execute");
		String token = null;
		Map<String, Object> result = new HashMap<String, Object>();
		// variabili document In input
		HashMap<String, Object> documentoMap = null;
		//variabile per propietà addVersion
		String addVersion=null;
		Boolean checkVersion=null;

		Documento documento = new Documento();
		String filePathDocumento="";
		// recupero il campo addVersion
		addVersion=(String) inputs.get("addVersion");

		//recupero documento in input
		documentoMap = inputs.containsKey("document")?(HashMap<String, Object>)inputs.get("document"):null;
		//controllo il pin del documento
		if(documentoMap == null )throw new ActionRuntimeException("PIN 'documento' non trovato nei parametri di input!");
		//recupero il filePath del documento da FileSystem
		filePathDocumento = (String)documentoMap.get("filePath");
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

		try {
			token = getToken(inputs);

		    //recupero il documento da fileSystem
			//String httpFilePath = FilenameUtils.getFullPathNoEndSeparator(URLDecoder.decode(filePathDocumento, "UTF-8"));
			//String fileName = FilenameUtils.getName(URLDecoder.decode(filePathDocumento, "UTF-8"));
			//URLConnection cnx = new URL(httpFilePath+"?fileName="+ URLEncoder.encode(fileName, "UTF-8")).openConnection();
			//String username = ToolkitConnector.getSysUser();
			//cnx.addRequestProperty("SSO_USER", username);
			//cnx.addRequestProperty("SSO_USER_ENTE", getEnte());
			//URLConnection cnx = new URL(filePathDocumento).openConnection();
			//cnx.addRequestProperty(DocerAction.AUTHORIZATION, getJWTToken(inputs));
			//InputStream is = cnx.getInputStream();

			InputStream is = getStreamFromUrl(filePathDocumento);

			if("".equals(addVersion)){
				throw new ActionRuntimeException("Props 'addVersion' non definito nei parametri di input!");
			}

			if(addVersion.equals("true"))
			{
				//aggiungi nuova versione
				DocerService.aggiungiNuovaVersione(token, documento, is);
			}

			else
			{
				DocerService.sovrascriviUltimaVersione(token, documento, is);
			}

		    Documento documentoOut=null;

				documentoOut= DocerService.recuperaProfiloDocumento(token,documento.getDocNum());

			result.put("document",documentoOut.toFlowObject());
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
