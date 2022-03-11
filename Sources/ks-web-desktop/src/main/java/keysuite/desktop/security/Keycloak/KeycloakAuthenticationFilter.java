/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keysuite.desktop.security.Keycloak;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import it.kdm.orchestratore.session.Session;
import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.DesktopUtils;
import keysuite.desktop.security.SecurityFilter;
import keysuite.desktop.security.SecurityHelper;
import keysuite.docer.client.Actor;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.client.User;
import org.apache.logging.log4j.util.Strings;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.ServerRequest;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.adapters.springsecurity.KeycloakAuthenticationException;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationSuccessHandler;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.UrlPathHelper;

/**
 *
 * @author andrea.fenaroli
 */
public class KeycloakAuthenticationFilter extends KeycloakAuthenticationProcessingFilter {

    //private static final Logger log = LoggerFactory.getLogger(KeycloakAuthenticationFilter.class);

    protected final static Logger logger = LoggerFactory.getLogger(KeycloakAuthenticationFilter.class);
    public static final String SSO_KC_ACCESS = "SSO_KC_ACCESS";
    public static final String SSO_KC_ID = "SSO_KC_ID";
    public static final String SSO_KC_REFRESH = "SSO_KC_REFRESH";

    //final static String cfRegex = "^[A-Z]{6}[0-9LMNPQRSTUV]{2}[A-EHLMPR-T]{1}[0-9LMNPQRSTUV]{2}[A-Z]{1}[0-9LMNPQRSTUV]{3}[A-Z]{1}$";

    private SecurityFilter securityFilter = null;
    private KeycloakSpringBootProperties config = null;
    private DiscoveryClientRouteLocator zuulLocator = null;
    private UrlPathHelper urlPathHelper = new UrlPathHelper();


    //private UserPermissionService userPermissionService = new UserPermissionService();

    public KeycloakAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        //SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
        //handler.setTargetUrlParameter("request_uri");
        setAuthenticationSuccessHandler(new KeycloakAuthenticationSuccessHandler(null));
    }

    private boolean enabled;

    public void setSecurityFilter(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    public void setRouteLocator(DiscoveryClientRouteLocator discoveryClientRouteLocator) {
        this.zuulLocator = discoveryClientRouteLocator;
    }

    PathBasedKeycloakConfigResolver resolver = new PathBasedKeycloakConfigResolver();

    public void setConfig(KeycloakSpringBootProperties config) {
        this.config = config;
        this.resolver.setAdapterConfig(config);
    }

    /*private KeycloakAuthenticationToken fakeAccount(){
        String name = "pippo";
        String roles = "aaa,bbb";

        Principal p = new UserPrincipal() {
            @Override
            public String getName() {
                return name;
            }
        };

        Set<String> accroles = new HashSet<>(Arrays.asList(roles.split(",")));

        String jwtToken = ClientCacheAuthUtils.getInstance().simpleJWTToken("AOO_TEST", name);

        RefreshableKeycloakSecurityContext ctx = new RefreshableKeycloakSecurityContext(null,null,jwtToken,null,null,null,null);

        final KeycloakAccount account = new SimpleKeycloakAccount(p, accroles, ctx);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

        for (String role : accroles) {
            grantedAuthorities.add(new KeycloakRole(role));
        }

        KeycloakAuthenticationToken kacc = new KeycloakAuthenticationToken(account,false,grantedAuthorities);
        kacc.setAuthenticated(true);

        return kacc;

    }*/

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {

        if (!isEnabled() || this.config == null)
            return false;

        //il pattern configurato attiva o disattiva l'integrazione con keycloak

        String pattern = (String) this.config.getConfig().get("pattern");

        if (pattern != null && !request.getRequestURL().toString().matches(pattern))
            return false;

        //if (jwtToken!=null && request.getServletPath().equals("/auth/logout"))
        //    jwtToken = null;

//        ServletContext servletContext = request.getServletContext();
//        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//        userPermissionService = webApplicationContext.getBean(UserPermissionService.class);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
//        userPermissionService.setAuthenticated(authentication instanceof  KeycloakAuthenticationToken);

        /*if (authentication==null && "true".equals(getEnvironment().getProperty("keycloakFake"))) {
            authentication = fakeAccount();
        }*/

        //L'autenticazione KeyCloak è richiesta solo se la pagina è privata
        //e non c'è già una sessione valida

        boolean required = securityFilter.requiresAuthentication(request);

        if (authentication instanceof Session) {

            // Nel caso di sessione interna KS "non guest" occorre sincronizzarla con quella di KC

            Session KSauth = (Session) authentication;

            if (KSauth.isAuthenticated() && !KSauth.getDetails().isGuest()) {

                String redir = refreshTokens(request, response, required);
                if (redir != null && !response.isCommitted()) {
                    try {
                        if (required)
                            response.sendRedirect(redir);
                    } catch (IOException e) {
                        logger.error("can't redirect", e);
                    }
                }
                return false;

            } else
                return required;

        } else if (authentication instanceof KeycloakAuthenticationToken) {
            if (authentication.isAuthenticated()){
                String newToken = getNewToken( (KeycloakAuthenticationToken) authentication );
                request.setAttribute(SecurityFilter.AUTHORIZATION,newToken);
                return false;
            }
            else
                return required;

        } else {

            //SSO e/o primo giro
            if (!required)
                return false;

            String jwtToken = securityFilter.getJtwToken(request);

            if (jwtToken==null)
                return true;

            try {

                //In questo caso il token potrebbe essere interno di KS (SSO) oppure esterno
                //Si discrimina tramite la presenza di un issuer (esterno)

                Claims claims = ClientUtils.parseJWTTokenWithoutSecret(jwtToken);

                if (claims.getIssuer()==null)
                    return false;
                else
                    return true;

            } catch (Exception exc) {
                //Il token non era valido...
                logger.error("token non valido", exc);
                return true;
            }
        }
    }

    private String refreshTokens(HttpServletRequest request, HttpServletResponse response, boolean required) {
        Cookie[] cookies = request.getCookies();
        String jwtIdToken = null, jwtAccessToken = null, jwtRefreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SSO_KC_ACCESS.equalsIgnoreCase(cookie.getName()))
                    jwtAccessToken = cookie.getValue();
                else if (SSO_KC_ID.equalsIgnoreCase(cookie.getName()))
                    jwtIdToken = cookie.getValue();
                else if (SSO_KC_REFRESH.equalsIgnoreCase(cookie.getName()))
                    jwtRefreshToken = cookie.getValue();
            }
        }

        if (jwtAccessToken != null && jwtRefreshToken != null) {

            boolean expired = false;

            try {
                Claims claims = ClientUtils.parseJWTTokenWithoutSecret(jwtAccessToken);

                long ttlMs = config.getTokenMinimumTimeToLive() * 1000;
                long exp = claims.getExpiration().getTime() - ttlMs;

                long still = exp - System.currentTimeMillis();

                expired = (still <= 0);
            } catch (ExpiredJwtException eje) {
                expired = true;
            }

            if (expired) {
                try {
                    KeycloakDeployment deployment = resolver.resolve(request.getRequestURI());
                    AccessTokenResponse resp = ServerRequest.invokeRefresh(deployment, jwtRefreshToken);

                    if (resp.getToken() != null)
                        response.addCookie(SecurityHelper.createCookie(SSO_KC_ACCESS, resp.getToken()));

                    if (resp.getRefreshToken() != null)
                        response.addCookie(SecurityHelper.createCookie(SSO_KC_REFRESH, resp.getRefreshToken()));

                    if (resp.getIdToken() != null && jwtIdToken != null)
                        response.addCookie(SecurityHelper.createCookie(SSO_KC_ID, resp.getIdToken()));

                } catch (JwtException | ServerRequest.HttpFailure failure) {

                    int status = 0;
                    if (failure instanceof ServerRequest.HttpFailure)
                        status = ((ServerRequest.HttpFailure) failure).getStatus();

                    if (status >= 400 && status < 500 || failure instanceof JwtException) {
                        String redir = (String) this.config.getConfig().getOrDefault("expired-url", "/auth/logout?redirect_uri=%2Fauth%2Fredirect%3Fsso");

                        if (Strings.isNotBlank(redir)) {
                            return redir;
                        }
                    }

                } catch (Exception exc) {
                    logger.error(exc.getMessage(), exc);
                }
            }
        }
        return null;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication oldauth = securityContext.getAuthentication();

        if (oldauth instanceof Session)
            securityContext.setAuthentication(null);

        if (securityFilter.isRestUrl(request)){
            try {
                Authentication auth = super.attemptAuthentication(request,response);

                if (auth == null){
                    response.sendError(401);
                    response.flushBuffer();
                }
                return auth;

            } catch (AuthenticationException failed) {
                request.setAttribute(RequestDispatcher.ERROR_EXCEPTION,failed);
                response.sendError(401);
                //response.flushBuffer();
                return null;
            } catch (Exception exc) {
                request.setAttribute(RequestDispatcher.ERROR_EXCEPTION,exc);
                response.sendError(500);
                //response.flushBuffer();
                return null;
            }
        } else {
            return super.attemptAuthentication(request,response);
        }
    }

    private String getNewToken(KeycloakAuthenticationToken token){
        OidcKeycloakAccount account = token.getAccount();
        RefreshableKeycloakSecurityContext ksc = (RefreshableKeycloakSecurityContext) account.getKeycloakSecurityContext();

        String jwtIdToken = ksc.getIdTokenString();
        String jwtAccessToken = ksc.getTokenString();
        String jwtRefreshToken = ksc.getRefreshToken();

        Map<String, Object> newClaims = ClientCacheAuthUtils.buildMergedClaims( config.getConfig(),jwtAccessToken,jwtIdToken );

        if (newClaims==null){
            throw new RuntimeException("missing keycloak configuration or default configuration");
        }

        //newClaims.put("kc_idtoken",jwtIdToken);
        //newClaims.put("kc_accesstoken",jwtAccessToken);

        if (!newClaims.containsKey("aoo"))
            newClaims.put("aoo", DesktopUtils.getAOOforRealm());

        String uniqueField = (String) config.getConfig().getOrDefault("uniquekey","USER_KEY");
        String uniqueValue = (String) newClaims.get(uniqueField);
        if (uniqueValue!=null){
            String aoo = (String) newClaims.get("aoo");
            Collection<Actor> actors = ClientCache.getInstance().getActorsByField(aoo,uniqueField,uniqueValue);
            if (actors.size()==1){
                User a0 = (User) actors.iterator().next();
                newClaims.put("sub",a0.getUserName());
            } else if (actors.size()>1)
                throw new RuntimeException(uniqueField + " non univoco:"+uniqueValue);
        }

        logger.debug("interactive authentication: \n\u001B[32maccessToken:\u001B[0m{}\n\u001B[32midToken:\u001B[0m{}\n\u001B[32mmapped:\u001B[0m{}",jwtAccessToken,jwtIdToken,newClaims );

        User user = ClientCacheAuthUtils.getInstance().getUserByClaims(newClaims);

        String secret = System.getProperty("secretKey", "SECRET");

        String newToken = ClientUtils.simpleJWTToken(user.getCodAoo(), user.getUserName(), secret, newClaims);

        return newToken;

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        if (config==null)
            throw new RuntimeException("missing configuration");

        /* se l'autenticazione non è interattiva KeyCloak prosegue la catena dei filtri nella chiamata a super */
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authResult;

        OidcKeycloakAccount account = token.getAccount();
        RefreshableKeycloakSecurityContext ksc = (RefreshableKeycloakSecurityContext) account.getKeycloakSecurityContext();

        String jwtIdToken = ksc.getIdTokenString();
        String jwtAccessToken = ksc.getTokenString();
        String jwtRefreshToken = ksc.getRefreshToken();

        try{
            String newToken = getNewToken((KeycloakAuthenticationToken) authResult);

            request.setAttribute(SecurityFilter.AUTHORIZATION,newToken);

            if (token.isInteractive()){

                List<String> cookies = null;

                Object obj = config.getConfig().get("setcookies");
                if (obj instanceof List)
                    cookies = (List) obj;
                else if (obj instanceof Map)
                    cookies = new ArrayList (((Map)obj).values());

                if (cookies==null)
                    cookies = Collections.singletonList(SecurityFilter.SSO_USER);

                for (String cookie : cookies){

                    if ( SecurityFilter.SSO_USER.equalsIgnoreCase(cookie)) {
                        SecurityHelper.AuthenticateRequest(response, newToken);
                    } else if (SSO_KC_ACCESS.equalsIgnoreCase(cookie)){
                        Cookie jwtAccessCookie = SecurityHelper.createCookie(SSO_KC_ACCESS,jwtAccessToken);
                        response.addCookie(jwtAccessCookie);
                    } else if (SSO_KC_ID.equalsIgnoreCase(cookie)){
                        Cookie jwtIdCookie = SecurityHelper.createCookie(SSO_KC_ID,jwtIdToken);
                        response.addCookie(jwtIdCookie);
                    } else if (SSO_KC_REFRESH.equalsIgnoreCase(cookie)){
                        Cookie jwtRefreshCookie = SecurityHelper.createCookie(SSO_KC_REFRESH,jwtRefreshToken);
                        response.addCookie(jwtRefreshCookie);
                    }   else {
                        Cookie c = SecurityHelper.createCookie(cookie,newToken);
                        response.addCookie(c);
                    }
                }

                super.successfulAuthentication(request,response,chain,authResult);
            } else {
                chain.doFilter(request, response);
            }


        } catch (Exception e) {
            request.setAttribute(RequestDispatcher.ERROR_EXCEPTION,e);
            AuthenticationException failed = new KeycloakAuthenticationException("unsuccessfulAuthentication",e);
            super.unsuccessfulAuthentication(request,response,failed);
        }

    }


    /*private static boolean isFiscalCode(String cf) {
        String cfp = "^[A-Z]{6}[0-9LMNPQRSTUV]{2}[A-EHLMPR-T]{1}[0-9LMNPQRSTUV]{2}[A-Z]{1}[0-9LMNPQRSTUV]{3}[A-Z]{1}$";
        Pattern pattern = Pattern.compile(cfp);
        Matcher matcher = pattern.matcher(cf);

        return matcher.matches();
    }*/

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        super.doFilter(servletRequest, servletResponse,filterChain);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}