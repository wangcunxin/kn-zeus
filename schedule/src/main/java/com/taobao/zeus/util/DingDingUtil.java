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
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tengdj on 2017/5/3.
 */
public class DingDingUtil {

    private static Logger log= LoggerFactory.getLogger(DingDingUtil.class);

    public static void send(JobResult jobResult) throws Exception {
//        try {
//            JobDescriptor job = jobResult.getJob();
//            HttpClient httpclient = HttpClients.createDefault();
//            HttpPost httppost = new HttpPost(jobResult.getWebhookToken());
//            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
//            String alarmContent = "";
//            if (jobResult.getResultFlag() == 0) {
//                alarmContent = "[zeus任务执行成功] 任务id: " + job.getId() + " 任务名: " +
//                        job.getName() + " 描述: " + job.getDesc() + "。请查看";
//            } else if (jobResult.getIsTimeOut() == 1) {
//                // 超时
//                alarmContent = "[#####zeus任务执行超时#####] 任务id: " + job.getId() + " 任务名: " +
//                        job.getName() + " 描述: " + job.getDesc()  + "。" + jobResult.getTimeoutRemark() + ", 请查找原因";
//            } else {
//                alarmContent = "[*****zeus任务执行失败*****] 任务id: " + job.getId() + " 任务名: " +
//                        job.getName() + " 描述: " + job.getDesc() + "。请检查恢复";
//            }
//
//            JSONObject ddMsgJson = new JSONObject();
//            ddMsgJson.put("msgtype", "text");
//            JSONObject contentJsonObject = new JSONObject();
//            contentJsonObject.put("content", alarmContent);
//            ddMsgJson.put("text", contentJsonObject);
//
//            JSONArray atMobilesArr = new JSONArray();
//            if (null != job.getResponsibleMail() && !"".equals(job.getResponsibleMail().trim())) {
//                String[] receiverArr = job.getResponsibleMail().trim().split(",");
//                for(String receiver: receiverArr) {
//                    String phoneStr = DingDingConfig.mailPhoneMap.get(receiver);
//                    if (null != phoneStr && !"".equals(phoneStr)) {
//                        atMobilesArr.put(phoneStr);
//                    }
//                }
//            }
//
//            if (atMobilesArr.length() != 0) {
//                JSONObject atMobilesObj = new JSONObject();
//                atMobilesObj.put("atMobiles", atMobilesArr);
//                atMobilesObj.put("isAtAll", false);
//                ddMsgJson.put("at", atMobilesObj);
//            }
//
//            StringEntity se = new StringEntity(ddMsgJson.toString(), "utf-8");
//            httppost.setEntity(se);
//
//            HttpResponse response = httpclient.execute(httppost);
//
//            log.info("send dingding message result: " + EntityUtils.toString(response.getEntity()));
//
//            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
//                log.info("send dingding message successfully for zeus id" + job.getId());
//            } else {
//                log.error("send dingding message fails for response status: " + response.getStatusLine().getStatusCode());
//            }
//
//        } catch (Exception e) {
//            log.error("send dingding message fails", e);
//            throw e;
//        }
    }

    public static void send(JobDescriptor job) throws Exception {
//        try {
//            HttpClient httpclient = HttpClients.createDefault();
//            HttpPost httppost = new HttpPost(DingDingConfig.WEBHOOK_TOKEN);
//            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
//            String alarmContent = "[zeus任务执行失败] 任务id: " + job.getId() + " 任务名: " +
//                    job.getName() + " 描述: " + job.getDesc() + "。请检查恢复";
//            JSONObject ddMsgJson = new JSONObject();
//            ddMsgJson.put("msgtype", "text");
//            JSONObject contentJsonObject = new JSONObject();
//            contentJsonObject.put("content", alarmContent);
//            ddMsgJson.put("text", contentJsonObject);
//
//            JSONArray atMobilesArr = new JSONArray();
//            if (null != job.getResponsibleMail() && !"".equals(job.getResponsibleMail().trim())) {
//                String[] receiverArr = job.getResponsibleMail().trim().split(",");
//                for(String receiver: receiverArr) {
//                    String phoneStr = DingDingConfig.mailPhoneMap.get(receiver);
//                    if (null != phoneStr && !"".equals(phoneStr)) {
//                        atMobilesArr.put(phoneStr);
//                    }
//                }
//            }
//
//            if (atMobilesArr.length() != 0) {
//                JSONObject atMobilesObj = new JSONObject();
//                atMobilesObj.put("atMobiles", atMobilesArr);
//                atMobilesObj.put("isAtAll", false);
//                ddMsgJson.put("at", atMobilesObj);
//            }
//
//            StringEntity se = new StringEntity(ddMsgJson.toString(), "utf-8");
//            httppost.setEntity(se);
//
//            HttpResponse response = httpclient.execute(httppost);
//
//            log.info("send dingding message result: " + EntityUtils.toString(response.getEntity()));
//
//            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
//                String result= EntityUtils.toString(response.getEntity());
//                log.info("send dingding message successfully for zeus id" + job.getId());
//            } else {
//                log.error("send dingding message fails for response status: " + response.getStatusLine().getStatusCode());
//            }
//
//        } catch (Exception e) {
//            log.error("send dingding message fails", e);
//            throw e;
//        }
    }

    public static void main(String[] args) throws Exception {
        JobDescriptor job = new JobDescriptor();
        job.setId("2138");
        job.setResponsibleMail("wanghaichao@kuainiugroup.com");
        job.setName("prod_derivativevariables_jxl");
        send(job);
    }
}
