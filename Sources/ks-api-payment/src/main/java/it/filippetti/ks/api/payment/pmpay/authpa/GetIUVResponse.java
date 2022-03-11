
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
 *         &lt;element name="GetIUVResult" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "getIUVResult"
})
@XmlRootElement(name = "GetIUVResponse")
public class GetIUVResponse {

    @XmlElement(name = "GetIUVResult", required = true)
    protected String getIUVResult;

    /**
     * Gets the value of the getIUVResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetIUVResult() {
        return getIUVResult;
    }

    /**
     * Sets the value of the getIUVResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetIUVResult(String value) {
        this.getIUVResult = value;
    }

}
