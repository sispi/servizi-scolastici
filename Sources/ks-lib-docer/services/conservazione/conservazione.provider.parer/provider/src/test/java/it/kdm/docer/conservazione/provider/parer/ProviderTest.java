package it.kdm.docer.conservazione.provider.parer;

import it.eng.parer.ccd.lib.IParerLib;
import it.eng.parer.ws.versamento.dto.FileBinario;
import it.eng.parer.ws.xml.versReq.UnitaDocAggAllegati;
import it.eng.parer.ws.xml.versReq.UnitaDocumentaria;
import it.eng.parer.ws.xml.versResp.EsitoGenerale;
import it.eng.parer.ws.xml.versResp.EsitoVersAggAllegati;
import it.eng.parer.ws.xml.versResp.EsitoVersamento;
import it.eng.parer.ws.xml.versResp.types.ECEsitoExtType;
import it.kdm.docer.conservazione.ConservazioneException;
import it.kdm.docer.conservazione.ConservazioneResult;
import it.kdm.docer.conservazione.File;
import it.kdm.docer.conservazione.Provider.TipoConservazione;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProviderTest {

	static ParerLibMockup mockup;
	static ProviderParer providerParer;
	
	static OMElement minimal, minimalMixed, full;

	@BeforeClass
	static public void setupMockups() throws ConservazioneException, XMLStreamException {
		providerParer = new ProviderParer();
		mockup = new ParerLibMockup();
		providerParer.setParerLib(mockup);
		
		minimal = parseDoc("minimal.xml");
		minimalMixed = parseDoc("mixed-minimal.xml");
		full = parseDoc("full.xml");
	}
	
	private static OMElement parseDoc(String filename) throws XMLStreamException {
		
		InputStream stream = ProviderTest.class.getResourceAsStream("/" + filename);
		StAXOMBuilder builder = new StAXOMBuilder(stream);
		return builder.getDocumentElement();
	}
	
	@Test
	public void positiveOutcomeTest() throws Exception {
		
		EsitoVersamento esito = EsitoVersamento.unmarshal(new InputStreamReader(
				this.getClass().getResourceAsStream("/response.xml")));
		
		mockup.setEsito(esito);
		ConservazioneResult result = providerParer.versamento(minimal, new File[0], TipoConservazione.SOSTITUTIVA,
				true, true, true);
		Assert.assertEquals("0", result.getErrorCode());
	}
	
	@Test
	public void negativeOutcomeTest() throws Exception {
		EsitoVersamento esito = EsitoVersamento.unmarshal(new InputStreamReader(
				this.getClass().getResourceAsStream("/response.xml")));
		esito.getEsitoGenerale().setCodiceEsito(ECEsitoExtType.NEGATIVO);
		
		mockup.setEsito(esito);
		ConservazioneResult result = providerParer.versamento(minimal, new File[0], TipoConservazione.SOSTITUTIVA,
				true, true, true);
		Assert.assertEquals("-1", result.getErrorCode());
	}
	
	@Test
	public void warningOutcomeTest() throws Exception {
		EsitoVersamento esito = EsitoVersamento.unmarshal(new InputStreamReader(
				this.getClass().getResourceAsStream("/response.xml")));
		esito.getEsitoGenerale().setCodiceEsito(ECEsitoExtType.WARNING);
		
		mockup.setEsito(esito);
		ConservazioneResult result = providerParer.versamento(minimal, new File[0], TipoConservazione.SOSTITUTIVA,
				true, true, true);
		Assert.assertEquals("0", result.getErrorCode());
	}
}

class ParerLibMockup implements IParerLib {

	private EsitoVersamento esito;

	public ParerLibMockup() {
		EsitoVersamento esito = new EsitoVersamento();
		EsitoGenerale esitoGenerale = new EsitoGenerale();
		esitoGenerale.setCodiceErrore("0");
		esitoGenerale.setCodiceEsito(ECEsitoExtType.POSITIVO);
		esitoGenerale.setMessaggioErrore("success");
		esito.setEsitoGenerale(esitoGenerale);

		this.esito = esito;
	}

	public EsitoVersamento getEsito() {
		return esito;
	}

	public void setEsito(EsitoVersamento esito) {
		this.esito = esito;
	}

	public EsitoVersamento versamento(String username, String password,
			UnitaDocumentaria versamento, Vector<FileBinario> fileBinari) 
					throws Exception {
		return this.esito;
	}

	@Override
	public EsitoVersAggAllegati modificaMetadati(String username, String password,
			UnitaDocAggAllegati ud) throws Exception {
		return null;
	}

	@Override
	public EsitoVersAggAllegati aggiungiDocumento(String username,
			String password, UnitaDocAggAllegati ud, FileBinario file)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}