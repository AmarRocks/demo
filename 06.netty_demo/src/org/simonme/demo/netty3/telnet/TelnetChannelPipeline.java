/*
 * 文 件 名:  TelnetChannelPipeline.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved xx Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-8-22
 */
package org.simonme.demo.netty3.telnet;

import java.util.Date;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.UpstreamMessageEvent;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-8-22]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TelnetChannelPipeline extends DefaultChannelPipeline
{
    
    private TelnetHandler telnetHandler;

    @Override
    public void sendUpstream(ChannelEvent e)
    {
        super.sendUpstream(e);
        if(e instanceof UpstreamMessageEvent)
        {
            System.out.println("客户端发完一次消息--" + (new Date()).getTime() + "--" + e.getClass());
            fireSendUpStreamDone(e);
        }
    }
    
    private void fireSendUpStreamDone(ChannelEvent e)
    {
        if (telnetHandler != null)
        {
            telnetHandler.fireSendUpStreamDone(e);
        }
    }

    public void setTelnetHandler(TelnetHandler telnetHandler)
    {
        this.telnetHandler = telnetHandler;
    }
    
}
