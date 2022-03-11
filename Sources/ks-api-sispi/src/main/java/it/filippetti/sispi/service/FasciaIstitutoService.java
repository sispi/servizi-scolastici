package it.filippetti.sispi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import it.filippetti.sispi.fasciaistituto.CambioFasciaBean;
import it.filippetti.sispi.model.FasciaIstituto;
import it.filippetti.sispi.model.Istanza;
import it.filippetti.sispi.pagamentoretta.DateUtils;
import it.filippetti.sispi.pagamentoretta.DettaglioRataBean;
import it.filippetti.sispi.pagamentoretta.RataUtils;
import it.filippetti.sispi.repository.FasciaIstitutoRepository;

@Service
@Validated
@Transactional
public class FasciaIstitutoService {

	private static final Logger logger = LoggerFactory.getLogger(FasciaIstitutoService.class);

	@Autowired
	private FasciaIstitutoRepository fasciaIstitutoRepository;

	@Autowired
	private IstanzaService istanzaService;

	public List<FasciaIstituto> getListaFasciaIstituto(String codiceFiscale, String annoScolastico) {

		final List<Istanza> listaIstanze = istanzaService.getListaIstanzaConfermate(codiceFiscale, annoScolastico);
		if (listaIstanze.size() == 0) {
			throw new ServiceException(
					"Istanza non trovata per minore: " + codiceFiscale + " - anno scolastico " + annoScolastico);
		}
		final Istanza istanza = listaIstanze.iterator().next();
		return getListaFasciaIstituto(istanza);
	}

	public List<FasciaIstituto> getListaFasciaIstituto(@Valid Istanza istanza) {
		logger.debug("Cerco lista fascia cfMinore: {} - annoScolastico: {}", istanza.getCfMinore(),
				istanza.getAnnoScolastico());
		if (!istanza.isAsiloNido()) {
			throw new ServiceException("Tipo procedimento non valido per minore: " + istanza.getCfMinore()
					+ " - anno scolastico " + istanza.getAnnoScolastico());
		}
		List<FasciaIstituto> listaFasciaIstituto = fasciaIstitutoRepository
				.findByCodiceFiscaleAndAnnoScolasticoOrderBySort(istanza.getCfMinore(), istanza.getAnnoScolastico());

		if (!IscrizioneConstants.NOTIFICATA_ISCRIZIONE_SI.equals(istanza.getConfermaNotificata())) {
			for (FasciaIstituto fasciaIstituto : listaFasciaIstituto) {
				fasciaIstitutoRepository.delete(fasciaIstituto);
			}
			listaFasciaIstituto.clear();
		}

		if (listaFasciaIstituto == null || listaFasciaIstituto.size() == 0) {
			listaFasciaIstituto = inserisciDatiIstituto(istanza);
		}

		return listaFasciaIstituto;
	}

	private List<FasciaIstituto> inserisciDatiIstituto(Istanza istanza) {
		final List<FasciaIstituto> result = new ArrayList<FasciaIstituto>();
		final List<DettaglioRataBean> listaMesi = RataUtils.getListaRateDefault();
		for (DettaglioRataBean dettaglioRataBean : listaMesi) {
			final FasciaIstituto fascia = getFasciaIstituto(istanza, dettaglioRataBean);
			result.add(fasciaIstitutoRepository.save(fascia));
		}
		return result;
	}

	private FasciaIstituto getFasciaIstituto(Istanza istanza, DettaglioRataBean dettaglioRataBean) {
		final FasciaIstituto fasciaIstituto = new FasciaIstituto();
		fasciaIstituto.setAnnoScolastico(istanza.getAnnoScolastico());
		fasciaIstituto.setCodiceFiscale(istanza.getCfMinore());
		fasciaIstituto.setDescrizione(RataUtils.getDescizioneSoloMese(dettaglioRataBean.getDescrizione()));
		fasciaIstituto.setFascia(istanza.getFascia());
		fasciaIstituto.setFasciaOraria(istanza.getFasciaOraria());
		fasciaIstituto.setNomeIstituto(istanza.getNomeIstituto());
		fasciaIstituto.setSort(dettaglioRataBean.getOrdinamento());
		return fasciaIstituto;

	}

//	private String getDescizioneFasciaIstituto(DettaglioRataBean dettaglioRataBean) {
//		String descrizione = dettaglioRataBean.getDescrizione();
//		String res = descrizione.substring(5);
//		return res;
//	}

	public List<FasciaIstituto> inserisciDati() {
		final List<Istanza> listaIstanza = istanzaService.getListaIstanzaConfermate();
		return inserisciDati(listaIstanza);
	}

	public List<FasciaIstituto> inserisciDati(List<Istanza> listaIstanza) {
		final List<FasciaIstituto> result = new ArrayList<FasciaIstituto>();
		for (final Istanza istanza : listaIstanza) {
			try {
				result.addAll(getListaFasciaIstituto(istanza));
				fasciaIstitutoRepository.flush();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	public List<FasciaIstituto> inserisciDati(String cfMinore) {
		final String annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		final Istanza istanza = istanzaService.getIstanzaNotificata(cfMinore, annoScolastico);
		if (istanza == null) {
			throw new ServiceException(
					"Istanza non trovata per minore: " + cfMinore + " - anno scolastico " + annoScolastico);
		}
		return getListaFasciaIstituto(istanza);
	}

	public FasciaIstituto salvaDati(@Valid FasciaIstituto fasciaIstituto) {
		return fasciaIstitutoRepository.save(fasciaIstituto);
	}

	public FasciaIstituto salvaSuccessivi(@Valid FasciaIstituto fasciaIstituto) {
		fasciaIstituto = salvaDati(fasciaIstituto);
		final List<FasciaIstituto> listaRisultati = fasciaIstitutoRepository
				.findByCodiceFiscaleAndAnnoScolasticoOrderBySort(fasciaIstituto.getCodiceFiscale(),
						fasciaIstituto.getAnnoScolastico());

		for (final FasciaIstituto singoloRisultato : listaRisultati) {
			if (singoloRisultato.getSort() > fasciaIstituto.getSort()) {
				singoloRisultato.setFascia(fasciaIstituto.getFascia());
				singoloRisultato.setFasciaOraria(fasciaIstituto.getFasciaOraria());
				singoloRisultato.setNomeIstituto(fasciaIstituto.getNomeIstituto());
				salvaDati(singoloRisultato);
			}
		}
		return fasciaIstituto;
	}

	public Map<String, String> cambioFasciaOraria(@Valid CambioFasciaBean inputData) {
		final List<Integer> listaMesiRiferimento = getListaMesiRiferimento(inputData);
		final Map<String, String> listaAlunniAggiornati = new HashMap<>();
		for (final Integer meseRiferimento : listaMesiRiferimento) {
			final List<FasciaIstituto> listaAlunniDaAggiornare = fasciaIstitutoRepository
					.findByAnnoScolasticoAndSortAndNomeIstitutoAndFasciaAndFasciaOraria(inputData.getAnnoScolastico(),
							meseRiferimento, inputData.getNomeIstituto(), inputData.getFascia(),
							inputData.getFasciaOraria());

			for (final FasciaIstituto alunnoDaAggiornare : listaAlunniDaAggiornare) {
				alunnoDaAggiornare.setFasciaOraria(inputData.getFasciaOrariaNuova());
				fasciaIstitutoRepository.save(alunnoDaAggiornare);
				if (!listaAlunniAggiornati.containsKey(alunnoDaAggiornare.getCodiceFiscale())) {
					listaAlunniAggiornati.put(alunnoDaAggiornare.getCodiceFiscale(),
							alunnoDaAggiornare.getCodiceFiscale());
				}
			}
		}
		return listaAlunniAggiornati;
	}

	private List<Integer> getListaMesiRiferimento(@Valid CambioFasciaBean inputData) {
		final List<Integer> listaMesiRiferimento = new ArrayList<Integer>();
		listaMesiRiferimento.add(inputData.getMeseRiferimento());

		if ("si".equals(inputData.getMesiSuccessivi())) {
			final Integer meseSuccessivo = inputData.getMeseRiferimento() + 1;
			for (int i = meseSuccessivo; i <= 11; i++) {
				listaMesiRiferimento.add(i);
			}
		}
		return listaMesiRiferimento;
	}

	public FasciaIstituto get(Long idRecord) {
		return fasciaIstitutoRepository.findById(idRecord).orElse(null);
	}
}
