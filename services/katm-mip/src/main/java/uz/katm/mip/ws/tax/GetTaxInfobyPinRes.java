
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
 *                             &lt;element name="branch_id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="doc_num" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="reg_date" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="tin" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlRootElement(name = "getTaxInfobyPinRes")
public class GetTaxInfobyPinRes {

    @XmlElement(name = "err_code", required = true)
    protected String errCode;
    @XmlElement(name = "err_text", required = true)
    protected String errText;
    @XmlElement(required = true)
    protected GetTaxInfobyPinRes.Root root;

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
     *     {@link GetTaxInfobyPinRes.Root }
     *     
     */
    public GetTaxInfobyPinRes.Root getRoot() {
        return root;
    }

    /**
     * Sets the value of the root property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetTaxInfobyPinRes.Root }
     *     
     */
    public void setRoot(GetTaxInfobyPinRes.Root value) {
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
     *                   &lt;element name="branch_id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="doc_num" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="reg_date" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="tin" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
        protected GetTaxInfobyPinRes.Root.List list;

        /**
         * Gets the value of the list property.
         * 
         * @return
         *     possible object is
         *     {@link GetTaxInfobyPinRes.Root.List }
         *     
         */
        public GetTaxInfobyPinRes.Root.List getList() {
            return list;
        }

        /**
         * Sets the value of the list property.
         * 
         * @param value
         *     allowed object is
         *     {@link GetTaxInfobyPinRes.Root.List }
         *     
         */
        public void setList(GetTaxInfobyPinRes.Root.List value) {
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
         *         &lt;element name="branch_id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="doc_num" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="reg_date" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="tin" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
            "branchId",
            "docNum",
            "regDate",
            "status",
            "tin"
        })
        public static class List {

            @XmlElement(name = "branch_id", required = true)
            protected String branchId;
            @XmlElement(name = "doc_num", required = true)
            protected String docNum;
            @XmlElement(name = "reg_date", required = true)
            protected String regDate;
            @XmlElement(required = true)
            protected String status;
            @XmlElement(required = true)
            protected String tin;

            /**
             * Gets the value of the branchId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBranchId() {
                return branchId;
            }

            /**
             * Sets the value of the branchId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBranchId(String value) {
                this.branchId = value;
            }

            /**
             * Gets the value of the docNum property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDocNum() {
                return docNum;
            }

            /**
             * Sets the value of the docNum property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDocNum(String value) {
                this.docNum = value;
            }

            /**
             * Gets the value of the regDate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRegDate() {
                return regDate;
            }

            /**
             * Sets the value of the regDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRegDate(String value) {
                this.regDate = value;
            }

            /**
             * Gets the value of the status property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStatus() {
                return status;
            }

            /**
             * Sets the value of the status property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStatus(String value) {
                this.status = value;
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

        }

    }

}
