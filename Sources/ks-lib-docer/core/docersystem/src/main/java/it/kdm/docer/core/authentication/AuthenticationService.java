package it.kdm.docer.core.authentication;


import org.apache.axis2.context.MessageContext;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.slf4j.Logger;

import java.util.Map;

public class AuthenticationService {


    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenticationService.class);

    public String login(String username, String password, String codiceEnte, String application) throws Exception {
        logger.debug(String.format("login(%s, %s)", username, "******"));

        String ipAddress = MessageContext.getCurrentMessageContext().getFrom().getAddress();

        AuthenticationManager manager = new AuthenticationManager();
        return manager.login(username, password, codiceEnte, ipAddress, application, null);
    }

    public String loginSSO(String saml, String codiceEnte, String application) throws Exception {


        String ipAddress = MessageContext.getCurrentMessageContext().getFrom().getAddress();

        AuthenticationManager manager = new AuthenticationManager();
        String token = manager.loginSSO(saml, codiceEnte, application, ipAddress);

        //SSOTicketCache.getInstance().putSaml(token, saml);

        return token;
    }

    public String getRealUser(String saml) throws Exception {
        AuthenticationManager manager = new AuthenticationManager();
        return manager.getRealUser(saml);
    }

    public String loginOnDemand(String token, String serviceName, String serviceUrl) throws Exception {

        AuthenticationManager manager = new AuthenticationManager();
        return manager.loginOnDemand(token, serviceName, serviceUrl);
    }

    public boolean verifyToken(String token) throws Exception {
        AuthenticationManager manager = new AuthenticationManager();
        return manager.verifyToken(token);
    }

    public String getEnteDescription(String token) throws Exception {
        logger.debug(String.format("getEnteDescription(%s)", token));

        AuthenticationManager manager = new AuthenticationManager();
        return manager.getEnteDescription(token);
    }

    public DefaultKeyValue[] getUserInfo(String token) throws Exception {
        logger.debug(String.format("getUserInfo(%s)", token));

        AuthenticationManager manager = new AuthenticationManager();
        Map<String, String> userInfo = manager.getUserInfo(token);
        DefaultKeyValue[] ret = new DefaultKeyValue[userInfo.size()];
        short i = 0;
        for (String key : userInfo.keySet()) {

            ret[i] = new DefaultKeyValue(key, userInfo.get(key));

            i++;
        }

        return ret;
    }

    public boolean logout(String token) throws Exception {
        return true;
        //throw new UnsupportedOperationException("Not yet implemented!");
    }
}
