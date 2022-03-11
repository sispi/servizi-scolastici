package it.kdm.sign.enums;

public enum FormatoFirma {
	XADES("XADES"), PADES("PADES"), CADES("CADES");
	
	private String formato;
	
	FormatoFirma( String formato ){
		this.formato = formato;
	}
	
	public String getFormato(){
		return formato;
	}
	
	public static FormatoFirma getEnum(String value) {
        for(FormatoFirma v : values() ){
            if( v.getFormato().equalsIgnoreCase(value) ) 
            	return v;
        }
        return null;
    }

}
