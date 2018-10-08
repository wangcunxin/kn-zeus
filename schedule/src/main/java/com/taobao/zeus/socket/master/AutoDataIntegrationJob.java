package com.taobao.zeus.socket.master;

import com.taobao.zeus.model.JobDescriptor;
import com.taobao.zeus.model.JobHistory;
import com.taobao.zeus.model.JobStatus;
import com.taobao.zeus.store.JobHistoryManager;
import com.taobao.zeus.store.mysql.tool.PersistenceAndBeanConvert;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by tengdj on 2017/2/16.
 */
public class AutoDataIntegrationJob implements Job {

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        JobDataIntegration jobData = (JobDataIntegration) context.getJobDetail().getJobDataMap().get("jobData");
        JobHistoryManager jobHistoryManager = (JobHistoryManager) jobData.getApplicationContext().getBean("jobHistoryManager");

        JobDescriptor jobDescriptor = PersistenceAndBeanConvert.convert(jobData.getJob()).getX();
        JobHistory history = new JobHistory();
        history.setJobId(jobDescriptor.getId());
        history.setTriggerType(JobStatus.TriggerType.SCHEDULE);
        history.setStatisEndTime(jobDescriptor.getStatisEndTime());
        history.setTimezone(jobDescriptor.getTimezone());
        history.setCycle(jobDescriptor.getCycle());
        history.setExecuteHost(jobDescriptor.getHost());
        JobHistory a = jobHistoryManager.addJobHistory(history);
        jobData.getMaster().run(history);
    }
}
