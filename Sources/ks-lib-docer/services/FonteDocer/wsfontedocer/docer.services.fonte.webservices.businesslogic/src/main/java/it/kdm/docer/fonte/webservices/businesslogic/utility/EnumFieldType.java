package it.kdm.docer.fonte.webservices.businesslogic.utility;

public enum EnumFieldType {

    CODICE, DESCRIZIONE, GENERIC;

    public static final String strCODICE = "CODICE";
	public static final String strDESCRIZIONE = "DESCRIZIONE";
	
	public static EnumFieldType getEnum(String val) {
				
		if(val.toUpperCase().equals(strCODICE))
			return CODICE;
		if(val.toUpperCase().equals(strDESCRIZIONE))
			return DESCRIZIONE;
		
		return GENERIC;
	}
}
