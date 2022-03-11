package it.kdm.firma.bean;

import eu.europa.esig.dss.simplereport.jaxb.XmlTimestamp;

import java.util.ArrayList;
import java.util.List;

public class VerificaFirma {
	public static final String NOME = "VerificaFirma";
	public static final String NOME_LISTA_FORMATARI = "Firmatari";
	
	private List<Firmatario> firmatari;
	private List<XmlTimestamp> xmlTimestamps = new ArrayList<>();
	
	public VerificaFirma(List<Firmatario> firmatari, List<XmlTimestamp> xmlTimestamps) {
		this.firmatari = firmatari;
		this.xmlTimestamps = xmlTimestamps;
	}

	public List<Firmatario> getFirmatari() {
		return firmatari;
	}

	public void setFirmatari(List<Firmatario> firmatari) {
		this.firmatari = firmatari;
	}

	public List<XmlTimestamp> getXmlTimestamps() {
		return xmlTimestamps;
	}

	public void setXmlTimestamps(List<XmlTimestamp> xmlTimestamps) {
		this.xmlTimestamps = xmlTimestamps;
	}
	
}