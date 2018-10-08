package com.taobao.zeus.store.mysql;

import com.taobao.zeus.model.JobTimeOutFlag;
import com.taobao.zeus.store.JobTimeOutFlagManager;
import com.taobao.zeus.store.mysql.persistence.JobTimeOutFlagPersistence;
import com.taobao.zeus.store.mysql.tool.PersistenceAndBeanConvert;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengdj on 2017/6/26.
 */
public class MysqlJobTimeOutFlagManager extends HibernateDaoSupport implements JobTimeOutFlagManager {

    @Override
    public void addTimeoutJobFlag(JobTimeOutFlag jobTimeOutFlag) {
        JobTimeOutFlagPersistence persist= PersistenceAndBeanConvert.convert(jobTimeOutFlag);
        getHibernateTemplate().save(persist);
    }

    @Override
    public List<JobTimeOutFlag> getJobTimeOutFlagList(final String jobId, final String timeoutDateStr) {
        return (List<JobTimeOutFlag>) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                SQLQuery query=session.createSQLQuery("select id,job_id,timeout_date from zeus_job_timeout where job_id=? and timeout_date=?");
                query.setLong(0, Long.valueOf(jobId));
                query.setString(1, timeoutDateStr);
                List<Object[]> list=query.list();
                List<JobTimeOutFlag> result=new ArrayList<JobTimeOutFlag>();
                for(Object[] o:list){
                    JobTimeOutFlagPersistence p=new JobTimeOutFlagPersistence();
                    p.setId(((Number)o[0]).longValue());
                    p.setJobId(((Number)o[1]).longValue());
                    p.setTimeoutDate((String)o[2]);
                    result.add(PersistenceAndBeanConvert.convert(p));
                }
                return result;
            }
        });
    }
}
