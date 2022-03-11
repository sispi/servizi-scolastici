package keysuite.desktop.security;

import com.google.common.base.Splitter;
import it.kdm.orchestratore.session.Session;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import keysuite.desktop.DesktopUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by microchip on 17/10/14.
 */
public class SecurityHelper {

    private static final Logger logger = LoggerFactory.getLogger(SecurityHelper.class);


    public static class SecurityContext{
        public SecurityContext(String username,String ente,String aoo,long timestamp){
            this.username = username;
            this.ente = ente;
            this.aoo = aoo;
            int duration = Integer.parseInt(System.getProperty("httpfilter.expiredCookie","30"));
            this.expiration = timestamp+duration*60*1000;
        }

        private String username;
        private String ente;
        private String aoo;
        public long expiration=0;

        public String getUsername() {
            return username;
        }

        public String getEnte() {
            return ente;
        }

        public String getAoo() {
            return aoo;
        }

        public boolean isValid() {
            return System.currentTimeMillis()<expiration && username!=null && aoo!=null && ente!=null;
        }
    }

    //5 minuti
    public static final int MAX_AGE = Integer.parseInt(System.getProperty("MAX_AGE","36000"));

    private static String  getClientSecretKey(HttpServletRequest request) {

        if (request==null)
            return null;
        //is client behind something?
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        String secretKey = System.getProperty("secretKey","");

        return ipAddress+"|"+secretKey;
    }

    public static Cookie AuthenticateRequest(HttpServletResponse response, String jwttoken) {

        Cookie userCookie = null;
        if(StringUtils.isNotEmpty(jwttoken)){
            userCookie = SecurityHelper.createUserCookie(jwttoken);
            response.addCookie(userCookie);
            return userCookie;
        }else{
            throw new java.lang.IllegalArgumentException("Jwt must be mandatory");
        }

    }


    public static Cookie createCookie(String name, String value){
        //String domain = getDomain();
        String domain = Session.getServerName();
        Cookie cookie = new Cookie(name,value);
        cookie.setMaxAge(MAX_AGE);
        cookie.setPath("/");
        cookie.setDomain(domain);
        return cookie;
    }

    public static Cookie cleanCookie(String name){
        //String domain = getDomain();
        String domain = Session.getServerName();
        Cookie cookie = new Cookie(name,null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain(domain);
        return cookie;
    }

    public static void logout() throws Exception{
        if (Session.getRequest()!=null) {
            HttpSession session = Session.getRequest().getSession(false);
            if (session != null) {
                session.invalidate();
            }
            Session.getRequest().logout();
        }
        if (Session.getResponse()!=null){
            cleanSession(Session.getResponse());
        }
        if (SecurityContextHolder.getContext()!=null){
            SecurityContextHolder.getContext().setAuthentication(null);
            SecurityContextHolder.clearContext();
        }

    }

    public static void cleanSession(HttpServletResponse response) {

        /*String username = SecurityHelper.getCookie("KS_USER");

        if (username!=null)
            CustomAppCache.removeUserInstances(username);

        HttpServletRequest request = Session.getRequest();
        if (request.getSession()!=null) {
            request.getSession().invalidate();
        }*/

        Session s = Session.getAuthentication();
        if (s!=null)
            s.setAuthenticated(false);

        response.addCookie(cleanCookie("SSO_USER"));
        //response.addCookie(cleanCookie("KS_ENTE"));
        //response.addCookie(cleanCookie("KS_AOO"));
        //response.addCookie(cleanCookie("KS_USER"));
    }
    public static Cookie createUserCookie(String jwtToken) {
        HttpServletRequest request = Session.getRequest();
        return createCookie("SSO_USER",jwtToken);
    }
    public static Cookie createUserCookie(String userName,String ente,String aoo) {

        //String userPassword = ToolkitConnector.makeSecurePassword(userName);

        HttpServletRequest request = Session.getRequest();

        long ts = System.currentTimeMillis();

        String secretKey = getClientSecretKey(request);

        String cookieValue = String.format("%s|%s|%s|%d", userName, ente, aoo, ts);

        String cryptedCookie = null;
        try {
            cryptedCookie = DesktopUtils.encrypt(cookieValue, secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //String finalCookie = cryptedCookie+"|"+ts+"|"+codiceEnte;

        return createCookie("SSO_USER",cryptedCookie);
    }

    public static String getUserCookie(){
        return getCookie("SSO_USER");
    }

    public static SecurityContext securityChecks() throws SecurityException {
        //controllo cookie corrotto (manipolato)
        SecurityContext ctx = parseUserCookie();

        if (!ctx.isValid())
            throw new SecurityException("Cookie valido ma scaduto");
        return ctx;
    }

    public static SecurityContext parseBPMCookie() throws SecurityException {

        HttpServletRequest request = Session.getRequest();

        if (request==null)
            return new SecurityContext(null,null,null,-1);

        String roles = request.getHeader("KS_AUTH_GROUP");

        if (roles==null)
            return new SecurityContext(null,null,null,-1);

        try {

            String[] val = roles.split("\\|");

            String username = val[0];
            String ente = "*".equals(val[1]) ? null : val[1] ;
            String aoo =  "*".equals(val[2]) ? null : val[2] ;
            long ts = System.currentTimeMillis();

            return new SecurityContext(username,ente,aoo,ts);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SecurityException("Cookie is not valid");
        }
    }

    public static SecurityContext parseUserCookie() throws SecurityException {

        HttpServletRequest request = Session.getRequest();

        if (request==null)
            return new SecurityContext(null,null,null,-1);

        try {

            if (request.getHeader("KS_AUTH_GROUP")!=null)
                return parseBPMCookie();

            String cryptedValue = getUserCookie();

            //if (cryptedValue==null && request.getHeader("KS_AUTH_GROUP")!=null)
            //    return parseBPMCookie();

            String secretKey = getClientSecretKey(request);

            //List<String> pars = Splitter.on('|').splitToList(cookieValue);
            //String cryptedValue = pars.get(0);

            //ts = pars.get(1);
            //codiceEnte = pars.get(2);

            String decrypedValue = DesktopUtils.decrypt(cryptedValue, secretKey);

            List<String> decryptedPairs = Splitter.on('|').splitToList(decrypedValue);

            String username = decryptedPairs.get(0);
            String ente = decryptedPairs.get(1);
            String aoo = decryptedPairs.get(2);
            String ts = decryptedPairs.get(3);

            if (request.getUserPrincipal() != null) {
                String up = request.getUserPrincipal().getName();
                if (up != null && !up.equals(username))
                    throw new SecurityException("Session state is not valid");
            }

            return new SecurityContext(username,ente,aoo,Long.parseLong(ts));

        } catch (Exception e) {
            e.printStackTrace();
            throw new SecurityException("Cookie is not valid");
        }
    }

    public static String getCookie(String cookieName) {
        HttpServletRequest request = Session.getRequest();
        return extractCookie(cookieName,request.getCookies());
    }

    public static String extractCookie(String cookieName, Cookie[] cookies) {
        Cookie cookie = null;
        if(cookies != null){
            for (int i = cookies.length-1; i >= 0; i--) {
                cookie = cookies[i];
                if(cookieName.equals(cookie.getName())){
                    try {
                        return cookie.getValue();
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }

        return null;
    }

    /*public static String getDomain(){
        return getDomain(Session.getRequest());
    }

    public static String getDomain(HttpServletRequest request){
        return ResourceUtils.getDomain(request);
    }*/

    public static boolean checkRemoteAddr(String ipFilter){

        if (Strings.isEmpty(ipFilter) || ipFilter.equals("*")){
            return true;
        } else {
            HttpServletRequest request = Session.getRequest();
            String[] ipGroup = ipFilter.split(",");
            for (int i=0;i<ipGroup.length;i++){
                if (request.getRemoteAddr().equals(ipGroup[i])||request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")){
                    return true;
                }
            }
        }
        return false;
    }

}
