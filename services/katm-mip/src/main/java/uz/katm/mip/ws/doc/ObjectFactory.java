
package uz.katm.mip.ws.doc;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uz.katm.mip.ws.doc package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uz.katm.mip.ws.doc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CEPRequest }
     * 
     * @return
     *     the new instance of {@link CEPRequest }
     */
    public CEPRequest createCEPRequest() {
        return new CEPRequest();
    }

    /**
     * Create an instance of {@link CEPRequest.AuthInfo }
     * 
     * @return
     *     the new instance of {@link CEPRequest.AuthInfo }
     */
    public CEPRequest.AuthInfo createCEPRequestAuthInfo() {
        return new CEPRequest.AuthInfo();
    }

    /**
     * Create an instance of {@link CEPResponse }
     * 
     * @return
     *     the new instance of {@link CEPResponse }
     */
    public CEPResponse createCEPResponse() {
        return new CEPResponse();
    }

}
