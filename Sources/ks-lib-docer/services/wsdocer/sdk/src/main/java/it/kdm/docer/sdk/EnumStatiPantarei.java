package it.kdm.docer.sdk;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumStatiPantarei {

	undefined(0), 
	generico(1),
	daProtocollare(2),
	daFascicolare(3),
	protocollato(4), 
	fascicolato(5),
	allegato(6);
	
	private static final Map<Integer,EnumStatiPantarei> lookup = 
			new HashMap<Integer,EnumStatiPantarei>();

	static {
	    for(EnumStatiPantarei s : EnumSet.allOf(EnumStatiPantarei.class))
	         lookup.put(s.getCode(), s);
	}

	private int code;
	
	private EnumStatiPantarei(int code) {
	    this.code = code;
	}
	
	/**
	 * 
	 * @return
	 * 
	0 (undefined), 
	1 (generico),
	2 (daProtocollare),
	3 (daFascicolare),
	4 (protocollato), 
	5 (fascicolato),
	6 (allegato);
	 */
	public int getCode() { 
		return code; 
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public static EnumStatiPantarei getCode(int code) { 
	    return lookup.get(code); 
	}
	
	
}





