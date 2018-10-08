package com.taobao.zeus.util;

import com.taobao.zeus.model.JobDescriptor;
import com.taobao.zeus.model.JobResult;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by tengdj on 2017/5/4.
 */
public class JobBlockingQueue {

//    private static BlockingQueue<JobDescriptor> queue = new LinkedBlockingDeque<JobDescriptor>();
//
//    public static void put(JobDescriptor job) throws Exception {
//        queue.put(job);
//    }
//
//    public static JobDescriptor take() throws Exception {
//        return queue.take();
//    }

    // 2017-05-09
    private static BlockingQueue<JobResult> queue = new LinkedBlockingDeque<JobResult>();

    public static void put(JobResult job) throws Exception {
        queue.put(job);
    }

    public static JobResult take() throws Exception {
        return queue.take();
    }
}
