package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentVerifyRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentVerifyRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://services.api.web.cg.igfs.apps.netsw.it/}BasePaymentInitRequest">
 *       &lt;sequence>
 *         &lt;element name="paymentID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="refTranID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentVerifyRequest", propOrder = {
    "paymentID",
    "refTranID"
})
public class PaymentVerifyRequest
    extends BasePaymentInitRequest
{

    @XmlElement(required = true)
    protected String paymentID;
    protected Long refTranID;

    /**
     * Gets the value of the paymentID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentID() {
        return paymentID;
    }

    /**
     * Sets the value of the paymentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentID(String value) {
        this.paymentID = value;
    }

    /**
     * Gets the value of the refTranID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRefTranID() {
        return refTranID;
    }

    /**
     * Sets the value of the refTranID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRefTranID(Long value) {
        this.refTranID = value;
    }

}
