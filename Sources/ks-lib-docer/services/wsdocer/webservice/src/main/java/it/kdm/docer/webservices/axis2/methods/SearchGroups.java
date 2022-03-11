package it.kdm.docer.webservices.axis2.methods;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;

import java.util.Iterator;

/**
 * Created by Lorenzo Lucherini on 1/27/15.
 */
public class SearchGroups extends MethodAdapter {

    @Override
    protected void handleRequest(OMElement body, MessageContext msgContext) throws MethodException {
        Optional<String> prefixOpt = getPrefix(msgContext);
        if (prefixOpt.isPresent()) {
            String prefix = prefixOpt.get();

            Iterator<?> children = body.getChildElements();

            while (children.hasNext()) {
                OMElement child = (OMElement) children.next();

                if (child.getLocalName().equals("searchCriteria")) {
                    Optional<OMElement> key = getFirstChildWithLocalName(child, "key");
                    if (key.isPresent()) {

                        String keyText = key.get().getText();
                        if (!Strings.isNullOrEmpty(keyText) &&
                                (keyText.equalsIgnoreCase("GROUP_ID") || keyText.equalsIgnoreCase("PARENT_GROUP_ID"))) {
                            Optional<OMElement> value = getFirstChildWithLocalName(child, "value");
                            if (value.isPresent()) {
                                String valueText = value.get().getText();

                                if (!Strings.isNullOrEmpty(valueText)) {
                                    value.get().setText(prefix + valueText);
                                }
                            }
                        }
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

                    Iterator<?> metadataIter = child.getChildElements();
                    while (metadataIter.hasNext()) {
                        OMElement metadata = (OMElement) metadataIter.next();

                        if (metadata.getLocalName().equals("metadata")) {
                            Optional<OMElement> key = getFirstChildWithLocalName(metadata, "key");
                            if (key.isPresent()) {

                                String keyText = key.get().getText();
                                if (!Strings.isNullOrEmpty(keyText) &&
                                        (keyText.equals("GROUP_ID") || keyText.equals("PARENT_GROUP_ID"))) {
                                    Optional<OMElement> value = getFirstChildWithLocalName(metadata, "value");
                                    if (value.isPresent()) {
                                        String valueText = value.get().getText();

                                        if (!Strings.isNullOrEmpty(valueText) && valueText.startsWith(prefix)) {
                                            value.get().setText(valueText.substring(prefix.length()));
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
    }
}
