package it.kdm.docer.sdk.classes;

import java.util.HashMap;
import java.util.Map;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.ICustomItemInfo;

public class CustomItemInfo implements ICustomItemInfo {

	String type = null;
	String codiceCustom = null;
	String descrizioneCustom = null;
	String codiceEnte = null;
	String codiceAOO = null;
	EnumBoolean enabled = EnumBoolean.UNSPECIFIED;
	Map<String, String> extraInfo = new HashMap<String, String>();
	
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getCodiceCustom() {
		return this.codiceCustom;
	}

	public void setCodiceCustom(String codiceCustom) {
		this.codiceCustom = codiceCustom;
	}

	public String getCodiceEnte() {
		return this.codiceEnte;
	}


	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}

	public String getCodiceAOO() {
		return this.codiceAOO;
	}

	public void setCodiceAOO(String codiceAOO) {
		this.codiceAOO = codiceAOO;
	}
	
	public String getDescrizione() {
		return this.descrizioneCustom;
	}

	public void setDescrizione(String descrizione) {
		this.descrizioneCustom = descrizione;

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
