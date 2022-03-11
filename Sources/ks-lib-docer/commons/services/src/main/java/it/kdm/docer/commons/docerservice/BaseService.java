package it.kdm.docer.commons.docerservice;

import com.google.common.base.Strings;
import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.docerservice.provider.ISsoProvider;
import it.kdm.docer.commons.docerservice.provider.IStandardProvider;
import it.kdm.docer.commons.sso.SsoUtils;
import it.kdm.docer.sdk.exceptions.DocerException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.opensaml.saml2.core.Assertion;

import java.util.List;

/**
 * Created by Łukasz Kwasek on 17/12/14.
 */
public abstract class BaseService {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(BaseService.class);

    private ProviderUtils pu = new ProviderUtils();

    /**
     * Esegue la login sia sul documentale che su tutti i provider registrati in modalità STANDARD.
     *
     * @param userId
     * @param password
     * @param codiceEnte
     * @return
     * @throws BaseServiceException
     */
    public String baseLogin(String userId, String password, String codiceEnte) throws BaseServiceException {

        DocerServicesStub.Login login = new DocerServicesStub.Login();
        login.setCodiceEnte(codiceEnte);
        login.setPassword(password);
        login.setUserId(userId);
        DocerServicesStub.LoginResponse loginResp;

        try {
            // eseguire il login sull'authenticationService per il retrieve del
            // token
            loginResp = ClientManager.INSTANCE.getDocerServicesClient().login(login);

        } catch (Exception e) {
            throw new BaseServiceException("Impossibile collegarsi al sistema documentale. " + e.getMessage());
        }

        try {
            String ticket = loginProviders(userId, password, codiceEnte);

            ticket = Utils.addTokenKey(ticket, "ente", codiceEnte);
            ticket = Utils.addTokenKey(ticket, "uid", userId);
            ticket = Utils.addTokenKey(ticket, "documentale", loginResp.get_return());

            return ticket;

        } catch (Exception e) {
            throw new BaseServiceException(e);
        }

    }

    /**
     * Esegue il login sul documentale e su tutti i provider registrati in modalità SSO.
     *
     * @param saml
     * @param codiceEnte
     * @return
     * @throws BaseServiceException
     */
    public String baseLoginSSO(String saml, String codiceEnte) throws BaseServiceException {

        Assertion ass = SsoUtils.parseAssertion(saml);
        String userId = SsoUtils.getUserID(ass);

        String realUser = SsoUtils.getAssertionAttributeByName(ass, SsoUtils.REAL_USER_ID);
        if (!Strings.isNullOrEmpty(realUser)) {
            userId = realUser;
        }

        DocerServicesStub.LoginSSO login = new DocerServicesStub.LoginSSO();
        login.setCodiceEnte(codiceEnte);
        login.setSaml(saml);
        DocerServicesStub.LoginSSOResponse loginResp;

        try {
            // eseguire il login sull'authenticationService per il retrieve del
            // token
            loginResp = ClientManager.INSTANCE.getDocerServicesClient().loginSSO(login);

        } catch (Exception e) {
            throw new BaseServiceException("Impossibile collegarsi al sistema documentale. " + e.getMessage());
        }

        String ticketSSO = null;
        String uid = null;

        for (DocerServicesStub.KeyValuePair p : loginResp.get_return()) {
            if (p.getKey().equalsIgnoreCase("ticket")) {
                ticketSSO = p.getValue();
            }
            if (p.getKey().equalsIgnoreCase("uid")) {
                uid = p.getValue();
            }
        }

        if (!Strings.isNullOrEmpty(uid)) {
            userId = uid;
        }

        try {
            String ticket = loginSsoProviders(saml, codiceEnte);

            ticket = Utils.addTokenKey(ticket, "ente", codiceEnte);
            ticket = Utils.addTokenKey(ticket, "uid", userId);
            ticket = Utils.addTokenKey(ticket, "documentale", ticketSSO);

            return ticket;

        } catch (BaseServiceException e) {
            throw new BaseServiceException(e);
        }

    }

    /**
     * Esegue solo il login sui provider registrati in modalità STANDARD.
     *
     * @param user
     * @param pass
     * @param ente
     * @return
     * @throws BaseServiceException
     */
    public String loginProviders(String user, String pass, String ente) throws BaseServiceException {

        String token = "";

        try {
            List<ProviderInfo> info = pu.extractProviderInfoList(ente);

            int ind = 0;
            for (ProviderInfo i : info) {
                ind++;
                try {
                    if (i.getEnte().equalsIgnoreCase(ente)) {

                        String ticket;
                        IStandardProvider provider = pu.getProvider(i);

                        if (i.getMode() == LoginMode.STANDARD) {
                            ticket = provider.login(user, pass, ente);
                        } else {
                            UserProfile up = new UserProfile("user");
                            ticket = defaultLogin(i, ente, provider, up);
                        }


                        String keyProvider = "Provider_" + ente + "_" + i.getAoo();
                        //fix per inizializzazione registro
                        if(i.getRegistro()!=null) {
                            keyProvider += "_"+i.getRegistro();
                        }
                        token = Utils.addTokenKey(token, keyProvider, ticket);

                    }
                } catch (Exception e) {
                    String message = "Error nell'esecuzione del provider (login) %d per l'ente %s";
                    log.error(String.format(message, ind, ente), e);
                }
            }

            return token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseServiceException(e);
        }

    }

    /**
     * Esegue la login sui provider in modalità SSO.
     *
     * @param saml
     * @param ente
     * @return
     * @throws BaseServiceException
     */
    public String loginSsoProviders(String saml, String ente) throws BaseServiceException {

        String token = "";

        try {
            List<ProviderInfo> info = pu.extractProviderInfoList(ente);

            int ind = 0;
            for (ProviderInfo i : info) {
                ind++;
                try {
                    if (i.getEnte().equalsIgnoreCase(ente)) {

                        String ticket;
                        IStandardProvider provider = pu.getProvider(i);

                        if (i.getMode() == LoginMode.SSO && provider instanceof ISsoProvider) {
                            ticket = ((ISsoProvider) provider).loginSSO(saml, ente);
                        } else {
                            UserProfile up = extractUserProfile(saml);
                            ticket = defaultLogin(i, ente, provider, up);
                        }

                        String keyProvider = "Provider_" + ente + "_" + i.getAoo();
                        token = Utils.addTokenKey(token, keyProvider, ticket);
                    }
                } catch (Exception e) {
                    String message = "Error nell'esecuzione del provider (loginSSO) %d per l'ente %s";
                    log.error(String.format(message, ind, ente), e);
                }
            }

            return token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseServiceException(e);
        }
    }

    public UserProfile extractUserProfile(String saml) {
        // TODO: da implementare

        Assertion ass = SsoUtils.parseAssertion(saml);

        String userId = SsoUtils.getUserID(ass);
        String codiceEnte = SsoUtils.getAssertionAttributeByName(ass, SsoUtils.CODICE_ENTE);
        String email = SsoUtils.getAssertionAttributeByName(ass, SsoUtils.EMAIL);
        String realUserId = SsoUtils.getAssertionAttributeByName(ass, SsoUtils.REAL_USER_ID);

        UserProfile ui = new UserProfile();
        ui.setUserID(userId);
        if (realUserId != null && !realUserId.isEmpty()) {
            ui.setUserID(realUserId);
        }
        ui.setCodiceEnte(codiceEnte);
        ui.setEmail(email);

        return ui;
    }


    private String defaultLogin(ProviderInfo i, String ente, IStandardProvider provider, UserProfile up) throws DocerException {
        String ticket;

        String user;
        String pass;

        if (i.isMd5() && provider instanceof ISsoProvider && up != null) {
            user = up.getUserID();
            pass = PasswordGenerator.generateMD5(user);
        } else {
            user = i.getDefaultUser();
            pass = i.getDefaultPassword();
        }

        if (StringUtils.isBlank(user) || StringUtils.isBlank(pass)) {
            throw new DocerException("Impossibile eseguire login di fallback, username o password di default non trovati");
        }

        try {
            ticket = provider.login(user, pass, ente);
        } catch (Exception e) {
            throw new DocerException(e);
        }

        return ticket;
    }

}
