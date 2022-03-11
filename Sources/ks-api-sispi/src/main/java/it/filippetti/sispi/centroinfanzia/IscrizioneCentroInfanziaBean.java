package it.filippetti.sispi.centroinfanzia;

import java.io.Serializable;

public class IscrizioneCentroInfanziaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String nomeRichiedente;

	private String cognomeRichiedente;

	private String tipoRichiedente;

	private String nomeMinore;

	private String cognomeMinore;

	private String dataNascitaMinore;

	private String emailUtente;

	private String cfRichiedente;

	private String esitoValutazione;

	private Long idFamiglia;

	private String cfMinore;

	private String confermataIscrizione;

	private String dataInizioIscrizione;

	private String dataFineIscrizione;

	private String annoScolastico;

	private String nomeIstituto;

	private String fascia;

	private String fasciaOraria;

	private String confermaNotificata;

	private String ricalcoloRate;

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + hashCode() + " [id=" + getId() + ", cognomeMinore="
				+ getCognomeMinore() + ", cognomeMinore=" + getCognomeMinore() + ", nomeMinore=" + getNomeMinore()
				+ ", cfRichiedente=" + getCfRichiedente() + ", nomeIstituto=" + getNomeIstituto() + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeRichiedente() {
		return nomeRichiedente;
	}

	public void setNomeRichiedente(String nomeRichiedente) {
		this.nomeRichiedente = nomeRichiedente;
	}

	public String getCognomeRichiedente() {
		return cognomeRichiedente;
	}

	public void setCognomeRichiedente(String cognomeRichiedente) {
		this.cognomeRichiedente = cognomeRichiedente;
	}

	public String getTipoRichiedente() {
		return tipoRichiedente;
	}

	public void setTipoRichiedente(String tipoRichiedente) {
		this.tipoRichiedente = tipoRichiedente;
	}

	public String getNomeMinore() {
		return nomeMinore;
	}

	public void setNomeMinore(String nomeMinore) {
		this.nomeMinore = nomeMinore;
	}

	public String getCognomeMinore() {
		return cognomeMinore;
	}

	public void setCognomeMinore(String cognomeMinore) {
		this.cognomeMinore = cognomeMinore;
	}

	public String getDataNascitaMinore() {
		return dataNascitaMinore;
	}

	public void setDataNascitaMinore(String dataNascitaMinore) {
		this.dataNascitaMinore = dataNascitaMinore;
	}

	public String getEmailUtente() {
		return emailUtente;
	}

	public void setEmailUtente(String emailUtente) {
		this.emailUtente = emailUtente;
	}

	public String getCfRichiedente() {
		return cfRichiedente;
	}

	public void setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
	}

	public String getEsitoValutazione() {
		return esitoValutazione;
	}

	public void setEsitoValutazione(String esitoValutazione) {
		this.esitoValutazione = esitoValutazione;
	}

	public Long getIdFamiglia() {
		return idFamiglia;
	}

	public void setIdFamiglia(Long idFamiglia) {
		this.idFamiglia = idFamiglia;
	}

	public String getCfMinore() {
		return cfMinore;
	}

	public void setCfMinore(String cfMinore) {
		this.cfMinore = cfMinore;
	}

	public String getConfermataIscrizione() {
		return confermataIscrizione;
	}

	public void setConfermataIscrizione(String confermataIscrizione) {
		this.confermataIscrizione = confermataIscrizione;
	}

	public String getDataInizioIscrizione() {
		return dataInizioIscrizione;
	}

	public void setDataInizioIscrizione(String dataInizioIscrizione) {
		this.dataInizioIscrizione = dataInizioIscrizione;
	}

	public String getDataFineIscrizione() {
		return dataFineIscrizione;
	}

	public void setDataFineIscrizione(String dataFineIscrizione) {
		this.dataFineIscrizione = dataFineIscrizione;
	}

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

	public String getConfermaNotificata() {
		return confermaNotificata;
	}

	public void setConfermaNotificata(String confermaNotificata) {
		this.confermaNotificata = confermaNotificata;
	}

	public String getRicalcoloRate() {
		return ricalcoloRate;
	}

	public void setRicalcoloRate(String ricalcoloRate) {
		this.ricalcoloRate = ricalcoloRate;
	}

}
