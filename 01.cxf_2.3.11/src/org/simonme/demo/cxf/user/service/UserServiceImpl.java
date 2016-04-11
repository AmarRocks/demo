package org.simonme.demo.cxf.user.service;

import org.simonme.demo.cxf.user.bean.User;

public class UserServiceImpl
{
    
    public User queryUser(User user)
    {
        User mockData = new User();
        mockData.setId(11);
        mockData.setName("Simon");
        return mockData;
    }
}
