package org.simonme.dubbo.demo.provider.service.asyncinvoke;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/conf/test/simonme/dubbo/demo/provider/asyncinvoke/applicationContextNode2.xml")
public class HelloServiceTestNode2
{

    @Test
    public void testSayHello()
    {
        try
        {
            System.out.println("路由规则demo：I am node2:-)");
            System.in.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
