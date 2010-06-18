package com.googlecode.jazure.sdk.aggregator;

import java.util.Collection;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public abstract class FineGrainedStrategy implements CorrelationStrategy, CompletionStrategy {

	public static final String CORRELATION_KEY_SEPRATOR = ":";

	protected abstract Object correlationKeyAppended(TaskInvocation result);
	protected abstract int correlatedTasksSize(TaskInvocation anyTask);
	
	@Override
	public Object getCorrelationKey(TaskInvocation result) {
		return (result.getCorrelationId() + CORRELATION_KEY_SEPRATOR + correlationKeyAppended(result)).intern();
	}

	@Override
	public boolean isComplete(Collection<TaskInvocation> taskInvocations) {
		TaskInvocation anyTask = taskInvocations.iterator().next();
		return correlatedTasksSize(anyTask) == taskInvocations.size();
	}

}
