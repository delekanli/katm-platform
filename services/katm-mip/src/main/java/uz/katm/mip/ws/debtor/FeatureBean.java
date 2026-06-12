
package uz.katm.mip.ws.debtor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for featureBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="featureBean"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="field_key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="field_value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "featureBean", propOrder = {
    "fieldKey",
    "fieldValue"
})
public class FeatureBean {

    @XmlElement(name = "field_key")
    protected String fieldKey;
    @XmlElement(name = "field_value")
    protected String fieldValue;

    /**
     * Gets the value of the fieldKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldKey() {
        return fieldKey;
    }

    /**
     * Sets the value of the fieldKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldKey(String value) {
        this.fieldKey = value;
    }

    /**
     * Gets the value of the fieldValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldValue() {
        return fieldValue;
    }

    /**
     * Sets the value of the fieldValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldValue(String value) {
        this.fieldValue = value;
    }

}
