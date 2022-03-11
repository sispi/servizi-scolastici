package it.emilia_romagna.fonteraccoglitore.bl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.Signature;

import it.emilia_romagna.fonteraccoglitore.bl.docer.utils.FederaDataUtils;
import it.emilia_romagna.fonteraccoglitore.bl.docer.utils.FonteDocerUtils;
import it.emilia_romagna.fonteraccoglitore.bl.federa.utils.AssertionUtil;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic.IGetMetadatiEntitaInformativa;

public class GetMetadatiEntitaInformativa implements IGetMetadatiEntitaInformativa {

    private FonteDocerUtils fdu = null;
    private String cfRequest = null;
    private List<String> gruppiDelega = null;

    @Override
    public GetMetadatiEntitaInformativaResponse execute(GetMetadatiEntitaInformativaRequest request, String federaData) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {

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

	String xmlMetadata = fdu.getMetadatiEntitaInformativa(cfChiamante, request.getUidEntitaInformativa());

	GetMetadatiEntitaInformativaResponse response = new GetMetadatiEntitaInformativaResponse();
	response.setXmlMetadata(xmlMetadata);

	return response;

    }

    @Override
    public void preExecute(GetMetadatiEntitaInformativaRequest request, String middlewareData) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {

	fdu = new FonteDocerUtils();
	Assertion assertion = AssertionUtil.parseFederaAssertion(this.getClass().getSimpleName(), middlewareData, fdu);
	cfRequest = AssertionUtil.getCodiceFiscale(assertion);
	gruppiDelega = AssertionUtil.getDeleghe(assertion);

    }
}
