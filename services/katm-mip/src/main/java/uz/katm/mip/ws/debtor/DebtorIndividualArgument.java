
package uz.katm.mip.ws.debtor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DebtorIndividualArgument complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DebtorIndividualArgument"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://deptorservice.mibcenter.mib.uz/}GenericArgument"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="passport_sn" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="passport_number" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DebtorIndividualArgument", propOrder = {
    "passportSn",
    "passportNumber"
})
public class DebtorIndividualArgument
    extends GenericArgument
{

    @XmlElement(name = "passport_sn", required = true)
    protected String passportSn;
    @XmlElement(name = "passport_number", required = true)
    protected String passportNumber;

    /**
     * Gets the value of the passportSn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassportSn() {
        return passportSn;
    }

    /**
     * Sets the value of the passportSn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassportSn(String value) {
        this.passportSn = value;
    }

    /**
     * Gets the value of the passportNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     * Sets the value of the passportNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassportNumber(String value) {
        this.passportNumber = value;
    }

}
