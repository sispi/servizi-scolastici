package it.filippetti.sispi.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ISCRIZIONI_LOG")
public class IscrizioniLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "iscrizioni_log_seq")
	@SequenceGenerator(name = "iscrizioni_log_seq", sequenceName = "ISCRIZIONI_LOG_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "ID_ISTANZA_ISCRIZIONE")
	private String idIstanzaIscrizione;

	@Column(name = "UTENTE_MODIFICA")
	private String utenteModifica;

	@Column(name = "DATA_MODIFICA")
	private String dataModifica;

	@Column(name = "CAUSALE")
	private String causale;

	@Column(name = "MOTIVAZIONE")
	private String motivazione;

	@Column(name = "DATI_RICHIESTA_PRE")
	private String datiRichiestaPre;

	@Column(name = "DATI_RICHIESTA_POST")
	private String datiRichiestaPost;

	@Column(name = "ESITO_PRE")
	private String esitoPre;

	@Column(name = "ESITO_POST")
	private String esitoPost;

	@Column(name = "TIPO_DOMANDA")
	private String tipoDomanda;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdIstanzaIscrizione() {
		return idIstanzaIscrizione;
	}

	public void setIdIstanzaIscrizione(String idIstanzaIscrizione) {
		this.idIstanzaIscrizione = idIstanzaIscrizione;
	}

	public String getUtenteModifica() {
		return utenteModifica;
	}

	public void setUtenteModifica(String utenteModifica) {
		this.utenteModifica = utenteModifica;
	}

	public String getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(String dataModifica) {
		this.dataModifica = dataModifica;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getMotivazione() {
		return motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}

	public String getDatiRichiestaPre() {
		return datiRichiestaPre;
	}

	public void setDatiRichiestaPre(String datiRichiestaPre) {
		this.datiRichiestaPre = datiRichiestaPre;
	}

	public String getDatiRichiestaPost() {
		return datiRichiestaPost;
	}

	public void setDatiRichiestaPost(String datiRichiestaPost) {
		this.datiRichiestaPost = datiRichiestaPost;
	}

	public String getEsitoPre() {
		return esitoPre;
	}

	public void setEsitoPre(String esitoPre) {
		this.esitoPre = esitoPre;
	}

	public String getEsitoPost() {
		return esitoPost;
	}

	public void setEsitoPost(String esitoPost) {
		this.esitoPost = esitoPost;
	}

	public String getTipoDomanda() {
		return tipoDomanda;
	}

	public void setTipoDomanda(String tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
	}
}
