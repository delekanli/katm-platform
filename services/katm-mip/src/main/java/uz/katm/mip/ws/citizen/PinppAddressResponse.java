
package uz.katm.mip.ws.citizen;

import java.math.BigInteger;
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
 *         &lt;element name="PinppAddressResult" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="pResult" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *                   &lt;element name="AnswereMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AnswereComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AnswereId" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *                   &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="pComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
    "pinppAddressResult"
})
@XmlRootElement(name = "PinppAddressResponse")
public class PinppAddressResponse {

    @XmlElement(name = "PinppAddressResult")
    protected PinppAddressResponse.PinppAddressResult pinppAddressResult;

    /**
     * Gets the value of the pinppAddressResult property.
     * 
     * @return
     *     possible object is
     *     {@link PinppAddressResponse.PinppAddressResult }
     *     
     */
    public PinppAddressResponse.PinppAddressResult getPinppAddressResult() {
        return pinppAddressResult;
    }

    /**
     * Sets the value of the pinppAddressResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link PinppAddressResponse.PinppAddressResult }
     *     
     */
    public void setPinppAddressResult(PinppAddressResponse.PinppAddressResult value) {
        this.pinppAddressResult = value;
    }


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
     *         &lt;element name="pResult" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
     *         &lt;element name="AnswereMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AnswereComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AnswereId" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
     *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="pComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "pResult",
        "answereMessage",
        "answereComment",
        "answereId",
        "data",
        "pComment"
    })
    public static class PinppAddressResult {

        protected BigInteger pResult;
        @XmlElement(name = "AnswereMessage")
        protected String answereMessage;
        @XmlElement(name = "AnswereComment")
        protected String answereComment;
        @XmlElement(name = "AnswereId")
        protected BigInteger answereId;
        @XmlElement(name = "Data")
        protected String data;
        protected String pComment;

        /**
         * Gets the value of the pResult property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getPResult() {
            return pResult;
        }

        /**
         * Sets the value of the pResult property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setPResult(BigInteger value) {
            this.pResult = value;
        }

        /**
         * Gets the value of the answereMessage property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAnswereMessage() {
            return answereMessage;
        }

        /**
         * Sets the value of the answereMessage property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAnswereMessage(String value) {
            this.answereMessage = value;
        }

        /**
         * Gets the value of the answereComment property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAnswereComment() {
            return answereComment;
        }

        /**
         * Sets the value of the answereComment property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAnswereComment(String value) {
            this.answereComment = value;
        }

        /**
         * Gets the value of the answereId property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getAnswereId() {
            return answereId;
        }

        /**
         * Sets the value of the answereId property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setAnswereId(BigInteger value) {
            this.answereId = value;
        }

        /**
         * Gets the value of the data property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getData() {
            return data;
        }

        /**
         * Sets the value of the data property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setData(String value) {
            this.data = value;
        }

        /**
         * Gets the value of the pComment property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPComment() {
            return pComment;
        }

        /**
         * Sets the value of the pComment property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPComment(String value) {
            this.pComment = value;
        }

    }

}
