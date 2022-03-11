package keysuite.docer.ws;

import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.firma.*;
//import it.kdm.doctoolkit.helper.InputStreamDataSource;
//import it.kdm.orchestratore.session.Session;
import it.kdm.docer.ws.WSTransformer;
import it.kdm.orchestratore.session.Session;
import keysuite.SessionUtils;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.*;
import keysuite.docer.interceptors.Logging;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Endpoint
@Logging(group = "WSFirma")
public class WSFirmaEndpoint {

    @Autowired(required = false)
    IFirma firmaService;

    @Autowired(required = false)
    IDocumenti documentiService;

    public WSFirmaEndpoint() {
    }

    protected BusinessLogic newBL(){
        try{
            return new BusinessLogic();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected FileServiceCommon newFS(String token){
        try {
            String username = Utils.extractTokenKey(token, "uid");
            String aoo = Utils.extractTokenKey(token, "aoo");
            FileServiceCommon fsCommon = new FileServiceCommon(aoo, username);
            return fsCommon;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private URL docToURL(String token, String docnum){
        try {
            return new URL(System.getProperty("docer.url")+"/documenti/"+docnum+"/file");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private URL fileToURL(String token, String fileName, byte[] bytes) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            NamedInputStream file = NamedInputStream.getNamedInputStream(is, fileName);
            FileServiceCommon fsc = newFS(token);
            String fileId = fsc.create(file, null);
            return new URL(System.getProperty("docer.url") + "/files/" + fileId);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private StreamDescriptor URLtoStream(String token,String original, URL url, String message, String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden, IOException {

        NamedInputStream nis = newFS(token).openURL(url,token);

        StreamDescriptor sd = WSTransformer.firmaFactory.createStreamDescriptor();

        byte[] bytes = IOUtils.toByteArray(nis.getStream());

        sd.setDataHandler(WSTransformer.firmaFactory.createStreamDescriptorDataHandler(bytes));
        sd.setByteSize(Integer.valueOf(bytes.length).longValue());
        if (nis.getName()!=null)
            sd.setName(WSTransformer.firmaFactory.createStreamDescriptorName(nis.getName()));
        if (original!=null)
            sd.setOriginalName(WSTransformer.firmaFactory.createStreamDescriptorOriginalName(original));
        if (docnum!=null)
            sd.setDocNum(WSTransformer.firmaFactory.createStreamDescriptorDocNum(docnum));
        if (message!=null)
            sd.setMessage(WSTransformer.firmaFactory.createStreamDescriptorMessage(message));

        return sd;
    }

    @PayloadRoot(namespace = "http://firma.docer.kdm.it", localPart = "firmaRemota")
    @ResponsePayload
    public FirmaRemotaResponse firmaRemota(@RequestPayload FirmaRemota request) throws WSFirmaException {

        String alias = request.getAlias().getValue();
        List<String> documenti = request.getDocumenti();
        String otp = request.getOTP().getValue();
        String pin = request.getPin().getValue();
        String tipo = request.getTipo().getValue();
        String token = request.getToken().getValue();

        String jwtToken = ClientCacheAuthUtils.getInstance().convertToJWTToken(token);
        Session.attach(jwtToken);
        SessionUtils.getRequest().setAttribute("authorization",jwtToken);


        try {
            List<URL> urls = new ArrayList<>();
            List<Documento> docs = new ArrayList<>();

            for( String docnum : documenti ){
                Documento d = documentiService.get(docnum);
                docs.add(d);
                urls.add(d.getURL());
            }

            URL[] res = firmaService.firmaRemota(alias,pin,tipo,otp,urls.toArray(new URL[0]));

            assert res.length == documenti.size();

            FirmaRemotaResponse response = WSTransformer.firmaFactory.createFirmaRemotaResponse();

            List<StreamDescriptor> streams = response.getReturn();

            for( int i=0; i<documenti.size(); i++){
                Documento d = docs.get(i);
                streams.add(URLtoStream(token,d.getDocname(),res[i],null,d.getDocnum()));
            }

            return response;

        } catch (Exception e) {
            throw new WSFirmaException(e);
        }


    }

    @PayloadRoot(namespace = "http://firma.docer.kdm.it", localPart = "firmaRemotaFile")
    @ResponsePayload
    public FirmaRemotaFileResponse firmaRemotaFile(@RequestPayload FirmaRemotaFile request) throws WSFirmaException {

        String alias = request.getAlias().getValue();
        List<KeyValuePairDF> files = request.getFile();
        String otp = request.getOTP().getValue();
        String pin = request.getPin().getValue();
        String tipo = request.getTipo().getValue();
        String token = request.getToken().getValue();

        String jwtToken = ClientCacheAuthUtils.getInstance().convertToJWTToken(token);
        Session.attach(jwtToken);
        SessionUtils.getRequest().setAttribute("authorization",jwtToken);

        try {
            List<URL> urls = new ArrayList<>();

            for( KeyValuePairDF file : files ){
                urls.add(fileToURL(token,file.getFileName().getValue(),file.getStream().getValue()));
            }

            URL[] res = firmaService.firmaRemota(alias,pin,tipo,otp,urls.toArray(new URL[0]));

            assert res.length == files.size();

            FirmaRemotaFileResponse response = WSTransformer.firmaFactory.createFirmaRemotaFileResponse();

            List<StreamDescriptor> streams = response.getReturn();

            for( int i=0; i<files.size(); i++){
                KeyValuePairDF f = files.get(i);
                streams.add(URLtoStream(token,f.getFileName().getValue(),res[i],null,null));
            }

            return response;

        } catch (Exception e) {
            throw new WSFirmaException(e);
        }

    }

    @PayloadRoot(namespace = "http://firma.docer.kdm.it", localPart = "firmaAutomatica")
    @ResponsePayload
    public FirmaAutomaticaResponse firmaAutomatica(@RequestPayload FirmaAutomatica request) throws WSFirmaException {

        String alias = request.getAlias().getValue();
        List<String> documenti = request.getDocumenti();
        String pin = request.getPin().getValue();
        String tipo = request.getTipo().getValue();
        String token = request.getToken().getValue();

        String jwtToken = ClientCacheAuthUtils.getInstance().convertToJWTToken(token);
        Session.attach(jwtToken);
        SessionUtils.getRequest().setAttribute("authorization",jwtToken);

        try {
            List<URL> urls = new ArrayList<>();
            List<Documento> docs = new ArrayList<>();

            for( String docnum : documenti ){
                Documento d = documentiService.get(docnum);
                docs.add(d);
                urls.add(d.getURL());
            }

            URL[] res = firmaService.firmaAutomatica(alias,pin,tipo,urls.toArray(new URL[0]));

            assert res.length == documenti.size();

            FirmaAutomaticaResponse response = WSTransformer.firmaFactory.createFirmaAutomaticaResponse();

            List<StreamDescriptor> streams = response.getReturn();

            for( int i=0; i<documenti.size(); i++){
                Documento d = docs.get(i);
                streams.add(URLtoStream(token,d.getDocname(),res[i],null,d.getDocnum()));
            }

            return response;

        } catch (Exception e) {
            throw new WSFirmaException(e);
        }
    }

    @PayloadRoot(namespace = "http://firma.docer.kdm.it", localPart = "firmaAutomaticaFile")
    @ResponsePayload
    public FirmaAutomaticaFileResponse firmaAutomaticaFile(@RequestPayload FirmaAutomaticaFile request) throws WSFirmaException {

        String alias = request.getAlias().getValue();
        List<KeyValuePairDF> files = request.getFile();
        String pin = request.getPin().getValue();
        String tipo = request.getTipo().getValue();
        String token = request.getToken().getValue();

        String jwtToken = ClientCacheAuthUtils.getInstance().convertToJWTToken(token);
        Session.attach(jwtToken);
        SessionUtils.getRequest().setAttribute("authorization",jwtToken);

        try {
            List<URL> urls = new ArrayList<>();

            for( KeyValuePairDF file : files ){
                urls.add(fileToURL(token,file.getFileName().getValue(),file.getStream().getValue()));
            }

            URL[] res = firmaService.firmaAutomatica(alias,pin,tipo,urls.toArray(new URL[0]));

            assert res.length == files.size();

            FirmaAutomaticaFileResponse response = WSTransformer.firmaFactory.createFirmaAutomaticaFileResponse();

            List<StreamDescriptor> streams = response.getReturn();

            for( int i=0; i<files.size(); i++){
                KeyValuePairDF f = files.get(i);
                streams.add(URLtoStream(token,f.getFileName().getValue(),res[i],null,null));
            }

            return response;

        } catch (Exception e) {
            throw new WSFirmaException(e);
        }
    }

    @PayloadRoot(namespace = "http://firma.docer.kdm.it", localPart = "requestOTP")
    @ResponsePayload
    public RequestOTPResponse requestOTP(@RequestPayload RequestOTP request) throws WSFirmaException {

        String alias = request.getAlias().getValue();
        String pin = request.getPin().getValue();
        String token = request.getToken().getValue();

        String jwtToken = ClientCacheAuthUtils.getInstance().convertToJWTToken(token);
        Session.attach(jwtToken);
        SessionUtils.getRequest().setAttribute("authorization",jwtToken);

        try {
            firmaService.requestOTP(alias,pin);
        } catch (Exception e) {
            throw new WSFirmaException(e);
        }

        RequestOTPResponse response = WSTransformer.firmaFactory.createRequestOTPResponse();

        return response;
    }

}
