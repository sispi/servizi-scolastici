package it.filippetti.sispi.rest;

import java.util.List;
import java.util.Optional;

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

import it.filippetti.sispi.model.Isee;
import it.filippetti.sispi.repository.IseeRepository;
import it.filippetti.sispi.service.IseeService;

@RestController
@RequestMapping(path = "/isee", produces = MediaType.APPLICATION_JSON_VALUE)
public class IseeController {

	private static final Logger logger = LoggerFactory.getLogger(IseeController.class);

	@Autowired
	private IseeRepository iseeRepository;

	@Autowired
	private IseeService iseeService;

	@RequestMapping(value = "/getIsee/{idIsee}", method = { RequestMethod.GET })
	public ResponseEntity<Isee> getIseeById(@PathVariable("idIsee") Long idIsee) {
		logger.info("isee by id {}", idIsee);
		final Optional<Isee> res = iseeRepository.findById(idIsee);
		return ResponseEntity.ok(res.orElse(null));
	}

	@RequestMapping(value = "/lista", method = { RequestMethod.GET })
	public ResponseEntity<List<Isee>> getListaIsee(
			@RequestParam(name = "soloAttivi", required = false, defaultValue = "true") Boolean soloAttivi) {
		logger.info("lista isee");
		final List<Isee> res = iseeService.getListaIsee(soloAttivi);
		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/lista/{cfAlunno}", method = { RequestMethod.GET })
	public ResponseEntity<List<Isee>> getIseeAlunno(@PathVariable("cfAlunno") String cfAlunno,
			@RequestParam(name = "soloAttivi", required = false, defaultValue = "true") Boolean soloAttivi) {
		logger.info("isee cf alunno: {}", cfAlunno);
		final List<Isee> res = iseeService.getListaIsee(cfAlunno.toUpperCase(), soloAttivi);
		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/autocomplete/alunno", method = { RequestMethod.GET })
	public ResponseEntity<List<String>> getAutocompleteAlunno(@RequestParam(name = "q", required = true) String q) {
		logger.info("isee autocomplete alunno: {}", q);
		final List<String> res = iseeRepository.findByCodiceFiscaleContains(q.toUpperCase());
		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ResponseEntity<Isee> salvaIsee(@RequestBody Isee inputData) {
		final Isee result = iseeService.aggiornaIsee(inputData);
		return ResponseEntity.ok(result);
	}
}
