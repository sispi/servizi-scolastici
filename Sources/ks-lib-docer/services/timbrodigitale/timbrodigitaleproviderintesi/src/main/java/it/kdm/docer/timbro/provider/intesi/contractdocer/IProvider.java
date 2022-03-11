package it.kdm.docer.timbro.provider.intesi.contractdocer;

import it.kdm.docer.timbro.provider.intesi.TemplateDTO;
import it.kdm.docer.timbro.provider.intesi.TimbroIntesiDTO;
import it.kdm.docer.timbrodigitale.sdk.ProviderException;

import javax.activation.DataHandler;
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
	 *
	 */

	public Properties getConfiguration() throws ProviderException;
	
	public DataHandler applicaTimbro(TimbroIntesiDTO timbroParam) throws ProviderException;

	public TemplateDTO retrieveTemplate(TemplateDTO template) throws ProviderException;

	
}
