package com.googlecode.jazure.sdk.core;

import java.util.List;

import com.googlecode.jazure.sdk.endpoint.QueueStorageEndpoint;
import com.googlecode.jazure.sdk.job.Job;
import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.job.eventdriven.EventDrivenJobBuilder;
import com.googlecode.jazure.sdk.job.exception.DuplicateJobException;
import com.googlecode.jazure.sdk.job.exception.JobAlreadyRunningException;
import com.googlecode.jazure.sdk.job.exception.JobNotFoundException;
import com.googlecode.jazure.sdk.job.exception.JobNotRunningException;
import com.googlecode.jazure.sdk.job.polling.PollingJobBuilder;
import com.googlecode.jazure.sdk.job.polling.PollingJobConfig;
import com.googlecode.jazure.sdk.lifecycle.LifeCycle;
import com.googlecode.jazure.sdk.task.Status;
import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.task.storage.TaskStorage;
import com.googlecode.jazure.sdk.task.tracker.TaskTracker;

public interface Console extends LifeCycle {

	Console configProject(ProjectConfiguration projectConfiguration);
	
	ProjectConfiguration projectConfiguration();
	
	Console storeTaskIn(TaskStorage taskStorage);

	Console connectBy(QueueStorageEndpoint queueStorageEndpoint);
	
	<T extends PollingJobConfig> PollingJobBuilder<T> addPollingJobConfig(T jobConfig) throws DuplicateJobException;
	
	<T extends JobConfig> EventDrivenJobBuilder<T> addEventDrivenJobConfig(T jobConfig) throws DuplicateJobException;
	
	Console removeJobConfig(JobConfig jobConfig) throws JobNotFoundException, JobAlreadyRunningException;

	<T extends JobConfig> Job<T> getJob(T jobConfig) throws JobNotFoundException;
	
	List<Job<?>> getJobs();
	
	/**
	 * Rebuild all jobs by job configurations, if you add or remove job configurations after console started,
	 * you must run rebuild to synchronize jobs with configurations.
	 * @return console
	 */
	Console rebuildJobs();
	
	TaskTracker getTaskTracker();
	
	/**
	 * Convenient method to execute a task separately, a task can be re-executed after success or failed.
	 * @param invocation the invocation
	 * @return console
	 * @throws JobNotFoundException if can't get job this task belong to
	 * @throws JobNotRunningException if the job this bask belong to is not running
	 * @see Job#executeTask(TaskInvocation)
	 * @see #getJob(JobConfig)
	 * @see Status
	 */
	Console executeTask(TaskInvocation invocation) throws JobNotFoundException, JobNotRunningException;

}
