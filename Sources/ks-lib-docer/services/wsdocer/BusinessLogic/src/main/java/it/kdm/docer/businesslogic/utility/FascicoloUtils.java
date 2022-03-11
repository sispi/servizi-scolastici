package it.kdm.docer.businesslogic.utility;

import it.kdm.docer.sdk.Constants;
import it.kdm.docer.sdk.classes.KeyValuePair;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class FascicoloUtils {

	private String fascicoliSecFormat = "classifica/anno_fascicolo/progr_fascicolo";
	private String regexpFascicoliSec = "^([^/]+)/([12][0-9][0-9][0-9])/(.+)$";
	private Pattern patternFascicoliSec = null;

	private int classifica_position = 1;
	private int anno_fascicolo_position = 2;
	private int progr_fascicolo_position = 3;

	public FascicoloUtils(String fascicoliSecondariFormat, String regexpFascicoliSecondari) {
		if (StringUtils.isNotEmpty(fascicoliSecondariFormat)) {
			fascicoliSecFormat = fascicoliSecondariFormat;
		}

		if (StringUtils.isNotEmpty(regexpFascicoliSecondari)) {
			regexpFascicoliSec = regexpFascicoliSecondari;
		}

		patternFascicoliSec = Pattern.compile(regexpFascicoliSec);
	}

	public FascicoloUtils(String fascicoliSecondariFormat, String regexpFascicoliSecondari, int classificaPositionOnRegex, int annoFascicoloPositionOnRegex, int progrFascicoloPositionOnRegex) {
		if (StringUtils.isNotEmpty(fascicoliSecondariFormat)) {
			fascicoliSecFormat = fascicoliSecondariFormat;
		}

		if (StringUtils.isNotEmpty(regexpFascicoliSecondari)) {
			regexpFascicoliSec = regexpFascicoliSecondari;
		}

		patternFascicoliSec = Pattern.compile(regexpFascicoliSec);

		classifica_position = classificaPositionOnRegex;
		anno_fascicolo_position = annoFascicoloPositionOnRegex;
		progr_fascicolo_position = progrFascicoloPositionOnRegex;
	}

	public String[] parseFascicoloString(String fascicoloString) {

		String[] arr = new String[3];

		Matcher m = patternFascicoliSec.matcher(fascicoloString);

		String parsedClassifica = "";
		String parsedAnnoFascicolo = "";
		String parsedProgrFascicolo = "";

		if (m.matches()) {
			parsedClassifica = m.group(classifica_position);
			parsedAnnoFascicolo = m.group(anno_fascicolo_position);
			parsedProgrFascicolo = m.group(progr_fascicolo_position);
		}

		arr[classifica_position - 1] = parsedClassifica;
		arr[anno_fascicolo_position - 1] = parsedAnnoFascicolo;
		arr[progr_fascicolo_position - 1] = parsedProgrFascicolo;

		return arr;
	}

	public String toFascicoloSecondarioString(String classifica, String anno_fascicolo, String progr_fascicolo) {
		String appo_fasc_secondari_format = fascicoliSecFormat;

		if(StringUtils.isEmpty(classifica) || StringUtils.isEmpty(anno_fascicolo) || StringUtils.isEmpty(progr_fascicolo)){
			return "";
		}
		
		appo_fasc_secondari_format = appo_fasc_secondari_format.replace("classifica", classifica);
		appo_fasc_secondari_format = appo_fasc_secondari_format.replace("anno_fascicolo", anno_fascicolo);
		appo_fasc_secondari_format = appo_fasc_secondari_format.replace("progr_fascicolo", progr_fascicolo);
		return appo_fasc_secondari_format;
	}	

	public String toReadableString(KeyValuePair[] params) {

		String cod_ente = null;
		String cod_aoo = null;
		String classifica = null;
		String anno_fascicolo = null;
		String progr_fascicolo = null;
		String fasc_secondari = null;

		for (KeyValuePair pair : params) {
			//			if (pair.getKey().equalsIgnoreCase("COD_ENTE")){
			//				cod_ente = pair.getValue();
			//				continue;
			//			}				
			//			if (pair.getKey().equalsIgnoreCase("COD_AOO")){
			//				cod_aoo = pair.getValue();
			//				continue;
			//			}
			if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
				classifica = pair.getValue();
				continue;
			}
			if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
				anno_fascicolo = pair.getValue();
				continue;
			}
			if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
				progr_fascicolo = pair.getValue();
				continue;
			}
			//			if (pair.getKey().equalsIgnoreCase("FASC_SECONDARI")){
			//				fasc_secondari = pair.getValue();
			//				continue;
			//			}
		}
		String out = "classifica";
		out = out.replace("classifica", String.valueOf(classifica));

		if (StringUtils.isNotEmpty(progr_fascicolo)) {

			out = fascicoliSecFormat;
			out = out.replace("classifica", String.valueOf(classifica));
			out = out.replace("anno_fascicolo", String.valueOf(anno_fascicolo));
			out = out.replace("progr_fascicolo", String.valueOf(progr_fascicolo));
		}

		return out;
	}

	public KeyValuePair[] toDocerKeyValuePairArray(String cod_ente, String cod_aoo, String fascicoloString) {

		String[] fArr = parseFascicoloString(fascicoloString);

		KeyValuePair[] fascicolo = new KeyValuePair[5];

		KeyValuePair kvpCodEnte = new KeyValuePair();
		kvpCodEnte.setKey("COD_ENTE");
		kvpCodEnte.setValue(cod_ente);
		fascicolo[0] = kvpCodEnte;

		KeyValuePair kvpCodAOO = new KeyValuePair();
		kvpCodAOO.setKey("COD_AOO");
		kvpCodAOO.setValue(cod_aoo);
		fascicolo[1] = kvpCodAOO;

		KeyValuePair kvpClassifica = new KeyValuePair();
		kvpClassifica.setKey("CLASSIFICA");
		kvpClassifica.setValue(fArr[classifica_position - 1]);
		fascicolo[2] = kvpClassifica;

		KeyValuePair kvpProgrFascicolo = new KeyValuePair();
		kvpProgrFascicolo.setKey("PROGR_FASCICOLO");
		kvpProgrFascicolo.setValue(fArr[progr_fascicolo_position - 1]);
		fascicolo[3] = kvpProgrFascicolo;

		KeyValuePair kvpAnnoFascicolo = new KeyValuePair();
		kvpAnnoFascicolo.setKey("ANNO_FASCICOLO");
		kvpAnnoFascicolo.setValue(fArr[anno_fascicolo_position - 1]);
		fascicolo[4] = kvpAnnoFascicolo;

		return fascicolo;
	}
	
	public Map<String, String> toMap(String cod_ente, String cod_aoo, String fascicoloString) {

		String[] fArr = parseFascicoloString(fascicoloString);

		Map<String, String> map = new HashMap<String, String>();

		map.put(Constants.fascicolo_cod_ente, cod_ente);
		map.put(Constants.fascicolo_cod_aoo, cod_aoo);
		map.put(Constants.fascicolo_classifica, fArr[classifica_position - 1]);
		map.put(Constants.fascicolo_anno_fascicolo, fArr[anno_fascicolo_position - 1]);
		map.put(Constants.fascicolo_progr_fascicolo, fArr[progr_fascicolo_position - 1]);

		return map;
	}

}

