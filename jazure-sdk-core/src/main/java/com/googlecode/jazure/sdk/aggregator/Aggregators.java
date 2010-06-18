package com.googlecode.jazure.sdk.aggregator;

import java.util.Collection;

import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.task.TaskInvocation;

public abstract class Aggregators {
	
	public static <T extends JobConfig> Aggregator<T> emptyAggregator() {
		return new Aggregator<T>() {
			@Override
			public boolean aggregateSupported(TaskInvocation result) {
				return false;
			}
			
			@Override
			public boolean aggregateCorrelatedSupported(T jobConfig) {
				return false;
			}
			
			@Override
			public void aggregateCorrelated(T jobConfig, Object correlationKey, Collection<TaskInvocation> results) {
			}
			
			@Override
			public void aggregate(TaskInvocation result) {
			}
		};
	}
	
}
