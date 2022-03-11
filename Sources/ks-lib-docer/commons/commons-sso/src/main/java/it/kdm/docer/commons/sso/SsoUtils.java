package it.kdm.docer.commons.sso;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.impl.AssertionMarshaller;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.signature.Signature;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import it.cefriel.icar.inf3.ICARConstants;
import it.cefriel.icar.inf3.attrmgr.beans.AttributeBean;
import it.cefriel.icar.inf3.web.beans.AuthenticationSessionBean;
import it.emilia_romagna.regione.samlutil.SamlUtil;
import it.emilia_romagna.regione.samlutil.samlwriter.SamlWriter;
import it.kdm.docer.commons.configuration.ConfigurationLoadingException;
import it.kdm.docer.commons.configuration.ConfigurationUtils;

/**
 * Created by Łukasz Kwasek on 08/01/15.
 */
public class SsoUtils extends SamlUtil {

    public static final String USER_ID = "userid";
    public static final String REAL_USER_ID = "realUserId";
    public static final String CODICE_ENTE = "codiceEnte";
    public static final String CODICE_FISCALE = "CodiceFiscale";
    public static final String CLIENT_ID = "clientId";
    public static final String EMAIL = "emailAddressPersonale";
    public static final String NESTED_ASSERTION = "nestedAssertion";

    private static XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

    private static AssertionMarshaller marshaller = new AssertionMarshaller();

    public static Assertion createBlankAssertion() {

        SAMLObjectBuilder assertionBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
        return (Assertion) assertionBuilder.buildObject();
    }

    public static Assertion createAssertion() throws ConfigurationException, SsoException {

        SAMLObjectBuilder assertionBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
        Assertion assertion = (Assertion) assertionBuilder.buildObject();

        signSamlAsserion(assertion);

        return assertion;
    }

    public static Assertion createAssertion(AuthenticationSessionBean userInfo) throws ConfigurationException, SsoException {

        SAMLObjectBuilder assertionBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
        Assertion assertion = (Assertion) assertionBuilder.buildObject();

        try {
            addAttribute(assertion, CLIENT_ID, getMyAlias());
        } catch (org.apache.commons.configuration.ConfigurationException e) {
            throw new ConfigurationException(e);
        }

        addUserInfo(assertion, userInfo);

        return assertion;
    }

    public static Assertion parseAssertion(String xml) throws IllegalArgumentException {
        try {
            return SamlWriter.parseAssertion(xml);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Signature parseSignature(String xml) {
        try {
            ByteArrayInputStream b = new ByteArrayInputStream(xml.getBytes());
            return (Signature) SamlWriter.parseXmlObject(b);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void signSamlAsserion(Assertion assertion) throws SsoException {
        try {

            KeyStore ks = loadKeystore();
            Signature s = getSignature(ks);

            signSamlAsserion(assertion, s);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SsoException(e);
        }
    }

    public static Signature getSignature(KeyStore ks) throws org.opensaml.xml.security.SecurityException, org.apache.commons.configuration.ConfigurationException {

        String xfalias = getMyAlias();
        String xfpass = getMyPassword();

        return SsoUtils.getSignature(ks, xfalias, xfpass);
    }

    private static String getMyPassword() throws org.apache.commons.configuration.ConfigurationException {
        return getPropertiesConfiguration().getString("sso.keystore.my.password");
    }

    public static String getMyAlias() throws org.apache.commons.configuration.ConfigurationException {
        return getPropertiesConfiguration().getString("sso.keystore.my.alias");
    }

    public static KeyStore loadKeystore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, org.apache.commons.configuration.ConfigurationException {

        PropertiesConfiguration config = getPropertiesConfiguration();

        String kspath = config.getString("sso.keystore.path");
        String kspass = config.getString("sso.keystore.passowrd");

        return SsoUtils.loadKeystore(kspath, kspass);
    }

    private static PropertiesConfiguration getPropertiesConfiguration() throws org.apache.commons.configuration.ConfigurationException {
        
    	try {
    		File ssoCfg = ConfigurationUtils.loadResourceFile("sso.properties");
    		return new PropertiesConfiguration(ssoCfg);
    	} catch (ConfigurationLoadingException e) {
			throw new org.apache.commons.configuration.ConfigurationException(e);
		}
    }

    public static void addUserInfo(Assertion assertion, AuthenticationSessionBean userInfo) {

        addUserID(assertion, userInfo);
        addNestedAssertion(assertion, userInfo);

        Map<String, Object> attributeBeanMap = userInfo.getAttributesMap();
        for (String key : attributeBeanMap.keySet()) {
            Object ob = attributeBeanMap.get(key);

            if (ob instanceof AttributeBean) {
                AttributeBean ab = (AttributeBean) ob;
                String name = ab.getName();
                String[] values = ab.getValues();

                addAttribute(assertion, name, values);
            } else if (ob instanceof Collection) {
                Collection c = (Collection) ob;
                Object[] arr = c.toArray();

                String[] values = Arrays.copyOf(arr, arr.length, String[].class);
                addAttribute(assertion, key, values);
            }
        }
    }

    public static void addNestedAssertion(Assertion assertion, AuthenticationSessionBean userInfo) {
        addAttribute(assertion, NESTED_ASSERTION, userInfo.getAuthenticationAssertion());
    }

    public static void addNestedAssertion(Assertion assertion, String assertionToNest) {
        addAttribute(assertion, NESTED_ASSERTION, assertionToNest);
    }

    public static void addNestedAssertion(Assertion assertion, Assertion assertionToNest) throws MarshallingException {
        addAttribute(assertion, NESTED_ASSERTION, assertionToString(assertionToNest));
    }

    public static void addUserID(Assertion assertion, String userID) {
        addAttribute(assertion, USER_ID, userID);
    }

    public static void addUserID(Assertion assertion, AuthenticationSessionBean userInfo) {
        addAttribute(assertion, USER_ID, userInfo.getUserID());
    }

    public static String getUserID(Assertion assertion) {
        return getAssertionAttributeByName(assertion, USER_ID);
    }

    public static void addAttribute(Assertion assertion, String name, String[] values) {

        for (String v : values) {

            SAMLObjectBuilder assertionBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
            AttributeStatement as = (AttributeStatement) assertionBuilder.buildObject();

            addAttribute(as, name, v);

            assertion.getAttributeStatements().add(as);

        }

    }

    private static Pattern signatureRegex = Pattern.compile("(<[^:]*:Signature[^>]*>.*</[^:]*:Signature>)");

    public static Assertion cloneSignature(Assertion from, Assertion to) throws MarshallingException {
        String strFrom = assertionToString(from);
        String strTo = assertionToString(to);

        Matcher mFrom = signatureRegex.matcher(strFrom);
        Matcher mTo = signatureRegex.matcher(strTo);
        if (mFrom.matches() && mTo.matches()) {
            String strSignFrom = mFrom.group(1);
            String newAssertion = mTo.replaceFirst(strSignFrom);

            return parseAssertion(newAssertion);
        } else {
            return to;
        }
    }

    public static Assertion cloneAssertion(Assertion assertion) throws MarshallingException {
        return parseAssertion(assertionToString(assertion));
    }

    public static Signature cloneSignature(Signature signature) throws MarshallingException {
        return parseSignature(signatureToString(signature));
    }

    public static void addAttribute(Assertion assertion, String name, String value) {
        addAttribute(assertion, name, new String[]{value});
    }

    public static String assertionToString(Assertion assertion) throws MarshallingException {
        return SamlWriter.serializeAssertion(assertion);
    }

    public static String signatureToString(Signature signature) {
        Document doc = SamlWriter.asDOMDocument(signature);
        return new String(toByteArray(doc));
    }

    public static AuthenticationSessionBean getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map serviceContextMap = (Map) session.getAttribute(ICARConstants.SERVICE_CONTEXT_MAP);
        String serviceURLPrefix = (String) session.getAttribute(ICARConstants.SERVICE_URL_PREFIX);

        if (serviceContextMap != null) {
            return (AuthenticationSessionBean) serviceContextMap.get(serviceURLPrefix);
        } else {
            return null;
        }

    }

    public static boolean isAuthenticated(HttpServletRequest request) {
        return getUserInfo(request) != null;
    }

    public static Assertion createAssertion(AuthenticationSessionBean userInfo, String codiceEnte) throws SsoException, ConfigurationException {

        Assertion as = createAssertion(userInfo);

        if (StringUtils.isNotBlank(codiceEnte)) {
            addAttribute(as, CODICE_ENTE, codiceEnte);
        }

        signSamlAsserion(as);

        return as;
    }

    public static String cleanSaml(String saml) {
        BufferedReader reader = new BufferedReader(new StringReader(saml));
        StringBuffer result = new StringBuffer();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line.trim());
            }

            /*
            Il parser del middleware engineering è molto "sensibile" alla formattazione del xml. Anche se si passa un
            xml ben formattato, c'è il rischio che il modulus della chiave rsa venga messo su un'unica linea. Questo
            romperebbe il parser nel middelware.
             */

            return fixModulus(result.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String fixModulus(String saml) {

        try {
            Assertion ass = parseAssertion(saml);
            String modulus = ass.getSignature()
                    .getKeyInfo()
                    .getKeyValues()
                    .get(0)
                    .getRSAKeyValue()
                    .getModulus()
                    .getDOM()
                    .getTextContent();

            Iterable<String> itModulus = Splitter.fixedLength(76).split(modulus);
            String newModulus = Joiner.on("\n").join(itModulus);

            return saml.replace(modulus, newModulus);
        } catch (RuntimeException e) {
            return saml;
        }
    }

    public static Assertion getNestedAssertion(Assertion assertion) {
        String saml = getAssertionAttributeByName(assertion, NESTED_ASSERTION);

        if (StringUtils.isNotBlank(saml)) {
            return parseAssertion(saml);
        } else {
            throw new IllegalArgumentException("Asserzione annidata non trovata");
        }
    }

    private static byte[] toByteArray(Node doc) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            TransformerFactory.newInstance().newTransformer()
                    .transform(new DOMSource(doc), new StreamResult(buffer));
            byte[] rawResult = buffer.toByteArray();
            buffer.close();
            return rawResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
