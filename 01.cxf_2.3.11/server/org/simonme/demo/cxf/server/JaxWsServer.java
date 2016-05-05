/*
 * 文 件 名:  JaxWsServer.java
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-5-5
 */
package org.simonme.demo.cxf.server;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.simonme.demo.cxf.server.service.user.service.UserService;
import org.simonme.demo.cxf.server.service.user.service.UserServiceImpl;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-5-5]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class JaxWsServer
{
    
    /** <一句话功能简述>
     * <功能详细描述>
     * @param args
     * @see [类、类#方法、类#成员]
     */
    public static void main(String[] args)
    {
        UserService userServiceImpl = new UserServiceImpl();  
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();  
        factory.setAddress("http://localhost:8085/ws/hello");  
        factory.setServiceBean(userServiceImpl);  
        factory.setServiceClass(UserService.class);
//        //LoggingInInterceptor和LoggingOutInterceptor是日志拦截器，用于输入和输出时显示日志  
//        factory.getInInterceptors().add(new LoggingInInterceptor());  
//        factory.getOutInterceptors().add(new LoggingOutInterceptor());  
        factory.create();  
        System.out.println("ws is published");  
    }
    
}
