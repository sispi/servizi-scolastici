package it.kdm.firma;

import eu.europa.esig.dss.alert.LogOnStatusAlert;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jaxb.common.TransformerFactoryBuilder;
import eu.europa.esig.dss.jaxb.common.XmlDefinerUtils;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.XAdESTimestampParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;

public class XAdESSigner extends Signer {

    static {
        XmlDefinerUtils xmlDefinerUtils = XmlDefinerUtils.getInstance();

        TransformerFactoryBuilder transformerBuilder = TransformerFactoryBuilder.getSecureTransformerBuilder();
        transformerBuilder.setSecurityExceptionAlert(new LogOnStatusAlert());
        //transformerBuilder.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        xmlDefinerUtils.setTransformerFactoryBuilder(transformerBuilder);
    }

    public XAdESSigner(){
        level = SignatureLevel.XAdES_BASELINE_B;
        packaging = SignaturePackaging.ENVELOPED;
    }

    public XAdESSigner(SignatureLevel level){
        this();
        withLevel(level);
    }

    @Override
    public DSSDocument signDocument(DSSDocument document) {

        // Preparing parameters for the CAdES signature
        XAdESSignatureParameters parameters = new XAdESSignatureParameters();
        // We choose the level of the signature (-B, -T, -LT, -LTA).
        parameters.setSignatureLevel(this.level);
        // We choose the type of the signature packaging (ENVELOPING, DETACHED).
        parameters.setSignaturePackaging(this.packaging);
        // We set the digest algorithm to use with the signature algorithm. You must use the
        // same parameter when you invoke the method sign on the token. The default value is
        // SHA256
        parameters.setDigestAlgorithm(digestAlgorithm);

        DSSPrivateKeyEntry privateKey = getPrivateKey();

        // We set the signing certificate
        parameters.setSigningCertificate(privateKey.getCertificate());
        // We set the certificate chain
        parameters.setCertificateChain(privateKey.getCertificateChain());

        // Create common certificate verifier
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
        // Create CAdES xadesService for signature

        XAdESService service = new XAdESService(commonCertificateVerifier);
        service.setTspSource(getTSPSource());

        // Get the SignedInfo segment that need to be signed.
        ToBeSigned dataToSign = service.getDataToSign(document, parameters);

        // This function obtains the signature value for signed information using the
        // private key and specified algorithm
        DigestAlgorithm digestAlgorithm = parameters.getDigestAlgorithm();
        SignatureValue signatureValue = token.sign(dataToSign, digestAlgorithm, privateKey);

        // We invoke the xadesService to sign the document with the signature value obtained in
        // the previous step.
        DSSDocument signedDocument = service.signDocument(document, parameters, signatureValue);

        return signedDocument;
    }

    @Override
    public DSSDocument timestampDocument(DSSDocument document) {
        XAdESTimestampParameters parameters = new XAdESTimestampParameters();
        parameters.setDigestAlgorithm(digestAlgorithm);

        // Create common certificate verifier
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
        // Create CAdES xadesService for signature

        XAdESService service = new XAdESService(commonCertificateVerifier);
        service.setTspSource(getTSPSource());

        return service.timestamp(document,parameters);
    }
}
