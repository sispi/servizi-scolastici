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
 * <p>Java class for richiestaRT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="richiestaRT">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RIF_INTERNO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IUV" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "richiestaRT", propOrder = {
    "rifinterno",
    "iuv"
})
public class RichiestaRT {

    @XmlElement(name = "RIF_INTERNO", required = true)
    protected String rifinterno;
    @XmlElement(name = "IUV", required = true)
    protected String iuv;

    /**
     * Gets the value of the rifinterno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRIFINTERNO() {
        return rifinterno;
    }

    /**
     * Sets the value of the rifinterno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRIFINTERNO(String value) {
        this.rifinterno = value;
    }

    /**
     * Gets the value of the iuv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIUV() {
        return iuv;
    }

    /**
     * Sets the value of the iuv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIUV(String value) {
        this.iuv = value;
    }

}
