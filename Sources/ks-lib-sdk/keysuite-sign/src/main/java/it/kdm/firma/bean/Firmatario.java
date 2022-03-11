package it.kdm.firma.bean;

import eu.europa.esig.dss.simplereport.jaxb.XmlSignature;

public class Firmatario {
	public static final String NOME = "Firmatario";
	public static final String NOME_ORDINE_FIRMA = "OrdineFirma";
	public static final String NOME_COGNOME_NOME = "CognomeNome";
	public static final String NOME_CODICE_FISCALE = "CodiceFiscale";
	public static final String NOME_FORMATO_FIRMA = "FormatoFirma";
	public static final String NOME_RIFERIMENTO_TEMPORALE_USATO = "RiferimentoTemporaleUsato";
	public static final String NOME_TIPO_RIFERIMENTO_TEMPORALE_USATO = "TipoRiferimentoTemporaleUsato";
	
	private Integer ordineFirma;
	private String cognomeNome;
	private String formatoFirma;
	private String riferimentoTemporaleUsato;
	//private String tipoRiferimentoTemporaleUsato;
	private EsitoFirma esitoFirma;
	private String cf;
	private XmlSignature xmlSignature = null;
	
	/*public Firmatario(String ordineFirma, String cognomeNome, String formatoFirma, String riferimentoTemporaleUsato,
			String tipoRiferimentoTemporaleUsato, EsitoFirma esitoFirma) {
		this.ordineFirma = ordineFirma;
		this.cognomeNome = cognomeNome;
		this.formatoFirma = formatoFirma;
		this.riferimentoTemporaleUsato = riferimentoTemporaleUsato;
		this.tipoRiferimentoTemporaleUsato = tipoRiferimentoTemporaleUsato;
		this.esitoFirma = esitoFirma;
	}*/
	
	public Firmatario(int ordineFirma, String cognomeNome, String formatoFirma, String riferimentoTemporaleUsato, EsitoFirma esitoFirma, String cf) {
		this.ordineFirma = ordineFirma;
		this.cognomeNome = cognomeNome;
		this.formatoFirma = formatoFirma;
		this.riferimentoTemporaleUsato = riferimentoTemporaleUsato;
		this.esitoFirma = esitoFirma;
		this.cf = cf;
	}

	public void setXmlSignature(XmlSignature xmlSignature){
		this.xmlSignature = xmlSignature;
	}

	public XmlSignature getXmlSignature(){
		return this.xmlSignature;
	}

	public Integer getOrdineFirma() {
		return ordineFirma;
	}

	public void setOrdineFirma(Integer ordineFirma) {
		this.ordineFirma = ordineFirma;
	}

	public String getCognomeNome() {
		return cognomeNome;
	}

	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = cognomeNome;
	}

	public String getFormatoFirma() {
		return formatoFirma;
	}

	public void setFormatoFirma(String formatoFirma) {
		this.formatoFirma = formatoFirma;
	}

	public String getRiferimentoTemporaleUsato() {
		return riferimentoTemporaleUsato;
	}

	public void setRiferimentoTemporaleUsato(String riferimentoTemporaleUsato) {
		this.riferimentoTemporaleUsato = riferimentoTemporaleUsato;
	}

	/*public String getTipoRiferimentoTemporaleUsato() {
		return tipoRiferimentoTemporaleUsato;
	}

	public void setTipoRiferimentoTemporaleUsato(String tipoRiferimentoTemporaleUsato) {
		this.tipoRiferimentoTemporaleUsato = tipoRiferimentoTemporaleUsato;
	}*/

	public EsitoFirma getEsitoFirma() {
		return esitoFirma;
	}

	public void setEsitoFirma(EsitoFirma esitoFirma) {
		this.esitoFirma = esitoFirma;
	}
	
	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}
}