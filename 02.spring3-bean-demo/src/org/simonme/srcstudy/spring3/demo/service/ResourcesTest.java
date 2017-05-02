/*
 * 文 件 名:  RourcesTest.java
 * 版    权:   .
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-5-26
 */
package org.simonme.srcstudy.spring3.demo.service;

import java.util.Map;

import org.simonme.srcstudy.spring3.demo.assistant.MTTA;
import org.simonme.srcstudy.spring3.demo.assistant.MTTA.MTRunner;
import org.simonme.srcstudy.spring3.demo.service.impl.ResourceServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-5-26]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ResourcesTest
{
    
    /** <一句话功能简述>
     * <功能详细描述>
     * @param args
     * @see [类、类#方法、类#成员]
     */
    public static void main(String[] args)
    {
        MTRunner runner = new MTRunner()
        {

            @Override
            public String getRunnerName()
            {
                return "testWebServiceActionPerform";
            }

            @Override
            public void run(Map<?, ?> runnerContext)
            {
                try
                {
                    t1();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        MTTA.mt(runner, 1000, null);
        
    }
    
    private static void t1()
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        ResourceServiceImpl resourceServiceImpl = (ResourceServiceImpl)applicationContext.getBean("resourceServiceImpl", ResourceServiceImpl.class);
        resourceServiceImpl.test();
    }
    
}
