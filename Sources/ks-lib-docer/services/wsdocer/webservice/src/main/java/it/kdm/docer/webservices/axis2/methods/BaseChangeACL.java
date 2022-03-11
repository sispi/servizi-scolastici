package it.kdm.docer.webservices.axis2.methods;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;

import java.util.Iterator;

/**
 * Created by Lorenzo Lucherini on 1/27/15.
 */
public class BaseChangeACL extends MethodAdapter {

    @Override
    protected void handleRequest(OMElement body, MessageContext msgContext) throws MethodException {

        Optional<String> prefixOpt = getPrefix(msgContext);
        if (prefixOpt.isPresent()) {
            String prefix = prefixOpt.get();

            Iterator<?> children = body.getChildElements();

            while (children.hasNext()) {
                OMElement child = (OMElement) children.next();

                if (child.getLocalName().equals("aclToAdd")) {
                    Optional<OMElement> key = getFirstChildWithLocalName(child, "key");
                    if (key.isPresent()) {

                        String keyText = key.get().getText();
                        if (!Strings.isNullOrEmpty(keyText)) {
                            key.get().setText(prefix + keyText);
                        }
                    }
                } else if (child.getLocalName().equals("aclToRemove")) {
                    String text = child.getText();

                    if (!Strings.isNullOrEmpty(text)) {
                        child.setText(prefix + text);
                    }
                }
            }
        }
    }

    @Override
    protected void handleResponse(OMElement body, MessageContext msgContext) throws MethodException {

        Optional<String> prefixOpt = getPrefix(msgContext);
        if (prefixOpt.isPresent()) {
            String prefix = prefixOpt.get();

            Iterator<?> children = body.getChildElements();
            while (children.hasNext()) {
                OMElement child = (OMElement) children.next();

                if (child.getLocalName().equals("return")) {
                    Optional<OMElement> key = getFirstChildWithLocalName(child, "key");
                    if (key.isPresent()) {

                        String keyText = key.get().getText();
                        if (!Strings.isNullOrEmpty(keyText) && keyText.startsWith(prefix)) {
                            key.get().setText(keyText.substring(prefix.length()));
                        }
                    }
                }
            }
        }
    }
}
