package it.kdm.docer.core.authentication.providers;

import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.core.authentication.BaseAuthProvider;
import it.kdm.docer.core.authentication.bean.LoginInfo;
import it.kdm.docer.core.authentication.bean.SSOLoginCredential;
import it.kdm.docer.core.authentication.providers.sso.SSOTicketCache;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SSOProvider extends BaseAuthProvider {

    Logger logger = org.slf4j.LoggerFactory.getLogger(SSOProvider.class);

    public SSOProvider() {
        setServiceName("SSO");
    }

    @Override
    public void setServiceName(String service) {
        super.setServiceName("SSO");
    }

    public SSOLoginCredential loginSSO(String saml, String codiceEnte, String application) throws AuthenticationException {
        //logger.debug(String.format("login(%s,%s)", username, password));

        try {

            DocerServicesStub.LoginSSO loginSSO = new DocerServicesStub.LoginSSO();

            loginSSO.setCodiceEnte(codiceEnte);
            loginSSO.setSaml(saml);

            DocerServicesStub.LoginSSOResponse loginSSOResponse = ClientManager.INSTANCE.getDocerServicesClient().loginSSO(loginSSO);
            DocerServicesStub.KeyValuePair[] kvp = loginSSOResponse.get_return();

            return getSsoLoginCredential(saml, kvp);

        } catch (Exception e) {
            throw new AuthenticationException("Saml non valido");
        }
    }

    private SSOLoginCredential getSsoLoginCredential(String saml, DocerServicesStub.KeyValuePair[] kvp) {

        String newSaml = getKvp("saml", kvp);
        String ticket = getKvp("ticket", kvp);
        String user = getKvp("uid", kvp);
        String groups = getKvp("groups", kvp);

        SSOLoginCredential ret = new SSOLoginCredential();

        if (StringUtils.isBlank(newSaml)) {
            ret.setSaml(newSaml);
        } else {
            ret.setSaml(saml);
        }

        ret.setOriginalSaml(saml);
        ret.setTicket(ticket);
        ret.setRealUser(user);
        ret.setGroups(groups);

        return ret;
    }

    private String getKvp(String key, DocerServicesStub.KeyValuePair[] kvp) {

        for (DocerServicesStub.KeyValuePair p : kvp) {
            if (p.getKey().equalsIgnoreCase(key)) {
                return p.getValue();
            }
        }

        return null;
    }

    @Override
    public LoginInfo login(String username, String password, String codiceEnte, String application) throws AuthenticationException {
        try {
            logger.debug(String.format("login(%s,%s)", username, "******"));

            HttpClient client = new HttpClient();

            PostMethod method = new PostMethod(this.getEpr("login"));

            method.addParameter("userId", username);
            method.addParameter("password", password);
            method.addParameter("codiceEnte", codiceEnte);

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

            LoginInfo info = new LoginInfo(username, password, codiceEnte, application);
            info.setTicket(responseString);
            return info;

        } catch (IllegalArgumentException e) {
            throw new AuthenticationException(e.getMessage(), e);
        } catch (HttpException e) {
            throw new AuthenticationException(e.getMessage(), e);
        } catch (IOException e) {
            throw new AuthenticationException(e.getMessage(), e);
        } catch (FactoryConfigurationError e) {
            throw new AuthenticationException(e.getMessage(), e);
        } catch (XMLStreamException e) {
            throw new AuthenticationException(e.getMessage(), e);
        }
    }

    @Override
    public boolean verifyTicket(String token, String ticket) throws Exception {
        logger.debug(String.format("verifyTicket(%s)", ticket));
        return SSOTicketCache.getInstance().contains(ticket);
    }

    @Override
    public String renewTicket(String token, String ticket) throws Exception {
        logger.debug(String.format("renewTicket(%s)", ticket));

        return ticket;
    }

    @Override
    public Map<String, String> getUserInfo(String token, String uid) throws Exception {

        logger.debug(String.format("getUserInfo(%s,%s)", token, uid));

        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(this.getEpr("getUser"));

        method.addParameter("token", token);
        method.addParameter("userId", uid);

        client.executeMethod(method);

        String content = method.getResponseBodyAsString().split("\n")[5];
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(
                new StringReader(content));
//					method.getResponseBodyAsStream());
        StAXOMBuilder builder = new StAXOMBuilder(reader);
//				new XOPAwareStAXOMBuilder(factory, parser, attachments)
        OMElement doc = builder.getDocumentElement();

        if (method.getStatusCode() != 200) {
            OMElement responseElement = doc.getFirstElement();
            throw new Exception(responseElement.getText());
        }

        Iterator<?> nodes = doc.getChildElements();

        Map<String, String> ret = new HashMap<String, String>();
        while (nodes.hasNext()) {
            OMElement elem = (OMElement) nodes.next();
            Iterator<?> childNodes = elem.getChildElements();
            String key = null, value = null;
            while (childNodes.hasNext()) {
                OMElement child = (OMElement) childNodes.next();
                if (child.getLocalName().equals("key")) {
                    key = child.getText();
                } else if (child.getLocalName().equals("value")) {
                    value = child.getText();
                }
            }

            if (key == null) {
                //TODO: throw exception?
                throw new KeyException("Malformed service response");
            }

            ret.put(key, value);
        }

        return ret;
    }

    @Override
    public String[] getUserGroups(String token) throws Exception {
        String userid = Utils.extractTokenKey(token, "uid");

        DocerServicesStub.GetGroupsOfUser group = new DocerServicesStub.GetGroupsOfUser();
        String callType = "calltype:internal|";
//        String newToken = token + callType;
        group.setToken(token);
        group.setUserId(userid);

        DocerServicesStub.GetGroupsOfUserResponse resp = ClientManager.INSTANCE.getDocerServicesClient().getGroupsOfUser(group);
        String[] groups = resp.get_return();

        return groups;
    }

    @Override
    public String getEnteDescription(String token) throws Exception {
        DocerServicesStub.GetEnte req = new DocerServicesStub.GetEnte();
        req.setToken(token);
        req.setCodiceEnte(Utils.extractTokenKey(token, "ente"));

        DocerServicesStub.GetEnteResponse resp = ClientManager.INSTANCE.getDocerServicesClient().getEnte(req);

        for (DocerServicesStub.KeyValuePair pair : resp.get_return()) {
            if (pair.getKey().equalsIgnoreCase("DES_ENTE")) {
                return pair.getValue();
            }
        }

        return "";
    }
}
