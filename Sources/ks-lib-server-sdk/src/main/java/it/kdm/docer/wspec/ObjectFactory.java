//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.11.16 at 10:59:54 AM CET 
//


package it.kdm.docer.wspec;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.kdm.docer.pec package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _WSPECPECExceptionPECException_QNAME = new QName("http://PEC.docer.kdm.it", "PECException");
    private final static QName _InvioPECToken_QNAME = new QName("http://PEC.docer.kdm.it", "token");
    private final static QName _InvioPECDatiPec_QNAME = new QName("http://PEC.docer.kdm.it", "datiPec");
    private final static QName _InvioPECResponseReturn_QNAME = new QName("http://PEC.docer.kdm.it", "return");
    private final static QName _LoginSSOSaml_QNAME = new QName("http://PEC.docer.kdm.it", "saml");
    private final static QName _LoginSSOCodiceEnte_QNAME = new QName("http://PEC.docer.kdm.it", "codiceEnte");
    private final static QName _WriteConfigXmlConfig_QNAME = new QName("http://PEC.docer.kdm.it", "xmlConfig");
    private final static QName _LoginUserId_QNAME = new QName("http://PEC.docer.kdm.it", "userId");
    private final static QName _LoginPassword_QNAME = new QName("http://PEC.docer.kdm.it", "password");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.kdm.docer.pec
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link WSPECPECException }
     * 
     */
    public WSPECPECException createWSPECPECException() {
        return new WSPECPECException();
    }

    /**
     * Create an instance of {@link PECException }
     * 
     */
    public PECException createPECException() {
        return new PECException();
    }

    /**
     * Create an instance of {@link InvioPEC }
     * 
     */
    public InvioPEC createInvioPEC() {
        return new InvioPEC();
    }

    /**
     * Create an instance of {@link InvioPECResponse }
     * 
     */
    public InvioPECResponse createInvioPECResponse() {
        return new InvioPECResponse();
    }

    /**
     * Create an instance of {@link LoginSSO }
     * 
     */
    public LoginSSO createLoginSSO() {
        return new LoginSSO();
    }

    /**
     * Create an instance of {@link LoginSSOResponse }
     * 
     */
    public LoginSSOResponse createLoginSSOResponse() {
        return new LoginSSOResponse();
    }

    /**
     * Create an instance of {@link WriteConfig }
     * 
     */
    public WriteConfig createWriteConfig() {
        return new WriteConfig();
    }

    /**
     * Create an instance of {@link WriteConfigResponse }
     * 
     */
    public WriteConfigResponse createWriteConfigResponse() {
        return new WriteConfigResponse();
    }

    /**
     * Create an instance of {@link ReadConfig }
     * 
     */
    public ReadConfig createReadConfig() {
        return new ReadConfig();
    }

    /**
     * Create an instance of {@link ReadConfigResponse }
     * 
     */
    public ReadConfigResponse createReadConfigResponse() {
        return new ReadConfigResponse();
    }

    /**
     * Create an instance of {@link Login }
     * 
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link Logout }
     * 
     */
    public Logout createLogout() {
        return new Logout();
    }

    /**
     * Create an instance of {@link LogoutResponse }
     * 
     */
    public LogoutResponse createLogoutResponse() {
        return new LogoutResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PECException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PECException }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "PECException", scope = WSPECPECException.class)
    public JAXBElement<PECException> createWSPECPECExceptionPECException(PECException value) {
        return new JAXBElement<PECException>(_WSPECPECExceptionPECException_QNAME, PECException.class, WSPECPECException.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "token", scope = InvioPEC.class)
    public JAXBElement<String> createInvioPECToken(String value) {
        return new JAXBElement<String>(_InvioPECToken_QNAME, String.class, InvioPEC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "datiPec", scope = InvioPEC.class)
    public JAXBElement<String> createInvioPECDatiPec(String value) {
        return new JAXBElement<String>(_InvioPECDatiPec_QNAME, String.class, InvioPEC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "return", scope = InvioPECResponse.class)
    public JAXBElement<String> createInvioPECResponseReturn(String value) {
        return new JAXBElement<String>(_InvioPECResponseReturn_QNAME, String.class, InvioPECResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "saml", scope = LoginSSO.class)
    public JAXBElement<String> createLoginSSOSaml(String value) {
        return new JAXBElement<String>(_LoginSSOSaml_QNAME, String.class, LoginSSO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "codiceEnte", scope = LoginSSO.class)
    public JAXBElement<String> createLoginSSOCodiceEnte(String value) {
        return new JAXBElement<String>(_LoginSSOCodiceEnte_QNAME, String.class, LoginSSO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "return", scope = LoginSSOResponse.class)
    public JAXBElement<String> createLoginSSOResponseReturn(String value) {
        return new JAXBElement<String>(_InvioPECResponseReturn_QNAME, String.class, LoginSSOResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "token", scope = WriteConfig.class)
    public JAXBElement<String> createWriteConfigToken(String value) {
        return new JAXBElement<String>(_InvioPECToken_QNAME, String.class, WriteConfig.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "xmlConfig", scope = WriteConfig.class)
    public JAXBElement<String> createWriteConfigXmlConfig(String value) {
        return new JAXBElement<String>(_WriteConfigXmlConfig_QNAME, String.class, WriteConfig.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "token", scope = ReadConfig.class)
    public JAXBElement<String> createReadConfigToken(String value) {
        return new JAXBElement<String>(_InvioPECToken_QNAME, String.class, ReadConfig.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "return", scope = ReadConfigResponse.class)
    public JAXBElement<String> createReadConfigResponseReturn(String value) {
        return new JAXBElement<String>(_InvioPECResponseReturn_QNAME, String.class, ReadConfigResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "userId", scope = Login.class)
    public JAXBElement<String> createLoginUserId(String value) {
        return new JAXBElement<String>(_LoginUserId_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "password", scope = Login.class)
    public JAXBElement<String> createLoginPassword(String value) {
        return new JAXBElement<String>(_LoginPassword_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "codiceEnte", scope = Login.class)
    public JAXBElement<String> createLoginCodiceEnte(String value) {
        return new JAXBElement<String>(_LoginSSOCodiceEnte_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "return", scope = LoginResponse.class)
    public JAXBElement<String> createLoginResponseReturn(String value) {
        return new JAXBElement<String>(_InvioPECResponseReturn_QNAME, String.class, LoginResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://PEC.docer.kdm.it", name = "token", scope = Logout.class)
    public JAXBElement<String> createLogoutToken(String value) {
        return new JAXBElement<String>(_InvioPECToken_QNAME, String.class, Logout.class, value);
    }

}
