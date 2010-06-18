package com.googlecode.jazure.sdk.aggregator;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public class DefaultCorrelationStrategy implements CorrelationStrategy {

	@Override
	public Object getCorrelationKey(TaskInvocation result) {
		return result.getCorrelationId();
	}

}
