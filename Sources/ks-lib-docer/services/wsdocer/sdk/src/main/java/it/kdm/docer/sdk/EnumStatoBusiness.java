package it.kdm.docer.sdk;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumStatoBusiness {

	undefined(0), 
	da_protocollare(1),
	da_fascicolare(2),	
	da_registrare(3),
	da_firmare(4);

	private static final Map<Integer,EnumStatoBusiness> lookup = 
			new HashMap<Integer,EnumStatoBusiness>();

	static {
	    for(EnumStatoBusiness s : EnumSet.allOf(EnumStatoBusiness.class))
	         lookup.put(s.getCode(), s);
	}

	private int code;
	
	private EnumStatoBusiness(int code) {
	    this.code = code;
	}
	
	/**
	 * 
	 * @return
	 * 0 (undefined), 
	1 (da_protocollare),
	2 (da_fascicolare),	
	3 (da_registrare),
	4 (da_firmare);
	 */
	public int getCode() { 
		return code; 
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public static EnumStatoBusiness getCode(int code) { 
	    return lookup.get(code); 
	}
	
	
}





