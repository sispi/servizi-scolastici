package it.filippetti.psso.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "VIS_PSSOSCUOLAALUNNOREF")
public class ScuolaAlunno implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CODICE_ALUNNO")
	private String codiceAlunno;

	@Column(name = "COGNOME_ALUNNO")
	private String cognomeAlunno;

	@Column(name = "NOME_ALUNNO")
	private String nomeAlunno;

	@Column(name = "CODICE_FISCALE_ALUNNO")
	private String codiceFiscaleAlunno;

	@Column(name = "DATA_NASCITA_ALUNNO")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataNascitaAlunno;

	@Column(name = "LUOGO_NASCITA_ALUNNO")
	private String luogoNascitaAlunno;

	@Column(name = "SESSO_ALUNNO")
	private String sessoAlunno;

	@Column(name = "CF_RESPONSABILE")
	private String cfResponsabile;

	@Column(name = "CF_CORRESPONSABILE")
	private String cfCorresponsabile;

	@Column(name = "INDIRIZZO_RESIDENZA")
	private String indirizzoResidenza;

	@Column(name = "NUMERO_CIVICO_RESIDENZA")
	private String numeroCivicoResidenza;

	@Column(name = "CAP_RESIDENZA")
	private String capResidenza;

	@Column(name = "COMUNE_RESIDENZA")
	private String comuneResidenza;

	@Column(name = "PROVINCIA_RESIDENZA")
	private String provinciaResidenza;

	@Column(name = "CODICE_SCUOLA_FREQUENZA")
	private String codiceScuolaFrequenza;

	@Column(name = "SCUOLA_FREQUENZA")
	private String scuolaFrequenza;

	@Column(name = "CODICE_TIPO_SCUOLA")
	private String codiceTipoScuola;

	@Column(name = "TIPO_SCUOLA")
	private String tipoScuola;

	@Column(name = "CODICE_TIPO_CLASSE")
	private String codiceTipoClasse;

	@Column(name = "TIPO_CLASSE")
	private String tipoClasse;

	@Column(name = "SEZIONE")
	private String sezione;

	@Column(name = "ANNO_FREQUENZA")
	private String annoFrequenza;

	@Column(name = "GG_FUNZIONALI")
	private String ggFunzionali;

	@Column(name = "SERVIZIO_MENSA")
	private String servizioMensa;

	@Column(name = "SOSPESO")
	private String sospeso;

	@Column(name = "ISTANZA")
	private String istanza;

	public String getCodiceAlunno() {
		return codiceAlunno;
	}

	public void setCodiceAlunno(String codiceAlunno) {
		this.codiceAlunno = codiceAlunno;
	}

	public String getCognomeAlunno() {
		return cognomeAlunno;
	}

	public void setCognomeAlunno(String cognomeAlunno) {
		this.cognomeAlunno = cognomeAlunno;
	}

	public String getNomeAlunno() {
		return nomeAlunno;
	}

	public void setNomeAlunno(String nomeAlunno) {
		this.nomeAlunno = nomeAlunno;
	}

	public String getCodiceFiscaleAlunno() {
		return codiceFiscaleAlunno;
	}

	public void setCodiceFiscaleALunno(String codiceFiscaleAlunno) {
		this.codiceFiscaleAlunno = codiceFiscaleAlunno;
	}

	public LocalDate getDataNascitaAlunno() {
		return dataNascitaAlunno;
	}

	public void setDataNascitaAlunno(LocalDate dataNascitaAlunno) {
		this.dataNascitaAlunno = dataNascitaAlunno;
	}

	public String getLuogoNascitaAlunno() {
		return luogoNascitaAlunno;
	}

	public void setLuogoNascitaAlunno(String luogoNascitaAlunno) {
		this.luogoNascitaAlunno = luogoNascitaAlunno;
	}

	public String getSessoAlunno() {
		return sessoAlunno;
	}

	public void setSessoAlunno(String sessoAlunno) {
		this.sessoAlunno = sessoAlunno;
	}

	public String getCfResponsabile() {
		return cfResponsabile;
	}

	public void setCfResponsabile(String cfResponsabile) {
		this.cfResponsabile = cfResponsabile;
	}

	public String getCfCorresponsabile() {
		return cfCorresponsabile;
	}

	public void setCfCorresponsabile(String cfCorresponsabile) {
		this.cfCorresponsabile = cfCorresponsabile;
	}

	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}

	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}

	public String getNumeroCivicoResidenza() {
		return numeroCivicoResidenza;
	}

	public void setNumeroCivicoResidenza(String numeroCivicoResidenza) {
		this.numeroCivicoResidenza = numeroCivicoResidenza;
	}

	public String getCapResidenza() {
		return capResidenza;
	}

	public void setCapResidenza(String capResidenza) {
		this.capResidenza = capResidenza;
	}

	public String getComuneResidenza() {
		return comuneResidenza;
	}

	public void setComuneResidenza(String comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}

	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}

	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}

	public String getCodiceScuolaFrequenza() {
		return codiceScuolaFrequenza;
	}

	public void setCodiceScuolaFrequenza(String codiceScuolaFrequenza) {
		this.codiceScuolaFrequenza = codiceScuolaFrequenza;
	}

	public String getScuolaFrequenza() {
		return scuolaFrequenza;
	}

	public void setScuolaFrequenza(String scuolaFrequenza) {
		this.scuolaFrequenza = scuolaFrequenza;
	}

	public String getCodiceTipoScuola() {
		return codiceTipoScuola;
	}

	public void setCodiceTipoScuola(String codiceTipoScuola) {
		this.codiceTipoScuola = codiceTipoScuola;
	}

	public String getTipoScuola() {
		return tipoScuola;
	}

	public void setTipoScuola(String tipoScuola) {
		this.tipoScuola = tipoScuola;
	}

	public String getCodiceTipoClasse() {
		return codiceTipoClasse;
	}

	public void setCodiceTipoClasse(String codiceTipoClasse) {
		this.codiceTipoClasse = codiceTipoClasse;
	}

	public String getTipoClasse() {
		return tipoClasse;
	}

	public void setTipoClasse(String tipoClasse) {
		this.tipoClasse = tipoClasse;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getAnnoFrequenza() {
		return annoFrequenza;
	}

	public void setAnnoFrequenza(String annoFrequenza) {
		this.annoFrequenza = annoFrequenza;
	}

	public String getGgFunzionali() {
		return ggFunzionali;
	}

	public void setGgFunzionali(String ggFunzionali) {
		this.ggFunzionali = ggFunzionali;
	}

	public String getServizioMensa() {
		return servizioMensa;
	}

	public void setServizioMensa(String servizioMensa) {
		this.servizioMensa = servizioMensa;
	}

	public String getSospeso() {
		return sospeso;
	}

	public void setSospeso(String sospeso) {
		this.sospeso = sospeso;
	}

	public String getIstanza() {
		return istanza;
	}

	public void setIstanza(String istanza) {
		this.istanza = istanza;
	}
}
