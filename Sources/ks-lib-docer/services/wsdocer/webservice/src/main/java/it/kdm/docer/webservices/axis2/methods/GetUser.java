package it.kdm.docer.webservices.axis2.methods;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;

import java.util.Iterator;

/**
 * Created by Lorenzo Lucherini on 1/27/15.
 */
public class GetUser extends GetUserRequest {

    @Override
    protected void handleResponse(OMElement body, MessageContext msgContext) {

        Optional<String> prefixOpt = getPrefix(msgContext);
        if (prefixOpt.isPresent()) {
            String prefix = prefixOpt.get();

            Iterator<?> children = body.getChildElements();

            while (children.hasNext()) {
                OMElement child = (OMElement) children.next();

                if (child.getLocalName().equals("return")) {

                    Optional<OMElement> key = getFirstChildWithLocalName(child, "key");
                    if (key.isPresent()) {
                        String text = key.get().getText();

                        if (!Strings.isNullOrEmpty(text) && text.equalsIgnoreCase("USER_ID")) {
                            Optional<OMElement> valueOpt = getFirstChildWithLocalName(child, "value");
                            if (valueOpt.isPresent()) {

                                OMElement value = valueOpt.get();
                                text = value.getText();

                                if (!Strings.isNullOrEmpty(text) && text.startsWith(prefix)) {
                                    value.setText(text.substring(prefix.length()));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
