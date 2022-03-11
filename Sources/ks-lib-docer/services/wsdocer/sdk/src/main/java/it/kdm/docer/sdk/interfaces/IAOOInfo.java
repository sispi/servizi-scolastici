package it.kdm.docer.sdk.interfaces;

import java.util.Map;
import it.kdm.docer.sdk.EnumBoolean;

/**
 * Descrive il Profilo di un'anagrafica AOO
 * 
 * @author kdm
 *
 */
public interface IAOOInfo {

    /**
     * @return Codice della AOO
     */
	public String getCodiceAOO();
	
	/**
	 * @param codiceAOO Codice della AOO
	 */
	public void setCodiceAOO(String codiceAOO);
	
	/**
	 * @return Descrizione della AOO
	 */
	public String getDescrizione();
	
	/**
	 * @param descrizione Descrizione della AOO
	 */
	public void setDescrizione(String descrizione);
	
	/**
	 * @return Lo stato di abilitazione della AOO
	 */
	public EnumBoolean getEnabled();
	
	/**
	 * @param enabled TRUE se abilitato, FALSE se disabilitato, UNSPECIFIED se non specificato
	 */
	public void setEnabled(EnumBoolean enabled);
	
	/**
	 * @return Codice Ente della AOO
	 */
	public String getCodiceEnte();
	
	/**
	 * @param codiceEnte Codice Ente della AOO
	 */
	public void setCodiceEnte(String codiceEnte);
	
	/**
	 * @return Le informazioni aggiuntive del profilo della AOO
	 */
	public Map<String, String> getExtraInfo();
	
	/**
	 * @param extraInfo Le informazioni aggiuntive del profilo della AOO
	 */
	public void setExtraInfo(Map<String, String> extraInfo);
}
