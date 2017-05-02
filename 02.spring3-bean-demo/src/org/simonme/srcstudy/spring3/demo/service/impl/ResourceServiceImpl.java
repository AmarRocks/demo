/*
 * 文 件 名:  ResourceServiceImpl.java
 * 版    权:   .
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-5-26
 */
package org.simonme.srcstudy.spring3.demo.service.impl;

import java.io.IOException;

import org.springframework.core.io.Resource;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-5-26]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ResourceServiceImpl
{
    
    private Resource configLocation;
    
    public void test()
    {
        try
        {
            System.out.println("123:" + this.configLocation.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Resource getConfigLocation()
    {
        return configLocation;
    }

    public void setConfigLocation(Resource configLocation)
    {
        this.configLocation = configLocation;
    }
    
    
}
