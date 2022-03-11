package keysuite.docer.server;

import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.simplereport.jaxb.XmlCertificate;
import eu.europa.esig.dss.simplereport.jaxb.XmlSignature;
import eu.europa.esig.dss.simplereport.jaxb.XmlTimestamp;
import it.kdm.firma.PAdESSigner;
import it.kdm.firma.SignatureValidator;
import it.kdm.firma.Signer;
import it.kdm.firma.SignerFactory;
import it.kdm.firma.bean.Firmatario;
import it.kdm.orchestratore.utils.ResourceUtils;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.docer.client.IFirmaProvider;
import keysuite.docer.client.NamedInputStream;
import keysuite.docer.client.verificafirma.Signature;
import keysuite.docer.client.verificafirma.Timestamp;
import keysuite.docer.client.verificafirma.Token;
import keysuite.docer.client.verificafirma.VerificaFirmaDTO;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class FirmaDefaultProvider implements IFirmaProvider {

    Environment env;
    String keystore;
    String tspServer;
    Map<String,String> options;

    private String getSignedName(String tipo, String name){
        String basename = FilenameUtils.getBaseName(name);
        char c = tipo.toUpperCase().charAt(0);
        if (c=='C')
            return name+".p7m";
        else if (c=='P')
            return basename+"_signed.pdf";
        else if (c=='X')
            return basename+"_signed.xml";
        else
            return basename+"_signed."+FilenameUtils.getExtension(name);
    }

    public FirmaDefaultProvider(Map<String,String> options){
        this.options = options;
        this.keystore = options.getOrDefault("keystore","keystore");
        this.tspServer = options.get("tsp-server");
    }

    public NamedInputStream[] firma(String alias, String pin, String tipo, String text, String style, NamedInputStream... streams) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        try{

            String keystore = this.keystore;
            if (alias!=null){
                if (ResourceUtils.getResourceNoExc(alias+".p12")!=null){
                    keystore = alias;
                    alias = null;
                }
            }

            Signer signer = SignerFactory.get(tipo).withKeyStore(keystore,pin).withAlias(alias);

            if (tspServer!=null)
                signer.withTimestampServer(tspServer);

            List<NamedInputStream> outStreams = new ArrayList<>();

            for( NamedInputStream stream : streams ){

                DSSDocument signedDoc;

                if (text!=null && signer instanceof PAdESSigner){
                    signedDoc = ((PAdESSigner)signer).signDocument(
                            new InMemoryDocument(stream.getStream()),text,style);
                } else {
                    signedDoc = signer.signDocument(stream.getStream());
                }

                outStreams.add(
                        NamedInputStream.getNamedInputStream(
                                signedDoc.openStream(),
                                getSignedName(tipo,stream.getName())));
            }

            return outStreams.toArray(new NamedInputStream[0]);

        } catch ( Exception e ){
            throw new KSRuntimeException(e);
        }
    }

    public NamedInputStream[] print(String text, String style , NamedInputStream... streams) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        String alias = options.get("print-alias");
        String pin = options.get("print-password");

        return firma(alias,pin,"PADES",text,style,streams);
    }

    private String formatDateTime(Date date){
        return new DateTime(date).toString();
    }

    @Override
    public Object[] verificaFirme(String verificationDate, String policyFile, NamedInputStream... streams) throws KSExceptionBadRequest {
        Object[] dtos = new Object[streams.length];

        for( int x=0; x < streams.length; x++ ) {

            //URL url = urls[x];
            NamedInputStream nis = streams[x];

            long t0 = System.currentTimeMillis();

            InputStream stream;
            int size;
            String name=null;
            try {
                //stream = fileService.openURL(url).getStream();
                stream = nis.getStream();
                name = nis.getName();
                size = stream.available();

                /*if (stream instanceof FileInputStream){
                    try {
                        Field pathField = FileInputStream.class.getDeclaredField("path");
                        pathField.setAccessible(true);
                        name = (String) pathField.get(stream);
                        name = FilenameUtils.getName(name);
                    }catch (Exception e){}
                }

                if (name==null){
                    String ext = FilenameUtils.getExtension(name);
                    if (ext!=null && ext.length()>0 && ext.length()<6){
                        name = "file"+x+"."+ext;
                    } else{
                        name = "file"+x+".tmp";
                    }
                }*/
            } catch (IOException e) {
                throw new KSRuntimeException(e);
            }

            Date verDate = null;

            if (verificationDate != null)
                verDate = new DateTime(verificationDate).toDate();

            SignatureValidator validator = new SignatureValidator(stream);

            List<Firmatario> firmatari = new ArrayList<>();
            List<XmlTimestamp> marche = new ArrayList<>();

            try {
                if (policyFile != null)
                    validator.setPolicyFile(ResourceUtils.getResourceAsStream(policyFile));
                validator.validateDocument(verDate);

                firmatari = validator.getFirmatari();
                marche = validator.getMarcheTemporali();

            } catch (Exception exc) {
                throw new KSRuntimeException(exc);
            }

            Indication indication;
            List<Signature> signatures = new ArrayList<>();
            List<Timestamp> timestamps = new ArrayList<>();

            for (XmlTimestamp ts : marche) {
                Timestamp timestamp = new Timestamp();

                timestamp.setIndication(Token.Indication.valueOf(ts.getIndication().name()) );
                timestamp.setSigningTime(formatDateTime(ts.getProductionTime()));

                //timestamp.setErrors(ts.getErrors());
                //timestamp.setWarnings(ts.getWarnings());
                //timestamp.setInfos(ts.getInfos());
                timestamp.setInfos(Collections.singletonList(ts.getProducedBy()));
                timestamp.setLevel(ts.getTimestampLevel().getValue().name());

                List<XmlCertificate> chain = ts.getCertificateChain().getCertificate();

                for (int i = 0; i < chain.size(); i++) {
                    XmlCertificate certificate = chain.get(i);
                    timestamp.getChain().add(certificate.getQualifiedName());

                    if (i == 1)
                        timestamp.setIssuer(certificate.getQualifiedName());
                }
                timestamps.add(timestamp);
            }

            if (firmatari.size() == 0)
                indication = Indication.NO_SIGNATURE_FOUND;
            else
                indication = Indication.TOTAL_PASSED;

            for (Firmatario firmatario : firmatari) {

                Signature signature = new Signature();
                signatures.add(signature);

                signature.setPosition(firmatario.getOrdineFirma());

                signature.setTrustedListValidation(firmatario.getEsitoFirma().getVerificaFirma().getControlloCatenaTrusted());
                signature.setCertificateValidation(firmatario.getEsitoFirma().getVerificaFirma().getControlloCertificato());
                signature.setCompliancyValidation(firmatario.getEsitoFirma().getVerificaFirma().getCompliancyString());
                signature.setCryptographyValidation(firmatario.getEsitoFirma().getVerificaFirma().getControlloCrittografico());
                signature.setRevocationStatus(firmatario.getEsitoFirma().getVerificaFirma().getControlloCRL());
                signature.setCompliancyValidation(firmatario.getEsitoFirma().getControlloConformita());

                XmlSignature xmlSignature = firmatario.getXmlSignature();

                if (xmlSignature.getIndication().ordinal() > indication.ordinal())
                    indication = xmlSignature.getIndication();

                //TOTAL_PASSED, TOTAL_FAILED, INDETERMINATE, PASSED, FAILED, NO_SIGNATURE_FOUND

                /*
                signature.setSigner(firmatario.getCognomeNome());
                signature.setRiferimentoTemporale(firmatario.getRiferimentoTemporaleUsato());
                signature.setCodiceEsito(firmatario.getEsitoFirma().getControlloConformita());
                signature.setFormatoFirma(firmatario.getFormatoFirma());
                signature.setOrdineFirma(firmatario.getOrdineFirma());*/

                signature.setSignatureFormat(xmlSignature.getSignatureFormat().name());
                signature.setSigner(xmlSignature.getSignedBy());
                signature.setIndication(Token.Indication.valueOf(xmlSignature.getIndication().name()));
                signature.setSigningTime(formatDateTime(xmlSignature.getSigningTime()));
                signature.setBestSignatureTime(formatDateTime(xmlSignature.getBestSignatureTime()));
                signature.setExtensionPeriodMax(formatDateTime(xmlSignature.getExtensionPeriodMax()));
                signature.setExtensionPeriodMin(formatDateTime(xmlSignature.getExtensionPeriodMin()));
                signature.setParentId(xmlSignature.getParentId());
                signature.setCounterSignature(xmlSignature.isCounterSignature());
                //signature.setErrors(xmlSignature.getErrors());
                //signature.setWarnings(xmlSignature.getWarnings());
                //signature.setInfos(xmlSignature.getInfos());
                signature.setLevel(xmlSignature.getSignatureLevel().getValue().name());

                List<XmlCertificate> chain = xmlSignature.getCertificateChain().getCertificate();

                for (int i = 0; i < chain.size(); i++) {
                    XmlCertificate certificate = chain.get(i);
                    signature.getChain().add(certificate.getQualifiedName());

                    if (i == 1)
                        signature.setIssuer(certificate.getQualifiedName());
                }
            }


            VerificaFirmaDTO dto = new VerificaFirmaDTO();
            dto.setIndication(Token.Indication.valueOf(indication.name()));
            dto.setSignatures(signatures);
            dto.setTimestamps(timestamps);
            dto.setElapsed(((Number) (System.currentTimeMillis() - t0)).intValue());
            dto.setSize(size);
            dto.setName(name);

            dtos[x] = dto;

            /*try {
                String json = ClientUtils.OM.writeValueAsString(dto);
                dtos[x] = ClientUtils.OM.readValue(json, Map.class);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }*/
        }

        return dtos;
    }

    @Override
    public NamedInputStream[] firmaRemota(String alias, String pin, String tipo, String OTP, NamedInputStream... streams) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return firma(alias,pin,tipo,null,null,streams);
    }

    @Override
    public NamedInputStream[] firmaAutomatica(String alias, String pin, String tipo, NamedInputStream... streams) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return firma(alias,pin,tipo,null,null,streams);
    }

    @Override
    public NamedInputStream[] timestamp(String tipo, NamedInputStream... streams) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try{

            Signer signer = SignerFactory.get(tipo);

            if (tspServer!=null)
                signer.withTimestampServer(tspServer);

            List<NamedInputStream> outStreams = new ArrayList<>();

            for( NamedInputStream stream : streams ){
                outStreams.add(NamedInputStream.getNamedInputStream(
                        signer.timestampDocument(stream.getStream()).openStream(),
                        getSignedName(tipo,stream.getName())));
            }

            return outStreams.toArray(new NamedInputStream[0]);

        } catch ( Exception e ){
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public void requestOTP(String alias, String pin) throws KSExceptionForbidden, KSExceptionBadRequest {

    }

}
