package it.kdm.sign.enums;

public enum TipoFile {
	
	PDF("PDF"), DOC("DOC"), DOCX("DOCX"), XML("XML");
	
	private String tipoFile;
	
	private TipoFile( String tipoFile ) {
		this.tipoFile = tipoFile;
	}
	
	public String getTipoFile(){
		return tipoFile;
	}
	
	public static TipoFile getEnum(String value) {
        for(TipoFile v : values() ){
            if( v.getTipoFile().equalsIgnoreCase(value) ) 
            	return v;
        }
        return null;
    }
}
