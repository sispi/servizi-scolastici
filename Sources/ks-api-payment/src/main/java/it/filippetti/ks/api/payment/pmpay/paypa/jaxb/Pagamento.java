//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.07 at 06:06:44 PM CEST 
//


package it.filippetti.ks.api.payment.pmpay.paypa.jaxb;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for pagamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pagamento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RIF_PRATICA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CAUSALE_PAGAMENTO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SERVIZIO_PAGAMENTO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DIVISA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IMPORTO_TOTALE" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="DATA_PAGAMENTO" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pagamento", propOrder = {
    "rifpratica",
    "causalepagamento",
    "serviziopagamento",
    "divisa",
    "importototale",
    "datapagamento"
})
public class Pagamento {

    @XmlElement(name = "RIF_PRATICA", required = true)
    protected String rifpratica;
    @XmlElement(name = "CAUSALE_PAGAMENTO", required = true)
    protected String causalepagamento;
    @XmlElement(name = "SERVIZIO_PAGAMENTO", required = true)
    protected String serviziopagamento;
    @XmlElement(name = "DIVISA", required = true)
    protected String divisa;
    @XmlElement(name = "IMPORTO_TOTALE", required = true)
    protected BigDecimal importototale;
    @XmlElement(name = "DATA_PAGAMENTO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datapagamento;

    /**
     * Gets the value of the rifpratica property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRIFPRATICA() {
        return rifpratica;
    }

    /**
     * Sets the value of the rifpratica property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRIFPRATICA(String value) {
        this.rifpratica = value;
    }

    /**
     * Gets the value of the causalepagamento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAUSALEPAGAMENTO() {
        return causalepagamento;
    }

    /**
     * Sets the value of the causalepagamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAUSALEPAGAMENTO(String value) {
        this.causalepagamento = value;
    }

    /**
     * Gets the value of the serviziopagamento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSERVIZIOPAGAMENTO() {
        return serviziopagamento;
    }

    /**
     * Sets the value of the serviziopagamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSERVIZIOPAGAMENTO(String value) {
        this.serviziopagamento = value;
    }

    /**
     * Gets the value of the divisa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDIVISA() {
        return divisa;
    }

    /**
     * Sets the value of the divisa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDIVISA(String value) {
        this.divisa = value;
    }

    /**
     * Gets the value of the importototale property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIMPORTOTOTALE() {
        return importototale;
    }

    /**
     * Sets the value of the importototale property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIMPORTOTOTALE(BigDecimal value) {
        this.importototale = value;
    }

    /**
     * Gets the value of the datapagamento property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATAPAGAMENTO() {
        return datapagamento;
    }

    /**
     * Sets the value of the datapagamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATAPAGAMENTO(XMLGregorianCalendar value) {
        this.datapagamento = value;
    }

}
