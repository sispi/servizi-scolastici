package it.kdm.docer.providers.solr4.bl;

/**
 * Created by microchip on 05/05/15.
 */
public class Fields {
    public static class Base {
        public static String SOLR_ID = "id";
        public static String SOLR_PARENT = "parent";
        public static String COD_AOO = "COD_AOO";
        public static String COD_ENTE = "COD_ENTE";
        public static String ENABLED = "ENABLED";
        public static String ACL = "acl_explicit";
        public static String LOCK = "lock_to";
        public static String LOCKED_BY = "MODIFIER";
        public static String EFFECTIVE_RIGHTS = "[user_profiles]";

    }

    public static class Related {
        public static String RELATED = "related";
        public static String COD_AOO = "COD_AOO";
        public static String COD_ENTE = "COD_ENTE";
    }

    public static class Ente {
        public static String COD_ENTE = "COD_ENTE";
        public static String DES_ENTE = "DES_ENTE";
        public static String ENABLED = "ENABLED";
    }

    public static class Aoo {
        public static String COD_ENTE = "COD_ENTE";
        public static String COD_AOO = "COD_AOO";
        public static String DES_AOO = "DES_AOO";
        public static String ENABLED = "ENABLED";
    }

    public static class User extends Base {
        public static String SYSTEM_ID = "USER_ID";
        public static String USER_ID = "USER_ID";
        public static String FULL_NAME = "FULL_NAME";
        public static String USER_PASSWORD = "USER_PASSWORD";
        public static String FIRST_NAME = "FIRST_NAME";
        public static String LAST_NAME = "LAST_NAME";
        public static String EMAIL_ADDRESS = "EMAIL_ADDRESS";
        public static String NETWORK_ALIAS = "NETWORK_ALIAS";
        public static String GROUPS = "groups";
        public static String USER_PREFIX = "USER_";
    }

    public static class Group extends Base {
        public static String SYSTEM_ID = "GROUP_ID";
        public static String GROUP_ID = "GROUP_ID";
        public static String GROUP_NAME = "GROUP_NAME";
        public static String PARENT_GROUP_ID = "PARENT_GROUP_ID";
        public static String GRUPPO_STRUTTURA = "GRUPPO_STRUTTURA";
        public static String GROUP_PREFIX = "GROUP_";
    }

    public static class Folder extends Base {
        public static String SYSTEM_ID = "FOLDER_ID";
        public static String FOLDER_ID = "FOLDER_ID";
        public static String FOLDER_NAME = "FOLDER_NAME";
        public static String FOLDER_OWNER = "FOLDER_OWNER";
        public static String DES_FOLDER = "DES_FOLDER";
        public static String PARENT_FOLDER_ID = "PARENT_FOLDER_ID";
    }

    public static class Fascicolo extends Base {
        public static String PROGRESSIVO= "PROGR_FASCICOLO";
        public static String ANNO_FASCICOLO = "ANNO_FASCICOLO";
        public static String PIANO_CLASS = "PIANO_CLASS";
        public static String CLASSIFICA = "CLASSIFICA";
        public static String NUM_FASCICOLO = "NUM_FASCICOLO";
        public static String DES_FASCICOLO = "DES_FASCICOLO";
        public static String PARENT_PROGRESSIVO = "PARENT_PROGR_FASCICOLO";
        public static String DATA_APERTURA = "DATA_APERTURA";
        public static String DATA_CHIUSURA = "DATA_CHIUSURA";
        public static String CF_PERSONA = "CF_PERSONA";
        public static String CF_AZIENDA = "CF_AZIENDA";
        public static String ID_IMMOBILE = "ID_IMMOBILE";
        public static String ID_PROC = "ID_PROC";
    }

    public static class Titolario extends Base {
        public static String DES_TITOLARIO = "DES_TITOLARIO";
        public static String CLASSIFICA = "CLASSIFICA";
        public static String PARENT_CLASSIFICA = "PARENT_CLASSIFICA";
        public static String PIANO_CLASS = "PIANO_CLASS";
        public static String CLASSIFICA_SORT = "classifica_sort";
    }

    public static class CustomItem extends Base {
//        public static String COD_CUSTOM = "COD_CUSTOM";
        public static String TYPE = "type";
    }

    public static class Documento extends Base {
        public static String SYSTEM_ID = "DOCNUM";
        public static String DOC_URL = "DOC_URL";
        public static String DOCNUM = "DOCNUM";
        public static String DOCNAME = "DOCNAME";
        public static String DOCTYPE = "TYPE_ID";
        public static String DESCRIZIONE = "ABSTRACT";
        public static String RIFERIMENTI = "riferimenti";
        public static String CONTENT_SIZE = "content_size";
        public static String VERSIONS = "[versions]";
        public static String PARENT_FOLDER_ID = "PARENT_FOLDER_ID";
        public static String FASC_SECONDARI = "FASC_SECONDARI";
    }
}