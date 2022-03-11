package it.kdm.docer.sdk.interfaces;

/**
 * Descrive l'Id di un'anagrafica voce di Titolario (formato dalla 3-pla: Codice Ente - Codice AOO - Classifica e/o Codice della voce di Titolario
 * @author kdm
 *
 */
public interface ITitolarioId {

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
	 * @return Codice della della voce di Titolario
	 */
	public String getCodiceTitolario();
	
	/**
	 * 
	 * @param codiceTitolario Codice della voce di Titolario
	 */
	public void setCodiceTitolario(String codiceTitolario);
	
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

}
