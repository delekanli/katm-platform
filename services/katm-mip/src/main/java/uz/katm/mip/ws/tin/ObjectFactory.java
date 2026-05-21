
package uz.katm.mip.ws.tin;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uz.katm.mip.ws.tin package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uz.katm.mip.ws.tin
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FormData }
     * 
     * @return
     *     the new instance of {@link FormData }
     */
    public FormData createFormData() {
        return new FormData();
    }

    /**
     * Create an instance of {@link FormData.Root }
     * 
     * @return
     *     the new instance of {@link FormData.Root }
     */
    public FormData.Root createFormDataRoot() {
        return new FormData.Root();
    }

    /**
     * Create an instance of {@link GetTinbyPas }
     * 
     * @return
     *     the new instance of {@link GetTinbyPas }
     */
    public GetTinbyPas createGetTinbyPas() {
        return new GetTinbyPas();
    }

    /**
     * Create an instance of {@link FormData.Root.List }
     * 
     * @return
     *     the new instance of {@link FormData.Root.List }
     */
    public FormData.Root.List createFormDataRootList() {
        return new FormData.Root.List();
    }

}
