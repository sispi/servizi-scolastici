//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.07 at 06:06:43 PM CEST 
//


package it.filippetti.ks.api.payment.pmpay.authpa.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the authpa package. 
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

    private final static QName _WSLOGINREQUEST_QNAME = new QName("urn:authPA", "WSLOGIN_REQUEST");
    private final static QName _WSLOGINRESPONSE_QNAME = new QName("urn:authPA", "WSLOGIN_RESPONSE");
    private final static QName _RICHIEDIIUVREQUEST_QNAME = new QName("urn:authPA", "RICHIEDI_IUV_REQUEST");
    private final static QName _RICHIEDIIUVRESPONSE_QNAME = new QName("urn:authPA", "RICHIEDI_IUV_RESPONSE");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: authpa
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link WSLOGINRESPONSE }
     * 
     */
    public WSLOGINRESPONSE createWSLOGINRESPONSE() {
        return new WSLOGINRESPONSE();
    }

    /**
     * Create an instance of {@link RICHIEDIIUVREQUEST }
     * 
     */
    public RICHIEDIIUVREQUEST createRICHIEDIIUVREQUEST() {
        return new RICHIEDIIUVREQUEST();
    }

    /**
     * Create an instance of {@link RICHIEDIIUVRESPONSE }
     * 
     */
    public RICHIEDIIUVRESPONSE createRICHIEDIIUVRESPONSE() {
        return new RICHIEDIIUVRESPONSE();
    }

    /**
     * Create an instance of {@link WSLOGINREQUEST }
     * 
     */
    public WSLOGINREQUEST createWSLOGINREQUEST() {
        return new WSLOGINREQUEST();
    }

    /**
     * Create an instance of {@link RichiestaIUV }
     * 
     */
    public RichiestaIUV createRichiestaIUV() {
        return new RichiestaIUV();
    }

    /**
     * Create an instance of {@link HeaderRichiestaIUV }
     * 
     */
    public HeaderRichiestaIUV createHeaderRichiestaIUV() {
        return new HeaderRichiestaIUV();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WSLOGINREQUEST }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:authPA", name = "WSLOGIN_REQUEST")
    public JAXBElement<WSLOGINREQUEST> createWSLOGINREQUEST(WSLOGINREQUEST value) {
        return new JAXBElement<WSLOGINREQUEST>(_WSLOGINREQUEST_QNAME, WSLOGINREQUEST.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WSLOGINRESPONSE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:authPA", name = "WSLOGIN_RESPONSE")
    public JAXBElement<WSLOGINRESPONSE> createWSLOGINRESPONSE(WSLOGINRESPONSE value) {
        return new JAXBElement<WSLOGINRESPONSE>(_WSLOGINRESPONSE_QNAME, WSLOGINRESPONSE.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RICHIEDIIUVREQUEST }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:authPA", name = "RICHIEDI_IUV_REQUEST")
    public JAXBElement<RICHIEDIIUVREQUEST> createRICHIEDIIUVREQUEST(RICHIEDIIUVREQUEST value) {
        return new JAXBElement<RICHIEDIIUVREQUEST>(_RICHIEDIIUVREQUEST_QNAME, RICHIEDIIUVREQUEST.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RICHIEDIIUVRESPONSE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:authPA", name = "RICHIEDI_IUV_RESPONSE")
    public JAXBElement<RICHIEDIIUVRESPONSE> createRICHIEDIIUVRESPONSE(RICHIEDIIUVRESPONSE value) {
        return new JAXBElement<RICHIEDIIUVRESPONSE>(_RICHIEDIIUVRESPONSE_QNAME, RICHIEDIIUVRESPONSE.class, null, value);
    }

}