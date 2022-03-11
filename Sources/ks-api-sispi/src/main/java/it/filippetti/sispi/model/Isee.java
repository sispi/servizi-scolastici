package it.filippetti.sispi.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "ISEE")
@DynamicUpdate
public class Isee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "isee_seq")
	@SequenceGenerator(name = "isee_seq", sequenceName = "ISEE_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "DATA_PRESENTAZIONE")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Data presentazione obbligatoria")
	private LocalDate dataPresentazione;

	@Column(name = "ANNO")
	@NotNull(message = "Anno obbligatorio")
	private Integer anno;

	@Column(name = "CODICE_FISCALE")
	@NotNull(message = "Codice fiscale obbligatorio")
	private String codiceFiscale;

	@Column(name = "IMPORTO")
	@NotNull(message = "Importo obbligatorio")
	private Float importo;

	@Column(name = "DATA_INIZIO_VALIDITA")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Data inizio validita obbligatoria")
	private LocalDate dataInizioValidita;

	@Column(name = "DATA_FINE_VALIDITA")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Data fine validita obbligatoria")
	private LocalDate dataFineValidita;

	@Column(name = "UTENTE_INSERITORE")
	@NotBlank(message = "Utente inseritore obbligatorio")
	private String utenteInseritore;

	@Column(name = "UTENTE_RICHIEDENTE")
	@NotBlank(message = "Utente richiedente obbligatorio")
	private String utenteRichiedente;

	@Column(name = "CAUSALE")
	@NotBlank(message = "Causale obbligatoria")
	private String causale;

	@Column(name = "DATA_PROTOCOLLO")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataProtocollo;

	@Column(name = "NUMERO_PROTOCOLLO")
	private String numeroProtocollo;

	@Column(name = "DOCNUM_PRINCIPALE")
	private String docnumPrincipale;

	@Column(name = "ID_ISTANZA")
	private String idIstanza;

	@Column(name = "STATO")
	private String stato;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDataPresentazione() {
		return dataPresentazione;
	}

	public void setDataPresentazione(LocalDate dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public Float getImporto() {
		return importo;
	}

	public void setImporto(Float importo) {
		this.importo = importo;
	}

	public LocalDate getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(LocalDate dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public LocalDate getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(LocalDate dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public String getUtenteInseritore() {
		return utenteInseritore;
	}

	public void setUtenteInseritore(String utenteInseritore) {
		this.utenteInseritore = utenteInseritore;
	}

	public String getUtenteRichiedente() {
		return utenteRichiedente;
	}

	public void setUtenteRichiedente(String utenteRichiedente) {
		this.utenteRichiedente = utenteRichiedente;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public LocalDate getDataProtocollo() {
		return dataProtocollo;
	}

	public void setDataProtocollo(LocalDate dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public String getDocnumPrincipale() {
		return docnumPrincipale;
	}

	public void setDocnumPrincipale(String docnumPrincipale) {
		this.docnumPrincipale = docnumPrincipale;
	}

	public String getIdIstanza() {
		return idIstanza;
	}

	public void setIdIstanza(String idIstanza) {
		this.idIstanza = idIstanza;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
}
