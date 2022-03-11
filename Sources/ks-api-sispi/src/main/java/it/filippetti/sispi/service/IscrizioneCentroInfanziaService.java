package it.filippetti.sispi.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.filippetti.sispi.centroinfanzia.DettaglioIscrizioneCentroInfanziaBean;
import it.filippetti.sispi.centroinfanzia.IscrizioneCentroInfanziaBean;
import it.filippetti.sispi.centroinfanzia.PagamentoCentroInfanziaBean;
import it.filippetti.sispi.model.CentroInfanziaLog;
import it.filippetti.sispi.model.CentroInfanziaTariffe;
import it.filippetti.sispi.model.DettaglioIscrizioneCentroInfanzia;
import it.filippetti.sispi.model.GiorniFrequenzaIstituto;
import it.filippetti.sispi.model.IscrizioneCentroInfanzia;
import it.filippetti.sispi.model.Isee;
import it.filippetti.sispi.pagamentoretta.DateUtils;
import it.filippetti.sispi.pagamentoretta.DettaglioMinoreBean;
import it.filippetti.sispi.pagamentoretta.DettaglioRataBean;
import it.filippetti.sispi.pagamentoretta.RataUtils;
import it.filippetti.sispi.repository.CentroInfanziaLogRepository;
import it.filippetti.sispi.repository.CentroInfanziaTariffeRepository;
import it.filippetti.sispi.repository.DettaglioIscrizioneCentroInfanziaRepository;
import it.filippetti.sispi.repository.IscrizioneCentroInfanziaRepository;

@Service
@Transactional
@Validated
public class IscrizioneCentroInfanziaService {

	private static final Logger logger = LoggerFactory.getLogger(IscrizioneCentroInfanziaService.class);

	@Autowired
	private IscrizioneCentroInfanziaRepository iscrizioneCentroInfanziaRepository;

	@Autowired
	private DettaglioIscrizioneCentroInfanziaRepository dettaglioIscrizioneCentroInfanziaRepository;

	@Autowired
	private CentroInfanziaLogRepository centroInfanziaLogRepository;

	@Autowired
	private CentroInfanziaTariffeRepository centroInfanziaTariffeRepository;

	@Autowired
	private GiorniFrequenzaIstitutoService giorniFrequenzaIstitutoService;

	@Autowired
	private IseeService iseeService;

	public List<DettaglioIscrizioneCentroInfanzia> ricalcolaRateIscritto(@NotBlank String cfMinore,
			String utenteModifica, String annoScolastico, String istanzaOriginaleJson, String causale) {
		cfMinore = cfMinore.toUpperCase();
		List<DettaglioIscrizioneCentroInfanzia> res = new ArrayList<>();
		final List<IscrizioneCentroInfanzia> listaIstanze = getListaIstanzaConfermate(cfMinore, annoScolastico);
		if (listaIstanze.isEmpty()) {
			throw new ServiceException("Nessun record valido per: " + cfMinore);
		}

		final IscrizioneCentroInfanzia istanza = listaIstanze.iterator().next();
		final IscrizioneCentroInfanzia istanzaOriginale = getIstanzaOriginale(istanzaOriginaleJson);

		if (istanza.getDataFineIscrizioneDate() != null && istanza.getDataInizioIscrizioneDate() != null) {
			// istanza.setRicalcoloRate("");
			final List<Isee> listaIsee = iseeService.getListaIsee(cfMinore, Boolean.TRUE);
			if (listaIsee.isEmpty()) {
				throw new ServiceException("Isee non trovata per: " + cfMinore);
			}

			res = aggiornaRate(istanza, listaIsee, utenteModifica, causale);

			iscrizioneCentroInfanziaRepository.save(istanza);
			centroInfanziaLogRepository
					.save(getLog(istanzaOriginale, istanza, utenteModifica, "RICALCOLO_ISTANZA", null));
		} else {
			throw new ServiceException("Data inizio o fine non valorizzata per ID: " + istanza.getId());
		}

		return res;
	}

	private CentroInfanziaLog getLog(Object valoreOriginale, Object valoreDaSalvare, String utenteModifica,
			String tipoModifica, String causale) {
		final CentroInfanziaLog centroInfanziaLog = new CentroInfanziaLog();
		centroInfanziaLog.setDataModifica(LocalDate.now());
		centroInfanziaLog.setDatiPre(getDatiRecord(valoreOriginale));
		centroInfanziaLog.setDatiPost(getDatiRecord(valoreDaSalvare));
		if (DettaglioIscrizioneCentroInfanzia.class.isInstance(valoreDaSalvare)) {
			centroInfanziaLog.setDettaglioIscrizione((DettaglioIscrizioneCentroInfanzia) valoreDaSalvare);
		}
		centroInfanziaLog.setCausale(causale);
		centroInfanziaLog.setTipoModifica(tipoModifica);
		centroInfanziaLog.setUtenteModifica(utenteModifica);
		return centroInfanziaLog;
	}

	private Map<String, Object> getDatiRecord(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, Object> datiJson = mapper.convertValue(obj, Map.class);
		return datiJson;
	}

	private List<DettaglioIscrizioneCentroInfanzia> aggiornaRate(IscrizioneCentroInfanzia istanza, List<Isee> listaIsee,
			String utenteModifica, String causale) {
		final List<DettaglioIscrizioneCentroInfanzia> res = new ArrayList<DettaglioIscrizioneCentroInfanzia>();
		final List<DettaglioRataBean> rateDefault = RataUtils.getListaRateDefault();
		final List<DettaglioIscrizioneCentroInfanzia> listaRate = istanza.getListaDettaglioIscrizione();
		final List<GiorniFrequenzaIstituto> listaGiorniFrequenza = giorniFrequenzaIstitutoService
				.getByAnnoScolastico(istanza.getAnnoScolastico());

		if (listaGiorniFrequenza.size() == 0) {
			throw new ServiceException(
					"Dati giorni frequenza istituto non trovati per anno scolastico: " + istanza.getAnnoScolastico());
		}

		for (final DettaglioRataBean dettaglioRataBean : rateDefault) {
			final DettaglioIscrizioneCentroInfanzia dettaglioSalvato = trovaDettaglioSalvato(dettaglioRataBean,
					listaRate);
			if (RataUtils.isIntervalloValido(istanza.getDataInizioIscrizioneDate(), istanza.getDataFineIscrizioneDate(),
					dettaglioRataBean, istanza.getAnnoScolastico())) {
				final Optional<DettaglioIscrizioneCentroInfanzia> rataOriginale = getDettaglioOriginale(
						dettaglioSalvato);

				DettaglioIscrizioneCentroInfanzia rataDaSalvare = getDettaglioDaSalvare(istanza, dettaglioRataBean,
						dettaglioSalvato, listaIsee, listaGiorniFrequenza);
				rataDaSalvare = dettaglioIscrizioneCentroInfanziaRepository.save(rataDaSalvare);
				if (rataOriginale.isPresent()) {
					centroInfanziaLogRepository.save(
							getLog(rataOriginale.get(), rataDaSalvare, utenteModifica, "RICALCOLO_RATA", causale));
				}
				res.add(rataDaSalvare);
			} else if (dettaglioSalvato != null && dettaglioSalvato.getId() != null
					&& dettaglioSalvato.getDocnumPagamento() == null) {
				dettaglioSalvato.setValoreRata(null);
				dettaglioIscrizioneCentroInfanziaRepository.save(dettaglioSalvato);
			}

		}
		return res;
	}

	private DettaglioIscrizioneCentroInfanzia getDettaglioDaSalvare(IscrizioneCentroInfanzia istanza,
			DettaglioRataBean dettaglioRataBean, DettaglioIscrizioneCentroInfanzia dettaglioSalvato,
			List<Isee> listaIsee, List<GiorniFrequenzaIstituto> listaGiorniFrequenza) {
		DettaglioIscrizioneCentroInfanzia result = dettaglioSalvato;
		if (dettaglioSalvato == null) {
			result = new DettaglioIscrizioneCentroInfanzia();
			if (istanza.getListaDettaglioIscrizione().size() == 0) {
				throw new ServiceException("Nessun dettaglio trovato per ID istanza: " + istanza.getId());
			}
			result.setIscrizione(istanza);
			final DettaglioIscrizioneCentroInfanzia dettaglio = istanza.getListaDettaglioIscrizione().get(0);
			result.setFascia(dettaglio.getFascia());
			result.setFasciaOraria(dettaglio.getFasciaOraria());
			result.setNomeIstituto(dettaglio.getNomeIstituto());
			result.setDescrizione(dettaglioRataBean.getDescrizione());
			result.setSort(dettaglioRataBean.getOrdinamento());
		}
		final Isee iseeRiferimento = iseeService.getIseeFromMonth(dettaglioRataBean.getMeseRiferimento(),
				istanza.getAnnoScolastico(), listaIsee);

		final GiorniFrequenzaIstituto meseFrequenzaIstituto = getGiorniFrequenzaIstituto(listaGiorniFrequenza,
				result.getSort());

		final List<CentroInfanziaTariffe> listaTariffeIstituto = centroInfanziaTariffeRepository
				.findByDenominazioneAsiloAndCategoriaAndAnnoRiferimento(dettaglioSalvato.getNomeIstituto(),
						dettaglioSalvato.getFascia(),
						DateUtils.getAnnoRiferimentoByAnnoScolastico(istanza.getAnnoScolastico()));

		final Float valoreTariffaBase = getTariffa(dettaglioSalvato, iseeRiferimento, listaTariffeIstituto,
				meseFrequenzaIstituto);
		result.setValoreRata(valoreTariffaBase);
		return result;
	}

	private GiorniFrequenzaIstituto getGiorniFrequenzaIstituto(List<GiorniFrequenzaIstituto> listaGiorniFrequenza,
			Integer meseRiferimento) {
		GiorniFrequenzaIstituto result = null;
		for (final GiorniFrequenzaIstituto dettaglio : listaGiorniFrequenza) {
			if (meseRiferimento == dettaglio.getSort()) {
				result = dettaglio;
				break;
			}
		}
		return result;
	}

	private Float getTariffa(DettaglioIscrizioneCentroInfanzia dettaglio, Isee iseeRiferimento,
			List<CentroInfanziaTariffe> listaTariffeIstituto, GiorniFrequenzaIstituto meseFrequenzaIstituto) {
		Float result = 0f;
		final List<CentroInfanziaTariffe> listaTariffeFascia = new ArrayList<>();
		for (final CentroInfanziaTariffe asiloTariffe : listaTariffeIstituto) {
			if (asiloTariffe.getFasciaOraria1() != null
					&& asiloTariffe.getFasciaOraria1().equals(dettaglio.getFasciaOraria())) {
				listaTariffeFascia.add(asiloTariffe);
			}
			if (asiloTariffe.getFasciaOraria2() != null
					&& asiloTariffe.getFasciaOraria2().equals(dettaglio.getFasciaOraria())) {
				listaTariffeFascia.add(asiloTariffe);
			}
			if (asiloTariffe.getFasciaOraria3() != null
					&& asiloTariffe.getFasciaOraria3().equals(dettaglio.getFasciaOraria())) {
				listaTariffeFascia.add(asiloTariffe);
			}
			if (asiloTariffe.getFasciaOraria4() != null
					&& asiloTariffe.getFasciaOraria4().equals(dettaglio.getFasciaOraria())) {
				listaTariffeFascia.add(asiloTariffe);
			}
			if (asiloTariffe.getFasciaOraria5() != null
					&& asiloTariffe.getFasciaOraria5().equals(dettaglio.getFasciaOraria())) {
				listaTariffeFascia.add(asiloTariffe);
			}
		}

		for (CentroInfanziaTariffe singolaTarffa : listaTariffeFascia) {
			Float iseeDaFloat = Float.MIN_VALUE;
			if (singolaTarffa.getIseeDa() != null) {
				iseeDaFloat = singolaTarffa.getIseeDa();
			}

			Float iseeAFloat = Float.MAX_VALUE;
			if (singolaTarffa.getIseeA() != null) {
				iseeAFloat = singolaTarffa.getIseeA();
			}

			if (iseeRiferimento.getImporto() >= iseeDaFloat && iseeRiferimento.getImporto() <= iseeAFloat) {
//				TODO: recuperare i gliorni frequenza dalla relativa gestione
				result = getImporto(singolaTarffa, dettaglio.getFasciaOraria(),
						meseFrequenzaIstituto.getGiorniFrequenza());
				break;
			}
		}

		return result;
	}

	private Optional<DettaglioIscrizioneCentroInfanzia> getDettaglioOriginale(
			DettaglioIscrizioneCentroInfanzia rataSalvata) {
		if (rataSalvata != null) {
			final DettaglioIscrizioneCentroInfanzia result = new DettaglioIscrizioneCentroInfanzia();
			BeanUtils.copyProperties(rataSalvata, result);
			return Optional.ofNullable(result);
		}
		return Optional.empty();
	}

	private DettaglioIscrizioneCentroInfanzia trovaDettaglioSalvato(DettaglioRataBean dettaglioRataBean,
			List<DettaglioIscrizioneCentroInfanzia> listaRate) {
		DettaglioIscrizioneCentroInfanzia result = null;
		for (final DettaglioIscrizioneCentroInfanzia dettaglio : listaRate) {
			if (dettaglioRataBean.getOrdinamento() == dettaglio.getSort()) {
				result = dettaglio;
				break;
			}
		}
		return result;
	}

	private IscrizioneCentroInfanzia getIstanzaOriginale(String istanzaOriginaleJson) {
		IscrizioneCentroInfanzia result = new IscrizioneCentroInfanzia();
		if (istanzaOriginaleJson != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				result = mapper.readValue(istanzaOriginaleJson, IscrizioneCentroInfanzia.class);
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return result;
	}

	private Float getImporto(CentroInfanziaTariffe singolaTarffa, String fasciaOraria, Integer giorniFrequenza) {
		Float result = 0f;
		if (singolaTarffa.getImportoFasciaOraria1() != null && fasciaOraria.equals(singolaTarffa.getFasciaOraria1())) {
			result = singolaTarffa.getImportoFasciaOraria1();
		} else if (singolaTarffa.getImportoFasciaOraria2() != null
				&& fasciaOraria.equals(singolaTarffa.getFasciaOraria2())) {
			result = singolaTarffa.getImportoFasciaOraria2();
		} else if (singolaTarffa.getImportoFasciaOraria3() != null
				&& fasciaOraria.equals(singolaTarffa.getFasciaOraria3())) {
			result = singolaTarffa.getImportoFasciaOraria3();
		} else if (singolaTarffa.getImportoFasciaOraria4() != null
				&& fasciaOraria.equals(singolaTarffa.getFasciaOraria4())) {
			result = singolaTarffa.getImportoFasciaOraria4();
		} else if (singolaTarffa.getImportoFasciaOraria5() != null
				&& fasciaOraria.equals(singolaTarffa.getFasciaOraria5())) {
			result = singolaTarffa.getImportoFasciaOraria5();
		}

		result = calcolaValoreGiorniFrequenza(result, giorniFrequenza, singolaTarffa.getFlagComunale());

		return result;
	}

	private Float calcolaValoreGiorniFrequenza(Float importoRata, Integer giorniFrequenza, String flagComunale) {
		// Se e comunale l'importo non dipende dal numero di giorni e ore
		if ("S".equals(flagComunale)) {
			return importoRata;
		}
		if (giorniFrequenza == null) {
			// Nel caso in cui non ho salvato la rata con questo nuovo valore (record creati
			// prima di aggiungere questa colonna)
			giorniFrequenza = IscrizioneConstants.GIORNI_FREQUENZA_DEFAULT;
		}
		final Float result = importoRata * giorniFrequenza;
		return (float) (Math.round(result * 100.0) / 100.0);
	}

//	public void ricalcolaRateFamiglia(String cfMinore, String utenteModifica, String annoScolastico) {
//		logger.debug("Ricalcolo rate centro infanzia {} - {}", cfMinore, annoScolastico);
//		final List<IscrizioneCentroInfanzia> listaIstanze = getListaIstanzaConfermate(cfMinore, annoScolastico);
//		if (listaIstanze.size() > 0) {
//			final IscrizioneCentroInfanzia istanza = listaIstanze.iterator().next();
//			if (istanza.getIdFamiglia() != null && istanza.getIdFamiglia() > 0) {
//				final List<IscrizioneCentroInfanzia> listaRecordFamiglia = iscrizioneCentroInfanziaRepository
//						.findByIdFamigliaAndConfermaNotificataAndAnnoScolastico(istanza.getIdFamiglia(),
//								IscrizioneConstants.NOTIFICATA_ISCRIZIONE_SI, annoScolastico);
//
//				for (final IscrizioneCentroInfanzia recordFamiglia : listaRecordFamiglia) {
//					if (recordFamiglia.getId() != istanza.getId()) {
//						ricalcolaRateIscritto(recordFamiglia.getCfMinore(), utenteModifica, annoScolastico, null, null);
//					}
//				}
//			}
//		}
//
//	}

	public IscrizioneCentroInfanzia salvaDati(@Valid IscrizioneCentroInfanziaBean inputData) {
		if (inputData.getId() != null) {
			IscrizioneCentroInfanzia result = aggiornaIscritto(inputData);
			if (inputData.getId() != null && inputData.getNomeIstituto() != null && inputData.getFascia() != null
					&& inputData.getFasciaOraria() != null) {
				rigeneraRateMinore(inputData.getCfMinore(), inputData.getAnnoScolastico(), inputData.getNomeIstituto(),
						inputData.getFascia(), inputData.getFasciaOraria(), inputData.getId());
			}

			return result;
		} else {
			validaDatiNuovoIscritto(inputData);
			return inserisciNuovoIscritto(inputData);
		}
	}

	private void validaDatiNuovoIscritto(@Valid IscrizioneCentroInfanziaBean inputData) {
		if (!StringUtils.hasText(inputData.getNomeIstituto())) {
			throw new ServiceException("Nome istituto obbligatorio per nuovo iscritto");
		}
		if (!StringUtils.hasText(inputData.getFascia())) {
			throw new ServiceException("Fascia obbligatoria per nuovo iscritto");
		}
		if (!StringUtils.hasText(inputData.getFasciaOraria())) {
			throw new ServiceException("Fascia oraria obbligatoria per nuovo iscritto");
		}
	}

	private IscrizioneCentroInfanzia aggiornaIscritto(@Valid IscrizioneCentroInfanziaBean inputData) {
		final IscrizioneCentroInfanzia nuovoValore = convertBean(inputData);
		final IscrizioneCentroInfanzia result = iscrizioneCentroInfanziaRepository.findById(inputData.getId())
				.orElse(null);
		if (result == null) {
			throw new ServiceException("Iscrizione centro infanzia non trovata: " + inputData.getId());
		}
		List<DettaglioIscrizioneCentroInfanzia> listaDettaglio = result.getListaDettaglioIscrizione();
		BeanUtils.copyProperties(nuovoValore, result);
		result.setListaDettaglioIscrizione(listaDettaglio);
		return iscrizioneCentroInfanziaRepository.save(result);
	}

	private IscrizioneCentroInfanzia inserisciNuovoIscritto(@Valid IscrizioneCentroInfanziaBean inputData) {
		final IscrizioneCentroInfanzia iscrizione = iscrizioneCentroInfanziaRepository.save(convertBean(inputData));
		final List<DettaglioRataBean> rateDefault = RataUtils.getListaRateDefault();
		for (final DettaglioRataBean dettaglioRataBean : rateDefault) {
			DettaglioIscrizioneCentroInfanzia dettaglio = convertDettaglioBean(inputData, dettaglioRataBean);
			dettaglio.setIscrizione(iscrizione);
			dettaglio = dettaglioIscrizioneCentroInfanziaRepository.save(dettaglio);
			iscrizione.getListaDettaglioIscrizione().add(dettaglio);
		}
		return iscrizione;
	}

	public void rigeneraRateMinore(String cfMinore, String annoScolastico, String nomeIstituto, String fascia,
			String fasciaOraria, Long idRecord) {
		final List<IscrizioneCentroInfanzia> listaIstanze = getListaIstanza(cfMinore, annoScolastico, idRecord);
		for (IscrizioneCentroInfanzia istanza : listaIstanze) {
			if (istanza.getListaDettaglioIscrizione().size() == 0) {
				final List<DettaglioRataBean> rateDefault = RataUtils.getListaRateDefault();
				for (final DettaglioRataBean dettaglioRataBean : rateDefault) {

					final IscrizioneCentroInfanziaBean inputData = getInputData(nomeIstituto, fascia, fasciaOraria);
					DettaglioIscrizioneCentroInfanzia dettaglio = convertDettaglioBean(inputData, dettaglioRataBean);
					dettaglio.setValoreRata(0f);
					dettaglio.setIscrizione(istanza);

					dettaglio = dettaglioIscrizioneCentroInfanziaRepository.save(dettaglio);
					istanza.getListaDettaglioIscrizione().add(dettaglio);
				}
			} else if (!IscrizioneConstants.CONFERMATA_ISCRIZIONE_SI.equals(istanza.getConfermataIscrizione())) {
				for (DettaglioIscrizioneCentroInfanzia iscrizioneCentroInfanzia : istanza
						.getListaDettaglioIscrizione()) {
					iscrizioneCentroInfanzia.setNomeIstituto(nomeIstituto);
					iscrizioneCentroInfanzia.setFascia(fascia);
					iscrizioneCentroInfanzia.setFasciaOraria(fasciaOraria);
				}
				iscrizioneCentroInfanziaRepository.save(istanza);
			}
		}
	}

	public void rigeneraRateMinore(String cfMinore, String annoScolastico, String nomeIstituto, String fascia,
			String fasciaOraria) {
		rigeneraRateMinore(cfMinore, annoScolastico, nomeIstituto, fascia, fasciaOraria, null);
	}

	private IscrizioneCentroInfanziaBean getInputData(String nomeIstituto, String fascia, String fasciaOraria) {
		final IscrizioneCentroInfanziaBean dettaglio = new IscrizioneCentroInfanziaBean();
		dettaglio.setFascia(fascia);
		dettaglio.setFasciaOraria(fasciaOraria);
		dettaglio.setNomeIstituto(nomeIstituto);
		return dettaglio;
	}

	private DettaglioIscrizioneCentroInfanzia convertDettaglioBean(@Valid IscrizioneCentroInfanziaBean inputData,
			DettaglioRataBean dettaglioRataBean) {
		final DettaglioIscrizioneCentroInfanzia dettaglio = new DettaglioIscrizioneCentroInfanzia();
		dettaglio.setDescrizione(dettaglioRataBean.getDescrizione());
		dettaglio.setFascia(inputData.getFascia());
		dettaglio.setFasciaOraria(inputData.getFasciaOraria());
		dettaglio.setNomeIstituto(inputData.getNomeIstituto());
		dettaglio.setSort(dettaglioRataBean.getOrdinamento());
		dettaglio.setValoreConguaglio(0f);
		return dettaglio;
	}

	private IscrizioneCentroInfanzia convertBean(IscrizioneCentroInfanziaBean inputData) {
		final IscrizioneCentroInfanzia iscrizione = new IscrizioneCentroInfanzia();
		iscrizione.setId(inputData.getId());
		iscrizione.setAnnoScolastico(inputData.getAnnoScolastico());
		iscrizione.setCfMinore(inputData.getCfMinore());
		iscrizione.setCfRichiedente(inputData.getCfRichiedente());
		iscrizione.setCognomeMinore(inputData.getCognomeMinore());
		iscrizione.setCognomeRichiedente(inputData.getCognomeRichiedente());
		iscrizione.setConfermaNotificata(null);
		iscrizione.setConfermataIscrizione(inputData.getConfermataIscrizione());
		iscrizione.setDataFineIscrizione(inputData.getDataFineIscrizione());
		iscrizione.setDataInizioIscrizione(inputData.getDataInizioIscrizione());
		iscrizione.setDataNascitaMinore(inputData.getDataNascitaMinore());
		iscrizione.setEmailUtente(inputData.getEmailUtente());
		iscrizione.setEsitoValutazione(inputData.getEsitoValutazione());
		iscrizione.setIdFamiglia(inputData.getIdFamiglia());
		iscrizione.setNomeMinore(inputData.getNomeMinore());
		iscrizione.setNomeRichiedente(inputData.getNomeRichiedente());
		iscrizione.setTipoRichiedente(inputData.getTipoRichiedente());
		iscrizione.setConfermaNotificata(inputData.getConfermaNotificata());
		iscrizione.setRicalcoloRate(inputData.getRicalcoloRate());
		return iscrizione;
	}

	private DettaglioIscrizioneCentroInfanzia getDettaglioBean(Long idRecord) {
		if (idRecord == null) {
			throw new ServiceException("ID DettaglioIscrizioneCentroInfanziaBean null");
		}
		final DettaglioIscrizioneCentroInfanzia dettaglioIscrizione = dettaglioIscrizioneCentroInfanziaRepository
				.findById(idRecord).orElse(null);
		if (dettaglioIscrizione == null) {
			throw new ServiceException("DettaglioIscrizioneCentroInfanzia non trovato per ID: " + idRecord);
		}

		return dettaglioIscrizione;
	}

	public DettaglioIscrizioneCentroInfanzia salvaDettaglioSuccessivi(
			@Valid DettaglioIscrizioneCentroInfanziaBean inputData, String utenteModifica) {
		final DettaglioIscrizioneCentroInfanzia iscrizioneDettaglio = salvaDettaglio(inputData, utenteModifica);
		final List<DettaglioIscrizioneCentroInfanzia> listaDettagliIscrizione = iscrizioneDettaglio.getIscrizione()
				.getListaDettaglioIscrizione();

		for (final DettaglioIscrizioneCentroInfanzia dettaglioIscrizioneCentroInfanzia : listaDettagliIscrizione) {
			if (dettaglioIscrizioneCentroInfanzia.getSort() > iscrizioneDettaglio.getSort()) {
				dettaglioIscrizioneCentroInfanzia.setNomeIstituto(inputData.getNomeIstituto());
				dettaglioIscrizioneCentroInfanzia.setFascia(inputData.getFascia());
				dettaglioIscrizioneCentroInfanzia.setFasciaOraria(inputData.getFasciaOraria());
			}
		}
		iscrizioneCentroInfanziaRepository.save(iscrizioneDettaglio.getIscrizione());
		return iscrizioneDettaglio;
	}

	public DettaglioIscrizioneCentroInfanzia salvaDettaglio(@Valid DettaglioIscrizioneCentroInfanziaBean inputData,
			String utenteModifica) {
		DettaglioIscrizioneCentroInfanzia iscrizioneDettaglio = getDettaglioBean(inputData.getId());
		final Optional<DettaglioIscrizioneCentroInfanzia> dettaglioOriginale = getDettaglioOriginale(
				iscrizioneDettaglio);

		BeanUtils.copyProperties(inputData, iscrizioneDettaglio);
		iscrizioneDettaglio = dettaglioIscrizioneCentroInfanziaRepository.save(iscrizioneDettaglio);
		centroInfanziaLogRepository.save(getLog(dettaglioOriginale.get(), iscrizioneDettaglio, utenteModifica,
				"RICALCOLO_RATA", inputData.getMotivazioneVariazione()));
		return iscrizioneDettaglio;
	}

	private List<IscrizioneCentroInfanzia> getListaIstanza(String cfMinore, String annoScolastico, Long idRecord) {
		List<IscrizioneCentroInfanzia> listaIstanze = null;
		if (idRecord != null) {
			IscrizioneCentroInfanzia recordTrovato = iscrizioneCentroInfanziaRepository.findById(idRecord).orElse(null);

			if (recordTrovato == null) {
				throw new ServiceException("Dati istanza centro infanzia non trovati per ID: " + idRecord);
			}
			listaIstanze = new ArrayList<>();
			listaIstanze.add(recordTrovato);
		} else if (StringUtils.hasText(cfMinore)) {
			listaIstanze = iscrizioneCentroInfanziaRepository.findByCfMinoreAndAnnoScolastico(cfMinore, annoScolastico);
		} else {
			listaIstanze = iscrizioneCentroInfanziaRepository.findByAnnoScolastico(annoScolastico);
		}

		return listaIstanze;
	}

	public List<IscrizioneCentroInfanzia> getListaIstanzaConfermate(String cfMinore, String annoScolastico) {
		return iscrizioneCentroInfanziaRepository
				.findByCfMinoreAndEsitoValutazioneAndConfermataIscrizioneAndAnnoScolastico(cfMinore,
						IscrizioneConstants.ESITO_AMMESSO, IscrizioneConstants.CONFERMATA_ISCRIZIONE_SI,
						annoScolastico);
	}

	public Map<String, Serializable> prossimaRata(String cfMinore, String annoScolastico) {
		final HashMap<String, Serializable> obj = new HashMap<String, Serializable>();
		cfMinore = cfMinore.toUpperCase();
		if (annoScolastico == null) {
			annoScolastico = DateUtils.getAnnoScolasticoAttuale();
		}
		logger.debug("prossimarata: {} {}", cfMinore, annoScolastico);

		final Optional<IscrizioneCentroInfanzia> resultIstanza = getIstanzaNotificata(cfMinore, annoScolastico);
		if (!resultIstanza.isPresent()) {
			logger.debug("Codice fiscale non trovato");
			obj.put("esito", "Codice fiscale non trovato");
		} else {
			final IscrizioneCentroInfanzia istanza = resultIstanza.get();
			logger.debug("Dati trovati: {}", istanza);
			final DettaglioIscrizioneCentroInfanzia rata = findProssimaRataByCfIscrittoAndAnnoScolastico(cfMinore,
					annoScolastico);
			obj.put("nome", istanza.getNomeMinore());
			obj.put("cognome", istanza.getCognomeMinore());
			obj.put("codicefiscale", istanza.getCfMinore());
			obj.put("importo", "0");
			if (rata == null) {
				obj.put("esito", "Nessuna rata da pagare");
			} else {
				logger.debug("Prossima rata: {}", rata);
				obj.put("importo", rata.getImportoTotale().toString());
				obj.put("esito", "Dati recuperati con successo");
				obj.put("rata", rata);
			}
		}
		return obj;
	}

	private DettaglioIscrizioneCentroInfanzia findProssimaRataByCfIscrittoAndAnnoScolastico(String cfMinore,
			String annoScolastico) {
		final List<IscrizioneCentroInfanzia> listaIstanze = getListaIstanzaConfermate(cfMinore, annoScolastico);
		DettaglioIscrizioneCentroInfanzia result = null;
		if (listaIstanze.size() > 0) {
			final IscrizioneCentroInfanzia istanza = listaIstanze.get(0);
			final List<DettaglioIscrizioneCentroInfanzia> listaDettaglio = istanza.getListaDettaglioIscrizione();
			for (DettaglioIscrizioneCentroInfanzia dettaglioIscrizioneCentroInfanzia : listaDettaglio) {
				if (!dettaglioIscrizioneCentroInfanzia.isPagato()
						&& dettaglioIscrizioneCentroInfanzia.getImportoTotale() > 0) {
					result = dettaglioIscrizioneCentroInfanzia;
					break;
				}
			}
		}

		return result;
	}

	public List<DettaglioMinoreBean> getListaFigli(@NotBlank String cfContribuente, @NotBlank String annoScolastico) {
		cfContribuente = cfContribuente.toUpperCase();
		logger.debug("ricercaFigli: {} - {}", cfContribuente, annoScolastico);
		final List<DettaglioMinoreBean> result = new ArrayList<>();
		final List<IscrizioneCentroInfanzia> listaMinori = iscrizioneCentroInfanziaRepository
				.findByCfRichiedenteAndConfermaNotificataAndAnnoScolastico(cfContribuente,
						IscrizioneConstants.NOTIFICATA_ISCRIZIONE_SI, annoScolastico);

		for (final IscrizioneCentroInfanzia istanza : listaMinori) {
			logger.debug("trovato figlio: {}", istanza);
			result.add(getDettaglioMinore(istanza));
		}
		return result;
	}

	private DettaglioMinoreBean getDettaglioMinore(IscrizioneCentroInfanzia istanza) {
		final DettaglioMinoreBean result = new DettaglioMinoreBean();
		result.setCodicefiscale(istanza.getCfMinore());
		result.setCognome(istanza.getCognomeMinore());
		result.setNome(istanza.getNomeMinore());
		return result;
	}

	public DettaglioIscrizioneCentroInfanzia salvaPagamento(PagamentoCentroInfanziaBean inputData) {
		DettaglioIscrizioneCentroInfanzia iscrizioneDettaglio = getDettaglioBean(inputData.getId());
		logger.debug("salvaPagamento jsondoc: {}", inputData.getJsonDoc());
		BeanUtils.copyProperties(inputData, iscrizioneDettaglio);
		if (inputData.getJsonDoc() != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				@SuppressWarnings("unchecked")
				final Map<String, Object> jsonDocMap = mapper.readValue(inputData.getJsonDoc(), Map.class);
				iscrizioneDettaglio.setJsonDoc(jsonDocMap);
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage(), e);
			}
		}

		return dettaglioIscrizioneCentroInfanziaRepository.save(iscrizioneDettaglio);
	}

	public List<CentroInfanziaTariffe> getListaTariffeCentriScolastici() {
		final Sort sort = Sort.by("denominazioneAsilo").ascending().and(Sort.by("fasciaOraria1").ascending());
		return centroInfanziaTariffeRepository.findAll(sort);
	}

	public Optional<CentroInfanziaLog> getDatiLogCentroInfanzia(@NotNull Long idRecord) {
		return centroInfanziaLogRepository.findById(idRecord);
	}

	public Optional<IscrizioneCentroInfanzia> findById(@NotNull Long idRecord) {
		return iscrizioneCentroInfanziaRepository.findById(idRecord);
	}

	public Optional<DettaglioIscrizioneCentroInfanzia> findDettaglioById(@NotNull Long idRecord) {
		return dettaglioIscrizioneCentroInfanziaRepository.findById(idRecord);
	}

	public List<DettaglioIscrizioneCentroInfanzia> findDettaglioByIdIstanza(Long idIscrizione) {
		Optional<IscrizioneCentroInfanzia> iscrizione = findById(idIscrizione);
		if (iscrizione.isPresent()) {
			return dettaglioIscrizioneCentroInfanziaRepository
					.findAllByIscrizioneIdAndValoreRataNotNullOrderBySort(idIscrizione);
		}
		return new ArrayList<>();
	}

	public Optional<IscrizioneCentroInfanzia> getIstanzaNotificata(@NotBlank String cfMinore,
			@NotBlank String annoScolastico) {
		return iscrizioneCentroInfanziaRepository.findOneByConfermaNotificataAndCfMinoreAndAnnoScolastico(
				IscrizioneConstants.NOTIFICATA_ISCRIZIONE_SI, cfMinore.toUpperCase(), annoScolastico);
	}

}
