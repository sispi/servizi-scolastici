package it.kdm.docer.sdk.interfaces;

import java.util.List;
import java.util.Map;

import it.kdm.docer.sdk.EnumBoolean;

/**
 * Contiene il profilo di un Utente e la lista dei Gruppi a cui esso e' associato
 * @author luca.biasin
 *
 */
public interface IUserInfo {
	
	/**
	 * 
	 * @return Profilo dell'Utente
	 */
	public IUserProfileInfo getProfileInfo();
	
	/**
	 * 
	 * @param profileInfo Profilo dell'Utente
	 */
	public void setProfileInfo(IUserProfileInfo profileInfo);
	
	/**
	 * 
	 * @return Lista degli Id dei Gruppi a cui e' associato l'Utente
	 */
	public List<String> getGroups();
	
	/**
	 * 
	 * @param groups Lista degli Id dei Gruppi a cui e' associato l'Utente
	 */
	public void setGroups(List<String> groups);
}
