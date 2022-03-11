package it.kdm.docer.conservazione.batch;

import it.kdm.docer.conservazione.EsitoConservazione;

public class ConservazioneResult {
	
	private String errorCode;
	private EsitoConservazione esito;
	private String errorMessage;
	
	private String xmlChiamata;
	private String xmlEsitoVersamento;

	public ConservazioneResult(it.kdm.docer.clients.WSConservazioneBatchStub.ConservazioneResult result) {
		this.errorCode = result.getErrorCode();
		this.errorMessage = result.getErrorMessage();
		this.esito = EsitoConservazione.valueOf(result.getEsito());
		
		this.xmlChiamata = result.getXmlChiamata();
		this.xmlEsitoVersamento = result.getXmlEsitoVersamento();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public EsitoConservazione getEsito() {
		return esito;
	}

	public void setEsito(EsitoConservazione esito) {
		this.esito = esito;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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
