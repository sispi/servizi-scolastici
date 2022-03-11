package it.filippetti.sispi.pagamentoretta;

public class DettaglioRataBean {

	public DettaglioRataBean() {
	}

	public DettaglioRataBean(Integer ordinamento, String descrizione, Float percentualeSconto,
			Integer meseRiferimento) {
		this.ordinamento = ordinamento;
		this.descrizione = descrizione;
		this.percentualeSconto = percentualeSconto;
		this.meseRiferimento = meseRiferimento;
	}

	private Integer ordinamento;

	private String descrizione;

	private Float percentualeSconto;

	private Integer meseRiferimento;

	public Integer getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Float getPercentualeSconto() {
		return percentualeSconto;
	}

	public void setPercentualeSconto(Float percentualeSconto) {
		this.percentualeSconto = percentualeSconto;
	}

	public Integer getMeseRiferimento() {
		return meseRiferimento;
	}

	public void setMeseRiferimento(Integer meseRiferimento) {
		this.meseRiferimento = meseRiferimento;
	}
}
