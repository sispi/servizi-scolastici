
package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for BaseDTOResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BaseDTOResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="payInstr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reqTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="rc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="error" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="errorDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "BaseDTOResponse", propOrder = {
    "tid",
    "payInstr",
    "reqTime",
    "rc",
    "error",
    "errorDesc",
    "signature"
})
@XmlSeeAlso({
    BasePaymentInitResponse.class
})
public abstract class BaseDTOResponse {

    @XmlElement(required = true)
    protected String tid;
    protected String payInstr;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar reqTime;
    @XmlElement(required = true)
    protected String rc;
    protected boolean error;
    @XmlElement(required = true)
    protected String errorDesc;
    @XmlElement(required = true)
    protected String signature;

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
     * Gets the value of the rc property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRc() {
        return rc;
    }

    /**
     * Sets the value of the rc property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRc(String value) {
        this.rc = value;
    }

    /**
     * Gets the value of the error property.
     *
     */
    public boolean isError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     *
     */
    public void setError(boolean value) {
        this.error = value;
    }

    /**
     * Gets the value of the errorDesc property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getErrorDesc() {
        return errorDesc;
    }

    /**
     * Sets the value of the errorDesc property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setErrorDesc(String value) {
        this.errorDesc = value;
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
