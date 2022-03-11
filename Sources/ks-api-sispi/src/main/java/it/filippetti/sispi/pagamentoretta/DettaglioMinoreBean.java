package it.filippetti.sispi.pagamentoretta;

import java.io.Serializable;

public class DettaglioMinoreBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;

	private String cognome;

	private String codicefiscale;

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

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + hashCode() + " [nome=" + getNome() + ", cognome=" + getCognome()
				+ ", codicefiscale=" + getCodicefiscale() + "]";
	}
}
