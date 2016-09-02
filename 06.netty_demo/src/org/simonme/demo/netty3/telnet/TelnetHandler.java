/*
 * 文 件 名:  EFwkTelnetHandler.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved  Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-7-14
 */
package org.simonme.demo.netty3.telnet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-7-14]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TelnetHandler extends SimpleChannelHandler
{
    private static Logger logger = Logger.getLogger(TelnetHandler.class);
    
    private static byte[] UP = new byte[]{(byte)27,(byte)91, (byte)65};
    
    private static byte[] DOWN = new byte[]{(byte)27,(byte)91, (byte)66};
    
    private static byte[] LEFT = new byte[]{(byte)27,(byte)91, (byte)68};
    
    private static byte[] RIGHT = new byte[]{(byte)27,(byte)91, (byte)67};
    
    private static byte[] ENTER1 = new byte[]{(byte)'\r'};
    
    private static byte[] BACKSPACE = new byte[]{(byte)8};
    
    private static byte[] TAB = new byte[]{(byte)9};
    
    private static byte[] ENTER2 = new byte[]{(byte)'\n'};
    
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
            System.out.println("received-:" + receivedBytes[0]);
            appendByteToBuffer(ctx.getChannel().getId(), receivedBytes, ctx.getChannel());
        }
    }
    
    public void fireSendUpStreamDone(ChannelEvent e)
    {
        Channel channel = e.getChannel();
        InputByteBuffer existBuffer = getChannleInputBuffer(channel);
        
        /**
         * 响应协商
         */
        if (existBuffer.negotiationState == NegotiationState.ING)
        {
            if (existBuffer.negotiationBatchNo == 2)
            {
                existBuffer.clear();
                negotiationStep2(channel);
                return;
            }
            if (existBuffer.negotiationBatchNo == 3)
            {
                existBuffer.clear();
                negotiationStep3(channel);
                return;
            }
            if (existBuffer.negotiationBatchNo == 4)
            {
                existBuffer.clear();
                negotiationStep4(channel);
                return;
            }
            if (existBuffer.negotiationBatchNo == 5)
            {
                System.out.println("改状态...");
                existBuffer.clear();
                existBuffer.negotiationState = NegotiationState.ED;
                this.writePrompt(channel);
                return;
            }
        }
        
        /**
         * 往前找 ，找到-1 即255的就将当前的到-1的全部clear
         */
        existBuffer.removeFromCurrentToLeft((byte)-1);
        if (existBuffer.negotiationState == NegotiationState.ED)
        {
            if (existBuffer.endsWithFromCurrent(UP))
            {
                existBuffer.removeFromCurrentToLeft(3);
                if (existBuffer.getCommandHistory().size() > 0 && existBuffer.getHistoryIndex() != 0)
                {
                    int historyIndex = -1;
                    if (existBuffer.getHistoryIndex() == -1)
                    {
                        historyIndex = existBuffer.getCommandHistory().size() - 1;
                        existBuffer.setHistoryIndex(historyIndex);
                    }
                    else
                    {
                        historyIndex = existBuffer.getHistoryIndex() - 1;
                        existBuffer.setHistoryIndex(historyIndex);
                    }
                    if (existBuffer.getHistoryIndex() >= 0 && existBuffer.getHistoryIndex() < existBuffer.getCommandHistory().size())
                    {
                        String cmd = existBuffer.getCommandHistory().get(existBuffer.getHistoryIndex());
                        existBuffer.clear();
                        existBuffer.append(cmd.getBytes());
                        String toWriteStr = "\r\n" + ITelnetCommandHandler.PROMPT + cmd;
                        byte[] toWrite = toWriteStr.getBytes();
                        ChannelBuffer buffer = ChannelBuffers.buffer(toWrite.length);
                        buffer.writeBytes(toWrite);
                        channel.write(buffer);
                    }
                }
                return;
            }
            if (existBuffer.endsWithFromCurrent(DOWN))
            {
                existBuffer.removeFromCurrentToLeft(3);
                if (existBuffer.getCommandHistory().size() > 0 && existBuffer.getHistoryIndex() != existBuffer.getCommandHistory().size() - 1)
                {
                    if (existBuffer.getHistoryIndex() == -1)
                    {
                        return;
                    }
                    else
                    {
                        existBuffer.setHistoryIndex(existBuffer.getHistoryIndex() + 1);
                    }
                    if (existBuffer.getHistoryIndex() > 0 && existBuffer.getHistoryIndex() < existBuffer.getCommandHistory().size())
                    {
                        String cmd = existBuffer.getCommandHistory().get(existBuffer.getHistoryIndex());
                        existBuffer.clear();
                        existBuffer.append(cmd.getBytes());
                        String toWriteStr = "\r\n" + ITelnetCommandHandler.PROMPT + cmd;
                        byte[] toWrite = toWriteStr.getBytes();
                        ChannelBuffer buffer = ChannelBuffers.buffer(toWrite.length);
                        buffer.writeBytes(toWrite);
                        channel.write(buffer);
                    }
                }
                return;
            }
            if (existBuffer.endsWithFromCurrent(LEFT))
            {
                existBuffer.removeFromCurrentToLeft(3);
                
                /**
                 * 已经到第一个了 就不要再向左了
                 */
                if (existBuffer.countToLeft() == 0 || (existBuffer.countToLeft() == 1 && existBuffer.isZeroCursor))
                {
                    return;
                }
                
                /**
                 * 要把当前位置到末尾的所有字符都写到客户端去，并且补齐相应个数个\b
                 */
                existBuffer.appendOnTailAndNotMoveCurrent((byte)'\b');
                byte[] toWrite = new byte[]{(byte)'\b'};
                ChannelBuffer buffer = ChannelBuffers.buffer(toWrite.length);
                buffer.writeBytes(toWrite);
                channel.write(buffer);
                existBuffer.moveCursorToLeft();
                return;
            }
            if (existBuffer.endsWithFromCurrent(RIGHT))
            {
                existBuffer.removeFromCurrentToLeft(3);
                existBuffer.moveCursorToRight();
                ChannelBuffer buffer = ChannelBuffers.buffer(RIGHT.length);
                buffer.writeBytes(RIGHT);
                channel.write(buffer);
                
                /**
                 * 有一次向右键  就应该在末尾移除一个\b
                 */
                existBuffer.removeTailAndIsTarget((byte)'\b');
                return;
            }
            if (existBuffer.endsWithFromCurrent(ENTER1))
            {
                handleEnter(channel, existBuffer);
                return;
            }
            if (existBuffer.endsWithFromCurrent(ENTER2))
            {
                handleEnter(channel, existBuffer);
                return;
            }
            if (existBuffer.endsWithFromCurrent(TAB))
            {
                handleTab(channel, existBuffer);
                return;
            }
            if (existBuffer.endsWithFromCurrent(BACKSPACE))
            {
                existBuffer.removeFromCurrentToLeft(2);
                byte[] toWrite = new byte[]{(byte)'\b', (byte)27,(byte)91, (byte)75};
                byte[] toWriteInput = existBuffer.toCommandBytes4BackSpaceOP();
                byte[] toWrite1= new byte[toWrite.length + toWriteInput.length];
                System.arraycopy(toWrite, 0, toWrite1, 0, toWrite.length);
                System.arraycopy(toWriteInput, 0, toWrite1, toWrite.length, toWriteInput.length);
                ChannelBuffer buffer = ChannelBuffers.buffer(toWrite1.length);
                buffer.writeBytes(toWrite1);
                channel.write(buffer);
                return;
            }
            writeBuffer2Channel(channel, existBuffer);
        }
    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @param channel
     * @param existBuffer
     * @see [类、类#方法、类#成员]
     */
    private void writeBuffer2Channel(Channel channel, InputByteBuffer existBuffer)
    {
        byte[] toWrite = existBuffer.toCommandBytesFromCurrent();
        ChannelBuffer buffer = ChannelBuffers.buffer(toWrite.length);
        buffer.writeBytes(toWrite);
        channel.write(buffer);
    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @param channel
     * @see [类、类#方法、类#成员]
     */
    private InputByteBuffer getChannleInputBuffer(Channel channel)
    {
        InputByteBuffer existBuffer = channelId2Buffer.get(channel.getId());
        return existBuffer;
    }

    private synchronized void appendByteToBuffer(Integer id, byte[] receivedBytes, Channel channel)
    {
        if (id == null || receivedBytes == null || receivedBytes.length == 0)
        {
            return;
        }
        InputByteBuffer existBuffer = channelId2Buffer.get(id);
        for (byte b : receivedBytes)
        {
            existBuffer.append(b);
        }
    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @param channel
     * @param existBuffer
     * @see [类、类#方法、类#成员]
     */
    private void writePrompt(Channel channel)
    {
        StringBuffer msgSb = new StringBuffer();
        msgSb.append("====================== Welcome to console!======================\r\n");
        msgSb.append(ITelnetCommandHandler.PROMPT);
        ChannelBuffer buffer = ChannelBuffers.buffer(msgSb.length());
        buffer.writeBytes(msgSb.toString().getBytes());
        channel.write(buffer);
    }
    
    private void handleTab(Channel channel, InputByteBuffer existBuffer)
    {
        existBuffer.removeFromCurrentToLeft(1);
        
        /**
         * TODO:补全tab相应信息
         */
    }

    private void handleEnter(Channel channel, InputByteBuffer existBuffer)
    {
        existBuffer.removeFromTailToLeftAndIsTarget((byte)'\b');
        existBuffer.setHistoryIndex(-1);
        
        /**
         * 去掉刚进来的回车字符
         */
        existBuffer.removeFromCurrentToLeft(1);
        String cmd = existBuffer.toCommandString();
        if (!(cmd == null || "".equals(cmd)))
        {
            existBuffer.getCommandHistory().add(cmd);
        }
        System.out.println("Command is:" + cmd);
        existBuffer.clear();
        String toWriteStr = "\r\n" + ITelnetCommandHandler.PROMPT;
//        existBuffer.append(toWriteStr.getBytes());
        byte[] toWrite = toWriteStr.getBytes();
        ChannelBuffer buffer = ChannelBuffers.buffer(toWrite.length);
        buffer.writeBytes(toWrite);
//        existBuffer.append(toWriteStr.getBytes());
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
    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
        throws Exception
    {
        super.handleDownstream(ctx, e);
    }

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
        throws Exception
    {
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
        InputByteBuffer existBuffer = channelId2Buffer.get(e.getChannel().getId());
        if(existBuffer == null)
        {
            existBuffer = new InputByteBuffer();
            channelId2Buffer.put(e.getChannel().getId(), existBuffer);
        }
        negotiationStep1(e.getChannel());
    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @param e
     * @see [类、类#方法、类#成员]
     */
    private void negotiationStep1(Channel channel)
    {
        byte[] bytes = {
            (byte)0xff,(byte)0xfd,(byte)0x18,
            (byte)0xff,(byte)0xfd,(byte)0x20,
            (byte)0xff,(byte)0xfd,(byte)0x23,
            (byte)0xff,(byte)0xfd,(byte)0x27
            };
        ChannelBuffer buffer = ChannelBuffers.buffer(bytes.length);
        buffer.writeBytes(bytes);
        channel.write(buffer);
        InputByteBuffer existBuffer = getChannleInputBuffer(channel);
        existBuffer.negotiationState = NegotiationState.ING;
        existBuffer.negotiationBatchNo++;
        System.out.println("协商1完成...");
    }
    
    private void negotiationStep2(Channel channel)
    {
        byte[] bytes = {
            (byte)0xff,(byte)0xfa,(byte)0x20,(byte)0x01,
            (byte)0xff,(byte)0xf0,
            (byte)0xff,(byte)0xfa,(byte)0x23,(byte)0x01,
            (byte)0xff,(byte)0xf0,
            (byte)0xff,(byte)0xfa,(byte)0x27,(byte)0x01,
            (byte)0xff,(byte)0xf0,
            (byte)0xff,(byte)0xfa,(byte)0x18,(byte)0x01,
            (byte)0xff,(byte)0xf0
            };
        ChannelBuffer buffer = ChannelBuffers.buffer(bytes.length);
        buffer.writeBytes(bytes);
        channel.write(buffer);
        InputByteBuffer existBuffer = getChannleInputBuffer(channel);
        existBuffer.negotiationBatchNo++;
        System.out.println("协商2完成...");
    }
    
    private void negotiationStep3(Channel channel)
    {
        byte[] bytes = {
            (byte)0xff, (byte)0xfb, (byte)0x03,
            (byte)0xff, (byte)0xfd, (byte)0x1,
            (byte)0xff, (byte)0xfd, (byte)0x1f,
            (byte)0xff, (byte)0xfb, (byte)0x05,
            (byte)0xff, (byte)0xfd, (byte)0x21,
            //(byte)0xff, (byte)0xfb, (byte)0x01
            };
        ChannelBuffer buffer = ChannelBuffers.buffer(bytes.length);
        buffer.writeBytes(bytes);
        channel.write(buffer);
        InputByteBuffer existBuffer = getChannleInputBuffer(channel);
        existBuffer.negotiationBatchNo++;
        System.out.println("协商3完成...");
    }
    
    private void negotiationStep4(Channel channel)
    {
        byte[] bytes = {
            (byte)0xff, (byte)0xfb, (byte)0x01
            };
        ChannelBuffer buffer = ChannelBuffers.buffer(bytes.length);
        buffer.writeBytes(bytes);
        channel.write(buffer);
        InputByteBuffer existBuffer = getChannleInputBuffer(channel);
        existBuffer.negotiationBatchNo++;
        System.out.println("协商4完成...");
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
	
	public static enum NegotiationState
	{
	    
	    /**
	     * 未开始
	     */
	    INIT,
	    
	    /**
	     * 进行中
	     */
	    ING,
	    
	    /**
	     * 结束了
	     */
	    ED
	}
	
	public static class InputByteBuffer
	{
	    
	    private ByteBufferNode header;
	    
	    private ByteBufferNode current;
	    
	    /**
	     * 协议协商是否完成
	     */
	    private NegotiationState negotiationState = NegotiationState.INIT;
	    
	    private int negotiationBatchNo = 1;
	    
	    private int historyIndex = -1;
	    
	    private List<String> commandHistory = new ArrayList<String>();
	    
	    /**
	     * 最顶端的，  比如abc 我现在要在a前面输入 那么就有顶端这个说法
	     */
	    private boolean isZeroCursor = true;
	    
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
	        else
	        {
	            isZeroCursor = true;
	        }
	    }
	    
	    public void moveCursorToRight()
	    {
	        if (current.next != null)
	        {
	            current = current.next;
	        }
	    }
	    
	    public void appendOnTailAndNotMoveCurrent(byte b)
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
                ByteBufferNode tail = findTail();
                tail.next = next;
                next.previous = tail;
            }
        }
	    
	    public void appendAndNotMoveCurrent(byte b)
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
                next.next = current.next;
                if(current.next != null)
                {
                    current.next.previous = next;
                }
                next.previous = current;
                current.next = next;
            }
        }
	    
	    public void append(byte b)
	    {
	        if (current == null)
	        {
	            if (header == null)
	            {
	                header = new ByteBufferNode();
	                isZeroCursor = false;
	            }
	            current = header;
	            current.setBuffer(b);
	        }
	        else
	        {
	            ByteBufferNode next = new ByteBufferNode();
	            next.setBuffer(b);
	            if (isZeroCursor)
	            {
	                next.next = header;
	                header.previous = next;
	                header = next;
	                current = header;
	                isZeroCursor = false;
	                return;
	            }
	            next.next = current.next;
	            if(current.next != null)
	            {
	                current.next.previous = next;
	            }
	            next.previous = current;
	            current.next = next;
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
	    
	    public boolean startsWith(byte[] bytes)
        {
            ByteBufferNode tail = findTail();
            if (tail == null)
            {
                return false;
            }
            ByteBufferNode cursorNode = header;
            int length = bytes.length;
            for (int i = 0; i < length; i++)
            {
                if (cursorNode == null || cursorNode.getBuffer() != bytes[i])
                {
                    return false;
                }
                cursorNode = cursorNode.next;
            }
            return true;
        }
	    
	    public boolean endsWithFromCurrent(byte[] bytes)
        {
            ByteBufferNode tail = current;
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
	    
	    public ByteBufferNode findToLeft(byte b)
        {
	        ByteBufferNode temp = this.current;
	        while (temp !=  null)
            {
                if (temp.getBuffer() == b)
                {
                    return temp;
                }
                temp = temp.previous;
            }
	        return null;
        }
	    
	    public void removeFromCurrentToLeft(byte b)
        {
	        ByteBufferNode target = findToLeft(b);
	        if (target == null)
	        {
	            return;
	        }
	        ByteBufferNode temp = this.current;
	        while (temp !=  null)
            {
	            boolean found = false;
	            if (temp == target)
	            {
	                found = true;
	                if (temp == header)
	                {
	                    isZeroCursor = true;
	                }
	            }
	            if (current.next != null)
	            {
	                current.next.previous = current.previous;
	            }
	            if (current.previous != null)
	            {
	                current.previous.next = current.next;
	            }
	            current = current.previous;
	            temp = current;
	            if (found)
	            {
	                break;
	            }
            }
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
	            if (oldPrevious != null)
	            {
	                oldPrevious.next = oldNext;
	            }
	            if (oldNext != null)
	            {
	                oldNext.previous = oldPrevious;
	            }
	            current.previous = null;
	            current.next = null;
	            current = oldPrevious;
	        }
	        if (current == null)
	        {
	            isZeroCursor = true;
	        }
	    }
	    
	    public void removeTailAndIsTarget(byte b)
	    {
	        ByteBufferNode tail = this.findTail();
	        if (tail == null || tail.getBuffer() != b)
	        {
	            return;
	        }
	        if (tail.previous == null)
	        {
	           header = null;
	           return;
	        }
	        tail.previous.next = null;
	        tail.previous = null;
	        tail = null;
	    }
	    
	    public void removeFromTailToLeftAndIsTarget(byte b)
        {
            ByteBufferNode tail = this.findTail();
            while (tail != null && tail.getBuffer() == b)
            {
                ByteBufferNode toRemoveTail = tail;
                tail = tail.previous;
                
                // 同时移动当前节点
                if (this.current == toRemoveTail)
                {
                    this.current = tail;
                }
                toRemoveTail.previous = null;
                if (tail != null)
                {
                    tail.next = null;
                }
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
	    
	    public int countToRightAndIsTarget(byte target)
        {
            int count = 0;
            ByteBufferNode temp = current;
            while (temp != null && target != temp.getBuffer())
            {
                count = count + 1;
                temp = temp.next;
            }
            return count;
        }
	    
	    public int countToRight()
        {
            int count = 0;
            ByteBufferNode temp = current;
            while (temp != null)
            {
                count = count + 1;
                temp = temp.next;
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
	    
	    /**
	     * <一句话功能简述> 从当前节点向后组byte<br/>
	     * <功能详细描述>
	     * @return
	     * @see [类、类#方法、类#成员]
	     */
	    public byte[] toCommandBytesFromCurrent()
        {
            int count = countToRight();
            byte[] bytes = new byte[count];
            ByteBufferNode temp = current;
            int i = 0;
            while (temp != null)
            {
                bytes[i] = temp.getBuffer();
                temp = temp.next;
                i++;
            }
            return bytes;
        }
	    
	    public byte[] toCommandBytes4BackSpaceOP()
        {
            int count = countToRight() - 1;
            byte[] bytes = new byte[count];
            if (count == 0 || count == -1)
            {
                return bytes;
            }
            ByteBufferNode temp = current.next;
            int i = 0;
            while (temp != null)
            {
                bytes[i] = temp.getBuffer();
                temp = temp.next;
                i++;
            }
            return bytes;
        }
	    
	    public byte[] toCommandBytes()
        {
            int count = count();
            byte[] bytes = new byte[count];
            if (count == 0)
            {
                return bytes;
            }
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

        public int getHistoryIndex()
        {
            return historyIndex;
        }

        public void setHistoryIndex(int historyIndex)
        {
            this.historyIndex = historyIndex;
        }

        public List<String> getCommandHistory()
        {
            return commandHistory;
        }

        public void setCommandHistory(List<String> commandHistory)
        {
            this.commandHistory = commandHistory;
        }
	    
	}
    
}
