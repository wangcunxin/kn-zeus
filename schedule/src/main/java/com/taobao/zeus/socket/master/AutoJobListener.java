package com.taobao.zeus.socket.master;

import com.taobao.zeus.schedule.mvc.JobController;
import com.taobao.zeus.schedule.mvc.ScheduleInfoLog;
import com.taobao.zeus.store.mysql.MysqlGroupManager;
import com.taobao.zeus.store.mysql.persistence.JobPersistence;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.springframework.context.ApplicationContext;
import redis.clients.jedis.JedisPubSub;
import org.quartz.Scheduler;

import java.util.Calendar;

/**
 * Created by tengdj on 2017/2/16.
 */
public class AutoJobListener extends JedisPubSub {

    private Scheduler scheduler;
    private Master master;
    private ApplicationContext applicationContext;
    private final int DEPENDENT_TYPE = 1;

    public AutoJobListener(Scheduler _scheduler, Master _master, ApplicationContext _applicationContext) {
        this.scheduler = _scheduler;
        this.master = _master;
        this.applicationContext = _applicationContext;
    }

    /**
     * 消息回调接口
     * @param channel
     * @param jobId
     */
    public void onMessage(String channel, String jobId) {
        try {
            ScheduleInfoLog.info("[Data Integration] receive redis channel message, job id: " + jobId);

            MysqlGroupManager jobManager = (MysqlGroupManager) applicationContext.getBean("groupManager");
            JobPersistence job = jobManager.getJobPersistence(jobId);

            if ( 1==1 || job.getScheduleType().intValue() == DEPENDENT_TYPE) {
//                JobController controller=new JobController(master.getContext(), master, jobId);
//                master.getContext().getDispatcher().addController(controller);
//            } else {
                // 修复自动调度的开关
                JobController controller=new JobController(master.getContext(), master, jobId);
                master.getContext().getDispatcher().addController(controller);

                JobDataIntegration jobData = new JobDataIntegration(job, master, applicationContext);

                Calendar c = Calendar.getInstance();
                c.add(Calendar.SECOND, 3);
                int second = c.get(Calendar.SECOND);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int min = c.get(Calendar.MINUTE);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;

                // 自定义定义表达式执行
                String str = second + " " + min + " " + hour + " " + day + " " + month + " ? " + year;
                ScheduleInfoLog.info("from channel: jobId " + jobId + ", cronexpression: " + str );
                // CronTrigger trigger = new CronTrigger(job.getId().toString(), "zeus", job.getCronExpression());
                CronTrigger trigger = new CronTrigger(job.getId().toString(), "zeus", str);

                JobDetail detail = new JobDetail(job.getId().toString(), "zeus", AutoDataIntegrationJob.class);

                detail.getJobDataMap().put("jobData", jobData);

                scheduler.scheduleJob(detail, trigger);
            }

            ScheduleInfoLog.info("[Data Integration] JobId:" + jobId
                    + " is scheduled successfully");

        } catch (Exception e) {
            ScheduleInfoLog.error("[Data Integration] JobId:" + jobId + " failed to schedule", e);
        }
    }

    public static void main(String[] args) {
//        Calendar c = Calendar.getInstance();
//        System.out.println(c.get(Calendar.SECOND));
//        System.out.println(c.get(Calendar.DAY_OF_MONTH));
//        System.out.println(c.get(Calendar.MINUTE));
//        System.out.println(c.get(Calendar.HOUR_OF_DAY));
//        System.out.println(c.get(Calendar.YEAR));
//        System.out.println(c.get(Calendar.MONTH) + 1);
//
//        System.out.println("----");
//        c.add(Calendar.SECOND, 1);
//        System.out.println(c.get(Calendar.SECOND));
//        System.out.println(c.get(Calendar.DAY_OF_MONTH));
//        System.out.println(c.get(Calendar.MINUTE));
//        System.out.println(c.get(Calendar.HOUR_OF_DAY));
//        System.out.println(c.get(Calendar.YEAR));
//        System.out.println(c.get(Calendar.MONTH) + 1);
//
//
//
//        int second = c.get(Calendar.SECOND);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        int min = c.get(Calendar.MINUTE);
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH) + 1;
//
//
//
//        String str = second + " " + min + " " + hour + " " + day + " " + month + " ? " + year;
//        System.out.println(str);

        String str = "lib_base=/data/www/software/rj/data/works/jars\n" +
                "jar_base=/data/www/software/rj/data/works/hc/detail\n" +
                "param_one=$(date -d \"1 days ago\" +'%Y%m%d')\n" +
                "param_two=/flume/spd/rc_bqs_n/$param_one/*\n" +
                "\n" +
                "echo \"---- \" $param_one \" ----\"\n" +
                "echo \"---- \" $param_two \" ----\"\n" +
                "hadoop fs -rm -r /original_log/tmp_spd/calls/*\n" +
                "hadoop fs -rm -r /original_log/tmp_spd/nets/*\n" +
                "hadoop fs -rm -r /original_log/tmp_spd/msgs/*\n" +
                "spark2-submit \\\n" +
                "    --class com.qsq.rc.data.detail.DetailParse \\\n" +
                "        --master yarn \\\n" +
                "        --deploy-mode client \\\n" +
                "        --num-executors 10 \\\n" +
                "        --executor-cores 3 \\\n" +
                "        --executor-memory 8g \\\n" +
                "        --driver-cores 3 \\\n" +
                "        --driver-memory 8g \\\n" +
                "        --conf spark.cores.max=24 \\\n" +
                "        --jars $lib_base/hbase-client-1.2.0-cdh5.8.3.jar,$lib_base/hbase-common-1.2.0-cdh5.8.3.jar,$lib_base/hbase-protocol-1.2.0-cdh5.8.3.jar,$lib_base/htrace-core.jar,$lib_base/hive-hbase-handler-1.1.0-cdh5.8.3.jar,$lib_base/hbase-server-1.2.0-cdh5.8.3.jar,$lib_base/metrics-core-2.2.0.jar \\\n" +
                "        $jar_base/qspark.jar \\\n" +
                "        yarn $param_two \"\" bqs";
        System.out.println(str.replace("$param_one", "tsp1").replace("$param_two", "tsp2"));


    }
}
