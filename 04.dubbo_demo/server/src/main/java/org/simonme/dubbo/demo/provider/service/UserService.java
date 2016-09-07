/*
 * 文 件 名:  UserService.java
 * 版    权:   . Copyright 2008-2016,  All rights reserved xxx Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-9-6
 */
package org.simonme.dubbo.demo.provider.service;

import org.simonme.dubbo.demo.provider.bean.User;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  chen.simon
 * @version  [版本号, 2016-9-6]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface UserService
{
    public User queryUser(int id);
}
