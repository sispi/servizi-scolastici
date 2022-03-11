
package it.filippetti.ks.api.payment.pmpay.paypa;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


//wsdlLocation = "file:/home/raffaele/Documenti/Filippetti/PagoPA/PayPA.wsdl"
/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01
 * Generated source version: 2.2
 *
 */
@WebServiceClient(name = "PayPA", targetNamespace = "http://pmpay.it/ws/payPA/", wsdlLocation = "https://service.pmpay.it/payPA/services/PayPA?wsdl")
public class PayPA_Service
    extends Service
{

    private final static URL PAYPA_WSDL_LOCATION;
    private final static WebServiceException PAYPA_EXCEPTION;
    private final static QName PAYPA_QNAME = new QName("http://pmpay.it/ws/payPA/", "PayPA");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("https://service.pmpay.it/payPA/services/PayPA?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        PAYPA_WSDL_LOCATION = url;
        PAYPA_EXCEPTION = e;
    }

    public PayPA_Service() {
        super(__getWsdlLocation(), PAYPA_QNAME);
    }

    public PayPA_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), PAYPA_QNAME, features);
    }

    public PayPA_Service(URL wsdlLocation) {
        super(wsdlLocation, PAYPA_QNAME);
    }

    public PayPA_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, PAYPA_QNAME, features);
    }

    public PayPA_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PayPA_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns PayPA
     */
    @WebEndpoint(name = "PayPA")
    public PayPA getPayPA() {
        return super.getPort(new QName("http://pmpay.it/ws/payPA/", "PayPA"), PayPA.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PayPA
     */
    @WebEndpoint(name = "PayPA")
    public PayPA getPayPA(WebServiceFeature... features) {
        return super.getPort(new QName("http://pmpay.it/ws/payPA/", "PayPA"), PayPA.class, features);
    }

    private static URL __getWsdlLocation() {
        if (PAYPA_EXCEPTION!= null) {
            throw PAYPA_EXCEPTION;
        }
        return PAYPA_WSDL_LOCATION;
    }

}