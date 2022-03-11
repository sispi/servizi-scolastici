package it.filippetti.sispi.service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import it.filippetti.sispi.iscrizioni.ImportIscrizioniBean;
import it.filippetti.sispi.model.Isee;
import it.filippetti.sispi.model.ScuIscrizioni;
import it.filippetti.sispi.repository.IseeRepository;
import it.filippetti.sispi.repository.ScuIscrizioniRepository;

@Service
@Validated
public class ScuIscrizioniService {

	private static final Logger logger = LoggerFactory.getLogger(ScuIscrizioniService.class);

	@Autowired
	private ScuIscrizioniRepository scuIscrizioniRepository;

	@Autowired
	private IseeService iseeService;

	@Autowired
	private IseeRepository iseeRepository;

	public List<String> getTipoRichistaAsilo() {
		return Arrays.asList("iscrizione_asilo_nido", "riconferma_asilo_nido", "trasferimento_asilo_nido");
	}

	public List<ScuIscrizioni> getListaIscrizioniAsiloValide() {
		return scuIscrizioniRepository.findByTipoRichiestaInAndIseeNotNullAndCfMinoreNotNull(getTipoRichistaAsilo());
//		return null;
	}

	public ImportIscrizioniBean aggiornaIsee(String cfMinore) {
		final ImportIscrizioniBean result = new ImportIscrizioniBean();
		List<ScuIscrizioni> listaIscrizioni = null;
		if (cfMinore != null) {
			logger.debug("Aggiorno isee per CF_MINORE: {}", cfMinore);
			listaIscrizioni = scuIscrizioniRepository
					.findByTipoRichiestaInAndIseeNotNullAndCfMinore(getTipoRichistaAsilo(), cfMinore);
		} else {
			listaIscrizioni = scuIscrizioniRepository
					.findByTipoRichiestaInAndIseeNotNullAndCfMinoreNotNull(getTipoRichistaAsilo());
		}
		for (final ScuIscrizioni scuIscrizioni : listaIscrizioni) {
			try {
				aggiornaSingoloIsee(scuIscrizioni);
				result.getIseeImportati().add(scuIscrizioni.getCfMinore());
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
				result.getIseeError().add(scuIscrizioni.getCfMinore());
			}
		}

		return result;

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void aggiornaSingoloIsee(ScuIscrizioni scuIscrizioni) {

		final Isee ultimoIsee = iseeService.getIseeAttivo(scuIscrizioni.getCfMinore());
		Isee datiIsee = getIseeDaScuIscrizioni(scuIscrizioni);
		if (ultimoIsee != null) {
			// Se esiste un isee il nuovo ha validita dal primo agosto
			datiIsee.setDataInizioValidita(setGiornoMese(1, 8));
//			iseeService.fixDateValidita(datiIsee);
			if (ultimoIsee.getDataInizioValidita().compareTo(datiIsee.getDataInizioValidita()) >= 0) {
				throw new ServiceException(
						"Esiste un elemento con data inizio validità maggiore o uguale di quella da aggiornare: "
								+ iseeService.getSdf().format(ultimoIsee.getDataInizioValidita()));
			}

			ultimoIsee.setDataFineValidita(datiIsee.getDataInizioValidita().minusDays(1));
			iseeRepository.save(ultimoIsee);
		}

		datiIsee = iseeRepository.save(datiIsee);
//		iseeRepository.flush();
	}

	private Isee getIseeDaScuIscrizioni(ScuIscrizioni scuIscrizioni) {
		final Isee result = new Isee();
		result.setAnno(LocalDate.now().get(ChronoField.YEAR));
		result.setCausale("Aggiornamento automatico da SCU_ISCRIZIONI2021");
		result.setCodiceFiscale(scuIscrizioni.getCfMinore());
		result.setDataFineValidita(setGiornoMese(31, 12));
		result.setDataInizioValidita(setGiornoMese(1, 1));
		result.setDataPresentazione(LocalDate.now());
		result.setImporto(scuIscrizioni.getIsee());
		result.setUtenteInseritore("admin");
		result.setUtenteRichiedente("admin");

//		iseeService.fixDateValidita(result);
		return result;
	}

	private LocalDate setGiornoMese(Integer giorno, Integer mese) {
		Integer annoAttuale = LocalDate.now().get(ChronoField.YEAR);
		return LocalDate.of(annoAttuale, mese, giorno);
	}

	public static void main(String[] args) {
		ScuIscrizioniService serv = new ScuIscrizioniService();
		LocalDate res = serv.setGiornoMese(31, 12);
		logger.debug("res 31/12: {}", res);

		res = serv.setGiornoMese(1, 1);
		logger.debug("res 1/1: {}", res);

		res = serv.setGiornoMese(1, 8);
		logger.debug("res 1/8: {}", res);
	}

}
