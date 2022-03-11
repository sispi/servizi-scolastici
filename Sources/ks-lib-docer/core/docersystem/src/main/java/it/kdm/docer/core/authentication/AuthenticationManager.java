package it.kdm.docer.core.authentication;

import com.google.common.base.Splitter;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.core.authentication.bean.LoginInfo;
import it.kdm.docer.core.authentication.bean.SSOLoginCredential;
import it.kdm.docer.core.authentication.loginondemand.SSOLoginOnDemand;
import it.kdm.docer.core.authentication.loginondemand.StandardLoginOnDemand;
import it.kdm.docer.core.authentication.providers.sso.SSOSaml2RealUserCache;
import it.kdm.docer.core.authentication.providers.sso.SSOTicketCache;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.utils.ResourceLoader;
import org.apache.commons.configuration.ConfigurationMap;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.joda.time.DateTime;

import java.io.File;
import java.security.KeyException;
import java.security.ProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuthenticationManager {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenticationManager.class);

    private ConfigurationMap config;

    public AuthenticationManager() throws Exception {
        logger.debug("AbstractLoginOnDemand()");

        File confFile = ConfigurationUtils.loadResourceFile("authproviders.xml");

        XMLConfiguration conf = new XMLConfiguration(confFile);
        config = new ConfigurationMap(conf);
    }


    public String login(String username, String password, String ente, String IPAddress, String application) throws Exception {
        return login(username, password, ente, IPAddress, application, null);
    }

    public String login(String username, String password, String ente, String IPAddress, String application, String providerName) throws Exception {
        logger.debug(String.format("login(%s, %s)", username, "******"));

        if (StringUtils.isEmpty(password))
            throw new AuthenticationException("Password non consentita. La Password non può essere vuota.");

        if (StringUtils.isEmpty(application) || StringUtils.isBlank(application)) {
            application = "[empty]";
        }
        if (StringUtils.isBlank(providerName)) {
            providerName = getDefaultProviderName();
        }

        String token = String.format("auth:%s|uid:%s|ipaddr:%s|app:%s|ente:%s|", providerName, username, IPAddress, application, ente);
        BaseAuthProvider provider = getProvider(token);

        try {
            // TODO: in caso di provider sso è errato chiamare questa firma: correggere
            // TODO: in caso di sso alfresco come lo loggiamo? (parlarne con Paolo)
            LoginInfo loginInfo = provider.login(username, password, ente, application);

            TicketCipher tc = new TicketCipher();
            String expirationTokenValue = getExpirationTokenValue();
            String encTicket = tc.encryptTicket(loginInfo.getTicket(), expirationTokenValue);

            token = Utils.addTokenKey(token, String.format("ticket%s", provider.getServiceName()), encTicket);

            for (Map.Entry<String, String> entry : loginInfo.getTokenKeys()) {
                token = Utils.addTokenKey(token, entry.getKey(), entry.getValue());
            }

            //trustedService
            List<String> trustedServicesList = provider.getTrustedServices();
            for (String trustedService : trustedServicesList)
                token = Utils.addTokenKey(token, trustedService, loginInfo.getTicket());

            //aggiunto nanotime al token per risolvere il problema della concorrenza (deadlock)
            token = Utils.addTokenKey(token, "nt", String.valueOf(System.nanoTime()));

            // Salva in cache user, pass, ente e application
            AuthenticationCache.getInstance().putLoginInfo(token, loginInfo);

//            try {
            String[] groupList = provider.getUserGroups(token);
            String userGroups = StringUtils.join(groupList, ";");

            token = Utils.addTokenKey(token, "userGroup", userGroups);
//            } catch (Exception e){
//                logger.warn("Non sono stati trovati i gruppi utente nel token");
//            }

            // Salva in cache user, pass, ente e application
            AuthenticationCache.getInstance().putLoginInfo(token, loginInfo);

            return token;
        } catch (Exception e) {
            logger.error("Autenticazione fallita", e);
            throw new AuthenticationException("Autenticazione fallita.");
        }
    }

    public String loginSSO(String saml, String codiceEnte, String application, String ipAddress) throws DocerException, AuthenticationException {

        try {
            String username = "admin";
            String token = String.format("auth:sso|ipaddr:%s|app:%s|ente:%s|", ipAddress, application, codiceEnte);
            BaseAuthProvider provider = getProvider(token);

            /*
            "saml" è il saml inviato dal client
            "ssoLoginCredential" contiene il saml NUOVO validato dal mw di engineering ed il ticket
             */

            SSOLoginCredential ssoLoginCredential = provider.loginSSO(saml, codiceEnte, application);

            if (StringUtils.isNotBlank(ssoLoginCredential.getRealUser())) {
                token = Utils.addTokenKey(token, "uid", ssoLoginCredential.getRealUser());
            } else {
                throw new AuthenticationException("Autenticazione fallita: il servizio di autenticazione non ha restituito un uid corretto");
            }

            TicketCipher tc = new TicketCipher();
            String expirationTokenValue = getExpirationTokenValue();
            String encTicket = tc.encryptTicket(ssoLoginCredential.getTicket(), expirationTokenValue);

            token = Utils.addTokenKey(token, String.format("ticket%s", provider.getServiceName()), encTicket);

            String userGroups = getUserGroups(ssoLoginCredential);

            token = Utils.addTokenKey(token, "userGroup", userGroups);

            // Se il servizio restituisce un saml, usiamo quello
            if (StringUtils.isNotBlank(ssoLoginCredential.getSaml())) {
                saml = ssoLoginCredential.getSaml();
            }

            SSOSaml2RealUserCache.getInstance().putRealUser(saml, ssoLoginCredential.getRealUser());
            SSOTicketCache.getInstance().putSaml(token, saml);

            return token;
        } catch (Exception e) {
            logger.error("Autenticazione fallita", e);
            throw new AuthenticationException("Autenticazione fallita.");
        }
    }

    private String getUserGroups(SSOLoginCredential ssoLoginCredential) {
        String[] groupList = Splitter
                .on(";")
                .omitEmptyStrings()
                .trimResults()
                .splitToList(ssoLoginCredential.getGroups())
                .toArray(new String[]{});

        return StringUtils.join(groupList, ";");
    }

    public String loginOnDemand(String token, String serviceName, String serviceUrl) throws Exception {

        String auth = Utils.extractTokenKey(token, "auth");
        if (StringUtils.isBlank(auth)) {
            auth = getDefaultProviderName();
        }

        if (auth.equalsIgnoreCase("sso")) {
            return new SSOLoginOnDemand().login(token, serviceName, serviceUrl);
        } else {
            return new StandardLoginOnDemand().login(token, serviceName, serviceUrl);
        }
    }

    private String getExpirationTokenValue() {

        String key = "auth-providers[@expirationToken]";
        if (!config.containsKey(key)) {
            throw new ProviderException("expirationToken setting not found");
        }

        return (String) config.get(key);
    }

    private String getDefaultProviderName() {

        String key = "auth-providers[@default]";
        if (!this.config.containsKey(key)) {
            throw new ProviderException("Default provider not found");
        }

        return (String) this.config.get(key);
    }

    private BaseAuthProvider getProvider(String token) throws KeyException, ClassNotFoundException, InstantiationException, IllegalAccessException, AuthenticationException, javax.naming.ConfigurationException {
        logger.debug(String.format("getProvider(%s)", token));

        String authMode;
        try {
            authMode = Utils.extractTokenKey(token, "auth");
        } catch (KeyException e) {
            logger.error(e.getMessage(), e);
            throw new AuthenticationException("Token is not valid");
        }

        String key = String.format("auth-providers.%s.class", authMode);
        if (!this.config.containsKey(key)) {
            throw new ProviderException("Authorization provider not found for mode: " + authMode);
        }

        String className = (String) this.config.get(key);
        Class<?> klass = Class.forName(className);
        BaseAuthProvider provider = (BaseAuthProvider) klass.newInstance();

        String eprKey = String.format("auth-providers.%s.epr", authMode);
        if (!this.config.containsKey(eprKey)) {
            throw new ProviderException(String.format("Configuration error for mode: %s. " +
                    "EPR not found", authMode));
        }

        provider.setAuthMode(authMode);

        //setta il config al provider per eventuali letture dei settings
        provider.setConfigSettings(config);


        String epr = (String) this.config.get(eprKey);
        provider.setEpr(epr);

        provider.setServiceName(epr.substring(epr.lastIndexOf("/") + 1));

        String trustedServicesKey = String.format("auth-providers.%s.trustedService", authMode);
        @SuppressWarnings("unchecked")
        ArrayList<String> trustedServices = (ArrayList<String>) this.config.get(trustedServicesKey);
        provider.setTrustedServices(trustedServices);

        provider.initProvider();

        return provider;
    }

    public boolean verifyToken(String token) throws Exception {
        logger.debug(String.format("verifyToken(%s)", token));

        BaseAuthProvider provider = getProvider(token);

        String ticket = Utils.extractTokenKey(token, "ticket" + provider.getServiceName());

        TicketCipher tc = new TicketCipher();
        String cleanTicket = tc.decryptTicket(ticket);

        //controllo scadenza token
        DateTime date = tc.getExpirationDate();
        if (date.isBeforeNow())
            return false;
        //throw new SecurityException("Token scaduto");

        return provider.verifyTicket(token, cleanTicket);
    }

    public Map<String, String> getUserInfo(String token) throws Exception {
        logger.debug(String.format("getUserInfo(%s)", token));

        BaseAuthProvider provider = getProvider(token);

        //String ticket = Utils.extractTokenKey(token, "ticket" + provider.getServiceName());
        String uid = Utils.extractTokenKey(token, "uid");

        Map<String, String> hash = provider.getUserInfo(token, uid);
        String userGroups = Utils.extractTokenKey(token, "userGroup");
        hash.put("___groups", userGroups);

        return hash;
    }

    public String getEnteDescription(String token) throws Exception {
        logger.debug(String.format("getUserInfo(%s)", token));

        BaseAuthProvider provider = getProvider(token);

        return provider.getEnteDescription(token);
    }

    public String getRealUser(String saml) {

        if (SSOSaml2RealUserCache.getInstance().contains(saml)) {
            return SSOSaml2RealUserCache.getInstance().getRealUser(saml);
        } else {
            throw new IllegalStateException("Saml non trovato in chace, impossibile estrarre il RealUser");
        }
    }
}
