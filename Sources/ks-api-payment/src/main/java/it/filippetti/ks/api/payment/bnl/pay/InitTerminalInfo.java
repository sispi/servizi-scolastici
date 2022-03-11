package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InitTerminalInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InitTerminalInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="payInstrToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InitTerminalInfo", propOrder = {
    "tid",
    "payInstrToken",
    "billingID"
})
public class InitTerminalInfo {

    @XmlElement(required = true)
    protected String tid;
    protected String payInstrToken;
    protected String billingID;

    /**
     * Gets the value of the tid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTid() {
        return tid;
    }

    /**
     * Sets the value of the tid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTid(String value) {
        this.tid = value;
    }

    /**
     * Gets the value of the payInstrToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayInstrToken() {
        return payInstrToken;
    }

    /**
     * Sets the value of the payInstrToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayInstrToken(String value) {
        this.payInstrToken = value;
    }

    /**
     * Gets the value of the billingID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingID() {
        return billingID;
    }

    /**
     * Sets the value of the billingID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingID(String value) {
        this.billingID = value;
    }

}
