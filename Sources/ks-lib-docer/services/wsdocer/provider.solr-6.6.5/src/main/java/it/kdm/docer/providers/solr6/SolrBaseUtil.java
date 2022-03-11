package it.kdm.docer.providers.solr6;

import com.google.common.base.Strings;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.providers.solr6.bl.Configuration;
import it.kdm.docer.providers.solr6.bl.Fields;
import it.kdm.docer.sdk.Constants;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.classes.*;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.*;
import it.kdm.solr.client.SolrClient;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;
import it.kdm.utils.exceptions.DataTableException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrRequest;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by microchip on 04/05/15.
 */
public class SolrBaseUtil {


    public static final long DEFAULT_LOCK_DURATION = 1000 * 60 * 60 * 24 * 365 * 10; //10 anni
//    //Lista dei tipi
//    public enum SolrObjectType {
//        TYPE_ENTE,
//        TYPE_AOO,
//        TYPE_DOCUMENTO,
//        TYPE_FASCICOLO,
//        TYPE_TITOLARIO,
//        TYPE_FOLDER,
//        TYPE_LOCATION,
//        TYPE_GENERIC_NODE,
//        TYPE_USER,
//        TYPE_GROUP,
//        TYPE_RELATED
//    }

    public static final String TYPE_ENTE = "ente";
    public static final String TYPE_AOO = "aoo";
    public static final String TYPE_DOCUMENTO = "documento";
    public static final String TYPE_FASCICOLO = "fascicolo";
    public static final String TYPE_TITOLARIO = "titolario";
    public static final String TYPE_FOLDER = "folder";
    public static final String TYPE_LOCATION = "location";
    public static final String TYPE_GENERIC_NODE = "node";
    public static final String TYPE_USER = "user";
    public static final String TYPE_GROUP = "group";
    public static final String TYPE_RELATED = "related";
    public static final String TYPE_VERSIONS = "versions";
    public static final String DATE_ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final int MAX_ROWS =10000;
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_ISO_FORMAT);


    public enum SolrExecuteAction {INSERT,UPDATE,DELETE,DELETE_ALL}

    static Properties config;

    private String location="";

    static Logger log = org.slf4j.LoggerFactory.getLogger(SolrBaseUtil.class.getName());

    static Lock lockCreateUpdate = new ReentrantLock();

    public SolrBaseUtil() throws DocerException {
        //cache della configurazione
        if (config==null)
            config = loadConfiguration();

        this.location = config.getProperty(Configuration.Keys.LOCATION);
    }

    public String paddingClassifica(String classifica) {
        String pattern = "(?<!\\d)(\\d)(?!\\d)";
        String value = classifica.replaceAll(pattern,"0$1");

        return value;
    }

    public String buildMd5Password(String value) throws NoSuchAlgorithmException {
        String usePasswordMd5 = config.getProperty(Configuration.Keys.USE_PASSWORD_MD5);
        String secret = config.getProperty(Configuration.Keys.SECRET);

        if (usePasswordMd5.equalsIgnoreCase("TRUE"))
            return buildMd5(value+secret);

        return value;
    }

    public String buildMd5(String value) throws NoSuchAlgorithmException {
        return DigestUtils.md5Hex(value);
        /*
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(value.getBytes(Charset.forName("UTF8")));
        final byte[] resultByte = messageDigest.digest();
        final String result = new String(Hex.encodeHex(resultByte));

        return result;
        */
    }

    public void deleteAll(ILoggedUserInfo currentUser) throws DocerException {
        solrExecuteDeleteByQuery(currentUser, "*", "*", "*");

    }

    public void createLocation(ILoggedUserInfo currentUser, String location) throws DocerException {
        SolrInputDocument metadata = new SolrInputDocument();
        metadata.setField("name",location);

        solrInsert(currentUser, location + "!@location", metadata);
    }

    public String getLocation() {
        return location;
    }

//    public List<Map<String, String>> genericSearch(ILoggedUserInfo currentUser, String type, Map<String, List<String>> searchCriteria, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderBy) throws DocerException {
//
//        SolrDocumentList results = solrSearch(currentUser, type, searchCriteria, null, returnProperties, maxResults, orderBy);
//
//        //converte solr results in DataTable<String>
//        List<Map<String, String>> mapList = this.convertSolrToMapList(results);
//
//        return mapList;
//    }



//    public DataTable<String> genericSearch(ILoggedUserInfo currentUser, String type, Map<String, List<String>> searchCriteria, List<String> keywords, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderBy) throws DocerException {
//
//        SolrDocumentList results = solrSearch(currentUser, type, searchCriteria, keywords, returnProperties, maxResults, orderBy);
//
//        try {
//            //converte solr results in DataTable<String>
//            DataTable<String> dataTable = this.convertSolrToDataTable(results);
//
//            return dataTable;
//
//        } catch (DataTableException e) {
//            throw new DocerException(e);
//        }
//
//
//    }


    public String extractIdItemFromSolrId(String solrId) {
        final String sidRegex = ".+[\\.!]([^!@]+).*|([^\\.!@]+)[!@]+[^!@]+";

        String sid = solrId.replaceFirst( sidRegex,"$1$2" );

        return sid;
    }

    public String getSolrId(IEnteId enteId) {
        //ENTE: location.codEnte!@ente
        String format = "%s.%s!@ente";

        return String.format(format, this.getLocation(), enteId.getCodiceEnte());
    }

    public String getSolrId(IAOOId aooId) {
        //AOO: location.codEnte!codAoo!@aoo
        String format = "%s.%s!%s!@aoo";

        return String.format(format, this.getLocation(), aooId.getCodiceEnte(), aooId.getCodiceAOO());
    }

    public String getSolrId(IFascicoloId fascicoloId) {
        //FASCICOLO: location.codEnte!codAoo!classifica|annoFascicolo|progressivoFascicolo@fascicolo
        String format = "%s.%s!%s!%s@fascicolo";

        String formatFascicolo = "%s|%s|%s";

        // DOCER-36 Piano di classificazione: location.codEnte!codAoo!classifica$piano|annoFascicolo|progressivoFascicolo@fascicolo
        String formatFascicoloPC = "%s$%s|%s|%s";
        String idFascicolo = null;
        if (!StringUtils.isEmpty(fascicoloId.getPianoClassificazione()))
            idFascicolo = String.format(formatFascicoloPC, encode(fascicoloId.getClassifica()), fascicoloId.getPianoClassificazione(), fascicoloId.getAnnoFascicolo(), encode(fascicoloId.getProgressivo()));
        else
            idFascicolo = String.format(formatFascicolo, encode(fascicoloId.getClassifica()), fascicoloId.getAnnoFascicolo(), encode(fascicoloId.getProgressivo()));

        return String.format(format, this.getLocation(), fascicoloId.getCodiceEnte(), fascicoloId.getCodiceAOO(), idFascicolo);
    }

    public String getSolrId(ITitolarioId titolarioId) {
        //TITOLARIO: location.codEnte!codAoo!classifica@titolario
        String format = "%s.%s!%s!%s@titolario";
        // DOCER-36 Piano di classificazione: location.codEnte!codAoo!classifica$piano@titolario
        String formatPC = "%s.%s!%s!%s$%s@titolario";

        if (!StringUtils.isEmpty(titolarioId.getPianoClassificazione()))
            return String.format(formatPC, this.getLocation(), titolarioId.getCodiceEnte(), titolarioId.getCodiceAOO(), encode(titolarioId.getClassifica()) , titolarioId.getPianoClassificazione());
        else
            return String.format(format, this.getLocation(), titolarioId.getCodiceEnte(), titolarioId.getCodiceAOO(), encode(titolarioId.getClassifica()));
    }

    public String getSolrId(String docId, Map<String, String> metaDati) {
        //DOCUMENTO: location.codEnte!codAoo!docnum@documento
        String format = "%s.%s!%s!%s@documento";

        return String.format(format, this.getLocation(), metaDati.get("COD_ENTE"), metaDati.get("COD_AOO"), docId);
    }

    public String getSolrId(String folderId, IFolderInfo folderNewInfo) {
        //FOLDER: location.codEnte!codAoo!folderId@folder
        String format = "%s.%s!%s!%s@folder";

        return String.format(format, this.getLocation(), folderNewInfo.getCodiceEnte(), folderNewInfo.getCodiceAOO(), folderId);
    }

    public String getSolrId(ICustomItemId customItemId) {
        //ANAGRAFICA_CUSTOM: location.codEnte!codAoo!customId@anagraficaCustom
        String format = "%s.%s!%s!%s@%s";

        return String.format(format, this.getLocation(), customItemId.getCodiceEnte(), customItemId.getCodiceAOO(), encode(customItemId.getCodiceCustom()), customItemId.getType());
    }

    public String getSolrId(IUserProfileInfo userNewInfo, String codEnte) {
        //USER: userid@user
        String format = "%s@user";

        return String.format(format, encode(userNewInfo.getUserId()));
    }

    public String getSolrId(IGroupProfileInfo groupNewInfo, String codEnte) {
        //GROUP: groupid@group
        String format = "%s@group";

        return String.format(format, encode(groupNewInfo.getGroupId()));
    }

    static final String regexLong = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[-+].*$";
    static final String regexShort = "^\\d{4}-\\d{2}-\\d{2}T$";
    static final String regexInterval = "^\\[.*\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[-+]\\d{2}\\:?\\d{2}.*\\]$";
    static final String regexReplaceInterval = "[-+]\\d{2}\\:?\\d{2}";
    static final String regexStupid = "1971-01-02T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[-+]\\d{2}\\:?\\d{2}";
    public SolrInputDocument buildExtraInfo(SolrInputDocument actualMetadata, Map<String,String> extraProps) {

        String val = "";

        String regexpFields = config.getProperty("solr.mv.fields","^(.*_IS|.*_SS|.*_LS|.*_BS|.*_FS|.*_DS|.*_DTS)$");
        String regexpSep = config.getProperty("solr.mv.separator",",");

        for (String key : extraProps.keySet()) {
            val = extraProps.get(key);
            if (val!=null) {
                if (Pattern.matches(regexLong, val)) {
                    val = val.substring(0, 23) + "Z";
                } else if (Pattern.matches(regexShort, val)) {
                    val = val + "00:00:00.000Z";
                }

                if (key.matches(regexpFields)) {
                    String[] pars = val.split(regexpSep);
                    actualMetadata.setField(key.toUpperCase(), pars);
                    continue;
                }

            }

            actualMetadata.setField(key.toUpperCase(), val);
        }

        return actualMetadata;
    }

    private String encode(String text) {
        HashSet<Character> charsToEncode = new HashSet<Character>();
        //"\\/:*?\"<>!.@"
        charsToEncode.add('\\');
        charsToEncode.add('/');
        charsToEncode.add(':');
        charsToEncode.add('*');
        charsToEncode.add('?');
        charsToEncode.add('"');
        charsToEncode.add('<');
        charsToEncode.add('>');
        charsToEncode.add('|');
        charsToEncode.add('.');
        charsToEncode.add('@');

        StringBuilder builder = new StringBuilder();
        for(int i=0; i<text.length(); i++) {
            char ch = text.charAt(i);
            if (charsToEncode.contains(ch)) {
                builder.append('%');
                if (ch < 0x10) {
                    builder.append('0');
                }
                builder.append(Integer.toHexString(ch));
            } else {
                builder.append(ch);
            }
        }

        return builder.toString();
    }

    private Properties loadConfiguration() throws DocerException {
     
        Properties prop = new Properties();
        
        try {
            
        	prop = ConfigurationUtils.loadProperties("solr.properties");
        } catch (IOException e) {
            throw new DocerException("Impossibile caricare il file di configurazione solr.properties.",e);
        }

        return prop;
    }

    public NamedList<?> solrDeleteById(ILoggedUserInfo currentUser, String solrId) throws DocerException {
        return solrExecuteDeleteById(currentUser,solrId);
    }

    public NamedList<?> solrDelete(ILoggedUserInfo currentUser, String type, String fieldName, String fieldValue) throws DocerException {
        return solrExecuteDeleteByQuery(currentUser,type,fieldName,fieldValue);
    }

//    public NamedList<?> solrDelete(ILoggedUserInfo currentUser, String solrId) throws DocerException {
//        return solrExecuteUpdate(currentUser,solrId,null,SolrExecuteAction.DELETE);
//    }

    public NamedList<?> solrUpdate(ILoggedUserInfo currentUser, String solrIdOrType, SolrInputDocument metadata) throws DocerException {
        return solrExecuteUpdate(currentUser,solrIdOrType,metadata,SolrExecuteAction.UPDATE, null);
    }

    public NamedList<?> solrUpdate(ILoggedUserInfo currentUser, String solrIdOrType, SolrInputDocument metadata, InputStream documentFile, Map<String,String> params) throws DocerException {
        metadata.setField("state","content");
        return solrExecuteUpdate(currentUser,solrIdOrType,metadata,SolrExecuteAction.UPDATE, documentFile,params);
    }

//    public NamedList<?> solrInsert(ILoggedUserInfo currentUser, SolrObjectType type, SolrInputDocument metadata) throws DocerException {
//        return solrExecuteUpdate(currentUser,null,metadata,SolrExecuteAction.INSERT);
//    }

    public NamedList<?> solrInsert(ILoggedUserInfo currentUser, String solrIdOrType, SolrInputDocument metadata, InputStream documentFile) throws DocerException {
        metadata.setField("state","content");
        return solrExecuteUpdate(currentUser, solrIdOrType, metadata, SolrExecuteAction.INSERT, documentFile);
    }

    public NamedList<?> solrInsert(ILoggedUserInfo currentUser, String solrIdOrType, SolrInputDocument metadata) throws DocerException {
        return solrExecuteUpdate(currentUser,solrIdOrType, metadata,SolrExecuteAction.INSERT, null);
    }

    public NamedList<?> solrExecuteUpdate(ILoggedUserInfo currentUser, String solrIdOrType, SolrInputDocument metadata, SolrExecuteAction mode, InputStream documentFile) throws DocerException {
        return solrExecuteUpdate(currentUser,solrIdOrType,metadata,mode,documentFile,null);
    }

    public synchronized NamedList<?> solrExecuteUpdate(ILoggedUserInfo currentUser, String solrIdOrType, SolrInputDocument metadata, SolrExecuteAction mode, InputStream documentFile, Map<String,String> params) throws DocerException {

        lockCreateUpdate.lock();

        try {
            SolrClient cli = SolrClient.getInstance();
            SolrClient.Request req;

            String solrId = null;
            String type = null;

            String currentType = "";

            if (solrIdOrType.contains("@")) {
                solrId = solrIdOrType;
                currentType = solrId.split("@")[1];
            } else {
                type = solrIdOrType;
                currentType = type;
            }


            //gestione fascicoli secondari: imposta acl_parents con id dei fascicoli secondari
            if (metadata.containsKey(Fields.Documento.FASC_SECONDARI)) {
                if (solrId == null)
                    throw new DocerException(400, "Parametri non validi. FASC_SECONDARI non atteso.");

                Collection<String> acl_parents = null;
                String fSecondari = (String) metadata.getFieldValue(Fields.Documento.FASC_SECONDARI);
                if (!Strings.isNullOrEmpty(fSecondari)) {
                    acl_parents = new ArrayList<String>();
                    //estrarre ente, aoo
                    String ente = currentUser.getCodiceEnte();
                    String aoo = solrId.split("!")[1];
                    //split fasc secondari
                    String[] secondari = fSecondari.split(";");
                    for (String f : secondari) {
                        Pattern pattern = Pattern.compile("(.*)\\/(\\d{4})\\/(.*)");
                        Matcher matcher = pattern.matcher(f);
                        if (matcher.find()) {
                            IFascicoloId fid = new FascicoloId();
                            fid.setCodiceEnte(ente);
                            fid.setCodiceAOO(aoo);
                            fid.setClassifica(matcher.group(1));
                            fid.setAnnoFascicolo(matcher.group(2));
                            fid.setProgressivo(matcher.group(3));
                            String id = getSolrId(fid);

                            acl_parents.add(id);
                        } else {
                            throw new DocerException(400, "Parametri non validi. FASC_SECONDARI in formato errato.");
                        }
                    }
                }
                metadata.setField("acl_parents", acl_parents);
            }

            //sistema i valori dei codici nei metatdati
            metadata = finalizeMetaValue(metadata, currentType);

            //gestisce i metadati vuoti per Solr
            for (String meta : metadata.getFieldNames()) {
                Object value = metadata.getFieldValue(meta);
                if (value == null || "".equals(value))
                    metadata.setField(meta, Collections.singletonMap("set", null));
            }

            //switch della modalità di esecuzione (insert o update)
            if (mode.equals(SolrExecuteAction.INSERT)) {
                req = new SolrClient.Request("/create");
            } else {
                req = new SolrClient.Request("/update");
            }

            if (mode.equals(SolrExecuteAction.DELETE_ALL)) {
                req.deleteByQuery("*:*");
            } else if (mode.equals(SolrExecuteAction.DELETE)) {
                req.deleteByQuery("id:" + solrId);
            } else {
                //campi per identificare l'item
                if (solrId != null) {
                    metadata.setField("id", solrId);
                } else {
                    metadata.setField("type", type);
                }
//            SolrInputDocument history = new SolrInputDocument();
//            history.setField("id", UUID.randomUUID().toString()+"@history");
//            history.setField("name", UUID.randomUUID().toString());
                req.add(metadata);
//            req.add(history);
            }

            if (documentFile != null) {
                req.setStream(documentFile);
            }

            req.setParam("ticket", currentUser.getTicket());

            if (params != null) {
                for (String paramKey : params.keySet())
                    req.setParam(paramKey, params.get(paramKey));
            }

            try {
//            if (mode.equals(SolrExecuteAction.UPDATE)) {
//                //TODO: verificare se corretto il retrieve della location con un getObject ineve che dal solrId
//                SolrDocument item = getSolrObject(currentUser, solrId, Collections.singletonList("location"));
//                String location = (String) item.get("location");
//                metadata.setField("location", location);
//            }

                NamedList<?> result = cli.request(req);

                return result;

            } catch (SolrServerException e) {
                throw new DocerException(e);
            } catch (IOException e) {
                throw new DocerException(e);
            }
        } catch (DocerException de) {
            throw de;
        } catch (Exception e) {
            throw new DocerException(e);
        } finally {
            lockCreateUpdate.unlock();
        }

    }

    private SolrInputDocument finalizeMetaValue(SolrInputDocument metadata, String type) {
        List<String> metaList = new ArrayList<String>();
        metaList.add(Fields.Fascicolo.PROGRESSIVO);
        //metaList.add(Fields.Titolario.CLASSIFICA);
        metaList.add("COD_"+type);

        for (String meta : metaList)
            if (metadata.containsKey(meta)) {
                metadata.setField(meta, metadata.getFieldValue(meta).toString().toUpperCase());
            }

        return metadata;
    }

    public NamedList<?> solrExecuteDeleteById(ILoggedUserInfo currentUser, String solrId) throws DocerException {

        SolrClient cli = SolrClient.getInstance();
        UpdateRequest req = new UpdateRequest();

        req.setParam("ticket",currentUser.getTicket());
        req.setAction(AbstractUpdateRequest.ACTION.COMMIT, true,true,true);

        req.deleteById(solrId);

        try {
            NamedList<?> result = cli.request(req);

            return result;

        } catch (SolrServerException e) {
            throw new DocerException(e);
        } catch (IOException e) {
            throw new DocerException(e);
        }

    }

    public NamedList<?> solrExecuteDeleteByQuery(ILoggedUserInfo currentUser, String objectType, String fieldName, String fieldValue) throws DocerException {

        SolrClient cli = SolrClient.getInstance();
        UpdateRequest req = new UpdateRequest();

        req.setParam("ticket",currentUser.getTicket());
        req.setAction(AbstractUpdateRequest.ACTION.COMMIT, true,true,true);

        req.deleteByQuery(fieldName + ":" + fieldValue);

        try {
            NamedList<?> result = cli.request(req);

            return result;

        } catch (SolrServerException e) {
            throw new DocerException(e);
        } catch (IOException e) {
            throw new DocerException(e);
        }

    }

    public SolrDocument getSolrObjectByFieldId(ILoggedUserInfo currentUser, String type, String id, String fieldName) throws DocerException  {
        return getSolrObjectByFieldId(currentUser, type, id,fieldName, Collections.singletonList("*"));
    }
    public SolrDocument getSolrObjectByFieldId(ILoggedUserInfo currentUser, String type, String value, String fieldName, List<String> returnProperties) throws DocerException {
        return getSolrObjectByFieldId(currentUser, type, Collections.singletonList(value),fieldName,returnProperties);
    }

    public SolrDocument getSolrObjectByFieldId(ILoggedUserInfo currentUser, String type, List<String> values, String fieldName, List<String> returnProperties) throws DocerException  {
        Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
        searchCriteria.put(fieldName, values);
        searchCriteria.put("type", Collections.singletonList(type));

        //forza ricerca anche per gli object eliminati in Solr (logicamente)
        searchCriteria.put(Constants.custom_enabled, Collections.singletonList("*"));

        SolrDocumentList list = solrSearch(currentUser, null, searchCriteria, null, returnProperties, 1, null);

        if (list.size()==0) {
            throw new DocerException(404,"L'elemento non può essere trovato. Id inesistente nel Sistema.");
        }

        return list.get(0);
    }

    public SolrDocument getSolrObject(ILoggedUserInfo currentUser, String solrId) throws DocerException {
        return getSolrObject(currentUser, solrId, null);
    }

    public SolrDocument getSolrObject(ILoggedUserInfo currentUser, String solrId, List<String> returnProperties) throws DocerException {
        SolrClient cli = SolrClient.getInstance();
        SolrQuery query = new SolrQuery();
        query.set("ticket",currentUser.getTicket());

        query.set("id", solrId);

        if (returnProperties!=null) {
            for (String retField : returnProperties) {
                query.addField(retField);
            }
        }

        SolrDocumentList results;
        try {
            SolrDocument item = cli.get(query);

            return item;

        } catch (SolrServerException e) {
            throw new DocerException(e);
        }
    }

    public ContentStreamBase getSolrStream(ILoggedUserInfo currentUser, String solrId, String version) throws DocerException {
        SolrClient cli = SolrClient.getInstance();
        SolrQuery query = new SolrQuery();
        query.set("ticket",currentUser.getTicket());

        query.set("id", solrId);
        query.set("getVersion", version);

        SolrDocumentList results;
        try {
            final InputStream stream = cli.getStream(query);

            ContentStreamBase cstream = new ContentStreamBase() {
                @Override
                public InputStream getStream() throws IOException {
                    return stream;
                }
            };

            return cstream;

        } catch (SolrServerException e) {
            throw new DocerException(e);
        }
    }

    public SolrDocumentList filterDeleteDocs(ILoggedUserInfo currentUser, Collection<Object> docIds) throws DocerException {
        SolrClient cli = SolrClient.getInstance();
        SolrQuery query = new SolrQuery();
        query.set("ticket",currentUser.getTicket());

        query.addField(Fields.Documento.DOCNUM);

        String q = "";
        for (Object docid:docIds) {
            if (!Strings.isNullOrEmpty(q))
                q+="OR";

            q+=" id:" + docid + " ";
        }

        query.setQuery(q);
        query.setRows(MAX_ROWS);

        SolrDocumentList results;
        try {
            results = cli.query(query, SolrRequest.METHOD.POST).getResults();

            return results;

        } catch (SolrServerException e) {
            throw new DocerException(e);
        } catch (IOException e) {
            throw new DocerException(e);
        }
    }

    public SolrDocumentList solrSearchByQuery(ILoggedUserInfo currentUser, String q, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderBy) throws DocerException {
        return solrSearch(currentUser,null,null,null,returnProperties,maxResults,orderBy,null,q);
    }

    public SolrDocumentList solrSearch(ILoggedUserInfo currentUser, String type, Map<String, List<String>> searchCriteria, List<String> keywords, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderBy) throws DocerException {
        return solrSearch(currentUser,type,searchCriteria,keywords,returnProperties,maxResults,orderBy,null,null);
    }

    public SolrDocumentList solrSearch(ILoggedUserInfo currentUser, String type, Map<String, List<String>> searchCriteria, List<String> keywords, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderBy, Map<String,String> params, String customQuery) throws DocerException {

        SolrClient cli = SolrClient.getInstance();
        SolrQuery query = new SolrQuery();
        query.set("ticket",currentUser.getTicket());
        query.set("shortCircuit",false);

        //aggiunta filtro implicito per ente se presente
//        if (!Strings.isNullOrEmpty(currentUser.getCodiceEnte())) {
//            String fq = SolrClient.Query.makeClause(Fields.Base.COD_ENTE, currentUser.getCodiceEnte());
//            query.addFilterQuery(fq);
//        }

        if (orderBy==null)
            orderBy = new HashMap<String, EnumSearchOrder>();

        if (params!=null) {
            for (String paramKey : params.keySet())
                query.set(paramKey,params.get(paramKey));
        }

//        if (searchCriteria==null)
//            throw new DocerException(-5000, "search criteria cannot be null");

        if (returnProperties==null)
            throw new DocerException(-5000, "return properties cannot be null");


        if (maxResults>=0)
            query.setRows(maxResults);
        else
            query.setRows(MAX_ROWS);

        for (String retField : returnProperties) {
            query.addField( (retField.equalsIgnoreCase("acl_explicit") || retField.equalsIgnoreCase("content_size")) ? retField.toLowerCase() : retField);
        }

        String q = "";
        if (customQuery==null) {
            if (type != null) {
                q = SolrClient.Query.makeClause("type", type);
                if (!type.equalsIgnoreCase(TYPE_GROUP) && !type.equalsIgnoreCase(TYPE_USER))
                    q += SolrClient.Query.makeClause("location", this.getLocation());
            }
            main:
            for (String searchField : searchCriteria.keySet()) {
                List<String> value = new ArrayList<String>();

                //gestione del formato delle date
                List<String> valueList = searchCriteria.get(searchField);
                String val = "";
                for (String v : valueList) {
                    val = v;
                    if (Pattern.matches(regexInterval, val)) {
                        val = val.replaceFirst(regexStupid,"*");
                        val = val.replaceAll(regexReplaceInterval,"Z");
                        q += " +"+searchField+":"+val+" ";
                        continue main;
                    } else if (Pattern.matches(regexLong, val)) {
                        val = val.substring(0,23)+"Z";
                    } else if (Pattern.matches(regexShort, val)) {
                        val = val+"00:00:00.000Z";
                    }
                    value.add(val);
                }

                //gestione dei metadati vuoti verso solr
                //List<String> value = searchCriteria.get(searchField);
                String listValue = StringUtils.join(value, "");
                if (value.size()==0 || listValue.equalsIgnoreCase("")) {
                    q += SolrClient.Query.makeClause(searchField, (Object) null);
                    continue;
                }

                //gestione dei metadati speciali che iniziano con $
                if (searchField.startsWith("$")) {

                    if (searchField.equalsIgnoreCase("$ORDER_BY")) {
                        List<String> sv = searchCriteria.get(searchField);
                        for (String order : sv) {
                            String[] pars = order.split("=");
                            if (pars[1].equalsIgnoreCase("ASC")) {
                                orderBy.put(pars[0],EnumSearchOrder.ASC);
                            } else {
                                orderBy.put(pars[0],EnumSearchOrder.DESC);
                            }
                        }
                        continue;
                    }

                    if (searchField.equalsIgnoreCase("$MAX_RESULTS") || searchField.equalsIgnoreCase("$MAX_ROWS")) {
                        maxResults = Integer.parseInt(searchCriteria.get(searchField).get(0));
                        continue;
                    }

                }

                //q += SolrClient.Query.makeClause(searchField, searchCriteria.get(searchField).toArray());
                // acl_explicit deve essere minuscolo
                if (searchField.equalsIgnoreCase("acl_explicit"))
                    searchField = "acl_explicit";

                //switch dell'handler di solr in caso di ENABLED=*
                if (searchField.equalsIgnoreCase("ENABLED") && value.contains("*")) {
                    query.setRequestHandler("/selectall");
                    continue;
                }

                q += SolrClient.Query.makeClause(searchField, value.toArray());
            }
        } else {
            q = customQuery;
        }

        if (keywords!=null) {
            for (String key : keywords) {
                q += SolrClient.Query.makeTextClause(key);
            }
        }

        if (orderBy!=null) {
            for (String field : orderBy.keySet()) {

                //switch fieldName
                String fieldName = field;
                if (fieldName.equalsIgnoreCase("RANKING"))
                    fieldName = "score";

                EnumSearchOrder enumOrder = orderBy.get(field);
                if (enumOrder.equals(EnumSearchOrder.ASC))
                    query.addSort(fieldName, SolrQuery.ORDER.asc);
                else
                    query.addSort(fieldName, SolrQuery.ORDER.desc);
            }
        }

        //imposta i criteri
        query.setQuery(q);

        SolrDocumentList results;
        try {
            results = cli.query(query, SolrRequest.METHOD.POST ).getResults();

            //se la ricerca è per DOCNUM
            if(searchCriteria !=null && results.size()==0) {

                if (searchCriteria.containsKey("DOCNUM")) {
                    String codAoo = config.getProperty(Configuration.Keys.DEFAULT_COD_AOO);
                    if (codAoo != null) {

                        String DOCNUM = searchCriteria.get("DOCNUM").get(0);
                        String codEnte = currentUser.getCodiceEnte();


                        log.error("FIXDOCNONTROVATO || Ricerca per documento fallita: DOCNUM: " + DOCNUM);

                        Map<String, String> map = new HashMap<String, String>();
                        map.put("COD_AOO", codAoo);
                        map.put("COD_ENTE", codEnte);

                        String id = getSolrId(DOCNUM, map);

                        log.info("FIXDOCNONTROVATO || Tento per id: " + id);
                        log.info("FIXDOCNONTROVATO || Con il ticket: " + currentUser.getTicket());

                        query.clear();
                        query.set("qt", "/get");
                        query.set("ticket", currentUser.getTicket());
                        query.set("shortCircuit", false);
                        query.set("id", id);
                        try {
                            SolrDocument doc = (SolrDocument) cli.query(query).getResponse().get("doc");
                            if (doc != null) {
                                //problema scavalcato....
                                log.info("FIXDOCNONTROVATO || Ricerca riuscita per id: " + id);
                                results.add(doc);
                            } else {
                                //c'è ancora il problema....
                                log.error("FIXDOCNONTROVATO || Ricerca fallita per id: " + id);
                            }
                        } catch (SolrServerException e) {
                            log.error("FIXDOCNONTROVATO || Ricerca fallita per id: " + id);
                            log.error("FIXDOCNONTROVATO || La ricerca ha causato l'eccezione: " + e);
                        }

                    }
                }
            }

            return results;

        } catch (SolrServerException e) {
            throw new DocerException(e);
        } catch (IOException e) {
            throw new DocerException(e);
        }

    }

    public EnumACLRights convertSolrACLRights(String rights) throws DocerException {

        if (rights==null)
            return EnumACLRights.undefined;

        try {
            return EnumACLRights.valueOf(rights);
        } catch (Exception e) {
            throw new DocerException(e);
        }

    }

    public List<String> convertRelatedListToDocer(Collection<Object> related) {
        List<String> out = new ArrayList<String>();

        if (related==null)
            return out;

        String re = ".*!(.*)@.*";
        for (Object rel : related) {
            out.add(rel.toString().replaceAll(re, "$1"));
        }

        return out;
    }

    public Map<String,String> getUserOrGroup(ILoggedUserInfo currentUser, List<String> idList) throws DocerException {
        if (idList == null || idList.size() == 0)
            return new HashMap<String, String>();
        String q = "(%s) OR (%s) OR (%s)";
        String user_id_clause = SolrClient.Query.makeClause("USER_ID", idList.toArray());
        String group_id_clause = SolrClient.Query.makeClause("GROUP_ID", idList.toArray());
        String admin_group_id_clause = SolrClient.Query.makeClause("ADMIN_GROUP_ID", idList.toArray());

        q = String.format(q, user_id_clause,group_id_clause, admin_group_id_clause);

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(Fields.Base.SOLR_ID);
        returnProperties.add("sid");
        returnProperties.add("type");
        returnProperties.add("ADMIN_GROUP_ID");

        SolrDocumentList userList = solrSearchByQuery(currentUser, q, returnProperties, -1, null);

        Map<String,String> out = new HashMap<String, String>();
        for (String u : idList) {
            for (SolrDocument item : userList) {

                if (item.getFieldValue("type").equals("user") && item.getFieldValue("sid").equals(u)) {
                    out.put((String) item.getFieldValue("sid"), item.getFieldValue("sid").toString()+"@user");
                    break;
                }

                if (item.getFieldValue("type").equals("group") && item.getFieldValue("sid").equals(u)) {
                    out.put((String) item.getFieldValue("sid"), item.getFieldValue("sid").toString()+"@group");
                    break;
                }

                if (item.getFieldValue("type").equals("aoo") && item.getFieldValue("sid").equals(u)) {
                    out.put((String) item.getFieldValue("sid"), item.getFieldValue("sid").toString()+"@group");
                    break;
                }

                if (item.getFieldValue("type").equals("ente") && item.getFieldValue("sid").equals(u)) {
                    out.put((String) item.getFieldValue("sid"), item.getFieldValue("sid").toString()+"@group");
                    break;
                }

                if (item.getFieldValue("type").equals("aoo") && item.getFieldValue("ADMIN_GROUP_ID").equals(u)) {
                    out.put((String) item.getFieldValue("ADMIN_GROUP_ID"), item.getFieldValue("ADMIN_GROUP_ID").toString()+"@group");
                    break;
                }

                if (item.getFieldValue("type").equals("ente") && item.getFieldValue("ADMIN_GROUP_ID").equals(u)) {
                    out.put((String) item.getFieldValue("ADMIN_GROUP_ID"), item.getFieldValue("ADMIN_GROUP_ID").toString()+"@group");
                    break;
                }
            }
        }

        return out;
    }

    public List<String> convertMapToSolrACL(ILoggedUserInfo currentUser, Map<String,EnumACLRights> aclList) throws DocerException {
        List<String> acls = new ArrayList<String>();

        if (aclList==null)
            return acls;

        List<String> keys = new ArrayList(aclList.keySet());
        Map<String,String> userGroup = getUserOrGroup(currentUser,keys);

        for (String key : aclList.keySet()) {

//            String solrType="";
//            String type = key.substring(0,1);
//            String userGroup = key.substring(1);
//
//            if (!type.equalsIgnoreCase("U") && !type.equalsIgnoreCase("G"))
//                throw new DocerException("Valore ACL non valido ("+key+") per essere settato.");
//
//            if (type.equalsIgnoreCase("U"))
//                solrType="@user";
//
//            if (type.equalsIgnoreCase("G"))
//                solrType="@group";
//
//            String recordAcl = userGroup+solrType+":"+((EnumACLRights)aclList.get(key)).toString();
            String recordAcl = userGroup.get(key)+":"+((EnumACLRights)aclList.get(key)).toString();
            acls.add(recordAcl);
        }

        return acls;
    }

    public List<String> convertSolrCollectionToMap(Collection<Object> versions) {
        List<String> list = new ArrayList<String>();

        if (versions==null)
            return list;

        for (Object val : versions) {
            list.add(val.toString());
        }

        return list;
    }

    public Map<String,EnumACLRights> convertSolrACLToMap(Collection<Object> aclList) {
        Map<String,EnumACLRights> acls = new HashMap<String, EnumACLRights>();

        if (aclList==null)
            return acls;

        String pattern = "([^!@]+).*[^:]+:(.*)";

        for (Object acl : aclList) {
            String[] pars = acl.toString().replaceAll( pattern , "$1,$2" ).split(",");
//            if (!pars[1].equalsIgnoreCase("u"))
//                pars[1]="g";

//            String userGroup = pars[1].toUpperCase()+pars[0];
            String userGroup = pars[0];
            acls.put(userGroup, EnumACLRights.valueOf(pars[1]));
        }

        return acls;
    }

    public IUserInfo convertSolrDocumentToUserInfo(SolrDocument item) {
        IUserInfo uInfo = new UserInfo();
        IUserProfileInfo info = new UserProfileInfo();
        Map<String,String> extra = new HashMap<String, String>();

        for (String key : item.keySet()) {
            if (key.equalsIgnoreCase(Fields.User.GROUPS)) {
                List<String> groups = new ArrayList<String>();
                Collection<Object> groupList = item.getFieldValues(Fields.User.GROUPS);
                for (Object k : groupList)
                        groups.add(k.toString().split("@")[0]);

                uInfo.setGroups(groups);
            }
            else if (key.equalsIgnoreCase(Fields.User.USER_ID))
                info.setUserId((String) item.get(Fields.User.USER_ID));
            else if (key.equalsIgnoreCase(Fields.User.USER_PASSWORD))
                info.setUserPassword((String) item.get(Fields.User.USER_PASSWORD));
            else if (key.equalsIgnoreCase(Fields.User.LAST_NAME))
                info.setLastName((String) item.get(Fields.User.LAST_NAME));
            else if (key.equalsIgnoreCase(Fields.User.FIRST_NAME))
                info.setFirstName((String) item.get(Fields.User.FIRST_NAME));
            else if (key.equalsIgnoreCase(Fields.User.FULL_NAME))
                info.setFullName((String) item.get(Fields.User.FULL_NAME));
            else if (key.equalsIgnoreCase(Fields.User.EMAIL_ADDRESS))
                info.setEmailAddress((String) item.get(Fields.User.EMAIL_ADDRESS));
            else if (key.equalsIgnoreCase(Fields.User.NETWORK_ALIAS))
                info.setNetworkAlias((String) item.get(Fields.User.NETWORK_ALIAS));
            else if (key.equalsIgnoreCase(Fields.User.ENABLED)) {
                if (item.get(Fields.User.ENABLED)!=null && item.get(Fields.User.ENABLED).toString().equalsIgnoreCase("true"))
                    info.setEnabled(EnumBoolean.TRUE);

                if (item.get(Fields.User.ENABLED)!=null && item.get(Fields.User.ENABLED).toString().equalsIgnoreCase("false"))
                    info.setEnabled(EnumBoolean.FALSE);

                if (item.get(Fields.User.ENABLED)!=null && item.get(Fields.User.ENABLED).toString().equalsIgnoreCase("UNSPECIFIED"))
                    info.setEnabled(EnumBoolean.UNSPECIFIED);

                if (item.get(Fields.User.ENABLED)==null)
                    info.setEnabled(EnumBoolean.UNSPECIFIED);

            } else {
                //TODO: verificare la gestione delle extraInfo perchè cosi non si riesce a distinguere i metadati
                //extrainfo da quelli di solr
                if (item.get(key) instanceof String && isDocerField(key))
                    extra.put(key,(String)item.get(key));
            }
        }

        info.setExtraInfo(extra);
        uInfo.setProfileInfo(info);

        return uInfo;
    }

    public IGroupInfo convertSolrDocumentToGroupInfo(SolrDocument item) {
        IGroupInfo uInfo = new GroupInfo();
        IGroupProfileInfo info = new GroupProfileInfo();
        Map<String,String> extra = new HashMap<String, String>();

        for (String key : item.keySet()) {
            if (key.equalsIgnoreCase(Fields.User.GROUPS)) {
                List<String> users = new ArrayList<String>();
                //TODO: parsare la lista degli utenti per il gruppo
                uInfo.setUsers(users);
            }
            else if (key.equalsIgnoreCase(Fields.Group.PARENT_GROUP_ID))
                info.setParentGroupId((String) item.get(Fields.Group.PARENT_GROUP_ID));
            else if (key.equalsIgnoreCase(Fields.Group.GROUP_ID))
                info.setGroupId((String) item.get(Fields.Group.GROUP_ID));
            else if (key.equalsIgnoreCase(Fields.Group.GROUP_NAME))
                info.setGroupName((String) item.get(Fields.Group.GROUP_NAME));
            else if (key.equalsIgnoreCase(Fields.Group.ENABLED)) {
                if (item.get(Fields.Group.ENABLED)!=null && item.get(Fields.Group.ENABLED).toString().equalsIgnoreCase("true"))
                    info.setEnabled(EnumBoolean.TRUE);

                if (item.get(Fields.Group.ENABLED)!=null && item.get(Fields.Group.ENABLED).toString().equalsIgnoreCase("false"))
                    info.setEnabled(EnumBoolean.FALSE);

                if (item.get(Fields.Group.ENABLED)!=null && item.get(Fields.Group.ENABLED).toString().equalsIgnoreCase("UNSPECIFIED"))
                    info.setEnabled(EnumBoolean.UNSPECIFIED);

                if (item.get(Fields.Group.ENABLED)==null)
                    info.setEnabled(EnumBoolean.UNSPECIFIED);

            } else {
                //TODO: verificare la gestione delle extraInfo perchè cosi non si riesce a distinguere i metadati
                //extrainfo da quelli di solr
                if (item.get(key) instanceof String && isDocerField(key))
                    extra.put(key,(String)item.get(key));
            }
        }

        info.setExtraInfo(extra);
        uInfo.setProfileInfo(info);

        return uInfo;
    }

    private boolean isDocerField(String key) {
        return Character.isUpperCase(key.charAt(0));
    }

    public DataTable<String> convertSolrToDataTable(SolrDocumentList records, List<String> returnProperties) throws DataTableException {
        DataTable<String> dtResult = new DataTable<String>();

        if (dtResult.getColumnNames().size() == 0) {
            //dataTable non inizializzata con le colonne
            dtResult = createDataTableStructure(returnProperties);
        }
        String sep = config.getProperty("solr.mv.separator",",");
        for (Iterator iterator = records.iterator(); iterator.hasNext();) {

            Map rec = (Map) iterator.next();

            DataRow<String> row = dtResult.newRow();

            for (String colName : returnProperties) {
                String value = "";
                if (rec.containsKey(colName)) {
                    if (rec.get(colName) instanceof Date) {
                        value = simpleDateFormat.format(rec.get(colName));
                    } else {
                        //gestione Array e collection
                        if (rec.get(colName) instanceof Collection) {
                            Object[] arr = ((Collection)rec.get(colName)).toArray();
                            value = StringUtils.join(arr,sep);
                        } else if (rec.get(colName) instanceof Object[]) {
                            value = StringUtils.join((Object[])rec.get(colName),sep);
                        } else {
                            //gestione single value
                            value = rec.get(colName).toString();
                        }
                    }
                } else // Caso a parte per ACL_EXPLICIT che su SolR è in minuscolo
                    if (rec.containsKey(colName.toLowerCase()))
                        value = rec.get(colName.toLowerCase()).toString();

                row.put(colName.toUpperCase(), value);
            }

            dtResult.addRow(row);
        }

        return dtResult;
    }

    private DataTable<String> createDataTableStructure(List<String> returnProps) {
        DataTable<String> dtResult = new DataTable<String>();

        for (Object columnName : returnProps) {

            if (dtResult.getColumnNames().contains(columnName.toString().toUpperCase())) {
                continue;
            }

            dtResult.getColumnNames().add(columnName.toString().toUpperCase());

        }

        return dtResult;
    }

    public List<Map<String, String>> convertSolrToMapList(SolrDocumentList records) {
        List<Map<String, String>> dtResult = new ArrayList<Map<String, String>>();

        for (Iterator iterator = records.iterator(); iterator.hasNext();) {

            Map rec = (Map) iterator.next();

            Map<String, String> map = new HashMap<String, String>();

            for (Object key : rec.keySet()) {
                map.put(key.toString().toUpperCase(), rec.get(key).toString());
            }

            dtResult.add(map);
        }

        return dtResult;
    }

    public List<IUserProfileInfo> convertSolrToUserList(SolrDocumentList records) {
        List<IUserProfileInfo> dtResult = new ArrayList<IUserProfileInfo>();

        for (Iterator iterator = records.iterator(); iterator.hasNext();) {

            Map rec = (Map) iterator.next();

            IUserProfileInfo obj = new UserProfileInfo();

            Map<String,String> extra = new HashMap<String, String>();
            obj.setExtraInfo(extra);
            for (Object key : rec.keySet()) {
                //TODO: COD_AOO per USER???
//                if (key.toString().equalsIgnoreCase(Fields.User.COD_AOO))
//                    obj.set(rec.get(key).toString());

                //TODO: COD_ENTE per USER???
//                if (key.toString().equalsIgnoreCase(Fields.User.COD_ENTE))
//                    obj.set(rec.get(key).toString());

                //TODO: NETWORK_ALIAS per USER???
//                if (key.toString().equalsIgnoreCase(Fields.User.NETWORK_ALIAS))
//                    obj.setNetworkAlias(rec.get(key).toString());

                if (key.toString().equalsIgnoreCase(Fields.User.EMAIL_ADDRESS))
                    obj.setEmailAddress(rec.get(key).toString());

                else if (key.toString().equalsIgnoreCase(Fields.User.ENABLED))
                    obj.setEnabled(convertBoolean(rec.get(key).toString()));

                else if (key.toString().equalsIgnoreCase(Fields.User.FIRST_NAME))
                    obj.setFirstName(rec.get(key).toString());

                else if (key.toString().equalsIgnoreCase(Fields.User.LAST_NAME))
                    obj.setLastName(rec.get(key).toString());

                else if (key.toString().equalsIgnoreCase(Fields.User.FULL_NAME))
                    obj.setFullName(rec.get(key).toString());

                else if (key.toString().equalsIgnoreCase(Fields.User.USER_PASSWORD))
                    obj.setUserPassword(rec.get(key).toString());

                else if (key.toString().equalsIgnoreCase(Fields.User.USER_ID))
                    obj.setUserId(rec.get(key).toString());

                else if (key.toString().startsWith(Fields.User.USER_PREFIX))
                    obj.getExtraInfo().put(key.toString(),rec.get(key).toString());

            }

            dtResult.add(obj);
        }

        return dtResult;

    }

    public List<IGroupProfileInfo> convertSolrToGroupList(SolrDocumentList records) {

        List<IGroupProfileInfo> dtResult = new ArrayList<IGroupProfileInfo>();

        for (Iterator iterator = records.iterator(); iterator.hasNext();) {

            Map rec = (Map) iterator.next();

            IGroupProfileInfo obj = new GroupProfileInfo();

            Map<String,String> extra = new HashMap<String, String>();
            obj.setExtraInfo(extra);
            for (Object key : rec.keySet()) {

                if (key.toString().equalsIgnoreCase(Fields.Group.GROUP_ID))
                    obj.setGroupId(rec.get(key).toString());

                else if (key.toString().equalsIgnoreCase(Fields.Group.ENABLED))
                    obj.setEnabled(convertBoolean(rec.get(key).toString()));

                else if (key.toString().equalsIgnoreCase(Fields.Group.GROUP_NAME))
                    obj.setGroupName(rec.get(key).toString());

                else if (key.toString().equalsIgnoreCase(Fields.Group.PARENT_GROUP_ID))
                    obj.setParentGroupId(rec.get(key).toString());

                else if (key.toString().equalsIgnoreCase(Fields.Group.GRUPPO_STRUTTURA)) {
                    obj.getExtraInfo().put(key.toString(), rec.get(key).toString());
                }
                else if (key.toString().startsWith(Fields.Group.GROUP_PREFIX)) {
                    obj.getExtraInfo().put(key.toString(), rec.get(key).toString());
                }

            }

            dtResult.add(obj);
        }

        return dtResult;

    }

    public EnumBoolean convertBoolean(String boolValue) {
        boolean ret = Boolean.parseBoolean(boolValue);

        if (ret)
            return EnumBoolean.TRUE;

        return EnumBoolean.FALSE;
    }

    public boolean isNullOrEmpty(String string) {
        return (string == null || StringUtils.isEmpty(string));
    }
}


