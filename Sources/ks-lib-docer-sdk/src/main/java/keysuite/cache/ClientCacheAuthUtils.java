package keysuite.cache;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.client.Group;
import keysuite.docer.client.User;
//import keysuite.docer.sdk.APIClient;
//import keysuite.docer.sdk.APIFactory;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.WebUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientCacheAuthUtils {

    //public static final String UID_KEY = "uid";
    public static final String AOO_KEY = "aoo";
    public static final String ENTE_KEY = "ente";
    public static final String DISPLAY_NAME_KEY = "name";
    public static final String EMAIL_KEY = "email";
    public static final String GROUPS_KEY = "groups";
    public static final String ROLES_KEY = "roles";
    public static final String LANGUAGE_KEY = "language";
    public static final String COUNTRY_KEY = "country";
    public static final String SUBJECT_KEY = "sub";


    private static final Logger logger = LoggerFactory.getLogger(ClientCacheAuthUtils.class);

    @FunctionalInterface
    public interface ThreadBearerResolver {
        String bearer();
    }

    @FunctionalInterface
    public interface CreateUserHelper {
        User create(User user) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound;
    }

    @FunctionalInterface
    public interface UpdateUserHelper {
        User update(User user) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound;
    }

    static ClientCacheAuthUtils instance;

    public static ClientCacheAuthUtils getInstance(){
        if (instance==null){
            instance = new ClientCacheAuthUtils();
        }
        return instance;
    }

    private String getSecretKey(){
        return System.getProperty("secretKey","SECRET");
    }

    public static String generateMD5(String str){
        return DigestUtils.md5DigestAsHex((str + System.getProperty("secretKey","SECRET") ).getBytes());
    }

    /*private APIClient getAdminAPIClient(String codAoo){

        String jwt = simpleJWTToken(codAoo,"admin");

        APIClient APIClient = ((keysuite.docer.sdk.APIClient) APIFactory.getClient(docerUrl,jwt))
                .setTimeout(20000)
                .setClientRetries(500L,1500L)
                .setServerRetries(500L,1500L);
        return APIClient;
    }*/

    public ClientCacheAuthUtils(){

        docerUrl = System.getProperty("docer.url","http://localhost:8080");

        if (docerUrl==null){
            throw new RuntimeException("docer.url deve essere configurato");
        }

        /*if (!docerUrl.endsWith("/docer/v1/")){
            docerUrl += "/docer/v1/";
        }*/
    }

    private String docerUrl;
    //private boolean autoSyncUser;

    private static ThreadBearerResolver threadBearerResolver = null;
    private static CreateUserHelper createUserHelper = null;
    private static UpdateUserHelper updateUserHelper = null;

    public static void setThreadBearerResolver(ThreadBearerResolver threadBearerResolver){
        ClientCacheAuthUtils.threadBearerResolver = threadBearerResolver;
    }

    public static void setSyncHelpers(CreateUserHelper createUserHelper, UpdateUserHelper updateUserHelper){
        ClientCacheAuthUtils.updateUserHelper = updateUserHelper;
        ClientCacheAuthUtils.createUserHelper = createUserHelper;
    }

    public static boolean isHelpersDefined(){
        return updateUserHelper!=null && createUserHelper!=null;
    }

    static ThreadLocal<User> threadUser = new ThreadLocal<>();

    public User setThreadUser(String jwtToken){
        return setThreadUser(parseClaims(jwtToken));
    }

    public User setThreadUser(Claims claims){
        User user = getUserByClaims(claims);
        threadUser.set(user);
        return user;
    }

    /*public static void setThreadUser(User user){
        threadUser.set(user);
    }*/

    public User getUserByClaims(Map<String,Object> claims){

        if (claims==null)
            return null;

        String username = (String) claims.get("sub");
        String codAoo = (String) claims.getOrDefault("aoo", claims.get("aud"));

        //Object oGroup = claims.get(GROUPS_KEY);

        User user = null;

        if (isHelpersDefined() && !"system".equals(codAoo) && !"admin".equals(username) && !"guest".equals(username)){
            try {

                //claims.put(AOO_KEY,codAoo);
                //claims.put(SUBJECT_KEY,username);
                user = syncUser(claims);
            } catch(Exception e) {
                logger.error("non è stato possibile aggiornare l'utente:"+username,e);
                if (e instanceof KSRuntimeException)
                    throw (KSRuntimeException) e;
                else
                    throw new KSRuntimeException(e,"non è stato possibile aggiornare l'utente:"+username);

                //throw new RuntimeException("non è stato possibile aggiornare l'utente:"+username,e);
            }
        } else {
            user = getUser(codAoo,username);
        }

        return user;
    }

    private Map<String,Object> checkClaims(Map<String,Object> claims) throws KSExceptionNotFound{
        claims = new HashMap<>(claims);

        String codAoo = (String) claims.get(AOO_KEY);
        //String username = (String) claims.get(SUBJECT_KEY);

        Group aoo = ClientCache.getInstance().getAOO(codAoo);

        if (aoo==null)
            throw new KSExceptionNotFound("Aoo non trovata:"+codAoo);

        claims.put(GROUPS_KEY, getlist(aoo, claims.get(GROUPS_KEY), true ));
        claims.put(ROLES_KEY, getlist(aoo, claims.get(ROLES_KEY), false ));

        for( String k : claims.keySet().toArray(new String[0]) ) {
            if ("".equals(claims.get(k)))
                claims.remove(k);
        }

        return claims;
    }

    private List<String> getlist(Group aoo, Object _grps, boolean struttura){

        if (_grps==null)
            return null;

        List<String> groups = null;

        if (_grps instanceof String){
            if (Strings.isNullOrEmpty((String)_grps))
                groups = new ArrayList<>();
            else
                groups = new ArrayList<>(Arrays.asList(((String)_grps).split(",")));
        } else if (_grps instanceof List) {
            groups = (List) _grps;
        } else if (_grps instanceof String[]) {
            groups = new ArrayList<>(Arrays.asList((String[])_grps));
        }

        String codEnte = aoo.getCodEnte();
        String codAoo = aoo.getCodAoo();

        groups.remove(codAoo);
        groups.remove(codEnte);
        groups.remove("everyone");
        //groups.remove(aoo.getPrefix()+codAoo);
        //groups.remove(aoo.getPrefix()+codEnte);
        for( String grp : groups.toArray(new String[0])){
            Group g = ClientCache.getInstance().getGroup(codAoo,grp);
            if (g==null || g.isStruttura() != struttura) {
                groups.remove(grp);
                continue;
            }

            if (!"true".equals(g.otherFields().get("GRUPPO_SSO"))){
                groups.remove(grp);
                continue;
            }

            String groupAoo = g.getCodAoo();

            if (groupAoo != null && !codAoo.equals(groupAoo)) {
                groups.remove(grp);
                continue;
            }

            String groupEnte = g.getCodEnte();

            if (groupEnte != null && !codEnte.equals(groupEnte)) {
                groups.remove(grp);
                continue;
            }
        }
        return groups;
    }

    private List<String> getCurrentSSOGroups(String codAoo, String[] groups){
        List<String> list = new ArrayList<>();
        for( String g : groups ){
            Group grp = ClientCache.getInstance().getGroup(codAoo, g);
            if (grp==null)
                continue;
            if ("true".equals(grp.otherFields().get("GRUPPO_SSO")))
                list.add(g);
        }
        return list;
    }

    public boolean checkExistingUser(User user, Map<String,Object> claims){

        String username = (String) claims.remove(SUBJECT_KEY);
        String fullName = (String) claims.remove(DISPLAY_NAME_KEY);
        String email = (String) claims.remove(EMAIL_KEY);
        List<String> groups = (List) claims.remove(GROUPS_KEY);
        List<String> roles = (List) claims.remove(ROLES_KEY);
        String codAoo = (String) claims.remove(AOO_KEY);

        Group aoo = ClientCache.getInstance().getAOO(codAoo);

        String codEnte = aoo.getCodEnte();

        boolean checkRoles = roles != null;
        boolean checkGroups = groups != null;

        boolean fnChanged = fullName!=null && !user.getFullName().equals(fullName);
        boolean emailChanged = false;

        if (email!=null){
            String ce = user.getEmail();
            if (ce==null)
                ce = "";
            emailChanged = !ce.equals(email);
        }

        boolean grpChanged = groups != null || roles != null;
        boolean anyChange = fnChanged || emailChanged;

        if (emailChanged)
            logger.warn("Per l'utente "+ username+"("+codAoo+") EMAIL_ADDRESS è cambiato da "+user.getEmail()+ " a "+email);

        if (fnChanged)
            logger.warn("Per l'utente "+ username+"("+codAoo+") FULL_NAME è cambiato da "+user.getFullName()+ " a "+fullName);

        if (!anyChange){
            Map<String,Object> props = user.otherFields();

            for( String k : claims.keySet() ){

                if (k.matches(".*[a-z].*")) /* solo maiuscoli */
                    continue;

                Object v1 = props.get(k);
                Object v2 = claims.get(k);

                if ("".equals(v1))
                    v1 = null;
                if ("".equals(v2))
                    v2 = null;

                if (v1==null && v2==null)
                    continue;
                if (v1==null || v2==null || !v1.equals(v2)) {
                    anyChange = true;
                    logger.warn("Per l'utente "+ username+"("+codAoo+") il campo "+k+" è cambiato da "+v1+ " a "+v2);
                    break;
                }
            }
        }

        List<String> toAdd=null;
        List<String> toRemove=null;
        //String[] actualGroups=null;

        if (grpChanged){

            //String[] usergroups = user.getGroups();
            /*List<String> currentGroups = new ArrayList(Arrays.asList(user.getGroups()));
            currentGroups.remove(codAoo);
            currentGroups.remove(codEnte);
            currentGroups.remove("everyone");*/
            List<String> currentGroups = getCurrentSSOGroups(codAoo, user.getGroups());
            //currentGroups.remove(aoo.getPrefix()+codAoo);
            //currentGroups.remove(aoo.getPrefix()+codEnte);

            for( String grp : currentGroups.toArray(new String[0])){
                Group g = ClientCache.getInstance().getGroup(codAoo,grp);
                if (g==null) {
                    logger.warn("gruppo non trovato:" + codAoo+"/"+ grp);
                    currentGroups.remove(grp);
                    continue;
                }

                if (!checkRoles && !g.isStruttura())
                    currentGroups.remove(grp);
                if (!checkGroups && g.isStruttura())
                    currentGroups.remove(grp);
            }

            List<String> allgroups = new ArrayList();

            if (checkGroups) {
                /*for( String grp : groups.toArray(new String[0])){
                    Group g = ClientCache.getInstance().getGroup(codAoo,grp);
                    if (g==null || !g.isStruttura())
                        groups.remove(grp);
                }*/
                allgroups.addAll(groups);
            }

            if (checkRoles) {
                /*for( String grp : roles.toArray(new String[0])){
                    Group g = ClientCache.getInstance().getGroup(codAoo,grp);
                    if (g==null || g.isStruttura())
                        roles.remove(grp);
                }*/
                allgroups.addAll(roles);
            }

            /*for ( String grp : allgroups ){
                Group g = ClientCache.getInstance().getGroup(codAoo,grp);
                boolean remove = (g==null);

                if (!remove){
                    String groupEnte = g.getCodEnte();
                    String groupAoo = g.getCodAoo();

                    if (groupAoo != null && !codAoo.equals(groupAoo))
                        remove = true;
                    else if (groupEnte != null && !codEnte.equals(groupEnte))
                        remove = true;
                }

                if (remove)
                    allgroups.remove(grp);
            }*/

            toAdd = new ArrayList<>(allgroups);
            toAdd.removeAll(currentGroups);

            toRemove = new ArrayList<>(currentGroups);
            toRemove.removeAll(allgroups);

            grpChanged = toAdd.size() > 0 || toRemove.size() > 0;

            if (grpChanged) {
                logger.debug("jwt disallineato con cache:"+codAoo+" "+username+" "+groups+"-"+currentGroups);

                ClientCache.getInstance().removeItem(user.getSolrId());

                User freshuser = getUser(codAoo, username);

                /*currentGroups = new ArrayList(Arrays.asList(freshuser.getGroups()));
                currentGroups.remove(codAoo);
                currentGroups.remove(codEnte);
                currentGroups.remove("everyone");*/
                currentGroups = getCurrentSSOGroups(codAoo, freshuser.getGroups());

                for( String grp : currentGroups.toArray(new String[0])){
                    Group g = ClientCache.getInstance().getGroup(codAoo,grp);
                    if (g==null) {
                        logger.warn("gruppo non trovato:" + codAoo+"/"+ grp);
                        currentGroups.remove(grp);
                        continue;
                    }

                    if (!checkRoles && !g.isStruttura())
                        currentGroups.remove(grp);
                    if (!checkGroups && g.isStruttura())
                        currentGroups.remove(grp);
                }

                //currentGroups = new ArrayList(Arrays.asList(freshuser.getGroups()));
                //currentGroups.remove(codAoo);
                //currentGroups.remove(codEnte);

                toAdd = new ArrayList<>(allgroups);
                toAdd.removeAll(currentGroups);

                toRemove = new ArrayList<>(currentGroups);
                toRemove.removeAll(allgroups);

                grpChanged = toAdd.size() > 0 || toRemove.size() > 0;

                //actualGroups = freshuser.getGroups();
                if (grpChanged)
                    anyChange = true;
            }
        }

        if (anyChange){
            if(grpChanged){

                List<String> diff = new ArrayList<>();

                for( String grp : toAdd )
                    diff.add("+"+grp);
                for( String grp : toRemove )
                    diff.add("-"+grp);

                user.setGroups(diff.toArray(new String[0]));

                logger.warn("Per l'utente "+ username+"("+codAoo+") i gruppi sono disallineati:"+diff);

            }

            user.setFullName(fullName);
            user.setEmail(email);

            Map<String,Object> props = user.otherFields();

            for( String k : claims.keySet() ){
                Object v1 = props.get(k);
                Object v2 = claims.get(k);
                if (v1==null && v2==null)
                    continue;
                if (v1==null || v2==null || !v1.equals(v2)) {
                    props.put(k, v2);
                    if (v2==null)
                        user.addNullField(k);
                }
            }
        }

        return anyChange;
    }

    public User setNewUser(Map<String,Object> claims){

        String username = (String) claims.remove(SUBJECT_KEY);
        String fullName = (String) claims.remove(DISPLAY_NAME_KEY);
        String email = (String) claims.remove(EMAIL_KEY);
        List groups = (List) claims.remove(GROUPS_KEY);
        List roles = (List) claims.remove(ROLES_KEY);
        String codAoo = (String) claims.remove(AOO_KEY);

        Group aoo = ClientCache.getInstance().getAOO(codAoo);

        String codEnte = aoo.getCodEnte();

        if (Strings.isNullOrEmpty(fullName))
            fullName = username;

        User newuser = new User();
        newuser.setUserName(username);
        newuser.setFullName(fullName);
        newuser.setCodAoo(codAoo);
        newuser.setCodEnte(codEnte);

        if (!Strings.isNullOrEmpty(email))
            newuser.setEmail(email);

        List<String> grpList = new ArrayList<>();
        grpList.add(codEnte);
        grpList.add(codAoo);
        grpList.add("everyone");

        if (groups != null){
            grpList.addAll(groups);
        }

        if (roles != null){
            grpList.addAll(roles);
        }

        newuser.setGroups(grpList.toArray(new String[0]));
        newuser.setPassword(UUID.randomUUID().toString().replace("-","").substring(0,10));

        for( String k : claims.keySet() ){
            Object value = claims.get(k);
            if (value!=null && !"".equals(value))
                newuser.otherFields().put(k, value);
        }

        return newuser;
    }

    public final static String USERNAME_PATTERN = "^[a-zA-Z0-9][-a-zA-Z0-9-_\\.]{1,100}[a-zA-Z0-9]$";

    public User syncUser(Map<String,Object> claims) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        String codAoo = (String) claims.getOrDefault(AOO_KEY, claims.get("aud"));
        String username = (String) claims.get(SUBJECT_KEY);

        if (codAoo==null || username==null)
            throw new KSRuntimeException("aoo e sub sono claims obbligatori");

        //String fullName = (String) claims.get(DISPLAY_NAME_KEY);
        //String email = (String) claims.get(EMAIL_KEY);

        User user = getUser(codAoo,username);
        //claims = new HashMap<>(claims);

        if (user == null) {
            if (!username.matches(USERNAME_PATTERN))
                throw new KSExceptionBadRequest("username non valido:"+username);

            logger.info("user non esistente:"+codAoo+" "+username);

            claims = checkClaims(claims);

            user = setNewUser(claims);

            user = createUserHelper.create(user);

            logger.info("user creato:"+codAoo+" "+username);

            ClientCache.getInstance().putActor(user);

            user = getUser(codAoo, username);

        } else  {

            claims = checkClaims(claims);

            boolean changed = checkExistingUser(user,claims);
            if (changed) {
                logger.info("user cambiato:" + codAoo + " " + username);

                user = updateUserHelper.update(user);
                logger.info("user aggiornato:" + codAoo + " " + username);

                ClientCache.getInstance().putActor(user);

                user = getUser(codAoo, username);
            }
        }
        return user;
    }

    /*public User getCurrentByRequest(){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getCurrentByToken(req.getHeader("Authorization"));
    }*/

    public String simpleJWTToken(String codAoo,String username){
        return ClientUtils.simpleJWTToken(codAoo,username,getSecretKey());
    }

    public String simpleJWTToken(String codAoo, String username, String fullname, String... groups){
        Map<String,Object> claims = new HashMap<>();
        claims.put(DISPLAY_NAME_KEY,fullname);
        if (groups != null && groups.length>0)
            claims.put(GROUPS_KEY,Arrays.asList(groups));

        return ClientUtils.simpleJWTToken(codAoo,username,getSecretKey(),claims);
    }

    public String simpleJWTToken(String codAoo, String username, Map<String,Object> claims){
        return ClientUtils.simpleJWTToken(codAoo,username,getSecretKey(),claims);
    }

    public static String getRequestBearer(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        if (bearer==null){
            Cookie ssoUserCookie = WebUtils.getCookie( request , "SSO_USER");
            if (ssoUserCookie!=null)
                bearer = ssoUserCookie.getValue();
        }
        if (bearer==null)
            bearer = (String) request.getAttribute("Authorization");
        return bearer;
    }

    public static String getRequestRealm(String url){
        String pattern = System.getProperty("realm.pattern","https?://([^\\.]+)\\.");
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(url);
        if (m.find() && m.groupCount()>0) {
            String g = m.group(1);
            return System.getProperty("realm." + g.toUpperCase(), g.toUpperCase());
        } else
            return System.getProperty("realm.default");
    }

    private User getUser(String codEnteOrAoo,String username){

        User current = ClientCache.getInstance().getUser(codEnteOrAoo,username);
        if (current==null) {
            return null;
        }
        current = current.copy();

        Group enteOrAoo = ClientCache.getInstance().getEnteOrAoo(codEnteOrAoo);
        if (enteOrAoo!=null){

            String codAoo;

            if (enteOrAoo.isEnte()){
                codAoo = null;
            } else {
                codAoo = enteOrAoo.getCodAoo();
            }

            current.setCodAoo(codAoo);
            current.setCodEnte(enteOrAoo.getCodEnte());
            current.setPrefix(enteOrAoo.getPrefix());

            if ("admin".equals(username) || "guest".equals(username))
                current.setPrefix("");

            if ("guest".equals(username)){
                List<String> groupIds = new ArrayList<>();
                if (codAoo!=null)
                    groupIds.add(codAoo);
                groupIds.add(enteOrAoo.getCodEnte());
                current.setGroups(groupIds.toArray(new String[0]));
                return current;
            }

            Collection<Group> groups = ClientCache.getInstance().getUserGroups(codEnteOrAoo,username);

            List<String> groupIds = new ArrayList<>();
            for( Group group: groups)
                groupIds.add(group.getDocerId());

            if ("admin".equals(username) && !groupIds.contains("admins"))
                groupIds.add("admins");

            current.setGroups(groupIds.toArray(new String[0]));
        } else {

            if ("admin".equals(username)){
                Group ente = ClientCache.getInstance().getEnte(codEnteOrAoo);
                if (ente!=null)
                    current.setCodEnte(codEnteOrAoo);
                else
                    current.setCodEnte("system");

                current.setCodAoo("system");

                current.setPrefix("");
                return current;
            }

            return null;
            //current.setGroups(new String[0]);
        }

        return current;
    }

    public Claims parseClaims(String jwtToken){
        if (jwtToken==null)
            return null;
        if (jwtToken.startsWith("Bearer "))
            jwtToken = jwtToken.substring("Bearer ".length());
        String SECRET = System.getProperty("secretKey","SECRET");
        Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
        if (claims.getAudience()==null){
            claims.setAudience( (String) claims.get("aoo"));
        }
        return claims;
    }

    public User getThreadUser(){
        User user = threadUser.get();

        if (user==null && threadBearerResolver!=null){

            String bearer = null;
            try {
                bearer = threadBearerResolver.bearer();
            } catch (Exception e){
                e.printStackTrace();
            }

            return setThreadUser(bearer);
        }
        return user;
    }

    public User getUser(String jwtToken){
        if (jwtToken==null)
            return null;

        Claims claims = parseClaims(jwtToken);

        User user = getUserByClaims(claims);

        return user;
    }

    static ThreadLocal<Cipher> threadCipher = new ThreadLocal<>();

    private final byte[][] keys = new byte[][]{
            {29, 42, 21, 42, 100, 29, -128, 88, 12, 107, -51, -17, -99, 55, -123, 88},
            {52, 17, 119, 28, -37, -82, -9, 79, -110, -96, -51, 45, -46, 44, 2, 11},
            {-43, -126, -72, -70, -29, 84, -78, 107, -45, -110, -16, -15, 9, -119, -27, 36},
            {126, -110, 38, -76, -103, 10, 94, -1, 39, -126, -52, 81, 51, -55, -19, 65},
            {-51, 79, 81, 48, 22, 74, 120, -99, 91, -49, -107, 8, -71, 72, 41, -120}
    };

    byte[] runCipher(int mode, byte[] input, byte[] key) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = threadCipher.get();

        if (cipher==null){
            try {
                cipher = Cipher.getInstance("AES");
            } catch (Exception e) {
                throw new SecurityException(e);
            }
            threadCipher.set(cipher);
        }

        cipher.init(mode, new SecretKeySpec(key, "AES"));
        return cipher.doFinal(input);
    }

    public String encrypt(String token){
        try {
            DateTime date = new DateTime();
            date = date.plusDays(1);

            token = String.format("%s-|-%d", token, date.getMillis());

            byte[] encrypted = this.runCipher(Cipher.ENCRYPT_MODE, token.getBytes(),
                    this.keys[new Random(System.nanoTime()).nextInt(this.keys.length)]);

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new SecurityException("Errore nella creazione del token", e);
        }
    }

    public String getDMSTicket(User user){
        return getDMSTicket(user,null);
    }

    public String getDMSTicket(User user, String password){
        String un = ClientCache.suffix(user.getPrefix()) + user.getUserName();
        /*String pfx =  user.getPrefix();
        if (!Strings.isNullOrEmpty(pfx))
            un = user.getPrefix()+"__"+user.getUserName();*/

        String codEnte = user.getCodEnte();
        String eprefix = "";

        if (!"system".equals(codEnte)){
            Group ente = ClientCache.getInstance().getEnte(codEnte);
            if (ente!=null)
                eprefix = ente.getPrefix();
        }

        String ticket = String.format("ente:%s|uid:%s|dmsticket:%s|",codEnte,un,un);

        if (password!=null && "true".equals(System.getProperty("ondemand")))
            ticket += "secret:"+password+"|";

        ticket += "prefix:" + eprefix + "|";

        ticket = encrypt(ticket);

        return ticket;
    }

    public String convertToJWTToken(String docerToken){

        String regex = "(?:\\||^)%s:([^|]*?)\\|.*";

        String uid = null;
        String ente = null;
        String aoo = null;

        Matcher matcher = Pattern.compile(String.format(regex, "uid")).matcher(docerToken);
        if(matcher.find())
            uid = matcher.group(1);

        matcher = Pattern.compile(String.format(regex, "ente")).matcher(docerToken);
        if(matcher.find())
            ente = matcher.group(1);

        matcher = Pattern.compile(String.format(regex, "aoo")).matcher(docerToken);
        if(matcher.find())
            aoo = matcher.group(1);

        if (aoo==null && ente!=null){
            Collection<Group> aoos = ClientCache.getInstance().getAllAOOs();
            for ( Group g : aoos ){
                if (g.getCodEnte().equals(ente))
                    aoo = g.getDocerId();
            }
        }

        return simpleJWTToken(aoo,uid);
    }

    public String convertToDocerToken(String jwtToken){
        Claims claims = parseClaims(jwtToken);
        User user = getUser(jwtToken);
        String token = "|expiration:%s|uid:%s|ente:%s|app:%s|userGroup:%s|fullName:%s|fullname:%s|email:%s|";
        token = String.format(token,
                claims.getExpiration().getTime(),
                user.getUserName(),
                user.getCodEnte(),
                System.getProperty("sede","DOCAREA"),
                StringUtils.join(user.getGroups(),";"),
                user.getFullName(),
                user.getFullName(),
                user.getEmail());

        if (user.getCodAoo()!=null)
            token += "aoo:"+user.getCodAoo()+"|";

        //String ticket = String.format("ente:%s|uid:%s|dmsticket:%s|",user.getCodEnte(),user.getPrefix()+user.getUserName(),user.getPrefix()+user.getUserName());

        //ticket = encrypt(ticket);
        String ticket = getDMSTicket(user, (String) claims.get("secret") );
        ticket = encrypt(ticket);

        token += "ticketDocerServices:" +ticket+"|";
        token += "nt:" + System.nanoTime() + "|";

        String md5 = DigestUtils.md5DigestAsHex((token + getSecretKey()).getBytes());

        token += "md5:"+md5+"|";

        return token;
    }

    public static Map<String, Object> buildMergedClaims(Map<String,Object> config, String token, String... others){

        if (config==null)
            return null;

        Map mappings = (Map) config.get("mappings");

        if (mappings==null || token==null)
            return null;

        try {

            Map claims = new HashMap();

            DecodedJWT decodeToken = JWT.decode(token);

            String issuer = decodeToken.getIssuer();
            String realm = getRealm(issuer);

            if (realm==null)
                return null;

            Map myConfig = (Map) mappings.getOrDefault(realm , mappings.get("default"));

            if (myConfig == null)
                return null;

            Map remapConfig = (Map) myConfig.get("claims");

            if (remapConfig==null)
                return null;

            claims.put("realm",realm);
            claims.putAll(decodeToken.getClaims());

            if (others!=null){
                for( String other : others ){
                    if (other==null)
                        continue;

                    token = other;

                    decodeToken = JWT.decode(other);
                    claims.putAll(decodeToken.getClaims());
                }
            }

            Map<String, Object> newClaims = remapClaims(remapConfig, claims);

            return newClaims;

        } catch (Exception ex) {
            logger.error("Errore remap token: " + token, ex);
            throw new RuntimeException(ex);
        }
    }

    private static Map remapClaims(Map configClaims, Map tokenClaims){
        Map remappedClaims = new HashMap();

        for(Object configClaimKey: configClaims.keySet()){
            Object referVal = configClaims.get(configClaimKey);

            if(referVal instanceof String){
                String ref = (String) referVal;
                Object value = remapClaimString(tokenClaims,ref );

                if (value!=null) {
                    if (value instanceof List){
                        if (((List)value).size()>0)
                            value = ((List)value).get(0);
                        else
                            continue;
                    }
                    putOrAddIntoMap(remappedClaims, configClaimKey, value);
                }

            } else if(referVal instanceof List) {

                List<String> refs = (List<String>) referVal;
                for(String obj: refs){
                    Object value = remapClaimString(tokenClaims,obj);

                    if (value!=null) {
                        putOrAddIntoMap(remappedClaims, configClaimKey, value);
                    }
                }
            } else if(referVal instanceof Map) {

                Map<String,String> refs = (Map<String,String>) referVal;
                for(String obj: refs.values()){
                    Object value = remapClaimString(tokenClaims,obj);

                    if (value!=null) {
                        putOrAddIntoMap(remappedClaims, configClaimKey, value);
                    }
                }
            }

            if ("groups".equals(configClaimKey) || "roles".equals(configClaimKey)){
                if (!remappedClaims.containsKey(configClaimKey))
                    remappedClaims.put(configClaimKey, new ArrayList<>());
            }
        }
        return remappedClaims;
    }

    private static Object remapClaimString (Map inputClaims, String inputRef){

        Object value;

        if (inputRef.startsWith("{") && inputRef.endsWith("}")) {
            inputRef = inputRef.substring(1, inputRef.length()-1);
            Object def = null;
            int idx = inputRef.lastIndexOf(":");

            if (idx>0){
                def = inputRef.substring(idx+1);
                inputRef = inputRef.substring(0,idx);

                if (((String)def).startsWith("{") && ((String)def).endsWith("}")) {
                    def = inputClaims.get(((String)def).substring(1, ((String)def).length()-1));
                    def = parseValToInsert(def);
                }
            }

            String[] parts = inputRef.replace("\\/","\\u2215").split("/");

            inputRef = parts[0];

            if (inputRef.contains(".")) {
                value = getValueFromMap(inputClaims, inputRef.split("\\."));
            } else {
                value = inputClaims.get(inputRef);
            }

            value = parseValToInsert(value);

            if (value!=null && parts.length>1){
                value = applyRule(value, parts[1].replace("\\\\u2215","/"), parts.length>2 ? parts[2] : null );
            }

            if (value==null)
                value = def;
        } else {
            value = parseValToInsert(inputRef);
        }

        return value;
    }

    private static Object applyRule(Object value, String regex, String replacement){
        if (value == null || regex == null)
            return value;
        else if (value instanceof List){
            List old = (List) value;
            List lst = new ArrayList();
            for( int i=0; i<old.size(); i++ ){
                Object v = old.get(i);
                v = applyRule( (v == null) ? null : v.toString() , regex, replacement );
                if (v!=null)
                    lst.add(v);
            }
            return lst;
        } else if (value.toString().matches(regex)) {
            if (replacement!=null){
                boolean toUpper = false;
                boolean toLower = false;
                if (replacement.contains("\\U")) {
                    replacement = replacement.replaceAll("\\\\U", "");
                    toUpper = true;
                }
                if (replacement.contains("\\L")) {
                    replacement = replacement.replaceAll("\\\\L", "");
                    toLower = true;
                }
                if (replacement.matches(".*\\\\G\\d\\d.*")) {
                    int idx = replacement.indexOf("\\G");
                    int len = Integer.parseInt(replacement.substring(idx+2,idx+4));
                    String muuid = UUID.randomUUID().toString().replace("-","").substring(0,len);
                    replacement = replacement.replaceFirst("\\\\G\\d\\d", muuid);
                }

                String out = value.toString().replaceAll(regex,replacement);
                if (toLower)
                    out = out.toLowerCase();
                if (toUpper)
                    out = out.toUpperCase();

                return out;
            } else {
                return value;
            }
        } else {
            return null;
        }
    }


    private static Object parseValToInsert(Object obj){
        Object returnObj = obj;
        if(obj instanceof Claim){
            Claim clm = (Claim) obj;
            if(clm.asMap() != null){
                returnObj =clm.asMap();
            }else if(clm.asList(String.class) != null){
                returnObj = clm.asList(String.class);
            }else if(clm.asString() != null){
                returnObj = clm.asString();
            }
        }
        return returnObj;
    }

    private static Object getValueFromMap(Map mappa, String[] val){
        Object returnedValue = null;
        if(mappa != null) {
            returnedValue = mappa;
            for (String v : val) {
                if(returnedValue instanceof Map) {
                    returnedValue = getValueFromMap((Map) returnedValue, v);
                }else if(returnedValue instanceof Claim){
                    Claim clm = (Claim) returnedValue;
                    Map mp =clm.asMap();
                    if(mp != null){
                        returnedValue = mp;
                    }
                    returnedValue = getValueFromMap((Map) returnedValue, v);
                }
            }
        }
        return returnedValue;
    }

    private static Object getValueFromMap(Map mappa, String val){
        Object returnedObj = null;
        if(mappa != null && StringUtils.isNotEmpty(val)){
            if(!val.contains("[") && mappa.containsKey(val)){
                returnedObj = mappa.get(val);
            }else if( val.contains("[") ){
                String key = val.substring(0,val.indexOf("["));
                if(mappa.containsKey(key)){
                    Object objList = mappa.get(key);
                    if(objList instanceof List){
                        String posStr = val.substring(val.indexOf("[")+1);
                        posStr =posStr.substring(0,posStr.indexOf("]"));
                        Integer pos = Integer.parseInt(posStr);
                        returnedObj = ((List) objList).get(pos);
                    }
                }
            }
        }
        return  returnedObj;
    }

    private static Map putOrAddIntoMap(Map mappa, Object key, Object val){
        if(mappa != null){
            Object obj = val;
            if(mappa.containsKey(key)){
                obj = mappa.get(key);
                if(obj instanceof List ){
                    if(val instanceof Collection) {
                        ((List) obj).addAll((Collection) val);
                    }else {
                        ((List) obj).add(val);
                    }
                }else if (obj instanceof Map){
                    ((Map)obj).put(key, val);
                }
            }
            mappa.put(key, obj);
        }
        return mappa;
    }

    public static String getRealm(String issuer){
        if (issuer == null)
            return null;

        int idx = issuer.lastIndexOf("/auth/realms/");

        if (idx==-1)
            return null;

        return issuer.substring(idx+"/auth/realms/".length());
    }

}
