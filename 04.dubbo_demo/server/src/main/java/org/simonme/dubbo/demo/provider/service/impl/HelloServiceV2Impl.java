/*
 * 文 件 名:  HelloServiceV2Impl.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved Hiaward Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  Chenxiaguang
 * 修改时间:  2015-9-3
 */
package org.simonme.dubbo.demo.provider.service.impl;

import java.util.Date;

import org.simonme.dubbo.demo.provider.service.HelloService;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2015-9-3]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HelloServiceV2Impl implements HelloService
{

    /**
     * @param name
     */
    @Override
    public void sayHello(String name)
    {
        System.out.println("[v2]Hello " + name + "-" + (new Date()));
    }

}
