package com.googlecode.jazure.sdk.task;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.jazure.sdk.core.ProjectConfiguration;
import com.googlecode.jazure.sdk.job.JobConfig;

public class TaskInvocation implements Serializable {

	private static final long serialVersionUID = -4491800548378004327L;

	private final TaskMetaData metaData;
	private final TimeTrace timeTrace;
	private final String id;
	private final TaskCorrelation correlation;
	private final Task task;
	private Status status;
	private Result result;

	
	public TaskInvocation(
		TaskMetaData metaData,
		String id,
		TaskCorrelation correlation, 
		Task task,
		Status status,
		Result result,
		TimeTrace timeTrace) {
		
		this.metaData = metaData;
		this.id = id;
		this.correlation = correlation;
		this.task = task;
		this.status = status;
		this.result = result;
		this.timeTrace = timeTrace;
	}
	
	
	public static final class TaskMetaData implements Serializable {
		
		private static final long serialVersionUID = 4929676267338604605L;

		private final ProjectConfiguration projectConfiguration;
		private final JobConfig jobConfig;
		private final String taskQueue;
		private final String resultQueue;
		private String gridWorker;
		
		public TaskMetaData(
			ProjectConfiguration projectConfiguration,
			JobConfig jobConfig, 
			String taskQueue, 
			String resultQueue,
			String gridWorker) {

			this.projectConfiguration = projectConfiguration;
			this.jobConfig = jobConfig;
			this.taskQueue = taskQueue;
			this.resultQueue = resultQueue;
			this.gridWorker = gridWorker;
		}

		public ProjectConfiguration getProjectConfiguration() {
			return projectConfiguration;
		}

		public JobConfig getJobConfig() {
			return jobConfig;
		}

		public String getTaskQueue() {
			return taskQueue;
		}

		public String getResultQueue() {
			return resultQueue;
		}

		public String getGridWorker() {
			return gridWorker;
		}

		public void setGridWorker(String gridWorker) {
			this.gridWorker = gridWorker;
		}
		
	}
	
	public static final class TimeTrace implements Serializable {
		
		private static final long serialVersionUID = 4835986153712206186L;

		private final Date createdTime;
		private Date lastModifiedTime;
		
		public TimeTrace(Date createdTime) {
			this.createdTime = createdTime;
		}
		
		public Date getLastModifiedTime() {
			return lastModifiedTime;
		}

		public TimeTrace setLastModifiedTime(Date lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public Date getCreatedTime() {
			return createdTime;
		}
	}
	
	public String getId() {
		return id;
	}

	public Task getTask() {
		return task;
	}

	public Status getStatus() {
		return status;
	}

	public Result getResult() {
		return result;
	}

	public TaskInvocation setResult(Result result) {
		this.result = result;
		return this;
	}

	public TaskInvocation replaceWith(TaskInvocation invocation) {
		this.status = invocation.getStatus();
		this.result = invocation.getResult();
		return this.timeUpdated();
	}

	
	public TaskInvocation successful() {
		this.status = Status.SUCCESSFUL;
		return this.timeUpdated();
	}

	public TaskInvocation failed() {
		this.status = Status.FAILED;
		return this.timeUpdated();
	}

	public boolean isCompleted() {
		return status == Status.SUCCESSFUL || status == Status.FAILED;
	}

	public TaskInvocation executing() {
		this.status = Status.EXECUTING;
		return this.timeUpdated();
	}

	public TaskInvocation completed() {
		if (result.successful()) {
			return successful();
		} 
		return failed();
	}
	
	private TaskInvocation timeUpdated() {
		this.getTimeTrace().setLastModifiedTime(new Date());
		return this;
	}

	public TaskMetaData getMetaData() {
		return metaData;
	}

	public TimeTrace getTimeTrace() {
		return timeTrace;
	}

	public String getCorrelationId() {
		return correlation.getCorrelationId();
	}

	public TaskCorrelation getCorrelation() {
		return correlation;
	}
	
	
	

	
}
