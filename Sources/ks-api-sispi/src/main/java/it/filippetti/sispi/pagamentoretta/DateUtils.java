package it.filippetti.sispi.pagamentoretta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DateUtils {

	public static final List<Integer> MESI_ANNO_SUCCESSIVO = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
//	private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public static LocalDate stringToDate(String dataString) {
		return stringToDate(dataString, "dd/MM/yyyy");
	}

	public static LocalDate stringToDate(String dataString, String dateFormat) {
		if (dataString != null && dataString.length() == 10) {
			try {
				return LocalDate.parse(dataString, DateTimeFormatter.ofPattern(dateFormat));
//				result = sdf.parse(dataString);
			} catch (Exception e) {

			}
		}
		return null;
	}

	public static LocalDate setDayOfMonth(LocalDate localDate, Integer dayOfMonth) {
		LocalDate result = localDate;
		if (localDate != null) {
			result = localDate.withDayOfMonth(dayOfMonth);
		}
		return result;
	}

	public static LocalDate getDateFromMonth(Integer meseRiferimento, String annoScolastico) {
		final Integer annoRiferimento = getAnnoRiferimentoByMese(meseRiferimento, annoScolastico);
		String meseRiferimentoString = meseRiferimento.toString();
		if (meseRiferimentoString.length() == 1) {
			meseRiferimentoString = "0" + meseRiferimentoString;
		}
		final String dataString = "01/" + meseRiferimentoString + "/" + annoRiferimento;
		return stringToDate(dataString);
	}

	public static Integer getAnnoRiferimento() {
		Integer annoAttuale = Calendar.getInstance().get(Calendar.YEAR);
		final Integer meseAttuale = Calendar.getInstance().get(Calendar.MONTH);
		if (MESI_ANNO_SUCCESSIVO.contains(meseAttuale)) {
			// Per i mesi dell'anno successivo devo sottrarre un anno (Gennaio 2021 fa parte
			// dell'anno scolastico 2020/2021)
			annoAttuale = annoAttuale - 1;
		}
		return annoAttuale;
	}

	public static String getAnnoScolasticoAttuale() {
		return getAnnoScolasticoByAnno(getAnnoRiferimento());
	}

	public static Integer getAnnoRiferimentoByMese(Integer meseRiferimento, String annoScolastico) {
		final String anno1 = annoScolastico.substring(0, 4);
		final Integer anno1Int = Integer.parseInt(anno1);
		final String anno2 = annoScolastico.substring(5);
		final Integer anno2Int = Integer.parseInt(anno2);
		Integer result = anno1Int;
		if (MESI_ANNO_SUCCESSIVO.contains(meseRiferimento)) {
			result = anno2Int;
		}
		return result;
	}

	public static Integer getAnnoRiferimentoByAnnoScolastico(String annoScolastico) {
		final String anno1 = annoScolastico.substring(0, 4);
		return Integer.parseInt(anno1);
	}

	public static String getAnnoScolasticoByAnno(Integer annoAttuale) {
		return annoAttuale + "/" + (annoAttuale + 1);
	}

}
