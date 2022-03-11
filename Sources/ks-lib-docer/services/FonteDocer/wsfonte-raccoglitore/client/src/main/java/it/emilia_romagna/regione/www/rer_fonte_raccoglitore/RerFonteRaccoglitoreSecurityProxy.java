package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

import org.apache.axis.client.Stub;
import org.apache.axis.message.SOAPHeaderElement;

public class RerFonteRaccoglitoreSecurityProxy extends RerFonteRaccoglitoreProxy {
	
	public RerFonteRaccoglitoreSecurityProxy() {
		
	}
	
	public RerFonteRaccoglitoreSecurityProxy(String endpoint) {
		super(endpoint);
	}
	
	public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore getRerFonteRaccoglitore(String securityData) {
	    RerFonteRaccoglitore rerFonteRaccoglitore = getRerFonteRaccoglitore();
	    SOAPHeaderElement header = new SOAPHeaderElement("http://www.regione.emilia-romagna.it/securityData","securityData",securityData);
		((Stub)rerFonteRaccoglitore).setHeader(header);
		return rerFonteRaccoglitore;
	}
	
}
