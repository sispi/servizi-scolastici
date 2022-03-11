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
 *         &lt;element name="request" type="{http://services.api.web.cg.igfs.apps.netsw.it/}PaymentVerifyRequest"/>
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
@XmlRootElement(name = "Verify")
public class Verify {

    @XmlElement(required = true)
    protected PaymentVerifyRequest request;

    /**
     * Gets the value of the request property.
     *
     * @return
     *     possible object is
     *     {@link PaymentVerifyRequest }
     *
     */
    public PaymentVerifyRequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     *
     * @param value
     *     allowed object is
     *     {@link PaymentVerifyRequest }
     *
     */
    public void setRequest(PaymentVerifyRequest value) {
        this.request = value;
    }

}
