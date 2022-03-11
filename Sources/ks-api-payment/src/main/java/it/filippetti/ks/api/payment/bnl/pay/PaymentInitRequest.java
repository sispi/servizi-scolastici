package it.filippetti.ks.api.payment.bnl.pay;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for PaymentInitRequest complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PaymentInitRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://services.api.web.cg.igfs.apps.netsw.it/}BasePaymentInitRequest">
 *       &lt;sequence>
 *         &lt;element name="shopUserRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shopUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shopUserAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shopUserMobilePhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shopUserIMEI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trType">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="AUTH"/>
 *               &lt;enumeration value="PURCHASE"/>
 *               &lt;enumeration value="VERIFY"/>
 *               &lt;enumeration value="TOKENIZE"/>
 *               &lt;enumeration value="DELETE"/>
 *               &lt;enumeration value="MODIFY"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="currencyCode" type="{http://services.api.web.cg.igfs.apps.netsw.it/}Currency" minOccurs="0"/>
 *         &lt;element name="langID" type="{http://services.api.web.cg.igfs.apps.netsw.it/}Language"/>
 *         &lt;element name="notifyURL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="errorURL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="callbackURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addInfo5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="payInstrToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="regenPayInstrToken" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="keepOnRegenPayInstrToken" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="payInstrTokenExpire" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="payInstrTokenUsageLimit" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="payInstrTokenAlg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="level3Info" type="{http://services.api.web.cg.igfs.apps.netsw.it/}Level3Info" minOccurs="0"/>
 *         &lt;element name="mandateInfo" type="{http://services.api.web.cg.igfs.apps.netsw.it/}MandateInfo" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="freeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="topUpID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstTopUp" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="payInstrTokenAsTopUpID" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="validityExpire" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="minExpireMonth" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="minExpireYear" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="termInfo" type="{http://services.api.web.cg.igfs.apps.netsw.it/}InitTerminalInfo" maxOccurs="25" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentInitRequest", propOrder = {
    "shopUserRef",
    "shopUserName",
    "shopUserAccount",
    "shopUserMobilePhone",
    "shopUserIMEI",
    "trType",
    "amount",
    "currencyCode",
    "langID",
    "notifyURL",
    "errorURL",
    "callbackURL",
    "addInfo1",
    "addInfo2",
    "addInfo3",
    "addInfo4",
    "addInfo5",
    "payInstrToken",
    "billingID",
    "regenPayInstrToken",
    "keepOnRegenPayInstrToken",
    "payInstrTokenExpire",
    "payInstrTokenUsageLimit",
    "payInstrTokenAlg",
    "accountName",
    "level3Info",
    "mandateInfo",
    "description",
    "paymentReason",
    "freeText",
    "topUpID",
    "firstTopUp",
    "payInstrTokenAsTopUpID",
    "validityExpire",
    "minExpireMonth",
    "minExpireYear",
    "termInfo"
})
public class PaymentInitRequest
    extends BasePaymentInitRequest
{

    protected String shopUserRef;
    protected String shopUserName;
    protected String shopUserAccount;
    protected String shopUserMobilePhone;
    protected String shopUserIMEI;
    @XmlElement(required = true)
    protected String trType;
    protected Long amount;
    protected Currency currencyCode;
    @XmlElement(required = true)
    protected Language langID;
    @XmlElement(required = true)
    protected String notifyURL;
    @XmlElement(required = true)
    protected String errorURL;
    protected String callbackURL;
    protected String addInfo1;
    protected String addInfo2;
    protected String addInfo3;
    protected String addInfo4;
    protected String addInfo5;
    protected String payInstrToken;
    protected String billingID;
    protected Boolean regenPayInstrToken;
    protected Boolean keepOnRegenPayInstrToken;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar payInstrTokenExpire;
    protected Integer payInstrTokenUsageLimit;
    protected String payInstrTokenAlg;
    protected String accountName;
    protected Level3Info level3Info;
    protected MandateInfo mandateInfo;
    protected String description;
    protected String paymentReason;
    protected String freeText;
    protected String topUpID;
    protected Boolean firstTopUp;
    protected Boolean payInstrTokenAsTopUpID;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validityExpire;
    protected Integer minExpireMonth;
    protected Integer minExpireYear;
    protected List<InitTerminalInfo> termInfo;

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
     * Gets the value of the shopUserName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getShopUserName() {
        return shopUserName;
    }

    /**
     * Sets the value of the shopUserName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setShopUserName(String value) {
        this.shopUserName = value;
    }

    /**
     * Gets the value of the shopUserAccount property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getShopUserAccount() {
        return shopUserAccount;
    }

    /**
     * Sets the value of the shopUserAccount property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setShopUserAccount(String value) {
        this.shopUserAccount = value;
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

    /**
     * Gets the value of the shopUserIMEI property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getShopUserIMEI() {
        return shopUserIMEI;
    }

    /**
     * Sets the value of the shopUserIMEI property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setShopUserIMEI(String value) {
        this.shopUserIMEI = value;
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
     * Gets the value of the notifyURL property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNotifyURL() {
        return notifyURL;
    }

    /**
     * Sets the value of the notifyURL property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNotifyURL(String value) {
        this.notifyURL = value;
    }

    /**
     * Gets the value of the errorURL property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getErrorURL() {
        return errorURL;
    }

    /**
     * Sets the value of the errorURL property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setErrorURL(String value) {
        this.errorURL = value;
    }

    /**
     * Gets the value of the callbackURL property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCallbackURL() {
        return callbackURL;
    }

    /**
     * Sets the value of the callbackURL property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCallbackURL(String value) {
        this.callbackURL = value;
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

    /**
     * Gets the value of the regenPayInstrToken property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRegenPayInstrToken() {
        return regenPayInstrToken;
    }

    /**
     * Sets the value of the regenPayInstrToken property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRegenPayInstrToken(Boolean value) {
        this.regenPayInstrToken = value;
    }

    /**
     * Gets the value of the keepOnRegenPayInstrToken property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isKeepOnRegenPayInstrToken() {
        return keepOnRegenPayInstrToken;
    }

    /**
     * Sets the value of the keepOnRegenPayInstrToken property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setKeepOnRegenPayInstrToken(Boolean value) {
        this.keepOnRegenPayInstrToken = value;
    }

    /**
     * Gets the value of the payInstrTokenExpire property.
     *
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getPayInstrTokenExpire() {
        return payInstrTokenExpire;
    }

    /**
     * Sets the value of the payInstrTokenExpire property.
     *
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public void setPayInstrTokenExpire(XMLGregorianCalendar value) {
        this.payInstrTokenExpire = value;
    }

    /**
     * Gets the value of the payInstrTokenUsageLimit property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getPayInstrTokenUsageLimit() {
        return payInstrTokenUsageLimit;
    }

    /**
     * Sets the value of the payInstrTokenUsageLimit property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setPayInstrTokenUsageLimit(Integer value) {
        this.payInstrTokenUsageLimit = value;
    }

    /**
     * Gets the value of the payInstrTokenAlg property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPayInstrTokenAlg() {
        return payInstrTokenAlg;
    }

    /**
     * Sets the value of the payInstrTokenAlg property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPayInstrTokenAlg(String value) {
        this.payInstrTokenAlg = value;
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
     * Gets the value of the mandateInfo property.
     *
     * @return
     *     possible object is
     *     {@link MandateInfo }
     *
     */
    public MandateInfo getMandateInfo() {
        return mandateInfo;
    }

    /**
     * Sets the value of the mandateInfo property.
     *
     * @param value
     *     allowed object is
     *     {@link MandateInfo }
     *
     */
    public void setMandateInfo(MandateInfo value) {
        this.mandateInfo = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the paymentReason property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPaymentReason() {
        return paymentReason;
    }

    /**
     * Sets the value of the paymentReason property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPaymentReason(String value) {
        this.paymentReason = value;
    }

    /**
     * Gets the value of the freeText property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFreeText() {
        return freeText;
    }

    /**
     * Sets the value of the freeText property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFreeText(String value) {
        this.freeText = value;
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
     * Gets the value of the firstTopUp property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isFirstTopUp() {
        return firstTopUp;
    }

    /**
     * Sets the value of the firstTopUp property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setFirstTopUp(Boolean value) {
        this.firstTopUp = value;
    }

    /**
     * Gets the value of the payInstrTokenAsTopUpID property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isPayInstrTokenAsTopUpID() {
        return payInstrTokenAsTopUpID;
    }

    /**
     * Sets the value of the payInstrTokenAsTopUpID property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setPayInstrTokenAsTopUpID(Boolean value) {
        this.payInstrTokenAsTopUpID = value;
    }

    /**
     * Gets the value of the validityExpire property.
     *
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getValidityExpire() {
        return validityExpire;
    }

    /**
     * Sets the value of the validityExpire property.
     *
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public void setValidityExpire(XMLGregorianCalendar value) {
        this.validityExpire = value;
    }

    /**
     * Gets the value of the minExpireMonth property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getMinExpireMonth() {
        return minExpireMonth;
    }

    /**
     * Sets the value of the minExpireMonth property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setMinExpireMonth(Integer value) {
        this.minExpireMonth = value;
    }

    /**
     * Gets the value of the minExpireYear property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getMinExpireYear() {
        return minExpireYear;
    }

    /**
     * Sets the value of the minExpireYear property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setMinExpireYear(Integer value) {
        this.minExpireYear = value;
    }

    /**
     * Gets the value of the termInfo property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the termInfo property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTermInfo().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InitTerminalInfo }
     *
     *
     */
    public List<InitTerminalInfo> getTermInfo() {
        if (termInfo == null) {
            termInfo = new ArrayList<InitTerminalInfo>();
        }
        return this.termInfo;
    }

}
