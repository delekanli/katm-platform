
package uz.katm.mip.ws.debtor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DebtorIndividualByPinflArgument complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DebtorIndividualByPinflArgument"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://deptorservice.mibcenter.mib.uz/}GenericArgument"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pinfl" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DebtorIndividualByPinflArgument", propOrder = {
    "pinfl"
})
public class DebtorIndividualByPinflArgument
    extends GenericArgument
{

    @XmlElement(required = true)
    protected String pinfl;

    /**
     * Gets the value of the pinfl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPinfl() {
        return pinfl;
    }

    /**
     * Sets the value of the pinfl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPinfl(String value) {
        this.pinfl = value;
    }

}
