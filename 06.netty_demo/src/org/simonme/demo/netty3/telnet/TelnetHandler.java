/*
 * 文 件 名:  EFwkTelnetHandler.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved  Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-7-14
 */
package org.simonme.demo.netty3.telnet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-7-14]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TelnetHandler extends SimpleChannelUpstreamHandler
{
    private static Logger logger = Logger.getLogger(TelnetHandler.class);
    
    private static byte[] UP = new byte[]{(byte)27,(byte)91, (byte)65};
    
    private static byte[] DOWN = new byte[]{(byte)27,(byte)91, (byte)66};
    
    private static byte[] ENTER1 = new byte[]{(byte)'\r'};
    
    private static byte[] ENTER2 = new byte[]{(byte)'\n'};
    
    private Map<String, ITelnetCommandHandler> command2Handler = new HashMap();
    
    private Map<Integer, ByteBuffer> channelId2Buffer = new ConcurrentHashMap<Integer, ByteBuffer>();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
        throws Exception
    {
        super.exceptionCaught(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
        throws Exception
    {
        Object message = e.getMessage();
//        String msg = handleCommand(message, e.getChannel());
//        byte[] bytes = msg.getBytes("utf-8");
//        ChannelBuffer buffer = ChannelBuffers.buffer(bytes.length);
//		buffer.writeBytes(bytes);
//        e.getChannel().write(buffer);
        if (message instanceof BigEndianHeapChannelBuffer)
        {
            BigEndianHeapChannelBuffer heapByteBuffer = (BigEndianHeapChannelBuffer)message;
            byte[] receivedBytes = heapByteBuffer.array();
            appendByteToBuffer(ctx.getChannel().getId(), receivedBytes);
            System.out.println("received:" + receivedBytes[0]);
            ctx.getChannel().getId();
        }
        System.out.println("received:" + message);
    }

    private synchronized void appendByteToBuffer(Integer id, byte[] receivedBytes)
    {
        if (id == null || receivedBytes == null || receivedBytes.length == 0)
        {
            return;
        }
        ByteBuffer existBuffer = channelId2Buffer.get(id);
        if(existBuffer == null)
        {
            existBuffer = ByteBuffer.allocate(32);
            channelId2Buffer.put(id, existBuffer);
        }
        
        /**
         * 判断是否需要对buffer扩容 然后塞入接收到的byte
         */
        if (existBuffer.remaining() < receivedBytes.length)
        {
            //  扩32个长度
            int newCapacity = existBuffer.capacity() + 32;
            ByteBuffer temp = ByteBuffer.allocate(newCapacity);
            existBuffer.flip();
            temp.put(existBuffer);
            existBuffer = temp;
        }
        existBuffer.put(receivedBytes);
        if (endsWith(existBuffer, UP))
        {
            System.out.println("handle command up");
        }
        if (endsWith(existBuffer, DOWN))
        {
            System.out.println("handle command DOWN");
        }
        if (endsWith(existBuffer, ENTER1))
        {
            System.out.println("handle command ENTER1");
        }
        if (endsWith(existBuffer, ENTER2))
        {
            System.out.println("handle command ENTER2");
        }
        
    }
    
    private boolean endsWith(ByteBuffer existBuffer, byte[] target)
    {
        if (existBuffer == null || target == null || target.length == 0)
        {
            return false;
        }
        if(existBuffer.capacity() - existBuffer.remaining() < target.length)
        {
            return false;
        }
        int targetLength = target.length;
        for (int i = targetLength - 1; i >= 0; i--)
        {
             if (existBuffer.get(i) == null || existBuffer.get(i) != target[i])
             {
                 return false;
             }
        }
        return true;
    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @param message
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String handleCommand(Object message, Channel channel)
    {
        if (!(message instanceof String))
        {
            return ITelnetCommandHandler.PROMPT;
        }
        String command = (String)message;
        if (command == null || "".equals(command))
        {
            return ITelnetCommandHandler.PROMPT;
        }
        String msg = "";
        String[] commandParts = command.split("\\s");
        try
        {
            ITelnetCommandHandler commandHandler = command2Handler.get(commandParts[0]);
            if (commandHandler == null)
            {
                msg = "Unsupport operation:" + message + "\r\n" + ITelnetCommandHandler.PROMPT;
                return msg;
            }
            StringBuffer resultSb = new StringBuffer();
            resultSb = commandHandler.telnet(channel, commandParts, 0, resultSb);
            return resultSb.toString();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return msg;
    }

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
        throws Exception
    {
//        super.handleUpstream(ctx, e);
        if (e instanceof ChannelStateEvent)
        {
            System.out.println(e.toString());
        }
        super.handleUpstream(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
        throws Exception
    {
        StringBuffer msgSb = new StringBuffer();
        msgSb.append("   ***                                                                       \r\n");
        msgSb.append("  ** **                          sssssK  ssssss              ss              \r\n");
        msgSb.append(" **   **                         s       ss                  ss              \r\n");
        msgSb.append(" **   **         ****            s       ss     f,   ff   ff ss   ff         \r\n");
        msgSb.append(" **   **       **   ****         s       ss     ss   ss   s: ss  ss          \r\n");
        msgSb.append(" **  **       *   **   **        sssss   ssssss  s   ss   s  ss ss           \r\n");
        msgSb.append("  **  *      *  **  ***  **      stttt   ssssss  s  sjsK :s  ssss            \r\n");
        msgSb.append("   **  *    *  **     **  *      s       ss      ss s  s sE  sss             \r\n");
        msgSb.append("    ** **  ** **        **       s       ss      fs s  s s   ssts            \r\n");
        msgSb.append("    **   **  **                  s       ss       sEs  s s   ss Ws           \r\n");
        msgSb.append("   *           *                 ssssss  ss       ss   Dss   ss  ss          \r\n");
        msgSb.append("  *             *                ssssss  ss       ss    s    ss   ss         \r\n");
        msgSb.append(" *    0     0    *                                                           \r\n");
        msgSb.append(" *   /   @   \\   *                                                           \r\n");
        msgSb.append(" *   \\__/ \\__/   *                                                           \r\n");
        msgSb.append("   *     W     *                                                             \r\n");
        msgSb.append("     **     **                                                               \r\n");
        msgSb.append("====================== Welcome to console!======================\r\n");
        msgSb.append(ITelnetCommandHandler.PROMPT);
        ChannelBuffer buffer = ChannelBuffers.buffer(msgSb.length());
        //buffer.writeBytes(msgSb.toString().getBytes());
        byte[] bytes = {(byte)255, (byte)251, (byte)3};
        buffer.writeBytes(bytes);
        e.getChannel().write(buffer);
    }

	public Map<String, ITelnetCommandHandler> getCommand2Handler() 
	{
		return command2Handler;
	}

	public void setCommand2Handler(
			Map<String, ITelnetCommandHandler> command2Handler) 
	{
		this.command2Handler = command2Handler;
	}
    
}
