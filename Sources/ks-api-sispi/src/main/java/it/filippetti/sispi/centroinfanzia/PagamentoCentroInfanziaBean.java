package it.filippetti.sispi.centroinfanzia;

import java.io.Serializable;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.filippetti.sispi.model.Modalita;

public class PagamentoCentroInfanziaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String jsonDoc;

	private Float importoPagamento;

	private String causalePagamento;

	private String docnumPagamento;

	private Integer modalitaId;

	private String istanzaPagamento;

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + hashCode() + " [id=" + getId() + ", importoPagamento="
				+ getImportoPagamento() + ", causalePagamento=" + getCausalePagamento() + ", docnumPagamento="
				+ getDocnumPagamento() + ", modalitaId=" + getModalitaId() + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJsonDoc() {
		return jsonDoc;
	}

	public void setJsonDoc(String jsonDoc) {
		this.jsonDoc = jsonDoc;
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

	public Integer getModalitaId() {
		return modalitaId;
	}

	public void setModalitaId(Integer modalitaId) {
		this.modalitaId = modalitaId;
	}

	public String getIstanzaPagamento() {
		return istanzaPagamento;
	}

	public void setIstanzaPagamento(String istanzaPagamento) {
		this.istanzaPagamento = istanzaPagamento;
	}

	@Transient
	@JsonIgnore
	public Modalita getModalita() {
		return Modalita.getEnumFromId(getModalitaId());
	}

}
