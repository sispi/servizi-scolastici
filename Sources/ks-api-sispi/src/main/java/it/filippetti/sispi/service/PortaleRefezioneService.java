package it.filippetti.sispi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.filippetti.sispi.refezione.InputDataRefezionePagamento;
import it.filippetti.sispi.refezione.OutputDataRefezionePagamento;
import it.filippetti.sispi.refezione.PortaleRefezioneProperties;
import it.filippetti.sispi.refezione.RefezioneAlunnoBean;
import it.filippetti.sispi.refezione.RefezioneAlunnoPortale;

@Service
public class PortaleRefezioneService {

	private static final Logger logger = LoggerFactory.getLogger(PortaleRefezioneService.class);

	private final RestTemplate restTemplate;

	@Autowired
	private PortaleRefezioneProperties portaleRefezioneProperties;

	public PortaleRefezioneService(RestTemplateBuilder restTemplateBuilder,
			PortaleRefezioneProperties portaleRefezioneProperties) {
		this.restTemplate = restTemplateBuilder
				.basicAuthentication(portaleRefezioneProperties.getUsername(), portaleRefezioneProperties.getPassword())
				.build();
	}

	public RefezioneAlunnoBean getDatiAlunno(String cfMinore) {
		logger.debug("ricerco alunno: {}", cfMinore);
		final String baseUrl = portaleRefezioneProperties.getUrl();
		RefezioneAlunnoBean result = null;
		ResponseEntity<RefezioneAlunnoPortale> entity = restTemplate
				.getForEntity(baseUrl + "/refezione/alunno?codice-fiscale=" + cfMinore, RefezioneAlunnoPortale.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			result = convertAlunnoPortale(entity.getBody());
		}
		return result;
	}

	public OutputDataRefezionePagamento salvaPagamento(InputDataRefezionePagamento pagamento) {
		logger.debug("salva pagamento: {}", pagamento);
		final String baseUrl = portaleRefezioneProperties.getUrl();
		OutputDataRefezionePagamento result = null;
		ResponseEntity<OutputDataRefezionePagamento> entity = restTemplate
				.postForEntity(baseUrl + "/refezione/pagamento", pagamento, OutputDataRefezionePagamento.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			result = entity.getBody();
			logger.debug("salva pagamento dati ricevuti: {}", result);
		}
		return result;
	}

	private RefezioneAlunnoBean convertAlunnoPortale(RefezioneAlunnoPortale alunnoPortale) {
		final RefezioneAlunnoBean refezioneAlunnoBean = new RefezioneAlunnoBean();
		refezioneAlunnoBean.setAnno_refezione(alunnoPortale.getAnnoRefezione());
		refezioneAlunnoBean.setCodice_domanda(alunnoPortale.getCodiceDomanda());
		refezioneAlunnoBean.setCodicefiscale(alunnoPortale.getCodiceFiscale());
		refezioneAlunnoBean.setCognome(alunnoPortale.getCognome());
		refezioneAlunnoBean.setData_nascita(alunnoPortale.getDataNascita());
		refezioneAlunnoBean.setDescrizione_scuola(alunnoPortale.getDescrizioneScuola());
		refezioneAlunnoBean.setGiorni_funzionali(alunnoPortale.getGiorniFunzionali());
		refezioneAlunnoBean.setImporto_tariffa(fixAggiungiDecimali(alunnoPortale.getImportoTariffa(), 2));
		refezioneAlunnoBean.setLuogo_nascita(alunnoPortale.getLuogoNascita());
		refezioneAlunnoBean.setNome(alunnoPortale.getNome());
		refezioneAlunnoBean.setProtocollo_domanda(alunnoPortale.getProtocolloDomanda());
		refezioneAlunnoBean.setTipo_tariffa(alunnoPortale.getTipoTariffa());
		return refezioneAlunnoBean;
	}

	private String fixAggiungiDecimali(String importoSenzaDecimali, Integer numeroDecimali) {
		String result = importoSenzaDecimali;
		if (importoSenzaDecimali != null) {
			for (int i = importoSenzaDecimali.length(); i < numeroDecimali + 1; i++) {
				importoSenzaDecimali = "0" + importoSenzaDecimali;
			}
			final String parteIntera = importoSenzaDecimali.substring(0,
					importoSenzaDecimali.length() - numeroDecimali);
			final String parteDecimale = importoSenzaDecimali.substring(importoSenzaDecimali.length() - numeroDecimali);
			result = parteIntera + "," + parteDecimale;
		}
		return result;
	}
}
