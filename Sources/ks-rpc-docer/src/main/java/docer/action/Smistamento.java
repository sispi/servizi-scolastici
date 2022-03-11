package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Acl;
import it.kdm.doctoolkit.model.Acl.aclRights;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Smistamento extends DocerAction {
	private final static Logger log = LoggerFactory.getLogger(docer.action.Smistamento.class);
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)
			throws ActionRuntimeException {
		log.info("init method execute");
		String token=null;
		Map<String, Object> result = new HashMap<String, Object>();
		String actor = null;
		HashMap<String, String> documentoMap = null;
		
		actor = inputs.containsKey("actor")?(String)inputs.get("actor"):null;
		documentoMap = inputs.containsKey("document")?(HashMap<String, String>)inputs.get("document"):null;
		
		if(actor == null)throw new ActionRuntimeException("PIN 'actor' non trovato nei parametri di input!");
		if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
		
		Documento documento = new Documento();
		documentoMap.remove("TaskName");
		documentoMap.remove("filePath");
		documentoMap.remove("GroupId");
		documentoMap.remove("comment");
		documentoMap.remove("outcome");
		documentoMap.remove("NodeName");
		documentoMap.remove("ActorId");
		documentoMap.remove("@structure");
		
		documento.properties = documentoMap;
		
		try {
			List<Acl> diritti = new ArrayList<Acl>();
			//String respRole = (String)this.getSystemProperties().get("respRole");
			//diritti.add(new Acl(respRole, aclRights.FullAccess));
			diritti.add(new Acl(actor, aclRights.FullAccess));
			token = getToken(inputs);
			DocerService.impostaDirittiDocumento(token, documento.getDocNum(), diritti);
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
