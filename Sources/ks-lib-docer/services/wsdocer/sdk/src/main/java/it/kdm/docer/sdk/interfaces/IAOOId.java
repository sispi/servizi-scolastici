package it.kdm.docer.sdk.interfaces;

/**
 * Descrive l'Id di un'anagrafica AOO (formato dalla coppia Codice Ente - Codice AOO)
 * @author kdm
 *
 */
public interface IAOOId {

	/**
	 * @return Codice Ente della AOO
	 */
	public String getCodiceEnte();
	
	/**	 
	 * @param codiceEnte Codice Ente della AOO
	 */
	public void setCodiceEnte(String codiceEnte);
	
	/**
	 * @return Codice della AOO
	 */
	public String getCodiceAOO();
	
	/**
	 * @param codiceAOO Codice della AOO
	 */
	public void setCodiceAOO(String codiceAOO);
	
}
