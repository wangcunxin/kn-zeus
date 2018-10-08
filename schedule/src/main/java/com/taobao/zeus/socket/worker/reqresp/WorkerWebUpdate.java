package com.taobao.zeus.socket.worker.reqresp;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import com.taobao.zeus.socket.SocketLog;
import com.taobao.zeus.socket.master.AtomicIncrease;
import com.taobao.zeus.socket.protocol.Protocol.ExecuteKind;
import com.taobao.zeus.socket.protocol.Protocol.Response;
import com.taobao.zeus.socket.protocol.Protocol.SocketMessage;
import com.taobao.zeus.socket.protocol.Protocol.WebOperate;
import com.taobao.zeus.socket.protocol.Protocol.WebRequest;
import com.taobao.zeus.socket.protocol.Protocol.WebResponse;
import com.taobao.zeus.socket.protocol.Protocol.SocketMessage.Kind;
import com.taobao.zeus.socket.worker.WorkerContext;
import com.taobao.zeus.socket.worker.WorkerHandler.ResponseListener;

public class WorkerWebUpdate {

	public Future<WebResponse> execute(final WorkerContext context,String jobId){

        // SocketLog.info("#### WorkerWebUpdate ####");
        final WebRequest req=WebRequest.newBuilder().setRid(AtomicIncrease.getAndIncrement()).setOperate(WebOperate.UpdateJob)
			.setEk(ExecuteKind.ManualKind)//此次无用，随便设置一个
			.setId(jobId).build();
		SocketMessage sm=SocketMessage.newBuilder().setKind(Kind.WEB_REUQEST).setBody(req.toByteString()).build();
        // SocketLog.info("#### WorkerWebUpdate #### 2");
        Future<WebResponse> f=context.getThreadPool().submit(new Callable<WebResponse>() {
			private WebResponse response;
			public WebResponse call() throws Exception {
				final CountDownLatch latch=new CountDownLatch(1);
				context.getHandler().addListener(new ResponseListener() {
					public void onWebResponse(WebResponse resp) {

                        // SocketLog.info("#### WorkerWebUpdate #### resp.getRid():" + resp.getRid() + ", req.getRid:" + req.getRid());
                        if(resp.getRid()== req.getRid()){
                            context.getHandler().removeListener(this);
                            response=resp;
                            latch.countDown();

                        }
					}
					public void onResponse(Response resp) {}
				});

                // SocketLog.info("#### WorkerWebUpdate #### 3");
				latch.await();
                // SocketLog.info("#### WorkerWebUpdate #### 4");
				return response;
			}
		});
        // SocketLog.info("#### WorkerWebUpdate #### 5");
		context.getServerChannel().write(sm);
		SocketLog.info("send web update to master,rid="+req.getRid()+",jobId="+jobId);
		return f;
	}
}
