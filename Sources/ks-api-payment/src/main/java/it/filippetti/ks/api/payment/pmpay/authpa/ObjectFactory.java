
package it.filippetti.ks.api.payment.pmpay.authpa;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.pmpay.ws.authpa package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.pmpay.ws.authpa
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetTicket }
     * 
     */
    public GetTicket createGetTicket() {
        return new GetTicket();
    }

    /**
     * Create an instance of {@link GetTicketResponse }
     * 
     */
    public GetTicketResponse createGetTicketResponse() {
        return new GetTicketResponse();
    }

    /**
     * Create an instance of {@link GetIUV }
     * 
     */
    public GetIUV createGetIUV() {
        return new GetIUV();
    }

    /**
     * Create an instance of {@link GetIUVResponse }
     * 
     */
    public GetIUVResponse createGetIUVResponse() {
        return new GetIUVResponse();
    }

}
