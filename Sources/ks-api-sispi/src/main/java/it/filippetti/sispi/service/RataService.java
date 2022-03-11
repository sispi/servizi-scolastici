package it.filippetti.sispi.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.filippetti.sispi.model.AsiloTariffe;
import it.filippetti.sispi.model.FasciaIstituto;
import it.filippetti.sispi.model.Isee;
import it.filippetti.sispi.model.Istanza;
import it.filippetti.sispi.model.Rata;
import it.filippetti.sispi.model.RataLog;
import it.filippetti.sispi.pagamentoretta.DateUtils;
import it.filippetti.sispi.pagamentoretta.DettaglioMinoreBean;
import it.filippetti.sispi.pagamentoretta.DettaglioRataBean;
import it.filippetti.sispi.pagamentoretta.RataUtils;
import it.filippetti.sispi.repository.AsiloTariffeRepository;
import it.filippetti.sispi.repository.IstanzaRepository;
import it.filippetti.sispi.repository.RataLogRepository;
import it.filippetti.sispi.repository.RataRepository;

@Service
@Transactional
@Validated
public class RataService {

	private static final Logger logger = LoggerFactory.getLogger(RataService.class);

	public static Float IMPORTO_ISEE_SCONTO = 15000f;

	@Autowired
	private RataRepository rataRepository;

	@Autowired
	private FasciaIstitutoService fasciaIstitutoService;

	@Autowired
	private RataLogRepository rataLogRepository;

	@Autowired
	private IstanzaRepository istanzaRepository;

	@Autowired
	private AsiloTariffeRepository asiloTariffeRepository;

	@Autowired
	private IseeService iseeService;

	@Autowired
	private IstanzaService istanzaService;

	public List<Rata> ricalcolaRateIscritto(String cfMinore, String utenteModifica, String annoScolastico) {
		return ricalcolaRateIscritto(cfMinore, utenteModifica, annoScolastico, null, null);
	}

	public List<Rata> ricalcolaRateIscritto(String cfMinore, String utenteModifica, String annoScolastico,
			String istanzaOriginaleJson, String causale) {
		cfMinore = cfMinore.toUpperCase();
		List<Rata> res = new ArrayList<>();
		final List<Istanza> listaIstanze = istanzaService.getListaIstanzaConfermate(cfMinore, annoScolastico);

		if (listaIstanze.isEmpty()) {
			throw new ServiceException("Nessun record valido per: " + cfMinore);
		}

		final Istanza istanza = listaIstanze.iterator().next();
		final Istanza istanzaOriginale = getIstanzaOriginale(istanza, istanzaOriginaleJson);

		if (istanza.getDataFineIscrizioneDate() != null && istanza.getDataInizioIscrizioneDate() != null) {
			// valorizzo lo sconto_famiglia per poter effettuare il calcolo dell importo
			istanza.setRicalcoloRate("");
			if (istanza.isAsiloNido()) {
				final List<Isee> listaIsee = iseeService.getListaIsee(cfMinore, Boolean.TRUE);
				if (listaIsee.isEmpty()) {
					throw new ServiceException("Isee valida non trovata per: " + cfMinore);
				}

				final List<FasciaIstituto> listaFasciaIstituto = fasciaIstitutoService.getListaFasciaIstituto(istanza);
				res = aggiornaRate(istanza, listaIsee, utenteModifica, listaFasciaIstituto, causale);
			} else {
				istanza.setImportoRetta(0f);
			}
			istanzaRepository.save(istanza);
			rataLogRepository.save(getRataLog(istanzaOriginale, istanza, utenteModifica, "RICALCOLO_ISTANZA", null));
		} else {
			throw new ServiceException("Data inizio o fine non valorizzata per ID_COD: " + istanza.getId());
		}

		return res;
	}

	public List<DettaglioMinoreBean> getListaFigli(String cfContribuente, String annoScolastico) {
		final List<DettaglioMinoreBean> result = new ArrayList<>();
		final List<Istanza> datiGenitore = istanzaRepository
				.findByCfContribuenteAndEsitoValutazioneAndAnnoScolasticoAndIdFamigliaNotNull(cfContribuente,
						IscrizioneConstants.ESITO_AMMESSO, annoScolastico);

		for (final Istanza singoloValore : datiGenitore) {
			logger.debug("trovati dati genitore: {}", singoloValore);
			if (singoloValore.getIdFamiglia() != null && singoloValore.getIdFamiglia() > 0) {
				final List<Istanza> listaMinori = istanzaRepository
						.findByConfermaNotificataAndIdFamigliaAndAnnoScolastico(
								IscrizioneConstants.NOTIFICATA_ISCRIZIONE_SI, singoloValore.getIdFamiglia(),
								annoScolastico);
				for (final Istanza istanza : listaMinori) {
					logger.debug("trovato figlio: {}", istanza);
					result.add(getDettaglioMinore(istanza));
				}
				break;
			}
		}

		return result;
	}

	private DettaglioMinoreBean getDettaglioMinore(Istanza istanza) {
		final DettaglioMinoreBean result = new DettaglioMinoreBean();
		result.setCodicefiscale(istanza.getCfMinore());
		result.setCognome(istanza.getCognomeMinore());
		result.setNome(istanza.getNomeMinore());
		return result;
	}

	private List<Rata> aggiornaRate(Istanza istanza, List<Isee> listaIsee, String utenteModifica,
			List<FasciaIstituto> listaFasciaIstituto, String causale) {
		final List<Rata> res = new ArrayList<>();
		final List<DettaglioRataBean> rateDefault = RataUtils.getListaRateDefault();
		final List<Rata> listaRate = rataRepository.findByCfIscrittoAndAnnoScolastico(istanza.getCfMinore(),
				istanza.getAnnoScolastico());
		for (final DettaglioRataBean dettaglioRataBean : rateDefault) {
			final Rata rataSalvata = trovaRataSalvata(dettaglioRataBean, listaRate);
			final FasciaIstituto fasciaRiferimento = getFasciaRiferimento(listaFasciaIstituto,
					dettaglioRataBean.getOrdinamento());
			if (RataUtils.isIntervalloValido(istanza.getDataInizioIscrizioneDate(), istanza.getDataFineIscrizioneDate(),
					dettaglioRataBean, istanza.getAnnoScolastico())) {
				final Optional<Rata> rataOriginale = getRataOriginale(rataSalvata);

				Rata rataDaSalvare = getRataDaSalvare(dettaglioRataBean, rataSalvata, istanza, listaIsee,
						fasciaRiferimento);
				rataDaSalvare = rataRepository.save(rataDaSalvare);
				if (rataOriginale.isPresent()) {
					rataLogRepository.save(
							getRataLog(rataOriginale.get(), rataDaSalvare, utenteModifica, "RICALCOLO_RATA", causale));
				}
				fasciaRiferimento.setRata(rataDaSalvare);
				res.add(rataDaSalvare);
			} else if (rataSalvata != null && rataSalvata.getId() != null && rataSalvata.getPagamenti().size() == 0) {
				rataRepository.delete(rataSalvata);
				fasciaRiferimento.setRata(null);
			}
			fasciaIstitutoService.salvaDati(fasciaRiferimento);
		}
		return res;
	}

	private RataLog getRataLog(Object valoreOriginale, Object valoreDaSalvare, String utenteModifica,
			String tipoModifica, String causale) {
		final RataLog rataLog = new RataLog();
		rataLog.setDataModifica(LocalDate.now());
		rataLog.setDatiRecord(getDatiRecord(valoreOriginale, valoreDaSalvare));
		if (Rata.class.isInstance(valoreDaSalvare)) {
			rataLog.setRata((Rata) valoreDaSalvare);
			rataLog.setCausale(((Rata) valoreDaSalvare).getMotivazioneVariazione());
		}
		if (causale != null) {
			rataLog.setCausale(causale);
		}
		rataLog.setTipoModifica(tipoModifica);
		rataLog.setUtenteModifica(utenteModifica);
		return rataLog;
	}

	private Map<String, Object> getDatiRecord(Object objPre, Object objPost) {
		final Map<String, Object> datiJson = new HashMap<String, Object>();
		final String chiavePrefix = getChiavePrefix(objPre);
		datiJson.put(chiavePrefix + "Pre", objPre);
		datiJson.put(chiavePrefix + "Post", objPost);
		return datiJson;
	}

	private String getChiavePrefix(Object objPre) {
		String result = null;
		if (Istanza.class.isInstance(objPre)) {
			result = "istanza";
		} else if (Rata.class.isInstance(objPre)) {
			result = "rata";
		}
		return result;
	}

	private Optional<Rata> getRataOriginale(Rata rataSalvata) {
		if (rataSalvata != null) {
			final Rata result = new Rata();
			BeanUtils.copyProperties(rataSalvata, result);
			return Optional.ofNullable(result);
		}
		return Optional.empty();
	}

	private Istanza getIstanzaOriginale(Istanza istanza, String istanzaOriginaleJson) {
		Istanza result = new Istanza();
		if (istanzaOriginaleJson != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				result = mapper.readValue(istanzaOriginaleJson, Istanza.class);
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage(), e);
			}

		} else {
			// TODO: non credo serva
			BeanUtils.copyProperties(istanza, result);
		}

		return result;
	}

	private String getScontoFamigliaRata(Istanza istanza, Isee iseeRiferimento, Integer ordinamentoRata) {
		String scontoFamiglia = "N";
		if (iseeRiferimento.getImporto() <= IMPORTO_ISEE_SCONTO && istanza.getIdFamiglia() != null
				&& istanza.getIdFamiglia() > 0 && !"Casa famiglia".equals(istanza.getTipoRichiedente())) {
			// FIX:
			final List<Istanza> listaRecordFamiglia = istanzaRepository
					.findByIdFamigliaAndConfermaNotificataAndAnnoScolastico(istanza.getIdFamiglia(),
							IscrizioneConstants.NOTIFICATA_ISCRIZIONE_SI, istanza.getAnnoScolastico());
			for (final Istanza recordFamiglia : listaRecordFamiglia) {
				if (recordFamiglia.getId() != istanza.getId() && recordFamiglia.isAsiloNido()) {
					final List<Rata> rate = rataRepository.findByCfIscrittoAndAnnoScolasticoAndSort(
							recordFamiglia.getCfMinore(), istanza.getAnnoScolastico(), ordinamentoRata);

					for (final Rata rata : rate) {
						if (!"S".equals(rata.getScontoFamiglia())) {
							scontoFamiglia = "S";
						}
					}
				}
			}
		}
		istanza.setScontoFamiglia(scontoFamiglia);
		return scontoFamiglia;
	}

//	private Boolean isIntervalloValido(Istanza istanza, DettaglioRataBean dettaglioRataBean) {
//		Boolean result = Boolean.TRUE;
//		final Date dataInizio = DateUtils.setDayOfMonth(istanza.getDataInizioIscrizioneDate(), 1);
//		final Date dataFine = istanza.getDataFineIscrizioneDate();
//		final Date dataRata = DateUtils.getDateFromMonth(dettaglioRataBean.getMeseRiferimento(),
//				istanza.getAnnoScolastico());
//		if (dataRata.compareTo(dataInizio) >= 0 && dataRata.compareTo(dataFine) <= 0) {
//			result = Boolean.TRUE;
//		} else {
//			result = Boolean.FALSE;
//		}
//		return result;
//	}

	private Rata trovaRataSalvata(DettaglioRataBean dettaglioRataBean, List<Rata> listaRate) {
		Rata result = null;
		for (final Rata rata : listaRate) {
			if (dettaglioRataBean.getOrdinamento() == rata.getSort()) {
				result = rata;
				break;
			}
		}
		return result;
	}

	private Rata getRataDaSalvare(DettaglioRataBean dettaglioRataBean, Rata rataSalvata, Istanza istanza,
			List<Isee> listaIsee, FasciaIstituto fasciaRiferimento) {
		Rata result = rataSalvata;
		if (rataSalvata == null) {
			result = new Rata();
		}
		final Isee iseeRiferimento = iseeService.getIseeFromMonth(dettaglioRataBean.getMeseRiferimento(),
				istanza.getAnnoScolastico(), listaIsee);

		result.setAnnoScolastico(istanza.getAnnoScolastico());
		result.setCfIscritto(istanza.getCfMinore());
		result.setDescrizione(dettaglioRataBean.getDescrizione());
		result.setSort(dettaglioRataBean.getOrdinamento());
		result.setScontoFamiglia(getScontoFamigliaRata(istanza, iseeRiferimento, dettaglioRataBean.getOrdinamento()));

		final List<AsiloTariffe> listaTariffeIstituto = asiloTariffeRepository
				.findByDenominazioneAsiloAndDescrizioneCategoriaAndAnnoRiferimento(fasciaRiferimento.getNomeIstituto(),
						fasciaRiferimento.getFascia().toUpperCase(),
						DateUtils.getAnnoRiferimentoByAnnoScolastico(istanza.getAnnoScolastico()));

		final Float valoreTariffaBase = getTariffa(fasciaRiferimento, iseeRiferimento, listaTariffeIstituto, result);

		result.setValoreRata(getValoreRata(valoreTariffaBase, dettaglioRataBean.getPercentualeSconto()));

		// Imposto sempre il valore in modo da avere l'ultimo calcolato
		istanza.setImportoRetta(valoreTariffaBase);
		return result;
	}

	private FasciaIstituto getFasciaRiferimento(List<FasciaIstituto> listaFasciaIstituto, Integer ordinamento) {
		FasciaIstituto result = null;
		for (FasciaIstituto fasciaIstituto : listaFasciaIstituto) {
			if (fasciaIstituto.getSort() == ordinamento) {
				result = fasciaIstituto;
			}
		}
		return result;
	}

	private String getValoreRata(Float importoRetta, Float percentualeSconto) {
		Float result = importoRetta;
		if (percentualeSconto != null) {
			result = importoRetta * (1 - (percentualeSconto / 100));
		}
		return result.toString();
	}

	private Float getTariffa(FasciaIstituto fasciaIstituto, Isee iseeRiferimento,
			List<AsiloTariffe> listaTariffeIstituto, Rata rata) {
		Float result = 0f;
		final List<AsiloTariffe> listaTariffeFascia = new ArrayList<>();
		for (final AsiloTariffe asiloTariffe : listaTariffeIstituto) {
			if (asiloTariffe.getFasciaOraria1() != null
					&& asiloTariffe.getFasciaOraria1().equals(fasciaIstituto.getFasciaOraria())) {
				listaTariffeFascia.add(asiloTariffe);
			}
			if (asiloTariffe.getFasciaOraria2() != null
					&& asiloTariffe.getFasciaOraria2().equals(fasciaIstituto.getFasciaOraria())) {
				listaTariffeFascia.add(asiloTariffe);
			}
			if (asiloTariffe.getFasciaOraria3() != null
					&& asiloTariffe.getFasciaOraria3().equals(fasciaIstituto.getFasciaOraria())) {
				listaTariffeFascia.add(asiloTariffe);
			}
			if (asiloTariffe.getFasciaOraria4() != null
					&& asiloTariffe.getFasciaOraria4().equals(fasciaIstituto.getFasciaOraria())) {
				listaTariffeFascia.add(asiloTariffe);
			}
			if (asiloTariffe.getFasciaOraria5() != null
					&& asiloTariffe.getFasciaOraria5().equals(fasciaIstituto.getFasciaOraria())) {
				listaTariffeFascia.add(asiloTariffe);
			}
		}

		for (AsiloTariffe singolaTarffa : listaTariffeFascia) {
			Float iseeDaFloat = Float.MIN_VALUE;
			if (singolaTarffa.getImportoIseeDa() != null) {
				iseeDaFloat = singolaTarffa.getImportoIseeDa();
			}

			Float iseeAFloat = Float.MAX_VALUE;
			if (singolaTarffa.getImportoIseeA() != null) {
				iseeAFloat = singolaTarffa.getImportoIseeA();
			}

			if (iseeRiferimento.getImporto() >= iseeDaFloat && iseeRiferimento.getImporto() <= iseeAFloat) {
				result = getImporto(singolaTarffa, fasciaIstituto.getFasciaOraria(), rata.getGiorniFrequenza());
				break;
			}
		}
		if ("S".equals(rata.getScontoFamiglia())) {
			// Ho trovato almeno 2 elementi (uno è il record "istanza")
			result = result / 2;
		}

		return result;
	}

//	public Float getTariffa(Istanza istanza, Isee iseeRiferimento, Rata rata) {
//
//		final List<AsiloTariffe> listaTariffeIstituto = asiloTariffeRepository
//				.findByDenominazioneAsiloAndDescrizioneCategoriaAndAnnoRiferimento(istanza.getNomeIstituto(),
//						istanza.getFascia().toUpperCase(), DateUtils.getAnnoRiferimento());
//		return getTariffa(istanza, iseeRiferimento, listaTariffeIstituto, rata);
//	}

	private Float getImporto(AsiloTariffe singolaTarffa, String fasciaOraria, String giorniFrequenza) {
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

		result = calcolaValoreGiorniFrequenza(result, giorniFrequenza);

		return result;
	}

	private Float calcolaValoreGiorniFrequenza(Float importoRata, String giorniFrequenza) {
		if (giorniFrequenza == null) {
			// Nel caso in cui non ho salvato la rata con questo nuovo valore (record creati
			// prima di aggiungere questa colonna)
			giorniFrequenza = IscrizioneConstants.GIORNI_FREQUENZA_DEFAULT.toString();
		}
		final Float giorniFrequenzaFloat = RataUtils.convertStringToFloat(giorniFrequenza);
		final Float giorniFrequenzaDefaultFloat = RataUtils
				.convertStringToFloat(IscrizioneConstants.GIORNI_FREQUENZA_DEFAULT.toString());
		final Float result = (importoRata / giorniFrequenzaDefaultFloat) * giorniFrequenzaFloat;
		return (float) (Math.round(result * 100.0) / 100.0);
	}

	public void ricalcolaRateFamiglia(String cfMinore, String utenteModifica, String annoScolastico) {
		final List<Istanza> listaIstanze = istanzaService.getListaIstanzaConfermate(cfMinore, annoScolastico);
		if (listaIstanze.size() > 0) {
			final Istanza istanza = listaIstanze.iterator().next();
			if (istanza.getIdFamiglia() != null && istanza.getIdFamiglia() > 0) {
				final List<Istanza> listaRecordFamiglia = istanzaRepository
						.findByIdFamigliaAndConfermaNotificataAndAnnoScolastico(istanza.getIdFamiglia(),
								IscrizioneConstants.NOTIFICATA_ISCRIZIONE_SI, annoScolastico);

				for (final Istanza recordFamiglia : listaRecordFamiglia) {
					if (recordFamiglia.getId() != istanza.getId() && recordFamiglia.isAsiloNido()) {
						ricalcolaRateIscritto(recordFamiglia.getCfMinore(), utenteModifica, annoScolastico);
					}
				}
			}
		}
	}

	public Rata aggiornaRata(@Valid Rata inputData, String utenteModifica) {
		Optional<Rata> rataSalvata = rataRepository.findById(inputData.getId());
		if (!rataSalvata.isPresent()) {
			throw new ServiceException("Rata non trovata!");
		}
		Rata rataDaSalvare = rataSalvata.get();
		final Optional<Rata> rataOriginale = getRataOriginale(rataDaSalvare);
		rataDaSalvare.setGiorniFrequenza(inputData.getGiorniFrequenza());
		rataDaSalvare.setMotivazioneVariazione(inputData.getMotivazioneVariazione());
		rataDaSalvare.setValoreConguaglio(inputData.getValoreConguaglio());
		rataDaSalvare.setValoreRata(inputData.getValoreRata());

		rataDaSalvare = rataRepository.save(rataDaSalvare);
		rataLogRepository.save(getRataLog(rataOriginale.get(), rataDaSalvare, utenteModifica, "AGGIORNA_RATA", null));
		return rataDaSalvare;
	}

	public Rata getRataById(Long idRata) {
		final Optional<Rata> result = rataRepository.findById(idRata);
		return result.orElse(null);
	}

}
