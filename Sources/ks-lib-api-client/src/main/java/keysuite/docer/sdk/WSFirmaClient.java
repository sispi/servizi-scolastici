package keysuite.docer.sdk;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.client.IFirma;
import keysuite.docer.client.NamedInputStream;
import keysuite.docer.client.verificafirma.VerificaFirmaDTO;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class WSFirmaClient implements IFirma {

    final static String collectionPath = "/firma";
    //final static String itemPath = collectionPath + "/{...}/";

    APIClient client;
    public WSFirmaClient(APIClient client){
        this.client = client;
    }

    @Override
    public URL[] firmaAutomatica(String alias, String pin, String tipo, URL... urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        List<Map> results = client.execute( collectionPath+"/firmaAutomatica", alias,pin,tipo,urls);
        URL[] outUrls = new URL[results.size()];
        for ( int i=0; i<results.size();i++){
            try {
                outUrls[i] = new URL( (String) results.get(i).get("result") );
            } catch (MalformedURLException e) {
                throw new KSRuntimeException(e);
            }
        }

        return outUrls;

    }

    @Override
    public URL[] firmaRemota(String alias, String pin, String tipo, String OTP, URL... urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        List<Map> results = client.execute( collectionPath+"/firmaRemota", OTP,alias,pin,tipo,urls);
        URL[] outUrls = new URL[results.size()];
        for ( int i=0; i<results.size();i++){
            try {
                outUrls[i] = new URL( (String) results.get(i).get("result") );
            } catch (MalformedURLException e) {
                throw new KSRuntimeException(e);
            }
        }

        return outUrls;
    }

    public URL[] print(String text, String style, URL... urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        List<Map> results = client.execute( collectionPath+"/print", style,text,urls);
        URL[] outUrls = new URL[results.size()];
        for ( int i=0; i<results.size();i++){
            try {
                outUrls[i] = new URL( (String) results.get(i).get("result") );
            } catch (MalformedURLException e) {
                throw new KSRuntimeException(e);
            }
        }

        return outUrls;
    }

    @Override
    public URL[] timestamp(String tipo, URL... urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        List<Map> results = client.execute( collectionPath+"/timestamp", tipo,urls);
        URL[] outUrls = new URL[results.size()];
        for ( int i=0; i<results.size();i++){
            try {
                outUrls[i] = new URL( (String) results.get(i).get("result") );
            } catch (MalformedURLException e) {
                throw new KSRuntimeException(e);
            }
        }

        return outUrls;
    }

    /*@Override
    public Documento[] firmaAutomaticaDocumenti(String alias, String pin, String tipo, boolean relate,URL[] urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return null;
    }

    @Override
    public Documento[] firmaRemotaDocumenti(String alias, String pin, String tipo, String OTP, boolean relate, URL[] urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return null;
    }*/

    @Override
    public void requestOTP(String alias, String pin) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

    }

    @Override
    public VerificaFirmaDTO[] verificaFirme(String verificationDate, String policyFile, URL... urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return ClientUtils.toClientBeans( (List) client.execute( collectionPath+"/verificaFirme#GET", policyFile,urls, verificationDate), VerificaFirmaDTO[].class );
    }

    public String report(String verificationDate, String policyFile, boolean bootStrap3, URL... urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Object result = client.execute( collectionPath+"/report", bootStrap3, policyFile,urls, verificationDate );
        return (String) result;
    }

    public InputStream pdfReport(String verificationDate, String policyFile, boolean detailed, URL url) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        InputStream stream = client.execute( collectionPath+"/pdfReport", detailed, policyFile,url, verificationDate );
        return stream;
    }
}
