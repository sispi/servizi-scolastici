/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.registrazione;

import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.DocerExceptionException;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.clients.DocerServicesStub.GetUserRights;
import it.kdm.docer.clients.DocerServicesStub.GetUserRightsResponse;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.XMLUtil;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.commons.docerservice.BaseService;
import it.kdm.docer.commons.docerservice.BaseServiceException;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.classes.LoggedUserInfo;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import it.kdm.utils.ResourceLoader;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author lorenzo
 */
public class BusinessLogic extends BaseService implements IBusinessLogic {

    private static Configurations CONFIGURATIONS = new Configurations();

    private IProvider provider;

    private DocerServicesStub documentale;

    private String entetoken = null;
    static private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BusinessLogic.class.getName());
    public BusinessLogic() throws IOException, XMLStreamException, JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        // CONFIGURATIONS.setConfigurationProperties("configuration.properties","config.path");
        documentale = ClientManager.INSTANCE.getDocerServicesClient();
    }

    public String login(String userId, String password, String codiceEnte) throws RegistrazioneException {

        entetoken = codiceEnte;

        try {
            return baseLogin(userId, password, codiceEnte);
        } catch (BaseServiceException e) {
            throw new RegistrazioneException(e);
        }
    }

    @Override
    public String loginSSO(String saml, String codiceEnte) throws RegistrazioneException {
        entetoken = codiceEnte;

        try {
            return baseLoginSSO(saml, codiceEnte);
        } catch (BaseServiceException e) {
            throw new RegistrazioneException(e);
        }
    }


    public void logout(String token) throws RegistrazioneException {

        provider.logout(token);
    }

    public String registra(String ticket, long documentId, String registroId, String datiRegistrazione) throws RegistrazioneException {

        logger.debug("init method registra registroId"+registroId + "documentId:"+documentId);

        try {
            entetoken = Utils.extractTokenKey(ticket, "ente");
        } catch (KeyException e) {
            e.printStackTrace();
            throw new RegistrazioneException(e.getMessage());
        }


        String userId;
        // String library;
        String providerData;
        String docTicket = "calltype:internal|";

        String ente = "";
        String aoo = "";

        XMLUtil dom = null;

        DocerServicesStub.KeyValuePair[] metadatiDOC;

        try {
            dom = new XMLUtil(datiRegistrazione);

            docTicket += Utils.extractTokenKey(ticket, "documentale");

            // getProfileDocument del principale
            metadatiDOC = Utils.getProfile(docTicket, String.valueOf(documentId));

            String archiveType = null;

            // TipoComponente -> ALLEGATO, ANNESSO, ANNOTAZIONE
            for (DocerServicesStub.KeyValuePair pair : metadatiDOC) {
                if (pair.getKey().equals("COD_ENTE"))
                    ente = pair.getValue();

                if (pair.getKey().equals("COD_AOO"))
                    aoo = pair.getValue();

                if (pair.getKey().equals("ARCHIVE_TYPE")) {
                    archiveType = pair.getValue();
                }
            }

//            if (StringUtils.isEmpty(archiveType)) {
//                throw new Exception("Metadato ARCHIVE_TYPE mancante");
//            }

            if (!Utils.checkArchiveTypeConsistency(docTicket, String.valueOf(documentId), archiveType)) {
                throw new Exception("I documenti correlati devono avere la stessa natura del documento principale");
            }

        } catch (Exception e) {
            throw new RegistrazioneException(e);
        }

        try {
            // spostato il controllo XSD per switchare il file
            // validazione del formato
            String xsdFileName = getProviderXsdAttribute(ente, aoo, "xsd"); // "/input-validation.xsd"
            File schema = ConfigurationUtils.loadResourceFile(xsdFileName);
            dom.validate(schema.getAbsolutePath());
        } catch (Exception e) {
            throw new RegistrazioneException(e);
        }

        String keyProvider = "Provider_" + ente + "_" + aoo + "_" + registroId;

        ILoggedUserInfo currentUser = new LoggedUserInfo();

        try {
            userId = Utils.extractTokenKey(ticket, "uid");
            // library = Utils.extractTokenKey(ticket, "lib");
            providerData = Utils.extractTokenKey(ticket, keyProvider);

            // token = Utils.extractTokenKey(ticket, "token");
            currentUser.setCodiceEnte(ente);
            currentUser.setTicket(providerData);
            currentUser.setUserId(userId);

            // switch rovider per registro
            this.provider = getProvider(ente, aoo, registroId);

            this.provider.setCurrentUser(currentUser);
        } catch (Exception e) {
            throw new RegistrazioneException("Ticket non valido o provider per ENTE: " + ente + " AOO: " + aoo + " Registro particolare: " + registroId + " non disponibile. " + e.getMessage());
        }

        String firma = "";
        String NumeroRegistrazione = "";
        String DataRegistrazione = "";
        String oggettoRegistrazione = "";

        try {
            firma = dom.getNodeValue("//Flusso/Firma");
            // NumeroRegistrazione =
            // dom.getNodeValue("//Identificatore/NumeroRegistrazione");
            // DataRegistrazione =
            // dom.getNodeValue("//Identificatore/DataRegistrazione");
            // oggettoRegistrazione =
            // dom.getNodeValue("//Intestazione/Oggetto");

            // ********************************************************************************************
            // Controlli preventivi su parametri XML di input
            // ********************************************************************************************

            if (!firma.equalsIgnoreCase("FD") && !firma.equalsIgnoreCase("FE") && !firma.equalsIgnoreCase("F") && !firma.equalsIgnoreCase("NF")) {
                throw new RegistrazioneException("Valore 'Firma' non valido.");
            }

            // ********************************************************************************************
            checkUserRights(documentId, docTicket, userId, EnumACLRights.normalAccess);

            // gestione asincrona (inoltro firma)
            if (firma.equalsIgnoreCase("F")) {
                logger.debug("method registra firma"+firma);
                OMElement omeInoltroFirmaSistema = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode("//inoltroFirma");
                String inoltroFirma="";
                if (omeInoltroFirmaSistema!=null)
                    inoltroFirma = omeInoltroFirmaSistema.getText();

                if (!"true".equalsIgnoreCase(inoltroFirma)) {
                    //avvia il woekflow di firma
                    logger.debug("method registra viene invocato il workflow di firma");
                    Utils.runWorkflowSignProcess(docTicket, Utils.enumSignProcessAction.REGISTRARE, registroId, userId, userId, datiRegistrazione, metadatiDOC);
                    //*******************************************************************************

                    //throw new RegistrazioneException("Il sistema verticale di registro non supporta l'inoltro alla Firma.");
                    OMFactory factory = OMAbstractFactory.getOMFactory();
                    OMElement docItem = factory.createOMElement("esito", null);

                    OMElement metaItem = factory.createOMElement("codice", null);
                    metaItem.setText("1"); // warning
                    docItem.addChild(metaItem);

                    metaItem = factory.createOMElement("descrizione", null);

                    metaItem.setText("");

                    docItem.addChild(metaItem);

                    return docItem.toString(); // ritorna esito warning
                }

                logger.debug("method registra non viene invocato il workflow di firma");

            }


            String xmlRet = "";
            String fsecStr = "";

            // ***Recupero metadati e file dei documenti (principale, allegati ,
            // annessi e annotazioni)
            OMElement nodeToAdd = getExtendedNode(docTicket, String.valueOf(documentId));
            // OMElement root = dom.getNode("/Segnatura");
            // root.addChild(nodeToAdd);

            dom.addNode("/Segnatura", nodeToAdd);

            String datiRegistrazioneFull = dom.toXML();
            logger.debug("method registra chiamo il provider con registroId:"+registroId);
            logger.debug("method registra chiamo il provider con datiRegistrazione:"+datiRegistrazioneFull.toString());
            xmlRet = this.provider.registra(registroId, datiRegistrazioneFull);

            // controllare solo esito (se OK formatta output e return
            XMLUtil util = new XMLUtil(xmlRet);

            String esito = util.getNodeValue("/esito/codice");
            String errDescription = util.getNodeValue("/esito/descrizione");
            logger.debug("method registra esito provider:"+esito);
            logger.debug("method registra esito provider errDescription:"+errDescription);
            if (!esito.equalsIgnoreCase("0"))
                throw new RegistrazioneException(errDescription);

            if (firma.equalsIgnoreCase("F")) {
                // // Setta l'esito a warning
                util.setNodeValue("/esito/codice", "1");

                return util.toXML(); // ***ritorno esito warning
            }


            // gestion sincrona
            DataRegistrazione = util.getNodeValue("/esito/dati_registro/DataRegistrazione");
            NumeroRegistrazione = util.getNodeValue("/esito/dati_registro/NumeroRegistrazione");
            oggettoRegistrazione = util.getNodeValue("/esito/dati_registro/OggettoRegistrazione");

            DocerServicesStub.RegistraDocumento protDoc = new DocerServicesStub.RegistraDocumento();

            DocerServicesStub.KeyValuePair kvp = new DocerServicesStub.KeyValuePair();
            kvp.setKey("D_REGISTRAZ");

            kvp.setValue(DataRegistrazione);
            protDoc.addMetadata(kvp);

            kvp = new DocerServicesStub.KeyValuePair();
            kvp.setKey("N_REGISTRAZ");
            kvp.setValue(NumeroRegistrazione);
            protDoc.addMetadata(kvp);

            kvp = new DocerServicesStub.KeyValuePair();
            kvp.setKey("O_REGISTRAZ");
            kvp.setValue(oggettoRegistrazione);
            protDoc.addMetadata(kvp);

            kvp = new DocerServicesStub.KeyValuePair();
            kvp.setKey("ID_REGISTRO");
            kvp.setValue(registroId);

            protDoc.addMetadata(kvp);

            String errorMessage = "";
            String esitoOut = "0";

            // MITTENTI
            OMElement mitNode = dom.getNode("//Mittenti");
            if (mitNode != null) {
                kvp = new DocerServicesStub.KeyValuePair();

                kvp.setKey("MITTENTI");
                kvp.setValue(mitNode.toString());
                protDoc.addMetadata(kvp);
            }

            // DESTINATARI
            OMElement desNode = dom.getNode("//Destinatari");
            if (desNode != null) {
                kvp = new DocerServicesStub.KeyValuePair();

                kvp.setKey("DESTINATARI");
                kvp.setValue(desNode.toString());
                protDoc.addMetadata(kvp);
            }

            // TIPO_FIRMA
            OMElement tipoFirmaNode = dom.getNode("//Flusso/Firma");
            if (tipoFirmaNode != null) {
                kvp = new DocerServicesStub.KeyValuePair();

                kvp.setKey("TIPO_FIRMA");
                kvp.setValue(tipoFirmaNode.getText());
                protDoc.addMetadata(kvp);
            }

            // FIRMATARIO
            OMElement tipoFirmatarioNode = dom.getNode("//Flusso/Firmatario");
            if (tipoFirmatarioNode != null) {
                kvp = new DocerServicesStub.KeyValuePair();

                kvp.setKey("FIRMATARIO");
                kvp.setValue(tipoFirmatarioNode.toString());
                protDoc.addMetadata(kvp);
            }

            try {
                // Aggiornamento dei metadati di protocollazione sul documentale
                protDoc.setDocId(String.valueOf(documentId));
                // docTicket = "calltype:internal|" + docTicket;
                protDoc.setToken(docTicket);
                logger.debug("method registra chiamo documentale registraDocumento");

                documentale.registraDocumento(protDoc);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RegistrazioneException("Impossibile aggiornare il documento con i dati di registrazione particolare. " + e.getMessage());

                // errorMessage =
                // "Impossibile aggiornare il documento con i dati di fascicolo. "
                // + e.getMessage();
                // esitoOut="2";
            }

            OMFactory factory = OMAbstractFactory.getOMFactory();
            OMElement docItem = factory.createOMElement("esito", null);

            OMElement metaItem = factory.createOMElement("codice", null);
            metaItem.setText(esitoOut);
            docItem.addChild(metaItem);

            metaItem = factory.createOMElement("descrizione", null);

            metaItem.setText(errorMessage);

            docItem.addChild(metaItem);

            metaItem = factory.createOMElement("dati_registro", null);
            docItem.addChild(metaItem);

            OMElement item = factory.createOMElement("NumeroRegistrazione", null);
            item.setText(NumeroRegistrazione);
            metaItem.addChild(item);

            item = factory.createOMElement("DataRegistrazione", null);
            item.setText(DataRegistrazione);
            metaItem.addChild(item);

            item = factory.createOMElement("OggettoRegistrazione", null);
            item.setText(oggettoRegistrazione);
            metaItem.addChild(item);

            item = factory.createOMElement("IDRegistro", null);
            item.setText(registroId);
            metaItem.addChild(item);
            logger.debug("method registra end con successo");
            return docItem.toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RegistrazioneException(e);
        }

    }

    public boolean writeConfig(String token, String xml) throws RegistrazioneException {

        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            e.printStackTrace();
            throw new RegistrazioneException(e.getMessage());
        }

        try {
            CONFIGURATIONS.writeConfig(entetoken, xml);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RegistrazioneException(e.getMessage());
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RegistrazioneException(e.getMessage());
        }

        return true;

    }

    public String readConfig(String token) throws RegistrazioneException {

        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            e.printStackTrace();
            throw new RegistrazioneException(e.getMessage());
        }

        try {
            return CONFIGURATIONS.readConfig(entetoken);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RegistrazioneException(e.getMessage());
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RegistrazioneException(e.getMessage());
        }

    }


    private IProvider getProvider(String ente, String aoo, String registroId) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException, RegistrazioneException {

        String providerClassName = "";

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s' and @registro='%s']", ente, aoo, registroId);
        try {
            providerClassName = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode(xpathProvider).getText();
        } catch (Exception e) {
            throw new RegistrazioneException("Impossibile trovare il provider per ENTE: " + ente + " e AOO: " + aoo + " Registro particolare: " + registroId + " nel file di configurazione. "
                    + e.getMessage());
        }

        IProvider prov = (IProvider) Class.forName(providerClassName).newInstance();

        return prov;
    }

    private String getProviderXsdAttribute(String ente, String aoo, String attributeName) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            RegistrazioneException, DocerException {

        String providerAttribute = "";
        String defaultXsdFile = "";

        OMElement ome1;
        try {
            ome1 = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode("//section[@name='Providers']");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RegistrazioneException(e.getMessage());
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RegistrazioneException(e.getMessage());
        }
        if (ome1 == null) {
            throw new RegistrazioneException("xpath //section[@name='Providers'] non trovato nel file di configurazione.");
        }

        defaultXsdFile = ome1.getAttributeValue(new QName("default-xsd"));
        if (defaultXsdFile == null) {
            throw new RegistrazioneException("Impossibile trovare l'attributo 'default-xsd' del nodo 'Providers' nel file di configurazione.");
        }

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);

        OMElement ome2;
        try {
            ome2 = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode(xpathProvider);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RegistrazioneException(e.getMessage());
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RegistrazioneException(e.getMessage());
        }
        if (ome2 == null) {
            throw new RegistrazioneException("provider non definito per ente=" + ente + " e aoo=" + aoo);
        }

        providerAttribute = ome2.getAttributeValue(new QName(attributeName));
        if (providerAttribute == null) {
            return defaultXsdFile;
        }

        return providerAttribute;
    }

    private OMElement getExtendedNode(String token, String documentoId) throws RemoteException, RegistrazioneException, UnsupportedEncodingException, DocerExceptionException {

        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMElement extItem = factory.createOMElement("Documenti", null);

        // Blocco documento principale
        OMElement docItem = getDocumentBlock(token, documentoId, documentoId);
        extItem.addChild(docItem);

        // getRelated -> lista docId
        DocerServicesStub.GetRelatedDocuments related = new DocerServicesStub.GetRelatedDocuments();
        related.setDocId(documentoId);
        related.setToken(token);
        DocerServicesStub.GetRelatedDocumentsResponse relResp = documentale.getRelatedDocuments(related);
        String[] rels = relResp.get_return();

        OMElement allegatiItem = factory.createOMElement("Allegati", null);
        OMElement annessiItem = factory.createOMElement("Annessi", null);
        OMElement annotazioniItem = factory.createOMElement("Annotazioni", null);

        if (rels != null) {
            // altri blocchi: allegati,annessi,annotazioni
            for (String rel : rels) {
                docItem = getDocumentBlock(token, rel, documentoId);
                // ******switch su tipocomponente (allegati,annessi,annotazioni)
                String tipoComponente = extractTipoComponente(docItem);

                if (tipoComponente.equalsIgnoreCase("ALLEGATO") || tipoComponente.equalsIgnoreCase(""))
                    allegatiItem.addChild(docItem);

                if (tipoComponente.equalsIgnoreCase("ANNESSO"))
                    annessiItem.addChild(docItem);

                if (tipoComponente.equalsIgnoreCase("ANNOTAZIONE"))
                    annotazioniItem.addChild(docItem);
            }
        }

        extItem.addChild(allegatiItem);
        extItem.addChild(annessiItem);
        extItem.addChild(annotazioniItem);

        return extItem;
    }

    private String extractTipoComponente(OMElement documentBlock) throws RegistrazioneException {
        try {
            String xpath = "//Metadati/Parametro[@nome='TIPO_COMPONENTE']";

            AXIOMXPath path = new AXIOMXPath(xpath);
            OMElement node = (OMElement) path.selectSingleNode(documentBlock);

            OMAttribute a = node.getAttribute(new QName("valore"));

            return a.getAttributeValue();

        } catch (JaxenException ex) {
            org.slf4j.LoggerFactory.getLogger(BusinessLogic.class.getName()).error(ex.getMessage(), ex);
            throw new RegistrazioneException(ex);
        }
    }

    private OMElement getDocumentBlock(String token, String documentoId, String docNumPrincipale) throws RemoteException, RegistrazioneException, UnsupportedEncodingException, DocerExceptionException {
        // getProfileDocument del principale
        OMFactory factory = OMAbstractFactory.getOMFactory();
        DocerServicesStub.GetProfileDocument doc = new DocerServicesStub.GetProfileDocument();

        doc.setDocId(documentoId);
        doc.setToken(token);
        DocerServicesStub.GetProfileDocumentResponse resp = documentale.getProfileDocument(doc);
        DocerServicesStub.KeyValuePair[] metadati = resp.get_return();

        // TipoComponente -> ALLEGATO, ANNESSO, ANNOTAZIONE

        OMElement docItem = factory.createOMElement("Documento", null);
        OMElement metaItem = factory.createOMElement("Metadati", null);
        docItem.addChild(metaItem);

        String docEpr = ClientManager.INSTANCE.getDocerServicesEpr();
        String docNum = "";
        String docName = "";
        String docDefExt = "";

        String cod_ente = "";

        String tipo_componente = "";
        String stato_archivistico = "";
        boolean pubblicato = false;
        boolean protocollato = false;


        String tipo_protocollazione = "";

        for (DocerServicesStub.KeyValuePair meta : metadati) {
            OMElement parItem;

            if (meta.getKey().equalsIgnoreCase("cod_ente")) {
                cod_ente = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("stato_archivistico")) {
                stato_archivistico = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("pubblicato")) {
                pubblicato = Boolean.valueOf(meta.getValue());
            }

            if (meta.getKey().equalsIgnoreCase("tipo_componente")) {
                tipo_componente = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("num_pg")) {
                if (!meta.getValue().equalsIgnoreCase("")) {
                    protocollato = true;
                }
            }

            if (meta.getKey().equalsIgnoreCase("docname")) {
                docName = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("default_extension")) {
                docDefExt = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("docnum")) {
                docNum = meta.getValue();

                docItem.addAttribute("id", meta.getValue(), null);
                continue;
            }

            if (meta.getKey().equalsIgnoreCase("tipo_protocollazione")) {
                tipo_protocollazione = meta.getValue();
                if (tipo_protocollazione == null) {
                    tipo_protocollazione = "";
                }
            }

            parItem = factory.createOMElement("Parametro", null);
            parItem.addAttribute("nome", meta.getKey(), null);
            String value = meta.getValue();
            if (value == null)
                value = "";
            parItem.addAttribute("valore", value, null);

            metaItem.addChild(parItem);
        }

        // Controllo su documento mastertipoComponente ammesso: PRINCIPALE o
        // VUOTO
        if (docNumPrincipale.equalsIgnoreCase(documentoId)) {
            // controllare stato_archivistico: ARCHIVIATO (6) oppure pubblicato
            if (stato_archivistico.equalsIgnoreCase("6") || pubblicato) {
                throw new RegistrazioneException("Non e' permesso registrare documenti archiviati o pubblicati");
            }

            if (!tipo_componente.equalsIgnoreCase("PRINCIPALE") && !tipo_componente.equalsIgnoreCase(""))
                throw new RegistrazioneException("Non è permesso registrare allegati, annessi o annotazioni.");

            if (protocollato && !tipo_protocollazione.equals("E")) {
                throw new RegistrazioneException("Non è permesso registrare documenti protocollati con TIPO_PROTOCOLLAZIONE=" + tipo_protocollazione);
            }
        }

        String docFileName = String.format("%s.%s", docName, docDefExt);

        // costruire l'url per il donwload del documento
        String baseUrl = "";
        try {
            baseUrl = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode("//xformsServiceURL").getText();
        } catch (Exception e) {
            throw new RegistrazioneException("Parametro: xformsServiceURL non trovato nel file di configurazione.");
        }
        String docUri = String.format("%s?action=download&docnum=%s&filename=%s&token=%s", baseUrl, docNum, docFileName, token);

        docItem.addAttribute("uri", docUri, null);

        // Blocco acl
        OMElement aclItem = getAclBlock(token, documentoId);
        docItem.addChild(aclItem);
    /*
     * OMElement item = null;
	 * 
	 * //Se non e' il documento principale allora controlla il
	 * tipocomponente ed e' ammesso anche vuoto //come allegato if
	 * (!docNumPrincipale.equalsIgnoreCase(documentoId)){ if
	 * (tipoComponente.equalsIgnoreCase("ALLEGATO") ||
	 * tipoComponente.equalsIgnoreCase("")) item =
	 * factory.createOMElement("Allegati", null); }
	 * 
	 * if (tipoComponente.equalsIgnoreCase("ANNESSO")) item =
	 * factory.createOMElement("Annessi", null);
	 * 
	 * if (tipoComponente.equalsIgnoreCase("ANNOTAZIONE")) item =
	 * factory.createOMElement("Annotazioni", null);
	 * 
	 * if (item!=null){ item.addChild(docItem); return item; }
	 */

        return docItem;
    }

    private OMElement getAclBlock(String token, String documentoId) throws RemoteException, DocerExceptionException {

        OMFactory factory = OMAbstractFactory.getOMFactory();

        // ACL
        DocerServicesStub.GetACLDocument aclDoc = new DocerServicesStub.GetACLDocument();
        aclDoc.setDocId(documentoId);
        aclDoc.setToken(token);
        DocerServicesStub.GetACLDocumentResponse aclResp = documentale.getACLDocument(aclDoc);
        DocerServicesStub.KeyValuePair[] acls = aclResp.get_return();

        OMElement aclItem = factory.createOMElement("Acl", null);

        if (acls != null) {
            for (DocerServicesStub.KeyValuePair acl : acls) {
                OMElement parItem;

                parItem = factory.createOMElement("Parametro", null);
                parItem.addAttribute("attore", acl.getKey(), null);
                parItem.addAttribute("valore", acl.getValue(), null);

                aclItem.addChild(parItem);
            }
        }

        return aclItem;
    }

    private void checkUserRights(long docId, String ticketDocumentale, String userId, EnumACLRights minimumRight) throws RegistrazioneException, RemoteException, DocerExceptionException {

        GetUserRights getUserRights = new GetUserRights();
        getUserRights.setDocId(String.valueOf(docId));
        getUserRights.setToken(ticketDocumentale);
        getUserRights.setUserId(userId);
        GetUserRightsResponse getUserRightsResponse = documentale.getUserRights(getUserRights);

        int userRight = -1;

        if (getUserRightsResponse != null && getUserRightsResponse.get_return() > -1) {
            userRight = getUserRightsResponse.get_return();
        }

        // 0 fullAccess
        // 1 normalAccess
        // 2 readOnly

        if (userRight < 0) {
            throw new RegistrazioneException("documento " + docId + " non trovato nel documentale");
        }

        // la verifica va fatta al contrario...
        if (userRight > minimumRight.getCode()) {
            throw new RegistrazioneException("l'utente " + userId + " non ha diritti sufficienti sul documento " + docId + " nel documentale");
        }

    }

	public KeyValuePair[] getRegistri(String token, String ente, String aoo ) throws RegistrazioneException{

		List<KeyValuePair> listReg = new ArrayList<KeyValuePair>();
		String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);
		try {
			entetoken = Utils.extractTokenKey(token, "ente");
		}
		catch (KeyException e) {
			e.printStackTrace();
			throw new RegistrazioneException(e.getMessage());
		}
		List<OMElement> ome2;
		try {
			ome2 = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNodes(xpathProvider);

			if(ome2!=null) {
				for (OMElement omelem : ome2) {
					String descrizione ="";
					String registro="";
					Iterator it = omelem.getAllAttributes();
					for(;it.hasNext();){
						OMAttribute att = (OMAttribute)it.next();
						if(att.getLocalName().equals("descrizione")){
							descrizione=att.getAttributeValue();
						}else if(att.getLocalName().equals("registro")) {
							registro = att.getAttributeValue();
						}
					}
					KeyValuePair kvp = new KeyValuePair();
					kvp.setKey(registro);
					kvp.setValue(descrizione);
					listReg.add(kvp);
				}
			}

		}
		catch (IOException e) {

			e.printStackTrace();
			throw new RegistrazioneException(e.getMessage());
		}
		catch (XMLStreamException e) {

			e.printStackTrace();
			throw new RegistrazioneException(e.getMessage());
		}catch (JaxenException e1) {

			e1.printStackTrace();
			throw new RegistrazioneException("Configurazione malformata");
		}

		KeyValuePair [] returnKVP  = new KeyValuePair[listReg.size()];

		int i = 0;
		for(KeyValuePair temp :listReg){
			returnKVP[i] = temp;
			i++;
		}

		return returnKVP;
	}

}
