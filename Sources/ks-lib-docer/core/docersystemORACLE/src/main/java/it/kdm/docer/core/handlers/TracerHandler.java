package it.kdm.docer.core.handlers;


import java.io.IOException;
import java.security.KeyException;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.slf4j.Logger;

import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.core.tracer.Trace;
import it.kdm.docer.core.tracer.Tracer;

public class TracerHandler extends AbstractHandler {
    static String tracerMethodList = "";

    Logger logger = org.slf4j.LoggerFactory.getLogger(TracerHandler.class);

    private enum MODE {
        OFF,
        HEADERS,
        FULL
    }

    private enum TYPE_FLOW {
        IN,
        OUT,
        ALL
    }

    private MODE currentMode;
    private TYPE_FLOW typeMode;

    public TracerHandler() throws IOException {
        Properties props = ConfigurationUtils.loadProperties("tracer.properties");

        String mode = props.getProperty("mode");
        this.currentMode = MODE.valueOf(mode.toUpperCase());

        String type = props.getProperty("type");
        this.typeMode = TYPE_FLOW.valueOf(type.toUpperCase());

    }

    public InvocationResponse invoke(MessageContext msgContext)
            throws AxisFault {

        logger.debug(String.format("invoke(%s)", msgContext));

        if (this.currentMode.equals(MODE.OFF)) {
            return InvocationResponse.CONTINUE;
        }

        int type = msgContext.getFLOW();
        if (!getFlowType(type)) {
            return InvocationResponse.CONTINUE;
        }


        OMElement method = msgContext.getEnvelope().getBody().getFirstElement();
        String envelope;
        switch (this.currentMode) {
            case HEADERS:
                //TODO: Get only headers
                envelope = msgContext.getEnvelope().toString();
                break;
            case FULL:
            default:
                envelope = msgContext.getEnvelope().toString();
                break;
        }

        if (method == null) {
            logger.error("Method is null!");
            return InvocationResponse.CONTINUE;
        } else if (method.getLocalName().equalsIgnoreCase("login")) {

            Trace traceItem = new Trace();
            //traceItem.setMessage(envelope);
            traceItem.setMethodName(method.getLocalName());
            traceItem.setServiceName("DocerServices");

            if (msgContext.getEnvelope().getBody().getFirstElement() == null) {
                logger.error("checkRequest Body is null!");
                return InvocationResponse.CONTINUE;
            }

            String userId = null;
            Iterator<?> iter = msgContext.getEnvelope().getBody().getFirstElement().getChildElements();
            while (iter.hasNext()) {
                OMElement elem = (OMElement) iter.next();
                if (elem.getLocalName().equalsIgnoreCase("username"))
                    userId = elem.getText();
            }

            traceItem.setUser(userId);

            this.writeTrace("", traceItem, type, "wire");

            return InvocationResponse.CONTINUE;

        } else if (method.getLocalName().equalsIgnoreCase("checkRequest") ||
                method.getLocalName().equalsIgnoreCase("checkRequestResponse")) {
            String serviceName = msgContext.getAxisMessage().getName();

            String callerMethod = "";
            String callerService = "";
            String callerToken = "";
            String docNum = null;
            String extraData = null;
            String serviceUrl = null;

            if (msgContext.getEnvelope().getBody().getFirstElement() == null) {
                logger.error("checkRequest Body is null!");
                return InvocationResponse.CONTINUE;
            }

            Iterator<?> iter = msgContext.getEnvelope().getBody().getFirstElement().getChildElements();
            while (iter.hasNext()) {
                OMElement elem = (OMElement) iter.next();
                if (elem.getLocalName().equalsIgnoreCase("method"))
                    callerMethod = elem.getText();
                if (elem.getLocalName().equalsIgnoreCase("service"))
                    callerService = elem.getText();
                if (elem.getLocalName().equalsIgnoreCase("token"))
                    callerToken = elem.getText();
                if (elem.getLocalName().equalsIgnoreCase("docnumber"))
                    docNum = elem.getText();
                if (elem.getLocalName().equalsIgnoreCase("extradata"))
                    extraData = elem.getText();
                if (elem.getLocalName().equalsIgnoreCase("serviceUrl"))
                    serviceUrl = elem.getText();
            }
            Tracer tracer = new Tracer();
                         /*
                        //if (tracerMethodList.equalsIgnoreCase("")) {
                            try {
                                Tracer tracer = new Tracer();
                                tracerMethodList=tracer.readMethodList();
                            } catch (Exception ex) {
                                java.util.logging.org.slf4j.LoggerFactory.getLogger(TracerHandler.class.getName()).error(ex.getMessage(), ex);
                            }
                        //}
                        
                        //skip dei metodi non dichiarati per il trace
                        if (!tracerMethodList.contains(";"+callerService+"!"+callerMethod+";"))
                            return InvocationResponse.CONTINUE;
                        */

            //se non sono valorizzati non traccia
            if (docNum == null && extraData == null && serviceUrl == null)
                return InvocationResponse.CONTINUE;

            Trace traceItem = new Trace();
            //traceItem.setMessage(envelope);
            traceItem.setMethodName(callerMethod);
            traceItem.setServiceName(callerService);

            if (docNum != null && !docNum.equals(""))
                traceItem.setDocNum(docNum);

            if (extraData != null && !extraData.equals(""))
                traceItem.setExtraData(extraData);

            if (serviceUrl != null && !serviceUrl.equals(""))
                traceItem.setServiceUrl(serviceUrl);

            try {
                traceItem.setUser(Utils.extractTokenKey(callerToken, "uid"));
                //estrazione ulteriori dati per WF tessere
                traceItem.setApplication(Utils.extractTokenKey(callerToken, "app"));
                traceItem.setCodEnte(Utils.extractTokenKey(callerToken, "ente"));
                traceItem.setIp(Utils.extractTokenKey(callerToken, "ipaddr"));
                //---------------------------------------------------------
            } catch (KeyException ex) {
                org.slf4j.LoggerFactory.getLogger(TracerHandler.class.getName()).error(ex.getMessage(), ex);
            }


            this.writeTrace(callerToken, traceItem, type, "wire");
            return InvocationResponse.CONTINUE;
                        /*
            Iterator<?> iter = method.getChildrenWithName(new QName("envelope"));
			if(!iter.hasNext()) {
				Exception ex = new Exception("Envelope is null!");
				ex.fillInStackTrace();
				logger.error(ex.getMessage(), ex);
				throw AxisFault.makeFault(ex);
			}
			
			try {
				OMElement elem = (OMElement)iter.next();
				envelope = Utils.base64Decode(elem.getText());
				OMElement envelopeElem = Utils.parseSoapXML(envelope);
				method = envelopeElem.getFirstElement().getFirstElement();
				
			} catch (XMLStreamException e) {
				logger.error(e.getMessage(), e);
				throw AxisFault.makeFault(e);
			}
                        * 
                        */
        }
		/*
		logger.debug("Method Name: " + method.getLocalName());
		
		OMElement tokenElem = Utils.findToken(method);
		// Non si puo' fare check aggiuntivi sul token in quanto 
		// non e' presente nelle richieste in uscita
		String token = tokenElem == null ? "" : tokenElem.getText();
		
		//int type = msgContext.getFLOW();
		
		this.writeTrace(token, method.getLocalName(), envelope, type, "wire");
		*/
        return InvocationResponse.CONTINUE;
    }

    private void writeTrace(String token, it.kdm.docer.core.tracer.Trace traceItem, int type, String level) throws AxisFault {
        logger.debug(String.format("writeTrace(%s,%s,%s, %s, %d, %s)", token, traceItem.getServiceName(), traceItem.getMethodName(), traceItem.getMessage(), type, level));

        try {
            Tracer service = new Tracer();
            service.write(traceItem, Tracer.LEVEL.NORMAL, Tracer.TYPE.getType(type));
        } catch (Exception ex) {
            throw AxisFault.makeFault(ex);
        }
    }

    private boolean getFlowType(int type) {
        if (type == 1 && this.typeMode.equals(TYPE_FLOW.IN)) {
            return true;
        }
        if (type == 2 && this.typeMode.equals(TYPE_FLOW.OUT)) {
            return true;
        }
        if (this.typeMode.equals(TYPE_FLOW.ALL)) {
            return true;
        }

        return false;
    }
}
