//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.01.17 at 07:27:24 PM CET 
//


package it.kdm.docer.registrazione;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cod_ente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cod_aoo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "token",
    "codEnte",
    "codAoo"
})
@XmlRootElement(name = "getRegistri")
public class GetRegistri {

    @XmlElementRef(name = "token", namespace = "http://registrazione.docer.kdm.it", type = JAXBElement.class, required = false)
    protected JAXBElement<String> token;
    @XmlElementRef(name = "cod_ente", namespace = "http://registrazione.docer.kdm.it", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codEnte;
    @XmlElementRef(name = "cod_aoo", namespace = "http://registrazione.docer.kdm.it", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codAoo;

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setToken(JAXBElement<String> value) {
        this.token = value;
    }

    /**
     * Gets the value of the codEnte property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodEnte() {
        return codEnte;
    }

    /**
     * Sets the value of the codEnte property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodEnte(JAXBElement<String> value) {
        this.codEnte = value;
    }

    /**
     * Gets the value of the codAoo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodAoo() {
        return codAoo;
    }

    /**
     * Sets the value of the codAoo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodAoo(JAXBElement<String> value) {
        this.codAoo = value;
    }

}
