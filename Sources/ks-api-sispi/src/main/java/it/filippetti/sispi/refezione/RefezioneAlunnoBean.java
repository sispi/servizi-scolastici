package it.filippetti.sispi.refezione;

import java.io.Serializable;

public class RefezioneAlunnoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String esito;

	private String anno_refezione;

	private String codice_domanda;

	private String codicefiscale;

	private String cognome;

	private String data_nascita;

	private String descrizione_scuola;

	private String giorni_funzionali;

	private String importo_tariffa;

	private String luogo_nascita;

	private String nome;

	private String protocollo_domanda;

	private String tipo_tariffa;

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getAnno_refezione() {
		return anno_refezione;
	}

	public void setAnno_refezione(String anno_refezione) {
		this.anno_refezione = anno_refezione;
	}

	public String getCodice_domanda() {
		return codice_domanda;
	}

	public void setCodice_domanda(String codice_domanda) {
		this.codice_domanda = codice_domanda;
	}

	public String getCodicefiscale() {
		return codicefiscale;
	}

	public void setCodicefiscale(String codicefiscale) {
		this.codicefiscale = codicefiscale;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getData_nascita() {
		return data_nascita;
	}

	public void setData_nascita(String data_nascita) {
		this.data_nascita = data_nascita;
	}

	public String getDescrizione_scuola() {
		return descrizione_scuola;
	}

	public void setDescrizione_scuola(String descrizione_scuola) {
		this.descrizione_scuola = descrizione_scuola;
	}

	public String getGiorni_funzionali() {
		return giorni_funzionali;
	}

	public void setGiorni_funzionali(String giorni_funzionali) {
		this.giorni_funzionali = giorni_funzionali;
	}

	public String getImporto_tariffa() {
		return importo_tariffa;
	}

	public void setImporto_tariffa(String importo_tariffa) {
		this.importo_tariffa = importo_tariffa;
	}

	public String getLuogo_nascita() {
		return luogo_nascita;
	}

	public void setLuogo_nascita(String luogo_nascita) {
		this.luogo_nascita = luogo_nascita;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getProtocollo_domanda() {
		return protocollo_domanda;
	}

	public void setProtocollo_domanda(String protocollo_domanda) {
		this.protocollo_domanda = protocollo_domanda;
	}

	public String getTipo_tariffa() {
		return tipo_tariffa;
	}

	public void setTipo_tariffa(String tipo_tariffa) {
		this.tipo_tariffa = tipo_tariffa;
	}

}
