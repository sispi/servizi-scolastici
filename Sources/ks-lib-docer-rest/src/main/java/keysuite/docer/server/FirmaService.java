package keysuite.docer.server;

import com.github.underscore.lodash.U;
import com.google.common.base.Strings;
import eu.europa.esig.dss.validation.reports.Reports;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.ResourceCache;
import it.kdm.firma.SignatureValidator;
import it.kdm.firma.bean.Firmatario;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.utils.ResourceUtils;
import keysuite.cache.ClientCache;
import keysuite.desktop.exceptions.*;
import keysuite.docer.client.*;
import keysuite.docer.client.verificafirma.VerificaFirmaDTO;
import net.sf.saxon.TransformerFactoryImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.*;
import org.apache.fop.util.DefaultErrorListener;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import static keysuite.docer.client.ClientUtils.throwKSException;

@Component
public class FirmaService extends BaseService implements IFirma {

    protected final static Logger logger = LoggerFactory.getLogger(FirmaService.class);

    @Autowired
    DocumentiService documentiService;

    @Autowired
    FileService fileService;

    private static Map<String,Map<String,String>> providerOptions = new LinkedHashMap<>();

    private static Map<String,String> initFirmaService(String codAoo) {

        if (confFile!=null && !confFile.isValid()){
            synchronized (providerOptions){
                providerOptions.clear();
                confFile = null;
            }
        }

        if (providerOptions.containsKey(codAoo))
            return providerOptions.get(codAoo);

        synchronized (providerOptions) {

            if (confFile==null)
                confFile = ConfigurationUtils.getFile(null,"configuration.xml");

            String xml;
            ResourceCache.ResourceFile f;
            String codEnte = ClientCache.getInstance().getAOO(codAoo).getCodEnte();
            try {
                f = ConfigurationUtils.getFile( codEnte,"configuration.xml");
                xml = FileUtils.readFileToString(f, Charset.defaultCharset());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Map<String, Object> map = (Map) U.fromXml(xml);

            List<Map> groups = (List) U.get(map, "configuration.group");

            for( Map x : groups ){

                if ("Firma".equals(x.get("-name"))){
                    Map s = (Map) x.get("section");
                    Object y = s.get("provider");
                    String def = (String) s.get("-default-provider");

                    List<Map> providers = y instanceof List ? (List<Map>) y : Collections.singletonList((Map) y);

                    for( Map<String,Object> provider : providers ){
                        String ente = (String) provider.remove("-ente");
                        String aoo = (String) provider.remove("-aoo");
                        String className = (String) provider.remove("#text");

                        HashMap m = new HashMap();

                        for( String k : provider.keySet()){
                            if (k.startsWith("-"))
                                m.put(k.substring(1),provider.get(k));
                        }

                        if (Strings.isNullOrEmpty(className))
                            m.put("class",def);
                        else
                            m.put("class",className);

                        if ("*".equals(ente))
                            providerOptions.put("*", m );
                        else if ("*".equals(aoo))
                            providerOptions.put(ente, m );
                        else
                            providerOptions.put(aoo, m );
                    }

                }
            }
        }

        return providerOptions.get(codAoo);
    }

    private Map<String,String> getProviderOptions(){
        String codAoo = Session.getUserInfoNoExc().getCodAoo();

        Map<String,String> options = initFirmaService(codAoo);

        if (options==null){
            String codEnte = ClientCache.getInstance().getAOO(codAoo).getCodEnte();
            options = providerOptions.getOrDefault(codEnte , providerOptions.getOrDefault("*" , new HashMap<>()) );
            providerOptions.put(codAoo, options);
        }

        return new HashMap(options);
    }

    private IFirmaProvider getProvider() {
        Map<String,String> options = getProviderOptions();
        return getProvider(options);
    }

    private IFirmaProvider getProvider(Map<String,String> options) {
        try {
            String provider = options.get("class");
            return (IFirmaProvider) Class.forName(provider).getConstructor(Map.class).newInstance(options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public URL[] firmaAutomatica(String alias, String pin, String tipo,URL[] urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        NamedInputStream[] inputStreams = new NamedInputStream[urls.length];

        for( int i=0; i<inputStreams.length; i++){
            inputStreams[i] = fileService.openURL(urls[0]);
        }

        NamedInputStream[] streams = getProvider().firmaAutomatica(alias,pin,tipo,inputStreams);

        URL[] outUrls = new URL[streams.length];

        for( int i=0; i<outUrls.length; i++){
            outUrls[i] = fileService.createURL(streams[i]);
        }

        return outUrls;

    }

    @Override
    public URL[] firmaRemota(String alias, String pin, String tipo, String OTP, URL[] urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        NamedInputStream[] inputStreams = new NamedInputStream[urls.length];

        for( int i=0; i<inputStreams.length; i++){
            inputStreams[i] = fileService.openURL(urls[0]);
        }

        NamedInputStream[] streams = getProvider().firmaRemota(alias,pin,tipo,OTP,inputStreams);

        URL[] outUrls = new URL[streams.length];

        for( int i=0; i<outUrls.length; i++){
            outUrls[i] = fileService.createURL(streams[i]);
        }

        return outUrls;
    }


    public URL[] print(String text, String style, URL... urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        NamedInputStream[] inputStreams = new NamedInputStream[urls.length];

        for( int i=0; i<inputStreams.length; i++){
            inputStreams[i] = fileService.openURL(urls[0]);
        }

        Map<String,String> options = getProviderOptions();
        options.put("keystore",options.get("print-keystore"));

        NamedInputStream[] streams = getProvider(options).print(text,style,inputStreams);

        URL[] outUrls = new URL[streams.length];

        for( int i=0; i<outUrls.length; i++){
            outUrls[i] = fileService.createURL(streams[i]);
        }

        return outUrls;
    }

    @Override
    public URL[] timestamp(String tipo, URL... urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        NamedInputStream[] inputStreams = new NamedInputStream[urls.length];

        for( int i=0; i<inputStreams.length; i++){
            inputStreams[i] = fileService.openURL(urls[0]);
        }

        NamedInputStream[] streams = getProvider().timestamp(tipo,inputStreams);

        URL[] outUrls = new URL[streams.length];

        for( int i=0; i<outUrls.length; i++){
            outUrls[i] = fileService.createURL(streams[i]);
        }

        return outUrls;
    }

    public List<String> reports(String verificationDate, String policyFile, URL[] urls, boolean bootStrap3) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        List<String> reports = new ArrayList<>();

        for( int x=0; x<urls.length; x++ ) {

            URL url = urls[x];

            long t0 = System.currentTimeMillis();

            NamedInputStream stream = fileService.openURL(url);

            Date verDate = null;

            if (verificationDate != null)
                verDate = new DateTime(verificationDate).toDate();
                //verDate = Utils.parseDateTime(verificationDate).toDate();

            SignatureValidator validator = new SignatureValidator(stream.getStream());

            try {
                if (policyFile != null)
                    validator.setPolicyFile(ResourceUtils.getResourceAsStream(policyFile));

                validator.validateDocument(verDate);

                if (bootStrap3)
                    reports.add(validator.generateHtmlReportBS3());
                else
                    reports.add(validator.generateHtmlReportBS4());

            } catch (Exception exc) {
                throw throwKSException(exc);
            }
        }

        return reports;
    }

    /*@Override
    public Documento[] firmaAutomaticaDocumenti(String alias, String pin, String tipo, boolean relate, URL[] urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        throw new KSRuntimeException(Code.E000, "operazione non implementata");
    }

    @Override
    public Documento[] firmaRemotaDocumenti(String alias, String pin, String tipo, String OTP, boolean relate, URL[] urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        throw new KSRuntimeException(Code.E000, "operazione non implementata");
    }*/

    @Override
    public void requestOTP(String alias, String pin) throws KSExceptionForbidden, KSExceptionBadRequest {
        getProvider().requestOTP(alias,pin);
    }

    @Override
    public VerificaFirmaDTO[] verificaFirme(String verificationDate, String policyFile, URL[] urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        NamedInputStream[] inputStreams = new NamedInputStream[urls.length];

        for( int i=0; i<inputStreams.length; i++){
            inputStreams[i] = fileService.openURL(urls[0]);
        }

        VerificaFirmaDTO[] dtos = verificaFirme(verificationDate,policyFile,inputStreams);

        return dtos;
    }

    public VerificaFirmaDTO[] verificaFirme(String verificationDate, String policyFile, NamedInputStream[] inputStreams) throws KSExceptionBadRequest {

        Object[] result = getProvider().verificaFirme(verificationDate,policyFile,inputStreams);
        VerificaFirmaDTO[] dtos = new VerificaFirmaDTO[result.length];

        for( int x = 0; x<dtos.length; x++ ){
            Object obj = result[x];
            if ( !(obj instanceof VerificaFirmaDTO)){
                try {
                    String json = ClientUtils.OM.writeValueAsString(obj);
                    obj = ClientUtils.OM.readValue(json, VerificaFirmaDTO.class);
                } catch (Exception exc) {
                    throw new KSRuntimeException("la struttura del bean ritornato è invalido:"+obj);
                }
            }
            dtos[x] = (VerificaFirmaDTO) obj;
        }

        return dtos;
    }

    @Override
    public DocerBean create(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        throw new KSExceptionForbidden("unsupported");
    }

    @Override
    public DocerBean update(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        throw new KSExceptionForbidden("unsupported");
    }

    public NamedInputStream getPDFReport(String verificationDate, String policyFile, URL url, boolean detailed) throws Exception {

        NamedInputStream stream = fileService.openURL(url);

        Date verDate = null;

        if (verificationDate != null)
            verDate = new DateTime(verificationDate).toDate();
            //verDate = Utils.parseDateTime(verificationDate).toDate();

        SignatureValidator validator = new SignatureValidator(stream.getStream());
        if (policyFile != null)
            validator.setPolicyFile(ResourceUtils.getResourceAsStream(policyFile));

        validator.validateDocument(verDate);

        Reports reports = validator.getReports();

        String report = reports.getXmlSimpleReport();
        InputStream tmpl = ResourceUtils.getResourceAsStream("simple-report.xslt");
        String xslt = IOUtils.toString(tmpl,"utf-8");

        StreamSource xmlSource = new StreamSource(new StringReader(report));
        StreamSource xslSource = new StreamSource(new StringReader(xslt));

        FopFactoryBuilder builder = new FopFactoryBuilder(new File(".").toURI());
        builder.setAccessibility(true);

        FopFactory fopFactory = builder.build();
        //FopFactory fopFactory = FopFactory.newInstance();
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        TransformerFactoryImpl impl = new TransformerFactoryImpl();
        impl.setErrorListener(new DefaultErrorListener(LogFactory.getLog(FirmaService.class)));

        Transformer xslfoTransformer = impl.newTransformer(xslSource);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outStream);
        Result res = new SAXResult(fop.getDefaultHandler());

        //SimpleReportFacade.newFacade().generatePdfReport(reports.getSimpleReportJaxb(),res);
        xslfoTransformer.transform(xmlSource, res);

        String docname = FilenameUtils.getBaseName(stream.getName())+"_report.pdf";

        InputStream streamOut = new ByteArrayInputStream(outStream.toByteArray());

        return NamedInputStream.getNamedInputStream(streamOut,docname);
    }
}
