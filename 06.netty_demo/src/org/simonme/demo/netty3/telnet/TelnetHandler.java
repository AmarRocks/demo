/*
 * 文 件 名:  EFwkTelnetHandler.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved  Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-7-14
 */
package org.simonme.demo.netty3.telnet;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
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
    
    private static byte[] LEFT = new byte[]{(byte)27,(byte)91, (byte)66};
    
    private static byte[] RIGHT = new byte[]{(byte)27,(byte)91, (byte)66};
    
    private static byte[] FIRST_RESP = new byte[]{(byte)-3, (byte)3};
    
    private static byte[] ENTER1 = new byte[]{(byte)'\r'};
    
    private static byte[] ENTER2 = new byte[]{(byte)'\n'};
    
    private static String p = "P>";
    
    private Map<String, ITelnetCommandHandler> command2Handler = new HashMap();
    
    private Map<Integer, InputByteBuffer> channelId2Buffer = new ConcurrentHashMap<Integer, InputByteBuffer>();
    
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
        if (message instanceof BigEndianHeapChannelBuffer)
        {
            BigEndianHeapChannelBuffer heapByteBuffer = (BigEndianHeapChannelBuffer)message;
            byte[] receivedBytes = heapByteBuffer.array();
            appendByteToBuffer(ctx.getChannel().getId(), receivedBytes, ctx.getChannel());
            ctx.getChannel().getId();
        }
        System.out.println("received:" + message);
    }

    private synchronized void appendByteToBuffer(Integer id, byte[] receivedBytes, Channel channel)
    {
        if (id == null || receivedBytes == null || receivedBytes.length == 0)
        {
            return;
        }
        InputByteBuffer existBuffer = channelId2Buffer.get(id);
        if(existBuffer == null)
        {
            existBuffer = new InputByteBuffer();
            channelId2Buffer.put(id, existBuffer);
        }
        for (byte b : receivedBytes)
        {
            System.out.println(b);
            
            // 心跳包 忽略
            if (b == (byte)-1 || b == (byte)-15)
            {
                continue;
            }
            existBuffer.append(b);
        }
        if (existBuffer.endsWith(UP))
        {
            existBuffer.clear();
            
            // 将上一条命令写入buffer
        }
        if (existBuffer.endsWith(DOWN))
        {
            existBuffer.clear();
            // 将下一条命令写入buffer
        }
        if (existBuffer.endsWith(LEFT))
        {
            existBuffer.removeFromCurrentToLeft(3);
            existBuffer.moveCursorToLeft();
            existBuffer.clear();
            return;
        }
        if (existBuffer.endsWith(RIGHT))
        {
            existBuffer.removeFromCurrentToLeft(3);
            existBuffer.moveCursorToRight();
            existBuffer.clear();
            return;
        }
        if (existBuffer.endsWith(ENTER1))
        {
            handleEnter(channel, existBuffer);
            return;
        }
        if (existBuffer.endsWith(ENTER2))
        {
            handleEnter(channel, existBuffer);
            return;
        }
        if (existBuffer.endsWith(FIRST_RESP))
        {
            existBuffer.clear();
            
            // 将上一条命令写入buffer
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
            buffer.writeBytes(msgSb.toString().getBytes());
            existBuffer.append(ITelnetCommandHandler.PROMPT.getBytes());
            channel.write(buffer);
            return;
        }
        byte[] toWrite = existBuffer.toCommandBytes();
        ChannelBuffer buffer = ChannelBuffers.buffer(1);
        buffer.writeByte(toWrite[toWrite.length - 1]);
        channel.write(buffer);
    }

    private void handleEnter(Channel channel, InputByteBuffer existBuffer)
    {
        existBuffer.clear();
        existBuffer.append(ITelnetCommandHandler.PROMPT.getBytes());
        byte[] toWrite = existBuffer.toCommandBytes();
        ChannelBuffer buffer = ChannelBuffers.buffer(toWrite.length);
        buffer.writeBytes(toWrite);
        existBuffer.append(ITelnetCommandHandler.PROMPT.getBytes());
        channel.write(buffer);
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
        
        ChannelBuffer buffer = ChannelBuffers.buffer(3);
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
	
	public static class ByteBufferNode
	{
	    private byte buffer;
	    
	    private ByteBufferNode next;
	    
	    private ByteBufferNode previous;

        public byte getBuffer()
        {
            return buffer;
        }

        public void setBuffer(byte buffer)
        {
            this.buffer = buffer;
        }

        public ByteBufferNode getNext()
        {
            return next;
        }

        public void setNext(ByteBufferNode next)
        {
            this.next = next;
        }

        public ByteBufferNode getPrevious()
        {
            return previous;
        }

        public void setPrevious(ByteBufferNode previous)
        {
            this.previous = previous;
        }
	}
	
	public static class InputByteBuffer
	{
	    
	    private ByteBufferNode header;
	    
	    private ByteBufferNode current;
	    
	    public void clear()
	    {
	        current = null;
	        header = null;
	    }
	    
	    public void moveCursorToLeft()
	    {
	        if(current.previous != null)
	        {
	            current = current.previous;
	        }
	    }
	    
	    public void moveCursorToRight()
	    {
	        if (current.next != null)
	        {
	            current = current.next;
	        }
	    }
	    
	    public void append(byte b)
	    {
	        if (current == null)
	        {
	            if (header == null)
	            {
	                header = new ByteBufferNode();
	            }
	            current = header;
	            current.setBuffer(b);
	        }
	        else
	        {
	            ByteBufferNode next = new ByteBufferNode();
	            next.setBuffer(b);
	            current.next = next;
	            next.previous = current;
	            current = next;
	        }
	    }
	    
	    public void append(byte[] bytes)
        {
            if (bytes == null)
            {
                return;
            }
            for (byte b : bytes)
            {
                append(b);
            }
        }
	    
	    public boolean endsWith(byte[] bytes)
	    {
	        ByteBufferNode tail = findTail();
	        if (tail == null)
	        {
	            return false;
	        }
	        ByteBufferNode cursorNode = tail;
	        int length = bytes.length;
	        for (int i = length - 1; i >= 0; i--)
	        {
	            if (cursorNode == null || cursorNode.getBuffer() != bytes[i])
	            {
	                return false;
	            }
	            cursorNode = cursorNode.previous;
	        }
	        return true;
	    }
	    
	    public ByteBufferNode findTail()
	    {
	        if (current == null)
	        {
	            return null;
	        }
	        ByteBufferNode temp = current;
	        while (temp != null && temp.next != null)
	        {
	            temp = temp.next;
	        }
	        return temp;
	    }
	    
	    public void removeFromCurrentToLeft(int count)
	    {
	        int countToLeft = countToLeft();
	        if (countToLeft < count)
	        {
	            throw new IllegalArgumentException("向左向移除的个数比到左向还剩的个数多。" + count + "," + countToLeft);
	        }
	        
	        for (int i = 0; i < count; i++)
	        {
	            ByteBufferNode oldPrevious = current.previous;
	            ByteBufferNode oldNext = current.next;
	            oldPrevious.next = oldNext;
	            if (oldNext != null)
	            {
	                oldNext.previous = oldPrevious;
	            }
	            current.previous = null;
	            current.next = null;
	            current = oldPrevious;
	        }
	    }
	    
	    public int countToLeft()
	    {
	        int count = 0;
	        ByteBufferNode temp = current;
	        while (temp != null)
	        {
	            count = count + 1;
	            temp = temp.previous;
	        }
	        return count;
	    }
	    
	    public int count()
        {
            int count = 0;
            ByteBufferNode temp = findTail();
            while (temp != null)
            {
                count = count + 1;
                temp = temp.previous;
            }
            return count;
        }
	    
	    public byte[] toCommandBytes()
        {
            int count = count();
            byte[] bytes = new byte[count];
            ByteBufferNode temp = header;
            int i = 0;
            while (temp != null)
            {
                bytes[i] = temp.getBuffer();
                temp = temp.next;
                i++;
            }
            return bytes;
        }
	    
	    public String toCommandString()
	    {
	        String s = null;
	        try
            {
                s = new String (toCommandBytes(), "utf-8");
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
	        return s;
	    }
	    
        public ByteBufferNode getHeader()
        {
            return header;
        }

        public void setHeader(ByteBufferNode header)
        {
            this.header = header;
        }

        public ByteBufferNode getCurrent()
        {
            return current;
        }

        public void setCurrent(ByteBufferNode current)
        {
            this.current = current;
        }
	    
	}
    
}
