package it.kdm.docer.webservices.axis2.methods;

import com.google.common.base.Optional;
import it.kdm.docer.commons.Config;
import it.kdm.docer.commons.Utils;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.Handler;

import java.security.KeyException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Lorenzo Lucherini on 1/26/15.
 */
public class MethodAdapter {

    public final Handler.InvocationResponse invoke(MessageContext msgContext) {

        try {
            OMElement body = msgContext.getEnvelope().getBody().getFirstElement();

            switch (msgContext.getFLOW()) {
                case MessageContext.IN_FLOW:
                    handleRequest(body, msgContext);
                    break;
                case MessageContext.OUT_FLOW:
                    handleResponse(body, msgContext);
                    break;
            }
            return Handler.InvocationResponse.CONTINUE;
        } catch (MethodException e) {
            //TODO: Handle exception
            return Handler.InvocationResponse.ABORT;
        }
    }

    protected final Optional<String> getPrefix(MessageContext msgContext) {
        try {
            String token = (String) msgContext.getOperationContext().getProperty("token");
            if (token != null) {
                if (Utils.hasTokenKey(token, "calltype") &&
                    Utils.extractTokenKey(token, "calltype").equals("internal")) {
                    return Optional.absent();
                }

                return Optional.of(Utils.extractTokenKey(token, "prefix") + Utils.USERNAME_SEP);
            }

            return Optional.absent();
        } catch (KeyException e) {
            throw new IllegalStateException(e);
        }
    }

    protected final Optional<OMElement> getFirstChildWithLocalName(OMElement parent, String localName) {

        Iterator<?> keys = parent.getChildrenWithLocalName(localName);
        if (keys.hasNext()) {
            return Optional.of((OMElement) keys.next());
        }

        return Optional.absent();
    }

    protected void handleRequest (OMElement body, MessageContext msgContext) throws MethodException {}
    protected void handleResponse(OMElement body, MessageContext msgContext) throws MethodException {}

}
