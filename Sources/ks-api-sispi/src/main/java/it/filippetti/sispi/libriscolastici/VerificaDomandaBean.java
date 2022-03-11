package it.filippetti.sispi.libriscolastici;

import java.io.Serializable;

public class VerificaDomandaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String esito;

	private String cf_richiedente;

	private String cf_minore;

	private String data_presentazione;

	private String numero_protocollo;

	private String anno_protocollo;

	private String anno_scolastico;

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getCf_richiedente() {
		return cf_richiedente;
	}

	public void setCf_richiedente(String cf_richiedente) {
		this.cf_richiedente = cf_richiedente;
	}

	public String getCf_minore() {
		return cf_minore;
	}

	public void setCf_minore(String cf_minore) {
		this.cf_minore = cf_minore;
	}

	public String getData_presentazione() {
		return data_presentazione;
	}

	public void setData_presentazione(String data_presentazione) {
		this.data_presentazione = data_presentazione;
	}

	public String getNumero_protocollo() {
		return numero_protocollo;
	}

	public void setNumero_protocollo(String numero_protocollo) {
		this.numero_protocollo = numero_protocollo;
	}

	public String getAnno_protocollo() {
		return anno_protocollo;
	}

	public void setAnno_protocollo(String anno_protocollo) {
		this.anno_protocollo = anno_protocollo;
	}

	public String getAnno_scolastico() {
		return anno_scolastico;
	}

	public void setAnno_scolastico(String anno_scolastico) {
		this.anno_scolastico = anno_scolastico;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + hashCode() + " [esito=" + getEsito() + ", cf_richiedente="
				+ getCf_richiedente() + ", cf_minore=" + getCf_minore() + ", data_presentazione="
				+ getData_presentazione() + ", numero_protocollo=" + getNumero_protocollo() + ", anno_protocollo="
				+ getAnno_protocollo() + ", anno_scolastico=" + getAnno_scolastico() + "]";
	}

}
