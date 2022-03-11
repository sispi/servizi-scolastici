package it.emilia_romagna.fonteraccoglitore.bl;

import java.util.List;

import it.emilia_romagna.fonteraccoglitore.bl.docer.utils.FonteDocerUtils;
import it.emilia_romagna.fonteraccoglitore.bl.federa.utils.AssertionUtil;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic.IGetAttributiRicerca;

import org.apache.commons.lang.StringUtils;
import org.opensaml.saml2.core.Assertion;

public class GetAttributiRicerca implements IGetAttributiRicerca {

    private FonteDocerUtils fdu = null;
    private String cfRequest = null;
    private List<String> gruppiDelega = null;

    @Override
    public GetAttributiRicercaResponse execute(GetAttributiRicercaRequest request, String federaData) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {

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

	GetAttributiRicercaResponse response = new GetAttributiRicercaResponse();

	String fonteId = request.getCodiceFonte();
	response.setAttributi(fdu.getAttributiRicerca(fonteId).toArray(new AttributoType[0]));
	response.setEsito(true);

	return response;
    }

    @Override
    public void preExecute(GetAttributiRicercaRequest request, String middlewareData) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {
	
	fdu = new FonteDocerUtils();
	Assertion assertion = AssertionUtil.parseFederaAssertion(this.getClass().getSimpleName(), middlewareData, fdu);
	cfRequest = AssertionUtil.getCodiceFiscale(assertion);
	gruppiDelega = AssertionUtil.getDeleghe(assertion);
    }

}
