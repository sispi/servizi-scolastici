package it.kdm.docer.sdk.interfaces;

import java.util.Date;

/**
 * Descrive un evento storicizzato del documento
 * @author kdm
 *
 */
public interface IHistoryItem {

	/**
	 * 
	 * @return Datetime dell'evento
	 */
	public Date getDate();
	
	/**
	 * 
	 * @param date Datetime dell'evento
	 */
	public void setDate(Date date);
	
	/**
	 * 
	 * @return Id dell'Utente autore dell'azione storicizzata
	 */
	public String getUser();
	
	/**
	 * 
	 * @param user Id dell'Utente autore dell'azione storicizzata
	 */
	public void setUser(String user);
	
	/**
	 * 
	 * @return Descrizione dell'evento storicizzato
	 */
	public String getDescription();
	
	/**
	 * 
	 * @param description Descrizione dell'evento storicizzato
	 */
	public void setDescription(String description);
	
}
