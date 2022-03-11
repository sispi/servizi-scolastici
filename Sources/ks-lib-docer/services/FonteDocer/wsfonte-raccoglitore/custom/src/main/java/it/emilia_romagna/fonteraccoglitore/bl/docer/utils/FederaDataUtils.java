package it.emilia_romagna.fonteraccoglitore.bl.docer.utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.jaxen.JaxenException;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class FederaDataUtils {

    static public String getCodiceFiscale(String xml) {

	if (xml == null) {
	    return "";
	}
	XPath xPath = XPathFactory.newInstance().newXPath();
	String codiceFiscale = "";
	try {
	    codiceFiscale = xPath.evaluate("//*[local-name() = 'Attribute' and @Name='CodiceFiscale']/*[local-name() = 'AttributeValue']", new InputSource(new StringReader(xml)));
	}
	catch (XPathExpressionException e) {
	    System.out.println(e.getMessage());
	    // throw new
	    // AuthorizationDeniedError("errore recupero codice fiscale da FederaData",
	    // e);
	}

	return codiceFiscale;
    }

    public static QName tipoRelazione_QName = new QName("tipoRelazione");
    public static QName tipoId_QName = new QName("tipoId");
    public static QName id_QName = new QName("id");

    static public List<String> getDeleghe(String xml) {

	List<String> gruppiDelega = new ArrayList<String>();

	OMElement omElement;
	try {
	    omElement = AXIOMUtil.stringToOM(xml);
	}
	catch (XMLStreamException e1) {
	    System.out.println(e1.getMessage());
	    return gruppiDelega;
	}

	List<OMElement> delegheElements = new ArrayList<OMElement>();

	AXIOMXPath pathCittadino = null;
	AXIOMXPath pathOperatorePA = null;
	try {
	    pathCittadino = new AXIOMXPath("//ruoli/ruolo[@name='CITTADINO']/deleghe/delega");
	    pathOperatorePA = new AXIOMXPath("//ruoli/ruolo[@name='OPERATORE_PA']/deleghe/delega");
	}
	catch (JaxenException e) {
	    System.out.println(e.getMessage());
	    return gruppiDelega;
	}

	try {
	    List<OMElement> delegheCittadino = pathCittadino.selectNodes(omElement);

	    if (delegheCittadino != null && delegheCittadino.size() > 0) {
		delegheElements.addAll(delegheCittadino);
	    }

	}
	catch (JaxenException e) {
	    System.out.println(e.getMessage());
	}

	try {
	    List<OMElement> delegheOperatorePA = pathOperatorePA.selectNodes(omElement);

	    if (delegheOperatorePA != null && delegheOperatorePA.size() > 0) {
		delegheElements.addAll(delegheOperatorePA);
	    }

	}
	catch (JaxenException e) {
	    System.out.println(e.getMessage());
	}

	if (delegheElements != null) {

	    for (OMElement omelement : delegheElements) {
		OMAttribute attTipoRelazione = omelement.getAttribute(tipoRelazione_QName);
		OMAttribute attTipoId = omelement.getAttribute(tipoId_QName);
		OMAttribute attId = omelement.getAttribute(id_QName);

		if (attTipoRelazione == null || attTipoId == null || attTipoId == null) {
		    continue;
		}

		// String gruppoOperatoriPa =
		// generateGroupIdOperatoriPa("operatore_pa",
		// "fonte", id_fonte);
		String gid = FonteDocerUtils.generateGroupIdFromDelega(attTipoRelazione.getAttributeValue(), attTipoId.getAttributeValue(), attId.getAttributeValue());

		if (gruppiDelega.contains(gid)) {
		    continue;
		}
		gruppiDelega.add(gid);
	    }
	}

	return gruppiDelega;

    }

}
