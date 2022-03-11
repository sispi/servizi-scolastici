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
 *         &lt;element name="response" type="{http://services.api.web.cg.igfs.apps.netsw.it/}PaymentInitResponse"/>
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
@XmlRootElement(name = "InitResponse")
public class InitResponse {

    @XmlElement(required = true)
    protected PaymentInitResponse response;

    /**
     * Gets the value of the response property.
     *
     * @return
     *     possible object is
     *     {@link PaymentInitResponse }
     *
     */
    public PaymentInitResponse getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     *
     * @param value
     *     allowed object is
     *     {@link PaymentInitResponse }
     *
     */
    public void setResponse(PaymentInitResponse value) {
        this.response = value;
    }

}
