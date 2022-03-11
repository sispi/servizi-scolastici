package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for BasePaymentInitRequest complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BasePaymentInitRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://services.api.web.cg.igfs.apps.netsw.it/}BaseDTORequest">
 *       &lt;sequence>
 *         &lt;element name="shopID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BasePaymentInitRequest", propOrder = {
    "shopID"
})
@XmlSeeAlso({
    PaymentVerifyRequest.class,
    PaymentInitRequest.class,
    PaymentSelectorRequest.class
})
public abstract class BasePaymentInitRequest
    extends BaseDTORequest
{

    @XmlElement(required = true)
    protected String shopID;

    /**
     * Gets the value of the shopID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getShopID() {
        return shopID;
    }

    /**
     * Sets the value of the shopID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setShopID(String value) {
        this.shopID = value;
    }

}
