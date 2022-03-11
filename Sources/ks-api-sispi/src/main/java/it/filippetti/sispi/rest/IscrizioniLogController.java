package it.filippetti.sispi.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.filippetti.sispi.model.IscrizioniLog;
import it.filippetti.sispi.repository.IscrizioniLogRepository;

@RestController
@RequestMapping(path = "/iscrizionilog", produces = MediaType.APPLICATION_JSON_VALUE)
public class IscrizioniLogController {

	private static final Logger logger = LoggerFactory.getLogger(IscrizioniLogController.class);

	@Autowired
	private IscrizioniLogRepository iscrizioniLogRepository;

	@RequestMapping(value = "/lista", method = { RequestMethod.GET })
	public ResponseEntity<List<IscrizioniLog>> getListaIscrizioniLogByIdIstanza(
			@RequestParam(required = false) String idIstanza) {
		List<IscrizioniLog> res = null;
		if (StringUtils.hasLength(idIstanza)) {
			logger.info("lista iscrizioni log by istanza {}", idIstanza);
			res = iscrizioniLogRepository
					.findByIdIstanzaIscrizioneOrderByIdIstanzaIscrizioneAscDataModificaDesc(idIstanza);
		} else {
			logger.info("lista iscrizioni log");
			final Sort sort = Sort.by("idIstanzaIscrizione").ascending().and(Sort.by("dataModifica").ascending());
			res = iscrizioniLogRepository.findAll(sort);
		}

		return ResponseEntity.ok(res);
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ResponseEntity<IscrizioniLog> salvaIscrizioneLog(@RequestBody IscrizioniLog inputData) {
		final IscrizioniLog result = iscrizioniLogRepository.save(inputData);
		return ResponseEntity.ok(result);
	}
}
