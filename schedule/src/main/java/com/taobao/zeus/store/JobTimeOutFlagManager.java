package com.taobao.zeus.store;

import com.taobao.zeus.model.JobTimeOutFlag;

import java.util.List;

/**
 * Created by tengdj on 2017/6/26.
 */
public interface JobTimeOutFlagManager {

    public void addTimeoutJobFlag(JobTimeOutFlag jobTimeOutFlag);

    public List<JobTimeOutFlag> getJobTimeOutFlagList(String jobId, String timeoutDateStr);
}
