package it.kdm.docer.core.authentication.providers;

import it.kdm.docer.commons.Config;
import it.kdm.docer.commons.LDAPManager;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.core.authentication.bean.LoginInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.httpclient.auth.AuthenticationException;

import javax.naming.ConfigurationException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LDAPProvider extends StandardProvider {

    private ConcurrentHashMap<String, LDAPConfig> configs = new ConcurrentHashMap<String, LDAPConfig>();
    private String secret;
    private boolean usePrefix;

    @Override
    public LoginInfo login(String username, String password, String codiceEnte, String application)
            throws AuthenticationException {

        String pwd = password;
        String generatedPassword = "";

        try {
            generatedPassword = md5(username);
            LDAPConfig config = getConfig(codiceEnte);
            String ldapUser = username;
            if (!password.equals(generatedPassword)) {
                if (!"".equals(config.getDnUserFormat())) {
                    String dnUserFormat = config.getDnUserFormat();
                    ldapUser = dnUserFormat.replace("{username}",ldapUser);
                }
                try {
                    LDAPManager.getInstance(config.getHostname(), config.getPort(), ldapUser, password);
                    pwd = generatedPassword;
                } catch (Exception err) {
                    pwd = password;
                }

            }

            LoginInfo info;
            if (usePrefix) {
                String prefix = config.getPrefix();
                info = super.login(prefix + Utils.USERNAME_SEP + username, pwd, codiceEnte, application);
                info.addTokenKey("prefix", prefix);
            } else {
                info = super.login(username, pwd, codiceEnte, application);
            }

            info.setPass(pwd);

            return info;

        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage(), e);
        }
    }

    private String md5(String username) {
        return DigestUtils.md5Hex(username + secret);
    }

    private LDAPConfig getConfig(String codiceEnte) throws AuthenticationException {
        LDAPConfig config = configs.get(codiceEnte);
        if (config == null) {
            throw new AuthenticationException("Configurazione LDAP non trovata per ente: " + codiceEnte);
        }

        return config;
    }

    @Override
    protected void initProvider() throws ConfigurationException {
        secret = this.getConfigSetting(getAuthMode(), "secret");
        usePrefix = Boolean.parseBoolean(this.getConfigSetting(getAuthMode(), "use-prefix"));

        XMLConfiguration.setDefaultListDelimiter('|');
        XMLConfiguration xmlConfiguration = (XMLConfiguration) this.getConfigSettings().getConfiguration();
        String key = "auth-providers.standard.configuration";
        for (int i = 0; i <= xmlConfiguration.getMaxIndex(key); i++) {
            SubnodeConfiguration subNode = xmlConfiguration.configurationAt(String.format("%s(%d)", key, i));

            //subNode.setDelimiterParsingDisabled(true);

            LDAPConfig ldapConfig = new LDAPConfig();

            String epr = subNode.getString("epr");
            String[] pars = epr.split(":");



            if (pars.length < 2)
                throw new ConfigurationException("Epr parameter not valid. Format: <servername/ipaddress>:<hostport>");

            ldapConfig.setHostname(pars[0]);
            ldapConfig.setPort(Integer.parseInt(pars[1]));

            ldapConfig.setPrefix(subNode.getString("prefix"));
            ldapConfig.setDnUserFormat(subNode.getString("ldap-user-dn-format"));



            configs.putIfAbsent(subNode.getString("ente"), ldapConfig);
        }
    }
}
