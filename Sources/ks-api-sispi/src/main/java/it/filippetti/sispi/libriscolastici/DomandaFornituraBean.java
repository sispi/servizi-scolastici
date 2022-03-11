package it.filippetti.sispi.libriscolastici;

import java.io.Serializable;

public class DomandaFornituraBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String esito;

	private String nome;

	private String cognome;

	private String codicefiscale;

	private String data_nascita;

	private String luogo_nascita;

	private String indirizzo;

	private String civico;

	private String cap;

	private String comune;

	private String denominazione_scuola;

	private String comune_scuola;

	private String provincia_scuola;

	private String indirizzo_scuola;

	private String civico_scuola;

	private String cap_scuola;

	private String telefono_scuola;

	private String classe_scuola;

	private ScuolaBean scuola;

	private String anno_scolastico;

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCodicefiscale() {
		return codicefiscale;
	}

	public void setCodicefiscale(String codicefiscale) {
		this.codicefiscale = codicefiscale;
	}

	public String getData_nascita() {
		return data_nascita;
	}

	public void setData_nascita(String data_nascita) {
		this.data_nascita = data_nascita;
	}

	public String getLuogo_nascita() {
		return luogo_nascita;
	}

	public void setLuogo_nascita(String luogo_nascita) {
		this.luogo_nascita = luogo_nascita;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getDenominazione_scuola() {
		return denominazione_scuola;
	}

	public void setDenominazione_scuola(String denominazione_scuola) {
		this.denominazione_scuola = denominazione_scuola;
	}

	public String getComune_scuola() {
		return comune_scuola;
	}

	public void setComune_scuola(String comune_scuola) {
		this.comune_scuola = comune_scuola;
	}

	public String getProvincia_scuola() {
		return provincia_scuola;
	}

	public void setProvincia_scuola(String provincia_scuola) {
		this.provincia_scuola = provincia_scuola;
	}

	public String getIndirizzo_scuola() {
		return indirizzo_scuola;
	}

	public void setIndirizzo_scuola(String indirizzo_scuola) {
		this.indirizzo_scuola = indirizzo_scuola;
	}

	public String getCivico_scuola() {
		return civico_scuola;
	}

	public void setCivico_scuola(String civico_scuola) {
		this.civico_scuola = civico_scuola;
	}

	public String getCap_scuola() {
		return cap_scuola;
	}

	public void setCap_scuola(String cap_scuola) {
		this.cap_scuola = cap_scuola;
	}

	public String getTelefono_scuola() {
		return telefono_scuola;
	}

	public void setTelefono_scuola(String telefono_scuola) {
		this.telefono_scuola = telefono_scuola;
	}

	public String getClasse_scuola() {
		return classe_scuola;
	}

	public void setClasse_scuola(String classe_scuola) {
		this.classe_scuola = classe_scuola;
	}

	public ScuolaBean getScuola() {
		return scuola;
	}

	public void setScuola(ScuolaBean scuola) {
		this.scuola = scuola;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + hashCode() + " [esito=" + getEsito() + ", codicefiscale="
				+ getCodicefiscale() + ", scuola=" + getScuola() + "]";
	}

	public String getAnno_scolastico() {
		return anno_scolastico;
	}

	public void setAnno_scolastico(String anno_scolastico) {
		this.anno_scolastico = anno_scolastico;
	}

}
