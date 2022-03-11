package it.kdm.docer.sdk.interfaces;

import java.util.Map;

import it.kdm.docer.sdk.EnumBoolean;

/**
 * Descrive il profilo di un'anagrafica Fascicolo
 * @author kdm
 *
 */
public interface IFascicoloInfo {

    public String getPianoClassificazione();
    public void setPianoClassificazione(String pianoClassificazione);

	/**
	 * 
	 * @return Progressivo del Fascicolo
	 */
	public String getProgressivo();
	
	/**
	 * 
	 * @param progressivo Progressivo del Fascicolo
	 */
	public void setProgressivo(String progressivo);
	
	/**
	 * 
	 * @return Progressivo del Fascicolo Padre del Fascicolo
	 */
	public String getParentProgressivo();
	
	/**
	 * 
	 * @param parentProgressivo Progressivo del Fascicolo Padre del Fascicolo
	 */
	public void setParentProgressivo(String parentProgressivo);
	
	/**
	 * 
	 * @return Numero del Fascicolo
	 */
	public String getNumeroFascicolo();
	
	/**
	 * 
	 * @param numeroFascicolo Numero del Fascicolo
	 */
	public void setNumeroFascicolo(String numeroFascicolo);
	
	/**
	 * 
	 * @return Anno del Fascicolo (YYYY)
	 */
	public String getAnnoFascicolo();
	
	/**
	 * 
	 * @param annoFascicolo Anno del Fascicolo (YYYY)
	 */
	public void setAnnoFascicolo(String annoFascicolo);
	
	/**
	 * 
	 * @return Descrizione del Fascicolo
	 */
	public String getDescrizione();
	
	/**
	 * 
	 * @param descrizione Descrizione del Fascicolo
	 */
	public void setDescrizione(String descrizione);
	
	/**
	 * 
	 * @return Codice Ente del Fascicolo
	 */
	public String getCodiceEnte();
	
	/**
	 * 
	 * @param codiceEnte Codice Ente del Fascicolo
	 */
	public void setCodiceEnte(String codiceEnte);
	
	/**
	 * 
	 * @return Codice AOO del Fascicolo
	 */
	public String getCodiceAOO();
	
	/**
	 * 
	 * @param codiceAOO Codice AOO del Fascicolo
	 */
	public void setCodiceAOO(String codiceAOO);
	
	/**
	 * 
	 * @return Classifica della voce di Titolario del Fascicolo
	 */
	public String getClassifica();
	
	/**
	 * 
	 * @param classifica Classifica della voce di Titolario del Fascicolo
	 */
	public void setClassifica(String classifica);
	
	/**
	 * @return Lo stato di abilitazione del Fascicolo
	 */
	public EnumBoolean getEnabled();
	
	/**
	 * @param enabled TRUE se abilitato, FALSE se disabilitato, UNSPECIFIED se non specificato
	 */
	public void setEnabled(EnumBoolean enabled);
	
	/**
	 * @return Le informazioni aggiuntive del profilo del Fascicolo
	 */
	public Map<String, String> getExtraInfo();
	
	/**
	 * @param extraInfo Le informazioni aggiuntive del profilo del Fascicolo
	 */
	public void setExtraInfo(Map<String, String> extraInfo);
}
