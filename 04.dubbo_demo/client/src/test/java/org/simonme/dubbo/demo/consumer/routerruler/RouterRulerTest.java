/*
 * 文 件 名:  RouterRulerTest.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved Hiaward Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  Chenxiaguang
 * 修改时间:  2015-9-2
 */
package org.simonme.dubbo.demo.consumer.routerruler;

import org.junit.Test;
import org.simonme.dubbo.demo.consumer.ConfigConstant;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2015-9-2]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RouterRulerTest
{

    /**
     * <测试条件路由，后续这个动作交由监控中心操作>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    @Test
    public void testConditionRuler()
    {

        /**
         * 向三个注册中心配置 consumer1的请求全部由provider2来提供
         */
        RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
        Registry registry = registryFactory.getRegistry(URL.valueOf("zookeeper://" + ConfigConstant.REGISTER_CENTER_4));
        registry.register(URL.valueOf("condition://0.0.0.0/org.simonme.dubbo.demo.provider.service.HelloService?category=routers&dynamic=false&rule="
                + URL.encode("host = " + ConfigConstant.CONSUMER_IP_1 + " => host = " + ConfigConstant.PROVIDER_IP_2)));
        registry = registryFactory.getRegistry(URL.valueOf("zookeeper://" + ConfigConstant.REGISTER_CENTER_5));
        registry.register(URL.valueOf("condition://0.0.0.0/org.simonme.dubbo.demo.provider.service.HelloService?category=routers&dynamic=false&rule="
                + URL.encode("host = " + ConfigConstant.CONSUMER_IP_1 + " => host = " + ConfigConstant.PROVIDER_IP_2)));
        registry = registryFactory.getRegistry(URL.valueOf("zookeeper://" + ConfigConstant.REGISTER_CENTER_6));
        registry.register(URL.valueOf("condition://0.0.0.0/org.simonme.dubbo.demo.provider.service.HelloService?category=routers&dynamic=false&rule="
                + URL.encode("host = " + ConfigConstant.CONSUMER_IP_1 + " => host = " + ConfigConstant.PROVIDER_IP_2)));
    }
}
