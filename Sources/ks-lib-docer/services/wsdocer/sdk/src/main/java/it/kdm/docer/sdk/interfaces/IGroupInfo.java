package it.kdm.docer.sdk.interfaces;


import java.util.List;
import java.util.Map;

import it.kdm.docer.sdk.EnumBoolean;

/**
 * Contiene il profilo di un Gruppo e la lista degli Utenti ad esso associati
 * @author kdm
 *
 */

public interface IGroupInfo {

	/**
	 * 
	 * @return Profilo del Gruppo
	 */
	public IGroupProfileInfo getProfileInfo();
	
	/**
	 * 
	 * @param profileInfo Profilo del Gruppo
	 */
	public void setProfileInfo(IGroupProfileInfo profileInfo);
	
	/**
	 * 
	 * @return Lista degli Id degli Utenti associati al Gruppo
	 */
	public List<String> getUsers();
	
	/**
	 * 
	 * @param users Lista degli Id degli Utenti associati al Gruppo
	 */
	public void setUsers(List<String> users);

}
