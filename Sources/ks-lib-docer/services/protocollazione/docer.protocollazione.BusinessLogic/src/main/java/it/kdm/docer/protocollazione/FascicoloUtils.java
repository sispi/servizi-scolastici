package it.kdm.docer.protocollazione;


import it.kdm.docer.clients.DocerServicesStub;

import java.util.ArrayList;
import java.util.List;
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

		appo_fasc_secondari_format = appo_fasc_secondari_format.replace("classifica", classifica);
		appo_fasc_secondari_format = appo_fasc_secondari_format.replace("anno_fascicolo", anno_fascicolo);
		appo_fasc_secondari_format = appo_fasc_secondari_format.replace("progr_fascicolo", progr_fascicolo);
		return appo_fasc_secondari_format;
	}

	public String toFascicoloSecondarioString(DocerServicesStub.KeyValuePair[] params) {

		String cod_ente = null;
		String cod_aoo = null;
		String classifica = null;
		String anno_fascicolo = null;
		String progr_fascicolo = null;
		String fasc_secondari = null;

		for (DocerServicesStub.KeyValuePair pair : params) {
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

		String appo_fasc_secondari_format = fascicoliSecFormat;

		appo_fasc_secondari_format = appo_fasc_secondari_format.replace("classifica", String.valueOf(classifica));
		appo_fasc_secondari_format = appo_fasc_secondari_format.replace("anno_fascicolo", String.valueOf(anno_fascicolo));
		appo_fasc_secondari_format = appo_fasc_secondari_format.replace("progr_fascicolo", String.valueOf(progr_fascicolo));
		return appo_fasc_secondari_format;
	}

	public String toReadableString(DocerServicesStub.KeyValuePair[] params) {

		String cod_ente = null;
		String cod_aoo = null;
		String classifica = null;
		String anno_fascicolo = null;
		String progr_fascicolo = null;
		String fasc_secondari = null;

		for (DocerServicesStub.KeyValuePair pair : params) {
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

	public DocerServicesStub.KeyValuePair[] toDocerKeyValuePairArray(String cod_ente, String cod_aoo, String fascicoloString) {

		String[] fArr = parseFascicoloString(fascicoloString);

		DocerServicesStub.KeyValuePair[] fascicolo = new DocerServicesStub.KeyValuePair[5];

		DocerServicesStub.KeyValuePair kvpCodEnte = new DocerServicesStub.KeyValuePair();
		kvpCodEnte.setKey("COD_ENTE");
		kvpCodEnte.setValue(cod_ente);
		fascicolo[0] = kvpCodEnte;

		DocerServicesStub.KeyValuePair kvpCodAOO = new DocerServicesStub.KeyValuePair();
		kvpCodAOO.setKey("COD_AOO");
		kvpCodAOO.setValue(cod_aoo);
		fascicolo[1] = kvpCodAOO;

		DocerServicesStub.KeyValuePair kvpClassifica = new DocerServicesStub.KeyValuePair();
		kvpClassifica.setKey("CLASSIFICA");
		kvpClassifica.setValue(fArr[classifica_position - 1]);
		fascicolo[2] = kvpClassifica;

		DocerServicesStub.KeyValuePair kvpProgrFascicolo = new DocerServicesStub.KeyValuePair();
		kvpProgrFascicolo.setKey("PROGR_FASCICOLO");
		kvpProgrFascicolo.setValue(fArr[progr_fascicolo_position - 1]);
		fascicolo[3] = kvpProgrFascicolo;

		DocerServicesStub.KeyValuePair kvpAnnoFascicolo = new DocerServicesStub.KeyValuePair();
		kvpAnnoFascicolo.setKey("ANNO_FASCICOLO");
		kvpAnnoFascicolo.setValue(fArr[anno_fascicolo_position - 1]);
		fascicolo[4] = kvpAnnoFascicolo;

		return fascicolo;
	}

	public DocerServicesStub.KeyValuePair[] toDocerKeyValuePairArray(it.kdm.docer.sdk.classes.KeyValuePair[] kvpArray) {

		List<DocerServicesStub.KeyValuePair> list = new ArrayList<DocerServicesStub.KeyValuePair>();

		for (it.kdm.docer.sdk.classes.KeyValuePair kvp : kvpArray) {

			DocerServicesStub.KeyValuePair p = new DocerServicesStub.KeyValuePair();
			p.setKey(kvp.getKey());
			p.setValue(kvp.getValue());
			list.add(p);
		}

		return list.toArray(new DocerServicesStub.KeyValuePair[0]);

	}

}

