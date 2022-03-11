package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="request" type="{http://services.api.web.cg.igfs.apps.netsw.it/}PaymentInitRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "request"
})
@XmlRootElement(name = "Init")
public class Init {

    @XmlElement(required = true)
    protected PaymentInitRequest request;

    /**
     * Gets the value of the request property.
     *
     * @return
     *     possible object is
     *     {@link PaymentInitRequest }
     *
     */
    public PaymentInitRequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     *
     * @param value
     *     allowed object is
     *     {@link PaymentInitRequest }
     *
     */
    public void setRequest(PaymentInitRequest value) {
        this.request = value;
    }

}
