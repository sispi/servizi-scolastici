package it.kdm.orchestratore.session;

import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.docer.client.Actor;
import keysuite.docer.client.Group;
import keysuite.docer.client.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ActorsCache implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ActorsCache.class);

    static ThreadLocal<String> threadCodEnte = new ThreadLocal<>();

    public static void setThreadCodEnte(String codEnte){
        threadCodEnte.set(codEnte);
    }

    public static String getThreadCodEnte(){
        return threadCodEnte.get();
    }

    public static String getPrefix(String codEnte){
        Group ente = ClientCache.getInstance().getEnte(codEnte);
        if (ente==null)
            return "";
        else
            return ente.getPrefix();
    }

    public User getUser(String username){
        return ClientCache.getInstance().getUser( threadCodEnte.get(), username);
    }

    public Group getGroup(String groupId){
        return ClientCache.getInstance().getGroup( threadCodEnte.get(), groupId);
    }

    //public final static String ACTUAL_OWNER = "$actualOwner";
    //public final static String POT_OWNERS = "$owners";

    private static ActorsCache instance;

    //private Map<String,CacheUser> users = new HashMap<>();
    //private Map<String,CacheGroup> groups = new HashMap<>();



    //final static int period = Integer.parseInt(ToolkitConnector.getGlobalProperty("actorscache.duration","30"));

    public static ActorsCache getInstance(){

        if(instance== null){
            instance = new ActorsCache();
        }
        return instance;
    }

    private ActorsCache(){
        //this.clientCache = clientCache;
        //refresh();
        /*Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, period*60*1000,period*60*1000);*/
    }

    public Integer refresh(){
        return ClientCache.getInstance().refresh();
    }

    public Integer getCount(){
        return ClientCache.getInstance().getCount();
    }

    public Integer init(){
        ClientCache.getInstance().init();
        return getCount();
    }

    public Group getAOO(String codAoo){
        return ClientCache.getInstance().getAOO(codAoo);
    }

    public Group getEnte(String codEnte){
        return ClientCache.getInstance().getEnte(codEnte);
    }

    private static String getCurrentCodEnte(){

        String codEnte = getThreadCodEnte();

        if (codEnte!=null)
            return codEnte;

        if (Session.getRequest()!=null){
            String auth = Session.getRequest().getHeader("Authorization");
            if (auth!=null)
                return ClientCacheAuthUtils.getInstance().parseClaims(auth).getSubject();
        }
        return null;
    }

    public Collection<User> getUsersInGroup(String groupId){
        return ClientCache.getInstance().getUsersInGroup(getCurrentCodEnte(),groupId);
    }

    public User getUserForUsername(String username){
        return ClientCache.getInstance().getUser(getCurrentCodEnte(),username);
    }

    public Group getGroupForGroupId(String groupId){
        return ClientCache.getInstance().getGroup(getCurrentCodEnte(),groupId);
    }

    public static String getDisplayName(String actor){
        Actor cActor = ClientCache.getInstance().getActor(getCurrentCodEnte(),actor);
        if (cActor!=null)
            return cActor.getDisplayName();
        else
            return null;
    }

    public static String getName(String actor){
        Actor cActor = ClientCache.getInstance().getActor(getCurrentCodEnte(),actor);
        if (cActor!=null)
            return cActor.getName();
        else
            return null;
    }

    public Collection<Group> getEnti() {
        return ClientCache.getInstance().getAllEnti();
    }

    public Collection<Group> getAOO() {
        return ClientCache.getInstance().getAllAOOs();
    }


    public User getUser(String codEnte, String username){
        return ClientCache.getInstance().getUser(codEnte,username);
    }


    public Group getGroup(String codEnte, String groupId){
        return ClientCache.getInstance().getGroup(codEnte,groupId);
    }

    public Actor getActor(String codEnte, String actor){
        return ClientCache.getInstance().getActor(codEnte,actor);
    }

    public static String[] getNames(String... actors){
        if (actors==null || actors.length==0)
            return new String[0];
        String[] out = new String[actors.length];
        for( int i=0; i<actors.length; i++)
            out[i] = getName(actors[i]);
        return out;
    }

    public static String[] getDisplayNames(String... actors){
        if (actors==null || actors.length==0)
            return new String[0];
        String[] out = new String[actors.length];
        for( int i=0; i<actors.length; i++)
            out[i] = getDisplayName(actors[i]);
        return out;
    }

    public static List<String> getGroups(String user){

        User cactor = getInstance().getUser(user);

        if (cactor instanceof User)
            return Arrays.asList(cactor.getGroups());
        else
            return null;
    }

    public static void setActor(Actor cacheActor){
        ClientCache.getInstance().putActor(cacheActor);
    }

    public static Collection<Group> getGroups(String user, String codEnte, String codAoo){
        if (codAoo!=null)
            return ClientCache.getInstance().getUserGroups(codAoo,user);
        else
            return ClientCache.getInstance().getUserGroups(codEnte,user);
    }

}
