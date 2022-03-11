package it.filippetti.psso.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VIS_PSSOTIPODIETASPECIALE")
public class TipoDietaSpeciale implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TDI_COD")
	private String codice;

	@Column(name = "TDI_DES")
	private String descrizione;

	@Column(name = "TDI_FLGOBBLIGANO")
	private String obbligatorio;

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getObbligatorio() {
		return obbligatorio;
	}

	public void setObbligatorio(String obbligatorio) {
		this.obbligatorio = obbligatorio;
	}

}
