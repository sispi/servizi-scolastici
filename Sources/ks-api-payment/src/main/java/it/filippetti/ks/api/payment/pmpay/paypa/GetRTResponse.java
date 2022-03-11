
package it.filippetti.ks.api.payment.pmpay.paypa;

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
 *         &lt;element name="GetRTResult" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "getRTResult"
})
@XmlRootElement(name = "GetRTResponse")
public class GetRTResponse {

    @XmlElement(name = "GetRTResult", required = true)
    protected String getRTResult;

    /**
     * Gets the value of the getRTResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetRTResult() {
        return getRTResult;
    }

    /**
     * Sets the value of the getRTResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetRTResult(String value) {
        this.getRTResult = value;
    }

}
