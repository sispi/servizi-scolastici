package it.filippetti.sispi.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.filippetti.sispi.model.Istanza;
import it.filippetti.sispi.pagamentoretta.DateUtils;
import it.filippetti.sispi.repository.IstanzaRepository;
import it.filippetti.sispi.service.IscrizioneConstants;
import it.filippetti.sispi.service.IstanzaService;

@RestController
@RequestMapping(path = "/istanza", produces = MediaType.APPLICATION_JSON_VALUE)
public class IstanzaController {

	private static final Logger logger = LoggerFactory.getLogger(IseeController.class);

	@Autowired
	private IstanzaRepository istanzaRepository;

	@Autowired
	private IstanzaService istanzaService;

	@RequestMapping(value = "/getByCfMinoreAndAnnoScolastico", method = { RequestMethod.GET })
	public ResponseEntity<Istanza> getIstanzaByCfMinoreAndAnnoScolastico(
			@RequestParam(name = "cfMinoreAnagrafe", required = true) String cfMinoreAnagrafe,
			@RequestParam(name = "annoScolastico", required = true) String annoScolastico) {
		logger.info("getByCfMinoreAndAnnoScolastico by cfMinore: {} - annoScolastico: {}", cfMinoreAnagrafe,
				annoScolastico);
		final Istanza istanza = istanzaRepository.findOneByConfermaNotificataAndCfMinoreAndAnnoScolastico(
				IscrizioneConstants.NOTIFICATA_ISCRIZIONE_SI, cfMinoreAnagrafe.toUpperCase(), annoScolastico);
		return ResponseEntity.ok(istanza);
	}

	@RequestMapping(value = "/getConfermateByCfMinore/{cfMinore}", method = { RequestMethod.GET })
	public ResponseEntity<List<Istanza>> getIstanzaByCfMinore(@PathVariable String cfMinore) {
		final List<Istanza> listaIstanze = istanzaService.getListaIstanzaConfermate(cfMinore,
				DateUtils.getAnnoScolasticoAttuale());
		return ResponseEntity.ok(listaIstanze);
	}

	@RequestMapping(value = "/getById/{idIstanza}", method = { RequestMethod.GET })
	public ResponseEntity<Istanza> getIstanzaById(@PathVariable Long idIstanza) {
		logger.info("getById : {}", idIstanza);
		final Istanza istanza = istanzaRepository.findById(idIstanza).orElse(null);
		return ResponseEntity.ok(istanza);
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ResponseEntity<Istanza> salvaIsee(@RequestBody Istanza inputData) {
		final Istanza result = istanzaService.aggiornaIstanza(inputData);
		return ResponseEntity.ok(result);
	}
}
