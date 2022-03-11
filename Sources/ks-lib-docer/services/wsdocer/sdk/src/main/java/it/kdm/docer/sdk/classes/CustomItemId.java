package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.interfaces.ICustomItemId;

public class CustomItemId implements ICustomItemId {

	String type = null;
	String codiceCustom = null;
	String codiceEnte = null;
	String codiceAOO = null;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getCodiceCustom() {
		return codiceCustom;
	}

	public void setCodiceCustom(String codiceCustom) {
		this.codiceCustom = codiceCustom;
	}
	
	public String getCodiceEnte() {
		return codiceEnte;
	}
	
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}

	public String getCodiceAOO() {
		return codiceAOO;
	}

	public void setCodiceAOO(String codiceAOO) {
		this.codiceAOO = codiceAOO;
	}

}
