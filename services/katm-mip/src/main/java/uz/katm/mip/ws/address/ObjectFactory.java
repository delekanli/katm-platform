
package uz.katm.mip.ws.address;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uz.katm.mip.ws.address package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uz.katm.mip.ws.address
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResponseModel }
     * 
     * @return
     *     the new instance of {@link ResponseModel }
     */
    public ResponseModel createResponseModel() {
        return new ResponseModel();
    }

    /**
     * Create an instance of {@link Request }
     * 
     * @return
     *     the new instance of {@link Request }
     */
    public Request createRequest() {
        return new Request();
    }

    /**
     * Create an instance of {@link ResponseModel.PPermanentAddress }
     * 
     * @return
     *     the new instance of {@link ResponseModel.PPermanentAddress }
     */
    public ResponseModel.PPermanentAddress createResponseModelPPermanentAddress() {
        return new ResponseModel.PPermanentAddress();
    }

    /**
     * Create an instance of {@link ResponseModel.PTemproaryAddress }
     * 
     * @return
     *     the new instance of {@link ResponseModel.PTemproaryAddress }
     */
    public ResponseModel.PTemproaryAddress createResponseModelPTemproaryAddress() {
        return new ResponseModel.PTemproaryAddress();
    }

}
