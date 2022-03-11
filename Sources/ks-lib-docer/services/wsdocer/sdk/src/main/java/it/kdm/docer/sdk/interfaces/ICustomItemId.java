package it.kdm.docer.sdk.interfaces;

/**
 * Descrive l'Id di un'anagrafica custom (formato dalla 4-pla: Tipo, Codice Ente, Codice AOO, Codice Custom)
 * @author kdm
 *
 */
public interface ICustomItemId {

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

}
