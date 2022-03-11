package it.kdm.docer.core.authentication.loginondemand;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.core.authentication.AuthenticationCache;
import it.kdm.docer.core.authentication.SSOLoginService;
import it.kdm.docer.core.authentication.bean.LoginInfo;
import it.kdm.docer.core.authentication.bean.WSTicketAuthInfo;
import it.kdm.docer.core.authentication.providers.sso.SSOTicketCache;
import it.kdm.docer.core.configuration.ConfigurationManager;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.jaxen.JaxenException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.Map;

/**
 * Created by Łukasz Kwasek on 16/12/14.
 */
public class SSOLoginOnDemand extends AbstractLoginOnDemand {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(SSOLoginOnDemand.class);

    public SSOLoginOnDemand() throws Exception {
        super();
    }

    public String login(String token, String serviceName, String serviceUrl) throws Exception {

        // Estrai dalla cache i dati utente
        String saml = SSOTicketCache.getInstance().getSaml(token);

        if (StringUtils.isBlank(saml)) {
            // La login potrebbe essere scaduta
            throw new AuthenticationException("SAML non trovato in cache per il token fornito.");
        }

        LoginInfo li = new SSOLoginService().extractLoginInfo(saml);

        String userName = Utils.getUserFromToken(token);
        String password = li.getPass();
        String application = li.getApplication();
        String ente = li.getEnte();

        ConfigurationManager confManager = new ConfigurationManager();

        long servId = confManager.getIdService(serviceName);

        TicketCipher tc = new TicketCipher();

        Map.Entry<String, String> credentials =
                confManager.getSystemCredentials(servId, application, ente);
        if (credentials != null) {
            userName = credentials.getKey();
            password = credentials.getValue();
        }

        // Cerca il wsticket in cache, se non lo trovi, esegui login normale
        WSTicketAuthInfo wsTicketAuthInfo = new WSTicketAuthInfo(serviceUrl, userName, password, ente, application,token);
        String wsticket = AuthenticationCache.getInstance().getWSTicket(wsTicketAuthInfo);

        if (StringUtils.isBlank(wsticket) || wsticket.equals("LOGIN_ERROR")) {
            wsticket = serviceLogin(serviceUrl, saml, userName, ente);
            AuthenticationCache.getInstance().putWSTicket(wsTicketAuthInfo, wsticket);
        }

        String expirationTokenValue = getExpirationTokenValue();
        String encTicket = tc.encryptTicket(wsticket, expirationTokenValue);

        return encTicket;
    }

    private String serviceLogin(String url, String saml, String user, String ente) throws AuthenticationException {
        try {
            logger.debug(String.format("doServicelogin(%s,%s,%s)", user, ente, saml));

            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(getEpr(url, "loginSSO"));

            method.addParameter("saml", saml);
            method.addParameter("codiceEnte", ente);

            client.executeMethod(method);

            String content = method.getResponseBodyAsString().split("\n")[5];

            OMElement doc = getOmElement(content);

            String ticket = extractTicket(doc);

            if (method.getStatusCode() != 200) {
                throw new AuthenticationException(ticket);
            }

            TicketCipher tc = new TicketCipher();
            return tc.decryptTicket(ticket);

        } catch (Exception e) {
            return "LOGIN_ERROR";
        }
    }

    private String extractTicket(OMElement doc) throws JaxenException {

        // Tentiamo l'estrazione classica (qualsiasi login tranne per loginSSO di WSDOCER)
        String responseString = doc.getFirstElement().getText();

        // Se non va, estraiamo puntualmente
        if (StringUtils.isBlank(responseString)) {
            AXIOMXPath xpath = new AXIOMXPath("//*[local-name() = 'value' and ../*[local-name() = 'key' and text() = 'ticket'] ]");
            OMElement ticketNode = (OMElement) xpath.selectSingleNode(doc);

            if (ticketNode != null) {
                responseString = ticketNode.getText();
            }
        }
        return responseString;
    }

    private OMElement getOmElement(String content) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(
                new StringReader(content));
        StAXOMBuilder builder = new StAXOMBuilder(reader);
        return builder.getDocumentElement();
    }


}
