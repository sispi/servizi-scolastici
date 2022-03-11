package it.kdm.docer.sdk.classes;

import java.util.HashMap;
import java.util.Map;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.IAOOInfo;

public class AOOInfo implements IAOOInfo{

    String codice = null;
    String descrizione = null;
    EnumBoolean enabled = EnumBoolean.UNSPECIFIED;
    String enteId = null;
    Map<String, String> extraInfo = new HashMap<String, String>();
	
	public String getCodiceAOO() {		
		return this.codice;
	}
	
	public void setCodiceAOO(String codiceAOO) {
		this.codice = codiceAOO;
	}
	
	public String getDescrizione() {
		return this.descrizione;
	}
	
	public void setDescrizione(String descrizione) {		
		this.descrizione = descrizione;
	}
	
	public String getCodiceEnte() {
		return this.enteId;
	}

	public void setCodiceEnte(String codiceEnte) {
		this.enteId = codiceEnte;
	}

	public EnumBoolean getEnabled() {
		return this.enabled;
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
