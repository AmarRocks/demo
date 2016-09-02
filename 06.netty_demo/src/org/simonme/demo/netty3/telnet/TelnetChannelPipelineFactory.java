/*
 * 文 件 名:  EFwkServiceChannelPipelineFactory.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved  Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-7-11
 */
package org.simonme.demo.netty3.telnet;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.FixedLengthFrameDecoder;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-7-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TelnetChannelPipelineFactory implements ChannelPipelineFactory
{
    
    private static final int C_8192 = 8192;
    
    private TelnetHandler telnethandler;
    
    public TelnetChannelPipelineFactory()
    {
        telnethandler = new TelnetHandler();
    }

    /**
     * 获取pipeline
     * @return 
     * @throws Exception
     */
    @Override
    public ChannelPipeline getPipeline()
        throws Exception
    {
        TelnetChannelPipeline pipeline = new TelnetChannelPipeline();
        pipeline.setTelnetHandler(telnethandler);
//        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(C_8192, Delimiters.lineDelimiter()));
        pipeline.addLast("framer", new FixedLengthFrameDecoder(1));
//        pipeline.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
//        pipeline.addLast("encoder", new StringDecoder(Charset.forName("UTF-8")));
        pipeline.addLast("handler", telnethandler);
        return pipeline;
    }

}
