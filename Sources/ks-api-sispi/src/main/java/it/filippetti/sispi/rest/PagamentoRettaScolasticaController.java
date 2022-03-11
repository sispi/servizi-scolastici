package it.filippetti.sispi.rest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.filippetti.sispi.model.Istanza;
import it.filippetti.sispi.model.Modalita;
import it.filippetti.sispi.model.Pagamento;
import it.filippetti.sispi.model.Rata;
import it.filippetti.sispi.pagamentoretta.DateUtils;
import it.filippetti.sispi.pagamentoretta.DettaglioMinoreBean;
import it.filippetti.sispi.repository.IstanzaRepository;
import it.filippetti.sispi.repository.RataRepository;
import it.filippetti.sispi.service.PagamentoService;
import it.filippetti.sispi.service.RataService;

@RestController
@RequestMapping(path = "/pagamentoretta", produces = MediaType.APPLICATION_JSON_VALUE)
public class PagamentoRettaScolasticaController {

	private static final Logger logger = LoggerFactory.getLogger(PagamentoRettaScolasticaController.class);

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private IstanzaRepository istanzaRepository;

	@Autowired
	private RataRepository rataRepository;

	@Autowired
	private RataService rataService;

	@RequestMapping(value = "/listafigli/{cfContribuente}", method = { RequestMethod.GET })
	public ResponseEntity<List<DettaglioMinoreBean>> getListaDettaglioFigli(@PathVariable String cfContribuente,
			@RequestParam(name = "annoScolastico", required = false) String annoScolastico) {
		logger.info("listafigli: {}", cfContribuente);
		cfContribuente = cfContribuente.toUpperCase();
		if (annoScolastico == null) {
			annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		}
		logger.info("annoScolastico: {}" + annoScolastico);
		final List<DettaglioMinoreBean> result = rataService.getListaFigli(cfContribuente, annoScolastico);
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/prossimarata/{cfMinore}", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Serializable>> getProssimaRata(@PathVariable String cfMinore,
			@RequestParam(name = "annoScolastico", required = false) String annoScolastico) {
		final HashMap<String, Serializable> obj = new HashMap<String, Serializable>();
		logger.info("prossimarata: {}" + cfMinore);
		cfMinore = cfMinore.toUpperCase();
		if (annoScolastico == null) {
			annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		}
		logger.info("annoScolastico: {}" + annoScolastico);

		// FIXED: anno_scolastico
		final Istanza istanza = istanzaRepository.findOneByConfermaNotificataAndCfMinoreAndAnnoScolastico("S", cfMinore,
				annoScolastico);
		if (istanza == null) {
			logger.info("Codice fiscale non trovato");
			obj.put("esito", "Codice fiscale non trovato");
		} else {
			logger.info("Dati trovati: {}", istanza);
			// FIXED: anno_scolastico
			final List<Rata> rata = rataRepository.findProssimaRataByCfIscrittoAndAnnoScolastico(cfMinore,
					annoScolastico);
			obj.put("nome", istanza.getNomeMinore());
			obj.put("cognome", istanza.getCognomeMinore());
			obj.put("codicefiscale", istanza.getCfMinore());
			obj.put("importo", "0");
			if (rata == null || !rata.iterator().hasNext()) {
				obj.put("esito", "Nessuna rata da pagare");
			} else {
				Rata prossimaRata = null;
				for (Rata singolaRata : rata) {
					if (singolaRata.getImportoTotale() > 0) {
						prossimaRata = singolaRata;
						break;
					}
				}
				if (prossimaRata != null) {
					logger.info("Prossima rata: {}", prossimaRata);
					obj.put("importo", prossimaRata.getImportoTotale().toString());
					obj.put("esito", "Dati recuperati con successo");
					obj.put("rata", prossimaRata);
				} else {
					// TODO: gestire caso senza rata
				}

			}
		}
		return ResponseEntity.ok(obj);
	}

	@RequestMapping(value = "/aggiornarate/{cfMinore}", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> aggiornaRateMinore(@PathVariable String cfMinore,
			@RequestParam(name = "utenteModifica", required = true) String utenteModifica,
			@RequestParam(name = "istanzaOriginale", required = false) String istanzaOriginale,
			@RequestParam(name = "annoScolastico", required = false) String annoScolastico,
			@RequestParam(name = "causale", required = false) String causale) {
		logger.info("aggiornaRateMinore: {}", cfMinore);
		if (annoScolastico == null) {
			annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		}
		logger.info("aggiornaRateMinore: {}", annoScolastico);
		final List<Rata> listaRate = rataService.ricalcolaRateIscritto(cfMinore, utenteModifica, annoScolastico,
				istanzaOriginale, causale);
		rataService.ricalcolaRateFamiglia(cfMinore, utenteModifica, annoScolastico);
		final Map<String, Object> result = new HashMap<>();
		result.put("resultMessage", "OK");
		result.put("result", listaRate);
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/aggiornarata", method = { RequestMethod.POST })
	public ResponseEntity<Map<String, Object>> aggiornaRata(@RequestBody Rata inputData,
			@RequestParam(name = "utenteModifica", required = true) String utenteModifica) {
		logger.info("aggiornaRata: {}", inputData);
		final Rata rata = rataService.aggiornaRata(inputData, utenteModifica);
		final Map<String, Object> result = new HashMap<>();
		result.put("resultMessage", "OK");
		result.put("result", rata);
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/pagamentirettascolastica", method = { RequestMethod.GET })
	public ResponseEntity<List<Pagamento>> getListaPagamentiRetta(@RequestParam(required = false) Long id,
			@RequestParam(required = false) String cfMinore, @RequestParam(required = false) String importo,
			@RequestParam(required = false) String cfContribuente,
			@RequestParam(required = false) String docnumPrincipale, @RequestParam(required = false) Modalita modalita,
			@RequestParam(required = false) Long idRata, @RequestParam(required = false) String nomeIstituto,
			@RequestParam(required = false) String descrizione) {
		final Map<String, Object> listaFiltri = new HashMap<>();
		if (id != null) {
			listaFiltri.put("id", id);
		}

		if (StringUtils.hasText(cfMinore)) {
			listaFiltri.put("cfMinore", cfMinore);
		}

		if (StringUtils.hasText(importo)) {
			listaFiltri.put("importo", importo);
		}

		if (StringUtils.hasText(cfContribuente)) {
			listaFiltri.put("cfContribuente", cfContribuente);
		}

		if (StringUtils.hasText(docnumPrincipale)) {
			listaFiltri.put("docnumPrincipale", docnumPrincipale);
		}

		if (modalita != null) {
			listaFiltri.put("modalita", modalita);
		}

		if (idRata != null) {
			listaFiltri.put("idRata", idRata);
		}

		if (StringUtils.hasText(nomeIstituto)) {
			listaFiltri.put("nomeIstituto", nomeIstituto);
		}

		if (StringUtils.hasText(descrizione)) {
			listaFiltri.put("descrizione", descrizione);
		}

		logger.info("pagamentirettascolastica: {}", listaFiltri);
		List<Pagamento> result = pagamentoService.findAll(listaFiltri);
		return ResponseEntity.ok(result);

	}

	@RequestMapping(value = "/listarate/{cfMinore}", method = { RequestMethod.GET })
	public ResponseEntity<List<Rata>> getRataByCfMinore(@PathVariable String cfMinore) {
		logger.info("getRataByCfMinore: {}", cfMinore);
		final List<Rata> result = rataRepository.findByCfIscrittoOrderByAnnoScolasticoAscSortAsc(cfMinore);
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/rata/{idRata}", method = { RequestMethod.GET })
	public ResponseEntity<Rata> getRataById(@PathVariable Long idRata) {
		logger.info("getRataById: {}", idRata);
		final Rata result = rataService.getRataById(idRata);
		return ResponseEntity.ok(result);
	}

}
