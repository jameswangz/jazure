package com.googlecode.jazure.sdk.task.tracker;

import java.util.EventObject;

import com.google.inject.Inject;
import com.googlecode.jazure.sdk.event.EventListener;
import com.googlecode.jazure.sdk.task.TaskInvocation;

public class TaskInvocationListener implements EventListener {

	private WriteableTaskTracker taskTracker;
	
	@Inject
	public TaskInvocationListener(WriteableTaskTracker taskTracker) {
		this.taskTracker = taskTracker;
	}

	@Override
	public void onEvent(EventObject event) {
		if (TaskInvocationSaveEvent.class.isInstance(event)) {
			TaskInvocation invocation = ((TaskInvocationSaveEvent) event).getSource();
			taskTracker.save(invocation);
		} else if (TaskInvocationUpdateEvent.class.isInstance(event)) {
			TaskInvocation invocation = ((TaskInvocationUpdateEvent) event).getSource();
			taskTracker.update(invocation);
		}
	}

}
