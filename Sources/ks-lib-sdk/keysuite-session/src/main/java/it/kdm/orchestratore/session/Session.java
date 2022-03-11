package it.kdm.orchestratore.session;

import com.google.common.base.Preconditions;
import io.jsonwebtoken.Claims;
//import it.kdm.doctoolkit.services.ToolkitConnector;
import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.docer.client.User;
//import org.apache.commons.collections.collection.AbstractCollectionDecorator;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Session implements Authentication {




    private static final Logger logger = LoggerFactory.getLogger(Session.class);

    public final static String keySession = "SSO_USER";

    private UserInfo userInfo;
    //private String jwtToken;
    boolean authenticated = false;
    private static ThreadLocal<String> prefix = new ThreadLocal<>();

    public static String getServerName(HttpServletRequest request){
        String xForwardedHost = request.getHeader("X-Forwarded-Host");
        if (!Strings.isEmpty(xForwardedHost)) {
            String val = xForwardedHost.split(",")[0];
            return val.split(":")[0];
        } else
            return request.getServerName();
    }

    public static Object getBean(String bean){
        return WebApplicationContextUtils.getRequiredWebApplicationContext(Session.getRequest().getSession().getServletContext()).getBean(bean);
    }

    public static <T> T getBean(Class<T> aClass){
        return WebApplicationContextUtils.getRequiredWebApplicationContext(Session.getRequest().getSession().getServletContext()).getBean(aClass);
    }

    public static int getServerPort(HttpServletRequest request){
        String xForwardedHost = request.getHeader("X-Forwarded-Host");
        if (!Strings.isEmpty(xForwardedHost)){
            String val = xForwardedHost.split(",")[0];
            if (val.contains(":"))
                return Integer.parseInt(val.split(":")[1]);
            else
                return isSSL(request) ? 443 : 80;
        } else
            return request.getServerPort();
    }

    public static String getServerScheme(HttpServletRequest request){
        String xForwardedProto = request.getHeader("X-Forwarded-Proto");
        if (!Strings.isEmpty(xForwardedProto)) {
            return xForwardedProto.equalsIgnoreCase("https") ? "https" : "http";
        } else
            return request.getScheme().toLowerCase();
    }

    public static String getRemoteHost(HttpServletRequest request){
        String xForwardedHFor = request.getHeader("X-Forwarded-For");
        if (!Strings.isEmpty(xForwardedHFor)) {
            return xForwardedHFor;
        } else
            return request.getRemoteHost();
    }

    public static String getHostURL(HttpServletRequest request) {
        //String currurl = request.getRequestURL().toString();
        String url = Session.getServerScheme(request)+"://"+Session.getServerName(request);
        int port = Session.getServerPort(request);
        boolean isSSL = Session.isSSL(request);

        if (isSSL && port != 443 || !isSSL && port != 80)
            url += ":" + port;

        //String url = request.getScheme() + "://" + Session.getServerName(request);
        //url += currurl.matches(REGEX_SERVICE_PORT) ?":" + request.getServerPort() :"";
        return url;
    }

    public static boolean isSSL(HttpServletRequest request){
        return "https".equalsIgnoreCase(getServerScheme(request));
    }

    public static String getServerName(){
        return getServerName(getRequest());
    }

    public static int getServerPort(){
        return getServerPort(getRequest());
    }

    public static boolean isSSL(){
        return isSSL(getRequest());
    }

    public static String getServerScheme(){
        return getServerScheme(getRequest());
    }

    public static String getRemoteHost(){
        return getRemoteHost(getRequest());
    }

    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (attributes!=null)
            return attributes.getRequest();
        else {
            return null;
        }
    }

    public static HttpServletResponse getResponse(){
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (attributes!=null)
            return attributes.getResponse();
        else {
            //logger.warn("ServletResponseAttributes null");
            return null;
        }
    }

    public static UserInfo getUserInfoNoExc()  {
        return getUserInfo();
    }

    public static String getSede() {
        return System.getProperty("sede","DOCAREA");
    }

    public static String getDocerToken(){
        return getUserInfo().getDocerToken();
    }

    static boolean selfAttach = false;

    public static void setSelfAttachOn(){
        selfAttach = true;
    }

    public static UserInfo getUserInfo() {

        Session s = getAuthentication();

        UserInfo ui = null;

        if (s!=null && s.isAuthenticated()){
            ui = s.getDetails();
        }

        if(ui!=null)
            return ui;
        else
            return new UserInfo();
    }

    /*public static String getJWTToken() {
        String token = Session.getUserInfo().getJwtToken();
        return token;
    }*/

    public static Session getAuthentication(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof Session)
            return (Session) auth;

        Session s = (Session) getRequest().getAttribute(keySession);

        if ( s==null && selfAttach){
            s = Session.attach();
            //s = sc.getAuthentication();
        }
        return s;
    }

    public static String getPrefix() {
        String str = prefix.get();
        if (str==null)
            return "";
        else
            return str;
    }

    public static String getBearer(){
        return ClientCacheAuthUtils.getRequestBearer(Session.getRequest());
    }

    public static Session attach(){
        String bearer = getBearer();
        if (bearer!=null) {
            return attach(bearer);
        } else {
            logger.debug("missing bearer in request ( header, cookie or attribute )");
            return null;
        }
    }

    public static Session attach(String jwtToken){

        if (jwtToken!=null && jwtToken.startsWith("Bearer "))
            jwtToken = jwtToken.substring(7);

        long t0 = System.currentTimeMillis();

        ActorsCache.setThreadCodEnte(null);

        Session session = new Session();
        //session.setJwtToken(jwtToken);
        SecurityContext sc = SecurityContextHolder.getContext();

        Claims claims = ClientCacheAuthUtils.getInstance().parseClaims(jwtToken);

        User user = ClientCacheAuthUtils.getInstance().getUserByClaims(claims);

        if (user==null){
            throw new IllegalArgumentException("username doesn't exists:"+claims.getSubject()+" in "+claims.getAudience());
        }

        UserInfo ui = new UserInfo(jwtToken);

        session.setDetails(ui);
        session.setAuthenticated(true);
        //session.setJwtToken(jwtToken);

        Authentication auth = sc.getAuthentication();

        if (auth != null && !(auth instanceof Session)){
            //altre forme di SSO
            getRequest().setAttribute(keySession,session);
        } else {
            sc.setAuthentication(session);
        }

        ActorsCache.setThreadCodEnte(ui.getCodEnte());
        prefix.set(ActorsCache.getPrefix(ui.getCodEnte()));

        logger.debug("attach time:{}",System.currentTimeMillis()-t0);

        return session;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userInfo!=null)
            return this.userInfo.getActors().stream().map(x -> new SimpleGrantedAuthority("ROLE_"+x)).collect(Collectors.toList());
        else
            return new ArrayList<>();
    }

    @Override
    public String getCredentials() {
        if (userInfo!=null)
            return this.userInfo.getJwtToken();
        else
            return null;
    }

    @Override
    public UserInfo getDetails() {
        return this.userInfo;
    }

    public void setDetails(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String getPrincipal() {
        if (userInfo!=null)
            return userInfo.getUsername();
        else
            return null;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        if (userInfo!=null)
            return userInfo.getUsername();
        else
            return null;
    }

    public String getJwtToken() {
        if (userInfo!=null)
            return userInfo.getJwtToken();
        else
            return null;
    }

    /*public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }*/




}
