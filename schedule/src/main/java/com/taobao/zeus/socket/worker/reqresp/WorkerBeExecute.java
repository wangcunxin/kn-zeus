package com.taobao.zeus.socket.worker.reqresp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.google.protobuf.InvalidProtocolBufferException;
import com.taobao.zeus.jobs.Job;
import com.taobao.zeus.jobs.JobContext;
import com.taobao.zeus.model.*;
import com.taobao.zeus.schedule.mvc.ScheduleInfoLog;
import com.taobao.zeus.socket.SocketLog;
import com.taobao.zeus.socket.protocol.Protocol.DebugMessage;
import com.taobao.zeus.socket.protocol.Protocol.ExecuteMessage;
import com.taobao.zeus.socket.protocol.Protocol.ManualMessage;
import com.taobao.zeus.socket.protocol.Protocol.Operate;
import com.taobao.zeus.socket.protocol.Protocol.Request;
import com.taobao.zeus.socket.protocol.Protocol.Response;
import com.taobao.zeus.socket.protocol.Protocol.Status;
import com.taobao.zeus.socket.worker.WorkerContext;
import com.taobao.zeus.store.JobBean;
import com.taobao.zeus.util.*;

public class WorkerBeExecute {
	public Future<Response> execute(final WorkerContext context,
			final Request req) {
		if (req.getOperate() == Operate.Debug) {
			return debug(context, req);
		} else if (req.getOperate() == Operate.Manual) {
			return manual(context, req);
		} else if (req.getOperate() == Operate.Schedule) {
			return schedule(context, req);
		}
		return null;
	}

	public Future<Response> manual(final WorkerContext context,
			final Request req) {
		ManualMessage mm = null;
		try {
			mm = ManualMessage.newBuilder().mergeFrom(req.getBody()).build();
		} catch (InvalidProtocolBufferException e1) {
		}
		SocketLog.info("receive master to worker manual request,rid="
				+ req.getRid() + ",historyId=" + mm.getHistoryId());
		final String historyId = mm.getHistoryId();
		final JobHistory history = context.getJobHistoryManager()
				.findJobHistory(historyId);
		Future<Response> f = context.getThreadPool().submit(
				new Callable<Response>() {
					public Response call() throws Exception {
						history.setExecuteHost(WorkerContext.host);
						history.setStartTime(new Date());
						context.getJobHistoryManager()
								.updateJobHistory(history);

						String date = new SimpleDateFormat("yyyy-MM-dd")
								.format(new Date());
						File direcotry = new File(Environment.getDownloadPath()
								+ File.separator + date + File.separator
								+ "manual-" + history.getId());
						if (!direcotry.exists()) {
							direcotry.mkdirs();
						}
						JobBean jb = context.getGroupManager()
								.getUpstreamJobBean(history.getJobId());

						final Job job = JobUtils.createJob(new JobContext(JobContext.MANUAL_RUN),
								jb, history, direcotry.getAbsolutePath(),
								context.getApplicationContext());
						context.getManualRunnings().put(historyId, job);

						Integer exitCode = -1;
						Exception exception = null;
						try {
							exitCode = job.run();
						} catch (Exception e) {
							exception = e;
							history.getLog().appendZeusException(e);
						} finally {
							// 发送维护邮件
							Calendar cal = Calendar.getInstance();
							int currentHour = cal.get(Calendar.HOUR_OF_DAY);
							// 晚上发钉钉告警
							if (exitCode != 0) {
//								sendMaintainMail(jb, history);

								// 异步发送邮件
								final JobDescriptor mailJob = jb.getJobDescriptor();
								new Thread(new Runnable() {
									@Override
									public void run() {
										String receiver = mailJob.getResponsibleMail();
										String[] receiverArr = MailUtil.ccMail;
										if (null != receiver && !"".equals(receiver.trim())) {
											receiverArr = receiver.trim().split(",");
										}
										String subject = "[失败]zeus任务结果";

										String mailContent = "任务id：" + mailJob.getId() + ", 任务名：" +
												mailJob.getName() + ", 记录id：" + history.getId() + ", 描述：" + mailJob.getDesc() +
												", 执行结果：失败。请检查恢复";

										// 发送邮件
										if(MailUtil.send("", "",
												receiverArr, MailUtil.ccMail, subject, mailContent, null)) {
											ScheduleInfoLog.info("send main succeed for zeus id: " + mailJob.getId());
										} else {
											ScheduleInfoLog.error("send main failed for zeus id: " + mailJob.getId(), null);
										}
									}
								}).start();

//								if (currentHour >= DingDingConfig.ALARM_START_TIME || currentHour < DingDingConfig.ALARM_END_TIME) {
//									try {
//										// 2017-05-09
//										String dingdingGroup = jb.getJobDescriptor().getProperties().get("dingding.group");
//										if (null != dingdingGroup && !"".equals(dingdingGroup.trim())) {
//											String[] ddGroupArr = dingdingGroup.trim().split(",");
//											for(String ddGroup : ddGroupArr) {
//												String webhookToken = DingDingGroup.getWebhookToken(ddGroup.trim());
//												if (null == webhookToken) continue;
//
//												JobResult jobResult = new JobResult();
//												jobResult.setJob(jb.getJobDescriptor());
//												jobResult.setResultFlag(exitCode);
//												jobResult.setWebhookToken(webhookToken);
//												JobBlockingQueue.put(jobResult);
//											}
//										} else {
//											JobResult jobResult = new JobResult();
//											jobResult.setJob(jb.getJobDescriptor());
//											jobResult.setResultFlag(exitCode);
//											jobResult.setWebhookToken(DingDingGroup.DEFAULT.getWebhookToken());
//											JobBlockingQueue.put(jobResult);
//										}
//									} catch (Exception e) {
//										history.getLog().appendZeus("###########发送钉钉消息失败###########");
//										history.getLog().appendZeusException(e);
//									}
//								}
							} else {
//								if (currentHour >= DingDingConfig.ALARM_START_TIME || currentHour < DingDingConfig.ALARM_END_TIME) {
//									// 2017-05-09
//									// 成功时根据策略是否发送钉钉
//									try {
//										JobDescriptor jobDesc = jb.getJobDescriptor();
//										String succSendFlag = jobDesc.getProperties().get("zeus.job.succ.send");
//
//										if ("true".equals(succSendFlag)) {
//											String dingdingGroup = jobDesc.getProperties().get("dingding.group");
//											if (null != dingdingGroup && !"".equals(dingdingGroup.trim())) {
//												String[] ddGroupArr = dingdingGroup.trim().split(",");
//												for(String ddGroup : ddGroupArr) {
//													String webhookToken = DingDingGroup.getWebhookToken(ddGroup.trim());
//													if (null == webhookToken) continue;
//
//													JobResult jobResult = new JobResult();
//													jobResult.setJob(jb.getJobDescriptor());
//													jobResult.setResultFlag(exitCode);
//													jobResult.setWebhookToken(webhookToken);
//													JobBlockingQueue.put(jobResult);
//												}
//											} else {
//												JobResult jobResult = new JobResult();
//												jobResult.setJob(jb.getJobDescriptor());
//												jobResult.setResultFlag(exitCode);
//												jobResult.setWebhookToken(DingDingGroup.DEFAULT.getWebhookToken());
//												JobBlockingQueue.put(jobResult);
//											}
//
//										}
//									} catch (Exception e) {
//										history.getLog().appendZeus("###########发送钉钉消息失败###########");
//										history.getLog().appendZeusException(e);
//									}
//								}
							}

							// 发送邮件
//							sendBusiMail(jb, history, exitCode);

							JobHistory jobHistory = context
									.getJobHistoryManager()
									.findJobHistory(history.getId());
							jobHistory.setEndTime(new Date());
							if (exitCode == 0) {
								jobHistory
										.setStatus(com.taobao.zeus.model.JobStatus.Status.SUCCESS);
							} else {
								jobHistory
										.setStatus(com.taobao.zeus.model.JobStatus.Status.FAILED);
							}
							context.getJobHistoryManager().updateJobHistory(
									jobHistory);
							history.getLog().appendZeus("exitCode=" + exitCode);
							context.getJobHistoryManager().updateJobHistoryLog(
									history.getId(),
									history.getLog().getContent());
							context.getManualRunnings().remove(historyId);
						}

						Status status = Status.OK;
						String errorText = "";
						if (exitCode != 0) {
							status = Status.ERROR;
						}
						if (exception != null && exception.getMessage() != null) {
							errorText = exception.getMessage();
						}
						Response resp = Response.newBuilder().setRid(
								req.getRid()).setOperate(Operate.Manual)
								.setStatus(status).setErrorText(errorText)
								.build();
						SocketLog
								.info("send manual response,manual complete,rid="
										+ req.getRid()
										+ ",historyId="
										+ historyId);
						return resp;
					}
				});
		return f;
	}

	public Future<Response> debug(final WorkerContext context, final Request req) {
		DebugMessage dm = null;
		try {
			dm = DebugMessage.newBuilder().mergeFrom(req.getBody()).build();
		} catch (InvalidProtocolBufferException e1) {
		}
		SocketLog.info("receive master to worker debug request,rid="
				+ req.getRid() + ",debugId=" + dm.getDebugId());
		final String debugId = dm.getDebugId();
		final DebugHistory history = context.getDebugHistoryManager()
				.findDebugHistory(debugId);
		Future<Response> f = context.getThreadPool().submit(
				new Callable<Response>() {
					public Response call() throws Exception {
						history.setExecuteHost(WorkerContext.host);
						history.setStartTime(new Date());
						context.getDebugHistoryManager().updateDebugHistory(
								history);

						String date = new SimpleDateFormat("yyyy-MM-dd")
								.format(new Date());
						File direcotry = new File(Environment.getDownloadPath()
								+ File.separator + date + File.separator
								+ "debug-" + history.getId());
						if (!direcotry.exists()) {
							direcotry.mkdirs();
						}
						final Job job = JobUtils.createDebugJob(
								new JobContext(JobContext.DEBUG_RUN), history, direcotry
										.getAbsolutePath(), context
										.getApplicationContext());
						context.getDebugRunnings().put(debugId, job);

						Integer exitCode = -1;
						Exception exception = null;
						try {
							exitCode = job.run();
						} catch (Exception e) {
							exception = e;
							history.getLog().appendZeusException(e);
						} finally {
							DebugHistory debugHistory = context
									.getDebugHistoryManager()
									.findDebugHistory(history.getId());
							debugHistory.setEndTime(new Date());
							if (exitCode == 0) {
								debugHistory
										.setStatus(com.taobao.zeus.model.JobStatus.Status.SUCCESS);
							} else {
								debugHistory
										.setStatus(com.taobao.zeus.model.JobStatus.Status.FAILED);
							}
							context.getDebugHistoryManager()
									.updateDebugHistory(debugHistory);
							history.getLog().appendZeus("exitCode=" + exitCode);
							context.getDebugHistoryManager()
									.updateDebugHistoryLog(history.getId(),
											history.getLog().getContent());
							context.getDebugRunnings().remove(debugId);
						}

						Status status = Status.OK;
						String errorText = "";
						if (exitCode != 0) {
							status = Status.ERROR;
						}
						if (exception != null && exception.getMessage() != null) {
							errorText = exception.getMessage();
						}
						Response resp = Response.newBuilder().setRid(
								req.getRid()).setOperate(Operate.Debug)
								.setStatus(status).setErrorText(errorText)
								.build();
						SocketLog
								.info("send debug response,debug complete,rid="
										+ req.getRid() + ",debugId=" + debugId);
						return resp;
					}
				});
		return f;
	}

	private Future<Response> schedule(final WorkerContext context,
			final Request req) {
		// 查找该job是否在运行中，如果在，响应ERROR
		// 如果不在，开始执行job，等待执行完毕后，发送完毕请求
		ExecuteMessage em = null;
		try {
			em = ExecuteMessage.newBuilder().mergeFrom(req.getBody()).build();
		} catch (InvalidProtocolBufferException e1) {
		}
		SocketLog.info("receive master to worker execute request,rid="
				+ req.getRid() + ",jobId=" + em.getJobId());
		final String jobId = em.getJobId();
		if (context.getRunnings().containsKey(jobId)) {
			SocketLog
					.info("send execute response,job is running and can't run again,rid="
							+ req.getRid() + ",jobId=" + em.getJobId());
			return context.getThreadPool().submit(new Callable<Response>() {
				public Response call() throws Exception {
					return Response.newBuilder().setRid(req.getRid())
							.setOperate(Operate.Schedule).setStatus(
									Status.ERROR).build();
				}
			});
		}
		final JobStatus js = context.getGroupManager().getJobStatus(jobId);
		final JobHistory history = context.getJobHistoryManager()
				.findJobHistory(js.getHistoryId());
		Future<Response> f = context.getThreadPool().submit(
				new Callable<Response>() {
					public Response call() throws Exception {
						history.setExecuteHost(WorkerContext.host);
						history.setStartTime(new Date());
						context.getJobHistoryManager()
								.updateJobHistory(history);

						JobBean jb = context.getGroupManager()
								.getUpstreamJobBean(history.getJobId());
						String date = new SimpleDateFormat("yyyy-MM-dd")
								.format(new Date());
						File direcotry = new File(Environment.getDownloadPath()
								+ File.separator + date + File.separator
								+ history.getId());
						if (!direcotry.exists()) {
							direcotry.mkdirs();
						}

						final Job job = JobUtils.createJob(new JobContext(JobContext.SCHEDULE_RUN),
								jb, history, direcotry.getAbsolutePath(),
								context.getApplicationContext());
						context.getRunnings().put(jobId, job);

						Integer exitCode = -1;
						Exception exception = null;
						try {
							exitCode = job.run();
						} catch (Exception e) {
							exception = e;
							history.getLog().appendZeusException(e);
						} finally {
							// 发送维护邮件
							Calendar cal = Calendar.getInstance();
							int currentHour = cal.get(Calendar.HOUR_OF_DAY);
							// 晚上发钉钉告警
							if (exitCode != 0) {
//								sendMaintainMail(jb, history);
								// 异步发送邮件
								final JobDescriptor mailJob = jb.getJobDescriptor();
								new Thread(new Runnable() {
									@Override
									public void run() {
										String receiver = mailJob.getResponsibleMail();
										String[] receiverArr = MailUtil.ccMail;
										if (null != receiver && !"".equals(receiver.trim())) {
											receiverArr = receiver.trim().split(",");
										}
										String subject = "[失败]zeus任务结果";

										String mailContent = "任务id：" + mailJob.getId() + ", 任务名：" +
												mailJob.getName() + ", 记录id：" + history.getId() + ", 描述：" + mailJob.getDesc() +
												", 执行结果：失败。请检查恢复";

										// 发送邮件
										if(MailUtil.send("", "",
												receiverArr, MailUtil.getInstance().ccMail /**/, subject, mailContent, null)) {
											ScheduleInfoLog.info("send main succeed for zeus id: " + mailJob.getId());
										} else {
											ScheduleInfoLog.error("send main failed for zeus id: " + mailJob.getId(), null);
										}
									}
								}).start();

//								if (currentHour >= DingDingConfig.ALARM_START_TIME || currentHour < DingDingConfig.ALARM_END_TIME) {
//									try {
//										// 2017-05-09
//										String dingdingGroup = jb.getJobDescriptor().getProperties().get("dingding.group");
//										if (null != dingdingGroup && !"".equals(dingdingGroup.trim())) {
//											String[] ddGroupArr = dingdingGroup.trim().split(",");
//											for(String ddGroup : ddGroupArr) {
//												String webhookToken = DingDingGroup.getWebhookToken(ddGroup.trim());
//												if (null == webhookToken) continue;
//
//												JobResult jobResult = new JobResult();
//												jobResult.setJob(jb.getJobDescriptor());
//												jobResult.setResultFlag(exitCode);
//												jobResult.setWebhookToken(webhookToken);
//												JobBlockingQueue.put(jobResult);
//											}
//										} else {
//											JobResult jobResult = new JobResult();
//											jobResult.setJob(jb.getJobDescriptor());
//											jobResult.setResultFlag(exitCode);
//											jobResult.setWebhookToken(DingDingGroup.DEFAULT.getWebhookToken());
//											JobBlockingQueue.put(jobResult);
//										}
//									} catch (Exception e) {
//										history.getLog().appendZeus("###########发送钉钉消息失败###########");
//										history.getLog().appendZeusException(e);
//									}
//								}
							} else {
								/**  发送丁叮  */
								// if (currentHour >= DingDingConfig.ALARM_START_TIME || currentHour < DingDingConfig.ALARM_END_TIME) {
								// 	// 2017-05-09
								// 	// 成功时根据策略是否发送钉钉
								// 	try {
								// 		JobDescriptor jobDesc = jb.getJobDescriptor();
								// 		String succSendFlag = jobDesc.getProperties().get("zeus.job.succ.send");
								// 		if ("true".equals(succSendFlag)) {
								// 			String dingdingGroup = jobDesc.getProperties().get("dingding.group");
								// 			if (null != dingdingGroup && !"".equals(dingdingGroup.trim())) {
								// 				String[] ddGroupArr = dingdingGroup.trim().split(",");
								// 				for(String ddGroup : ddGroupArr) {
								// 					String webhookToken = DingDingGroup.getWebhookToken(ddGroup.trim());
								// 					if (null == webhookToken) continue;
								// 					JobResult jobResult = new JobResult();
								// 					jobResult.setJob(jb.getJobDescriptor());
								// 					jobResult.setResultFlag(exitCode);
								// 					jobResult.setWebhookToken(webhookToken);
								// 					JobBlockingQueue.put(jobResult);
								// 				}
								// 			} else {
								// 				JobResult jobResult = new JobResult();
								// 				jobResult.setJob(jb.getJobDescriptor());
								// 				jobResult.setResultFlag(exitCode);
								// 				jobResult.setWebhookToken(DingDingGroup.DEFAULT.getWebhookToken());
								// 				JobBlockingQueue.put(jobResult);
								// 			}
								// 		}
								// 	} catch (Exception e) {
								// 		history.getLog().appendZeus("###########发送钉钉消息失败###########");
								// 		history.getLog().appendZeusException(e);
								// 	}
								// }
							}

							// 发送邮件
//							sendBusiMail(jb, history, exitCode);

							JobHistory jobHistory = context
									.getJobHistoryManager()
									.findJobHistory(history.getId());
							jobHistory.setEndTime(new Date());
							if (exitCode == 0) {
								jobHistory
										.setStatus(com.taobao.zeus.model.JobStatus.Status.SUCCESS);
							} else {
								jobHistory
										.setStatus(com.taobao.zeus.model.JobStatus.Status.FAILED);
							}
							context.getJobHistoryManager().updateJobHistory(
									jobHistory);
							history.getLog().appendZeus("exitCode=" + exitCode);
							context.getJobHistoryManager().updateJobHistoryLog(
									history.getId(),
									history.getLog().getContent());
							context.getRunnings().remove(jobId);
						}

						Status status = Status.OK;
						String errorText = "";
						if (exitCode != 0) {
							status = Status.ERROR;
						}
						if (exception != null) {
							errorText = exception.getMessage();
						}
						Response resp = Response.newBuilder().setRid(
								req.getRid()).setOperate(Operate.Schedule)
								.setStatus(status).setErrorText(errorText)
								.build();
						SocketLog
								.info("send execute response,execute complete,rid="
										+ req.getRid()
										+ ",jobId="
										+ history.getJobId());
						return resp;
					}
				});
		return f;
	}

	/**
	 * 发送业务邮件
	 * @param jb
	 * @param history
	 * @param exitCode
	 */
	private void sendBusiMail(JobBean jb, JobHistory history, int exitCode) {
		String zeusRunResult = "失败";
		if(exitCode == 0) {
			zeusRunResult = "成功";
		}
		String receivers = jb.getJobDescriptor().getMailReceivers();
		if (null != receivers && !"".equals(receivers.trim())) {
			String[] receiversArr = receivers.split(",");
			for(int i = 0; i < receiversArr.length; i++) {
				receiversArr[i] = receiversArr[i].trim();
			}
			String[] ccArr  = null;
			String cc = jb.getJobDescriptor().getMailCc();
			if (null != cc && !"".equals(cc)) {
				ccArr = cc.split(",");
				for(int i = 0; i < ccArr.length; i++) {
					ccArr[i] = ccArr[i].trim();
				}
			}

			String[] attachesArr  = null;
			String attaches = jb.getJobDescriptor().getMailAttaches();
			if (attaches != cc && !"".equals(attaches)) {
				attachesArr = attaches.split(",");
				for(int i = 0; i < attachesArr.length; i++) {
					attachesArr[i] = attachesArr[i].trim();
				}
			}

			String mailSubject = jb.getJobDescriptor().getMailSubject();
			mailSubject = mailSubject.replaceAll("\\[ZEUS_RUN_RESULT\\]", zeusRunResult);

			String mailContent = jb.getJobDescriptor().getMailContent();
			mailContent = mailContent.replaceAll("\\[ZEUS_RUN_RESULT\\]", zeusRunResult);

			history.getLog().appendZeus("###################发送邮件##############");
			history.getLog().appendZeus("开始发送邮件......");
			// 发送邮件
			if(MailUtil.send("", "",
					receiversArr, ccArr, mailSubject, mailContent, attachesArr)) {
				history.getLog().appendZeus("发送邮件成功");
			} else {
				history.getLog().appendZeus("发送邮件失败");
			}


			history.getLog().appendZeus("结束发送邮件......");
		}
	}


	/**
	 * 发送执行结果邮件
	 * @param jb
	 * @param history
	 */
	private void sendMaintainMail(JobBean jb, JobHistory history) {
		String receiver = jb.getJobDescriptor().getResponsibleMail();
		String[] receiverArr = MailUtil.ccMail;
		if (null != receiver && !"".equals(receiver.trim())) {
			receiverArr = receiver.trim().split(",");
		}

		history.getLog().appendZeus("###################发送邮件给责任人##############");

		String subject = "[失败]zeus任务结果";

		String mailContent = "任务id：" + jb.getJobDescriptor().getId() + ", 任务名：" +
				jb.getJobDescriptor().getName() + ", 描述：" + jb.getJobDescriptor().getDesc() +
				", 执行结果：失败。请检查恢复";

		// 发送邮件
		if(MailUtil.send("", "",
				receiverArr, MailUtil.ccMail, subject, mailContent, null)) {
			history.getLog().appendZeus("发送邮件成功");
		} else {
			history.getLog().appendZeus("发送邮件失败");
		}
	}
}
