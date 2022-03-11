package it.filippetti.ks.api.payment.bnl.pay;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentSelectorResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PaymentSelectorResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://services.api.web.cg.igfs.apps.netsw.it/}BasePaymentInitResponse">
 *       &lt;sequence>
 *         &lt;element name="termInfo" type="{http://services.api.web.cg.igfs.apps.netsw.it/}SelectorTerminalInfo" maxOccurs="25" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentSelectorResponse", propOrder = {
    "termInfo"
})
public class PaymentSelectorResponse
    extends BasePaymentInitResponse
{

    protected List<SelectorTerminalInfo> termInfo;

    /**
     * Gets the value of the termInfo property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the termInfo property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTermInfo().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SelectorTerminalInfo }
     *
     *
     */
    public List<SelectorTerminalInfo> getTermInfo() {
        if (termInfo == null) {
            termInfo = new ArrayList<SelectorTerminalInfo>();
        }
        return this.termInfo;
    }

}
