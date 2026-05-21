
package uz.katm.mip.ws.citizen;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uz.katm.mip.ws.citizen package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uz.katm.mip.ws.citizen
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PinppAddressResponse }
     * 
     * @return
     *     the new instance of {@link PinppAddressResponse }
     */
    public PinppAddressResponse createPinppAddressResponse() {
        return new PinppAddressResponse();
    }

    /**
     * Create an instance of {@link PinppAddress }
     * 
     * @return
     *     the new instance of {@link PinppAddress }
     */
    public PinppAddress createPinppAddress() {
        return new PinppAddress();
    }

    /**
     * Create an instance of {@link PinppAddressResponse.PinppAddressResult }
     * 
     * @return
     *     the new instance of {@link PinppAddressResponse.PinppAddressResult }
     */
    public PinppAddressResponse.PinppAddressResult createPinppAddressResponsePinppAddressResult() {
        return new PinppAddressResponse.PinppAddressResult();
    }

}
