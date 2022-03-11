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
 *         &lt;element name="request" type="{http://services.api.web.cg.igfs.apps.netsw.it/}PaymentSelectorRequest"/>
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
@XmlRootElement(name = "Selector")
public class Selector {

    @XmlElement(required = true)
    protected PaymentSelectorRequest request;

    /**
     * Gets the value of the request property.
     *
     * @return
     *     possible object is
     *     {@link PaymentSelectorRequest }
     *
     */
    public PaymentSelectorRequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     *
     * @param value
     *     allowed object is
     *     {@link PaymentSelectorRequest }
     *
     */
    public void setRequest(PaymentSelectorRequest value) {
        this.request = value;
    }

}
