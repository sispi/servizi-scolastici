package it.kdm.docer.businesslogic.configuration.enums;


public enum EnumBLPropertyType {
 
	UNDEFINED, BOOLEAN, STRING, DATETIME, INTEGER, DECIMAL, DATE;

	public static final String strString = "xs:string";
	public static final String strDatetime = "xs:dateTime";
	public static final String strInteger = "xs:int";
	public static final String strDecimal = "xs:decimal";
	public static final String strBoolean = "xs:boolean";
	public static final String strDate = "xs:date";
	
	public static EnumBLPropertyType getEnum(String val) {
		

		if(val == null || val.equals(""))
			return STRING;		
		if(val.equalsIgnoreCase(strString))
			return STRING;
		if(val.equalsIgnoreCase(strDatetime))
			return DATETIME;
		if(val.equalsIgnoreCase(strInteger))
			return INTEGER;
		if(val.equalsIgnoreCase(strDecimal))
			return DECIMAL;
		if(val.equalsIgnoreCase(strBoolean))
			return BOOLEAN;
		if(val.equalsIgnoreCase(strDate))
			return DATE;
		return UNDEFINED;
	}

}
