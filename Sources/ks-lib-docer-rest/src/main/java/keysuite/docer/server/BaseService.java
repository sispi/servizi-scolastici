package keysuite.docer.server;

import com.github.underscore.lodash.U;
import com.google.common.base.Strings;
import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.businesslogic.configuration.ConfigByEnte;
import it.kdm.docer.commons.configuration.ConfigurationLoadingException;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.ResourceCache;
import it.kdm.docer.providers.solr.SolrBaseUtil;
import it.kdm.orchestratore.session.ActorsCache;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.session.UserInfo;
import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.exceptions.*;
import keysuite.docer.client.*;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;
import it.kdm.docer.ws.*;
import keysuite.solr.QueryParams;
import keysuite.solr.SolrSimpleClient;
import keysuite.solr.SolrUtils;
import keysuite.swagger.client.SwaggerClient;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.*;

import static keysuite.docer.client.ClientUtils.throwKSException;

//import keysuite.docer.providers.SolrBaseUtil;

public abstract class BaseService implements EnvironmentAware {

    @Autowired(required = false)
    protected AuthenticationServiceEndpoint authenticationServiceEndpoint;

    @Autowired(required = false)
    protected WSFascicolazioneEndpoint wsFascicolazioneEndpoint;

    @Autowired(required = false)
    protected WSProtocollazioneEndpoint wsProtocollazioneEndpoint;

    @Autowired(required = false)
    protected WSRegistrazioneEndpoint wsRegistrazioneEndpoint;

    @Autowired(required = false)
    protected DocerServicesEndpoint docerServicesEndpoint;

    @Autowired(required = false)
    protected WSPECEndpoint wsPECEndpoint;


    public abstract DocerBean create( DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound;

    public abstract DocerBean update( DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound;

    protected final static Logger logger = LoggerFactory.getLogger(BaseService.class);

    public final static String WSDocer = "WSDocer";
    public final static String WSFascicolazione = "WSFascicolazione";
    public final static String WSRegistrazione = "WSRegistrazione";
    public final static String WSProtocollazione = "WSProtocollazione";
    public final static String WSFirma = "WSFirma";
    public final static String AuthenticationService = "AuthenticationService";
    final static long ttl = 60*10*1000;

    protected final static SolrSimpleClient solrClient = new SolrSimpleClient();

    private final PassiveExpiringMap<String,Object> tokenCache = new PassiveExpiringMap<>(ttl);

    private static Properties securityProps = null;
    private static Map<String,String> pianiClass = new LinkedHashMap<>();

    BaseService(){
        try {
            securityProps = ConfigurationUtils.loadProperties("security.properties");
        } catch (ConfigurationLoadingException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getFascicoloIdAndPiano(String fascicoloId){
        String[] parts = ClientUtils.splitFascicoloId(fascicoloId);
        String withClass = ClientUtils.joinFascicoloId(parts[0],parts[1],parts[2], getPianoClassAnno(Session.getUserInfoNoExc().getCodAoo(),parts[1]));
        return withClass;
    }

    static ResourceCache.ResourceFile confFile = null;

    private static void initPianoClass(String codAoo) {

        if (confFile!=null && !confFile.isValid()){
            synchronized (pianiClass){
                pianiClass.clear();
                confFile = null;
            }
        }

        if (pianiClass.containsKey(codAoo))
            return;

        synchronized (pianiClass) {

            if (confFile==null)
                confFile = ConfigurationUtils.getFile(null,"configuration.xml");

            String xml;
            ResourceCache.ResourceFile f;
            String codEnte = ClientCache.getInstance().getAOO(codAoo).getCodEnte();
            try {
                f = ConfigurationUtils.getFile( codEnte,"configuration.xml");
                xml = FileUtils.readFileToString(f, Charset.defaultCharset());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Map<String, Object> map = (Map) U.fromXml(xml);
            Object piano_class_default = U.get(map, "configuration.group[0].section.piano_class");
            List<Map> l = piano_class_default instanceof List ? (List<Map>) piano_class_default : Collections.singletonList((Map) piano_class_default);

            for (Map p : l)
                if (p != null) {
                    String anno = (String) p.get("-anno");
                    String value = (String) p.get("-value");
                    String aoo = "" + p.get("-aoo");

                    ConfigByEnte.initPiano(aoo,anno,value,pianiClass);

                    pianiClass.put(aoo,"true");
                }
            pianiClass.put(codAoo,"true");
        }
    }

    public String getPianoClassFascicolo(String aoo, String annoFascicolo){
        return getPianoClassFascicolo(aoo,annoFascicolo,null);
    }

    public String getPianoClassFascicolo(String aoo, String annoFascicolo, String piano){

        if (Strings.isNullOrEmpty(annoFascicolo))
            throw new RuntimeException("ANNO FASCICOLO non specificato");

        String pianoConf = getPianoClassAnno(aoo,annoFascicolo);

        if (Strings.isNullOrEmpty(piano) || piano.equals(pianoConf))
            return pianoConf;

        throw new RuntimeException("PIANO "+piano+" non compatibile per l'anno "+annoFascicolo);
    }

    public static String getPianoClassCorrente(String aoo){
        return getPianoClassAnno(aoo,null);
    }

    public static String getPianoClassAnno(String aoo, String anno){
        initPianoClass(aoo);

        if (pianiClass.size()==0)
            return null;

        if (Strings.isNullOrEmpty(anno)){
            anno = ""+Calendar.getInstance().get(Calendar.YEAR);
        }

        String piano;
        if (pianiClass.containsKey(aoo+anno))
            piano = pianiClass.get(aoo+anno);
        else if (pianiClass.containsKey(aoo+"*"))
            piano = pianiClass.get(aoo+"*");
        else if (pianiClass.containsKey("*"+anno))
            piano = pianiClass.get("*"+anno);
        else if (pianiClass.containsKey("**"))
            piano = pianiClass.get("**");
        else
            throw new RuntimeException("PIANO non configurato per l'anno "+anno);

        if (Strings.isNullOrEmpty(piano))
            piano = null;

        return piano;
    }

    public static void addExtraData(String key, Object value){
        HttpServletRequest req = Session.getRequest();
        if (req==null)
            return;
        Map old = (Map) req.getAttribute("extraData");

        if (old==null){
            old = new LinkedHashMap();
            req.setAttribute("extraData",old);
        }
        old.put(key,value);
    }

    protected BusinessLogic newBLDocer(){
        try{
            return new BusinessLogic(it.kdm.docer.providers.solr.Provider.class.getName(),1000);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setEnvironment(Environment environment){
        SwaggerClient.READ_TIMEOUT = environment.getProperty("client.timeout",Integer.class,10000);

        Session.setSelfAttachOn();

        //ApplicationProperties.setEnv(environment);

    }

    @Autowired
    protected Environment env;

    protected boolean isPUT(){
        if (Session.getRequest() != null && Session.getResponse() != null){
            return Session.getRequest().getMethod().toUpperCase().equals("PUT");
        } else {
            return false;
        }
    }

    /*boolean isAutenticazioneInterna(){
        return env.getProperty("Authentication.internal",Boolean.class,false);
    }*/

    /*boolean isFascicolazioneInterna(){
        return Boolean.parseBoolean(env.getProperty("WSFascicolazione.internal."+ Session.getUserInfo().getCodAoo(),env.getProperty("WSFascicolazione.internal")));
    }

    boolean isProtocollazioneInterna(){
        return Boolean.parseBoolean(env.getProperty("WSProtocollazione.internal."+ Session.getUserInfo().getCodAoo(),env.getProperty("WSProtocollazione.internal")));
    }

    boolean isRegistrazioneInterna(){
        return Boolean.parseBoolean(env.getProperty("WSRegistrazione.internal."+ Session.getUserInfo().getCodAoo(),env.getProperty("WSRegistrazione.internal")));
    }*/

    /*private String docerLogin(String username, String password, String codEnte) throws bpmn2.ServiceException, AuthenticationException {

        //Map result = AuthenticationClient.execute("#login",username, password,codEnte,ToolkitConnector.getSedeLocale());
        //return (String) result.get("return");
        return authenticationServiceEndpoint.login(username, password,codEnte,ToolkitConnector.getSedeLocale());

    }*/

    public String loginJWT( String codAoo, String username, String password ) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {
            if (Strings.isNullOrEmpty(codAoo) || Strings.isNullOrEmpty(username)  || Strings.isNullOrEmpty(password) )
                throw new KSExceptionBadRequest("codAoo, username e password obbligatori");

            String md5 = ClientCacheAuthUtils.generateMD5(username); // DigestUtils.md5DigestAsHex((username + ToolkitConnector.getGlobalProperty("secretKey")).getBytes());

            if ("system".equals(codAoo) && "admin".equals(username) && md5.equals(password)){
                return ClientUtils.simpleJWTToken(codAoo,username,System.getProperty("secretKey"));
            }

            Group group = ActorsCache.getInstance().getAOO(codAoo);

            if (group==null || !group.isAOO())
                throw new KSExceptionBadRequest("invalid AOO:"+codAoo);

            User user = ClientCache.getInstance().getUser(codAoo,username);

            if (user==null){
                String id = ClientUtils.getSolrId(null,null, ClientCache.suffix(group.getPrefix()) + username,User.TYPE);
                ClientCache.getInstance().removeItem(id);
                user = ClientCache.getInstance().getUser(codAoo,username);
            }

            if (user==null){
                //se passo per i claims l'utente potrebbe crearlo
                String jwtToken = ClientCacheAuthUtils.getInstance().simpleJWTToken(codAoo,username,username);
                user = ClientCacheAuthUtils.getInstance().getUser(jwtToken);
                if (user==null)
                    throw new RuntimeException("user not found:"+username);
            }

            if (!md5.equalsIgnoreCase(password) && !password.equals(user.getPassword())) {
                return authenticationServiceEndpoint.login(username, password,group.getCodEnte(),Session.getSede());
                /*if (isAutenticazioneInterna())
                    throw new KSExceptionForbidden("password errata");
                else {
                    try{
                        docerLogin(username, password,group.getCodEnte());
                    } catch (bpmn2.ServiceException e) {
                        throw new KSExceptionForbidden(e.getReason());
                    } catch (AuthenticationException e) {
                        throw new KSExceptionForbidden(e.getMessage());
                    }

                }*/
            }

            return ClientUtils.simpleJWTToken(codAoo,username,System.getProperty("secretKey"));

        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    public String getToken() {
        UserInfo ui = Session.getUserInfo();
        return ui.getJwtToken();
    }

    public String getTicket() {
        UserInfo ui = Session.getUserInfo();
        return ui.getDocTicket();
    }

    /*public static SOAPExecutor getSOAPServiceInstance(String SOAPService){

        //String SOAPService = this.getClass().getSimpleName();

        if (!swaggers.containsKey(SOAPService)){
            try{
                Swagger swagger = ClientFactory.getRESTService("docer/"+SOAPService+".json");
                swaggers.put(SOAPService,swagger);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return new SOAPExecutor(swaggers.get(SOAPService));
    }*/

    public static void solrDelete(String id,String type) throws KSExceptionForbidden {
        solrDelete(getSolrId(id,type));
    }

    public static void solrDelete(String id) throws KSExceptionForbidden{
        if (Session.getUserInfo().isAdmin()) {
            try {
                Map<String,Object> map = new HashMap<>();
                map.put("enabled",false);
                solrClient.update(id, map );
                //solrClient.delete(id);
            } catch (Exception e){
                if (e.getMessage() != null && e.getMessage().contains("Node is not empty")) {
                    throw new KSRuntimeException(Code.E001,"Node id not empty");
                } else {
                    throw e;
                }
            }
        } else
            throw new KSExceptionForbidden("solo admin può eliminare questo elemento:"+id);
    }

    public final static String SEARCH_PARAMS = "params";

    public static <T extends DocerBean> ISearchResponse<T> solrSimpleSearch(ISearchParams params) throws KSExceptionBadRequest {

        String reqParams = params.getFirst(SEARCH_PARAMS);

        if (reqParams!=null){
            params.set(SEARCH_PARAMS,null);
            SearchParams sp = ServerUtils.parseQueryString(reqParams);
            for( String p : sp.keySet() )
                params.add(p, sp.get(p));
        }

        return solrSimpleSearch(new QueryParams(params.entrySet()));
    }

    public static QueryResponse solrSelect(QueryParams params) throws KSExceptionBadRequest {

        try {
            params.set("ticket", Session.getUserInfoNoExc().getPrefixedUsername());

            String solrFq = System.getProperty("solr.fq", "(COD_ENTE:{{COD_ENTE}} AND COD_AOO:{{COD_AOO}}) (COD_ENTE:{{COD_ENTE}} NOT COD_AOO:*) (*:* NOT COD_ENTE:*)" );
            int solrRows = Integer.parseInt(System.getProperty("solr.rows" , "10000"));

            solrFq = solrFq.replace("{{COD_AOO}}" , Session.getUserInfoNoExc().getCodAoo() );
            solrFq = solrFq.replace("{{COD_ENTE}}" , Session.getUserInfoNoExc().getCodEnte() );

            if ( params.contains("fq") || params.contains("q") || (!params.contains("id") && !params.contains("ids")))
                params.add("fq", solrFq);

            if (!params.contains("rows")) {
                params.set("rows", solrRows);
            }

            if (params.getBool("permissions", false)) {
                params.add("fl", "*,permissions:[user_rights],inherited:[acl_inherited]");
            }

            if (!params.contains("qt"))
                params.set("qt", "/select");

            String prefix = Session.getPrefix();

            if (!Strings.isNullOrEmpty(prefix)) {
                if (!prefix.endsWith("__"))
                    prefix += "__";

                String q = params.get("q");

                if (!Strings.isNullOrEmpty(q)) {
                    params.set("q", ServerUtils.rewriteWithPrefix(prefix, q));
                }

                String[] fq = params.getParams("fq");

                if (fq != null) {
                    for (int i = 0; i < fq.length; i++) {
                        fq[i] = ServerUtils.rewriteWithPrefix(prefix, fq[i]);
                    }
                    params.set("fq", fq);
                }
            }

            QueryResponse resp0 = solrClient.select(params);

            SolrDocumentList resp = resp0.getResults();
            if (resp==null){
                SolrDocument doc = (SolrDocument) resp0.getResponse().get("doc");
                if (doc!=null){
                    resp = new SolrDocumentList();
                    resp.setNumFound(1);
                    resp.add(doc);
                    resp0.getResponse().add("response",resp);
                    resp0.setResponse(resp0.getResponse());
                }
            }

            if (!Strings.isNullOrEmpty(prefix)) {
                for( int i=0; i<resp.size(); i++ ){
                    ServerUtils.removePrefix(resp.get(i));
                }
            }

            return resp0;
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        } catch (SolrException e){
            throw new KSExceptionBadRequest(e);
        }
    }

    public static QueryResponse adminSolrSelect(QueryParams params) throws KSExceptionBadRequest {

        try {
            params.set("ticket", "admin");

            if (!params.contains("rows")) {
                params.set("rows", 10000);
            }

            if (!params.contains("qt"))
                params.set("qt", "/select");

            QueryResponse resp0 = solrClient.select(params);

            SolrDocumentList resp = resp0.getResults();
            if (resp==null){
                SolrDocument doc = (SolrDocument) resp0.getResponse().get("doc");
                if (doc!=null){
                    resp = new SolrDocumentList();
                    resp.setNumFound(1);
                    resp.add(doc);
                    resp0.getResponse().add("response",resp);
                    resp0.setResponse(resp0.getResponse());
                }
            }

            return resp0;
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        } catch (SolrException e){
            throw new KSExceptionBadRequest(e);
        }
    }

    public static <T extends DocerBean> ISearchResponse<T> solrSimpleSearch(QueryParams params) throws KSExceptionBadRequest{
        try {
            List<T> results = new ArrayList<>();

            SolrDocumentList resp = solrSelect(params).getResults();

            for(SolrDocument doc : resp ){
                Class cls;
                String type = doc.get("id").toString().split("@")[1];

                try {
                    cls = Class.forName("keysuite.docer.client."+ StringUtils.capitalize(type));
                } catch (ClassNotFoundException e) {
                    if ("aoo".equals(type) || "ente".equals(type))
                        cls = Group.class;
                    else
                        cls = Anagrafica.class;
                }
                ServerUtils.removePrefix(doc);
                results.add( evalPermissions(doc,ClientUtils.toClientBean(doc,cls)));
            }
            /*QueryResponse<T> qr = new QueryResponse<T>();
            qr.setData(results);
            qr.setRecordCount( ((Long)resp.getNumFound()).intValue() );
            qr.setPageSize(params.getPageSize());
            qr.setPageNumber(params.getPageNumber());*/

            return new ISearchResponse<T>() {
                @Override
                public List<T> getData() {
                    return results;
                }

                @Override
                public Integer getRecordCount() {
                    return ((Long)resp.getNumFound()).intValue();
                }
            };


        } catch (SolrException e){
            throw new KSExceptionBadRequest(e);
        }
    }

    public static <T extends DocerBean> ISearchResponse<T> solrSimpleSearch(String q,String... fq) throws KSExceptionBadRequest{

        ModifiableSolrParams params = new QueryParams().set("q",q);
        if (fq.length>0) params.set("fq",fq);

        return solrSimpleSearch( (QueryParams) params);
    }

    public static String[] solrGetAdvancedVersions(String docnum) throws KSExceptionBadRequest{

        QueryParams params = new QueryParams();
        params.set("ticket",Session.getUserInfoNoExc().getPrefixedUsername());
        params.set("q","related:"+ getSolrId(docnum,Documento.TYPE));
        params.set("fq","type:versions");
        params.set("fl","id,related");
        params.set("qt","/select");

        try {
            SolrDocumentList resp = solrClient.select(params).getResults();

            if (resp.size()>0){
                List<String> related = (List<String>) resp.get(0).getFieldValue("related");
                /*String[] rels = new String[related.size()];

                for( int i=0; i<related.size(); i++){
                    rels[i] = related.get(i).split("[!@]")[2];
                }
                return rels;*/
                return related.toArray(new String[0]);
            }
            else
                return null;

        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        } catch (SolrException e){
            throw new KSExceptionBadRequest(e);
        }


    }

    public static String[] solrGetRelated(String docnum) throws KSExceptionBadRequest{

        QueryParams params = new QueryParams();
        params.set("ticket",Session.getUserInfoNoExc().getPrefixedUsername());
        params.set("q","related:"+ getSolrId(docnum,Documento.TYPE));
        params.set("fq","type:related");
        params.set("fl","id,related");
        params.set("qt","/select");

        try {
            SolrDocumentList resp = solrClient.select(params).getResults();

            if (resp.size()>0){
                List<String> related = (List<String>) resp.get(0).getFieldValue("related");
                /*String[] rels = new String[related.size()];

                for( int i=0; i<related.size(); i++){
                    rels[i] = related.get(i).split("[!@]")[2];
                }
                return rels;*/
                return related.toArray(new String[0]);
            }
            else
                return null;

        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        } catch (SolrException e){
            throw new KSExceptionBadRequest(e);
        }


    }

    public static <T extends DocerBean> T solrGetFirst(String q,String... fq) throws KSExceptionBadRequest{
        List results = solrSimpleSearch(q,fq).getData();
        if (results.size()>0)
            return (T) results.get(0);
        else
            return null;
    }

    public static String[] solrGetAcls(String id, String type) throws KSExceptionNotFound{
        return solrGet(id,type, "id", "acl_explicit").getAcls();
    }

    public SolrDocument getLastAddedSolrDocument(){
        Map map = SolrBaseUtil.extraFields.get();
        if (map!=null){
            SimpleOrderedMap som = (SimpleOrderedMap) map.get("__result__");
            if (som!=null)
                return (SolrDocument) som.get("processAdd");
        }
        return null;
    }

    public void checkExtraField(DocerBean bean) {

        //Map<String,Object> extra = new LinkedHashMap<>();

        if (bean instanceof User){
            User user = (User) bean;
            if (user.getGroups()!=null){
                /*String[] groups = user.getGroups();
                String prefix = Session.getPrefix();

                for( int i=0; i<groups.length; i++) {
                    if (!Strings.isNullOrEmpty(prefix)) {
                        if (groups[i] != null && !groups[i].startsWith(prefix)) {
                            groups[i] = prefix + groups[i];
                        }
                    }
                    groups[i] = groups[i] + "@group";
                }

                groups = new HashSet<String>(Arrays.asList(groups)).toArray(new String[0]);

                extra.put("groups",groups);*/
                bean.otherFields().put("GROUPS",user.getGroups());
            }
        }

        if (bean.getAcls()!=null){
            /*String[] acls = bean.getAcls();
            String prefix = Session.getPrefix();
            if (!Strings.isNullOrEmpty(prefix)){
                for( int i=0; i<acls.length; i++){
                    if (acls[i]!=null && !acls[i].startsWith(prefix)){
                        acls[i] = prefix + acls[i];
                    }
                }
            }
            extra.put("acl_explicit",acls);*/
            bean.otherFields().put("ACL_EXPLICIT_SS",bean.getAcls());
        }

        if (bean.getGuid()!=null){
            //extra.put("guid",bean.getGuid());
            bean.otherFields().put("GUID",bean.getGuid());
        }

        /*String regex = env.getProperty("extra-fields","^EXTRA_.*");

        List<String> extraFields = new ArrayList(bean.otherFields().keySet());
        for ( String key : extraFields ){
            if (key.matches(regex)){
                extra.put(key,bean.otherFields().remove(key));
            }
        }

        if (extra.size()>0)
            SolrBaseUtil.extraFields.set(extra);*/
    }

    public static boolean checkAcl(DocerBean oldBean, DocerBean bean) throws KSExceptionNotFound, KSExceptionBadRequest {

        String[] newAcls = bean.getAcls();
        try {
            String[] oldAcls = null;
            if (oldBean!=null)
                oldAcls = oldBean.getAcls();
            else
                oldAcls = new String[0];

            boolean isDiff = false;
            boolean isUp = false;

            if (newAcls == null)
                return isDiff;

            for (int i = 0; i < newAcls.length; i++) {

                String acl = newAcls[i];

                if (!isDiff && (acl.startsWith("+") || acl.startsWith("-")))
                    isDiff = true;
                if (!isUp && acl.contains("^"))
                    isUp = true;

                if (!acl.contains(":") && acl.startsWith("-"))
                    acl += ":*";

                String[] parts = acl.split(":");

                if (!parts[0].contains("@")) {
                    String actor = parts[0];
                    if (actor.startsWith("+") || actor.startsWith("-") || actor.startsWith("^"))
                        actor = actor.substring((1));
                    if (ActorsCache.getInstance().getUserForUsername(actor) != null)
                        parts[0] += "@user";
                    else
                        parts[0] += "@group";
                }

                newAcls[i] = parts[0] + ":" + parts[1];
            }

            if (isUp)
                newAcls = ServerUtils.resolveUpAcl(newAcls);

            if (isDiff) {
                newAcls = ServerUtils.diffAcl(oldAcls, newAcls);
            }

            boolean equals = false;

            if ( oldAcls!=null && newAcls.length==oldAcls.length){
                List<String> oldList = new ArrayList(Arrays.asList(oldAcls));
                List<String> newList = Arrays.asList(newAcls);
                oldList.removeAll(newList);
                if (oldList.size()==0)
                    equals = true;
            }

            if (equals)
                newAcls = null;

            bean.setAcls(newAcls);

            return isDiff;
        } /*catch(KSException kse){
            throw kse;
        } */catch(Exception e){
            throw new KSExceptionBadRequest("bad acl:"+StringUtils.join(newAcls,","));
        }
    }

    public static <T extends DocerBean> T solrGet(String id) throws KSExceptionNotFound{
        return solrGet(id,null);
    }

    public static <T extends DocerBean> T solrGetNoExc(String id,String type) {
        try {
            return solrGet(id,type);
        } catch (KSExceptionNotFound notFound) {
            return null;
        }
    }

    public static <T extends DocerBean> T solrGetNoExc(String id){
        try {
            return solrGet(id,null);
        } catch (KSExceptionNotFound notFound) {
            return null;
        }
    }

    public static <T extends DocerBean> List<T> solrGets(String... id){
        try {
            String prefix = Session.getPrefix();
            if (!Strings.isNullOrEmpty(prefix)) {
                if (!prefix.endsWith("__"))
                    prefix += "__";
                for (int i = 0; i < id.length; i++) {
                    if (id[i].endsWith("@user") || id[i].endsWith("@group"))
                        id[i] = prefix + id[i];
                }
            }
            String ids = StringUtils.join(id, " ");
            return (List<T>) solrSimpleSearch("id:("+ids+")").getData();
        } catch (KSExceptionBadRequest ksExceptionBadRequest) {
            return new ArrayList<>();
        }
    }

    public static String getSolrId(String targetId,String type){
        String id = SolrUtils.getSolrId(targetId,type);
        String prefix = Session.getPrefix();
        if ( !Strings.isNullOrEmpty(prefix) && (type.equals("group") || type.equals("user")) && !id.startsWith(prefix) ){
            if (!prefix.endsWith("__"))
                prefix += "__";
            return prefix+id;
        } else {
            return id;
        }
    }

    public static <T extends DocerBean> T solrGet(String id, String type, String... fl) throws KSExceptionNotFound {

        if (!id.contains("@")){
            id = getSolrId(id,type);
        } else {
            type = id.split("@")[1];
        }

        if (fl==null || fl.length==0){
            fl = new String[] { "*" , "permissions:[user_rights]","inherited:[acl_inherited]" };
        }

        Class cls;

        try {
            cls = Class.forName("keysuite.docer.client."+ StringUtils.capitalize(type));
        } catch (ClassNotFoundException e) {
            if ("aoo".equals(type) || "ente".equals(type))
                cls = Group.class;
            else
                cls = Anagrafica.class;
        }

        try {
            String gethandler = "/get";
            SolrDocument doc = solrClient.getByHandler(gethandler, Session.getUserInfoNoExc().getPrefixedUsername(), id, fl);
            ServerUtils.removePrefix(doc);
            return evalPermissions(doc,ClientUtils.toClientBean(doc , cls));
        }catch (SolrException se){
            if (se.code() == SolrException.ErrorCode.NOT_FOUND.code)
                throw new KSExceptionNotFound(se);
            else
                throw se;
        }
    }

    public static <T extends DocerBean> T solrGetByGUID(String guid){
        try {
            return solrGetFirst("guid:"+guid);
        } catch (KSExceptionBadRequest ksExceptionBadRequest) {
            return null;
        }
    }

    protected static Map<String,String> targetRules(String targetType){

        Map<String,String> rules = new HashMap<>();

        for( String name : securityProps.stringPropertyNames() ){
            String[] parts = name.split("\\.");
            if (parts.length==1 || parts[1].equalsIgnoreCase(targetType))
                rules.put(parts[0],securityProps.getProperty(name));
        }

        return rules;
    }

    public final static ExpressionParser expressionParser = new SpelExpressionParser();

    static Map<Integer, Expression> expressions = new HashMap<>();

    public static boolean eval(String expression,Collection<String> permissions, Map map, DocerBean targetObject ){

        UserInfo ui = Session.getUserInfoNoExc();

        StandardEvaluationContext ctx = new StandardEvaluationContext(new Object() {
            public boolean hasRole(String... role){
                return ui.hasRole(role);
            }

            public boolean hasPermission(String permission){
                return permissions.contains(permission);
            }
        });

        ctx.setVariables(map);
        ctx.setVariable("target",targetObject);

        int hash = expression.hashCode();

        Expression expr = expressions.get(hash);
        if (expr==null){
            synchronized (expressions) {
                expr = expressionParser.parseExpression(expression);
                expressions.put(hash, expr);
            }
        }

        return (Boolean) expr.getValue(ctx);
    }

    public static <T extends DocerBean> T evalPermissions(Map map, T targetObject){

        Collection<String> permissions = targetObject.getPermissions();
        if (permissions==null || permissions.size()==0)
            return targetObject;

        Map<String,String> rules = targetRules(targetObject.getType());

        for( String rule : rules.keySet() ){
            if (eval(rules.get(rule),permissions,map,targetObject)){
                permissions.add(rule);
            } else {
                permissions.remove(rule);
            }
        }
        return targetObject;
    }

}
