package it.kdm.docer.webservices;

import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.classes.*;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.ISearchItem;
import it.kdm.docer.webservices.utility.TemporaryFileSource;
import it.kdm.docer.webservices.utility.WSTransformer;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.lang.StringUtils;
import org.jaxen.JaxenException;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.security.KeyException;
import java.util.*;

public class DocerServices {

    private static TicketCipher cipher = new TicketCipher();

    private static String providerName = "";
    private static int PRIMARYSEARCH_MAX_RESULTS = 5000;
    private static long DISK_BUFFER_THRESHOLD = 0;
    private static String DISK_BUFFER_DIRECTORY = "";
    BusinessLogic bl = null;
    private static Logger log = org.slf4j.LoggerFactory.getLogger(DocerServices.class);

    private static Properties p = null;

    public DocerServices() throws DocerException { // throws DocerException {

        if (p == null || p.size() == -1) {

            
            try {
                p = ConfigurationUtils.loadProperties("docer_config.xml");
           
            } catch (IOException e) {
                log.error("500 - read config.xml: IOException: " + e.getMessage());
                throw new DocerException("500 - read config.xml: IOException: " + e.getMessage());
            }

            // appena viene invocato il WS recupero l'informazione del
            // 'provider'
            // dal file 'config.xml'
            providerName = p.getProperty("provider");

            try {
                DISK_BUFFER_THRESHOLD = Long.parseLong(p.getProperty("DISK_BUFFER_THRESHOLD"));
            } catch (Exception provEx) {
                log.error("500 - DISK_BUFFER_THRESHOLD config key wrong or missing: " + provEx.getMessage());
                throw new DocerException("500 - DISK_BUFFER_THRESHOLD config key wrong or missing: " + provEx.getMessage());
            }

            try {
                DISK_BUFFER_DIRECTORY = p.getProperty("DISK_BUFFER_DIRECTORY");
            } catch (Exception provEx) {
                log.error("500 - DISK_BUFFER_DIRECTORY config key wrong or missing: " + provEx.getMessage());
                throw new DocerException("500 - DISK_BUFFER_DIRECTORY config key wrong or missing: " + provEx.getMessage());
            }

            try {
                PRIMARYSEARCH_MAX_RESULTS = Integer.parseInt(p.getProperty("PRIMARYSEARCH_MAX_RESULTS"));
            } catch (Exception provEx) {
                log.error("500 - PRIMARYSEARCH_MAX_RESULTS config key missing: " + provEx.getMessage());
                throw new DocerException("500 - PRIMARYSEARCH_MAX_RESULTS config key missing: " + provEx.getMessage());
            }

            try {
                BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
            } catch (Exception provEx) {
                log.error("500 - CONFIG_DIR config key missing: " + provEx.getMessage());
                throw new DocerException("500 - CONFIG_DIR config key missing: " + provEx.getMessage());
            }

        }

        try {
            bl = new BusinessLogic(providerName, PRIMARYSEARCH_MAX_RESULTS);
        } catch (DocerException e) {
            log.error("500 - errore istanza Business Logic: " + e.getMessage());
            throw new DocerException("500 - errore istanza Business Logic: " + e.getMessage());
        }
    }

    public boolean writeConfig(String token, String xmlConfig) throws DocerException {
        try {
            bl.writeConfig(token, xmlConfig);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public String readConfig(String token) throws DocerException {

        try {
            return bl.readConfig(token);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public String createDocument(String token, KeyValuePair[] metadata, DataHandler file) throws DocerException {

        Map<String, String> metadataMap = WSTransformer.toMap1(metadata);

        try {
            String docId;
            if (file == null) {
                // Se ARCHIVE_TYPE=PAPER e non mi è stato passato il file, creo un file fittizio (DOCER-33 12/10/2015)
                if ("PAPER".equals(metadataMap.get("ARCHIVE_TYPE"))) {
                    // Deve essere passato anche il metadato NO_FILE = true per la creazione senza file
                    if ( !"true".equals(metadataMap.get("NO_FILE")) )
                        throw new DocerException("Per creare un documento senza file e' necessario impostare il metadato NO_FILE = true");

                    // Creo il file fittizio e rimuovo il flag
                    file = new DataHandler(new ByteArrayDataSource(new byte[]{0}));
//                    metadataMap.remove("NO_FILE");

                    docId = bl.createDocument(token, metadataMap, file.getInputStream());
                } else {
                    docId = bl.createDocument(token, metadataMap, null);
                }

            } else {
                docId = bl.createDocument(token, metadataMap, file.getInputStream());
            }

            addVersionXml(token, docId, "1");

            return docId;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        } catch (IOException e) {
            throw new DocerException("520 - " + e.getMessage());
        } catch (Exception e) {
            throw new DocerException("500 - " + e.getMessage());
        }
    }


    //permette di aggiungere o rimuovere acl ad un folder
    public KeyValuePair[] changeACLFolder(String token, String folderId, KeyValuePair aclToAdd[], String aclToRemove[]) throws DocerException {
        try {
            Map<String, EnumACLRights> aclsMap = null;
            aclsMap = bl.updateACLFolder(token, folderId, aclToAdd, aclToRemove);
            return WSTransformer.toArray2(aclsMap);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    //permette di aggiungere o rimuovere acl ad un document
    public KeyValuePair[] changeACLDocumento(String token, String docnum, KeyValuePair aclToAdd[], String aclToRemove[]) throws DocerException {
        try {
            Map<String, EnumACLRights> aclsMap = null;
            aclsMap = bl.updateACLDocumento(token, docnum, aclToAdd, aclToRemove);
            return WSTransformer.toArray2(aclsMap);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    //permette di aggiungere o rimuovere acl di un titolario
    public KeyValuePair[] changeACLTitolario(String token, KeyValuePair[] titolarioId, KeyValuePair[] aclToAdd, String aclToRemove[]) throws DocerException {
        try {
            Map<String, EnumACLRights> aclsMap = null;

            aclsMap = bl.updateACLTitolari(token, WSTransformer.toMap1(titolarioId), aclToAdd, aclToRemove);

            return WSTransformer.toArray2(aclsMap);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    //permette di aggiungere o rimuovere acl di un fascicolo
    public KeyValuePair[] changeACLFascicolo(String token, KeyValuePair[] fascicoloId, KeyValuePair[] aclToAdd, String aclToRemove[]) throws DocerException {
        try {
            Map<String, EnumACLRights> aclsMap = null;

            aclsMap = bl.updateACLFascicolo(token, WSTransformer.toMap1(fascicoloId), aclToAdd, aclToRemove);

            return WSTransformer.toArray2(aclsMap);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }


    public boolean setACLDocument(String token, String docId, KeyValuePair[] acls) throws DocerException {

        Map<String, EnumACLRights> aclsMap = WSTransformer.toMap2(acls);

        try {
            bl.setACLDocument(token, docId, aclsMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public KeyValuePair[] getACLDocument(String token, String docId) throws DocerException {

        try {
            Map<String, EnumACLRights> aclsMap = bl.getACLDocument(token, docId);
            return WSTransformer.toArray2(aclsMap);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public boolean updateProfileDocument(String token, String docId, KeyValuePair[] metadata) throws DocerException {
        try {
            Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
            bl.updateProfileDocument(token, docId, metadataMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public KeyValuePair[] getProfileDocument(String token, String docId) throws DocerException {
        try {
            Map<String, String> metadataMap = bl.getProfileDocument(token, docId);
            return WSTransformer.toArray1(metadataMap);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public String[] getVersions(String token, String docId) throws DocerException {

        try {
            List<String> versions = bl.getVersions(token, docId);
            return versions.toArray(new String[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public String addNewVersion(String token, String docId, DataHandler file) throws DocerException {

        try {
            String newVersion;

            if (file == null) {
                newVersion = bl.addNewVersion(token, docId, null);
            } else {
                newVersion = bl.addNewVersion(token, docId, file.getInputStream());
            }

            addVersionXml(token, docId, newVersion);

            return newVersion;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        } catch (IOException e) {
            throw new DocerException("520 - " + e.getMessage());
        } catch (KeyException e) {
            throw new DocerException(e);
        } catch (XMLStreamException e) {
            throw new DocerException(e);
        }

    }


    private void addVersionXml(String token, String docId, String newVersion) throws DocerException, XMLStreamException, KeyException {
        Map<String, String> profile = bl.getProfileDocument(token, docId);
        String xml = profile.get("VERSIONS");

        if (StringUtils.isEmpty(xml)) {
            xml = "<versions />";
        }

        OMElement doc = AXIOMUtil.stringToOM(xml);

        OMElement newChild = createNewElement("version", doc);

        String decryptedToken = cipher.decryptTicket(token);

        createNewElement("number", newVersion, newChild);
        createNewElement("date", ISODateTimeFormat.dateTime().print(new Date().getTime()), newChild);
        createNewElement("userId", Utils.extractTokenKey(decryptedToken, "uid"), newChild);
        createNewElement("filename", profile.get("DOCNAME"), newChild);

        HashMap<String, String> update = new HashMap<String, String>();
        update.put("VERSIONS", doc.toString());
        bl.updateProfileDocument(token, docId, update);
    }

    private OMElement createNewElement(String name, OMElement parent) {
        return createNewElement(name, null, parent);
    }

    private OMElement createNewElement(String name, String value, OMElement parent) {
        OMElement elem = OMAbstractFactory.getOMFactory().createOMElement(new QName(null, name));
        if (value != null) {
            elem.setText(value);
        }

        if (parent != null) {
            parent.addChild(elem);
        }

        return elem;
    }

    public boolean replaceLastVersion(String token, String docId, DataHandler file) throws DocerException {

        try {

            if (file == null) {
                bl.replaceLastVersion(token, docId, null);
            } else {
                bl.replaceLastVersion(token, docId, file.getInputStream());
            }

            Map<String, String> profile = bl.getProfileDocument(token, docId);
            String xml = profile.get("VERSIONS");

            if (StringUtils.isEmpty(xml)) {
                throw new DocerException("Metadato VERSIONS mancante");
            }

            OMElement doc = AXIOMUtil.stringToOM(xml);

            int maxVersion = -1;
            OMElement maxVersionElement = null;

            AXIOMXPath xpath = new AXIOMXPath("//version/number");
            List<?> versions = xpath.selectNodes(doc);
            for (Object obj : versions) {
                OMElement child = (OMElement) obj;
                try {
                    int version = Integer.parseInt(child.getText());
                    if (version > maxVersion) {
                        maxVersion = version;
                        maxVersionElement = (OMElement) child.getParent();
                    }
                } catch (NumberFormatException ex) {
                    throw new DocerException("Metadato VERSIONS non valido.");
                }
            }

            if (maxVersionElement == null) {
                throw new DocerException("Metadato VERSIONS vuoto.");
            }
            String decryptedToken = cipher.decryptTicket(token);

            updateVersionsElement(maxVersionElement, ISODateTimeFormat.dateTime().print(new Date().getTime()),
                    Utils.extractTokenKey(decryptedToken, "uid"),
                    profile.get("DOCNAME"));

            HashMap<String, String> update = new HashMap<String, String>();
            update.put("VERSIONS", doc.toString());
            bl.updateProfileDocument(token, docId, update);

            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        } catch (IOException e) {
            throw new DocerException("520 - " + e.getMessage());
        } catch (KeyException e) {
            throw new DocerException(e);
        } catch (JaxenException e) {
            throw new DocerException(e);
        } catch (XMLStreamException e) {
            throw new DocerException(e);
        }
    }

    private void updateVersionsElement(OMElement element, String date, String userId, String filename) {
        OMElement dateElem = element.getFirstChildWithName(new QName(null, "date"));
        dateElem.setText(date);

        OMElement userIdElem = element.getFirstChildWithName(new QName(null, "userId"));
        userIdElem.setText(userId);

        OMElement fileName = element.getFirstChildWithName(new QName(null, "filename"));
        fileName.setText(userId);
    }

    public boolean lockDocument(String token, String docId) throws DocerException {

        try {
            bl.lockDocument(token, docId);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public boolean unlockDocument(String token, String docId) throws DocerException {

        try {
            bl.unlockDocument(token, docId);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public LockStatus getLockStatus(String token, String docId) throws DocerException {

        try {
            LockStatus lockStatus = (LockStatus) bl.getLockStatus(token, docId);
            return lockStatus;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public StreamDescriptor downloadDocument(String token, String docId) throws DocerException {

        try {
            StreamDescriptor descriptor = new StreamDescriptor();

            File f = new File(DISK_BUFFER_DIRECTORY, UUID.randomUUID().toString());

            byte[] file = bl.downloadDocument(token, docId, f.getAbsolutePath(), DISK_BUFFER_THRESHOLD);
            if (file == null) {
                descriptor.setByteSize(f.length());
                TemporaryFileSource tfs = new TemporaryFileSource(f);
                descriptor.setHandler(new DataHandler(tfs));
                return descriptor;
            }
            descriptor.setByteSize(file.length);
            descriptor.setHandler(new DataHandler(new ByteArrayDataSource(file)));
            return descriptor;

        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public StreamDescriptor downloadVersion(String token, String docId, String versionNumber) throws DocerException {

        try {
            StreamDescriptor descriptor = new StreamDescriptor();
            File f = new File(DISK_BUFFER_DIRECTORY, UUID.randomUUID().toString());

            byte[] file = bl.downloadVersion(token, docId, versionNumber, f.getAbsolutePath(), DISK_BUFFER_THRESHOLD);
            if (file == null) {
                descriptor.setByteSize(f.length());
                TemporaryFileSource tfs = new TemporaryFileSource(f);
                descriptor.setHandler(new DataHandler(tfs));
                return descriptor;
            }
            descriptor.setByteSize(file.length);
            descriptor.setHandler(new DataHandler(new ByteArrayDataSource(file)));
            return descriptor;

        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public boolean addRelated(String token, String docId, String[] related) throws DocerException {

        try {
            if (related == null)
                return true;
            Set<String> toAddRelated = new HashSet<String>();
            for (String id : related) {
                toAddRelated.add(id);
            }
            bl.addRelated(token, docId, new ArrayList<String>(toAddRelated));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public boolean removeRelated(String token, String docId, String[] related) throws DocerException {

        try {
            if (related == null)
                return true;
            List<String> toRemoveRelated = new ArrayList<String>();
            for (String id : related) {
                if (toRemoveRelated.contains(id))
                    continue;
                toRemoveRelated.add(id);
            }
            bl.removeRelated(token, docId, toRemoveRelated);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public String[] getRelatedDocuments(String token, String docId) throws DocerException {

        try {
            return bl.getRelatedDocuments(token, docId).toArray(new String[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public String login(String userId, String password, String codiceEnte) throws DocerException {

        try {
            return bl.login(codiceEnte, userId, password);
        } catch (DocerException e) {

            log.error(e.getMessage() + " @ " + e.getStackTrace()[0].toString());

            throw new DocerException(e.getErrNumber() + " - " + "Autenticazione fallita.");
        }

    }

    public KeyValuePair[] loginSSO(String saml, String codiceEnte) throws DocerException {

        try {
            return bl.loginSSO(saml, codiceEnte);
        } catch (DocerException e) {

            log.error(e.getMessage() + " @ " + e.getStackTrace()[0].toString());

            throw new DocerException(e.getErrNumber() + " - " + "Autenticazione fallita.");
        }

    }

    public boolean logout(String token) throws DocerException {

        try {
            bl.logout(token);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public boolean deleteDocument(String token, String docId) throws DocerException {

        try {
            bl.deleteDocument(token, docId);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public int getUserRights(String token, String docId, String userId) throws DocerException {

        try {
            return bl.getUserRights(token, docId, userId).getCode();
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public HistoryItem[] getHistory(String token, String docId) throws DocerException {
        try {
            return bl.getHistory(token, docId).toArray(new HistoryItem[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public SearchItem[] searchDocumentiFascicolo(String token, KeyValuePair[] fascicoloId, int maxRows) throws DocerException {
        Map<String, String> fascicolo = bl.getFascicolo(token, WSTransformer.toMap1(fascicoloId));

        // DOCER-36 Piano Class
        String pianoClass = fascicolo.get("PIANO_CLASS");
        if (pianoClass == null)
            pianoClass = "";

        KeyValuePair[] searchCriteria = new KeyValuePair[5];

        searchCriteria[0] = new KeyValuePair();
        searchCriteria[0].setKey("COD_ENTE");
        searchCriteria[0].setValue(fascicolo.get("COD_ENTE"));

        searchCriteria[1] = new KeyValuePair();
        searchCriteria[1].setKey("COD_AOO");
        searchCriteria[1].setValue(fascicolo.get("COD_AOO"));

        searchCriteria[2] = new KeyValuePair();
        searchCriteria[2].setKey("CLASSIFICA");
        searchCriteria[2].setValue(fascicolo.get("CLASSIFICA"));

        searchCriteria[3] = new KeyValuePair();
        searchCriteria[3].setKey("ANNO_FASCICOLO");
        searchCriteria[3].setValue(fascicolo.get("ANNO_FASCICOLO"));

        searchCriteria[4] = new KeyValuePair();
        searchCriteria[4].setKey("PROGR_FASCICOLO");
        searchCriteria[4].setValue(fascicolo.get("PROGR_FASCICOLO"));

        /*if (!StringUtils.isEmpty(pianoClass)) {
            searchCriteria[5] = new KeyValuePair();
            searchCriteria[5].setKey("PIANO_CLASS");
            searchCriteria[5].setValue(pianoClass);
        }*/

        int firstHalf = maxRows / 2;
        SearchItem[] firstSearch = searchDocuments(token, searchCriteria, null, firstHalf, null);

        searchCriteria = new KeyValuePair[5];

        searchCriteria[0] = new KeyValuePair();
        searchCriteria[0].setKey("COD_ENTE");
        searchCriteria[0].setValue(fascicolo.get("COD_ENTE"));

        searchCriteria[1] = new KeyValuePair();
        searchCriteria[1].setKey("COD_AOO");
        searchCriteria[1].setValue(fascicolo.get("COD_AOO"));

        String fascicoloString = String.format("%s/%s/%s",
                fascicolo.get("CLASSIFICA"),
                fascicolo.get("ANNO_FASCICOLO"),
                fascicolo.get("PROGR_FASCICOLO"));

        searchCriteria[2] = new KeyValuePair();
        searchCriteria[2].setKey("FASC_SECONDARI");
        searchCriteria[2].setValue(fascicoloString+";*");

        searchCriteria[3] = new KeyValuePair();
        searchCriteria[3].setKey("FASC_SECONDARI");
        searchCriteria[3].setValue("*;"+fascicoloString+";*");

        // (!pianoClass.equals(Constants.no_piano_class)) {
        if (!StringUtils.isEmpty(pianoClass)) {
            searchCriteria[4] = new KeyValuePair();
            searchCriteria[4].setKey("PIANO_CLASS");
            searchCriteria[4].setValue(pianoClass);
        }

        SearchItem[] secondSearch = searchDocuments(token, searchCriteria, null, maxRows - firstHalf, null);

        SearchItem[] ret = new SearchItem[firstSearch.length + secondSearch.length];

        System.arraycopy(firstSearch, 0, ret, 0, firstSearch.length);
        System.arraycopy(secondSearch, 0, ret, firstSearch.length, secondSearch.length);

        return ret;
    }

    public SearchItem[] searchDocuments(String token, KeyValuePair[] searchCriteria, String[] keywords, int maxRows, KeyValuePair[] orderby) throws DocerException {

        try {
            List<ISearchItem> list = new ArrayList<ISearchItem>();
            list = bl.searchDocuments(token, WSTransformer.toMap3(searchCriteria), WSTransformer.toList(keywords), maxRows, WSTransformer.toMap4(orderby));

            return list.toArray(new SearchItem[0]);

        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean createEnte(String token, KeyValuePair[] enteInfo) throws DocerException {

        try {
            bl.createEnte(token, WSTransformer.toMap1(enteInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean updateEnte(String token, String codiceEnte, KeyValuePair[] enteInfo) throws DocerException {

        try {
            bl.updateEnte(token, codiceEnte, WSTransformer.toMap1(enteInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public KeyValuePair[] getEnte(String token, String codiceEnte) throws DocerException {

        try {
            return WSTransformer.toArray1(bl.getEnte(token, codiceEnte));
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean createAOO(String token, KeyValuePair[] aooInfo) throws DocerException {

        try {
            bl.createAOO(token, WSTransformer.toMap1(aooInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public Version[] getVersionEstesa(String token, String docId) throws DocerException {
        try {

            Set<String> allVersions = new HashSet<String>(bl.getVersions(token, docId));

            Map<String, String> profile = bl.getProfileDocument(token, docId);
            String xml = profile.get("VERSIONS");

            Map<String, Version> versionMap = new TreeMap<String, Version>();
            if (StringUtils.isNotEmpty(xml)) {

                OMElement doc = AXIOMUtil.stringToOM(xml);

                Iterator<?> versions = doc.getChildElements();

                while (versions.hasNext()) {
                    OMElement version = (OMElement) versions.next();

                    Version v = new Version();
                    ArrayList<KeyValuePair> metadata = new ArrayList<KeyValuePair>();
                    Iterator<?> children = version.getChildElements();

                    String number = null;

                    while (children.hasNext()) {
                        OMElement child = (OMElement) children.next();
                        String fieldName = child.getLocalName();
                        String fieldValue = child.getText();

                        KeyValuePair pair = new KeyValuePair();
                        pair.setKey(fieldName);
                        pair.setValue(fieldValue);
                        metadata.add(pair);

                        if (fieldName.equals("number")) {
                            number = fieldValue;
                        }
                    }

                    if (number == null) {
                        //TODO: Throw exception
                        break;
                    }

                    v.setMetadata(metadata.toArray(new KeyValuePair[metadata.size()]));
                    allVersions.remove(number);
                    versionMap.put(number, v);
                }
            }

            for (String oldVersion : allVersions) {
                Version v = new Version();
                KeyValuePair[] metadata = new KeyValuePair[2];

                metadata[0] = new KeyValuePair();
                metadata[0].setKey("number");
                metadata[0].setValue(oldVersion);

                metadata[1] = new KeyValuePair();
                metadata[1].setKey("filename");
                metadata[1].setValue(profile.get("DOCNAME"));

                v.setMetadata(metadata);
                versionMap.put(oldVersion, v);
            }

            ArrayList<Version> ret = new ArrayList<Version>();

            for (Map.Entry<String, Version> entry : versionMap.entrySet()) {
                ret.add(entry.getValue());
            }

            return ret.toArray(new Version[ret.size()]);

        } catch (XMLStreamException e) {
            throw new DocerException(e);
        }
    }

    public boolean updateAOO(String token, KeyValuePair[] aooId, KeyValuePair[] aooInfo) throws DocerException {

        try {
            bl.updateAOO(token, WSTransformer.toMap1(aooId), WSTransformer.toMap1(aooInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public KeyValuePair[] getAOO(String token, KeyValuePair[] aooId) throws DocerException {
        try {
            return WSTransformer.toArray1(bl.getAOO(token, WSTransformer.toMap1(aooId)));
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    // public boolean createAreaTematica(String token, KeyValuePair[]
    // areaTematicaInfo)
    // throws DocerException {
    //
    // try {
    // bl.createAreaTematica(token, WSTransformer
    // .toMap1(areaTematicaInfo));
    // return true;
    // } catch (DocerException e) {
    // throw new DocerException(e.getErrNumber() + " - "
    // + e.getErrDescription());
    // }
    // }
    //
    // public boolean updateAreaTematica(String token, KeyValuePair[]
    // areaTematicaId,
    // KeyValuePair[] areaTematicaInfo) throws DocerException {
    //
    // try {
    // bl.updateAreaTematica(token, WSTransformer.toMap1(areaTematicaId),
    // WSTransformer.toMap1(areaTematicaInfo));
    // return true;
    // } catch (DocerException e) {
    // throw new DocerException(e.getErrNumber() + " - "
    // + e.getErrDescription());
    // }
    // }
    //
    // public KeyValuePair[] getAreaTematica(String token,
    // KeyValuePair[] areaTematicaId) throws DocerException {
    // try {
    // return WSTransformer.toArray1(bl.getAreaTematica(token,
    // WSTransformer.toMap1(areaTematicaId)));
    // } catch (DocerException e) {
    // throw new DocerException(e.getErrNumber() + " - "
    // + e.getErrDescription());
    // }
    // }

    public boolean createTitolario(String token, KeyValuePair[] titolarioInfo) throws DocerException {

        try {
            bl.createTitolario(token, WSTransformer.toMap1(titolarioInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public boolean updateTitolario(String token, KeyValuePair[] titolarioId, KeyValuePair[] titolarioInfo) throws DocerException {

        try {
            bl.updateTitolario(token, WSTransformer.toMap1(titolarioId), WSTransformer.toMap1(titolarioInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public KeyValuePair[] getTitolario(String token, KeyValuePair[] titolarioId) throws DocerException {

        try {
            return WSTransformer.toArray1(bl.getTitolario(token, WSTransformer.toMap1(titolarioId)));
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean createFascicolo(String token, KeyValuePair[] fascicoloInfo) throws DocerException {

        try {
            bl.createFascicolo(token, WSTransformer.toMap1(fascicoloInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean updateFascicolo(String token, KeyValuePair[] fascicoloId, KeyValuePair[] fascicoloInfo) throws DocerException {
        try {
            bl.updateFascicolo(token, WSTransformer.toMap1(fascicoloId), WSTransformer.toMap1(fascicoloInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public KeyValuePair[] getFascicolo(String token, KeyValuePair[] fascicoloId) throws DocerException {
        try {
            return WSTransformer.toArray1(bl.getFascicolo(token, WSTransformer.toMap1(fascicoloId)));
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean createAnagraficaCustom(String token, KeyValuePair[] customInfo) throws DocerException {

        try {
            bl.createAnagraficaCustom(token, WSTransformer.toMap1(customInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public boolean updateAnagraficaCustom(String token, KeyValuePair[] customId, KeyValuePair[] customInfo) throws DocerException {

        try {
            bl.updateAnagraficaCustom(token, WSTransformer.toMap1(customId), WSTransformer.toMap1(customInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public KeyValuePair[] getAnagraficaCustom(String token, KeyValuePair[] customId) throws DocerException {

        try {
            return WSTransformer.toArray1(bl.getAnagraficaCustom(token, WSTransformer.toMap1(customId)));
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public SearchItem[] searchAnagraficheEstesa(String token, String type, KeyValuePair[] searchCriteria, int maxRows, KeyValuePair[] orderby) throws DocerException {
        long start = new Date().getTime();

        try {
            return bl.searchAnagraficheEstesa(token, type, WSTransformer.toMap3(searchCriteria), maxRows, WSTransformer.toMap4(orderby)).toArray(new SearchItem[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        } finally {
            // long end = new Date().getTime();
            // log.info("searchAnagrafiche: " +(end-start) +" ms");
        }
    }

    public SearchItem[] searchAnagrafiche(String token, String type, KeyValuePair[] searchCriteria) throws DocerException {

        long start = new Date().getTime();

        try {
            return bl.searchAnagrafiche(token, type, WSTransformer.toMap3(searchCriteria)).toArray(new SearchItem[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        } finally {
            // long end = new Date().getTime();
            // log.info("searchAnagrafiche: " +(end-start) +" ms");
        }
    }

    public boolean createUser(String token, KeyValuePair[] userInfo) throws DocerException {

        try {
            bl.createUser(token, WSTransformer.toMap1(userInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean updateUser(String token, String userId, KeyValuePair[] userInfo) throws DocerException {

        try {
            bl.updateUser(token, userId, WSTransformer.toMap1(userInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public KeyValuePair[] getUser(String token, String userId) throws DocerException {

        try {
            return WSTransformer.toArray1(bl.getUser(token, userId));
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public SearchItem[] searchUsers(String token, KeyValuePair[] searchCriteria) throws DocerException {

        try {
            return bl.searchUsers(token, WSTransformer.toMap3(searchCriteria)).toArray(new SearchItem[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean createGroup(String token, KeyValuePair[] groupInfo) throws DocerException {

        try {
            bl.createGroup(token, WSTransformer.toMap1(groupInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean updateGroup(String token, String groupId, KeyValuePair[] groupInfo) throws DocerException {

        try {
            bl.updateGroup(token, groupId, WSTransformer.toMap1(groupInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public KeyValuePair[] getGroup(String token, String groupId) throws DocerException {

        try {
            return WSTransformer.toArray1(bl.getGroup(token, groupId));
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public SearchItem[] searchGroups(String token, KeyValuePair[] searchCriteria) throws DocerException {
        try {
            return bl.searchGroups(token, WSTransformer.toMap3(searchCriteria)).toArray(new SearchItem[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean updateUsersOfGroup(String token, String groupId, String[] usersToAdd, String[] usersToRemove) throws DocerException {

        try {
            bl.updateUsersOfGroup(token, groupId, WSTransformer.toList(usersToAdd), WSTransformer.toList(usersToRemove));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean setUsersOfGroup(String token, String groupId, String[] users) throws DocerException {
        try {
            bl.setUsersOfGroup(token, groupId, WSTransformer.toList(users));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public String[] getUsersOfGroup(String token, String groupId) throws DocerException {

        try {
            return bl.getUsersOfGroup(token, groupId).toArray(new String[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean updateGroupsOfUser(String token, String userId, String[] groupsToAdd, String[] groupsToRemove) throws DocerException {

        try {
            bl.updateGroupsOfUser(token, userId, WSTransformer.toList(groupsToAdd), WSTransformer.toList(groupsToRemove));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean setGroupsOfUser(String token, String userId, String[] groups) throws DocerException {
        try {
            bl.setGroupsOfUser(token, userId, WSTransformer.toList(groups));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public String[] getGroupsOfUser(String token, String userId) throws DocerException {

        try {
            return bl.getGroupsOfUser(token, userId).toArray(new String[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public KeyValuePair[] getDocumentTypes(String token) throws DocerException {
        try {
            return bl.getDocumentTypes(token).toArray(new KeyValuePair[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean verifyTicket(String token) {
        return bl.verifyTicket(token);
    }

    public boolean setACLTitolario(String token, KeyValuePair[] titolarioId, KeyValuePair[] acls) throws DocerException {

        Map<String, EnumACLRights> aclsMap = WSTransformer.toMap2(acls);

        try {
            bl.setACLTitolario(token, WSTransformer.toMap1(titolarioId), aclsMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public KeyValuePair[] getACLTitolario(String token, KeyValuePair[] titolarioId) throws DocerException {

        try {
            Map<String, EnumACLRights> aclsMap = bl.getACLTitolario(token, WSTransformer.toMap1(titolarioId));
            return WSTransformer.toArray2(aclsMap);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public boolean setACLFascicolo(String token, KeyValuePair[] fascicoloId, KeyValuePair[] acls) throws DocerException {

        Map<String, EnumACLRights> aclsMap = WSTransformer.toMap2(acls);

        try {
            bl.setACLFascicolo(token, WSTransformer.toMap1(fascicoloId), aclsMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public KeyValuePair[] getACLFascicolo(String token, KeyValuePair[] fascicoloId) throws DocerException {

        try {
            Map<String, EnumACLRights> aclsMap = bl.getACLFascicolo(token, WSTransformer.toMap1(fascicoloId));
            return WSTransformer.toArray2(aclsMap);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public String createFolder(String token, KeyValuePair[] folderInfo) throws DocerException {

        try {
            return bl.createFolder(token, WSTransformer.toMap1(folderInfo));
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public boolean updateFolder(String token, String folderId, KeyValuePair[] folderInfo) throws DocerException {

        try {
            bl.updateFolder(token, folderId, WSTransformer.toMap1(folderInfo));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean deleteFolder(String token, String folderId) throws DocerException {

        try {
            bl.deleteFolder(token, folderId);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean setACLFolder(String token, String folderId, KeyValuePair[] acls) throws DocerException {

        Map<String, EnumACLRights> aclsMap = WSTransformer.toMap2(acls);

        try {
            bl.setACLFolder(token, folderId, aclsMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public KeyValuePair[] getACLFolder(String token, String folderId) throws DocerException {

        try {
            Map<String, EnumACLRights> aclsMap = bl.getACLFolder(token, folderId);
            return WSTransformer.toArray2(aclsMap);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public String[] getFolderDocuments(String token, String folderId) throws DocerException {

        try {
            return bl.getFolderDocuments(token, folderId).toArray(new String[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean addToFolderDocuments(String token, String folderId, String[] document) throws DocerException {

        try {
            if (document == null)
                return true;
            List<String> toAdd = new ArrayList<String>();
            for (String id : document) {
                if (toAdd.contains(id))
                    continue;
                toAdd.add(id);
            }
            bl.addToFolderDocuments(token, folderId, toAdd);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean removeFromFolderDocuments(String token, String folderId, String[] document) throws DocerException {

        try {
            if (document == null)
                return true;
            List<String> toRemove = new ArrayList<String>();
            for (String id : document) {
                if (toRemove.contains(id))
                    continue;
                toRemove.add(id);
            }
            bl.removeFromFolderDocuments(token, folderId, toRemove);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public SearchItem[] searchFolders(String token, KeyValuePair[] searchCriteria, int maxRows, KeyValuePair[] orderby) throws DocerException {

        try {
            List<ISearchItem> list = new ArrayList<ISearchItem>();
            list = bl.searchFolders(token, WSTransformer.toMap3(searchCriteria), maxRows, WSTransformer.toMap4(orderby));

            return list.toArray(new SearchItem[0]);

        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public String[] getRiferimentiDocuments(String token, String docId) throws DocerException {

        try {
            return bl.getRiferimentiDocuments(token, docId).toArray(new String[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean addRiferimentiDocuments(String token, String docId, String[] riferimenti) throws DocerException {

        try {
            if (riferimenti == null)
                return true;
            List<String> toAddRiferimenti = new ArrayList<String>();
            for (String id : riferimenti) {
                if (toAddRiferimenti.contains(id))
                    continue;
                toAddRiferimenti.add(id);
            }
            bl.addRiferimentiDocuments(token, docId, toAddRiferimenti);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean removeRiferimentiDocuments(String token, String docId, String[] riferimenti) throws DocerException {

        try {
            if (riferimenti == null)
                return true;
            List<String> toRemoveRiferimenti = new ArrayList<String>();
            for (String id : riferimenti) {
                if (toRemoveRiferimenti.contains(id))
                    continue;
                toRemoveRiferimenti.add(id);
            }
            bl.removeRiferimentiDocuments(token, docId, toRemoveRiferimenti);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean protocollaDocumento(String token, String docId, KeyValuePair[] metadata) throws DocerException {

        try {
            Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
            bl.protocollaDocumento(token, docId, metadataMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean registraDocumento(String token, String docId, KeyValuePair[] metadata) throws DocerException {

        try {
            Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
            bl.registraDocumento(token, docId, metadataMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean fascicolaDocumento(String token, String docId, KeyValuePair[] metadata) throws DocerException {

        try {
            Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
            bl.fascicolaDocumento(token, docId, metadataMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean classificaDocumento(String token, String docId, KeyValuePair[] metadata) throws DocerException {

        try {
            Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
            bl.classificaDocumento(token, docId, metadataMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean pubblicaDocumento(String token, String docId, KeyValuePair[] metadata) throws DocerException {

        try {
            Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
            bl.pubblicaDocumento(token, docId, metadataMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean archiviaDocumento(String token, String docId, KeyValuePair[] metadata) throws DocerException {

        try {
            Map<String, String> metadataMap = WSTransformer.toMap1(metadata);
            bl.archiviaDocumento(token, docId, metadataMap);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public int getUserRightsAnagrafiche(String token, String type, KeyValuePair[] id, String userId) throws DocerException {

        try {
            return bl.getUserRightsAnagrafiche(token, type, WSTransformer.toMap1(id), userId).getCode();
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public int getUserRightsFolder(String token, String folderId, String userId) throws DocerException {

        try {
            return bl.getUserRightsFolder(token, folderId, userId).getCode();
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public KeyValuePair[] getDocumentTypesByAOO(String token, String codiceEnte, String codiceAOO) throws DocerException {
        try {
            return bl.getDocumentTypes(token, codiceEnte, codiceAOO).toArray(new KeyValuePair[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean addNewAdvancedVersion(String token, String docIdLastVersion, String docIdNewVersion) throws DocerException {

        try {

            bl.addNewAdvancedVersion(token, docIdLastVersion, docIdNewVersion);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public boolean addOldAdvancedVersion(String token, String docIdLastVersion, String docIdNewVersion, Double version) throws DocerException {

        try {

            bl.addOldAdvancedVersion(token, docIdLastVersion, docIdNewVersion, version);
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public String[] getAdvancedVersions(String token, String docId) throws DocerException {

        try {
            return bl.getAdvancedVersions(token, docId).toArray(new String[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }

    }

    public SearchItem[] getRiferimentiFascicolo(String token, KeyValuePair[] fascicoloId) throws DocerException {

        try {
            return bl.getRiferimentiFascicolo(token, WSTransformer.toMap1(fascicoloId)).toArray(new SearchItem[0]);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean addRiferimentiFascicolo(String token, KeyValuePair[] fascicoloId, SearchItem[] riferimenti) throws DocerException {

        try {
            if (riferimenti == null)
                return true;

            bl.addRiferimentiFascicolo(token, WSTransformer.toMap1(fascicoloId), WSTransformer.toList1(riferimenti));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public boolean removeRiferimentiFascicolo(String token, KeyValuePair[] fascicoloId, SearchItem[] riferimenti) throws DocerException {

        try {
            if (riferimenti == null)
                return true;

            bl.removeRiferimentiFascicolo(token, WSTransformer.toMap1(fascicoloId), WSTransformer.toList1(riferimenti));
            return true;
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }
}
