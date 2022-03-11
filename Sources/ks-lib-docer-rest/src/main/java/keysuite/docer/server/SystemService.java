package keysuite.docer.server;

import com.google.common.base.Strings;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.ResourceCache;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.session.UserInfo;
import it.kdm.orchestratore.utils.ResourceUtils;
import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.docer.client.*;
import keysuite.docer.query.ISearchResponse;
import keysuite.solr.QueryParams;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SystemService {

    final static Logger logger = LoggerFactory.getLogger(SystemService.class);

    @Autowired
    UtentiService utentiService;

    @Autowired
    GruppiService gruppiService;

    @Autowired
    TitolariService titolariService;

    @Autowired
    AnagraficheService anagraficheService;

    @Autowired
    DocumentiService documentiService;

    public String impersonate(String codAoo, String username) throws KSExceptionForbidden {

        User user = ClientCacheAuthUtils.getInstance().getThreadUser();

        if (!user.isAdmin())
            throw new KSExceptionForbidden("only admin");

        Group aoo = ClientCache.getInstance().getAOO(codAoo);

        user = ClientCache.getInstance().getUser(codAoo,username);

        if (aoo==null){
            throw new KSExceptionForbidden("aoo non esistente");
        }

        if (user==null){
            throw new KSExceptionForbidden("username non esistente");
        }

        return ClientUtils.simpleJWTToken(codAoo,username, System.getProperty("secretKey"));
    }

    public User register(String codAoo, String username, String password) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        User user = ClientCacheAuthUtils.getInstance().getThreadUser();

        if (!user.isAdmin())
            throw new KSExceptionForbidden("only admin");

        Group aoo = ClientCache.getInstance().getAOO(codAoo);

        user = ClientCache.getInstance().getUser(codAoo,username);

        if (aoo==null){
            throw new KSExceptionForbidden("aoo non esistente");
        }

        if (user!=null){
            throw new KSExceptionForbidden("username già esistente");
        }

        user = new User();
        user.setUserName(username);
        user.setPassword(password);
        user.setFullName(username);

        return utentiService.create(user);
    }

    public ISearchResponse adminsolr(String qt, HttpServletRequest request) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        String bearer = Session.getBearer();

        if (bearer==null)
            throw new KSExceptionForbidden("only admin");

        String jwtToken = bearer.startsWith("Bearer ") ? bearer.substring("Bearer ".length()) : bearer;

        User user = ClientCacheAuthUtils.getInstance().getUser(jwtToken);

        if (!user.isAdmin())
            throw new KSExceptionForbidden("only admin");

        //String SECRET = env.getProperty("secretKey","SECRET");

        //Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();

        //if (!"admin".equals(claims.getSubject()))
        //    throw new KSExceptionForbidden("only admin");

        String qs = request.getQueryString();
        QueryParams params = new QueryParams(qs);
        params.setQt("/"+qt);

        QueryResponse solrResponse = documentiService.adminSolrSelect(params);

        ISearchResponse resp = new ISearchResponse<SolrDocument>() {
            @Override
            public List<SolrDocument> getData() {
                SolrDocumentList list = solrResponse.getResults();
                return list != null ? list : new SolrDocumentList();
            }

            @Override
            public Integer getRecordCount() {
                SolrDocumentList list = solrResponse.getResults();
                return list!=null?((Long)solrResponse.getResults().getNumFound()).intValue():0;
            }
        };
        return resp;
    }

    public Map importCsv_byfilename(String filename,boolean continueOnError,boolean includeInfo,boolean forceUpdate,boolean init) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        if (filename==null){
            throw new KSExceptionBadRequest("filename non specificato");
        }

        InputStream is = ResourceUtils.getResourceNoExc(filename);
        if (is==null)
            throw new KSExceptionBadRequest("filename non trovato");

        try {
            String text = StreamUtils.copyToString(is, Charset.defaultCharset());
            return importCsv_bycontent(text,continueOnError,includeInfo,forceUpdate,init);
        } catch (IOException e) {
            throw new KSRuntimeException(e);
        }
    }

    public Map importCsv_bycontent(String csv,boolean continueOnError,boolean includeInfo,boolean forceUpdate,boolean init ) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        try{
            return importCsv(csv,includeInfo,continueOnError,forceUpdate,init);
        } catch ( IOException | CsvException ioe ){
            throw new KSExceptionBadRequest("bad csv content");
        }
    }

    private Map importCsv(String text, boolean includeInfo, boolean continueOnError, boolean forceUpdate, boolean init) throws IOException, CsvException {

        long ts = System.currentTimeMillis();

        if (text==null)
            throw new RuntimeException("context is null");

        logger.info("inizio import ({} {} {}) text length:",includeInfo,continueOnError,forceUpdate,text.length());

        UserInfo ui = Session.getUserInfoNoExc();

        String defDocEnte = ui.getCodEnte();
        String defCodAoo = ui.getCodAoo();

        List<Map> results = new ArrayList<>();

        List<String> header = null;
        int errors = 0;
        int updated = 0;
        int created = 0;
        int notupdated = 0;

        Map initReport = null;

        try (CSVReader reader = new CSVReader(new StringReader(text))) {

            List<String[]> lines = reader.readAll();
            List<Map<String,Object>> records = new ArrayList<>();
            int empty=0;



            if (init){
                String initStr = "type,ID,NAME,groups";
                initStr += "\nlocation";
                initStr += "\ngroup,everyone,Everyone";
                initStr += "\ngroup,admins,Administrators";
                initStr += "\nuser,admin,Administrator,\"everyone,admins\"";

                initReport = importCsv(initStr,includeInfo,continueOnError,forceUpdate,false);
            }

            for( int i=0; i< lines.size(); i++ ){
                String[] line = lines.get(i);

                if (line[0].startsWith("//") || line.length==0 || line.length==1 && line[0].equals("")){
                    records.add(null);
                    empty++;
                    continue;
                }

                if (header==null) {
                    header = Arrays.asList(line);

                    for( int h=0; h<header.size(); h++)
                        header.set(h,header.get(h).trim());

                    records.add(null);
                    continue;
                }

                Map<String,Object> record = new LinkedHashMap<>();

                for(int x=0; x< line.length; x++){
                    record.put(header.get(x),line[x].trim());
                }
                records.add(record);
            }

            logger.info("lines:{} empty:{} header:{}",records.size(),empty,header);

            for( int i=1; i< records.size(); i++ ){

                Map<String,Object> record = records.get(i);

                if (record==null){
                    continue;
                }

                Map result = new LinkedHashMap();
                String[] line = lines.get(i);
                result.put("line",i);
                result.put("content",Arrays.toString(line));

                String type = (String) record.get("type");

                if (Strings.isNullOrEmpty(type)) {
                    result.put("status",400);
                    result.put("message","type non specificato");
                    results.add(result);

                    errors++;

                    if (!continueOnError){
                        break;
                    }

                    continue;
                }

                if ("location".equals(type)){
                    result.put("type","location");
                    try {
                        gruppiService.location();
                        result.put("status",200);
                        result.put("message","creato");
                        created++;
                        if (includeInfo)
                            results.add(result);

                    } catch (Exception e) {

                        if (e.getMessage().contains("Duplicate entry")
                                || e.getMessage().contains("version conflict")) {
                            result.put("type", type);
                            result.put("status", 204);
                            result.put("message", "non modificato");
                            notupdated++;
                            if (includeInfo)
                                results.add(result);

                        } else
                            throw new KSRuntimeException(e);

                    }
                    continue;

                }

                try{
                    Class cls = ClientUtils.getClassForBean(type);

                    BaseService service;

                    String ID = (String) record.remove("ID");
                    String NAME = (String) record.remove("NAME");
                    String ACL = (String) record.remove("ACL");
                    String PARENT = (String) record.remove("PARENT");
                    String PREFIX = (String) record.remove("PREFIX");
                    String acl_explicit = (String) record.remove("acl_explicit");

                    String COD_ENTE = (String) record.remove("COD_ENTE");
                    String COD_AOO = (String) record.remove("COD_AOO");

                    if (!Strings.isNullOrEmpty(COD_ENTE) && !Strings.isNullOrEmpty(COD_AOO)){
                        //nop
                    } else if (!Strings.isNullOrEmpty(COD_ENTE)){
                        COD_AOO = null;
                    } else if (!Strings.isNullOrEmpty(COD_AOO)){
                        Group aoo = ClientCache.getInstance().getAOO(COD_AOO);
                        if (aoo==null)
                            throw new KSExceptionBadRequest("aoo not found:"+COD_AOO);
                        COD_ENTE = aoo.getCodEnte();
                    } else {
                        COD_AOO = defCodAoo;
                        COD_ENTE = defDocEnte;
                    }

                    if (!Strings.isNullOrEmpty(acl_explicit)){
                        record.put("acl_explicit", acl_explicit.split(",") );
                    }

                    if (!Strings.isNullOrEmpty(ACL)){

                        String[] acls = ACL.split(",");
                        String[] explicits = new String[acls.length];

                        for( int a=0; a<acls.length; a++){
                            String acl = acls[a];
                            String right = "readOnly";
                            if (acl.contains(":")){
                                right = acl.split(":")[1].toUpperCase();
                                if (right.equals("1") || right.equals("EDIT") || right.equals("NORMALACCESS") || right.equals("NORMAL"))
                                    right = "normalAccess";
                                else if (right.equals("0") || right.equals("FULL") || right.equals("FULLACCESS"))
                                    right = "fullAccess";
                                else
                                    right = "readOnly";
                            }
                            explicits[a] = acl.split(":")[0] + ":" + right;
                        }
                        record.put("acl_explicit",explicits);
                        logger.debug("acl_explicit {}",explicits);
                    }

                    if (cls.isAssignableFrom(Group.class)) {
                        service = gruppiService;

                        if (!Strings.isNullOrEmpty(ID))
                            record.put("GROUP_ID",ID);
                        if (!Strings.isNullOrEmpty(NAME))
                            record.put("GROUP_NAME",NAME);

                        if (type.equals(Group.TYPE)){
                            if (!Strings.isNullOrEmpty(PARENT)){
                                record.put("PARENT_GROUP_ID",PARENT);
                            }
                        } else if (type.equals(Group.TYPE_ENTE)){
                            COD_ENTE = (String) record.getOrDefault("GROUP_ID",record.get("COD_ENTE"));

                            if (!Strings.isNullOrEmpty(PREFIX) && !"false".equals(PREFIX)){
                                if ("true".equals(PREFIX))
                                    record.put("prefix",COD_ENTE /*+"__"*/ );
                                else
                                    record.put("prefix",PREFIX /*+"__"*/ );
                            }

                            /*if (PREFIX!=null && "true".equalsIgnoreCase(PREFIX)){
                                record.put("prefix",COD_ENTE+"__");
                            }*/

                        } else if (type.equals(Group.TYPE_AOO)){
                            if (!Strings.isNullOrEmpty(PARENT)){
                                record.put("COD_ENTE",PARENT);
                            }
                            COD_ENTE = (String) record.get("COD_ENTE");
                            COD_AOO = (String) record.getOrDefault("GROUP_ID",record.get("COD_AOO"));
                        }

                    } else if (cls.isAssignableFrom(User.class)) {
                        service = utentiService;
                        if (!Strings.isNullOrEmpty(ID))
                            record.put("USER_ID",ID);
                        if (!Strings.isNullOrEmpty(NAME))
                            record.put("FULL_NAME",NAME);

                        String groups = (String) record.remove("groups");

                        if (!Strings.isNullOrEmpty(groups)){

                            String[] grps = groups.split(",");
                            if (grps.length==1 && "everyone".equals(grps[0]))
                                throw new KSExceptionBadRequest("non è possibile specificare il gruppo singolo 'everyone'");

                           List<String> l = new ArrayList<>(Arrays.asList(groups.split(",")));
                           if (!l.contains(COD_ENTE))
                               l.add(COD_ENTE);
                            if (!l.contains(COD_AOO))
                                l.add(COD_AOO);
                            if (!l.contains("everyone"))
                                l.add("everyone");

                            record.put("groups", l.toArray(new String[0]));
                        }

                    } else if (cls.isAssignableFrom(Titolario.class)) {
                        service = titolariService;
                        if (!Strings.isNullOrEmpty(ID))
                            record.put("CLASSIFICA",ID);
                        if (!Strings.isNullOrEmpty(NAME))
                            record.put("DES_TITOLARIO",NAME);
                        if (!Strings.isNullOrEmpty(PARENT))
                            record.put("PARENT_CLASSIFICA",PARENT);
                    } else if (cls.isAssignableFrom(Anagrafica.class)) {
                        service = anagraficheService;
                        if (!Strings.isNullOrEmpty(ID))
                            record.put("COD_"+type.toUpperCase(),ID);
                        if (!Strings.isNullOrEmpty(NAME))
                            record.put("DES_"+type.toUpperCase(),NAME);
                        if (!Strings.isNullOrEmpty(PARENT))
                            record.put("PARENT_COD_"+type.toUpperCase(),PARENT);
                    } /*else if (cls.isAssignableFrom(Folder.class)) {
                        service = cartelleService;
                        if (!Strings.isNullOrEmpty(ID))
                            record.put("FOLDER_NAME",ID);
                        if (!Strings.isNullOrEmpty(NAME))
                            record.put("DES_FOLDER",NAME);
                        if (!Strings.isNullOrEmpty(PARENT))
                            record.put("PARENT_FOLDER_ID",PARENT);
                    }*/ else {
                        throw new KSExceptionBadRequest("non ammesso il type "+type);
                    }

                    if (!ui.isAdmin()){
                        if (!defCodAoo.equals(COD_AOO) || !defDocEnte.equals(COD_ENTE)){
                            throw new KSExceptionForbidden("l'utente corrente non può importare specificando altro ente o altro aoo");
                        }
                    }

                    String audience = defCodAoo;

                    if (COD_AOO != null && !COD_AOO.equalsIgnoreCase(defCodAoo)){
                        audience = COD_AOO;
                    } else if (COD_ENTE != null && !COD_ENTE.equalsIgnoreCase(defDocEnte)){
                        audience = COD_ENTE;
                    } else {
                        String PARENT_GROUP_ID = (String) record.get("PARENT_GROUP_ID");
                        if (PARENT_GROUP_ID!=null)
                            audience = PARENT_GROUP_ID;
                    }

                    DocerBean bean = ClientUtils.toClientBean(record,cls);

                    if (type.equals(Folder.TYPE)){
                        //nop
                    } else if (Strings.isNullOrEmpty(bean.getDocerId())){
                        result.put("status",400);
                        result.put("message","codice non specificato");
                        results.add(result);

                        errors++;

                        if (!continueOnError){
                            break;
                        }

                        continue;
                    }

                    result.put("type",type);
                    result.put("sid",bean.getDocerId());
                    result.put("name",bean.getName());

                    String id = null;

                    if (type.equals(Group.TYPE) || type.equals(User.TYPE)){
                        String prefix = "";
                        if (!"system".equals(COD_ENTE))
                            prefix = ClientCache.getInstance().getEnte(COD_ENTE).getPrefix();
                        if (!Strings.isNullOrEmpty(prefix))
                            prefix += "__";
                        id = ClientUtils.getSolrId(null,null,prefix + bean.getDocerId(),type);
                    } else if (type.equals(Titolario.TYPE)) {
                        String piano =  ((Titolario)bean).getPianoClass();
                        if (!Strings.isNullOrEmpty(piano))
                            piano = "$" + piano;
                        id = ClientUtils.getSolrId(COD_ENTE,COD_AOO, bean.getDocerId()+piano,type);
                    } else {
                        id = ClientUtils.getSolrId(COD_ENTE,COD_AOO, bean.getDocerId(),type);
                    }

                    result.put("id",id);
                    result.put("COD_ENTE",COD_ENTE);
                    result.put("COD_AOO",COD_AOO);

                    if (type.equals(Group.TYPE_ENTE) || type.equals(Group.TYPE_AOO) || type.equals(User.TYPE) && "admin".equals(bean.getDocerId())
                            || type.equals(Group.TYPE) && ("everyone".equals(bean.getDocerId()) || "admins".equals(bean.getDocerId()) )

                    ) {
                        //audience = "system";

                        if (type.equals(Group.TYPE_ENTE)) {
                            audience = "system";
                            result.remove("COD_AOO");
                        }

                        attach(ui.getUsername(), "system");
                    } else {
                        if ("system".equals(audience))
                            throw new KSExceptionBadRequest("occorre specifiare COD_AOO oppure login esplicita");
                        attach(ui.getUsername(), audience);
                    }

                    DocerBean oldBean = service.solrGetNoExc(id);

                    if (oldBean!=null){

                        if (type.equals(Group.TYPE_ENTE) || type.equals(Group.TYPE_AOO)){
                            attach(ui.getUsername(), audience);
                            oldBean = service.solrGetNoExc(id);
                        }

                        logger.debug("oldBean trovato:"+id);

                        service.checkAcl(oldBean,bean);
                        boolean diff = bean.getAcls()!=null;

                        if (diff)
                            logger.debug("acl modificata {} {}",id,bean.getAcls());

                        if (!forceUpdate && !diff) {
                            Map<String, String> uMap = ServerUtils.toUpdateMap(oldBean, bean);

                            uMap.remove("COD_ENTE");
                            uMap.remove("COD_AOO");

                            if (uMap.size()>0) {
                                diff = true;
                                logger.debug("profilo modificato {} {}",id,uMap);
                            }

                            if (!diff) {

                                if (type.equals(User.TYPE) && record.containsKey("groups")) {
                                    String[] oldGroups = ((User) oldBean).getGroups();
                                    String[] newGroups = ((User) bean).getGroups();

                                    if (oldGroups == null)
                                        oldGroups = new String[0];
                                    if (newGroups == null)
                                        newGroups = new String[0];

                                    Set<String> l1 = new HashSet<>(Arrays.asList(oldGroups));
                                    Set<String> l2 = new HashSet<>(Arrays.asList(newGroups));

                                    l1.remove(COD_ENTE);
                                    l1.remove(COD_AOO);
                                    l1.remove("everyone");
                                    l2.remove(COD_ENTE);
                                    l2.remove(COD_AOO);
                                    l2.remove("everyone");

                                    if (l1.size() == l2.size()) {
                                        l1.removeAll(l2);
                                        diff = l1.size() > 0;
                                    } else {
                                        diff = true;
                                        ((User)bean).addGroups("everyone");
                                        logger.debug("user groups modificati {} {}",id,((User)bean).getGroups());
                                    }
                                }
                            }
                        }

                        if (!diff && !forceUpdate){
                            logger.debug("no diff {}",id);
                            result.put("status",204);
                            result.put("message","non modificato");
                            notupdated++;
                            if (includeInfo)
                                results.add(result);
                            continue;
                        }
                        service.update(bean);
                        updated++;

                        result.put("status",200);
                        result.put("message","modificato");
                    } else {
                        logger.debug("oldBean non trovato:"+id);

                        if (type.equals(Group.TYPE_AOO) || type.equals(Group.TYPE_ENTE)) {

                            if (!ui.isAdmin())
                                throw new KSExceptionForbidden("l'utente corrente non può importare enti o aoo");

                            attach(ui.getUsername(), "system");
                        }

                        service.create(bean);
                        created++;

                        result.put("status",201);
                        result.put("message","creato");
                    }

                    if (includeInfo)
                        results.add(result);

                } catch (Exception e) {
                    result.put("status",500);
                    result.put("message",e.getMessage());
                    results.add(result);

                    errors++;

                    if (!continueOnError){
                        break;
                    }

                    continue;
                }
            }
        }

        Map report = new LinkedHashMap();

        if (initReport!=null){
            List<Map> initDetails = (List<Map>) initReport.get("details");

            if (initDetails!=null){
                initDetails.addAll(results);
                results = initDetails;
            }
        }


        ts = System.currentTimeMillis()-ts;

        report.put("errors",errors);
        report.put("created",created);
        report.put("updated",updated);
        report.put("skipped",notupdated);
        report.put("elapsed",ts);
        report.put("details",results);

        logger.info("import eseguito in {}ms",ts);

        return report;
    }

    private void attach(String user,String audience){
        String jwtToken = ClientUtils.simpleJWTToken(audience,user,System.getProperty("secretKey"));
        Session.attach(jwtToken);
    }

    final static Pattern regex = Pattern.compile("\\$\\{([^}]{2,80})}");

    private String apply(Properties sp,String path){

        //FileOutputStream outStream = null;
        //InputStream stream=null;
        String configURI = System.getProperty("DOCER_CONFIG");

        try{
            File template = new File(configURI, path+".tmpl" );

            String content;

            if (template.exists()){
                content = FileUtils.readFileToString(template,Charset.defaultCharset());
            } else {
                Enumeration<URL> urls = ConfigurationUtils.class.getClassLoader().getResources(path);
                URL resourceURL = null;

                while(urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    if (url.getPath().contains("commons-config")) {
                        resourceURL = url;
                        break;
                    }
                }

                //URL resourceURL = ConfigurationUtils.class.getClassLoader().getResource(path);

                if (resourceURL==null)
                    throw new KSExceptionNotFound("file not found:"+path);

               try(InputStream stream = resourceURL.openStream()){
                   content = StreamUtils.copyToString(stream, Charset.defaultCharset());
               }

               FileUtils.writeStringToFile(template,content, Charset.defaultCharset() );
            }

            StringBuffer resultString = new StringBuffer();
            Matcher regexMatcher = regex.matcher(content);
            boolean changed = false;
            while (regexMatcher.find()) {
                changed = true;
                String found = regexMatcher.group(1);
                String def = "";
                int idx = found.indexOf(":");
                if (idx>0){
                    def = found.substring(idx+1);
                    found = found.substring(0,idx);
                }

                String fileProp = sp.getProperty(found); //,def).replace("${DOCER_CONFIG}",configURI);
                if (fileProp==null)
                    fileProp = System.getProperty(found,def);

                fileProp = fileProp.replace("${DOCER_CONFIG}",configURI);

                regexMatcher.appendReplacement(resultString, fileProp);
            }

            if (changed){

                File fsFile = new File(configURI,path);

                regexMatcher.appendTail(resultString);

                String newcontent = resultString.toString();
                String oldcontent = FileUtils.readFileToString(fsFile,Charset.defaultCharset());

                if (!newcontent.equals(oldcontent)){
                    FileUtils.writeStringToFile(fsFile,newcontent,Charset.defaultCharset());
                    return newcontent;
                }
            }

            return null;

        } catch (Exception e) {
            throw new KSRuntimeException(e);
        }
    }

    public String applySystemPropertiesToConf() throws KSExceptionForbidden {

        if (!Session.getUserInfoNoExc().isAdmin())
            throw new KSExceptionForbidden("only admin");

        try{

            String configURI = System.getProperty("DOCER_CONFIG");

            Properties sp = ResourceCache.get(configURI+"/system.properties").getProp();

            for( String k : sp.stringPropertyNames() ) {
                if (System.getProperty(k) == null){
                    String v = sp.getProperty(k);
                    if (v.contains("${"))
                        v = ResourceCache.resolveProp(v);
                    System.setProperty(k,v);
                }
                    System.setProperty(k, sp.getProperty(k).replace("${DOCER_CONFIG}",configURI));
                logger.info(k+"="+System.getProperty(k));
            }

            /*List<String> files = new ArrayList<>();
            String[] files0 = new String[] {
                "configuration.xml"
            };

            for( String file : files0 ){
                String newcontent = apply(sp,file);
                if (newcontent!=null)
                    files.add(file);
            }*/

            return apply(sp,"configuration.xml");

        } catch (Exception e) {
            throw new KSRuntimeException(e);
        }
    }



}
