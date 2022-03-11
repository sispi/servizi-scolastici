package it.filippetti.sispi.service;

import it.filippetti.sispi.model.Pagamento;
import it.filippetti.sispi.pagamentoretta.PagamentoRettaScolaticaSpecification;
import it.filippetti.sispi.repository.PagamentoRepository;
import it.filippetti.sispi.spring.SearchCriteria;
import it.filippetti.sispi.spring.SearchOperation;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
public class PagamentoService {

	@Autowired
	private PagamentoRepository pagamentoRepository;

	public List<Pagamento> findAll(@Valid Map<String, Object> listaFiltri) {
		final PagamentoRettaScolaticaSpecification spec = new PagamentoRettaScolaticaSpecification();

		if (listaFiltri.containsKey("cfMinore")) {
			spec.add(new SearchCriteria("cfMinore", listaFiltri.get("cfMinore"), SearchOperation.EQUAL));
		}

		if (listaFiltri.containsKey("nomeIstituto")) {
			spec.add(new SearchCriteria("nomeIstituto", listaFiltri.get("nomeIstituto"), SearchOperation.EQUAL));
		}

		if (listaFiltri.containsKey("descrizione")) {
			spec.add(new SearchCriteria("descrizione", listaFiltri.get("descrizione"), SearchOperation.EQUAL));
		}

		if (listaFiltri.containsKey("cfContribuente")) {
			spec.add(new SearchCriteria("cfContribuente", listaFiltri.get("cfContribuente"), SearchOperation.EQUAL));
		}

		if (listaFiltri.containsKey("modalita")) {
			spec.add(new SearchCriteria("modalita", listaFiltri.get("modalita"), SearchOperation.EQUAL));
		}

		if (listaFiltri.containsKey("idRata")) {
			spec.add(new SearchCriteria("idRata", listaFiltri.get("idRata"), SearchOperation.EQUAL));
		}

		return pagamentoRepository.findAll(spec, Sort.by("cfMinore"));
	}

	/*
	 * and (:DATA_RICEVUTA_DA is null or (json_value(p.JSON_DOC,
	 * '$.altriDati.dataOraMessaggioRicevuta' RETURNING timestamp) >=
	 * TO_DATE(:DATA_RICEVUTA_DA||' 00:00:00','DD/MM/YYYY HH24:MI:SS') OR
	 * json_value(p.JSON_DOC, '$.altriDati."pay_i:dataOraMessaggioRicevuta"'
	 * RETURNING timestamp) >= TO_DATE(:DATA_RICEVUTA_DA||' 00:00:00','DD/MM/YYYY
	 * HH24:MI:SS') )) and (:DATA_RICEVUTA_A is null or ( json_value(p.JSON_DOC,
	 * '$.altriDati.dataOraMessaggioRicevuta' RETURNING timestamp) <=
	 * TO_DATE(:DATA_RICEVUTA_A||' 23:59:59','DD/MM/YYYY HH24:MI:SS') OR
	 * json_value(p.JSON_DOC, '$.altriDati."pay_i:dataOraMessaggioRicevuta"'
	 * RETURNING timestamp) <= TO_DATE(:DATA_RICEVUTA_A||' 23:59:59','DD/MM/YYYY
	 * HH24:MI:SS') ))
	 */

}
