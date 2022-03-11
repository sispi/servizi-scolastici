package it.kdm.sign.enums;

public enum TipoFirma {
	
	REMOTA("REMOTA") , AUTOMATICA("AUTOMATICA");
	
	private String tipoFirma;
	
	TipoFirma( String tipoFirma) {
		this.tipoFirma = tipoFirma;
	}
	
	public String getTipoFirma(){
		return tipoFirma;
	}

	public static TipoFirma getEnum(String value) {
        for(TipoFirma v : values() ){
            if( v.getTipoFirma().equalsIgnoreCase(value) ) 
            	return v;
        }
        return null;
    }
}
