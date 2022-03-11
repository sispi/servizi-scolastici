package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.interfaces.IEnteId;

public class EnteId implements IEnteId{

	private String codiceEnte = null;
	
	public String getCodiceEnte() {		
		return this.codiceEnte;
	}

	public void setCodiceEnte(String codEnte) {
		this.codiceEnte = codEnte;
		
	}

}
