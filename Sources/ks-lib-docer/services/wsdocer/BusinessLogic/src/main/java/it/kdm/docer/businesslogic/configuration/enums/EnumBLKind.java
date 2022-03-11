package it.kdm.docer.businesslogic.configuration.enums;

public enum EnumBLKind {

	UNDEFINED, FILE, PAPER, ANAGRAFICA, CUSTOM;

	private static final String strFILE = "FILE";
	private static final String strPAPER = "PAPER";
	private static final String strANAGRAFICA = "ANAGRAFICA";
	private static final String strCUSTOM = "CUSTOM";
	
	public static EnumBLKind getEnum(String val) {
				
		if(val.equalsIgnoreCase(strFILE))
			return FILE;
		if(val.toUpperCase().equals(strPAPER))
			return PAPER;
		if(val.toUpperCase().equals(strANAGRAFICA))
			return ANAGRAFICA;		
		if(val.toUpperCase().equals(strCUSTOM))
			return CUSTOM;
		
		return UNDEFINED;
	}
}
