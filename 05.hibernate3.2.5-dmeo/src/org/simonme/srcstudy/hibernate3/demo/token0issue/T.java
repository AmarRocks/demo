/*
 * 文 件 名:  T.java
 * 版    权:   . Copyright 2008-2016,  All rights reserved Beijing Kinchany Science and Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  simon
 * 修改时间:  2017年4月28日
 */
package org.simonme.srcstudy.hibernate3.demo.token0issue;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.simonme.srcstudy.hibernate3.demo.token0issue.bean.Team;
import org.simonme.srcstudy.hibernate3.demo.token0issue.bean.User;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  simon
 * @version  [版本号, 2017年4月28日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class T
{
    
    /** <一句话功能简述>
     * <功能详细描述>
     * @param args
     * @see [类、类#方法、类#成员]
     */
    public static void main(String[] args)
    {
        Configuration cfg = new Configuration().configure();//对于configure()方法，我们可以引入其源代码进行查看，其会在类路  
      //径下自动去加载一个默认的hibernate.cfg.xml文件；如果我们想换其他名字，可以使用其重载方法，具体可以查看其源代码（下载的压缩包中有）  
              SessionFactory factory = cfg.buildSessionFactory();  
              Session session = null;  
              Transaction tx = null;  
              try {  
                  Team t = new Team();
                      t.setTeamName("test1");;
                  User u = new User();
                  u.setName("tt1");
                  session = factory.openSession();  
                  tx = session.beginTransaction();//开启事务  
                  session.save(t);//进行保存  
                  session.save(u);
                  tx.commit();//提交事务  
              } catch (HibernateException e) {  
                  if(tx!=null){  
                      tx.rollback();//回滚事务  
                  }  
                  throw e;//必须抛出异常  
              }finally{  
                  if(session!=null){  
                      session.close();//关闭session  
                  }  
              }  
        
    }
    
}
