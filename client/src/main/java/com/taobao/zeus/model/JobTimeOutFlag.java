package com.taobao.zeus.model;

/**
 * Created by tengdj on 2017/6/26.
 */
public class JobTimeOutFlag {
    private Long id;
    private Long jobId;
    private String timeoutDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getTimeoutDate() {
        return timeoutDate;
    }

    public void setTimeoutDate(String timeoutDate) {
        this.timeoutDate = timeoutDate;
    }
}
