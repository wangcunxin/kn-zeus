package com.taobao.zeus.socket.worker;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.taobao.zeus.util.RunShell;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.taobao.zeus.jobs.Job;
import com.taobao.zeus.store.DebugHistoryManager;
import com.taobao.zeus.store.GroupManager;
import com.taobao.zeus.store.JobHistoryManager;

public class WorkerContext {
	private static final Logger log  = LoggerFactory.getLogger(WorkerContext.class);
	public static String host;
	private static Integer cpuCoreNum;
	private static final String findCpuCoreNumber="grep 'model name' /proc/cpuinfo | wc -l";
	public static final Float MaxNetBps = 3 * 1024 * 1024 * 1024f;
	private static Long lastNetInputBits;
	private static Long lastNetOutputBits;
	public static final String getNetInOutBytes="grep eth0 /proc/net/dev |awk '{print $2 \",\" $10}'";
	private volatile Float currentNetRate = 0.5f;
	static{
		try {
			host=InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			//ignore
		}

		String os=System.getProperties().getProperty("os.name");
		if(os!=null && (os.startsWith("win") || os.startsWith("Win") || os.startsWith("Mac"))){
			//ignore
		}
		else{
			try {
				RunShell runShell = new RunShell(findCpuCoreNumber);
				if(runShell.run() == 0){
					String result = runShell.getResult();
					if (result != null) {
						cpuCoreNum = Integer.valueOf(result);
					}

				}

				 RunShell shell = new RunShell(getNetInOutBytes);
				 if(shell.run() == 0){
					String result = shell.getResult();
					if (result != null) {
						String[] netBytesNumArr = result.split(",");
						lastNetInputBits = Long.valueOf(netBytesNumArr[0]) * 8;
						lastNetOutputBits = Long.valueOf(netBytesNumArr[1]) * 8;
					}
				 }

			} catch (Exception e) {
				log.error("error",e);
			}
		}
	}
	private String serverHost;
	private Channel serverChannel;
	private Map<String, Job> runnings=new ConcurrentHashMap<String, Job>();
	private Map<String, Job> manualRunnings=new ConcurrentHashMap<String, Job>();
	private Map<String,Job> debugRunnings=new ConcurrentHashMap<String, Job>();
	private WorkerHandler handler;
	private ClientWorker clientWorker;
	private ExecutorService threadPool=Executors.newCachedThreadPool();
	private ApplicationContext applicationContext;
	public String getServerHost() {
		return serverHost;
	}
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	public Channel getServerChannel() {
		return serverChannel;
	}
	public void setServerChannel(Channel serverChannel) {
		this.serverChannel = serverChannel;
	}
	public Map<String, Job> getRunnings() {
		return runnings;
	}
	public void setRunnings(Map<String, Job> runnings) {
		this.runnings = runnings;
	}
	public WorkerHandler getHandler() {
		return handler;
	}
	public void setHandler(WorkerHandler handler) {
		this.handler = handler;
	}
	public ExecutorService getThreadPool() {
		return threadPool;
	}
	public void setThreadPool(ExecutorService threadPool) {
		this.threadPool = threadPool;
	}
	public ClientWorker getClientWorker() {
		return clientWorker;
	}
	public void setClientWorker(ClientWorker clientWorker) {
		this.clientWorker = clientWorker;
	}
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	public JobHistoryManager getJobHistoryManager() {
		return (JobHistoryManager) applicationContext.getBean("jobHistoryManager");
	}
	public DebugHistoryManager getDebugHistoryManager(){
		return (DebugHistoryManager)applicationContext.getBean("debugHistoryManager");
	}
	public GroupManager getGroupManager() {
		return (GroupManager) applicationContext.getBean("groupManager");
	}
	public Map<String, Job> getDebugRunnings() {
		return debugRunnings;
	}
	public Map<String, Job> getManualRunnings() {
		return manualRunnings;
	}
	@Override
	public String toString() {
		return "WorkerContext [serverHost=" + serverHost + ", serverChannel="
				+ serverChannel + ", runnings=" + runnings
				+ ", manualRunnings=" + manualRunnings + ", debugRunnings="
				+ debugRunnings + ", handler=" + handler + ", clientWorker="
				+ clientWorker + ", threadPool=" + threadPool
				+ ", applicationContext=" + applicationContext + "]";
	}

	public static Integer getCpuCoreNum() {
		return cpuCoreNum;
	}

	public static String getFindCpuCoreNumber() {
		return findCpuCoreNumber;
	}

	public static Long getLastNetInputBits() {
		return lastNetInputBits;
	}

	public static Long getLastNetOutputBits() {
		return lastNetOutputBits;
	}

	public static void setLastNetInputBits(Long lastNetInputBits) {
		WorkerContext.lastNetInputBits = lastNetInputBits;
	}

	public static void setLastNetOutputBits(Long lastNetOutputBits) {
		WorkerContext.lastNetOutputBits = lastNetOutputBits;
	}

	public Float getCurrentNetRate() {
		return currentNetRate;
	}

	public void setCurrentNetRate(Float currentNetRate) {
		this.currentNetRate = currentNetRate;
	}
}
