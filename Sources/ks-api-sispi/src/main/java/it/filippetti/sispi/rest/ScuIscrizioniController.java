package it.filippetti.sispi.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.filippetti.sispi.iscrizioni.ImportIscrizioniBean;
import it.filippetti.sispi.model.ScuIscrizioni;
import it.filippetti.sispi.service.ScuIscrizioniService;

@RestController
@RequestMapping(path = "/iscrizioni", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScuIscrizioniController {

	private static final Logger logger = LoggerFactory.getLogger(IseeController.class);

	@Autowired
	private ScuIscrizioniService scuIscrizioniService;

	@RequestMapping(value = "/lista", method = { RequestMethod.GET })
	public ResponseEntity<List<ScuIscrizioni>> getListaIscrizioniAsiloValide() {
		logger.info("getListaIscrizioniAsiloValide");
		final List<ScuIscrizioni> res = scuIscrizioniService.getListaIscrizioniAsiloValide();
		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/aggiornaIsee", method = { RequestMethod.GET })
	public ResponseEntity<ImportIscrizioniBean> aggiornaIseeIscrizioni2021(
			@RequestParam(name = "cfMinore", required = false) String cfMinore) {
		logger.info("aggiornaIseeIscrizioni2021");
		final ImportIscrizioniBean res = scuIscrizioniService.aggiornaIsee(cfMinore);
		return ResponseEntity.ok(res);
	}

}
