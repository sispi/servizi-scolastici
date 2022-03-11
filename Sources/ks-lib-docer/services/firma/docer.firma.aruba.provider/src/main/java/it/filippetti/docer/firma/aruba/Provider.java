package it.filippetti.docer.firma.aruba;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.axis2.AxisFault;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.google.common.base.Strings;

import it.kdm.docer.clients.ArubaSignServiceServiceStub;
import it.kdm.docer.clients.TypeOfTransportNotImplementedException;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.firma.FirmaException;
import it.kdm.docer.firma.IProvider;
import it.kdm.docer.firma.model.DocumentResultInfo;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import it.kdm.sign.model.ResultInfo;

/**
 * Created by microchip on 17/10/18.
 */
public class Provider  implements IProvider {

    private String token;
    private ILoggedUserInfo currentUser;

    private String serviceUrl = "";
    private String paramHSM = "COSIGN";
    private String paramOtpAuth = "";
    private String paramCertID = "";
    private String paramSourceFolder = "";
    private String paramDestFolder = "";

    public Provider() throws IOException, ConfigurationException {
        initConfig();
    }

    @Override
    public String login(String username, String password, String codiceEnte) {
        return "tokenFirma";
    }

    @Override
    public void logout(String token) throws FirmaException {

    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void setCurrentUser(ILoggedUserInfo info) throws FirmaException {
        this.currentUser = info;
    }

    @Override
    public List<DocumentResultInfo> firmaRemota(String ticket, String alias, String pin, String tipo, String OTP, Collection<String> documenti, Map<String, String> opzioni) throws FirmaException {
        //se pades = solo doc pdf
        //se cades = tutti i tipi doc
        //se xades = solo doc xml
        String serviceTicket = "";
        ArubaSignServiceServiceStub.Auth identity = null;

        if (!isValidTipo(tipo)) {
            throw new FirmaException("Tipo firma non valido (validi: CADES, PADES, XADES");
        }

        try {

            identity = getServiceItendity(OTP,alias,pin);

            //apre la sessione con il servizio aruba
//            ArubaSignServiceServiceStub.OpensessionResponseE session = openServiceSession(identity);
//            serviceTicket = session.getOpensessionResponse().get_return();


            //firma i documenti
            ArubaSignServiceServiceStub.SignReturnV2[] signedDocs = signDocs(identity,tipo, serviceTicket,documenti,opzioni);

            List<DocumentResultInfo> resultList = convertResultList(signedDocs);

            return resultList;

        } catch (Exception e) {
            throw new FirmaException(e);
        } finally {
            //chiude la sessione con il servizio aruba se è aperta una sessione
            if (!"".equalsIgnoreCase(serviceTicket)) {
                //chiude la sessione con il servizio aruba
                ArubaSignServiceServiceStub.ClosesessionResponseE closeResponse = null;
                try {
                    closeResponse = closeServiceSession(identity, serviceTicket);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                String ret = closeResponse.getClosesessionResponse().get_return();
            }
        }

    }

    private List<DocumentResultInfo> convertResultList(ArubaSignServiceServiceStub.SignReturnV2[] resultList) {
        List<DocumentResultInfo> list = new ArrayList<DocumentResultInfo>();

        if (resultList==null) {
            return list;
        }

//        for (ArubaSignServiceServiceStub.SignReturnV2 item : resultList) {
        for (int i=0;i<resultList.length;i++) {
            ArubaSignServiceServiceStub.SignReturnV2 item = resultList[i];
            DocumentResultInfo dInfo = new DocumentResultInfo();
            ResultInfo rInfo = new ResultInfo();
            rInfo.setInfo(item.getStatus());
            rInfo.setStatus(extractErrorCode(item.getReturn_code()));
            dInfo.setResultInfo(rInfo);
            dInfo.setFileName(extractFileName(item.getDstPath()));
//            dInfo.setFileName(item.getDstPath());

            list.add(dInfo);
        }

        return list;
    }

    private int extractErrorCode(String errorCode) {
        //estrae il codice di errore dalla stringa ritornata dal servizio
        String pattern = "([0-9].*)";
        int ret = 0;
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(errorCode);
        if (m.find( ))
            ret = Integer.parseInt(m.group(0));

        return ret;
    }

    private String extractFileName(String filePath) {

        if (filePath==null)
            return "";

        File f = new File(filePath);
        return f.getName();
    }

    private String extractFileExtension(String filePath) {
        String ext1 = FilenameUtils.getExtension(filePath);
        return ext1;
    }

    @Override
    public List<DocumentResultInfo> firmaAutomatica(String ticket, String alias, String pin, String tipo, Collection<String> documenti, Map<String, String> opzioni) throws FirmaException {
        throw new FirmaException("Tipo firmaAutomatica non supportata.");
    }

    @Override
    public String requestOTP(String alias, String pin) throws FirmaException {
        throw new FirmaException("Richiesta OTP non supportata.");
    }

    private boolean isValidTipo(String tipo) {
        return tipo.equals("CADES") || tipo.equals("PADES") || tipo.equals("XADES");
    }

    private ArubaSignServiceServiceStub getService() throws AxisFault {
        ArubaSignServiceServiceStub service = new ArubaSignServiceServiceStub(this.serviceUrl);

        return service;
    }

    public void initConfig() throws IOException {
        Properties config = ConfigurationUtils.loadProperties("provider-aruba-config.properties");
        
        serviceUrl = config.getProperty("serviceUrl");
        paramHSM = config.getProperty("HSM");
        paramOtpAuth = config.getProperty("otpAuth");
        paramCertID = config.getProperty("certID");
        paramSourceFolder = config.getProperty("sourceFolder");
        paramDestFolder = config.getProperty("destFolder");
    }

    private ArubaSignServiceServiceStub.SignReturnV2[] signDocs(ArubaSignServiceServiceStub.Auth identity, String tipo, String serviceTicket,  Collection<String> documenti, Map<String, String> opzioni) throws RemoteException, TypeOfTransportNotImplementedException, FirmaException {
        /*
        pkcs7SignV2_multiple Apposizione multipla di firme CAdES (-BES, -T) ad altro
        pdfsignatureV2_multiple Apposizione multipla di firme PAdES (-BES, -T) ad un file pdf
        xmlsignature_multiple Apposizione multipla di firme XAdES (-BES, -T) ad un file xml
         */

        //switch dell'otp per la firma (che è diversa)
        if (opzioni!=null && opzioni.containsKey("otpFirma")) {
            identity.setOtpPwd(opzioni.get("otpFirma"));
        }

        //TODO: switch su tipo firma (CADES,PADES,XADES)
        if ("CADES".equalsIgnoreCase(tipo)) {
            return signCADES(identity,serviceTicket,documenti,opzioni);
        } else if ("PADES".equalsIgnoreCase(tipo)) {
            return signPADES(identity, serviceTicket, documenti, opzioni);
        } else if ("XADES".equalsIgnoreCase(tipo)) {
            return signXADES(identity, serviceTicket, documenti, opzioni);
        } else {
            throw new FirmaException("Tipo firma non valido (validi: CADES, PADES, XADES");
        }
    }

    private ArubaSignServiceServiceStub.SignReturnV2[] signPADES(ArubaSignServiceServiceStub.Auth identity, String serviceTicket, Collection<String> documenti, Map<String, String> opzioni) throws RemoteException, TypeOfTransportNotImplementedException, FirmaException {
         /*
        pdfsignatureV2_multiple Apposizione multipla di firme PAdES (-BES, -T) ad un file pdf
         */
        ArubaSignServiceServiceStub.PdfsignatureV2_multiple signer = new ArubaSignServiceServiceStub.PdfsignatureV2_multiple();

        signer.setIdentity(identity);

//        List<ArubaSignServiceServiceStub.SignRequestV2> params = new ArrayList<ArubaSignServiceServiceStub.SignRequestV2>();
//        ArubaSignServiceServiceStub.SignRequestV2 param = null;
        ArubaSignServiceServiceStub.SignRequestV2[] params = new ArubaSignServiceServiceStub.SignRequestV2[documenti.size()];


        int i = 0;
        Map<Integer,String> mapFiles = new HashMap<Integer, String>();

        //ciclo er ogni file da firmare
        for (String filePath : documenti) {
            String ext = extractFileExtension(filePath);
            if (!"PDF".equalsIgnoreCase(ext))
                throw new FirmaException("Per poter firmare, i document devono essere in formato pdf. Impossibile procedere con la firma.");

            Path currentPath = Paths.get(paramSourceFolder);
            filePath = Paths.get(currentPath.toString(), filePath).toString();

            ArubaSignServiceServiceStub.SignRequestV2 param = new ArubaSignServiceServiceStub.SignRequestV2();
            param.setIdentity(identity);
            param.setStream(getFileDataHandler(filePath));
            param.setCertID(paramCertID);
//            param.setSession_id(serviceTicket);
            param.setRequiredmark(false);
            param.setTransport(ArubaSignServiceServiceStub.TypeTransport.STREAM);
//            signer.addSignRequestV2(param);

            params[i] = param;
            mapFiles.put(i++,filePath);
        }

        signer.setSignRequestV2(params);

        ArubaSignServiceServiceStub.PdfsignatureV2_multipleE signerE = new ArubaSignServiceServiceStub.PdfsignatureV2_multipleE();
        signerE.setPdfsignatureV2_multiple(signer);
        ArubaSignServiceServiceStub service = getService();
        ArubaSignServiceServiceStub.PdfsignatureV2_multipleResponseE response = service.pdfsignatureV2_multiple(signerE);

        ArubaSignServiceServiceStub.SignReturnV2[] signedDocs = response.getPdfsignatureV2_multipleResponse().get_return().getReturn_signs();

        String status = response.getPdfsignatureV2_multipleResponse().get_return().getStatus();

        if (!"OK".equalsIgnoreCase(status)) {
            String errorCode = response.getPdfsignatureV2_multipleResponse().get_return().getReturn_code();
            throw new FirmaException(errorCode+" Errore durante il processo di firma.");
        }

        try {
            signedDocs = finalizeResultList(mapFiles, signedDocs, null);
        } catch (IOException e) {
            throw new FirmaException("Errore durante la scrittura dei file di output su filesystem. Controllare lo stacktrace per capire la problematica.",e);
        }

        return signedDocs;
    }

    private ArubaSignServiceServiceStub.SignReturnV2[] finalizeResultList(Map<Integer, String> mapFiles, ArubaSignServiceServiceStub.SignReturnV2[] signedDocs, String newExtension) throws IOException {

        for(int i=0;i<signedDocs.length;i++) {
            String originalFileName = extractFileName(mapFiles.get(i));
            if (!Strings.isNullOrEmpty(newExtension))
                originalFileName+=newExtension;
            //scrive lo stream su filesystem
            File file = new File(paramDestFolder, originalFileName);
            String destFileName = file.getPath();
//            FileOutputStream toFile = new FileOutputStream(file);
//            InputStream is = ((DataHandlerExt)signedDocs[i].getStream()).readOnce();
            InputStream is = signedDocs[i].getStream().getInputStream();
            FileUtils.copyInputStreamToFile(is,file);
//            signedDocs[i].getStream().writeTo(toFile);
//            toFile.flush();
//            toFile.close();
            //setta il filename di output
            signedDocs[i].setDstPath(destFileName);
        }

        return signedDocs;
    }

    private ArubaSignServiceServiceStub.SignReturnV2[] signXADES(ArubaSignServiceServiceStub.Auth identity, String serviceTicket, Collection<String> documenti, Map<String, String> opzioni) throws RemoteException, TypeOfTransportNotImplementedException, FirmaException {
         /*
        xmlsignature_multiple Apposizione multipla di firme XAdES (-BES, -T) ad un file xml
         */
        ArubaSignServiceServiceStub.Xmlsignature_multiple signer = new ArubaSignServiceServiceStub.Xmlsignature_multiple();

        signer.setIdentity(identity);

        ArubaSignServiceServiceStub.SignRequestV2[] params = new ArubaSignServiceServiceStub.SignRequestV2[documenti.size()];


        int i = 0;
        Map<Integer,String> mapFiles = new HashMap<Integer, String>();

        //ciclo er ogni file da firmare
        for (String filePath : documenti) {
            String ext = extractFileExtension(filePath);
            if (!"XML".equalsIgnoreCase(ext))
                throw new FirmaException("Per poter firmare, i document devono essere in formato xml. Impossibile procedere con la firma.");

            Path currentPath = Paths.get(paramSourceFolder);
            filePath = Paths.get(currentPath.toString(), filePath).toString();

            ArubaSignServiceServiceStub.SignRequestV2 param = new ArubaSignServiceServiceStub.SignRequestV2();
            param.setIdentity(identity);
            param.setStream(getFileDataHandler(filePath));
            param.setCertID(paramCertID);
//            param.setSession_id(serviceTicket);
            param.setRequiredmark(false);
            param.setTransport(ArubaSignServiceServiceStub.TypeTransport.STREAM);
//            signer.addSignRequestV2(param);

            params[i] = param;
            mapFiles.put(i++,filePath);
        }

        signer.setSignRequestV2(params);

        ArubaSignServiceServiceStub.Xmlsignature_multipleE signerE = new ArubaSignServiceServiceStub.Xmlsignature_multipleE();
        signerE.setXmlsignature_multiple(signer);
        ArubaSignServiceServiceStub service = getService();
        ArubaSignServiceServiceStub.Xmlsignature_multipleResponseE response = service.xmlsignature_multiple(signerE);

        ArubaSignServiceServiceStub.SignReturnV2[] signedDocs = response.getXmlsignature_multipleResponse().get_return().getReturn_signs();

        String status = response.getXmlsignature_multipleResponse().get_return().getStatus();

        if (!"OK".equalsIgnoreCase(status)) {
            String errorCode = response.getXmlsignature_multipleResponse().get_return().getReturn_code();
            throw new FirmaException(errorCode+" Errore durante il processo di firma.");
        }

        try {
            signedDocs = finalizeResultList(mapFiles, signedDocs, null);
        } catch (IOException e) {
            throw new FirmaException("Errore durante la scrittura dei file di output su filesystem. Controllare lo stacktrace per capire la problematica.",e);
        }

        return signedDocs;
    }

    private ArubaSignServiceServiceStub.SignReturnV2[] signCADES(ArubaSignServiceServiceStub.Auth identity, String serviceTicket, Collection<String> documenti, Map<String, String> opzioni) throws RemoteException, TypeOfTransportNotImplementedException, FirmaException {
         /*
        pkcs7SignV2_multiple Apposizione multipla di firme CAdES (-BES, -T) ad altro
         */
        ArubaSignServiceServiceStub.Pkcs7SignV2_multiple signer = new ArubaSignServiceServiceStub.Pkcs7SignV2_multiple();

        signer.setIdentity(identity);

        ArubaSignServiceServiceStub.SignRequestV2[] params = new ArubaSignServiceServiceStub.SignRequestV2[documenti.size()];


        int i = 0;
        Map<Integer,String> mapFiles = new HashMap<Integer, String>();

        //ciclo er ogni file da firmare
        for (String filePath : documenti) {
            Path currentPath = Paths.get(paramSourceFolder);
            filePath = Paths.get(currentPath.toString(), filePath).toString();
            ArubaSignServiceServiceStub.SignRequestV2 param = new ArubaSignServiceServiceStub.SignRequestV2();
            param.setIdentity(identity);
            param.setStream(getFileDataHandler(filePath));
            param.setCertID(paramCertID);
//            param.setSession_id(serviceTicket);
            param.setRequiredmark(false);
            param.setTransport(ArubaSignServiceServiceStub.TypeTransport.STREAM);
//            signer.addSignRequestV2(param);

            params[i] = param;
            mapFiles.put(i++,filePath);
        }

        signer.setSignRequestV2(params);

        signer.setDetached(false);
        ArubaSignServiceServiceStub.Pkcs7SignV2_multipleE signerE = new ArubaSignServiceServiceStub.Pkcs7SignV2_multipleE();
        signerE.setPkcs7SignV2_multiple(signer);
        ArubaSignServiceServiceStub service = getService();

        ArubaSignServiceServiceStub.Pkcs7SignV2_multipleResponseE response = service.pkcs7SignV2_multiple(signerE);

        ArubaSignServiceServiceStub.SignReturnV2[] signedDocs = response.getPkcs7SignV2_multipleResponse().get_return().getReturn_signs();
        String status = response.getPkcs7SignV2_multipleResponse().get_return().getStatus();

        if (!"OK".equalsIgnoreCase(status)) {
            String errorCode = response.getPkcs7SignV2_multipleResponse().get_return().getReturn_code();
            throw new FirmaException(errorCode+" Errore durante il processo di firma.");
        }

        try {
            signedDocs = finalizeResultList(mapFiles, signedDocs, ".p7m");
        } catch (IOException e) {
            throw new FirmaException("Errore durante la scrittura dei file di output su filesystem. Controllare lo stacktrace per capire la problematica.",e);
        }

        return signedDocs;
    }

    private DataHandler getFileDataHandler(String filePath) {
        FileDataSource fileDataSource = new javax.activation.FileDataSource(filePath);
        DataHandler dataHandler = new DataHandler(fileDataSource);

        return dataHandler;
    }

    private ArubaSignServiceServiceStub.Auth getServiceItendity(String otp, String userName, String pwd) {
        ArubaSignServiceServiceStub.Auth identity = new ArubaSignServiceServiceStub.Auth();

        identity.setOtpPwd(otp);
        //TypeHSM = Stringa contenente il tipo di HSM.TypeHSM Va sempre valorizzata con la stringa 'COSIGN'
        identity.setTypeHSM(paramHSM);

        //TypeOtpAuth
        // firma -> Nel caso in cui si stia utilizzando un'utenza di firma remota.
        // Nel caso in cui si stia utilizzando un'utenza di firma remota con dominio personalizzato è necessario specificare tale dominio (Es. frXXXX).
        // Nel caso in cui si stia utilizzando un'utenza di firma automatica è necessario specificare la stringa indicata in fase di installazione di ARSS.

        identity.setTypeOtpAuth(paramOtpAuth);
        identity.setUser(userName);
        identity.setUserPWD(pwd);

        return identity;
    }

    private ArubaSignServiceServiceStub.OpensessionResponseE openServiceSession(ArubaSignServiceServiceStub.Auth identity) throws RemoteException, FirmaException {
        ArubaSignServiceServiceStub service = getService();
        ArubaSignServiceServiceStub.OpensessionE os = new ArubaSignServiceServiceStub.OpensessionE();

        ArubaSignServiceServiceStub.Opensession session = new ArubaSignServiceServiceStub.Opensession();
        session.setIdentity(identity);
        os.setOpensession(session);

        ArubaSignServiceServiceStub.OpensessionResponseE response = service.opensession(os);

        String serviceTicket = response.getOpensessionResponse().get_return();

        //verificare ticket errore: KO-xxxxx
        if (serviceTicket.contains("KO-")) {
            throw new FirmaException(serviceTicket+" Impossibile aprire la sessione di Firma. Controllare le credenziali e riprovare.");
        }

        return response;
    }

    private ArubaSignServiceServiceStub.ClosesessionResponseE closeServiceSession(ArubaSignServiceServiceStub.Auth identity, String serviceTicket) throws RemoteException {
        ArubaSignServiceServiceStub service = getService();
        ArubaSignServiceServiceStub.ClosesessionE cs = new ArubaSignServiceServiceStub.ClosesessionE();

        ArubaSignServiceServiceStub.Closesession session = new ArubaSignServiceServiceStub.Closesession();
        session.setIdentity(identity);
        session.setSessionid(serviceTicket);
        cs.setClosesession(session);

        ArubaSignServiceServiceStub.ClosesessionResponseE response = service.closesession(cs);

        return response;
    }
}
