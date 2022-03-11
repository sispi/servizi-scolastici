package it.kdm.firma.bean;

public class EsitoFirma {
	public static final String NOME = "EsitoFirma";
	public static final String NOME_CONTROLLO_CONFORMITA = "ControlloConformita";
	
	private String controlloConformita;
	private VerificaFirmaInt verificaFirma;
	
	public EsitoFirma(String controlloConformita, VerificaFirmaInt verificaFirma) {
		this.controlloConformita = controlloConformita;
		this.verificaFirma = verificaFirma;
	}

	@Override
	public String toString(){
		return this.controlloConformita;
	}

	public String getControlloConformita() {
		return controlloConformita;
	}

	public void setControlloConformita(String controlloConformita) {
		this.controlloConformita = controlloConformita;
	}

	public VerificaFirmaInt getVerificaFirma() {
		return verificaFirma;
	}

	public void setVerificaFirma(VerificaFirmaInt verificaFirma) {
		this.verificaFirma = verificaFirma;
	}
	
}