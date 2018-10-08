package com.taobao.zeus.store.mysql.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "zeus_job")
public class JobPersistence implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * 是否开启调度 1:true 0:false
	 */
	@Column
	private Integer auto = 0;
	/**
	 * 1:独立Job 2：有依赖的Job
	 */
	@Column(name = "schedule_type")
	private Integer scheduleType;
	/**
	 * 运行的类型，比如Shell， Hive Mapreduce
	 */
	@Column(name = "run_type")
	private String runType;
	@Column(length=4096)
	private String configs;
	@Column(name = "cron_expression")
	private String cronExpression;
	@Column
	private String dependencies;

	@Column(nullable = false)
	private String name;
	
	@Column
	private String descr;
	
	@Column(name = "group_id", nullable = false)
	private Integer groupId;
	
	@Column(nullable = false)
	private String owner;
	
	@Column(length=4096)
	private String resources;
	
	@Column(length=4096)
	private String script;
	
	@Column(name = "gmt_create", nullable = false)
	private Date gmtCreate = new Date();
	
	@Column(name = "gmt_modified", nullable = false)
	private Date gmtModified = new Date();
	
	@Column(name = "history_id")
	private Long historyId;
	
	@Column
	private String status;
	
	@Column(name = "ready_dependency", length=4096)
	private String readyDependency;
	
	@Column(name = "pre_processers")
	private String preProcessers;
	
	@Column(name = "post_processers")
	private String postProcessers;
	
	@Column(name = "timezone")
	private String timezone;
	
	@Column(name = "start_time", nullable = true)
	private Date startTime;
	
	@Column(name = "start_timestamp")
	private long startTimestamp;
	
	@Column(name = "offset")
	private int offset;
	
	@Column(name = "last_end_time")
	private Date lastEndTime;
	
	@Column(name = "last_result")
	private String lastResult;
	
	@Column(name="statis_start_time")
	private Date statisStartTime;
	
	@Column(name="statis_end_time")
	private Date statisEndTime;
	
	@Column(name="cycle")
	private String cycle;
	
	@Column(name="host")
	private String host;

	@Column(name="mail_receivers")
	private String mail_receivers;

	@Column(name="mail_cc")
	private String mail_cc;

	@Column(name="mail_subject")
	private String mail_subject;

	@Column(name="mail_attaches")
	private String mail_attaches;

	@Column(name="mail_content", length=100000)
	private String mail_content;

	@Column(name="responsible_mail")
	private String responsible_mail;

	@Column(name="task_id")
	private int taskId = -1;
	
	public String getConfigs() {
		return configs;
	}

	public void setConfigs(String configs) {
		this.configs = configs;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDependencies() {
		return dependencies;
	}

	public void setDependencies(String dependencies) {
		this.dependencies = dependencies;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Integer getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(Integer scheduleType) {
		this.scheduleType = scheduleType;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getRunType() {
		return runType;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public Integer getAuto() {
		return auto;
	}

	public void setAuto(Integer auto) {
		this.auto = auto;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReadyDependency() {
		return readyDependency;
	}

	public void setReadyDependency(String readyDependency) {
		this.readyDependency = readyDependency;
	}

	public String getPreProcessers() {
		return preProcessers;
	}

	public void setPreProcessers(String preProcessers) {
		this.preProcessers = preProcessers;
	}

	public String getPostProcessers() {
		return postProcessers;
	}

	public void setPostProcessers(String postProcessers) {
		this.postProcessers = postProcessers;
	}

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Date getLastEndTime() {
		return lastEndTime;
	}

	public void setLastEndTime(Date lastEndTime) {
		this.lastEndTime = lastEndTime;
	}

	public String getLastResult() {
		return lastResult;
	}

	public void setLastResult(String lastResult) {
		this.lastResult = lastResult;
	}

	public Date getStatisStartTime() {
		return statisStartTime;
	}

	public void setStatisStartTime(Date statisStartTime) {
		this.statisStartTime = statisStartTime;
	}

	public Date getStatisEndTime() {
		return statisEndTime;
	}

	public void setStatisEndTime(Date statisEndTime) {
		this.statisEndTime = statisEndTime;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getMail_receivers() {
		return mail_receivers;
	}

	public String getMail_cc() {
		return mail_cc;
	}

	public String getMail_subject() {
		return mail_subject;
	}

	public String getMail_attaches() {
		return mail_attaches;
	}

	public void setMail_receivers(String mail_receivers) {
		this.mail_receivers = mail_receivers;
	}

	public void setMail_cc(String mail_cc) {
		this.mail_cc = mail_cc;
	}

	public void setMail_subject(String mail_subject) {
		this.mail_subject = mail_subject;
	}

	public void setMail_attaches(String mail_attaches) {
		this.mail_attaches = mail_attaches;
	}

	public String getMail_content() {
		return mail_content;
	}

	public void setMail_content(String mail_content) {
		this.mail_content = mail_content;
	}

	public String getResponsible_mail() {
		return responsible_mail;
	}

	public void setResponsible_mail(String responsible_mail) {
		this.responsible_mail = responsible_mail;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
}
