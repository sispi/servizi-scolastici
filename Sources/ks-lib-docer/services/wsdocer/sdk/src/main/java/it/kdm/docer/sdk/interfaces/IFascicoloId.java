package it.kdm.docer.sdk.interfaces;

/**
 * Descrive l'Id di un'anagrafica Fascicolo (formato dalla 5-pla: Codice Ente - Codice AOO - Classifica della voce di Titolario - Anno Fascicolo - Progressivo Fascicolo e/o Numero Fascicolo )
 * @author kdm
 *
 */
public interface IFascicoloId {
	
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

    // DOCER-36 Piano di classificazione
	public String getPianoClassificazione();
    public void setPianoClassificazione(String pianoClassificazione);
}
