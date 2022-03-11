package it.kdm.firma;

import eu.europa.esig.dss.cades.CAdESSignatureParameters;
import eu.europa.esig.dss.cades.signature.CAdESService;
import eu.europa.esig.dss.cades.signature.CAdESTimestampParameters;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;

public class CAdESSigner extends Signer {

    public CAdESSigner(){
        level = SignatureLevel.CAdES_BASELINE_B;
        packaging = SignaturePackaging.ENVELOPING;
    }

    public CAdESSigner(SignatureLevel level){
        this();
        withLevel(level);
    }

    @Override
    public DSSDocument signDocument(DSSDocument document) {

        // Preparing parameters for the CAdES signature
        CAdESSignatureParameters parameters = new CAdESSignatureParameters();
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

        CAdESService service = new CAdESService(commonCertificateVerifier);

        // Get the SignedInfo segment that need to be signed.
        ToBeSigned dataToSign = service.getDataToSign(document, parameters);
        service.setTspSource(getTSPSource());

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
        CAdESTimestampParameters parameters = new CAdESTimestampParameters();
        parameters.setDigestAlgorithm(digestAlgorithm);

        // Create common certificate verifier
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
        // Create CAdES xadesService for signature

        CAdESService service = new CAdESService(commonCertificateVerifier);
        service.setTspSource(getTSPSource());

        return service.timestamp(document,parameters);
    }
}
