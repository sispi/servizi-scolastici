package it.kdm.docer.fascicolazione.batch;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;
import it.kdm.docer.clients.AuthenticationServiceExceptionException;
import it.kdm.docer.clients.FascicolazioneExceptionException;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.exceptions.DocerException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

public class BLFacade {

	private static final ThreadLocal<String> TL = new ThreadLocal<String>();

	private static Logger log = org.slf4j.LoggerFactory.getLogger(BLFacade.class);

	public static boolean existsTitolario(Map<String, String> info) throws DocerException {
		try {
			Map<String, String> titolario = DefaultBusinessLogic.INSTANCE.getTitolario(login(), info);
		} catch (DocerException e) {
			// Titolario non trovato
			if (e.getErrNumber() == 472)
				return false;
			else
				throw e;
		}

		return true;
	}

	public static void createTitolario(Map<String, String> info) throws DocerException {
		DefaultBusinessLogic.INSTANCE.createTitolario(login(), info);
	}

	public static void setACLTitolario(Map<String, String> info, String acls) throws DocerException {
		Map<String, String> id = new HashMap<String, String>();
		id.put("COD_ENTE",info.get("cod_ente"));
		id.put("COD_AOO",info.get("cod_aoo"));
		id.put("CLASSIFICA",info.get("classifica"));
		id.put("PIANO_CLASS",info.get("piano_class"));

		Map<String, EnumACLRights> acl = new HashMap<String, EnumACLRights>();

		if (Strings.isNullOrEmpty(acls))
			return;

		String[] names = acls.split(";");
		for(int i=0;i<names.length;i++) {
			EnumACLRights access = EnumACLRights.readOnly;
			String[] pars = names[i].split(":");

			if ("0".equalsIgnoreCase(pars[1]))
				access = EnumACLRights.fullAccess;
			else if ("1".equalsIgnoreCase(pars[1]))
				access = EnumACLRights.normalAccess;
			else if ("2".equalsIgnoreCase(pars[1]))
				access = EnumACLRights.readOnly;

			acl.put(pars[0],access);
		}

		DefaultBusinessLogic.INSTANCE.setACLTitolario(login(), id, acl);
	}

	public static boolean existsFascicolo(Map<String, String> info) throws DocerException {
		try {
			Map<String,String> fascicolo = DefaultBusinessLogic.INSTANCE.getFascicolo(login(), info);
		} catch (DocerException e) {
			// Fascicolo non trovato
			if (e.getErrNumber() == 475)
				return false;
			else
				throw e;
		}

		return true;
	}

	public static void createFascicolo(Map<String, String> info) throws DocerException {
		info.remove("acls");
		DefaultBusinessLogic.INSTANCE.createFascicolo(login(), info);
	}

	public static void setACLFascicolo(Map<String, String> info, String acls) throws DocerException {
		Map<String, String> id = new HashMap<String, String>();
		id.put("COD_ENTE",info.get("cod_ente"));
		id.put("COD_AOO",info.get("cod_aoo"));
		id.put("CLASSIFICA",info.get("classifica"));
		id.put("PROGR_FASCICOLO",info.get("progr_fascicolo"));
		id.put("ANNO_FASCICOLO",info.get("anno_fascicolo"));
		id.put("PIANO_CLASS",info.get("piano_class"));

		Map<String, EnumACLRights> acl = new HashMap<String, EnumACLRights>();

		if (Strings.isNullOrEmpty(acls))
			return;

		String[] names = acls.split(";");
		for(int i=0;i<names.length;i++) {
			EnumACLRights access = EnumACLRights.readOnly;
			String[] pars = names[i].split(":");

			if ("0".equalsIgnoreCase(pars[1]))
				access = EnumACLRights.fullAccess;
			else if ("1".equalsIgnoreCase(pars[1]))
				access = EnumACLRights.normalAccess;
			else if ("2".equalsIgnoreCase(pars[1]))
				access = EnumACLRights.readOnly;

			acl.put(pars[0],access);
		}

		DefaultBusinessLogic.INSTANCE.setACLFascicolo(login(), id, acl);
	}

	private static String login() throws DocerException {
		if (TL.get() == null) {
			String userid = Configuration.getInstance().getProperty("auth.username");
			String password = Configuration.getInstance().getProperty("auth.password");
			String codiceEnte = Configuration.getInstance().getProperty("auth.ente");
			String token = DefaultBusinessLogic.INSTANCE.login(codiceEnte, userid, password);
			//String token = WSBusinessLogic.INSTANCE.login(codiceEnte, userid, password);
			TL.set(token);
			log.info("Token restituito: " + token);
		}
		return TL.get();
	}

	/*
	public static void createFascicoloREST(Map<String, String> info) throws DocerException, FascicolazioneExceptionException, RemoteException {
		WSBusinessLogic.INSTANCE.createFascicolo(login(), info);
	}
	*/

	/*
	private static String loginTitolario() throws DocerException {
		if (tokenTitolario == null) {
			String userid = Configuration.getInstance().getProperty("auth.username");
			String password = Configuration.getInstance().getProperty("auth.password");
			String codiceEnte = Configuration.getInstance().getProperty("auth.ente");
			tokenTitolario = DefaultBusinessLogic.INSTANCE.login(codiceEnte, userid, password);
			log.info("Token titolario: " + tokenTitolario);
		}
		return tokenTitolario;
	}
	*/

}
