package it.kdm.docer.webservices.axis2.methods;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;

import java.util.Iterator;

/**
 * Created by Lorenzo Lucherini on 1/26/15.
 */
public class BaseGetACL extends MethodAdapter {

    @Override
    protected void handleResponse(OMElement body, MessageContext msgContext) {

        Optional<String> prefixOpt = getPrefix(msgContext);
        if (prefixOpt.isPresent()) {
            String prefix = prefixOpt.get();

            Iterator<?> returns = body.getChildElements();
            while (returns.hasNext()) {
                OMElement returnElem = (OMElement) returns.next();

                Iterator<?> values = returnElem.getChildElements();
                while (values.hasNext()) {
                    OMElement value = (OMElement) values.next();
                    if (value.getLocalName().equals("key")) {
                        String text = value.getText();
                        if (!Strings.isNullOrEmpty(text) && text.startsWith(prefix)) {
                            value.setText(text.substring(prefix.length()));
                        }
                        break;
                    }
                }
            }
        }
    }
}
