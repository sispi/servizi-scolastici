package keysuite.security;

import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.docer.client.ClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.*;


public class PreSecurityFilter implements Filter {

    protected final static Logger logger = LoggerFactory.getLogger(PreSecurityFilter.class);

    private static final String AUTHORIZATION ="authorization";

    private static final String JWKURL = "jwkURL";
    private static final String CLAIMS = "claims";
    private static final String MAPPINGS = "mappings";
    //public static final String ID_CLAIMS = "IDClaims";
    private Environment env;
    private Map config;
    //private String jwkURL;
    //String watchedPath = null;
    //long watchedTs = 0;

    //final static ObjectMapper YAMLmapper = new ObjectMapper(new YAMLFactory());
    final static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public PreSecurityFilter(Environment env, Map config){
        super();
        this.env = env;
        this.config = config;
    }

    /*private void reload(File file){
        try {
            this.watchedPath = file.getAbsolutePath();
            this.watchedTs = file.lastModified();
            this.config = YAMLmapper.readValue(file,Map.class);
        } catch (IOException e) {
            logger.error("configuration error",e);
        }
    }*/

    /*private void checkForChanges(){
        if (watchedPath==null)
            return;

        File current = new File(watchedPath);
        long newTs = current.lastModified();
        if (newTs>watchedTs){
            logger.info("configuration changed:"+watchedPath);
            reload(current);
        }
    }*/

    public Map getConfig() {
        return this.config;
    }

    public void setConfig(Map config) {
        //this.watchedPath = null;
        //this.watchedTs = 0;
        this.config = config;
    }

    /*public void setConfig(File file){
        if (file!=null) {
            reload(file);
        } else {
            this.watchedPath = null;
            this.watchedTs = 0;
            this.config = null;
        }
    }*/



    /*private static Map<String,Object> getRealmConfig(String realm){
        Map<String,Object> realms = (Map) this.config.get(MAPPINGS);
        if (realms==null || realm==null)
            return null;

        Map map = (Map) realms.getOrDefault(realm , realms.get("default"));
        return map;
    }*/

    public void verifyToken(String token){
        verifyToken(this.config,token);
    }

    public void verifyTokens(String... tokens){
        verifyTokens(this.config,tokens);
    }

    public void verifyTokens(Map<String,Object> config, String... tokens){
        if (tokens!=null) {
            for (String token : tokens)
                verifyToken(config, token);
        }
    }

    public static void verifyToken(Map<String,Object> config, String token){

        if (token==null)
            throw new IllegalArgumentException("token null");

        DecodedJWT decodeToken = JWT.decode(token);
        String issuer = decodeToken.getIssuer();

        String realm = ClientCacheAuthUtils.getRealm(issuer);

        if (realm==null)
            return;

        Map mappings = (Map) config.get(MAPPINGS);

        Map myConfig = (Map) mappings.getOrDefault(realm , mappings.get("default"));

        if (myConfig==null)
            return;

        String jwkURL = (String) myConfig.get(JWKURL);

        if (jwkURL == null){
            jwkURL = issuer + "/protocol/openid-connect/certs";
        }

        //DecodedJWT jwt = verifyJWTToken(authorizationHeader, jwkURL);
        // DecodedJWT jwt = null;

        try {
            //decodeToken = JWT.decode(authorizationHeader);
            if (!"none".equals(jwkURL)){
                Jwk jwk = getJWK(jwkURL);
                String alg = jwk.getAlgorithm();
                Algorithm algorithm;
                switch (alg){
                    case "RS256":
                    case "RSA256":
                    default:
                        algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
                }
                algorithm.verify(decodeToken);
            }

            //System.out.println("ok");
        }catch (Exception e){
            //decodeToken = null;
            String message = "Il token " + token +" non è in formato valido oppure non è stato possibile verificarlo. "+e.getMessage();
            logger.error(message, e);
            throw new RuntimeException(message, e);
        }
    }



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //checkForChanges();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authorizationHeader = getJWTToken(request);

        if (Strings.isNotEmpty(authorizationHeader))
            verifyToken(authorizationHeader);

        Map<String, Object> newClaims = ClientCacheAuthUtils.buildMergedClaims( (Map) this.config.get(MAPPINGS), authorizationHeader);

        if (newClaims!=null){

            logger.debug("bearer authentication: \ntoken:{}\nmapped:{}",authorizationHeader, newClaims );

            String AOO = (String) newClaims.get(ClientCacheAuthUtils.AOO_KEY);
            String username = (String) newClaims.get(ClientCacheAuthUtils.SUBJECT_KEY);

            if (AOO!=null && username!=null){
                String secret = System.getProperty("secretKey", "SECRET");

                String ksJwtToken = ClientUtils.simpleJWTToken(AOO, username, secret, newClaims);

                HeaderHttpRequestWrapper requestWrapper = new HeaderHttpRequestWrapper(request);
                requestWrapper.setBearer(ksJwtToken);

                if ( (Boolean) config.getOrDefault("setcookie",true) ){
                    requestWrapper.setAttribute("setcookie",true);
                }

                RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(requestWrapper, (HttpServletResponse) servletResponse));

                //filterChain.doFilter(requestWrapper, servletResponse);
                servletRequest = requestWrapper;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }





    private static String getJWTToken(HttpServletRequest request){
        String jwtToken = null;
        if(request != null) {
            jwtToken =request.getHeader(AUTHORIZATION);

            if (Strings.isNotEmpty(jwtToken) && jwtToken.startsWith("Bearer ")) {
                jwtToken = jwtToken.substring(7);
            }

            if(Strings.isEmpty(jwtToken)) {
                jwtToken = (String) request.getAttribute("pre_"+AUTHORIZATION);
            }

            if(Strings.isEmpty(jwtToken)) {
                jwtToken = (String) request.getAttribute(AUTHORIZATION);
            }
        }

        return jwtToken;
    }

    private static Jwk getJWK(String jwkURL) throws IOException {
        Map vals = JWKCache.getInstance().getConfiguration(jwkURL);
        if(vals == null || vals.isEmpty()){
            //ObjectMapper mapper = new ObjectMapper();
            vals = mapper.readValue(new URL(jwkURL), Map.class);
            if(vals != null){
                if(vals.containsKey("keys")){
                    Object obj = vals.get("keys");
                    if(obj instanceof Map){
                        vals = (Map) obj;
                    }else if (obj instanceof Collection){
                        List list = (List) obj;
                        if(list != null && list.size()>0){
                            Object obj1 = list.get(0);
                            if(obj1 != null && obj1 instanceof Map){
                                vals = (Map)obj1;
                            }
                        }
                    }
                    JWKCache.getInstance().put(jwkURL,vals);
                }
            }
        }

        Jwk jwk = null;
        if(vals != null && !vals.isEmpty()){
            jwk = Jwk.fromValues(vals);
        }
        return jwk;
    }

}
