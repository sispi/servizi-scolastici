package it.filippetti.sispi.portalescuola;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScuolaPortale {

	@JsonProperty("codice-scuola")
	private String idScuola;

	@JsonProperty("des-barrato")
	private String barrato;

	@JsonProperty("des-cap")
	private String cap;

	@JsonProperty("des-comune-scuola")
	private String descrizioneComuneScuola;

	@JsonProperty("des-denominazione")
	private String denominazione;

	@JsonProperty("des-indirizzo")
	private String indirizzo;

	@JsonProperty("des-istituto")
	private String istituto;

	@JsonProperty("des-istituzione-scuola")
	private String descrizioneIstituzioneScuola;

	@JsonProperty("des-prov-scuola")
	private String provincia;

	@JsonProperty("des-scuola")
	private String scuola;

	@JsonProperty("des-telefono")
	private String telefono;

	@JsonProperty("des-tipo-scuola")
	private String tipoScuola;

	@JsonProperty("des-via")
	private String via;

	@JsonProperty("num-civico")
	private String numCivico;

	@JsonProperty("tsu-cod")
	private String codiceTipoScuola;

	@JsonProperty("via-cod")
	private String codiceScuola;

	public String getIdScuola() {
		return idScuola;
	}

	public void setIdScuola(String idScuola) {
		this.idScuola = idScuola;
	}

	public String getBarrato() {
		return barrato;
	}

	public void setBarrato(String barrato) {
		this.barrato = barrato;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getDescrizioneComuneScuola() {
		return descrizioneComuneScuola;
	}

	public void setDescrizioneComuneScuola(String descrizioneComuneScuola) {
		this.descrizioneComuneScuola = descrizioneComuneScuola;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getIstituto() {
		return istituto;
	}

	public void setIstituto(String istituto) {
		this.istituto = istituto;
	}

	public String getDescrizioneIstituzioneScuola() {
		return descrizioneIstituzioneScuola;
	}

	public void setDescrizioneIstituzioneScuola(String descrizioneIstituzioneScuola) {
		this.descrizioneIstituzioneScuola = descrizioneIstituzioneScuola;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getScuola() {
		return scuola;
	}

	public void setScuola(String scuola) {
		this.scuola = scuola;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipoScuola() {
		return tipoScuola;
	}

	public void setTipoScuola(String tipoScuola) {
		this.tipoScuola = tipoScuola;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getNumCivico() {
		return numCivico;
	}

	public void setNumCivico(String numCivico) {
		this.numCivico = numCivico;
	}

	public String getCodiceTipoScuola() {
		return codiceTipoScuola;
	}

	public void setCodiceTipoScuola(String codiceTipoScuola) {
		this.codiceTipoScuola = codiceTipoScuola;
	}

	public String getCodiceScuola() {
		return codiceScuola;
	}

	public void setCodiceScuola(String codiceScuola) {
		this.codiceScuola = codiceScuola;
	}
}
