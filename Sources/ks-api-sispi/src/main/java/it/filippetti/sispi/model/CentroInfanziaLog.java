package it.filippetti.sispi.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.filippetti.sispi.spring.HashMapConverter;

@Entity
@Table(name = "CENTRO_INFANZIA_LOG")
public class CentroInfanziaLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "centro_infanzia_log_seq")
	@SequenceGenerator(name = "centro_infanzia_log_seq", sequenceName = "CENTRO_INFANZIA_LOG_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "UTENTE_MODIFICA")
	private String utenteModifica;

	@Column(name = "DATA_MODIFICA")
	private LocalDate dataModifica;

	@Column(name = "CAUSALE")
	private String causale;

	@JoinColumn(name = "ID_DETTAGLIO_ISCRIZIONE_CENTRO_INFANZIA")
	@ManyToOne(cascade = CascadeType.ALL)
	private DettaglioIscrizioneCentroInfanzia dettaglioIscrizione;

	@Column(name = "TIPO_MODIFICA")
	private String tipoModifica;

	@Column(name = "DATI_PRE")
	@Convert(converter = HashMapConverter.class)
	private Map<String, Object> datiPre;

	@Column(name = "DATI_POST")
	@Convert(converter = HashMapConverter.class)
	private Map<String, Object> datiPost;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUtenteModifica() {
		return utenteModifica;
	}

	public void setUtenteModifica(String utenteModifica) {
		this.utenteModifica = utenteModifica;
	}

	public LocalDate getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(LocalDate dataModifica) {
		this.dataModifica = dataModifica;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public DettaglioIscrizioneCentroInfanzia getDettaglioIscrizione() {
		return dettaglioIscrizione;
	}

	public void setDettaglioIscrizione(DettaglioIscrizioneCentroInfanzia dettaglioIscrizione) {
		this.dettaglioIscrizione = dettaglioIscrizione;
	}

	public String getTipoModifica() {
		return tipoModifica;
	}

	public void setTipoModifica(String tipoModifica) {
		this.tipoModifica = tipoModifica;
	}

	public Map<String, Object> getDatiPre() {
		return datiPre;
	}

	public void setDatiPre(Map<String, Object> datiPre) {
		this.datiPre = datiPre;
	}

	public Map<String, Object> getDatiPost() {
		return datiPost;
	}

	public void setDatiPost(Map<String, Object> datiPost) {
		this.datiPost = datiPost;
	}

}
