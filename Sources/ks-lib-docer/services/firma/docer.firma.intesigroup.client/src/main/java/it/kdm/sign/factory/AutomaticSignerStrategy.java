package it.kdm.sign.factory;

import it.kdm.sign.model.ResultInfo;
import it.pkbox.client.Envelope;
import it.pkbox.client.XMLEnvelope;
import it.pkbox.client.XMLReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Map;

public class AutomaticSignerStrategy implements SignerStrategy {

    @Override
    public void sign(String customerInfo, String alias, String pin, String formatoFirma, File fin, String authData, File fout, Envelope enve, Map<String, ResultInfo> returnValue, int accessPermissions) {
        try {
            FileInputStream fileIn = new FileInputStream(fin);
            FileOutputStream fileOut = new FileOutputStream(fout);

            if ("PADES".equalsIgnoreCase(formatoFirma)) {
                FileInputStream fis = new FileInputStream(fin);
                FileOutputStream fos = new FileOutputStream(fout);
                byte[] document = new byte[(int) fin.length()];
                fis.read(document);
                fis.close();
                byte[] outdocument = enve.pdfsign(document, accessPermissions, "", "", "", "", "", customerInfo, alias, pin, authData, new Date(), null, 0, 0, 0, 0, 0, 0);
                fos.write(outdocument);
                fos.close();

            } else if ("CADES".equalsIgnoreCase(formatoFirma)) {
                enve.sign(fileIn, fileIn.available(), alias, pin, Envelope.implicitMode, Envelope.base64Encoding, null, fileOut);
            }

            fileOut.close();

            returnValue.put(fin.getName(), new ResultInfo(0, fout.getName()));
        } catch (Exception ex) {
            if (fout != null) {
                fout.delete();
            }
            returnValue.put(fin.getName(), new ResultInfo(400, ex.getMessage()));
        }
    }

    @Override
    public void xmlsign(XMLReference[] references, File fin, String signer, String pin, String signerPin, XMLEnvelope xmlEnve, Map<String, ResultInfo> returnValue, File fout) {
        try {
            FileInputStream fis = new FileInputStream(fin);
            FileOutputStream fos = new FileOutputStream(fout);
            int dataLength = (int) fin.length();
            xmlEnve.xmlsign(fis, dataLength, null, references, null, signer, pin, null, XMLEnvelope.envelopedMode, new Date(), fos);
            returnValue.put(fin.getName(), new ResultInfo(0, fout.getName()));
        } catch (Exception ex) {
            if (fout != null) {
                fout.delete();
            }
            returnValue.put(fin.getName(), new ResultInfo(400, ex.getMessage()));
        }
    }
}
