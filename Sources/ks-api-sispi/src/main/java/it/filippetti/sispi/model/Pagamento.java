package it.filippetti.sispi.model;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.filippetti.sispi.spring.HashMapConverter;

@Entity
@Table(name = "PAGAMENTO")
public class Pagamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "CF_MINORE")
	private String cfMinore;

	@Column(name = "IMPORTO")
	private Float importo;

	@Column(name = "CF_CONTRIBUENTE")
	private String cfContribuente;

	@Column(name = "CAUSALE")
	private String causale;

	@Column(name = "DOCNUM_PRINCIPALE")
	private String docnumPrincipale;

	@Column(name = "MODALITA_ID")
	private Modalita modalita;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RATA_ID", insertable = false, updatable = false)
	private Rata rata;

	@Column(name = "JSON_DOC")
	@Convert(converter = HashMapConverter.class)
	private Map<String, Object> jsonDoc;

	public Pagamento() {
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

	public Float getImporto() {
		return importo;
	}

	public void setImporto(Float importo) {
		this.importo = importo;
	}

	public String getCfContribuente() {
		return cfContribuente;
	}

	public void setCfContribuente(String cfContribuente) {
		this.cfContribuente = cfContribuente;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getDocnumPrincipale() {
		return docnumPrincipale;
	}

	public void setDocnumPrincipale(String docnumPrincipale) {
		this.docnumPrincipale = docnumPrincipale;
	}

	public Modalita getModalita() {
		return modalita;
	}

	public void setModalita(Modalita modalita) {
		this.modalita = modalita;
	}

	public Rata getRata() {
		return rata;
	}

	public void setRata(Rata rata) {
		this.rata = rata;
	}

	public Map<String, Object> getJsonDoc() {
		return jsonDoc;
	}

	public void setJsonDoc(Map<String, Object> jsonDoc) {
		this.jsonDoc = jsonDoc;
	}

}
