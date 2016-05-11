/*
 * 文 件 名:  Test000101.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-5-11
 */
package org.simonme.mt.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author Chenxiaguang
 * @version [版本号, 2016-5-11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Test000101
{
    
    public static void main(String[] args)
    {
        execute(provideThreadPoolTaskExecutor());
        execute(provideSyncTaskExecutor());
    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private static void execute(TaskExecutor taskESExecutor)
    {
        System.out.println(taskESExecutor);
        Callable<String> run = new Callable<String>()
        {
            public String call()
            {
                return runTask();
            }
        };
        
        FutureTask<String> task = new FutureTask<String>(run);
        taskESExecutor.execute(task);
        System.out.println("Before get");
        
        /**
         * 下面的get 利用了FutureTask的阻塞特性，使得主线程等待 TaskExecutor异步执行完后拿到结果<br/>
         * 此处虽说用的是异步的TaskExecutor，但是拿结果的动作还是同步的<br/>
         */
        try
        {
            task.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        System.out.println("After get");
    }
    
    public static TaskExecutor provideThreadPoolTaskExecutor()
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-0001.xml");
        TaskExecutor taskESExecutor000101 = (TaskExecutor)applicationContext.getBean("taskESExecutor000101");
        return taskESExecutor000101;
    }
    
    public static TaskExecutor provideSyncTaskExecutor()
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-0001.xml");
        TaskExecutor taskESExecutor000102 = (TaskExecutor)applicationContext.getBean("taskESExecutor000102");
        return taskESExecutor000102;
    }
    
    private static String runTask()
    {
        try
        {
            Thread.sleep(2 * 1000l);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        String result = "Finish";
        System.out.println(result);
        return result;
    }
    
}
