/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.jaxp.OMSource;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.jaxen.JaxenException;
import org.xml.sax.SAXException;

/**
 *
 * @author stefano.vigna
 */
public class XMLUtil {
    private OMElement config;
     
    public XMLUtil(String xml) throws IOException, XMLStreamException {
        loadConfig(xml);
    }
    
    public String toXML() {
        return config.toString();
    }
    public final void loadConfig(String xml) throws IOException, XMLStreamException {
        XMLStreamReader xmlStreamReader = StAXUtils.createXMLStreamReader(new StringReader(xml));
        StAXOMBuilder builder = new StAXOMBuilder(xmlStreamReader);
        this.config = builder.getDocumentElement();
    }
    
    public void addDocumentNode(String xpath, OMElement newElement) throws JaxenException {
        OMElement node = getNode(xpath);
        node.addChild(newElement);
    }
    
    public void addNode(String xpath, String nodeName) throws JaxenException {
        OMElement node = getNode(xpath);
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMElement newElement = fac.createOMElement(new QName(nodeName));
        node.addChild(newElement);
    }
    
    public void addNode(String xpath, OMElement newElement) throws JaxenException {
        OMElement node = getNode(xpath);
        node.addChild(newElement);
    }
    
    public void setNodeValue(String xpath, String value) throws JaxenException {
        OMElement node = getNode(xpath);
        node.setText(value);
    }
     
    public OMElement getNode(String xpath) throws JaxenException {
        AXIOMXPath path = new AXIOMXPath(xpath);
	return (OMElement)path.selectSingleNode(this.config);
    }
    
    public String getNodeValue(String xpath) throws JaxenException {
        AXIOMXPath path = new AXIOMXPath(xpath);
	OMElement elem = (OMElement)path.selectSingleNode(this.config);
        
        return elem.getText();
    }
    
    public List<OMElement> getNodes(String xpath) throws JaxenException {
        AXIOMXPath path = new AXIOMXPath(xpath);
        return (List<OMElement>)path.selectNodes(this.config);
    }
    
    public void validate(String schemaPath) throws IOException, SAXException, XMLStreamException, Exception {
        
        Validator validator;
        
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(new FileInputStream(schemaPath)));
        validator = schema.newValidator();
        
        try {
            validator.validate(new OMSource(this.config));
        } catch (Exception ex) {
            throw new Exception("L'xml di input non e' valido: " + ex.getMessage(), ex);
        }
        
    }
}
