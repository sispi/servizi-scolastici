//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.11.16 at 10:59:54 AM CET 
//


package it.kdm.docer.wspec;

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
 *         &lt;element name="PECException" type="{http://PEC.docer.kdm.it/xsd}PECException" minOccurs="0"/&gt;
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
    "pecException"
})
@XmlRootElement(name = "WSPECPECException")
public class WSPECPECException {

    @XmlElementRef(name = "PECException", namespace = "http://PEC.docer.kdm.it", type = JAXBElement.class, required = false)
    protected JAXBElement<PECException> pecException;

    /**
     * Gets the value of the pecException property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PECException }{@code >}
     *     
     */
    public JAXBElement<PECException> getPECException() {
        return pecException;
    }

    /**
     * Sets the value of the pecException property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PECException }{@code >}
     *     
     */
    public void setPECException(JAXBElement<PECException> value) {
        this.pecException = value;
    }

}
