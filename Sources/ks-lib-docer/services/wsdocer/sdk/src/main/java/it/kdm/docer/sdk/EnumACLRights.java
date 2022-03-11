package it.kdm.docer.sdk;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumACLRights {

	undefined(-3), deny(-2), remove(-1), normalAccess(1), fullAccess(0), readOnly(2), viewProfile(3), createOnly(4), readAndCreate(5);

	private static final Map<Integer,EnumACLRights> lookup = 
			new HashMap<Integer,EnumACLRights>();

	static {
	    for(EnumACLRights s : EnumSet.allOf(EnumACLRights.class))
	         lookup.put(s.getCode(), s);
	}

	private int code;
	
	private EnumACLRights(int code) {
	    this.code = code;
	}
	
	/**
	 * 
	 * @return
	 * 1 (normalAccess), 0 (fullAccess), 2 (readOnly);           
	 *    [ compatibilita' DOCAREA: -3 (undefined), -2 (deny), -1 (remove), 3 (viewProfile) ]
	 */
	public int getCode() { 
		return code; 
	}
	
	public static EnumACLRights getCode(int code) { 
	    return lookup.get(code); 
	} 
		
}
