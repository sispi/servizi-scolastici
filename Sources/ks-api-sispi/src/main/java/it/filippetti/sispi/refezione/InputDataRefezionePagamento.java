package it.filippetti.sispi.refezione;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InputDataRefezionePagamento {

	@JsonProperty("cfiscale-alunno")
	private String cFiscaleAlunno;

	@JsonProperty("cfiscale-pagante")
	private String cFiscalePagante;

	@JsonProperty("codice-domanda")
	private Integer codiceDomanda;

	@JsonProperty("cognome-pagamento")
	private String cognomePagamento;

	@JsonProperty("data-pagamento")
	private String dataPagamento;

	@JsonProperty("des-iuv")
	private String desIuv;

	@JsonProperty("des-pag")
	private String desPag;

	@JsonProperty("des-pdf-rt")
	private DocumentoInput desPdfRt;

	@JsonProperty("des-xml-rt")
	private DocumentoInput desXmlRt;

	@JsonProperty("email-pagamento")
	private String emailPagamento;

	@JsonProperty("nome-pagamento")
	private String nomePagamento;

	@JsonProperty("num-anno")
	private Integer numAnno;

	@JsonProperty("num-imp-pag")
	private Integer numImpPag;

	@JsonProperty("protocollo")
	private String protocollo;

	public String getcFiscaleAlunno() {
		return cFiscaleAlunno;
	}

	public void setcFiscaleAlunno(String cFiscaleAlunno) {
		this.cFiscaleAlunno = cFiscaleAlunno;
	}

	public String getcFiscalePagante() {
		return cFiscalePagante;
	}

	public void setcFiscalePagante(String cFiscalePagante) {
		this.cFiscalePagante = cFiscalePagante;
	}

	public Integer getCodiceDomanda() {
		return codiceDomanda;
	}

	public void setCodiceDomanda(Integer codiceDomanda) {
		this.codiceDomanda = codiceDomanda;
	}

	public String getCognomePagamento() {
		return cognomePagamento;
	}

	public void setCognomePagamento(String cognomePagamento) {
		this.cognomePagamento = cognomePagamento;
	}

	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getDesIuv() {
		return desIuv;
	}

	public void setDesIuv(String desIuv) {
		this.desIuv = desIuv;
	}

	public String getDesPag() {
		return desPag;
	}

	public void setDesPag(String desPag) {
		this.desPag = desPag;
	}

	public DocumentoInput getDesPdfRt() {
		return desPdfRt;
	}

	public void setDesPdfRt(DocumentoInput desPdfRt) {
		this.desPdfRt = desPdfRt;
	}

	public DocumentoInput getDesXmlRt() {
		return desXmlRt;
	}

	public void setDesXmlRt(DocumentoInput desXmlRt) {
		this.desXmlRt = desXmlRt;
	}

	public String getEmailPagamento() {
		return emailPagamento;
	}

	public void setEmailPagamento(String emailPagamento) {
		this.emailPagamento = emailPagamento;
	}

	public String getNomePagamento() {
		return nomePagamento;
	}

	public void setNomePagamento(String nomePagamento) {
		this.nomePagamento = nomePagamento;
	}

	public Integer getNumAnno() {
		return numAnno;
	}

	public void setNumAnno(Integer numAnno) {
		this.numAnno = numAnno;
	}

	public Integer getNumImpPag() {
		return numImpPag;
	}

	public void setNumImpPag(Integer numImpPag) {
		this.numImpPag = numImpPag;
	}

	public String getProtocollo() {
		return protocollo;
	}

	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}

}
