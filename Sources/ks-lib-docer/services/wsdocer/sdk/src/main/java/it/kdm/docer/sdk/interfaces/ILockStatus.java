package it.kdm.docer.sdk.interfaces;

/**
 * Descrive lo stato di blocco esclusivo di un documento
 * @author kdm
 *
 */
public interface ILockStatus {

	/**
	 * 
	 * @return Id dell'utente che possiede il blocco esclusivo del documento
	 */
	public String getUserId();
	
	/**
	 * 
	 * @param userId Id dell'utente che possiede il blocco esclusivo del documento
	 */
	public void setUserId(String userId);

	/**
	 * 
	 * @return Full-name dell'utente che possiede il blocco esclusivo del documento
	 */
	public String getFullName();
	
	/**
	 * 
	 * @param fullname Full-name dell'utente che possiede il blocco esclusivo del documento
	 */
	public void setFullName(String fullname);

	/**
	 * 
	 * @return true se in stato di blocco esclusivo altrimenti false
	 */
	public boolean getLocked();
	
	/**
	 * 
	 * @param locked true se in stato di blocco esclusivo altrimenti false
	 */
	public void setLocked(boolean locked);

}