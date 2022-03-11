package it.kdm.docer.core.authorization;

import it.kdm.docer.commons.Utils;
import org.apache.axis2.AxisFault;
import org.slf4j.Logger;

import java.security.KeyException;

public class AuthorizationService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthorizationService.class);

    public boolean isAuthorized(String token, String service, String method) throws AxisFault {
        logger.debug(String.format("isAuthorized(%s, %s, %s)", token, service, method));

        String username;
        try {
            username = Utils.extractTokenKey(token, "uid");
        } catch (KeyException e) {
            logger.info(e.getMessage(), e);
            throw AxisFault.makeFault(new Exception("Token is not valid"));
        }

        try {
            AuthorizationManager manager = new AuthorizationManager();
            return manager.isAuthorized(token, username, service, method);
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
            throw AxisFault.makeFault(ex);
        }

    }

    public boolean checkRequest(String token, String service, String method, String docnumber, String extraData, String serviceUrl) throws AxisFault {
        logger.debug(String.format("checkRequest(%s, %s, %s)", token, service, method));
        return this.isAuthorized(token, service, method);
    }

    public String getMethodList(String token, int serviceId) throws Exception {

        AuthorizationManager manager = new AuthorizationManager();
        String xml = Utils.convertToXML(manager.getMethodList(serviceId));

        return xml;
    }

    public String getRules(String token, String serviceName, String methodName, String userName, String authType) throws Exception {

        AuthorizationManager manager = new AuthorizationManager();
        String xml = Utils.convertToXML(manager.getRules(serviceName, methodName, userName, authType));

        return xml;
    }

    public boolean addRule(String token, String serviceName, String methodId, String callerIp, String keyFilter, String userGroup, String authType, String systemRule, String enabled) throws Exception {

        AuthorizationManager manager = new AuthorizationManager();
        manager.addRule(serviceName, methodId, callerIp, keyFilter, userGroup, authType, systemRule, enabled);
        return true;
    }

    public boolean updateRule(String token, long id, String serviceName, String methodId, String callerIp, String keyFilter, String userGroup, String authType, String systemRule, String enabled) throws Exception {

        AuthorizationManager manager = new AuthorizationManager();
        manager.updateRule(id, serviceName, methodId, callerIp, keyFilter, userGroup, authType, systemRule, enabled);
        return true;
    }

    public boolean deleteRule(String token, long id) throws Exception {

        AuthorizationManager manager = new AuthorizationManager();
        manager.deleteRule(id);
        return true;
    }


}
