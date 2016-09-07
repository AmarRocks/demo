/*
 * 文 件 名:  User.java
 * 版    权:   . Copyright 2008-2016,  All rights reserved xxx Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-9-6
 */
package org.simonme.dubbo.demo.provider.bean;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  chen.simon
 * @version  [版本号, 2016-9-6]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class User implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -9013469977687925032L;

    private int id;
    
    private String name;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
