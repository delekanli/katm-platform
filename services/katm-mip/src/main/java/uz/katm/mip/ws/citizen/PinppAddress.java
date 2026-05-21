
package uz.katm.mip.ws.citizen;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for anonymous complex type&lt;/p&gt;.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pinpp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pinpp"
})
@XmlRootElement(name = "PinppAddress")
public class PinppAddress {

    protected String pinpp;

    /**
     * Gets the value of the pinpp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPinpp() {
        return pinpp;
    }

    /**
     * Sets the value of the pinpp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPinpp(String value) {
        this.pinpp = value;
    }

}
