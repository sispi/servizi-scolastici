package it.filippetti.sispi.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.filippetti.sispi.model.AsiloTariffe;
import it.filippetti.sispi.repository.AsiloTariffeRepository;

@RestController
@RequestMapping(path = "/asilotariffe", produces = MediaType.APPLICATION_JSON_VALUE)
public class AsiloTariffeController {

	private static final Logger logger = LoggerFactory.getLogger(AsiloTariffeController.class);

	@Autowired
	private AsiloTariffeRepository asiloTariffeRepository;

	@RequestMapping(value = "/lista", method = { RequestMethod.GET })
	public ResponseEntity<List<AsiloTariffe>> getListaTariffe() {
		logger.info("lista tariffe");
		final List<AsiloTariffe> res = asiloTariffeRepository.findAll();
		return ResponseEntity.ok(res);
	}
}
