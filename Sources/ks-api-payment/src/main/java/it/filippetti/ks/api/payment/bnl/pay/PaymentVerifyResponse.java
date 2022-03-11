package it.filippetti.ks.api.payment.bnl.pay;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentVerifyResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PaymentVerifyResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://services.api.web.cg.igfs.apps.netsw.it/}BasePaymentInitResponse">
 *       &lt;sequence>
 *         &lt;element name="paymentID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tranID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="authCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enrStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="brand" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="acquirerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maskedPan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="payInstrToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expireMonth" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="expireYear" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="level3Info" type="{http://services.api.web.cg.igfs.apps.netsw.it/}Level3Info" minOccurs="0"/>
 *         &lt;element name="additionalFee" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nssResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="topUpID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receiptPdf" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="payAddData" type="{http://services.api.web.cg.igfs.apps.netsw.it/}Entry" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="payUserRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shopUserMobilePhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentVerifyResponse", propOrder = {
    "paymentID",
    "tranID",
    "authCode",
    "enrStatus",
    "authStatus",
    "brand",
    "acquirerID",
    "maskedPan",
    "addInfo1",
    "addInfo2",
    "addInfo3",
    "addInfo4",
    "addInfo5",
    "payInstrToken",
    "expireMonth",
    "expireYear",
    "level3Info",
    "additionalFee",
    "status",
    "accountName",
    "nssResult",
    "topUpID",
    "receiptPdf",
    "payAddData",
    "payUserRef",
    "shopUserMobilePhone"
})
public class PaymentVerifyResponse
    extends BasePaymentInitResponse
{

    @XmlElement(required = true)
    protected String paymentID;
    protected Long tranID;
    protected String authCode;
    protected String enrStatus;
    protected String authStatus;
    protected String brand;
    protected String acquirerID;
    protected String maskedPan;
    protected String addInfo1;
    protected String addInfo2;
    protected String addInfo3;
    protected String addInfo4;
    protected String addInfo5;
    protected String payInstrToken;
    protected Integer expireMonth;
    protected Integer expireYear;
    protected Level3Info level3Info;
    protected Long additionalFee;
    protected String status;
    protected String accountName;
    protected String nssResult;
    protected String topUpID;
    protected byte[] receiptPdf;
    protected List<Entry> payAddData;
    protected String payUserRef;
    protected String shopUserMobilePhone;

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
     * Gets the value of the tranID property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getTranID() {
        return tranID;
    }

    /**
     * Sets the value of the tranID property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setTranID(Long value) {
        this.tranID = value;
    }

    /**
     * Gets the value of the authCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * Sets the value of the authCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAuthCode(String value) {
        this.authCode = value;
    }

    /**
     * Gets the value of the enrStatus property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEnrStatus() {
        return enrStatus;
    }

    /**
     * Sets the value of the enrStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEnrStatus(String value) {
        this.enrStatus = value;
    }

    /**
     * Gets the value of the authStatus property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAuthStatus() {
        return authStatus;
    }

    /**
     * Sets the value of the authStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAuthStatus(String value) {
        this.authStatus = value;
    }

    /**
     * Gets the value of the brand property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the value of the brand property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBrand(String value) {
        this.brand = value;
    }

    /**
     * Gets the value of the acquirerID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAcquirerID() {
        return acquirerID;
    }

    /**
     * Sets the value of the acquirerID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAcquirerID(String value) {
        this.acquirerID = value;
    }

    /**
     * Gets the value of the maskedPan property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMaskedPan() {
        return maskedPan;
    }

    /**
     * Sets the value of the maskedPan property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMaskedPan(String value) {
        this.maskedPan = value;
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
     * Gets the value of the expireMonth property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getExpireMonth() {
        return expireMonth;
    }

    /**
     * Sets the value of the expireMonth property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setExpireMonth(Integer value) {
        this.expireMonth = value;
    }

    /**
     * Gets the value of the expireYear property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getExpireYear() {
        return expireYear;
    }

    /**
     * Sets the value of the expireYear property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setExpireYear(Integer value) {
        this.expireYear = value;
    }

    /**
     * Gets the value of the level3Info property.
     *
     * @return
     *     possible object is
     *     {@link Level3Info }
     *
     */
    public Level3Info getLevel3Info() {
        return level3Info;
    }

    /**
     * Sets the value of the level3Info property.
     *
     * @param value
     *     allowed object is
     *     {@link Level3Info }
     *
     */
    public void setLevel3Info(Level3Info value) {
        this.level3Info = value;
    }

    /**
     * Gets the value of the additionalFee property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getAdditionalFee() {
        return additionalFee;
    }

    /**
     * Sets the value of the additionalFee property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setAdditionalFee(Long value) {
        this.additionalFee = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the accountName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Sets the value of the accountName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAccountName(String value) {
        this.accountName = value;
    }

    /**
     * Gets the value of the nssResult property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNssResult() {
        return nssResult;
    }

    /**
     * Sets the value of the nssResult property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNssResult(String value) {
        this.nssResult = value;
    }

    /**
     * Gets the value of the topUpID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTopUpID() {
        return topUpID;
    }

    /**
     * Sets the value of the topUpID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTopUpID(String value) {
        this.topUpID = value;
    }

    /**
     * Gets the value of the receiptPdf property.
     *
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getReceiptPdf() {
        return receiptPdf;
    }

    /**
     * Sets the value of the receiptPdf property.
     *
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setReceiptPdf(byte[] value) {
        this.receiptPdf = value;
    }

    /**
     * Gets the value of the payAddData property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the payAddData property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPayAddData().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Entry }
     *
     *
     */
    public List<Entry> getPayAddData() {
        if (payAddData == null) {
            payAddData = new ArrayList<Entry>();
        }
        return this.payAddData;
    }

    /**
     * Gets the value of the payUserRef property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPayUserRef() {
        return payUserRef;
    }

    /**
     * Sets the value of the payUserRef property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPayUserRef(String value) {
        this.payUserRef = value;
    }

    /**
     * Gets the value of the shopUserMobilePhone property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getShopUserMobilePhone() {
        return shopUserMobilePhone;
    }

    /**
     * Sets the value of the shopUserMobilePhone property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setShopUserMobilePhone(String value) {
        this.shopUserMobilePhone = value;
    }

}
