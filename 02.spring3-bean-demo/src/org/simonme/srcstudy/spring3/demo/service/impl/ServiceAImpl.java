/*
 * 文 件 名:  ServiceAImpl.java
 * 版    权:   .
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-4-11
 */
package org.simonme.srcstudy.spring3.demo.service.impl;

import org.simonme.srcstudy.spring3.demo.service.ServiceA;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-4-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ServiceAImpl implements ServiceA
{
    
    private String field;
    
    private String key1;
    
    private String key2;
    
    /**
     * 模拟返回测试数据
     * @return
     */
    @Override
    public String queryA()
    {
        System.out.println("key1:" + key1);
        System.out.println("key2:" + key2);
        return "Query A Result" + field;
    }

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }

    public String getKey1()
    {
        return key1;
    }

    public void setKey1(String key1)
    {
        this.key1 = key1;
    }

    public String getKey2()
    {
        return key2;
    }

    public void setKey2(String key2)
    {
        this.key2 = key2;
    }
}
