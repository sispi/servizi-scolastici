package it.filippetti.sispi.service;

import it.filippetti.sispi.libriscolastici.DomandaFornituraBean;
import it.filippetti.sispi.libriscolastici.ScuolaBean;
import it.filippetti.sispi.portalescuola.AlunnoPortale;
import it.filippetti.sispi.portalescuola.PortaleScuolaProperties;
import it.filippetti.sispi.portalescuola.ScuolaPortale;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PortaleScuolaService {

	private static final Logger logger = LoggerFactory.getLogger(PortaleScuolaService.class);

	private final RestTemplate restTemplate;

	@Autowired
	private PortaleScuolaProperties portalescuolaProperties;

	public PortaleScuolaService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public List<ScuolaBean> getElencoScuole() {
		final List<ScuolaBean> res = new ArrayList<ScuolaBean>();
		final String baseUrl = portalescuolaProperties.getUrl();
		logger.debug("Recupero elenco scuole dal portale: " + baseUrl);
		final String[] listaCodiciScuola = portalescuolaProperties.getCodiciScuola();
		ResponseEntity<ScuolaPortale[]> entity = null;
		for (int i = 0; i < listaCodiciScuola.length; i++) {
			final String codiceScuola = listaCodiciScuola[i];
			logger.debug("Tipo scuola: " + codiceScuola);
			try {
				entity = restTemplate.getForEntity(baseUrl + "/scuola/lista/" + codiceScuola, ScuolaPortale[].class);
				if (entity.getStatusCode().is2xxSuccessful()) {
					res.addAll(convertListaScuolaPortale(entity.getBody()));
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		return res;
	}

	public ScuolaBean getScuolaById(String idScuola) {
		ScuolaBean result = null;
		final String baseUrl = portalescuolaProperties.getUrl();
		try {
			ResponseEntity<ScuolaPortale> entity = restTemplate.getForEntity(baseUrl + "/scuola/codice/" + idScuola,
					ScuolaPortale.class);
			if (entity.getStatusCode().is2xxSuccessful()) {
				result = convertScuolaPortale(entity.getBody());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	private List<ScuolaBean> convertListaScuolaPortale(ScuolaPortale[] listaScuolaPortale) {
		final List<ScuolaBean> result = new ArrayList<ScuolaBean>();
		for (int i = 0; i < listaScuolaPortale.length; i++) {
			final ScuolaPortale scuolaPortale = listaScuolaPortale[i];
			result.add(convertScuolaPortale(scuolaPortale));
		}
		return result;
	}

	private ScuolaBean convertScuolaPortale(ScuolaPortale scuolaPortale) {
		final ScuolaBean scuolaBean = new ScuolaBean();
		scuolaBean.setCap_scuola(scuolaPortale.getCap());
		scuolaBean.setCivico_scuola(scuolaPortale.getNumCivico());
		scuolaBean.setComune_scuola(scuolaPortale.getDescrizioneComuneScuola());
		// TODO: secondo le specifiche ci andrebbe getScuola ma getDenominazione ha piu
		// dati
		scuolaBean.setDenominazione_scuola(scuolaPortale.getDenominazione());
		scuolaBean.setId_scuola(scuolaPortale.getIdScuola());
		scuolaBean.setIndirizzo_scuola(scuolaPortale.getVia());
		scuolaBean.setProvincia_scuola(scuolaPortale.getProvincia());
		scuolaBean.setTelefono_scuola(scuolaPortale.getTelefono());
		scuolaBean.setIstituzione_scolastica(scuolaPortale.getTipoScuola());
		return scuolaBean;
	}

	public DomandaFornituraBean getDatiDomandaFornitura(String cfAlunno, String cfResponsabile) {
		DomandaFornituraBean result = null;
		final String baseUrl = portalescuolaProperties.getUrl();
		try {
			/// alunno/codice-alunno/BNCRTI08M56G273Z/codice-responsabile/SNTMRA83S44G273B
			ResponseEntity<AlunnoPortale> entity = restTemplate.getForEntity(
					baseUrl + "/alunno/codice-alunno/" + cfAlunno + "/codice-responsabile/" + cfResponsabile,
					AlunnoPortale.class);

			if (entity.getStatusCode().is2xxSuccessful()) {
				final AlunnoPortale alunnoPortale = entity.getBody();
				final ScuolaBean scuolaAlunno = getScuolaById(alunnoPortale.getIdScuola());
				result = convertAlunnoPortale(alunnoPortale, scuolaAlunno);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	private DomandaFornituraBean convertAlunnoPortale(AlunnoPortale alunnoPortale, ScuolaBean scuola) {
		final DomandaFornituraBean result = new DomandaFornituraBean();
		result.setCap(alunnoPortale.getCap());
		result.setCap_scuola(scuola.getCap_scuola());
		result.setCivico(alunnoPortale.getNumeroCivico());
		result.setCivico_scuola(scuola.getCivico_scuola());
		result.setClasse_scuola(alunnoPortale.getClasse());
		result.setCodicefiscale(alunnoPortale.getCodiceFiscale());
		result.setCognome(alunnoPortale.getCognome());
		result.setComune(alunnoPortale.getComuneResidenza());
		result.setComune_scuola(scuola.getComune_scuola());
		result.setData_nascita(alunnoPortale.getDataNascita());
		result.setDenominazione_scuola(scuola.getDenominazione_scuola());
		result.setEsito("0");
		result.setIndirizzo(alunnoPortale.getIndirizzo());
		result.setIndirizzo_scuola(scuola.getIndirizzo_scuola());
		result.setLuogo_nascita(alunnoPortale.getComuneNascita());
		result.setNome(alunnoPortale.getNome());
		result.setProvincia_scuola(scuola.getProvincia_scuola());
		result.setTelefono_scuola(scuola.getTelefono_scuola());
		result.setScuola(scuola);
		return result;
	}
}
