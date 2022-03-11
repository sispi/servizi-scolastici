package it.filippetti.sispi.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.filippetti.sispi.refezione.InputDataRefezionePagamento;
import it.filippetti.sispi.refezione.OutputDataRefezionePagamento;
import it.filippetti.sispi.refezione.RefezioneAlunnoBean;
import it.filippetti.sispi.service.PortaleRefezioneService;

@RestController
@RequestMapping(path = "/refezione", produces = MediaType.APPLICATION_JSON_VALUE)
public class RefezioneScolasticaController {

	private static final Logger logger = LoggerFactory.getLogger(RefezioneScolasticaController.class);

	@Autowired
	private PortaleRefezioneService portaleRefezioneService;

	@RequestMapping(value = "/alunno/{cfAlunno}", method = { RequestMethod.GET })
	public ResponseEntity<RefezioneAlunnoBean> getListaDomandeFornitura(@PathVariable("cfAlunno") String cfAlunno) {
		logger.info("Ricerco refezione alunno: {}", cfAlunno);
		cfAlunno = cfAlunno.toUpperCase();
		RefezioneAlunnoBean result = portaleRefezioneService.getDatiAlunno(cfAlunno);
		if (result == null) {
			// Non ho trovato il cfAlunno
			result = new RefezioneAlunnoBean();
			result.setEsito("1");
			result.setCodicefiscale(cfAlunno);
		} else {
			result.setEsito("0");
		}
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/pagamento", method = { RequestMethod.POST })
	public ResponseEntity<OutputDataRefezionePagamento> eseguiPagamento(
			@RequestBody InputDataRefezionePagamento inputData) {
		final OutputDataRefezionePagamento result = portaleRefezioneService.salvaPagamento(inputData);
		return ResponseEntity.ok(result);
	}

}
