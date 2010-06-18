package com.googlecode.jazure.sdk.aggregator;

import java.util.Collection;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public interface CompletionStrategy {

	boolean isComplete(Collection<TaskInvocation> taskInvocations);

}
