package it.filippetti.sispi.rest;

import java.util.List;
import java.util.Map;

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

import it.filippetti.ks6.service.TableJDBCService;

@RestController
@RequestMapping(path = "/db", produces = MediaType.APPLICATION_JSON_VALUE)
public class DbSelectController {

	private static final Logger logger = LoggerFactory.getLogger(DbSelectController.class);

	@Autowired
	private TableJDBCService tableJdbcRepository;

	@RequestMapping(value = "/select/{tableName}", method = { RequestMethod.GET })
	public ResponseEntity<List<Map<String, Object>>> getListaRisultatiSelect(
			@PathVariable("tableName") String tableName,
			@RequestParam(name = "where", required = false) String whereCondition,
			@RequestParam(name = "orderBy", required = false) String orderCondition,
			@RequestParam(value = "max_row", required = false) Integer maxRows) {
		logger.info("getListaRisultatiSelect: " + tableName);
		final List<Map<String, Object>> res = tableJdbcRepository.findAll(tableName, whereCondition, orderCondition,
				maxRows);
		return ResponseEntity.ok(res);
	}
}
