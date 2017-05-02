package org.simonme.demo.cxf.client.user;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.3.11
 * 2016-05-05T21:49:51.634+08:00
 * Generated source version: 2.3.11
 * 
 */
@WebServiceClient(name = "UserServiceService", 
                  wsdlLocation = "http://localhost:8085/efwkdemo/userservice?wsdl",
                  targetNamespace = "http://service.user.service.server.cxf.demo.simonme.org/") 
public class UserServiceService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://service.user.service.server.cxf.demo.simonme.org/", "UserServiceService");
    public final static QName UserServicePort = new QName("http://service.user.service.server.cxf.demo.simonme.org/", "UserServicePort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8085/efwkdemo/userservice?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(UserServiceService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8085/efwkdemo/userservice?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public UserServiceService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public UserServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public UserServiceService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public UserServiceService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public UserServiceService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public UserServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns UserService
     */
    @WebEndpoint(name = "UserServicePort")
    public UserService getUserServicePort() {
        return super.getPort(UserServicePort, UserService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns UserService
     */
    @WebEndpoint(name = "UserServicePort")
    public UserService getUserServicePort(WebServiceFeature... features) {
        return super.getPort(UserServicePort, UserService.class, features);
    }

}
