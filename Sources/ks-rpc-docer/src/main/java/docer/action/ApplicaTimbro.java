package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.helper.InputStreamDataSource;
import it.kdm.doctoolkit.model.DocerFile;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.model.Timbro;
import it.kdm.doctoolkit.model.TimbroCoordinates;
import it.kdm.doctoolkit.services.DocerService;
import it.kdm.doctoolkit.services.ServizioTimbro;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.activation.DataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




//
//documento":{"label":"Documento da timbrare","type":"document","value":""},
//		"timbro":{"label":"Timbro","type":"document","value":""},
//		"pagina":{"label":"Pagina","value":"","type":"textRef","hidden":false},
//		"coordX":{"label":"Ascissa","value":"","type":"textRef","hidden":false},
//		"coordY":{"label":"Ordinata","value":"","type":"textRef","hidden":false},
//		"userToken":{"label":"Token","value":"systemToken","type":"userToken","hidden":false}
//		},


public class ApplicaTimbro extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.ApplicaTimbro.class);

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		//String tempDir = System.getProperty("java.io.tmpdir");
		log.info("init method execute");
		String token = null;
		Map<String, Object> result = new HashMap<String, Object>();
		HashMap<String, Object> documentoMap = null;
		HashMap<String, Object> timbro = null;
		String fileName = null;

		String filePathTimbro="";

		String pagina="";
		String coordX="";
		String coordY="";

		Documento documento = new Documento();
		Documento timbroDoc = new Documento();
		documentoMap = inputs.containsKey("documento")?(HashMap<String, Object>)inputs.get("documento"):null;
		timbro = inputs.containsKey("timbro")?(HashMap<String, Object>)inputs.get("timbro"):null;

		pagina = inputs.containsKey("pagina")?(String)inputs.get("pagina"):null;


		coordX = inputs.containsKey("coordX")?(String)inputs.get("coordX"):null;


		coordY = inputs.containsKey("coordY")?(String)inputs.get("coordY"):null;



		if(pagina == null || !isInteger(pagina))throw new ActionRuntimeException("PIN 'pagina' non valido! inserire un valore intero");
		if(coordX == null || !isInteger(coordX) )throw new ActionRuntimeException("PIN ' coordX' non valido! inserire un valore intero");
		if(coordY == null || !isInteger(coordY))throw new ActionRuntimeException("PIN 'coordY' non valido! inserire un valore intero");
		if(documentoMap == null )throw new ActionRuntimeException("PIN 'documento' non trovato nei parametri di input!");
		if(timbro == null )throw new ActionRuntimeException("PIN 'timbro' non trovato nei parametri di input!");
		filePathTimbro = (String)timbro.get("filePath");



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

            //String tempDir = ServerProperties.getParamsBpm("mail.attachDirEmailPec");
            //UUID guid = UUID.randomUUID();
            //String inputDirectory = tempDir+ File.separator+guid;

			token = getToken(inputs);
			//fileName=documento.getDocName();
			Timbro timbroObj = new Timbro();
			InputStream is = null;
			if(filePathTimbro==null){
				String timbroDocNum = (String)timbro.get("DOCNUM");
				DocerFile timbroF = DocerService.downloadDocument(token, timbroDocNum);
				is = timbroF.getContent().getInputStream();
			}else {
				//String httpFilePath = FilenameUtils.getFullPathNoEndSeparator(URLDecoder.decode(filePathTimbro, "UTF-8"));
				//fileName = FilenameUtils.getName(URLDecoder.decode(filePathTimbro, "UTF-8"));
				//URLConnection cnx = new URL(httpFilePath+"?fileName="+ URLEncoder.encode(fileName, "UTF-8")).openConnection();
				//String username = ToolkitConnector.getSysUser();
				//cnx.addRequestProperty("SSO_USER", username);
				//cnx.addRequestProperty("SSO_USER_ENTE", getEnte());
				//URLConnection cnx = new URL(filePathTimbro).openConnection();
				//cnx.addRequestProperty(DocerAction.AUTHORIZATION, getJWTToken(inputs));
				//is = cnx.getInputStream();

				is = getStreamFromUrl(filePathTimbro);
			}

			DataHandler dh = new DataHandler(new InputStreamDataSource(is));

            //String linkFileUrl = ServerProperties.getParamsBpm("url.download.file");

			timbroObj.setTimbroImg(dh);
			DocerFile fileDownload = DocerService.downloadDocument(token, documento.getDocNum());
			TimbroCoordinates timbroCoordinates = new TimbroCoordinates();
			timbroCoordinates.setPagina(Integer.parseInt(pagina));
			timbroCoordinates.setX(Integer.parseInt(coordX));
			timbroCoordinates.setY(Integer.parseInt(coordY));

			DocerFile documentoTimbrato = ServizioTimbro.applicaTimbro(token, timbroObj, fileDownload,timbroCoordinates);

			/*File fileDoc = new File(inputDirectory, documento.getDocName());

			fileDoc.getParentFile().mkdirs();
			FileWriter writer = new FileWriter(fileDoc);
			OutputStream out = new FileOutputStream(fileDoc);
			IOUtils.copy(documentoTimbrato.getContent().getInputStream(), out);
			out.close();
			writer.close();*/
			Documento documentoout = new Documento();
			//documentoout.properties.put("filePath",linkFileUrl+fileDoc.getAbsolutePath());
			documentoout.properties.put("filePath", getUrlFromStream(documentoTimbrato.getContent().getInputStream()) );

			result.put("documentoTimbrato", documentoout.toFlowObject());
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
