package keysuite.cache;

import com.google.common.base.Strings;
import keysuite.docer.client.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.Serializable;
import java.util.*;

/*

    Le chiavi espirano in base all'inverso della data di ultima modifica:
    un elemento modificata recentemente espira prima

    quando la mappa va oltre il maxSize gli elementi prossimi all'espirazione vengono rimossi

    Se è configurato il zkHost avviene accesso diretto a Solr tramite url

    Se period>0 vengono rimossi periodicamente gli ultimi modificati su solr

 */

public class ClientCache implements Serializable {

    @FunctionalInterface
    public interface QueryHelper {
        List<Map> getItems(String q, String fl);
    }

    private static QueryHelper queryHelper = null;

    public static void setQueryHelper(QueryHelper queryHelper){
        ClientCache.queryHelper = queryHelper;
    }

    private static final Logger logger = LoggerFactory.getLogger(ClientCache.class);

    public final static String ACTUAL_OWNER = "$actualOwner";
    public final static String POT_OWNERS = "$owners";

    private static ClientCache instance;
    //private static Object mutex = new Object();

    private Map<String,DocerBean> map;

    WebClient client=null;

    Map<String,Group> _aoos;
    Map<String,Group> _enti;
    //String jwt=null;

    int defaultTTL = 10*60; //600 secondi
    int maxSize = 4096;
    int period = 1*60; //60 secondi
    Timer timer = null;

    String lastCheck = null;
    String adminmail = null;
    String[] admingroups = null;

    String SECRET;

    String baseUrl;

    //String baseUrl;

    final static Collection<String> prefixedValues = Arrays.asList("CREATOR","MODIFIER","GROUP_ID","ADMIN_GROUP_ID","PARENT_GROUP_ID","USER_ID");
    final static Collection<String> prefixedArrays = Arrays.asList("acl_explicit","groups");

    public static ClientCache createInstance(String baseUrl, String SECRET, Integer maxSize, Integer defaultTTL, Integer period){

        ClientCache curr = new ClientCache(baseUrl);
        if (SECRET!=null)
            curr.SECRET = SECRET;
        if (defaultTTL!=null)
            curr.defaultTTL = defaultTTL;
        if (maxSize!=null)
            curr.maxSize = maxSize;
        if (period!=null)
            curr.period = period;

        return curr;
    }

    public static ClientCache getInstance(){

        if (instance==null){
            String docerUrl = System.getProperty("docer.url");

            if (docerUrl==null && queryHelper==null){
                throw new RuntimeException("docer.url o queryHelper devono essere configurati");
            }

            String SECRET = System.getProperty("secretKey","SECRET");
            String s_defaultTTL = System.getProperty("clientcache.defaultTTL");
            String s_maxSize = System.getProperty("clientcache.maxSize");
            String s_period = System.getProperty("clientcache.refresh");

            Integer defaultTTL = null;
            Integer maxSize = null;
            Integer period = null;

            if (s_defaultTTL!=null)
                defaultTTL = Integer.parseInt(s_defaultTTL);

            if (s_maxSize!=null)
                maxSize = Integer.parseInt(s_maxSize);

            if (s_period!=null)
                period = Integer.parseInt(s_period);

            ClientCache curr = createInstance(docerUrl,SECRET,maxSize,defaultTTL,period);
            curr.init();
            instance = curr;
        }
        return instance;
    }

    /*public static ClientCache createInstance(PropertyResolver env){

        String baseUrl = env.getProperty("spring.solr.zkHost",env.getProperty("zuul.routes.docer.url",env.getProperty("docer.url","http://localhost:8080")));
        String SECRET = env.getProperty("secretKey","SECRET");
        Integer defaultTTL = env.getProperty("clientcache.defaultTTL",Integer.class);
        Integer maxSize = env.getProperty("clientcache.maxSize",Integer.class);
        Integer period = env.getProperty("clientcache.refresh", Integer.class);

        return createInstance(baseUrl,SECRET,maxSize,defaultTTL,period);
    }*/

    private ClientCache(String baseUrl){

        if (baseUrl!=null && !baseUrl.endsWith("/system/solr")){
            baseUrl += "/system/solr";
        }
        this.baseUrl = baseUrl;

        map = new HashMap() {

            @Override
            public Object put(Object key, Object bean){
                while (size() > (maxSize - 1)) {
                    String older = Collections.min( map.keySet(), expComparator );
                    remove(older);
                    logger.info("removed " + older);
                }

                Object obj = super.put(key, bean);
                return obj;
            };

            /*@Override
            public Object remove(Object key){
                Object obj = super.remove(key);
                orderedIds.remove()
                assert orderedIds.size()==size();
                return obj;
            }*/
        };

        _aoos = new HashMap<>();
        _enti = new HashMap<>();
    }

    public void init(){
        synchronized (map){

            int memorySize = 16 * 1024 * 1024;

            if (baseUrl!=null) {
                client = WebClient
                        .builder()
                        .baseUrl(baseUrl)
                        .defaultHeader("Authorization", ClientUtils.simpleJWTToken("system","admin", SECRET))
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(memorySize))
                        .build();
            }

            lastCheck = new DateTime(DateTimeZone.UTC).toString();

            map.clear();
            _aoos.clear();
            _enti.clear();

            /*User dummy = new User();
            dummy.setUserName(ACTUAL_OWNER);
            dummy.setFullName(ACTUAL_OWNER);
            dummy.setEmail("no_reply@keysuite.it");
            dummy.setGroups(new String[0]);

            Group dummy2 = new Group();
            dummy2.setGroupId(POT_OWNERS);
            dummy2.setGroupName(POT_OWNERS);
            dummy2.setEmail("no_reply@keysuite.it");
            dummy2.setStruttura(false);

            map.put(ACTUAL_OWNER,dummy);
            map.put(POT_OWNERS,dummy2);*/
        }

        if (period>0 /*&& false*/){
            if (timer!=null){
                timer.cancel();
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try{
                        instance.refresh();
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }, period*1000,period*1000);
        }
    }

    public int getCount(){
        return map.size() + _aoos.size() + _enti.size();
    }

    public int refresh(){

        logger.info("refresh started:"+lastCheck);

        String newlastCheck = new DateTime(DateTimeZone.UTC).toString();

        String q = String.format("indexed_on:[ %s TO * ] AND created_on:[ * TO %s ]",lastCheck,lastCheck);

        List<Map> changed = querySolrItems(q,"id");

        List<String> keys = new ArrayList<>();

        logger.debug("changed:"+changed.size());

        for( Map doc : changed ){
            String id = (String) doc.get("id");
            if (map.containsKey(id))
                keys.add(id);

            if (id.equals("admin@user")){
                adminmail = null;
                admingroups = null;
            }
        }

        if (keys.size()>0){
            synchronized (map){
                for( String key : keys ){
                    map.remove(key);
                    logger.debug("removed key "+key);
                }
            }
        }

        lastCheck = newlastCheck;

        logger.info("refresh ended:"+keys.size());

        return keys.size();
    }

    private Long getExpiration(DocerBean value){

        if (value!=null) {
            String id = value.getSolrId();
            if ("admins@group".equals(id) || "admin@user".equals(id) || "everyone@group".equals(id))
                return Long.MAX_VALUE;
        }

        if (value!=null && value.getModified()!=null){
            try{
                long age = System.currentTimeMillis() - new DateTime(value.getModified()).getMillis();
                return  System.currentTimeMillis() + age;
            } catch (Exception e){
                logger.warn("MODIFIED non valido per id:"+value.getSolrId());
                //e.printStackTrace();
            }
        }

        return System.currentTimeMillis() + defaultTTL*1000;
    }

    Comparator<String> expComparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            int cmp = getExpiration(map.get(o1)).compareTo(getExpiration(map.get(o2)));
            if (cmp==0)
                return o1.compareTo(o2);
            else
                return cmp;
        }
    };

    public int getSize(){
        return map.size();
    }

    public Map<String,Long> reportIds(){
        Map<String,Long> rep = new LinkedHashMap<>();

        List orderedIds = new ArrayList(map.keySet());
        Collections.sort(orderedIds,expComparator);
        Iterator<String> it = orderedIds.iterator();
        while (it.hasNext()){
            String id = it.next();
            DocerBean bean = map.get(id);
            rep.put(id,getExpiration(bean));
        }
        return rep;
    }

    private void check(){
        if (_aoos.size()==0){
            synchronized (map) {
                List<Map> roots = querySolrItems("type:(ente aoo)", null);

                for (Map doc : roots) {
                    DocerBean bean = getDocerBean(doc);
                    String type = (String) doc.get("type");
                    logger.info("Registrato " + type + ":" + bean.getDocerId());
                    //String id;
                    if (Group.TYPE_ENTE.equals(type)) {
                        //id = ClientUtils.getSolrId(null,null,bean.getId(),Group.TYPE_ENTE);
                        _enti.put(bean.getDocerId(), (Group) bean);
                    } else {
                        //id = ClientUtils.getSolrId(bean.getCodEnte(),null,bean.getId(),Group.TYPE_ENTE);
                        _aoos.put(bean.getDocerId(), (Group) bean);
                    }
                    putItem(bean.getSolrId(), bean);
                    //map.put(bean.getSolrId(), bean);
                }
            }
        }
    }

    /**********************************************************/

    public Collection<Actor> getActorsByEmail(String codEnteOrAoo, String email){
        return getActorsByField(codEnteOrAoo, "EMAIL_ADDRESS" , email);
    }

    public Collection<Actor> getActorsByField(String codEnteOrAoo, String field, String value){

        Collection<Actor> actors = new ArrayList<>();

        Actor enteOrAoo = getActor(codEnteOrAoo,codEnteOrAoo);

        if (enteOrAoo==null || field==null || field.length()==0 || value==null || value.length()==0)
            return actors;

        List<Map> roots = querySolrItems("type:(group user) AND "+field+":"+value, "id");

        String prefix = suffix(enteOrAoo.getPrefix());

        for (Map doc : roots) {
            String solrId = getDocerBean(doc).getSolrId();
            if (prefix.length()>0){
                if (!solrId.startsWith(prefix))
                    continue;
            } else {
                if (solrId.contains("__"))
                    continue;
            }

            DocerBean actor = getItem(solrId);
            actors.add( (Actor) actor);
        }
        return actors;
    }

    public Collection<User> getUsersInGroup(String codEnteOrAoo, String groupId){
        Group group = getGroup(codEnteOrAoo,groupId);

        if (group==null)
            return new ArrayList<>();

        Collection<User> users = (Collection<User>) group.otherFields().get("users");

        if (users!=null)
            return users;

        users = new ArrayList<>();

        String solrId = suffix(group.getPrefix())+group.getDocerId()+"@group";

        List<Map> roots = querySolrItems("type:user AND groups:"+solrId, "id");

        for (Map doc : roots) {
            DocerBean user = getItem(getDocerBean(doc).getSolrId());
            if (user instanceof User)
                users.add( (User) user);
        }

        group.otherFields().put("users",users);
        return users;
    }

    private Map<String,Group> getEntiChecked(){
        check();
        return _enti;
    }

    private Map<String,Group> getAOOsChecked(){
        check();
        return _aoos;
    }

    /*public DocerBean putItem(DocerBean bean){
        String id = bean.getSolrId();
        if (id==null){
            String sid = bean.getId();
            String type = bean.getType();
            id = getSolrId(sid,type);
        }
        return putItem(id,bean);
    }*/

    public Actor putActor(Actor actor){
        //String sid = actor.getDocerId();
        String type = actor.getType();
        String id;

        if (Group.TYPE_ENTE.equals(type)){
            id = ClientUtils.getSolrId(null,null,actor.getDocerId(),type);
            getEntiChecked().put(actor.getDocerId(), (Group) actor);
        } else if (Group.TYPE_AOO.equals(type)){
            id = ClientUtils.getSolrId( ((Group)actor).getCodEnte(),null,actor.getDocerId(),type);
            getAOOsChecked().put(actor.getDocerId(), (Group) actor);
        } else {
            //String prefix = actor.getPrefix()!=null ? actor.getPrefix() : "";
            id = ClientUtils.getSolrId(null,null,suffix(actor.getPrefix())+actor.getDocerId(),type);
        }
        return (Actor) putItem(id,actor);
    }

    private DocerBean putItem(String id, DocerBean bean){

        //String prefix = "";
        String type = id.split("@")[1];
        DocerBean old;

        synchronized (map){
            old = map.put(id,bean);

            if (bean==null){
                logger.info("Inserito in cache "+type+":"+id+"(null)");
                return old;
            }

            logger.info("Inserito in cache "+type+":"+id);

            String adminGroupId = (String) bean.otherFields().get("ADMIN_GROUP_ID");
            String adminGroupName = (String) bean.otherFields().getOrDefault("ADMIN_GROUP_NAME", adminGroupId);

            if (type.equals("ente") && adminGroupId!=null) {

                Group ente = (Group) bean;
                ente.setStruttura(true);

                Group cuAdmins = new Group();

                cuAdmins.setCodEnte(ente.getCodEnte());
                cuAdmins.setGroupId(adminGroupId);
                cuAdmins.setGroupName(adminGroupName);
                cuAdmins.setEmail(ente.getEmail());
                cuAdmins.setStruttura(false);
                cuAdmins.setPrefix(ente.getPrefix());
                map.put( ClientUtils.getSolrId(null,null,suffix(ente.getPrefix())+cuAdmins.getGroupId(),Group.TYPE) , cuAdmins );
                //map.put( prefix+ClientUtils.getSolrId(null,null,cuAdmins.getGroupId(),Group.TYPE) , cuAdmins );
            }

            if (type.equals("aoo") && adminGroupId!=null){

                Group aoo = (Group) bean;
                aoo.setStruttura(true);

                Group cuAdmins = new Group();
                cuAdmins.setCodEnte(aoo.getCodEnte());
                cuAdmins.setCodAoo(aoo.getCodAoo());
                cuAdmins.setGroupId(adminGroupId);
                cuAdmins.setGroupName(adminGroupName);
                cuAdmins.setEmail(aoo.getEmail());
                cuAdmins.setStruttura(false);
                cuAdmins.setPrefix(aoo.getPrefix());
                map.put( ClientUtils.getSolrId(null,null,suffix(aoo.getPrefix())+cuAdmins.getGroupId(),Group.TYPE) , cuAdmins );
            }
        }
        return old;
    }

    /*private String getSolrId(String sid, String type){

        if ("null".equals(sid) || Strings.isNullOrEmpty(sid))
            return null;

        String prefix = "";
        String id;

        if  ((User.TYPE.equals(type) || Group.TYPE.equals(type))){
            //prefix = getCurrentPrefix();
            if (!sid.contains("__")){
                prefix = Current.getPrefix();
                sid = prefix+sid;
            }

            id = ClientUtils.getSolrId(null,null,sid,type);
        } else if ("ente".equals(type)){
            id = ClientUtils.getSolrId(null,null,sid,type);
        } else if ("aoo".equals(type)){
            id = ClientUtils.getSolrId(Current.getCodEnte(),null,sid,type);
        } else {
            id = ClientUtils.getSolrId(Current.getCodEnte(),Current.getCodAOO(),sid,type);
        }
        return id;
    }*/

    /*public DocerBean getItem(String sid, String type){
        return getItem(getSolrId(sid,type));
    }*/

    public DocerBean getItem(String id){

        //String id = getSolrId(sid,type);
        if (!id.contains("@"))
            return null;
        String type = id.split("@")[1];

        if ("aoo".equals(type))
            return getAOOsChecked().get(id.split("!")[1]);

        if ("ente".equals(type))
            return getEntiChecked().get(id.split("!")[0].split("\\.")[1]);

        DocerBean bean;
        if (map.containsKey(id)){
            bean = map.get(id);

            long expiration = getExpiration(bean);
            if (expiration<System.currentTimeMillis()){
                removeItem(id);
            } else{
                return bean;
            }
        }

        Map doc = querySolrItem(id,null);
        bean = getDocerBean(doc);
        putItem(id,bean);
        return bean;
    }

    /*public DocerBean removeItem(String sid,String type){
        synchronized (map){
            return map.remove(getSolrId(sid,type));
        }
    }*/

    public DocerBean removeItem(String id){
        synchronized (map){
            DocerBean bean = map.remove(id);
            if (bean!=null) {
                if ("aoo".equals(bean.getType()))
                    _aoos.remove(bean.getDocerId());
                if ("ente".equals(bean.getType()))
                    _enti.remove(bean.getDocerId());
            }
            return bean;
        }
    }

    /*private User getUser(String username){
        return getUser( Current.getCodEnte() , username);
    }*/

    public Group getAOOforRealm(String realm){
        if (realm==null)
            return null;
        //realm = realm.toUpperCase();

        Group aoo = this.getGroup(null,realm);

        if (aoo==null)
            return null;

        if (aoo.isAOO())
            return aoo;

        if (!aoo.isEnte())
            return null;

        String def = (String) aoo.otherFields().get("DEFAULT_AOO");

        if (def!=null)
            return this.getAOO(def);

        //se non è specificata una aoo di default prendo la prima
        Collection<Group> aoos = ClientCache.getInstance().getAllAOOs();
        for ( Group x : aoos ){
            if (x.getCodEnte().equals(realm)) {
                return x;
            }
        }

        return null;
    }

    public User getUser(String codEnteOrAoo, String username){

        if ("admin".equals(username) || "Administrator".equals(username)){
            Group enteOrAoo = this.getEnteOrAoo(codEnteOrAoo);
            //User admin = ClientCache.getInstance().getUser(null,username);
            User admin = new User("admin");
            admin.setGroups(new String[] {"admins","everyone"} );
            admin.setFullName("admin");
            admin.setPrefix("");
            admin.setCodEnte("system");
            admin.setCodAoo("system");

            if (adminmail==null || admingroups==null){
                Map adminSolr = querySolrItem("admin@user",null);
                if (adminSolr!=null) {
                    adminmail = (String) adminSolr.getOrDefault("EMAIL_ADDRESS", adminSolr.get("USER_MAIL"));
                    if (adminmail == null)
                        adminmail = "";
                    else
                        logger.info("admin email:" + adminmail);
                    Collection<String> grps  = (Collection<String>) adminSolr.get("groups");
                    if (grps!=null && grps.size()>0) {
                        admingroups = grps.toArray(new String[0]);
                        logger.info("admin groups:" + admingroups);
                    } else
                        admingroups = new String[0];
                } else {
                    adminmail = "";
                    admingroups = new String[0];
                }
            }

            if (!Strings.isNullOrEmpty(adminmail))
                admin.setEmail(adminmail);

            if (admingroups!=null && admingroups.length>0)
                admin.setGroups(admingroups);

            if (enteOrAoo!=null){
                //admin.setPrefix(enteOrAoo.getPrefix());
                admin.setCodEnte(enteOrAoo.getCodEnte());
                if (enteOrAoo.isAOO())
                    admin.setCodAoo(enteOrAoo.getCodAoo());
            }

            return admin;
        }

        if ("guest".equals(username)){
            Group aoo = getAOOforRealm(codEnteOrAoo);

            if (aoo==null || !aoo.isAOO()){
                throw new RuntimeException("codEnteOrAoo non valido:"+codEnteOrAoo);
            }

            User guest = new User("guest");
            guest.setGroups(new String[] {aoo.getCodEnte(),aoo.getCodAoo()} );
            guest.setFullName("guest");
            guest.setPrefix("");
            guest.setCodEnte(aoo.getCodEnte());
            guest.setCodAoo(aoo.getCodAoo());

            return guest;
        }

        if (!"Administrator".equals(username) && !"admin".equals(username) && !"guest".equals(username))
            username = suffix(getPrefix(codEnteOrAoo)) + username;

        User user = (User) getItem( ClientUtils.getSolrId(null,null, username,User.TYPE) );

        if (user==null)
            return null;

        Group enteOrAoo = this.getEnteOrAoo(codEnteOrAoo);

        if (enteOrAoo!=null){
            user.setCodEnte(enteOrAoo.getCodEnte());
            if (enteOrAoo.isAOO())
                user.setCodAoo(enteOrAoo.getCodAoo());
        }

        if (user.isAdmin())
            return user;
        List<String> groups = Arrays.asList(user.getGroups());
        if (codEnteOrAoo!=null && !groups.contains(codEnteOrAoo))
            return null;
        return user;
    }

    /*public Group getGroup(String groupId){
        if (getAOOsChecked().containsKey(groupId))
            return getAOOsChecked().get(groupId);

        if (getEntiChecked().containsKey(groupId))
            return getEntiChecked().get(groupId);

        return getGroup( Current.getCodEnte(), groupId);
    }*/

    public Group getGroup(String codEnteOrAoo, String groupId){

        //Group aoo = getAOO(groupId);
        Group aoo = getAOOsChecked().get(groupId);
        if (aoo!=null && (codEnteOrAoo==null || groupId.equals(codEnteOrAoo) || aoo.getCodEnte().equals(codEnteOrAoo)))
            return aoo;

        //Group ente = getEnte(groupId);
        Group ente = getEntiChecked().get(groupId);
        if (ente!=null && (codEnteOrAoo==null || groupId.equals(codEnteOrAoo)))
            return ente;

        final String groupId0 = suffix(getPrefix(codEnteOrAoo)) + ("Administrators".equals(groupId) ? "SYS_ADMINS" : groupId);
        return (Group) getItem( ClientUtils.getSolrId(null,null, groupId0,Group.TYPE) );
    }

    public Actor getActor(String codEnteOrAoo, String actor){

        Group g = getGroup(codEnteOrAoo, actor);

        if (g!=null)
            return g;

        User u = getUser(codEnteOrAoo, actor);

        if (u!=null)
            return u;

        return null;
    }

    public Group getEnteOrAoo(String codEnteOrAoo){
        Group ente = getEnte(codEnteOrAoo);
        if (ente!=null)
            return ente;
        else
            return getAOO(codEnteOrAoo);
    }

    public Group getEnte(String codEnte){
        if ("*".equals(codEnte) || "system".equals(codEnte))
            return null;
        Group g =  getEntiChecked().get(codEnte);
        if (g==null){
            if (_aoos.containsKey(codEnte))
                return null;
            String id = ClientUtils.getSolrId(null,null,codEnte,Group.TYPE_ENTE);
            Map doc = querySolrItem(id,null);
            if (doc!=null) {
                g = (Group) getDocerBean(doc);
                putItem(id, g);
            }
        }
        return g;
    }

    public Group getAOO(String codAoo){
        if ("*".equals(codAoo) || "system".equals(codAoo))
            return null;
        Group g = getAOOsChecked().get(codAoo);
        if (g==null){
            if (_enti.containsKey(codAoo))
                return null;
            List<Map> docs = querySolrItems("type:aoo AND COD_AOO:"+codAoo,null);
            if (docs!=null && docs.size()>0) {
                g = (Group) getDocerBean(docs.get(0));
                putItem(g.getSolrId(), g);
            }
        }
        return g;
    }

    public Collection<Group> getAllAOOs() {
        return getAOOsChecked().values();
    }

    public Collection<Group> getAllEnti() {
        return getEntiChecked().values();
    }

    private List<Map> querySolrItems(String q, String fl){

        if (queryHelper!=null){
            return queryHelper.getItems(q,fl);
        }

        String url = "/select?q="+q;
        if (fl!=null)
            url += "&fl=" + fl;

        url += "&rows=10000";

        Map response = client.get().uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .block();


        List<Map> docs = (List<Map>) response.get("data");

        return docs;
    }

    private Map querySolrItem(String id, String fl){

        if (queryHelper!=null){
            List<Map> docs = queryHelper.getItems(id,fl);
            if (docs.size()>0)
                return docs.get(0);
            else
                return null;
        }

        Map response = client.get().uri("/get?ticket=admin&id="+id)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map> docs = (List<Map>) response.get("data");
        if (docs.size()>0)
            return docs.get(0);
        else
            return null;
    }

    private String getPrefixById(String id){
        if (id.endsWith("@ente") || id.endsWith("@aoo")) {
            return "";
        } else if (id.contains("!") && id.contains(".")){
            String codEnte = id.split("!")[0].split("\\.")[1];
            return getPrefix(codEnte);
        } else if (id.contains("__")){
            return id.split("__")[0];
        } else
            return "";
    }

    private DocerBean getDocerBean(Map doc){

        if (doc==null)
            return null;

        String id = (String) doc.get("id");
        String type = id.split("@")[1];
        String prefix = getPrefixById(id);

        Class cls;
        try {
            cls = Class.forName("keysuite.docer.client."+ org.apache.commons.lang.StringUtils.capitalize(type));
        } catch (ClassNotFoundException e) {
            if ("aoo".equals(type) || "ente".equals(type))
                cls = Group.class;
            else
                cls = Anagrafica.class;
        }

        if ("aoo".equals(type) || "ente".equals(type) || "group".equals(type) || "user".equals(type) ){

            //String ID = (String) doc.getOrDefault("USER_ID", doc.get("GROUP_ID"));
            if ("aoo".equals(type) || "ente".equals(type)) {
                String ID = (String) doc.get("GROUP_ID");

                if (ID!=null && ID.contains("__")) {
                    prefix = ID.split("__")[0] /*+"__"*/ ;
                }
            }

            //if ( !Strings.isNullOrEmpty(prefix) && !prefix.endsWith("__") )
            //    prefix += "__";

            doc.put("prefix",prefix);

        } /*else {
            if (id.contains("!") && id.contains(".")){
                String codEnte = id.split("!")[0].split("\\.")[1];
                prefix = getPrefix(codEnte);
            }
        }*/

        if ( !Strings.isNullOrEmpty(prefix) ){

            //if (!prefix.endsWith("__"))
            //    prefix += "__";
            prefix = suffix(prefix);

            for( String field : prefixedValues ){
                String val = (String) doc.get(field);
                if (val!=null && val.startsWith(prefix)){
                    doc.put(field, val.substring(prefix.length()) );
                }
            }

            for( String field : prefixedArrays ){
                Collection<String> vals = (Collection) doc.get(field);
                if (vals!=null){
                    List<String> newvals = new ArrayList<>();
                    for( String val : vals ){
                        if (val!=null && val.startsWith(prefix)){
                            newvals.add(val.substring(prefix.length()));
                        } else {
                            newvals.add(val);
                        }
                    }
                    doc.put(field, newvals);
                }
            }
        }

        DocerBean bean = ClientUtils.toClientBean(doc,cls);

        return bean;
    }

    public Collection<Group> getUserGroups(String codEnteOrAoo, String username){
        //List<String> groups = ClientCache.getGroups(user);
        User user = getUser(codEnteOrAoo,username);

        Group aoo = getAOO(codEnteOrAoo);
        Group ente = null;

        if (aoo!=null){
            ente = getEnte(aoo.getCodEnte());
        } else {
            ente = getEnte(codEnteOrAoo);
        }

        if (ente==null && !"system".equals(codEnteOrAoo))
            throw new RuntimeException("codEnteOrAoo not found:"+codEnteOrAoo);

        List<String> groupsIds = Arrays.asList(user.getGroups());

        Collection<Group> groups = new ArrayList<>();
        if (ente!=null)
            groups.add(ente);

        if (aoo!=null)
            groups.add(aoo);

        for (String solrGrpId : groupsIds) {

            String grp = solrGrpId.split("@")[0];

            if (grp.equals("everyone") || grp.equals("admins")) {
                groups.add(getGroup(null,grp));
                continue;
            }

            Group cGroup = getGroup(ente.getCodEnte(),grp);

            if (cGroup == null)
                continue;

            String groupEnte = cGroup.getCodEnte();
            String groupAoo = cGroup.getCodAoo();

            if (groupAoo != null && aoo != null && !aoo.getCodAoo().equals(groupAoo))
                continue;

            if (groupEnte != null && ente!=null && !ente.getCodEnte().equals(groupEnte))
                continue;

            if(groups.contains(cGroup))
                continue;

            groups.add(cGroup);
        }
        return groups;
    }

    public static String suffix(String prefix){
        if (prefix==null)
            return "";
        if (Strings.isNullOrEmpty(prefix) || prefix.endsWith("__"))
            return prefix;
        return prefix + "__";
    }

    private String getPrefix(String codEnteOrAoo){

        if (codEnteOrAoo==null)
            return "";

        Group ente = getEnte(codEnteOrAoo);
        if (ente!=null)
            return ente.getPrefix();

        Group aoo = getAOO(codEnteOrAoo);
        if (aoo!=null)
            return aoo.getPrefix();

        return "";
    }

    /*public void setThreadJWTToken(String jwtToken){
        if (jwtToken!=null) {
            Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
            threadClaims.set(claims);
        } else {
            threadClaims.set(null);
        }
    }*/


}
