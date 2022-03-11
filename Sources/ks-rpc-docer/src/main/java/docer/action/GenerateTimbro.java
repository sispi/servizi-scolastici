package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.model.Timbro;
import it.kdm.doctoolkit.services.ServizioTimbro;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateTimbro extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.GenerateTimbro.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		//String tempDir = System.getProperty("java.io.tmpdir");


		log.info("init method execute");
		String token = null;
		Map<String, Object> result = new HashMap<String, Object>();
		HashMap<String, Object> documentoMap = null;
        String fileName = null;

		String dpi="";
		String larghezza="";
		String altezza="";
		String formato="";
		Documento documento = new Documento();
		documentoMap = inputs.containsKey("documento")?(HashMap<String, Object>)inputs.get("documento"):null;
		dpi = inputs.containsKey("dpi")?(String)inputs.get("dpi"):null;
		larghezza = inputs.containsKey("larghezza")?(String)inputs.get("larghezza"):null;
		altezza = inputs.containsKey("altezza")?(String)inputs.get("altezza"):null;
		formato = inputs.containsKey("formato")?(String)inputs.get("formato"):null;

		if(dpi == null || !isInteger(dpi))throw new ActionRuntimeException("PIN 'dpi' non valido! inserire un valore intero");
		if(larghezza == null || !isInteger(larghezza) )throw new ActionRuntimeException("PIN ' larghezza' non valido! inserire un valore intero");
		if(altezza == null || !isInteger(altezza))throw new ActionRuntimeException("PIN 'altezza' non valido! inserire un valore intero");
		if(formato == null )throw new ActionRuntimeException("PIN 'formato' non trovato nei parametri di input!");
		if(documentoMap == null )throw new ActionRuntimeException("PIN 'documento' non trovato nei parametri di input!");


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
            String tempDir = ServerProperties.getParamsBpm("mail.attachDirEmailPec");
            UUID guid = UUID.randomUUID();
            String inputDirectory = tempDir+ File.separator+guid;
			token = getToken(inputs);
			fileName=documento.getDocName();
			Timbro timbro = new Timbro();

			timbro.setDpi(Integer.parseInt(dpi));
			timbro.setFormat(formato);
			timbro.setMaxh(Integer.parseInt(altezza));
			timbro.setMaxw(Integer.parseInt(larghezza));
			Timbro timbrogenerato = null;

            //String linkFileUrl = ServerProperties.getParamsBpm("url.download.file");

			timbrogenerato = ServizioTimbro.getTimbroFromDocument(token, timbro, documento);


			File fileDoc = new File(inputDirectory, "timbro_"+FilenameUtils.getBaseName(documento.getDocName())+"."+formato);

			fileDoc.getParentFile().mkdirs();
			FileWriter writer = new FileWriter(fileDoc);

			byte [] arrayb = IOUtils.toByteArray(timbrogenerato.getTimbroImg().getInputStream());

			FileOutputStream fous = new FileOutputStream(fileDoc);
			fous.write(arrayb);

			fous.close();
			writer.close();
			Documento documentoout = new Documento();
			documentoout.properties.put("filePath",getUrlFromFile(fileDoc));
			result.put("timbro", documentoout.toFlowObject());
			result.put("userToken", token);
		} catch (Exception e) {
			log.error("method execute error:{}",e.getMessage());
			throw new ActionRuntimeException(e);
		}
		log.info("end method execute");
		return result;
	}

}
