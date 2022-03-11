package it.kdm.docer.sdk;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumStatoArchivistico {
	
	undefined(-1), 
	generico(0),
	generico_definitivo(1),
	registrato(2),	
	protocollato(3), 
	classificato(4),
	fascicolato(5),
	in_archivio_di_deposito(6);

	private static final Map<Integer,EnumStatoArchivistico> lookup = 
			new HashMap<Integer,EnumStatoArchivistico>();

	static {
	    for(EnumStatoArchivistico s : EnumSet.allOf(EnumStatoArchivistico.class))
	         lookup.put(s.getCode(), s);
	}

	private int code;
	
	private EnumStatoArchivistico(int code) {
	    this.code = code;
	}
	
	/**
	 * 
	 * @return 
	-1 (undefined), 
	0 (generico),
	1 (generico_definitivo),
	2 (registrato),	
	3 (protocollato), 
	4 (classificato),
	5 (fascicolato),
	6 (in_archivio_di_deposito);
	 */
	public int getCode() { 
		return code; 
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public static EnumStatoArchivistico getCode(int code) { 
	    return lookup.get(code); 
	}
	
	
}





