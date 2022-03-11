package it.kdm.firma;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.pades.*;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;

import java.awt.*;

public class PAdESSigner extends Signer {

    public PAdESSigner(){
        level = SignatureLevel.PAdES_BASELINE_B;
        packaging = SignaturePackaging.ENVELOPED;
    }

    public PAdESSigner(SignatureLevel level){
        this();
        withLevel(level);
    }

    @Override
    public DSSDocument signDocument(DSSDocument document) {
        return signDocument(document,null,null);
    }

    public DSSDocument signDocument(DSSDocument document, String text, String style) {

        // Preparing parameters for the CAdES signature
        PAdESSignatureParameters parameters = new PAdESSignatureParameters();
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

        if (text!=null){
            SignatureImageParameters imageParameters = new SignatureImageParameters();

            DSSFont font = new DSSJavaFont(Font.SERIF);
            font.setSize(12);
            Color color = Color.BLUE;

            if (style!=null){
                String[] parts = style.split("\\s");
                font = new DSSJavaFont(parts[0].trim());
                if (parts.length>1)
                    font.setSize(Integer.parseInt(parts[1].trim()));
                if (parts.length>2) {
                    try {
                        color = (Color) Color.class.getField(parts[2].trim().toUpperCase()).get(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (parts.length>3){
                    SignatureFieldParameters fieldParameters = imageParameters.getFieldParameters();
                    fieldParameters.setOriginX(Integer.parseInt(parts[3]));
                    if (parts.length>4)
                        fieldParameters.setOriginY(Integer.parseInt(parts[4]));

                    imageParameters.setFieldParameters(fieldParameters);
                }
            }

            SignatureImageTextParameters textParameters = new SignatureImageTextParameters();

            textParameters.setFont(font);
            textParameters.setTextColor(color);
            textParameters.setText(text);



            imageParameters.setTextParameters(textParameters);



            parameters.setImageParameters(imageParameters);
        }

        // Create common certificate verifier
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
        // Create CAdES xadesService for signature

        PAdESService service = new PAdESService(commonCertificateVerifier);
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
        PAdESTimestampParameters parameters = new PAdESTimestampParameters();
        parameters.setDigestAlgorithm(digestAlgorithm);

        // Create common certificate verifier
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
        // Create CAdES xadesService for signature

        PAdESService service = new PAdESService(commonCertificateVerifier);
        service.setTspSource(getTSPSource());

        return service.timestamp(document,parameters);
    }
}
