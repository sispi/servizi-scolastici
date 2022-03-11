package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentSelectorRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentSelectorRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://services.api.web.cg.igfs.apps.netsw.it/}BasePaymentInitRequest">
 *       &lt;sequence>
 *         &lt;element name="shopUserRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trType">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="AUTH"/>
 *               &lt;enumeration value="PURCHASE"/>
 *               &lt;enumeration value="VERIFY"/>
 *               &lt;enumeration value="TOKENIZE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="currencyCode" type="{http://services.api.web.cg.igfs.apps.netsw.it/}Currency" minOccurs="0"/>
 *         &lt;element name="langID" type="{http://services.api.web.cg.igfs.apps.netsw.it/}Language"/>
 *         &lt;element name="addInfo1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="payInstrToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentSelectorRequest", propOrder = {
    "shopUserRef",
    "trType",
    "amount",
    "currencyCode",
    "langID",
    "addInfo1",
    "addInfo2",
    "addInfo3",
    "addInfo4",
    "addInfo5",
    "payInstrToken",
    "billingID"
})
public class PaymentSelectorRequest
    extends BasePaymentInitRequest
{

    protected String shopUserRef;
    @XmlElement(required = true)
    protected String trType;
    protected Long amount;
    protected Currency currencyCode;
    @XmlElement(required = true)
    protected Language langID;
    protected String addInfo1;
    protected String addInfo2;
    protected String addInfo3;
    protected String addInfo4;
    protected String addInfo5;
    protected String payInstrToken;
    protected String billingID;

    /**
     * Gets the value of the shopUserRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShopUserRef() {
        return shopUserRef;
    }

    /**
     * Sets the value of the shopUserRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShopUserRef(String value) {
        this.shopUserRef = value;
    }

    /**
     * Gets the value of the trType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrType() {
        return trType;
    }

    /**
     * Sets the value of the trType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrType(String value) {
        this.trType = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAmount(Long value) {
        this.amount = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link Currency }
     *     
     */
    public Currency getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Currency }
     *     
     */
    public void setCurrencyCode(Currency value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the langID property.
     * 
     * @return
     *     possible object is
     *     {@link Language }
     *     
     */
    public Language getLangID() {
        return langID;
    }

    /**
     * Sets the value of the langID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Language }
     *     
     */
    public void setLangID(Language value) {
        this.langID = value;
    }

    /**
     * Gets the value of the addInfo1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddInfo1() {
        return addInfo1;
    }

    /**
     * Sets the value of the addInfo1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddInfo1(String value) {
        this.addInfo1 = value;
    }

    /**
     * Gets the value of the addInfo2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddInfo2() {
        return addInfo2;
    }

    /**
     * Sets the value of the addInfo2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddInfo2(String value) {
        this.addInfo2 = value;
    }

    /**
     * Gets the value of the addInfo3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddInfo3() {
        return addInfo3;
    }

    /**
     * Sets the value of the addInfo3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddInfo3(String value) {
        this.addInfo3 = value;
    }

    /**
     * Gets the value of the addInfo4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddInfo4() {
        return addInfo4;
    }

    /**
     * Sets the value of the addInfo4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddInfo4(String value) {
        this.addInfo4 = value;
    }

    /**
     * Gets the value of the addInfo5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddInfo5() {
        return addInfo5;
    }

    /**
     * Sets the value of the addInfo5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddInfo5(String value) {
        this.addInfo5 = value;
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
