package it.kdm.docer.timbrodigitale.provider;


import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public final class FixedCertTruster implements X509TrustManager
{
	private X509Certificate known_server_cert;

	public void setTrustedCert(X509Certificate x509certificate)
	{
		known_server_cert = x509certificate;
	}

	public X509Certificate[] getAcceptedIssuers()
	{
		return null;
	}

	public void checkClientTrusted(X509Certificate ax509certificate[], String s)
	{
	}

	public void checkServerTrusted(X509Certificate ax509certificate[], String s)
	{
		boolean allowUnsafeRenegotiation = false;
		String propertyValueStr = System.getProperty("sun.security.ssl.allowUnsafeRenegotiation");
		if (propertyValueStr!=null && propertyValueStr.equalsIgnoreCase("TRUE")){
			allowUnsafeRenegotiation = true;
		}
		java.security.PublicKey publickey = ax509certificate[0].getPublicKey();
		if(!allowUnsafeRenegotiation && publickey != known_server_cert.getPublicKey())
			throw new IllegalArgumentException();
		else
			return;
	}
}
