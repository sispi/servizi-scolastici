package keysuite.desktop.htmlview;

import com.google.common.base.Strings;
import it.kdm.orchestratore.session.ActorsCache;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.utils.ResourceUtils;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import javax.script.*;
import keysuite.desktop.DesktopUtils;
import keysuite.desktop.exceptions.KSException;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.desktop.security.SecurityFilter;
import keysuite.freemarker.TemplateUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;

//import static keysuite.desktop.components.AppInitializator.STATIC_ROOT;

public class HtmlTemplate implements IHTMLTemplate {

    protected static final Logger logger = LoggerFactory.getLogger(HtmlTemplate.class);


    private Map<String,String> attributes = new HashMap<>();
    private String rawbody=null;
    private String head=null;
    private String title=null;
    private String lang=null;
    private String name=null;
    private String asbolutePath=null;
    private Map<String,String> titles=null;

    Document htmlDocument = null;

    public long getTimeStamp() {
        return timeStamp;
    }

    private long timeStamp;

    HtmlTemplate(){

    }

    /*private String getStaticRoot(){
        return SecurityFilter.STATIC_ROOT;
    }*/

    Reader read(String path) throws ScriptException {
        /*String resFolder = KDMUtils.resFolder;
        if (path.startsWith("${resources}"))
            path = path.substring("${resources}".length());
        else if (path.startsWith(resFolder))
            path = path.substring(resFolder.length()+1);*/

        InputStream in = ResourceUtils.getResourceNoExc(SecurityFilter.TEMPLATES_ROOT+path);
        if (in==null)
            throw new ScriptException("script not found:"+path);
        return new InputStreamReader(in);
    }

    public String getAttribute(String name,String defaultValue){
        if (attributes.containsKey("data-"+name))
            name = "data-"+name;

        String v = attributes.get(name);
        if (v==null)
            return defaultValue;
        else
            return v;
    }

    public String getAttribute(String name){
        return getAttribute(name,null);
    }

    public String getHead(){
        return head;
    }

    public String getRawBody(){
        return rawbody;
    }

    public Map<String,String> getBodyAttributes(){
        Map<String,String> map = new LinkedHashMap<>();
        for( String att : attributes.keySet() ){
            if (!att.startsWith("@")){
                map.put(att,attributes.get(att));
            }
        }
        return map;
    }

    public Map<String,String> getServerAttributes(){
        Map<String,String> map = new LinkedHashMap<>();
        for( String att : attributes.keySet() ){
            if (att.startsWith("@")){
                map.put(att,attributes.get(att));
            }
        }
        return map;
    }

    /*static String getAbsolute(String modelUrl){

        if (modelUrl.contains("://"))
            return modelUrl;

        HttpServletRequest request = Session.getRequest();

        String baseUrl = request.getRequestURL().toString();
        baseUrl = baseUrl.substring(0,baseUrl.indexOf("/",10)) ;

        if (modelUrl.startsWith("/"))
            return baseUrl + modelUrl;
        else
            return baseUrl + request.getContextPath() + "/"+ modelUrl;
    }*/

    private Object evaluateScript(ScriptEngine engine, Object script) throws ScriptException {

        Bindings engineScope = engine.getContext().getBindings(ScriptContext.ENGINE_SCOPE);

        //int gen = engineScope.keySet().size()-1;

        //CompiledScript compiled = getCompiledScript(engine,node);
        //compiled.eval(engineScope);
        Object result;
        if (script instanceof Element){
            DesktopUtils.setTimer("eval"+script.hashCode());
            result = engine.eval(((Element)script).html());
            DesktopUtils.setTimer("eval"+script.hashCode());
        } else {
            DesktopUtils.setTimer("eval"+script);
            result = engine.eval(read( (String) script));
            DesktopUtils.setTimer("eval"+script);
        }
/*
        for (int i=gen; i< engineScope.size(); i++  ){

            Object obj = ((List)engineScope.values()).get(i);

            if (obj instanceof Bindings){
                Bindings app = (Bindings) obj;
                Boolean isVue = (Boolean) app.get("_isVue");

                if (isVue!=null && isVue){
                    DesktopUtils.setTimer("vue"+i);
                    Bindings options = (Bindings) app.get("$options");

                    Boolean replace = (Boolean) options.get("replace");

                    if (replace==null)
                        replace = true;

                    Object elObj = options.get("el");

                    String rendered = null;
                    try {
                        DesktopUtils.setTimer("vueRend"+i);
                        rendered = (String) ((Invocable)engine).invokeFunction("renderVue",app);
                        DesktopUtils.setTimer("vueRend"+i);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                    Element tmplEl;

                    if (elObj instanceof String){
                        tmplEl = htmlDocument.selectFirst( (String)elObj );
                    } else {
                        tmplEl = (Element) elObj;
                    }

                    Element newElem;

                    if (replace){
                        newElem = tmplEl.after(rendered).nextElementSibling();
                        tmplEl.remove();
                    } else {
                        tmplEl.html(rendered);
                        newElem = (Element) tmplEl.childNode(0);
                    }

                    newElem.removeAttr("data-server-rendered");

                    DesktopUtils.setTimer("vue"+i);
                }
            }
        }
*/
        return result;
    }

    /*private Map<String,Object> cleanModel(Map<String,Object> model){
        model.remove("springMacroRequestContext");
        model.remove("view");
        model.remove("org.springframework.validation.BindingResult.utils");
        model.remove("org.springframework.validation.BindingResult.response");
        model.remove("utils");
        return model;
    }*/



    public String renderBody(Map<String,Object> model) throws Exception {

        Map<String,Object> inspector = new LinkedHashMap<>();

        DesktopUtils.setTimer("renderBody");
        try {

            String out;

            Map<String,String> serverSources = new LinkedHashMap<>();
            inspector.put("rawBody",rawbody);
            inspector.put("sources",serverSources);
            model.put("inspector",inspector);

            if (htmlDocument!=null){

                ScriptEngine engine = null;

                Document htmlClone = htmlDocument.clone();
                Elements serverNodes = htmlClone.select("[@server],[@server-model]");
                htmlClone.select("[@ignore]").remove();

                assert serverNodes.size()>0;

                for( Element node : serverNodes ){

                    String tagName = node.tagName();
                    String src = node.attr("src");
                    String method = node.attr("@method");
                    String body = node.attr("@body");
                    String bodyCt = node.attr("@content-type");
                    String type = node.attr("type");
                    String ignoreNotFound = node.attr("@ignore-notfound");
                    String ftlModel = node.attr("@ftl-model");
                    if (ftlModel.isEmpty())
                        ftlModel = node.attr("@server-model");
                    node.removeAttr("@server");
                    node.removeAttr("@method");
                    node.removeAttr("@body");
                    node.removeAttr("@ftl-model");
                    node.removeAttr("@server-model");
                    node.removeAttr("@ignore-notfound");
                    //boolean NotFound = false;

                    boolean isExecutable = "script".equals(tagName) && (Strings.isNullOrEmpty(type) || type.contains("javascript"));

                    //if (isExecutable && !prerender)
                    //    continue;

                    if (isExecutable && engine==null){
                        DesktopUtils.setTimer("engine");
                        //engine = getScriptEngine(htmlClone);
                        engine = getScriptEngine(model);
                        DesktopUtils.setTimer("engine");
                    }

                    if (!Strings.isNullOrEmpty(src)){

                        String mark = src;
                        /*int idx = src.lastIndexOf("/");
                        if (idx>0)
                            mark = src.substring(idx);*/

                        if (isExecutable){
                            Object result = evaluateScript(engine,src);

                            if (!Strings.isNullOrEmpty(ftlModel)) {
                                model.put(ftlModel,result);
                                engine = null;
                            }

                        } else {

                            //src = src.replace("${resources}",KDMUtils.resFolder);

                            if (src.contains("${")){
                                src = TemplateUtils.ftlHandler("<#ftl>"+src,model);
                            }

                            if (!src.startsWith("/") && !src.toLowerCase().startsWith("http"))
                                src = TemplateUtils.fixContext( (String) model.get("context"))+"/"+src;

                            String data;

                            if ( !src.contains(":") && src.contains(".") && SecurityFilter.staticsRe.matcher(src).find()){
                                src = src.replace("&inspector","");

                                if (src.contains("{")){
                                    src = DesktopUtils.buildRequestUrl(src);
                                    src = src.substring(src.indexOf("/",10));
                                }

                                String part = null;

                                if (src.contains("#")){
                                    part = src.split("#")[1];
                                    src = src.split("#")[0];
                                }

                                InputStream in = ResourceUtils.getResourceNoExc(SecurityFilter.TEMPLATES_ROOT+src);
                                data = StreamUtils.copyToString(in,Charset.defaultCharset());
                                IOUtils.closeQuietly(in);

                                if (data!=null && part!=null){
                                    if (data.trim().startsWith("{")){
                                        Map json = DesktopUtils.parseJson(data);
                                        Object json2 = json.get(part);
                                        if (json2==null)
                                            data = null;
                                        else
                                            data = DesktopUtils.toJson(json2);
                                    }
                                }

                            } else {
                                DesktopUtils.setTimer("src:"+mark);
                                try{
                                    if ( !Strings.isNullOrEmpty(method) || !Strings.isNullOrEmpty(body) ){
                                        Object oBody = null;
                                        if (!Strings.isNullOrEmpty(body)){
                                            oBody = model.get(body);
                                            if (oBody==null) {
                                                if (engine==null){
                                                    engine = getScriptEngine(model);
                                                }
                                                oBody = engine.eval("x=(" + body + ")");
                                            }
                                        }
                                        if (Strings.isNullOrEmpty(method))
                                            method = "POST";
                                        data = DesktopUtils.getData(src, String.class, HttpMethod.valueOf(method), oBody , bodyCt );
                                    } else {
                                        data = DesktopUtils.getData(src, String.class );
                                    }

                                } catch(WebClientResponseException.NotFound e){
                                    data = node.html();
                                    if (!"true".equals(ignoreNotFound) && ("false".equals(ignoreNotFound) || data.trim().length()==0) ){
                                        throw e;
                                    }
                                }

                                DesktopUtils.setTimer("src:"+mark);

                                String resolvedUrl = (String) Session.getRequest().getAttribute("lastResolvedModelUrl");

                                serverSources.put(resolvedUrl,data);
                            }

                            node.removeAttr("src");

                            if ("script".equals(tagName) || Strings.isNullOrEmpty(ftlModel)) {
                                if (data==null)
                                    node.html("");
                                else {
                                    if (data.contains("$[")){
                                        data = data.replace("$[","$[#noparse");
                                    }
                                    node.html( "[#noparse]"+ data+"[/#noparse]");
                                }
                            }

                            if (!Strings.isNullOrEmpty(ftlModel)) {
                                String ct = node.attr("type");
                                //node.removeAttr("@ftl-model");
                                if ("application/json".equals(ct)) {
                                    if ("*".equals(ftlModel)) {
                                        if (!Strings.isNullOrEmpty(data))
                                            model.putAll(DesktopUtils.parseJson(data));
                                    }else {
                                        if (!Strings.isNullOrEmpty(data))
                                            model.put(ftlModel, DesktopUtils.parseJson(data));
                                        else
                                            model.put(ftlModel, null);
                                    }
                                } else {
                                    model.put(ftlModel,data);
                                }
                                engine = null;
                            }
                        }

                    } else {

                        if (isExecutable) {

                            try {
                                Object result = evaluateScript(engine, node);

                                if (!Strings.isNullOrEmpty(ftlModel)) {
                                    model.put(ftlModel,result);
                                    engine = null;
                                }

                                if (result==null)
                                    node.html("");
                                else
                                    node.html(DesktopUtils.toJson(result,true));

                            } catch (ScriptException e) {
                                String ctype = attributes.get("@content-type");
                                KSRuntimeException ce = new KSRuntimeException(e);
                                ce.setUrl(Session.getRequest().getRequestURL().toString());
                                ce.getDetails().put("script", node.html());
                                List warnings = (List) model.getOrDefault("warnings", new ArrayList<>() );
                                warnings.add(ce);
                                model.put("warnings", warnings);
                                cache.remove(this.name);

                                if (ctype!=null && !ctype.contains("html")){
                                    Session.getResponse().setContentType(ctype);
                                    throw ce;
                                }
                            }
                        }
                    }
                }

                if (titles!=null && titles.size()>0) {
                    Map<String,String> hrefs = new LinkedHashMap<>();
                    for( String href : titles.keySet()){
                        String desc = titles.get(href);
                        if (href.contains("${"))
                            href = TemplateUtils.ftlHandler("<#ftl>"+href,model);
                        if (desc.contains("${"))
                            desc = TemplateUtils.ftlHandler("<#ftl>"+desc,model);
                        hrefs.put(href,desc);
                    }
                    model.put("titles", hrefs);
                }

                /*if (!Strings.isNullOrEmpty(parentLink)){
                    if (parentLink.contains("${")){
                        model.put("parentLink", DesktopUtils.ftlHandler("<#ftl>"+parentLink,model) );
                    } else {
                        model.put("parentLink",parentLink);
                    }
                }
                if (!Strings.isNullOrEmpty(parentTitle)) {
                    if (parentTitle.contains("${")){
                        model.put("parentTitle", DesktopUtils.ftlHandler("<#ftl>"+parentTitle,model) );
                    } else {
                        model.put("parentTitle",parentTitle);
                    }
                }*/

                out = htmlClone.body().html();
            } else {
                out = rawbody;
            }

            out = out.trim();

            if (!out.startsWith("[#ftl]")){
                if (!"false".equals(attributes.get("@ftl")))
                    out = "[#ftl]" + out;
            }
            inspector.put("outBody",out);

            return out;

        } catch (WebClientResponseException e){
            if ( e.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON)){

                KSException kse = DesktopUtils.parseJson(e.getResponseBodyAsString(),KSException.class);
                if (kse.getUrl()==null)
                    kse.setUrl(e.getRequest().getURI().toString());
                throw kse ;
            } else {
                throw e;
            }
        }
    }

    private ScriptEngine getScriptEngine(Map<String,Object> model) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        ScriptContext newContext = new SimpleScriptContext();
        newContext.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
        Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);

        engine.setContext(newContext);

        //engineScope.putAll(model);
        engineScope.putAll(TemplateUtils.buildRenderModel(model));

        engineScope.put("apiClient", DesktopUtils.buildAPIClient());
        engineScope.put("request",  Session.getRequest());
        engineScope.put("response", Session.getResponse());
        engineScope.put("logger", LoggerFactory.getLogger("ServerScript") );

        /*engineScope.put("env", ApplicationProperties.getEnv());
        engineScope.put("utils", new TemplateUtils());
        engineScope.put("names", (Function<Object,List<String>>) VueTemplate::names );
        engineScope.put("name", (Function<String,String>) VueTemplate::name );
        engineScope.put("displayNames", (Function<Object,List<String>>) VueTemplate::displayNames );
        engineScope.put("displayName", (Function<String,String>) VueTemplate::displayName );*/

        return engine;
    }

    private static String name(String v){
        return ActorsCache.getName(v);
    }
    private static String displayName(String v){
        return ActorsCache.getDisplayName(v);
    }

    private static List<String> names(Object v){
        if (v instanceof List)
            return Arrays.asList( ActorsCache.getNames( (String[]) ((List)v).toArray(new String[0])) );
        else if (v instanceof String)
            return Arrays.asList( new String[] {ActorsCache.getName( (String) v)} );
        else
            return null;
    }
    private static List<String> displayNames(Object v){
        if (v instanceof List)
            return Arrays.asList( ActorsCache.getDisplayNames( (String[]) ((List)v).toArray(new String[0])) );
        else if (v instanceof String)
            return Arrays.asList( new String[] {ActorsCache.getDisplayName( (String) v)} );
        else
            return null;
    }

/*    private ScriptEngine getScriptEngine(Document htmlClone) throws ScriptException {
        ScriptEngine engine;
        engine = new ScriptEngineManager().getEngineByName("nashorn");

        ScriptContext newContext = new SimpleScriptContext();
        newContext.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
        Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);

        engine.setContext(newContext);

        engine.put("logger", logger);

        engineScope.put("document", htmlClone);
        engineScope.put("username", Session.getUserInfoNoExc().getUsername());

        //eval(engine,"js/server/global-polyfill.js");
        //eval(engine,"js/server/timer-polyfill.js");


        engine.eval(read("js/server/vue-nashorn.js"));
        //engineScope.put("Vue",Vue);

        engine.eval(read("js/server/renderContext.js"));
        engine.eval(read("js/common.js"));

        engine.eval(read("js/server/promise-polyfill.js"));
        engine.eval(read("js/server/xml-http-request-polyfill.js"));
        engine.eval(read("js/server/axios.js"));

        //engineScope.put("axios",axios);
        //engineScope.put("Vue",Vue);
        //engineScope.put("renderVue",renderVue);




        //engine.eval(read("vue-nashorn.js"));
        //engine.eval(read("renderContext.js"));

        //engineScope.put("document", htmlClone);

        HttpServletRequest req = Session.getRequest();

        String href = req.getRequestURL().toString();

        try {
            Field fld = htmlClone.getClass().getDeclaredField("location");
            ReflectionUtils.makeAccessible(fld);
            fld.set(htmlClone,href);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        engineScope.put("location", new HashMap(){{

                String protocol = href.split("//")[0]; // "http:"
                String hostname = req.getServerName(); // "appdoc.keysuite.intranet"
                String port = req.getServerPort() == 80 ? "" : ""+req.getServerPort(); // "8081"
                String pathname = req.getServletPath(); // "/AppDoc/view/reportvue"
                String search = "?" + req.getQueryString();
                String host = hostname + ( port.length()>0 ? ":" + port : "" ); // "appdoc.keysuite.intranet:8081"
                String origin = protocol + "//" + host; // "http://appdoc.keysuite.intranet:8081"
                String hash = ""; // non estraibile

                put("href", href);
                put("protocol", protocol);
                put("hostname", hostname);
                put("port", port);
                put("pathname", pathname);
                put("search", search);
                put("host", host);
                put("origin", origin);
                put("hash", hash);

            };

        });
        return engine;
    }*/

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return lang;
    }

    public String getAsbolutePath() {
        return asbolutePath;
    }

    private static Map<String, HtmlTemplate> cache = new HashMap<>();

    public static HtmlTemplate getTemplate(String path, Locale locale) throws IOException {

        boolean useCache = "true".equalsIgnoreCase(System.getProperty("template.cache","true"));

        String country = locale.getCountry();
        String language = locale.getLanguage();

        int idx = path.lastIndexOf(".");
        String suffix = path.substring(idx);
        path = path.substring(0,idx);


        String[] paths = new String[] { path+"_"+language+"_"+country, path+"_"+language, path };

        InputStream in = null; // = ResourceUtils.getResourceNoExc(path);

        for( String p : paths){
            in = ResourceUtils.getResourceNoExc(p+suffix);
            if (in!=null) {
                path = p+suffix;
                break;
            }
        }

        if (in==null)
            return null;

        HtmlTemplate cached = cache.get(path);
        File file = ResourceUtils.getResourceFile(in);

        long ts = 0;
        if (file!=null)
            ts = file.lastModified();

        if (useCache && cached!=null && cached.timeStamp==ts) {

            if (cached.timeStamp==ts) {
                logger.info("template "+cached.name+" from cache:"+cached.timeStamp);
                return cached;
            } else {
                logger.info("template "+cached.name+" expired:"+cached.timeStamp);
            }
        }

        DesktopUtils.setTimer("getTemplate");

        String html = IOUtils.toString(in,Charset.defaultCharset());
        IOUtils.closeQuietly(in);

        HtmlTemplate vm = new HtmlTemplate();
        vm.name = path;
        vm.timeStamp = ts;
        vm.asbolutePath = ResourceUtils.getAbsolutePath(path);

        if (locale!=null)
            vm.lang = locale.getLanguage();
        else
            vm.lang = Session.getRequest().getLocale().getLanguage();

        if (html.indexOf("<body")==-1){
            html = "<i/>" + html;
        }

        Document doc = Jsoup.parse(html);
        Element title = doc.selectFirst("title");
        if (title==null && doc.head()!=null)
            title = doc.head().selectFirst("title");
        if (title==null && doc.body()!=null)
            title = doc.body().selectFirst("title");
        if (title==null){
            vm.title = path.substring(path.lastIndexOf("/")+1).split("\\.")[0];
        } else {
            vm.title = title.html();
        }

        Elements els = doc.select("title[href]");

        if (els!=null && els.size()>0){
            vm.titles = new LinkedHashMap<>();
            for( Element el : els ){
                vm.titles.put(el.attr("href"),el.text());
                el.remove();
            }
        }

        /*Element parentHref = doc.selectFirst("a[role='parent']");
        if (parentHref!=null){
            vm.parentLink = parentHref.attr("href");
            vm.parentTitle = parentHref.text();
        }*/


        /*if (doc.head()!=null){
            Elements titles = doc.head().selectFirst("title");
            Elements titles2 = doc.body().select("title");
            titles.remove();

            if (titles.size()>0)
                vm.title = titles.get(0).html();
            else if (titles2.size()>0)
                vm.title = titles2.get(0).html();
        }*/

        doc.select("[@ignore and not(@server) and not(@server-model)]").remove();

        vm.rawbody = doc.body().html();

        List<Attribute> atts = new ArrayList<>( doc.body().parent().attributes().asList() );
        atts.addAll(doc.body().attributes().asList());

        for( Attribute att : atts ){
            vm.attributes.put(att.getKey(),att.getValue());
        }

        if (doc.head()!=null){
            doc.head().select("[@ignore]").remove();
            if (doc.head()!=null)
                vm.head = doc.head().html();
        }

        /*Elements srcNodes = doc.select("[@src]");
        for( Element srcNode : srcNodes ){
            String src = srcNode.attr("@src");
            srcNode.removeAttr("@src");
            srcNode.attr("@server",null);
            srcNode.attr("src",src);
        }*/

        Elements serverNodes = doc.select("[@server],[@server-model]");

        if (serverNodes.size()>0) {
            doc.outputSettings().prettyPrint(false);
            vm.htmlDocument = doc;
        }

        if (useCache)
            cache.put(path,vm);

        DesktopUtils.setTimer("getTemplate");

        return vm;
    }

}
