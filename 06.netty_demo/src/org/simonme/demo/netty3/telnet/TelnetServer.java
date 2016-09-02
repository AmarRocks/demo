/*
 * 文 件 名:  EFwkServer.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-7-11
 */
package org.simonme.demo.netty3.telnet;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author Chenxiaguang
 * @version [版本号, 2016-7-11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TelnetServer
{
    
    /**
     * EFwk服务默认端口号
     */
    private static final int PORT = 23;
    
    /**
     * 线程数为2， telnet管理服务线程不需太多
     */
    private static final int THREAD_COUNT = 2;
    
    private static Logger logger = Logger.getLogger(TelnetServer.class);
    
    private int customPort = 0;
    
    private TelnetChannelPipelineFactory serviceChannelPipelineFactory;
    
    /**
     * <一句话功能简述> 启动websocket server <功能详细描述>
     * 
     * @see [类、类#方法、类#成员]
     */
    public void boot()
    {
        // 设置 Socket channel factory
        ServerBootstrap bootstrap =
            new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool(), THREAD_COUNT));
        
        serviceChannelPipelineFactory = new TelnetChannelPipelineFactory();
        
        // 设置 Socket pipeline factory
        bootstrap.setPipelineFactory(serviceChannelPipelineFactory);
        
        // 启动服务，开始监听
        int tempPort = PORT;
        
        // 如果定制了端口，采用定制端口
        if (customPort != 0)
        {
            tempPort = customPort;
        }
        bootstrap.bind(new InetSocketAddress(tempPort));
        
        // 打印提示信息
        logger.info("telnet server started at port " + tempPort + '.');
    }
    
    public static void main(String[] args)
    {
        TelnetServer telnetServer = new TelnetServer();
        telnetServer.boot();
    }

}
