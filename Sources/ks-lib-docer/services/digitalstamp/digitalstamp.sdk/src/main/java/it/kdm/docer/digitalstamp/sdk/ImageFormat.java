package it.kdm.docer.digitalstamp.sdk;


import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ImageFormat {
	/*formato dell'immagine del codice
	JPG=8
	GIF=1
	PBM=2
	PNG=7
	/TIFF=10
	*/	

	UNKNOWN(-1), JPG(8), GIF(1), PBM(2), PNG(7), TIFF(10);


	private int code;	

	public int getCode() { 
		return code; 
	}
	
	public static ImageFormat getImageFormat(String formatExt) { 
	    
		if(formatExt==null)
			return JPG;
		if(formatExt.equals(""))
			return JPG;
		
		if(formatExt.equalsIgnoreCase("JPG"))
	    	return JPG;
	    
	    if(formatExt.equalsIgnoreCase("GIF"))
	    	return GIF;
	    
	    if(formatExt.equalsIgnoreCase("PBM"))
	    	return PBM;
	    
	    if(formatExt.equalsIgnoreCase("PNG"))
	    	return PNG;
	    
	    if(formatExt.equalsIgnoreCase("TIFF"))
	    	return TIFF;
	    
	    return UNKNOWN;
	} 
	
	public static ImageFormat getImageFormat(int code) { 
	    return lookup.get(code); 
	} 
	
	private static final Map<Integer,ImageFormat> lookup = 
		new HashMap<Integer,ImageFormat>();

	static {
	    for(ImageFormat s : EnumSet.allOf(ImageFormat.class))
	         lookup.put(s.getCode(), s);
	}
	
	
	private ImageFormat(int code) {
	    this.code = code;
	}
	
}
