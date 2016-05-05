package org.simonme.demo.cxf.client.user;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.3.11
 * 2016-05-05T21:49:51.621+08:00
 * Generated source version: 2.3.11
 * 
 */
@WebService(targetNamespace = "http://service.user.service.server.cxf.demo.simonme.org/", name = "UserService")
@XmlSeeAlso({ObjectFactory.class})
public interface UserService {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "queryUser", targetNamespace = "http://service.user.service.server.cxf.demo.simonme.org/", className = "org.simonme.demo.cxf.client.user.QueryUser")
    @WebMethod
    @ResponseWrapper(localName = "queryUserResponse", targetNamespace = "http://service.user.service.server.cxf.demo.simonme.org/", className = "org.simonme.demo.cxf.client.user.QueryUserResponse")
    public org.simonme.demo.cxf.client.user.User queryUser(
        @WebParam(name = "arg0", targetNamespace = "")
        org.simonme.demo.cxf.client.user.User arg0
    );
}