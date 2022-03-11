package it.kdm.docer.commons.handlers;

import java.io.File;
import java.io.InputStream;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.jaxen.JaxenException;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.configuration.ConfigurationUtils;

public class ServiceFilter extends AbstractHandler {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ServiceFilter.class);
    private boolean initialized = false;
    private String epr;
    private String eprAuthentication;

    public InvocationResponse invoke(MessageContext context) throws AxisFault {

        //logger.debug(String.format("invoke(%s)", context));

        if (context.getFrom() == null) { // TODO: check if only internal calls
            // can trigger this.
            logger.debug("from: null!");
            return InvocationResponse.CONTINUE;
        }

        AxisService service = context.getAxisService();
        if (service == null) {
            throw AxisFault.makeFault(new Exception("Error: Service name is null"));
        }

        String serviceName = service.getName();

        OMElement methodElement = context.getEnvelope().getBody().getFirstElement();
        if (methodElement == null) {
            logger.error("Method is null!");
            throw AxisFault.makeFault(new Exception("Error: Method not specified"));
        }

        String methodName = methodElement.getLocalName();

        if (methodName.equalsIgnoreCase("loginOnDemand") ||
                methodName.equalsIgnoreCase("login") ||
                methodName.equalsIgnoreCase("loginSSO") ||
                methodName.equalsIgnoreCase("getRealUser") ||
                methodName.equalsIgnoreCase("verifyTicket")) {
            try {

                OMElement tokenElem = Utils.findToken(methodElement);
                if (tokenElem != null) {
                    String token = tokenElem.getText();

                    String ticketName = String.format("ticket%s", serviceName);

                    String ticket;

                    try {
                        ticket = Utils.extractTokenKey(token, ticketName);
                    } catch (Exception e){
                        ticket = Utils.extractFirstTicket(token);
                    }

                    TicketCipher tc = new TicketCipher();
                    String cleanTicket = tc.decryptTicket(ticket);
                    tokenElem.setText(cleanTicket);
                } else {
                    logger.warn("Impossibile estrarre il token dalla chiamata " + methodName);
                }

            } catch (KeyException e) {
                logger.warn("Impossibile estrarre il token dalla chiamata " + methodName);
            }

            return InvocationResponse.CONTINUE;
        }

        OMElement tokenElem = Utils.findToken(methodElement);
        if (tokenElem == null) {
            throw AxisFault.makeFault(new Exception("Error: Token not specified"));
        }

        String token = tokenElem.getText();
        if (context.getOperationContext()!=null)
            context.getOperationContext().setProperty("token", token);

        // bug fix su pubblicazione della configurazione del FrontendService
        if (serviceName.equalsIgnoreCase("FrontendService") && (methodElement.getLocalName().equalsIgnoreCase("writeConfig") || methodElement.getLocalName().equalsIgnoreCase("readConfig"))) {
            return InvocationResponse.CONTINUE;
        }


        try {
            if (Utils.hasTokenKey(token, "calltype")) {
                String callType = Utils.extractTokenKey(token, "calltype");

                if (callType.equals("internal")) {

                    token = Utils.removeTokenKey(token, "calltype").replace("|", "");
                    tokenElem.setText(token);
                    return InvocationResponse.CONTINUE;
                }
            }
        } catch (KeyException e) {
            logger.info("Call di tipo NORMAL");
        }

        // Gestione on demand
        String ticket = loginOnDemand(token, context);

        String docNumber = null;
        OMElement extraData = null;
        String prefix = "";
        String namespaceURI = "";

        String serviceUrl = null;

        try {
                OMElement conf = null;
                try {
                    conf = getConfig(ticket, context);
                }catch(Exception e){
                    logger.error("getConfig Error ticket:"+ticket);
                    logger.error("getConfig Error token:"+token);
                    logger.error("getConfig Error Exception:"+e.getMessage());
                    logger.error("getConfig Error Exception:"+e);
                    ServiceFilterAuthenticationCache.getInstance().putTicket(token,"");
                }

            AXIOMXPath path = new AXIOMXPath(String.format("//group[@name='audit' and @active='true']", methodName));

            OMElement auditNode = (OMElement) path.selectSingleNode(conf);
            if (auditNode != null) {
                prefix = auditNode.getAttributeValue(new QName("prefix"));
                namespaceURI = auditNode.getAttributeValue(new QName("namespaceURI"));
            }

            path = new AXIOMXPath(String.format("//group[@name='audit' and @active='true']" + "/section[@name='%s' and @active='true']", methodName));
            OMElement audit = (OMElement) path.selectSingleNode(conf);

            if (audit != null) {
                if (prefix == null) {
                    AxisFault fault = AxisFault.makeFault(new Exception("AUDIT ERROR: attributo 'prefix' mancante. Controllare la configurazione della sezione audit del servizio: " + serviceName));
                    throw fault;
                }

                if (namespaceURI == null) {
                    AxisFault fault = AxisFault.makeFault(new Exception("AUDIT ERROR: attributo 'namespaceURI' mancante. Controllare la configurazione della sezione audit del servizio: "
                            + serviceName));
                    throw fault;
                }

                HttpServletRequest httpServletRequest = (HttpServletRequest) context.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
                serviceUrl = httpServletRequest.getRequestURL().toString();

                Iterator<?> children = audit.getChildElements();
                while (children.hasNext()) {
                    OMElement child = (OMElement) children.next();
                    // String childName = child.getLocalName();
                    String childName = child.getAttributeValue(new QName("name"));
                    String childValue = child.getAttributeValue(new QName("xpath"));

                    if (childName == null) {
                        AxisFault fault = AxisFault.makeFault(new Exception("AUDIT ERROR: attributo 'name' mancante o non valido. Controllare la configurazione della sezione audit. [" + serviceName
                                + ":" + methodName + "]"));
                        throw fault;
                    }

                    if (childValue == null) {
                        AxisFault fault = AxisFault.makeFault(new Exception("AUDIT ERROR: attributo 'xpath' mancante o non valido. Controllare la configurazione della sezione audit. [" + serviceName
                                + ":" + methodName + "]"));
                        throw fault;
                    }

                    List<OMElement> data = extractData(childValue, context.getEnvelope(), prefix, namespaceURI);
                    if (data.size() > 0) {
                        if (childName.equalsIgnoreCase("docnumber")) {
                            docNumber = data.get(0).getText();
                        } else if (!childName.isEmpty()) {
                            if (extraData == null) {
                                extraData = child.getOMFactory().createOMElement("extradata", null);
                            }
                            for (OMElement elem : data) {
                                extraData.addChild(elem.cloneOMElement());
                            }
                        }
                    }
                }
            }
        } catch (JaxenException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
            logger.error(e1.getMessage(),e1);
        }


        // TODO da rimuovere la checkRequest?
        if (this.checkRequest(token, serviceName, methodName, docNumber, extraData, serviceUrl)) {
            try {

                TicketCipher tc = new TicketCipher();
                String cleanTicket = tc.decryptTicket(ticket);

                // controllo su ticket=LOGIN_ERROR (genera exception)
                if (StringUtils.isBlank(cleanTicket) || cleanTicket.equals("LOGIN_ERROR")) {

                    AxisFault fault = AxisFault
                            .makeFault(new Exception(
                                    "[401] - Il servizio richiesto non risulta correttamente autenticato. Eseguire nuovamente l'autenticazione al sistema. Se il problema persiste, contattare l'ammnistratore di sistema e segnalare il problema."));
                    throw fault;
                }

                tokenElem.setText(ticket);
                return InvocationResponse.CONTINUE;

            } catch (Exception e) {
                throw AxisFault.makeFault(e);
            }
        }

        return InvocationResponse.ABORT;
    }


    @SuppressWarnings("unchecked")
    private List<OMElement> extractData(String xpath, SOAPEnvelope soapEnvelope, String prefix, String namespaceURI) throws JaxenException {
        AXIOMXPath path = new AXIOMXPath(soapEnvelope, xpath);
        path.addNamespace(prefix, namespaceURI);

        return (List<OMElement>) path.selectNodes(soapEnvelope);
    }

    private OMElement getConfig(String ticket, MessageContext context) throws AxisFault {
        String epr = context.getAxisService().getEPRs()[0];

        if (!this.initialized) {
            this.initialize();
        }

        String xmlElementReadConfig = null;
        String returnElementString = "";
        OMElement returnElem=null;

        try {

            HttpClient client = new HttpClient();
            if (!epr.endsWith("/")) {
                epr += "/";
            }
            PostMethod httpMethod = new PostMethod(epr + "readConfig");

            ticket += "|calltype:internal|";
            NameValuePair[] data = {new NameValuePair("token", ticket)};

            httpMethod.setRequestBody(data);

            client.executeMethod(httpMethod);

            InputStream in = httpMethod.getResponseBodyAsStream();

            OMElement payload = Utils.parseMTOMSoapXML(in);
            returnElem = payload.getFirstElement();
            returnElementString=returnElem.getText();
            return AXIOMUtil.stringToOM(returnElem.getText());

        } catch (Exception e) {

            System.out.println("[ERROR]: " + epr + "readConfig");
            logger.error("getConfig Error returnElem:"+returnElem.toString());
            logger.error("getConfig Error returnElementString:"+returnElementString);
            logger.error(e.getMessage(), e);

            throw AxisFault.makeFault(e);
        }
    }

    private String loginOnDemand(String token, MessageContext context) throws AxisFault {
        if (!this.initialized) {
            this.initialize();
        }

        // Controlliamo che il ticket sia presente in chace
        String ret = ServiceFilterAuthenticationCache.getInstance().getTicket(token);
        if (StringUtils.isNotBlank(ret)) {
            // Se c'è, non bisogna contattare il server
            return ret;
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) context.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
        String serviceUrl = httpServletRequest.getRequestURL().toString();
        String serviceName = context.getAxisService().getName();
        String methodName = context.getEnvelope().getBody().getFirstElement().getLocalName();

        HttpClient client = new HttpClient();
        String url = eprAuthentication + "/loginOnDemand";
        PostMethod httpMethod = new PostMethod(url);

        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new NameValuePair("token", token));
        data.add(new NameValuePair("serviceName", serviceName));
        data.add(new NameValuePair("method", methodName));
        data.add(new NameValuePair("serviceUrl", serviceUrl));

        httpMethod.setRequestBody(data.toArray(new NameValuePair[0]));

        try {
            client.executeMethod(httpMethod);

            InputStream in = httpMethod.getResponseBodyAsStream();

            OMElement payload = Utils.parseCleanedSoapXML(in);

            OMElement returnElem = payload.getFirstElement();

            logger.debug("returnElem: " + returnElem.getLocalName());
            // TODO: ricavare status code della AxisFault originale
            if (httpMethod.getStatusCode() != 200) {
                AxisFault ex = new AxisFault(returnElem.getText());
                ex.setFaultCode(Integer.toString(httpMethod.getStatusCode()));
                ex.setDetail(payload);
                throw ex;
            }

            String ticket = returnElem.getText();

            // Aggiungiamo in cache
            ServiceFilterAuthenticationCache.getInstance().putTicket(token, ticket);

            return ticket;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw AxisFault.makeFault(e);
        }
    }

    private boolean checkRequest(String token, String service, String method, String docNumber, OMElement extraData, String serviceUrl) throws AxisFault {
        logger.debug(String.format("checkRequest(%s, %s, %s)", token, service, method));

        if (!this.initialized) {
            this.initialize();
        }

        HttpClient client = new HttpClient();
        PostMethod httpMethod = new PostMethod(epr + "/checkRequest");
        httpMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        // String encodedEnvelope = Utils.base64Encode(envelope);

        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new NameValuePair("token", token));
        data.add(new NameValuePair("service", service));
        data.add(new NameValuePair("method", method));

        if (docNumber != null) {
            data.add(new NameValuePair("docnumber", docNumber));
        }

        if (extraData != null) {
            data.add(new NameValuePair("extraData", extraData.toString()));
        }

        if (serviceUrl != null) {
            data.add(new NameValuePair("serviceUrl", serviceUrl));
        }

        httpMethod.setRequestBody(data.toArray(new NameValuePair[0]));

        try {
            client.executeMethod(httpMethod);

            InputStream in = httpMethod.getResponseBodyAsStream();

            OMElement payload = Utils.parseCleanedSoapXML(in);

            OMElement returnElem = payload.getFirstElement();

            logger.debug("returnElem: " + returnElem.getLocalName());
            // TODO: ricavare status code della AxisFault originale
            if (httpMethod.getStatusCode() != 200) {
                AxisFault ex = new AxisFault(returnElem.getText());
                ex.setFaultCode(Integer.toString(httpMethod.getStatusCode()));
                ex.setDetail(payload);
                throw ex;
            }

            return Boolean.parseBoolean(returnElem.getText());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw AxisFault.makeFault(e);
        }
    }

    private void initialize() throws AxisFault {

        try {
            File filterConfig = ConfigurationUtils.loadResourceFile("filter-configuration.xml");
            XMLConfiguration conf = new XMLConfiguration(filterConfig);
            this.epr = conf.getString("remote-authorization-epr");
            this.eprAuthentication = conf.getString("remote-authentication-epr");

            this.initialized = true;
        } catch (Exception e) {
            // logger.error(e.getMessage(), e);
            throw AxisFault.makeFault(e);
        }
    }

    private OMElement createErrorDetail(String message, String code) {
        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMElement detail = factory.createOMElement("detail", null);
        OMElement messageElem = factory.createOMElement("message", null);
        messageElem.setText(message);
        detail.addChild(messageElem);
        OMElement codeElem = factory.createOMElement("code", null);
        codeElem.setText(code);
        detail.addChild(codeElem);

        return detail;
    }

}
