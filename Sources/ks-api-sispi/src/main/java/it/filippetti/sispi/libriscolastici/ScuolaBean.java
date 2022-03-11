package it.filippetti.sispi.libriscolastici;

import java.io.Serializable;

public class ScuolaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id_scuola;

	private String denominazione_scuola;

	private String comune_scuola;

	private String provincia_scuola;

	private String indirizzo_scuola;

	private String cap_scuola;

	private String civico_scuola;

	private String telefono_scuola;

	private String istituzione_scolastica;

	public String getId_scuola() {
		return id_scuola;
	}

	public void setId_scuola(String id_scuola) {
		this.id_scuola = id_scuola;
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

	public String getCap_scuola() {
		return cap_scuola;
	}

	public void setCap_scuola(String cap_scuola) {
		this.cap_scuola = cap_scuola;
	}

	public String getCivico_scuola() {
		return civico_scuola;
	}

	public void setCivico_scuola(String civico_scuola) {
		this.civico_scuola = civico_scuola;
	}

	public String getTelefono_scuola() {
		return telefono_scuola;
	}

	public void setTelefono_scuola(String telefono_scuola) {
		this.telefono_scuola = telefono_scuola;
	}

	public String getIstituzione_scolastica() {
		return istituzione_scolastica;
	}

	public void setIstituzione_scolastica(String istituzione_scolastica) {
		this.istituzione_scolastica = istituzione_scolastica;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + hashCode() + " [id_scuola=" + getId_scuola()
				+ ", denominazione_scuola=" + getDenominazione_scuola() + ", indirizzo_scuola=" + getIndirizzo_scuola()
				+ "]";
	}
}
