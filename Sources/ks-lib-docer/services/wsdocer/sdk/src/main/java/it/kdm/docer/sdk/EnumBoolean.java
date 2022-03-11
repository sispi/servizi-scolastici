package it.kdm.docer.sdk;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumBoolean {

	FALSE(0), TRUE(1), UNSPECIFIED(-1);
	
	private static final Map<Integer,EnumBoolean> lookup = 
		new HashMap<Integer,EnumBoolean>();

	static {
	    for(EnumBoolean s : EnumSet.allOf(EnumBoolean.class))
	         lookup.put(s.getCode(), s);
	}
	
	private int code;
	
	private EnumBoolean(int code) {
	    this.code = code;
	}

	/**
	 * 
	 * @return
	 * 0 (FALSE), 1 (TRUE), -1 (UNSPECIFIED);
	 */
	public int getCode() { 
		return code; 
	}
	
	public static EnumBoolean getCode(int code) { 
	    return lookup.get(code); 
	} 	
}
