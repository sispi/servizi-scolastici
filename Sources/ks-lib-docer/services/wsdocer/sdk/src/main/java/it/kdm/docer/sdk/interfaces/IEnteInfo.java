package it.kdm.docer.sdk.interfaces;

import it.kdm.docer.sdk.EnumBoolean;

import java.util.HashMap;
import java.util.Map;

/**
 * Descrive il profilo di un'anagrafica Ente
 * @author kdm
 *
 */
public interface IEnteInfo {

	/**
	 * 
	 * @return Codice dell'Ente
	 */
	public String getCodiceEnte();
	
	/**
	 * 
	 * @param codEnte Codice dell'Ente
	 */
	public void setCodiceEnte(String codEnte);
	
	/**
	 * 
	 * @return Descrizione dell'Ente
	 */
	public String getDescrizione();
	
	/**
	 * 
	 * @param descrizione Descrizione dell'Ente
	 */
	public void setDescrizione(String descrizione);
	
	/**
	 * @return Lo stato di abilitazione dell'Ente
	 */
	public EnumBoolean getEnabled();
	
	/**
	 * @param enabled TRUE se abilitato, FALSE se disabilitato, UNSPECIFIED se non specificato
	 */
	public void setEnabled(EnumBoolean enabled);
	
	/**
	 * @return Le informazioni aggiuntive del profilo dell'Ente
	 */
	public Map<String, String> getExtraInfo();
	
	/**
	 * @param extraInfo Le informazioni aggiuntive del profilo dell'Ente
	 */
	public void setExtraInfo(Map<String, String> extraInfo);
	
	
}
