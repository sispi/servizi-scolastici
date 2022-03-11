package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for MandateInfo complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MandateInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mandateID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contractID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sequenceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="frequency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="durationStartDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="durationEndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="firstCollectionDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="finalCollectionDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="maxAmount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MandateInfo", propOrder = {
    "mandateID",
    "contractID",
    "sequenceType",
    "frequency",
    "durationStartDate",
    "durationEndDate",
    "firstCollectionDate",
    "finalCollectionDate",
    "maxAmount"
})
public class MandateInfo {

    protected String mandateID;
    protected String contractID;
    protected String sequenceType;
    protected String frequency;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar durationStartDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar durationEndDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar firstCollectionDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar finalCollectionDate;
    protected Long maxAmount;

    /**
     * Gets the value of the mandateID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMandateID() {
        return mandateID;
    }

    /**
     * Sets the value of the mandateID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMandateID(String value) {
        this.mandateID = value;
    }

    /**
     * Gets the value of the contractID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getContractID() {
        return contractID;
    }

    /**
     * Sets the value of the contractID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setContractID(String value) {
        this.contractID = value;
    }

    /**
     * Gets the value of the sequenceType property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSequenceType() {
        return sequenceType;
    }

    /**
     * Sets the value of the sequenceType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSequenceType(String value) {
        this.sequenceType = value;
    }

    /**
     * Gets the value of the frequency property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Sets the value of the frequency property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFrequency(String value) {
        this.frequency = value;
    }

    /**
     * Gets the value of the durationStartDate property.
     *
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDurationStartDate() {
        return durationStartDate;
    }

    /**
     * Sets the value of the durationStartDate property.
     *
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public void setDurationStartDate(XMLGregorianCalendar value) {
        this.durationStartDate = value;
    }

    /**
     * Gets the value of the durationEndDate property.
     *
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDurationEndDate() {
        return durationEndDate;
    }

    /**
     * Sets the value of the durationEndDate property.
     *
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public void setDurationEndDate(XMLGregorianCalendar value) {
        this.durationEndDate = value;
    }

    /**
     * Gets the value of the firstCollectionDate property.
     *
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getFirstCollectionDate() {
        return firstCollectionDate;
    }

    /**
     * Sets the value of the firstCollectionDate property.
     *
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public void setFirstCollectionDate(XMLGregorianCalendar value) {
        this.firstCollectionDate = value;
    }

    /**
     * Gets the value of the finalCollectionDate property.
     *
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getFinalCollectionDate() {
        return finalCollectionDate;
    }

    /**
     * Sets the value of the finalCollectionDate property.
     *
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public void setFinalCollectionDate(XMLGregorianCalendar value) {
        this.finalCollectionDate = value;
    }

    /**
     * Gets the value of the maxAmount property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getMaxAmount() {
        return maxAmount;
    }

    /**
     * Sets the value of the maxAmount property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setMaxAmount(Long value) {
        this.maxAmount = value;
    }

}
