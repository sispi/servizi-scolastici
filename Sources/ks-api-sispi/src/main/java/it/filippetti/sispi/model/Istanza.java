package it.filippetti.sispi.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.filippetti.sispi.pagamentoretta.DateUtils;

@Entity
@Table(name = "PORTSCU_ISTANZE")
@DynamicUpdate
public class Istanza implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "portscu_istanze_seq")
	@SequenceGenerator(name = "portscu_istanze_seq", sequenceName = "PORTSCU_ISTANZE_SEQ", allocationSize = 1)
	@Column(name = "ID_COD")
	private Long id;

	@Column(name = "DPS_CFRICHIEDENTE")
	private String cfContribuente;

	@Column(name = "CF_MINORE_ANAGRAFE")
	private String cfMinore;

	@Column(name = "CONFERMA_NOTIFICATA")
	private String confermaNotificata;

	@Column(name = "NOME_MINORE")
	private String nomeMinore;

	@Column(name = "COGNOME_MINORE")
	private String cognomeMinore;

	@Column(name = "IMPORTO_RETTA")
	private Float importoRetta;

	@Column(name = "ID_FAMIGLIA")
	private Long idFamiglia;

	@Column(name = "RICALCOLO_RATE")
	private String ricalcoloRate;

	@Column(name = "NOME_ISTITUTO")
	private String nomeIstituto;

	@Column(name = "FASCIA_ORARIA")
	private String fasciaOraria;

	@Column(name = "FASCIA")
	private String fascia;

//	@Column(name = "ISEE_RIFERIMENTO")
//	private Float iseeRiferimento;

	@Column(name = "DATA_INIZIO_ISCRIZIONE")
	private String dataInizioIscrizione;

	@Column(name = "DATA_FINE_ISCRIZIONE")
	private String dataFineIscrizione;

	@Column(name = "TIPO_RICHIEDENTE")
	private String tipoRichiedente;

	@Column(name = "TIPO_PROCEDIMENTO")
	private String tipoProcedimento;

	@Column(name = "SCONTO_FAMIGLIA")
	private String scontoFamiglia;

	@Column(name = "ESITO_VALUTAZIONE")
	private String esitoValutazione;

	@Column(name = "CONFERMATA_ISCRIZIONE")
	private String confermataIscrizione;

	@Column(name = "ANNO_SCOLASTICO")
	private String annoScolastico;

	@Column(name = "NOME_PROCEDIMENTO")
	private String nomeProcedimento;

	@Column(name = "DPS_FLGVALUTAZIONE")
	private String dpsFlgvalutazione;

	@Column(name = "NOME_RICHIEDENTE")
	private String nomeRichiedente;

	@Column(name = "COGNOME_RICHIEDENTE")
	private String cognomeRichiedente;

	@Column(name = "DATA_NASCITA_MINORE")
	private String dataNascitaMinore;

	@Column(name = "EMAIL_UTENTE")
	private String emailUtente;

	public Istanza() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCfContribuente() {
		return cfContribuente;
	}

	public void setCfContribuente(String cfContribuente) {
		this.cfContribuente = cfContribuente;
	}

	public String getCfMinore() {
		return cfMinore;
	}

	public void setCfMinore(String cfMinore) {
		this.cfMinore = cfMinore;
	}

	public String getConfermaNotificata() {
		return confermaNotificata;
	}

	public void setConfermaNotificata(String confermaNotificata) {
		this.confermaNotificata = confermaNotificata;
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

	public Float getImportoRetta() {
		return importoRetta;
	}

	public void setImportoRetta(Float importoRetta) {
		this.importoRetta = importoRetta;
	}

	public Long getIdFamiglia() {
		return idFamiglia;
	}

	public void setIdFamiglia(Long idFamiglia) {
		this.idFamiglia = idFamiglia;
	}

	public String getRicalcoloRate() {
		return ricalcoloRate;
	}

	public void setRicalcoloRate(String ricalcoloRate) {
		this.ricalcoloRate = ricalcoloRate;
	}

	public String getNomeIstituto() {
		return nomeIstituto;
	}

	public void setNomeIstituto(String nomeIstituto) {
		this.nomeIstituto = nomeIstituto;
	}

	public String getFasciaOraria() {
		return fasciaOraria;
	}

	public void setFasciaOraria(String fasciaOraria) {
		this.fasciaOraria = fasciaOraria;
	}

	public String getFascia() {
		return fascia;
	}

	public void setFascia(String fascia) {
		this.fascia = fascia;
	}
//
//	public Float getIseeRiferimento() {
//		return iseeRiferimento;
//	}
//
//	public void setIseeRiferimento(Float iseeRiferimento) {
//		this.iseeRiferimento = iseeRiferimento;
//	}

	public String getDataInizioIscrizione() {
		return dataInizioIscrizione;
	}

	@Transient
	@JsonIgnore
	public LocalDate getDataInizioIscrizioneDate() {
		return DateUtils.stringToDate(getDataInizioIscrizione());
	}

	public void setDataInizioIscrizione(String dataInizioIscrizione) {
		this.dataInizioIscrizione = dataInizioIscrizione;
	}

	public String getDataFineIscrizione() {
		return dataFineIscrizione;
	}

	@Transient
	@JsonIgnore
	public LocalDate getDataFineIscrizioneDate() {
		return DateUtils.stringToDate(getDataFineIscrizione());
	}

	public void setDataFineIscrizione(String dataFineIscrizione) {
		this.dataFineIscrizione = dataFineIscrizione;
	}

	public String getTipoRichiedente() {
		return tipoRichiedente;
	}

	public void setTipoRichiedente(String tipoRichiedente) {
		this.tipoRichiedente = tipoRichiedente;
	}

	public String getTipoProcedimento() {
		return tipoProcedimento;
	}

	public void setTipoProcedimento(String tipoProcedimento) {
		this.tipoProcedimento = tipoProcedimento;
	}

	@Transient
	@JsonIgnore
	public Boolean isAsiloNido() {
		final List<String> listaCodiciAsilo = Arrays.asList("4", "7", "8");
		return listaCodiciAsilo.contains(getTipoProcedimento());
	}

	public String getScontoFamiglia() {
		return scontoFamiglia;
	}

	public void setScontoFamiglia(String scontoFamiglia) {
		this.scontoFamiglia = scontoFamiglia;
	}

	public String getEsitoValutazione() {
		return esitoValutazione;
	}

	public void setEsitoValutazione(String esitoValutazione) {
		this.esitoValutazione = esitoValutazione;
	}

	public String getConfermataIscrizione() {
		return confermataIscrizione;
	}

	public void setConfermataIscrizione(String confermataIscrizione) {
		this.confermataIscrizione = confermataIscrizione;
	}

	public String getAnnoScolastico() {
		return annoScolastico;
	}

	public void setAnnoScolastico(String annoScolastico) {
		this.annoScolastico = annoScolastico;
	}

	public String getNomeProcedimento() {
		return nomeProcedimento;
	}

	public void setNomeProcedimento(String nomeProcedimento) {
		this.nomeProcedimento = nomeProcedimento;
	}

	public String getDpsFlgvalutazione() {
		return dpsFlgvalutazione;
	}

	public void setDpsFlgvalutazione(String dpsFlgvalutazione) {
		this.dpsFlgvalutazione = dpsFlgvalutazione;
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

}
