package com.taobao.zeus.store.mysql.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by tengdj on 2017/6/26.
 */

@Entity(name="zeus_job_timeout")
public class JobTimeOutFlagPersistence implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="job_id")
    private Long jobId;

    @Column(name="timeout_date")
    private String timeoutDate;

    public Long getId() {
        return id;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getTimeoutDate() {
        return timeoutDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public void setTimeoutDate(String timeoutDate) {
        this.timeoutDate = timeoutDate;
    }
}
