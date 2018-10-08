package com.taobao.zeus.socket.master;

import com.taobao.zeus.store.mysql.persistence.JobPersistence;
import org.springframework.context.ApplicationContext;
import java.io.Serializable;

/**
 * Created by tengdj on 2017/2/16.
 */
public class JobDataIntegration implements Serializable {
    private static final long serialVersionUID = 1L;

    private JobPersistence job;
    private Master master;
    private ApplicationContext applicationContext;

    public JobDataIntegration(JobPersistence job, Master master, ApplicationContext applicationContext) {
        this.job = job;
        this.master = master;
        this.applicationContext = applicationContext;
    }

    public JobPersistence getJob() {
        return job;
    }

    public Master getMaster() {
        return master;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
