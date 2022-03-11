package it.kdm.docer.sdk;

public class Constants {

	//nomi dei metadati relativi ad un documento
	public static final String doc_cod_ente = "COD_ENTE";	
	
	public static final String doc_stato_conservazione = "STATO_CONSERV";
	public static final String doc_docnum = "DOCNUM";
	public static final String doc_docnum_record = "DOCNUM_RECORD";
	public static final String doc_stato_pantarei = "STATO_PANTAREI";
	public static final String doc_stato_archivistico = "STATO_ARCHIVISTICO";
	public static final String doc_docname = "DOCNAME";
	public static final String doc_abstract = "ABSTRACT";
	public static final String doc_type_id = "TYPE_ID";
	public static final String doc_type_id_des = "TYPE_ID_DES";
	public static final String doc_creation_date = "CREATION_DATE";
	
	public static final String doc_archive_type = "ARCHIVE_TYPE";
	public static final String doc_doc_url = "DOC_URL";
	public static final String doc_version = "DOC_VERSION";

	//public static final String doc_schema_id = "SCHEMA_ID";	
	//public static final String doc_author_id = "AUTHOR_ID";
	//public static final String doc_typist_id = "TYPIST_ID";
//	public static final String doc_app_id = "APP_ID";
	//public static final String doc_default_extension= "DEFAULT_EXTENSION";
	public static final String doc_ranking = "RANKING";
	
	public static final String doc_tipo_componente = "TIPO_COMPONENTE";
	public static final String doc_docnum_princ = "DOCNUM_PRINC";
	
	public static final String doc_cod_aoo = "COD_AOO";	
	
	//public static final String doc_areatematica= "COD_AREA";
	
	public static final String doc_cod_titolario = "COD_TITOLARIO"; // *** deprecated

    public static final String doc_classifica= "CLASSIFICA";
    public static final String doc_piano_class= "PIANO_CLASS";
	public static final String doc_progr_fascicolo = "PROGR_FASCICOLO";
	
	
	public static final String doc_anno_fascicolo = "ANNO_FASCICOLO";
	public static final String doc_num_fascicolo = "NUM_FASCICOLO"; // *** deprecated
	public static final String doc_fascicoli_secondari = "FASC_SECONDARI";
	
	public static final String doc_protocollo_anno = "ANNO_PG";
	public static final String doc_protocollo_numero = "NUM_PG";
	public static final String doc_protocollo_oggetto = "OGGETTO_PG";
	public static final String doc_protocollo_registro = "REGISTRO_PG";
	public static final String doc_protocollo_data = "DATA_PG";
	public static final String doc_protocollo_tipo_protocollazione = "TIPO_PROTOCOLLAZIONE";
	// nuovi metadati di protocollo
    public static final String doc_protocollo_num_pg_mittente = "NUM_PG_MITTENTE";
    public static final String doc_protocollo_data_pg_mittente = "DATA_PG_MITTENTE";
    public static final String doc_protocollo_cod_ente_mittente = "COD_ENTE_MITTENTE";
    public static final String doc_protocollo_cod_aoo_mittente = "COD_AOO_MITTENTE";
    public static final String doc_protocollo_classifica_mittente = "CLASSIFICA_MITTENTE";
    public static final String doc_protocollo_fascicolo_mittente = "FASCICOLO_MITTENTE";
    public static final String doc_protocollo_annullato_protocollo = "ANNULLATO_PG";
    public static final String doc_protocollo_data_annullamento_protocollo = "D_ANNULL_PG";
    public static final String doc_protocollo_motivo_annullamento_protocollo = "M_ANNULL_PG";
    public static final String doc_protocollo_provvedimento_annullamento_protocollo = "P_ANNULL_PG";

    // metadati condivisi tra protocollazione e registrazione
	public static final String doc_reg_e_proto_mittenti = "MITTENTI";
	public static final String doc_reg_e_proto_destinatari = "DESTINATARI";
	public static final String doc_reg_e_proto_tipo_firma = "TIPO_FIRMA";
	public static final String doc_reg_e_proto_firmatario = "FIRMATARIO";
	
	public static final String doc_registrazione_numero = "N_REGISTRAZ";
	public static final String doc_registrazione_data = "D_REGISTRAZ";
	public static final String doc_registrazione_anno = "A_REGISTRAZ";
	public static final String doc_registrazione_oggetto = "O_REGISTRAZ";
	public static final String doc_registrazione_id_registro = "ID_REGISTRO";	
	// nuovi metadati di registrazione
	public static final String doc_registrazione_annullata_registrazione = "ANNULL_REGISTRAZ";
	public static final String doc_registrazione_data_annullamento_registrazione = "D_ANNULL_REGISTRAZ";
	public static final String doc_registrazione_motivo_annullamento_registrazione = "M_ANNULL_REGISTRAZ";
	public static final String doc_registrazione_provvedimento_annullamento_registrazione = "P_ANNULL_REGISTRAZ";

	
	public static final String doc_pubblicazione_registro = "REGISTRO_PUB";
	public static final String doc_pubblicazione_numero = "NUMERO_PUB";
	public static final String doc_pubblicazione_anno = "ANNO_PUB";
	public static final String doc_pubblicazione_oggetto = "OGGETTO_PUB";
	public static final String doc_pubblicazione_data_inizio = "DATA_INIZIO_PUB";
	public static final String doc_pubblicazione_data_fine = "DATA_FINE_PUB";
	public static final String doc_pubblicazione_pubblicato = "PUBBLICATO";
    public static final String doc_pubblicazione_annullata = "ANNULL_PUB";
    public static final String doc_pubblicazione_data_annullamento = "D_ANNULL_PUB";
    public static final String doc_pubblicazione_history = "HISTORY_PUB";

	public static final String doc_pubblicazione_motivo_annullamento = "M_ANNULL_PUB";
	//nomi dei metadati relativi ad un utente
	public static final String user_user_id = "USER_ID";
	public static final String user_full_name = "FULL_NAME";
	public static final String user_first_name = "FIRST_NAME";
	public static final String user_last_name = "LAST_NAME";
	
	public static final String user_network_alias = "NETWORK_ALIAS";
	public static final String user_user_password = "USER_PASSWORD";
	public static final String user_email_address = "EMAIL_ADDRESS";
	public static final String user_enabled = "ENABLED";
	
	public static final String user_cod_ente = "COD_ENTE";
	public static final String user_cod_aoo = "COD_AOO";

    public static final String user_cod_fiscale = "CODICE_FISCALE";


//	public static final String user_secondary_group_id = "SECONDARY_GROUP_ID";
//	public static final String user_primary_group_id = "PRIMARY_GROUP_ID";
	
	
	//nomi dei metadati relativi ad un gruppo
	public static final String group_group_id = "GROUP_ID";
	public static final String group_group_name = "GROUP_NAME";
	public static final String group_parent_group_id = "PARENT_GROUP_ID";
	public static final String group_enabled = "ENABLED";
	public static final String group_gruppo_struttura = "GRUPPO_STRUTTURA";
	
	public static final String ente_cod_ente = "COD_ENTE";
	public static final String ente_des_ente = "DES_ENTE";
	public static final String ente_enabled = "ENABLED";
	public static final String ente_type_id = "ENTE";
	
	public static final String aoo_cod_aoo = "COD_AOO";
	public static final String aoo_des_aoo = "DES_AOO";
	public static final String aoo_enabled = "ENABLED";
	public static final String aoo_cod_ente = "COD_ENTE";
	public static final String aoo_type_id = "AOO";


    public static final String titolario_piano_class = "PIANO_CLASS";
    public static final String titolario_classifica = "CLASSIFICA";
	public static final String titolario_parent_classifica = "PARENT_CLASSIFICA";
	public static final String titolario_cod_titolario = "COD_TITOLARIO";
	public static final String titolario_des_titolario = "DES_TITOLARIO";	
	public static final String titolario_cod_ente = "COD_ENTE";
	public static final String titolario_cod_aoo = "COD_AOO";
	public static final String titolario_enabled = "ENABLED";
	public static final String titolario_type_id = "TITOLARIO";
	
	
	public static final String fascicolo_progr_fascicolo = "PROGR_FASCICOLO";
	public static final String fascicolo_parent_progr_fascicolo = "PARENT_PROGR_FASCICOLO";
	public static final String fascicolo_num_fascicolo = "NUM_FASCICOLO";
	public static final String fascicolo_anno_fascicolo = "ANNO_FASCICOLO";
	public static final String fascicolo_des_fascicolo = "DES_FASCICOLO";	
	public static final String fascicolo_cod_ente = "COD_ENTE";
	public static final String fascicolo_cod_aoo = "COD_AOO";
	//public static final String fascicolo_cod_titolario = "COD_TITOLARIO";
    public static final String fascicolo_classifica = "CLASSIFICA";
    public static final String fascicolo_piano_class = "PIANO_CLASS";
	public static final String fascicolo_enabled = "ENABLED";
	public static final String fascicolo_type_id = "FASCICOLO";

	public static final String folder_cod_ente = "COD_ENTE";
	public static final String folder_cod_aoo = "COD_AOO";
	public static final String folder_folder_id = "FOLDER_ID";
	public static final String folder_folder_name = "FOLDER_NAME";
	public static final String folder_owner = "FOLDER_OWNER";
	public static final String folder_des_folder = "DES_FOLDER";
	public static final String folder_parent_folder_id = "PARENT_FOLDER_ID";
	//public static final String folder_folder_path = "FOLDER_PATH";
	public static final String inherits_acl = "INHERITS_ACL";
	public static final String path = "PATH";
               
	//public static final String doc_fascicolo_descrizione = "DES_FASCICOLO";		
	//public static final String doc_areatematica_descrizione = "DES_AOO";	
	
	public static final String custom_cod_ente = "COD_ENTE";
	public static final String custom_cod_aoo = "COD_AOO";
	//public static final String custom_cod_custom_prefix = "COD_";
	//public static final String custom_type_regex_replace = "^" +custom_cod_custom_prefix;	
	//public static final String custom_des_custom_prefix = "DES_";
	public static final String custom_enabled = "ENABLED";	
	public static final String anagrafica_type_id = "TYPE_ID";

	public static final String no_piano_class = "NO_PIANO";
}
