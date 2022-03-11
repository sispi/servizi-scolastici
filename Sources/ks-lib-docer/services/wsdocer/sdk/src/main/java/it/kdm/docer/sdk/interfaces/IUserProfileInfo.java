package it.kdm.docer.sdk.interfaces;

import it.kdm.docer.sdk.EnumBoolean;

import java.util.Map;

/**
 * Descrive il profilo di un Utente del sistema documentale
 * @author kdm
 *
 */
public interface IUserProfileInfo {
	
	/**
	 * 
	 * @return Id dell'Utente (univoco in tutto il sistema documentale)
	 */
	public String getUserId();	
	
	/**
	 * 
	 * @param userId Id dell'Utente (univoco in tutto il sistema documentale)
	 */
	public void setUserId(String userId);
	
	/**
	 * 
	 * @return Full-name dell'Utente
	 */
	public String getFullName();
	
	/**
	 * 
	 * @param fullname Full-name dell'Utente
	 */
	public void setFullName(String fullname);
	
//	/**
//	 * 	
//	 * @return Id del Gruppo primario a cui e' associato l'Utente (solitamente e' il Gruppo Ente)
//	 */
//	public String getPrimaryGroupId();
//	
//	/**
//	 * 
//	 * @param primaryGroupId Id del Gruppo primario a cui e' associato l'Utente (solitamente e' il Gruppo Ente)
//	 */
//	public void setPrimaryGroupId(String primaryGroupId);	

	/**
	 * 
	 * @return Nomde dell'Utente
	 */
	public String getFirstName();
	
	/**
	 * 
	 * @param firstName Nome dell'Utente
	 */
	public void setFirstName(String firstName);	

	/**
	 * 
	 * @return Cognome dell'Utente
	 */
	public String getLastName();
	
	/**
	 * 
	 * @param lastName Cognome dell'Utente
	 */
	public void setLastName(String lastName);	

	/**
	 * 
	 * @return CN dell'Utente nella directory LDAP (dipende dal sistema documentale)
	 */
	public String getNetworkAlias();
	
	/**
	 * 
	 * @param networkAlias CN dell'Utente nella directory LDAP (dipende dal sistema documentale)
	 */
	public void setNetworkAlias(String networkAlias);	

	/**
	 * 
	 * @return La password dell'Utente nel sistema documentale (generalmente null per motivi di sicurezza)  
	 */
	public String getUserPassword();
	
	/**
	 * 
	 * @param userPassword La password dell'Utente nel sistema documentale  (se null, per motivi di sicurezza, puo' essere generata automaticamente per essere cambiata in seguito dall'Utente)
	 */
	public void setUserPassword(String userPassword);	

	/**
	 * 
	 * @return Indirizzo e-mail dell'Utente
	 */
	public String getEmailAddress();
	
	/**
	 * 
	 * @param emailAddress Indirizzo e-mail dell'Utente
	 */
	public void setEmailAddress(String emailAddress);
	
	/**
	 * @return Lo stato di abilitazione dell'Utente
	 */
	public EnumBoolean getEnabled();
	
	/**
	 * @param enabled TRUE se abilitato, FALSE se disabilitato, UNSPECIFIED se non specificato
	 */
	public void setEnabled(EnumBoolean enabled);
	
	/**
	 * @return Le informazioni aggiuntive del profilo dell'Utente
	 */
	public Map<String, String> getExtraInfo();
	
	/**
	 * @param extraInfo Le informazioni aggiuntive del profilo dell'Utente
	 */
	public void setExtraInfo(Map<String, String> extraInfo);

}
