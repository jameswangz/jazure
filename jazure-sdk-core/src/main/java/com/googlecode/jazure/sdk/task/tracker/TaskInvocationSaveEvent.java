package com.googlecode.jazure.sdk.task.tracker;

import java.util.EventObject;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public class TaskInvocationSaveEvent extends EventObject {

	private static final long serialVersionUID = -1490903817328030042L;

	public TaskInvocationSaveEvent(TaskInvocation invocation) {
		super(invocation);
	}
	
	@Override
	public TaskInvocation getSource() {
		return (TaskInvocation) source;
	}
	
}
