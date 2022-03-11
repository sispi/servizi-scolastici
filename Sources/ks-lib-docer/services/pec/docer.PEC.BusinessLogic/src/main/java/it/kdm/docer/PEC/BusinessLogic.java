/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.PEC;

import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.DocerExceptionException;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.XMLUtil;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.commons.docerservice.BaseService;
import it.kdm.docer.commons.docerservice.BaseServiceException;
import it.kdm.docer.sdk.classes.LoggedUserInfo;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import it.kdm.utils.ResourceLoader;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.slf4j.Logger;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.KeyException;

/**
 * @author lorenzo
 */
public class BusinessLogic extends BaseService implements IBusinessLogic {

    Logger logger = org.slf4j.LoggerFactory.getLogger(BusinessLogic.class);

    private static Configurations CONFIGURATIONS = new Configurations();

    private DocerServicesStub documentale;
    private IProvider provider;

    public BusinessLogic() throws IOException, XMLStreamException, JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        documentale = ClientManager.INSTANCE.getDocerServicesClient();

    }

    public String login(String userId, String password, String codiceEnte) throws PECException {

        try {
            return baseLogin(userId, password, codiceEnte);
        } catch (BaseServiceException e) {
            throw new PECException(e);
        }

    }

    @Override
    public String loginSSO(String saml, String codiceEnte) throws PECException {
        try {
            return baseLoginSSO(saml, codiceEnte);
        } catch (BaseServiceException e) {
            throw new PECException(e);
        }
    }

    public void logout(String token) throws PECException {

        provider.logout(token);
    }

    public String invioPEC(String ticket, long documentoId, String datiPec) throws PECException {

        String userId;
        // String library;
        String providerData;
        String docTicket = "calltype:internal|";

        String ente = "";
        String aoo = "";
        OMElement classificaNode = null;

        XMLUtil dom = null;

        DocerServicesStub.KeyValuePair[] metadatiDOC;

        try {
            dom = new XMLUtil(datiPec);

            docTicket += Utils.extractTokenKey(ticket, "documentale");

            // getProfileDocument del principale
            DocerServicesStub.GetProfileDocument doc = new DocerServicesStub.GetProfileDocument();

            doc.setDocId(String.valueOf(documentoId));
            doc.setToken(docTicket);
            DocerServicesStub.GetProfileDocumentResponse resp = documentale.getProfileDocument(doc);
            metadatiDOC = resp.get_return();

            // TipoComponente -> ALLEGATO, ANNESSO, ANNOTAZIONE

            for (DocerServicesStub.KeyValuePair pair : metadatiDOC) {
                if (pair.getKey().equals("COD_ENTE"))
                    ente = pair.getValue();

                if (pair.getKey().equals("COD_AOO"))
                    aoo = pair.getValue();

            }

        } catch (Exception e) {
            throw new PECException(e);
        }

        try {
            // spostato il controllo XSD per switchare il file
            // validazione del formato
            String xsdFileName = getProviderXsdAttribute(ente, aoo, "xsd"); // "/input-validation.xsd"
            
            File schema = ConfigurationUtils.loadResourceFile(xsdFileName);
            dom.validate(schema.getAbsolutePath());
        } catch (Exception e) {
            throw new PECException(e);
        }

        String keyProvider = "Provider_" + ente + "_" + aoo;

        ILoggedUserInfo currentUser = new LoggedUserInfo();

        try {
            userId = Utils.extractTokenKey(ticket, "uid");
            providerData = Utils.extractTokenKey(ticket, keyProvider);
            currentUser.setCodiceEnte(ente);
            currentUser.setTicket(providerData);
            currentUser.setUserId(userId);

            this.provider = getProvider(ente, aoo);
            this.provider.setCurrentUser(currentUser);
        } catch (Exception e) {
            throw new PECException("Ticket non valido o provider per ENTE: " + ente + " AOO: " + aoo + " non disponibile. " + e.getMessage());
        }

        try {

            // ***Recupero metadati e file dei documenti (principale, allegati ,
            // annessi e annotazioni)
            OMElement nodeToAdd = getExtendedNode(docTicket, String.valueOf(documentoId));
            // OMElement root = dom.getNode("/Segnatura");
            // root.addChild(nodeToAdd);

            dom.addNode("/Segnatura", nodeToAdd);

            String datiPecFull = dom.toXML();

            return provider.invioPEC(documentoId, datiPecFull);

        } catch (Exception e) {
            throw new PECException(e);
        }

    }

    public boolean writeConfig(String token, String xml) throws PECException {

        String entetoken;
        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            logger.error(e.getMessage(), e);
            throw new PECException(e.getMessage());
        }

        try {
            CONFIGURATIONS.writeConfig(entetoken, xml);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new PECException("Configurazione malformata");
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new PECException("Configurazione malformata");
        }

        return true;

    }

    public String readConfig(String token) throws PECException {

        String entetoken;
        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            logger.error(e.getMessage(), e);
            throw new PECException(e.getMessage());
        }

        try {
            return CONFIGURATIONS.readConfig(entetoken);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new PECException(e.getMessage());
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new PECException(e.getMessage());
        }

    }

    private IProvider getProvider(String ente, String aoo) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException, PECException {

        String providerClassName = "";

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);
        try {
            providerClassName = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(xpathProvider).getText();
        } catch (Exception e) {
            throw new PECException("Impossibile trovare il provider per ENTE: " + ente + " e AOO: " + aoo + " nel file di configurazione. " + e.getMessage());
        }

        IProvider prov = (IProvider) Class.forName(providerClassName).newInstance();

        return prov;
    }

    private String getProviderXsdAttribute(String ente, String aoo, String attributeName) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException, PECException {

        String providerAttribute = "";
        String defaultXsdFile = "";

        OMElement ome1;
        try {
            ome1 = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode("//section[@name='Providers']");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new PECException(e.getMessage());
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
            throw new PECException(e.getMessage());
        }
        if (ome1 == null) {
            throw new PECException("xpath //section[@name='Providers'] non trovato nel file di configurazione.");
        }

        defaultXsdFile = ome1.getAttributeValue(new QName("default-xsd"));
        if (defaultXsdFile == null) {
            throw new PECException("Impossibile trovare l'attributo 'default-xsd' del nodo 'Providers' nel file di configurazione.");
        }

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);

        OMElement ome2;
        try {
            ome2 = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(xpathProvider);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new PECException(e.getMessage());
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
            throw new PECException(e.getMessage());
        }
        if (ome2 == null) {
            throw new PECException("provider non definito per ente=" + ente + " e aoo=" + aoo);
        }

        providerAttribute = ome2.getAttributeValue(new QName(attributeName));
        if (providerAttribute == null) {
            return defaultXsdFile;
        }

        return providerAttribute;
    }

    private OMElement getDocumentBlock(String token, String documentoId, String docNumPrincipale) throws RemoteException, PECException, UnsupportedEncodingException, DocerExceptionException {
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
        String tipoComponente = "";

        for (DocerServicesStub.KeyValuePair meta : metadati) {
            OMElement parItem;

            if (meta.getKey().equalsIgnoreCase("docname")) {
                docName = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("cod_ente")) {
                cod_ente = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("default_extension")) {
                docDefExt = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("docnum")) {
                docNum = meta.getValue();

                docItem.addAttribute("id", meta.getValue(), null);
                continue;
            }

            if (meta.getKey().equalsIgnoreCase("tipocomponente")) {
                tipoComponente = meta.getValue();

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
            if (!tipoComponente.equalsIgnoreCase("PRINCIPALE") && !tipoComponente.equalsIgnoreCase(""))
                throw new PECException("Non e' permesso inviare a PEC direttamente allegati, annessi o annotazioni.");
        }

        String docFileName = String.format("%s.%s", docName, docDefExt);
        String baseUrl = "";
        try {
            baseUrl = CONFIGURATIONS.getConfiguration(cod_ente).getConfig().getNode("//xformsServiceURL").getText();
        } catch (Exception e) {
            throw new PECException("Parametro: xformsServiceURL non trovato nel file di configurazione.");
        }
        // costruire l'url per il donwload del documento
        // String encodedtoken = URLEncoder.encode(token,"UTF-8");
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

    private OMElement getExtendedNode(String token, String documentoId) throws RemoteException, PECException, UnsupportedEncodingException, DocerExceptionException {

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

    private String extractTipoComponente(OMElement documentBlock) throws PECException {
        try {
            String xpath = "//Metadati/Parametro[@nome='TIPO_COMPONENTE']";

            AXIOMXPath path = new AXIOMXPath(xpath);
            OMElement node = (OMElement) path.selectSingleNode(documentBlock);

            OMAttribute a = node.getAttribute(new QName("valore"));

            return a.getAttributeValue();

        } catch (JaxenException ex) {
            logger.error(ex.getMessage(), ex);
            throw new PECException(ex);
        }
    }

}
