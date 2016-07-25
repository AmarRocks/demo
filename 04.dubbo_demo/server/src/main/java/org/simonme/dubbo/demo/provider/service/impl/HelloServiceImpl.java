/*
 * 文 件 名:  HelloServiceImpl.java
 * 版    权:   . Copyright 2008-1025,  All rights reserved hiaward
 * 描    述:  <描述>
 * 修 改 人:  Chenxiaguang
 * 修改时间:  2015-8-12
 */
package org.simonme.dubbo.demo.provider.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.simonme.dubbo.demo.provider.service.HelloService;


/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2015-8-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HelloServiceImpl implements HelloService
{

    @Override
    public void sayHello(String name)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("Hello " + name + "-" + sdf.format(new Date()));
    }

}
