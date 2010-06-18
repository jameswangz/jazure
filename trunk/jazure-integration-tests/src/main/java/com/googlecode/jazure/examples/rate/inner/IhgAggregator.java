package com.googlecode.jazure.examples.rate.inner;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jazure.sdk.aggregator.Aggregator;
import com.googlecode.jazure.sdk.task.TaskInvocation;


public class IhgAggregator implements Aggregator<IhgJobConfig> {

	private static Logger logger = LoggerFactory.getLogger(IhgAggregator.class);
	
	@Override
	public void aggregate(TaskInvocation invocation) {
		logger.debug("Received ihg result : " + invocation);
	}

	@Override
	public void aggregateCorrelated(IhgJobConfig jobConfig, Object correlationKey, Collection<TaskInvocation> results) {
		logger.debug("Completed of job " + jobConfig.getId() + " and results " + results);
	}

	@Override
	public boolean aggregateCorrelatedSupported(IhgJobConfig jobConfig) {
		return false;
	}

	@Override
	public boolean aggregateSupported(TaskInvocation result) {
		return true;
	}

}
