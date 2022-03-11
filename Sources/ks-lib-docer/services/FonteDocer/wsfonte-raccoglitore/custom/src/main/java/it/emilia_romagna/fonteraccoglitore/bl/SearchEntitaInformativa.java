package it.emilia_romagna.fonteraccoglitore.bl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.Signature;

import it.emilia_romagna.fonteraccoglitore.bl.docer.utils.FederaDataUtils;
import it.emilia_romagna.fonteraccoglitore.bl.docer.utils.FonteDocerUtils;
import it.emilia_romagna.fonteraccoglitore.bl.federa.utils.AssertionUtil;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DateRangeType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic.ISearchEntitaInformativa;

public class SearchEntitaInformativa implements ISearchEntitaInformativa {

    private FonteDocerUtils fdu = null;
    private String cfRequest = null;
    private List<String> gruppiDelega = null;

    @Override
    public SearchEntitaInformativaResponse execute(SearchEntitaInformativaRequest request, String federaData) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {

	FonteDocerUtils fdu = new FonteDocerUtils();

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

	DateRangeType dataRange = request.getDateRange();
	String numeroRegistrazione = request.getNumeroRegistrazione();
	String testo = request.getTesto();

	String minDate = null;
	String maxDate = null;

	if (dataRange != null) {
	    minDate = fdu.toDocerDatetime(dataRange.getMinDate());
	    maxDate = fdu.toDocerDatetime(dataRange.getMaxDate());
	}

	if (fdu.getDebug()) {
	    System.out.println(this.getClass().getSimpleName() + ": search: cfChiamante: " + cfChiamante + ", CodiceFonte: " + request.getCodiceFonte() + ", minDate: " + minDate + ", maxDate: "
		    + maxDate + ", testo: " + testo + ", numeroRegistrazione: " + numeroRegistrazione);
	}

	SearchEntitaInformativaResponse searchEntitaInformativaResponse = fdu.searchEntitaInformativa(cfChiamante, request.getCodiceFonte(), minDate, maxDate, testo, numeroRegistrazione);

	if (fdu.getDebug()) {
	    System.out.println(this.getClass().getSimpleName() + ", trovati: " + searchEntitaInformativaResponse.getNumFound() + " risultati");
	}

	return searchEntitaInformativaResponse;

    }

    @Override
    public void preExecute(SearchEntitaInformativaRequest request, String middlewareData) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {

	fdu = new FonteDocerUtils();
	Assertion assertion = AssertionUtil.parseFederaAssertion(this.getClass().getSimpleName(), middlewareData, fdu);
	cfRequest = AssertionUtil.getCodiceFiscale(assertion);
	gruppiDelega = AssertionUtil.getDeleghe(assertion);

    }
}
