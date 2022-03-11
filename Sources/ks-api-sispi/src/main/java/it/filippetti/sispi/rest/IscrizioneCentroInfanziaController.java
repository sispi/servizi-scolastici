package it.filippetti.sispi.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.filippetti.sispi.centroinfanzia.DettaglioIscrizioneCentroInfanziaBean;
import it.filippetti.sispi.centroinfanzia.IscrizioneCentroInfanziaBean;
import it.filippetti.sispi.centroinfanzia.PagamentoCentroInfanziaBean;
import it.filippetti.sispi.model.CentroInfanziaLog;
import it.filippetti.sispi.model.CentroInfanziaTariffe;
import it.filippetti.sispi.model.DettaglioIscrizioneCentroInfanzia;
import it.filippetti.sispi.model.GiorniFrequenzaIstituto;
import it.filippetti.sispi.model.IscrizioneCentroInfanzia;
import it.filippetti.sispi.pagamentoretta.DateUtils;
import it.filippetti.sispi.pagamentoretta.DettaglioMinoreBean;
import it.filippetti.sispi.service.GiorniFrequenzaIstitutoService;
import it.filippetti.sispi.service.IscrizioneCentroInfanziaService;

@RestController
@RequestMapping(path = "/centroinfanzia", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class IscrizioneCentroInfanziaController {

	private static final Logger logger = LoggerFactory.getLogger(IscrizioneCentroInfanziaController.class);

	@Autowired
	private IscrizioneCentroInfanziaService iscrizioneCentroInfanziaService;

	@Autowired
	private GiorniFrequenzaIstitutoService giorniFrequenzaIstitutoService;

	@RequestMapping(value = "/getConfermateByCfMinore/{cfMinore}", method = { RequestMethod.GET })
	public ResponseEntity<List<IscrizioneCentroInfanzia>> getCentroInfanziaByCfMinore(@PathVariable String cfMinore,
			@RequestParam(name = "annoScolastico", required = false) String annoScolastico) {
		if (annoScolastico == null) {
			annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		}
		final List<IscrizioneCentroInfanzia> listaIstanze = iscrizioneCentroInfanziaService
				.getListaIstanzaConfermate(cfMinore, annoScolastico);
		return ResponseEntity.ok(listaIstanze);
	}

	@RequestMapping(value = "/getNotificataByCfMinore/{cfMinore}", method = { RequestMethod.GET })
	public ResponseEntity<IscrizioneCentroInfanzia> getCentroInfanziaNotificataByCfMinore(@PathVariable String cfMinore,
			@RequestParam(name = "annoScolastico", required = false) String annoScolastico) {
		if (annoScolastico == null) {
			annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		}
		final Optional<IscrizioneCentroInfanzia> result = iscrizioneCentroInfanziaService.getIstanzaNotificata(cfMinore,
				annoScolastico);
		return ResponseEntity.ok(result.orElse(null));
	}

	@RequestMapping(value = "/aggiornarate/{cfMinore}", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> aggiornaRateCentroInfanzia(@PathVariable String cfMinore,
			@RequestParam(name = "utenteModifica", required = true) String utenteModifica,
			@RequestParam(name = "istanzaOriginale", required = false) String istanzaOriginale,
			@RequestParam(name = "annoScolastico", required = false) String annoScolastico,
			@RequestParam(name = "causale", required = false) String causale) {
		logger.info("aggiornaRateMinore: {}", cfMinore);
		if (annoScolastico == null) {
			annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		}
		logger.info("aggiornaRateMinore: {}", annoScolastico);
		final List<DettaglioIscrizioneCentroInfanzia> listaRate = iscrizioneCentroInfanziaService
				.ricalcolaRateIscritto(cfMinore, utenteModifica, annoScolastico, istanzaOriginale, causale);
//		iscrizioneCentroInfanziaService.ricalcolaRateFamiglia(cfMinore, utenteModifica, annoScolastico);
		final Map<String, Object> result = new HashMap<>();
		result.put("resultMessage", "OK");
		result.put("result", listaRate);
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/rigeneraRateMinore", method = { RequestMethod.GET })
	public void rigeneraRateMinore(@RequestParam(name = "cfMinore", required = false) String cfMinore,
			@RequestParam(name = "nomeIstituto", required = false) String nomeIstituto,
			@RequestParam(name = "fascia", required = false) String fascia,
			@RequestParam(name = "fasciaOraria", required = false) String fasciaOraria,
			@RequestParam(name = "annoScolastico", required = false) String annoScolastico,
			@RequestParam(name = "causale", required = false) String causale) {
		logger.info("aggiornaRateMinore: {}", cfMinore);
		if (annoScolastico == null) {
			annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		}
		logger.info("aggiornaRateMinore: {}", annoScolastico);
		iscrizioneCentroInfanziaService.rigeneraRateMinore(cfMinore, annoScolastico, nomeIstituto, fascia,
				fasciaOraria);
	}

//	rigeneraRateMinore

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ResponseEntity<IscrizioneCentroInfanzia> salvaIscrizioneCentroInfanzia(
			@RequestBody IscrizioneCentroInfanziaBean inputData) {
		final IscrizioneCentroInfanzia result = iscrizioneCentroInfanziaService.salvaDati(inputData);
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/saveDettaglio", method = { RequestMethod.POST })
	public ResponseEntity<DettaglioIscrizioneCentroInfanzia> salvaDettaglioIscrizioneCentroInfanzia(
			@RequestBody DettaglioIscrizioneCentroInfanziaBean inputData,
			@RequestParam(name = "salvaSuccessivi", required = false, defaultValue = "0") Integer salvaSuccessivi,
			@RequestParam(name = "utenteModifica", required = true) String utenteModifica) {
		DettaglioIscrizioneCentroInfanzia result = null;
		if (1 == salvaSuccessivi) {
			result = iscrizioneCentroInfanziaService.salvaDettaglioSuccessivi(inputData, utenteModifica);
		} else {
			result = iscrizioneCentroInfanziaService.salvaDettaglio(inputData, utenteModifica);
		}

		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/inserisciGiorniFrequenza", method = { RequestMethod.GET })
	public ResponseEntity<List<GiorniFrequenzaIstituto>> inserisciGiorniFrequenza(
			@RequestParam(name = "annoScolastico", required = false) String annoScolastico) {
		if (annoScolastico == null) {
			annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		}
		logger.info("inseriscigiornifrequenza anno: {}", annoScolastico);
		final List<GiorniFrequenzaIstituto> res = giorniFrequenzaIstitutoService
				.inserisciDatiPerAnnoScolastico(annoScolastico);
		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/listaGiorniFrequenza", method = { RequestMethod.GET })
	public ResponseEntity<List<GiorniFrequenzaIstituto>> getListaGiorniFrequenza() {
		logger.info("getListaGiorniFrequenza");
		final List<GiorniFrequenzaIstituto> res = giorniFrequenzaIstitutoService.getAll();
		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/prossimarata/{cfMinore}", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Serializable>> getProssimaRataCentroInfanzia(@PathVariable String cfMinore,
			@RequestParam(name = "annoScolastico", required = false) String annoScolastico) {
		final Map<String, Serializable> result = iscrizioneCentroInfanziaService.prossimaRata(cfMinore, annoScolastico);
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/prossimarata/{cfMinore}/{annoScolastico}", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Serializable>> getProssimaRataCentroInfanziaByAnnoScolastico(
			@PathVariable String cfMinore, @PathVariable Integer annoScolastico) {
		final Map<String, Serializable> result = iscrizioneCentroInfanziaService.prossimaRata(cfMinore,
				DateUtils.getAnnoScolasticoByAnno(annoScolastico));
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/listafigli/{cfContribuente}", method = { RequestMethod.GET })
	public ResponseEntity<List<DettaglioMinoreBean>> getListaDettaglioFigliCentroInfanzia(
			@PathVariable String cfContribuente,
			@RequestParam(name = "annoScolastico", required = false) String annoScolastico) {
		logger.info("listafigli: {}", cfContribuente);
		if (annoScolastico == null) {
			annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		}
		final List<DettaglioMinoreBean> result = iscrizioneCentroInfanziaService.getListaFigli(cfContribuente,
				annoScolastico);
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/listafigli/{cfContribuente}/{annoScolastico}", method = { RequestMethod.GET })
	public ResponseEntity<List<DettaglioMinoreBean>> getListaDettaglioFigliCentroInfanziaByAnnoScolastico(
			@PathVariable String cfContribuente, @PathVariable Integer annoScolastico) {
		logger.info("getListaDettaglioFigliByAnnoScolastico: {} - {}", cfContribuente, annoScolastico);
		final List<DettaglioMinoreBean> result = iscrizioneCentroInfanziaService.getListaFigli(cfContribuente,
				DateUtils.getAnnoScolasticoByAnno(annoScolastico));
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/savePagamento", method = { RequestMethod.POST })
	public ResponseEntity<DettaglioIscrizioneCentroInfanzia> salvaPagamentoIscrizioneCentroInfanzia(
			@RequestBody PagamentoCentroInfanziaBean inputData) {
		logger.info("salvaPagamentoIscrizioneCentroInfanzia: {} ", inputData);
		final DettaglioIscrizioneCentroInfanzia result = iscrizioneCentroInfanziaService.salvaPagamento(inputData);
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/listatariffe", method = { RequestMethod.GET })
	public ResponseEntity<List<CentroInfanziaTariffe>> getListaTariffeCentriScolastici() {
		logger.info("getListaTariffeCentriScolastici");
		final List<CentroInfanziaTariffe> result = iscrizioneCentroInfanziaService.getListaTariffeCentriScolastici();
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/getDettaglioLog", method = { RequestMethod.GET })
	public ResponseEntity<CentroInfanziaLog> getDettaglioLog(
			@RequestParam(name = "idRecord", required = true) Long idRecord) {
		logger.info("getDettaglioLog - {}", idRecord);
		final Optional<CentroInfanziaLog> result = iscrizioneCentroInfanziaService.getDatiLogCentroInfanzia(idRecord);
		return ResponseEntity.ok(result.orElse(null));
	}

	@RequestMapping(value = "/get", method = { RequestMethod.GET })
	public ResponseEntity<IscrizioneCentroInfanzia> getDatiIscrizione(
			@RequestParam(name = "idRecord", required = true) Long idRecord) {
		logger.info("getDatiIscrizione - {}", idRecord);
		final Optional<IscrizioneCentroInfanzia> result = iscrizioneCentroInfanziaService.findById(idRecord);
		return ResponseEntity.ok(result.orElse(null));
	}

	@RequestMapping(value = "/getDettaglio", method = { RequestMethod.GET })
	public ResponseEntity<List<DettaglioIscrizioneCentroInfanzia>> getDatiIscrizioneDettaglio(
			@RequestParam(name = "idRecord", required = false) Long idRecord,
			@RequestParam(name = "idIscrizione", required = false) Long idIscrizione) {
		logger.info("getDatiIscrizioneDettaglio - {}", idRecord);
		List<DettaglioIscrizioneCentroInfanzia> result = new ArrayList<>();
		if (idRecord != null) {
			final Optional<DettaglioIscrizioneCentroInfanzia> resultById = iscrizioneCentroInfanziaService
					.findDettaglioById(idRecord);
			if (resultById.isPresent()) {
				result.add(resultById.get());
			}
		} else if (idIscrizione != null) {
			result = iscrizioneCentroInfanziaService.findDettaglioByIdIstanza(idIscrizione);
		}

		return ResponseEntity.ok(result);
	}

}
