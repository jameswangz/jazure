package com.googlecode.jazure.sdk.aggregator;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.googlecode.jazure.sdk.job.SimpleJobConfig;
import com.googlecode.jazure.sdk.task.TaskInvocation;

public class SimpleCounterAggregator implements Aggregator<SimpleJobConfig> {

	private AtomicInteger counter = new AtomicInteger();
	
	@Override
	public void aggregate(TaskInvocation result) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void aggregateCorrelated(SimpleJobConfig jobConfig, Object correlationKey, Collection<TaskInvocation> results) {
		counter.incrementAndGet();
	}

	@Override
	public boolean aggregateCorrelatedSupported(SimpleJobConfig jobConfig) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean aggregateSupported(TaskInvocation result) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public Integer aggregateCorrelatedCalledTimes() {
		return counter.intValue();
	}

}
