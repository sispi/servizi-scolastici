package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Cartella;
import it.kdm.doctoolkit.model.CartellaCriteria;
import it.kdm.doctoolkit.services.DocerService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreaCartella extends DocerAction {

	private final static Logger log = LoggerFactory.getLogger(docer.action.CreaCartella.class);
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
		log.info("init method execute");
		String token=null;
		Map<String, Object> result = new HashMap<String, Object>();
		Cartella cartellaRoot = null;
		HashMap<String, String> cartellaMap = null;
		HashMap<String, String> cartellaPadreMap = null;


        cartellaMap = inputs.containsKey("cartella")?(HashMap<String, String>) inputs.get("cartella"):null;
        cartellaPadreMap = inputs.containsKey("cartellaPadre")?(HashMap<String, String>) inputs.get("cartellaPadre"):null;


		if(cartellaMap == null)throw new ActionRuntimeException("PIN 'cartella' non trovato nei parametri di input!");

		Cartella cartella = new Cartella();
        cartellaMap.remove("TaskName");
        cartellaMap.remove("filePath");
        cartellaMap.remove("GroupId");
        cartellaMap.remove("comment");
        cartellaMap.remove("outcome");
        cartellaMap.remove("NodeName");
        cartellaMap.remove("ActorId");
        cartellaMap.remove("@structure");
        cartellaMap.remove("name");
        cartellaMap.remove("id");
		
		if(cartellaMap.containsKey("userToken")){
            cartellaMap.remove("userToken");
		}
		try {
			token = getToken(inputs);
		if(!cartellaMap.containsKey("PARENT_FOLDER_ID") || "".equals(cartellaMap.get("PARENT_FOLDER_ID"))){
			CartellaCriteria cartellaCriteria = new CartellaCriteria();
			if(!cartellaMap.containsKey("COD_AOO") || "".equals(cartellaMap.get("COD_AOO"))){
				cartellaCriteria.setAoo(((HashMap)this.getSystemProperties()).get("aoo").toString());
			}else{
				cartellaCriteria.setAoo(cartellaMap.get("COD_AOO"));
			}
			if(!cartellaMap.containsKey("COD_ENTE") || "".equals(cartellaMap.get("COD_ENTE"))){
				cartellaCriteria.setEnte(((HashMap)this.getSystemProperties()).get("ente").toString());
			}else{
				cartellaCriteria.setEnte(cartellaMap.get("COD_ENTE"));
			}
			cartellaCriteria.setProperty("PARENT_FOLDER_ID", "");
			cartellaCriteria.setProperty("FOLDER_NAME", "Cartelle");
			List<Cartella> listRoot = DocerService.ricercaCartella(token, cartellaCriteria);
			if(listRoot==null || listRoot.size()==0){
				throw new ActionRuntimeException("ROOT folder not found");
			}else{
				cartellaRoot=listRoot.get(0);
			}
		}


        cartella.properties = cartellaMap;
        
        //setto il parent folder id
        if(cartellaRoot!=null){
        	 cartella.setProperty("PARENT_FOLDER_ID", cartellaRoot.getProperty("FOLDER_ID"));
        }
        
       
		Cartella cartellaPadre = new Cartella();
        cartellaPadre.properties = cartellaPadreMap;
	
		if(cartellaPadreMap != null){
            cartella.setCartellaSuperiore(cartellaPadre.getID());
		}

		
			/*List<Acl> diritti = new ArrayList<Acl>();
			String respRole = (String)this.getSystemProperties().get("respRole");
			diritti.add(new Acl(respRole, aclRights.FullAccess));
			//TODO settare readonly su tutte le UO
			String respUo = (String)this.getSystemProperties().get("respUo");
			diritti.add(new Acl(respUo, aclRights.ReadOnly));*/

            //cartella.setProperty("id_proc", String.valueOf(this.getProcessInstanceId()));
			

			if(cartella.getAoo()==null || cartella.getAoo().equals("")){
                cartella.setAoo(((HashMap)this.getSystemProperties()).get("aoo").toString());
			}
			
			if(cartella.getEnte()==null || cartella.getEnte().equals("")){
                cartella.setEnte(((HashMap)this.getSystemProperties()).get("ente").toString());
			}
			

			
            Cartella folder = null;
			folder = DocerService.creaCartella(token,cartella);

			folder.setProperty("@structure", "cartella");
			result.put("cartella", folder.properties);
			result.put("userToken", token);
		} catch (Exception e) {
			log.error("method execute error:{}",e.getMessage());
			e.printStackTrace();
			throw new ActionRuntimeException(e);
			
		}
		log.info("end method execute");
		return result;
	}
	
}
