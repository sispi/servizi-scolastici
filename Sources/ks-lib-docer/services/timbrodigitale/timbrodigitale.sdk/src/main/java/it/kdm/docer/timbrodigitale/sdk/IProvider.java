package it.kdm.docer.timbrodigitale.sdk;

import java.util.Map;
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
	
	/**
	 * Genera un timbro digitale contenente i dati forniti come array di byte.
	 * Si presume che il servizio del timbro digitale non entra nel merito del formato dei
	 * dati da comprimere all'interno del timbro stesso.
	 *
	 * @param data L'array di byte che compone i dati da comprimere nel timbro digitale
	 * 
	 * @return Un array di byte che compone l'immagine ritornata dal servizio digitale
	 */
	public byte[] getTimbro(byte[] data, ImageFormat imgFormat, int imgMaxH, int imgMaxW, int imgDPI, Map<String,String> params) throws ProviderException;		
	
}
