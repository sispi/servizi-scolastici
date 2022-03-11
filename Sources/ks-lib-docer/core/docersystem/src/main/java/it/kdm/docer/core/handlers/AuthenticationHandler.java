package it.kdm.docer.core.handlers;

import it.kdm.docer.commons.Utils;
import it.kdm.docer.core.authentication.AuthenticationService;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.handlers.AbstractHandler;
import org.slf4j.Logger;

public class AuthenticationHandler extends AbstractHandler {

    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenticationHandler.class);

    public InvocationResponse invoke(MessageContext context) throws AxisFault {
        logger.debug(String.format("invoke(%s)", context));

        if (context.getRelatesTo() != null) {
            // E' una response
            return InvocationResponse.CONTINUE;
        }

        AxisService service = context.getAxisService();
        if (service == null) {
            throw AxisFault.makeFault(new Exception("Error: Service Name is null"));
        }

        OMElement methodElement = context.getEnvelope().getBody().getFirstElement();
        if (methodElement == null) {
            logger.error("Method is null!");
            throw AxisFault.makeFault(new Exception("Error: Method not specified"));
        }

        if (methodElement.getLocalName().equalsIgnoreCase("login") ||
                methodElement.getLocalName().equalsIgnoreCase("loginSSO") ||
                methodElement.getLocalName().equalsIgnoreCase("getRealUser") ||
                methodElement.getLocalName().equalsIgnoreCase("loginOnDemand")) {

            // Bypass della verifica del token se la chiamata e'
            // per il login diretto da client
            return InvocationResponse.CONTINUE;

        }

        OMElement token = Utils.findToken(methodElement);
        if (token == null) {
            throw AxisFault.makeFault(new Exception("Error: Token not specified"));
        }
        try {
            if (!this.verifyToken(token)) {
                //TODO: Verificare se tirare eccezione o rispondere con
                //		errore HTTP access denied
                throw new Exception("Token non valido");
            }
            return InvocationResponse.CONTINUE;
        } catch (Exception e) {
            throw AxisFault.makeFault(e);
        }

    }

    private boolean verifyToken(OMElement token) throws Exception {
        logger.debug(String.format("verifyToken(%s)", token));

        AuthenticationService service = new AuthenticationService();
        return service.verifyToken(token.getText());

    }

}
