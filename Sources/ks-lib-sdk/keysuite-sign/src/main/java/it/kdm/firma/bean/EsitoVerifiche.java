package it.kdm.firma.bean;

public class EsitoVerifiche {

	public static final String ESIG_PASSED = "PASSED";
	public static final String ESIG_TOTAL_PASSED = "TOTAL_PASSED";
	public static final String ESIG_IGNORED = "IGNORED";
	public static final String ESIG_STATUS_OK = "OK";
	public static final String ESIG_IDS_CERTIFICATE_REVOCATION = "BBB_XCV_ISCR";
	public static final String POSITIVE = "POSITIVO";
	public static final String NEGATIVE = "NEGATIVO";
	public static final String WARNING = "WARNING";
	public static final String NOT_SUPPORTED = "Non Supportato";
	
	public static final String NOME = "EsitoVerifiche";
	
	private VerificaFormato verificaFormato;
	private VerificaFirma verificaFirma;
	
	public EsitoVerifiche(VerificaFormato verificaFormato, VerificaFirma verificaFirma) {
		this.verificaFormato = verificaFormato;
		this.verificaFirma = verificaFirma;
	}

	public VerificaFormato getVerificaFormato() {
		return verificaFormato;
	}

	public void setVerificaFormato(VerificaFormato verificaFormato) {
		this.verificaFormato = verificaFormato;
	}

	public VerificaFirma getVerificaFirma() {
		return verificaFirma;
	}

	public void setVerificaFirma(VerificaFirma verificaFirma) {
		this.verificaFirma = verificaFirma;
	}
	
}
