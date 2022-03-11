
package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for BaseDTORequest complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BaseDTORequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="apiVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="merID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="payInstr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reqTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="signature" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseDTORequest", propOrder = {
    "apiVersion",
    "tid",
    "merID",
    "payInstr",
    "reqTime",
    "signature"
})
@XmlSeeAlso({
    BasePaymentInitRequest.class
})
public abstract class BaseDTORequest {

    protected String apiVersion;
    protected String tid;
    protected String merID;
    protected String payInstr;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar reqTime;
    @XmlElement(required = true)
    protected String signature;

    /**
     * Gets the value of the apiVersion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * Sets the value of the apiVersion property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setApiVersion(String value) {
        this.apiVersion = value;
    }

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
     * Gets the value of the merID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMerID() {
        return merID;
    }

    /**
     * Sets the value of the merID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMerID(String value) {
        this.merID = value;
    }

    /**
     * Gets the value of the payInstr property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPayInstr() {
        return payInstr;
    }

    /**
     * Sets the value of the payInstr property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPayInstr(String value) {
        this.payInstr = value;
    }

    /**
     * Gets the value of the reqTime property.
     *
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getReqTime() {
        return reqTime;
    }

    /**
     * Sets the value of the reqTime property.
     *
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public void setReqTime(XMLGregorianCalendar value) {
        this.reqTime = value;
    }

    /**
     * Gets the value of the signature property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSignature(String value) {
        this.signature = value;
    }

}
