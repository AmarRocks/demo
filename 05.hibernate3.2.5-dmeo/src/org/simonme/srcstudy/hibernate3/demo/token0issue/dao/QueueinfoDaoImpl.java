/*
 * 文 件 名:  QueueinfoDaoImpl.java
 * 版    权:   . Copyright 2008-2015,  All rights reserved Hiaward Information Technology Co.,Ltd.
 * 描    述:  <描述>
 * 修 改 人:  chen.simon
 * 修改时间:  2016-7-25
 */
package org.simonme.srcstudy.hibernate3.demo.token0issue.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version  [版本号, 2016-7-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class QueueinfoDaoImpl extends HibernateDaoSupport
{
    public void queryAndUpate()
    {
        final String querySql = "SELECT ID, NAME,STATUS,row change token for QUEUEINFO e,DTPRINTTIME, BEGINTIME,ENDTIME,CELLPHONE,PREFIX,ORGNUM FROM EFWK_DEMO.QUEUEINFO where status <> 1";
        this.getHibernateTemplate().execute(new HibernateCallback()
        {
            
            @Override
            public Object doInHibernate(Session session)
                throws HibernateException, SQLException
            {
                Query query = session.createQuery(querySql);
                List queryRes = query.list();
                System.out.println(queryRes);
                return null;
            }
        });
    }
}
