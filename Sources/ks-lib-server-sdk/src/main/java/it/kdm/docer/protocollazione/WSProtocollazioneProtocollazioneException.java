//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.01.17 at 07:28:12 PM CET 
//


package it.kdm.docer.protocollazione;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.kdm.docer.protocollazione.xsd.ProtocollazioneException;


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
 *         &lt;element name="ProtocollazioneException" type="{http://protocollazione.docer.kdm.it/xsd}ProtocollazioneException" minOccurs="0"/&gt;
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
    "protocollazioneException"
})
@XmlRootElement(name = "WSProtocollazioneProtocollazioneException")
public class WSProtocollazioneProtocollazioneException {

    @XmlElementRef(name = "ProtocollazioneException", namespace = "http://protocollazione.docer.kdm.it", type = JAXBElement.class, required = false)
    protected JAXBElement<ProtocollazioneException> protocollazioneException;

    /**
     * Gets the value of the protocollazioneException property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ProtocollazioneException }{@code >}
     *     
     */
    public JAXBElement<ProtocollazioneException> getProtocollazioneException() {
        return protocollazioneException;
    }

    /**
     * Sets the value of the protocollazioneException property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ProtocollazioneException }{@code >}
     *     
     */
    public void setProtocollazioneException(JAXBElement<ProtocollazioneException> value) {
        this.protocollazioneException = value;
    }

}
