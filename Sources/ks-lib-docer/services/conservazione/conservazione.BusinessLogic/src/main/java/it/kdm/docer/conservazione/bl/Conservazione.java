package it.kdm.docer.conservazione.bl;

import it.kdm.docer.conservazione.ConservazioneException;
import it.kdm.docer.conservazione.ConservazioneResult;
import it.kdm.docer.conservazione.File;
import it.kdm.docer.conservazione.Provider;
import it.kdm.docer.conservazione.converter.Converter;

import org.apache.axiom.om.OMElement;

public class Conservazione implements IConservazioneBL {
		
	private Converter converter;
	private Provider provider;
	
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Converter getConverter() {
		return converter;
	}

	public void setConverter(Converter converter) {
		this.converter = converter;
	}

	public ConservazioneResult versamento(OMElement xmlDocumento, File[] files,
			String tipoDocumento, String applicazioneChiamante, TipoConservazione tipoConservazione,
			boolean forzaAccettazione, boolean forzaConservazione,
			boolean forzaCollegamento, String codEnte, String codAoo) throws ConservazioneException {
		
		//if(!tipoDocumento.equalsIgnoreCase(IConservazioneBL.DEFAULT_TYPE)) {
		xmlDocumento = this.converter.converter(xmlDocumento, tipoDocumento, codEnte, codAoo);
		//}
		
		it.kdm.docer.conservazione.Provider.TipoConservazione tipo = 
				it.kdm.docer.conservazione.Provider.TipoConservazione.valueOf(tipoConservazione.toString());
		return this.provider.versamento(xmlDocumento, files, tipo, forzaAccettazione, 
					forzaConservazione, forzaCollegamento);
	}

	@Override
	public ConservazioneResult modificaMetadati(OMElement doc,
			String tipoDocumento, String applicazioneChiamante,
			TipoConservazione tipoConservazione, String codEnte, String codAoo) throws ConservazioneException {
		
		doc = this.converter.converter(doc, tipoDocumento, codEnte, codAoo);
		it.kdm.docer.conservazione.Provider.TipoConservazione tipo = 
				it.kdm.docer.conservazione.Provider.TipoConservazione.valueOf(tipoConservazione.toString());
		
		return this.provider.modificaMetadati(doc, tipo);
		
	}

	@Override
	public ConservazioneResult aggiungiDocumento(OMElement doc,
			String tipoDocumento, String applicazioneChiamante,
			TipoConservazione tipoConservazione, boolean forzaAccettazione,
			boolean forzaConservazione, boolean forzaCollegamento, File file,
												 String codEnte, String codAoo) throws ConservazioneException {
		
		doc = this.converter.converter(doc, tipoDocumento, codEnte, codAoo);
		it.kdm.docer.conservazione.Provider.TipoConservazione tipo = 
				it.kdm.docer.conservazione.Provider.TipoConservazione.valueOf(tipoConservazione.toString());
		
		return this.provider.aggiungiDocumento(doc, file, tipo, forzaAccettazione, 
				forzaConservazione, forzaCollegamento);
	}

	
}
