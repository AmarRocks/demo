package org.simonme.dubbo.demo.provider.service.registercenter.zookeeper;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/conf/test/simonme/dubbo/demo/provider/registercenter/zookeeper/applicationContextNode2.xml")
public class HelloServiceTestNode2
{

    @Test
    public void testSayHello()
    {
        try
        {
            System.out.println("zookeeper注册中心demo：I am node2:-)");
            System.in.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
