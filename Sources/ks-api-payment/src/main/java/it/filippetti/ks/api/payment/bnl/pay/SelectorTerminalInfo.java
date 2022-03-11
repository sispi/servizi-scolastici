package it.filippetti.ks.api.payment.bnl.pay;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SelectorTerminalInfo complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SelectorTerminalInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="payInstr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="payInstrDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imgURL" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelectorTerminalInfo", propOrder = {
    "tid",
    "description",
    "payInstr",
    "payInstrDescription",
    "imgURL"
})
public class SelectorTerminalInfo {

    @XmlElement(required = true)
    protected String tid;
    protected String description;
    @XmlElement(required = true)
    protected String payInstr;
    protected String payInstrDescription;
    protected List<String> imgURL;

    /**
     * Gets the value of the tid property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTid() {
        return tid;
    }

    /**
     * Sets the value of the tid property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTid(String value) {
        this.tid = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the payInstr property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPayInstr() {
        return payInstr;
    }

    /**
     * Sets the value of the payInstr property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPayInstr(String value) {
        this.payInstr = value;
    }

    /**
     * Gets the value of the payInstrDescription property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPayInstrDescription() {
        return payInstrDescription;
    }

    /**
     * Sets the value of the payInstrDescription property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPayInstrDescription(String value) {
        this.payInstrDescription = value;
    }

    /**
     * Gets the value of the imgURL property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imgURL property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImgURL().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     *
     *
     */
    public List<String> getImgURL() {
        if (imgURL == null) {
            imgURL = new ArrayList<String>();
        }
        return this.imgURL;
    }

}
