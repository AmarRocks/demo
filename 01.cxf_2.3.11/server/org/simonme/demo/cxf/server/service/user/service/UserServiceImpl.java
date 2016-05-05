package org.simonme.demo.cxf.server.service.user.service;

import org.simonme.demo.cxf.server.service.user.bean.User;

public class UserServiceImpl implements UserService
{
    
    @Override
    public User queryUser(User user)
    {
        User mockData = new User();
        mockData.setId(11);
        mockData.setName("Simon");
        return mockData;
    }
}
