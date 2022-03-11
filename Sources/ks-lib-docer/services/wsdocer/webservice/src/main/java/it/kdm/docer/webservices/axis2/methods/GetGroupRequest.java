package it.kdm.docer.webservices.axis2.methods;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;

import java.util.Iterator;

/**
 * Created by Lorenzo Lucherini on 1/27/15.
 */
public class GetGroupRequest extends MethodAdapter {

    @Override
    protected void handleRequest(OMElement body, MessageContext msgContext) throws MethodException {

        Optional<String> prefixOpt = getPrefix(msgContext);
        if (prefixOpt.isPresent()) {
            String prefix = prefixOpt.get();

            Iterator<?> children = body.getChildElements();

            while (children.hasNext()) {
                OMElement child = (OMElement) children.next();

                if (child.getLocalName().equals("groupId")) {
                    String text = child.getText();
                    if (!Strings.isNullOrEmpty(text)) {
                        child.setText(prefix + text);
                        break;
                    }
                }
            }
        }
    }
}
