package it.kdm.docer.commons.handlers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Łukasz Kwasek on 26/11/14.
 */
public enum ServiceFilterAuthenticationCache {
    INSTANCE;

    private static Pattern ticketHash = Pattern.compile("^.*\\|ticket[^:]+:([^|]+).*$");

    private final Cache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build();

    public static ServiceFilterAuthenticationCache getInstance() {
        return INSTANCE;
    }

    public String getTicket(String inTicket) {
        return cache.asMap().get(extractHash(inTicket));
    }

    public void putTicket(String inTicket, String outTicket) {
        cache.put(extractHash(inTicket), outTicket);
    }

    private static String extractHash(String ticket) {
        Matcher m = ticketHash.matcher(ticket);
        if (m.matches()) {
            return m.group(1);
        } else {
            return ticket;
        }
    }
}
