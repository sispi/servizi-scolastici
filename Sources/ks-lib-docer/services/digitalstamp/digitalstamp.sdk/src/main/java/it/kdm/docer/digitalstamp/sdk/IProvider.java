package it.kdm.docer.digitalstamp.sdk;

import javax.activation.DataHandler;
import java.util.HashMap;
import java.util.Properties;

/**
 * Il provider comunica direttamente con una appliance di timbratura digiale. Per rimanere 
 * indipendenti dalla appliance utilizzata, l'unica funzionalita' richiesta e' la generazione 
 * di un timbro digitale contenente i dati forniti in chiamata.
 *
 * @author Lorenzo Lucherini
 *
 */
public interface IProvider {
	
	/**
	 * Imposta la configurazione del provider corrente.
	 * Non esiste specifica o documentazione. Ogni appliance richiedera' configurazioni
	 * differenti ed e' compito di chi implementa il provider fornire tale documentazione.
	 *
	 * @param conf Le coppie chiave-valore di configurazione
	 */
	public void setConfiguration(Properties conf) throws ProviderException;
	
	/*
	 * Legge le proprieta' di configurazione
	 * */
	public Properties getConfiguration() throws ProviderException;

	/*public DataHandler AddStampAndSign(String ente, String aoo, DataHandler documento, KeyValuePair[] metadata, int stampPage, int positionX, int positionY, String positionTag, String correctionLevel,
									   String signer, String pin, String otp, int action, int qrsize, boolean isShortnedURL, String url,
									   String typeDoc, String templateName, String locale, String title, String exDate)throws ProviderException;
*/
	public DataHandler AddStampAndSign(DataHandler documento, String dataProvider)throws ProviderException;
    public DataHandler AddStampAndSignbyDocnum(DataHandler documento, String dataProvider, HashMap<String,String> otherMetadata)throws ProviderException;




}
