package com.googlecode.jazure.examples.rate.inner;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jazure.sdk.aggregator.Aggregator;
import com.googlecode.jazure.sdk.task.FailedResult;
import com.googlecode.jazure.sdk.task.Result;
import com.googlecode.jazure.sdk.task.TaskInvocation;

public class HiltonAggregator implements Aggregator<HiltonJobConfig> {
	
	private static Logger logger = LoggerFactory.getLogger(HiltonAggregator.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void aggregate(TaskInvocation invocation) {
		logger.debug("Received hilton result : " + invocation);
		Result result = invocation.getResult();
		assertResultType(result, HiltonResult.class, FailedResult.class);
	}

	private void assertResultType(Result result, Class<? extends Result>... possibleTypes) {
		if (possibleTypes == null) {
			return;
		}
		
		for (Class<? extends Result> type : possibleTypes) {
			if (result.getClass() == type) {
				return;
			}
		}
		
		throw new IllegalArgumentException(result.getClass() + " should be one of these types [ " + StringUtils.join(possibleTypes, ",") + "]");
	}

	@Override
	public void aggregateCorrelated(HiltonJobConfig jobConfig, Object correlationKey, Collection<TaskInvocation> results) {
		logger.debug("Completed of job " + jobConfig.getId() + " and results " + results);
	}

	@Override
	public boolean aggregateCorrelatedSupported(HiltonJobConfig jobConfig) {
		return true;
	}

	@Override
	public boolean aggregateSupported(TaskInvocation result) {
		return true;
	}

	
}
