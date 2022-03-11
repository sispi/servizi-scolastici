package it.filippetti.ks.api.payment.bnl.pay;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Level3Info complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Level3Info">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="invoiceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="senderPostalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="senderCountryCode" type="{http://services.api.web.cg.igfs.apps.netsw.it/}Country" minOccurs="0"/>
 *         &lt;element name="destinationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinationStreet" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinationStreet2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinationStreet3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinationCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinationState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinationPostalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinationCountryCode" type="{http://services.api.web.cg.igfs.apps.netsw.it/}Country" minOccurs="0"/>
 *         &lt;element name="destinationPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinationFax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinationEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="billingName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingStreet" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingStreet2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingStreet3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingPostalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingCountryCode" type="{http://services.api.web.cg.igfs.apps.netsw.it/}Country" minOccurs="0"/>
 *         &lt;element name="billingPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingFax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="freightAmount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="taxAmount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="vat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="note" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="product" type="{http://services.api.web.cg.igfs.apps.netsw.it/}Level3InfoProduct" maxOccurs="10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Level3Info", propOrder = {
    "invoiceNumber",
    "senderPostalCode",
    "senderCountryCode",
    "destinationName",
    "destinationStreet",
    "destinationStreet2",
    "destinationStreet3",
    "destinationCity",
    "destinationState",
    "destinationPostalCode",
    "destinationCountryCode",
    "destinationPhone",
    "destinationFax",
    "destinationEmail",
    "destinationDate",
    "billingName",
    "billingStreet",
    "billingStreet2",
    "billingStreet3",
    "billingCity",
    "billingState",
    "billingPostalCode",
    "billingCountryCode",
    "billingPhone",
    "billingFax",
    "billingEmail",
    "freightAmount",
    "taxAmount",
    "vat",
    "note",
    "product"
})
public class Level3Info {

    protected String invoiceNumber;
    protected String senderPostalCode;
    protected Country senderCountryCode;
    protected String destinationName;
    protected String destinationStreet;
    protected String destinationStreet2;
    protected String destinationStreet3;
    protected String destinationCity;
    protected String destinationState;
    protected String destinationPostalCode;
    protected Country destinationCountryCode;
    protected String destinationPhone;
    protected String destinationFax;
    protected String destinationEmail;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar destinationDate;
    protected String billingName;
    protected String billingStreet;
    protected String billingStreet2;
    protected String billingStreet3;
    protected String billingCity;
    protected String billingState;
    protected String billingPostalCode;
    protected Country billingCountryCode;
    protected String billingPhone;
    protected String billingFax;
    protected String billingEmail;
    protected Long freightAmount;
    protected Long taxAmount;
    protected String vat;
    protected String note;
    protected List<Level3InfoProduct> product;

    /**
     * Gets the value of the invoiceNumber property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * Sets the value of the invoiceNumber property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInvoiceNumber(String value) {
        this.invoiceNumber = value;
    }

    /**
     * Gets the value of the senderPostalCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSenderPostalCode() {
        return senderPostalCode;
    }

    /**
     * Sets the value of the senderPostalCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSenderPostalCode(String value) {
        this.senderPostalCode = value;
    }

    /**
     * Gets the value of the senderCountryCode property.
     *
     * @return
     *     possible object is
     *     {@link Country }
     *
     */
    public Country getSenderCountryCode() {
        return senderCountryCode;
    }

    /**
     * Sets the value of the senderCountryCode property.
     *
     * @param value
     *     allowed object is
     *     {@link Country }
     *
     */
    public void setSenderCountryCode(Country value) {
        this.senderCountryCode = value;
    }

    /**
     * Gets the value of the destinationName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestinationName() {
        return destinationName;
    }

    /**
     * Sets the value of the destinationName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestinationName(String value) {
        this.destinationName = value;
    }

    /**
     * Gets the value of the destinationStreet property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestinationStreet() {
        return destinationStreet;
    }

    /**
     * Sets the value of the destinationStreet property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestinationStreet(String value) {
        this.destinationStreet = value;
    }

    /**
     * Gets the value of the destinationStreet2 property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestinationStreet2() {
        return destinationStreet2;
    }

    /**
     * Sets the value of the destinationStreet2 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestinationStreet2(String value) {
        this.destinationStreet2 = value;
    }

    /**
     * Gets the value of the destinationStreet3 property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestinationStreet3() {
        return destinationStreet3;
    }

    /**
     * Sets the value of the destinationStreet3 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestinationStreet3(String value) {
        this.destinationStreet3 = value;
    }

    /**
     * Gets the value of the destinationCity property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestinationCity() {
        return destinationCity;
    }

    /**
     * Sets the value of the destinationCity property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestinationCity(String value) {
        this.destinationCity = value;
    }

    /**
     * Gets the value of the destinationState property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestinationState() {
        return destinationState;
    }

    /**
     * Sets the value of the destinationState property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestinationState(String value) {
        this.destinationState = value;
    }

    /**
     * Gets the value of the destinationPostalCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestinationPostalCode() {
        return destinationPostalCode;
    }

    /**
     * Sets the value of the destinationPostalCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestinationPostalCode(String value) {
        this.destinationPostalCode = value;
    }

    /**
     * Gets the value of the destinationCountryCode property.
     *
     * @return
     *     possible object is
     *     {@link Country }
     *
     */
    public Country getDestinationCountryCode() {
        return destinationCountryCode;
    }

    /**
     * Sets the value of the destinationCountryCode property.
     *
     * @param value
     *     allowed object is
     *     {@link Country }
     *
     */
    public void setDestinationCountryCode(Country value) {
        this.destinationCountryCode = value;
    }

    /**
     * Gets the value of the destinationPhone property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestinationPhone() {
        return destinationPhone;
    }

    /**
     * Sets the value of the destinationPhone property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestinationPhone(String value) {
        this.destinationPhone = value;
    }

    /**
     * Gets the value of the destinationFax property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestinationFax() {
        return destinationFax;
    }

    /**
     * Sets the value of the destinationFax property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestinationFax(String value) {
        this.destinationFax = value;
    }

    /**
     * Gets the value of the destinationEmail property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestinationEmail() {
        return destinationEmail;
    }

    /**
     * Sets the value of the destinationEmail property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestinationEmail(String value) {
        this.destinationEmail = value;
    }

    /**
     * Gets the value of the destinationDate property.
     *
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDestinationDate() {
        return destinationDate;
    }

    /**
     * Sets the value of the destinationDate property.
     *
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public void setDestinationDate(XMLGregorianCalendar value) {
        this.destinationDate = value;
    }

    /**
     * Gets the value of the billingName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBillingName() {
        return billingName;
    }

    /**
     * Sets the value of the billingName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBillingName(String value) {
        this.billingName = value;
    }

    /**
     * Gets the value of the billingStreet property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBillingStreet() {
        return billingStreet;
    }

    /**
     * Sets the value of the billingStreet property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBillingStreet(String value) {
        this.billingStreet = value;
    }

    /**
     * Gets the value of the billingStreet2 property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBillingStreet2() {
        return billingStreet2;
    }

    /**
     * Sets the value of the billingStreet2 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBillingStreet2(String value) {
        this.billingStreet2 = value;
    }

    /**
     * Gets the value of the billingStreet3 property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBillingStreet3() {
        return billingStreet3;
    }

    /**
     * Sets the value of the billingStreet3 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBillingStreet3(String value) {
        this.billingStreet3 = value;
    }

    /**
     * Gets the value of the billingCity property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBillingCity() {
        return billingCity;
    }

    /**
     * Sets the value of the billingCity property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBillingCity(String value) {
        this.billingCity = value;
    }

    /**
     * Gets the value of the billingState property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBillingState() {
        return billingState;
    }

    /**
     * Sets the value of the billingState property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBillingState(String value) {
        this.billingState = value;
    }

    /**
     * Gets the value of the billingPostalCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBillingPostalCode() {
        return billingPostalCode;
    }

    /**
     * Sets the value of the billingPostalCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBillingPostalCode(String value) {
        this.billingPostalCode = value;
    }

    /**
     * Gets the value of the billingCountryCode property.
     *
     * @return
     *     possible object is
     *     {@link Country }
     *
     */
    public Country getBillingCountryCode() {
        return billingCountryCode;
    }

    /**
     * Sets the value of the billingCountryCode property.
     *
     * @param value
     *     allowed object is
     *     {@link Country }
     *
     */
    public void setBillingCountryCode(Country value) {
        this.billingCountryCode = value;
    }

    /**
     * Gets the value of the billingPhone property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBillingPhone() {
        return billingPhone;
    }

    /**
     * Sets the value of the billingPhone property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBillingPhone(String value) {
        this.billingPhone = value;
    }

    /**
     * Gets the value of the billingFax property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBillingFax() {
        return billingFax;
    }

    /**
     * Sets the value of the billingFax property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBillingFax(String value) {
        this.billingFax = value;
    }

    /**
     * Gets the value of the billingEmail property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBillingEmail() {
        return billingEmail;
    }

    /**
     * Sets the value of the billingEmail property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBillingEmail(String value) {
        this.billingEmail = value;
    }

    /**
     * Gets the value of the freightAmount property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getFreightAmount() {
        return freightAmount;
    }

    /**
     * Sets the value of the freightAmount property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setFreightAmount(Long value) {
        this.freightAmount = value;
    }

    /**
     * Gets the value of the taxAmount property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getTaxAmount() {
        return taxAmount;
    }

    /**
     * Sets the value of the taxAmount property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setTaxAmount(Long value) {
        this.taxAmount = value;
    }

    /**
     * Gets the value of the vat property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVat() {
        return vat;
    }

    /**
     * Sets the value of the vat property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVat(String value) {
        this.vat = value;
    }

    /**
     * Gets the value of the note property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNote(String value) {
        this.note = value;
    }

    /**
     * Gets the value of the product property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the product property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProduct().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Level3InfoProduct }
     *
     *
     */
    public List<Level3InfoProduct> getProduct() {
        if (product == null) {
            product = new ArrayList<Level3InfoProduct>();
        }
        return this.product;
    }

}
