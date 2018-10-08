package com.taobao.zeus.util;

import com.taobao.zeus.model.JobResult;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by tengdj on 2017/5/31.
 */
public class TimeOutJobQueue {
    private static BlockingQueue<JobResult> queue = new LinkedBlockingDeque<JobResult>();

    public static void put(JobResult job) throws Exception {
        queue.put(job);
    }

    public static JobResult take() throws Exception {
        return queue.take();
    }
}
