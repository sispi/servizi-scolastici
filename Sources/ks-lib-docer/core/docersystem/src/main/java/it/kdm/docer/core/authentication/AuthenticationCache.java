package it.kdm.docer.core.authentication;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.core.authentication.bean.LoginInfo;
import it.kdm.docer.core.authentication.bean.WSTicketAuthInfo;
import it.kdm.docer.core.configuration.ConfigurationManager;
import it.kdm.utils.ResourceLoader;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationMap;
import org.apache.commons.configuration.XMLConfiguration;

import java.io.File;
import java.security.ProviderException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Łukasz Kwasek on 26/11/14.
 */
public enum AuthenticationCache {
    INSTANCE;


    private static Pattern ticketHash = Pattern.compile("^.*\\|ticket[^:]+:([^|]+).*$");

//    private final Cache<String, LoginInfo> userInfoCache = CacheBuilder.newBuilder()
//            .maximumSize(this.getMaxCacheTokenValue())
//            .expireAfterWrite(1, TimeUnit.DAYS)
//            .build();
//
//    private final Cache<WSTicketAuthInfo, String> ticketCache = CacheBuilder.newBuilder()
//            .maximumSize(this.getMaxCacheTokenValue())
//            .expireAfterWrite(1, TimeUnit.DAYS)
//            .build();

    public static AuthenticationCache getInstance() throws Exception {
        return INSTANCE;
    }

    public String getWSTicket(WSTicketAuthInfo WSTicketAuthInfo) {
//        return ticketCache.asMap().get(WSTicketAuthInfo);
        ConfigurationManager man = new ConfigurationManager();
        try {
            return man.getWSTicket(WSTicketAuthInfo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void putWSTicket(WSTicketAuthInfo WSTicketAuthInfo, String ticket) {
//        ticketCache.put(WSTicketAuthInfo, ticket);
        ConfigurationManager man = new ConfigurationManager();
        try {
            man.putWSTicket(WSTicketAuthInfo,ticket);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public LoginInfo getLoginInfo(String token) {
//        return userInfoCache.asMap().get(extractTicket(token));
        ConfigurationManager man = new ConfigurationManager();
        try {
            return man.getLoginInfo(extractTicket(token));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void putLoginInfo(String token, LoginInfo loginInfo) {
//        userInfoCache.put(extractTicket(token), loginInfo);
        ConfigurationManager man = new ConfigurationManager();
        try {
            man.putLoginInfo(extractTicket(token),loginInfo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String extractTicket(String token) {
        Matcher m = ticketHash.matcher(token);
        if (m.matches()) {
            return m.group(1);
        } else {
            return token;
        }
    }

    public Integer getMaxCacheTokenValue() {

        Integer value;

        try {
            File confFile = ConfigurationUtils.loadResourceFile("authproviders.xml");

            XMLConfiguration conf = new XMLConfiguration(confFile);
            ConfigurationMap config = new ConfigurationMap(conf);

            String key = "auth-providers[@maxCacheToken]";
            if (!config.containsKey(key)) {
                return 1000;
//            throw new ProviderException("maxCacheToken setting not found");
            }


            try {
                value = Integer.parseInt((String) config.get(key));
            } catch (NumberFormatException ex) {
                throw new ProviderException("maxCacheToken setting is not a number");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return value;
    }


}
