package com.googlecode.jazure.sdk.aggregator;

import java.util.ArrayList;
import java.util.Collection;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public class TaskInvocationBarrier {

	private boolean complete = false;
	private Collection<TaskInvocation> invocations = new ArrayList<TaskInvocation>();

	public void addIfNotCompleted(TaskInvocation invocation) {
		if (complete) {
			return;
		}
		invocations.add(invocation);
	}

	public boolean processing() {
		return !complete && !invocations.isEmpty();
	}
	
	public Collection<TaskInvocation> getInvocations() {
		return invocations;
	}

	public void completed() {
		complete = true;
	}

	public boolean isComplete() {
		return complete;
	}
	
}
