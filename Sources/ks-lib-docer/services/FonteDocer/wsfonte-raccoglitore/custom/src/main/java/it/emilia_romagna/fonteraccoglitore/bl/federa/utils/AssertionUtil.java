package it.emilia_romagna.fonteraccoglitore.bl.federa.utils;

import it.emilia_romagna.fonteraccoglitore.bl.docer.utils.FederaDataUtils;
import it.emilia_romagna.fonteraccoglitore.bl.docer.utils.FonteDocerUtils;
import it.emilia_romagna.fonteraccoglitore.util.FonteMappingWithJson;
import it.emilia_romagna.fonteraccoglitore.util.SecurityCheck;
import it.emilia_romagna.regione.samlutil.samlwriter.SamlWriter;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.XMLParserException;
import org.w3c.dom.Element;

public class AssertionUtil {

    public static Assertion parseFederaAssertion(String methodName, String middlewareData, FonteDocerUtils fdu) throws AuthorizationDeniedError, RerFonteError {

	SecurityCheck.defaultChecks(middlewareData);
	Collection<String> fontiAmmesse = SecurityCheck.getFontiAmmesse(FonteMappingWithJson.class, middlewareData);

	if (fontiAmmesse.isEmpty()) {
	    throw new AuthorizationDeniedError("Fonte non utilizzabile: le fonti ammesse specificate nell'asserzione." + "non comprendono l'ID configurato per questa fonte", new Exception());
	}

	 try {
	    return SamlWriter.parseAssertion(middlewareData);
	}
	catch (UnmarshallingException e) {
	    if (fdu.getDebug()) {
		 fdu.saveStringToFile(middlewareData, methodName +
			 "_ParseAssertionFail_middlewareData.txt");
	     }
	    throw new RerFonteError(e);
	}
	catch (XMLParserException e) {
	    if (fdu.getDebug()) {
		 fdu.saveStringToFile(middlewareData, methodName +
			 "_ParseAssertionFail_middlewareData.txt");
	     }
	     throw new RerFonteError(e);
	}
	 
	// throw new AuthorizationDeniedError(new String[] {
	// "Errore formato Assert" });
	// }
	// sig = assertion.getSignature();
	// try {
	// ts =
	// SamlUtil.loadKeystore(Config.getParam(Config.Constants.TRUSTSTORE_PATH),
	// Config.getParam(Config.Constants.TRUSTSTORE_PASS));
	// }
	// catch (KeyStoreException e) {
	// if (fdu.getDebug()) {
	// fdu.saveStringToFile(middlewareData, methodName +
	// "_KeyStoreExceptionFail_middlewareData.txt");
	// }
	// throw new AuthorizationDeniedError(new String[] {
	// "Errore certificato: KeyStoreException: " + e.getMessage() });
	// }
	// catch (NoSuchAlgorithmException e) {
	// if (fdu.getDebug()) {
	// fdu.saveStringToFile(middlewareData, methodName +
	// "_NoSuchAlgorithmExceptionFail_middlewareData.txt");
	// }
	// throw new AuthorizationDeniedError(new String[] {
	// "Errore certificato: NoSuchAlgorithmException: " + e.getMessage() });
	// }
	// catch (CertificateException e) {
	// if (fdu.getDebug()) {
	// fdu.saveStringToFile(middlewareData, methodName +
	// "_CertificateExceptionFail_middlewareData.txt");
	// }
	// throw new AuthorizationDeniedError(new String[] {
	// "Errore certificato: CertificateException: " + e.getMessage() });
	// }
	// catch (IOException e) {
	// if (fdu.getDebug()) {
	// fdu.saveStringToFile(middlewareData, methodName +
	// "_IOExceptionFail_middlewareData.txt");
	// }
	// throw new AuthorizationDeniedError(new String[] {
	// "Errore certificato: IOException: " + e.getMessage() });
	// }
	// Credential c = SamlUtil.getCredential(ts,
	// Config.getParam(Config.Constants.TRUSTSTORE_ALIAS),
	// Config.getParam(Config.Constants.TRUSTSTORE_PASS));
	// if (!SamlValidator.validateSignature(sig, c)) {
	// if (fdu.getDebug()) {
	// fdu.saveStringToFile(middlewareData, methodName +
	// "_ValidateSignatureFail_middlewareData.txt");
	// }
	// throw new AuthorizationDeniedError(new String[] {
	// "Firma MW non valida" });
	// }
	// if (!SamlUtil.isValidDate(assertion)) {
	// if (fdu.getDebug()) {
	// fdu.saveStringToFile(middlewareData, methodName +
	// "_IsValidDateFail_middlewareData.txt");
	// }
	// throw new AuthorizationDeniedError(new String[] {
	// "Assert scaduta non valida" });
	// }

    }

    // public static Assertion parseFederaAssertion(String methodName, String
    // middlewareData, FonteDocerUtils fdu) throws RerFonteError,
    // ObjectNotFoundError, AuthorizationDeniedError {
    //
    // if (middlewareData == null) {
    // if (fdu.getDebug()) {
    // fdu.saveStringToFile("null", methodName +
    // "_AssertionFail_middlewareData.txt");
    // }
    // throw new AuthorizationDeniedError(new String[] {
    // "Autorizzazione non fornita" });
    // }
    // Assertion assertion = null;
    // KeyStore ts = null;
    // Signature sig = null;
    // try {
    // assertion = SamlWriter.parseAssertion(middlewareData);
    // }
    // catch (Exception e) {
    // if (fdu.getDebug()) {
    // fdu.saveStringToFile(middlewareData, methodName +
    // "_ParseAssertionFail_middlewareData.txt");
    // }
    // throw new AuthorizationDeniedError(new String[] { "Errore formato Assert"
    // });
    // }
    // sig = assertion.getSignature();
    // try {
    // ts =
    // SamlUtil.loadKeystore(Config.getParam(Config.Constants.TRUSTSTORE_PATH),
    // Config.getParam(Config.Constants.TRUSTSTORE_PASS));
    // }
    // catch (KeyStoreException e) {
    // if (fdu.getDebug()) {
    // fdu.saveStringToFile(middlewareData, methodName +
    // "_KeyStoreExceptionFail_middlewareData.txt");
    // }
    // throw new AuthorizationDeniedError(new String[] {
    // "Errore certificato: KeyStoreException: " + e.getMessage() });
    // }
    // catch (NoSuchAlgorithmException e) {
    // if (fdu.getDebug()) {
    // fdu.saveStringToFile(middlewareData, methodName +
    // "_NoSuchAlgorithmExceptionFail_middlewareData.txt");
    // }
    // throw new AuthorizationDeniedError(new String[] {
    // "Errore certificato: NoSuchAlgorithmException: " + e.getMessage() });
    // }
    // catch (CertificateException e) {
    // if (fdu.getDebug()) {
    // fdu.saveStringToFile(middlewareData, methodName +
    // "_CertificateExceptionFail_middlewareData.txt");
    // }
    // throw new AuthorizationDeniedError(new String[] {
    // "Errore certificato: CertificateException: " + e.getMessage() });
    // }
    // catch (IOException e) {
    // if (fdu.getDebug()) {
    // fdu.saveStringToFile(middlewareData, methodName +
    // "_IOExceptionFail_middlewareData.txt");
    // }
    // throw new AuthorizationDeniedError(new String[] {
    // "Errore certificato: IOException: " + e.getMessage() });
    // }
    // Credential c = SamlUtil.getCredential(ts,
    // Config.getParam(Config.Constants.TRUSTSTORE_ALIAS),
    // Config.getParam(Config.Constants.TRUSTSTORE_PASS));
    // if (!SamlValidator.validateSignature(sig, c)) {
    // if (fdu.getDebug()) {
    // fdu.saveStringToFile(middlewareData, methodName +
    // "_ValidateSignatureFail_middlewareData.txt");
    // }
    // throw new AuthorizationDeniedError(new String[] { "Firma MW non valida"
    // });
    // }
    // if (!SamlUtil.isValidDate(assertion)) {
    // if (fdu.getDebug()) {
    // fdu.saveStringToFile(middlewareData, methodName +
    // "_IsValidDateFail_middlewareData.txt");
    // }
    // throw new AuthorizationDeniedError(new String[] {
    // "Assert scaduta non valida" });
    // }
    //
    // return assertion;
    //
    // }

    public static String getCodiceFiscale(Assertion assertion) {

	String cfChiamante = null;

	List<AttributeStatement> attributeStatementList = assertion.getAttributeStatements();

	for (AttributeStatement attributeStatement : attributeStatementList) {

	    List<Attribute> attributeList = attributeStatement.getAttributes();

	    for (Attribute attribute : attributeList) {

		if (attribute.getName().equalsIgnoreCase("SecurityData")) {
		    Element elem = attribute.getDOM();
		    cfChiamante = FederaDataUtils.getCodiceFiscale(elem.getTextContent());
		    return cfChiamante;
		}
	    }

	}

	return cfChiamante;
    }

    public static List<String> getDeleghe(Assertion assertion) {

	List<String> gruppiDelega = new ArrayList<String>();

	List<AttributeStatement> attributeStatementList = assertion.getAttributeStatements();

	for (AttributeStatement attributeStatement : attributeStatementList) {

	    List<Attribute> attributeList = attributeStatement.getAttributes();

	    for (Attribute attribute : attributeList) {

		if (attribute.getName().equalsIgnoreCase("DELEGHE")) {

		    Element elem = attribute.getDOM();

		    gruppiDelega.addAll(FederaDataUtils.getDeleghe(elem.getTextContent()));

		    return gruppiDelega;
		}

	    }

	}

	return gruppiDelega;
    }

    // public static List<String> getDelegheTest(String cfChiamante, OMElement
    // elem) throws RerFonteError, ObjectNotFoundError, AuthorizationDeniedError
    // {
    //
    // List<String> gruppiDelega = new ArrayList<String>();
    //
    // String gidTitolare =
    // FonteDocerUtils.generateGroupIdFromDelega("titolare", "codiceFiscale",
    // cfChiamante);
    // gruppiDelega.add(gidTitolare);
    //
    // AXIOMXPath pathCittadino = null;
    // AXIOMXPath pathOperatorePA = null;
    // try {
    // pathCittadino = new
    // AXIOMXPath("//ruoli/ruolo[@name='CITTADINO']/deleghe/delega");
    // pathOperatorePA = new
    // AXIOMXPath("//ruoli/ruolo[@name='OPERATORE_PA']/deleghe/delega");
    // }
    // catch (JaxenException e) {
    // System.out.println(e.getMessage());
    // return gruppiDelega;
    // }
    //
    // List<OMElement> delegheElements = new ArrayList<OMElement>();
    //
    // try {
    // List<OMElement> delegheCittadino = pathCittadino.selectNodes(elem);
    //
    // if (delegheCittadino != null && delegheCittadino.size() > 0) {
    // delegheElements.addAll(delegheCittadino);
    // }
    //
    // }
    // catch (JaxenException e) {
    // System.out.println(e.getMessage());
    // }
    //
    // try {
    // List<OMElement> delegheOperatorePA = pathOperatorePA.selectNodes(elem);
    //
    // if (delegheOperatorePA != null && delegheOperatorePA.size() > 0) {
    // delegheElements.addAll(delegheOperatorePA);
    // }
    //
    // }
    // catch (JaxenException e) {
    // System.out.println(e.getMessage());
    // }
    //
    // if (delegheElements != null) {
    //
    // for (OMElement omelement : delegheElements) {
    // OMAttribute attTipoRelazione =
    // omelement.getAttribute(FederaDataUtils.tipoRelazione_QName);
    // OMAttribute attTipoId =
    // omelement.getAttribute(FederaDataUtils.tipoId_QName);
    // OMAttribute attId = omelement.getAttribute(FederaDataUtils.id_QName);
    //
    // if (attTipoRelazione == null || attTipoId == null || attTipoId == null) {
    // continue;
    // }
    //
    // String gid =
    // FonteDocerUtils.generateGroupIdFromDelega(attTipoRelazione.getAttributeValue(),
    // attTipoId.getAttributeValue(), attId.getAttributeValue());
    //
    // if (gruppiDelega.contains(gid)) {
    // continue;
    // }
    // gruppiDelega.add(gid);
    // }
    // }
    //
    // return gruppiDelega;
    // }

}