//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.01.17 at 07:25:38 PM CET 
//


package it.kdm.docer.fascicolazione;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.kdm.docer.fascicolazione.xsd.FascicolazioneException;


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
 *         &lt;element name="FascicolazioneException" type="{http://fascicolazione.docer.kdm.it/xsd}FascicolazioneException" minOccurs="0"/&gt;
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
    "fascicolazioneException"
})
@XmlRootElement(name = "WSFascicolazioneFascicolazioneException")
public class WSFascicolazioneFascicolazioneException {

    @XmlElementRef(name = "FascicolazioneException", namespace = "http://fascicolazione.docer.kdm.it", type = JAXBElement.class, required = false)
    protected JAXBElement<FascicolazioneException> fascicolazioneException;

    /**
     * Gets the value of the fascicolazioneException property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FascicolazioneException }{@code >}
     *     
     */
    public JAXBElement<FascicolazioneException> getFascicolazioneException() {
        return fascicolazioneException;
    }

    /**
     * Sets the value of the fascicolazioneException property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FascicolazioneException }{@code >}
     *     
     */
    public void setFascicolazioneException(JAXBElement<FascicolazioneException> value) {
        this.fascicolazioneException = value;
    }

}
