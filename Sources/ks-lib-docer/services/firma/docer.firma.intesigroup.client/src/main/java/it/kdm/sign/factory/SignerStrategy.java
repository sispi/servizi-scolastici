package it.kdm.sign.factory;

import it.kdm.sign.model.ResultInfo;
import it.pkbox.client.Envelope;
import it.pkbox.client.XMLEnvelope;
import it.pkbox.client.XMLReference;

import java.io.File;
import java.util.Map;

public interface SignerStrategy {

	void sign(String customerInfo, String alias, String pin, String formatoFirma, File fin, String authData, File fout, Envelope enve, Map<String, ResultInfo> returnValue, int accessPermissions);
	
	void xmlsign(XMLReference[] references, File fin, String signer, String pin, String signerPin, XMLEnvelope xmlEnve, Map<String, ResultInfo> returnValue, File fout);
}
