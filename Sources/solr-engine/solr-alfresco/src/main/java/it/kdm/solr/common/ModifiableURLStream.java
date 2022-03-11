package it.kdm.solr.common;

import org.apache.solr.common.util.ContentStreamBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class ModifiableURLStream extends ContentStreamBase
{
	public static Logger log = LoggerFactory.getLogger(ModifiableURLStream.class);
	
	private final URL url;
	
	private Map<String,String> headers;
	
	
	//private String authStringEnc;

	public ModifiableURLStream( URL url ) {
		this.url = url; 
		sourceInfo = "url";
		headers = new HashMap<>();
		
	}
	
	public void setHeader( String header, String val )
	{
		headers.put(header,val);
	}
	
	/*public AuthURLStream( URL url , String authStringEnc ) {
		this.url = url; 
		this.authStringEnc = authStringEnc;
		sourceInfo = "url";
		
		log.info("*************************** Authorization:"+authStringEnc);
		log.info("*************************** url:"+url.toString() );
	}*/

	@Override
	public InputStream getStream() throws IOException {
		
		URLConnection conn = this.url.openConnection();
		
		for (Map.Entry<String, String> entry : headers.entrySet())
		{
			conn.setRequestProperty(entry.getKey() , entry.getValue());
		}
		
		
		
		/*if (this.authStringEnc != null)
			conn.setRequestProperty("Authorization", authStringEnc);*/
	  
		contentType = conn.getContentType();
		name = url.toExternalForm();
		size = (long) conn.getContentLength();
		
		return conn.getInputStream();
	}
}