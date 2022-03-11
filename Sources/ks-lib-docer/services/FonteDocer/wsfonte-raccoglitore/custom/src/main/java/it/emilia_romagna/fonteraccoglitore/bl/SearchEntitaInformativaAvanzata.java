package it.emilia_romagna.fonteraccoglitore.bl;

import it.emilia_romagna.fonteraccoglitore.bl.docer.utils.FonteDocerUtils;
import it.emilia_romagna.fonteraccoglitore.bl.federa.utils.AssertionUtil;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoBooleanoType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataRangeType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoNumeroType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoTestoType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic.ISearchEntitaInformativaAvanzata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.opensaml.saml2.core.Assertion;

public class SearchEntitaInformativaAvanzata implements ISearchEntitaInformativaAvanzata {

    private FonteDocerUtils fdu = null;
    private String cfRequest = null;
    private List<String> gruppiDelega = null;
    
    @Override
    public SearchEntitaInformativaAvanzataResponse execute(SearchEntitaInformativaAvanzataRequest request, String federaData) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {

	// String cfRequest = FederaDataUtils.getCodiceFiscale(federaData);

	if (fdu.getDebug()) {
	    System.out.println(this.getClass().getSimpleName() + ": cfRequest: " + cfRequest);
	}

	String cfChiamante = cfRequest;

	if (!StringUtils.isEmpty(fdu.getTestUser())) {
	    cfChiamante = fdu.getTestUser();
	}

	if (StringUtils.isEmpty(cfChiamante)) {
	    throw new AuthorizationDeniedError();
	}

	String gidTitolare = FonteDocerUtils.generateGroupIdFromDelega("titolare", "codiceFiscale", cfChiamante);
	if(!gruppiDelega.contains(gidTitolare)){
	    gruppiDelega.add(gidTitolare);    
	}
	
//	if (request.getDeleghe() != null) {
//	    for (GetEntitaInformativaRequestDelegheDelega delega : request.getDeleghe()) {
//		String gid = FonteDocerUtils.generateGroupIdFromDelega(delega.getTipoRelazione(), delega.getTipoId(), delega.getId());
//		if (gruppiDelega.contains(gid)) {
//		    continue;
//		}
//		gruppiDelega.add(gid);
//	    }
//	}

	if (fdu.getDebug()) {
	    System.out.println(this.getClass().getSimpleName() + ": login: cfChiamante=" + cfChiamante + ", gruppiDelega=" + gruppiDelega.toString());
	}

	fdu.loginWSFonte(cfChiamante, gruppiDelega);

	String tipo_oggetto = "";

	// if (request.getAttributiTesto() != null) {
	// for (AttributoTestoType type : request.getAttributiTesto()) {
	// // System.out.println(type.getCodice()+" = "+type.getValore());
	// if (type.getCodice().equalsIgnoreCase("tipo_oggetto")) {
	// tipo_oggetto = type.getValore();
	// }
	// }
	// }

	Map<String, String> searchCriteria = new HashMap<String, String>();

	if (request.getAttributiBoolean() != null) {
	    for (AttributoBooleanoType type : request.getAttributiBoolean()) {
		// System.out.println(type.getCodice()+" = "+type.isValore());
		searchCriteria.put(type.getCodice(), String.valueOf(type.isValore()));
	    }
	}

	if (request.getAttributiData() != null) {
	    for (AttributoDataType type : request.getAttributiData()) {
		// System.out.println(type.getCodice()+" = "+type.getValore());
		String date = fdu.toDocerDatetime(type.getValore());
		if (date != null) {
		    searchCriteria.put(type.getCodice(), date);
		}

	    }
	}

	if (request.getAttributiDataRange() != null) {
	    for (AttributoDataRangeType type : request.getAttributiDataRange()) {
		// System.out.println(type.getCodice()+" = "+type.getValore().getMinDate()+" - "+type.getValore().getMaxDate());
		String minDate = fdu.toDocerDatetime(type.getValore().getMinDate());
		String maxDate = fdu.toDocerDatetime(type.getValore().getMaxDate());

		String min = "MIN";
		String max = "MAX";

		if (StringUtils.isNotEmpty(minDate)) {
		    min = minDate;
		}

		if (StringUtils.isNotEmpty(maxDate)) {
		    max = maxDate;
		}

		searchCriteria.put(type.getCodice(), "[" + min + " TO " + max + "]");

	    }
	}

	if (request.getAttributiNumero() != null) {
	    for (AttributoNumeroType type : request.getAttributiNumero()) {
		// System.out.println(type.getCodice()+" = "+type.getValore());
		searchCriteria.put(type.getCodice(), Float.toString(type.getValore()));
	    }
	}
	if (request.getAttributiTesto() != null) {
	    for (AttributoTestoType type : request.getAttributiTesto()) {
		// System.out.println(type.getCodice()+" = "+type.getValore());
		if (type.getCodice().equalsIgnoreCase("tipo_oggetto")) {
		    tipo_oggetto = type.getValore();
		    continue;
		}
		searchCriteria.put(type.getCodice(), type.getValore());
	    }
	}

	if (fdu.getDebug()) {
	    System.out.println(this.getClass().getSimpleName() + ": search: cfChiamante: " + cfChiamante + ", CodiceFonte: " + request.getCodiceFonte() + ", tipo_oggetto: " + tipo_oggetto
		    + ", searchCriteria: " + searchCriteria.toString());
	}

	SearchEntitaInformativaAvanzataResponse searchEntitaInformativaAvanzataResponse = fdu.searchEntitaInformativaAvanzata(cfChiamante, request.getCodiceFonte(), tipo_oggetto, searchCriteria);

	if (fdu.getDebug()) {
	    System.out.println(this.getClass().getSimpleName() + ", trovati: " + searchEntitaInformativaAvanzataResponse.getNumFound() + " risultati");
	}

	return searchEntitaInformativaAvanzataResponse;

    }

    @Override
    public void preExecute(SearchEntitaInformativaAvanzataRequest request, String middlewareData) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {
	fdu = new FonteDocerUtils();
	Assertion assertion = AssertionUtil.parseFederaAssertion(this.getClass().getSimpleName(), middlewareData, fdu);
	cfRequest = AssertionUtil.getCodiceFiscale(assertion);
	gruppiDelega = AssertionUtil.getDeleghe(assertion);
    }

}
