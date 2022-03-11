/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.commons;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.jaxen.JaxenException;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lorenxs
 */
public class Config {

    Logger logger = org.slf4j.LoggerFactory.getLogger(Config.class);

    private File configFile = null;
    private OMElement configElement;

    public Config() throws IOException, XMLStreamException {

        /*try {
            Properties props = ConfigurationUtils.loadProperties("configuration.properties");

            this.configFile = ConfigurationUtils.getFile(props.getProperty("config.path"));
            if (this.configFile.exists()) {
                this.loadConfig();
            } else {
                IOException e = new IOException("file di configurazione non trovato in " + configFile.getAbsolutePath());
                logger.error(e.getMessage(), e);
            }
        }catch (ConfigurationLoadingException e) {       
            logger.error(e.getMessage(), e);
        }*/
    }

    public Config(File configurationFile) throws IOException, XMLStreamException {
        this.configFile = configurationFile;
        if (this.configFile.exists()) {
            this.loadConfig();
        } else {
            logger.error("file di configurazione non trovato in " + this.configFile.getAbsolutePath());
            throw new IOException("file di configurazione non trovato in " + this.configFile.getAbsolutePath());
        }

    }

    public Config(String xml) throws XMLStreamException {

        this.configFile = new File("");

        StAXOMBuilder builder;
        try {
            builder = new StAXOMBuilder(new ByteArrayInputStream(xml.getBytes()));
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        try {
            this.configElement = builder.getDocumentElement();
        } catch (Exception e) {
            XMLStreamException xmlex = new XMLStreamException("Config(String): errore formato XML: " + e.getMessage());
            logger.error(xmlex.getMessage(),xmlex);
            throw xmlex;
        }

    }

    public Config(OMElement xml) throws XMLStreamException {

        this.configFile = new File("");

        this.configElement = xml;
    }

    public File getConfigFile() throws IOException {
        if (this.configFile == null) {

            this.configFile = File.createTempFile("config", ".xml");

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            try {
                docBuilder = docFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                logger.error(e.getMessage(), e);
                throw new IOException(e.getMessage());
            }

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("configuration");
            doc.appendChild(rootElement);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            try {
                transformer = transformerFactory.newTransformer();
            } catch (TransformerConfigurationException e) {
                logger.error(e.getMessage(), e);
                throw new IOException(e.getMessage());
            }
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(this.configFile);

            try {
                transformer.transform(source, result);
            } catch (TransformerException e) {
                logger.error(e.getMessage(), e);
                throw new IOException(e.getMessage());
            }
        }

        return this.configFile;
    }

    public void setConfigFile(File configFile) throws IOException, XMLStreamException {
        this.configFile = configFile;
        this.loadConfig();
    }

    public final void loadConfig() throws IOException, XMLStreamException {
        String path;
        try {
            path = getConfigFile().getAbsolutePath();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        StAXOMBuilder builder;
        try {
            builder = new StAXOMBuilder(path);
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        try {
            this.configElement = builder.getDocumentElement();
        } catch (Exception e) {
            XMLStreamException xmlex = new XMLStreamException("loadConfig(): errore formato XML: " + e.getMessage());
            logger.error(xmlex.getMessage(), xmlex);
            throw xmlex;
        }
    }

    public final void saveConfig() throws XMLStreamException, IOException {
        /*XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(FileUtils.openOutputStream(getConfigFile()));

        if (this.configElement == null) {
            loadConfig();
        }

        this.configElement.serialize(writer);
        writer.close();*/
        throw new NotImplementedException();
    }

    public final String readConfig() throws XMLStreamException, IOException {

        if (this.configElement == null) {
            loadConfig();
        }
        return this.configElement.toString();
    }

    public final OMElement readConfigXml() throws IOException, XMLStreamException {

        if (this.configElement == null) {
            loadConfig();
        }
        return this.configElement;
    }

    public final void writeConfig(String config) throws IOException, XMLStreamException {
        /*if (config == null || config.equals("")) {
            throw new IOException("La stringa di configurazione e' vuota");
        }

        if (Base64.isBase64(config.trim().getBytes()[0])) {
            try {
                Base64 decoder = new Base64(true);
                config = new String(decoder.decode(config));
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        FileUtils.writeStringToFile(getConfigFile(), config, "UTF-8");
        loadConfig();*/
        throw new NotImplementedException();
    }

    /*public final void writeConfigXml(OMElement config) throws IOException, XMLStreamException {

        OMElement prevConfigElement = this.configElement;
        try {
            this.configElement = config;
            saveConfig();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.configElement = prevConfigElement;
        }

    }*/

    /*public void addNode(String xpath, OMElement newElement) throws JaxenException {
        OMElement node = getNode(xpath);
        node.addChild(newElement);
    }*/

    public OMElement getNode(String xpath) throws JaxenException {

        if (this.configElement == null) {
            try {
                loadConfig();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return null;
            } catch (XMLStreamException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }

        AXIOMXPath path = new AXIOMXPath(xpath);
        return (OMElement) path.selectSingleNode(this.configElement);
    }

    @SuppressWarnings("unchecked")
    public List<OMElement> getNodes(String xpath) throws JaxenException {

        if (this.configElement == null) {
            try {
                loadConfig();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return new ArrayList<OMElement>();
            } catch (XMLStreamException e) {
                logger.error(e.getMessage(), e);
                return new ArrayList<OMElement>();
            }
        }

        AXIOMXPath path = new AXIOMXPath(xpath);
        return (List<OMElement>) path.selectNodes(this.configElement);
    }
}