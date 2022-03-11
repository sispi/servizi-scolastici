package it.kdm.docer.core.authentication.loginondemand;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.core.authentication.AuthenticationCache;
import it.kdm.docer.core.authentication.bean.LoginInfo;
import it.kdm.docer.core.authentication.bean.WSTicketAuthInfo;
import it.kdm.docer.core.configuration.ConfigurationManager;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.Map;

/**
 * Created by Łukasz Kwasek on 16/12/14.
 */
public class StandardLoginOnDemand extends AbstractLoginOnDemand {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(StandardLoginOnDemand.class);

    public StandardLoginOnDemand() throws Exception {
        super();
    }

    public String login(String token, String serviceName, String serviceUrl) throws Exception {

        // Estrai dalla cache i dati utente
        LoginInfo userInfo = AuthenticationCache.getInstance().getLoginInfo(token);

        if (userInfo == null) {
            throw new AuthenticationException("Token non valido in cache.");
        }

        String userName = Utils.getUserFromToken(token);
        String password = userInfo.getPass();
        String ente = userInfo.getEnte();
        String application = userInfo.getApplication();

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
            wsticket = serviceLogin(serviceUrl, userName, password, ente, application);
            AuthenticationCache.getInstance().putWSTicket(wsTicketAuthInfo, wsticket);
        }

        String expirationTokenValue = getExpirationTokenValue();
        String encTicket = tc.encryptTicket(wsticket, expirationTokenValue);

        return encTicket;
    }

    private String serviceLogin(String url, String username, String password, String ente, String application) throws AuthenticationException {
        try {
            logger.debug(String.format("doServicelogin(%s,%s,%s)", username, "******", ente));

            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(getEpr(url, "login"));

            method.addParameter("userId", username);
            method.addParameter("password", password);
            method.addParameter("codiceEnte", ente);

            client.executeMethod(method);

            String content = method.getResponseBodyAsString().split("\n")[5];
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(
                    new StringReader(content));
            StAXOMBuilder builder = new StAXOMBuilder(reader);
            OMElement doc = builder.getDocumentElement();

            String responseString = doc.getFirstElement().getText();

            if (method.getStatusCode() != 200) {
                throw new AuthenticationException(responseString);
            }

            TicketCipher tc = new TicketCipher();

            return tc.decryptTicket(responseString);

        } catch (Exception e) {
            return "LOGIN_ERROR";
        }
    }


}
