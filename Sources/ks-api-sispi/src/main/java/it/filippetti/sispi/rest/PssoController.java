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
import org.springframework.web.bind.annotation.RestController;

import it.filippetti.psso.model.ScuolaAlunno;
import it.filippetti.psso.model.TipoDieta;
import it.filippetti.psso.model.TipoDietaSpeciale;
import it.filippetti.psso.model.TipoRichiedente;
import it.filippetti.psso.service.PssoService;

@RestController
@RequestMapping(path = "/psso", produces = MediaType.APPLICATION_JSON_VALUE)
public class PssoController {

	private static final Logger logger = LoggerFactory.getLogger(PssoController.class);

	@Autowired
	private PssoService pssoService;

	@RequestMapping(value = "/tipoDieta", method = { RequestMethod.GET })
	public ResponseEntity<List<TipoDieta>> getListaTipoDieta() {
		logger.info("lista tipo dieta");
		final List<TipoDieta> result = pssoService.getListaTipoDieta();
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/tipoDietaSpeciale", method = { RequestMethod.GET })
	public ResponseEntity<List<TipoDietaSpeciale>> getListaTipoDietaSpeciale() {
		logger.info("lista tipo dieta speciale");
		final List<TipoDietaSpeciale> result = pssoService.getListaTipoDietaSpeciale();
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/tipoRichiedente", method = { RequestMethod.GET })
	public ResponseEntity<List<TipoRichiedente>> getListaTipoRichiedente() {
		logger.info("lista tipo dieta speciale");
		final List<TipoRichiedente> result = pssoService.getListaTipoRichiedente();
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/getScuolaAlunnoByCfAlunno/{cfAlunno}", method = { RequestMethod.GET })
	public ResponseEntity<List<ScuolaAlunno>> getScuolaAlunnoByCfAlunno(@PathVariable String cfAlunno) {
		logger.info("getScuolaAlunnoByCfAlunno: {}", cfAlunno);
		final List<ScuolaAlunno> result = pssoService.findByCodiceFiscaleAlunno(cfAlunno);
		return ResponseEntity.ok(result);
	}

}
