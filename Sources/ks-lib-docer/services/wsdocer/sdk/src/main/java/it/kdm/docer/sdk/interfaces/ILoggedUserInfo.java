package it.kdm.docer.sdk.interfaces;

/**
 * Contiene le informazioni dell'Utente operatore
 * @author kdm
 *
 */
public interface ILoggedUserInfo {
	
	/**
	 * 
	 * @return Id dell'Utente operatore
	 */
    public String getUserId();
    
    /**
     * 
     * @param userid Id dell'Utente operatore
     */
    public void setUserId(String userid);
    
    /**
     * 
     * @return Ticket di sessione dell'Utente operatore
     */
    public String getTicket();
    
    /**
     * 
     * @param ticket Ticket di sessione dell'Utente operatore
     */
    public void setTicket(String ticket);
    
    /**
     * 
     * @return Identificativo univoco dell'ente su cui si intende operare
     */
    public String getCodiceEnte();
    
    /**
     * 
     * @param codiceEnte Identificativo univoco dell'ente su cui si intende operare
     */
    public void setCodiceEnte(String codiceEnte);
}
