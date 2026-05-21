
package uz.katm.mip.ws.tax;

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
 *         &lt;element name="pin" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="lang" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="AuthInfo"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="userSessionId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="WS_ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="LE_ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "action",
    "pin",
    "lang",
    "authInfo"
})
@XmlRootElement(name = "getTaxInfobyPinReq")
public class GetTaxInfobyPinReq {

    @XmlElement(required = true)
    protected String action;
    @XmlElement(required = true)
    protected String pin;
    @XmlElement(required = true)
    protected String lang;
    @XmlElement(name = "AuthInfo", required = true)
    protected GetTaxInfobyPinReq.AuthInfo authInfo;

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
     * Gets the value of the pin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPin() {
        return pin;
    }

    /**
     * Sets the value of the pin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPin(String value) {
        this.pin = value;
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

    /**
     * Gets the value of the authInfo property.
     * 
     * @return
     *     possible object is
     *     {@link GetTaxInfobyPinReq.AuthInfo }
     *     
     */
    public GetTaxInfobyPinReq.AuthInfo getAuthInfo() {
        return authInfo;
    }

    /**
     * Sets the value of the authInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetTaxInfobyPinReq.AuthInfo }
     *     
     */
    public void setAuthInfo(GetTaxInfobyPinReq.AuthInfo value) {
        this.authInfo = value;
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
     *         &lt;element name="userSessionId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="WS_ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="LE_ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
        "userSessionId",
        "wsid",
        "leid"
    })
    public static class AuthInfo {

        @XmlElement(required = true)
        protected String userSessionId;
        @XmlElement(name = "WS_ID", required = true)
        protected String wsid;
        @XmlElement(name = "LE_ID", required = true)
        protected String leid;

        /**
         * Gets the value of the userSessionId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUserSessionId() {
            return userSessionId;
        }

        /**
         * Sets the value of the userSessionId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUserSessionId(String value) {
            this.userSessionId = value;
        }

        /**
         * Gets the value of the wsid property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWSID() {
            return wsid;
        }

        /**
         * Sets the value of the wsid property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWSID(String value) {
            this.wsid = value;
        }

        /**
         * Gets the value of the leid property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLEID() {
            return leid;
        }

        /**
         * Sets the value of the leid property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLEID(String value) {
            this.leid = value;
        }

    }

}
