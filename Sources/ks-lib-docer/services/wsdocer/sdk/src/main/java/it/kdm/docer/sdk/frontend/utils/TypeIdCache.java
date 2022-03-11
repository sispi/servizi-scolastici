package it.kdm.docer.sdk.frontend.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.slf4j.Logger;

public enum TypeIdCache {

	INSTANCE;
	
	private Logger log = org.slf4j.LoggerFactory.getLogger(TypeIdCache.class);

	private OMElement typeIdCache = null;
	private OMElement typeIdCacheAll = null;
	private Map<String, OMElement> typeIdCacheByAoo = new HashMap<String, OMElement>();
	
	private static String AOO_FORMAT = "%s|%s";

	public synchronized OMElement getTypeIds() {
		return typeIdCache;
	}

	public synchronized OMElement getAllTypeIds() {
		return typeIdCacheAll;
	}

	public synchronized OMElement getTypeIdsByAoo(String codEnte, String codAoo) {
		return typeIdCacheByAoo.get(String.format(AOO_FORMAT, codEnte, codAoo));
	}
	
	public synchronized void setTypeIds(OMElement typeIds) {
		log.debug("Caching type ids");
		this.typeIdCache = typeIds;
	}

	public synchronized void setTypeIdsByAoo(String codEnte, String codAoo, OMElement typeIds) {
		log.debug("Caching type ids");
		this.typeIdCacheByAoo.put(String.format(AOO_FORMAT, codEnte, codAoo), typeIds);
	}

	public synchronized void setAllTypeIds(OMElement typeIds) {
		log.debug("Caching all type ids");
		this.typeIdCacheAll = typeIds;
	}

}

	