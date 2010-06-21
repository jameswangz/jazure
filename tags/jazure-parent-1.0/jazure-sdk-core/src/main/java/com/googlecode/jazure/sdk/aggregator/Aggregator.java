package com.googlecode.jazure.sdk.aggregator;

import java.util.Collection;

import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.task.TaskInvocation;


public interface Aggregator<T extends JobConfig> {
	
	boolean aggregateSupported(TaskInvocation result);
	
	void aggregate(TaskInvocation result);

	boolean aggregateCorrelatedSupported(T jobConfig);
	
	void aggregateCorrelated(T jobConfig, Object correlationKey, Collection<TaskInvocation> results);

}
