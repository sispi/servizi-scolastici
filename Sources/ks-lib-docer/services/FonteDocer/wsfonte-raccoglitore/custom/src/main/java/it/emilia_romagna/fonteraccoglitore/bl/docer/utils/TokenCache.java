package it.emilia_romagna.fonteraccoglitore.bl.docer.utils;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.jcs.engine.ElementAttributes;
import org.apache.jcs.engine.behavior.IElementAttributes;


public class TokenCache {

    private static JCS TOKEN_CACHE = null;

    public void clear() {

	try {

	    if (TOKEN_CACHE == null) {
		TOKEN_CACHE = JCS.getInstance("default");
	    }
	    else {
		TOKEN_CACHE.clear();
	    }
	}
	catch (CacheException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private static long expiringSeconds = 60; // 1 minuto

    public void setExpiring(int expireInMinutes) {
	expiringSeconds = expireInMinutes * 60;
    }

    public void put(String userId, String token) {

	try {
	    if (TOKEN_CACHE == null) {
		TOKEN_CACHE = JCS.getInstance("default");
	    }

	    IElementAttributes elem = new ElementAttributes();
	    elem.setIsEternal(false);
	    elem.setLastAccessTimeNow();
	    elem.setMaxLifeSeconds(expiringSeconds);

	    TOKEN_CACHE.put(userId, token, elem);

	}
	catch (CacheException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public String get(String userId) {

	try {

	    if (TOKEN_CACHE == null) {
		TOKEN_CACHE = JCS.getInstance("default");
	    }

	    Object token = TOKEN_CACHE.get(userId);

	    if (token == null) {
		return null;
	    }

	    IElementAttributes elem = new ElementAttributes();
	    elem.setIsEternal(false);
	    elem.setLastAccessTimeNow();
	    elem.setMaxLifeSeconds(expiringSeconds);
	    TOKEN_CACHE.put(userId, token, elem);
	    return String.valueOf(token);

	}
	catch (CacheException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return null;
	}
    }

}
