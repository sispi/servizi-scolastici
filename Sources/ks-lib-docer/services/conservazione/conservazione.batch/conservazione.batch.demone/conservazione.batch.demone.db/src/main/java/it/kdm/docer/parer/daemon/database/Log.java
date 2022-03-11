package it.kdm.docer.parer.daemon.database;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Log {
		
	public Log() {}
	
	public Log(Job job) {
		setJob(job);
		forzaAccettazione = job.getForzaAccettazione();
		forzaCollegamento = job.getForzaCollegamento();
		forzaConservazione = job.getForzaConservazione();
		esito = job.getStato();
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;
	
	@ManyToOne
	@JoinColumn(name = "job_id")
	private Job job;
	
    @NotNull
    private char esito;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar dataChiamata;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar dataRisposta;

    @Size(max = 15)
    private String errorCode;
    @Lob
    private String message;

    private Boolean forzaAccettazione;

    private Boolean forzaCollegamento;

    private Boolean forzaConservazione;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar dataRegistrazione;

    @Lob
    private String xmlRichiesta;

    @Lob
    private String xmlEsito;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public char getEsito() {
		return esito;
	}

	public void setEsito(char esito) {
		this.esito = esito;
		job.setStato(esito);
	}

	public Calendar getDataChiamata() {
		return dataChiamata;
	}

	public void setDataChiamata(Calendar dataChiamata) {
		this.dataChiamata = dataChiamata;
		job.setDataChiamata(dataChiamata);
	}

	public Calendar getDataRisposta() {
		return dataRisposta;
	}

	public void setDataRisposta(Calendar dataRisposta) {
		this.dataRisposta = dataRisposta;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getForzaAccettazione() {
		return forzaAccettazione;
	}

	public void setForzaAccettazione(Boolean forzaAccettazione) {
		this.forzaAccettazione = forzaAccettazione;
	}

	public Boolean getForzaCollegamento() {
		return forzaCollegamento;
	}

	public void setForzaCollegamento(Boolean forzaCollegamento) {
		this.forzaCollegamento = forzaCollegamento;
	}

	public Boolean getForzaConservazione() {
		return forzaConservazione;
	}

	public void setForzaConservazione(Boolean forzaConservazione) {
		this.forzaConservazione = forzaConservazione;
	}

	public Calendar getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(Calendar dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public String getXmlRichiesta() {
		return xmlRichiesta;
	}

	public void setXmlRichiesta(String xmlRichiesta) {
		this.xmlRichiesta = xmlRichiesta;
	}

	public String getXmlEsito() {
		return xmlEsito;
	}

	public void setXmlEsito(String xmlEsito) {
		this.xmlEsito = xmlEsito;
	}
	
	@Override
	@Transient
	public String toString() {
		return String.format("Log(id: %d, jobId: %d)",
				id, job.getId());
	}
}
