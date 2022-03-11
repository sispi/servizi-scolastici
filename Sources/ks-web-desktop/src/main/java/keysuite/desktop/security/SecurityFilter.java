package keysuite.desktop.security;


import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.JwtException;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.session.UserInfo;
import it.kdm.orchestratore.utils.ResourceUtils;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.DesktopApplication;
import keysuite.desktop.DesktopUtils;
import keysuite.desktop.components.UrlRewriter;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.security.HeaderHttpRequestWrapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.core.env.Environment;
import org.springframework.http.ContentDisposition;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

//Filtro di sicurezza
public class SecurityFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    public static final String SSO_USER = "SSO_USER";
    public static final String AUTHORIZATION = "authorization";
    public static final String SSO_USER_NAME = "SSO_USER_NAME";
    public static final String SSO_USER_AOO = "SSO_USER_AOO";
    //public static final String STATIC_ROOT = "templates/static";
    public static final String TEMPLATES_ROOT = "templates";
    //public static final String RESOURCES_ROOT = "/:resources";
    public static final String REQUEST_APP_CONFIG = "REQUEST_APP_CONFIG";

    //Map app;

    //String  publicRoot;
    public static Pattern staticsRe;
    //Pattern publicsExt;
    public static Pattern publicsPath;
    public static Pattern authorizedPath;
    //String  servletPath;
    Environment env;
    DiscoveryClientRouteLocator zuulLocator;
    UrlPathHelper urlPathHelper;
    UrlRewriter urlRewriter;

    private Pattern pahtToPattern(String path){
        if (Strings.isEmpty(path))
            return Pattern.compile("(?!.*)");
        if (path.endsWith(","))
            path = path.substring(0,path.length()-1);
        path = path.replace(",","|");
        path = path.replace("/**","(/.*)?");
        path = path.replace("**",".*");
        path = path.replace("/*","(/[^/]+)?");
        path = path.replaceAll("(\\w+)(\\*)","$1[^/]+");
        return Pattern.compile("^("+ path +")$");
    }

    public SecurityFilter(Environment env, DiscoveryClientRouteLocator discoveryClientRouteLocator, UrlRewriter urlRewriter){

        this.env = env;
        this.zuulLocator = discoveryClientRouteLocator;
        this.urlPathHelper = new UrlPathHelper();
        this.urlRewriter = urlRewriter;

        //String staticRoot = env.getProperty("static.root", STATIC_ROOT);
        /*String[] statics = env.getProperty("static.patterns", SecurityConfig.STATIC_PATTERNS).split(",");
        String[] publics = env.getProperty("public.patterns",SecurityConfig.PUBLIC_PATTERNS).split(",");
        String[] authorized = env.getProperty("authorized.patterns",SecurityConfig.AUTHORIZED_PATTERNS).split(",");

        String patternStatics = org.apache.commons.lang3.StringUtils.join(statics,"|");
        String patternPublicsPath = org.apache.commons.lang3.StringUtils.join(publics, "|");
        String patternAuthorizedPath = org.apache.commons.lang3.StringUtils.join(authorized, "|");

        staticsRe = Pattern.compile("^("+ patternStatics +")(/.*)?$");
        publicsPath = Pattern.compile("^("+ patternPublicsPath+")(/.*)?$");
        authorizedPath = Pattern.compile("^("+ patternAuthorizedPath+")(/.*)?$");*/

        initPatterns();
    }

    public void initPatterns(){
        staticsRe = pahtToPattern("/static/**,/**/static/**,"+env.getProperty("static.patterns", SecurityConfig.STATIC_PATTERNS));
        publicsPath = pahtToPattern("/error/**,/auth/**,"+env.getProperty("public.patterns",SecurityConfig.PUBLIC_PATTERNS));
        authorizedPath = pahtToPattern(env.getProperty("authorized.patterns",SecurityConfig.AUTHORIZED_PATTERNS));
    }

    public void doFilter(ServletRequest req, ServletResponse resp,FilterChain next) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        RequestAttributes requestAttributes = new ServletRequestAttributes(request,response);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        String urlToTest = request.getServletPath();

        String domain = ResourceUtils.getDomain();
        String server = Session.getServerName(request);

        String domainHost = System.getProperty("host."+domain);
        if (domainHost==null)
            System.setProperty("host."+domain,server);

        String defaultHost = System.getProperty("host.default");
        if (defaultHost==null) {
            if (System.getProperty("host.default")==null){
                System.out.println(DesktopApplication.ANSI_RED);
                System.out.println("ERROR: host.default non impostato. Valore individuato:"+server);
                System.out.print(DesktopApplication.ANSI_RESET);
            }
            System.setProperty("host.default", server);
        }

        if (isResourceUrl(request)){

            request.setAttribute("isResource","true");

            //String jwtToken = getJtwToken(request);

            if ("GET".equals(request.getMethod()) && !urlToTest.startsWith("/:")){
                try {
                    responseResource(request, response);
                } catch (FileNotFoundException fnf ){
                    Route route = this.zuulLocator.getMatchingRoute(urlToTest);
                    if (route!=null && !route.getLocation().endsWith(":0")){
                        //mando dritto a zuul
                        RequestContext.getCurrentContext().remove("forward.to");
                        next.doFilter(request, response);
                    } else {
                        if (!response.isCommitted())
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        else
                            logger.error(urlToTest+ "not found");
                    }

                } catch (Exception e) {
                    request.setAttribute(RequestDispatcher.ERROR_EXCEPTION,e);
                    if (!response.isCommitted())
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    else
                        logger.error("Internal Server Error",e);
                }
                return;
            }

            //TODO check admin
            //if (!"admin".equals(username))
            //    throw new RuntimeException("Only admin can manage resources");

            if ("GET".equals(request.getMethod())) {
                responseResource(request, response);
            } else if ("POST".equals(request.getMethod())) {
                updateResource(request, response);
            } else if ("DELETE".equals(request.getMethod())){
                deleteResource(request, response);
            } else {
                if (!response.isCommitted())
                    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                else
                    logger.error("Method not allowed "+request.getMethod());
            }
            return;

        }

        //non è una risorsa quidi la elaboro come pagina


        RequestContext.getCurrentContext().remove("forward.to");

        /*  nota su zuul :

            Se forward.to è null viene seguito l'url configurato
            Se forward.to è impostato a stringa vuota viene saltato il reindirizzamento
            Se forward.to è impostato a qualunque altro valore vince sull'url configurato
            Poichè il filtro "eatall" configurato indirizza tutto ad un fake url, se non viene gestito i redirect non funzionano
        */

        response.setHeader("Cache-Control", "no-cache");

        String offlinePath = env.getProperty("auth.offline.redirect");

        boolean isOffline = !Strings.isBlank(offlinePath);

        if(isOffline){
            if (request.getServletPath().equals(offlinePath) || request.getServletPath().equals("/error")){
                //disabilita processamento zuul
                RequestContext.getCurrentContext().put("forward.to","");
                next.doFilter(request, response);
                return;
            } else {
                SecurityHelper.cleanSession(response);
                response.sendRedirect(request.getContextPath() + offlinePath+"?requestURL="+DesktopUtils.encodeURIComponent(request.getRequestURL()+ ((org.apache.commons.lang3.StringUtils.isNotEmpty(request.getQueryString()))? "?"+request.getQueryString():"")));
                return;
            }
        }

        if(request.getServletPath().equals(offlinePath)){
            String requestURL = request.getParameter("requestURL");

            if(!StringUtils.isEmpty(requestURL)){
                response.sendRedirect(requestURL);
                return;
            }
        }

        //String rewritten = (String) request.getAttribute("rewritten-url");
        String requestURL = DesktopUtils.getRequestURL(request);

        String rewritten = urlRewriter.rewrite(requestURL);

        if (rewritten!=null){
            if (rewritten.startsWith("redirect:")) {
                response.sendRedirect(rewritten.substring("redirect:".length()));
                return;
            }
            if (rewritten.startsWith("forward:"))
                rewritten = rewritten.substring("forward:".length());
            /*if (rewritten.contains("/~")){
                rewritten = rewritten.replace("/~", "/");
                request.setAttribute("has-tilde" , true);
            }*/
            request.setAttribute("rewritten-url" , rewritten);

            requestURL = Session.getHostURL(request)+rewritten;
        }

        /*if (requestURL.contains("/~")){
            requestURL = requestURL.replace("/~", "/");
            request.setAttribute("has-tilde" , true);
        }*/

        Map app = ConfigAppBean.getApp(requestURL);

        String currentLang = ConfigAppBean.getCurrentLang();
        Locale currentLocale = ConfigAppBean.getCurrentLocale();

        request.setAttribute(ConfigAppBean.COOKIE_LANG, currentLang );
        request.setAttribute(ConfigAppBean.REQ_LOCALE, currentLocale );

        //boolean isPublic = isPublicUrl(request);

        if (app!=null){
            request.setAttribute(REQUEST_APP_CONFIG,app);
            request.setAttribute("appName", app.get("appName"));
        }

        if ( app==null || app.get("targetUri")==null ){
            final String requestURI = this.urlPathHelper.getPathWithinApplication(request);
            Route route = this.zuulLocator.getMatchingRoute(requestURI);
            if (route==null || route.getLocation().endsWith(":0")) {
                //disabilita processamento zuul
                RequestContext.getCurrentContext().put("forward.to", "");
            }
        }

/*        boolean isPublic = publicsPath.matcher(urlToTest).find() || Strings.isEmpty(urlToTest);

        if (app!=null){
            request.setAttribute(REQUEST_APP_CONFIG,app);
            request.setAttribute("appName", app.get("appName"));
            List<String> roles = (List<String>) app.get("roles");
            if (roles!=null && roles.contains("guest"))
                isPublic = true;
        }

        if ( app==null || app.get("targetUri")==null ){

            final String requestURI = this.urlPathHelper.getPathWithinApplication(request);

            Route route = this.zuulLocator.getMatchingRoute(requestURI);

            if (route==null || route.getLocation().endsWith(":0")) {
                //disabilita processamento zuul
                RequestContext.getCurrentContext().put("forward.to", "");
            } else {
                isPublic = env.getProperty("zuul.routes."+route.getId()+".public",Boolean.class,false);
            }
        }*/

        String newJwtTokenFromUrl=null;

        /*String requestURLwithQs = request.getRequestURL().toString();
        if (request.getQueryString() != null)
            requestURLwithQs += "?" + request.getQueryString();*/

        String clientIpAddress = request.getHeader(env.getProperty("x.forwarded.header", "X-FORWARDED-FOR"));
        if (clientIpAddress == null || "".equals(clientIpAddress)) {
            clientIpAddress = request.getRemoteAddr();
        }

        if (requestURL.contains(";")){

            if (requestURL.contains(";lang=")){

                String lang = requestURL.substring(requestURL.lastIndexOf(";")+";lang=".length());
                requestURL = requestURL.substring(0,requestURL.lastIndexOf(";"));

                Cookie cookie = new Cookie(ConfigAppBean.COOKIE_LANG,lang);
                response.addCookie(cookie);
                response.sendRedirect(requestURL);
                return;
            }

            newJwtTokenFromUrl = DesktopUtils.getTokenFromTaggedLink(requestURL, clientIpAddress);
            if(Strings.isNotEmpty(newJwtTokenFromUrl)){
                //SecurityHelper.AuthenticateRequest(response,newJwtTokenFromUrl);
                Cookie userCookie = SecurityHelper.createUserCookie(newJwtTokenFromUrl);
                //userCookie.setDomain("");
                userCookie.setHttpOnly(true);

                response.addCookie(userCookie);

                requestURL = requestURL.substring(0,requestURL.lastIndexOf(";"));

                responseRedirect(requestURL,response);
                return;
            }
        }

        String jwtToken = (newJwtTokenFromUrl!=null)? newJwtTokenFromUrl : getJtwToken(request);

        /*if (request.getAttribute("continueOnAuth") != null){
            SecurityHelper.AuthenticateRequest(response,jwtToken);
        }*/

        if (jwtToken==null){
            jwtToken = DesktopUtils.getGuestJWTToken(request);
        }

        Session session = null;
        if (request.getServletPath().equals("/auth/logout")){
            next.doFilter(request, response);
            //System.out.println("Clear-Site-Data");
            //response.setHeader("Clear-Site-Data", "\"cookies\"");
            return;
        }

        if (jwtToken!=null) {
            try {
                session = Session.attach(jwtToken);

                String secureTag = DesktopUtils.getSecureTag(session.getDetails().getUsername(), session.getDetails().getCodAoo(),clientIpAddress);

                request.setAttribute("secureTag",secureTag);

                HeaderHttpRequestWrapper rw;
                if(!(request instanceof HeaderHttpRequestWrapper)){
                    rw = new HeaderHttpRequestWrapper(request);
                    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(rw,response));
                }else{
                    rw = (HeaderHttpRequestWrapper)request;
                }
                rw.setBearer(jwtToken);

                if ( Boolean.TRUE.equals ((Boolean)request.getAttribute("setcookie"))){
                    SecurityHelper.AuthenticateRequest(response,jwtToken);
                }

                //gestione homepage
                if ("/".equals(request.getServletPath())){

                    String requestURL0 = request.getParameter("requestURL");

                    if (requestURL0!=null && !session.getDetails().isGuest()) {
                        requestURL0 += ";" + secureTag ;
                        response.sendRedirect(requestURL0);
                        return;
                    }

                    Map header = ConfigAppBean.getCurrentHeaderConfig();
                    String dest = (String) header.getOrDefault("home","/");
                    if (Session.getUserInfoNoExc().isGuest())
                        dest = (String) header.getOrDefault("public-home",dest);

                    if (dest!=null && !dest.startsWith("/"))
                        dest = "/" + dest;

                    if (dest!=null && !dest.equals("/")){
                        response.sendRedirect( ConfigAppBean.getBaseUrl() + dest);
                        return;
                    }
                }

                if (isPublicUrl(requestURL)) {
                    next.doFilter(rw, response);
                    return;
                }
                //non è public
                UserInfo ui = session.getDetails();

                if (!ui.isGuest()) {
                    if (!isRestUrl(request)){
                        if (!isAuthorized(requestURL)){
                            logger.warn("user "+ui.getUsername()+" ha provato ad accedere all'url priobito "+request.getServletPath());

                            if (!response.isCommitted()) {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                return;
                            } else {
                                throw new KSRuntimeException("Pagina non ammessa");
                            }
                        }
                    }
                    next.doFilter(rw, response);
                    return;
                }

            } catch (JwtException e) {
                SecurityHelper.cleanSession(response);
                logger.warn("Token invalid:" + jwtToken,e);
            } catch (IllegalArgumentException exc) {
                SecurityHelper.cleanSession(response);
                logger.warn("Token illegal:" + jwtToken,exc);
            }
        }

        //non c'è jwtToken e quindi l'utente non è autenticato

        if ( (request.getHeader("accept")!=null && request.getHeader("accept").contains("application/json")) || Strings.isNotEmpty(request.getHeader("X-fragment"))) { //ajax mode!
            if (!response.isCommitted())
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            else
                logger.error("Unauthorized "+urlToTest);
            return;
        }

        //l'indirizzo richiesto è fuori dal dominio
        if ( !requestURL.startsWith(ConfigAppBean.getBaseUrl()) ){
            //requestURL = "/?requestURL=" + DesktopUtils.encodeURIComponent(requestURL);
            String url = ConfigAppBean.getBaseUrl() + request.getServletPath();
            if (request.getQueryString()!=null)
                url += "?" + request.getQueryString();
            logger.warn("invalid client host request redirected:"+url);
            response.sendRedirect( url);
        } else {

            boolean isPublic = isPublicUrl(requestURL);

            if (!isPublic){

                if (!response.isCommitted()){
                    boolean isRest = isRestUrl(request);
                    if (isRest) {
                        response.sendError(401);
                    } else {
                        String redirectURL = env.getProperty("auth.login.url","/auth/login?requestURL=") + DesktopUtils.encodeURIComponent(requestURL);
                        response.sendRedirect( ConfigAppBean.getBaseUrl() + redirectURL);
                    }
                } else {
                    logger.error("url is not public:"+urlToTest);
                }
            }
        }
    }

    private boolean isSSO(String requestURL){
        int idx = requestURL.indexOf("?");
        if (idx>0){
            String qs = requestURL.substring(idx+1);
            if (qs.equals("sso") || qs.startsWith("sso&"))
                return true;
        }
        return false;
    }

    public boolean requiresAuthentication(HttpServletRequest request){
        //cosi si forza il redirect a sso
        String requestURL = DesktopUtils.getRequestURL(request);
        String rewritten = urlRewriter.rewrite(requestURL);
        if (rewritten!=null) {
            if (rewritten.startsWith("forward:"))
                rewritten = rewritten.substring("forward:".length());
            requestURL = Session.getHostURL(request) + rewritten;
        }

        if (isSSO(requestURL))
            return true;

        if (isResourceUrl(request) || isPublicUrl(requestURL))
            return false;
        else
            return true;
    }

    Pattern restPaths = Pattern.compile("^(/caches/.*|/swagger/.*|/messages(/.*)?|/names(/.*)?)$");

    public boolean isRestUrl(HttpServletRequest request){
        if (restPaths.matcher(request.getServletPath()).find())
            return true;

        Map app = (Map) request.getAttribute(REQUEST_APP_CONFIG);

        if ( app==null || app.get("targetUri")==null ){

            final String requestURI = this.urlPathHelper.getPathWithinApplication(request);

            Route route = this.zuulLocator.getMatchingRoute(requestURI);

            if (route!=null && !route.getLocation().endsWith(":0")) {
                return true;
            }
        }
        return false;
    }

    public boolean isAuthorized(String requestURL){

        //String urlToTest = request.getServletPath();
        //String requestURL = request.getRequestURL().toString();
        String urlToTest = getServletPath(requestURL);

        /*if (requestURL.contains("/~")) {
            urlToTest = urlToTest.replace("/~", "/");
            requestURL = requestURL.replace("/~", "/");
        }*/

        if (Strings.isEmpty(urlToTest) || "/".equals(urlToTest))
            return true;

        if (authorizedPath.matcher(urlToTest).find())
            return true;

        UserInfo ui = Session.getUserInfoNoExc();
        Map app = ConfigAppBean.getApp(requestURL);

        if (app!=null && app.get("name") != null){
            List<String> roles = (List<String>) app.get("roles");
            if (roles==null || roles.size()==0)
                return true;
            else
                return ui.hasRole(roles.toArray(new String[0]));
        }
        return false;
    }

    private String getServletPath(String requestURL){
        int idx = requestURL.indexOf("/","https://x".length());
        if (idx>0)
            requestURL = requestURL.substring(requestURL.indexOf("/","https://x".length()));
        else
            return "/";
        idx = requestURL.indexOf("?");
        if (idx>0)
            requestURL = requestURL.substring(0,idx);
        return requestURL;
    }

    public boolean isPublicUrl(String requestURL){
        if (isSSO(requestURL) && Session.getUserInfoNoExc().isGuest())
            return false;

        String urlToTest = getServletPath(requestURL);

        //String urlToTest = request.getServletPath();
        //String requestURL = request.getRequestURL().toString();

        /*if (requestURL.contains("/~")) {
            urlToTest = urlToTest.replace("/~", "/");
            requestURL = requestURL.replace("/~", "/");
        }*/

        if (Strings.isEmpty(urlToTest) || "/".equals(urlToTest))
            return true;

        if (publicsPath.matcher(urlToTest).find())
            return true;

        Map app = ConfigAppBean.getApp(requestURL);

        if (app!=null){
            List<String> roles = (List<String>) app.get("roles");
            if (roles!=null && roles.contains("guest"))
                 return true;
        }

        if ( app==null || app.get("targetUri")==null ){

            //final String requestURI = this.urlPathHelper.getPathWithinApplication(request);

            Route route = this.zuulLocator.getMatchingRoute(urlToTest);

            if (route!=null && !route.getLocation().endsWith(":0")) {
                return env.getProperty("zuul.routes."+route.getId()+".public",Boolean.class,false);
            }
        }
        return false;
    }

    private boolean isResourceUrl(HttpServletRequest request){
        String urlToTest = request.getServletPath();
        return (staticsRe.matcher(urlToTest).find() || urlToTest.startsWith("/:") );
    }

    private String resolveRsxPath(HttpServletRequest request){

        String rsxPath = request.getServletPath().replace("&inspector","");

        if (rsxPath.startsWith("/:")){
            return rsxPath;
        } else {
            return TEMPLATES_ROOT + rsxPath;
        }
    }

    private void deleteResource(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String resource = resolveRsxPath(request);
        ResourceUtils.deleteResource(resource);
        /*String rsxSub = resolveRsxSub(request);

        File file = new File( ResourceUtils.getResourceRoot(rsxSub), rsxPath );

        if (file.exists()){
            FileUtils.deleteQuietly(file);
        }*/
    }

    private void updateResource(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String resource = resolveRsxPath(request);

        InputStream in = request.getInputStream();

        if (resource.endsWith(".yaml") && "json".equals(request.getQueryString())){
            Map map = DesktopUtils.parseJson(in);
            String yaml = DesktopUtils.toYAML(map);
            in = new ByteArrayInputStream(yaml.getBytes());
        }

        if (resource.endsWith(".properties") && "json".equals(request.getQueryString())){
            Map map = DesktopUtils.parseJson(in);
            String properties = DesktopUtils.toProperties(map);
            in = new ByteArrayInputStream(properties.getBytes());
        }

        if (resource.endsWith(".yaml") || resource.endsWith(".json") || resource.endsWith(".properties")){
            byte[] bytes = IOUtils.toByteArray(in);
            InputStream check = new ByteArrayInputStream(bytes);
            in = new ByteArrayInputStream(bytes);

            if (resource.endsWith(".yaml"))
                DesktopUtils.parseYAML(check);

            if (resource.endsWith(".json"))
                DesktopUtils.parseJson(check);

            if (resource.endsWith(".properties"))
                DesktopUtils.parseProperties(check);
        }

        boolean create = ResourceUtils.updateResource(resource,in);

        /*String rsxSub = resolveRsxSub(request);

        File file = new File( ResourceUtils.getResourceRoot(rsxSub), rsxPath );
        boolean create = !file.exists();

        FileUtils.copyInputStreamToFile(request.getInputStream(),file);*/

        if (create)
            response.setStatus(201);
    }

    private void responseResource(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String cacheControl=null;
        String qs = request.getQueryString();

        if (qs!=null){
            if (qs.startsWith("no-cache"))
                cacheControl = "no-cache";
            else if (qs.startsWith("no-store"))
                cacheControl = "no-store";
            else if (qs.startsWith("max-age")) {
                String maxage = request.getParameter("max-age");
                if (Strings.isEmpty(maxage)){
                    maxage = "" + (86400*365);
                }
                cacheControl = "max-age="+maxage;
            }
        }

        String resource = resolveRsxPath(request);

        String ext = FilenameUtils.getExtension(resource.toLowerCase()).toLowerCase();

        boolean cache = true;

        String ct = null;
        if (ext.length()>0){
            ct = System.getProperty("mimetype."+ext);
        }

        InputStream fileRisorsa = null;

        if (ct==null){

            List<String> results = ResourceUtils.getResources(resource);

            if (results!=null){

                if (results.size()==0 && ext.length()>0){
                    fileRisorsa = ResourceUtils.getResourceNoExc(resource);
                }

                if (fileRisorsa==null||fileRisorsa.available()==0){
                    response.setHeader("Cache-Control", "no-store");
                    String json = DesktopUtils.toJson(results,true);

                    response.setContentType("application/json");
                    response.setContentLength(json.length());

                    StreamUtils.copy(json.getBytes(Charset.defaultCharset()), response.getOutputStream() );

                    return;
                }
            }
        }

        if (fileRisorsa==null)
            fileRisorsa = ResourceUtils.getResourceNoExc(resource);

        if (fileRisorsa==null){
            throw new FileNotFoundException("Risorsa non trovata");
            //response.sendError(HttpServletResponse.SC_NOT_FOUND, "Risorsa non trovata");
            //return;
        }

        if ("text/x-yaml".equals(ct) && "json".equals(request.getQueryString())){
            Map<String,Object> map = DesktopUtils.parseYAML(fileRisorsa);
            String json = DesktopUtils.toJson(map);
            fileRisorsa = new ByteArrayInputStream(json.getBytes());
            //fileRisorsa = new ByteArrayInputStream(DesktopUtils.toYAML(map).getBytes());
            ct = "application/json";
        }

        if (ext.equals("properties") && "json".equals(request.getQueryString())){
            Map<String,Object> map = DesktopUtils.parseProperties(fileRisorsa);
            String json = DesktopUtils.toJson(map);
            fileRisorsa = new ByteArrayInputStream(json.getBytes());
            //fileRisorsa = new ByteArrayInputStream(DesktopUtils.toYAML(map).getBytes());
            ct = "application/json";
        }

        if ("text/html".equals(ct)){
            ct = "text/plain";
            ContentDisposition contentDisposition = ContentDisposition.builder("inline")
                    //.filename("dummy.txt")
                    .build();

            response.setHeader("Content-Disposition",contentDisposition.toString());
        }

        if (ct==null)
            ct = "application/octet-stream";

        if (cacheControl==null){
            if (cache)
                cacheControl = "max-age=" + (86400*365);
            else
                cacheControl = "no-cache";
        }

        //response.setHeader("Cache-Control", "max-age=86400;");
        response.setContentLength(fileRisorsa.available());
        response.setContentType(ct);

        response.setHeader("Cache-Control", cacheControl);

        if (cacheControl.equals("no-cache")){
            String ETag = ResourceUtils.getResourceETag(resource,fileRisorsa);

            if (ETag!=null){

                String IfNoneMatch = request.getHeader("If-None-Match");

                if (IfNoneMatch!=null && IfNoneMatch.equals("\""+ETag+"\"")){
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                    // re-use original last modified timestamp
                    //response.setHeader("Last-Modified", request.getHeader("If-Modified-Since"));
                } else {
                    response.setHeader("ETag","\"" + ETag + "\"");
                }
            }
        }

        StreamUtils.copy(fileRisorsa, response.getOutputStream() );
        IOUtils.closeQuietly(fileRisorsa);
    }

    private void responseRedirect(String url,HttpServletResponse response) throws IOException {
        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">    \n" +
                "<head>\n" +
                "<meta http-equiv=\"refresh\" content=\"0;URL='"+url+"'\" />\n" +
                "</head>\n" +
                "<body></body>\n" +
                "</html>";

        response.setContentLength(html.length());
        response.setContentType("text/html");
        StreamUtils.copy(html.getBytes(), response.getOutputStream() );
    }


    //metodo per verifica SSO
    public String getJtwToken(HttpServletRequest request){

        //1. Header JWT
        String jwtToken = (String) request.getAttribute(AUTHORIZATION);

        if(Strings.isEmpty(jwtToken)) {
            jwtToken = (String) request.getHeader(AUTHORIZATION);
        }

        if (Strings.isEmpty(jwtToken)){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof Session)
                jwtToken = (String) ((Session)auth).getJwtToken();
        }

        if(Strings.isEmpty(jwtToken)){

        //2. Header con parametro SSO (username/AOO)
            String utenteKey = env.getProperty("auth.header.utente", SSO_USER_NAME);
            String username = request.getHeader(utenteKey);

            if (Strings.isNotEmpty(username)){
                String aooKey = env.getProperty("auth.header.aoo", env.getProperty("auth.header.ente", SSO_USER_AOO));
                String matchCase = env.getProperty("auth.header.case.username");

                if ("uppercase".equalsIgnoreCase(matchCase)) {
                    username = username.toUpperCase();
                }else if ("lowercase".equalsIgnoreCase(matchCase)) {
                    username = username.toLowerCase();
                }

                String aoo = request.getHeader(aooKey);
                if (StringUtils.isEmpty(aoo))
                    aoo = env.getProperty("auth.header.aoo.default", env.getProperty("auth.header.ente.default"));

                if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(aoo)) {
                    logger.info("Parametri autenticazione SSO corretti. User {} Aoo {}", username, aoo);
                    jwtToken = ClientCacheAuthUtils.getInstance().simpleJWTToken(aoo, username);
                }
            }
        }
        //3. Cookie interno
        if(Strings.isEmpty(jwtToken)){
            Cookie ssoUserCookie = WebUtils.getCookie(request, SSO_USER);
            if(ssoUserCookie != null) {
                jwtToken = ssoUserCookie.getValue();
            }
        }

        if(Strings.isNotEmpty(jwtToken) && jwtToken.startsWith("Bearer ")){
            jwtToken = jwtToken.substring(7);
        }

        return jwtToken;
    }

    public void destroy() {
    }
}