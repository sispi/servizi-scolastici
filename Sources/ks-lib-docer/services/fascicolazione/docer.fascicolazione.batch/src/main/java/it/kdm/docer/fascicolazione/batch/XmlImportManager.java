package it.kdm.docer.fascicolazione.batch;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import it.kdm.docer.fascicolazione.batch.persistence.LoadDbManager;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SL_550 on 12/01/2016.
 */
public class XmlImportManager {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(XmlImportManager.class);

    private String XML_PATH;
    private LoadDbManager dbManager;

    public XmlImportManager(String xmlPath) {
        this.XML_PATH = xmlPath;
        this.dbManager = new LoadDbManager();
    }

    /**
     * Importa il file xml nel db
     */
    public void executeImport() throws Exception {
        try {
            log.info("Inizio import da XML...");

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            File xmlFile = new File(XML_PATH);
            if (!xmlFile.exists())
                throw new Exception("File XML da importare non trovato: " + XML_PATH);

            // Inizio il parsing del documento
            Document xmlDocument = docBuilder.parse(xmlFile);
            NodeList rootList = xmlDocument.getElementsByTagName("root");

            // Svuoto la tabella sul db
            dbManager.truncateFascicoli(null);

            if (rootList != null && rootList.item(0) != null
                    && rootList.item(0).getNodeType() == Node.ELEMENT_NODE) {

                NodeList children = rootList.item(0).getChildNodes();

                for (int i=0; i < children.getLength(); i++) {
                    Node childNode = children.item(i);

                    if (childNode.getNodeType() != Node.ELEMENT_NODE)
                        continue;

                    if (childNode.getNodeName().equalsIgnoreCase("ENTE")) {
                        importEnte(childNode);
                    }

                    if (childNode.getNodeName().equalsIgnoreCase("AOO")) {
                        importAOO(childNode);
                    }
                }
            }

            int recordCount = dbManager.getCountFascicoli();
            log.info("Fine import da XML. Record inseriti nel database: " + recordCount);

        } catch(SQLException e){
            log.error("Errore nel caricamento del database: " + e.getMessage());
            throw e;
        } catch(Exception e){
            throw e;
        } finally {
            try {
                dbManager.closeConnection();
            } catch (Exception e) {
                log.error("Errore in chiusura connessione: " + e.getMessage(), e);
            }
        }
    }

    /**
     *
     * @param node
     */
    private void importEnte(Node node) throws Exception {
        NodeList children = node.getChildNodes();

        for (int i=0; i < children.getLength(); i++) {
            Node childNode = children.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if (childNode.getNodeName().equalsIgnoreCase("AOO")) {
                importAOO(childNode);
                continue;
            }
        }

    }

    /**
     *
     * @param node
     */
    private void importAOO(Node node) throws Exception {
        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node childNode = children.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if (childNode.getNodeName().equalsIgnoreCase("TITOLARIO")) {
                importTitolario(childNode);
                continue;
            }

        }

    }

    /**
     *
     * @param node
     */
    private void importTitolario(Node node) throws Exception {

        Map<String, String> metadata = new HashMap<String, String>();
        NodeList propsNodeList = node.getChildNodes();

        for (int j = 0; j < propsNodeList.getLength(); j++) {
            Node propElement = propsNodeList.item(j);

            if (propElement.getNodeType() != Node.ELEMENT_NODE)
                continue;

            String propName = propElement.getNodeName();
            String propValue = propElement.getTextContent();

            if (propName.equalsIgnoreCase("COD_TITOLARIO")) {
                continue;
            } else if (propName.equalsIgnoreCase("TITOLARIO")){
                continue;
            } else if (propName.equalsIgnoreCase("ACLS")){
                String acls = "";

                NodeList aclNodes = propElement.getChildNodes();
                for (int jj = 0; jj < aclNodes.getLength(); jj++) {
                    Node aclNode = aclNodes.item(jj);

                    if (aclNode.getNodeType() != Node.ELEMENT_NODE)
                        continue;

                    String userOrGroup = ((Element)aclNode).getElementsByTagName("KEY").item(0).getTextContent();
                    String rights = ((Element)aclNode).getElementsByTagName("VALUE").item(0).getTextContent();
                    acls+=userOrGroup+":"+rights+";";
                }
                propValue = acls;
            } else if (propName.equalsIgnoreCase("FASCICOLO")){
                continue;
            } else if (propName.equalsIgnoreCase("DES_TITOLARIO")){
                // Ripulisco dal carattere 160, lo sostituisco col 32
                propValue = propValue.replace('\u00A0', ' ');
            }

            metadata.put(propName, propValue);
        }

        // Inserisco il titolario
        dbManager.insert(metadata, null);

        // Figli del titolario
        NodeList children = node.getChildNodes();
        for (int index = 0; index < children.getLength(); index++) {

            Node childNode = children.item(index);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if (childNode.getNodeName().equalsIgnoreCase("TITOLARIO")) {
                importTitolario(childNode);
                continue;
            }

            if (childNode.getNodeName().equalsIgnoreCase("FASCICOLO")) {
                importFascicolo(childNode, null);
                continue;
            }
        }

    }

    /**
     *
     * @param node
     */
    private void importFascicolo(Node node, String desTitolario) throws Exception {

        Map<String, String> metadata = new HashMap<String, String>();
        NodeList propsNodeList = node.getChildNodes();
        for (int j = 0; j < propsNodeList.getLength(); j++) {

            Node propElement = propsNodeList.item(j);

            if (propElement.getNodeType() != Node.ELEMENT_NODE)
                continue;

            String propName = propElement.getNodeName();
            String propValue = propElement.getTextContent();

            if (propName.equalsIgnoreCase("DATA_APERTURA")) {
                continue;
            } else if (propName.equalsIgnoreCase("DATA_CHIUSURA")){
                continue;
            } else if (propName.equalsIgnoreCase("ACLS")){
                String acls = "";

                NodeList aclNodes = propElement.getChildNodes();
                for (int jj = 0; jj < aclNodes.getLength(); jj++) {
                    Node aclNode = aclNodes.item(jj);

                    if (aclNode.getNodeType() != Node.ELEMENT_NODE)
                        continue;

                    String userOrGroup = ((Element)aclNode).getElementsByTagName("KEY").item(0).getTextContent();
                    String rights = ((Element)aclNode).getElementsByTagName("VALUE").item(0).getTextContent();
                    acls+=userOrGroup+":"+rights+";";
                }
                propValue = acls;
            } else if (propName.equalsIgnoreCase("NUM_FASCICOLO")){
                continue;
            } else if (propName.equalsIgnoreCase("FASCICOLO")){
                continue;
            } else if (propName.equalsIgnoreCase("DES_FASCICOLO")){
                // Ripulisco dal carattere 160, lo sostituisco col 32
                propValue = propValue.replace('\u00A0', ' ');
            }

            metadata.put(propName, propValue);
        }

        if (desTitolario != null)
            metadata.put("DES_TITOLARIO", desTitolario);

        // Inserisco il fascicolo
        dbManager.insert(metadata, null);

        // Controllo se ha fascicoli figli
        NodeList children = node.getChildNodes();
        for (int index = 0; index < children.getLength(); index++) {

            Node childNode = children.item(index);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if (childNode.getNodeName().equalsIgnoreCase("FASCICOLO")) {
                importFascicolo(childNode, desTitolario);
                continue;
            }

        }

    }

    /**
     *
     * @param filePath
     * @param dom
     * @param encoding
     * @throws FileNotFoundException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    private void xmlToFile(String filePath, Document dom, String encoding) throws FileNotFoundException, TransformerConfigurationException, TransformerException, IOException {

        java.io.FileOutputStream fw = new java.io.FileOutputStream(filePath);

        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty("encoding", encoding);
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        t.transform(new DOMSource(dom), new StreamResult(fw));

        fw.flush();
        fw.close();
    }

}
