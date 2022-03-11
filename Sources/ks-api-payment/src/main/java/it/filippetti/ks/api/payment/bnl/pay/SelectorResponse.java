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
 *         &lt;element name="response" type="{http://services.api.web.cg.igfs.apps.netsw.it/}PaymentSelectorResponse"/>
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
    "response"
})
@XmlRootElement(name = "SelectorResponse")
public class SelectorResponse {

    @XmlElement(required = true)
    protected PaymentSelectorResponse response;

    /**
     * Gets the value of the response property.
     *
     * @return
     *     possible object is
     *     {@link PaymentSelectorResponse }
     *
     */
    public PaymentSelectorResponse getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     *
     * @param value
     *     allowed object is
     *     {@link PaymentSelectorResponse }
     *
     */
    public void setResponse(PaymentSelectorResponse value) {
        this.response = value;
    }

}
