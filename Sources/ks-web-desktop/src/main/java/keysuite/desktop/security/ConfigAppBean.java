package keysuite.desktop.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Strings;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.utils.ResourceUtils;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import keysuite.desktop.DesktopUtils;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

public class ConfigAppBean {

    private static final Logger logger = LoggerFactory.getLogger(ConfigAppBean.class);

    public static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    final static long ttl = 60*10*1000;
    final static PassiveExpiringMap<String,Object> cache = new PassiveExpiringMap<>(ttl);

    public static void clearCache(){
        synchronized (cache){
            cache.clear();
        }
    }

    public static String getBaseUrl(){
        return (String) getCurrentHeaderConfig().get("baseUrl");
    }

    public static Map getCurrentHeaderConfig() {

        try{

            String domain = ResourceUtils.getDomain();

            String key = "header|"+ domain; /*+"|"+ Session.getUserInfoNoExc().getCodAoo();*/
            Map<String,Object> header = (Map) cache.get(key);

            if (header==null){

                InputStream headerStream = ResourceUtils.getResourceNoExc("apps.yaml");

                header = mapper.readValue(headerStream, Map.class);

                String baseUrl = (String) header.get("baseUrl");
                header.put("domain",domain);

                if (baseUrl == null) {
                    String host = System.getProperty("host."+domain,domain);

                    if (host.contains("//"))
                        baseUrl = host;
                    else {
                        baseUrl = Session.getHostURL(Session.getRequest());
                        /*String port = ApplicationProperties.getEnv().getProperty("server.port");
                        String ssl = ApplicationProperties.getEnv().getProperty("server.ssl.key-store");
                        String protocol = ssl != null ? "https" : "http";
                        baseUrl = protocol + "://" + host + ":" + port;*/
                    }

                    header.put("baseUrl",baseUrl);
                    //logger.warn("baseUrl non impostato in apps.yaml");
                }

                Object themeObj = header.get("theme");
                Map<String,Object> theme;

                if (themeObj==null)
                    themeObj = "default";

                String folder,name;

                if (themeObj instanceof Map) {
                    theme = (Map) themeObj;
                    name = (String) theme.getOrDefault("name","default");
                    folder = "/themes/"+name;
                } else {
                    name = themeObj.toString();
                    folder = "/themes/"+name;

                    InputStream stream = ResourceUtils.getResourceNoExc("templates" + folder + "/theme.yaml");

                    if (stream!=null){
                        theme = mapper.readValue(stream, Map.class);
                    } else {
                        theme = new HashMap<>();
                    }
                }

                theme.put("name", name);
                theme.put("folder", theme.getOrDefault("folder",folder));
                theme.put("common", theme.getOrDefault("common","/themes/common"));
                theme.put("default", theme.getOrDefault("default","/themes/default"));

                header.put("themeConfig",theme);

                List<Map> apps = (List) header.get("apps");

                if (apps==null){
                    apps = new ArrayList<>();
                    header.put("apps",apps);
                }

                for(Map app: apps){
                    checkApp(app,header);
                }

                Collections.sort(apps, (m1,m2) -> {
                    Integer o1 = (Integer) m1.get("order");
                    Integer o2 = (Integer) m2.get("order");

                    if (o1==null && o2==null) return 0;
                    if (o1==null) return 1;
                    if (o2==null) return -1;
                    return o1.compareTo(o2);
                });

                cache.put(key,header);
            }

            return header;
        } catch (Exception e){
            throw new RuntimeException("header configuration not found",e);
        }

    }

    /*private static void setBaseUrl(Map<String,Object> app){
        if ("true".equals(app.get("relative"))){
            String link = (String) app.get("link");
            String baseUrl = getBaseUrl();

            if (!link.startsWith("http")) {
                link = baseUrl + link;
                app.put("link",link);
            } else if (!link.startsWith(baseUrl)){
                int idx = link.indexOf("/","https://x".length());
                link = baseUrl + link.substring(idx);
                app.put("link",link);
            }
        }
    }*/

    private static void checkApp(Map<String,Object> app, Map<String,Object> header) throws Exception{

        //String langmask = (String) header.getOrDefault("langRegex", "(?:-(\\w{2}))?");
        String appName = (String) app.getOrDefault("name",app.getOrDefault("appName",app.get("folder")));
        String link = (String) app.get("link");

        if (Strings.isNullOrEmpty(link) && Strings.isNullOrEmpty(appName))
            throw new RuntimeException("link or appName for app");
        if (Strings.isNullOrEmpty(link)) {
            link = "/" + appName;
        }

        String context;
        String defRegex;

        String baseUrl = (String) header.get("baseUrl");

        assert baseUrl != null;

        if (link.startsWith("http")) {

            String host = link.split("/")[2];

            if (!host.matches(".*\\w.*")){
                throw new RuntimeException("host in link must be alphanumeric:"+host);
            }

            context = host;
            String hostmask = "";

            if (host.contains(".")){
                //la prima parte dell'host
                context = host.split("\\.")[0];
                hostmask = "\\."+"[^/]+";
            }

            defRegex = "^https?://" + context + hostmask+"(?:[/?].*)?$";

            app.put("relative","false");

        } else {

            if (!link.startsWith("/"))
                link = "/" + link;

            context = link.substring(1);

            if (context.contains("/"))
                context = context.substring(0,context.indexOf("/"));
            else if (context.contains("?"))
                context = context.substring(0,context.indexOf("?"));

            defRegex = "^https?://[^/]+(/" + context + ")(?:[/?].*)?$";

            app.put("relative","true");

            link = baseUrl + link;
        }

        if (Strings.isNullOrEmpty(appName)){
            appName = context;
        }

        if (!link.startsWith(baseUrl)) {
            app.put("securelink", true);
        }

        /*if (!Strings.isNullOrEmpty(clientIpAddress)){

            if (!link.startsWith(baseUrl)) {
                UserInfo user = Session.getUserInfoNoExc();
                String username = user.getUsername();
                String aoo = user.getAoo().getCod();
                link = TripleDES.getSecureLink(username, aoo, link, clientIpAddress);
            }
        }*/

        String regex = (String)app.getOrDefault("regex", defRegex);

        Pattern pattern = Pattern.compile(regex);

        /*String test = link;
        if (!test.startsWith("http")){
            test = "http://0.0.0.0" + test;
        }*/

        if (!link.matches(regex))
            throw new RuntimeException("invalid regex/link configuration for app "+appName);

        logger.info("regex for app '{}':{}",appName,regex);

        /* default folder */
        String folder;

        if (app.containsKey("folder")){
            folder = (String) app.get("folder");
        } else {
            folder = appName;
        }
        if (folder==null)
            folder = "";

        if (folder.length()>0 && !folder.endsWith("/"))
            folder += "/";

        /* default master */
        String master = (String) app.getOrDefault("master" , header.get("master"));

        /* default menu */
        Object menu;

        //InputStream menuStream = ResourceUtils.getResourceAsStream("custom-apps/side-menu.json");
        InputStream menuStream = ResourceUtils.getResourceAsStream("menus.yaml");
        Map menus = mapper.readValue(menuStream, Map.class);

        if (app.containsKey("menu")){
            menu = app.get("menu");
        } else if (menus.containsKey(appName)) {
            menu = appName;
        } else {
            menu = "/"+folder;
        }

        app.put("link", link);
        app.put("appName", appName);
        app.put("name", appName);
        app.put("pattern",pattern);
        app.put("folder",folder);
        app.put("master",master);
        app.put("context",context.length()==0 ? "" : "/"+context);

        if (menu!=null){
            if (menu instanceof String){
                String menuId = (String) menu;
                if (menuId.startsWith("/")){
                    List<String> rsx = ResourceUtils.getResources("templates"+menuId);

                    List<Map> list = new ArrayList<>();
                    DesktopUtils.Client webClient = new DesktopUtils.Client();
                    for (String name : rsx){
                        if (!name.endsWith(".html") && !name.endsWith(".ftl") && !rsx.contains(rsx+".html") ){
                            Map item = new LinkedHashMap();
                            item.put("name",name);
                            item.put("url",name);
                            item.put("icon","fa-folder");
                            list.add(item);
                        }
                    }
                    for (String name : rsx){
                        if (name.endsWith(".html")){
                            //String title = (String) webClient.getHead(menuId+"/"+name).getOrDefault("Content-Title",name);
                            String title = name.substring(0,name.length()-5);
                            Map item = new LinkedHashMap();
                            item.put("name",title);
                            item.put("url",name);
                            item.put("icon","fa-link");
                            list.add(item);
                        }
                    }
                    menu = list;

                } else {
                    //Map menus = getMenus();
                    if (!menus.containsKey(menuId)){
                            /*if (Session.getUserInfoNoExc().isGuest())
                                menuId = (String) header.get("public-menu");
                            else*/
                        menuId = (String) header.get("menu");
                    }
                    menu = menus.get(menuId);
                }

            }
        }

        app.put("menu",menu);
        header = new LinkedHashMap<>(header);
        header.remove("apps");
        app.put("header",header);

    }

    /*private static Map getMenus() {
        try {
            //InputStream menuStream = ResourceUtils.getResourceAsStream("custom-apps/side-menu.json");
            InputStream menuStream = ResourceUtils.getResourceAsStream("menus.yaml");
            Map menus = mapper.readValue(menuStream, Map.class);
            return menus;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }*/

    /*private static Map<String,Object> buildBean(Map app){

        try{
            Map header = getCurrentHeaderConfig();

            Map newApp = new HashMap(app);
            newApp.put("header", header);

            Object menu = app.get("menu");

            if (menu!=null){
                if (menu instanceof String){
                    String menuId = (String) menu;
                    if (menuId.startsWith("/")){
                        menu = ResourceUtils.getResources("templates"+menuId);
                    } else {
                        Map menus = getMenus();
                        if (!menus.containsKey(menuId)){

                            menuId = (String) header.get("menu");
                        }
                        menu = menus.get(menuId);
                    }

                }
                newApp.put("menu", menu);
            }

            return newApp;

        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }*/

    /*public static Map<String,Object> getBeanForApp(Map app, HttpServletRequest request){
        //UserInfo ui = Session.getUserInfoNoExc();
        if (app==null){
            app = new HashMap();
            app.put("header",getCurrentHeaderConfig());
            app.put("folder","");
            return app;
        } else {
            return app;
        }
    }*/

    public static Locale getLocale(String locale){

        if (Strings.isNullOrEmpty(locale))
            locale = "it-IT";

        String lang,country;

        String[] parts = locale.split("-");
        lang = parts[0];
        if (parts.length==1)
            country = Locale.forLanguageTag(lang).getCountry();
        else
            country = parts[1];

        return new Locale(lang,country);
    }

    public static String getDefaultLang(){
        Map header = getCurrentHeaderConfig();
        Map languages = (Map) header.get("locales");

        if (languages!=null)
            return (String) languages.keySet().iterator().next();
        else
            return "it";
    }

    public static final String COOKIE_LANG = "KS_LANG";
    public static final String REQ_LOCALE = "KS_LOCALE";

    public static String getCurrentLang(){

        Cookie cookie = WebUtils.getCookie(Session.getRequest(), COOKIE_LANG);
        if (cookie!=null){
            String lang = cookie.getValue();
            if (!Strings.isNullOrEmpty(lang))
                return lang;
        }

        return getDefaultLang();
        /*Map header = getHeaderConfig();
        Map languages = (Map) header.get("locales");

        if (languages!=null){
            String def = (String) languages.keySet().iterator().next();
            Map app = ConfigAppBean.getBeanForCurrentRequest();
            if (app!=null && app.containsKey("lang")){
                return (String) app.getOrDefault("lang",def);
            } else {
                return def;
            }
        }
        return "it";*/
    }

    public static Locale getCurrentLocale(){

        Map header = getCurrentHeaderConfig();
        Map languages = (Map) header.get("locales");
        String locale = null;

        if (languages!=null){
            locale = (String) languages.get(getCurrentLang());
        }

        return getLocale(locale);
    }

    /*public static String getLang(Map<String,Object> app){
        Pattern pattern = (Pattern) app.get("pattern");
        HttpServletRequest request = Session.getRequest();
        if (pattern!=null && request!=null) {
            String url = request.getRequestURL().toString();
            Matcher matcher = pattern.matcher(url);
            if (matcher.find() && matcher.groupCount()>1){
                String lang = matcher.group(2);
                if (!Strings.isNullOrEmpty(lang))
                    return lang;
            }
        }
        Map header = getHeaderConfig();
        Map languages = (Map) header.get("languages");
        if (languages!=null && languages.size()>0)
            return (String) languages.keySet().iterator().next();
        else
            return "it";
    }*/

    public static Map<String,Object> getCurrentTheme(){
        return (Map) getCurrentHeaderConfig().get("themeConfig");
    }

    public static Map<String,Object> getBeanForCurrentRequest(){

        HttpServletRequest request = Session.getRequest();

        if (request==null) {
            return getApp(null);
        }

        Map bean = (Map) request.getAttribute("currentAppBean");

        if (bean!=null){
            //bean.put("lang",getLang(bean));
            return bean;
        }

        String url = request.getRequestURL().toString();

        bean = getApp(url);

        //bean = getBeanForApp(app, request);

        request.setAttribute("currentAppBean",bean);

        return bean;
    }

    /*public static Map<String,Map> getApps(){
        Map header = ConfigAppBean.getHeaderConfig();
        List<Map> apps = (List) header.get("apps");
        Map keyedApps = new LinkedHashMap();
        for( Map app : apps){
            keyedApps.put(app.get("appName"),app);
        }
        return keyedApps;
    }*/

    /*public static List<Map> getProxyApps(){

        List<Map> proxyApps = new LinkedList<>();

        Map header = getHeaderConfig();

        List<Map> apps = (List<Map>) header.get("apps");

        for( int i=0; i< apps.size(); i++) {

            Map m = apps.get(i);

            String targetUri = (String) m.get("targetUri");
            String regex = (String) m.get("regex");

            if (targetUri != null && regex != null) {
                m = parseMap(m);
                proxyApps.add(m);
            }
        }
        return proxyApps;
    }*/

    public static Map getApp(String url){

        Map header = null;
        if (url!=null) {

            header = getCurrentHeaderConfig();

            List<Map> apps = (List<Map>) header.get("apps");

            for (int i = 0; i < apps.size(); i++) {

                Map m = apps.get(i);

                Pattern pattern = (Pattern) m.get("pattern");

                if (pattern.matcher(url).find()) {
                    //m = parseMap(m);
                    //m.put("lang",getLang(m));
                    return m;
                }

            }

            String ERROR_REQUEST_URI = (String) Session.getRequest().getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
            if (ERROR_REQUEST_URI != null && ERROR_REQUEST_URI != url)
                return getApp(ERROR_REQUEST_URI);
        }

        //throw new RuntimeException("app not found for url:"+url);
        Map app = new HashMap();
        app.put("header",header);
        app.put("folder","");
        return app;
    }

    /*private static Map parseMap(Map m){
        if(m != null && !m.isEmpty()){
            for(Object key: m.keySet()){

                Object value = m.get(key);
                if(value instanceof String){
                    String valStr = (String) value;
                    valStr = parseValue(valStr);
                    m.put(key, valStr);
                }else if( value instanceof Map){
                    Map subMap = (Map) value;
                    subMap = parseMap(subMap);
                    m.put(key, subMap);
                }else if (value instanceof Collection){
                    Collection coll = (Collection)value;
                    List lista = new ArrayList();
                    for(Object x: coll){
                        if(x instanceof Map){
                            Map subMap = (Map) x;
                            x = parseMap(subMap);
                        }else if(x instanceof String){
                            String valStr = (String) x;
                            x = parseValue(valStr);
                        }
                        lista.add(x);
                    }
                    m.put(key, lista);
                }
            }
        }
        return m;
    }

    private static String parseValue(String value){

        if (value.contains("${")) {
            Map<String, Object> master = new HashMap<>();
            UserInfo userInfo = Session.getUserInfo();
            Claims claims = userInfo.getClaims();
            master.put("username", userInfo.getUsername());
            master.put("aoo", userInfo.getCodAoo());
            master.put("ente", userInfo.getCodEnte());
            if (claims!=null) {
                for (String keyClaims : claims.keySet()) {
                    master.put(keyClaims, claims.get(keyClaims));
                }
            }
            StringSubstitutor ss = new StringSubstitutor(master);

            value = ss.replace(value);
        }
        return value;
    }*/
}
