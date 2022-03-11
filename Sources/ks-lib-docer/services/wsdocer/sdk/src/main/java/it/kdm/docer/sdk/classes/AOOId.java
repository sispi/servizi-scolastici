package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.interfaces.IAOOId;

public class AOOId implements IAOOId {

	private String codiceEnte = null;
	private String codiceAoo = null;
	
	public String getCodiceEnte() {		
		return this.codiceEnte;
	}

	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}

	public String getCodiceAOO() {		
		return this.codiceAoo;
	}

	public void setCodiceAOO(String codiceAOO) {
		this.codiceAoo = codiceAOO;
	}



}
