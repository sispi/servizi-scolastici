package it.filippetti.sispi.rest;

import java.util.List;
import java.util.Map;

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

import it.filippetti.sispi.fasciaistituto.CambioFasciaBean;
import it.filippetti.sispi.model.FasciaIstituto;
import it.filippetti.sispi.pagamentoretta.DateUtils;
import it.filippetti.sispi.service.FasciaIstitutoService;

@RestController
@RequestMapping(path = "/fasciaistituto", produces = MediaType.APPLICATION_JSON_VALUE)
public class FasciaIstitutoController {

	private static final Logger logger = LoggerFactory.getLogger(IseeController.class);

	@Autowired
	private FasciaIstitutoService fasciaIstitutoService;

	@RequestMapping(value = "/ricalcola", method = { RequestMethod.GET })
	public ResponseEntity<List<FasciaIstituto>> ricalcolaFasciaIstituto() {
		logger.info("ricalcola fascia istituto");
		final List<FasciaIstituto> res = fasciaIstitutoService.inserisciDati();
		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/ricalcola/{cfAlunno}", method = { RequestMethod.GET })
	public ResponseEntity<List<FasciaIstituto>> ricalcolaFasciaIstitutoAlunno(
			@PathVariable("cfAlunno") String cfAlunno) {
		logger.info("ricalcola cf alunno: {}", cfAlunno);
		final List<FasciaIstituto> res = fasciaIstitutoService.inserisciDati(cfAlunno);
		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ResponseEntity<FasciaIstituto> salvaFasciaIstituto(@RequestBody FasciaIstituto inputData,
			@RequestParam(name = "salvaSuccessivi", required = false, defaultValue = "0") Integer salvaSuccessivi) {
		logger.info("salvaFasciaIstituto salvaSuccessivi: {}", salvaSuccessivi);
		FasciaIstituto result = null;
		if (1 == salvaSuccessivi) {
			result = fasciaIstitutoService.salvaSuccessivi(inputData);
		} else {
			result = fasciaIstitutoService.salvaDati(inputData);
		}
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/cambiofascia", method = { RequestMethod.POST })
	public ResponseEntity<Map<String, String>> cambioFasciaOrariaIstituto(@RequestBody CambioFasciaBean inputData) {
		logger.info("cambioFasciaOrariaIstituto inputData: {}", inputData);
		final Map<String, String> result = fasciaIstitutoService.cambioFasciaOraria(inputData);
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/lista/{cfAlunno}", method = { RequestMethod.GET })
	public ResponseEntity<List<FasciaIstituto>> getFasciaIstitutoAlunno(@PathVariable("cfAlunno") String cfAlunno) {
		logger.info("fascia istituto cf alunno: {}", cfAlunno);
		final List<FasciaIstituto> res = fasciaIstitutoService.getListaFasciaIstituto(cfAlunno,
				DateUtils.getAnnoScolasticoAttuale());
		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/lista/{cfAlunno}/{annoScolastico}", method = { RequestMethod.GET })
	public ResponseEntity<List<FasciaIstituto>> getFasciaIstitutoAlunnoAnnoScolastico(
			@PathVariable("cfAlunno") String cfAlunno, @PathVariable Integer annoScolastico) {
		logger.info("fascia istituto cf alunno: {} - annoScolastico: {}", cfAlunno, annoScolastico);
		final List<FasciaIstituto> res = fasciaIstitutoService.getListaFasciaIstituto(cfAlunno,
				DateUtils.getAnnoScolasticoByAnno(annoScolastico));
		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/get/{idRecord}", method = { RequestMethod.GET })
	public ResponseEntity<FasciaIstituto> getFasciaIstitutoById(@PathVariable("idRecord") Long idRecord) {
		logger.info("fascia istituto ID: {}", idRecord);
		final FasciaIstituto res = fasciaIstitutoService.get(idRecord);
		return ResponseEntity.ok(res);
	}

}
