package it.kdm.docer.webservices.axis2.methods;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;

import java.util.Iterator;

/**
 * Created by Lorenzo Lucherini on 1/26/15.
 */
public class GetGroupsOfUser extends GetUserRequest {

    @Override
    protected void handleResponse(OMElement body, MessageContext msgContext) {

        Optional<String> prefixOpt = getPrefix(msgContext);
        if (prefixOpt.isPresent()) {
            String prefix = prefixOpt.get();

            Iterator<?> children = body.getChildElements();

            while (children.hasNext()) {
                OMElement child = (OMElement) children.next();

                if (child.getLocalName().equals("return")) {
                    String text = child.getText();

                    if (!Strings.isNullOrEmpty(text) && text.startsWith(prefix)) {
                        child.setText(text.substring(prefix.length()));
                    }
                }
            }
        }
    }
}
