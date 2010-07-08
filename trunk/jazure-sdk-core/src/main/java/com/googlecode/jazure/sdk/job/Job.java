package com.googlecode.jazure.sdk.job;

import com.googlecode.jazure.sdk.aggregator.Aggregator;
import com.googlecode.jazure.sdk.job.exception.JobNotRunningException;
import com.googlecode.jazure.sdk.lifecycle.LifeCycle;
import com.googlecode.jazure.sdk.task.TaskInvocation;

public interface Job<T extends JobConfig> extends LifeCycle {
	
	T getJobConfig();
		
	Aggregator<? super T> getAggregator();

	Job<T> executeTask(TaskInvocation invocation) throws JobNotRunningException;

}
