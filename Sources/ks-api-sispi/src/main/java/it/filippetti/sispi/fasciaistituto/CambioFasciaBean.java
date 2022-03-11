package it.filippetti.sispi.fasciaistituto;

import java.io.Serializable;

public class CambioFasciaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String annoScolastico;

	private String nomeIstituto;

	private String fascia;

	private String fasciaOraria;

	private String fasciaOrariaNuova;

	private Integer meseRiferimento;

	private String mesiSuccessivi;

	private String causale;

	public String getAnnoScolastico() {
		return annoScolastico;
	}

	public void setAnnoScolastico(String annoScolastico) {
		this.annoScolastico = annoScolastico;
	}

	public String getNomeIstituto() {
		return nomeIstituto;
	}

	public void setNomeIstituto(String nomeIstituto) {
		this.nomeIstituto = nomeIstituto;
	}

	public String getFascia() {
		return fascia;
	}

	public void setFascia(String fascia) {
		this.fascia = fascia;
	}

	public String getFasciaOraria() {
		return fasciaOraria;
	}

	public void setFasciaOraria(String fasciaOraria) {
		this.fasciaOraria = fasciaOraria;
	}

	public String getFasciaOrariaNuova() {
		return fasciaOrariaNuova;
	}

	public void setFasciaOrariaNuova(String fasciaOrariaNuova) {
		this.fasciaOrariaNuova = fasciaOrariaNuova;
	}

	public Integer getMeseRiferimento() {
		return meseRiferimento;
	}

	public void setMeseRiferimento(Integer meseRiferimento) {
		this.meseRiferimento = meseRiferimento;
	}

	public String getMesiSuccessivi() {
		return mesiSuccessivi;
	}

	public void setMesiSuccessivi(String mesiSuccessivi) {
		this.mesiSuccessivi = mesiSuccessivi;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + hashCode() + " [nomeIstituto=" + getNomeIstituto() + ", fascia="
				+ getFascia() + ", fasciaOraria=" + getFasciaOraria() + ", fasciaOrariaNuova=" + getFasciaOrariaNuova()
				+ ", meseRiferimento=" + getMeseRiferimento() + ", mesiSuccessivi=" + getMesiSuccessivi() + "]";
	}

}
