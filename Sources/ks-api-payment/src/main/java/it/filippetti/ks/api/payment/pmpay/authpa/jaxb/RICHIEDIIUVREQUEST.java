//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.07 at 06:06:43 PM CEST 
//


package it.filippetti.ks.api.payment.pmpay.authpa.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RICHIEDI_IUV_REQUEST complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RICHIEDI_IUV_REQUEST">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="headerRichiestaIUV" type="{urn:authPA}headerRichiestaIUV"/>
 *         &lt;element name="richiestaIUV" type="{urn:authPA}richiestaIUV"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RICHIEDI_IUV_REQUEST", propOrder = {
    "headerRichiestaIUV",
    "richiestaIUV"
})
public class RICHIEDIIUVREQUEST {

    @XmlElement(required = true)
    protected HeaderRichiestaIUV headerRichiestaIUV;
    @XmlElement(required = true)
    protected RichiestaIUV richiestaIUV;

    /**
     * Gets the value of the headerRichiestaIUV property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderRichiestaIUV }
     *     
     */
    public HeaderRichiestaIUV getHeaderRichiestaIUV() {
        return headerRichiestaIUV;
    }

    /**
     * Sets the value of the headerRichiestaIUV property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderRichiestaIUV }
     *     
     */
    public void setHeaderRichiestaIUV(HeaderRichiestaIUV value) {
        this.headerRichiestaIUV = value;
    }

    /**
     * Gets the value of the richiestaIUV property.
     * 
     * @return
     *     possible object is
     *     {@link RichiestaIUV }
     *     
     */
    public RichiestaIUV getRichiestaIUV() {
        return richiestaIUV;
    }

    /**
     * Sets the value of the richiestaIUV property.
     * 
     * @param value
     *     allowed object is
     *     {@link RichiestaIUV }
     *     
     */
    public void setRichiestaIUV(RichiestaIUV value) {
        this.richiestaIUV = value;
    }

}
