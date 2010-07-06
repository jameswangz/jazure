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

/**
 * Facade interface of JAzure enterprise side to allow client configure and control everything.
 * @author James Wang
 *
 */
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
	 * Rebuild all jobs by job configurations, a rebuild operation is expected after adding or removing job configurations
	 * to synchronize jobs with configurations.
	 * @return console
	 */
	Console rebuildJobs();
	
	TaskTracker getTaskTracker();
	
	/**
	 * Determine whether a task can be re-executed, this depends on the following conditions:
	 * <ul>
	 * <li>Job that task belongs to must exists.</li>
	 * <li>Job that task belongs to must running.</li>
	 * <li>Status of task must be completed.</li>
	 * </ul>
	 * @param invocation the invocation
	 * @return whether task is re-executable
	 * @see Status#isCompleted()
	 */
	boolean reExecutable(TaskInvocation invocation);
	
	/**
	 * Convenient method to execute a task separately, a task can be re-executed only when {@link #reExecutable(TaskInvocation)} returns true.
	 * @param invocation the invocation
	 * @return console
	 * @throws JobNotFoundException if can't get job this task belongs to
	 * @throws JobNotRunningException if the job this bask belongs to is not running
	 * @see #reExecutable(TaskInvocation)
	 * @see Job#executeTask(TaskInvocation)
	 */
	Console executeTask(TaskInvocation invocation) throws JobNotFoundException, JobNotRunningException;

}
