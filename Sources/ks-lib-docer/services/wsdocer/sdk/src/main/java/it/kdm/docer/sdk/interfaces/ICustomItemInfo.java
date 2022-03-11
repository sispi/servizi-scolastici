package it.kdm.docer.sdk.interfaces;

import java.util.Map;

import it.kdm.docer.sdk.EnumBoolean;

/**
 * Descrive il profilo di un'anagrafica custom
 * @author kdm
 *
 */
public interface ICustomItemInfo {

	/**
	 * 
	 * @return Tipo o TYPE_ID dell'anagrafica custom
	 */
	
	public String getType();
	/**
	 * 
	 * @param type Tipo o TYPE_ID dell'anagrafica custom
	 */
	public void setType(String type);
	
	/**
	 * 
	 * @return Codice dell'anagrafica custom
	 */
	public String getCodiceCustom();
	
	/**
	 * 
	 * @param codiceCustom Codice dell'anagrafica custom
	 */
	public void setCodiceCustom(String codiceCustom);
	
	/**
	 * 
	 * @return Descrizione dell'anagrafica custom
	 */
	public String getDescrizione();
	
	/**
	 * 
	 * @param descrizione Descrizione dell'anagrafica custom
	 */
	public void setDescrizione(String descrizione);
		
	/**
	 * @return Lo stato di abilitazione dell'anagrafica custom
	 */
	public EnumBoolean getEnabled();
	
	/**
	 * @param enabled TRUE se abilitato, FALSE se disabilitato, UNSPECIFIED se non specificato
	 */
	public void setEnabled(EnumBoolean enabled);
	
	/**
	 * 
	 * @return Codice Ente dell'anagrafica custom
	 */
	public String getCodiceEnte();
	
	/**
	 * 
	 * @param codiceEnte Codice Ente dell'anagrafica custom
	 */
	public void setCodiceEnte(String codiceEnte);
	
	/**
	 * 
	 * @return Codice AOO dell'anagrafica custom
	 */
	public String getCodiceAOO();
	
	/**
	 * 
	 * @param codiceAOO Codice AOO dell'anagrafica custom
	 */
	public void setCodiceAOO(String codiceAOO);
	
	/**
	 * @return Le informazioni aggiuntive del profilo dell'anagrafica custom
	 */
	public Map<String, String> getExtraInfo();
	
	/**
	 * @param extraInfo Le informazioni aggiuntive del profilo dell'anagrafica custom
	 */
	public void setExtraInfo(Map<String, String> extraInfo);
	
	
}
