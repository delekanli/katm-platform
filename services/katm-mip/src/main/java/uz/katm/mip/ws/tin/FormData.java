
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
 *         &lt;element name="err_code" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="err_text" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="root"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="list"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="tin_url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="tin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="fio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
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
    "errCode",
    "errText",
    "root"
})
@XmlRootElement(name = "FormData")
public class FormData {

    @XmlElement(name = "err_code", required = true)
    protected String errCode;
    @XmlElement(name = "err_text", required = true)
    protected String errText;
    @XmlElement(required = true)
    protected FormData.Root root;

    /**
     * Gets the value of the errCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrCode() {
        return errCode;
    }

    /**
     * Sets the value of the errCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrCode(String value) {
        this.errCode = value;
    }

    /**
     * Gets the value of the errText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrText() {
        return errText;
    }

    /**
     * Sets the value of the errText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrText(String value) {
        this.errText = value;
    }

    /**
     * Gets the value of the root property.
     * 
     * @return
     *     possible object is
     *     {@link FormData.Root }
     *     
     */
    public FormData.Root getRoot() {
        return root;
    }

    /**
     * Sets the value of the root property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormData.Root }
     *     
     */
    public void setRoot(FormData.Root value) {
        this.root = value;
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
     *         &lt;element name="list"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="tin_url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="tin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="fio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "list"
    })
    public static class Root {

        @XmlElement(required = true)
        protected FormData.Root.List list;

        /**
         * Gets the value of the list property.
         * 
         * @return
         *     possible object is
         *     {@link FormData.Root.List }
         *     
         */
        public FormData.Root.List getList() {
            return list;
        }

        /**
         * Sets the value of the list property.
         * 
         * @param value
         *     allowed object is
         *     {@link FormData.Root.List }
         *     
         */
        public void setList(FormData.Root.List value) {
            this.list = value;
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
         *         &lt;element name="tin_url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="tin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="fio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
            "tinUrl",
            "tin",
            "fio"
        })
        public static class List {

            @XmlElement(name = "tin_url")
            protected String tinUrl;
            protected String tin;
            protected String fio;

            /**
             * Gets the value of the tinUrl property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTinUrl() {
                return tinUrl;
            }

            /**
             * Sets the value of the tinUrl property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTinUrl(String value) {
                this.tinUrl = value;
            }

            /**
             * Gets the value of the tin property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTin() {
                return tin;
            }

            /**
             * Sets the value of the tin property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTin(String value) {
                this.tin = value;
            }

            /**
             * Gets the value of the fio property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFio() {
                return fio;
            }

            /**
             * Sets the value of the fio property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFio(String value) {
                this.fio = value;
            }

        }

    }

}
