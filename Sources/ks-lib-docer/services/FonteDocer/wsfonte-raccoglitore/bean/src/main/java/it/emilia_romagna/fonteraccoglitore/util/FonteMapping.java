package it.emilia_romagna.fonteraccoglitore.util;

import static it.emilia_romagna.fonteraccoglitore.util.MatcherEvaluator.select;
import static it.emilia_romagna.fonteraccoglitore.util.MatcherEvaluator.the;
import static it.emilia_romagna.regione.samlutil.Util.retrieveRuoliFromAssertionAttribute;
import static org.hamcrest.Matchers.*;
import it.emilia_romagna.regione.samlutil.beans.Delega;
import it.emilia_romagna.regione.samlutil.beans.Ruolo;
import it.emilia_romagna.regione.samlutil.samlwriter.SamlWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.opensaml.saml2.core.Assertion;

public abstract class FonteMapping {
	private static Logger LOG = org.slf4j.LoggerFactory.getLogger(FonteMapping.class);
	
	private List<Ruolo> assertion = new ArrayList<Ruolo>();
	
	public abstract Collection<String> getFonti();
	
	public abstract Map<String, String> getFontiMap();
	
	public void loadAsserzione(String securityData) {
		Assertion as = this.getAssertion(securityData);
		if (as != null) {
			assertion = retrieveRuoliFromAssertionAttribute(as, "DELEGHE");
		} else {
			assertion = new ArrayList<Ruolo>();
		}
	}
	
	public Collection<String> getFontiAmmesse() {
		List<String> allCodiciFonti = getAllCodiciFonti(assertion);
		if (allCodiciFonti == null) {
			return this.getFontiMap().values();
		} 			
		return select(this.getFontiMap(), isIn(allCodiciFonti)).values();
	}
	
	public boolean hasOperatorePA() {
		return the(assertion, hasItem(hasProperty("tipoRuolo", equalTo("OPERATORE_PA"))));
	}
	
	public Collection<String> getFontiAmmessePerOperatorePA() {
		Collection<String> result = new ArrayList<String>();
		for (Ruolo ruolo : assertion) {
			if (ruolo.getTipoRuolo().equals("OPERATORE_PA")) {
				if (ruolo.getDeleghe() != null) {
					for(Delega delega : ruolo.getDeleghe()) {
						if (delega != null && this.getFontiMap() != null && this.getFontiMap().keySet().contains(delega.getId())) {
							result.add(this.getFontiMap().get(delega.getId()));
						}
					}
				}
			}
		}
		return result;
	}
	
	public Collection<Ruolo> getRuolieDelegheDaAsserzione() {
		Collection<Ruolo> ruoli = new ArrayList<Ruolo>();
		for (Ruolo ruolo : assertion) {
			if (!ruolo.getTipoRuolo().equals("FONTI_AMMESSE")) {
				ruoli.add(ruolo);
			}
		}
		return ruoli;
	}
	
	private Assertion getAssertion(String secutityData) {
		try {
			return SamlWriter.parseAssertion(secutityData);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
	
	private static List<String> getAllCodiciFonti(List<Ruolo> ruoli) {
		List<String> codiciFonti = new ArrayList<String>();
		for (Ruolo ruolo : ruoli) {
			if (ruolo.getHasAllFonti()) {
				return null;
			}
			for (Delega delega : ruolo.getDeleghe()) {
				if (!codiciFonti.contains(delega.getId())) {
					codiciFonti.add(delega.getId());
				}
			}
		}
		return codiciFonti;
	}
}
