package it.kdm.docer.alfresco.provider;

import it.kdm.docer.sdk.exceptions.DocerException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

public class ThreadFascicolazione implements Runnable {

    private String name = null;
    private String threadName = null;
    private String ALFRESCO_END_POINT_ADDRESS = null;
    private String ADMIN_USERID = null;
    private String alf_ticket = null;
    private String tomoveUUID = null;
    private String newParentUUID = null;
    private Boolean inherits = null;
    private static final String UNAUTH_SESSION_ERROR = "ticket non valido o utente non autorizzato";

    private boolean documento_eredita_acl_fascicolo = false;
    private boolean documento_eredita_acl_folder = false;
    
    public ThreadFascicolazione(String docId, String name, String ALFRESCO_END_POINT_ADDRESS, String ADMIN_USERID, String alf_ticket, String tomoveUUID, String newParentUUID, Boolean inherits, boolean documento_eredita_acl_fascicolo, boolean documento_eredita_acl_folder) {

	// super(docId);
	this.name = name;
	threadName = docId;
	this.ALFRESCO_END_POINT_ADDRESS = ALFRESCO_END_POINT_ADDRESS;
	this.ADMIN_USERID = ADMIN_USERID;
	this.alf_ticket = alf_ticket;
	this.tomoveUUID = tomoveUUID;
	this.newParentUUID = newParentUUID;
	this.inherits = inherits;
	
	this.documento_eredita_acl_fascicolo = documento_eredita_acl_fascicolo;
	this.documento_eredita_acl_folder = documento_eredita_acl_folder;
	    
    }

    public void run() {
	try {
	    moveToSpaceRunasAdmin();
	    org.slf4j.LoggerFactory.getLogger(ThreadFascicolazione.class).debug("ThreadFascicolazione: docId " + getName() + " spostato nel Fascicolo " + newParentUUID);
	}
	catch (DocerException e) {
	    org.slf4j.LoggerFactory.getLogger(ThreadFascicolazione.class).error("ThreadFascicolazione Exception: docId " + getName() + ": " + e.getMessage());
	}

    }

    private String getName() {
	return this.threadName;
    }

    private String combineUrl(String url1, String url2) {
	url1 = url1.replaceAll("/+$", "");
	url2 = url2.replaceAll("^/+", "");
	String combiUrl = url1 + "/" + url2;
	return combiUrl;
    }

    private void moveToSpaceRunasAdmin() throws DocerException {

	InputStream responseStream = null;
	PostMethod method = null;
	HttpClient httpClient = null;
	String url = null;

	try {

	    url = combineUrl(ALFRESCO_END_POINT_ADDRESS.replaceAll("/?api/?$", ""), "/service/kdm/runas");

	    httpClient = new HttpClient();

	    method = new PostMethod(url);

	    method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	    method.addRequestHeader("Connection", "Keep-Alive");

	    List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();

	    NameValuePair nv1 = new NameValuePair();
	    nv1.setName("alf_ticket");
	    nv1.setValue(URLEncoder.encode(alf_ticket, "UTF-8"));
	    parametersBody.add(nv1);

	    NameValuePair nvop = new NameValuePair();
	    nvop.setName("operation");
	    nvop.setValue("move");
	    parametersBody.add(nvop);

	    NameValuePair nv2 = new NameValuePair();
	    nv2.setName("runas");
	    nv2.setValue(String.valueOf(ADMIN_USERID));
	    parametersBody.add(nv2);

	    NameValuePair nv3 = new NameValuePair();
	    nv3.setName("adminuid");
	    nv3.setValue(ADMIN_USERID);
	    parametersBody.add(nv3);

	    NameValuePair nv4 = new NameValuePair();
	    nv4.setName("nodeuuid");
	    nv4.setValue(tomoveUUID);
	    parametersBody.add(nv4);

	    NameValuePair nv5 = new NameValuePair();
	    nv5.setName("spaceuuid");
	    nv5.setValue(newParentUUID);
	    parametersBody.add(nv5);

	    NameValuePair nv6 = new NameValuePair();
	    nv6.setName("newname");
	    nv6.setValue(name);
	    parametersBody.add(nv6);
	    
	    if(inherits!=null){
		NameValuePair nvInherits = new NameValuePair();
		nvInherits.setName("inherits");
		nvInherits.setValue(Boolean.toString(inherits));
		parametersBody.add(nvInherits);		
	    }
	    
	    NameValuePair nv7 = new NameValuePair();
	    nv7.setName("documento_eredita_acl_fascicolo");
	    nv7.setValue(String.valueOf(documento_eredita_acl_fascicolo));
	    parametersBody.add(nv7);
	    
	    NameValuePair nv8 = new NameValuePair();
	    nv8.setName("documento_eredita_acl_folder");
	    nv8.setValue(String.valueOf(documento_eredita_acl_folder));
	    parametersBody.add(nv8);
	    
	    method.setRequestBody(parametersBody.toArray(new NameValuePair[0]));

	    int resCode = httpClient.executeMethod(method);

	    if (resCode != 200) {
		// String responseString = method.getResponseBodyAsString();
		InputStream in = method.getResponseBodyAsStream();
		String encoding = method.getResponseCharSet();
		encoding = encoding == null ? "UTF-8" : encoding;
		String responseString = IOUtils.toString(in, encoding);

		int arg0 = responseString.indexOf("InizioErrore-") + 13;
		int arg1 = responseString.indexOf("-FineErrore");

		if (arg1 > arg0)
		    throw new Exception("errore http: " + responseString.substring(arg0, arg1));

		if (resCode == 401)
		    throw new Exception(UNAUTH_SESSION_ERROR);

		throw new Exception("errore http: " + resCode);
	    }

	}
	catch (Exception e) {
	    throw new DocerException("moveToSpaceRunasAdmin: " + e.getMessage());
	}
	finally {
	    try {
		if (responseStream != null)
		    responseStream.close();
	    }
	    catch (IOException e) {

	    }

	    if (method != null) {
		method.releaseConnection();
	    }
	}

    }

}
