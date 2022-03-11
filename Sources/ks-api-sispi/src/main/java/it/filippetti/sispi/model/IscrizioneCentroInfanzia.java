package it.filippetti.sispi.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.filippetti.sispi.pagamentoretta.DateUtils;

@Entity
@Table(name = "ISCRIZIONI_CENTRO_INFANZIA")
@DynamicUpdate
public class IscrizioneCentroInfanzia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "centro_infanzia_seq")
	@SequenceGenerator(name = "centro_infanzia_seq", sequenceName = "ISCRIZIONI_CENTRO_INFANZIA_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "NOME_RICHIEDENTE")
	private String nomeRichiedente;

	@Column(name = "COGNOME_RICHIEDENTE")
	private String cognomeRichiedente;

	@Column(name = "TIPO_RICHIEDENTE")
	private String tipoRichiedente;

	@Column(name = "NOME_MINORE")
	private String nomeMinore;

	@Column(name = "COGNOME_MINORE")
	private String cognomeMinore;

	@Column(name = "DATA_NASCITA_MINORE")
	private String dataNascitaMinore;

	@Column(name = "EMAIL_UTENTE")
	private String emailUtente;

	@Column(name = "CF_RICHIEDENTE")
	private String cfRichiedente;

	@Column(name = "ESITO_VALUTAZIONE")
	private String esitoValutazione;

	@Column(name = "ID_FAMIGLIA")
	private Long idFamiglia;

	@Column(name = "CF_MINORE")
	private String cfMinore;

	@Column(name = "CONFERMATA_ISCRIZIONE")
	private String confermataIscrizione;

	@Column(name = "CONFERMA_NOTIFICATA")
	private String confermaNotificata;

	@Column(name = "DATA_INIZIO_ISCRIZIONE")
	private String dataInizioIscrizione;

	@Column(name = "DATA_FINE_ISCRIZIONE")
	private String dataFineIscrizione;

	@Column(name = "RICALCOLO_RATE")
	private String ricalcoloRate;

	@Column(name = "ANNO_SCOLASTICO")
	private String annoScolastico;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "iscrizione", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<DettaglioIscrizioneCentroInfanzia> listaDettaglioIscrizione = new ArrayList<>();

	public IscrizioneCentroInfanzia() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDataInizioIscrizione() {
		return dataInizioIscrizione;
	}

	@Transient
	@JsonIgnore
	public LocalDate getDataInizioIscrizioneDate() {
		return DateUtils.stringToDate(getDataInizioIscrizione(), "yyyy-MM-dd");
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
		return DateUtils.stringToDate(getDataFineIscrizione(), "yyyy-MM-dd");
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

//	@Transient
//	@JsonIgnore
//	public Boolean isAsiloNido() {
//		final List<String> listaCodiciAsilo = Arrays.asList("4", "7", "8");
//		return listaCodiciAsilo.contains(getTipoProcedimento());
//	}

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

	public String getCfRichiedente() {
		return cfRichiedente;
	}

	public void setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
	}

	public List<DettaglioIscrizioneCentroInfanzia> getListaDettaglioIscrizione() {
		return listaDettaglioIscrizione;
	}

	public void setListaDettaglioIscrizione(List<DettaglioIscrizioneCentroInfanzia> listaDettaglioIscrizione) {
		this.listaDettaglioIscrizione = listaDettaglioIscrizione;
	}

}
