package it.filippetti.sispi.centroinfanzia;

import java.io.Serializable;

public class DettaglioIscrizioneCentroInfanziaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String nomeIstituto;

	private String fascia;

	private String fasciaOraria;

	private String motivazioneVariazione;

	private Float valoreConguaglio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getMotivazioneVariazione() {
		return motivazioneVariazione;
	}

	public void setMotivazioneVariazione(String motivazioneVariazione) {
		this.motivazioneVariazione = motivazioneVariazione;
	}

	public Float getValoreConguaglio() {
		return valoreConguaglio;
	}

	public void setValoreConguaglio(Float valoreConguaglio) {
		this.valoreConguaglio = valoreConguaglio;
	}
}
