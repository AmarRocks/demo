/*
 * 文 件 名:  MTTA.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved Hiaward Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  Chenxiaguang
 * 修改时间:  2015-8-26
 */
package org.simonme.srcstudy.spring3.demo.assistant;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * <一句话功能简述> 多线程测试帮助助手<br/>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2015-8-26]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MTTA
{
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param runner 封装了测试逻辑的运行器
     * @param concurrnetCount 要测试并发的个数
     * @see [类、类#方法、类#成员]
     */
    public static void mt(MTRunner runner, int concurrnetCount)
    {
        mt(runner, concurrnetCount, null);
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param runner 封装了测试逻辑的运行器
     * @param concurrnetCount 要测试并发的个数
     * @param runnerContext 测试逻辑中用到的上下文 会传递给测试运行器的run方法
     * @see [类、类#方法、类#成员]
     */
    public static void mt(MTRunner runner, int concurrnetCount, Map<?, ?> runnerContext)
    {
        if(runner == null)
        {
            throw new NullPointerException("runner 必须传递！");
        }
        if(concurrnetCount <= 0)
        {
            throw new IllegalArgumentException("并发数必须大于0");
        }
        final CountDownLatch begin = new CountDownLatch(concurrnetCount); 
        Long[] costs = new Long[concurrnetCount];
        for(int i = 0; i < concurrnetCount; i++)
        {
            RunnerThread gsn = new RunnerThread(runner, begin, i, costs, runnerContext);
            Thread t = new Thread(gsn, runner.getRunnerName() + "-" + i);
            t.start();
        }
        try
        {
            begin.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        for(int j = 0 ; j < costs.length; j++)
        {
            System.out.println(runner.getRunnerName() + "-" + j + "耗时:" + costs[j] + "ms");
        }
    }
    
    
    
    public static interface MTRunner
    {
        
        /**
         * <一句话功能简述>
         * <功能详细描述>
         * @return 运行器的名字
         * @see [类、类#方法、类#成员]
         */
        public String getRunnerName();
        
        /**
         * <一句话功能简述> 测试运行逻辑的封装
         * <功能详细描述>
         * @param runnerContext 测试逻辑需要的上下文
         * @see [类、类#方法、类#成员]
         */
        public void run(Map<?,?> runnerContext);
    }
    
    public static class RunnerThread implements Runnable
    {
        
        private MTRunner runner;
        
        private CountDownLatch countDownLatch;
        
        private int tNo;
        
        private Long[] costs;
        
        private Map<?,?> runnerContext;
        
        public RunnerThread(MTRunner runner, CountDownLatch countDownLatch, int tNo, Long[] costs, Map<?,?> runnerContext)
        {
            this.runner = runner;
            this.countDownLatch = countDownLatch;
            this.costs = costs;
            this.tNo = tNo;
            this.runnerContext = runnerContext;
        }

        @Override
        public void run()
        {
            try
            {
                long start = System.currentTimeMillis();
                runner.run(runnerContext);
                long end = System.currentTimeMillis();
                long cost = end - start;
                costs[tNo] = cost;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                countDownLatch.countDown();
            }
        }
        
    }
}

