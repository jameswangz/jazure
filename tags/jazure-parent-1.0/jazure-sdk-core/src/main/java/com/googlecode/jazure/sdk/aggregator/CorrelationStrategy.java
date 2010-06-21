package com.googlecode.jazure.sdk.aggregator;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public interface CorrelationStrategy {

	Object getCorrelationKey(TaskInvocation result);

}
