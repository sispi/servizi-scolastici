package it.kdm.docer.sdk.classes;

import java.util.HashMap;
import java.util.Map;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.IEnteInfo;

public class EnteInfo implements IEnteInfo{
	
    String nomeEnte = null;
    String descrizione = null;
    EnumBoolean enabled = EnumBoolean.UNSPECIFIED;
    Map<String, String> extraInfo = new HashMap<String, String>();

	
	public String getCodiceEnte() {		
		return nomeEnte;
	}
	
	public void setCodiceEnte(String codEnte) {
		this.nomeEnte = codEnte;
	}	
	
	public String getDescrizione() {		
		return descrizione;
	}	
	
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;		
	}		

	public EnumBoolean getEnabled() {		
		return enabled;
	}
	public void setEnabled(EnumBoolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, String> getExtraInfo() {
		return this.extraInfo;
	}

	public void setExtraInfo(Map<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}
		
}
