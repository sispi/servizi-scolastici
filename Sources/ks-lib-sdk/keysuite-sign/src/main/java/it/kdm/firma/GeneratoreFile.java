package it.kdm.firma;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.europa.esig.dss.detailedreport.jaxb.XmlBasicBuildingBlocks;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlDetailedReport;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSubXCV;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.simplereport.jaxb.XmlSignature;
import eu.europa.esig.dss.simplereport.jaxb.XmlSimpleReport;
import eu.europa.esig.dss.simplereport.jaxb.XmlTimestamp;
import eu.europa.esig.dss.simplereport.jaxb.XmlToken;
import eu.europa.esig.dss.validation.reports.Reports;
import it.kdm.firma.bean.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneratoreFile {

	private EsitoVerifiche esitoVerifiche;
	private Document xmlReport;
	private String jsonReport;
	
	public String getXmlStringReport() {
		if (xmlReport == null)
			createXmlReport();
		return Utils.documentToString(xmlReport);
	}

	public String getJsonReport() {
		if (jsonReport == null)
			createJsonReport();
		return jsonReport;
	}

	public GeneratoreFile(Reports reports, Map<String, String> cFs) {
		this.esitoVerifiche = generateEsitoVerifiche(reports,cFs);
	}
	
	public void writeFiles(String xmlPath, String jsonPath) throws TransformerException, TransformerFactoryConfigurationError, ParserConfigurationException, FileNotFoundException {
		writeXmlFile(xmlPath);
		writeJsonFile(jsonPath);
	}
	
	
	private void createXmlReport() {
		// Root element
		DocumentBuilder builder = null;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    xmlReport = builder.newDocument();
		Element root = xmlReport.createElement(EsitoVerifiche.NOME);
		xmlReport.appendChild(root);

		// VerificaFormato
		Element verificaFormato = xmlReport.createElement(VerificaFormato.NOME);
		root.appendChild(verificaFormato);
		appendTextNode(xmlReport, verificaFormato, VerificaFormato.NOME_VERIFICA_RICONOSCIMENTO_FORMATO, esitoVerifiche.getVerificaFormato().getVerificaRiconoscimentoFormato());
		appendTextNode(xmlReport, verificaFormato, VerificaFormato.NOME_MESSAGGIO_RICONOSCIMENTO_FORMATO, esitoVerifiche.getVerificaFormato().getMessaggioRiconoscimentoFormato());

		// VerificaFirma
		Element verificaFirma = xmlReport.createElement(VerificaFirma.NOME);
		root.appendChild(verificaFirma);
		Element firmatari = xmlReport.createElement(VerificaFirma.NOME_LISTA_FORMATARI);
		verificaFirma.appendChild(firmatari);

		Element firmatario;
		Element esitoFirma;
		Element verificaFirmaInt;
		VerificaFirmaInt verificaFirmaIntPojo;

		// Firmatari
		for (Firmatario firm : esitoVerifiche.getVerificaFirma().getFirmatari()) {
			firmatario = xmlReport.createElement(Firmatario.NOME);
			firmatari.appendChild(firmatario);

			appendTextNode(xmlReport, firmatario, Firmatario.NOME_ORDINE_FIRMA, ""+firm.getOrdineFirma());
			appendTextNode(xmlReport, firmatario, Firmatario.NOME_COGNOME_NOME, firm.getCognomeNome());
			appendTextNode(xmlReport, firmatario, Firmatario.NOME_CODICE_FISCALE, firm.getCf());
			appendTextNode(xmlReport, firmatario, Firmatario.NOME_FORMATO_FIRMA, firm.getFormatoFirma());
			appendTextNode(xmlReport, firmatario, Firmatario.NOME_RIFERIMENTO_TEMPORALE_USATO, firm.getRiferimentoTemporaleUsato());
			//appendTextNode(xmlReport, firmatario, Firmatario.NOME_TIPO_RIFERIMENTO_TEMPORALE_USATO, firm.getTipoRiferimentoTemporaleUsato());

			esitoFirma = xmlReport.createElement(EsitoFirma.NOME);
			firmatario.appendChild(esitoFirma);

			appendTextNode(xmlReport, esitoFirma, EsitoFirma.NOME_CONTROLLO_CONFORMITA, firm.getEsitoFirma().getControlloConformita());

			verificaFirmaInt = xmlReport.createElement(VerificaFirmaInt.NOME);
			esitoFirma.appendChild(verificaFirmaInt);
			verificaFirmaIntPojo = firm.getEsitoFirma().getVerificaFirma();

			appendTextNode(xmlReport, verificaFirmaInt, VerificaFirmaInt.NOME_CODICE_ESITO, verificaFirmaIntPojo.getCodiceEsito());
			appendTextNode(xmlReport, verificaFirmaInt, VerificaFirmaInt.NOME_CONTROLLO_CRITTOGRAFICO, verificaFirmaIntPojo.getControlloCrittografico());
			appendTextNode(xmlReport, verificaFirmaInt, VerificaFirmaInt.NOME_CONTROLLO_CATENA_TRUSTED, verificaFirmaIntPojo.getControlloCatenaTrusted());
			appendTextNode(xmlReport, verificaFirmaInt, VerificaFirmaInt.NOME_CONTROLLO_CERTIFICATO, verificaFirmaIntPojo.getControlloCertificato());
			appendTextNode(xmlReport, verificaFirmaInt, VerificaFirmaInt.NOME_CONTROLLO_CRL, verificaFirmaIntPojo.getControlloCRL());
		}
	}
	
	public void writeXmlFile(String path) throws TransformerException, TransformerFactoryConfigurationError, ParserConfigurationException, FileNotFoundException {
		createXmlReport();
		// Write to file
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(xmlReport);
		StreamResult result = new StreamResult(new FileOutputStream(new File(path)));
		transformer.transform(source,  result);
	}
	
	public void writeJsonFile(String path) throws FileNotFoundException {
		createJsonReport();
		
		PrintWriter outFile = new PrintWriter(path);
		outFile.println(jsonReport);
		outFile.close();
	}
	
	public EsitoVerifiche getEsitoVerifiche() {
		return esitoVerifiche;
	}

	private void createJsonReport() {
		Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();

		jsonReport = gson.toJson(esitoVerifiche);
		
	}

	private void appendTextNode(Document doc, Element parentNode, String nodeName, String nodeValue) {
		Element node = doc.createElement(nodeName);
		node.appendChild(doc.createTextNode(nodeValue));
		parentNode.appendChild(node);
	}
	
	private EsitoVerifiche generateEsitoVerifiche(Reports reports, Map<String, String> cFs) {
		XmlSimpleReport simpleReport = reports.getSimpleReportJaxb();
		XmlDetailedReport detailedReport = reports.getDetailedReportJaxb();
		boolean verificaGlobale = true;
		VerificaFormato verificaFormato;
		VerificaFirma verificaFirma;
		
		List<XmlToken> xmlTokens = simpleReport.getSignatureOrTimestamp();
		List<XmlBasicBuildingBlocks> buildingBlocks = detailedReport.getBasicBuildingBlocks();

		List<Firmatario> firmatari = new ArrayList<>();
		List<XmlTimestamp> xmlTimestamps = new ArrayList<>();

		for (XmlToken xmlToken : xmlTokens) {

			if (xmlToken instanceof XmlTimestamp) {
				XmlTimestamp timestamp = (XmlTimestamp) xmlToken;
				xmlTimestamps.add(timestamp);
			}

			if (xmlToken instanceof XmlSignature) {
				XmlSignature signature = (XmlSignature) xmlToken;

				String signatureId = signature.getId();
				XmlBasicBuildingBlocks block = getBuildingBlock(signatureId, buildingBlocks);

				//String indication = block.getFC().getConclusion().getIndication().name();
				String indication = block.getConclusion().getIndication().name();

				verificaGlobale = verificaGlobale && (EsitoVerifiche.ESIG_PASSED.equals(indication) || EsitoVerifiche.ESIG_TOTAL_PASSED.equals(indication));

				String cf = cFs.get(signature.getSignedBy());
				if (cf == null)
					cf = "NON PRESENTE";

				/*List<XmlBasicBuildingBlocks> timestamps = getTimeStampBlock(buildingBlocks);

				for( XmlBasicBuildingBlocks ts : timestamps ){
					String indication1 = ts.getConclusion().getIndication().name();
					Date dt = ts.getSAV().getValidationTime();
					XmlTimestamp xmlTimestamp = new XmlTimestamp();
					xmlTimestamp.setProducedBy();
					xmlTimestamp.setProductionTime(dt);
					xmlTimestamp.setIndication(ts.getConclusion().getIndication());
					xmlTimestamp.setId(ts.getId());
					xmlTimestamp.setSubIndication(ts.getConclusion().getSubIndication());
					//xmlTimestamp.setCertificateChain(ts.getISC().getCertificateChain());
					xmlTimestamps.add(xmlTimestamp);

					verificaGlobale = verificaGlobale && (EsitoVerifiche.ESIG_PASSED.equals(indication1) || EsitoVerifiche.ESIG_TOTAL_PASSED.equals(indication1));
				}*/

				Firmatario firmatario = generateFirmatario(firmatari.size()+1, signature, block, cf);
				firmatari.add(firmatario);
			}



		}
		
		verificaFormato = new VerificaFormato(verificaGlobale, EsitoVerifiche.NOT_SUPPORTED);
		verificaFirma = new VerificaFirma(firmatari,xmlTimestamps);
		
		return new EsitoVerifiche(verificaFormato, verificaFirma);
	}
	
	private XmlBasicBuildingBlocks getBuildingBlock(String id, List<XmlBasicBuildingBlocks> blocks) {
		for (XmlBasicBuildingBlocks block : blocks) {
			if (id.equals(block.getId())) return block;
		}
		return null;
	}

	private List<XmlBasicBuildingBlocks> getTimeStampBlock(List<XmlBasicBuildingBlocks> blocks) {

		List<XmlBasicBuildingBlocks> ts = new ArrayList<>();
		for (XmlBasicBuildingBlocks block : blocks) {
			if (Context.TIMESTAMP.equals(block.getType()))
				ts.add(block);
		}
		return ts;
	}
	
	private EsitoFirma generateEsitoFirma(XmlSignature signature, XmlBasicBuildingBlocks block) {
		VerificaFirmaInt verificaFirma = new VerificaFirmaInt(
				convertStatusToString(block.getConclusion().getIndication().name()),
				convertStatusToString(block.getCV().getConclusion().getIndication().name()),
				convertStatusToString(block.getXCV().getConclusion().getIndication().name()),
				convertStatusToString(block.getISC().getConclusion().getIndication().name()),
				convertStatusToString(getCertificateRevocationStatus(block.getXCV().getSubXCV()))
				);
		
		String controlloConformita = verificaFirma.getCompliancyString();
		
		return new EsitoFirma(controlloConformita, verificaFirma);
	}
	
	private Firmatario generateFirmatario(int signaturePosition, XmlSignature signature, XmlBasicBuildingBlocks block, String cf) {
		EsitoFirma esitoFirma = generateEsitoFirma(signature, block);

		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		DateTime dateTime = new DateTime(signature.getSigningTime());

		Firmatario firmatario = new Firmatario(
				signaturePosition,
				signature.getSignedBy(),
				signature.getSignatureFormat().toString(),
				fmt.print(dateTime.toDateTime(DateTimeZone.UTC)),
				esitoFirma,
				cf
				);

		/*
			Qualification: QESig
			Signature format: CAdES-BASELINE-B
			Indication: TOTAL_PASSED
			Certificate Chain:
				Dante Ciantra
				Intesi Group EU Qualified Electronic Signature CA G2
			On claimed time: 2021-01-20T07:54:23
			Best signature time: 2021-02-12T17:18:13
			Signature position: 1 out of 1
			Signature scope: Full document
		 */

		firmatario.setXmlSignature(signature);

		return firmatario;
	}
	
	private String getCertificateRevocationStatus(List<XmlSubXCV> subXcvs) {
		for (XmlSubXCV subXcv : subXcvs) {
			for (XmlConstraint constraint : subXcv.getConstraint()) {
				//if (EsitoVerifiche.ESIG_IDS_CERTIFICATE_REVOCATION.equals(constraint.getName().getNameId())) {
				if (EsitoVerifiche.ESIG_IDS_CERTIFICATE_REVOCATION.equals(constraint.getName().getKey())) {
					return constraint.getStatus().name();
				}
			}
		}
		return EsitoVerifiche.NEGATIVE;
	}
	
	private String convertStatusToString(String status) {
		if (EsitoVerifiche.ESIG_PASSED.equals(status) || 
				EsitoVerifiche.ESIG_TOTAL_PASSED.equals(status) || 
				EsitoVerifiche.ESIG_IGNORED.equals(status) ||
				EsitoVerifiche.ESIG_STATUS_OK.equals(status)) {
			return EsitoVerifiche.POSITIVE; 
		} else {
			return EsitoVerifiche.NEGATIVE;
		}
	}
	
}
