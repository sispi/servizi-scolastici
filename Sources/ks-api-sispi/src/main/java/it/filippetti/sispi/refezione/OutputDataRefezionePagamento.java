package it.filippetti.sispi.refezione;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutputDataRefezionePagamento {

	@JsonProperty("codice-domanda")
	private Integer codiceDomanda;

	@JsonProperty("des-dat-prot")
	private String desDatProt;

	@JsonProperty("num-prot")
	private Integer numProt;

	public Integer getCodiceDomanda() {
		return codiceDomanda;
	}

	public void setCodiceDomanda(Integer codiceDomanda) {
		this.codiceDomanda = codiceDomanda;
	}

	public String getDesDatProt() {
		return desDatProt;
	}

	public void setDesDatProt(String desDatProt) {
		this.desDatProt = desDatProt;
	}

	public Integer getNumProt() {
		return numProt;
	}

	public void setNumProt(Integer numProt) {
		this.numProt = numProt;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + hashCode() + " [codice-domanda=" + getCodiceDomanda()
				+ ", des-dat-prot=" + getDesDatProt() + ", num-prot=" + getNumProt() + "]";
	}

}
