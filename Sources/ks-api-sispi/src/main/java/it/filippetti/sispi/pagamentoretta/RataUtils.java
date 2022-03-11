package it.filippetti.sispi.pagamentoretta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RataUtils {

	private RataUtils() {
	}

	public static List<DettaglioRataBean> getListaRateDefault() {
		final List<DettaglioRataBean> result = new ArrayList<DettaglioRataBean>();
		result.add(new DettaglioRataBean(1, "Rata Settembre", 50f, 9));
		result.add(new DettaglioRataBean(2, "Rata Ottobre", 0f, 10));
		result.add(new DettaglioRataBean(3, "Rata Novembre", 0f, 11));
		result.add(new DettaglioRataBean(4, "Rata Dicembre", 50f, 12));
		result.add(new DettaglioRataBean(5, "Rata Gennaio", 0f, 1));
		result.add(new DettaglioRataBean(6, "Rata Febbraio", 0f, 2));
		result.add(new DettaglioRataBean(7, "Rata Marzo", 0f, 3));
		result.add(new DettaglioRataBean(8, "Rata Aprile", 0f, 4));
		result.add(new DettaglioRataBean(9, "Rata Maggio", 0f, 5));
		result.add(new DettaglioRataBean(10, "Rata Giugno", 0f, 6));
		result.add(new DettaglioRataBean(11, "Rata Luglio", 0f, 7));
		return result;
	}

	public static String getDescizioneSoloMese(String descrizione) {
		String res = null;
		if (descrizione != null && descrizione.length() > 5) {
			res = descrizione.substring(5);
		}
		return res;
	}

	public static Boolean isIntervalloValido(LocalDate dataInizioIscrizione, LocalDate dataFineIscrizione,
			DettaglioRataBean dettaglioRataBean, String annoScolastico) {
		Boolean result = Boolean.TRUE;
		final LocalDate dataInizio = DateUtils.setDayOfMonth(dataInizioIscrizione, 1);
		final LocalDate dataFine = dataFineIscrizione;
		final LocalDate dataRata = DateUtils.getDateFromMonth(dettaglioRataBean.getMeseRiferimento(),
				DateUtils.getAnnoScolasticoAttuale());
		if (!dataRata.isBefore(dataInizio) && !dataRata.isAfter(dataFine)) {
			result = Boolean.TRUE;
		} else {
			result = Boolean.FALSE;
		}
		return result;
	}

	public static Float convertStringToFloat(String valore) {
		Float result = 0f;

		try {
			result = Float.parseFloat(valore);
		} catch (Exception e) {

		}
		return result;
	}

}
