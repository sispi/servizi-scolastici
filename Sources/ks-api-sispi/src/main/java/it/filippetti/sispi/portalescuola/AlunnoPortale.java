package it.filippetti.sispi.portalescuola;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlunnoPortale {

	@JsonProperty("codice-scuola")
	private String idScuola;

	@JsonProperty("des-anno-frequenza")
	private String annoFrequenza;

	@JsonProperty("des-barrato")
	private String barrato;

	@JsonProperty("des-cap")
	private String cap;

	@JsonProperty("des-classe")
	private String classe;

	@JsonProperty("des-codice-fiscale")
	private String codiceFiscale;

	@JsonProperty("des-cognome")
	private String cognome;

	@JsonProperty("des-comune-nascita")
	private String comuneNascita;

	@JsonProperty("des-comune-residenza")
	private String comuneResidenza;

	@JsonProperty("des-data-nascita")
	private String dataNascita;

	@JsonProperty("des-indirizzo")
	private String indirizzo;

	@JsonProperty("des-nome")
	private String nome;

	@JsonProperty("des-sezione")
	private String sezione;

	@JsonProperty("num-civico")
	private String numeroCivico;

	public String getIdScuola() {
		return idScuola;
	}

	public void setIdScuola(String idScuola) {
		this.idScuola = idScuola;
	}

	public String getAnnoFrequenza() {
		return annoFrequenza;
	}

	public void setAnnoFrequenza(String annoFrequenza) {
		this.annoFrequenza = annoFrequenza;
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

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
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

	public String getComuneNascita() {
		return comuneNascita;
	}

	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	public String getComuneResidenza() {
		return comuneResidenza;
	}

	public void setComuneResidenza(String comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getNumeroCivico() {
		return numeroCivico;
	}

	public void setNumeroCivico(String numeroCivico) {
		this.numeroCivico = numeroCivico;
	}

}
