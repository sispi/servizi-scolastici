package it.kdm.docer.sdk;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumSearchOrder {


	ASC(0), DESC(1);
	
	private static final Map<Integer,EnumSearchOrder> lookup = 
		new HashMap<Integer,EnumSearchOrder>();

	static {
	    for(EnumSearchOrder s : EnumSet.allOf(EnumSearchOrder.class))
	         lookup.put(s.getCode(), s);
	}
	
	private int code;
	
	private EnumSearchOrder(int code) {
	    this.code = code;
	}

	/**
	 * 
	 * @return
	 * 0 (ASC), 1 (DESC);
	 */
	public int getCode() { 
		return code; 
	}
	
	public static EnumSearchOrder getCode(int code) { 
	    return lookup.get(code); 
	} 	
}
