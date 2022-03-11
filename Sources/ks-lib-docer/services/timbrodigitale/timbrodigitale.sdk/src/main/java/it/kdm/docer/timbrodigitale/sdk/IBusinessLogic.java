package it.kdm.docer.timbrodigitale.sdk;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

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
	
	/**
	 * Applica il timbro al documento pdf specificato in base ai parametri specificati.
	 * 
	 * @param timbro Un array di byte che compone l'immagine del timbro digitale
	 * @param pdf Un array di byte che compone il pdf su cui applicare il timbro digitale
	 * @param pagina La pagina del pdf su cui applicare il timbro digitale
	 * @param x La coordinata x su cui applicare sul timbro digitale
	 * @param y La coordinata y su cui applicare sul timbro digitale
	 * 
	 * @return Un array di byte che compone il pdf con il timbro apposto
	 */

    public File applicaTimbro(InputStream timbro, InputStream pdf, int pagina, int x, int y) throws BusinessLogicException;

	
	public byte[] getTimbro(byte[] data,ImageFormat imgFormat,int imgMaxH, int imgMaxW, int imgDPI, Map<String,String> parameters) throws BusinessLogicException;
}
