package com.taobao.zeus.util;

import com.taobao.zeus.model.JobDescriptor;
import com.taobao.zeus.model.JobResult;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 百度告警平台
 * Created by tengdj on 2017/6/14.
 */
public class BaiduAlarmPlatform {
    private static Logger log= LoggerFactory.getLogger(BaiduAlarmPlatform.class);

//    public static void alarm(JobResult jobResult) {
//        try {
//            JobDescriptor job = jobResult.getJob();
//            HttpClient httpclient = HttpClients.createDefault();
//            HttpPost httppost = new HttpPost(BaiduAlarmConfig.URL);
//            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
//            httppost.addHeader("servicekey", BaiduAlarmConfig.SERVICE_KEY);
//
//            String responsilePersons = "";
//            if (null != job.getResponsibleMail() && !"".equals(job.getResponsibleMail().trim())) {
//                String[] receiverArr = job.getResponsibleMail().trim().split(",");
//                for(String receiver: receiverArr) {
//                    String nameStr = BaiduAlarmConfig.mailPersonMap.get(receiver);
//                    if (null != nameStr && !"".equals(nameStr)) {
//                        responsilePersons += nameStr + ",";
//                    }
//                }
//            }
//
//            if (null != responsilePersons && !"".equals(responsilePersons.trim())) {
//                responsilePersons = responsilePersons.substring(0, responsilePersons.length() - 1);
//            }
//
//            String alarmContent = "";
//            if (jobResult.getResultFlag() == 0) {
//                alarmContent = "[zeus任务执行成功] 任务id: " + job.getId() + " 任务名: " +
//                        job.getName() + " 描述: " + job.getDesc() + "。请查看" +
//                        ((null != responsilePersons && !"".equals(responsilePersons.trim())) ? "，责任人：" + responsilePersons : "");
//            } else if (jobResult.getIsTimeOut() == 1) {
//                // 超时
//                alarmContent = "[zeus任务执行超时] 任务id: " + job.getId() + " 任务名: " +
//                        job.getName() + " 描述: " + job.getDesc() + "。" + jobResult.getTimeoutRemark() + ", 请查找原因" +
//                        ((null != responsilePersons && !"".equals(responsilePersons.trim())) ? "，责任人：" + responsilePersons : "");
//            } else {
//                alarmContent = "[zeus任务执行失败] 任务id: " + job.getId() + " 任务名: " +
//                        job.getName() + " 描述: " + job.getDesc() + "。请检查恢复" +
//                        ((null != responsilePersons && !"".equals(responsilePersons.trim())) ? "，责任人：" + responsilePersons : "");
//            }
//
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("service_id", BaiduAlarmConfig.SERVICE_ID);
//            jsonObj.put("event_type", "trigger");
//            jsonObj.put("description", alarmContent);
//
//            StringEntity se = new StringEntity(jsonObj.toString(), "utf-8");
//            httppost.setEntity(se);
//
//            HttpResponse response = httpclient.execute(httppost);
//
//            log.info("send baidu alarm message result: " + EntityUtils.toString(response.getEntity()));
//
//            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
//                log.info("send baidu alarm message successfully for zeus id " + job.getId());
//            } else {
//                log.error("send baidu alarm message fails for response status: " + response.getStatusLine().getStatusCode());
//            }
//        } catch (Exception e) {
//            log.error("send baidu alarm http message fails for zeus id: " + jobResult.getJob().getId(), e);
//        }
//    }

    public static void main(String[] args) {
        JobResult jobResult = new JobResult();
        jobResult.setIsTimeOut(0);
        jobResult.setResultFlag(1);
        JobDescriptor job = new JobDescriptor();
        jobResult.setJob(job);
        job.setName("td_monitor_store");
        job.setResponsibleMail("wanghaichao@kuainiugroup.com,wanghaichao@kuainiugroup.com");
        job.setId("2681");
        job.setDesc("同盾贷后个人资料入库");
        // alarm(jobResult);
    }
}
