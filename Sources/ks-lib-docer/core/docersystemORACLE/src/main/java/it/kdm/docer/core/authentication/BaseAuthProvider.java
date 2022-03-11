package it.kdm.docer.core.authentication;

import it.kdm.docer.core.authentication.bean.LoginInfo;
import it.kdm.docer.core.authentication.bean.SSOLoginCredential;
import org.apache.commons.configuration.ConfigurationMap;
import org.apache.commons.httpclient.auth.AuthenticationException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.naming.ConfigurationException;
import java.security.ProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseAuthProvider {

    private String epr;
    private String serviceName;
    private String authMode = "";
    private List<String> trustedServices = new ArrayList<String>();
    private ConfigurationMap config;
    private String sysGroup;

    public abstract LoginInfo login(String username, String password, String ente, String application) throws AuthenticationException;

    public SSOLoginCredential loginSSO(String saml, String ente, String application) throws AuthenticationException {
        throw new NotImplementedException();
    }

    public abstract boolean verifyTicket(String token, String ticket) throws Exception;

    public abstract String renewTicket(String token, String ticket) throws Exception;

    public abstract Map<String, String> getUserInfo(String token, String uid) throws Exception;

    public abstract String[] getUserGroups(String token) throws Exception;

    public abstract String getEnteDescription(String token) throws Exception;

    public final String getEpr() {
        return epr;
    }

    protected final String getEpr(String method) {
        String epr;
        if (this.getEpr().endsWith("/")) {
            epr = this.epr + method;
        } else {
            epr = this.epr + "/" + method;
        }

        return epr;
    }

    public final void setEpr(String epr) {
        this.epr = epr;
    }

    public final void setSysGroup(String sysGroup) {
        this.sysGroup = sysGroup;
    }

    public final String getSysGroup() {
        return this.sysGroup;
    }

    public String getAuthMode() {
        return this.authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public void setConfigSettings(ConfigurationMap config) {
        this.config = config;
    }

    public ConfigurationMap getConfigSettings() {
        return this.config;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public final List<String> getTrustedServices() {
        return this.trustedServices;
    }

    public final void setTrustedServices(List<String> services) {
        if (services != null)
            this.trustedServices = services;
    }

    public String getConfigSetting(String authMode, String key) throws ProviderException {

        String keyStr = String.format("auth-providers.%s.%s", authMode, key);
        if (!this.config.containsKey(keyStr)) {
            throw new ProviderException("Config Setting provider not found");
        }

        Object ret = this.config.get(keyStr);

        String outValue = "";

        if (ret instanceof ArrayList) {
            for (Object s : (ArrayList) ret)
                outValue += "," + (String) s;

            outValue = outValue.substring(1);
        } else
            outValue = (String) ret;

        return outValue;
    }

    public List<String> getConfigSection(String authMode, String section) throws ProviderException {

        String keyStr = String.format("auth-providers.%s.%s", authMode, section);
        if (!this.config.containsKey(keyStr)) {
            throw new ProviderException("Config Section provider not found");
        }

        List<String> ret = (List<String>) this.config.get(keyStr);

        return ret;
    }

    protected void initProvider() throws ConfigurationException {

    }
}
