package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the it.pianetacloud.idea.web.bnl.pay package.
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

    private final static QName _PaymentSelectorResponse_QNAME = new QName("http://services.api.web.cg.igfs.apps.netsw.it/", "PaymentSelectorResponse");
    private final static QName _PaymentVerifyResponse_QNAME = new QName("http://services.api.web.cg.igfs.apps.netsw.it/", "PaymentVerifyResponse");
    private final static QName _PaymentSelectorRequest_QNAME = new QName("http://services.api.web.cg.igfs.apps.netsw.it/", "PaymentSelectorRequest");
    private final static QName _PaymentInitRequest_QNAME = new QName("http://services.api.web.cg.igfs.apps.netsw.it/", "PaymentInitRequest");
    private final static QName _PaymentVerifyRequest_QNAME = new QName("http://services.api.web.cg.igfs.apps.netsw.it/", "PaymentVerifyRequest");
    private final static QName _PaymentInitResponse_QNAME = new QName("http://services.api.web.cg.igfs.apps.netsw.it/", "PaymentInitResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.pianetacloud.idea.web.bnl.pay
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PaymentInitResponse }
     *
     */
    public PaymentInitResponse createPaymentInitResponse() {
        return new PaymentInitResponse();
    }

    /**
     * Create an instance of {@link PaymentVerifyRequest }
     *
     */
    public PaymentVerifyRequest createPaymentVerifyRequest() {
        return new PaymentVerifyRequest();
    }

    /**
     * Create an instance of {@link InitResponse }
     *
     */
    public InitResponse createInitResponse() {
        return new InitResponse();
    }

    /**
     * Create an instance of {@link PaymentInitRequest }
     *
     */
    public PaymentInitRequest createPaymentInitRequest() {
        return new PaymentInitRequest();
    }

    /**
     * Create an instance of {@link Verify }
     *
     */
    public Verify createVerify() {
        return new Verify();
    }

    /**
     * Create an instance of {@link Init }
     *
     */
    public Init createInit() {
        return new Init();
    }

    /**
     * Create an instance of {@link PaymentVerifyResponse }
     *
     */
    public PaymentVerifyResponse createPaymentVerifyResponse() {
        return new PaymentVerifyResponse();
    }

    /**
     * Create an instance of {@link VerifyResponse }
     *
     */
    public VerifyResponse createVerifyResponse() {
        return new VerifyResponse();
    }

    /**
     * Create an instance of {@link PaymentSelectorRequest }
     *
     */
    public PaymentSelectorRequest createPaymentSelectorRequest() {
        return new PaymentSelectorRequest();
    }

    /**
     * Create an instance of {@link Selector }
     *
     */
    public Selector createSelector() {
        return new Selector();
    }

    /**
     * Create an instance of {@link SelectorResponse }
     *
     */
    public SelectorResponse createSelectorResponse() {
        return new SelectorResponse();
    }

    /**
     * Create an instance of {@link PaymentSelectorResponse }
     *
     */
    public PaymentSelectorResponse createPaymentSelectorResponse() {
        return new PaymentSelectorResponse();
    }

    /**
     * Create an instance of {@link Level3Info }
     *
     */
    public Level3Info createLevel3Info() {
        return new Level3Info();
    }

    /**
     * Create an instance of {@link Entry }
     *
     */
    public Entry createEntry() {
        return new Entry();
    }

    /**
     * Create an instance of {@link Level3InfoProduct }
     *
     */
    public Level3InfoProduct createLevel3InfoProduct() {
        return new Level3InfoProduct();
    }

    /**
     * Create an instance of {@link InitTerminalInfo }
     *
     */
    public InitTerminalInfo createInitTerminalInfo() {
        return new InitTerminalInfo();
    }

    /**
     * Create an instance of {@link SelectorTerminalInfo }
     *
     */
    public SelectorTerminalInfo createSelectorTerminalInfo() {
        return new SelectorTerminalInfo();
    }

    /**
     * Create an instance of {@link MandateInfo }
     *
     */
    public MandateInfo createMandateInfo() {
        return new MandateInfo();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link PaymentSelectorResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://services.api.web.cg.igfs.apps.netsw.it/", name = "PaymentSelectorResponse")
    public JAXBElement<PaymentSelectorResponse> createPaymentSelectorResponse(PaymentSelectorResponse value) {
        return new JAXBElement<PaymentSelectorResponse>(_PaymentSelectorResponse_QNAME, PaymentSelectorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link PaymentVerifyResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://services.api.web.cg.igfs.apps.netsw.it/", name = "PaymentVerifyResponse")
    public JAXBElement<PaymentVerifyResponse> createPaymentVerifyResponse(PaymentVerifyResponse value) {
        return new JAXBElement<PaymentVerifyResponse>(_PaymentVerifyResponse_QNAME, PaymentVerifyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link PaymentSelectorRequest }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://services.api.web.cg.igfs.apps.netsw.it/", name = "PaymentSelectorRequest")
    public JAXBElement<PaymentSelectorRequest> createPaymentSelectorRequest(PaymentSelectorRequest value) {
        return new JAXBElement<PaymentSelectorRequest>(_PaymentSelectorRequest_QNAME, PaymentSelectorRequest.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link PaymentInitRequest }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://services.api.web.cg.igfs.apps.netsw.it/", name = "PaymentInitRequest")
    public JAXBElement<PaymentInitRequest> createPaymentInitRequest(PaymentInitRequest value) {
        return new JAXBElement<PaymentInitRequest>(_PaymentInitRequest_QNAME, PaymentInitRequest.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link PaymentVerifyRequest }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://services.api.web.cg.igfs.apps.netsw.it/", name = "PaymentVerifyRequest")
    public JAXBElement<PaymentVerifyRequest> createPaymentVerifyRequest(PaymentVerifyRequest value) {
        return new JAXBElement<PaymentVerifyRequest>(_PaymentVerifyRequest_QNAME, PaymentVerifyRequest.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link PaymentInitResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://services.api.web.cg.igfs.apps.netsw.it/", name = "PaymentInitResponse")
    public JAXBElement<PaymentInitResponse> createPaymentInitResponse(PaymentInitResponse value) {
        return new JAXBElement<PaymentInitResponse>(_PaymentInitResponse_QNAME, PaymentInitResponse.class, null, value);
    }

}
