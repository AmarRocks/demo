/*
 * 文 件 名:  UserService.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved  Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-5-5
 */
package org.simonme.demo.cxf.server.service.user.service;

import javax.jws.WebService;

import org.simonme.demo.cxf.server.service.user.bean.User;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-5-5]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@WebService 
public interface UserService
{

    public abstract User queryUser(User user);
    
}
