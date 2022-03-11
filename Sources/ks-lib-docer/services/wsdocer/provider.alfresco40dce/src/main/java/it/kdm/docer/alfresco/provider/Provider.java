package it.kdm.docer.alfresco.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.alfresco.webservice.accesscontrol.ACE;
import org.alfresco.webservice.accesscontrol.AccessControlFault;
import org.alfresco.webservice.accesscontrol.AccessStatus;
import org.alfresco.webservice.accesscontrol.NewAuthority;
import org.alfresco.webservice.accesscontrol.SiblingAuthorityFilter;
import org.alfresco.webservice.administration.AdministrationFault;
import org.alfresco.webservice.administration.NewUserDetails;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.authentication.AuthenticationFault;
import org.alfresco.webservice.authentication.AuthenticationResult;
import org.alfresco.webservice.authentication.AuthenticationServiceSoapBindingStub;
import org.alfresco.webservice.authoring.AuthoringFault;
import org.alfresco.webservice.authoring.LockTypeEnum;
import org.alfresco.webservice.authoring.VersionResult;
import org.alfresco.webservice.dictionary.ClassPredicate;
import org.alfresco.webservice.dictionary.DictionaryFault;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.CMLCreateAssociation;
import org.alfresco.webservice.types.CMLDelete;
import org.alfresco.webservice.types.CMLMove;
import org.alfresco.webservice.types.CMLRemoveAssociation;
import org.alfresco.webservice.types.CMLUpdate;
import org.alfresco.webservice.types.ClassDefinition;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.PropertyDefinition;
import org.alfresco.webservice.types.Query;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.types.Version;
import org.alfresco.webservice.types.VersionHistory;
import org.alfresco.webservice.util.AuthenticationDetails;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.ISO9075;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import it.kdm.docer.alfresco.model.DocerModel;
import it.kdm.docer.alfresco.provider.bo.LoginBO;
import it.kdm.docer.alfresco.search.DocId;
import it.kdm.docer.alfresco.search.LuceneUtility;
import it.kdm.docer.alfresco.search.SearchResult;
import it.kdm.docer.alfresco.search.SearchResultSet;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.IProvider;
import it.kdm.docer.sdk.classes.CustomItemId;
import it.kdm.docer.sdk.classes.GroupInfo;
import it.kdm.docer.sdk.classes.GroupProfileInfo;
import it.kdm.docer.sdk.classes.LockStatus;
import it.kdm.docer.sdk.classes.LoggedUserInfo;
import it.kdm.docer.sdk.classes.UserInfo;
import it.kdm.docer.sdk.classes.UserProfileInfo;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.IAOOId;
import it.kdm.docer.sdk.interfaces.IAOOInfo;
import it.kdm.docer.sdk.interfaces.ICustomItemId;
import it.kdm.docer.sdk.interfaces.ICustomItemInfo;
import it.kdm.docer.sdk.interfaces.IEnteId;
import it.kdm.docer.sdk.interfaces.IEnteInfo;
import it.kdm.docer.sdk.interfaces.IFascicoloId;
import it.kdm.docer.sdk.interfaces.IFascicoloInfo;
import it.kdm.docer.sdk.interfaces.IFolderInfo;
import it.kdm.docer.sdk.interfaces.IGroupInfo;
import it.kdm.docer.sdk.interfaces.IGroupProfileInfo;
import it.kdm.docer.sdk.interfaces.IHistoryItem;
import it.kdm.docer.sdk.interfaces.ILockStatus;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import it.kdm.docer.sdk.interfaces.ISsoUserInfo;
import it.kdm.docer.sdk.interfaces.ITitolarioId;
import it.kdm.docer.sdk.interfaces.ITitolarioInfo;
import it.kdm.docer.sdk.interfaces.IUserInfo;
import it.kdm.docer.sdk.interfaces.IUserProfileInfo;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;

public class Provider implements IProvider {

    private static Map<String, String> USERS_MAP = new HashMap<String, String>();
    private static final char ESCAPE_CHAR = '%';
    private static Pattern extensionPattern = Pattern.compile("^.+\\.([0-9a-zA-Z]+)$");
    private final static String REGEX_NODENAME_1 = "[\\\"\\*\\>\\<\\?\\/\\:\\|\\\\]";
    private final static String REGEX_NODENAME_2 = "^ *\\.+ *";
    private final static String REGEX_NODENAME_3 = " *\\.+ *$";
    private final static String REPLACEWITH_NODENAME = "_";

    private static Map<String, Reference> REPO_CACHE = new HashMap<String, Reference>();
    private static Map<String, Reference> ENTE_NODE_CACHE = new HashMap<String, Reference>();
    private static Map<String, Reference> AOO_NODE_CACHE = new HashMap<String, Reference>();

    // private static final String PATH = "PATH";
    // private static final String INHERITS_ACL = "INHERITS_ACL";
    public static final String HAS_TILDE = "HAS_TILDE";
    public static final String DOCUMENTI_REPO = "Temporanei";
    public static final String CARTELLE_REPO = "Cartelle";

    private static String regexpSize = ".*\\|size\\=([^\\|]*)\\|.*";
    private static Pattern patternSize = Pattern.compile(regexpSize);

    private static WorkQueue wq = null;
    private static int POOL_THREADS = 25;
    private static boolean defaultInheritsACL;

    ILoggedUserInfo currentUser = null;

    private static String SYS_NODE_UUID = "sys:node-uuid";
    private static String CM_NAME = "cm:name";
    private static String CM_CONTENT = "cm:content";
    private static String DOCAREA_DOCNUM = "docarea:docnum";

    private static String DOCAREA_FOLDERID = "docarea:folderId";

    private static boolean QUERY_DEBUG = false;
    private static String ALFRESCO_END_POINT_ADDRESS = null;
    private static int SEARCH_MAX_RESULTS = 1000;
    private static int ALFRESCO_TIMEOUT_MILLIS = 60000;
    private static long MIGRATION_DOCNUM_OFFSET = 0;
    private static String ADMIN_USERID = "admin";

    // METAMAPPING esegue la mappatura tra i nomi delle proprieta'
    // e' possibile recuperare una prop o dal nome di Business Logic o dal nome
    // URI Alfresco o dal short name Alfresco
    private static MetaMap METAMAPPING = new MetaMap();
    private static MetaMap METAMAPPING_USER = new MetaMap();
    private static MetaMap METAMAPPING_GROUP = new MetaMap();
    private static Map<String, CustomItemProps> CUSTOM_ITEM_MAPPING = new HashMap<String, CustomItemProps>();

    // The security token could not be authenticated or authorized
    private static final String UNAUTH_SESSION_MESSAGE = "the security token";
    private static final String UNAUTH_SESSION_ERROR = "ticket non valido o utente non autorizzato";

    // private static List<String> INTERNAL_GROUPS = null;

    private static String DEFAULT_LIBRARY = "DOCAREA";
    private static Map<String, String> LIBRARY_MAPPING = new HashMap<String, String>();

    // formato dei nomi dei gruppi AOO {codAoo}-{codEnte}
    private static String AOO_GROUPID_FORMAT = "{codAoo}";
    private static String AOO_GROUPID_REGEX_AOO = "\\{codAoo\\}";
    private static String AOO_GROUPID_REGEX_ENTE = "\\{codEnte\\}";

    private static String ENTE_ADMINS_GROUPID_PREFIX = "ADMINS_ENTE_";
    private static String AOO_ADMINS_GROUPID_PREFIX = "ADMINS_AOO_";

    private static String SYS_ADMINS_GROUPID = "SYS_ADMINS";

    private static String ALFRESCO_ADMINISTRATORS = "ALFRESCO_ADMINISTRATORS";
    private static String GROUP_EVERYONE = "GROUP_EVERYONE";

    private static Map<String, String> DOCUMENT_TYPE_MAPPING = new HashMap<String, String>();

    private static Map<String, Map<String, EnumSearchOrder>> orderbySearchAnagrafiche = new HashMap<String, Map<String, EnumSearchOrder>>();

    protected static boolean DOCUMENTO_EREDITA_ACL_FASCICOLO = false;
    protected static boolean DOCUMENTO_EREDITA_ACL_FOLDER = false;

    private static String ORDERBY_ALFRESCO_FIELD_TYPE = "PREFIX";

    private static boolean ORDERBY_ENABLED = false;

    // private Map<String, String> DOCNUM_CACHE = new HashMap<String, String>();
    // private Map<String, String> FASCICOLI_CACHE = new HashMap<String,
    // String>();
    // private static Map<String, String> REPOSITORY_CACHE = new HashMap<String,
    // String>();

    private static String ALFRESCO_PROVIDER_CONFIG_XML_PATH = null;

    private static Map<String, PropertyDefinition> ALFRESCO_PROPERTIES_MAP = new HashMap<String, PropertyDefinition>();

    private static long lastModified = 0;


    private static boolean CIFS = false;

    // private String getDigest(byte[] config) {
    // return DigestUtils.md5Hex(config);
    // }

    private static Properties loadProperties(byte[] config) throws IOException {
        Properties p = new Properties();
        p.loadFromXML(new ByteArrayInputStream(config));
        return p;
    }

    private static boolean initialised = false;

    public Provider() throws DocerException {

      
        Properties props = null;
        try {
        	
        	ALFRESCO_PROVIDER_CONFIG_XML_PATH = ConfigurationUtils.loadProperties("alfresco_provider_config.properties").getProperty("config.file.path");

            if (StringUtils.isEmpty(ALFRESCO_PROVIDER_CONFIG_XML_PATH)) {
                System.out.println("WARNING: variabile config.file.path vuota o non trovata nel file /alfresco_provider_config.properties");
                ALFRESCO_PROVIDER_CONFIG_XML_PATH = "";
            }

        } catch (Exception e) {
            ALFRESCO_PROVIDER_CONFIG_XML_PATH = "";
            System.out.println("WARNING: errore inizializzazione Provider Alfresco: lettura file alfresco_provider_config.properties: " + e.getMessage());
        } 

        InputStream cis = null;
        try {

            if (StringUtils.isNotEmpty(ALFRESCO_PROVIDER_CONFIG_XML_PATH)) {
                File f = new File(ALFRESCO_PROVIDER_CONFIG_XML_PATH);

                if (!f.exists()) {
                    System.out.println("WARNING: config file non trovato: " + f.getAbsolutePath());
                    initialised = false;
                } else {
                    if (initialised && f.lastModified() == lastModified) {
                        return;
                    }

                    lastModified = f.lastModified();

                    byte[] alfrescoProviderConfig = FileUtils.readFileToByteArray(f);
                    props = loadProperties(alfrescoProviderConfig);
                    initialised = false;
                }
            }

            if (initialised) {
                return;
            }

            if (props == null) {
                props = ConfigurationUtils.loadProperties("alfresco_provider_config.xml");
            }

            QUERY_DEBUG = Boolean.valueOf(props.getProperty("QUERY_DEBUG"));
            CIFS = Boolean.valueOf(props.getProperty("CIFS"));

            ALFRESCO_END_POINT_ADDRESS = props.getProperty("ALFRESCO_END_POINT_ADDRESS");

            try {
                SEARCH_MAX_RESULTS = Integer.parseInt(props.getProperty("SEARCH_MAX_RESULTS"));
            } catch (NumberFormatException nfe) {
                System.out.println("WARNING: la chiave SEARCH_MAX_RESULTS deve essere un Integer");
            }

            try {
                ALFRESCO_TIMEOUT_MILLIS = Integer.parseInt(props.getProperty("ALFRESCO_TIMEOUT_MILLIS"));
            } catch (NumberFormatException nfe) {
                System.out.println("WARNING: la chiave ALFRESCO_TIMEOUT_MILLIS deve essere un integer");
            }

            try {
                MIGRATION_DOCNUM_OFFSET = Long.parseLong(props.getProperty("MIGRATION_DOCNUM_OFFSET"));
            } catch (NumberFormatException nfe) {
                throw new InvalidPropertiesFormatException("la chiave MIGRATION_DOCNUM_OFFSET deve essere un long");
            }

            ADMIN_USERID = props.getProperty("ADMIN_USERID");

            // formato di generazione dei group id delle AOO
            AOO_GROUPID_FORMAT = props.getProperty("AOO_GROUPID_FORMAT");

            ENTE_ADMINS_GROUPID_PREFIX = props.getProperty("ENTE_ADMINS_GROUPID_PREFIX");
            AOO_ADMINS_GROUPID_PREFIX = props.getProperty("AOO_ADMINS_GROUPID_PREFIX");

            SYS_ADMINS_GROUPID = props.getProperty("SYS_ADMINS_GROUPID");

            String defaultInherits = props.getProperty("DEFAULT_INHERITS_ACL");
            if (StringUtils.isNotEmpty(defaultInherits) && defaultInherits.equalsIgnoreCase("true")) {
                defaultInheritsACL = true;
            } else {
                defaultInheritsACL = false;
            }

            DOCUMENT_TYPE_MAPPING.clear();
            String dtm = props.getProperty("DOCUMENT_TYPE_MAPPING");
            if (dtm != null) {
                String[] mappingArr = dtm.split(" *; *");

                for (String mapping : mappingArr) {
                    if (mapping.equals("")) {
                        continue;
                    }
                    if (mapping.matches(".+=.+")) {
                        String[] keyvalue = mapping.split(" *= *");
                        DOCUMENT_TYPE_MAPPING.put(keyvalue[0], keyvalue[1]);
                    }
                }
            }

            // <entry
            // key="LIBRARY_MAPPING">default=docarea;docarea=ENTE1,ENTEX;libraryx=ENTE2,ENTEN;</entry>
            LIBRARY_MAPPING.clear();
            String libmapping = props.getProperty("LIBRARY_MAPPING");
            if (libmapping != null) {
                String[] mappingArr = libmapping.split(" *; *");

                for (String mapping : mappingArr) {
                    if (mapping.equals("")) {
                        continue;
                    }

                    if (mapping.matches("[^\\=]+=[^\\=]+")) {
                        String[] keyvalue = mapping.split(" *= *");

                        String libraryName = keyvalue[0];
                        String[] codiciEnte = keyvalue[1].split(" *, *");
                        if (libraryName.equalsIgnoreCase("default")) {
                            DEFAULT_LIBRARY = codiciEnte[0].toUpperCase();
                            continue;
                        }

                        for (String codEnte : codiciEnte) {
                            LIBRARY_MAPPING.put(codEnte.toUpperCase(), libraryName.toUpperCase());
                        }

                    }
                }
            }

            // <entry
            // key="ORDERBY_SEARCH_ANAGRAFICHE">ENTE:COD_ENTE=ASC,DES_ENTE=DESC;AOO:COD_ENTE=ASC,COD_AOO=ASC;TITOLARIO:COD_ENTE=ASC,COD_AOO=ASC,CLASSIFICA=ASC;FASCICOLO:ANNO_FASCICOLO=DESC,PROGR_FASCICOLO=ASC</entry>
            orderbySearchAnagrafiche.clear();
            String obsa = props.getProperty("ORDERBY_SEARCH_ANAGRAFICHE");
            if (obsa != null) {
                String[] anatypes = obsa.split(" *; *");

                // ciclo i singoli order by per anagrafica
                for (String anatype : anatypes) {
                    String[] ana = anatype.split(" *: *");

                    if (ana.length != 2)
                        continue;

                    Map<String, EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();

                    String[] singleOrderby = ana[1].split(" *, *");
                    for (String so : singleOrderby) {
                        String[] propAndOrder = so.split(" *= *");
                        if (propAndOrder.length != 2)
                            continue;
                        if (propAndOrder[1].equalsIgnoreCase("ASC")) {
                            orderby.put(propAndOrder[0].toUpperCase(), EnumSearchOrder.ASC);
                            continue;
                        }
                        if (propAndOrder[1].equalsIgnoreCase("DESC")) {
                            orderby.put(propAndOrder[0].toUpperCase(), EnumSearchOrder.DESC);
                            continue;
                        }

                    }

                    if (orderby.size() > 0) {
                        orderbySearchAnagrafiche.put(ana[0].toUpperCase(), orderby);
                    }

                }

            }

            ORDERBY_ENABLED = Boolean.valueOf(props.getProperty("ORDERBY_ENABLED"));

            String orderbyAlfrescoFieldType = props.getProperty("ORDERBY_ALFRESCO_FIELD_TYPE");
            if (orderbyAlfrescoFieldType != null)
                ORDERBY_ALFRESCO_FIELD_TYPE = props.getProperty("ORDERBY_ALFRESCO_FIELD_TYPE");

            String val = props.getProperty("DOCUMENTO_EREDITA_ACL_FASCICOLO");
            if (val != null)
                DOCUMENTO_EREDITA_ACL_FASCICOLO = Boolean.parseBoolean(props.getProperty("DOCUMENTO_EREDITA_ACL_FASCICOLO"));

            val = props.getProperty("DOCUMENTO_EREDITA_ACL_FOLDER");
            if (val != null)
                DOCUMENTO_EREDITA_ACL_FOLDER = Boolean.parseBoolean(props.getProperty("DOCUMENTO_EREDITA_ACL_FOLDER"));

            REPO_CACHE.clear();
            ENTE_NODE_CACHE.clear();
            AOO_NODE_CACHE.clear();

            ALFRESCO_PROPERTIES_MAP.clear();
            METAMAPPING.clear();
            // METAMAPPING_GROUP.clear();
            // METAMAPPING_USER.clear();
            CUSTOM_ITEM_MAPPING.clear();

            for (Object obj : props.keySet()) {
                String key = (String) obj;
                String value = (String) props.get(obj);

                if (key.startsWith("#")) {

                    MetaProperty m = new MetaProperty(key, value);

                    if (m.isValid())
                        METAMAPPING.putMetaProperty(m);

                    continue;
                }

                if (key.startsWith("CUSTOM_ITEM_")) {

                    String[] customProps = value.split("[=;,]");

                    CustomItemProps cProps = new CustomItemProps();

                    for (int i = 0; i < customProps.length; i++) {
                        if (customProps[i] == null)
                            continue;
                        if (cProps.getBlType().equals("")) {
                            cProps.setBlType(customProps[i]);
                            continue;
                        }
                        if (cProps.getAlfType().equals("")) {
                            cProps.setAlfType(customProps[i]);
                            continue;
                        }
                        if (cProps.getBlCodicePropName().equals("")) {
                            cProps.setBlCodicePropName(customProps[i]);
                            continue;
                        }
                        if (cProps.getBlDescrizionePropName().equals("")) {
                            cProps.setBlDescrizionePropName(customProps[i]);
                            continue;
                        }

                    }

                    if (cProps.getBlType().equals("") || cProps.getAlfType().equals("") || cProps.getBlCodicePropName().equals("") || cProps.getBlDescrizionePropName().equals(""))
                        continue;

                    // aggiungo al mapping solo se correttamente configurata
                    CUSTOM_ITEM_MAPPING.put(cProps.getBlType(), cProps);
                }

            }

            if (METAMAPPING_USER.getAllMetaProperties().size() == 0) {
                METAMAPPING_USER.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.user_user_id, DocerModel.PROP_USER_USERID));
                METAMAPPING_USER.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.user_full_name, DocerModel.PROP_USER_FULLNAME));
                METAMAPPING_USER.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.user_first_name, DocerModel.PROP_USER_FIRSTNAME));
                METAMAPPING_USER.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.user_last_name, DocerModel.PROP_USER_LASTNAME));
                METAMAPPING_USER.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.user_email_address, DocerModel.PROP_USER_MAIL));
                METAMAPPING_USER.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.user_enabled, DocerModel.PROP_ENABLED));
                METAMAPPING_USER.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.user_network_alias, DocerModel.PROP_USER_NETWORK_ALIAS));
                METAMAPPING_USER.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.user_cod_ente, DocerModel.PROP_USER_COD_ENTE));
                METAMAPPING_USER.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.user_cod_aoo, DocerModel.PROP_USER_COD_AOO));
                METAMAPPING_USER.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.user_cod_fiscale, DocerModel.PROP_USER_COD_FISCALE));
                // test per mappare altre props
                // METAMAPPING_USER.putMetaProperty(METAMAPPING.getMetaPropertyFromBusinessLogicName("DES_ENTE"));
            }

            if (METAMAPPING_GROUP.getAllMetaProperties().size() == 0) {
                METAMAPPING_GROUP.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.group_group_id, DocerModel.PROP_GROUP_GROUPID));
                METAMAPPING_GROUP.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.group_group_name, DocerModel.PROP_GROUP_GROUPNAME));
                METAMAPPING_GROUP.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.group_parent_group_id, DocerModel.PROP_GROUP_PARENTGROUPID));
                METAMAPPING_GROUP.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.group_gruppo_struttura, DocerModel.PROP_GROUP_GRUPPOSTRUTTURA));
                METAMAPPING_GROUP.putMetaProperty(new MetaProperty(it.kdm.docer.sdk.Constants.group_enabled, DocerModel.PROP_ENABLED));
                // test per mappare altre props
                // METAMAPPING_GROUP.putMetaProperty(METAMAPPING.getMetaPropertyFromBusinessLogicName("DES_ENTE"));
            }
            int newPoolThread = POOL_THREADS;
            try {
                newPoolThread = Integer.parseInt(props.getProperty("POOL_THREADS"));
            } catch (Exception e) {
                System.out.println("la chiave POOL_THREADS deve essere un intero; valore in configurazione: " + props.getProperty("POOL_THREADS"));
            }

            if (newPoolThread != POOL_THREADS) {
                POOL_THREADS = newPoolThread;
                wq = null;
            }

            if (wq == null) {
                wq = new WorkQueue(POOL_THREADS);
            }

            WebServiceFactory.setEndpointAddress(ALFRESCO_END_POINT_ADDRESS);
            WebServiceFactory.setTimeoutMilliseconds(ALFRESCO_TIMEOUT_MILLIS);

            LuceneUtility.initialize(QUERY_DEBUG, METAMAPPING, METAMAPPING_USER, METAMAPPING_GROUP, ORDERBY_ALFRESCO_FIELD_TYPE);
            initialised = true;
            System.out.println("provider alfresco inizializzato");
        } catch (InvalidPropertiesFormatException e) {
            initialised = false;
            throw new DocerException(-1194, "InvalidPropertiesFormatException lettura file /alfresco_provider_config.xml:" + e.getMessage());
        } catch (IOException e) {
            initialised = false;
            throw new DocerException(-1194, "IOException lettura file /alfresco_provider_config.xml:" + e.getMessage());
        }

    }

    // ***** METODI PUBBLICI *******

    public EnumACLRights getEffectiveRightsFolder(String folderId, String userid) throws DocerException {

        String metodo = "getEffectiveRightsFolder";
        String rif = folderId;

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        try {

            if (StringUtils.isEmpty(folderId)) {
                throw new DocerException("folderId obbligatorio");
            }

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

            String lucenequery = LuceneUtility.buildAlfrescoFoldersQueryString(searchCriteria, true);

            SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, null, -1, userid, false, false, false, false, false, true, null);

            if (webScriptResults.getCount() == 0) {
                return EnumACLRights.undefined;
            }

            if (webScriptResults.getCount() > 1) {
                throw new DocerException("trovati " + webScriptResults.getCount() + " risultati");
            }

            SearchResult sr = webScriptResults.getFirstAndOnly();

            return sr.getEffectiveRights();
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public EnumACLRights getEffectiveRightsAnagrafiche(String type, Map<String, String> id, String userid) throws DocerException {

        String metodo = "getEffectiveRightsAnagrafiche";

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        try {
            if (type == null || type.equals(""))
                throw new DocerException("type (tipo anagrafica) obbligatorio");

            return getAlfrescoEffectiveRightsAnagrafica(type, id, userid);
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    public void deleteFolder(String folderId) throws DocerException {

        String metodo = "deleteFolder";
        String rif = folderId;

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        try {

            MetaProperty mp = METAMAPPING.getMetaPropertyFromBusinessLogicName(it.kdm.docer.sdk.Constants.folder_folder_id);
            if (mp == null) {
                throw new DocerException("il metadato " + it.kdm.docer.sdk.Constants.folder_folder_id + " non e' mappato in configurazione");
            }

            SearchResultSet folderSearchResult = getFolderFullProfile(folderId);

            if (folderSearchResult.getCount() == 0) {

                // vediamo se e' nel cestino (possono esserci piu' di una con
                // stesso folderId)
                List<Reference> deletedList = getFolderFromBin(folderId);

                if (deletedList == null) {
                    throw new DocerException("Folder non trovata");
                }

                // altrimenti la cancello dal cestino
                for (Reference ref : deletedList) {
                    deleteFromBin(ref);
                }

                return;
            }

            if (folderSearchResult.getCount() > 1)
                throw new DocerException("trovate " + folderSearchResult.getCount() + " folder con folderId: " + folderId);

            SearchResult sr = folderSearchResult.getFirstAndOnly();

            EnumACLRights effectiverights = sr.getEffectiveRights();
            if (!effectiverights.equals(EnumACLRights.fullAccess))
                throw new DocerException("l'utente corrente non possiede diritti sufficienti sulla Folder " + folderId);

            int count = countContainedFolderAndDocumentsAsAdmin(sr.getReference());

            if (count > 0) {
                throw new DocerException("la Folder non e' vuota");
            }

            CMLDelete cmlDelete = new CMLDelete();
            Predicate delPredicate = createPredicate(sr.getReference());
            cmlDelete.setWhere(delPredicate);

            CML cml = new CML();
            cml.setDelete(new CMLDelete[]{cmlDelete});

            UpdateResult[] updateResult = getAlfrescoRepositoryService_update(cml);

            if (updateResult.length < 1)
                throw new DocerException("errore delete generico");

            // CANCELLO DAL CESTINO
            deleteFromBin(updateResult[0].getSource());
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public void setACLFolder(String folderId, Map<String, EnumACLRights> acls) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "setACLFolder";
        String rif = folderId;

        // if (acls == null || acls.size() == 0)
        // return;

        try {
            SearchResultSet folderSearchResult = getFolderFullProfile(folderId);

            if (folderSearchResult.getCount() == 0)
                throw new DocerException("Folder " + folderId + " non trovata");

            if (folderSearchResult.getCount() > 1)
                throw new DocerException("Folder " + folderId + ": trovate " + folderSearchResult.getCount() + " folder");

            SearchResult sr = folderSearchResult.getFirstAndOnly();

            EnumACLRights effectiverights = sr.getEffectiveRights();

            // String author = getElementPropertyValue("cm:creator",
            // sr.getAllResultsOrderedMap().get(0));

            if (!effectiverights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti sulla Folder " + folderId);
            }

            // recupero gli id dei gruppi
            List<String> groupsList = getCaseSensitiveGroupsIds(new ArrayList<String>(acls.keySet()));

            // recupero gli id degli utenti (Alfresco e' CASE SENSITIVE in
            // assegnazione ACL)
            List<String> usersList = getCaseSensitiveUsersIds(new ArrayList<String>(acls.keySet()));

            List<ACE> addACEList = new ArrayList<ACE>();

            String authority = "";
            String alfRight = null;
            boolean isGroup = true;

            for (String newKey : acls.keySet()) {

                isGroup = true;

                // cerco tra i gruppi
                authority = findGroupId(groupsList, newKey);

                // se non e' tra i gruppi cerco tra gli utenti
                if (authority == null) {
                    isGroup = false;
                    // cerco tra gli utenti
                    authority = findUserId(usersList, newKey);
                    if (authority == null)
                        throw new DocerException(newKey + ": utente o gruppo non definito");
                }

                alfRight = convertToAlfrescoRights(acls.get(newKey));
                if (alfRight == null)
                    continue;

                // assegnamo la ACE
                ACE addACE = new ACE();

                if (isGroup)
                    addACE.setAuthority(Constants.GROUP_PREFIX + authority);
                else
                    addACE.setAuthority(authority);

                addACE.setPermission(alfRight);
                addACE.setAccessStatus(AccessStatus.acepted);
                addACEList.add(addACE);
            }

            Predicate predicate = createPredicate(sr.getReference());

            boolean isPublic = false;
            String folderOwner = sr.getProfile().get(it.kdm.docer.sdk.Constants.folder_owner);
            if (folderOwner.equals("")) {
                isPublic = true;
            }

            setACESRunasAdmin(sr.getReference(), addACEList, null);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public Map<String, EnumACLRights> getACLFolder(String folderId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "getACLFolder";
        String rif = folderId;

        try {
            SearchResultSet folderSearchResult = getFolderFullProfile(folderId);

            if (folderSearchResult.getCount() == 0)
                throw new DocerException("Folder " + folderId + " non trovata");

            if (folderSearchResult.getCount() > 1)
                throw new DocerException("trovate " + folderSearchResult.getCount() + " folder");

            SearchResult sr = folderSearchResult.getFirstAndOnly();

            Map<String, EnumACLRights> alfACLList = sr.getAcl();

            Map<String, EnumACLRights> acl = new HashMap<String, EnumACLRights>();

            for (String id : alfACLList.keySet()) {
                acl.put(id.replaceAll("^" + Constants.GROUP_PREFIX, ""), alfACLList.get(id));
            }

            return acl;

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public List<String> getFolderDocuments(String folderId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "getFolderDocuments";
        String rif = folderId;

        try {

            SearchResultSet folderSearchResult = getFolderFullProfile(folderId);

            if (folderSearchResult.getCount() == 0)
                throw new DocerException("Folder " + folderId + " non trovata");

            if (folderSearchResult.getCount() > 1)
                throw new DocerException("trovate " + folderSearchResult.getCount() + " folder");

            SearchResult srFolder = folderSearchResult.getFirstAndOnly();

            String lucenequery = DocerModel.SEARCH_QUERY_DOCER_FOLDER_DOCUMENTS;
            lucenequery = lucenequery.replace(DocerModel.NODE_UUID_REPLACER, srFolder.getReference().getUuid());

            Map<String, EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
            orderby.put(it.kdm.docer.sdk.Constants.doc_docnum, EnumSearchOrder.ASC);

            SearchResultSet documentsSearchResult = webScriptSearch(false, lucenequery, null, -1, null, false, false, false, false, false, false, orderby);

            List<String> docids = new ArrayList<String>();
            for (SearchResult srDoc : documentsSearchResult.getAllSearchResult()) {
                docids.add(srDoc.getDocId());
            }

            return docids;
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public void addToFolderDocuments(String folderId, List<String> documents) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "addToFolderDocuments";
        String rif = folderId;

        try {
            SearchResultSet folderSearchResult = getFolderFullProfile(folderId);

            if (folderSearchResult.getCount() == 0)
                throw new DocerException("Folder " + folderId + " non trovata");

            if (folderSearchResult.getCount() > 1)
                throw new DocerException("trovate " + folderSearchResult.getCount() + " folder");

            SearchResult srFolder = folderSearchResult.getFirstAndOnly();

            String codEnte = srFolder.getProfile().get(it.kdm.docer.sdk.Constants.folder_cod_ente);
            String codAoo = srFolder.getProfile().get(it.kdm.docer.sdk.Constants.folder_cod_aoo);

            EnumACLRights folderEffectiverights = srFolder.getEffectiveRights();

            if (!folderEffectiverights.equals(EnumACLRights.normalAccess) && !folderEffectiverights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti sulla Folder " + folderId);
            }

            if (documents == null || documents.size() == 0)
                return;

            List<String> toAddDocs = new ArrayList<String>();
            for (String id : documents) {
                if (toAddDocs.contains(id))
                    continue;
                toAddDocs.add(id);
            }

            // DataRow<String> folderProfile = srFolder.getProfile();

            // String folderOwner =
            // folderProfile.get(it.kdm.docer.sdk.Constants.folder_owner);
            // boolean isPublicFolder = (folderOwner != null &&
            // folderOwner.equals(""));

            MetaProperty mpParentFolderId = METAMAPPING.getMetaPropertyFromBusinessLogicName(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
            if (mpParentFolderId == null) {
                throw new DocerException("il metadato " + it.kdm.docer.sdk.Constants.folder_parent_folder_id + " non e' mappato in configurazione");
            }

            String lucenequery = "";
            for (String docid : toAddDocs) {
                lucenequery += "@docarea\\:docnum:\"" + docid + "\" ";
            }

            SearchResultSet documentsSearchResult = webScriptSearch(false, lucenequery,
                    Arrays.asList(it.kdm.docer.sdk.Constants.doc_docnum, it.kdm.docer.sdk.Constants.doc_docname, it.kdm.docer.sdk.Constants.doc_cod_ente, it.kdm.docer.sdk.Constants.doc_cod_aoo), -1,
                    null, false, false, false, false, true, true, null);

            // controllo presenza e diritti
            for (SearchResult srDoc : documentsSearchResult.getAllSearchResult()) {

                EnumACLRights documentEffectiveRights = srDoc.getEffectiveRights();
                if (!documentEffectiveRights.equals(EnumACLRights.fullAccess)) {
                    throw new DocerException("l'utente corrente non possiede diritti sufficienti sul documento " + srDoc.getDocId());
                }

                toAddDocs.remove(srDoc.getDocId());
            }

            if (toAddDocs.size() > 0) {
                throw new DocerException("addToFolderDocuments: documenti " + toAddDocs + " non trovati");
            }

            for (SearchResult srDoc : documentsSearchResult.getAllSearchResult()) {

                String docEnte = srDoc.getProfile().get(it.kdm.docer.sdk.Constants.doc_cod_ente);
                String docAoo = srDoc.getProfile().get(it.kdm.docer.sdk.Constants.doc_cod_aoo);

                if (!docEnte.equals(codEnte) || !docAoo.equals(codAoo)) {
                    throw new DocerException("Impossibile spostare un documento con ente o aoo diversi dalla folder di destinazione");
                }

                Predicate predicateDoc = createPredicate(srDoc.getReference());

                String oldName = srDoc.getProfile().get(it.kdm.docer.sdk.Constants.doc_docname);
                oldName = removeAffix(oldName, String.format("~%s", srDoc.getDocId()));

                // TODO: SCOMMENTARE quando attiveremo ereditarieta' multipla
                // propagateACL(srDoc.getDocId());
                // TODO: gestire inherits con default e owner (privata o meno)
                // CONTROLLARE VALORE old INHERITS_ACL
                // infine sposto fisicamente il documento
                moveToSpaceRunasAdmin(srDoc.getReference().getUuid(), srFolder.getReference().getUuid(), null, oldName);
            }

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public void removeFromFolderDocuments(String folderId, List<String> documents) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "removeFromFolderDocuments";
        String rif = folderId;

        try {

            SearchResultSet folderSearchResult = getFolderFullProfile(folderId);

            if (folderSearchResult.getCount() == 0)
                throw new DocerException("Folder " + folderId + " non trovata");

            if (folderSearchResult.getCount() > 1)
                throw new DocerException("trovate " + folderSearchResult.getCount() + " folder");

            SearchResult srFolder = folderSearchResult.getFirstAndOnly();

            EnumACLRights effectiverights = srFolder.getEffectiveRights();

            if (!effectiverights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti sulla Folder " + folderId);
            }

            // DataRow<String> folderProfile = srFolder.getProfile();

            // String lucenequery = SEARCH_DOCUMENT_QUERY
            // + " +PARENT:\"workspace://SpacesStore/"
            // + srFolder.getReference().getUuid() + "\"";

            String lucenequery = DocerModel.SEARCH_QUERY_DOCER_FOLDER_DOCUMENTS;
            lucenequery = lucenequery.replace(DocerModel.NODE_UUID_REPLACER, srFolder.getReference().getUuid());

            List<String> documentsToRemove = new ArrayList<String>();
            for (String toremove : documents) {
                if (documentsToRemove.contains(toremove))
                    continue;

                documentsToRemove.add(toremove);
            }

            List<String> documentsContained = new ArrayList<String>();

            SearchResultSet documentsSearchResult = webScriptSearch(false, lucenequery, Arrays.asList(it.kdm.docer.sdk.Constants.doc_docnum, it.kdm.docer.sdk.Constants.doc_docname), -1, null, false,
                    false, false, false, true, true, null);

            for (SearchResult srToRemoveDoc : documentsSearchResult.getAllSearchResult()) {

                // controllo solo i documenti da rimuovere
                if (documents.contains(srToRemoveDoc.getDocId())) {

                    if (documentsContained.contains(srToRemoveDoc.getDocId())) {
                        continue;
                    }

                    EnumACLRights documentEffectiveRights = srToRemoveDoc.getEffectiveRights();
                    // il diritto deve essere fullAccess sul documento per la
                    // gestione dell'inherits
                    if (!documentEffectiveRights.equals(EnumACLRights.fullAccess)) {
                        throw new DocerException("l'utente corrente non possiede diritti sufficienti sul documento " + srToRemoveDoc.getDocId());
                    }

                    documentsContained.add(srToRemoveDoc.getDocId());
                }
            }

            String codiceEnte = srFolder.getProfile().get(it.kdm.docer.sdk.Constants.folder_cod_ente);
            String codiceAOO = srFolder.getProfile().get(it.kdm.docer.sdk.Constants.folder_cod_aoo);

            Reference repositoryRef = getRepositoryFolderReference(codiceEnte, codiceAOO, new DateTime());

            MetaProperty mpParentFolderId = METAMAPPING.getMetaPropertyFromBusinessLogicName(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
            if (mpParentFolderId == null) {
                throw new DocerException("il metadato " + it.kdm.docer.sdk.Constants.folder_parent_folder_id + " non e' mappato in configurazione");
            }

            for (String toRemoveDocId : documentsContained) {

                SearchResult srDoc = documentsSearchResult.get(new DocId(toRemoveDocId));

                // predicato del documento
                Predicate predicateDoc = createPredicate(srDoc.getReference());

                NamedValue propParentFolderId = new NamedValue();
                propParentFolderId.setName(mpParentFolderId.getAlfFullPropName());
                propParentFolderId.setValue("");

                // CML update
                CMLUpdate cmlUpdate = new CMLUpdate();
                cmlUpdate.setWhere(predicateDoc);
                cmlUpdate.setProperty(new NamedValue[]{propParentFolderId});

                CML cml = new CML();
                cml.setUpdate(new CMLUpdate[]{cmlUpdate});
                // aggiorno profilo impostando PARENT_FOLDER_ID
                getAlfrescoRepositoryService_update(cml);

                // TODO: SCOMMENTARE quando attiveremo ereditarieta' multipla
                // propagateACL(srDoc.getDocId());
                // infine sposto fisicamente il documento
                moveToSpaceRunasAdmin(srDoc.getReference().getUuid(), repositoryRef.getUuid(), null, null);

            }

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public DataTable<String> searchFolders(Map<String, List<String>> searchCriteria, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderby) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "searchFolders";

        try {

            String lucenequery = buildQueryForPath("cm:folder", searchCriteria);

            if (lucenequery.equals("")) {
                lucenequery = LuceneUtility.buildAlfrescoFoldersQueryString(searchCriteria, true);
            }

            // System.out.println(lucenequery);

            // TODO: aggiungere ai metadati di base delle folder (profili
            // dinamici)
            if (CIFS) {
                returnProperties.add(it.kdm.docer.sdk.Constants.path);
                returnProperties.add("CREATED");
                returnProperties.add("CREATOR");
                returnProperties.add("MODIFIER");
                returnProperties.add("MODIFIED");
                returnProperties.add(it.kdm.docer.sdk.Constants.inherits_acl);
            }

            SearchResultSet res = webScriptSearch(false, lucenequery, returnProperties, maxResults, null, false, false, false, false, false, false, orderby);
            return res.getResultsAsDatatable();

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    // TODO: Construct this value from configuration
    private final static String ROOT = "/app:company_home/cm:DOCAREA";

    protected static String encodePath(String path) {
        if (path.startsWith("\\")) {
            path = path.substring(1);
        }

        if (path.isEmpty()) {
            return ROOT;
        }

        List<String> encodedComps = new ArrayList<String>();

        for (String pathComp : Splitter.on("\\").omitEmptyStrings().split(path)) {
            encodedComps.add(ISO9075.encode(encode(pathComp)));
        }

        StringBuilder encodedPath = new StringBuilder();
        encodedPath.append(ROOT);
        encodedPath.append("/cm:");

        Joiner.on("/cm:").skipNulls().appendTo(encodedPath, encodedComps);

        return encodedPath.toString();
    }

    protected static String decodePath(String path) {
        if (!path.startsWith(ROOT)) {
            return path;
        }

        path = path.substring(ROOT.length());

        if (path.isEmpty()) {
            return "\\";
        }

        List<String> decodedComps = new ArrayList<String>();

        for (String pathComp : Splitter.on("/cm:").omitEmptyStrings().split(path)) {
            decodedComps.add(decode(ISO9075.decode(pathComp)));
        }

        StringBuilder decodedPath = new StringBuilder();
        decodedPath.append("\\");
        Joiner.on("\\").skipNulls().appendTo(decodedPath, decodedComps);

        return decodedPath.toString();
    }

    private String buildQueryForPath(String type, Map<String, List<String>> searchCriteria) {
        List<String> pathSearches = new ArrayList<String>();

        if (searchCriteria.containsKey(it.kdm.docer.sdk.Constants.path)) {
            List<String> pathCriteria = searchCriteria.get(it.kdm.docer.sdk.Constants.path);

            if (pathCriteria.size() > 0) {
                for (String path : pathCriteria) {
                    if (Strings.isNullOrEmpty(path)) {
                        continue;
                    }

                    StringBuilder lucenequery = new StringBuilder();
                    lucenequery.append("+PATH:\"");
                    lucenequery.append(encodePath(path));
                    lucenequery.append("\"");
                    pathSearches.add(lucenequery.toString());
                }
            }
        }

        StringBuilder ret = new StringBuilder();

        if (pathSearches.size() > 0) {
            ret.append("+TYPE:\"");
            ret.append(type);
            ret.append("\" ");
        }

        Joiner.on(" ").skipNulls().appendTo(ret, pathSearches);

        return ret.toString();
    }

    public EnumACLRights getEffectiveRights(String docId, String userid) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "getEffectiveRights";
        String rif = docId;

        try {

            if (docId == null)
                throw new DocerException("docId null");

            if (userid == null)
                throw new DocerException("userid null");

            String lucenequery = "@docarea\\:docnum:\"" + docId + "\" ";

            SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, null, -1, userid, false, false, false, false, false, true, null);

            if (webScriptResults.getCount() == 0) {
                return EnumACLRights.undefined;
            }

            if (webScriptResults.getCount() > 1) {
                throw new DocerException("trovati " + webScriptResults.getCount() + " risultati con docId " + docId);
            }

            return webScriptResults.getFirstAndOnly().getEffectiveRights();

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public void createCustomItem(ICustomItemInfo customItemInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "createCustomItem";

        try {
            if (customItemInfo == null)
                throw new DocerException("customItemInfo is null");

            if (customItemInfo.getType() == null || customItemInfo.getType().equals(""))
                throw new DocerException("TYPE obbligatorio tra i customItemInfo");

            if (customItemInfo.getCodiceEnte() == null || customItemInfo.getCodiceEnte().equals(""))
                throw new DocerException("COD_ENTE obbligatorio tra i customItemInfo");

            if (customItemInfo.getCodiceAOO() == null || customItemInfo.getCodiceAOO().equals(""))
                throw new DocerException("COD_AOO obbligatorio tra i customItemInfo");

            String codCustom = null;
            String descrizione = null;
            // String customType = null;

            String codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.custom_cod_ente, customItemInfo.getCodiceEnte());
            String codiceAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.custom_cod_aoo, customItemInfo.getCodiceAOO());

            SearchResultSet result = _getAoo(codiceAOO, codiceEnte);
            if (result.getCount() == 0)
                throw new DocerException(codiceAOO + ": AOO non trovata");

            if (result.getCount() != 1)
                throw new DocerException(codiceAOO + ": trovate " + result.getCount() + " AOO");

            SearchResult srAoo = result.getFirstAndOnly();

            EnumACLRights aooEffectiverights = srAoo.getEffectiveRights();
            if (!aooEffectiverights.equals(EnumACLRights.normalAccess) && !aooEffectiverights.equals(EnumACLRights.fullAccess))
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");

            String customType = customItemInfo.getType().toUpperCase();

            // verifico che la custom anagrafica sia stata mappata in
            // configurazione
            if (!CUSTOM_ITEM_MAPPING.containsKey(customType))
                throw new DocerException("customType non definito o definizione errata nel provider");

            CustomItemProps cProps = CUSTOM_ITEM_MAPPING.get(customType);

            codCustom = getMappedPropertyCaseFromBusinessLogicName(cProps.getBlCodicePropName(), customItemInfo.getCodiceCustom());
            descrizione = customItemInfo.getDescrizione();

            // tutte le anagrafiche custom si creano sotto la AOO
            // cartella raccoglietrice per il tipo di anagrafica
            Reference customRoot = getCustomItemRoot(customType.toLowerCase(), srAoo.getReference().getUuid());
            if (customRoot == null) {
                customRoot = createFolderToParentUUID(Constants.TYPE_FOLDER, "Container " + customType, srAoo.getReference().getUuid(), DocerModel.ICON_PREFIX + cProps.getAlfType(), null);
            }

            Map<String, String> props = new HashMap<String, String>();

            props.put(it.kdm.docer.sdk.Constants.custom_cod_ente, codiceEnte);
            props.put(it.kdm.docer.sdk.Constants.custom_cod_aoo, codiceAOO);
            props.put(cProps.getBlCodicePropName(), codCustom);
            props.put(cProps.getBlDescrizionePropName(), descrizione);
            props.put(it.kdm.docer.sdk.Constants.custom_enabled, customItemInfo.getEnabled().toString());

            // le altre proprieta'
            for (String prop : customItemInfo.getExtraInfo().keySet()) {
                props.put(prop.toUpperCase(), customItemInfo.getExtraInfo().get(prop));
            }

            // creo l'anagrafica
            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put("COD_ENTE", Arrays.asList(codiceEnte));
            searchCriteria.put("COD_AOO", Arrays.asList(codiceAOO));
            searchCriteria.put(cProps.getBlCodicePropName(), Arrays.asList(codCustom));

            int count = countAnagraficaAsAdmin(customType, searchCriteria);

            if (count > 0)
                throw new DocerException("verifica univocita' anagrafica sul documentale: esiste gia' una anagrafica custom " + customType + " con COD_ENTE " + codiceEnte + ", COD_AOO " + codiceAOO
                        + ", " + cProps.getBlCodicePropName() + " " + codCustom);

            // nodo parent
            ParentReference parent = createParentReference(null, customRoot.getUuid(), Constants.ASSOC_CONTAINS, Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, codCustom));

            List<NamedValue> propList = new ArrayList<NamedValue>();

            populateAlfrescoProperties(props, propList);

            NamedValue nv0 = new NamedValue();
            nv0.setName(Constants.PROP_NAME);
            nv0.setValue(adjustNodeName(codCustom));
            propList.add(nv0);

            // NamedValue nv1 = new NamedValue();
            // nv1.setName(DocerModel.PROP_DESCRIPTION);
            // nv1.setValue(descrizione);
            // propList.add(nv1);

            NamedValue nv2 = new NamedValue();
            nv2.setName(DocerModel.PROP_ICON);
            nv2.setValue(DocerModel.ICON_PREFIX + cProps.getAlfType());
            propList.add(nv2);

            CMLCreate create = new CMLCreate();
            create.setParent(parent);
            create.setType(Constants.createQNameString(DocerModel.DOCAREA_NAMESPACE_CONTENT_MODEL, cProps.getAlfType()));
            create.setProperty(propList.toArray(new NamedValue[0]));

            CML cml = new CML();
            cml.setCreate(new CMLCreate[]{create});

            getAlfrescoRepositoryService_update(cml);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    public void updateCustomItem(ICustomItemId customId, ICustomItemInfo customItemInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "updateCustomItem";

        try {
            if (customId == null)
                throw new DocerException("customId is null");

            if (customItemInfo == null)
                throw new DocerException("customItemInfo is null");

            if (customId.getType() == null || customId.getType().equals(""))
                throw new DocerException("TYPE obbligatorio tra gli id_criteria");

            if (customId.getCodiceEnte() == null || customId.getCodiceEnte().equals(""))
                throw new DocerException("COD_ENTE obbligatorio tra gli id_criteria");

            if (customId.getCodiceAOO() == null || customId.getCodiceAOO().equals(""))
                throw new DocerException("COD_AOO obbligatorio tra gli id_criteria");

            String customType = customId.getType().toUpperCase();

            if (!CUSTOM_ITEM_MAPPING.containsKey(customType))
                throw new DocerException("customType non definito o definizione errata nel provider");

            CustomItemProps cProps = CUSTOM_ITEM_MAPPING.get(customType);

            if (customId.getCodiceCustom() == null || customId.getCodiceCustom().equals(""))
                throw new DocerException(cProps.getBlCodicePropName() + " obbligatorio tra gli id_criteria");

            ICustomItemId localId = new CustomItemId();
            localId.setCodiceCustom(getMappedPropertyCaseFromBusinessLogicName(cProps.getBlCodicePropName(), customId.getCodiceCustom()));
            localId.setCodiceEnte(getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.custom_cod_ente, customId.getCodiceEnte()));
            localId.setCodiceAOO(getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.custom_cod_aoo, customId.getCodiceAOO()));
            localId.setType(cProps.getAlfType()); // valorizzo con il type di
            // Alfresco

            Reference oldCustomItemRef = getCustomItemReference(localId, cProps);
            if (oldCustomItemRef == null)
                throw new DocerException("updateCustomItem (" + localId.getType() + "): " + localId.getCodiceCustom() + " non trovata");

            Map<String, String> props = new HashMap<String, String>();

            if (customItemInfo.getDescrizione() != null)
                props.put(cProps.getBlDescrizionePropName(), customItemInfo.getDescrizione());
            if (!customItemInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
                props.put(it.kdm.docer.sdk.Constants.custom_enabled, customItemInfo.getEnabled().toString());

            for (String key : customItemInfo.getExtraInfo().keySet()) {
                props.put(key.toUpperCase(), customItemInfo.getExtraInfo().get(key));
            }

            updateAlfrescoSpace(customType, customId.getCodiceCustom(), oldCustomItemRef, props);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    public void setACLTitolario(ITitolarioId titolarioId, Map<String, EnumACLRights> acls) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "setACLTitolario";

        try {

            // String titolarioUUID =
            // getTitolarioUUID(titolarioId.getClassifica(),
            // titolarioId.getCodiceTitolario(), titolarioId.getCodiceAOO(),
            // titolarioId.getCodiceEnte());
            SearchResultSet result = getTitolario(titolarioId.getClassifica(), titolarioId.getCodiceTitolario(), titolarioId.getCodiceAOO(), titolarioId.getCodiceEnte());

            if (result.getCount() == 0)
                throw new DocerException("voce di Titolario non trovata");

            if (result.getCount() > 1)
                throw new DocerException("trovate " + result.getCount() + " voci di Titolario");

            SearchResult srTitolario = result.getFirstAndOnly();

            EnumACLRights effectiverights = srTitolario.getEffectiveRights();

            if (!effectiverights.equals(EnumACLRights.fullAccess))
                throw new DocerException("l'utente corrente non possiede diritti sufficienti sulla voce di Titolario");

            // recupero gli id dei gruppi
            List<String> groupsList = getCaseSensitiveGroupsIds(new ArrayList<String>(acls.keySet()));

            // recupero gli id degli utenti (Alfresco e' CASE SENSITIVE)
            List<String> usersList = getCaseSensitiveUsersIds(new ArrayList<String>(acls.keySet()));

            List<ACE> addACEList = new ArrayList<ACE>();

            String authority = "";
            String alfRight = null;
            boolean isGroup = true;

            String adminsEnteGroupId = generateEnteAdminsGroupId(titolarioId.getCodiceEnte());// amministratori
            // di
            // Ente
            String adminsAooGroupId = generateAOOAdminsGroupId(titolarioId.getCodiceAOO(), titolarioId.getCodiceEnte());

            for (String newKey : acls.keySet()) {

                isGroup = true;

                // cerco tra i gruppi
                authority = findGroupId(groupsList, newKey);

                // se non e' tra i gruppi cerco tra gli utenti
                if (authority == null) {
                    isGroup = false;
                    // cerco tra gli utenti
                    authority = findUserId(usersList, newKey);
                    if (authority == null)
                        throw new DocerException(newKey + ": utente o gruppo non definito");
                }

                if (authority.equals(adminsEnteGroupId) || authority.equals(adminsAooGroupId) || authority.equals(SYS_ADMINS_GROUPID)) {
                    continue;
                }

                alfRight = convertToAlfrescoRights(acls.get(newKey));
                if (alfRight == null)
                    continue;

                // assegnamo la ACE
                ACE addACE = new ACE();

                if (isGroup)
                    addACE.setAuthority(Constants.GROUP_PREFIX + authority);
                else
                    addACE.setAuthority(authority);

                addACE.setPermission(alfRight);
                addACE.setAccessStatus(AccessStatus.acepted);
                addACEList.add(addACE);
            }

            // assegno sempre fullAccess agli Amministratori di Ente
            ACE addACE1 = new ACE();
            addACE1.setAuthority(Constants.GROUP_PREFIX + adminsEnteGroupId);
            addACE1.setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            addACE1.setAccessStatus(AccessStatus.acepted);
            addACEList.add(addACE1);

            // assegno sempre fullAccess agli Amministratori di AOO
            ACE addACE2 = new ACE();
            addACE2.setAuthority(Constants.GROUP_PREFIX + adminsAooGroupId);
            addACE2.setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            addACE2.setAccessStatus(AccessStatus.acepted);
            addACEList.add(addACE2);

            // fullAccess agli amministratori di system
            ACE addACE3 = new ACE();
            addACE3.setAuthority(Constants.GROUP_PREFIX + SYS_ADMINS_GROUPID);
            addACE3.setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            addACE3.setAccessStatus(AccessStatus.acepted);
            addACEList.add(addACE3);

            setACESRunasAdmin(srTitolario.getReference(), addACEList, null);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    public Map<String, EnumACLRights> getACLTitolario(ITitolarioId titolarioId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "getACLTitolario";

        try {

            // String titolarioUUID =
            // getTitolarioUUID(titolarioId.getClassifica(),
            // titolarioId.getCodiceTitolario(), titolarioId.getCodiceAOO(),
            // titolarioId.getCodiceEnte());
            SearchResultSet result = getTitolario(titolarioId.getClassifica(), titolarioId.getCodiceTitolario(), titolarioId.getCodiceAOO(), titolarioId.getCodiceEnte());

            if (result.getCount() == 0)
                throw new DocerException("voce di Titolario non trovata");

            if (result.getCount() > 1)
                throw new DocerException("trovate " + result.getCount() + " voci di Titolario");

            SearchResult srTitolario = result.getFirstAndOnly();

            Map<String, EnumACLRights> alfACLList = srTitolario.getAcl();

            Map<String, EnumACLRights> acl = new HashMap<String, EnumACLRights>();

            for (String key : alfACLList.keySet()) {
                acl.put(key.replaceAll("^" + Constants.GROUP_PREFIX, ""), alfACLList.get(key));
            }

            return acl;

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    public void setACLFascicolo(IFascicoloId fascicoloId, Map<String, EnumACLRights> acls) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "setACLFascicolo";

        try {

            SearchResultSet results = getFascicolo(fascicoloId.getProgressivo(), fascicoloId.getNumeroFascicolo(), fascicoloId.getAnnoFascicolo(), fascicoloId.getClassifica(),
                    fascicoloId.getCodiceAOO(), fascicoloId.getCodiceEnte());

            if (results.getCount() == 0)
                throw new DocerException("Fascicolo " + fascicoloId.getProgressivo() + " non trovato");

            if (results.getCount() > 1)
                throw new DocerException("trovati " + results.getCount() + " Fascicoli");

            SearchResult srFascicolo = results.getFirstAndOnly();

            EnumACLRights effectiverights = srFascicolo.getEffectiveRights();
            if (!effectiverights.equals(EnumACLRights.fullAccess))
                throw new DocerException("l'utente corrente non possiede diritti sufficienti sul Fascicolo");

            // recupero gli id dei gruppi
            List<String> groupsList = getCaseSensitiveGroupsIds(new ArrayList<String>(acls.keySet()));

            // recupero gli id degli utenti (Alfresco e' CASE SENSITIVE)
            List<String> usersList = getCaseSensitiveUsersIds(new ArrayList<String>(acls.keySet()));

            List<ACE> addACEList = new ArrayList<ACE>();

            String authority = "";
            String alfRight = null;
            boolean isGroup = true;

            for (String newKey : acls.keySet()) {

                isGroup = true;

                // cerco tra i gruppi
                authority = findGroupId(groupsList, newKey);

                // se non e' tra i gruppi cerco tra gli utenti
                if (authority == null) {
                    isGroup = false;
                    // cerco tra gli utenti
                    authority = findUserId(usersList, newKey);
                    if (authority == null)
                        throw new DocerException(newKey + ": utente o gruppo non definito");
                }

                alfRight = convertToAlfrescoRights(acls.get(newKey));
                if (alfRight == null)
                    continue;

                // assegnamo la ACE
                ACE addACE = new ACE();

                if (isGroup)
                    addACE.setAuthority(Constants.GROUP_PREFIX + authority);
                else
                    addACE.setAuthority(authority);

                addACE.setPermission(alfRight);
                addACE.setAccessStatus(AccessStatus.acepted);
                addACEList.add(addACE);
            }

            setACESRunasAdmin(srFascicolo.getReference(), addACEList, null);
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    public Map<String, EnumACLRights> getACLFascicolo(IFascicoloId fascicoloId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "getACLFascicolo";
        String rif = "";

        try {

            SearchResultSet results = getFascicolo(fascicoloId.getProgressivo(), fascicoloId.getNumeroFascicolo(), fascicoloId.getAnnoFascicolo(), fascicoloId.getClassifica(),
                    fascicoloId.getCodiceAOO(), fascicoloId.getCodiceEnte());

            if (results.getCount() == 0)
                throw new DocerException("Fascicolo " + fascicoloId.getProgressivo() + " non trovato");

            if (results.getCount() > 1)
                throw new DocerException("trovati " + results.getCount() + " Fascicoli");

            SearchResult srFascicolo = results.getFirstAndOnly();

            Map<String, EnumACLRights> alfACLList = srFascicolo.getAcl();

            Map<String, EnumACLRights> acl = new HashMap<String, EnumACLRights>();

            for (String key : alfACLList.keySet()) {
                acl.put(key.replaceAll("^" + Constants.GROUP_PREFIX, ""), alfACLList.get(key));
            }

            return acl;

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    public List<String> getRiferimentiDocuments(String docId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "getRiferimentiDocuments";
        String rif = docId;

        try {

            SearchResultSet webScriptResults = getUniqueDocument(docId, false, false, true, false, false, false);

            SearchResult sr = webScriptResults.getFirstAndOnly();

            Map<String, Reference> map = sr.getRiferimentiMap();

            return new ArrayList<String>(map.keySet());
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public void addRiferimentiDocuments(String docId, List<String> riferimenti) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "addRiferimentiDocuments";
        String rif = docId;

        try {
            if (riferimenti == null || riferimenti.size() == 0)
                return;

            if (riferimenti.size() == 1 && riferimenti.get(0).equals(docId))
                return;

            List<String> internalItems = new ArrayList<String>();
            internalItems.add(docId);
            for (String id : riferimenti) {
                if (internalItems.contains(id))
                    continue;
                internalItems.add(id);
            }

            // recupero tutti gli uuid alfresco di cui avra' bisogno
            String lucenequery = "";
            for (String id : internalItems) {
                lucenequery += "@docarea\\:docnum:\"" + id + "\" ";
            }

            SearchResultSet documentsSearchResult = webScriptSearch(false, lucenequery, Arrays.asList(it.kdm.docer.sdk.Constants.doc_docnum), -1, null, false, false, true, false, false, false, null);

            if (!documentsSearchResult.containsDocId(docId)) {
                throw new DocerException(rif + ": documento " + docId + " non trovato");
            }

            SearchResult mainDoc = documentsSearchResult.get(new DocId(docId));

            // recupero la lista dei riferimenti del documento
            Map<String, Reference> currentRiferimenti = mainDoc.getRiferimentiMap();

            List<CMLCreateAssociation> createAssocList = new ArrayList<CMLCreateAssociation>();

            // definisco le associazioni
            for (SearchResult sr : documentsSearchResult.getAllSearchResult()) {

                if (!internalItems.contains(sr.getDocId())) {
                    internalItems.add(sr.getDocId());
                }

                if (sr.getDocId().equals(docId))
                    continue;
                if (currentRiferimenti.containsKey(sr.getDocId()))
                    continue;

                createAssocList.add(getAddRiferimentoAssoc(mainDoc.getReference(), sr.getReference()));
                createAssocList.add(getAddRiferimentoAssoc(sr.getReference(), mainDoc.getReference()));
            }

            checkEffectiveRightsForNormalaccess(internalItems, metodo);

            // eseguo l'aggiornamento su Alfresco
            if (createAssocList.size() > 0) {
                CML associations = new CML();
                associations.setCreateAssociation(createAssocList.toArray(new CMLCreateAssociation[0]));

                getAlfrescoRepositoryService_update(associations);

            }
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public void removeRiferimentiDocuments(String docId, List<String> riferimenti) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "removeRiferimentiDocuments";
        String rif = docId;

        try {
            if (riferimenti == null || riferimenti.size() == 0)
                return;

            // non rimuovo da se stesso
            if (riferimenti.size() == 1 && riferimenti.get(0).equals(docId))
                return;

            List<String> internalItems = new ArrayList<String>();
            internalItems.add(docId);
            for (String id : riferimenti) {
                if (internalItems.contains(id))
                    continue;
                internalItems.add(id);
            }

            // recupero tutti gli uuid alfresco di cui avra' bisogno
            String lucenequery = "";
            for (String id : internalItems) {
                lucenequery += "@docarea\\:docnum:\"" + id + "\" ";
            }

            SearchResultSet documentsSearchResult = webScriptSearch(false, lucenequery, Arrays.asList(it.kdm.docer.sdk.Constants.doc_docnum), -1, null, false, false, true, false, false, false, null);

            if (!documentsSearchResult.containsDocId(docId)) {
                throw new DocerException(rif + ": documento " + docId + " non trovato");
            }

            // risultato del documento di riferimento
            SearchResult srMainDoc = documentsSearchResult.get(new DocId(docId));

            // lista riferimenti attuale
            Map<String, Reference> currentRiferimenti = srMainDoc.getRiferimentiMap();

            List<CMLRemoveAssociation> toRemoveRiferimentiList = new ArrayList<CMLRemoveAssociation>();

            // ciclo i riferimenti effettivamente da sganciare
            for (SearchResult sr : documentsSearchResult.getAllSearchResult()) {

                if (!internalItems.contains(sr.getDocId())) {
                    internalItems.add(sr.getDocId());
                }

                if (sr.getDocId().equals(docId))
                    continue;

                if (currentRiferimenti.containsKey(sr.getDocId())) {
                    toRemoveRiferimentiList.add(getRemoveRiferimentoAssoc(srMainDoc.getReference(), sr.getReference()));
                    toRemoveRiferimentiList.add(getRemoveRiferimentoAssoc(sr.getReference(), srMainDoc.getReference()));
                }

            }

            checkEffectiveRightsForNormalaccess(internalItems, metodo);

            if (toRemoveRiferimentiList.size() > 0) {
                CML associations = new CML();
                associations.setRemoveAssociation(toRemoveRiferimentiList.toArray(new CMLRemoveAssociation[0]));

                getAlfrescoRepositoryService_update(associations);

            }

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    private String getFilenameHash(String data) {
        String hex = DigestUtils.shaHex(data);
        return hex.substring(0, 6);
    }

    private boolean parseHasTilde(String hasTilde) {
        if (StringUtils.isNotEmpty(hasTilde)) {
            if (hasTilde.equalsIgnoreCase("false")) {
                return false;
            }
        }

        return true;
    }

    private boolean parseInheritsACL(String inheritsACL) {
        if (!CIFS) {
            return false;
        }

        if (StringUtils.isNotEmpty(inheritsACL)) {
            if (inheritsACL.equalsIgnoreCase("true")) {
                return true;
            } else if (inheritsACL.equalsIgnoreCase("false")) {
                return false;
            }
        }

        return defaultInheritsACL;
    }

    public String createFolder(IFolderInfo folderInfo) throws DocerException {

        String metodo = "createFolder";

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        try {
            if (folderInfo == null)
                throw new DocerException("folderInfo is null");

            MetaProperty mp = METAMAPPING.getMetaPropertyFromBusinessLogicName(it.kdm.docer.sdk.Constants.folder_folder_id);
            if (mp == null) {
                throw new DocerException("il metadato " + it.kdm.docer.sdk.Constants.folder_folder_id + " non e' mappato in configurazione");
            }

            if (folderInfo.getCodiceEnte() == null)
                throw new DocerException("COD_ENTE obbligatorio");

            if (folderInfo.getCodiceAOO() == null)
                throw new DocerException("COD_AOO obbligatorio");

            if (folderInfo.getFolderId() != null) {
                throw new DocerException("FOLDER_ID non puo' essere assegnato dall'esterno");
            }

            String codiceEnte = folderInfo.getCodiceEnte();
            String codiceAOO = folderInfo.getCodiceAOO();
            String folderName = folderInfo.getFolderName();
            String parentFolderId = folderInfo.getParentFolderId();
            String folderOwner = folderInfo.getFolderOwner();
            String folderDescrizione = folderInfo.getDescrizione();

            if (folderName == null || folderName.equals("")) {
                throw new DocerException("FOLDER_NAME obbligatorio");
            }

            boolean isPublic = false;
            if (folderOwner == null) {
                folderOwner = "";
            }

            if (folderOwner.equals("")) {
                isPublic = true;
            }

            if (folderDescrizione == null) {
                folderDescrizione = "";
            }

            if (parentFolderId == null || parentFolderId.equals("")) {
                throw new DocerException("Parent Folder " + parentFolderId + " non trovata");
            }

            SearchResultSet parentFolderSearchResult = getFolderFullProfile(parentFolderId);

            if (parentFolderSearchResult.getCount() == 0)
                throw new DocerException("Parent Folder " + parentFolderId + " non trovata");

            SearchResult srParentFolder = parentFolderSearchResult.getFirstAndOnly();

            EnumACLRights effectiveRights = srParentFolder.getEffectiveRights();

            if (effectiveRights.equals(EnumACLRights.readOnly) || effectiveRights.equals(EnumACLRights.viewProfile)) {
                throw new DocerException("l'utente corrente non possiede sufficienti diritti sulla Folder Parent " + parentFolderId);
            }

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_name, Arrays.asList(folderName));
            searchCriteria.put(it.kdm.docer.sdk.Constants.folder_parent_folder_id, Arrays.asList(parentFolderId));
            if (!isPublic) {
                searchCriteria.put(it.kdm.docer.sdk.Constants.folder_owner, Arrays.asList(folderOwner));
            }

            int count = countFolderAsAdmin(searchCriteria);

            if (count > 0)
                throw new DocerException("esiste gia' una Folder " + folderName + " nella Folder Parent " + parentFolderId);

            Reference folderRef = createFolderToParentUUID(Constants.TYPE_FOLDER, folderName, srParentFolder.getReference().getUuid(), DocerModel.ICON_DEFAULT, null);

            String folderId = String.valueOf(getNodeDbId(folderRef));

            boolean inheritsACL = parseInheritsACL(folderInfo.getExtraInfo().remove(it.kdm.docer.sdk.Constants.inherits_acl));

            Map<String, String> props = new HashMap<String, String>();

            props.put(it.kdm.docer.sdk.Constants.folder_folder_id, folderId);
            props.put(it.kdm.docer.sdk.Constants.folder_cod_ente, codiceEnte);
            props.put(it.kdm.docer.sdk.Constants.folder_cod_aoo, codiceAOO);
            props.put(it.kdm.docer.sdk.Constants.folder_folder_name, folderName);
            props.put(it.kdm.docer.sdk.Constants.folder_owner, folderOwner);
            props.put(it.kdm.docer.sdk.Constants.folder_des_folder, folderDescrizione);
            props.put(it.kdm.docer.sdk.Constants.folder_parent_folder_id, parentFolderId);

            if (CIFS) {
                props.put(it.kdm.docer.sdk.Constants.inherits_acl, Boolean.toString(inheritsACL));
            }

            for (String key : folderInfo.getExtraInfo().keySet()) {
                if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_folder_id) || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_parent_folder_id)
                        || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_cod_ente) || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_cod_aoo)
                        || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_des_folder) || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_folder_name)
                        || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_owner)) {
                    continue;
                }

                props.put(key, folderInfo.getExtraInfo().get(key));
            }

            updateAlfrescoFolder(isPublic, folderRef, props);

            ACE[] ace = null;

            // ora assegnamo la ACE alla folder
            if (isPublic) {
                String aooGroupId = generateAOOGroupId(codiceAOO, codiceEnte);
                ace = new ACE[1];
                // contributor al gruppo AOO
                ace[0] = new ACE();
                ace[0].setAuthority(Constants.GROUP_PREFIX + aooGroupId);
                ace[0].setPermission(convertToAlfrescoRights(EnumACLRights.normalAccess)); // il
                // gruppo
                // della
                // AOO
                // può creare nodi
                // ma
                // non modifica il
                // contenuto
                ace[0].setAccessStatus(AccessStatus.acepted);
            }

            // se e' pubblica assegno contributor alla AOO
            if (isPublic) {
                setACESRunasAdmin(folderRef, Arrays.asList(ace), false);
            }

            setInheritsRunasAdmin(folderRef, inheritsACL);

            return folderId;

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    public void updateFolder(String folderId, IFolderInfo folderNewInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "updateFolder";
        String rif = folderId;

        try {

            if (folderNewInfo == null)
                throw new DocerException("folderNewInfo is null");

            MetaProperty mp = METAMAPPING.getMetaPropertyFromBusinessLogicName(it.kdm.docer.sdk.Constants.folder_folder_id);
            if (mp == null) {
                throw new DocerException("il metadato " + it.kdm.docer.sdk.Constants.folder_folder_id + " non e' mappato in configurazione");
            }

            SearchResultSet folderSearchResult = getFolderFullProfile(folderId);

            if (folderSearchResult.getCount() == 0)
                throw new DocerException("Folder " + folderId + " non trovata");

            if (folderSearchResult.getCount() > 1)
                throw new DocerException("Folder " + folderId + ": trovate " + folderSearchResult.getCount() + " folder");

            SearchResult srFolder = folderSearchResult.getFirstAndOnly();

            EnumACLRights effectiveRights = srFolder.getEffectiveRights();

            if (!effectiveRights.equals(EnumACLRights.normalAccess) && !effectiveRights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            // DataRow<String> folderProfile =
            // folderSearchResult.getDatatable().getRow(0);
            // DataRow<String> folderProfile =
            // folderSearchResult.getFirstAndOnly().getProfile();

            String oldFolderId = folderSearchResult.getFirstAndOnly().getProfile().get(it.kdm.docer.sdk.Constants.folder_folder_id);
            String oldCodiceEnte = folderSearchResult.getFirstAndOnly().getProfile().get(it.kdm.docer.sdk.Constants.folder_cod_ente);
            String oldCodiceAOO = folderSearchResult.getFirstAndOnly().getProfile().get(it.kdm.docer.sdk.Constants.folder_cod_aoo);
            String oldFolderDescrizione = folderSearchResult.getFirstAndOnly().getProfile().get(it.kdm.docer.sdk.Constants.folder_des_folder);
            String oldFolderOwner = folderSearchResult.getFirstAndOnly().getProfile().get(it.kdm.docer.sdk.Constants.folder_owner);
            String oldFolderName = folderSearchResult.getFirstAndOnly().getProfile().get(it.kdm.docer.sdk.Constants.folder_folder_name);
            String oldParentFolderId = folderSearchResult.getFirstAndOnly().getProfile().get(it.kdm.docer.sdk.Constants.folder_parent_folder_id);

            if (folderNewInfo.getCodiceEnte() != null && !folderNewInfo.getCodiceEnte().equalsIgnoreCase(oldCodiceEnte)) {
                throw new DocerException("COD_ENTE della Folder non e' modificabile");
            }

            if (folderNewInfo.getCodiceAOO() != null && !folderNewInfo.getCodiceAOO().equalsIgnoreCase(oldCodiceAOO)) {
                throw new DocerException("COD_AOO della Folder non e' modificabile");
            }

            if (folderNewInfo.getFolderId() != null && !folderNewInfo.getFolderId().equalsIgnoreCase(oldFolderId)) {
                throw new DocerException("FOLDER_ID non e' modificabile");
            }

            if (folderNewInfo.getFolderOwner() != null && !folderNewInfo.getFolderOwner().equals(oldFolderOwner)) {
                throw new DocerException("FOLDER_OWNER non e' modificabile");
            }

            if (folderNewInfo.getParentFolderId() != null && !folderNewInfo.getParentFolderId().equals(oldParentFolderId)) {
                throw new DocerException("PARENT_FOLDER_ID non e' modificabile");
            }

            String newFolderName = folderNewInfo.getFolderName();
            String newFolderDescrizione = folderNewInfo.getDescrizione();

            if (newFolderName != null && newFolderName.equals("")) {
                throw new DocerException("FOLDER_NAME non puo' essere assegnato a vuoto");
            }

            Map<String, String> upd = new HashMap<String, String>();

            // ricerco folder con stesso name
            if (newFolderName != null && !newFolderName.equals(oldFolderName)) {
                Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
                searchCriteria.put(it.kdm.docer.sdk.Constants.folder_parent_folder_id, Arrays.asList(oldParentFolderId));
                searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_name, Arrays.asList(newFolderName));
                int count = countFolderAsAdmin(searchCriteria);

                if (count > 0)
                    throw new DocerException("impossibile rinominare la Folder " + oldFolderName + " in " + newFolderName + ": Folder esistente");

                upd.put(it.kdm.docer.sdk.Constants.folder_folder_name, newFolderName);
            }

            if (newFolderDescrizione != null && !newFolderDescrizione.equals(oldFolderDescrizione)) {
                upd.put(it.kdm.docer.sdk.Constants.folder_des_folder, newFolderDescrizione);
            }

            boolean inheritsACL = false;
            boolean modifyPermissions = false;
            if (folderNewInfo.getExtraInfo().containsKey(it.kdm.docer.sdk.Constants.inherits_acl)) {
                modifyPermissions = true;
                inheritsACL = parseInheritsACL(folderNewInfo.getExtraInfo().remove(it.kdm.docer.sdk.Constants.inherits_acl));
                upd.put(it.kdm.docer.sdk.Constants.inherits_acl, Boolean.toString(inheritsACL));
            }

            // extra info
            for (String key : folderNewInfo.getExtraInfo().keySet()) {
                if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_folder_id) || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_parent_folder_id)
                        || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_cod_ente) || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_cod_aoo)
                        || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_des_folder) || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_folder_name)
                        || key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.folder_owner)) {
                    continue;
                }
                upd.put(key, folderNewInfo.getExtraInfo().get(key));
            }

            if (upd.size() > 0) {

                boolean isPublic = (oldFolderOwner != null && oldFolderOwner.equals(""));
                Reference folderRef = srFolder.getReference();
                updateAlfrescoFolder(isPublic, folderRef, upd);

                if (modifyPermissions) {
                    setInheritsRunasAdmin(folderRef, inheritsACL);
                }

            }
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public List<IUserProfileInfo> searchUsers(Map<String, List<String>> searchCriteria) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "searchUsers";

        try {
            List<IUserProfileInfo> users = new ArrayList<IUserProfileInfo>();

            String lucenequery = LuceneUtility.buildAlfrescoUsersSearchQueryString(searchCriteria);

            // ordinamento inserito tra i searchCriteria
            Map<String, EnumSearchOrder> orderby = LuceneUtility.extractOrderByFromSearchCriteria(searchCriteria);
            if (orderby == null) {
                // ordinamento di configurazione
                orderby = orderbySearchAnagrafiche.get("USER");
            }

            int maxResults = LuceneUtility.extractMaxResultsFromSearchCriteria(searchCriteria);

            SearchResultSet webScriptResults = webScriptUsersSearch(false, lucenequery, maxResults, ADMIN_USERID, orderby);

            if (webScriptResults.getCount() < 1)
                return users;

            for (int i = 0; i < webScriptResults.getResultsAsDatatable().getRows().size(); i++) {

                DataRow<String> dr = webScriptResults.getResultsAsDatatable().getRow(i);

                IUserProfileInfo userProfileInfo = new UserProfileInfo();
                userProfileInfo.setUserId("");
                userProfileInfo.setEmailAddress("");
                userProfileInfo.setEnabled(EnumBoolean.TRUE);
                userProfileInfo.setFirstName("");
                userProfileInfo.setFullName("");
                userProfileInfo.setLastName("");
                userProfileInfo.setUserPassword("");
                userProfileInfo.setNetworkAlias("");
                userProfileInfo.getExtraInfo().put(it.kdm.docer.sdk.Constants.user_cod_ente, "");
                userProfileInfo.getExtraInfo().put(it.kdm.docer.sdk.Constants.user_cod_aoo, "");

                for (String nomeColonna : webScriptResults.getResultsAsDatatable().getColumnNames()) {

                    String val = dr.get(nomeColonna);
                    if (val == null) {
                        val = "";
                    }

                    MetaProperty mp = METAMAPPING_USER.getMetaPropertyFromBusinessLogicName(nomeColonna);
                    if (mp == null) {
                        mp = METAMAPPING.getMetaPropertyFromBusinessLogicName(nomeColonna);
                    }

                    if (mp == null) {
                        continue;
                    }

                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.user_user_id)) {
                        userProfileInfo.setUserId(val);
                        continue;
                    }
                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.user_full_name)) {
                        userProfileInfo.setFullName(val);
                        continue;
                    }
                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.user_first_name)) {
                        userProfileInfo.setFirstName(val);
                        continue;
                    }
                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.user_last_name)) {
                        userProfileInfo.setLastName(val);
                        continue;
                    }
                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.user_email_address)) {
                        userProfileInfo.setEmailAddress(val);
                        continue;
                    }
                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.user_network_alias)) {
                        userProfileInfo.setNetworkAlias(val);
                        continue;
                    }
                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.user_enabled)) {
                        userProfileInfo.setEnabled(EnumBoolean.TRUE);
                        if (val != null && val.equalsIgnoreCase("false")) {
                            userProfileInfo.setEnabled(EnumBoolean.FALSE);
                        }
                        continue;
                    }

                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.user_cod_ente)) {
                        userProfileInfo.getExtraInfo().put(it.kdm.docer.sdk.Constants.user_cod_ente, val);
                        continue;
                    }
                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.user_cod_aoo)) {
                        userProfileInfo.getExtraInfo().put(it.kdm.docer.sdk.Constants.user_cod_aoo, val);
                        continue;
                    }

                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.user_cod_fiscale)) {
                        userProfileInfo.getExtraInfo().put(it.kdm.docer.sdk.Constants.user_cod_fiscale, val);
                        continue;
                    }

                    userProfileInfo.getExtraInfo().put(nomeColonna, val);

                }

                users.add(userProfileInfo);
            }

            return users;

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    public List<IGroupProfileInfo> searchGroups(Map<String, List<String>> searchCriteria) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "searchGroups";
        try {
            List<IGroupProfileInfo> groups = new ArrayList<IGroupProfileInfo>();

            String lucenequery = LuceneUtility.buildAlfrescoGroupsSearchQueryString(searchCriteria);

            // ordinamento inserito tra i searchCriteria
            Map<String, EnumSearchOrder> orderby = LuceneUtility.extractOrderByFromSearchCriteria(searchCriteria);
            if (orderby == null) {
                // ordinamento di configurazione
                orderby = orderbySearchAnagrafiche.get("GROUP");
            }

            int maxResults = LuceneUtility.extractMaxResultsFromSearchCriteria(searchCriteria);

            SearchResultSet webScriptResults = webScriptGroupsSearch(false, lucenequery, maxResults, ADMIN_USERID, orderby);

            if (webScriptResults.getCount() < 1)
                return groups;

            for (int i = 0; i < webScriptResults.getResultsAsDatatable().getRows().size(); i++) {

                DataRow<String> dr = webScriptResults.getResultsAsDatatable().getRow(i);

                IGroupProfileInfo groupProfileInfo = new GroupProfileInfo();
                groupProfileInfo.setEnabled(EnumBoolean.TRUE);
                groupProfileInfo.setGroupId("");
                groupProfileInfo.setGroupName("");
                groupProfileInfo.setParentGroupId("");
                groupProfileInfo.getExtraInfo().put(it.kdm.docer.sdk.Constants.group_gruppo_struttura, "false");

                for (MetaProperty mp : METAMAPPING_GROUP.getAllMetaProperties()) {

                    String nomeColonna = mp.getBlPropName();
                    String val = dr.get(mp.getBlPropName());
                    if (val == null) {
                        val = "";
                    }

                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.group_group_id)) {
                        groupProfileInfo.setGroupId(val.replaceAll("^" + Constants.GROUP_PREFIX, ""));
                        continue;
                    }
                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.group_group_name)) {
                        groupProfileInfo.setGroupName(val);
                        continue;
                    }
                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.group_parent_group_id)) {
                        groupProfileInfo.setParentGroupId(val);
                        continue;
                    }
                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.group_gruppo_struttura)) {
                        groupProfileInfo.getExtraInfo().put(it.kdm.docer.sdk.Constants.group_gruppo_struttura, "false");
                        if (val.equalsIgnoreCase("true")) {
                            groupProfileInfo.getExtraInfo().put(it.kdm.docer.sdk.Constants.group_gruppo_struttura, "true");
                        }
                        continue;
                    }
                    if (nomeColonna.equals(it.kdm.docer.sdk.Constants.group_enabled)) {
                        groupProfileInfo.setEnabled(EnumBoolean.TRUE);
                        if (val.equalsIgnoreCase("false")) {
                            groupProfileInfo.setEnabled(EnumBoolean.FALSE);
                        }
                        continue;
                    }

                    groupProfileInfo.getExtraInfo().put(nomeColonna, val);
                }

                groups.add(groupProfileInfo);
            }

            return groups;

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, String>> searchAnagraficheEstesa(String type, Map<String, List<String>> searchCriteria, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderby) throws DocerException {

        ArrayList<String> values = new ArrayList<String>();
        values.add(Integer.toString(maxResults));
        searchCriteria.put("$MAX_RESULTS", values);

        values = new ArrayList<String>();
        for (Map.Entry<String, EnumSearchOrder> entry : orderby.entrySet()) {
            values.add(String.format("%s=%s", entry.getKey(), entry.getValue().name()));
        }

        if (!values.isEmpty()) {
            searchCriteria.put("$ORDER_BY", values);
        }

        return this.searchAnagrafiche(type, searchCriteria, returnProperties);
    }

    public List<Map<String, String>> searchAnagrafiche(String type, Map<String, List<String>> searchCriteria, List<String> returnProperties) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "searchAnagrafiche";
        try {
            if (StringUtils.isEmpty(type)) {
                throw new DocerException("type (tipo anagrafica) obbligatorio");
            }

            String alfType = getType(type);

            // COSTRUZIONE query
            String lucenequery = buildQueryForPath(alfType, searchCriteria);
            if (lucenequery.isEmpty()) {
                lucenequery = LuceneUtility.buildAlfrescoAnagraficheQueryString(alfType, searchCriteria);
            }

            // ordinamento inserito tra i searchCriteria
            Map<String, EnumSearchOrder> orderby = LuceneUtility.extractOrderByFromSearchCriteria(searchCriteria);
            if (orderby == null) {
                // ordinamento di configurazione
                orderby = orderbySearchAnagrafiche.get(type);
            }

            int maxResults = LuceneUtility.extractMaxResultsFromSearchCriteria(searchCriteria);

            if (CIFS) {
                returnProperties.add(it.kdm.docer.sdk.Constants.inherits_acl);
            }

            SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, returnProperties, maxResults, null, false, false, false, false, false, false, orderby);

            DataTable<String> dtResult = webScriptResults.getResultsAsDatatable();

            List<Map<String, String>> result = new ArrayList<Map<String, String>>();

            for (int i = 0; i < dtResult.getRows().size(); i++) {
                DataRow<String> row = dtResult.getRow(i);

                Map<String, String> profile = new HashMap<String, String>();
                result.add(profile);
                for (String columnName : dtResult.getColumnNames()) {
                    profile.put(columnName, row.get(columnName));
                }
            }

            return result;
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    public String addNewVersion(String docId, InputStream documentFile) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "addNewVersion";
        String rif = docId;

        try {

            SearchResultSet results = getUniqueDocument(docId, false, false, false, false, false, true);

            SearchResult srDoc = results.getFirstAndOnly();

            EnumACLRights effectiveRights = srDoc.getEffectiveRights();
            if (!effectiveRights.equals(EnumACLRights.normalAccess) && !effectiveRights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            NamedValue[] majorVersionProps = new NamedValue[1];
            majorVersionProps[0] = Utils.createNamedValue("versionType", "MAJOR");

            NamedValue[] minorVersionProps = new NamedValue[1];
            minorVersionProps[0] = Utils.createNamedValue("versionType", "MINOR");

            // predicato del documento
            Predicate docPredicate = createPredicate(srDoc.getReference());

            String revertVersionLabel = null;

            VersionResult vr = null;

            revertVersionLabel = getLastVersionLabel(srDoc.getReference());

            if (revertVersionLabel == null) {// se non ha versioni creo la I
                // versione
                List<NamedValue> props = new ArrayList<NamedValue>();

                NamedValue propAutoversion = new NamedValue();
                propAutoversion.setName(DocerModel.PROP_AUTOVERSION);
                propAutoversion.setValue("false");
                props.add(propAutoversion);

                NamedValue propAutoversionUpdate = new NamedValue();
                propAutoversionUpdate.setName(DocerModel.PROP_AUTOVERSION_UPDATE);
                propAutoversionUpdate.setValue("false");
                props.add(propAutoversionUpdate);

                CMLUpdate cmlUpdateDoc = null;
                cmlUpdateDoc = new CMLUpdate();
                cmlUpdateDoc.setProperty(props.toArray(new NamedValue[0]));

                cmlUpdateDoc.setWhere(docPredicate); // il documento

                // abilitazione del versioning
                CML cml = new CML();
                cml.setUpdate(new CMLUpdate[]{cmlUpdateDoc});

                // abilito il versionamento
                getAlfrescoRepositoryService_update(cml);

                // recupero label di versione
                String firstVersionLabel = getLastVersionLabel(srDoc.getReference());

                // se la versione di Alfresco e' prima della 4.0.0 crea subito
                // la versione 1.0 abilitando il versioning
                // dalla versione 4.0.0 in poi crea la 0.1 come prima versione
                if (firstVersionLabel.startsWith("0.")) {
                    // creo la versione 1.0
                    vr = getAlfrescoAuthoringService_createVersion(docPredicate, majorVersionProps);
                    revertVersionLabel = vr.getVersions(0).getLabel();
                }

            }

            // creo una nuova minor version
            vr = getAlfrescoAuthoringService_createVersion(docPredicate, minorVersionProps);

            String versNum = vr.getVersions(0).getLabel();
            versNum = versNum.substring(0, versNum.indexOf("."));

            // String filename = getFileName(nodeRef);
            String filename = srDoc.getCmName();

            // upload sulla nuova MINOR version
            uploadFile(srDoc.getReference(), filename, documentFile);

            // creo MAJOR version con nuovo file
            vr = getAlfrescoAuthoringService_createVersion(docPredicate, majorVersionProps);

            // versNum = getLastVersionLabel(nodeUUID);
            // versNum = versNum.substring(0, versNum.indexOf("."));

            setAuthor(srDoc.getReference(), currentUser.getUserId());

            versNum = vr.getVersions(0).getLabel();
            versNum = versNum.substring(0, versNum.indexOf("."));

            return versNum;

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public byte[] downloadVersion(String docId, String versionNumber, String destinationFilePath, long maxFileLength) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "downloadVersion";
        String rif = docId;

        try {

            String urlSuffix = "";
            String verUuid = getVersionId(docId, versionNumber);

            if (verUuid == null) {
                if (versionNumber.equals("1")) { // se non ha versioni
                    urlSuffix = getContentUrlSuffix(docId);
                    return downloadFile(currentUser.getTicket(), urlSuffix, destinationFilePath, maxFileLength, DocerModel.ALFRESCO_DOWNLOAD_SERVLET_URL);
                }
                throw new DocerException(-1011, "downloadVersion: " + docId + ": versione " + versionNumber + " non esistente");
            }

            urlSuffix = getVersionContentUrlSuffix(verUuid);
            return downloadFile(currentUser.getTicket(), urlSuffix, destinationFilePath, maxFileLength, DocerModel.ALFRESCO_VERSION_DOWNLOAD_SERVLET_URL);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public void addNewAdvancedVersion(String docIdLastVersion, String docIdNewVersion) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "addNewAdvancedVersion";
        String rif = docIdLastVersion;

        try {
            if (docIdLastVersion == null || docIdLastVersion.equals(""))
                return;

            if (docIdNewVersion == null || docIdNewVersion.equals(""))
                return;

            if (docIdLastVersion.equals(docIdNewVersion))
                return;

            String lucenequery = "@docarea\\:docnum:\"" + docIdLastVersion + "\" @docarea\\:docnum:\"" + docIdNewVersion + "\"";

            SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, null, -1, null, false, true, false, false, false, false, null);

            // Map<String, Reference> docIdToReferenceMap =
            // webScriptResults.getDocidToReferenceMap();

            SearchResult srLastVersion = webScriptResults.get(new DocId(docIdLastVersion));
            SearchResult srNewVersion = webScriptResults.get(new DocId(docIdNewVersion));

            if (srLastVersion == null)
                throw new DocerException("docIdLastVersion " + docIdLastVersion + " non trovato");

            if (srNewVersion == null)
                throw new DocerException("docIdNewVersion " + docIdNewVersion + " non trovato");

            // recupero la lista delle av associate al documento
            Map<String, Reference> mainAdvancedVersionChainMap = srLastVersion.getAdvancedVersionsMap();
            // la modifico (tanto la uso solo qui)
            mainAdvancedVersionChainMap.put(docIdLastVersion, srLastVersion.getReference());

            if (mainAdvancedVersionChainMap.containsKey(docIdNewVersion)) {
                return;
            }

            List<CMLCreateAssociation> tocreateAssocList = new ArrayList<CMLCreateAssociation>();

            Map<String, Reference> toaddAdvancedVersionChainMap = srNewVersion.getAdvancedVersionsMap();

            // la modifico tanto la uso internamente
            toaddAdvancedVersionChainMap.put(docIdNewVersion, srNewVersion.getReference());

            // creo tutte le associazioni mancanti
            for (String toaddId : toaddAdvancedVersionChainMap.keySet()) {

                Reference toaddRef = toaddAdvancedVersionChainMap.get(toaddId);

                // creo le associazioni con tutti i documenti della main chain
                for (String mainChainMemberId : mainAdvancedVersionChainMap.keySet()) {

                    // se il rel da aggiungere fa gia' parte della
                    // catena...continuo
                    if (mainAdvancedVersionChainMap.containsKey(toaddId))
                        continue;

                    Reference mainChainMemberReference = mainAdvancedVersionChainMap.get(mainChainMemberId);

                    tocreateAssocList.add(getAddAdvancedVersionAssoc(mainChainMemberReference, toaddRef));
                    tocreateAssocList.add(getAddAdvancedVersionAssoc(toaddRef, mainChainMemberReference));
                }

            }

            for (String toaddId : toaddAdvancedVersionChainMap.keySet()) {
                mainAdvancedVersionChainMap.put(toaddId, toaddAdvancedVersionChainMap.get(toaddId));
            }

            checkEffectiveRightsForNormalaccess(mainAdvancedVersionChainMap.keySet(), "addNewAdvancedVersion");

            // eseguo l'aggiornamento su Alfresco
            if (tocreateAssocList.size() > 0) {
                CML associations = new CML();
                associations.setCreateAssociation(tocreateAssocList.toArray(new CMLCreateAssociation[0]));

                getAlfrescoRepositoryService_update(associations);

            }

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public List<String> getAdvancedVersions(String docId) throws DocerException {
        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "getAdvancedVersions";
        String rif = docId;

        try {

            SearchResultSet webScriptResults = getUniqueDocument(docId, false, true, false, false, false, false);

            return new ArrayList<String>(webScriptResults.getFirstAndOnly().getAdvancedVersionsMap().keySet());

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, String>> getRiferimentiFascicolo(IFascicoloId fascicoloId) throws DocerException {
        return null;
    }

    @Override
    public void addRiferimentiFascicolo(IFascicoloId fascicoloId, List<IFascicoloId> riferimenti) throws DocerException {

    }

    @Override
    public void removeRiferimentiFascicolo(IFascicoloId fascicoloId, List<IFascicoloId> riferimenti) throws DocerException {

    }

    public IGroupInfo getGroup(String groupId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");
        String metodo = "getGroup";
        String rif = groupId;
        try {
            IGroupInfo groupInfo = _getGroup(groupId);

            return groupInfo;

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public void removeGroupsFromUser(String userId, List<String> groups) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        if (userId == null)
            return;

        String metodo = "removeGroupsFromUser";
        String rif = userId;

        try {

            // recupero profilo parziale (senza i gruppi) dell'utente
            IUserProfileInfo userProfileInfo = getUserProfile(userId);

            if (userProfileInfo == null)
                throw new DocerException("utente non trovato: " + userId);

            String[] user = new String[]{userProfileInfo.getUserId()};

            List<String> alfGroupIds = getCaseSensitiveGroupsIds(groups);

            for (String alfIdGroupToRemove : alfGroupIds) {
                getAlfrescoAccessControlService_removeChildAuthorities(Constants.GROUP_PREFIX + alfIdGroupToRemove, user);
            }

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public List<IHistoryItem> getHistory(String docId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        List<IHistoryItem> hItems = new ArrayList<IHistoryItem>();

        // String nomeMetodo = "GetHistory";
        // String rif = ""+ docId;
        //
        // String docUUID = _GetAlfrescoUUID(docId,DocerModel.WORKSPACE_STORE);
        // if (docUUID == null)
        // throw new DocerException(-1111, nomeMetodo + ": " + rif +
        // ": documento non trovato");
        //
        // Reference refDoc = CreateReference(null, docUUID);
        // //Predicate predicatoDoc = CreatePredicate(refDoc);
        //
        // VersionHistory results = null;
        // try
        // {
        // results =
        // WebServiceFactory.getAuthoringService().getVersionHistory(refDoc);
        // }
        // catch (AuthoringFault e) {
        // throw new DocerException(-1027,nomeMetodo +": " +rif +":" +
        // e.getMessage1());
        // } catch (RemoteException e) {
        // throw new DocerException(-1027,nomeMetodo +": " +rif +":" +
        // e.getMessage());
        // }
        // HistoryItem hItem;
        // for (Version ver : results.getVersions())
        // {
        // if (!ver.isMajor())
        // {
        // hItem = new HistoryItem();
        //
        // hItem.setDate(ver.getCreated().getTime());
        // hItem.setUser(ver.getCreator());
        // hItem.setDescription(ver.getCommentaries(0).toString());
        // hItems.add(hItem);
        // }
        // }
        return hItems;
    }

    public List<String> getVersions(String docId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "getVersions";
        String rif = docId;
        try {
            List<String> versions = new ArrayList<String>();

            SearchResultSet webScriptResult = getUniqueDocument(docId, false, false, false, false, false, true);

            VersionHistory results = null;

            results = getAlfrescoAuthoringService_getVersionHistory(webScriptResult.getFirstAndOnly().getReference());

            if (results.getVersions() != null)
                for (Version ver : results.getVersions()) {

                    // if (ver.isMajor()){
                    if (ver.getLabel().matches(".+\\.0$")) {
                        String versNum = ver.getLabel().replaceAll("\\..+$", "");
                        versions.add(versNum);
                    }
                }

            if (!versions.contains("1"))
                versions.add("1"); // vale se non ha versioni. La 1.0 e' una
            // minor

            return versions;

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public byte[] downloadLastVersion(String docId, String destinationFilePath, long maxFileLength) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "downloadLastVersion";
        String rif = docId;

        try {
            String urlSuffix = getContentUrlSuffix(docId);
            return downloadFile(currentUser.getTicket(), urlSuffix, destinationFilePath, maxFileLength, DocerModel.ALFRESCO_DOWNLOAD_SERVLET_URL);
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    // TODO: commentare quando attiveremo ereditarieta' multipla
    public void setACLDocument(String docId, Map<String, EnumACLRights> acls) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "setACLDocument";
        String rif = docId;

        try {
            SearchResultSet result = getUniqueDocument(docId, false, false, false, true, true, true);

            SearchResult sr = result.getFirstAndOnly();

            LockStatus lockStatus = sr.getLockStatus();

            if (lockStatus.getLocked() && !lockStatus.getUserId().equals(currentUser.getUserId())) {
                throw new DocerException("il documento " + sr.getDocId() + " e' bloccato esclusivamente da un altro utente");
            }

            EnumACLRights effectiverights = sr.getEffectiveRights();

            if (!effectiverights.equals(EnumACLRights.fullAccess))
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");

            // recupero gli id dei gruppi
            List<String> groupsList = getCaseSensitiveGroupsIds(new ArrayList<String>(acls.keySet()));

            // recupero gli id degli utenti (Alfresco e' CASE SENSITIVE)
            List<String> usersList = getCaseSensitiveUsersIds(new ArrayList<String>(acls.keySet()));

            List<ACE> aces = new ArrayList<ACE>();

            for (String newKey : acls.keySet()) {

                EnumACLRights permission = acls.get(newKey);

                boolean isGroup = true;
                // cerco tra i gruppi
                String authority = findGroupId(groupsList, newKey);

                // se non e' tra i gruppi cerco tra gli utenti
                if (authority == null) {
                    isGroup = false;
                    // cerco tra gli utenti
                    authority = findUserId(usersList, newKey);
                    if (authority == null)
                        throw new DocerException(newKey + ": utente o gruppo non definito");
                }

                String alfRight = convertToAlfrescoRights(permission);
                if (alfRight == null) {
                    continue;
                }

                // assegnamo la ACE
                if (isGroup) {
                    authority = Constants.GROUP_PREFIX + authority;
                }

                ACE ace = new ACE();
                ace.setAuthority(authority);
                ace.setPermission(alfRight);
                ace.setAccessStatus(AccessStatus.acepted);
                aces.add(ace);

            }

            setACESRunasAdmin(sr.getReference(), aces, null);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    // TODO: SCOMMENTARE e rivedere quando attiveremo ereditarieta' multipla
    // public void setACLDocument(String docId, Map<String, EnumACLRights> acls)
    // throws DocerException {
    //
    // if (currentUser == null)
    // throw new DocerException(-1113, "current user is null");
    //
    // String metodo = "setACLDocument";
    // String rif = docId;
    //
    // try {
    // SearchResultSet result = getUniqueDocument(docId, false, false, false,
    // true, true, true);
    //
    // SearchResult sr = result.getFirstAndOnly();
    //
    // LockStatus lockStatus = sr.getLockStatus();
    //
    // if (lockStatus.getLocked() &&
    // !lockStatus.getUserId().equals(currentUser.getUserId())) {
    // throw new DocerException("il documento " + sr.getDocId() +
    // " e' bloccato esclusivamente da un altro utente");
    // }
    //
    // EnumACLRights effectiverights = sr.getEffectiveRights();
    //
    // if (!effectiverights.equals(EnumACLRights.fullAccess))
    // throw new
    // DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
    //
    // // recupero gli id dei gruppi
    // List<String> groupsList = getCaseSensitiveGroupsIds(new
    // ArrayList<String>(acls.keySet()));
    //
    // // recupero gli id degli utenti (Alfresco e' CASE SENSITIVE)
    // List<String> usersList = getCaseSensitiveUsersIds(new
    // ArrayList<String>(acls.keySet()));
    //
    // Map<String, EnumACLRights> aclDocument = new HashMap<String,
    // EnumACLRights>();
    //
    // for (String newKey : acls.keySet()) {
    //
    // EnumACLRights permission = acls.get(newKey);
    //
    // boolean isGroup = true;
    // // cerco tra i gruppi
    // String authority = findGroupId(groupsList, newKey);
    //
    // // se non e' tra i gruppi cerco tra gli utenti
    // if (authority == null) {
    // isGroup = false;
    // // cerco tra gli utenti
    // authority = findUserId(usersList, newKey);
    // if (authority == null)
    // throw new DocerException(newKey + ": utente o gruppo non definito");
    // }
    //
    // String alfRight = convertToAlfrescoRights(permission);
    // if (alfRight == null) {
    // continue;
    // }
    //
    // // assegnamo la ACE
    // if (isGroup) {
    // aclDocument.put(Constants.GROUP_PREFIX + authority, permission);
    // }
    // else {
    // aclDocument.put(authority, permission);
    // }
    // }
    //
    // propagateACL(docId, aclDocument);
    //
    // }
    // catch (DocerException e) {
    // throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
    // }
    // }

    // TODO: da rivedere e scommentare quando attiveremo ereditarieta' multipla
    // private void propagateACL(String docId) throws DocerException {
    // propagateACL(docId, null);
    // }

    // TODO: da rivedere e scommentare quando attiveremo ereditarieta' multipla
    // private void propagateACL(String docId, Map<String, EnumACLRights>
    // aclToSet) throws DocerException {
    //
    // // recupero profilo aggiornato
    // // SearchResultSet result = getUniqueDocument(docId, false, false,
    // // false, true, true, true);
    //
    // String lucenequery = "@docarea\\:docnum:\"" + docId + "\" ";
    //
    // List<String> returnProperties = new ArrayList<String>();
    // returnProperties.add(it.kdm.docer.sdk.Constants.doc_docnum);
    // returnProperties.add(it.kdm.docer.sdk.Constants.doc_cod_ente);
    // returnProperties.add(it.kdm.docer.sdk.Constants.doc_cod_aoo);
    // returnProperties.add(it.kdm.docer.sdk.Constants.doc_classifica);
    // returnProperties.add(it.kdm.docer.sdk.Constants.doc_anno_fascicolo);
    // returnProperties.add(it.kdm.docer.sdk.Constants.doc_progr_fascicolo);
    // returnProperties.add(it.kdm.docer.sdk.Constants.doc_fascicoli_secondari);
    // returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
    // returnProperties.add(ACL_EXPLICIT);
    //
    // SearchResultSet searchResultSet = webScriptSearch(false, lucenequery,
    // returnProperties, -1, null, false, false, false, false, true, true,
    // null);
    // if (searchResultSet.getCount() == 0)
    // throw new DocerException("documento " + docId + " non trovato");
    //
    // if (searchResultSet.getCount() > 1)
    // throw new DocerException("trovati " + searchResultSet.getCount() +
    // " risultati con docId " + docId);
    //
    // SearchResult srDocument = searchResultSet.getFirstAndOnly();
    //
    // Map<String, EnumACLRights> nextAclDocument = new HashMap<String,
    // EnumACLRights>();
    //
    // String aclExplicitString = "";
    //
    // if (aclToSet != null) {
    // nextAclDocument.putAll(aclToSet);
    // aclExplicitString = generateACLExplicitString(nextAclDocument);
    // }
    // else {
    // aclExplicitString = srDocument.getProfile().get(ACL_EXPLICIT);
    // // se e' un ambiente 1.3.4 aggiornato a 1.3.5 non ha metadato
    // // acl_explicit
    // // il valore di confronto 'nothing' lo imposto nei risultati della
    // // ricerca quando e' null
    // if (aclExplicitString.equals("nothing")) {
    // nextAclDocument = srDocument.getAcl(); // dal web script della
    // // ricerca restituisco
    // // solo le acl dirette
    // aclExplicitString = generateACLExplicitString(nextAclDocument);
    // }
    // else {
    // nextAclDocument = parseACLExplicit(aclExplicitString);
    // }
    // }
    //
    // String progr_fascicolo =
    // srDocument.getProfile().get(it.kdm.docer.sdk.Constants.doc_progr_fascicolo);
    // String parent_folder_id =
    // srDocument.getProfile().get(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
    //
    // Map<String, EnumACLRights> aclFascicoli = null;
    // Map<String, EnumACLRights> aclPublicFolder = null;
    //
    // if (StringUtils.isNotEmpty(progr_fascicolo)) {
    //
    // if (DOCUMENTO_EREDITA_ACL) {
    // aclFascicoli = getACLFascicoliOfDocument(srDocument.getProfile());
    // }
    // }
    //
    // if (StringUtils.isNotEmpty(parent_folder_id)) {
    //
    // if (DOCUMENTO_EREDITA_ACL) {
    //
    // SearchResultSet folderSearchResult =
    // getFolderFullProfileAsAdmin(parent_folder_id);
    //
    // if (folderSearchResult.getCount() == 0)
    // throw new DocerException("Folder " + parent_folder_id + " non trovata");
    //
    // if (folderSearchResult.getCount() > 1)
    // throw new DocerException("trovate " + folderSearchResult.getCount() +
    // " folder");
    //
    // SearchResult srFolder = folderSearchResult.getFirstAndOnly();
    //
    // String folderOwner =
    // srFolder.getProfile().get(it.kdm.docer.sdk.Constants.folder_owner);
    //
    // boolean isPublicFolder = (folderOwner != null && folderOwner.equals(""));
    //
    // if (isPublicFolder) {
    // aclPublicFolder = srFolder.getAcl();
    // }
    // }
    // }
    //
    // if (aclFascicoli != null) {
    // nextAclDocument = mergeACL2(nextAclDocument, aclFascicoli);
    // }
    //
    // if (aclPublicFolder != null) {
    // nextAclDocument = mergeACL2(nextAclDocument, aclPublicFolder);
    // }
    //
    // // verifico se cambiano le ACL rispetto alle attuali
    // boolean aclChanged = false;
    // if (!mapsAreEqual(nextAclDocument, srDocument.getAcl())) {
    // aclChanged = true;
    // }
    //
    // if (DOCUMENTO_EREDITA_ACL) {
    //
    // MetaProperty mpAclExplicit =
    // METAMAPPING.getMetaPropertyFromBusinessLogicName(ACL_EXPLICIT);
    // if (mpAclExplicit == null) {
    // throw new
    // DocerException("il metadato ACL_EXPLICIT non e' mappato in configurazione");
    // }
    //
    // NamedValue propAclExplicit = new NamedValue();
    // propAclExplicit.setName(mpAclExplicit.getAlfFullPropName());
    // propAclExplicit.setValue(aclExplicitString);
    //
    // Predicate predicateDoc = createPredicate(srDocument.getReference());
    //
    // // CML update
    // CMLUpdate cmlUpdate = new CMLUpdate();
    // cmlUpdate.setWhere(predicateDoc);
    // cmlUpdate.setProperty(new NamedValue[] { propAclExplicit });
    //
    // CML cml = new CML();
    // cml.setUpdate(new CMLUpdate[] { cmlUpdate });
    // // aggiorno profilo impostando ACL_EXPLICIT
    // getAlfrescoRepositoryService_update(cml);
    //
    // // CONTROLLO CONSISTENZA DELLA ACL_EXPLICIT
    // Map<String, EnumACLRights> aclExplicitFinali =
    // parseACLExplicit(aclExplicitString);
    // if (aclExplicitFinali.size() > nextAclDocument.size()) {
    // System.out.println("ERRORE CONSISTENZA ACL_EXPLICIT: docId: " + docId +
    // "; aclExplicitFinali-->" + aclExplicitFinali.toString() +
    // "; nextAclDocument-->" + nextAclDocument.toString());
    // }
    // }
    //
    // if (!aclChanged) {
    // setInheritsRunasAdmin(srDocument.getReference(), false);
    // return;
    // }
    //
    // List<ACE> aceList = mergeACL(nextAclDocument, null);
    // setACESRunasAdmin(srDocument.getReference(), aceList, false);
    //
    // }

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

    public void updateFascicolo(IFascicoloId fascicoloId, IFascicoloInfo fascicoloNewInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "updateFascicolo";
        String rif = "";
        try {
            String codiceEnte = fascicoloId.getCodiceEnte();
            String codiceAOO = fascicoloId.getCodiceAOO();
            String classifica = fascicoloId.getClassifica();
            String progressivo = fascicoloId.getProgressivo();
            String annoFascicolo = String.valueOf(fascicoloId.getAnnoFascicolo());
            String numeroFascicolo = fascicoloId.getNumeroFascicolo();

            if (codiceEnte == null)
                codiceEnte = "";
            if (codiceAOO == null)
                codiceAOO = "";
            if (classifica == null)
                classifica = "";
            if (progressivo == null)
                progressivo = "";
            if (numeroFascicolo == null)
                numeroFascicolo = "";

            classifica = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_classifica, classifica);
            codiceAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_cod_aoo, codiceAOO);
            codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_cod_ente, codiceEnte);
            progressivo = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_progr_fascicolo, progressivo);
            numeroFascicolo = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_num_fascicolo, numeroFascicolo);

            rif = "classifica: " + fascicoloId.getClassifica() + ": annoFascicolo: " + fascicoloId.getAnnoFascicolo() + ", progressivo: " + fascicoloId.getProgressivo() + ", numeroFascicolo: "
                    + fascicoloId.getNumeroFascicolo();

            SearchResultSet result = getFascicolo(fascicoloId.getProgressivo(), fascicoloId.getNumeroFascicolo(), fascicoloId.getAnnoFascicolo(), fascicoloId.getClassifica(),
                    fascicoloId.getCodiceAOO(), fascicoloId.getCodiceEnte());

            if (result.getCount() == 0)
                throw new DocerException("Fascicolo " + fascicoloId.getProgressivo() + " non trovato");

            if (result.getCount() > 1)
                throw new DocerException("trovati " + result.getCount() + " Fascicoli");

            SearchResult sr = result.getFirstAndOnly();

            if (sr == null)
                throw new DocerException("Fascicolo " + fascicoloId.getProgressivo() + " non trovato");

            Reference fascicoloRef = sr.getReference();

            EnumACLRights effectiveRights = sr.getEffectiveRights();
            if (!effectiveRights.equals(EnumACLRights.normalAccess) && !effectiveRights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            Map<String, String> props = new HashMap<String, String>();

            String newName = fascicoloNewInfo.getDescrizione();
            if (newName == null) {
                newName = sr.getProfile().get(it.kdm.docer.sdk.Constants.fascicolo_des_fascicolo);
            } else {
                props.put(it.kdm.docer.sdk.Constants.fascicolo_des_fascicolo, newName);
            }

            String id = Joiner.on('-').join(progressivo, classifica, annoFascicolo);
            String affix = String.format("~%s", getFilenameHash(id));

            boolean hasTilde;
            if (fascicoloNewInfo.getExtraInfo().containsKey(HAS_TILDE)) {
                hasTilde = parseHasTilde(fascicoloNewInfo.getExtraInfo().remove(HAS_TILDE));
            } else {
                hasTilde = sr.getCmName().contains(affix);
            }

            if (hasTilde && !newName.contains(affix)) {
                newName = addAffix(newName, affix);
            } else if (!hasTilde && newName.contains(affix)) {
                newName = removeAffix(newName, affix);
            }

            if (!fascicoloNewInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
                props.put(it.kdm.docer.sdk.Constants.fascicolo_enabled, fascicoloNewInfo.getEnabled().toString().toLowerCase());

            for (String key : fascicoloNewInfo.getExtraInfo().keySet()) {
                props.put(key.toUpperCase(), fascicoloNewInfo.getExtraInfo().get(key));
            }

            boolean inheritsACL = false;
            boolean modifyPermissions = false;
            if (CIFS && fascicoloNewInfo.getExtraInfo().containsKey(it.kdm.docer.sdk.Constants.inherits_acl)) {
                modifyPermissions = true;
                inheritsACL = parseInheritsACL(fascicoloNewInfo.getExtraInfo().remove(it.kdm.docer.sdk.Constants.inherits_acl));
                props.put(it.kdm.docer.sdk.Constants.inherits_acl, Boolean.toString(inheritsACL));
            }

            updateAlfrescoSpace("Fascicolo", newName, fascicoloRef, props);

            if (modifyPermissions) {
                setInheritsRunasAdmin(fascicoloRef, inheritsACL);
            }

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    public void updateTitolario(ITitolarioId titolarioId, ITitolarioInfo titolarioNewInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "updateTitolario";
        try {
            if (titolarioNewInfo == null)
                throw new DocerException("titolarioNewInfo is null");

            String classifica = titolarioId.getClassifica();
            String codiceEnte = titolarioId.getCodiceEnte();
            String codiceAOO = titolarioId.getCodiceAOO();
            String codiceTitolario = titolarioId.getCodiceTitolario();

            if (codiceEnte == null)
                codiceEnte = "";
            if (codiceAOO == null)
                codiceAOO = "";
            if (classifica == null)
                classifica = "";
            if (codiceTitolario == null)
                codiceTitolario = "";

            classifica = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_classifica, classifica);
            codiceAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_cod_aoo, codiceAOO);
            codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_cod_ente, codiceEnte);
            codiceTitolario = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_cod_titolario, codiceTitolario);

            // String oldTitolarioRef = getTitolarioUUID(classifica,
            // codiceTitolario, codiceAOO, codiceEnte);
            SearchResultSet result = getTitolario(classifica, codiceTitolario, codiceAOO, codiceEnte);

            SearchResult sr = result.getFirstAndOnly();

            if (sr == null)
                throw new DocerException("voce di Titolario non trovata");

            if (result.getCount() > 1)
                throw new DocerException("trovate " + result.getCount() + " voci di Titolario");

            Reference titolarioRef = sr.getReference();

            EnumACLRights effectiveRights = sr.getEffectiveRights();
            if (!effectiveRights.equals(EnumACLRights.normalAccess) && !effectiveRights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            Map<String, String> props = new HashMap<String, String>();

            String newName = titolarioNewInfo.getDescrizione();
            if (newName != null)
                props.put(it.kdm.docer.sdk.Constants.titolario_des_titolario, newName);

            if (!titolarioNewInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
                props.put(it.kdm.docer.sdk.Constants.titolario_enabled, titolarioNewInfo.getEnabled().toString().toLowerCase());

            for (String key : titolarioNewInfo.getExtraInfo().keySet()) {
                props.put(key.toUpperCase(), titolarioNewInfo.getExtraInfo().get(key));
            }

            boolean inheritsACL = false;
            boolean modifyPermissions = false;
            if (CIFS && titolarioNewInfo.getExtraInfo().containsKey(it.kdm.docer.sdk.Constants.inherits_acl)) {
                modifyPermissions = true;
                inheritsACL = parseInheritsACL(titolarioNewInfo.getExtraInfo().remove(it.kdm.docer.sdk.Constants.inherits_acl));
                props.put(it.kdm.docer.sdk.Constants.inherits_acl, Boolean.toString(inheritsACL));
            }

            updateAlfrescoSpace("Titolario", newName, titolarioRef, props);

            if (modifyPermissions) {
                setInheritsRunasAdmin(titolarioRef, inheritsACL);
            }
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    public void updateGroup(String groupId, IGroupProfileInfo groupNewInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        // in alfresco non posso modificare nulla del profilo del gruppo
        String metodo = "updateGroup";
        String rif = groupId;

        try {
            if (groupId == null)
                throw new DocerException("Specificare groupId");

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put(it.kdm.docer.sdk.Constants.group_group_id, Arrays.asList(groupId));
            String lucenequery = LuceneUtility.buildAlfrescoGroupsSearchQueryString(searchCriteria);

            SearchResultSet webScriptResults = webScriptGroupsSearch(false, lucenequery, -1, ADMIN_USERID, null);

            if (webScriptResults.getCount() == 0) {
                throw new DocerException("gruppo " + groupId + " non trovato");
            }

            if (webScriptResults.getCount() != 1) {
                throw new DocerException("trovati " + webScriptResults.getCount() + " gruppi con groupId " + groupId);
            }

            SearchResult srGroup = webScriptResults.getFirstAndOnly();

            if (groupNewInfo.getParentGroupId() != null && !groupNewInfo.getParentGroupId().equals("")) {

                if (groupNewInfo.getParentGroupId().equals(groupId)) {
                    throw new DocerException("gruppo parent coincide con il gruppo stesso");
                }

                IGroupProfileInfo parentgroupprofile = getGroupProfile(groupNewInfo.getParentGroupId());

                if (parentgroupprofile == null) {
                    throw new DocerException("gruppo parent " + groupNewInfo.getParentGroupId() + " non trovato");
                }
            }

            completeGroupProfile(srGroup.getReference().getUuid(), groupNewInfo);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public void updateUser(String userId, IUserProfileInfo userNewInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "updateUser";
        String rif = userId;

        try {
            if (userId == null)
                throw new DocerException("specificare userId");

            IUserInfo oldUserInfo = _getUser(userId);
            if (oldUserInfo == null)
                throw new DocerException("user " + userId + " non trovato");

            if (userNewInfo.getUserId() != null && !userNewInfo.getUserId().equals(userId))
                throw new DocerException("non e' possibile modificare userId");

            IUserProfileInfo oldUserProfileInfo = oldUserInfo.getProfileInfo();

            userId = oldUserProfileInfo.getUserId();

            String oldCodEnte = oldUserProfileInfo.getExtraInfo().get(it.kdm.docer.sdk.Constants.user_cod_ente);
            // String oldCodAOO =
            // oldUserProfileInfo.getExtraInfo().get(it.kdm.docer.sdk.Constants.user_cod_aoo);

            String newEnteGroupId = null;
            String newAooGroupId = null;

            String newCodiceEnte = null;
            String newCodiceAOO = null;
            String newCodiceFiscale = null;

            if (userNewInfo.getExtraInfo() != null) {
                for (String key : userNewInfo.getExtraInfo().keySet()) {
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_ente)) {
                        newCodiceEnte = userNewInfo.getExtraInfo().get(key);
                        continue;
                    }
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_aoo)) {
                        newCodiceAOO = userNewInfo.getExtraInfo().get(key);
                        continue;
                    }
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_fiscale)) {
                        newCodiceFiscale = userNewInfo.getExtraInfo().get(key);
                        continue;
                    }
                }
            }

            if (newCodiceEnte != null && !newCodiceEnte.equals("")) {

                String appoNewCodiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.ente_cod_ente, newCodiceEnte);
                newEnteGroupId = getCaseSensitiveGroupId(appoNewCodiceEnte);

                if (newEnteGroupId == null) {
                    throw new DocerException("nuovo gruppo Ente " + appoNewCodiceEnte + " non trovato");
                }
            }

            if (newCodiceAOO != null && !newCodiceAOO.equals("")) {

                String aooCodEnte = "";
                if (newCodiceEnte != null && !newCodiceEnte.equals("")) {
                    aooCodEnte = newCodiceEnte;
                } else {
                    aooCodEnte = oldCodEnte;
                }

                newCodiceAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.aoo_cod_aoo, newCodiceAOO);

                String appoNewAooGroupId = generateAOOGroupId(newCodiceAOO, aooCodEnte);

                newAooGroupId = getCaseSensitiveGroupId(appoNewAooGroupId);

                if (newAooGroupId == null) {
                    throw new DocerException("nuovo gruppo AOO " + appoNewAooGroupId + " non trovato");
                }
            }

            // proprieta' dell'user
            List<NamedValue> properties = new ArrayList<NamedValue>();
            UserDetails userDetail = new UserDetails();
            userDetail.setUserName(oldUserProfileInfo.getUserId()); // identificatore

            if (userNewInfo.getEmailAddress() != null) {
                NamedValue nv = new NamedValue();
                nv.setName(DocerModel.PROP_USER_MAIL);
                nv.setValue(userNewInfo.getEmailAddress());
                properties.add(nv);
            }

            if (userNewInfo.getFirstName() != null) {
                NamedValue nv = new NamedValue();
                nv.setName(DocerModel.PROP_USER_FIRSTNAME);
                nv.setValue(userNewInfo.getFirstName());
                properties.add(nv);
            }

            if (userNewInfo.getLastName() != null) {
                NamedValue nv = new NamedValue();
                nv.setName(DocerModel.PROP_USER_LASTNAME);
                nv.setValue(userNewInfo.getLastName());
                properties.add(nv);
            }

            if (userNewInfo.getFullName() != null) {// full name mappato nel
                // middlename
                NamedValue nv = new NamedValue();
                nv.setName(DocerModel.PROP_USER_FULLNAME);
                nv.setValue(userNewInfo.getFullName());
                properties.add(nv);
            }

            if (newCodiceEnte != null) { // ente (viene assegnato alla
                // organizationalId)
                NamedValue nv = new NamedValue();
                nv.setName(DocerModel.PROP_USER_COD_ENTE);
                nv.setValue(newCodiceEnte);
                properties.add(nv);
            }

            if (newCodiceAOO != null) { // aoo (viene assegnato alla
                // organizationalId)
                NamedValue nv = new NamedValue();
                nv.setName(DocerModel.PROP_USER_COD_AOO);
                nv.setValue(newCodiceAOO);
                properties.add(nv);
            }

            if (newCodiceFiscale != null) { // aoo (viene assegnato alla
                // organizationalId)
                NamedValue nv = new NamedValue();
                nv.setName(DocerModel.PROP_USER_COD_FISCALE);
                nv.setValue(newCodiceFiscale);
                properties.add(nv);
            }

            Map<String, String> props = new HashMap<String, String>();
            if (userNewInfo.getExtraInfo() != null) {
                for (String key : userNewInfo.getExtraInfo().keySet()) {
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_ente)) {
                        continue;
                    }
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_aoo)) {
                        continue;
                    }
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_user_id)) {
                        continue;
                    }
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_user_password)) {
                        continue;
                    }
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_fiscale)) {
                        continue;
                    }

                    props.put(key, userNewInfo.getExtraInfo().get(key));
                }
            }

            populateAlfrescoProperties(props, properties);

            if (properties.size() > 0)
                userDetail.setProperties(properties.toArray(new NamedValue[0]));

            if (userDetail.getProperties() != null && userDetail.getProperties().length > 0) {

                String[] users = new String[]{userId};

                if (newEnteGroupId != null && !newEnteGroupId.equals("")) {
                    getAlfrescoAccessControlService_removeChildAuthorities(Constants.GROUP_PREFIX + newEnteGroupId, users);
                    getAlfrescoAccessControlService_addChildAuthorities(Constants.GROUP_PREFIX + newEnteGroupId, users);
                }
                // if(removeFromOldPrimaryGroup)
                // WebServiceFactory.getAccessControlService().removeChildAuthorities(Constants.GROUP_PREFIX
                // + oldPrimaryGroupId, users);

                if (newAooGroupId != null && !newAooGroupId.equals("")) {
                    getAlfrescoAccessControlService_removeChildAuthorities(Constants.GROUP_PREFIX + newAooGroupId, users);
                    getAlfrescoAccessControlService_addChildAuthorities(Constants.GROUP_PREFIX + newAooGroupId, users);
                }
                // if(removeFromOldSecondaryGroup)
                // WebServiceFactory.getAccessControlService().removeChildAuthorities(Constants.GROUP_PREFIX
                // + oldSecondaryGroupId, users);

                getAlfrescoAdministrationService_updateUsers(new UserDetails[]{userDetail});

            }

            if (userNewInfo.getUserPassword() != null) {

                if (userNewInfo.getUserPassword().equals("")) {
                    throw new DocerException("changePassword: non e' ammesso inserire una USER_PASSWORD vuota");
                }

                try {
                    WebServiceFactory.getAdministrationService().changePassword(userId, null, userNewInfo.getUserPassword());
                } catch (AdministrationFault e) {
                    throw new DocerException("changePassword: " + e.getMessage1());
                } catch (RemoteException e) {
                    if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                        throw new DocerException(UNAUTH_SESSION_ERROR);
                    }
                    throw new DocerException("changePassword: " + e.getMessage());
                }

                // catch (AccessControlFault e) {
                // throw new DocerException("changePassword: " +
                // e.getMessage1());
                // }
                // catch (RemoteException e) {
                // if
                // (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE)
                // > -1) {
                // throw new DocerException(UNAUTH_SESSION_ERROR);
                // }
                // throw new DocerException("changePassword: " +
                // e.getMessage());
                // }
            }

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public void addRelatedDocuments(String docId, List<String> items) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "addRelatedDocuments";
        String rif = docId;

        // ciclo su associated docId
        // ciclo su associated di ogni item
        // associazione se gia' non esiste
        // se non esiste associo
        try {
            if (items == null || items.size() == 0)
                return;

            if (items.size() == 1 && items.get(0).equals(docId))
                return;

            List<String> internalItems = new ArrayList<String>();
            internalItems.add(docId);
            for (String id : items) {
                if (internalItems.contains(id))
                    continue;
                internalItems.add(id);
            }

            String lucenequery = "";
            for (String id : internalItems) {
                lucenequery += "@docarea\\:docnum:\"" + id + "\" ";
            }

            SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, null, -1, null, true, false, false, false, false, false, null);

            // Map<String, Reference> toAddRelatedMap =
            // webScriptResults.getDocidToReferenceMap();

            // controllo esistenza di tutti i documenti
            if (webScriptResults.getCount() != internalItems.size()) {
                for (String id : internalItems) {

                    SearchResult sr = webScriptResults.get(new DocId(id));
                    if (sr != null) {
                        continue;
                    }
                    throw new DocerException("documento " + id + " non trovato");
                }
            }

            SearchResult srDoc = webScriptResults.get(new DocId(docId));

            // recupero la lista dei related associati al documento
            Map<String, Reference> mainRelatedChainMap = srDoc.getRelatedMap();
            // la modifico (tanto la uso solo qui)
            mainRelatedChainMap.put(docId, srDoc.getReference());

            List<CMLCreateAssociation> tocreateAssocList = new ArrayList<CMLCreateAssociation>();

            Map<String, Reference> toaddRelatedChainMap = null;

            // ciclo tutti gli elementi da aggiungere
            for (String toaddRelatedId : internalItems) {

                SearchResult srToAddRelated = webScriptResults.get(new DocId(toaddRelatedId));

                // se fa parte della catena non lo aggiungo
                if (mainRelatedChainMap.containsKey(toaddRelatedId))
                    continue;

                toaddRelatedChainMap = srToAddRelated.getRelatedMap();

                // modifico collezione tanto la uso solo internamente
                // inserisco il related stesso alla collezione per comodita'
                toaddRelatedChainMap.put(toaddRelatedId, srToAddRelated.getReference());

                // creo tutte le associazioni mancanti

                for (String toaddId : toaddRelatedChainMap.keySet()) {

                    Reference toaddRef = toaddRelatedChainMap.get(toaddId);

                    // creo le associazioni con tutti i documenti della main
                    // chain
                    for (String mainChainMemberId : mainRelatedChainMap.keySet()) {

                        // se il rel da aggiungere fa gia' parte della
                        // catena...continuo
                        if (mainRelatedChainMap.containsKey(toaddId))
                            continue;

                        Reference mainChainMemberRef = mainRelatedChainMap.get(mainChainMemberId);

                        tocreateAssocList.add(getAddRelatedAssoc(mainChainMemberRef, toaddRef));
                        tocreateAssocList.add(getAddRelatedAssoc(toaddRef, mainChainMemberRef));
                    }

                }

                for (String toaddId : toaddRelatedChainMap.keySet()) {
                    mainRelatedChainMap.put(toaddId, toaddRelatedChainMap.get(toaddId));
                }

            }

            checkEffectiveRightsForNormalaccess(mainRelatedChainMap.keySet(), "addRelatedDocuments");

            // eseguo l'aggiornamento su Alfresco
            if (tocreateAssocList.size() > 0) {
                CML associations = new CML();

                int chunkSize = 20;

                if (tocreateAssocList.size() > chunkSize) {

                    int offSet;
                    // Processo i chunk
                    for (offSet = 0; offSet + chunkSize < tocreateAssocList.size(); offSet += chunkSize) {
                        CMLCreateAssociation[] assocs = new CMLCreateAssociation[chunkSize];
                        for (int i = 0; i < assocs.length; i++) {
                            assocs[i] = tocreateAssocList.get(i + offSet);
                        }

                        associations.setCreateAssociation(assocs);
                        getAlfrescoRepositoryService_update(associations);
                    }

                    // Processo gli elementi rimanenti se ce ne sono
                    // Offset non può essere minore della size perché è la condizione di terminazione del ciclo
                    // precedente. Se offSet è uguale alla size allora size è un multiplo di chunkSize e tutti gli
                    // elementi sono stati processati. Se offSet è maggiore allora rimane qualcosa da processare.
                    if (offSet + chunkSize > tocreateAssocList.size()) {
                        CMLCreateAssociation[] assocs = new CMLCreateAssociation[tocreateAssocList.size() - offSet];
                        for (int i = 0; i < assocs.length; i++) {
                            assocs[i] = tocreateAssocList.get(i + offSet);
                        }

                        associations.setCreateAssociation(assocs);
                        getAlfrescoRepositoryService_update(associations);
                    }

                } else {
                    associations.setCreateAssociation(tocreateAssocList.toArray(
                            new CMLCreateAssociation[tocreateAssocList.size()]));

                    getAlfrescoRepositoryService_update(associations);
                }
            }

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public void removeRelatedDocuments(String docId, List<String> items) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "removeRelatedDocuments";
        String rif = docId;

        try {
            // ciclo su associated docId
            // ciclo su associated di ogni item
            // associazione se gia' non esiste
            // se non esiste associo
            // recupero la lista dei related associati al documento per
            // controllare
            // successivamente
            // che non siano gia' associati

            if (items == null || items.size() == 0)
                return;

            // non rimuovo da se stesso
            if (items.size() == 1 && items.get(0).equals(docId))
                return;

            List<String> internalItems = new ArrayList<String>();
            internalItems.add(docId);
            for (String id : items) {
                if (internalItems.contains(id))
                    continue;
                internalItems.add(id);
            }

            String lucenequery = "";
            for (String id : internalItems) {
                lucenequery += "@docarea\\:docnum:\"" + id + "\" ";
            }

            SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, null, -1, null, true, false, false, false, false, false, null);

            // Map<String, Reference> toRemoveRelatedMap =
            // webScriptResults.getDocidToReferenceMap();

            // controllo esistenza di tutti i documenti
            if (webScriptResults.getCount() != internalItems.size()) {
                for (String id : internalItems) {

                    SearchResult sr = webScriptResults.get(new DocId(id));

                    if (sr != null) {
                        continue;
                    }

                    throw new DocerException("documento " + id + " non trovato");
                }
            }

            SearchResult srMainDoc = webScriptResults.get(new DocId(docId));

            // catena related attuale
            // Map<String, Reference> relatedChain = srMainDoc.getRelatedMap();

            List<String> tocheckItems = new ArrayList<String>();

            List<CMLRemoveAssociation> toRemoveRelatedList = new ArrayList<CMLRemoveAssociation>();

            tocheckItems.add(docId);

            // ciclo i related effettivamente da sganciare
            for (SearchResult srToRemoveRelated : webScriptResults.getAllSearchResult()) {

                if (srToRemoveRelated.getDocId().equals(docId)) {
                    continue;
                }

                // rimuovo relazione con il principale
                toRemoveRelatedList.add(getRemoveRelatedAssoc(srMainDoc.getReference(), srToRemoveRelated.getReference()));
                toRemoveRelatedList.add(getRemoveRelatedAssoc(srToRemoveRelated.getReference(), srMainDoc.getReference()));

                if (!tocheckItems.contains(srToRemoveRelated.getDocId()))
                    tocheckItems.add(srToRemoveRelated.getDocId());

                for (String relOfChainId : srMainDoc.getRelatedMap().keySet()) {
                    // rimuovo doppia associazione A-->B B-->A
                    if (relOfChainId.equals(srToRemoveRelated.getDocId()) || relOfChainId.equals(docId))
                        continue;

                    Reference relRef = srMainDoc.getRelatedMap().get(relOfChainId);

                    toRemoveRelatedList.add(getRemoveRelatedAssoc(relRef, srToRemoveRelated.getReference()));
                    toRemoveRelatedList.add(getRemoveRelatedAssoc(srToRemoveRelated.getReference(), relRef));

                    if (!tocheckItems.contains(relOfChainId))
                        tocheckItems.add(relOfChainId);
                }

            }

            checkEffectiveRightsForNormalaccess(tocheckItems, "removeRelatedDocuments");

            if (toRemoveRelatedList.size() > 0) {
                CML associations = new CML();
                associations.setRemoveAssociation(toRemoveRelatedList.toArray(new CMLRemoveAssociation[0]));

                getAlfrescoRepositoryService_update(associations);

            }

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public void updateAOO(IAOOId aooId, IAOOInfo aooNewInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "updateAOO";

        try {
            if (aooId.getCodiceEnte() == null)
                throw new DocerException("COD_ENTE is null");
            if (aooId.getCodiceAOO() == null)
                throw new DocerException("COD_AOO is null");
            if (aooNewInfo == null)
                throw new DocerException("aooNewInfo is null");

            String codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.aoo_cod_ente, aooId.getCodiceEnte());
            String codiceAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.aoo_cod_aoo, aooId.getCodiceAOO());

            SearchResultSet result = _getAoo(codiceAOO, codiceEnte);
            if (result.getCount() == 0)
                throw new DocerException(codiceAOO + ": AOO non trovata");

            if (result.getCount() != 1)
                throw new DocerException(codiceAOO + ": trovate " + result.getCount() + " AOO");

            SearchResult srAoo = result.getFirstAndOnly();

            Reference aooNodeRef = srAoo.getReference();

            EnumACLRights aooEffectiverights = srAoo.getEffectiveRights();
            if (!aooEffectiverights.equals(EnumACLRights.normalAccess) && !aooEffectiverights.equals(EnumACLRights.fullAccess))
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");

            Map<String, String> props = new HashMap<String, String>();

            String nodeName = aooNewInfo.getDescrizione();
            if (nodeName != null)
                props.put(it.kdm.docer.sdk.Constants.aoo_des_aoo, nodeName);

            if (!aooNewInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
                props.put(it.kdm.docer.sdk.Constants.aoo_enabled, aooNewInfo.getEnabled().toString().toLowerCase());

            for (String key : aooNewInfo.getExtraInfo().keySet()) {
                props.put(key.toUpperCase(), aooNewInfo.getExtraInfo().get(key));
            }

            updateAlfrescoSpace("AOO", nodeName, aooNodeRef, props);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    public void updateEnte(IEnteId enteId, IEnteInfo enteNewInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "updateEnte";
        try {
            if (enteId.getCodiceEnte() == null)
                throw new DocerException("COD_ENTE is null");

            if (enteNewInfo == null)
                throw new DocerException("enteNewInfo is null");

            String codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.ente_cod_ente, enteId.getCodiceEnte());

            SearchResultSet result = _getEnte(codiceEnte);
            if (result.getCount() == 0)
                throw new DocerException(codiceEnte + ": Ente non trovato");

            if (result.getCount() != 1)
                throw new DocerException(codiceEnte + ": trovati " + result.getCount() + " Enti");

            SearchResult srEnte = result.getFirstAndOnly();

            Reference enteNodeRef = srEnte.getReference();

            EnumACLRights enteEffectiverights = srEnte.getEffectiveRights();
            if (!enteEffectiverights.equals(EnumACLRights.normalAccess) && !enteEffectiverights.equals(EnumACLRights.fullAccess))
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");

            Map<String, String> props = new HashMap<String, String>();

            if (enteNewInfo.getDescrizione() != null)
                props.put(it.kdm.docer.sdk.Constants.ente_des_ente, enteNewInfo.getDescrizione());

            if (!enteNewInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
                props.put(it.kdm.docer.sdk.Constants.ente_enabled, enteNewInfo.getEnabled().toString().toLowerCase());

            for (String key : enteNewInfo.getExtraInfo().keySet()) {
                props.put(key.toUpperCase(), enteNewInfo.getExtraInfo().get(key));
            }

            updateAlfrescoSpace("Ente", enteNewInfo.getDescrizione(), enteNodeRef, props);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    public List<String> getRelatedDocuments(String docId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "getRelatedDocuments";
        String rif = docId;

        try {

            SearchResultSet result = getUniqueDocument(docId, true, false, false, false, false, false);

            Map<String, Reference> map = result.getFirstAndOnly().getRelatedMap();

            // nelle key della mappa dei related ci sono i docid, nei values gli
            // uuid
            return new ArrayList<String>(map.keySet());

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public ILockStatus isCheckedOutDocument(String docId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "isCheckedOutDocument";
        String rif = docId;

        try {

            SearchResultSet webScriptResults = getUniqueDocument(docId, false, false, false, true, false, false);

            return webScriptResults.getFirstAndOnly().getLockStatus();

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public void lockDocument(String docId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "lockDocument";
        String rif = docId;

        try {
            SearchResultSet result = getUniqueDocument(docId, false, false, false, true, false, true);

            Reference nodeRef = result.getFirstAndOnly().getReference();

            EnumACLRights effectiveRights = result.getFirstAndOnly().getEffectiveRights();
            if (!effectiveRights.equals(EnumACLRights.normalAccess) && !effectiveRights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            LockStatus lockstatus = result.getFirstAndOnly().getLockStatus();

            if (lockstatus.getLocked()) {
                if (lockstatus.getUserId().equals(currentUser.getUserId())) {
                    return;
                }
                throw new DocerException("il documento e' gia' bloccato esclusivamente da un altro utente");
            }

            Predicate predicate = createPredicate(nodeRef);

            getAlfrescoAuthoringService_lock(predicate);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public void unlockDocument(String docId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "unlockDocument";
        String rif = docId;

        try {
            SearchResultSet result = getUniqueDocument(docId, false, false, false, true, false, true);

            EnumACLRights effectiveRights = result.getFirstAndOnly().getEffectiveRights();
            if (!effectiveRights.equals(EnumACLRights.normalAccess) && !effectiveRights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            LockStatus lockstatus = result.getFirstAndOnly().getLockStatus();

            if (!lockstatus.getLocked()) {
                return; // throw new
                // DocerException("il documento non e' in stato di lock");
            }

            Predicate predicate = createPredicate(result.getFirstAndOnly().getReference());

            getAlfrescoAuthoringService_unlock(predicate);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public void logout() throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        try {
            WebServiceFactory.getAuthenticationService().endSession(currentUser.getTicket());
        } catch (AuthenticationFault e) {
            // throw new DocerException(-1001, nomeMetodo + ": " + rif + ":" +
            // e.getMessage1());
        } catch (RemoteException e) {
            // throw new DocerException(-1001, nomeMetodo + ": " + rif + ":" +
            // e.getMessage());
        }

    }

    public void replaceLastVersion(String docId, InputStream content) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "replaceLastVersion";
        String rif = docId;

        try {
            SearchResultSet result = getUniqueDocument(docId, false, false, false, false, false, true);

            EnumACLRights effectiveRights = result.getFirstAndOnly().getEffectiveRights();
            if (!effectiveRights.equals(EnumACLRights.normalAccess) && !effectiveRights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            // String filename = getFileName(nodeRef);
            String filename = result.getFirstAndOnly().getCmName();
            // upload sulla versione di checkout
            uploadFile(result.getFirstAndOnly().getReference(), filename, content);

            setAuthor(result.getFirstAndOnly().getReference(), currentUser.getUserId());

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public void setCurrentUser(ILoggedUserInfo info) throws DocerException {

        if (info.getUserId() == null || info.getUserId().equals("")) {
            throw new DocerException(-4009, "setCurrentUser: userId con valore null o vuoto: " + info.getUserId());

        }
        if (currentUser == null)
            currentUser = new LoggedUserInfo();
        else {
            currentUser.setCodiceEnte(null);
            currentUser.setTicket(null);
            currentUser.setUserId(null);
        }

        if (info.getCodiceEnte() != null) {
            currentUser.setCodiceEnte(info.getCodiceEnte());
        }

        currentUser.setTicket(info.getTicket());
        currentUser.setUserId(info.getUserId());

        // boolean valido = verifyTicket(info.getUserId(),
        // currentUser.getCodiceEnte(), currentUser.getTicket());
        //
        // if (!valido) {
        // throw new DocerException(-4010, UNAUTH_SESSION_ERROR);
        // }
        AuthenticationDetails authDetails = new AuthenticationDetails(currentUser.getUserId(), currentUser.getTicket(), null);
        AuthenticationUtils.setAuthenticationDetails(authDetails);
        // non possiamo utilizzare il controllo del timeout di alfresco

        // boolean valid = AuthenticationUtils.isCurrentTicketTimedOut();
        // if(!valid)
        // throw new
        // DocerException(-2147221096,"ticket non valido o utente non autorizzato");

        String caseSensitiveUserId = getCaseSensitiveUserId(info.getUserId());
        currentUser.setUserId(caseSensitiveUserId);

    }

    public boolean verifyTicket(String userId, String codiceEnte, String ticket) throws DocerException {


        // per i posteri questo metodo va sostituito con una chiamata http:
        //http://192.168.0.174:8081/alfresco/service/api/login/ticket/{alf_ticket}
        // se torna http code 202 OK
        // se torna http code 404 --> scaduto	

        AuthenticationDetails authDetails = new AuthenticationDetails(userId, ticket, null);
        AuthenticationUtils.setAuthenticationDetails(authDetails);

        Query query = new Query();

        // Create a query object
        String search = DocerModel.SEARCH_QUERY_WORKSPACE;
        search = search.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(""));
        query.setStatement(search);

        // QueryResult qresult = null;

        try {
            // qresult =
            getAlfrescoRepositoryService_query(query);
            return true;
        } catch (DocerException e) {

            if (e.getMessage().equals(UNAUTH_SESSION_ERROR))
                return false;

            throw new DocerException(-1000, "verifyTicket: " + e.getMessage());

        } finally {
            AuthenticationUtils.setAuthenticationDetails(null);
        }

    }

    public String login(String userName, String password, String codiceEnte) throws DocerException {

        if (userName == null || userName.equals(""))
            throw new DocerException(-1000, "login: userName obbligatorio");

        if (password == null)
            password = "";

        String nomeMetodo = "login";
        String rif = userName;
        try {

            AuthenticationServiceSoapBindingStub auth = WebServiceFactory.getAuthenticationService();

            AuthenticationResult result = auth.startSession(userName, password);

            if (ALFRESCO_PROPERTIES_MAP.size() == 0) {
                // il primo che fa login recupera tutte le proprieta' alfresco
                // (static)
                AuthenticationDetails authDetails = new AuthenticationDetails(userName, result.getTicket(), null);
                AuthenticationUtils.setAuthenticationDetails(authDetails);
                getAlfrescoAllProperties();
            }

            return result.getTicket();
        } catch (AuthenticationFault e) {
            throw new DocerException(-2147221071, e.getMessage1());
        } catch (RemoteException e) {
            throw new DocerException(-1027, nomeMetodo + ": " + rif + ": " + e.getMessage());
        } finally {
            // rimetto a null il current user (deve essere impostato ad ogni
            // istanza del provider)
            AuthenticationUtils.setAuthenticationDetails(null);

            currentUser = null;
        }
    }

    @Override
    public ISsoUserInfo loginSSO(String saml, String codiceEnte) throws DocerException {

        LoginBO loginBO = new LoginBO(this, codiceEnte);
        return loginBO.loginSso(saml);

    }

    public void createGroup(IGroupProfileInfo groupProfileInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "createGroup";
        try {
            String groupId = groupProfileInfo.getGroupId();
            // String parentGroupId = groupProfileInfo.getParentGroupId();

            if (groupId == null || groupId.equals(""))
                throw new DocerException("specificare GROUP_ID");

            // if (parentGroupId == null || parentGroupId.equals(""))
            // throw new DocerException("specificare PARENT_GROUP_ID");

            // proibiamo la creazione di Gruppi con id come i Prefix dei Gruppi
            // Amministratori
            if (groupId.toUpperCase().startsWith(ENTE_ADMINS_GROUPID_PREFIX.toUpperCase())) {
                throw new DocerException("il prefisso " + ENTE_ADMINS_GROUPID_PREFIX + " e' riservato agli Amministratori di Ente");
            }

            if (groupId.toUpperCase().startsWith(AOO_ADMINS_GROUPID_PREFIX.toUpperCase())) {
                throw new DocerException("il prefisso " + AOO_ADMINS_GROUPID_PREFIX + " e' riservato agli Amministratori di AOO");
            }

            createGroupInAlfresco(groupProfileInfo);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    public void createEnte(IEnteInfo enteInfo) throws DocerException {

        // REPOSITORY_CACHE.clear();

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "createEnte";
        Reference enteRef = null;

        try {
            if (enteInfo == null)
                throw new DocerException("enteInfo is null");

            String codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.ente_cod_ente, enteInfo.getCodiceEnte());
            if (codiceEnte == null) {
                codiceEnte = "";
            }

            Map<String, String> props = new HashMap<String, String>();
            props.put(it.kdm.docer.sdk.Constants.ente_cod_ente, enteInfo.getCodiceEnte());
            props.put(it.kdm.docer.sdk.Constants.ente_des_ente, enteInfo.getDescrizione());
            props.put(it.kdm.docer.sdk.Constants.ente_enabled, enteInfo.getEnabled().toString());

            for (String key : enteInfo.getExtraInfo().keySet()) {
                props.put(key, enteInfo.getExtraInfo().get(key));
            }

            List<NamedValue> properties = new ArrayList<NamedValue>();
            populateAlfrescoProperties(props, properties);

            String nodeName = adjustNodeName(enteInfo.getDescrizione());
            properties.add(new NamedValue(Constants.PROP_NAME, false, nodeName, null));
            properties.add(new NamedValue(DocerModel.PROP_ICON, false, "space-icon-ente", null));

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put("COD_ENTE", Arrays.asList(codiceEnte));

            int count = countAnagraficaAsAdmin("ENTE", searchCriteria);

            if (count > 0)
                throw new DocerException("verifica univocita' anagrafica sul documentale: esiste gia' un Ente con COD_ENTE " + codiceEnte);

            String enteGroupId = codiceEnte;
            String adminsEnteGroupId = generateEnteAdminsGroupId(codiceEnte);

            SearchResultSet webScriptResult = getAlfrescoSpace(DocerModel.SEARCH_QUERY_WORKSPACE);
            if (webScriptResult.getCount() == 0) {
                throw new DocerException("Spazio WorkSpace non trovato");
            }

            String libraryName = getLibraryForEnte(currentUser.getCodiceEnte());

            webScriptResult = getNodeChildrenByName(webScriptResult.getFirstAndOnly().getReference(), libraryName);

            if (webScriptResult.getCount() == 0) {
                throw new DocerException("Spazio Library " + libraryName + " non trovato");
            }

            EnumACLRights effectiverights = webScriptResult.getFirstAndOnly().getEffectiveRights();
            if (!effectiverights.equals(EnumACLRights.normalAccess) && !effectiverights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            // nodo della libreria
            String pathLibrary = DocerModel.PATH_LIBRARY.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(libraryName));
            ParentReference nodeLibrary = createParentReference(pathLibrary, null, Constants.ASSOC_CONTAINS, Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, nodeName));

            // proprieta' del nodo ente da creare

            CMLCreate createEnte = new CMLCreate();
            createEnte.setParent(nodeLibrary);
            createEnte.setType(DocerModel.TYPE_ENTE);
            createEnte.setProperty(properties.toArray(new NamedValue[0]));

            String pathEnte = DocerModel.PATH_ENTE.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));
            pathEnte = pathEnte.replace(DocerModel.COD_ENTE_REPLACER, ISO9075.encode(nodeName));

            // la folder del repository dei documenti la creiamo sotto l'ENTE
            ParentReference nodeEnte = createParentReference(pathEnte, null, Constants.ASSOC_CONTAINS, Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, DocerModel.DOCUMENTI_SPACE_NAME));

            // CML create per spazio DOCUMENTI
            CMLCreate createSpaceDocumenti = new CMLCreate();
            createSpaceDocumenti.setParent(nodeEnte);
            createSpaceDocumenti.setType(Constants.TYPE_FOLDER);

            NamedValue[] propertiesSpaceDocumenti = new NamedValue[1];
            propertiesSpaceDocumenti[0] = new NamedValue();
            propertiesSpaceDocumenti[0].setName(Constants.PROP_NAME);
            propertiesSpaceDocumenti[0].setValue(DocerModel.DOCUMENTI_SPACE_NAME);
            createSpaceDocumenti.setProperty(propertiesSpaceDocumenti);

            CML cml = new CML();
            cml.setCreate(new CMLCreate[]{createEnte, createSpaceDocumenti});

            UpdateResult[] result = null;

            // se esiste ci pensa il web-script a non fare casini
            createGroupRunasAdmin(SYS_ADMINS_GROUPID, "Amministratori di Sistema", "", false);
            createGroupRunasAdmin(enteGroupId, enteInfo.getDescrizione(), "", true);
            createGroupRunasAdmin(adminsEnteGroupId, String.format("%s (admins)", enteInfo.getDescrizione()), "", false);

            // creo l'anagrafica ente e lo spazio dei documenti
            result = getAlfrescoRepositoryService_update(cml);

            if (result == null || result.length < 2)
                throw new DocerException("errore creazione ente");

            enteRef = result[0].getDestination();
            Reference repositorySpaceRef = result[1].getDestination();

            // assegnamo le ACL

            ACE[] ace = new ACE[3];
            // readOnly al gruppo Ente
            ace[0] = new ACE();
            ace[0].setAuthority(Constants.GROUP_PREFIX + enteGroupId);
            ace[0].setPermission(convertToAlfrescoRights(EnumACLRights.readOnly));
            ace[0].setAccessStatus(AccessStatus.acepted);

            // fullAccess agli amministratori di Ente (per modificare acl quando
            // creaiamo le AOO)
            ace[1] = new ACE();
            ace[1].setAuthority(Constants.GROUP_PREFIX + adminsEnteGroupId);
            ace[1].setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            ace[1].setAccessStatus(AccessStatus.acepted);

            // normalAccess agli amministratori di sistema
            ace[2] = new ACE();
            ace[2].setAuthority(Constants.GROUP_PREFIX + SYS_ADMINS_GROUPID);
            ace[2].setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            ace[2].setAccessStatus(AccessStatus.acepted);

            // assegno ACL:
            // no ereditarieta'
            // readOnly all'Ente
            // normalAccess agli Admins dell'Ente
            setACESRunasAdmin(enteRef, ace, false);

            // ora assegnamo la ACE allo spazio dei documenti
            // normalAccess al Gruppo Ente
            // ace = new ACE[1];
            // ace[0] = new ACE();
            // ace[0].setAuthority(Constants.GROUP_PREFIX + enteGroupId);
            // ace[0].setPermission(convertToAlfrescoRight(EnumACLRights.normalAccess));
            // ace[0].setAccessStatus(AccessStatus.acepted);

            ace = new ACE[3];
            // readOnly al gruppo Ente
            ace[0] = new ACE();
            ace[0].setAuthority(Constants.GROUP_PREFIX + enteGroupId);
            ace[0].setPermission(convertToAlfrescoRights(EnumACLRights.normalAccess));
            ace[0].setAccessStatus(AccessStatus.acepted);

            // readOnly agli amministratori di Ente (non possono creare)
            ace[1] = new ACE();
            ace[1].setAuthority(Constants.GROUP_PREFIX + adminsEnteGroupId);
            ace[1].setPermission(convertToAlfrescoRights(EnumACLRights.readOnly));
            ace[1].setAccessStatus(AccessStatus.acepted);

            // readOnly agli amministratori di sistema (non possono creare)
            ace[2] = new ACE();
            ace[2].setAuthority(Constants.GROUP_PREFIX + SYS_ADMINS_GROUPID);
            ace[2].setPermission(convertToAlfrescoRights(EnumACLRights.readOnly));
            ace[2].setAccessStatus(AccessStatus.acepted);

            setACESRunasAdmin(repositorySpaceRef, ace, false);

        } catch (DocerException e) {
            if (enteRef != null)
                deleteNodes(new String[]{enteRef.getUuid()});
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    public void createAOO(IAOOInfo aooInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "createAOO";
        Reference aooRef = null;

        try {
            if (aooInfo == null)
                throw new DocerException("aooInfo is null");

            String codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.aoo_cod_ente, aooInfo.getCodiceEnte());
            String codiceAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.aoo_cod_aoo, aooInfo.getCodiceAOO());
            String descrizioneAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.aoo_des_aoo, aooInfo.getDescrizione());

            if (codiceEnte == null) {
                codiceEnte = "";
            }
            if (codiceAOO == null) {
                codiceAOO = "";
            }
            if (descrizioneAOO == null) {
                descrizioneAOO = "";
            }

            Map<String, String> props = new HashMap<String, String>();
            props.put(it.kdm.docer.sdk.Constants.aoo_cod_ente, aooInfo.getCodiceEnte());
            props.put(it.kdm.docer.sdk.Constants.aoo_cod_aoo, aooInfo.getCodiceAOO());
            props.put(it.kdm.docer.sdk.Constants.aoo_des_aoo, aooInfo.getDescrizione());
            props.put(it.kdm.docer.sdk.Constants.aoo_enabled, aooInfo.getEnabled().toString());

            for (String key : aooInfo.getExtraInfo().keySet()) {
                props.put(key, aooInfo.getExtraInfo().get(key));
            }

            List<NamedValue> properties = new ArrayList<NamedValue>();
            populateAlfrescoProperties(props, properties);

            String nodeName = adjustNodeName(aooInfo.getDescrizione());
            properties.add(new NamedValue(Constants.PROP_NAME, false, nodeName, null));
            properties.add(new NamedValue(DocerModel.PROP_ICON, false, "space-icon-aoo", null));

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put(it.kdm.docer.sdk.Constants.aoo_cod_ente, Arrays.asList(codiceEnte));
            searchCriteria.put(it.kdm.docer.sdk.Constants.aoo_cod_aoo, Arrays.asList(codiceAOO));
            int count = countAnagraficaAsAdmin(it.kdm.docer.sdk.Constants.aoo_type_id, searchCriteria);

            if (count > 0)
                throw new DocerException("verifica univocita' anagrafica sul documentale: esiste gia' una AOO con COD_ENTE " + codiceEnte + " e COD_AOO " + codiceAOO);

            String enteGroupId = codiceEnte;
            String aooGroupId = generateAOOGroupId(codiceAOO, codiceEnte);

            String adminsEnteGroupId = generateEnteAdminsGroupId(codiceEnte);
            String adminsAOOGroupId = generateAOOAdminsGroupId(codiceAOO, codiceEnte);

            String libraryName = getLibraryForEnte(currentUser.getCodiceEnte());

            SearchResultSet workspaceResult = getAlfrescoSpace(DocerModel.SEARCH_QUERY_WORKSPACE);
            if (workspaceResult.getCount() == 0) {
                throw new DocerException("Spazio WorkSpace non trovato");
            }

            Reference workspaceRef = workspaceResult.getFirstAndOnly().getReference();

            // ricerco la library
            SearchResultSet libraryResult = getNodeChildrenByName(workspaceRef, libraryName);
            if (libraryResult.getCount() == 0) {
                throw new DocerException("Spazio Library " + libraryName + " non trovato");
            }

            // ricerco l'ente
            SearchResultSet enteResult = _getEnte(codiceEnte);
            if (enteResult.getCount() == 0) {
                throw new DocerException("Ente " + codiceEnte + " non trovato");
            }

            Reference enteRef = enteResult.getFirstAndOnly().getReference();
            String descEnte = enteResult.getFirstAndOnly().getCmName();

            // dobbiamo avere fullAccess per impostare le ACL all'Ente
            EnumACLRights effectiverights = enteResult.getFirstAndOnly().getEffectiveRights();
            if (!effectiverights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            String pathEnte = DocerModel.PATH_ENTE.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(libraryName)).replace(DocerModel.COD_ENTE_REPLACER, ISO9075.encode(descEnte));
            String pathAOO = DocerModel.PATH_AOO.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(libraryName)).replace(DocerModel.COD_ENTE_REPLACER, ISO9075.encode(descEnte))
                    .replace(DocerModel.COD_AOO_REPLACER, ISO9075.encode(nodeName));
            String pathDocumenti = DocerModel.PATH_DOCUMENTI.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(libraryName)).replace(DocerModel.COD_ENTE_REPLACER, ISO9075.encode(descEnte))
                    .replace(DocerModel.COD_AOO_REPLACER, ISO9075.encode(nodeName));

            // nodo parent
            ParentReference parentRef = createParentReference(pathEnte, null, Constants.ASSOC_CONTAINS, Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, nodeName));

            CMLCreate createAoo = new CMLCreate();
            createAoo.setParent(parentRef);
            createAoo.setType(DocerModel.TYPE_AOO);
            createAoo.setProperty(properties.toArray(new NamedValue[0]));

            // la folder del repository dei documenti la creiamo sotto la AOO
            ParentReference nodeAOORef = createParentReference(pathAOO, null, Constants.ASSOC_CONTAINS,
                    Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, DocerModel.DOCUMENTI_SPACE_NAME));

            // CML create per spazio DOCUMENTI
            CMLCreate createSpaceDocumenti = new CMLCreate();
            createSpaceDocumenti.setParent(nodeAOORef);
            createSpaceDocumenti.setType(Constants.TYPE_FOLDER);

            NamedValue[] propertiesSpaceDocumenti = new NamedValue[1];
            propertiesSpaceDocumenti[0] = new NamedValue();
            propertiesSpaceDocumenti[0].setName(Constants.PROP_NAME);
            propertiesSpaceDocumenti[0].setValue(DocerModel.DOCUMENTI_SPACE_NAME);
            createSpaceDocumenti.setProperty(propertiesSpaceDocumenti);

            CMLCreate createSpaceRootFolder = getCMLCreateFolder(Constants.TYPE_FOLDER, String.valueOf((new DateTime()).getMillis()), null, pathAOO, DocerModel.ICON_DEFAULT);

            CML cml = new CML();
            cml.setCreate(new CMLCreate[]{createAoo, createSpaceDocumenti, createSpaceRootFolder});

            UpdateResult[] result = null;

            String id = getCaseSensitiveGroupId(adminsEnteGroupId);
            // se non esiste il gruppo degli amministratori dell'ente lo creo
            if (id == null) {
                throw new DocerException("Gruppo Amministratori di Ente " + adminsEnteGroupId + " non trovato");
            }

            createGroupRunasAdmin(aooGroupId, descrizioneAOO, enteGroupId, true);
            createGroupRunasAdmin(adminsAOOGroupId, String.format("%s (admins)", descrizioneAOO), "", false);

            // *** creo l'anagrafica AOO e il relativo spazio dei documenti ***
            result = getAlfrescoRepositoryService_update(cml);

            Reference repositorySpaceRef = result[1].getDestination();
            Reference folderRootRef = result[2].getDestination();

            if (result.length < 3)
                throw new DocerException("errore creazione AOO");

            String folderRootId = String.valueOf(getNodeDbId(folderRootRef));

            aooRef = result[0].getDestination();

            // ASSEGNAMO le ACL allo spazio ENTE
            // readOnly agli amministratori di AOO
            ACE[] ace = new ACE[1];
            ace[0] = new ACE();
            ace[0].setAuthority(Constants.GROUP_PREFIX + adminsAOOGroupId);
            ace[0].setPermission(convertToAlfrescoRights(EnumACLRights.readOnly));
            ace[0].setAccessStatus(AccessStatus.acepted);

            // setACESRunasAdmin(enteRef, ace, false);
            addACESRunasAdmin(enteRef, ace, false);

            // assegnamo la ACE alla AOO
            ace = new ACE[4];
            // readOnly al gruppo AOO
            ace[0] = new ACE();
            ace[0].setAuthority(Constants.GROUP_PREFIX + aooGroupId);
            ace[0].setPermission(convertToAlfrescoRights(EnumACLRights.readOnly));
            ace[0].setAccessStatus(AccessStatus.acepted);

            // normalAccess agli amministratori di Ente
            ace[1] = new ACE();
            ace[1].setAuthority(Constants.GROUP_PREFIX + adminsEnteGroupId);
            ace[1].setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            ace[1].setAccessStatus(AccessStatus.acepted);

            // normalAccess agli amministratori di AOO
            ace[2] = new ACE();
            ace[2].setAuthority(Constants.GROUP_PREFIX + adminsAOOGroupId);
            ace[2].setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            ace[2].setAccessStatus(AccessStatus.acepted);

            // normalAccess agli amministratori di sistema
            ace[3] = new ACE();
            ace[3].setAuthority(Constants.GROUP_PREFIX + SYS_ADMINS_GROUPID);
            ace[3].setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            ace[3].setAccessStatus(AccessStatus.acepted);

            setACESRunasAdmin(aooRef, ace, false);

            // ora assegnamo la ACE allo spazio dei documenti
            ace = new ACE[4];
            // normalAccess al gruppo AOO
            ace[0] = new ACE();
            ace[0].setAuthority(Constants.GROUP_PREFIX + aooGroupId);
            ace[0].setPermission(convertToAlfrescoRights(EnumACLRights.normalAccess));
            ace[0].setAccessStatus(AccessStatus.acepted);

            // readOnly agli amministratori di Ente (non possono creare
            // documenti)
            ace[1] = new ACE();
            ace[1].setAuthority(Constants.GROUP_PREFIX + adminsEnteGroupId);
            ace[1].setPermission(convertToAlfrescoRights(EnumACLRights.readOnly));
            ace[1].setAccessStatus(AccessStatus.acepted);

            // readOnly agli amministratori di AOO (non possono creare
            // documenti)
            ace[2] = new ACE();
            ace[2].setAuthority(Constants.GROUP_PREFIX + adminsAOOGroupId);
            ace[2].setPermission(convertToAlfrescoRights(EnumACLRights.readOnly));
            ace[2].setAccessStatus(AccessStatus.acepted);

            // readOnly agli amministratori di sistema (non possono creare
            // documenti)
            ace[3] = new ACE();
            ace[3].setAuthority(Constants.GROUP_PREFIX + SYS_ADMINS_GROUPID);
            ace[3].setPermission(convertToAlfrescoRights(EnumACLRights.readOnly));
            ace[3].setAccessStatus(AccessStatus.acepted);

            setACESRunasAdmin(repositorySpaceRef, ace, false);

            // ora assegnamo la ACE allo spazio delle Folder
            ace = new ACE[1];
            // contributor al gruppo AOO
            ace[0] = new ACE();
            ace[0].setAuthority(Constants.GROUP_PREFIX + aooGroupId);
            ace[0].setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            ace[0].setAccessStatus(AccessStatus.acepted);

            setACESRunasAdmin(folderRootRef, ace, false);

            // creo folder ROOT
            Map<String, String> upd = new HashMap<String, String>();
            upd.put(it.kdm.docer.sdk.Constants.folder_folder_id, folderRootId);
            upd.put(it.kdm.docer.sdk.Constants.folder_cod_ente, codiceEnte);
            upd.put(it.kdm.docer.sdk.Constants.folder_cod_aoo, codiceAOO);
            upd.put(it.kdm.docer.sdk.Constants.folder_des_folder, "Folder root delle Folder");
            upd.put(it.kdm.docer.sdk.Constants.folder_folder_name, CARTELLE_REPO);
            upd.put(it.kdm.docer.sdk.Constants.folder_parent_folder_id, "");

            // finalizeFolderProfile(folderRootUUID, folderProfile);
            updateAlfrescoFolder(true, folderRootRef, upd);

            createSpaceRootFolder = getCMLCreateFolder(Constants.TYPE_FOLDER, String.valueOf((new DateTime()).getMillis()), null, pathAOO, DocerModel.ICON_DEFAULT);

            cml = new CML();
            cml.setCreate(new CMLCreate[]{createSpaceRootFolder});

            result = getAlfrescoRepositoryService_update(cml);

            folderRootRef = result[0].getDestination();

            // Applichiamo le stesse ACL dello spazio delle Folder
            setACESRunasAdmin(folderRootRef, ace, false);

            folderRootId = String.valueOf(getNodeDbId(folderRootRef));

            upd = new HashMap<String, String>();
            upd.put(it.kdm.docer.sdk.Constants.folder_folder_id, folderRootId);
            upd.put(it.kdm.docer.sdk.Constants.folder_cod_ente, codiceEnte);
            upd.put(it.kdm.docer.sdk.Constants.folder_cod_aoo, codiceAOO);
            upd.put(it.kdm.docer.sdk.Constants.folder_des_folder, "Folder root delle Unita Documentarie");
            upd.put(it.kdm.docer.sdk.Constants.folder_folder_name, DOCUMENTI_REPO);
            upd.put(it.kdm.docer.sdk.Constants.folder_parent_folder_id, "");

            // finalizeFolderProfile(folderRootUUID, folderProfile);
            updateAlfrescoFolder(true, folderRootRef, upd);

        } catch (DocerException e) {
            if (aooRef != null) {
                deleteNodes(new String[]{aooRef.getUuid()});
            }
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    public void createTitolario(ITitolarioInfo titolarioInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");
        String metodo = "createTitolario";
        Reference titolarioRef = null;
        try {
            String codiceTitolario = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_cod_titolario, titolarioInfo.getCodiceTitolario());
            String codiceAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_cod_aoo, titolarioInfo.getCodiceAOO());
            String codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_cod_ente, titolarioInfo.getCodiceEnte());
            String classifica = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_classifica, titolarioInfo.getClassifica());
            String parentClassifica = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_parent_classifica, titolarioInfo.getParentClassifica());
            String descrizioneTitolario = titolarioInfo.getDescrizione();
            EnumBoolean enabled = titolarioInfo.getEnabled();
            if (codiceEnte == null)
                codiceEnte = "";
            if (codiceAOO == null)
                codiceAOO = "";
            if (classifica == null)
                classifica = "";
            if (parentClassifica == null)
                parentClassifica = "";
            if (codiceTitolario == null)
                codiceTitolario = "";

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put("COD_ENTE", Arrays.asList(codiceEnte));
            searchCriteria.put("COD_AOO", Arrays.asList(codiceAOO));
            searchCriteria.put("CLASSIFICA", Arrays.asList(classifica));
            int count = countAnagraficaAsAdmin("TITOLARIO", searchCriteria);

            if (count > 0)
                throw new DocerException("verifica univocita' anagrafica sul documentale: esiste gia' una voce di Titolario con COD_ENTE " + codiceEnte + ", COD_AOO " + codiceAOO + ", CLASSIFICA "
                        + classifica);

            searchCriteria.clear();
            searchCriteria.put("COD_ENTE", Arrays.asList(codiceEnte));
            searchCriteria.put("COD_AOO", Arrays.asList(codiceAOO));
            searchCriteria.put("COD_TITOLARIO", Arrays.asList(codiceTitolario));
            count = countAnagraficaAsAdmin("TITOLARIO", searchCriteria);

            if (count > 0)
                throw new DocerException("verifica univocita' anagrafica sul documentale: esiste gia' una voce di Titolario con COD_ENTE " + codiceEnte + ", COD_AOO " + codiceAOO + ", COD_TITOLARIO "
                        + codiceTitolario);

            Map<String, String> props = new HashMap<String, String>();

            props.put(it.kdm.docer.sdk.Constants.titolario_classifica, classifica);
            props.put(it.kdm.docer.sdk.Constants.titolario_cod_ente, codiceEnte);
            props.put(it.kdm.docer.sdk.Constants.titolario_cod_aoo, codiceAOO);
            props.put(it.kdm.docer.sdk.Constants.titolario_cod_titolario, codiceTitolario);
            props.put(it.kdm.docer.sdk.Constants.titolario_des_titolario, descrizioneTitolario);
            if (!enabled.equals(EnumBoolean.UNSPECIFIED))
                props.put(it.kdm.docer.sdk.Constants.titolario_enabled, enabled.toString().toLowerCase());
            props.put(it.kdm.docer.sdk.Constants.titolario_parent_classifica, parentClassifica);

            for (String key : titolarioInfo.getExtraInfo().keySet()) {
                props.put(key.toUpperCase(), titolarioInfo.getExtraInfo().get(key));
            }

            Reference parentReference = null;

            SearchResultSet result = _getAoo(codiceAOO, codiceEnte);
            if (result.getCount() == 0)
                throw new DocerException(codiceAOO + ": AOO non trovata");

            if (result.getCount() != 1)
                throw new DocerException(codiceAOO + ": trovate " + result.getCount() + " AOO");

            Reference aooNodeRef = result.getFirstAndOnly().getReference();
            EnumACLRights aooEffectiverights = result.getFirstAndOnly().getEffectiveRights();
            parentReference = aooNodeRef;

            if (parentClassifica != null && !parentClassifica.equals("")) {
                // String parentTitUUID = getTitolarioUUID(parentClassifica,
                // null, codiceAOO, codiceEnte);
                SearchResultSet parenttitolario = getTitolario(parentClassifica, null, codiceAOO, codiceEnte);

                Reference parentTitRef = null;
                if (parenttitolario.getCount() > 0) {
                    parentTitRef = parenttitolario.getFirstAndOnly().getReference();
                }

                // se dobbiamo agganciare il titolario sotto un'altra voce
                // titolario
                // cambiamo reference del nodo -> verso il codiceTitolarioPadre
                if (parentTitRef == null)
                    throw new DocerException("titolario padre " + parentClassifica + " non trovato");

                EnumACLRights effectiverights = parenttitolario.getFirstAndOnly().getEffectiveRights();

                if (!effectiverights.equals(EnumACLRights.normalAccess) && !effectiverights.equals(EnumACLRights.fullAccess))
                    throw new DocerException("l'utente corrente non possiede diritti sufficienti sulla voce di Titolario Padre " + parentClassifica);

                // il padre cambia... dalla AOO diventa un titolario padre
                parentReference = parentTitRef;
            } else {

                // se devo creare un titolario di primo livello devo avere
                // diritti sulla AOO
                if (!aooEffectiverights.equals(EnumACLRights.normalAccess) && !aooEffectiverights.equals(EnumACLRights.fullAccess))
                    throw new DocerException("l'utente corrente non possiede diritti sufficienti sulla AOO per eseguire l'operazione");
            }

            String folderId = classifica;
            if (folderId.equals(""))
                folderId = codiceTitolario;

            boolean inheritsAcl = parseInheritsACL(titolarioInfo.getExtraInfo().get(it.kdm.docer.sdk.Constants.inherits_acl));
            if (CIFS) {
                props.put(it.kdm.docer.sdk.Constants.inherits_acl, Boolean.toString(inheritsAcl));
            }

            titolarioRef = createFolderToParentUUID(DocerModel.TYPE_TITOLARIO, descrizioneTitolario, parentReference.getUuid(), DocerModel.ICON_PREFIX + DocerModel.TITOLARIO_LOCAL_ID, props);

            String aooGroupId = generateAOOGroupId(codiceAOO, codiceEnte);
            String adminsEnteGroupId = generateEnteAdminsGroupId(codiceEnte);
            String adminsAOOGroupId = generateAOOAdminsGroupId(codiceAOO, codiceEnte);

            // impostazione delle ACL
            // assegnamo la ACE
            ACE[] ace = new ACE[4];
            // readOnly al gruppo AOO
            ace[0] = new ACE();
            ace[0].setAuthority(Constants.GROUP_PREFIX + aooGroupId);
            ace[0].setPermission(convertToAlfrescoRights(EnumACLRights.readOnly));
            ace[0].setAccessStatus(AccessStatus.acepted);

            // full access agli amministratori di Ente
            ace[1] = new ACE();
            ace[1].setAuthority(Constants.GROUP_PREFIX + adminsEnteGroupId);
            ace[1].setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            ace[1].setAccessStatus(AccessStatus.acepted);

            // fullAccess agli amministratori di AOO
            ace[2] = new ACE();
            ace[2].setAuthority(Constants.GROUP_PREFIX + adminsAOOGroupId);
            ace[2].setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            ace[2].setAccessStatus(AccessStatus.acepted);

            // fullAccess agli amministratori di system
            ace[3] = new ACE();
            ace[3].setAuthority(Constants.GROUP_PREFIX + SYS_ADMINS_GROUPID);
            ace[3].setPermission(convertToAlfrescoRights(EnumACLRights.fullAccess));
            ace[3].setAccessStatus(AccessStatus.acepted);

            Predicate titolarioPredicate = createPredicate(titolarioRef);

            // assegno ACL:
            // no ereditarieta'
            // readOnly al gruppo AOO
            // fullAccess agli Admins dell'Ente
            // fullACcess agli Admins della AOO

            setACESRunasAdmin(titolarioRef, ace, inheritsAcl);

        } catch (DocerException e) {
            if (titolarioRef != null)
                deleteNodes(new String[]{titolarioRef.getUuid()});
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    public void createFascicolo(IFascicoloInfo fascicoloInfo) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "createFascicolo";
        Reference fascicoloRef = null;
        try {
            String progressivo = fascicoloInfo.getProgressivo();
            String numeroFascicolo = fascicoloInfo.getNumeroFascicolo();
            String annoFascicolo = String.valueOf(fascicoloInfo.getAnnoFascicolo());
            String classifica = fascicoloInfo.getClassifica();
            String codiceAOO = fascicoloInfo.getCodiceAOO();
            String codiceEnte = fascicoloInfo.getCodiceEnte();
            String descrizioneFascicolo = fascicoloInfo.getDescrizione();
            EnumBoolean enabled = fascicoloInfo.getEnabled();
            String parentProgressivo = fascicoloInfo.getParentProgressivo();

            boolean hasTilde = parseHasTilde(fascicoloInfo.getExtraInfo().remove(HAS_TILDE));

            if (progressivo == null)
                progressivo = "";
            if (numeroFascicolo == null)
                numeroFascicolo = "";
            if (classifica == null)
                classifica = "";
            if (codiceAOO == null)
                codiceAOO = "";
            if (codiceEnte == null)
                codiceEnte = "";
            if (descrizioneFascicolo == null)
                descrizioneFascicolo = "";
            if (parentProgressivo == null)
                parentProgressivo = "";

            progressivo = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_progr_fascicolo, progressivo);
            numeroFascicolo = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_num_fascicolo, numeroFascicolo);
            classifica = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_classifica, classifica);
            codiceAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_cod_aoo, codiceAOO);
            codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_cod_ente, codiceEnte);
            parentProgressivo = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_parent_progr_fascicolo, parentProgressivo);

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put("COD_ENTE", Arrays.asList(codiceEnte));
            searchCriteria.put("COD_AOO", Arrays.asList(codiceAOO));
            searchCriteria.put("CLASSIFICA", Arrays.asList(classifica));
            searchCriteria.put("ANNO_FASCICOLO", Arrays.asList(annoFascicolo));
            searchCriteria.put("PROGR_FASCICOLO", Arrays.asList(progressivo));

            int count = countAnagraficaAsAdmin("FASCICOLO", searchCriteria);

            if (count > 0)
                throw new DocerException("verifica univocita' anagrafica sul documentale: esiste gia' un FASCICOLO con COD_ENTE " + codiceEnte + ", COD_AOO " + codiceAOO + ", CLASSIFICA "
                        + classifica + ", ANNO_FASCICOLO " + annoFascicolo + ", PROGR_FASCIOLO " + progressivo);

            searchCriteria.clear();
            searchCriteria.put("COD_ENTE", Arrays.asList(codiceEnte));
            searchCriteria.put("COD_AOO", Arrays.asList(codiceAOO));
            searchCriteria.put("CLASSIFICA", Arrays.asList(classifica));
            searchCriteria.put("ANNO_FASCICOLO", Arrays.asList(annoFascicolo));
            searchCriteria.put("NUM_FASCICOLO", Arrays.asList(numeroFascicolo));

            count = countAnagraficaAsAdmin("FASCICOLO", searchCriteria);

            if (count > 0)
                throw new DocerException("verifica univocita' anagrafica sul documentale: esiste gia' un FASCICOLO con COD_ENTE " + codiceEnte + ", COD_AOO " + codiceAOO + ", CLASSIFICA "
                        + classifica + ", ANNO_FASCICOLO " + annoFascicolo + ", NUM_FASCIOLO " + numeroFascicolo);

            String codice = progressivo;
            if (codice.equals(""))
                codice = numeroFascicolo;

            Map<String, String> props = new HashMap<String, String>();

            props.put(it.kdm.docer.sdk.Constants.fascicolo_progr_fascicolo, progressivo);
            props.put(it.kdm.docer.sdk.Constants.fascicolo_cod_ente, codiceEnte);
            props.put(it.kdm.docer.sdk.Constants.fascicolo_cod_aoo, codiceAOO);
            props.put(it.kdm.docer.sdk.Constants.fascicolo_classifica, classifica);
            props.put(it.kdm.docer.sdk.Constants.fascicolo_anno_fascicolo, annoFascicolo);
            props.put(it.kdm.docer.sdk.Constants.fascicolo_num_fascicolo, numeroFascicolo);
            props.put(it.kdm.docer.sdk.Constants.fascicolo_des_fascicolo, descrizioneFascicolo);
            props.put(it.kdm.docer.sdk.Constants.fascicolo_parent_progr_fascicolo, parentProgressivo);
            if (!enabled.equals(EnumBoolean.UNSPECIFIED))
                props.put(it.kdm.docer.sdk.Constants.fascicolo_enabled, enabled.toString().toLowerCase());

            for (String key : fascicoloInfo.getExtraInfo().keySet()) {
                props.put(key.toUpperCase(), fascicoloInfo.getExtraInfo().get(key));
            }

            SearchResultSet titolarioSearchResultSet = getTitolario(classifica, null, codiceAOO, codiceEnte);

            if (titolarioSearchResultSet.getCount() == 0)
                throw new DocerException("voce di Titolario con classifica " + classifica + " non trovata");

            if (titolarioSearchResultSet.getCount() > 1)
                throw new DocerException("trovate " + titolarioSearchResultSet.getCount() + " voci di Titolario con classifica " + classifica);

            // devo avere diritti di creazione nella voce di titolario per
            // creare l'anno fascicolo
            EnumACLRights titolarioEffectiverights = titolarioSearchResultSet.getFirstAndOnly().getEffectiveRights();

            Reference parentNodeReference = getAnnoFascicoloReference(annoFascicolo, titolarioSearchResultSet.getFirstAndOnly().getReference());
            if (parentNodeReference == null) {
                // non esiste l'anno fascicolo...lo creo

                if (!titolarioEffectiverights.equals(EnumACLRights.normalAccess) && !titolarioEffectiverights.equals(EnumACLRights.fullAccess))
                    throw new DocerException("l'utente corrente non possiede diritti sufficienti nella voce di Titolario per eseguire l'operazione");

                parentNodeReference = createAnnoFascicolo(annoFascicolo, titolarioSearchResultSet.getFirstAndOnly().getReference());
                if (parentNodeReference == null)
                    throw new DocerException("errore creazione anno fascicolo");
            }

            if (parentProgressivo != null && !parentProgressivo.equals("")) {

                SearchResultSet fascicoloSearchResultSet = getFascicolo(parentProgressivo, null, annoFascicolo, classifica, codiceAOO, codiceEnte);

                if (fascicoloSearchResultSet.getCount() == 0) {
                    throw new DocerException("fascicolo parent con progressivo" + parentProgressivo + " non trovato");
                }

                EnumACLRights parentFascicoloEffectiverights = fascicoloSearchResultSet.getFirstAndOnly().getEffectiveRights();

                // se devo creare nel fascicolo devo avere diritti sufficienti
                if (!parentFascicoloEffectiverights.equals(EnumACLRights.normalAccess) && !parentFascicoloEffectiverights.equals(EnumACLRights.fullAccess))
                    throw new DocerException("l'utente corrente non possiede diritti sufficienti sul Fascicolo Padre per eseguire l'operazione");

                parentNodeReference = fascicoloSearchResultSet.getFirstAndOnly().getReference();

            }

            boolean inheritsAcl = parseInheritsACL(fascicoloInfo.getExtraInfo().get(it.kdm.docer.sdk.Constants.inherits_acl));
            if (CIFS) {
                props.put(it.kdm.docer.sdk.Constants.inherits_acl, Boolean.toString(inheritsAcl));
            }

            String nodeName = descrizioneFascicolo;
            if (hasTilde) {
                String id = Joiner.on('-').join(progressivo, classifica, annoFascicolo);
                String affix = String.format("~%s", getFilenameHash(id));
                nodeName = addAffix(nodeName, affix);
            }

            fascicoloRef = createFolderToParentUUID(DocerModel.TYPE_FASCICOLO, nodeName, parentNodeReference.getUuid(), DocerModel.ICON_PREFIX + DocerModel.FASCICOLO_LOCAL_ID, props);

            setInheritsRunasAdmin(fascicoloRef, inheritsAcl);

        } catch (DocerException e) {
            if (fascicoloRef != null)
                deleteNodes(new String[]{fascicoloRef.getUuid()});
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    public String createDocument(String documentType, Map<String, String> profileInfo, InputStream documentFile) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "createDocument";

        Reference documentRef = null;
        try {
            // Necessario quando si chiama il provider da BL Docarea 15
            Map<String, String> metadata = removeExclusiveMetadata(profileInfo);

            // se e' in modalita' migrazione il docnumber arriva dalle
            // proprieta'
            // del profilo
            String docnumber = metadata.get(it.kdm.docer.sdk.Constants.doc_docnum);

            if (MIGRATION_DOCNUM_OFFSET < 0) {
                // sono in modalita' migrazione
                if (StringUtils.isEmpty(docnumber)) {
                    throw new DocerException("Provider: Modalita' migrazione attivata (MIGRATION_DOCNUM_OFFSET < 0): DOCNUM obbligatorio");
                }

                int count = countDocnumAsAdmin(docnumber);
                if (count > 0) {
                    throw new DocerException("Provider: Modalita' migrazione attivata (MIGRATION_DOCNUM_OFFSET < 0): documento con docId " + docnumber + " esistente.");
                }
            } else {
                if (StringUtils.isNotEmpty(docnumber)) {
                    throw new DocerException("Provider: Modalita' migrazione disattivata (MIGRATION_DOCNUM_OFFSET >= 0): DOCNUM non deve essere specificato dall'esterno");
                }
            }

            // il formato della CREATION DATE e' gestito a livello di Business
            // Logic
            // comunque lo controllo
            DateTimeFormatter fm = ISODateTimeFormat.dateTime();

            DateTime creationDate = new DateTime();

            // data creazione (diversa dalla CreationDate di Alfresco che viene
            // assegnata da Alfresco e non e' modificabile
            if (StringUtils.isNotEmpty(metadata.get(it.kdm.docer.sdk.Constants.doc_creation_date))) {
                try {
                    // creationDate =
                    // fm.parseDateTime(profileInfo.get(it.kdm.docer.sdk.Constants.doc_creation_date));
                    creationDate = parseDateTime(metadata.get(it.kdm.docer.sdk.Constants.doc_creation_date));
                } catch (Exception e) {
                    throw new DocerException("CREATION_DATE: " + e.getMessage());
                }
            }

            metadata.put(it.kdm.docer.sdk.Constants.doc_creation_date, fm.print(creationDate));

            // docname
            String docname = metadata.get(it.kdm.docer.sdk.Constants.doc_docname);
            if (docname == null || docname.equals(""))
                throw new DocerException("DOCNAME obbligatorio");

            if (metadata.get(it.kdm.docer.sdk.Constants.doc_cod_ente) == null || metadata.get(it.kdm.docer.sdk.Constants.doc_cod_ente).equals(""))
                throw new DocerException("COD_ENTE obbligatorio");

            // codice Ente e' obbligatorio
            String codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.doc_cod_ente, metadata.get(it.kdm.docer.sdk.Constants.doc_cod_ente));

            // codice AOO non e' obbligatorio
            String codiceAOO = metadata.get(it.kdm.docer.sdk.Constants.doc_cod_aoo);
            if (codiceAOO != null) {
                codiceAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.doc_cod_aoo, codiceAOO);
            }

            String progressivo = metadata.get(it.kdm.docer.sdk.Constants.doc_progr_fascicolo);
            if (progressivo != null) {
                progressivo = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.doc_progr_fascicolo, progressivo);
            }

            String numeroFascicolo = metadata.get(it.kdm.docer.sdk.Constants.doc_num_fascicolo);
            if (numeroFascicolo != null) {
                numeroFascicolo = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.doc_num_fascicolo, numeroFascicolo);
            }

            String classifica = metadata.get(it.kdm.docer.sdk.Constants.doc_classifica);
            if (classifica != null) {
                classifica = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.doc_classifica, classifica);
            }

            // String annoFascicolo =
            // profileInfo.get(it.kdm.docer.sdk.Constants.doc_anno_fascicolo);

            String typeId = "documento";

            boolean hasTilde = parseHasTilde(metadata.remove(HAS_TILDE));
            Reference repositoryFolderRef = getRepositoryFolderReference(codiceEnte, codiceAOO, creationDate);

            String ext = getExtension(metadata.get(it.kdm.docer.sdk.Constants.doc_docname));

            ParentReference parentDoc = createParentReference(null, repositoryFolderRef.getUuid(), Constants.ASSOC_CONTAINS, Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, docname));

            String doc_qname = UUID.randomUUID().toString().replaceAll("\\-", "");
            if (ext != null && !ext.equals("")) {
                doc_qname = doc_qname + "." + ext;
            }

            CMLCreate createTempDoc = new CMLCreate();
            // createTempDoc.associationType = Constants.ASSOC_CONTAINS;
            createTempDoc.setType(Constants.createQNameString(DocerModel.DOCAREA_NAMESPACE_CONTENT_MODEL, typeId));
            createTempDoc.setParent(parentDoc);
            createTempDoc.setChildName(doc_qname);

            // assegno le proprieta'
            List<NamedValue> props = new ArrayList<NamedValue>();

            NamedValue nameProperty = new NamedValue();
            nameProperty.setName(Constants.PROP_NAME);
            nameProperty.setValue(doc_qname);
            props.add(nameProperty);

            createTempDoc.setProperty(props.toArray(new NamedValue[0]));

            // creazione del profilo temporaneo
            CML cml = new CML();
            cml.setCreate(new CMLCreate[]{createTempDoc});

            // creazione documento temporaneo
            UpdateResult[] resultDoc = getAlfrescoRepositoryService_update(cml);

            // docid
            documentRef = resultDoc[0].getDestination();

            // se non e' in modalita' migrazione assegno il docnum (sommo il
            // trim per non farlo coincidere con uno dei docnum migrati)
            if (docnumber == null || docnumber.equals("")) {

                long nodeDBID = getNodeDbId(documentRef);

                docnumber = String.valueOf(nodeDBID + MIGRATION_DOCNUM_OFFSET);

                int count = countDocnumAsAdmin(docnumber);
                if (count > 0) {
                    throw new DocerException(docnumber + " <- (" + nodeDBID + " + MIGRATION_DOCNUM_OFFSET): documento con docId " + docnumber + " esistente");
                }

            }

            // modifiche del 14/12/2012 per documenti di tipo URL
            if (documentFile != null) {
                uploadFile(documentRef, doc_qname, documentFile);
            }

            String newChildName = docname;
            if (hasTilde) {
                String affix = String.format("~%s", docnumber);
                newChildName = addAffix(docname, affix);
            }

            // riassegno le proprieta'
            props.clear();

            // il nuovo nome del nodo
            props.add(new NamedValue(Constants.PROP_NAME, false, newChildName, null));

            // docnumber
            metadata.put(it.kdm.docer.sdk.Constants.doc_docnum, docnumber);
            // metadata.put(it.kdm.docer.sdk.Constants.doc_docname, docname);

            boolean inheritsAcl = parseInheritsACL(metadata.remove(it.kdm.docer.sdk.Constants.inherits_acl));
            if (CIFS) {
                metadata.put(it.kdm.docer.sdk.Constants.inherits_acl, Boolean.toString(inheritsAcl));
            }

            populateAlfrescoProperties(metadata, props);

            // aggiornamento del profilo
            Predicate docPredicate = createPredicate(documentRef);

            if (!hasTilde) {
                repositoryFolderRef = getRepositoryFolderReference(codiceEnte, codiceAOO, docnumber);
                parentDoc = createParentReference(null, repositoryFolderRef.getUuid(), Constants.ASSOC_CONTAINS, Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, newChildName));
            }

            // CML per aggiornamento profilo

            CMLUpdate cmlUpdateDoc = null;
            if (props.size() > 0) {
                cmlUpdateDoc = new CMLUpdate();

                // FIX del bug del CMLUpdate con move e update di 22 props: da
                // capire...
                if (props.size() == 22) {
                    props.add(new NamedValue("{" + DocerModel.NAMESPACE_CONTENT_MODEL + "}title", false, null, null));
                }

                cmlUpdateDoc.setProperty(props.toArray(new NamedValue[0])); // le
                // proprieta'
                cmlUpdateDoc.setWhere(docPredicate); // il documento

            }

            cml = new CML();
            if (cmlUpdateDoc != null) {
                cml.setUpdate(new CMLUpdate[]{cmlUpdateDoc});
            }

            // aggiorno il profilo
            resultDoc = getAlfrescoRepositoryService_update(cml);

            // sposto per rinominare
            moveToSpaceRunasAdmin(documentRef.getUuid(), repositoryFolderRef.getUuid(), inheritsAcl, newChildName);

            // TODO: SCOMMENTARE quando attiveremo ereditarieta' multipla
            // propagateACL(docnumber);

            return docnumber;

        } catch (DocerException e) {
            if (documentRef != null)
                tryToDeleteIncompleteDocument(documentRef);
            throw new DocerException(metodo + ": " + e.getMessage());
        } catch (Exception e) {
            if (documentRef != null)
                tryToDeleteIncompleteDocument(documentRef);
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    private String makeFolder(String folderName, String codiceEnte, String codiceAOO, Reference folderReference, String parentId) throws DocerException {
        String repositoryFolderId = String.valueOf(getNodeDbId(folderReference));

        HashMap<String, String> upd = new HashMap<String, String>();
        upd.put(it.kdm.docer.sdk.Constants.folder_folder_id, repositoryFolderId);
        upd.put(it.kdm.docer.sdk.Constants.folder_cod_ente, codiceEnte);
        upd.put(it.kdm.docer.sdk.Constants.folder_cod_aoo, codiceAOO);
        upd.put(it.kdm.docer.sdk.Constants.folder_des_folder, "Unita Documentaria " + folderName);
        upd.put(it.kdm.docer.sdk.Constants.folder_folder_name, folderName);
        upd.put(it.kdm.docer.sdk.Constants.folder_parent_folder_id, parentId);

        // finalizeFolderProfile(folderRootUUID, folderProfile);
        updateAlfrescoFolder(true, folderReference, upd);

        return repositoryFolderId;
    }

    private String addAffix(String name, String affix) {

        name = name.trim();
        // tolgo i caratteri non ammessi per i nomi (cm:name) in alfresco
        name = name.replaceAll("[\"*\\\\><?/:|]", "");
        String newChildName;
        int idx = name.lastIndexOf('.');
        if (idx > -1) {
            newChildName = String.format("%s%s%s", name.substring(0, idx), affix, name.substring(idx));
        } else {
            newChildName = String.format("%s%s", name, affix);
        }
        return newChildName;
    }

    private String removeAffix(String name, String affix) {
        return name.replace(affix, "");
    }

    public IUserInfo getUser(String userId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "getUser";
        String rif = userId;
        try {
            return _getUser(userId);
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    // // mock per SOLR
    // public IUserInfo getUser(String userId) throws DocerException {
    //
    // if (currentUser == null)
    // throw new DocerException(-1113, "current user is null");
    //
    // IUserProfileInfo userProfileInfo = new UserProfileInfo();
    // userProfileInfo.setEnabled(EnumBoolean.TRUE);
    // userProfileInfo.setFirstName(userId);
    // userProfileInfo.setLastName(userId);
    // userProfileInfo.setFullName(userId);
    // userProfileInfo.setUserId(userId);
    // userProfileInfo.getExtraInfo().put("COD_ENTE", "EMR");
    // userProfileInfo.getExtraInfo().put("COD_AOO", "AOO_EMR");
    //
    // IUserInfo userInfo = new UserInfo();
    //
    // userInfo.setProfileInfo(userProfileInfo);
    //
    // userInfo.getGroups().add("EMR");
    // userInfo.getGroups().add("AOO_EMR");
    // userInfo.getGroups().add("AOO_AL");
    //
    // if(userId.equalsIgnoreCase("admin")){
    // userInfo.getGroups().add("ALFRESCO_ADMINISTRATORS");
    // }
    // else if(userId.equalsIgnoreCase("sysadmin")){
    // userInfo.getGroups().add("SYS_ADMINS");
    // }
    //
    // return userInfo;
    // }

    public void updateProfileDocument(String docId, Map<String, String> metaDati) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "updateProfileDocument";
        String rif = docId;

        try {
            // Necessario quando si chiama il provider da BL Docarea 15
            Map<String, String> newMetadata = removeExclusiveMetadata(metaDati);

            SearchResultSet result = getUniqueDocument(docId, false, false, false, true, true, true);

            SearchResult srDoc = result.getFirstAndOnly();

            // predicati di riferimento
            Predicate predicateDoc = createPredicate(srDoc.getReference());

            String oldName = srDoc.getProfile().get(it.kdm.docer.sdk.Constants.doc_docname);

            LockStatus lockStatus = srDoc.getLockStatus();
            if (lockStatus.getLocked() && !lockStatus.getUserId().equals(currentUser.getUserId())) {
                throw new DocerException("il documento " + docId + " e' bloccato esclusivamente da un altro utente");
            }

            EnumACLRights docEffectiveRights = srDoc.getEffectiveRights();
            if (!docEffectiveRights.equals(EnumACLRights.normalAccess) && !docEffectiveRights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti sul documento " + docId + " per eseguire l'operazione");
            }

            // assegno le proprieta'
            List<NamedValue> props = new ArrayList<NamedValue>();

            if (newMetadata.containsKey(it.kdm.docer.sdk.Constants.doc_progr_fascicolo) || newMetadata.containsKey(it.kdm.docer.sdk.Constants.doc_fascicoli_secondari)) {
                // se e' fascicolazione o sfascicolazione parent_folder_id
                // dovrebbe essere annullato dalla Business Logic

                if (newMetadata.get(it.kdm.docer.sdk.Constants.doc_progr_fascicolo).equals("")) {
                    newMetadata.put(it.kdm.docer.sdk.Constants.doc_fascicoli_secondari, "");
                }
            }

            String newName = newMetadata.get(it.kdm.docer.sdk.Constants.doc_docname);
            if (newName == null) {
                newName = oldName;
            }

            String hasTildeMetaData = metaDati.remove(HAS_TILDE);

            String affix = String.format("~%s", docId);

            boolean hasTilde;
            if (!Strings.isNullOrEmpty(hasTildeMetaData)) {
                hasTilde = parseHasTilde(hasTildeMetaData);
            } else {
                hasTilde = result.getFirstAndOnly().getCmName().contains(affix);
            }

            if (hasTilde && !newName.contains(affix)) {
                newName = addAffix(newName, affix);
            } else if (!hasTilde && newName.contains(affix)) {
                newName = removeAffix(newName, affix);
            }

            boolean inheritsACL = false;

            if (CIFS && newMetadata.containsKey(it.kdm.docer.sdk.Constants.inherits_acl)) {
                inheritsACL = parseInheritsACL(newMetadata.remove(it.kdm.docer.sdk.Constants.inherits_acl));
                newMetadata.put(it.kdm.docer.sdk.Constants.inherits_acl, Boolean.toString(inheritsACL));
            } else {
                String inherits = result.getFirstAndOnly().getProfile().get(it.kdm.docer.sdk.Constants.inherits_acl);
                if (StringUtils.isNotEmpty(inherits)) {
                    inheritsACL = Boolean.parseBoolean(inherits);
                } else {
                    inheritsACL = defaultInheritsACL;
                }
            }

            populateAlfrescoProperties(newMetadata, props);

            // CML per aggiornamento del profilo
            CMLUpdate cmlUpdateDoc = null;
            if (props.size() > 0) {
                cmlUpdateDoc = new CMLUpdate();
                cmlUpdateDoc.setProperty(props.toArray(new NamedValue[0])); // props
                cmlUpdateDoc.setWhere(predicateDoc); // il documento

                CML cml = new CML();
                cml.setUpdate(new CMLUpdate[]{cmlUpdateDoc});

                // aggiorno
                getAlfrescoRepositoryService_update(cml);

            }

            // muovo fisicamente se presente fascicolo o e' sfascicolazione
            move(srDoc, newName, newMetadata, inheritsACL);

            // TODO: SCOMMENTARE quando attiveremo ereditarieta' multipla
            // propagateACL(srDoc.getDocId());

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    private Map<String, EnumACLRights> getACLFascicoliOfDocument(DataRow<String> documentProfile) throws DocerException {

        Map<String, EnumACLRights> aclAll = new HashMap<String, EnumACLRights>();

        String xcodente = documentProfile.get(it.kdm.docer.sdk.Constants.doc_cod_ente);
        String xcodaoo = documentProfile.get(it.kdm.docer.sdk.Constants.doc_cod_aoo);
        String xclassifica = documentProfile.get(it.kdm.docer.sdk.Constants.doc_classifica);
        String xanno = documentProfile.get(it.kdm.docer.sdk.Constants.doc_anno_fascicolo);
        String xprogr = documentProfile.get(it.kdm.docer.sdk.Constants.doc_progr_fascicolo);
        String xfsec = documentProfile.get(it.kdm.docer.sdk.Constants.doc_fascicoli_secondari);

        if (xclassifica == null) {
            throw new DocerException("getACLFromFascicoliOfDocument: la colonna CLASSIFICA non e' presente nel profilo dell'oggetto recuperato");
        }
        if (xanno == null) {
            throw new DocerException("getACLFromFascicoliOfDocument: la colonna ANNO_FASCICOLO non e' presente nel profilo dell'oggetto recuperato");
        }
        if (xprogr == null) {
            throw new DocerException("getACLFromFascicoliOfDocument: la colonna PROGR_FASCICOLO non e' presente nel profilo dell'oggetto recuperato");
        }
        if (xfsec == null) {
            throw new DocerException("getACLFromFascicoliOfDocument: la colonna FASC_SECONDARI non e' presente nel profilo dell'oggetto recuperato");
        }

        if (StringUtils.isNotEmpty(xprogr)) {
            xfsec = xfsec.replaceAll(" *; *$", "");
            xfsec += ";" + xclassifica + "/" + xanno + "/" + xprogr;

            String[] tuttifascicoli = xfsec.split(" *; *");
            for (String xf : tuttifascicoli) {

                if (xf.equals("")) {
                    continue;
                }

                // faccio split senza togliere spazi bianchi eventuali
                String[] fascicoloSplit = xf.split("/");
                if (fascicoloSplit.length < 3) {
                    throw new DocerException("il Fascicolo " + xf + " non rispetta il formato dei fascicoli secondari classifica/anno/progressivo");
                }

                String cl = fascicoloSplit[0];
                String an = fascicoloSplit[1];

                // faccio substring considerando i "/"
                String pr = xf.substring(cl.length() + 1).substring(an.length() + 1);

                Map<String, EnumACLRights> xaclf = getACLFascicoloAsAdmin(pr.trim(), an.trim(), cl.trim(), xcodaoo, xcodente);

                aclAll = mergeACL2(aclAll, xaclf);

            }
        }

        return aclAll;
    }

    // private Map<String, EnumACLRights> getFolderACLAsAdmin(String folderId)
    // throws DocerException {
    //
    // SearchResultSet folderSearchResult =
    // getFolderFullProfileAsAdmin(folderId);
    //
    // if (folderSearchResult.getCount() == 0) {
    // throw new DocerException("Folder con FOLDER_ID " + folderId +
    // " non trovata");
    // }
    //
    // if (folderSearchResult.getCount() > 1) {
    // throw new DocerException("trovate " + folderSearchResult.getCount() +
    // " folder con FOLDER_ID " + folderId);
    // }
    //
    // return folderSearchResult.getFirstAndOnly().getAcl();
    //
    // }

    private void move(SearchResult srDoc, String newName, Map<String, String> newMetadata, Boolean inherits) throws DocerException {

        String oldName = srDoc.getCmName();

        DataRow<String> oldProfile = srDoc.getProfile();

        String oldCodiceEnte = oldProfile.get(it.kdm.docer.sdk.Constants.doc_cod_ente);
        if (oldCodiceEnte == null) {
            oldCodiceEnte = "";
        }
        String oldCodiceAOO = oldProfile.get(it.kdm.docer.sdk.Constants.doc_cod_aoo);
        if (oldCodiceAOO == null) {
            oldCodiceAOO = "";
        }
        String oldClassifica = oldProfile.get(it.kdm.docer.sdk.Constants.doc_classifica);
        if (oldClassifica == null) {
            oldClassifica = "";
        }
        String oldAnnoFascicolo = oldProfile.get(it.kdm.docer.sdk.Constants.doc_anno_fascicolo);
        if (oldAnnoFascicolo == null) {
            oldAnnoFascicolo = "";
        }
        String oldProgressivo = oldProfile.get(it.kdm.docer.sdk.Constants.doc_progr_fascicolo);
        if (oldProgressivo == null) {
            oldProgressivo = "";
        }
        String oldFascSecondari = oldProfile.get(it.kdm.docer.sdk.Constants.doc_fascicoli_secondari);
        if (oldFascSecondari == null) {
            oldFascSecondari = "";
        }

        String codiceEnte = newMetadata.get(it.kdm.docer.sdk.Constants.doc_cod_ente);
        if (codiceEnte == null) {
            codiceEnte = "";
        }
        String codiceAOO = newMetadata.get(it.kdm.docer.sdk.Constants.doc_cod_aoo);
        if (codiceAOO == null) {
            codiceAOO = "";
        }
        String classifica = newMetadata.get(it.kdm.docer.sdk.Constants.doc_classifica);
        if (classifica == null) {
            classifica = "";
        }
        String annoFascicolo = newMetadata.get(it.kdm.docer.sdk.Constants.doc_anno_fascicolo);
        if (annoFascicolo == null) {
            annoFascicolo = "";
        }
        String progressivo = newMetadata.get(it.kdm.docer.sdk.Constants.doc_progr_fascicolo);
        if (progressivo == null) {
            progressivo = "";
        }
        String fascSecondari = newMetadata.get(it.kdm.docer.sdk.Constants.doc_fascicoli_secondari);
        if (fascSecondari == null) {
            fascSecondari = "";
        }

        boolean fascicoloSpecificato = newMetadata.containsKey(it.kdm.docer.sdk.Constants.doc_progr_fascicolo);
        boolean fascicoliSecondariSpecificati = newMetadata.containsKey(it.kdm.docer.sdk.Constants.doc_fascicoli_secondari);

        // SE NON CAMBIA IL FASCICOLO O LA FOLDER NON DEVO FARE QUERY SUL PARENT
        // ALTRIMENTI VA IN ECCEZIONE SE NON HO VISIBILITA'
        // i documenti fascicolati non possono essere spostati in una folder

        String nodeName = oldName;
        if (!StringUtils.isEmpty(newName) && !oldName.equals(newName)) {
            nodeName = newName;
        }

        // parent attuale
        String repositoryFolderUUID = null;

        if (fascicoloSpecificato) {

            // sfascicolazione
            if (StringUtils.isEmpty(progressivo)) {

                // folder del giorno
                Reference ref = getRepositoryFolderReference(codiceEnte, codiceAOO, new DateTime());
                if (ref != null) {
                    repositoryFolderUUID = ref.getUuid();
                }
            } else {
                // qui cambia il fascicolo principale
                SearchResultSet webScriptResults = getFascicolo(ADMIN_USERID, progressivo, null, annoFascicolo, classifica, codiceAOO, codiceEnte);

                if (webScriptResults.getCount() > 0) {

                    if (webScriptResults.getCount() > 1) {
                        throw new DocerException("recupero repositoryFolderRef: trovati " + webScriptResults.getCount() + " fascicoli");
                    }

                    // folder fascicolo
                    Reference ref = webScriptResults.getFirstAndOnly().getReference();
                    if (ref != null) {
                        repositoryFolderUUID = ref.getUuid();
                    }
                }
            }

        }

        // se modalita' migrazione allora fascicoliamo con thread
        if (MIGRATION_DOCNUM_OFFSET < 0) {

            String docId = oldProfile.get(it.kdm.docer.sdk.Constants.doc_docnum);
            ThreadFascicolazione runnablethread = new ThreadFascicolazione(docId, nodeName, ALFRESCO_END_POINT_ADDRESS, ADMIN_USERID, currentUser.getTicket(), srDoc.getReference().getUuid(),
                    repositoryFolderUUID, inherits, DOCUMENTO_EREDITA_ACL_FASCICOLO, DOCUMENTO_EREDITA_ACL_FOLDER);

            Thread th = new Thread(runnablethread);
            wq.execute(th);
        } else {
            moveToSpaceRunasAdmin(srDoc.getReference().getUuid(), repositoryFolderUUID, inherits, nodeName);
        }

    }

    public DataTable<String> searchDocuments(Map<String, List<String>> searchCriteria, List<String> keywords, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderby)
            throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "searchDocuments";
        try {

            String lucenequery = buildQueryForPath("docarea:documento", searchCriteria);
            if (lucenequery.isEmpty()) {
                lucenequery = LuceneUtility.buildAlfrescoDocumentsSearchQueryString(searchCriteria, keywords);

            }

            Map<String, EnumSearchOrder> myOrderby = null;
            if (orderby == null) {
                myOrderby = LuceneUtility.extractOrderByFromSearchCriteria(searchCriteria);
            } else {
                myOrderby = new HashMap<String, EnumSearchOrder>();
                myOrderby.putAll(orderby);
            }

            // long start = new Date().getTime();

            SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, returnProperties, maxResults, null, false, false, false, false, false, false, myOrderby);

            // long end = new Date().getTime();

            // System.out.println("tempo search provider: " +(end-start)
            // +" msec");

            return webScriptResults.getResultsAsDatatable();
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    // public boolean userIsSystemManager(String userId) throws DocerException {
    //
    // if (currentUser == null)
    // throw new DocerException(-1113, "current user is null");
    // try {
    // IUserInfo userinfo = _getUser(userId);
    // if (userinfo == null) {
    // throw new DocerException("utente " + userId + " non trovato");
    // }
    // for (String candidateGroup : userinfo.getGroups()) {
    // if (candidateGroup.equals(ALFRESCO_ADMINISTRATORS))
    // return true;
    // if (candidateGroup.equals(SYS_ADMINS_GROUPID))
    // return true;
    // }
    //
    // return false;
    //
    // }
    // catch (DocerException e) {
    // throw new DocerException("userIsSystemManager" + ": " + userId + ":" +
    // e.getMessage());
    // }
    // }

    public void createUser(IUserProfileInfo userProfile) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        boolean created = false;

        String metodo = "createUser";

        try {

            if (userProfile.getUserId() == null)
                throw new DocerException("specificare USER_ID");

            if (userProfile.getUserId().toUpperCase().startsWith(Constants.GROUP_PREFIX))
                throw new DocerException("il prefisso " + Constants.GROUP_PREFIX + " presente in USER_ID e' riservato ai gruppi Alfresco");

            String enteGroupId = null;
            String aooGroupId = null;

            String codiceEnte = "";
            String codiceAOO = "";
            String codiceFiscale = "";
            if (userProfile.getExtraInfo() != null) {
                for (String key : userProfile.getExtraInfo().keySet()) {
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_ente)) {
                        codiceEnte = userProfile.getExtraInfo().get(key);
                        continue;
                    }
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_aoo)) {
                        codiceAOO = userProfile.getExtraInfo().get(key);
                        continue;
                    }
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_fiscale)) {
                        codiceFiscale = userProfile.getExtraInfo().get(key);
                        continue;
                    }
                }
            }

            if (codiceEnte != null && !codiceEnte.equals("")) {

                codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.user_cod_ente, codiceEnte);

                enteGroupId = getCaseSensitiveGroupId(codiceEnte);

                if (enteGroupId == null) {
                    throw new DocerException("gruppo Ente " + codiceEnte + " non trovato");
                }
            }

            if (codiceEnte != null && !codiceEnte.equals("") && codiceAOO != null && !codiceAOO.equals("")) {

                codiceAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.user_cod_aoo, codiceAOO);

                String appoAooGroupId = generateAOOGroupId(codiceAOO, codiceEnte);

                aooGroupId = getCaseSensitiveGroupId(appoAooGroupId);

                if (aooGroupId == null) {
                    throw new DocerException("gruppo AOO " + appoAooGroupId + " non trovato");
                }
            }

            // per gli utenti, a differenza dei gruppi, non possono esistere due
            // utenti che si chiamano uguale case insensitive
            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put("USER_ID", Arrays.asList(userProfile.getUserId()));
            List<IUserProfileInfo> results = searchUsers(searchCriteria);

            if (results != null && results.size() > 0) {
                IUserProfileInfo foundUserProfileInfo = results.get(0);
                throw new DocerException(metodo + ": " + foundUserProfileInfo.getUserId() + ": utente esistente");
            }

            // IUserProfileInfo userProfileInfo =
            // getUserProfile(userProfile.getUserId());
            // if (userProfileInfo != null)
            // throw new DocerException(metodo + ": " + userProfile.getUserId()
            // + ": utente esistente");

            // proprieta' dell'user
            List<NamedValue> properties = new ArrayList<NamedValue>();

            NewUserDetails newUserDetail = new NewUserDetails();
            newUserDetail.setUserName(userProfile.getUserId());

            // si imposta la password come la userid (uno la puo' cambiare
            // quando
            // vuole)
            newUserDetail.setPassword(userProfile.getUserId());
            if (userProfile.getUserPassword() != null && !userProfile.getUserPassword().equals(""))
                newUserDetail.setPassword(userProfile.getUserPassword());

            String fullName = userProfile.getFullName();
            String firstName = userProfile.getFirstName();
            String lastName = userProfile.getLastName();
            String email = userProfile.getEmailAddress();

            if (fullName == null) {
                fullName = "";
            }
            if (firstName == null) {
                firstName = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            if (email == null) {
                email = "";
            }

            // email
            NamedValue nvUserMail = new NamedValue();
            nvUserMail.setName(DocerModel.PROP_USER_MAIL);
            nvUserMail.setValue(email);
            properties.add(nvUserMail);

            // full name (obbligatorio dalla Business Logic)
            NamedValue nvFullName = new NamedValue();
            nvFullName.setName(DocerModel.PROP_USER_FULLNAME);
            nvFullName.setValue(fullName);
            properties.add(nvFullName);

            // first name
            NamedValue nvFirstName = new NamedValue();
            nvFirstName.setName(DocerModel.PROP_USER_FIRSTNAME);
            nvFirstName.setValue(firstName);
            properties.add(nvFirstName);

            // last name
            NamedValue nvLastName = new NamedValue();
            nvLastName.setName(DocerModel.PROP_USER_LASTNAME);
            nvLastName.setValue(lastName);
            properties.add(nvLastName);

            // ente group (viene assegnato alla organizationalId)
            if (codiceEnte != null && !codiceEnte.equals("")) {
                NamedValue nvCodiceEnte = new NamedValue();
                nvCodiceEnte.setName(DocerModel.PROP_USER_COD_ENTE);
                nvCodiceEnte.setValue(codiceEnte);
                properties.add(nvCodiceEnte);
            }
            if (aooGroupId != null && !aooGroupId.equals("")) {
                NamedValue nvCodiceAOO = new NamedValue();
                nvCodiceAOO.setName(DocerModel.PROP_USER_COD_AOO);
                nvCodiceAOO.setValue(codiceAOO);
                properties.add(nvCodiceAOO);
            }
            if (codiceFiscale != null && !codiceFiscale.equals("")) {
                NamedValue nvCodiceAOO = new NamedValue();
                nvCodiceAOO.setName(DocerModel.PROP_USER_COD_FISCALE);
                nvCodiceAOO.setValue(codiceFiscale);
                properties.add(nvCodiceAOO);
            }

            Map<String, String> props = new HashMap<String, String>();
            if (userProfile.getExtraInfo() != null) {
                for (String key : userProfile.getExtraInfo().keySet()) {
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_ente)) {
                        continue;
                    }
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_aoo)) {
                        continue;
                    }
                    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.user_cod_fiscale)) {
                        continue;
                    }

                    props.put(key, userProfile.getExtraInfo().get(key));
                }
            }

            populateAlfrescoProperties(props, properties);

            if (properties.size() > 0)
                newUserDetail.setProperties(properties.toArray(new NamedValue[0]));

            // creo l'utente
            getAlfrescoAdministrationService_createUsers(new NewUserDetails[]{newUserDetail});

            created = true;

            String[] users = new String[]{userProfile.getUserId()};

            // aggiungo i gruppi all'utente (Ente e AOO)
            if (enteGroupId != null && !enteGroupId.equals("")) {
                getAlfrescoAccessControlService_removeChildAuthorities(Constants.GROUP_PREFIX + enteGroupId, users);
                getAlfrescoAccessControlService_addChildAuthorities(Constants.GROUP_PREFIX + enteGroupId, users);
            }

            if (aooGroupId != null && !aooGroupId.equals("")) {
                getAlfrescoAccessControlService_removeChildAuthorities(Constants.GROUP_PREFIX + aooGroupId, users);
                getAlfrescoAccessControlService_addChildAuthorities(Constants.GROUP_PREFIX + aooGroupId, users);
            }

        } catch (DocerException e) {
            if (created)
                deleteUser(userProfile.getUserId());

            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    public void deleteDocument(String docId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "deleteDocument";
        String rif = docId;

        try {

            String lucenequery = "@docarea\\:docnum:\"" + docId + "\" ";

            SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, null, -1, null, false, false, false, false, false, true, null);

            if (webScriptResults.getCount() == 0) {
                // vediamo se sta nel cestino (possono esserci piu' di uno nel
                // secchio con stesso docId)
                List<Reference> deletedList = getDocumentFromBin(docId);

                if (deletedList == null) {
                    throw new DocerException("documento non trovato");
                }

                // altrimenti li cancello dal cestino
                for (Reference ref : deletedList) {
                    deleteFromBin(ref);
                }
                return;
            }

            if (webScriptResults.getCount() > 1) {
                throw new DocerException("trovati " + webScriptResults.getCount() + " risultati");
            }

            Reference docRef = webScriptResults.getFirstAndOnly().getReference();

            EnumACLRights effectiveRights = webScriptResults.getFirstAndOnly().getEffectiveRights();
            if (!effectiveRights.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            CMLDelete cmlDelete = new CMLDelete();
            Predicate delPredicate = createPredicate(docRef);
            cmlDelete.setWhere(delPredicate);

            CML cml = new CML();
            cml.setDelete(new CMLDelete[]{cmlDelete});

            UpdateResult[] updateResult = getAlfrescoRepositoryService_update(cml);

            if (updateResult.length < 1)
                throw new DocerException("errore cancellazione documento");

            // CANCELLO DAL CESTINO
            deleteFromBin(updateResult[0].getSource());
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public Map<String, EnumACLRights> getACLDocument(String docId) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "getACLDocument";
        String rif = docId;

        try {

            String lucenequery = "@docarea\\:docnum:\"" + docId + "\" ";

            List<String> returnProperties = new ArrayList<String>();
            // TODO: da scommentare quando attiviamo ereditarieta' multipla acl
            // returnProperties.add(ACL_EXPLICIT);
            returnProperties.add(it.kdm.docer.sdk.Constants.doc_progr_fascicolo);
            returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);

            SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, returnProperties, -1, null, false, false, false, false, true, false, null);

            if (webScriptResults.getCount() == 0)
                throw new DocerException("documento " + docId + " non trovato");

            if (webScriptResults.getCount() > 1)
                throw new DocerException("trovati " + webScriptResults.getCount() + " risultati");

            Map<String, EnumACLRights> aclmap = webScriptResults.getFirstAndOnly().getAcl();

            Map<String, EnumACLRights> acl = new HashMap<String, EnumACLRights>();

            for (String id : aclmap.keySet()) {
                acl.put(id.replaceAll("^" + Constants.GROUP_PREFIX, ""), aclmap.get(id));
            }

            // TODO: da rivedere
            // if (DOCUMENTO_EREDITA_ACL) {
            //
            // String progressivoFascicolo =
            // webScriptResults.getFirstAndOnly().getProfile().get(it.kdm.docer.sdk.Constants.doc_progr_fascicolo);
            // String parentFolderId =
            // webScriptResults.getFirstAndOnly().getProfile().get(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
            //
            // String aclExplicit = null;
            //
            // // o e' fascicolato o e folderato
            // if (StringUtils.isNotEmpty(progressivoFascicolo) &&
            // DOCUMENTO_EREDITA_ACL) {
            // aclExplicit =
            // webScriptResults.getFirstAndOnly().getProfile().get(ACL_EXPLICIT);
            // }
            // else if (StringUtils.isNotEmpty(parentFolderId) &&
            // DOCUMENTO_EREDITA_ACL) {
            // SearchResultSet srFolder =
            // getFolderFullProfileAsAdmin(parentFolderId);
            // if (srFolder.getCount() == 1) {
            // String folderOwner =
            // srFolder.getFirstAndOnly().getProfile().get(it.kdm.docer.sdk.Constants.folder_owner);
            // if (StringUtils.isEmpty(folderOwner)) {
            // // e' folder pubblica
            // aclExplicit =
            // webScriptResults.getFirstAndOnly().getProfile().get(ACL_EXPLICIT);
            // }
            // }
            // }
            //
            // if (StringUtils.isNotEmpty(aclExplicit)) {
            // acl.clear();
            // String[] arr = aclExplicit.split(";");
            // for (String s : arr) {
            // s = s.trim();
            // String[] aclArr = s.split("=");
            // if (aclArr.length == 2) {
            // String id = aclArr[0];
            // String ace = aclArr[1];
            // if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(ace)) {
            // acl.put(id.replaceAll("^" + Constants.GROUP_PREFIX, ""),
            // convertToDocareaRights(ace));
            // }
            //
            // }
            // }
            // }
            //
            // }

            return acl;
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public void addUsersToGroup(String groupId, List<String> users) throws DocerException {
        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "addUsersToGroup";
        String rif = groupId;

        try {
            List<String> usersIds = getCaseSensitiveUsersIds(users);

            for (String uid : users) {
                if (usersIds.contains(uid)) {
                    continue;
                }

                throw new DocerException("utente " + uid + " non trovato");
            }

            IGroupInfo groupInfo = _getGroup(groupId);

            if (groupInfo == null)
                throw new DocerException("gruppo " + groupId + " non trovato");

            // tolgo quelli gia' associati
            for (String id : groupInfo.getUsers()) {
                if (usersIds.contains(id))
                    usersIds.remove(id);
            }

            if (usersIds != null && usersIds.size() > 0) {
                // assegno i nuovi utenti al gruppo
                accessControlService_addChildAuthorities(Constants.GROUP_PREFIX + groupInfo.getProfileInfo().getGroupId(), usersIds);

            }
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    public void removeUsersFromGroup(String groupId, List<String> users) throws DocerException {
        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        String metodo = "RemoveUsersFromGroup";
        String rif = groupId;

        try {
            groupId = getCaseSensitiveGroupId(groupId);

            if (groupId == null)
                throw new DocerException("gruppo " + rif + " non trovato");

            List<String> usersIds = getCaseSensitiveUsersIds(users);

            if (usersIds != null && usersIds.size() > 0) {
                getAlfrescoAccessControlService_removeChildAuthorities(Constants.GROUP_PREFIX + groupId, usersIds);
            }
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }

    }

    public void addGroupsToUser(String userId, List<String> groups) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        if (groups == null)
            return;

        String metodo = "addGroupsToUser";
        String rif = userId;

        try {
            // recupero profilo dell'utente (per recuperare user-id case
            // sensitive)
            IUserInfo userInfo = _getUser(userId);
            if (userInfo == null)
                throw new DocerException("utente " + userId + " non trovato");

            // recupero veri id dei gruppi
            List<String> alfGroupsIds = getCaseSensitiveGroupsIds(groups);

            for (String gid : groups) {
                if (alfGroupsIds.contains(gid)) {
                    continue;
                }

                throw new DocerException("gruppo " + gid + " non trovato");
            }

            String[] user = new String[]{userInfo.getProfileInfo().getUserId()};

            for (String groupAuthId : alfGroupsIds) {
                if (groupAuthId == null || groupAuthId.equals(""))
                    continue;

                // se il gruppo fa gia' parte dei gruppi dell'utente
                if (collectionContainsString(true, userInfo.getGroups(), groupAuthId))
                    continue;

                getAlfrescoAccessControlService_addChildAuthorities(Constants.GROUP_PREFIX + groupAuthId, user);

            }
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + rif + ": " + e.getMessage());
        }
    }

    // ***** METODI PRIVATI ****
    // ricava l'uuid del risultato in una collezione di element (risultato della
    // ricerca col web-script) basandosi sul nome
    // private Reference getNodeReferenceFromResultsByCmName(SearchResultSet
    // webScriptResult, String nameToFind) {
    //
    // if (webScriptResult.getCount() > 0) {
    // for (int i = 0; i < webScriptResult.getCount(); i++) {
    // List<Element> nodeproperties =
    // webScriptResult.getAllResultsOrderedMap().get(i);
    // String name = getElementPropertyValue("cm:name", nodeproperties);
    // if (nameToFind.equalsIgnoreCase(name)) {
    // String uuid = getElementPropertyValue("sys:node-uuid", nodeproperties);
    // return createReference(uuid);
    // }
    // }
    // }
    //
    // return null;
    // }

    // private String getElementPropertyValue(String propName, List<Element>
    // properties) {
    //
    // for (Element propElement : properties) {
    //
    // String alfPropName = propElement.getAttribute("name");
    //
    // String propValue = propElement.getAttribute("value");
    // if (propValue == null)
    // propValue = "";
    //
    // if (alfPropName.matches(propName)) {
    // return propValue;
    // }
    //
    // }
    //
    // return "";
    // }

    private static String combineUrl(String url1, String url2) {
        url1 = url1.replaceAll("/+$", "");
        url2 = url2.replaceAll("^/+", "");
        String combiUrl = url1 + "/" + url2;
        return combiUrl;
    }

    private String getLibraryForEnte(String codEnte) {

        String key = codEnte;

        if (key == null) {
            key = "";
        }
        key = key.toUpperCase();

        String libraryName = DEFAULT_LIBRARY;
        if (LIBRARY_MAPPING.containsKey(key)) {
            libraryName = LIBRARY_MAPPING.get(key);
            if (libraryName == null || libraryName.equals(""))
                libraryName = DEFAULT_LIBRARY;
        }

        return libraryName;
    }

    private void createGroupRunasAdmin(String groupId, String groupName, String parentGroupId, boolean gruppoStruttura) throws DocerException {

        InputStream responseStream = null;
        PostMethod method = null;
        HttpClient httpClient = null;
        String url = null;

        try {

            url = combineUrl(ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$", ""), "/service/kdm/runas");

            httpClient = new HttpClient();

            method = new PostMethod(url);

            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            method.addRequestHeader("Connection", "Keep-Alive");

            List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();

            NameValuePair nv1 = new NameValuePair();
            nv1.setName("alf_ticket");
            nv1.setValue(URLEncoder.encode(currentUser.getTicket(), "UTF-8"));
            parametersBody.add(nv1);

            NameValuePair nvOp = new NameValuePair();
            nvOp.setName("operation");
            nvOp.setValue("creategroup");
            parametersBody.add(nvOp);

            // URLEncoder.encode(
            NameValuePair nv2 = new NameValuePair();
            nv2.setName("gruppostruttura");
            nv2.setValue(String.valueOf(gruppoStruttura));
            parametersBody.add(nv2);

            NameValuePair nv3 = new NameValuePair();
            nv3.setName("runas");
            nv3.setValue(ADMIN_USERID);
            parametersBody.add(nv3);

            NameValuePair nv4 = new NameValuePair();
            nv4.setName("groupid");
            nv4.setValue(groupId);
            parametersBody.add(nv4);

            NameValuePair nv5 = new NameValuePair();
            nv5.setName("groupname");
            nv5.setValue(groupName);
            parametersBody.add(nv5);

            NameValuePair nv6 = new NameValuePair();
            nv6.setName("parentgroupid");
            nv6.setValue(parentGroupId);
            parametersBody.add(nv6);

            method.setRequestBody(parametersBody.toArray(new NameValuePair[0]));

            int resCode = httpClient.executeMethod(method);

            if (resCode != 200) {
                // String responseString = method.getResponseBodyAsString();
                InputStream in = method.getResponseBodyAsStream();
                String encoding = method.getResponseCharSet();
                encoding = encoding == null ? "UTF-8" : encoding;
                String responseString = IOUtils.toString(in, encoding);

                int arg0 = responseString.indexOf("InizioErrore-") + 13;
                int arg1 = responseString.indexOf("-FineErrore");

                if (arg1 > arg0)
                    throw new Exception("errore http: " + responseString.substring(arg0, arg1));

                if (resCode == 401)
                    throw new Exception(UNAUTH_SESSION_ERROR);

                throw new Exception("errore http: " + resCode);
            }

        } catch (Exception e) {
            throw new DocerException("createGroupRunasAdmin: " + e.getMessage());
        } finally {
            try {
                if (responseStream != null)
                    responseStream.close();
            } catch (IOException e) {

            }

            if (method != null) {
                method.releaseConnection();
            }
        }

    }

    private String queryParentRunasAdmin(String nodeuuid) throws DocerException {

        InputStream responseStream = null;
        PostMethod method = null;
        HttpClient httpClient = null;
        String url = null;

        try {

            url = combineUrl(ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$", ""), "/service/kdm/runas");

            httpClient = new HttpClient();

            method = new PostMethod(url);

            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            method.addRequestHeader("Connection", "Keep-Alive");

            List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();

            NameValuePair nv1 = new NameValuePair();
            nv1.setName("alf_ticket");
            nv1.setValue(URLEncoder.encode(currentUser.getTicket(), "UTF-8"));
            parametersBody.add(nv1);

            NameValuePair nvOp = new NameValuePair();
            nvOp.setName("operation");
            nvOp.setValue("queryparent");
            parametersBody.add(nvOp);

            NameValuePair nv3 = new NameValuePair();
            nv3.setName("runas");
            nv3.setValue(ADMIN_USERID);
            parametersBody.add(nv3);

            NameValuePair nv4 = new NameValuePair();
            nv4.setName("uuid");
            nv4.setValue(nodeuuid);
            parametersBody.add(nv4);

            method.setRequestBody(parametersBody.toArray(new NameValuePair[0]));

            int resCode = httpClient.executeMethod(method);

            // String responseString = method.getResponseBodyAsString();
            InputStream in = method.getResponseBodyAsStream();
            String encoding = method.getResponseCharSet();
            encoding = encoding == null ? "UTF-8" : encoding;
            String responseString = IOUtils.toString(in, encoding);

            if (resCode != 200) {

                int arg0 = responseString.indexOf("InizioErrore-") + 13;
                int arg1 = responseString.indexOf("-FineErrore");

                if (arg1 > arg0)
                    throw new Exception("errore http: " + responseString.substring(arg0, arg1));

                if (resCode == 401)
                    throw new Exception(UNAUTH_SESSION_ERROR);

                throw new Exception("errore http: " + resCode);
            }

            return responseString;

        } catch (Exception e) {
            throw new DocerException("queryParentRunasAdmin: " + e.getMessage());
        } finally {
            try {
                if (responseStream != null)
                    responseStream.close();
            } catch (IOException e) {

            }

            if (method != null) {
                method.releaseConnection();
            }
        }

    }

    private void moveToSpaceRunasAdmin(String tomoveUUID, String newParentUUID, Boolean inherits, String name) throws DocerException {

        InputStream responseStream = null;
        PostMethod method = null;
        HttpClient httpClient = null;
        String url = null;

        try {

            url = combineUrl(ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$", ""), "/service/kdm/runas");

            httpClient = new HttpClient();

            method = new PostMethod(url);

            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            method.addRequestHeader("Connection", "Keep-Alive");

            List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();

            if (inherits != null) {
                NameValuePair nvInherits = new NameValuePair();
                nvInherits.setName("inherits");
                nvInherits.setValue(Boolean.toString(inherits));
                parametersBody.add(nvInherits);
            }

            NameValuePair nv1 = new NameValuePair();
            nv1.setName("alf_ticket");
            nv1.setValue(URLEncoder.encode(currentUser.getTicket(), "UTF-8"));
            parametersBody.add(nv1);

            NameValuePair nvop = new NameValuePair();
            nvop.setName("operation");
            nvop.setValue("move");
            parametersBody.add(nvop);

            NameValuePair nv2 = new NameValuePair();
            nv2.setName("runas");
            nv2.setValue(String.valueOf(ADMIN_USERID));
            parametersBody.add(nv2);

            NameValuePair nv3 = new NameValuePair();
            nv3.setName("adminuid");
            nv3.setValue(ADMIN_USERID);
            parametersBody.add(nv3);

            NameValuePair nv4 = new NameValuePair();
            nv4.setName("nodeuuid");
            nv4.setValue(tomoveUUID);
            parametersBody.add(nv4);

            NameValuePair nv5 = new NameValuePair();
            nv5.setName("spaceuuid");
            nv5.setValue(newParentUUID);
            parametersBody.add(nv5);

            NameValuePair nv6 = new NameValuePair();
            nv6.setName("newname");
            nv6.setValue(name);
            parametersBody.add(nv6);

            NameValuePair nv7 = new NameValuePair();
            nv7.setName("documento_eredita_acl_fascicolo");
            nv7.setValue(String.valueOf(DOCUMENTO_EREDITA_ACL_FASCICOLO));
            parametersBody.add(nv7);

            NameValuePair nv8 = new NameValuePair();
            nv8.setName("documento_eredita_acl_folder");
            nv8.setValue(String.valueOf(DOCUMENTO_EREDITA_ACL_FOLDER));
            parametersBody.add(nv8);

            method.setRequestBody(parametersBody.toArray(new NameValuePair[0]));

            int resCode = httpClient.executeMethod(method);

            if (resCode != 200) {
                // String responseString = method.getResponseBodyAsString();
                InputStream in = method.getResponseBodyAsStream();
                String encoding = method.getResponseCharSet();
                encoding = encoding == null ? "UTF-8" : encoding;
                String responseString = IOUtils.toString(in, encoding);

                int arg0 = responseString.indexOf("InizioErrore-") + 13;
                int arg1 = responseString.indexOf("-FineErrore");

                if (arg1 > arg0)
                    throw new Exception("errore http: " + responseString.substring(arg0, arg1));

                if (resCode == 401)
                    throw new Exception(UNAUTH_SESSION_ERROR);

                throw new Exception("errore http: " + resCode);
            }

        } catch (Exception e) {
            throw new DocerException("moveToSpaceRunasAdmin: " + e.getMessage());
        } finally {
            try {
                if (responseStream != null)
                    responseStream.close();
            } catch (IOException e) {

            }

            if (method != null) {
                method.releaseConnection();
            }
        }

    }

    private void setInheritsRunasAdmin(Reference reference, boolean inherits) throws DocerException {
        reference.setPath(null);

        InputStream responseStream = null;
        PostMethod method = null;
        HttpClient httpClient = null;
        String url = null;

        try {

            url = combineUrl(ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$", ""), "/service/kdm/runas");

            httpClient = new HttpClient();

            method = new PostMethod(url);

            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            method.addRequestHeader("Connection", "Keep-Alive");

            List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();

            NameValuePair nv1 = new NameValuePair();
            nv1.setName("alf_ticket");
            nv1.setValue(URLEncoder.encode(currentUser.getTicket(), "UTF-8"));
            parametersBody.add(nv1);

            NameValuePair nv2 = new NameValuePair();
            nv2.setName("operation");
            nv2.setValue("setinherits");
            parametersBody.add(nv2);

            NameValuePair nv3 = new NameValuePair();
            nv3.setName("runas");
            nv3.setValue(ADMIN_USERID);
            parametersBody.add(nv3);

            NameValuePair nv4 = new NameValuePair();
            nv4.setName("uuid");
            nv4.setValue(reference.getUuid());
            parametersBody.add(nv4);

            NameValuePair nv5 = new NameValuePair();
            nv5.setName("inherits");
            nv5.setValue(String.valueOf(inherits));
            parametersBody.add(nv5);

            method.setRequestBody(parametersBody.toArray(new NameValuePair[0]));

            int resCode = httpClient.executeMethod(method);

            if (resCode != 200) {
                // String responseString = method.getResponseBodyAsString();
                InputStream in = method.getResponseBodyAsStream();
                String encoding = method.getResponseCharSet();
                encoding = encoding == null ? "UTF-8" : encoding;
                String responseString = IOUtils.toString(in, encoding);

                int arg0 = responseString.indexOf("InizioErrore-") + 13;
                int arg1 = responseString.indexOf("-FineErrore");

                if (arg1 > arg0)
                    throw new Exception("errore http: " + responseString.substring(arg0, arg1));

                if (resCode == 401)
                    throw new Exception(UNAUTH_SESSION_ERROR);

                throw new Exception("errore http: " + resCode);
            }

        } catch (Exception e) {
            throw new DocerException("setInheritsRunasAdmin: " + e.getMessage());
        } finally {
            try {
                if (responseStream != null)
                    responseStream.close();
            } catch (IOException e) {

            }

            if (method != null) {
                method.releaseConnection();
            }
        }
    }

    private void setACESRunasAdmin(Reference reference, ACE[] aces, Boolean inherits) throws DocerException {
        // _deletePermissions(reference);
        _setACESRunasAdmin(reference, Arrays.asList(aces));

        if (inherits != null) {
            setInheritsRunasAdmin(reference, inherits);
        }

    }

    private void setACESRunasAdmin(Reference reference, List<ACE> aces, Boolean inherits) throws DocerException {
        // _deletePermissions(reference);
        _setACESRunasAdmin(reference, aces);

        if (inherits != null) {
            setInheritsRunasAdmin(reference, inherits);
        }
    }

    private void _setACESRunasAdmin(Reference reference, List<ACE> aces) throws DocerException {

        InputStream responseStream = null;
        PostMethod method = null;
        HttpClient httpClient = null;
        String url = null;

        try {

            url = combineUrl(ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$", ""), "/service/kdm/runas");

            httpClient = new HttpClient();

            method = new PostMethod(url);

            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            method.addRequestHeader("Connection", "Keep-Alive");

            List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();

            NameValuePair nv1 = new NameValuePair();
            nv1.setName("alf_ticket");
            nv1.setValue(URLEncoder.encode(currentUser.getTicket(), "UTF-8"));
            parametersBody.add(nv1);

            NameValuePair nv2 = new NameValuePair();
            nv2.setName("operation");
            nv2.setValue("setaces");
            parametersBody.add(nv2);

            NameValuePair nv3 = new NameValuePair();
            nv3.setName("runas");
            nv3.setValue(ADMIN_USERID);
            parametersBody.add(nv3);

            NameValuePair nv4 = new NameValuePair();
            nv4.setName("uuid");
            nv4.setValue(reference.getUuid());
            parametersBody.add(nv4);

            String strAces = "";

            for (ACE ace : aces) {
                strAces += ace.getAuthority() + "=" + ace.getPermission() + ";";
            }

            strAces = strAces.replaceAll(";$", "");

            NameValuePair nv5 = new NameValuePair();
            nv5.setName("aces");
            nv5.setValue(strAces);
            parametersBody.add(nv5);

            NameValuePair nv7 = new NameValuePair();
            nv7.setName("documento_eredita_acl_fascicolo");
            nv7.setValue(String.valueOf(DOCUMENTO_EREDITA_ACL_FASCICOLO));
            parametersBody.add(nv7);

            NameValuePair nv8 = new NameValuePair();
            nv8.setName("documento_eredita_acl_folder");
            nv8.setValue(String.valueOf(DOCUMENTO_EREDITA_ACL_FOLDER));
            parametersBody.add(nv8);


            method.setRequestBody(parametersBody.toArray(new NameValuePair[0]));

            int resCode = httpClient.executeMethod(method);

            if (resCode != 200) {
                // String responseString = method.getResponseBodyAsString();
                InputStream in = method.getResponseBodyAsStream();
                String encoding = method.getResponseCharSet();
                encoding = encoding == null ? "UTF-8" : encoding;
                String responseString = IOUtils.toString(in, encoding);

                int arg0 = responseString.indexOf("InizioErrore-") + 13;
                int arg1 = responseString.indexOf("-FineErrore");

                if (arg1 > arg0)
                    throw new Exception("errore http: " + responseString.substring(arg0, arg1));

                if (resCode == 401)
                    throw new Exception(UNAUTH_SESSION_ERROR);

                throw new Exception("errore http: " + resCode);
            }

        } catch (Exception e) {
            throw new DocerException("setACESRunasAdmin: " + e.getMessage());
        } finally {
            try {
                if (responseStream != null)
                    responseStream.close();
            } catch (IOException e) {

            }

            if (method != null) {
                method.releaseConnection();
            }
        }

    }

    // solo per la creazione della AOO
    private void addACESRunasAdmin(Reference reference, ACE[] aces, boolean doInheritsAsAdmin) throws DocerException {

        _addACESRunasAdmin(reference, Arrays.asList(aces));

        setInheritsRunasAdmin(reference, false);
    }

    private void _addACESRunasAdmin(Reference reference, List<ACE> aces) throws DocerException {

        InputStream responseStream = null;
        PostMethod method = null;
        HttpClient httpClient = null;
        String url = null;

        try {

            url = combineUrl(ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$", ""), "/service/kdm/runas");

            httpClient = new HttpClient();

            method = new PostMethod(url);

            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            method.addRequestHeader("Connection", "Keep-Alive");

            List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();

            NameValuePair nv1 = new NameValuePair();
            nv1.setName("alf_ticket");
            nv1.setValue(URLEncoder.encode(currentUser.getTicket(), "UTF-8"));
            parametersBody.add(nv1);

            NameValuePair nv2 = new NameValuePair();
            nv2.setName("operation");
            nv2.setValue("addaces");
            parametersBody.add(nv2);

            NameValuePair nv3 = new NameValuePair();
            nv3.setName("runas");
            nv3.setValue(ADMIN_USERID);
            parametersBody.add(nv3);

            NameValuePair nv4 = new NameValuePair();
            nv4.setName("uuid");
            nv4.setValue(reference.getUuid());
            parametersBody.add(nv4);

            String strAces = "";

            for (ACE ace : aces) {
                strAces += ace.getAuthority() + "=" + ace.getPermission() + ";";
            }

            strAces = strAces.replaceAll(";$", "");

            NameValuePair nv5 = new NameValuePair();
            nv5.setName("aces");
            nv5.setValue(strAces);
            parametersBody.add(nv5);

            method.setRequestBody(parametersBody.toArray(new NameValuePair[0]));

            int resCode = httpClient.executeMethod(method);

            if (resCode != 200) {
                // String responseString = method.getResponseBodyAsString();
                InputStream in = method.getResponseBodyAsStream();
                String encoding = method.getResponseCharSet();
                encoding = encoding == null ? "UTF-8" : encoding;
                String responseString = IOUtils.toString(in, encoding);

                int arg0 = responseString.indexOf("InizioErrore-") + 13;
                int arg1 = responseString.indexOf("-FineErrore");

                if (arg1 > arg0)
                    throw new Exception("errore http: " + responseString.substring(arg0, arg1));

                if (resCode == 401)
                    throw new Exception(UNAUTH_SESSION_ERROR);

                throw new Exception("errore http: " + resCode);
            }

        } catch (Exception e) {
            throw new DocerException("addACESRunasAdmin: " + e.getMessage());
        } finally {
            try {
                if (responseStream != null)
                    responseStream.close();
            } catch (IOException e) {

            }

            if (method != null) {
                method.releaseConnection();
            }
        }

    }

    private void _deletePermissions(Reference reference) throws DocerException {

        // InputStream responseStream = null;
        // PostMethod method = null;
        // HttpClient httpClient = null;
        // String url = null;
        //
        // try {
        //
        // url = combineUrl(ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$",
        // ""), "/service/kdm/runas");
        //
        // httpClient = new HttpClient();
        //
        // method = new PostMethod(url);
        //
        // method.addRequestHeader("Content-Type",
        // "application/x-www-form-urlencoded");
        // method.addRequestHeader("Connection", "Keep-Alive");
        //
        // List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();
        //
        // NameValuePair nv1 = new NameValuePair();
        // nv1.setName("alf_ticket");
        // nv1.setValue(URLEncoder.encode(currentUser.getTicket(), "UTF-8"));
        // parametersBody.add(nv1);
        //
        // NameValuePair nv2 = new NameValuePair();
        // nv2.setName("operation");
        // nv2.setValue("deletepermissions");
        // parametersBody.add(nv2);
        //
        // NameValuePair nv3 = new NameValuePair();
        // nv3.setName("runas");
        // nv3.setValue(ADMIN_USERID);
        // parametersBody.add(nv3);
        //
        // NameValuePair nv4 = new NameValuePair();
        // nv4.setName("uuid");
        // nv4.setValue(reference.getUuid());
        // parametersBody.add(nv4);
        //
        // method.setRequestBody(parametersBody.toArray(new NameValuePair[0]));
        //
        // int resCode = httpClient.executeMethod(method);
        //
        // if (resCode != 200) {
        // // String responseString = method.getResponseBodyAsString();
        // InputStream in = method.getResponseBodyAsStream();
        // String encoding = method.getResponseCharSet();
        // encoding = encoding == null ? "UTF-8" : encoding;
        // String responseString = IOUtils.toString(in, encoding);
        //
        // int arg0 = responseString.indexOf("InizioErrore-") + 13;
        // int arg1 = responseString.indexOf("-FineErrore");
        //
        // if (arg1 > arg0)
        // throw new Exception("errore http: " + responseString.substring(arg0,
        // arg1));
        //
        // if (resCode == 401)
        // throw new Exception(UNAUTH_SESSION_ERROR);
        //
        // throw new Exception("errore http: " + resCode);
        // }
        //
        // }
        // catch (Exception e) {
        // throw new DocerException("deletePermissions: " + e.getMessage());
        // }
        // finally {
        // try {
        // if (responseStream != null)
        // responseStream.close();
        // }
        // catch (IOException e) {
        //
        // }
        //
        // if (method != null) {
        // method.releaseConnection();
        // }
        // }

    }

    private void setOwnerRunasAdmin(Reference reference, String owner) throws DocerException {

        InputStream responseStream = null;
        PostMethod method = null;
        HttpClient httpClient = null;
        String url = null;

        try {

            url = combineUrl(ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$", ""), "/service/kdm/runas");

            httpClient = new HttpClient();

            method = new PostMethod(url);

            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            method.addRequestHeader("Connection", "Keep-Alive");

            List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();

            NameValuePair nv1 = new NameValuePair();
            nv1.setName("alf_ticket");
            nv1.setValue(URLEncoder.encode(currentUser.getTicket(), "UTF-8"));
            parametersBody.add(nv1);

            NameValuePair nv2 = new NameValuePair();
            nv2.setName("operation");
            nv2.setValue("setowner");
            parametersBody.add(nv2);

            NameValuePair nv3 = new NameValuePair();
            nv3.setName("runas");
            nv3.setValue(ADMIN_USERID);
            parametersBody.add(nv3);

            NameValuePair nv4 = new NameValuePair();
            nv4.setName("uuid");
            nv4.setValue(reference.getUuid());
            parametersBody.add(nv4);

            NameValuePair nv5 = new NameValuePair();
            nv5.setName("owner");
            nv5.setValue(owner);
            parametersBody.add(nv5);

            method.setRequestBody(parametersBody.toArray(new NameValuePair[0]));

            int resCode = httpClient.executeMethod(method);

            if (resCode != 200) {
                // String responseString = method.getResponseBodyAsString();
                InputStream in = method.getResponseBodyAsStream();
                String encoding = method.getResponseCharSet();
                encoding = encoding == null ? "UTF-8" : encoding;
                String responseString = IOUtils.toString(in, encoding);

                int arg0 = responseString.indexOf("InizioErrore-") + 13;
                int arg1 = responseString.indexOf("-FineErrore");

                if (arg1 > arg0)
                    throw new Exception("errore http: " + responseString.substring(arg0, arg1));

                if (resCode == 401)
                    throw new Exception(UNAUTH_SESSION_ERROR);

                throw new Exception("errore http: " + resCode);
            }

        } catch (Exception e) {
            throw new DocerException("setOwnerRunasAdmin: " + e.getMessage());
        } finally {
            try {
                if (responseStream != null)
                    responseStream.close();
            } catch (IOException e) {

            }

            if (method != null) {
                method.releaseConnection();
            }
        }

    }

    private void uploadFile(Reference contentRef, String filename, InputStream fileStream) throws DocerException {

        String metodo = "uploadFile";

        String putUrl;
        String params;
        HttpClient httpClient = null;

        try {
            putUrl = ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$", "") + DocerModel.ALFRESCO_UPLOAD_URL + contentRef.getUuid() + "/" + URLEncoder.encode(filename, "UTF-8");
            params = "?filename=" + URLEncoder.encode(filename, "UTF-8") + "&encoding=UTF-8&ticket=" + currentUser.getTicket();
        } catch (UnsupportedEncodingException e1) {
            throw new DocerException(metodo + ": unsupportedEncodingException:" + e1.getMessage());
        } catch (Exception e) {
            throw new DocerException(metodo + ": generic exception:" + e.getMessage());
        }

        // VERIFICARE CHE SI FA UPLOAD SENZA PASSWORD MA CON IL TICKET
        // String creds = user.getUserId()+ ":" + user.getPassword();

        httpClient = new HttpClient();

        PutMethod method = new PutMethod(putUrl + params);
        method.addRequestHeader("Connection", "Keep-Alive");

        RequestEntity reqEnt = new InputStreamRequestEntity(fileStream);

        method.setRequestEntity(reqEnt);
        // method.setRequestBody(inputStream);
        method.setContentChunked(true);

        // Header localeHeader = new Header("Content-Language", "it_IT");
        // method.setRequestHeader(localeHeader);

        int status = 0;
        String risposta = "";
        try {
            status = httpClient.executeMethod(method);
            // risposta = method.getResponseBodyAsString();
            InputStream in = method.getResponseBodyAsStream();
            String encoding = method.getResponseCharSet();
            encoding = encoding == null ? "UTF-8" : encoding;
            risposta = IOUtils.toString(in, encoding);
        } catch (HttpException e) {
            throw new DocerException(metodo + ": HttpException:" + e.getMessage());
        } catch (IOException e) {
            throw new DocerException(metodo + ": IOException:" + e.getMessage());
        } finally {
            method.releaseConnection();
        }

        if (risposta.indexOf("|size=0|") >= 0)
            throw new DocerException(metodo + ": file length = 0");

        if (risposta.contains("is locked")) {
            throw new DocerException(metodo + ": documento bloccato esclusivamente da un altro utente");
        }

        if (risposta.indexOf("|size=") < 0)
            throw new DocerException(metodo + ": upload error");

        if (status != 200)
            throw new DocerException(metodo + ": upload error: status " + status);

    }

    private byte[] downloadFile(String token, String urlSuffix, String bufferPath, long maxFileLength, String servletUrl) throws DocerException {

        String metodo = "downloadFile";
        String downloadUrl = ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$", "") + servletUrl + urlSuffix;
        String params = "?ticket=" + token;

        HttpClient httpClient = null;

        GetMethod method = new GetMethod(downloadUrl + params);
        method.addRequestHeader("Connection", "Keep-Alive");

        InputStream responseStream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = null;
        FileOutputStream filesystemOutputStream = null;

        int status = 0;
        try {

            httpClient = new HttpClient();

            status = httpClient.executeMethod(method);

            if (status != 200)
                throw new DocerException(metodo + ": status " + status + "; downloadUrl: " + downloadUrl);

            responseStream = method.getResponseBodyAsStream();

            // buffer
            byte[] buffer = new byte[4096];

            int read = 1;
            long totalRead = 0;

            // while (totalRead < maxFileLength && read>0)
            // {
            // read = responseStream.read(buffer, 0, buffer.length);
            // if (read>=0)
            // {
            // outputStr.write(buffer, 0, read);
            // totalRead += read;
            // }
            // }

            while (totalRead < maxFileLength && ((read = responseStream.read(buffer, 0, buffer.length)) > 0)) {
                baos.write(buffer, 0, read);
                totalRead += read;
            }

            // se ho letto tutto lo stream (sono in memoria) restituisco il
            // byte[]
            if (read <= 0) {

                return baos.toByteArray();
            }

            // *********+
            // CI SONO ANCORA BYTE DA LEGGERE DALLO STREAM DELLA RISPOSTA
            // ************

            // stream per scrivere su filesystem
            filesystemOutputStream = new FileOutputStream(bufferPath);

            // copio il file parziale finora ricevuto, su file system
            bais = new ByteArrayInputStream(baos.toByteArray());
            while ((read = bais.read(buffer, 0, buffer.length)) > 0) {
                filesystemOutputStream.write(buffer, 0, read);
            }

            baos.close();
            bais.close();

            // continuo a scrivere su filesystem ma leggendo il continuo dello
            // stream della Response
            while ((read = responseStream.read(buffer, 0, buffer.length)) > 0) {
                filesystemOutputStream.write(buffer, 0, read);
            }

            filesystemOutputStream.close();

            return null;

        } catch (IOException e) {
            throw new DocerException(metodo + ": IOException: " + e.getMessage());

        }
        // catch (RemoteException e) {
        // throw new DocerException(-1295, "Exception: DownloadFile: "
        // +e.getMessage());
        // }
        finally {

            try {
                if (responseStream != null)
                    responseStream.close();
                if (filesystemOutputStream != null)
                    filesystemOutputStream.close();

                if (baos != null)
                    baos.close();
                if (bais != null)
                    bais.close();

            } catch (IOException e2) {

            }

            method.releaseConnection();

        }
    }

    private IUserInfo _getUser(String userId) throws DocerException {

        IUserProfileInfo userProfileInfo = getUserProfile(userId);
        if (userProfileInfo == null)
            return null;

        String metodo = "_getUser";
        try {
            IUserInfo userInfo = new UserInfo();
            userInfo.setProfileInfo(userProfileInfo);

            SiblingAuthorityFilter siblingAuthorityFilter = new SiblingAuthorityFilter();
            siblingAuthorityFilter.setAuthorityType("GROUP");
            // se immediate = true recupera solo il gruppo parent diretto se
            // false
            // li recupera tutti
            siblingAuthorityFilter.setImmediate(true);

            String[] groups = getAlfrescoAccessControlService_getParentAuthorities(userProfileInfo.getUserId(), siblingAuthorityFilter);

            if (groups != null)
                for (String groupId : groups) {
                    // if (!userInfo.getGroups().contains(groupId)) non c'e'
                    // bisogno
                    // del controllo perche' un utente non puo' far parte due
                    // volte
                    // dello stesso gruppo
                    userInfo.getGroups().add(groupId.replaceAll("^" + Constants.GROUP_PREFIX, ""));
                }

            return userInfo;
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    private IUserProfileInfo getUserProfile(String userId) throws DocerException {
        if (userId == null)
            return null;

        String metodo = "getUserProfile";

        try {

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put(it.kdm.docer.sdk.Constants.user_user_id, Arrays.asList(userId));

            List<IUserProfileInfo> users = searchUsers(searchCriteria);

            if (users.size() < 1) {
                return null;
            }

            if (users.size() > 1) {
                throw new DocerException("trovati " + users.size() + " utenti con userId=" + userId);
            }

            return users.get(0);
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    private boolean collectionContainsString(boolean ignorecase, List<String> collection, String stringToFind) {

        for (String str : collection) {
            if (ignorecase) {
                if (str.equalsIgnoreCase(stringToFind))
                    return true;
            } else if (str.equals(stringToFind))
                return true;
        }
        return false;
    }

    private IGroupInfo _getGroup(String groupId) throws DocerException {

        String metodo = "_getGroup";

        // recupero le informazioni del gruppo ad eccezione degli utenti
        IGroupProfileInfo groupProfileInfo = getGroupProfile(groupId);

        if (groupProfileInfo == null)
            return null;

        try {
            IGroupInfo groupInfo = new GroupInfo();
            groupInfo.setProfileInfo(groupProfileInfo);

            SiblingAuthorityFilter siblingAuthorityFilter = new SiblingAuthorityFilter();
            siblingAuthorityFilter.setAuthorityType("USER");
            siblingAuthorityFilter.setImmediate(true);

            String[] users = getAlfrescoAccessControlService_getChildAuthorities(Constants.GROUP_PREFIX + groupProfileInfo.getGroupId(), siblingAuthorityFilter);

            if (users != null) {
                for (String userId : users) {
                    groupInfo.getUsers().add(userId);
                }
            }

            return groupInfo;
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    private IGroupProfileInfo getGroupProfile(String groupId) throws DocerException {

        if (groupId == null)
            return null;

        String metodo = "getGroupProfile";

        try {

            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
            searchCriteria.put(it.kdm.docer.sdk.Constants.group_group_id, Arrays.asList(groupId));
            List<IGroupProfileInfo> groups = searchGroups(searchCriteria);

            if (groups.size() < 1)
                return null;

            if (groups.size() > 1) {
                throw new DocerException("trovati " + groups.size() + " gruppi con groupId=" + groupId);
            }

            return groups.get(0);

        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }

    }

    // private void setOwner(Reference nodeRef, String userId, String
    // messageSuffix) throws DocerException {

    // try {
    // WebServiceFactory.getAccessControlService().setOwners(createPredicate(nodeRef),
    // userId);
    // }
    // catch (AccessControlFault e) {
    // throw new DocerException(message);
    // }
    // catch (RemoteException e) {
    // if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
    // throw new DocerException(UNAUTH_SESSION_ERROR);
    // }
    // throw new DocerException("setOwner: " + messageSuffix + ": " +
    // e.getMessage());
    // }
    // }

    // private OwnerResult[] getOwners(Reference nodeRef) throws DocerException
    // {
    //
    // try {
    // return
    // WebServiceFactory.getAccessControlService().getOwners(createPredicate(nodeRef));
    // }
    // catch (AccessControlFault e) {
    // throw new DocerException("getOwners: " + e.getMessage());
    // }
    // catch (RemoteException e) {
    // if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
    // throw new DocerException(UNAUTH_SESSION_ERROR);
    // }
    // throw new DocerException("getOwners: " + e.getMessage());
    // }
    // }

    private void setAuthor(Reference nodeRef, String userId) throws DocerException {

        String metodo = "setAuthor";
        try {
            NamedValue propAuthor = new NamedValue();
            propAuthor.setName("{http://www.alfresco.org/model/content/1.0}author");
            propAuthor.setValue(userId);

            // CML per aggiornamento profilo
            CMLUpdate cmlUpdateDoc = new CMLUpdate();
            cmlUpdateDoc.setProperty(new NamedValue[]{propAuthor}); // proprieta'
            cmlUpdateDoc.setWhere(createPredicate(nodeRef)); // il documento

            CML cml = new CML();
            cml.setUpdate(new CMLUpdate[]{cmlUpdateDoc});

            // UpdateResult[] risultati = null;
            getAlfrescoRepositoryService_update(cml);
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    // private void setInheritPermissionsToFalse(Reference nodeRef) throws
    // DocerException {
    //
    // try {
    // WebServiceFactory.getAccessControlService().setInheritPermission(createPredicate(nodeRef),
    // false);
    // }
    // catch (AccessControlFault e) {
    // throw new DocerException("setInheritPermissionsToFalse:" +
    // e.getMessage1());
    // }
    // catch (RemoteException e) {
    // if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
    // throw new DocerException(UNAUTH_SESSION_ERROR);
    // }
    // throw new DocerException("setInheritPermission:" + e.getMessage());
    // }
    // }

    private Reference getAnnoFascicoloReference(String annoFascicolo, Reference titolarioRef) throws DocerException {

        String metodo = "getAnnoFascicoloReference";

        Query query = new Query();

        try {

            // Create a query object
            String search = DocerModel.SEARCH_QUERY_ANNO_FASCICOLO;
            search = search.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));
            search = search.replace(DocerModel.PARAM_REPLACER, titolarioRef.getUuid()); // PARENT
            search = search.replace(DocerModel.ANNO_FASCICOLO_REPLACER, annoFascicolo);
            query.setStatement(search);

            QueryResult queryresult = getAlfrescoRepositoryService_query(query);

            ResultSet rs = queryresult.getResultSet();

            if (rs.getTotalRowCount() > 1)
                throw new DocerException("trovati " + rs.getTotalRowCount() + " risultati per ANNO_FASCICOLO " + annoFascicolo);

            if (rs.getTotalRowCount() == 0)
                return null;

            return createReference(rs.getRows(0).getNode().getId());
        } catch (DocerException e) {
            throw new DocerException(metodo + ": " + e.getMessage());
        }
    }

    private String findGroupId(List<String> authorityIds, String toFindId) {
        for (String authorityId : authorityIds) {
            if (authorityId.equalsIgnoreCase(toFindId)) // in alfresco posso
                // creare 2
                // group_id che differisce solo
                // da
                // un masiuscola
                return authorityId;
        }
        return null;
    }

    private String findUserId(List<String> ids, String toFindId) {
        for (String id : ids)
            if (id.equalsIgnoreCase(toFindId)) // in alfresco non posso creare
                // due utenti con stesso user_id
                // (anche se differiscono solo
                // con le maiuscole/minuscole)
                return id;
        return null;
    }

    private List<Reference> getFolderFromBin(String folderId) throws DocerException {

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
        searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

        String search = LuceneUtility.buildAlfrescoFoldersQueryString(searchCriteria, true);

        Query query = new Query();
        query.setStatement(search);
        QueryResult result = getAlfrescoArchiveRepositoryService_query(query);

        if (result.getResultSet().getTotalRowCount() < 1) {
            return null;
        }

        List<Reference> results = new ArrayList<Reference>();

        for (ResultSetRow rsr : result.getResultSet().getRows()) {
            results.add(createReference(null, rsr.getNode().getId(), DocerModel.ARCHIVE_STORE));
        }

        return results;

    }

    private List<Reference> getDocumentFromBin(String docId) throws DocerException {

        String search = DocerModel.SEARCH_QUERY_BY_DOCNUM;

        search = search.replace(DocerModel.DOCNUM_REPLACER, docId);

        Query query = new Query();
        query.setStatement(search);
        QueryResult result = getAlfrescoArchiveRepositoryService_query(query);

        if (result.getResultSet().getTotalRowCount() < 1) {
            return null;
        }

        List<Reference> results = new ArrayList<Reference>();

        for (ResultSetRow rsr : result.getResultSet().getRows()) {
            results.add(createReference(null, rsr.getNode().getId(), DocerModel.ARCHIVE_STORE));
        }

        return results;

    }

    private String getLastVersionLabel(Reference nodeRef) throws DocerException {

        VersionHistory results = getAlfrescoAuthoringService_getVersionHistory(nodeRef);

        if (results.getVersions() != null) {
            Version ver = results.getVersions(0);
            return ver.getLabel();
        }

        return null;
    }

    private void tryToDeleteIncompleteDocument(Reference documentRef) {

        CMLDelete cmlDelete = new CMLDelete();

        cmlDelete.setWhere(createPredicate(documentRef));

        CML cml = new CML();
        cml.setDelete(new CMLDelete[]{cmlDelete});

        UpdateResult[] updateResult = null;
        try {
            updateResult = getAlfrescoRepositoryService_update(cml);
        } catch (DocerException e) {
            return;
        }

        if (updateResult.length < 1)
            return;

        // CANCELLO DAL CESTINO
        try {
            deleteFromBin(updateResult[0].getSource());
        } catch (DocerException e) {

        }

    }

    private String getContentUrlSuffix(String docId) throws DocerException {

        String metodo = "getContentUrlSuffix";

        String search = DocerModel.SEARCH_QUERY_BY_DOCNUM;

        search = search.replace(DocerModel.DOCNUM_REPLACER, docId);
        search = search.replace(DocerModel.LIBRARY_REPLACER, getLibraryForEnte(currentUser.getCodiceEnte()));

        Query query = new Query();

        query.setStatement(search);
        QueryResult result = getAlfrescoRepositoryService_query(query);// ,
        // true);

        if (result.getResultSet().getTotalRowCount() < 1)
            throw new DocerException(metodo + ": documento " + docId + " non trovato");

        if (result.getResultSet().getTotalRowCount() > 1)
            throw new DocerException(metodo + ": trovati " + result.getResultSet().getTotalRowCount() + " documenti");

        ResultSetRow rsr = result.getResultSet().getRows(0);

        for (NamedValue namedValue : rsr.getColumns()) {
            if (namedValue.getName().equals(Constants.PROP_NAME)) {
                String nodeValue = null;
                try {
                    nodeValue = java.net.URLEncoder.encode(namedValue.getValue(), "UTF-8");
                } catch (Exception e) {
                    throw new DocerException(metodo + ":" + e.getMessage());
                }
                return rsr.getNode().getId() + "/" + nodeValue;
            }
        }

        throw new DocerException(metodo + ": docname not found");

    }

    private String getVersionContentUrlSuffix(String versId) throws DocerException {
        String metodo = "getVersionContentUrlSuffix";
        String rif = versId;

        // recupero proprieta' del documento
        Predicate predicate = createPredicate(createVersionReference(null, versId));

        Node[] nodi = getAlfrescoRepositoryService_get(predicate);

        if (nodi == null || nodi.length == 0) {
            throw new DocerException(metodo + ":" + rif + ": documento non trovato");
        }
        Node nodo = nodi[0];

        for (NamedValue namedValue : nodo.getProperties()) {
            if (namedValue.getName().equals(Constants.PROP_NAME)) {
                return nodo.getReference().getUuid() + "/" + ISO9075.encode(namedValue.getValue());
            }
        }

        throw new DocerException(metodo + ":" + rif + ": docname non trovato");
    }

    private CMLCreateAssociation getAddAdvancedVersionAssoc(Reference firstRef, Reference secondRef) {
        CMLCreateAssociation addObject = new CMLCreateAssociation();
        addObject.setAssociation(DocerModel.ASSOC_ADVANCED_VERSION);
        addObject.setFrom(createPredicate(secondRef));
        addObject.setTo(createPredicate(firstRef));
        return addObject;
    }

    private CMLCreateAssociation getAddRelatedAssoc(Reference firstRef, Reference secondRef) {
        CMLCreateAssociation addObject = new CMLCreateAssociation();
        addObject.setAssociation(DocerModel.ASSOC_RELATED);
        addObject.setFrom(createPredicate(secondRef));
        addObject.setTo(createPredicate(firstRef));
        return addObject;
    }

    private CMLRemoveAssociation getRemoveRelatedAssoc(Reference firstRef, Reference secondRef) {
        CMLRemoveAssociation remObject = new CMLRemoveAssociation();
        remObject.setAssociation(DocerModel.ASSOC_RELATED);

        remObject.setFrom(createPredicate(secondRef));
        remObject.setTo(createPredicate(firstRef));
        return remObject;
    }

    private CMLCreateAssociation getAddRiferimentoAssoc(Reference firstRef, Reference secondRef) {
        CMLCreateAssociation addObject = new CMLCreateAssociation();
        addObject.setAssociation(DocerModel.ASSOC_RIFERIMENTO);
        addObject.setFrom(createPredicate(secondRef));
        addObject.setTo(createPredicate(firstRef));
        return addObject;
    }

    private CMLRemoveAssociation getRemoveRiferimentoAssoc(Reference firstRef, Reference secondRef) {
        CMLRemoveAssociation remObject = new CMLRemoveAssociation();
        remObject.setAssociation(DocerModel.ASSOC_RIFERIMENTO);
        remObject.setFrom(createPredicate(secondRef));
        remObject.setTo(createPredicate(firstRef));
        return remObject;
    }

    private String getCaseSensitiveUserId(String user) throws DocerException {
        if (!USERS_MAP.containsKey(user)) {
            List<String> users = new ArrayList<String>();
            users.add(user);
            List<String> ret = getCaseSensitiveUsersIds(users);
            if (ret.isEmpty()) {
                throw new DocerException("User not found");
            }

            USERS_MAP.put(user, ret.get(0));
        }

        return USERS_MAP.get(user);
    }

    private List<String> getCaseSensitiveUsersIds(List<String> users) throws DocerException {

        if (users == null || users.size() == 0)
            return new ArrayList<String>();

        HashSet<String> lcUsers = new HashSet<String>();
        for (String user : users) {
            lcUsers.add(user.toLowerCase());
        }

        Query query = new Query();

        // Create a query object
        String queryBase = DocerModel.SEARCH_QUERY_USER_ROOT;

        String queryUserIds = "";
        for (String userId : users) {
            queryUserIds += DocerModel.SEARCH_QUERY_BY_USERID.replace(DocerModel.PARAM_REPLACER, LuceneUtility.escapeLuceneQueryValue(userId)) + " ";
        }

        if (!queryUserIds.equals("")) {
            queryUserIds = "+(" + queryUserIds + ")";
        }

        query.setStatement(queryBase + queryUserIds);
        QueryResult queryresult = getAlfrescoRepositoryService_query(query);

        if (queryresult.getResultSet() == null || queryresult.getResultSet().getTotalRowCount() < 1)
            return new ArrayList<String>();

        List<String> usersIds = new ArrayList<String>();
        for (ResultSetRow row : queryresult.getResultSet().getRows()) {
            for (NamedValue property : row.getColumns()) {
                if (property.getName().equals(DocerModel.PROP_USER_USERID)) {

                    if (lcUsers.contains(property.getValue().toLowerCase())) {
                        usersIds.add(property.getValue());
                    }

                    break;
                }
            }
        }

        return usersIds;
    }

    private String getMappedPropertyCaseFromBusinessLogicName(String propName, String propValue) {

        if (propName == null || propValue == null) {
            return propValue;
        }

        MetaProperty mp = METAMAPPING.getMetaPropertyFromBusinessLogicName(propName);

        if (mp != null) {
            if (mp.getIsUppercase()) {
                propValue = propValue.toUpperCase();
            } else if (mp.getIsUppercase()) {
                propValue = propValue.toLowerCase();
            }
        }

        return propValue;
    }

    private Reference getCustomItemReference(ICustomItemId localId, CustomItemProps cProps) throws DocerException {
        String nomeMetodo = "getCustomItemReference";

        if (localId.getCodiceEnte() == null || localId.getCodiceEnte().equals(""))
            throw new DocerException(nomeMetodo + ": COD_ENTE obbligatorio tra gli id_criteria");

        if (localId.getCodiceAOO() == null || localId.getCodiceAOO().equals(""))
            throw new DocerException(nomeMetodo + ": COD_AOO obbligatorio tra gli id_criteria");

        if (localId.getCodiceCustom() == null || localId.getCodiceCustom().equals(""))
            throw new DocerException(nomeMetodo + ": " + cProps.getBlCodicePropName() + " obbligatorio tra gli id_criteria");

        // String codiceCustomName =
        // SIMPLE_META_MAPPING.get(cProps.getBlCodicePropName());
        String codiceCustomName = "";
        MetaProperty mp = METAMAPPING.getMetaPropertyFromBusinessLogicName(cProps.getBlCodicePropName());
        if (mp != null) {
            codiceCustomName = mp.getAlfShortPropName().replaceAll(":", "\\\\:");
        }

        Query query = new Query();

        // Create a query object
        String search = DocerModel.SEARCH_QUERY_CUSTOM_ANAGR;
        search = search.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));

        search = search.replace(DocerModel.COD_ENTE_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.custom_cod_ente, localId.getCodiceEnte()));

        search = search.replace(DocerModel.COD_AOO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.custom_cod_aoo, localId.getCodiceAOO()));

        search = search.replace(DocerModel.CUSTOM_ANAGR_LOCALID_REPLACER, cProps.getAlfType());

        search = search.replace(DocerModel.COD_CUSTOM_NAME_REPLACER, codiceCustomName);

        search = search.replace(DocerModel.COD_CUSTOM_VALUE_REPLACER, getMappedPropertyCaseFromBusinessLogicName(cProps.getBlCodicePropName(), localId.getCodiceCustom()));

        query.setStatement(search);

        QueryResult queryresult = getAlfrescoRepositoryService_query(query);

        if (queryresult.getResultSet().getTotalRowCount() > 1)
            throw new DocerException(nomeMetodo + ": " + cProps.getBlType() + ": trovati " + queryresult.getResultSet().getTotalRowCount() + " risultati");

        if (queryresult.getResultSet().getTotalRowCount() == 0)
            return null;

        return createReference(queryresult.getResultSet().getRows(0).getNode().getId());

    }

    private Predicate createPredicate(Reference reference) {
        Predicate predicate = new Predicate();
        predicate.setNodes(new Reference[]{reference});
        return predicate;
    }

    private Reference createReference(String path, String uuid, Store store) {
        Reference reference = new Reference();
        if (path != null && !path.equals(""))
            reference.setPath(path);
        else
            reference.setUuid(uuid);
        reference.setStore(store);
        return reference;
    }

    private Reference createReference(String uuid) {
        if (uuid == null) {
            return null;
        }

        return createReference(null, uuid, DocerModel.WORKSPACE_STORE);
    }

    private Reference createReference(String path, String uuid) {
        return createReference(path, uuid, DocerModel.WORKSPACE_STORE);
    }

    private Reference createVersionReference(String path, String uuid) {
        return createReference(path, uuid, DocerModel.VERSION_STORE);
    }

    private SearchResultSet getFascicolo(String progressivo, String numeroFascicolo, String annoFascicolo, String classifica, String codiceAOO, String codiceEnte) throws DocerException {

        if (StringUtils.isEmpty(codiceEnte))
            return new SearchResultSet();

        if (StringUtils.isEmpty(codiceAOO))
            return new SearchResultSet();

        if (StringUtils.isEmpty(classifica))
            return new SearchResultSet();

        if (StringUtils.isEmpty(annoFascicolo))
            return new SearchResultSet();

        // se sono entrambi vuoti
        if (StringUtils.isEmpty(progressivo)) {
            if (StringUtils.isEmpty(numeroFascicolo)) {
                return new SearchResultSet();
            }
        }

        String metodo = "getFascicolo";
        String rif = progressivo + " (" + numeroFascicolo + ")@" + annoFascicolo;

        // Create a query object
        String lucenequery = DocerModel.SEARCH_QUERY_FASCICOLO_FROM_BOTH;

        // se e' vuoto il progressivo recupero dal numero
        if (StringUtils.isEmpty(progressivo)) {
            progressivo = "";
            lucenequery = DocerModel.SEARCH_QUERY_FASCICOLO_FROM_NUMERO;
        } else if (StringUtils.isEmpty(numeroFascicolo)) {
            numeroFascicolo = "";
            lucenequery = DocerModel.SEARCH_QUERY_FASCICOLO_FROM_PROGRESSIVO;
        }

        lucenequery = lucenequery.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));
        lucenequery = lucenequery.replace(DocerModel.COD_ENTE_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_cod_ente, codiceEnte));
        lucenequery = lucenequery.replace(DocerModel.COD_AOO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_cod_aoo, codiceAOO));
        lucenequery = lucenequery.replace(DocerModel.CLASSIFICA_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_classifica, classifica));
        lucenequery = lucenequery.replace(DocerModel.ANNO_FASCICOLO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_anno_fascicolo, annoFascicolo));
        lucenequery = lucenequery.replace(DocerModel.PROGR_FASCICOLO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_progr_fascicolo, progressivo));
        lucenequery = lucenequery.replace(DocerModel.NUM_FASCICOLO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_num_fascicolo, numeroFascicolo));

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_cod_ente);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_cod_aoo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_classifica);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_num_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_anno_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_des_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_progr_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_parent_progr_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_enabled);

        SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, returnProperties, -1, null, false, false, false, false, true, true, null);

        if (webScriptResults.getCount() > 1)
            throw new DocerException(metodo + ": " + rif + ": trovati " + webScriptResults.getCount() + " risultati");

        return webScriptResults;

    }

    private SearchResultSet getFascicolo(String runasUser, String progressivo, String numeroFascicolo, String annoFascicolo, String classifica, String codiceAOO, String codiceEnte)
            throws DocerException {

        if (StringUtils.isEmpty(codiceEnte))
            return new SearchResultSet();

        if (StringUtils.isEmpty(codiceAOO))
            return new SearchResultSet();

        if (StringUtils.isEmpty(classifica))
            return new SearchResultSet();

        if (StringUtils.isEmpty(annoFascicolo))
            return new SearchResultSet();

        // se sono entrambi vuoti
        if (StringUtils.isEmpty(progressivo)) {
            if (StringUtils.isEmpty(numeroFascicolo)) {
                return new SearchResultSet();
            }
        }

        String metodo = "getFascicolo";
        String rif = progressivo + " (" + numeroFascicolo + ")@" + annoFascicolo;

        // Create a query object
        String lucenequery = DocerModel.SEARCH_QUERY_FASCICOLO_FROM_BOTH;

        // se e' vuoto il progressivo recupero dal numero
        if (StringUtils.isEmpty(progressivo)) {
            progressivo = "";
            lucenequery = DocerModel.SEARCH_QUERY_FASCICOLO_FROM_NUMERO;
        } else if (StringUtils.isEmpty(numeroFascicolo)) {
            numeroFascicolo = "";
            lucenequery = DocerModel.SEARCH_QUERY_FASCICOLO_FROM_PROGRESSIVO;
        }

        lucenequery = lucenequery.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));
        lucenequery = lucenequery.replace(DocerModel.COD_ENTE_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_cod_ente, codiceEnte));
        lucenequery = lucenequery.replace(DocerModel.COD_AOO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_cod_aoo, codiceAOO));
        lucenequery = lucenequery.replace(DocerModel.CLASSIFICA_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_classifica, classifica));
        lucenequery = lucenequery.replace(DocerModel.ANNO_FASCICOLO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_anno_fascicolo, annoFascicolo));
        lucenequery = lucenequery.replace(DocerModel.PROGR_FASCICOLO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_progr_fascicolo, progressivo));
        lucenequery = lucenequery.replace(DocerModel.NUM_FASCICOLO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_num_fascicolo, numeroFascicolo));

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_cod_ente);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_cod_aoo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_classifica);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_num_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_anno_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_des_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_progr_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_parent_progr_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_enabled);

        SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, returnProperties, -1, runasUser, false, false, false, false, true, true, null);

        if (webScriptResults.getCount() > 1)
            throw new DocerException(metodo + ": " + rif + ": trovati " + webScriptResults.getCount() + " risultati");

        return webScriptResults;

    }

    private Map<String, EnumACLRights> getACLFascicoloAsAdmin(String progressivo, String annoFascicolo, String classifica, String codiceAOO, String codiceEnte) throws DocerException {

        Map<String, EnumACLRights> aces = new HashMap<String, EnumACLRights>();

        if (StringUtils.isEmpty(codiceEnte))
            return aces;

        if (StringUtils.isEmpty(codiceAOO))
            return aces;

        if (StringUtils.isEmpty(classifica))
            return aces;

        if (StringUtils.isEmpty(annoFascicolo))
            return aces;

        // se sono entrambi vuoti
        if (StringUtils.isEmpty(progressivo)) {
            return aces;
        }

        String metodo = "getACLFascicoloAsAdmin";
        String rif = classifica + "/" + annoFascicolo + "/" + progressivo;

        // Create a query object
        String lucenequery = DocerModel.SEARCH_QUERY_FASCICOLO_FROM_PROGRESSIVO;

        lucenequery = lucenequery.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));
        lucenequery = lucenequery.replace(DocerModel.COD_ENTE_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_cod_ente, codiceEnte));
        lucenequery = lucenequery.replace(DocerModel.COD_AOO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_cod_aoo, codiceAOO));
        lucenequery = lucenequery.replace(DocerModel.CLASSIFICA_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_classifica, classifica));
        lucenequery = lucenequery.replace(DocerModel.ANNO_FASCICOLO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_anno_fascicolo, annoFascicolo));
        lucenequery = lucenequery.replace(DocerModel.PROGR_FASCICOLO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.fascicolo_progr_fascicolo, progressivo));

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_cod_ente);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_cod_aoo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_classifica);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_num_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_anno_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_des_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_progr_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_parent_progr_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.fascicolo_enabled);

        SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, returnProperties, -1, ADMIN_USERID, false, false, false, false, true, true, null);

        if (webScriptResults.getCount() > 1)
            throw new DocerException(metodo + ": " + rif + ": trovati " + webScriptResults.getCount() + " risultati");

        if (webScriptResults.getCount() == 0)
            throw new DocerException(metodo + ": " + rif + ": non trovato");

        return webScriptResults.getFirstAndOnly().getAcl();

    }

    private List<String> getCaseSensitiveGroupsIds(List<String> groups) throws DocerException {
        if (groups == null || groups.size() == 0)
            return new ArrayList<String>();

        HashSet<String> lcGroups = new HashSet<String>();
        for (String group : groups) {
            lcGroups.add(group.toLowerCase());
        }

        Query query = new Query();

        // Create a query object
        String baseQuery = DocerModel.SEARCH_QUERY_GROUP_ROOT;

        String orGroupIdQuery = "";

        for (String groupId : groups) {
            orGroupIdQuery += DocerModel.SEARCH_QUERY_BY_GROUPID.replace(DocerModel.PARAM_REPLACER, LuceneUtility.escapeLuceneQueryValue(groupId)) + " ";
        }

        if (!orGroupIdQuery.equals("")) {

            orGroupIdQuery = " +(" + orGroupIdQuery + ")";
        }

        query.setStatement(baseQuery + orGroupIdQuery);

        QueryResult queryresult = getAlfrescoRepositoryService_query(query);

        if (queryresult.getResultSet() == null || queryresult.getResultSet().getTotalRowCount() < 1)
            return new ArrayList<String>();

        List<String> groupsIds = new ArrayList<String>();
        for (ResultSetRow row : queryresult.getResultSet().getRows()) {

            String id = readGroupId(row);

            if (lcGroups.contains(id.toLowerCase())) {
                groupsIds.add(id);
            }

        }

        return groupsIds;
    }

    private String readGroupId(ResultSetRow rsr) {
        for (NamedValue nv : rsr.getColumns()) {

            if (nv.getName().equals(DocerModel.PROP_GROUP_GROUPID) && nv.getValue() != null) {
                return nv.getValue().replaceAll("^" + Constants.GROUP_PREFIX, "");
            }
        }

        return "";
    }

    private Reference getCustomItemRoot(String customType, String aooUUID) throws DocerException {
        String nomeMetodo = "getCustomItemRoot";
        String rif = "";

        if (aooUUID == null || aooUUID.equals(""))
            throw new DocerException(nomeMetodo + ": aooUUID obbligatorio");

        if (customType == null || customType.equals(""))
            throw new DocerException(nomeMetodo + ": customType obbligatorio tra gli IDs");

        Query query = new Query();

        // Create a query object
        String search = DocerModel.SEARCH_QUERY_CUSTOM_ANAGR_ROOT;
        search = search.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));
        search = search.replace(DocerModel.PARAM_REPLACER, aooUUID);

        search = search.replace(DocerModel.CUSTOM_ANAGR_LOCALID_REPLACER, customType.toUpperCase());
        query.setStatement(search);

        QueryResult queryresult = getAlfrescoRepositoryService_query(query);

        ResultSet rs = queryresult.getResultSet();

        if (rs.getTotalRowCount() > 1)
            throw new DocerException(nomeMetodo + ": " + rif + ": trovati " + rs.getTotalRowCount() + " risultati");

        if (rs.getTotalRowCount() == 0)
            return null;

        return createReference(null, queryresult.getResultSet().getRows(0).getNode().getId());
    }

    private void updateAlfrescoSpace(String spaceType, String alfNodeName, Reference spaceRef, Map<String, String> props) throws DocerException {

        // proprieta' del nodo ente da creare
        List<NamedValue> properties = new ArrayList<NamedValue>();

        populateAlfrescoProperties(props, properties);

        if (properties.size() == 0)
            return;

        CML cml = new CML();

        Predicate predicate = createPredicate(spaceRef);

        if (!StringUtils.isEmpty(alfNodeName)) {
            NamedValue nameProperty = new NamedValue();
            nameProperty.setName(Constants.PROP_NAME);
            nameProperty.setValue(alfNodeName);
            properties.add(nameProperty);
        }

        // CML update
        CMLUpdate update = new CMLUpdate();
        update.setWhere(predicate);
        update.setProperty(properties.toArray(new NamedValue[0]));

        cml.setUpdate(new CMLUpdate[]{update});
        // UpdateResult[] result = null;

        if (!StringUtils.isEmpty(alfNodeName)) {
            Reference parentReference = getParentReference(spaceRef);

            ParentReference parentParentReference = createParentReference(null, parentReference.getUuid(), Constants.ASSOC_CONTAINS,
                    Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, alfNodeName));

            CMLMove move = new CMLMove();
            move.setChildName(Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL, alfNodeName));
            move.setWhere(predicate);
            move.setTo(parentParentReference);
            cml.setMove(new CMLMove[]{move});
        }

        getAlfrescoRepositoryService_update(cml);

    }

    private void updateAlfrescoFolder(boolean isPublic, Reference nodeRef, Map<String, String> props) throws DocerException {

        CML cml = new CML();

        // proprieta' del nodo ente da creare
        List<NamedValue> properties = new ArrayList<NamedValue>();

        populateAlfrescoProperties(props, properties);

        Predicate predicate = createPredicate(nodeRef);

        if (props != null) {

            String folderName = props.get(it.kdm.docer.sdk.Constants.folder_folder_name);

            if (folderName != null) {

                NamedValue propName = new NamedValue();
                propName.setName(Constants.PROP_NAME);

                String nodeName = adjustNodeName(folderName);

                propName.setValue(nodeName);

                properties.add(propName);

                Reference parentReference = getParentReference(nodeRef);

                ParentReference parentParentReference = createParentReference(null, parentReference.getUuid(), Constants.ASSOC_CONTAINS,
                        Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, nodeName));

                CMLMove move = new CMLMove();
                move.setChildName(Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL, nodeName));
                move.setWhere(predicate);
                move.setTo(parentParentReference);
                cml.setMove(new CMLMove[]{move});

            }
        }

        if (properties.size() == 0)
            return;

        // CML update
        CMLUpdate update = new CMLUpdate();
        update.setWhere(predicate);
        update.setProperty(properties.toArray(new NamedValue[0]));

        cml.setUpdate(new CMLUpdate[]{update});
        // UpdateResult[] result = null;

        getAlfrescoRepositoryService_update(cml);

    }

    private SearchResultSet getTitolario(String classifica, String codiceTitolario, String codiceAOO, String codiceEnte) throws DocerException {

        if (StringUtils.isEmpty(codiceEnte))
            return new SearchResultSet();

        if (StringUtils.isEmpty(codiceAOO))
            return new SearchResultSet();

        if (StringUtils.isEmpty(classifica)) {
            if (StringUtils.isEmpty(codiceTitolario)) {
                return new SearchResultSet();
            }
        }
        String nomeMetodo = "_getTitolario";
        String rif = classifica + "(" + codiceTitolario + ")";

        // Create a query object
        String lucenequery = DocerModel.SEARCH_QUERY_TITOLARIO_FROM_BOTH;

        if (StringUtils.isEmpty(codiceTitolario)) {
            codiceTitolario = "";
            lucenequery = DocerModel.SEARCH_QUERY_TITOLARIO_FROM_CLASSIFICA;
        } else if (StringUtils.isEmpty(classifica)) {
            classifica = "";
            lucenequery = DocerModel.SEARCH_QUERY_TITOLARIO_FROM_CODICE;
        }

        lucenequery = lucenequery.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));
        lucenequery = lucenequery.replace(DocerModel.COD_ENTE_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_cod_ente, codiceEnte));
        lucenequery = lucenequery.replace(DocerModel.COD_AOO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_cod_aoo, codiceAOO));
        lucenequery = lucenequery.replace(DocerModel.COD_TITOLARIO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_cod_titolario, codiceTitolario));
        lucenequery = lucenequery.replace(DocerModel.CLASSIFICA_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.titolario_classifica, classifica));

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(it.kdm.docer.sdk.Constants.titolario_cod_ente);
        returnProperties.add(it.kdm.docer.sdk.Constants.titolario_cod_aoo);
        returnProperties.add(it.kdm.docer.sdk.Constants.titolario_classifica);
        returnProperties.add(it.kdm.docer.sdk.Constants.titolario_cod_titolario);
        returnProperties.add(it.kdm.docer.sdk.Constants.titolario_des_titolario);
        returnProperties.add(it.kdm.docer.sdk.Constants.titolario_parent_classifica);
        returnProperties.add(it.kdm.docer.sdk.Constants.titolario_enabled);

        SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, returnProperties, -1, null, false, false, false, false, true, true, null);

        if (webScriptResults.getCount() > 1)
            throw new DocerException(nomeMetodo + ": " + rif + ": trovati " + webScriptResults.getCount() + " risultati");

        return webScriptResults;
    }

    private static String convertToAlfrescoRights(EnumACLRights value) {

        if (value.equals(EnumACLRights.viewProfile) || value.equals("ViewProfileAccess"))
            return "ViewProfileAccess";
        if (value.equals(EnumACLRights.readOnly) || value.equals("ReadOnlyAccess"))
            return "ReadOnlyAccess";
        if (value.equals(EnumACLRights.normalAccess) || value.equals("NormalAccess"))
            return "NormalAccess";
        if (value.equals(EnumACLRights.fullAccess) || value.equals("FullAccess"))
            return "FullAccess";
        return null;
    }

    private static EnumACLRights convertToDocareaRights(String alfrescoRight) {

        if (alfrescoRight.equals("ViewProfileAccess") || alfrescoRight.equals(EnumACLRights.viewProfile))
            return EnumACLRights.viewProfile;

        if (alfrescoRight.equals("ReadOnlyAccess") || alfrescoRight.equals(EnumACLRights.readOnly))
            return EnumACLRights.readOnly;

        if (alfrescoRight.equals("NormalAccess") || alfrescoRight.equals(EnumACLRights.normalAccess))
            return EnumACLRights.normalAccess;

        if (alfrescoRight.equals("FullAccess") || alfrescoRight.equals(EnumACLRights.fullAccess))
            return EnumACLRights.fullAccess;

        return EnumACLRights.undefined;
    }

    private void deleteUser(String userId) throws DocerException {
        String nomeMetodo = "deleteUser";
        String rif = userId;

        try {

            WebServiceFactory.getAdministrationService().deleteUsers((new String[]{userId.toLowerCase()}));
        } catch (AdministrationFault e) {
            throw new DocerException(nomeMetodo + ": " + rif + ": " + e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
        return;
    }

    private SearchResultSet _getAoo(String codiceAOO, String codiceEnte) throws DocerException {

        if (codiceEnte == null || codiceEnte.equals(""))
            new SearchResultSet();

        if (codiceAOO == null || codiceAOO.equals(""))
            new SearchResultSet();

        // Create a query object
        String search = DocerModel.SEARCH_QUERY_AOO;
        search = search.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));
        search = search.replace(DocerModel.COD_ENTE_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.aoo_cod_ente, codiceEnte));
        search = search.replace(DocerModel.COD_AOO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.aoo_cod_aoo, codiceAOO));

        return webScriptSearch(false, search, null, -1, null, false, false, false, false, false, true, null);

    }

    private Reference getAooReference(String codiceAOO, String codiceEnte) throws DocerException {

        if (codiceEnte == null || codiceEnte.equals(""))
            return null;

        if (codiceAOO == null || codiceAOO.equals(""))
            return null;

        String nomeMetodo = "getAooReference";
        String rif = codiceAOO;

        Query query = new Query();

        // Create a query object
        String search = DocerModel.SEARCH_QUERY_AOO;
        search = search.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));
        search = search.replace(DocerModel.COD_ENTE_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.aoo_cod_ente, codiceEnte));
        search = search.replace(DocerModel.COD_AOO_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.aoo_cod_aoo, codiceAOO));
        query.setStatement(search);

        QueryResult queryresult = getAlfrescoRepositoryService_query(query);

        if (queryresult.getResultSet() == null || queryresult.getResultSet().getTotalRowCount() == 0)
            return null;

        if (queryresult.getResultSet().getTotalRowCount() > 1)
            throw new DocerException(nomeMetodo + ": " + rif + ": trovati " + queryresult.getResultSet().getTotalRowCount() + " risultati");

        String uuid = queryresult.getResultSet().getRows(0).getNode().getId();

        Reference nodeRef = createReference(uuid);
        addToAooNodeCache(codiceAOO, codiceAOO, nodeRef);
        return nodeRef;
    }

    private SearchResultSet _getEnte(String codiceEnte) throws DocerException {

        if (codiceEnte == null || codiceEnte.equals(""))
            return new SearchResultSet();

        // Create a query object
        String search = DocerModel.SEARCH_QUERY_ENTE;
        search = search.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));
        search = search.replace(DocerModel.COD_ENTE_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.ente_cod_ente, codiceEnte));

        return webScriptSearch(false, search, null, -1, null, false, false, false, false, false, true, null);

    }

    private Reference getEnteReference(String codiceEnte) throws DocerException {

        if (codiceEnte == null || codiceEnte.equals(""))
            return null;

        String nomeMetodo = "getEnteReference";
        String rif = codiceEnte;

        Query query = new Query();

        // Create a query object
        String search = DocerModel.SEARCH_QUERY_ENTE;
        search = search.replace(DocerModel.LIBRARY_REPLACER, ISO9075.encode(getLibraryForEnte(currentUser.getCodiceEnte())));
        search = search.replace(DocerModel.COD_ENTE_REPLACER, getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.ente_cod_ente, codiceEnte));
        query.setStatement(search);

        QueryResult queryresult = getAlfrescoRepositoryService_query(query);

        if (queryresult.getResultSet() == null || queryresult.getResultSet().getTotalRowCount() == 0)
            return null;

        if (queryresult.getResultSet().getTotalRowCount() > 1)
            throw new DocerException(nomeMetodo + ":" + rif + ": trovati " + queryresult.getResultSet().getTotalRowCount() + " risultati");

        String uuid = queryresult.getResultSet().getRows(0).getNode().getId();

        Reference nodeRef = createReference(uuid);
        addToEnteNodeCache(codiceEnte, nodeRef);

        return nodeRef;
    }

    private Reference createFolderToParentUUID(String folderType, String folderName, String parentUUID, String iconName, Map<String, String> props) throws DocerException {
        return createNodeFolder(folderType, folderName, parentUUID, null, iconName, props);
    }

    // TODO: implementare funzione
    private String adjustNodeName(String nodeName) {

        if (nodeName == null)
            return "";
        // nodeName = nodeName.replaceAll(REGEX_NODENAME_1,
        // REPLACEWITH_NODENAME);
        // nodeName = nodeName.replaceAll(REGEX_NODENAME_2, "");
        // nodeName = nodeName.replaceAll(REGEX_NODENAME_3, "");

        return nodeName;
    }

    private Reference createNodeFolder(String folderType, String folderName, String parentUUID, String parentPath, String iconName, Map<String, String> props) throws DocerException {

        String nomeMetodo = "create" + folderType.replaceAll("^\\{[^\\{\\}]*\\}", "");

        folderName = encode(folderName);

        String rif = folderName;

        if (parentUUID == null || parentUUID.equals(""))
            if (parentPath == null || parentPath.equals(""))
                throw new DocerException(nomeMetodo + ": " + rif + "parentUUID e parentPath null");

        // nodo parent
        ParentReference parent = createParentReference(parentPath, parentUUID, Constants.ASSOC_CONTAINS, Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, folderName));

        // lista delle proprieta' da inserire
        List<NamedValue> properties = new ArrayList<NamedValue>();

        NamedValue prop1 = new NamedValue();
        prop1.setName(Constants.PROP_NAME);
        prop1.setValue(adjustNodeName(folderName));

        properties.add(prop1);

        NamedValue prop3 = new NamedValue();
        prop3.setName(DocerModel.PROP_ICON);
        prop3.setValue(iconName);

        properties.add(prop3);

        // props e' la lista delle proprieta' da aggiungere
        // (cod_ente,cod_aoo...) sono campi che saranno utilizzati nelle
        // ricerche
        populateAlfrescoProperties(props, properties);

        CMLCreate create = new CMLCreate();
        create.setParent(parent);
        create.setType(folderType);
        create.setProperty(properties.toArray(new NamedValue[0]));

        CML cml = new CML();
        cml.setCreate(new CMLCreate[]{create});
        UpdateResult[] result = getAlfrescoRepositoryService_update(cml);
        return result[0].getDestination();

    }

    private CMLCreate getCMLCreateFolder(String folderType, String folderName, String parentUUID, String parentPath, String iconName) throws DocerException {

        String nomeMetodo = "getCMLCreateFolder " + folderType;
        String rif = folderName;

        if (parentUUID == null || parentUUID.equals(""))
            if (parentPath == null || parentPath.equals(""))
                throw new DocerException(nomeMetodo + ": " + rif + "parentUUID e parentPath null");

        // nodo parent
        ParentReference parent = createParentReference(parentPath, parentUUID, Constants.ASSOC_CONTAINS, Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, folderName));

        // lista delle proprieta' da inserire
        List<NamedValue> properties = new ArrayList<NamedValue>();

        NamedValue prop1 = new NamedValue();
        prop1.setName(Constants.PROP_NAME);
        prop1.setValue(adjustNodeName(folderName));

        properties.add(prop1);

        NamedValue prop3 = new NamedValue();
        prop3.setName(DocerModel.PROP_ICON);
        prop3.setValue(iconName);

        properties.add(prop3);

        CMLCreate create = new CMLCreate();
        create.setParent(parent);
        create.setType(folderType);
        create.setProperty(properties.toArray(new NamedValue[0]));

        return create;

    }

    private Reference createAnnoFascicolo(String annoFascicolo, Reference titolarioRef) throws DocerException {

        Reference refAnno = createFolderToParentUUID(DocerModel.TYPE_ANNOFASCICOLO, annoFascicolo, titolarioRef.getUuid(), DocerModel.ICON_PREFIX + DocerModel.ANNO_FASCICOLO_LOCAL_ID, null);

        return refAnno;
    }

    private void addToEnteNodeCache(String codEnte, Reference nodeRef) {
        ENTE_NODE_CACHE.put(codEnte.toUpperCase(), nodeRef);
    }

    private void addToAooNodeCache(String codAoo, String codEnte, Reference nodeRef) {
        AOO_NODE_CACHE.put(codEnte.toUpperCase() + "/" + codAoo.toUpperCase(), nodeRef);
    }

    private Reference getEnteNodeFromCache(String codEnte) {
        if (codEnte == null || codEnte.equals("")) {
            return null;
        }
        return ENTE_NODE_CACHE.get(codEnte.toUpperCase());
    }

    private Reference getAooReferenceFromCache(String codAoo, String codEnte) {
        if (codEnte == null || codEnte.equals("") || codAoo == null || codAoo.equals("")) {
            return null;
        }
        return AOO_NODE_CACHE.get(codEnte.toUpperCase() + "/" + codAoo.toUpperCase());
    }

    // ricerca i children di un nodo in base al nome (possono essere + di uno
    // perche' cm:name e' tokenizzato
    private SearchResultSet getNodeChildrenByName(Reference parentNodeRef, String nodeName) throws DocerException {

        String luceneQuery = "+PARENT:\"workspace://SpacesStore/" + parentNodeRef.getUuid() + "\" +@cm\\:name:\"" + nodeName + "\"";

        return getAlfrescoSpace(luceneQuery);
    }

    private Reference getRepositoryFolderReference(String codiceEnte, String codiceAOO, DateTime dataCreazione) throws DocerException {

        String nomeMetodo = "getRepositoryFolderReference";

        if (codiceAOO == null)
            codiceAOO = "";

        try {

            Reference rootRef = null;
            String pathRoot = "";

            // se e' specificato il codiceAoo allora devo creare nel repository
            // della AOO
            if (codiceAOO != "") {
                Reference aooNodeRef = getAooReferenceFromCache(codiceAOO, codiceEnte);
                if (aooNodeRef == null) {
                    aooNodeRef = getAooReference(codiceAOO, codiceEnte);
                }

                if (aooNodeRef == null) {
                    throw new DocerException("AOO " + codiceAOO + " dell'Ente " + codiceEnte + " non trovata");
                }

                rootRef = aooNodeRef;
                pathRoot = codiceEnte.toUpperCase() + "/" + codiceAOO.toUpperCase();
            } else {
                Reference enteNodeRef = getEnteNodeFromCache(codiceEnte);
                if (enteNodeRef == null) {
                    enteNodeRef = getEnteReference(codiceEnte);
                }

                if (enteNodeRef == null) {
                    throw new DocerException("Ente " + codiceEnte + " non trovato");
                }

                rootRef = enteNodeRef;
                pathRoot = codiceEnte.toUpperCase();
            }

            SearchResultSet webScriptResultDocumenti = getNodeChildrenByName(rootRef, DocerModel.DOCUMENTI_SPACE_NAME);

            if (webScriptResultDocumenti.getCount() == 0) {
                throw new DocerException("Folder repository non trovata oppure l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            Reference repoDocumentiRef = webScriptResultDocumenti.getFirstAndOnly().getReference();

            String pathDocumenti = pathRoot + "/" + DocerModel.DOCUMENTI_SPACE_NAME;

            EnumACLRights effectiveRightsSpaceDocumenti = webScriptResultDocumenti.getFirstAndOnly().getEffectiveRights();
            if (!effectiveRightsSpaceDocumenti.equals(EnumACLRights.normalAccess) && !effectiveRightsSpaceDocumenti.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            String anno = Integer.toString(dataCreazione.getYear());
            String mese = Integer.toString(dataCreazione.getMonthOfYear());
            String giorno = Integer.toString(dataCreazione.getDayOfMonth());

            String repoPathGiorno = pathDocumenti + "/" + anno + "/" + mese + "/" + giorno;
            String repoPathMese = pathDocumenti + "/" + anno + "/" + mese;
            String repoPathAnno = pathDocumenti + "/" + anno;

            Reference repoGiornoRef = REPO_CACHE.get(repoPathGiorno);
            Reference repoMeseRef = REPO_CACHE.get(repoPathMese);
            Reference repoAnnoRef = REPO_CACHE.get(repoPathAnno);

            if (repoGiornoRef == null) {

                if (repoMeseRef == null) {

                    if (repoAnnoRef == null) {
                        SearchResultSet webScriptResultAnno = getNodeChildrenByName(repoDocumentiRef, anno);

                        if (webScriptResultAnno.getCount() == 0) {
                            repoAnnoRef = createFolderToParentUUID(Constants.TYPE_FOLDER, anno, repoDocumentiRef.getUuid(), DocerModel.ICON_DEFAULT, null);
                            REPO_CACHE.put(repoPathAnno, repoAnnoRef);
                            // impongo l'owner ADMIN altrimenti chi crea la
                            // cartella puo'
                            // cancellare tutto anche se non ha diritti sul
                            // contenuto
                            setOwnerRunasAdmin(repoAnnoRef, ADMIN_USERID);
                        } else {
                            repoAnnoRef = webScriptResultAnno.getFirstAndOnly().getReference();
                        }

                    }

                    SearchResultSet webScriptResultMese = getNodeChildrenByName(repoAnnoRef, mese);

                    if (webScriptResultMese.getCount() == 0) {
                        repoMeseRef = createFolderToParentUUID(Constants.TYPE_FOLDER, mese, repoAnnoRef.getUuid(), DocerModel.ICON_DEFAULT, null);

                        REPO_CACHE.put(repoPathMese, repoMeseRef);
                        // impongo l'owner ADMIN altrimenti chi crea la cartella
                        // puo'
                        // cancellare tutto anche se non ha diritti sul
                        // contenuto
                        setOwnerRunasAdmin(repoMeseRef, ADMIN_USERID);
                    } else {
                        repoMeseRef = webScriptResultMese.getFirstAndOnly().getReference();
                    }

                }

                SearchResultSet webScriptResultGiorno = getNodeChildrenByName(repoMeseRef, giorno);

                if (webScriptResultGiorno.getCount() == 0) {
                    repoGiornoRef = createFolderToParentUUID(Constants.TYPE_FOLDER, giorno, repoMeseRef.getUuid(), DocerModel.ICON_DEFAULT, null);

                    REPO_CACHE.put(repoPathGiorno, repoGiornoRef);
                    // impongo l'owner ADMIN altrimenti chi crea la cartella
                    // puo'
                    // cancellare tutto anche se non ha diritti sul contenuto
                    setOwnerRunasAdmin(repoGiornoRef, ADMIN_USERID);
                } else {
                    repoGiornoRef = webScriptResultGiorno.getFirstAndOnly().getReference();
                }
            }

            return repoGiornoRef;

        } catch (DocerException ex) {
            throw new DocerException(nomeMetodo + ": " + ex.getMessage());
        }
    }

    private Reference getRepositoryFolderReference(String codiceEnte, String codiceAOO, String docnum) throws DocerException {

        String nomeMetodo = "getRepositoryFolderReference";

        if (codiceAOO == null)
            codiceAOO = "";

        try {

            Reference rootRef = null;
            String pathRoot = "";

            // se e' specificato il codiceAoo allora devo creare nel repository
            // della AOO
            Reference aooNodeRef = getAooReferenceFromCache(codiceAOO, codiceEnte);
            if (aooNodeRef == null) {
                aooNodeRef = getAooReference(codiceAOO, codiceEnte);
            }

            if (aooNodeRef == null) {
                throw new DocerException("AOO " + codiceAOO + " dell'Ente " + codiceEnte + " non trovata");
            }

            rootRef = aooNodeRef;
            pathRoot = codiceEnte.toUpperCase() + "/" + codiceAOO.toUpperCase();

            SearchResultSet webScriptResultDocumenti = getNodeChildrenByName(rootRef, DOCUMENTI_REPO);

            if (webScriptResultDocumenti.getCount() == 0) {
                throw new DocerException("Folder repository non trovata oppure l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            Reference repoDocumentiRef = webScriptResultDocumenti.getFirstAndOnly().getReference();

            EnumACLRights effectiveRightsSpaceDocumenti = webScriptResultDocumenti.getFirstAndOnly().getEffectiveRights();
            if (!effectiveRightsSpaceDocumenti.equals(EnumACLRights.normalAccess) && !effectiveRightsSpaceDocumenti.equals(EnumACLRights.fullAccess)) {
                throw new DocerException("l'utente corrente non possiede diritti sufficienti per eseguire l'operazione");
            }

            String repoPathDocnum = pathRoot + '/' + docnum;
            String repoId = String.valueOf(getNodeDbId(repoDocumentiRef));

            Reference repoDocnumRef = createFolderToParentUUID(Constants.TYPE_FOLDER, docnum, repoDocumentiRef.getUuid(), DocerModel.ICON_DEFAULT, null);
            makeFolder(docnum, codiceEnte, codiceAOO, repoDocnumRef, repoId);
            REPO_CACHE.put(repoPathDocnum, repoDocnumRef);
            // impongo l'owner ADMIN altrimenti chi crea la cartella
            // puo'
            // cancellare tutto anche se non ha diritti sul contenuto
            setOwnerRunasAdmin(repoDocnumRef, ADMIN_USERID);

            return repoDocnumRef;

        } catch (DocerException ex) {
            throw new DocerException(nomeMetodo + ": " + ex.getMessage());
        }
    }

    private long getNodeDbId(Reference nodeRef) throws DocerException {

        String nomeMetodo = "getNodeDbId";
        String rif = nodeRef.getUuid();

        Predicate predicate = createPredicate(nodeRef);

        Node[] risultati = null;
        try {
            risultati = getAlfrescoRepositoryService_get(predicate);
        } catch (DocerException e) {
            throw new DocerException(nomeMetodo + ": " + rif + ": " + e.getMessage());
        }
        Node nodo = risultati[0];

        for (NamedValue namedValue : nodo.getProperties()) {
            if (namedValue.getName().equals(DocerModel.PROP_NODEDBID))
                return Long.parseLong(namedValue.getValue());
        }

        throw new DocerException(nomeMetodo + ":" + rif + ": db-id error");
    }

    private Reference getParentReference(Reference nodeRef) throws DocerException {

        try {
            String parentUuid = queryParentRunasAdmin(nodeRef.getUuid());
            return createReference(parentUuid);
        } catch (DocerException e) {
            throw new DocerException("getParentReference: " + e.getMessage());
        }

    }

    private void deleteNodes(String[] uuids) {
        // cancello i nodi

        if (uuids == null || uuids.length < 1)
            return;

        CMLDelete[] cmlDel = new CMLDelete[uuids.length];

        for (int pos = 0; pos < uuids.length; pos++) {
            CMLDelete cmlDelete = new CMLDelete();
            cmlDelete.setWhere(createPredicate(createReference(null, uuids[pos])));
            cmlDel[pos] = cmlDelete;
        }

        CML cml = new CML();
        cml.setDelete(cmlDel);
        try {
            getAlfrescoRepositoryService_update(cml);
        } catch (DocerException e) {
            // String mess = e.getMessage();
        }
    }

    protected void deleteNode(String uuid) {
        // cancello i nodi

        if (uuid == null)
            return;

        CMLDelete[] cmlDel = new CMLDelete[1];

        Reference ref = createReference(null, uuid);
        CMLDelete cmlDelete = new CMLDelete();
        cmlDelete.setWhere(createPredicate(ref));
        cmlDel[0] = cmlDelete;

        CML cml = new CML();
        cml.setDelete(cmlDel);
        try {
            getAlfrescoRepositoryService_update(cml);
            deleteFromBin(ref);
        } catch (DocerException e) {
            System.out.println(e.getMessage());
        }

    }

    private String createGroupInAlfresco(IGroupProfileInfo groupProfileInfo) throws DocerException {

        String nomeMetodo = "createGroupInAlfresco";

        String groupId = groupProfileInfo.getGroupId();
        String parentGroupId = groupProfileInfo.getParentGroupId();
        String groupName = groupProfileInfo.getGroupName();
        String gruppoStruttura = groupProfileInfo.getExtraInfo().get("GRUPPO_STRUTTURA");

        // verifica id del gruppo da creare
        if (groupId == null || groupId.equals(""))
            throw new DocerException(nomeMetodo + ": specificare Id Gruppo");

        String rif = groupId;

        if (parentGroupId == null)
            parentGroupId = "";

        // proibiamo la creazione di Sottogruppi dei Gruppi Amministratori
        // Alfresco
        if (parentGroupId.equalsIgnoreCase(ALFRESCO_ADMINISTRATORS)) {
            throw new DocerException(nomeMetodo + ": " + rif + ": e' inibita la creazione di sottogruppi di ALFRESCO_ADMINISTRATORS");
        }

        if (parentGroupId.toUpperCase().startsWith(ENTE_ADMINS_GROUPID_PREFIX.toUpperCase())) {
            throw new DocerException(nomeMetodo + ": " + rif + ": e' inibita la creazione di sottogruppi di Amministratori di Ente");
        }

        if (parentGroupId.toUpperCase().startsWith(AOO_ADMINS_GROUPID_PREFIX.toUpperCase())) {
            throw new DocerException(nomeMetodo + ": " + rif + ": e' inibita la creazione di sottogruppi di Amministratori di AOO");
        }

        String alfGroupId = getCaseSensitiveGroupId(groupId);

        if (alfGroupId != null)
            throw new DocerException(nomeMetodo + ": " + rif + ": Gruppo esistente");

        if (!parentGroupId.equals("")) {
            parentGroupId = getCaseSensitiveGroupId(parentGroupId);
            if (parentGroupId == null)
                throw new DocerException(nomeMetodo + ": " + rif + ": Gruppo Parent " + groupProfileInfo.getParentGroupId() + " non trovato");
        }

        NewAuthority newAuthority = new NewAuthority();
        newAuthority.setAuthorityType("GROUP");
        newAuthority.setName(groupId);

        String[] res = null;
        try {
            res = getAlfrescoAccessControlService_createAuthorities(null, new NewAuthority[]{newAuthority});
        } catch (DocerException e) {
            throw new DocerException(nomeMetodo + ":" + rif + ": " + e.getMessage());
        }

        if (res == null || res.length == 0)
            throw new DocerException(nomeMetodo + ":" + rif + ": errore creazione gruppo");

        // String groupUUID = getGroupUUID(groupId);
        // if (groupUUID == null)
        // throw new DocerException(nomeMetodo + ":" + rif + ": " +
        // "getGroupUUID : gruppo non trovato");

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
        searchCriteria.put(it.kdm.docer.sdk.Constants.group_group_id, Arrays.asList(groupProfileInfo.getGroupId()));
        String lucenequery = LuceneUtility.buildAlfrescoGroupsSearchQueryString(searchCriteria);

        SearchResultSet webScriptResults = webScriptGroupsSearch(false, lucenequery, -1, ADMIN_USERID, null);

        if (webScriptResults.getCount() == 0) {
            throw new DocerException(nomeMetodo + ":" + rif + ": gruppo non trovato dopo creazione");
        }

        completeGroupProfile(webScriptResults.getFirstAndOnly().getReference().getUuid(), groupProfileInfo);

        return res[0];
    }

    private ParentReference createParentReference(String path, String uuid, String associationType, String childName) {
        return createParentReference(path, uuid, associationType, childName, DocerModel.WORKSPACE_STORE);
    }

    private ParentReference createParentReference(String path, String uuid, String associationType, String childName, Store store) {
        ParentReference reference = new ParentReference();
        if (path != null && !path.equals("")) {
            reference.setPath(path);
        } else {
            reference.setUuid(uuid);
        }

        reference.setStore(store);
        if (associationType != null && !associationType.equals("")) {
            reference.setAssociationType(associationType);
        }

        if (childName != null && !childName.equals("")) {
            reference.setChildName(childName);
        }

        return reference;
    }

    private String getCaseSensitiveGroupId(String groupId) throws DocerException {

        List<String> gids = new ArrayList<String>();
        gids.add(groupId);
        gids = getCaseSensitiveGroupsIds(gids);

        if (gids == null)
            return null;
        if (gids.size() == 0)
            return null;

        if (gids.size() > 1)
            throw new DocerException("getCaseSensitiveGroupId :" + groupId + ": trovati " + gids.size() + " risultati");

        return gids.get(0);

    }

    // assegno parentGroup
    private void completeGroupProfile(String groupNodeUUID, IGroupProfileInfo groupProfileInfo) throws DocerException {

        String parentGroupId = groupProfileInfo.getParentGroupId();
        String groupName = groupProfileInfo.getGroupName();
        String gruppoStruttura = groupProfileInfo.getExtraInfo().get("GRUPPO_STRUTTURA");

        Reference reference = createReference(null, groupNodeUUID);
        Predicate predicate = createPredicate(reference);

        List<NamedValue> nvList = new ArrayList<NamedValue>();
        if (parentGroupId != null) {
            nvList.add(new NamedValue(DocerModel.PROP_GROUP_PARENTGROUPID, false, parentGroupId, null));
        }
        if (groupName != null) {
            nvList.add(new NamedValue(DocerModel.PROP_GROUP_GROUPNAME, false, groupName, null));
        }
        if (gruppoStruttura != null) {
            nvList.add(new NamedValue(DocerModel.PROP_GROUP_GRUPPOSTRUTTURA, false, gruppoStruttura, null));
        }

        Map<String, String> props = new HashMap<String, String>();
        for (String key : groupProfileInfo.getExtraInfo().keySet()) {
            if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.group_gruppo_struttura)) {
                continue;
            }
            if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.group_group_id)) {
                continue;
            }
            if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.group_group_name)) {
                continue;
            }
            if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.group_parent_group_id)) {
                continue;
            }

            props.put(key, groupProfileInfo.getExtraInfo().get(key));
        }

        populateAlfrescoProperties(props, nvList);

        if (nvList.size() == 0) {
            return;
        }

        // CML per aggiornamento profilo
        CMLUpdate cmlUpdate = new CMLUpdate();
        cmlUpdate.setProperty(nvList.toArray(new NamedValue[0])); // le
        // proprieta'
        cmlUpdate.setWhere(predicate); // il documento

        CML cml = new CML();
        if (cmlUpdate != null)
            cml.setUpdate(new CMLUpdate[]{cmlUpdate});

        getAlfrescoRepositoryService_update(cml);
    }

    private String getExtension(String docname) {

        if (docname == null) {
            return "";
        }

        Matcher m = extensionPattern.matcher(docname);

        if (m.matches()) {
            return m.group(1);
        }

        return "";
    }

    private int countAnagraficaAsAdmin(String type, Map<String, List<String>> searchCriteria) throws DocerException {

        String alfType = getType(type);

        String lucenequery = LuceneUtility.buildAlfrescoAnagraficheQueryString(alfType, searchCriteria);
        SearchResultSet webScriptResults = webScriptSearch(true, lucenequery, null, -1, ADMIN_USERID, false, false, false, false, false, false, null);

        return webScriptResults.getCount();
    }

    private int countDocnumAsAdmin(String docnum) throws DocerException {

        String lucenequery = "@docarea\\:docnum:\"" + docnum + "\"";
        SearchResultSet webScriptResults = webScriptSearch(true, lucenequery, null, -1, ADMIN_USERID, false, false, false, false, false, false, null);

        return webScriptResults.getCount();
    }

    private int countFolderAsAdmin(Map<String, List<String>> searchCriteria) throws DocerException {

        String lucenequery = LuceneUtility.buildAlfrescoFoldersQueryString(searchCriteria, false);
        SearchResultSet webScriptResults = webScriptSearch(true, lucenequery, null, -1, ADMIN_USERID, false, false, false, false, false, false, null);

        return webScriptResults.getCount();
    }

    private int countContainedFolderAndDocumentsAsAdmin(Reference folderRef) throws DocerException {

        String lucenequery = "+(TYPE:\"cm:content\" TYPE:\"cm:folder\") +PARENT:\"workspace://SpacesStore/" + folderRef.getUuid() + "\"";
        SearchResultSet webScriptResults = webScriptSearch(true, lucenequery, null, -1, ADMIN_USERID, false, false, false, false, false, false, null);

        return webScriptResults.getCount();

    }

    // private boolean[] documentIsInsideFascicoloOrPublicFolder(Reference
    // reference) throws DocerException {
    //
    // QueryResult queryresult =
    // getAlfrescoRepositoryService_queryParents(reference);
    //
    // if (queryresult.getResultSet() == null ||
    // queryresult.getResultSet().getTotalRowCount() < 1)
    // return new boolean[] { false, false };
    //
    // for (ResultSetRow row : queryresult.getResultSet().getRows()) {
    // String type = row.getNode().getType();
    // if (type.equals(DocerModel.TYPE_FASCICOLO)) {
    // return new boolean[] { true, false };
    // }
    //
    // List<String> aspectList = new ArrayList<String>();
    // aspectList = CopyArrayToList(aspectList, row.getNode().getAspects());
    // // se e' una Folder Docarea
    // if (aspectList.contains(DocerModel.ASPECT_DOCAREA_FOLDER)) {
    // for (NamedValue namedValue : row.getColumns()) {
    // if (namedValue.getName().equals(it.kdm.docer.sdk.Constants.folder_owner))
    // {
    //
    // String folderOwner = namedValue.getValue();
    // if (folderOwner == null) {
    // folderOwner = ""; // per le folder pubbliche
    // // l'owner e' vuoto
    // }
    // if (folderOwner.equals("")) {
    // return new boolean[] { false, true };
    // }
    // }
    // }
    // }
    //
    // }
    //
    // return new boolean[] { false, false };
    //
    // }

    private List<String> CopyArrayToList(List<String> list, String[] array) {

        if (list == null)
            return new ArrayList<String>();

        list.clear();

        for (String obj : array)
            list.add(obj);

        return list;
    }

    // private String escapeLuceneQueryValue(String userInput) {
    // // String escapeChars1 =
    // // "[\\.\\\\\\+\\-\\!\\(\\)\\:\\^\\]\\[\\{\\}\\~\\*\\?\\\"]";
    // String escapeChars1 =
    // "[\\.\\\\\\+\\-\\!\\(\\)\\:\\^\\]\\[\\{\\}\\~\\?\\\"]";
    //
    // // proibiti \:*"?
    //
    // String escaped = userInput;
    // escaped = escaped.replaceAll(escapeChars1, "\\\\$0");
    //
    // return escaped;
    // }

    private String getVersionId(String docId, String versionNum) throws DocerException {

        Map<String, String> versions = new HashMap<String, String>();

        SearchResultSet result = getUniqueDocument(docId, false, false, false, false, false, false);

        Reference refDoc = result.getFirstAndOnly().getReference();

        VersionHistory results = getAlfrescoAuthoringService_getVersionHistory(refDoc);

        if (results.getVersions() != null) {
            for (Version ver : results.getVersions()) {
                String versNum = ver.getLabel().substring(0, ver.getLabel().indexOf("."));
                String versId = ver.getId().getUuid();
                if (!versions.containsKey(versNum))
                    versions.put(versNum, versId);
            }
        }

        return versions.get(versionNum);
    }

    // private string getkeyfromvalue(map<string, reference> map, reference
    // value) {
    //
    // for (string key : map.keyset()) {
    // reference val = map.get(key);
    // if (val != null && value != null &&
    // val.getuuid().equals(value.getuuid())) {
    // return key;
    // }
    // }
    //
    // return "";
    // }

    private void checkEffectiveRightsForNormalaccess(Collection<String> docIds, String errorMessageHead) throws DocerException {

        String lucenequery = "";
        for (String docid : docIds) {
            lucenequery += "@docarea\\:docnum:\"" + docid + "\" ";
        }

        SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, null, -1, null, false, false, false, false, false, true, null);

        for (SearchResult sr : webScriptResults.getAllSearchResult()) {
            EnumACLRights effectiveRight = sr.getEffectiveRights();

            if (effectiveRight.equals(EnumACLRights.readOnly) || effectiveRight.equals(EnumACLRights.viewProfile)) {
                throw new DocerException(errorMessageHead + ": l'utente corrente non possiede sufficienti diritti sul documento " + sr.getDocId());
            }
        }
    }

    private SearchResultSet getFolderFullProfile(String folderId) throws DocerException {

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
        searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_ente);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_aoo);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_des_folder);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_name);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
        // returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_path);

        if (CIFS) {
            returnProperties.add(it.kdm.docer.sdk.Constants.path);
            returnProperties.add("CREATED");
            returnProperties.add("CREATOR");
            returnProperties.add("MODIFIER");
            returnProperties.add("MODIFIED");
            returnProperties.add(it.kdm.docer.sdk.Constants.inherits_acl);
        }

        String lucenequery = LuceneUtility.buildAlfrescoFoldersQueryString(searchCriteria, true);

        SearchResultSet results = webScriptSearch(false, lucenequery, returnProperties, -1, null, false, false, false, false, true, true, null);

        if (results.getCount() > 1) {
            throw new DocerException("errore: trovate " + results.getCount() + " folder con FOLDER_ID " + folderId);
        }

        return results;
    }

    private SearchResultSet getFolderFullProfileAsAdmin(String folderId) throws DocerException {

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
        searchCriteria.put(it.kdm.docer.sdk.Constants.folder_folder_id, Arrays.asList(folderId));

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_id);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_ente);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_cod_aoo);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_des_folder);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_name);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_owner);
        returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);
        // returnProperties.add(it.kdm.docer.sdk.Constants.folder_folder_path);

        if (CIFS) {
            returnProperties.add(it.kdm.docer.sdk.Constants.path);
            returnProperties.add("CREATED");
            returnProperties.add("CREATOR");
            returnProperties.add("MODIFIER");
            returnProperties.add("MODIFIED");
            returnProperties.add(it.kdm.docer.sdk.Constants.inherits_acl);
        }

        String lucenequery = LuceneUtility.buildAlfrescoFoldersQueryString(searchCriteria, true);

        SearchResultSet results = webScriptSearch(false, lucenequery, returnProperties, -1, ADMIN_USERID, false, false, false, false, true, true, null);

        if (results.getCount() > 1) {
            throw new DocerException("errore: trovate " + results.getCount() + " folder con FOLDER_ID " + folderId);
        }

        return results;
    }

    private SearchResultSet getUniqueDocument(String docId, boolean getrelated, boolean getAdvancedVersions, boolean getRiferimenti, boolean getcheckout, boolean getacl, boolean geteffectiverights)
            throws DocerException {

        // restituiamo il profilo base base...
        String lucenequery = "@docarea\\:docnum:\"" + docId + "\" ";

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(it.kdm.docer.sdk.Constants.doc_docname);
        returnProperties.add(it.kdm.docer.sdk.Constants.doc_docnum);
        returnProperties.add(it.kdm.docer.sdk.Constants.doc_type_id);
        returnProperties.add(it.kdm.docer.sdk.Constants.doc_archive_type);

        returnProperties.add(it.kdm.docer.sdk.Constants.doc_cod_ente);

        returnProperties.add(it.kdm.docer.sdk.Constants.doc_cod_aoo);

        returnProperties.add(it.kdm.docer.sdk.Constants.doc_classifica);
        returnProperties.add(it.kdm.docer.sdk.Constants.doc_cod_titolario);

        returnProperties.add(it.kdm.docer.sdk.Constants.doc_anno_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.doc_progr_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.doc_num_fascicolo);
        returnProperties.add(it.kdm.docer.sdk.Constants.doc_fascicoli_secondari);

        returnProperties.add(it.kdm.docer.sdk.Constants.folder_parent_folder_id);

        returnProperties.add(it.kdm.docer.sdk.Constants.inherits_acl);

        SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, returnProperties, -1, null, getrelated, getAdvancedVersions, getRiferimenti, getcheckout, getacl, geteffectiverights,
                null);

        if (webScriptResults.getCount() == 0)
            throw new DocerException("documento " + docId + " non trovato");

        if (webScriptResults.getCount() > 1)
            throw new DocerException("trovati " + webScriptResults.getCount() + " risultati con docId " + docId);

        return webScriptResults;
    }

    private SearchResultSet getAlfrescoSpace(String lucenequery) throws DocerException {

        return webScriptSearch(false, lucenequery, new ArrayList<String>(), -1, null, false, false, false, false, false, true, null);
    }

    // Necessario quando si chiama da Business Logic Docarea 1.5
    private Map<String, String> removeExclusiveMetadata(Map<String, String> profileData) {

        Map<String, String> cleanProfile = new HashMap<String, String>();

        cleanProfile.putAll(profileData);

        cleanProfile.remove(it.kdm.docer.sdk.Constants.ente_des_ente);
        cleanProfile.remove(it.kdm.docer.sdk.Constants.aoo_des_aoo);
        cleanProfile.remove(it.kdm.docer.sdk.Constants.titolario_des_titolario);
        cleanProfile.remove(it.kdm.docer.sdk.Constants.titolario_parent_classifica);
        cleanProfile.remove(it.kdm.docer.sdk.Constants.fascicolo_des_fascicolo);
        cleanProfile.remove(it.kdm.docer.sdk.Constants.fascicolo_parent_progr_fascicolo);
        // TODO: da scommentare quando attiviamo ereditarieta' multipla acl
        // cleanProfile.remove(ACL_EXPLICIT);
        for (CustomItemProps cip : CUSTOM_ITEM_MAPPING.values()) {
            cleanProfile.remove(cip.getBlDescrizionePropName());
        }

        return cleanProfile;
    }

    private String generateEnteAdminsGroupId(String codiceEnte) {
        if (codiceEnte == null || codiceEnte.equals(""))
            return "";

        codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.custom_cod_ente, codiceEnte);

        return ENTE_ADMINS_GROUPID_PREFIX + codiceEnte;
    }

    private String generateAOOAdminsGroupId(String codiceAoo, String codiceEnte) {
        if (codiceAoo == null || codiceAoo.equals(""))
            return "";
        if (codiceEnte == null || codiceEnte.equals(""))
            return "";

        return AOO_ADMINS_GROUPID_PREFIX + generateAOOGroupId(codiceAoo, codiceEnte);
    }

    private String generateAOOGroupId(String codiceAOO, String codiceEnte) {

        if (codiceAOO == null || codiceAOO.equals(""))
            return "";
        if (codiceEnte == null || codiceEnte.equals(""))
            return "";
        String aooGroupId = AOO_GROUPID_FORMAT;

        codiceAOO = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.custom_cod_aoo, codiceAOO);
        codiceEnte = getMappedPropertyCaseFromBusinessLogicName(it.kdm.docer.sdk.Constants.custom_cod_ente, codiceEnte);

        aooGroupId = aooGroupId.replaceAll(AOO_GROUPID_REGEX_AOO, codiceAOO);
        aooGroupId = aooGroupId.replaceAll(AOO_GROUPID_REGEX_ENTE, codiceEnte);

        return aooGroupId;
    }

    private DateTime parseDateTime(String datetime) {
        DateTimeFormatter fm = ISODateTimeFormat.dateTime();
        return fm.parseDateTime(datetime);
    }

    private EnumACLRights getAlfrescoEffectiveRightsAnagrafica(String type, Map<String, String> id, String userid) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        if (type == null || type.equals(""))
            throw new DocerException((short) -1171, "type obbligatorio");

        String methodName = "getAlfrescoEffectiveRightsAnagrafica";

        String myType = getType(type);

        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
        for (String key : id.keySet()) {

            searchCriteria.put(key, Arrays.asList(id.get(key)));
        }
        // String criteria = buildAlfrescoSearchQueryString(searchCriteria,
        // null, false);

        String lucenequery = LuceneUtility.buildAlfrescoAnagraficheQueryString(myType, searchCriteria);

        // String luceneParam = "";
        //
        // if (!criteria.equals(""))
        // luceneParam = "+(" + criteria + ")";
        //
        // String lucenequery = typeParam + " " + luceneParam;

        SearchResultSet webScriptResults = webScriptSearch(false, lucenequery, null, -1, userid, false, false, false, false, false, true, null);

        if (webScriptResults.getCount() == 0) {
            return EnumACLRights.undefined;
        }

        if (webScriptResults.getCount() > 1) {
            throw new DocerException("trovati " + webScriptResults.getCount() + " risultati");
        }

        return webScriptResults.getFirstAndOnly().getEffectiveRights();

    }

    private String getType(String type) {
        String myType = type.toLowerCase();

        CustomItemProps cProps = CUSTOM_ITEM_MAPPING.get(type.toUpperCase());
        if (cProps != null)
            myType = cProps.getAlfType();
        return DocerModel.MY_MODEL_PREFIX + ":" + myType;
    }

    // // ******* LUCENE QUERY BULDING ******* //

    protected boolean deleteUsers(Map<String, List<String>> searchCriteria, List<String> exclude, int controlSize) throws Exception {

        if (searchCriteria.size() == 0)
            return true;

        List<IUserProfileInfo> uinfo = searchUsers(searchCriteria);

        if (controlSize > -1 && uinfo.size() != controlSize) {
            throw new Exception("Errore verifica di conteggio: trovati " + uinfo.size() + " <> " + controlSize + " (numero di controllo)");
        }

        List<String> usersToDelete = new ArrayList<String>();

        for (IUserProfileInfo upi : uinfo) {

            if (exclude.contains(upi.getUserId())) {
                continue;
            }

            usersToDelete.add(upi.getUserId());
        }

        try {
            if (usersToDelete.size() == 0)
                return true;
            WebServiceFactory.getAdministrationService().deleteUsers(usersToDelete.toArray(new String[0]));
            return true;
        } catch (RepositoryFault e) {

            throw new Exception(e.getMessage1());

        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }

    }

    protected boolean deleteGroups(Map<String, List<String>> searchCriteria, List<String> exclude, int controlSize) throws Exception {

        if (searchCriteria.size() == 0)
            return true;

        List<IGroupProfileInfo> ginfo = searchGroups(searchCriteria);

        if (ginfo.size() != controlSize) {
            throw new Exception("Errore verifica di conteggio: gruppi da eliminare " + ginfo.size() + " <> " + controlSize + " (numero di controllo)");
        }

        List<String> groupsToDelete = new ArrayList<String>();

        for (IGroupProfileInfo gpi : ginfo) {

            if (exclude.contains(gpi.getGroupId())) {
                continue;
            }

            groupsToDelete.add("GROUP_" + gpi.getGroupId());
        }

        try {
            if (groupsToDelete.size() == 0)
                return true;

            WebServiceFactory.getAccessControlService().deleteAuthorities(groupsToDelete.toArray(new String[0]));
            return true;
        } catch (RepositoryFault e) {
            throw new Exception(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }

    }

    private UpdateResult[] getAlfrescoRepositoryService_update(CML cml) throws DocerException {
        // aggiorno e muovo
        try {
            return WebServiceFactory.getRepositoryService().update(cml);
        } catch (RepositoryFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    private QueryResult getAlfrescoArchiveRepositoryService_query(Query query) throws DocerException {

        try {
            // sovrascrivo language per bug web service
            query.setLanguage("lucene");

            return WebServiceFactory.getRepositoryService().query(DocerModel.ARCHIVE_STORE, query, false);
        } catch (RepositoryFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }

    }

    private QueryResult getAlfrescoRepositoryService_query(Query query) throws DocerException {

        try {
            // sovrascrivo language per bug web service
            query.setLanguage("lucene");
            if (QUERY_DEBUG) {
                System.out.println(query.getStatement());
            }
            return WebServiceFactory.getRepositoryService().query(DocerModel.WORKSPACE_STORE, query, false);
        } catch (RepositoryFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }

    }

    private String[] getAlfrescoAccessControlService_getParentAuthorities(String userId, SiblingAuthorityFilter siblingAuthorityFilter) throws DocerException {
        try {
            return WebServiceFactory.getAccessControlService().getParentAuthorities(userId, siblingAuthorityFilter);
        } catch (AccessControlFault e) {

            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    private VersionResult getAlfrescoAuthoringService_createVersion(Predicate predicate, NamedValue[] comments) throws DocerException {
        try {
            return WebServiceFactory.getAuthoringService().createVersion(predicate, comments, false);
        } catch (AuthoringFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }

    }

    private void accessControlService_addChildAuthorities(String parentAuthority, List<String> authorities) throws DocerException {
        try {
            WebServiceFactory.getAccessControlService().addChildAuthorities(parentAuthority, authorities.toArray(new String[0]));
        } catch (AccessControlFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    private void getAlfrescoAuthoringService_lock(Predicate predicate) throws DocerException {
        try {
            WebServiceFactory.getAuthoringService().lock(predicate, false, LockTypeEnum.write);
        } catch (AuthoringFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    private void getAlfrescoAuthoringService_unlock(Predicate predicate) throws DocerException {
        try {
            WebServiceFactory.getAuthoringService().unlock(predicate, false);
        } catch (AuthoringFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    private void getAlfrescoAccessControlService_addChildAuthorities(String parentAuthority, String[] authorities) throws DocerException {
        try {
            WebServiceFactory.getAccessControlService().addChildAuthorities(parentAuthority, authorities);
        } catch (AccessControlFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    private void getAlfrescoAccessControlService_removeChildAuthorities(String parentAuthority, List<String> authorities) throws DocerException {
        try {
            WebServiceFactory.getAccessControlService().removeChildAuthorities(parentAuthority, authorities.toArray(new String[0]));
        } catch (AccessControlFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    private VersionHistory getAlfrescoAuthoringService_getVersionHistory(Reference node) throws DocerException {

        try {
            return WebServiceFactory.getAuthoringService().getVersionHistory(node);
        } catch (AuthoringFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    private void getAlfrescoAccessControlService_removeChildAuthorities(String parentAuthority, String[] authorities) throws DocerException {
        try {
            WebServiceFactory.getAccessControlService().removeChildAuthorities(parentAuthority, authorities);
        } catch (AccessControlFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    private String[] getAlfrescoAccessControlService_getChildAuthorities(String authority, SiblingAuthorityFilter siblingAuthorityFilter) throws DocerException {
        try {
            return WebServiceFactory.getAccessControlService().getChildAuthorities(authority, siblingAuthorityFilter);
        } catch (AccessControlFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    // private QueryResult repositoryService_queryAssociated(Reference
    // node,Association association) throws DocerException{
    // try {
    // return WebServiceFactory.getRepositoryService().queryAssociated(node,
    // association);
    // }
    // catch (RepositoryFault e) {
    // throw new DocerException(e.getMessage1());
    // }
    // catch (RemoteException e) {
    // throw new DocerException(e.getMessage());
    // }
    // }

    private Node[] getAlfrescoRepositoryService_get(Predicate predicate) throws DocerException {

        try {
            return WebServiceFactory.getRepositoryService().get(predicate);
        } catch (RepositoryFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    // private QueryResult getAlfrescoRepositoryService_queryParents(Reference
    // node) throws DocerException {
    //
    // try {
    // return WebServiceFactory.getRepositoryService().queryParents(node);
    // }
    // catch (RepositoryFault e) {
    // throw new DocerException(e.getMessage1());
    // }
    // catch (RemoteException e) {
    // if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
    // throw new DocerException(UNAUTH_SESSION_ERROR);
    // }
    // throw new DocerException(e.getMessage());
    // }
    // }

    private void getAlfrescoAdministrationService_createUsers(NewUserDetails[] newUserDetail) throws DocerException {

        try {
            WebServiceFactory.getAdministrationService().createUsers(newUserDetail);
        } catch (AdministrationFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            if (e.getMessage().equals("")) {
                throw new DocerException("AdministrationService createUsers Error");
            }
            throw new DocerException(e.getMessage());
        }

    }

    private void getAlfrescoAdministrationService_updateUsers(UserDetails[] users) throws DocerException {
        try {
            WebServiceFactory.getAdministrationService().updateUsers(users);
        } catch (AdministrationFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            if (e.getMessage().equals("")) {
                throw new DocerException("AdministrationService updateUsers Error");
            }
            throw new DocerException(e.getMessage());
        }
    }

    private String[] getAlfrescoAccessControlService_createAuthorities(String parentAuthority, NewAuthority[] newAuthorites) throws DocerException {

        try {
            return WebServiceFactory.getAccessControlService().createAuthorities(parentAuthority, newAuthorites);
        } catch (AccessControlFault e) {
            throw new DocerException(e.getMessage1());
        } catch (RemoteException e) {
            if (e.getMessage().toLowerCase().indexOf(UNAUTH_SESSION_MESSAGE) > -1) {
                throw new DocerException(UNAUTH_SESSION_ERROR);
            }
            throw new DocerException(e.getMessage());
        }
    }

    private SearchResultSet webScriptUsersSearch(boolean onlyCount, String lucenequery, int maxResults, String runAsUserid, Map<String, EnumSearchOrder> orderby) throws DocerException {

        List<String> returnProperties = new ArrayList<String>();

        for (MetaProperty mp : METAMAPPING_USER.getAllMetaProperties()) {
            returnProperties.add(mp.getBlPropName());
        }

        return webScriptSearch("USER", onlyCount, lucenequery, returnProperties, maxResults, runAsUserid, false, false, false, false, false, false, orderby);
    }

    private SearchResultSet webScriptGroupsSearch(boolean onlyCount, String lucenequery, int maxResults, String runAsUserid, Map<String, EnumSearchOrder> orderby) throws DocerException {
        List<String> returnProperties = new ArrayList<String>();

        for (MetaProperty mp : METAMAPPING_GROUP.getAllMetaProperties()) {
            returnProperties.add(mp.getBlPropName());
        }

        return webScriptSearch("GROUP", onlyCount, lucenequery, returnProperties, maxResults, runAsUserid, false, false, false, false, false, false, orderby);
    }

    private SearchResultSet webScriptSearch(boolean onlyCount, String lucenequery, List<String> returnProperties, int maxrows, String runAsUserid, boolean getrelated, boolean getadvancedversions,
                                            boolean getriferimenti, boolean getcheckout, boolean getacl, boolean geteffectiverights, Map<String, EnumSearchOrder> orderby) throws DocerException {
        return webScriptSearch("", onlyCount, lucenequery, returnProperties, maxrows, runAsUserid, getrelated, getadvancedversions, getriferimenti, getcheckout, getacl, geteffectiverights, orderby);
    }

    // private static String DOC = "D";
    // private static String PROP = "P";
    // private static String PROPS = "PS";
    // private static String name_attribute = "n";
    // private static String value_attribute = "v";

    private SearchResultSet webScriptSearch(String type, boolean onlyCount, String lucenequery, List<String> returnProperties, int maxrows, String runAsUserid, boolean getrelated,
                                            boolean getadvancedversions, boolean getriferimenti, boolean getcheckout, boolean getacl, boolean geteffectiverights, Map<String, EnumSearchOrder> orderby) throws DocerException {

        if (maxrows < 0) {
            maxrows = SEARCH_MAX_RESULTS;
        }

        InputStream responseStream = null;
        PostMethod method = null;

        HttpClient httpClient = null;

        DataTable<String> dtResult = new DataTable<String>();

        List<String> returnPropertiesAlfresco = null;

        returnPropertiesAlfresco = new ArrayList<String>();

        // proprieta' che vanno sempre restituite
        returnPropertiesAlfresco.add(SYS_NODE_UUID);
        returnPropertiesAlfresco.add(CM_NAME);
        returnPropertiesAlfresco.add(it.kdm.docer.sdk.Constants.path);

        type = type.toUpperCase();

        if (returnProperties != null) {

            for (String columnName : returnProperties) {

                if (dtResult.getColumnNames().contains(columnName.toUpperCase())) {
                    continue;
                }

                dtResult.getColumnNames().add(columnName.toUpperCase());

                MetaProperty mp = null;

                if (type.equals("USER")) {
                    mp = METAMAPPING_USER.getMetaPropertyFromBusinessLogicName(columnName.toUpperCase());
                } else if (type.equals("GROUP")) {
                    mp = METAMAPPING_GROUP.getMetaPropertyFromBusinessLogicName(columnName.toUpperCase());
                } else {
                    mp = METAMAPPING.getMetaPropertyFromBusinessLogicName(columnName.toUpperCase());
                }

                if (mp == null) {
                    continue;
                }

                returnPropertiesAlfresco.add(mp.getAlfShortPropName());
            }
        }

        // se vogliono il content size
        if (dtResult.getColumnNames().contains("CONTENT_SIZE")) {
            returnPropertiesAlfresco.add(CM_CONTENT);
        }
        // il docnum va sempre restituito
        if (!dtResult.getColumnNames().contains("DOCNUM")) {
            returnPropertiesAlfresco.add(DOCAREA_DOCNUM);
        }

        // else {
        //
        // for (MetaProperty mp : METAMAPPING.getAllMetaProperties()) {
        //
        // String columnName = mp.getBlPropName();
        //
        // if (dtResult.getColumnNames().contains(columnName.toUpperCase())) {
        // continue;
        // }
        //
        // dtResult.getColumnNames().add(columnName.toUpperCase());
        // }
        //
        // }

        // il RANKING sta nelle return properties
        // dtResult.getColumnNames().add("RANKING");

        String url = null;

        try {

            url = combineUrl(ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$", ""), "/service/kdm/search");

            httpClient = new HttpClient();

            method = new PostMethod(url);
            method.addRequestHeader("Connection", "Keep-Alive");
            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");

            // NameValuePair nvp = new NameValuePair();

            List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();

            NameValuePair nv0 = new NameValuePair();
            nv0.setName("provider_version");
            nv0.setValue("1.3.5-r11078");
            parametersBody.add(nv0);

            NameValuePair nv1 = new NameValuePair();
            nv1.setName("alf_ticket");
            nv1.setValue(URLEncoder.encode(currentUser.getTicket(), "UTF-8"));
            parametersBody.add(nv1);

            NameValuePair nv2 = new NameValuePair();
            nv2.setName("lucenequery");
            // nv2.setValue(lucenequery);
            if (QUERY_DEBUG) {
                System.out.println(lucenequery);
            }
            nv2.setValue(StringEscapeUtils.escapeHtml(lucenequery));
            parametersBody.add(nv2);

            NameValuePair nv3 = new NameValuePair();
            nv3.setName("maxrows");
            nv3.setValue(String.valueOf(maxrows));
            parametersBody.add(nv3);

            NameValuePair nvLimit = new NameValuePair();
            nvLimit.setName("limit");
            // nvLimit.setValue(String.valueOf(-1));
            nvLimit.setValue(String.valueOf(maxrows));
            parametersBody.add(nvLimit);

            NameValuePair nv4 = new NameValuePair();
            nv4.setName("adminuid");
            nv4.setValue(ADMIN_USERID);
            parametersBody.add(nv4);

            if (CIFS) {
                NameValuePair nvPath = new NameValuePair();
                nvPath.setName("getpath");
                nvPath.setValue("true");
                parametersBody.add(nvPath);
            }

            if (runAsUserid != null) {
                NameValuePair nv5 = new NameValuePair();
                nv5.setName("runas");
                nv5.setValue(runAsUserid);
                parametersBody.add(nv5);
            }

            NameValuePair nv6 = new NameValuePair();
            nv6.setName("getrelated");
            nv6.setValue(String.valueOf(getrelated));
            parametersBody.add(nv6);

            NameValuePair nv61 = new NameValuePair();
            nv61.setName("getriferimenti");
            nv61.setValue(String.valueOf(getriferimenti));
            parametersBody.add(nv61);

            NameValuePair nv62 = new NameValuePair();
            nv62.setName("getadvancedversions");
            nv62.setValue(String.valueOf(getadvancedversions));
            parametersBody.add(nv62);

            NameValuePair nv7 = new NameValuePair();
            nv7.setName("getcheckout");
            nv7.setValue(String.valueOf(getcheckout));
            parametersBody.add(nv7);

            NameValuePair nv8 = new NameValuePair();
            nv8.setName("prefix");
            nv8.setValue(String.valueOf(true));
            parametersBody.add(nv8);

            NameValuePair nv9 = new NameValuePair();
            nv9.setName("geteffectiverights");
            nv9.setValue(String.valueOf(geteffectiverights));
            parametersBody.add(nv9);

            if (onlyCount) {
                NameValuePair nv10 = new NameValuePair();
                nv10.setName("operation");
                nv10.setValue("count");
                parametersBody.add(nv10);
            }

            if (ORDERBY_ENABLED && orderby != null && orderby.size() > 0) {
                NameValuePair nv11 = new NameValuePair();
                nv11.setName("orderby");
                String strOrderBy = LuceneUtility.buildAlfrescoSearchOrderBy(type, orderby);
                if (QUERY_DEBUG) {
                    System.out.println(strOrderBy);
                }
                nv11.setValue(strOrderBy);

                parametersBody.add(nv11);
            }

            NameValuePair nv12 = new NameValuePair();
            nv12.setName("getacl");
            nv12.setValue(String.valueOf(getacl));
            parametersBody.add(nv12);

            NameValuePair nvLanguage = new NameValuePair();
            nvLanguage.setName("language");
            nvLanguage.setValue("lucene");
            parametersBody.add(nvLanguage);

            NameValuePair nvtype = new NameValuePair();
            nvtype.setName("gettype");
            nvtype.setValue(String.valueOf(false));
            parametersBody.add(nvtype);

            NameValuePair nvcheckexists = new NameValuePair();
            nvcheckexists.setName("checkexists");
            nvcheckexists.setValue(String.valueOf(false));
            parametersBody.add(nvcheckexists);

            if (returnPropertiesAlfresco != null && returnPropertiesAlfresco.size() > 0) {
                NameValuePair nvReturnProperties = new NameValuePair();
                nvReturnProperties.setName("returnproperties");
                nvReturnProperties.setValue(returnPropertiesAlfresco.toString().replaceAll("^\\[", "").replaceAll("\\]$", ""));
                parametersBody.add(nvReturnProperties);
            }

            method.setRequestBody(parametersBody.toArray(new NameValuePair[0]));

            long start = new Date().getTime();

            int resCode = httpClient.executeMethod(method);

            long end = new Date().getTime();

            // System.out.println("tempo search webscript: " +(end-start)
            // +" msec");
            if (resCode != 200) {

                // String responseString = method.getResponseBodyAsString();

                InputStream in = method.getResponseBodyAsStream();
                String encoding = method.getResponseCharSet();
                encoding = encoding == null ? "UTF-8" : encoding;
                String responseString = IOUtils.toString(in, encoding);

                int arg0 = responseString.indexOf("InizioErrore-") + 13;
                int arg1 = responseString.indexOf("-FineErrore");

                if (arg1 > arg0)
                    throw new DocerException("search web script, errore http: " + responseString.substring(arg0, arg1));

                if (resCode == 401)
                    throw new Exception(UNAUTH_SESSION_ERROR);

                throw new DocerException("search web script, errore http: " + resCode);
            }

            responseStream = method.getResponseBodyAsStream();

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document dom = db.parse(responseStream);

            NodeList resultsNodeList = dom.getElementsByTagName("RESULTS");

            if (resultsNodeList == null)
                throw new DocerException("search web script: RESULTS: xmlNodeList==null");

            Element resultsElement = (Element) resultsNodeList.item(0);

            String strCount = resultsElement.getAttribute("count");

            if (strCount == null || strCount.equals(""))
                throw new DocerException("search web script count, attributo count assente o null");

            // String strFound = resultsElement.getAttribute("found");
            // if(strFound==null || strFound.equals("")){
            // strFound = strCount;
            // }

            // System.out.println("query-time: "
            // +resultsElement.getAttribute("query-time"));
            // System.out.println("execution-time: "
            // +resultsElement.getAttribute("execution-time"));

            SearchResultSet getDocumentsWebScriptResult = new SearchResultSet();
            getDocumentsWebScriptResult.setCount(Integer.parseInt(strCount));
            getDocumentsWebScriptResult.setDatatable(dtResult);

            NodeList docNodeList = resultsElement.getElementsByTagName("D");

            if (docNodeList == null || docNodeList.getLength() == 0) {
                docNodeList = resultsElement.getElementsByTagName("DOC");
                if (docNodeList == null || docNodeList.getLength() == 0) {
                    return getDocumentsWebScriptResult;
                }
            }


            // cicliamo tutti i risultati (nodi <DOC>)
            for (int i = 0; i < docNodeList.getLength(); i++) {

                SearchResult sr = new SearchResult();
                // String folderid = "";
                String docid = "";

                Reference nodeRef = new Reference();
                nodeRef.setUuid("");

                DataRow<String> row = dtResult.newRow("");

                Element docElement = (Element) docNodeList.item(i);

                NodeList propsNodeList = docElement.getElementsByTagName("PS");
                if (propsNodeList == null || propsNodeList.getLength() == 0) {
                    propsNodeList = docElement.getElementsByTagName("PROPS");
                }

                if (propsNodeList != null && propsNodeList.getLength() > 0) {

                    Element propsElement = (Element) propsNodeList.item(0);
                    NodeList propNodeList = propsElement.getElementsByTagName("P");

                    if (propNodeList == null || propNodeList.getLength() == 0) {
                        propNodeList = propsElement.getElementsByTagName("PROP");
                    }

                    if (propNodeList != null) {

                        // cicliamo i metadati
                        for (int j = 0; j < propNodeList.getLength(); j++) {

                            Element propElement = (Element) propNodeList.item(j);

                            String alfPropName = propElement.getAttribute("n");
                            if (propElement.hasAttribute("name")) {
                                alfPropName = propElement.getAttribute("name");
                            }
                            alfPropName = alfPropName.replaceAll("^da:", "docarea:");

                            String propValue = propElement.getAttribute("v");
                            if (propElement.hasAttribute("value")) {
                                propValue = propElement.getAttribute("value");
                            }

                            if (alfPropName.equals(SYS_NODE_UUID)) {
                                nodeRef.setStore(DocerModel.WORKSPACE_STORE);
                                nodeRef.setUuid(propValue);
                                sr.setReference(nodeRef);
                            } else if (alfPropName.equals(DOCAREA_DOCNUM)) {
                                docid = propValue;
                                sr.setDocId(new DocId(docid));
                            } else if (alfPropName.equals(CM_NAME)) {
                                sr.setCmName(propValue);
                            }

                            // else if (alfPropName.equals("docarea:folderId"))
                            // {
                            // folderid = propValue;
                            // }

                            if (alfPropName.equals(it.kdm.docer.sdk.Constants.doc_ranking)) {

                                if (row.get(it.kdm.docer.sdk.Constants.doc_ranking) != null) {
                                    row.put(it.kdm.docer.sdk.Constants.doc_ranking, propValue);
                                }
                                continue;
                            }

                            MetaProperty mp = null;
                            String docerPropName = null;

                            if (type.equals("USER")) {
                                mp = METAMAPPING_USER.getMetaPropertyFromAlfrescoShortName(alfPropName);
                                if (mp == null) {
                                    mp = METAMAPPING_USER.getMetaPropertyFromAlfrescoFullName(alfPropName);
                                }
                            } else if (type.equals("GROUP")) {
                                mp = METAMAPPING_GROUP.getMetaPropertyFromAlfrescoShortName(alfPropName);
                                if (mp == null) {
                                    mp = METAMAPPING_GROUP.getMetaPropertyFromAlfrescoFullName(alfPropName);
                                }
                            } else {
                                mp = METAMAPPING.getMetaPropertyFromAlfrescoShortName(alfPropName);
                                if (mp == null) {
                                    mp = METAMAPPING.getMetaPropertyFromAlfrescoFullName(alfPropName);
                                }
                            }

                            if (mp != null) {
                                docerPropName = mp.getBlPropName();
                            }

                            if (alfPropName.equals(CM_CONTENT)) {

                                if (dtResult.getColumnNames().contains("CONTENT_SIZE")) {

                                    Matcher m = patternSize.matcher(propValue);

                                    String size = "-1";
                                    if (m.matches()) {
                                        size = m.group(1);
                                    }

                                    row.put("CONTENT_SIZE", size);
                                }

                            }

                            if (alfPropName.matches(it.kdm.docer.sdk.Constants.path)) {
                                docerPropName = it.kdm.docer.sdk.Constants.path;
                                propValue = decodePath(propValue);
                            }

                            if (StringUtils.isNotEmpty(docerPropName) && dtResult.getColumnNames().contains(docerPropName)) {
                                row.put(docerPropName, propValue);
                            }

                        } // fine ciclo delle props del nodo

                        // aggiungo row
                        dtResult.addRow(row);
                        sr.setProfile(row);
                        getDocumentsWebScriptResult.addSearchResult(sr);

                    }
                }

                if (getacl) {
                    Map<String, EnumACLRights> aclsMap = new HashMap<String, EnumACLRights>();
                    sr.setAcl(aclsMap);
                    NodeList aclsNodeList = docElement.getElementsByTagName("ACLS");

                    if (aclsNodeList != null && aclsNodeList.getLength() > 0) {

                        Element aclsElement = (Element) aclsNodeList.item(0);
                        NodeList aclNodeList = aclsElement.getElementsByTagName("ACL");
                        if (aclNodeList != null) {

                            for (int j = 0; j < aclNodeList.getLength(); j++) {
                                Element aclElement = (Element) aclNodeList.item(j);

                                String name = aclElement.getAttribute("n");
                                if (aclElement.hasAttribute("name")) {
                                    name = aclElement.getAttribute("name");
                                }
                                String value = aclElement.getAttribute("v");
                                if (aclElement.hasAttribute("value")) {
                                    value = aclElement.getAttribute("value");
                                }
                                aclsMap.put(name, convertToDocareaRights(value));
                            }
                        }
                    }
                }

                if (getrelated) {
                    Map<String, Reference> relatedMap = new HashMap<String, Reference>();
                    sr.setRelatedMap(relatedMap);

                    NodeList relatedNodeList = docElement.getElementsByTagName("RELATED");
                    if (relatedNodeList != null && relatedNodeList.getLength() > 0) {

                        Element relatedElement = (Element) relatedNodeList.item(0);
                        NodeList relNodeList = relatedElement.getElementsByTagName("REL");
                        if (relNodeList != null) {

                            for (int j = 0; j < relNodeList.getLength(); j++) {
                                Element relElement = (Element) relNodeList.item(j);

                                String relDocnum = relElement.getAttribute("docnum");
                                Reference relRef = createReference(relElement.getAttribute("uuid"));
                                relatedMap.put(relDocnum, relRef);
                            }
                        }
                    }
                }

                if (getadvancedversions) {
                    Map<String, Reference> advancedVersionMap = new HashMap<String, Reference>();
                    sr.setAdvancedVersionsMap(advancedVersionMap);

                    NodeList advancedVersionNodeList = docElement.getElementsByTagName("ADV_VERSION");
                    if (advancedVersionNodeList != null && advancedVersionNodeList.getLength() > 0) {

                        Element advancedVersionElement = (Element) advancedVersionNodeList.item(0);
                        NodeList avNodeList = advancedVersionElement.getElementsByTagName("AV");
                        if (avNodeList != null) {

                            for (int j = 0; j < avNodeList.getLength(); j++) {
                                Element relElement = (Element) avNodeList.item(j);

                                String avDocnum = relElement.getAttribute("docnum");
                                Reference avRef = createReference(relElement.getAttribute("uuid"));
                                advancedVersionMap.put(avDocnum, avRef);
                            }
                        }
                    }
                }

                if (getriferimenti) {
                    Map<String, Reference> riferimentiMap = new HashMap<String, Reference>();
                    sr.setRiferimentiMap(riferimentiMap);

                    NodeList riferimentiNodeList = docElement.getElementsByTagName("RIFS");
                    if (riferimentiNodeList != null && riferimentiNodeList.getLength() > 0) {

                        Element riferimentiElement = (Element) riferimentiNodeList.item(0);
                        NodeList rifNodeList = riferimentiElement.getElementsByTagName("RIF");
                        if (rifNodeList != null) {

                            for (int j = 0; j < rifNodeList.getLength(); j++) {
                                Element rifElement = (Element) rifNodeList.item(j);

                                String rifDocnum = rifElement.getAttribute("docnum");
                                Reference rifRef = createReference(rifElement.getAttribute("uuid"));
                                riferimentiMap.put(rifDocnum, rifRef);
                            }
                        }
                    }
                }

                if (getcheckout) {
                    it.kdm.docer.sdk.classes.LockStatus lockStatus = new it.kdm.docer.sdk.classes.LockStatus();
                    sr.setLockStatus(lockStatus);

                    NodeList lockNodeList = docElement.getElementsByTagName("LOCK");
                    if (lockNodeList != null && lockNodeList.getLength() > 0) {

                        Element lockElement = (Element) lockNodeList.item(0);
                        NodeList lockerPropNodeList = lockElement.getElementsByTagName("P");
                        if (lockerPropNodeList == null || lockerPropNodeList.getLength() == 0) {
                            lockerPropNodeList = lockElement.getElementsByTagName("PROP");
                        }

                        if (lockerPropNodeList != null) {

                            boolean locked = false;
                            String user_Id = "";
                            String full_Name = "";
                            for (int j = 0; j < lockerPropNodeList.getLength(); j++) {
                                Element propElement = (Element) lockerPropNodeList.item(j);

                                String name = propElement.getAttribute("n");
                                if (propElement.hasAttribute("name")) {
                                    name = propElement.getAttribute("name");
                                }

                                String value = propElement.getAttribute("v");
                                if (propElement.hasAttribute("value")) {
                                    value = propElement.getAttribute("value");
                                }

                                if (name == null) {
                                    name = "";
                                }
                                if (value == null) {
                                    value = "";
                                }

                                if (name.equalsIgnoreCase("USER_ID")) {
                                    locked = true;
                                    user_Id = value;
                                    continue;
                                }
                                if (name.equalsIgnoreCase("FULL_NAME")) {
                                    locked = true;
                                    full_Name = value;
                                    continue;
                                }
                            }

                            if (locked) {
                                lockStatus.setLocked(true);
                                lockStatus.setUserId(user_Id);
                                lockStatus.setFullName(full_Name);
                            }
                        }
                    }

                }
                if (geteffectiverights) {
                    NodeList effectiveRightsNodeList = docElement.getElementsByTagName("EFFECTIVE_RIGHTS");
                    if (effectiveRightsNodeList != null && effectiveRightsNodeList.getLength() > 0) {

                        Element effectiveRightsElement = (Element) effectiveRightsNodeList.item(0);

                        String value = effectiveRightsElement.getAttribute("v");
                        if (effectiveRightsElement.hasAttribute("value")) {
                            value = effectiveRightsElement.getAttribute("value");
                        }

                        sr.setEffectiveRights(convertToDocareaRights(value));
                    }
                }

            }
            return getDocumentsWebScriptResult;
        } catch (Exception e) {

            throw new DocerException(e.getMessage());
        } finally {
            try {
                if (responseStream != null)
                    responseStream.close();
            } catch (IOException e) {

            }

            if (method != null) {
                method.releaseConnection();
            }
        }

    }

    private boolean IsTextualAlfProperty(String alfPropertyName) {
        PropertyDefinition pd = ALFRESCO_PROPERTIES_MAP.get(alfPropertyName);
        if (pd != null && pd.getDataType().matches(".+}(text|mltext)$")) {
            return true;
        }

        return false;
    }

    private void getAlfrescoAllProperties() {

        if (ALFRESCO_PROPERTIES_MAP.size() > 0) {
            return;
        }

        try {

            ClassPredicate predicate = new ClassPredicate();
            // predicate.setNames(new String[]{type});

            ClassDefinition[] def = WebServiceFactory.getDictionaryService().getClasses(null, null);

            for (ClassDefinition classDefinition : def) {

                PropertyDefinition[] propsDef = classDefinition.getProperties();
                if (propsDef != null) {
                    for (PropertyDefinition property : propsDef) {
                        // if (property.getName().startsWith("{" +
                        // DocerModel.DOCAREA_NAMESPACE_CONTENT_MODEL + "}")) {
                        ALFRESCO_PROPERTIES_MAP.put(property.getName(), property);
                        // }

                    }
                }
            }

        } catch (DictionaryFault e) {
            System.out.println("ERRORE getAlfrescoAllProperties: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("ERRORE getAlfrescoAllProperties: " + e.getMessage());
        }

    }

    private void populateAlfrescoProperties(Map<String, String> businessLogicProps, List<NamedValue> toPopulatePropList) throws DocerException {

        if (toPopulatePropList == null) {
            toPopulatePropList = new ArrayList<NamedValue>();
        }

        if (businessLogicProps == null) {
            return;
        }

        if (businessLogicProps.containsKey(it.kdm.docer.sdk.Constants.path)) {
            businessLogicProps.remove(it.kdm.docer.sdk.Constants.path);
        }

        for (String propName : businessLogicProps.keySet()) {

            // TODO: da scommentare quando attiviamo ereditarieta' multipla acl
            // ACL_EXPLICIT e' una campo interno al provider
            // if (propName.equalsIgnoreCase(ACL_EXPLICIT)) {
            // continue;
            // }

            MetaProperty mp = METAMAPPING.getMetaPropertyFromBusinessLogicName(propName);
            if (mp == null) {
                // continue;
                throw new DocerException("populateAlfrescoProperties: il metadato " + propName + " non e' mappato in configurazione");
            }

            String val = businessLogicProps.get(propName);

            NamedValue prop = new NamedValue();
            prop.setName(mp.getAlfFullPropName());

            if (val != null && mp.getIsUppercase()) {
                val = val.toUpperCase();
            } else if (val != null && mp.getIsLowercase()) {
                val = val.toLowerCase();
            }

            if (mp.getIsMultivalue()) {
                prop.setIsMultiValue(true);

                if (val == null) {
                    prop.setValues(null);
                } else {
                    if (val.equals("") && !IsTextualAlfProperty(prop.getName())) {
                        prop.setValues(null);
                    } else {

                        String[] appoArr = val.split(";");

                        List<String> values = new ArrayList<String>();
                        for (String str : appoArr) {
                            str = str.trim();
                            if (StringUtils.isEmpty(str)) {
                                continue;
                            }
                            values.add(str);
                        }
                        prop.setValues(values.toArray(new String[0]));

                    }
                }
            } else { // non e' multivalue

                prop.setIsMultiValue(false);
                if (val == null) {
                    prop.setValue(null);
                } else {
                    if (val.equals("") && !IsTextualAlfProperty(prop.getName())) {
                        prop.setValue(null);
                    } else {
                        prop.setValue(val);
                    }
                }

            }

            toPopulatePropList.add(prop);
        }
    }

    protected List<Reference> getDocerObjectsFromBin(long minNodeDbid, long maxNodeDbid) throws DocerException {

        List<Reference> results = new ArrayList<Reference>();

        // String search = "(TYPE:\"cm:content\" TYPE:\"cm:folder\")";
        // String search = "+@sys\\:node-dbid:[" +minNodeDbid +" TO "
        // +maxNodeDbid +"]";
        String search = "+@docarea\\:docnum:[" + minNodeDbid + " TO " + maxNodeDbid + "]";
        Query query = new Query();
        query.setStatement(search);
        QueryResult result = getAlfrescoArchiveRepositoryService_query(query);

        if (result.getResultSet().getTotalRowCount() < 1) {
            return results;
        }

        for (ResultSetRow rsr : result.getResultSet().getRows()) {

            // if(rsr.getNode().getType().indexOf("docarea")<0){
            // continue;
            // }
            if (!rsr.getNode().getType().startsWith("{http://www.docarea.it/model/content/1.0}")) {
                continue;
            }

            results.add(createReference(null, rsr.getNode().getId(), DocerModel.ARCHIVE_STORE));
        }

        return results;

    }

    protected void deleteFromBin(Reference ref) throws DocerException {

        Reference nodeRef = createReference(null, ref.getUuid(), DocerModel.ARCHIVE_STORE);
        Predicate delPredicate = createPredicate(nodeRef);

        CMLDelete cmlDelete = new CMLDelete();
        cmlDelete.setWhere(delPredicate);

        CML cml = new CML();
        cml.setDelete(new CMLDelete[]{cmlDelete});

        getAlfrescoRepositoryService_update(cml);

    }

    // private boolean arrayListContainsIgnoreCase(List<String> list, String
    // value) {
    //
    // if (list != null) {
    // for (int i = 0; i < list.size(); i++) {
    // String v = list.get(i);
    // if (v == null) {
    // continue;
    // }
    // if (v.equalsIgnoreCase(value)) {
    // return true;
    // }
    // }
    // }
    // return false;
    // }

    // protected void renameFile(String docId, Map<String, String> props) throws
    // DocerException {
    //
    // SearchResultSet result = getUniqueDocument(docId, false, false, false,
    // true, false, true);
    //
    // Reference docRef = result.getFirstAndOnly().getReference();
    //
    // CML cml = new CML();
    //
    // // proprieta' del nodo ente da creare
    // List<NamedValue> properties = new ArrayList<NamedValue>();
    //
    // populateAlfrescoProperties(props, properties);
    //
    // Predicate predicate = createPredicate(docRef);
    //
    // if (props != null) {
    //
    // String docname = props.get(it.kdm.docer.sdk.Constants.doc_docname);
    //
    // if (docname != null) {
    //
    // NamedValue propName = new NamedValue();
    // propName.setName(Constants.PROP_NAME);
    //
    // String nodeName = adjustNodeName(docname);
    //
    // propName.setValue(nodeName);
    // properties.add(propName);
    //
    // Reference parentReference = getParentReference(docRef);
    //
    // ParentReference parentParentReference = createParentReference(null,
    // parentReference.getUuid(), Constants.ASSOC_CONTAINS,
    // Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL,
    // nodeName));
    //
    // CMLMove move = new CMLMove();
    // move.setChildName(Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL,
    // nodeName));
    // move.setWhere(predicate);
    // move.setTo(parentParentReference);
    // cml.setMove(new CMLMove[] { move });
    //
    // }
    // }
    //
    // if (properties.size() == 0)
    // return;
    //
    // // CML update
    // CMLUpdate update = new CMLUpdate();
    // update.setWhere(predicate);
    // update.setProperty(properties.toArray(new NamedValue[0]));
    //
    // cml.setUpdate(new CMLUpdate[] { update });
    // // UpdateResult[] result = null;
    //
    // getAlfrescoRepositoryService_update(cml);
    //
    // }

    private List<ACE> mergeACL(Map<String, EnumACLRights> acl1, Map<String, EnumACLRights> acl2) {

        List<ACE> aces = new ArrayList<ACE>();

        if (acl1 == null && acl2 == null) {
            return aces;
        }

        Map<String, EnumACLRights> aclMerged = new HashMap<String, EnumACLRights>();

        if (acl1 != null && acl2 != null) {
            aclMerged.putAll(acl1);

            for (String alfUserOrGroup : acl2.keySet()) {
                EnumACLRights right2 = acl2.get(alfUserOrGroup);

                EnumACLRights right1 = aclMerged.get(alfUserOrGroup);

                // aggiungo diritto della acl2 se non presente nella acl1
                // oppure sovrascrivo se inferiore (con codice superiore...)
                if (right1 == null || right2.getCode() < right1.getCode()) {
                    aclMerged.put(alfUserOrGroup, right2);
                }
            }
        } else if (acl1 != null) {
            aclMerged.putAll(acl1);
        } else {
            aclMerged.putAll(acl2);
        }

        for (String alfUserOrGroup : aclMerged.keySet()) {
            ACE ace = new ACE();
            ace.setAuthority(alfUserOrGroup);
            ace.setPermission(convertToAlfrescoRights(aclMerged.get(alfUserOrGroup)));
            ace.setAccessStatus(AccessStatus.acepted);
            aces.add(ace);
        }

        return aces;
    }

    private Map<String, EnumACLRights> mergeACL2(Map<String, EnumACLRights> acl1, Map<String, EnumACLRights> acl2) {

        Map<String, EnumACLRights> aclMerged = new HashMap<String, EnumACLRights>();

        if (acl1 == null && acl2 == null) {
            return aclMerged;
        }

        if (acl1 != null && acl2 != null) {
            aclMerged.putAll(acl1);

            for (String alfUserOrGroup : acl2.keySet()) {
                EnumACLRights right2 = acl2.get(alfUserOrGroup);

                EnumACLRights right1 = aclMerged.get(alfUserOrGroup);

                // aggiungo diritto della acl2 se non presente nella acl1
                // oppure sovrascrivo se inferiore (con codice superiore...)
                if (right1 == null || right2.getCode() < right1.getCode()) {
                    aclMerged.put(alfUserOrGroup, right2);
                }
            }
        } else if (acl1 != null) {
            aclMerged.putAll(acl1);
        } else {
            aclMerged.putAll(acl2);
        }
        return aclMerged;
    }

    private Map<String, EnumACLRights> parseACLExplicit(String aclExplicit) throws DocerException {

        Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();

        if (StringUtils.isEmpty(aclExplicit)) {
            return acls;
        }

        String xacl = aclExplicit;

        xacl = xacl.replaceAll("^ *\\{ *", "");
        xacl = xacl.replaceAll(" *\\} *$", "");
        String[] aclexplicite = xacl.split(" *[,;] *");

        for (String aclex : aclexplicite) {

            String[] u = aclex.split(" *\\= *");

            if (u.length != 2) {
                continue;
            }

            String userOrGroup = u[0];
            String permission = u[1];

            acls.put(userOrGroup, convertToDocareaRights(permission));
        }

        return acls;
    }

    private String generateACLExplicitString(Map<String, EnumACLRights> acl) {

        if (acl == null || acl.size() == 0) {
            return "";
        }

        String aclExplicit = "";

        for (String authority : acl.keySet()) {

            String alfRight = convertToAlfrescoRights(acl.get(authority));
            if (alfRight == null) {
                continue;
            }

            aclExplicit += authority + "=" + alfRight + ";";
        }

        aclExplicit = aclExplicit.replaceAll(";$", "");

        return aclExplicit;
    }

    protected void testSetInherits(String docId, boolean inherits) throws DocerException {

        if (currentUser == null)
            throw new DocerException(-1113, "current user is null");

        try {

            SearchResultSet result = getUniqueDocument(docId, false, false, false, true, true, true);

            SearchResult srDoc = result.getFirstAndOnly();

            setInheritsRunasAdmin(srDoc.getReference(), false);

        } catch (Exception e) {
            throw new DocerException("testSetInherits: " + e.getMessage());
        }

    }

    private static boolean isValidHex(char ch1) {
        return (ch1 >= '0' && ch1 <= '9') || (ch1 >= 'A' && ch1 <= 'F') || (ch1 >= 'a' && ch1 <= 'f');
    }

    static String encode(String string) {

        StringBuilder sb = new StringBuilder();

	/*
     * for (int i=0; i<string.length(); i++) { char ch = string.charAt(i);
	 * if (ch == ESCAPE_CHAR || ch == '.') { sb.append(ESCAPE_CHAR); if (ch
	 * < 0X10) { sb.append('0'); } sb.append(Integer.toHexString(ch)); }
	 * else { sb.append(ch); } }
	 */

        sb.append(string.substring(0, string.length() - 1));
        char ch = string.charAt(string.length() - 1);
        if (ch == '.') {
            sb.append(ESCAPE_CHAR);
            if (ch < 0x10) {
                sb.append('0');
            }

            sb.append(Integer.toHexString(ch));
        } else {
            sb.append(ch);
        }

        return sb.toString();
    }

    static String decode(String string) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (ch == ESCAPE_CHAR) {
                char ch1 = string.charAt(++i);
                char ch2 = string.charAt(++i);
                if (isValidHex(ch1) && isValidHex(ch2)) {
                    String hexString = new String(new char[]{ch1, ch2});
                    sb.append((char) Integer.parseInt(hexString, 16));
                } else {
                    sb.append(ESCAPE_CHAR);
                    sb.append(ch1);
                    sb.append(ch2);
                }
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }


}
