package it.kdm.docer.digitalstamp.sdk;

import javax.activation.DataHandler;

/**
 * La Business Logic implementa la logica di applicazione del timbro su una pagina di un 
 * documento pdf. Tale logica deve essere indipendente dalla appliance utilizzata per la 
 * generazione del timbro, pertanto anche se SecureEdge implementa simili funzionalita', 
 * esse non possono essere utilizzate in quanto non  e' garantito che lo stesso discorso 
 * sia applicabile ad un altra appliance di timbratura digitale.
 * 
 * @author Lorenzo Lucherini
 *
 */
public interface IBusinessLogic {

	public boolean writeConfig(String token, String xml)
			throws DigitalStampException;

	public String readConfig(String token)
			throws DigitalStampException;


	public DataHandler AddStampAndSign(String ente, String aoo, DataHandler documento, KeyValuePair[] metadata, int stampPage, int positionX, int positionY, String positionTag, String correctionLevel,
									   String signer, String pin, String otp, int action, int qrsize, boolean isShortnedURL, String url,
									   String typeDoc, String templateName, String locale, String title, String exDate) throws BusinessLogicException;

}
