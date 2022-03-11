
package it.filippetti.ks.api.payment.pmpay.authpa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetTicketResult" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getTicketResult"
})
@XmlRootElement(name = "GetTicketResponse")
public class GetTicketResponse {

    @XmlElement(name = "GetTicketResult", required = true)
    protected String getTicketResult;

    /**
     * Gets the value of the getTicketResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetTicketResult() {
        return getTicketResult;
    }

    /**
     * Sets the value of the getTicketResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetTicketResult(String value) {
        this.getTicketResult = value;
    }

}
