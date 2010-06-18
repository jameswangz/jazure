package com.googlecode.jazure.sdk.aggregator;

import java.util.Collection;

import com.googlecode.jazure.sdk.job.SimpleJobConfig;
import com.googlecode.jazure.sdk.task.TaskInvocation;

public class SimpleAggregator implements Aggregator<SimpleJobConfig> {

	private boolean aggregateCollelatedCalled;

	@Override
	public void aggregate(TaskInvocation result) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void aggregateCorrelated(SimpleJobConfig jobConfig, Object correlationKey, Collection<TaskInvocation> results) {
		this.aggregateCollelatedCalled = true;
	}

	@Override
	public boolean aggregateCorrelatedSupported(SimpleJobConfig jobConfig) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean aggregateSupported(TaskInvocation result) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public boolean aggregateCollelatedCalled() {
		return aggregateCollelatedCalled;
	}

}
