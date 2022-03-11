package it.kdm.docer.sdk.interfaces;

import java.util.Map;

import it.kdm.docer.sdk.EnumBoolean;

/**
 * Descrive il profilo di una Folder
 * @author kdm
 *
 */
public interface IFolderInfo {

    /**
     * 
     * @return Id della Folder
     */
    public String getFolderId();
    
    /**
     * 
     * @param folderId Id della Folder
     */
    public void setFolderId(String folderId);
    
	/**
	 * 
	 * @return Nome della Folder
	 */
	public String getFolderName();
	
	/**
	 * 
	 * @param folderName Nome della Folder
	 */
	public void setFolderName(String folderName);
	
	/**
	 * 
	 * @return Descrizione della Folder
	 */
	public String getDescrizione();
	
	/**
	 * 
	 * @param descrizione Descrizione della Folder
	 */
	public void setDescrizione(String descrizione);	
		
	/**
	 * 
	 * @return Codice Ente della Folder
	 */
	public String getCodiceEnte();
	
	/**
	 * 
	 * @param codEnte Codice Ente della Folder
	 */
	public void setCodiceEnte(String codEnte);
	
	/**
	 * 
	 * @return Codice AOO della Folder
	 */
	public String getCodiceAOO();
	
	/**
	 * 
	 * @param codAOO Codice AOO della Folder
	 */
	public void setCodiceAOO(String codAOO);
		
	/**
	 * 
	 * @return Id dell'Utente che e' owner della Folder
	 */
	public String getFolderOwner();
	
	/**
	 * 
	 * @param owner Id dell'Utente che e' owner della Folder
	 */
	public void setFolderOwner(String owner);
		
	/**
	 * 
	 * @return Nome o Id della Folder Padre
	 */
	public String getParentFolderId();
	
	/**
	 * 
	 * @param parentFolderId Nome o Id della Folder Padre
	 */
	public void setParentFolderId(String parentFolderId);

	/**
	 * @return Lo stato di abilitazione della Folder
	 */
	public EnumBoolean getEnabled();
	
	/**
	 * @param enabled TRUE se abilitato, FALSE se disabilitato, UNSPECIFIED se non specificato
	 */
	public void setEnabled(EnumBoolean enabled);
	
	/**
	 * @return Le informazioni aggiuntive del profilo della Folder
	 */
	public Map<String, String> getExtraInfo();
	
	/**
	 * @param extraInfo Le informazioni aggiuntive del profilo della Folder
	 */
	public void setExtraInfo(Map<String, String> extraInfo);
}
