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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.filippetti.sispi.spring.HashMapConverter;

@Entity
@Table(name = "RATA_LOG")
public class RataLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "rata_log_seq")
	@SequenceGenerator(name = "rata_log_seq", sequenceName = "RATA_LOG_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "UTENTE_MODIFICA")
	private String utenteModifica;

	@Column(name = "DATA_MODIFICA")
	private LocalDate dataModifica;

	@Column(name = "CAUSALE")
	private String causale;

	@JoinColumn(name = "ID_RATA")
	@OneToOne(cascade = CascadeType.ALL)
	private Rata rata;

	@Column(name = "TIPO_MODIFICA")
	private String tipoModifica;

	@Column(name = "DATI_RECORD")
	@Convert(converter = HashMapConverter.class)
	private Map<String, Object> datiRecord;

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

	public Rata getRata() {
		return rata;
	}

	public void setRata(Rata rata) {
		this.rata = rata;
	}

	public String getTipoModifica() {
		return tipoModifica;
	}

	public void setTipoModifica(String tipoModifica) {
		this.tipoModifica = tipoModifica;
	}

	public Map<String, Object> getDatiRecord() {
		return datiRecord;
	}

	public void setDatiRecord(Map<String, Object> datiRecord) {
		this.datiRecord = datiRecord;
	}

}
