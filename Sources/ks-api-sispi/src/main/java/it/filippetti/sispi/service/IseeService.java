package it.filippetti.sispi.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import it.filippetti.sispi.model.Isee;
import it.filippetti.sispi.pagamentoretta.DateUtils;
import it.filippetti.sispi.repository.IseeRepository;

@Service
@Transactional
@Validated
public class IseeService {

	private static final Logger logger = LoggerFactory.getLogger(IseeService.class);

	private DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@Autowired
	private IseeRepository iseeRepository;

//	@Autowired
//	private IstanzaRepository istanzaRepository;

	public Isee aggiornaIsee(@Valid Isee datiIsee) {
		if (datiIsee.getId() != null) {
			return updateIsee(datiIsee);
		} else {
			return insertIsee(datiIsee);
		}
	}

	private Isee updateIsee(Isee datiIsee) {
		logger.debug("Update isee ID: {}", datiIsee.getId());
//		fixDateValidita(datiIsee);
		Isee result = iseeRepository.findById(datiIsee.getId()).orElse(null);
		BeanUtils.copyProperties(datiIsee, result);
		result = iseeRepository.save(result);
//		aggiornaImportoIstanza(datiIsee);
		return result;
	}

	private Isee insertIsee(Isee datiIsee) {
		logger.debug("Aggiorno dati isee CF: {}", datiIsee.getCodiceFiscale());
//		fixDateValidita(datiIsee);
		final Isee ultimoIsee = getIseeAttivo(datiIsee.getCodiceFiscale());
		if (ultimoIsee != null) {
			if (!ultimoIsee.getDataInizioValidita().isBefore(datiIsee.getDataInizioValidita())) {
				throw new ServiceException(
						"Esiste un elemento con data inizio validità maggiore o uguale di quella da aggiornare: "
								+ ultimoIsee.getDataInizioValidita().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			}

			ultimoIsee.setDataFineValidita(datiIsee.getDataInizioValidita().minusDays(1));

			iseeRepository.save(ultimoIsee);
		}
		datiIsee = iseeRepository.save(datiIsee);
		return datiIsee;
	}

	public List<Isee> getListaIsee(String codiceFiscale, Boolean soloAttivi) {
		List<Isee> listaIsee = iseeRepository.findByCodiceFiscaleOrderByDataInizioValiditaDesc(codiceFiscale);
		if (soloAttivi) {
			listaIsee = getSoloApprovati(listaIsee);
		}
		return listaIsee;
	}

	public List<Isee> getListaIsee(Boolean soloAttivi) {
		List<Isee> listaIsee = iseeRepository.findAll();
		if (soloAttivi) {
			listaIsee = getSoloApprovati(listaIsee);
		}
		return listaIsee;
	}

	private List<Isee> getSoloApprovati(List<Isee> listaIsee) {
		return listaIsee.stream().filter(singoloIsee -> IseeConstants.STATO_APPROVATO.equals(singoloIsee.getStato()))
				.collect(Collectors.toList());
	}

	public List<Isee> getListaIseeValidita(String codiceFiscale) {
		return iseeRepository.findByCodiceFiscaleOrderByDataInizioValiditaDesc(codiceFiscale);
	}

	public Isee getIseeAttivo(String codiceFiscale) {
		Isee result = null;
		final List<Isee> listaIseeAlunno = getListaIsee(codiceFiscale, Boolean.TRUE);
		if (listaIseeAlunno.size() > 0) {
			result = getIseeAttivo(listaIseeAlunno);
		}
		return result;
	}

	public Isee getIseeAttivo(List<Isee> listaIseeAlunno) {
		Isee result = null;
		if (listaIseeAlunno != null && listaIseeAlunno.size() > 0) {
			result = listaIseeAlunno.get(0);
			for (final Isee singoloIsee : listaIseeAlunno) {
				if (singoloIsee.getDataInizioValidita().isAfter(result.getDataInizioValidita())) {
					result = singoloIsee;
				}
			}
		}
		return result;
	}

	public Isee getIseeFromMonth(Integer meseRiferimento, String annoScolastico, List<Isee> listaIsee) {
		final Integer annoRiferimento = DateUtils.getAnnoRiferimentoByMese(meseRiferimento, annoScolastico);
		String meseRiferimentoString = meseRiferimento.toString();
		if (meseRiferimentoString.length() == 1) {
			meseRiferimentoString = "0" + meseRiferimentoString;
		}
		final String dataString = "01/" + meseRiferimentoString + "/" + annoRiferimento;

		LocalDate dataRata = DateUtils.stringToDate(dataString);
		int ultimoGiornoMese = dataRata.lengthOfMonth();
		dataRata = dataRata.withDayOfMonth(ultimoGiornoMese);
		Isee result = listaIsee.get(0);
		for (final Isee isee : listaIsee) {
			if (!dataRata.isBefore(isee.getDataInizioValidita()) && !dataRata.isAfter(isee.getDataFineValidita())) {
				result = isee;
				break;
			}
		}

		return result;
	}

	public DateFormat getSdf() {
		return sdf;
	}

}
