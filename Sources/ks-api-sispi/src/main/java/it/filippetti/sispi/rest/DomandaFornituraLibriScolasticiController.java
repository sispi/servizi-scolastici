package it.filippetti.sispi.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.filippetti.sispi.libriscolastici.DomandaFornituraBean;
import it.filippetti.sispi.libriscolastici.ScuolaBean;
import it.filippetti.sispi.libriscolastici.VerificaDomandaBean;
import it.filippetti.sispi.model.DomandaFornituraLibri;
import it.filippetti.sispi.pagamentoretta.DateUtils;
import it.filippetti.sispi.repository.DomandaFornituraLibriRepository;
import it.filippetti.sispi.service.DomandaFornituraLibriService;
import it.filippetti.sispi.service.PortaleScuolaService;

@RestController
@RequestMapping(path = "/libriscolastici", produces = MediaType.APPLICATION_JSON_VALUE)
public class DomandaFornituraLibriScolasticiController {

	private static final Logger logger = LoggerFactory.getLogger(DomandaFornituraLibriScolasticiController.class);

	@Autowired
	private DomandaFornituraLibriRepository domandaFornituraLibriRepository;

	@Autowired
	private PortaleScuolaService portalescuolaService;

	@Autowired
	private DomandaFornituraLibriService domandaFornituraLibriService;

	@RequestMapping(value = "/getalunno/{cfAlunno}", method = { RequestMethod.GET })
	public ResponseEntity<DomandaFornituraBean> getListaDomandeFornitura(@PathVariable("cfAlunno") String cfAlunno,
			@RequestParam(name = "cf", required = true) String cfRichiedente,
			@RequestParam(name = "annoScolastico", required = false) String annoScolastico) {
		if (annoScolastico == null) {
			annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		}
		logger.info("Ricerca alunno - cfAlunno: {} - cfRichiedente: {} - annoScolastico: {}", cfAlunno, cfRichiedente,
				annoScolastico);

		cfAlunno = cfAlunno.toUpperCase();
		cfRichiedente = cfRichiedente.toUpperCase();
		DomandaFornituraBean result = portalescuolaService.getDatiDomandaFornitura(cfAlunno, cfRichiedente);
		if (result == null) {
			// Non ho trovato il cfAlunno
			result = new DomandaFornituraBean();
			result.setEsito("2");
			result.setCodicefiscale(cfAlunno);
			result.setAnno_scolastico(annoScolastico);
		} else if (domandaFornituraLibriService.isAlunnoMaggiorenne(result)) {
			// Alunno maggiorenne
			result = new DomandaFornituraBean();
			result.setEsito("3");
			result.setCodicefiscale(cfAlunno);
			result.setAnno_scolastico(annoScolastico);
		} else {
			// Cerco domande di rimborso attive
			final List<DomandaFornituraLibri> listaRisultati = domandaFornituraLibriRepository
					.findByCfMinoreAndAnnoScolastico(cfAlunno, annoScolastico);
			if (domandaFornituraLibriService.getDomandaAttiva(listaRisultati) != null) {
				result = new DomandaFornituraBean();
				result.setEsito("1");
				result.setCodicefiscale(cfAlunno);
				result.setAnno_scolastico(annoScolastico);
			} else {
				result.setEsito("0");
				result.setAnno_scolastico(annoScolastico);
			}
		}
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/elencoscuole", method = { RequestMethod.GET })
	public ResponseEntity<List<ScuolaBean>> getListaScuole() {
		logger.info("elencoscuole");
		final List<ScuolaBean> res = portalescuolaService.getElencoScuole();
//		final List<ScuolaBean> res = DomandaFornituraDatiTest.getListaScuoleTest();
		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/verificaannullamento/{numeroProtocollo}/{annoProtocollo}", method = { RequestMethod.GET })
	public ResponseEntity<VerificaDomandaBean> verificaAnnullamento(
			@PathVariable("numeroProtocollo") String numeroProtocollo,
			@PathVariable("annoProtocollo") String annoProtocollo,
			@RequestParam(name = "cf", required = true) String cfRichiedente) {
		logger.info("verificaAnnullamento per richiedente: " + cfRichiedente);
		cfRichiedente = cfRichiedente.toUpperCase();
		final VerificaDomandaBean result = new VerificaDomandaBean();
		final List<DomandaFornituraLibri> listaRisultati = domandaFornituraLibriRepository
				.findByNumeroProtocolloAndAnnoProtocolloAndCfRichiedente(numeroProtocollo, annoProtocollo,
						cfRichiedente);
		final DomandaFornituraLibri domandaAttiva = domandaFornituraLibriService.getDomandaAttiva(listaRisultati);
		if (domandaAttiva != null) {
			result.setAnno_protocollo(domandaAttiva.getAnnoProtocollo());
			result.setCf_minore(domandaAttiva.getCfMinore());
			result.setCf_richiedente(domandaAttiva.getCfRichiedente());
			result.setData_presentazione(domandaAttiva.getDataPresentazione());
			result.setEsito("0");
			result.setNumero_protocollo(domandaAttiva.getNumeroProtocollo());
			result.setAnno_scolastico(domandaAttiva.getAnnoScolastico());
		} else {
			result.setEsito("1");
		}
		return ResponseEntity.ok(result);
	}

}
