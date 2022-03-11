package it.kdm.docer.alfresco.model;

//import java.util.HashMap;
//import java.util.Map;
//
//import javax.xml.namespace.QName;
//
//import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.Constants;

public interface DocerModel {

    public static String GROUP_ADMINS_ENTE_PREFIX = "ADMINS_ENTE_";
    public static String GROUP_ADMINS_AOO_PREFIX = "ADMINS_AOO_";

    public static String NAMESPACE_CONTENT_MODEL = Constants.NAMESPACE_CONTENT_MODEL;

    public static String NAMESPACE_SYSTEM_MODEL = Constants.NAMESPACE_SYSTEM_MODEL;
    public static final String MY_MODEL_PREFIX = "docarea";

    public static final String DOCAREA_NAMESPACE_CONTENT_MODEL = "http://www.docarea.it/model/content/1.0";

    public static final String ALFRESCO_DOWNLOAD_SERVLET_URL = "/download/attach/workspace/SpacesStore/";
    public static final String ALFRESCO_VERSION_DOWNLOAD_SERVLET_URL = "/download/attach/versionStore/version2Store/";
    public static final String DELETE_URL = "/wcservice/api/node/content/workspace/SpacesStore/";

    public static String PROP_ICON = "{http://www.alfresco.org/model/application/1.0}icon";

    public static String ICON_DEFAULT = "space-icon-default";
    public static String ICON_PREFIX = "space-icon-";

    public static String ENTE_LOCAL_ID = "ente";
    public static String AOO_LOCAL_ID = "aoo";
    public static String TITOLARIO_LOCAL_ID = "titolario";
    public static String AREATEMATICA_LOCAL_ID = "areatematica";
    public static String ANNO_FASCICOLO_LOCAL_ID = "fascicoloanno";
    public static String FASCICOLO_LOCAL_ID = "fascicolo";
    public static String SPACES_STORE_NAME = "SpacesStore";

    public static String DOCUMENT_TYPE_REPLACER = "/@DOCUMENT_TYPE/";
    public static String LIBRARY_REFERENCE_REPLACER = "/@LIBRARY_REFERENCE@/";
    public static String ENTE_REFERENCE_REPLACER = "/@ENTE_REFERENCE@/";
    public static String AOO_REFERENCE_REPLACER = "/@AOO_REFERENCE@/";
    public static String TITOLARIO_REFERENCE_REPLACER = "/@TITOLARIO_REFERENCE@/";
    public static String AREATEMATICA_REFERENCE_REPLACER = "/@AREATEMATICA_REFERENCE@/";
    public static String ANNOFASCICOLO_REFERENCE_REPLACER = "/@ANNOFASCICOLO_REFERENCE@/";
    public static String LIBRARY_REPLACER = "/@library@/";
    public static String PARAM_REPLACER = "/@param@/";
    public static String COD_ENTE_REPLACER = "/@codEnte@/";
    public static String COD_AOO_REPLACER = "/@codAoo@/";
    public static String COD_AREA_REPLACER = "/@codArea@/";

    public static String TYPE_ID_REPLACER = "/@TypeId@/";
    public static String NODE_UUID_REPLACER = "/@NODE_UUID@/";

    // public static String CODICE_REPLACER = "/@codTitolarioOrClassifica@/";

    public static String COD_TITOLARIO_REPLACER = "/@codTitolario@/";
    public static String CLASSIFICA_REPLACER = "/@classifica@/";

    public static String ANNO_FASCICOLO_REPLACER = "/@annoFascicolo@/";
    public static String NUM_FASCICOLO_REPLACER = "/@numFascicolo@/";
    public static String PROGR_FASCICOLO_REPLACER = "/@progrFascicolo@/";

    // public static String COD_PARENT_TITOLARIO_REPLACER =
    // "/@parentTitolario@/";

    public static String CUSTOM_ANAGR_LOCALID_REPLACER = "/@CustomType@/";
    public static String COD_CUSTOM_NAME_REPLACER = "/@CustomName@/";
    public static String COD_CUSTOM_VALUE_REPLACER = "/@codCustom@/";

    public static String DOCNUM_REPLACER = "/@docnum@/";

    public static Store WORKSPACE_STORE = new Store(Constants.WORKSPACE_STORE, SPACES_STORE_NAME);

    public static String ARCHIVE_STORE_NAME = "archive";
    public static Store ARCHIVE_STORE = new Store(ARCHIVE_STORE_NAME, SPACES_STORE_NAME);

    public static String VERSION_STORE_NAME = "version2Store";
    public static Store VERSION_STORE = new Store(Constants.WORKSPACE_STORE, VERSION_STORE_NAME);

    public static String ALFRESCO_UPLOAD_URL = "/upload/workspace/SpacesStore/";

    public static String ASPECT_WORKING_COPY = Constants.createQNameString(NAMESPACE_CONTENT_MODEL, "workingcopy");
    public static String ASPECT_DOCAREA_FOLDER = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "propsFolder");

    public static String PROP_NODEDBID = Constants.createQNameString(NAMESPACE_SYSTEM_MODEL, "node-dbid");

    public static String SEARCH_DOCUMENT_QUERY = "+TYPE:\"docarea:documento\" -ASPECT:\"cm:workingcopy\"";
    public static String SEARCH_QUERY_DOCER_FOLDER_DOCUMENTS = SEARCH_DOCUMENT_QUERY + " +PARENT:\"workspace://SpacesStore/" + NODE_UUID_REPLACER + "\"";

    //public static String SEARCH_QUERY_BY_TYPE = "+TYPE:\"" + DocerModel.MY_MODEL_PREFIX + ":" + TYPE_ID_REPLACER + "\" ";
    public static String SEARCH_QUERY_CM_FOLDER = "+TYPE:\"cm:folder" + "\"";
    public static String SEARCH_QUERY_DOCER_FOLDER = SEARCH_QUERY_CM_FOLDER + " +ASPECT:\"docarea:propsFolder\" ";

    public static String SEARCH_QUERY_ANNO_FASCICOLO = "+PARENT:\"workspace://SpacesStore/" + PARAM_REPLACER + "\"" + " +@cm\\:name:\"" + ANNO_FASCICOLO_REPLACER + "\" +TYPE:\"" + MY_MODEL_PREFIX
	    + ":" + ANNO_FASCICOLO_LOCAL_ID + "\"";
    public static String SEARCH_QUERY_CUSTOM_ANAGR_ROOT = "+PARENT:\"workspace://SpacesStore/" + PARAM_REPLACER + "\"" + " +@cm\\:name:\"" + CUSTOM_ANAGR_LOCALID_REPLACER + "\"";
    public static String SEARCH_QUERY_ENTE = "+TYPE:\"" + MY_MODEL_PREFIX + ":" + ENTE_LOCAL_ID + "\"" + " +@" + MY_MODEL_PREFIX + "\\:codEnte:\"" + COD_ENTE_REPLACER + "\"";
    public static String SEARCH_QUERY_AOO = "+TYPE:\"" + MY_MODEL_PREFIX + ":" + AOO_LOCAL_ID + "\"" + " +@" + MY_MODEL_PREFIX + "\\:codAoo:\"" + COD_AOO_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX
	    + "\\:codEnte:\"" + COD_ENTE_REPLACER + "\"";
    // public static String SEARCH_QUERY_AREA_TEMATICA = "+TYPE:\"" +
    // MY_MODEL_PREFIX + ":" + AREATEMATICA_LOCAL_ID + "\"" + " +@" +
    // MY_MODEL_PREFIX
    // + "\\:codArea:\"" + COD_AREA_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX +
    // "\\:codEnte:\"" + COD_ENTE_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX
    // + "\\:codAoo:\"" + COD_AOO_REPLACER + "\"";
    public static String SEARCH_QUERY_TITOLARIO_FROM_BOTH = "+TYPE:\"" + MY_MODEL_PREFIX + ":" + TITOLARIO_LOCAL_ID + "\"" + " +(+@" + MY_MODEL_PREFIX + "\\:codTitolario:\"" + COD_TITOLARIO_REPLACER
	    + "\" +@" + MY_MODEL_PREFIX + "\\:classifica:\"" + CLASSIFICA_REPLACER + "\") +@" + MY_MODEL_PREFIX + "\\:codEnte:\"" + COD_ENTE_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX + "\\:codAoo:\""
	    + COD_AOO_REPLACER + "\"";
    public static String SEARCH_QUERY_TITOLARIO_FROM_CODICE = "+TYPE:\"" + MY_MODEL_PREFIX + ":" + TITOLARIO_LOCAL_ID + "\" +@" + MY_MODEL_PREFIX + "\\:codTitolario:\"" + COD_TITOLARIO_REPLACER
	    + "\" +@" + MY_MODEL_PREFIX + "\\:codEnte:\"" + COD_ENTE_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX + "\\:codAoo:\"" + COD_AOO_REPLACER + "\"";
    public static String SEARCH_QUERY_TITOLARIO_FROM_CLASSIFICA = "+TYPE:\"" + MY_MODEL_PREFIX + ":" + TITOLARIO_LOCAL_ID + "\" +@" + MY_MODEL_PREFIX + "\\:classifica:\"" + CLASSIFICA_REPLACER
	    + "\" +@" + MY_MODEL_PREFIX + "\\:codEnte:\"" + COD_ENTE_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX + "\\:codAoo:\"" + COD_AOO_REPLACER + "\"";
    public static String SEARCH_QUERY_FASCICOLO_FROM_BOTH = "+TYPE:\"" + MY_MODEL_PREFIX + ":" + FASCICOLO_LOCAL_ID + "\"" + " +(+@" + MY_MODEL_PREFIX + "\\:numFascicolo:\"" + NUM_FASCICOLO_REPLACER
	    + "\" +@" + MY_MODEL_PREFIX + "\\:progressivoFascicolo:\"" + PROGR_FASCICOLO_REPLACER + "\")" + " +@" + MY_MODEL_PREFIX + "\\:annoFascicolo:\"" + ANNO_FASCICOLO_REPLACER + "\"" + " +@"
	    + MY_MODEL_PREFIX + "\\:codEnte:\"" + COD_ENTE_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX + "\\:codAoo:\"" + COD_AOO_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX + "\\:classifica:\""
	    + CLASSIFICA_REPLACER + "\"";
    public static String SEARCH_QUERY_FASCICOLO_FROM_NUMERO = "+TYPE:\"" + MY_MODEL_PREFIX + ":" + FASCICOLO_LOCAL_ID + "\"" + " +@" + MY_MODEL_PREFIX + "\\:numFascicolo:\"" + NUM_FASCICOLO_REPLACER
	    + "\" +@" + MY_MODEL_PREFIX + "\\:annoFascicolo:\"" + ANNO_FASCICOLO_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX + "\\:codEnte:\"" + COD_ENTE_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX
	    + "\\:codAoo:\"" + COD_AOO_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX + "\\:classifica:\"" + CLASSIFICA_REPLACER + "\"";
    public static String SEARCH_QUERY_FASCICOLO_FROM_PROGRESSIVO = "+TYPE:\"" + MY_MODEL_PREFIX + ":" + FASCICOLO_LOCAL_ID + "\"" + " +@" + MY_MODEL_PREFIX + "\\:progressivoFascicolo:\""
	    + PROGR_FASCICOLO_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX + "\\:annoFascicolo:\"" + ANNO_FASCICOLO_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX + "\\:codEnte:\"" + COD_ENTE_REPLACER + "\""
	    + " +@" + MY_MODEL_PREFIX + "\\:codAoo:\"" + COD_AOO_REPLACER + "\"" + " +@" + MY_MODEL_PREFIX + "\\:classifica:\"" + CLASSIFICA_REPLACER + "\"";
    public static String SEARCH_QUERY_CUSTOM_ANAGR = "+TYPE:\"" + MY_MODEL_PREFIX + ":" + CUSTOM_ANAGR_LOCALID_REPLACER + "\"" + " +@" + COD_CUSTOM_NAME_REPLACER + ":\"" + COD_CUSTOM_VALUE_REPLACER
	    + "\"" + " +@" + MY_MODEL_PREFIX + "\\:codEnte:\"" + COD_ENTE_REPLACER + "\" +@" + MY_MODEL_PREFIX + "\\:codAoo:\"" + COD_AOO_REPLACER + "\"";;
    public static String SEARCH_QUERY_GROUP_ROOT = "+TYPE:\"cm:authorityContainer\"";
    // public static String SEARCH_QUERY_GROUP_PARENT =
    // "PARENT:\"workspace://SpacesStore/" + PARAM_REPLACER + "\"";
    public static String SEARCH_QUERY_BY_GROUPID = "@cm\\:authorityName:\"GROUP_" + PARAM_REPLACER + "\"";
    public static String SEARCH_QUERY_BY_USERID = "@cm\\:userName:\"" + PARAM_REPLACER + "\"";
    public static String SEARCH_QUERY_USER_ROOT = "+TYPE:\"cm:person\"";
    public static String SEARCH_QUERY_WORKSPACE = "+PATH:\"/app:company_home\"";
    public static String SEARCH_QUERY_BY_DOCNUM = "+@" + MY_MODEL_PREFIX + "\\:docnum:\"" + DOCNUM_REPLACER + "\"";
    // public static String SEARCH_DEFAULT_PROFILE = "+@" + MY_MODEL_PREFIX +
    // "\\:docnum:\"" + DOCNUM_REPLACER + "\" +TYPE:\"" + MY_MODEL_PREFIX + ":"
    // + DOCUMENT_TYPE_REPLACER + "\"";

    public static String PATH_LIBRARY = "/app:company_home/cm:" + LIBRARY_REPLACER;
    public static String PATH_ENTE = "/app:company_home/cm:" + LIBRARY_REPLACER + "/cm:" + COD_ENTE_REPLACER;
    public static String PATH_AOO = "/app:company_home/cm:" + LIBRARY_REPLACER + "/cm:" + COD_ENTE_REPLACER + "/cm:" + COD_AOO_REPLACER;
    public static String PATH_DOCUMENTI = "/app:company_home/cm:" + LIBRARY_REPLACER + "/cm:" + COD_ENTE_REPLACER + "/cm:" + COD_AOO_REPLACER + "/cm:DOCUMENTI";

    // public static String LINKS_PREFIX = "link_";

    // public static String ASPECT_RELATED = NAMESPACE_PREFIX_DOCAREA_CHILDNAME
    // + "related";
    public static String ASPECT_RELATED = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "related");

    public static String ASSOC_RELATED = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "related");
    public static String ASSOC_RIFERIMENTO = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "riferimento");
    public static String ASSOC_ADVANCED_VERSION = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "advancedVersion");

    public static String TYPE_ENTE = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "ente");
    public static String TYPE_AOO = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "aoo");
    public static String TYPE_AREATEMATICA = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "areatematica");
    public static String TYPE_TITOLARIO = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "titolario");
    public static String TYPE_ANNOFASCICOLO = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "fascicoloanno");
    public static String TYPE_FASCICOLO = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "fascicolo");

    public static String PROP_ENABLED = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "enabled");
    public static String PROP_ENABLED_PREFIXED = "docarea:enabled";

    public static String PROP_AUTOVERSION = Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, "autoVersion");
    public static String PROP_AUTOVERSION_UPDATE = Constants.createQNameString(DocerModel.NAMESPACE_CONTENT_MODEL, "autoVersionOnUpdateProps");

    public static String DOCUMENTI_SPACE_NAME = "DOCUMENTI";

    public static String PROP_GROUP_GROUPID = Constants.createQNameString(NAMESPACE_CONTENT_MODEL, "authorityName");
    public static String PROP_GROUP_PARENTGROUPID = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "parentGroupId");
    public static String PROP_GROUP_GRUPPOSTRUTTURA = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "gruppoStruttura");
    public static String PROP_GROUP_GROUPNAME = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "nomeGruppo");

    // public static String PROP_GROUP_GROUPID_PREFIXED = "cm:authorityName";
    // public static String PROP_GROUP_PARENTGROUPID_PREFIXED =
    // "docarea:parentGroupId";
    // public static String PROP_GROUP_GRUPPOSTRUTTURA_PREFIXED =
    // "docarea:gruppoStruttura";
    // public static String PROP_GROUP_GROUPNAME_PREFIXED =
    // "docarea:nomeGruppo";

    public static String PROP_USER_USERID = Constants.createQNameString(NAMESPACE_CONTENT_MODEL, "userName");
    public static String PROP_USER_MAIL = Constants.createQNameString(NAMESPACE_CONTENT_MODEL, "email");
    public static String PROP_USER_FIRSTNAME = Constants.createQNameString(NAMESPACE_CONTENT_MODEL, "firstName");
    public static String PROP_USER_LASTNAME = Constants.createQNameString(NAMESPACE_CONTENT_MODEL, "lastName");
    public static String PROP_USER_FULLNAME = Constants.createQNameString(NAMESPACE_CONTENT_MODEL, "middleName");
    public static String PROP_USER_COD_ENTE = Constants.createQNameString(NAMESPACE_CONTENT_MODEL, "organization");
    public static String PROP_USER_COD_AOO = Constants.createQNameString(NAMESPACE_CONTENT_MODEL, "location");
    public static String PROP_USER_COD_FISCALE = Constants.createQNameString(NAMESPACE_CONTENT_MODEL, "jobtitle");


    // public static String PROP_USER_USERID_PREFIXED = "cm:userName";
    // public static String PROP_USER_MAIL_PREFIXED = "cm:email";
    // public static String PROP_USER_FIRSTNAME_PREFIXED = "cm:firstName";
    // public static String PROP_USER_LASTNAME_PREFIXED = "cm:lastName";
    // public static String PROP_USER_FULLNAME_PREFIXED = "cm:middleName";
    // public static String PROP_USER_COD_ENTE_PREFIXED = "cm:organization";
    // public static String PROP_USER_COD_AOO_PREFIXED = "cm:location";

    public static String PROP_USER_NETWORK_ALIAS = Constants.createQNameString(DOCAREA_NAMESPACE_CONTENT_MODEL, "networkAlias");
    public static String PROP_USER_NETWORK_ALIAS_PREFIXED = "docarea:networkAlias";
}
