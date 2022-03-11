package it.kdm.docer.sdk.classes;

import java.util.HashMap;
import java.util.Map;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.IFolderInfo;

public class FolderInfo implements IFolderInfo {
 
    String folderId = null;
	String folderName = null;
	String descrizione = null;
	String codiceEnte = null;
	String codiceAOO = null;
	String folderOwner = null; 
	String parentFolderId = null;
	EnumBoolean enabled = EnumBoolean.UNSPECIFIED;
	Map<String, String> extraInfo = new HashMap<String, String>(); 	
	
	public String getFolderId() {
        return folderId;
    }
    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }
    
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodiceEnte() {
		return codiceEnte;
	}

	public void setCodiceEnte(String codEnte) {
		this.codiceEnte = codEnte;
	}

	public String getCodiceAOO() {
		return codiceAOO;
	}

	public void setCodiceAOO(String codAOO) {
		this.codiceAOO = codAOO;
	}

	public String getFolderOwner() {
		return folderOwner;
	}

	public void setFolderOwner(String owner) {
		this.folderOwner = owner;
	}

	public String getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId; 

	}

	public EnumBoolean getEnabled() {
		return enabled;
	}

	public void setEnabled(EnumBoolean enabled) {
		this.enabled = enabled;

	}

	public Map<String, String> getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(Map<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}

}
