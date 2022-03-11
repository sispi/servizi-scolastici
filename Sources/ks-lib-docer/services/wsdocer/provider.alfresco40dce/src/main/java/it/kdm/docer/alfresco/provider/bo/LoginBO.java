package it.kdm.docer.alfresco.provider.bo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.opensaml.saml2.core.Assertion;

import com.google.common.base.Joiner;

import it.emilia_romagna.regione.security_mw.ws.wsdl.SecurityError;
import it.kdm.docer.commons.configuration.ConfigurationLoadingException;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.docerservice.PasswordGenerator;
import it.kdm.docer.commons.sso.SsoUtils;
import it.kdm.docer.sdk.Constants;
import it.kdm.docer.sdk.IProvider;
import it.kdm.docer.sdk.classes.LoggedUserInfo;
import it.kdm.docer.sdk.classes.SsoUserInfo;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.ISsoUserInfo;
import it.kdm.docer.sdk.interfaces.IUserProfileInfo;
import it.kdm.docer.tools.sso.kit.backend.SsoBackendUtils;
import it.kdm.docer.tools.sso.kit.backend.bean.Validation;
import it.kdm.docer.tools.sso.kit.backend.bean.ValidationStatus;
import it.kdm.docer.tools.sso.kit.backend.exception.MiddlewareException;

/**
 * Created by Łukasz Kwasek on 11/02/15.
 */
public class LoginBO {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoginBO.class);

    private Configuration config;

    private final String mode;
    private final String testUser;
    private final String testPass;
    private final String serviceUser;
    private final String servicePass;
    private final String validateMWUrl;

    private String codiceEnte;
    private IProvider provider;

    public LoginBO(IProvider provider, String codiceEnte) {
        try {
            this.provider = provider;
            this.codiceEnte = codiceEnte;

            File cfg = ConfigurationUtils.loadResourceFile("provider-config.properties");            
            config = new PropertiesConfiguration(cfg);

            mode = config.getString("sso.login.mode", "test");
            testUser = config.getString("sso.login.test.username", "admin");
            testPass = config.getString("sso.login.test.password", "admin");
            serviceUser = config.getString("provider.service-user.username", "admin");
            servicePass = config.getString("provider.service-user.passwrod", "admin");
            validateMWUrl = config.getString("provider.middleware.url", "http://localhost:8080/SecurityMW/services/SecurityMW");

        } catch (ConfigurationException | ConfigurationLoadingException e) {
            throw new IllegalStateException(e);
        }
    }

    public ISsoUserInfo loginSso(String saml) throws DocerException {
        try {
            if (isValid(saml)) {

                String uid = getRealUser(saml);
                String pass = getPassword(uid);
                String ticket = login(uid, pass);
                String groups = getGroups(uid);

                SsoUserInfo info = new SsoUserInfo();

                info.setUserID(uid);
                info.setTicket(ticket);
                info.setGroups(groups);

                return info;

            } else {
                throw new DocerException(new IllegalArgumentException("Il saml in input non è valido"));
            }

        } catch (Exception e) {
            throw new DocerException(e);
        }
    }

    private String login(String uid, String pass) throws DocerException {
        return provider.login(uid, pass, codiceEnte);
    }

    private boolean isValid(String saml) throws SecurityError, MiddlewareException {

        SsoBackendUtils utils = new SsoBackendUtils("alfresco-provider", validateMWUrl);
        Validation validation = utils.validate(saml);

        return validation.getStatus() == ValidationStatus.OK;
    }

    private String getCF(String saml) {
        Assertion ass = SsoUtils.parseAssertion(saml);

        String cf = SsoUtils.getAssertionAttributeByName(ass, SsoUtils.CODICE_FISCALE);

        if (StringUtils.isBlank(cf)) {
            throw new IllegalStateException("CODICE_FISCALE non trovata nell'asserzione saml");
        }

        return cf;
    }

    private String getRealUser(String saml) {

        if (mode.equalsIgnoreCase("test")) {
            return testUser;
        }

        String cf = getCF(saml);

        return findUser(cf);
    }

    private String getPassword(String realUser) {

        if (mode.equalsIgnoreCase("test") && realUser.equalsIgnoreCase(testUser)) {
            return testPass;
        } else {
            if (mode.equalsIgnoreCase("md5")) {
                return PasswordGenerator.generateMD5(realUser);
            } else {
                return realUser;
            }
        }
    }

    private String findUser(String cf) {
        try {

            initServiceProvider();

            Map<String, List<String>> criteria = new HashMap<String, List<String>>();

            ArrayList<String> list = new ArrayList<String>();
            list.add(cf);

            criteria.put(Constants.user_cod_fiscale, list);


            List<IUserProfileInfo> results = provider.searchUsers(criteria);
            if (results.isEmpty()) {
                throw new IllegalStateException("User not found");
            }

            if (results.size() > 1) {
                throw new IllegalStateException("Too many users");
            }

            IUserProfileInfo result = results.get(0);
            String userId = result.getUserId();

            if (StringUtils.isBlank(userId)) {
                throw new IllegalStateException("Found user with no USER_ID");
            }

            destroySeviceProvider();

            return userId;

        } catch (DocerException e) {
            throw new IllegalArgumentException("Impersonificazione durante il login fallita");
        }
    }

    private String getGroups(String uid) throws DocerException {

        initServiceProvider();
        List<String> groups = provider.getUser(uid).getGroups();
        destroySeviceProvider();

        return Joiner.on(";").skipNulls().join(groups);
    }

    private LoggedUserInfo serviceLogin() throws DocerException {
        String ticket = login(serviceUser, servicePass);

        LoggedUserInfo loginUserInfo = new LoggedUserInfo();
        loginUserInfo.setCodiceEnte(codiceEnte);
        loginUserInfo.setTicket(ticket);
        loginUserInfo.setUserId(serviceUser);

        return loginUserInfo;

    }

    private void initServiceProvider() throws DocerException {
        provider.setCurrentUser(serviceLogin());
    }

    private void destroySeviceProvider() throws DocerException {
//        provider.setCurrentUser(null);
    }

}
