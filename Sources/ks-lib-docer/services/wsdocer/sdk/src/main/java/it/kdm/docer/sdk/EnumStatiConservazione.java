package it.kdm.docer.sdk;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumStatiConservazione {
	
	da_non_conservare(0), 
	da_conservare(1), 
	inviato_a_conservazione(2), 
	conservato(3), 
	in_errore(4),
	da_aggiornare(5),
	in_attesa(6);
	
	
	private static final Map<Integer,EnumStatiConservazione> lookup = 
			new HashMap<Integer,EnumStatiConservazione>();

	static {
	    for(EnumStatiConservazione s : EnumSet.allOf(EnumStatiConservazione.class))
	         lookup.put(s.getCode(), s);
	}

	private int code;
	
	private EnumStatiConservazione(int code) {
	    this.code = code;
	}
	
	/**
	 * 
	 * @return
	 * 0 (da_non_conservare), 
	1 (da_conservare), 
	2 (inviato_a_conservazione), 
	3 (conservato), 
	4 (in_errore);
	 */
	public int getCode() { 
		return code; 
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public static EnumStatiConservazione getCode(int code) { 
	    return lookup.get(code); 
	}	
	
	
}
