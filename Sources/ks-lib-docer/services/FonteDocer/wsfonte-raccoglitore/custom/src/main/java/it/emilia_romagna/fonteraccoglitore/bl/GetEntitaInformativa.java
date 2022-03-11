package it.emilia_romagna.fonteraccoglitore.bl;

import it.emilia_romagna.fonteraccoglitore.bl.docer.objects.FileDescriptor;
import it.emilia_romagna.fonteraccoglitore.bl.docer.utils.FederaDataUtils;
import it.emilia_romagna.fonteraccoglitore.bl.docer.utils.FonteDocerUtils;
import it.emilia_romagna.fonteraccoglitore.bl.federa.utils.AssertionUtil;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic.IGetEntitaInformativa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.XMLObject;

import org.apache.commons.lang.StringUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.Signature;

public class GetEntitaInformativa implements IGetEntitaInformativa {

    private FonteDocerUtils fdu = null;
    private String cfRequest = null;
    private List<String> gruppiDelega = null;

    @Override
    public GetEntitaInformativaResponse execute(GetEntitaInformativaRequest request, String federaData) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {

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

	// File zip =
	// fdu.getEntitaInformativa(request.getUidEntitaInformativa());

	FileDescriptor fd = fdu.getEntitaInformativa(cfChiamante, request.getUidEntitaInformativa());

	GetEntitaInformativaResponse response = new GetEntitaInformativaResponse();

	byte[] buffer = getFileBytes(fd.getIn());

	if (fdu.getDebug()) {
	    System.out.println(this.getClass().getSimpleName() + " response file length: " + buffer.length);
	}

	response.setEntitaInformativa(buffer);

	String mimetype = "application/octet-stream";

	response.setMimteType(mimetype);
	response.setTitle(fd.getFileName());

	return response;

    }

    private byte[] getFileBytes(InputStream ios) throws RerFonteError {
	ByteArrayOutputStream ous = null;

	try {
	    byte[] buffer = new byte[4096];
	    ous = new ByteArrayOutputStream();
	    int read = 0;
	    while ((read = ios.read(buffer)) != -1)
		ous.write(buffer, 0, read);
	}
	catch (IOException ioe) {
	    throw new RerFonteError(ioe);
	}
	finally {
	    try {
		if (ous != null)
		    ous.close();
	    }
	    catch (IOException e) {
		// swallow, since not that important
	    }
	    try {
		if (ios != null)
		    ios.close();
	    }
	    catch (IOException e) {
		// swallow, since not that important
	    }
	}
	return ous.toByteArray();
    }

    @Override
    public void preExecute(GetEntitaInformativaRequest request, String middlewareData) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError {

	fdu = new FonteDocerUtils();
	Assertion assertion = AssertionUtil.parseFederaAssertion(this.getClass().getSimpleName(), middlewareData, fdu);
	cfRequest = AssertionUtil.getCodiceFiscale(assertion);
	gruppiDelega = AssertionUtil.getDeleghe(assertion);
    }

}
