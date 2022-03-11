package it.kdm.docer.sdk.interfaces;


import it.kdm.docer.sdk.EnumBoolean;
import java.util.Map;

/**
 * Descrive il profilo di un Gruppo del sistema documentale
 * @author kdm
 *
 */
public interface IGroupProfileInfo {

	/**
	 * 
	 * @return Id del Gruppo (univoco in tutto il sistema documentale)
	 */
	public String getGroupId();	
	
	/**
	 * 
	 * @param groupId Id del Gruppo (univoco in tutto il sistema documentale)
	 */
	public void setGroupId(String groupId);
	
	/**
	 * 
	 * @return Nome del Gruppo
	 */
	public String getGroupName();
	
	/**
	 * 
	 * @param groupname Nome del Gruppo
	 */
	public void setGroupName(String groupname);
	
	/**
	 * 
	 * @return Id del Gruppo Padre (null o stringa vuota per i gruppi di I livello come i gruppi Enti)
	 */
	public String getParentGroupId();
	
	/**
	 * 
	 * @param parentGroupId Id del Gruppo Padre (null o stringa vuota per i gruppi di I livello come i gruppi Enti)
	 */
	public void setParentGroupId(String parentGroupId);	
 
	/**
	 * @return Lo stato di abilitazione del Gruppo
	 */
	public EnumBoolean getEnabled();
	
	/**
	 * @param enabled TRUE se abilitato, FALSE se disabilitato, UNSPECIFIED se non specificato
	 */
	public void setEnabled(EnumBoolean enabled);
	
	/**
	 * @return Le informazioni aggiuntive del profilo del Gruppo
	 */
	public Map<String, String> getExtraInfo();
	
	/**
	 * @param extraInfo Le informazioni aggiuntive del profilo del Gruppo
	 */
	public void setExtraInfo(Map<String, String> extraInfo);

}
