package it.kdm.docer.conservazione.bl;

import it.kdm.docer.conservazione.ConservazioneException;
import it.kdm.docer.conservazione.ConservazioneResult;
import it.kdm.docer.conservazione.File;

import it.kdm.docer.sdk.exceptions.DocerException;
import org.apache.axiom.om.OMElement; // Apache Axiom 1.2.8

public interface IConservazioneBL {
	
	public final static String DEFAULT_TYPE = "raw";
	
	public enum TipoConservazione {
		SOSTITUTIVA,
		FISCALE
	}

	public ConservazioneResult versamento(OMElement xmlDocumento, File[] files,
			String tipoDocumento, String applicazioneChiamante, TipoConservazione tipoConservazione,
			boolean forzaAccettazione, boolean forzaConservazione,
			boolean forzaCollegamento, String codEnte, String codAoo) throws ConservazioneException;

	public ConservazioneResult modificaMetadati(OMElement doc,
			String tipoDocumento, String applicazioneChiamante,
			TipoConservazione tipoConservazione, String codEnte, String codAoo) throws ConservazioneException;

	public ConservazioneResult aggiungiDocumento(OMElement doc,
			String tipoDocumento, String applicazioneChiamante,
			TipoConservazione tipoConservazione, boolean forzaAccettazione,
			boolean forzaConservazione, boolean forzaCollegamento,
			File file, String codEnte, String codAoo) throws ConservazioneException;
}
