package it.kdm.docer.ws;

import com.github.underscore.lodash.U;
import com.google.common.base.Strings;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.ResourceCache;
import it.kdm.docer.commons.docerservice.PasswordGenerator;
import it.kdm.docer.core.authentication.*;
import it.kdm.docer.sdk.classes.xsd.KeyValuePair;
import it.kdm.docer.sdk.exceptions.DocerException;
//import it.kdm.orchestratore.session.Session;
import keysuite.SessionUtils;
import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.docer.client.Group;
import keysuite.docer.client.User;
import keysuite.docer.interceptors.Logging;
import org.apache.commons.collections.keyvalue.xsd.DefaultKeyValue;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.naming.Context;
import javax.naming.directory.InitialDirContext;
import javax.xml.bind.JAXBElement;
import java.lang.Exception;
import java.nio.charset.Charset;
import java.util.*;

//import static it.kdm.docer.commons.handlers.ServiceFilterUtils.TICKET_KEY;

@Endpoint
@Logging(group = "AuthenticationService")
@Controller
@RequestMapping("docersystem/methods/AuthenticationService")
public class AuthenticationServiceEndpoint {

    public static final String TICKET_KEY = "ticketDocerServices";

    protected final static Logger logger = LoggerFactory.getLogger(AuthenticationServiceEndpoint.class);

    TicketCipher tc = new TicketCipher();
    private BusinessLogic BLDocer;

    static Map<String, Object> config;
    static Map<String, Map<String, Object>> ldapConfigs;
    static List<String> legacy;
    static ResourceCache.ResourceFile confFile = null;
    long expirationMillis;

    public AuthenticationServiceEndpoint() {

        try {
            BLDocer = new BusinessLogic();

            if (confFile == null || !confFile.isValid()) {
                ResourceCache.ResourceFile f = ConfigurationUtils.loadResourceFile("configuration.xml");

                String xml = FileUtils.readFileToString(f, Charset.defaultCharset());

                Map<String, Object> map = (Map) U.fromXml(xml);

                String expirationTokenValue = (String) ((Map) U.get(map, "configuration.auth-providers")).getOrDefault("-expirationToken", "D1");

                DateTime date = new DateTime();
                DateTime date0 = new DateTime(date);
                int expirationValue = 0;
                try {
                    expirationValue = Integer.parseInt(expirationTokenValue.substring(1));
                } catch (Exception e) {
                    throw new SecurityException("Parametro 'expiration' non valido", e);
                }

                //formato expiration: [M1|D2|m30|s40]
                if (expirationTokenValue.startsWith("M"))
                    date = date.plusMonths(expirationValue);

                if (expirationTokenValue.startsWith("D"))
                    date = date.plusDays(expirationValue);

                if (expirationTokenValue.startsWith("m"))
                    date = date.plusMinutes(expirationValue);

                if (expirationTokenValue.startsWith("s"))
                    date = date.plusSeconds(expirationValue);

                expirationMillis = date.getMillis()-date0.getMillis();

                config = U.get(map, "configuration.auth-providers.ldap");

                String apps = (String) config.get("legacy-apps");
                legacy = new ArrayList<>();

                if (!Strings.isNullOrEmpty(apps))
                    legacy = Arrays.asList(apps.split(","));

                ldapConfigs = new HashMap<>();

                Object ldaps = U.get(config, "configuration");
                List<Map> l = ldaps instanceof List ? (List<Map>) ldaps : Collections.singletonList((Map) ldaps);

                for (Map ldapConf : l) {
                    if (ldapConf != null) {
                        ldapConfigs.put((String) ldapConf.get("ente"), ldapConf);
                    }
                }

                confFile = f;
            }

        } catch (java.lang.Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    private String ldapLogin(String username, String password, String codiceEnte) throws AuthenticationException {

        User user = ClientCache.getInstance().getUser(codiceEnte, username);

        if (user == null)
            throw new AuthenticationException("user not found:" + username);

        String dmsTicket = ClientCacheAuthUtils.getInstance().getDMSTicket(user, ClientCacheAuthUtils.getInstance().encrypt(password));
        String generatedPassword = ClientCacheAuthUtils.generateMD5(username);
        String userPassword = user.getPassword();

        if (password.equals(generatedPassword) || password.equals(userPassword))
            return dmsTicket;

        Map<String, Object> ldapConfig = ldapConfigs.getOrDefault(codiceEnte, config);

        String hostname = ldapConfig.get("epr") instanceof String ? (String) ldapConfig.get("epr") : null;

        if (Strings.isNullOrEmpty(hostname))
            throw new AuthenticationException("wrong password (no ldap)");

        String dnUserFormat = ldapConfig.get("ldap-user-dn-format") instanceof String ? (String) ldapConfig.get("ldap-user-dn-format") : null;

        String ldapUser;

        if (!Strings.isNullOrEmpty(dnUserFormat))
            ldapUser = dnUserFormat.replace("{username}", username);
        else
            ldapUser = username;

        String providerURL = "ldap://" + hostname;

        if (!hostname.contains(":"))
            providerURL += ":389";

        try {
            if (!"true".equals(ldapConfig.get("test"))) {

                Properties props = new Properties();
                props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                props.put(Context.PROVIDER_URL, providerURL);
                props.put(Context.SECURITY_AUTHENTICATION, "simple");
                props.put(Context.SECURITY_PRINCIPAL, ldapUser);
                props.put(Context.SECURITY_CREDENTIALS, password);

                new InitialDirContext(props);
            } else {
                logger.warn("test ldap:" + ldapUser + ":" + password + " on " + providerURL);
            }

            return dmsTicket;

        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    private String systemLogin(String username, String password, String application) throws AuthenticationException {
        boolean isJwt = !legacy.contains(application) && !legacy.contains("*");

        String ticket = ldapLogin(username, password, null);

        String token;

        List<String> groupList = new ArrayList<>();
        //groupList.add("admins");
        groupList.add("SYS_ADMINS");

        String userGroups = StringUtils.join(groupList, ";");

        String IPAddress = SessionUtils.getRequest().getRemoteAddr();

        if (isJwt) {

            String audience = "system";

            Map<String, Object> claims = new LinkedHashMap<>();
            claims.put("userGroup", userGroups);
            claims.put("IPAddress", IPAddress);
            if (password!=null && "true".equals(System.getProperty("ondemand")))
                claims.put("secret", ClientCacheAuthUtils.getInstance().encrypt(password) );

            token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setAudience(audience)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis()+expirationMillis))
                    .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(System.getProperty("secretKey", "SECRET").getBytes()))
                    .compact();

        } else {

            token = String.format("auth:ldap|uid:%s|ipaddr:%s|app:%s|ente:%s|", username, IPAddress, application, "");

            token = Utils.addTokenKey(token, TICKET_KEY, tc.encryptTicket(ticket));
            token = Utils.addTokenKey(token, "nt", String.valueOf(System.nanoTime()));
            token = Utils.addTokenKey(token, "userGroup", userGroups);
            token = Utils.addTokenKey(token, "expiration", "" + (System.currentTimeMillis()+expirationMillis));

            String md5 = ClientCacheAuthUtils.generateMD5(token);

            token = Utils.addTokenKey(token, "md5", md5);
        }
        return token;
    }

    public String login(String username, String password, String enteOrAoo, String application) throws AuthenticationException {

        if ( ("system".equals(enteOrAoo) || Strings.isNullOrEmpty(enteOrAoo)) && "admin".equals(username)){
            return systemLogin(username,password,application);
        }

        if (StringUtils.isEmpty(enteOrAoo)) {
            throw new AuthenticationException("ente o aoo non può essere vuoto.");
        }

        if (StringUtils.isEmpty(username)) {
            throw new AuthenticationException("username non può essere vuoto.");
        }

        if (StringUtils.isEmpty(password)) {
            throw new AuthenticationException("Password non consentita. La Password non può essere vuota.");
        }

        if (StringUtils.isEmpty(application) || StringUtils.isBlank(application)) {
            application = "[empty]";
        }

        String IPAddress = SessionUtils.getRequest().getRemoteAddr();

        Group enteProfile = null;
        Group aooProfile = ClientCache.getInstance().getAOO(enteOrAoo);
        String ente;

        if (aooProfile == null) {
            enteProfile = ClientCache.getInstance().getEnte(enteOrAoo);

            if (enteProfile == null)
                throw new AuthenticationException("ente o aoo non trovato:" + enteOrAoo);

            ente = enteProfile.getCodEnte();

            for (Group g : ClientCache.getInstance().getAllAOOs()) {
                if (ente.equals(g.getCodEnte())) {
                    aooProfile = g;
                    break;
                }
            }

        } else {

            ente = aooProfile.getCodEnte();
            enteProfile = ClientCache.getInstance().getEnte(ente);

            if (enteProfile == null)
                throw new AuthenticationException("ente non trovato:" + ente);
        }

        Collection<Group> groups = ClientCache.getInstance().getUserGroups(enteOrAoo, username);
        List<String> groupList = new ArrayList<>();

        for (Group g : groups)
            groupList.add(g.getGroupId());

        String userGroups = StringUtils.join(groupList, ";");

        String ticket = ldapLogin(username, password, ente);

        boolean isJwt = !legacy.contains(application) && !legacy.contains("*");

        String token;

        if (isJwt) {

            String audience = aooProfile != null ? aooProfile.getCodAoo() : ente;

            Map<String, Object> claims = new LinkedHashMap<>();
            claims.put("userGroup", userGroups);
            claims.put("IPAddress", IPAddress);
            if (password!=null && "true".equals(System.getProperty("ondemand")))
                claims.put("secret", ClientCacheAuthUtils.getInstance().encrypt(password) );

            token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setAudience(audience)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis()+expirationMillis))
                    .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(System.getProperty("secretKey", "SECRET").getBytes()))
                    .compact();

        } else {

            token = String.format("auth:ldap|uid:%s|ipaddr:%s|app:%s|ente:%s|", username, IPAddress, application, ente);

            if (aooProfile != null)
                token = Utils.addTokenKey(token, "aoo", aooProfile.getCodAoo());

            if (!Strings.isNullOrEmpty(enteProfile.getPrefix()))
                token = Utils.addTokenKey(token, "prefix", enteProfile.getPrefix());

            token = Utils.addTokenKey(token, TICKET_KEY, tc.encryptTicket(ticket));
            token = Utils.addTokenKey(token, "nt", String.valueOf(System.nanoTime()));
            token = Utils.addTokenKey(token, "userGroup", userGroups);
            token = Utils.addTokenKey(token, "expiration", "" + (System.currentTimeMillis()+expirationMillis));

            String md5 = ClientCacheAuthUtils.generateMD5(token);

            token = Utils.addTokenKey(token, "md5", md5);
        }
        return token;
    }

    /*@RequestMapping(value="login", method={RequestMethod.GET})
    @ResponsePayload
    public LoginResponse loginGET(@RequestParam String username,@RequestParam String password,@RequestParam String enteOrAoo,@RequestParam String application) throws IOException {
        String token = login(username,password,enteOrAoo,application);
        return WSTransformer.getReturn(LoginResponse.class,token);
    }*/

    @PayloadRoot(namespace = "http://authentication.core.docer.kdm.it", localPart = "login")
    @ResponsePayload
    public LoginResponse login(@RequestPayload Login request) throws AuthenticationException {

        String application = "";
        if (request.getApplication()!=null)
            application = request.getApplication().getValue();
        String token = login(request.getUsername().getValue(),
                request.getPassword().getValue(),
                request.getCodiceEnte().getValue(),
                application);

        return WSTransformer.getReturn(LoginResponse.class, token);
    }

    public static void verifyToken(String token) {
        String md5 = Utils.extractTokenKeyNoExc(token, "md5");
        String md5check = PasswordGenerator.generateMD5(Utils.removeTokenKey(token, "md5"));

        if (!md5check.equals(md5)) {
            throw new MalformedJwtException("invalid token");
        }

        String exp = Utils.extractTokenKeyNoExc(token, "expiration");

        DateTime date = new DateTime(Long.parseLong(exp));

        if (date.isBeforeNow())
            throw new ExpiredJwtException(null, null, "expired token");
    }

    //getUserInfo
    @PayloadRoot(namespace = "http://authentication.core.docer.kdm.it", localPart = "getUserInfo")
    @ResponsePayload
    public GetUserInfoResponse getUserInfo(@RequestPayload GetUserInfo request) throws AuthenticationException {

        String token = request.getToken().getValue();
        String userId = Utils.extractTokenKeyNoExc(token, "uid");

        try {
            Map<String, String> map = BLDocer.getUser(token, userId);

            GetUserInfoResponse getUserInfoResponse = new GetUserInfoResponse();
            List<KeyValuePair> pairs = WSTransformer.toPairs(map);
            for (KeyValuePair kvp : pairs) {
                DefaultKeyValue dkv = WSTransformer.authXsdFactory.createDefaultKeyValue();
                dkv.setKey((JAXBElement) kvp.getKey());
                dkv.setValue((JAXBElement) kvp.getValue());
                getUserInfoResponse.getReturn().add(dkv);
            }

            return getUserInfoResponse;

        } catch (DocerException de) {
            throw new AuthenticationException(de.getMessage());
        }


    }

    //getEnteDescription
    @PayloadRoot(namespace = "http://authentication.core.docer.kdm.it", localPart = "getEnteDescription")
    @ResponsePayload
    public GetEnteDescriptionResponse getEnteDescription(@RequestPayload GetEnteDescription request) throws AuthenticationException {

        String token = request.getToken().getValue();
        String ente = Utils.extractTokenKeyNoExc(token, "ente");
        String desc = ClientCache.getInstance().getEnte(ente).getGroupName();

        return WSTransformer.getReturn(GetEnteDescriptionResponse.class, desc);
    }

    //getRealUser
    @PayloadRoot(namespace = "http://authentication.core.docer.kdm.it", localPart = "getRealUser")
    @ResponsePayload
    public GetRealUserResponse getRealUser(@RequestPayload GetRealUser request) throws AuthenticationException {

        String jwtToken = request.getJwtToken().getValue();
        String user = ClientCacheAuthUtils.getInstance().getUser(jwtToken).getUserName();

        return WSTransformer.getReturn(GetRealUserResponse.class, user);
    }

    //verifyToken
    @PayloadRoot(namespace = "http://authentication.core.docer.kdm.it", localPart = "verifyToken")
    @ResponsePayload
    public VerifyTokenResponse verifyToken(@RequestPayload VerifyToken request) throws AuthenticationException {
        boolean ver;
        try {
            verifyToken(request.getToken().getValue());
            ver = true;
        } catch (Exception e) {
            ver = false;
        }
        return WSTransformer.getReturn(VerifyTokenResponse.class, ver);
    }

    //logout
    @PayloadRoot(namespace = "http://authentication.core.docer.kdm.it", localPart = "logout")
    @ResponsePayload
    public LogoutResponse logout(@RequestPayload Logout request) throws AuthenticationException {
        return WSTransformer.getReturn(LogoutResponse.class, Boolean.TRUE);
    }
}
