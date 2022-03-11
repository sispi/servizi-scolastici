/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.protocollazione;

import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.commons.Config;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.XMLUtil;
import it.kdm.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.io.IOUtils;
import org.jaxen.JaxenException;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;

/**
 * 
 * @author stefano.vigna
 */
public class WSProtocollazioneTest {

    public WSProtocollazioneTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private static QName inoltroFirmaQName = new QName("inoltroFirma");

    @Test
    public void testConfigInoltroFirma() {

	String inoltroFirmaProvider = null;
	String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", "C_I840", "C-I840-01");

	Config config;
	try {
	    config = new Config();
	

	    OMElement omelement = config.getNode(xpathProvider);
	    if (omelement != null) {
		OMAttribute attr = omelement.getAttribute(inoltroFirmaQName);
		if(attr!=null){
		    inoltroFirmaProvider = attr.getAttributeValue();    
		}	
	    }

	    // se e' specificato inoltro firma sul provider
	    if (inoltroFirmaProvider != null) {
		if (!inoltroFirmaProvider.equalsIgnoreCase("true")) {
		    throw new Exception("Il provider non supporta l'inoltro alla Firma.");
		}
	    }
	    else {
		boolean inoltroFirmaSistema = false;
		OMElement omeInoltroFirmaSistema = config.getNode("//inoltroFirma");
		// se e' specificato nodo inoltroFirma generale (fino a Docer
		// 1.3.4)
		if (omeInoltroFirmaSistema == null) {
		    throw new Exception("Il sistema di protocollo non supporta l'inoltro alla Firma.");
		}
		inoltroFirmaSistema = Boolean.valueOf(omeInoltroFirmaSistema.getText());
		if (!inoltroFirmaSistema) {
		    throw new Exception("Il sistema di protocollo non supporta l'inoltro alla Firma.");
		}
		
	    }
	    
	    System.out.println("ad inoltro firma");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	}

    }

    @Test
    public void testConfig() {
	Config config;
	try {
	    config = new Config();
	    OMElement fascSecondariFormatNode = config.getNode("//section[@name='variables']/fasc_secondari_format");
	    System.out.println(fascSecondariFormatNode.getText());
	}
	catch (IOException e) {
	    System.out.println(e.getMessage());
	}
	catch (XMLStreamException e) {
	    System.out.println(e.getMessage());
	}
	catch (JaxenException e) {
	    System.out.println(e.getMessage());
	}

    }

    @Test
    public void test() throws Exception {
	
	try{
	InputStream resStream = this.getClass().getResourceAsStream("/xmlInput.xml");
	String datiProtocollo = IOUtils.toString(resStream);

	WSProtocollazione p = new WSProtocollazione();
	String ticket = p.login("admin", "admin", "EMR");

	long documentoId = 18730;

	p.protocollaById(ticket, documentoId, datiProtocollo);
	}
	catch(Exception e){
	    e.printStackTrace();
	}
    }

    @Test
    public void testValidate() throws Exception {
	InputStream resStream = this.getClass().getResourceAsStream("/xmlInput.xml");
	String datiProtocollo = IOUtils.toString(resStream);

	XMLUtil dom = null;

	dom = new XMLUtil(datiProtocollo);

	URL xsdUrl = this.getClass().getResource("/input-validation.xsd");
	if (xsdUrl == null) {
	    throw new Exception("file xsd non trovato");
	}

	File schema = ResourceLoader.openFile(xsdUrl);
	dom.validate(schema.getAbsolutePath());

    }

    @Test
    public void testProtocolla() throws Exception {
	InputStream resStream = this.getClass().getResourceAsStream("/segnatura.xml");
	String datiProtocollo = IOUtils.toString(resStream);

	WSProtocollazione p = new WSProtocollazione();
	String ticket = p.login("admin", "admin", "EMR");

	long documentoId = 5027;

	p.protocollaById(ticket, documentoId, datiProtocollo);

    }
}
