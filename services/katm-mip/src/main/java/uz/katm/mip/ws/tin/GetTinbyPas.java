
package uz.katm.mip.ws.tin;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="action" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="pass_ser" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="pass_num" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="lang" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "action",
    "passSer",
    "passNum",
    "lang"
})
@XmlRootElement(name = "GetTinbyPas")
public class GetTinbyPas {

    @XmlElement(required = true)
    protected String action;
    @XmlElement(name = "pass_ser", required = true)
    protected String passSer;
    @XmlElement(name = "pass_num", required = true)
    protected String passNum;
    @XmlElement(required = true)
    protected String lang;

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    /**
     * Gets the value of the passSer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassSer() {
        return passSer;
    }

    /**
     * Sets the value of the passSer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassSer(String value) {
        this.passSer = value;
    }

    /**
     * Gets the value of the passNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassNum() {
        return passNum;
    }

    /**
     * Sets the value of the passNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassNum(String value) {
        this.passNum = value;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

}
