/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.firma;

import com.google.common.base.Strings;
import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.DocerExceptionException;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.commons.docerservice.BaseService;
import it.kdm.docer.commons.docerservice.BaseServiceException;
import it.kdm.docer.firma.model.DocumentResultInfo;
import it.kdm.docer.firma.model.KeyValuePairDF;
import it.kdm.docer.firma.model.StreamDescriptor;
import it.kdm.docer.firma.utils.ConfigUtils;
import it.kdm.docer.sdk.classes.LoggedUserInfo;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import org.apache.axiom.om.OMElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.jaxen.JaxenException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyException;
import java.util.*;

/**
 * @author lorenzo
 */
public class BusinessLogic extends BaseService implements IBusinessLogic {

    Logger logger = org.slf4j.LoggerFactory.getLogger(BusinessLogic.class);

    private static Configurations CONFIGURATIONS = new Configurations();

    private DocerServicesStub documentale;
    private IProvider provider;
    private PropertiesConfiguration conf;
    private String tmpInputDirectory;
    private String tmpOutputDirectory;
//    private ConfigUtils conf;

    public BusinessLogic() throws IOException, XMLStreamException, JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException, ConfigurationException {
        documentale = ClientManager.INSTANCE.getDocerServicesClient();
//        conf = new ConfigUtils();
        initConfig();
    }

    public String login(String userId, String password, String codiceEnte) throws FirmaException {
        try {
            return baseLogin(userId, password, codiceEnte);
        } catch (BaseServiceException e) {
            throw new FirmaException(e);
        }
    }

    public String loginSSO(String saml, String codiceEnte) throws FirmaException {
        try {
            return baseLoginSSO(saml, codiceEnte);
        } catch (BaseServiceException e) {
            throw new FirmaException(e);
        }
    }

    public void logout(String token) throws FirmaException {
        if (provider != null) {
            provider.logout(token);
        }
    }

    @Override
    public StreamDescriptor[] firmaRemota(String ticket, String alias, String pin, String tipo, String OTP, KeyValuePairDF[] file) throws FirmaException {
        try {

//            IProvider provider = new Provider();
            IProvider provider = initProvider(ticket);

            Map<String, DocumentInfo> documenti = documenti(file);

            List<DocumentResultInfo> infos = provider.firmaRemota(ticket, alias, pin, tipo, OTP, documenti.keySet(), null);
            return orderResults(streamDescriptors(infos, documenti, tipo));

        } catch (Exception e) {
            throw new FirmaException(e);
        }
    }
    @Override
    public StreamDescriptor[] firmaRemota(String ticket, String alias, String pin, String tipo, String OTP, String[] docNum) throws FirmaException {
        try {

//            IProvider provider = new Provider();
            IProvider provider = initProvider(ticket);

            String docTicket = "calltype:internal|";
            docTicket += Utils.extractTokenKey(ticket, "documentale");

            Map<String, DocumentInfo> documenti = documenti(docTicket, docNum);

            List<DocumentResultInfo> infos = provider.firmaRemota(ticket, alias, pin, tipo, OTP, documenti.keySet(), null);
            return orderResults(streamDescriptors(infos, documenti,tipo));

        } catch (Exception e) {
            throw new FirmaException(e);
        }
    }

    @Override
    public StreamDescriptor[] firmaAutomatica(String ticket, String alias, String pin, String tipo, KeyValuePairDF[] file) throws FirmaException {
        try {

//            IProvider provider = new Provider();
            IProvider provider = initProvider(ticket);

            Map<String, DocumentInfo> documenti = documenti(file);

            List<DocumentResultInfo> infos = provider.firmaAutomatica(ticket, alias, pin, tipo, documenti.keySet(), null);
            return orderResults(streamDescriptors(infos, documenti,tipo));

        } catch (Exception e) {
            throw new FirmaException(e);
        }
    }
    @Override
    public StreamDescriptor[] firmaAutomatica(String ticket, String alias, String pin, String tipo, String[] docNum) throws FirmaException {
        try {

//            IProvider provider = new Provider();
            IProvider provider = initProvider(ticket);

            String docTicket = "calltype:internal|";
            docTicket += Utils.extractTokenKey(ticket, "documentale");

            Map<String, DocumentInfo> documenti = documenti(docTicket, docNum);

            List<DocumentResultInfo> infos = provider.firmaAutomatica(ticket, alias, pin, tipo, documenti.keySet(), null);
            return orderResults(streamDescriptors(infos, documenti,tipo));

        } catch (Exception e) {
            throw new FirmaException(e);
        }
    }

    @Override
    public String requestOTP(String ticket, String alias, String pin) throws FirmaException {
        try {
            IProvider provider = initProvider(ticket);

            return provider.requestOTP(alias, pin);
//            return new Provider().requestOTP(alias,pin);
        } catch (Exception e) {
            throw new FirmaException(e);
        }
    }

    private StreamDescriptor[] streamDescriptors(List<DocumentResultInfo> infos, Map<String, DocumentInfo> documenti, String tipo) {

        List<StreamDescriptor> ret = new ArrayList<StreamDescriptor>();

        for (DocumentResultInfo i : infos) {
            StreamDescriptor descriptor = new StreamDescriptor();

            String fileName = i.getFileName();
            //se è CADES elimina l'ultima l'estensione (.p7m) gli altri tipi mantengono l'estensione
            String key = fileName;
            if ("CADES".equalsIgnoreCase(tipo))
                key = fileName.replaceAll("(^[0-9]*_.*)(\\..*)$", "$1");

            DocumentInfo di = documenti.get(key);

            if (i.getResultInfo().getStatus() == 0) {
//                File f = new File(conf.getOutputDirectory(), i.getResultInfo().getInfo());
//                File f = new File(tmpOutputDirectory, i.getResultInfo().getInfo());
                File f = new File(tmpOutputDirectory, fileName);

                descriptor.setByteSize(f.length());

                /*gestione fileName*/
                String OutPutFileName=f.getName();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
                String nomeFileSenzaEstensione=org.apache.commons.io.FilenameUtils.getBaseName(OutPutFileName).split("\\.")[0];
                String numeroPrefissoDocumento=nomeFileSenzaEstensione.split("_")[0];
                String estensioneFileCorrente=OutPutFileName.replace(nomeFileSenzaEstensione,"");
                String composizioneNomeFileOutput=nomeFileSenzaEstensione+"-".concat(numeroPrefissoDocumento).concat( sdf.format(new java.util.Date())).concat(estensioneFileCorrente);
                /*fine gestione fileName*/

                descriptor.setName(composizioneNomeFileOutput.replaceAll("^[0-9]*_(.*)$", "$1"));
                descriptor.setDataHandler(new DataHandler(new FileDataSource(f)));
                descriptor.setMessage("OK");

            } else {
                int codice = i.getResultInfo().getStatus();
                String msg = i.getResultInfo().getInfo();
                descriptor.setMessage(String.format("ERRORE %d: %s", codice, msg));
            }



            descriptor.setOriginalName(di.getName());
            descriptor.setDocNum(di.getDocNum());

            ret.add(descriptor);
        }

        return ret.toArray(new StreamDescriptor[ret.size()]);
    }



    private Map<String, DocumentInfo> documenti(KeyValuePairDF [] file) throws IOException, DocerExceptionException {
        Map<String, DocumentInfo> documenti = new HashMap<String, DocumentInfo>();

        for (KeyValuePairDF df : file) {
            InputStream is = df.getStream().getInputStream();

            String name = df.getFileName();

            String fileName = String.format("%d_%s", System.nanoTime(), name);

//            File inputFile = new File(conf.getInputDirectory(), fileName);
            File inputFile = new File(tmpInputDirectory, fileName);

            FileUtils.copyInputStreamToFile(is, inputFile);

            DocumentInfo di = new DocumentInfo();
            di.setDocNum("0");
            di.setName(fileName);


            documenti.put(fileName, di);
        }

        return documenti;
    }



   /* private Map<String, DocumentInfo> documenti(DataHandler[] file) throws IOException, DocerExceptionException {
        Map<String, DocumentInfo> documenti = new HashMap<String, DocumentInfo>();

        for (DataHandler dh : file) {
            InputStream is = dh.getInputStream();

            String name = dh.getName();

            String fileName = String.format("%d_%s", System.nanoTime(), name);

            File inputFile = new File(conf.getInputDirectory(), fileName);

            FileUtils.copyInputStreamToFile(is, inputFile);

            DocumentInfo di = new DocumentInfo();
            di.setDocNum("0");
            di.setName(fileName);
            di.setName(name);

            documenti.put(fileName, di);
        }

        return documenti;
    }*/

    private Map<String, DocumentInfo> documenti(String ticket, String[] docNum) throws IOException, DocerExceptionException {

        Map<String, DocumentInfo> ret = new HashMap<String, DocumentInfo>();

        if (docNum != null && docNum.length > 0 && docNum[0] != null && docNum[0].equals("*")) {
            throw new IllegalArgumentException("wildcard non accettata");
        }

        if (docNum != null && docNum.length == 0 || docNum == null) {
            throw new IllegalArgumentException("nessun documento richiesto");
        }

        for (String d : docNum) {

            DocerServicesStub.GetProfileDocumentResponse respProf = info(ticket, d);
            DocerServicesStub.DownloadDocumentResponse resp = download(ticket, d);

            InputStream is = inputStream(resp);
            String name = name(respProf);

            String fileName = String.format("%d_%s", System.nanoTime(), name);

//            File inputFile = new File(conf.getInputDirectory(), fileName);
            File inputFile = new File(tmpInputDirectory, fileName);

            FileUtils.copyInputStreamToFile(is, inputFile);

            DocumentInfo di = new DocumentInfo();
            di.setDocNum(d);
            di.setName(fileName);
            /*di.setName(name);*/

            ret.put(fileName, di);
        }

        return ret;
    }

    private StreamDescriptor[] orderResults(StreamDescriptor[] streams) {
        List<Long> nums = new ArrayList<Long>();
        Map<Long,StreamDescriptor> buffer = new HashMap<Long, StreamDescriptor>();

        for (int i=0;i<streams.length;i++) {
            String[] parts = streams[i].getOriginalName().split("_",2);
            nums.add(Long.parseLong(parts[0]));
            streams[i].setOriginalName(parts[1]);
            buffer.put(Long.parseLong(parts[0]),streams[i]);
        }

        Collections.sort(nums);

        StreamDescriptor[] outStreams = new StreamDescriptor[nums.size()];
        for (int i=0;i<nums.size();i++) {
            Long key = nums.get(i);
            outStreams[i] = buffer.get(key);
        }

        return outStreams;
    }

    private InputStream inputStream(DocerServicesStub.DownloadDocumentResponse resp) throws IOException {
        return resp.get_return().getHandler().getInputStream();
    }

    private String name(DocerServicesStub.GetProfileDocumentResponse respProf) {
        return ((DocerServicesStub.KeyValuePair) CollectionUtils.find(Arrays.asList(respProf.get_return()), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return object instanceof DocerServicesStub.KeyValuePair && ((DocerServicesStub.KeyValuePair) object).getKey().equals("DOCNAME");
            }
        })).getValue();
    }

    private DocerServicesStub.DownloadDocumentResponse download(String ticket, String d) throws java.rmi.RemoteException, DocerExceptionException {
        DocerServicesStub.DownloadDocument req = new DocerServicesStub.DownloadDocument();
        req.setToken(ticket);
        req.setDocId(d);
        return documentale.downloadDocument(req);
    }

    private DocerServicesStub.GetProfileDocumentResponse info(String ticket, String d) throws java.rmi.RemoteException, DocerExceptionException {
        DocerServicesStub.GetProfileDocument reqProf = new DocerServicesStub.GetProfileDocument();
        reqProf.setToken(ticket);
        reqProf.setDocId(d);
        return documentale.getProfileDocument(reqProf);
    }


    private IProvider initProvider(String ticket) throws FirmaException, KeyException {

        String ente = Utils.extractTokenKey(ticket, "ente");
        //settato "*" perché non è disponibile e non necessario
        String aoo = "*";

        try {

            String keyProvider = "Provider_" + ente + "_" + aoo;

            ILoggedUserInfo currentUser = new LoggedUserInfo();

            String userId = Utils.extractTokenKey(ticket, "uid");
            String providerData = Utils.extractTokenKey(ticket, keyProvider);
            currentUser.setCodiceEnte(ente);
            currentUser.setTicket(providerData);
            currentUser.setUserId(userId);

            String docTicket = "calltype:internal|" + Utils.extractTokenKey(ticket, "documentale");

            IProvider provider = getProvider(ente, aoo);
            provider.setCurrentUser(currentUser);
            provider.setToken(docTicket);

            return provider;

        } catch (Exception e) {
            throw new FirmaException("Ticket non valido o provider per ENTE: " + ente + " AOO: " + aoo + " non disponibile. " + e.getMessage());
        }

    }

//    private IProvider getProvider(String ente, String aoo) throws IOException, ConfigurationException {
//        return new Provider();
//    }

    private IProvider getProvider(String ente, String aoo) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException, FirmaException {

        String providerClassName = "";

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);
        try {
            providerClassName = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(xpathProvider).getText();
        } catch (Exception e) {
            throw new FirmaException("Impossibile trovare il provider per ENTE: " + ente + " e AOO: " + aoo + " nel file di configurazione. " + e.getMessage());
        }

        IProvider prov = (IProvider) Class.forName(providerClassName).newInstance();

        return prov;
    }

    public boolean writeConfig(String token, String xml) throws FirmaException {

        String entetoken;
        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            logger.error(e.getMessage(), e);
            throw new FirmaException(e.getMessage());
        }

        try {
            CONFIGURATIONS.writeConfig(entetoken, xml);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new FirmaException("Configurazione malformata");
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new FirmaException("Configurazione malformata");
        }

        return true;

    }

    public String readConfig(String token) throws FirmaException {

        String entetoken;
        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            logger.error(e.getMessage(), e);
            throw new FirmaException(e.getMessage());
        }

        try {
            return CONFIGURATIONS.readConfig(entetoken);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new FirmaException(e.getMessage());
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new FirmaException(e.getMessage());
        }

    }


    private String getProviderXsdAttribute(String ente, String aoo) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException, FirmaException {

        String providerAttribute;
        String defaultXsdFile;

        OMElement ome1;
        try {
            ome1 = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode("//section[@name='Providers']");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new FirmaException(e.getMessage());
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
            throw new FirmaException(e.getMessage());
        }
        if (ome1 == null) {
            throw new FirmaException("xpath //section[@name='Providers'] non trovato nel file di configurazione.");
        }

        defaultXsdFile = ome1.getAttributeValue(new QName("default-xsd"));
        if (defaultXsdFile == null) {
            throw new FirmaException("Impossibile trovare l'attributo 'default-xsd' del nodo 'Providers' nel file di configurazione.");
        }

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);

        OMElement ome2;
        try {
            ome2 = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(xpathProvider);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new FirmaException(e.getMessage());
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
            throw new FirmaException(e.getMessage());
        }
        if (ome2 == null) {
            throw new FirmaException("provider non definito per ente=" + ente + " e aoo=" + aoo);
        }

        providerAttribute = ome2.getAttributeValue(new QName("xsd"));
        if (providerAttribute == null) {
            return defaultXsdFile;
        }

        return providerAttribute;
    }

    private boolean doDestinatariValidation(String ente) throws IOException, XMLStreamException, JaxenException {
        OMElement e = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode("//section[@name='BusinessLogic']/validation-destinatari");

        return e == null || e.getText().equalsIgnoreCase("true");

    }

    private void initConfig() throws ConfigurationException, IOException {
    	File config = ConfigurationUtils.loadResourceFile("configuration.properties");
        conf = new PropertiesConfiguration(config);
        tmpInputDirectory = conf.getString("tmp.directory.input");
        tmpOutputDirectory = conf.getString("tmp.directory.output");
    }


}
