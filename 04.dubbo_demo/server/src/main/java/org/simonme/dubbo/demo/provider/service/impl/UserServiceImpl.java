/*
 * 文 件 名:  UserServiceImpl.java
 * 版    权:   . Copyright 2008-2016,  All rights reserved xxx Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-9-7
 */
package org.simonme.dubbo.demo.provider.service.impl;

import java.util.Date;

import org.simonme.dubbo.demo.provider.bean.User;
import org.simonme.dubbo.demo.provider.service.UserService;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  chen.simon
 * @version  [版本号, 2016-9-7]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UserServiceImpl implements UserService
{
    
    /**
     * @param id
     * @return
     */
    @Override
    public User queryUser(int id)
    {
        User u = new User();
        u.setId(id);
        u.setName("Simon");
        System.out.println("Logger:query user--" + new Date());
        return u;
    }
    
}
