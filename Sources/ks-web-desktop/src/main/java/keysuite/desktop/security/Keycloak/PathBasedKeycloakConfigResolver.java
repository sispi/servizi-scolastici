package keysuite.desktop.security.Keycloak;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.keycloak.adapters.*;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PathBasedKeycloakConfigResolver implements KeycloakConfigResolver {

    private final Map<String, KeycloakDeployment> cache = new ConcurrentHashMap<String, KeycloakDeployment>();
    private static final Logger logger = LoggerFactory.getLogger(PathBasedKeycloakConfigResolver.class);

    //private KeycloakDeployment keycloakDeployment;

    @Autowired(
            required = false
    )
    private AdapterConfig adapterConfig;
    private Pattern pattern = null;

    public PathBasedKeycloakConfigResolver() {
    }

    void setAdapterConfig(AdapterConfig adapterConfig) {
        this.adapterConfig = adapterConfig;
        this.pattern = null;
        this.cache.clear();
    }

    public String getRealm(String uri){
        return getRealm(uri,pattern==null ? getPattern(this.adapterConfig) : pattern );
    }

    private static Pattern getPattern(AdapterConfig config){
        Pattern pattern = Pattern.compile("NONE", Pattern.CASE_INSENSITIVE); // ^https?://([a-z]\\w+)\\.
        if (config instanceof KeycloakSpringBootProperties){
            Map cfg =((KeycloakSpringBootProperties)config).getConfig();
            if (cfg!=null  && cfg.containsKey("pattern")){
                pattern = Pattern.compile( (String) cfg.get("pattern"), Pattern.CASE_INSENSITIVE);
            }
        }
        return pattern;
    }

    private static String getRealm(String uri,  Pattern pattern ){
        Matcher m = pattern.matcher(uri);
        if (m.find()) {
            if (m.groupCount()==0)
                return null;
            return m.group(1);
        } else
            return null;
    }

    public KeycloakDeployment build(String realm){

        AdapterConfig config = this.adapterConfig;
        KeycloakDeployment kd = KeycloakDeploymentBuilder.build(config);
        if (realm==null)
            realm = config.getRealm();
        kd.setRealm(realm);
        String baseUrl = kd.getAuthServerBaseUrl();
        //int idx = baseUrl.lastIndexOf("/auth/realms/");
        //baseUrl = baseUrl.substring(0,idx+"/auth/realms/".length())+realm;

        if (config instanceof KeycloakSpringBootProperties){

            Map cfg = ((KeycloakSpringBootProperties) config).getConfig();
            if (cfg!=null){
                cfg = (Map) cfg.get("mappings");
                if (cfg!=null){
                    cfg = (Map) cfg.get(realm);
                    if (cfg!=null){
                        baseUrl = (String) cfg.getOrDefault("auth-server-url",baseUrl);
                    }
                }
            }
        }

        KeycloakSpringBootProperties k2 = new KeycloakSpringBootProperties();
        k2.setAuthServerUrl(baseUrl);
        kd.setAuthServerBaseUrl(k2);

        if (null == kd.getAuthUrl()){
            throw new RuntimeException("invalid keycloak url:"+baseUrl);
        }

        return kd;
    }

    @Override
    public KeycloakDeployment resolve(OIDCHttpFacade.Request request) {
        try{
            return resolve(request.getURI());
        } catch (Exception e) {
            logger.error("invalid keycloak operation",e);

            KSKeycloakException exc = new KSKeycloakException(e,"invalid keycloak operation");
            request.setError(exc);
            return new KeycloakDeployment();
        }
    }

    public KeycloakDeployment resolve(String uri) {

            if (this.adapterConfig==null || this.adapterConfig.getRealm() == null)
                return new KeycloakDeployment();

            if (this.pattern==null){
                this.pattern = getPattern(this.adapterConfig);
            }

            String realm = getRealm(uri);

            if (realm==null)
                realm = this.adapterConfig.getRealm();

            KeycloakDeployment kd = cache.get(realm);

            if (kd != null)
                return kd;

            kd = build(realm);

            cache.put(realm,kd);

            return kd;
    }


}
