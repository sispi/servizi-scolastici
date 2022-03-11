package it.kdm.docer.commons.docerservice;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import it.kdm.docer.commons.configuration.ConfigurationUtils;

import java.net.URL;

/**
 * Created by Łukasz Kwasek on 18/12/14.
 */
public class PasswordGenerator {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(PasswordGenerator.class);

    public static String SECRET = "SECRET";

    static {
        try {
            
            String secret = ConfigurationUtils.loadProperties("sso-login.properties").getProperty("sso.login.md5.secret");

            if (StringUtils.isNotBlank(secret)) {
                SECRET = secret;
            }

        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    public static String generateMD5(String userId, String secret) {
        return DigestUtils.md5Hex(userId + secret);
    }

    public static String generateMD5(String userId) {
        return generateMD5(userId, SECRET);
    }
}
