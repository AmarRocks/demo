/*
 * 文 件 名:  ByteBufferTest.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved xx Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  mac
 * 修改时间:  2016-8-18
 */
package org.simonme.demo.nio;

import java.nio.ByteBuffer;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-8-18]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ByteBufferTest
{
    
    /** <一句话功能简述>
     * <功能详细描述>
     * @param args
     * @see [类、类#方法、类#成员]
     */
    public static void main(String[] args)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        System.out.println(byteBuffer.limit());//32
        byteBuffer.put((byte)'a');
        byteBuffer.put((byte)'b');
        byteBuffer.put((byte)'c');
        System.out.println(byteBuffer.limit());//32
        System.out.println(byteBuffer.position());//3
        System.out.println(byteBuffer.get());//0
        System.out.println(byteBuffer.position());//4 get一次或者put一次 position就加
        byteBuffer.flip(); // limit = position; position = 0; mark = -1; flip 将position置成0
        System.out.println(byteBuffer.limit());//4
        System.out.println(byteBuffer.position());//0
        System.out.println(byteBuffer.get());//97
        System.out.println(byteBuffer.position());//1
        System.out.println(byteBuffer.get());//98
        byteBuffer.position(0);
        byteBuffer.put((byte)'d');// 写完之后，bytbuffer变成100, 98, 99,  也就是position在哪，buffer就在哪写入
        byteBuffer.put((byte)'e');
        byteBuffer.put((byte)'f');// 写完之后，bytbuffer变成 100, 101, 102 也就是会覆盖之前存在的值
        byteBuffer.put((byte)'g');
        byteBuffer.put((byte)'h');// 超过limit限制虽然还没达到capacity，也会出异常：java.nio.BufferOverflowException
        
        // 另，还有个mark属性 mark()方法用来设定mark=position。  reset()方法 会将position=mark， 相当于position暂存点
        // rewind与flip相似 但是他不改变 limit的值
    }
    
}
