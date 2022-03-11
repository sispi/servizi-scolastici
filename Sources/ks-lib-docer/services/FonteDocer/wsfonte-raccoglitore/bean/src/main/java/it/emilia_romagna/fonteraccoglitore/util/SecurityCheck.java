package it.emilia_romagna.fonteraccoglitore.util;

import it.emilia_romagna.regione.samlutil.SamlUtil;
import it.emilia_romagna.regione.samlutil.samlvalidator.SamlValidator;
import it.emilia_romagna.regione.samlutil.samlwriter.SamlWriter;
import it.emilia_romagna.regione.security_rsp.conf.Config;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.Signature;

public class SecurityCheck {

	private static Logger LOG = org.slf4j.LoggerFactory.getLogger(SecurityCheck.class);
	
	/**
	 * Default checks on the provided Assertion: <br>
	 * - security data not null
	 * - valid signature against trustore public key  <br>
	 * - not expired assertion  <br>
	 *   
	 * @param middlewareData
	 * @throws RerFonteError
	 * @throws ObjectNotFoundError
	 * @throws AuthorizationDeniedError
	 */
	public static void defaultChecks(String securityData) throws AuthorizationDeniedError {

		if (securityData == null) {
			throw new AuthorizationDeniedError(new String[] { "Autorizzazione non fornita" });
		}

		// Recupera credenziali (Certificato pubblico MW) da trustore
		Assertion a = null;
		KeyStore ts = null;
		Signature sig = null;

		try {
			a = (Assertion) SamlWriter.parseAssertion(securityData);
			sig = a.getSignature();
			ts = SamlUtil.loadKeystore(Config.getParam(Config.Constants.TRUSTSTORE_PATH),
					Config.getParam(Config.Constants.TRUSTSTORE_PASS));
		} catch (Exception e) {
			throw new AuthorizationDeniedError(new String[] { "Errore caricamento trustore/keystore" });
		}

		Credential c = SamlUtil.getCredential(ts,
				Config.getParam(Config.Constants.TRUSTSTORE_ALIAS),
				Config.getParam(Config.Constants.TRUSTSTORE_PASS));

		// Test validita crittografica rispetto a certificato MW
		if (SamlValidator.validateSignature(sig, c)) {
			System.out.println("\n-- Signature is valid, Service can be provided..");
		} else {
			System.out.println("\n-- Signature is invalid! No service for you.");
			throw new AuthorizationDeniedError(new String[] { "Firma MW non valida" });
		}

		// FIXME Decommenta
		// Test validita data emissione / scadenza

		if (!SamlUtil.isValidDate(a)) {
			System.out.println("\n-- Assertion is expired! No service for you.");
			throw new AuthorizationDeniedError(new String[] { "Asserzione MW scaduta" });
		}
	}
	
	/**
	 * Recupera gli identificativi delle fonti ammesse dall'asserzione e fa
	 * l'intersezione con gli identificativi definiti nella configurazione
	 * (fonte_mapping.json) e restituisce.
	 * 
	 * @param un
	 *            mapper per recuperare l'ID / gli ID assegnati alla fonte
	 * @param securityData
	 *            asserzione di sicurezza del MW
	 * @return
	 */
	public static <T extends FonteMapping> Collection<String> getFontiAmmesse(Class<T> mapper,
			String securityData) throws RerFonteError {

		FonteMapping fonteMapping = null;
		Collection<String> fontiAmmesse = new ArrayList<String>();

		try {
			fonteMapping = mapper.newInstance();
		} catch (Exception e) {
			LOG.error("Unable to instance FonteMapper: " + mapper.getCanonicalName(), e);
			throw new RerFonteError("No configuration file fonte_mapping.json found", e);
		}

		if (fonteMapping != null) {
			fonteMapping.loadAsserzione(securityData);
			if (fonteMapping.hasOperatorePA()) {
				fontiAmmesse = fonteMapping.getFontiAmmessePerOperatorePA();
			} else {
				fontiAmmesse = fonteMapping.getFontiAmmesse();
			}
		}
		
		return fontiAmmesse;
	}
	
}
