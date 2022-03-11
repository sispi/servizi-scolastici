package it.filippetti.sispi.model;

import java.io.Serializable;
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
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.filippetti.sispi.spring.HashMapConverter;

@Entity
@Table(name = "DETTAGLIO_ISCRIZIONI_CENTRO_INFANZIA")
@DynamicUpdate
public class DettaglioIscrizioneCentroInfanzia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "dettaglio_centro_infanzia_seq")
	@SequenceGenerator(name = "dettaglio_centro_infanzia_seq", sequenceName = "DETTAGLIO_ISCRIZIONI_CENTRO_INFANZIA_SEQ", allocationSize = 1)
	private Long id;

	@JoinColumn(name = "ID_ISCRIZIONE_CENTRO_INFANZIA")
	@ManyToOne(cascade = CascadeType.ALL)
	private IscrizioneCentroInfanzia iscrizione;

	@Column(name = "DESCRIZIONE")
	private String descrizione;

	@Column(name = "SORT")
	private Integer sort;

	@Column(name = "VALORE_RATA")
	private Float valoreRata;

	@Column(name = "IMPORTO_PAGAMENTO")
	private Float importoPagamento;

	@Column(name = "CAUSALE_PAGAMENTO")
	private String causalePagamento;

	@Column(name = "DOCNUM_PAGAMENTO")
	private String docnumPagamento;

	@Column(name = "ISTANZA_PAGAMENTO")
	private String istanzaPagamento;

	@Column(name = "MODALITA_ID")
	private Modalita modalita;

	@Column(name = "JSON_DOC_PAGAMENTO")
	@Convert(converter = HashMapConverter.class)
	private Map<String, Object> jsonDoc;

	@Column(name = "NOME_ISTITUTO")
	private String nomeIstituto;

	@Column(name = "FASCIA")
	private String fascia;

	@Column(name = "FASCIA_ORARIA")
	private String fasciaOraria;

	@Column(name = "VALORE_CONGUAGLIO")
	private Float valoreConguaglio;

	@Column(name = "MOTIVAZIONE_VARIAZIONE")
	private String motivazioneVariazione;

	public DettaglioIscrizioneCentroInfanzia() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IscrizioneCentroInfanzia getIscrizione() {
		return iscrizione;
	}

	public void setIscrizione(IscrizioneCentroInfanzia iscrizione) {
		this.iscrizione = iscrizione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Float getValoreRata() {
		return valoreRata;
	}

	public void setValoreRata(Float valoreRata) {
		this.valoreRata = valoreRata;
	}

	public Float getImportoPagamento() {
		return importoPagamento;
	}

	public void setImportoPagamento(Float importoPagamento) {
		this.importoPagamento = importoPagamento;
	}

	public String getCausalePagamento() {
		return causalePagamento;
	}

	public void setCausalePagamento(String causalePagamento) {
		this.causalePagamento = causalePagamento;
	}

	public String getDocnumPagamento() {
		return docnumPagamento;
	}

	public void setDocnumPagamento(String docnumPagamento) {
		this.docnumPagamento = docnumPagamento;
	}

	public Modalita getModalita() {
		return modalita;
	}

	public void setModalita(Modalita modalita) {
		this.modalita = modalita;
	}

	public Map<String, Object> getJsonDoc() {
		return jsonDoc;
	}

	public void setJsonDoc(Map<String, Object> jsonDoc) {
		this.jsonDoc = jsonDoc;
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

	public Float getValoreConguaglio() {
		return valoreConguaglio;
	}

	public void setValoreConguaglio(Float valoreConguaglio) {
		this.valoreConguaglio = valoreConguaglio;
	}

	public String getMotivazioneVariazione() {
		return motivazioneVariazione;
	}

	public void setMotivazioneVariazione(String motivazioneVariazione) {
		this.motivazioneVariazione = motivazioneVariazione;
	}

	public String getIstanzaPagamento() {
		return istanzaPagamento;
	}

	public void setIstanzaPagamento(String istanzaPagamento) {
		this.istanzaPagamento = istanzaPagamento;
	}

	@Transient
	@JsonIgnore
	public Float getImportoTotale() {
		Float result = 0f;
		if (getValoreRata() != null) {
			result = getValoreRata() + getValoreConguaglio();
		}
		return result;
	}

	@Transient
	@JsonIgnore
	public Boolean isPagato() {
		return getDocnumPagamento() != null;
	}

}
