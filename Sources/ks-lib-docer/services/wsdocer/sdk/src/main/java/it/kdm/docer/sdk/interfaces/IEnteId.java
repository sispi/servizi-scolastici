package it.kdm.docer.sdk.interfaces;

/**
 * Descrive l'Id di un'anagrafica Ente
 * @author kdm
 *
 */
public interface IEnteId {
	
	/**
	 * 
	 * @return Codice dell'Ente e del corrispondente Gruppo
	 */
	public String getCodiceEnte();
	
	/**
	 * 
	 * @param codEnte Codice dell'Ente e del corrispondente Gruppo
	 */
	public void setCodiceEnte(String codEnte);
}
