package it.kdm.docer.parer.daemon.database;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    private Integer version;
    
    /** Lo stato di un Job
     * I valori possibili sono i seguenti:
     * - A = In attesa di essere mandato in conservazione
     * - W = Mandato in conservazione
     * - X = Conservato
     * - E = Errore
     * - F = Errore Doc/ER
     * - C = Job concluso
     */
    @NotNull
    private char stato;

	private String chiaveDoc;

	@NotNull
	private String codEnte;

	private String codAoo;
    
    /** L'azione da intraprendere
     * I valori possibili sono i seguenti:
     * - C = Conservazione
     * - M = Modifica dei metadati
     * - A = Aggiunta di un documento
     */
    @NotNull
    private char azione;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar dataInserimento;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar dataUltimaModifica;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar dataChiamata;

    @Size(max = 10)
    private String webService;

    @NotNull
    private String docId;

    @Lob
    private String xmlDocumento;

    @Lob
    private String xmlAllegati;

    @Lob
    private String files;

    @Size(max = 100)
    private String tipoDocumento;

    @Size(max = 50)
    private String appChiamante;

    @Size(max = 30)
    private String tipoConservazione;

    private Boolean forzaCollegamento;

    private Boolean forzaAccettazione;
    
    private Boolean forzaConservazione;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dataRegistrazione;

	@Size(max = 15)
    private String errorCode;

    @Lob
    private String errorMessage;

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

	public char getStato() {
		return stato;
	}

	public void setStato(char stato) {
		this.stato = stato;
	}
	
	public char getAzione() {
		return azione;
	}

	public void setAzione(char azione) {
		this.azione = azione;
	}

	public Calendar getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Calendar dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Calendar getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	public void setDataUltimaModifica(Calendar dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

	public Calendar getDataChiamata() {
		return dataChiamata;
	}

	public void setDataChiamata(Calendar dataChiamata) {
		this.dataChiamata = dataChiamata;
	}

	public String getWebService() {
		return webService;
	}

	public void setWebService(String webService) {
		this.webService = webService;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getXmlDocumento() {
		return xmlDocumento;
	}

	public void setXmlDocumento(String xmlDocumento) {
		this.xmlDocumento = xmlDocumento;
	}

	public String getXmlAllegati() {
		return xmlAllegati;
	}

	public void setXmlAllegati(String xmlAllegati) {
		this.xmlAllegati = xmlAllegati;
	}

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getAppChiamante() {
		return appChiamante;
	}

	public void setAppChiamante(String appChiamante) {
		this.appChiamante = appChiamante;
	}

	public String getTipoConservazione() {
		return tipoConservazione;
	}

	public void setTipoConservazione(String tipoConservazione) {
		this.tipoConservazione = tipoConservazione;
	}

	public Boolean getForzaCollegamento() {
		return forzaCollegamento;
	}

	public void setForzaCollegamento(Boolean forzaCollegamento) {
		this.forzaCollegamento = forzaCollegamento;
	}

	public Boolean getForzaAccettazione() {
		return forzaAccettazione;
	}

	public void setForzaAccettazione(Boolean forzaAccettazione) {
		this.forzaAccettazione = forzaAccettazione;
	}

	public Boolean getForzaConservazione() {
		return forzaConservazione;
	}

	public void setForzaConservazione(Boolean forzaConservazione) {
		this.forzaConservazione = forzaConservazione;
	}

	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public String getChiaveDoc() {
		return chiaveDoc;
	}

	public void setChiaveDoc(String chiaveDoc) {
		this.chiaveDoc = chiaveDoc;
	}

	public String getCodEnte() {
		return codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}

	public String getCodAoo() {
		return codAoo;
	}

	public void setCodAoo(String codAoo) {
		this.codAoo = codAoo;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	@Transient
	public String toString() {
		return String.format("Job(id: %d, docId: %s, azione: %s)",
				id, docId, azione);
	}

}
