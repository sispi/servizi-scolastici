package com.intesigroup.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Stub;
import org.apache.axis2.java.security.SSLProtocolSocketFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * 
 * @author Daniele Ribaudo
 *
 * @param <T>
 */
public class BaseService<T extends Stub> {
	
	private T stub;
	private String p12Path;
	private String p12Password;

	/**
	 * 
	 * @param stub
	 * @param p12Path
	 * @param p12Password
	 * @throws Exception
	 */
	public BaseService(T stub, String p12Path, String p12Password) throws Exception {
		this.stub = stub;
		this.p12Path = p12Path;
		this.p12Password = p12Password;
		
		init();
	}
	
	private void init() throws Exception {
		
    	KeyStore keyStore = KeyStore.getInstance("pkcs12");
    	
    	FileInputStream keyInput = new FileInputStream(p12Path);
		keyStore.load(keyInput, p12Password.toCharArray());

		try	{
			keyInput.close();
		}
		catch (IOException e) {
        	//ignore exception
		}

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keyStore, p12Password.toCharArray());
		
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(keyManagerFactory.getKeyManagers(), null, null);
		
		SSLProtocolSocketFactory sslProtocolSocketFactory = new SSLProtocolSocketFactory(context);
		this.stub._getServiceClient().getOptions().setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER, 
				new Protocol("https", (ProtocolSocketFactory)sslProtocolSocketFactory, 443));
	}
	
	public T getStub() {
		return stub;
	}
	
	public void cleanupStub() throws AxisFault {
		stub.cleanup();
		stub._getServiceClient().cleanup();
	}
}