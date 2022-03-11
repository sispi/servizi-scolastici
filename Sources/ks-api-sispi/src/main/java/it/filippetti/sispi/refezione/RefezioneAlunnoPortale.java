package it.filippetti.sispi.refezione;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefezioneAlunnoPortale {

	@JsonProperty("anno-refezione")
	private String annoRefezione;

	@JsonProperty("codice-domanda")
	private String codiceDomanda;

	@JsonProperty("codice-fiscale")
	private String codiceFiscale;

	@JsonProperty("cognome")
	private String cognome;

	@JsonProperty("data-nascita")
	private String dataNascita;

	@JsonProperty("descrizione-scuola")
	private String descrizioneScuola;

	@JsonProperty("giorni-funzionali")
	private String giorniFunzionali;

	@JsonProperty("importo-tariffa")
	private String importoTariffa;

	@JsonProperty("luogo-nascita")
	private String luogoNascita;

	@JsonProperty("nome")
	private String nome;

	@JsonProperty("protocollo-domanda")
	private String protocolloDomanda;

	@JsonProperty("tipo-tariffa")
	private String tipoTariffa;

	public String getAnnoRefezione() {
		return annoRefezione;
	}

	public void setAnnoRefezione(String annoRefezione) {
		this.annoRefezione = annoRefezione;
	}

	public String getCodiceDomanda() {
		return codiceDomanda;
	}

	public void setCodiceDomanda(String codiceDomanda) {
		this.codiceDomanda = codiceDomanda;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getDescrizioneScuola() {
		return descrizioneScuola;
	}

	public void setDescrizioneScuola(String descrizioneScuola) {
		this.descrizioneScuola = descrizioneScuola;
	}

	public String getGiorniFunzionali() {
		return giorniFunzionali;
	}

	public void setGiorniFunzionali(String giorniFunzionali) {
		this.giorniFunzionali = giorniFunzionali;
	}

	public String getImportoTariffa() {
		return importoTariffa;
	}

	public void setImportoTariffa(String importoTariffa) {
		this.importoTariffa = importoTariffa;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getProtocolloDomanda() {
		return protocolloDomanda;
	}

	public void setProtocolloDomanda(String protocolloDomanda) {
		this.protocolloDomanda = protocolloDomanda;
	}

	public String getTipoTariffa() {
		return tipoTariffa;
	}

	public void setTipoTariffa(String tipoTariffa) {
		this.tipoTariffa = tipoTariffa;
	}
}
