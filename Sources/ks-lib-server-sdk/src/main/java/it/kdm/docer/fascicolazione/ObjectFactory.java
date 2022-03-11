//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.01.17 at 07:25:38 PM CET 
//


package it.kdm.docer.fascicolazione;

import it.kdm.docer.fascicolazione.xsd.FascicolazioneException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.kdm.docer.fascicolazione package. 
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

    private final static QName _WSFascicolazioneExceptionWSFascicolazioneException_QNAME = new QName("http://fascicolazione.docer.kdm.it", "WSFascicolazioneException");
    private final static QName _LoginOnDemandUserId_QNAME = new QName("http://fascicolazione.docer.kdm.it", "userId");
    private final static QName _LoginOnDemandPassword_QNAME = new QName("http://fascicolazione.docer.kdm.it", "password");
    private final static QName _LoginOnDemandCodiceEnte_QNAME = new QName("http://fascicolazione.docer.kdm.it", "codiceEnte");
    private final static QName _LoginOnDemandCodiceAoo_QNAME = new QName("http://fascicolazione.docer.kdm.it", "codiceAoo");
    private final static QName _LoginOnDemandDocTicket_QNAME = new QName("http://fascicolazione.docer.kdm.it", "docTicket");
    private final static QName _LoginOnDemandResponseReturn_QNAME = new QName("http://fascicolazione.docer.kdm.it", "return");
    private final static QName _WSFascicolazioneFascicolazioneExceptionFascicolazioneException_QNAME = new QName("http://fascicolazione.docer.kdm.it", "FascicolazioneException");
    private final static QName _LogoutToken_QNAME = new QName("http://fascicolazione.docer.kdm.it", "token");
    private final static QName _FascicolaByIdDatiFascicolo_QNAME = new QName("http://fascicolazione.docer.kdm.it", "datiFascicolo");
    private final static QName _LoginSSOSaml_QNAME = new QName("http://fascicolazione.docer.kdm.it", "saml");
    private final static QName _WriteConfigXmlConfig_QNAME = new QName("http://fascicolazione.docer.kdm.it", "xmlConfig");
    private final static QName _ExceptionMessage_QNAME = new QName("http://fascicolazione.docer.kdm.it", "Message");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.kdm.docer.fascicolazione
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link WSFascicolazioneException }
     * 
     */
    public WSFascicolazioneException createWSFascicolazioneException() {
        return new WSFascicolazioneException();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link LoginOnDemand }
     * 
     */
    public LoginOnDemand createLoginOnDemand() {
        return new LoginOnDemand();
    }

    /**
     * Create an instance of {@link LoginOnDemandResponse }
     * 
     */
    public LoginOnDemandResponse createLoginOnDemandResponse() {
        return new LoginOnDemandResponse();
    }

    /**
     * Create an instance of {@link WSFascicolazioneFascicolazioneException }
     * 
     */
    public WSFascicolazioneFascicolazioneException createWSFascicolazioneFascicolazioneException() {
        return new WSFascicolazioneFascicolazioneException();
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
     * Create an instance of {@link FascicolaById }
     * 
     */
    public FascicolaById createFascicolaById() {
        return new FascicolaById();
    }

    /**
     * Create an instance of {@link FascicolaByIdResponse }
     * 
     */
    public FascicolaByIdResponse createFascicolaByIdResponse() {
        return new FascicolaByIdResponse();
    }

    /**
     * Create an instance of {@link CreaFascicolo }
     * 
     */
    public CreaFascicolo createCreaFascicolo() {
        return new CreaFascicolo();
    }

    /**
     * Create an instance of {@link CreaFascicoloResponse }
     * 
     */
    public CreaFascicoloResponse createCreaFascicoloResponse() {
        return new CreaFascicoloResponse();
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
     * Create an instance of {@link UpdateFascicolo }
     * 
     */
    public UpdateFascicolo createUpdateFascicolo() {
        return new UpdateFascicolo();
    }

    /**
     * Create an instance of {@link UpdateFascicoloResponse }
     * 
     */
    public UpdateFascicoloResponse createUpdateFascicoloResponse() {
        return new UpdateFascicoloResponse();
    }

    /**
     * Create an instance of {@link ChangeACLFascicolo }
     * 
     */
    public ChangeACLFascicolo createChangeACLFascicolo() {
        return new ChangeACLFascicolo();
    }

    /**
     * Create an instance of {@link ChangeACLFascicoloResponse }
     * 
     */
    public ChangeACLFascicoloResponse createChangeACLFascicoloResponse() {
        return new ChangeACLFascicoloResponse();
    }

    /**
     * Create an instance of {@link UpdateACLFascicolo }
     * 
     */
    public UpdateACLFascicolo createUpdateACLFascicolo() {
        return new UpdateACLFascicolo();
    }

    /**
     * Create an instance of {@link UpdateACLFascicoloResponse }
     * 
     */
    public UpdateACLFascicoloResponse createUpdateACLFascicoloResponse() {
        return new UpdateACLFascicoloResponse();
    }

    /**
     * Create an instance of {@link ChangeFascicoliById }
     * 
     */
    public ChangeFascicoliById createChangeFascicoliById() {
        return new ChangeFascicoliById();
    }

    /**
     * Create an instance of {@link ArrayOfKeyValuePair }
     * 
     */
    public ArrayOfKeyValuePair createArrayOfKeyValuePair() {
        return new ArrayOfKeyValuePair();
    }

    /**
     * Create an instance of {@link ChangeFascicoliByIdResponse }
     * 
     */
    public ChangeFascicoliByIdResponse createChangeFascicoliByIdResponse() {
        return new ChangeFascicoliByIdResponse();
    }

    /**
     * Create an instance of {@link ForzaNuovoFascicolo }
     * 
     */
    public ForzaNuovoFascicolo createForzaNuovoFascicolo() {
        return new ForzaNuovoFascicolo();
    }

    /**
     * Create an instance of {@link ForzaNuovoFascicoloResponse }
     * 
     */
    public ForzaNuovoFascicoloResponse createForzaNuovoFascicoloResponse() {
        return new ForzaNuovoFascicoloResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "WSFascicolazioneException", scope = WSFascicolazioneException.class)
    public JAXBElement<Exception> createWSFascicolazioneExceptionWSFascicolazioneException(Exception value) {
        return new JAXBElement<Exception>(_WSFascicolazioneExceptionWSFascicolazioneException_QNAME, Exception.class, WSFascicolazioneException.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "userId", scope = LoginOnDemand.class)
    public JAXBElement<String> createLoginOnDemandUserId(String value) {
        return new JAXBElement<String>(_LoginOnDemandUserId_QNAME, String.class, LoginOnDemand.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "password", scope = LoginOnDemand.class)
    public JAXBElement<String> createLoginOnDemandPassword(String value) {
        return new JAXBElement<String>(_LoginOnDemandPassword_QNAME, String.class, LoginOnDemand.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "codiceEnte", scope = LoginOnDemand.class)
    public JAXBElement<String> createLoginOnDemandCodiceEnte(String value) {
        return new JAXBElement<String>(_LoginOnDemandCodiceEnte_QNAME, String.class, LoginOnDemand.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "codiceAoo", scope = LoginOnDemand.class)
    public JAXBElement<String> createLoginOnDemandCodiceAoo(String value) {
        return new JAXBElement<String>(_LoginOnDemandCodiceAoo_QNAME, String.class, LoginOnDemand.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "docTicket", scope = LoginOnDemand.class)
    public JAXBElement<String> createLoginOnDemandDocTicket(String value) {
        return new JAXBElement<String>(_LoginOnDemandDocTicket_QNAME, String.class, LoginOnDemand.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "return", scope = LoginOnDemandResponse.class)
    public JAXBElement<String> createLoginOnDemandResponseReturn(String value) {
        return new JAXBElement<String>(_LoginOnDemandResponseReturn_QNAME, String.class, LoginOnDemandResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FascicolazioneException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FascicolazioneException }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "FascicolazioneException", scope = WSFascicolazioneFascicolazioneException.class)
    public JAXBElement<FascicolazioneException> createWSFascicolazioneFascicolazioneExceptionFascicolazioneException(FascicolazioneException value) {
        return new JAXBElement<FascicolazioneException>(_WSFascicolazioneFascicolazioneExceptionFascicolazioneException_QNAME, FascicolazioneException.class, WSFascicolazioneFascicolazioneException.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "userId", scope = Login.class)
    public JAXBElement<String> createLoginUserId(String value) {
        return new JAXBElement<String>(_LoginOnDemandUserId_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "password", scope = Login.class)
    public JAXBElement<String> createLoginPassword(String value) {
        return new JAXBElement<String>(_LoginOnDemandPassword_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "codiceEnte", scope = Login.class)
    public JAXBElement<String> createLoginCodiceEnte(String value) {
        return new JAXBElement<String>(_LoginOnDemandCodiceEnte_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "return", scope = LoginResponse.class)
    public JAXBElement<String> createLoginResponseReturn(String value) {
        return new JAXBElement<String>(_LoginOnDemandResponseReturn_QNAME, String.class, LoginResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "token", scope = Logout.class)
    public JAXBElement<String> createLogoutToken(String value) {
        return new JAXBElement<String>(_LogoutToken_QNAME, String.class, Logout.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "token", scope = FascicolaById.class)
    public JAXBElement<String> createFascicolaByIdToken(String value) {
        return new JAXBElement<String>(_LogoutToken_QNAME, String.class, FascicolaById.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "datiFascicolo", scope = FascicolaById.class)
    public JAXBElement<String> createFascicolaByIdDatiFascicolo(String value) {
        return new JAXBElement<String>(_FascicolaByIdDatiFascicolo_QNAME, String.class, FascicolaById.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "return", scope = FascicolaByIdResponse.class)
    public JAXBElement<String> createFascicolaByIdResponseReturn(String value) {
        return new JAXBElement<String>(_LoginOnDemandResponseReturn_QNAME, String.class, FascicolaByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "token", scope = CreaFascicolo.class)
    public JAXBElement<String> createCreaFascicoloToken(String value) {
        return new JAXBElement<String>(_LogoutToken_QNAME, String.class, CreaFascicolo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "return", scope = CreaFascicoloResponse.class)
    public JAXBElement<String> createCreaFascicoloResponseReturn(String value) {
        return new JAXBElement<String>(_LoginOnDemandResponseReturn_QNAME, String.class, CreaFascicoloResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "saml", scope = LoginSSO.class)
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
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "codiceEnte", scope = LoginSSO.class)
    public JAXBElement<String> createLoginSSOCodiceEnte(String value) {
        return new JAXBElement<String>(_LoginOnDemandCodiceEnte_QNAME, String.class, LoginSSO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "return", scope = LoginSSOResponse.class)
    public JAXBElement<String> createLoginSSOResponseReturn(String value) {
        return new JAXBElement<String>(_LoginOnDemandResponseReturn_QNAME, String.class, LoginSSOResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "token", scope = ReadConfig.class)
    public JAXBElement<String> createReadConfigToken(String value) {
        return new JAXBElement<String>(_LogoutToken_QNAME, String.class, ReadConfig.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "return", scope = ReadConfigResponse.class)
    public JAXBElement<String> createReadConfigResponseReturn(String value) {
        return new JAXBElement<String>(_LoginOnDemandResponseReturn_QNAME, String.class, ReadConfigResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "token", scope = WriteConfig.class)
    public JAXBElement<String> createWriteConfigToken(String value) {
        return new JAXBElement<String>(_LogoutToken_QNAME, String.class, WriteConfig.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "xmlConfig", scope = WriteConfig.class)
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
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "token", scope = UpdateFascicolo.class)
    public JAXBElement<String> createUpdateFascicoloToken(String value) {
        return new JAXBElement<String>(_LogoutToken_QNAME, String.class, UpdateFascicolo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "return", scope = UpdateFascicoloResponse.class)
    public JAXBElement<String> createUpdateFascicoloResponseReturn(String value) {
        return new JAXBElement<String>(_LoginOnDemandResponseReturn_QNAME, String.class, UpdateFascicoloResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "token", scope = ChangeACLFascicolo.class)
    public JAXBElement<String> createChangeACLFascicoloToken(String value) {
        return new JAXBElement<String>(_LogoutToken_QNAME, String.class, ChangeACLFascicolo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "token", scope = UpdateACLFascicolo.class)
    public JAXBElement<String> createUpdateACLFascicoloToken(String value) {
        return new JAXBElement<String>(_LogoutToken_QNAME, String.class, UpdateACLFascicolo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "token", scope = ChangeFascicoliById.class)
    public JAXBElement<String> createChangeFascicoliByIdToken(String value) {
        return new JAXBElement<String>(_LogoutToken_QNAME, String.class, ChangeFascicoliById.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "token", scope = ForzaNuovoFascicolo.class)
    public JAXBElement<String> createForzaNuovoFascicoloToken(String value) {
        return new JAXBElement<String>(_LogoutToken_QNAME, String.class, ForzaNuovoFascicolo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "return", scope = ForzaNuovoFascicoloResponse.class)
    public JAXBElement<String> createForzaNuovoFascicoloResponseReturn(String value) {
        return new JAXBElement<String>(_LoginOnDemandResponseReturn_QNAME, String.class, ForzaNuovoFascicoloResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://fascicolazione.docer.kdm.it", name = "Message", scope = Exception.class)
    public JAXBElement<String> createExceptionMessage(String value) {
        return new JAXBElement<String>(_ExceptionMessage_QNAME, String.class, Exception.class, value);
    }

}