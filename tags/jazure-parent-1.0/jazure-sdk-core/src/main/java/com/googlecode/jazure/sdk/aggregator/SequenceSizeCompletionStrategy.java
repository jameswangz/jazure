package com.googlecode.jazure.sdk.aggregator;

import java.util.Collection;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public class SequenceSizeCompletionStrategy implements CompletionStrategy {

	@Override
	public boolean isComplete(Collection<TaskInvocation> invocations) {
		if (invocations.isEmpty()) {
			return false;
		}
		return invocations.size() >= oneof(invocations).getCorrelation().getSequenceSize();
	}

	private TaskInvocation oneof(Collection<TaskInvocation> taskInvocations) {
		return taskInvocations.iterator().next();
	}

}
