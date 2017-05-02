/*
 * 文 件 名:  ITelnetHandler.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved  Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-7-11
 */
package org.simonme.demo.netty3.telnet;

import org.jboss.netty.channel.Channel;


/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-7-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface ITelnetCommandHandler
{
    public static final String PROMPT = "EFwk>";
    
    /**
     * telnet.命令处理接口
     * @param channel 通道
     * @param commandParts 命令信息
     * @param currentIndex 当前处理到命令中的哪个部分了
     * @param resultSb 前面处理出来的结果
     * @throws RemotingException 远端处理异常
     * @return 处理结果
     */
    StringBuffer telnet(Channel channel, String[] commandParts, int currentIndex, StringBuffer resultSb)
        throws Exception;
    
    /**
     * <一句话功能简述> 返回命令名 <br/>
     * <功能详细描述> 
     * @return 命令名
     * @see [类、类#方法、类#成员]
     */
    public String getCommand();
}
