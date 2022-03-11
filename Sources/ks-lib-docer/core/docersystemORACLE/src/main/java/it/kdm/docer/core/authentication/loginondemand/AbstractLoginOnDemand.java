package it.kdm.docer.core.authentication.loginondemand;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.utils.ResourceLoader;
import org.apache.commons.configuration.ConfigurationMap;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.security.ProviderException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Łukasz Kwasek on 16/12/14.
 */
public abstract class AbstractLoginOnDemand {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(AbstractLoginOnDemand.class);

    private ConfigurationMap config;

    public AbstractLoginOnDemand() throws Exception {
        logger.debug("AbstractLoginOnDemand()");

        File confFile = ConfigurationUtils.loadResourceFile("authproviders.xml");

        XMLConfiguration conf = new XMLConfiguration(confFile);
        config = new ConfigurationMap(conf);
    }

    public abstract String login(String token, String serviceName, String serviceUrl) throws Exception;

    public final String getEpr(String serviceUrl, String method) {
        serviceUrl = cleanServiceUrl(serviceUrl);
        String epr;
        if (serviceUrl.endsWith("/")) {
            epr = serviceUrl + method;
        } else {
            epr = serviceUrl + "/" + method;
        }

        return epr;
    }

    public static Pattern serviceUrlPattern = Pattern.compile("(^.*/services/[^/.]+)(\\..+HttpSoap[0-9]+Endpoint)?/.*$");

    public String cleanServiceUrl(String serviceUrl) {
        Matcher m = serviceUrlPattern.matcher(serviceUrl);
        if (m.matches()) {
            return m.group(1);
        } else {
            return serviceUrl;
        }
    }

    public String getExpirationTokenValue() {

        String key = "auth-providers[@expirationToken]";
        if (!config.containsKey(key)) {
            throw new ProviderException("expirationToken setting not found");
        }

        return (String) config.get(key);
    }



}
