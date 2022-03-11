package it.filippetti.sispi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DOMANDA_FORNITURA_LIBRI")
public class DomandaFornituraLibri implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_ISTANZA")
	private String idIstanza;

	@Column(name = "CF_RICHIEDENTE")
	private String cfRichiedente;

	@Column(name = "CF_MINORE")
	private String cfMinore;

	@Column(name = "DATI_RICHIESTA")
	private String datiRichiesta;

	@Column(name = "DATA_PRESENTAZIONE")
	private String dataPresentazione;

	@Column(name = "NUMERO_PROTOCOLLO")
	private String numeroProtocollo;

	@Column(name = "ANNO_PROTOCOLLO")
	private String annoProtocollo;

	@Column(name = "DOCNUM_PRINCIPALE")
	private String docnumPrincipale;

	@Column(name = "ANNULLATO")
	private String annullato;

	@Column(name = "ID_ISTANZA_ANNULLAMENTO")
	private String idIstanzaAnnullamento;

	@Column(name = "DATI_RICHIESTA_ANNULLAMENTO")
	private String datiRichiestaAnnullamento;

	@Column(name = "DATA_PRESENTAZIONE_ANNULLAMENTO")
	private String dataPresentazioneAnnullamento;

	@Column(name = "NUMERO_PROTOCOLLO_ANNULLAMENTO")
	private String numeroProtocolloAnnullamento;

	@Column(name = "ANNO_PROTOCOLLO_ANNULLAMENTO")
	private String annoProtocolloAnnullamento;

	@Column(name = "DOCNUM_PRINCIPALE_ANNULLAMENTO")
	private String docnumPrincipaleAnnullamento;

	@Column(name = "ANNO_SCOLASTICO")
	private String annoScolastico;

	public String getIdIstanza() {
		return idIstanza;
	}

	public void setIdIstanza(String idIstanza) {
		this.idIstanza = idIstanza;
	}

	public String getCfRichiedente() {
		return cfRichiedente;
	}

	public void setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
	}

	public String getCfMinore() {
		return cfMinore;
	}

	public void setCfMinore(String cfMinore) {
		this.cfMinore = cfMinore;
	}

	public String getDatiRichiesta() {
		return datiRichiesta;
	}

	public void setDatiRichiesta(String datiRichiesta) {
		this.datiRichiesta = datiRichiesta;
	}

	public String getDataPresentazione() {
		return dataPresentazione;
	}

	public void setDataPresentazione(String dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public String getAnnoProtocollo() {
		return annoProtocollo;
	}

	public void setAnnoProtocollo(String annoProtocollo) {
		this.annoProtocollo = annoProtocollo;
	}

	public String getDocnumPrincipale() {
		return docnumPrincipale;
	}

	public void setDocnumPrincipale(String docnumPrincipale) {
		this.docnumPrincipale = docnumPrincipale;
	}

	public String getAnnullato() {
		return annullato;
	}

	public void setAnnullato(String annullato) {
		this.annullato = annullato;
	}

	public String getIdIstanzaAnnullamento() {
		return idIstanzaAnnullamento;
	}

	public void setIdIstanzaAnnullamento(String idIstanzaAnnullamento) {
		this.idIstanzaAnnullamento = idIstanzaAnnullamento;
	}

	public String getDatiRichiestaAnnullamento() {
		return datiRichiestaAnnullamento;
	}

	public void setDatiRichiestaAnnullamento(String datiRichiestaAnnullamento) {
		this.datiRichiestaAnnullamento = datiRichiestaAnnullamento;
	}

	public String getDataPresentazioneAnnullamento() {
		return dataPresentazioneAnnullamento;
	}

	public void setDataPresentazioneAnnullamento(String dataPresentazioneAnnullamento) {
		this.dataPresentazioneAnnullamento = dataPresentazioneAnnullamento;
	}

	public String getNumeroProtocolloAnnullamento() {
		return numeroProtocolloAnnullamento;
	}

	public void setNumeroProtocolloAnnullamento(String numeroProtocolloAnnullamento) {
		this.numeroProtocolloAnnullamento = numeroProtocolloAnnullamento;
	}

	public String getAnnoProtocolloAnnullamento() {
		return annoProtocolloAnnullamento;
	}

	public void setAnnoProtocolloAnnullamento(String annoProtocolloAnnullamento) {
		this.annoProtocolloAnnullamento = annoProtocolloAnnullamento;
	}

	public String getDocnumPrincipaleAnnullamento() {
		return docnumPrincipaleAnnullamento;
	}

	public void setDocnumPrincipaleAnnullamento(String docnumPrincipaleAnnullamento) {
		this.docnumPrincipaleAnnullamento = docnumPrincipaleAnnullamento;
	}

	public String getAnnoScolastico() {
		return annoScolastico;
	}

	public void setAnnoScolastico(String annoScolastico) {
		this.annoScolastico = annoScolastico;
	}

}
