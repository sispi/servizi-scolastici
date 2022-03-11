package it.kdm.docer.core.handlers;

import it.kdm.docer.commons.Utils;
import it.kdm.docer.core.authorization.AuthorizationService;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.handlers.AbstractHandler;
import org.slf4j.Logger;

public class AuthorizationHandler extends AbstractHandler {

    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthorizationHandler.class);

    public InvocationResponse invoke(MessageContext context) throws AxisFault {

        logger.debug(String.format("invoke(%s)", context));

        if (context.getRelatesTo() != null) {
            // E' una response
            logger.debug(" --> e' una response");
            return InvocationResponse.CONTINUE;
        }

        AxisService service = context.getAxisService();
        if (service == null) {
            logger.error("Service Name is null");
            throw AxisFault.makeFault(new Exception("Error: Service Name is null"));
        }

        if (service.getName().equalsIgnoreCase("AuthorizationService") || service.getName().equalsIgnoreCase("TracerService")) {
            logger.debug(service.getName() + " --> CONTINUE");
            return InvocationResponse.CONTINUE;
        }

        OMElement method = context.getEnvelope().getBody().getFirstElement();
        if (method == null) {
            logger.error("Method is null!");
            return InvocationResponse.CONTINUE;
        }

        logger.debug("Method Name: " + method.getLocalName());

        OMElement token = Utils.findToken(method);
        if (token == null) {
            if (method.getLocalName().equalsIgnoreCase("login") ||
                    method.getLocalName().equalsIgnoreCase("getRealUser") ||
                    method.getLocalName().equalsIgnoreCase("loginSSO")) {
                logger.debug("login --> CONTINUE");
                return InvocationResponse.CONTINUE;
            }
            logger.error("Token not specified");
            throw AxisFault.makeFault(new Exception("Error: Token not specified"));
        }

        if (method.getLocalName().equalsIgnoreCase("loginOnDemand")) {
            return InvocationResponse.CONTINUE;
        }

        if (!this.isAuthorized(token.getText(), service.getName(), method.getLocalName())) {
            // TODO: Verificare se tirare eccezione o rispondere con
            // errore HTTP access denied
            logger.error("Token non autorizzato ad eseguire la chiamata.");
            throw new AxisFault("Token non autorizzato ad eseguire la chiamata.");
            // throw AxisFault.makeFault(new
            // Exception("Token non autorizzato ad eseguire la chiamata."));
        }

        // TODO: Con l'introduzione del redirect nei servizi l'estrazione del
        // ticket non dovrebbe piu' servire visto che i filtri serviranno solo
        // per le funzionalita' di core
    /*
     * String ticket; try { ticket = Utils.extractTokenKey(token.getText(),
	 * "ticket" + service.getName()); } catch (KeyException e) { throw new
	 * AxisFault("Error: Token is not valid"); } // Sostituzione del token
	 * con il ticket token.setText(ticket);
	 */

        return InvocationResponse.CONTINUE;
    }

    private boolean isAuthorized(String token, String serviceName, String methodName) throws AxisFault {
        logger.debug(String.format("isAuthorized(%s, %s, %s)", token, serviceName, methodName));

        AuthorizationService service = new AuthorizationService();
        return service.isAuthorized(token, serviceName, methodName);
    }

}
