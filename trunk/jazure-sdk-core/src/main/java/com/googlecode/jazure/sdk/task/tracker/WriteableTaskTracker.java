package com.googlecode.jazure.sdk.task.tracker;

import java.util.Iterator;

import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.task.storage.TaskStorage;

public class WriteableTaskTracker implements TaskTracker {

	private final TaskStorage taskStorage;

	public WriteableTaskTracker(TaskStorage taskStorage) {
		this.taskStorage = taskStorage;
	}

	@Override
	public PaginatedList<TaskInvocation> getTasks(TaskCondition condition, ConditionPostFilter filter) {
		PaginatedList<TaskInvocation> tasks = taskStorage.getTasks(condition);
		
		for (Iterator<TaskInvocation> iterator  = tasks.iterator(); iterator.hasNext();) {
			TaskInvocation next = iterator.next();
			if (!filter.accept(next)) {
				iterator.remove();
			}
		}
		
		return tasks;
	}

	public void save(TaskInvocation invocation) {
		taskStorage.save(invocation);
	}

	public void update(TaskInvocation invocation) {
		taskStorage.update(invocation);
	}

	@Override
	public TaskInvocation load(String id) throws TaskRetrievalFailureException {
		return taskStorage.load(id);
	}

}
