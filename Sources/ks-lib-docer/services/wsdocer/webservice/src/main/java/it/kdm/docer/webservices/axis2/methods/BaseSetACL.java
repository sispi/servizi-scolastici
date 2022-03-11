package it.kdm.docer.webservices.axis2.methods;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;

import java.util.Iterator;

/**
 * Created by Lorenzo Lucherini on 1/26/15.
 */
public class BaseSetACL extends MethodAdapter {

    @Override
    protected void handleRequest(OMElement body, MessageContext msgContext) {

        Optional<String> prefixOpt = getPrefix(msgContext);
        if (prefixOpt.isPresent()) {
            String prefix = prefixOpt.get();

            Iterator<?> children = body.getChildElements();

            while (children.hasNext()) {
                OMElement child = (OMElement) children.next();

                if (child.getLocalName().equals("acls")) {
                    Iterator<?> values = child.getChildElements();

                    while (values.hasNext()) {
                        OMElement value = (OMElement) values.next();

                        if (value.getLocalName().equals("key")) {
                            String text = value.getText();

                            if (!Strings.isNullOrEmpty(text)) {
                                value.setText(String.format("%s%s", prefix, text));
                            }

                            break;
                        }
                    }
                }
            }
        }
    }
}
