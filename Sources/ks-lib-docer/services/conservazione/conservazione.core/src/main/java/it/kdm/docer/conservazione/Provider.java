package it.kdm.docer.conservazione;

import org.apache.axiom.om.OMElement;

public interface Provider {

	public static enum TipoConservazione {
		SOSTITUTIVA,
		FISCALE
	}
	
	ConservazioneResult versamento(OMElement xmlDocument, File[] files,
			TipoConservazione tipoConservazione, boolean forzaAccettazione, 
			boolean forzaConservazione, boolean forzaCollegamento) throws ConservazioneException;

	ConservazioneResult modificaMetadati(OMElement doc,
			TipoConservazione tipo) throws ConservazioneException;

	ConservazioneResult aggiungiDocumento(OMElement xmlDocument,
			File file, TipoConservazione tipo, boolean forzaAccettazione, 
			boolean forzaConservazione, boolean forzaCollegamento) throws ConservazioneException;
	
	
	
}
