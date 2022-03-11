package keysuite.desktop;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.google.common.base.Strings;
import it.kdm.orchestratore.session.Session;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.components.UrlRewriter;
import keysuite.desktop.controllers.UtilityController;
import keysuite.desktop.exceptions.*;
import keysuite.desktop.security.ConfigAppBean;
import keysuite.docer.client.*;
import keysuite.docer.sdk.APIClient;
import keysuite.solr.QueryParams;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriTemplate;

public class DesktopUtils {

    private static PropertyResolver env = null;

    public static void setEnv(PropertyResolver env){
        DesktopUtils.env = env;
    }

    static ObjectMapper indentOM = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    static ObjectMapper OM = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    static ObjectMapper YAMLMapper;
    static ObjectMapper PropsMapper;
    //static String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+"/";

    static {

        YAMLFactory factory = new YAMLFactory();

        factory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        factory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        //factory.enable(YAMLGenerator.Feature.CANONICAL_OUTPUT);
        factory.disable(YAMLGenerator.Feature.INDENT_ARRAYS);

        YAMLMapper = new ObjectMapper(factory);
        YAMLMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        PropsMapper = new JavaPropsMapper();

    }

    static final String cTag = "\n#####################";
    static final String cTagKey = "__cTag__";

    public static Map<String,Object> parseProperties(String properties) throws IOException {
        int idx = properties.indexOf(cTag);
        Map<String,Object>  map = PropsMapper.readValue(properties, LinkedHashMap.class);

        if (idx>0){
            idx = properties.indexOf("\n",idx+1);
            map.put(cTagKey,properties.substring(0,idx+1));
        }
        return map;
    }

    public static Map<String,Object> parseProperties(InputStream in) throws IOException {
        String properties = IOUtils.toString(in);
        IOUtils.closeQuietly(in);
        return parseProperties(properties);
    }

    public static String toProperties(Map<String,Object> map) throws IOException {
        String prolog = (String) map.remove(cTagKey);
        String properties = PropsMapper.writeValueAsString(map);
        if (prolog!=null) {
            properties = prolog + properties;
        }
        return properties;
    }

    public static Map<String,Object> parseYAML(InputStream in) throws IOException {
        String yaml = IOUtils.toString(in);
        IOUtils.closeQuietly(in);
        return parseYAML(yaml);
    }

    public static Map<String,Object> parseYAML(String yaml) throws IOException {
        int idx = yaml.indexOf(cTag);
        Map<String,Object>  map = YAMLMapper.readValue(yaml, LinkedHashMap.class);

        if (idx>0){
            idx = yaml.indexOf("\n",idx+1);
            map.put(cTagKey,yaml.substring(0,idx+1));
        }
        return map;
    }

    public static String toYAML(Map<String,Object> map) throws IOException {
        String prolog = (String) map.remove(cTagKey);
        String yaml = YAMLMapper.writeValueAsString(map);
        if (prolog!=null) {
            if (yaml.startsWith("---"))
                yaml = yaml.substring(3);
            yaml = prolog + yaml;
        }
        return yaml;
    }

    public static <T> T parseJson(String json) throws IOException {
        return (T) OM.readValue(json, Object.class);
    }

    public static <T> T parseJson(InputStream in) throws IOException {
        return (T) OM.readValue(in, Object.class);
    }

    public static <T> T parseJson(String json, Class cls) throws IOException {
        return (T) OM.readValue(json, cls);
    }

    public static String toJson(Object bean) throws IOException {
        return toJson(bean,true);
    }

    public static String toJson(Object bean,boolean indent) throws IOException {
        return indent? indentOM.writeValueAsString(bean) : OM.writeValueAsString(bean);
    }

    public static Map<String,String> parseCookies(String cookies) {
        Map<String,String> map = new HashMap<>();

        if (Strings.isNullOrEmpty(cookies))
            return map;

        String[] pairs = cookies.split(";");

        for( String pair : pairs) {

            int idx = pair.indexOf("=");
            if (idx>0){
                String key = pair.substring(0,idx);
                String val = pair.substring(idx+1);
                map.put(key.trim(),val.trim());
            } else {
                map.put(pair.trim(),null);
            }
        }

        return map;
    }

    public static String toCookies(Map<String,String> cookies){
        if (cookies==null || cookies.size()==0)
            return "";

        List<String> pairs = new ArrayList<>();
        for( String key: cookies.keySet()) {
            String val = cookies.get(key);
            if (val==null)
                pairs.add(key);
            else
                pairs.add(key+"="+cookies.get(key));
        }

        String val = StringUtils.join(pairs,"; ");

        return val;
    }

    public static Object getBean(String bean){
        return WebApplicationContextUtils.getRequiredWebApplicationContext(Session.getRequest().getSession().getServletContext()).getBean(bean);
    }

    public static <T> T getBean(Class<T> aClass){
        return WebApplicationContextUtils.getRequiredWebApplicationContext(Session.getRequest().getSession().getServletContext()).getBean(aClass);
    }



    /*public static APIClient getDocerClient(){
        return (APIClient) DesktopUtils.getBean("docerClient");
    }*/

    /*public static WebClient.RequestBodySpec getWebClientSpec(HttpMethod method, String url){
        return ((WebClient) DesktopUtils.getBean("webClient")).method(method).uri(buildRequestUrl(url));
    }*/

    /*public static Map<String,Object> getUserOptions() throws KSException{

        User user = getDocerClient().utenti().get(Session.getUserInfoNoExc().getUsername());

        Map<String,Object> others = user.otherFields();

        Map<String,Object> options = new LinkedHashMap<>();

        for( String field : others.keySet() ){
            if (field.startsWith("EXTRA_OPTION"))
                options.put(field,others.get(field));
        }
        return options;
    }*/

    /*public URI buildRequestURI(URI uri){
        try {
            return new URI(buildRequestUrl(uri.toString()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }*/

    public static boolean isServerSSL(){
        String ssl = env.getProperty("server.ssl.key-store");
        return ssl != null;
    }

    public static int getServerPort(){
        return Integer.parseInt(env.getProperty("local.server.port","8080"));
    }

    public static String getServerContext(){
        //return Session.getRequest().getContextPath();
        return env.getProperty("server.servlet.context-path","");
    }

    public static String getLocalBaseUrl(){
        return ( isServerSSL() ? "https" : "http") + "://localhost:" + getServerPort() + getServerContext();
    }

    final static String AMPCOD = "---AMPCOD---";

    public static String buildRequestUrl(String url){

        if (url == null /* || url.toLowerCase().startsWith("http") */)
            return url;

        url = url.replace("%26",AMPCOD);

        //url = url.replace(" ","%20");

        url = url.replace("{{","{").replace("}}","}");

        UriTemplate uriTemplate = new UriTemplate(url);

        List<String> vars = uriTemplate.getVariableNames();

        Map<String,String> tmpl = new HashMap<>();
        HttpServletRequest req = Session.getRequest();
        for( String param : vars){
            String[] vals = req.getParameterValues(param);
            String val = null;
            if (vals!=null && vals.length>0)
                val = StringUtils.join(vals,",");
            if (val!=null)
                tmpl.put(param,val);
            else {
                int idx = url.indexOf("{"+param+":");
                if (idx!=-1){
                    int idx2 = url.indexOf("}",idx);
                    String v = url.substring(idx+param.length()+2,idx2);
                    v = v.replace("\\*","*");
                    tmpl.put(param, v);
                } else {
                    tmpl.put(param, "");
                }
            }
        }
        url = uriTemplate.expand(tmpl).toString();

        url = url.replace(AMPCOD,"%26");

        //HttpServletRequest req = Session.getRequest();
        if (req!=null)
            req.setAttribute("lastResolvedModelUrl",url);

        if (!url.toLowerCase().startsWith("http")) {

            if (!url.startsWith("/"))
                throw new RuntimeException("relative urls must start by /");

            //    url = env.getProperty("zuul.routes.bl.path", "/**").replace("**", url);

            String route = "";

            if (url.length()>1)
                route=url.split("/")[1];

            String base = env.getProperty("zuul.routes." + route + ".url");
            if (base == null) {
                base = getLocalBaseUrl();
            } else {
                Boolean strip = env.getProperty("zuul.routes." + route + ".stripPrefix", Boolean.class, false);
                String path = env.getProperty("zuul.routes." + route + ".path");

                path = path.replace("/**","");

                if (!url.startsWith(path)){
                    base = getLocalBaseUrl();
                } else if (strip){
                    //String path = env.getProperty("zuul.routes." + route + ".path");
                    //path = path.replace("/**","");
                    if (url.length()>=path.length())
                        url = url.substring(path.length());
                    else
                        base = getLocalBaseUrl();
                }
            }

            url = base + url;
        }

        return url;
    }

    /*public static WebClient getBpmClient(){
        return (WebClient) DesktopUtils.getBean("bpmClient");
    }*/

    public static WebClient getWebClient(){
        return buildWebClient();
    }

    public static String getEnteforRealm(){
        return getEnteforRealm(Session.getRequest());
    }

    public static String getEnteforRealm(HttpServletRequest request){
        return ClientCache.getInstance().getAOO(getAOOforRealm(request)).getCodEnte();
    }

    public static String getAOOforRealm(){
        return getAOOforRealm(Session.getRequest());
    }

    public static String getAOOforRealm(HttpServletRequest request){

        //estratto dall'url tramite regex oppure letto da "realm.default"
        String realm = ClientCacheAuthUtils.getRequestRealm(request.getRequestURL().toString());
        Map config = ConfigAppBean.getCurrentHeaderConfig();
        String domain = (String) config.get("domain");

        //se non è la cartella risorse di default verifico se è specificato in apps.yaml
        if (!"default".equals(domain)){
            String defaultAoo = (String) ConfigAppBean.getCurrentHeaderConfig().get("defaultAoo"); //per retrocompatibiltà con l'attuale configurazinoe
            String realm0 = (String) ConfigAppBean.getCurrentHeaderConfig().getOrDefault("realm",defaultAoo);
            if (realm0!=null)
                realm = realm0;
        }

        if (realm!=null){
            Group aoo = ClientCache.getInstance().getAOOforRealm(realm);

            if (aoo==null)
                throw new RuntimeException("realm non corrispondende a ente o aoo:"+realm);

            return aoo.getCodAoo();
        } else {
            Collection<Group> allEnti = ClientCache.getInstance().getAllEnti();

            if (allEnti.size()!=1)
                throw new RuntimeException("realm non disambiguabile: più di un ente");

            Group ente = allEnti.iterator().next();
            Group aoo = ClientCache.getInstance().getAOOforRealm(ente.getCodEnte());
            return aoo.getCodAoo();
        }
    }

    public static String getGuestJWTToken(){
        return getGuestJWTToken(Session.getRequest());
    }

    public static String getGuestJWTToken(HttpServletRequest request){
        String defaultAoo = getAOOforRealm(request);
        return ClientCacheAuthUtils.getInstance().simpleJWTToken(defaultAoo, "guest");
    }

    private static String getJWTToken(){

        String jwtToken = null;
        Session s = Session.getAuthentication();
        if (s!=null)
            jwtToken = s.getJwtToken();

        if (jwtToken==null){
            jwtToken = getGuestJWTToken(Session.getRequest());
        }

        return jwtToken;
    }

    public static APIClient buildAPIClient(){
        String jwtToken = getJWTToken();

        if (jwtToken==null)
            return null;

        APIClient APIClient = new APIClient(jwtToken);
        return APIClient;
    }

    public static WebClient buildWebClient(){

        int memorySize = 16 * 1024 * 1024;

        String jwtToken = getJWTToken();

        WebClient.Builder builder = WebClient
                .builder();

        if (jwtToken!=null)
            builder.defaultHeader("Authorization","Bearer "+jwtToken);

        WebClient client = builder
               .defaultHeader("Accept-Language", ConfigAppBean.getCurrentLocale().toLanguageTag())
               .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(memorySize))
               .build();



//                .baseUrl(baseurl)
//                .defaultHeader("KS_AUTH_GROUP",Session.getRequest().getHeader(SecurityFilter.AUTH_HEADER))


        return client;
    }

    public static WebClient buildAdminWebClient(String codAoo){

        int memorySize = 16 * 1024 * 1024;

        String jwtToken = ClientCacheAuthUtils.getInstance().simpleJWTToken(codAoo,"admin");
        WebClient client = WebClient
                .builder()
//                .baseUrl(baseurl)
//                .defaultHeader("KS_AUTH_GROUP",Session.getRequest().getHeader(SecurityFilter.AUTH_HEADER))
                .defaultHeader("Authorization","Bearer "+jwtToken)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(memorySize))
                .build();

        return client;
    }

    public static <T> T GET(String url, QueryParams params, Class<T> responseType){
        return GETorPOST(url,params,null,responseType);
    }

    public static Map<String,String> HEAD(String url, QueryParams params){
        return GETorPOST(url,params,"HEAD",Map.class);
    }

    public static Map<String,String> HEAD(String url, String querystring){
        return GETorPOST(url,new QueryParams(querystring),"HEAD",Map.class);
    }

    public static Map<String,String> HEAD(String url){
        return GETorPOST(url,null,"HEAD",Map.class);
    }

    public static Map<String,Object> GET(String url, String querystring){
        return GETorPOST(url,new QueryParams(querystring),null,Map.class);
    }

    public static Map<String,Object> GET(String url){
        return GETorPOST(url,null,null,Map.class);
    }

    public static Map<String,Object> GET(String url, QueryParams params){
        return GETorPOST(url,params,null,Map.class);
    }

    public static <T> T POST(String url, QueryParams params, Object post, Class<T> responseType){
        return GETorPOST(url,params,post,responseType);
    }

    private static <T> T GETorPOST(String url, QueryParams params, Object post, Class<T> responseType) {

        try{
            WebClient client = DesktopUtils.getWebClient();

            String qs = "";

            if (params!=null)
                qs = params.toString();

            url = buildRequestUrl(url);

            if (!Strings.isNullOrEmpty(qs)){
                url = url + "?" + qs;
            }

            /*URI uri;
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }*/

            WebClient.RequestHeadersSpec uriSpec;

            URI uri;
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            if ("HEAD".equals(post)) {
                uriSpec = client.head().uri(uri);

                HttpHeaders headers = uriSpec.retrieve().toEntity(String.class)
                        .map(aResponse -> aResponse.getHeaders())
                        .block();

                Map<String,String> resp = new LinkedHashMap<>();
                for (Map.Entry<String,List<String>> entry : headers.entrySet() ){
                    resp.put(entry.getKey(), entry.getValue().get(0));
                }
                return (T) resp;

            } else if (post!=null){
                uriSpec = client.post().uri(uri).body(BodyInserters.fromValue(post));
            } else {
                uriSpec = client.get().uri(uri);
            }

            T response = uriSpec
                    .retrieve()
                    .bodyToMono(responseType)
                    .block();

            return response;

        } catch (WebClientResponseException e){
            try{
                if ( e.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON)){
                    KSRuntimeException kse = DesktopUtils.parseJson(e.getResponseBodyAsString(), KSRuntimeException.class);
                    throw kse ;
                }

            } catch(IOException e2){

            }
            throw e;
        }
    }

    public static Map<String,List<Long>> setTimer(String mark){
        Map<String,List<Long>> timers = (Map) Session.getRequest().getAttribute("timers");
        if (timers==null){
            timers = new LinkedHashMap<>();
            Session.getRequest().setAttribute("timers",timers);
        }
        if (mark!=null){
            List<Long> moments = timers.get(mark);
            if (moments==null) {
                moments = new ArrayList<>();
                timers.put(mark,moments);
            }
            moments.add(System.currentTimeMillis());
        }
        return timers;
    }

    public static Map<String,Long> getTimersInfo(){
        Map<String,Long> timersInfo = new LinkedHashMap<>();
        Map<String,List<Long>> timers = (Map) Session.getRequest().getAttribute("timers");
        if (timers==null)
            return timersInfo;

        Long first = null;
        Long tot = 0L;
        for( String mark : timers.keySet() ){
            List<Long> moments = timers.get(mark);
            Long t0 = moments.get(0);
            if (first==null) {
                first = t0;
            }
            if (moments.size()>1) {
                //timersInfo += mark + ":";
                /*for (int i = 1; i < moments.size(); i++) {
                    //timersInfo += (moments.get(i) - moments.get(i-1)) + "ms ";
                    last = moments.get(i);
                }*/
                Long last = moments.get(moments.size()-1);
                timersInfo.put(mark,last-t0);
                tot += (last-t0);
            }
        }
        timersInfo.put("t0",first);
        timersInfo.put("t1",tot);
        //return timersInfo+"total:"+(last-first)+"ms";
        return timersInfo;
    }

    public static Map<String,Object> getDataMap(String src) throws IOException {
        return (Map) getData(src,Map.class);
    }

    public static List<Object> getDataList(String src) throws IOException {
        return (List) getData(src,List.class);
    }

    public static String getDataString(String src) throws IOException {
        return (String) getData(src,String.class);
    }

    public static Map<String,String> getHead(String src) throws IOException {
        return HEAD(src);
    }

    public static <T> T getData(String src, Class<T> cls) throws IOException {
        return getData(src,cls,null,null);
    }

    public static <T> T getData(String src, Class<T> cls, HttpMethod method, Object body) throws IOException {
        return getData(src,cls,method,body,null);
    }

    public static <T> T getData(String src, Class<T> cls, HttpMethod method, Object body , String ct) throws IOException {

        if (src.endsWith("#userInfo")){

            User user = ClientCacheAuthUtils.getInstance().getThreadUser();

            if (cls.equals(String.class))
                return (T) DesktopUtils.toJson(user,true);
            else
                return (T) user;
        }

        HttpServletRequest req = Session.getRequest();

        String baseUrl = req.getServletPath();
        int idx = src.indexOf("?");
        if (idx>0){
            baseUrl+=src.substring(idx);
        }

        if (src.matches(".*[?&]\\.\\.+$")){
            String qs = req.getQueryString();
            if (qs==null)
                qs = "";
            else {
                qs = qs.replace("%26",AMPCOD);
                qs = URLDecoder.decode(qs, "utf-8");
                qs = qs.replace(AMPCOD,"%26");
            }

            //la richiesta contiene l'intera qs
            src=src.replaceAll("([?&])\\.\\.+$","$1"+qs);
            //il baseUrl contiene i parametri di base
            //retrocompatibilità vecchi template ftl
            baseUrl=baseUrl.replaceAll("&?\\.\\.+$","");
        }

        String modelUrl = DesktopUtils.buildRequestUrl(src) ;

        WebClient client = DesktopUtils.getWebClient();

        long t0=System.currentTimeMillis();

        T response = null;
        try {

            if (src.startsWith("/messages")){
                MessageSource ms =(MessageSource) Session.getBean("messageSource");
                List<NameValuePair> params = URLEncodedUtils.parse(new URI(modelUrl), Charset.forName("UTF-8"));
                List<String> filters = new ArrayList<>();
                for( NameValuePair pair : params)
                    if ("filter".equals(pair.getName())) {
                        String[] multi = pair.getValue().split(",");
                        for( String m : multi)
                            filters.add(m);
                    }
                Map res = UtilityController.extractMessages(ms, ConfigAppBean.getCurrentLocale(),filters.toArray(new String[0]));

                response = (T) toJson(res);
            } else if (src.startsWith("/names")){
                List<NameValuePair> params = URLEncodedUtils.parse(new URI(modelUrl), Charset.forName("UTF-8"));
                List<String> keys = new ArrayList<>();
                for( NameValuePair pair : params)
                    if ("key".equals(pair.getName())){
                        String[] multi = pair.getValue().split(",");
                        for( String m : multi)
                            keys.add(m);
                    }
                Map res = UtilityController.extractNames(keys.toArray(new String[0]));
                response = (T) toJson(res);
            } else {

                WebClient.RequestHeadersSpec uriSpec; // = client.method(method).uri(new URI(modelUrl));

                if (method==null)
                    method = HttpMethod.GET;

                if (body!=null){
                    uriSpec = client.method(method).uri(new URI(modelUrl)).body(BodyInserters.fromValue(body));
                    if (Strings.isNullOrEmpty(ct))
                        ct = "application/json";
                    uriSpec.header("content-type",ct);
                } else {
                    uriSpec = client.method(method).uri(new URI(modelUrl));
                }

                response = uriSpec
                        .header("X-fragment","body")
                        .header("X-baseurl",baseUrl)
                        .retrieve()
                        .bodyToMono(cls)
                        .block();
            }


        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (WebClientResponseException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        t0=System.currentTimeMillis()-t0;

        if (src.indexOf("#")>0){
            String part = src.split("#")[1];
            if (response instanceof Map){
                response = (T) ((Map)response).get(part);
            } else if (response instanceof String && ((String)response).trim().startsWith("{")){
                Map json = DesktopUtils.parseJson( (String) response);
                Object json2 = json.get(part);
                if (json2==null)
                    response = null;
                else
                    response = (T) DesktopUtils.toJson(json2);
            }
        }

        return response;
    }

    public static class Client {

        public String get(String src) throws IOException{
            return DesktopUtils.getData(src,String.class);
        }
        public String post(String src,Object body) throws IOException{
            return DesktopUtils.getData(src,String.class,HttpMethod.POST,body);
        }

        public Map headMap(String src) throws IOException{
            return DesktopUtils.getHead(src);
        }

        public Map getMap(String src) throws IOException{
            return DesktopUtils.getDataMap(src);
        }
        public Map httpMap(String src,String method, Object body) throws IOException{
            return DesktopUtils.getData(src,Map.class,HttpMethod.valueOf(method),body);
        }

        public List getList(String src) throws IOException{
            return DesktopUtils.getDataList(src);
        }
        public List httpList(String src, String method, Object body) throws IOException{
            return DesktopUtils.getData(src,List.class,HttpMethod.valueOf(method),body);
        }

        public String getString(String src) throws IOException{
            return DesktopUtils.getDataString(src);
        }
        public String httpString(String src,String method, Object body) throws IOException{
            return DesktopUtils.getData(src,String.class,HttpMethod.valueOf(method),body);
        }
    }

    public static Map<String,Object> getParameters(HttpServletRequest request) {

        Map<String,Object> params = new HashMap<>();

        Enumeration<String> it = request.getParameterNames();

        while (it.hasMoreElements()){
            String name = it.nextElement();

            String[] values = request.getParameterValues(name);

            if (values.length==1)
                params.put(name,values[0]);
            else if (values.length>1)
                params.put(name,values);
        }

        return params;
    }

    public static InputStream zip(String... files) throws IOException {
        URL[] urls = new URL[files.length];
        for( int i=0; i<urls.length; i++)
            urls[i] = new URL(files[i]);
        return zip(urls);
    }

    public static InputStream zip(URL... files) throws IOException {

        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        String jwt = Session.getAuthentication().getJwtToken();

        APIClient client = new APIClient(jwt);
        FileServiceCommon fsc = client.getFileServiceCommon();
        Set<String> entries = new HashSet<>();

        for (URL url : files) {

            NamedInputStream nis;
            try {
                nis = fsc.openURL(url,jwt);
            } catch (KSException exc) {
                throw new IOException(exc);
            }

            String name = nis.getName();

            if (Strings.isNullOrEmpty(name))
                name = "temp.bin";

            String fname = name;
            String ext = FilenameUtils.getExtension(name);
            String bn = FilenameUtils.getBaseName(name);

            if (Strings.isNullOrEmpty(ext)) {
                ext = "bin";
                fname = name + ".bin";
            }

            int idx = 0;
            while (entries.contains(fname)){
                fname = String.format("%s (%s).%s", bn, ++idx, ext);
            }

            entries.add(fname);

            ZipEntry zipEntry = new ZipEntry(fname);
            zipOut.putNextEntry(zipEntry);

            IOUtils.copy(nis.getStream(), zipOut);

            nis.getStream().close();
        }
        zipOut.close();

        return  new ByteArrayInputStream(fos.toByteArray());
    }


    public static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

    public static String encodeURIComponent(String input) {
        if(StringUtils.isEmpty(input)) {
            return input;
        }

        int l = input.length();
        StringBuilder o = new StringBuilder(l * 3);
        try {
            for (int i = 0; i < l; i++) {
                String e = input.substring(i, i + 1);
                if (ALLOWED_CHARS.indexOf(e) == -1) {
                    byte[] b = e.getBytes("UTF-8");
                    o.append(getHex(b));
                    continue;
                }
                o.append(e);
            }
            return o.toString();
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return input;
    }

    private static String getHex(byte buf[]) {
        StringBuilder o = new StringBuilder(buf.length * 3);
        for (int i = 0; i < buf.length; i++) {
            int n = (int) buf[i] & 0xff;
            o.append("%");
            if (n < 0x10) {
                o.append("0");
            }
            o.append(Long.toString(n, 16).toUpperCase());
        }
        return o.toString();
    }

    public static String decodeURIComponent(String encodedURI) {
        char actualChar;

        StringBuffer buffer = new StringBuffer();

        int bytePattern, sumb = 0;

        for (int i = 0, more = -1; i < encodedURI.length(); i++) {
            actualChar = encodedURI.charAt(i);

            switch (actualChar) {
                case '%': {
                    actualChar = encodedURI.charAt(++i);
                    int hb = (Character.isDigit(actualChar) ? actualChar - '0'
                            : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
                    actualChar = encodedURI.charAt(++i);
                    int lb = (Character.isDigit(actualChar) ? actualChar - '0'
                            : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
                    bytePattern = (hb << 4) | lb;
                    break;
                }
                case '+': {
                    bytePattern = ' ';
                    break;
                }
                default: {
                    bytePattern = actualChar;
                }
            }

            if ((bytePattern & 0xc0) == 0x80) { // 10xxxxxx
                sumb = (sumb << 6) | (bytePattern & 0x3f);
                if (--more == 0)
                    buffer.append((char) sumb);
            } else if ((bytePattern & 0x80) == 0x00) { // 0xxxxxxx
                buffer.append((char) bytePattern);
            } else if ((bytePattern & 0xe0) == 0xc0) { // 110xxxxx
                sumb = bytePattern & 0x1f;
                more = 1;
            } else if ((bytePattern & 0xf0) == 0xe0) { // 1110xxxx
                sumb = bytePattern & 0x0f;
                more = 2;
            } else if ((bytePattern & 0xf8) == 0xf0) { // 11110xxx
                sumb = bytePattern & 0x07;
                more = 3;
            } else if ((bytePattern & 0xfc) == 0xf8) { // 111110xx
                sumb = bytePattern & 0x03;
                more = 4;
            } else { // 1111110x
                sumb = bytePattern & 0x01;
                more = 5;
            }
        }
        return buffer.toString();
    }

    public static String getSecureTag(String username, String aoo, String ipAddress) throws UnsupportedEncodingException {
        Calendar c = Calendar.getInstance();
        Integer minuteOfValidityToken = new Integer(System.getProperty("token.validity", "30"));
        c.add(Calendar.MINUTE, minuteOfValidityToken);
        String timeInMillis = ""+c.getTimeInMillis();

        if (isLocal(ipAddress))
            ipAddress = "localhost";

        java.util.Base64.Encoder urlEncoder = java.util.Base64.getUrlEncoder().withoutPadding();
        String base64UsernameAoo = urlEncoder.encodeToString((username+"##"+aoo+"##"+timeInMillis+"##"+ipAddress).getBytes("UTF-8"));
        String linkToEncoded = username + "##" + aoo + "##" + timeInMillis + "##" + ipAddress;
        String md5EncodedUrl = DigestUtils.md5DigestAsHex(linkToEncoded.toLowerCase().getBytes());
        return base64UsernameAoo + "=" + md5EncodedUrl;
    }

    private static boolean isLocal(String ipAddress){
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            if (inet.isLoopbackAddress() || inet.isLoopbackAddress() || inet.isSiteLocalAddress());
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public static String getTokenFromTaggedLink(String link, String ipAddress) throws UnsupportedEncodingException {

        if (isLocal(ipAddress))
            ipAddress = "localhost";

        String result = null;
        if(org.apache.logging.log4j.util.Strings.isNotEmpty(link) && link.contains(";")){
            String[] parts = link.split(";");
            if (parts.length!=2)
                return null;
            String val = link.split(";")[1];
            parts = val.split("=");
            if (parts.length!=2)
                return null;
            String user_aoo = parts[0];
            String md5check = parts[1];

            java.util.Base64.Decoder urlDecoder = java.util.Base64.getUrlDecoder();
            byte[] decoded = urlDecoder.decode(user_aoo);
            user_aoo = new String(decoded, "UTF-8");
            parts = user_aoo.split("##");
            if (parts.length!=4)
                return null;

            String user = user_aoo.split("##")[0];
            String aoo =  user_aoo.split("##")[1];
            String timeInMillis =  user_aoo.split("##")[2];
            String requestIpAddress =  user_aoo.split("##")[3];

            String tag = user+"##"+aoo+"##"+timeInMillis+"##"+ipAddress;
            String md5Url = DigestUtils.md5DigestAsHex(tag.toLowerCase().getBytes());

            if(md5Url.equalsIgnoreCase(md5check)){
                Calendar c = Calendar.getInstance();
                if(c.getTimeInMillis()<Long.parseLong(timeInMillis) && requestIpAddress.equalsIgnoreCase(ipAddress)){
                    result= ClientCacheAuthUtils.getInstance().simpleJWTToken(aoo, user);
                }
            }
        }
        return result;
    }

    public static String encrypt(String message) throws Exception{
        String secretKey = getSecret();
        return encrypt(message,secretKey);
    }

    public static String encrypt(String message, String secretKey) throws Exception {


        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] plainTextBytes = message.getBytes("utf-8");
        byte[] buf = cipher.doFinal(plainTextBytes);
        byte[] base64Bytes = org.apache.commons.codec.binary.Base64.encodeBase64(buf);
        String base64EncryptedString = new String(base64Bytes);
        return base64EncryptedString;
    }

    private static String getSecret() throws Exception {
        return System.getProperty("secretKey", "");
    }

    public static String decrypt(String encryptedText) throws Exception {
        String secretKey = getSecret();
        return decrypt(encryptedText,secretKey);
    }

    public static String decrypt(String encryptedText, String secretKey) throws Exception {

        byte[] message = org.apache.commons.codec.binary.Base64.decodeBase64(encryptedText.getBytes("utf-8"));
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        Cipher decipher = Cipher.getInstance("DESede");
        decipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = decipher.doFinal(message);
        return new String(plainText, "UTF-8");
    }

    public static String getRequestURL(HttpServletRequest request){
        String queryString = request.getQueryString();

        String requestURL;
        if (queryString == null) {
            requestURL = request.getRequestURL().toString();
        } else {
            requestURL = request.getRequestURL().append("?").append(queryString).toString();
        }
        return requestURL;
    }

    public static String rewrite(UrlRewriter urlRewriter, HttpServletRequest request){
        String requestURL = getRequestURL(request);

        String rewrite = urlRewriter.rewrite(requestURL);
        if (!com.google.common.base.Strings.isNullOrEmpty(rewrite)) {
            if (rewrite.startsWith("redirect:")){
                String url = rewrite.substring("redirect:".length());
                if (!url.startsWith("/")){
                    String ref = request.getHeader("Referer");
                    if (ref!=null) {
                        String[] parts =  ref.split("/");
                        if (parts.length>3 && !com.google.common.base.Strings.isNullOrEmpty(parts[3]))
                            rewrite = "redirect:/" + parts[3] + "/" + url;
                    }
                }
            }
            return rewrite;
        }
        return null;
    }




}
