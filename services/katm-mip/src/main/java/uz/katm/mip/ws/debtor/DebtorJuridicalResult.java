
package uz.katm.mip.ws.debtor;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for debtorJuridicalResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="debtorJuridicalResult"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://deptorservice.mibcenter.mib.uz/}genericResult"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="all_debet_sum" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="debets" type="{http://deptorservice.mibcenter.mib.uz/}debtorBean" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="inn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="review_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "debtorJuridicalResult", propOrder = {
    "allDebetSum",
    "debets",
    "inn",
    "reviewDate"
})
public class DebtorJuridicalResult
    extends GenericResult
{

    @XmlElement(name = "all_debet_sum")
    protected Double allDebetSum;
    @XmlElement(nillable = true)
    protected List<DebtorBean> debets;
    protected String inn;
    @XmlElement(name = "review_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar reviewDate;

    /**
     * Gets the value of the allDebetSum property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAllDebetSum() {
        return allDebetSum;
    }

    /**
     * Sets the value of the allDebetSum property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAllDebetSum(Double value) {
        this.allDebetSum = value;
    }

    /**
     * Gets the value of the debets property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the debets property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDebets().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DebtorBean }
     * 
     * 
     */
    public List<DebtorBean> getDebets() {
        if (debets == null) {
            debets = new ArrayList<DebtorBean>();
        }
        return this.debets;
    }

    /**
     * Gets the value of the inn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInn() {
        return inn;
    }

    /**
     * Sets the value of the inn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInn(String value) {
        this.inn = value;
    }

    /**
     * Gets the value of the reviewDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReviewDate() {
        return reviewDate;
    }

    /**
     * Sets the value of the reviewDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReviewDate(XMLGregorianCalendar value) {
        this.reviewDate = value;
    }

}
