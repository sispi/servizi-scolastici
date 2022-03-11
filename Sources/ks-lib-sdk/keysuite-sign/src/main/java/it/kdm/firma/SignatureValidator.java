package it.kdm.firma;


import eu.europa.esig.dss.alert.LogOnStatusAlert;
import eu.europa.esig.dss.jaxb.common.TransformerFactoryBuilder;
import eu.europa.esig.dss.jaxb.common.XmlDefinerUtils;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.service.crl.OnlineCRLSource;
import eu.europa.esig.dss.service.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.service.http.commons.FileCacheDataLoader;
import eu.europa.esig.dss.service.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.simplereport.SimpleReportFacade;
import eu.europa.esig.dss.simplereport.jaxb.XmlTimestamp;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.spi.client.http.DSSFileLoader;
import eu.europa.esig.dss.spi.client.http.IgnoreDataLoader;
import eu.europa.esig.dss.spi.tsl.TrustedListsCertificateSource;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.CommonCertificateSource;
import eu.europa.esig.dss.spi.x509.KeyStoreCertificateSource;
import eu.europa.esig.dss.tsl.alerts.LOTLAlert;
import eu.europa.esig.dss.tsl.alerts.TLAlert;
import eu.europa.esig.dss.tsl.alerts.detections.LOTLLocationChangeDetection;
import eu.europa.esig.dss.tsl.alerts.detections.OJUrlChangeDetection;
import eu.europa.esig.dss.tsl.alerts.detections.TLExpirationDetection;
import eu.europa.esig.dss.tsl.alerts.detections.TLSignatureErrorDetection;
import eu.europa.esig.dss.tsl.alerts.handlers.log.LogLOTLLocationChangeAlertHandler;
import eu.europa.esig.dss.tsl.alerts.handlers.log.LogOJUrlChangeAlertHandler;
import eu.europa.esig.dss.tsl.alerts.handlers.log.LogTLExpirationAlertHandler;
import eu.europa.esig.dss.tsl.alerts.handlers.log.LogTLSignatureErrorAlertHandler;
import eu.europa.esig.dss.tsl.cache.CacheCleaner;
import eu.europa.esig.dss.tsl.function.OfficialJournalSchemeInformationURI;
import eu.europa.esig.dss.tsl.job.TLValidationJob;
import eu.europa.esig.dss.tsl.source.LOTLSource;
import eu.europa.esig.dss.tsl.sync.AcceptAllStrategy;
import eu.europa.esig.dss.validation.AdvancedSignature;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.executor.signature.DefaultSignatureProcessExecutor;
import eu.europa.esig.dss.validation.reports.Reports;
import it.kdm.firma.bean.Firmatario;
import it.kdm.firma.bean.VerificaFirma;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class SignatureValidator {

    static {
        System.setProperty("javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema","com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory");

        XmlDefinerUtils xmlDefinerUtils = XmlDefinerUtils.getInstance();

        TransformerFactoryBuilder transformerBuilder = TransformerFactoryBuilder.getSecureTransformerBuilder();
        transformerBuilder.setSecurityExceptionAlert(new LogOnStatusAlert());
        //transformerBuilder.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        xmlDefinerUtils.setTransformerFactoryBuilder(transformerBuilder);
    }

    private static final Logger log = LoggerFactory.getLogger(SignatureValidator.class);

    private String simpleReport;
    private String diagnosticReport;
    private String detailedReport;
    private String italianXmlReport;
    private String italianJsonReport;
    private DSSDocument dssDocument;
    Reports reports = null;

    private InputStream policyFile = null;
    private GeneratoreFile gf = null;

    List<DSSDocument> originalDocuments = null;

    static CommonCertificateVerifier certificateVerifier = new CommonCertificateVerifier();
    static Date lastRefresh = null;

    private static final String LOTL_URL = "https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml";
    //private static final String LOTL_URL = "https://ec.europa.eu/tools/lotl/eu-lotl.xml";

    private static final String OJ_URL = "https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=uriserv:OJ.C_.2019.276.01.0001.01.ENG";
    static Date getLastRefresh()
    {
        return lastRefresh;
    }

    public SignatureValidator(InputStream stream) {
        this(new InMemoryDocument(stream));
    }

    public SignatureValidator(byte[] bytes) {
        this(new InMemoryDocument(bytes));
    }

    public SignatureValidator(File file) {
        this(new FileDocument(file));
    }

    public SignatureValidator(DSSDocument document) {
        this.dssDocument = document;
    }

    public SignatureValidator setPolicyFile(InputStream policyFile){
        this.policyFile = policyFile;
        return this;
    }

    private static TLValidationJob job() {
        TLValidationJob job = new TLValidationJob();
        job.setOfflineDataLoader(offlineLoader());
        job.setOnlineDataLoader(onlineLoader());
        job.setTrustedListCertificateSource(trustedCertificateSource());
        job.setSynchronizationStrategy(new AcceptAllStrategy());
        job.setCacheCleaner(cacheCleaner());

        LOTLSource europeanLOTL = europeanLOTL();
        job.setListOfTrustedListSources(europeanLOTL);

        job.setLOTLAlerts(Arrays.asList(ojUrlAlert(europeanLOTL), lotlLocationAlert(europeanLOTL)));
        job.setTLAlerts(Arrays.asList(tlSigningAlert(), tlExpirationDetection()));

        return job;
    }

    private static TrustedListsCertificateSource trustedCertificateSource() {
        return new TrustedListsCertificateSource();
    }

    private static LOTLSource europeanLOTL() {
        LOTLSource lotlSource = new LOTLSource();
        lotlSource.setUrl(LOTL_URL);
//		lotlSource.setCertificateSource(officialJournalContentKeyStore());
        lotlSource.setCertificateSource(new CommonCertificateSource());
        lotlSource.setSigningCertificatesAnnouncementPredicate(new OfficialJournalSchemeInformationURI(OJ_URL));
        lotlSource.setPivotSupport(true);

        return lotlSource;
    }

    private static DSSFileLoader offlineLoader() {
        FileCacheDataLoader offlineFileLoader = new FileCacheDataLoader();
        offlineFileLoader.setCacheExpirationTime(Long.MAX_VALUE);
        offlineFileLoader.setDataLoader(new IgnoreDataLoader());
        offlineFileLoader.setFileCacheDirectory(tlCacheDirectory());
        return offlineFileLoader;
    }

    private static DSSFileLoader onlineLoader() {
        FileCacheDataLoader onlineFileLoader = new FileCacheDataLoader();
        onlineFileLoader.setCacheExpirationTime(0);
        onlineFileLoader.setDataLoader(dataLoader());
        onlineFileLoader.setFileCacheDirectory(tlCacheDirectory());
        return onlineFileLoader;
    }

    private static CommonsDataLoader dataLoader() {
        return new CommonsDataLoader();
    }

    private static CacheCleaner cacheCleaner() {
        CacheCleaner cacheCleaner = new CacheCleaner();
        cacheCleaner.setCleanMemory(true);
        cacheCleaner.setCleanFileSystem(true);
        cacheCleaner.setDSSFileLoader(offlineLoader());
        return cacheCleaner;
    }

    private static TLAlert tlSigningAlert() {
        TLSignatureErrorDetection signingDetection = new TLSignatureErrorDetection();
        LogTLSignatureErrorAlertHandler handler = new LogTLSignatureErrorAlertHandler();
        return new TLAlert(signingDetection, handler);
    }

    private static TLAlert tlExpirationDetection() {
        TLExpirationDetection expirationDetection = new TLExpirationDetection();
        LogTLExpirationAlertHandler handler = new LogTLExpirationAlertHandler();
        return new TLAlert(expirationDetection, handler);
    }

    private static LOTLAlert ojUrlAlert(LOTLSource source) {
        OJUrlChangeDetection ojUrlDetection = new OJUrlChangeDetection(source);
        LogOJUrlChangeAlertHandler handler = new LogOJUrlChangeAlertHandler();
        return new LOTLAlert(ojUrlDetection, handler);
    }

    private static LOTLAlert lotlLocationAlert(LOTLSource source) {
        LOTLLocationChangeDetection lotlLocationDetection = new LOTLLocationChangeDetection(source);
        LogLOTLLocationChangeAlertHandler handler = new LogLOTLLocationChangeAlertHandler();
        return new LOTLAlert(lotlLocationDetection, handler);
    }

    private static File tlCacheDirectory() {
        File rootFolder = new File(System.getProperty("java.io.tmpdir"));
        File tslCache = new File(rootFolder, "dss-tsl-loader");
        if (tslCache.mkdirs()) {
            log.info("TL Cache folder : {}", tslCache.getAbsolutePath());
        }
        return tslCache;
    }

    private static CertificateSource officialJournalContentKeyStore() {
        try {
            String keystoreFileName = "keystore.p12";
            File homeDir = new File(System.getProperty("user.home"));
            File configDir = new File(homeDir, "bpm-config");
            File keystoreFile = new File(configDir,keystoreFileName);

            return new KeyStoreCertificateSource(keystoreFile, "PKCS12", "dss-password");
        } catch (IOException e) {
            throw new DSSException("Unable to load the keystore", e);
        }
    }

    private static CommonCertificateVerifier createCertificateVerifier() {
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
        TLValidationJob job = job();
        TrustedListsCertificateSource trustedListsCertificateSource = new TrustedListsCertificateSource();
        job.setTrustedListCertificateSource(trustedListsCertificateSource);
        job.onlineRefresh();
        commonCertificateVerifier.setTrustedCertSources(trustedListsCertificateSource);
        CommonsDataLoader commonsDataLoader = new CommonsDataLoader();
        commonCertificateVerifier.setCrlSource(new OnlineCRLSource());
        commonCertificateVerifier.setOcspSource(new OnlineOCSPSource());
        commonCertificateVerifier.setDataLoader(commonsDataLoader);

        return commonCertificateVerifier;
    }

    private CommonCertificateVerifier getCertificateVerifier(boolean refreshCertificates) {
        if (lastRefresh!=null && !refreshCertificates)
            return certificateVerifier;

        synchronized (certificateVerifier)
        {
            CommonCertificateVerifier newVerifier = createCertificateVerifier();

            certificateVerifier = newVerifier;

            System.out.println("Verifier initialized:"+lastRefresh);

            lastRefresh = new Date();
            return certificateVerifier;
        }
    }

    public List<DSSDocument> getOriginalDocuments(){
        return originalDocuments;
    }

    public VerificaFirma validateDocument() {
        return validateDocument(null);
    }

    public String generateHtmlReportBS4(){
        try {
            return SimpleReportFacade.newFacade().generateHtmlReport(this.simpleReport);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateHtmlReportBS3(){
        try {
            return SimpleReportFacade.newFacade().generateHtmlReport(this.simpleReport);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Reports getReports(){
        return reports;
    }

    public VerificaFirma validateDocument(Date verificationDate) {

        SignedDocumentValidator validator = SignedDocumentValidator.fromDocument(dssDocument);

        if (verificationDate==null)
            verificationDate = new Date();

        //tolgo 30 minuti
        verificationDate = new Date(verificationDate.getTime()-1000*60*30);

        reports = null;

        boolean forceRefresh = false;

        if (lastRefresh==null || verificationDate.after(lastRefresh))
            forceRefresh = true;

        DefaultSignatureProcessExecutor executor = new DefaultSignatureProcessExecutor();
        executor.setCurrentTime(verificationDate);
        validator.setProcessExecutor(executor);

        CommonCertificateVerifier verifier = getCertificateVerifier(forceRefresh);

        //validator.getDefaultProcessExecutor().setCurrentTime(verificationDate);

        validator.setCertificateVerifier( verifier );
        reports = validator.validateDocument(policyFile);

        List<AdvancedSignature> signatures = validator.getSignatures();
        Map<String, String> CFs = new HashMap<String, String>();

        originalDocuments = null;

        for (AdvancedSignature signature : signatures) {

            if (originalDocuments==null)
                originalDocuments = validator.getOriginalDocuments(signature.getId());

            List<CertificateToken> certificates = signature.getCertificates();

            for( CertificateToken ct : certificates ){
                String name = DSSASN1Utils.getHumanReadableName(ct);
                String cf = DSSASN1Utils.get(ct.getSubject().getPrincipal()).get(BCStyle.SERIALNUMBER.toString());
                if (cf!=null && name!=null) {
                    CFs.put(name, cf);
                }
            }
        }

        setSimpleReport(reports.getXmlSimpleReport());
        setDetailedReport(reports.getXmlDetailedReport());
        setDiagnosticReport(reports.getXmlDiagnosticData());

        gf = new GeneratoreFile(reports, CFs);
        setItalianXmlReport(gf.getXmlStringReport());
        setItalianJsonReport(gf.getJsonReport());

        if (originalDocuments!=null) {
            validateNested(verificationDate);
        }

        return gf.getEsitoVerifiche().getVerificaFirma();
    }

    public String getSimpleReport() {
        return simpleReport;
    }

    public void setSimpleReport(String simpleReport) {
        this.simpleReport = simpleReport;
    }

    public String getDiagnosticReport() {
        return diagnosticReport;
    }

    public void setDiagnosticReport(String diagnosticReport) {
        this.diagnosticReport = diagnosticReport;
    }

    public String getDetailedReport() {
        return detailedReport;
    }

    public void setDetailedReport(String detailedReport) {
        this.detailedReport = detailedReport;
    }

    public String getItalianXmlReport() {
        return italianXmlReport;
    }

    public void setItalianXmlReport(String italianXmlReport) {
        this.italianXmlReport = italianXmlReport;
    }

    public String getItalianJsonReport() {
        return italianJsonReport;
    }

    public void setItalianJsonReport(String italianJsonReport) {
        this.italianJsonReport = italianJsonReport;
    }

    public List<XmlTimestamp> getMarcheTemporali(){
        return gf.getEsitoVerifiche().getVerificaFirma().getXmlTimestamps();
    }

    public List<Firmatario> getFirmatari() {
        return gf.getEsitoVerifiche().getVerificaFirma().getFirmatari();
    }

    private void validateNested(Date verificationDate){

        for( DSSDocument docs : originalDocuments){
            try{
                InMemoryDocument stream = new InMemoryDocument(docs.openStream());
                if (stream.getBytes().length==0)
                    continue;
                SignatureValidator val = new SignatureValidator(stream);
                val.validateDocument(verificationDate);

                gf.getEsitoVerifiche()
                        .getVerificaFirma()
                        .getFirmatari()
                        .addAll(val.getFirmatari());

                gf.getEsitoVerifiche()
                        .getVerificaFirma()
                        .getXmlTimestamps()
                        .addAll(val.getMarcheTemporali());

            } catch (DSSException e) {
                throw e;
            } catch (Exception e ) {
                throw e;
            }
        }
    }
}
