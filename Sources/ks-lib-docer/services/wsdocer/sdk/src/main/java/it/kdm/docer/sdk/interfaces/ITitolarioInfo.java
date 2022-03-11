package it.kdm.docer.sdk.interfaces;

import it.kdm.docer.sdk.EnumBoolean;

import java.util.Map;

/**
 * Descrive il profilo di una voce di Titolario
 * @author kdm
 *
 */
public interface ITitolarioInfo {

    public String getPianoClassificazione();
    public void setPianoClassificazione(String pianoClassificazione);

	/**
	 * 
	 * @return Classifica della voce di Titolario
	 */
	public String getClassifica();
	
	/**
	 * 
	 * @param classifica Classifica della voce di Titolario
	 */
	public void setClassifica(String classifica);	
	
	/**
	 * 
	 * @return Codice della voce di Titolario
	 */
	public String getCodiceTitolario();
	
	/**
	 * 
	 * @param codiceTitolario Codice della voce di Titolario
	 */
	public void setCodiceTitolario(String codiceTitolario);
	
	/**
	 * 
	 * @return Descrizione della voce di Titolario
	 */
	public String getDescrizione();
	
	/**
	 * 
	 * @param descrizione Descrizione della voce di Titolario
	 */
	public void setDescrizione(String descrizione);
		
	/**
	 * 
	 * @return Codice Ente della voce di Titolario
	 */
	public String getCodiceEnte();
	
	/**
	 * 
	 * @param codEnte Codice Ente della voce di Titolario
	 */
	public void setCodiceEnte(String codEnte);
	
	/**
	 * 
	 * @return Codice AOO della voce di Titolario
	 */
	public String getCodiceAOO();
	
	/**
	 * 
	 * @param codAOO Codice AOO della voce di Titolario
	 */
	public void setCodiceAOO(String codAOO);
	
	/**
	 * 
	 * @return Classifica Padre della voce di Titolario
	 */
	public String getParentClassifica();
	
	/**
	 * 
	 * @param parentClassifica Classifica Padre della voce di Titolario
	 */
	public void setParentClassifica(String parentClassifica);
	
	
	/**
	 * @return Lo stato di abilitazione della voce di Titolario
	 */
	public EnumBoolean getEnabled();
	
	/**
	 * @param enabled TRUE se abilitato, FALSE se disabilitato, UNSPECIFIED se non specificato
	 */
	public void setEnabled(EnumBoolean enabled);
	
	/**
	 * @return Le informazioni aggiuntive del profilo della voce di Titolario
	 */
	public Map<String, String> getExtraInfo();
	
	/**
	 * @param extraInfo Le informazioni aggiuntive del profilo della voce di Titolario
	 */
	public void setExtraInfo(Map<String, String> extraInfo);
} 
