//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.07 at 06:06:44 PM CEST 
//


package it.filippetti.ks.api.payment.pmpay.paypa.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RICHIEDI_RT_REQUEST complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RICHIEDI_RT_REQUEST">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="headerRichiestaRT" type="{urn:pmpay}headerRichiestaRT"/>
 *         &lt;element name="richiestaRT" type="{urn:pmpay}richiestaRT"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RICHIEDI_RT_REQUEST", propOrder = {
    "headerRichiestaRT",
    "richiestaRT"
})
public class RICHIEDIRTREQUEST {

    @XmlElement(required = true)
    protected HeaderRichiestaRT headerRichiestaRT;
    @XmlElement(required = true)
    protected RichiestaRT richiestaRT;

    /**
     * Gets the value of the headerRichiestaRT property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderRichiestaRT }
     *     
     */
    public HeaderRichiestaRT getHeaderRichiestaRT() {
        return headerRichiestaRT;
    }

    /**
     * Sets the value of the headerRichiestaRT property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderRichiestaRT }
     *     
     */
    public void setHeaderRichiestaRT(HeaderRichiestaRT value) {
        this.headerRichiestaRT = value;
    }

    /**
     * Gets the value of the richiestaRT property.
     * 
     * @return
     *     possible object is
     *     {@link RichiestaRT }
     *     
     */
    public RichiestaRT getRichiestaRT() {
        return richiestaRT;
    }

    /**
     * Sets the value of the richiestaRT property.
     * 
     * @param value
     *     allowed object is
     *     {@link RichiestaRT }
     *     
     */
    public void setRichiestaRT(RichiestaRT value) {
        this.richiestaRT = value;
    }

}
