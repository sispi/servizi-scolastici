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
public enum SSOTicketCache {
    INSTANCE;

    private static Pattern ticketHash = Pattern.compile("^.*\\|[^|]*ticket[^:]*:([^|]+).*$");

    private final Cache<String, String> ticketSamlCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build();

    public static SSOTicketCache getInstance() {
        return INSTANCE;
    }

    public void putSaml(String token, String saml) {
        ticketSamlCache.put(extractTicket(token), saml);
    }

    public boolean contains(String token) {
        return ticketSamlCache.asMap().containsKey(extractTicket(token));
    }

    public String getSaml(String ticket) {
        return ticketSamlCache.asMap().get(extractTicket(ticket));
    }

    private static String extractTicket(String token) {

        return deepDecrypt(token);
    }

    private static String deepDecrypt(String token) {

        String ticket;

        Matcher m = ticketHash.matcher(token);
        if (m.matches()) {
            ticket = m.group(1);
        } else {
            ticket = token;
        }

        try {
            TicketCipher tc = new TicketCipher();
            ticket = tc.decryptTicket(ticket);

            return deepDecrypt(ticket);

        } catch (Exception e) {
            return ticket;
        }
    }
}
