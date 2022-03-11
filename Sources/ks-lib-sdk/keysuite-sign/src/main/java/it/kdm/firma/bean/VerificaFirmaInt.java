package it.kdm.firma.bean;

public class VerificaFirmaInt {
	public static final String NOME = "VerificaFirma";
	public static final String NOME_CODICE_ESITO = "CodiceEsito";
	public static final String NOME_CONTROLLO_CRITTOGRAFICO = "ControlloCrittografico";
	public static final String NOME_CONTROLLO_CATENA_TRUSTED = "ControlloCatenaTrusted";
	public static final String NOME_CONTROLLO_CERTIFICATO = "ControlloCertificato";
	public static final String NOME_CONTROLLO_CRL = "ControlloCRL";
	
	private String codiceEsito;
	private String controlloCrittografico;
	private String controlloCatenaTrusted;
	private String controlloCertificato;
	private String controlloCRL;
	
	public VerificaFirmaInt(String codiceEsito, String controlloCrittografico, String controlloCatenaTrusted, 
			String controlloCertificato, String controlloCRL) {
		this.codiceEsito = codiceEsito;
		this.controlloCrittografico = controlloCrittografico;
		this.controlloCatenaTrusted = controlloCatenaTrusted;
		this.controlloCertificato = controlloCertificato;
		this.controlloCRL = controlloCRL;
	}

	public String getCodiceEsito() {
		return codiceEsito;
	}

	public void setCodiceEsito(String codiceEsito) {
		this.codiceEsito = codiceEsito;
	}

	public String getControlloCrittografico() {
		return controlloCrittografico;
	}

	public void setControlloCrittografico(String controlloCrittografico) {
		this.controlloCrittografico = controlloCrittografico;
	}

	public String getControlloCatenaTrusted() {
		return controlloCatenaTrusted;
	}

	public void setControlloCatenaTrusted(String controlloCatenaTrusted) {
		this.controlloCatenaTrusted = controlloCatenaTrusted;
	}

	public String getControlloCertificato() {
		return controlloCertificato;
	}

	public void setControlloCertificato(String controlloCertificato) {
		this.controlloCertificato = controlloCertificato;
	}

	public String getControlloCRL() {
		return controlloCRL;
	}

	public void setControlloCRL(String controlloCRL) {
		this.controlloCRL = controlloCRL;
	}
	
	public String getCompliancyString() {
		if (EsitoVerifiche.POSITIVE.equals(codiceEsito) &&
			EsitoVerifiche.POSITIVE.equals(controlloCrittografico) &&
			EsitoVerifiche.POSITIVE.equals(controlloCatenaTrusted) &&
			EsitoVerifiche.POSITIVE.equals(controlloCertificato) &&
			EsitoVerifiche.POSITIVE.equals(controlloCRL)) {
				return EsitoVerifiche.POSITIVE;
			} else if (EsitoVerifiche.POSITIVE.equals(codiceEsito) ||
			EsitoVerifiche.POSITIVE.equals(controlloCrittografico) ||
			EsitoVerifiche.POSITIVE.equals(controlloCatenaTrusted) ||
			EsitoVerifiche.POSITIVE.equals(controlloCertificato) ||
			EsitoVerifiche.POSITIVE.equals(controlloCRL)) {
				return EsitoVerifiche.WARNING;
			} else {
				return EsitoVerifiche.NEGATIVE;
			}
	}
	
}