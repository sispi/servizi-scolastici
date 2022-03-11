package it.kdm.docer.timbrodigitale.provider;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.timbrodigitale.sdk.IProvider;
import it.kdm.docer.timbrodigitale.sdk.ImageFormat;
import it.kdm.docer.timbrodigitale.sdk.ProviderException;
import org.mozilla.jss.util.Base64OutputStream;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class SecureEdgeProvider implements IProvider {

	private Properties conf;
	private static Properties errorCodes;

	static {
		try {
			errorCodes= ConfigurationUtils.loadProperties("ErrorCode.properties");
		} catch (IOException e) {
			e.printStackTrace();
			errorCodes = null;
		}

	}

	private String client_jks_pwd;
	private String client_jks_fname;
	private KeyStore cli_keyStore;


	public void setConfiguration(Properties conf) throws ProviderException {
		this.conf=conf;
	}

	public Properties getConfiguration() throws ProviderException {
		return this.conf;
	}

	public byte[] getTimbro(byte[] data,ImageFormat imgFormat,int imgMaxH, int imgMaxW, int imgDPI, Map<String,String> params) throws ProviderException {
		try {
			String pathCerSer=conf.getProperty("pathCerSer");
			String pwdCerSer=conf.getProperty("pwdCerSer");
			String nameCerSer=conf.getProperty("nameCerSer");
			String pathCerClient=conf.getProperty("pathCerClient");
			String pwdCerClient=conf.getProperty("pwdCerClient");
			String urlServer=conf.getProperty("urlServer");
			int portaServer=Integer.parseInt(conf.getProperty("portaServer"));
			String pathServer=conf.getProperty("pathServer_nossl");
			if(pathServer==null)
				pathServer=conf.getProperty("pathServer");

			if(params==null){
				params = new HashMap<String,String>();
			}

			//configurazione da utilizzare
			String defaultCfg="cfg.txt2";

			/*formato dell'immagine del codice
			JPG=8
			GIF=1
			PBM=2
			PNG=7
			/TIFF=10
			*/
			String imageFormat="7";
			if(imgFormat!=null && !imgFormat.equals(ImageFormat.UNKNOWN))
				imageFormat= String.valueOf(imgFormat.getCode());

			//altezza massima (mm) occupata dal codice
			String PeS_IMG_MAXH ="200";
			if(imgMaxH>-1){
				PeS_IMG_MAXH = String.valueOf(imgMaxH);
			}
			//larghezza massima (mm) occupata dal codice
			String PeS_IMG_MAXW="90";
			if(imgMaxW>-1){
				PeS_IMG_MAXW = String.valueOf(imgMaxW);
			}
			//numero di punti per pollice per il quale l'immagine e' stata creata
			String Img_DPI="500";
			if(imgDPI>-1){
				Img_DPI = String.valueOf(imgDPI);
			}
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(bOut);
			Base64OutputStream out = new Base64OutputStream(ps);
			out.write(data);
			out.flush();
			String doc= URLEncoder.encode(bOut.toString(), "UTF-8");
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			System.setProperty("java.protocol.handler.pkgs", "javax.net.ssl");

			//Security.addProvider(new Provider());
			try
			{
				MyHostVerifier hv =new MyHostVerifier();
				HttpsURLConnection.setDefaultHostnameVerifier(hv);
			}
			catch(Exception exception) { }
			// è stato commentato perché non server il check client del certificato server trusted
//            FixedCertTruster afixedcerttruster[] = new FixedCertTruster[1];
//            afixedcerttruster[0] = new FixedCertTruster();
//            X509Certificate known_server_cert = CertFromJKS(pathCerSer, pwdCerSer, nameCerSer);

//            afixedcerttruster[0].setTrustedCert(known_server_cert);
			SSLContext sslcontext = SSLContext.getInstance("SSL");
			KeyManagerFactory keymanagerfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			retrieveClientStuff(pathCerClient, pwdCerClient);
			keymanagerfactory.init(cli_keyStore, client_jks_pwd.toCharArray());
			javax.net.ssl.KeyManager akeymanager[] = keymanagerfactory.getKeyManagers();

			//***************************************
			//bypass trusted server (controllo client)
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
			};
			//***************************************

//            sslcontext.init(akeymanager, afixedcerttruster, null);
			sslcontext.init(akeymanager, trustAllCerts, null);

			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			URL url = new URL("https", urlServer, portaServer, pathServer);
			HttpsURLConnection httpsurlconnection = (HttpsURLConnection)url.openConnection();

           /* if(!params.containsKey("cfg")){
                params.put("cfg", defaultCfg);
            }*/
			//qualora è presente il parametro di configurazione conf nel file secureedge.properties
			if(conf.containsKey("cfg")){
				params.put("cfg", (String)conf.get("cfg"));
			}else{
				params.put("cfg", defaultCfg);
			}
			params.put("PeS_image_format", imageFormat);
			params.put("PeS_IMG_MAXHmm", PeS_IMG_MAXH);
			params.put("PeS_IMG_MAXWmm", PeS_IMG_MAXW);
			params.put("Img_DPI", Img_DPI);
			params.put("data", doc);

			//String urlPostParameters = (new StringBuffer()).append("cfg=").append(cfg).append("&PeS_image_format=").append(imageFormat).append("&PeS_IMG_MAXHmm=").append(PeS_IMG_MAXH).append("&PeS_IMG_MAXWmm=").append(PeS_IMG_MAXW).append("&Img_DPI").append(Img_DPI).append("&data=").append(doc).toString();

			String urlPostParameters = buildUrlPostParameters(params);

			httpsurlconnection.setDoInput(true);
			httpsurlconnection.setDoOutput(true);
			httpsurlconnection.setUseCaches(false);
			httpsurlconnection.setRequestProperty("Content-Type", "text/xml");
			httpsurlconnection.setAllowUserInteraction(true);
			httpsurlconnection.setRequestMethod("POST");
			if(!urlPostParameters.equals(""))
			{
				httpsurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				httpsurlconnection.setRequestProperty("Content-Length", (new StringBuffer()).append("").append(Integer.toString(urlPostParameters.getBytes().length)).toString());
				DataOutputStream dataoutputstream = new DataOutputStream(httpsurlconnection.getOutputStream());
				dataoutputstream.writeBytes(urlPostParameters);
				dataoutputstream.flush();
				dataoutputstream.close();
			}
			httpsurlconnection.setInstanceFollowRedirects(true);
			httpsurlconnection.connect();
			InputStream inputstream = httpsurlconnection.getInputStream();
			int c;
			while ((c = inputstream.read()) != -1) {
				byteArrayOutputStream.write(c);
			}
			inputstream.close();
			if(httpsurlconnection.getContentType().indexOf("text/plain")>-1){
				String errorCode=byteArrayOutputStream.toString();
				throw new ProviderException("Il sistema ha generato il seguente codice di errore: " + errorCode + " " + getErrorCode(errorCode));
			}
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new ProviderException(e);
		}
	}

//	public byte[] getTimbro(byte[] data) throws ProviderException {
//		 return getTimbro(data,ImageFormat.JPG,200, 90, 500);
//	}

	private String buildUrlPostParameters(Map<String,String> parameters) {

		StringBuffer sb = new StringBuffer();

		for(String key : parameters.keySet()){
			sb.append(key+"="+parameters.get(key) +"&");
		}

		return sb.toString().replaceAll("\\&$", "");
	}

	private String getErrorCode(String cod) {
		if (errorCodes != null) {
			String codeMessage = errorCodes.getProperty(cod);

			int i = 0;
			while (codeMessage == null && i < cod.length()) {
				cod = cod.replaceAll("([0-9])(x*)$", "x$2");
				codeMessage = errorCodes.getProperty(cod);
				i++;
			}
			return codeMessage;
		} else {
			return "";
		}
	}

	private X509Certificate CertFromJKS(String s, String s1, String s2)throws Exception
	{
		try{
			KeyStore keystore;
			FileInputStream fileinputstream = new FileInputStream(s);
			keystore = KeyStore.getInstance("JKS");
			keystore.load(fileinputstream, s1.toCharArray());
			fileinputstream.close();
			return (X509Certificate)(X509Certificate)keystore.getCertificate(s2);
		}catch(Exception e){
			e.printStackTrace();
			//return null;
			throw new Exception("Impossibile verificare il certificato " + s);
		}

	}

	private void retrieveClientStuff(String s, String s1)throws Exception
	{
		client_jks_fname = s;
		client_jks_pwd = s1;
		cli_keyStore = JKSKeyStore(client_jks_fname, client_jks_pwd);
		if(cli_keyStore == null)
		{
			System.out.println("ERROR: could not acquire keystore");
			//return;
			throw new Exception("Impossibile verificare il certificato client " + s);
		} else
		{
			return;
		}
	}

	private static KeyStore JKSKeyStore(String s, String s1)
	{
		try{
			KeyStore keystore;
			FileInputStream fileinputstream = new FileInputStream(s);
			try{
				keystore = KeyStore.getInstance("JKS");
				keystore.load(fileinputstream, s1.toCharArray());
			}catch(Exception e){
				fileinputstream = new FileInputStream(s);
				keystore=null;
				keystore = KeyStore.getInstance("PKCS12");
				keystore.load(fileinputstream, s1.toCharArray());
			}
			fileinputstream.close();
			return keystore;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}

	}

}

class MyHostVerifier implements javax.net.ssl.HostnameVerifier{

	public boolean verify(String urlHostName, String certHostName){
		System.out.println("urlHostName= " + urlHostName + "certHostName= " + certHostName);
		return true;
	}

	public boolean verify(String urlHost, SSLSession sslSession){
		return true;
	}

}
