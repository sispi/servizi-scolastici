package it.kdm.docer.conservazione;

public class ConservazioneResult {

	private String errorCode;
	private String errorMessage;

	private String esito;

	private String xmlChiamata;
	private String xmlEsitoVersamento;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getXmlChiamata() {
		return xmlChiamata;
	}

	public void setXmlChiamata(String xmlChiamata) {
		this.xmlChiamata = xmlChiamata;
	}

	public String getXmlEsitoVersamento() {
		return xmlEsitoVersamento;
	}

	public void setXmlEsitoVersamento(String xmlEsitoVersamento) {
		this.xmlEsitoVersamento = xmlEsitoVersamento;
	}
}
