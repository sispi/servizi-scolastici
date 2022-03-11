package it.kdm.docer.core.authentication.providers.sso;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import it.kdm.docer.commons.TicketCipher;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Łukasz Kwasek on 26/11/14.
 */
public enum SSOSaml2RealUserCache {
    INSTANCE;

    private final Cache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build();

    public static SSOSaml2RealUserCache getInstance() {
        return INSTANCE;
    }

    public void putRealUser(String saml, String realUser) {
        cache.put(saml, realUser);
    }

    public boolean contains(String saml) {
        return cache.asMap().containsKey(saml);
    }

    public String getRealUser(String saml) {
        return cache.asMap().get(saml);
    }

}
