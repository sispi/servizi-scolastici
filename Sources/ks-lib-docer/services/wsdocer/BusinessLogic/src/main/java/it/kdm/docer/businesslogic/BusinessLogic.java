package it.kdm.docer.businesslogic;

import com.google.common.base.Strings;
import it.kdm.docer.businesslogic.configuration.ConfigurationManager;
import it.kdm.docer.businesslogic.configuration.enums.EnumBLPropertyType;
import it.kdm.docer.businesslogic.configuration.types.AnagraficaType;
import it.kdm.docer.businesslogic.configuration.types.DocumentType;
import it.kdm.docer.businesslogic.configuration.types.FieldDescriptor;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.docerservice.BaseService;
import it.kdm.docer.commons.docerservice.UserProfile;
import it.kdm.docer.sdk.*;
import it.kdm.docer.sdk.classes.*;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.*;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;
import it.kdm.utils.DocerUtils;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyException;
import java.util.*;
import java.util.Map.Entry;

public class BusinessLogic extends BaseService {

    // private static List<String> ADMINS_GROUPS = new ArrayList<String>();

    private static TicketCipher cipher = new TicketCipher();

    private static String SLASH = "/";
    private static String VOID = "";

    private static String ERROR_MESSAGE_CREATE_AOO = "Verificare i campi obbligatori (COD_ENTE, COD_AOO, DES_AOO)";
    private static String ERROR_MESSAGE_CREATE_TITOLARIO = "Verificare i campi obbligatori (COD_ENTE, COD_AOO, CLASSIFICA, DES_TITOLARIO)";
    private static String ERROR_MESSAGE_CREATE_FASCICOLO = "Verificare i campi obbligatori (COD_ENTE, COD_AOO, CLASSIFICA, PROGR_FASCICOLO, ANNO_FASCICOLO, DES_FASCICOLO)";

    // private static String USER = "USER";
    // private static String GROUP = "GROUP";

    // private static boolean CONFIG_INITIALIZED = false;
    // private static String CONFIG_ERROR_MESSAGE = "";

    private int PRIMARYSEARCH_MAX_ROWS = 1000;

    private it.kdm.docer.sdk.IProvider provider = null;
    private Configuration config;


    // private static List<String> BASE_PROFILE = new ArrayList<String>();
    // private static Map<String, FieldDescriptor> FIELDS = new HashMap<String,
    // FieldDescriptor>();
    // private static Map<String, DocumentType> DOCUMENT_TYPES = new
    // HashMap<String, DocumentType>();
    // private static Map<String, AnagraficaType> ANAGRAFICHE_TYPES = new
    // HashMap<String, AnagraficaType>();
    // private static Map<String, List<String>> HITLISTS = new HashMap<String,
    // List<String>>();

    private static Map<String, String> enteAooDescriptionCaching = new HashMap<String, String>();

    // private static String fascicoliSecondariFormat =
    // "classifica/anno_fascicolo/progr_fascicolo";
    // private static String regexpFascicoliSec =
    // "^([^/]+)/([12][0-9][0-9][0-9])/(.+)$";
    // private static Pattern patternFascicoliSec =
    // Pattern.compile(regexpFascicoliSec);
    // private static int classificaPosition = 1;
    // private static int annoFascicoloPosition = 2;
    // private static int progrFascicoloPosition = 3;
    // private static boolean MIGRATION_MODE = false;
    //
    // private static FascicoloUtils fascicoloUtils = new
    // FascicoloUtils(fascicoliSecondariFormat, regexpFascicoliSec,
    // classificaPosition, annoFascicoloPosition, progrFascicoloPosition);
    //
    // private static boolean allow_record_versioning_archive = false;
    // private static boolean allow_record_add_allegato = false;
    // private static boolean allow_pubblicato_add_related = false;

    // private static String configPath = "confstub.xml";
    // private static Config c = null;

    private static ConfigurationManager BL_CONFIGURATIONS = new ConfigurationManager();

    private String entetoken = null;

    public static void setConfigPath(String path) {
        // configPath = path;
        BL_CONFIGURATIONS.setConfigurationFile(path);
    }

    private LoggedUserInfo parseTicket(String ticket) throws DocerException {

        if (ticket == null || ticket.equals(VOID))
            throw new DocerException("ticket null");

        try {

            ticket = cipher.decryptTicket(ticket);

            String codiceEnte = Utils.extractTokenKey(ticket, "ente");
            String userid = Utils.extractTokenKey(ticket, "uid");
            String dmsticket = Utils.extractTokenKey(ticket, "dmsticket");

            LoggedUserInfo loginUserInfo = new LoggedUserInfo();
            loginUserInfo.setCodiceEnte(codiceEnte);
            loginUserInfo.setTicket(dmsticket);
            loginUserInfo.setUserId(userid);

            return loginUserInfo;
        } catch (KeyException e) {
            throw new DocerException("Errore parseTicket: " + e.getMessage());
        }

    }

    // TODO: messo per compatibilita' all'indietro degli altri Providers
    // (gestione CIFS)
    // TODO: da togliere quando gestiremo dinamicamente i profilo delle Folders
//    private static boolean CIFS = false;

    public BusinessLogic(String providerName, int pimarySearchMaxRows) throws DocerException {

        // provider verso il quale si deve effettiare la login
        this.provider = ProviderManager.create(providerName);
        this.PRIMARYSEARCH_MAX_ROWS = pimarySearchMaxRows;

//        if (providerName.equals("it.kdm.docer.alfresco.provider.Provider")) {
//            CIFS = true;
//        }

        try {
            config = new PropertiesConfiguration("businesslogic.properties");
        } catch (ConfigurationException e) {
            throw new DocerException(e);
        }
    }

    // ************* METODI PUBBLICI ***********/
    public boolean writeConfig(String ticket, String xml) throws DocerException {

        // if(!CONFIG_INITIALIZED){
        // throw new DocerException("Configurazione non inizializzata: "
        // +CONFIG_ERROR_MESSAGE);
        // }

        try {
            // NEL WRITECONFIG NON DEVE ESSERE FATTA LA VALIDAZIONE DEL ticket
            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            //
            // if (!provider.verifyTicket(loginUserInfo.extractUserProfile(),
            // loginUserInfo.getCodiceEnte(), loginUserInfo.getTicket())) {
            // throw new DocerException("ticket non valido");
            // }

            if (xml != null && !xml.equals(VOID)) {
                // il file puo' viaggiare criptato e non posso leggerlo come
                // fosse in chiaro
                // validateNextConfigurationXml(xml);
                BL_CONFIGURATIONS.writeConfig(entetoken, xml);
            }

            // initConfiguration();

            clearCache();

            return true;
        } catch (Exception err) {
            throw new DocerException(500, "writeConfig: " + err.getMessage());
        }
    }

    public String readConfig(String ticket) throws DocerException {

        try {

            // NEL READ CONFIG NON DEVE ESSERE VERIFICATO IL ticket
            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            //
            // if (!provider.verifyTicket(loginUserInfo.extractUserProfile(),
            // loginUserInfo.getCodiceEnte(), loginUserInfo.getTicket())) {
            // throw new DocerException("ticket non valido");
            // }

            return BL_CONFIGURATIONS.getConfiguration(entetoken).readConfig();
        }
        // catch (DocerException docEx) {
        //
        // throw new DocerException(500, "readConfig :" + docEx.getMessage());
        //
        // }
        catch (Exception err) {
            throw new DocerException(500, "readConfig :" + err.getMessage());
        }
    }

    // 40
    public String login(String codiceEnte, String userid, String password) throws DocerException {

        try {

            // if (codiceEnte == null || codiceEnte.equals(VOID))
            // throw new DocerException("codiceEnte non specificato");

            if (userid == null || userid.equals(VOID))
                throw new DocerException("userid non specificata");
            if (password == null || password.equals(VOID))
                throw new DocerException("password non specificata");

            if (codiceEnte == null) {
                codiceEnte = "";
            }
            entetoken = codiceEnte;

            // effettuo login
            String dmsticket = provider.login(userid, password, codiceEnte);

            ILoggedUserInfo loggedUserInfo = new LoggedUserInfo(userid, codiceEnte, dmsticket);
            provider.setCurrentUser(loggedUserInfo);
            IUserInfo userInfo = provider.getUser(userid);

            if (StringUtils.isEmpty(codiceEnte)) {
                boolean isAdmin = false;
                for (String groupId : userInfo.getGroups()) {
                    if (BL_CONFIGURATIONS.getConfig(entetoken).getADMINS_GROUPS().contains(groupId)) {
                        isAdmin = true;
                        break;
                    }
                }
                if (!isAdmin) {
                    throw new DocerException(-1000, "Ente obbligatorio per l'autenticazione se non si e' amministratori");
                }
            } else {
                Map<String, String> idCrit = new HashMap<String, String>();
                idCrit.put(Constants.ente_cod_ente, codiceEnte);
                Map<String, String> profile = getAnagraficaProfile(Constants.ente_type_id, idCrit);

                if (profile == null) {
                    throw new DocerException("Ente " + codiceEnte + " non trovato");
                }
            }

            String ticket = makeTicket(codiceEnte, dmsticket, userid);

            return ticket;

        } catch (DocerException docEx) {

            // if(MIGRATION_MODE)
            throw new DocerException(440, docEx.getMessage());

            // throw new DocerException(440, "Autenticazione fallita.");

        } catch (Exception err) {
            // if(MIGRATION_MODE)
            throw new DocerException(540, err.getMessage());

            // throw new DocerException(440, "Autenticazione fallita.");
        }
    }

    private String findUser(UserProfile ui, String adminUser, String adminPwd) {
        try {
            String ticket = login(ui.getCodiceEnte(), adminUser, adminPwd);

            Map<String, List<String>> criteria = new HashMap<String, List<String>>();

            ArrayList<String> list = new ArrayList<String>();
            list.add(ui.getEmail());

            criteria.put(Constants.user_email_address, list);

            List<ISearchItem> results = searchUsers(ticket, criteria);
            if (results.isEmpty()) {
                throw new IllegalStateException("User not found");
            }

            if (results.size() > 1) {
                throw new IllegalStateException("Too many users");
            }

            ISearchItem result = results.get(0);
            KeyValuePair[] metadata = result.getMetadata();

            for (KeyValuePair meta : metadata) {
                String key = meta.getKey();
                if (key.equals(Constants.user_user_id)) {
                    return meta.getValue();
                }
            }

            throw new IllegalStateException("Found user with no USER_ID");

        } catch (DocerException e) {
            throw new IllegalArgumentException("Impersonificazione durante il login fallita");
        }
    }


    public KeyValuePair[] loginSSO(String saml, String codiceEnte) throws DocerException {

        try {

            if (codiceEnte == null) {
                codiceEnte = "";
            }

            entetoken = codiceEnte;

            // effettuo login
            ISsoUserInfo ssoUserInfo = provider.loginSSO(saml, codiceEnte);

            String dmsticket = ssoUserInfo.getTicket();
            String userid = ssoUserInfo.getUserID();

            ILoggedUserInfo loggedUserInfo = new LoggedUserInfo(userid, codiceEnte, dmsticket);
            provider.setCurrentUser(loggedUserInfo);
            IUserInfo userInfo = provider.getUser(userid);

            if (StringUtils.isEmpty(codiceEnte)) {
                boolean isAdmin = false;
                for (String groupId : userInfo.getGroups()) {
                    if (BL_CONFIGURATIONS.getConfig(entetoken).getADMINS_GROUPS().contains(groupId)) {
                        isAdmin = true;
                        break;
                    }
                }
                if (!isAdmin) {
                    throw new DocerException(-1000, "Ente obbligatorio per l'autenticazione se non si e' amministratori");
                }
            } else {
                Map<String, String> idCrit = new HashMap<String, String>();
                idCrit.put(Constants.ente_cod_ente, codiceEnte);
                Map<String, String> profile = getAnagraficaProfile(Constants.ente_type_id, idCrit);

                if (profile == null) {
                    throw new DocerException("Ente " + codiceEnte + " non trovato");
                }
            }

            String ticket = makeTicket(codiceEnte, dmsticket, userid);

            return ssoUserInfo2KeyValuePairs(ssoUserInfo, ticket);

        } catch (DocerException docEx) {

            // if(MIGRATION_MODE)
            throw new DocerException(440, docEx.getMessage());

            // throw new DocerException(440, "Autenticazione fallita.");

        } catch (Exception err) {
            // if(MIGRATION_MODE)
            throw new DocerException(540, err.getMessage());

            // throw new DocerException(440, "Autenticazione fallita.");
        }
    }

    private String makeTicket(String codiceEnte, String dmsticket, String userid) {
        String ticket = "";
        ticket = Utils.addTokenKey(ticket, "ente", codiceEnte);
        ticket = Utils.addTokenKey(ticket, "uid", userid);
        ticket = Utils.addTokenKey(ticket, "dmsticket", dmsticket);

        ticket = cipher.encryptTicket(ticket);
        return ticket;
    }

    private KeyValuePair[] ssoUserInfo2KeyValuePairs(ISsoUserInfo ssoUserInfo, String ticket) {

        if (StringUtils.isBlank(ticket)) {
            ticket = ssoUserInfo.getTicket();
        }

        List<KeyValuePair> retList = new ArrayList<KeyValuePair>();

        retList.add(new KeyValuePair("ticket", ticket));
        retList.add(new KeyValuePair("uid", ssoUserInfo.getUserID()));
        retList.add(new KeyValuePair("groups", ssoUserInfo.getGroups()));

        if (ssoUserInfo.getExtraData() != null) {
            for (String key : ssoUserInfo.getExtraData().keySet()) {

                Object obj = ssoUserInfo.getExtraData().get(key);

                String value = null;
                if (obj != null) {
                    value = obj.toString();
                }

                retList.add(new KeyValuePair(key, value));
            }
        }

        return retList.toArray(new KeyValuePair[]{});
    }

    // 41
    public void logout(String ticket) throws DocerException {

        // if (!CONFIG_INITIALIZED) {
        // throw new DocerException("Configurazione non inizializzata: " +
        // CONFIG_ERROR_MESSAGE);
        // }

        try {

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();
            entetoken = loginUserInfo.getCodiceEnte();

            // setto i parametri dell'utente
            provider.setCurrentUser(loginUserInfo);

            // eseguo logout
            provider.logout();

        } catch (DocerException docEx) {

            throw new DocerException(441, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(541, err.getMessage());
        }
    }

    // 42
    public String createDocument(String ticket, Map<String, String> metadata, InputStream file) throws DocerException {

        try {

            metadata = toUCMap(metadata, "metadata");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            String codEnte = metadata.get(Constants.doc_cod_ente);
            String codAOO = metadata.get(Constants.doc_cod_aoo);
            String docname = metadata.get(Constants.doc_docname);
            String documentTypeId = metadata.get(Constants.doc_type_id);

            if (StringUtils.isEmpty(documentTypeId) || StringUtils.isEmpty(docname) || StringUtils.isEmpty(codEnte) || StringUtils.isEmpty(codAOO)) {
                throw new DocerException("TYPE_ID, DOCNAME, COD_ENTE, COD_AOO obbligatori");
            }

            boolean isUrlDocument = false;
            String archiveType = String.valueOf(metadata.get(Constants.doc_archive_type));
            String docUrl = metadata.get(Constants.doc_doc_url);

            if (archiveType.equalsIgnoreCase("URL")) {
                if (StringUtils.isEmpty(docUrl)) {
                    throw new DocerException("DOC_URL obbligatorio se ARCHIVE_TYPE=URL");

                }

                isUrlDocument = true;

                if (file != null) {
                    throw new DocerException("file documento non ammesso se ARCHIVE_TYPE=URL");
                }

            } else if ( "true".equals(metadata.get("NO_FILE")) ) {
                //---------------------------------------------------
                String default_no_file = BL_CONFIGURATIONS.getConfig(entetoken).getDefault_no_file();
                String ext = FilenameUtils.getExtension(default_no_file);
                String docNameExt = FilenameUtils.getExtension(docname);
                if (docNameExt.equalsIgnoreCase(ext)) {
                    file = new FileInputStream(default_no_file);
                }
                //---------------------------------------------------
                metadata.remove("NO_FILE");

            }
            else {

                if (file == null)
                    throw new DocerException("file documento null");
                if (file.available() == 0)
                    throw new DocerException("file documento vuoto");
            }

            documentTypeId = documentTypeId.toUpperCase();

            // sostituiamo il valore uppercase
            metadata.put(Constants.doc_type_id, documentTypeId);

            DocumentType docType = BL_CONFIGURATIONS.getConfig(entetoken).getDocumentTypesMapping().get(documentTypeId, codEnte, codAOO);

            if (docType == null) {
                // throw new DocerException("type_id sconosciuto: " +
                // documentTypeId);
                throw new DocerException("Document Type " + documentTypeId + " non e' definito per Ente " + codEnte + " e AOO " + codAOO);
            }

            // Controllo che il document type e il tipo componente siano compatibili
            if (!checkComponentType(metadata.get("TIPO_COMPONENTE"), docType))
                throw new DocerException("Document Type " + documentTypeId + " non e' definito per il tipo componente " + metadata.get("TIPO_COMPONENTE"));

            // if (!docType.isDefinedForAOO(codEnte, codAOO))
            // throw new DocerException("Document Type " + documentTypeId +
            // " non e' definito per Ente " + codEnte + " e AOO " + codAOO);

            FieldDescriptor fd = null;
            // controllo se i campi specifiati appartengono al documenttype
            for (String str : metadata.keySet()) {

                fd = docType.getFieldDescriptor(str);

                if (fd == null)
                    throw new DocerException("il campo " + str + " non appartiene al tipo " + documentTypeId);

                // controllo lunghezza e formato dei metadati
                metadata.put(str, fd.checkValueFormat(metadata.get(str)));
            }

            // controllo i metadati obbligatori e vietati e pulisco
            Map<String, String> cleanMetadata = checkPropertiesCreateDocument(metadata);

            // test della library

            // se non e' specificato AUTHOR_ID, lo assegno
            // if (metadata.get(Constants.doc_author_id) == null)
            // metadata.put(Constants.doc_author_id, userid);

            // se non e' specificato TYPIST_ID, lo assegno
            // if (metadata.get(Constants.doc_typist_id) == null)
            // metadata.put(Constants.doc_typist_id, userid);

            // controllo Ente
            checkEnte(codEnte); // codice_ente e' obbligatorio
            // controllo AOO
            checkAOO(codAOO, codEnte); // codice_aoo e' obbligatorio

            EnumStatoArchivistico statoArchivistico = getEnumStatoArchivistico(metadata.get(Constants.doc_stato_archivistico));
            if (statoArchivistico.equals(EnumStatoArchivistico.undefined)) {
                statoArchivistico = EnumStatoArchivistico.generico;
                cleanMetadata.put(Constants.doc_stato_archivistico, String.valueOf(statoArchivistico.getCode()));
            }

            // compatibilita' Docarea
            if (!cleanMetadata.containsKey(Constants.doc_stato_pantarei)) {
                cleanMetadata.put(Constants.doc_stato_pantarei, String.valueOf(EnumStatiPantarei.generico.getCode()));
            }

            // controllo di esistenza delle anagrafiche custom
            // checkAnagraficheCustom(codEnte, codAOO, cleanMetadata);

            // controllo l'esistenza delle anagrafiche dei Multivalues
            // checkAnagraficheMultivalue(codEnte, codAOO, docType,
            // cleanMetadata);

            checkAnagraficheCustom(codEnte, codAOO, docType, cleanMetadata);

            cleanMetadata.put(Constants.doc_version,"1.0");

            // *** CREAZIONE
            String docId = provider.createDocument(docType.getTypeId(), cleanMetadata, file);

            return docId;

        } catch (DocerException docEx) {

            throw new DocerException(442, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(542, err.getMessage());
        }
    }

    // 43
    public void updateProfileDocument(String ticket, String docId, Map<String, String> metadata) throws DocerException {

        try {

            metadata = toUCMap(metadata, "metadata");

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // CREATE_VALIDATION
            // boolean creaAnagrafiche = true;

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);
            searchCriteria.put(Constants.doc_docnum, criteria);
            searchCriteria.put(Constants.custom_enabled, Collections.singletonList("*"));

            // devo confrontare tutti i metadati del protocollo
            // List<String> returnProperties =
            // SCHEMASDEFINITIONS.getSchemaDefinition(PROTOCOLLO_SCHEMAID);
            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_type_id);
            returnProperties.add(Constants.doc_cod_ente);
            returnProperties.add(Constants.doc_cod_aoo);

            // RICERCA PRIMARIA
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato perche' devo modificare un solo
            // documento
            if (searchResults.getRows().size() == 0)
                throw new DocerException("documento " + docId + " non trovato");
            if (searchResults.getRows().size() != 1)
                throw new DocerException("docId " + docId + " non univoco");

            ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);
            if (checkedOutInfo.getLocked() && !checkedOutInfo.getUserId().equalsIgnoreCase(loginUserInfo.getUserId()))
                throw new DocerException("documento " + docId + " bloccato esclusivamente da un altro utente");

            // HO TROVATO IL DOCUMENTO -> un solo risultato
            DataRow<String> oldProfileData = searchResults.getRow(0);

            String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
            String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);

            if (oldCodEnte == null || oldCodEnte.equals(VOID))
                throw new DocerException("Errore: documento " + docId + " senza COD_ENTE assegnato");

            if (oldCodAOO == null || oldCodAOO.equals(VOID))
                throw new DocerException("Errore: documento " + docId + " senza COD_AOO assegnato");

            // documentType NON puo' essere null (e' dato obbligatorio di
            // creazione)
            String oldTypeId = oldProfileData.get(Constants.doc_type_id);

            if (oldTypeId == null || oldTypeId.equals(VOID))
                throw new DocerException("TYPE_ID non assegnato al documento nel sistema: " + docId);

            oldTypeId = oldTypeId.toUpperCase();

            DocumentType oldDocType = BL_CONFIGURATIONS.getConfig(entetoken).getDocumentTypesMapping().get(oldTypeId, oldCodEnte, oldCodAOO);

            if (oldDocType == null) {
                // throw new DocerException("documento " + docId +
                // " con type_id sconosciuto: " + oldTypeId);
                throw new DocerException("Document Type " + oldTypeId + " non e' definito per Ente " + oldCodEnte + " e AOO " + oldCodAOO);
            }

            // if (!olddoctype.isdefinedforaoo(oldcodente, oldcodaoo)) {
            // throw new docerexception("document type " + oldtypeid +
            // " non e' definito per ente " + oldcodente + " e aoo " +
            // oldcodaoo);
            //
            // }

            returnProperties = oldDocType.getFieldsNames();

            // inserisco i campi necessari ai controlli per sicurezza
            if (!returnProperties.contains(Constants.doc_stato_archivistico)) {
                returnProperties.add(Constants.doc_stato_archivistico);
            }
            if (!returnProperties.contains(Constants.doc_docname)) {
                returnProperties.add(Constants.doc_docname);
            }
            if (!returnProperties.contains(Constants.doc_tipo_componente)) {
                returnProperties.add(Constants.doc_tipo_componente);
            }
            if (!returnProperties.contains(Constants.doc_docnum)) {
                returnProperties.add(Constants.doc_docnum);
            }
            if (!returnProperties.contains(Constants.doc_stato_pantarei)) {
                returnProperties.add(Constants.doc_stato_pantarei);
            }
            if (!returnProperties.contains(Constants.doc_pubblicazione_pubblicato)) {
                returnProperties.add(Constants.doc_pubblicazione_pubblicato);
            }
            if (!returnProperties.contains(Constants.doc_docnum_record)) {
                returnProperties.add(Constants.doc_docnum_record);
            }
            // if(!returnProperties.contains(Constants.doc_app_id)){
            // returnProperties.add(Constants.doc_app_id);
            // }
            if (!returnProperties.contains(Constants.doc_docnum_princ)) {
                returnProperties.add(Constants.doc_docnum_princ);
            }
            if (!returnProperties.contains(Constants.doc_creation_date)) {
                returnProperties.add(Constants.doc_creation_date);
            }
            if (!returnProperties.contains(Constants.doc_archive_type)) {
                returnProperties.add(Constants.doc_archive_type);
            }

            searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato perche' devo modificare un solo
            // documento
            if (searchResults.getRows().size() == 0)
                throw new DocerException("documento " + docId + " non trovato");
            if (searchResults.getRows().size() != 1)
                throw new DocerException("docId " + docId + " non univoco");

            // HO TROVATO IL DOCUMENTO -> un solo risultato con PROFILO COMPLETO
            oldProfileData = searchResults.getRow(0);

            // verifico se e' cambiato
            EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));

            if (oldStatoArchivistico.equals(EnumStatoArchivistico.in_archivio_di_deposito)) {
                throw new DocerException("non e' possibile modificare un documento con STATO_ARCHIVISTICO: " + oldStatoArchivistico + " (" + oldStatoArchivistico.getCode() + ")");
            }

            DocumentType docType = oldDocType;

            // da Docer 1.2 e' permesso il cambio di TYPE_ID
            if (metadata.containsKey(Constants.doc_type_id)) {
                String newTypeId = metadata.get(Constants.doc_type_id);

                if (newTypeId == null) {
                    newTypeId = "";
                }

                if (!oldTypeId.equalsIgnoreCase(newTypeId)) {
                    newTypeId = newTypeId.toUpperCase();

                    if (newTypeId.equals(VOID)) {
                        throw new DocerException("non e' possibile modificare TYPE_ID del documento " + docId + " in stringa vuota");
                    }

                    // sostituiamo il valore uppercase
                    DocumentType newDocType = BL_CONFIGURATIONS.getConfig(entetoken).getDocumentTypesMapping().get(newTypeId, oldCodEnte, oldCodAOO);

                    if (newDocType == null) {
                        // throw new
                        // DocerException("nuovo TYPE_ID del documento " + docId
                        // + " sconosciuto: " + newTypeId);
                        throw new DocerException("TYPE_ID " + newTypeId + " non e' definito per Ente " + oldCodEnte + " e AOO " + oldCodAOO);
                    }

                    // Controllo che il document type e il tipo componente siano compatibili
                    if (!checkComponentType(metadata.get("TIPO_COMPONENTE"), docType))
                        throw new DocerException("Document Type " + newTypeId + " non e' definito per il tipo componente " + metadata.get("TIPO_COMPONENTE"));

                    // if (!newDocType.isDefinedForAOO(oldCodEnte, oldCodAOO))
                    // throw new DocerException("TYPE_ID " + newTypeId +
                    // " non e' definito per Ente " + oldCodEnte + " e AOO " +
                    // oldCodAOO);

                    metadata.put(Constants.doc_type_id, newTypeId);

                    docType = newDocType;

                }

            }

            // se e' URL non devo verificare modifica estensione
            String oldArchiveType = oldProfileData.get(Constants.doc_archive_type);
            if (oldArchiveType == null) {
                oldArchiveType = "";
            }
            // controllo dei campi
            Map<String, String> cleanMetadata = checkPropertiesUpdateProfile(oldProfileData, metadata);

            FieldDescriptor fd = null;
            // controllo se i campi specificati appartengono al documenttype
            for (String key : cleanMetadata.keySet()) {

                fd = docType.getFieldDescriptor(key);

                if (fd == null) {
                    throw new DocerException("il campo " + key + " non appartiene al tipo " + docType.getTypeId());
                }

                String newValue = cleanMetadata.get(key);
                String oldValue = oldProfileData.get(key);

                if (newValue == null) {
                    newValue = "";
                }
                if (oldValue == null) {
                    oldValue = "";
                }

                // se il valore non e' cambiato non verifico nuovamente il
                // formato (BUG degli update con i profili restituiti)
                if (newValue.equals(oldValue)) {
                    continue;
                }

                // controllo lunghezza e formato dei metadati che cambiano
                cleanMetadata.put(key, fd.checkValueFormat(newValue));

                // controllo sul docname spostato su
                // checkPropertiesUpdateProfile
                // if (key.equalsIgnoreCase(Constants.doc_docname)) {
                // if (StringUtils.isEmpty(newValue)) {
                // throw new
                // DocerException("non e' possibile assegnare il campo " +
                // Constants.doc_docname + " null o vuoto");
                // }
                //
                // String newExtension = getExtension(newValue);
                // String oldExtension = getExtension(oldValue);
                //
                // if (!oldArchiveType.equalsIgnoreCase("URL") &&
                // !newExtension.equalsIgnoreCase(oldExtension)) {
                // throw new
                // DocerException("non e' possibile modificare la parte estensione nel campo "
                // + Constants.doc_docname +
                // ": utilizzare versionamento avanzato");
                // }
                // }

            }

            // // controllo dei campi spostato sopra
            // checkPropertiesUpdateProfile(oldProfileData, metadata);

            // controlliamo il docname: spesso rimandano erroneamente anche
            // l'estensione
            // String oldDocName = oldProfileData.get(Constants.doc_docname);
            // String oldExtension = getExtension(oldDocName);
            //
            // if (cleanMetadata.containsKey(Constants.doc_docname)) {
            //
            // String newDocName = cleanMetadata.get(Constants.doc_docname);
            // if (newDocName == null || newDocName.equals(VOID)) {
            // throw new DocerException("non e' possibile assegnare il campo " +
            // Constants.doc_docname + " null o vuoto");
            // }
            //
            // String newExtension = getExtension(newDocName);
            //
            // if (!newExtension.equalsIgnoreCase(oldExtension)) {
            // throw new
            // DocerException("non e' possibile modificare la parte estensione nel campo "
            // + Constants.doc_docname + ": utilizzare versionamento avanzato");
            // }
            // }

            // controllo di esistenza delle anagrafiche custom
            // checkAnagraficheCustom(oldCodEnte, oldCodAOO, cleanMetadata);
            // controllo l'esistenza delle anagrafiche dei Multivalues
            // checkAnagraficheMultivalue(oldCodEnte, oldCodAOO, docType,
            // cleanMetadata);

            checkAnagraficheCustom(oldCodEnte, oldCodAOO, docType, cleanMetadata);

            // aggiorno il profilo del documento principale
            provider.updateProfileDocument(docId, cleanMetadata);

        } catch (DocerException docEx) {

            throw new DocerException(443, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(543, err.getMessage());
        }

    }

    // 44
    public void setACLDocument(String ticket, String docId, Map<String, EnumACLRights> acls) throws DocerException {

        try {

            if (acls == null)
                throw new DocerException("acls null");

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);
            searchCriteria.put(Constants.doc_docnum, criteria);

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_docnum);
            returnProperties.add(Constants.doc_stato_archivistico);
            returnProperties.add(Constants.doc_tipo_componente);
            returnProperties.add(Constants.doc_stato_pantarei);
            returnProperties.add(Constants.doc_type_id);

            // test della library

            // RICERCA PRIMARIA
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato perche' devo modificare un solo
            // documento
            if (searchResults.getRows().size() == 0)
                throw new DocerException("documento " + docId + " non trovato");
            if (searchResults.getRows().size() != 1)
                throw new DocerException("documento non univoco");

            // ILockStatus checkedOutInfo =
            // provider.isCheckedOutDocument(docId);
            // if (checkedOutInfo.getLocked()) // &&
            // // !checkedOutInfo.extractUserProfile().equalsIgnoreCase(userid))
            // throw new
            // DocerException("documento in stato di blocco esclusivo");

            // HO TROVATO IL DOCUMENTO -> un solo risultato
            DataRow<String> oldProfileData = searchResults.getRow(0);

            // type_id
            String masterType_id = oldProfileData.get(Constants.doc_type_id);

            // stato_pantarei
            EnumStatiPantarei statoPantarei = getEnumStatoPantarei(oldProfileData.get(Constants.doc_stato_pantarei));

            // stato_archivistico
            EnumStatoArchivistico statoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));

            // controlla lo stato pantarei del documento
            // checkStatoPantarei(statoArchivistico, statoArchivistico);

            if (statoArchivistico.equals(EnumStatoArchivistico.undefined))
                throw new DocerException("Errore STATO_ARCHIVISTICO del documento: undefined");

            EnumTipiComponente tipoComponente = getEnumTipiComponente(oldProfileData.get(Constants.doc_tipo_componente));
            if (statoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode() && tipoComponente.equals(EnumTipiComponente.ALLEGATO))
                throw new DocerException("non e' possibile impostare esplicitamente ACL a documenti con STATO_ARCHIVISTICO " + statoArchivistico + " (" + statoArchivistico.getCode() + ") e "
                        + Constants.doc_tipo_componente + " " + tipoComponente);

            if (tipoComponente.equals(EnumTipiComponente.ALLEGATO) && statoPantarei.equals(EnumStatiPantarei.allegato))
                throw new DocerException("compatibilita' DOCAREA: non e' possibile impostare esplicitamente ACL a documenti con TIPO_COMPONENTE ALLEGATO e STATO_PANTAREI allegato (6)");

            // rimuovo le deny e le remove dalle ACL
            Map<String, EnumACLRights> newACL = getNewACLForSetACL(acls);
            // newACL.put(userid.toLowerCase(), EnumACLRights.fullAccess);

            //STEFANO: aggiungere una flag per impostare la lista dei type_id che permettono la propagazione delle acl sempre
            boolean allow_propagate_acl = false;
            List<String> allow_propagate_list = BL_CONFIGURATIONS.getConfig(entetoken).isAllow_propagate_acl();

            if (allow_propagate_list.contains("*") || allow_propagate_list.contains(masterType_id))
                allow_propagate_acl = true;

            // recupero lista dei related
            if ((statoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode() || allow_propagate_acl==true) && tipoComponente.equals(EnumTipiComponente.PRINCIPALE)) {

                List<String> relatedChain = provider.getRelatedDocuments(docId);
                // rimuovo dalla lista il MASTER (nel caso il DMS lo
                // restituisca)
                relatedChain.remove(docId);



                // aggiorno ACL dei related solo se record
                if (statoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode() || allow_propagate_acl==true) {
                    updateRelatedACL(relatedChain, newACL);
                }
            }

            // aggiornamento ACL del documento principale
            provider.setACLDocument(docId, newACL);

        } catch (DocerException docEx) {
            throw new DocerException(444, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(544, err.getMessage());
        }

    }

    // 45
    public Map<String, EnumACLRights> getACLDocument(String ticket, String docId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);
            searchCriteria.put(Constants.doc_docnum, criteria);

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_docnum);

            // test della library

            // // RICERCA PRIMARIA
            // DataTable<String> searchResults = provider.searchDocuments(
            // searchCriteria, null, returnProperties,
            // PRIMARYSEARCH_MAX_ROWS, null);
            //
            // // deve esserci un solo risultato
            // if (searchResults.getRows().size() == 0)
            // throw new DocerException("documento " +docId +" non trovato");
            // if (searchResults.getRows().size() != 1)
            // throw new DocerException("docId " +docId +" non univoco");

            acls = provider.getACLDocument(docId);

            return acls;
        } catch (DocerException docEx) {

            throw new DocerException(445, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(545, err.getMessage());
        }

    }

    // 46
    public void addRelated(String ticket, String docId, List<String> toAddRelated) throws DocerException {

        try {

            if (StringUtils.isEmpty(ticket))
                throw new DocerException("ticket null");

            if (StringUtils.isEmpty(docId))
                throw new DocerException("docId null");

            if (toAddRelated == null)
                throw new DocerException("lista related null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            if (toAddRelated.size() < 1) {
                return;
            }

            boolean listIsEmpty = true;
            for (String rel : toAddRelated) {
                // controllo abbiano mandato una lista consistente
                if (StringUtils.isEmpty(rel) || rel.equals(docId)) {
                    continue;
                }

                listIsEmpty = false;
                break;
            }

            if (listIsEmpty) {
                return;
            }

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);
            searchCriteria.put(Constants.doc_docnum, criteria);

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_docnum);
            returnProperties.add(Constants.doc_docnum_princ);
            returnProperties.add(Constants.doc_cod_ente);
            returnProperties.add(Constants.doc_cod_aoo);
            returnProperties.add(Constants.doc_stato_archivistico);
            returnProperties.add(Constants.doc_stato_pantarei);
            returnProperties.add(Constants.doc_tipo_componente);
            returnProperties.add(Constants.doc_classifica);
            returnProperties.add(Constants.doc_cod_titolario);
            returnProperties.add(Constants.doc_progr_fascicolo);
            returnProperties.add(Constants.doc_num_fascicolo);
            returnProperties.add(Constants.doc_anno_fascicolo);
            returnProperties.add(Constants.doc_fascicoli_secondari);
            returnProperties.add(Constants.doc_archive_type);
            returnProperties.add(Constants.doc_pubblicazione_pubblicato);
            returnProperties.add(Constants.doc_type_id);
            //aggiunto ritorno pianoClass
            returnProperties.add(Constants.doc_piano_class);

            // RICERCA PRIMARIA
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato perche' devo modificare un solo
            // documento
            if (searchResults.getRows().size() == 0)
                throw new DocerException("documento " + docId + " non trovato");
            if (searchResults.getRows().size() != 1)
                throw new DocerException("docId " + docId + " non univoco");

            // il profilo del documento oggetto del metodo
            DataRow<String> profile = searchResults.getRow(0);
            String pianoClassPrincipale  = profile.get(Constants.doc_piano_class);


            // String codEnte = profile.get(Constants.doc_cod_ente);
            // String codAOO = profile.get(Constants.doc_cod_aoo);
            // EnumStatiPantarei statoPantarei =
            // getEnumStatoPantarei(profile.get(Constants.doc_stato_pantarei));
            // EnumStatoArchivistico statoArchivistico =
            // getEnumStatoArchivistico(profile.get(Constants.doc_stato_archivistico));
            // EnumTipiComponente tipoComponente =
            // getEnumTipiComponente(profile.get(Constants.doc_tipo_componente));
            // boolean pubblicato =
            // Boolean.parseBoolean(profile.get(Constants.doc_pubblicazione_pubblicato));
            // String archiveType = profile.get(Constants.doc_archive_type);

            // recupero la catena corrente dei related al documento
            List<String> relatedChain = provider.getRelatedDocuments(docId);
            relatedChain.add(docId);

            // creo una lista dove un docId sia presente una sola volta
            Set<String> currentRelatedChain = new HashSet<String>();
            currentRelatedChain.addAll(relatedChain);

            Set<String> toaddRelatedChain = new HashSet<String>();
            toaddRelatedChain.addAll(toAddRelated);

            // dalla collezione da aggiungere rimuovo quelli presenti nella
            // current
            toaddRelatedChain.removeAll(currentRelatedChain);

            // imposto i criteri di ricerca
            criteria.clear();
            criteria.addAll(currentRelatedChain);
            // criteria.addAll(toaddRelatedChain);

            // per impostare i criteri di ricerca devo considerare tutti i
            // related (anche quelli delle catene dei related che aggiungo)
            for (String relatedToAdd : toaddRelatedChain) {

                // gia' l'ho inserito perche' era related di un related da
                // aggiungere
                if (criteria.contains(relatedToAdd)) {
                    continue;
                }

                List<String> relatedChainOfRelatedToAdd = provider.getRelatedDocuments(relatedToAdd);
                if (!relatedChainOfRelatedToAdd.contains(relatedToAdd)) {
                    relatedChainOfRelatedToAdd.add(relatedToAdd);
                }

                for (String r : relatedChainOfRelatedToAdd) {
                    if (!criteria.contains(r)) {
                        criteria.add(r);
                    }
                }

            }

            searchCriteria.put(Constants.doc_docnum, criteria);

            // ricerco tutti i documenti coinvolti nell'operazione
            searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
            if (searchResults == null || searchResults.getRows().size() == 0) {
                throw new DocerException("nessun documento trovato: " + criteria.toString());
            }

            if (searchResults.getRows().size() != criteria.size()) {
                for (DataRow<String> dr : searchResults.getRows()) {
                    String id = dr.get(Constants.doc_docnum);
                    criteria.remove(id);
                }

                throw new DocerException("documenti non trovati: " + criteria.toString());
            }

            DataRow<String> masterProfile = null;

            List<String> principali = new ArrayList<String>();

            // recuperiamo informazioni sul master
            for (DataRow<String> relatedProfile : searchResults.getRows()) {

                String relDocId = relatedProfile.get(Constants.doc_docnum);
                EnumStatoArchivistico relStatoArchivistico = getEnumStatoArchivistico(relatedProfile.get(Constants.doc_stato_archivistico));
                // String relCodEnte =
                // relatedProfile.get(Constants.doc_cod_ente);
                // String relCodAOO = relatedProfile.get(Constants.doc_cod_aoo);
                EnumStatiPantarei relStatoPantarei = getEnumStatoPantarei(relatedProfile.get(Constants.doc_stato_pantarei));
                // boolean relPubblicato =
                // Boolean.parseBoolean(relatedProfile.get(Constants.doc_pubblicazione_pubblicato));
                EnumTipiComponente relTipoComponente = getEnumTipiComponente(relatedProfile.get(Constants.doc_tipo_componente));
                // String relArchiveType =
                // relatedProfile.get(Constants.doc_archive_type);
                // String relClassifica = null;
                // String relCodTitolario = null;
                // String relProgrFascicolo = null;
                // String relNumFascicolo = null;
                // String relAnnoFascicolo = null;
                // String relFascicoliSecondari = null;

                if (relTipoComponente.equals(EnumTipiComponente.PRINCIPALE)) {

                    if (principali.contains(relDocId)) {
                        continue;
                    }

                    principali.add(relDocId);
                    // se e' un record ed e' un master
                    if (relStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
                        masterProfile = relatedProfile;
                    }
                }

            }

            if (principali.size() > 1) {
                throw new DocerException("operazione non consentita: la nuova catena dei related conterrebbe " + principali.size() + " documenti con TIPO_COMPONENTE=PRINCIPALE: "
                        + principali.toString());
            }

            // non esiste alcun master quindi il mio master di riferimento e'
            // proprio docId
            if (masterProfile == null) {
                masterProfile = profile;
            }

            String masterDocId = masterProfile.get(Constants.doc_docnum);
            EnumStatoArchivistico masterStatoArchivistico = getEnumStatoArchivistico(masterProfile.get(Constants.doc_stato_archivistico));
            String masterCodEnte = masterProfile.get(Constants.doc_cod_ente);
            String masterCodAOO = masterProfile.get(Constants.doc_cod_aoo);
            EnumStatiPantarei masterStatoPantarei = getEnumStatoPantarei(masterProfile.get(Constants.doc_stato_pantarei));
            boolean masterPubblicato = Boolean.parseBoolean(masterProfile.get(Constants.doc_pubblicazione_pubblicato));
            EnumTipiComponente masterTipoComponente = getEnumTipiComponente(masterProfile.get(Constants.doc_tipo_componente));
            String masterArchiveType = masterProfile.get(Constants.doc_archive_type);
            String masterClassifica = masterProfile.get(Constants.doc_classifica);
            String masterCodTitolario = masterProfile.get(Constants.doc_cod_titolario);
            String masterProgrFascicolo = masterProfile.get(Constants.doc_progr_fascicolo);
            String masterNumFascicolo = masterProfile.get(Constants.doc_num_fascicolo);
            String masterAnnoFascicolo = masterProfile.get(Constants.doc_anno_fascicolo);
            String masterFascSecondari = masterProfile.get(Constants.doc_fascicoli_secondari);

            Set<String> masterRelatedChain = currentRelatedChain;
            if (!masterDocId.equals(docId)) {
                masterRelatedChain = new HashSet<String>(provider.getRelatedDocuments(masterDocId));
                if (!masterRelatedChain.contains(masterDocId)) {
                    masterRelatedChain.add(masterDocId);
                }
            }

            boolean isRecord = (masterStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode());

            if (masterStatoArchivistico.equals(EnumStatoArchivistico.in_archivio_di_deposito)) {
                throw new DocerException("il documento " + masterDocId + " ha STATO_ARCHIVISTICO " + masterStatoArchivistico + " (" + masterStatoArchivistico + ") e non ammette aggiunta di related");
            }

            // modifica del 14/12/2012
            if (masterPubblicato && !BL_CONFIGURATIONS.getConfig(entetoken).isAllow_pubblicato_add_related()) { // sotto
                // flag
                if (!masterArchiveType.equals("PAPER")) {
                    throw new DocerException("il documento " + masterDocId + " e' PUBBLICATO e ha " + Constants.doc_archive_type + "=" + masterArchiveType + " e non ammette aggiunta di related");
                }
            }

            // controllo i profili dei related
            for (DataRow<String> relatedProfile : searchResults.getRows()) {

                String relDocId = relatedProfile.get(Constants.doc_docnum);
                if (relDocId.equals(masterDocId)) {
                    continue;
                }
                checkRelated(relatedProfile, masterRelatedChain.contains(relDocId), masterProfile);
            }

            Map<String, EnumACLRights> masterACL = null;
            if (masterStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
                masterACL = provider.getACLDocument(masterDocId);
            }
            Map<String, EnumACLRights> relatedACL = null;

            List<String> rels = new ArrayList<String>();

            // aggiorno acl, aggiungo alla catena e aggiorno profilo related
            for (DataRow<String> relatedProfile : searchResults.getRows()) {

                String relDocId = relatedProfile.get(Constants.doc_docnum);

                if (relDocId.equals(masterDocId)) {
                    continue;
                }

                // verifico se hanno stesse ACL e nel caso le allineo
                if (masterACL != null) {

                    relatedACL = provider.getACLDocument(relDocId);

                    if (!mapsAreEqual(masterACL, relatedACL)) {
                        rels.clear();
                        rels.add(relDocId);
                        updateRelatedACL(rels, masterACL);
                    }
                }

                // aggiungo un related alla volta
                if (!masterRelatedChain.contains(relDocId)) {
                    rels.clear();
                    rels.add(relDocId);
                    provider.addRelatedDocuments(docId, rels);

                    //controlla ed eventualmente aggiorna STATO_CONSERV
                    updateStatoConserv(docId, relDocId);
                }

                Map<String, String> relatedPropertiesToUpdate = checkIfRelatedNeedsProfileUpdate(relatedProfile, masterProfile);

                if (relatedPropertiesToUpdate != null) {
                    provider.updateProfileDocument(relDocId, relatedPropertiesToUpdate);
                }
            }

        } catch (DocerException docEx) {

            throw new DocerException(446, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(546, err.getMessage());
        }

    }

    // 47
    public void removeRelated(String ticket, String docId, List<String> toRemoveRelated) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            if (toRemoveRelated == null)
                throw new DocerException("lista related null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            List<String> removingList = new ArrayList<String>();

            for (String remElement : toRemoveRelated) {

                if (remElement == null || remElement.equals(VOID))
                    continue;

                if (remElement.equals(docId))
                    continue;

                if (removingList.contains(remElement))
                    continue;

                removingList.add(remElement);
            }

            if (removingList.size() < 1) {
                return;
            }

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);
            searchCriteria.put(Constants.doc_docnum, criteria);

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_docnum);
            returnProperties.add(Constants.doc_tipo_componente);
            returnProperties.add(Constants.doc_stato_archivistico);
            returnProperties.add(Constants.doc_stato_pantarei);

            // test della library

            // RICERCA PRIMARIA
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato perche' devo modificare un solo
            // documento
            if (searchResults.getRows().size() == 0)
                throw new DocerException("documento " + docId + " non trovato");
            if (searchResults.getRows().size() != 1)
                throw new DocerException("docId " + docId + " non univoco");

            // ILockStatus checkedOutInfo =
            // provider.isCheckedOutDocument(docId);
            // if (checkedOutInfo.getLocked()) {
            // throw new
            // DocerException("documento in stato di blocco esclusivo");
            // }

            // HO TROVATO IL DOCUMENTO -> un solo risultato
            DataRow<String> oldProfileData = searchResults.getRow(0);

            docId = oldProfileData.get(Constants.doc_docnum);

            // stato pantarei del documento da cui vogliamo rimuovere gli
            // allegati
            EnumStatiPantarei masterStatoPantarei = getEnumStatoPantarei(oldProfileData.get(Constants.doc_stato_pantarei));
            EnumStatoArchivistico masterStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));
            EnumTipiComponente masterTipoComponente = getEnumTipiComponente(oldProfileData.get(Constants.doc_tipo_componente));

            // non si possono rimuovere related ad un RECORD
            if (masterStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode())
                throw new DocerException("non si possono rimuovere related di un documento con STATO_ARCHIVISTICO " + masterStatoArchivistico + " (" + masterStatoArchivistico.getCode() + ")");

            if (masterStatoPantarei.getCode() > 3)
                throw new DocerException("compatibilita' DOCAREA: non si possono rimuovere related di un documento con STATO_PANTAREI " + masterStatoPantarei + " (" + masterStatoPantarei.getCode()
                        + ")");

            List<String> relatedChain = provider.getRelatedDocuments(docId);

            // se non ha related
            if (relatedChain.size() == 0) {
                return;
            }

            if (!relatedChain.contains(docId)) {
                relatedChain.add(docId);
            }

            // modifica delle relazioni con i related
            List<String> relToUpdate = checkRelatedChainWhenRemove(relatedChain);

            Map<String, String> relNewProperties = new HashMap<String, String>();
            relNewProperties.put(Constants.doc_docnum_princ, VOID);

            for (String toUpdateId : relToUpdate) {
                // aggiorniamo il profilo del related da rimuovere
                provider.updateProfileDocument(toUpdateId, relNewProperties);
            }

            provider.removeRelatedDocuments(docId, removingList);

        } catch (DocerException docEx) {

            throw new DocerException(447, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(547, err.getMessage());
        }

    }

    // 48
    public Map<String, String> getProfileDocument(String ticket, String docId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);
            searchCriteria.put(Constants.doc_docnum, criteria);

            // richiedo tutte le proprieta'
            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add("DOCNUM");
            returnProperties.add("TYPE_ID");
            returnProperties.add("COD_ENTE");
            returnProperties.add("COD_AOO");

            // RICERCA PRIMARIA
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato
            if (searchResults.getRows().size() == 0)
                throw new DocerException("documento " + docId + " non trovato");
            if (searchResults.getRows().size() != 1)
                throw new DocerException("docId " + docId + " non univoco");

            // HO TROVATO IL DOCUMENTO -> un solo risultato
            DataRow<String> profileData = searchResults.getRow(0);

            String documentTypeId = profileData.get(Constants.doc_type_id);
            String codEnte = profileData.get(Constants.doc_cod_ente);
            String codAOO = profileData.get(Constants.doc_cod_aoo);

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("ERRORE: documento " + docId + " senza COD_ENTE assegnato");

            if (codAOO == null) {
                codAOO = VOID;
            }

            if (documentTypeId == null || documentTypeId.equals(VOID))
                throw new DocerException("ERRORE: documento " + docId + " senza TYPE_ID assegnato");

            documentTypeId = documentTypeId.toUpperCase();

            DocumentType docType = BL_CONFIGURATIONS.getConfig(entetoken).getDocumentTypesMapping().get(documentTypeId, codEnte, codAOO);

            if (docType == null) {
                // throw new DocerException("type_id assegnato al documento " +
                // docId + " sconosciuto: " + documentTypeId);
                throw new DocerException("Errore di configurazione: TYPE_ID (" + documentTypeId + ") del documento " + docId + " non definito per Ente " + codEnte + " e AOO " + codAOO);
            }

            // if (!docType.isDefinedForAOO(codEnte, codAOO)) {
            // throw new DocerException("Errore di configurazione: TYPE_ID (" +
            // documentTypeId + ") del documento " + docId +
            // " non definito per Ente " + codEnte + " e AOO " + codAOO);
            // }

            returnProperties = docType.getFieldsNames();

            // RICERCA PRIMARIA
            searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato
            if (searchResults.getRows().size() == 0)
                throw new DocerException("documento " + docId + " non trovato");
            if (searchResults.getRows().size() != 1)
                throw new DocerException("docId " + docId + " non univoco");

            // HO TROVATO IL DOCUMENTO -> un solo risultato
            profileData = searchResults.getRow(0);

            // inserisco le descrizioni delle anagrafiche
            return formatProfile(profileData);

        } catch (DocerException docEx) {

            throw new DocerException(448, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(548, err.getMessage());
        }
    }

    // 49
    public byte[] downloadDocument(String ticket, String docId, String outputFileUniquePath, long maxFileLength) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // Ricerca documento con i criteri
            if (docId == null || docId.equals(VOID)) {
                throw new DocerException("docId obbligatorio"); // modificare in
                // docId
                // obbligatorio
            }

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);
            searchCriteria.put(Constants.doc_docnum, criteria);

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_docnum);
            returnProperties.add(Constants.doc_type_id);
            returnProperties.add(Constants.doc_archive_type);

            // test

            //
            // // RICERCA PRIMARIA
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato perche' devo modificare un solo
            // documento
            if (searchResults.getRows().size() == 0) {
                throw new DocerException("documento " + docId + " non trovato");
            }

            if (searchResults.getRows().size() != 1) {
                throw new DocerException("docId " + docId + " non univoco");
            }

            // HO TROVATO IL DOCUMENTO -> un solo risultato
            DataRow<String> profileData = searchResults.getRow(0);
            String archiveType = profileData.get(Constants.doc_archive_type);

            byte[] file = provider.downloadLastVersion(docId, outputFileUniquePath, maxFileLength);

            if (archiveType != null && archiveType.equalsIgnoreCase("URL")) {
                return new byte[0];
            }

            return file;

        } catch (DocerException docEx) {

            throw new DocerException(449, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(549, err.getMessage());
        }
    }

    // 50
    public byte[] downloadVersion(String ticket, String docId, String versionNumber, String outputFileUniquePath, long maxFileLength) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            if (versionNumber == null || versionNumber.equals(VOID))
                throw new DocerException("versionNumber null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // Ricerca documento con i criteri
            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId obbligatorio");

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);
            searchCriteria.put(Constants.doc_docnum, criteria);

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_docnum);
            returnProperties.add(Constants.doc_archive_type);

            // RICERCA PRIMARIA
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato perche' devo modificare un solo
            // documento
            if (searchResults.getRows().size() == 0) {
                throw new DocerException("documento " + docId + " non trovato");
            }

            if (searchResults.getRows().size() != 1) {
                throw new DocerException("docId " + docId + " non univoco");
            }

            // HO TROVATO IL DOCUMENTO -> un solo risultato
            DataRow<String> profileData = searchResults.getRow(0);
            String archiveType = profileData.get(Constants.doc_archive_type);

            byte[] file = provider.downloadVersion(docId, versionNumber, outputFileUniquePath, maxFileLength);

            if (archiveType != null && archiveType.equalsIgnoreCase("URL")) {
                return new byte[0];
            }

            return file;

        } catch (DocerException docEx) {

            throw new DocerException(450, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(550, err.getMessage());
        }
    }

    // 51
    public void replaceLastVersion(String ticket, String docId, InputStream file) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);
            if (checkedOutInfo.getLocked() && !checkedOutInfo.getUserId().equalsIgnoreCase(loginUserInfo.getUserId()))
                throw new DocerException("documento bloccato esclusivamente da un altro utente");

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);

            searchCriteria.put(Constants.doc_docnum, criteria);

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_docnum);
            returnProperties.add(Constants.doc_stato_archivistico);
            returnProperties.add(Constants.doc_archive_type);
            returnProperties.add(Constants.doc_stato_pantarei);
            returnProperties.add(Constants.doc_type_id);

            // RICERCA PRIMARIA
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato perche' devo modificare un solo
            // documento
            if (searchResults.getRows().size() == 0)
                throw new DocerException("documento " + docId + " non trovato");
            if (searchResults.getRows().size() != 1)
                throw new DocerException("docId " + docId + " non univoco");

            // HO TROVATO IL DOCUMENTO -> un solo risultato
            DataRow<String> profileData = searchResults.getRow(0);

            // String documentType = profileData.get(Constants.doc_type_id);

            // if (PAPER_DOCUMENTS.contains(documentType.toUpperCase()))
            // throw new DocerException("I documenti di tipo " + documentType
            // + " non ammettono file");

            String archiveType = profileData.get(Constants.doc_archive_type);
            if (archiveType == null || archiveType.equals(VOID)) {
                archiveType = "ARCHIVE";
            }

            if (archiveType.equalsIgnoreCase("URL")) {
                throw new DocerException("i documenti con ARCHIVE_TYPE=URL non ammettono modifica dell'ultima versione del file documento");
            }

            // stato_pantarei
            EnumStatoArchivistico statoArchivistico = getEnumStatoArchivistico(profileData.get(Constants.doc_stato_archivistico));

            EnumStatiPantarei statoPantarei = getEnumStatoPantarei(profileData.get(Constants.doc_stato_pantarei));

            if (statoArchivistico.getCode() > EnumStatoArchivistico.generico.getCode()) {

                if (archiveType.equals("PAPER")) {
                    throw new DocerException("DOCNUM " + docId + ", attenzione, il documento ARCHIVE_TYPE=PAPER ha STATO_ARCHIVISTICO " + statoArchivistico + " (" + statoArchivistico.getCode()
                            + ") e non ammette la sostituzione della versione corrente. Eventualmente e' possibile creare una nuova versione");
                }

                throw new DocerException("DOCNUM " + docId + ", attenzione, il documento ARCHIVE_TYPE=ARCHIVE ha STATO_ARCHIVISTICO " + statoArchivistico + " (" + statoArchivistico.getCode()
                        + ") e non ammette versioning");

                // modifica del 15 novembre 2013
                // modifica del 28/02/2013 --> i PAPER sono sempre versionabili
                // modifica del 14/01/2013
                // if (!archiveType.equals("PAPER")) {
                //
                // if (!allow_record_versioning_archive) {
                // throw new DocerException("DOCNUM " + docId +
                // ", attenzione, il documento ha STATO_ARCHIVISTICO " +
                // statoArchivistico + " (" + statoArchivistico.getCode()
                // +
                // ") e ammette modifica dell'ultima versione solo se ARCHIVE_TYPE=PAPER");
                // }
                //
                // // togliamo il controllo sullo stato pantarei perche' non
                // // piu' applicabile
                //
                // }

            }

            // e' richiesto anche il file documento oltre al profilo
            if (file == null)
                throw new DocerException("file null");

            if (file.available() == 0)
                throw new DocerException("file vuoto"); // File Attachment
            // Documento vuoto o
            // errata codifica
            // Base64 (MIME)

            provider.replaceLastVersion(docId, file);

        } catch (DocerException docEx) {

            throw new DocerException(451, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(551, err.getMessage());
        }

    }

    // 52
    public String addNewVersion(String ticket, String docId, InputStream file) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);

            if (checkedOutInfo.getLocked() && !checkedOutInfo.getUserId().equalsIgnoreCase(loginUserInfo.getUserId()))
                throw new DocerException("documento bloccato esclusivamente da un altro utente");

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);

            searchCriteria.put(Constants.doc_docnum, criteria);

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_cod_ente);
            returnProperties.add(Constants.doc_docnum);
            returnProperties.add(Constants.doc_stato_archivistico);
            returnProperties.add(Constants.doc_stato_pantarei);
            returnProperties.add(Constants.doc_type_id);
            // aggiunto il 14/12/2012
            returnProperties.add(Constants.doc_archive_type);
            returnProperties.add(Constants.doc_tipo_componente);
            // RICERCA PRIMARIA
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato perche' devo modificare un solo
            // documento
            if (searchResults.getRows().size() == 0)
                throw new DocerException("documento " + docId + " non trovato");
            if (searchResults.getRows().size() != 1)
                throw new DocerException("docId " + docId + " non univoco");

            // HO TROVATO IL DOCUMENTO -> un solo risultato
            DataRow<String> profileData = searchResults.getRow(0);

            EnumStatoArchivistico statoArchivistico = getEnumStatoArchivistico(profileData.get(Constants.doc_stato_archivistico));

            EnumStatiPantarei statoPantarei = getEnumStatoPantarei(profileData.get(Constants.doc_stato_pantarei));

            String archiveType = profileData.get(Constants.doc_archive_type);
            if (archiveType == null || archiveType.equals(VOID)) {
                archiveType = "ARCHIVE";
            }


            EnumTipiComponente thisTipoComponente = getEnumTipiComponente(profileData.get(Constants.doc_tipo_componente));

            if (archiveType.equalsIgnoreCase("URL")) {
                throw new DocerException("i documenti con ARCHIVE_TYPE=URL non ammettono versioning del file documento");
            }

            // ARCHIVE
            // PAPER
            // URL

            if(!(EnumTipiComponente.ANNESSO.equals(thisTipoComponente) || EnumTipiComponente.ANNOTAZIONE.equals(thisTipoComponente) )){
                if (statoArchivistico.getCode() > EnumStatoArchivistico.generico.getCode()) {

                    // modifica del 15/11/2013 --> i PAPER sono sempre versionabili
                    // modifica del 28/02/2013 --> i PAPER sono sempre versionabili
                    // modifica del 14/01/2013
                    if (!archiveType.equals("PAPER")) {

                        String cod_ente = profileData.get(Constants.doc_cod_ente);

                        if (!BL_CONFIGURATIONS.getConfig(entetoken).isAllow_record_versioning_archive()) {
                            throw new DocerException("DOCNUM " + docId + ", attenzione, il documento ARCHIVE_TYPE=ARCHIVE ha STATO_ARCHIVISTICO " + statoArchivistico + " (" + statoArchivistico.getCode()
                                    + ") e non ammette versioning");
                        }

                        // togliamo il controllo sullo stato pantarei perche' non
                        // piu' applicabile
                    }

                }
            }
            // e' richiesto anche il file documento oltre al profilo
            if (file == null)
                throw new DocerException("file null");

            if (file.available() == 0)
                throw new DocerException("file vuoto");

            String versionNumber = provider.addNewVersion(docId, file);

            return versionNumber;

        } catch (DocerException docEx) {

            throw new DocerException(452, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(552, err.getMessage());
        }

    }

    // 53
    public void lockDocument(String ticket, String docId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);

            if (checkedOutInfo.getLocked()) {
                if (!checkedOutInfo.getUserId().equalsIgnoreCase(loginUserInfo.getUserId())) {
                    throw new DocerException("documento bloccato esclusivamente da un altro utente");
                }
                // e' gia' bloccato
                return;
            }

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);
            searchCriteria.put(Constants.doc_docnum, criteria);

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_docnum);
            returnProperties.add(Constants.doc_stato_archivistico);
            returnProperties.add(Constants.doc_stato_pantarei);
            returnProperties.add(Constants.doc_archive_type);
            // test della library

            // RICERCA PRIMARIA
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato
            if (searchResults.getRows().size() == 0)
                throw new DocerException("documento " + docId + " non trovato");
            if (searchResults.getRows().size() != 1)
                throw new DocerException("docId " + docId + " non univoco");

            // NON POSSIAMO PROIBIRE IL LOCK PERCHE' NON SAPPIAMO SE POI
            // MODIFICHERANNO IL FILE O SOLO IL PROFILO!!!
            // HO TROVATO IL DOCUMENTO -> un solo risultato
            // DataRow<String> profileData = searchResults.getRow(0);
            //
            // // stato_pantarei
            // EnumStatoArchivistico statoArchivistico =
            // getEnumStatoArchivistico(profileData.get(Constants.doc_stato_archivistico));
            // EnumStatiPantarei statoPantarei =
            // getEnumStatoPantarei(profileData.get(Constants.doc_stato_pantarei));
            //
            // String archiveType = profileData.get(Constants.doc_archive_type);
            // if (archiveType == null || archiveType.equals(VOID)) {
            // archiveType = "ARCHIVE";
            // }
            //
            // if (statoArchivistico.getCode() >
            // EnumStatoArchivistico.generico.getCode()) {
            //
            // // modifica del 28/03/2013 --> i PAPER sono sempre versionabili
            // if (!archiveType.equals("PAPER")) {
            //
            // if (!allow_record_versioning_archive) {
            // throw new DocerException("DOCNUM " + docId +
            // ", attenzione, il documento ha STATO_ARCHIVISTICO " +
            // statoArchivistico + " (" + statoArchivistico.getCode() +
            // ") e ammette lock e modifica dell'ultima versione solo se ARCHIVE_TYPE=PAPER");
            // }
            //
            // // qui e' allow_record_versioning_archive = true
            // if (!archiveType.equalsIgnoreCase("ARCHIVE")) {
            // throw new DocerException("DOCNUM " + docId +
            // ", attenzione, il documento ha STATO_ARCHIVISTICO " +
            // statoArchivistico + " (" + statoArchivistico.getCode() +
            // ") e ammette lock e modifica dell'ultima versione solo se ARCHIVE_TYPE=PAPER oppure ARCHIVE_TYPE=ARCHIVE (o vuoto)");
            // }
            // }
            //
            // }

            // if (statoArchivistico.getCode() >
            // EnumStatoArchivistico.generico.getCode())
            // throw new
            // DocerException("non e' permesso eseguire il blocco esclusivo di un documento con STATO_ARCHIVISTICO "
            // + statoArchivistico + " (" + statoArchivistico.getCode() + ")");
            //
            // if (statoPantarei.getCode() > 3) {
            // throw new
            // DocerException("compatibilita' DOCAREA: non e' permesso eseguire il blocco esclusivo di un documento con STATO_PANTAREI "
            // + statoPantarei + " (" + statoPantarei.getCode() + ")");
            // }

            // eseguo il lock
            provider.lockDocument(docId);

        } catch (DocerException docEx) {

            throw new DocerException(453, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(553, err.getMessage());
        }

    }

    // 54
    public void unlockDocument(String ticket, String docId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);

            if (!checkedOutInfo.getLocked()) {
                // throw new
                // DocerException("documento non in stato di blocco esclusivo");
                return;
            }

            // if
            // (!checkedOutInfo.extractUserProfile().equalsIgnoreCase(loginUserInfo.extractUserProfile())
            // && !provider.userIsManager(loginUserInfo.extractUserProfile())) //
            // l'unlock lo
            // // permettiamo comunque
            // // agli amministratori
            // throw new DocerException(
            // "documento bloccato esclusamente da un altro utente");

            // eseguo il lock
            provider.unlockDocument(docId);

        } catch (DocerException docEx) {

            throw new DocerException(454, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(554, err.getMessage());
        }

    }

    // 55
    public ILockStatus getLockStatus(String ticket, String docId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            return provider.isCheckedOutDocument(docId);

        } catch (DocerException docEx) {

            throw new DocerException(455, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(555, err.getMessage());
        }
    }

    // 56
    public List<String> getRelatedDocuments(String ticket, String docId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            List<String> related = provider.getRelatedDocuments(docId);
            related.remove(docId);
            return related;

        } catch (DocerException docEx) {

            throw new DocerException(456, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(556, err.getMessage());
        }
    }

    // 57
    public void deleteDocument(String ticket, String docId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            // impostazione dell'unico criterio di ricerca
            List<String> criteria = new ArrayList<String>();
            criteria.add(docId);
            searchCriteria.put(Constants.doc_docnum, criteria);

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_docnum);
            returnProperties.add(Constants.doc_stato_archivistico);

            // test della library

            ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);

            if (checkedOutInfo.getLocked())
                throw new DocerException("documento in stato di blocco esclusivo");

            // RICERCA PRIMARIA
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato
            if (searchResults.getRows().size() == 0)
                throw new DocerException("documento " + docId + " non trovato");
            if (searchResults.getRows().size() != 1)
                throw new DocerException("docId " + docId + " non univoco");

            // HO TROVATO IL DOCUMENTO -> un solo risultato
            DataRow<String> profileData = searchResults.getRow(0);

            EnumStatiPantarei statoPantarei = getEnumStatoPantarei(profileData.get(Constants.doc_stato_pantarei));

            // stato_archivistico
            EnumStatoArchivistico statoArchivistico = getEnumStatoArchivistico(profileData.get(Constants.doc_stato_archivistico));

            if (statoArchivistico.getCode() > EnumStatoArchivistico.generico.getCode()) {
                throw new DocerException("non e' permesso eliminare documenti con STATO_ARCHIVISTICO " + statoArchivistico + " (" + statoArchivistico.getCode() + ")");
            }

            if (statoPantarei.getCode() > 3)
                throw new DocerException("compatibilita' DOCAREA: non e' permesso eliminare documenti con STATO_PANTAREI " + statoPantarei + " (" + statoPantarei.getCode() + ")");

            // esecuzione della cancellazione
            provider.deleteDocument(docId);

        } catch (DocerException docEx) {

            throw new DocerException(457, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(557, err.getMessage());
        }

    }

    // 58
    public List<String> getVersions(String ticket, String docId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            return provider.getVersions(docId);

        } catch (DocerException docEx) {

            throw new DocerException(458, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(558, err.getMessage());
        }
    }

    // 59
    public EnumACLRights getUserRights(String ticket, String docId, String userId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            if (userId == null || userId.equals(VOID))
                throw new DocerException("userId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            return provider.getEffectiveRights(docId, userId);

        } catch (DocerException docEx) {

            throw new DocerException(459, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(559, err.getMessage());
        }

    }

    // 60
    public List<IHistoryItem> getHistory(String ticket, String docId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            return provider.getHistory(docId);

        } catch (DocerException docEx) {

            throw new DocerException(460, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(560, err.getMessage());
        }

    }

    // ************** ANAGRAFICHE ********************

    // 61
    public void createEnte(String ticket, Map<String, String> info) throws DocerException {

        clearCache();

        try {

            info = toUCMap(info, " info");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            String codEnte = info.get(Constants.ente_cod_ente);

            AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(Constants.ente_type_id, null, null);
            if (anagraficaType == null)
                throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.ente_type_id);

            String desEnte = info.get(Constants.ente_des_ente);
            String enabled = info.get(Constants.ente_enabled);
            if (enabled == null)
                enabled = "true";

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("Verificare i campi obbligatori (COD_ENTE, DES_ENTE)");

            if (desEnte == null || desEnte.equals(VOID))
                throw new DocerException("Verificare i campi obbligatori (COD_ENTE, DES_ENTE)");

            // Map<String, String> p = getEnteProfile(codEnte);
            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.ente_cod_ente, codEnte);
            Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCrit);
            if (p != null)
                throw new DocerException("Ente " + codEnte + " esistente");

            IEnteInfo enteInfo = new EnteInfo();
            enteInfo.setEnabled(getEnumBoolean(enabled));
            enteInfo.setCodiceEnte(codEnte.toUpperCase());
            enteInfo.setDescrizione(desEnte);

            FieldDescriptor fd = null;
            for (String fieldName : info.keySet()) {

                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }

                fd = anagraficaType.getFieldDescriptor(fieldName);
                if (fd == null) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica ENTE");
                }

                String propValue = fd.checkValueFormat(info.get(fieldName));
                if (fieldName.equalsIgnoreCase(Constants.ente_cod_ente)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.ente_des_ente)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.ente_enabled)) {
                    continue;
                }

                enteInfo.getExtraInfo().put(fd.getPropName(), propValue);
            }

            provider.createEnte(enteInfo);

        } catch (DocerException docEx) {

            throw new DocerException(461, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(561, err.getMessage());
        }
    }

    // 62
    public void updateEnte(String ticket, String codiceEnte, Map<String, String> info) throws DocerException {

        clearCache();

        try {

            info = toUCMap(info, "info");

            if (codiceEnte == null || codiceEnte.equals(VOID))
                throw new DocerException("specificare COD_ENTE da modificare");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(Constants.ente_type_id, null, null);
            if (anagraficaType == null)
                throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.ente_type_id);

            // String newCodEnte = info.get(Constants.ente_cod_ente);

            IEnteId enteid = new EnteId();
            enteid.setCodiceEnte(codiceEnte);

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.ente_cod_ente, codiceEnte);
            idCrit.put(Constants.custom_enabled, "*");

            Map<String, String> pente = getAnagraficaProfile(Constants.ente_type_id, idCrit);
            if (pente == null)
                throw new DocerException("Ente " + codiceEnte + " non trovato");

            IEnteInfo enteInfo = new EnteInfo();

            FieldDescriptor fd = null;
            for (String fieldName : info.keySet()) {

                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }

                fd = anagraficaType.getFieldDescriptor(fieldName);
                if (fd == null) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica ENTE");
                }

                String newPropValue = fd.checkValueFormat(info.get(fieldName));
                String oldPropValue = pente.get(fieldName);

                if (fieldName.equalsIgnoreCase(Constants.ente_cod_ente)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare " + Constants.ente_cod_ente);

                    continue;
                }

                if (fieldName.equalsIgnoreCase(Constants.ente_des_ente)) {
                    enteInfo.setDescrizione(newPropValue);
                }

                if (fieldName.equalsIgnoreCase(Constants.ente_enabled)) {
                    enteInfo.setEnabled(getEnumBoolean(newPropValue));
                    continue;
                }

                enteInfo.getExtraInfo().put(fieldName, newPropValue);
            }

            // modifica ente
            provider.updateEnte(enteid, enteInfo);

        } catch (DocerException docEx) {

            throw new DocerException(462, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(562, err.getMessage());
        }

    }

    // 63
    public Map<String, String> getEnte(String ticket, String codiceEnte) throws DocerException {

        try {

            if (codiceEnte == null)
                throw new DocerException("anagrafica id null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            if (codiceEnte == null || codiceEnte.equals(VOID))
                throw new DocerException("specificare COD_ENTE");

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.ente_cod_ente, codiceEnte);
            Map<String, String> profile = getAnagraficaProfile(Constants.ente_type_id, idCrit);

            if (profile == null) {
                throw new DocerException("Ente " + codiceEnte + " non trovato");
            }

            return profile;

        } catch (DocerException docEx) {

            throw new DocerException(463, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(563, err.getMessage());
        }

    }

    // 64
    public void createAOO(String ticket, Map<String, String> info) throws DocerException {

        clearCache();

        try {

            info = toUCMap(info, "info");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            String codEnte = info.get(Constants.aoo_cod_ente);
            String codAoo = info.get(Constants.aoo_cod_aoo);

            AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(Constants.aoo_type_id, codEnte, codAoo);
            if (anagraficaType == null)
                throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.aoo_type_id + " per COD_ENTE: " + codEnte + " e COD_AOO: " + codAoo);

            String desAoo = info.get(Constants.aoo_des_aoo);
            String enabled = info.get(Constants.aoo_enabled);
            if (enabled == null)
                enabled = "true";

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_AOO);

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_AOO);

            if (desAoo == null || desAoo.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_AOO);

            IEnteId enteid = new EnteId();
            enteid.setCodiceEnte(codEnte);

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.ente_cod_ente, codEnte);
            Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCrit);
            if (p == null)
                throw new DocerException("Ente " + codEnte + " non trovato");

            idCrit.put(Constants.aoo_cod_aoo, codAoo);

            Map<String, String> paoo = getAnagraficaProfile(Constants.aoo_type_id, idCrit);
            if (paoo != null)
                throw new DocerException("AOO " + idCrit.toString() + " esistente");

            IAOOInfo aooInfo = new AOOInfo();
            aooInfo.setEnabled(getEnumBoolean(enabled));
            aooInfo.setCodiceEnte(codEnte);
            aooInfo.setCodiceAOO(codAoo);
            aooInfo.setDescrizione(desAoo);

            FieldDescriptor fd = null;
            for (String fieldName : info.keySet()) {

                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }

                fd = anagraficaType.getFieldDescriptor(fieldName);
                if (fd == null) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica AOO");
                }

                String propValue = fd.checkValueFormat(info.get(fieldName));

                if (fieldName.equalsIgnoreCase(Constants.aoo_cod_ente)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.aoo_cod_aoo)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.aoo_des_aoo)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.aoo_enabled)) {
                    continue;
                }

                aooInfo.getExtraInfo().put(fd.getPropName(), propValue);
            }

            provider.createAOO(aooInfo);

        } catch (DocerException docEx) {

            throw new DocerException(464, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(564, err.getMessage());
        }
    }

    // 65
    public void updateAOO(String ticket, Map<String, String> id, Map<String, String> info) throws DocerException {

        clearCache();

        try {

            id = toUCMap(id, "id");
            info = toUCMap(info, "info");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            String codEnte = id.get(Constants.aoo_cod_ente);

            String codAoo = id.get(Constants.aoo_cod_aoo);

            AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(Constants.aoo_type_id, codEnte, codAoo);
            if (anagraficaType == null)
                throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.aoo_type_id + " per COD_ENTE: " + codEnte + " e COD_AOO: " + codAoo);

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("AOO non trovata: specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("AOO non trovata: specificare COD_AOO");

            IAOOId aooId = new AOOId();
            aooId.setCodiceEnte(codEnte);
            aooId.setCodiceAOO(codAoo);

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.aoo_cod_ente, codEnte);
            idCrit.put(Constants.aoo_cod_aoo, codAoo);

            idCrit.put(Constants.custom_enabled, "*");

            Map<String, String> paoo = getAnagraficaProfile(Constants.aoo_type_id, idCrit);
            if (paoo == null)
                throw new DocerException("AOO " + idCrit.toString() + " non trovata");

            IAOOInfo aooInfo = new AOOInfo();

            FieldDescriptor fd = null;
            for (String fieldName : info.keySet()) {

                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }

                fd = anagraficaType.getFieldDescriptor(fieldName);
                if (fd == null) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica AOO");
                }

                // info.put(fieldName,
                // fd.checkValueFormat(info.get(fieldName)));
                String newPropValue = fd.checkValueFormat(info.get(fieldName));
                String oldPropValue = paoo.get(fieldName);

                if (fieldName.equalsIgnoreCase(Constants.aoo_cod_ente)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare " + Constants.aoo_cod_ente);

                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.aoo_cod_aoo)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare " + Constants.aoo_cod_aoo);

                    continue;
                }

                if (fieldName.equalsIgnoreCase(Constants.aoo_des_aoo)) {
                    aooInfo.setDescrizione(newPropValue);
                    continue;
                }

                if (fieldName.equalsIgnoreCase(Constants.aoo_enabled)) {
                    aooInfo.setEnabled(getEnumBoolean(newPropValue));
                    continue;
                }

                aooInfo.getExtraInfo().put(fieldName, newPropValue);
            }

            provider.updateAOO(aooId, aooInfo);

        } catch (DocerException docEx) {

            throw new DocerException(465, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(565, err.getMessage());
        }
    }

    // 66
    public Map<String, String> getAOO(String ticket, Map<String, String> id) throws DocerException {

        try {

            id = toUCMap(id, "id");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            String codEnte = id.get(Constants.aoo_cod_ente);
            String codAoo = id.get(Constants.aoo_cod_aoo);

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("specificare COD_AOO");

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.aoo_cod_ente, codEnte);
            idCrit.put(Constants.aoo_cod_aoo, codAoo);

            Map<String, String> profile = getAnagraficaProfile(Constants.aoo_type_id, idCrit);

            if (profile == null) {
                throw new DocerException("AOO " + idCrit.toString() + " non trovata");
            }

            return profile;

        } catch (DocerException docEx) {

            throw new DocerException(466, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(566, err.getMessage());
        }
    }

    // 70
    public void createTitolario(String ticket, Map<String, String> info) throws DocerException {

        try {
            info = toUCMap(info, "info");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            String codEnte = info.get(Constants.titolario_cod_ente);
            String codAoo = info.get(Constants.titolario_cod_aoo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;
            String pianoClass = isPianoClassEnabled ?
                    (info.get(Constants.titolario_piano_class) != null ? info.get(Constants.titolario_piano_class) : pianoClassDefault)
                    : null;

            AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(Constants.titolario_type_id, codEnte, codAoo);
            if (anagraficaType == null)
                throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.titolario_type_id + " per COD_ENTE: " + codEnte + " e COD_AOO: " + codAoo);

            String codTitolario = info.get(Constants.titolario_cod_titolario);
            String classifica = info.get(Constants.titolario_classifica);
            if (codTitolario == null || codTitolario.equals(VOID)) {
                codTitolario = classifica;
            }
            String desTitolario = info.get(Constants.titolario_des_titolario);
            String parentClassifica = info.get(Constants.titolario_parent_classifica);

            // controllo credenziali di manager
            // if(parentClassifica==null || parentClassifica.equals(VOID)){
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Voce di Titolario di primo livello: utente non autorizzato per le operazioni di management");
            // }

            String enabled = info.get(Constants.titolario_enabled);
            if (enabled == null)
                enabled = "true";

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_TITOLARIO);

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_TITOLARIO);

            // if (codTitolario == null || codTitolario.equals(VOID))
            // throw new DocerException(ERROR_MESSAGE_CREATE_TITOLARIO);

            if (desTitolario == null || desTitolario.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_TITOLARIO);

            if (classifica == null || classifica.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_TITOLARIO);

            Map<String, String> idCritAoo = new HashMap<String, String>();
            idCritAoo.put(Constants.aoo_cod_ente, codEnte);
            idCritAoo.put(Constants.aoo_cod_aoo, codAoo);

            Map<String, String> paoo = getAnagraficaProfile(Constants.aoo_type_id, idCritAoo);

            if (paoo == null) {

                Map<String, String> idCritEnte = new HashMap<String, String>();
                idCritEnte.put(Constants.aoo_cod_ente, codEnte);

                Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCritEnte);
                if (p == null)
                    throw new DocerException("Ente " + codEnte + " non trovato");

                throw new DocerException("AOO " + idCritAoo.toString() + " non trovata");
            }

            // Verifico utilizzo della CLASSIFICA (anche con piano di classificazione differente ma ENABLED)
            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            List<String> enteList = new ArrayList<String>();
            enteList.add(codEnte);
            List<String> aooList = new ArrayList<String>();
            aooList.add(codAoo);
            List<String> classList = new ArrayList<String>();
            classList.add(classifica);
            searchCriteria.put(Constants.aoo_cod_ente, enteList);
            searchCriteria.put(Constants.aoo_cod_aoo, aooList);
            searchCriteria.put(Constants.titolario_classifica, classList);

            List<ISearchItem> listaDoppioni = searchAnagrafiche(ticket, Constants.titolario_type_id, searchCriteria);
            if (listaDoppioni != null && listaDoppioni.size() > 0) {
                if (!isPianoClassEnabled)
                    throw new DocerException("Voce di Titolario " + codEnte + "|" + codAoo + "|" + classifica + " esistente");

                if (enabled.equalsIgnoreCase("true")){
                    // Se uso il pianoClass e sto creando un titolario abilitato, controllo che tra quelle trovate ce ne sia almeno un'altra abilitata
                    for (ISearchItem doppione : listaDoppioni) {
                        for (KeyValuePair metadato : doppione.getMetadata()) {
                            if (metadato.getKey().equals(Constants.titolario_enabled) && metadato.getValue().equals("true"))
                                throw new DocerException("Voce di Titolario " +  codEnte + "|" + codAoo + "|" + classifica + " esistente e abilitata");
                        }
                    }
                }
            }

            Map<String, String> idCritTitoario = new HashMap<String, String>();
            idCritTitoario.clear();
            idCritTitoario.put(Constants.aoo_cod_ente, codEnte);
            idCritTitoario.put(Constants.aoo_cod_aoo, codAoo);
            idCritTitoario.put(Constants.titolario_cod_titolario, codTitolario);
            // DOCER-36 Piano Class
            if (isPianoClassEnabled)
                idCritTitoario.put(Constants.titolario_piano_class, pianoClass);

            // verifico utilizzo del COD_TITOLARIO
            Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, idCritTitoario);
            if (ptit != null) {
                throw new DocerException("Compatibilita' DOCAREA/DocER: il metadato COD_TITOLARIO " + codTitolario
                        + " (impostato come CLASSIFICA se non specificato) e' gia' utilizzato da un altra voce di Titolario");
            }

            // controllo di esistenza del padre
            if (parentClassifica != null && !parentClassifica.equals(VOID)) {

                idCritTitoario.clear();
                idCritTitoario.put(Constants.aoo_cod_ente, codEnte);
                idCritTitoario.put(Constants.aoo_cod_aoo, codAoo);
                idCritTitoario.put(Constants.titolario_classifica, parentClassifica);
                // DOCER-36 Piano Class
                if (isPianoClassEnabled)
                    idCritTitoario.put(Constants.titolario_piano_class, pianoClass);

                Map<String, String> ptitPadre = getAnagraficaProfile(Constants.titolario_type_id, idCritTitoario);
                if (ptitPadre == null)
                    throw new DocerException("Voce di Titolario padre " + idCritTitoario.toString() + " non trovato");
            }

            ITitolarioInfo titInfo = new TitolarioInfo();
            titInfo.setEnabled(getEnumBoolean(enabled));
            titInfo.setCodiceEnte(codEnte);
            titInfo.setCodiceAOO(codAoo);
            titInfo.setClassifica(classifica);
            titInfo.setDescrizione(desTitolario);
            titInfo.getExtraInfo().put(Constants.titolario_cod_titolario, codTitolario);

            if (parentClassifica == null)
                parentClassifica = VOID;
            titInfo.setParentClassifica(parentClassifica);

            if (isPianoClassEnabled && !StringUtils.isEmpty(pianoClass))
                titInfo.setPianoClassificazione(pianoClass);

            FieldDescriptor fd = null;
            // aggiungiamo leextrainfo
            for (String fieldName : info.keySet()) {

                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }

                fd = anagraficaType.getFieldDescriptor(fieldName);
                if (fd == null) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica TITOLARIO per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
                }

                // info.put(fieldName,fd.checkValueFormat(info.get(fieldName)));
                String propValue = fd.checkValueFormat(info.get(fieldName));

                if (fieldName.equalsIgnoreCase(Constants.titolario_cod_ente)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.titolario_cod_aoo)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.titolario_cod_titolario)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.titolario_des_titolario)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.titolario_enabled)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.titolario_classifica)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.titolario_parent_classifica)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.titolario_piano_class)) {
                    continue;
                }

                titInfo.getExtraInfo().put(fd.getPropName(), propValue);
            }

            provider.createTitolario(titInfo);

        } catch (DocerException docEx) {

            throw new DocerException(470, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(570, err.getMessage());
        }
    }

    // 71
    public void updateTitolario(String ticket, Map<String, String> id, Map<String, String> info) throws DocerException {

        try {

            info = toUCMap(info, "info");

            id = toUCMap(id, "id");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            String codEnte = id.get(Constants.titolario_cod_ente);
            String codAoo = id.get(Constants.titolario_cod_aoo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;
            String pianoClass = isPianoClassEnabled ?
                    (id.get(Constants.fascicolo_piano_class) != null ? id.get(Constants.fascicolo_piano_class) : pianoClassDefault)
                    : null;

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(Constants.titolario_type_id, codEnte, codAoo);
            if (anagraficaType == null)
                throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.titolario_type_id + " per COD_ENTE: " + codEnte + " e COD_AOO: " + codAoo);

            // String codTitolario = id.get(Constants.titolario_cod_titolario);
            String classifica = id.get(Constants.titolario_classifica);

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("Voce di Titolario non trovata: specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("Voce di Titolario non trovata: specificare COD_AOO");

            // if ((classifica == null || classifica.equals(VOID))
            // && (codTitolario == null || codTitolario.equals(VOID)))
            // throw new DocerException(
            // "Titolario non trovato: specificare COD_TITOLARIO e/o CLASSIFICA");

            if (classifica == null || classifica.equals(VOID))
                throw new DocerException("Voce di Titolario non trovata: specificare CLASSIFICA");

            // DOCER-36 Se sto abilitando il titolario e ne esiste gia' uno abilitato con la stessa
              // classifica --> Errore !!
            if ( isPianoClassEnabled && "true".equalsIgnoreCase(info.get("ENABLED")) ) {
                Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
                List<String> enteList = new ArrayList<String>();
                enteList.add(codEnte);
                List<String> aooList = new ArrayList<String>();
                aooList.add(codAoo);
                List<String> classList = new ArrayList<String>();
                classList.add(classifica);
                searchCriteria.put(Constants.aoo_cod_ente, enteList);
                searchCriteria.put(Constants.aoo_cod_aoo, aooList);
                searchCriteria.put(Constants.titolario_classifica, classList);
                List<ISearchItem> listaDoppioni = searchAnagrafiche(ticket, Constants.titolario_type_id, searchCriteria);

                if (listaDoppioni != null && listaDoppioni.size() > 1) {
                    // Se uso il piano controllo che tra le classifiche trovate ce ne sia almeno una abilitata
                    // se sì, non può essere abilitata anche questa
                    for (ISearchItem doppione : listaDoppioni) {
                        // Cerco il piano di classificazione, se è il mio salto
                        String tempPianoClass = "";
                        for (KeyValuePair metadato : doppione.getMetadata()) {
                            if (metadato.getKey().equals(Constants.titolario_piano_class)) {
                                tempPianoClass = metadato.getValue();
                                break;
                            }
                        }

                        // Se è un altro piano classifica controllo se è abilitato
                        if (!pianoClass.equals(tempPianoClass))
                            for (KeyValuePair metadato : doppione.getMetadata()) {
                                if (metadato.getKey().equals(Constants.titolario_enabled) && metadato.getValue().equals("true"))
                                    throw new DocerException("Esiste gia' una voce di titolario " + codEnte + "|" + codAoo + "|" + classifica + " abilitata per il piano di classificazione: " + tempPianoClass);
                            }
                    }
                }
            }

            ITitolarioId titId = new TitolarioId();
            titId.setCodiceEnte(codEnte);
            titId.setCodiceAOO(codAoo);
            titId.setClassifica(classifica);
            // DOCER-36 Piano Class
            //if (!StringUtils.isEmpty(pianoClass))
                titId.setPianoClassificazione(pianoClass);

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.titolario_cod_ente, codEnte);
            idCrit.put(Constants.titolario_cod_aoo, codAoo);
            idCrit.put(Constants.titolario_classifica, classifica);
            // DOCER-36 Piano Class
            //if (!StringUtils.isEmpty(pianoClass))
                idCrit.put(Constants.titolario_piano_class, pianoClass);

            idCrit.put(Constants.custom_enabled, "*");

            Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, idCrit);
            if (ptit == null) {
                // if (codTitolario != null && classifica != null)
                // throw new DocerException("Titolario con COD_TITOLARIO "
                // + codTitolario + " e CLASSIFICA " + classifica
                // + " non trovato");
                // if (codTitolario != null)
                // throw new DocerException("Titolario con COD_TITOLARIO "
                // + codTitolario + " non trovato");

                throw new DocerException("Voce di Titolario " + idCrit.toString() + " non trovata");
            }

            // String oldCodEnte = ptit.get(Constants.titolario_cod_ente);
            // String oldCodAOO = ptit.get(Constants.titolario_cod_aoo);
            // //String oldCodTitolario =
            // ptit.get(Constants.titolario_cod_titolario);
            // String oldClassifica = ptit.get(Constants.titolario_classifica);
            // String oldParentClassifica =
            // ptit.get(Constants.titolario_parent_classifica);
            //
            // if (oldParentClassifica == null)
            // oldParentClassifica = VOID;

            // if(oldParentClassifica.equals(VOID)){
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Aggiornamento Voce di Titolario di primo livello: utente non autorizzato per le operazioni di management");
            // }

            ITitolarioInfo titInfo = new TitolarioInfo();

            FieldDescriptor fd = null;
            for (String fieldName : info.keySet()) {

                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }

                fd = anagraficaType.getFieldDescriptor(fieldName);
                if (fd == null) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica TITOLARIO per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
                }

                String newPropValue = fd.checkValueFormat(info.get(fieldName));
                String oldPropValue = ptit.get(fieldName);

                if (fieldName.equalsIgnoreCase(Constants.titolario_cod_ente)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare COD_ENTE");

                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.titolario_cod_aoo)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare COD_AOO");

                    continue;
                }
                /*if (fieldName.equalsIgnoreCase(Constants.titolario_cod_titolario)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare COD_TITOLARIO");

                    continue;
                }*/
                if (fieldName.equalsIgnoreCase(Constants.titolario_classifica)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare CLASSIFICA");

                    continue;
                }

                if (fieldName.equalsIgnoreCase(Constants.titolario_parent_classifica)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare PARENT_CLASSIFICA");

                    continue;
                }

                if (fieldName.equalsIgnoreCase(Constants.titolario_des_titolario)) {
                    titInfo.setDescrizione(newPropValue);
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.titolario_enabled)) {
                    titInfo.setEnabled(getEnumBoolean(newPropValue));
                    continue;
                }

                titInfo.getExtraInfo().put(fieldName, newPropValue);
            }

            provider.updateTitolario(titId, titInfo);

        } catch (DocerException docEx) {

            throw new DocerException(471, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(571, err.getMessage());
        }
    }

    // 72
    public Map<String, String> getTitolario(String ticket, Map<String, String> id) throws DocerException {

        try {

            id = toUCMap(id, "id");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            String codEnte = id.get(Constants.titolario_cod_ente);
            String codAoo = id.get(Constants.titolario_cod_aoo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;
            String pianoClass = isPianoClassEnabled ?
                    (id.get(Constants.fascicolo_piano_class) != null ? id.get(Constants.fascicolo_piano_class) : pianoClassDefault)
                    : null;
            // String codTitolario = id.get(Constants.titolario_cod_titolario);
            String classifica = id.get(Constants.titolario_classifica);

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("specificare COD_AOO");

            // if ((codTitolario == null || codTitolario.equals(VOID))
            // && (classifica == null || classifica.equals(VOID)))
            // throw new DocerException(
            // "specificare COD_TITOLARIO e/o CLASSIFICA");

            if (classifica == null || classifica.equals(VOID))
                throw new DocerException("specificare CLASSIFICA");

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.titolario_cod_ente, codEnte);
            idCrit.put(Constants.titolario_cod_aoo, codAoo);
            idCrit.put(Constants.titolario_classifica, classifica);
            // DOCER-36 Piano Class
            //
            // if (!StringUtils.isEmpty(pianoClass))
                idCrit.put(Constants.titolario_piano_class, pianoClass);

            Map<String, String> profile = getAnagraficaProfile(Constants.titolario_type_id, idCrit);

            if (profile == null) {
                throw new DocerException("Voce di Titolario " + idCrit.toString() + " non trovata");
            }

            return profile;

        } catch (DocerException docEx) {

            throw new DocerException(472, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(572, err.getMessage());
        }
    }

    // 73
    public void createFascicolo(String ticket, Map<String, String> info) throws DocerException {

        try {

            info = toUCMap(info, "info");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            String codEnte = info.get(Constants.fascicolo_cod_ente);
            String codAoo = info.get(Constants.fascicolo_cod_aoo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;
            /*String pianoClass = isPianoClassEnabled ?
                    (info.get(Constants.fascicolo_piano_class) != null ? info.get(Constants.fascicolo_piano_class) : pianoClassDefault)
                    : null;*/

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            String pianoClass = null;
            //se pianoClass è abilitato
            if(isPianoClassEnabled){
                pianoClass = info.get(Constants.fascicolo_piano_class) != null ? info.get(Constants.fascicolo_piano_class) : null;
                //se pianoClass non è passato in input alla chiamata
                if(pianoClass==null){
                    //recupero dalla configurazione il piano di classificazione (se presente) per l'anno del fascicolo che si sta tentando di creare, altrimenti null
                    String pianoClassByAnno = BL_CONFIGURATIONS.getConfig(entetoken).getStoricoPianoClass(codEnte,codAoo,info.get(Constants.fascicolo_anno_fascicolo));

                    if(pianoClassByAnno!=null){
                        //cerco il titolario con pianoClass uguale a pianoClassByAnno
                        Map<String, String> idCritTitolario = new HashMap<String, String>();
                        idCritTitolario.put(Constants.fascicolo_cod_ente, codEnte);
                        idCritTitolario.put(Constants.fascicolo_cod_aoo, codAoo);
                        idCritTitolario.put(Constants.fascicolo_classifica, info.get(Constants.fascicolo_classifica));
                        idCritTitolario.put(Constants.fascicolo_piano_class, pianoClassByAnno);
                        // cerco il titolario
                        Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, idCritTitolario);
                        if (ptit != null) {
                            pianoClass = pianoClassByAnno;
                        }else{
                            throw new DocerException("Titolario " + idCritTitolario.toString() + " non trovato");
                        }
                    }else{
                        throw new DocerException("Piano di classificazione non configurato per l'anno "+info.get(Constants.fascicolo_anno_fascicolo));
                    }
                }
            }

            // // controllo credenziali di manager
            // boolean isManager = provider.userIsManager(userid);
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(Constants.fascicolo_type_id, codEnte, codAoo);
            if (anagraficaType == null)
                throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.fascicolo_type_id + " per COD_ENTE: " + codEnte + " e COD_AOO: " + codAoo);

            String classifica = info.get(Constants.fascicolo_classifica);
            String codTitolario = VOID;

            String progrFascicolo = info.get(Constants.fascicolo_progr_fascicolo);
            String numFascicolo = info.get(Constants.fascicolo_num_fascicolo);
            if (numFascicolo == null || numFascicolo.equals(VOID)) {
                numFascicolo = progrFascicolo;
            }

            String annoFascicolo = info.get(Constants.fascicolo_anno_fascicolo);
            String desFascicolo = info.get(Constants.fascicolo_des_fascicolo);
            String enabled = info.get(Constants.fascicolo_enabled);
            if (enabled == null)
                enabled = "true";

            String parentProgressivo = info.get(Constants.fascicolo_parent_progr_fascicolo);

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

            if (classifica == null || classifica.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

            if (progrFascicolo == null || progrFascicolo.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

            // if (numFascicolo == null || numFascicolo.equals(VOID))
            // throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

            if (annoFascicolo == null || annoFascicolo.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

            if (desFascicolo == null || desFascicolo.equals(VOID))
                throw new DocerException(ERROR_MESSAGE_CREATE_FASCICOLO);

            Map<String, String> idCritTitolario = new HashMap<String, String>();
            idCritTitolario.put(Constants.fascicolo_cod_ente, codEnte);
            idCritTitolario.put(Constants.fascicolo_cod_aoo, codAoo);
            idCritTitolario.put(Constants.fascicolo_classifica, classifica);
            // DOCER-36 Piano Class
            if (isPianoClassEnabled)
                idCritTitolario.put(Constants.fascicolo_piano_class, pianoClass);

            // cerco il titolario
            Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, idCritTitolario);

            if (ptit == null) {

                Map<String, String> idCritAoo = new HashMap<String, String>();
                idCritAoo.put(Constants.fascicolo_cod_ente, codEnte);
                idCritAoo.put(Constants.fascicolo_cod_aoo, codAoo);

                Map<String, String> paoo = getAnagraficaProfile(Constants.aoo_type_id, idCritAoo);

                if (paoo == null) {
                    // verifico esistenza dell'ente
                    Map<String, String> idCritEnte = new HashMap<String, String>();
                    idCritEnte.put(Constants.fascicolo_cod_ente, codEnte);

                    Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCritEnte);
                    if (p == null)
                        throw new DocerException("Ente " + codEnte + " non trovato");

                    throw new DocerException("AOO " + idCritAoo.toString() + " non trovata");
                }

                throw new DocerException("Titolario " + idCritTitolario.toString() + " non trovato");

            }

            codTitolario = ptit.get(Constants.titolario_cod_titolario);
            if (codTitolario == null || codTitolario.equals(VOID)) {
                throw new DocerException("Compatibilita' DOCAREA/DocER: la voce di titolario " + ptit.toString() + " non ha COD_TITOLARIO assegnato");
            }

            Map<String, String> idCritFascicolo = new HashMap<String, String>();
            idCritFascicolo.put(Constants.fascicolo_cod_ente, codEnte);
            idCritFascicolo.put(Constants.fascicolo_cod_aoo, codAoo);
            idCritFascicolo.put(Constants.fascicolo_classifica, classifica);
            idCritFascicolo.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);
            idCritFascicolo.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);
            // DOCER-36 Piano Class
            //if (isPianoClassEnabled && !StringUtils.isEmpty(pianoClass))
              //  idCritFascicolo.put(Constants.fascicolo_piano_class, pianoClass);

            Map<String, String> pfasc = getAnagraficaProfile(Constants.fascicolo_type_id, idCritFascicolo);
            if (pfasc != null)
                throw new DocerException("Fascicolo " + idCritFascicolo.toString() + " esistente");

            // controllo utilizzo del num_fascicolo
            idCritFascicolo.clear();
            idCritFascicolo.put(Constants.fascicolo_cod_ente, codEnte);
            idCritFascicolo.put(Constants.fascicolo_cod_aoo, codAoo);
            idCritFascicolo.put(Constants.fascicolo_classifica, classifica);
            idCritFascicolo.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);
            idCritFascicolo.put(Constants.fascicolo_num_fascicolo, numFascicolo);
            // DOCER-36 Piano Class
            //if (isPianoClassEnabled)
              //  idCritFascicolo.put(Constants.fascicolo_piano_class, pianoClass);

            pfasc = getAnagraficaProfile(Constants.fascicolo_type_id, idCritFascicolo);
            if (pfasc != null)
                throw new DocerException("Compatibilita' DOCAREA/DocER: il metadato NUM_FASCICOLO " + numFascicolo
                        + " (impostato come PROGR_FASCICOLO se non specificato) e' gia' utilizzato dal Fascicolo " + pfasc.toString());

            // controllo di esistenza del padre
            if (parentProgressivo != null && !parentProgressivo.equals(VOID)) {

                idCritFascicolo.clear();
                idCritFascicolo.put(Constants.fascicolo_cod_ente, codEnte);
                idCritFascicolo.put(Constants.fascicolo_cod_aoo, codAoo);
                idCritFascicolo.put(Constants.fascicolo_classifica, classifica);
                idCritFascicolo.put(Constants.fascicolo_progr_fascicolo, parentProgressivo);
                idCritFascicolo.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);

                Map<String, String> pfascPadre = getAnagraficaProfile(Constants.fascicolo_type_id, idCritFascicolo);
                if (pfascPadre == null)
                    throw new DocerException("Fascicolo padre " + idCritFascicolo.toString() + " non trovato");
            }

            IFascicoloInfo fascInfo = new FascicoloInfo();
            fascInfo.setEnabled(getEnumBoolean(enabled));
            fascInfo.setCodiceEnte(codEnte);
            fascInfo.setCodiceAOO(codAoo);
            fascInfo.setClassifica(classifica);
            fascInfo.setProgressivo(progrFascicolo);
            fascInfo.setNumeroFascicolo(numFascicolo);

            // DOCER-36
            if (isPianoClassEnabled)
                fascInfo.setPianoClassificazione(pianoClass);

            // lo metto anche nelle extrainfo e due i campi per sicurezza
            fascInfo.getExtraInfo().put(Constants.fascicolo_num_fascicolo, numFascicolo);
            fascInfo.getExtraInfo().put(Constants.titolario_cod_titolario, codTitolario);

            fascInfo.setAnnoFascicolo(annoFascicolo);
            fascInfo.setDescrizione(desFascicolo);
            if (parentProgressivo == null)
                parentProgressivo = VOID;
            fascInfo.setParentProgressivo(parentProgressivo);

            FieldDescriptor fd = null;
            // aggiungiamo le altre extra info
            for (String fieldName : info.keySet()) {

                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }

                fd = anagraficaType.getFieldDescriptor(fieldName);
                if (fd == null) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica FASCICOLO per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
                }

                String propValue = fd.checkValueFormat(info.get(fieldName));

                if (fieldName.equalsIgnoreCase(Constants.fascicolo_cod_ente)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_cod_aoo)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_classifica)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.titolario_cod_titolario)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_progr_fascicolo)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_anno_fascicolo)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_des_fascicolo)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_enabled)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_parent_progr_fascicolo)) {
                    continue;
                }

                fascInfo.getExtraInfo().put(fd.getPropName(), propValue);
            }

            provider.createFascicolo(fascInfo);

        } catch (DocerException docEx) {

            throw new DocerException(473, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(573, err.getMessage());
        }
    }

    // 74
    public void updateFascicolo(String ticket, Map<String, String> id, Map<String, String> info) throws DocerException {

        try {

            info = toUCMap(info, "info");
            id = toUCMap(id, "id");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            String codEnte = id.get(Constants.fascicolo_cod_ente);
            String codAoo = id.get(Constants.fascicolo_cod_aoo);

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;

            String pianoClass = null;
            //se pianoClass è abilitato
            if(isPianoClassEnabled){
                pianoClass = id.get(Constants.fascicolo_piano_class) != null ? id.get(Constants.fascicolo_piano_class) : null;
                //se pianoClass non è passato in input alla chiamata
                if(pianoClass==null){
                    //recupero dalla configurazione il piano di classificazione (se presente) per l'anno del fascicolo che si sta tentando di creare, altrimenti null
                    String pianoClassByAnno = BL_CONFIGURATIONS.getConfig(entetoken).getStoricoPianoClass(codEnte,codAoo,id.get(Constants.fascicolo_anno_fascicolo));

                    if(pianoClassByAnno!=null){
                        //cerco il titolario con pianoClass uguale a pianoClassByAnno
                        Map<String, String> idCritTitolario = new HashMap<String, String>();
                        idCritTitolario.put(Constants.fascicolo_cod_ente, codEnte);
                        idCritTitolario.put(Constants.fascicolo_cod_aoo, codAoo);
                        idCritTitolario.put(Constants.fascicolo_classifica, id.get(Constants.fascicolo_classifica));
                        idCritTitolario.put(Constants.fascicolo_piano_class, pianoClassByAnno);
                        // cerco il titolario
                        Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, idCritTitolario);
                        if (ptit != null) {
                            pianoClass = pianoClassByAnno;
                        }else{
                            throw new DocerException("Titolario " + idCritTitolario.toString() + " non trovato");
                        }
                    }else{
                        throw new DocerException("Piano di classificazione non configurato per l'anno "+id.get(Constants.fascicolo_anno_fascicolo));
                    }
                }
            }



            // controllo credenziali di manager
            // boolean isManager = provider.userIsManager(userid);
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(Constants.fascicolo_type_id, codEnte, codAoo);
            if (anagraficaType == null)
                throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + Constants.fascicolo_type_id + " per COD_ENTE: " + codEnte + " e COD_AOO: " + codAoo);

            String classifica = id.get(Constants.fascicolo_classifica);
            String progrFascicolo = id.get(Constants.fascicolo_progr_fascicolo);
            // String numFascicolo = id.get(Constants.fascicolo_num_fascicolo);
            String annoFascicolo = id.get(Constants.fascicolo_anno_fascicolo);

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

            if (classifica == null || classifica.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

            // if ((numFascicolo == null || numFascicolo.equals(VOID))
            // && (progressivo == null || progressivo.equals(VOID)))
            // throw new DocerException(
            // "Fascicolo non trovato: specificare NUM_FASCICOLO e/o PROGR_FASCICOLO");

            if (progrFascicolo == null || progrFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare PROGR_FASCICOLO");

            if (annoFascicolo == null || annoFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

            IFascicoloId fascId = new FascicoloId();
            fascId.setCodiceEnte(codEnte);
            fascId.setCodiceAOO(codAoo);
            fascId.setClassifica(classifica);
            fascId.setProgressivo(progrFascicolo);
            fascId.setAnnoFascicolo(annoFascicolo);
            // DOCER-36 Piano Class
            if (!StringUtils.isEmpty(pianoClass)) {
                fascId.setPianoClassificazione(pianoClass);
                id.put("PIANO_CLASS", pianoClass);
            }

            id.put(Constants.custom_enabled, "*");

            Map<String, String> pfasc = getAnagraficaProfile(Constants.fascicolo_type_id, id);
            if (pfasc == null) {

                throw new DocerException("Fascicolo " + id.toString() + " non trovato");
            }

            IFascicoloInfo fascInfo = new FascicoloInfo();

            FieldDescriptor fd = null;
            String newPropValue = VOID;
            String oldPropValue = VOID;

            // assegno le extrainfo
            for (String fieldName : info.keySet()) {

                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }

                fd = anagraficaType.getFieldDescriptor(fieldName);
                if (fd == null) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica FASCICOLO per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
                }

                newPropValue = fd.checkValueFormat(info.get(fieldName));
                oldPropValue = pfasc.get(fieldName);

                if (fieldName.equalsIgnoreCase(Constants.fascicolo_cod_ente)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare COD_ENTE");

                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_cod_aoo)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare COD_AOO");

                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_classifica)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare CLASSIFICA");
                    continue;
                }
                // il cod_titolario //compatibilita' DOCAREA
                if (fieldName.equalsIgnoreCase(Constants.titolario_cod_titolario)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare COD_TITOLARIO");
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_progr_fascicolo)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare PROGR_FASCICOLO");
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_num_fascicolo)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare NUM_FASCICOLO");
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_parent_progr_fascicolo)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare " + Constants.fascicolo_parent_progr_fascicolo);
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_anno_fascicolo)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare ANNO_FASCICOLO");
                    continue;
                }

                if (fieldName.equalsIgnoreCase(Constants.fascicolo_des_fascicolo)) {
                    fascInfo.setDescrizione(newPropValue);
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.fascicolo_enabled)) {
                    fascInfo.setEnabled(getEnumBoolean(newPropValue));
                    continue;
                }

                fascInfo.getExtraInfo().put(fieldName, newPropValue);
            }

            provider.updateFascicolo(fascId, fascInfo);

        } catch (DocerException docEx) {

            throw new DocerException(474, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(574, err.getMessage());
        }
    }

    // 75
    public Map<String, String> getFascicolo(String ticket, Map<String, String> id) throws DocerException {

        try {

            id = toUCMap(id, "id");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            String codEnte = id.get(Constants.fascicolo_cod_ente);
            String codAoo = id.get(Constants.fascicolo_cod_aoo);
            String classifica = id.get(Constants.fascicolo_classifica);
            String progrFascicolo = id.get(Constants.fascicolo_progr_fascicolo);
            String annoFascicolo = id.get(Constants.fascicolo_anno_fascicolo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;

            String pianoClass = null;
            //se pianoClass è abilitato
            if(isPianoClassEnabled){
                pianoClass = id.get(Constants.fascicolo_piano_class) != null ? id.get(Constants.fascicolo_piano_class) : null;
                //se pianoClass non è passato in input alla chiamata
                if(pianoClass==null){
                    //recupero dalla configurazione il piano di classificazione (se presente) per l'anno del fascicolo che si sta tentando di creare, altrimenti null
                    String pianoClassByAnno = BL_CONFIGURATIONS.getConfig(entetoken).getStoricoPianoClass(codEnte,codAoo,id.get(Constants.fascicolo_anno_fascicolo));

                    if(pianoClassByAnno!=null){
                        //cerco il titolario con pianoClass uguale a pianoClassByAnno
                        Map<String, String> idCritTitolario = new HashMap<String, String>();
                        idCritTitolario.put(Constants.fascicolo_cod_ente, codEnte);
                        idCritTitolario.put(Constants.fascicolo_cod_aoo, codAoo);
                        idCritTitolario.put(Constants.fascicolo_classifica, id.get(Constants.fascicolo_classifica));
                        idCritTitolario.put(Constants.fascicolo_piano_class, pianoClassByAnno);
                        // cerco il titolario
                        Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, idCritTitolario);
                        if (ptit != null) {
                            pianoClass = pianoClassByAnno;
                        }else{
                            throw new DocerException("Titolario " + idCritTitolario.toString() + " non trovato");
                        }
                    }else{
                        throw new DocerException("Piano di classificazione non configurato per l'anno "+id.get(Constants.fascicolo_anno_fascicolo));
                    }
                }
            }

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

            if (classifica == null || classifica.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

            if (progrFascicolo == null || progrFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare PROGR_FASCICOLO");

            if (annoFascicolo == null || annoFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.fascicolo_cod_ente, codEnte);
            idCrit.put(Constants.fascicolo_cod_aoo, codAoo);
            idCrit.put(Constants.fascicolo_classifica, classifica);
            idCrit.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);
            idCrit.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);

            // DOCER-36 Piano Class
            if (!StringUtils.isEmpty(pianoClass))
                idCrit.put(Constants.fascicolo_piano_class, pianoClass);

            Map<String, String> profile = getAnagraficaProfile(Constants.fascicolo_type_id, idCrit);

            if (profile == null) {
                throw new DocerException("Fascicolo " + idCrit.toString() + " non trovato");
            }

            return profile;

        } catch (DocerException docEx) {

            throw new DocerException(475, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(575, err.getMessage());
        }
    }

    // 76
    public void createAnagraficaCustom(String ticket, Map<String, String> info) throws DocerException {

        try {
            info = toUCMap(info, "info");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            String type_id = info.get(Constants.anagrafica_type_id);

            if (type_id == null || type_id.equals(VOID))
                throw new DocerException("TYPE_ID obbligatorio");

            type_id = type_id.toUpperCase();

            String codEnte = info.get(Constants.fascicolo_cod_ente);
            String codAoo = info.get(Constants.fascicolo_cod_aoo);

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("COD_ENTE obbligatorio");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("COD_AOO obbligatorio");

            AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(type_id, codEnte, codAoo);
            if (anagraficaType == null)
                throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + type_id + " per COD_ENTE: " + codEnte + " e COD_AOO: " + codAoo);

            if (!anagraficaType.isAnagraficaCustom())
                throw new DocerException("Anagrafica " + type_id + " non e' configurata come anagrafica custom");

            // if (!anagraficaType.getDefinedForEnte(codEnte))
            // throw new DocerException(
            // "Anagrafica Custom " +type_id +" non e' definita per Ente "
            // +codEnte);

            String codCustom = info.get(anagraficaType.getCodicePropName());
            if (codCustom == null || codCustom.equals(VOID))
                throw new DocerException("errore creazione anagrafica custom: " + anagraficaType.getCodicePropName() + " obbligatorio");

            String desCustom = info.get(anagraficaType.getDescrizionePropName());
            if (desCustom == null || desCustom.equals(VOID))
                throw new DocerException("errore creazione anagrafica custom: " + anagraficaType.getDescrizionePropName() + " obbligatorio");

            ICustomItemInfo customItemInfo = new CustomItemInfo();
            customItemInfo.setType(type_id);
            customItemInfo.setCodiceEnte(codEnte);
            customItemInfo.setCodiceAOO(codAoo);
            customItemInfo.setCodiceCustom(codCustom);
            customItemInfo.setDescrizione(desCustom);

            customItemInfo.setEnabled(EnumBoolean.TRUE);

            FieldDescriptor fd = null;

            for (String fieldName : info.keySet()) {

                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }

                fd = anagraficaType.getFieldDescriptor(fieldName);
                if (fd == null) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica " + type_id + " per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
                }

                String propValue = fd.checkValueFormat(info.get(fieldName));

                if (fieldName.equalsIgnoreCase(Constants.custom_cod_ente)) {
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.custom_cod_aoo)) {
                    continue;
                }

                if (fieldName.equalsIgnoreCase(anagraficaType.getCodicePropName())) {
                    continue;
                }

                if (fieldName.equalsIgnoreCase(anagraficaType.getDescrizionePropName())) {
                    continue;
                }

                if (fieldName.equalsIgnoreCase(Constants.custom_enabled)) {
                    customItemInfo.setEnabled(getEnumBoolean(info.get(fieldName)));
                    continue;
                }

                customItemInfo.getExtraInfo().put(fd.getPropName(), propValue);
            }

            Map<String, String> idCritAoo = new HashMap<String, String>();
            idCritAoo.put(Constants.custom_cod_ente, codEnte);
            idCritAoo.put(Constants.custom_cod_aoo, codAoo);

            Map<String, String> p = getAnagraficaProfile(Constants.aoo_type_id, idCritAoo);
            if (p == null) {

                Map<String, String> idCritEnte = new HashMap<String, String>();
                idCritEnte.put(Constants.custom_cod_ente, codEnte);
                p = getAnagraficaProfile(Constants.ente_type_id, idCritEnte);

                if (p == null)
                    throw new DocerException("Ente " + codEnte + " non trovato");

                throw new DocerException("AOO " + idCritAoo.toString() + " non trovata");
            }

            Map<String, String> idCritCustom = new HashMap<String, String>();
            idCritCustom.put(Constants.custom_cod_ente, codEnte);
            idCritCustom.put(Constants.custom_cod_aoo, codAoo);
            idCritCustom.put(anagraficaType.getCodicePropName(), customItemInfo.getCodiceCustom());

            p = getAnagraficaProfile(type_id, idCritCustom);
            if (p != null)
                throw new DocerException("Anagrafica " + type_id + " " + idCritCustom.toString() + " esistente");

            // creazione custom item
            provider.createCustomItem(customItemInfo);

        } catch (DocerException docEx) {

            throw new DocerException(476, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(576, err.getMessage());
        }
    }

    // 77
    public void updateAnagraficaCustom(String ticket, Map<String, String> id, Map<String, String> info) throws DocerException {

        try {

            id = toUCMap(id, "id");
            info = toUCMap(info, "info");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            String type_id = id.get(Constants.anagrafica_type_id);
            if (type_id == null || type_id.equals(VOID))
                throw new DocerException("TYPE_ID obbligatorio tra i metadati di ID");

            type_id = type_id.toUpperCase();

            String codEnte = id.get(Constants.fascicolo_cod_ente);
            String codAoo = id.get(Constants.fascicolo_cod_aoo);

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("COD_ENTE obbligatorio");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("COD_AOO obbligatorio");

            AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(type_id, codEnte, codAoo);
            if (anagraficaType == null)
                throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + type_id + " per COD_ENTE: " + codEnte + " e COD_AOO: " + codAoo);

            if (!anagraficaType.isAnagraficaCustom())
                throw new DocerException("Anagrafica " + type_id + " non e' configurata come anagrafica custom");

            // if (!anagraficaType.getDefinedForEnte(codEnte))
            // throw new DocerException(
            // "Anagrafica Custom " +type_id +" non e' definita per Ente "
            // +codEnte);

            String codCustom = id.get(anagraficaType.getCodicePropName());

            if (codCustom == null || codCustom.equals(VOID))
                throw new DocerException("errore update anagrafica custom: " + anagraficaType.getCodicePropName() + " obbligatorio");

            ICustomItemId customId = new CustomItemId();
            customId.setType(type_id);
            customId.setCodiceEnte(codEnte);
            customId.setCodiceAOO(codAoo);
            customId.setCodiceCustom(codCustom);

            // controllo la collezione di ID
            FieldDescriptor fd = null;
            for (String fieldName : id.keySet()) {

                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }

                fd = anagraficaType.getFieldDescriptor(fieldName);
                if (fd == null) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica " + type_id + " per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
                }

            }

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.custom_cod_ente, codEnte);
            idCrit.put(Constants.custom_cod_aoo, codAoo);
            idCrit.put(anagraficaType.getCodicePropName(), codCustom);

            idCrit.put(Constants.custom_enabled, "*");

            Map<String, String> pcustom = getAnagraficaProfile(type_id, idCrit);
            if (pcustom == null)
                throw new DocerException("Anagrafica " + type_id + " " + idCrit.toString() + " non trovata");

            ICustomItemInfo customItemNewInfo = new CustomItemInfo();

            for (String fieldName : info.keySet()) {

                if (fieldName.equalsIgnoreCase(Constants.anagrafica_type_id)) {
                    continue;
                }

                fd = anagraficaType.getFieldDescriptor(fieldName);
                if (fd == null) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica " + type_id + " per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
                }

                String newPropValue = fd.checkValueFormat(info.get(fieldName));
                String oldPropValue = pcustom.get(fieldName);

                if (fieldName.equalsIgnoreCase(Constants.custom_cod_ente)) {
                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare COD_ENTE");

                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.custom_cod_aoo)) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare COD_AOO");

                    continue;
                }
                if (fieldName.equalsIgnoreCase(anagraficaType.getCodicePropName())) {

                    if (newPropValue != null && !newPropValue.equalsIgnoreCase(oldPropValue))
                        throw new DocerException("non e' permesso modificare " + anagraficaType.getCodicePropName());

                    continue;
                }
                if (fieldName.equalsIgnoreCase(anagraficaType.getDescrizionePropName())) {
                    customItemNewInfo.setDescrizione(newPropValue);
                    continue;
                }
                if (fieldName.equalsIgnoreCase(Constants.custom_enabled)) {
                    customItemNewInfo.setEnabled(getEnumBoolean(newPropValue));
                    continue;
                }

                customItemNewInfo.getExtraInfo().put(fieldName, newPropValue);

            }

            // creazione custom item
            provider.updateCustomItem(customId, customItemNewInfo);

        } catch (DocerException docEx) {

            throw new DocerException(477, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(577, err.getMessage());
        }
    }

    // 78
    public Map<String, String> getAnagraficaCustom(String ticket, Map<String, String> id) throws DocerException {

        try {

            id = toUCMap(id, "id");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            String type_id = id.get(Constants.anagrafica_type_id);
            if (type_id == null || type_id.equals(VOID))
                throw new DocerException("TYPE_ID obbligatorio tra i metadati di ID");

            type_id = type_id.toUpperCase();

            String codEnte = id.get(Constants.fascicolo_cod_ente);
            String codAoo = id.get(Constants.fascicolo_cod_aoo);

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("COD_ENTE obbligatorio");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("COD_AOO obbligatorio");

            AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(type_id, codEnte, codAoo);
            if (anagraficaType == null)
                throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + type_id + " per COD_ENTE: " + codEnte + " e COD_AOO: " + codAoo);

            if (!anagraficaType.isAnagraficaCustom())
                throw new DocerException("Anagrafica " + type_id + " non e' configurata come anagrafica custom");

            // if (!anagraficaType.getDefinedForEnte(codEnte))
            // throw new DocerException(
            // "Anagrafica Custom " +type_id +" non e' definita per Ente "
            // +codEnte);

            String codCustom = id.get(anagraficaType.getCodicePropName());

            if (codCustom == null || codCustom.equals(VOID))
                throw new DocerException(anagraficaType.getCodicePropName() + " obbligatorio");

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.custom_cod_ente, codEnte);
            idCrit.put(Constants.custom_cod_aoo, codAoo);
            idCrit.put(anagraficaType.getCodicePropName(), codCustom);
            // idCrit.put(Constants.anagrafica_type_id, type_id);

            Map<String, String> profile = getAnagraficaProfile(type_id, idCrit);

            return profile;

        } catch (DocerException docEx) {

            throw new DocerException(478, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(578, err.getMessage());
        }
    }

    // 79
    public void createUser(String ticket, Map<String, String> info) throws DocerException {

        try {

            info = toUCMap(info, "info");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            String codEnte = null;
            String codAOO = null;

            IUserProfileInfo userProfile = new UserProfileInfo();

            for (String key : info.keySet()) {

                if (key == null)
                    continue;

                if (key.equalsIgnoreCase(Constants.user_user_id)) {
                    userProfile.setUserId(info.get(Constants.user_user_id));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.user_full_name)) {
                    userProfile.setFullName(info.get(Constants.user_full_name));
                    continue;
                }

                if (key.equalsIgnoreCase(Constants.user_email_address)) {
                    userProfile.setEmailAddress(info.get(Constants.user_email_address));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.user_first_name)) {
                    userProfile.setFirstName(info.get(Constants.user_first_name));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.user_last_name)) {
                    userProfile.setLastName(info.get(Constants.user_last_name));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.user_user_password)) {
                    userProfile.setUserPassword(info.get(Constants.user_user_password));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.user_network_alias)) {
                    userProfile.setNetworkAlias(info.get(Constants.user_network_alias));
                    continue;
                }

                if (key.equalsIgnoreCase(Constants.user_enabled)) {
                    userProfile.setEnabled(getEnumBoolean(info.get(Constants.user_enabled)));
                    continue;
                }

                if (key.equalsIgnoreCase(Constants.user_cod_ente)) {
                    codEnte = info.get(Constants.user_cod_ente);
                } else if (key.equalsIgnoreCase(Constants.user_cod_aoo)) {
                    codAOO = info.get(Constants.user_cod_aoo);
                }
                // le extrainfo
                userProfile.getExtraInfo().put(key.toUpperCase(), info.get(key));
            }

            if (userProfile.getUserId() == null || userProfile.getUserId().equals(VOID))
                throw new DocerException("specificare USER_ID");

            if (userProfile.getFullName() == null || userProfile.getFullName().equals(VOID))
                throw new DocerException("specificare FULL_NAME");

            // verifica esistenza utente
            if (provider.getUser(userProfile.getUserId()) != null)
                throw new DocerException("Utente esistente: " + userProfile.getUserId());

            Map<String, String> id = null;

            // verifica esistenza dell'Ente
            if (codEnte != null && !codEnte.equals(VOID)) {
                id = new HashMap<String, String>();
                id.put(Constants.ente_cod_ente, codEnte);
                Map<String, String> p = getAnagraficaProfile("ENTE", id);

                if (p == null) {
                    throw new DocerException("Ente non trovato: " + codEnte);
                }
            }

            // verifica esistenza della AOO
            if (codAOO != null && !codAOO.equals(VOID)) {

                if (codEnte == null || codEnte.equals(VOID)) {
                    throw new DocerException("AOO non trovata, specificare COD_ENTE");
                }
                if (id != null) {
                    id.clear();
                } else {
                    id = new HashMap<String, String>();
                }

                id.put(Constants.aoo_cod_ente, codEnte);
                id.put(Constants.aoo_cod_aoo, codAOO);
                Map<String, String> p = getAnagraficaProfile("AOO", id);

                if (p == null) {
                    throw new DocerException("AOO non trovata: " + codAOO);
                }
            }

            List<String> criteria = new ArrayList<String>();
            criteria.add(userProfile.getUserId());

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put(Constants.group_group_id, criteria);

            List<IGroupProfileInfo> r = provider.searchGroups(searchCriteria);
            // verifica esistenza utente
            if (r != null && r.size() > 0)
                throw new DocerException("non e' ammesso assegnare ad un utente lo stesso id di un gruppo: " + userProfile.getUserId());

            provider.createUser(userProfile);

        } catch (DocerException docEx) {

            throw new DocerException(479, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(579, err.getMessage());
        }
    }

    // 80
    public void updateUser(String ticket, String userId, Map<String, String> info) throws DocerException {

        try {

            info = toUCMap(info, "info");

            if (userId == null)
                throw new DocerException("userId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            // verifica presenza del parametro id
            if (userId == null || userId.equals(VOID))
                throw new DocerException("Utente non trovato: specificare userId");

            IUserProfileInfo newUserProfile = new UserProfileInfo();
            String newCodEnte = null;
            String newCodAOO = null;
            for (String key : info.keySet()) {

                if (key == null)
                    continue;

                if (key.equalsIgnoreCase(Constants.user_user_id)) {
                    String newUserId = info.get(Constants.user_user_id);

                    if (newUserId != null && !newUserId.equals(userId))
                        throw new DocerException("non e' permesso modificare USER_ID");

                    continue;
                }
                if (key.equalsIgnoreCase(Constants.user_full_name)) {
                    newUserProfile.setFullName(info.get(Constants.user_full_name));
                    continue;
                }

                if (key.equalsIgnoreCase(Constants.user_email_address)) {
                    newUserProfile.setEmailAddress(info.get(Constants.user_email_address));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.user_first_name)) {
                    newUserProfile.setFirstName(info.get(Constants.user_first_name));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.user_last_name)) {
                    newUserProfile.setLastName(info.get(Constants.user_last_name));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.user_user_password)) {
                    newUserProfile.setUserPassword(info.get(Constants.user_user_password));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.user_network_alias)) {
                    newUserProfile.setNetworkAlias(info.get(Constants.user_network_alias));
                    continue;
                }

                if (key.equalsIgnoreCase(Constants.user_enabled)) {
                    newUserProfile.setEnabled(getEnumBoolean(info.get(Constants.user_enabled)));
                    continue;
                }

                if (key.equalsIgnoreCase(Constants.user_cod_ente)) {
                    newCodEnte = info.get(Constants.user_cod_ente);
                } else if (key.equalsIgnoreCase(Constants.user_cod_aoo)) {
                    newCodAOO = info.get(Constants.user_cod_aoo);
                }

                newUserProfile.getExtraInfo().put(key, info.get(key));
            }

            IUserInfo userInfo = provider.getUser(userId);
            // verifica esistenza utente
            if (userInfo == null)
                throw new DocerException("Utente non trovato: " + userId);

            String oldCodEnte = userInfo.getProfileInfo().getExtraInfo().get(Constants.user_cod_ente);
            String oldCodAOO = userInfo.getProfileInfo().getExtraInfo().get(Constants.user_cod_aoo);

            Map<String, String> id = null;

            // verifica esistenza dell'Ente
            if (newCodEnte != null && !newCodEnte.equals(VOID) && !newCodEnte.equalsIgnoreCase(oldCodEnte)) {
                id = new HashMap<String, String>();
                id.put(Constants.ente_cod_ente, newCodEnte);
                Map<String, String> p = getAnagraficaProfile("ENTE", id);

                if (p == null) {
                    throw new DocerException("nuovo Ente non trovato: " + newCodEnte);
                }
            }

            // verifica esistenza della AOO
            if (newCodAOO != null && !newCodAOO.equals(VOID) && !newCodAOO.equalsIgnoreCase(oldCodAOO)) {

                if (newCodEnte == null || newCodEnte.equals(VOID)) {
                    throw new DocerException("nuova AOO non trovata, specificare COD_ENTE");
                }
                if (id != null) {
                    id.clear();
                } else {
                    id = new HashMap<String, String>();
                }
                id.put(Constants.aoo_cod_ente, newCodEnte);
                id.put(Constants.aoo_cod_aoo, newCodAOO);
                Map<String, String> p = getAnagraficaProfile("AOO", id);

                if (p == null) {
                    throw new DocerException("nuova AOO non trovata: " + newCodAOO);
                }
            }

            provider.updateUser(userId, newUserProfile);

        } catch (DocerException docEx) {

            throw new DocerException(480, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(580, err.getMessage());
        }
    }

    // 81
    public Map<String, String> getUser(String ticket, String userId) throws DocerException {

        try {

            if (userId == null)
                throw new DocerException("userId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // verifica presenza del parametro id
            if (userId == null || userId.equals(VOID))
                throw new DocerException("Utente non trovato: specificare userId");

            // boolean isManager = provider.userIsManager(currentUser);
            // if (!isManager && !currentUser.equalsIgnoreCase(userId))
            // // l'utente non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            // verifica esistenza utente
            IUserInfo userInfo = provider.getUser(userId);

            if (userInfo == null)
                return null;

            Map<String, String> user = new HashMap<String, String>();

            user.put(Constants.user_user_id, userInfo.getProfileInfo().getUserId());
            user.put(Constants.user_full_name, userInfo.getProfileInfo().getFullName());
            user.put(Constants.user_email_address, userInfo.getProfileInfo().getEmailAddress());
            user.put(Constants.user_first_name, userInfo.getProfileInfo().getFirstName());
            user.put(Constants.user_last_name, userInfo.getProfileInfo().getLastName());
            user.put(Constants.user_network_alias, userInfo.getProfileInfo().getNetworkAlias());

            user.put(Constants.user_enabled, userInfo.getProfileInfo().getEnabled().toString().toLowerCase());

            // le extrainfo
            for (String key : userInfo.getProfileInfo().getExtraInfo().keySet()) {
                user.put(key, userInfo.getProfileInfo().getExtraInfo().get(key));
            }

            // user.put("IS_ADMINISTRATOR",
            // String.valueOf(provider.userIsManager(userId)));

            return user;

        } catch (DocerException docEx) {

            throw new DocerException(481, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(581, err.getMessage());
        }
    }

    // 82
    public void updateGroupsOfUser(String ticket, String userId, List<String> groupsToAdd, List<String> groupsToRemove) throws DocerException {

        try {

            if (userId == null)
                throw new DocerException("userId null");

            if (groupsToAdd == null)
                groupsToAdd = new ArrayList<String>();

            if (groupsToRemove == null)
                groupsToRemove = new ArrayList<String>();

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            // verifica presenza del parametro id
            if (userId == null || userId.equals(VOID))
                throw new DocerException("Utente non trovato: specificare userId");

            // verifica esistenza utente
            IUserInfo userInfo = provider.getUser(userId);

            if (userInfo == null)
                throw new DocerException("Utente non trovato");

            List<String> finalGroupsToRemove = new ArrayList<String>();
            List<String> finalGroupsToAdd = new ArrayList<String>();

            // Nella lista dei gruppi da rimuovere non ci devono essere elementi
            // VOID
            for (int i = 0; i < groupsToRemove.size(); i++) {
                if (!groupsToRemove.get(i).equals(VOID))
                    finalGroupsToRemove.add(groupsToRemove.get(i));
            }

            // Nella lista dei gruppi da rimuovere non ci devono essere elementi
            // VOID
            for (int i = 0; i < groupsToAdd.size(); i++) {
                if (!groupsToAdd.get(i).equals(VOID))
                    finalGroupsToAdd.add(groupsToAdd.get(i));
            }

            // controllo dei gruppi da aggiungere
            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            List<IGroupProfileInfo> groups = null;

            // rimuovo eventuali gruppi
            if (finalGroupsToRemove.size() > 0) {

                searchCriteria.clear();

                // controllo degli utenti da rimuovere
                searchCriteria.put(Constants.group_group_id, finalGroupsToRemove);

                groups = provider.searchGroups(searchCriteria);

                for (String id : finalGroupsToRemove) {

                    boolean found = false;

                    for (IGroupProfileInfo groupProfile : groups) {
                        if (groupProfile.getGroupId().equals(id)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        throw new DocerException("Gruppo da rimuovere non trovato: " + id);
                }

                // rimuovo gruppi
                provider.removeGroupsFromUser(userId, finalGroupsToRemove);
            }

            searchCriteria.clear();

            // aggiungo eventuali gruppi
            if (finalGroupsToAdd.size() > 0) {

                searchCriteria.put(Constants.group_group_id, finalGroupsToAdd);

                groups = provider.searchGroups(searchCriteria);

                for (String id : finalGroupsToAdd) {

                    boolean found = false;

                    for (IGroupProfileInfo groupProfile : groups) {
                        if (groupProfile.getGroupId().equals(id)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        throw new DocerException("Gruppo da aggiungere non trovato: " + id);
                }

                // aggiungo gruppi
                provider.addGroupsToUser(userId, finalGroupsToAdd);
            }

        } catch (DocerException docEx) {

            throw new DocerException(482, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(582, err.getMessage());
        }

    }

    // 83
    public void setGroupsOfUser(String ticket, String userId, List<String> groupsToSet) throws DocerException {

        try {

            if (userId == null)
                throw new DocerException("userId null");

            if (groupsToSet == null)
                throw new DocerException("groupsToSet null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            // verifica presenza del parametro id
            if (userId == null || userId.equals(VOID))
                throw new DocerException("Utente non trovato: specificare userId");

            // verifica esistenza gruppo
            IUserInfo userInfo = provider.getUser(userId);

            if (userInfo == null)
                throw new DocerException("Utente " + userId + " non trovato");

            List<String> groupsToAssign = new ArrayList<String>();

            // non posso e non devo riassegnare il primary group
            for (int i = 0; i < groupsToSet.size(); i++) {
                if (!groupsToSet.get(i).equals(VOID)) {
                    groupsToAssign.add(groupsToSet.get(i));
                }
            }

            // controllo degli utenti da aggiungere
            if (groupsToAssign.size() > 0) {
                Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
                searchCriteria.put(Constants.group_group_id, groupsToAssign);

                List<IGroupProfileInfo> groups = provider.searchGroups(searchCriteria);

                for (String id : groupsToAssign) {

                    boolean found = false;

                    for (IGroupProfileInfo userProfile : groups) {
                        if (userProfile.getGroupId().equals(id)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        throw new DocerException("Gruppo da assegnare non trovato: " + id);
                }
            }

            // rimuovo tutti i gruppi dell'utente
            provider.removeGroupsFromUser(userId, userInfo.getGroups());

            // assegno i nuovi gruppi
            if (groupsToAssign.size() > 0) {
                provider.addGroupsToUser(userId, groupsToAssign);
            }

        } catch (DocerException docEx) {

            throw new DocerException(483, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(583, err.getMessage());
        }

    }

    // 84
    public List<String> getGroupsOfUser(String ticket, String userId) throws DocerException {

        try {

            if (userId == null)
                throw new DocerException("userId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager = provider.userIsManager(userid);
            // if (!isManager)
            // // l'utente non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            // verifica presenza del parametro id
            if (userId == null || userId.equals(VOID))
                throw new DocerException("Utente non trovato: specificare userId");

            // verifica esistenza gruppo
            IUserInfo userInfo = provider.getUser(userId);

            if (userInfo == null)
                throw new DocerException("Utente " + userId + " non trovato");

            return userInfo.getGroups();

        } catch (DocerException docEx) {

            throw new DocerException(484, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(584, err.getMessage());
        }

    }

    // 85
    public void createGroup(String ticket, Map<String, String> info) throws DocerException {

        try {

            info = toUCMap(info, "info");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            IGroupProfileInfo groupProfile = new GroupProfileInfo();

            for (String key : info.keySet()) {

                if (key == null)
                    continue;

                if (key.equalsIgnoreCase(Constants.group_group_id)) {
                    groupProfile.setGroupId(info.get(key));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.group_group_name)) {
                    groupProfile.setGroupName(info.get(key));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.group_parent_group_id)) {
                    groupProfile.setParentGroupId(info.get(key));
                    continue;
                }

                groupProfile.getExtraInfo().put(key, info.get(key));
            }

            if (groupProfile.getGroupId() == null || groupProfile.getGroupId().equals(VOID))
                throw new DocerException("specificare GROUP_ID");

            if (groupProfile.getGroupName() == null || groupProfile.getGroupName().equals(VOID))
                throw new DocerException("specificare GROUP_NAME");

            // if (groupProfile.getParentGroupId() == null ||
            // groupProfile.getParentGroupId().equals(VOID))
            // throw new DocerException("specificare PARENT_GROUP_ID");

            // verifica esistenza gruppo
            if (provider.getGroup(groupProfile.getGroupId()) != null)
                throw new DocerException("Gruppo esistente: " + groupProfile.getGroupId());

            // verifica esistenza utente
            if (provider.getUser(groupProfile.getGroupId()) != null)
                throw new DocerException("non e' ammesso assegnare ad un gruppo lo stesso id di un utente: " + groupProfile.getGroupId());

            // verifica esistenza gruppo parent se specificato
            if (groupProfile.getParentGroupId() != null && !groupProfile.getParentGroupId().equals(VOID)) {
                if (provider.getGroup(groupProfile.getParentGroupId()) == null)
                    throw new DocerException("Parent group non trovato: " + groupProfile.getParentGroupId());
            }

            provider.createGroup(groupProfile);

        } catch (DocerException docEx) {

            throw new DocerException(485, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(585, err.getMessage());
        }
    }

    // 86
    public void updateGroup(String ticket, String groupId, Map<String, String> info) throws DocerException {

        try {

            if (groupId == null)
                throw new DocerException("groupId null");

            info = toUCMap(info, "info");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            // verifica presenza del parametro id
            if (groupId == null || groupId.equals(VOID))
                throw new DocerException("Gruppo non trovato: specificare groupId");

            if (info.get(Constants.group_parent_group_id) != null && !info.get(Constants.group_parent_group_id).equals(VOID) && info.get(Constants.group_parent_group_id).equals(groupId))
                throw new DocerException("gruppo parent coincidente con il gruppo stesso");

            IGroupProfileInfo newGroupProfile = new GroupProfileInfo();

            for (String key : info.keySet()) {

                if (key == null)
                    continue;

                if (key.equalsIgnoreCase(Constants.group_group_id)) {
                    newGroupProfile.setGroupId(info.get(key));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.group_group_name)) {
                    newGroupProfile.setGroupName(info.get(key));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.group_parent_group_id)) {
                    newGroupProfile.setParentGroupId(info.get(key));
                    continue;
                }
                if (key.equalsIgnoreCase(Constants.group_enabled)) {
                    newGroupProfile.setEnabled(getEnumBoolean(info.get(Constants.group_enabled)));
                    continue;
                }

                newGroupProfile.getExtraInfo().put(key, info.get(key));
            }

            // -- DA FARE -- sollevare eccezione solo se cambia
            if (newGroupProfile.getGroupId() != null)
                throw new DocerException("non e' permesso modificare GROUP_ID");

            // verifica esistenza gruppo
            if (provider.getGroup(groupId) == null)
                throw new DocerException("Gruppo non trovato: " + groupId);

            // verifica esistenza del gruppo primario
            if (newGroupProfile.getParentGroupId() != null && !newGroupProfile.getParentGroupId().equals(VOID) && provider.getGroup(newGroupProfile.getParentGroupId()) == null)
                throw new DocerException("Parent group non trovato: " + newGroupProfile.getParentGroupId());

            provider.updateGroup(groupId, newGroupProfile);

        } catch (DocerException docEx) {

            throw new DocerException(486, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(586, err.getMessage());
        }
    }

    // 87
    public Map<String, String> getGroup(String ticket, String groupId) throws DocerException {

        try {

            if (groupId == null)
                throw new DocerException("groupId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // boolean isManager = provider.userIsManager(userid);
            // if (!isManager)
            // // l'utente non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            // verifica presenza del parametro id
            if (groupId == null || groupId.equals(VOID))
                throw new DocerException("Gruppo non trovato: specificare groupId");

            // verifica esistenza utente
            IGroupInfo groupInfo = provider.getGroup(groupId);

            if (groupInfo == null)
                return null;

            Map<String, String> group = new HashMap<String, String>();

            group.put(Constants.group_group_id, groupInfo.getProfileInfo().getGroupId());
            group.put(Constants.group_group_name, groupInfo.getProfileInfo().getGroupName());
            group.put(Constants.group_parent_group_id, groupInfo.getProfileInfo().getParentGroupId());

            if (groupInfo.getProfileInfo().getEnabled().equals(EnumBoolean.TRUE))
                group.put(Constants.group_enabled, "true");
            else if (groupInfo.getProfileInfo().getEnabled().equals(EnumBoolean.FALSE))
                group.put(Constants.group_enabled, "false");

            group.put(Constants.group_gruppo_struttura, String.valueOf(Boolean.valueOf(groupInfo.getProfileInfo().getExtraInfo().get(Constants.group_gruppo_struttura))));



            // le extrainfo
            for (String key : groupInfo.getProfileInfo().getExtraInfo().keySet()) {
                group.put(key, groupInfo.getProfileInfo().getExtraInfo().get(key));
            }
            return group;

        } catch (DocerException docEx) {

            throw new DocerException(487, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(587, err.getMessage());
        }
    }

    // 88
    public void updateUsersOfGroup(String ticket, String groupId, List<String> usersToAdd, List<String> usersToRemove) throws DocerException {

        try {

            if (groupId == null)
                throw new DocerException("groupId null");

            if (usersToAdd == null)
                usersToAdd = new ArrayList<String>();

            if (usersToRemove == null)
                usersToRemove = new ArrayList<String>();

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            // verifica presenza del parametro id
            if (groupId == null || groupId.equals(VOID))
                throw new DocerException("Gruppo non trovato: specificare groupId");

            // verifica esistenza gruppo
            IGroupInfo groupInfo = provider.getGroup(groupId);

            if (groupInfo == null)
                throw new DocerException("Gruppo " + groupId + " non trovato");

            List<String> finalUsersToRemove = new ArrayList<String>();
            List<String> finalUsersToAdd = new ArrayList<String>();

            // Nella lista dei gruppi da rimuovere non ci devono essere elementi
            // VOID
            for (int i = 0; i < usersToRemove.size(); i++) {
                if (!usersToRemove.get(i).equals(VOID))
                    finalUsersToRemove.add(usersToRemove.get(i));
            }

            // Nella lista dei gruppi da aggiungere non ci devono essere
            // elementi VOID
            for (int i = 0; i < usersToAdd.size(); i++) {
                if (!usersToAdd.get(i).equals(VOID))
                    finalUsersToAdd.add(usersToAdd.get(i));
            }

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            List<IUserProfileInfo> users = null;

            // rimuovo gli utenti eventuali
            if (finalUsersToRemove.size() > 0) {

                searchCriteria.clear();

                // controllo degli utenti da rimuovere
                searchCriteria.put(Constants.user_user_id, finalUsersToRemove);

                users = provider.searchUsers(searchCriteria);

                for (String id : finalUsersToRemove) {

                    boolean found = false;

                    for (IUserProfileInfo userProfile : users) {
                        if (userProfile.getUserId().equals(id)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        throw new DocerException("Utente da rimuovere non trovato: " + id);
                }

                // rimuovo utenti
                provider.removeUsersFromGroup(groupId, finalUsersToRemove);

            }

            searchCriteria.clear();

            // aggiungo gli utenti eventuali
            if (finalUsersToAdd.size() > 0) {

                // controllo degli utenti da aggiungere
                searchCriteria.put(Constants.user_user_id, finalUsersToAdd);

                users = provider.searchUsers(searchCriteria);

                for (String id : finalUsersToAdd) {

                    boolean found = false;

                    for (IUserProfileInfo userProfile : users) {
                        if (userProfile.getUserId().equals(id)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        throw new DocerException("Utente da aggiungere non trovato: " + id);
                }

                // aggiungo utenti
                provider.addUsersToGroup(groupId, finalUsersToAdd);
            }

        } catch (DocerException docEx) {

            throw new DocerException(488, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(588, err.getMessage());
        }

    }

    // 89
    public void setUsersOfGroup(String ticket, String groupId, List<String> usersToSet) throws DocerException {

        try {

            if (groupId == null)
                throw new DocerException("groupId null");

            if (usersToSet == null)
                throw new DocerException("usersToSet null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager =
            // provider.userIsManager(loginUserInfo.extractUserProfile());
            // if (!isManager)
            // // l'utente che sta tentando il login non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            // verifica presenza del parametro id
            if (groupId == null || groupId.equals(VOID))
                throw new DocerException("Gruppo non trovato: specificare groupId");

            // verifica esistenza gruppo
            IGroupInfo groupInfo = provider.getGroup(groupId);

            if (groupInfo == null)
                throw new DocerException("Gruppo " + groupId + " non trovato");

            List<String> finalUsersToSet = new ArrayList<String>();

            // gli utenti da aggiungere non devono essere VOID
            for (int i = 0; i < usersToSet.size(); i++) {
                if (!usersToSet.get(i).equals(VOID))
                    finalUsersToSet.add(usersToSet.get(i));
            }

            // controllo degli utenti da aggiungere
            if (finalUsersToSet.size() > 0) {

                Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
                searchCriteria.put(Constants.user_user_id, finalUsersToSet);

                List<IUserProfileInfo> users = provider.searchUsers(searchCriteria);

                for (String id : finalUsersToSet) {

                    boolean found = false;

                    for (IUserProfileInfo userProfile : users) {
                        if (userProfile.getUserId().equals(id)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        throw new DocerException("Utente da assegnare non trovato: " + id);
                }
            }

            // rimuovo tutti gli utenti del gruppo
            provider.removeUsersFromGroup(groupId, groupInfo.getUsers());

            // aggiungo i nuovi utenti
            if (finalUsersToSet.size() > 0) {
                provider.addUsersToGroup(groupId, finalUsersToSet);
            }

        } catch (DocerException docEx) {

            throw new DocerException(489, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(589, err.getMessage());
        }

    }

    // 90
    public List<String> getUsersOfGroup(String ticket, String groupId) throws DocerException {

        try {

            if (groupId == null)
                throw new DocerException("groupId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // test della library

            // controllo credenziali di manager
            // boolean isManager = provider.userIsManager(userid);
            // if (!isManager)
            // // l'utente non e' un manager
            // throw new DocerException(
            // "Utente non autorizzato per le operazioni di management");

            // verifica presenza del parametro id
            if (groupId == null || groupId.equals(VOID))
                throw new DocerException("Gruppo non trovato: specificare groupId");

            // verifica esistenza gruppo
            IGroupInfo groupInfo = provider.getGroup(groupId);

            if (groupInfo == null)
                throw new DocerException("Gruppo " + groupId + " non trovato");

            return groupInfo.getUsers();

        } catch (DocerException docEx) {

            throw new DocerException(490, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(590, err.getMessage());
        }

    }

    // 91
    public List<ISearchItem> searchDocuments(String ticket, Map<String, List<String>> searchCriteria, List<String> keywords, int maxRows, Map<String, EnumSearchOrder> orderby) throws DocerException {

        long start = new Date().getTime();

        searchCriteria = toUCMapOfList(searchCriteria);

        List<String> outputKeywords = new ArrayList<String>();

        try {

            // CheckFields.checkSearchCriteria(searchCriteria);

            // if (outputKeywords == null)
            // outputKeywords = new ArrayList<String>();

            // le keywords non possono essere null
            if (keywords != null)
                for (int i = 0; i < keywords.size(); i++) {
                    if (keywords.get(i) == null)
                        continue;

                    outputKeywords.add(keywords.get(i));
                }

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            if (maxRows < 0)
                maxRows = PRIMARYSEARCH_MAX_ROWS;

            List<String> returnProperties = BL_CONFIGURATIONS.getConfig(entetoken).getHitlists().get("HITLIST");

            DataTable<String> dtresults = provider.searchDocuments(searchCriteria, outputKeywords, returnProperties, maxRows, orderby);

            long end1 = new Date().getTime();

            List<ISearchItem> results = new ArrayList<ISearchItem>();

            for (int i = 0; i < dtresults.getRows().size(); i++) {
                DataRow<String> dr = dtresults.getRow(i);

                List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
                ISearchItem searchItem = new SearchItem();

                for (String columnName : returnProperties) {

                    String val = dr.get(columnName);
                    if (val == null) {
                        val = VOID;
                    }

                    KeyValuePair kvp = new KeyValuePair(columnName, val);

                    kvpList.add(kvp);
                }

                searchItem.setMetadata(kvpList.toArray(new KeyValuePair[0]));

                results.add(searchItem);
            }

            long end2 = new Date().getTime();

            System.out.println("tempo search da provider: " + (end1 - start) + " msec");

            System.out.println("tempo search da business logic: " + (end2 - start) + " msec");

            return results;

        } catch (DocerException docEx) {

            throw new DocerException(491, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(591, err.getMessage());
        }
    }

    // 92
    public List<ISearchItem> searchAnagraficheEstesa(String ticket, String type, Map<String, List<String>> searchCriteria, int maxRows, Map<String, EnumSearchOrder> orderby) throws DocerException {

        searchCriteria = toUCMapOfList(searchCriteria);

        try {

            if (type == null)
                throw new DocerException("type null");

            type = type.toUpperCase();

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // AnagraficaType anagraficaType =
            // DOCER_CONFIGURATIONS.getConfig(loginUserInfo.getCodiceEnte()).getAnagraficaTypesMapping().get(type);
            // if (anagraficaType == null)
            // throw new
            // DocerException("Anagrafica non definita in configurazione Business Logic: "
            // + type);

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            List<String> hitlist = BL_CONFIGURATIONS.getConfig(entetoken).getHitlists().get("HITLIST_" + type);

            List<Map<String, String>> res = provider.searchAnagraficheEstesa(type, searchCriteria, hitlist, maxRows, orderby);

            List<ISearchItem> results = new ArrayList<ISearchItem>();

            for (Map<String, String> r : res) {

                SearchItem si = new SearchItem();

                List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
                for (Entry<String, String> entry : r.entrySet()) {
                    KeyValuePair kvp = new KeyValuePair(entry.getKey(), entry.getValue());
                    kvpList.add(kvp);
                }

                si.setMetadata(kvpList.toArray(new KeyValuePair[0]));

                results.add(si);
            }

            return results;

        } catch (DocerException docEx) {

            throw new DocerException(492, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(592, err.getMessage());
        }
    }

    // 92
    public List<ISearchItem> searchAnagrafiche(String ticket, String type, Map<String, List<String>> searchCriteria) throws DocerException {

        searchCriteria = toUCMapOfList(searchCriteria);

        try {

            if (type == null)
                throw new DocerException("type null");

            type = type.toUpperCase();

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // AnagraficaType anagraficaType =
            // DOCER_CONFIGURATIONS.getConfig(loginUserInfo.getCodiceEnte()).getAnagraficaTypesMapping().get(type);
            // if (anagraficaType == null)
            // throw new
            // DocerException("Anagrafica non definita in configurazione Business Logic: "
            // + type);

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            List<String> hitlist = BL_CONFIGURATIONS.getConfig(entetoken).getHitlists().get("HITLIST_" + type);

            List<Map<String, String>> res = provider.searchAnagrafiche(type, searchCriteria, hitlist);

            List<ISearchItem> results = new ArrayList<ISearchItem>();

            for (Map<String, String> r : res) {

                SearchItem si = new SearchItem();

                List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
                for (Entry<String, String> entry : r.entrySet()) {
                    KeyValuePair kvp = new KeyValuePair(entry.getKey(), entry.getValue());
                    kvpList.add(kvp);
                }

                si.setMetadata(kvpList.toArray(new KeyValuePair[0]));

                results.add(si);
            }

            return results;

        } catch (DocerException docEx) {

            throw new DocerException(492, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(592, err.getMessage());
        }
    }

    // 93
    public List<ISearchItem> searchUsers(String ticket, Map<String, List<String>> searchCriteria) throws DocerException {

        searchCriteria = toUCMapOfList(searchCriteria);

        try {

            // CheckFields.checkSearchCriteria(searchCriteria);

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

	    /*
         * // controllo credenziali di manager boolean isManager =
	     * provider.userIsManager(userid); if (!isManager) // l'utente non
	     * e' un manager throw new DocerException(
	     * "Utente non autorizzato per le operazioni di management");
	     */

            List<IUserProfileInfo> usersFound = provider.searchUsers(searchCriteria);

            List<ISearchItem> results = new ArrayList<ISearchItem>();

            for (IUserProfileInfo profileInfo : usersFound) {
                ISearchItem user = new SearchItem();

                List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();

                kvpList.add(new KeyValuePair(Constants.user_user_id, profileInfo.getUserId()));
                kvpList.add(new KeyValuePair(Constants.user_full_name, profileInfo.getFullName()));
                kvpList.add(new KeyValuePair(Constants.user_email_address, profileInfo.getEmailAddress()));
                kvpList.add(new KeyValuePair(Constants.user_first_name, profileInfo.getFirstName()));
                kvpList.add(new KeyValuePair(Constants.user_last_name, profileInfo.getLastName()));
                kvpList.add(new KeyValuePair(Constants.user_network_alias, profileInfo.getNetworkAlias()));
                kvpList.add(new KeyValuePair(Constants.user_enabled, profileInfo.getEnabled().toString().toLowerCase()));

                for (String key : profileInfo.getExtraInfo().keySet()) {
                    kvpList.add(new KeyValuePair(key, profileInfo.getExtraInfo().get(key)));
                }

                user.setMetadata(kvpList.toArray(new KeyValuePair[0]));
                results.add(user);
            }

            return results;

        } catch (DocerException docEx) {

            throw new DocerException(493, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(593, err.getMessage());
        }
    }

    // 94
    public List<ISearchItem> searchGroups(String ticket, Map<String, List<String>> searchCriteria) throws DocerException {

        searchCriteria = toUCMapOfList(searchCriteria);

        try {

            // CheckFields.checkSearchCriteria(searchCriteria);

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

	    /*
         * // controllo credenziali di manager boolean isManager =
	     * provider.userIsManager(userid); if (!isManager) // l'utente non
	     * e' un manager throw new DocerException(
	     * "Utente non autorizzato per le operazioni di management");
	     */

            List<IGroupProfileInfo> groupsFound = provider.searchGroups(searchCriteria);

            List<ISearchItem> results = new ArrayList<ISearchItem>();

            for (IGroupProfileInfo profileInfo : groupsFound) {
                ISearchItem group = new SearchItem();

                List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();

                kvpList.add(new KeyValuePair(Constants.group_group_id, profileInfo.getGroupId()));
                kvpList.add(new KeyValuePair(Constants.group_group_name, profileInfo.getGroupName()));
                kvpList.add(new KeyValuePair(Constants.group_parent_group_id, profileInfo.getParentGroupId()));
                kvpList.add(new KeyValuePair(Constants.group_enabled, VOID));

                if (profileInfo.getEnabled().equals(EnumBoolean.TRUE))
                    kvpList.add(new KeyValuePair(Constants.group_enabled, "true"));
                else if (profileInfo.getEnabled().equals(EnumBoolean.FALSE))
                    kvpList.add(new KeyValuePair(Constants.group_enabled, "false"));

                kvpList.add(new KeyValuePair(Constants.group_gruppo_struttura, String.valueOf(Boolean.valueOf(profileInfo.getExtraInfo().get(Constants.group_gruppo_struttura)))));



                for (String key : profileInfo.getExtraInfo().keySet()) {
                    kvpList.add(new KeyValuePair(key, profileInfo.getExtraInfo().get(key)));
                }

                group.setMetadata(kvpList.toArray(new KeyValuePair[0]));

                results.add(group);
            }

            return results;

        } catch (DocerException docEx) {

            throw new DocerException(494, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(594, err.getMessage());
        }

    }

    // 95
    public List<IKeyValuePair> getDocumentTypes(String ticket) throws DocerException {

        try {

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            List<IKeyValuePair> result = new ArrayList<IKeyValuePair>();

            List<DocumentType> dts = BL_CONFIGURATIONS.getConfig(entetoken).getDocumentTypesMapping().getDocumentTypes(null, null);

            if (dts != null) {
                for (DocumentType docType : dts) {
                    result.add(new KeyValuePair(docType.getTypeId(), docType.getDescription()));
                }
            }

            return result;

        }
        // catch (DocerException docEx) {
        //
        // throw new DocerException(495, docEx.getMessage());
        //
        // }
        catch (Exception err) {
            throw new DocerException(595, err.getMessage());
        }
    }

    // 96
    public boolean verifyTicket(String ticket) {

        // if(!CONFIG_INITIALIZED){
        // throw new DocerException("Configurazione non inizializzata: "
        // +CONFIG_ERROR_MESSAGE);
        // }

        try {

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // non bisogna impostare il current user
            return provider.verifyTicket(loginUserInfo.getUserId(), loginUserInfo.getCodiceEnte(), loginUserInfo.getTicket());

        } catch (Exception err) {
            return false;
        }
    }

    // 97
    public void setACLTitolario(String ticket, Map<String, String> id, Map<String, EnumACLRights> acls) throws DocerException {

        try {

            if (id == null)
                throw new DocerException("Titolario id null");

            if (acls == null)
                throw new DocerException("acls is null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            String codEnte = id.get(Constants.titolario_cod_ente);
            String codAoo = id.get(Constants.titolario_cod_aoo);
            String codTitolario = id.get(Constants.titolario_cod_titolario);
            String classifica = id.get(Constants.titolario_classifica);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;
            String pianoClass = isPianoClassEnabled ?
                    (id.get(Constants.titolario_piano_class) != null ? id.get(Constants.titolario_piano_class) : pianoClassDefault)
                    : null;

//            // DOCER-36 Controllo se devo gestire il Piano Class
//            String pianoClass = id.get(Constants.titolario_piano_class) != null ? id.get(Constants.titolario_piano_class) : "";

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("specificare COD_AOO");

            if (codTitolario == null || codTitolario.equals(VOID)) {
                if (classifica == null || classifica.equals(VOID))
                    throw new DocerException("specificare COD_TITOLARIO o CLASSIFICA");
            }

            ITitolarioId titolarioId = new TitolarioId();
            titolarioId.setCodiceEnte(codEnte);
            titolarioId.setCodiceAOO(codAoo);
            titolarioId.setCodiceTitolario(codTitolario);
            titolarioId.setClassifica(classifica);
            // DOCER-36 Piano Class
            //if (!StringUtils.isEmpty(pianoClass))
                titolarioId.setPianoClassificazione(pianoClass);

            provider.setACLTitolario(titolarioId, acls);

        } catch (DocerException docEx) {

            throw new DocerException(497, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(597, err.getMessage());
        }

    }

    // 98
    public Map<String, EnumACLRights> getACLTitolario(String ticket, Map<String, String> id) throws DocerException {

        try {

            if (id == null)
                throw new DocerException("Titolario id null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            String codEnte = id.get(Constants.titolario_cod_ente);
            String codAoo = id.get(Constants.titolario_cod_aoo);
            String codTitolario = id.get(Constants.titolario_cod_titolario);
            String classifica = id.get(Constants.titolario_classifica);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;
            String pianoClass = isPianoClassEnabled ?
                    (id.get(Constants.fascicolo_piano_class) != null ? id.get(Constants.fascicolo_piano_class) : pianoClassDefault)
                    : null;

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("specificare COD_AOO");

            if (codTitolario == null || codTitolario.equals(VOID)) {
                if (classifica == null || classifica.equals(VOID))
                    throw new DocerException("specificare COD_TITOLARIO o CLASSIFICA");
            }

            ITitolarioId titolarioId = new TitolarioId();
            titolarioId.setCodiceEnte(codEnte);
            titolarioId.setCodiceAOO(codAoo);
            titolarioId.setCodiceTitolario(codTitolario);
            titolarioId.setClassifica(classifica);
            // DOCER-36 Piano Class
            if (!StringUtils.isEmpty(pianoClass))
                titolarioId.setPianoClassificazione(pianoClass);

            return provider.getACLTitolario(titolarioId);

        } catch (DocerException docEx) {

            throw new DocerException(498, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(598, err.getMessage());
        }

    }

    // 99
    public void setACLFascicolo(String ticket, Map<String, String> id, Map<String, EnumACLRights> acls) throws DocerException {

        try {

            if (id == null)
                throw new DocerException("Fascicolo id null");

            if (acls == null)
                throw new DocerException("acls null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            String codEnte = id.get(Constants.fascicolo_cod_ente);
            String codAoo = id.get(Constants.fascicolo_cod_aoo);
            String classifica = id.get(Constants.fascicolo_classifica);
            String numFascicolo = id.get(Constants.fascicolo_num_fascicolo);
            String annoFascicolo = id.get(Constants.fascicolo_anno_fascicolo);
            String progressivoFascicolo = id.get(Constants.fascicolo_progr_fascicolo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;

            String pianoClass = null;
            //se pianoClass è abilitato
            if(isPianoClassEnabled){
                pianoClass = id.get(Constants.fascicolo_piano_class) != null ? id.get(Constants.fascicolo_piano_class) : null;
                //se pianoClass non è passato in input alla chiamata
                if(pianoClass==null){
                    //recupero dalla configurazione il piano di classificazione (se presente) per l'anno del fascicolo che si sta tentando di creare, altrimenti null
                    String pianoClassByAnno = BL_CONFIGURATIONS.getConfig(entetoken).getStoricoPianoClass(codEnte,codAoo,id.get(Constants.fascicolo_anno_fascicolo));

                    if(pianoClassByAnno!=null){
                        //cerco il titolario con pianoClass uguale a pianoClassByAnno
                        Map<String, String> idCritTitolario = new HashMap<String, String>();
                        idCritTitolario.put(Constants.fascicolo_cod_ente, codEnte);
                        idCritTitolario.put(Constants.fascicolo_cod_aoo, codAoo);
                        idCritTitolario.put(Constants.fascicolo_classifica, id.get(Constants.fascicolo_classifica));
                        idCritTitolario.put(Constants.fascicolo_piano_class, pianoClassByAnno);
                        // cerco il titolario
                        Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, idCritTitolario);
                        if (ptit != null) {
                            pianoClass = pianoClassByAnno;
                        }else{
                            throw new DocerException("Titolario " + idCritTitolario.toString() + " non trovato");
                        }
                    }else{
                        throw new DocerException("Piano di classificazione non configurato per l'anno "+id.get(Constants.fascicolo_anno_fascicolo));
                    }
                }
            }

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

            if (classifica == null || classifica.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

            if (numFascicolo == null || numFascicolo.equals(VOID)) {
                if (progressivoFascicolo == null || progressivoFascicolo.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare NUM_FASCICOLO o PROGR_FASCICOLO");
            }

            if (annoFascicolo == null || annoFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

            IFascicoloId fascicoloId = new FascicoloId();
            fascicoloId.setCodiceEnte(codEnte);
            fascicoloId.setCodiceAOO(codAoo);
            fascicoloId.setClassifica(classifica);
            fascicoloId.setNumeroFascicolo(numFascicolo);
            fascicoloId.setProgressivo(progressivoFascicolo);
            fascicoloId.setAnnoFascicolo(annoFascicolo);
            // DOCER-36 Piano Class
            if (!StringUtils.isEmpty(pianoClass))
                fascicoloId.setPianoClassificazione(pianoClass);

            provider.setACLFascicolo(fascicoloId, acls);

        } catch (DocerException docEx) {

            throw new DocerException(499, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(599, err.getMessage());
        }

    }

    // 10
    public Map<String, EnumACLRights> getACLFascicolo(String ticket, Map<String, String> id) throws DocerException {

        try {
            if (id == null)
                throw new DocerException("anagrafica id null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            String codEnte = id.get(Constants.fascicolo_cod_ente);
            String codAoo = id.get(Constants.fascicolo_cod_aoo);
            String classifica = id.get(Constants.fascicolo_classifica);
            String numFascicolo = id.get(Constants.fascicolo_num_fascicolo);
            String progressivoFascicolo = id.get(Constants.fascicolo_progr_fascicolo);
            String annoFascicolo = id.get(Constants.fascicolo_anno_fascicolo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;

            String pianoClass = null;
            //se pianoClass è abilitato
            if(isPianoClassEnabled){
                pianoClass = id.get(Constants.fascicolo_piano_class) != null ? id.get(Constants.fascicolo_piano_class) : null;
                //se pianoClass non è passato in input alla chiamata
                if(pianoClass==null){
                    //recupero dalla configurazione il piano di classificazione (se presente) per l'anno del fascicolo che si sta tentando di creare, altrimenti null
                    String pianoClassByAnno = BL_CONFIGURATIONS.getConfig(entetoken).getStoricoPianoClass(codEnte,codAoo,id.get(Constants.fascicolo_anno_fascicolo));

                    if(pianoClassByAnno!=null){
                        //cerco il titolario con pianoClass uguale a pianoClassByAnno
                        Map<String, String> idCritTitolario = new HashMap<String, String>();
                        idCritTitolario.put(Constants.fascicolo_cod_ente, codEnte);
                        idCritTitolario.put(Constants.fascicolo_cod_aoo, codAoo);
                        idCritTitolario.put(Constants.fascicolo_classifica, id.get(Constants.fascicolo_classifica));
                        idCritTitolario.put(Constants.fascicolo_piano_class, pianoClassByAnno);
                        // cerco il titolario
                        Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, idCritTitolario);
                        if (ptit != null) {
                            pianoClass = pianoClassByAnno;
                        }else{
                            throw new DocerException("Titolario " + idCritTitolario.toString() + " non trovato");
                        }
                    }else{
                        throw new DocerException("Piano di classificazione non configurato per l'anno "+id.get(Constants.fascicolo_anno_fascicolo));
                    }
                }
            }

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

            if (classifica == null || classifica.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

            if (numFascicolo == null || numFascicolo.equals(VOID)) {
                if (progressivoFascicolo == null || progressivoFascicolo.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare NUM_FASCICOLO o PROGR_FASCICOLO");
            }

            if (annoFascicolo == null || annoFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

            IFascicoloId fascicoloId = new FascicoloId();
            fascicoloId.setCodiceEnte(codEnte);
            fascicoloId.setCodiceAOO(codAoo);
            fascicoloId.setClassifica(classifica);
            fascicoloId.setNumeroFascicolo(numFascicolo);
            fascicoloId.setProgressivo(progressivoFascicolo);
            fascicoloId.setAnnoFascicolo(annoFascicolo);
            // DOCER-36 Piano Class
            if (!StringUtils.isEmpty(pianoClass))
                fascicoloId.setPianoClassificazione(pianoClass);

            Map<String, EnumACLRights> acls = provider.getACLFascicolo(fascicoloId);

            return acls;

        } catch (DocerException docEx) {

            throw new DocerException(410, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(510, err.getMessage());
        }

    }

    // 11
    public void addRiferimentiDocuments(String ticket, String docId, List<String> toAddRiferimenti) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            if (toAddRiferimenti == null)
                throw new DocerException("lista Riferimenti null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            if (toAddRiferimenti == null || toAddRiferimenti.size() < 1)
                return;

            List<String> addingList = new ArrayList<String>();

            for (String rif : toAddRiferimenti) {
                // pulisco la collezione rimuovendo i vuoti, i doppioni e il
                // master
                if (rif == null || rif.equals(VOID) || addingList.contains(rif) || rif.equals(docId))
                    continue;

                addingList.add(rif);
            }

            // test della library

            // ILockStatus checkedOutInfo =
            // provider.isCheckedOutDocument(docId);
            // if (checkedOutInfo.getLocked()) // &&
            // // !checkedOutInfo.extractUserProfile().equalsIgnoreCase(userid))
            // throw new
            // DocerException("documento in stato di blocco esclusivo"); //
            // documento
            // // bloccato
            // // esclusivamente
            // // da
            // // un
            // // altro
            // // utente

            // controllo checkout dei Riferimenti
            // checkLockStatusRiferimentiToAdd(addingList);

            if (addingList.size() > 0)
                provider.addRiferimentiDocuments(docId, addingList);

        } catch (DocerException docEx) {

            throw new DocerException(411, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(511, err.getMessage());
        }

    }

    // 12
    public void removeRiferimentiDocuments(String ticket, String docId, List<String> toRemoveRiferimenti) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            if (toRemoveRiferimenti == null)
                throw new DocerException("lista Riferimenti null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            if (toRemoveRiferimenti == null || toRemoveRiferimenti.size() < 1)
                return;

            List<String> removingList = new ArrayList<String>();

            for (String rif : toRemoveRiferimenti) {
                // pulisco la collezione rimuovendo i vuoti, i doppioni e il
                // master
                if (rif == null || rif.equals(VOID) || removingList.contains(rif) || rif.equals(docId))
                    continue;

                removingList.add(rif);
            }

            // test della library

            // ILockStatus checkedOutInfo =
            // provider.isCheckedOutDocument(docId);
            // if (checkedOutInfo.getLocked()) // &&
            // // !checkedOutInfo.extractUserProfile().equalsIgnoreCase(userid))
            // throw new
            // DocerException("documento in stato di blocco esclusivo"); //
            // documento
            // // bloccato
            // // esclusivamente
            // // da
            // // un
            // // altro
            // // utente

            // controllo checkout dei Riferimenti
            // checkLockStatusRiferimentiToRemove(removingList);

            if (removingList.size() > 0)
                provider.removeRiferimentiDocuments(docId, removingList);

        } catch (DocerException docEx) {

            throw new DocerException(412, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(512, err.getMessage());
        }

    }

    // 13
    public List<String> getRiferimentiDocuments(String ticket, String docId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // Map<String, List<String>> searchCriteria = new HashMap<String,
            // List<String>>();
            // impostazione dell'unico criterio di ricerca
            // List<String> criteria = new ArrayList<String>();
            // criteria.add(docId);
            // searchCriteria.put(Constants.doc_docnum, criteria);
            //
            // List<String> returnProperties = new ArrayList<String>();
            // returnProperties.add(Constants.doc_docnum);

            // test della library

            // // RICERCA PRIMARIA
            // DataTable<String> searchResults = provider.searchDocuments(
            // searchCriteria, null, returnProperties,
            // PRIMARYSEARCH_MAX_ROWS, null);

            // // deve esserci un solo risultato
            // if (searchResults.getRows().size() == 0)
            // throw new DocerException("documento " +docId +" non trovato");
            // if (searchResults.getRows().size() != 1)
            // throw new DocerException("docId " +docId +" non univoco");

            List<String> riferimenti = provider.getRiferimentiDocuments(docId);
            riferimenti.remove(docId);
            return riferimenti;

        } catch (DocerException docEx) {

            throw new DocerException(413, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(513, err.getMessage());
        }
    }

    // 14
    public void protocollaDocumento(String ticket, String docId, Map<String, String> metadata) throws DocerException {

        try {

            metadata = toUCMap(metadata, "metadata");

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            DataRow<String> oldProfileData = getOldProfile(OPERAZIONE.protocollazione, docId);
            doOperationCheck(OPERAZIONE.protocollazione, docId, loginUserInfo.getUserId(), oldProfileData, metadata);

            Map<String, String> metadatiProtocollazione = checkAndGetMetadatiProtocollazione(oldProfileData, metadata);

            String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
            String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);

            EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));
            EnumStatoArchivistico newStatoArchivistico = getEnumStatoArchivistico(metadatiProtocollazione.get(Constants.doc_stato_archivistico));

            EnumStatiPantarei newStatoPantarei = getEnumStatoPantarei(metadatiProtocollazione.get(Constants.doc_stato_pantarei));

            // recupero lista dei related
            List<String> relatedChain = provider.getRelatedDocuments(docId);
            // rimuovo dalla lista il MASTER se presente
            relatedChain.remove(docId);

            Map<String, EnumACLRights> newACL = null;
            if (oldStatoArchivistico.getCode() < EnumStatoArchivistico.registrato.getCode() && newStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
                newACL = provider.getACLDocument(docId);
            }

            Map<String, Map<String, String>> relNewProperties = checkRecordRelatedChain(newStatoArchivistico, newStatoPantarei, oldCodEnte, oldCodAOO, docId, loginUserInfo.getUserId(), relatedChain);

            // advanced versions
            setAdvancedVersionsDocnumRecord(docId, oldStatoArchivistico, newStatoArchivistico, "protocollazione");

            // aggiorno i profili dei related
            for (String relatedId : relNewProperties.keySet()) {

                // aggiorno il profilo del related
                Map<String, String> newProperties = relNewProperties.get(relatedId);

                if (newProperties != null) {
                    // aggiorno il profilo del related
                    provider.updateProfileDocument(relatedId, newProperties);
                }

                if (newACL != null) {
                    provider.setACLDocument(relatedId, newACL);
                }
            }

            metadatiProtocollazione.put(Constants.doc_tipo_componente, EnumTipiComponente.PRINCIPALE.toString());
            metadatiProtocollazione.put(Constants.doc_docnum_princ, VOID);
            metadatiProtocollazione.put(Constants.doc_docnum_record, VOID);
            provider.updateProfileDocument(docId, metadatiProtocollazione);

            //controlla ed eventualmente aggiorna STATO_CONSERV
            updateStatoConserv(docId, null);

        } catch (DocerException docEx) {

            throw new DocerException(443, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(543, err.getMessage());
        }
    }

    // 15
    public void registraDocumento(String ticket, String docId, Map<String, String> metadata) throws DocerException {

        try {

            metadata = toUCMap(metadata, "metadata");

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            DataRow<String> oldProfileData = getOldProfile(OPERAZIONE.registrazione, docId);
            doOperationCheck(OPERAZIONE.registrazione, docId, loginUserInfo.getUserId(), oldProfileData, metadata);

            // **** controllo e profilazione dei dati di registro ****
            Map<String, String> metadatiRegistrazione = checkAndGetMetadatiRegistrazione(oldProfileData, metadata);

            String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
            String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);
            EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));
            EnumStatoArchivistico newStatoArchivistico = getEnumStatoArchivistico(metadatiRegistrazione.get(Constants.doc_stato_archivistico));

            EnumStatiPantarei newStatoPantarei = getEnumStatoPantarei(metadatiRegistrazione.get(Constants.doc_stato_pantarei));

            // recupero lista dei related
            List<String> relatedChain = provider.getRelatedDocuments(docId);
            // rimuovo dalla lista il MASTER se presente
            relatedChain.remove(docId);

            Map<String, EnumACLRights> newACL = null;
            if (oldStatoArchivistico.getCode() < EnumStatoArchivistico.registrato.getCode() && newStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
                newACL = provider.getACLDocument(docId);
            }

            Map<String, Map<String, String>> relNewProperties = checkRecordRelatedChain(newStatoArchivistico, newStatoPantarei, oldCodEnte, oldCodAOO, docId, loginUserInfo.getUserId(), relatedChain);

            // advanced versions
            setAdvancedVersionsDocnumRecord(docId, oldStatoArchivistico, newStatoArchivistico, "registrazione");

            // aggiorno i profili se sono cambiati
            for (String relatedId : relNewProperties.keySet()) {

                // aggiorno il profilo del related
                Map<String, String> newProperties = relNewProperties.get(relatedId);

                if (newProperties != null) {
                    // il profilo lo aggiorno solo se e' stato specificato
                    provider.updateProfileDocument(relatedId, newProperties);
                }

                if (newACL != null) {
                    provider.setACLDocument(relatedId, newACL);
                }
            }

            metadatiRegistrazione.put(Constants.doc_tipo_componente, EnumTipiComponente.PRINCIPALE.toString());
            metadatiRegistrazione.put(Constants.doc_docnum_princ, VOID);
            metadatiRegistrazione.put(Constants.doc_docnum_record, VOID);
            provider.updateProfileDocument(docId, metadatiRegistrazione);

            //controlla ed eventualmente aggiorna STATO_CONSERV
            updateStatoConserv(docId, null);

        } catch (DocerException docEx) {

            throw new DocerException(415, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(515, err.getMessage());
        }
    }


    // 16
    public void fascicolaDocumento(String ticket, String docId, Map<String, String> metadata) throws DocerException {

        try {

            metadata = toUCMap(metadata, "metadata");

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            DataRow<String> oldProfileData = getOldProfile(OPERAZIONE.fascicolazione, docId);
            String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
            String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(oldCodEnte, oldCodAOO);
            boolean isPianoClassEnabled = pianoClassDefault != null;

            // DOCER-36 recupero il piano_class dal fascicolo
            /*if (isPianoClassEnabled && metadata.size() > 0) {
                metadata.put("PIANO_CLASS", pianoClassDefault);
                Map<String, String> fascicolo = getFascicolo(ticket, metadata);
                if (fascicolo == null)
                    throw new DocerException("Fascicolo non trovato per il piano di classificazione " + pianoClassDefault);
            }*/

            if(isPianoClassEnabled && metadata.size() > 0){
                String pianoClassInput = metadata.get(Constants.fascicolo_piano_class);
                if(pianoClassInput==null){
                    if(metadata.get(Constants.fascicolo_anno_fascicolo)!=null) {
                        String pianoClassByAnno = BL_CONFIGURATIONS.getConfig(entetoken).getStoricoPianoClass(oldCodEnte, oldCodAOO, metadata.get(Constants.fascicolo_anno_fascicolo));
                        if (pianoClassByAnno != null) {
                            metadata.put("PIANO_CLASS", pianoClassByAnno);
                            Map<String, String> fascicolo = getFascicolo(ticket, metadata);
                            if (fascicolo == null)
                                throw new DocerException("Fascicolo non trovato per il piano di classificazione " + pianoClassByAnno);
                        } else {
                            throw new DocerException("Piano di classificazione non configurato per l'anno " + metadata.get(Constants.fascicolo_anno_fascicolo));
                        }
                    }
                }
            }

            doOperationCheck(OPERAZIONE.fascicolazione, docId, loginUserInfo.getUserId(), oldProfileData, metadata);

            Map<String, String> metadatiFascicolazione = checkAndGetMetadatiFascicolazione(oldProfileData, metadata);

            // checkFascicoloPrincipale(metadatiFascicolazione);
            //
            // checkFascicoliSecondari(metadatiFascicolazione);

            List<String> fascicoli_delta = getFascicoliDelta(oldProfileData, metadatiFascicolazione);

            // controllo i diritti sui fascicoli effettivamente da aggiungere o
            // da rimuovere (almeno normalAccess)
            for (String fascicoloString : fascicoli_delta) {

                checkUserRightsFascicolo(oldCodEnte, oldCodAOO, fascicoloString, loginUserInfo.getUserId(), EnumACLRights.normalAccess);
            }

            checkFascicoloPrincipale(oldProfileData, metadatiFascicolazione);

            EnumStatoArchivistico newStatoArchivistico = getEnumStatoArchivistico(metadatiFascicolazione.get(Constants.doc_stato_archivistico));

            EnumStatiPantarei newStatoPantarei = getEnumStatoPantarei(metadatiFascicolazione.get(Constants.doc_stato_pantarei));

            // modifico sempre il fascicolo dei related
            // recupero lista dei related
            List<String> relatedChain = provider.getRelatedDocuments(docId);

            // rimuovo dalla lista il MASTER se presente
            relatedChain.remove(docId);

            // annullo i riferimenti alla folder se sposto in un fascicolo
            if (fascicoli_delta.size() > 0 && StringUtils.isNotEmpty(oldProfileData.get(Constants.folder_parent_folder_id))) {
                metadatiFascicolazione.put(Constants.folder_parent_folder_id, "");
            }

            // controllo stato_pantarei, TIPO_COMPONENTE e checkoutStatus della
            // related chain e ritorno le nuove proprieta' dei profili
            Map<String, Map<String, String>> relNewProperties = checkRecordRelatedChain(newStatoArchivistico, newStatoPantarei, oldCodEnte, oldCodAOO, docId, loginUserInfo.getUserId(), relatedChain);

            for (String relatedId : relNewProperties.keySet()) {

                // aggiorno il profilo del related
                Map<String, String> newProperties = relNewProperties.get(relatedId);

                // aggiungo i metadati di fascicolo del master
                for (String key : metadatiFascicolazione.keySet()) {

                    // non sovrascrivo lo STATO_PANTAREI
                    if (key.equals(Constants.doc_stato_pantarei)) {
                        continue;
                    }

                    newProperties.put(key, metadatiFascicolazione.get(key));
                }

                // il profilo lo aggiorno solo se e' stato specificato
                if (newProperties.size() > 0)
                    provider.updateProfileDocument(relatedId, newProperties);
            }

            // advanced versions
            // EnumStatoArchivistico oldStatoArchivistico =
            // getEnumStatoArchivistico(
            // oldProfileData.get(Constants.doc_stato_archivistico));
            // setAdvancedVersionDocnumRecord(docId, oldStatoArchivistico,
            // newStatoArchivistico);

            // aggiorno il master
            if (newStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
                metadatiFascicolazione.put(Constants.doc_tipo_componente, EnumTipiComponente.PRINCIPALE.toString());
                metadatiFascicolazione.put(Constants.doc_docnum_princ, VOID);
            }

            provider.updateProfileDocument(docId, metadatiFascicolazione);

        } catch (DocerException docEx) {

            throw new DocerException(416, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(516, err.getMessage());
        }
    }

    // 17
    public EnumACLRights getUserRightsAnagrafiche(String ticket, String type, Map<String, String> id, String userId) throws DocerException {

        try {

            id = toUCMap(id, "id");


            if (userId == null || userId.equals(VOID))
                throw new DocerException("userId null");

            if (type == null || type.equals(VOID))
                throw new DocerException("TYPE_ID obbligatorio");

            type = type.toUpperCase();

            String cod_ente = id.get(Constants.ente_cod_ente);

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            String cod_aoo = id.get(Constants.aoo_cod_aoo);

            if (!type.equalsIgnoreCase(Constants.ente_type_id)) {
                AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(type, cod_ente, cod_aoo);
                if (anagraficaType == null)
                    throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + type + " per COD_ENTE: " + cod_ente + " e COD_AOO: " + cod_aoo);
            }


            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            id.remove(Constants.anagrafica_type_id);

            return provider.getEffectiveRightsAnagrafiche(type, id, userId);

        } catch (DocerException docEx) {

            throw new DocerException(417, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(517, err.getMessage());
        }

    }

    // 18
    public EnumACLRights getUserRightsFolder(String ticket, String folderId, String userId) throws DocerException {

        try {

            if (folderId == null || folderId.equals(VOID))
                throw new DocerException("folderId null");

            if (userId == null || userId.equals(VOID))
                throw new DocerException("userId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            return provider.getEffectiveRightsFolder(folderId, userId);

        } catch (DocerException docEx) {

            throw new DocerException(418, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(518, err.getMessage());
        }

    }

    // 19
    public List<IKeyValuePair> getDocumentTypes(String ticket, String codEnte, String codAOO) throws DocerException {

        if (codEnte == null || codEnte.equals(VOID))
            throw new DocerException(419, "COD_ENTE obbligatorio");

        if (codAOO == null || codAOO.equals(VOID))
            throw new DocerException(419, "COD_AOO obbligatorio");

        try {

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            List<IKeyValuePair> result = new ArrayList<IKeyValuePair>();

            List<DocumentType> dts = BL_CONFIGURATIONS.getConfig(entetoken).getDocumentTypesMapping().getDocumentTypes(codEnte, codAOO);

            if (dts != null) {
                for (DocumentType docType : dts) {
                    result.add(new KeyValuePair(docType.getTypeId(), docType.getDescription()));
                }
            }

            return result;
        } catch (Exception e) {
            throw new DocerException(519, e.getMessage());
        }
    }

    // 20
    public void classificaDocumento(String ticket, String docId, Map<String, String> metadata) throws DocerException {

        try {

            metadata = toUCMap(metadata, "metadata");

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // recupero vecchii profilo
            DataRow<String> oldProfileData = getOldProfile(OPERAZIONE.classificazione, docId);

            String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
            String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(oldCodEnte, oldCodAOO);
            boolean isPianoClassEnabled = pianoClassDefault != null;
            String pianoClass = isPianoClassEnabled ?
                    (metadata.get(Constants.titolario_piano_class) != null ? metadata.get(Constants.titolario_piano_class) : pianoClassDefault)
                    : null;

            String oldClassifica = oldProfileData.get(Constants.doc_classifica);
            if (oldClassifica == null) {
                oldClassifica = "";
            }

            String oldCodTitolario = oldProfileData.get(Constants.doc_cod_titolario);
            if (oldCodTitolario == null) {
                oldCodTitolario = "";
            }

            EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));

            if (!metadata.containsKey(Constants.doc_classifica)) {
                throw new DocerException("CLASSIFICA e' obbligatorio");
            }

            String newClassifica = metadata.get(Constants.doc_classifica);
            if (StringUtils.isEmpty(newClassifica)) {
                throw new DocerException("non e' possibile assegnare CLASSIFICA null o vuota");
            }

            String newCodTitolario = metadata.get(Constants.doc_cod_titolario);
            if (StringUtils.isNotEmpty(newCodTitolario)) {
                if (!newCodTitolario.equals(oldCodTitolario)) {
                    throw new DocerException("non e' possibile modificare COD_TITOLARIO dall'esterno");
                }
            }

            // *** CONTROLLI ***//

            doOperationCheck(OPERAZIONE.classificazione, docId, loginUserInfo.getUserId(), oldProfileData, metadata);

            // controllo e calcolo del nuovo stato archivistico
            EnumStatoArchivistico newStatoArchivistico = checkStatoArchivisticoClassificaDocumento(oldStatoArchivistico);

            // controllo del fascicolo
            String oldProgrFascicolo = oldProfileData.get(Constants.doc_progr_fascicolo);

            if (oldProgrFascicolo != null && !oldProgrFascicolo.equals(VOID))
                throw new DocerException("classificaDocumento: modifica della classifica: il documento " + docId + " possiede un Fascicolo assegnato");

            if (newClassifica.equals(oldClassifica)) {
                // se la classifica non cambia recupero quella precedente
                newClassifica = oldClassifica;
                newCodTitolario = oldCodTitolario;
            } else {
                // altrimenti ricerco la voce di titolario (devo avere almeno
                // readOnly)
                Map<String, String> titolario = getTitolario(oldCodEnte, oldCodAOO, newClassifica, pianoClass);

                newClassifica = titolario.get(Constants.doc_classifica);
                newCodTitolario = titolario.get(Constants.doc_cod_titolario);
            }

            // inseriamo vecchio codEnte e codAOO nel caso non sia stato
            // specificato
            HashMap<String, String> metadatiClassificazione = new HashMap<String, String>();
            metadatiClassificazione.put(Constants.doc_cod_ente, oldCodEnte);
            metadatiClassificazione.put(Constants.doc_cod_aoo, oldCodAOO);
            metadatiClassificazione.put(Constants.doc_classifica, newClassifica);
            metadatiClassificazione.put(Constants.doc_cod_titolario, newCodTitolario);
            metadatiClassificazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

            //setto il piano di classificazione se attivo
            if(pianoClass!=null && !"".equals(pianoClass)){
                metadatiClassificazione.put(Constants.doc_piano_class, pianoClass);
            }


            // lo stato_pantarei non cambia nella classificazione
            EnumStatiPantarei newStatoPantarei = getEnumStatoPantarei(oldProfileData.get(Constants.doc_stato_pantarei));

            // modifica la classifica dei related
            List<String> relatedChain = provider.getRelatedDocuments(docId);
            // rimuovo dalla lista il MASTER se presente
            relatedChain.remove(docId);

            // controllo stato_pantarei, DOCNUM_PRINC, TIPO_COMPONENTE e
            // checkoutStatus
            // della related chain e ritorno le nuove proprieta' dei profili
            Map<String, Map<String, String>> relNewProperties = checkRecordRelatedChain(newStatoArchivistico, newStatoPantarei, oldCodEnte, oldCodAOO, docId, loginUserInfo.getUserId(), relatedChain);

            // aggiorno i profili se sono cambiati
            for (String relatedId : relNewProperties.keySet()) {

                // aggiorno il profilo del related
                Map<String, String> newProperties = relNewProperties.get(relatedId);

                newProperties.put(Constants.doc_classifica, newClassifica);
                newProperties.put(Constants.doc_cod_titolario, newCodTitolario);
                //setto il piano di classificazione se attivo
                if(pianoClass!=null && !"".equals(pianoClass)){
                    newProperties.put(Constants.doc_piano_class, pianoClass);
                }
                // aggiorno il profilo
                provider.updateProfileDocument(relatedId, newProperties);
            }

            // //advanced versions
            // setAdvancedVersionDocnumRecord(docId, oldStatoArchivistico,
            // newStatoArchivistico);

            // aggiorno il profilo del documento principale
            if (newStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
                metadatiClassificazione.put(Constants.doc_tipo_componente, EnumTipiComponente.PRINCIPALE.toString());
                metadatiClassificazione.put(Constants.doc_docnum_princ, VOID);
            }

            provider.updateProfileDocument(docId, metadatiClassificazione);

        } catch (DocerException docEx) {

            throw new DocerException(420, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(520, err.getMessage());
        }

    }

    // 21
    public void pubblicaDocumento(String ticket, String docId, Map<String, String> metadata) throws DocerException {

        try {

            metadata = toUCMap(metadata, "metadata");


            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            DataRow<String> oldProfileData = getOldProfile(OPERAZIONE.pubblicazione, docId);
            doOperationCheck(OPERAZIONE.pubblicazione, docId, loginUserInfo.getUserId(), oldProfileData, metadata);

            Map<String, String> metadatiPubblicazione = checkAndGetMetadatiPubblicazione(oldProfileData, metadata);

            String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
            String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);
            EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));
            EnumStatoArchivistico newStatoArchivistico = getEnumStatoArchivistico(metadatiPubblicazione.get(Constants.doc_stato_archivistico));

            EnumStatiPantarei newStatoPantarei = getEnumStatoPantarei(metadatiPubblicazione.get(Constants.doc_stato_pantarei));

            // modifico i related se ho cambiato stato
            // recupero lista dei related
            List<String> relatedChain = provider.getRelatedDocuments(docId);

            // rimuovo dalla lista il MASTER se presente
            relatedChain.remove(docId);

            // controllo stato_pantarei, TIPO_COMPONENTE e checkoutStatus
            // della related chain e ritorno le nuove proprieta' dei profili
            Map<String, Map<String, String>> relNewProperties = checkRecordRelatedChain(newStatoArchivistico, newStatoPantarei, oldCodEnte, oldCodAOO, docId, loginUserInfo.getUserId(), relatedChain);

            // aggiorno i profili se sono cambiati
            for (String relatedId : relNewProperties.keySet()) {

                // aggiorno il profilo del related
                Map<String, String> newProperties = relNewProperties.get(relatedId);

                // // aggiorno i metadati di pubblicazione
                // for(String key : metadatiPubblicazione.keySet()){
                //
                // // non sovrascrivo lo STATO_PANTAREI
                // if(key.equals(Constants.doc_stato_pantarei)){
                // continue;
                // }
                //
                // newProperties.put(key, metadatiPubblicazione.get(key));
                // }

                // il profilo lo aggiorno solo se e' stato specificato
                provider.updateProfileDocument(relatedId, newProperties);
            }

            // //advanced versions
            // setAdvancedVersionDocnumRecord(docId, oldStatoArchivistico,
            // newStatoArchivistico);

            // aggiorno il profilo del documento principale
            // TIPO_COMPONENTE lo metto solo per sicurezza nel caso sia
            // UNDEFINED

            // l'operazione di pubblicazione e' ammessa solo per RECORD quindi i
            // metadati tipo_componente e docnum_princ sono stati gia' assegnati
            // ma lo faccio tanto non costa nulla
            metadatiPubblicazione.put(Constants.doc_tipo_componente, EnumTipiComponente.PRINCIPALE.toString());
            metadatiPubblicazione.put(Constants.doc_docnum_princ, VOID);
            provider.updateProfileDocument(docId, metadatiPubblicazione);

            //controlla ed eventualmente aggiorna STATO_CONSERV
            updateStatoConserv(docId, null);

        } catch (DocerException docEx) {

            throw new DocerException(421, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(521, err.getMessage());
        }

    }

    // 22
    public void archiviaDocumento(String ticket, String docId, Map<String, String> metadata) throws DocerException {

	/*
     * if(!CONFIG_INITIALIZED){ throw new
	 * DocerException("Configurazione non inizializzata: "
	 * +CONFIG_ERROR_MESSAGE); } metadata = toUCMap(metadata);
	 */

        throw new DocerException("not implemented");

    }

    public void addNewAdvancedVersion(String ticket, String docIdLastVersion, String docIdNewVersion) throws DocerException {
        addOldAdvancedVersion(ticket, docIdLastVersion, docIdNewVersion, -99999.99999);
    }

    // 23
    public void addOldAdvancedVersion(String ticket, String docIdLastVersion, String docIdNewVersion, Double version) throws DocerException {

        try {

            if (docIdLastVersion == null || docIdLastVersion.equals(VOID))
                throw new DocerException("docIdLastVersion void o null");

            if (docIdNewVersion == null || docIdNewVersion.equals(VOID))
                throw new DocerException("docIdNewVersion void o null");

            if (docIdLastVersion.equals(docIdNewVersion)) {
                throw new DocerException("docIdLastVersion e docIdNewVersion coincidenti");
            }

            //controllo se già esiste la versione
            Map<Double,String> docVersions =  _getAdvancedVersions(ticket,docIdLastVersion);
            if (docVersions.containsKey(version)) {
                throw new DocerException("Impossibile aggiungere la nuova versione avanzata. La versione già esiste.");
            }


            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            List<String> lastAdvancedVersionChain = provider.getAdvancedVersions(docIdLastVersion);

            // il documento fa parte gia' delle versioni avanzate
            if (lastAdvancedVersionChain.contains(docIdLastVersion)) {
                return;
            }

            if (!lastAdvancedVersionChain.contains(docIdLastVersion)) {
                lastAdvancedVersionChain.add(docIdLastVersion);
            }

            List<String> newAdvancedVersionChain = provider.getAdvancedVersions(docIdNewVersion);

            if (!newAdvancedVersionChain.contains(docIdNewVersion)) {
                newAdvancedVersionChain.add(docIdNewVersion);
            }

            //FIX: per retrocompatibilità di chiamata
            if (lastAdvancedVersionChain.size() == 1 && newAdvancedVersionChain.size() > 1)
            {
                //swap per retrocompatibilità
                List<String> appoAdvancedVersionChain = newAdvancedVersionChain;
                newAdvancedVersionChain = lastAdvancedVersionChain;
                lastAdvancedVersionChain = appoAdvancedVersionChain;

                //swap dei docunumber
                String appoid = docIdLastVersion;
                docIdLastVersion = docIdNewVersion;
                docIdNewVersion = appoid;

            }



            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

            // impostazione dei criteri di ricerca
            List<String> criteria = new ArrayList<String>();

            criteria.add(docIdNewVersion);
            criteria.add(docIdLastVersion);

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_cod_ente);
            returnProperties.add(Constants.doc_cod_aoo);
            returnProperties.add(Constants.doc_docnum_record);
            returnProperties.add(Constants.doc_version);
            returnProperties.add(Constants.doc_docnum);

            searchCriteria.put(Constants.doc_docnum, criteria);

            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
            if (searchResults.getRows().size() != 2) {
                throw new DocerException("Documento non trovato");
            }

            if (!searchResults.getRow(0).get(Constants.doc_cod_ente).equals(
                    searchResults.getRow(1).get(Constants.doc_cod_ente))) {
                throw new DocerException("Impossibile aggiungere versione avanzata con enti differenti");
            }

            if (!searchResults.getRow(0).get(Constants.doc_cod_aoo).equals(
                    searchResults.getRow(1).get(Constants.doc_cod_aoo))) {
                throw new DocerException("Impossibile aggiungere versione avanzata con aoo differenti");
            }

            for (DataRow<String> dataRow : searchResults.getRows()) {
                String myDocnum = dataRow.get(Constants.doc_docnum);
                String myDocVersion = dataRow.get(Constants.doc_version);
                String myDocRecord = dataRow.get(Constants.doc_docnum_record);

                // se docIdLastVersion ha doc_docnum_record valorizzato
                // e non è stata specificata una versione -> errore
                if (myDocnum.equals(docIdLastVersion)) {
                    if (myDocRecord!=null && !StringUtils.isBlank(myDocRecord) && version == -99999.99999) {
                        throw new DocerException("Impossibile aggiungere la versione avanzata. Le versioni del documento contengono un record.");
                    }
                }

                //se docIdNewVersion ha versioni -> errore
                if (myDocnum.equals(docIdNewVersion)) {
                    String checkVersion = getDocumentLastVersion(ticket,docIdNewVersion);
                    if (!checkVersion.equals("")) {
                        if (Double.parseDouble(checkVersion) > 1.0) {
                            throw new DocerException("Impossibile aggiungere la versione avanzata. Il documento da aggiungere contiene già delle versioni.");
                        }
                    }
                }
            }


            searchCriteria.clear();
            criteria.clear();

            for (String id : lastAdvancedVersionChain) {
                if (criteria.contains(id)) {
                    continue;
                }
                criteria.add(id);
            }

            for (String id : newAdvancedVersionChain) {
                if (criteria.contains(id)) {
                    continue;
                }
                criteria.add(id);
            }

            searchCriteria.put(Constants.doc_docnum, criteria);

            returnProperties = new ArrayList<String>();
            returnProperties.add(Constants.doc_docnum);
            returnProperties.add(Constants.doc_cod_ente);
            returnProperties.add(Constants.doc_cod_aoo);
            returnProperties.add(Constants.doc_stato_archivistico);
            returnProperties.add(Constants.doc_tipo_componente);
            returnProperties.add(Constants.doc_docnum_record);
            returnProperties.add(Constants.doc_archive_type);
            returnProperties.add(Constants.doc_protocollo_anno);
            returnProperties.add(Constants.doc_protocollo_numero);
            returnProperties.add(Constants.doc_protocollo_registro);
            returnProperties.add(Constants.doc_registrazione_anno);
            returnProperties.add(Constants.doc_registrazione_numero);
            returnProperties.add(Constants.doc_registrazione_id_registro);
            returnProperties.add(Constants.doc_version);

            // RICERCA PRIMARIA per verificare i metadati
            searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            if (searchResults.getRows().size() == 0)
                throw new DocerException(criteria.toString() + ": nessun documento non trovato");

            // verifico profili advanced version
            // String lastDocnum = VOID;
            EnumTipiComponente lastTipoComponente = EnumTipiComponente.UNDEFINED;
            EnumStatoArchivistico lastStatoArchivistico = EnumStatoArchivistico.undefined;
            String lastArchiveType = VOID;
            String lastVersion = VOID;

            EnumTipiComponente newTipoComponente = EnumTipiComponente.UNDEFINED;
            EnumStatoArchivistico newStatoArchivistico = EnumStatoArchivistico.undefined;
            String newArchiveType = VOID;

            DataRow<String> drRecordOld = null;
            DataRow<String> drRecordNew = null;

            // leggo i metadati
            for (DataRow<String> dataRow : searchResults.getRows()) {
                String thisDocnum = dataRow.get(Constants.doc_docnum);
                String thisDocnumRecord = dataRow.get(Constants.doc_docnum_record);
                if (thisDocnumRecord == null) {
                    thisDocnumRecord = VOID;
                }

                criteria.remove(thisDocnum);

                EnumStatoArchivistico thisStatoArchivistico = getEnumStatoArchivistico(dataRow.get(Constants.doc_stato_archivistico));
                EnumTipiComponente thisTipoComponente = getEnumTipiComponente(dataRow.get(Constants.doc_tipo_componente));

                String thisArchiveType = dataRow.get(Constants.doc_archive_type);
                if (thisArchiveType == null || thisArchiveType.equals(VOID)) {
                    thisArchiveType = "ARCHIVE";
                }

                if (thisStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {

                    // lo considero solo se e' il Record principale (potrebbero
                    // esistere altri Record con stessi estremi di protocollo)
                    if (thisDocnumRecord.equals(VOID)) {
                        if (newAdvancedVersionChain.contains(thisDocnum)) {
                            drRecordNew = dataRow;
                        } else {
                            drRecordOld = dataRow;
                        }
                    }

                }

                if (thisDocnum.equals(docIdLastVersion)) {

                    lastTipoComponente = thisTipoComponente;
                    lastStatoArchivistico = thisStatoArchivistico;
                    lastArchiveType = thisArchiveType;

                    lastVersion = dataRow.get(Constants.doc_version);
//                    if (StringUtils.isBlank(lastVersion))
//                        lastVersion = paddingVersion(1.0);

                    continue;
                }

                if (thisDocnum.equals(docIdNewVersion)) {

                    // newDocnum = docnum;
                    newTipoComponente = thisTipoComponente;
                    newStatoArchivistico = thisStatoArchivistico;
                    newArchiveType = thisArchiveType;

                    continue;
                }

            }

            if (criteria.size() > 0) {
                throw new DocerException("documento/i " + Arrays.toString(criteria.toArray()) + " delle versioni avanzate non trovato");
            }

            if (!lastTipoComponente.equals(EnumTipiComponente.PRINCIPALE)) {
                throw new DocerException("il versioning avanzato e' ammesso solo per documenti con " + Constants.doc_tipo_componente + " PRINCIPALE: valore " + lastTipoComponente);
            }

            if (!newTipoComponente.equals(lastTipoComponente)) {

                throw new DocerException("non e' possibile modificare " + Constants.doc_tipo_componente + " nel versioning avanzato: " + lastTipoComponente + " del documento " + docIdLastVersion
                        + " diverso da " + newTipoComponente + " del documento " + docIdNewVersion);
            }

            if (!lastArchiveType.equals(newArchiveType)) {

                throw new DocerException("le versioni avanzate di un documento non possono avere " + Constants.doc_archive_type + " diverso: " + lastArchiveType + " del documento " + docIdLastVersion
                        + " diverso da " + newArchiveType + " del documento " + docIdNewVersion);
            }

            String docnumRecord = getAdvancedVersionsRecord(drRecordOld, drRecordNew);

            Map<String, String> metadata = new HashMap<String, String>();
            metadata.put(Constants.doc_docnum_record, docnumRecord);

            String newVersion = "";

            //se version=NULL => addNewAdvancedVersion - altrimenti => addOldAdvancedVersion
            if (version != -99999.99999) {
                newVersion = paddingVersion(version);
            } else {
                newVersion = getDocumentLastVersion(ticket,docIdLastVersion);
                //if (StringUtils.isNotBlank(lastVersion) && StringUtils.isNumeric(lastVersion)) {
                    newVersion = paddingVersion(Double.parseDouble(newVersion) + 1);
                    //lastVersion = Integer.toString(Integer.parseInt(lastVersion) + 1);
//                } else {
//                    lastVersion = paddingVersion(1.0);
//                }
            }



            // setto il docnum_record per la catena attuale
            for (String docId : lastAdvancedVersionChain) {
                if (docId.equals(docnumRecord)) {
                    continue;
                }

                if (StringUtils.isBlank(lastVersion)) {
                    lastVersion = paddingVersion(1.0);
                    metadata.put(Constants.doc_version, lastVersion);
                }

                provider.updateProfileDocument(docId, metadata);

            }

            metadata.put(Constants.doc_version, newVersion);
            // setto il docnum_record per l'altra catena
            for (String docId : newAdvancedVersionChain) {
                if (docId.equals(docnumRecord)) {
                    continue;
                }

                // nel caso in cui abbia gia' aggiornato il doc
                if (lastAdvancedVersionChain.contains(docId)) {
                    continue;
                }

                provider.updateProfileDocument(docId, metadata);
            }

            //rimuove la versione per sicurezza visto che c'e' un alto update sotto
            metadata.remove(Constants.doc_version);

            if (!docnumRecord.equals(VOID)) {
                metadata.put(Constants.doc_docnum_record, VOID);
                //se il doc è il record e lo sto aggiungendo come versione setta la versione sul doc
                if (docnumRecord.equals(docIdNewVersion)) {
                    metadata.put(Constants.doc_version, newVersion);
                }
                provider.updateProfileDocument(docnumRecord, metadata);
            }

            provider.addNewAdvancedVersion(docIdLastVersion, docIdNewVersion);

        } catch (DocerException docEx) {

            throw new DocerException(423, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(523, err.getMessage());
        }
    }

    private String getDocumentLastVersion(String ticket, String docId) throws DocerException {
        Map<Double,String> map = _getAdvancedVersions(ticket,docId);
        return Collections.max(map.keySet()).toString();
    }
//    private String getDocumentLastVersion(String ticket, String docId) throws DocerException {
//        try {
//
//            if (docId == null || docId.equals(VOID))
//                throw new DocerException("docId null");
//
//            LoggedUserInfo loginUserInfo = parseTicket(ticket);
//            entetoken = loginUserInfo.getCodiceEnte();
//
//            // setto l'utente nel provider
//            provider.setCurrentUser(loginUserInfo);
//
//            List<String> avs = provider.getAdvancedVersions(docId);
//
//            if (!avs.contains(docId)) {
//                avs.add(docId);
//            }
//
//            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
//            searchCriteria.put("DOCNUM",avs);
//
//            Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
//            orderby.put(Constants.doc_version,EnumSearchOrder.DESC);
//
//            List<String> returnProps = new ArrayList<String>();
//            returnProps.add("DOCNUM");
//            returnProps.add(Constants.doc_version);
//
//            DataTable<String> results = provider.searchDocuments(searchCriteria, null, returnProps, -1, orderby);
//
//            List<Double> outList = new ArrayList<Double>();
//            for (int i = 0; i < results.getRows().size(); i++) {
//                DataRow<String> dr = results.getRow(i);
//
//                //default
//                Double val = 1.0;
//                try {
//                    val = Double.parseDouble(dr.get(Constants.doc_version));
//                } catch (Exception ex) {
//                    //niente perché il default è già settato
//                }
//
//                outList.add(val);
//            }
//
//            Double maxValue = Collections.max(outList);
//
//            return String.valueOf(maxValue);
//
//        } catch (DocerException docEx) {
//
//            throw new DocerException(824, docEx.getMessage());
//
//        } catch (Exception err) {
//            throw new DocerException(924, err.getMessage());
//        }
//
//    }

    private String paddingVersion(Double version) {
        String versionStr = String.valueOf(version);
        return StringUtils.rightPad(versionStr,5,' ');
    }

    // 24
    public List<String> getAdvancedVersions(String ticket, String docId) throws DocerException {

        Map<Double,String> map = _getAdvancedVersions(ticket,docId);
        List<String> outList = new ArrayList<String>(map.values());

        return outList;
    }

    private Map<Double,String> _getAdvancedVersions(String ticket, String docId) throws DocerException {

        try {

            if (docId == null || docId.equals(VOID))
                throw new DocerException("docId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            List<String> avs = provider.getAdvancedVersions(docId);

            if (!avs.contains(docId)) {
                avs.add(docId);
            }

            //search dei docnum ordinati per metadato DOC_VERSION
//            List<String> docnumList = new ArrayList<String>();
//            for (String dnum : avs) {
//                docnumList.add(dnum);
//            }
            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put("DOCNUM",avs);

//            Map<String,EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
//            orderby.put(Constants.doc_version,EnumSearchOrder.ASC);

            List<String> returnProps = new ArrayList<String>();
            returnProps.add("DOCNUM");
            returnProps.add(Constants.doc_version);

            DataTable<String> results = provider.searchDocuments(searchCriteria, null, returnProps, -1, null);

            TreeMap<Double,String> outList = new TreeMap<Double,String>();
            for (int i = 0; i < results.getRows().size(); i++) {
                DataRow<String> dr = results.getRow(i);
                Double val = 1.0;
                try {
                    val = Double.parseDouble(dr.get(Constants.doc_version));
                } catch (Exception ex) {
                    //niente perché il default è già settato
                }
                outList.put(val, dr.get("DOCNUM"));
            }

            return outList;

        } catch (DocerException docEx) {

            throw new DocerException(424, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(524, err.getMessage());
        }
    }

    // 25
    public String createFolder(String ticket, Map<String, String> info) throws DocerException {

        try {

            info = toUCMap(info, "info");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            if (info.get(Constants.folder_folder_id) != null)
                throw new DocerException(Constants.folder_folder_id + " non e' specificabile dall'esterno");

            String codEnte = info.get(Constants.folder_cod_ente);
            String codAOO = info.get(Constants.folder_cod_aoo);
            String folderName = info.get(Constants.folder_folder_name);

            String parentFolderId = info.get(Constants.folder_parent_folder_id);
            if (parentFolderId == null) {
                parentFolderId = "";
            }
            String desFolder = info.get(Constants.folder_des_folder);

            String owner = info.get(Constants.folder_owner);
            if (owner == null) {
                owner = VOID;
            }

            boolean isPrivateFolder = StringUtils.isNotEmpty(owner);

            if (StringUtils.isEmpty(folderName))
                throw new DocerException("FOLDER_NAME obbligatorio");

            // ricerco il padre
            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

            searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(parentFolderId));

            boolean isFirstLevelFolder = false;

            if (StringUtils.isEmpty(parentFolderId)) {

                isFirstLevelFolder = true;

                if (codEnte != null) {
                    searchCriteria.put(it.kdm.docer.sdk.Constants.folder_cod_ente, Arrays.asList(codEnte));
                }

                if (codAOO != null) {
                    searchCriteria.put(it.kdm.docer.sdk.Constants.folder_cod_aoo, Arrays.asList(codAOO));
                }
            }

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_ente);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_aoo);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_des_folder);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_name);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);

//            if (CIFS) {
//                returnProperties.add(it.kdm.docer.sdk.Constants.path);
//            }

            DataTable<String> results = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            if (results == null || results.getRows() == null || results.getRows().size() == 0) {
                if (!Strings.isNullOrEmpty(parentFolderId))
                    throw new DocerException("Folder Parent non trovata: " + searchCriteria.toString());
            } else {

                if (results.getRows().size() > 1) {
                    throw new DocerException("Trovate " + results.getRows().size() + " Folder Parent con i seguenti criteri: " + searchCriteria.toString());
                }

                DataRow<String> drParentFolder = results.getRow(0);

                String parentOwner = drParentFolder.get(it.kdm.docer.sdk.Constants.folder_owner);
                if (parentOwner == null) {
                    parentOwner = VOID;
                }

                String parentCodEnte = drParentFolder.get(it.kdm.docer.sdk.Constants.folder_cod_ente);
                if (parentCodEnte == null) {
                    parentCodEnte = VOID;
                }
                String parentCodAOO = drParentFolder.get(it.kdm.docer.sdk.Constants.folder_cod_aoo);
                if (parentCodAOO == null) {
                    parentCodAOO = VOID;
                }

                if (StringUtils.isEmpty(codEnte)) {
                    codEnte = parentCodEnte;
                }

                if (StringUtils.isEmpty(codAOO)) {
                    codAOO = parentCodAOO;
                }

                if (!codEnte.equals(parentCodEnte)) {
                    throw new DocerException("non e' ammesso creare una Folder con COD_ENTE (" + codEnte + ") diverso dal COD_ENTE della Folder Parent  (" + parentCodEnte + ") ");
                }

                if (!codAOO.equals(parentCodAOO)) {
                    throw new DocerException("non e' ammesso creare una Folder con COD_AOO (" + codAOO + ") diverso dal COD_AOO della Folder Parent (" + parentCodAOO + ")");
                }
            }


            IFolderInfo folderProfile = new FolderInfo();
            folderProfile.setCodiceEnte(codEnte);
            folderProfile.setCodiceAOO(codAOO);
            folderProfile.setParentFolderId(parentFolderId);
            folderProfile.setDescrizione(desFolder);
            folderProfile.setFolderName(folderName);
            folderProfile.setFolderOwner(owner);

            if (info.containsKey(Constants.custom_enabled))
                folderProfile.setEnabled(getEnumBoolean(info.get(Constants.custom_enabled)));
            else
                folderProfile.setEnabled(EnumBoolean.TRUE);

            if (info.containsKey(Constants.inherits_acl)) {
                folderProfile.getExtraInfo().put(Constants.inherits_acl, info.get(Constants.inherits_acl));
            }


            /*
            if (CIFS) {

                // se folder pubblica di I livello inherits e' sempre true
                // se folder privata di I livello inherits e' sempre false
                if (isFirstLevelFolder) {
                    if (isPrivateFolder) {
                        folderProfile.getExtraInfo().put(Constants.inherits_acl, "false");
                    } else {
                        folderProfile.getExtraInfo().put(Constants.inherits_acl, "true");
                    }
                }

                if (info.containsKey(Constants.inherits_acl)) {
                    folderProfile.getExtraInfo().put(Constants.inherits_acl, info.get(Constants.inherits_acl));
                }
            }
            */

            return provider.createFolder(folderProfile);

        } catch (DocerException docEx) {

            throw new DocerException(425, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(525, err.getMessage());
        }
    }

    // 26
    public void updateFolder(String ticket, String folderId, Map<String, String> info) throws DocerException {

        try {

            info = toUCMap(info, "info");

            if (folderId == null || folderId.equals(VOID))
                throw new DocerException("folderId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            if (info.get(Constants.folder_folder_id) != null)
                throw new DocerException(Constants.folder_folder_id + " non e' specificabile dall'esterno");

            String newCodEnte = info.get(Constants.folder_cod_ente);
            String newCodAoo = info.get(Constants.folder_cod_aoo);
            String newFolderName = info.get(Constants.folder_folder_name);
            String newParentFolderId = info.get(Constants.folder_parent_folder_id);
            String newDesFolder = info.get(Constants.folder_des_folder);
            String newFolderOwner = info.get(Constants.folder_owner);

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.folder_folder_id, folderId);
            IFolderInfo folderInfo = getFolderProfile(idCrit);

            if (folderInfo == null)
                throw new DocerException("Folder " + folderId + " non trovata");

            if (newCodEnte != null && !newCodEnte.equalsIgnoreCase(folderInfo.getCodiceEnte())) {
                throw new DocerException("non e' permesso modificare COD_ENTE di una Folder");
            }
            if (newCodAoo != null && !newCodAoo.equalsIgnoreCase(folderInfo.getCodiceAOO())) {
                throw new DocerException("non e' permesso modificare COD_AOO di una Folder");
            }

            String oldParentFolderId = folderInfo.getParentFolderId();
            if (oldParentFolderId == null) {
                oldParentFolderId = VOID;
            }

//            if (newParentFolderId != null && newParentFolderId.equals(oldParentFolderId)) {
//                throw new DocerException("non e' permesso modificare " + Constants.folder_parent_folder_id + " di una Folder");
//            }

            String oldFolderOwner = folderInfo.getFolderOwner();
            if (oldFolderOwner == null) {
                oldFolderOwner = VOID;
            }

            if (newFolderOwner != null && !oldFolderOwner.equals(newFolderOwner)) {
                throw new DocerException("non e' permesso modificare " + Constants.folder_owner + " di una Folder");
            }

            IFolderInfo folderNewInfo = new FolderInfo();
            folderNewInfo.setDescrizione(newDesFolder);
            folderNewInfo.setFolderName(newFolderName);

            if (newParentFolderId != null && !newParentFolderId.equals(oldParentFolderId)) {
                folderNewInfo.setParentFolderId(newParentFolderId);
            }

            if (info.containsKey(Constants.custom_enabled)) {
                folderNewInfo.setEnabled(getEnumBoolean(info.get(Constants.custom_enabled)));
            }

            if (info.containsKey(Constants.inherits_acl)) {
                folderNewInfo.getExtraInfo().put(Constants.inherits_acl, info.get(Constants.inherits_acl));
            }

            provider.updateFolder(folderId, folderNewInfo);

        } catch (DocerException docEx) {

            throw new DocerException(426, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(526, err.getMessage());
        }

    }

    // 27
    public List<ISearchItem> searchFolders(String ticket, Map<String, List<String>> searchCriteria, int maxRows, Map<String, EnumSearchOrder> orderby) throws DocerException {

        searchCriteria = toUCMapOfList(searchCriteria);

        try {

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            if (maxRows < 0)
                maxRows = PRIMARYSEARCH_MAX_ROWS;

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_ente);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_aoo);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_des_folder);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_name);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
            returnProperties.add(Constants.custom_enabled);
//            if (CIFS) {
//                returnProperties.add(it.kdm.docer.sdk.Constants.path);
//            }

            DataTable<String> dtresults = provider.searchFolders(searchCriteria, returnProperties, maxRows, orderby);

            List<ISearchItem> results = new ArrayList<ISearchItem>();

            for (int i = 0; i < dtresults.getRows().size(); i++) {
                DataRow<String> dr = dtresults.getRow(i);

                List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
                ISearchItem searchItem = new SearchItem();

                for (String columnName : returnProperties) {

                    String val = dr.get(columnName);
                    if (val == null) {
                        val = VOID;
                    }

                    KeyValuePair kvp = new KeyValuePair(columnName, val);

                    kvpList.add(kvp);
                }

                searchItem.setMetadata(kvpList.toArray(new KeyValuePair[0]));

                results.add(searchItem);
            }

            return results;

        } catch (DocerException docEx) {

            throw new DocerException(427, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(527, err.getMessage());
        }
    }

    // 28
    public void deleteFolder(String ticket, String folderId) throws DocerException {

        try {

            if (folderId == null || folderId.equals(VOID))
                throw new DocerException("folderId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // controllo esistenza
            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);

            DataTable<String> results = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            if (results == null || results.getRows().size() == 0) {
                throw new DocerException("Folder " + folderId + " non trovata");
            }

            String parentFolderId = results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
            if (parentFolderId == null || parentFolderId.equals(VOID)) {
                throw new DocerException("non e' permesso eliminare la Folder root");
            }

            String folderOwner = results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_owner);
            if (folderOwner == null) {
                folderOwner = VOID;
            }

            if (!folderOwner.equals(VOID) && !folderOwner.equals(loginUserInfo.getUserId())) {
                throw new DocerException("non e' ammesso eliminare Folder private di un altro utente");
            }

            searchCriteria.clear();
            // impostazione dell'unico criterio di ricerca
            searchCriteria.put(Constants.folder_parent_folder_id, Arrays.asList(folderId));

            returnProperties.clear();
            returnProperties.add(Constants.doc_docnum);

            // RICERCA DOCUMENTI
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato
            if (searchResults.getRows().size() > 0)
                throw new DocerException("la Folder " + folderId + " contiene documenti e non e' ammesso eliminarla");

            returnProperties.clear();
            returnProperties.add(Constants.folder_folder_id);
            // RICERCA FOLDERS
            searchResults = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            // deve esserci un solo risultato
            if (searchResults.getRows().size() > 0)
                throw new DocerException("la Folder " + folderId + " contiene Folder e non e' ammesso eliminarla");

            // esecuzione della cancellazione
            provider.deleteFolder(folderId);

        } catch (DocerException docEx) {

            throw new DocerException(428, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(528, err.getMessage());
        }

    }

    // 29
    public void setACLFolder(String ticket, String folderId, Map<String, EnumACLRights> acls) throws DocerException {

        try {

            if (folderId == null)
                throw new DocerException("folderId null");

            if (acls == null)
                throw new DocerException("acls null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // controllo esistenza
            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);

            DataTable<String> results = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            if (results == null || results.getRows().size() == 0) {
                throw new DocerException("Folder " + folderId + " non trovata");
            }

            String parentFolderId = results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
//            if (parentFolderId == null || parentFolderId.equals(VOID)) {
//                throw new DocerException("non e' permesso modificare le ACL alla Folder root");
//            }

            boolean isPublic = false;
            String folderOwner = results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_owner);
            if (folderOwner == null) {
                folderOwner = VOID;
            }

            if (folderOwner.equals(VOID)) {
                isPublic = true;
            }

            provider.setACLFolder(folderId, acls);

        } catch (DocerException docEx) {

            throw new DocerException(429, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(529, err.getMessage());
        }

    }

    // 30
    public Map<String, EnumACLRights> getACLFolder(String ticket, String folderId) throws DocerException {

        try {

            if (folderId == null)
                throw new DocerException("folderId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // // controllo esistenza
            // Map<String, List<String>> searchCriteria = new HashMap<String,
            // List<String>>();
            // searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id,
            // Arrays.asList(folderId));
            //
            // List<String> returnProperties = new ArrayList<String>();
            // returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
            // returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
            // returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
            //
            // DataTable<String> results =
            // provider.searchFolders(searchCriteria, returnProperties,
            // PRIMARYSEARCH_MAX_ROWS, null);
            //
            // if(results==null || results.getRows().size()==0){
            // throw new DocerException("Folder " +folderId +" non trovata");
            // }

            // test della library
            return provider.getACLFolder(folderId);

        } catch (DocerException docEx) {

            throw new DocerException(430, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(530, err.getMessage());
        }

    }

    // 56
    public List<String> getFolderDocuments(String ticket, String folderId) throws DocerException {

        try {

            if (folderId == null || folderId.equals(VOID))
                throw new DocerException("folderId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // // controllo esistenza
            // Map<String, List<String>> searchCriteria = new HashMap<String,
            // List<String>>();
            // searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id,
            // Arrays.asList(folderId));
            //
            // List<String> returnProperties = new ArrayList<String>();
            // returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
            // returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
            // returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
            //
            // DataTable<String> results =
            // provider.searchFolders(searchCriteria, returnProperties,
            // PRIMARYSEARCH_MAX_ROWS, null);
            //
            // if(results==null || results.getRows().size()==0){
            // throw new DocerException("Folder " +folderId +" non trovata");
            // }

            return provider.getFolderDocuments(folderId);

        } catch (DocerException docEx) {

            throw new DocerException(431, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(531, err.getMessage());
        }
    }

    public void addToFolderDocuments(String ticket, String folderId, List<String> documents) throws DocerException {

        try {

            if (folderId == null || folderId.equals(VOID))
                throw new DocerException("folderId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // controllo esistenza
            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);

            DataTable<String> results = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            if (results == null || results.getRows().size() == 0) {
                throw new DocerException("Folder " + folderId + " non trovata");
            }

            String parentFolderId = results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
            if (parentFolderId == null || parentFolderId.equals(VOID)) {
                throw new DocerException("non e' permesso aggiungere documenti alla Folder root");
            }

            // String folderOwner =
            // results.getRow(0).get(it.kdm.docer.sdk.Constants.folder_owner);
            // if(folderOwner==null){
            // folderOwner = VOID;
            // }
            //
            // if(!folderOwner.equals(VOID) &&
            // !folderOwner.equals(loginUserInfo.extractUserProfile())){
            // throw new
            // DocerException("non e' ammesso aggiungere documenti in Folder private di un altro utente");
            // }

            if (documents == null || documents.size() == 0) {
                return;
            }

            searchCriteria.clear();
            // impostazione dell'unico criterio di ricerca
            searchCriteria.put(Constants.doc_docnum, documents);

            returnProperties.clear();
            returnProperties.add(Constants.doc_docnum);
            returnProperties.add(Constants.doc_stato_archivistico);
            returnProperties.add(Constants.doc_progr_fascicolo);

            // RICERCA DOCUMENTI
            DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            String docnumsFascicolati = VOID;
            for (int i = 0; i < searchResults.getRows().size(); i++) {
                DataRow<String> dr = searchResults.getRow(i);

                String docnum = dr.get(Constants.doc_docnum);
                EnumStatoArchivistico statoArchivistico = getEnumStatoArchivistico(dr.get(Constants.doc_stato_archivistico));
                String progrFascicolo = dr.get(Constants.doc_progr_fascicolo);

                // if(statoArchivistico.getCode()>EnumStatoArchivistico.classificato.getCode()){
                // docnumsFascicolati += docnum +", ";
                // }
                if (progrFascicolo != null && !progrFascicolo.equals(VOID)) {
                    docnumsFascicolati += docnum + ", ";
                }
            }

            docnumsFascicolati = docnumsFascicolati.replaceAll(", $", VOID);

            if (!docnumsFascicolati.equals(VOID)) {
                throw new DocerException("i documenti " + docnumsFascicolati + " possiedono un Fascicolo e non e' ammesso spostarli nelle Folder");
            }

            provider.addToFolderDocuments(folderId, documents);

        } catch (DocerException docEx) {

            throw new DocerException(431, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(531, err.getMessage());
        }
    }

    public void removeFromFolderDocuments(String ticket, String folderId, List<String> documents) throws DocerException {

        try {

            if (folderId == null || folderId.equals(VOID))
                throw new DocerException("folderId null");

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            // controllo esistenza
            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

            List<String> returnProperties = new ArrayList<String>();
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);

            DataTable<String> results = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

            if (results == null || results.getRows().size() == 0) {
                throw new DocerException("Folder " + folderId + " non trovata");
            }

            if (documents != null && documents.size() > 0) {
                provider.removeFromFolderDocuments(folderId, documents);
            }

        } catch (DocerException docEx) {

            throw new DocerException(431, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(531, err.getMessage());
        }
    }

    // ************ METODI PRIVATI *****************//
    // ************ METODI PRIVATI *****************//
    // ************ METODI PRIVATI *****************//
    // ************ METODI PRIVATI *****************//

    // private Map<String, Map<String, String>>
    // getRelatedProfilesToUpdatex(DataRow<String> masterProfile, List<String>
    // currentRelatedChain, List<String> toAddRelated) throws DocerException {
    //
    // Map<String, Map<String, String>> relatedProfileToUpdate = new
    // HashMap<String, Map<String, String>>();
    //
    // String masterDocid = masterProfile.get(Constants.doc_docnum);
    //
    // // controllo anche la catena attuale (recupero aggiornamenti parziali o
    // // andati male)
    // List<String> relatedToCheck = new ArrayList<String>();
    // for (String id : currentRelatedChain) {
    // if (id.equals(masterDocid)) {
    // continue;
    // }
    // if (relatedToCheck.contains(id)) {
    // continue;
    // }
    // relatedToCheck.add(id);
    // }
    // for (String id : toAddRelated) {
    // if (id.equals(masterDocid)) {
    // continue;
    // }
    // if (relatedToCheck.contains(id)) {
    // continue;
    // }
    // relatedToCheck.add(id);
    // }
    //
    // if (relatedToCheck.size() == 0) {
    // return relatedProfileToUpdate;
    // }
    //
    // // creo i criteri di ricerca
    // Map<String, List<String>> searchCriteria = new HashMap<String,
    // List<String>>();
    // searchCriteria.put(Constants.doc_docnum, relatedToCheck);
    //
    // // metadati che voglio ricercare
    // List<String> returnProperties = new ArrayList<String>();
    // returnProperties.add(Constants.doc_docnum);
    // returnProperties.add(Constants.doc_docnum_princ);
    // returnProperties.add(Constants.doc_cod_ente);
    // returnProperties.add(Constants.doc_cod_aoo);
    // returnProperties.add(Constants.doc_stato_archivistico);
    // returnProperties.add(Constants.doc_stato_pantarei);
    // returnProperties.add(Constants.doc_tipo_componente);
    // returnProperties.add(Constants.doc_classifica);
    // returnProperties.add(Constants.doc_progr_fascicolo);
    // returnProperties.add(Constants.doc_anno_fascicolo);
    // returnProperties.add(Constants.doc_fascicoli_secondari);
    // returnProperties.add(Constants.doc_num_fascicolo);
    // returnProperties.add(Constants.doc_cod_titolario);
    // returnProperties.add(Constants.doc_archive_type);
    //
    // // recupero dei profili di tutti i related (I livello -> relatedChain e
    // // toAdd)
    // DataTable<String> searchResults =
    // provider.searchDocuments(searchCriteria, null, returnProperties,
    // PRIMARYSEARCH_MAX_ROWS, null);
    //
    // // errore ricerca: nessun related e' presente sul DMS (!!!)
    // if (searchResults == null || searchResults.getRows().size() == 0)
    // throw new
    // DocerException("Errore recupero profili catena dei related: nessuno dei related "
    // + relatedToCheck.toString() + " e' stato trovato");
    //
    // List<String> allRelatedOfRelatedList = new ArrayList<String>();
    //
    // List<String> yetChecked = new ArrayList<String>();
    // yetChecked.add(masterDocid);
    //
    // // controllo i related (attuali e da aggiungere)
    // for (DataRow<String> relatedProfile : searchResults.getRows()) {
    //
    // String relDocnum = relatedProfile.get(Constants.doc_docnum);
    //
    // relatedToCheck.removeAll(Arrays.asList(relDocnum));
    //
    // // se l'ho gia' controllato
    // if (yetChecked.contains(relDocnum)) {
    // continue;
    // }
    //
    // // controllo del checkout
    // // checkedOutInfo =
    // // (LockStatus)provider.isCheckedOutDocument(relDocnum);
    // //
    // // if (checkedOutInfo.getLocked()) {
    // // if (originalRelatedChain.contains(relDocnum)) {
    // // throw new DocerException("il documento " + relDocnum +
    // // " della catena dei related e' in stato di blocco esclusivo");
    // // }
    // // throw new DocerException("il related da aggiungere " + relDocnum
    // // + " e' in stato di blocco esclusivo");
    // // }
    //
    // yetChecked.add(relDocnum);
    //
    // boolean belongsToOriginalRelatedChain =
    // currentRelatedChain.contains(relDocnum);
    //
    // // TODO...
    // // controllo il related
    // checkRelated(relatedProfile, belongsToOriginalRelatedChain,
    // masterProfile);
    //
    // checkIfNeedsProfileUpdate(relatedProfile, masterProfile,
    // relatedProfileToUpdate);
    //
    // if (belongsToOriginalRelatedChain) {
    // continue;
    // }
    //
    // // *** recupero i related del related da aggiungere ***
    // List<String> relOfRels = provider.getRelatedDocuments(relDocnum);
    // if (relOfRels == null)
    // throw new
    // DocerException("errore recupero related del documento related da aggiungere: "
    // + relDocnum);
    //
    // for (String elem : relOfRels) {
    // if (allRelatedOfRelatedList.contains(elem)) {
    // continue;
    // }
    // allRelatedOfRelatedList.add(elem);
    // }
    //
    // }
    //
    // if (relatedToCheck.size() > 0) {
    // throw new DocerException("alcuni related non sono stati trovati: " +
    // String.valueOf(relatedToCheck));
    // }
    //
    // if (allRelatedOfRelatedList.size() == 0) {
    // return relatedProfileToUpdate;
    // }
    //
    // relatedToCheck.clear();
    // relatedToCheck.addAll(allRelatedOfRelatedList);
    //
    // searchCriteria.clear();
    // searchCriteria.put(Constants.doc_docnum, allRelatedOfRelatedList);
    //
    // // recupero dei profili dei related dei related da aggiungere
    // searchResults = provider.searchDocuments(searchCriteria, null,
    // returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
    //
    // // errore ricerca: nessun related e' presente sul DMS (!!!)
    // if (searchResults == null || searchResults.getRows().size() == 0)
    // throw new
    // DocerException("Errore recupero profili related dei related: nessuno dei related dei related da aggiungere e' stato trovato");
    //
    // // controllo i related dei related da aggiungere
    // for (DataRow<String> relOfRelProfile : searchResults.getRows()) {
    //
    // String relOfRelDocnum = relOfRelProfile.get(Constants.doc_docnum);
    //
    // relatedToCheck.removeAll(Arrays.asList(relOfRelDocnum));
    //
    // // se fa parte della chain originale e' yet checked
    // if (yetChecked.contains(relOfRelDocnum)) {
    // continue;
    // }
    //
    // // controllo del checkout
    // // checkedOutInfo =
    // // (LockStatus)provider.isCheckedOutDocument(relOfRelDocnum);
    // //
    // // if (checkedOutInfo.getLocked()) {
    // // throw new DocerException("il related " + relOfRelDocnum +
    // // " di un related da aggiungere e' in stato di blocco esclusivo");
    // // }
    //
    // yetChecked.add(relOfRelDocnum);
    //
    // boolean belongsToOriginalRelatedChain =
    // originalRelatedChain.contains(relOfRelDocnum);
    //
    // // controllo il related da aggiungere
    // checkRelated(relOfRelProfile, belongsToOriginalRelatedChain,
    // masterProfile);
    //
    // checkIfNeedsProfileUpdate(relOfRelProfile, masterProfile,
    // relatedProfileToUpdate);
    //
    // // *** non serve recuperare i related del related perche' e
    // // ridondante***
    // }
    //
    // return relatedProfileToUpdate;
    // }

    private Map<String, String> checkIfRelatedNeedsProfileUpdate(DataRow<String> relatedProfile, DataRow<String> masterProfile) {

        String relDocnum = String.valueOf(relatedProfile.get(Constants.doc_docnum));
        String relCodEnte = String.valueOf(relatedProfile.get(Constants.doc_cod_ente));
        String relCodAOO = String.valueOf(relatedProfile.get(Constants.doc_cod_aoo));
        EnumStatoArchivistico relStatoArchivistico = getEnumStatoArchivistico(relatedProfile.get(Constants.doc_stato_archivistico));
        EnumTipiComponente relTipoComponente = getEnumTipiComponente(relatedProfile.get(Constants.doc_tipo_componente));
        String relClassifica = String.valueOf(relatedProfile.get(Constants.doc_classifica));
        String relProgrFascicolo = String.valueOf(relatedProfile.get(Constants.doc_progr_fascicolo));
        String relAnnoFascicolo = String.valueOf(relatedProfile.get(Constants.doc_anno_fascicolo));
        String relFascSecondari = String.valueOf(relatedProfile.get(Constants.doc_fascicoli_secondari));
        String relNumFascicolo = String.valueOf(relatedProfile.get(Constants.doc_num_fascicolo));
        String relCodTitolario = String.valueOf(relatedProfile.get(Constants.doc_cod_titolario));
        String relArchiveType = String.valueOf(relatedProfile.get(Constants.doc_archive_type));
        EnumStatiPantarei relStatoPantarei = getEnumStatoPantarei(relatedProfile.get(Constants.doc_stato_pantarei));
        String relDocnumPrinc = String.valueOf(relatedProfile.get(Constants.doc_docnum_princ));

        String masterDocnum = String.valueOf(masterProfile.get(Constants.doc_docnum));
        String masterCodEnte = String.valueOf(masterProfile.get(Constants.doc_cod_ente));
        String masterCodAOO = String.valueOf(masterProfile.get(Constants.doc_cod_aoo));
        EnumStatoArchivistico masterStatoArchivistico = getEnumStatoArchivistico(masterProfile.get(Constants.doc_stato_archivistico));
        EnumTipiComponente masterTipoComponente = getEnumTipiComponente(masterProfile.get(Constants.doc_tipo_componente));
        String masterClassifica = String.valueOf(masterProfile.get(Constants.doc_classifica));
        String masterProgrFascicolo = String.valueOf(masterProfile.get(Constants.doc_progr_fascicolo));
        String masterAnnoFascicolo = String.valueOf(masterProfile.get(Constants.doc_anno_fascicolo));
        String masterFascSecondari = String.valueOf(masterProfile.get(Constants.doc_fascicoli_secondari));
        String masterNumFascicolo = String.valueOf(masterProfile.get(Constants.doc_num_fascicolo));
        String masterCodTitolario = String.valueOf(masterProfile.get(Constants.doc_cod_titolario));
        String masterPianoClass = String.valueOf(masterProfile.get(Constants.doc_piano_class)); //update 2020-02-10
        String masterArchiveType = String.valueOf(masterProfile.get(Constants.doc_archive_type));
        EnumStatiPantarei masterStatoPantarei = getEnumStatoPantarei(masterProfile.get(Constants.doc_stato_pantarei));

        boolean needsUpdate = false;

        if (!masterClassifica.equals(relClassifica)) {
            needsUpdate = true;
        }
        if (!masterCodTitolario.equals(relCodTitolario)) {
            needsUpdate = true;
        }
        if (!masterProgrFascicolo.equals(relProgrFascicolo)) {
            needsUpdate = true;
        }
        if (!masterNumFascicolo.equals(relNumFascicolo)) {
            needsUpdate = true;
        }
        if (!masterAnnoFascicolo.equals(relAnnoFascicolo)) {
            needsUpdate = true;
        }
        if (!masterFascSecondari.equals(relFascSecondari)) {
            needsUpdate = true;
        }

        if (masterTipoComponente.equals(EnumTipiComponente.PRINCIPALE) && masterStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {

            if (!masterStatoArchivistico.equals(relStatoArchivistico)) {
                needsUpdate = true;
            }
            if (!relDocnumPrinc.equals(masterDocnum)) {
                needsUpdate = true;
            }
            if (relTipoComponente.equals(EnumTipiComponente.UNDEFINED)) {
                needsUpdate = true;
            }
        }

        if (masterStatoPantarei.getCode() > 3) {
            if (!relStatoPantarei.equals(EnumStatiPantarei.allegato)) {
                needsUpdate = true;
            }
        }

        if (needsUpdate) {

            Map<String, String> relNewProperties = new HashMap<String, String>();

            relNewProperties.put(Constants.doc_cod_ente, masterCodEnte);
            relNewProperties.put(Constants.doc_cod_aoo, masterCodAOO);
            relNewProperties.put(Constants.doc_classifica, masterClassifica);
            relNewProperties.put(Constants.doc_progr_fascicolo, masterProgrFascicolo);
            relNewProperties.put(Constants.doc_anno_fascicolo, masterAnnoFascicolo);
            relNewProperties.put(Constants.doc_fascicoli_secondari, masterFascSecondari);
            relNewProperties.put(Constants.doc_cod_titolario, masterCodTitolario);

            if (masterPianoClass != null && !"".equals(masterPianoClass))
                relNewProperties.put(Constants.doc_piano_class, masterPianoClass); //update 2020-02-10


            relNewProperties.put(Constants.doc_num_fascicolo, masterNumFascicolo);


            if (masterTipoComponente.equals(EnumTipiComponente.PRINCIPALE) && masterStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
                relNewProperties.put(Constants.doc_stato_archivistico, String.valueOf(masterStatoArchivistico.getCode()));

                relNewProperties.put(Constants.doc_docnum_princ, masterDocnum);

                if (relTipoComponente.equals(EnumTipiComponente.UNDEFINED)) {
                    relNewProperties.put(Constants.doc_tipo_componente, EnumTipiComponente.ALLEGATO.toString());
                }
            }

            if (masterStatoPantarei.getCode() > 3) {
                relNewProperties.put(Constants.doc_stato_pantarei, Integer.toString(EnumStatiPantarei.allegato.getCode()));
            }

            return relNewProperties;
        }

        return null;

    }

    private void checkRelated(DataRow<String> relatedProfile, boolean belongsToOriginalRelatedChain, DataRow<String> masterProfile) throws DocerException {

        String masterDocid = masterProfile.get(Constants.doc_docnum);
        String masterTypeid = masterProfile.get(Constants.doc_type_id);
        String masterCodEnte = masterProfile.get(Constants.doc_cod_ente);
        String masterCodAOO = masterProfile.get(Constants.doc_cod_aoo);
        EnumStatoArchivistico masterStatoArchivistico = getEnumStatoArchivistico(masterProfile.get(Constants.doc_stato_archivistico));
        String masterArchiveType = masterProfile.get(Constants.doc_archive_type);

        String relatedId = relatedProfile.get(Constants.doc_docnum);
        EnumStatoArchivistico relStatoArchivistico = getEnumStatoArchivistico(relatedProfile.get(Constants.doc_stato_archivistico));
        EnumStatiPantarei relStatoPantarei = getEnumStatoPantarei(relatedProfile.get(Constants.doc_stato_pantarei));
        String relCodEnte = relatedProfile.get(Constants.doc_cod_ente);
        String relCodAOO = relatedProfile.get(Constants.doc_cod_aoo);
        String relDocnumPrinc = relatedProfile.get(Constants.doc_docnum_princ);
        if (relCodEnte == null) {
            relCodEnte = VOID;
        }
        if (relCodAOO == null) {
            relCodAOO = VOID;
        }
        if (relDocnumPrinc == null) {
            relDocnumPrinc = VOID;
        }

        EnumTipiComponente relTipoComponente = getEnumTipiComponente(relatedProfile.get(Constants.doc_tipo_componente));

        // controllo stato archivistico del related solo se non fa parte gia'
        // della catena dei related (add related parzialmente riuscito)
        if (relStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
            if (!belongsToOriginalRelatedChain) {
                throw new DocerException("il related da aggiungere " + relatedId + " ha STATO_ARCHIVISTICO " + relStatoArchivistico + " (" + relStatoArchivistico.getCode() + ")");
            }
        }

        // controlliamo DOCNUM_PRINC e che non faccia riferimento ad un
        // altro documento principale
        if (!relDocnumPrinc.equals(VOID) && !relDocnumPrinc.equals(masterDocid)) {
            if (!belongsToOriginalRelatedChain) {
                throw new DocerException("il related da aggiungere " + relatedId + " fa riferimento ad un altro documento principale: DOCNUM_PRINC=" + relDocnumPrinc);
            }
        }
        //STEFANO: utilizzare la flag come lista lista dei TYPE_ID (del principale)
        //se flag=* è valida per tutte le tipologie
        //altrimenti è false
        boolean allow_record_add_allegato = false;

        List<String> allow_record_type_id = BL_CONFIGURATIONS.getConfig(entetoken).isAllow_record_add_allegato();
        if (allow_record_type_id.contains("*") || allow_record_type_id.contains(masterTypeid))
            allow_record_add_allegato = true;


        // dopo essere diventato un record possiamo solo aggiungere annotazioni
        // e annessi
        if ((masterStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) && !relTipoComponente.equals(EnumTipiComponente.ANNESSO)
                && !relTipoComponente.equals(EnumTipiComponente.ANNOTAZIONE)) {


            // modifica del 14/12/2012
            if (!belongsToOriginalRelatedChain && !allow_record_add_allegato) {

                if (!masterArchiveType.equals("PAPER")) {
                    throw new DocerException("il documento PRINCIPALE " + masterDocid + " ha STATO_ARCHIVISTICO " + masterStatoArchivistico + " e ARCHIVE_TYPE=" + masterArchiveType
                            + " ed ammette solo l'aggiunta di related con " + Constants.doc_tipo_componente + " ANNESSO o ANNOTAZIONE: il related da aggiungere " + relatedId + " ha "
                            + Constants.doc_tipo_componente + " " + relTipoComponente);
                }
            }
        }

        // controlliamo cod_ente e cod_aoo
        if (relCodEnte.equals(VOID)) {
            throw new DocerException("il documento " + relatedId + " non ha COD_ENTE assegnato");
        }
        // controlliamo cod_ente e cod_aoo
        if (relCodAOO.equals(VOID)) {
            throw new DocerException("il documento " + relatedId + " non ha COD_AOO assegnato");
        }

        if (!relCodEnte.equals(masterCodEnte)) {
            throw new DocerException("il related " + relatedId + " ha COD_ENTE (" + relCodEnte + ") diverso da quello del documento " + masterDocid + ": " + masterCodEnte);
        }

        if (!relCodAOO.equals(masterCodAOO)) {
            throw new DocerException("il related " + relatedId + " ha COD_AOO (" + relCodAOO + ") diverso da quello del documento " + masterDocid + ": " + masterCodAOO);
        }

        // // controllo stato pantarei del related
        // if (relStatoPantarei.getCode() > 3) {
        //
        // if (!belongsToOriginalRelatedChain) {
        // throw new DocerException("compatibilita' DOCAREA: il documento " +
        // relatedId + " ha STATO_PANTAREI: " + relStatoPantarei + " (" +
        // relStatoPantarei.getCode() + ")");
        // }
        //
        // // c'e' un related che e' un master per DOCAREA (non dovrebbe mai
        // // accadere ma lo controllo)
        // if (relStatoPantarei.getCode() != 6) {
        // throw new DocerException("compatibilita' DOCAREA: il documento " +
        // relatedId + " ha STATO_PANTAREI: " + relStatoPantarei + " (" +
        // relStatoPantarei.getCode() + ")");
        // }
        // }
    }

    public static EnumStatiConservazione getEnumStatiConservazione(String statoConservazione) {

        try {
            EnumStatiConservazione value = EnumStatiConservazione.getCode(Integer.parseInt(statoConservazione));

            if (value == null) {
                value = EnumStatiConservazione.da_non_conservare;
            }
            return value;
        } catch (Exception e) {
            return EnumStatiConservazione.da_non_conservare;
        }
    }

    public static EnumTipiComponente getEnumTipiComponente(String tipoComponente) {

        if (tipoComponente == null)
            return EnumTipiComponente.UNDEFINED;

        if (tipoComponente.equalsIgnoreCase("PRINCIPALE"))
            return EnumTipiComponente.PRINCIPALE;

        if (tipoComponente.equalsIgnoreCase("ALLEGATO"))
            return EnumTipiComponente.ALLEGATO;

        if (tipoComponente.equalsIgnoreCase("ANNOTAZIONE"))
            return EnumTipiComponente.ANNOTAZIONE;

        if (tipoComponente.equalsIgnoreCase("ANNESSO"))
            return EnumTipiComponente.ANNESSO;

        return EnumTipiComponente.UNDEFINED;
    }

    public static EnumStatiPantarei getEnumStatoPantarei(String statoPantarei) {

        try {
            EnumStatiPantarei value = EnumStatiPantarei.getCode(Integer.parseInt(statoPantarei));

            if (value == null) {
                value = EnumStatiPantarei.undefined;
            }
            return value;
        } catch (Exception e) {
            return EnumStatiPantarei.undefined;
        }

    }

    public static EnumStatoArchivistico getEnumStatoArchivistico(String statoArchivistico) {

        try {
            EnumStatoArchivistico value = EnumStatoArchivistico.getCode(Integer.parseInt(statoArchivistico));

            if (value == null) {
                value = EnumStatoArchivistico.undefined;
            }
            return value;
        } catch (Exception e) {
            return EnumStatoArchivistico.undefined;
        }

    }

    public static EnumBoolean getEnumBoolean(String value) {

        if (value == null)
            return EnumBoolean.UNSPECIFIED;

        if (value.equals("1") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("YES"))
            return EnumBoolean.TRUE;

        if (value.equals("0") || value.equalsIgnoreCase("FALSE") || value.equalsIgnoreCase("N") || value.equalsIgnoreCase("NO"))
            return EnumBoolean.FALSE;

        return EnumBoolean.UNSPECIFIED;
    }

    public static EnumACLRights getEnumACLRights(String right) {

        try {
            EnumACLRights value = EnumACLRights.getCode(Integer.parseInt(right));

            if (value == null) {
                value = EnumACLRights.undefined;
            }
            return value;
        } catch (Exception e) {
            return EnumACLRights.undefined;
        }

    }

    private void checkEnte(String codEnte) throws DocerException {

        if (codEnte == null)
            throw new DocerException("Ente " + codEnte + " non trovato");

        Map<String, String> idCrit = new HashMap<String, String>();
        idCrit.put(Constants.aoo_cod_ente, codEnte);

        Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCrit);
        if (p == null)
            throw new DocerException("Ente " + codEnte + " non trovato");

        if (p.get(Constants.ente_enabled).equalsIgnoreCase(EnumBoolean.FALSE.toString()))
            throw new DocerException("Ente " + codEnte + " disabilitato");

    }

    private void checkAOO(String codAoo, String codEnte) throws DocerException {

        if (codAoo == null)
            throw new DocerException("AOO " + codAoo + " non trovata. Ente: " + codEnte);

        Map<String, String> idCrit = new HashMap<String, String>();
        idCrit.put(Constants.aoo_cod_ente, codEnte);
        idCrit.put(Constants.aoo_cod_aoo, codAoo);

        Map<String, String> p = getAnagraficaProfile(Constants.aoo_type_id, idCrit);
        if (p == null)
            throw new DocerException("AOO " + codAoo + " non trovata. Ente: " + codEnte);

        if (p.get(Constants.aoo_enabled).equalsIgnoreCase(EnumBoolean.FALSE.toString()))
            throw new DocerException("AOO " + codAoo + " disabilitata. Ente: " + codEnte);

    }

    private EnumStatoArchivistico checkStatoArchivisticoClassificaDocumento(EnumStatoArchivistico oldStatoArchivistico) throws DocerException {

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico)) {
            return EnumStatoArchivistico.generico;
        }

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico_definitivo)) {
            return EnumStatoArchivistico.generico_definitivo;
        }

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato) || oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato)
                || oldStatoArchivistico.equals(EnumStatoArchivistico.classificato)) {
            return EnumStatoArchivistico.classificato;
        }

        throw new DocerException("non e' permessa l'invocazione del metodo classificaDocumento per un documento con STATO_ARCHIVISTICO " + oldStatoArchivistico + " (" + oldStatoArchivistico.getCode()
                + ")");

    }

    private Map<String, String> checkPropertiesCreateDocument(Map<String, String> metadata) throws DocerException {

        String cod_ente = metadata.get(Constants.doc_cod_ente);

        boolean migration_mode = BL_CONFIGURATIONS.getConfig(entetoken).isMIGRATION_MODE();

        Map<String, String> cleanMetadata = new HashMap<String, String>();

        cleanMetadata.putAll(metadata);

        for (String propName : metadata.keySet()) {

            String propValue = metadata.get(propName);

            // if (propValue == null)
            // continue;

            if (propName.equalsIgnoreCase(Constants.doc_stato_pantarei)) {

                EnumStatiPantarei statoPantarei = getEnumStatoPantarei(propValue);

                if (!statoPantarei.equals(EnumStatiPantarei.generico) && !statoPantarei.equals(EnumStatiPantarei.daProtocollare) && !statoPantarei.equals(EnumStatiPantarei.daFascicolare)) {
                    throw new DocerException("compatibilita' DOCAREA: non e' permesso creare un documento con " + Constants.doc_stato_pantarei + " " + statoPantarei + " (" + statoPantarei.getCode()
                            + ")");
                }

                continue;
            }

            if (propName.equalsIgnoreCase(Constants.doc_stato_archivistico)) {

                EnumStatoArchivistico statoArchivistico = getEnumStatoArchivistico(propValue);

                if (statoArchivistico.equals(EnumStatoArchivistico.generico) || statoArchivistico.equals(EnumStatoArchivistico.generico_definitivo)) {
                    continue;
                }

                throw new DocerException("il metadato " + propName + " e' assegnabile dall'esterno solo per i valori " + EnumStatoArchivistico.generico + " ("
                        + EnumStatoArchivistico.generico.getCode() + ") e " + EnumStatoArchivistico.generico_definitivo + " (" + EnumStatoArchivistico.generico_definitivo.getCode() + ")");
            }
            // metadati interni
            if (propName.equalsIgnoreCase(Constants.doc_docnum_princ)
                    // || propName.equalsIgnoreCase(Constants.doc_app_id)
                    || propName.equalsIgnoreCase(Constants.doc_cod_titolario) || propName.equalsIgnoreCase(Constants.doc_num_fascicolo) || propName.equalsIgnoreCase(Constants.doc_docnum_record)) {
                throw new DocerException("il metadato " + propName + " non e' assegnabile dall'esterno");
            }

            if (propName.equalsIgnoreCase(Constants.doc_docnum)) {
                if (!migration_mode)
                    throw new DocerException("il metadato  " + Constants.doc_docnum + " puo' essere assegnato dall'esterno solo se MIGRATION_MODE=true");

                if (propValue == null) {
                    throw new DocerException("MIGRATION MODE: il metadato " + Constants.doc_docnum + " non puo' essere impostato a null");
                }

                continue;
            }

            if (propName.equalsIgnoreCase(Constants.doc_classifica)) {

                if (StringUtils.isEmpty(propValue)) {
                    cleanMetadata.remove(propName);
                    continue;
                }
                throw new DocerException("il metodo createDocument non permette di valorizzare il campo " + propName.toUpperCase()
                        + " di un documento. Per la classificazione utilizzare il metodo classificaDocumento");
            }

            if (propName.equalsIgnoreCase(Constants.doc_protocollo_registro) || propName.equalsIgnoreCase(Constants.doc_protocollo_oggetto) || propName.equalsIgnoreCase(Constants.doc_protocollo_data)
                    || propName.equalsIgnoreCase(Constants.doc_protocollo_numero) || propName.equalsIgnoreCase(Constants.doc_protocollo_anno)
                    || propName.equalsIgnoreCase(Constants.doc_protocollo_annullato_protocollo) || propName.equalsIgnoreCase(Constants.doc_protocollo_data_annullamento_protocollo)
                    || propName.equalsIgnoreCase(Constants.doc_protocollo_motivo_annullamento_protocollo) || propName.equalsIgnoreCase(Constants.doc_protocollo_provvedimento_annullamento_protocollo)) {

                if (StringUtils.isEmpty(propValue)) {
                    cleanMetadata.remove(propName);
                    continue;
                }

                throw new DocerException("il metodo createDocument non permette di valorizzare il campo " + propName.toUpperCase()
                        + " di un documento. Per la protocollazione utilizzare il metodo protocollaDocumento");
            }

            if (propName.equalsIgnoreCase(Constants.doc_pubblicazione_pubblicato)
                    || propName.equalsIgnoreCase(Constants.doc_pubblicazione_annullata) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_annullamento)
                    || propName.equalsIgnoreCase(Constants.doc_pubblicazione_motivo_annullamento)) {

                if (StringUtils.isEmpty(propValue)) {
                    cleanMetadata.remove(propName);
                    continue;
                }

                throw new DocerException("il metodo createDocument non permette di valorizzare il campo " + propName.toUpperCase()
                        + " di un documento. Per la pubblicazione utilizzare il metodo protocollaDocumento");
            }

            if (propName.equalsIgnoreCase(Constants.doc_progr_fascicolo) || propName.equalsIgnoreCase(Constants.doc_anno_fascicolo) || propName.equalsIgnoreCase(Constants.doc_fascicoli_secondari)) {

                if (StringUtils.isEmpty(propValue)) {
                    cleanMetadata.remove(propName);
                    continue;
                }

                throw new DocerException("il metodo createDocument non permette di valorizzare il campo " + propName.toUpperCase()
                        + " di un documento. Per la fascicolazione utilizzare il metodo fascicolaDocumento");
            }

            // metadati di registrazione
            if (propName.equalsIgnoreCase(Constants.doc_registrazione_anno) || propName.equalsIgnoreCase(Constants.doc_registrazione_data)
                    || propName.equalsIgnoreCase(Constants.doc_registrazione_id_registro) || propName.equalsIgnoreCase(Constants.doc_registrazione_numero)
                    || propName.equalsIgnoreCase(Constants.doc_registrazione_oggetto) || propName.equalsIgnoreCase(Constants.doc_registrazione_annullata_registrazione)
                    || propName.equalsIgnoreCase(Constants.doc_registrazione_data_annullamento_registrazione)
                    || propName.equalsIgnoreCase(Constants.doc_registrazione_motivo_annullamento_registrazione)
                    || propName.equalsIgnoreCase(Constants.doc_registrazione_provvedimento_annullamento_registrazione)) {

                if (StringUtils.isEmpty(propValue)) {
                    cleanMetadata.remove(propName);
                    continue;
                }

                throw new DocerException("il metodo createDocument non permette di valorizzare il campo " + propName.toUpperCase()
                        + " di un documento. Per la registrazione utilizzare il metodo registraDocumento");
            }

            // metadati di pubblicazione
            if (propName.equalsIgnoreCase(Constants.doc_pubblicazione_registro) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_anno)
                    || propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_fine) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_inizio)
                    || propName.equalsIgnoreCase(Constants.doc_pubblicazione_numero) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_oggetto)
                    || propName.equalsIgnoreCase(Constants.doc_pubblicazione_pubblicato)) {

                if (StringUtils.isEmpty(propValue)) {
                    cleanMetadata.remove(propName);
                    continue;
                }

                throw new DocerException("il metodo createDocument non permette di valorizzare il campo " + propName.toUpperCase()
                        + " di un documento. Per la pubblicazione utilizzare il metodo pubblicaDocumento");
            }

        }

        return cleanMetadata;

    }

    private Map<String, String> checkPropertiesUpdateProfile(DataRow<String> oldProfileData, Map<String, String> newMetadata) throws DocerException {

        Map<String, String> cleanMetadata = new HashMap<String, String>();
        cleanMetadata.putAll(newMetadata);

        EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));
        boolean oldPubblicato = Boolean.parseBoolean(oldProfileData.get(Constants.doc_pubblicazione_pubblicato));

        String oldArchiveType = String.valueOf(oldProfileData.get(Constants.doc_archive_type));

        String docId = oldProfileData.get(Constants.doc_docnum);

        for (String propName : newMetadata.keySet()) {

            String newValue = newMetadata.get(propName);
            String oldValue = oldProfileData.get(propName);

            if (newValue == null) {
                newValue = "";
            }
            if (oldValue == null) {
                oldValue = "";
            }

            if (propName.equalsIgnoreCase(Constants.doc_stato_pantarei)) {

                EnumStatiPantarei oldStatoPantarei = getEnumStatoPantarei(oldValue);
                EnumStatiPantarei newStatoPantarei = getEnumStatoPantarei(newValue);

                if (!oldStatoPantarei.equals(newStatoPantarei)) {

                    if (newStatoPantarei.equals(EnumStatiPantarei.undefined) || newStatoPantarei.equals(EnumStatiPantarei.protocollato) || newStatoPantarei.equals(EnumStatiPantarei.fascicolato)
                            || newStatoPantarei.equals(EnumStatiPantarei.allegato)) {
                        throw new DocerException("compatibilita' DOCAREA: non e' permesso modificare STATO_PANTAREI dal valore " + oldStatoPantarei + " (" + oldStatoPantarei.getCode() + ")"
                                + " al valore " + newStatoPantarei + " (" + newStatoPantarei.getCode() + ")");
                    }

                    if (oldStatoPantarei.equals(EnumStatiPantarei.protocollato) || oldStatoPantarei.equals(EnumStatiPantarei.fascicolato) || oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {
                        throw new DocerException("compatibilita' DOCAREA: non e' permesso modificare STATO_PANTAREI dal valore " + oldStatoPantarei + " (" + oldStatoPantarei.getCode() + ")"
                                + " al valore " + newStatoPantarei + " (" + newStatoPantarei.getCode() + ")");
                    }

                }

                continue;
            }

            if (propName.equals(Constants.doc_tipo_componente)) {

                EnumTipiComponente oldTipoComponente = getEnumTipiComponente(oldValue);
                EnumTipiComponente newTipoComponente = getEnumTipiComponente(newValue);

                if (!newTipoComponente.equals(oldTipoComponente)) {

                    if (oldStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
                        throw new DocerException("documento " + docId + ": non e' permesso modificare il campo " + Constants.doc_tipo_componente + " di un documento con STATO_ARCHIVISTICO "
                                + oldStatoArchivistico + " (" + oldStatoArchivistico.getCode() + ")");
                    }

                    if (oldPubblicato) {
                        throw new DocerException("documento " + docId + ": non e' permesso modificare il campo " + Constants.doc_tipo_componente + " di un documento PUBBLICATO");
                    }
                }

                // modifico TIPO_COMPONENTE assegnando PRINCIPALE
                if (!oldTipoComponente.equals(newTipoComponente) && newTipoComponente.equals(EnumTipiComponente.PRINCIPALE)) {

                    List<String> relatedChain = provider.getRelatedDocuments(docId);

                    if (relatedChain == null || relatedChain.size() == 0) {
                        continue; // non ho related
                    }

                    // controllo i metadati dei related da aggiungere
                    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

                    List<String> criteria = new ArrayList<String>();

                    for (String relId : relatedChain) {

                        if (relId.equals(docId)) {
                            continue;
                        }

                        if (!criteria.contains(relId))
                            criteria.add(relId);
                    }

                    if (criteria.size() < 1) {
                        continue;
                    }

                    // creo i criteri di ricerca (i related che hanno
                    // TIPO_COMPONENTE=PRINCIPALE
                    searchCriteria.put(Constants.doc_docnum, criteria);
                    searchCriteria.put(Constants.doc_tipo_componente, Arrays.asList("PRINCIPALE"));

                    // le proprieta' di ritorno
                    // metadati che voglio ricercare
                    List<String> returnProperties = new ArrayList<String>();
                    returnProperties.add(Constants.doc_tipo_componente);
                    returnProperties.add(Constants.doc_docnum);

                    // recupero il TIPO_COMPONENTE dei related
                    DataTable<String> searchResultsRelated = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

                    // nessun related con TIPO_COMPONENTE=PRINCIPALE
                    if (searchResultsRelated == null || searchResultsRelated.getRows().size() == 0) {
                        continue;
                    }

                    // almeno un risultato l'ho trovato...
                    throw new DocerException("documento " + docId + ": non e' permesso modificare il campo " + Constants.doc_tipo_componente + " da " + oldTipoComponente + " a " + newTipoComponente
                            + " in quanto la catena dei Related contiene gia' un documento (" + searchResultsRelated.getRow(0).get(Constants.doc_docnum) + ") con " + Constants.doc_tipo_componente
                            + " PRINCIPALE");

                }

                continue;
            }

            if (propName.equalsIgnoreCase(Constants.doc_classifica) || propName.equalsIgnoreCase(Constants.doc_cod_titolario)) {

                if (newValue.equals(oldValue)) {
                    cleanMetadata.remove(propName);
                    continue;
                }

                throw new DocerException("documento " + docId + ": il metodo updateProfileDocument non permette di modificare il campo " + propName
                        + " di un documento. Per la classificazione utilizzare il metodo classificaDocumento");
            }

            if (propName.equalsIgnoreCase(Constants.doc_protocollo_registro) || propName.equalsIgnoreCase(Constants.doc_protocollo_oggetto) || propName.equalsIgnoreCase(Constants.doc_protocollo_data)
                    || propName.equalsIgnoreCase(Constants.doc_protocollo_numero) || propName.equalsIgnoreCase(Constants.doc_protocollo_anno)
                    || propName.equalsIgnoreCase(Constants.doc_protocollo_annullato_protocollo) || propName.equalsIgnoreCase(Constants.doc_protocollo_data_annullamento_protocollo)
                    || propName.equalsIgnoreCase(Constants.doc_protocollo_motivo_annullamento_protocollo) || propName.equalsIgnoreCase(Constants.doc_protocollo_provvedimento_annullamento_protocollo)) {

                if (newValue.equals(oldValue)) {
                    cleanMetadata.remove(propName);
                    continue;
                }

                throw new DocerException("documento " + docId + ": il metodo updateProfileDocument non permette di modificare il campo " + propName
                        + " di un documento. Per la protocollazione utilizzare il metodo protocollaDocumento");
            }

            if (propName.equalsIgnoreCase(Constants.doc_progr_fascicolo) || propName.equalsIgnoreCase(Constants.doc_anno_fascicolo) || propName.equalsIgnoreCase(Constants.doc_fascicoli_secondari)
                    || propName.equalsIgnoreCase(Constants.doc_num_fascicolo)) {

                if (newValue.equals(oldValue)) {
                    cleanMetadata.remove(propName);
                    continue;
                }

                throw new DocerException("documento " + docId + ": il metodo updateProfileDocument non permette di modificare il campo " + propName
                        + " di un documento. Per la fascicolazione utilizzare il metodo fascicolaDocumento");
            }

            // metadati di registrazione
            if (propName.equalsIgnoreCase(Constants.doc_registrazione_anno) || propName.equalsIgnoreCase(Constants.doc_registrazione_data)
                    || propName.equalsIgnoreCase(Constants.doc_registrazione_id_registro)
                    || propName.equalsIgnoreCase(Constants.doc_registrazione_numero)
                    || propName.equalsIgnoreCase(Constants.doc_registrazione_oggetto)
                    // nuovi metadati 25-10-2012
                    || propName.equalsIgnoreCase(Constants.doc_registrazione_annullata_registrazione) || propName.equalsIgnoreCase(Constants.doc_registrazione_data_annullamento_registrazione)
                    || propName.equalsIgnoreCase(Constants.doc_registrazione_motivo_annullamento_registrazione)
                    || propName.equalsIgnoreCase(Constants.doc_registrazione_provvedimento_annullamento_registrazione)) {

                if (newValue.equals(oldValue)) {
                    cleanMetadata.remove(propName);
                    continue;
                }

                throw new DocerException("documento " + docId + ": il metodo updateProfileDocument non permette di modificare il campo " + propName
                        + " di un documento. Per la registrazione utilizzare il metodo registraDocumento");
            }

            // metadati di pubblicazione
            if (propName.equalsIgnoreCase(Constants.doc_pubblicazione_registro) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_anno)
                    || propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_fine) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_inizio)
                    || propName.equalsIgnoreCase(Constants.doc_pubblicazione_numero) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_oggetto)
                    || propName.equalsIgnoreCase(Constants.doc_pubblicazione_pubblicato)) {

                if (newValue.equals(oldValue)) {
                    cleanMetadata.remove(propName);
                    continue;
                }

                throw new DocerException("documento " + docId + ": il metodo updateProfileDocument non permette di specificare il campo " + propName
                        + " di un documento. Per la pubblicazione utilizzare il metodo pubblicaDocumento");
            }

            if (newValue.equals(oldValue)) {
                continue; // non cambiano
            }

            if (propName.equalsIgnoreCase(Constants.doc_docname)) {
                if (StringUtils.isEmpty(newValue)) {
                    throw new DocerException("documento " + docId + ": non e' possibile modificare il campo " + Constants.doc_docname + " in null o vuoto");
                }

                String newExtension = getExtension(newValue);
                String oldExtension = getExtension(oldValue);

                if (!oldArchiveType.equalsIgnoreCase("URL") && !newExtension.equalsIgnoreCase(oldExtension)) {
                    throw new DocerException("documento " + docId + ": non e' possibile modificare la parte estensione nel campo " + Constants.doc_docname + ": utilizzare versionamento avanzato");
                }
                continue;
            }

            if (propName.equalsIgnoreCase(Constants.doc_docnum_record)) {
                throw new DocerException("documento " + docId + ": non e' possibile modificare esplicitamente il campo " + propName + " di un documento");
            }

            if (propName.equalsIgnoreCase(Constants.doc_stato_archivistico)) {

                EnumStatoArchivistico newStatoArch = getEnumStatoArchivistico(newValue);
                EnumStatoArchivistico oldStatoArch = getEnumStatoArchivistico(oldValue);

                if (!oldStatoArch.equals(EnumStatoArchivistico.generico) || !newStatoArch.equals(EnumStatoArchivistico.generico_definitivo)) {

                    throw new DocerException("documento " + docId + ": e' possibile modificare il campo " + propName + " di un documento limitatamente al passaggio dal valore "
                            + String.valueOf(EnumStatoArchivistico.generico.getCode()) + " (" + EnumStatoArchivistico.generico + ") al valore "
                            + String.valueOf(EnumStatoArchivistico.generico_definitivo.getCode()) + " (" + EnumStatoArchivistico.generico_definitivo + ")");
                }
                continue;
            }

            if (propName.equalsIgnoreCase(Constants.doc_cod_ente) || propName.equalsIgnoreCase(Constants.doc_cod_aoo) || propName.equalsIgnoreCase(Constants.doc_docnum)
                    // || propName.equalsIgnoreCase(Constants.doc_app_id)
                    || propName.equalsIgnoreCase(Constants.doc_docnum_princ)
                    // ||
                    // propName.equalsIgnoreCase(Constants.doc_stato_archivistico)
                    || propName.equalsIgnoreCase(Constants.doc_stato_pantarei)) {
                throw new DocerException("documento " + docId + ": non e' possibile modificare il campo " + propName + " di un documento");
            }

            if (propName.equalsIgnoreCase(Constants.doc_creation_date) || propName.equalsIgnoreCase(Constants.doc_type_id)) {

                if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato) || oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato)
                        || oldStatoArchivistico.equals(EnumStatoArchivistico.fascicolato) || oldStatoArchivistico.equals(EnumStatoArchivistico.classificato)) {

                    throw new DocerException("documento " + docId + ": non e' possibile modificare " + propName + " di un documento con " + Constants.doc_stato_archivistico + " "
                            + oldStatoArchivistico + " (" + oldStatoArchivistico.getCode() + ")");
                }

                if (oldPubblicato) {
                    throw new DocerException("documento " + docId + ": non e' possibile modificare " + propName + " di un documento PUBBLICATO");
                }

                continue;
            }

        } // fine ciclo for

        return cleanMetadata;
    }

    // private void checkIfPropertyChangesz(String methodName, String fieldName,
    // EnumStatoArchivistico oldStatoArchivistico, String oldValue, String
    // newValue) throws DocerException {
    //
    // String operation = VOID;
    // if (methodName.startsWith("registra"))
    // operation = "registrazione";
    // else if (methodName.startsWith("classifica"))
    // operation = "classificazione";
    // else if (methodName.startsWith("protocolla"))
    // operation = "protocollazione";
    // else if (methodName.startsWith("pubblica"))
    // operation = "pubblicazione";
    // else if (methodName.startsWith("archivia"))
    // operation = "archivio di deposito";
    //
    // String messageStart1 = methodName +
    // ": non e' ammesso eseguire l'operazione di " + operation;
    // String messageStart2 = methodName +
    // ": non e' ammessa la modifica del campo " + fieldName;
    // String messageEnd = " per un documento con " +
    // Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " (" +
    // oldStatoArchivistico.getCode() + ")";
    //
    // if (operation.equals("pubblicazione")) {
    // messageEnd = " per un documento PUBBLICATO";
    // }
    //
    // if (oldValue == null || oldValue.equals(VOID)) {
    //
    // if (newValue != null && !newValue.equals(VOID))
    // throw new DocerException(messageStart1 + messageEnd);
    //
    // return;
    // }
    //
    // if (oldValue != null) {
    //
    // if (newValue == null)
    // newValue = VOID;
    //
    // if (!oldValue.equals(newValue)) {
    // throw new DocerException(messageStart2 + messageEnd);
    // }
    // }
    // }

    private void forbidRegistrationChanges(DataRow<String> oldProfile, Map<String, String> metadatiRegistrazione) throws DocerException {

        EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

        String newRegIdReg = metadatiRegistrazione.get(Constants.doc_registrazione_id_registro);
        String newRegNumero = metadatiRegistrazione.get(Constants.doc_registrazione_numero);

        String newRegData = metadatiRegistrazione.get(Constants.doc_registrazione_data);

        String oldRegIdReg = oldProfile.get(Constants.doc_registrazione_id_registro);
        String oldRegNumero = oldProfile.get(Constants.doc_registrazione_numero);

        String oldRegData = oldProfile.get(Constants.doc_registrazione_data);

        if (metadatiRegistrazione.containsKey(Constants.doc_registrazione_id_registro)) {
            checkIfPropertyChanges("registraDocumento", Constants.doc_registrazione_id_registro, oldStatoArchivistico, oldRegIdReg, newRegIdReg);
        }

        if (metadatiRegistrazione.containsKey(Constants.doc_registrazione_numero)) {
            checkIfPropertyChanges("registraDocumento", Constants.doc_registrazione_numero, oldStatoArchivistico, oldRegNumero, newRegNumero);
        }

        if (metadatiRegistrazione.containsKey(Constants.doc_registrazione_data)) {
            checkIfPropertyChanges("registraDocumento", Constants.doc_registrazione_data, oldStatoArchivistico, oldRegData, newRegData);
        }

        if (metadatiRegistrazione.containsKey(Constants.doc_registrazione_anno)) {
            String newRegAnno = metadatiRegistrazione.get(Constants.doc_registrazione_anno);
            String oldRegAnno = oldProfile.get(Constants.doc_registrazione_anno);

            checkIfPropertyChanges("registraDocumento", Constants.doc_registrazione_anno, oldStatoArchivistico, oldRegAnno, newRegAnno);
        }
    }

    private void forbidPubblicazioneChanges(DataRow<String> oldProfile, Map<String, String> metadatiPubblicazione) throws DocerException {

        EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

        String oldPubAnno = oldProfile.get(Constants.doc_pubblicazione_anno);
        String oldPubDataInizio = oldProfile.get(Constants.doc_pubblicazione_data_inizio);
        String oldPubDataFine = oldProfile.get(Constants.doc_pubblicazione_data_fine);
        String oldPubNumero = oldProfile.get(Constants.doc_pubblicazione_numero);
        String oldPubReg = oldProfile.get(Constants.doc_pubblicazione_registro);
        String oldPubblicato = oldProfile.get(Constants.doc_pubblicazione_pubblicato);

        String newPubAnno = metadatiPubblicazione.get(Constants.doc_pubblicazione_anno);
        String newPubDataInizio = metadatiPubblicazione.get(Constants.doc_pubblicazione_data_inizio);
        String newPubDataFine = metadatiPubblicazione.get(Constants.doc_pubblicazione_data_fine);
        String newPubNumero = metadatiPubblicazione.get(Constants.doc_pubblicazione_numero);
        String newPubReg = metadatiPubblicazione.get(Constants.doc_pubblicazione_registro);
        String newPubblicato = metadatiPubblicazione.get(Constants.doc_pubblicazione_pubblicato);

        // Se si tratta di un annullamento pubblicazione, i nuovi metadati devono essere uguali ai vecchi (escluso il flag pubblicato)
        if ( ("false".equalsIgnoreCase(oldProfile.get(Constants.doc_pubblicazione_annullata)) && metadatiPubblicazione.get(Constants.doc_pubblicazione_annullata) == null)
                || "true".equalsIgnoreCase(metadatiPubblicazione.get(Constants.doc_pubblicazione_annullata)) ) {

            if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_registro)) {
                checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_registro, oldStatoArchivistico, oldPubReg, newPubReg);
            }

            if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_numero)) {
                checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_numero, oldStatoArchivistico, oldPubNumero, newPubNumero);
            }

            if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_data_inizio)) {
                String newDataOra = newPubDataInizio.substring(0, 23);
                String oldDataOra = oldPubDataInizio.substring(0, 23);
                checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_data_inizio, oldStatoArchivistico, oldDataOra, newDataOra);
            }

            if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_data_fine)) {
                String newDataOra = newPubDataFine.substring(0, 23);
                String oldDataOra = oldPubDataFine.substring(0, 23);
                checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_data_fine, oldStatoArchivistico, oldDataOra, newDataOra);
            }

            if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_anno)) {
                checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_anno, oldStatoArchivistico, oldPubAnno, newPubAnno);
            }

//        if (metadatiPubblicazione.containsKey(Constants.doc_pubblicazione_pubblicato)) {
//            checkIfPropertyChanges("pubblicaDocumento", Constants.doc_pubblicazione_pubblicato, oldStatoArchivistico, oldPubblicato, newPubblicato);
//        }
        }
    }

    private void forbidProtocolChanges(DataRow<String> oldProfile, Map<String, String> metadatiProtocollazione) throws DocerException {

        EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

        String newProtoAnno = metadatiProtocollazione.get(Constants.doc_protocollo_anno);
        String newProtoReg = metadatiProtocollazione.get(Constants.doc_protocollo_registro);
        String newProtoNumero = metadatiProtocollazione.get(Constants.doc_protocollo_numero);
        String newProtoData = metadatiProtocollazione.get(Constants.doc_protocollo_data);
        String newTipoProtocollazione = metadatiProtocollazione.get(Constants.doc_protocollo_tipo_protocollazione);

        String oldProtoAnno = oldProfile.get(Constants.doc_protocollo_anno);
        String oldProtoReg = oldProfile.get(Constants.doc_protocollo_registro);
        String oldProtoNumero = oldProfile.get(Constants.doc_protocollo_numero);
        String oldProtoData = oldProfile.get(Constants.doc_protocollo_data);
        String oldTipoProtocollazione = oldProfile.get(Constants.doc_protocollo_tipo_protocollazione);

        if (metadatiProtocollazione.containsKey(Constants.doc_protocollo_registro)) {
            checkIfPropertyChanges("protocollaDocumento", Constants.doc_protocollo_registro, oldStatoArchivistico, oldProtoReg, newProtoReg);
        }

        if (metadatiProtocollazione.containsKey(Constants.doc_protocollo_numero)) {
            checkIfPropertyChanges("protocollaDocumento", Constants.doc_protocollo_numero, oldStatoArchivistico, oldProtoNumero, newProtoNumero);
        }

        if (metadatiProtocollazione.containsKey(Constants.doc_protocollo_data)) {
            checkIfPropertyChanges("protocollaDocumento", Constants.doc_protocollo_data, oldStatoArchivistico, oldProtoData, newProtoData);
        }

        if (metadatiProtocollazione.containsKey(Constants.doc_protocollo_anno)) {
            checkIfPropertyChanges("protocollaDocumento", Constants.doc_protocollo_anno, oldStatoArchivistico, oldProtoAnno, newProtoAnno);
        }

        if (metadatiProtocollazione.containsKey(Constants.doc_protocollo_tipo_protocollazione)) {
            checkIfPropertyChanges("protocollaDocumento", Constants.doc_protocollo_tipo_protocollazione, oldStatoArchivistico, oldTipoProtocollazione, newTipoProtocollazione);
        }
    }

    private void forbidRegAndProtoCommonFieldsChanges(String methodName, DataRow<String> oldProfile, Map<String, String> metadati) throws DocerException {

        // campi comuni registrazione e protocollazione
        String newDestinatari = metadati.get(Constants.doc_reg_e_proto_destinatari);
        String newFirmatario = metadati.get(Constants.doc_reg_e_proto_firmatario);
        String newMittenti = metadati.get(Constants.doc_reg_e_proto_mittenti);
        String newTipoFirma = metadati.get(Constants.doc_reg_e_proto_tipo_firma);

        EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

        String oldDestinatari = oldProfile.get(Constants.doc_reg_e_proto_destinatari);
        String oldFirmatario = oldProfile.get(Constants.doc_reg_e_proto_firmatario);
        String oldMittenti = oldProfile.get(Constants.doc_reg_e_proto_mittenti);
        String oldTipoFirma = oldProfile.get(Constants.doc_reg_e_proto_tipo_firma);

        // modifica del 14/12/2012
        // if(metadati.containsKey(Constants.doc_reg_e_proto_destinatari)){
        // checkIfPropertyChanges(Constants.doc_reg_e_proto_destinatari,
        // oldStatoArchivistico,oldDestinatari, newDestinatari);
        // }
        //
        // if(metadati.containsKey(Constants.doc_reg_e_proto_firmatario)){
        // checkIfPropertyChanges(Constants.doc_reg_e_proto_firmatario,
        // oldStatoArchivistico,oldFirmatario, newFirmatario);
        // }
        //
        // if(metadati.containsKey(Constants.doc_reg_e_proto_mittenti)){
        // checkIfPropertyChanges(Constants.doc_reg_e_proto_mittenti,
        // oldStatoArchivistico,oldMittenti, newMittenti);
        // }

        if (metadati.containsKey(Constants.doc_reg_e_proto_tipo_firma)) {
            checkIfPropertyChanges(methodName, Constants.doc_reg_e_proto_tipo_firma, oldStatoArchivistico, oldTipoFirma, newTipoFirma);
        }
    }

    private void checkIfPropertyChanges(String methodName, String fieldName, EnumStatoArchivistico oldStatoArchivistico, String oldValue, String newValue) throws DocerException {

        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }

        String messageStart = methodName + ": non e' ammessa la modifica del campo " + fieldName;
        String messageEnd = " per un documento con " + Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " (" + oldStatoArchivistico.getCode() + ")";

        // if (StringUtils.isEmpty(oldValue)) {
        //
        // if (StringUtils.isNotEmpty(newValue))
        // throw new DocerException(messageStart + messageEnd);
        //
        // return;
        // }

        // if (oldValue != null) {
        //
        // if (newValue == null)
        // newValue = VOID;
        //
        // if (!oldValue.equals(newValue)) {
        // throw new DocerException(messageStart + messageEnd);
        // }
        // }

        if (!oldValue.equals(newValue)) {
            throw new DocerException(messageStart + messageEnd);
        }
    }

    private void validateRegistrazioneProperties(Map<String, String> metadatiRegistrazione) throws DocerException {

        String idRegistro = metadatiRegistrazione.get(Constants.doc_registrazione_id_registro);
        String numeroRegistro = metadatiRegistrazione.get(Constants.doc_registrazione_numero);
        String dataRegistro = metadatiRegistrazione.get(Constants.doc_registrazione_data);

        String tipoFirma = metadatiRegistrazione.get(Constants.doc_reg_e_proto_tipo_firma);

        // String destinatari =
        // metadatiRegistrazione.get(Constants.doc_reg_e_proto_destinatari);
        String firmatario = metadatiRegistrazione.get(Constants.doc_reg_e_proto_firmatario);
        // String mittenti =
        // metadatiRegistrazione.get(Constants.doc_reg_e_proto_mittenti);

        if (StringUtils.isEmpty(numeroRegistro) || StringUtils.isEmpty(idRegistro) || StringUtils.isEmpty(dataRegistro)) {
            throw new DocerException(Constants.doc_registrazione_numero + ", " + Constants.doc_registrazione_id_registro + ", " + Constants.doc_registrazione_data
                    + " obbligatori per la registrazione");
        }

        if (StringUtils.isEmpty(tipoFirma)) {
            throw new DocerException(Constants.doc_reg_e_proto_tipo_firma + " obbligatorio per la registrazione");
        }

        if (StringUtils.isEmpty(firmatario)) {
            if (tipoFirma.equals("F")) {
                throw new DocerException(Constants.doc_reg_e_proto_firmatario + " obbligatorio per la registrazione quando " + Constants.doc_reg_e_proto_tipo_firma + " ha valore F");
            }
        }

        // FACOLTATIVI
        // if (mittenti == null || mittenti.equals(VOID)) {
        // if(tipoProtocollazione.equals("E")){
        // throw new DocerException(Constants.doc_reg_e_proto_mittenti
        // + " obbligatorio per la registrazione se "
        // +Constants.doc_protocollo_tipo_protocollazione +" ha valore E");
        // }
        // }
        //
        // if (destinatari== null || destinatari.equals(VOID)) {
        // if(tipoProtocollazione.equals("I") ||
        // tipoProtocollazione.equals("U")){
        // throw new DocerException(Constants.doc_reg_e_proto_destinatari
        // + " obbligatorio per la registrazione se "
        // +Constants.doc_protocollo_tipo_protocollazione +" ha valore I o U");
        // }
        // }

        DateTime dataRegistrazione = new DateTime();
        try {

            dataRegistrazione = parseDateTime(dataRegistro);
        } catch (Exception e) {
            throw new DocerException(Constants.doc_registrazione_data + " formato errato");
        }

        String annoRegistro = String.valueOf(dataRegistrazione.getYear());

        metadatiRegistrazione.put(Constants.doc_registrazione_anno, annoRegistro);

    }

    private void validateProtocolloProperties(Map<String, String> metadatiProtocollazione, boolean migration_mode) throws DocerException {

        String registroProtocollo = metadatiProtocollazione.get(Constants.doc_protocollo_registro);
        String numeroProtocollo = metadatiProtocollazione.get(Constants.doc_protocollo_numero);
        String annoProtocollo = metadatiProtocollazione.get(Constants.doc_protocollo_anno);
        String dataProtocollo = metadatiProtocollazione.get(Constants.doc_protocollo_data);
        String tipoProtocollazione = metadatiProtocollazione.get(Constants.doc_protocollo_tipo_protocollazione);

        String tipoFirma = metadatiProtocollazione.get(Constants.doc_reg_e_proto_tipo_firma);

        String destinatari = metadatiProtocollazione.get(Constants.doc_reg_e_proto_destinatari);
        String firmatario = metadatiProtocollazione.get(Constants.doc_reg_e_proto_firmatario);
        String mittenti = metadatiProtocollazione.get(Constants.doc_reg_e_proto_mittenti);

        if (migration_mode) {
            if (StringUtils.isEmpty(numeroProtocollo) || StringUtils.isEmpty(registroProtocollo) || StringUtils.isEmpty(annoProtocollo)) {
                throw new DocerException("migration mode: " + Constants.doc_protocollo_numero + ", " + Constants.doc_protocollo_registro + ", " + Constants.doc_protocollo_anno
                        + " obbligatori per la protocollazione");
            }
        } else {

            if (StringUtils.isEmpty(numeroProtocollo) || StringUtils.isEmpty(registroProtocollo) || StringUtils.isEmpty(dataProtocollo) || StringUtils.isEmpty(tipoProtocollazione)
                    || StringUtils.isEmpty(tipoFirma)) {
                throw new DocerException(Constants.doc_protocollo_numero + ", " + Constants.doc_protocollo_registro + ", " + Constants.doc_protocollo_data + ", "
                        + Constants.doc_protocollo_tipo_protocollazione + ", " + Constants.doc_reg_e_proto_tipo_firma + " obbligatori per la protocollazione");
            }

            if (StringUtils.isEmpty(tipoFirma)) {
                throw new DocerException(Constants.doc_reg_e_proto_tipo_firma + " obbligatorio per la protocollazione");
            }

            if (StringUtils.isEmpty(firmatario)) {
                if (tipoFirma.equals("F")) {
                    throw new DocerException(Constants.doc_reg_e_proto_firmatario + " obbligatorio per la protocollazione se " + Constants.doc_reg_e_proto_tipo_firma + " ha valore F");
                }
            }

            if (StringUtils.isEmpty(mittenti)) {
                if (tipoProtocollazione.equals("E")) {
                    throw new DocerException(Constants.doc_reg_e_proto_mittenti + " obbligatorio per la protocollazione se " + Constants.doc_protocollo_tipo_protocollazione + " ha valore E");
                }
            }

            if (StringUtils.isEmpty(destinatari)) {
                if (tipoProtocollazione.equals("I") || tipoProtocollazione.equals("U")) {
                    throw new DocerException(Constants.doc_reg_e_proto_destinatari + " obbligatorio per la protocollazione se " + Constants.doc_protocollo_tipo_protocollazione + " ha valore I o U");
                }
            }

            DateTime dataProtocollazione = new DateTime();
            try {

                dataProtocollazione = parseDateTime(dataProtocollo);
            } catch (Exception e) {
                throw new DocerException(Constants.doc_protocollo_data + " formato errato");
            }

            annoProtocollo = String.valueOf(dataProtocollazione.getYear());
            metadatiProtocollazione.put(Constants.doc_protocollo_anno, annoProtocollo);
        }

    }

    private void validatePubblicazioneProperties(Map<String, String> metadatiPubblicazione) throws DocerException {

        String registroPubblicazione = metadatiPubblicazione.get(Constants.doc_pubblicazione_registro);
        String numeroPubblicazione = metadatiPubblicazione.get(Constants.doc_pubblicazione_numero);
        String dataInizioPub = metadatiPubblicazione.get(Constants.doc_pubblicazione_data_inizio);
        String dataFinePub = metadatiPubblicazione.get(Constants.doc_pubblicazione_data_fine);

        String annull_pub = metadatiPubblicazione.get(Constants.doc_pubblicazione_annullata);
        String d_annull_pub = metadatiPubblicazione.get(Constants.doc_pubblicazione_data_annullamento);
        String m_annull_pub = metadatiPubblicazione.get(Constants.doc_pubblicazione_motivo_annullamento);
        // String pubblicato =
        // metadatiPubblicazione.get(Constants.doc_pubblicazione_pubblicato);


        if (registroPubblicazione == null || registroPubblicazione.equals(VOID) || numeroPubblicazione == null || numeroPubblicazione.equals(VOID) || dataInizioPub == null
                || dataInizioPub.equals(VOID) || dataFinePub == null || dataFinePub.equals(VOID)) {
            // || pubblicato == null || pubblicato.equals(VOID)) {
            throw new DocerException(Constants.doc_pubblicazione_registro + ", " + Constants.doc_pubblicazione_numero + ", " + Constants.doc_pubblicazione_data_inizio + ", "
                    + Constants.doc_pubblicazione_data_fine + ", " + Constants.doc_pubblicazione_pubblicato + " obbligatori per la pubblicazione");
        }


        DateTime dataPub = new DateTime();
        try {

            dataPub = parseDateTime(dataInizioPub);
        } catch (Exception e) {
            throw new DocerException(Constants.doc_pubblicazione_data_inizio + " formato errato");
        }

        String annoPub = String.valueOf(dataPub.getYear());

        metadatiPubblicazione.put(Constants.doc_pubblicazione_anno, annoPub);
        //metadatiPubblicazione.put(Constants.doc_pubblicazione_pubblicato, "true");


        if ("false".equalsIgnoreCase(metadatiPubblicazione.get(Constants.doc_pubblicazione_pubblicato))) { //se si tratta di annullamento di una pubblicazione

            if (annull_pub == null || annull_pub.equals(VOID) || d_annull_pub == null || d_annull_pub.equals(VOID) || m_annull_pub == null
                    || m_annull_pub.equals(VOID)) {
                // || pubblicato == null || pubblicato.equals(VOID)) {
                throw new DocerException(Constants.doc_pubblicazione_annullata + ", " + Constants.doc_pubblicazione_data_annullamento + ", " + Constants.doc_pubblicazione_motivo_annullamento + " obbligatori per l'annullamento della pubblicazione");
            }

        } else {
            //se pubblicazione e' true setto la pubblicazione come annullata uguale a false
            metadatiPubblicazione.put(Constants.doc_pubblicazione_annullata, "false");
            metadatiPubblicazione.put(Constants.doc_pubblicazione_data_annullamento, "");
            metadatiPubblicazione.put(Constants.doc_pubblicazione_motivo_annullamento, "");
        }

    }

    private Map<String, String> checkAndGetMetadatiRegistrazione(DataRow<String> oldProfile, Map<String, String> metadata) throws DocerException {

        String oldCodEnte = oldProfile.get(Constants.doc_cod_ente);
        String oldCodAOO = oldProfile.get(Constants.doc_cod_aoo);
        String oldRegNumero = oldProfile.get(Constants.doc_registrazione_numero);

        EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));
        EnumStatoArchivistico newStatoArchivistico = EnumStatoArchivistico.undefined;

        Map<String, String> metadatiRegistrazione = new HashMap<String, String>();
        metadatiRegistrazione.put(Constants.doc_cod_ente, oldCodEnte);
        metadatiRegistrazione.put(Constants.doc_cod_aoo, oldCodAOO);

        EnumStatiPantarei oldStatoPantarei = getEnumStatoPantarei(oldProfile.get(Constants.doc_stato_pantarei));
        // la registrazione di un allegato dovrebbe essere bloccata prima ma la
        // controllo per sicurezza
        if (oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {
            throw new DocerException("compatibilita' DOCAREA: non e' permesso invocare il metodo registraDocumento per un documento con STATO_PANTAREI allegato (6) ");
        }

        metadatiRegistrazione.put(Constants.doc_stato_pantarei, oldProfile.get(Constants.doc_stato_pantarei));

        if (metadata.containsKey(Constants.doc_registrazione_data)) {
            metadatiRegistrazione.put(Constants.doc_registrazione_data, metadata.get(Constants.doc_registrazione_data));
        }

        if (metadata.containsKey(Constants.doc_registrazione_id_registro)) {
            metadatiRegistrazione.put(Constants.doc_registrazione_id_registro, metadata.get(Constants.doc_registrazione_id_registro));
        }

        if (metadata.containsKey(Constants.doc_registrazione_numero)) {
            metadatiRegistrazione.put(Constants.doc_registrazione_numero, metadata.get(Constants.doc_registrazione_numero));
        }

        if (metadata.containsKey(Constants.doc_registrazione_oggetto)) {
            metadatiRegistrazione.put(Constants.doc_registrazione_oggetto, metadata.get(Constants.doc_registrazione_oggetto));
        }

        // if(metadata.containsKey(Constants.doc_protocollo_tipo_protocollazione)){
        // metadatiRegistrazione.put(Constants.doc_protocollo_tipo_protocollazione,
        // metadata.get(Constants.doc_protocollo_tipo_protocollazione));
        // }

        if (metadata.containsKey(Constants.doc_reg_e_proto_tipo_firma)) {
            metadatiRegistrazione.put(Constants.doc_reg_e_proto_tipo_firma, metadata.get(Constants.doc_reg_e_proto_tipo_firma));
        } else {
            // se e' gia' stato assegnato lo recupero dal vecchio profilo (e' un
            // campo che sottosta alla logica di business della registrazione)
            String oldTipoFirma = oldProfile.get(Constants.doc_reg_e_proto_tipo_firma);
            if (StringUtils.isNotEmpty(oldTipoFirma)) {
                metadatiRegistrazione.put(Constants.doc_reg_e_proto_tipo_firma, oldTipoFirma);
            }

        }

        if (metadata.containsKey(Constants.doc_reg_e_proto_firmatario)) {
            metadatiRegistrazione.put(Constants.doc_reg_e_proto_firmatario, metadata.get(Constants.doc_reg_e_proto_firmatario));
        } else {
            // se e' gia' stato assegnato lo recupero dal vecchio profilo (e' un
            // campo che sottosta alla logica di business della registrazione)
            String oldFirmatario = oldProfile.get(Constants.doc_reg_e_proto_firmatario);
            if (StringUtils.isNotEmpty(oldFirmatario)) {
                metadatiRegistrazione.put(Constants.doc_reg_e_proto_firmatario, oldFirmatario);
            }

        }

        if (metadata.containsKey(Constants.doc_reg_e_proto_destinatari)) {
            metadatiRegistrazione.put(Constants.doc_reg_e_proto_destinatari, metadata.get(Constants.doc_reg_e_proto_destinatari));
        }

        if (metadata.containsKey(Constants.doc_reg_e_proto_mittenti)) {
            metadatiRegistrazione.put(Constants.doc_reg_e_proto_mittenti, metadata.get(Constants.doc_reg_e_proto_mittenti));
        }

        // nuovi metadati 25-10-2012
        if (metadata.containsKey(Constants.doc_registrazione_annullata_registrazione)) {
            metadatiRegistrazione.put(Constants.doc_registrazione_annullata_registrazione, metadata.get(Constants.doc_registrazione_annullata_registrazione));
        }

        if (metadata.containsKey(Constants.doc_registrazione_data_annullamento_registrazione)) {
            metadatiRegistrazione.put(Constants.doc_registrazione_data_annullamento_registrazione, metadata.get(Constants.doc_registrazione_data_annullamento_registrazione));
        }

        if (metadata.containsKey(Constants.doc_registrazione_motivo_annullamento_registrazione)) {
            metadatiRegistrazione.put(Constants.doc_registrazione_motivo_annullamento_registrazione, metadata.get(Constants.doc_registrazione_motivo_annullamento_registrazione));
        }

        if (metadata.containsKey(Constants.doc_registrazione_provvedimento_annullamento_registrazione)) {
            metadatiRegistrazione.put(Constants.doc_registrazione_provvedimento_annullamento_registrazione, metadata.get(Constants.doc_registrazione_provvedimento_annullamento_registrazione));
        }

        boolean hasClassifica = false;
        boolean hasFascicolo = false;
        boolean hasProtocollo = false;
        boolean hasRegistrazione = false;

        if (StringUtils.isNotEmpty(oldRegNumero)) {
            hasRegistrazione = true;
        }

        String oldNumPg = oldProfile.get(Constants.doc_protocollo_numero);
        if (StringUtils.isNotEmpty(oldNumPg)) {
            hasProtocollo = true;
            metadatiRegistrazione.put(Constants.doc_stato_pantarei, Integer.toString(EnumStatiPantarei.protocollato.getCode()));
        }

        String oldClassifica = oldProfile.get(Constants.doc_classifica);
        if (StringUtils.isNotEmpty(oldClassifica)) {
            hasClassifica = true;
        }
        String oldProgrFascicolo = oldProfile.get(Constants.doc_progr_fascicolo);
        if (StringUtils.isNotEmpty(oldProgrFascicolo)) {
            hasFascicolo = true;
        }

        if (hasProtocollo || hasRegistrazione) {
            forbidRegAndProtoCommonFieldsChanges("registraDocumento", oldProfile, metadatiRegistrazione);
        }

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico) || oldStatoArchivistico.equals(EnumStatoArchivistico.generico_definitivo)) {

            validateRegistrazioneProperties(metadatiRegistrazione);

            newStatoArchivistico = EnumStatoArchivistico.registrato;

            if (hasFascicolo) {
                newStatoArchivistico = EnumStatoArchivistico.fascicolato;
            } else if (hasClassifica) {
                newStatoArchivistico = EnumStatoArchivistico.classificato;
            }

            metadatiRegistrazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

            return metadatiRegistrazione;
        }

        // qui e' gia' record
        newStatoArchivistico = oldStatoArchivistico;
        metadatiRegistrazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato)) {

            // ammessa solo modifica dell'oggetto
            // proibisco modifiche di registro
            forbidRegistrationChanges(oldProfile, metadatiRegistrazione);
            return metadatiRegistrazione;
        }

        // se e' record
        if (oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato) || oldStatoArchivistico.equals(EnumStatoArchivistico.classificato)
                || oldStatoArchivistico.equals(EnumStatoArchivistico.fascicolato)) {

            // se ha metadati di registrazione e' ammessa solo modifica
            // dell'oggetto
            if (hasRegistrazione) {

                try {
                    // proibisco modifiche di registro
                    forbidRegistrationChanges(oldProfile, metadatiRegistrazione);
                } catch (DocerException e) {
                    throw new DocerException(e.getMessage() + " e metadati di registrazione assegnati");
                }
                return metadatiRegistrazione;
            }

            // registrazione di un documento con solo i metadati di protocollo
            // questo if e' inutile perche' se e' record e non ha metadati di
            // registrazione deve avere per forza quelli di protocollo
            if (hasProtocollo) {

                String tipo_protocollazione = oldProfile.get(Constants.doc_protocollo_tipo_protocollazione);
                if (tipo_protocollazione == null) {
                    tipo_protocollazione = "";
                }

                // posso fare la registrazione ma non cambia lo
                // stato_archivistico
                if (tipo_protocollazione.equals("E")) {
                    validateRegistrazioneProperties(metadatiRegistrazione);
                } else {
                    try {
                        forbidRegistrationChanges(oldProfile, metadatiRegistrazione);
                    } catch (DocerException e) {
                        throw new DocerException(e.getMessage() + ", metadati di protocollazione assegnati  e " + Constants.doc_protocollo_tipo_protocollazione + "=" + tipo_protocollazione);
                    }
                }

                return metadatiRegistrazione;

            }

            // qui non dovrebbe mai arrivare...
            // se non ha protocollo e non ha registro allora lo posso modificare
            // (assegnare)
            validateRegistrazioneProperties(metadatiRegistrazione);
            metadatiRegistrazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));
            return metadatiRegistrazione;

        }

        throw new DocerException("non e' ammesso invocare il metodo registraDocumento per un documento con " + Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " ("
                + oldStatoArchivistico.getCode() + ")");
    }

    private Map<String, String> checkAndGetMetadatiProtocollazione(DataRow<String> oldProfile, Map<String, String> metadata) throws DocerException {

        String oldCodEnte = oldProfile.get(Constants.doc_cod_ente);

        boolean migration_mode = BL_CONFIGURATIONS.getConfig(entetoken).isMIGRATION_MODE();

        String oldCodAOO = oldProfile.get(Constants.doc_cod_aoo);

        EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));
        EnumStatoArchivistico newStatoArchivistico = EnumStatoArchivistico.undefined;

        String oldProtoNumero = oldProfile.get(Constants.doc_protocollo_numero);

        Map<String, String> metadatiProtocollazione = new HashMap<String, String>();
        metadatiProtocollazione.put(Constants.doc_cod_ente, oldCodEnte);
        metadatiProtocollazione.put(Constants.doc_cod_aoo, oldCodAOO);

        // IMPOSTO LO STATO_PANTAREI A 4 (PROTOCOLLATO)
        EnumStatiPantarei oldStatoPantarei = getEnumStatoPantarei(oldProfile.get(Constants.doc_stato_pantarei));
        // la protocollazione di un allegato dovrebbe essere bloccata prima ma
        // la controllo per sicurezza
        if (oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {
            throw new DocerException("compatibilita' DOCAREA: non e' permesso invocare il metodo protocollaDocumento per un documento con STATO_PANTAREI allegato (6) ");
        }
        metadatiProtocollazione.put(Constants.doc_stato_pantarei, Integer.toString(EnumStatiPantarei.protocollato.getCode()));

        // bug fix 18-09-2012
        if (metadata.containsKey(Constants.doc_protocollo_anno)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_anno, metadata.get(Constants.doc_protocollo_anno));
        }

        if (metadata.containsKey(Constants.doc_protocollo_data)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_data, metadata.get(Constants.doc_protocollo_data));
        }

        if (metadata.containsKey(Constants.doc_protocollo_numero)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_numero, metadata.get(Constants.doc_protocollo_numero));
        }

        if (metadata.containsKey(Constants.doc_protocollo_registro)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_registro, metadata.get(Constants.doc_protocollo_registro));
        }

        if (metadata.containsKey(Constants.doc_protocollo_oggetto)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_oggetto, metadata.get(Constants.doc_protocollo_oggetto));
        }

        if (metadata.containsKey(Constants.doc_protocollo_tipo_protocollazione)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_tipo_protocollazione, metadata.get(Constants.doc_protocollo_tipo_protocollazione));
        }

        if (metadata.containsKey(Constants.doc_reg_e_proto_tipo_firma)) {
            metadatiProtocollazione.put(Constants.doc_reg_e_proto_tipo_firma, metadata.get(Constants.doc_reg_e_proto_tipo_firma));
        } else {
            // se e' gia' stato assegnato lo recupero dal vecchio profilo (e' un
            // campo che sottosta alla logica di business della protocollazione)
            String oldTipoFirma = oldProfile.get(Constants.doc_reg_e_proto_tipo_firma);
            if (StringUtils.isNotEmpty(oldTipoFirma)) {
                metadatiProtocollazione.put(Constants.doc_reg_e_proto_tipo_firma, oldTipoFirma);
            }
        }

        if (metadata.containsKey(Constants.doc_reg_e_proto_firmatario)) {
            metadatiProtocollazione.put(Constants.doc_reg_e_proto_firmatario, metadata.get(Constants.doc_reg_e_proto_firmatario));
        } else {
            // se e' gia' stato assegnato lo recupero dal vecchio profilo (e' un
            // campo che sottosta alla logica di business della protocollazione)
            String oldFirmatario = oldProfile.get(Constants.doc_reg_e_proto_firmatario);
            if (StringUtils.isNotEmpty(oldFirmatario)) {
                metadatiProtocollazione.put(Constants.doc_reg_e_proto_firmatario, oldFirmatario);
            }
        }

        if (metadata.containsKey(Constants.doc_reg_e_proto_destinatari)) {
            metadatiProtocollazione.put(Constants.doc_reg_e_proto_destinatari, metadata.get(Constants.doc_reg_e_proto_destinatari));
        } else {
            // se e' gia' stato assegnato lo recupero dal vecchio profilo (e' un
            // campo che sottosta alla logica di business della protocollazione)
            String oldDestinatari = oldProfile.get(Constants.doc_reg_e_proto_destinatari);
            if (StringUtils.isNotEmpty(oldDestinatari)) {
                metadatiProtocollazione.put(Constants.doc_reg_e_proto_destinatari, oldDestinatari);
            }
        }

        if (metadata.containsKey(Constants.doc_reg_e_proto_mittenti)) {
            metadatiProtocollazione.put(Constants.doc_reg_e_proto_mittenti, metadata.get(Constants.doc_reg_e_proto_mittenti));
        } else {
            // se e' gia' stato assegnato lo recupero dal vecchio profilo (e' un
            // campo che sottosta alla logica di business della protocollazione)
            String oldMittenti = oldProfile.get(Constants.doc_reg_e_proto_mittenti);
            if (StringUtils.isNotEmpty(oldMittenti)) {
                metadatiProtocollazione.put(Constants.doc_reg_e_proto_mittenti, oldMittenti);
            }
        }

        // nuovi metadati 25-10-2012
        if (metadata.containsKey(Constants.doc_protocollo_num_pg_mittente)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_num_pg_mittente, metadata.get(Constants.doc_protocollo_num_pg_mittente));
        }

        if (metadata.containsKey(Constants.doc_protocollo_data_pg_mittente)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_data_pg_mittente, metadata.get(Constants.doc_protocollo_data_pg_mittente));
        }

        if (metadata.containsKey(Constants.doc_protocollo_cod_ente_mittente)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_cod_ente_mittente, metadata.get(Constants.doc_protocollo_cod_ente_mittente));
        }

        if (metadata.containsKey(Constants.doc_protocollo_cod_aoo_mittente)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_cod_aoo_mittente, metadata.get(Constants.doc_protocollo_cod_aoo_mittente));
        }

        if (metadata.containsKey(Constants.doc_protocollo_classifica_mittente)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_classifica_mittente, metadata.get(Constants.doc_protocollo_classifica_mittente));
        }

        if (metadata.containsKey(Constants.doc_protocollo_fascicolo_mittente)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_fascicolo_mittente, metadata.get(Constants.doc_protocollo_fascicolo_mittente));
        }

        if (metadata.containsKey(Constants.doc_protocollo_annullato_protocollo)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_annullato_protocollo, metadata.get(Constants.doc_protocollo_annullato_protocollo));
        }

        if (metadata.containsKey(Constants.doc_protocollo_data_annullamento_protocollo)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_data_annullamento_protocollo, metadata.get(Constants.doc_protocollo_data_annullamento_protocollo));
        }

        if (metadata.containsKey(Constants.doc_protocollo_motivo_annullamento_protocollo)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_motivo_annullamento_protocollo, metadata.get(Constants.doc_protocollo_motivo_annullamento_protocollo));
        }

        if (metadata.containsKey(Constants.doc_protocollo_provvedimento_annullamento_protocollo)) {
            metadatiProtocollazione.put(Constants.doc_protocollo_provvedimento_annullamento_protocollo, metadata.get(Constants.doc_protocollo_provvedimento_annullamento_protocollo));
        }

        boolean hasRegistrazione = false;

        boolean hasClassifica = false;
        boolean hasFascicolo = false;
        boolean hasProtocollo = false;

        String oldRegistrazioneNumero = oldProfile.get(Constants.doc_registrazione_numero);
        if (oldRegistrazioneNumero != null && !oldRegistrazioneNumero.equals(VOID)) {
            hasRegistrazione = true;
        }

        if (oldProtoNumero != null && !oldProtoNumero.equals(VOID)) {
            hasProtocollo = true;
        }

        String oldClassifica = oldProfile.get(Constants.doc_classifica);
        if (oldClassifica != null && !oldClassifica.equals(VOID)) {
            hasClassifica = true;
        }
        String oldProgrFascicolo = oldProfile.get(Constants.doc_progr_fascicolo);
        if (oldProgrFascicolo != null && !oldProgrFascicolo.equals(VOID)) {
            hasFascicolo = true;
        }

        if (hasProtocollo || hasRegistrazione) {
            forbidRegAndProtoCommonFieldsChanges("protocollaDocumento", oldProfile, metadatiProtocollazione);
        }

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico) || oldStatoArchivistico.equals(EnumStatoArchivistico.generico_definitivo)) {

            validateProtocolloProperties(metadatiProtocollazione, migration_mode);

            newStatoArchivistico = EnumStatoArchivistico.protocollato;

            if (hasFascicolo) {
                newStatoArchivistico = EnumStatoArchivistico.fascicolato;
            } else if (hasClassifica) {
                newStatoArchivistico = EnumStatoArchivistico.classificato;
            }

            metadatiProtocollazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

            return metadatiProtocollazione;
        }

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato)) {

            validateProtocolloProperties(metadatiProtocollazione, migration_mode);

            newStatoArchivistico = EnumStatoArchivistico.protocollato;

            metadatiProtocollazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

            return metadatiProtocollazione;
        }

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato)) {

            // ammessa solo modifica dell'oggetto
            newStatoArchivistico = oldStatoArchivistico;

            forbidProtocolChanges(oldProfile, metadatiProtocollazione);

            metadatiProtocollazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

            return metadatiProtocollazione;
        }

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.classificato) || oldStatoArchivistico.equals(EnumStatoArchivistico.fascicolato)) {

            newStatoArchivistico = oldStatoArchivistico;

            // se ha protocollo posso modificare solo oggetto
            if (hasProtocollo) {

                try {
                    forbidProtocolChanges(oldProfile, metadatiProtocollazione);
                } catch (DocerException e) {
                    throw new DocerException(e.getMessage() + " e metadati di protocollazione assegnati");
                }

                metadatiProtocollazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

                return metadatiProtocollazione;
            }

            validateProtocolloProperties(metadatiProtocollazione, migration_mode);

            metadatiProtocollazione.put(Constants.doc_stato_archivistico, String.valueOf(newStatoArchivistico.getCode()));

            return metadatiProtocollazione;

        }

        throw new DocerException("non e' ammesso invocare il metodo protocollaDocumento per un documento con " + Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " ("
                + oldStatoArchivistico.getCode() + ")");
    }

    private Map<String, String> checkAndGetMetadatiFascicolazione(DataRow<String> oldProfile, Map<String, String> metadata) throws DocerException {

        boolean oldPubblicato = Boolean.parseBoolean(oldProfile.get(Constants.doc_pubblicazione_pubblicato));

        String oldCodEnte = oldProfile.get(Constants.doc_cod_ente);
        String oldCodAOO = oldProfile.get(Constants.doc_cod_aoo);

        String newCodEnte = metadata.get(Constants.doc_cod_ente);
        String newCodAOO = metadata.get(Constants.doc_cod_aoo);

        EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

        String newClassifica = metadata.get(Constants.doc_classifica);
        String newPianoClass = metadata.get(Constants.doc_piano_class);
        String newFascicoloProgressivo = metadata.get(Constants.doc_progr_fascicolo);
        String newFascicoloAnno = metadata.get(Constants.doc_anno_fascicolo);
        String newFascicoliSecondari = metadata.get(Constants.doc_fascicoli_secondari);

        String oldClassifica = oldProfile.get(Constants.doc_classifica);
        String oldFascicoloProgressivo = oldProfile.get(Constants.doc_progr_fascicolo);
        String oldFascicoloAnno = oldProfile.get(Constants.doc_anno_fascicolo);
        String oldFascicoliSecondari = oldProfile.get(Constants.doc_fascicoli_secondari);

        Map<String, String> metadatiFascicolazione = new HashMap<String, String>();
        metadatiFascicolazione.put(Constants.doc_cod_ente, oldCodEnte);
        metadatiFascicolazione.put(Constants.doc_cod_aoo, oldCodAOO);

        // STATO_PANTAREI
        EnumStatiPantarei oldStatoPantarei = getEnumStatoPantarei(oldProfile.get(Constants.doc_stato_pantarei));

        if (newCodEnte != null && !newCodEnte.equalsIgnoreCase(oldCodEnte)) {
            throw new DocerException("Non e' permesso modificare " + Constants.doc_cod_ente);
        }

        if (newCodAOO != null && !newCodAOO.equalsIgnoreCase(oldCodAOO)) {
            throw new DocerException("Non e' permesso modificare " + Constants.doc_cod_aoo);
        }

        boolean hadFascicolo = oldFascicoloProgressivo != null && !oldFascicoloProgressivo.equals(VOID);

        boolean progrFascicoloSpecificato = metadata.containsKey(Constants.doc_progr_fascicolo);
        boolean annoFascicoloSpecificato = metadata.containsKey(Constants.doc_anno_fascicolo);
        boolean classificaSpecificata = metadata.containsKey(Constants.doc_classifica);
        boolean fascicoliSecondariSpecificati = metadata.containsKey(Constants.doc_fascicoli_secondari);

        boolean annullamentoFascicolazione = progrFascicoloSpecificato && (newFascicoloProgressivo == null || newFascicoloProgressivo.equals(VOID));

        if (annullamentoFascicolazione) {
            metadatiFascicolazione.put(Constants.doc_progr_fascicolo, VOID);
            metadatiFascicolazione.put(Constants.doc_anno_fascicolo, VOID);
            metadatiFascicolazione.put(Constants.doc_num_fascicolo, VOID);
            metadatiFascicolazione.put(Constants.doc_fascicoli_secondari, VOID);
            // DOCER-36
            metadatiFascicolazione.put(Constants.doc_piano_class, VOID);
        } else {

            if (progrFascicoloSpecificato) {

                if (!annoFascicoloSpecificato) {
                    throw new DocerException("fascicolaDocumento: per individuare il Fascicolo principale e' necessario specificare ANNO_FASCIOLO");
                }

                if (newFascicoloAnno == null || newFascicoloAnno.equals(VOID) || newFascicoloAnno.equals("0")) {
                    throw new DocerException("fascicolaDocumento: specificare un valore di ANNO_FASCICOLO diverso da null o stringa vuota o 0");
                }

                metadatiFascicolazione.put(Constants.doc_progr_fascicolo, newFascicoloProgressivo);
                metadatiFascicolazione.put(Constants.doc_anno_fascicolo, newFascicoloAnno);

            } else {

                if (annoFascicoloSpecificato) {
                    throw new DocerException("fascicolaDocumento: se non si specifica PROGR_FASCICOLO non va specificato il campo ANNO_FASCIOLO");
                }
                if (classificaSpecificata) {
                    throw new DocerException("fascicolaDocumento: se non si specifica PROGR_FASCICLO non va specificato il campo CLASSIFICA");
                }
            }

        }

        if (fascicoliSecondariSpecificati) {

            if (annullamentoFascicolazione) {
                if (newFascicoliSecondari != null && !newFascicoliSecondari.equals(VOID)) {
                    throw new DocerException("fascicolaDocumento: Annullamento della fascicolazione: il campo " + Constants.doc_fascicoli_secondari
                            + " puo' essere annullato oppure non va specificato e sara' annullato implicitamente");
                }
            } else {

                // non posso assegnare fascicoli secondari se non e' assegnato
                // un primario
                if (!progrFascicoloSpecificato && !hadFascicolo)
                    throw new DocerException("fascicolaDocumento: il campo " + Constants.doc_fascicoli_secondari + " non puo' essere assegnato a documenti che non hanno Fascicolo principale");

                metadatiFascicolazione.put(Constants.doc_fascicoli_secondari, newFascicoliSecondari);
            }

        }

        // se specifico classifica
        if (classificaSpecificata) {

            if (oldClassifica == null) {
                oldClassifica = VOID;
            }

            if (newClassifica == null) {
                newClassifica = VOID;
            }

            if (annullamentoFascicolazione && !newClassifica.equalsIgnoreCase(oldClassifica)) {
                throw new DocerException("fascicolaDocumento: annullamento della fascicolazione: non e' permesso modificare CLASSIFICA");
            }

            metadatiFascicolazione.put(Constants.doc_classifica, newClassifica);

            if (newPianoClass != null)
                metadatiFascicolazione.put(Constants.doc_piano_class, newPianoClass);

        }

        // la fascicolazione di un allegato dovrebbe essere bloccata prima ma la
        // controllo per sicurezza
        if (oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {
            throw new DocerException("compatibilita' DOCAREA: non e' permesso invocare il metodo fascicolaDocumento per un documento in STATO_PANTAREI allegato (6) ");
        }

        // metadatiFascicolazione.put(Constants.doc_stato_pantarei,oldProfile.get(Constants.doc_stato_pantarei));
        if (oldStatoPantarei.equals(EnumStatiPantarei.protocollato)) {
            metadatiFascicolazione.put(Constants.doc_stato_pantarei, String.valueOf(EnumStatiPantarei.protocollato.getCode()));
        }
        // else {
        // metadatiFascicolazione.put(Constants.doc_stato_pantarei,
        // String.valueOf(EnumStatiPantarei.fascicolato.getCode()));
        // }

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico)) {

            metadatiFascicolazione.put(Constants.doc_stato_archivistico, String.valueOf(EnumStatoArchivistico.generico.getCode()));
            return metadatiFascicolazione;
        }

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.generico_definitivo)) {

            metadatiFascicolazione.put(Constants.doc_stato_archivistico, String.valueOf(EnumStatoArchivistico.generico_definitivo.getCode()));
            return metadatiFascicolazione;
        }

        if (oldPubblicato) {
            // throw new
            // DocerException("non e' ammesso invocare il metodo fascicolaDocumento per un documento PUBBLICATO");
            return metadatiFascicolazione;
        }

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato) || oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato)
                || oldStatoArchivistico.equals(EnumStatoArchivistico.classificato) || oldStatoArchivistico.equals(EnumStatoArchivistico.fascicolato)) {

            if (newFascicoloProgressivo != null && !newFascicoloProgressivo.equals(VOID)) {
                metadatiFascicolazione.put(Constants.doc_stato_archivistico, String.valueOf(EnumStatoArchivistico.fascicolato.getCode()));
            }

            if (annullamentoFascicolazione) {
                metadatiFascicolazione.put(Constants.doc_stato_archivistico, String.valueOf(EnumStatoArchivistico.classificato.getCode()));
            }

            return metadatiFascicolazione;
        }

        throw new DocerException("non e' ammesso invocare il metodo fascicolaDocumento per un documento con " + Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " ("
                + oldStatoArchivistico.getCode() + ")");
    }

    private Map<String, String> checkAndGetMetadatiPubblicazione(DataRow<String> oldProfile, Map<String, String> metadata) throws DocerException {

        boolean oldPubblicato = Boolean.parseBoolean(oldProfile.get(Constants.doc_pubblicazione_pubblicato));

        String oldCodEnte = oldProfile.get(Constants.doc_cod_ente);
        String oldCodAOO = oldProfile.get(Constants.doc_cod_aoo);

        EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfile.get(Constants.doc_stato_archivistico));

        // String oldProtoNumero =
        // oldProfile.get(Constants.doc_protocollo_numero);
        Map<String, String> metadatiPubblicazione = new HashMap<String, String>();
        metadatiPubblicazione.put(Constants.doc_cod_ente, oldCodEnte);
        metadatiPubblicazione.put(Constants.doc_cod_aoo, oldCodAOO);

        EnumStatiPantarei oldStatoPantarei = getEnumStatoPantarei(oldProfile.get(Constants.doc_stato_pantarei));
        // la pubblicazione di un allegato dovrebbe essere bloccata prima ma la
        // controllo per sicurezza
        if (oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {
            throw new DocerException("compatibilita' DOCAREA: non e' permesso invocare il metodo pubblicaDocumento per un documento in STATO_PANTAREI allegato (6)");
        }

        metadatiPubblicazione.put(Constants.doc_stato_pantarei, oldProfile.get(Constants.doc_stato_pantarei));

        if (metadata.containsKey(Constants.doc_pubblicazione_data_fine)) {
            metadatiPubblicazione.put(Constants.doc_pubblicazione_data_fine, metadata.get(Constants.doc_pubblicazione_data_fine));
        }

        if (metadata.containsKey(Constants.doc_pubblicazione_data_inizio)) {
            metadatiPubblicazione.put(Constants.doc_pubblicazione_data_inizio, metadata.get(Constants.doc_pubblicazione_data_inizio));
        }

        if (metadata.containsKey(Constants.doc_pubblicazione_registro)) {
            metadatiPubblicazione.put(Constants.doc_pubblicazione_registro, metadata.get(Constants.doc_pubblicazione_registro));
        }

        if (metadata.containsKey(Constants.doc_pubblicazione_numero)) {
            metadatiPubblicazione.put(Constants.doc_pubblicazione_numero, metadata.get(Constants.doc_pubblicazione_numero));
        }

        if (metadata.containsKey(Constants.doc_pubblicazione_oggetto)) {
            metadatiPubblicazione.put(Constants.doc_pubblicazione_oggetto, metadata.get(Constants.doc_pubblicazione_oggetto));
        }

        if (metadata.containsKey(Constants.doc_pubblicazione_pubblicato)) {
            metadatiPubblicazione.put(Constants.doc_pubblicazione_pubblicato, metadata.get(Constants.doc_pubblicazione_pubblicato));
        }

        if (metadata.containsKey(Constants.doc_pubblicazione_annullata)) {
            metadatiPubblicazione.put(Constants.doc_pubblicazione_annullata, metadata.get(Constants.doc_pubblicazione_annullata));
        }
        if (metadata.containsKey(Constants.doc_pubblicazione_data_annullamento)) {
            metadatiPubblicazione.put(Constants.doc_pubblicazione_data_annullamento, metadata.get(Constants.doc_pubblicazione_data_annullamento));
        }
        if (metadata.containsKey(Constants.doc_pubblicazione_motivo_annullamento)) {
            metadatiPubblicazione.put(Constants.doc_pubblicazione_motivo_annullamento, metadata.get(Constants.doc_pubblicazione_motivo_annullamento));
        }

        // Se è un annullamento di pubblicazione, controllo che siano stati inviati nuovamente i vecchi dati di pubblicazione
        if (oldPubblicato || metadata.containsKey(Constants.doc_pubblicazione_annullata)) {
            forbidPubblicazioneChanges(oldProfile, metadatiPubblicazione);
            return metadatiPubblicazione;
        }

        // Se non e' pubblicato
        if (oldStatoArchivistico.equals(EnumStatoArchivistico.registrato) || oldStatoArchivistico.equals(EnumStatoArchivistico.protocollato)
                || oldStatoArchivistico.equals(EnumStatoArchivistico.classificato) || oldStatoArchivistico.equals(EnumStatoArchivistico.fascicolato)) {

            validatePubblicazioneProperties(metadatiPubblicazione);

            // Se è una ripubblicazione di una vecchia annullata
            if ( "true".equalsIgnoreCase(oldProfile.get(Constants.doc_pubblicazione_annullata)) ) {
                // Annullo l'annullamento
                metadatiPubblicazione.put(Constants.doc_pubblicazione_annullata, "false");
                // Costruisco la history
                StringBuilder history = new StringBuilder(oldProfile.get(Constants.doc_pubblicazione_history));
                String oldPubAnno = oldProfile.get(Constants.doc_pubblicazione_anno);
                String oldPubNumero = oldProfile.get(Constants.doc_pubblicazione_numero);
                String oldPubReg = oldProfile.get(Constants.doc_pubblicazione_registro);
                String oldPubDataInizio = oldProfile.get(Constants.doc_pubblicazione_data_inizio);
                String oldPubDataFine = oldProfile.get(Constants.doc_pubblicazione_data_fine);
                String oldPubOggetto = oldProfile.get(Constants.doc_pubblicazione_oggetto);
                String oldPubDataAnnull = oldProfile.get(Constants.doc_pubblicazione_data_annullamento);
                String oldPubMotivoAnnull = oldProfile.get(Constants.doc_pubblicazione_motivo_annullamento);
                if (!StringUtils.isEmpty(history.toString()))
                    history.append("\n");
                history.append(Constants.doc_pubblicazione_anno).append(":" + oldPubAnno + " ~ ")
                        .append(Constants.doc_pubblicazione_numero).append(":" + oldPubNumero + " ~ ")
                        .append(Constants.doc_pubblicazione_registro).append(":" + oldPubReg + " ~ ")
                        .append(Constants.doc_pubblicazione_data_inizio).append(":" + oldPubDataInizio + " ~ ")
                        .append(Constants.doc_pubblicazione_data_fine).append(":" + oldPubDataFine + " ~ ")
                        .append(Constants.doc_pubblicazione_oggetto).append(":" + oldPubOggetto + " ~ ")
                        .append(Constants.doc_pubblicazione_data_annullamento).append(":" + oldPubDataAnnull + " ~ ")
                        .append(Constants.doc_pubblicazione_motivo_annullamento).append(":" + oldPubMotivoAnnull);

                metadatiPubblicazione.put(Constants.doc_pubblicazione_history, history.toString());
            }

            return metadatiPubblicazione;
        }

        throw new DocerException("non e' ammesso invocare il metodo pubblicaDocumento per un documento con " + Constants.doc_stato_archivistico + " " + oldStatoArchivistico + " ("
                + oldStatoArchivistico.getCode() + ")");
    }

    private List<String> checkRelatedChainWhenRemove(List<String> relatedChain) throws DocerException {

        // sia arriva a questo controllo solo se master_stato_pantarei =
        // generico, da fascicolare, da protocollare

        List<String> listToUpdate = new ArrayList<String>();

        if (relatedChain.size() == 0) {
            return listToUpdate;
        }

        // creo i criteri di ricerca

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
        searchCriteria.put(Constants.doc_docnum, relatedChain);

        // creo le proprieta' di ritorno
        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(Constants.doc_docnum);
        returnProperties.add(Constants.doc_stato_archivistico);
        returnProperties.add(Constants.doc_stato_pantarei);
        returnProperties.add(Constants.doc_docnum_princ);

        DataTable<String> searchResults = null;
        // eseguo la ricerca ed ottengo il dt di ritorno
        try {
            searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
        } catch (Exception e) {
            throw new DocerException(e.getMessage());
        }

        if (searchResults == null || searchResults.getRows().size() == 0)
            throw new DocerException("nessun related e' stato trovato");

        Collection<DataRow<String>> rows = searchResults.getRows();
        for (DataRow<String> dr : rows) {

            String relId = dr.get(Constants.doc_docnum);

            String relDocnumPrinc = dr.get(Constants.doc_docnum_princ);
            if (!relDocnumPrinc.equals(VOID)) {
                listToUpdate.add(relId);
            }

            EnumStatoArchivistico relatedStatoArchivistico = getEnumStatoArchivistico(dr.get(Constants.doc_stato_archivistico));
            EnumStatiPantarei relatedStatoPantarei = getEnumStatoPantarei(dr.get(Constants.doc_stato_pantarei));

            if (relatedStatoArchivistico.getCode() > 1) {
                throw new DocerException("il related " + relId + " e' in STATO_ARCHIVISTICO " + relatedStatoArchivistico + " (" + relatedStatoArchivistico.getCode() + ")");
            }

            // e' possibile rimuovere o allegare solo documenti in stato
            // pantarei 1, 2, 3
            if (relatedStatoPantarei.getCode() > 3)
                throw new DocerException("compatibilita' DOCAREA: il related " + relId + " in STATO_PANTAREI " + relatedStatoPantarei + " (" + relatedStatoPantarei.getCode() + ")");

            // LockStatus checkedOutInfo;
            // try {
            // checkedOutInfo =
            // (LockStatus)provider.isCheckedOutDocument(relId);
            // }
            // catch (Exception e) {
            // throw new DocerException(e.getMessage());
            // }
            //
            // if (checkedOutInfo.getLocked())
            // throw new DocerException("il related " + relId +
            // " e' in stato di blocco exclusivo");
        }

        return listToUpdate;

    }

    // private Map<String, EnumACLRights> getACLCompound(String docId) throws
    // DocerException {
    //
    // // recupero le ACL del Master
    // try {
    // return provider.getACLDocument(docId);
    // }
    // catch (Exception e) {
    // throw new DocerException(e.getMessage());
    // }
    //
    // }

    private Map<String, EnumACLRights> getNewACLForSetACL(Map<String, EnumACLRights> accessrights) throws DocerException {

        Map<String, EnumACLRights> aclNew = new HashMap<String, EnumACLRights>();

        for (String userOrGroup : accessrights.keySet()) {

            EnumACLRights rights = accessrights.get(userOrGroup);
            if (rights.equals(EnumACLRights.deny) || rights.equals(EnumACLRights.remove) || rights.equals(EnumACLRights.undefined))
                continue;
            aclNew.put(userOrGroup, rights);
        }

        return aclNew;
    }

    // modifica del 30-10-2012
    // private void updateRelatedACL(List<String> relatedList,
    // Map<String, EnumACLRights> newACLtoSet) throws DocerException {
    //
    // if (relatedList == null || relatedList.size() < 1)
    // return;
    //
    // // attuali ACL del RELATED
    // Map<String, EnumACLRights> oldACLRelated = null;
    //
    // // nuove ACL del RELATED
    // Map<String, EnumACLRights> newACLRelated = new HashMap<String,
    // EnumACLRights>();
    //
    // for (String idRelated : relatedList) {
    //
    // oldACLRelated = provider.getACLDocument(idRelated);
    //
    // // copio le vecchie ACL
    // for (String personOrGroup : oldACLRelated.keySet()) {
    // EnumACLRights oldRight = oldACLRelated.get(personOrGroup);
    // if (oldACLRelated.equals(EnumACLRights.deny)
    // || oldACLRelated.equals(EnumACLRights.remove)
    // || oldACLRelated.equals(EnumACLRights.undefined))
    // continue;
    //
    // newACLRelated.put(personOrGroup, oldRight);
    // }
    //
    // // aggiungo le nuove ereditate dal master
    // for (String newUserOrGroup : newACLtoSet.keySet()) {
    // newACLRelated.put(newUserOrGroup,
    // newACLtoSet.get(newUserOrGroup));
    // }
    //
    // // aggiorno i diritti del related
    // try {
    //
    // // le ACL le aggiorno solo se sono state specificate
    // if (newACLRelated.size() > 0)
    // provider.setACLDocument(idRelated, newACLRelated);
    // } catch (Exception e) {
    // throw new DocerException(e.getMessage());
    // }
    // }
    // }

    private void updateRelatedACL(List<String> relatedList, Map<String, EnumACLRights> masterACL) throws DocerException {

        if (relatedList == null || relatedList.size() < 1)
            return;

        for (String idRelated : relatedList) {

            try {
                // aggiorno i diritti del related
                provider.setACLDocument(idRelated, masterACL);
            } catch (Exception e) {
                throw new DocerException(e.getMessage());
            }
        }
    }

    // // completa i profili con le descrizioni delle anagrafiche
    // private Map<String, String> formatProfile(DataRow<String> profile) {
    //
    // Map<String, String> completeProfile = new HashMap<String, String>();
    //
    // String typeId = profile.get(Constants.doc_type_id);
    // typeId = typeId.toUpperCase();
    //
    // String codEnte = profile.get(Constants.doc_cod_ente);
    // String codAOO = profile.get(Constants.doc_cod_aoo);
    // String classifica = profile.get(Constants.doc_classifica);
    // String progrFascicolo = profile.get(Constants.doc_progr_fascicolo);
    // String annoFascicolo = profile.get(Constants.doc_anno_fascicolo);
    //
    // DocumentType docType = DOCUMENT_TYPES.get(typeId);
    // if (docType == null)
    // return completeProfile;
    //
    // completeProfile.put(Constants.doc_type_id, typeId);
    // completeProfile.put(Constants.doc_type_id_des, docType.getDescription());
    //
    // for (String propName : profile) {
    //
    // if (propName.equals(Constants.doc_type_id))
    // continue;
    //
    // if (propName.equals(Constants.doc_type_id_des))
    // continue;
    //
    // FieldDescriptor fd = FIELDS.get(propName.toUpperCase());
    // if (fd == null) {
    // continue;
    // }
    //
    // String propValue = profile.get(propName);
    //
    // if (propValue == null)
    // propValue = VOID;
    //
    // // // e' definito per il type_id
    // // if (docType.getFieldDescriptor(codEnte, codAOO,
    // propName.toUpperCase()) != null) {
    // // // if(propValue==null)
    // // // completeProfile.put(propName, VOID);
    // // // else
    // // completeProfile.put(propName, propValue);
    // // }
    //
    // // se non e' definito per il type_id non la aggiungo
    // if (docType.getFieldDescriptor(codEnte, codAOO, propName.toUpperCase())
    // == null) {
    // continue;
    // }
    // completeProfile.put(propName, propValue);
    //
    //
    //
    // if (propName.equalsIgnoreCase(Constants.doc_cod_ente)) {
    // if (profile.get(Constants.ente_des_ente) == null)
    // completeProfile.put(Constants.ente_des_ente, VOID);
    //
    // continue;
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_cod_aoo)) {
    // if (profile.get(Constants.aoo_des_aoo) == null)
    // completeProfile.put(Constants.aoo_des_aoo, VOID);
    // continue;
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_classifica)) {
    //
    // if (profile.get(Constants.titolario_des_titolario) == null)
    // completeProfile.put(Constants.titolario_des_titolario, VOID);
    //
    // continue;
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_progr_fascicolo)) {
    // if (profile.get(Constants.fascicolo_des_fascicolo) == null)
    // completeProfile.put(Constants.fascicolo_des_fascicolo, VOID);
    // continue;
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_anno_fascicolo)) {
    // if (profile.get(Constants.fascicolo_des_fascicolo) == null)
    // completeProfile.put(Constants.fascicolo_des_fascicolo, VOID);
    // continue;
    // }
    //
    // if (propName.equalsIgnoreCase(Constants.doc_type_id)) {
    // continue;
    // }
    //
    // if (fd.pointToAnagrafica() && codEnte != null && codAOO != null) {
    //
    // try {
    // AnagraficaType at =
    // ANAGRAFICHE_TYPES.get(fd.getAnagraficaTypeId().toUpperCase());
    //
    // String des = profile.get(at.getDescrizionePropName());
    //
    // String codAnagr = profile.get(propName);
    //
    // // se la descrizione non proviene dalla ricerca allora la
    // // ricavo
    // if ((des == null || des.equals(VOID)) && codAnagr != null &&
    // !codAnagr.equals(VOID)) {
    // Map<String, String> cusId = new HashMap<String, String>();
    // cusId.put(Constants.custom_cod_ente, codEnte);
    // cusId.put(Constants.custom_cod_aoo, codAOO);
    // cusId.put(at.getCodicePropName(), codAnagr);
    //
    // Map<String, String> p = getAnagraficaProfile(at.getTypeId(), cusId);
    // if (p != null) {
    // des = p.get(at.getDescrizionePropName());
    // }
    //
    // if (des == null) {
    // des = VOID;
    // }
    //
    // }
    //
    // completeProfile.put(at.getDescrizionePropName(), des);
    //
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // continue;
    // }
    //
    // } // fine ciclo
    //
    // // recupero descrizione ente solo se non viene gia' dal provider
    // if (codEnte != null && !codEnte.equals(VOID)) {
    //
    // String desEnte = profile.get(Constants.ente_des_ente);
    // completeProfile.put(Constants.ente_des_ente, desEnte);
    //
    // if (desEnte == null || desEnte.equals(VOID)) {
    //
    // desEnte = VOID;
    // String enteKey = codEnte.toUpperCase();
    // if (enteAooDescriptionCaching.containsKey(enteKey)) {
    //
    // desEnte = enteAooDescriptionCaching.get(enteKey);
    // }
    // else {
    // try {
    // Map<String, String> idCrit = new HashMap<String, String>();
    // idCrit.put(Constants.ente_cod_ente, codEnte);
    // Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id,
    // idCrit);
    // if (p != null) {
    // desEnte = p.get(Constants.ente_des_ente);
    // enteAooDescriptionCaching.put(enteKey, desEnte);
    // }
    //
    // }
    // catch (Exception e) {
    // e.printStackTrace(); // NON SOLLEVO ECCEZIONE
    // }
    // }
    //
    // if (desEnte == null)
    // desEnte = VOID;
    //
    // completeProfile.put(Constants.ente_des_ente, desEnte);
    // }
    // }
    //
    // // recupero descrizione aoo solo se non viene gia' dal provider
    // if (codEnte != null && !codEnte.equals(VOID) && codAOO != null &&
    // !codAOO.equals(VOID)) {
    //
    // String desAOO = profile.get(Constants.aoo_des_aoo);
    //
    // completeProfile.put(Constants.aoo_des_aoo, desAOO);
    //
    // if (desAOO == null || desAOO.equals(VOID)) {
    //
    // desAOO = VOID;
    // String aooKey = codEnte.toUpperCase() + SLASH + codAOO.toUpperCase();
    //
    // if (enteAooDescriptionCaching.containsKey(aooKey))
    // desAOO = enteAooDescriptionCaching.get(aooKey);
    // else {
    // try {
    //
    // Map<String, String> idCrit = new HashMap<String, String>();
    // idCrit.put(Constants.aoo_cod_ente, codEnte);
    // idCrit.put(Constants.aoo_cod_aoo, codAOO);
    //
    // Map<String, String> p = getAnagraficaProfile(Constants.aoo_type_id,
    // idCrit);
    // if (p != null) {
    // desAOO = p.get(Constants.aoo_des_aoo);
    // enteAooDescriptionCaching.put(aooKey, desAOO);
    // }
    //
    // }
    // catch (Exception e) {
    // e.printStackTrace(); // NON SOLLEVO ECCEZIONE
    // }
    // }
    //
    // if (desAOO == null)
    // desAOO = VOID;
    //
    // completeProfile.put(Constants.aoo_des_aoo, desAOO);
    //
    // }
    // }
    //
    // // recupero descrizione titolario solo se non viene gia' dal provider
    // if (codEnte != null && !codEnte.equals(VOID) && codAOO != null &&
    // !codAOO.equals(VOID) && classifica != null && !classifica.equals(VOID)) {
    // try {
    //
    // String desTitolario = profile.get(Constants.titolario_des_titolario);
    // completeProfile.put(Constants.titolario_des_titolario, desTitolario);
    //
    // if (desTitolario == null || desTitolario.equals(VOID)) {
    //
    // Map<String, String> idCrit = new HashMap<String, String>();
    // idCrit.put(Constants.titolario_cod_ente, codEnte);
    // idCrit.put(Constants.titolario_cod_aoo, codAOO);
    // idCrit.put(Constants.titolario_classifica, classifica);
    //
    // Map<String, String> p = getAnagraficaProfile(Constants.titolario_type_id,
    // idCrit);
    // if (p != null) {
    //
    // desTitolario = p.get(Constants.titolario_des_titolario);
    // }
    //
    // if (desTitolario == null)
    // desTitolario = VOID;
    //
    // completeProfile.put(Constants.titolario_des_titolario, desTitolario);
    //
    // }
    //
    // }
    // catch (Exception e) {
    // e.printStackTrace(); // NON SOLLEVO ECCEZIONE
    // }
    // }
    //
    // // recupero descrizione fascicolo solo se non viene gia' dal provider
    // if (codEnte != null && !codEnte.equals(VOID) && codAOO != null &&
    // !codAOO.equals(VOID) && classifica != null && !classifica.equals(VOID) &&
    // annoFascicolo != null && !annoFascicolo.equals(VOID) &&
    // !annoFascicolo.equals("0") && progrFascicolo != null &&
    // !progrFascicolo.equals(VOID)) {
    // try {
    //
    // String desFascicolo = profile.get(Constants.fascicolo_des_fascicolo);
    // completeProfile.put(Constants.fascicolo_des_fascicolo, desFascicolo);
    //
    // if (desFascicolo == null || desFascicolo.equals(VOID)) {
    //
    // Map<String, String> idCrit = new HashMap<String, String>();
    // idCrit.put(Constants.fascicolo_cod_ente, codEnte);
    // idCrit.put(Constants.fascicolo_cod_aoo, codAOO);
    // idCrit.put(Constants.fascicolo_classifica, classifica);
    // idCrit.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);
    // idCrit.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);
    //
    // Map<String, String> p = getAnagraficaProfile(Constants.fascicolo_type_id,
    // idCrit);
    //
    // if (p != null) {
    // desFascicolo = p.get(Constants.fascicolo_des_fascicolo);
    // }
    //
    // if (desFascicolo == null)
    // desFascicolo = VOID;
    //
    // completeProfile.put(Constants.fascicolo_des_fascicolo, desFascicolo);
    // }
    //
    // }
    // catch (Exception e) {
    // e.printStackTrace(); // NON SOLLEVO ECCEZIONE
    // }
    // }
    //
    // return completeProfile;
    // }

    // completa i profili con le descrizioni delle anagrafiche
    private Map<String, String> formatProfile(DataRow<String> profile) throws DocerException {

        Map<String, String> completeProfile = new HashMap<String, String>();

        String typeId = profile.get(Constants.doc_type_id);
        typeId = typeId.toUpperCase();

        String codEnte = profile.get(Constants.doc_cod_ente);
        String codAOO = profile.get(Constants.doc_cod_aoo);
        String classifica = profile.get(Constants.doc_classifica);
        // DOCER-36
        String pianoClass = profile.get(Constants.doc_piano_class);

        String progrFascicolo = profile.get(Constants.doc_progr_fascicolo);
        String annoFascicolo = profile.get(Constants.doc_anno_fascicolo);

        DocumentType documentType = BL_CONFIGURATIONS.getConfig(entetoken).getDocumentTypesMapping().get(typeId, codEnte, codAOO);
        if (documentType == null) {
            return completeProfile;
        }

        // completeProfile.put(Constants.doc_type_id, typeId);
        // completeProfile.put(Constants.doc_type_id_des,
        // docType.getDescription());

        Map<String, String> cusId = new HashMap<String, String>();

        for (String propName : profile) {

            FieldDescriptor fd = documentType.getFieldDescriptor(propName);
            if (fd == null) {
                continue;
            }

            String propValue = profile.get(propName);

            if (propValue == null) {
                propValue = VOID;
            }

            if (propName.equals(Constants.doc_type_id)) {
                completeProfile.put(Constants.doc_type_id, typeId);
                completeProfile.put(Constants.doc_type_id_des, documentType.getDescription());
                continue;
            }

            if (propName.equals(Constants.doc_type_id_des)) {
                continue;
            }

            completeProfile.put(propName, propValue);

            if (propName.equalsIgnoreCase(Constants.doc_cod_ente)) {
                if (profile.get(Constants.ente_des_ente) == null) {
                    completeProfile.put(Constants.ente_des_ente, VOID);
                }
                continue;
            }

            if (propName.equalsIgnoreCase(Constants.doc_cod_aoo)) {
                if (profile.get(Constants.aoo_des_aoo) == null) {
                    completeProfile.put(Constants.aoo_des_aoo, VOID);
                }
                continue;
            }

            if (propName.equalsIgnoreCase(Constants.doc_classifica)) {

                if (profile.get(Constants.titolario_des_titolario) == null) {
                    completeProfile.put(Constants.titolario_des_titolario, VOID);
                }

                continue;
            }

            if (propName.equalsIgnoreCase(Constants.doc_progr_fascicolo)) {
                if (profile.get(Constants.fascicolo_des_fascicolo) == null) {
                    completeProfile.put(Constants.fascicolo_des_fascicolo, VOID);
                }
                continue;
            }

            if (propName.equalsIgnoreCase(Constants.doc_anno_fascicolo)) {
                if (profile.get(Constants.fascicolo_des_fascicolo) == null) {
                    completeProfile.put(Constants.fascicolo_des_fascicolo, VOID);
                }
                continue;
            }

            if (fd.pointToAnagrafica() && codEnte != null && codAOO != null) {

                try {
                    AnagraficaType at = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(fd.getAnagraficaTypeId().toUpperCase(), codEnte, codAOO);

                    if (at == null) {
                        continue;
                    }

                    String desAnagrafica = profile.get(at.getDescrizionePropName());

                    if (desAnagrafica == null) {
                        desAnagrafica = "";
                    }

                    String codAnagrValue = profile.get(propName);

                    String[] codAnagArray = null;
                    if (fd.isMultivalue()) {
                        codAnagArray = codAnagrValue.split(" *; *");
                    } else {
                        codAnagArray = new String[]{codAnagrValue};
                    }

                    if (StringUtils.isEmpty(desAnagrafica)) {

                        for (String codAnagr : codAnagArray) {
                            // se la descrizione non proviene dalla ricerca
                            // allora la
                            // ricavo
                            if (StringUtils.isNotEmpty(codAnagr)) {

                                cusId.clear();
                                cusId.put(Constants.custom_cod_ente, codEnte);
                                cusId.put(Constants.custom_cod_aoo, codAOO);
                                cusId.put(at.getCodicePropName(), codAnagr);

                                Map<String, String> p = getAnagraficaProfile(at.getTypeId(), cusId);
                                if (p != null) {
                                    desAnagrafica += p.get(at.getDescrizionePropName()) + ";";
                                }

                            }
                        }

                        desAnagrafica = desAnagrafica.replaceAll(";$", "");

                    }

                    if (desAnagrafica == null) {
                        desAnagrafica = VOID;
                    }
                    completeProfile.put(at.getDescrizionePropName(), desAnagrafica);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                continue;
            }

        } // fine ciclo

        // recupero descrizione ente solo se non viene gia' dal provider
        if (completeProfile.containsKey(Constants.ente_cod_ente) && codEnte != null && !codEnte.equals(VOID)) {

            String desEnte = profile.get(Constants.ente_des_ente);
            completeProfile.put(Constants.ente_des_ente, desEnte);

            if (desEnte == null || desEnte.equals(VOID)) {

                desEnte = VOID;
                String enteKey = codEnte.toUpperCase();
                if (enteAooDescriptionCaching.containsKey(enteKey)) {

                    desEnte = enteAooDescriptionCaching.get(enteKey);
                } else {
                    try {
                        Map<String, String> idCrit = new HashMap<String, String>();
                        idCrit.put(Constants.ente_cod_ente, codEnte);
                        Map<String, String> p = getAnagraficaProfile(Constants.ente_type_id, idCrit);
                        if (p != null) {
                            desEnte = p.get(Constants.ente_des_ente);
                            enteAooDescriptionCaching.put(enteKey, desEnte);
                        }

                    } catch (Exception e) {
                        e.printStackTrace(); // NON SOLLEVO ECCEZIONE
                    }
                }

                if (desEnte == null)
                    desEnte = VOID;

                completeProfile.put(Constants.ente_des_ente, desEnte);
            }
        }

        // recupero descrizione aoo solo se non viene gia' dal provider
        if (completeProfile.containsKey(Constants.aoo_cod_aoo) && codEnte != null && !codEnte.equals(VOID) && codAOO != null && !codAOO.equals(VOID)) {

            String desAOO = profile.get(Constants.aoo_des_aoo);

            completeProfile.put(Constants.aoo_des_aoo, desAOO);

            if (desAOO == null || desAOO.equals(VOID)) {

                desAOO = VOID;
                String aooKey = codEnte.toUpperCase() + SLASH + codAOO.toUpperCase();

                if (enteAooDescriptionCaching.containsKey(aooKey))
                    desAOO = enteAooDescriptionCaching.get(aooKey);
                else {
                    try {

                        Map<String, String> idCrit = new HashMap<String, String>();
                        idCrit.put(Constants.aoo_cod_ente, codEnte);
                        idCrit.put(Constants.aoo_cod_aoo, codAOO);

                        Map<String, String> p = getAnagraficaProfile(Constants.aoo_type_id, idCrit);
                        if (p != null) {
                            desAOO = p.get(Constants.aoo_des_aoo);
                            enteAooDescriptionCaching.put(aooKey, desAOO);
                        }

                    } catch (Exception e) {
                        e.printStackTrace(); // NON SOLLEVO ECCEZIONE
                    }
                }

                if (desAOO == null)
                    desAOO = VOID;

                completeProfile.put(Constants.aoo_des_aoo, desAOO);

            }
        }

        // recupero descrizione titolario solo se non viene gia' dal provider
        if (completeProfile.containsKey(Constants.titolario_classifica) && codEnte != null && !codEnte.equals(VOID) && codAOO != null && !codAOO.equals(VOID) && classifica != null
                && !classifica.equals(VOID)) {
            try {

                String desTitolario = profile.get(Constants.titolario_des_titolario);
                completeProfile.put(Constants.titolario_des_titolario, desTitolario);

                if (desTitolario == null || desTitolario.equals(VOID)) {

                    Map<String, String> idCrit = new HashMap<String, String>();
                    idCrit.put(Constants.titolario_cod_ente, codEnte);
                    idCrit.put(Constants.titolario_cod_aoo, codAOO);
                    idCrit.put(Constants.titolario_classifica, classifica);
                    idCrit.put(Constants.titolario_piano_class, pianoClass);

                    Map<String, String> p = getAnagraficaProfile(Constants.titolario_type_id, idCrit);
                    if (p != null) {

                        desTitolario = p.get(Constants.titolario_des_titolario);
                    }

                    if (desTitolario == null)
                        desTitolario = VOID;

                    completeProfile.put(Constants.titolario_des_titolario, desTitolario);

                }

            } catch (Exception e) {
                e.printStackTrace(); // NON SOLLEVO ECCEZIONE
            }
        }

        // recupero descrizione fascicolo solo se non viene gia' dal provider
        if (completeProfile.containsKey(Constants.fascicolo_progr_fascicolo) && completeProfile.containsKey(Constants.fascicolo_anno_fascicolo) && codEnte != null && !codEnte.equals(VOID)
                && codAOO != null && !codAOO.equals(VOID) && classifica != null && !classifica.equals(VOID) && annoFascicolo != null && !annoFascicolo.equals(VOID) && !annoFascicolo.equals("0")
                && progrFascicolo != null && !progrFascicolo.equals(VOID)) {
            try {

                String desFascicolo = profile.get(Constants.fascicolo_des_fascicolo);
                completeProfile.put(Constants.fascicolo_des_fascicolo, desFascicolo);

                if (desFascicolo == null || desFascicolo.equals(VOID)) {

                    Map<String, String> idCrit = new HashMap<String, String>();
                    idCrit.put(Constants.fascicolo_cod_ente, codEnte);
                    idCrit.put(Constants.fascicolo_cod_aoo, codAOO);
                    idCrit.put(Constants.fascicolo_classifica, classifica);
                    idCrit.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);
                    idCrit.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);

                    Map<String, String> p = getAnagraficaProfile(Constants.fascicolo_type_id, idCrit);

                    if (p != null) {
                        desFascicolo = p.get(Constants.fascicolo_des_fascicolo);
                    }

                    if (desFascicolo == null)
                        desFascicolo = VOID;

                    completeProfile.put(Constants.fascicolo_des_fascicolo, desFascicolo);
                }

            } catch (Exception e) {
                e.printStackTrace(); // NON SOLLEVO ECCEZIONE
            }
        }

        return completeProfile;
    }

    private void clearCache() {
        enteAooDescriptionCaching.clear();
    }

    // private void checkAnagraficheCustom(String codEnte, String codAOO,
    // Map<String, String> metadata) throws DocerException {
    //
    // for (String propName : metadata.keySet()) {
    //
    // String propValue = metadata.get(propName);
    //
    // if (propValue == null || propValue.equals(VOID))
    // continue;
    //
    // FieldDescriptor fd = FIELDS.get(propName.toUpperCase());
    //
    // if (fd.pointToAnagrafica() && !fd.isMultivalue()) {
    //
    // if (codEnte == null || codEnte.equals(VOID)) {
    // throw new
    // DocerException("COD_ENTE richiesto per individuare Anagrafica custom " +
    // fd.getAnagraficaTypeId() + " puntata dalla proprieta' " + propName);
    // }
    //
    // if (codAOO == null || codAOO.equals(VOID)) {
    // throw new
    // DocerException("COD_AOO richiesto per individuare Anagrafica custom " +
    // fd.getAnagraficaTypeId() + " puntata dalla proprieta' " + propName);
    // }
    //
    // AnagraficaType at =
    // ANAGRAFICHE_TYPES.get(fd.getAnagraficaTypeId().toUpperCase());
    //
    // Map<String, String> cusId = new HashMap<String, String>();
    // cusId.put(Constants.custom_cod_ente, codEnte);
    // cusId.put(Constants.custom_cod_aoo, codAOO);
    // cusId.put(at.getCodicePropName(), propValue);
    //
    // Map<String, String> p = getAnagraficaProfile(at.getTypeId(), cusId);
    //
    // if (p == null)
    // throw new DocerException("Anagrafica custom " + metadata.get(propName) +
    // " di tipo " + fd.getAnagraficaTypeId() + " non trovata");
    //
    // continue;
    // }
    // }
    //
    // }

    private boolean checkComponentType(String tipoComponente, DocumentType docType) {
        // Se il tipo componente non viene passato oppure non è definito componentTypes per il type, non effettuo il check
        if (StringUtils.isEmpty(tipoComponente) || StringUtils.isEmpty(docType.getComponentTypes()))
            return true;

        String[] typesAllowed = docType.getComponentTypes().split(" ");
        for (String typeAllowed : typesAllowed) {
            if (tipoComponente.equalsIgnoreCase(typeAllowed))
                return true;
        }

        return false;
    }

    private void checkAnagraficheCustom(String codEnte, String codAOO, DocumentType docType, Map<String, String> metadata) throws DocerException {

        Map<String, List<String>> searchCriteria = null;

        List<String> criteria = null;

        String[] mv = null;

        List<String> found = null;

        String pointedPropName = VOID;
        String anagraficaTypeId = VOID;

        FieldDescriptor fd = null;
        List<String> returnProperties = null;

        List<String> enteCriteria = Arrays.asList(new String[]{codEnte});
        List<String> aooCriteria = Arrays.asList(new String[]{codAOO});

        // controllo le anagrafiche custom
        for (String propName : metadata.keySet()) {

            String propValue = metadata.get(propName);

            if (StringUtils.isEmpty(propValue)) {
                continue; // non devo controllare nulla
            }

            // i fascicoli secondari sono ammessi solo da fascicolaDocumento
            if (propName.equalsIgnoreCase(Constants.doc_fascicoli_secondari))
                continue;

            fd = docType.getFieldDescriptor(propName);

            if (fd == null) {
                throw new DocerException("il campo " + propName + " non appartiene al tipo " + docType.getTypeId());
            }

            // se non e' una proprieta' multivalue...non controllo
            // if (!fd.isMultivalue())
            // continue;

            // se il multivalue non punta ad una anagrafica...non controllo
            if (!fd.pointToAnagrafica()) {
                continue;
            }

            // tipo di anagrafica puntata
            anagraficaTypeId = fd.getAnagraficaTypeId();

            AnagraficaType at = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(anagraficaTypeId, codEnte, codAOO);

            if (at == null) { // non e' una anagrafica
                continue;
            }

            // proprieta' con ruolo di codice che e' puntata dal mv
            pointedPropName = at.getCodicePropName();

            if (pointedPropName == null) {
                continue;
            }

            if (searchCriteria == null) {
                searchCriteria = new HashMap<String, List<String>>();
                criteria = new ArrayList<String>();
            } else {
                searchCriteria.clear();
                criteria.clear();
            }

            searchCriteria.put(Constants.doc_cod_ente, enteCriteria);
            searchCriteria.put(Constants.doc_cod_aoo, aooCriteria);

            // propValue = propValue.toUpperCase();

            String singleOrMulti = "Singlevalue";
            if (fd.isMultivalue()) {
                mv = propValue.split(" *; *");
                singleOrMulti = "Multivalue";
            } else {
                mv = new String[]{propValue.trim()};
            }

            for (String singleValue : mv) {
                String idAnagr = singleValue.trim();
                if (idAnagr.equals(VOID) || criteria.contains(idAnagr)) {
                    continue;
                }
                criteria.add(idAnagr);
            }

            searchCriteria.put(pointedPropName.toUpperCase(), criteria);

            if (criteria.size() > 0) {

                if (StringUtils.isEmpty(codEnte)) {
                    throw new DocerException("COD_ENTE obbligatorio se si specifica il campo " + propName + " dell'anagrafica custom " + anagraficaTypeId);
                }

                if (StringUtils.isEmpty(codAOO)) {
                    throw new DocerException("COD_AOO obbligatorio se si specifica il campo " + propName + " dell'anagrafica custom " + anagraficaTypeId);
                }

                if (returnProperties == null) {
                    returnProperties = new ArrayList<String>();
                } else {
                    returnProperties.clear();
                }

                returnProperties.add(pointedPropName);

                List<Map<String, String>> r = provider.searchAnagrafiche(anagraficaTypeId, searchCriteria, returnProperties);
                if (r == null || r.size() < 1)
                    throw new DocerException("Anagrafiche " + Arrays.toString(criteria.toArray()) + " specificate in " + propName + " (" + singleOrMulti + " di tipo " + anagraficaTypeId + " verso "
                            + pointedPropName + ") non trovate");

                if (found == null) {
                    found = new ArrayList<String>();
                } else {
                    found.clear();
                }

                // popolo la lista dei trovati
                for (Map<String, String> searchItem : r) {

                    for (String key : searchItem.keySet()) {
                        if (key.equalsIgnoreCase(pointedPropName)) {
                            found.add(searchItem.get(key));
                            break;
                        }
                    }
                }

                if (criteria.size() > found.size()) { // ho trovato meno
                    // risultati
                    for (String f : found) {
                        criteria.remove(f);
                    }
                    throw new DocerException("Anagrafiche " + Arrays.toString(criteria.toArray()) + " specificate in " + propName + " (" + singleOrMulti + " di tipo " + anagraficaTypeId + " verso "
                            + pointedPropName + ") non trovate");
                }

                if (criteria.size() < found.size()) { // ho trovato piu'
                    // risultati
                    for (String c : criteria) {
                        found.remove(c);
                    }
                    throw new DocerException("Anagrafiche non univoche: " + Arrays.toString(found.toArray()) + "; specificate in " + propName + " (" + singleOrMulti + " di tipo " + anagraficaTypeId
                            + " verso " + pointedPropName + ")");
                }

            }

        }

    }

    // private void checkFascicoliSecondari(Map<String, String> metadata) throws
    // DocerException {
    //
    // boolean fascicoliSecondariSpecificati =
    // metadata.containsKey(Constants.doc_fascicoli_secondari);
    //
    // if (!fascicoliSecondariSpecificati)
    // return;
    //
    // // tratto i fascicoli secondari
    // String fascicoliSecondari =
    // metadata.get(Constants.doc_fascicoli_secondari);
    //
    // if (fascicoliSecondari == null || fascicoliSecondari.equals(VOID)) {
    // return; // annullamento
    // }
    // // tutti i multivalue sono verso anagrafiche e sotto le aoo
    // String codEnte = metadata.get(Constants.ente_cod_ente);
    // String codAOO = metadata.get(Constants.aoo_cod_aoo);
    //
    // if (codEnte == null || codEnte.equals(VOID))
    // throw new
    // DocerException("Ricerca Fascicoli secondari: COD_ENTE obbligatorio");
    //
    // if (codAOO == null || codAOO.equals(VOID))
    // throw new
    // DocerException("Ricerca Fascicoli secondari: COD_AOO obbligatorio");
    //
    // Map<String, List<String>> searchCriteria = null;
    //
    // List<String> enteCriteria = null;
    // List<String> aooCriteria = null;
    // List<String> classificaCriteria = null;
    // List<String> annoFascicoloCriteria = null;
    // List<String> progrFascicoloCriteria = null;
    //
    // String[] fascicoli = null;
    //
    // fascicoli = fascicoliSecondari.split(";");
    //
    // for (String fascicolo : fascicoli) {
    //
    // fascicolo = fascicolo.trim();
    //
    // if (fascicolo.equals(VOID))
    // continue;
    //
    // if (searchCriteria == null) {
    // searchCriteria = new HashMap<String, List<String>>();
    // enteCriteria = new ArrayList<String>();
    // aooCriteria = new ArrayList<String>();
    //
    // enteCriteria.add(codEnte);
    // aooCriteria.add(codAOO);
    //
    // classificaCriteria = new ArrayList<String>();
    // annoFascicoloCriteria = new ArrayList<String>();
    // progrFascicoloCriteria = new ArrayList<String>();
    // }
    // else {
    // searchCriteria.clear();
    // classificaCriteria.clear();
    // annoFascicoloCriteria.clear();
    // progrFascicoloCriteria.clear();
    // }
    //
    // Matcher m = patternFascicoliSec.matcher(fascicolo);
    //
    // if (m.matches()) {
    //
    // classificaCriteria.add(m.group(classificaPosition));
    // annoFascicoloCriteria.add(m.group(annoFascicoloPosition));
    // progrFascicoloCriteria.add(m.group(progrFascicoloPosition));
    //
    // searchCriteria.put(Constants.fascicolo_cod_ente, enteCriteria);
    // searchCriteria.put(Constants.fascicolo_cod_aoo, aooCriteria);
    // searchCriteria.put(Constants.fascicolo_classifica, classificaCriteria);
    // searchCriteria.put(Constants.fascicolo_anno_fascicolo,
    // annoFascicoloCriteria);
    // searchCriteria.put(Constants.fascicolo_progr_fascicolo,
    // progrFascicoloCriteria);
    //
    // List<Map<String, String>> r =
    // provider.searchAnagrafiche(Constants.fascicolo_type_id, searchCriteria,
    // HITLISTS.get("HITLIST_FASCICOLO"));
    // if (r == null || r.size() < 1)
    // throw new DocerException("FASCICOLI_SEC: fascicolo " + fascicolo +
    // " non trovato");
    // }
    //
    // }
    //
    // }

    private Map<String, String> getAnagraficaProfile(String typeid, Map<String, String> id) throws DocerException {

        typeid = String.valueOf(typeid).toUpperCase();

        String codEnte = null;
        String codAoo = null;

        codEnte = id.get(Constants.ente_cod_ente);

        // codEnte e' sempre obbligatorio
        if (StringUtils.isEmpty(codEnte))
            throw new DocerException("COD_ENTE obbligatorio");

        // se non e' un Ente allora codAoo e' sempre obbligatorio
        if (!typeid.equals(Constants.ente_type_id)) {

            codAoo = id.get(Constants.aoo_cod_aoo);

            // codAoo e' sempre obbligatorio se non e'un Ente
            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("COD_AOO obbligatorio");
        }

        AnagraficaType anagraficaType = BL_CONFIGURATIONS.getConfig(entetoken).getAnagraficaTypesMapping().get(typeid, codEnte, codAoo);
        if (anagraficaType == null)
            throw new DocerException("Anagrafica non definita in configurazione Business Logic: " + typeid + " per COD_ENTE: " + codEnte + " e COD_AOO: " + codAoo);

        FieldDescriptor fd = null;

        Map<String, List<String>> idCriteria = new HashMap<String, List<String>>();

        // assegno criteria di ricerca
        for (String fieldName : id.keySet()) {

            fd = anagraficaType.getFieldDescriptor(fieldName);

            if (fd == null) {
                if (typeid.equals(Constants.ente_type_id)) {
                    throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica " + typeid + " per COD_ENTE " + codEnte);
                }
                throw new DocerException(fieldName + " non e' definito tra i metadati dell'anagrafica " + typeid + " per COD_ENTE " + codEnte + " e COD_AOO " + codAoo);
            }

            boolean isIdCriteria = false;

            if (typeid.equalsIgnoreCase("ENTE")) {
                if (fd.getPropName().equalsIgnoreCase(Constants.ente_cod_ente)) {
                    isIdCriteria = true;
                }
            } else if (typeid.equalsIgnoreCase("AOO")) {
                if (fd.getPropName().equalsIgnoreCase(Constants.aoo_cod_ente)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.aoo_cod_aoo)) {
                    isIdCriteria = true;
                }
            } else if (typeid.equalsIgnoreCase("TITOLARIO")) {
                if (fd.getPropName().equalsIgnoreCase(Constants.titolario_cod_ente)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.titolario_cod_aoo)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.titolario_classifica)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.titolario_cod_titolario)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.titolario_piano_class)) {
                    isIdCriteria = true;
                }
            } else if (typeid.equalsIgnoreCase("FASCICOLO")) {
                if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_cod_ente)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_cod_aoo)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_classifica)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_progr_fascicolo)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_num_fascicolo)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_anno_fascicolo)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.fascicolo_piano_class)) {
                    isIdCriteria = true;
                }
            } else {
                if (fd.getPropName().equalsIgnoreCase(Constants.custom_cod_ente)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(Constants.custom_cod_aoo)) {
                    isIdCriteria = true;
                } else if (fd.getPropName().equalsIgnoreCase(anagraficaType.getCodicePropName())) {
                    isIdCriteria = true;
                }
            }

            if (id.containsKey(Constants.custom_enabled) && fd.getPropName().equalsIgnoreCase(Constants.custom_enabled))
                isIdCriteria = true;

            if (isIdCriteria) {
                List<String> crit = new ArrayList<String>();
                crit.add(id.get(fieldName));
                idCriteria.put(fd.getPropName(), crit);
            }

        }

        List<Map<String, String>> result = provider.searchAnagrafiche(typeid, idCriteria, anagraficaType.getFieldsNames());

        if (result.size() == 0)
            return null;

        if (result.size() != 1)
            throw new DocerException("Trovati " + result.size() + " risultati");

        Map<String, String> resultProfile = result.get(0);

        Map<String, String> profile = new HashMap<String, String>();

        // assegno le extrainfo
        for (String key : resultProfile.keySet()) {

            String value = resultProfile.get(key);

            key = key.toUpperCase();

            profile.put(key, value);
        }

        return profile;
    }

    private IFolderInfo getFolderProfile(Map<String, String> id) throws DocerException {

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

        // assegno criteria di ricerca
        for (String fieldName : id.keySet()) {

            if (fieldName == null)
                continue;
            List<String> criteria = new ArrayList<String>();
            criteria.add(id.get(fieldName));

            searchCriteria.put(fieldName.toUpperCase(), criteria);
        }

        searchCriteria.put(Constants.custom_enabled, Collections.singletonList("*"));

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_ente);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_aoo);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_des_folder);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_name);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
        returnProperties.add(Constants.custom_enabled);
//        if (CIFS) {
//            returnProperties.add("PATH");
//        }

        DataTable<String> dtResult = null;

        try {
            dtResult = provider.searchFolders(searchCriteria, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
        } catch (Exception e) {
            throw new DocerException(e.getMessage());
        }

        if (dtResult.getRows().size() == 0)
            return null;

        if (dtResult.getRows().size() > 1)
            throw new DocerException("Trovate " + dtResult.getRows().size() + " Folder");

        IFolderInfo folderProfile = new FolderInfo();
        DataRow<String> rowProfile = dtResult.getRow(0);

        for (String columnName : dtResult.getColumnNames()) {

            if (columnName.equals(it.kdm.docer.sdk.Constants.folder_folder_id)) {
                folderProfile.setFolderId(rowProfile.get(columnName));
                continue;
            }
            if (columnName.equals(Constants.custom_enabled)) {
                folderProfile.setEnabled(getEnumBoolean(rowProfile.get(columnName)));
                continue;
            }
            if (columnName.equals(it.kdm.docer.sdk.Constants.folder_cod_ente)) {
                folderProfile.setCodiceEnte(rowProfile.get(columnName));
                continue;
            }
            if (columnName.equals(it.kdm.docer.sdk.Constants.folder_cod_aoo)) {
                folderProfile.setCodiceAOO(rowProfile.get(columnName));
                continue;
            }
            if (columnName.equals(it.kdm.docer.sdk.Constants.folder_parent_folder_id)) {
                folderProfile.setParentFolderId(rowProfile.get(columnName));
                continue;
            }
            if (columnName.equals(it.kdm.docer.sdk.Constants.folder_des_folder)) {
                folderProfile.setDescrizione(rowProfile.get(columnName));
                continue;
            }
            if (columnName.equals(it.kdm.docer.sdk.Constants.folder_folder_name)) {
                folderProfile.setFolderName(rowProfile.get(columnName));
                continue;
            }
            if (columnName.equals(it.kdm.docer.sdk.Constants.folder_owner)) {
                folderProfile.setFolderOwner(rowProfile.get(columnName));
                continue;
            }
            if (columnName.equals("PATH")) {
                folderProfile.getExtraInfo().put("PATH", rowProfile.get(columnName));
                continue;
            }

        }

        return folderProfile;

    }

    // private List<ISearchItem> listOfMapToList(List<Map<String, String>> list)
    // {
    //
    // List<ISearchItem> searchItems = new ArrayList<ISearchItem>();
    //
    // for (Map<String, String> singleRes : list) {
    // ISearchItem si = new SearchItem_old();
    // List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
    // for (String key : singleRes.keySet()) {
    // kvpList.add(new KeyValuePair(key, singleRes.get(key)));
    // }
    // si.setMetadata(kvpList.toArray(new KeyValuePair[0]));
    // searchItems.add(si);
    // }
    //
    // return searchItems;
    // }

    // private List<IKeyValuePair> mapToList(Map<String, String> map) {
    //
    // List<IKeyValuePair> list = new ArrayList<IKeyValuePair>();
    //
    // for (String key : map.keySet()) {
    //
    // list.add(new KeyValuePair(key, map.get(key)));
    // }
    //
    // return list;
    // }

    private Map<String, String> getTitolario(String codEnte, String codAOO, String classifica, String pianoClass) throws DocerException {

        if (StringUtils.isEmpty(codEnte))
            throw new DocerException("Ricerca voce di Titolario: COD_ENTE obbligatorio");
        if (StringUtils.isEmpty(codAOO))
            throw new DocerException("Ricerca voce di Titolario: COD_AOO obbligatorio");

        Map<String, String> id = new HashMap<String, String>();
        id.put(Constants.titolario_cod_ente, codEnte);
        id.put(Constants.titolario_cod_aoo, codAOO);
        id.put(Constants.titolario_classifica, classifica);

        // DOCER-36 Piano Class
        if (!StringUtils.isEmpty(pianoClass))
            id.put(Constants.titolario_piano_class, pianoClass);
        else
            id.put(Constants.titolario_piano_class, "");

        Map<String, String> ptit = getAnagraficaProfile(Constants.titolario_type_id, id);

        if (ptit == null) {
            throw new DocerException("Titolario non trovato: " + id.toString());
        }

        // if (ptit.get(Constants.titolario_enabled).equalsIgnoreCase(
        // EnumBoolean.FALSE.toString()))
        // throw new DocerException("Titolario disabilitato: CLASSIFICA "
        // + classifica);

        String codTitolario = ptit.get(Constants.titolario_cod_titolario);
        if (StringUtils.isEmpty(codTitolario)) {
            throw new DocerException("Errore compatibilita' DOCAREA/DocER: la voce di Titolario con CLASSIFICA " + classifica + " non ha COD_TITOLARIO assegnato");
        }

        return ptit;

    }

    private void checkFascicoloPrincipale(DataRow<String> oldProfileData, Map<String, String> metadatiFascicolazione) throws DocerException {

        boolean progrFascicoloSpecificato = metadatiFascicolazione.containsKey(Constants.doc_progr_fascicolo);

        if (!progrFascicoloSpecificato) {
            return;
        }

        String new_progr_fascicolo = metadatiFascicolazione.get(Constants.doc_progr_fascicolo);

        if (progrFascicoloSpecificato && StringUtils.isEmpty(new_progr_fascicolo)) {
            return; // annullamento fascicolazione
        }

        String codice_ente = metadatiFascicolazione.get(Constants.doc_cod_ente);
        String codice_aoo = metadatiFascicolazione.get(Constants.doc_cod_aoo);
        String new_classifica = metadatiFascicolazione.get(Constants.doc_classifica);
        String new_anno_fascicolo = metadatiFascicolazione.get(Constants.doc_anno_fascicolo);
        String new_fasc_secondari = metadatiFascicolazione.get(Constants.doc_fascicoli_secondari);
        // DOCER-36 Piano Class
        String pianoClass = metadatiFascicolazione.get(Constants.doc_piano_class);
        if (pianoClass == null)
            pianoClass = "";

        String old_classifica = oldProfileData.get(Constants.doc_classifica);
        String old_anno_fascicolo = oldProfileData.get(Constants.doc_anno_fascicolo);
        String old_progr_fascicolo = oldProfileData.get(Constants.doc_progr_fascicolo);
        String old_cod_titolario = oldProfileData.get(Constants.doc_cod_titolario);
        String old_num_fascicolo = oldProfileData.get(Constants.doc_num_fascicolo);

        String old_fascicoloPrimario = BL_CONFIGURATIONS.getConfig(entetoken).getFascicoloUtils().toFascicoloSecondarioString(old_classifica, old_anno_fascicolo, old_progr_fascicolo);
        String new_fascicoloPrimario = BL_CONFIGURATIONS.getConfig(entetoken).getFascicoloUtils().toFascicoloSecondarioString(new_classifica, new_anno_fascicolo, new_progr_fascicolo);

        if (old_fascicoloPrimario.equals(new_fascicoloPrimario)) {
            // non e' cambiato il fascicolo primario
            metadatiFascicolazione.put(Constants.titolario_cod_titolario, old_cod_titolario);
            metadatiFascicolazione.put(Constants.fascicolo_num_fascicolo, old_num_fascicolo);
            return;
        }

        Map<String, String> id = new HashMap<String, String>();
        id.put(Constants.fascicolo_cod_ente, codice_ente);
        id.put(Constants.fascicolo_cod_aoo, codice_aoo);
        id.put(Constants.fascicolo_classifica, new_classifica);
        id.put(Constants.fascicolo_anno_fascicolo, new_anno_fascicolo);
        id.put(Constants.fascicolo_progr_fascicolo, new_progr_fascicolo);
        // DOCER-36
        id.put(Constants.fascicolo_piano_class, pianoClass);

        if (StringUtils.isEmpty(codice_ente))
            throw new DocerException("Ricerca Fascicolo principale: COD_ENTE obbligatorio");
        if (StringUtils.isEmpty(codice_aoo))
            throw new DocerException("Ricerca Fascicolo principale: COD_AOO obbligatorio");
        if (StringUtils.isEmpty(new_classifica))
            throw new DocerException("Ricerca Fascicolo principale: CLASSIFICA obbligatoria");

        Map<String, String> fascicolo = getAnagraficaProfile(Constants.fascicolo_type_id, id);

        // se non ho diritti di visibilita' sul fascicolo primario
        if (fascicolo == null) {
            // throw new DocerException("Fascicolo con COD_ENTE: " + codice_ente
            // + ", COD_AOO: " + codice_aoo + ", CLASSIFICA: " + classifica +
            // ", ANNO_FASCICOLO: " + anno_fascicolo + ", PROGR_FASCICOLO: " +
            // progressivo_fascicolo + " non trovato");

            // se il vecchio primario e' diventato un nuovo secondario non
            // eseguo la ricerca ma lo riassegno
            // come nuovo primario altrimenti devo recuperare cod_titolario e
            // num_fascicolo
            List<String> fascicoli_secondari_new = new ArrayList<String>();
            if (StringUtils.isNotEmpty(new_fasc_secondari)) {
                String[] arr_fascicoli_secondari = new_fasc_secondari.split(" *; *");

                for (String f : arr_fascicoli_secondari) {

                    if (StringUtils.isEmpty(f) || fascicoli_secondari_new.contains(f)) {
                        continue;
                    }
                    fascicoli_secondari_new.add(f);
                }

                if (fascicoli_secondari_new.contains(old_fascicoloPrimario)) {
                    metadatiFascicolazione.put(Constants.doc_classifica, old_classifica);
                    metadatiFascicolazione.put(Constants.doc_anno_fascicolo, old_anno_fascicolo);
                    metadatiFascicolazione.put(Constants.doc_progr_fascicolo, old_progr_fascicolo);
                    metadatiFascicolazione.put(Constants.titolario_cod_titolario, old_cod_titolario);
                    metadatiFascicolazione.put(Constants.fascicolo_num_fascicolo, old_num_fascicolo);

                    // sposto il nuovo fasciolo primario nei secondari e al
                    // primario ci rimetto il vecchio
                    fascicoli_secondari_new.remove(old_fascicoloPrimario);

                    fascicoli_secondari_new.add(new_fascicoloPrimario);

                    new_fasc_secondari = "";
                    for (String fsec : fascicoli_secondari_new) {
                        if (StringUtils.isEmpty(fsec)) {
                            continue;
                        }
                        new_fasc_secondari += fsec + ";";
                    }

                    new_fasc_secondari = new_fasc_secondari.replaceAll(";$", "");

                    metadatiFascicolazione.put(Constants.doc_fascicoli_secondari, old_num_fascicolo);

                    return;
                }
            }

            throw new DocerException("Fascicolo non trovato: " + id.toString());
        }

        // if (fascicolo.get(Constants.fascicolo_enabled).equalsIgnoreCase(
        // EnumBoolean.FALSE.toString()))
        // throw new DocerException("Fascicolo disabilitato; COD_ENTE: "
        // + codice_ente + ", COD_AOO: " + codice_aoo
        // + ", CLASSIFICA: " + classifica + ", ANNO_FASCICOLO: "
        // + anno_fascicolo + ", PROGR_FASCICOLO: "
        // + progressivo_fascicolo);

        String codTitolario = fascicolo.get(Constants.titolario_cod_titolario);
        String numFascicolo = fascicolo.get(Constants.fascicolo_num_fascicolo);

        if (numFascicolo == null || numFascicolo.equals(VOID)) {
            throw new DocerException("Errore compatibilita' DOCAREA/DocER: il fascicolo non ha NUM_FASCICOLO assegnato: " + id.toString());
        }
        if (codTitolario == null || codTitolario.equals(VOID)) {
            throw new DocerException("Errore compatibilita' DOCAREA/DocER: il fascicolo non ha COD_TITOLARIO assegnato: " + id.toString());
        }

        metadatiFascicolazione.put(Constants.titolario_cod_titolario, codTitolario);
        metadatiFascicolazione.put(Constants.fascicolo_num_fascicolo, numFascicolo);
    }

    // private void checkLockStatusRiferimentiToRemove(List<String>
    // toRemoveRiferimenti) throws DocerException {
    //
    // for (String rifId : toRemoveRiferimenti) {
    //
    // LockStatus checkedOutInfo;
    // try {
    // checkedOutInfo = (LockStatus)provider.isCheckedOutDocument(rifId);
    // }
    // catch (Exception e) {
    // throw new DocerException(e.getMessage());
    // }
    // if (checkedOutInfo.getLocked())
    // throw new DocerException("il Riferimento da rimuovere " + rifId +
    // " e' in stato di blocco exclusivo");
    // }
    // }

    // private void checkLockStatusRiferimentiToAdd(List<String>
    // toAddRiferimenti) throws DocerException {
    //
    // for (String rifId : toAddRiferimenti) {
    //
    // LockStatus checkedOutInfo;
    // try {
    // checkedOutInfo = (LockStatus)provider.isCheckedOutDocument(rifId);
    // }
    // catch (Exception e) {
    // throw new DocerException(e.getMessage());
    // }
    // if (checkedOutInfo.getLocked())
    // throw new DocerException("il Riferimento da aggiungere " + rifId +
    // " e' in stato di blocco exclusivo");
    // }
    // }

    private DateTime parseDateTime(String datetime) {
        DateTimeFormatter fm = ISODateTimeFormat.dateTime();
        return fm.parseDateTime(datetime);
    }

    // controllo della catena dei related quando il documento diventa un RECORD
    private Map<String, Map<String, String>> checkRecordRelatedChain(EnumStatoArchivistico newMasterStatoArchivistico, EnumStatiPantarei newMasterStatoPantarei, String codEnte, String codAOO,
                                                                     String docId, String typistId, List<String> relatedChain) throws DocerException {

        Map<String, Map<String, String>> relProfiles = new HashMap<String, Map<String, String>>();

        if (relatedChain == null || relatedChain.size() < 1)
            return relProfiles;

        // criteri di ricerca
        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
        List<String> criteria = new ArrayList<String>();

        // metadati che voglio ricercare
        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(Constants.doc_cod_ente);
        returnProperties.add(Constants.doc_cod_aoo);
        returnProperties.add(Constants.doc_docnum);
        returnProperties.add(Constants.doc_stato_archivistico);
        returnProperties.add(Constants.doc_tipo_componente);
        returnProperties.add(Constants.doc_stato_pantarei);
        returnProperties.add(Constants.doc_docnum_princ);

        DataTable<String> dtResults = null;

        criteria.clear();

        for (String id : relatedChain) {

            if (id.equals(docId))
                continue;

            if (!criteria.contains(id))
                criteria.add(id);
        }

        if (criteria.size() == 0) {
            return relProfiles;
        }

        // criteri di ricerca
        searchCriteria.clear();
        searchCriteria.put(Constants.doc_docnum, criteria);

        // cerco tutti gli stati pantarei dei documenti related attualmente
        try {
            dtResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
        } catch (Exception e) {
            throw new DocerException(e.getMessage());
        }

        Collection<DataRow<String>> searchResults = dtResults.getRows();

        // verifico lo stato dei related attuali al documento
        for (DataRow<String> dr : searchResults) {

            String idRel = dr.get(Constants.doc_docnum);

            // criteria.remove(idRel);

            String relCodEnte = dr.get(Constants.doc_cod_ente);
            String relCodAOO = dr.get(Constants.doc_cod_aoo);
            EnumTipiComponente tipoComponenteRelated = getEnumTipiComponente(dr.get(Constants.doc_tipo_componente));
            String relDocnumPrinc = dr.get(Constants.doc_docnum_princ);

            EnumStatiPantarei relStatoPantarei = getEnumStatoPantarei(dr.get(Constants.doc_stato_pantarei));

            if (StringUtils.isEmpty(relCodEnte)) {
                throw new DocerException("il related " + idRel + " ha COD_ENTE non assegnato");
            }

            if (StringUtils.isEmpty(relCodAOO)) {
                throw new DocerException("il related " + idRel + " ha COD_AOO non assegnato");
            }

            if (!relCodEnte.equalsIgnoreCase(codEnte)) {
                throw new DocerException("il related " + idRel + " ha COD_ENTE " + relCodEnte + " che non coincide con COD_ENTE del documento " + docId + ": " + codEnte);
            }

            if (!relCodAOO.equalsIgnoreCase(codAOO)) {
                throw new DocerException("il related " + idRel + " ha COD_AOO " + relCodAOO + " che non coincide con COD_AOO del documento " + docId + ": " + codAOO);
            }

            if (tipoComponenteRelated.equals(EnumTipiComponente.PRINCIPALE)) {
                throw new DocerException("il related " + idRel + " ha " + Constants.doc_tipo_componente + " PRINCIPALE");
            }

            if (StringUtils.isNotEmpty(relDocnumPrinc) && !relDocnumPrinc.equals(docId)) {
                throw new DocerException("il related " + idRel + " ha un DOCNUM_PRINC " + relDocnumPrinc + " diverso dal DOCNUM del documento PRINCIPALE dell'unita' documentaria: " + docId);
            }

            // commento questa parte perche' in caso di operazione parziale non
            // posso ripetere operazione
            // if (relStatoPantarei.equals(EnumStatiPantarei.protocollato) ||
            // relStatoPantarei.equals(EnumStatiPantarei.fascicolato)) {
            // throw new DocerException("compatibilita' DOCAREA: il related " +
            // idRel + " ha STATO_PANTAREI " + relStatoPantarei + " (" +
            // relStatoPantarei.getCode() + ")");
            // }

            // creo i profili solo se non ci sono errori
            Map<String, String> relProperties = new HashMap<String, String>();

            // se e' un RECORD
            if (newMasterStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
                // relProperties.put(Constants.doc_typist_id, typistId);
                relProperties.put(Constants.doc_docnum_princ, docId);
                relProperties.put(Constants.doc_stato_archivistico, Integer.toString(newMasterStatoArchivistico.getCode()));

                if (newMasterStatoPantarei.equals(EnumStatiPantarei.protocollato)) {
                    relProperties.put(Constants.doc_stato_pantarei, Integer.toString(EnumStatiPantarei.allegato.getCode()));
                }

                relProperties.put(Constants.doc_tipo_componente, tipoComponenteRelated.toString());
                if (tipoComponenteRelated.equals(EnumTipiComponente.UNDEFINED)) {
                    relProperties.put(Constants.doc_tipo_componente, EnumTipiComponente.ALLEGATO.toString());
                }
            }

            relProfiles.put(idRel, relProperties);

        } // fine ciclo for

        return relProfiles;
    }

    // private List<String> getFilteredRelated(String docId,
    // List<EnumTipiComponente> filterTipoComponenteRelated) throws
    // DocerException {
    //
    // List<String> typedRelated = new ArrayList<String>();
    //
    // List<String> relatedChain = provider.getRelatedDocuments(docId);
    //
    // // criteri di ricerca
    // Map<String, List<String>> searchCriteria = new HashMap<String,
    // List<String>>();
    // List<String> criteria = new ArrayList<String>();
    //
    // // metadati che voglio ricercare
    // List<String> returnProperties = new ArrayList<String>();
    // returnProperties.add(Constants.doc_docnum);
    //
    // DataTable<String> dtResults = null;
    //
    // criteria.clear();
    //
    // for (String id : relatedChain) {
    //
    // if (id.equals(docId))
    // continue;
    //
    // if (!criteria.contains(id))
    // criteria.add(id);
    // }
    //
    // if (criteria.size() == 0) {
    // return typedRelated;
    // }
    //
    // // criteri di ricerca
    // searchCriteria.clear();
    // searchCriteria.put(Constants.doc_docnum, criteria);
    //
    // // cerco tutti gli stati pantarei dei documenti related attualmente
    // try {
    // dtResults = provider.searchDocuments(searchCriteria, null,
    // returnProperties, PRIMARYSEARCH_MAX_ROWS, null);
    // }
    // catch (Exception e) {
    // throw new DocerException(e.getMessage());
    // }
    //
    // Collection<DataRow<String>> searchResults = dtResults.getRows();
    //
    // // verifico lo stato dei related attuali al documento
    // for (DataRow<String> dr : searchResults) {
    //
    // String idRel = dr.get(Constants.doc_docnum);
    //
    // EnumTipiComponente tipoComponenteRelated =
    // getEnumTipiComponente(dr.get(Constants.doc_tipo_componente));
    //
    // if (filterTipoComponenteRelated.contains(tipoComponenteRelated)) {
    // if (typedRelated.contains(idRel)) {
    // continue;
    // }
    //
    // typedRelated.add(idRel);
    // }
    // }
    //
    // return typedRelated;
    // }

    private enum OPERAZIONE {
        registrazione, protocollazione, classificazione, fascicolazione, pubblicazione, archiviazione_in_deposito
    }

    ;

    private DataRow<String> getOldProfile(OPERAZIONE operazione, String docId) throws DocerException {

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

        // impostazione dell'unico criterio di ricerca
        List<String> criteria = new ArrayList<String>();
        criteria.add(docId);
        searchCriteria.put(Constants.doc_docnum, criteria);

        // richiedo le proprieta' da confrontare
        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(Constants.doc_stato_pantarei);
        returnProperties.add(Constants.doc_cod_ente);
        returnProperties.add(Constants.doc_cod_aoo);
        returnProperties.add(Constants.doc_type_id);
        returnProperties.add(Constants.doc_stato_archivistico);
        returnProperties.add(Constants.doc_tipo_componente);
        returnProperties.add(Constants.doc_pubblicazione_pubblicato);
        returnProperties.add(Constants.doc_docnum_record);

        if (operazione.equals(OPERAZIONE.classificazione)) {
            returnProperties.add(Constants.doc_classifica);
            returnProperties.add(Constants.doc_progr_fascicolo);
        } else if (operazione.equals(OPERAZIONE.fascicolazione)) {

            returnProperties.add(Constants.doc_classifica);
            returnProperties.add(Constants.doc_anno_fascicolo);
            returnProperties.add(Constants.doc_progr_fascicolo);
            returnProperties.add(Constants.doc_fascicoli_secondari);
            // DOCER-36 Piano di classificazione
            returnProperties.add(Constants.doc_piano_class);

            // MODIFICA 2015-11-09 Bug fix su aggiunta fascicoli secondari dopo prima fascicolazione
            returnProperties.add(Constants.doc_num_fascicolo);
            returnProperties.add(Constants.doc_cod_titolario);
            // --- FINE MODIFICA
        } else if (operazione.equals(OPERAZIONE.registrazione)) {

            returnProperties.add(Constants.doc_classifica);
            returnProperties.add(Constants.doc_progr_fascicolo);

            returnProperties.add(Constants.doc_anno_fascicolo);

            returnProperties.add(Constants.doc_registrazione_anno);
            returnProperties.add(Constants.doc_registrazione_data);
            returnProperties.add(Constants.doc_registrazione_id_registro);
            returnProperties.add(Constants.doc_registrazione_numero);
            returnProperties.add(Constants.doc_registrazione_oggetto);

            returnProperties.add(Constants.doc_protocollo_numero);
            returnProperties.add(Constants.doc_protocollo_tipo_protocollazione);

            returnProperties.add(Constants.doc_reg_e_proto_destinatari);
            returnProperties.add(Constants.doc_reg_e_proto_firmatario);
            returnProperties.add(Constants.doc_reg_e_proto_mittenti);
            returnProperties.add(Constants.doc_reg_e_proto_tipo_firma);

            // nuovi metadati 25-10-2012
            returnProperties.add(Constants.doc_registrazione_annullata_registrazione);
            returnProperties.add(Constants.doc_registrazione_data_annullamento_registrazione);
            returnProperties.add(Constants.doc_registrazione_motivo_annullamento_registrazione);
            returnProperties.add(Constants.doc_registrazione_provvedimento_annullamento_registrazione);
        } else if (operazione.equals(OPERAZIONE.protocollazione)) {

            returnProperties.add(Constants.doc_protocollo_numero);
            returnProperties.add(Constants.doc_protocollo_anno);
            returnProperties.add(Constants.doc_protocollo_oggetto);
            returnProperties.add(Constants.doc_protocollo_registro);
            returnProperties.add(Constants.doc_protocollo_data);
            returnProperties.add(Constants.doc_protocollo_tipo_protocollazione);

            returnProperties.add(Constants.doc_classifica);

            returnProperties.add(Constants.doc_anno_fascicolo);
            returnProperties.add(Constants.doc_progr_fascicolo);

            returnProperties.add(Constants.doc_registrazione_numero);

            returnProperties.add(Constants.doc_reg_e_proto_destinatari);
            returnProperties.add(Constants.doc_reg_e_proto_firmatario);
            returnProperties.add(Constants.doc_reg_e_proto_mittenti);
            returnProperties.add(Constants.doc_reg_e_proto_tipo_firma);

            // nuovi metadati 25-10-2012
            returnProperties.add(Constants.doc_protocollo_num_pg_mittente);
            returnProperties.add(Constants.doc_protocollo_data_pg_mittente);
            returnProperties.add(Constants.doc_protocollo_cod_ente_mittente);
            returnProperties.add(Constants.doc_protocollo_cod_aoo_mittente);
            returnProperties.add(Constants.doc_protocollo_classifica_mittente);
            returnProperties.add(Constants.doc_protocollo_fascicolo_mittente);
            returnProperties.add(Constants.doc_protocollo_annullato_protocollo);
            returnProperties.add(Constants.doc_protocollo_data_annullamento_protocollo);
            returnProperties.add(Constants.doc_protocollo_motivo_annullamento_protocollo);
            returnProperties.add(Constants.doc_protocollo_provvedimento_annullamento_protocollo);
        } else if (operazione.equals(OPERAZIONE.pubblicazione)) {

            returnProperties.add(Constants.doc_pubblicazione_registro);
            returnProperties.add(Constants.doc_pubblicazione_numero);
            returnProperties.add(Constants.doc_pubblicazione_anno);
            returnProperties.add(Constants.doc_pubblicazione_oggetto);
            returnProperties.add(Constants.doc_pubblicazione_data_inizio);
            returnProperties.add(Constants.doc_pubblicazione_data_fine);
            returnProperties.add(Constants.doc_pubblicazione_annullata);
            returnProperties.add(Constants.doc_pubblicazione_data_annullamento);
            returnProperties.add(Constants.doc_pubblicazione_motivo_annullamento);
            returnProperties.add(Constants.doc_pubblicazione_history);
        } else if (operazione.equals(OPERAZIONE.archiviazione_in_deposito)) {

        }

        // test della library

        // RICERCA PRIMARIA
        DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

        // deve esserci un solo risultato perche' devo modificare un solo
        // documento
        if (searchResults.getRows().size() == 0)
            throw new DocerException("documento " + docId + " non trovato");
        if (searchResults.getRows().size() != 1)
            throw new DocerException("docId " + docId + " non univoco");

        // HO TROVATO IL DOCUMENTO -> un solo risultato
        return searchResults.getRow(0);

    }

    private void doOperationCheck(OPERAZIONE operazione, String docId, String userid, DataRow<String> oldProfileData, Map<String, String> metadata) throws DocerException {

        String oldDocnumRecord = oldProfileData.get(Constants.doc_docnum_record);
        if (oldDocnumRecord != null && !oldDocnumRecord.equals(VOID)) {

            if (operazione.equals(OPERAZIONE.registrazione) || operazione.equals(OPERAZIONE.protocollazione)) {
                throw new DocerException("non e' possibile eseguire l'operazione di " + operazione + " per un documento con DOCNUM_RECORD assegnato (" + oldDocnumRecord + ")");
            }
        }

        ILockStatus checkedOutInfo = provider.isCheckedOutDocument(docId);
        if (checkedOutInfo.getLocked() && !checkedOutInfo.getUserId().equalsIgnoreCase(userid))
            throw new DocerException("documento bloccato esclusivamente da un altro utente");

        EnumTipiComponente oldTipoComponente = getEnumTipiComponente(oldProfileData.get(Constants.doc_tipo_componente));

        if (!oldTipoComponente.equals(EnumTipiComponente.UNDEFINED) && !oldTipoComponente.equals(EnumTipiComponente.PRINCIPALE))
            throw new DocerException("non e' possibile eseguire esplicitamente l'operazione di " + operazione + " per un documento con " + Constants.doc_tipo_componente + " " + oldTipoComponente);

        EnumStatiPantarei oldStatoPantarei = getEnumStatoPantarei(oldProfileData.get(Constants.doc_stato_pantarei));

        if (oldStatoPantarei.equals(EnumStatiPantarei.allegato)) {

            throw new DocerException("compatibilita' DOCAREA: non e' possibile eseguire l'operazione di " + operazione + " per un documento con STATO_PANTAREI: " + oldStatoPantarei + " ("
                    + oldStatoPantarei.getCode() + ")");
        }

        EnumStatoArchivistico oldStatoArchivistico = getEnumStatoArchivistico(oldProfileData.get(Constants.doc_stato_archivistico));

        if (oldStatoArchivistico.equals(EnumStatoArchivistico.in_archivio_di_deposito)) {

            throw new DocerException("non e' possibile modificare un documento con STATO_ARCHIVISTICO: " + EnumStatoArchivistico.in_archivio_di_deposito + " ("
                    + EnumStatoArchivistico.in_archivio_di_deposito.getCode() + ")");
        }

        String oldCodEnte = oldProfileData.get(Constants.doc_cod_ente);
        if (oldCodEnte == null || oldCodEnte.equals(VOID))
            throw new DocerException("ERRORE: documento " + docId + " senza COD_ENTE assegnato");

        String oldCodAOO = oldProfileData.get(Constants.doc_cod_aoo);
        if (oldCodAOO == null || oldCodAOO.equals(VOID))
            throw new DocerException("ERRORE: documento " + docId + " senza COD_AOO assegnato");

        if (!metadata.containsKey(Constants.doc_cod_ente)) {
            // throw new DocerException("COD_ENTE obbligatorio");
            metadata.put("COD_ENTE", oldCodEnte);
        }

        String newCodEnte = metadata.get(Constants.doc_cod_ente);
        if (!oldCodEnte.equalsIgnoreCase(newCodEnte))
            throw new DocerException("COD_ENTE specificato (" + newCodEnte + ") e' diverso da COD_ENTE assegnato al documento (" + oldCodEnte + ")");

        if (!metadata.containsKey(Constants.doc_cod_aoo)) {
            // throw new DocerException("COD_AOO obbligatorio");
            metadata.put("COD_AOO", oldCodAOO);
        }

        String newCodAOO = metadata.get(Constants.doc_cod_aoo);
        if (!oldCodAOO.equalsIgnoreCase(newCodAOO))
            throw new DocerException("COD_AOO specificato (" + newCodAOO + ") e' diverso da COD_AOO assegnato al documento (" + oldCodAOO + ")");

        // documentType NON puo' essere null (e' dato obbligatorio di
        // creazione)
        String oldDocumentTypeId = oldProfileData.get(Constants.doc_type_id);

        if (oldDocumentTypeId == null)
            throw new DocerException("document type non assegnato al documento nel sistema: " + docId);

        oldDocumentTypeId = oldDocumentTypeId.toUpperCase();

        DocumentType docType = BL_CONFIGURATIONS.getConfig(entetoken).getDocumentTypesMapping().get(oldDocumentTypeId, oldCodEnte, oldCodAOO);

        if (docType == null) {
            throw new DocerException("Document Type " + oldDocumentTypeId + " non e' definito per Ente " + oldCodEnte + " e AOO " + oldCodAOO);
        }

        FieldDescriptor fd = null;
        // controllo se i campi specifiati appartengono al documenttype
        for (String propName : metadata.keySet()) {

            String newValue = metadata.get(propName);
            String oldValue = oldProfileData.get(propName);

            if (newValue == null) {
                newValue = "";
            }
            if (oldValue == null) {
                oldValue = "";
            }

            if (newValue.equals(oldValue)) {
                continue;
            }

            fd = docType.getFieldDescriptor(propName);

            if (fd == null) {
                throw new DocerException("il campo: " + propName + " non appartiene al tipo: " + oldDocumentTypeId + " per Ente: " + oldCodEnte + " e AOO: " + oldCodAOO);
            }

            // controllo lunghezza e formato dei metadati e modifico il formato
            // (solo se e' cambiato)
            String newValueFormatted = fd.checkValueFormat(newValue);

            // per i DATE possono cambiare ore, minuti, secondi tra il valore
            // passato e quello assegnato da EDMS
            if (fd.getType().equals(EnumBLPropertyType.DATE)) {

                if (StringUtils.isNotEmpty(oldValue) && StringUtils.isNotEmpty(newValue)) {

                    String oldDate = oldValue.replaceAll("T.+$", "");
                    String newDate = newValueFormatted.replaceAll("T.+$", "");
                    if (oldDate.equals(newDate)) {
                        newValueFormatted = oldValue;
                    }
                }
            }

            metadata.put(propName, newValueFormatted);

        }

        // CONTROLLO I METADATI DA NON SPECIFICARE
        for (String propName : metadata.keySet()) {

            if (propName.equalsIgnoreCase(Constants.doc_cod_ente) || propName.equalsIgnoreCase(Constants.doc_cod_aoo)) {
                continue;
            }

            if (operazione.equals(OPERAZIONE.registrazione)) {

                // metadati di registrazione
                if (propName.equalsIgnoreCase(Constants.doc_registrazione_anno) || propName.equalsIgnoreCase(Constants.doc_registrazione_data)
                        || propName.equalsIgnoreCase(Constants.doc_registrazione_id_registro) || propName.equalsIgnoreCase(Constants.doc_registrazione_numero)
                        || propName.equalsIgnoreCase(Constants.doc_registrazione_oggetto) || propName.equalsIgnoreCase(Constants.doc_reg_e_proto_destinatari)
                        || propName.equalsIgnoreCase(Constants.doc_reg_e_proto_firmatario)
                        || propName.equalsIgnoreCase(Constants.doc_reg_e_proto_mittenti)
                        || propName.equalsIgnoreCase(Constants.doc_reg_e_proto_tipo_firma)
                        // nuovi metadati 25-10-2012
                        || propName.equalsIgnoreCase(Constants.doc_registrazione_annullata_registrazione) || propName.equalsIgnoreCase(Constants.doc_registrazione_data_annullamento_registrazione)
                        || propName.equalsIgnoreCase(Constants.doc_registrazione_motivo_annullamento_registrazione)
                        || propName.equalsIgnoreCase(Constants.doc_registrazione_provvedimento_annullamento_registrazione)) {
                    continue;
                }

                throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);

            }

            if (operazione.equals(OPERAZIONE.protocollazione)) {

                if (propName.equalsIgnoreCase(Constants.doc_protocollo_registro) || propName.equalsIgnoreCase(Constants.doc_protocollo_oggetto)
                        || propName.equalsIgnoreCase(Constants.doc_protocollo_data) || propName.equalsIgnoreCase(Constants.doc_protocollo_numero)
                        || propName.equalsIgnoreCase(Constants.doc_protocollo_anno)
                        || propName.equalsIgnoreCase(Constants.doc_protocollo_tipo_protocollazione)
                        || propName.equalsIgnoreCase(Constants.doc_reg_e_proto_destinatari)
                        || propName.equalsIgnoreCase(Constants.doc_reg_e_proto_firmatario)
                        || propName.equalsIgnoreCase(Constants.doc_reg_e_proto_mittenti)
                        || propName.equalsIgnoreCase(Constants.doc_reg_e_proto_tipo_firma)
                        || propName.equalsIgnoreCase(Constants.doc_protocollo_num_pg_mittente)
                        // nuovi metadati 25-10-2012
                        || propName.equalsIgnoreCase(Constants.doc_protocollo_data_pg_mittente) || propName.equalsIgnoreCase(Constants.doc_protocollo_cod_ente_mittente)
                        || propName.equalsIgnoreCase(Constants.doc_protocollo_cod_aoo_mittente) || propName.equalsIgnoreCase(Constants.doc_protocollo_classifica_mittente)
                        || propName.equalsIgnoreCase(Constants.doc_protocollo_fascicolo_mittente) || propName.equalsIgnoreCase(Constants.doc_protocollo_annullato_protocollo)
                        || propName.equalsIgnoreCase(Constants.doc_protocollo_data_annullamento_protocollo) || propName.equalsIgnoreCase(Constants.doc_protocollo_motivo_annullamento_protocollo)
                        || propName.equalsIgnoreCase(Constants.doc_protocollo_provvedimento_annullamento_protocollo)) {

                    continue;
                }

                throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);

            }

            if (operazione.equals(OPERAZIONE.classificazione)) {

                if (propName.equalsIgnoreCase(Constants.doc_classifica) || propName.equalsIgnoreCase(Constants.doc_cod_titolario)) {
                    continue;
                }

                throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);
            }

            if (operazione.equals(OPERAZIONE.fascicolazione)) {

                if (propName.equalsIgnoreCase(Constants.doc_classifica) || propName.equalsIgnoreCase(Constants.doc_num_fascicolo) || propName.equalsIgnoreCase(Constants.doc_progr_fascicolo)
                        || propName.equalsIgnoreCase(Constants.doc_anno_fascicolo) || propName.equalsIgnoreCase(Constants.doc_fascicoli_secondari)
                        || propName.equalsIgnoreCase(Constants.doc_piano_class)) {
                    continue;
                }

                throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);
            }

            if (operazione.equals(OPERAZIONE.pubblicazione)) {

                // metadati di pubblicazione
                if (propName.equalsIgnoreCase(Constants.doc_pubblicazione_registro) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_anno)
                        || propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_fine) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_inizio)
                        || propName.equalsIgnoreCase(Constants.doc_pubblicazione_numero) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_oggetto)
                        || propName.equalsIgnoreCase(Constants.doc_pubblicazione_pubblicato) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_annullata) ||
                        propName.equalsIgnoreCase(Constants.doc_pubblicazione_data_annullamento) || propName.equalsIgnoreCase(Constants.doc_pubblicazione_motivo_annullamento)) {
                    continue;
                }

                throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);

            }

            if (operazione.equals(OPERAZIONE.archiviazione_in_deposito)) {
                // DA IMPLEMETARE
                // metadati di pubblicazione
                // if () {
                // continue;
                // }

                throw new DocerException("il metadato " + propName + " non deve essere specificato nell'operazione di " + operazione);

            }

        }

    }

    private Map<String, String> toUCMap(Map<String, String> map, String variableName) throws DocerException {

        if (map == null) {
            throw new DocerException(variableName + " null");
        }

        Map<String, String> ucmap = new HashMap<String, String>();

        for (String key : map.keySet()) {

            ucmap.put(key.toUpperCase(), map.get(key));
        }

        return ucmap;
    }

    private Map<String, List<String>> toUCMapOfList(Map<String, List<String>> mapOfList) {

        Map<String, List<String>> ucmapOfList = new HashMap<String, List<String>>();

        for (String key : mapOfList.keySet()) {

            ucmapOfList.put(key.toUpperCase(), mapOfList.get(key));
        }

        return ucmapOfList;
    }

    private void setAdvancedVersionsDocnumRecord(String docnumRecord, EnumStatoArchivistico oldStatoArchivistico, EnumStatoArchivistico newStatoArchivistico, String operazione) throws DocerException {

        // se non cambio stato non faccio modifiche
        if (newStatoArchivistico.equals(oldStatoArchivistico)) {
            return;
        }

        if (newStatoArchivistico.getCode() <= EnumStatoArchivistico.generico_definitivo.getCode()) {
            return; // non e' una trasformazione in record
        }

        if (oldStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
            return; // e' gia' un record
        }

        List<String> advancedVersions = provider.getAdvancedVersions(docnumRecord);

        if (advancedVersions.size() == 0) {
            return;
        }

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

        // impostazione dei criteri di ricerca
        List<String> criteria = new ArrayList<String>();

        for (String id : advancedVersions) {
            criteria.add(id);
        }

        searchCriteria.put(Constants.doc_docnum, criteria);

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(Constants.doc_docnum);
        returnProperties.add(Constants.doc_stato_archivistico);

        // RICERCA PRIMARIA per verificare i metadati
        DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, PRIMARYSEARCH_MAX_ROWS, null);

        if (searchResults.getRows().size() == 0) {
            throw new DocerException("controllo versioni avanzate: nessun risultato trovato");
        }

        // leggo i metadati
        for (DataRow<String> dataRow : searchResults.getRows()) {

            String advDocnum = dataRow.get(Constants.doc_docnum);
            criteria.remove(advDocnum);
            if (advDocnum.equals(docnumRecord)) {
                continue;
            }

            EnumStatoArchivistico advStatoArchivistico = getEnumStatoArchivistico(dataRow.get(Constants.doc_stato_archivistico));
            if (advStatoArchivistico.getCode() > EnumStatoArchivistico.generico_definitivo.getCode()) {
                throw new DocerException("l'operazione di " + operazione + " per il documento " + docnumRecord
                        + " non e' ammessa perche' la catena delle versioni avanzate contiene gia' un documento record: " + advDocnum);
            }
        }

        Map<String, String> metadata = new HashMap<String, String>();
        // metadata.put(Constants.doc_docnum_record, VOID);

        // aggiorno il documento record
        // provider.updateProfileDocument(docnumRecord, metadata);

        metadata.put(Constants.doc_docnum_record, docnumRecord);

        advancedVersions.remove(docnumRecord);
        for (String docId : advancedVersions) {
            provider.updateProfileDocument(docId, metadata);
        }

    }

    private String getExtension(String docname) {

        if (docname == null) {
            return VOID;
        }

        // recupero estensione contenuta nel docname
        String ext = VOID;
        if (docname.indexOf(".") > -1) {
            // estensione
            ext = docname.replaceAll("^.*\\.", VOID);
        }

        return ext;
    }

    private void validateNextConfigurationXml(String xml) throws DocerException {

        try {

            StAXOMBuilder builder = new StAXOMBuilder(new ByteArrayInputStream(xml.getBytes("UTF-8")));

            OMElement testConfig = builder.getDocumentElement();

            // variabili di Business Logic
            AXIOMXPath path = new AXIOMXPath("//configuration/group[@name='business_logic_variables']/section[@name='props']");
            List<OMElement> elements = (List<OMElement>) path.selectNodes(testConfig);
            if (elements == null || elements.size() == 0) {
                throw new Exception("//configuration/group[@name='business_logic_variables']/section[@name='props'] -> nodo mancante");
            }

            // FIELDS
            path = new AXIOMXPath("//configuration/group[@name='impianto']/section[@name='fields']");
            elements = (List<OMElement>) path.selectNodes(testConfig);
            if (elements == null || elements.size() == 0) {
                throw new Exception("//configuration/group[@name='impianto']/section[@name='fields'] -> nodo mancante");
            }

            // BASE PROFILE
            path = new AXIOMXPath("//configuration/group[@name='impianto']/section[@name='baseprofile']");
            elements = (List<OMElement>) path.selectNodes(testConfig);
            if (elements == null || elements.size() == 0) {
                throw new Exception("//configuration/group[@name='impianto']/section[@name='baseprofile'] -> nodo mancante");
            }

            // DOCUMENT TYPES
            path = new AXIOMXPath("//configuration/group[@name='impianto']/section[@name='document_types']");
            elements = (List<OMElement>) path.selectNodes(testConfig);
            if (elements == null || elements.size() == 0) {
                throw new Exception("//configuration/group[@name='impianto']/section[@name='document_types'] -> nodo mancante");
            }

            // ANAGRAFICHE_TYPES
            path = new AXIOMXPath("//configuration/group[@name='impianto']/section[@name='anagrafiche_types']");
            elements = (List<OMElement>) path.selectNodes(testConfig);
            if (elements == null || elements.size() == 0) {
                throw new Exception("//configuration/group[@name='impianto']/section[@name='anagrafiche_types'] -> nodo mancante");
            }

            // HITLIST
            path = new AXIOMXPath("//configuration/group[@name='impianto']/section[@name='hitlists']");
            elements = (List<OMElement>) path.selectNodes(testConfig);
            if (elements == null || elements.size() == 0) {
                throw new Exception("//configuration/group[@name='impianto']/section[@name='hitlists'] -> nodo mancante");
            }

            // FORM_DINAMICHE documenti
            path = new AXIOMXPath("//configuration/group[@name='form_dinamiche']/section[@name='documenti']");
            elements = (List<OMElement>) path.selectNodes(testConfig);
            if (elements == null || elements.size() == 0) {
                throw new Exception("//configuration/group[@name='form_dinamiche']/section[@name='documenti'] -> nodo mancante");
            }

            // FORM_DINAMICHE anagrafiche
            path = new AXIOMXPath("//configuration/group[@name='form_dinamiche']/section[@name='anagrafiche']");
            elements = (List<OMElement>) path.selectNodes(testConfig);
            if (elements == null || elements.size() == 0) {
                throw new Exception("//configuration/group[@name='form_dinamiche']/section[@name='anagrafiche'] -> nodo mancante");
            }

        } catch (Exception e) {

            throw new DocerException("validateNextConfigurationXml: " + e.getMessage());
        }
    }

    private String getAdvancedVersionsRecord(DataRow<String> drRecordOld, DataRow<String> drRecordNew) throws DocerException {

        if (drRecordOld == null && drRecordNew == null) {
            return VOID;
        }

        if (drRecordNew == null) {
            return drRecordOld.get(Constants.doc_docnum);
        }

        if (drRecordOld == null) {
            return drRecordNew.get(Constants.doc_docnum);
        }

        boolean protocolException = false;

        if (!String.valueOf(drRecordNew.get(Constants.doc_protocollo_anno)).equals(String.valueOf(drRecordOld.get(Constants.doc_protocollo_anno)))) {
            protocolException = true;
        } else if (!String.valueOf(drRecordNew.get(Constants.doc_protocollo_numero)).equals(String.valueOf(drRecordOld.get(Constants.doc_protocollo_numero)))) {
            protocolException = true;
        } else if (!String.valueOf(drRecordNew.get(Constants.doc_protocollo_registro)).equals(String.valueOf(drRecordOld.get(Constants.doc_protocollo_registro)))) {
            protocolException = true;
        } else if (!String.valueOf(drRecordNew.get(Constants.doc_registrazione_anno)).equals(String.valueOf(drRecordOld.get(Constants.doc_registrazione_anno)))) {
            protocolException = true;
        } else if (!String.valueOf(drRecordNew.get(Constants.doc_registrazione_numero)).equals(String.valueOf(drRecordOld.get(Constants.doc_registrazione_numero)))) {
            protocolException = true;
        } else if (!String.valueOf(drRecordNew.get(Constants.doc_registrazione_id_registro)).equals(String.valueOf(drRecordOld.get(Constants.doc_registrazione_id_registro)))) {
            protocolException = true;
        }

        if (protocolException) {
            throw new DocerException("e' ammesso un solo Record (o piu' Record con stessi estremi di protocollazione/registrazione) tra le versioni avanzate di un documento: "
                    + drRecordNew.get(Constants.doc_docnum) + " ha " + Constants.doc_stato_archivistico + " " + drRecordNew.get(Constants.doc_stato_archivistico) + ", "
                    + drRecordOld.get(Constants.doc_docnum) + " ha " + Constants.doc_stato_archivistico + " " + drRecordOld.get(Constants.doc_stato_archivistico));
        }

        return drRecordNew.get(Constants.doc_docnum);
    }

    private List<String> getFascicoliDelta(DataRow<String> oldProfileData, Map<String, String> metadata) throws DocerException {

        String cod_ente = oldProfileData.get(Constants.doc_cod_ente);

        List<String> fascicoli_delta = new ArrayList<String>();

        List<String> fascicoli_current = new ArrayList<String>();

        String classifica = oldProfileData.get(Constants.doc_classifica);
        String anno_fascicolo = oldProfileData.get(Constants.doc_anno_fascicolo);
        String progr_fascicolo = oldProfileData.get(Constants.doc_progr_fascicolo);

        String fascicolo_primario_current = BL_CONFIGURATIONS.getConfig(entetoken).getFascicoloUtils().toFascicoloSecondarioString(classifica, anno_fascicolo, progr_fascicolo);
        if (StringUtils.isNotEmpty(fascicolo_primario_current)) {
            fascicoli_current.add(fascicolo_primario_current);
        }

        List<String> fasc_secondari_current_list = new ArrayList<String>();

        String fasc_secondari = oldProfileData.get(Constants.doc_fascicoli_secondari);
        if (StringUtils.isNotEmpty(fasc_secondari)) {
            String[] fascicoli_secondari_current = fasc_secondari.split(" *; *");

            for (String f : fascicoli_secondari_current) {

                if (StringUtils.isEmpty(f) || fascicoli_current.contains(f)) {
                    continue;
                }
                fascicoli_current.add(f);
                fasc_secondari_current_list.add(f);
            }
        }

        List<String> fascicoli_new = new ArrayList<String>();

        String new_classifica = metadata.get(Constants.doc_classifica);
        String new_anno_fascicolo = metadata.get(Constants.doc_anno_fascicolo);
        String new_progr_fascicolo = metadata.get(Constants.doc_progr_fascicolo);

        boolean annullamentoFascicolazione = metadata.containsKey(Constants.doc_progr_fascicolo) && StringUtils.isEmpty(new_progr_fascicolo);

        // in annullamento sono tutti da controllare
        if (annullamentoFascicolazione) {
            return fascicoli_current;
        }

        // se hanno specificato il fascicolo primario
        if (metadata.containsKey(Constants.doc_progr_fascicolo)) {
            String fascicolo_primario_new = BL_CONFIGURATIONS.getConfig(entetoken).getFascicoloUtils().toFascicoloSecondarioString(new_classifica, new_anno_fascicolo, new_progr_fascicolo);
            if (StringUtils.isNotEmpty(fascicolo_primario_new)) {
                fascicoli_new.add(fascicolo_primario_new);
            }
        } else {
            fascicoli_new.add(fascicolo_primario_current);
        }

        // se hanno specificato i fascicoli secondari
        if (metadata.containsKey(Constants.doc_fascicoli_secondari)) {
            String new_fasc_secondari = metadata.get(Constants.doc_fascicoli_secondari);
            if (StringUtils.isNotEmpty(new_fasc_secondari)) {
                String[] fascicoli_secondari_new = new_fasc_secondari.split(" *; *");

                for (String f : fascicoli_secondari_new) {

                    if (StringUtils.isEmpty(f) || fascicoli_new.contains(f)) {
                        continue;
                    }
                    fascicoli_new.add(f);
                }
            }
        } else {
            fascicoli_new.addAll(fasc_secondari_current_list);
        }

        //nuova routine delta
        fascicoli_delta = DocerUtils.getFascicoliDelta(fascicoli_new,fascicoli_current);

//        // e' diverso dai WSProtocollazione e WSFascicolazione perche' qui non
//        // sono sempre presenti i metadati
//        // ciclo i nuovi in assegnazione
//        for (String f : fascicoli_new) {
//            if (fascicoli_current.contains(f)) {
//                continue;
//            }
//
//            if (fascicoli_delta.contains(f)) {
//                continue;
//            }
//
//            // aggiungo fascicoli non presenti tra gli attuali (quelli da
//            // aggiungere)
//            fascicoli_delta.add(f);
//        }
//
//        for (String f : fascicoli_current) {
//            if (fascicoli_new.contains(f)) {
//                continue;
//            }
//
//            if (fascicoli_delta.contains(f)) {
//                continue;
//            }
//
//            // aggiungo fascicoli non presenti nel nuovo (quelli da rimuovere)
//            fascicoli_delta.add(f);
//        }

        return fascicoli_delta;

    }

    private void checkUserRightsFascicolo(String cod_ente, String cod_aoo, String fascicoloString, String userId, EnumACLRights minimumRight) throws DocerException {

        Map<String, String> id = BL_CONFIGURATIONS.getConfig(entetoken).getFascicoloUtils().toMap(cod_ente, cod_aoo, fascicoloString);

        EnumACLRights effectiveRights = provider.getEffectiveRightsAnagrafiche("FASCICOLO", id, userId);

        // 0 fullAccess
        // 1 normalAccess
        // 2 readOnly

        if (effectiveRights.getCode() < 0) {
            throw new DocerException("Fascicolo non trovato: " + id.toString());
        }

        // la verifica va fatta al contrario...
        if (effectiveRights.getCode() > minimumRight.getCode()) {
            throw new DocerException("l'utente " + userId + " non ha diritti sufficienti sul Fascicolo: " + id.toString());
        }

    }


    private boolean mapsAreEqual(Map<String, EnumACLRights> map1, Map<String, EnumACLRights> map2) {

        for (String key : map1.keySet()) {
            EnumACLRights value1 = map1.get(key);
            EnumACLRights value2 = map2.get(key);
            if (!value1.equals(value2)) {
                return false;
            }
        }

        for (String key : map2.keySet()) {
            EnumACLRights value2 = map2.get(key);
            EnumACLRights value1 = map1.get(key);
            if (!value2.equals(value1)) {
                return false;
            }
        }

        return true;
    }


    public Map<String, EnumACLRights> updateACLDocumento(String token, String docnum, KeyValuePair aclToAdd[], String aclToRemove[]) throws DocerException {

        if (docnum == null || "".equals(docnum)) {
            throw new DocerException("521 - campo docnum obbligatorio");
        }

        if ((aclToAdd == null || aclToAdd.length < 1) && (aclToRemove == null || aclToRemove.length < 1)) {
            throw new DocerException("522 - liste acl da aggiungere e da rimuovere non valorizzate");
        }

        try {
            Map<String, EnumACLRights> aclOnDocument = getACLDocument(token, docnum);
            if (aclToRemove != null) {
                for (String id : aclToRemove) {
                    if (aclOnDocument.containsKey(id)) {
                        aclOnDocument.remove(id);
                    }
                }
            }

            if (aclToAdd != null) {
                for (KeyValuePair kvp : aclToAdd) {
                    if (kvp.getKey() != null)
                        aclOnDocument.put(kvp.getKey(), BusinessLogic.getEnumACLRights(kvp.getValue()));
                }
            }
            setACLDocument(token, docnum, aclOnDocument);
            return getACLDocument(token, docnum);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    public Map<String, EnumACLRights> updateACLFolder(String token, String folderId, KeyValuePair aclToAdd[], String aclToRemove[]) throws DocerException {


        if (folderId == null || "".equals(folderId)) {
            throw new DocerException("521 - campo folderId obbligatorio");
        }

        if ((aclToAdd == null || aclToAdd.length < 1) && (aclToRemove == null || aclToRemove.length < 1)) {
            throw new DocerException("522 - liste acl da aggiungere e da rimuovere non valorizzate");
        }

        try {
            Map<String, EnumACLRights> aclOnFolder = getACLFolder(token, folderId);
            if (aclToRemove != null) {
                for (String kvp : aclToRemove) {
                    if (aclOnFolder.containsKey(kvp)) {
                        aclOnFolder.remove(kvp);
                    }
                }
            }

            if (aclToAdd != null) {
                for (KeyValuePair kvp : aclToAdd) {
                    if (kvp.getKey() != null)
                        aclOnFolder.put(kvp.getKey(), BusinessLogic.getEnumACLRights(kvp.getValue()));
                }
            }
            setACLFolder(token, folderId, aclOnFolder);
            return getACLFolder(token, folderId);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    // 97
    public Map<String, EnumACLRights> updateACLTitolari(String ticket, Map<String, String> id, KeyValuePair aclToAdd[], String aclToRemove[]) throws DocerException {

        if (id == null || id.get("CLASSIFICA") == null || id.get("COD_ENTE") == null || id.get("COD_AOO") == null) {
            throw new DocerException("521 - CLASSIFICA, COD_ENTE e COD_AOO sono metadati obbligatori");
        }

        if ((aclToAdd == null || aclToAdd.length < 1) && (aclToRemove == null || aclToRemove.length < 1)) {
            throw new DocerException("522 - liste acl da aggiungere e da rimuovere non valorizzate");
        }

        try {
            Map<String, EnumACLRights> aclOnTitolario = getACLTitolario(ticket, id);
            if (aclToRemove != null) {
                for (String kvp : aclToRemove) {
                    if (aclOnTitolario.containsKey(kvp)) {
                        aclOnTitolario.remove(kvp);
                    }
                }
            }

            if (aclToAdd != null) {
                for (KeyValuePair kvp : aclToAdd) {
                    if (kvp.getKey() != null)
                        aclOnTitolario.put(kvp.getKey(), BusinessLogic.getEnumACLRights(kvp.getValue()));
                }
            }
            setACLTitolario(ticket, id, aclOnTitolario);
            return getACLTitolario(ticket, id);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }


    public Map<String, EnumACLRights> updateACLFascicolo(String ticket, Map<String, String> id, KeyValuePair aclToAdd[], String aclToRemove[]) throws DocerException {

        if (id == null || id.get("CLASSIFICA") == null || id.get("COD_ENTE") == null || id.get("COD_AOO") == null || id.get("ANNO_FASCICOLO") == null || id.get("PROGR_FASCICOLO") == null) {
            throw new DocerException("521 - CLASSIFICA, COD_ENTE, COD_AOO, PROGR_FASCICOLO e ANNO_FASCICOLO sono metadati obbligatori");
        }

        if ((aclToAdd == null || aclToAdd.length < 1) && (aclToRemove == null || aclToRemove.length < 1)) {
            throw new DocerException("522 - liste acl da aggiungere e da rimuovere non valorizzate");
        }

        try {
            Map<String, EnumACLRights> aclOnFascicolo = getACLFascicolo(ticket, id);
            if (aclToRemove != null) {
                for (String kvp : aclToRemove) {
                    if (aclOnFascicolo.containsKey(kvp)) {
                        aclOnFascicolo.remove(kvp);
                    }
                }
            }

            if (aclToAdd != null) {
                for (KeyValuePair kvp : aclToAdd) {
                    if (kvp.getKey() != null)
                        aclOnFascicolo.put(kvp.getKey(), BusinessLogic.getEnumACLRights(kvp.getValue()));
                }
            }
            setACLFascicolo(ticket, id, aclOnFascicolo);
            return getACLFascicolo(ticket, id);
        } catch (DocerException e) {
            throw new DocerException(e.getErrNumber() + " - " + e.getErrDescription());
        }
    }

    private void updateStatoConserv(String docIdPrincipale, String docIdAllegato) throws DocerException {
        //****************************************************************************************
        // verifica se è necessario settare STATO_CONSERV=5 (modificato)
        //****************************************************************************************

        if (docIdAllegato == null)
            docIdAllegato = docIdPrincipale;

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
        List<String> criteria = new ArrayList<String>();
        criteria.add(docIdPrincipale);
        searchCriteria.put(Constants.doc_docnum, criteria);

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(Constants.doc_stato_conservazione);

        DataTable<String> searchResults = provider.searchDocuments(searchCriteria, null, returnProperties, 1, null);

        if (searchResults.getRows().size() == 0)
            throw new DocerException("documento " + docIdPrincipale + " non trovato");
        if (searchResults.getRows().size() != 1)
            throw new DocerException("docId " + docIdPrincipale + " non univoco");

        DataRow<String> profileData = searchResults.getRow(0);

        String statoConserv = profileData.get(Constants.doc_stato_conservazione);

        //STATO_CONSER=CONSERVATO (3)
        if (statoConserv.equalsIgnoreCase("3")) {
            //setta STATO_CONSERV=5 per aggiornamento conservazione
            Map<String, String> meta = new HashMap<String, String>();
            meta.put(Constants.doc_stato_conservazione, "5");
            provider.updateProfileDocument(docIdAllegato, meta);
        }

        //****************************************************************************************
    }

    public List<ISearchItem> getRiferimentiFascicolo(String ticket, Map<String, String> id) throws DocerException {

        try {

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();

            // setto l'utente nel provider
            provider.setCurrentUser(loginUserInfo);

            /* business logic */

            id = toUCMap(id, "id");

            String codEnte = id.get(Constants.fascicolo_cod_ente);
            String codAoo = id.get(Constants.fascicolo_cod_aoo);
            String classifica = id.get(Constants.fascicolo_classifica);
            String progrFascicolo = id.get(Constants.fascicolo_progr_fascicolo);
            String annoFascicolo = id.get(Constants.fascicolo_anno_fascicolo);

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

            if (classifica == null || classifica.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

            if (progrFascicolo == null || progrFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare PROGR_FASCICOLO");

            if (annoFascicolo == null || annoFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.fascicolo_cod_ente, codEnte);
            idCrit.put(Constants.fascicolo_cod_aoo, codAoo);
            idCrit.put(Constants.fascicolo_classifica, classifica);
            idCrit.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);
            idCrit.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;

            String pianoClass = "";
            if(isPianoClassEnabled) {
                pianoClass = id.get(Constants.fascicolo_piano_class);
                if (pianoClass == null) {
                    pianoClass = BL_CONFIGURATIONS.getConfig(entetoken).getStoricoPianoClass(codEnte, codAoo, annoFascicolo);
                    if (pianoClass != null) {
                        idCrit.put("PIANO_CLASS", pianoClass);
                    } else {
                        throw new DocerException("Piano di classificazione non configurato per l'anno " + annoFascicolo);
                    }
                }
            }


            Map<String, String> profile = getAnagraficaProfile(Constants.fascicolo_type_id, idCrit);

            if (profile == null) {
                throw new DocerException("Fascicolo " + idCrit.toString() + " non trovato");
            }


            IFascicoloId fascId = new FascicoloId();
            fascId.setCodiceEnte(codEnte);
            fascId.setCodiceAOO(codAoo);
            fascId.setClassifica(classifica);
            fascId.setProgressivo(progrFascicolo);
            fascId.setAnnoFascicolo(annoFascicolo);
            if (!StringUtils.isEmpty(pianoClass))
                fascId.setPianoClassificazione(pianoClass);

            List<Map<String, String>> riferimenti = provider.getRiferimentiFascicolo(fascId);

            List<ISearchItem> results = new ArrayList<ISearchItem>();

            for (Map<String, String> r : riferimenti) {

                SearchItem si = new SearchItem();

                List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
                for (Entry<String, String> entry : r.entrySet()) {
                    KeyValuePair kvp = new KeyValuePair(entry.getKey(), entry.getValue());
                    kvpList.add(kvp);
                }

                si.setMetadata(kvpList.toArray(new KeyValuePair[0]));

                results.add(si);
            }

            return results;


        } catch (DocerException docEx) {

            throw new DocerException(413, docEx.getMessage());

        } catch (Exception err) {
            throw new DocerException(513, err.getMessage());
        }
    }


    public void addRiferimentiFascicolo(String ticket, Map<String, String> id, List<Map<String, String>> toAddRiferimenti) throws DocerException {

        try {

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();
            provider.setCurrentUser(loginUserInfo);

            /* business logic */

            id = toUCMap(id, "id");

            String codEnte = id.get(Constants.fascicolo_cod_ente);
            String codAoo = id.get(Constants.fascicolo_cod_aoo);
            String classifica = id.get(Constants.fascicolo_classifica);
            String progrFascicolo = id.get(Constants.fascicolo_progr_fascicolo);
            String annoFascicolo = id.get(Constants.fascicolo_anno_fascicolo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

            if (classifica == null || classifica.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

            if (progrFascicolo == null || progrFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare PROGR_FASCICOLO");

            if (annoFascicolo == null || annoFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.fascicolo_cod_ente, codEnte);
            idCrit.put(Constants.fascicolo_cod_aoo, codAoo);
            idCrit.put(Constants.fascicolo_classifica, classifica);
            idCrit.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);
            idCrit.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);

            String pianoClass = "";
            if(isPianoClassEnabled) {
                pianoClass = id.get(Constants.fascicolo_piano_class);
                if (pianoClass == null) {
                    pianoClass = BL_CONFIGURATIONS.getConfig(entetoken).getStoricoPianoClass(codEnte, codAoo, annoFascicolo);
                    if (pianoClass != null) {
                        idCrit.put("PIANO_CLASS", pianoClass);
                    } else {
                        throw new DocerException("Piano di classificazione non configurato per l'anno " + annoFascicolo);
                    }
                }
            }

            Map<String, String> profile = getAnagraficaProfile(Constants.fascicolo_type_id, idCrit);

            if (profile == null) {
                throw new DocerException("Fascicolo " + idCrit.toString() + " non trovato");
            }

            IFascicoloId fascId = new FascicoloId();
            fascId.setCodiceEnte(codEnte);
            fascId.setCodiceAOO(codAoo);
            fascId.setClassifica(classifica);
            fascId.setProgressivo(progrFascicolo);
            fascId.setAnnoFascicolo(annoFascicolo);
            if (!StringUtils.isEmpty(pianoClass))
                fascId.setPianoClassificazione(pianoClass);

            if (toAddRiferimenti == null)
                throw new DocerException("lista Riferimenti null");


            List<IFascicoloId> riferimenti = new ArrayList<IFascicoloId>();

            for(Map<String, String> riferimento : toAddRiferimenti) {
                riferimento = toUCMap(riferimento, "id");

                codEnte = riferimento.get(Constants.fascicolo_cod_ente);
                codAoo = riferimento.get(Constants.fascicolo_cod_aoo);
                classifica = riferimento.get(Constants.fascicolo_classifica);
                progrFascicolo = riferimento.get(Constants.fascicolo_progr_fascicolo);
                annoFascicolo = riferimento.get(Constants.fascicolo_anno_fascicolo);

                if (codEnte == null || codEnte.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

                if (codAoo == null || codAoo.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

                if (classifica == null || classifica.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

                if (progrFascicolo == null || progrFascicolo.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare PROGR_FASCICOLO");

                if (annoFascicolo == null || annoFascicolo.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

                idCrit = new HashMap<String, String>();
                idCrit.put(Constants.fascicolo_cod_ente, codEnte);
                idCrit.put(Constants.fascicolo_cod_aoo, codAoo);
                idCrit.put(Constants.fascicolo_classifica, classifica);
                idCrit.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);
                idCrit.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);

                pianoClass = "";
                if(isPianoClassEnabled) {
                    pianoClass = riferimento.get(Constants.fascicolo_piano_class);
                    if (pianoClass == null) {
                        pianoClass = BL_CONFIGURATIONS.getConfig(entetoken).getStoricoPianoClass(codEnte, codAoo, annoFascicolo);
                        if (pianoClass != null) {
                            idCrit.put("PIANO_CLASS", pianoClass);
                        } else {
                            throw new DocerException("Piano di classificazione non configurato per l'anno " + annoFascicolo);
                        }
                    }
                }

                profile = getAnagraficaProfile(Constants.fascicolo_type_id, idCrit);

                if (profile == null) {
                    throw new DocerException("Fascicolo " + idCrit.toString() + " non trovato");
                }

                IFascicoloId rifFascId = new FascicoloId();
                rifFascId.setCodiceEnte(codEnte);
                rifFascId.setCodiceAOO(codAoo);
                rifFascId.setClassifica(classifica);
                rifFascId.setProgressivo(progrFascicolo);
                rifFascId.setAnnoFascicolo(annoFascicolo);
                if (!StringUtils.isEmpty(pianoClass))
                    rifFascId.setPianoClassificazione(pianoClass);

                riferimenti.add(rifFascId);
            }

            if (riferimenti.size() > 0)
                provider.addRiferimentiFascicolo(fascId, riferimenti);


        } catch (DocerException docEx) {

            throw new DocerException(411, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(511, err.getMessage());
        }

    }


    public void removeRiferimentiFascicolo(String ticket, Map<String, String> id, List<Map<String, String>> toRemoveRiferimenti) throws DocerException {

        try {

            LoggedUserInfo loginUserInfo = parseTicket(ticket);
            entetoken = loginUserInfo.getCodiceEnte();
            provider.setCurrentUser(loginUserInfo);

            /* business logic */
            id = toUCMap(id, "id");

            String codEnte = id.get(Constants.fascicolo_cod_ente);
            String codAoo = id.get(Constants.fascicolo_cod_aoo);
            String classifica = id.get(Constants.fascicolo_classifica);
            String progrFascicolo = id.get(Constants.fascicolo_progr_fascicolo);
            String annoFascicolo = id.get(Constants.fascicolo_anno_fascicolo);

            // DOCER-36 Controllo se devo gestire il Piano Class
            String pianoClassDefault = BL_CONFIGURATIONS.getConfig(entetoken).getPianoClass(codEnte, codAoo);
            boolean isPianoClassEnabled = pianoClassDefault != null;

            if (codEnte == null || codEnte.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

            if (codAoo == null || codAoo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

            if (classifica == null || classifica.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

            if (progrFascicolo == null || progrFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare PROGR_FASCICOLO");

            if (annoFascicolo == null || annoFascicolo.equals(VOID))
                throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

            Map<String, String> idCrit = new HashMap<String, String>();
            idCrit.put(Constants.fascicolo_cod_ente, codEnte);
            idCrit.put(Constants.fascicolo_cod_aoo, codAoo);
            idCrit.put(Constants.fascicolo_classifica, classifica);
            idCrit.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);
            idCrit.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);

            String pianoClass = "";
            if(isPianoClassEnabled) {
                pianoClass = id.get(Constants.fascicolo_piano_class);
                if (pianoClass == null) {
                    pianoClass = BL_CONFIGURATIONS.getConfig(entetoken).getStoricoPianoClass(codEnte, codAoo, annoFascicolo);
                    if (pianoClass != null) {
                        idCrit.put("PIANO_CLASS", pianoClass);
                    } else {
                        throw new DocerException("Piano di classificazione non configurato per l'anno " + annoFascicolo);
                    }
                }
            }

            Map<String, String> profile = getAnagraficaProfile(Constants.fascicolo_type_id, idCrit);

            if (profile == null) {
                throw new DocerException("Fascicolo " + idCrit.toString() + " non trovato");
            }

            IFascicoloId fascId = new FascicoloId();
            fascId.setCodiceEnte(codEnte);
            fascId.setCodiceAOO(codAoo);
            fascId.setClassifica(classifica);
            fascId.setProgressivo(progrFascicolo);
            fascId.setAnnoFascicolo(annoFascicolo);
            if (!StringUtils.isEmpty(pianoClass))
                fascId.setPianoClassificazione(pianoClass);

            if (toRemoveRiferimenti == null)
                throw new DocerException("lista Riferimenti null");


            List<IFascicoloId> riferimenti = new ArrayList<IFascicoloId>();

            for(Map<String, String> riferimento : toRemoveRiferimenti) {
                riferimento = toUCMap(riferimento, "id");

                codEnte = riferimento.get(Constants.fascicolo_cod_ente);
                codAoo = riferimento.get(Constants.fascicolo_cod_aoo);
                classifica = riferimento.get(Constants.fascicolo_classifica);
                progrFascicolo = riferimento.get(Constants.fascicolo_progr_fascicolo);
                annoFascicolo = riferimento.get(Constants.fascicolo_anno_fascicolo);

                if (codEnte == null || codEnte.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare COD_ENTE");

                if (codAoo == null || codAoo.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare COD_AOO");

                if (classifica == null || classifica.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare CLASSIFICA");

                if (progrFascicolo == null || progrFascicolo.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare PROGR_FASCICOLO");

                if (annoFascicolo == null || annoFascicolo.equals(VOID))
                    throw new DocerException("Fascicolo non trovato: specificare ANNO_FASCICOLO");

                idCrit = new HashMap<String, String>();
                idCrit.put(Constants.fascicolo_cod_ente, codEnte);
                idCrit.put(Constants.fascicolo_cod_aoo, codAoo);
                idCrit.put(Constants.fascicolo_classifica, classifica);
                idCrit.put(Constants.fascicolo_anno_fascicolo, annoFascicolo);
                idCrit.put(Constants.fascicolo_progr_fascicolo, progrFascicolo);

                pianoClass = "";
                if(isPianoClassEnabled) {
                    pianoClass = riferimento.get(Constants.fascicolo_piano_class);
                    if (pianoClass == null) {
                        pianoClass = BL_CONFIGURATIONS.getConfig(entetoken).getStoricoPianoClass(codEnte, codAoo, annoFascicolo);
                        if (pianoClass != null) {
                            idCrit.put("PIANO_CLASS", pianoClass);
                        } else {
                            throw new DocerException("Piano di classificazione non configurato per l'anno " + annoFascicolo);
                        }
                    }
                }

                profile = getAnagraficaProfile(Constants.fascicolo_type_id, idCrit);

                if (profile == null) {
                    throw new DocerException("Fascicolo " + idCrit.toString() + " non trovato");
                }

                IFascicoloId rifFascId = new FascicoloId();
                rifFascId.setCodiceEnte(codEnte);
                rifFascId.setCodiceAOO(codAoo);
                rifFascId.setClassifica(classifica);
                rifFascId.setProgressivo(progrFascicolo);
                rifFascId.setAnnoFascicolo(annoFascicolo);
                if (!StringUtils.isEmpty(pianoClass))
                    rifFascId.setPianoClassificazione(pianoClass);

                riferimenti.add(rifFascId);
            }

            if (riferimenti.size() > 0)
                provider.removeRiferimentiFascicolo(fascId, riferimenti);



        } catch (DocerException docEx) {

            throw new DocerException(411, docEx.getMessage());

        } catch (Exception err) {

            throw new DocerException(511, err.getMessage());
        }
    }


}
