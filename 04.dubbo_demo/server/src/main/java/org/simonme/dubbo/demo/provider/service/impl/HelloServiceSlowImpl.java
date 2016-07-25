/*
 * 文 件 名:  HelloServiceImpl.java
 * 版    权:   . Copyright 2008-1025,  All rights reserved hiaward
 * 描    述:  <描述>
 * 修 改 人:  Chenxiaguang
 * 修改时间:  2015-8-12
 */
package org.simonme.dubbo.demo.provider.service.impl;

import java.util.Date;

import org.simonme.dubbo.demo.provider.service.HelloService;


/**
 * <方法中存在延时的一个慢实现，用于负载均衡demo>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2015-8-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HelloServiceSlowImpl implements HelloService
{

    @SuppressWarnings("static-access")
    @Override
    public void sayHello(String name)
    {
        try
        {
            Thread.currentThread().sleep(3000l);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println("Hello " + name + "-" + (new Date()));
    }

}
