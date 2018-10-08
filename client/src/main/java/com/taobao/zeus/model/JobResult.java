package com.taobao.zeus.model;

import java.io.Serializable;

/**
 * Created by tengdj on 2017/5/10.
 */
public class JobResult implements Serializable {
    private JobDescriptor job;
    private Integer resultFlag; // 0表示成功，非0表示失败
    private String webhookToken;
    private Integer isTimeOut = 0; // 0表示不超时，1表示超时
    private String timeoutRemark = ""; // 超时备注

    public JobDescriptor getJob() {
        return job;
    }

    public Integer getResultFlag() {
        return resultFlag;
    }

    public void setJob(JobDescriptor job) {
        this.job = job;
    }

    public void setResultFlag(Integer resultFlag) {
        this.resultFlag = resultFlag;
    }

    public String getWebhookToken() {
        return webhookToken;
    }

    public void setWebhookToken(String webhookToken) {
        this.webhookToken = webhookToken;
    }

    public Integer getIsTimeOut() {
        return isTimeOut;
    }

    public void setIsTimeOut(Integer isTimeOut) {
        this.isTimeOut = isTimeOut;
    }

    public String getTimeoutRemark() {
        return timeoutRemark;
    }

    public void setTimeoutRemark(String timeoutRemark) {
        this.timeoutRemark = timeoutRemark;
    }
}
