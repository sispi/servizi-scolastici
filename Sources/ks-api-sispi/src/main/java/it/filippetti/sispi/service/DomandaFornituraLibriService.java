package it.filippetti.sispi.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import it.filippetti.sispi.libriscolastici.DomandaFornituraBean;
import it.filippetti.sispi.model.DomandaFornituraLibri;
import it.filippetti.sispi.pagamentoretta.DateUtils;

@Service
@Validated
@Transactional
public class DomandaFornituraLibriService {

	private static final Logger logger = LoggerFactory.getLogger(DomandaFornituraLibriService.class);

	private static final String GIORNO_MESE_MAGGIORE_ETA = "14/09";
	private static final DateFormat dataNascitaFormatter = new SimpleDateFormat("dd/MM/yyyy");

	public Boolean isAlunnoMaggiorenne(DomandaFornituraBean datiAlunno) {
		Boolean result = Boolean.FALSE;
		final String dataNascita = datiAlunno.getData_nascita();
		if (dataNascita != null) {
			try {
				final Date dataNascitaDate = dataNascitaFormatter.parse(dataNascita);
				final Date dataMaggioreEta = dataNascitaFormatter.parse(getDataMaggioreEta());
				result = (dataMaggioreEta.compareTo(dataNascitaDate) >= 0);
				logger.debug(String.format("data nascita %s - maggiorenne %s", dataNascita, result));
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return result;
	}

	public DomandaFornituraLibri getDomandaAttiva(List<DomandaFornituraLibri> listaDomande) {
		DomandaFornituraLibri result = null;
		for (final DomandaFornituraLibri domandaFornituraLibri : listaDomande) {
			if ("N".equals(domandaFornituraLibri.getAnnullato())) {
				result = domandaFornituraLibri;
				break;
			}
		}
		return result;
	}

	private String getDataMaggioreEta() {
		Integer annoRiferimento = DateUtils.getAnnoRiferimento();
		annoRiferimento = annoRiferimento - 19;
		return GIORNO_MESE_MAGGIORE_ETA + "/" + annoRiferimento;
	}

	public static void main(String[] args) {
		DomandaFornituraLibriService domandaFornituraService = new DomandaFornituraLibriService();
		DomandaFornituraBean bean = new DomandaFornituraBean();
		String dataNascitaTest = "13/09/2002";
		bean.setData_nascita(dataNascitaTest);
		Boolean result = domandaFornituraService.isAlunnoMaggiorenne(bean);
		logger.info("result {}: {}", dataNascitaTest, result);

		dataNascitaTest = "14/09/2002";
		bean.setData_nascita(dataNascitaTest);
		result = domandaFornituraService.isAlunnoMaggiorenne(bean);
		logger.info("result {}: {}", dataNascitaTest, result);

		dataNascitaTest = "15/09/2002";
		bean.setData_nascita(dataNascitaTest);
		result = domandaFornituraService.isAlunnoMaggiorenne(bean);
		logger.info("result {}: {}", dataNascitaTest, result);
	}
}
