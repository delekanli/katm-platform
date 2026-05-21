
package uz.katm.mip.ws.tax;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uz.katm.mip.ws.tax package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uz.katm.mip.ws.tax
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetTaxInfobyPinReq }
     * 
     * @return
     *     the new instance of {@link GetTaxInfobyPinReq }
     */
    public GetTaxInfobyPinReq createGetTaxInfobyPinReq() {
        return new GetTaxInfobyPinReq();
    }

    /**
     * Create an instance of {@link GetTaxInfobyPinRes }
     * 
     * @return
     *     the new instance of {@link GetTaxInfobyPinRes }
     */
    public GetTaxInfobyPinRes createGetTaxInfobyPinRes() {
        return new GetTaxInfobyPinRes();
    }

    /**
     * Create an instance of {@link GetTaxInfobyPinRes.Root }
     * 
     * @return
     *     the new instance of {@link GetTaxInfobyPinRes.Root }
     */
    public GetTaxInfobyPinRes.Root createGetTaxInfobyPinResRoot() {
        return new GetTaxInfobyPinRes.Root();
    }

    /**
     * Create an instance of {@link GetTaxInfobyPinReq.AuthInfo }
     * 
     * @return
     *     the new instance of {@link GetTaxInfobyPinReq.AuthInfo }
     */
    public GetTaxInfobyPinReq.AuthInfo createGetTaxInfobyPinReqAuthInfo() {
        return new GetTaxInfobyPinReq.AuthInfo();
    }

    /**
     * Create an instance of {@link GetTaxInfobyPinRes.Root.List }
     * 
     * @return
     *     the new instance of {@link GetTaxInfobyPinRes.Root.List }
     */
    public GetTaxInfobyPinRes.Root.List createGetTaxInfobyPinResRootList() {
        return new GetTaxInfobyPinRes.Root.List();
    }

}
