/*
 * 文 件 名:  HelloTest.java
 * 版    权:   . Copyright 2008-1025,  All rights reserved hiaward
 * 描    述:  <描述>
 * 修 改 人:  Chenxiaguang
 * 修改时间:  2015-8-12
 */
package org.simonme.dubbo.demo.consumer.registercenter.zookeeper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.simonme.dubbo.demo.provider.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2015-8-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/conf/test/simonme/dubbo/demo/consumer/registercenter/zookeeper/applicationContext.xml")
public class HelloClientTest
{
    
    @Autowired
    private HelloService helloService;

    @SuppressWarnings("static-access")
    @Test
    public void testSayHello()
    {
        helloService.sayHello("simon");
        try
        {
            Thread.currentThread().sleep(100011l);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
