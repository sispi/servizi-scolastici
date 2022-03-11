package keysuite.docer.client;

import bpmn2.ServiceException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtParser;
import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.exceptions.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriTemplate;

import java.io.File;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.*;

public class ClientUtils {

    public final static DateFormat dateFormat;

    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


    /*static {
        Properties prop = new Properties();
        String propFileName = "application.properties";

        InputStream inputStream = APIFactory.class.getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            try {
                prop.load(inputStream);
                for ( String key : prop.stringPropertyNames()){
                    System.setProperty(key, prop.getProperty(key));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    /*public static final String UID_KEY = "uid";
    public static final String AOO_KEY = "aoo";
    public static final String ENTE_KEY = "ente";
    public static final String DISPLAY_NAME_KEY = "displayName";
    public static final String EMAIL_KEY = "email";
    public static final String GROUPS_KEY = "groups";
    public static final String LANGUAGE_KEY = "language";
    public static final String COUNTRY_KEY = "country";*/

    public static String joinFascicoloId(String classifica, String anno, String progressivo, String piano) {
        if (classifica == null || anno == null || progressivo == null)
            return null;
        if ("".equals(classifica) || "".equals(anno) || "".equals(progressivo))
            return "";
        else if (piano != null)
            return classifica + "$" + piano + "/" + anno + "/" + progressivo;
        else
            return classifica + "/" + anno + "/" + progressivo;
    }

    public static String[] splitFascicoloId(String id) {
        if (id == null)
            return null;

        if ("".equals(id))
            return new String[]{"", "", "", ""};

        int idx = id.indexOf("/");
        int idx1 = id.indexOf("/", idx + 1);
        if (idx < 0 || idx1 < 0)
            return null;

        String classifica = id.substring(0, idx);

        return new String[]{
                getOnlyClass(classifica),
                id.substring(idx + 1, idx1),
                id.substring(idx1 + 1),
                getOnlyPiano(classifica)
        };
    }

    public static String getClassWithPiano(String classifica, String piano) {
        return classifica + (piano != null ? "$" + piano : "");
    }

    public static String getOnlyClass(String classifica) {
        return classifica.split("\\$")[0];
    }

    public static String getOnlyPiano(String classifica) {
        if (classifica.contains("$"))
            return classifica.split("\\$")[1];
        else
            return null;
    }

    public static String getClassWithPiano(Titolario titolario) {
        return getClassWithPiano(titolario.getClassifica(), titolario.getPianoClass());
    }

    final static String[] NOT_FOUND = new String[]{"non trovat"};
    final static String[] BAD_REQUEST = new String[]{"non e' valido"};
    final static String[] FORBIDDEN = new String[]{"can't operation", "bloccato", "autenticazione fallita", "non autorizzato"};

    public static KSRuntimeException throwKSExceptionDefNotFound(Throwable exception) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden,KSRuntimeException {
        Throwable exc = getKSException(exception);

        if (exc instanceof KSExceptionNotFound)
            throw (KSExceptionNotFound) exc;
        else if (exc instanceof KSExceptionBadRequest)
            throw (KSExceptionBadRequest) exc;
        else if (exc instanceof KSExceptionForbidden)
            throw (KSExceptionForbidden) exc;
        else
            throw new KSExceptionNotFound(exc);
    }

    public static KSRuntimeException throwKSExceptionDefForbidden(Throwable exception) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden,KSRuntimeException {
        Throwable exc = getKSException(exception);

        if (exc instanceof KSExceptionNotFound)
            throw (KSExceptionNotFound) exc;
        else if (exc instanceof KSExceptionBadRequest)
            throw (KSExceptionBadRequest) exc;
        else if (exc instanceof KSExceptionForbidden)
            throw (KSExceptionForbidden) exc;
        else
            throw new KSExceptionForbidden(exc);
    }

    public static KSRuntimeException throwKSExceptionDefBadRequest(Throwable exception) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden,KSRuntimeException {
        Throwable exc = getKSException(exception);

        if (exc instanceof KSExceptionNotFound)
            throw (KSExceptionNotFound) exc;
        else if (exc instanceof KSExceptionBadRequest)
            throw (KSExceptionBadRequest) exc;
        else if (exc instanceof KSExceptionForbidden)
            throw (KSExceptionForbidden) exc;
        else
            throw new KSExceptionBadRequest(exc);
    }

    public static KSRuntimeException throwKSException(Throwable exception) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden,KSRuntimeException {
        Throwable exc = getKSException(exception);

        if (exc instanceof KSExceptionNotFound)
            throw (KSExceptionNotFound) exc;
        else if (exc instanceof KSExceptionBadRequest)
            throw (KSExceptionBadRequest) exc;
        else if (exc instanceof KSExceptionForbidden)
            throw (KSExceptionForbidden) exc;
        else if (exc instanceof KSRuntimeException)
            throw (KSRuntimeException) exc;
        else
            throw new KSRuntimeException(exc);
    }

    public static Throwable getKSException(Throwable exception) {

        if (exception==null)
            return new KSRuntimeException();

        if (exception instanceof RuntimeException && exception.getCause()!=null)
            exception = exception.getCause();

        String message = exception.getMessage();

        if (exception instanceof KSException || exception instanceof KSRuntimeException)
            return exception;

        if (exception instanceof ServiceException){

            ServiceException se = ((ServiceException)exception);

            if (se.getDetail() instanceof KSException)
                return (Exception) se.getDetail();

            message = ((ServiceException)exception).getReason();
        }

        if (message!=null){
            String lcm = message.toLowerCase();

            if (Arrays.stream(NOT_FOUND).parallel().anyMatch(lcm::contains))
                return new KSExceptionNotFound(exception,message);
            else if (Arrays.stream(BAD_REQUEST).parallel().anyMatch(lcm::contains))
                return new KSExceptionBadRequest(exception,message);
            else if (Arrays.stream(FORBIDDEN).parallel().anyMatch(lcm::contains))
                return new KSExceptionForbidden(exception,message);
        }

        return new KSRuntimeException(exception,message);
    }

    public static String getConfigHome(){

        String homepath = System.getProperty("KEYSUITE_CONFIG");
        if (homepath==null){
            String location = System.getenv("spring.config.location");
            if (location==null)
                location = "./config";

            if (location.startsWith("./"))
                location = location.substring(2);
            File home = new File(location); //new File(environment.getProperty("config.home","home"));
            if (home.isFile())
                home = home.getParentFile();

            homepath = home.getAbsolutePath();
            if (!home.exists())
                throw new RuntimeException(home+" non esiste");
            System.setProperty("KEYSUITE_CONFIG",homepath);
        }

        return homepath;
    }

    public static ObjectMapper OM = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);


    /*public static String getXmlVersion(Integer idx, String VERSIONS, String username, String docname, List<Version> verArray )  {

        boolean create = (idx==null);

        Map<Integer, Map> indexedVersion = new LinkedHashMap<>();

        if (!create){

            String xml = VERSIONS;

            if (org.apache.commons.lang.StringUtils.isEmpty(xml)) {
                xml = "<versions />";
            }

            Map xMap = (Map) U.fromXml(xml);

            Object versions = U.get(xMap,"versions.version");

            List<Map> nodes;
            if (versions==null) {
                nodes = new ArrayList<>();
            } else if (!(versions instanceof List)) {
                nodes = Arrays.asList( (Map) ( ((Map) versions).get("version")));
            } else
                nodes = (List) versions;

            for( Map node : nodes ){
                indexedVersion.put( Integer.parseInt( node.get("number").toString() ) , node );
            }

            int internalIdx = verArray.length;

            for (int i=1; i<=internalIdx; i++){
                if (!indexedVersion.containsKey(i) || i==internalIdx){
                    final int idx = i;

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String date = dateFormat.format(new Date());

                    Map newNode = new LinkedHashMap() {{

                        put("number" , idx );
                        put("date" , date );
                        put("userId" , username);
                        put("filename", docname);

                    }};
                    indexedVersion.put(i,newNode);

                    verArray[i-1] = new Documento.Version(docname,date,username);
                }
            }
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = dateFormat.format(new Date());

            Map newNode = new LinkedHashMap() {{

                put("number" , 1 );
                put("date" , date );
                put("userId" , username);
                put("filename", docname);

            }};
            indexedVersion.put(1,newNode);
            //verArray[0] = new Documento.Version(docname,date,username);
        }

        String xml = U.toXml(new LinkedHashMap(){{
            put("versions", new LinkedHashMap(){{
                put("version", indexedVersion.values());
            }});
        }});

        xml = ClientUtils.cleanUXml(xml);

        return xml;
    }*/

    public static Class getClassForBean(String idOrType){

        String type;
        if (idOrType.contains("@"))
            type = idOrType.split("@")[1];
        else
            type = idOrType;

        Class cls;
        try {
            cls = Class.forName("keysuite.docer.client."+ org.apache.commons.lang.StringUtils.capitalize(type));
        } catch (ClassNotFoundException e) {
            if ("aoo".equals(type) || "ente".equals(type))
                cls = Group.class;
            else
                cls = Anagrafica.class;
        }
        return cls;
    }

    public static <T> T toClientBean(Map map, Class cls){
        try {
            T bean = (T) OM.readValue(OM.writeValueAsString(map), cls);
            return bean;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static <T> T toClientBeans(List list, Class cls){
        try {
            T beans = (T) OM.readValue(OM.writeValueAsString(list), cls);
            return beans;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String cleanUXml(String xml){

        xml = xml.replaceAll("<\\?xml[^>]+>\\n?","");
        xml = xml.replaceAll("(array|null|number)\\s*=\\s*\"[^\"]+\"","");

        return xml;
    }

    public static String simpleJWTToken(String audience, String subject, String secret, Map<String,Object> claims){

        if (!claims.containsKey("aoo"))
            claims.put("aoo",audience);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getJwtTokenValidity(subject)*1000L))
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()) )
                .compact();
    }

    public static String simpleJWTToken(String audience, String subject, String secret){
        Map<String,Object> claims = new HashMap<>();
        return simpleJWTToken(audience,subject,secret,claims);
    }

    public final static int JWT_expiration_seconds = 24*60*60; //1 giorno

    private static int getJwtTokenValidity(String username) {

        int seconds = JWT_expiration_seconds;
        if ("admin".equals(username))
            seconds = JWT_expiration_seconds*365; //1 anno

        return Integer.parseInt(System.getProperty("jwtTokenValidity", ""+seconds));
//        return ToolkitConnector.loadConfigFile().getProperty("jwtTokenValidity",Integer.class, defaultTokenValidity);
    }

    public static Claims parseJWTToken(String token, String secret){
        return (Claims) Jwts.parser().setSigningKey(secret.getBytes()).parse(token).getBody();
    }

    public static Claims parseJWTTokenWithoutSecret(String token) {
        String[] splitToken = token.split("\\.");
        String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";

        DefaultJwtParser parser = new DefaultJwtParser();
        Jwt<?, ?> jwt = parser.parse(unsignedToken);
        Claims claims = (Claims) jwt.getBody();
        return claims;
    }

    public static String buildRequestUrl(Map<String,String[]> params,String url){

        if (url == null)
            return url;

        UriTemplate uriTemplate = new UriTemplate(url);

        List<String> vars = uriTemplate.getVariableNames();

        if (vars!=null && vars.size()>0){
            Map<String,String> tmpl = new HashMap<>();

            for( String param : vars){
                String[] vals = params.get(param);
                if (vals != null && vals.length>0) {
                    if (vals.length==1)
                        tmpl.put(param, vals[0]);
                    else
                        tmpl.put(param, StringUtils.join(vals,","));
                } else {
                    tmpl.put(param, "");
                }
            }
            url = uriTemplate.expand(tmpl).toString();
        }

        return url;
    }

    public static Map GET(WebClient client, Map<String,String[]> params, String url) {

        try{
            url = buildRequestUrl(params,url);

            WebClient.RequestHeadersSpec uriSpec = client.get().uri(url);

            Map response = uriSpec
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response;

        } catch (WebClientResponseException e){
            try{
                if ( e.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON)){

                    KSException kse = new ObjectMapper().readValue(e.getResponseBodyAsString(),KSException.class);
                    throw kse ;
                }

            } catch(Exception e2){

            }
            throw e;
        }
    }

    public static String encode(String token, String chars){
        if (token==null)
            return null;
        StringBuilder s = new StringBuilder(token.length());
        CharacterIterator it = new StringCharacterIterator(token);
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
            if (chars.indexOf(ch)!=-1)
                s.append("%").append(Integer.toHexString((int) ch));
            else
                s.append(ch);
        }

        token = s.toString();
        return token;
    }

    public static final String specialChars = "\\/:*?\"<>!.@%";

    public static String getSolrId(String targetId,String targetType){
        String codEnte = ClientCacheAuthUtils.getInstance().getThreadUser().getCodEnte();
        String codAoo = ClientCacheAuthUtils.getInstance().getThreadUser().getCodAoo();
        return getSolrId(codEnte,codAoo,targetId,targetType);
    }

    public static String getSolrId(String codEnte, String codAoo, String targetId,String targetType){

        String loc = "DOCAREA";

        if ("fascicolo".equals(targetType) && !targetId.contains("|")){
            targetId = targetId.replaceFirst("/","|").replaceFirst("/","|");
        }
        targetId = encode(targetId,specialChars);

        switch(targetType){
            case "ente":
                targetId = String.format("%s.%s!@ente",loc,targetId);
                break;
            case "aoo":
                targetId = String.format("%s.%s!%s!@aoo",loc,codEnte,targetId);
                break;
            case "group":
            case "user":
                targetId = String.format("%s@%s",targetId,targetType);
                break;
            case "fascicolo":
                targetId = String.format("%s.%s!%s!%s@%s",loc,codEnte,codAoo,targetId,targetType);
                break;
            default:
                targetId = String.format("%s.%s!%s!%s@%s",loc,codEnte,codAoo,targetId,targetType);
                break;
        }
        return targetId;
    }

    public static Actor getActor(String sid){
        String codAoo = ClientCacheAuthUtils.getInstance().getThreadUser().getCodAoo();
        return ClientCache.getInstance().getActor(codAoo,sid);
    }

    public static DocerBean getItem(String sid,String type){

        if (sid==null)
            return null;

        if (type==null || type.equals("group") || type.equals("user") || type.equals("aoo") || type.equals("ente")) {
            return getActor(sid);
        }else{
            return ClientCache.getInstance().getItem(ClientUtils.getSolrId(sid,type));
        }
    }

    public static String datetime(Object date){
        return datetime(date,null);
    }

    public static String datetime(Object date, String format){
        if (date==null || "".equals(date))
            return "";

        try{
            DateTime dt = new DateTime(date, DateTimeZone.UTC);
            if (format==null) {
                if (dt.getHourOfDay()==0 && dt.getMinuteOfDay()==0 && dt.getSecondOfDay()==0 && dt.getMillisOfDay()==0)
                    format = System.getProperty("dateFormat", "dd/MM/YYYY");
                else
                    format = System.getProperty("dateTimeFormat", "dd/MM/YYYY HH:mm");
            }
            if ("iso".equalsIgnoreCase(format))
                return dt.toString();
            else
                return dt.toString(format);
        } catch (Exception e) {
            return date.toString();
        }
    }
}
