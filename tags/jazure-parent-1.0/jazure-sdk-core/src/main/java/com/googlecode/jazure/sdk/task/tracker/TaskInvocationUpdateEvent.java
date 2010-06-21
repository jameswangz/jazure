package com.googlecode.jazure.sdk.task.tracker;

import java.util.EventObject;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public class TaskInvocationUpdateEvent extends EventObject {

	private static final long serialVersionUID = 1479495315876186324L;

	public TaskInvocationUpdateEvent(TaskInvocation taskInvocation) {
		super(taskInvocation);
	}

	@Override
	public TaskInvocation getSource() {
		return (TaskInvocation) source;
	}
	
}
